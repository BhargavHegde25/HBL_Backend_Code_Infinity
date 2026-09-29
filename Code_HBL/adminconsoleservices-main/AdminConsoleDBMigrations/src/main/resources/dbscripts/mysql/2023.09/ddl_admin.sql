DROP PROCEDURE IF EXISTS `create_approvalmatrixrule_for_featureaction_and_limittype_proc`;
DELIMITER $$
CREATE PROCEDURE `create_approvalmatrixrule_for_featureaction_and_limittype_proc`(
  	IN `_contractId`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_coreCustomerId`    VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionId`   VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_accountIds`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_limitTypeId`       VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_limitValuesJSON`   TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SELECT `companyLegalUnit` INTO @legalEntityId FROM `contractcorecustomers` WHERE `coreCustomerId` = @coreCustomerId AND `contractId` = `_contractId`;
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @isAccountLevelFeature = (SELECT DISTINCT(`isAccountLevel`) FROM `featureaction` WHERE `id` = `_featureActionId` AND `companyLegalUnit` = @legalEntityId) ;
    SET @isGroupMatrix = (SELECT DISTINCT(`isGroupLevel`) FROM `approvalmode` WHERE `coreCustomerId` = `_coreCustomerId` AND `contractId` = `_contractId`);
    IF `_accountIds` IS NULL OR `_accountIds` = '' THEN
		SET @isAccountLevelUpdate = 0;
		SET @accountIds = (SELECT GROUP_CONCAT(`accountId`) FROM `contractaccounts` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
	ELSE
		SET @accountIds = `_accountIds`;
	END IF;
    IF @isAccountLevelFeature = 0 THEN
		-- customer-level feature action rule is being added/updated, hence, no account ids are associated
        SET @accountIds = '';
	END IF;
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    SET @currency = (SELECT DISTINCT(`currency`) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
    IF @isAccountLevelUpdate = 0 THEN
		-- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
        -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
        SET @templateIdsForForceDelete = (SELECT GROUP_CONCAT(`id`)  FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId` AND `actionId` = `_featureActionId` AND `isGroupMatrix` = @isGroupMatrix AND `limitTypeId` = `_limitTypeId`);
        IF @templateIdsForForceDelete IS NOT NULL THEN
			SET @templateIds = CONCAT('\'', REPLACE(@templateIdsForForceDelete, ',', '\',\''), '\'');
			SET @sqlStmt = CONCAT('DELETE FROM `customerapprovalmatrixtemplate` WHERE `approvalMatrixId` IN (', @templateIds, ')');
			PREPARE STMT FROM @sqlStmt;
			EXECUTE STMT;
			SET @sqlStmt = CONCAT('DELETE FROM `signatorygroupmatrixtemplate` WHERE `approvalMatrixId` IN (', @templateIds, ')');
			PREPARE STMT FROM @sqlStmt;
			EXECUTE STMT;
			DEALLOCATE PREPARE STMT;
			DELETE FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId` AND `actionId` = `_featureActionId` AND `isGroupMatrix` = @isGroupMatrix AND `limitTypeId` = `_limitTypeId`;
		END IF;
	END IF;
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0 THEN
		UPDATE `approvalmatrix` SET `softdeleteflag` = 1 WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId` AND `actionId` = `_featureActionId` AND `isGroupMatrix` = @isGroupMatrix AND `limitTypeId` = `_limitTypeId`;
	ELSE
		UPDATE `approvalmatrix` SET `softdeleteflag` = 1 WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId` AND `actionId` = `_featureActionId` AND `isGroupMatrix` = @isGroupMatrix AND `limitTypeId` = `_limitTypeId` AND FIND_IN_SET(`accountId`, @accountIds);
    END IF;
    SET @limitsLength = JSON_LENGTH(`_limitValuesJSON`);
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    LIMITS_INIT : LOOP
		IF @limitIndex = @limitsLength THEN
			LEAVE LIMITS_INIT;
		ELSE
			SET @currentLimit = JSON_EXTRACT(`_limitValuesJSON`, CONCAT('$[', @limitIndex, ']'));
            SET @limitIndex = @limitIndex + 1;
            SET @approvalRuleId = JSON_UNQUOTE(JSON_EXTRACT(@currentLimit, '$.approvalruleId'));
            SET @lowerLimit = JSON_EXTRACT(@currentLimit, '$.lowerlimit');
            SET @upperLimit = JSON_EXTRACT(@currentLimit, '$.upperlimit');
            SET @groupList = JSON_UNQUOTE(JSON_EXTRACT(@currentLimit, '$.groupList'));
            SET @groupRule = JSON_UNQUOTE(JSON_EXTRACT(@currentLimit, '$.groupRule'));
            SET @approvers = JSON_EXTRACT(@currentLimit, '$.approvers');
            -- error handling
            -- if lowerLimit or upperLimit is null, then return an error signal
            -- if group mode, then if groupList or groupRule is null, then return error signal
            -- if user-based mode, then if approvalRuleId or approvers are null, then return error
            
            IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1 THEN
				SET @approvalRuleId = 'NO_APPROVAL';
			END IF;
            IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]') THEN
				SET @approvalRuleId = 'NO_APPROVAL';
			END IF;
            IF (@approvers IS NOT NULL AND JSON_UNQUOTE(@approvers) = '[]') THEN
                SET @approvalRuleId = 'NO_APPROVAL';
            END IF;
            -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
			IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0 THEN
				INSERT INTO `approvalmatrixtemplate`(contractId, coreCustomerId, actionId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency)
					VALUES(`_contractId`, `_coreCustomerId`, `_featureActionId`, `_limitTypeId`, @isGroupMatrix, @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency);
				SET @lastTemplateId = LAST_INSERT_ID();
                IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL THEN
					IF @isGroupMatrix = 1 THEN
						INSERT INTO `signatorygroupmatrixtemplate` (`approvalMatrixId`, `groupList`, `groupRule`)
							VALUES(@lastTemplateId, @groupList, @groupRule);
					ELSE
						SET @noOfApprovers = JSON_LENGTH(@approvers);
                        SET @currentUserId = '';
                        SET @approverIndex = 0;
                        USERBASED_MATRIXTEMPLATE_INIT: LOOP
							IF @approverIndex = @noOfApprovers THEN
								LEAVE USERBASED_MATRIXTEMPLATE_INIT;
							ELSE
								SET @currentUserId = JSON_UNQUOTE(JSON_EXTRACT(JSON_EXTRACT(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'));
								INSERT INTO `customerapprovalmatrixtemplate` (`customerId`, `approvalMatrixId`)
									VALUES (@currentUserId, @lastTemplateId);
								SET @approverIndex = @approverIndex + 1;
							END IF;
                        END LOOP;
					END IF;
				END IF;
            END IF;
            IF @isAccountLevelFeature = 0 THEN
                -- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, NULL);
                IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1) THEN
					INSERT INTO `approvalmatrix`(name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency)
							VALUES(CONCAT(`_contractId`, '-', `_featureActionId`), `_contractId`, `_coreCustomerId`, `_featureActionId`, NULL, `_limitTypeId`, @isGroupMatrix, @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency);
					SET @lastTemplateId = LAST_INSERT_ID();
					IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL THEN
						IF @isGroupMatrix = 1 THEN
							INSERT INTO `signatorygroupmatrix` (`approvalMatrixId`, `groupList`, `groupRule`)
								VALUES(@lastTemplateId, @groupList, @groupRule);
						ELSE
							SET @noOfApprovers = JSON_LENGTH(@approvers);
							SET @currentUserId = '';
							SET @approverIndex = 0;
							USERBASED_MATRIX_INIT_1: LOOP
								IF @approverIndex = @noOfApprovers THEN
									LEAVE USERBASED_MATRIX_INIT_1;
								ELSE
									SET @currentUserId = JSON_UNQUOTE(JSON_EXTRACT(JSON_EXTRACT(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'));
									INSERT INTO `customerapprovalmatrix` (`customerId`, `approvalMatrixId`)
										VALUES (@currentUserId, @lastTemplateId);
									SET @approverIndex = @approverIndex + 1;
								END IF;
							END LOOP;
						END IF;
					END IF;
				END IF;
            ELSE
				-- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
				SET @noOfAccountIds = LENGTH(@accountIds) - LENGTH(REPLACE(@accountIds, ',', '')) + 1;
				SET @accountIndex = 0;
				MATRIX_ACCRULES_INIT : LOOP
					SET @accountIndex = @accountIndex + 1;
					IF @accountIndex = @noOfAccountIds + 1 THEN
						LEAVE MATRIX_ACCRULES_INIT;
					ELSE
						SET @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@accountIds, ',', @accountIndex), ',', -1);
						-- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId);
                        -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                        IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1) THEN
							INSERT INTO `approvalmatrix`(name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency)
								VALUES(CONCAT(`_contractId`, '-', `_featureActionId`), `_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId, `_limitTypeId`, @isGroupMatrix, @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency);
							SET @lastTemplateId = LAST_INSERT_ID();
							IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL THEN
								IF @isGroupMatrix = 1 THEN
									INSERT INTO `signatorygroupmatrix` (`approvalMatrixId`, `groupList`, `groupRule`)
										VALUES(@lastTemplateId, @groupList, @groupRule);
								ELSE
									SET @noOfApprovers = JSON_LENGTH(@approvers);
									SET @currentUserId = '';
									SET @approverIndex = 0;
									USERBASED_MATRIX_INIT_2: LOOP
										IF @approverIndex = @noOfApprovers THEN
											LEAVE USERBASED_MATRIX_INIT_2;
										ELSE
											SET @currentUserId = JSON_UNQUOTE(JSON_EXTRACT(JSON_EXTRACT(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'));
											INSERT INTO `customerapprovalmatrix` (`customerId`, `approvalMatrixId`)
												VALUES (@currentUserId, @lastTemplateId);
											SET @approverIndex = @approverIndex + 1;
										END IF;
									END LOOP;
								END IF;
							END IF;
						END IF;
					END IF;
				END LOOP;
            END IF;
		END IF;
	END LOOP;
END$$
DELIMITER ;
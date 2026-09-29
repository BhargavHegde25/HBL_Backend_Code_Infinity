
ALTER TABLE `bbactedrequest` ADD COLUMN `assocRequestId` VARCHAR(64) NULL DEFAULT NULL AFTER `requestId`;
ALTER TABLE `bbrequest` ADD COLUMN `assocRequestId` VARCHAR(64) NULL DEFAULT NULL AFTER `requestId`;
ALTER TABLE `approvalmatrix` CHANGE COLUMN `accountId` `accountId` VARCHAR(50) NULL DEFAULT NULL ;
CREATE INDEX `idx_assocRequestId` ON `bbrequest` (`assocRequestId`);
CREATE INDEX `idx_assocRequestId` ON `bbactedrequest` (`assocRequestId`);


DROP PROCEDURE IF EXISTS `approvalmatrix_default_init_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_default_init_proc`(
	IN `_contractId`		VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,    -- the contract for which the matrix is being initialized
    IN `_coreCustomerIds`	TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,			-- list of cifs for the contract
    IN `_featureActionIds`	TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,			-- list of featureActionIds to be created IN approvalmatrixtemplate
    IN `_accountIds`		TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,			-- list of accounts for which approvalmatrix is to be initiated
    IN `_isGroupRule`		VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci,		-- 0-user based, 1-group based
    IN `_isDefaultDisabled`	VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,	-- 0-enabled, 1-disabled
    IN `_currency`	        VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	IF `_contractId` IS NULL OR `_contractId` = '' THEN
        -- TODO: return error
		LEAVE MAINEXEC;
	END IF;
	IF `_coreCustomerIds` IS NULL OR `_coreCustomerIds` = '' THEN
        -- TODO: return error
		LEAVE MAINEXEC;
	END IF;
	IF `_featureActionIds` IS NULL OR `_featureActionIds` = '' THEN
        -- TODO: return error
        LEAVE MAINEXEC;
    END IF;
    SET @legalEntityId = "ALL";

	SET @isGroupLevel = IF(`_isGroupRule` = NULL OR `_isGroupRule` = '' , 0 , `_isGroupRule` * 1);	-- if _isGroupRule NOT passed, user-based approval mode is assumed
    SET @isMatrixDisabled = IF(`_isDefaultDisabled` = NULL OR `_isDefaultDisabled` = '', 0, `_isDefaultDisabled` * 1); -- if _isDefaultDisabled is NOT passed, the approval matrix is assumed to be enabled
	SET @cifIndex = 0;
    SET @numOfCifs = LENGTH(`_coreCustomerIds`) - LENGTH(REPLACE(`_coreCustomerIds`, ',', '')) + 1;
    APPROVALMATRIX_INIT: LOOP
		SET @cifIndex = @cifIndex + 1;
		IF @cifIndex = @numOfCifs + 1 THEN 
			LEAVE APPROVALMATRIX_INIT;
		ELSE
			SET @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(`_coreCustomerIds`, ',', @cifIndex), ',', -1);
            SELECT `companyLegalUnit` INTO @legalEntityId FROM `contractcorecustomers` WHERE `coreCustomerId` = @coreCustomerId AND `contractId` = `_contractId`;

            IF @legalEntityId IS NULL THEN
                -- TODO: return error - No Legal Entity Found for the user
                LEAVE MAINEXEC;
            END IF;

            -- if there is no existing active approvalmatrixtemplate entries for the given contract AND cif
			IF (EXISTS(SELECT `id` FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 0) = 0) THEN

                -- CLEAN-UP APPROVALMATRIX - perform a force-delete ON approvalmatrix for that contract AND cif combination
                DELETE FROM `approvalmatrix` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId;

                -- CLEAN-UP APPROVALMATRIXTEMPLATE - perform a force-delete ON approvalmatrixtemplate for that contract AND cif combination
                -- STEP 1: first list out all the inactive rules ON approvalmatrixtemplate
                SET @inactiveTemplateRuleIds = (SELECT GROUP_CONCAT(`id`) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 1);
                -- STEP 2: delete all the entries IN signatorygroupmatrixtemplate or customerapprovalmatrixtemplate containing the inactive template id's
                IF @inactiveTemplateRuleIds IS NULL or @inactiveTemplateRuleIds = '' THEN
                    DELETE FROM `signatorygroupmatrixtemplate` WHERE (FIND_IN_SET(`approvalMatrixId`, @inactiveTemplateRuleIds) > 0);
                    DELETE FROM `customerapprovalmatrixtemplate` WHERE (FIND_IN_SET(`approvalMatrixId`, @inactiveTemplateRuleIds) > 0);
                END IF;
                DELETE FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 1;
                
                -- SET APPROVAL MODE - check for existing entry IN approvalmode table
                IF (EXISTS(SELECT `id` FROM `approvalmode` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId) > 0) THEN
                    UPDATE `approvalmode` SET `isGroupLevel` = @isGroupLevel WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId;
                ELSE
                    INSERT INTO `approvalMode`(`id`, `coreCustomerId`, `contractId`, `isGroupLevel`) VALUES (uuid(), @coreCustomerId, `_contractId`, @isGroupLevel);
                END IF;
                
                -- SET APPROVAL MATRIX STATUS - enabled/disabled
                IF (EXISTS(SELECT `contractId` FROM `manageapprovalmatrix` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 0) > 0) THEN
                    UPDATE `manageapprovalmatrix` SET `isDisabled` = @isMatrixDisabled  WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId;
                ELSE
                    INSERT INTO `manageapprovalmatrix`(`contractId`,`coreCustomerId`,`isDisabled`) VALUES (`_contractId`, @coreCustomerId, @isMatrixDisabled);
                END IF;
                
                -- CREATE APPROVAL MATRIX TEMPLATE ENTRIES
                CALL `approvalmatrixtemplate_default_create_proc`(`_featureActionIds`, `_contractId`, @coreCustomerId, @isGroupLevel, `_currency`);
            ELSE
                -- there exists some entry/entries IN approvalmatrixtemplate for that contract AND cif
                
                -- get the existing featureactionIds for which active rules are set IN the approvalmatrixtemplate
                SET @existingFeatureActionIds = (SELECT GROUP_CONCAT(DISTINCT(`actionId`)) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 0);
                -- iterate over the provided featureActionIds to search for newer featureActions
                SET @newFeatureActionIds = '';
                SET @actionIdIndex = 0;
                SET @numOfActionIds = LENGTH(`_featureActionIds`) - LENGTH(REPLACE(`_featureActionIds`, ',', '')) + 1;
                
                NEWACTIONS_INIT: LOOP
					SET @actionIdIndex = @actionIdIndex + 1;
					IF @actionIdIndex = @numOfActionIds + 1 THEN 
						LEAVE NEWACTIONS_INIT;
					ELSE
						SET @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(`_featureActionIds`, ',', @actionIdIndex), ',', -1);
                        IF(FIND_IN_SET(@actionId, @existingFeatureActionIds) = 0) THEN
							-- the action id supplied does NOT exist IN the existing feature action ids
                            IF @newFeatureActionIds = '' THEN
                                SET @newFeatureActionIds = @actionId;
                            ELSE
							    SET @newFeatureActionIds = CONCAT(@newFeatureActionIds, ',', @actionId);
                            END IF;
						END IF;
					END IF;
                END LOOP NEWACTIONS_INIT;
                
                IF @newFeatureActionIds != '' THEN
					-- create approval matrix template entries for the newer feature actions added to the contract AND cif
                    CALL `approvalmatrixtemplate_default_create_proc`(@newFeatureActionIds, `_contractId`, @coreCustomerId, @isGroupLevel, `_currency`);
				END IF;
                
                -- check for existing active rule entries IN approvalmatrix for the contract AND cif
                IF(EXISTS(SELECT `id` FROM `approvalmatrix` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 0) > 0) THEN
					-- check for newer accounts, AND migrate active rules FROM approvalmatrixtemplate to approvalmatrix for the new accounts
                    SET @existingAccountIds = (SELECT GROUP_CONCAT(DISTINCT(`accountId`)) FROM `approvalmatrix` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `softdeleteflag` = 0);
                    SET @newAccountIds = '';
                    SET @accountIdIndex = 0;
                    SET @noOfAccountIds = LENGTH(`_accountIds`) - LENGTH(REPLACE(`_accountIds`, ',', '')) + 1;
                    SET @noOfNewAccountIds = 0;
                    NEWACCOUNTS_INIT : LOOP
                        SET @accountIdIndex = @accountIdIndex + 1;
                        IF @accountIdIndex = @noOfAccountIds + 1 THEN 
                            LEAVE NEWACCOUNTS_INIT;
                        ELSE
                            SET @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(`_accountIds`, ',', @accountIdIndex), ',', -1);
                            IF (FIND_IN_SET(@accountId, @existingAccountIds) = 0) THEN
                                -- the account id supplied does NOT exist IN the existing account ids
                                SET @noOfNewAccountIds = @noOfNewAccountIds + 1;
                                IF @newAccountIds = '' THEN
                                    SET @newAccountIds = @accountId;
                                ELSE
                                    SET @newAccountIds = CONCAT(@newAccountIds, ',', @accountId);
                                END IF;
                            END IF;
                        END IF;
                    END LOOP NEWACCOUNTS_INIT;
                    IF @newAccountIds = '' THEN
                        LEAVE MAINEXEC;
                    ELSE
                        -- migrate rules FROM approvalmatrixtemplate for the new account ids as well
                        SET @nonDefaultMatrixTemplateIds = '';
                        -- find all the ids IN approvalmatrixtemplate for which the account-level action rules are set, AND migrate only those account level actions to the newer accounts
                        -- SET @nonDefaultMatrixTemplateIds = (SELECT GROUP_CONCAT(DISTINCT(`id`)) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = @coreCustomerId AND `actionId` IN (SELECT DISTINCT(`id`) FROM `featureaction` WHERE `actionlevelId` = 'ACCOUNT_LEVEL' AND `companyLegalUnit` = @legalEntityId) AND `id` NOT IN(SELECT `id` FROM `approvalmatrixtemplate` WHERE `approvalruleId` = 'NO_APPROVAL' AND `lowerlimit` = -1.0 AND `upperlimit` = -1.0));
                        SET @nonDefaultMatrixTemplateIds = (SELECT GROUP_CONCAT(DISTINCT(`amt`.`id`)) FROM `approvalmatrixtemplate` AS `amt` LEFT JOIN `featureaction` AS `fa` ON `amt`.`actionId` = `fa`.`id` AND `fa`.`actionlevelId` = 'ACCOUNT_LEVEL' AND `fa`.`companyLegalUnit` = @legalEntityId
                            WHERE `amt`.`coreCustomerId` = @coreCustomerId AND `amt`.`contractId` = `_contractId`
                            AND `amt`.`id` NOT IN (SELECT `id` FROM `approvalmatrixtemplate` WHERE `approvalruleId` = 'NO_APPROVAL' AND `lowerlimit` = -1.0 AND `upperlimit` = -1.0));
                        SET @templateIdIndex = 0;
                        SET @noOfTemplateIds = LENGTH(@nonDefaultMatrixTemplateIds) - LENGTH(REPLACE(@nonDefaultMatrixTemplateIds, ',', '')) + 1;
                        IF @nonDefaultMatrixTemplateIds IS NOT NULL THEN
							TEMPLATERULEMIGRATION_INIT : LOOP
								SET @templateIdIndex = @templateIdIndex + 1;
								IF @templateIdIndex = @noOfTemplateIds + 1 THEN 
									LEAVE TEMPLATERULEMIGRATION_INIT;
								ELSE
									SET @templateId = SUBSTRING_INDEX(SUBSTRING_INDEX(@nonDefaultMatrixTemplateIds, ',', @templateIdIndex), ',', -1);
									-- for the template id, pick up the rule info FROM approvalmatrixtemplate, AND store IN approvalmatrix for that account
									SET @accountIdIndex = 0;
									ACCOUNTIDMIGRATION_INIT : LOOP
										SET @accountIdIndex = @accountIdIndex + 1;
										IF @accountIdIndex = @noOfNewAccountIds + 1 THEN 
											LEAVE ACCOUNTIDMIGRATION_INIT;
										ELSE
											SET @newAccountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@newAccountIds, ',', @accountIdIndex), ',', -1);
											SET @migIsGroupMatrix = 0;

											-- approval matrix table migration
											SELECT `isGroupMatrix` into @migIsGroupMatrix FROM `approvalmatrixtemplate` WHERE `id` = @templateId;
											-- transfer the rule (given the templateId) from approvalmatrixtemplate to approvalmatrix for the new account id
											INSERT INTO `approvalmatrix` (`name`, `contractId`, `coreCustomerId`, `accountId`, `actionId`, `approvalruleId`, `isGroupMatrix`, `limitTypeId`, `lowerlimit`, `upperlimit`, `currency`)
												SELECT CONCAT(`contractId`, '-', `actionId`) AS `name`,
													`amt`.`contractId`,
													`amt`.`coreCustomerId`,
													@newAccountId AS `accountId`,
													`amt`.`actionId`,
													`amt`.`approvalruleId`,
													`amt`.`isGroupMatrix`,
													`amt`.`limitTypeId`,
													`amt`.`lowerlimit`,
													`amt`.`upperlimit`,
													`amt`.`currency`
												FROM `approvalmatrixtemplate` AS `amt` WHERE `amt`.`id` = @templateId;
											SET @newMatrixId = LAST_INSERT_ID();

											IF @migIsGroupMatrix = 1 THEN
												-- use this matrix id to insert into signatorygroupmatrix, for group-based approval
												INSERT INTO `signatorygroupmatrix` (`approvalMatrixId`, `groupList`, `groupRule`)
													SELECT
														@newMatrixId AS `approvalMatrixId`,
														`smt`.`groupList`,
														`smt`.`groupRule`
													FROM `signatorygroupmatrixtemplate` AS `smt` WHERE `smt`.`approvalMatrixId` = @templateId;
											ELSE
												-- use this matrix id to insert into signatorygroupmatrix, for user-based approval
												INSERT INTO `customerapprovalmatrix` (`customerId`, `approvalMatrixId`)
													SELECT
														`camt`.`customerId`,
														@newMatrixId AS `approvalMatrixId`
													FROM `customerapprovalmatrixtemplate` AS `camt` WHERE `camt`.`approvalMatrixId` = @templateId;
											END IF;   
										END IF;
									END LOOP;
								END IF;
							END LOOP;
						END IF;
                    END IF;
                END IF;
            END IF;
        END IF;
    END LOOP;
END$$
DELIMITER ;


-- not in use [POC only]
-- a proc to check whether an approval matrix id is safe to be deleted (if requests are completed, i.e. not in pending state, the approval matrix id can be deleted safely)
DROP PROCEDURE IF EXISTS `approvalmatrix_ids_checkncleanup_for_safedelete_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(
    IN `_contractId`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_coreCustomerId`    VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionId`   VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_accountId`         VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
    SET @legalEntityId = 'ALL';
    -- Check whether the given featureActionId is an account level action or a customer level action
    SELECT `companyLegalUnit` INTO @legalEntityId FROM `contractcorecustomers` WHERE `coreCustomerId` = `_coreCustomerId` AND `contractId` = `_contractId`;
    SET @actionLevel = (SELECT DISTINCT(`actionlevelId`) FROM `featureaction` WHERE `id` = `_featureActionId` AND `companyLegalUnit` = @legalEntityId);
    IF @actionLevel = 'ACCOUNT_LEVEL' AND (`_accountId` = '' OR `_accountId` IS NULL) THEN
		SIGNAL sqlstate '45000' SET message_text = 'No Account ID passed for an account-level feature';
        -- SHOW ERROR - if feature action is account level, account id must be passed. If not passed, return error signal
        LEAVE MAINEXEC;
    END IF;
    SET @matrixIdsSafeToDelete = '';
    -- fetch all the approvalmatrix id's which are marked for soft delete, and no pending requests are associated with it.
    -- select all the unused or used matrix ids whose associated requests are not in Pending state and the matrix id is marked for soft delete. These are unused rules and can be deleted safely.
    IF `_accountId` = '' OR `_accountId` IS NULL THEN
        SET @matrixIdsSafeToDelete = (SELECT GROUP_CONCAT(DISTINCT(`am`.`id`)) FROM `approvalmatrix` AS `am` 
			LEFT JOIN `requestapprovalmatrix` AS `ram` ON `am`.`id` = `ram`.`approvalMatrixId`
			LEFT JOIN `bbrequest` AS `br` ON `br`.`requestId` = `ram`.`requestId`
			WHERE `am`.`softdeleteflag` = 1
			AND (`br`.`status` != 'Pending' OR `br`.`status` IS NULL) 
			AND `am`.`contractId` = `_contractId`
            AND `am`.`coreCustomerId` = `_coreCustomerId`
            AND `am`.`actionId` = `_featureActionId`);
    ELSE
        SET @matrixIdsSafeToDelete = (SELECT GROUP_CONCAT(DISTINCT(`am`.`id`)) FROM `approvalmatrix` AS `am` 
			LEFT JOIN `requestapprovalmatrix` AS `ram` ON `am`.`id` = `ram`.`approvalMatrixId`
			LEFT JOIN `bbrequest` AS `br` ON `br`.`requestId` = `ram`.`requestId`
			WHERE `am`.`softdeleteflag` = 1
			AND (`br`.`status` != 'Pending' OR `br`.`status` IS NULL) 
            AND `am`.`contractId` = `_contractId`
            AND `am`.`coreCustomerId` = `_coreCustomerId`
            AND `am`.`actionId` = `_featureActionId`
            AND `am`.`accountId` = `_accountId`);
    END IF;
    
    IF @matrixIdsSafeToDelete IS NOT NULL OR @matrixIdsSafeToDelete != '' THEN
		SET @matrixIdsSafeToDelete = CONCAT('\'', REPLACE(@matrixIdsSafeToDelete, ',', '\',\''), '\'');
        -- proceed to delete rules from approvalmatrix, signatorygroupmatrix, and customerapprovalmatrix (signatorygroupmatrix || customerapprovalmatrix > requestapprovalmatrix > approvalmatrix : Order is to be maintained)
        SET @sqlStmt = CONCAT('DELETE FROM `signatorygroupmatrix` WHERE `approvalMatrixId` IN (', @matrixIdsSafeToDelete, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
        SET @sqlStmt = CONCAT('DELETE FROM `customerapprovalmatrix` WHERE `approvalMatrixId` IN (', @matrixIdsSafeToDelete, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
        SET @sqlStmt = CONCAT('DELETE FROM `signatorygrouprequestmatrix` WHERE `approvalMatrixId` IN (', @matrixIdsSafeToDelete, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
        SET @sqlStmt = CONCAT('DELETE FROM `requestapprovalmatrix` WHERE `approvalMatrixId` IN (', @matrixIdsSafeToDelete, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
        SET @sqlStmt = CONCAT('DELETE FROM `approvalmatrix` WHERE `id` IN (', @matrixIdsSafeToDelete, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
        DEALLOCATE PREPARE STMT;
    END IF;
END$$
DELIMITER ;


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
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @isAccountLevelFeature = (SELECT DISTINCT(`isAccountLevel`) FROM `featureaction` WHERE `id` = `_featureActionId`);
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


DROP PROCEDURE IF EXISTS `fetch_approvalmatrix_activerules_for_contractcifmap`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalmatrix_activerules_for_contractcifmap`(
	IN `_contractCifMapJSON`    TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_featureActionIds`      TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	-- parse contractCifMapJSON to fetch all contractId and cif ids
    SET @contractIds = '';
    SET @cifIds = '';
    SET @noOfContractIds = JSON_LENGTH(`_contractCifMapJSON`);
    SET @contractIdIndex = 0;
    SET @featureActionIds = CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\'');
    SET @sqlStmt = '';
    CONTRACTID_EXTRACT : LOOP
		IF @contractIdIndex = @noOfContractIds THEN
			LEAVE CONTRACTID_EXTRACT;
		END IF;
		SET @contractIdJSON = JSON_EXTRACT(`_contractCifMapJSON`, CONCAT('$[', @contractIdIndex, ']'));
        SET @contractIdIndex = @contractIdIndex + 1;
        SET @contractId = JSON_UNQUOTE(JSON_EXTRACT(@contractIdJSON, '$.contractId'));
        IF @contractId IS NULL OR @contractId = '' THEN
			-- return sql error signal
            LEAVE MAINEXEC;
        END IF;
        IF @contractIds = '' THEN
			SET @contractIds = @contractId;
		ELSE
			SET @contractIds = CONCAT(@contractIds, ',', @contractId);
		END IF;
        SET @cifIdsJSON = JSON_EXTRACT(@contractIdJSON, '$.cifs');
        SET @noOfCifIds = JSON_LENGTH(@cifIdsJSON);
        SET @cifIndex = 0;
		CORECUSTOMERID_EXTRACT : LOOP
			IF @cifIndex = @noOfCifIds THEN
				LEAVE CORECUSTOMERID_EXTRACT;
			END IF;
            SET @cifId = JSON_UNQUOTE(JSON_EXTRACT(JSON_EXTRACT(@cifIdsJSON, CONCAT('$[', @cifIndex, ']')), '$.id'));
            SET @cifIndex = @cifIndex + 1;
            IF @cifId IS NULL OR @cifId = '' THEN
				-- return sql error signal
				LEAVE MAINEXEC;
			END IF;
            -- prepare statement for approval matrix fetch
            -- check from approvalmode to conditionally join customerapprovalmatrix or signatorygroupmatrix
            SET @matrixFetchStmt = '';
            SET @isGroupMatrix = (SELECT DISTINCT(`isGroupLevel`) FROM `approvalmode` WHERE `contractId` = @contractId AND `coreCustomerId` = @cifId);
            IF @isGroupMatrix = 0 THEN
				SET @matrixFetchStmt = CONCAT('(SELECT `am`.`id` AS `matrixId`,
					`am`.`contractId`,
					`am`.`coreCustomerId`,
					`am`.`accountId`,
					`am`.`actionId`,
					`am`.`limitTypeId`,
					CAST(`am`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`,
					`am`.`approvalruleId`,
					`am`.`lowerlimit`,
					`am`.`upperlimit`,
					NULL AS `groupList`,
					NULL AS `groupRule`,
					GROUP_CONCAT(`cam`.`customerId`) AS `customerIds` from `approvalmatrix` AS `am` 
					LEFT JOIN `customerapprovalmatrix` AS `cam` ON `am`.`id` = `cam`.`approvalMatrixId` WHERE `am`.`contractId` IN (', @contractId, ') AND `am`.`coreCustomerId` IN (', @cifId,') AND `am`.`actionId` IN (', @featureActionIds,') AND `am`.`softdeleteflag` = 0 GROUP BY `cam`.`approvalMatrixId`)');
			ELSE
				SET @matrixFetchStmt = CONCAT('(SELECT 
					`am`.`id` AS `matrixId`,
					`am`.`contractId`,
					`am`.`coreCustomerId`,
					`am`.`accountId`,
					`am`.`actionId`,
					`am`.`limitTypeId`,
					CAST(`am`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`,
					`am`.`approvalruleId`,
					`am`.`lowerlimit`,
					`am`.`upperlimit`,
					`sgm`.`groupList`,
					`sgm`.`groupRule`,
					NULL AS `customerIds` FROM `approvalmatrix` AS `am` 
					LEFT JOIN `signatorygroupmatrix` AS `sgm` ON `am`.`id` = `sgm`.`approvalMatrixId` WHERE `am`.`contractId` IN (', @contractId, ') AND `am`.`coreCustomerId` IN (', @cifId,') AND `am`.`actionId` IN (', @featureActionIds,') AND `am`.`softdeleteflag` = 0)');
			END IF;
            IF @sqlStmt = '' THEN
				SET @sqlStmt = @matrixFetchStmt;
			ELSE
				SET @sqlStmt = CONCAT(@sqlStmt, ' UNION ', @matrixFetchStmt);
			END IF;
		END LOOP;
    END LOOP;
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_requests_with_approvalmatrixinfo_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_requests_with_approvalmatrixinfo_proc`(
	IN `_requestIds` 	        TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_isAssociationId` 	    VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_contractCifMapJSON`    TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_isActiveRulesFetch`    VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @isAssocId = IF(`_isAssociationId` IS NULL OR `_isAssociationId` = '', 0, CAST(`_isAssociationId` AS UNSIGNED));
    SET @isActiveRulesFetch = IF(`_isActiveRulesFetch` IS NULL OR `_isActiveRulesFetch` = '', 0, CAST(`_isActiveRulesFetch` AS UNSIGNED));
	SET @requestIds = `_requestIds`;
    IF @isAssocId = 1 THEN
		SET @requestIds = CONCAT('\'', REPLACE(`_requestIds`, ',', '\',\''), '\'');
		SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(DISTINCT(`requestId`)) INTO @requestIds FROM `bbrequest` WHERE `assocRequestId` IN (', @requestIds, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
	END IF;
    IF @requestIds IS NULL THEN
		SET @requestIds = '';
	END IF;
    SET @isCifLevelFilter = 1;
    SET @contractIds = '';
	SET @cifIds = '';
    IF `_contractCifMapJSON` IS NULL OR `_contractCifMapJSON` = '' THEN
		SET @isCifLevelFilter = 0;
	ELSE
		SET @noOfContractIds = JSON_LENGTH(`_contractCifMapJSON`);
		SET @contractIdIndex = 0;
		CONTRACTID_EXTRACT : LOOP
			IF @contractIdIndex = @noOfContractIds THEN
				LEAVE CONTRACTID_EXTRACT;
			END IF;
			SET @contractJSON = JSON_EXTRACT(`_contractCifMapJSON`, CONCAT('$[', @contractIdIndex, ']'));
			SET @contractIdIndex = @contractIdIndex + 1;
			SET @contractId = JSON_UNQUOTE(JSON_EXTRACT(@contractJSON, '$.contractId'));
			IF @contractId IS NULL OR @contractId = '' THEN
				-- return sql error signal
				LEAVE MAINEXEC;
			END IF;
			IF @contractIds = '' THEN
				SET @contractIds = @contractId;
			ELSE
				SET @contractIds = CONCAT(@contractIds, ',', @contractId);
			END IF;
			SET @cifsJSON = JSON_EXTRACT(@contractJSON, '$.cifs');
			SET @noOfCifIds = JSON_LENGTH(@cifsJSON);
			SET @cifIndex = 0;
			CORECUSTOMERID_EXTRACT : LOOP
				IF @cifIndex = @noOfCifIds THEN
					LEAVE CORECUSTOMERID_EXTRACT;
				END IF;
				SET @cifJSON = JSON_EXTRACT(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
				SET @cifIndex = @cifIndex + 1;
				SET @cifId = JSON_UNQUOTE(JSON_EXTRACT(@cifJSON, '$.id'));
				IF @cifId IS NULL OR @cifId = '' THEN
					-- return sql error signal
					LEAVE MAINEXEC;
				END IF;
				IF @cifIds = '' THEN
					SET @cifIds = @cifId;
				ELSE
					SET @cifIds = CONCAT(@cifIds, ',', @cifId);
				END IF;
			END LOOP;
		END LOOP;
		SET @contractIds = CONCAT('\'', REPLACE(@contractIds, ',', '\',\''), '\'');
		SET @cifIds = CONCAT('\'', REPLACE(@cifIds, ',', '\',\''), '\'');
    END IF;
    SET @sqlStmt = '';
    SET @noOfRequestIds = LENGTH(@requestIds) - LENGTH(REPLACE(@requestIds, ',', '')) + 1;
    SET @requestIdIndex = 1;
    REQUESTIDEXTRACT_INIT : LOOP
		SET @requestId = SUBSTRING_INDEX(SUBSTRING_INDEX(@requestIds, ',', @requestIdIndex), ',', -1);
        IF @requestIdIndex = @noOfRequestIds + 1 THEN
			LEAVE REQUESTIDEXTRACT_INIT;
		END IF;
        SET @requestIdIndex = @requestIdIndex + 1;
        SET @isGroupMatrix = (SELECT DISTINCT(`isGroupMatrix`) FROM `bbrequest` WHERE `requestId` = @requestId);
        SET @sqlSubStmt = '';
        IF @isGroupMatrix = 0 THEN
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `am`.`contractId`, `am`.`coreCustomerId`, `br`.`featureActionId`, `br`.`accountId`, `br`.`status`, `br`.`createdby`, `br`.`createdts`, `br`.`requiredSets`, `br`.`receivedSets`, `ram`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, `ram`.`receivedApprovals`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, NULL AS `groupList`, NULL AS `pendingGroupList`, NULL AS `groupRuleValue`, NULL AS `isGroupRuleApproved`, GROUP_CONCAT(`cam`.`customerId`) AS `approverIds`, `br`.`additionalMeta` 
			FROM `bbrequest` AS `br` LEFT JOIN `requestapprovalmatrix` AS `ram` ON `br`.`requestId` = `ram`.`requestId`
			LEFT JOIN `customerapprovalmatrix` AS `cam` ON `cam`.`approvalMatrixId` = `ram`.`approvalMatrixId` LEFT JOIN `approvalmatrix` AS `am` ON `am`.`id` = `ram`.`approvalMatrixId` WHERE `br`.`requestId` = \'', @requestId, '\' ', IF(@isCifLevelFilter = 1, CONCAT('AND `am`.`contractId` IN (', @contractIds, ') AND `am`.`coreCustomerId` IN (', @cifIds, ')'), ''), ' GROUP BY `br`.`requestId`, `ram`.`approvalMatrixId`)');
		ELSE
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `am`.`contractId`, `am`.`coreCustomerId`, `br`.`featureActionId`, `br`.`accountId`, `br`.`status`, `br`.`createdby`, `br`.`createdts`, `br`.`requiredSets`, `br`.`receivedSets`, `ram`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, `ram`.`receivedApprovals`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, `sgrm`.`groupList`, `sgrm`.`pendingGroupList`, `sgrm`.`groupRuleValue`, CAST(`sgrm`.`isApproved` AS UNSIGNED) AS `isGroupRuleApproved`, NULL AS `approverIds`, `br`.`additionalMeta` 
			FROM `bbrequest` AS `br` LEFT JOIN `requestapprovalmatrix` AS `ram` ON `br`.`requestId` = `ram`.`requestId`
			LEFT JOIN `signatorygrouprequestmatrix` AS `sgrm` ON (`ram`.`approvalMatrixId` = `sgrm`.`approvalMatrixId` AND `sgrm`.`requestId` = `br`.`requestId`) LEFT JOIN `approvalmatrix` AS `am` ON `am`.`id` = `ram`.`approvalMatrixId` WHERE `br`.`requestId` = \'', @requestId, '\' ', IF(@isActiveRulesFetch = 1, ' AND `sgrm`.`isApproved` = 0', ''), IF(@isCifLevelFilter = 1, CONCAT(' AND `am`.`contractId` IN (', @contractIds, ') AND `am`.`coreCustomerId` IN (', @cifIds, ')'), ''), ')');
		END IF;
        IF @sqlStmt = '' THEN
			SET @sqlStmt = @sqlSubStmt;
        ELSE
			SET @sqlStmt = CONCAT(@sqlStmt, 'UNION ALL', @sqlSubStmt);
		END IF;
    END LOOP;
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `create_new_composite_request_in_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `create_new_composite_request_in_approvalqueue_proc`(
	IN `_contractCifMatrixIdsJSON`  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_assocRequestId`            VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_confirmationNumber`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionId`           VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_accountId`                 VARCHAR(45) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_createdBy`                 VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_comments`                  VARCHAR(512) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_additionalMetaJSON`        TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @requestIds = '';
	SET @assocRequestId = NULL;
	IF `_assocRequestId` IS NULL OR `_assocRequestId` = '' THEN
		SET @assocRequestId = uuid();
	ELSE
		SET @assocRequestId = `_assocRequestId`;
	END IF;
	SET @noOfContractIds = JSON_LENGTH(`_contractCifMatrixIdsJSON`);
    SET @contractIdIndex = 0;
	CONTRACTID_EXTRACT : LOOP
		IF @contractIdIndex = @noOfContractIds THEN
			LEAVE CONTRACTID_EXTRACT;
		END IF;
		SET @contractJSON = JSON_EXTRACT(`_contractCifMatrixIdsJSON`, CONCAT('$[', @contractIdIndex, ']'));
        SET @contractIdIndex = @contractIdIndex + 1;
        SET @contractId = JSON_UNQUOTE(JSON_EXTRACT(@contractJSON, '$.contractId'));
        IF @contractId IS NULL OR @contractId = '' THEN
			-- return sql error signal
            LEAVE MAINEXEC;
        END IF;
        SET @cifsJSON = JSON_EXTRACT(@contractJSON, '$.cifs');
        SET @noOfCifIds = JSON_LENGTH(@cifsJSON);
        SET @cifIndex = 0;
		CORECUSTOMERID_EXTRACT : LOOP
			IF @cifIndex = @noOfCifIds THEN
				LEAVE CORECUSTOMERID_EXTRACT;
			END IF;
            SET @cifJSON = JSON_EXTRACT(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
			SET @cifIndex = @cifIndex + 1;
            SET @cifId = JSON_UNQUOTE(JSON_EXTRACT(@cifJSON, '$.id'));
            IF @cifId IS NULL OR @cifId = '' THEN
				-- return sql error signal
				LEAVE MAINEXEC;
			END IF;
            SET @companyLegalUnit = (SELECT DISTINCT(`companyLegalUnit`) FROM `contractcorecustomers` WHERE `contractId` = @contractId AND `coreCustomerId` = @cifId);
            SET @isGroupMatrix = (SELECT DISTINCT(`isGroupLevel`) FROM `approvalmode` WHERE `contractId` = @contractId AND `coreCustomerId` = @cifId);
            SET @matrixIdsJSON = JSON_EXTRACT(@cifJSON, '$.matrixIds');
            SET @noOfMatrixIds = JSON_LENGTH(@matrixIdsJSON);	-- required sets
            IF @noOfMatrixIds != 0 THEN
				-- for each combination of contract and cif, create entries in bbrequest
				INSERT INTO `bbrequest` (`assocRequestId`, `transactionId`, `featureActionId`, `createdby`, `companyId`, `requiredSets`, `receivedSets`, `status`, `accountId`, `isGroupMatrix`, `additionalMeta`, `companyLegalUnit`)
					VALUES (@assocRequestId, `_confirmationNumber`, `_featureActionId`, `_createdBy`, CONCAT(@contractId, '_', @cifId), @noOfMatrixIds, 0, 'Pending', `_accountId`, @isGroupMatrix, `_additionalMetaJSON`, @companyLegalUnit);
				SET @requestId = LAST_INSERT_ID();
				IF @requestIds = '' THEN
					SET @requestIds = @requestId;
				ELSE
					SET @requestIds = CONCAT(@requestIds, ',', @requestId);
				END IF;
				SET @matrixIdIndex = 0;
				MATRIXIDS_EXTRACT: LOOP
					IF @matrixIdIndex = @noOfMatrixIds THEN
						LEAVE MATRIXIDS_EXTRACT;
					END IF;
					-- matrix id from approval matrix, which is tallied up for the request being created
					SET @matrixId = JSON_UNQUOTE(JSON_EXTRACT(JSON_EXTRACT(@matrixIdsJSON, CONCAT('$[', @matrixIdIndex, ']')), '$.id'));
					SET @matrixIdIndex = @matrixIdIndex + 1;
					-- make an entry in requestapprovalmatrix
					INSERT INTO `requestapprovalmatrix` (`approvalMatrixId`, `requestId`, `receivedApprovals`, `isGroupRule`, `createdby`)
						VALUES (@matrixId, @requestId, 0, @isGroupMatrix, `_createdBy`);
					IF @isGroupMatrix = 1 THEN
						-- for group-based approval, fetch the groupList and groupRule from signatorygroupmatrix and store in signatorygrouprequestmatrix
						INSERT INTO `signatorygrouprequestmatrix` (`signatoryGroupRequestMatrixId`, `requestId`, `approvalMatrixId`, `groupList`, `groupRuleValue`, `pendingGroupList`, `createdby`)
							SELECT uuid() AS `signatoryGroupRequestMatrixId`, @requestId AS `requestId`, @matrixId AS `approvalMatrixId`, `sgm`.`groupList`, `sgm`.`groupRule` AS `groupRuleValue`, `sgm`.`groupList` AS `pendingGroupList`,`_createdBy` AS `createdby`
							FROM `signatorygroupmatrix` AS `sgm` WHERE `sgm`.`approvalMatrixId` = @matrixId;
					END IF;
				END LOOP;
				-- log request creation for the given contract and cif combination
				INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `action`, `companyLegalUnit`)
					VALUES (@requestId, @assocRequestId, CONCAT(@contractId, '_', @cifId), 'Pending', `_comments`, `_createdBy`, 'Pending', @companyLegalUnit);
			END IF;
            
		END LOOP;
	END LOOP;
    CALL `fetch_requests_with_approvalmatrixinfo_proc`(@requestIds, '0', '', '0');
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_composite_approvalmode_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_composite_approvalmode_proc`(
	IN `_contractCifMapJSON`    TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @contractIds = '';
    SET @cifIds = '';
	SET @noOfContractIds = JSON_LENGTH(`_contractCifMapJSON`);
    SET @contractIdIndex = 0;
	CONTRACTID_EXTRACT : LOOP
		IF @contractIdIndex = @noOfContractIds THEN
			LEAVE CONTRACTID_EXTRACT;
		END IF;
		SET @contractJSON = JSON_EXTRACT(`_contractCifMapJSON`, CONCAT('$[', @contractIdIndex, ']'));
        SET @contractIdIndex = @contractIdIndex + 1;
        SET @contractId = JSON_UNQUOTE(JSON_EXTRACT(@contractJSON, '$.contractId'));
        IF @contractId IS NULL OR @contractId = '' THEN
			-- return sql error signal
            LEAVE MAINEXEC;
        END IF;
        IF @contractIds = '' THEN
			SET @contractIds = @contractId;
		ELSE
			SET @contractIds = CONCAT(@contractIds, ',', @contractId);
		END IF;
        SET @cifsJSON = JSON_EXTRACT(@contractJSON, '$.cifs');
        SET @noOfCifIds = JSON_LENGTH(@cifsJSON);
        SET @cifIndex = 0;
		CORECUSTOMERID_EXTRACT : LOOP
			IF @cifIndex = @noOfCifIds THEN
				LEAVE CORECUSTOMERID_EXTRACT;
			END IF;
            SET @cifJSON = JSON_EXTRACT(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
			SET @cifIndex = @cifIndex + 1;
            SET @cifId = JSON_UNQUOTE(JSON_EXTRACT(@cifJSON, '$.id'));
            IF @cifId IS NULL OR @cifId = '' THEN
				-- return sql error signal
				LEAVE MAINEXEC;
			END IF;
            IF @cifIds = '' THEN
				SET @cifIds = @cifId;
			ELSE
				SET @cifIds = CONCAT(@cifIds, ',', @cifId);
			END IF;
		END LOOP;
	END LOOP;
    SET @contractIds = CONCAT('\'', REPLACE(@contractIds, ',', '\',\''), '\'');
    SET @cifIds = CONCAT('\'', REPLACE(@cifIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('SELECT `contractId`, `coreCustomerId`, `isGroupLevel` FROM `approvalmode` WHERE `contractId` IN (', @contractIds, ') AND `coreCustomerId` IN (', @cifIds, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_composite_approvalmatrixstatus_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_composite_approvalmatrixstatus_proc`(
	IN `_contractCifMapJSON`    TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @contractIds = '';
    SET @cifIds = '';
	SET @noOfContractIds = JSON_LENGTH(`_contractCifMapJSON`);
    SET @contractIdIndex = 0;
	CONTRACTID_EXTRACT : LOOP
		IF @contractIdIndex = @noOfContractIds THEN
			LEAVE CONTRACTID_EXTRACT;
		END IF;
		SET @contractJSON = JSON_EXTRACT(`_contractCifMapJSON`, CONCAT('$[', @contractIdIndex, ']'));
        SET @contractIdIndex = @contractIdIndex + 1;
        SET @contractId = JSON_UNQUOTE(JSON_EXTRACT(@contractJSON, '$.contractId'));
        IF @contractId IS NULL OR @contractId = '' THEN
			-- return sql error signal
            LEAVE MAINEXEC;
        END IF;
        IF @contractIds = '' THEN
			SET @contractIds = @contractId;
		ELSE
			SET @contractIds = CONCAT(@contractIds, ',', @contractId);
		END IF;
        SET @cifsJSON = JSON_EXTRACT(@contractJSON, '$.cifs');
        SET @noOfCifIds = JSON_LENGTH(@cifsJSON);
        SET @cifIndex = 0;
		CORECUSTOMERID_EXTRACT : LOOP
			IF @cifIndex = @noOfCifIds THEN
				LEAVE CORECUSTOMERID_EXTRACT;
			END IF;
            SET @cifJSON = JSON_EXTRACT(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
			SET @cifIndex = @cifIndex + 1;
            SET @cifId = JSON_UNQUOTE(JSON_EXTRACT(@cifJSON, '$.id'));
            IF @cifId IS NULL OR @cifId = '' THEN
				-- return sql error signal
				LEAVE MAINEXEC;
			END IF;
            IF @cifIds = '' THEN
				SET @cifIds = @cifId;
			ELSE
				SET @cifIds = CONCAT(@cifIds, ',', @cifId);
			END IF;
		END LOOP;
	END LOOP;
    SET @contractIds = CONCAT('\'', REPLACE(@contractIds, ',', '\',\''), '\'');
    SET @cifIds = CONCAT('\'', REPLACE(@cifIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('SELECT `contractId`, `coreCustomerId`, `isDisabled` FROM `manageapprovalmatrix` WHERE `contractId` IN (', @contractIds, ') AND `coreCustomerId` IN (', @cifIds, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `approve_pendingrequests_in_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `approve_pendingrequests_in_approvalqueue_proc`(
	IN `_requestMatrixDataJSON`     TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_customerId`                VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @noOfRequests = JSON_LENGTH(`_requestMatrixDataJSON`);
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';
    REQUESTEXTRACT_INIT: LOOP
		IF @requestIndex = @noOfRequests THEN
			LEAVE REQUESTEXTRACT_INIT;
		END IF;
        SET @requestJSON = JSON_EXTRACT(`_requestMatrixDataJSON`, CONCAT('$[', @requestIndex, ']'));
        SET @requestIndex = @requestIndex + 1;
        SET @requestId = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.requestId'));
        IF @requestIds = '' THEN
			SET @requestIds = @requestId;
		ELSE
			SET @requestIds = CONCAT(@requestIds, ',', @requestId);
		END IF;
        SET @comments = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.comments'));
        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @receivedSetsCount = 0;
        SET @requiredSetsCount = 0;
        SET @requestStatus = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';
        SELECT `assocRequestId`, `companyId`, `isGroupMatrix`, `requiredSets`, `receivedSets`, `status`, `companyLegalUnit` INTO @assocRequestId, @companyId, @isGroupMatrix, @requiredSetsCount, @receivedSetsCount, @requestStatus, @companyLegalUnit FROM `bbrequest` WHERE `requestId` = @requestId;
        SET @matrixEntriesJSON = JSON_EXTRACT(@requestJSON, '$.matrixData');
        IF @matrixEntriesJSON IS NULL THEN
			LEAVE MAINEXEC;
            -- RETURN SQL SIGNAL
		END IF;
        IF @assocRequestIds = '' THEN
			SET @assocRequestIds = @assocRequestId;
		ELSE
			SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);
		END IF;
        SET @noOfMatrixEntries = JSON_LENGTH(@matrixEntriesJSON);
		SET @matrixIndex = 0;
        
        MATRIXDATAEXTRACT_INIT: LOOP
			IF @matrixIndex = @noOfMatrixEntries THEN
				LEAVE MATRIXDATAEXTRACT_INIT;
			END IF;
            SET @matrixEntryJSON = JSON_EXTRACT(@matrixEntriesJSON, CONCAT('$[', @matrixIndex, ']'));
            SET @matrixIndex = @matrixIndex + 1;
            SET @matrixId = JSON_UNQUOTE(JSON_EXTRACT(@matrixEntryJSON, '$.matrixId'));
            SET @receivedApprovals = JSON_UNQUOTE(JSON_EXTRACT(@matrixEntryJSON, '$.receivedApprovals'));
            SET @pendingGroupList = JSON_UNQUOTE(JSON_EXTRACT(@matrixEntryJSON, '$.pendingGroupList'));
            SET @groupRuleValue = JSON_UNQUOTE(JSON_EXTRACT(@matrixEntryJSON, '$.groupRuleValue'));
            SET @isApproved = JSON_UNQUOTE(JSON_EXTRACT(@matrixEntryJSON, '$.isApproved'));
            IF @isApproved IS NULL THEN
				SET @isApproved = 0;
			ELSE
				SET @isApproved = CAST(@isApproved AS UNSIGNED);
			END IF;
            
            IF @isGroupMatrix = 1 THEN
				IF @pendingGroupList IS NULL OR @groupRuleValue IS NULL THEN
					LEAVE MAINEXEC;
                    -- RETURN SQL SIGNAL
				ELSE
					UPDATE `signatorygrouprequestmatrix` SET `pendingGroupList` = @pendingGroupList, `groupRuleValue` = @groupRuleValue, `isApproved` = @isApproved WHERE `requestId` = @requestId AND `approvalMatrixId` = @matrixId;
				END IF;      
            END IF;
            UPDATE `requestapprovalmatrix` SET `receivedApprovals` = @receivedApprovals WHERE `requestId` = @requestId AND `approvalMatrixId` = @matrixId;
            SET @receivedSetsCount = @receivedSetsCount + @isApproved;
        END LOOP;
        
        -- update entry in bbrequest
        IF @receivedSetsCount >= @requiredSetsCount THEN
			SET @requestStatus = 'Approved';
		END IF;
        
        UPDATE `bbrequest` SET `receivedSets` = @receivedSetsCount, `status` = @requestStatus WHERE `requestId` = @requestId;
        
        IF @isGroupMatrix = 1 THEN
			SET @actingGroupsCSV = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.actingGroupsCSV'));
            IF @actingGroupsCSV IS NULL THEN
				LEAVE MAINEXEC;
			ELSE
				SET @actingGroups = '';
                SET @actingGroupsCSV = CONCAT('\'', REPLACE(@actingGroupsCSV, ',', '\',\''), '\'');
				SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`signatoryGroupName`) INTO @actingGroups FROM `signatorygroup` WHERE `signatoryGroupId` IN (', @actingGroupsCSV, ')');
                PREPARE STMT FROM @sqlStmt;
                EXECUTE STMT;
                DEALLOCATE PREPARE STMT;
				INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `groupName`, `action`, `companyLegalUnit`)
					VALUES (@requestId, @assocRequestId, @companyId, 'Approved', @comments , `_customerId`, @actingGroups, 'Approved', @companyLegalUnit);
			END IF;
        ELSE 
			INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `action`, `companyLegalUnit`)
				VALUES (@requestId, @assocRequestId, @companyId, 'Approved', @comments , `_customerId`, 'Approved', @companyLegalUnit);
        END IF;
    END LOOP;
    CALL `fetch_requests_with_approvalmatrixinfo_proc`(@assocRequestIds, '1', '', '0');
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `reject_pendingrequests_in_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `reject_pendingrequests_in_approvalqueue_proc`(
	IN `_requestMatrixDataJSON`     TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_customerId`                VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @noOfRequests = JSON_LENGTH(`_requestMatrixDataJSON`);
    SET @sqlStmt = '';
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';
    REQUESTEXTRACT_INIT: LOOP
		IF @requestIndex = @noOfRequests THEN
			LEAVE REQUESTEXTRACT_INIT;
		END IF;
        SET @requestJSON = JSON_EXTRACT(`_requestMatrixDataJSON`, CONCAT('$[', @requestIndex, ']'));
        SET @requestIndex = @requestIndex + 1;
        SET @requestId = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.requestId'));
        SET @comments = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.comments'));
        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';
        SELECT `assocRequestId`, `companyId`, `isGroupMatrix`, `companyLegalUnit` INTO @assocRequestId, @companyId, @isGroupMatrix, @companyLegalUnit FROM `bbrequest` WHERE `requestId` = @requestId;
        IF @assocRequestIds = '' THEN
			SET @assocRequestIds = @assocRequestId;
		ELSE
			SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);
		END IF;
        
        IF @isGroupMatrix = 1 THEN
			SET @actingGroupsCSV = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.actingGroupsCSV'));
            IF @actingGroupsCSV IS NULL THEN
				LEAVE MAINEXEC;
			ELSE
				SET @actingGroups = '';
                SET @actingGroupsCSV = CONCAT('\'', REPLACE(@actingGroupsCSV, ',', '\',\''), '\'');
				SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`signatoryGroupName`) INTO @actingGroups FROM `signatorygroup` WHERE `signatoryGroupId` IN (', @actingGroupsCSV, ')');
                PREPARE STMT FROM @sqlStmt;
                EXECUTE STMT;
				INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `groupName`, `action`, `companyLegalUnit`)
					VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments , `_customerId`, @actingGroups, 'Rejected', @companyLegalUnit);
			END IF;
        ELSE 
			INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `action`, `companyLegalUnit`)
				VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments , `_customerId`, 'Rejected', @companyLegalUnit);
        END IF;
    END LOOP;
    SET @assocRequestIdsSQL = CONCAT('\'', REPLACE(@assocRequestIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`requestId`) INTO @requestIds FROM `bbrequest` WHERE `assocRequestId` IN ( ', @assocRequestIdsSQL, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    SET @requestIds = CONCAT('\'', REPLACE(@requestIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('UPDATE `bbrequest` SET `status` = \'Rejected\' WHERE `requestId` IN (', @requestIds, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
    CALL `fetch_requests_with_approvalmatrixinfo_proc`(@assocRequestIds, '1', '', '0');
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `withdraw_pendingrequests_in_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `withdraw_pendingrequests_in_approvalqueue_proc`(
	IN `_requestMatrixDataJSON`     TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_customerId`                VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @noOfRequests = JSON_LENGTH(`_requestMatrixDataJSON`);
    SET @sqlStmt = '';
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';
    REQUESTEXTRACT_INIT: LOOP
		IF @requestIndex = @noOfRequests THEN
			LEAVE REQUESTEXTRACT_INIT;
		END IF;
        SET @requestJSON = JSON_EXTRACT(`_requestMatrixDataJSON`, CONCAT('$[', @requestIndex, ']'));
        SET @requestIndex = @requestIndex + 1;
        SET @requestId = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.requestId'));
        SET @comments = JSON_UNQUOTE(JSON_EXTRACT(@requestJSON, '$.comments'));
        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';
        SELECT `assocRequestId`, `companyId`, `isGroupMatrix`, `companyLegalUnit` INTO @assocRequestId, @companyId, @isGroupMatrix, @companyLegalUnit FROM `bbrequest` WHERE `requestId` = @requestId;
        IF @assocRequestIds = '' THEN
			SET @assocRequestIds = @assocRequestId;
		ELSE
			SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);
		END IF;
        
		INSERT INTO `bbactedrequest` (`requestId`, `assocRequestId`, `companyId`, `status`, `comments`, `createdby`, `action`, `companyLegalUnit`)
			VALUES (@requestId, @assocRequestId, @companyId, 'Withdrawn', @comments , `_customerId`, 'Withdrawn', @companyLegalUnit);
    END LOOP;
    SET @assocRequestIdsSQL = CONCAT('\'', REPLACE(@assocRequestIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`requestId`) INTO @requestIds FROM `bbrequest` WHERE `assocRequestId` IN ( ', @assocRequestIdsSQL, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    SET @requestIds = CONCAT('\'', REPLACE(@requestIds, ',', '\',\''), '\'');
    SET @sqlStmt = CONCAT('UPDATE `bbrequest` SET `status` = \'Withdrawn\' WHERE `requestId` IN (', @requestIds, ')');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
    CALL `fetch_requests_with_approvalmatrixinfo_proc`(@assocRequestIds, '1', '', '0');
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_pendingapprovers_for_request_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_pendingapprovers_for_request_proc`(
	IN `_requestId`         TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_isAssociationId`   VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @isAssocId = IF(`_isAssociationId` IS NULL OR `_isAssociationId` = '', 0, CAST(`_isAssociationId` AS UNSIGNED));
    SET @requestIds = `_requestId`;
    SET @sqlStmt = '';
    IF @isAssocId = 1 THEN
		SET @requestIds = CONCAT('\'', REPLACE(@requestIds, ',', '\',\''), '\'');
		SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(DISTINCT(`requestId`)) INTO @requestIds FROM `bbrequest` WHERE `assocRequestId` IN (', @requestIds, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
	END IF;
    IF @requestIds IS NULL THEN
		SET @requestIds = '';
	END IF;
		
    SET @noOfRequestIds = LENGTH(@requestIds) - LENGTH(REPLACE(@requestIds, ',', '')) + 1;
    SET @requestIdIndex = 1;
    SET @sqlStmt = '';
    REQUESTIDEXTRACT_INIT : LOOP
		SET @requestId = SUBSTRING_INDEX(SUBSTRING_INDEX(@requestIds, ',', @requestIdIndex), ',', -1);
        IF @requestIdIndex = @noOfRequestIds + 1 THEN
			LEAVE REQUESTIDEXTRACT_INIT;
		END IF;
        SET @requestIdIndex = @requestIdIndex + 1;
        SET @isGroupMatrix = (SELECT DISTINCT(`isGroupMatrix`) FROM `bbrequest` WHERE `requestId` = @requestId);
        SET @actedUsers = (SELECT GROUP_CONCAT(`createdby`) FROM `bbactedrequest` WHERE `requestId` = @requestId AND `status` != 'Pending');
        IF @actedUsers IS NULL THEN
			SET @actedUsers = '';
		END IF;
        SET @actedUsers = CONCAT('\'', REPLACE(@actedUsers, ',', '\',\''), '\'');
        SET @sqlSubStmt = '';
        IF @isGroupMatrix = 0 THEN
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `br`.`status`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, `ram`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, `ar`.`name` AS `approvalruleName`, `ram`.`receivedApprovals`, NULL AS `groupList`, NULL AS `groupRule`, NULL AS `pendingGroupList`, NULL AS `groupRuleValue`, NULL AS isGroupRuleApproved, NULL AS `signatoryGroupId`, NULL AS `signatoryGroupName`, NULL AS `groupApproversList`,
				JSON_ARRAYAGG(JSON_OBJECT("customerId", `cam`.`customerId`, "userName", `c`.`UserName`, "firstName", `c`.`FirstName`, "lastName", `c`.`LastName`, "role", `mg`.`Name`, "userImage", `c`.`userImageURL`)) AS `approversList` 
				FROM `bbrequest` AS `br` INNER JOIN `requestapprovalmatrix` AS `ram` ON `br`.`requestId` = `ram`.`requestId` 
				INNER JOIN `approvalmatrix` AS `am` ON `am`.`id` = `ram`.`approvalMatrixId`
				INNER JOIN `customerapprovalmatrix` AS `cam` ON `ram`.`approvalMatrixId` = `cam`.`approvalMatrixId`
                LEFT JOIN `approvalrule` AS `ar` ON `am`.`approvalruleId` = `ar`.`id`
				INNER JOIN `customer` AS `c` ON `cam`.`customerId` = `c`.`id`
				LEFT JOIN `customergroup` AS `cg` ON `c`.`id` = `cg`.`Customer_id` AND `cg`.`contractId` = `am`.`contractId` AND `cg`.`coreCustomerId` = `am`.`coreCustomerId`
				LEFT JOIN `membergroup` AS `mg` ON `cg`.`Group_id` = `mg`.`id`
				WHERE `br`.`requestId` = \'', @requestId, '\'
				AND `cam`.`customerId` NOT IN (', @actedUsers, ')
				GROUP BY `br`.`requestId`, `am`.`id`)');
		ELSE
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `br`.`status`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, `sgrm`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, NULL AS `approvalruleName`, NULL AS `receivedApprovals`, `sgrm`.`groupList`, `sgm`.`groupRule`, `sgrm`.`pendingGroupList`, `sgrm`.`groupRuleValue`, CAST(`sgrm`.`isApproved` AS UNSIGNED) AS `isGroupRuleApproved`, `sg`.`signatoryGroupId`, `sg`.`signatoryGroupName`,
				JSON_ARRAYAGG(JSON_OBJECT("customerId", `csg`.`customerId`, "userName", `c`.`UserName`, "firstName", `c`.`FirstName`, "lastName", `c`.`LastName`, "role", `mg`.`Name`, "userImage", `c`.`userImageURL`)) AS `groupApproversList`, NULL AS `approversList`
				FROM `bbrequest` AS `br` INNER JOIN `signatorygrouprequestmatrix` AS `sgrm` ON `br`.`requestId` = `sgrm`.`requestId`
				INNER JOIN `approvalmatrix` AS `am` ON `am`.`id` = `sgrm`.`approvalMatrixId`
                INNER JOIN `signatorygroupmatrix` AS `sgm` ON `am`.`id` = `sgm`.`approvalMatrixId`
				INNER JOIN `signatorygroup` AS `sg` ON (FIND_IN_SET(`sg`.`signatoryGroupId`, REPLACE(REPLACE(`sgrm`.`pendingGroupList`, \'[\', \'\'), \']\', \'\')) > 0)
				INNER JOIN `customersignatorygroup` AS `csg` ON `sg`.`signatoryGroupId` = `csg`.`signatoryGroupId`
				INNER JOIN `customer` AS `c` on `csg`.`customerId` = `c`.`id`
				LEFT JOIN `customergroup` AS `cg` ON `c`.`id` = `cg`.`Customer_id` AND `cg`.`contractId` = `am`.`contractId` AND `cg`.`coreCustomerId` = `am`.`coreCustomerId`
				LEFT JOIN `membergroup` AS `mg` ON `cg`.`Group_id` = `mg`.`id`
				WHERE `br`.`requestId` = \'', @requestId, '\'
				AND `csg`.`customerId` NOT IN (', @actedUsers, ')
				GROUP BY `br`.`requestId`, `am`.`id`, `sg`.`signatoryGroupId`)');
		END IF;
        IF @sqlStmt = '' THEN
			SET @sqlStmt = @sqlSubStmt;
        ELSE
			SET @sqlStmt = CONCAT(@sqlStmt, 'UNION ALL', @sqlSubStmt);
		END IF;
    END LOOP;
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_request_historyinfo_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_request_historyinfo_proc`(
	IN `_requestId`         VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_isAssociationId`   VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @isAssocId = IF(`_isAssociationId` IS NULL OR `_isAssociationId` = '', 0, CAST(`_isAssociationId` AS UNSIGNED));
    SET @requestIds = `_requestId`;
    IF @isAssocId = 1 THEN
		SET @requestIds = CONCAT('\'', REPLACE(`_requestId`, ',', '\',\''), '\'');
		SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(DISTINCT(`requestId`)) INTO @requestIds FROM `bbrequest` WHERE `assocRequestId` IN (', @requestIds, ')');
        PREPARE STMT FROM @sqlStmt;
        EXECUTE STMT;
	END IF;
    IF @requestIds IS NULL THEN
		SET @requestIds = '\'\'';
	ELSE
		SET @requestIds = CONCAT('\'', REPLACE(@requestIds, ',', '\',\''), '\'');
	END IF;
    SET @sqlStmt = CONCAT('SELECT DISTINCT(`bar`.`approvalId`),
		`bar`.`requestId`,
		`bar`.`assocRequestId`,
		`br`.`featureActionId`,
		`br`.`status` AS `requestStatus`,
		`bar`.`action`,
        `bar`.`companyId`,
		`bar`.`createdby` AS `requestActedBy`,
		`bar`.`createdts` AS `requestActionts`,
		`bar`.`groupName`,
		`bar`.`comments`,
		`c`.`UserName` AS `userName`,
		`c`.`FirstName` AS `firstName`,
		`c`.`LastName` AS `lastName`,
		CONCAT(`c`.`FirstName`, \' \', `c`.`LastName`) AS `fullName`,
		`cg`.`Group_id` AS `roleId`,
		`mg`.`Name` AS `roleName` FROM `bbactedrequest` AS `bar` 
	INNER JOIN 
		(SELECT `requestId`, `assocRequestId`, `featureActionId`, `status` FROM `bbrequest` WHERE `requestId` IN (', @requestIds, ')) AS `br` 
		ON `bar`.`requestId` = `br`.`requestId`
	INNER JOIN `customer` AS `c` ON `bar`.`createdby` = `c`.`id`
	LEFT JOIN `customergroup` AS `cg` ON `c`.`id` = `cg`.`Customer_id`
	LEFT JOIN `membergroup` AS `mg` ON `cg`.`Group_id` = `mg`.`id`
	WHERE `bar`.`requestId` IN (', @requestIds, ') ORDER BY `bar`.`approvalId`');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_all_pendingrequests_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_pendingrequests_proc`(
	IN `_customerId`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionIds`  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'PENDING_REQUEST\' AS `requestType`
		FROM `bbrequest` AS `br`
		LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
        WHERE `br`.`createdby` = \'', @customerId, '\'
        AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
        IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
		'GROUP BY `br`.`assocRequestId`
        HAVING FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 ORDER BY `br`.`createdts` DESC');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_all_pendingapprovals_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_pendingapprovals_proc`(
	IN `_customerId` VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionIds` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
    SET @requestIds = '';
	SELECT GROUP_CONCAT(CONCAT('\'', `pr`.`requestId`, '\'')) INTO @requestIds FROM (SELECT DISTINCT(`camr`.`requestId`) FROM (
		-- ALL USER-BASED matrix requests pending for approval from given customerId
		SELECT `ram`.`requestId`, `ram`.`approvalMatrixId`, `am`.`approvalruleId`, `ar`.`numberOfApprovals`, `ram`.`receivedApprovals`
			FROM `requestapprovalmatrix` AS `ram`
			INNER JOIN `approvalmatrix` AS `am` ON `ram`.`approvalMatrixId` = `am`.`id`
			INNER JOIN `customerapprovalmatrix` AS `cam` ON `cam`.`approvalMatrixId` = `am`.`id`
			INNER JOIN approvalrule AS `ar` ON `am`.`approvalruleId` = `ar`.`id`
				WHERE `ram`.`isGroupRule` = 0 AND `ram`.`requestId` NOT IN (SELECT DISTINCT(`requestId`) FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId)
				GROUP BY `ram`.`requestId`, `ram`.`approvalmatrixId` 
				HAVING `ram`.`receivedApprovals` < (CASE WHEN `ar`.`numberOfApprovals` = -1 THEN COUNT((`cam`.`customerId`)) ELSE `ar`.`numberOfApprovals` END) 
				AND FIND_IN_SET(@customerId, GROUP_CONCAT(`cam`.`customerId`))) AS `camr`
		UNION ALL
		-- ALL SIGNATORY-BASED requests pending for approval from a given customerId
		SELECT DISTINCT(`sgrm`.`requestId`) FROM `signatorygrouprequestmatrix` AS `sgrm`
			INNER JOIN (SELECT `signatoryGroupId` FROM `customersignatorygroup` WHERE `customerId` = @customerId AND `softdeleteflag` = 0) AS `csgd` ON FIND_IN_SET(`csgd`.`signatoryGroupId`, REPLACE(REPLACE(`sgrm`.`pendingGroupList`, '[', ''), ']', ''))
			WHERE `sgrm`.`isApproved` = 0 AND `sgrm`.`requestId` NOT IN (SELECT DISTINCT(`requestId`) FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId)) AS `pr`;
	IF @requestIds IS NULL THEN
		SET @requestIds = '\'\'';
	END IF;
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'PENDING_APPROVAL\' AS `requestType`
        FROM `bbrequest` AS `br`
		LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
        WHERE `br`.`requestId` IN (', @requestIds, ')
        AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
        IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
		'GROUP BY `br`.`assocRequestId`
        ORDER BY `br`.`createdts` DESC');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_all_requesthistory_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_requesthistory_proc`(
	IN `_customerId` VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionIds` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'REQUEST_HISTORY\' AS `requestType`
        FROM `bbrequest` AS `br`
		LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
        WHERE `br`.`createdby` = \'', @customerId, '\'
        AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
        IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
		'GROUP BY `br`.`assocRequestId`
        HAVING FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) = 0 ORDER BY `br`.`createdts` DESC');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_all_approvalhistory_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_approvalhistory_proc`(
	IN `_customerId` VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionIds` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
	SET @requestIds = '';
	SELECT GROUP_CONCAT(DISTINCT(CONCAT('\'', `requestId`, '\''))) INTO @requestIds FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId;
    IF @requestIds IS NULL THEN
		SET @requestIds = '\'\'';
	END IF;
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'APPROVAL_HISTORY\' AS `requestType`
		FROM `bbrequest` AS `br`
		LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
        LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
        WHERE `br`.`requestId` IN (', @requestIds, ')
        AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
        IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
		'GROUP BY `br`.`assocRequestId`
        ORDER BY `br`.`createdts` DESC');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;
       

DROP PROCEDURE IF EXISTS `fetch_all_approvalrequests_counts_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_approvalrequests_counts_proc`(
	IN `_customerId`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionIds`  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
	SET @allActedRequestIds = (SELECT GROUP_CONCAT(DISTINCT(CONCAT('\'', `requestId`, '\''))) FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId);
	IF @allActedRequestIds IS NULL THEN
		SET @allActedRequestIds = '\'\'';
	END IF;
    
    SET @requestsPendingForApproval = '';
    SELECT GROUP_CONCAT(CONCAT('\'', `pr`.`requestId`, '\'')) INTO @requestsPendingForApproval FROM (SELECT DISTINCT(`camr`.`requestId`) FROM (
		-- ALL USER-BASED matrix requests pending for approval from given customerId
		SELECT `ram`.`requestId`, `ram`.`approvalMatrixId`, `am`.`approvalruleId`, `ar`.`numberOfApprovals`, `ram`.`receivedApprovals`
			FROM `requestapprovalmatrix` AS `ram`
			INNER JOIN `approvalmatrix` AS `am` ON `ram`.`approvalMatrixId` = `am`.`id`
			INNER JOIN `customerapprovalmatrix` AS `cam` ON `cam`.`approvalMatrixId` = `am`.`id`
			INNER JOIN approvalrule AS `ar` ON `am`.`approvalruleId` = `ar`.`id`
				WHERE `ram`.`isGroupRule` = 0 AND `ram`.`requestId` NOT IN (SELECT DISTINCT(`requestId`) FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId)
				GROUP BY `ram`.`requestId`, `ram`.`approvalmatrixId` 
				HAVING `ram`.`receivedApprovals` < (CASE WHEN `ar`.`numberOfApprovals` = -1 THEN COUNT((`cam`.`customerId`)) ELSE `ar`.`numberOfApprovals` END) 
				AND FIND_IN_SET(@customerId, GROUP_CONCAT(`cam`.`customerId`))) AS `camr`
		UNION ALL
		-- ALL SIGNATORY-BASED requests pending for approval from a given customerId
		SELECT DISTINCT(`sgrm`.`requestId`) FROM `signatorygrouprequestmatrix` AS `sgrm`
			INNER JOIN (SELECT `signatoryGroupId` FROM `customersignatorygroup` WHERE `customerId` = @customerId AND `softdeleteflag` = 0) AS `csgd` ON FIND_IN_SET(`csgd`.`signatoryGroupId`, REPLACE(REPLACE(`sgrm`.`pendingGroupList`, '[', ''), ']', ''))
			WHERE `sgrm`.`isApproved` = 0 AND `sgrm`.`requestId` NOT IN (SELECT DISTINCT(`requestId`) FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId)) AS `pr`;
	
    IF @requestsPendingForApproval IS NULL THEN
		SET @requestsPendingForApproval = '\'\'';
	END IF;
    
    SET @sqlStmt = CONCAT('
	SELECT `arr`.`limitGroupId`, `arr`.`limitGroupName`, `arr`.`featureActionId`, `arr`.`featureActionName`, `arr`.`featureId`, `arr`.`featureName`, `arr`.`typeId`, `arr`.`requestType`, COUNT(`arr`.`assocRequestId`) AS `count` FROM (
		(SELECT * FROM (SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
			(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
			(CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
			(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
			GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
			`br`.`createdby`,
			`br`.`createdts`,
			\'PENDING_REQUEST\' AS `requestType`
			FROM `bbrequest` AS `br`
			LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
            INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
			LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
			WHERE `br`.`createdby` = \'', @customerId, '\'
			AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
            IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
			'GROUP BY `br`.`assocRequestId`
			HAVING FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0) AS `pr`)
		UNION ALL
		(SELECT * FROM (SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
			(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
			(CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
			(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
			GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
			`br`.`createdby`,
			`br`.`createdts`,
			\'REQUEST_HISTORY\' AS `requestType`
			FROM `bbrequest` AS `br`
			LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
            INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
			LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
			WHERE `br`.`createdby` = \'', @customerId, '\'
			AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
            IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
			'GROUP BY `br`.`assocRequestId`
			HAVING FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) = 0) AS `requestHistory`)
		UNION ALL
		(SELECT * FROM (SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
			(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
			(CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
			(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
			GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
			`br`.`createdby`,
			`br`.`createdts`,
			\'APPROVAL_HISTORY\' AS `requestType`
			FROM `bbrequest` AS `br`
			LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
            INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
			LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
			WHERE `br`.`requestId` IN (', @allActedRequestIds, ')
			AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
			IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
			'GROUP BY `br`.`assocRequestId`
			ORDER BY `br`.`createdts` DESC) AS `approvalHistory`)
		UNION ALL
			(SELECT * FROM (SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
			(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
			(CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
			(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
			GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
			`br`.`createdby`,
			`br`.`createdts`,
			\'PENDING_APPROVAL\' AS `requestType`
			FROM `bbrequest` AS `br`
			LEFT JOIN `featureaction` AS `fa` ON `br`.`featureActionId` = `fa`.`id` AND `br`.`companyLegalUnit` = `fa`.`companyLegalUnit`
			INNER JOIN `feature` AS `f` ON `f`.`id` = `fa`.`Feature_id` AND `f`.`companyLegalUnit` = `fa`.`companyLegalUnit`
			LEFT JOIN `limitgroup` AS `lg` ON `lg`.`id` = `fa`.`limitgroupId`
			WHERE `br`.`requestId` IN (', @requestsPendingForApproval, ')
			AND `fa`.`status` = \'SID_ACTION_ACTIVE\'',
			IF(`_featureActionIds` IS NULL OR `_featureActionIds` = '', '', CONCAT('AND `fa`.`id` IN (', CONCAT('\'', REPLACE(`_featureActionIds`, ',', '\',\''), '\''), ')')),
			'GROUP BY `br`.`assocRequestId`
			ORDER BY `br`.`createdts` DESC) AS `pendingApprovals`)
	) AS `arr` GROUP BY `arr`.`limitGroupId`, `arr`.`featureActionId`, `arr`.`requestType`
    ');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `update_approvalmode_for_contractandcif_proc`;
DELIMITER $$
CREATE PROCEDURE `update_approvalmode_for_contractandcif_proc`(
	IN `_contractId`        VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_coreCustomerId`    VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_isGroupMatrix`     VARCHAR(2) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN

	-- tasks
    -- 1. mark all rules in approvalmatrix for that contract and cif as soft deleted
    -- 2. delete the existing approvalmatrixtemplate entry for that contract and cif - first from customerapprovalmatrixtemplate and signatorygrouprequestmatrixtemplate, and then from approvalmatrixtemplate
    -- 3. call approvalmatrixtemplate_default_create_proc
    SET @isGroupLevel = IF(`_isGroupMatrix` IS NULL OR `_isGroupMatrix` = '', 0, CAST(`_isGroupMatrix` AS UNSIGNED));
    
    SET @oldIsGroupMatrix = (SELECT CAST(`isGroupLevel` AS UNSIGNED) FROM `approvalmode` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
    
    IF @oldIsGroupLevel = @isGroupLevel THEN
		LEAVE MAINEXEC;
	END IF;

    SET @oldCurrency = (SELECT DISTINCT(`currency`) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
    SET @existingFeatureActionIds = (SELECT GROUP_CONCAT(DISTINCT(`actionId`)) FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);

    UPDATE `approvalmode` SET `isGroupLevel` = @isGroupLevel WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`;
    -- mark all rules in approvalmatrix for that contract and cif as soft deleted
    UPDATE `approvalmatrix` SET `softdeleteflag` = 1 WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`;
    
    -- delete the existing approvalmatrixtemplate entry for that contract and cif - first from customerapprovalmatrixtemplate and signatorygrouprequestmatrixtemplate, and then from approvalmatrixtemplate
    DELETE FROM `customerapprovalmatrixtemplate` WHERE `approvalMatrixId` IN (SELECT `id` FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
    DELETE FROM `signatorygroupmatrixtemplate` WHERE `approvalMatrixId` IN (SELECT `id` FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`);
    DELETE FROM `approvalmatrixtemplate` WHERE `contractId` = `_contractId` AND `coreCustomerId` = `_coreCustomerId`;

    -- call approvalmatrixtemplate_default_create_proc
    CALL `approvalmatrixtemplate_default_create_proc`(@existingFeatureActionIds, `_contractId`, `_coreCustomerId`, `_isGroupMatrix`, @oldCurrency);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalqueue_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);
        
        SET _transactionIds = if(_transactionIds = '' OR _transactionIds = NULL, '\'\'', _transactionIds);
        SET _requestIds = if(_requestIds = '' OR _requestIds = NULL, '\'\'', _requestIds);
       
        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = '';
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ',') FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = '';
		END IF;
        
        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ',') FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN      
			SET @customerGroupIds = '';
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ',') from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = '\'\'';
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM requestapprovalmatrix
        	INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
			INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        WHERE requestapprovalmatrix.isGroupRule = 0 
                AND FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        	AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        	AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
					OR
					(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
		);
		
		IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = '\'\'';
		END IF;
		
        SET @groupIds = @customerGroupIds;
        GROUPREQUESTS_EXTRACT: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM signatorygrouprequestmatrix 
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0  
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN      
					SET @requestIds = '\'\'';
				END IF;
				SET @approvalRequestIds = if(@approvalRequestIds = '' OR @approvalRequestIds IS NULL, @requestIds, CONCAT(@approvalRequestIds, CONCAT(',',@requestIds) ));
			SET @SubStrLen = LENGTH(SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = MID(@groupIds, @SubStrLen + 2, @strLen);
			IF LENGTH(@groupIds) <= 0 THEN
			  LEAVE GROUPREQUESTS_EXTRACT;
			END IF;
		END LOOP GROUPREQUESTS_EXTRACT;
        
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = '''';
		END IF;
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = '';
		END IF;
        
        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0);
        IF @monetaryActions is NULL THEN      
			SET @monetaryActions = '';
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ',') FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = '';
        END IF;
        
        SET @requestIds = if(_requestIds = '\'\'',@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = '\'\''
	        ,concat('FIND_IN_SET(bbrequest.requestId, \'',@requestIds,'\') ')
	        ,concat('FIND_IN_SET(bbrequest.transactionId, \'',_transactionIds, '\') AND  FIND_IN_SET(bbrequest.featureActionId, \'',@monetaryActions,'\')') );
        
		SET @select_statement = concat('SELECT 
					bbrequest.requestId,
                    bbrequest.assocRequestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    bbrequest.isGroupMatrix,
                    bbrequest.companyId,
					bbrequest.accountId,
                    bbrequest.additionalMeta,
                    bbrequest.companyLegalUnit,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`,
					(CASE 
						WHEN  bbrequest.requestId IN (',@approvalRequestIds,') THEN \'true\' 
						ELSE \'false\'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = \'0\') 
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
							(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
							, 
							SUM(
								CASE approvalrule.numberOfApprovals
									WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
									WHEN NULL OR \'\' THEN 0
									ELSE approvalrule.numberOfApprovals
								END
							) 
						)
                        ELSE NULL
					END as requiredApprovals
				FROM bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.requestId'
				);

	PREPARE stmt FROM @select_statement;
    EXECUTE stmt; 
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;
DROP PROCEDURE IF EXISTS `GetExternalPayeesProc`;
DELIMITER $$
CREATE PROCEDURE `GetExternalPayeesProc`(IN userId VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,IN legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
IF legalEntityId != "" THEN
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId;
ELSE
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus  from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT *, (CASE WHEN EXISTS(SELECT `requestId` FROM `bbrequest` WHERE `transactionId` = ea.Id AND `status` = 'Pending') > 0 THEN 'Pending' ELSE NULL END) AS payeeRequestStatus  from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1);
END IF;
END$$
DELIMITER ;
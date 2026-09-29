DROP PROCEDURE IF EXISTS `user_contracts_proc`;

DELIMITER $$
CREATE  PROCEDURE `user_contracts_proc`(
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci
)
BEGIN
	SET SESSION group_concat_max_len = 1000000;
	
	SET @filteredContracts = (SELECT GROUP_CONCAT(DISTINCT `contractaccounts`.`contractId`) 
	FROM `contractaccounts` 
	WHERE `contractaccounts`.`statusDesc` != 'CLOSED' 
	AND `contractaccounts`.`coreCustomerId` IN
	(SELECT `contractcustomers`.`coreCustomerId` 
	FROM `contractcustomers` WHERE `contractcustomers`.`customerId` = _customerId));

    SET @select_statement = ("(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`companyLegalUnit` AS `companyLegalUnit`,
        `contractcustomers`.`contractId` AS `contractId`,
        `contractcustomers`.`autoSyncAccounts` AS `autoSyncAccounts`,
        `contract`.`name` AS `contractName`,
        `contractcustomers`.`isPrimary` AS `isPrimary`,
        `contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `contractcorecustomers`.`isBusiness` AS `isBusiness`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `membergroup`.`id` AS `roleId`,
        `membergroup`.`Name` AS `userRole`
    FROM
        ((((((`contractcustomers`
        LEFT JOIN `contractcorecustomers` ON (((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
            AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `contract` ON ((`contract`.`id` = `contractcorecustomers`.`contractId`)))
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `contract`.`servicedefinitionId`)))
        LEFT JOIN `membergrouptype` ON ((`membergrouptype`.`id` = `servicedefinition`.`serviceType`)))
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
            AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
            AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))");
    SET @isWhereAppened = false;
    SET @shouldAndAppend = false;
    IF(_customerId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`customerId` = ",quote(_customerId));
    END IF;
    IF(_legalEntityId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`companyLegalUnit` = ",quote(_legalEntityId));
    END IF;

    IF(_contractId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      SET @select_statement =  concat(@select_statement ," `contractcustomers`.`contractId` = ",quote(_contractId));
    END IF;
    SET @select_statement =  concat(@select_statement," and `contractcustomers`.`contractId` in (", @filteredContracts,"));");
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER;



DROP PROCEDURE IF EXISTS `user_contracts_withaccounts_proc`;
DELIMITER $$
CREATE  PROCEDURE `user_contracts_withaccounts_proc`(
    in _customerId varchar(50) character set UTF8 collate utf8_general_ci,
    in _contractId varchar(50) character set UTF8 collate utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci
)
begin
    set @select_statement = "(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`companyLegalUnit` AS `companyLegalUnit`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`contractId` AS `contractId`,
        `contractcustomers`.`autoSyncAccounts` AS `autoSyncAccounts`,
        `contract`.`name` AS `contractName`,
        `contractcustomers`.`isPrimary` AS `isPrimary`,
        `contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `contractcorecustomers`.`isBusiness` AS `isBusiness`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `membergroup`.`id` AS `roleId`,
        `membergroup`.`Name` AS `userRole`,
		`contractaccounts`.`accountId`,
		`contractaccounts`.`accountName`,
		`contractaccounts`.`statusDesc`,
		`contractaccounts`.`typeId`
    FROM
        ((`contractcustomers`
        LEFT JOIN `contract` ON (`contract`.`id` = `contractcustomers`.`contractId`)
		LEFT JOIN `contractcorecustomers` ON ((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `contractaccounts` ON ((`contractaccounts`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractaccounts`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `servicedefinition` ON (`servicedefinition`.`id` = `contract`.`servicedefinitionId`)
		LEFT JOIN `membergrouptype` ON (`membergrouptype`.`id` = `servicedefinition`.`serviceType`)
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
        AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
		AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))";

set @isWhereAppened = false;

set @shouldAndAppend = false;



if(_customerId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

           
set @isWhereAppened = true;

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`customerId` = ", quote(_customerId));
end if;




if(_legalEntityId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @shouldAndAppend = true;
end if;

if(@isWhereAppened
and @shouldAndAppend) then
            set @select_statement = CONCAT(@select_statement , " and");

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`companyLegalUnit` = ", quote(_legalEntityId));
end if;



if(_contractId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @shouldAndAppend = true;
end if;

if(@isWhereAppened
and @shouldAndAppend) then
            set @select_statement = CONCAT(@select_statement , " and");

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`contractId` = ", quote(_contractId));
end if;

set @select_statement =  concat(@select_statement ,");");


prepare stmt from @select_statement;

execute stmt;

deallocate prepare stmt;
end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$
CREATE  PROCEDURE `fetch_approvalqueue_proc`(IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                             IN _transactionIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                             IN _requestIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
											 IN _featureactionlist text CHARACTER SET UTF8 COLLATE utf8_general_ci)
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
			SET @approvalRequestIds = '\'\'';
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
				GROUP BY bbrequest.assocRequestId'
				);

	PREPARE stmt FROM @select_statement;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `create_new_composite_request_in_approvalqueue_proc`;
DELIMITER $$

CREATE  PROCEDURE `create_new_composite_request_in_approvalqueue_proc`(IN _contractCifMatrixIdsJSON text,
                                                                                         IN _assocRequestId varchar(64),
                                                                                         IN _confirmationNumber varchar(128),
                                                                                         IN _featureActionId varchar(64),
                                                                                         IN _accountId varchar(45),
                                                                                         IN _createdBy varchar(32),
                                                                                         IN _comments varchar(512),
                                                                                         IN _additionalMetaJSON text)
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
    CALL `fetch_statement_for_requests_with_approvalmatrixinfo_proc`(@requestIds, '0', '', '0', @outStmt);
    PREPARE STMT FROM @outStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER;


DROP PROCEDURE IF EXISTS `fetch_statement_for_requests_with_approvalmatrixinfo_proc`;
DELIMITER $$

CREATE  PROCEDURE `fetch_statement_for_requests_with_approvalmatrixinfo_proc`(IN _requestIds text,
                                                                                                IN _isAssociationId varchar(2),
                                                                                                IN _contractCifMapJSON text,
                                                                                                IN _isActiveRulesFetch varchar(2),
                                                                                                OUT _response varchar(16000))
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
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `am`.`contractId`, `am`.`coreCustomerId`, `br`.`featureActionId`, `br`.`accountId`, `br`.`status`, `br`.`createdby`, `br`.`createdts`, `br`.`requiredSets`, `br`.`receivedSets`, `ram`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, `ram`.`receivedApprovals`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, NULL AS `groupList`, NULL AS `pendingGroupList`, NULL AS `groupRuleValue`, NULL AS `isGroupRuleApproved`, GROUP_CONCAT(`cam`.`customerId`) AS `approverIds`, `br`.`additionalMeta`,
            (SELECT GROUP_CONCAT(\'{"\',`cam`.`customerId`,\'":"\',`c`.`userName`,\'"}\') FROM `customer` AS `c` WHERE `c`.`id` = `cam`.`customerId`) AS `approverUserNames`
			FROM `bbrequest` AS `br` LEFT JOIN `requestapprovalmatrix` AS `ram` ON `br`.`requestId` = `ram`.`requestId`
			LEFT JOIN `customerapprovalmatrix` AS `cam` ON `cam`.`approvalMatrixId` = `ram`.`approvalMatrixId` LEFT JOIN `approvalmatrix` AS `am` ON `am`.`id` = `ram`.`approvalMatrixId` WHERE `br`.`requestId` = \'', @requestId, '\' ', IF(@isCifLevelFilter = 1, CONCAT('AND `am`.`contractId` IN (', @contractIds, ') AND `am`.`coreCustomerId` IN (', @cifIds, ')'), ''), ' GROUP BY `br`.`requestId`, `ram`.`approvalMatrixId`)');
		ELSE
			SET @sqlSubStmt = CONCAT('(SELECT `br`.`requestId`, `br`.`assocRequestId`, `br`.`transactionId`, `am`.`contractId`, `am`.`coreCustomerId`, `br`.`featureActionId`, `br`.`accountId`, `br`.`status`, `br`.`createdby`, `br`.`createdts`, `br`.`requiredSets`, `br`.`receivedSets`, `ram`.`approvalMatrixId`, `am`.`limitTypeId`, `am`.`approvalruleId`, `ram`.`receivedApprovals`, CAST(`br`.`isGroupMatrix` AS UNSIGNED) AS `isGroupMatrix`, `sgrm`.`groupList`, `sgrm`.`pendingGroupList`, `sgrm`.`groupRuleValue`, CAST(`sgrm`.`isApproved` AS UNSIGNED) AS `isGroupRuleApproved`,
            (SELECT GROUP_CONCAT(`cust`.`customerid`) FROM `customersignatorygroup` AS `cust` WHERE FIND_IN_SET(`cust`.`signatoryGroupId`, (SELECT (REPLACE(REPLACE(REPLACE(`sgrm`.`pendingGroupList`,\']\',\'\'),\'[\',\'\'),\'"\',\'\'))))) AS `approverIds`, `br`.`additionalMeta`,
            (SELECT GROUP_CONCAT(\'{"\',`cust`.`customerid`,\'":"\',`c`.`userName`,\'"}\') FROM `customersignatorygroup` AS `cust`, `customer` AS `c` WHERE FIND_IN_SET(`cust`.`signatoryGroupId`, (SELECT (REPLACE(REPLACE(REPLACE(`sgrm`.`pendingGroupList`,\']\',\'\'),\'[\',\'\'),\'"\',\'\')))) AND `c`.`id` = `cust`.`customerid`) AS `approverUserNames`
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
    select @sqlStmt into _response;
END$$
DELIMITER;



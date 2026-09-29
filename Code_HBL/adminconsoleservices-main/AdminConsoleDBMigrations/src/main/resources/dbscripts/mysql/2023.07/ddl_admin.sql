ALTER TABLE `internationalfundtransfers` MODIFY `bankName` VARCHAR(200) ;
ALTER TABLE `interbankfundtransfers` MODIFY `bankName` VARCHAR(200) ;
ALTER TABLE `intrabanktransfers` MODIFY `bankName` VARCHAR(200) ;
ALTER TABLE `externalaccount` MODIFY `bankName` VARCHAR(200) ;

DROP PROCEDURE IF EXISTS `fetch_all_pendingrequests_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_all_pendingrequests_proc`(IN _customerId varchar(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _featureActionIds text CHARACTER SET UTF8 COLLATE utf8_general_ci)
MAINEXEC : BEGIN
	SET @customerId = `_customerId`;
    IF @customerId IS NULL THEN
		SET @customerId = '';
	END IF;
	
    SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);
       
        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = '';
		END IF;
        
        IF _featureActionIds is NULL THEN      
			LEAVE MAINEXEC;
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

    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`additionalMeta`,`br`.`companyLegalUnit`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'PENDING_REQUEST\' AS `requestType`,
	(CASE 
	WHEN  `br`.`requestId` IN (',@approvalRequestIds,') THEN \'true\' 
						ELSE \'false\'
					END)
					 as `amIApprover`,
					 `br`.`isGroupMatrix`,
					(CASE
						WHEN `br`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = `br`.`requestId` AND `softdeleteflag` = \'0\') 
							as receivedApprovals,
					(CASE
						WHEN `br`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`
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
	
	SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);
       
        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = '';
		END IF;
        
        IF _featureActionIds is NULL THEN      
			LEAVE MAINEXEC;
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
	
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`additionalMeta`,`br`.`companyLegalUnit`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'REQUEST_HISTORY\' AS `requestType`,
		(CASE 
	WHEN  `br`.`requestId` IN (',@approvalRequestIds,') THEN \'true\' 
						ELSE \'false\'
					END)
					 as `amIApprover`,
					 `br`.`isGroupMatrix`,
					(CASE
						WHEN `br`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = `br`.`requestId` AND `softdeleteflag` = \'0\') 
							as receivedApprovals,
					(CASE
						WHEN `br`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`
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
	
	SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);
       
        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = '';
		END IF;
        
        IF _featureActionIds is NULL THEN      
			LEAVE MAINEXEC;
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
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`additionalMeta`,`br`.`companyLegalUnit`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'PENDING_APPROVAL\' AS `requestType`,
	(CASE 
	WHEN  `br`.`requestId` IN (',@approvalRequestIds,') THEN \'true\' 
						ELSE \'false\'
					END)
					 as `amIApprover`,
					 `br`.`isGroupMatrix`,
					(CASE
						WHEN `br`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = `br`.`requestId` AND `softdeleteflag` = \'0\') 
							as receivedApprovals,
					(CASE
						WHEN `br`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`
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
	
	SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);
       
        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = '';
		END IF;
        
        IF _featureActionIds is NULL THEN      
			LEAVE MAINEXEC;
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
	
	SET @requestIds = '';
	SELECT GROUP_CONCAT(DISTINCT(CONCAT('\'', `requestId`, '\''))) INTO @requestIds FROM `bbactedrequest` WHERE `action` IN ('Approved', 'Rejected', 'Withdrawn') AND `createdby` = @customerId;
    IF @requestIds IS NULL THEN
		SET @requestIds = '\'\'';
	END IF;
    SET @sqlStmt = CONCAT('SELECT GROUP_CONCAT(`br`.`requestId`) AS `compositeRequestIds`, `br`.`assocRequestId`, `br`.`additionalMeta`,`br`.`companyLegalUnit`, `br`.`transactionId`, `br`.`featureActionId`, `fa`.`name` AS `featureActionName`, `fa`.`Feature_id` AS `featureId`, `f`.`name` AS `featureName`, `fa`.`Type_id` AS `typeId`,
		(CASE WHEN `fa`.`limitgroupId` IS NULL THEN \'OTHER\' ELSE `fa`.`limitgroupId` END) AS `limitGroupId`,
        (CASE WHEN `lg`.`name` IS NULL THEN \'Other\' ELSE `lg`.`name` END) AS `limitGroupName`,
		(CASE WHEN FIND_IN_SET(\'Pending\', GROUP_CONCAT(`br`.`status`)) > 0 THEN \'Pending\' ELSE SUBSTRING_INDEX(GROUP_CONCAT(`br`.`status`), \',\', 1) END) AS `assocStatus`,
		GROUP_CONCAT(`br`.`status`) `compositeStatuses`,
		`br`.`createdby`,
		`br`.`createdts`,
        \'APPROVAL_HISTORY\' AS `requestType`,
	(CASE 
	WHEN  `br`.`requestId` IN (',@approvalRequestIds,') THEN \'true\' 
						ELSE \'false\'
					END)
					 as `amIApprover`,
					 `br`.`isGroupMatrix`,
					(CASE
						WHEN `br`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = `br`.`requestId` AND `softdeleteflag` = \'0\') 
							as receivedApprovals,
					(CASE
						WHEN `br`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`
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

USE `dbxdb`;
DROP procedure IF EXISTS `fetch_requests_with_approvalmatrixinfo_proc`;
DELIMITER $$

USE `dbxdb`$$
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
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$

DELIMITER ;
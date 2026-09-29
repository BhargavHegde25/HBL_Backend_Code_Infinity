ALTER TABLE `archivedalerthistory` ADD COLUMN `coreCustomerId` VARCHAR(45) NULL AFTER `Customer_Id`;


DROP procedure IF EXISTS `group_features_actions_view_proc`;

DELIMITER $$
CREATE PROCEDURE `group_features_actions_view_proc`
(
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE select_statement TEXT;
SET @select_statement = 'SELECT groupactionlimit.Group_id, 
groupactionlimit.Action_id, 
groupactionlimit.LimitType_id, 
groupactionlimit.value, 
groupactionlimit.id AS groupactionlimit_id,
groupactionlimit.softdeleteflag AS softdelete, 
membergroup.Type_id, 
membergroup.Name AS Group_name,
membergroup.Description AS Group_description,
featureaction.name AS Action_name,
featureaction.description AS Action_description, 
featureaction.Type_id AS Action_Type_id, 
featureaction.Feature_id, 
featureaction.isMFAApplicable, 
featureaction.isAccountLevel,
featureaction.isPrimary, 
featureaction.DisplaySequence AS Action_displaysequence, 
featureaction.dependency AS Action_dependency, 
featureaction.status AS actionStatus,
featureaction.companyLegalUnit AS companyLegalUnit,
accesspolicy.name AS accessPolicy, 
featureaction.accesspolicyId, 
featureaction.limitgroupId, 
limitgroup.name AS limitGroup, 
actionlevel.name AS actionlevel, 
featureaction.actionlevelId,
feature.name AS featureName, 
feature.name AS Feature_name, 
feature.description AS Feature_description, 
feature.Type_id AS Feature_Type_id, 
feature.Status_id AS Feature_Status_id,
feature.DisplaySequence AS Feature_displaysequence, 
feature.isPrimary AS Feature_isPrimary
FROM groupactionlimit 
LEFT OUTER JOIN
membergroup ON (membergroup.id = groupactionlimit.Group_id
and membergroup.companyLegalUnit = groupactionlimit.companyLegalUnit) 
LEFT OUTER JOIN
featureaction ON (featureaction.id = groupactionlimit.Action_id
and featureaction.companyLegalUnit = groupactionlimit.companyLegalUnit) 
LEFT OUTER JOIN
feature ON (feature.id = featureaction.Feature_id
and  feature.companyLegalUnit = featureaction.companyLegalUnit) 
LEFT OUTER JOIN
accesspolicy ON (featureaction.accesspolicyId = accesspolicy.id) 
LEFT OUTER JOIN
limitgroup ON (featureaction.limitgroupId = limitgroup.id) 
LEFT OUTER JOIN
actionlevel ON (featureaction.actionlevelId = actionlevel.id
and featureaction.companyLegalUnit = actionlevel.companyLegalUnit)';

IF _groupId is not null and CHAR_LENGTH(RTRIM(_groupId))>0 THEN 
SET @select_statement = CONCAT (@select_statement ,'where groupactionlimit.Group_id =''', _groupId,'''', 'and `groupactionlimit`.`companyLegalUnit` =''',_companyLegalUnit ,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;


DROP VIEW IF EXISTS `actiondependency_view`;

CREATE VIEW `actiondependency_view` AS
SELECT
    `dependentactions`.`dependentactionId` AS `actionName`,
    `dependentactions`.`actionId` AS `dependencyAction`,
    `dependentactions`.`featureId` AS `featureId`,
	`dependentactions`.`companyLegalUnit` AS `companyLegalUnit`,
    `featureaction`.`status` AS `actionStatus`,
    `feature`.`Status_id` AS `featureStatus`
FROM
    ((`dependentactions`
left JOIN `featureaction` ON
    ((`dependentactions`.`dependentactionId` = `featureaction`.`id`)))
left JOIN `feature` ON
    ((`dependentactions`.`featureId` = `feature`.`id`)));
	
	
DROP procedure IF EXISTS `customer_actions_proc`;
DELIMITER $$

USE `dbxdb`$$
CREATE PROCEDURE `customer_actions_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 100000000;

SET @orgId = (SELECT Organization_Id from customer where id=_customerId);

IF @orgId is null THEN 
  SET @orgId = "";
END IF;

SET @active_features = (SELECT group_concat(id SEPARATOR ",") from feature where Status_id = 'SID_FEATURE_ACTIVE');

IF @active_features is null THEN 
  SET @active_features = "";
END IF;

SET @active_actions = (SELECT group_concat(id SEPARATOR ",") from featureaction where FIND_IN_SET(Feature_id, @active_features));

IF @active_actions is null THEN 
  SET @active_actions = "";
END IF;

SET @organization_active_features = (SELECT group_concat(featureId SEPARATOR ",") from organisationfeatures where (featureStatus is null OR featureStatus = 'SID_FEATURE_ACTIVE') AND FIND_IN_SET(featureId, @active_features) AND organisationId = @orgId);

IF @organization_active_features is null THEN 
  SET @organization_active_features = "";
END IF;

SET @organization_active_actions = (SELECT group_concat(id SEPARATOR ",") from featureaction where FIND_IN_SET(Feature_id, @organization_active_features));

IF @organization_active_actions is null THEN 
  SET @organization_active_actions = "";
END IF;

SET @business_customer_enabled_actions = (SELECT group_concat(Action_id SEPARATOR ",") from customeraction where isAllowed = '1' AND Customer_id =_customerId AND FIND_IN_SET(Action_id, @organization_active_actions));

IF @business_customer_enabled_actions is null THEN 
    SET @business_customer_enabled_actions = "";
END IF;

SET @customer_disabled_actions = (SELECT group_concat(Action_id SEPARATOR ",") from customeraction where isAllowed = '0' AND Account_id is NULL AND Customer_id =_customerId);

IF @customer_disabled_actions is null THEN 
    SET @customer_disabled_actions = "";
END IF;

SET @customer_groups = (SELECT group_concat(Group_id SEPARATOR ",") from customergroup where Customer_id = _customerId);

IF @customer_groups is null THEN 
  SET @customer_groups = "";
END IF;

SET @group_actions = (SELECT group_concat(Action_id SEPARATOR ",") from groupactionlimit where FIND_IN_SET(Group_id, @customer_groups));

IF @group_actions is null THEN 
  SET @group_actions = "";
END IF;

IF @orgId = "" THEN 
  SET @active_feature_condition = concat("`feature`.`Status_id` = 'SID_FEATURE_ACTIVE' AND FIND_IN_SET(`featureaction`.`id`, '",@group_actions,"') ");
  ELSE 
    SET @active_feature_condition = concat("FIND_IN_SET(`featureaction`.`id`, '",@organization_active_actions,"') AND FIND_IN_SET(`featureaction`.`id`, '",@group_actions,"') ");
END IF;

set @select_statement = concat("SELECT 
        `customeraction`.`Customer_id` AS `Customer_id`,
        `customeraction`.`Account_id` AS `Account_id`,
        IF(`customeraction`.`isAllowed` = '1', 'true', 'false') AS `isAllowed`,
        `featureaction`.`id` AS `Action_id`,
        IF(`featureaction`.`isAccountLevel` = '1', 'true', 'false') AS `isAccountLevel`,
        `feature`.`Status_id` AS `Feature_Status_id`,
        `feature`.`id` AS `Feature_id`,
        `customeraction`.`RoleType_id` AS `RoleType_id`,
      `customeraction`.`LimitType_id` AS `LimitType_id`,
        `customeraction`.`value` AS `value`
    FROM
        (`customeraction`
      LEFT JOIN `featureaction` ON (`featureaction`.`id` = `customeraction`.`Action_id`)
      LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
    where `customeraction`.`Customer_id` = ", quote(_customerId)," and `featureaction`.`status` = 'SID_ACTION_ACTIVE' and ",@active_feature_condition);
    
    IF(_actionId != '') THEN
    set @select_statement =  concat(@select_statement ," and `customeraction`.`Action_id` = ",quote(_actionId));
  END IF;
  
    set @select_statement =  concat(@select_statement ," 
    UNION SELECT 
        `customergroup`.`Customer_id` AS `Customer_id`,
        NULL AS `Account_id`,
        IF(`membergroup`.`Type_id` = 'TYPE_ID_BUSINESS', 
          IF(FIND_IN_SET(`featureaction`.`id`, '",@business_customer_enabled_actions,"'), 'true', 'false') , 'true') AS `isAllowed`,
        `featureaction`.`id` AS `Action_id`,
        IF(`featureaction`.`isAccountLevel` = '1', 'true', 'false') AS `isAccountLevel`,
        `feature`.`Status_id` AS `Feature_Status_id`,
        `feature`.`id` AS `Feature_id`,
        `membergroup`.`Type_id` AS `RoleType_id`,
        `groupactionlimit`.`LimitType_id` AS `LimitType_id`,
        `groupactionlimit`.`value` AS `value`
    FROM
        (`customergroup`
        LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
        LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
        LEFT JOIN `featureaction` ON (`featureaction`.`id` = `groupactionlimit`.`Action_id`)
        LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
    where `customergroup`.`Customer_id` =", quote(_customerId), " and `feature`.`Status_id` = 'SID_FEATURE_ACTIVE'
      and NOT FIND_IN_SET(`groupactionlimit`.`Action_id`, '",@customer_disabled_actions,"')
        and `featureaction`.`status` = 'SID_ACTION_ACTIVE'
      and ", @active_feature_condition );
    
    IF(_actionId != '') THEN
      set @select_statement =  concat(@select_statement ," and `groupactionlimit`.`Action_id` = ",quote(_actionId));
    END IF;
    
    -- select @select_statement;
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

DROP procedure IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$

USE `dbxdb`$$
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
END $$

DELIMITER ;
		
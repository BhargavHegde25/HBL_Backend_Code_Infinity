CREATE TABLE `jobtype` (
`jobType` varchar(50) NOT NULL,
`jobName` varchar(50) DEFAULT NULL,
`createdby` varchar(50) DEFAULT NULL,
`modifiedby` varchar(50) DEFAULT NULL,
`createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
PRIMARY KEY (`jobType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `infinityjob` (
`id` varchar(50) NOT NULL,
`data` text,
`status` varchar(50) NOT NULL DEFAULT 'SID_JOB_INPROGRESS',
`createdby` varchar(50) DEFAULT NULL,
`modifiedby` varchar(50) DEFAULT NULL,
`createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
`type` varchar(50) NOT NULL,
PRIMARY KEY (`id`),
KEY `FK_InfinityJob_Status_` (`status`),
KEY `infinityjob_FK` (`type`),
CONSTRAINT `FK_InfinityJob_Status_` FOREIGN KEY (`status`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT `infinityjob_FK` FOREIGN KEY (`type`) REFERENCES `jobtype` (`jobType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `infinityjoblog` (
`id` varchar(50) NOT NULL,
`data` text,
`createdby` varchar(50) DEFAULT NULL,
`modifiedby` varchar(50) DEFAULT NULL,
`createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
`lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
`softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP procedure IF EXISTS `fetch_restrictive_featureactionlimits_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_restrictive_featureactionlimits_proc`(
IN _locale varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accessPolicyIdList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @select_statement = '';
SET @action_select_statement = '';
SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id )");
IF(_accessPolicyIdList != '') THEN
SET @action_select_statement = CONCAT(@action_select_statement , "WHERE FIND_IN_SET(featureaction.accessPolicyId ,",QUOTE(_accessPolicyIdList),")");
END IF;
SET @action_select_statement = CONCAT(@action_select_statement,")");
IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , ")");
SET @action_select_statement = @select_statement;
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
SET @select_statement = CONCAT(@select_statement , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , ")");
SET @action_select_statement = @select_statement;
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT("(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , ")");
SET @action_select_statement = @select_statement;
END IF;
IF(_userId != '') THEN
SET @select_statement = CONCAT("(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
END IF;
SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , ")");
SET @action_select_statement = @select_statement;
END IF;
SET @select_statement = "( SELECT
feature.id AS featureId,
featuredisplaynamedescription.displayName AS featureName,
featuredisplaynamedescription.displayDescription AS featureDescription,
feature.Status_id AS featureStatus,
featureaction.status AS actionStatus,
featureaction.id AS actionId,
actiondisplaynamedescription.displayName AS actionName,
actiondisplaynamedescription.displayDescription AS actionDescription,
featureaction.isAccountLevel AS isAccountLevel,
featureaction.Type_id AS typeId,
featureaction.limitgroupId as limitGroupId,
featureaction.accesspolicyId as accessPolicyId,
featureaction.actionlevelId as actionLevelId,
null as limitTypeId,
null as fiLimitValue";
if(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
END IF;
IF _roleId != '' AND _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", IF(groupactionlimit.isNewAction = '1' OR contractactionlimit.isNewAction = '1', '1', '0') as isNewAction");
ELSE
IF _roleId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.isNewAction AS isNewAction");
ELSEIF _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.isNewAction AS isNewAction");
END IF;
END IF;
SET @select_statement = CONCAT(@select_statement , " FROM feature
LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , "LEFT JOIN groupactionlimit ON (groupactionlimit.Action_id = featureaction.id)");
END IF;
SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY' AND featureaction.id IN " , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
SET @select_statement = CONCAT(@select_statement , " UNION (SELECT
feature.id AS featureId,
featuredisplaynamedescription.displayName AS featureName,
featuredisplaynamedescription.displayDescription AS featureDescription,
feature.Status_id AS featureStatus,
featureaction.status AS actionStatus,
featureaction.id AS actionId,
actiondisplaynamedescription.displayName AS actionName,
actiondisplaynamedescription.displayDescription AS actionDescription,
featureaction.isAccountLevel AS isAccountLevel,
featureaction.Type_id AS typeId,
featureaction.limitgroupId as limitGroupId,
featureaction.accesspolicyId as accessPolicyId,
featureaction.actionlevelId as actionLevelId,
actionlimit.LimitType_id as limitTypeId,
actionlimit.value as fiLimitValue");
IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");
END IF;
IF _roleId != '' AND _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", IF(groupactionlimit.isNewAction = '1' OR contractactionlimit.isNewAction = '1', '1', '0') as isNewAction");
ELSE
IF _roleId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.isNewAction AS isNewAction");
ELSEIF _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.isNewAction AS isNewAction");
END IF;
END IF; SET @select_statement = CONCAT(@select_statement , " FROM feature
LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND
servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND
groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND
contractactionlimit.limitTypeId = actionlimit.LimitType_id)");
END IF;
SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Group_id = " , QUOTE(_roleId));
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.coreCustomerId = " , QUOTE(_coreCustomerId));
END IF;
SET @select_statement = CONCAT(@select_statement , " AND featureaction.id IN " , @action_select_statement);
SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

CREATE TABLE `excludedcustomeraction` (
  `id` varchar(50) NOT NULL,
  `RoleType_id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `contractId` varchar(45) DEFAULT NULL,            
  `coreCustomerId` varchar(45) DEFAULT NULL,
  `featureId` varchar(45) DEFAULT NULL,
  `Action_id` varchar(255) DEFAULT NULL,
  `Account_id` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELIMITER $$
CREATE PROCEDURE `excluded_customeraction_save_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\'',UUID(),'\'',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
             set @query = concat('INSERT INTO excludedcustomeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;

CREATE TABLE `excludedcustomroleactionlimits` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `customRole_id` bigint(20) NOT NULL,
 `contractId` varchar(50) DEFAULT NULL,
  `coreCustomerId` varchar(50) DEFAULT NULL,
  `featureId` varchar(50) DEFAULT NULL,
  `action_id` varchar(255) NOT NULL,
  `account_id` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12903 DEFAULT CHARSET=utf8;



DROP procedure IF EXISTS `customer_contract_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_contract_delete_proc`(in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
       
       SET @contract_statement = 'DELETE FROM contractcustomers where';
       SET @suspended_statement = 'DELETE FROM suspendedcustomers where';
       SET @accounts_statement = 'DELETE FROM customeraccounts where ';
       SET @excluded_accounts_statement = 'DELETE FROM excludedcustomeraccounts where ';
       SET @group_statement = 'DELETE FROM customergroup where ';
       SET @action_statement = 'DELETE FROM customeraction where ';
       SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
       SET @excluded_action_statement = 'DELETE FROM excludedcustomeraction where ';
       

       SET @where_clause = '';
       SET @where_clause1 = '';
       if(customerId != '') THEN
              SET @where_clause = CONCAT(@where_clause , '`customerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(customerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`Customer_id` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(customerId));
       END IF;
       
       IF(contractId != '') THEN
              IF(@where_clause != '') THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(contractId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
       END IF;
       
       IF(coreCustomerId != '') THEN
              IF(@where_clause != '') THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
       END IF;
       
       IF(@where_clause != '') THEN     
              SET @contract_statement = CONCAT(@contract_statement , @where_clause);
              SET @suspended_statement = CONCAT(@suspended_statement , @where_clause);
              SET @accounts_statement = CONCAT(@accounts_statement , @where_clause1); 
              SET @excluded_accounts_statement = CONCAT(@excluded_accounts_statement , @where_clause1);
              SET @group_statement = CONCAT(@group_statement , @where_clause1); 
              SET @action_statement = CONCAT(@action_statement , @where_clause1);     
              SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);    
              SET @excluded_action_statement = CONCAT(@excluded_action_statement , @where_clause1);
                     
       
              PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @suspended_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
       END if;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `excluded_customrole_actionlimits_create_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20))
BEGIN
      DELETE FROM excludedcustomroleactionlimits where excludedcustomroleactionlimits.customRole_id = _customRoleId;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            set @query = concat('INSERT INTO excludedcustomroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;


DROP procedure IF EXISTS `customrole_contract_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `customrole_contract_delete_proc`(in customRoleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
       
       SET @contract_statement = 'DELETE FROM contractcustomrole where';
       SET @accounts_statement = 'DELETE FROM customroleaccounts where ';
       SET @excludedaccounts_statement = 'DELETE FROM excludedcustomroleaccounts where ';
       SET @action_statement = 'DELETE FROM customroleactionlimits where ';
       SET @excluded_action_statement = 'DELETE FROM excludedcustomroleactionlimits where ';
       SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
       
       SET @where_clause = '';
       SET @where_clause1 = '';
       SET @where_clause2 = '';
       if(customRoleId != '') THEN
              SET @where_clause = CONCAT(@where_clause , '`customRoleId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(customRoleId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`customRole_id` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(customRoleId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`Customer_id` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(customRoleId));
       
       END IF;
       
       IF(contractId != '') THEN
              IF(@where_clause != '') THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
                     SET @where_clause2 = CONCAT(@where_clause2 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(contractId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`contractId` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(contractId));
       END IF;
       
       IF(coreCustomerId != '') THEN
              IF(@where_clause != '') THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
                     SET @where_clause2 = CONCAT(@where_clause2 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`coreCustomerId` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(coreCustomerId));
       END IF;
       
       IF(@where_clause != '') THEN     
              SET @contract_statement = CONCAT(@contract_statement , @where_clause);  
              SET @accounts_statement = CONCAT(@accounts_statement , @where_clause);
              SET @excludedaccounts_statement = CONCAT(@excludedaccounts_statement , @where_clause);
              SET @action_statement = CONCAT(@action_statement , @where_clause1);
              SET @excluded_action_statement = CONCAT(@excluded_action_statement , @where_clause1);
              SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause2);    
       
              PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excludedaccounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
       END if;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS user_securityattributes_get_proc;

DELIMITER $$
CREATE PROCEDURE `user_securityattributes_get_proc`(
in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SET SESSION group_concat_max_len = 10000000;

SET @userAssociatedCoreCustomers =  (SELECT group_concat(distinct contractcustomers.coreCustomerId SEPARATOR ",") FROM contractcustomers WHERE contractcustomers.customerId = _userId);

SET @userAssociatedContracts =  (SELECT group_concat(distinct contractcustomers.contractId SEPARATOR ",") FROM contractcustomers WHERE contractcustomers.customerId = _userId);

SET @userAssociatedServiceDefinitions = (SELECT group_concat(distinct contract.servicedefinitionId SEPARATOR ",") FROM contract WHERE FIND_IN_SET(contract.id,@userAssociatedContracts) 
                                        AND contract.statusId = 'SID_CONTRACT_ACTIVE');

SET @userAssociatedGroups =  (SELECT group_concat(distinct customergroup.Group_id SEPARATOR ",") FROM customergroup WHERE customergroup.Customer_id = _userId);

SET @actionsAtCoreCustomers = (SELECT group_concat(distinct contractactionlimit.actionId SEPARATOR ",") FROM contractactionlimit WHERE FIND_IN_SET(contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers));

SET @newActionsAtCoreCustomers = (SELECT group_concat(distinct contractactionlimit.actionId SEPARATOR ",") FROM contractactionlimit WHERE FIND_IN_SET(contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) and contractactionlimit.isNewAction = '1');

SET @actionsAtServiceDefinitions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE FIND_IN_SET(servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions));

SET @actionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups));

SET @newActionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups) and groupactionlimit.isNewAction = '1');

SET @actionsAtuser = (SELECT group_concat(distinct customeraction.Action_id SEPARATOR ",") FROM customeraction WHERE customeraction.Customer_id = _userId 
                     AND (customeraction.isAllowed ='1' OR customeraction.isAllowed = true));
                     
SET @activeFeaturesAtFI = (SELECT group_concat(distinct feature.id SEPARATOR ",") FROM feature WHERE Status_id = 'SID_FEATURE_ACTIVE'); 

SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE' AND
                                FIND_IN_SET(featureaction.id,@actionsAtuser) AND
                                FIND_IN_SET(featureaction.id,@actionsAtGroups) AND
                                FIND_IN_SET(featureaction.id,@actionsAtServiceDefinitions) AND
                                FIND_IN_SET(featureaction.id,@actionsAtCoreCustomers) AND 
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));
                               
SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE' AND(
                                FIND_IN_SET(featureaction.id,@intersectedUserActions) OR
                                FIND_IN_SET(featureaction.id,@newActionsAtCoreCustomers) OR
                                FIND_IN_SET(featureaction.id,@newActionsAtGroups)) AND
                                FIND_IN_SET(featureaction.id,@actionsAtServiceDefinitions) AND 
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));                               
                                
SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (SELECT group_concat(distinct featureaction.Feature_id SEPARATOR ",") FROM featureaction WHERE 
                                  FIND_IN_SET(featureaction.id,@intersectedUserActions));
                                  
SELECT @intersectedUserFeatures AS features;
                                  
                       
END$$
DELIMITER ;


DROP procedure IF EXISTS `contract_actionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_actionlimits_create_proc`(
  IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      
      DECLARE tempLimitValue varchar(255) DEFAULT "";
      
      SET SESSION group_concat_max_len = 100000000;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      set @serviceDefinitionId = "";
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\"',UUID(),'\"',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
            
            set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',2 ), '\"', -1 );
            set @customerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 );
            set @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4), ',\"', -1 );
            set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );
            set @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            set @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );
            set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            
             SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',6 ),'\"');
             
              IF isnull(@serviceDefinitionId) OR @serviceDefinitionId = "" THEN
                SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @contractId);
                     END IF;
             
                     IF @limitId != '@' AND  @limitValue != '@' THEN 
              SET @limitAtFI = (SELECT actionlimit.value FROM actionlimit WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId);
      
              SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId
                                                   AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId );
                       SET tempLimitValue = LEAST(@limitAtFI,@limitATServiceDefinition,@limitValue);
            END IF;
            
            IF !isnull(tempLimitValue) AND tempLimitValue !='' THEN
               SET @limitValue = tempLimitValue;
                     END IF;
            

            SET @contarctFeatures = (select group_concat(contractfeatures.id SEPARATOR ",") from contractfeatures where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId = @featureId);
                     
            
             
             SET @serviceDefinitionActions = (select group_concat(servicedefinitionactionlimit.id SEPARATOR ",") from servicedefinitionactionlimit where actionId=@actionId AND 
                                                               serviceDefinitionId =@serviceDefinitionId);
             
                      SET @existingActionLimitRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId AND 
                                      limitTypeId = @limitId );
             
             SET @existingActionRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId);
                                     
                     IF !isnull(@contarctFeatures) AND @contarctFeatures != "" AND !isnull(@serviceDefinitionActions) AND @serviceDefinitionActions != ""  THEN
                IF !isnull(@existingActionLimitRecords) AND @existingActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE contractactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingActionLimitRecords,'\'',';'); 
                           ELSE 
                                  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords) OR @existingActionRecords = "") THEN
                                       SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId, isNewAction) VALUES (',@recordsDataWithoutLimits,');');
                               ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
                                         SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId, isNewAction, limitTypeId,value) VALUES (',@recordsData,');');
                                  END IF;
                           END IF;
                     END IF;
            END IF;
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;

DROP procedure IF EXISTS `contract_action_limit_update`;

DELIMITER $$
CREATE PROCEDURE `contract_action_limit_update`(
  IN _contractActionLimit LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
       set @numOfRecords = LENGTH(_contractActionLimit) - LENGTH(REPLACE(_contractActionLimit, '|', '')) + 1;
  updateRecords : LOOP 
     set index1 = index1 + 1;
              IF index1 = @numOfRecords + 1 THEN 
                     LEAVE updateRecords;
              else
                  set @contractValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_contractActionLimit, '|', index1), '|', -1 );
                     set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',1 ), '\"', -1 );
                     set @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',2), ',\"', -1 );
                     set @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',3), ',\"', -1 );
                     set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',4), ',\"', -1 );
                     set @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',5), ',\"', -1 );
                     set @limitTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',6), ',\"', -1 );
                     set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, ',\"',-1 ), '\"', 1 );
            set @num = cast(@limitValue AS DECIMAL(20,2));
                     UPDATE contractactionlimit SET value = @num where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId; 
              END if;
              
       END LOOP updateRecords;
END$$

DELIMITER ;

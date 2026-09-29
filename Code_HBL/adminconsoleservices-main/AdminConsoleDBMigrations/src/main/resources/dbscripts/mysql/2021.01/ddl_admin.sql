DROP TABLE IF EXISTS `bulkpaymenttemplatepos`;
DROP TABLE IF EXISTS `bulkpaymenttemplate`;

CREATE TABLE `bulkpaymenttemplate` (
  `templateId` varchar(50) NOT NULL,
  `templateName` varchar(50) NOT NULL,
  `processingMode` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `paymentId` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `paymentDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scheduledDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fileId` varchar(50) NULL,
  `reviewedBy` varchar(50) DEFAULT NULL,
  `initiatedBy` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `fromAccount` bigint(20) DEFAULT NULL,
  `totalAmount` bigint(20) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `requestId` varchar(50) DEFAULT NULL,
  `paymentStatus` varchar(50) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL, 
  `bulkType` varchar(50) DEFAULT NULL,
  `updateReference` varchar(50) DEFAULT NULL,
  `creditReference` varchar(50) DEFAULT NULL,
  `debitReference` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  
  PRIMARY KEY (`templateId`),
  KEY `FK_bulkpaymenttemplate_requestId` (`requestId`),
  KEY `FK_bulkpaymenttemplate_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymenttemplate_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymenttemplate_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymenttemplate_companyId` (`companyId`),
  KEY `FK_bulkpaymenttemplate_reviewedBy` (`reviewedBy`),
  KEY `FK_bulkpaymenttemplate_initiatedBy` (`initiatedBy`),
  KEY `FK_bulkpaymenttemplate_status` (`status`),
  KEY `FK_bulkpaymenttemplate_roleId` (`roleId`),
  KEY `FK_bulkpaymenttemplate_fileId` (`fileId`),
  CONSTRAINT `FK_bulkpaymenttemplate_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymenttemplate_reviewedBy_idx` FOREIGN KEY (`reviewedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymenttemplate_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymenttemplate_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP procedure IF EXISTS `customer_actions_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_actions_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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


CREATE TABLE `bulkpaymenttemplatepos` (
  `paymentOrderId` varchar(50) NOT NULL,
  `templateId` varchar(50) DEFAULT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `recipientName` varchar(50) NOT NULL,
  `accountNumber` varchar(50) NOT NULL,
  `bankName` varchar(50) DEFAULT NULL,
  `swift` varchar(50) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `feesPaidBy` varchar(50) DEFAULT NULL,
  `paymentReference` varchar(50) DEFAULT NULL,
  `debitAccountIBAN` varchar(50) DEFAULT NULL,
  `beneficiaryIBAN` varchar(50) DEFAULT NULL,
  `beneficiaryName` varchar(50) DEFAULT NULL,
  `beneficiaryNickName` varchar(50) DEFAULT NULL,
  `beneficiaryAddress` varchar(125) DEFAULT NULL,
  `accountWithBankBIC` varchar(50) DEFAULT NULL,
  `customer` varchar(50) DEFAULT NULL,
  `paymentMethod` varchar(50) DEFAULT NULL,
  `accType` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',

  PRIMARY KEY (`paymentOrderId`),
  KEY `FK_bulkpaymenttemplatepos_accountNumber` (`accountNumber`),
  KEY `FK_bulkpaymenttemplatepos_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymenttemplatepos_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymenttemplatepos_companyId` (`companyId`),
  KEY `FK_bulkpaymenttemplatepos_createdby` (`createdby`),
  KEY `FK_bulkpaymenttemplatepos_recipientName` (`recipientName`),
  KEY `FK_bulkpaymenttemplatepos_status` (`status`),
  KEY `FK_bulkpaymenttemplatepos_roleId` (`roleId`),
  KEY `FK_bulkpaymenttemplatepos_templateId` (`templateId`),
  CONSTRAINT `FK_bulkpaymenttemplatepos_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymenttemplatepos_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymenttemplatepos_templateId_idx` FOREIGN KEY (`templateId`) REFERENCES `bulkpaymenttemplate` (`templateId`)ON DELETE  CASCADE,
  CONSTRAINT `FK_bulkpaymenttemplatepos_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymenttemplatepos_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentrequestpos`;
DROP TABLE IF EXISTS `bulkpaymentrequest`;

CREATE TABLE `bulkpaymentrequest` (
  `paymentrequestId` varchar(50) NOT NULL,
  `templateId` varchar(50) NOT NULL,
  `templateName` varchar(50) NOT NULL,
  `processingMode` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `paymentId` varchar(50) DEFAULT NULL,
  `description` varchar(100) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `paymentDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scheduledDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fileId` varchar(50) NULL,
  `reviewedBy` varchar(50) DEFAULT NULL,
  `initiatedBy` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `fromAccount` bigint(20) DEFAULT NULL,
  `totalAmount` bigint(20) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `paymentStatus` varchar(50) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL, 
  `bulkType` varchar(50) DEFAULT NULL,
  `updateReference` varchar(50) DEFAULT NULL,
  `creditReference` varchar(50) DEFAULT NULL,
  `debitReference` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  
  PRIMARY KEY (`paymentrequestId`),
  KEY `FK_bulkpaymentrequest_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymentrequest_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentrequest_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentrequest_companyId` (`companyId`),
  KEY `FK_bulkpaymentrequest_reviewedBy` (`reviewedBy`),
  KEY `FK_bulkpaymentrequest_initiatedBy` (`initiatedBy`),
  KEY `FK_bulkpaymentrequest_status` (`status`),
  KEY `FK_bulkpaymentrequest_roleId` (`roleId`),
  KEY `FK_bulkpaymentrequest_templateId` (`templateId`),
  CONSTRAINT `FK_bulkpaymentrequest_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrequest_reviewedBy_idx` FOREIGN KEY (`reviewedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentrequest_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentreuest_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `bulkpaymentrequestpos` (
  `paymentrequestPOId` varchar(50) NOT NULL,	
  `paymentrequestId` varchar(50) NOT NULL,
  `paymentOrderId` varchar(50) NOT NULL,
  `templateId` varchar(50) DEFAULT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `recipientName` varchar(50) NOT NULL,
  `accountNumber` varchar(50) NOT NULL,
  `bankName` varchar(50) DEFAULT NULL,
  `swift` varchar(50) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `amount` bigint(20) DEFAULT NULL,
  `feesPaidBy` varchar(50) DEFAULT NULL,
  `paymentReference` varchar(50) DEFAULT NULL,
  `debitAccountIBAN` varchar(50) DEFAULT NULL,
  `beneficiaryIBAN` varchar(50) DEFAULT NULL,
  `beneficiaryName` varchar(50) DEFAULT NULL,
  `beneficiaryNickName` varchar(50) DEFAULT NULL,
  `beneficiaryAddress` varchar(125) DEFAULT NULL,
  `accountWithBankBIC` varchar(50) DEFAULT NULL,
  `customer` varchar(50) DEFAULT NULL,
  `paymentMethod` varchar(50) DEFAULT NULL,
  `accType` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',

  PRIMARY KEY (`paymentrequestPOId`),
  KEY `FK_bulkpaymentrequestpos_accountNumber` (`accountNumber`),
  KEY `FK_bulkpaymentrequestpos_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentrequestpos_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentrequestpos_companyId` (`companyId`),
  KEY `FK_bulkpaymentrequestpos_createdby` (`createdby`),
  KEY `FK_bulkpaymentrequestpos_recipientName` (`recipientName`),
  KEY `FK_bulkpaymentrequestpos_status` (`status`),
  KEY `FK_bulkpaymentrequestpos_roleId` (`roleId`),
  KEY `FK_bulkpaymentrequestpos_paymentrequestId` (`paymentrequestId`),
  CONSTRAINT `FK_bulkpaymentrequestpos_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrequestpos_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentrequestpos_paymentrequestId` FOREIGN KEY (`paymentrequestId`) REFERENCES `bulkpaymentrequest` (`paymentrequestId`) ON DELETE CASCADE,
  CONSTRAINT `FK_bulkpaymentrequestpos_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrequestpos_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `featureaction` ADD COLUMN `status` varchar(100) DEFAULt 'SID_ACTION_ACTIVE';
ALTER TABLE `featureaction` ADD CONSTRAINT `FK_featureaction_status` FOREIGN KEY (`status`) REFERENCES `status` (`id`)  ON UPDATE NO ACTION ON DELETE NO ACTION;

DROP TABLE IF EXISTS `servicedefinition`;
CREATE TABLE `servicedefinition` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(150) DEFAULT NULL,
  `serviceType` varchar(50) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_servicedefinition_serviceType` FOREIGN KEY (`serviceType`) REFERENCES `membergrouptype` (`id`)  ON UPDATE NO ACTION ON DELETE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `accesspolicy`;
CREATE TABLE `accesspolicy` (
   `id` varchar(50) NOT NULL,
   `name` varchar(50) NOT NULL,
   `description` varchar(100) NOT NULL,
   `createdby` varchar(45) DEFAULT NULL,
   `modifiedby` varchar(45) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `actionlevel`;
 CREATE TABLE `actionlevel` (
   `id` varchar(50) NOT NULL,
   `name` varchar(50) NOT NULL,
   `description` varchar(100) NOT NULL,
   `createdby` varchar(45) DEFAULT NULL,
   `modifiedby` varchar(45) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 
 DROP TABLE IF EXISTS `limitgroup`;
 CREATE TABLE `limitgroup` (
   `id` varchar(50) NOT NULL,
   `name` varchar(50) NOT NULL,
   `description` varchar(100) NOT NULL,
   `createdby` varchar(45) DEFAULT NULL,
   `modifiedby` varchar(45) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`id`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
 DROP TABLE IF EXISTS `manageapprovalmatrix`;
 CREATE TABLE `manageapprovalmatrix` (
   `contractId` varchar(50) NOT NULL,
   `coreCustomerId` varchar(50) NOT NULL,
   `isDisabled` tinyint(1) NOT NULL DEFAULT 1,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `updatedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`contractId`,`coreCustomerId`)
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `membergroup` ADD `isApplicabletoAllServices` tinyint(1) NOT NULL DEFAULT '0';

ALTER TABLE `featureaction` ADD COLUMN limitgroupId varchar(50) default NULL;
ALTER TABLE `featureaction` ADD COLUMN accesspolicyId varchar(50) default NULL;
ALTER TABLE `featureaction` ADD COLUMN actionlevelId varchar(50) default NULL;
ALTER TABLE `featureaction` ADD CONSTRAINT `FK_featureaction_accesspolicyId` FOREIGN KEY (`accesspolicyId`) REFERENCES `accesspolicy` (`id`)  ON UPDATE NO ACTION ON DELETE NO ACTION;  
ALTER TABLE `featureaction` ADD CONSTRAINT `FK_featureaction_actionlevelId` FOREIGN KEY (`actionlevelId`) REFERENCES `actionlevel` (`id`)  ON UPDATE NO ACTION ON DELETE NO ACTION;  
ALTER TABLE `featureaction` ADD CONSTRAINT `FK_featureaction_limitgroupId` FOREIGN KEY (`limitgroupId`) REFERENCES `limitgroup` (`id`)  ON UPDATE NO ACTION ON DELETE NO ACTION;  

ALTER TABLE `groupactionlimit` DROP FOREIGN KEY `FK_groupactionlimit_Group`;
ALTER TABLE `groupactionlimit` ADD CONSTRAINT `FK_groupactionlimit_Group` FOREIGN KEY (`Group_id`) REFERENCES `membergroup` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `actiondisplaynamedescription`
CHANGE COLUMN `displayName` `displayName` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `displayDescription` `displayDescription` VARCHAR(300) NOT NULL ;

DROP TABLE IF EXISTS `groupservicedefinition`;
CREATE TABLE `groupservicedefinition` (
  `Group_id` varchar(50) NOT NULL,
  `serviceDefinitionId` varchar(50) NOT NULL,
  `isDefaultGroup` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Group_id`,`serviceDefinitionId`),
  KEY `IXFK_groupservicedefinition_Group` (`Group_id`),
  KEY `IXFK_groupservicedefinition_businesstype` (`serviceDefinitionId`),
  CONSTRAINT `FK_groupservicedefinition_Businesstype_id` FOREIGN KEY (`serviceDefinitionId`) REFERENCES `servicedefinition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_groupservicedefinition_Group` FOREIGN KEY (`Group_id`) REFERENCES `membergroup` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `servicedefinitionactionlimit`;
CREATE TABLE `servicedefinitionactionlimit` (
   `id` varchar(50) NOT NULL,
   `serviceDefinitionId` varchar(50) NOT NULL,
   `actionId` varchar(255) NOT NULL,
   `limitTypeId` varchar(50) DEFAULT NULL,
   `value` decimal(20,2) DEFAULT NULL,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`),
   UNIQUE KEY `UNIQUE_servicedefinitionactionlimit` (`serviceDefinitionId`,`actionId`,`limitTypeId`),
   KEY `IXFK_servicedefinitionactionlimit_serviceDefinitionId` (`serviceDefinitionId`),
   KEY `IXFK_servicedefinitionactionlimit_action` (`actionId`),
   KEY `FK_servicedefinitionactionlimit_limitType_idx` (`limitTypeId`),
   CONSTRAINT `FK_servicedefinitionactionlimit_action` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_servicedefinitionactionlimit_limitType` FOREIGN KEY (`limitTypeId`) REFERENCES `limittype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_servicedefinitionactionlimit_serviceDefinition` FOREIGN KEY (`serviceDefinitionId`) REFERENCES `servicedefinition` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;
 
DROP procedure IF EXISTS `fetch_approvers_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvers_proc`(
in _requestId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SELECT customerapprovalmatrix.customerId as customerId 
    FROM customerapprovalmatrix  WHERE 
    customerapprovalmatrix.approvalMatrixId 
    IN (
		SELECT requestapprovalmatrix.approvalMatrixId 
        FROM requestapprovalmatrix WHERE 
        requestapprovalmatrix.requestId = _requestId
       );
END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_default_delete_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_default_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
 IF _filterColumnName = "actionId" THEN
	 DELETE FROM approvalmatrix where contractId = _contractId and coreCustomerId = _cif and FIND_IN_SET(actionId,_filterColumnIds) COLLATE utf8_general_ci;
 ELSEIF _filterColumnName = "accountId" THEN
 	 DELETE FROM approvalmatrix where contractId = _contractId and coreCustomerId = _cif and FIND_IN_SET(accountId,_filterColumnIds) COLLATE utf8_general_ci;
 ELSEIF _filterColumnName = "cif" THEN
 	 DELETE FROM approvalmatrix where contractId = _contractId and FIND_IN_SET(coreCustomerId,_filterColumnIds) COLLATE utf8_general_ci;
 END IF;
 	
END$$

DELIMITER ;

DROP procedure IF EXISTS `manageapprovalmatrix_update_proc`;
DELIMITER $$
CREATE PROCEDURE `manageapprovalmatrix_update_proc`(
	IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _cifList longtext CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _isDisabledflag VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE mod_contractId VARCHAR(60);
    set mod_contractId=concat("\"",_contractId,"\"");
	set @numOfRecords = LENGTH(_cifList) - LENGTH(REPLACE(_cifList, ',', '')) + 1;
	insertRecords : LOOP
		set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE insertRecords;
		else
			set @recordsData = concat("\"",SUBSTRING_INDEX(SUBSTRING_INDEX(_cifList, ',', index1), ',', -1 ),"\"");
			set @cquery= concat('select count(*) from manageapprovalmatrix where contractId=',mod_contractId,' and coreCustomerId=',@recordsData,'INTO @count');
            PREPARE count_query FROM @cquery;
			EXECUTE count_query;
			if @count = 0 THEN
				set @query = concat('INSERT INTO manageapprovalmatrix(contractId,coreCustomerId,isDisabled) VALUES (',mod_contractId,',',@recordsData,',',_isDisabledflag,');');
				PREPARE sql_query FROM @query;
				EXECUTE sql_query;				
			else
				set @query2=concat('UPDATE manageapprovalmatrix set isDisabled=',_isDisabledflag,' where contractId=',mod_contractId,' and coreCustomerId=',@recordsData);
                PREPARE update_query FROM @query2;
				EXECUTE update_query;
			END IF;
	    END IF;
	END LOOP insertRecords;
END$$

DELIMITER ;

ALTER TABLE `bulkpaymenttemplatepos` ADD `beneficiaryType` varchar(50);

ALTER TABLE `bulkpaymenttemplatepos` ADD `addToExistingFlag` VARCHAR(5);

DROP procedure IF EXISTS `bulkpayment_template_po_create_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkpayment_template_po_create_proc`(
IN _povalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPOs = LENGTH(_povalues) - LENGTH(REPLACE(_povalues, '|', '')) + 1;
		insertPOs : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfPOs + 1 THEN
            LEAVE insertPOs;
        ELSE
        SET @posData = SUBSTRING_INDEX(SUBSTRING_INDEX(_povalues, '|', index1), '|', -1 );
            SET @query = CONCAT('INSERT INTO bulkpaymenttemplatepos(paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,featureActionId,companyId,roleId,status,createdby,beneficiaryName,paymentMethod,currency,amount,feesPaidBy,paymentReference,swift,beneficiaryNickName,beneficiaryAddress,accType,beneficiaryType,addToExistingFlag,beneficiaryIBAN,bankName)
            VALUES (',@posData,');');
            PREPARE sql_query FROM @query;
            EXECUTE sql_query;
         END IF;
    END LOOP insertPOs;
END$$
DELIMITER ;

ALTER TABLE `bulkpaymentfiles` ADD `batchMode` VARCHAR(15);
ALTER TABLE `bulkpaymentfilesmock` ADD `batchMode` VARCHAR(15);
ALTER TABLE `bulkpaymentrecord` ADD `batchMode` VARCHAR(15);
ALTER TABLE `bulkpaymentrecordmock` ADD `batchMode` VARCHAR(15);

DROP VIEW IF EXISTS `group_features_actions_view`;
CREATE VIEW `group_features_actions_view` AS
 select `groupactionlimit`.`Group_id` AS `Group_id`,
 `groupactionlimit`.`Action_id` AS `Action_id`,
 `groupactionlimit`.`LimitType_id` AS `LimitType_id`,
 `groupactionlimit`.`value` AS `value`,
 `groupactionlimit`.`id` AS `groupactionlimit_id`,
 `groupactionlimit`.`softdeleteflag` AS `softdelete`,
 `membergroup`.`Type_id` AS `Type_id`,
 `membergroup`.`Name` AS `Group_name`,
 `membergroup`.`Description` AS `Group_description`,
 `featureaction`.`name` AS `Action_name`,
 `featureaction`.`description` AS `Action_description`,
 `featureaction`.`Type_id` AS `Action_Type_id`,
 `featureaction`.`Feature_id` AS `Feature_id`,
 `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
 `featureaction`.`isAccountLevel` AS `isAccountLevel`,
 `featureaction`.`isPrimary` AS `isPrimary`,
 `featureaction`.`DisplaySequence` AS `Action_displaysequence`,
 `featureaction`.`dependency` AS `Action_dependency`,
 `featureaction`.`status` AS `actionStatus`,
 `accesspolicy`.`name` AS `accessPolicy`,
 `featureaction`.`accesspolicyId` AS `accessPolicyId`,
 `featureaction`.`limitgroupId` AS `limitGroupId`,
 `limitgroup`.`name` AS `limitGroup`,
 `actionlevel`.`name` AS `actionlevel`,
 `featureaction`.`actionlevelId` AS `actionlevelId`,
 `feature`.`name` AS `Feature_name`,
 `feature`.`description` AS `Feature_description`,
 `feature`.`Type_id` AS `Feature_Type_id`,
 `feature`.`Status_id` AS `Feature_Status_id`,
 `feature`.`DisplaySequence` AS `Feature_displaysequence`,
 `feature`.`isPrimary` AS `Feature_isPrimary`
 from ((((((`groupactionlimit` left join `membergroup` on((`membergroup`.`id` = `groupactionlimit`.`Group_id`))) left join `featureaction` on((`featureaction`.`id` = `groupactionlimit`.`Action_id`))) left join `feature` on((`feature`.`id` = `featureaction`.`Feature_id`))) left join `accesspolicy` on ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`))) left join `limitgroup` on ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)) left join `actionlevel` on ((`featureaction`.`actionlevelId` = `actionlevel`.`id`))));
 
DROP procedure IF EXISTS `get_filtered_locations_proc`;
DELIMITER $$
CREATE  PROCEDURE `get_filtered_locations_proc`(
in _searchText varchar(60) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select * from location where DisplayName like Concat('%', _searchText,'%') order by DisplayName limit 20;
END$$
DELIMITER ;
 
 DROP TABLE IF EXISTS `limitgroupdisplaynamedescription`;
 CREATE TABLE `limitgroupdisplaynamedescription` (
   `limitGroupId` varchar(255) NOT NULL,
   `localeId` varchar(50) NOT NULL,
   `displayName` varchar(100) NOT NULL,
   `displayDescription` varchar(300) NOT NULL,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`limitGroupId`,`localeId`),
   KEY `limitgroupdisplaynamedescription_localeId` (`localeId`),
   CONSTRAINT `FK_limitgroupdisplaynamedescription_limitGroupId` FOREIGN KEY (`limitGroupId`) REFERENCES `limitgroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_limitgroupdisplaynamedescription_localeId` FOREIGN KEY (`localeId`) REFERENCES `locale` (`Code`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;



DROP TABLE IF EXISTS `dependentactions`;
CREATE TABLE `dependentactions` (
   `actionId` varchar(255) NOT NULL,
   `dependentactionId` varchar(255) NOT NULL,
   `featureId` varchar(255) NOT NULL,
   `actionName` varchar(255) NOT NULL,
   `featureName` varchar(255) NOT NULL,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   PRIMARY KEY (`actionId`,`dependentactionId`),
   KEY `FK_dependentactions_dependentactionId` (`dependentactionId`),
   KEY `FK_dependentactions_feature` (`featureId`),
   CONSTRAINT `FK_dependentactions_actionId` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_dependentactions_dependentactionId` FOREIGN KEY (`dependentactionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_dependentactions_feature` FOREIGN KEY (`featureId`) REFERENCES `feature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP VIEW IF EXISTS `servicedefinition_features_actions_view`;
CREATE VIEW `servicedefinition_features_actions_view` AS
  SELECT 
        `servicedefinitionactionlimit`.`serviceDefinitionId` AS `serviceDefinitionId`,
        `servicedefinitionactionlimit`.`actionId` AS `actionId`,
        `servicedefinitionactionlimit`.`limitTypeId` AS `limitTypeId`,
        `servicedefinitionactionlimit`.`value` AS `value`,
        `servicedefinitionactionlimit`.`id` AS `serviceDefinitionActionLimitId`,
        `servicedefinitionactionlimit`.`softdeleteflag` AS `softdelete`,
        `servicedefinition`.`serviceType` AS `serviceType`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `servicedefinition`.`description` AS `serviceDefinitionDescription`,
        `featureaction`.`name` AS `actionName`,
        `featureaction`.`description` AS `actionDescription`,
        `featureaction`.`Type_id` AS `actionTypeId`,
        `featureaction`.`Feature_id` AS `featureId`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`DisplaySequence` AS `actionDisplaysequence`,
        `featureaction`.`dependency` AS `actionDependency`,
        `featureaction`.`status` AS `actionStatus`,
        `accesspolicy`.`name` AS `accessPolicy`,
        `featureaction`.`accesspolicyId` AS `accessPolicyId`,
        `featureaction`.`limitgroupId` AS `limitGroupId`,
        `limitgroup`.`name` AS `limitGroup`,
        `actionlevel`.`name` AS `actionlevel`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
		`dependentactions`.`dependentactionId` AS `dependentactionId`,
        `dependentactions`.`featureId` AS `dependentFeatureId`,
        `dependentactions`.`actionName` AS `dependentActionName`,
        `dependentactions`.`featureName` AS `dependentFeatureName`,
        `feature`.`name` AS `featureName`,
        `feature`.`description` AS `featureDescription`,
        `feature`.`Type_id` AS `featureTypeId`,
        `feature`.`Status_id` AS `featureStatusId`,
        `feature`.`DisplaySequence` AS `featureDisplaysequence`,
        `feature`.`isPrimary` AS `featureIsPrimary`
    FROM
        (((((((`servicedefinitionactionlimit`
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `servicedefinitionactionlimit`.`serviceDefinitionId`)))
        LEFT JOIN `featureaction` ON ((`featureaction`.`id` = `servicedefinitionactionlimit`.`actionId`)))
		LEFT JOIN `dependentactions` ON ((`featureaction`.`id` = `dependentactions`.`actionId`)))
        LEFT JOIN `feature` ON ((`feature`.`id` = `featureaction`.`Feature_id`)))
        LEFT JOIN `accesspolicy` ON ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`)))
        LEFT JOIN `limitgroup` ON ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)))
        LEFT JOIN `actionlevel` ON ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)));


DROP VIEW IF EXISTS `servicedefinition_view`;
CREATE VIEW `servicedefinition_view` AS
  SELECT 
    `servicedefinition`.`id` AS `id`,
    `servicedefinition`.`name` AS `name`,
    `servicedefinition`.`description` AS `description`,
    `servicedefinition`.`serviceType` AS `serviceType`,
    `servicedefinition`.`status` AS `status`,
    (SELECT COUNT(`groupservicedefinition`.`Group_id`) FROM `groupservicedefinition`
        WHERE (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)) AS `numberOfRoles`,
	(SELECT COUNT(`membergroup`.`id`) FROM `membergroup` WHERE Status_id LIKE "SID_ACTIVE" AND id in (SELECT `groupservicedefinition`.`Group_id` FROM 
		`groupservicedefinition` WHERE (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`))) AS `numberOfActiveRoles`,
    (SELECT `groupservicedefinition`.`Group_id` FROM `groupservicedefinition`
        WHERE ((`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`) AND (`groupservicedefinition`.`isDefaultGroup` = 1))) AS `defaultRole`,
    (SELECT COUNT(DISTINCT `servicedefinition_features_actions_view`.`featureId`) FROM `servicedefinition_features_actions_view`
        WHERE ((`servicedefinition`.`id` = `servicedefinition_features_actions_view`.`serviceDefinitionId`) AND (`servicedefinition_features_actions_view`.`softdelete` = '0'))) AS `numberOfFeatures`,
    (SELECT COUNT(`contract`.`id`) FROM `contract` 
      WHERE (`contract`.`servicedefinitionId` = `servicedefinition`.`id`)) AS `numberOfContracts`
  FROM `servicedefinition`;


DROP VIEW IF EXISTS `limitgroups_view`;
CREATE VIEW `limitgroups_view` AS
    SELECT 
        `limitgroup`.`id` AS `id`,
        `limitgroup`.`name` AS `name`,
        `limitgroup`.`description` AS `description`,
        `limitgroupdisplaynamedescription`.`localeId` AS `localeId`,
        `limitgroupdisplaynamedescription`.`displayName` AS `displayName`,
        `limitgroupdisplaynamedescription`.`displayDescription` AS `displayDescription`
    FROM
        (`limitgroup`
        LEFT JOIN `limitgroupdisplaynamedescription` ON ((`limitgroupdisplaynamedescription`.`limitGroupId` = `limitgroup`.`id`)));

DROP VIEW IF EXISTS `dependentactions_view`;
CREATE VIEW `dependentactions_view` AS
      SELECT 
        `dependentactions`.`actionId` AS `actionId`,
        `dependentactions`.`dependentactionId` AS `dependentactionId`,
        `featureaction`.`name` AS `actionName`,
        `dependentactions`.`featureId` AS `featureId`,
        `feature`.`name` AS `featureName`
    FROM
        ((`dependentactions`
        LEFT JOIN `featureaction` ON ((`dependentactions`.`dependentactionId` = `featureaction`.`id`)))
        LEFT JOIN `feature` ON ((`dependentactions`.`featureId` = `feature`.`id`)));
		
DROP VIEW IF EXISTS `get_feature_actions_view`;
CREATE VIEW `get_feature_actions_view` AS
     SELECT 
        `featureaction`.`id` AS `actionId`,
        `featureaction`.`Feature_id` AS `featureId`,
        `featureaction`.`name` AS `actionName`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`description` AS `actionDescription`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`notes` AS `notes`,
        `featureaction`.`Type_id` AS `typeId`,
        `featureaction`.`DisplaySequence` AS `actionDisplaySequence`,
        `featureaction`.`dependency` AS `actionDependency`,
        `featureaction`.`status` AS `actionStatus`,
        `featureactionroletype`.`RoleType_id` AS `actionType`,
        `featureaction`.`accesspolicyId` AS `accessPolicyId`,
        `accesspolicy`.`name` AS `accessPolicy`,
        `featureaction`.`limitgroupId` AS `limitGroupId`,
        `limitgroup`.`name` AS `limitGroup`,
        `feature`.`Status_id` AS `featureStatus`,
        `feature`.`name` AS `featureName`,
        `feature`.`description` AS `featureDescription`,
        `feature`.`Type_id` AS `featureType`,
        `featureroletype`.`RoleType_id` AS `featureGroup`,
        `feature`.`DisplaySequence` AS `featureDisplaySequence`,
        `feature`.`isPrimary` AS `isFeaturePrimary`,
        `actionlevel`.`name` AS `actionlevel`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
        `actiondisplaynamedescription`.`Locale_id` AS `localeId`,
        `actiondisplaynamedescription`.`displayName` AS `displayName`,
        `actiondisplaynamedescription`.`displayDescription` AS `displayDescription`,
        `actionlimit`.`LimitType_id` AS `limitTypeId`,
        `actionlimit`.`value` AS `value`,
        `dependentactions_view`.`dependentactionId` AS `dependentactionId`,
        `dependentactions_view`.`featureId` AS `dependentFeatureId`,
        `dependentactions_view`.`actionName` AS `dependentActionName`,
        `dependentactions_view`.`featureName` AS `dependentFeatureName`,
        `termandcondition`.`Code` AS `termsAndConditionCode`,
        `termandcondition`.`Title` AS `termsAndConditionTitle`,
        `termandcondition`.`Description` AS `termsAndConditionDescription`,
        `membergrouptype`.`description` AS `roleTypeName`
    FROM
        (((((((((((`featureaction`
        LEFT JOIN `feature` ON ((`feature`.`id` = `featureaction`.`Feature_id`)))
        LEFT JOIN `actiondisplaynamedescription` ON ((`actiondisplaynamedescription`.`Action_id` = `featureaction`.`id`)))
        LEFT JOIN `accesspolicy` ON ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`)))
        LEFT JOIN `featureroletype` ON ((`featureroletype`.`Feature_id` = `feature`.`id`)))
        LEFT JOIN `featureactionroletype` ON ((`featureaction`.`id` = `featureactionroletype`.`Action_id`)))
        LEFT JOIN `termandcondition` ON ((`featureaction`.`TermsAndConditions_id` = `termandcondition`.`id`)))
        LEFT JOIN `limitgroup` ON ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)))
        LEFT JOIN `actionlevel` ON ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)))
        LEFT JOIN `dependentactions_view` ON ((`featureaction`.`id` = `dependentactions_view`.`actionId`)))
        LEFT JOIN `membergrouptype` ON ((`featureactionroletype`.`RoleType_id` = `membergrouptype`.`id`)))
        LEFT JOIN `actionlimit` ON ((`actionlimit`.`Action_id` = `featureaction`.`id`)));
		
DROP VIEW IF EXISTS `get_all_features_view`;
CREATE VIEW `get_all_features_view` AS
    SELECT 
        `feature`.`id` AS `id`,
        `feature`.`name` AS `name`,
        `feature`.`description` AS `description`,
        `feature`.`Type_id` AS `Type_id`,
        `feature`.`Service_Fee` AS `Service_Fee`,
        `feature`.`Status_id` AS `Status_id`,
        `featureroletype`.`RoleType_id` AS `roleTypeId`,
        `featuredisplaynamedescription`.`Locale_id` AS `languageId`,
        `featuredisplaynamedescription`.`displayName` AS `displayName`,
        `featuredisplaynamedescription`.`displayDescription` AS `displayDescription`,
        `membergrouptype`.`description` AS `roleTypeName`,
        (SELECT 
                COUNT(DISTINCT `featureaction`.`id`)
            FROM
                `featureaction`
            WHERE
                ((`feature`.`id` = `featureaction`.`Feature_id`)
                    AND (`featureaction`.`Type_id` = 'MONETARY'))) AS `monetaryActions`,
        (SELECT 
                COUNT(DISTINCT `featureaction`.`id`)
            FROM
                `featureaction`
            WHERE
                ((`feature`.`id` = `featureaction`.`Feature_id`)
                    AND (`featureaction`.`Type_id` = 'NON_MONETARY'))) AS `nonMonetaryActions`
    FROM
        (((`feature`
        LEFT JOIN `featureroletype` ON ((`featureroletype`.`Feature_id` = `feature`.`id`)))
        LEFT JOIN `featuredisplaynamedescription` ON ((`featuredisplaynamedescription`.`Feature_id` = `feature`.`id`)))
        LEFT JOIN `membergrouptype` ON ((`featureroletype`.`RoleType_id` = `membergrouptype`.`id`)));
		
DROP PROCEDURE IF EXISTS `servicedefinition_defaultgroup_update_proc`;
DELIMITER $$
CREATE  PROCEDURE `servicedefinition_defaultgroup_update_proc`(
 IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _isDefault varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
 ) BEGIN
 IF(_isDefault = '0') THEN
     UPDATE groupservicedefinition SET isDefaultGroup = false where serviceDefinitionId = _serviceDefinitionId AND Group_id = _groupId ;
 ELSE
     UPDATE groupservicedefinition SET isDefaultGroup = false where serviceDefinitionId = _serviceDefinitionId;
     UPDATE groupservicedefinition SET isDefaultGroup = true where serviceDefinitionId = _serviceDefinitionId AND Group_id = _groupId;     
 END IF;
 END$$
DELIMITER ;
 
DROP VIEW IF EXISTS `user_customers_view`;

CREATE VIEW `user_customers_view` AS
    SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`contractId` AS `contractId`,
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
        (((`contractcustomers`
        LEFT JOIN `contractcorecustomers` ON (((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
            AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `contract` ON ((`contract`.`id` = `contractcorecustomers`.`contractId`)))
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `contract`.`servicedefinitionId`))
		LEFT JOIN `membergrouptype` ON ((`membergrouptype`.`id` = `servicedefinition`.`serviceType`))
		LEFT JOIN `customergroup` ON ((`customergroup`.`Customer_id` = `contractcustomers`.`customerId` AND `customergroup`.`contractId` = `contractcustomers`.`contractId` 
        AND `customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
        LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`));
        
ALTER TABLE `bulkpaymentfilesmock` CHANGE COLUMN `content` `content` LONGTEXT NOT NULL ;
ALTER TABLE `bulkpaymentfiles` CHANGE COLUMN `content` `content` LONGTEXT NOT NULL ;
ALTER TABLE `bulkpaymentfilesmock` MODIFY COLUMN `sysGeneratedFileName` VARCHAR(150);

ALTER TABLE `bulkpaymenttemplate` ADD `totalBeneficiaries` VARCHAR(20);
ALTER TABLE `bulkpaymentrequest` ADD `totalBeneficiaries` VARCHAR(20);

DROP VIEW IF EXISTS `actiondependency_view`;
CREATE VIEW `actiondependency_view` AS
    SELECT 
        `dependentactions`.`dependentactionId` AS `actionName`,
        `dependentactions`.`actionId` AS `dependencyAction`,
        `dependentactions`.`featureId` AS `featureId`,
        `featureaction`.`status` AS `actionStatus`,
        `feature`.`Status_id` AS `featureStatus`
    FROM
        ((`dependentactions`
        LEFT JOIN `featureaction` ON ((`dependentactions`.`dependentactionId` = `featureaction`.`id`)))
        LEFT JOIN `feature` ON ((`dependentactions`.`featureId` = `feature`.`id`)));
		
CREATE TABLE `cancellationreason` (
  `id` varchar(50) NOT NULL,
  `reason` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `bulkpaymentrecord` ADD COLUMN comments varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecord` ADD COLUMN cancellationreason varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN comments varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN cancellationreason varchar(50) default NULL;

ALTER TABLE `bulkpaymentrecord` ADD COLUMN rejectioncomments varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecord` ADD COLUMN rejectionreason varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN rejectioncomments varchar(50) default NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN rejectionreason varchar(50) default NULL;

ALTER TABLE `bulkpaymentsubrecord` ADD COLUMN paymentStatus varchar(50) default NULL;
ALTER TABLE `bulkpaymentsubrecordmock` ADD COLUMN paymentStatus varchar(50) default NULL;


DROP VIEW if exists groups_view;
CREATE VIEW `groups_view` AS 
select `membergroup`.`id` AS `Group_id`,
`membergroup`.`Type_id` AS `Type_id`,
`customertype`.`Name` AS `Type_Name`,
`membergroup`.`Description` AS `Group_Desc`,
`membergroup`.`Status_id` AS `Status_id`,
`membergroup`.`Name` AS `Group_Name`,
`membergroup`.`isEAgreementActive` AS `isEAgreementActive`,
`membergroup`.`isApplicabletoAllServices` As `isApplicabletoAllServices`,
(select count(`groupentitlement`.`Group_id`) from `groupentitlement` where
 (`groupentitlement`.`Group_id` = `membergroup`.`id`)) AS `Entitlements_Count`,
 (select count(distinct(`customergroup`.`Customer_id`)) from `customergroup` 
 where (`customergroup`.`Group_id` = `membergroup`.`id`)) AS `Customers_Count`,
 (case `membergroup`.`Status_id` when 'SID_ACTIVE' then 'Active' else 'Inactive' end) AS `Status` 
 from (`membergroup` join `customertype` on((`membergroup`.`Type_id` = `customertype`.`id`)));
  

ALTER TABLE actiondisplaynamedescription
DROP FOREIGN KEY FK_actiondisplaynamedescription_Action_id;
ALTER TABLE actiondisplaynamedescription
ADD CONSTRAINT FK_actiondisplaynamedescription_Action_id
FOREIGN KEY (Action_id)
REFERENCES featureaction (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE customeraction
DROP FOREIGN KEY FK_CustomerActionLimit_Action;
ALTER TABLE customeraction
ADD CONSTRAINT FK_CustomerActionLimit_Action
FOREIGN KEY (Action_id)
REFERENCES featureaction (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE groupactionlimit
DROP FOREIGN KEY FK_groupactionlimit_Action;
ALTER TABLE groupactionlimit
ADD CONSTRAINT FK_groupactionlimit_Action
FOREIGN KEY (Action_id)
REFERENCES featureaction (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE featureactionroletype
DROP FOREIGN KEY FK_featureactionroletype_Action_id;
ALTER TABLE featureactionroletype
ADD CONSTRAINT FK_featureactionroletype_Action_id
FOREIGN KEY (Action_id)
REFERENCES featureaction (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE organisationactionlimit
DROP FOREIGN KEY FK_organisationactionlimit_action;
ALTER TABLE organisationactionlimit
ADD CONSTRAINT FK_organisationactionlimit_action
FOREIGN KEY (Action_id)
REFERENCES featureaction (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

DROP procedure IF EXISTS `bulkpayment_request_po_create_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkpayment_request_po_create_proc`(
IN _povalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPOs = LENGTH(_povalues) - LENGTH(REPLACE(_povalues, '|', '')) + 1;
		insertPOs : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfPOs + 1 THEN
            LEAVE insertPOs;
        ELSE
        SET @posData = SUBSTRING_INDEX(SUBSTRING_INDEX(_povalues, '|', index1), '|', -1 );
            SET @query = CONCAT('INSERT INTO bulkpaymentrequestpos(paymentrequestPOId,paymentrequestId,paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,bankName,swift,featureActionId,companyId,roleId,status,currency,amount,feesPaidBy,paymentReference,debitAccountIBAN,beneficiaryIBAN,beneficiaryName,beneficiaryNickName,beneficiaryAddress,accountWithBankBIC,customer,paymentMethod,accType,createdby)
            VALUES (',@posData,');');
            PREPARE sql_query FROM @query;
            EXECUTE sql_query;
         END IF;
    END LOOP insertPOs;
END$$
DELIMITER ;

DROP VIEW IF EXISTS `customergroups_view`;
CREATE VIEW `customergroups_view` as
Select  count(distinct(`customergroup`.`Customer_id`)) as `customerCount`, 
`groupservicedefinition`.`serviceDefinitionId` as `servicedefinitionId`,
`groupservicedefinition`.`Group_id` as `groupId` 
from `groupservicedefinition`
join `contract`  on `groupservicedefinition`.`serviceDefinitionId` =`contract`.`servicedefinitionId` 
join  `customergroup`  on `groupservicedefinition`.`Group_id`=`customergroup`.`Group_id` and  `contract`.`id`=`customergroup`.`contractId`
group by `groupservicedefinition`.`Group_id`, `groupservicedefinition`.`serviceDefinitionId`;

DROP PROCEDURE IF EXISTS `fetch_achfiles_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_achfiles_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _achFile_id VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByParam TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByValue TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;
		
		SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from application);
        
		IF @isSelfApprovalEnabled is FALSE THEN
			SET @notCreatedBySelf = concat(" AND NOT FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"')");
		ELSE                
			SET @notCreatedBySelf = "";
		END IF;
		
        SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
        SET _achFile_id = if(_achFile_id = "" OR _achFile_id = NULL, '%', _achFile_id);
       
        SET @numOfParams = 0;
        IF LENGTH(_filterByParam) > 0 THEN
			SET @numOfParams = LENGTH(_filterByParam) - LENGTH(REPLACE(_filterByParam, ',', '')) + 1;
		END IF;
        
        SET @searchQuery = "";
		SET @idx = 1;
		filterParams:LOOP
			IF @idx > @numOfParams THEN 
				LEAVE filterParams;
			END IF;
			
			SET @filterParam = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByParam, ',', @idx), ',', -1 );
			SET @filterValue = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByValue, ',', @idx), ',', -1 );
			SET @searchQuery = concat(@searchQuery, " AND (",@filterParam," LIKE '",@filterValue,"' )");
			SET @idx = @idx + 1;
			 
		END LOOP filterParams;
        
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        							AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
													OR
												(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
									);
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achfile`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achfile`.`status` = 'Pending' ", @notCreatedBySelf),
                                        if(_queryType = 'rejected', concat("AND `achfile`.`status` = 'Rejected' "),
                                        if(_achFile_id = '%' , concat(" AND NOT `achfile`.`status` = 'Withdrawn'"),
                                        ''))));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery, 
                concat(@searchQuery, " AND (`achfile`.`achFileName` LIKE '%",_searchString,"%' OR `achfile`.`requestType` LIKE '%",_searchString,"%')"));
        
        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize)); 
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_UPLOAD");
        IF @createActions is NULL THEN      
			SET @createActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
			`achfile`.`achFile_id` AS `achFile_id`,
			`achfile`.`achFileName` AS `achFileName`,
			`achfile`.`featureActionId` AS `featureActionId`,
			`achfile`.`debitAmount` AS `debitAmount`,
			`achfile`.`approvalAccounts` AS `approvalAccounts`,
			`achfile`.`debitAccounts` AS `debitAccounts`,
			`achfile`.`createdby` AS `createdby`,
			`achfile`.`createdts` AS `createdts`,
			`achfile`.`requestType` AS `requestType`,
			`achfile`.`numberOfCredits` AS `numberOfCredits`,
			`achfile`.`numberOfDebits` AS `numberOfDebits`,
			`achfile`.`numberOfPrenotes` AS `numberOfPrenotes`,
			`achfile`.`requestId` AS `requestId`,
			`achfile`.`contents` AS `contents`,
			`achfile`.`fileSize` AS `fileSize`,
			`achfile`.`softDelete` AS `softDelete`,
			`achfile`.`creditAmount` AS `creditAmount`,
			`achfile`.`numberOfRecords` AS `numberOfRecords`,
			`achfile`.`achFileFormatType_id` AS `achFileFormatType_id`,
			`achfile`.`status` AS `status`,
            `achfile`.`companyId` AS `companyId`,
            `achfile`.`confirmationNumber` AS `confirmationNumber`,
			`customer`.`UserName` AS `userName`,
			`achfileformattype`.`fileType` AS `achFileFormatType`,
			`bbrequest`.`createdby` AS `requestCreatedby`,
			(CASE
				WHEN `achfile`.`createdby` IN (",@combinedIds,") THEN 'true'
				ELSE 'false'
			 END) as `amICreator`,
			(CASE 
				WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
        		ELSE 'false'
	 		END)
	 		 as `amIApprover`
		FROM
			(((`achfile`
			LEFT JOIN `customer` ON (`achfile`.`createdby` = `customer`.`id`))
			LEFT JOIN `achfileformattype` ON (`achfile`.`achFileFormatType_id` = `achfileformattype`.`id`))
			LEFT JOIN `bbrequest` ON (`achfile`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achfile`.`softDelete` = '0'
				AND (FIND_IN_SET(`achfile`.`companyId`, '", @companyId ,"') OR FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"'))
				AND `achfile`.`achFile_id` LIKE '",_achFile_id ,"'
                AND FIND_IN_SET(`achfile`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE FIND_IN_SET(bbrequest.requestId, \"",@companyRequestIds,"\")
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            `t1`.`requestId` = `t2`.`requestId`
            ORDER BY ", _sortByParam, " ", _sortOrder , " ",
            @paginationQuery);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

DROP procedure IF EXISTS `fetch_bbtemplate_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_bbtemplate_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _templateId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByParam TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByValue TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
		SET SESSION group_concat_max_len = 100000000;
		
		SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;
		
		SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
		
        SET _templateId = if(_templateId = "" OR _templateId = NULL, '%', _templateId);
        
        SET @numOfParams = 0;
        IF LENGTH(_filterByParam) > 0 THEN
			SET @numOfParams = LENGTH(_filterByParam) - LENGTH(REPLACE(_filterByParam, ',', '')) + 1;
		END IF;
        
        SET @searchQuery = "";
		SET @idx = 1;
		filterParams:LOOP
			IF @idx > @numOfParams THEN 
				LEAVE filterParams;
			END IF;
			
			SET @filterParam = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByParam, ',', @idx), ',', -1 );
			SET @filterValue = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByValue, ',', @idx), ',', -1 );
			SET @searchQuery = concat(@searchQuery, " AND (",@filterParam," LIKE '",@filterValue,"' )");
			SET @idx = @idx + 1;
			 
		END LOOP filterParams;
        
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
        
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest where FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
        
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
        								AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							);
        
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`bbtemplate`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected') "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`bbtemplate`.`requestId`,  \"",@approvalRequestIds,"\")  AND `bbtemplate`.`status` = 'Pending' "),
                                        if(_templateId = '%' ,concat(" AND NOT `bbtemplate`.`status` = 'Withdrawn'"),
                                        '')));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery, 
								concat(@searchQuery, " AND (`bbtemplate`.`templateName` LIKE '%",_searchString,"%' OR `customeraccounts`.`AccountName` LIKE '%",_searchString,"%' OR `bbtemplaterequesttype`.`templateRequestTypeName` LIKE '%",_searchString,"%' OR `bbtemplate`.`fromAccount` LIKE '%",_searchString,"%')"));
        
        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize)); 
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE_TEMPLATE");
        
        IF @createActions is NULL THEN      
			SET @createActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
        SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts WHERE FIND_IN_SET(Customer_id, @combinedIds));
	    
	    IF @customerAcounts is NULL THEN      
			SET @customerAcounts = "";
		END IF;
	    
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
			`bbtemplate`.`templateId` AS `templateId`,
            `bbtemplate`.`templateName` AS `templateName`,
            `bbtemplate`.`templateDescription` AS `templateDescription`,
			`bbtemplate`.`featureActionId` AS `featureActionId`,
			`bbtemplate`.`fromAccount` AS `fromAccount`,
			`bbtemplate`.`effectiveDate` AS `effectiveDate`,
			`bbtemplate`.`requestId` AS `requestId`,
			`bbtemplate`.`createdby` AS `createdby`,
            `bbtemplate`.`updatedBy` AS `updatedBy`,
			`bbtemplate`.`createdts` AS `createdts`,
			`bbtemplate`.`maxAmount` AS `maxAmount`,
			`bbtemplate`.`status` AS `status`,
			`bbtemplate`.`transactionType_id` AS `transactionType_id`,
			`bbtemplate`.`templateType_id` AS `templateType_id`,
			`bbtemplate`.`companyId` AS `companyId`,
			`bbtemplate`.`templateRequestType_id` AS `templateRequestType_id`,
			`bbtemplate`.`softDelete` AS `softDelete`,
			`bbtemplate`.`totalAmount` AS `totalAmount`,
			( CASE 
				WHEN `customeraccounts`.`AccountName` is NULL THEN 'AccountName' 
                ELSE `customeraccounts`.`AccountName` 
			END ) AS `accountName`,
			`customer`.`UserName` AS `userName`,
			`bbtransactiontype`.`transactionTypeName` AS `transactionTypeName`,
			`bbtemplatetype`.`templateTypeName` AS `templateTypeName`,
			`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`,
			`bbrequest`.`createdby` AS `requestCreatedby`,
			(CASE
				WHEN `bbtemplate`.`createdby` IN (",@combinedIds,") THEN 'true'
				ELSE 'false'
			 END) as `amICreator`,
			(CASE 
				WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
        		ELSE 'false'
	 		END)
	 		 as `amIApprover`
		FROM
			((((((`bbtemplate`
			LEFT JOIN `customer` ON (`bbtemplate`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`bbtemplate`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`bbtemplate`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`bbtemplate`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`bbtemplate`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`bbtemplate`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
			LEFT JOIN `bbrequest` ON (`bbtemplate`.`requestId` = `bbrequest`.`requestId`))
			WHERE `bbtemplate`.`softDelete` = '0'
				AND (FIND_IN_SET(`bbtemplate`.`companyId` , '", @companyId ,"') OR FIND_IN_SET(`bbtemplate`.`createdby`, '",@combinedIds,"'))
				AND `bbtemplate`.`templateId` LIKE '",_templateId ,"'
				AND FIND_IN_SET(`bbtemplate`.`fromAccount`, \"", @customerAcounts, "\")
                AND FIND_IN_SET(`bbtemplate`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE FIND_IN_SET(bbrequest.requestId, \"",@companyRequestIds,"\")
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            `t1`.`requestId` = `t2`.`requestId`
            ORDER BY ", _sortByParam, " ", _sortOrder , " ",
            @paginationQuery);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_generaltranscation_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_generaltranscation_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureActionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByParam TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByValue TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
		SET SESSION group_concat_max_len = 100000000;
		
		SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;
		
		SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from application);
		
		IF @isSelfApprovalEnabled is FALSE THEN
			SET @notCreatedBySelf = concat(" AND NOT FIND_IN_SET(`generaltransaction`.`createdby`, '",@combinedIds,"')");
		ELSE
			SET @notCreatedBySelf = "";
        END IF;
		
		SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
        
        SET _transactionId = IF(_transactionId = "" OR _transactionId = NULL, '%', _transactionId);
        SET _featureActionId = IF(_transactionId = "%" , '%', _featureActionId);

		SET @numOfParams = 0;
        IF LENGTH(_filterByParam) > 0 THEN
			SET @numOfParams = LENGTH(_filterByParam) - LENGTH(REPLACE(_filterByParam, ',', '')) + 1;
		END IF;
        
        SET @searchQuery = "";
		SET @idx = 1;
		filterParams:LOOP
			IF @idx > @numOfParams THEN 
				LEAVE filterParams;
			END IF;
			
			SET @filterParam = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByParam, ',', @idx), ',', -1 );
			SET @filterValue = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByValue, ',', @idx), ',', -1 );
			SET @searchQuery = concat(@searchQuery, " AND (",@filterParam," LIKE '",@filterValue,"' )");
			SET @idx = @idx + 1;
			 
		END LOOP filterParams;

	    SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	    
	    IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
        
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
        								AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							);
        
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`generaltransaction`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
								  if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`generaltransaction`.`requestId`,  '",@approvalRequestIds,"')  AND `generaltransaction`.`status` = 'Pending' ",@notCreatedBySelf),
                                        if(_queryType = 'rejected', concat(" AND `generaltransaction`.`status` = 'Rejected' "),
                                        if(_transactionId = '%' , concat(" AND NOT `generaltransaction`.`status` = 'Withdrawn'"),
                                        ''))));
        -- select @queryTypecondition;
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery, 
								concat(@searchQuery, " AND (`generaltransaction`.`payeeId` LIKE '%",_searchString,"%' OR `customeraccounts`.`accountName` LIKE '%",_searchString,"%' OR `feature`.`name` LIKE '%",_searchString,"%' OR `customer`.`userName` LIKE '%",_searchString,"%' )"));
        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize)); 
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE");
        
        IF @createActions is NULL THEN      
			SET @createActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts WHERE FIND_IN_SET(Customer_id, @combinedIds));
	    
        IF @customerAcounts is NULL THEN      
			SET @customerAcounts = "";
		END IF;
        
        SET @accountsQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND FIND_IN_SET(`generaltransaction`.`fromAccountNumber`, '", @customerAcounts,"') "));
        SET @companyQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND ( FIND_IN_SET(`generaltransaction`.`companyId` , '", @companyId ,"') OR FIND_IN_SET(`generaltransaction`.`createdby`, '",@combinedIds,"'))"));
        
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
						`generaltransaction`.`transactionId`,
						`generaltransaction`.`featureActionId`,
						`feature`.`name` as `featureName`,
						`generaltransaction`.`fromAccountNumber`,
						`generaltransaction`.`amount`,
						`generaltransaction`.`requestId`,
						`generaltransaction`.`createdby`,
						`generaltransaction`.`createdts`,
						`generaltransaction`.`frequencyTypeId`,
						`generaltransaction`.`status`,
						`generaltransaction`.`numberOfRecurrences`,
						`generaltransaction`.`payeeId`,
						`generaltransaction`.`companyId`,
						`generaltransaction`.`scheduledDate`,
						( CASE 
							WHEN `customeraccounts`.`AccountName` is NULL THEN 'AccountName' 
			                ELSE `customeraccounts`.`AccountName` 
						END ) AS `accountName`,
						`customer`.`UserName` AS `userName`,
						`bbrequest`.`createdby` AS `requestCreatedby`,
						(CASE
							WHEN `generaltransaction`.`createdby` IN (",@combinedIds,") THEN 'true'
							ELSE 'false'
						 END) as `amICreator`,
						(CASE 
							WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
			        		ELSE 'false'
				 		END)
				 		 as `amIApprover`

				FROM

				( SELECT 
						`billpaytransfers`.`transactionId` AS `transactionId`,
						`billpaytransfers`.`featureActionId` AS `featureActionId`,
						`billpaytransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`billpaytransfers`.`amount` AS `amount`,
						`billpaytransfers`.`requestId` AS `requestId`,
						`billpaytransfers`.`createdby` AS `createdby`,
						`billpaytransfers`.`createdts` AS `createdts`,
						`billpaytransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`billpaytransfers`.`status` AS `status`,
						`billpaytransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
                        CASE 
							WHEN `billpaytransfers`.`payeeName` IS NULL OR `billpaytransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `billpaytransfers`.`billerId` IS NULL OR `billpaytransfers`.`billerId` = '' THEN 
									CASE 
										WHEN `billpaytransfers`.`payeeId` IS NULL OR `billpaytransfers`.`payeeId` = '' THEN `billpaytransfers`.`toAccountNumber`
										ELSE `billpaytransfers`.`payeeId`
									END
								ELSE `billpaytransfers`.`billerId`
							END
							ELSE `billpaytransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`billpaytransfers`.`companyId` AS `companyId`,
		                `billpaytransfers`.`softdeleteflag` AS `softdeleteflag`,
						`billpaytransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`billpaytransfers`
				UNION
					SELECT 
						`p2ptransfers`.`transactionId` AS `transactionId`,
						`p2ptransfers`.`featureActionId` AS `featureActionId`,
						`p2ptransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`p2ptransfers`.`amount` AS `amount`,
						`p2ptransfers`.`requestId` AS `requestId`,
						`p2ptransfers`.`createdby` AS `createdby`,
						`p2ptransfers`.`createdts` AS `createdts`,
						`p2ptransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`p2ptransfers`.`status` AS `status`,
						`p2ptransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
						CASE 
							WHEN `p2ptransfers`.`payeeName` IS NULL OR `p2ptransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `p2ptransfers`.`personId` IS NULL OR `p2ptransfers`.`personId` = '' THEN 
									CASE 
										WHEN `p2ptransfers`.`p2pContact` IS NULL OR `p2ptransfers`.`p2pContact` = '' THEN `p2ptransfers`.`toAccountNumber`
										ELSE `p2ptransfers`.`p2pContact`
									END
								ELSE `p2ptransfers`.`personId`
							END
							ELSE `p2ptransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`p2ptransfers`.`companyId` AS `companyId`,
		                `p2ptransfers`.`softdeleteflag` AS `softdeleteflag`,
						`p2ptransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`p2ptransfers`
				UNION
					SELECT 
						`wiretransfers`.`transactionId` AS `transactionId`,
						`wiretransfers`.`featureActionId` AS `featureActionId`,
						`wiretransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`wiretransfers`.`amount` AS `amount`,
						`wiretransfers`.`requestId` AS `requestId`,
						`wiretransfers`.`createdby` AS `createdby`,
						`wiretransfers`.`createdts` AS `createdts`,
						null AS `frequencyTypeId`,
						`wiretransfers`.`status` AS `status`,
						null AS `numberOfRecurrences`,
                        CASE 
							WHEN `wiretransfers`.`payeeName` IS NULL OR `wiretransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `wiretransfers`.`payPersonName` IS NULL OR `wiretransfers`.`payPersonName` = '' THEN 
									CASE 
										WHEN `wiretransfers`.`payeeId` IS NULL OR `wiretransfers`.`payeeId` = '' THEN `wiretransfers`.`payeeAccountNumber`
										ELSE `wiretransfers`.`payeeId`
									END
								ELSE `wiretransfers`.`payPersonName`
							END
							ELSE `wiretransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`wiretransfers`.`companyId` AS `companyId`,
		                `wiretransfers`.`softdeleteflag` AS `softdeleteflag`,
						`wiretransfers`.`createdts` AS `scheduledDate`
					FROM
						`wiretransfers`
				UNION
					SELECT 
						`intrabanktransfers`.`transactionId` AS `transactionId`,
						`intrabanktransfers`.`featureActionId` AS `featureActionId`,
						`intrabanktransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`intrabanktransfers`.`amount` AS `amount`,
						`intrabanktransfers`.`requestId` AS `requestId`,
						`intrabanktransfers`.`createdby` AS `createdby`,
						`intrabanktransfers`.`createdts` AS `createdts`,
						`intrabanktransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`intrabanktransfers`.`status` AS `status`,
						`intrabanktransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
                        CASE 
							WHEN `intrabanktransfers`.`payeeName` IS NULL OR `intrabanktransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `intrabanktransfers`.`payPersonName` IS NULL OR `intrabanktransfers`.`payPersonName` = '' THEN 
									CASE 
										WHEN `intrabanktransfers`.`personId` IS NULL OR `intrabanktransfers`.`personId` = '' THEN `intrabanktransfers`.`toAccountNumber`
										ELSE `intrabanktransfers`.`personId`
									END
								ELSE `intrabanktransfers`.`payPersonName`
							END
							ELSE `intrabanktransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`intrabanktransfers`.`companyId` AS `companyId`,
		                `intrabanktransfers`.`softdeleteflag` AS `softdeleteflag`,
						`intrabanktransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`intrabanktransfers`
				UNION
					SELECT 
						`interbankfundtransfers`.`transactionId` AS `transactionId`,
						`interbankfundtransfers`.`featureActionId` AS `featureActionId`,
						`interbankfundtransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`interbankfundtransfers`.`amount` AS `amount`,
						`interbankfundtransfers`.`requestId` AS `requestId`,
						`interbankfundtransfers`.`createdby` AS `createdby`,
						`interbankfundtransfers`.`createdts` AS `createdts`,
						`interbankfundtransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`interbankfundtransfers`.`status` AS `status`,
						`interbankfundtransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
						CASE 
							WHEN `interbankfundtransfers`.`payeeName` IS NULL OR `interbankfundtransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `interbankfundtransfers`.`payPersonName` IS NULL OR `interbankfundtransfers`.`payPersonName` = '' THEN 
									CASE 
										WHEN `interbankfundtransfers`.`personId` IS NULL OR `interbankfundtransfers`.`personId` = '' THEN `interbankfundtransfers`.`toAccountNumber`
										ELSE `interbankfundtransfers`.`personId`
									END
								ELSE `interbankfundtransfers`.`payPersonName`
							END
							ELSE `interbankfundtransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`interbankfundtransfers`.`companyId` AS `companyId`,
		                `interbankfundtransfers`.`softdeleteflag` AS `softdeleteflag`,
						`interbankfundtransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`interbankfundtransfers`
				UNION
					SELECT 
						`internationalfundtransfers`.`transactionId` AS `transactionId`,
						`internationalfundtransfers`.`featureActionId` AS `featureActionId`,
						`internationalfundtransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`internationalfundtransfers`.`amount` AS `amount`,
						`internationalfundtransfers`.`requestId` AS `requestId`,
						`internationalfundtransfers`.`createdby` AS `createdby`,
						`internationalfundtransfers`.`createdts` AS `createdts`,
						`internationalfundtransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`internationalfundtransfers`.`status` AS `status`,
						`internationalfundtransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
						CASE 
							WHEN `internationalfundtransfers`.`payeeName` IS NULL OR `internationalfundtransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `internationalfundtransfers`.`payPersonName` IS NULL OR `internationalfundtransfers`.`payPersonName` = '' THEN 
									CASE 
										WHEN `internationalfundtransfers`.`personId` IS NULL OR `internationalfundtransfers`.`personId` = '' THEN `internationalfundtransfers`.`toAccountNumber`
										ELSE `internationalfundtransfers`.`personId`
									END
								ELSE `internationalfundtransfers`.`payPersonName` 
							END
							ELSE `internationalfundtransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`internationalfundtransfers`.`companyId` AS `companyId`,
		                `internationalfundtransfers`.`softdeleteflag` AS `softdeleteflag`,
						`internationalfundtransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`internationalfundtransfers`
				UNION
					SELECT 
						`ownaccounttransfers`.`transactionId` AS `transactionId`,
						`ownaccounttransfers`.`featureActionId` AS `featureActionId`,
						`ownaccounttransfers`.`fromAccountNumber` AS `fromAccountNumber`,
						`ownaccounttransfers`.`amount` AS `amount`,
						`ownaccounttransfers`.`requestId` AS `requestId`,
						`ownaccounttransfers`.`createdby` AS `createdby`,
						`ownaccounttransfers`.`createdts` AS `createdts`,
						`ownaccounttransfers`.`frequencyTypeId` AS `frequencyTypeId`,
						`ownaccounttransfers`.`status` AS `status`,
						`ownaccounttransfers`.`numberOfRecurrences` AS `numberOfRecurrences`,
						CASE 
							WHEN `ownaccounttransfers`.`payeeName` IS NULL OR `ownaccounttransfers`.`payeeName` = '' THEN 
							CASE
								WHEN `ownaccounttransfers`.`payPersonName` IS NULL OR `ownaccounttransfers`.`payPersonName` = '' THEN 
									CASE 
										WHEN `ownaccounttransfers`.`personId` IS NULL OR `ownaccounttransfers`.`personId` = '' THEN `ownaccounttransfers`.`toAccountNumber`
										ELSE `ownaccounttransfers`.`personId`
									END
								ELSE `ownaccounttransfers`.`payPersonName`
							END
							ELSE `ownaccounttransfers`.`payeeName`
						END 
                        AS `payeeId`,
						`ownaccounttransfers`.`companyId` AS `companyId`,
		                `ownaccounttransfers`.`softdeleteflag` AS `softdeleteflag`,
						`ownaccounttransfers`.`scheduledDate` AS `scheduledDate`
					FROM
						`ownaccounttransfers`
				) AS `generaltransaction`
		        LEFT JOIN `customer` ON (`generaltransaction`.`createdby` = `customer`.`id`)
		        LEFT JOIN `customeraccounts` ON ((`generaltransaction`.`createdby` = `customeraccounts`.`Customer_id`)
						AND (`generaltransaction`.`fromAccountNumber` = `customeraccounts`.`Account_id`))
		        LEFT JOIN `bbrequest` ON (`generaltransaction`.`requestId` = `bbrequest`.`requestId`)
		        LEFT JOIN `featureaction` ON (`generaltransaction`.`featureActionId` = `featureaction`.`id`)
                LEFT JOIN `feature` ON (`featureaction`.`Feature_id` = `feature`.`id`)
        
        	WHERE `generaltransaction`.`softdeleteflag` = 0
				",@companyQuery,"
				AND `generaltransaction`.`transactionId` LIKE '",_transactionId ,"' AND `generaltransaction`.`featureActionId` LIKE '",_featureActionId ,"'
				",@accountsQuery,"
                AND FIND_IN_SET(`generaltransaction`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE FIND_IN_SET(bbrequest.requestId, \"",@companyRequestIds,"\")
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            `t1`.`requestId` = `t2`.`requestId`
            ORDER BY ", _sortByParam, " ", _sortOrder , " ",
            @paginationQuery);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_achtransaction_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_achtransaction_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByParam TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByValue TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
		SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;
        
		SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from application);
		
		IF @isSelfApprovalEnabled is FALSE THEN
            SET @notCreatedBySelf = concat(" AND NOT FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"')");
        ELSE
            SET @notCreatedBySelf = "";
		END IF;
		
		SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
        SET _transactionId = if(_transactionId = "" OR _transactionId = NULL, '%', _transactionId);
        
        SET @numOfParams = 0;
        IF LENGTH(_filterByParam) > 0 THEN
			SET @numOfParams = LENGTH(_filterByParam) - LENGTH(REPLACE(_filterByParam, ',', '')) + 1;
		END IF;
        
        SET @searchQuery = "";
		SET @idx = 1;
		filterParams:LOOP
			IF @idx > @numOfParams THEN 
				LEAVE filterParams;
			END IF;
			
			SET @filterParam = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByParam, ',', @idx), ',', -1 );
			SET @filterValue = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByValue, ',', @idx), ',', -1 );
			SET @searchQuery = concat(@searchQuery, " AND (",@filterParam," LIKE '",@filterValue,"' )");
			SET @idx = @idx + 1;
			 
		END LOOP filterParams;
        
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	    IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix where FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest where FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix 
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							);
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achtransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achtransaction`.`status` = 'Pending' ",@notCreatedBySelf),
                                        if(_queryType = 'rejected', concat(" AND `achtransaction`.`status` = 'Rejected' "),
                                        if(_transactionId = '%' , concat(" AND NOT `achtransaction`.`status` = 'Withdrawn'"),
                                        ''))));
                                        
        SET @validAccountsJoin = if(_queryType = '', concat(" INNER JOIN (SELECT DISTINCT Account_id,
									REPLACE(Action_id, '_VIEW', '_CREATE') as Action_id
									FROM 
									customeraction 
									WHERE Customer_id = ",_customerId,"
									AND isAllowed = '1' 
									AND Account_id is NOT null
									AND Action_id like '%_VIEW') as 
								`can` ON (`achtransaction`.`featureActionId` = `can`.`Action_id` 
											AND `achtransaction`.`fromAccount` = `can`.`Account_id`) "), '');
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery, 
								concat(@searchQuery, " AND (`achtransaction`.`templateName` LIKE '%",_searchString,"%' OR `customeraccounts`.`AccountName` LIKE '%",_searchString,"%' OR `bbtemplaterequesttype`.`templateRequestTypeName` LIKE '%",_searchString,"%' OR `achtransaction`.`transaction_id` LIKE '%",_searchString,"%' OR `achtransaction`.`fromAccount` LIKE '%",_searchString,"%' )"));
        
        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize)); 
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE");
        IF @createActions is NULL THEN      
			SET @createActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts where FIND_IN_SET(Customer_id, @combinedIds));
	    IF @customerAcounts is NULL THEN      
			SET @customerAcounts = "";
		END IF;
	    
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
			`achtransaction`.`transaction_id` AS `transaction_id`,
			`achtransaction`.`fromAccount` AS `fromAccount`,
			`achtransaction`.`effectiveDate` AS `effectiveDate`,
			`achtransaction`.`requestId` AS `requestId`,
			`achtransaction`.`createdby` AS `createdby`,
			`achtransaction`.`createdts` AS `createdts`,
			`achtransaction`.`maxAmount` AS `maxAmount`,
			`achtransaction`.`status` AS `status`,
			`achtransaction`.`transactionType_id` AS `transactionType_id`,
			`achtransaction`.`templateType_id` AS `templateType_id`,
			`achtransaction`.`companyId` AS `companyId`,
			`achtransaction`.`templateRequestType_id` AS `templateRequestType_id`,
			`achtransaction`.`softDelete` AS `softDelete`,
			`achtransaction`.`templateName` AS `templateName`,
			`achtransaction`.`template_id` AS `template_id`,
			`achtransaction`.`totalAmount` AS `totalAmount`,
			`achtransaction`.`featureActionId` AS `featureActionId`,
			`achtransaction`.`confirmationNumber` AS `confirmationNumber`,
			( CASE 
				WHEN `customeraccounts`.`AccountName` is NULL THEN 'AccountName' 
                ELSE `customeraccounts`.`AccountName` 
			END ) AS `accountName`,
			`customer`.`UserName` AS `userName`,
			`bbtransactiontype`.`transactionTypeName` AS `transactionTypeName`,
			`bbtemplatetype`.`templateTypeName` AS `templateTypeName`,
			`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`,
			`bbrequest`.`createdby` AS `requestCreatedby`,
			(CASE
				WHEN `achtransaction`.`createdby` IN (",@combinedIds,") THEN 'true'
				ELSE 'false'
			 END) as `amICreator`,
			(CASE 
				WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
        		ELSE 'false'
	 		END)
	 		 as `amIApprover`
		FROM
			((((((`achtransaction`
			LEFT JOIN `customer` ON (`achtransaction`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`achtransaction`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`achtransaction`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`achtransaction`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`achtransaction`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`achtransaction`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))"
            ,@validAccountsJoin,
            "
			LEFT JOIN `bbrequest` ON (`achtransaction`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achtransaction`.`softDelete` = '0'
				AND ( FIND_IN_SET(`achtransaction`.`companyId` , '", @companyId ,"') OR FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"'))
				AND `achtransaction`.`transaction_id` LIKE '",_transactionId ,"'
				AND FIND_IN_SET(`achtransaction`.`fromAccount`, \"", @customerAcounts, "\")
                AND FIND_IN_SET(`achtransaction`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
                    (select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE FIND_IN_SET(bbrequest.requestId, \"",@companyRequestIds,"\")
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            `t1`.`requestId` = `t2`.`requestId`
            ORDER BY ", _sortByParam, " ", _sortOrder , " ",
            @paginationQuery);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END $$
DELIMITER ;

ALTER TABLE `achfile` CHANGE COLUMN `contents` `contents` LONGTEXT NULL DEFAULT NULL ;
ALTER TABLE `interbankfundtransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `interbankfundtransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `interbankfundtransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN fileName varchar(50) default NULL;


ALTER TABLE `transaction` CHANGE COLUMN `convertedAmount` `convertedAmount` VARCHAR(45) NULL DEFAULT NULL ;
ALTER TABLE `transaction` ADD COLUMN serviceCharge VARCHAR(45) NULL DEFAULT NULL;

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
        
        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = "" OR @combinedIds = NULL, "''", @combinedIds);
        
        SET _transactionIds = if(_transactionIds = "" OR _transactionIds = NULL, "''", _transactionIds);
        SET _requestIds = if(_requestIds = "" OR _requestIds = NULL, "''", _requestIds);
       
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        							AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
													OR
												(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
									);
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND Type_id = "MONETARY");
        IF @monetaryActions is NULL THEN      
			SET @monetaryActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
        SET @requestIds = if(_requestIds = "''",@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = "''"
	        ,concat("FIND_IN_SET(bbrequest.requestId, \"",@requestIds,"\") ")
	        ,concat("FIND_IN_SET(bbrequest.transactionId, \"",_transactionIds, "\") AND  FIND_IN_SET(bbrequest.featureActionId, \"",@monetaryActions,"\")") );
        
		SET @select_statement = concat("SELECT 
					bbrequest.requestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (",@combinedIds,") THEN 'true'
						ELSE 'false'
					 END) as `amICreator`,
					(CASE 
						WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
						ELSE 'false'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (",@alreadyApprovedIds,") THEN 'true'
						ELSE 'false'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

ALTER TABLE `featureaction` ADD `approveFeatureAction` VARCHAR(255) DEFAULT NULL;

ALTER TABLE `billpaytransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `billpaytransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `billpaytransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

ALTER TABLE `internationalfundtransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `internationalfundtransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `internationalfundtransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

ALTER TABLE `intrabanktransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `intrabanktransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `intrabanktransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

ALTER TABLE `ownaccounttransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `ownaccounttransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `ownaccounttransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

ALTER TABLE `p2ptransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `p2ptransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `p2ptransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

ALTER TABLE `wiretransfers` ADD COLUMN serviceCharge VARCHAR(45) DEFAULT NULL;
ALTER TABLE `wiretransfers` ADD COLUMN transactionAmount VARCHAR(45) DEFAULT NULL;
ALTER TABLE `wiretransfers` ADD COLUMN convertedAmount VARCHAR(45) DEFAULT NULL;

DROP PROCEDURE IF EXISTS `systemroles_permission_proc`;

DELIMITER $$
CREATE PROCEDURE `systemroles_permission_proc`(
IN _roleIds varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 SELECT distinct
	    `p`.`id` AS `id`,
        `p`.`Name` AS `name`,
        `p`.`Status_id` AS `status`,
		`p`.`PermissionValue` AS `PermissionValue`,
		 CAST(`p`.`softdeleteflag` AS unsigned) AS `softdeleteflag`
    FROM
    `rolepermission` `rp` , `permission` `p`
    where `p`.`id` = `rp`.`Permission_id` and FIND_IN_SET(`rp`.`Role_id`,_roleIds) order by `id`;
END$$
DELIMITER ;

ALTER TABLE `application` ADD COLUMN `isKeyCloakEnabled` TINYINT(1) NOT NULL DEFAULT '1' AFTER `customerCreationMode`;

DELIMITER ;
DROP PROCEDURE IF EXISTS `rolescompositeactions_proc`;

DELIMITER $$
CREATE PROCEDURE `rolescompositeactions_proc`(
IN _roleIds varchar(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _isEnabled TINYINT(1))
BEGIN
SELECT 
    `CompositeAction_id`,CASE WHEN `isEnabled` = '1' THEN 'true' ELSE 'false' END AS `isEnabled`
FROM
    `rolecompositeaction`
WHERE
    FIND_IN_SET(`role_id`, _roleIds)
        AND `isEnabled` = ifnull(_isEnabled,1) 
UNION SELECT 
    `CompositeAction_id`, CASE WHEN `isEnabled` = '1' THEN 'true' ELSE 'false' END AS `isEnabled`
FROM
    `rolecompositeaction`
WHERE
    FIND_IN_SET(`role_id`, _roleIds)
        AND `CompositeAction_id` NOT IN (SELECT 
            `CompositeAction_id`
        FROM
            `rolecompositeaction`
        WHERE
            FIND_IN_SET(`role_id`, _roleIds)
                AND `isEnabled` = ifnull(_isEnabled,1))
        AND `isEnabled` = ifnull(_isEnabled,0) ;
END$$
DELIMITER ;

ALTER TABLE `csrassistgrant` MODIFY COLUMN `userRoleId` VARCHAR(500);

ALTER TABLE `compositeaction` DROP PRIMARY KEY, ADD PRIMARY KEY(`id`,`Permission_id`), ADD FOREIGN KEY (`Permission_id`) REFERENCES `permission`(`id`);

DELIMITER ;
DROP PROCEDURE IF EXISTS `rolePermissionDelete_proc`;

DELIMITER $$
CREATE PROCEDURE `rolePermissionDelete_proc`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
DECLARE caid VARCHAR(50);
DECLARE isEnabled VARCHAR(10);
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM compositeaction c WHERE FIND_IN_SET(`Permission_id`, _PermissionIds));
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

OPEN caids; 
MANAGECAIDS : LOOP
FETCH caids INTO caid, isEnabled;
IF finished = 1 THEN 
	LEAVE MANAGECAIDS;
END IF;
SET @caid_count =(SELECT COUNT(*) FROM compositeaction c,  rolepermission rp WHERE rp.Role_id= _roleId
AND rp.Permission_id=c.Permission_id
AND NOT FIND_IN_SET(rp.Permission_id,_PermissionIds)
AND c.id=caid);

IF @caid_count=0 THEN 
DELETE FROM rolecompositeaction WHERE Role_id=_roleId AND CompositeAction_id=caid;
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;
DELETE FROM rolepermission WHERE Role_id=_roleId AND FIND_IN_SET(Permission_id,_PermissionIds);
END$$
DELIMITER ;

DROP procedure IF EXISTS `userPermissionDelete_proc`;

DELIMITER $$
CREATE PROCEDURE `userPermissionDelete_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
DECLARE caid VARCHAR(50);
DECLARE isEnabled VARCHAR(10);
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE caids CURSOR FOR (SELECT c.id FROM compositeaction c WHERE FIND_IN_SET(`Permission_id`, _PermissionIds));
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

OPEN caids; 
MANAGECAIDS : LOOP
FETCH caids INTO caid;
IF finished = 1 THEN 
	LEAVE MANAGECAIDS;
END IF;
SET @caid_count =(SELECT COUNT(*) FROM compositeaction c,  userpermission up WHERE up.User_id = _userId
AND up.Permission_id=c.Permission_id
AND NOT FIND_IN_SET(up.Permission_id,_PermissionIds)
AND c.id=caid);

IF @caid_count=0 THEN 
DELETE FROM usercompositeaction WHERE User_id=_userId AND CompositeAction_id=caid;
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;
DELETE FROM userpermission WHERE User_id=_userId AND FIND_IN_SET(Permission_id,_PermissionIds);
END$$
DELIMITER ;

ALTER TABLE `bulkwirefiles` 
DROP FOREIGN KEY `FK_bulkwirefiles_CompanyID`;

ALTER TABLE `ownaccounttransfers` 
DROP FOREIGN KEY `FK_internaltransfers_companyIdx`;

ALTER TABLE `bbrequest` 
DROP FOREIGN KEY `FK_bbrequest_businessbankingcompany`;

ALTER TABLE `bbactedrequest`
DROP FOREIGN KEY `FK_bbactedrequest_Organisation_id`;

ALTER TABLE `interbankfundtransfers` 
DROP FOREIGN KEY `FK_interbankfundtransfers_companyIdx`;

ALTER TABLE `intrabanktransfers` 
DROP FOREIGN KEY `FK_intrabanktransfers_companyIdx`;

ALTER TABLE `internationalfundtransfers` 
DROP FOREIGN KEY `FK_externaltransfers_companyIdx`;

ALTER TABLE `p2ptransfers` 
DROP FOREIGN KEY `FK_p2ptransfers_companyIdx`;

ALTER TABLE `billpaytransfers` 
DROP FOREIGN KEY `FK_billpaytranfers_companyIdx`;

ALTER TABLE `wiretransfers` 
DROP FOREIGN KEY `FK_wiretransfers_companyIdx`;

ALTER TABLE `achtransaction` 
DROP FOREIGN KEY `FK_bbtransaction_Organization`;

ALTER TABLE `bbtemplate` 
DROP FOREIGN KEY `FK_bbtemplate_Organization`;

ALTER TABLE `achfile` 
DROP FOREIGN KEY `FK_achfile_Company_id`;

ALTER TABLE `bulkpaymentfiles` 
DROP FOREIGN KEY `FK_bulkpaymentfiles_companyIdx`;

ALTER TABLE `bulkpaymentfilesmock` 
DROP FOREIGN KEY `FK_bulkpaymentfilesmock_companyIdx`;

ALTER TABLE `bulkpaymentrecord` 
DROP FOREIGN KEY `FK_bulkpaymentrecord_companyIdx`;

ALTER TABLE `bulkpaymentrecordmock` 
DROP FOREIGN KEY `FK_bulkpaymentrecordmock_companyIdx`;

ALTER TABLE `bulkpaymentrequest` 
DROP FOREIGN KEY `FK_bulkpaymentrequest_companyIdx`;

ALTER TABLE `bulkpaymentrequestpos` 
DROP FOREIGN KEY `FK_bulkpaymentrequestpos_companyIdx`;

ALTER TABLE `bulkpaymentsubrecord` 
DROP FOREIGN KEY `FK_bulkpaymentsubrecord_companyIdx`;

ALTER TABLE `bulkpaymentsubrecordmock` 
DROP FOREIGN KEY `FK_bulkpaymentsubrecordmock_companyIdx`;

ALTER TABLE `bulkpaymenttemplate` 
DROP FOREIGN KEY `FK_bulkpaymenttemplate_companyIdx`;

ALTER TABLE `bulkpaymenttemplatepos` 
DROP FOREIGN KEY `FK_bulkpaymenttemplatepos_companyIdx`;

ALTER TABLE `bulkwiretemplate` 
DROP FOREIGN KEY `FK2_bulkwiretemplate_CompanyID`;

DROP PROCEDURE IF EXISTS `authorizationCheckForRejectAndWithdrawl_proc`;

DELIMITER $$
CREATE PROCEDURE `authorizationCheckForRejectAndWithdrawl_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 100000000;
	
	SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
	IF @features is NULL THEN      
		SET @features = "";
	END IF;
	
	SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND approveFeatureAction is not NULL);
	
	IF @createActions is NULL THEN      
		SET @createActions = "";
	END IF;  
	
	SELECT * FROM bbrequest WHERE requestId = _requestId AND FIND_IN_SET(companyId, _companyId) AND FIND_IN_SET(featureActionId, @createActions);

END$$

DELIMITER ;


ALTER TABLE `approvalmatrix` ADD COLUMN `coreCustomerId` VARCHAR(50) NOT NULL AFTER `invalid`;

ALTER TABLE `approvalmatrix` 
DROP FOREIGN KEY `FK_approvalmatrix_Companyid`;
ALTER TABLE `approvalmatrix` 
DROP INDEX `FK_approvalmatrix_companyIdx` ;

ALTER TABLE `approvalmatrix` 
CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NOT NULL ;

ALTER TABLE `approvalmatrix` 
ADD INDEX `approvalmatrix_contractId_idx` (`contractId` ASC);

DROP procedure IF EXISTS `approvalmatrix_fetch_records_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalmatrix_fetch_records_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN 
    IF _cif = "" THEN
    SET _cif = "%";
    END IF;
    
    IF _accountId = "" THEN
    SET _accountId = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SELECT 
    	
      	`approvalMatrix`.`id`,
        `approvalMatrix`.`contractId`,
         `approvalMatrix`.`accountId`,
         `approvalMatrix`.`limitTypeId`,
         `featureAction`.`id` AS `actionId`,
         `featureAction`.`name` AS `actionName`,
         `featureAction`.`description` AS `actionDescription`,
         `featureAction`.`Feature_id` AS `featureId`,
         `feature`.`name` AS `featureName`,
         `feature`.`Status_id` AS `fifeaturestatus`,
         `approvalRule`.`id` AS `approvalruleId`,
         `approvalRule`.`numberOfApprovals`,
         `approvalRule`.`name` AS `approvalRuleName`,
         `approvalMatrix`.`lowerlimit`,
         `approvalMatrix`.`upperlimit`,
         `customer`.`id` AS `customerId`,
         `customer`.`FirstName` AS `firstName`,
         `customer`.`LastName` AS `lastName`,
         `contractcorecustomers`.`coreCustomerId` AS `cifId`,
         `contractcorecustomers`.`coreCustomerName` AS `cifName`,
         `approvalMatrix`.`invalid`
        FROM (((((((`approvalmatrix` AS `approvalMatrix`
        LEFT JOIN
        `customerapprovalmatrix` AS `customerApprovalMatrix`
        ON `approvalMatrix`.`id` = `customerApprovalMatrix`.`approvalMatrixId`)
        LEFT JOIN
        `customer` AS `customer`
        ON `customerApprovalMatrix`.`customerId` = `customer`.`id`)
        LEFT JOIN
        `featureaction` AS `featureAction`
        ON `approvalMatrix`.`actionId` = `featureAction`.`id`)
        LEFT JOIN
        `approvalrule` AS `approvalRule`
        ON `approvalMatrix`.`approvalruleId` = `approvalRule`.`id`) 
      LEFT JOIN
        `feature` AS `feature`
        ON `featureAction`.`Feature_id` = `feature`.`id`)
      LEFT JOIN
        `contractfeatures` AS `contractfeatures`
        ON `feature`.`id` = `contractfeatures`.`featureId`
          and  `approvalMatrix`.`contractId` = `contractfeatures`.`contractId`
          and `approvalMatrix`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
      LEFT JOIN
        `contractcorecustomers` AS `contractcorecustomers`
        ON `approvalMatrix`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalMatrix`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
    WHERE 
    `approvalMatrix`.`contractId` = `_contractId` AND
    `approvalMatrix`.`coreCustomerId` LIKE `_cif` AND
    `approvalMatrix`.`accountId` LIKE `_accountId` AND 
    FIND_IN_SET(`approvalMatrix`.`actionId`,`_actions`) > 0 AND 
    `approvalMatrix`.`limitTypeId` LIKE `_limitTypeId` AND 
    `approvalMatrix`.`softdeleteflag` = 0 AND
	`featureAction`.`status` = 'SID_ACTION_ACTIVE'
        ORDER BY `approvalMatrix`.`contractId`,`approvalMatrix`.`coreCustomerId`,`approvalMatrix`.`accountId`,`approvalMatrix`.`limitTypeId`,`approvalMatrix`.`actionId` ,`approvalMatrix`.`lowerlimit`;
END$$

DELIMITER ;

DROP procedure IF EXISTS `fetch_request_history_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_request_history_proc`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
	SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	IF @companyId is NULL THEN
	SET @companyId = "";
	END IF;
    
	SELECT 
		`bbactedrequest`.`approvalId` AS `approvalId`,
		`bbactedrequest`.`requestId` AS `requestId`,
		`bbactedrequest`.`companyId` AS `companyId`,
		`bbactedrequest`.`createdby` AS `requestActedby`,
		`bbactedrequest`.`status` AS `status`,
		`bbactedrequest`.`comments` AS `comments`,
		`bbactedrequest`.`action` AS `action`,
		`bbactedrequest`.`createdts` AS `actionts`,
		`bbactedrequest`.`softdeleteflag` AS `softdeleteflag`,
        `customer`.`UserName` AS `userName`,
        CASE 
			WHEN `bbactedrequest`.`createdby` IS null THEN "System"
			ELSE 
			concat_ws(
				" ",
				IF(LENGTH(`customer`.`FirstName`),`customer`.`FirstName`,NULL),
				IF(LENGTH(`customer`.`MiddleName`),`customer`.`MiddleName`,NULL),
				IF(LENGTH(`customer`.`LastName`),`customer`.`LastName`,NULL)
			) 
        END AS `customerName`,
        `customer`.`FullName` AS `customerFullName`
	FROM 
    ( `bbactedrequest`
    LEFT JOIN `customer` ON (`bbactedrequest`.`createdby` = `customer`.`id`))
    WHERE `bbactedrequest`.`requestId` = _requestId
    AND  FIND_IN_SET(`bbactedrequest`.`companyId`, @companyId);
 
END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_default_create_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalmatrix_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE accountList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 DECLARE accountIndex INTEGER DEFAULT 0;
 DECLARE actionIndex INTEGER DEFAULT 0;
set @numOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;
set @numOfActions = LENGTH(_actionIds) - LENGTH(REPLACE(_actionIds, ',', '')) + 1;
getAccount: LOOP
	set accountIndex = accountIndex + 1;
	IF accountIndex = @numOfAccounts + 1 THEN 
		LEAVE getAccount;
	Else
		set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(_accountIds, ',', accountIndex), ',', -1 );
		set actionIndex = 0; 
		 getAction: LOOP
            set actionIndex = actionIndex + 1;
            IF actionIndex = @numOfActions + 1 THEN
				LEAVE getAction;
			Else
				set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(_actionIds, ',', actionIndex), ',', -1 );				
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_1, "_", _contractId), @accountId, @actionId, limitTypeId_1,_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_2, "_", _contractId), @accountId,@actionId,limitTypeId_2,_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_3, "_", _contractId),@accountId, @actionId, limitTypeId_3,_cif,'NO_APPROVAL');
			END IF;
		 END LOOP getAction;
        set accountList = CONCAT(@accountId,",",accountList);
	END IF;
END LOOP getAccount;  

SET accountList = (select SUBSTRING(accountList FROM 1 FOR (CHAR_LENGTH(accountList)-1)));
select accountList;
END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_create_proc`;

DELIMITER $$

CREATE PROCEDURE `approvalmatrix_create_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _approverIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE index2 INTEGER DEFAULT 0;
	set @length = LENGTH(_matrixValues) - LENGTH(REPLACE(_matrixValues, ',', '')) + 1;
	getValues: LOOP
			set index1 = index1 + 1;
			IF index1 = @length + 1 THEN 
				LEAVE getValues;
			else
				set @matrixRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixValues, ',', index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;
				
				SET @id = LAST_INSERT_ID();
				set @customerIds = SUBSTRING_INDEX( SUBSTRING_INDEX(_approverIds, ',', index1), ',', -1 );
                IF @customerIds IS NOT NULL AND @customerIds != '' THEN
					set @customerIdsComma = REPLACE(@customerIds, ';', ',');
						set @length2 = LENGTH(@customerIdsComma) - LENGTH(REPLACE(@customerIdsComma, ',', '')) + 1;
						set index2 = 0;
						getCustomerIds: LOOP
							set index2 = index2 + 1;
							IF index2 = @length2 + 1 THEN 
								LEAVE getCustomerIds;
							else
								set @customerId = SUBSTRING_INDEX( SUBSTRING_INDEX(@customerIdsComma, ',', index2), ',', -1 );
								INSERT INTO customerapprovalmatrix(customerId,approvalMatrixId) values (@customerId,@id);							
								ITERATE getCustomerIds;
							END IF;
						END LOOP getCustomerIds;
					END IF;
				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END$$

DELIMITER ;

ALTER TABLE `customerrequest` DROP FOREIGN KEY `FK_CustomerRequest_AssignedTo`;

DROP procedure IF EXISTS `approvalmatrix_update_softdeleteflag_proc`;

DELIMITER $$

CREATE PROCEDURE `approvalmatrix_update_softdeleteflag_proc`(
	IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _accountIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 UPDATE approvalmatrix SET softdeleteflag = 1 WHERE contractId= _contractId AND 
													 coreCustomerId = _cif AND
													 FIND_IN_SET(accountId, _accountIds) AND 
													 actionId = _actionId AND 
													 limitTypeId = _limitTypeId COLLATE utf8_general_ci;
END$$

DELIMITER ;

DROP procedure IF EXISTS `fetch_bulkwire_files_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_files_proc`(
in createdBy varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in pageOffset int,
in pageSize int
)
BEGIN
    
    SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =createdby);
        IF @companyId is NULL THEN     
            SET @companyId = "";
        END IF;
		
    SET @companyId = (select concat(@companyId,",",Organization_Id) from customer where id =createdby);
	
	SET @isSMEUser = if(@companyId != "" OR @companyId != NULL , true, false);
    
    SET @filterRetail = concat("`bulkwirefiles`.`createdBy` = ", createdby , " AND `bulkwirefiles`.`softdeleteflag` = 0 ");
				
	SET @filterSME = concat("FIND_IN_SET(`bulkwirefiles`.`company_id`, '", @companyId  ,"')" " AND `bulkwirefiles`.`softdeleteflag` = 0");
    
    SET @getByIdFilter = if(@isSMEUser, @filterSME, @filterRetail);
    
    SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'createdts', sortByParam);
	SET sortByParam = if(sortByParam = 'username', 'firstName,lastname', sortByParam);
    SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
    
     SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));               
    
    SET @orderBy = concat(" ORDER BY ", sortByParam , " ", sortOrder);
    
    SET @paginationQuery = if((pageOffset != NULL OR pageOffset != "" AND pageSize != NULL OR pageSize != ""),
							concat(" LIMIT ",pageSize, " OFFSET " ,pageOffset),
                            ''); 
    
    
	SET @searchQuery = concat("(`bulkwirefiles`.`bulkWireFileName` LIKE ",searchString," OR `bulkwirefiles`.`noOfTransactions` LIKE ",
    searchString," OR `bulkwirefiles`.`noOfDomesticTransactions` LIKE ",searchString," OR `bulkwirefiles`.`noOfInternationalTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");
    
    SET @defaultFilter = concat(@getByIdFilter, @orderBy , @paginationQuery);
	SET @searchFilter = concat(@getByIdFilter," AND ",@searchQuery, @orderBy);
    SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

    
    SET @select_statement = concat("SELECT 
	    bulkwirefiles.bulkWireFileID,
		bulkwirefiles.bulkWireFileName, 
		bulkwirefiles.noOfTransactions, 
		bulkwirefiles.noOfDomesticTransactions,
		bulkwirefiles.noOfInternationalTransactions,
		bulkwirefiles.createdts,
		bulkwirefiles.lastmodifiedts,
		bulkwirefiles.lastExecutedOn,
        customer.id, 
		customer.FirstName as firstname,
		customer.LastName as lastname
     FROM
        (`bulkwirefiles`
		LEFT JOIN `customer` ON (`bulkwirefiles`.`createdBy` = `customer`.`id`))
		WHERE ", @filter);
    
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;

DROP procedure IF EXISTS `account_action_approvers_proc`;

DELIMITER $$
CREATE PROCEDURE `account_action_approvers_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountIds text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 1000000;

SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",") from (`customeraction`)
						where 
							`customeraction`.`isAllowed` = '0'
						and `customeraction`.`Action_id` = _approvalActionList
                        and FIND_IN_SET(`customeraction`.`Account_id`, _accountIds)
                        and `customeraction`.`contractId` = _contractId
                        and `customeraction`.`coreCustomerId` = _cif);

SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);
SET @NumberOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;

SET @customerIdListWithNoAccountAccess = (select group_concat(DISTINCT Customer_id SEPARATOR ",") 
										  from customeraccounts 
										  where FIND_IN_SET(Account_id, _accountIds) 
										  group by Customer_id having 
										  count(Account_id) != @NumberOfAccounts);
										  
SET @customerIdListWithNoAccountAccess = IF(@customerIdListWithNoAccountAccess is null, '', @customerIdListWithNoAccountAccess);
SELECT 
	DISTINCT (`customer`.`id` ) AS id , (`customer`.`username`) AS userName , (`membergroup`.`Name`) AS groupId,
										(`customer`.`FirstName`) AS firstName , (`customer`.`LastName`) AS lastName
from 
	(`customer`
LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and 
`contractcustomers`.`contractId` = _contractId and `contractcustomers`.`coreCustomerId` = _cif)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
LEFT JOIN `contractfeatures` ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
	where 
        `contractfeatures`.`contractId` = _contractId
        and `contractfeatures`.`coreCustomerId` = _cif
        and `contractfeatures`.`featureId` = _featureId
		and FIND_IN_SET(`customeraccounts`.`Account_id`,  _accountIds)
		and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
        and NOT FIND_IN_SET(`customer`.`id`,  @customerIdList)
		and NOT FIND_IN_SET(`customer`.`id`,  @customerIdListWithNoAccountAccess)
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0;      
END$$
DELIMITER ;

DROP procedure IF EXISTS `group_actions_remove_proc`;
DELIMITER $$
CREATE PROCEDURE `group_actions_remove_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customerIdValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
	set @numOfRecords = LENGTH(_customerIdValues) - LENGTH(REPLACE(_customerIdValues, ',', '')) + 1;
  deleteRecords : LOOP 
     set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE deleteRecords;
		else
		    set @customerValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_customerIdValues, ',', index1), ',', -1 );
			set @customerId = SUBSTRING_INDEX(@customerValues,'.',1);
			set @coreCustomerId = SUBSTRING_INDEX(@customerValues,'.',-1);
			delete from customeraction where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = _action;
		END IF;
		
	END LOOP deleteRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `group_limits_update_proc`;
DELIMITER $$
CREATE PROCEDURE `group_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2),
  IN _customerIdValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
	set @numOfRecords = LENGTH(_customerIdValues) - LENGTH(REPLACE(_customerIdValues, ',', '')) + 1;
  updateRecords : LOOP 
     set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE updateRecords;
		else
		    set @customerValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_customerIdValues, ',', index1), ',', -1 );
			set @customerId = SUBSTRING_INDEX(@customerValues,'.',1);
			set @coreCustomerId = SUBSTRING_INDEX(@customerValues,'.',-1);
			UPDATE customeraction SET value = _maxTxLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
			UPDATE customeraction SET value = _dailyLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
			UPDATE customeraction SET value = _weeklyLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
		END IF;
		
	END LOOP updateRecords;
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `feature_action_limits_update_proc`;
DELIMITER $$
CREATE PROCEDURE `feature_action_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _minTxLimit decimal(20,2),
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2)
)
BEGIN
  UPDATE actionlimit SET value = _minTxLimit where Action_id = _action AND LimitType_id = 'MIN_TRANSACTION_LIMIT'; 
  UPDATE actionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT'; 
  UPDATE actionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT';
  UPDATE actionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT';	
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `limits_update_proc`;
DELIMITER $$
CREATE PROCEDURE `limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2)
)
BEGIN
  UPDATE groupactionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE groupactionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE groupactionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit;
  UPDATE servicedefinitionactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE servicedefinitionactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE servicedefinitionactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
  UPDATE contractactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE contractactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE contractactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `servicedefinition_action_limits_update_proc`;
DELIMITER $$
CREATE PROCEDURE `servicedefinition_action_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2),
  IN _contractIdValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
  set @numOfRecords = LENGTH(_contractIdValues) - LENGTH(REPLACE(_contractIdValues, ',', '')) + 1;
   updateRecords : LOOP 
     set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE updateRecords;
  		else
		   set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(_contractIdValues, ',', index1), ',', -1 );
           UPDATE contractactionlimit SET value = _maxTxLimit where contractId = @contractId AND actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
           UPDATE contractactionlimit SET value = _dailyLimit where contractId = @contractId AND actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND value > _dailyLimit;
           UPDATE contractactionlimit SET value = _weeklyLimit where contractId = @contractId AND actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
           UPDATE customeraction SET value = _maxTxLimit where contractId = @contractId AND Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
           UPDATE customeraction SET value = _dailyLimit where contractId = @contractId AND Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
           UPDATE customeraction SET value = _weeklyLimit where contractId = @contractId AND Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
		   END IF;
	END LOOP updateRecords;
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `servicedefinition_remove_actions_proc`;
DELIMITER $$
CREATE PROCEDURE `servicedefinition_remove_actions_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _contractIdValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
  set @numOfRecords = LENGTH(_contractIdValues) - LENGTH(REPLACE(_contractIdValues, ',', '')) + 1;
   deleteRecords : LOOP 
     set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE deleteRecords;
  		else
		   set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(_contractIdValues, ',', index1), ',', -1 );
		   delete from contractactionlimit where contractId = @contractId AND actionId = _action;
		   delete from customeraction where contractId = @contractId AND Action_id = _action;
		   END IF;
	END LOOP deleteRecords;
END$$
DELIMITER;

ALTER TABLE `requestmessage` ADD COLUMN `frominternaluser` TINYINT(1) NOT NULL DEFAULT '0' AFTER `softdeleteflag`;

DROP procedure IF EXISTS `reports_messages_received`;

DELIMITER $$
CREATE PROCEDURE `reports_messages_received`(
  in from_date VARCHAR(19), 
  in to_date VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
  SELECT
    count(id) messages_received_count 
  FROM
    requestmessage 
  where
    frominternaluser <> '1'
    ";
if from_date != '' 
and from_date != '' then 
set 
  @queryStatement = concat(
    @queryStatement, 
    "
    and createdts >= ", 
    quote(STR_TO_DATE(from_date, '%m/%d/%Y %H:%i:%s')), 
    " and createdts <= ", 
    quote(STR_TO_DATE(to_date, '%m/%d/%Y %H:%i:%s'))
  );
end if;
if category_id != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
    and CustomerRequest_id in 
    (
      select
        id 
      from
        customerrequest 
      where
        RequestCategory_id = ", 
    quote(category_id), "
    )
    "
  );
end if;
if csr_name != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
    and RepliedBy_id = ", 
    quote(csr_name)
  );
end if;
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;


DROP procedure IF EXISTS `reports_messages_sent`;
DELIMITER $$
CREATE PROCEDURE `reports_messages_sent`(
  in from_date VARCHAR(19), 
  in to_date VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
    SELECT
      count(id) messages_sent_count 
    FROM
      requestmessage 
    where
      frominternaluser = '1'
      ";
if from_date != '' 
and from_date != '' then 
set 
  @queryStatement = concat(
    @queryStatement, 
    "
      and createdts >= ", 
    quote(STR_TO_DATE(from_date, '%m/%d/%Y %H:%i:%s')), 
    " and createdts <= ", 
    quote(STR_TO_DATE(to_date, '%m/%d/%Y %H:%i:%s'))
  );
end if;
if category_id != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
      and CustomerRequest_id in 
      (
        select
          id 
        from
          customerrequest 
        where
          RequestCategory_id = ", 
    quote(category_id), "
      )
      "
  );
end if;
if csr_name != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
      and RepliedBy_id = ", 
    quote(csr_name)
  );
end if;
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `internal_user_access_on_given_customer_proc`;

DELIMITER $$
CREATE PROCEDURE `internal_user_access_on_given_customer_proc`(
IN _roleIds varchar(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId  varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerUsername  varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

DECLARE isCustomerAccessible VARCHAR(6);
if(_customerId = '' and _customerUsername != '') THEN
	SET _customerId = (SELECT id from customer where UserName = _customerUsername);
END IF;

SET isCustomerAccessible = (_customerId in (SELECT distinct cg.Customer_id from userrolecustomerrole uc, customergroup cg 
where FIND_IN_SET(uc.UserRole_id, _roleIds) 
AND uc.CustomerRole_id=cg.Group_id));

IF ISNULL(isCustomerAccessible) or isCustomerAccessible = 0 THEN
	SET isCustomerAccessible = 'false';
ELSEIF(isCustomerAccessible = 1) THEN
	SET isCustomerAccessible = 'true';
END IF;

SELECT isCustomerAccessible;
END$$
DELIMITER ;

ALTER TABLE `customernote` DROP FOREIGN KEY `FK_CustomerNote_Createdby`;
ALTER TABLE `archivedcustomerrequest` DROP FOREIGN KEY `FK_ArchivedCustomerRequest_AssignedTo`;

ALTER TABLE `billpaytransfers` ADD COLUMN paidBy varchar(50) default NULL;
ALTER TABLE `billpaytransfers` ADD COLUMN swiftCode varchar(50) default NULL;
ALTER TABLE `ownaccounttransfers` ADD COLUMN paidBy varchar(50) default NULL;
ALTER TABLE `ownaccounttransfers` ADD COLUMN swiftCode varchar(50) default NULL;
ALTER TABLE `intrabanktransfers` ADD COLUMN paidBy varchar(50) default NULL;
ALTER TABLE `intrabanktransfers` ADD COLUMN swiftCode varchar(50) default NULL;
ALTER TABLE `p2ptransfers` ADD COLUMN paidBy varchar(50) default NULL;
ALTER TABLE `p2ptransfers` ADD COLUMN swiftCode varchar(50) default NULL;
ALTER TABLE `wiretransfers` ADD COLUMN paidBy varchar(50) default NULL;
ALTER TABLE `wiretransfers` ADD COLUMN swiftCode varchar(50) default NULL;

DROP VIEW IF EXISTS `customernotesfetch_view`;

CREATE VIEW `customernotesfetch_view` AS 
select `customernote`.`id` AS `id`,`customernote`.`Note` AS `Note`,`customernote`.`Customer_id` AS `Customer_id`,
`customer`.`FirstName` AS `Customer_FirstName`,`customer`.`MiddleName` AS `Customer_MiddleName`,
`customer`.`LastName` AS `Customer_LastName`,`customer`.`UserName` AS `Customer_Username`,
`customer`.`Status_id` AS `Customer_Status_id`,`customernote`.`createdby` AS `InternalUser_id`,
`customernote`.`createdts` AS `createdts`,
`customernote`.`synctimestamp` AS `synctimestamp`,`customernote`.`softdeleteflag` AS `softdeleteflag` 
from (`customernote` join `customer` on((`customernote`.`Customer_id` = `customer`.`id`)));

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
			set @limitTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',5), ',\"', -1 );
			set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, ',\"',-1 ), '\"', 1 );
            
			UPDATE customeraction SET value = @limitValue where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND Action_id = @actionId AND LimitType_id = @limitTypeId AND value > @limitValue; 
		END if;
		
	END LOOP updateRecords;
END$$

DELIMITER ;
;



ALTER TABLE `contract` 
ADD INDEX `FK_contract_servicedefinition_servicedefinitionId_idx` (`servicedefinitionId` ASC);
;
ALTER TABLE `contract` 
ADD CONSTRAINT `FK_contract_servicedefinition_servicedefinitionId`
  FOREIGN KEY (`servicedefinitionId`)
  REFERENCES `servicedefinition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

  ALTER TABLE `actionlimit` 
ADD INDEX `IDX_actionlimit_actionId_limitTypeId` (`Action_id` ASC, `LimitType_id` ASC);
  
  ALTER TABLE `servicedefinitionactionlimit` 
ADD INDEX `IDX_id_actionId` (`serviceDefinitionId` ASC, `actionId` ASC);

ALTER TABLE `servicedefinitionactionlimit` 
DROP INDEX `IDX_id_actionId` ,
ADD INDEX `IDX_serviceDefinition_serviceDefinitionId_actionId` (`serviceDefinitionId` ASC, `actionId` ASC);

ALTER TABLE `groupactionlimit` 
ADD INDEX `IDX_groupactionlimit_Groupid_actionid` (`Group_id` ASC, `Action_id` ASC);

ALTER TABLE `bulkpaymenttemplate` MODIFY totalAmount DECIMAL(19,2);
ALTER TABLE `bulkpaymenttemplatepos` MODIFY amount DECIMAL(19,2);
ALTER TABLE `bulkpaymentrequest` MODIFY totalAmount DECIMAL(19,2);
ALTER TABLE `bulkpaymentrequestpos` MODIFY amount DECIMAL(19,2); 
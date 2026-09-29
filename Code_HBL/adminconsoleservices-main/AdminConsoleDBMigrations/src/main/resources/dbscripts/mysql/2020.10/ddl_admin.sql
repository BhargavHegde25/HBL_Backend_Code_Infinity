DROP VIEW IF EXISTS `alerttype_view`;
CREATE VIEW `alerttype_view` AS
    SELECT 
        `dbxalerttype`.`id` AS `alerttype_id`,
        `dbxalerttype`.`Name` AS `alerttype_Name`,
        `dbxalerttype`.`AlertCategoryId` AS `alerttype_AlertCategoryId`,
        `dbxalerttype`.`Status_id` AS `alerttype_Status_id`,
        `dbxalerttype`.`IsGlobal` AS `alerttype_IsGlobal`,
        `dbxalerttype`.`DisplaySequence` AS `alerttype_DisplaySequence`,
		`dbxalerttype`.`defaultFrequencyId` as `alerttype_freqId`,
        `dbxalerttype`.`defaultFrequencyValue` as `alerttype_freqValue`,
        `dbxalerttype`.`defaultFrequencyTime` as `alerttype_freqTime`,
        `dbxalerttype`.`isAccountLevel` as `alerttype_isAccountLevel`,
        (SELECT 
                COUNT(`alertsubtype`.`id`)
            FROM
                `alertsubtype`
            WHERE
                (`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)) AS `Alerts_count`,
        `dbxalerttype`.`softdeleteflag` AS `alerttype_softdeleteflag`,
        `dbxalerttypetext`.`LanguageCode` AS `alerttypetext_LanguageCode`,
        `dbxalerttypetext`.`DisplayName` AS `alerttypetext_DisplayName`,
        `dbxalerttypetext`.`Description` AS `alerttypetext_Description`,
        `dbxalerttypetext`.`createdby` AS `alerttypetext_createdby`,
        `dbxalerttypetext`.`modifiedby` AS `alerttypetext_modifiedby`,
        `dbxalerttypetext`.`createdts` AS `alerttypetext_createdts`,
        `dbxalerttypetext`.`lastmodifiedts` AS `alerttypetext_lastmodifiedts`,
        `dbxalerttypetext`.`synctimestamp` AS `alerttypetext_synctimestamp`,
        `dbxalerttypetext`.`softdeleteflag` AS `alerttypetext_softdeleteflag`
    FROM
        (`dbxalerttypetext`
        JOIN `dbxalerttype` ON ((`dbxalerttypetext`.`AlertTypeId` = `dbxalerttype`.`id`)));

DROP VIEW IF EXISTS `alertcategory_view`;                
CREATE VIEW `alertcategory_view` AS
    SELECT 
        `dbxalertcategory`.`id` AS `alertcategory_id`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `dbxalertcategory`.`accountLevel` AS `alertcategory_accountLevel`,
        `dbxalertcategory`.`DisplaySequence` AS `alertcategory_DisplaySequence`,
        `dbxalertcategory`.`softdeleteflag` AS `alertcategory_softdeleteflag`,
        `dbxalertcategory`.`Name` AS `alertcategory_Name`,
        `dbxalertcategory`.`defaultFrequencyId` AS `alertcategory_freqId`,
        `dbxalertcategory`.`defaultFrequencyValue` AS `alertcategory_freqValue`,
        `dbxalertcategory`.`defaultFrequencyTime` AS `alertcategory_freqTime`,
        (SELECT 
                COUNT(`dbxalerttype`.`id`)
            FROM
                `dbxalerttype`
            WHERE
                (`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)) AS `Groups_count`,
        (SELECT 
                SUM(`alerttype_view`.`Alerts_count`)
            FROM
                `alerttype_view`
            WHERE
                (`alerttype_view`.`alerttype_AlertCategoryId` = `dbxalertcategory`.`id`)) AS `Alerts_count`,
        `dbxalertcategorytext`.`LanguageCode` AS `alertcategorytext_LanguageCode`,
        `dbxalertcategorytext`.`DisplayName` AS `alertcategorytext_DisplayName`,
        `dbxalertcategorytext`.`Description` AS `alertcategorytext_Description`,
        `dbxalertcategorytext`.`createdby` AS `alertcategorytext_createdby`,
        `dbxalertcategorytext`.`modifiedby` AS `alertcategorytext_modifiedby`,
        `dbxalertcategorytext`.`createdts` AS `alertcategorytext_createdts`,
        `dbxalertcategorytext`.`lastmodifiedts` AS `alertcategorytext_lastmodifiedts`,
        `dbxalertcategorytext`.`synctimestamp` AS `alertcategorytext_synctimestamp`,
        `dbxalertcategorytext`.`softdeleteflag` AS `alertcategorytext_softdeleteflag`
    FROM
        (`dbxalertcategorytext`
        JOIN `dbxalertcategory` ON ((`dbxalertcategorytext`.`AlertCategoryId` = `dbxalertcategory`.`id`)));
		
DROP VIEW IF EXISTS `alertsubtypetext_view`;		
CREATE VIEW `alertsubtypetext_view` as select
    `alertsubtype`.`id` as `alertsubtype_id`,
    `alertsubtype`.`AlertTypeId` as `alertsubtype_alertTypeId`,
    `alertsubtype`.`Name` as `alertsubtype_Name`,
    `alertsubtype`.`Status_id` as `alertsubtype_StatusId`,
    `alertsubtype`.`isAccountLevel` as `alertsubtype_isAccountLevel`,
    `alertsubtype`.`attributeId` as `alertsubtype_attributeId`,
    `alertsubtype`.`alertConditionId` as `alertsubtype_alertConditionId`,
    `alertsubtype`.`value1` as `alertsubtype_value1`,
    `alertsubtype`.`value2` as `alertsubtype_value2`,
    `alertsubtype`.`isGlobal` as `alertsubtype_isGlobal`,
    `alertsubtype`.`defaultFrequencyId` as `alertsubtype_defaultFrequencyId`,
    `alertsubtype`.`defaultFrequencyValue` as `alertsubtype_defaultFrequencyValue`,
    `alertsubtype`.`defaultFrequencyTime` as `alertsubtype_defaultFrequencyTime`,
    `alertsubtype`.`createdby` as `alertsubtype_createdby`,
    `alertsubtype`.`modifiedby` as `alertsubtype_modifiedby`,
    `alertsubtype`.`createdts` as `alertsubtype_createdts`,
    `alertsubtype`.`lastmodifiedts` as `alertsubtype_lastmodifiedts`,
    `alertsubtype`.`synctimestamp` as `alertsubtype_synctimestamp`,
    `alertsubtype`.`softdeleteflag` as `alertsubtype_softdeleteflag`,
    `alertsubtypetext`.`languageCode` as `alertsubtypetext_languageCode`,
    `alertsubtypetext`.`description` as `alertsubtypetext_description`,
    `alertsubtypetext`.`displayName` as `alertsubtypetext_displayName`
from
    (`alertsubtype`
join `alertsubtypetext` on
    ((`alertsubtypetext`.`alertSubTypeId` = `alertsubtype`.`id`)));

		
CREATE TABLE `rrole` (
  `id` VARCHAR(50) NOT NULL,
  `createdts`TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
ALTER TABLE `featureaction` 
ADD COLUMN `Rrole_id` VARCHAR(50) NULL AFTER `Type_id`,
ADD INDEX `FK_action_rrole_id_idx` (`Rrole_id` ASC);

ALTER TABLE `featureaction` 
ADD CONSTRAINT `FK_featureaction_rrole`
  FOREIGN KEY (`Rrole_id`)
  REFERENCES `rrole` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
DROP TABLE IF EXISTS `customview`;
CREATE TABLE `customview` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(300) NULL,
  `customerId` VARCHAR(50) NULL DEFAULT NULL,
  `accountIds` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

  ALTER TABLE `media` 
CHANGE COLUMN `Content` `Content` MEDIUMBLOB NULL DEFAULT NULL ;

  ALTER TABLE `archivedmedia` 
CHANGE COLUMN `Content` `Content` MEDIUMBLOB NULL DEFAULT NULL ;



CREATE TABLE `datasourcetype` (
  `id` VARCHAR(20) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
  

CREATE TABLE `datasource` (
  `id` VARCHAR(20) NOT NULL,
  `dataSourceTypeId` VARCHAR(20) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `schema` VARCHAR(100) NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_datasource_datasourcetype`
    FOREIGN KEY (`dataSourceTypeId`)
    REFERENCES `datasourcetype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `reportdatasource` (
  `id` VARCHAR(36) NOT NULL,
  `dataSourceId` VARCHAR(20) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `createdby` VARCHAR(50) NULL,
  `modifiedby` VARCHAR(50) NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_reportdatasource_datasource`
    FOREIGN KEY (`dataSourceId`)
    REFERENCES `datasource` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    
CREATE TABLE `report` (
  `id` VARCHAR(36) NOT NULL,
  `reportDataSourceId` VARCHAR(36) NOT NULL,
  `name` VARCHAR(100)  NOT NULL,
  `description` VARCHAR(255) NULL,
  `createdby` VARCHAR(50) NULL,
  `modifiedby` VARCHAR(50)  NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_report_reportdatasource`
    FOREIGN KEY (`reportDataSourceId`)
    REFERENCES `reportdatasource` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)ENGINE=InnoDB DEFAULT CHARSET=utf8;
    

CREATE TABLE `sharedreport` (
  `reportId` VARCHAR(36) NOT NULL,
  `userId` VARCHAR(50) NOT NULL,
  `roleId` VARCHAR(50) NOT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`reportId`, `userId`, `roleId`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	
CREATE TABLE `p2ppayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45)  DEFAULT NULL,
  `payeeId` varchar(50)  NOT NULL,
  `customerId` varchar(50)  NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

  
  
  CREATE TABLE `customeractionlimits` (
  `id` varchar(50) NOT NULL,
  `RoleType_id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `Action_id` varchar(255) NOT NULL,
  `Account_id` varchar(50) DEFAULT NULL,
  `isAllowed` tinyint(1) NOT NULL,
  `LimitType_id` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_customeractionlimits` (`RoleType_id`,`Customer_id`,`Action_id`,`Account_id`,`LimitType_id`),
  KEY `IXFK_CustomerActionLimits_Customer` (`Customer_id`),
  KEY `IXFK_CustomerActionLimits_Service` (`Action_id`),
  KEY `FK_CustomerActionLimits_RoleType_idx` (`RoleType_id`),
  KEY `FK_CustomerActionLimits_limitsubtype_idx` (`LimitType_id`),
  CONSTRAINT `FK_CustomerActionLimits_Action` FOREIGN KEY (`Action_id`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimits_Customer` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimits_customertype` FOREIGN KEY (`RoleType_id`) REFERENCES `membergrouptype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimits_limittype` FOREIGN KEY (`LimitType_id`) REFERENCES `limittype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP procedure IF EXISTS `customeractionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `customeractionlimits_create_proc`(
IN _customerActionsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _businessTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT ""  ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE accountId varchar(255) DEFAULT "" ;
 DECLARE actualLimitId varchar(255) DEFAULT "" ;

DECLARE accounts CURSOR
         FOR (SELECT Account_id FROM accounts WHERE FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList));
DECLARE limits CURSOR 
		FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

IF(ISNULL(_groupId) OR _groupId='' ) THEN
SET @groupId = (SELECT Group_id FROM groupbusinesstype WHERE BusinessType_id COLLATE utf8_general_ci = _businessTypeId 
                       AND isDefaultGroup = true);
ELSE
SET @groupId = _groupId ;
END IF;
 
SET @groupId = (SELECT id FROM membergroup WHERE id = @groupId AND Type_id = 'TYPE_ID_BUSINESS' 
                       AND Status_id = 'SID_ACTIVE' );
                       
SET @validActionsList = (SELECT group_concat(distinct Action_id SEPARATOR ",") FROM groupactionlimit WHERE Group_id COLLATE utf8_general_ci =@groupId COLLATE utf8_general_ci 
                       AND (FIND_IN_SET(Action_id,_customerActionsCSV)));
                       
OPEN accounts; 
getAccount : LOOP
FETCH accounts INTO accountId;
IF finished = 1 THEN 
	LEAVE getAccount;
ELSE
  OPEN actions; 
  getAction: LOOP
        SET entryStatus = 0;
        FETCH actions INTO featureActionId;
        IF finished = 1 THEN 
            LEAVE getAction;
	    else
            OPEN limits; 
			getlimit: LOOP
            FETCH limits INTO limitId;
			IF finished = 1 THEN 
               LEAVE getlimit;
			else
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id COLLATE utf8_general_ci = featureActionId  
               AND LimitType_id COLLATE utf8_general_ci = limitId );
               
			   if (limitId='MAX_TRANSACTION_LIMIT') THEN 
			   SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
			   ELSEIF (limitId='MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeractionlimits(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,0.00);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeractionlimits(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,0.00);
               SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
               
               
               
			   SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeractionlimits(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,@limitvalue);
                SET entryStatus = 1;
			   ITERATE  getlimit;
			END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
			      INSERT IGNORE INTO customeractionlimits(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true);
            END IF;
			set actionslist = CONCAT(featureActionId,",",actionslist);
			ITERATE  getAction;
        END IF;
  END LOOP getAction;
  CLOSE actions;
  ITERATE  getAccount;
 END IF;
END LOOP getAccount;
CLOSE accounts;

SET actionslist = (select SUBSTRING(actionslist FROM 1 FOR (CHAR_LENGTH(actionslist)-1)));

select actionslist;
END$$

DELIMITER ;

DROP procedure IF EXISTS `customer_action_limits_delete`;

DELIMITER $$
CREATE PROCEDURE `customer_action_limits_delete`(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM customeractionlimits where customeractionlimits.Customer_id = _customerId;
END$$

DELIMITER ;

DROP procedure IF EXISTS `customer_action_limits_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_action_limits_proc`(
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

SET @business_customer_enabled_actions = (SELECT group_concat(Action_id SEPARATOR ",") from customeractionlimits where isAllowed = '1' AND Customer_id =_customerId AND FIND_IN_SET(Action_id, @organization_active_actions));

IF @business_customer_enabled_actions is null THEN 
  	SET @business_customer_enabled_actions = "";
END IF;

SET @customer_disabled_actions = (SELECT group_concat(Action_id SEPARATOR ",") from customeractionlimits where isAllowed = '0' AND Account_id is NULL AND Customer_id =_customerId);

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
        `customeractionlimits`.`Customer_id` AS `Customer_id`,
        `customeractionlimits`.`Account_id` AS `Account_id`,
        IF(`customeractionlimits`.`isAllowed` = '1', 'true', 'false') AS `isAllowed`,
        `featureaction`.`id` AS `Action_id`,
        IF(`featureaction`.`isAccountLevel` = '1', 'true', 'false') AS `isAccountLevel`,
        `feature`.`Status_id` AS `Feature_Status_id`,
        `feature`.`id` AS `Feature_id`,
        `customeractionlimits`.`RoleType_id` AS `RoleType_id`,
    	`customeractionlimits`.`LimitType_id` AS `LimitType_id`,
        `customeractionlimits`.`value` AS `value`
    FROM
        (`customeractionlimits`
    	LEFT JOIN `featureaction` ON (`featureaction`.`id` = `customeractionlimits`.`Action_id`)
    	LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
  	where `customeractionlimits`.`Customer_id` = ", quote(_customerId)," and ",@active_feature_condition);
    
    IF(_actionId != '') THEN
    set @select_statement =  concat(@select_statement ," and `customeractionlimits`.`Action_id` = ",quote(_actionId));
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
    	and ", @active_feature_condition );
    
    IF(_actionId != '') THEN
    	set @select_statement =  concat(@select_statement ," and `groupactionlimit`.`Action_id` = ",quote(_actionId));
  	END IF;
    
    -- select @select_statement;
  	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;


DROP TABLE IF EXISTS `bulkpaymentfiles`;
CREATE TABLE `bulkpaymentfiles` (
  `fileId` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `fileName` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `content` text NOT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `uploadedBy` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `uploadedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `totalAmount` varchar(50) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `requestId` varchar(50) DEFAULT NULL,
  `fileSize` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`fileId`),
  KEY `FK_bulkpaymentfiles_requestId` (`requestId`),
  KEY `FK_bulkpaymentfiles_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymentfiles_uploadedDate` (`uploadedDate`),
  KEY `FK_bulkpaymentfiles_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentfiles_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentfiles_companyId` (`companyId`),
  KEY `FK_bulkpaymentfiles_uploadedBy` (`uploadedBy`),
  KEY `FK_bulkpaymentfiles_status` (`status`),
  KEY `FK_bulkpaymentfiles_roleId` (`roleId`),
  CONSTRAINT `FK_bulkpaymentfiles_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentfiles_uploadedBy_idx` FOREIGN KEY (`uploadedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentfiles_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentfiles_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentrecord`;
CREATE TABLE `bulkpaymentrecord` (
  `recordId` varchar(50) NOT NULL,
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
  `fromAccount` varchar(50) DEFAULT NULL,
  `totalAmount` varchar(50) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `requestId` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`recordId`),
  KEY `FK_bulkpaymentrecord_requestId` (`requestId`),
  KEY `FK_bulkpaymentrecord_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymentrecord_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentrecord_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentrecord_companyId` (`companyId`),
  KEY `FK_bulkpaymentrecord_reviewedBy` (`reviewedBy`),
  KEY `FK_bulkpaymentrecord_initiatedBy` (`initiatedBy`),
  KEY `FK_bulkpaymentrecord_status` (`status`),
  KEY `FK_bulkpaymentrecord_roleId` (`roleId`),
  KEY `FK_bulkpaymentrecord_fileId` (`fileId`),
  CONSTRAINT `FK_bulkpaymentrecord_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrecord_reviewedBy_idx` FOREIGN KEY (`reviewedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentrecord_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrecord_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentsubrecord`;
CREATE TABLE `bulkpaymentsubrecord` (
  `paymentOrderId` varchar(50) NOT NULL,
  `recordId` varchar(50) DEFAULT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `recipientName` varchar(50) NOT NULL,
  `acountNumber` varchar(50) NOT NULL,
  `bankName` varchar(50) DEFAULT NULL,
  `swift` varchar(50) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
 
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  
  `status` varchar(20) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `amount` varchar(20) DEFAULT NULL,
  `feesPaidBy` varchar(50) DEFAULT NULL,
  `paymentReference` varchar(50) DEFAULT NULL,
 
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',

  PRIMARY KEY (`paymentOrderId`),
  KEY `FK_bulkpaymentsubrecord_acountNumber` (`acountNumber`),
  KEY `FK_bulkpaymentsubrecord_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentsubrecord_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentsubrecord_companyId` (`companyId`),
  KEY `FK_bulkpaymentsubrecord_createdby` (`createdby`),
  KEY `FK_bulkpaymentsubrecord_recipientName` (`recipientName`),
  KEY `FK_bulkpaymentsubrecord_status` (`status`),
  KEY `FK_bulkpaymentsubrecord_roleId` (`roleId`),
  KEY `FK_bulkpaymentsubrecord_recordId` (`recordId`),
  CONSTRAINT `FK_bulkpaymentsubrecord_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentsubrecord_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentsubrecord_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentsubrecord_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentfilesmock`;
CREATE TABLE `bulkpaymentfilesmock` (
  `fileId` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `fileName` varchar(50) NOT NULL,
  `description` varchar(250) DEFAULT NULL,
  `content` text NOT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `uploadedBy` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  `uploadedDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `status` varchar(20) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `totalAmount` varchar(50) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `requestId` varchar(50) DEFAULT NULL,
  `fileSize` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`fileId`),
  KEY `FK_bulkpaymentfilesmock_requestId` (`requestId`),
  KEY `FK_bulkpaymentfilesmock_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymentfilesmock_uploadedDate` (`uploadedDate`),
  KEY `FK_bulkpaymentfilesmock_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentfilesmock_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentfilesmock_companyId` (`companyId`),
  KEY `FK_bulkpaymentfilesmock_uploadedBy` (`uploadedBy`),
  KEY `FK_bulkpaymentfilesmock_status` (`status`),
  KEY `FK_bulkpaymentfilesmock_roleId` (`roleId`),
  CONSTRAINT `FK_bulkpaymentfilesmock_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentfilesmock_uploadedBy_idx` FOREIGN KEY (`uploadedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentfilesmock_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentfilesmock_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentrecordmock`;
CREATE TABLE `bulkpaymentrecordmock` (
  `recordId` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `paymentId` varchar(50) DEFAULT NULL,
  `description` varchar(250) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
  `paymentDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `scheduledDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fileId` varchar(50) NULL,

  `reviewedBy` varchar(50) DEFAULT NULL,
  `initiatedBy` varchar(50) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  
  `status` varchar(20) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `totalAmount` varchar(50) DEFAULT NULL,
  `totalTransactions` bigint(20) DEFAULT NULL,
  `requestId` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`recordId`),
  KEY `FK_bulkpaymentrecordmock_requestId` (`requestId`),
  KEY `FK_bulkpaymentrecordmock_fromAccount` (`fromAccount`),
  KEY `FK_bulkpaymentrecordmock_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentrecordmock_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentrecordmock_companyId` (`companyId`),
  KEY `FK_bulkpaymentrecordmock_reviewedBy` (`reviewedBy`),
  KEY `FK_bulkpaymentrecordmock_initiatedBy` (`initiatedBy`),
  KEY `FK_bulkpaymentrecordmock_status` (`status`),
  KEY `FK_bulkpaymentrecordmock_roleId` (`roleId`),
  KEY `FK_bulkpaymentrecordmock_fileId` (`fileId`),
  CONSTRAINT `FK_bulkpaymentrecordmock_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrecordmock_reviewedBy_idx` FOREIGN KEY (`reviewedBy`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentrecordmock_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentrecordmock_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentsubrecordmock`;
CREATE TABLE `bulkpaymentsubrecordmock` (
  `paymentOrderId` varchar(50) NOT NULL,
  `recordId` varchar(50) NOT NULL,
  `confirmationNumber` varchar(50) DEFAULT NULL,
  `recipientName` varchar(50) NOT NULL,
  `acountNumber` varchar(50) NOT NULL,
  `bankName` varchar(50) DEFAULT NULL,
  `swift` varchar(50) DEFAULT NULL,
  `featureActionId` varchar(50) DEFAULT NULL,
 
  `companyId` varchar(50) DEFAULT NULL,
  `roleId` varchar(45) DEFAULT NULL,
  
  `status` varchar(20) DEFAULT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `amount` varchar(20) DEFAULT NULL,
  `feesPaidBy` varchar(50) DEFAULT NULL,
  `paymentReference` varchar(50) DEFAULT NULL,
 
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',

  PRIMARY KEY (`paymentOrderId`),
  KEY `FK_bulkpaymentsubrecordmock_acountNumber` (`acountNumber`),
  KEY `FK_bulkpaymentsubrecordmock_confirmationNumber` (`confirmationNumber`),
  KEY `FK_bulkpaymentsubrecordmock_featureActionId` (`featureActionId`),
  KEY `FK_bulkpaymentsubrecordmock_companyId` (`companyId`),
  KEY `FK_bulkpaymentsubrecordmock_createdby` (`createdby`),
  KEY `FK_bulkpaymentsubrecordmock_recipientName` (`recipientName`),
  KEY `FK_bulkpaymentsubrecordmock_status` (`status`),
  KEY `FK_bulkpaymentsubrecordmock_roleId` (`roleId`),
  KEY `FK_bulkpaymentsubrecordmock_recordId` (`recordId`),
  CONSTRAINT `FK_bulkpaymentsubrecordmock_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentsubrecordmock_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_bulkpaymentsubrecordmock_featureActionId_idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkpaymentsubrecordmock_roleId_idx` FOREIGN KEY (`roleId`) REFERENCES `membergroup` (`id`) ON DELETE SET NULL ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkpaymentsamplefiles`;
CREATE TABLE `bulkpaymentsamplefiles` (
  `fileId` varchar(50) NOT NULL,
  `fileName` varchar(50) NOT NULL,
  `description` varchar(100) DEFAULT NULL,
  `content` text NOT NULL,
  PRIMARY KEY (`fileId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP procedure IF EXISTS `get_reports_proc`;

DELIMITER $$
CREATE PROCEDURE `get_reports_proc`(
 IN p_userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN p_roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci  
)
BEGIN

select * from report rp where rp.createdby=p_userId
UNION
SELECT rp.* from report rp,sharedreport sr
where sr.reportId=rp.id
and sr.userid=p_userId
union
SELECT rp.* from report rp,sharedreport sr
where sr.reportId=rp.id
and sr.roleId=p_roleId order by createdts desc;

END$$

DELIMITER ;

DROP procedure IF EXISTS `customer_alert_preferences_reset_proc`;
DELIMITER $$
CREATE PROCEDURE `customer_alert_preferences_reset_proc`()
BEGIN
 truncate table dbxcustomeralertentitlement;
 truncate table customeralertchannel;
 truncate table customeralertfrequency;
 truncate table customeralertswitch;
END$$
DELIMITER ;
ALTER TABLE `report` 
ADD COLUMN `externalId` VARCHAR(100) NOT NULL AFTER `id`;

DROP VIEW IF EXISTS `bulkpaymentrecord_view`;
CREATE VIEW `bulkpaymentrecord_view` AS
    SELECT 
        `bulkpaymentrecord`.`featureActionId` AS `featureActionId`,
        `bulkpaymentrecord`.`companyId` AS `companyId`,
        `bulkpaymentrecord`.`createdby` AS `createdby`,
        `bulkpaymentrecord`.`createdts` AS `createdts`,
        `bulkpaymentrecord`.`status` AS `status`,
        `bulkpaymentrecord`.`roleId` AS `roleId`,
        `bulkpaymentrecord`.`paymentDate` AS `scheduledDate`,
        `bulkpaymentrecord`.`totalAmount` AS `amount`,
        `bulkpaymentrecord`.`fromAccount` AS `fromAccountNumber`
    FROM
        `bulkpaymentrecord` ;
        
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
       
        SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
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
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE companyId = @companyId AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));
        
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
END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalrequest_counts_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalrequest_counts_proc`(
	in _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _approveActionList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _createActionList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
	)
MAINLABEL:BEGIN

		SET SESSION group_concat_max_len = 100000000;
		
	    SELECT 0 as count, 'ACHTransactionsForMyApproval' as TransactionType
	    UNION 
	    SELECT 0 as count, 'ACHFilesForMyApproval' as TransactionType
	    UNION
        SELECT 0 as count, 'GeneralTransactionsForMyApproval' as TransactionType
	    UNION
	    SELECT 0 as count, 'myRequestsWaiting' as TransactionType
	    UNION 
	    SELECT 0 as count, 'myRequestsRejected' as TransactionType
	    UNION 
	    SELECT 0 as count, 'myRequestsApproved' as TransactionType;
	            
	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
        
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _approveActionList is NULL THEN      
			SET _approveActionList = "";
		END IF;
        
        IF _createActionList is NULL THEN      
			SET _createActionList = "";
		END IF;
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_approveActionList) > 0);
        
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createApproveActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND (id LIKE "%_CREATE" OR id LIKE "%_UPLOAD"));
        
        IF @createApproveActions is NULL THEN      
			SET @createApproveActions = "";
		END IF;
	    
	    SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix where customerId = _customerId);
	    
	    IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
		
		SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest where createdby = _customerId AND action = 'Approved');
        
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM requestapprovalmatrix WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds));
	    
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "";
		END IF;
        
	    set @select_statement = concat("select count(requestId) as count,
								TransactionType from (
									(select 
										DISTINCT(bbrequest.requestId),
										if(bbrequest.featureActionId like 'ACH_FILE%', 'ACHFilesForMyApproval' ,if(bbrequest.featureActionId like 'ACH%','ACHTransactionsForMyApproval', 'GeneralTransactionsForMyApproval')) as TransactionType,
										bbrequest.createdby,
										bbrequest.companyId,
										bbrequest.status
									FROM 
										(`bbrequest` 
										LEFT JOIN `requestapprovalmatrix` ON (bbrequest.requestId = requestapprovalmatrix.requestId ))
									WHERE FIND_IN_SET(bbrequest.requestId, \"", @approvalRequestIds,"\") AND bbrequest.companyId = ", @companyId
	                                ," AND 
	                                FIND_IN_SET(bbrequest.featureActionId, \"", @createApproveActions, "\") AND
	                                bbrequest.status = 'Pending') as tablea) 
	                                group BY TransactionType"); 
	                                
		set @select_statement = concat(@select_statement, " UNION select count(requestId) as count,
								TransactionType from (
									(select 
										DISTINCT(bbrequest.requestId),
										if(bbrequest.status = 'Pending', 'myRequestsWaiting', if(bbrequest.status = 'Rejected', 'myRequestsRejected', if(bbrequest.status = 'Approved', 'myRequestsApproved', 'myRequestsWithdrawn'))) as TransactionType,
										bbrequest.createdby,
										bbrequest.companyId,
										bbrequest.status
									FROM 
										`bbrequest` WHERE bbrequest.companyId = ", @companyId," AND 
	                                    FIND_IN_SET(bbrequest.featureActionId, \"", _createActionList, "\") AND
	                                    bbrequest.createdby = ", quote(_customerId),") as tableb) 
	                                group BY TransactionType");
		
	    IF @select_statement is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
	    -- select @select_statement;
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	
END$$

DELIMITER ;

ALTER TABLE `bulkpaymentsubrecordmock` CHANGE  `acountNumber` `accountNumber` varchar(50)  NULL;
ALTER TABLE `bulkpaymentsubrecord` CHANGE  `acountNumber` `accountNumber` varchar(50)  NULL;

ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN `paymentStatus` VARCHAR(50)  NULL AFTER `requestId`;
ALTER TABLE `bulkpaymentrecord` ADD COLUMN `paymentStatus` VARCHAR(50)  NULL AFTER `requestId`;

ALTER TABLE `bulkpaymentfilesmock` ADD COLUMN `sysGeneratedFileName` varchar(50) NULL;


ALTER TABLE `bulkpaymentsubrecord` ADD COLUMN `paymentMethod` VARCHAR(50)  NULL AFTER `paymentReference`;
ALTER TABLE `bulkpaymentsubrecordmock` ADD COLUMN `paymentMethod` VARCHAR(50)  NULL AFTER `paymentReference`;
ALTER TABLE `bulkpaymentsubrecord` ADD COLUMN `accType` VARCHAR(50)  NULL AFTER `paymentMethod`;
ALTER TABLE `bulkpaymentsubrecordmock` ADD COLUMN `accType` VARCHAR(50)  NULL AFTER `paymentMethod`;

DROP PROCEDURE IF EXISTS `fetch_approvers_proc`;
DELIMITER $$
CREATE PROCEDURE fetch_approvers_proc
(in _requestId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)

BEGIN

	SELECT customerapprovalmatrix.customerId as approver 
    FROM customerapprovalmatrix  WHERE 
    customerapprovalmatrix.approvalMatrixId 
    IN (
		SELECT requestapprovalmatrix.approvalMatrixId 
        FROM requestapprovalmatrix WHERE 
        requestapprovalmatrix.requestId = _requestId
	);
END $$

DELIMITER ;


ALTER TABLE `bulkpaymentrecord` ADD COLUMN `currency` VARCHAR(50)  NULL;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN `currency` VARCHAR(50)  NULL;


DROP procedure IF EXISTS `authorizationCheckForRejectAndWithdrawl_proc`;

DELIMITER $$
CREATE PROCEDURE `authorizationCheckForRejectAndWithdrawl_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 100000000;
	
	SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
	IF @features is NULL THEN      
		SET @features = "";
	END IF;
	
	SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND Type_id = "MONETARY");
	
	IF @createActions is NULL THEN      
		SET @createActions = "";
	END IF;  
	
	SELECT * FROM bbrequest WHERE requestId = _requestId AND companyId = _companyId AND FIND_IN_SET(featureActionId, @createActions);

END$$

DELIMITER ;

DROP procedure IF EXISTS `auto_reject_invalid_pending_requests_proc`;

DELIMITER $$
CREATE PROCEDURE `auto_reject_invalid_pending_requests_proc`( 
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
	
		SET SESSION group_concat_max_len = 100000000;

        SET @oldMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix where customerId = _customerId);
        
        IF @oldMatrixIds is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @newMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") 
								FROM customerapprovalmatrix 
                                LEFT JOIN
								`approvalmatrix` ON (`customerapprovalmatrix`.`approvalMatrixId` = `approvalmatrix`.`id`)
                                INNER JOIN
									(SELECT DISTINCT Account_id,
                                    featureaction.Feature_id as featureId,
									(select id from featureaction where featureaction.Feature_id = featureId and featureaction.Type_id = 'MONETARY') as Action_id
									FROM 
									customeraction 
                                    INNER JOIN featureaction ON (customeraction.Action_id = featureaction.id)
									WHERE Customer_id = _customerId
									AND isAllowed = '1' 
									AND Account_id is NOT null
									AND Action_id like '%_APPROVE') as 
								`can` ON ((`approvalmatrix`.`actionId` = `can`.`Action_id`) 
											AND (`approvalmatrix`.`accountId` = `can`.`Account_id`))
								where customerId = _customerId);
		
        IF @newMatrixIds is NULL THEN      
			SET @newMatrixIds = "";
		END IF;
         
		SET @invalidMatrixIds = ( SELECT GROUP_CONCAT(id SEPARATOR ",") 
									FROM `approvalmatrix` 
										WHERE FIND_IN_SET(`approvalmatrix`.`id`, @oldMatrixIds)
                                        AND NOT FIND_IN_SET(`approvalmatrix`.`id`, @newMatrixIds)
                                        );
								
        IF @invalidMatrixIds is NULL THEN 
			LEAVE MAINLABEL;
		END IF;
        
        SET @invalidRequestIds = ( SELECT GROUP_CONCAT(DISTINCT(`bbrequest`.`requestId`) SEPARATOR ",") 
									FROM 
                                    `bbrequest`
                                    LEFT JOIN 
									`requestapprovalmatrix` ON (`bbrequest`.`requestId` = `requestapprovalmatrix`.`requestId`)
                                    INNER JOIN 
                                    (SELECT 
										`approvalmatrix`.`id` as approvalMatrixId,
										`approvalrule`.`numberOfApprovals` as numberOfApprovalsRequired,
										(SELECT count(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE approvalMatrixId = `approvalmatrix`.`id`) as numberOfApprovers
										FROM 
										`approvalmatrix`
										LEFT JOIN
										`approvalrule` ON (`approvalmatrix`.`approvalruleId` = `approvalrule`.`id`)
									) as `temp_request_table` ON (`requestapprovalmatrix`.`approvalMatrixId` = `temp_request_table`.`approvalMatrixId`)
									WHERE  
                                    FIND_IN_SET(`requestapprovalmatrix`.`approvalMatrixId`, @invalidMatrixIds)
                                    AND 
                                    ( temp_request_table.numberOfApprovalsRequired = '-1' 
										OR 
									  temp_request_table.numberOfApprovalsRequired >= temp_request_table.numberOfApprovers )
									);
		
		SET SQL_SAFE_UPDATES = 0;
		
		SET @select_statement = concat("
										DELETE FROM customerapprovalmatrix WHERE approvalMatrixId in (",@invalidMatrixIds,") AND customerId = '",_customerId,"'
									");
        PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
		SET @select_statement = concat("
										UPDATE approvalmatrix SET invalid = '1' WHERE approvalmatrix.id in  (",@invalidMatrixIds,")
									");
        PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        IF @invalidRequestIds is NULL THEN   
        	SET SQL_SAFE_UPDATES = 1;   
			LEAVE MAINLABEL;
		END IF;
        
		SET @select_statement = concat("
										UPDATE bbrequest SET status = 'Rejected' WHERE bbrequest.requestId in  (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		
		SET @select_statement = concat("
										UPDATE billpaytransfers 
												INNER JOIN bbrequest ON billpaytransfers.requestId = bbrequest.requestId
                                                SET billpaytransfers.status =  'Rejected'
                                                WHERE billpaytransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE ownaccounttransfers 
												INNER JOIN bbrequest ON ownaccounttransfers.requestId = bbrequest.requestId
                                                SET ownaccounttransfers.status =  'Rejected'
                                                WHERE ownaccounttransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE interbankfundtransfers 
												INNER JOIN bbrequest ON interbankfundtransfers.requestId = bbrequest.requestId
                                                SET interbankfundtransfers.status =  'Rejected'
                                                WHERE interbankfundtransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE intrabanktransfers 
												INNER JOIN bbrequest ON intrabanktransfers.requestId = bbrequest.requestId
                                                SET intrabanktransfers.status =  'Rejected'
                                                WHERE intrabanktransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE p2ptransfers 
												INNER JOIN bbrequest ON p2ptransfers.requestId = bbrequest.requestId
                                                SET p2ptransfers.status =  'Rejected'
                                                WHERE p2ptransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE wiretransfers 
												INNER JOIN bbrequest ON wiretransfers.requestId = bbrequest.requestId
                                                SET wiretransfers.status =  'Rejected'
                                                WHERE wiretransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE internationalfundtransfers 
												INNER JOIN bbrequest ON internationalfundtransfers.requestId = bbrequest.requestId
                                                SET internationalfundtransfers.status =  'Rejected'
                                                WHERE internationalfundtransfers.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE achtransaction 
												INNER JOIN bbrequest ON achtransaction.requestId = bbrequest.requestId
                                                SET achtransaction.status =  'Rejected'
                                                WHERE achtransaction.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @select_statement = concat("
										UPDATE achfile 
												INNER JOIN bbrequest ON achfile.requestId = bbrequest.requestId
                                                SET achfile.status =  'Rejected'
                                                WHERE achfile.requestId in (",@invalidRequestIds,")
									");
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
        
        SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
        IF @companyId is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @numOfParams = 0;
        IF LENGTH(@invalidRequestIds) > 0 THEN
			SET @numOfParams = LENGTH(@invalidRequestIds) - LENGTH(REPLACE(@invalidRequestIds, ',', '')) + 1;
		END IF;
        
        SET @select_statement = "";
		SET @idx = 1;
		LogAction:LOOP
			IF @idx > @numOfParams THEN 
				LEAVE LogAction;
			END IF;
			
			SET @requestId = SUBSTRING_INDEX(SUBSTRING_INDEX(@invalidRequestIds, ',', @idx), ',', -1 );
            
            SET @select_statement = concat("
										INSERT INTO bbactedrequest (`requestId`, `companyId`, `comments`, `status`, `action`)
											VALUES ('",@requestId,"' , '",@companyId,"' , 'Rejected by system as one of the approver lost his permission', 'Rejected', 'Rejected');
									");
          	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            
			SET @idx = @idx + 1;
			 
		END LOOP LogAction;
		
		SET SQL_SAFE_UPDATES = 1;
END$$

DELIMITER ;

DROP procedure IF EXISTS `fetch_request_history_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_request_history_proc`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
	SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
 
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
    AND  `bbactedrequest`.`companyId` = @companyId;
 
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `customeralertchannel_sync`;

DELIMITER $$

CREATE PROCEDURE `customeralertchannel_sync`(
								in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in changedLevel varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in channelsStr varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,                                                        
                                in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci )
BEGIN
SET @preference = (select `alertPreferenceView` from `customerviewalertconfiguration`);

IF operationType is null THEN
     SET operationType = "";
ELSEIF operationType = 'edit' and changedLevel is not null THEN 
SET @channels_list = ( select group_concat(channel.id SEPARATOR ',') from `channel` where (FIND_IN_SET(`channel`.`id`,channelsStr))) ; 
SET @channels_list = IF(@channels_list is null, '', @channels_list);

	IF (@preference = changedLevel AND changedLevel LIKE 'CATEGORY') THEN
	   DELETE FROM `customeralertchannel` where `alertCategoryId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list);
	ELSEIF (@preference = changedLevel AND changedLevel LIKE 'GROUP') THEN
	   DELETE FROM `customeralertchannel` where `alertTypeId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list);
	ELSEIF (@preference = changedLevel AND changedLevel LIKE 'ALERT') THEN
	  DELETE FROM `customeralertchannel` where `alertSubTypeId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list);
	END IF;
ELSEIF operationType = 'reassign' THEN
  IF (@preference != 'CATEGORY' ) THEN
   DELETE FROM `customeralertchannel` where `alertTypeId` = filterValue;
  END IF;
 END IF;
END$$
DELIMITER ;



DROP PROCEDURE IF EXISTS `customeralertFrequency_sync`;

DELIMITER $$

CREATE PROCEDURE `customeralertFrequency_sync`( 
							in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
							in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci  )
BEGIN
SET @preference = (select `alertPreferenceView` from `customerviewalertconfiguration`);
IF (operationType is null) THEN
  SET operationType = "";
ELSEIF (operationType LIKE 'edit' && @preference = 'ALERT') THEN
   DELETE FROM `customeralertfrequency` where `alertSubTypeId` = filterValue; 
ELSEIF (operationType LIKE 'reassign') THEN
  IF (@preference != 'CATEGORY') THEN
   DELETE FROM `customeralertfrequency` where `alertTypeId` = filterValue;
	END IF;
END IF;
end$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `dbxcustomeralertentitlement_sync`;

DELIMITER $$
CREATE PROCEDURE `dbxcustomeralertentitlement_sync`( 
								in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci)
								
BEGIN
IF operationType = 'edit'  THEN	  
	  Delete from `dbxcustomeralertentitlement` where `alertSubTypeId`= filterValue;	
ELSEIF operationType = 'reassign' THEN
	  Delete from `dbxcustomeralertentitlement` where `AlertTypeId` = filterValue;
end IF;
END$$
DELIMITER ;

INSERT INTO `customertypeconfig` (`id`, `CustomerType_id`, `Appid`, `AccessPermitted`) VALUES ('26', 'TYPE_ID_BUSINESS', 'OnlineBanking', b'1');
INSERT INTO `customertypeconfig` (`id`, `CustomerType_id`, `Appid`, `AccessPermitted`) VALUES ('27', 'TYPE_ID_RETAIL', 'OnlineBanking', b'1');
INSERT INTO `customertypeconfig` (`id`, `CustomerType_id`, `Appid`, `AccessPermitted`) VALUES ('28', 'TYPE_ID_PROSPECT', 'OnlineBanking', b'1');

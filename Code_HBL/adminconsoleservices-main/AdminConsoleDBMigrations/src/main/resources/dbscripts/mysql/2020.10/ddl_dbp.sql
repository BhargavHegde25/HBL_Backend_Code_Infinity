CREATE TABLE `alertfrequency` (
  `id` VARCHAR(10) NOT NULL,
  `createdts`TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  

CREATE TABLE `alertfrequencytext` (
  `alertFrequencyId` VARCHAR(10) NOT NULL,
  `languageCode` VARCHAR(10) NOT NULL,
  `displayName` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`alertFrequencyId`, `languageCode`),
  CONSTRAINT `FK_alertfrequencytext_alertfrequency`
    FOREIGN KEY (`alertFrequencyId`)
    REFERENCES `alertfrequency` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alertfrequencytext_locale`
    FOREIGN KEY (`languageCode`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
CREATE TABLE `alerttypechannel` (
  `channelId` VARCHAR(100) NOT NULL,
  `alertTypeId` VARCHAR(255) NOT NULL,
  `createdby` VARCHAR(255) NULL,
  `modifiedby` VARCHAR(255) NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT DEFAULT '0',
  PRIMARY KEY (`channelId`, `alertTypeId`),
  CONSTRAINT `FK_alerttypechannel_channel`
    FOREIGN KEY (`channelId`)
    REFERENCES `channel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alerttypechannel_dbxalerttype`
    FOREIGN KEY (`alertTypeId`)
    REFERENCES `dbxalerttype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	

CREATE TABLE `alertsubtypetext` (
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `languageCode` VARCHAR(10) NOT NULL,
  `displayName` VARCHAR(255) NULL DEFAULT NULL, 
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `createdby` VARCHAR(255) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`alertSubTypeId`, `languageCode`),
  CONSTRAINT `FK_alertsubtypetext_alertsubtype`
    FOREIGN KEY (`alertSubTypeId`)
    REFERENCES `alertsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alertsubtypetext_locale`
    FOREIGN KEY (`languageCode`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	

CREATE TABLE `alertsubtypechannel` (
  `channelId` VARCHAR(100) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `createdby` VARCHAR(255) NULL,
  `modifiedby` VARCHAR(255) NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`channelId`, `alertSubTypeId`),
  CONSTRAINT `FK_alertsubtypechannel_channel`
    FOREIGN KEY (`channelId`)
    REFERENCES `channel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alertsubtypechannel_alertsubtype`
    FOREIGN KEY (`alertSubTypeId`)
    REFERENCES `alertsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `alertsubtypeapp` (
  `appId` VARCHAR(100) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `createdby` VARCHAR(255) NULL,
  `modifiedby` VARCHAR(255) NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`appId`, `alertSubTypeId`),
  CONSTRAINT `FK_alertsubtypeapp_app`
    FOREIGN KEY (`appId`)
    REFERENCES `app` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alertsubtypeapp_alertsubtype`
    FOREIGN KEY (`alertSubTypeId`)
    REFERENCES `alertsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	
	
CREATE TABLE `alertsubtypecustomertype` (
  `customerTypeId` VARCHAR(100) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `createdby` VARCHAR(255) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`customerTypeId`, `alertSubTypeId`),
  CONSTRAINT `FK_alertsubtypecustomertype_customertype`
    FOREIGN KEY (`customerTypeId`)
    REFERENCES `customertype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_alertsubtypecustomertype_alertsubtype`
    FOREIGN KEY (`alertSubTypeId`)
    REFERENCES `alertsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `alertsubtypeaccounttype` (
  `accountTypeId` VARCHAR(50) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `createdby` VARCHAR(255) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0' ,
  PRIMARY KEY (`accountTypeId`, `alertSubTypeId`),
  CONSTRAINT `FK_alertsubtypeaccounttype_alertsubtype`
    FOREIGN KEY (`alertSubTypeId`)
    REFERENCES `alertsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `customerviewalertconfiguration` (
  `id` INT(1) NOT NULL,
  `alertPreferenceView`  ENUM('CATEGORY','GROUP','ALERT') NOT NULL DEFAULT 'CATEGORY',
  `enableFrequency` TINYINT NULL,
  `enableSeparateContact` TINYINT NULL,
  `createdby` VARCHAR(255) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
 
CREATE TABLE `customeralertchannel` (
  `customerId` VARCHAR(100) NOT NULL,
  `alertCategoryId` VARCHAR(255) NOT NULL,
  `alertTypeId` VARCHAR(255) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `channelId` VARCHAR(100) NOT NULL,
  `accountId` VARCHAR(50) NOT NULL,
  `accountType` VARCHAR(50) NOT NULL,
  `createdby` VARCHAR(255)  NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `channelId`, `accountId`, `accountType`),
  CONSTRAINT `FK_customeralertchannel_customer`
    FOREIGN KEY (`customerId`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_customeralertchannel_dbxalertcategory`
    FOREIGN KEY (`alertCategoryId`)
    REFERENCES `dbxalertcategory` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_customeralertchannel_channel`
    FOREIGN KEY (`channelId`)
    REFERENCES `channel` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `customeralertfrequency` (
  `customerId` VARCHAR(100) NOT NULL,
  `alertCategoryId` VARCHAR(255) NOT NULL,
  `alertTypeId` VARCHAR(255) NOT NULL,
  `alertSubTypeId` VARCHAR(100) NOT NULL,
  `alertFrequencyId` VARCHAR(10) NOT NULL,
  `accountId` VARCHAR(50) NOT NULL,
  `accountType` VARCHAR(50) NOT NULL,
  `frequencyValue` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTime` TIME NULL DEFAULT NULL,
  `createdby` VARCHAR(255) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(255) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',
  PRIMARY KEY (`customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `accountId`, `accountType`),
  CONSTRAINT `FK_customeralertfrequency_customer`
    FOREIGN KEY (`customerId`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_customeralertfrequency_dbxalertcategory`
    FOREIGN KEY (`alertCategoryId`)
    REFERENCES `dbxalertcategory` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_customeralertfrequency_alertfrequency`
    FOREIGN KEY (`alertFrequencyId`)
    REFERENCES `alertfrequency` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
	


ALTER TABLE `dbxalertcategory` 
ADD COLUMN `defaultFrequencyId` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci'  NULL AFTER `DisplaySequence`,
ADD COLUMN `defaultFrequencyValue` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL AFTER `defaultFrequencyId`,
ADD COLUMN `defaultFrequencyTime` TIME NULL AFTER `defaultFrequencyValue`;
ALTER TABLE `dbxalertcategory`
ADD CONSTRAINT `FK_dbxalertcategory_alertfrequency`
  FOREIGN KEY (`defaultFrequencyId`)
  REFERENCES `alertfrequency`(`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  

ALTER TABLE `dbxalerttype` 
ADD COLUMN `isAccountLevel` TINYINT NULL DEFAULT '0' AFTER `AlertCategoryId`,
ADD COLUMN `defaultFrequencyId` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `DisplaySequence`,
ADD COLUMN `defaultFrequencyValue` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `defaultFrequencyId`,
ADD COLUMN `defaultFrequencyTime` TIME NULL DEFAULT NULL AFTER `defaultFrequencyValue`;
ALTER TABLE `dbxalerttype`
ADD CONSTRAINT `FK_dbxalerttype_alertfrequency`
  FOREIGN KEY (`defaultFrequencyId`)
  REFERENCES `alertfrequency`(`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  


ALTER TABLE `alertsubtype` 
ADD COLUMN `isAccountLevel` TINYINT NULL DEFAULT '0' AFTER `Name`,
ADD COLUMN `attributeId` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `isAccountLevel`,
ADD COLUMN `alertConditionId` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `attributeId`,
ADD COLUMN `value1` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `alertConditionId`,
ADD COLUMN `value2` VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `value1`,
ADD COLUMN `isGlobal` TINYINT NULL DEFAULT '0' AFTER `Status_id`,
ADD COLUMN `defaultFrequencyId` VARCHAR(10) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `isGlobal`,
ADD COLUMN `defaultFrequencyValue` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NULL DEFAULT NULL AFTER `defaultFrequencyId`,
ADD COLUMN `defaultFrequencyTime` TIME NULL DEFAULT NULL AFTER `defaultFrequencyValue`,
CHANGE COLUMN `Status_id` `Status_id` VARCHAR(50) NULL DEFAULT NULL AFTER `value2`;
ALTER TABLE `alertsubtype` 
ADD CONSTRAINT `FK_alertsubtype_alertattribute`
  FOREIGN KEY (`attributeId`)
  REFERENCES `alertattribute` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_alertsubtype_alertcondition`
  FOREIGN KEY (`alertConditionId`)
  REFERENCES `alertcondition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_alertsubtype_alertfrequency`
  FOREIGN KEY (`defaultFrequencyId`)
  REFERENCES `alertfrequency` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  

ALTER TABLE `dbxcustomeralertentitlement` 
ADD COLUMN `alertCategoryId`  VARCHAR(255) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT '*' AFTER `Customer_id`,
ADD COLUMN `alertSubTypeId` VARCHAR(100) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL DEFAULT '*' AFTER `AlertTypeId`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`Customer_id`, `alertCategoryId`, `AlertTypeId`, `alertSubTypeId`, `AccountId`, `AccountType`);
  

CREATE TABLE `alertfrequencyjobexectime` (
  `id` INT(1) NOT NULL,
  `lastExecTime` TIMESTAMP NULL,
  PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
  
CREATE TABLE `weekday` (
  `id` INT(1) NOT NULL,
  `Name` VARCHAR(20) NULL,
  PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
      
ALTER TABLE `channel` ADD COLUMN `sequence` INT NULL AFTER `status_id`;
ALTER TABLE `alertfrequency` ADD COLUMN `sequence` INT NULL AFTER `id`;


CREATE TABLE `weekdayvalue` (
  `weekdayId` INT(1) NOT NULL,
  `languageCode` VARCHAR(10) NOT NULL,
  `displayName` VARCHAR(255) NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`weekdayId`, `languageCode`),
  CONSTRAINT `FK_weekdayvalue_locale`
    FOREIGN KEY (`languageCode`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
    CONSTRAINT `FK_weekdayvalue_weekday`
    FOREIGN KEY (`weekdayId`)
    REFERENCES `weekday` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
    
ALTER TABLE `bulkwiretemplatelineitems` MODIFY recipientBankZipCode VARCHAR(50);


CREATE TABLE `alertfrequencytime` (
  `id` TIME NOT NULL,
  PRIMARY KEY (`id`))ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
	
	
DELIMITER $$
CREATE PROCEDURE `customer_action_save_proc`(
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
           	set @query = concat('INSERT INTO customeraction(id,RoleType_id,Customer_id,action_id,account_id,isAllowed,limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

ALTER TABLE `credentialchecker` 
ADD COLUMN `resendAttempts` VARCHAR(45) NULL DEFAULT 0 AFTER `UserName`;


DROP TABLE IF EXISTS `cardproducttype`;
CREATE TABLE `cardproducttype` (
	`id` INT NOT NULL AUTO_INCREMENT,
	`productId` INT (50) NOT NULL,
	`accountType` VARCHAR(50) NOT NULL,
        `createdOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
        `updatedOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `createdBy` VARCHAR(45),
        `modifiedby` varchar(50) DEFAULT NULL,
        `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',  
         PRIMARY KEY (`id`)
)

ENGINE = InnoDB;

DROP TABLE IF EXISTS `cardproducts`;
CREATE TABLE `cardproducts` (
	`productId` INT(50) NOT NULL AUTO_INCREMENT,
	`productName` VARCHAR(200) NOT NULL,
	`featureOverview` TEXT NULL DEFAULT NULL,
	`featureDescription` TEXT NULL DEFAULT NULL,
	`representativeLabel1` VARCHAR(2000) NULL DEFAULT NULL,
	`representativeLabel2` VARCHAR(2000) NULL DEFAULT NULL,
	`representativeLabel3` VARCHAR(2000) NULL DEFAULT NULL,
	`representativeValue1` VARCHAR(2000) NULL DEFAULT NULL,
	`representativeValue2` VARCHAR(2000) NULL DEFAULT NULL,
	`representativeValue3` VARCHAR(2000) NULL DEFAULT NULL,
        `createdOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
        `updatedOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
        `createdBy` VARCHAR(45),
        `modifiedby` varchar(50) DEFAULT NULL,
        `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
        `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',  
	 PRIMARY KEY (`productId`)
)
ENGINE = InnoDB;

ALTER TABLE `cardproducttype`
	ADD CONSTRAINT `FK_product_Id` FOREIGN KEY (`productId`) REFERENCES `cardproducts` (`productId`) ON UPDATE NO ACTION ON DELETE NO ACTION;
	
	ALTER TABLE `cardproducts`
	ADD COLUMN `withdrawlLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `softdeleteflag`,
	ADD COLUMN `withdrawalMinLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `withdrawlLimit`,
	ADD COLUMN `withdrawalMaxLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `withdrawalMinLimit`,
	ADD COLUMN `withdrawalStepLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `withdrawalMaxLimit`,
	ADD COLUMN `purchaseLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `withdrawalStepLimit`,
	ADD COLUMN `purchaseMinLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `purchaseLimit`,
	ADD COLUMN `purchaseMaxLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `purchaseMinLimit`,
	ADD COLUMN `purchaseStepLimit` VARCHAR(50) NULL DEFAULT NULL AFTER `purchaseMaxLimit`;

CREATE TABLE `loanschedule` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `AccountId` VARCHAR(50) NULL,
  `Amount` VARCHAR(50) NULL,
  `Principal` VARCHAR(50) NULL,
  `Interest` VARCHAR(50) NULL,
  `OutstandingBalance` VARCHAR(50) NULL,
  `Charges` VARCHAR(50) NULL,
  `Tax` VARCHAR(50) NULL,
  `Insurance` VARCHAR(50) NULL,
  `CumulativeInterest` VARCHAR(50) NULL,
  `InstallmentType` VARCHAR(50) NULL,
  `Date` DATE NULL,
  PRIMARY KEY (`id`));
  
 ALTER TABLE `customercommunication` ADD COLUMN `isAlertsRequired` TINYINT(1) NULL DEFAULT '0' AFTER `phoneCountryCode`;
 
 ALTER TABLE `card` 
ADD COLUMN `withdrawalMinLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `withdrawlLimit`,
ADD COLUMN `withdrawalMaxLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `withdrawalMinLimit`,
ADD COLUMN `withdrawalStepLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `withdrawalMaxLimit`,
ADD COLUMN `purchaseLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `withdrawalStepLimit`,
ADD COLUMN `purchaseMinLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `purchaseLimit`,
ADD COLUMN `purchaseMaxLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `purchaseMinLimit`,
ADD COLUMN `purchaseStepLimit` VARCHAR(50) NULL DEFAULT '0.00' AFTER `purchaseMaxLimit`;

DROP TABLE IF EXISTS `paymentfiles`;
CREATE TABLE `paymentfiles` (
  `paymentFileID` varchar(40) NOT NULL,
  `userId` varchar(50) NOT NULL,
  `transactionId` varchar(45) NOT NULL,
  `paymentFileName` varchar(150) NOT NULL,
  `paymentFileType` varchar(45) NOT NULL,
  `paymentFileContents` mediumtext NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`paymentFileID`),
  KEY `INDEX_PAYMENT_USERID` (`userId`));
  
DROP VIEW IF EXISTS  `channel_view`;
create view `channel_view` as select
    `channel`.`id` as `channel_id`,
    `channel`.`status_id` as `channel_status_id`,
    `channel`.`sequence` as `channel_sequence`,
    `channeltext`.`LanguageCode` as `channeltext_LanguageCode`,
    `channeltext`.`Description` as `channeltext_Description`,
    `channeltext`.`createdby` as `channeltext_createdby`,
    `channeltext`.`modifiedby` as `channeltext_modifiedby`,
    `channeltext`.`createdts` as `channeltext_createdts`,
    `channeltext`.`lastmodifiedts` as `channeltext_lastmodifiedts`,
    `channeltext`.`synctimestamp` as `channeltext_synctimestamp`,
    `channeltext`.`softdeleteflag` as `channeltext_softdeleteflag`
from
    (`channeltext`
join `channel` on
    ((`channeltext`.`channelID` = `channel`.`id`)));


DROP VIEW IF EXISTS  `alertfrequency_view`;
create view `alertfrequency_view` as select
    `alertfrequency`.`id` as `alertfrequency_id`,
    `alertfrequency`.`sequence` as `alertfrequency_sequence`,
    `alertfrequencytext`.`languageCode` as `alertfrequencytext_languageCode`,
    `alertfrequencytext`.`description` as `alertfrequencytext_description`,
    `alertfrequencytext`.`displayName` as `alertfrequencytext_displayName`
from
    (`alertfrequencytext`
join `alertfrequency` on
    ((`alertfrequencytext`.`alertFrequencyId` = `alertfrequency`.`id`)));

  
DROP VIEW IF EXISTS  alertcustomersaccountchannels_view;
DROP VIEW IF EXISTS alerts_fetch_globaldata_view;
		
DROP VIEW IF EXISTS alertcustomerchannels_view;
CREATE VIEW `alertcustomerchannels_view` AS
    SELECT 
        `dbxcustomeralertentitlement`.`alertSubTypeId` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`
    FROM
        (`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertSubTypeId` = `customeralertchannel`.`alertSubTypeId`))));
			
			
			
DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertgrouplevel` ;
CREATE VIEW `alerts_fetch_globaldata_view_alertgrouplevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alerttypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`
    FROM
        (((`dbxalerttype`
        JOIN `alerttypechannel` ON ((`dbxalerttype`.`id` = `alerttypechannel`.`alertTypeId`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)));


DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertlevel` ;
CREATE VIEW `alerts_fetch_globaldata_view_alertlevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertsubtypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`
    FROM
        (((`alertsubtype`
        JOIN `alertsubtypechannel` ON ((`alertsubtype`.`id` = `alertsubtypechannel`.`alertSubTypeId`)))
        JOIN `dbxalerttype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)));


DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertcategorylevel` ;
CREATE VIEW `alerts_fetch_globaldata_view_alertcategorylevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertcategorychannel`.`ChannelID` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`
    FROM
        (((`dbxalertcategory`
        JOIN `alertcategorychannel` ON ((`alertcategorychannel`.`AlertCategoryId` = `dbxalertcategory`.`id`)))
        JOIN `dbxalerttype` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)))
        JOIN `alertsubtype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)));

DROP VIEW IF EXISTS  `alertcustomerchannels_view`;



DROP VIEW IF EXISTS  `alertcustomerchannels_view_alertcategorylevel`;
CREATE VIEW `alertcustomerchannels_view_alertcategorylevel` AS
    SELECT 
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`
    FROM
        (((`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`))))
        JOIN `dbxalerttype` ON ((`dbxcustomeralertentitlement`.`alertCategoryId` = `dbxalerttype`.`AlertCategoryId`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)));
		

DROP VIEW IF EXISTS  `alertcustomerchannels_view_alertgrouplevel`;

CREATE VIEW `alertcustomerchannels_view_alertgrouplevel` AS
    SELECT 
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`
    FROM
        ((`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`AlertTypeId` = `customeralertchannel`.`alertTypeId`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`))))
        JOIN `alertsubtype` ON ((`dbxcustomeralertentitlement`.`AlertTypeId` = `alertsubtype`.`AlertTypeId`)));
		
		
DROP VIEW IF EXISTS  `alertcustomerchannels_view_alertlevel`;
CREATE VIEW `alertcustomerchannels_view_alertlevel` AS
    SELECT 
        `dbxcustomeralertentitlement`.`alertSubTypeId` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`
    FROM
        (`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertSubTypeId` = `customeralertchannel`.`alertSubTypeId`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`)
            AND (`dbxcustomeralertentitlement`.`AlertTypeId` = `customeralertchannel`.`alertTypeId`))));
        
DROP procedure IF EXISTS `subscriber_getCoreIdFromDbxIds`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getCoreIdFromDbxIds`(_dbxids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

select BackendId,Customer_id  from backendidentifier where FIND_IN_SET(`backendidentifier`.`Customer_id`,_dbxids) and BackendType is null;

END$$

DELIMITER ;




DROP procedure IF EXISTS `subscriber_getCoreIdFromDbxIds_coreSpecific`;

DELIMITER $$
CREATE PROCEDURE `subscriber_getCoreIdFromDbxIds_coreSpecific`(_dbxids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, _coretype TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select BackendId,Customer_id  from backendidentifier where FIND_IN_SET(`backendidentifier`.`Customer_id`,_dbxids) and BackendType = _coretype ;
END$$

DELIMITER ;




DROP procedure IF EXISTS `subscriber_getCustIdFromCore`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getCustIdFromCore`(_backendids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select BackendId,Customer_id  from backendidentifier where FIND_IN_SET(`backendidentifier`.`BackendId`,_backendids);
END$$

DELIMITER ;


DROP procedure IF EXISTS `subscriber_getAlertSubtypePreferences`;
DELIMITER $$
CREATE  PROCEDURE `subscriber_getAlertSubtypePreferences`(alerttypes TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select id as alertsubtypeid ,AlertTypeId as alerttypeid ,  attributeId as attributeid, alertConditionId as alertconditionid, value1, value2, isGlobal as isglobal
 from alertsubtype where FIND_IN_SET(`alertsubtype`.`AlertTypeId`,alerttypes)  ;
END$$

DELIMITER ;


DROP procedure IF EXISTS `subscriber_getEntitleMentsNocustomer`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getEntitleMentsNocustomer`(alerttypes TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2 FROM dbxcustomeralertentitlement where   FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId`,alerttypes)  ;
END$$

DELIMITER ;


DROP procedure IF EXISTS `subscriber_getEntitleMentsWithcustomer`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getEntitleMentsWithcustomer`(alerttypes TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,custids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2  FROM dbxcustomeralertentitlement where   FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId`,alerttypes)  and
   FIND_IN_SET(`dbxcustomeralertentitlement`.`Customer_id`,custids);
END$$

DELIMITER ;


ALTER TABLE `credentialchecker` 
CHANGE COLUMN `resendAttempts` `retryCount` VARCHAR(45) NULL DEFAULT '0' ;

DROP VIEW IF EXISTS `cardproductsview`;
CREATE VIEW `cardproductsview` AS SELECT `cardproducts`.`productId` AS `productId`,`cardproducts`.`productName` AS `productName`,`cardproducttype`.`accountType` AS `accountType`,`cardproducts`.`featureOverview` AS `featureOverview`,`cardproducts`.`featureDescription` AS `featureDescription`,
`cardproducts`.`representativeLabel1` AS `representativeLabel1`,
`cardproducts`.`representativeLabel2` AS `representativeLabel2`,
`cardproducts`.`representativeLabel3` AS `representativeLabel3`,
`cardproducts`.`representativeValue1` AS `representativeValue1`,
`cardproducts`.`representativeValue2` AS `representativeValue2`,
`cardproducts`.`representativeValue3` AS `representativeValue3`,
`cardproducts`.`withdrawlLimit` AS `withdrawlLimit`,
`cardproducts`.`withdrawalMinLimit` AS `withdrawalMinLimit`,
`cardproducts`.`withdrawalMaxLimit` AS `withdrawalMaxLimit`,
`cardproducts`.`withdrawalStepLimit` AS `withdrawalStepLimit`,
`cardproducts`.`purchaseLimit` AS `purchaseLimit`,
`cardproducts`.`purchaseMinLimit` AS `purchaseMinLimit`,
`cardproducts`.`purchaseMaxLimit` AS `purchaseMaxLimit`,
`cardproducts`.`purchaseStepLimit` AS `purchaseStepLimit`
from (`cardproducts` join `cardproducttype`) where (`cardproducttype`.`productId` = `cardproducts`.`productId`) ;
  

DROP PROCEDURE IF EXISTS `verify_user_proc`;

delimiter $$

CREATE PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci

)
BEGIN

SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`
    FROM
        (`customer`
        LEFT JOIN `customercommunication` `primaryphone` ON ((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`Value` = _phone)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))
        LEFT JOIN `customercommunication` `primaryemail` ON ((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`Value` = _email)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')))
	where
		`customer`.`DateOfBirth` = _dateOfBirth
		and `primaryphone`.`Customer_id` = `primaryemail`.`Customer_id`;

END$$

DELIMITER ;

ALTER TABLE `card`
	ADD COLUMN `cardDisplayName` VARCHAR(50) NULL DEFAULT NULL AFTER `currencyCode`;
	
CREATE TABLE `transactiontypemapping` (
  `backendTransactionTypeId` VARCHAR(10) NOT NULL,
  `backendTransactionType` VARCHAR(45) NULL,
  `dbxTransactionType` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`backendTransactionTypeId`));
  
  

 

DROP procedure IF EXISTS `getCustomersAlertFrequency_Sp_alertlevel`;
DELIMITER $$
CREATE  PROCEDURE `getCustomersAlertFrequency_Sp_alertlevel`(startTime varchar(50) CHARACTER SET UTF8, endTime Varchar(50) CHARACTER SET UTF8 , scheduleDay varchar(50) CHARACTER SET UTF8 ,scheduleDate INT(50), isLastDate VARCHAR(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTime  varchar(50) CHARACTER SET UTF8 DEFAULT startTime;
 DECLARE endTime varchar(50) CHARACTER SET UTF8 DEFAULT endTime;
 DECLARE scheduleDay varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDay;
 DECLARE scheduleDate INT(50) DEFAULT scheduleDate;
 DECLARE isLastDate VARCHAR(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 if (isLastDate = 'true')
 then
 select 
 *  
 from customeralertfrequency 
 where 
 customeralertfrequency.frequencyTime>startTime 
 and customeralertfrequency.frequencyTime<=endTime 
 and((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'));
 else
  select
  *  
  from 
  customeralertfrequency 
  where 
  customeralertfrequency.frequencyTime>startTime 
  and customeralertfrequency.frequencyTime<=endTime 
  and((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'));
 end if;
END$$

DELIMITER ;






DROP procedure IF EXISTS `getCustomersAlertFrequency_Sp_grouplevel`;

DELIMITER $$

CREATE  PROCEDURE `getCustomersAlertFrequency_Sp_grouplevel`(startTime varchar(50) CHARACTER SET UTF8, endTime Varchar(50) CHARACTER SET UTF8 , scheduleDay varchar(50) CHARACTER SET UTF8 ,scheduleDate INT(50), isLastDate VARCHAR(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTime  varchar(50) CHARACTER SET UTF8 DEFAULT startTime;
 DECLARE endTime varchar(50) CHARACTER SET UTF8 DEFAULT endTime;
 DECLARE scheduleDay varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDay;
 DECLARE scheduleDate INT(50) DEFAULT scheduleDate;
 DECLARE isLastDate VARCHAR(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 if (isLastDate = 'true')
 then
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 customeralertfrequency.alertTypeId, 
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId 
 from customeralertfrequency
 join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
 where 
 customeralertfrequency.frequencyTime>startTime 
 and customeralertfrequency.frequencyTime<=endTime 
 and((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
 and alertsubtype.defaultFrequencyId is not null;
 else
  select 
  customeralertfrequency.customerId,
  customeralertfrequency.alertCategoryId,
  customeralertfrequency.alertTypeId, 
  customeralertfrequency.accountId,
  alertsubtype.id as alertSubTypeId 
  from 
  customeralertfrequency 
  join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
  where 
  customeralertfrequency.frequencyTime>startTime 
  and customeralertfrequency.frequencyTime<=endTime 
  and((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
  and alertsubtype.defaultFrequencyId is not null;
 end if;
END$$

DELIMITER ;






DROP procedure IF EXISTS `getCustomersAlertFrequency_Sp_categorylevel`;

DELIMITER $$

CREATE  PROCEDURE `getCustomersAlertFrequency_Sp_categorylevel`(startTime varchar(50) CHARACTER SET UTF8, endTime Varchar(50) CHARACTER SET UTF8 , scheduleDay varchar(50) CHARACTER SET UTF8 ,scheduleDate INT(50), isLastDate VARCHAR(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTime  varchar(50) CHARACTER SET UTF8 DEFAULT startTime;
 DECLARE endTime varchar(50) CHARACTER SET UTF8 DEFAULT endTime;
 DECLARE scheduleDay varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDay;
 DECLARE scheduleDate INT(50) DEFAULT scheduleDate;
 DECLARE isLastDate VARCHAR(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 if (isLastDate = 'true')
 then
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 customeralertfrequency.frequencyTime>startTime 
 and  customeralertfrequency.frequencyTime<=endTime 
 and ((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
 and alertsubtype.defaultFrequencyId is not null;
 else
  select 
  customeralertfrequency.customerId,
  customeralertfrequency.alertCategoryId,
  dbxalerttype.id as alertTypeId, 
  customeralertfrequency.accountId,
  alertsubtype.id as alertSubTypeId 
  from 
  customeralertfrequency 
  join dbxalerttype on customeralertfrequency.alertCategoryId = dbxalerttype.AlertCategoryId 
  join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
  where 
  customeralertfrequency.frequencyTime>startTime 
  and customeralertfrequency.frequencyTime<=endTime 
  and((customeralertfrequency.frequencyValue=scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
  and alertsubtype.defaultFrequencyId is not null ;
 end if;
END$$

DELIMITER ;

DROP procedure IF EXISTS `getAllCustomersAlertFrequency_Sp_alertlevel`;

DELIMITER $$

CREATE  PROCEDURE `getAllCustomersAlertFrequency_Sp_alertlevel`(startTimePrev varchar(50) CHARACTER SET UTF8, startTimeCurr Varchar(50) CHARACTER SET UTF8, 
endTimePrev Varchar(50) CHARACTER SET UTF8, endTimeCurr Varchar(50) CHARACTER SET UTF8,scheduleDayPrev Varchar(50) CHARACTER SET UTF8,scheduleDayCurr Varchar(50) CHARACTER SET UTF8, 
scheduleDatePrev INT(50) , scheduleDateCurr INT(50) ,isLastDate Varchar(50) CHARACTER SET UTF8,isPrevDateLastDate Varchar(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTimePrev  varchar(50) CHARACTER SET UTF8 DEFAULT startTimePrev;
 DECLARE startTimeCurr  varchar(50) CHARACTER SET UTF8 DEFAULT startTimeCurr;
 DECLARE endTimePrev varchar(50) CHARACTER SET UTF8 DEFAULT endTimePrev;
 DECLARE endTimeCurr varchar(50) CHARACTER SET UTF8 DEFAULT endTimeCurr;
 DECLARE scheduleDayPrev varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayPrev;
 DECLARE scheduleDayCurr varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayCurr;
 DECLARE scheduleDatePrev INT(50) DEFAULT scheduleDatePrev;
 DECLARE scheduleDateCurr INT(50) DEFAULT scheduleDateCurr;
 DECLARE isLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 DECLARE isPrevDateLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isPrevDateLastDate;
 if(isLastDate = 'true') then
 if(isPrevDateLastDate = 'true' ) then
select 
*  
from 
customeralertfrequency 
where 
(customeralertfrequency.frequencyTime>startTimePrev and customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))
 or (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY')));
 else
 select 
 *  
 from 
 customeralertfrequency 
 where 
 (customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY')));
 end if;
 else
 select 
 *  
 from 
 customeralertfrequency 
 where 
 (customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY')));
 end if;
END$$

DELIMITER ;





DROP procedure IF EXISTS `getAllCustomersAlertFrequency_Sp_grouplevel`;

DELIMITER $$

CREATE  PROCEDURE `getAllCustomersAlertFrequency_Sp_grouplevel`(startTimePrev varchar(50) CHARACTER SET UTF8, startTimeCurr Varchar(50) CHARACTER SET UTF8, 
endTimePrev Varchar(50) CHARACTER SET UTF8, endTimeCurr Varchar(50) CHARACTER SET UTF8,scheduleDayPrev Varchar(50) CHARACTER SET UTF8,scheduleDayCurr Varchar(50) CHARACTER SET UTF8, 
scheduleDatePrev INT(50) , scheduleDateCurr INT(50) ,isLastDate Varchar(50) CHARACTER SET UTF8,isPrevDateLastDate Varchar(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTimePrev  varchar(50) CHARACTER SET UTF8 DEFAULT startTimePrev;
 DECLARE startTimeCurr  varchar(50) CHARACTER SET UTF8 DEFAULT startTimeCurr;
 DECLARE endTimePrev varchar(50) CHARACTER SET UTF8 DEFAULT endTimePrev;
 DECLARE endTimeCurr varchar(50) CHARACTER SET UTF8 DEFAULT endTimeCurr;
 DECLARE scheduleDayPrev varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayPrev;
 DECLARE scheduleDayCurr varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayCurr;
 DECLARE scheduleDatePrev INT(50) DEFAULT scheduleDatePrev;
 DECLARE scheduleDateCurr INT(50) DEFAULT scheduleDateCurr;
 DECLARE isLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 DECLARE isPrevDateLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isPrevDateLastDate;
 if(isLastDate = 'true') then
 if(isPrevDateLastDate = 'true' ) then
select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
where 
((customeralertfrequency.frequencyTime>startTimePrev and customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
and alertsubtype.defaultFrequencyId is not null;
 else
 select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null;
 end if;
 else
 select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
where 
 ((customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null;
 end if;
END$$

DELIMITER ;





DROP procedure IF EXISTS `getAllCustomersAlertFrequency_Sp_categorylevel`;

DELIMITER $$

CREATE  PROCEDURE `getAllCustomersAlertFrequency_Sp_categorylevel`(startTimePrev varchar(50) CHARACTER SET UTF8, startTimeCurr Varchar(50) CHARACTER SET UTF8, 
endTimePrev Varchar(50) CHARACTER SET UTF8, endTimeCurr Varchar(50) CHARACTER SET UTF8,scheduleDayPrev Varchar(50) CHARACTER SET UTF8,scheduleDayCurr Varchar(50) CHARACTER SET UTF8, 
scheduleDatePrev INT(50) , scheduleDateCurr INT(50) ,isLastDate Varchar(50) CHARACTER SET UTF8,isPrevDateLastDate Varchar(50) CHARACTER SET UTF8)
BEGIN
 DECLARE startTimePrev  varchar(50) CHARACTER SET UTF8 DEFAULT startTimePrev;
 DECLARE startTimeCurr  varchar(50) CHARACTER SET UTF8 DEFAULT startTimeCurr;
 DECLARE endTimePrev varchar(50) CHARACTER SET UTF8 DEFAULT endTimePrev;
 DECLARE endTimeCurr varchar(50) CHARACTER SET UTF8 DEFAULT endTimeCurr;
 DECLARE scheduleDayPrev varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayPrev;
 DECLARE scheduleDayCurr varchar(50) CHARACTER SET UTF8 DEFAULT scheduleDayCurr;
 DECLARE scheduleDatePrev INT(50) DEFAULT scheduleDatePrev;
 DECLARE scheduleDateCurr INT(50) DEFAULT scheduleDateCurr;
 DECLARE isLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isLastDate;
 DECLARE isPrevDateLastDate Varchar(50) CHARACTER SET UTF8 DEFAULT isPrevDateLastDate;
 if(isLastDate = 'true') then
 if(isPrevDateLastDate = 'true' ) then
select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
where 
((customeralertfrequency.frequencyTime>startTimePrev and customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
and alertsubtype.defaultFrequencyId is not null;
 else
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null;
 end if;
 else
 select 
customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>startTimePrev and  customeralertfrequency.frequencyTime<=endTimePrev and((customeralertfrequency.frequencyValue=scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or  (customeralertfrequency.frequencyTime>=startTimeCurr and  customeralertfrequency.frequencyTime<=endTimeCurr and((customeralertfrequency.frequencyValue=scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null;
 end if;
END$$

DELIMITER ;


DROP procedure IF EXISTS `getCoreIdForCustomerIds_Sp`;

DELIMITER $$
CREATE PROCEDURE `getCoreIdForCustomerIds_Sp`( customerIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,backendType TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 DECLARE customerIds  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT customerIds;
 DECLARE backendType  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT backendType;
 if(backendType = 'null')
 then 
  SELECT * FROM backendidentifier where FIND_IN_SET (`backendidentifier`.`Customer_id`,customerIds) and backendidentifier.backendType is NULL;
  else
    SELECT * FROM backendidentifier where FIND_IN_SET (`backendidentifier`.`Customer_id`,customerIds) and FIND_IN_SET(`backendidentifier`.`BackendType`,backendType);
end if;
END$$

DELIMITER ;

ALTER TABLE cardtransaction ADD isdisputed bit(1) DEFAULT b'0' NULL;
ALTER TABLE cardtransaction ADD disputedescription varchar(100) NULL;
ALTER TABLE cardtransaction ADD disputereason varchar(100) NULL;
ALTER TABLE cardtransaction ADD disputestatus varchar(50) NULL;
ALTER TABLE cardtransaction ADD disputedate TIMESTAMP NULL;


CREATE INDEX IDX_transaction_schdate ON transaction (scheduledDate ASC);

CREATE INDEX IDX_transaction_personid ON transaction (Person_ID ASC); 

DROP procedure IF EXISTS `dbpalerts_getCustomerData`;
DROP procedure IF EXISTS `dbpalerts_getCustIdFromCore`;
DROP procedure IF EXISTS `dbpalerts_getCustidFromAccount`;


DROP procedure IF EXISTS `dbpevents_getCustomerData`;
DELIMITER $$
CREATE PROCEDURE `dbpevents_getCustomerData` (_customerids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,_usernames TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select id as CustomerId, UserName from customer where FIND_IN_SET(`customer`.`id`,_customerids) or FIND_IN_SET(`customer`.`UserName`,_usernames) ;
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpevents_getCustIdFromCore`;
DELIMITER $$
CREATE PROCEDURE `dbpevents_getCustIdFromCore` (_backendids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select BackendId,Customer_id  from backendidentifier where FIND_IN_SET(`backendidentifier`.`BackendId`,_backendids);
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpevents_getCustidFromAccount`;
DELIMITER $$
CREATE PROCEDURE `dbpevents_getCustidFromAccount` (_accounts TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select Account_id, User_id, Type_id as accounttype_id from accounts where FIND_IN_SET(`accounts`.`Account_id`,_accounts);
END$$
DELIMITER ;

ALTER TABLE customer ADD isEnrolledFromSpotlight TINYINT DEFAULT 0 NULL;


DROP procedure IF EXISTS `customer_search_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_search_proc`( 
in _searchType varchar(60),
in _id varchar(50), 
in _name varchar(50),  
in _SSN varchar(50),
in _username varchar(50), 
in _phone varchar(100), 
in _email varchar(100),
in _IsStaffMember varchar(10),
in _cardorAccountnumber varchar(50),
in _TIN varchar(50),
in _group varchar(40), 
in _IDType varchar(50),
in _IDValue varchar(50),
in _companyId varchar(50),
in _requestID varchar(50),
in _branchIDS varchar(2000), 
in _productIDS varchar(2000), 
in _cityIDS varchar(2000), 
in _entitlementIDS varchar(2000), 
in _groupIDS varchar(2000), 
in _customerStatus varchar(50), 
in _before varchar(20), 
in _after varchar(20), 
in _sortVariable varchar(100), 
in _sortDirection varchar(4),  
in _pageOffset bigint, 
in _pageSize bigint)
BEGIN
	
    DECLARE _maxLockCount varchar(50);

	IF _searchType LIKE 'GROUP_SEARCH%' then
		set @search_select_statement = "SELECT 
					customer.id, customer.FirstName, customer.MiddleName, customer.LastName,
					concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
					customer.UserName as Username, customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember,
					IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`,
					customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,
					GROUP_CONCAT(customergroup.Group_id) as assigned_group_ids,
					address.City_id, city.Name As City_name,
					customer.Location_id AS branch_id,
					location.Name AS branch_name";
		set @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
		set @queryStatement = concat("
				FROM customer
                    JOIN (
						SELECT
							customer.id
						FROM
							customer ",
							IF( _groupIDS != '' OR _entitlementIDS != '', " LEFT JOIN customergroup ON (customergroup.Customer_id=customer.id)", ""),
							IF( _cityIDS != '', " LEFT JOIN customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
							LEFT JOIN address ON (customeraddress.Address_id = address.id)
							LEFT JOIN city ON (address.City_id = city.id)", ""),

							IF( _entitlementIDS != '', " LEFT JOIN customerentitlement ON (customerentitlement.Customer_id=customer.id) ", ""),
							IF( _productIDS != '', " LEFT JOIN customerproduct ON (customerproduct.Customer_id=customer.id) ", ""));
			set @whereclause = "WHERE true";
			
            IF _username != "" then
				
				set @whereclause = concat(@whereclause, " AND (customer.firstname like concat(",quote(_username),",'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.username like concat(",quote(_username)," ,'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.id like concat(", quote(_username) ,",'%')) ");
			end if;
            
            
			if _IsStaffMember != "" then
				if _IsStaffMember = "true" then
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '1'");
				else
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '0'");
				end if;
			end if;
            
			
			if _entitlementIDS != "" then
				set @whereclause = concat(@whereclause," AND (customerentitlement.Service_id in (",func_escape_input_for_in_operator(_entitlementIDS),") 
							OR customergroup.Group_id in ( select Group_id from groupentitlement where Service_id in (",func_escape_input_for_in_operator(_entitlementIDS)," )))");
			end if;
			
			
			if _groupIDS != "" then
				set @whereclause = concat(@whereclause," AND customergroup.Group_id in (",func_escape_input_for_in_operator(_groupIDS),") ");
			end if;
			
			
			if _productIDS != "" then
				set @whereclause = concat(@whereclause," AND customerproduct.Product_id in (",func_escape_input_for_in_operator(_productIDS),")");
			end if;
			
			
			if _branchIDS != "" then
				set @whereclause = concat(@whereclause, " AND customer.Location_id in (",func_escape_input_for_in_operator(_branchIDS),")");
			end if;
			
			
			if _customerStatus != "" then
				set @whereclause = concat(@whereclause, " AND customer.Status_id = ", quote(_customerStatus));
			end if;
			
			
			if _cityIDS != "" then
				set @whereclause = concat(@whereclause, " AND city.Name in (",func_escape_input_for_in_operator(_cityIDS),")");
			end if;
			
			
			if _before != "" and _after != "" then
				set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_before) ," and date(customer.createdts) <= date ", quote(_after));
			else 
				if _before != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) <= date ", quote(_before));
				elseif _after != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_after));
				end if;
			end if;
			
			set @queryStatement = concat(@queryStatement,@whereclause,
            ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
					
					LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
					LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
					LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
					LEFT JOIN address ON (customeraddress.Address_id = address.id)
					LEFT JOIN city ON (city.id = address.City_id)
					LEFT JOIN location ON (location.id=customer.Location_id)  
			");
			
			IF _searchType = 'GROUP_SEARCH' THEN
                set @queryStatement2 = concat(@search_count_statement, @queryStatement);
				set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
				IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

			ELSEIF _searchType = 'GROUP_SEARCH_TOTAL_COUNT' then
				set @queryStatement = concat(@search_count_statement, @queryStatement);
			END IF;


	ELSEIF _searchType LIKE 'CUSTOMER_SEARCH%' then
			
			SELECT accountLockoutThreshold from passwordlockoutsettings where id='PLOCKID1' INTO _maxLockCount;
            
            SET @search_select_statement = concat("SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,
				concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
				customer.UserName as Username,customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight,customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender,CONCAT('****', RIGHT(customer.Ssn, 4)) as Ssn,
                IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`, 
                company.id as CompanyId, company.Name as CompanyName,
                organisationemployees.isAuthSignatory as isAuthSignatory, 
                IF(IFNULL(customer.lockCount,0) >= ",_maxLockCount,", 'SID_CUS_LOCKED',customer.Status_id) as Status_id,
				PrimaryPhone.value AS PrimaryPhoneNumber,
				PrimaryEmail.value AS PrimaryEmailAddress,
                GROUP_CONCAT(membergroup.Name) as groups,
                customer.ApplicantChannel, customer.createdts");
            SET @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
			SET @queryStatement = concat("
			FROM customer
			JOIN (
				SELECT
					customer.id
				FROM customer ",
					IF( _phone != '', " JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE') ",""),
					IF(_email != '', "  JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL') ",""),
                    IF( _TIN != '', " LEFT JOIN organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)", ""),
                    IF( _cardorAccountnumber != '', " LEFT JOIN card ON (customer.id = card.User_id)
                    LEFT JOIN accounts ON (customer.id = accounts.User_id)
                    LEFT JOIN customeraccounts ON (customer.id = customeraccounts.Customer_id)", ""));

                SET @queryStatement = concat(@queryStatement," WHERE true");
                
                IF _id != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.id = ", quote(_id));
                end if;
                
                 IF _name != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.LastName like concat(", quote(_name),",'%')");
                end if;

                IF _SSN != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Ssn = ",quote(_SSN));
                end if;

                IF _username != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.username = ",quote(_username));
                end if;

                IF _phone != '' THEN
					IF length(_phone) > 9 THEN
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value like concat('%',",quote(_phone),",'%')");
                    ELSE
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value = ",quote(_phone));
					end if;
                end if;

                IF _email != '' THEN
					set @queryStatement = concat(@queryStatement," and PrimaryEmail.value = ",quote(_email));
                end if;

                IF _companyId != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Organization_id = ",quote(_companyId));
                end if;

				IF _IDValue != '' THEN
                    IF _IDType = 'ID_DRIVING_LICENSE' THEN
						set @queryStatement = concat(@queryStatement," and (customer.DrivingLicenseNumber = ",quote(_IDValue)," or 
                        (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),"))");
                    ELSE
						set @queryStatement = concat(@queryStatement," and (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),")");
					end if;
                end if;
                
				IF _TIN != '' THEN
					set @queryStatement = concat(@queryStatement," and organisationmembership.Taxid = ",quote(_TIN));
                end if;
                
                IF _cardorAccountnumber != '' THEN
					set @queryStatement = concat(@queryStatement," and (card.cardNumber = ",quote(_cardorAccountnumber),
                    " or accounts.Account_id = ",quote(_cardorAccountnumber)," or customeraccounts.Account_id = ",quote(_cardorAccountnumber),")");
                end if;
                
                set @queryStatement = concat(@queryStatement,
                ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
			LEFT JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE')
			LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
			LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
			LEFT JOIN membergroup ON (membergroup.id=customergroup.group_id)
            LEFT JOIN organisation company ON (customer.Organization_id = company.id)
            LEFT JOIN organisationemployees ON (organisationemployees.Organization_id = company.id)");

            IF _searchType = 'CUSTOMER_SEARCH' THEN
				set @queryStatement2 = concat(@search_count_statement, @queryStatement);
            	set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
            	IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

            ELSEIF _searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT' THEN
            	set @queryStatement = concat(@search_count_statement, @queryStatement);
            END IF;
	END IF;

	
	PREPARE stmt FROM @queryStatement; EXECUTE stmt; 
     IF _searchType = 'CUSTOMER_SEARCH' OR _searchType = 'GROUP_SEARCH'THEN
          PREPARE stmt FROM @queryStatement2; EXECUTE stmt;
     END If;
    DEALLOCATE PREPARE stmt;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS customer_basic_info_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_basic_info_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SELECT accountLockoutThreshold,accountLockoutTime into @accountLockoutThreshold,@accountLockoutTime from passwordlockoutsettings ;

SELECT 
        `customer`.`UserName` AS `Username`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        CONCAT(IFNULL(`customer`.`FirstName`, ''),
                ' ',
                IFNULL(`customer`.`MiddleName`, ''),
                ' ',
                IFNULL(`customer`.`LastName`, '')) AS `Name`,
        `customer`.`Salutation` AS `Salutation`,
        `customer`.`id` AS `Customer_id`,
        `customer`.`Ssn` AS `SSN`,
        `customer`.`createdts` AS `CustomerSince`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`isEnrolledFromSpotlight` AS `isEnrolledFromSpotlight`,
        IF(`customer`.`Status_id`= 'SID_CUS_SUSPENDED',`customer`.`Status_id`, 
        IF(`customer`.`lockCount`+1 >= @accountLockoutThreshold, 'SID_CUS_LOCKED',`customer`.`Status_id`)) AS `CustomerStatus_id`,
        `customerstatus`.`Description` AS `CustomerStatus_name`,
        `customer`.`MaritalStatus_id` AS `MaritalStatus_id`,
        `maritalstatus`.`Description` AS `MaritalStatus_name`,
        `customer`.`SpouseName` AS `SpouseName`,
        `customer`.`DrivingLicenseNumber` AS `DrivingLicenseNumber`,
        `customer`.`lockedOn` AS `lockedOn`,
        `customer`.`lockCount` AS `lockCount`,
        `customer`.`EmployementStatus_id` AS `EmployementStatus_id`,
        `employementstatus`.`Description` AS `EmployementStatus_name`,
        (SELECT 
                GROUP_CONCAT(`customerflagstatus`.`Status_id`, ' '
                        SEPARATOR ',')
            FROM
                `customerflagstatus`
            WHERE
                (`customerflagstatus`.`Customer_id` = `customer`.`id`)) AS `CustomerFlag_ids`,
        (SELECT 
                GROUP_CONCAT(`status`.`Description`, ' '
                        SEPARATOR ',')
            FROM
                `status`
            WHERE
                `status`.`id` IN (SELECT 
                        `customerflagstatus`.`Status_id`
                    FROM
                        `customerflagstatus`
                    WHERE
                        (`customerflagstatus`.`Customer_id` = `customer`.`id`))) AS `CustomerFlag`,
        `customer`.`IsEnrolledForOlb` AS `IsEnrolledForOlb`,
        `customer`.`isEnrolled` AS `isEnrolled`,
        `customer`.`IsStaffMember` AS `IsStaffMember`,
        `customer`.`Location_id` AS `Branch_id`,
        `location`.`Name` AS `Branch_name`,
        `location`.`Code` AS `Branch_code`,
        `customer`.`IsOlbAllowed` AS `IsOlbAllowed`,
        `customer`.`IsAssistConsented` AS `IsAssistConsented`,
        `customer`.`isEagreementSigned` AS `isEagreementSigned`,
	`customer`.`isCombinedUser` AS `isCombinedUser`,
	IFNULL(`customer`.`combinedUserId`, '') AS `combinedUserId`,	
        IF((`customer`.`isCombinedUser` = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            `customer`.`CustomerType_id`) AS `CustomerType_id`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail Banking,Business Banking',
            `customertype`.`Name`) AS `CustomerType_Name`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail and Business Banking User',
            `customertype`.`Description`) AS `CustomerType_Description`,        
        
    (SELECT `membergroup`.`id` FROM `membergroup` WHERE `id` in (SELECT `Group_id` from `customergroup` where `customergroup`.`Customer_id`= _customerId) AND `Type_id` = 'TYPE_ID_BUSINESS' ) AS `Customer_RoleId`,
    (SELECT `membergroup`.`Name` FROM `membergroup` WHERE `id` in (SELECT `Group_id` from `customergroup` where `customergroup`.`Customer_id`= _customerId) AND `Type_id` = 'TYPE_ID_BUSINESS' ) AS `Customer_Role`,
	(SELECT `membergroup`.`isEAgreementActive` FROM `membergroup` WHERE `id` in (SELECT `Group_id` from `customergroup` where `customergroup`.`Customer_id`= _customerId) AND `Type_id` = 'TYPE_ID_BUSINESS' ) AS `isEAgreementRequired`,
		`customer`.`Organization_Id` AS `organisation_id`,
        `organisation`.`BusinessType_id` AS `BusinessType_id`,
        `businesstype`.`name` AS `BusinessType`,        
        `organisation`.`Name` AS `organisation_name`,
	(SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND  `customercommunication`.`isPrimary` = 1 AND `customercommunication`.`isTypeBusiness` IS NULL
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL'  AND  `customercommunication`.`isPrimary` = 1 AND `customercommunication`.`isTypeBusiness` IS NULL
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryEmailAddress`,                
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND `customercommunication`.`isTypeBusiness` = '1'
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `BusinessPrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL' AND `customercommunication`.`isTypeBusiness` = '1'
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `BusinessPrimaryEmailAddress`,
        `customer`.`DocumentsSubmitted` AS `DocumentsSubmitted`,
        `customer`.`ApplicantChannel` AS `ApplicantChannel`,
        `customer`.`Product` AS `Product`,
        `customer`.`Reason` AS `Reason`,
        @accountLockoutTime AS accountLockoutTime
    FROM
        (((((((`customer`
        LEFT JOIN `location` ON ((`customer`.`Location_id` = `location`.`id`)))
        LEFT JOIN `organisation` ON ((`customer`.`Organization_Id` = `organisation`.`id`)))
        LEFT JOIN `businesstype` ON ((`businesstype`.`id` = `organisation`.`BusinessType_id`)))
        JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
        LEFT JOIN `status` `customerstatus` ON ((`customer`.`Status_id` = `customerstatus`.`id`)))
        LEFT JOIN `status` `maritalstatus` ON ((`customer`.`MaritalStatus_id` = `maritalstatus`.`id`)))
        LEFT JOIN `status` `employementstatus` ON ((`customer`.`EmployementStatus_id` = `employementstatus`.`id`)))
    WHERE `customer`.`id` = _customerId
    LIMIT 1;

END$$
DELIMITER ;

ALTER TABLE `organisationcommunication` ADD INDEX `IDX_orgcommunication_value` (`Value` ASC);


DROP procedure IF EXISTS `organisation_details_get_proc`;

DELIMITER $$
CREATE PROCEDURE `organisation_details_get_proc`(
IN _orgId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _orgName TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _orgEmail TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _orgTaxId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DECLARE orgIdCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT "" ;
IF _orgName != ''then
   SET orgIdCSV = (SELECT group_concat(DISTINCT organisation.id SEPARATOR ",") FROM organisation WHERE Name LIKE concat('%',_orgName,'%'));
END IF;

IF _orgEmail != '' then
  IF orgIdCSV = '' THEN
      SET orgIdCSV = (SELECT group_concat(DISTINCT organisationcommunication.Organization_id SEPARATOR ",") FROM organisationcommunication WHERE Value LIKE concat('%',_orgEmail,'%') AND Type_id = 'COMM_TYPE_EMAIL');
  ELSE
     SET orgIdCSV = concat(orgIdCSV,',',(SELECT group_concat(DISTINCT organisationcommunication.Organization_id SEPARATOR ",") FROM organisationcommunication WHERE Value LIKE concat('%',_orgEmail,'%') AND Type_id = 'COMM_TYPE_EMAIL' 
                    AND FIND_IN_SET(organisationcommunication.Organization_id,orgIdCSV)));
  END IF;
END IF;

IF _orgTaxId  != '' then
  IF orgIdCSV = '' THEN
     SET orgIdCSV = (SELECT group_concat(DISTINCT organisationmembership.Organization_id SEPARATOR ",") FROM organisationmembership WHERE Taxid LIKE concat('%',_orgTaxId,'%'));
  ELSE
     SET orgIdCSV = concat(orgIdCSV,',',(SELECT group_concat(DISTINCT organisationmembership.Organization_id SEPARATOR ",") FROM organisationmembership WHERE Taxid LIKE concat('%',_orgTaxId,'%') 
                    AND FIND_IN_SET(organisationmembership.Organization_id,orgIdCSV)));
  END IF;
END IF;

IF _orgId != '' then
  SET orgIdCSV = _orgId;
END IF;

SELECT DISTINCT `organisation`.`id` AS `id`,
        `organisation`.`Name` AS `Name`,
        `organisation`.`Type_Id` AS `TypeId`,
        `organisation`.`StatusId` AS `orgStatus`,
        `organisation`.`FaxId` AS `faxId`,
        `phone`.`value` AS `Phone`,
        `email`.`value` AS `Email`,
        `address`.`cityName` AS `cityName`,
        `address`.`addressLine1` AS `addressLine1`,
        `address`.`addressLine2` AS `addressLine2`,
        `address`.`zipCode` AS `zipCode`,
        `address`.`id` AS `addressId`,
        `address`.`state` AS `state`,
        `address`.`country` AS `country`,
        `organisationaddress`.`IsPrimary` AS `IsPrimary`,
        `businesstype`.`name` AS `businessType`,
        `businesstype`.`id` AS `businessTypeId`
    FROM
      (((`organisation`
        LEFT JOIN `organisationcommunication` phone ON ((FIND_IN_SET(organisation.id ,orgIdCSV)) AND phone.Type_id= 'COMM_TYPE_PHONE' AND (`organisation`.`id` = `phone`.`Organization_id`))
		LEFT JOIN `organisationcommunication` email ON ((FIND_IN_SET(organisation.id ,orgIdCSV)) AND email.Type_id= 'COMM_TYPE_EMAIL' AND (`organisation`.`id` = `email`.`Organization_id`))
        LEFT JOIN `organisationaddress` ON ((FIND_IN_SET(organisationaddress.Organization_id ,orgIdCSV)) AND (`organisation`.`id` = `organisationaddress`.`Organization_id`)))
        LEFT JOIN `address` ON ((`organisationaddress`.`Address_id` = `address`.`id`)))
        LEFT JOIN `businesstype` ON ((`businesstype`.`id` = `organisation`.`BusinessType_id`))) where FIND_IN_SET(organisation.id ,orgIdCSV);
END$$

DELIMITER ;




DROP procedure IF EXISTS `organisation_actions_create_proc`;

DELIMITER $$

CREATE PROCEDURE `organisation_actions_create_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT "";
 DECLARE entryStatus INTEGER DEFAULT 0 ;

 DECLARE actions CURSOR 
		FOR (select id from featureaction where FIND_IN_SET(Feature_id ,@features_list) COLLATE utf8_general_ci );
DECLARE limits CURSOR 
		FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
 
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _organisationType) 
                     where (FIND_IN_SET(feature.id,_features)) OR feature.isPrimary = '1' OR feature.isPrimary = true) ; 

SET @features_list = IF(@features_list is null, '', @features_list) COLLATE utf8_general_ci;

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
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id  = featureActionId COLLATE utf8_general_ci 
               AND LimitType_id = limitId COLLATE utf8_general_ci );
	       
			   SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO organisationactionlimit(id,Organisation_id,Action_id,LimitType_id,value) VALUES
		        (@id,_organisationId,featureActionId,limitId,@limitvalue);
                SET entryStatus = 1;
			   ITERATE  getlimit;
			END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @orgIds = (SELECT group_concat(DISTINCT organisationactionlimit.Organisation_id SEPARATOR ",") from organisationactionlimit
                  where Organisation_id = _organisationId AND Action_id = featureActionId);
                 IF isnull(@orgIds) OR @orgIds = "" THEN
                     SET @id = (SELECT LEFT(UUID(), 50));
					 INSERT IGNORE INTO organisationactionlimit(id,Organisation_id,Action_id) VALUES
					  (@id,_organisationId,featureActionId);
				 END IF;
            END IF;
			set actionslist = CONCAT(featureActionId,",",actionslist) COLLATE utf8_general_ci;
			ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;
  
SET actionslist = (select SUBSTRING(actionslist FROM 1 FOR (CHAR_LENGTH(actionslist)-1)));

select actionslist;

END$$

DELIMITER ;


DROP procedure IF EXISTS `organisation_features_create_proc`;

DELIMITER $$

CREATE PROCEDURE `organisation_features_create_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureId varchar(255) DEFAULT "";
 DECLARE featuresList TEXT DEFAULT "";

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features)) OR feature.isPrimary = '1' OR feature.isPrimary = true) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
            SET @id = (SELECT LEFT(UUID(), 50));
			INSERT IGNORE INTO organisationfeatures(id,organisationId,featureId) VALUES
		    (@id,_organisationId,featureId);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

END$$

DELIMITER ;



DROP procedure IF EXISTS `organisation_features_suspend_proc`;

DELIMITER $$

CREATE PROCEDURE `organisation_features_suspend_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureId varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT "";
 DECLARE featuresList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT "";

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
        
UPDATE organisationfeatures SET featureStatus = NULL WHERE organisationId = _organisationId;

SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features))) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
			UPDATE organisationfeatures set featureStatus = "SID_FEATURE_SUSPENDED" WHERE (organisationfeatures.organisationId COLLATE utf8_general_ci =_organisationId AND 
            organisationfeatures.featureId COLLATE utf8_general_ci = featureId COLLATE utf8_general_ci);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `account_action_approvers_proc`;

delimiter $$

CREATE  PROCEDURE `account_action_approvers_proc`(
in _organizationId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 1000000;

SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",") from (`customeraction`)
						where 
							`customeraction`.`isAllowed` = '0'
						and `customeraction`.`Action_id` = _approvalActionList
                        and `customeraction`.`Account_id` = _accountId);

SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);

SELECT 
	DISTINCT (`customer`.`id` ) AS id , (`customer`.`username`) AS userName , (`membergroup`.`Name`) AS groupId,
										(`customer`.`FirstName`) AS firstName , (`customer`.`LastName`) AS lastName
from 
	(`customer`
LEFT JOIN `organisation` ON (`organisation`.`id` = `customer`.`Organization_Id`)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
LEFT JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
LEFT JOIN `organisationfeatures` ON (`organisationfeatures`.`organisationId` = `customer`.`Organization_Id`))
	where 
		`organisation`.`id` = _organizationId
        and `membergroup`.`Type_id` = 'TYPE_ID_BUSINESS'
		and `organisationfeatures`.`featureId` = _featureId
		and (`organisationfeatures`.`featureStatus` is null or LENGTH(`organisationfeatures`.`featureStatus`) = 0)
		and `customeraccounts`.`Account_id` = _accountId
		and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
        and `customer`.`id` not in (@customerIdList)
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0;       
END$$

DELIMITER ;


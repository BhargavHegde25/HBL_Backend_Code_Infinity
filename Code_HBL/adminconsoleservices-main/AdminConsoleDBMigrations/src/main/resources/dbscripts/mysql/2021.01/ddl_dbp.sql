INSERT INTO `statustype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('STID_CONTRACTSTATUS', 'Contract status', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');

INSERT INTO `status` (`id`, `Type_id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('SID_CONTRACT_ACTIVE', 'STID_CONTRACTSTATUS', 'contract is active for use', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');
INSERT INTO `status` (`id`, `Type_id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('SID_CONTRACT_PENDING', 'STID_CONTRACTSTATUS', 'contract is pending for approval', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');
INSERT INTO `status` (`id`, `Type_id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('SID_CONTRACT_REJECTED', 'STID_CONTRACTSTATUS', 'contract is rejected', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');

CREATE TABLE `alertrecipienttype` (
  `id` int(2) NOT NULL,
  `name` VARCHAR(20) NOT NULL,
  `isaccountlevel` TINYINT NULL DEFAULT '0',
  `servicename` VARCHAR(50) NULL DEFAULT NULL,
  `operationname` VARCHAR(50) NULL DEFAULT NULL,
  `inputparamsmapping` VARCHAR(1000) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

Alter table `alertsubtype` add COLUMN `recipienttype` int(2) DEFAULT 1 AFTER `defaultFrequencyTime`;

ALTER TABLE `alertsubtypecustomertype` DROP FOREIGN KEY `FK_alertsubtypecustomertype_customertype`;

ALTER TABLE application ADD customerCreationMode ENUM ('WITHOUT-RECORD', 'WITH-RECORD', 'HYBRID') DEFAULT 'WITHOUT-RECORD' NOT NULL;

ALTER TABLE `membership` 
ADD COLUMN `firstName` VARCHAR(45) NULL DEFAULT NULL AFTER `name`,
ADD COLUMN `lastName` VARCHAR(45) NULL DEFAULT NULL AFTER `firstName`,
ADD COLUMN `dateOfBirth` DATE NULL DEFAULT NULL AFTER `lastName`,
ADD COLUMN `ssn` VARCHAR(45) NULL DEFAULT NULL AFTER `dateOfBirth`;


CREATE TABLE `membershiprelation` (
  `id` INT NOT NULL,
  `membershipId` VARCHAR(45) NULL DEFAULT NULL,
  `relatedMebershipId` VARCHAR(45) NULL DEFAULT NULL,
  `relationshipId` VARCHAR(45) NULL DEFAULT NULL,
  `relationshipName` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  INDEX `membershipId` (`membershipId` ASC));

DROP PROCEDURE IF EXISTS `membership_customer_search_proc`;

delimiter $$

CREATE PROCEDURE `membership_customer_search_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _name varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _status varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _city varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _zipCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

		SET @select_statement = concat("select membership.id , membership.industry,membership.name , membership.firstName , membership.lastName , membership.phone , membership.isBusinessType , membership.taxId , membership.faxId , membership.email , address.addressLine1 ,address.addressLine2 , address.cityName , address.country , address.zipCode , address.state from membership LEFT JOIN address on (membership.addressId = address.id) where membership.id is not null");

	IF(_id != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.id = ",quote(_id));
    END IF;
    
    IF(_name != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.name like '%",_name,"%'");
    END IF;
    
    IF(_email != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.email = ",quote(_email));
    END IF;
    
    IF(_phone != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.phone = ",quote(_phone));
    END IF;
    
    IF(_dateOfBirth != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.dateOfBirth = ",quote(_dateOfBirth));
    END IF;
		
	SET @address_select_statement = "";
	IF(_city != "" OR _country!= "" OR _zipCode!= "") THEN
    
		SET @address_select_statement = concat("select address.id from address where address.id is not null");
        IF(_city != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.cityName = ",quote(_city));
        END IF;
        
        IF(_country != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.country = ",quote(_country));
        END IF;
        
        IF(_zipCode != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.zipCode = ",quote(_zipCode));
        END IF;
        
    END IF;
    
	IF(@address_select_statement != "") THEN 
		SET @select_statement = concat(@select_statement , " and membership.addressId in (", @address_select_statement , ")");
    END IF;
    
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
      
END$$

DELIMITER;

DROP TABLE IF EXISTS `market`;
CREATE TABLE `market` (
  `id` varchar(20) NOT NULL,
  `name` varchar(50) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `currencymarketrates`;
CREATE TABLE `currencymarketrates` (
  `id` int(11) NOT NULL,
  `baseCurrencyCode` varchar(10) NOT NULL,
  `quoteCurrencyCode` varchar(10) NOT NULL,
  `marketId` varchar(20) NOT NULL,
  `buyRate` varchar(50) NOT NULL,
  `sellRate` varchar(50) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_currency_code_rates_idx` (`baseCurrencyCode`),
  KEY `FK_currency_code_recentquote` (`quoteCurrencyCode`),
  KEY `FK_market_id` (`marketId`),
  CONSTRAINT `FK_currency_code_recent_base` FOREIGN KEY (`baseCurrencyCode`) REFERENCES `currency` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_currency_code_recentquote` FOREIGN KEY (`quoteCurrencyCode`) REFERENCES `currency` (`code`)  ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_market_id` FOREIGN KEY (`marketId`) REFERENCES `market` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `popularcurrencies`;
CREATE TABLE `popularcurrencies` (
  `id` int(11) NOT NULL,
  `baseCurrencyCode` varchar(10) NOT NULL,
  `quoteCurrencyCode` varchar(10) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_currency_code_idx` (`baseCurrencyCode`),
  KEY `FK_currency_code_quote` (`quoteCurrencyCode`),
  CONSTRAINT `FK_currency_code_base` FOREIGN KEY (`baseCurrencyCode`) REFERENCES `currency` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_currency_code_quote` FOREIGN KEY (`quoteCurrencyCode`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `recentcurrencies`;
CREATE TABLE `recentcurrencies` (
  `id` varchar(60) NOT NULL ,
  `customerId` varchar(50) NOT NULL,
  `quoteCurrencyCode` varchar(10) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_customer_id_idx` (`customerId`),
  KEY `FK_currency_code_recent` (`quoteCurrencyCode`),
  CONSTRAINT `FK_currency_code_recent` FOREIGN KEY (`quoteCurrencyCode`) REFERENCES `currency` (`code`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_customer_id_recent` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `countrybasecurrency`;
CREATE TABLE `countrybasecurrency` (
  `id` int(11) NOT NULL,
  `countryCode` varchar(50) NOT NULL,
  `baseCurrencyCode` varchar(10) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_country_id_idx` (`countryCode`),
  KEY `FK_currency_code` (`baseCurrencyCode`),
  CONSTRAINT `FK_country_id` FOREIGN KEY (`countryCode`) REFERENCES `country` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_currency_code` FOREIGN KEY (`baseCurrencyCode`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP procedure IF EXISTS `update_user_recent_currency`;

DELIMITER $$

CREATE PROCEDURE `update_user_recent_currency`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _currencyCode VARCHAR(10) CHARACTER SET UTF8 COLLATE utf8_general_ci
)

BEGIN

DECLARE rowWithGivenCustomerAndCurrency INT DEFAULT 0;
DECLARE lengthOfRecentCurrencies INT DEFAULT 0;
DECLARE recentCurrencyToDelete VARCHAR(60);

SELECT COUNT(*) INTO rowWithGivenCustomerAndCurrency FROM 
recentcurrencies rc WHERE rc.customerId = _customerId AND rc.quoteCurrencyCode = _currencyCode;

SELECT COUNT(*) INTO lengthOfRecentCurrencies FROM 
recentcurrencies rc WHERE rc.customerId = _customerId ;

SELECT  recentcurrencies.id INTO recentCurrencyToDelete FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId
													ORDER BY recentcurrencies.createdts asc limit 1;
IF rowWithGivenCustomerAndCurrency >= 1 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId AND recentcurrencies.quoteCurrencyCode = _currencyCode;
ELSEIF lengthOfRecentCurrencies >= 5 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.id = recentCurrencyToDelete COLLATE utf8_general_ci;
END IF;

INSERT INTO recentcurrencies (`id`, `customerId`, `quoteCurrencyCode`) VALUES (concat( _currencyCode, _customerId ), _customerId, _currencyCode);
END$$

DELIMITER ;

DROP procedure IF EXISTS `forex_proc_get`;

DELIMITER $$
CREATE PROCEDURE `forex_proc_get`(IN read_query TEXT)

BEGIN
SET @stmt := CONCAT('', read_query);
PREPARE stmt FROM @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;



ALTER TABLE backendidentifier ADD contractId varchar(20) null AFTER identifier_name;
ALTER TABLE backendidentifier ADD contractTypeId varchar(20) null AFTER contractId;

ALTER TABLE customeraccounts ADD contractId varchar(20) null AFTER IsOrgAccountUnLinked;
ALTER TABLE customeraccounts ADD coreCustomerId varchar(20) null AFTER contractId;
ALTER TABLE customeraccounts ADD isBusinessAccount varchar(20) null AFTER coreCustomerId;
ALTER TABLE customeraccounts ADD accountType varchar(20) NULL;	
ALTER TABLE customeraccounts ADD COLUMN email varchar(50) DEFAULT NULL NULL;
ALTER TABLE customeraccounts ADD COLUMN `EStatementmentEnable` tinyint(1) DEFAULT 0 NULL;


DROP TABLE IF EXISTS `contractcustomers`;

CREATE TABLE `contractcustomers` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) DEFAULT NULL,
  `customerId` varchar(50) DEFAULT NULL,
  `coreCustomerId` varchar(45) DEFAULT NULL,
  `isAdmin` bit(1) NOT NULL DEFAULT b'0',
  `isOwner` bit(1) NOT NULL DEFAULT b'0',
  `isPrimary` bit(1) DEFAULT b'0',
  `isAuthSignatory` bit(1) DEFAULT b'0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT NULL,
  `lastmodifiedts` timestamp NULL DEFAULT NULL,
  `synctimestamp` timestamp NULL DEFAULT NULL,
  `softdeleteflag` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_contractcustomer_customerid_idx` (`customerId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `customeraction` 
ADD COLUMN `contractId` VARCHAR(45) NULL DEFAULT NULL AFTER `Customer_id`,
ADD COLUMN `coreCustomerId` VARCHAR(45) NULL DEFAULT NULL AFTER `contractId`,
ADD COLUMN `policyId` VARCHAR(45) NULL DEFAULT NULL AFTER `isAllowed`,
ADD COLUMN `limitGroupId` VARCHAR(45) NULL AFTER `policyId`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `synctimestamp`);

ALTER TABLE customergroup ADD coreCustomerId varchar(50) null AFTER Customer_id;
ALTER TABLE customergroup ADD contractId varchar(50) null AFTER coreCustomerId;

DROP PROCEDURE IF EXISTS customeractionlimit_save_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customeraction_save_proc`(
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
           	set @query = concat('INSERT INTO customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

CREATE TABLE `contract` (
  `id` varchar(50) NOT NULL,
  `servicedefinitionId` varchar(50) DEFAULT NULL,
  `serviceType` varchar(50) DEFAULT NULL,
  `name` varchar(50) DEFAULT NULL,
  `description` varchar(200) DEFAULT NULL,
  `statusId` varchar(50) NOT NULL DEFAULT 'SID_CONTRACT_PENDING',
  `faxId` varchar(45) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `rejectedby` varchar(50) DEFAULT NULL,
  `rejectedts` timestamp NULL DEFAULT NULL,
  `rejectedReason` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `Name_UNIQUE` (`name`),
  KEY `FK_contract_status_StatusId_idx` (`statusId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `contractaccounts` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `accountId` varchar(50) NOT NULL,
  `accountName` varchar(50) DEFAULT NULL,
  `typeId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `ownerType` varchar(50) DEFAULT NULL,
  `statusDesc` varchar(50) DEFAULT 'Active',
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` varchar(50) DEFAULT NULL,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `accountId_UNIQUE` (`accountId`),
  KEY `FK_contractaccounts_contract_contractId_idx` (`contractId`),
  CONSTRAINT `FK_contractaccounts_contract_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `contractactionlimit` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `policyId` varchar(50) DEFAULT NULL,
  `featureId` varchar(50) NOT NULL,
  `actionId` varchar(255) NOT NULL,
  `limitGroupId` varchar(45) DEFAULT NULL,
  `limitTypeId` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_contractactionlimit_contract_contractId_idx` (`contractId`),
  CONSTRAINT `FK_contractactionlimit_contract_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `contractaddress` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `contractId` varchar(50) NOT NULL,
  `addressId` varchar(45) NOT NULL,
  `durationOfStay` varchar(45) DEFAULT NULL,
  `isPrimary` bit(1) NOT NULL DEFAULT b'0',
  `createdby` varchar(45) DEFAULT NULL,
  `modifiedby` varchar(45) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `typeId` varchar(45) DEFAULT NULL,
  `synctimestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_contractaddress_contract_contractID_idx` (`contractId`),
  KEY `FK_contractaddress_address_addressId_idx` (`addressId`),
  CONSTRAINT `FK_contractaddress_address_addressId` FOREIGN KEY (`addressId`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_contractaddress_contract_contractID` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;


CREATE TABLE `contractcommunication` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `sequence` int(11) DEFAULT NULL,
  `value` varchar(100) NOT NULL,
  `extension` varchar(45) DEFAULT NULL,
  `phoneCountryCode` varchar(10) DEFAULT NULL,
  `description` varchar(45) DEFAULT NULL,
  `isPreferredContactMethod` bit(1) DEFAULT b'0',
  `preferredContactTime` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT NULL,
  `lastmodifiedts` timestamp NULL DEFAULT NULL,
  `synctimestamp` timestamp NULL DEFAULT NULL,
  `softdeleteflag` bit(1) DEFAULT b'0',
  PRIMARY KEY (`id`),
  KEY `FK_contractcommunication_contractId_idx` (`contractId`),
  CONSTRAINT `FK_contractcommunication_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24 DEFAULT CHARSET=utf8;


CREATE TABLE `contractcorecustomers` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `taxId` varchar(50) DEFAULT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `coreCustomerName` varchar(50) NOT NULL,
  `isPrimary` tinyint(1) NOT NULL DEFAULT '0',
  `isBusiness` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_contractcorecustomers_contract_contractId_idx` (`contractId`),
  CONSTRAINT `FK_contractcorecustomers_contract_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `contractfeatures` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `featureId` varchar(255) NOT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_contractfeatures_contract_contractId_idx` (`contractId`),
  CONSTRAINT `FK_contractfeatures_contract_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP procedure IF EXISTS `contract_features_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_features_create_proc`(
IN _features MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _defaultActionsEnabled VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureId varchar(255) DEFAULT "";
 DECLARE featuresList TEXT DEFAULT "";
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE limitId varchar(255) DEFAULT "";
 DECLARE tempLimitValue varchar(255) DEFAULT "";

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id COLLATE utf8_general_ci,@features_list COLLATE utf8_general_ci));
DECLARE actions CURSOR 
		FOR (select id from featureaction where FIND_IN_SET(featureaction.id COLLATE utf8_general_ci,@validServicedefinitionActions COLLATE utf8_general_ci));
 DECLARE limits CURSOR 
		FOR (select LimitType_id from actionlimit where actionlimit.Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
        
 SET SESSION group_concat_max_len = 100000000;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _serviceTypeId) 
                      where (FIND_IN_SET(feature.id COLLATE utf8_general_ci,_features COLLATE utf8_general_ci)) AND feature.Status_id = 'SID_FEATURE_ACTIVE') ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
            SET @id = (SELECT LEFT(UUID(), 50));
			INSERT INTO contractfeatures(id,contractId,coreCustomerId,featureId) VALUES
		    (@id,_contractId,_customerId,featureId);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

SET finished = 0;

SET @validFIActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE
                        FIND_IN_SET(featureaction.Feature_id COLLATE utf8_general_ci,featuresList COLLATE utf8_general_ci) AND
                        featureaction.status = 'SID_ACTION_ACTIVE');

SET @servicedefinitionId = (SELECT servicedefinitionId from contract WHERE id  = _contractId);

SET @validServicedefinitionActions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE
                                    servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                    FIND_IN_SET(servicedefinitionactionlimit.actionId COLLATE utf8_general_ci,@validFIActions COLLATE utf8_general_ci));
                                    
If !ISNULL(_defaultActionsEnabled) AND _defaultActionsEnabled = 'true' THEN

OPEN actions; 
getAction: LOOP
        SET entryStatus = 0;
        FETCH actions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
        IF finished = 1 THEN 
            LEAVE getAction;
	    else
            OPEN limits; 
			getlimit: LOOP
            FETCH limits INTO limitId;
			IF finished = 1 THEN 
               LEAVE getlimit;
			else
               
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci 
               AND LimitType_id COLLATE utf8_general_ci = limitId COLLATE utf8_general_ci);
			   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE 
                                                  servicedefinitionactionlimit.actionId COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci AND
                                                  servicedefinitionactionlimit.limitTypeId COLLATE utf8_general_ci = limitId COLLATE utf8_general_ci AND
                                                   servicedefinitionactionlimit.serviceDefinitionId COLLATE utf8_general_ci = @servicedefinitionId COLLATE utf8_general_ci);
			  SET tempLimitValue = LEAST(@limitvalue,@limitATServiceDefinition);
              
              if(!isnull(tempLimitValue) AND tempLimitValue !='') THEN
                 SET @limitvalue = tempLimitValue;
              END IF;
				
			   SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId,limitTypeId,value) VALUES
		        (@id,_contractId,_customerId,@featureId,featureActionId,limitId,@limitvalue);
                
                SET entryStatus = 1;
			   ITERATE  getlimit;
			END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId) VALUES
		        (@id,_contractId,_customerId,@featureId,featureActionId);
            END IF;
			ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;

END IF;


END$$

DELIMITER ;
;



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
            set @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            
             SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',5 ),'\"');
             
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
					     SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId) VALUES (',@recordsDataWithoutLimits,');');
				    ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
					       SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId,limitTypeId,value) VALUES (',@recordsData,');');
					END IF;
				END IF;
			 END IF;
            END IF;
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `membership_relative_customer_get_proc`;

DELIMITER $$

CREATE PROCEDURE `membership_relative_customer_get_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    select  membership.id , 
			membership.name , 
            membership.firstName , 
            membership.lastName , 
            membership.phone , 
            membership.email , 
			membership.dateOfBirth, 
			membership.taxId,
			membership.faxId,
            membership.industry, 
			membership.isBusinessType,
            membershiprelation.relationshipId,
            membershiprelation.relationshipName,
            address.addressLine1 ,
			address.addressLine2 , 
            address.cityName , 
            address.country , 
            address.zipCode , 
            address.state 
			from 
            membership 
			LEFT JOIN address on (membership.addressId = address.id)
            JOIN membershiprelation on (membershiprelation.relatedMebershipId COLLATE utf8_general_ci = membership.id)
			where membership.id in (select membershiprelation.relatedMebershipId COLLATE utf8_general_ci from membershiprelation where membershiprelation.membershipId COLLATE utf8_general_ci= _id)
            and membershiprelation.membershipId COLLATE utf8_general_ci = _id;
      
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `contract_search_proc`;

DELIMITER $$

CREATE PROCEDURE `contract_search_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _contractName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phoneCountryCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phoneNumber varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET @isWhereAppened = false;
    SET @shouldAndAppend = false;
	SET @select_statement = CONCAT("SELECT contract.id as contractId , contract.name as contractName , contract.servicedefinitionId as serviceDefinitionId , servicedefinition.name as serviceDefinitionName, emailcommunication.value as email , contractcorecustomers.coreCustomerName as coreCustomerName FROM contract  LEFT JOIN servicedefinition ON ( servicedefinition.id = contract.servicedefinitionId) LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id) LEFT JOIN contractcommunication emailcommunication ON (emailcommunication.contractId = contract.id and emailcommunication.typeId = 'COMM_TYPE_EMAIL') LEFT JOIN contractcommunication phonecommunication ON (phonecommunication.contractId = contract.id and phonecommunication.typeId = 'COMM_TYPE_PHONE')
    LEFT JOIN contractaddress ON (contractaddress.contractId = contract.id)  LEFT JOIN address ON (address.id = contractaddress.addressId)");
	
    IF(_contractId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " where contract.id = ",quote(_contractId));
        SET @isWhereAppened = true;
        SET @shouldAndAppend = true;
    END IF;
	
    IF(_contractName != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
		SET _contractName = REPLACE(_contractName , "'","''");
        SET @select_statement = CONCAT(@select_statement , " contract.name like '%",_contractName,"%'");
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_serviceDefinitionId != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " contract.servicedefinitionId = ",quote(_serviceDefinitionId));
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_coreCustomerId != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " contractcorecustomers.coreCustomerId =",quote(_coreCustomerId));
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_coreCustomerName != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " contractcorecustomers.coreCustomerName like '%",_coreCustomerName,"%'");
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_country != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " address.country =",quote(_country));
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_email != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " emailcommunication.value =",quote(_email));
        SET @shouldAndAppend = true;
    END IF;
    
    IF(_phoneCountryCode != '' and _phoneNumber!= '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where ");
            SET @isWhereAppened = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
        SET @select_statement = CONCAT(@select_statement , " phonecommunication.value = ",quote(_phoneNumber)," and phonecommunication.phoneCountryCode = ",quote(_phoneCountryCode));
        SET @shouldAndAppend = true;
    END IF;
    
    SET @select_statement = CONCAT(@select_statement , ";");
    
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `get_associated_contractaccounts_proc`;

DELIMITER $$

CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));

	select @accountIdList As accountIdList;

END$$

DELIMITER;
																								

DROP PROCEDURE IF EXISTS customer_action_limits_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE customer_action_limits_delete_proc(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM customeraction where customeraction.Customer_id = _customerId;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customer_accounts_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE customer_accounts_delete_proc(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM customeraccounts where customeraccounts.Customer_id = _customerId;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customer_group_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE customer_group_delete_proc(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM customergroup where customergroup.Customer_id = _customerId;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customer_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE customer_contract_delete_proc(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM contractcustomers where contractcustomers.customerId = _customerId;
END$$
DELIMITER ;

ALTER TABLE customergroup DROP FOREIGN KEY `FK_CustomerGroup_Group`;

DELIMITER $$
CREATE PROCEDURE `get_validcorecustomerslist_proc`(
IN _coreCustomersCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @list = (SELECT _coreCustomersCSV);
SET @validcorecustomersCSV = '';

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    SET @customers = (SELECT group_concat(id SEPARATOR ",") from contractcorecustomers WHERE (coreCustomerId=@value));
    IF ISNULL(@customers)>0 OR @customers="" THEN 
        IF @validcorecustomersCSV='' THEN
           SET @validcorecustomersCSV = @value;
		ELSE 
           SET @validcorecustomersCSV = CONCAT(@validcorecustomersCSV,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

select @validcorecustomersCSV As validCustomers;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomers_delete_proc`(
IN _contractCustomersList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

DELETE FROM `customergroup` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);

DELETE FROM `customeraction` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);
    
DELETE FROM `customeraccounts` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);
    
DELETE FROM `contractcustomers` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);

DELETE FROM `contractcorecustomers` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);
    
DELETE FROM `contractaccounts` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);
    
DELETE FROM `contractfeatures` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);
    
DELETE FROM `contractactionlimit` 
	WHERE  contractId = _contractId AND FIND_IN_SET(coreCustomerId,_contractCustomersList);

END$$
DELIMITER ;

DELIMITER $$
CREATE  PROCEDURE `contract_communication_address_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

DELETE FROM `contractcommunication` WHERE  contractId = _contractId ;
    
DELETE FROM `contractaddress` WHERE  contractId = _contractId ;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomer_details_get_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @customerAccounts = (SELECT group_concat(accountId SEPARATOR ",") from contractaccounts WHERE (contractId = _contractId AND coreCustomerId=_coreCustomerId));

SET @customerFeatures = (SELECT group_concat(featureId SEPARATOR ",") from contractfeatures WHERE (contractId = _contractId AND coreCustomerId=_coreCustomerId));

SET @customerActions = (SELECT group_concat(actionId SEPARATOR ",") from contractactionlimit WHERE (contractId = _contractId AND coreCustomerId=_coreCustomerId));

select @customerAccounts As coreCustomerAccounts;
select @customerFeatures As coreCustomerFeatures;
select @customerActions As coreCustomerActions;

END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomer_accounts_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM `customeraction` 
	WHERE  contractId = _contractId AND FIND_IN_SET(Account_id,_accountsCSV) AND coreCustomerId=_coreCustomerId ; 
    
DELETE FROM `customeraccounts` 
	WHERE  contractId = _contractId AND FIND_IN_SET(Account_id,_accountsCSV) AND coreCustomerId=_coreCustomerId ;

DELETE FROM `contractaccounts` 
	WHERE  contractId = _contractId AND FIND_IN_SET(accountId,_accountsCSV) AND coreCustomerId=_coreCustomerId ;
    
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomer_features_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _featuresCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM `customeraction` 
	WHERE  contractId = _contractId AND FIND_IN_SET(featureId,_featuresCSV) AND coreCustomerId=_coreCustomerId ; 
    
DELETE FROM `contractfeatures` 
	WHERE  contractId = _contractId AND FIND_IN_SET(featureId,_featuresCSV) AND coreCustomerId=_coreCustomerId ;
    
DELETE FROM `contractactionlimit` 
	WHERE  contractId = _contractId AND FIND_IN_SET(featureId,_featuresCSV) AND coreCustomerId=_coreCustomerId ;
    
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomer_actions_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _actionsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM `customeraction` 
	WHERE  contractId = _contractId AND FIND_IN_SET(Action_id,_actionsCSV) AND coreCustomerId = _coreCustomerId ; 
        
DELETE FROM `contractactionlimit` 
	WHERE  contractId = _contractId AND FIND_IN_SET(actionId,_actionsCSV) AND coreCustomerId = _coreCustomerId ;
    
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_restrictive_featureactionlimits_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_restrictive_featureactionlimits_proc`(
	IN _locale varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET @select_statement = '';
    SET @action_select_statement = '';
	SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id ))");
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
    SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
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
	SET @select_statement = CONCAT(@select_statement , " FROM feature
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

ALTER TABLE `customeraction` 
ADD COLUMN `featureId` VARCHAR(45) NULL DEFAULT NULL AFTER `coreCustomerId`;

ALTER TABLE `customeraction` 
DROP FOREIGN KEY `FK_CustomerActionLimit_customertype`;
ALTER TABLE `customeraction` 
DROP INDEX `UNIQUE_customeractionlimit` ;

CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    SQL SECURITY DEFINER
VIEW `customeraccountsview` AS
    SELECT DISTINCT
        `contractcorecustomers`.`coreCustomerId` AS `Membership_id`,
        `contractcorecustomers`.`coreCustomerName` AS `MembershipName`,
        `accounts`.`TaxId` AS `Taxid`,
        `customeraccounts`.`Customer_id` AS `Customer_id`,
        `customeraccounts`.`Customer_id` AS `User_id`,
        `accounts`.`Account_id` AS `Account_id`,
        `contractcorecustomers`.`isBusiness` AS `isBusinessAccount`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`UserName` AS `userName`,
        `accounts`.`CurrencyCode` AS `currencyCode`,
        `accounts`.`AccountHolder` AS `accountHolder`,
        `accounts`.`error` AS `error`,
        `accounts`.`Address` AS `Address`,
        `accounts`.`Scheme` AS `Scheme`,
        `accounts`.`Number` AS `number`,
        `accounts`.`AvailableBalance` AS `availableBalance`,
        `accounts`.`CurrentBalance` AS `currentBalance`,
        `accounts`.`InterestRate` AS `interestRate`,
        `accounts`.`AvailableCredit` AS `availableCredit`,
        `accounts`.`MinimumDue` AS `minimumDue`,
        `accounts`.`DueDate` AS `dueDate`,
        `accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
        `accounts`.`ClosingDate` AS `closingDate`,
        `accounts`.`PaymentTerm` AS `paymentTerm`,
        `accounts`.`OpeningDate` AS `openingDate`,
        `accounts`.`MaturityDate` AS `maturityDate`,
        `accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
        `accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,
        `accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
        `accounts`.`DividendRate` AS `dividendRate`,
        `accounts`.`DividendYTD` AS `dividendYTD`,
        `customeraccounts`.`EStatementmentEnable` AS `eStatementEnable`,
        `customeraccounts`.`IsOrganizationAccount` AS `isOrganizationAccount`,
        `customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,
        `accounts`.`StatusDesc` AS `statusDesc`,
        `accounts`.`NickName` AS `nickName`,
        `accounts`.`OriginalAmount` AS `originalAmount`,
        `accounts`.`OutstandingBalance` AS `outstandingBalance`,
        `accounts`.`PaymentDue` AS `paymentDue`,
        `accounts`.`PaymentMethod` AS `paymentMethod`,
        `accounts`.`SwiftCode` AS `swiftCode`,
        `accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
        `accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
        `accounts`.`RoutingNumber` AS `routingNumber`,
        `accounts`.`SupportBillPay` AS `supportBillPay`,
        `accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
        `accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
        `accounts`.`SupportTransferTo` AS `supportTransferTo`,
        `accounts`.`SupportDeposit` AS `supportDeposit`,
        `accounts`.`UnpaidInterest` AS `unpaidInterest`,
        `accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
        `accounts`.`principalBalance` AS `principalBalance`,
        `accounts`.`PrincipalValue` AS `principalValue`,
        `accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
        `accounts`.`phone` AS `phoneId`,
        `accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
        `accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
        `accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
        `accounts`.`LastPaymentDate` AS `lastPaymentDate`,
        `accounts`.`LastStatementBalance` AS `lastStatementBalance`,
        `accounts`.`LateFeesDue` AS `lateFeesDue`,
        `accounts`.`maturityAmount` AS `maturityAmount`,
        `accounts`.`MaturityOption` AS `maturityOption`,
        `accounts`.`payoffAmount` AS `payoffAmount`,
        `accounts`.`PayOffCharge` AS `payOffCharge`,
        `accounts`.`PendingDeposit` AS `pendingDeposit`,
        `accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
        `accounts`.`JointHolders` AS `jointHolders`,
        `accounts`.`IsPFM` AS `isPFM`,
        `accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
        `accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
        `accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
        `accounts`.`InterestEarned` AS `interestEarned`,
        `accounts`.`CurrentAmountDue` AS `currentAmountDue`,
        `accounts`.`CreditLimit` AS `creditLimit`,
        `accounts`.`CreditCardNumber` AS `creditCardNumber`,
        `accounts`.`BsbNum` AS `bsbNum`,
        `accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
        `accounts`.`BondInterest` AS `bondInterest`,
        `accounts`.`AvailablePoints` AS `availablePoints`,
        `accounts`.`AccountName` AS `accountName`,
        `customeraccounts`.`email` AS `email`,
        `accounts`.`IBAN` AS `IBAN`,
        `accounts`.`adminProductId` AS `adminProductId`,
        `accounts`.`UpdatedBy` AS `UpdatedBy`,
        `accounts`.`LastUpdated` AS `LastUpdated`,
        `accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,
        `bank`.`Description` AS `bankname`,
        `accounts`.`AccountPreference` AS `accountPreference`,
        `accounttype`.`transactionLimit` AS `transactionLimit`,
        `accounttype`.`transferLimit` AS `transferLimit`,
        `accounttype`.`rates` AS `rates`,
        `accounttype`.`termsAndConditions` AS `termsAndConditions`,
        `accounttype`.`TypeDescription` AS `typeDescription`,
        `accounttype`.`supportChecks` AS `supportChecks`,
        `accounttype`.`displayName` AS `displayName`,
        `accounts`.`accountSubType` AS `accountSubType`,
        `accounts`.`description` AS `description`,
        `accounts`.`schemeName` AS `schemeName`,
        `accounts`.`identification` AS `identification`,
        `accounts`.`secondaryIdentification` AS `secondaryIdentification`,
        `accounts`.`servicerSchemeName` AS `servicerSchemeName`,
        `accounts`.`servicerIdentification` AS `servicerIdentification`,
        `accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,
        `accounts`.`dataType` AS `dataType`,
        `accounts`.`dataDateTime` AS `dataDateTime`,
        `accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
        `accounts`.`dataCreditLineType` AS `dataCreditLineType`,
        `accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
        `accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`
    FROM
        ((((`accounts`
        JOIN `customeraccounts` ON ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`)))
        JOIN `accounttype` ON ((`accounts`.`Type_id` = `accounttype`.`TypeID`)))
        LEFT JOIN `contractcorecustomers` ON ((`customeraccounts`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)))
        LEFT JOIN `bank` ON ((`accounts`.`Bank_id` = `bank`.`id`)));

ALTER TABLE `membership` 
ADD COLUMN `status` VARCHAR(45) NULL DEFAULT NULL AFTER `addressId`,
ADD COLUMN `industry` VARCHAR(45) NULL DEFAULT NULL AFTER `status`;


DROP procedure IF EXISTS `contract_users_details_get_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_users_details_get_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _backendType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @customers = (SELECT group_concat(DISTINCT customerId SEPARATOR ",") from contractcustomers WHERE contractId = _contractId);

SELECT distinct customer.id AS customerId,FirstName AS firstName,MiddleName AS middleName, 
 LastName AS lastName, UserName AS userName, Status_id AS statusId, DateOfBirth AS dateOfBirth , Ssn AS Ssn ,
 backendidentifier.BackendId AS primaryCoreCustomerId,
 Value AS Email 
 FROM customer 
 LEFT JOIN customercommunication ON (customer.id = customercommunication.Customer_id AND customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = '1') 
 LEFT JOIN backendidentifier ON (backendidentifier.Customer_id = customer.id AND backendidentifier.BackendType = _backendType)
 WHERE FIND_IN_SET(customer.id ,@customers) ;
END$$

DELIMITER ;
;



ALTER TABLE `application` ADD COLUMN `isSelfApprovalEnabled` BIT(1) NULL DEFAULT b'1' AFTER `customerCreationMode`;


ALTER TABLE `membershiprelation` 
#COLLATE = DEFAULT ,
CHANGE COLUMN `membershipId` `membershipId` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `relatedMebershipId` `relatedMebershipId` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `relationshipId` `relationshipId` VARCHAR(45) NULL DEFAULT NULL ,
CHANGE COLUMN `relationshipName` `relationshipName` VARCHAR(45) NULL DEFAULT NULL,
CHANGE COLUMN `id` `id` VARCHAR(45) NOT NULL ;


ALTER TABLE customrole DROP FOREIGN KEY customrole_ibfk_2;
ALTER TABLE customrole DROP FOREIGN KEY customrole_ibfk_1;
ALTER TABLE customrole MODIFY COLUMN organization_id varchar(50) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;


ALTER TABLE customroleactionlimits ADD contractId varchar(50) NULL;
ALTER TABLE customroleactionlimits ADD coreCustomerId varchar(50) NULL;
ALTER TABLE customroleactionlimits ADD featureId varchar(50) NULL;
ALTER TABLE customroleactionlimits ADD policyId varchar(50) NULL;
ALTER TABLE customroleactionlimits ADD limitGroupId varchar(50) NULL;
ALTER TABLE customroleactionlimits DROP FOREIGN KEY customroleactionlimits_ibfk_2;
ALTER TABLE customroleactionlimits DROP FOREIGN KEY customroleactionlimits_ibfk_3;
ALTER TABLE customroleactionlimits DROP FOREIGN KEY customroleactionlimits_ibfk_4;


DROP PROCEDURE IF EXISTS customrole_actionlimits_create_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customrole_actionlimits_create_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20))
BEGIN
      DELETE FROM customroleactionlimits where customroleactionlimits.customRole_id = _customRoleId;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            set @query = concat('INSERT INTO customroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

ALTER TABLE `membership` 
ADD UNIQUE INDEX `id_UNIQUE` (`id` ASC);
ALTER TABLE `messageattachment` 
DROP FOREIGN KEY `FK_MessageAttachement_Media`;
ALTER TABLE `messageattachment` 
DROP INDEX `IXFK_MessageAttachement_Media` ;

DROP PROCEDURE IF EXISTS customer_action_save_proc;

DELIMITER $$
$$
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
           	set @query = concat('INSERT INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS customrole_actionlimits_create_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customrole_actionlimits_create_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20))
BEGIN
      DELETE FROM customroleactionlimits where customroleactionlimits.customRole_id = _customRoleId;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            set @query = concat('INSERT INTO customroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed, limitGroupId, limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `infinityuser_contractdetails_get_proc`;

delimiter $$

CREATE PROCEDURE `infinityuser_contractdetails_get_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.isPrimary AS isPrimary,
        customergroup.Group_id AS userRole,
        membergroup.name AS userRoleName,
        membergroup.Description AS userRoleDescription,
        "true" AS isAssociated
	FROM
		contract
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
        LEFT JOIN customergroup ON ( customergroup.contractId = contract.id AND customergroup.coreCustomerId = contractcorecustomers.coreCustomerId)
        LEFT JOIN membergroup ON (membergroup.id = customergroup.Group_id)
        LEFT JOIN contractcustomers ON (contractcustomers.contractId = contractcorecustomers.contractId)
	WHERE
		customergroup.Customer_id = _id
        AND contractcustomers.customerId = _id
	UNION
    select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.isPrimary AS isPrimary,
        NULL AS userRole,
        NULL AS userRoleName,
        NULL AS userRoleDescription,
        "false" AS isAssociated
	FROM
		contract
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
	WHERE
		contractcorecustomers.contractId IN ( select contractcustomers.contractId FROM contractcustomers WHERE contractcustomers.customerId = _id);
END$$

DELIMITER;

ALTER TABLE `membershiprelation` 
CHANGE COLUMN `id` `id` VARCHAR(45) NOT NULL ;

ALTER TABLE `customergroup` DROP PRIMARY KEY;
;


DELIMITER $$
CREATE PROCEDURE `useraccounts_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE accountID varchar(255) ;
DECLARE finished INTEGER DEFAULT 0 ;

DECLARE accountData CURSOR FOR (SELECT contractaccounts.accountId FROM contractaccounts WHERE contractId = _contractId
        AND coreCustomerId = _coreCustomerId AND find_in_set(contractaccounts.accountId,_accountsCSV) );
	
DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
         
OPEN accountData; 
getAccount : LOOP
fetch accountData into accountID; 
     IF finished = 1 THEN 
	     LEAVE getAccount;
	 ELSE
         SET @id = (SELECT LEFT(UUID(), 50));
		 INSERT INTO `customeraccounts` (`id`, `Customer_id`, `Account_id`,`contractId`, `coreCustomerId`)
            VALUES (@id, _userId, accountID, _contractId, _coreCustomerId);
      END IF;
      ITERATE  getAccount;
END LOOP getAccount;
CLOSE accountData;
END$$
DELIMITER ;

DROP procedure IF EXISTS `useractions_create_proc`;

DELIMITER $$
CREATE PROCEDURE `useractions_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,  
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
         FOR (SELECT customeraccounts.Account_id FROM customeraccounts WHERE contractId = _contractId AND coreCustomerId = _coreCustomerId 
         AND Customer_id = _userId AND FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true ));
DECLARE nonaccountlevelactions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '0' OR featureaction.isAccountLevel = false ));
DECLARE limits CURSOR 
        FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

IF(ISNULL(_accountsCSV) OR _accountsCSV='' ) THEN
SET _accountsCSV = (SELECT group_concat(distinct customeraccounts.Account_id SEPARATOR ",") FROM customeraccounts WHERE customeraccounts.Customer_id = _userId
              AND customeraccounts.contractId = _contractId AND  customeraccounts.coreCustomerId = _coreCustomerId );
END IF;
 
SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = _contractId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId);
IF(ISNULL(_groupId) OR _groupId='' ) THEN
SET _groupId = (SELECT Group_id FROM groupservicedefinition WHERE serviceDefinitionId = @serviceDefinitionId 
                       AND (isDefaultGroup = true OR isDefaultGroup = '1'));
END IF;
                        
SET @validFIActions = (SELECT group_concat(distinct id SEPARATOR ",") FROM featureaction);

SET @validServiceDefinitionActions = (SELECT group_concat(distinct actionId SEPARATOR ",") FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId= @serviceDefinitionId AND FIND_IN_SET(actionId,@validFIActions));

SET _groupId = (SELECT Group_id FROM groupservicedefinition WHERE serviceDefinitionId = @serviceDefinitionId 
                        AND  Group_id = _groupId );


SET @validGroupActions = (SELECT group_concat(distinct Action_id SEPARATOR ",") FROM groupactionlimit WHERE Group_id = _groupId 
                                AND FIND_IN_SET(Action_id,@validServiceDefinitionActions));
                                
SET @validActionsList = (SELECT group_concat(distinct actionId SEPARATOR ",") FROM contractactionlimit WHERE contractId = _contractId
                                AND coreCustomerId = _coreCustomerId AND FIND_IN_SET(actionId,@validGroupActions));
  

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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci);
        IF finished = 1 THEN 
            LEAVE getAction;
        else
            OPEN limits; 
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN 
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT value FROM contractactionlimit WHERE actionId = featureActionId  COLLATE utf8_general_ci
               AND limitTypeId = limitId COLLATE utf8_general_ci AND contractId = _contractId AND coreCustomerId = _coreCustomerId);
    
               if (limitId='MAX_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
               ELSEIF (limitId='MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,0.00);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,0.00);
               SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
               
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
               (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,@limitvalue);
                
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,limitId,@limitvalue);
                  
                SET entryStatus = 1;
               ITERATE  getlimit;
            END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
                   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true);
            END IF;
            ITERATE  getAction;
        END IF;
  END LOOP getAction;
  CLOSE actions;
  SET finished = 0;
  ITERATE  getAccount;
 END IF;
END LOOP getAccount;
CLOSE accounts;

 
SET finished = 0;
OPEN nonaccountlevelactions; 
  getAction: LOOP
        FETCH nonaccountlevelactions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci);
        SELECT @featureId;
        IF finished = 1 THEN 
            LEAVE getAction;
        else
                   SET @id = (SELECT LEFT(UUID(), 50));
                   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,isAllowed) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,true);
        END IF;
        ITERATE  getAction;
END LOOP getAction;
CLOSE nonaccountlevelactions;
END$$

DELIMITER ;
;



DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertgrouplevel` ;
CREATE VIEW `alerts_fetch_globaldata_view_alertgrouplevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
		`alertsubtype`.`recipienttype` as `recipienttype`,
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
		`alertsubtype`.`recipienttype` as `recipienttype`,
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
		`alertsubtype`.`recipienttype` as `recipienttype`,
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

DROP PROCEDURE IF EXISTS `get_valid_customeraccounts_get_proc`;
delimiter $$
CREATE PROCEDURE `get_valid_customeraccounts_get_proc`(
	IN _customeraccountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _customerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @list = (SELECT _customeraccountsCSV);
SET @validcustomeraccountsCSV = '';

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    SET @accountIdList = (SELECT GROUP_CONCAT(customeraccounts.Account_id SEPARATOR ",") from customeraccounts WHERE (Account_id=@value) and Customer_id = _customerId);
    IF ISNULL(@accountIdList)>0 OR @accountIdList="" THEN 
        IF @validcustomeraccountsCSV='' THEN
           SET @validcustomeraccountsCSV = @value;
		ELSE 
           SET @validcustomeraccountsCSV = CONCAT(@validcustomeraccountsCSV,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

select @validcustomeraccountsCSV As validAccounts;
END$$

DELIMITER;


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

SET @actionsAtServiceDefinitions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE FIND_IN_SET(servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions));

SET @actionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups));

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
                                
SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (SELECT group_concat(distinct featureaction.Feature_id SEPARATOR ",") FROM featureaction WHERE 
                                  FIND_IN_SET(featureaction.id,@intersectedUserActions));
                                  
SELECT @intersectedUserFeatures AS features;
                                  
                       
END$$
DELIMITER ;


DELIMITER $$
CREATE PROCEDURE `user_accountactions_get_proc`(
in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SET SESSION group_concat_max_len = 100000000;
SET @contractId = (SELECT contractcorecustomers.contractId FROM contractcorecustomers WHERE contractcorecustomers.coreCustomerId = _coreCustomerId );
SET @serviceDefinitionId = (SELECT contract.servicedefinitionId FROM contract WHERE contract.id = @contractId );
SET @groupId = (SELECT customergroup.Group_id FROM customergroup WHERE customergroup.contractId = @contractId 
                 AND customergroup.coreCustomerId = _coreCustomerId AND customergroup.Customer_id = _userId);
SET @groupId = (SELECT groupservicedefinition.Group_id FROM groupservicedefinition WHERE groupservicedefinition.serviceDefinitionId = @serviceDefinitionId
                 AND groupservicedefinition.Group_id = @groupId); 
IF isnull(@groupId) THEN
  SET @groupId = '';
END IF;

SET @validFIAccountLevelActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                    (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true) AND 
                                    featureaction.status = 'SID_ACTION_ACTIVE');
SET @validServiceDefinitinActions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE
                                servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                                FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIAccountLevelActions)); 
SET @validGroupActions = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE
                                groupactionlimit.Group_id = @groupId AND
                                FIND_IN_SET(groupactionlimit.Action_id, @validServiceDefinitinActions)); 
SET @validUserActions = (SELECT group_concat(distinct customeraction.Action_id SEPARATOR ",") FROM customeraction WHERE
                                customeraction.Customer_id = _userId AND
                                customeraction.coreCustomerId = _coreCustomerId AND 
                                customeraction.contractId = @contractId AND 
                                (customeraction.isAllowed = '1' OR customeraction.isAllowed = true) AND
                                FIND_IN_SET(customeraction.Action_id, @validGroupActions)); 
                                
SET @actionCondition = concat("FIND_IN_SET(`customeraction`.`Action_id`, '",@validUserActions,"')");

set @select_statement = concat("SELECT 
        `customeraction`.`Customer_id` AS `Customer_id`,
         `customeraction`.`contractId` AS `contractId`,
		 `customeraction`.`coreCustomerId` AS `coreCustomerId`,
        `customeraction`.`Account_id` AS `Account_id`,
        `customeraction`.`featureId` AS `featureId`,
        `customeraction`.`Action_id` AS `Action_id`,
        `customeraction`.`RoleType_id` AS `RoleType_id`,
    	`customeraction`.`LimitType_id` AS `LimitType_id`,
        `customeraction`.`value` AS `value`
    FROM
        (`customeraction`)
  	where `customeraction`.`Customer_id` = ", quote(_userId)," and ",@actionCondition ," and `customeraction`.`coreCustomerId` = ", quote(_coreCustomerId));
     
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customeraction_save_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customeraction_save_proc`(
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
           	set @query = concat('INSERT INTO customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId, limitType_id,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;


ALTER TABLE customeraction MODIFY COLUMN Action_id varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL;

ALTER TABLE `application` ADD COLUMN `newSettings` TINYINT(1) NOT NULL DEFAULT '0' AFTER `customerCreationMode`;

DROP VIEW IF EXISTS `allaccountsview`;
CREATE VIEW `allaccountsview` AS
    SELECT DISTINCT
        `accounts`.`AccountHolder` AS `AccountHolder`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`AccountName` AS `AccountName`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`isBusinessAccount` AS `IsOrganizationAccount`,
        `accounts`.`ownership` AS `ownership`,
        `accounts`.`StatusDesc` AS `accountStatus`,
        `accounttype`.`TypeDescription` AS `accountType`,
        `membershipaccounts`.`membershipId` AS `Membership_id`,
        `membership`.`taxId` AS `Taxid`,
        `membership`.`phone` AS `phoneNumber`,
        `membership`.`email` AS `emailId`,
        `membership`.`name` AS `name`
    FROM
        (((`accounts`
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `accounttype` ON ((`accounts`.`Type_id` = `accounttype`.`TypeID`)))
        LEFT JOIN `membership` ON ((`membership`.`id` = `membershipaccounts`.`membershipId`)));
		
ALTER TABLE `membership` 
ADD COLUMN `faxId` VARCHAR(45) NULL AFTER `email`,
DROP INDEX `taxId_UNIQUE` ;

DROP PROCEDURE IF EXISTS customer_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_contract_delete_proc`(in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
	SET @contract_statement = 'DELETE FROM contractcustomers where';
	SET @accounts_statement = 'DELETE FROM customeraccounts where ';
	SET @group_statement = 'DELETE FROM customergroup where ';
	SET @action_statement = 'DELETE FROM customeraction where ';
	SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
	

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
		SET @accounts_statement = CONCAT(@accounts_statement , @where_clause1);	
		SET @group_statement = CONCAT(@group_statement , @where_clause1);	
		SET @action_statement = CONCAT(@action_statement , @where_clause1);	
		SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);	
	
	
		PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;	
		PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	END if;

END$$
DELIMITER ;


CREATE TABLE `accountsstatementfiles` (
  `id` varchar(50) NOT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `fileContent` longblob,
  `fileName` varchar(150) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedBy` varchar(50) DEFAULT NULL,
  `fileType` varchar(50) DEFAULT NULL,
  `failureMessage` varchar(250) DEFAULT NULL,
  `fromDate` varchar(50) DEFAULT NULL,
  `toDate` varchar(55) DEFAULT NULL,
  `accountIds` mediumtext,
  PRIMARY KEY (`id`)
)ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE contractcustomrole (
	`id` int auto_increment NOT NULL,
	`contractId` varchar(100) NULL,
	`coreCustomerId` varchar(100) NULL,
	`customRoleId` varchar(100) NULL,
	`roleId` varchar(100) NULL,
	CONSTRAINT contractcustomrole_pk PRIMARY KEY (id)
)
ENGINE=InnoDB
DEFAULT CHARSET=utf8;

CREATE TABLE `customroleaccounts` (
  `id` varchar(50) NOT NULL,
  `customRoleId` varchar(50) DEFAULT NULL,
  `Account_id` varchar(50) DEFAULT NULL,
  `AccountName` varchar(50) DEFAULT NULL,
  `contractId` varchar(20) DEFAULT NULL,
  `coreCustomerId` varchar(20) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT NULL,
  `lastmodifiedts` timestamp NULL DEFAULT NULL,
  `accountType` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DELIMITER $$
CREATE PROCEDURE `get_actions_with_approvefeatureaction_proc`(
IN _featureActions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @actionsList = (SELECT group_concat(id SEPARATOR ",") from featureaction WHERE (Type_id = "MONETARY" AND
                      FIND_IN_SET(id,_featureActions)) AND
					(!ISNULL(featureaction.approveFeatureAction) || featureaction.approveFeatureAction != ''));

select @actionsList As actions;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customer_search_proc;

DELIMITER $$
CREATE PROCEDURE `customer_search_proc`( 
in _searchType varchar(60),
in _id varchar(50), 
in _name varchar(50),  
in _SSN varchar(50),
in _username varchar(50), 
in _phone varchar(100), 
in _email varchar(100),
in _dateOfBirth varchar(100),
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
                
                 IF _dateOfBirth != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.DateOfBirth = ",quote(_dateOfBirth));
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

CREATE TABLE `customerlimitgrouplimits` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `contractId` varchar(45) DEFAULT NULL,
  `coreCustomerId` varchar(45) DEFAULT NULL,
  `limitGroupId` varchar(45) DEFAULT NULL,
  `LimitType_id` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`,`synctimestamp`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP PROCEDURE IF EXISTS customer_search_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_search_proc`( 
in _searchType varchar(60),
in _id varchar(50), 
in _name varchar(50),  
in _SSN varchar(50),
in _username varchar(50), 
in _phone varchar(100), 
in _email varchar(100),
in _dateOfBirth varchar(100),
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
					address.addressLine1 As addressLine1,
					address.addressLine2 As addressLine2,
					city.Name As city,
					address.zipCode As zipCode,
					country.Name As county,
					customer.isEnrolled as isEnrolled,
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
					LEFT JOIN country ON (city.Country_id = country.id)
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
				address.addressLine1 As addressLine1,
				address.addressLine2 As addressLine2,
				city.Name As city,
				address.zipCode As zipCode,
				country.Name As county,
				customer.isEnrolled as isEnrolled,
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
                
                 IF _dateOfBirth != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.DateOfBirth = ",quote(_dateOfBirth));
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
			LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
			LEFT JOIN address ON (customeraddress.Address_id = address.id)
			LEFT JOIN city ON (city.id = address.City_id)
			LEFT JOIN country ON (city.Country_id = country.id)
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


ALTER TABLE `wiretransferspayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `wiretransferspayee` CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NULL DEFAULT NULL;

ALTER TABLE `billpaypayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `billpaypayee` CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NULL DEFAULT NULL;

ALTER TABLE `internationalpayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `internationalpayee` CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NULL DEFAULT NULL;

ALTER TABLE `intrabankpayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `intrabankpayee` CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NULL DEFAULT NULL;

ALTER TABLE `interbankpayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `interbankpayee` CHANGE COLUMN `companyId` `contractId` VARCHAR(50) NULL DEFAULT NULL;

ALTER TABLE `p2ppayee` CHANGE COLUMN `customerId` `createdBy` VARCHAR(50) NULL DEFAULT NULL; 
ALTER TABLE `p2ppayee` CHANGE COLUMN `createdts` `contractId` VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE `p2ppayee` CHANGE COLUMN `updatedts` `cif` VARCHAR(50) NULL DEFAULT NULL;

CREATE  OR REPLACE VIEW `customeraccounts_corecustomerinfo_view` AS
SELECT
	`customeraccounts`.`Customer_id` AS `User_id`,
	`customeraccounts`.`Account_id` AS `Account_id`,
    `customeraccounts`.`FavouriteStatus` AS `FavouriteStatus`,
    `contractcorecustomers`.`coreCustomerId` AS `Membership_id`,
    `contractcorecustomers`.`coreCustomerName` AS `MembershipName`,
	`contractcorecustomers`.`isBusiness` AS `isBusiness`
FROM 
	`customeraccounts`
    LEFT JOIN `contractcorecustomers` ON ( `contractcorecustomers`.`coreCustomerId` = `customeraccounts`.`coreCustomerId`);
    
ALTER TABLE `contractaccounts` 
ADD COLUMN `arrangementId` VARCHAR(50) NULL DEFAULT NULL AFTER `statusDesc`;

ALTER TABLE `externalaccount` ADD COLUMN addressLine2 varchar(50) DEFAULT NULL;
ALTER TABLE `externalaccount` ADD COLUMN email varchar(50) DEFAULT NULL;

DROP procedure IF EXISTS `user_limitgroup_limits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `user_limitgroup_limits_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 100000000;

SET @singlePaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'SINGLE_PAYMENT');
                             
SET @bulkPaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'BULK_PAYMENT');

SET @max_per_transaction_single_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));

SET @max_per_transaction_bulk_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
						
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment);

INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment);

INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment);

INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment);

INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment);

INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment);



END$$

DELIMITER ;


ALTER TABLE `contractcorecustomers` 
ADD COLUMN `sectorId` VARCHAR(50) NULL DEFAULT NULL AFTER `isBusiness`;


DROP procedure IF EXISTS `corecustomeraccounts_details_get_proc`;

DELIMITER $$
CREATE PROCEDURE `corecustomeraccounts_details_get_proc`(
IN _coreCustomerIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @corecustomersList = (SELECT group_concat(contractcustomers.coreCustomerId SEPARATOR ',') from contractcustomers WHERE contractcustomers.customerId = _customerId AND FIND_IN_SET(contractcustomers.coreCustomerId,_coreCustomerIdList));

	SET @selectstatement = concat("SELECT 
           `contractcorecustomers`.`coreCustomerId` AS coreCustomerId , 
           `contractcorecustomers`.`coreCustomerName` AS coreCustomerName ,
           `customeraccounts`.`email` AS email ,
           `customeraccounts`.`Customer_id` AS customerId ,
           `customeraccounts`.`FavouriteStatus` AS favouriteStatus ,
           IF ((`customeraccounts`.`EStatementmentEnable`) = '1' ,'true' , 'false') AS `eStatementEnable`,
           IF ((`contractcorecustomers`.`isBusiness`) = '1' ,'true' , 'false') AS `isBusinessAccount`,
           `contractaccounts`.`accountId` AS accountId
           from (`contractcorecustomers`) 
		   JOIN (`contractaccounts`) ON (`contractcorecustomers`.`coreCustomerId` = `contractaccounts`.`coreCustomerId`)
           JOIN (`customeraccounts`) ON (`customeraccounts`.`Account_id` = `contractaccounts`.`accountId`)
           where FIND_IN_SET(`contractcorecustomers`.`coreCustomerId`, '",@corecustomersList,"') AND `customeraccounts`.`Customer_id` = '",_customerId,"'");
           
           PREPARE stmt FROM @selectstatement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$

DELIMITER ;
;

ALTER TABLE `mfaservice` 
CHANGE COLUMN `securityQuestions` `securityQuestions` LONGTEXT NULL DEFAULT NULL ;

ALTER TABLE `contractcorecustomers`
ADD INDEX `FK_CoreCustID` (`coreCustomerId` ASC);

ALTER TABLE `contract` 
CHANGE COLUMN `servicedefinitionId` `servicedefinitionId` VARCHAR(50) NOT NULL ,
CHANGE COLUMN `name` `name` VARCHAR(50) NOT NULL ;

ALTER TABLE `contract` 
ADD CONSTRAINT `FK_contract_status_statusId`
  FOREIGN KEY (`statusId`)
  REFERENCES `status` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `contractactionlimit` 
ADD INDEX `FK_contractactionlimit_featureaction_actionId_idx` (`actionId` ASC),
ADD INDEX `FK_contractactionlimit_feature_featureId_idx` (`featureId` ASC);
;
ALTER TABLE `contractactionlimit` 
ADD CONSTRAINT `FK_contractactionlimit_featureaction_actionId`
  FOREIGN KEY (`actionId`)
  REFERENCES `featureaction` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION,
ADD CONSTRAINT `FK_contractactionlimit_feature_featureId`
  FOREIGN KEY (`featureId`)
  REFERENCES `feature` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
ALTER TABLE `contractactionlimit` 
ADD INDEX `IDX_contractactionlimit_corecustomerId` (`coreCustomerId` ASC);
;

ALTER TABLE `contractaccounts` 
ADD INDEX `IDX_contractaccounts_corecustomerId` (`coreCustomerId` ASC);
;



ALTER TABLE `contractcustomers` 
CHANGE COLUMN `customerId` `customerId` VARCHAR(50) NOT NULL ;

ALTER TABLE `contractcustomers` 
CHANGE COLUMN `coreCustomerId` `coreCustomerId` VARCHAR(45) NOT NULL ;


ALTER TABLE `contractcustomers` 
ADD INDEX `IDX_contractcustomers_corecustomerId` (`coreCustomerId` ASC);
;


ALTER TABLE `contractcustomers` 
ADD CONSTRAINT `FK_contractcustomers_customer_customerId`
  FOREIGN KEY (`customerId`)
  REFERENCES `customer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
  
ALTER TABLE `contractfeatures` 
ADD INDEX `FK_contractfeatures_feature_featureId_idx` (`featureId` ASC);
;
ALTER TABLE `contractfeatures` 
ADD CONSTRAINT `FK_contractfeatures_feature_featureId`
  FOREIGN KEY (`featureId`)
  REFERENCES `feature` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `contractfeatures` 
ADD INDEX `IDX_contractfeatures_corecustomerId` (`coreCustomerId` ASC);
;

ALTER TABLE `card`
	CHANGE COLUMN `billingAddress` `billingAddress` VARCHAR(150) NULL DEFAULT NULL AFTER `serviceProvider`;
	

ALTER TABLE `contractcustomers` 
ADD INDEX `IDX_contractcustomers_contractId` (`contractId` ASC);

ALTER TABLE `customergroup` 
ADD INDEX `IDX_customergroup_contractId` (`contractId` ASC),
ADD INDEX `IDX_customergroup_coreCustomerId` (`coreCustomerId` ASC);
	
ALTER TABLE `contractactionlimit` 
ADD INDEX `IDX_actionIdAndCoreCustomerId` (`actionId` ASC, `coreCustomerId` ASC);

ALTER TABLE `customeraction` 
ADD INDEX `IDX_actionid_customerId_isAllowed` (`Customer_id` ASC, `isAllowed` ASC, `Action_id` ASC);

DROP PROCEDURE IF EXISTS `user_customers_proc`;

delimiter $$

CREATE PROCEDURE `user_customers_proc`(
	in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)BEGIN
	SET @select_statement = ("(SELECT 
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
    IF(_coreCustomerId != '') THEN
		IF(!@isWhereAppened) THEN
			SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
		END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
		END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`coreCustomerId` = ",quote(_coreCustomerId));
    END IF;
    set @select_statement =  concat(@select_statement ,");");
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_user_corecustomer_actions`;

delimiter $$

CREATE PROCEDURE `fetch_user_corecustomer_actions`(
	IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
     DECLARE _serviceDefinitionId varchar(255) DEFAULT ""  ;
     DECLARE _roleId varchar(255) DEFAULT ""  ;

    SET _serviceDefinitionId = (select GROUP_CONCAT(contract.servicedefinitionId SEPARATOR ",") from contract 
								LEFT JOIN  contractcorecustomers on (contractcorecustomers.contractId = contract.id ) 
                                where contractcorecustomers.coreCustomerId = _coreCustomerId);
	SET _roleId = (select GROUP_CONCAT(customergroup.Group_id SEPARATOR ",") from customergroup where customergroup.Customer_id = _userId
					and customergroup.coreCustomerId = _coreCustomerId );

	SET @action_select_statement = ("(SELECT DISTINCT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id ))");
	IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT DISTINCT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT DISTINCT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT("(SELECT DISTINCT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
	IF(_userId != '') THEN 
		SET @select_statement = CONCAT("(SELECT DISTINCT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
        IF(_coreCustomerId != '') THEN 
			SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
        END IF;
        SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
	END IF;
   PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    
END$$

DELIMITER ;

ALTER TABLE address DROP FOREIGN KEY `FK_Address_Region`;

DROP VIEW IF EXISTS  `billview`;
CREATE VIEW `billview` AS
    SELECT 
        `bb`.`balanceAmount` AS `balanceAmount`,
        `bb`.`billDueDate` AS `billDueDate`,
        `bb`.`billGeneratedDate` AS `billGeneratedDate`,
        `bb`.`description` AS `description`,
        `bb`.`dueAmount` AS `dueAmount`,
        `bb`.`ebillURL` AS `ebillURL`,
        `bb`.`id` AS `id`,
        `bb`.`paidAmount` AS `paidAmount`,
        `bb`.`paidDate` AS `paidDate`,
        `bb`.`Payee_id` AS `payeeId`,
        `bb`.`currencyCode` AS `currencyCode`,
        `ca`.`AccountName` AS `fromAccountName`,
        `ca`.`Account_id` AS `fromAccountNumber`,
        `ca`.`Customer_id` AS `User_id`,
        `py`.`name` AS `payeeName`,
        `py`.`softDelete` AS `softDelete`,
        `py`.`eBillEnable` AS `ebillStatus`,
        `py`.`nickName` AS `payeeNickName`,
        `py`.`addressLine1` AS `payeeAddressLine1`,
        `bc`.`categoryName` AS `billerCategory`,
        `bm`.`billerCategoryId` AS `billerCategoryId`,
        `bm`.`ebillSupport` AS `ebillSupport`
    FROM
        ((((`bill` `bb`
        JOIN `payee` `py`)
        JOIN `customeraccounts` `ca`)
        JOIN `billercategory` `bc`)
        JOIN `billermaster` `bm`)
    WHERE
        ((`bb`.`Payee_id` = `py`.`Id`)
            AND (`bb`.`Account_id` = `ca`.`Account_id`)
            AND (`bm`.`billerCategoryId` = `bc`.`id`)
            AND (`bb`.`billerMaster_id` = `bm`.`id`));

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
                `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND  `customercommunication`.`isPrimary` = 1
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL'  AND  `customercommunication`.`isPrimary` = 1
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

CREATE OR REPLACE VIEW `customeraddress_view` AS
select
    `c`.`id` AS `CustomerId`,
    `ca`.`Address_id` AS `Address_id`,
    `ca`.`isPrimary` AS `isPrimary`,
    `ca`.`Type_id` AS `AddressType`,
    `ca`.`isTypeBusiness` AS `isTypeBusiness`,
    `a`.`id` AS `AddressId`,
    `a`.`addressLine1` AS `AddressLine1`,
    `a`.`addressLine2` AS `AddressLine2`,
    `a`.`zipCode` AS `ZipCode`,
    `a`.`Region_id` AS `Region_id`,
    `a`.`City_id` AS `City_id`,
    if((`a`.`country`IS NOT null), `a`.`country`, `reg`.`Country_id`) AS `Country_id`,
    `a`.`cityName` AS `CityName`,
    `reg`.`Name` AS `RegionName`,
    `reg`.`Code` AS `RegionCode`,
    if((`a`.`country`IS NOT null), `country`.`Name`, `coun`.`Name`) AS `CountryName`,
    if((`a`.`country`IS NOT null), `country`.`Code`, `coun`.`Code`) AS `CountryCode`
from
    ((((`customeraddress` `ca`
join `customer` `c` on
    (`ca`.`Customer_id` = `c`.`id`))
join `address` `a` on
    ((`a`.`id` = `ca`.`Address_id`))
left join `region` `reg` on
	(`reg`.`id` = `a`.`Region_id`))
left join `country` `coun` on
    (`coun`.`id` = `reg`.`Country_id`))
    left join `country` `country` on
    (`country`.`id` = `a`.`country`));
    
    
    
    
    
    
ALTER TABLE `contract` 
ADD INDEX `IDX_contract_id_name` (`id` ASC, `name` ASC);
;


ALTER TABLE `contractaccounts` 
ADD INDEX `IDX_contractaccounts_contractId_corecustomerId` (`contractId` ASC, `coreCustomerId` ASC);
;


ALTER TABLE `contractaccounts` 
ADD INDEX `IDX_contractaccounts_accountId` (`accountId` ASC);
;


ALTER TABLE `contract` 
ADD INDEX `IDX_contract_status` (`statusId` ASC);
;
 
 
ALTER TABLE `contractcorecustomers` 
ADD INDEX `IDX_contractcorecustomers_contractId_corecustomerId_isPrimary` (`contractId` ASC, `coreCustomerId` ASC, `isPrimary` ASC);
;

ALTER TABLE `contractfeatures` 
ADD INDEX `IDX_contractfeatures_contractId_corecustomerId` (`contractId` ASC, `coreCustomerId` ASC);
;


ALTER TABLE `contractactionlimit` 
ADD INDEX `IDX_contractactionlimit_contractId_corecustomerId` (`contractId` ASC, `coreCustomerId` ASC);
;


ALTER TABLE `contractfeatures` 
ADD INDEX `IDX_contractfeatures_contractId_corecustomerId_featureId` (`contractId` ASC, `coreCustomerId` ASC, `featureId` ASC);
;


ALTER TABLE `contractactionlimit` 
ADD INDEX `IDX_contractacttionlimit_contractId_corecustomerId_featuresId` (`contractId` ASC, `coreCustomerId` ASC, `featureId` ASC);
;


ALTER TABLE `contractactionlimit` 
ADD INDEX `IDX_contractactionlimit_contractId_corecustomerId_actionId` (`contractId` ASC, `coreCustomerId` ASC, `featureId` ASC, `actionId` ASC);
;


ALTER TABLE `backendidentifier` 
ADD INDEX `FK_backendIdentifier_customer_customerId_idx` (`Customer_id` ASC);
;
ALTER TABLE `backendidentifier` 
ADD CONSTRAINT `FK_backendIdentifier_customer_customerId`
  FOREIGN KEY (`Customer_id`)
  REFERENCES `customer` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `backendidentifier` 
ADD INDEX `IDX_backendIdentifier_customerId_backendType` (`Customer_id` ASC, `BackendType` ASC);
;

    
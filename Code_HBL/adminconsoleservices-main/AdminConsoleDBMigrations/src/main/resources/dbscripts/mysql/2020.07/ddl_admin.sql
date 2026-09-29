
CREATE TABLE `signatorytype` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(300) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `businesstype` (
  `id` varchar(50) NOT NULL,
  `name` VARCHAR(50) NULL,
  `minAuthSignatory` int(11) DEFAULT NULL,
  `maxAuthSignatory` int(11) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `businesstypecol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `groupbusinesstype` (
  `Group_id` VARCHAR(50) NOT NULL,
  `BusinessType_id` VARCHAR(255) NOT NULL,
  `isDefaultGroup` TINYINT(1) NOT NULL DEFAULT '0',  
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Group_id`,`BusinessType_id`),
  KEY `IXFK_groupbusinesstype_Group` (`Group_id`),
  KEY `IXFK_groupbusinesstype_businesstype` (`BusinessType_id`),
  CONSTRAINT `FK_groupbusinesstype_Businesstype_id` FOREIGN KEY (`BusinessType_id`) REFERENCES `businesstype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_groupbusinesstype_Group` FOREIGN KEY (`Group_id`) REFERENCES `membergroup` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `businesssignatory` (
  `BusinessType_id` varchar(50) NOT NULL,
  `Signatory_id` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`BusinessType_id`,`Signatory_id`),
  KEY `IXFK_BusinessSignatory_BusinessType` (`BusinessType_id`),
  KEY `IXFK_BusinessSignatory_Signatory` (`Signatory_id`),
  CONSTRAINT `FK_BusinessSignatory_BusinessType` FOREIGN KEY (`BusinessType_id`) REFERENCES `businesstype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_BusinessSignatory_Signatory` FOREIGN KEY (`Signatory_id`) REFERENCES `signatorytype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customerbusinesstype` (
  `Customer_id` varchar(50) NOT NULL,
  `BusinessType_id` varchar(50) NOT NULL,
  `SignatoryType_id` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Customer_id`,`BusinessType_id`,`SignatoryType_id`),
  KEY `IXFK_customerbusinesstype_Customer` (`Customer_id`),
  KEY `IXFK_customerbusinesstype_Business_Type` (`BusinessType_id`),
  KEY `IXFK_customerbusinesstype_Signatory_Type` (`SignatoryType_id`),  
  CONSTRAINT `FK_customerbusinesstype_Business_Type` FOREIGN KEY (`BusinessType_id`) REFERENCES `businesstype` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_customerbusinesstype_Signatory_Type` FOREIGN KEY (`SignatoryType_id`) REFERENCES `signatorytype` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_customerbusinesstype_Customer` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
ALTER TABLE `organisation` ADD COLUMN `BusinessType_id` VARCHAR(50) NULL DEFAULT NULL AFTER `Description`,ADD INDEX `FK_organisation_Business_Type_idx` (`BusinessType_id` ASC);
ALTER TABLE `organisation` ADD CONSTRAINT `FK_organisation_Business_Type`  FOREIGN KEY (`BusinessType_id`)  REFERENCES `businesstype` (`id`)  ON DELETE NO ACTION  ON UPDATE NO ACTION;
ALTER TABLE `organisation` ADD COLUMN `StatusId` VARCHAR(50) NOT NULL DEFAULT 'SID_ORG_PENDING' AFTER `BusinessType_id`;
ALTER TABLE `organisation` 
ADD INDEX `FK_organisation_status_StatusId_idx` (`StatusId` ASC);
ALTER TABLE `organisation` 
ADD CONSTRAINT `FK_organisation_status_StatusId`
  FOREIGN KEY (`StatusId`)
  REFERENCES `status` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `organisation` 
ADD COLUMN `createdby` VARCHAR(50) NULL AFTER `StatusId`,
ADD COLUMN `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP AFTER `createdby`,
ADD COLUMN `rejectedby` VARCHAR(50) NULL AFTER `createdts`,
ADD COLUMN `rejectedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP AFTER `rejectedby`;

ALTER TABLE `customer` ADD COLUMN `isSignatory` TINYINT(1) NULL DEFAULT '0' AFTER `taxid`;
ALTER TABLE `customer` ADD COLUMN `sigtype` varchar(50) DEFAULT NULL AFTER `isSignatory`;
ALTER TABLE `application` ADD COLUMN `isAccountCentricCore` TINYINT(1) NOT NULL DEFAULT '1' AFTER `bwFileTransactionsLimit`;
ALTER TABLE `application` ADD COLUMN `timeZoneOffset` VARCHAR(50) NOT NULL DEFAULT 'UTC+11:00' AFTER `isAccountCentricCore`;
ALTER TABLE `card` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `account_id`;

CREATE TABLE `businessconfiguration` (
  `id` varchar(255) NOT NULL,
  `key` varchar(50) NOT NULL,
  `displayname` varchar(255) NOT NULL,
  `description` text,
  `value` text,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `key` (`key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE VIEW `businesstypes_view` AS
    SELECT 
        `businesstype`.`id` AS `BusinessType_id`,
        `businesstype`.`name` AS `BusinessType_name`,
        `businesstype`.`minAuthSignatory` AS `minAuthSignatory`,
        `businesstype`.`maxAuthSignatory` AS `maxAuthSignatory`,
        (SELECT 
                COUNT(`groupbusinesstype`.`Group_id`)
            FROM
                `groupbusinesstype`
            WHERE
                (`groupbusinesstype`.`BusinessType_id` = `businesstype`.`id`)) AS `Groups_count`,
        (SELECT 
                `groupbusinesstype`.`Group_id`
            FROM
                `groupbusinesstype`
            WHERE
                ((`groupbusinesstype`.`BusinessType_id` = `businesstype`.`id`)
                    AND (`groupbusinesstype`.`isDefaultGroup` = 1))) AS `Default_group`,
        (SELECT 
                COUNT(IFNULL(`customerbusinesstype`.`Customer_id`, 0))
            FROM
                `customerbusinesstype`
            WHERE
                (`customerbusinesstype`.`BusinessType_id` = `businesstype`.`id`)) AS `Customers_Count`,
        (SELECT 
                COUNT(IFNULL(`customergroup`.`Customer_id`, 0))
            FROM
                `customergroup`
            WHERE
                ((`customergroup`.`Group_id` = `Default_group`)
                    AND `customergroup`.`Customer_id` IN (SELECT 
                        `customerbusinesstype`.`Customer_id`
                    FROM
                        `customerbusinesstype`
                    WHERE
                        (`customerbusinesstype`.`BusinessType_id` = `businesstype`.`id`)))) AS `DefaultGroupCustomers_Count`
    FROM
        `businesstype`;
        


CREATE VIEW `groupbusinesstypecustomercount_view` AS
    SELECT 
        `customergroup`.`Group_id` AS `Group_id`,
        `customerbusinesstype`.`BusinessType_id` AS `BusinessType_id`,
        (SELECT 
                COUNT(`customergroup`.`Customer_id`)
            FROM
                `customergroup`
            WHERE
                (`customergroup`.`Customer_id` = `customerbusinesstype`.`Customer_id`)) AS `Customers_Count`
    FROM
        ((`customergroup`
        JOIN `customerbusinesstype` ON ((`customerbusinesstype`.`Customer_id` = `customergroup`.`Customer_id`)))
        JOIN `businesstype` ON ((`customerbusinesstype`.`BusinessType_id` = `businesstype`.`id`)));
DROP VIEW IF EXISTS `internalusers_view`;
CREATE VIEW `internalusers_view` AS
    SELECT 
        `systemuser`.`id` AS `User_id`,
        `systemuser`.`Status_id` AS `Status_id`,
        `status`.`Description` AS `Status_Desc`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`lastLogints` AS `lastLogints`,
        CONCAT(`systemuser`.`FirstName`,
                ' ',
                `systemuser`.`LastName`) AS `Name`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`Email` AS `Email`,
        (SELECT 
                `userrole`.`Role_id`
            FROM
                `userrole`
            WHERE
                (`systemuser`.`id` = `userrole`.`User_id`)
            LIMIT 1) AS `Role_id`,
        (SELECT 
                `role`.`Description`
            FROM
                `role`
            WHERE
                `role`.`id` IN (SELECT 
                        `userrole`.`Role_id`
                    FROM
                        `userrole`
                    WHERE
                        (`systemuser`.`id` = `userrole`.`User_id`))
            LIMIT 1) AS `Role_Desc`,
        (SELECT 
                `role`.`Name`
            FROM
                `role`
            WHERE
                `role`.`id` IN (SELECT 
                        `userrole`.`Role_id`
                    FROM
                        `userrole`
                    WHERE
                        (`systemuser`.`id` = `userrole`.`User_id`))
            LIMIT 1) AS `Role_Name`,
        ((SELECT 
                COUNT(`userpermission`.`Permission_id`)
            FROM
                `userpermission`
            WHERE
                (`systemuser`.`id` = `userpermission`.`User_id`)) + (SELECT 
                COUNT(`rolepermission`.`Permission_id`)
            FROM
                `rolepermission`
            WHERE
                `rolepermission`.`Role_id` IN (SELECT 
                        `userrole`.`Role_id`
                    FROM
                        `userrole`
                    WHERE
                        (`systemuser`.`id` = `userrole`.`User_id`)))) AS `Permission_Count`,
        `systemuser`.`lastmodifiedts` AS `lastmodifiedts`,
        `systemuser`.`createdts` AS `createdts`,
        (SELECT 
                CONCAT(`workaddress`.`addressLine1`,
                            ', ',
                            IFNULL(`workaddress`.`addressLine2`, ''),
                            ', ',
                            (SELECT 
                                    `city`.`Name`
                                FROM
                                    `city`
                                WHERE
                                    (`city`.`id` = `workaddress`.`City_id`)),
                            ', ',
                            (SELECT 
                                    `region`.`Name`
                                FROM
                                    `region`
                                WHERE
                                    (`region`.`id` = `workaddress`.`Region_id`)),
                            ', ',
                            (SELECT 
                                    `country`.`Name`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `city`.`Country_id`
                                        FROM
                                            `city`
                                        WHERE
                                            (`city`.`id` = `workaddress`.`City_id`))),
                            ', ',
                            `workaddress`.`zipCode`)
            ) AS `Work_Addr`,
        (SELECT 
                CONCAT(`homeaddress`.`addressLine1`,
                            ', ',
                            IFNULL(`homeaddress`.`addressLine2`, ''),
                            ', ',
                            IFNULL(`homeaddress`.`cityName`, ''),
                            ', ',
                            (SELECT 
                                    `region`.`Name`
                                FROM
                                    `region`
                                WHERE
                                    (`region`.`id` = `homeaddress`.`Region_id`)),
                            ', ',
                            (SELECT 
                                    `country`.`Name`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `region`.`Country_id`
                                        FROM
                                            `region`
                                        WHERE
                                            (`region`.`id` = `homeaddress`.`Region_id`))),
                            ', ',
                            `homeaddress`.`zipCode`)
            ) AS `Home_Addr`,
        (SELECT IFNULL(`homeaddress`.`id`, '')) AS `Home_AddressID`,
        (SELECT IFNULL(`homeaddress`.`addressLine1`, '')) AS `Home_AddressLine1`,
        (SELECT IFNULL(`homeaddress`.`addressLine2`, '')) AS `Home_AddressLine2`,
        (SELECT IFNULL(`homeaddress`.`cityName`, '')) AS `Home_CityName`,
        (SELECT IFNULL(`homeaddress`.`City_id`, '')) AS `Home_CityID`,
        (SELECT 
                IFNULL((SELECT 
                                    `region`.`Name`
                                FROM
                                    `region`
                                WHERE
                                    (`region`.`id` = `homeaddress`.`Region_id`)),
                            '')
            ) AS `Home_StateName`,
        (SELECT IFNULL(`homeaddress`.`Region_id`, '')) AS `Home_StateID`,
        (SELECT 
                IFNULL((SELECT 
                                    `country`.`Name`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `region`.`Country_id`
                                        FROM
                                            `region`
                                        WHERE
                                            (`region`.`id` = `homeaddress`.`Region_id`))),
                            '')
            ) AS `Home_CountryName`,
        (SELECT 
                IFNULL((SELECT 
                                    `country`.`id`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `region`.`Country_id`
                                        FROM
                                            `region`
                                        WHERE
                                            (`region`.`id` = `homeaddress`.`Region_id`))),
                            '')
            ) AS `Home_CountryID`,
        (SELECT IFNULL(`homeaddress`.`zipCode`, '')) AS `Home_Zipcode`,
        (SELECT IFNULL(`workaddress`.`id`, '')) AS `Work_AddressID`,
        (SELECT IFNULL(`workaddress`.`addressLine1`, '')) AS `Work_AddressLine1`,
        (SELECT IFNULL(`workaddress`.`addressLine2`, '')) AS `Work_AddressLine2`,
        (SELECT 
                IFNULL((SELECT 
                                    `city`.`Name`
                                FROM
                                    `city`
                                WHERE
                                    (`city`.`id` = `workaddress`.`City_id`)),
                            '')
            ) AS `Work_CityName`,
        (SELECT IFNULL(`workaddress`.`City_id`, '')) AS `Work_CityID`,
        (SELECT 
                IFNULL((SELECT 
                                    `region`.`Name`
                                FROM
                                    `region`
                                WHERE
                                    (`region`.`id` = `workaddress`.`Region_id`)),
                            '')
            ) AS `Work_StateName`,
        (SELECT IFNULL(`workaddress`.`Region_id`, '')) AS `Work_StateID`,
        (SELECT 
                IFNULL((SELECT 
                                    `country`.`Name`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `city`.`Country_id`
                                        FROM
                                            `city`
                                        WHERE
                                            (`city`.`id` = `workaddress`.`City_id`))),
                            '')
            ) AS `Work_CountryName`,
        (SELECT 
                IFNULL((SELECT 
                                    `country`.`id`
                                FROM
                                    `country`
                                WHERE
                                    `country`.`id` IN (SELECT 
                                            `city`.`Country_id`
                                        FROM
                                            `city`
                                        WHERE
                                            (`city`.`id` = `workaddress`.`City_id`))),
                            '')
            ) AS `Work_CountryID`,
        (SELECT IFNULL(`workaddress`.`zipCode`, '')) AS `Work_Zipcode`
    FROM
        (((`systemuser`
        JOIN `status` ON ((`systemuser`.`Status_id` = `status`.`id`)))
        LEFT JOIN `address` `homeaddress` ON (`homeaddress`.`id` IN (SELECT 
                `useraddress`.`Address_id`
            FROM
                `useraddress`
            WHERE
                ((`systemuser`.`id` = `useraddress`.`User_id`)
                    AND (`useraddress`.`Type_id` = 'ADR_TYPE_HOME')))))
        LEFT JOIN `address` `workaddress` ON (`workaddress`.`id` IN (SELECT 
                `useraddress`.`Address_id`
            FROM
                `useraddress`
            WHERE
                ((`systemuser`.`id` = `useraddress`.`User_id`)
                    AND (`useraddress`.`Type_id` = 'ADR_TYPE_WORK')))));
                    
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
  	where `customeraction`.`Customer_id` = ", quote(_customerId)," and ",@active_feature_condition);
    
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
    	and ", @active_feature_condition );
    
    IF(_actionId != '') THEN
    	set @select_statement =  concat(@select_statement ," and `groupactionlimit`.`Action_id` = ",quote(_actionId));
  	END IF;
    
    -- select @select_statement;
  	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

ALTER TABLE `achfile` ADD COLUMN `approvalAccounts` TEXT NULL DEFAULT NULL AFTER `confirmationNumber`; 
ALTER TABLE `achfile` ADD COLUMN `debitAccounts` TEXT NULL DEFAULT NULL AFTER `approvalAccounts`;

ALTER TABLE `ownaccounttransfers`
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_ownaccounttransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `ownaccounttransfers`
	ADD CONSTRAINT `FK_ownaccounttransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
	  
ALTER TABLE `billpaytransfers`
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_billpaytransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `billpaytransfers` 
	ADD CONSTRAINT `FK_billpaytransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
	  
ALTER TABLE `p2ptransfers` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_p2ptransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `p2ptransfers` 
	ADD CONSTRAINT `FK_p2ptransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
	                  
ALTER TABLE `wiretransfers` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_wiretransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `wiretransfers` 
	ADD CONSTRAINT `FK_wiretransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;

ALTER TABLE `intrabanktransfers` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_intrabanktransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `intrabanktransfers` 
	ADD CONSTRAINT `FK_intrabanktransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;

ALTER TABLE `interbankfundtransfers` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_interbankfundtransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `interbankfundtransfers` 
	ADD CONSTRAINT `FK_interbankfundtransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;

ALTER TABLE `internationalfundtransfers` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_internationalfundtransfers_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `internationalfundtransfers` 
	ADD CONSTRAINT `FK_internationalfundtransfers_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
	  
ALTER TABLE `achtransaction` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_achtransaction_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `achtransaction` 
	ADD CONSTRAINT `FK_achtransaction_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
	  
ALTER TABLE `bbtemplate` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_bbtemplate_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `bbtemplate` 
	ADD CONSTRAINT `FK_bbtemplate_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;

ALTER TABLE `achfile` 
	ADD COLUMN `roleId` VARCHAR(45) NULL AFTER `companyId`,
	ADD INDEX `FK_achfile_roleId_idx_idx` (`roleId` ASC);

ALTER TABLE `achfile` 
	ADD CONSTRAINT `FK_achfile_roleId_idx`
	  FOREIGN KEY (`roleId`)
	  REFERENCES `membergroup` (`id`)
	  ON DELETE SET NULL
	  ON UPDATE CASCADE;
                    
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
        
        SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
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
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achtransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achtransaction`.`status` = 'Pending' "),
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
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE companyId = @companyId AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
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
			`organisation`.`Name` AS `companyName`,
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
			(((((((`achtransaction`
			LEFT JOIN `customer` ON (`achtransaction`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`achtransaction`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`achtransaction`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`achtransaction`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`achtransaction`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`achtransaction`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
			LEFT JOIN `organisation` ON (`achtransaction`.`companyId` = `organisation`.`id`))"
            ,@validAccountsJoin,
            "
			LEFT JOIN `bbrequest` ON (`achtransaction`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achtransaction`.`softDelete` = '0'
				AND ( `achtransaction`.`companyId` = '", @companyId ,"' OR FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"'))
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

DROP PROCEDURE IF EXISTS `fetch_fi_org_group_level_limits`;
DELIMITER $$
CREATE PROCEDURE `fetch_fi_org_group_level_limits`(
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

     SET @select = CONCAT(" select 
        `actionlimit`.`Action_id`,
        `actionlimit`.`LimitType_id`,
        `actionlimit`.`value` as `fiValue`,
        `organisationactionlimit`.`value` as `orgValue`,
        `groupactionlimit`.`value` as `groupValue`
         from (`actionlimit` 
         LEFT JOIN `groupactionlimit` ON ( 
                    `actionlimit`.`Action_id` = `groupactionlimit`.`Action_id` AND 
                    `actionlimit`.`LimitType_id` = `groupactionlimit`.`LimitType_id`
                  )
         LEFT JOIN `organisationactionlimit` ON ( 
                    `actionlimit`.`Action_id` =  `organisationactionlimit`.`Action_id` AND
                    `actionlimit`.`LimitType_id` = `organisationactionlimit`.`LimitType_id`
                  ) 
         )
         WHERE 
         `groupactionlimit`.`Group_id` = '", _groupId, "' and ",
         "`organisationactionlimit`.`Organisation_id` = ", _companyId, ";");

         PREPARE stmt FROM @select; 
         EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `fetch_organisation_customroles_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_organisation_customroles_proc`(
IN organisationId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN customRoleId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

  SET @select_roles = CONCAT("SELECT
    `customrole`.`synctimestamp`,
    `customrole`.`status_id`,
    `customrole`.`softdeleteflag`,
    `customrole`.`parent_id`,
    `customrole`.`organization_id`,
    `customrole`.`name`,
    `customrole`.`modifiedby`,
    `customrole`.`lastmodifiedts`,
    `customrole`.`id`,
    `customrole`.`description`,
    `customrole`.`createdts`,
    `customrole`.`createdby`,
    `customer`.`UserName` as `userName`,
    `membergroup`.`Name` as `parentRoleName`,
    `status`.`Description` as `statusValue`
     FROM ( `customrole` 
     LEFT JOIN `customer` ON (`customrole`.`createdby` = `customer`.`id`) 
     LEFT JOIN `status` ON (`customrole`.`status_id` = `status`.`id`)
     LEFT JOIN `membergroup` ON (`customrole`.`parent_id` = `membergroup`.`id`) )
     WHERE `customrole`.`organization_id` = ",organisationId, " and ",
     "`customrole`.`softdeleteflag` = 0");

     IF customRoleId = '' THEN
      SET @select_roles = CONCAT(@select_roles, ";");
     ELSE
      SET @select_roles = CONCAT(@select_roles, " and `customrole`.`id` =", customRoleId,";");
     END IF;
     
     PREPARE stmt FROM @select_roles; 
     EXECUTE stmt; DEALLOCATE PREPARE stmt;
      
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `customrole_actionlimits_create_proc`;
DELIMITER $$
CREATE PROCEDURE `customrole_actionlimits_create_proc`(
  IN _queryInput TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20)
)
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
            set @query = concat('INSERT INTO customroleactionlimits(customRole_id,action_id,account_id,isAllowed,limitType_id,value,createdby,modifiedby) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS  `custom_role_details_fetch_proc`;
DELIMITER $$
CREATE PROCEDURE `custom_role_details_fetch_proc`(
  IN customRoleID TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    SET @selectDetails = CONCAT("select 
    `customroleactionlimits`.`customRole_id`,
    `customroleactionlimits`.`action_id`,
    `customroleactionlimits`.`account_id`,
    `customroleactionlimits`.`isAllowed`,
    `customroleactionlimits`.`limitType_id`,
    `customroleactionlimits`.`value`,
    `accounts`.`AccountName` as `accountName`,
    `featureaction`.`isAccountLevel`,
    `featureaction`.`Type_id` as `actionType`,
    `featureaction`.`name` as `actionName`,
    `featureaction`.`description` as `actionDescription`,
    `feature`.`name` as `featureName`,
    `feature`.`description` as `featureDescription`,
    `feature`.`id` as `featureId`
     FROM ( `customroleactionlimits`
     LEFT JOIN `accounts` ON ( `customroleactionlimits`.`account_id` = `accounts`.`Account_id`)
     LEFT JOIN `featureaction` ON ( `customroleactionlimits`.`action_id` = `featureaction`.`id`)
     LEFT JOIN `feature` ON ( `featureaction`.`Feature_id` = `feature`.`id` )
     )
     WHERE `customroleactionlimits`.`customRole_id` = ", customRoleID, " ;");

    PREPARE stmt FROM @selectDetails;
    EXECUTE stmt; DEALLOCATE PREPARE stmt;

END$$
DELIMITER;

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
			LEAVE MAINLABEL;
		END IF;
		
		SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest where createdby = _customerId AND action = 'Approved');
        
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM requestapprovalmatrix WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds));
	    
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
        
        SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
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
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achfile`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achfile`.`status` = 'Pending' "),
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
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE companyId = @companyId AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
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
			`organisation`.`Name` AS `companyName`,
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
			((((`achfile`
			LEFT JOIN `customer` ON (`achfile`.`createdby` = `customer`.`id`))
			LEFT JOIN `achfileformattype` ON (`achfile`.`achFileFormatType_id` = `achfileformattype`.`id`))
			LEFT JOIN `organisation` ON (`achfile`.`companyId` = `organisation`.`id`))
			LEFT JOIN `bbrequest` ON (`achfile`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achfile`.`softDelete` = '0'
				AND (`achfile`.`companyId` = '", @companyId ,"' OR FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"'))
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

DROP PROCEDURE IF EXISTS `fetch_bbtemplate_proc`;
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
        
        SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
        
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
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE companyId = @companyId AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
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
			`organisation`.`Name` AS `companyName`,
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
			(((((((`bbtemplate`
			LEFT JOIN `customer` ON (`bbtemplate`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`bbtemplate`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`bbtemplate`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`bbtemplate`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`bbtemplate`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`bbtemplate`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
			LEFT JOIN `organisation` ON (`bbtemplate`.`companyId` = `organisation`.`id`))
			LEFT JOIN `bbrequest` ON (`bbtemplate`.`requestId` = `bbrequest`.`requestId`))
			WHERE `bbtemplate`.`softDelete` = '0'
				AND (`bbtemplate`.`companyId` = '", @companyId ,"' OR FIND_IN_SET(`bbtemplate`.`createdby`, '",@combinedIds,"'))
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
END $$
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

	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
	    
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
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`generaltransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `generaltransaction`.`status` = 'Pending' "),
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
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE companyId = @companyId AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts WHERE FIND_IN_SET(Customer_id, @combinedIds));
	    
        IF @customerAcounts is NULL THEN      
			SET @customerAcounts = "";
		END IF;
        
        SET @accountsQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND FIND_IN_SET(`generaltransaction`.`fromAccountNumber`, \"", @customerAcounts, "\") "));
        SET @companyQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND ( `generaltransaction`.`companyId` = '", @companyId ,"' OR FIND_IN_SET(`generaltransaction`.`createdby`, '",@combinedIds,"'))"));
        
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
						`organisation`.`Name` AS `companyName`,
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
				LEFT JOIN `organisation` ON (`generaltransaction`.`companyId` = `organisation`.`id`)
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

                    
DROP VIEW IF EXISTS `locationservices_view`;
CREATE VIEW `locationservices_view` AS
    SELECT 
        `location`.`id` AS `Location_id`,
        `location`.`Name` AS `Location_Name`,
        `location`.`DisplayName` AS `Location_Display_Name`,
        `location`.`Description` AS `Location_Description`,
        `location`.`PhoneNumber` AS `Location_Phone_Number`,
        `location`.`EmailId` AS `Location_EmailId`,
        `address`.`latitude` AS `Location_Latitude`,
        `address`.`logitude` AS `Location_Longitude`,
        `address`.`id` AS `Location_Address_id`,
        `location`.`IsMainBranch` AS `Location_IsMainBranch`,
        `location`.`isMobile` AS `Location_IsMobile`,
        `location`.`Status_id` AS `Location_Status_id`,
        `location`.`Type_id` AS `Location_Type_id`,
        `location`.`Code` AS `Location_Code`,
        `location`.`softdeleteflag` AS `Location_DeleteFlag`,
        `location`.`WorkSchedule_id` AS `Location_WorkScheduleId`,
        `facility`.`id` AS `Facility_id`,
        `facility`.`code` AS `Facility_code`,
        `facility`.`name` AS `Facility_name`,
        `facility`.`description` AS `Facility_description`,
        (SELECT 
                GROUP_CONCAT(`currency`.`code`
                        ORDER BY `currency`.`code` ASC
                        SEPARATOR ',')
    FROM
        (`currency`
        JOIN `locationcurrency`)
    WHERE
        ((`currency`.`code` = `locationcurrency`.`currency_code`)
            AND (`locationcurrency`.`Location_id` = `location`.`id`))) AS `currencies`,
 (SELECT 
        GROUP_CONCAT(`customersegment`.`type`
                ORDER BY `customersegment`.`id` ASC
                SEPARATOR ',')
        FROM
            (`customersegment`
            JOIN `locationcustomersegment`)
        WHERE
            ((`customersegment`.`id` = `locationcustomersegment`.`segment_id`)
                AND (`locationcustomersegment`.`Location_id` = `location`.`id`))) AS `Location_CustomerSegement`,
   `weekday`.`StartTime` AS `Weekday_StartTime`,
    `weekday`.`EndTime` AS `Weekday_EndTime`,
    `sunday`.`StartTime` AS `Sunday_StartTime`,
    `sunday`.`EndTime` AS `Sunday_EndTime`,
    `saturday`.`StartTime` AS `Saturday_StartTime`,
    `saturday`.`EndTime` AS `Saturday_EndTime`,
    CONCAT(`address`.`addressLine1`,
            ', ',
            IFNULL(`address`.`cityName`, ''),
            ', ',
            (SELECT 
                    `region`.`Name`
                FROM
                    `region`
                WHERE
                    (`region`.`id` = `address`.`Region_id`)),
            ', ',
            (SELECT 
                    `country`.`Name`
                FROM
                    `country`
                WHERE
                    `country`.`id` IN (SELECT 
                            `region`.`Country_id`
                        FROM
                            `region`
                        WHERE
                            (`region`.`id` = `address`.`Region_id`))),
            ', ',
            `address`.`zipCode`) AS `ADDRESS`
FROM
    ((((((`location`
    LEFT JOIN `locationfacility` ON ((`location`.`id` = `locationfacility`.`Location_id`)))
    LEFT JOIN `facility` ON ((`locationfacility`.`facility_id` = `facility`.`id`)))
    LEFT JOIN `dayschedule` `weekday` ON (((`location`.`WorkSchedule_id` = `weekday`.`WorkSchedule_id`)
        AND (`weekday`.`WeekDayName` = 'MONDAY'))))
LEFT JOIN `dayschedule` `sunday` ON (((`location`.`WorkSchedule_id` = `sunday`.`WorkSchedule_id`)
    AND (`sunday`.`WeekDayName` = 'SUNDAY'))))
LEFT JOIN `dayschedule` `saturday` ON (((`location`.`WorkSchedule_id` = `saturday`.`WorkSchedule_id`)
    AND (`saturday`.`WeekDayName` = 'SATURDAY'))))
JOIN `address` ON ((`address`.`id` = `location`.`Address_id`)));
					
DROP TABLE IF EXISTS `achfilesubrecord`;
DROP TABLE IF EXISTS `achfilerecord`;
DROP TABLE IF EXISTS `customrole`;
DROP TABLE IF EXISTS `customroleactionlimits`;

CREATE TABLE `customrole` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `status_id` varchar(50) NOT NULL,
  `organization_id` varchar(50) NOT NULL,
  `parent_id` varchar(50) DEFAULT NULL,
  `name` varchar(50) NOT NULL,
  `description` varchar(300) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  FOREIGN KEY (parent_id)
  REFERENCES membergroup(id)
  ON DELETE CASCADE,
  FOREIGN KEY (organization_id)
  REFERENCES organisation(id)
  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `customroleactionlimits` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `customRole_id` BIGINT(20) NOT NULL,
  `action_id` varchar(255) NOT NULL,
  `account_id` varchar(50) DEFAULT NULL,
  `isAllowed` tinyint(1) NOT NULL,
  `limitType_id` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  FOREIGN KEY (customRole_id)
  REFERENCES customrole(id)
  ON DELETE CASCADE,
  FOREIGN KEY (account_id)
  REFERENCES accounts(Account_id)
  ON DELETE CASCADE,
  FOREIGN KEY (action_id)
  REFERENCES featureaction(id)
  ON DELETE CASCADE,
  FOREIGN KEY (limitType_id)
  REFERENCES limittype(id)
  ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `achfilerecord` (
  `achFileRecordId` int(11) NOT NULL AUTO_INCREMENT,
  `achFileId` int(11) DEFAULT NULL,
  `transactionType` varchar(50) DEFAULT NULL,
  `requestType` varchar(50) DEFAULT NULL,
  `totalDebitAmount` double DEFAULT NULL,
  `totalCreditAmount` double DEFAULT NULL,
  `effectiveDate` DATE NULL,
  `offsetAccountNumber` varchar(50) DEFAULT NULL,
  `offsetTransactionType` varchar(50) DEFAULT NULL,
  `offsetAmount` double DEFAULT NULL,
  PRIMARY KEY (`achFileRecordId`),
  KEY `FK_achfilerecord_achFileId_idx` (`achFileId`),
  CONSTRAINT `FK_achfilerecord_achFileId` FOREIGN KEY (`achFileId`) REFERENCES `achfile` (`achFile_id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;

CREATE TABLE `achfilesubrecord` (
  `achFileSubRecordId` int(11) NOT NULL AUTO_INCREMENT,
  `achFileRecordId` int(11) DEFAULT NULL,
  `receiverTransactionType` varchar(50) DEFAULT NULL,
  `receiverAccountType` varchar(50) DEFAULT NULL,
  `receiverAccountNumber` varchar(50) DEFAULT NULL,
  `receiverName` varchar(50) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  PRIMARY KEY (`achFileSubRecordId`),
  KEY `FK_achfilesubrecord_achFileRecordId_idx` (`achFileRecordId`),
  CONSTRAINT `FK_achfilesubrecord_achFileRecordId` FOREIGN KEY (`achFileRecordId`) REFERENCES `achfilerecord` (`achFileRecordId`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;


DROP procedure IF EXISTS `ach_file_record_subrecord_create_proc`;
DELIMITER $$
CREATE PROCEDURE `ach_file_record_subrecord_create_proc`(
IN _recordvalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _subRecordvalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
	set @numOfRecords = LENGTH(_recordvalues) - LENGTH(REPLACE(_recordvalues, '|', '')) + 1;
	insertRecords : LOOP
		set index1 = index1 + 1;
		IF index1 = @numOfRecords + 1 THEN 
			LEAVE insertRecords;
		else
			set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_recordvalues, '|', index1), '|', -1 );
			SET @query = CONCAT('INSERT INTO achfilerecord(achFileId,offsetAccountNumber,offsetAmount,offsetTransactionType,effectiveDate,requestType,totalCreditAmount,totalDebitAmount,transactionType) VALUES (',@recordsData,');');
			PREPARE sql_query FROM @query;
			EXECUTE sql_query;
			SET @id = LAST_INSERT_ID();
			IF _subRecordvalues != '' THEN 
				SET @subRecordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_subRecordvalues, '|', index1), '|', -1 );
				IF @subRecordsData != ';' THEN
					SET @numberOfSubRecords = LENGTH(@subRecordsData) - LENGTH(REPLACE(@subRecordsData,';','')) + 1;
					SET @subRecordIndex = 1;
					createSubRecords : LOOP
						IF @subRecordIndex = @numberOfSubRecords + 1 THEN
							LEAVE createSubRecords;
						else
							set @subRecordData = SUBSTRING_INDEX(SUBSTRING_INDEX(@subRecordsData, ';', @subRecordIndex), ';', -1 );
							set @query = concat('INSERT INTO achfilesubrecord(amount,receiverAccountNumber,receiverAccountType,receiverName,receiverTransactionType,achFileRecordId) VALUES (',@subRecordData,',',@id,');');
							PREPARE sql_query FROM @query;
							EXECUTE sql_query;
						SET @subRecordIndex = @subRecordIndex + 1;
						END IF;
					END LOOP createSubRecords;
				END IF;
			END IF;
	    END IF;
	END LOOP insertRecords;
END$$

DELIMITER ;

ALTER TABLE `approvalmatrix` 
ADD COLUMN `invalid` TINYINT(1) NOT NULL DEFAULT '0' AFTER `softdeleteflag`;

ALTER TABLE `bbactedrequest` 
    DROP FOREIGN KEY `FK_bbactedrequest_user_id`;
ALTER TABLE `bbactedrequest` 
    CHANGE COLUMN `createdby` `createdby` VARCHAR(50) NULL ;
ALTER TABLE `bbactedrequest` 
    ADD CONSTRAINT `FK_bbactedrequest_user_id`
    FOREIGN KEY (`createdby`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION;
    
ALTER TABLE `achfilerecord` 
	CHANGE COLUMN `effectiveDate` `effectiveDate` TIMESTAMP NULL DEFAULT NULL ;

ALTER TABLE `achtransaction` 
	CHANGE COLUMN `effectiveDate` `effectiveDate` TIMESTAMP NULL DEFAULT NULL ;
	
DROP procedure IF EXISTS `updateRequestApprovalMatrix_proc`;

DELIMITER $$
CREATE PROCEDURE `updateRequestApprovalMatrix_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	CREATE TEMPORARY TABLE temp_request_table(
	request_id VARCHAR(50),	
	approvalMatrixId VARCHAR(50),
	requestapprovalMatrixId VARCHAR(50),
	receivedApprovals VARCHAR(50),
	numberOfApprovals VARCHAR(50),
	customerId VARCHAR(50)
	);
	
	INSERT INTO temp_request_table 
	SELECT bb.requestId,
	am.id AS approvalMatrixId,
	ram.id AS requestapprovalMatrixId,
	ram.receivedApprovals,
	ar.numberOfApprovals,
	cam.customerId 
	FROM bbrequest AS bb JOIN 
	requestapprovalmatrix AS ram 
	JOIN approvalmatrix AS am JOIN 
	customerapprovalmatrix AS cam JOIN 
	approvalrule AS ar 
	WHERE bb.requestId = ram.requestId AND 
	ram.approvalMatrixId = am.id 
	AND cam.approvalMatrixId = am.id 
	AND am.approvalruleId = ar.id 
	AND bb.requestId = _requestId 
	AND cam.customerId = _customerId COLLATE utf8_general_ci;
	
	SET @countOfTemp = (SELECT COUNT(*) FROM temp_request_table);
	IF @countOfTemp = 0 THEN
		SELECT -1 AS counter;
	ELSE
		UPDATE temp_request_table tmp SET numberOfApprovals = (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix AS cam WHERE tmp.approvalmatrixId = cam.approvalmatrixId) WHERE tmp.numberOfApprovals = -1;
		
		UPDATE requestapprovalmatrix SET `receivedApprovals` = `receivedApprovals` + 1 WHERE id IN(SELECT requestapprovalMatrixId FROM temp_request_table);
		
        DELETE FROM temp_request_table WHERE receivedApprovals >= numberOfApprovals;
        
		UPDATE temp_request_table SET `receivedApprovals` = `receivedApprovals` + 1;
		
		SELECT COUNT(*) AS counter FROM temp_request_table WHERE receivedApprovals >= numberOfApprovals;
	END IF;
	DROP TEMPORARY TABLE temp_request_table;

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
    WHERE `bbactedrequest`.`softdeleteflag` = '0'
    AND `bbactedrequest`.`requestId` = _requestId
    AND  `bbactedrequest`.`companyId` = @companyId;
 
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `auto_reject_invalid_pending_requests_proc`;

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
									REPLACE(REPLACE(Action_id, 'FILE_APPROVE', 'FILE_UPLOAD'), '_APPROVE', '_CREATE') as Action_id
									FROM 
									customeraction 
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
 

DELIMITER $$
CREATE PROCEDURE `businesstype_defaultgroup_update_proc`(
IN _businessTypeId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _isDefault varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
IF(_isDefault = '0') THEN
    UPDATE groupbusinesstype SET `isDefaultGroup` = false where `BusinessType_id` = _businessTypeId AND `Group_id` = _groupId;
ELSE
    UPDATE groupbusinesstype SET `isDefaultGroup` = false where `BusinessType_id` = _businessTypeId;
    UPDATE groupbusinesstype SET `isDefaultGroup` = true where `BusinessType_id` = _businessTypeId AND `Group_id` = _groupId;     
END IF;
END $$
DELIMITER ;

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
	
	SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE" OR id LIKE "%_UPLOAD");
	
	IF @createActions is NULL THEN      
		SET @createActions = "";
	END IF;  
	
	SELECT * FROM bbrequest WHERE requestId = _requestId AND companyId = _companyId AND FIND_IN_SET(featureActionId, @createActions);

END$$

DELIMITER ;

DROP VIEW IF EXISTS `organisationview`;

CREATE VIEW `organisationview` AS
    SELECT 
        `organisation`.`id` AS `org_id`,
        `organisation`.`Name` AS `org_Name`,
        `organisation`.`Type_Id` AS `org_typeId`,
        `organisation`.`StatusId` AS `org_status`,
        `organisationcommunication`.`Value` AS `orgcomm_Value`,
        `samplemember`.`Membership_id` AS `orgmem_memid`,
        `samplemember`.`Taxid` AS `orgmem_taxid`,
        `address`.`cityName` AS `cityName`,
        `address`.`addressLine1` AS `addressLine1`,
        `address`.`addressLine2` AS `addressLine2`,
        `address`.`zipCode` AS `zipCode`,
        `address`.`id` AS `addressId`,
        `sampleowner`.`FirstName` AS `orgown_firstName`,
        `sampleowner`.`MidleName` AS `orgown_midleName`,
        `sampleowner`.`LastName` AS `orgown_lastName`,
        `sampleowner`.`DateOfBirth` AS `orgown_dob`,
        `sampleowner`.`Ssn` AS `orgown_ssn`,
        `sampleowner`.`Email` AS `orgown_email`,
        `sampleowner`.`Phone` AS `orgown_phone`,
        `address`.`state` AS `State`,
        `address`.`country` AS `Country`,
        `customertype`.`Name` AS `TypeName`,
        `organisationaddress`.`IsPrimary` AS `IsPrimary`,
        `businesstype`.`name` AS `businessType`,
        `businesstype`.`id` AS `businessTypeId`
    FROM
        (((((((`organisation`
        JOIN `organisationcommunication`)
        JOIN `organisationaddress`)
        JOIN `address`)
        JOIN `customertype`)
        LEFT JOIN `organisationmembership` `samplemember` ON ((`organisation`.`id` = `samplemember`.`Organization_id`)))
        LEFT JOIN `organisationowner` `sampleowner` ON ((`sampleowner`.`Organization_id` = `organisation`.`id`)))
        LEFT JOIN `businesstype` ON ((`businesstype`.`id` = `organisation`.`BusinessType_id`)))
    WHERE
        ((`organisation`.`id` = `organisationcommunication`.`Organization_id`)
            AND (`organisation`.`id` = `organisationaddress`.`Organization_id`)
            AND (`organisationaddress`.`Address_id` = `address`.`id`)
            AND (`organisation`.`Type_Id` = `customertype`.`id`));

CREATE TABLE `featureactionroletype` (  `RoleType_id` varchar(50) NOT NULL,  `Action_id` varchar(255) NOT NULL,  `createdby` varchar(50) DEFAULT NULL,  `modifiedby` varchar(50) DEFAULT NULL,  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',  PRIMARY KEY (`RoleType_id`,`Action_id`),  KEY `FK_featureactionroletype_Action_id_idx` (`Action_id`),  CONSTRAINT `FK_featureactionroletype_Action_id` FOREIGN KEY (`Action_id`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,  CONSTRAINT `FK_featureactionroletype_membergrouptype` FOREIGN KEY (`RoleType_id`) REFERENCES `membergrouptype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
DROP procedure IF EXISTS `retail_accounts_proc`;

DELIMITER $$
CREATE PROCEDURE `retail_accounts_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @username = (SELECT UserName from customer where id=_customerId);
IF @username is null THEN 
  SET @username = "";
END IF;
SELECT Account_id,AccountName FROM accounts WHERE JSON_VALID(AccountHolder) AND (JSON_EXTRACT(AccountHolder, "$.username") =@username);
END$$
DELIMITER ;

ALTER TABLE `organisation` 
ADD COLUMN `rejectedReason` VARCHAR(45) NULL DEFAULT NULL AFTER `rejectedts`;
ALTER TABLE `customeraddress` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `HomeOwnership`;
ALTER TABLE `customerapplication` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `ProductId`;
ALTER TABLE `customercommunication` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `isPrimary`;
ALTER TABLE `customerpreference` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `Customer_id`;
ALTER TABLE `customersecurityimages` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `Image_id`;
ALTER TABLE `customersecurityquestions` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `CustomerAnswer`;
ALTER TABLE `customertermsandconditions` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `versionId`;
ALTER TABLE `customerentitlement` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `TransactionLimit_id`;
ALTER TABLE `customeraddress` DROP PRIMARY KEY, ADD PRIMARY KEY (`Address_id`);


DROP VIEW IF EXISTS `customer_communication_view`;
CREATE VIEW `customer_communication_view` AS
    SELECT 
        `customer`.`id` AS `customer_id`,
        `customer`.`FirstName` AS `customer_FirstName`,
        `customer`.`MiddleName` AS `customer_MiddleName`,
        `customer`.`LastName` AS `customer_LastName`,
        `customer`.`UserName` AS `customer_Username`,
        `customer`.`Salutation` AS `customer_Salutation`,
        `customer`.`Gender` AS `customer_Gender`,
        `customer`.`DateOfBirth` AS `customer_DateOfBirth`,
        `customer`.`Status_id` AS `customer_Status_id`,
        `customer`.`Ssn` AS `customer_Ssn`,
        `customer`.`MaritalStatus_id` AS `customer_MaritalStatus_id`,
        `customer`.`SpouseName` AS `customer_SpouseName`,
        `customer`.`EmployementStatus_id` AS `customer_EmployementStatus_id`,
        `customer`.`IsEnrolledForOlb` AS `customer_IsEnrolledForOlb`,
        `customer`.`IsStaffMember` AS `customer_IsStaffMember`,
        `customer`.`Location_id` AS `customer_Location_id`,
        `customer`.`PreferredContactMethod` AS `customer_PreferredContactMethod`,
        `customer`.`PreferredContactTime` AS `customer_PreferredContactTime`,
        `customercommunication`.`id` AS `customercommunication_id`,
        `customercommunication`.`Type_id` AS `customercommunication_Type_id`,
        `customercommunication`.`isPrimary` AS `customercommunication`,
        `customercommunication`.`isTypeBusiness` AS `isTypeBusiness`,
        `customercommunication`.`Value` AS `customercommunication_Value`,
        `customercommunication`.`Extension` AS `customercommunication_Extension`,
        `customercommunication`.`Description` AS `customercommunication_Description`,
        `customercommunication`.`createdby` AS `customercommunication_createdby`,
        `customercommunication`.`modifiedby` AS `customercommunication_modifiedby`,
        `customercommunication`.`createdts` AS `customercommunication_createdts`,
        `customercommunication`.`lastmodifiedts` AS `customercommunication_lastmodifiedts`,
        `customercommunication`.`synctimestamp` AS `customercommunication_synctimestamp`,
        `customercommunication`.`softdeleteflag` AS `customercommunication_softdeleteflag`
    FROM
        (`customer`
        JOIN `customercommunication` ON ((`customer`.`id` = `customercommunication`.`Customer_id`)));

DROP VIEW IF EXISTS `customeraddress_view`;		
CREATE VIEW `customeraddress_view` AS
    SELECT 
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
        `coun`.`id` AS `Country_id`,
        `a`.`cityName` AS `CityName`,
        `reg`.`Name` AS `RegionName`,
        `reg`.`Code` AS `RegionCode`,
        `coun`.`Name` AS `CountryName`,
        `coun`.`Code` AS `CountryCode`
    FROM
        ((((`customeraddress` `ca`
        JOIN `customer` `c` ON ((`ca`.`Customer_id` = `c`.`id`)))
        JOIN `address` `a` ON ((`a`.`id` = `ca`.`Address_id`)))
        JOIN `region` `reg` ON ((`reg`.`id` = `a`.`Region_id`)))
        JOIN `country` `coun` ON ((`coun`.`id` = `reg`.`Country_id`)));

DROP VIEW IF EXISTS `customeraddressmbview`;
CREATE VIEW `customeraddressmbview` AS
    SELECT 
        `ca`.`Customer_id` AS `CustomerId`,
        `ca`.`Address_id` AS `Address_id`,
        `ca`.`Type_id` AS `Type_id`,
        `ca`.`isTypeBusiness` AS `isTypeBusiness`,
        `ca`.`DurationOfStay` AS `DurationOfStay`,
        `ca`.`HomeOwnership` AS `HomeOwnership`,
        `ca`.`isPrimary` AS `isPrimary`,
        `at`.`Description` AS `AddressType`,
        `a`.`addressLine1` AS `AddressLine1`,
        `a`.`addressLine2` AS `AddressLine2`,
        `a`.`addressLine3` AS `AddressLine3`,
        `a`.`zipCode` AS `ZipCode`,
        `a`.`cityName` AS `CityName`,
        `a`.`country` AS `CountryName`,
        `a`.`state` AS `State`
    FROM
        ((`customeraddress` `ca`
        JOIN `address` `a` ON ((`a`.`id` = `ca`.`Address_id`)))
        JOIN `addresstype` `at` ON ((`ca`.`Type_id` = `at`.`id`)))
    WHERE
        (`a`.`softdeleteflag` = '0');

DROP VIEW IF EXISTS `customerbasicinfo_view`;
CREATE VIEW `customerbasicinfo_view` AS
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
        `customer`.`Status_id` AS `CustomerStatus_id`,
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
        `customer`.`IsStaffMember` AS `IsStaffMember`,
        `customer`.`Location_id` AS `Branch_id`,
        `location`.`Name` AS `Branch_name`,
        `location`.`Code` AS `Branch_code`,
        `customer`.`IsOlbAllowed` AS `IsOlbAllowed`,
        `customer`.`IsAssistConsented` AS `IsAssistConsented`,
        `customer`.`isEagreementSigned` AS `isEagreementSigned`,
        `customer`.`isCombinedUser` AS `isCombinedUser`,
        IF((`customer`.`isCombinedUser` = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            `customer`.`CustomerType_id`) AS `CustomerType_id`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail Banking,Business Banking',
            `customertype`.`Name`) AS `CustomerType_Name`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail and Business Banking User',
            `customertype`.`Description`) AS `CustomerType_Description`,
        (SELECT 
                `membergroup`.`Name`
            FROM
                `membergroup`
            WHERE
                `membergroup`.`id` IN (SELECT 
                        `customergroup`.`Group_id`
                    FROM
                        `customergroup`
                    WHERE
                        (`customer`.`id` = `customergroup`.`Customer_id`))
            LIMIT 1) AS `Customer_Role`,
        (SELECT 
                `membergroup`.`isEAgreementActive`
            FROM
                `membergroup`
            WHERE
                `membergroup`.`id` IN (SELECT 
                        `customergroup`.`Group_id`
                    FROM
                        `customergroup`
                    WHERE
                        (`customer`.`id` = `customergroup`.`Customer_id`))
            LIMIT 1) AS `isEAgreementRequired`,
        `customer`.`Organization_Id` AS `organisation_id`,
        `organisation`.`Name` AS `organisation_name`,
        ANY_VALUE(`primaryphone`.`Value`) AS `PrimaryPhoneNumber`,
        ANY_VALUE(`primaryemail`.`Value`) AS `PrimaryEmailAddress`,
        `customer`.`DocumentsSubmitted` AS `DocumentsSubmitted`,
        `customer`.`ApplicantChannel` AS `ApplicantChannel`,
        `customer`.`Product` AS `Product`,
        `customer`.`Reason` AS `Reason`
    FROM
        ((((((((`customer`
        LEFT JOIN `location` ON ((`customer`.`Location_id` = `location`.`id`)))
        LEFT JOIN `organisation` ON ((`customer`.`Organization_Id` = `organisation`.`id`)))
        LEFT JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
        LEFT JOIN `status` `customerstatus` ON ((`customer`.`Status_id` = `customerstatus`.`id`)))
        LEFT JOIN `status` `maritalstatus` ON ((`customer`.`MaritalStatus_id` = `maritalstatus`.`id`)))
        LEFT JOIN `status` `employementstatus` ON ((`customer`.`EmployementStatus_id` = `employementstatus`.`id`)))
        LEFT JOIN `customercommunication` `primaryphone` ON (((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`isPrimary` = 1)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))))
        LEFT JOIN `customercommunication` `primaryemail` ON (((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`isPrimary` = 1)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL'))))
    GROUP BY `customer`.`id`;
DROP VIEW IF EXISTS `customersecurityquestion_view`;
CREATE VIEW `customersecurityquestion_view` AS
    SELECT 
        `customersecurityquestions`.`Customer_id` AS `Customer_id`,
        `customersecurityquestions`.`SecurityQuestion_id` AS `SecurityQuestion_id`,
        `customersecurityquestions`.`CustomerAnswer` AS `CustomerAnswer`,
        `customersecurityquestions`.`isTypeBusiness` AS `isTypeBusiness`,
        `customersecurityquestions`.`createdby` AS `createdby`,
        `customersecurityquestions`.`modifiedby` AS `modifiedby`,
        `customersecurityquestions`.`createdts` AS `createdts`,
        `customersecurityquestions`.`lastmodifiedts` AS `lastmodifiedts`,
        `securityquestion`.`Question` AS `Question`,
        `securityquestion`.`Status_id` AS `QuestionStatus_id`,
        `customer`.`Status_id` AS `CustomerStatus_id`
    FROM
        ((`customersecurityquestions`
        JOIN `securityquestion` ON ((`securityquestion`.`id` = `customersecurityquestions`.`SecurityQuestion_id`)))
        JOIN `customer` ON ((`customer`.`id` = `customersecurityquestions`.`Customer_id`)));

DROP VIEW IF EXISTS `customerview`;
CREATE VIEW `customerview` AS
    SELECT 
        `c`.`id` AS `id`,
        `c`.`FirstName` AS `FirstName`,
        `c`.`LastName` AS `LastName`,
        `c`.`UserName` AS `UserName`,
        `cc`.`Value` AS `Value`,
        `cc`.`isTypeBusiness` AS `isTypeBusiness`,
        `ct`.`Description` AS `description`
    FROM
        ((`customercommunication` `cc`
        JOIN `customer` `c`)
        JOIN `communicationtype` `ct`)
    WHERE
        ((`cc`.`Type_id` = `ct`.`id`)
            AND (`cc`.`Customer_id` = `c`.`id`));
			
DROP VIEW IF EXISTS `cardaccountrequest_view`;
CREATE VIEW `cardaccountrequest_view` AS 
select `cardaccountrequest`.`id` AS `Request_id`,
`cardaccountrequest`.`Date` AS `Date`,
`cardaccountrequesttype`.`DisplayName` AS `Type`,
`cardaccountrequest`.`CardAccountNumber` AS `CardAccountNumber`,
`cardaccountrequest`.`CardAccountName` AS `CardAccountName`,
`cardaccountrequest`.`RequestReason` AS `Reason`, 
`cardaccountrequest`.`AccountType` AS `AccountType`,
(select `status`.`Description` from `status` where (`cardaccountrequest`.`Status_id` = `status`.`id`) limit 1) AS `Status`,`cardaccountrequest`.`Customer_id` AS `CustomerId`,`customercommunication`.`Value` AS `CommunicationValue`,`communicationtype`.`Description` AS `DeliveryMode`,concat_ws(`address`.`addressLine1`,`address`.`addressLine2`,`address`.`addressLine3`,`city`.`Name`,`region`.`Name`,`country`.`Name`,`address`.`zipCode`) AS `Address` from (((((((`cardaccountrequest` left join `cardaccountrequesttype` on((`cardaccountrequest`.`RequestType_id` = `cardaccountrequesttype`.`id`))) left join `address` on((`cardaccountrequest`.`Address_id` = `address`.`id`))) left join `city` on((`address`.`City_id` = `city`.`id`))) left join `region` on((`address`.`Region_id` = `region`.`id`))) left join `country` on((`city`.`Country_id` = `country`.`id`))) left join `customercommunication` on((`cardaccountrequest`.`Communication_id` = `customercommunication`.`id`))) left join `communicationtype` on((`customercommunication`.`Type_id` = `communicationtype`.`id`)));

DROP VIEW IF EXISTS `transactionfeesenduser_view`;
CREATE VIEW `transactionfeesenduser_view` AS select `customerentitlement`.`Customer_id` AS `customer_id`,
`customerentitlement`.`Service_id` AS `Service_id`,
`customerentitlement`.`isTypeBusiness` AS `isTypeBusiness`,
`customerentitlement`.`TransactionFee_id` AS `TransactionFee_id`,
`customerentitlement`.`TransactionLimit_id` AS `TransactionLimit_id`,
`transactionfee`.`Description` AS `transactionfee_Description`,
`transactionfeeslab`.`MinimumTransactionValue` AS `MinimumTransactionValue`,
`transactionfeeslab`.`MaximumTransactionValue` AS `MaximumTransactionValue`,
`transactionfeeslab`.`Currency` AS `Currency`,`transactionfeeslab`.`Fees` AS `Fees`,
`transactionfeeslab`.`id` AS `transactionFeeSlab_id` from ((`transactionfee` join `customerentitlement` on((`customerentitlement`.`TransactionFee_id` = `transactionfee`.`id`))) join `transactionfeeslab` on((`transactionfeeslab`.`TransactionFee_id` = `transactionfee`.`id`)));
 
 
DROP PROCEDURE IF EXISTS `customer_basic_info_proc`;
DELIMITER $$
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
DROP PROCEDURE IF EXISTS `customer_eagreement_get_proc`;
DELIMITER $$
CREATE  PROCEDURE `customer_eagreement_get_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select membergroup.isEAgreementActive AS isEAgreementActive from 
	membergroup 
    LEFT JOIN customergroup on (customergroup.Group_id = membergroup.id)
    where
    customergroup.Customer_id = _customerId AND membergroup.Type_id = 'TYPE_ID_BUSINESS';
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customer_search_proc`;
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
				customer.UserName as Username,customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender,CONCAT('****', RIGHT(customer.Ssn, 4)) as Ssn,
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

	
	PREPARE stmt FROM @queryStatement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;	


ALTER TABLE `organisation` 
CHANGE COLUMN `rejectedts` `rejectedts` TIMESTAMP NULL DEFAULT NULL ;
ALTER TABLE `organisation` ADD COLUMN `FaxId` VARCHAR(45) NULL DEFAULT NULL AFTER `StatusId`;

DROP VIEW IF EXISTS `organisationemployeesview`;
CREATE VIEW `organisationemployeesview` AS
    SELECT 
        `organisationemployees`.`id` AS `orgemp_id`,
        `organisationemployees`.`Organization_id` AS `orgemp_orgid`,
        `organisationemployees`.`Customer_id` AS `orgemp_cusid`,
        `organisationemployees`.`isAuthSignatory` AS `isAuthSignatory`,
        `organisationemployees`.`Is_Admin` AS `isOwner`,
        `customer`.`id` AS `customer_id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`DrivingLicenseNumber` AS `DrivingLicenseNumber`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customercommunication`.`id` AS `custcomm_id`,
        `customercommunication`.`Type_id` AS `custcomm_typeid`,
        `customercommunication`.`Customer_id` AS `custcomm_custid`,
        `customercommunication`.`Value` AS `custcomm_value`,
		`customercommunication`.`isTypeBusiness` AS `custcomm_istypebusiness`,
        `customer`.`Status_id` AS `Status_id`,
        `customer`.`createdts` AS `createdts`,
        `customer`.`Lastlogintime` AS `Lastlogintime`,
        `customergroup`.`Group_id` AS `Group_id`,
        `membergroup`.`Name` AS `role_name`,
        `customer`.`createdby` AS `createdby`,
        `signatorytype`.`id` AS `signatorytypeId`,
        `signatorytype`.`name` AS `signatorytypeName`
    FROM
        ((((((`organisationemployees`
        JOIN `customer` ON ((`organisationemployees`.`Customer_id` = `customer`.`id`)))
        LEFT JOIN `customercommunication` ON ((`customer`.`id` = `customercommunication`.`Customer_id`)))
        LEFT JOIN `customergroup` ON ((`customer`.`id` = `customergroup`.`Customer_id`)))
        LEFT JOIN `customerbusinesstype` ON ((`customerbusinesstype`.`Customer_id` = `customer`.`id`)))
        LEFT JOIN `signatorytype` ON ((`signatorytype`.`id` = `customerbusinesstype`.`SignatoryType_id`)))
        JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`) AND (`membergroup`.`Type_id` = 'TYPE_ID_BUSINESS')));
	
drop view if exists achtransaction_view;
CREATE VIEW `achtransaction_view` AS
    SELECT 
        `achtransaction`.`transaction_id` AS `transaction_id`,
        `achtransaction`.`fromAccount` AS `fromAccount`,
        `achtransaction`.`effectiveDate` AS `effectiveDate`,
        `achtransaction`.`requestId` AS `requestId`,
        `achtransaction`.`createdby` AS `createdby`,
        `achtransaction`.`roleId` AS `roleId`,
        `achtransaction`.`createdts` AS `createdts`,
        `achtransaction`.`maxAmount` AS `maxAmount`,
        `achtransaction`.`status` AS `status`,
        `achtransaction`.`transactionType_id` AS `transactionType_id`,
        `achtransaction`.`templateType_id` AS `templateType_id`,
        `achtransaction`.`companyId` AS `companyId`,
        `achtransaction`.`templateRequestType_id` AS `templateRequestType_id`,
        `achtransaction`.`softDelete` AS `softDelete`,
        `achtransaction`.`templateName` AS `templateName`,
        `achtransaction`.`confirmationNumber` AS `confirmationNumber`,
        `achtransaction`.`actedBy` AS `actedBy`,
        `achtransaction`.`template_id` AS `template_id`,
        `achtransaction`.`updatedts` AS `updatedts`,
        `achtransaction`.`totalAmount` AS `totalAmount`,
        `achtransaction`.`featureActionId` AS `featureActionId`,
        `achtransaction`.`fromAccount` AS `fromAccountNumber`,
        `achtransaction`.`totalAmount` AS `amount`,
        CAST(`achtransaction`.`effectiveDate` AS DATETIME) AS `scheduledDate`
    FROM
        `achtransaction`;

		
		
drop view if exists achfile_view;
CREATE VIEW `achfile_view` AS
    SELECT 
    	`achfile`.`featureActionId` AS `featureActionId`,
        `achfile`.`companyId` AS `companyId`,
        `achfile`.`createdby` AS `createdby`,
        `achfile`.`createdts` AS `createdts`,
        `achfile`.`status` AS `status`,
        `achfile`.`roleId` AS `roleId`,
        CAST(`achfilerecord`.`effectiveDate` AS DATETIME) AS `scheduledDate`,
        `achfilerecord`.`offsetAmount` AS `amount`,
        `achfilerecord`.`offsetAccountNumber` AS `fromAccountNumber`
    FROM
        (`achfile`
        JOIN `achfilerecord` ON ((`achfile`.`achFile_id` = `achfilerecord`.`achFileId`)));

drop view if exists wiretransfers_view;
CREATE VIEW `wiretransfers_view` AS
    SELECT 
    	`wiretransfers`.`featureActionId` AS `featureActionId`,
        `wiretransfers`.`amount` AS `amount`,
        `wiretransfers`.`roleId` AS `roleId`,
        `wiretransfers`.`companyId` AS `companyId`,
        `wiretransfers`.`createdby` AS `createdby`,
        `wiretransfers`.`fromAccountNumber` AS `fromAccountNumber`,
        `wiretransfers`.`createdts` AS `createdts`,
        `wiretransfers`.`status` AS `status`,
        `wiretransfers`.`createdts` AS `scheduledDate`
    FROM
        `wiretransfers`;
        
CREATE TABLE `billpaypayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `payeeId` varchar(50) COLLATE utf8_bin NOT NULL,
  `customerId` varchar(50) COLLATE utf8_bin NOT NULL,
  `companyId` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBusinessPayee` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `intrabankpayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `payeeId` varchar(50) COLLATE utf8_bin NOT NULL,
  `customerId` varchar(50) COLLATE utf8_bin NOT NULL,
  `companyId` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBusinessPayee` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

CREATE TABLE `internationalpayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `payeeId` varchar(50) COLLATE utf8_bin NOT NULL,
  `customerId` varchar(50) COLLATE utf8_bin NOT NULL,
  `companyId` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBusinessPayee` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`Id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
  
CREATE OR REPLACE 
VIEW `organisationview` AS
    SELECT 
        `organisation`.`id` AS `org_id`,
        `organisation`.`Name` AS `org_Name`,
        `organisation`.`Type_Id` AS `org_typeId`,
        `organisation`.`StatusId` AS `org_status`,
        `organisation`.`FaxId` AS `org_faxid`,
        `organisationcommunication`.`Value` AS `orgcomm_Value`,
        `samplemember`.`Membership_id` AS `orgmem_memid`,
        `samplemember`.`Taxid` AS `orgmem_taxid`,
        `address`.`cityName` AS `cityName`,
        `address`.`addressLine1` AS `addressLine1`,
        `address`.`addressLine2` AS `addressLine2`,
        `address`.`zipCode` AS `zipCode`,
        `address`.`id` AS `addressId`,
        `sampleowner`.`FirstName` AS `orgown_firstName`,
        `sampleowner`.`MidleName` AS `orgown_midleName`,
        `sampleowner`.`LastName` AS `orgown_lastName`,
        `sampleowner`.`DateOfBirth` AS `orgown_dob`,
        `sampleowner`.`Ssn` AS `orgown_ssn`,
        `sampleowner`.`Email` AS `orgown_email`,
        `sampleowner`.`Phone` AS `orgown_phone`,
        `address`.`state` AS `State`,
        `address`.`country` AS `Country`,
        `customertype`.`Name` AS `TypeName`,
        `organisationaddress`.`IsPrimary` AS `IsPrimary`,
        `businesstype`.`name` AS `businessType`,
        `businesstype`.`id` AS `businessTypeId`
    FROM
        (((((((`organisation`
        LEFT JOIN `organisationcommunication` ON ((`organisation`.`id` = `organisationcommunication`.`Organization_id`)))
        LEFT JOIN `organisationaddress` ON ((`organisation`.`id` = `organisationaddress`.`Organization_id`)))
        LEFT JOIN `address` ON ((`organisationaddress`.`Address_id` = `address`.`id`)))
        LEFT JOIN `customertype` ON ((`organisation`.`Type_Id` = `customertype`.`id`)))
        LEFT JOIN `organisationmembership` `samplemember` ON ((`organisation`.`id` = `samplemember`.`Organization_id`)))
        LEFT JOIN `organisationowner` `sampleowner` ON ((`sampleowner`.`Organization_id` = `organisation`.`id`)))
        LEFT JOIN `businesstype` ON ((`businesstype`.`id` = `organisation`.`BusinessType_id`)));
		
CREATE TABLE `wiretransferspayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `payeeId` varchar(50) COLLATE utf8_bin NOT NULL,
  `customerId` varchar(50) COLLATE utf8_bin NOT NULL,
  `companyId` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBusinessPayee` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
 
DROP PROCEDURE IF EXISTS `achtransactions_fetch_records_subrecords_proc`;
DELIMITER $$
CREATE PROCEDURE `achtransactions_fetch_records_subrecords_proc`(
    IN `_transactionId` INT 
)
LANGUAGE SQL
NOT DETERMINISTIC
CONTAINS SQL
SQL SECURITY DEFINER
COMMENT ''
BEGIN
    SELECT 
        
        `achTransaction`.`transaction_id`,
        `achTransaction`.`fromAccount`,
        `achTransaction`.`effectiveDate`,
        `achTransaction`.`requestId`,
        `achTransaction`.`createdby`,
        `achTransaction`.`createdts` AS `createdOn`,
        `achTransaction`.`maxAmount`,
        `achTransaction`.`status`,
        `achTransaction`.`transactionType_id`,
        `achTransaction`.`templateType_id`,
        `achTransaction`.`companyId`,
        `achTransaction`.`templateRequestType_id`,
        `achTransaction`.`templateName`,
        `achTransaction`.`confirmationNumber`,
        `achTransaction`.`actedBy`,
        `achTransaction`.`template_id`,
        `achTransaction`.`totalAmount`,
        `achTransaction`.`featureActionId`,
        `bbTemplateRequestType`.`templateRequestTypeName` AS `templateRequestType`,
        `achTransactionRecord`.`transactionRecord_id`,
        `achTransactionRecord`.`toAccountNumber`,
        `achTransactionRecord`.`toAccountType`,
        `achTransactionRecord`.`abatrcNumber`,
        `achTransactionRecord`.`detail_id`,
        `achTransactionRecord`.`amount`,
        `achTransactionRecord`.`additionalInfo`,
        `achTransactionRecord`.`eIN`,
        `achTransactionRecord`.`isZeroTaxDue`,
        `achTransactionRecord`.`taxType_id`,
        `achTransactionRecord`.`templateRequestType_id`,
        `achTransactionRecord`.`transaction_id` as `transaction_id_reclevel`,
        `achTransactionRecord`.`record_Name`,
        `achTransactionSubRecord`.`transcationSubRecord_id`,
        `achTransactionSubRecord`.`amount` as `subrecordamount`,
        `achTransactionSubRecord`.`taxSubCategory_id`,
        `bbTaxType`.`taxType`,
        `bbTaxSubtype`.`taxSubType`,
        `achTransactionSubRecord`.`transactionRecord_id` as `transactionRecord_id_subreclevel` 
      
        FROM (((((`achtransaction` as `achTransaction`
      LEFT JOIN `achtransactionrecord` as `achTransactionRecord` 
      ON `achTransaction`.`transaction_id` = `achTransactionRecord`.`transaction_id`)
      LEFT JOIN `achtransactionsubrecord` as `achTransactionSubRecord`
      ON `achTransactionRecord`.`transactionRecord_id` = `achTransactionSubRecord`.`transactionRecord_id`)
      LEFT JOIN `bbtaxtype` as `bbTaxType`
      ON `achTransactionRecord`.`taxType_id` = `bbTaxType`.`id`)
      LEFT JOIN `bbtaxsubtype` as `bbTaxSubtype`
      ON `achTransactionSubRecord`.`taxSubCategory_id` = `bbTaxSubtype`.`id`)
      LEFT JOIN `bbtemplaterequesttype` as `bbTemplateRequestType`
      ON `achTransaction`.`templateRequestType_id` = `bbTemplateRequestType`.`templateRequestType_id`)
      WHERE 
        `achTransaction`.`transaction_id` = _transactionId
      ORDER BY `achTransaction`.`transaction_id`,`achTransactionRecord`.`transactionRecord_id`,`achTransactionSubRecord`.`transactionRecord_id`;

END $$
DELIMITER ;     

CREATE TABLE `interbankpayee` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `typeId` varchar(45) COLLATE utf8_bin DEFAULT NULL,
  `payeeId` varchar(50) COLLATE utf8_bin NOT NULL,
  `customerId` varchar(50) COLLATE utf8_bin NOT NULL,
  `companyId` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `cif` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  `isBusinessPayee` varchar(50) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
   
   
DROP VIEW IF EXISTS locationdetails_view; 
CREATE VIEW `locationdetails_view` AS
     SELECT DISTINCT
        `location`.`id` AS `locationId`,
        (SELECT 
                GROUP_CONCAT(DISTINCT CONCAT(UPPER(LEFT(`dayschedule`.`WeekDayName`, 1)),
                                LOWER(SUBSTR(`dayschedule`.`WeekDayName`, 2)),
                                ':',
                                CONVERT( SUBSTR(`dayschedule`.`StartTime`, 1, 5) USING UTF8),
                                '-',
                                CONVERT( SUBSTR(`dayschedule`.`EndTime`, 1, 5) USING UTF8))
                        SEPARATOR ' || ')
            FROM
                (`dayschedule`
                JOIN `location` `l`)
            WHERE
                ((`dayschedule`.`WorkSchedule_id` = `l`.`WorkSchedule_id`)
                    AND (`l`.`id` = `location`.`id`))) AS `workingHours`,
        `location`.`Name` AS `informationTitle`,
        `location`.`Name` AS `name`,
        `location`.`Description` AS `Description`,
        `location`.`Code` AS `Code`,
        `location`.`PhoneNumber` AS `phoneNumber`,
        `location`.`EmailId` AS `email`,
        (CASE `location`.`softdeleteflag`
            WHEN '0' THEN 'OPEN'
            ELSE 'CLOSED'
        END) AS `status`,
        `location`.`Status_id` AS `isVisible`,
        `location`.`Type_id` AS `type`,
        `location`.`isMobile` AS `isMobile`,
        `location`.`IsMainBranch` AS `IsMainBranch`,
        (SELECT 
                GROUP_CONCAT(`facility`.`name`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR '||')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) = `location`.`id`))) AS `services`,
        (SELECT 
                GROUP_CONCAT(`currency`.`code`
                        ORDER BY `currency`.`code` ASC
                        SEPARATOR ',')
            FROM
                (`currency`
                JOIN `locationcurrency`)
            WHERE
                ((`currency`.`code` = CONVERT( `locationcurrency`.`currency_code` USING UTF8))
                    AND (CONVERT( `locationcurrency`.`Location_id` USING UTF8) = `location`.`id`))) AS `currencies`,
        (SELECT 
                GROUP_CONCAT(`customersegment`.`type`
                        ORDER BY `customersegment`.`type` ASC
                        SEPARATOR ',')
            FROM
                (`customersegment`
                JOIN `locationcustomersegment`)
            WHERE
                ((`customersegment`.`id` = CONVERT( `locationcustomersegment`.`segment_id` USING UTF8))
                    AND (CONVERT( `locationcustomersegment`.`Location_id` USING UTF8) = `location`.`id`))) AS `segments`,
        (SELECT 
                GROUP_CONCAT(`facility`.`name`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR ',')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) = `location`.`id`))) AS `facilities_names`,
        (SELECT 
                GROUP_CONCAT(`facility`.`code`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR ',')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) = `location`.`id`))) AS `facilities_codes`,
        (SELECT 
                `address`.`cityName`
            FROM
                `address`
            WHERE
                (`address`.`id` = `location`.`Address_id`)) AS `city`,
        (SELECT 
                `region`.`Name`
            FROM
                `region`
            WHERE
                (`region`.`id` = `address`.`Region_id`)) AS `region`,
        (SELECT 
                `country`.`Name`
            FROM
                `country`
            WHERE
                (`country`.`id` = (SELECT 
                        `region`.`Country_id`
                    FROM DUAL WHERE
                        (`region`.`id` = `address`.`Region_id`)))) AS `country`,
        `address`.`addressLine1` AS `addressLine1`,
        `address`.`addressLine2` AS `addressLine2`,
        `address`.`addressLine3` AS `addressLine3`,
        `address`.`zipCode` AS `zipCode`,
        `address`.`latitude` AS `latitude`,
        `address`.`logitude` AS `longitude`
    FROM
        ((`address`
        JOIN `location`)
        JOIN `region`)
    WHERE
        ((`address`.`id` = `location`.`Address_id`)
            AND (`region`.`id` = `address`.`Region_id`));
			
			
drop view if exists customeraccountsview;
CREATE VIEW `customeraccountsview` AS select distinct `accounts`.`Membership_id` AS `Membership_id`,
`accounts`.`MembershipName` AS `MembershipName`,
`accounts`.`TaxId` AS `Taxid`,
`customeraccounts`.`Customer_id` AS `Customer_id`,
`customeraccounts`.`Customer_id` AS `User_id`,
`accounts`.`Account_id` AS `Account_id`,
`accounts`.`isBusinessAccount` AS `isBusinessAccount`,
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
`accounts`.`PaymentTerm` AS `paymentTerm`,`accounts`.`OpeningDate` AS `openingDate`,
`accounts`.`MaturityDate` AS `maturityDate`,`accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
`accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,`accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
`accounts`.`DividendRate` AS `dividendRate`,`accounts`.`DividendYTD` AS `dividendYTD`,
`accounts`.`EStatementmentEnable` AS `eStatementEnable`,
`customeraccounts`.`IsOrganizationAccount` AS `isOrganizationAccount`,
`customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,`accounts`.`StatusDesc` AS `statusDesc`,
`accounts`.`NickName` AS `nickName`,`accounts`.`OriginalAmount` AS `originalAmount`,
`accounts`.`OutstandingBalance` AS `outstandingBalance`,`accounts`.`PaymentDue` AS `paymentDue`,
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
`accounts`.`phone` AS `phoneId`,`accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
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
`accounts`.`IsPFM` AS `isPFM`,`accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
`accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
`accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
`accounts`.`InterestEarned` AS `interestEarned`,`accounts`.`CurrentAmountDue` AS `currentAmountDue`,
`accounts`.`CreditLimit` AS `creditLimit`,`accounts`.`CreditCardNumber` AS `creditCardNumber`,
`accounts`.`BsbNum` AS `bsbNum`,`accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
`accounts`.`BondInterest` AS `bondInterest`,`accounts`.`AvailablePoints` AS `availablePoints`,
`accounts`.`AccountName` AS `accountName`,`accounts`.`email` AS `email`,
`accounts`.`IBAN` AS `IBAN`,`accounts`.`adminProductId` AS `adminProductId`,
`accounts`.`UpdatedBy` AS `UpdatedBy`,`accounts`.`LastUpdated` AS `LastUpdated`,
`accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,`bank`.`Description` AS `bankname`,
`accounts`.`AccountPreference` AS `accountPreference`,`accounttype`.`transactionLimit` AS `transactionLimit`,
`accounttype`.`transferLimit` AS `transferLimit`,`accounttype`.`rates` AS `rates`,`accounttype`.`termsAndConditions` AS `termsAndConditions`,
`accounttype`.`TypeDescription` AS `typeDescription`,`accounttype`.`supportChecks` AS `supportChecks`,
`accounttype`.`displayName` AS `displayName`,`accounts`.`accountSubType` AS `accountSubType`,
`accounts`.`description` AS `description`,`accounts`.`schemeName` AS `schemeName`,
`accounts`.`identification` AS `identification`,`accounts`.`secondaryIdentification` AS `secondaryIdentification`,
`accounts`.`servicerSchemeName` AS `servicerSchemeName`,`accounts`.`servicerIdentification` AS `servicerIdentification`,
`accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,`accounts`.`dataType` AS `dataType`,
`accounts`.`dataDateTime` AS `dataDateTime`,
`accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
`accounts`.`dataCreditLineType` AS `dataCreditLineType`,
`accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
`accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency` from (((((`accounts` join `customeraccounts`) join `accounttype`) left join `membershipaccounts` on((`accounts`.`Account_id` = `membershipaccounts`.`accountId`))) left join `membership` on((`membership`.`id` = `membershipaccounts`.`membershipId`))) left join `bank` on((`accounts`.`Bank_id` = `bank`.`id`))) where ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`) and (`accounts`.`Type_id` = `accounttype`.`TypeID`));
            

DROP procedure IF EXISTS `location_range_proc`;

DELIMITER $$

CREATE  PROCEDURE `location_range_proc`(
  in _currLatitude TEXT(50), 
  in _currLongitude TEXT(50), 
  in _radius float(50) )
BEGIN DECLARE rowLatitide TEXT(2000);
DECLARE rowLongitude TEXT(2000);
DECLARE b int(1);
DECLARE pipeFlag int(1);
set _radius=_radius/1000;
SELECT 
  location.id AS locationId, 
  (
    SELECT 
      GROUP_CONCAT(
        DISTINCT CONCAT(
          UCASE(
            LEFT(dayschedule.weekdayname, 1)
          ), 
          LCASE(
            SUBSTRING(dayschedule.weekdayname, 2)
          ), 
          ':', 
          SUBSTRING(dayschedule.StartTime, 1, 5), 
          '-', 
          SUBSTRING(dayschedule.endTime, 1, 5)
        ) SEPARATOR ' || '
      ) 
    FROM 
      dayschedule, 
      location 
    WHERE 
      dayschedule.WorkSchedule_id = location.WorkSchedule_id 
      and location.id = locationId
  ) AS workingHours, 
  location.name AS informationTitle, 
  location.phoneNumber AS phone, 
  location.emailId AS email, 
  CASE `location`.`softdeleteflag` WHEN '0' THEN 'OPEN' ELSE 'CLOSED' END AS status, 
  CASE `location`.`type_id` WHEN 'ATM' THEN 'ATM' ELSE 'BRANCH'  END AS type,
  location.status_id AS isVisible,
 (SELECT 
                GROUP_CONCAT(`facility`.`name`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR '||')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) =`location`.`id`))) AS  services, 
  (
    SELECT 
      address.cityName
    FROM 
      address
    WHERE 
      address.id = location.Address_id
  ) AS city, 
  address.addressLine1 AS addressLine1, 
  address.addressLine2 AS addressLine2, 
  address.addressLine3 AS addressLine3, 
  address.zipCode AS zipCode, 
  address.latitude AS latitude, 
  address.logitude AS longitude, 
  (
    6371 * 2 * ASIN(
      SQRT(
        POWER(
          SIN(
            (
              latitude - ABS(_currLatitude)
            ) * PI() / 180 / 2
          ), 
          2
        ) + COS(
          latitude * PI() / 180
        ) * COS(
          ABS(_currLatitude) * PI() / 180
        ) * POWER(
          SIN(
            (Logitude - _currLongitude) * PI() / 180 / 2
          ), 
          2
        )
      )
    )
  ) AS distance 
FROM 
  address, 
  location 
WHERE 
  address.id = location.address_id 
HAVING 
  distance <= _radius AND isVisible= 'SID_ACTIVE';
  
  
  END$$

DELIMITER ;


DROP procedure IF EXISTS `location_search_proc`;

DELIMITER $$

CREATE  PROCEDURE `location_search_proc`(
  in _searchKeyword TEXT(1000)
)
BEGIN DECLARE _next TEXT DEFAULT NULL;
DECLARE _nextlen INT DEFAULT NULL;
DECLARE _value TEXT DEFAULT NULL;
DECLARE sql1 TEXT (200000);
DECLARE sqlFrontPart TEXT (200000);
DECLARE sqlresult TEXT (200000) default null;
declare counter int(50);
set 
  @sqlFrontPart = 'SELECT * FROM locationdetails_view where isVisible = "SID_ACTIVE" AND';
set 
  @counter = 1;
iterator : LOOP IF LENGTH(
  TRIM(_searchKeyword)
) = 0 
OR _searchKeyword IS NULL THEN LEAVE iterator;
END IF;
SET 
  _next = SUBSTRING_INDEX(_searchKeyword, ',', 1);
SET 
  _nextlen = LENGTH(_next);
SET 
  _value = LCASE(
    TRIM(_next)
  );
set 
  @sql1 = null;
set 
  @var = null;
set 
  @var = REPLACE (
    trim(_value), 
    ' ', 
    '%'
  );
set 
  @sql1 = concat(
    '( 
 LOWER(city) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(informationTitle) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(name) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(Description) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(Code) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(phoneNumber) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(email) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(status) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(type) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(services) like (', '"', '%', 
    @var, '%', '"', ') 
  or LOWER(region) like (', '"', '%', 
    @var, '%', '"', ')
  or LOWER(addressLine1) like (', 
    '"', '%', @var, '%', '"', ') 
  or LOWER(addressLine2) like (', 
    '"', '%', @var, '%', '"', ') 
  or LOWER(addressLine3) like (', 
    '"', '%', @var, '%', '"', ') 
  or LOWER(country) like (', 
    '"', '%', @var, '%', '"', ') 
  or LOWER(zipcode) like (', 
    '"', '%', @var, '%', '"', '))'
  );
if @counter = 1 then 
set 
  @sqlresult = @sql1;
else 
set 
  @sqlresult = concat(@sql1, ' AND ', @sqlresult);
end if;
set 
  @counter = @counter + 1;

SET 
  _searchKeyword = INSERT(_searchKeyword, 1, _nextlen + 1, '');
END LOOP;

set 
  @sqlresult = concat(
    '(', @sqlFrontPart, @sqlresult, ')'
  );
PREPARE stmt1 
from 
  @sqlresult;
EXECUTE stmt1;

END$$

DELIMITER ;

ALTER TABLE `payee` ADD COLUMN `organizationId` VARCHAR(45) NULL DEFAULT NULL AFTER `transitDays`;

ALTER TABLE `externalaccount` ADD COLUMN `organizationId` VARCHAR(45) NULL DEFAULT NULL AFTER `user_id`;

DROP procedure IF EXISTS `Location_address_suggestions_proc`;
DELIMITER $$
CREATE  PROCEDURE `Location_address_suggestions_proc`(
  in _currLatitude TEXT(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
  in _currLongitude TEXT(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
  in _radius float(50) ,
  in _var TEXT(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN DECLARE rowLatitide TEXT(2000);
DECLARE rowLongitude TEXT(2000);
DECLARE b int(1);
DECLARE pipeFlag int(1);
set _var = concat('%',_var,'%');
set _radius= _radius/1000;
SELECT 
  location.id AS locationId, 
  (
    SELECT 
      GROUP_CONCAT(
        DISTINCT CONCAT(
          UCASE(
            LEFT(dayschedule.weekdayname, 1)
          ), 
          LCASE(
            SUBSTRING(dayschedule.weekdayname, 2)
          ), 
          ':', 
          SUBSTRING(dayschedule.StartTime, 1, 5), 
          '-', 
          SUBSTRING(dayschedule.endTime, 1, 5)
        ) SEPARATOR ' || '
      ) 
    FROM 
      dayschedule, 
      location 
    WHERE 
      dayschedule.WorkSchedule_id = location.WorkSchedule_id 
      and location.id = locationId
  ) AS workingHours,
  location.name AS informationTitle,
  location.Description AS description,
  location.phoneNumber AS phone, 
  location.emailId AS email, 
  CASE `location`.`softdeleteflag` WHEN 'SID_ACTIVE' THEN 'OPEN' ELSE 'CLOSED' END AS status, 
  CASE `location`.`type_id` WHEN 'ATM' THEN 'ATM' ELSE 'BRANCH'  END AS type,
  location.status_id AS isVisible,
  (SELECT 
                GROUP_CONCAT(`facility`.`name`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR '||')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) =`location`.`id`))) AS  services, 
  (
    SELECT 
      address.cityName
    FROM 
      address
    WHERE 
      address.id = location.Address_id
  ) AS city,
  (SELECT 
                region.Name
            FROM
                region
            WHERE
                (region.id = address.Region_id)) AS Region,
	(SELECT 
                country.Name
            FROM
                country
            WHERE
                (country.id = (SELECT 
                        region.Country_id
                    FROM region WHERE
                        (region.id = address.Region_id)))) AS country,
  address.addressLine1 AS addressLine1, 
  address.addressLine2 AS addressLine2, 
  address.addressLine3 AS addressLine3, 
  address.zipCode AS zipCode,
  address.latitude AS latitude, 
  address.logitude AS longitude, 
  (
    6371 * 2 * ASIN(
      SQRT(
        POWER(
          SIN(
            (
              latitude - ABS(_currLatitude)
            ) * PI() / 180 / 2
          ), 
          2
        ) + COS(
          latitude * PI() / 180
        ) * COS(
          ABS(_currLatitude) * PI() / 180
        ) * POWER(
          SIN(
            (Logitude - _currLongitude) * PI() / 180 / 2
          ), 
          2
        )
      )
    )
  ) AS distance 
FROM 
  address, 
  location 
WHERE 
  address.id = location.address_id 
HAVING 
  distance <= _radius AND (city like _var or type like _var or Region like _var  or informationTitle like _var or phone like _var or country like _var) AND isVisible= 'SID_ACTIVE'  ;

END$$

DELIMITER ;

ALTER TABLE `customerrequest` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL AFTER `softdeleteflag`;
ALTER TABLE `cardaccountrequest` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL AFTER `softdeleteflag`;
ALTER TABLE `customerdevice` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL AFTER `softdeleteflag`;
ALTER TABLE `notificationcardinfo` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL AFTER `softdeleteflag`;
ALTER TABLE `customer` ADD COLUMN `combinedUserId` VARCHAR(45) NULL AFTER `sigtype`;

ALTER TABLE `application` 
ADD COLUMN `stopReasons` VARCHAR(400) NULL DEFAULT NULL AFTER `timeZoneOffset`;

DELIMITER $$
CREATE PROCEDURE `customer_group_actions_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 100000000;

SET @isCombinedUser = (SELECT isCombinedUser from customer where id=_customerId);

IF @isCombinedUser = '1' THEN
 
SET @customer_disabled_actions = (SELECT group_concat(Action_id SEPARATOR ",") from customeraction where isAllowed = '0' AND Account_id is NULL AND Customer_id =_customerId);

IF @customer_disabled_actions is null THEN 
      SET @customer_disabled_actions = "";
END IF;

SET @customer_groups = (SELECT group_concat(Group_id SEPARATOR ",") from customergroup where Customer_id = _customerId and Group_id IN (select id from membergroup where Type_id = 'TYPE_ID_RETAIL') );

IF @customer_groups is null THEN 
  SET @customer_groups = "";
END IF;

SET @group_actions = (SELECT group_concat(Action_id SEPARATOR ",") from groupactionlimit where FIND_IN_SET(Group_id, @customer_groups));

IF @group_actions is null THEN 
  SET @group_actions = "";
END IF;

SET @active_feature_condition = concat("`feature`.`Status_id` = 'SID_FEATURE_ACTIVE' AND FIND_IN_SET(`featureaction`.`id`, '",@group_actions,"') ");

set @select_statement = concat("SELECT
        `customergroup`.`Customer_id` AS `Customer_id`,
        NULL AS `Account_id`,
        'true' AS `isAllowed`,
        `featureaction`.`id` AS `Action_id`,
        `feature`.`Status_id` AS `Feature_Status_id`,
        `feature`.`id` AS `Feature_id`,
		 IF(`featureaction`.`isAccountLevel` = '1', 'true', 'false') AS `isAccountLevel`,
        `membergroup`.`Type_id` AS `RoleType_id`,
        `groupactionlimit`.`LimitType_id` AS `LimitType_id`,
        `groupactionlimit`.`value` AS `value`
    FROM
        (`customergroup`
        LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id` and `membergroup`.`Type_id` = 'TYPE_ID_RETAIL')
        LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
        LEFT JOIN `featureaction` ON (`featureaction`.`id` = `groupactionlimit`.`Action_id`)
        LEFT JOIN `featureactionroletype` ON (`featureactionroletype`.`Action_id` = `featureaction`.`id` and `featureactionroletype`.`RoleType_id`= 'TYPE_ID_RETAIL')
        LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
      where `customergroup`.`Customer_id` =", quote(_customerId), " and `feature`.`Status_id` = 'SID_FEATURE_ACTIVE'
        and NOT FIND_IN_SET(`groupactionlimit`.`Action_id`, '",@customer_disabled_actions,"')
        and ", @active_feature_condition);
    
    IF(_actionId != '') THEN
        set @select_statement =  concat(@select_statement ," and `groupactionlimit`.`Action_id` = ",quote(_actionId));
      END IF;
    
    -- select @select_statement;
      PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END IF;
END$$

DELIMITER ;

DROP TABLE IF EXISTS `region_dummy`;
CREATE TABLE `region_dummy` (
  `old` VARCHAR(45) NULL,
  `new` VARCHAR(45) NULL,
  `Name` VARCHAR(45) NULL)ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
DROP PROCEDURE IF EXISTS `customrole_actionlimits_create_proc`;
DELIMITER $$
CREATE PROCEDURE `customrole_actionlimits_create_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20)
)
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
            set @query = concat('INSERT INTO customroleactionlimits(customRole_id,action_id,account_id,isAllowed,limitType_id,value,createdby,modifiedby) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;  
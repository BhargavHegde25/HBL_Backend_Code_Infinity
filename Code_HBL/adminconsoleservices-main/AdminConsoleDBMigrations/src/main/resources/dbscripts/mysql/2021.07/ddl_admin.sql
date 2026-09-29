		   

ALTER TABLE `featureaction` ADD COLUMN `isApprovalAction` tinyint(1) NOT NULL DEFAULT '0';

DROP TABLE IF EXISTS `usertype`;
CREATE TABLE `usertype` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) CHARACTER SET utf8 NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,  
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',  
  PRIMARY KEY (`id`),
  UNIQUE KEY `name_UNIQUE` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lob`;
CREATE TABLE `lob` (
  `id` VARCHAR(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,  
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',  
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `lobtext`;
CREATE TABLE `lobtext` (
  `lobId` VARCHAR(50) NOT NULL,
  `languageCode` VARCHAR(10) NOT NULL,
  `displayName` VARCHAR(255) NULL DEFAULT NULL,
  `description` VARCHAR(1000) NULL DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,  
  `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT NULL DEFAULT '0',  
  PRIMARY KEY (`lobId`, `languageCode`),
  CONSTRAINT `FK_lobtext_lob`
    FOREIGN KEY (`lobId`)
    REFERENCES `lob` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_lobtext_locale`
    FOREIGN KEY (`languageCode`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `userlob`;

CREATE TABLE `userlob` (
  `userId` varchar(50) NOT NULL,
  `lobId` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userId`,`lobId`),
  KEY `userlob_lobId_idx` (`lobId`),
  KEY `userlob_systemuserId_idx` (`userId`),
  CONSTRAINT `userlob_lobId` FOREIGN KEY (`lobId`) REFERENCES `lob` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userlob_systemuserId` FOREIGN KEY (`userId`) REFERENCES `systemuser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `internalusermanager` (
  `userId` varchar(50) NOT NULL,
  `manager` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userId`,`manager`),
  KEY `IXFK_internalusermanager_manager` (`manager`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `internalusertype` (
  `userId` varchar(50) NOT NULL,
  `userType` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`userId`,`userType`),
  KEY `IXFK_internalusertype_usertype` (`userType`),
  CONSTRAINT `FK_internalusertype_userType` FOREIGN KEY (`userType`) REFERENCES `usertype` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP VIEW IF EXISTS `lob_view`;
CREATE VIEW `lob_view` AS
    SELECT 
        `lob`.`id` AS `lob_id`,
        `lobtext`.`languageCode` AS `lobtext_languageCode`,
        `lobtext`.`description` AS `lobtext_description`,
        `lobtext`.`displayName` AS `lobtext_displayName`
    FROM
        (`lobtext`
        JOIN `lob` ON ((`lobtext`.`lobId` = `lob`.`id`)));

ALTER TABLE `useraddress` DROP FOREIGN KEY `FK_UserAddress_SystemUser`;

DROP PROCEDURE IF EXISTS `fetch_accountdetails_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_accountdetails_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _accountIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci 
    )
BEGIN 
	IF _cif = "" THEN
		SET _cif = "%";
    END IF;
    
	SELECT 
		`customeraccounts`.`Account_id`,
		`customeraccounts`.`AccountName`,
		`customeraccounts`.`accountType`,
		`contractaccounts`.`ownerType` AS `ownershipType`
	FROM (`customeraccounts` AS `customeraccounts`
		LEFT JOIN
		`contractaccounts` AS `contractaccounts`
		ON `customeraccounts`.`Account_id` = `contractaccounts`.`accountId`)
	WHERE 
		`customeraccounts`.`contractId` = `_contractId` AND
		`customeraccounts`.`coreCustomerId` LIKE `_cif` AND
		FIND_IN_SET(`customeraccounts`.`Account_id`,`_accountIds`) > 0
	ORDER BY `customeraccounts`.`contractId`,`customeraccounts`.`coreCustomerId`;
END$$
DELIMITER ;

ALTER TABLE `customeraction` ADD INDEX `IDX_contractId_coreCustomerID_Mul` (`contractId` ASC, `coreCustomerId` ASC);
ALTER TABLE `approvalmode` ADD UNIQUE INDEX `IDX_coreCustomerID_ContractId` (`contractId` ASC, `coreCustomerId` ASC);

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
		(SELECT 
				`internalusertype`.`userType`
			FROM
				`internalusertype`
			WHERE
				(`systemuser`.`id` = `internalusertype`.`userId`)
			LIMIT 1) AS `UserType`,
		(SELECT 
			`usertype`.`name`
		FROM
			`usertype`
		WHERE
			`usertype`.`id` IN (SELECT 
					`internalusertype`.`userType`
				FROM
					`internalusertype`
				WHERE
					(`systemuser`.`id` = `internalusertype`.`userId`))) AS `userTypeName`,
		(SELECT 
				`internalusermanager`.`manager`
			FROM
				`internalusermanager`
			WHERE
				(`systemuser`.`id` = `internalusermanager`.`userId`)
			LIMIT 1) AS `ReportingManager`,
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
        (SELECT IFNULL(`workaddress`.`zipCode`, '')) AS `Work_Zipcode`,
		(SELECT 
                GROUP_CONCAT(`userlob`.`lobId`
                        SEPARATOR ',')
            FROM
                `userlob`
            WHERE
                (`systemuser`.`id` = `userlob`.`userId`)) AS `lobId`,
		(SELECT 
                GROUP_CONCAT(`lobtext`.`displayName`
                        SEPARATOR ',')
            FROM
                `lobtext`
            WHERE
                `lobtext`.`lobId` IN (SELECT 
                        `userlob`.`lobId`
                    FROM
                        `userlob`
                    WHERE
                        (`systemuser`.`id` = `userlob`.`userId`))) AS `lobName`,

        (SELECT 
                `location`.`Name`
            FROM
                `location`
            WHERE
                (`workaddress`.`id` = `location`.`Address_id`)) AS `branchName`
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

DROP PROCEDURE IF EXISTS `approvalmatrix_signatorygroupmatrixcreate_proc`;
DELIMITER $$                    
CREATE PROCEDURE `approvalmatrix_signatorygroupmatrixcreate_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _signatorymatrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
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
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,approvalruleId,isGroupMatrix,limitTypeId,lowerlimit,upperlimit) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;
				
				set @sigValues = SUBSTRING_INDEX( SUBSTRING_INDEX(_signatorymatrixValues, '#', index1), '#', -1 );
				SET @id = LAST_INSERT_ID();
				SET @groupList = SUBSTRING_INDEX(@sigValues, ';', 1 );
				SET @groupRule = SUBSTRING_INDEX(@sigValues, ';', -1 );
				INSERT INTO signatorygroupmatrix(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
                
				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END$$
DELIMITER ;


DROP VIEW IF EXISTS `internaluserskc_view`;

CREATE VIEW `internaluserskc_view` AS
    SELECT 
        `internalusertype`.`userId` AS `User_id`,
        `internalusertype`.`userType` AS `UserType`,
        (SELECT 
                `internalusermanager`.`manager`
            FROM
                `internalusermanager`
            WHERE
                (`internalusertype`.`userId` = `internalusermanager`.`userId`)
            LIMIT 1) AS `ReportingManager`,
        (SELECT 
                GROUP_CONCAT(`userlob`.`lobId`
                        SEPARATOR ',')
            FROM
                `userlob`
            WHERE
                (`internalusertype`.`userId` = `userlob`.`userId`)) AS `lobId`,
        (SELECT 
                GROUP_CONCAT(`lobtext`.`displayName`
                        SEPARATOR ',')
            FROM
                `lobtext`
            WHERE
                `lobtext`.`lobId` IN (SELECT 
                        `userlob`.`lobId`
                    FROM
                        `userlob`
                    WHERE
                        (`internalusertype`.`userId` = `userlob`.`userId`))) AS `lobName`,
        (SELECT 
                `usertype`.`name`
            FROM
                `usertype`
            WHERE
                `usertype`.`id` IN (SELECT 
                        `internalusertype`.`userType`
                    FROM
                        `internalusertype`
                    WHERE
                        (`internalusertype`.`userId` = `internalusertype`.`userId`))LIMIT 1) AS `userTypeName`,
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
        (SELECT IFNULL(`workaddress`.`zipCode`, '')) AS `Work_Zipcode`,
        (SELECT 
                `location`.`Name`
            FROM
                `location`
            WHERE
                (`workaddress`.`id` = `location`.`Address_id`)) AS `branchName`,
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
            ) AS `Work_Addr`
    FROM
        ((`internalusertype`
        JOIN `internalusermanager` ON ((`internalusertype`.`userId` = `internalusermanager`.`userId`)))
        LEFT JOIN `address` `workaddress` ON (`workaddress`.`id` IN (SELECT 
                `useraddress`.`Address_id`
            FROM
                `useraddress`
            WHERE
                ((`internalusertype`.`userId` = `useraddress`.`User_id`)
                    AND (`useraddress`.`Type_id` = 'ADR_TYPE_WORK')))));
                    
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
        `p`.`isComposite` AS `isComposite`,
        `rp`.`Role_id` AS `Role_id`,
		 CAST(`p`.`softdeleteflag` AS unsigned) AS `softdeleteflag`
    FROM
    `rolepermission` `rp` , `permission` `p`
    WHERE `p`.`id` = `rp`.`Permission_id` AND `p`.`Status_id`='SID_ACTIVE' AND FIND_IN_SET(`rp`.`Role_id`,_roleIds) ORDER BY `id`;
END$$
DELIMITER ;

ALTER TABLE `userlob` DROP FOREIGN KEY `userlob_systemuserId`;
ALTER TABLE `usercompositeaction` DROP FOREIGN KEY `FK_UserCompositeAction_User`;


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

SET @customerIdListWithNoAccountAccess = (SELECT group_concat(DISTINCT Customer_id SEPARATOR ",") from
                                            ( SELECT Customer_id, Account_id
                                            from customeraccounts
                                            where FIND_IN_SET(Account_id, _accountIds)
                                            group by Customer_id having
                                            count(Account_id) != @NumberOfAccounts ) AS tempcustomeraccounts);
										  
SET @customerIdListWithNoAccountAccess = IF(@customerIdListWithNoAccountAccess is null, '', @customerIdListWithNoAccountAccess);
SELECT 
	DISTINCT (`customer`.`id` ) AS id , (`customer`.`username`) AS userName , (`membergroup`.`Name`) AS groupId,
										(`customer`.`FirstName`) AS firstName , (`customer`.`LastName`) AS lastName
from 
	(`customer`
LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and 
`contractcustomers`.`contractId` = _contractId and `contractcustomers`.`coreCustomerId` = _cif)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
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
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0
		and FIND_IN_SET(`customeraction`.`Action_id`,_approvalActionList) > 0;      

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `get_reports_proc`;
DELIMITER $$
CREATE PROCEDURE `get_reports_proc`(
IN p_userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN p_roleId varchar(250) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

select * from report rp where rp.createdby=p_userId
UNION
SELECT rp.* from report rp,sharedreport sr
where sr.reportId=rp.id
and sr.userid=p_userId
union
SELECT rp.* from report rp,sharedreport sr
where sr.reportId = rp.id
and FIND_IN_SET( sr.roleId,p_roleId) order by createdts desc;

END$$
DELIMITER ;

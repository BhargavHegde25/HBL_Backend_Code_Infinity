
CREATE TABLE `approvalrequests`(
	`requestId` varchar(50) , 	
	`recordId` LONGTEXT,		
	`module` varchar(50), 		
	`feature` varchar(50), 		
	`expAPIOperationName` varchar(250),											
	`expAPINickName` VARCHAR(100),		
	`permissionId` VARCHAR(50) DEFAULT NULL,
	`permissionName` VARCHAR(50) DEFAULT NULL,
	`createdby` varchar(50),
	`createdts` timestamp DEFAULT CURRENT_TIMESTAMP,
	`status` varchar(50),
	`reason` varchar(255),
	`checkedBy` varchar(50),
	`checkedts` timestamp NULL ON UPDATE CURRENT_TIMESTAMP,
	PRIMARY KEY(requestId)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permissionapprovals` (
  `id` INT NOT NULL AUTO_INCREMENT,	
  `expAPIOperationName` VARCHAR(250),
  `expAPINickName` VARCHAR(100),
  `permissionId` VARCHAR(50) DEFAULT NULL,
  `permissionName` VARCHAR(50) DEFAULT NULL,
  `approvalPermissionId` VARCHAR(50) NULL,
  `approvalPermissionName` VARCHAR(50) NULL,
  `isApprovalRequired` TINYINT(1) NOT NULL,
  `createdby` varchar(50),				
  `createdts` timestamp DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `permissionapprovalconfig` (
`id` INT NOT NULL AUTO_INCREMENT,
`expAPIOperationName` VARCHAR(250),
`expAPINickName` VARCHAR(100),
`permissionId` VARCHAR(50) DEFAULT NULL, 
`permissionName` VARCHAR(50) DEFAULT NULL,
`approvalTableName` VARCHAR(200) NOT NULL,
`originalTableName` VARCHAR(200) NOT NULL,
`keyColumnNames` VARCHAR(200) NOT NULL,
`updateSequence` INT,
`cleanup` TINYINT(1),
`update` TINYINT(1),
`sqlquery` LONGTEXT,
`createdby` varchar(50),
`createdts` timestamp DEFAULT CURRENT_TIMESTAMP,
PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `role_approval` (
  `id` varchar(50) NOT NULL,
  `Type_id` varchar(50) NOT NULL,
  `Status_id` varchar(50) NOT NULL,
  `Parent_id` varchar(50) DEFAULT NULL,
  `Name` varchar(50) NOT NULL,
  `Description` varchar(300) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
  `aprRequestId` varchar(50) NOT NULL,
  `crudAction` varchar(5) NOT NULL,
  PRIMARY KEY (`id`,`aprRequestId`),
  KEY `IXFK_Role_Role_Approval` (`Parent_id`),
  KEY `IXFK_Role_RoleType` (`Type_id`),
  KEY `IXFK_Role_Status` (`Status_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `rolepermission_approval` (
  `Role_id` varchar(50) NOT NULL,
  `Permission_id` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
  `aprRequestId` varchar(50) NOT NULL,
  `crudAction` varchar(5) NOT NULL,
  PRIMARY KEY (`Role_id`,`Permission_id`,`aprRequestId`),
  KEY `IXFK_RolePermission_Permission` (`Permission_id`),
  KEY `IXFK_RolePermission_Role` (`Role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `userrole_approval` (
  `User_id` varchar(50) NOT NULL,
  `Role_id` varchar(50) NOT NULL,
  `hasSuperAdminPrivilages` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
  `aprRequestId` varchar(50) NOT NULL,
  `crudAction` varchar(5) NOT NULL,
  PRIMARY KEY (`User_id`,`Role_id`,`aprRequestId`),
  KEY `IXFK_UserRole_Role` (`Role_id`),
  KEY `IXFK_UserRole_SystemUser` (`User_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



CREATE TABLE `userroleservicedefinition_approval` (
  `UserRole_id` varchar(50) NOT NULL,
  `servicedefinitionId` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
  `aprRequestId` varchar(50) NOT NULL,
  `crudAction` varchar(5) NOT NULL,
  PRIMARY KEY (`UserRole_id`,`servicedefinitionId`,`aprRequestId`),
  KEY `userroleservicedefinition_servicedefinition_id_idx` (`servicedefinitionId`),
  KEY `userroleservicedefinition_userrole_id_idx` (`UserRole_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `rolecompositeaction_approval` (
  `Role_id` varchar(50) NOT NULL,
  `CompositeAction_id` varchar(50) NOT NULL,
  `isEnabled` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
  `aprRequestId` varchar(50) NOT NULL,
  `crudAction` varchar(5) NOT NULL,
  PRIMARY KEY (`Role_id`,`CompositeAction_id`,`aprRequestId`),
  KEY `FK_RoleCompositeAction_Approval_CompositeAction_idx` (`CompositeAction_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



ALTER TABLE `accesspolicy` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `accounttype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `actiondisplaynamedescription` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `actionlevel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `actionlimit` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `app` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `application` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `attributeoption` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `businessconfiguration` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `city` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `communicationtemplate` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `compositeaction` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `configurationbundles` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `configurationmasters` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `configurations` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `country` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customer` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customeralertcategorychannel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customeralertswitch` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customerrequest` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customerservice` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customerviewalertconfiguration` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `dbxcustomeralertentitlement` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `eligibilitycriteria` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `eventsubtype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `eventtype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `facility` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `faqs` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `feature` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `featureaction` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `featureactionroletype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `featuredisplaynamedescription` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `frequencytype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `groupservicedefinition` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `internalusermanager` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `internalusertype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `limitgroupdisplaynamedescription` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `location` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `locationfacility` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `locationfile` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `locationservice` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `locationtype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `logview` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `media` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `membergrouptype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `messageattachment` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `messagetemplate` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `mfa` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `mfaconfigurations` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `mfakey` ADD COLUMN`companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `mfatype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `mfavariablereference` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `outagemessage` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `outagemessageapp` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `permission` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `policycontent` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `privacypolicy` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `region` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `requestcategory` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `requestmessage` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `role` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `rolecompositeaction` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `rolecompositepermission` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `rolepermission` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `roletype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `service` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `servicecommunication` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `membergroup` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `groupactionlimit` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `servicedefinition` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `servicedefinitionactionlimit` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `systemuser` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `termandcondition` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `termandconditionapp` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `useraddress` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `userlob` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `termandconditiontext` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `usercompositeaction` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `usernamerules` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `userpermission` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `userrole` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `userroleservicedefinition` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `usertype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `limitgroup` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `dependentactions` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';



DROP VIEW IF EXISTS `permissions_view`;
CREATE VIEW `permissions_view` AS
    SELECT 
        `permission`.`id` AS `Permission_id`,
        `permission`.`Type_id` AS `PermissionType_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Desc`,
        `permission`.`Status_id` AS `Status_id`,
        `permission`.`companyLegalUnit` AS `companyLegalUnit`,
        `status`.`Description` AS `Status_Desc`,
        (SELECT 
                COUNT(`rolepermission`.`Role_id`)
            FROM
                `rolepermission`
            WHERE
                (`rolepermission`.`Permission_id` = `permission`.`id`)) AS `Role_Count`,
        ((SELECT 
                COUNT(`userpermission`.`Permission_id`)
            FROM
                `userpermission`
            WHERE
                (`userpermission`.`Permission_id` = `permission`.`id`)) + (SELECT 
                COUNT(`userrole`.`User_id`)
            FROM
                `userrole`
            WHERE
                `userrole`.`Role_id` IN (SELECT 
                        `rolepermission`.`Role_id`
                    FROM
                        `rolepermission`
                    WHERE
                        (`rolepermission`.`Permission_id` = `permission`.`id`)))) AS `Users_Count`,
        (CASE `permission`.`Status_id`
            WHEN 'SID_ACTIVE' THEN 'Active'
            ELSE 'Inactive'
        END) AS `Status`
    FROM
        (`permission`
        JOIN `status` ON ((`permission`.`Status_id` = `status`.`id`)));
		
--------------------------------------------------------------------
		
DROP VIEW IF EXISTS `rolepermission_view`;
CREATE VIEW `rolepermission_view` AS
     SELECT 
        `role`.`Name` AS `Role_Name`,
        `role`.`Description` AS `Role_Description`,
        `role`.`Status_id` AS `Role_Status_id`,
        `rolepermission`.`Role_id` AS `Role_id`,
        `permission`.`companyLegalUnit` AS `companyLegalUnit`,
        `permission`.`id` AS `Permission_id`,
        `permission`.`Type_id` AS `Permission_Type_id`,
        `permission`.`Status_id` AS `Permission_Status_id`,
        `permission`.`DataType_id` AS `DataType_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Description`,
        `permission`.`isComposite` AS `Permission_isComposite`,
        `permission`.`PermissionValue` AS `PermissionValue`,
        `permission`.`createdby` AS `Permission_createdby`,
        `permission`.`modifiedby` AS `Permission_modifiedby`,
        `permission`.`createdts` AS `Permission_createdts`,
        `permission`.`lastmodifiedts` AS `Permission_lastmodifiedts`,
        `permission`.`synctimestamp` AS `Permission_synctimestamp`,
        `permission`.`softdeleteflag` AS `Permission_softdeleteflag`
    FROM
        ((`rolepermission`
        JOIN `permission` ON ((`rolepermission`.`Permission_id` = `permission`.`id`)))
        JOIN `role` ON ((`role`.`id` = `rolepermission`.`Role_id`)));
	
--------------------------------------------------------------------	
		
DROP VIEW IF EXISTS `userdirectpermission_view`;
CREATE VIEW `userdirectpermission_view` AS
    SELECT 
        `userpermission`.`User_id` AS `User_id`,
        `userpermission`.`Permission_id` AS `Permission_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Status_id` AS `Permission_Status_id`,
        `permission`.`Description` AS `Permission_Description`,
        `permission`.`isComposite` AS `Permission_isComposite`,
        `systemuser`.`Status_id` AS `User_Status_id`,
        `systemuser`.`Username` AS `UserName`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
		`systemuser`.`companyLegalUnit` AS `companyLegalUnit`,
        `systemuser`.`createdby` AS `createdby`,
        `systemuser`.`modifiedby` AS `updatedby`,
        `systemuser`.`createdts` AS `createdts`,
        `systemuser`.`lastmodifiedts` AS `updatedts`,
        `systemuser`.`softdeleteflag` AS `softdeleteflag`
    FROM
        ((`userpermission`
        JOIN `systemuser` ON ((`userpermission`.`User_id` = `systemuser`.`id`)))
        JOIN `permission` ON ((`userpermission`.`Permission_id` = `permission`.`id`)));
		
--------------------------------------------------------------------
		
DROP VIEW IF EXISTS `internalusers_view`;
CREATE VIEW `internalusers_view` AS
   SELECT 
        `systemuser`.`id` AS `User_id`,
        `systemuser`.`Status_id` AS `Status_id`,
        `status`.`Description` AS `Status_Desc`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`companyLegalUnit` AS `companyLegalUnit`,
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
		
 --------------------------------------------------------------------

DROP VIEW IF EXISTS `branch_view`;
CREATE VIEW `branch_view` AS
     SELECT 
        `loc`.`id` AS `Branch_id`,
        `loc`.`companyLegalUnit` AS `companyLegalUnit`,
        IFNULL(`loc`.`Type_id`, '') AS `Branch_Typeid`,
        IFNULL(`loc`.`Name`, '') AS `Branch_Name`,
        IFNULL(`loc`.`DisplayName`, '') AS `Branch_DisplayName`,
        IFNULL(`loc`.`Description`, '') AS `Branch_Description`,
        IFNULL(`loc`.`Code`, '') AS `Branch_Code`,
        IFNULL(`loc`.`PhoneNumber`, '') AS `Branch_PhoneNumber`,
        IFNULL(`loc`.`EmailId`, '') AS `Branch_EmailId`,
        IFNULL(`loc`.`Status_id`, '') AS `Branch_Status_id`,
        IFNULL(`loc`.`IsMainBranch`, '') AS `Branch_IsMainBranch`,
        IFNULL(`loc`.`WorkSchedule_id`, '') AS `Branch_WorkSchedule_id`,
        IFNULL(`loc`.`MainBranchCode`, '') AS `Branch_MainBranchCode`,
        IFNULL(`loc`.`WebSiteUrl`, '') AS `Branch_WebSiteUrl`,
        `address`.`City_id` AS `City_id`,
        `address`.`cityName` AS `City_Name`,
        `loc`.`Address_id` AS `Address_id`,
        CONCAT(`address`.`addressLine1`,
                ', ',
                IFNULL(`address`.`addressLine2`, ''),
                ', ',
                `address`.`cityName`,
                ', ',
                `region`.`Name`,
                ', ',
                `country`.`Name`,
                ', ',
                IFNULL(`address`.`zipCode`, '')) AS `Branch_Complete_Addr`,
        IFNULL(`loc`.`createdby`, '') AS `Branch_createdby`,
        IFNULL(`loc`.`modifiedby`, '') AS `Branch_modifiedby`,
        IFNULL(`loc`.`createdts`, '') AS `Branch_createdts`,
        IFNULL(`loc`.`lastmodifiedts`, '') AS `Branch_lastmodifiedts`,
        IFNULL(`loc`.`synctimestamp`, '') AS `Branch_synctimestamp`
    FROM
        (((`location` `loc`
        JOIN `address` ON (((`loc`.`Address_id` = `address`.`id`)
            AND (`loc`.`Type_id` = 'Branch'))))
        JOIN `region` ON ((`region`.`id` = `address`.`Region_id`)))
        JOIN `country` ON ((`region`.`Country_id` = `country`.`id`)));
		
	--------------------------------------------------------------------	
		

DROP VIEW IF EXISTS `internaluserdetails_view`;
CREATE VIEW `internaluserdetails_view` AS
    SELECT 
        `systemuser`.`id` AS `id`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`Status_id` AS `Status_id`,
        `systemuser`.`Password` AS `Password`,
        `systemuser`.`Code` AS `Code`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`FailedCount` AS `FailedCount`,
        `systemuser`.`companyLegalUnit` AS `companyLegalUnit`,
        `systemuser`.`LastPasswordChangedts` AS `LastPasswordChangedts`,
        `systemuser`.`ResetpasswordLink` AS `ResetpasswordLink`,
        `systemuser`.`ResetPasswordExpdts` AS `ResetPasswordExpdts`,
        `systemuser`.`lastLogints` AS `lastLogints`,
        `systemuser`.`createdby` AS `createdby`,
        `systemuser`.`createdts` AS `createdts`,
        `systemuser`.`modifiedby` AS `modifiedby`,
        `systemuser`.`lastmodifiedts` AS `lastmodifiedts`,
        `systemuser`.`synctimestamp` AS `synctimestamp`,
        `systemuser`.`softdeleteflag` AS `softdeleteflag`,
        `userrole`.`Role_id` AS `Role_id`,
        `userrole`.`hasSuperAdminPrivilages` AS `hasSuperAdminPrivilages`,
        `role`.`Name` AS `Role_Name`,
        `role`.`Status_id` AS `Role_Status_id`
    FROM
        ((`systemuser`
        LEFT JOIN `userrole` ON ((`userrole`.`User_id` = `systemuser`.`id`)))
        LEFT JOIN `role` ON ((`userrole`.`Role_id` = `role`.`id`)));
		
	--------------------------------------------------------------------
	
DROP VIEW IF EXISTS `systemuser_view`;
CREATE VIEW `systemuser_view` AS
    SELECT 
        `systemuser`.`id` AS `UserID`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`Status_id` AS `Status_id`,
        `systemuser`.`companyLegalUnit` AS `companyLegalUnit`,
        `systemuser`.`modifiedby` AS `UpdatedBy`,
        `systemuser`.`lastmodifiedts` AS `LastModifiedTimeStamp`
    FROM
        `systemuser`;
		
	--------------------------------------------------------------------	
		
DROP VIEW IF EXISTS `roles_view`;
CREATE VIEW `roles_view` AS
     SELECT 
        `role`.`id` AS `role_id`,
        `role`.`Type_id` AS `roleType_id`,
        `role`.`Name` AS `role_Name`,
        `role`.`Description` AS `role_Desc`,
        `role`.`Status_id` AS `Status_id`,
        `role`.`companyLegalUnit` AS `companyLegalUnit`,
        `status`.`Description` AS `Status_Desc`,
        (SELECT 
                COUNT(`rolepermission`.`Role_id`)
            FROM
                `rolepermission`
            WHERE
                (`rolepermission`.`Role_id` = `role`.`id`)) AS `permission_Count`,
        (SELECT 
                COUNT(`userrole`.`User_id`)
            FROM
                `userrole`
            WHERE
                `userrole`.`Role_id` IN (SELECT 
                        `rolepermission`.`Role_id`
                    FROM
                        `rolepermission`
                    WHERE
                        (`rolepermission`.`Role_id` = `role`.`id`))) AS `Users_Count`,
        (CASE `role`.`Status_id`
            WHEN 'SID_ACTIVE' THEN 'Active'
            ELSE 'Inactive'
        END) AS `Status`
    FROM
        (`role`
        JOIN `status` ON ((`role`.`Status_id` = `status`.`id`)));
		
	--------------------------------------------------------------------	
		
DROP VIEW IF EXISTS `internal_role_to_serviceDefinition_mapping_view`;
CREATE VIEW `internal_role_to_serviceDefinition_mapping_view` AS
     SELECT 
        `servicedefinition`.`id` AS `ServiceDefinition_id`,
        `servicedefinition`.`name` AS `ServiceDefinition_Name`,
        `servicedefinition`.`description` AS `ServiceDefinition_Description`,
        `servicedefinition`.`serviceType` AS `ServiceDefinition_Type_id`,
        `servicedefinition`.`status` AS `ServiceDefinition_Status_id`,
        `role`.`id` AS `InternalRole_id`,
        `role`.`Type_id` AS `InternalRole_Type_id`,
        `role`.`Status_id` AS `InternalRole_Status_id`,
        `role`.`Name` AS `InternalRole_Name`,
        `role`.`Description` AS `InternalRole_Description`,
        `role`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        ((`userroleservicedefinition`
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `userroleservicedefinition`.`servicedefinitionId`)))
        LEFT JOIN `role` ON ((`role`.`id` = `userroleservicedefinition`.`UserRole_id`)));
		
--------------------------------------------------------------------
		
DROP VIEW IF EXISTS `roleuser_view`;
CREATE VIEW `roleuser_view` AS
     SELECT 
        `userrole`.`User_id` AS `User_id`,
        `userrole`.`Role_id` AS `Role_id`,
        `systemuser`.`Status_id` AS `Status_id`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`companyLegalUnit` AS `companyLegalUnit`,
        `systemuser`.`modifiedby` AS `UpdatedBy`,
        `systemuser`.`lastmodifiedts` AS `LastModifiedTimeStamp`
    FROM
        (`userrole`
        JOIN `systemuser` ON ((`userrole`.`User_id` = `systemuser`.`id`)));

--------------------------------------------------------------------

DROP VIEW IF EXISTS `servicedefinition_view`;

CREATE  VIEW `servicedefinition_view` AS
SELECT
    `servicedefinition`.`id` AS `id`,
    `servicedefinition`.`name` AS `name`,
    `servicedefinition`.`description` AS `description`,
    `servicedefinition`.`serviceType` AS `serviceType`,
    `servicedefinition`.`status` AS `status`,
    `servicedefinition`.`companyLegalUnit` AS `companyLegalUnit`,
    (
   SELECT
        COUNT(`groupservicedefinition`.`Group_id`)
    FROM
        `groupservicedefinition`
     WHERE
        (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)) AS `numberOfRoles`,
    (
    SELECT
        COUNT(`membergroup`.`id`)
    FROM
        `membergroup`
    WHERE
        ((`membergroup`.`Status_id` LIKE 'SID_ACTIVE')
            AND `membergroup`.`id` IN (
            SELECT
                `groupservicedefinition`.`Group_id`
            FROM
                `groupservicedefinition`
            WHERE
                (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)))) AS `numberOfActiveRoles`,
    (
    SELECT
        `groupservicedefinition`.`Group_id`
    FROM
        `groupservicedefinition`
    WHERE
        ((`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)
            AND (`groupservicedefinition`.`isDefaultGroup` = 1))) AS`defaultRole`,
    (
    SELECT
        COUNT(DISTINCT `servicedefinition_features_actions_view`.`featureId`)
    FROM
        `servicedefinition_features_actions_view`
    WHERE
        ((`servicedefinition`.`id` = `servicedefinition_features_actions_view`.`serviceDefinitionId`)
            AND (`servicedefinition_features_actions_view`.`softdelete` = '0'))) AS `numberOfFeatures`,
    (
    SELECT
        COUNT(`contract`.`id`)
    FROM
        `contract`
    WHERE
        (`contract`.`servicedefinitionId` = `servicedefinition`.`id`)) AS `numberOfContracts`
FROM
    `servicedefinition`;
	
	-------------------------------------------------------------------------

DROP VIEW IF EXISTS `internal_role_to_customer_role_mapping_view`;

CREATE   VIEW `internal_role_to_customer_role_mapping_view` AS
SELECT
    `membergroup`.`id` AS `CustomerRole_id`,
    `membergroup`.`Name` AS `CustomerRole_Name`,
    `membergroup`.`Description` AS `CustomerRole_Description`,
    `membergroup`.`Type_id` AS `CustomerRole_Type_id`,
    `membergroup`.`Status_id` AS `CustomerRole_Status_id`,
	`membergroup`.`companyLegalUnit` AS `companyLegalUnit`,
    `role`.`id` AS `InternalRole_id`,
    `role`.`Type_id` AS `InternalRole_Type_id`,
    `role`.`Status_id` AS `InternalRole_Status_id`,
    `role`.`Name` AS `InternalRole_Name`,
    `role`.`Description` AS `InternalRole_Description`
FROM
    ((`userrolecustomerrole`
LEFT JOIN `membergroup` ON
    ((`membergroup`.`id` = `userrolecustomerrole`.`CustomerRole_id`)))
LEFT JOIN `role` ON
    ((`role`.`id` = `userrolecustomerrole`.`UserRole_id`)));
	
	---------------------------------------------------------------------

DROP VIEW IF EXISTS `configuration_view`;

CREATE  VIEW `configuration_view` AS
SELECT
    `configurations`.`configuration_id` AS `configuration_id`,
    `configurations`.`bundle_id` AS `bundle_id`,
    `configurationbundles`.`bundle_name` AS `bundle_name`,
    `configurations`.`config_type` AS `type`,
    `configurations`.`config_key` AS `key`,
    `configurations`.`description` AS `description`,
    `configurations`.`config_value` AS `value`,
    `configurations`.`target` AS `target`,
    `configurations`.`isPreLoginConfiguration` AS `isPreLoginConfiguration`,
    `configurationbundles`.`app_id` AS `app_id`,
	`configurationbundles`.`companyLegalUnit` AS `companyLegalUnit`
FROM
    (`configurations`
LEFT JOIN `configurationbundles` ON
    ((`configurationbundles`.`bundle_id` = `configurations`.`bundle_id`)));

-----------------------------------------------------------------------------

DROP VIEW IF EXISTS `policy_view`;

CREATE   VIEW `policy_view` AS
SELECT
    `policycontent`.`id` AS `id`,
    `policytype`.`id` AS `Type_id`,
    `policycontent`.`Locale_Code` AS `Locale`,
	`policycontent`.`companyLegalUnit` AS `companyLegalUnit`,
    `policycontent`.`Content` AS `PolicyContent`
FROM
    (`policycontent`
JOIN `policytype` ON
    ((`policycontent`.`Type_id` = `policytype`.`id`)));

------------------------------------------------------------------

DROP VIEW IF EXISTS `locationservices_view`;

CREATE   VIEW `locationservices_view` AS
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
	`facility`.`companyLegalUnit` AS `companyLegalUnit`,
    `facility`.`description` AS `Facility_description`,
    (
    SELECT
        group_concat(`currency`.`code` order by `currency`.`code` asc separator ',')
    FROM
        (`currency`
    JOIN `locationcurrency`)
    WHERE
        ((`currency`.`code` = `locationcurrency`.`currency_code`)
            AND (`locationcurrency`.`Location_id` = `location`.`id`))) AS `currencies`,
    (
    SELECT
        group_concat(`customersegment`.`type` order by `customersegment`.`id` asc separator ',')
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
    CONCAT(`address`.`addressLine1`, ', ', ifnull(`address`.`cityName`, ''), ', ',(SELECT `region`.`Name` from `region` WHERE (`region`.`id` = `address`.`Region_id`)), ', ',(SELECT `country`.`Name` from `country` WHERE `country`.`id` IN (SELECT `region`.`Country_id` from `region` WHERE (`region`.`id` = `address`.`Region_id`))), ', ', `address`.`zipCode`) AS `ADDRESS`
FROM
    ((((((`location`
LEFT JOIN `locationfacility` ON
    ((`location`.`id` = `locationfacility`.`Location_id`)))
LEFT JOIN `facility` ON
    ((`locationfacility`.`facility_id` = `facility`.`id`)))
LEFT JOIN `dayschedule` `weekday` ON
    (((`location`.`WorkSchedule_id` = `weekday`.`WorkSchedule_id`)
        AND (`weekday`.`WeekDayName` = 'MONDAY'))))
LEFT JOIN `dayschedule` `sunday` ON
    (((`location`.`WorkSchedule_id` = `sunday`.`WorkSchedule_id`)
        AND (`sunday`.`WeekDayName` = 'SUNDAY'))))
LEFT JOIN `dayschedule` `saturday` ON
    (((`location`.`WorkSchedule_id` = `saturday`.`WorkSchedule_id`)
        AND (`saturday`.`WeekDayName` = 'SATURDAY'))))
JOIN `address` ON
    ((`address`.`id` = `location`.`Address_id`)));

--------------------------------------------------------------------------

DROP VIEW IF EXISTS `locationfacility_view`;

CREATE   VIEW `locationfacility_view` AS
SELECT
    `location`.`id` AS `Location_id`,
    `facility`.`id` AS `Facility_id`,
    `facility`.`code` AS `Facility_code`,
    `facility`.`name` AS `Facility_Name`,
	`facility`.`companyLegalUnit` AS `companyLegalUnit`,
    `facility`.`description` AS `Facility_description`,
    `facility`.`facilitytype` AS `Facility_facilitytype`
FROM
    ((`location`
LEFT JOIN `locationfacility` ON
    ((`location`.`id` = `locationfacility`.`Location_id`)))
JOIN `facility` ON
    ((`locationfacility`.`facility_id` = `facility`.`id`)));

--------------------------------------------------------------------

DROP VIEW IF EXISTS `location_view`;

CREATE   VIEW `location_view` AS
SELECT
    DISTINCT `location`.`id` AS `id`,
    `location`.`Name` AS `Name`,
    `location`.`Code` AS `Code`,
	`location`.`companyLegalUnit` AS `companyLegalUnit`,
    `location`.`Description` AS `Description`,
    `location`.`PhoneNumber` AS `PhoneNumber`,
    `location`.`Type_id` AS `Type_id`,
    (case
        `location`.`Status_id` WHEN 'SID_ACTIVE' THEN 'Active'
        ELSE 'Inactive'
    END) AS `Status_id`
FROM
    `location`;

	--------------------------------------------------------------------
	
	DROP VIEW IF EXISTS `groups_view`;

CREATE  view `groups_view` AS
SELECT
    `membergroup`.`id` AS `Group_id`,
    `membergroup`.`Type_id` AS `Type_id`,
    `membergrouptype`.`description` AS `Type_Name`,
    `membergroup`.`Description` AS `Group_Desc`,
    `membergroup`.`Status_id` AS `Status_id`,
    `membergroup`.`Name` AS `Group_Name`,
	`membergroup`.`companyLegalUnit` AS `companyLegalUnit`,
    `membergroup`.`isEAgreementActive` AS `isEAgreementActive`,
    `membergroup`.`isApplicabletoAllServices` AS `isApplicabletoAllServices`,
    (
    SELECT
        COUNT(`groupentitlement`.`Group_id`)
    FROM
        `groupentitlement`
    WHERE
        (`groupentitlement`.`Group_id` = `membergroup`.`id`)) AS `Entitlements_Count`,
    (
    SELECT
        COUNT(distinct `customergroup`.`Customer_id`)
    FROM
        `customergroup`
    WHERE
        (`customergroup`.`Group_id` = `membergroup`.`id`)) AS `Customers_Count`,
    (CASE
        `membergroup`.`Status_id` WHEN 'SID_ACTIVE' THEN 'Active'
        ELSE 'Inactive'
    END) AS `Status`
FROM
    (`membergroup`
JOIN `membergrouptype` ON
    ((`membergroup`.`Type_id` = `membergrouptype`.`id`)));
	
	--------------------------------------------------------------------
	
DROP VIEW IF EXISTS `customergroups_view`;
CREATE VIEW `customergroups_view` as
Select  count(distinct(`customergroup`.`Customer_id`)) as `customerCount`, 
`groupservicedefinition`.`serviceDefinitionId` as `servicedefinitionId`, `groupservicedefinition`.`companyLegalUnit` AS `companyLegalUnit`,
`groupservicedefinition`.`Group_id` as `groupId` 
from `groupservicedefinition`
join `contract`  on `groupservicedefinition`.`serviceDefinitionId` =`contract`.`servicedefinitionId` 
join  `customergroup`  on `groupservicedefinition`.`Group_id`=`customergroup`.`Group_id` and  `contract`.`id`=`customergroup`.`contractId`
group by `groupservicedefinition`.`Group_id`, `groupservicedefinition`.`serviceDefinitionId`, `groupservicedefinition`.`companyLegalUnit`;

	
	--------------------------------------------------------------------

	
	
DROP VIEW IF EXISTS `customerservice_communication_view`;

CREATE VIEW `customerservice_communication_view` AS
SELECT
    `customerservice`.`id` AS `Service_id`,
    `customerservice`.`Name` AS `Service_Name`,
    `customerservice`.`Status_id` AS `Service_Status_id`,
    `customerservice`.`Description` AS `Service_Description`,
    `customerservice`.`softdeleteflag` AS `Service_SoftDeleteFlag`,
	`customerservice`.`companyLegalUnit` AS `companyLegalUnit`,
    `servicecommunication`.`id` AS `Servicecommunication_id`,
    `servicecommunication`.`Type_id` AS `Servicecommunication_Typeid`,
    `servicecommunication`.`Value` AS `Servicecommunication_Value`,
    `servicecommunication`.`Extension` AS `Servicecommunication_Extension`,
    `servicecommunication`.`Description` AS `Servicecommunication_Description`,
    `servicecommunication`.`Status_id` AS `Servicecommunication_Status_id`,
    `servicecommunication`.`Priority` AS `Servicecommunication_Priority`,
    `servicecommunication`.`createdby` AS `Servicecommunication_createdby`,
    `servicecommunication`.`modifiedby` AS `Servicecommunication_modifiedby`,
    `servicecommunication`.`createdts` AS `Servicecommunication_createdts`,
    `servicecommunication`.`Lastmodifiedts` AS `Servicecommunication_Lastmodifiedts`,
    `servicecommunication`.`synctimestamp` AS `Servicecommunication_synctimestamp`,
    `servicecommunication`.`softdeleteflag` AS `Servicecommunication_SoftDeleteFlag`
FROM
    (`servicecommunication`
JOIN `customerservice` ON
    ((`servicecommunication`.`Service_id` = `customerservice`.`id`)));
	
---------------------------------------------------------------------------------------		
	
DROP VIEW IF EXISTS `faqcategory_view`;

CREATE VIEW `faqcategory_view` AS
SELECT
    `faqs`.`id` AS `id`,
    `faqs`.`Status_id` AS `Status_id`,
    `faqs`.`QuestionCode` AS `QuestionCode`,
    `faqs`.`Question` AS `Question`,
    `faqs`.`Channel_id` AS `Channel_id`,
    `faqs`.`Answer` AS `Answer`,
    `faqs`.`FaqCategory_Id` AS `CategoryId`,
	`faqs`.`companyLegalUnit` AS `companyLegalUnit`,
    `faqcategory`.`Name` AS `CategoryName`
FROM
    (`faqs`
JOIN `faqcategory` ON
    ((`faqcategory`.`id` = `faqs`.`FaqCategory_Id`)));
	
---------------------------------------------------------------------------------------	
	
DROP VIEW IF EXISTS `actiondependency_view`;

CREATE VIEW `actiondependency_view` AS
SELECT
    `dependentactions`.`dependentactionId` AS `actionName`,
    `dependentactions`.`actionId` AS `dependencyaction`,
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
		
---------------------------------------------------------------------------------------	
	
DROP VIEW IF EXISTS `get_all_features_view`;

CREATE VIEW `get_all_features_view` AS
SELECT
    `feature`.`id` AS `id`,
    `feature`.`name` AS `name`,
    `feature`.`Description` AS `Description`,
    `feature`.`Type_id` AS `Type_id`,
    `feature`.`Service_Fee` AS `Service_Fee`,
    `feature`.`Status_id` AS `Status_id`,
	`feature`.`companyLegalUnit` AS `companyLegalUnit`,
    `featureroletype`.`RoleType_id` AS `roleTypeId`,
    `featuredisplaynamedescription`.`Locale_id` AS `languageId`,
    `featuredisplaynamedescription`.`displayName` AS `displayName`,
    `featuredisplaynamedescription`.`displayDescription` AS `displayDescription`,
    `membergrouptype`.`Description` AS `roleTypeName`,
    (
    SELECT
        COUNT(distinct `featureaction`.`id`)
    FROM
        `featureaction`
    WHERE
        ((`feature`.`id` = `featureaction`.`Feature_id`)
            AND (`featureaction`.`Type_id` = 'MONETARY'))) AS `monetaryactions`,
    (
    SELECT
        COUNT(distinct `featureaction`.`id`)
    FROM
        `featureaction`
    WHERE
        ((`feature`.`id` = `featureaction`.`Feature_id`)
            AND (`featureaction`.`Type_id` = 'NON_MONETARY'))) AS `nonMonetaryactions`
FROM
    (((`feature`
left JOIN `featureroletype` ON
    ((`featureroletype`.`Feature_id` = `feature`.`id`)))
left JOIN `featuredisplaynamedescription` ON
    ((`featuredisplaynamedescription`.`Feature_id` = `feature`.`id`)))
left JOIN `membergrouptype` ON
    ((`featureroletype`.`RoleType_id` = `membergrouptype`.`id`)));
	
---------------------------------------------------------------------------------------		
	
DROP VIEW IF EXISTS `limitgroups_view`;

CREATE VIEW `limitgroups_view` AS
SELECT
    `limitgroup`.`id` AS `id`,
    `limitgroup`.`name` AS `name`,
    `limitgroup`.`Description` AS `Description`,
	`limitgroup`.`companyLegalUnit` AS `companyLegalUnit`,
    `limitgroupdisplaynamedescription`.`localeId` AS `localeId`,
    `limitgroupdisplaynamedescription`.`displayName` AS `displayName`,
    `limitgroupdisplaynamedescription`.`displayDescription` AS `displayDescription`
FROM
    (`limitgroup`
left JOIN `limitgroupdisplaynamedescription` ON
    ((`limitgroupdisplaynamedescription`.`limitGroupId` = `limitgroup`.`id`)));
	
---------------------------------------------------------------------------------------		
	
DROP VIEW IF EXISTS `internaluserskc_view`;

CREATE VIEW `internaluserskc_view` AS
SELECT
    `internalusertype`.`userId` AS `User_id`,
	`internalusertype`.`companyLegalUnit` AS `companyLegalUnit`,
    `internalusertype`.`userType` AS `UserType`,
    (
    SELECT
        `internalusermanager`.`manager`
    FROM
        `internalusermanager`
    WHERE
        (`internalusertype`.`userId` = `internalusermanager`.`userId`)
    limit 1) AS `ReportingManager`,
    (
    SELECT
        group_concat(`userlob`.`lobId` separator ',')
    FROM
        `userlob`
    WHERE
        (`internalusertype`.`userId` = `userlob`.`userId`)) AS `lobId`,
    (
    SELECT
        group_concat(`lobtext`.`displayName` separator ',')
    FROM
        `lobtext`
    WHERE
        `lobtext`.`lobId` in (
        SELECT
            `userlob`.`lobId`
        FROM
            `userlob`
        WHERE
            (`internalusertype`.`userId` = `userlob`.`userId`))) AS `lobName`,
    (
    SELECT
        `usertype`.`name`
    FROM
        `usertype`
    WHERE
        `usertype`.`id` in (
        SELECT
            `internalusertype`.`userType`
        FROM
            `internalusertype`
        WHERE
            (`internalusertype`.`userId` = `internalusertype`.`userId`))
    limit 1) AS `userTypeName`,
    (
    SELECT
        ifnull(`workaddress`.`id`, '')) AS `Work_AddressID`,
    (
    SELECT
        ifnull(`workaddress`.`addressLine1`, '')) AS `Work_AddressLine1`,
    (
    SELECT
        ifnull(`workaddress`.`addressLine2`, '')) AS `Work_AddressLine2`,
    (
    SELECT
        ifnull((SELECT `city`.`Name` FROM `city` WHERE (`city`.`id` = `workaddress`.`City_id`)), '')) AS `Work_CityName`,
    (
    SELECT
        ifnull(`workaddress`.`City_id`, '')) AS `Work_CityID`,
    (
    SELECT
        ifnull((SELECT `region`.`Name` FROM `region` WHERE (`region`.`id` = `workaddress`.`Region_id`)), '')) AS `Work_StateName`,
    (
    SELECT
        ifnull(`workaddress`.`Region_id`, '')) AS `Work_StateID`,
    (
    SELECT
        ifnull((SELECT `country`.`Name` FROM `country` WHERE `country`.`id` in (SELECT `city`.`Country_id` FROM `city` WHERE (`city`.`id` = `workaddress`.`City_id`))), '')) AS `Work_CountryName`,
    (
    SELECT
        ifnull((SELECT `country`.`id` FROM `country` WHERE `country`.`id` in (SELECT `city`.`Country_id` FROM `city` WHERE (`city`.`id` = `workaddress`.`City_id`))), '')) AS `Work_CountryID`,
    (
    SELECT
        ifnull(`workaddress`.`zipCode`, '')) AS `Work_Zipcode`,
    (
    SELECT
        `location`.`Name`
    FROM
        `location`
    WHERE
        (`workaddress`.`id` = `location`.`Address_id`)) AS `branchName`,
    (
    SELECT
        concat(`workaddress`.`addressLine1`, ', ', ifnull(`workaddress`.`addressLine2`, ''), ', ',(SELECT `city`.`Name` FROM `city` WHERE (`city`.`id` = `workaddress`.`City_id`)), ', ',(SELECT `region`.`Name` FROM `region` WHERE (`region`.`id` = `workaddress`.`Region_id`)), ', ',(SELECT `country`.`Name` FROM `country` WHERE `country`.`id` in (SELECT `city`.`Country_id` FROM `city` WHERE (`city`.`id` = `workaddress`.`City_id`))), ', ', `workaddress`.`zipCode`)) AS `Work_Addr`
FROM
    ((`internalusertype`
JOIN `internalusermanager` ON
    ((`internalusertype`.`userId` = `internalusermanager`.`userId`)))
left JOIN `address` `workaddress` ON
    (`workaddress`.`id` in (
    SELECT
        `useraddress`.`Address_id`
    FROM
        `useraddress`
    WHERE
        ((`internalusertype`.`userId` = `useraddress`.`User_id`)
            AND (`useraddress`.`Type_id` = 'ADR_TYPE_WORK')))));
			
---------------------------------------------------------------------------------------				
			

	
DROP VIEW IF EXISTS `customer_request_csr_count_view`;

CREATE VIEW `customer_request_csr_count_view` AS
SELECT
    `customerrequest`.`Status_id` AS `customerrequest_Status_id`,
	`customerrequest`.`companyLegalUnit` AS `companyLegalUnit`,
    (
    SELECT
        `status`.`Description`
    FROM
        `status`
    WHERE
        (`status`.`id` = `customerrequest`.`Status_id`)
    limit 1) AS `status_Description`,
    `customerrequest`.`AssignedTo` AS `customerrequest_AssignedTo`,
    COUNT(`customerrequest`.`id`) AS `request_count`
FROM
    `customerrequest`
GROUP BY
    `customerrequest`.`AssignedTo`,
    `customerrequest`.`Status_id`,
	`customerrequest`.`companyLegalUnit`;
	
---------------------------------------------------------------------------------------		
	


DROP VIEW IF EXISTS `customer_request_detailed_view`;

CREATE VIEW `customer_request_detailed_view` AS
SELECT
    `customerrequest`.`id` AS `customerrequest_id`,
    `customerrequest`.`RequestCategory_id` AS `customerrequest_RequestCategory_id`,
    `customerrequest`.`lAStupdatedbycustomer` AS `customerrequest_lastupdatedbycustomer`,
    `requestcategory`.`Name` AS `requestcategory_Name`,
    `customerrequest`.`Customer_id` AS `customerrequest_Customer_id`,
    `customer`.`FirstName` AS `customer_FirstName`,
    `customer`.`MiddleName` AS `customer_MiddleName`,
    concat(`customer`.`FirstName`, ' ', `customer`.`LastName`) AS `customer_Fullname`,
    concat(`systemuser`.`FirstName`, ' ', `systemuser`.`LAStName`) AS `customerrequest_AssignedTo_Name`,
    `customer`.`LastName` AS `customer_LastName`,
    `customer`.`UserName` AS `customer_Username`,
    `customer`.`Salutation` AS `customer_Salutation`,
    `customer`.`Gender` AS `customer_Gender`,
    `customer`.`DateOfBirth` AS `customer_DateOfBirth`,
    `customer`.`Status_id` AS `customer_Status_id`,
    NULL AS `customer_Ssn`,
    `customer`.`MaritalStatus_id` AS `customer_MaritalStatus_id`,
    `customer`.`SpouseName` AS `customer_SpouseName`,
    `customer`.`EmployementStatus_id` AS `customer_EmployementStatus_id`,
    `customer`.`IsEnrolledForOlb` AS `customer_IsEnrolledForOlb`,
    `customer`.`IsStaffMember` AS `customer_IsStaffMember`,
    `customer`.`Location_id` AS `customer_Location_id`,
    `customer`.`PreferredContactMethod` AS `customer_PreferredContactMethod`,
    `customer`.`PreferredContactTime` AS `customer_PreferredContactTime`,
	`customer`.`companyLegalUnit` AS `companyLegalUnit`,
    `customerrequest`.`Priority` AS `customerrequest_Priority`,
    `customerrequest`.`Status_id` AS `customerrequest_Status_id`,
    `customerrequest`.`AssignedTo` AS `customerrequest_AssignedTo`,
    `customerrequest`.`RequestSubject` AS `customerrequest_RequestSubject`,
    `customerrequest`.`Accountid` AS `customerrequest_Accountid`,
    `customerrequest`.`createdby` AS `customerrequest_createdby`,
    `customerrequest`.`modifiedby` AS `customerrequest_modifiedby`,
    `customerrequest`.`createdts` AS `customerrequest_createdts`,
    `customerrequest`.`lastmodifiedts` AS `customerrequest_lAStmodifiedts`,
    `customerrequest`.`synctimestamp` AS `customerrequest_synctimestamp`,
    `customerrequest`.`softdeleteflag` AS `customerrequest_softdeleteflag`,
    `requestmessage`.`id` AS `requestmessage_id`,
    `requestmessage`.`isPriorityMessage` AS `requestmessage_isPriorityMessage`,
    `requestmessage`.`RepliedBy` AS `requestmessage_RepliedBy`,
    `requestmessage`.`RepliedBy_Name` AS `requestmessage_RepliedBy_Name`,
    `requestmessage`.`MessageDescription` AS `requestmessage_MessageDescription`,
    `requestmessage`.`ReplySequence` AS `requestmessage_ReplySequence`,
    `requestmessage`.`IsRead` AS `requestmessage_IsRead`,
    `requestmessage`.`createdby` AS `requestmessage_createdby`,
    `requestmessage`.`modifiedby` AS `requestmessage_modifiedby`,
    `requestmessage`.`createdts` AS `requestmessage_createdts`,
    `requestmessage`.`lastmodifiedts` AS `requestmessage_lAStmodifiedts`,
    `requestmessage`.`synctimestamp` AS `requestmessage_synctimestamp`,
    `requestmessage`.`softdeleteflag` AS `requestmessage_softdeleteflag`,
    `messageattachment`.`id` AS `messageattachment_id`,
    `messageattachment`.`AttachmentType_id` AS `messageattachment_AttachmentType_id`,
    `messageattachment`.`Media_id` AS `messageattachment_Media_id`,
    `messageattachment`.`createdby` AS `messageattachment_createdby`,
    `messageattachment`.`modifiedby` AS `messageattachment_modifiedby`,
    `messageattachment`.`createdts` AS `messageattachment_createdts`,
    `messageattachment`.`lastmodifiedts` AS `messageattachment_lastmodifiedts`,
    `messageattachment`.`softdeleteflag` AS `messageattachment_softdeleteflag`,
    `media`.`id` AS `media_id`,
    `media`.`Name` AS `media_Name`,
    `media`.`Size` AS `media_Size`,
    `media`.`Type` AS `media_Type`,
    `media`.`Description` AS `media_Description`,
    `media`.`Url` AS `media_Url`,
    `media`.`createdby` AS `media_createdby`,
    `media`.`modifiedby` AS `media_modifiedby`,
    `media`.`lastmodifiedts` AS `media_lAStmodifiedts`,
    `media`.`synctimestamp` AS `media_synctimestamp`,
    `media`.`softdeleteflag` AS `media_softdeleteflag`
FROM
    ((((((`customerrequest`
JOIN `customer` ON
    ((`customerrequest`.`Customer_id` = `customer`.`id`)))
JOIN `requestcategory` ON
    ((`customerrequest`.`RequestCategory_id` = `requestcategory`.`id`)))
JOIN `requestmessage` ON
    ((`customerrequest`.`id` = `requestmessage`.`CustomerRequest_id`)))
LEFT JOIN `systemuser` ON
    ((`customerrequest`.`ASsignedTo` = `systemuser`.`id`)))
LEFT JOIN `messageattachment` ON
    ((`requestmessage`.`id` = `messageattachment`.`RequestMessage_id`)))
LEFT JOIN `media` ON
    ((`messageattachment`.`Media_id` = `media`.`id`)));
	
	----------------------------------------------------------------------------------------

DROP VIEW IF EXISTS `customer_request_status_count_view`;

CREATE VIEW `customer_request_status_count_view` AS
SELECT
    COUNT(`cr`.`id`) AS `Count`,
    `s1`.`id` AS `Status_id`
FROM
    (`status` `s1`
JOIN `customerrequest` `cr` ON
    ((`s1`.`id` = `cr`.`Status_id`)))
WHERE
    ((`s1`.`Type_id` = 'STID_CUSTOMERREQUEST')
        AND (`s1`.`id` IN ('SID_CANCELLED', 'SID_DELETED', 'SID_INPROGRESS', 'SID_ONHOLD', 'SID_OPEN', 'SID_RESOLVED')))
GROUP BY
    `s1`.`id`
UNION ALL
SELECT
    COUNT(`acr`.`id`) AS `Count`,
    `s2`.`id` AS `Status_id`
FROM
    (`status` `s2`
JOIN `archivedcustomerrequest` `acr` ON
    ((`s2`.`id` = `acr`.`Status_id`)))
WHERE
    ((`s2`.`Type_id` = 'STID_CUSTOMERREQUEST')
        AND (`s2`.`id` = 'SID_ARCHIVED'))
GROUP BY
    `s2`.`id`;

---------------------------------------------------------------------------------------	

DROP VIEW IF EXISTS `customerbasicinfo_view`;

CREATE VIEW `customerbasicinfo_view` AS
SELECT
    `customer`.`UserName` AS `Username`,
    `customer`.`FirstName` AS `FirstName`,
    `customer`.`MiddleName` AS `MiddleName`,
    `customer`.`LastName` AS `LastName`,
    concat(ifNULL(`customer`.`FirstName`, ''), ' ', ifNULL(`customer`.`MiddleName`, ''), ' ', ifNULL(`customer`.`LastName`, '')) AS `Name`,
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
	`customer`.`companyLegalUnit` AS `companyLegalUnit`,
    `employementstatus`.`Description` AS `EmployementStatus_name`,
    (
    SELECT
        group_concat(`customerflagstatus`.`Status_id`, ' ' separator ',')
    FROM
        `customerflagstatus`
    WHERE
        (`customerflagstatus`.`Customer_id` = `customer`.`id`)) AS `CustomerFlag_ids`,
    (
    SELECT
        group_concat(`status`.`Description`, ' ' separator ',')
    FROM
        `status`
    WHERE
        `status`.`id` IN (
        SELECT
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
    `customer`.`IsAssistConsented` AS `IsASsistConsented`,
    `customer`.`isEagreementSigned` AS `isEagreementSigned`,
    `customer`.`isCombinedUser` AS `isCombinedUser`,
    if((`customer`.`isCombinedUser` = '1'),
    'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
    `customer`.`CustomerType_id`) AS `CustomerType_id`,
    if((`customer`.`isCombinedUser` = '1'),
    'Retail Banking,Business Banking',
    `customertype`.`Name`) AS `CustomerType_Name`,
    if((`customer`.`isCombinedUser` = '1'),
    'Retail and Business Banking User',
    `customertype`.`Description`) AS `CustomerType_Description`,
    (
    SELECT
        `membergroup`.`Name`
    FROM
        `membergroup`
    WHERE
        `membergroup`.`id` IN (
        SELECT
            `customergroup`.`Group_id`
        FROM
            `customergroup`
        WHERE
            (`customer`.`id` = `customergroup`.`Customer_id`))
    limit 1) AS `Customer_Role`,
    (
    SELECT
        `membergroup`.`isEAgreementActive`
    FROM
        `membergroup`
    WHERE
        `membergroup`.`id` IN (
        SELECT
            `customergroup`.`Group_id`
        FROM
            `customergroup`
        WHERE
            (`customer`.`id` = `customergroup`.`Customer_id`))
    limit 1) AS `isEAgreementRequired`,
    `customer`.`Organization_Id` AS `organisation_id`,
    `organisation`.`Name` AS `organisation_name`,
    any_value(`primaryphone`.`Value`) AS `PrimaryPhoneNumber`,
    any_value(`primaryemail`.`Value`) AS `PrimaryEmailAddress`,
    `customer`.`DocumentsSubmitted` AS `DocumentsSubmitted`,
    `customer`.`ApplicantChannel` AS `ApplicantChannel`,
    `customer`.`Product` AS `Product`,
    `customer`.`Reason` AS `Reason`
FROM
    ((((((((`customer`
LEFT JOIN `location` ON
    ((`customer`.`Location_id` = `location`.`id`)))
LEFT JOIN `organisation` ON
    ((`customer`.`Organization_Id` = `organisation`.`id`)))
LEFT JOIN `customertype` ON
    ((`customer`.`CustomerType_id` = `customertype`.`id`)))
LEFT JOIN `status` `customerstatus` ON
    ((`customer`.`Status_id` = `customerstatus`.`id`)))
LEFT JOIN `status` `maritalstatus` ON
    ((`customer`.`MaritalStatus_id` = `maritalstatus`.`id`)))
LEFT JOIN `status` `employementstatus` ON
    ((`customer`.`EmployementStatus_id` = `employementstatus`.`id`)))
LEFT JOIN `customercommunication` `primaryphone` ON
    (((`primaryphone`.`Customer_id` = `customer`.`id`)
        AND (`primaryphone`.`isPrimary` = 1)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))))
LEFT JOIN `customercommunication` `primaryemail` ON
    (((`primaryemail`.`Customer_id` = `customer`.`id`)
        AND (`primaryemail`.`isPrimary` = 1)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL'))))
GROUP BY
    `customer`.`id`;

	--------------------------------------------------------------------------------------


	
DROP VIEW IF EXISTS `feature_actions_view`;

CREATE VIEW `feature_actions_view` AS
SELECT
    `featureaction`.`id` AS `id`,
    `featureaction`.`Feature_id` AS `Feature_id`,
    `featureaction`.`name` AS `action_name`,
    `featureaction`.`description` AS `action_description`,
    `featureaction`.`isAccountLevel` AS `isAccountLevel`,
    `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
    `featureaction`.`isPrimary` AS `isPrimary`,
    `featureaction`.`notes` AS `notes`,
    `featureaction`.`Type_id` AS `action_Type_id`,
    `featureaction`.`DisplaySequence` AS `action_displaysequence`,
    `featureaction`.`dependency` AS `action_dependency`,
    `feature`.`Status_id` AS `feature_status_id`,
    `feature`.`name` AS `feature_name`,
    `feature`.`description` AS `feature_description`,
    `feature`.`Type_id` AS `feature_Type_id`,
    `feature`.`DisplaySequence` AS `feature_displaysequence`,
    `feature`.`isPrimary` AS `feature_isPrimary`,
	`feature`.`companyLegalUnit` AS `companyLegalUnit`,
    `actionlimit`.`LimitType_id` AS `LimitType_id`,
    `actionlimit`.`value` AS `value`
FROM
    ((`featureaction`
LEFT JOIN `feature` ON
    ((`feature`.`id` = `featureaction`.`Feature_id`)))
LEFT JOIN `actionlimit` ON
    ((`actionlimit`.`Action_id` = `featureaction`.`id`)));

 ------------------------------------------------------------------------------------



DROP procedure IF EXISTS `bulkassign_permissions_to_role_approval`;

DELIMITER $$
CREATE PROCEDURE `bulkassign_permissions_to_role_approval`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _permissions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPermissions = LENGTH(_permissions) - LENGTH(REPLACE(_permissions, '|', '')) + 1;
		insertPermissions : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfPermissions + 1 THEN
            LEAVE insertPermissions;
        ELSE
        SET @permissionsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_permissions, '|', index1), '|', -1 );
        SET @permissionsData = CONCAT("'",@permissionsData,"'");
		SET @query = CONCAT('insert into rolepermission_approval(role_id,permission_id,softdeleteflag,aprRequestId,crudAction) 
			values (''',_roleId,''',',@permissionsData,',','0',',''',_requestId,'''',',','''INS'');');

	PREPARE sql_query FROM @query; 
            EXECUTE sql_query;
		
         END IF;
    END LOOP insertPermissions;
END$$
DELIMITER ;


DROP procedure IF EXISTS `rolepermission_data_movement_approval_proc`;
DELIMITER $$
CREATE PROCEDURE `rolepermission_data_movement_approval_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _context VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI)
BEGIN
	IF _context = 'Approved' THEN
		INSERT INTO rolepermission select Role_id,Permission_id,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag,companyLegalUnit from rolepermission_approval where aprRequestId = _requestId;
	END IF;
	delete from rolepermission_approval where aprRequestId = _requestId and Role_id !='' and Permission_id != '';
END$$
DELIMITER ;


DROP procedure IF EXISTS `rolepermission_delete_approval_proc`;
DELIMITER $$
CREATE PROCEDURE `rolepermission_delete_approval_proc`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _context VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
DECLARE caid VARCHAR(50);
DECLARE isEnabled VARCHAR(10);
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM compositeaction c WHERE FIND_IN_SET(`Permission_id` COLLATE UTF8_GENERAL_CI, _PermissionIds));
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
SET SQL_SAFE_UPDATES = 0;
OPEN caids; 
MANAGECAIDS : LOOP
FETCH caids INTO caid, isEnabled;
IF finished = 1 THEN 
    LEAVE MANAGECAIDS;
END IF;
SET @caid_count =(SELECT COUNT(*) FROM compositeaction c,  rolepermission rp WHERE rp.Role_id COLLATE UTF8_GENERAL_CI = _roleId
AND rp.Permission_id COLLATE UTF8_GENERAL_CI =c.Permission_id COLLATE UTF8_GENERAL_CI
AND NOT FIND_IN_SET(rp.Permission_id COLLATE UTF8_GENERAL_CI ,_PermissionIds)
AND c.id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI);

IF @caid_count=0 THEN
IF _context = 'Approved' THEN
	DELETE FROM rolecompositeaction WHERE Role_id COLLATE UTF8_GENERAL_CI =_roleId AND CompositeAction_id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI;
END IF;
DELETE FROM rolecompositeaction_approval WHERE Role_id COLLATE UTF8_GENERAL_CI =_roleId AND CompositeAction_id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI AND aprRequestId COLLATE UTF8_GENERAL_CI =_requestId; 
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;

IF _context = 'Approved' THEN
	DELETE FROM rolepermission WHERE Role_id COLLATE UTF8_GENERAL_CI=_roleId AND FIND_IN_SET(Permission_id COLLATE
	UTF8_GENERAL_CI,_PermissionIds);
END IF;
 DELETE FROM rolepermission_approval WHERE Role_id COLLATE UTF8_GENERAL_CI=_roleId AND FIND_IN_SET(Permission_id COLLATE
 UTF8_GENERAL_CI,_PermissionIds) AND aprRequestId COLLATE UTF8_GENERAL_CI =_requestId; 
SET SQL_SAFE_UPDATES = 1;
END$$
DELIMITER ;


DROP procedure IF EXISTS `role_data_movement_to_approval_proc`;
DELIMITER $$
CREATE PROCEDURE `role_data_movement_to_approval_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI)
BEGIN
	Declare columnnames text;
	Set columnNames = (SELECT group_concat(COLUMN_NAME ORDER BY ORDINAL_POSITION) FROM 
	INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'role');
    Set @query = concat('insert into dbxdb.role_approval select ',columnNames,',''',_requestId
    ,''',','''','NONE','''',' from dbxdb.role where id=','''',_roleId,''';');
    prepare sql_query from @query;
    execute sql_query;
    
    
    Set columnNames = (SELECT group_concat(COLUMN_NAME ORDER BY ORDINAL_POSITION) FROM 
	INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'rolepermission');
    Set @query = concat('insert into dbxdb.rolepermission_approval select ',columnNames,',''',_requestId
    ,''',','''','NONE','''',' from dbxdb.rolepermission where Role_id=','''',_roleId,''';');
    prepare sql_query from @query;
    execute sql_query;
    
    
    Set columnNames = (SELECT group_concat(COLUMN_NAME ORDER BY ORDINAL_POSITION) FROM 
	INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'userrole');
    Set @query = concat('insert into dbxdb.userrole_approval select ',columnNames,',''',_requestId
    ,''',','''','NONE','''',' from dbxdb.userrole where Role_id=','''',_roleId,''';');
    prepare sql_query from @query;
    execute sql_query;
    
    
    Set columnNames = (SELECT group_concat(COLUMN_NAME ORDER BY ORDINAL_POSITION) FROM 
	INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'userroleservicedefinition');
    Set @query = concat('insert into dbxdb.userroleservicedefinition_approval select ',columnNames,',''',_requestId
    ,''',','''','NONE','''',' from dbxdb.userroleservicedefinition where UserRole_id=','''',_roleId,''';');
    prepare sql_query from @query;
    execute sql_query;
    
    Set columnNames = (SELECT group_concat(COLUMN_NAME ORDER BY ORDINAL_POSITION) FROM 
	INFORMATION_SCHEMA.COLUMNS WHERE TABLE_NAME = 'rolecompositeaction');
    Set @query = concat('insert into dbxdb.rolecompositeaction_approval select ',columnNames,',''',_requestId
    ,''',','''','NONE','''',' from dbxdb.rolecompositeaction where Role_id=','''',_roleId,''';');
    prepare sql_query from @query;
    execute sql_query;
    
END$$
DELIMITER ;


DROP procedure IF EXISTS `rolepermission_update_approval_proc`;
DELIMITER $$
CREATE PROCEDURE `rolepermission_update_approval_proc`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
DECLARE caid VARCHAR(50);
DECLARE isEnabled VARCHAR(10);
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM compositeaction c WHERE FIND_IN_SET(`Permission_id` COLLATE UTF8_GENERAL_CI, _PermissionIds));
DECLARE CONTINUE HANDLER
FOR NOT FOUND SET finished = 1;
SET SQL_SAFE_UPDATES = 0;
OPEN caids;
MANAGECAIDS : LOOP
FETCH caids INTO caid, isEnabled;
IF finished = 1 THEN
LEAVE MANAGECAIDS;
END IF;
SET @caid_count =(SELECT COUNT(*) FROM compositeaction c, rolepermission rp WHERE rp.Role_id COLLATE UTF8_GENERAL_CI = _roleId
AND rp.Permission_id COLLATE UTF8_GENERAL_CI =c.Permission_id COLLATE UTF8_GENERAL_CI
AND NOT FIND_IN_SET(rp.Permission_id COLLATE UTF8_GENERAL_CI ,_PermissionIds)
AND c.id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI);IF @caid_count=0 THEN
UPDATE rolecompositeaction_approval SET crudAction= 'DEL' WHERE aprRequestId COLLATE UTF8_GENERAL_CI=_requestId AND CompositeAction_id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI;
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;
UPDATE rolepermission_approval SET crudAction= 'DEL' WHERE aprRequestId COLLATE UTF8_GENERAL_CI=_requestId AND Role_id COLLATE UTF8_GENERAL_CI=_roleId AND FIND_IN_SET(Permission_id COLLATE UTF8_GENERAL_CI,_PermissionIds);
SET SQL_SAFE_UPDATES = 1;
END$$
DELIMITER ;


CREATE VIEW `internal_role_to_servicedefinition_mapping_view_approval` AS
    SELECT DISTINCT
        `servicedefinition`.`id` AS `ServiceDefinition_id`,
        `servicedefinition`.`name` AS `ServiceDefinition_Name`,
        `servicedefinition`.`description` AS `ServiceDefinition_Description`,
        `servicedefinition`.`serviceType` AS `ServiceDefinition_Type_id`,
        `servicedefinition`.`status` AS `ServiceDefinition_Status_id`,
        `role_approval`.`id` AS `InternalRole_id`,
        `role_approval`.`Type_id` AS `InternalRole_Type_id`,
        `role_approval`.`Status_id` AS `InternalRole_Status_id`,
        `role_approval`.`Name` AS `InternalRole_Name`,
        `role_approval`.`Description` AS `InternalRole_Description`,
        `userroleservicedefinition_approval`.`aprRequestId` AS `RequestIdForApprovalContext`
    FROM
        ((`servicedefinition`
        LEFT JOIN `userroleservicedefinition_approval` ON ((`servicedefinition`.`id` = `userroleservicedefinition_approval`.`servicedefinitionId`)))
        LEFT JOIN `role_approval` ON ((`role_approval`.`id` = `userroleservicedefinition_approval`.`UserRole_id`))) 
        WHERE (`userroleservicedefinition_approval`.`crudAction` != 'DEL' AND `role_approval`.`crudAction` != 'DEL');


CREATE VIEW `rolepermission_view_approval` AS
    SELECT DISTINCT
        `role_approval`.`Name` AS `Role_Name`,
        `role_approval`.`Description` AS `Role_Description`,
        `role_approval`.`Status_id` AS `Role_Status_id`,
        `rolepermission_approval`.`Role_id` AS `Role_id`,
        `permission`.`id` AS `Permission_id`,
        `permission`.`Type_id` AS `Permission_Type_id`,
        `permission`.`Status_id` AS `Permission_Status_id`,
        `permission`.`DataType_id` AS `DataType_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Description`,
        `permission`.`isComposite` AS `Permission_isComposite`,
        `permission`.`PermissionValue` AS `PermissionValue`,
        `permission`.`createdby` AS `Permission_createdby`,
        `permission`.`modifiedby` AS `Permission_modifiedby`,
        `permission`.`createdts` AS `Permission_createdts`,
        `permission`.`lastmodifiedts` AS `Permission_lastmodifiedts`,
        `permission`.`synctimestamp` AS `Permission_synctimestamp`,
        `permission`.`softdeleteflag` AS `Permission_softdeleteflag`,
        `rolepermission_approval`.`aprRequestId` AS `RequestIdForApprovalContext`
    FROM
        ((`rolepermission_approval`
        JOIN `permission` ON (((`rolepermission_approval`.`Permission_id` = `permission`.`id`))))
        JOIN `role_approval` ON ((`role_approval`.`id` = `rolepermission_approval`.`Role_id`))) 
        WHERE (`rolepermission_approval`.`crudAction` != 'DEL' AND `role_approval`.`crudAction` != 'DEL');


CREATE VIEW `roleuser_view_approval` AS
    SELECT DISTINCT
        `userrole_approval`.`User_id` AS `User_id`,
        `userrole_approval`.`Role_id` AS `Role_id`,
        `systemuser`.`Status_id` AS `Status_id`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`modifiedby` AS `UpdatedBy`,
        `systemuser`.`lastmodifiedts` AS `LastModifiedTimeStamp`,
        `userrole_approval`.`aprRequestId` AS `RequestIdForApprovalContext`
    FROM
        (`userrole_approval`
        JOIN `systemuser` ON ((`userrole_approval`.`User_id` = `systemuser`.`id`)))
        WHERE (`userrole_approval`.`crudAction` != 'DEL');


DROP PROCEDURE IF EXISTS `fetch_approvalrequests_counts_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalrequests_counts_proc`(
    IN _permissionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _userId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET @req_pending_status = 'Pending For Approval';
	SET @sql1 = CONCAT('
    select \'pendingRequests\' as category, count(*) as counts from `dbxdb`.`approvalrequests` where status = \'', @req_pending_status ,'\' and createdby = \'', _userId, '\' 
    union 
    select \'requestHistory\' as category, count(*) as counts from `dbxdb`.`approvalrequests` where createdby = \'', _userId, '\' 
    union 
    select \'pendingApprovals\' as category, count(*) as counts from `dbxdb`.`approvalrequests` where status = \'', @req_pending_status ,'\' and createdby != \'', _userId, '\' and expAPIOperationName in ( 
	select expAPIOperationName from `dbxdb`.`permissionapprovals` 
	where approvalPermissionName in (', _permissionListArr, ') 
    ) 
    union 
    select \'approvalHistory\' as category, count(*) as counts from `dbxdb`.`approvalrequests` where checkedBy = \'', _userId, '\';');
	PREPARE STMT FROM @sql1;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_pending_approvals_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_pending_approvals_proc`(
	IN _permissionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _userId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _moduleListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _actionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchStartDate VARCHAR(16) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchEndDate VARCHAR(16) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortParam VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci, -- status / checkedBy / createdby | DEFAULT : checkedby (for approval history) & createdby (for pending approvals)
    IN _sortOrder VARCHAR(8) CHARACTER SET UTF8 COLLATE utf8_general_ci -- ASC / DESC
)
BEGIN
	DECLARE dateFilterStmt TEXT;
	SET @dateField = 'ar.createdts';
    
    IF _sortParam IS NOT NULL THEN
		SET _sortParam = CONCAT('ar.', _sortParam);
	END IF;
    IF _searchStartDate IS NULL THEN	-- since no start date, no date filtering and show for all dates
		SET dateFilterStmt = '';
    ELSE
		SET dateFilterStmt = CONCAT(' AND DATE_FORMAT(', @dateField, ', \'%Y-%m-%d\') BETWEEN \'', _searchStartDate, 
        '\' AND ', IF( _searchEndDate IS NOT NULL, CONCAT('\'', _searchEndDate ,'\''), 'NOW()'));
    END IF;
    
	SET @req_pending_status = 'Pending For Approval';
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, cru.Username as `createdby`, ar.createdts, ar.status, cku.Username as `checkedBy`, ar.checkedts, ar.reason
					FROM `dbxdb`.`approvalrequests` ar
					LEFT JOIN `dbxdb`.`systemuser` cru ON cru.id = ar.createdby
					LEFT JOIN `dbxdb`.`systemuser` cku ON cku.id = ar.checkedBy',
				' WHERE ar.status = \'', @req_pending_status ,'\' AND ar.createdby != \'', _userId, '\'',
				IF( _moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', _moduleListArr, ')'), ''),
                IF( _featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', _featureListArr, ')'), ''),
                IF( _actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', _actionListArr, ')'), ''),
                dateFilterStmt,
                ' AND expAPIOperationName IN ( 
	SELECT expAPIOperationName FROM `dbxdb`.`permissionapprovals` 
	WHERE approvalPermissionName IN (', _permissionListArr, '))',
    ' ORDER BY ', IF( _sortParam IS NOT NULL AND _sortParam IN (@dateField), _sortParam, @dateField) , ' ', IF( _sortOrder IS NOT NULL AND (UPPER(_sortOrder) IN ('DESC', 'ASC')), _sortOrder, 'DESC'));
	PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_pending_approvals_proc_kc`;
DELIMITER $$
CREATE PROCEDURE `fetch_pending_approvals_proc_kc`(
	IN _permissionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _userId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _moduleListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _actionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchStartDate VARCHAR(16) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchEndDate VARCHAR(16) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortParam VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci, -- status / checkedBy / createdby | DEFAULT : checkedby (for approval history) & createdby (for pending approvals)
    IN _sortOrder VARCHAR(8) CHARACTER SET UTF8 COLLATE utf8_general_ci -- ASC / DESC
)
BEGIN
	DECLARE dateFilterStmt TEXT;
	SET @dateField = 'ar.createdts';
    
    IF _sortParam IS NOT NULL THEN
		SET _sortParam = CONCAT('ar.', _sortParam);
	END IF;
    IF _searchStartDate IS NULL THEN	-- since no start date, no date filtering and show for all dates
		SET dateFilterStmt = '';
    ELSE
		SET dateFilterStmt = CONCAT(' AND DATE_FORMAT(', @dateField, ', \'%Y-%m-%d\') BETWEEN \'', _searchStartDate, 
        '\' AND ', IF( _searchEndDate IS NOT NULL, CONCAT('\'', _searchEndDate ,'\''), 'NOW()'));
    END IF;
    
	SET @req_pending_status = 'Pending For Approval';
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, ar.createdby as `createdby`, ar.createdts, ar.status, ar.checkedBy as `checkedBy`, ar.checkedts, ar.reason
					FROM `dbxdb`.`approvalrequests` ar WHERE ar.status = \'', @req_pending_status ,'\' AND ar.createdby != \'', _userId, '\'',
				IF( _moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', _moduleListArr, ')'), ''),
                IF( _featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', _featureListArr, ')'), ''),
                IF( _actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', _actionListArr, ')'), ''),
                dateFilterStmt,
                ' AND expAPIOperationName IN ( 
	SELECT expAPIOperationName FROM `dbxdb`.`permissionapprovals` 
	WHERE approvalPermissionName IN (', _permissionListArr, '))',
    ' ORDER BY ', IF( _sortParam IS NOT NULL AND _sortParam IN (@dateField), _sortParam, @dateField) , ' ', IF( _sortOrder IS NOT NULL AND (UPPER(_sortOrder) IN ('DESC', 'ASC')), _sortOrder, 'DESC'));
	PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_approval_history_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approval_history_proc`(
    IN _userId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _moduleListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _actionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchStartDate VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchEndDate VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortParam VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci, -- status / checkedts / createdts | DEFAULT : checkedts (for approval history) & createdts (for pending approvals)
    IN _sortOrder VARCHAR(8) CHARACTER SET UTF8 COLLATE utf8_general_ci -- ASC / DESC
)
BEGIN
	DECLARE dateFilterStmt TEXT;
	SET @dateField = 'ar.checkedts';
    
    IF _sortParam IS NOT NULL THEN
		set _sortParam = CONCAT('ar.', _sortParam);
    END IF;
    
    IF _searchStartDate IS NULL THEN	-- since no start date, no date filtering and show for all dates
		SET dateFilterStmt = '';
    ELSE
		SET dateFilterStmt = CONCAT(' AND DATE_FORMAT(', @dateField, ', \'%Y-%m-%d\') BETWEEN \'', _searchStartDate, 
        '\' AND ', IF( _searchEndDate IS NOT NULL, CONCAT('\'', _searchEndDate ,'\''), 'NOW()'));
    END IF;
    
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, cru.Username as `createdby`, ar.createdts, ar.status, cku.Username as `checkedBy`, ar.checkedts, ar.reason
					FROM `dbxdb`.`approvalrequests` ar
					LEFT JOIN `dbxdb`.`systemuser` cru ON cru.id = ar.createdby
					LEFT JOIN `dbxdb`.`systemuser` cku ON cku.id = ar.checkedBy ',
				'WHERE ar.checkedby=\'', _userId, '\'',
				IF( _moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', _moduleListArr, ')'), ''),
                IF( _featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', _featureListArr, ')'), ''),
                IF( _actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', _actionListArr, ')'), ''),
                dateFilterStmt, 
                ' ORDER BY ', IF( _sortParam IS NOT NULL AND _sortParam IN ('ar.status', @dateField), _sortParam, @dateField) , ' ', IF( _sortOrder IS NOT NULL AND (UPPER(_sortOrder) IN ('DESC', 'ASC')), _sortOrder, 'DESC'));
	PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_approval_history_proc_kc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approval_history_proc_kc`(
    IN _userId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _moduleListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _actionListArr TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchStartDate VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchEndDate VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortParam VARCHAR(32) CHARACTER SET UTF8 COLLATE utf8_general_ci, -- status / checkedts / createdts | DEFAULT : checkedts (for approval history) & createdts (for pending approvals)
    IN _sortOrder VARCHAR(8) CHARACTER SET UTF8 COLLATE utf8_general_ci -- ASC / DESC
)
BEGIN
	DECLARE dateFilterStmt TEXT;
	SET @dateField = 'ar.checkedts';
    
    IF _sortParam IS NOT NULL THEN
		set _sortParam = CONCAT('ar.', _sortParam);
    END IF;
    
    IF _searchStartDate IS NULL THEN	-- since no start date, no date filtering and show for all dates
		SET dateFilterStmt = '';
    ELSE
		SET dateFilterStmt = CONCAT(' AND DATE_FORMAT(', @dateField, ', \'%Y-%m-%d\') BETWEEN \'', _searchStartDate, 
        '\' AND ', IF( _searchEndDate IS NOT NULL, CONCAT('\'', _searchEndDate ,'\''), 'NOW()'));
    END IF;
    
	SET @sqlStmt = CONCAT('SELECT ar.requestId, ar.recordId, ar.module, ar.feature, ar.expAPIOperationName, ar.expAPINickName, ar.permissionId, ar.permissionName, ar.createdby, ar.createdts, ar.status, ar.checkedBy, ar.checkedts, ar.reason
					FROM `dbxdb`.`approvalrequests` ar WHERE ar.checkedby=\'', _userId, '\'',
				IF( _moduleListArr IS NOT NULL , CONCAT(' AND ar.module IN (', _moduleListArr, ')'), ''),
                IF( _featureListArr IS NOT NULL , CONCAT(' AND ar.feature IN (', _featureListArr, ')'), ''),
                IF( _actionListArr IS NOT NULL , CONCAT(' AND ar.expAPINickName IN (', _actionListArr, ')'), ''),
                dateFilterStmt, 
                ' ORDER BY ', IF( _sortParam IS NOT NULL AND _sortParam IN ('ar.status', @dateField), _sortParam, @dateField) , ' ', IF( _sortOrder IS NOT NULL AND (UPPER(_sortOrder) IN ('DESC', 'ASC')), _sortOrder, 'DESC'));
	PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;

DROP procedure IF EXISTS `useractions_relative_create_proc`;
DELIMITER $$
CREATE PROCEDURE `useractions_relative_create_proc`(
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
						   AND  Group_id = _groupId );
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


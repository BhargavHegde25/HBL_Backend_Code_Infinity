-- >> Services or Entitlements Module redesign <<
SET sql_safe_updates = 0;
UPDATE service SET Feature_id= null;
SET sql_safe_updates = 1;
ALTER TABLE `feature` 
DROP COLUMN `DisplayDescription`,
DROP COLUMN `DisplayName`,
DROP COLUMN `code`,
CHANGE COLUMN `Name` `name` VARCHAR(100) NOT NULL ,
CHANGE COLUMN `Description` `description` VARCHAR(400) NULL DEFAULT NULL ;
SET sql_safe_updates = 0;
DELETE FROM feature;
SET sql_safe_updates = 1;
DROP TABLE IF EXISTS `groupactionlimit`;
DROP TABLE IF EXISTS `appactionlimit`;
DROP TABLE IF EXISTS `actiondisplaynamedescription`;
DROP TABLE IF EXISTS `customeractionlimit`;

DROP TABLE IF EXISTS `featuretermsandconditions`;
DROP TABLE IF EXISTS `featuredisplaynamedescription`;
DROP TABLE IF EXISTS `appfeature`;
DROP TABLE IF EXISTS `limittype`;

ALTER TABLE `service` 
DROP FOREIGN KEY `FK_Service_Feature_id`;

ALTER TABLE `feature` 
CHANGE COLUMN `id` `id` VARCHAR(255) NOT NULL ;
ALTER TABLE `feature` ADD COLUMN `App_id` VARCHAR(50) NOT NULL AFTER `id`, ADD KEY `FK_feature_App_id` (`App_id` ASC);
ALTER TABLE `feature` ADD CONSTRAINT `FK_feature_App_id` FOREIGN KEY (`App_id`) REFERENCES `app` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


ALTER TABLE `organisationaddress` 
CHANGE COLUMN `Organization_id` `Organization_id` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `organisationaddress` 
ADD INDEX `FK_organisationaddress_organizationId_idx` (`Organization_id` ASC);
;
ALTER TABLE `organisationaddress` 
ADD CONSTRAINT `FK_organisationaddress_organizationId`
  FOREIGN KEY (`Organization_id`)
  REFERENCES `organisation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `organisationcommunication` 
ADD INDEX `FK_orgcommunication_organizationId_idx` (`Organization_id` ASC);
;
ALTER TABLE `organisationcommunication` 
ADD CONSTRAINT `FK_orgcommunication_organizationId`
  FOREIGN KEY (`Organization_id`)
  REFERENCES `organisation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `organisationemployees` 
ADD INDEX `FK_orgemployees_organizationId_idx` (`Organization_id` ASC);
;
ALTER TABLE `organisationemployees` 
ADD CONSTRAINT `FK_orgemployees_organizationId`
  FOREIGN KEY (`Organization_id`)
  REFERENCES `organisation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  

ALTER TABLE `organisationowner` 
ADD INDEX `FK_organisationowner_organisationId_idx` (`Organization_id` ASC);
;
ALTER TABLE `organisationowner` 
ADD CONSTRAINT `FK_organisationowner_organisationId`
  FOREIGN KEY (`Organization_id`)
  REFERENCES `organisation` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

CREATE TABLE `limittype` (
  `id` VARCHAR(50) NOT NULL,
  `description` VARCHAR(255) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `limitsubtype` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `description` VARCHAR(50) NULL DEFAULT NULL,
  `LimitType_id` VARCHAR(50) NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  INDEX `FK_limitsubtype_limittype_idx` (`LimitType_id` ASC),
  CONSTRAINT `FK_limitsubtype_limittype`
    FOREIGN KEY (`LimitType_id`)
    REFERENCES `limittype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
CREATE TABLE `membergrouptype` (
  `id` VARCHAR(50) NOT NULL,
  `description` VARCHAR(300) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `actiontype` (
  `id` VARCHAR(50) NOT NULL,
  `description` VARCHAR(50) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
ALTER TABLE `feature` ADD COLUMN `Service_Fee` DECIMAL(20,2) NULL AFTER `Status_id`;  
ALTER TABLE `feature` ADD COLUMN `DisplaySequence` INT(11) NULL DEFAULT NULL AFTER `Service_Fee`;
ALTER TABLE `feature` ADD COLUMN `isPrimary` TINYINT(1) NOT NULL DEFAULT '0' AFTER `DisplaySequence`;
  
CREATE TABLE `featureaction` (
  `id` VARCHAR(255) NOT NULL,
  `Feature_id` VARCHAR(255) NULL,
  `App_id` varchar(50) NOT NULL,  
  `Type_id` VARCHAR(50) NULL DEFAULT NULL,
  `name` VARCHAR(100) NULL DEFAULT NULL,
  `description` VARCHAR(300) NULL DEFAULT NULL,
  `isAccountLevel` TINYINT(1) NOT NULL DEFAULT 0,
  `isMFAApplicable` TINYINT(1) NOT NULL DEFAULT 0,
  `notes` VARCHAR(2000) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `featureaction_unique_index` (`App_id`,`id`),
  KEY `FK_featureaction_app_id_idx` (`App_id`),
  INDEX `IXFK_featureaction_actiontype` (`Type_id` ASC),
  INDEX `FK_action_feature_id_idx` (`Feature_id` ASC),
  CONSTRAINT `FK_featureaction_app_id` 
    FOREIGN KEY (`App_id`) 
    REFERENCES `app` (`id`) 
    ON DELETE NO ACTION 
    ON UPDATE NO ACTION,  
  CONSTRAINT `FK_featureaction_actiontype`
    FOREIGN KEY (`Type_id`)
    REFERENCES `actiontype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_action_feature_id`
    FOREIGN KEY (`Feature_id`)
    REFERENCES `feature` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8; 
  
CREATE TABLE `actiondisplaynamedescription` (
  `Action_id` VARCHAR(255) NOT NULL,
  `Locale_id` VARCHAR(50) NOT NULL,
  `displayName` TEXT NOT NULL,
  `displayDescription` TEXT NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Action_id`, `Locale_id`),
  CONSTRAINT `FK_actiondisplaynamedescription_Action_id`
    FOREIGN KEY (`Action_id`)
    REFERENCES `featureaction` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_actiondisplaynamedescription_Locale_id`
    FOREIGN KEY (`Locale_id`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8; 

    
CREATE TABLE `featuredisplaynamedescription` (
  `Feature_id` VARCHAR(255) NOT NULL,
  `Locale_id` VARCHAR(50) NOT NULL,
  `displayName` TEXT NOT NULL,
  `displayDescription` TEXT NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Feature_id`, `Locale_id`),
  INDEX `FK_featuredisplaynamedescription_locale_idx` (`Locale_id` ASC),
  CONSTRAINT `FK_featuredisplaynamedescription_Feature_id`
    FOREIGN KEY (`Feature_id`)
    REFERENCES `feature` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_featuredisplaynamedescription_locale`
    FOREIGN KEY (`Locale_id`)
    REFERENCES `locale` (`Code`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8; 



CREATE TABLE `featureroletype` (
  `RoleType_id` VARCHAR(50) NOT NULL,
  `Feature_id` VARCHAR(255) NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_featureroletype_Feature_id_idx` (`Feature_id` ASC),
  PRIMARY KEY (`RoleType_id`, `Feature_id`),
  CONSTRAINT `FK_featureroletype_Feature_id`
    FOREIGN KEY (`Feature_id`)
    REFERENCES `feature` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_featureroletype_membergrouptype`
    FOREIGN KEY (`RoleType_id`)
    REFERENCES `membergrouptype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8; 


    
CREATE TABLE `organisationactionlimit` (
  `id` varchar(50) NOT NULL,
  `Organisation_id` varchar(50) NOT NULL,
  `Action_id` varchar(255) NOT NULL,
  `LimitType_id` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_organisationactionlimit` (`Organisation_id`,`Action_id`,`LimitType_id`),
  INDEX `FK_organisationactionlimit_organisation_idx` (`Organisation_id` ASC),
  INDEX `FK_organisationactionlimit_action_idx` (`Action_id` ASC),
  INDEX `FK_organisationactionlimit_limitsubtype_idx` (`LimitType_id` ASC),
  CONSTRAINT `FK_organisationactionlimit_action` FOREIGN KEY (`Action_id`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_organisationactionlimit_limitsubtype` FOREIGN KEY (`LimitType_id`) REFERENCES `limittype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_organisationactionlimit_organisation` FOREIGN KEY (`Organisation_id`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
CREATE TABLE `groupactionlimit` (
  `id` VARCHAR(50) NOT NULL,
  `Group_id` VARCHAR(50) NOT NULL,
  `Action_id` VARCHAR(255) NOT NULL,
  `LimitSubType_id` VARCHAR(50) NULL,
  `value` DECIMAL(20,2) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_groupactionlimit` (`Group_id`,`Action_id`,`LimitSubType_id`),
  INDEX `IXFK_groupactionlimit_Group` (`Group_id` ASC),
  INDEX `IXFK_groupactionlimit_Action` (`Action_id` ASC),
  INDEX `FK_groupactionlimit_limitsubtype_idx` (`LimitSubType_id` ASC),
  CONSTRAINT `FK_groupactionlimit_Group`
    FOREIGN KEY (`Group_id`)
    REFERENCES `membergroup` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_groupactionlimit_Action`
    FOREIGN KEY (`Action_id`)
    REFERENCES `featureaction` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_groupactionlimit_limitsubtype`
    FOREIGN KEY (`LimitSubType_id`)
    REFERENCES `limitsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
  
    
CREATE TABLE `customeractionlimit` (
  `id` VARCHAR(50) NOT NULL,
  `RoleType_id` VARCHAR(50) NOT NULL,
  `Customer_id` VARCHAR(50) NOT NULL,
  `Action_id` VARCHAR(255) NOT NULL,
  `Account_id` VARCHAR(50) NULL,
  `LimitSubType_id` VARCHAR(50) NULL,
  `value` DECIMAL(20,2) NULL DEFAULT '0.00',
  `isAllowed` TINYINT(1) NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_customeractionlimit` (`RoleType_id`,`Customer_id`,`Action_id`,`Account_id`,`LimitSubType_id`),
  INDEX `IXFK_CustomerActionLimit_Customer` (`Customer_id` ASC),
  INDEX `IXFK_CustomerActionLimit_Service` (`Action_id` ASC),
  INDEX `FK_CustomerActionLimit_LimitSubtype_id_idx` (`LimitSubType_id` ASC),
  INDEX `FK_CustomerActionLimit_RoleType_idx` (`RoleType_id` ASC),
  CONSTRAINT `FK_CustomerActionLimit_Customer`
    FOREIGN KEY (`Customer_id`)
    REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimit_LimitSubtype_id`
    FOREIGN KEY (`LimitSubType_id`)
    REFERENCES `limitsubtype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimit_Action`
    FOREIGN KEY (`Action_id`)
    REFERENCES `featureaction` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_CustomerActionLimit_customertype`
    FOREIGN KEY (`RoleType_id`)
    REFERENCES `membergrouptype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `customeractionlimit` RENAME TO  `customeraction` ;
ALTER TABLE `customeraction` 
DROP FOREIGN KEY `FK_CustomerActionLimit_LimitSubtype_id`;
ALTER TABLE `customeraction` 
DROP COLUMN `value`,
DROP COLUMN `LimitSubType_id`,
DROP INDEX `UNIQUE_customeractionlimit` ,
ADD UNIQUE INDEX `UNIQUE_customeractionlimit` (`RoleType_id` ASC, `Customer_id` ASC, `Action_id` ASC, `Account_id` ASC),
DROP INDEX `FK_CustomerActionLimit_LimitSubtype_id_idx` ;



ALTER TABLE `groupactionlimit` 
DROP FOREIGN KEY `FK_groupactionlimit_limitsubtype`;
ALTER TABLE `groupactionlimit` 
CHANGE COLUMN `LimitSubType_id` `LimitType_id` VARCHAR(50) NULL DEFAULT NULL ,
ADD INDEX `FK_groupactionlimit_limitsubtype_idx` (`LimitType_id` ASC),
DROP INDEX `FK_groupactionlimit_limitsubtype_idx` ;

ALTER TABLE `groupactionlimit` 
ADD CONSTRAINT `FK_groupactionlimit_limitsubtype`
  FOREIGN KEY (`LimitType_id`)
  REFERENCES `limittype` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
ALTER TABLE `membergroup` 
DROP FOREIGN KEY `FK_MemberGroup_Type_id`;
ALTER TABLE `membergroup` 
DROP INDEX `FK_MemberGroup_Type_id` ,
ADD INDEX `FK_MemberGroup_Type_id_idx` (`Type_id` ASC);


ALTER TABLE `appactionmfa` RENAME TO  `mfa` ;

ALTER TABLE `mfa` 
DROP FOREIGN KEY `FK_appactionmfa_AppAction_id`;
ALTER TABLE `mfa` 
DROP COLUMN `IsMFARequired`,
DROP COLUMN `AppAction_id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`),
DROP INDEX `FK_appactionmfa_AppAction_id_idx` ;

ALTER TABLE `mfa` 
ADD COLUMN `App_id` VARCHAR(50) NOT NULL AFTER `id`,
ADD COLUMN `Action_id` VARCHAR(50) NOT NULL AFTER `App_id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `App_id`, `Action_id`),
ADD INDEX `FK_appactionmfa_App_id_idx` (`App_id` ASC),
ADD INDEX `FK_appactionmfa_FeatureAction_id_idx` (`Action_id` ASC);

ALTER TABLE `featureaction` 
ADD COLUMN `MFA_id` VARCHAR(50) NULL AFTER `isMFAApplicable`,
ADD INDEX `FK_featureaction_mfa_idx` (`MFA_id` ASC);

ALTER TABLE `featureaction` 
ADD CONSTRAINT `FK_featureaction_mfa`
  FOREIGN KEY (`MFA_id`)
  REFERENCES `mfa` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

--  CREATE TABLE `organisationaccountsublimit` (
--  `id` VARCHAR(50) NOT NULL,
--  `name` VARCHAR(100) NULL,
--  `Organisation_id` int(11) NULL,
--  `Account_id` VARCHAR(50) NULL,
--  `LimitSubType_id` VARCHAR(50) NULL,
--  `value1` DECIMAL(20,2) NULL,
--  `value2` DECIMAL(20,2) NULL,
--  `createdby` VARCHAR(50) NULL DEFAULT NULL,
--  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
--  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
--  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
--  PRIMARY KEY (`id`),
--  INDEX `FK_organisationaccountsublimit_limitsubtype_idx` (`LimitSubType_id` ASC),
--  INDEX `FK_organisationaccountsublimit_organisation_idx` (`Organisation_id` ASC),
--  CONSTRAINT `FK_organisationaccountsublimit_limitsubtype`
--    FOREIGN KEY (`LimitSubType_id`)
--    REFERENCES `limitsubtype` (`id`)
--    ON DELETE NO ACTION
--    ON UPDATE NO ACTION,
--  CONSTRAINT `FK_organisationaccountsublimit_organisation`
--    FOREIGN KEY (`Organisation_id`)
--    REFERENCES `organisation` (`id`)
--    ON DELETE NO ACTION
--    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;
--    
-- ALTER TABLE `customer` 
-- ADD COLUMN `OrganisationLimitMatrix_id` VARCHAR(50) NULL AFTER `Organization_Id`;
--
-- ALTER TABLE `customer` 
-- ADD INDEX `FK_Customer_Organizationaccountsublimit_idx` (`OrganisationLimitMatrix_id` ASC);
--
-- ALTER TABLE `customer` 
-- ADD CONSTRAINT `FK_Customer_Organizationaccountsublimit`
--  FOREIGN KEY (`OrganisationLimitMatrix_id`)
--  REFERENCES `organisationaccountsublimit` (`id`)
--  ON DELETE NO ACTION
--  ON UPDATE NO ACTION;
  
  
CREATE TABLE `actionlimit` (
  `Action_id` VARCHAR(255) NOT NULL,
  `LimitType_id` VARCHAR(50) NOT NULL,
  `value` DECIMAL(20,2) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Action_id`, `LimitType_id`),
  INDEX `FK_actionlimit_limittype_idx` (`LimitType_id` ASC),
  CONSTRAINT `FK_actionlimit_featureaction`
    FOREIGN KEY (`Action_id`)
    REFERENCES `featureaction` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `FK_actionlimit_limittype`
    FOREIGN KEY (`LimitType_id`)
    REFERENCES `limittype` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE VIEW `organisation_action_limits_view` AS select `organisationactionlimit`.`id` AS `id`,`organisationactionlimit`.`Organisation_id` AS `Organisation_id`,`organisationactionlimit`.`Action_id` AS `Action_id`,`organisationactionlimit`.`LimitType_id` AS `LimitType_id`,`organisationactionlimit`.`value` AS `value`,`featureaction`.`isAccountLevel` AS `isAccountLevel` from (`organisationactionlimit` left join `featureaction` on((`featureaction`.`id` = `organisationactionlimit`.`Action_id`)));

DROP VIEW IF EXISTS `customergroupinfo_view`;
CREATE VIEW `customergroupinfo_view` AS select `customergroup`.`Customer_id` AS `Customer_id`,`customergroup`.`Group_id` AS `Group_id`,`membergroup`.`Name` AS `Group_name`,`membergroup`.`Description` AS `Group_Desc`,`membergroup`.`Status_id` AS `GroupStatus_id`,(select `status`.`Description` from `status` where (`membergroup`.`Status_id` = `status`.`id`)) AS `GroupStatus_name`,`membergroup`.`createdby` AS `Group_createdby`,`membergroup`.`modifiedby` AS `Group_modifiedby`,`membergroup`.`createdts` AS `Group_createdts`,`membergroup`.`lastmodifiedts` AS `Group_lastmodifiedts`,`membergroup`.`synctimestamp` AS `Group_synctimestamp`,`membergroup`.`Type_id` AS `Group_Type_id` from (`customergroup` join `membergroup` on((`customergroup`.`Group_id` = `membergroup`.`id`)));


ALTER TABLE `featureaction` 
ADD COLUMN `TermsAndConditions_id` VARCHAR(50) NULL AFTER `MFA_id`,
ADD INDEX `FK_featureaction_termandcondition_idx` (`TermsAndConditions_id` ASC);

ALTER TABLE `featureaction` 
ADD CONSTRAINT `FK_featureaction_termandcondition`
  FOREIGN KEY (`TermsAndConditions_id`)
  REFERENCES `termandcondition` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
ALTER TABLE `featureaction` ADD COLUMN `isPrimary` TINYINT(1) NOT NULL DEFAULT '0' AFTER `notes`;
ALTER TABLE `featureaction` ADD COLUMN `DisplaySequence` INT(11) NULL DEFAULT NULL AFTER `isPrimary`;
ALTER TABLE `featureaction` ADD COLUMN `dependency` VARCHAR(255) NULL DEFAULT NULL AFTER `DisplaySequence`;
CREATE VIEW `feature_actions_view` AS SELECT `featureaction`.`id` AS `id`,`featureaction`.`Feature_id` AS `Feature_id`,`featureaction`.`name` AS `action_name`,`featureaction`.`description` AS `action_description`,`featureaction`.`isAccountLevel` AS `isAccountLevel`,`featureaction`.`isMFAApplicable` AS `isMFAApplicable`,`featureaction`.`isPrimary` AS `isPrimary`,`featureaction`.`notes` AS `notes`,`featureaction`.`Type_id` AS `action_Type_id`,`featureaction`.`DisplaySequence` AS `action_displaysequence`,`featureaction`.`dependency` AS `action_dependency`,`feature`.`Status_id` AS `feature_status_id`,`feature`.`name` AS `feature_name`,`feature`.`description` AS `feature_description`,`feature`.`Type_id` AS `feature_Type_id`,`feature`.`DisplaySequence` AS `feature_displaysequence`,`feature`.`isPrimary` AS `feature_isPrimary`,`actionlimit`.`LimitType_id` AS `LimitType_id`,`actionlimit`.`value` AS `value`FROM ((`featureaction` LEFT JOIN `feature` ON ((`feature`.`id` = `featureaction`.`Feature_id`))) LEFT JOIN `actionlimit` ON ((`actionlimit`.`Action_id` = `featureaction`.`id`)));

ALTER TABLE `customeraction` 
ADD COLUMN `LimitSubType_id` VARCHAR(50) NULL AFTER `isAllowed`,
ADD COLUMN `value` DECIMAL(20,2) NULL AFTER `LimitSubType_id`,
ADD INDEX `FK_CustomerActionLimit_limitsubtype_idx` (`LimitSubType_id` ASC);

ALTER TABLE `customeraction` 
ADD CONSTRAINT `FK_CustomerActionLimit_limitsubtype`
  FOREIGN KEY (`LimitSubType_id`)
  REFERENCES `limitsubtype` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

DROP procedure IF EXISTS `get_filtered_companies_proc`;
DELIMITER $$
CREATE PROCEDURE `get_filtered_companies_proc`(
in _searchText varchar(60) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select * from organisation where name like Concat('%', _searchText,'%') order by name limit 20;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customer_requests_assign_proc`;
DELIMITER $$
CREATE PROCEDURE `customer_requests_assign_proc`(
IN _requestIds TEXT  CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _csrID varchar(200)  CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
		SET @updateclause = concat( "UPDATE `customerrequest` SET customerrequest.AssignedTo = ",quote(_csrID)," where customerrequest.id in (",func_escape_input_for_in_operator(_requestIds),")");
     	PREPARE stmt FROM @updateclause; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `archived_request_search_proc`;
DELIMITER $$
CREATE PROCEDURE `archived_request_search_proc`(
  in _dateInitialPoint char(50), 
  in _dateFinalPoint char(50), 
  in _requestStatusID char(50), 
  in _requestCategory char(50), 
  in _offset char(50), 
  in _sortCriteria char(50), 
  in _sortOrder char(50), 
  in _requestAssignedTo char(50), 
  in _searchKey char(50), 
  in _messageRepliedBy char(50), 
  in _recordsPerPage varchar(50),
  in _queryType char(50)
)
BEGIN 
SET 
  @selectClause := IF(
    IFNULL(_queryType, '') = "count", 
    'count(acr.id) AS cnt', 
    'acr.id AS customerrequest_id'
  );
SET 
  @stmt := CONCAT(
    'SELECT ', @selectClause, ' FROM archivedcustomerrequest acr '
  );
SET 
  @whereclause := ' WHERE true';
SET 
  @joinCustomer := 0;
IF _dateInitialPoint != '' 
AND _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) >= ", 
    quote(_dateInitialPoint), " 
    AND ", 
    " TIMESTAMP(DATE(acr.createdts)) <= ", quote(_dateFinalPoint)
  );
ELSEIF _dateInitialPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) = ", 
    quote(_dateInitialPoint)
  );
ELSEIF _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) = ", 
    quote(_dateFinalPoint)
  );
END IF;
IF _requestStatusID != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.Status_id IN 
    (
      ", 
    func_escape_input_for_in_operator(_requestStatusID), "
    )
    "
  );
END IF;
IF _requestCategory != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.RequestCategory_id = ", 
    quote(_requestCategory)
  );
END IF;
IF _requestAssignedTo != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.AssignedTo = ", 
    quote(_requestAssignedTo)
  );
END IF;
IF _messageRepliedBy != '' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN archivedrequestmessage ON (acr.id = archivedrequestmessage.CustomerRequest_id) '
  );
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND archivedrequestmessage.RepliedBy_id = ", 
    quote(_messageRepliedBy)
  );
END IF;
IF _searchKey != '' THEN 
SET 
  @joinCustomer = 1;
SET _searchKey=CONCAT("%",_searchKey,"%");
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND 
  (
    acr.Customer_id LIKE ", 
    quote(_searchKey), " OR acr.id LIKE ", 
    quote(_searchKey), " OR customer.UserName LIKE ", 
    quote(_searchKey), ")"
  );
END IF;
IF _queryType != 'count' THEN IF _sortCriteria = 'customer_Fullname' THEN 
SET 
  @joinCustomer = 1;
END IF;

SET @sortColumn='cr.lastmodifiedts';
IF( _sortCriteria = 'customerrequest_Customer_id') THEN
    SET @sortColumn = 'acr.Customer_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo') THEN
    SET @sortColumn = 'acr.AssignedTo';
ELSEIF( _sortCriteria = 'customerrequest_createdts') THEN
    SET @sortColumn = 'acr.createdts';
ELSEIF( _sortCriteria = 'customerrequest_RequestCategory_id') THEN
    SET @sortColumn = 'acr.RequestCategory_id';
ELSEIF( _sortCriteria = 'customer_Fullname') THEN
    SET @sortColumn = 'CONCAT(customer.FirstName,customer.LastName)';
ELSEIF( _sortCriteria = 'customerrequest_Status_id') THEN
    SET @sortColumn = 'acr.Status_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo_Name') THEN
    SET @sortColumn = 'CONCAT(systemuser.FirstName,systemuser.LastName)';
END IF;

SET 
  @whereclause = CONCAT(
    @whereclause, 
    " 
  ORDER BY
    ", 
    IF(
      @sortColumn = '', 'acr.lastmodifiedts', 
      @sortColumn
    ), 
    ' ', 
    IF(
      @sortColumn = '' 
      OR _sortOrder = '', 
      'DESC', 
      _sortOrder
    )
  );
SET 
  @whereclause = CONCAT(
    @whereclause, 
    " LIMIT ", 
    IF(_offset = '', '0', _offset),
    ',',
    IF(_recordsPerPage = '', '10', _recordsPerPage)
  );
END IF;
IF @joinCustomer = 1 THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN customer ON (acr.Customer_id = customer.id) '
  );
END IF;
IF _sortCriteria = 'customerrequest_AssignedTo_Name' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN systemuser ON (acr.AssignedTo = systemuser.id) '
  );
END IF;
SET 
  @stmt = CONCAT(@stmt, @whereclause);
PREPARE stmt 
FROM 
  @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customer_request_search_proc`;
DELIMITER $$
CREATE PROCEDURE `customer_request_search_proc`(
  in _dateInitialPoint varchar(50), 
  in _dateFinalPoint varchar(50), 
  in _requestStatusID varchar(50), 
  in _requestCategory varchar(50), 
  in _offset varchar(50), 
  in _sortCriteria varchar(50), 
  in _sortOrder varchar(50), 
  in _requestAssignedTo varchar(50), 
  in _searchKey varchar(50), 
  in _messageRepliedBy varchar(50), 
  in _recordsPerPage varchar(50),
  in _queryType varchar(50)
)
BEGIN 
SET 
  @selectClause := IF(
    IFNULL(_queryType, '') = "count", 
    'count(cr.id) AS cnt', 
    'cr.id AS customerrequest_id'
  );
SET 
  @stmt := CONCAT(
    'SELECT ', @selectClause, ' FROM customerrequest cr '
  );
SET 
  @whereclause := ' WHERE true';
SET 
  @joinCustomer := 0;
IF _dateInitialPoint != '' 
AND _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND TIMESTAMP(DATE(cr.createdts)) >= ", 
    quote(_dateInitialPoint), "
  AND ", 
    " TIMESTAMP(DATE(cr.createdts)) <= ",quote(_dateFinalPoint)
  );
ELSEIF _dateInitialPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND TIMESTAMP(DATE(cr.createdts)) = ", 
    quote(_dateInitialPoint)
  );
ELSEIF _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND TIMESTAMP(DATE(cr.createdts)) = ", 
    quote(_dateFinalPoint)
  );
END IF;
IF _requestStatusID != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND cr.Status_id IN 
  (
    ", 
   func_escape_input_for_in_operator(_requestStatusID), "
  )
  "
  );
END IF;
IF _requestCategory != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND cr.RequestCategory_id = ", 
    quote(_requestCategory)
  );
END IF;
IF _requestAssignedTo != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND cr.AssignedTo = ", 
    quote(_requestAssignedTo)
  );
END IF;
IF _messageRepliedBy != '' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'JOIN requestmessage ON (cr.id = requestmessage.CustomerRequest_id) '
  );
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND requestmessage.RepliedBy_id = ", 
    quote(_messageRepliedBy)
  );
END IF;
IF _searchKey != '' THEN 
SET 
  @joinCustomer = 1;
SET _searchKey=CONCAT("%",_searchKey,"%");
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND 
  (
    cr.Customer_id LIKE ", 
    quote(_searchKey), " OR cr.id LIKE ", 
    quote(_searchKey), " OR customer.UserName LIKE ", 
    quote(_searchKey), ")"
  );
END IF;
IF _queryType != 'count' THEN IF _sortCriteria = 'customer_Fullname' THEN 
SET 
  @joinCustomer = 1;
END IF;

SET @sortColumn='cr.lastmodifiedts';
IF( _sortCriteria = 'customerrequest_Customer_id') THEN
    SET @sortColumn = 'cr.Customer_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo') THEN
    SET @sortColumn = 'cr.AssignedTo';
ELSEIF( _sortCriteria = 'customerrequest_createdts') THEN
    SET @sortColumn = 'cr.createdts';
ELSEIF( _sortCriteria = 'customerrequest_RequestCategory_id') THEN
    SET @sortColumn = 'cr.RequestCategory_id';
ELSEIF( _sortCriteria = 'customer_Fullname') THEN
    SET @sortColumn = 'CONCAT(customer.FirstName,customer.LastName)';
ELSEIF( _sortCriteria = 'customerrequest_Status_id') THEN
    SET @sortColumn = 'cr.Status_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo_Name') THEN
    SET @sortColumn = 'CONCAT(systemuser.FirstName,systemuser.LastName)';
END IF;
  
  
SET 
  @whereclause = CONCAT(
    @whereclause, 
    " 
ORDER BY
  ", 
    IF(
      @sortColumn = '', 'cr.lastmodifiedts', 
      @sortColumn
    ), 
    ' ', 
    IF(
      @sortColumn = '' 
      OR _sortOrder = '', 
      'DESC', 
      _sortOrder
    )
  );

SET 
  @whereclause = CONCAT(
    @whereclause, 
    " LIMIT ", 
    IF(_offset = '', '0', _offset),
    ',',
    IF(_recordsPerPage = '', '10', _recordsPerPage)
  );
END IF;
IF @joinCustomer = 1 THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'JOIN customer ON (cr.Customer_id = customer.id) '
  );
END IF;
IF _sortCriteria = 'customerrequest_AssignedTo_Name' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN systemuser ON (cr.AssignedTo = systemuser.id) '
  );
END IF;

SET 
  @stmt = CONCAT(@stmt, @whereclause);
PREPARE stmt 
FROM 
  @stmt;

 -- select @stmt;  
 EXECUTE stmt;
 DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP FUNCTION IF EXISTS `func_escape_input_for_in_operator`;
DELIMITER $$
CREATE FUNCTION `func_escape_input_for_in_operator`(
  _input TEXT
) RETURNS text CHARSET utf8
    DETERMINISTIC
BEGIN
    DECLARE result TEXT;
    DECLARE element TEXT;
    DECLARE ctr LONG;
    SET ctr = 1;
    SET result = '';
    
    readinput: LOOP
		SET element = func_split_str(_input, ',', ctr);
		
		IF element = '' THEN
			LEAVE readinput;
		END IF;
        
        IF result != '' THEN
			set result = concat ( result ,',');
		END IF;
		
        set result = concat ( result , quote(element));        
        SET ctr = ctr + 1;
		ITERATE readinput;
    END LOOP readinput;
    
    RETURN (result);
END$$
DELIMITER ;

DROP FUNCTION IF EXISTS `func_split_str`;
DELIMITER $$
CREATE FUNCTION `func_split_str`(
  X TEXT,
  delim TEXT,
  pos INT
) RETURNS text CHARSET utf8
    READS SQL DATA
    DETERMINISTIC
RETURN REPLACE(SUBSTRING(SUBSTRING_INDEX(X, delim, pos),
       LENGTH(SUBSTRING_INDEX(X, delim, pos -1)) + 1),
       delim, '')$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `mfa_c360_feature_get_proc`;
DELIMITER $$
CREATE PROCEDURE `mfa_c360_feature_get_proc`()
BEGIN
SELECT DISTINCT feature.id AS feature_id, feature.name AS feature_name,feature.App_id as App_id
	FROM feature 
	JOIN featureaction ON (feature.id = featureaction.Feature_id)
	WHERE featureaction.isMFAApplicable = 1 and feature.Status_id = "SID_FEATURE_ACTIVE"
	ORDER BY feature.id;

END$$
DELIMITER ;

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
        CONCAT('****', RIGHT(`customer`.`Ssn`, 4)) AS `SSN`,
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
        `customer`.`CustomerType_id` AS `CustomerType_id`,
        `customertype`.`Name` AS `CustomerType_Name`,
        `customertype`.`Description` AS `CustomerType_Description`,
		`membergroup`.`Name` AS `Customer_Role`,
		`membergroup`.`isEAgreementActive` AS `isEAgreementRequired`,
        `customer`.`Organization_Id` AS `organisation_id`,
        `organisation`.`Name` AS `organisation_name`,
        `primaryphone`.`Value` AS `PrimaryPhoneNumber`,
        `primaryemail`.`Value` AS `PrimaryEmailAddress`,
        `customer`.`DocumentsSubmitted` AS `DocumentsSubmitted`,
        `customer`.`ApplicantChannel` AS `ApplicantChannel`,
        `customer`.`Product` AS `Product`,
        `customer`.`Reason` AS `Reason`,
        @accountLockoutTime AS accountLockoutTime
    FROM
        ((((((((`customer`
        LEFT JOIN `location` ON ((`customer`.`Location_id` = `location`.`id`)))
        LEFT JOIN `organisation` ON ((`customer`.`Organization_Id` = `organisation`.`id`)))
        JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
        LEFT JOIN `status` `customerstatus` ON ((`customer`.`Status_id` = `customerstatus`.`id`)))
        LEFT JOIN `status` `maritalstatus` ON ((`customer`.`MaritalStatus_id` = `maritalstatus`.`id`)))
        LEFT JOIN `status` `employementstatus` ON ((`customer`.`EmployementStatus_id` = `employementstatus`.`id`)))
        LEFT JOIN `customercommunication` `primaryphone` ON (((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`isPrimary` = 1)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))))
        LEFT JOIN `customercommunication` `primaryemail` ON (((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`isPrimary` = 1)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')))
		LEFT JOIN `customergroup` ON ((`customer`.`id` = `customergroup`.`Customer_id`))
        LEFT JOIN  `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))
            
    WHERE `customer`.`id` = _customerId
    LIMIT 1;

END$$
DELIMITER ;

DROP VIEW IF EXISTS `group_features_actions_view`;
CREATE VIEW `group_features_actions_view` AS SELECT `groupactionlimit`.`Group_id` AS `Group_id`,`groupactionlimit`.`Action_id` AS `Action_id`,`groupactionlimit`.`LimitType_id` AS `LimitType_id`,`groupactionlimit`.`value` AS `value`,`groupactionlimit`.`id` AS `groupactionlimit_id`,`membergroup`.`Type_id` AS `Type_id`,`membergroup`.`Name` AS `Group_name`,`membergroup`.`Description` AS `Group_description`,`featureaction`.`name` AS `Action_name`,`featureaction`.`description` AS `Action_description`,`featureaction`.`Type_id` AS `Action_Type_id`,`featureaction`.`Feature_id` AS `Feature_id`,`featureaction`.`isMFAApplicable` AS `isMFAApplicable`,`featureaction`.`isAccountLevel` AS `isAccountLevel`,`featureaction`.`isPrimary` AS `isPrimary`,`featureaction`.`DisplaySequence` AS `Action_displaysequence`,`featureaction`.`dependency` AS `Action_dependency`,`feature`.`name` AS `Feature_name`,`feature`.`description` AS `Feature_description`,`feature`.`Type_id` AS `Feature_Type_id`,`feature`.`Status_id` AS `Feature_Status_id`,`feature`.`DisplaySequence` AS `Feature_displaysequence`,`feature`.`isPrimary` AS `Feature_isPrimary` FROM (((`groupactionlimit` LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `groupactionlimit`.`Group_id`))) LEFT JOIN `featureaction` ON ((`featureaction`.`id` = `groupactionlimit`.`Action_id`))) LEFT JOIN `feature` ON ((`feature`.`id` = `featureaction`.`Feature_id`)));

-- Customer actions procedure
DROP PROCEDURE IF EXISTS `customer_actions_proc`;
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
        IF(`membergroup`.`Type_id` = 'TYPE_ID_SMALL_BUSINESS' OR `membergroup`.`Type_id` = 'TYPE_ID_MICRO_BUSINESS', 
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
END $$
DELIMITER ;

DROP VIEW IF EXISTS `feature_details_view`;
CREATE VIEW `feature_details_view` AS select `feature`.`id` AS `id`,`feature`.`name` AS `name`,`feature`.`description` AS `description`,`feature`.`Type_id` AS `Type_id`,`feature`.`Status_id` AS `Status_id`,`featuredisplaynamedescription`.`Locale_id` AS `Locale_id`,`featuredisplaynamedescription`.`displayName` AS `displayName`,`featuredisplaynamedescription`.`displayDescription` AS `displayDescription` from (`feature` left join `featuredisplaynamedescription` on((`featuredisplaynamedescription`.`Feature_id` = `feature`.`id`)));



ALTER TABLE `customeraction` 
DROP FOREIGN KEY `FK_CustomerActionLimit_limitsubtype`;
ALTER TABLE `customeraction` 
CHANGE COLUMN `LimitSubType_id` `LimitType_id` VARCHAR(50) NULL DEFAULT NULL,
ADD INDEX `FK_CustomerActionLimit_limitsubtype_idx` (`LimitType_id` ASC),
DROP INDEX `FK_CustomerActionLimit_limitsubtype_idx` ;

ALTER TABLE `customeraction` 
ADD CONSTRAINT `FK_CustomerActionLimit_limittype`
  FOREIGN KEY (`LimitType_id`)
  REFERENCES `limittype` (`id`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  
DROP TABLE `limitsubtype`;

ALTER TABLE `customeraction`
DROP INDEX `UNIQUE_customeractionlimit` ,
ADD UNIQUE INDEX `UNIQUE_customeractionlimit` (`RoleType_id` ASC, `Customer_id` ASC, `Action_id` ASC, `Account_id` ASC, `LimitType_id` ASC);

DROP VIEW IF EXISTS `feature_view`;
CREATE VIEW `feature_view` AS 
    SELECT 
        `feature`.`id` AS `Code`,
        GROUP_CONCAT(`membergrouptype`.`description`
            SEPARATOR ',') AS `Type`,
		GROUP_CONCAT(`membergrouptype`.`id`
            SEPARATOR ',') AS `Type_Id`,
        `feature`.`name` AS `Name`,
        `status`.`Description` AS `Status`,
        `status`.`id` AS `Status_Id`
    FROM
        (((`feature`
        LEFT JOIN `featureroletype` `fr` ON ((`fr`.`Feature_id` = `feature`.`id`)))
        LEFT JOIN `membergrouptype` ON ((`membergrouptype`.`id` = `fr`.`RoleType_id`)))
        LEFT JOIN `status` ON ((`feature`.`Status_id` = `status`.`id`)))
    GROUP BY `feature`.`id`;


DROP TABLE IF EXISTS `bbactedrequest`;
DROP TABLE IF EXISTS `bbrequest`;
DROP TABLE IF EXISTS `requestapprovalmatrix`;
DROP TABLE IF EXISTS `customerapprovalmatrix`;
DROP TABLE IF EXISTS `transactionfrequency`;
DROP TABLE IF EXISTS `approvalmatrix`;
DROP TABLE IF EXISTS `approvalrule`;
DROP TABLE IF EXISTS `transactiontypefeatureaction`;

CREATE TABLE IF NOT EXISTS `transactiontypefeatureaction` (
  `id` VARCHAR(50) NOT NULL,
  `transactiontype` VARCHAR(50) NOT NULL,
  `subtype` VARCHAR(50) NULL DEFAULT NULL,
  `actionId` VARCHAR(255) NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_transactiontypefeatureaction_action_idx` (`actionId`),
  CONSTRAINT `FK_transactiontypefeatureaction_actionid` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `bbrequest` (
  `requestId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionId` VARCHAR(50) NULL,
  `transactionTypeId` VARCHAR(50) NULL DEFAULT NULL,
  `requestTypeId` INT(11) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `requiredSets` INT(11) NULL DEFAULT NULL,
  `receivedSets` INT(11) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `softDelete` TINYINT(2) NULL DEFAULT '0',
  `accountId` VARCHAR(45) NULL DEFAULT NULL,
  INDEX `IDX_bbrequest_transactionId` (`transactionId` ASC),
  PRIMARY KEY (`requestId`),
  KEY `FK_bbrequest_customer_idx` (`createdby`),
  KEY `FK_bbrequest_bbstatus_idx` (`status`),
  KEY `FK_bbrequest_businessbankingcompany_idx` (`companyId`),
  KEY `FK_bbrequest_transactionTypeId` (`transactionTypeId`),
  KEY `FK_bbrequest_requestTypeId_idx` (`requestTypeId`),
  CONSTRAINT `FK_bbrequest_requestTypeId` FOREIGN KEY (`requestTypeId`) REFERENCES `bbgeneraltransactiontype` (`id`),
  CONSTRAINT `FK_bbrequest_bbstatus` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`),
  CONSTRAINT `FK_bbrequest_businessbankingcompany` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`),
  CONSTRAINT `FK_bbrequest_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bbrequest_customer` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE=InnoDB AUTO_INCREMENT=44 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `bbactedrequest` (
  `approvalId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `requestId` BIGINT(20) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `comments` VARCHAR(500) NOT NULL,
  `createdby` VARCHAR(50) NOT NULL,
  `action` VARCHAR(45) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`approvalId`),
  KEY `FK_bbactedrequest_bbstatus_idx` (`status`),
  KEY `FK_bbactedrequest_user_idx` (`createdby`),
  KEY `FK_bbactedrequest_requestId_idx` (`requestId`),
  KEY `FK_bbactedrequest_Organisation_id_idx` (`companyId`),
  CONSTRAINT `FK_bbactedrequest_Organisation_id` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bbactedrequest_requestId` FOREIGN KEY (`requestId`) REFERENCES `bbrequest` (`requestId`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `FK_bbactedrequest_bbstatus` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`),
  CONSTRAINT `FK_bbactedrequest_user_id` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `approvalrule` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `numberOfApprovals` INT NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `approvalmatrix` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(100) NOT NULL,
  `companyId` VARCHAR(50) NOT NULL,
  `actionId` VARCHAR(255) NOT NULL,
  `accountId` VARCHAR(50) NOT NULL,
  `approvalruleId` VARCHAR(50) NOT NULL,
  `limitTypeId` VARCHAR(50) NOT NULL,
  `lowerlimit` DECIMAL(20,2) NULL,
  `upperlimit` DECIMAL(20,2) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_approvalmatrix_approvalruleid_idx` (`approvalruleId`),
  KEY `FK_approvalmatrix_action_idx` (`actionId`),
  KEY `FK_approvalmatrix_companyIdx` (`companyId`),
  KEY `FK_approvalmatrix_limittype_idx` (`limitTypeId`),
  CONSTRAINT `FK_approvalmatrix_approvalruleid` FOREIGN KEY (`approvalruleId`) REFERENCES `approvalrule` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_approvalmatrix_actionid` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_approvalmatrix_limittypeid` FOREIGN KEY (`limitTypeId`) REFERENCES `limittype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_approvalmatrix_Companyid` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;


CREATE TABLE IF NOT EXISTS `requestapprovalmatrix` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `approvalMatrixId` BIGINT(20) NOT NULL,
  `requestId` BIGINT(20) NOT NULL,
  `receivedApprovals` INT(11) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_requestapprovalmatrix_request_idx` (`requestId`),
  KEY `FK_requestapprovalmatrix_approvalmatrix_idx` (`approvalMatrixId`),
  CONSTRAINT `FK_requestapprovalmatrix_bbrequest` FOREIGN KEY (`requestId`) REFERENCES `bbrequest` (`requestId`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_requestapprovalmatrix_approvalmatrix_id` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrix` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `customerapprovalmatrix` (
  `id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `customerId` VARCHAR(50) NOT NULL,
  `approvalMatrixId` BIGINT(20) NOT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_customerapprovalmatrix_customer_idx` (`customerId`),
  KEY `FK_customerapprovalmatrix_approvalmatrix_idx` (`approvalMatrixId`),
  CONSTRAINT `FK_customerapprovalmatrix_customer` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_customerapprovalmatrix_approvalmatrix` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrix` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `transactionfrequency` (
  `id` VARCHAR(50) NOT NULL,
  `value` VARCHAR(50) NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`))
ENGINE = InnoDB;

DROP TABLE IF EXISTS `billpaytranfers`;
CREATE TABLE IF NOT EXISTS `billpaytranfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTypeId` VARCHAR(50) NOT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `toAccountNumber` VARCHAR(50) NULL,
  `billerId` VARCHAR(50) NULL,
  `amount` DOUBLE NOT NULL,
  `status` INT(2) NOT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `frequencystartdate` DATE NULL,
  `frequencyenddate` DATE NULL,
  `numberOfRecurrences` INT NULL,
  `scheduledDate` DATE NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_billpaytranfers_requestId` (`requestId` ASC) ,
  INDEX `FK_billpaytranfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_billpaytranfers_scheduledDate` (`scheduledDate` ASC) ,
  INDEX `FK_billpaytranfers_frequencyTypeId` (`frequencyTypeId` ASC),
  PRIMARY KEY (`transactionId`),
  KEY `FK_billpaytranfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_billpaytranfers_companyId` (`companyId`),
  KEY `FK_billpaytranfers_createdby` (`createdby`),
  KEY `FK_billpaytranfers_status` (`status`),
  CONSTRAINT `FK_billpaytranfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_billpaytranfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_billpaytranfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_billpaytranfers_status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `p2ptransfers`;

CREATE TABLE IF NOT EXISTS `p2ptransfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTypeId` VARCHAR(50) NOT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `toAccountNumber` VARCHAR(50) NULL,
  `personId` VARCHAR(50) NULL,
  `amount` DOUBLE NULL DEFAULT NULL,
  `status` INT(2) NOT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `description` VARCHAR(255) NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `frequencystartdate` DATE NULL,
  `frequencyenddate` DATE NULL,
  `numberOfRecurrences` INT NULL,
  `scheduledDate` DATE NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_p2ptransfers_requestId` (`requestId` ASC) ,
  INDEX `FK_p2ptransfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_p2ptransfers_scheduledDate` (`scheduledDate` ASC) ,
  INDEX `FK_p2ptransfers_frequencyTypeId` (`frequencyTypeId` ASC),
  PRIMARY KEY (`transactionId`),
  KEY `FK_p2ptransfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_p2ptransfers_companyId` (`companyId`),
  KEY `FK_p2ptransfers_createdby` (`createdby`),
  KEY `FK_p2ptransfers_status` (`status`),
  CONSTRAINT `FK_p2ptransfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_p2ptransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_p2ptransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_p2ptransfers_status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `accounttransfers`;
CREATE TABLE IF NOT EXISTS `accounttransfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTypeId` VARCHAR(50) NOT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `toAccountNumber` VARCHAR(50) NULL,
  `amount` DOUBLE NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `iban` VARCHAR(50) NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `frequencystartdate` DATE NULL,
  `frequencyenddate` DATE NULL,
  `numberOfRecurrences` INT NULL,
  `scheduledDate` DATE NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_accounttransfers_requestId` (`requestId` ASC) ,
  INDEX `FK_accounttransfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_accounttransfers_scheduledDate` (`scheduledDate` ASC) ,
  INDEX `FK_accounttransfers_frequencyTypeId` (`frequencyTypeId` ASC),
  PRIMARY KEY (`transactionId`),
  KEY `FK_accounttransfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_accounttransfers_companyId` (`companyId`),
  KEY `FK_accounttransfers_createdby` (`createdby`),
  KEY `FK_accounttransfers_status` (`status`),
  CONSTRAINT `FK_accounttransfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_accounttransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_accounttransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_accounttransfers_status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `wiretransfers`;
CREATE TABLE IF NOT EXISTS `wiretransfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `payeeId` VARCHAR(50) NULL,
  `payeeCurrency` VARCHAR(50) NULL,
  `amount` DOUBLE NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `scheduledDate` DATE NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_wiretransfers_requestId` (`requestId` ASC) ,
  INDEX `FK_wiretransfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_wiretransfers_scheduledDate` (`scheduledDate` ASC) ,
  PRIMARY KEY (`transactionId`),
  KEY `FK_wiretransfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_wiretransfers_companyId` (`companyId`),
  KEY `FK_wiretransfers_createdby` (`createdby`),
  KEY `FK_wiretransfers_status` (`status`),
  CONSTRAINT `FK_wiretransfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_wiretransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_wiretransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_wiretransfers_status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `accounttransfers`;
DROP TABLE IF EXISTS `internaltransfers`;
DROP TABLE IF EXISTS `externaltransfers`;

CREATE TABLE IF NOT EXISTS `internaltransfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTypeId` VARCHAR(50) NOT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `toAccountNumber` VARCHAR(50) NULL,
  `amount` DOUBLE NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `iban` VARCHAR(50) NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `frequencystartdate` DATE NULL,
  `frequencyenddate` DATE NULL,
  `numberOfRecurrences` INT NULL,
  `scheduledDate` DATE NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_internaltransfers_requestId` (`requestId` ASC) ,
  INDEX `FK_internaltransfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_internaltransfers_scheduledDate` (`scheduledDate` ASC) ,
  INDEX `FK_internaltransfers_frequencyTypeId` (`frequencyTypeId` ASC),
  PRIMARY KEY (`transactionId`),
  KEY `FK_internaltransfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_internaltransfers_companyId` (`companyId`),
  KEY `FK_internaltransfers_createdby` (`createdby`),
  KEY `FK_internaltransfers_status` (`status`),
  CONSTRAINT `FK_internaltransfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_internaltransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_internaltransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_internaltransfers_status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

CREATE TABLE IF NOT EXISTS `externaltransfers` (
  `transactionId` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `transactionTypeId` VARCHAR(50) NOT NULL,
  `companyId` VARCHAR(50) NULL DEFAULT NULL,
  `frequencyTypeId` VARCHAR(50) NOT NULL,
  `requestId` BIGINT(20) NULL DEFAULT NULL,
  `fromAccountNumber` VARCHAR(50) NOT NULL,
  `toAccountNumber` VARCHAR(50) NULL,
  `amount` DOUBLE NULL DEFAULT NULL,
  `status` INT(2) NULL DEFAULT NULL,
  `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL,
  `iban` VARCHAR(50) NULL,
  `notes` VARCHAR(255) NULL,
  `transactionts` TIMESTAMP NULL,
  `frequencystartdate` DATE NULL,
  `frequencyenddate` DATE NULL,
  `numberOfRecurrences` INT NULL,
  `scheduledDate` DATE NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  INDEX `FK_externaltransfers_requestId` (`requestId` ASC) ,
  INDEX `FK_externaltransfers_fromAccountNumber` (`fromAccountNumber` ASC) ,
  INDEX `FK_externaltransfers_scheduledDate` (`scheduledDate` ASC) ,
  INDEX `FK_externaltransfers_frequencyTypeId` (`frequencyTypeId` ASC),
  PRIMARY KEY (`transactionId`),
  KEY `FK_externaltransfers_transactionTypeId` (`transactionTypeId`),
  KEY `FK_externaltransfers_companyId` (`companyId`),
  KEY `FK_externaltransfers_createdby` (`createdby`),
  KEY `FK_externaltransfers_status` (`status`),
  CONSTRAINT `FK_externaltransfers_transactionTypeIdx` FOREIGN KEY (`transactionTypeId`) REFERENCES `transactiontypefeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_externaltransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_externaltransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`) ,
  CONSTRAINT `FK_externaltransfers_Status_idx` FOREIGN KEY (`status`) REFERENCES `bbstatus` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION)
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

ALTER TABLE `approvalmatrix` 
		DROP FOREIGN KEY `FK_approvalmatrix_approvalruleid`;
ALTER TABLE `approvalmatrix` 
		CHANGE COLUMN `approvalruleId` `approvalruleId` VARCHAR(50) NULL ;
ALTER TABLE `approvalmatrix` 
		ADD CONSTRAINT `FK_approvalmatrix_approvalruleid`
  		FOREIGN KEY (`approvalruleId`) REFERENCES `approvalrule` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `approvalmatrix` 
	CHANGE COLUMN `lowerlimit` `lowerlimit` DECIMAL(20,2) NOT NULL DEFAULT -1 ,
	CHANGE COLUMN `upperlimit` `upperlimit` DECIMAL(20,2) NOT NULL DEFAULT -1 ;


ALTER TABLE `customerapprovalmatrix` DROP FOREIGN KEY `FK_customerapprovalmatrix_approvalmatrix`;
ALTER TABLE `customerapprovalmatrix` ADD CONSTRAINT `FK_customerapprovalmatrix_approvalmatrix` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrix` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;

DROP TABLE IF EXISTS `onetimepayee`;

CREATE TABLE IF NOT EXISTS `onetimepayee` (
  `onetime_id` BIGINT(20) NOT NULL AUTO_INCREMENT,
  `payeeName` VARCHAR(50) NOT NULL,
  `payeeNickName` VARCHAR(50) NOT NULL,
  `payeeType` VARCHAR(200) NULL,
  `wireAccountType` VARCHAR(50) NOT NULL,
  `swiftCode` VARCHAR(50) NULL,
  `routingNumber` VARCHAR(50) NULL,
  `zipCode` VARCHAR(50) NULL,
  `cityName` VARCHAR(50) NULL,
  `state` VARCHAR(50) NULL,
  `country` VARCHAR(50) NULL,
  `payeeAddressLine1` VARCHAR(50) NULL,
  `payeeAddressLine2` VARCHAR(50) NULL,
  `bankName` VARCHAR(50) NULL,
  `internationalRoutingCode` VARCHAR(50) NULL,
  `bankAddressLine1` VARCHAR(50) NULL,
  `bankAddressLine2` VARCHAR(50) NULL,
  `bankCity` VARCHAR(50) NULL,
  `bankState` VARCHAR(50) NULL,
  `bankZip` VARCHAR(200) NULL,
  PRIMARY KEY (`onetime_id`))
ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

ALTER TABLE `wiretransfers` 
	ADD COLUMN `onetime_id` BIGINT(20) NULL AFTER `companyId`,
	ADD INDEX `FK_wiretransfers_onetime_idx_idx` (`onetime_id` ASC);

ALTER TABLE `wiretransfers` 
	ADD CONSTRAINT `FK_wiretransfers_onetime_idx` FOREIGN KEY (`onetime_id`) REFERENCES `onetimepayee` (`onetime_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `billpaytranfers` 
	ADD COLUMN `onetime_id` BIGINT(20) NULL AFTER `companyId`,
	ADD INDEX `FK_billpaytranfers_onetime_idx_idx` (`onetime_id` ASC);

ALTER TABLE `billpaytranfers` 
	ADD CONSTRAINT `FK_billpaytranfers_onetime_idx` FOREIGN KEY (`onetime_id`) REFERENCES `onetimepayee` (`onetime_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
	
ALTER TABLE `p2ptransfers` 
	ADD COLUMN `onetime_id` BIGINT(20) NULL AFTER `companyId`,
	ADD INDEX `FK_p2ptransfers_onetime_idx_idx` (`onetime_id` ASC);

ALTER TABLE `p2ptransfers` 
	ADD CONSTRAINT `FK_p2ptransfers_onetime_idx` FOREIGN KEY (`onetime_id`) REFERENCES `onetimepayee` (`onetime_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `internaltransfers` 
	ADD COLUMN `onetime_id` BIGINT(20) NULL AFTER `companyId`,
	ADD INDEX `FK_internaltransfers_onetime_idx_idx` (`onetime_id` ASC);

ALTER TABLE `internaltransfers` 
	ADD CONSTRAINT `FK_internaltransfers_onetime_idx` FOREIGN KEY (`onetime_id`) REFERENCES `onetimepayee` (`onetime_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
	
ALTER TABLE `externaltransfers` 
	ADD COLUMN `onetime_id` BIGINT(20) NULL AFTER `companyId`,
	ADD INDEX `FK_externaltransfers_onetime_idx_idx` (`onetime_id` ASC);

ALTER TABLE `externaltransfers` 
	ADD CONSTRAINT `FK_externaltransfers_onetime_idx` FOREIGN KEY (`onetime_id`) REFERENCES `onetimepayee` (`onetime_id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
	
	
ALTER TABLE `wiretransfers` 
	ADD COLUMN `wireFileExecution_id` INT(11) NULL AFTER `onetime_id`,
	ADD INDEX `FK_wiretransfers_wirefileexecution_idx_idx` (`wireFileExecution_id` ASC);

ALTER TABLE `wiretransfers` 
	ADD CONSTRAINT `FK_wiretransfers_wirefileexecution_idx` FOREIGN KEY (`wireFileExecution_id`) REFERENCES `bulkwirefiletransactdetails` (`bulkWireTransactionID`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `billpaytranfers` 
	RENAME TO `billpaytransfers` ;
	
DROP TABLE IF EXISTS `organisationfeatures`;

CREATE TABLE `organisationfeatures` (
  `id` varchar(50) NOT NULL,
  `organisationId` VARCHAR(50) NOT NULL,
  `featureId` varchar(255) NOT NULL,
  `featureStatus` varchar(45) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `UNIQUE_organisationfeatures` (`organisationId` ASC, `featureId` ASC),
  KEY `Index_OrganisationId` (`organisationId`),
  KEY `FK_featureId_idx` (`featureId`),
  CONSTRAINT `FK_OrganisationId` FOREIGN KEY (`organisationId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_featureId` FOREIGN KEY (`featureId`) REFERENCES `feature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `p2ptransfers` 
	 DROP FOREIGN KEY `FK_p2ptransfers_status_idx`;
	 
ALTER TABLE `billpaytransfers` 
	DROP FOREIGN KEY `FK_billpaytranfers_status_idx`;
	
ALTER TABLE `wiretransfers` 
	DROP FOREIGN KEY `FK_wiretransfers_status_idx`;
	
ALTER TABLE `internaltransfers` 
	DROP FOREIGN KEY `FK_internaltransfers_status_idx`;

ALTER TABLE `externaltransfers` 
	DROP FOREIGN KEY `FK_externaltransfers_Status_idx`;
	
ALTER TABLE `bbrequest` 
	DROP FOREIGN KEY `FK_bbrequest_bbstatus`;
	
ALTER TABLE `bbactedrequest` 
	DROP FOREIGN KEY `FK_bbactedrequest_bbstatus`;
	
ALTER TABLE `achfile` 
	DROP FOREIGN KEY `FK_achfile_bbstatus`;
	
DROP TABLE IF EXISTS `bbgeneraltransaction`;

ALTER TABLE `bbtemplate` 
	DROP FOREIGN KEY `FK_bbtemplate_bbstatus`;
	
ALTER TABLE `bbtransaction` 
	DROP FOREIGN KEY `FK_bbtransaction_bbstatus`;


DROP TABLE IF EXISTS `bbstatus`;

ALTER TABLE `bbtemplate` 
	CHANGE COLUMN `Status` `status` VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE `bbtemplate` 
	CHANGE COLUMN `Company_id` `Company_id` VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE `bbtemplate` 
	ADD INDEX `FK_bbtemplate_Organization_idx` (`Company_id` ASC);
ALTER TABLE `bbtemplate` 
	ADD CONSTRAINT `FK_bbtemplate_Organization`
	FOREIGN KEY (`Company_id`)
	REFERENCES `organisation` (`id`)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION;
	
ALTER TABLE `bbtransaction` 
	CHANGE COLUMN `Status` `status` VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE `bbtransaction` 
	CHANGE COLUMN `Company_id` `Company_id` VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE `bbtransaction` 
	ADD INDEX `FK_bbtransaction_Organization_idx` (`Company_id` ASC);

ALTER TABLE `bbtransaction` 
	ADD CONSTRAINT `FK_bbtransaction_Organization`
	FOREIGN KEY (`Company_id`)
	REFERENCES `organisation` (`id`)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION;



ALTER TABLE `achfile` 
	CHANGE COLUMN `Status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `achfile` 
	CHANGE COLUMN `Company_id` `Company_id` VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE `achfile` 
	ADD INDEX `FK_achfile_Company_id_idx` (`Company_id` ASC);
;
ALTER TABLE `achfile` 
	ADD CONSTRAINT `FK_achfile_Company_id`
	FOREIGN KEY (`Company_id`)
	REFERENCES `organisation` (`id`)
	ON DELETE NO ACTION
	ON UPDATE NO ACTION;

ALTER TABLE `bbactedrequest` 
	CHANGE COLUMN `Status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `bbrequest` 
	CHANGE COLUMN `Status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `externaltransfers` 
	CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `internaltransfers` 
	CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;
	
ALTER TABLE `p2ptransfers` 
	CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;

ALTER TABLE `wiretransfers` 
	CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;
	
ALTER TABLE `billpaytransfers` 
	CHANGE COLUMN `status` `status` VARCHAR(50) NULL DEFAULT NULL ;
	
ALTER TABLE `bbrequest`
	DROP FOREIGN KEY `FK_bbrequest_requestTypeId`;

DROP TABLE IF EXISTS `bbgeneraltransactiontype`;

ALTER TABLE `bbrequest` 
	DROP COLUMN `requestTypeId`,
	DROP INDEX `FK_bbrequest_requestTypeId_idx` ;

ALTER TABLE `billpaytransfers` 
	DROP FOREIGN KEY `FK_billpaytranfers_onetime_idx`;
ALTER TABLE `billpaytransfers` 
	CHANGE COLUMN `onetime_id` `payeeId` VARCHAR(20) NULL DEFAULT NULL ,
	DROP INDEX `FK_billpaytranfers_onetime_idx_idx` ;

ALTER TABLE `billpaytransfers` 
	DROP FOREIGN KEY `FK_billpaytranfers_transactionTypeIdx`;
ALTER TABLE `billpaytransfers` 
	ADD COLUMN `transactionType` VARCHAR(45) NULL AFTER `featureActionId`,
	ADD COLUMN `transactionCurrency` VARCHAR(45) NULL AFTER `transactionType`,
	ADD COLUMN `deliverBy` TIMESTAMP NULL AFTER `scheduledDate`,
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	CHANGE COLUMN `scheduledDate` `scheduledDate` TIMESTAMP NULL DEFAULT NULL ;
	
ALTER TABLE `billpaytransfers` 
	ADD CONSTRAINT `FK_billpaytranfers_featureaction_idx`
  		FOREIGN KEY (`featureActionId`)
  		REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
 
ALTER TABLE `billpaytransfers`
	ADD CONSTRAINT `FK_billpaytranfers_currency_code_idx`
  		FOREIGN KEY (`transactionCurrency`)
  		REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  		
ALTER TABLE `billpaytransfers` 
	ADD COLUMN `zipCode` VARCHAR(45) NULL AFTER `deliverBy`,
	CHANGE COLUMN `billerId` `billerId` VARCHAR(50) NULL DEFAULT NULL AFTER `payeeId`;
	
ALTER TABLE `externaltransfers` 
	DROP FOREIGN KEY `FK_externaltransfers_transactionTypeIdx`;
	
ALTER TABLE `externaltransfers` 
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	ADD INDEX `FK_externaltransfers_featureaction_Idx_idx` (`featureActionId` ASC),
	DROP INDEX `FK_externaltransfers_transactionTypeId` ;

ALTER TABLE `externaltransfers` 
	ADD CONSTRAINT `FK_externaltransfers_featureaction_Idx`
  	FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  	
ALTER TABLE `externaltransfers` 
	DROP FOREIGN KEY `FK_externaltransfers_onetime_idx`;
	
ALTER TABLE `externaltransfers` 
	ADD COLUMN `transactionType` VARCHAR(45) NULL AFTER `featureActionId`,
	CHANGE COLUMN `frequencyenddate` `transactionCurrency` VARCHAR(50) NULL DEFAULT NULL ,
	DROP COLUMN `onetime_id`,
	CHANGE COLUMN `requestId` `requestId` BIGINT(20) NULL DEFAULT NULL AFTER `companyId`,
	CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NOT NULL AFTER `requestId`,
	CHANGE COLUMN `iban` `fromAccountCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `transactionCurrency`,
	CHANGE COLUMN `numberOfRecurrences` `numberOfRecurrences` INT(11) NULL DEFAULT NULL AFTER `amount`,
	CHANGE COLUMN `frequencystartdate` `frequencyEndDate` TIMESTAMP NULL DEFAULT NULL ,
	CHANGE COLUMN `scheduledDate` `scheduledDate` TIMESTAMP NULL DEFAULT NULL ;
	  
ALTER TABLE `externaltransfers` 
	ADD INDEX `FK_externaltransfers_currency_transactionCurrency_Idx_idx` (`transactionCurrency` ASC);

ALTER TABLE `externaltransfers` 
	ADD CONSTRAINT `FK_externaltransfers_currency_transactionCurrency_Idx`
	  FOREIGN KEY (`transactionCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

ALTER TABLE `externaltransfers` 
	ADD INDEX `FK_externaltransfers_currency_fromAccountCurrency_Idx_idx` (`fromAccountCurrency` ASC);

ALTER TABLE `externaltransfers` 
	ADD CONSTRAINT `FK_externaltransfers_currency_fromAccountCurrency_Idx`
	  FOREIGN KEY (`fromAccountCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  	  
ALTER TABLE `internaltransfers` 
	DROP FOREIGN KEY `FK_internaltransfers_transactionTypeIdx`;
	
ALTER TABLE `internaltransfers` 
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	ADD INDEX `FK_internaltransfers_featureaction_Idx_idx` (`featureActionId` ASC),
	DROP INDEX `FK_internaltransfers_transactionTypeId` ;
	
ALTER TABLE `internaltransfers` 
	ADD CONSTRAINT `FK_internaltransfers_featureaction_Idx`
  	FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
	
ALTER TABLE `internaltransfers` 
	DROP FOREIGN KEY `FK_internaltransfers_onetime_idx`;
	  
	  
ALTER TABLE `internaltransfers` 
	ADD COLUMN `transactionType` VARCHAR(45) NULL AFTER `featureActionId`,
	ADD COLUMN `transactionCurrency` VARCHAR(45) NULL AFTER `toAccountNumber`,
	CHANGE COLUMN `frequencyenddate` `toAccountCurrency` VARCHAR(50) NULL DEFAULT NULL,
	CHANGE COLUMN `requestId` `requestId` BIGINT(20) NULL DEFAULT NULL AFTER `companyId`,
	CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NOT NULL AFTER `requestId`,
	CHANGE COLUMN `iban` `fromAccountCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `transactionCurrency`,
	CHANGE COLUMN `numberOfRecurrences` `numberOfRecurrences` INT(11) NULL DEFAULT NULL AFTER `amount`,
	CHANGE COLUMN `frequencystartdate` `frequencyEndDate` TIMESTAMP NULL DEFAULT NULL ,
	CHANGE COLUMN `scheduledDate` `scheduledDate` TIMESTAMP NULL DEFAULT NULL ;
	  
ALTER TABLE `internaltransfers` 
	ADD INDEX `FK_internaltransfers_currency_transactionCurrency_Idx_idx` (`transactionCurrency` ASC);

ALTER TABLE `internaltransfers` 
	ADD CONSTRAINT `FK_internaltransfers_currency_transactionCurrency_Idx`
	  FOREIGN KEY (`transactionCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

ALTER TABLE `internaltransfers` 
	ADD INDEX `FK_internaltransfers_currency_fromAccountCurrency_Idx_idx` (`fromAccountCurrency` ASC);

ALTER TABLE `internaltransfers` 
	ADD CONSTRAINT `FK_internaltransfers_currency_fromAccountCurrency_Idx`
	  FOREIGN KEY (`fromAccountCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	 
ALTER TABLE `internaltransfers` 
	ADD INDEX `FK_internaltransfers_currency_toAccountCurrency_Idx_idx` (`toAccountCurrency` ASC);

ALTER TABLE `internaltransfers` 
	ADD CONSTRAINT `FK_internaltransfers_currency_toAccountCurrency_Idx`
	  FOREIGN KEY (`toAccountCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `p2ptransfers` 
	DROP FOREIGN KEY `FK_p2ptransfers_transactionTypeIdx`;
	
ALTER TABLE `p2ptransfers` 
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	ADD INDEX `FK_p2ptransfers_featureaction_Idx_idx` (`featureActionId` ASC),
	DROP INDEX `FK_p2ptransfers_transactionTypeId` ;
	
ALTER TABLE `p2ptransfers` 
	ADD CONSTRAINT `FK_p2ptransfers_featureaction_Idx`
  	FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
	
ALTER TABLE `p2ptransfers` 
	DROP FOREIGN KEY `FK_p2ptransfers_onetime_idx`;
	  
ALTER TABLE `p2ptransfers` 
	ADD COLUMN `transactionType` VARCHAR(45) NULL AFTER `featureActionId`,
	ADD COLUMN `transactionCurrency` VARCHAR(45) NULL AFTER `toAccountNumber`,
	CHANGE COLUMN `frequencyenddate` `p2pContact` VARCHAR(50) NULL DEFAULT NULL,
	CHANGE COLUMN `requestId` `requestId` BIGINT(20) NULL DEFAULT NULL AFTER `companyId`,
	CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NOT NULL AFTER `requestId`,
	CHANGE COLUMN `onetime_id` `fromAccountCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `transactionCurrency`,
	CHANGE COLUMN `numberOfRecurrences` `numberOfRecurrences` INT(11) NULL DEFAULT NULL AFTER `amount`,
	CHANGE COLUMN `frequencystartdate` `frequencyEndDate` TIMESTAMP NULL DEFAULT NULL ,
	CHANGE COLUMN `scheduledDate` `scheduledDate` TIMESTAMP NULL DEFAULT NULL ,
	DROP INDEX `FK_p2ptransfers_onetime_idx_idx` ;
	  
ALTER TABLE `p2ptransfers` 
	ADD INDEX `FK_p2ptransfers_currency_transactionCurrency_Idx_idx` (`transactionCurrency` ASC);

ALTER TABLE `p2ptransfers` 
	ADD CONSTRAINT `FK_p2ptransfers_currency_transactionCurrency_Idx`
	  FOREIGN KEY (`transactionCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

ALTER TABLE `p2ptransfers` 
	ADD INDEX `FK_p2ptransfers_currency_fromAccountCurrency_Idx_idx` (`fromAccountCurrency` ASC);

ALTER TABLE `p2ptransfers` 
	ADD CONSTRAINT `FK_p2ptransfers_currency_fromAccountCurrency_Idx`
	  FOREIGN KEY (`fromAccountCurrency`)
	  REFERENCES `currency` (`code`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `wiretransfers` 
	DROP FOREIGN KEY `FK_wiretransfers_transactionTypeIdx`;
	
ALTER TABLE `wiretransfers` 
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	DROP INDEX `FK_wiretransfers_transactionTypeId` ;
	
ALTER TABLE `wiretransfers` 
	ADD CONSTRAINT `FK_wiretransfers_featureaction_Idx`
  	FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

ALTER TABLE `wiretransfers` 
	ADD COLUMN `payeeAccountNumber` VARCHAR(45) NULL AFTER `fromAccountNumber`,
	ADD COLUMN `transactionType` VARCHAR(45) NULL AFTER `featureActionId`;
	
ALTER TABLE `wiretransfers` 
	DROP COLUMN `scheduledDate`,
	DROP INDEX `FK_wiretransfers_scheduledDate`;

ALTER TABLE `bbrequest` 
	DROP FOREIGN KEY `FK_bbrequest_transactionTypeIdx`;
	
ALTER TABLE `bbrequest` 
	CHANGE COLUMN `transactionTypeId` `featureActionId` VARCHAR(50) NOT NULL ,
	ADD CONSTRAINT `FK_bbrequest_featureaction_Idx`
  	FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  	
DROP TABLE IF EXISTS `transactiontypefeatureaction`;

ALTER TABLE `billpaytransfers`
CHANGE COLUMN `frequencystartdate` `frequencystartdate` TIMESTAMP NULL DEFAULT NULL ,
CHANGE COLUMN `frequencyenddate` `frequencyenddate` TIMESTAMP NULL DEFAULT NULL ;

ALTER TABLE `billpaytransfers` 
CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NULL ;

ALTER TABLE `p2ptransfers` 
CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NULL ;

ALTER TABLE `internaltransfers` 
CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NULL ;

ALTER TABLE `externaltransfers` 
CHANGE COLUMN `frequencyTypeId` `frequencyTypeId` VARCHAR(50) NULL ;

ALTER TABLE `wiretransfers` 
DROP FOREIGN KEY `FK_wiretransfers_wirefileexecution_idx`;

ALTER TABLE `bbtransactiontype` 
	CHANGE COLUMN `TransactionType_id` `transactionType_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `TransactionTypeName` `transactionTypeName` VARCHAR(45) NOT NULL ;
	
ALTER TABLE `bbtemplatetype` 
	CHANGE COLUMN `TemplateType_id` `templateType_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `TemplateTypeName` `templateTypeName` VARCHAR(45) NOT NULL ;
	
ALTER TABLE `bbtemplaterequesttype` 
	DROP FOREIGN KEY `FK_bbtemplaterequesttype_TransactionType_id`;
ALTER TABLE `bbtemplaterequesttype` 
	CHANGE COLUMN `TemplateRequestType_id` `templateRequestType_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `TemplateRequestTypeName` `templateRequestTypeName` VARCHAR(45) NOT NULL ,
	CHANGE COLUMN `TransactionType_id` `transactionType_id` INT(11) NOT NULL ;

ALTER TABLE `bbtemplaterequesttype` 
	ADD CONSTRAINT `FK_bbtemplaterequesttype_TransactionType_id`
	  FOREIGN KEY (`transactionType_id`)
	  REFERENCES `bbtransactiontype` (`transactionType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

ALTER TABLE `bbtemplate`
	DROP FOREIGN KEY `FK_bbtemplate_Organization`,
	DROP FOREIGN KEY `FK_bbtemplate_TemplateRequest_Type`,
	DROP FOREIGN KEY `FK_bbtemplate_Template_Type`,
	DROP FOREIGN KEY `FK_bbtemplate_Transaction_Type`,
	DROP FOREIGN KEY `FK_bbtemplate_user_2`;
	
ALTER TABLE `bbtemplate`
	CHANGE COLUMN `Template_id` `templateId` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `TemplateName` `templateName` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateDescription` `templateDescription` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `DebitAccount` `fromAccount` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `EffectiveDate` `effectiveDate` DATE NULL DEFAULT NULL ,
	CHANGE COLUMN `MaxAmount` `maxAmount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `Request_id` `requestId` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `UpdatedBy` `updatedBy` VARCHAR(50) NULL DEFAULT NULL ,
	CHANGE COLUMN `Updatedts` `updatedts` TIMESTAMP NULL DEFAULT NULL ,
	CHANGE COLUMN `TransactionType_id` `transactionType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateType_id` `templateType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Company_id` `companyId` VARCHAR(50) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateRequestType_id` `templateRequestType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `ActedBy` `actedBy` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `TotalAmount` `totalAmount` DOUBLE NULL DEFAULT NULL ;
	
	
ALTER TABLE `bbtemplate`
	ADD CONSTRAINT `FK_bbtemplate_Organization` FOREIGN KEY (`companyId`)
		REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
		
	ADD CONSTRAINT `FK_bbtemplate_TemplateRequest_Type`
	  FOREIGN KEY (`templateRequestType_id`)
	  REFERENCES `bbtemplaterequesttype` (`templateRequestType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplate_Template_Type`
	  FOREIGN KEY (`templateType_id`)
	  REFERENCES `bbtemplatetype` (`templateType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplate_Transaction_Type`
	  FOREIGN KEY (`transactionType_id`)
	  REFERENCES `bbtransactiontype` (`transactionType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplate_user_2`
	  FOREIGN KEY (`updatedBy`)
	  REFERENCES `customer` (`id`);
	  
	  
ALTER TABLE `bbtemplaterecord` 
	DROP FOREIGN KEY `FK_bbtemplaterecord_Account_Type_id`,
	DROP FOREIGN KEY `FK_bbtemplaterecord_Tax_Type_id`,
	DROP FOREIGN KEY `FK_bbtemplaterecord_Template_Request_Type_id`,
	DROP FOREIGN KEY `FK_bbtemplaterecord_Template_id`;
	
ALTER TABLE `bbtemplaterecord` 
	CHANGE COLUMN `TemplateRecord_id` `templateRecord_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `Record_Name` `record_Name` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `ToAccountNumber` `toAccountNumber` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `ABATRCNumber` `abatrcNumber` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `Detail_id` `detail_id` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `Amount` `amount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `AdditionalInfo` `additionalInfo` VARCHAR(500) NULL DEFAULT NULL ,
	CHANGE COLUMN `EIN` `ein` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `IsZeroTaxDue` `isZeroTaxDue` TINYINT(4) NULL DEFAULT NULL ,
	CHANGE COLUMN `Template_id` `template_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TaxType_id` `taxType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateRequestType_id` `templateRequestType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `ToAccountType` `toAccountType` INT(2) NULL DEFAULT NULL ;
	
ALTER TABLE `bbtemplaterecord` 
	ADD CONSTRAINT `FK_bbtemplaterecord_Account_Type_id`
	  FOREIGN KEY (`toAccountType`)
	  REFERENCES `achaccountstype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplaterecord_Tax_Type_id`
	  FOREIGN KEY (`taxType_id`)
	  REFERENCES `bbtaxtype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplaterecord_Template_Request_Type_id`
	  FOREIGN KEY (`templateRequestType_id`)
	  REFERENCES `bbtemplaterequesttype` (`templateRequestType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplaterecord_Template_id`
	  FOREIGN KEY (`template_id`)
	  REFERENCES `bbtemplate` (`templateId`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `bbtemplatesubrecord` 
	DROP FOREIGN KEY `FK_bbtemplatesubrecord_Taxsub_Type_id`,
	DROP FOREIGN KEY `FK_bbtemplatesubrecord_TemplateRecord_id`;
	
ALTER TABLE `bbtemplatesubrecord` 
	CHANGE COLUMN `TemplateSubRecord_id` `templateSubRecord_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `Amount` `amount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateRecord_id` `templateRecord_id` INT(11) NOT NULL ,
	CHANGE COLUMN `TaxSubCategory_id` `taxSubCategory_id` INT(11) NOT NULL ;
	
ALTER TABLE `bbtemplatesubrecord` 
	ADD CONSTRAINT `FK_bbtemplatesubrecord_Taxsub_Type_id`
	  FOREIGN KEY (`taxSubCategory_id`)
	  REFERENCES `bbtaxsubtype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	  
	ADD CONSTRAINT `FK_bbtemplatesubrecord_TemplateRecord_id`
	  FOREIGN KEY (`templateRecord_id`)
	  REFERENCES `bbtemplaterecord` (`templateRecord_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `bbtransaction` 
	DROP FOREIGN KEY `FK_bbtransaction_Organization`,
	DROP FOREIGN KEY `FK_bbtransaction_TemplateRequest_Type`,
	DROP FOREIGN KEY `FK_bbtransaction_Template_Type`,
	DROP FOREIGN KEY `FK_bbtransaction_Transaction_Type`;
	
ALTER TABLE `bbtransaction` 
	CHANGE COLUMN `Transaction_id` `transaction_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `DebitAccount` `fromAccount` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `EffectiveDate` `effectiveDate` DATE NULL DEFAULT NULL ,
	CHANGE COLUMN `Request_id` `requestId` BIGINT(20) NULL DEFAULT NULL ,
	CHANGE COLUMN `MaxAmount` `maxAmount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `TransactionType_id` `transactionType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateType_id` `templateType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Company_id` `companyId` VARCHAR(50) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateRequestType_id` `templateRequestType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateName` `templateName` VARCHAR(45) NULL DEFAULT 'No Template Used' ,
	CHANGE COLUMN `ConfirmationNumber` `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `ActedBy` `actedBy` VARCHAR(50) NULL DEFAULT NULL ,
	CHANGE COLUMN `Template_id` `template_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TotalAmount` `totalAmount` DOUBLE NULL DEFAULT NULL ;
	
ALTER TABLE `bbtransaction` 
	ADD CONSTRAINT `FK_bbtransaction_Organization`
	  FOREIGN KEY (`companyId`)
	  REFERENCES `organisation` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransaction_TemplateRequest_Type`
	  FOREIGN KEY (`templateRequestType_id`)
	  REFERENCES `bbtemplaterequesttype` (`templateRequestType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransaction_Template_Type`
	  FOREIGN KEY (`templateType_id`)
	  REFERENCES `bbtemplatetype` (`templateType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransaction_Transaction_Type`
	  FOREIGN KEY (`transactionType_id`)
	  REFERENCES `bbtransactiontype` (`transactionType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `bbtransaction` 
	RENAME TO  `achtransaction` ;
	
ALTER TABLE `bbtransactionrecord` 
	DROP FOREIGN KEY `FK_bbtransactionrecord_Achaccount_Type`,
	DROP FOREIGN KEY `FK_bbtransactionrecord_Tax_Type`,
	DROP FOREIGN KEY `FK_bbtransactionrecord_TemplateRequest_Type`,
	DROP FOREIGN KEY `FK_bbtransactionrecord_TransactionId`;
	
ALTER TABLE `bbtransactionrecord` 
	CHANGE COLUMN `TransactionRecord_id` `transactionRecord_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `ToAccountNumber` `toAccountNumber` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `ToAccountType` `toAccountType` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `ABATRCNumber` `abatrcNumber` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `Detail_id` `detail_id` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `Amount` `amount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `AdditionalInfo` `additionalInfo` VARCHAR(500) NULL DEFAULT NULL ,
	CHANGE COLUMN `EIN` `eIN` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `IsZeroTaxDue` `isZeroTaxDue` TINYINT(4) NULL DEFAULT NULL ,
	CHANGE COLUMN `TaxType_id` `taxType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Transaction_id` `transaction_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `TemplateRequestType_id` `templateRequestType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Record_Name` `record_Name` VARCHAR(45) NULL DEFAULT NULL ;
	
ALTER TABLE `bbtransactionrecord` 
	ADD CONSTRAINT `FK_bbtransactionrecord_Achaccount_Type`
	  FOREIGN KEY (`toAccountType`)
	  REFERENCES `achaccountstype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransactionrecord_Tax_Type`
	  FOREIGN KEY (`taxType_id`)
	  REFERENCES `bbtaxtype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransactionrecord_TemplateRequest_Type`
	  FOREIGN KEY (`templateRequestType_id`)
	  REFERENCES `bbtemplaterequesttype` (`templateRequestType_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransactionrecord_TransactionId`
	  FOREIGN KEY (`transaction_id`)
	  REFERENCES `achtransaction` (`transaction_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;
	  
	  
ALTER TABLE `bbtransactionrecord` 
	RENAME TO  `achtransactionrecord` ;
	
ALTER TABLE `bbtransactionsubrecord` 
	DROP FOREIGN KEY `FK_bbtransactionsubrecord_Taxsub_Type_id`,
	DROP FOREIGN KEY `FK_bbtransactionsubrecord_TransactionRecord_id`;
	
ALTER TABLE `bbtransactionsubrecord` 
	CHANGE COLUMN `TranscationSubRecord_id` `transcationSubRecord_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `Amount` `amount` DOUBLE NOT NULL ,
	CHANGE COLUMN `TransactionRecord_id` `transactionRecord_id` INT(11) NOT NULL ,
	CHANGE COLUMN `TaxSubCategory_id` `taxSubCategory_id` INT(11) NOT NULL ;
	
ALTER TABLE `bbtransactionsubrecord` 
	ADD CONSTRAINT `FK_bbtransactionsubrecord_Taxsub_Type_id`
	  FOREIGN KEY (`taxSubCategory_id`)
	  REFERENCES `bbtaxsubtype` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_bbtransactionsubrecord_TransactionRecord_id`
	  FOREIGN KEY (`transactionRecord_id`)
	  REFERENCES `achtransactionrecord` (`transactionRecord_id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION;

ALTER TABLE `bbtransactionsubrecord` 
	RENAME TO  `achtransactionsubrecord` ;


ALTER TABLE `achfileformattype` 
	CHANGE COLUMN `FileType` `fileType` VARCHAR(45) NOT NULL ,
	CHANGE COLUMN `Fileextension` `fileextension` VARCHAR(10) NULL DEFAULT NULL ,
	CHANGE COLUMN `MIMEtype` `mimetype` VARCHAR(100) NULL DEFAULT NULL ;
	
ALTER TABLE `achfile` 
	DROP FOREIGN KEY `FK_achfile_Company_id`,
	DROP FOREIGN KEY `FK_achfile_achfileformattype`;
	
ALTER TABLE `achfile` 
	CHANGE COLUMN `ACHFile_id` `achFile_id` INT(11) NOT NULL AUTO_INCREMENT ,
	CHANGE COLUMN `ACHFileName` `achFileName` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `DebitAmount` `debitAmount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `RequestType` `requestType` VARCHAR(45) NULL DEFAULT NULL ,
	CHANGE COLUMN `NumberOfCredits` `numberOfCredits` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `NumberOfDebits` `numberOfDebits` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `NumberOfPrenotes` `numberOfPrenotes` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Request_id` `requestId` BIGINT(20) NULL DEFAULT NULL ,
	CHANGE COLUMN `Contents` `contents` BLOB NULL DEFAULT NULL ,
	CHANGE COLUMN `FileSize` `fileSize` FLOAT NOT NULL ,
	CHANGE COLUMN `CreditAmount` `creditAmount` DOUBLE NULL DEFAULT NULL ,
	CHANGE COLUMN `NumberOfRecords` `numberOfRecords` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `ACHFileFormatType_id` `achFileFormatType_id` INT(11) NULL DEFAULT NULL ,
	CHANGE COLUMN `Company_id` `companyId` VARCHAR(50) NULL DEFAULT NULL ,
	CHANGE COLUMN `ActedBy` `actedBy` VARCHAR(45) NULL DEFAULT NULL ;
	
ALTER TABLE `achfile` 
	ADD CONSTRAINT `FK_achfile_Company_id`
	  FOREIGN KEY (`companyId`)
	  REFERENCES `organisation` (`id`)
	  ON DELETE NO ACTION
	  ON UPDATE NO ACTION,
	ADD CONSTRAINT `FK_achfile_achfileformattype`
	  FOREIGN KEY (`achFileFormatType_id`)
	  REFERENCES `achfileformattype` (`id`);
	  
ALTER TABLE `achfile` 
	ADD COLUMN `featureActionId` VARCHAR(50) NULL AFTER `achFileName`;

ALTER TABLE `achfile` 
	ADD CONSTRAINT `FK_achfile_featureaction_idx`
  		FOREIGN KEY (`featureActionId`)
  		REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  		
ALTER TABLE `achtransaction` 
	ADD COLUMN `featureActionId` VARCHAR(50) NULL AFTER `totalAmount`;

ALTER TABLE `achtransaction` 
	ADD CONSTRAINT `FK_achtransaction_featureaction_idx`
  		FOREIGN KEY (`featureActionId`)
  		REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;
  		
ALTER TABLE `bbtemplate` 
	ADD COLUMN `featureActionId` VARCHAR(50) NULL AFTER `totalAmount`;
	
ALTER TABLE `bbtemplate` 
	ADD CONSTRAINT `FK_bbtemplate_featureaction_idx`
  		FOREIGN KEY (`featureActionId`)
  		REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION;

DROP PROCEDURE IF EXISTS `approvalrequest_counts_proc`;

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
	    SELECT 0 as count, 'GeneralTransactionsForMyApproval' as TransactionType
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
        
        SET @createApproveActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE");
        
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
										if(bbrequest.featureActionId like 'ACH%', 'ACHTransactionsForMyApproval', 'GeneralTransactionsForMyApproval') as TransactionType,
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
	
END $$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_wiretransfer_details_proc`;
DELIMITER $$
CREATE PROCEDURE  `fetch_wiretransfer_details_proc`(
in _transactionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _wireFileExecution_id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
	
	IF _transactionId is NULL THEN
		SET _transactionId = "";
	END IF;
    
    IF _wireFileExecution_id is NULL THEN
		SET _wireFileExecution_id = "";
	END IF;
	
	IF @companyId is NULL THEN   
		SET @companyId = "";
	END IF;
    
    SET @isSMEUser = if( @companyId = "" OR @companyId = NULL, 0, 1);
    
	SET @filterRetail = if(_transactionId != "", concat("transactionId = ", _transactionId , " AND createdby = ", _customerId), 
					if( _wireFileExecution_id != "", concat("wireFileExecution_id = ", _wireFileExecution_id , " AND createdby = ", _customerId), '')) ;
                    
	SET @filterSME = if(_transactionId != "", concat("transactionId = ", _transactionId , " AND companyId = ", @companyId), 
					if( _wireFileExecution_id != "", concat("wireFileExecution_id = ", _wireFileExecution_id , " AND companyId = ", @companyId), '')) ;
    
    SET @filter = if( @isSMEUser = 0, @filterRetail, @filterSME);
    
	set @select_statement = concat("SELECT 
		wiretransfers.transactionId,
		wiretransfers.featureActionId,
        wiretransfers.confirmationNumber,
        wiretransfers.companyId,
        wiretransfers.createdby,
        wiretransfers.requestId,
        wiretransfers.status,
        wiretransfers.onetime_id,
        wiretransfers.wireFileExecution_id,
        wiretransfers.notes,
        wiretransfers.amount,
        wiretransfers.fromAccountNumber,
        wiretransfers.payeeAccountNumber,
        wiretransfers.transactionType,
        wiretransfers.payeeId,
        wiretransfers.payeeCurrency,
        onetimepayee.payeeName,
		onetimepayee.payeeNickName,
		onetimepayee.payeeType,
		onetimepayee.wireAccountType,
		onetimepayee.swiftCode,
		onetimepayee.routingNumber,
		onetimepayee.zipCode,
		onetimepayee.cityName,
		onetimepayee.state,
		onetimepayee.country,
		onetimepayee.payeeAddressLine1,
		onetimepayee.payeeAddressLine2,
		onetimepayee.bankName,
		onetimepayee.internationalRoutingCode,
		onetimepayee.bankAddressLine1,
		onetimepayee.bankAddressLine2,
		onetimepayee.bankCity,
		onetimepayee.bankState,
		onetimepayee.bankZip
        
    FROM
        (`wiretransfers`
		LEFT JOIN `onetimepayee` ON (`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`))
		WHERE ", @filter);
        
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
     
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrix_default_create_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalmatrix_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE actionId varchar(255) DEFAULT "";
 DECLARE actionList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 
 DECLARE actions CURSOR 
		FOR (select id from featureaction where FIND_IN_SET(id,_actionIds) COLLATE utf8_general_ci);
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

OPEN actions; 
 getAction: LOOP
        FETCH actions INTO actionId;
        IF finished = 1 THEN 
            LEAVE getAction;
	    else
			INSERT INTO approvalmatrix (companyId, name, accountId, actionId, limitTypeId) VALUES
		    (_companyId, concat(actionId, "_", _accountId, "_", limitTypeId_1, "_", _companyId), _accountId, actionId, limitTypeId_1);
			INSERT INTO approvalmatrix (companyId, name, accountId, actionId, limitTypeId) VALUES
		    (_companyId, concat(actionId, "_", _accountId, "_", limitTypeId_2, "_", _companyId), _accountId,actionId,limitTypeId_2);
		    INSERT INTO approvalmatrix (companyId, name, accountId, actionId, limitTypeId) VALUES
		    (_companyId, concat(actionId, "_", _accountId, "_", limitTypeId_3, "_", _companyId), _accountId, actionId, limitTypeId_3);
			set actionList = CONCAT(actionId,",",actionList);
			ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;
  
SET actionList = (select SUBSTRING(actionList FROM 1 FOR (CHAR_LENGTH(actionList)-1)));
select actionList;

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrix_default_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalmatrix_default_delete_proc`(
IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
 IF _filterColumnName = "actionId" THEN
	 DELETE FROM approvalmatrix where companyId = _companyId and FIND_IN_SET(actionId,_filterColumnIds) COLLATE utf8_general_ci;
 ELSEIF _filterColumnName = "accountId" THEN
 	 DELETE FROM approvalmatrix where companyId = _companyId and FIND_IN_SET(accountId,_filterColumnIds) COLLATE utf8_general_ci;
 END IF;
 	
END $$
DELIMITER ;

ALTER TABLE `requestapprovalmatrix` DROP FOREIGN KEY `FK_requestapprovalmatrix_approvalmatrix_id`;
ALTER TABLE `requestapprovalmatrix` ADD CONSTRAINT `FK_requestapprovalmatrix_approvalmatrix_id` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrix` (`id`) ON UPDATE NO ACTION ON DELETE CASCADE;

ALTER TABLE `mfaserviceconfig` 
	ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
	CHANGE COLUMN `transactionType` `transactionType` VARCHAR(120) NULL DEFAULT NULL,
	DROP PRIMARY KEY,
	ADD PRIMARY KEY (`id`);
	
ALTER TABLE `internaltransfers` 
	RENAME TO  `ownaccounttransfers` ;

DROP TABLE IF EXISTS `intrabanktransfers`;
CREATE TABLE IF NOT EXISTS `intrabanktransfers` (
  `transactionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `featureActionId` varchar(50) NOT NULL,
  `transactionType` varchar(45) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `requestId` bigint(20) DEFAULT NULL,
  `frequencyTypeId` varchar(50) DEFAULT NULL,
  `onetime_id` bigint(20) DEFAULT NULL,
  `fromAccountNumber` varchar(50) NOT NULL,
  `toAccountNumber` varchar(50) DEFAULT NULL,
  `transactionCurrency` varchar(45) DEFAULT NULL,
  `fromAccountCurrency` varchar(50) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `numberOfRecurrences` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `confirmationNumber` varchar(45) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `transactionts` timestamp NULL DEFAULT NULL,
  `frequencyEndDate` timestamp NULL DEFAULT NULL,
  `toAccountCurrency` varchar(50) DEFAULT NULL,
  `scheduledDate` timestamp NULL DEFAULT NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transactionId`),
  KEY `FK_intrabanktransfers_requestId` (`requestId`),
  KEY `FK_intrabanktransfers_fromAccountNumber` (`fromAccountNumber`),
  KEY `FK_intrabanktransfers_scheduledDate` (`scheduledDate`),
  KEY `FK_intrabanktransfers_frequencyTypeId` (`frequencyTypeId`),
  KEY `FK_intrabanktransfers_companyId` (`companyId`),
  KEY `FK_intrabanktransfers_createdby` (`createdby`),
  KEY `FK_intrabanktransfers_status` (`status`),
  KEY `FK_intrabanktransfers_onetime_idx_idx` (`onetime_id`),
  KEY `FK_intrabanktransfers_featureaction_Idx_idx` (`featureActionId`),
  KEY `FK_intrabanktransfers_currency_transactionCurrency_Idx_idx` (`transactionCurrency`),
  KEY `FK_intrabanktransfers_currency_fromAccountCurrency_Idx_idx` (`fromAccountCurrency`),
  KEY `FK_intrabanktransfers_currency_toAccountCurrency_Idx_idx` (`toAccountCurrency`),
  CONSTRAINT `FK_intrabanktransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_intrabanktransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_intrabanktransfers_currency_fromAccountCurrency_Idx` FOREIGN KEY (`fromAccountCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_intrabanktransfers_currency_toAccountCurrency_Idx` FOREIGN KEY (`toAccountCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_intrabanktransfers_currency_transactionCurrency_Idx` FOREIGN KEY (`transactionCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_intrabanktransfers_featureaction_Idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;


ALTER TABLE `externaltransfers` 
	RENAME TO  `internationalfundtransfers` ;


DROP TABLE IF EXISTS `interbankfundtransfers`;

CREATE TABLE IF NOT EXISTS `interbankfundtransfers` (
  `transactionId` bigint(20) NOT NULL AUTO_INCREMENT,
  `featureActionId` varchar(50) NOT NULL,
  `transactionType` varchar(45) DEFAULT NULL,
  `companyId` varchar(50) DEFAULT NULL,
  `requestId` bigint(20) DEFAULT NULL,
  `frequencyTypeId` varchar(50) DEFAULT NULL,
  `fromAccountNumber` varchar(50) NOT NULL,
  `toAccountNumber` varchar(50) DEFAULT NULL,
  `amount` double DEFAULT NULL,
  `numberOfRecurrences` int(11) DEFAULT NULL,
  `status` varchar(50) DEFAULT NULL,
  `confirmationNumber` varchar(45) DEFAULT NULL,
  `notes` varchar(255) DEFAULT NULL,
  `transactionts` timestamp NULL DEFAULT NULL,
  `frequencyEndDate` timestamp NULL DEFAULT NULL,
  `transactionCurrency` varchar(50) DEFAULT NULL,
  `fromAccountCurrency` varchar(50) DEFAULT NULL,
  `scheduledDate` timestamp NULL DEFAULT NULL,
  `processingDate` VARCHAR(50) NULL DEFAULT NULL,
  `personId` VARCHAR(50) NULL DEFAULT NULL,
  `fromNickName` VARCHAR(50) NULL DEFAULT NULL,
  `fromAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `day1` VARCHAR(50) NULL DEFAULT NULL,
  `day2` VARCHAR(50) NULL DEFAULT NULL,
  `toAccountType` VARCHAR(50) NULL DEFAULT NULL,
  `payPersonName` VARCHAR(50) NULL DEFAULT NULL,
  `securityQuestion` VARCHAR(50) NULL DEFAULT NULL,
  `SecurityAnswer` VARCHAR(50) NULL DEFAULT NULL,
  `checkImageBack` VARCHAR(50) NULL DEFAULT NULL,
  `payeeName` VARCHAR(50) NULL DEFAULT NULL,
  `profileId` VARCHAR(50) NULL DEFAULT NULL,
  `cardNumber` VARCHAR(50) NULL DEFAULT NULL,
  `cardExpiry` VARCHAR(50) NULL DEFAULT NULL,
  `isScheduled` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`transactionId`),
  KEY `FK_interbankfundtransfers_requestId` (`requestId`),
  KEY `FK_interbankfundtransfers_fromAccountNumber` (`fromAccountNumber`),
  KEY `FK_interbankfundtransfers_scheduledDate` (`scheduledDate`),
  KEY `FK_interbankfundtransfers_frequencyTypeId` (`frequencyTypeId`),
  KEY `FK_interbankfundtransfers_companyId` (`companyId`),
  KEY `FK_interbankfundtransfers_createdby` (`createdby`),
  KEY `FK_interbankfundtransfers_status` (`status`),
  KEY `FK_interbankfundtransfers_featureaction_Idx_idx` (`featureActionId`),
  KEY `FK_interbankfundtransfers_currency_transactionCurrency_Idx_idx` (`transactionCurrency`),
  KEY `FK_interbankfundtransfers_currency_fromAccountCurrency_Idx_idx` (`fromAccountCurrency`),
  CONSTRAINT `FK_interbankfundtransfers_companyIdx` FOREIGN KEY (`companyId`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_interbankfundtransfers_createdby_idx` FOREIGN KEY (`createdby`) REFERENCES `customer` (`id`),
  CONSTRAINT `FK_interbankfundtransfers_currency_fromAccountCurrency_Idx` FOREIGN KEY (`fromAccountCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_interbankfundtransfers_currency_transactionCurrency_Idx` FOREIGN KEY (`transactionCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_interbankfundtransfers_featureaction_Idx` FOREIGN KEY (`featureActionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `customerapplication`;

CREATE TABLE `customerapplication` (
  `Customer_id` VARCHAR(50) NOT NULL,
  `ApplicationId` VARCHAR(50) NOT NULL,
  `ProductId` VARCHAR(50) NULL DEFAULT NULL,
  `ApplicationStatus` VARCHAR(50) NULL DEFAULT NULL,
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Customer_id`, `ApplicationId`));

#ALTER TABLE `customerapplication` 
#COLLATE = DEFAULT ;

ALTER TABLE `customerapplication` 
ADD COLUMN `RequestKey` VARCHAR(45) NULL AFTER `ApplicationStatus`,
ADD COLUMN `JSESSIONID` VARCHAR(45) NULL AFTER `RequestKey`,
ADD COLUMN `SaveChallengeAnswer` VARCHAR(45) NULL AFTER `JSESSIONID`;

ALTER TABLE `customerapplication` 
CHANGE COLUMN `Customer_id` `Customer_id` VARCHAR(50) CHARACTER SET 'utf8' NULL ,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`ApplicationId`);

ALTER TABLE `customerapplication` CHANGE COLUMN `ProductId` `ProductId` VARCHAR(200) CHARACTER SET 'utf8' COLLATE 'utf8_unicode_ci' NULL DEFAULT NULL ;

ALTER TABLE `bbtemplate` 
	CHANGE COLUMN `createdts` `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ,
	CHANGE COLUMN `updatedts` `updatedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ;
	
ALTER TABLE `bbtemplaterecord` 
	CHANGE COLUMN `softDelete` `softDelete` INT(1) NULL DEFAULT 0 ;

ALTER TABLE `requestapprovalmatrix`
	DROP FOREIGN KEY `FK_requestapprovalmatrix_bbrequest`;
ALTER TABLE `requestapprovalmatrix`
	ADD CONSTRAINT `FK_requestapprovalmatrix_bbrequest`
	  FOREIGN KEY (`requestId`)
	  REFERENCES `bbrequest` (`requestId`)
	  ON DELETE CASCADE
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `achfile` 
	CHANGE COLUMN `createdts` `createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ;
	
ALTER TABLE `achfile` 
	ADD COLUMN `confirmationNumber` VARCHAR(45) NULL DEFAULT NULL AFTER `updatedts`;
	
DROP PROCEDURE IF EXISTS `approvalmatrix_create_proc`;

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
				set @query = concat('INSERT INTO approvalmatrix(name,companyId,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;
				
				SET @id = LAST_INSERT_ID();
				set @customerIds = SUBSTRING_INDEX( SUBSTRING_INDEX(_approverIds, ',', index1), ',', -1 );
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
				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `ach_template_record_subrecord_create_proc`;

DELIMITER $$
CREATE PROCEDURE `ach_template_record_subrecord_create_proc`(
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
			SET @query = CONCAT('INSERT INTO bbtemplaterecord(record_Name,toAccountNumber,abatrcNumber,detail_id,amount,additionalInfo,ein,isZeroTaxDue,template_id,taxType_id,templateRequestType_id,toAccountType) VALUES (',@recordsData,');');
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
							set @query = concat('INSERT INTO bbtemplatesubrecord(amount,taxSubCategory_id,templateRecord_id) VALUES (',@subRecordData,',',@id,');');
							PREPARE sql_query FROM @query;
							EXECUTE sql_query;
						SET @subRecordIndex = @subRecordIndex + 1;
						END IF;
					END LOOP createSubRecords;
				END IF;
			END IF;
	    END IF;
	END LOOP insertRecords;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrix_fetch_records_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_fetch_records_proc`(
    IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN
    IF _accountId = "" THEN
    SET _accountId = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SELECT 
    	
      	`approvalMatrix`.`id`,
        `approvalMatrix`.`companyId`,
         `approvalMatrix`.`accountId`,
         `approvalMatrix`.`limitTypeId`,
         `featureAction`.`id` AS `actionId`,
         `featureAction`.`name` AS `actionName`,
         `featureAction`.`description` AS `actionDescription`,
         `featureAction`.`Feature_id` AS `featureId`,
         `feature`.`name` AS `featureName`,
         `feature`.`Status_id` AS `fifeaturestatus`,
         `organisationfeatures`.`featureStatus` AS `orgfeaturestatus`,
         `approvalRule`.`id` AS `approvalruleId`,
         `approvalRule`.`numberOfApprovals`,
         `approvalRule`.`name` AS `approvalRuleName`,
         `approvalMatrix`.`lowerlimit`,
         `approvalMatrix`.`upperlimit`,
         `customer`.`id` AS `customerId`,
         `customer`.`FirstName` AS `firstName`,
         `customer`.`LastName` AS `lastName`
        FROM ((((((`approvalmatrix` AS `approvalMatrix`
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
        `organisationfeatures` AS `organisationfeatures`
        ON `feature`.`id` = `organisationfeatures`.`featureId`
          and  `approvalMatrix`.`companyId` = `organisationfeatures`.`organisationId`)
    WHERE 
    `approvalMatrix`.`companyId` = `_companyId`  AND 
    `approvalMatrix`.`accountId` LIKE `_accountId` AND 
    FIND_IN_SET(`approvalMatrix`.`actionId`,`_actions`) > 0 AND 
    `approvalMatrix`.`limitTypeId` LIKE `_limitTypeId` AND 
    `approvalMatrix`.`softdeleteflag` = 0
        ORDER BY `approvalMatrix`.`companyId`,`approvalMatrix`.`accountId`,`approvalMatrix`.`limitTypeId`,`approvalMatrix`.`actionId` ,`approvalMatrix`.`lowerlimit`;
END $$
DELIMITER ;	

DROP PROCEDURE IF EXISTS `fetch_achtransaction_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_achtransaction_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByTransactionType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN

		SET SESSION group_concat_max_len = 100000000;
		
        SET _transactionId = if(_transactionId = "" OR _transactionId = NULL, '%', _transactionId);
        SET _filterByTransactionType = if(_filterByTransactionType = NULL OR _filterByTransactionType ="", '%', _filterByTransactionType);
        
	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
	    
	    IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
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
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND  `achtransaction`.`createdby` = ", _customerId , " AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achtransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achtransaction`.`status` = 'Pending' "),
                                        if(_queryType = 'rejected', concat(" AND `achtransaction`.`status` = 'Rejected' "),
                                        '')));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", '', 
								concat(" AND (`achtransaction`.`templateName` LIKE '%",_searchString,"%' OR `customeraccounts`.`AccountName` LIKE '%",_searchString,"%' OR `bbtemplaterequesttype`.`templateRequestTypeName` LIKE '%",_searchString,"%' )"));
        
        SET @searchQuery = concat(@searchQuery, " AND (`bbtransactiontype`.`transactionTypeName` LIKE '",_filterByTransactionType,"' )");
        
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
        
	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts where Customer_id = _customerId);
	    
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
			`customeraccounts`.`AccountName` AS `accountName`,
			`customer`.`UserName` AS `userName`,
			`bbtransactiontype`.`transactionTypeName` AS `transactionTypeName`,
			`bbtemplatetype`.`templateTypeName` AS `templateTypeName`,
			`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`,
			`organisation`.`Name` AS `companyName`,
			`bbrequest`.`createdby` AS `requestCreatedby`
		FROM
			(((((((`achtransaction`
			LEFT JOIN `customer` ON (`achtransaction`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`achtransaction`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`achtransaction`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`achtransaction`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`achtransaction`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`achtransaction`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
			LEFT JOIN `organisation` ON (`achtransaction`.`companyId` = `organisation`.`id`))
			LEFT JOIN `bbrequest` ON (`achtransaction`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achtransaction`.`softDelete` = '0'
				AND `achtransaction`.`companyId` = '", @companyId ,"'
				AND `achtransaction`.`transaction_id` LIKE '",_transactionId ,"'
				AND FIND_IN_SET(`achtransaction`.`fromAccount`, \"", @customerAcounts, "\")
                AND FIND_IN_SET(`achtransaction`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					SUM(
						CASE requestapprovalmatrix.receivedApprovals
							WHEN NULL OR \"\" THEN 0
							ELSE requestapprovalmatrix.receivedApprovals
						END
					) as receivedApprovals,
					SUM(
						CASE approvalrule.numberOfApprovals
							WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
							WHEN NULL OR \"\" THEN 0
							ELSE approvalrule.numberOfApprovals
						END
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

DROP PROCEDURE IF EXISTS `fetch_achfiles_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_achfiles_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _achFile_id VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByStatus VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN

		SET SESSION group_concat_max_len = 100000000;
		
        SET _achFile_id = if(_achFile_id = "" OR _achFile_id = NULL, '%', _achFile_id);
        SET _filterByStatus = if(_filterByStatus = NULL OR _filterByStatus ="", '%', _filterByStatus);
        
	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
        
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
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
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND  `achfile`.`createdby` = ", _customerId, " AND `achfile`.`status` = 'Pending' "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achfile`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achfile`.`status` = 'Pending' "),
                                        if(_queryType = 'rejected', concat("AND `achfile`.`status` = 'Rejected' "),
                                        '')));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", '', 
				concat("AND (`achfile`.`achFileName` LIKE '%",_searchString,"%' OR `achfile`.`requestType` LIKE '%",_searchString,"%')"));
        SET @searchQuery = concat(@searchQuery, " AND (`achfile`.`status` LIKE '",_filterByStatus,"' )");
        
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
        
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
			`achfile`.`achFile_id` AS `achFile_id`,
			`achfile`.`achFileName` AS `achFileName`,
			`achfile`.`featureActionId` AS `featureActionId`,
			`achfile`.`debitAmount` AS `debitAmount`,
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
			`bbrequest`.`createdby` AS `requestCreatedby`
		FROM
			((((`achfile`
			LEFT JOIN `customer` ON (`achfile`.`createdby` = `customer`.`id`))
			LEFT JOIN `achfileformattype` ON (`achfile`.`achFileFormatType_id` = `achfileformattype`.`id`))
			LEFT JOIN `organisation` ON (`achfile`.`companyId` = `organisation`.`id`))
			LEFT JOIN `bbrequest` ON (`achfile`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achfile`.`softDelete` = '0'
				AND `achfile`.`companyId` = '", @companyId ,"'
				AND `achfile`.`achFile_id` LIKE '",_achFile_id ,"'
                AND FIND_IN_SET(`achfile`.`featureActionId`, \"", _featureactionlist, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					SUM(
						CASE requestapprovalmatrix.receivedApprovals
							WHEN NULL OR \"\" THEN 0
							ELSE requestapprovalmatrix.receivedApprovals
						END
					) as receivedApprovals,
					SUM(
						CASE approvalrule.numberOfApprovals
							WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
							WHEN NULL OR \"\" THEN 0
							ELSE approvalrule.numberOfApprovals
						END
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
    IN _filterByTransactionType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
	
		SET SESSION group_concat_max_len = 100000000;
		
        SET _templateId = if(_templateId = "" OR _templateId = NULL, '%', _templateId);
        SET _filterByTransactionType = if(_filterByTransactionType = NULL OR _filterByTransactionType ="", '%', _filterByTransactionType);
	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
        
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
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
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND  `bbtemplate`.`createdby` = ", _customerId , " AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected') "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`bbtemplate`.`requestId`,  \"",@approvalRequestIds,"\")  AND `bbtemplate`.`status` = 'Pending' "),
                                        ''));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", '', 
								concat(" AND (`bbtemplate`.`templateName` LIKE '%",_searchString,"%' OR `customeraccounts`.`AccountName` LIKE '%",_searchString,"%' OR `bbtemplaterequesttype`.`templateRequestTypeName` LIKE '%",_searchString,"%' )"));
        
        SET @searchQuery = concat(@searchQuery, " AND (`bbtransactiontype`.`transactionTypeName` LIKE '",_filterByTransactionType,"' )");
        
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
        
        SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts where Customer_id = _customerId);
	    
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
			`customeraccounts`.`AccountName` AS `accountName`,
			`customer`.`UserName` AS `userName`,
			`bbtransactiontype`.`transactionTypeName` AS `transactionTypeName`,
			`bbtemplatetype`.`templateTypeName` AS `templateTypeName`,
			`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`,
			`organisation`.`Name` AS `companyName`,
			`bbrequest`.`createdby` AS `requestCreatedby`
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
				AND `bbtemplate`.`companyId` = '", @companyId ,"'
				AND `bbtemplate`.`templateId` LIKE '",_templateId ,"'
				AND FIND_IN_SET(`bbtemplate`.`fromAccount`, \"", @customerAcounts, "\")
                AND FIND_IN_SET(`bbtemplate`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            ( 
				SELECT 
					bbrequest.requestId,
					SUM(
						CASE requestapprovalmatrix.receivedApprovals
							WHEN NULL OR \"\" THEN 0
							ELSE requestapprovalmatrix.receivedApprovals
						END
					) as receivedApprovals,
					SUM(
						CASE approvalrule.numberOfApprovals
							WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
							WHEN NULL OR \"\" THEN 0
							ELSE approvalrule.numberOfApprovals
						END
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

DROP PROCEDURE IF EXISTS `ach_transaction_record_subrecord_create_proc`;

DELIMITER $$
CREATE PROCEDURE `ach_transaction_record_subrecord_create_proc`(
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
			SET @query = CONCAT('INSERT INTO achtransactionrecord(record_Name,toAccountNumber,abatrcNumber,detail_id,amount,additionalInfo,eIN,isZeroTaxDue,transaction_id,taxType_id,templateRequestType_id,toAccountType) VALUES (',@recordsData,');');
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
							set @query = concat('INSERT INTO achtransactionsubrecord(amount,taxSubCategory_id,transactionRecord_id) VALUES (',@subRecordData,',',@id,');');
							PREPARE sql_query FROM @query;
							EXECUTE sql_query;
						SET @subRecordIndex = @subRecordIndex + 1;
						END IF;
					END LOOP createSubRecords;
                END IF;
			END IF;
	    END IF;
	END LOOP insertRecords;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrix_update_softdeleteflag_proc`;

DELIMITER $$
CREATE PROCEDURE `approvalmatrix_update_softdeleteflag_proc`(
	IN _companyId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
 UPDATE approvalmatrix SET softdeleteflag = 1 WHERE companyId = _companyId AND accountId = _accountId AND actionId = _actionId AND limitTypeId = _limitTypeId COLLATE utf8_general_ci;

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_achtemplaterecords_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_achtemplaterecords_proc`(
  IN _templateId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
	SELECT 
		`bbtemplaterecord`.`templateRecord_id` as `templateRecord_id`,
		`bbtemplaterecord`.`record_Name` AS `record_Name`,
		`bbtemplaterecord`.`toAccountNumber` AS `toAccountNumber`,
		`bbtemplaterecord`.`abatrcNumber` AS `abatrcNumber`,
		`bbtemplaterecord`.`detail_id` AS `detail_id`,
		`bbtemplaterecord`.`amount` AS `amount`,
		`bbtemplaterecord`.`additionalInfo` AS `additionalInfo`,
		`bbtemplaterecord`.`ein` AS `ein`,
		`bbtemplaterecord`.`isZeroTaxDue` AS `isZeroTaxDue`,
		`bbtemplaterecord`.`template_id` AS `template_id`,
		`bbtemplaterecord`.`taxType_id` AS `taxType_id`,
		`bbtemplaterecord`.`templateRequestType_id` AS `templateRequestType_id`,
		`bbtemplaterecord`.`softDelete` AS `softDelete`,
		`bbtemplaterecord`.`toAccountType` AS `toAccountType`,
		`bbtaxtype`.`taxType` AS `taxType`,
		`achaccountstype`.`accountType` AS `accountType`,
		`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`
	FROM 
    ((( `bbtemplaterecord`
    LEFT JOIN `bbtaxtype` ON (`bbtemplaterecord`.`taxType_id` = `bbtaxtype`.`id`))
    LEFT JOIN `achaccountstype` ON (`bbtemplaterecord`.`toAccountType` = `achaccountstype`.`id`))
    LEFT JOIN `bbtemplaterequesttype` ON (`bbtemplaterecord`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
    WHERE `bbtemplaterecord`.`softDelete` = '0'
    AND `bbtemplaterecord`.`template_id` = _templateId;
 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_request_history_proc` ;

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
        concat_ws(
			" ",
            IF(LENGTH(`customer`.`FirstName`),`customer`.`FirstName`,NULL),
            IF(LENGTH(`customer`.`MiddleName`),`customer`.`MiddleName`,NULL),
            IF(LENGTH(`customer`.`LastName`),`customer`.`LastName`,NULL)
		) AS `customerName`,
        `customer`.`FullName` AS `customerFullName`
	FROM 
    ( `bbactedrequest`
    LEFT JOIN `customer` ON (`bbactedrequest`.`createdby` = `customer`.`id`))
    WHERE `bbactedrequest`.`softdeleteflag` = '0'
    AND `bbactedrequest`.`requestId` = _requestId
    AND  `bbactedrequest`.`companyId` = @companyId;
 
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_achtemplatesubrecords_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_achtemplatesubrecords_proc`(
  IN _templateRecordId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SELECT 
		`bbtemplatesubrecord`.`templateSubRecord_id` AS `templateSubRecord_id`,
		`bbtemplatesubrecord`.`templateRecord_id` AS `templateRecord_id`,
		`bbtemplatesubrecord`.`taxSubCategory_id` AS `taxSubCategory_id`,
        `bbtemplatesubrecord`.`amount` AS `amount`,
        `bbtemplatesubrecord`.`softDelete` AS `softDelete`,
		`bbtaxsubtype`.`taxSubType` AS `taxSubType`
		 FROM
		(`bbtemplatesubrecord`
		LEFT JOIN `bbtaxsubtype` ON (`bbtemplatesubrecord`.`taxSubCategory_id` = `bbtaxsubtype`.`id`))
        WHERE `bbtemplatesubrecord`.`softDelete` = '0'
		AND `bbtemplatesubrecord`.`templateRecord_id` =  _templateRecordId;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS bbrequest_updatecounter_proc;
DELIMITER $$
CREATE PROCEDURE bbrequest_updatecounter_proc(
	IN _requestId BIGINT,
	IN `_counter` INT)
BEGIN
	SET _counter = if(_counter = NULL OR _counter = "" , 0 , _counter);
   UPDATE bbrequest
   SET `receivedSets` = `receivedSets` + _counter
  	WHERE `requestID` = `_requestId`;
  	
  	SELECT * FROM `bbrequest` WHERE `requestID` = `_requestId`;
END $$
DELIMITER ;	

DROP PROCEDURE IF EXISTS bbrequest_updatestatus_proc;
DELIMITER $$
CREATE PROCEDURE bbrequest_updatestatus_proc(
	IN _requestId BIGINT, IN _status VARCHAR(50))
BEGIN
  	
  	UPDATE bbrequest
   SET `status` = `_status`
   WHERE	`requestID` = `_requestId`;
  	
  	SELECT * FROM `bbrequest` WHERE `requestID` = `_requestId`;
  	
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
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN

		SET SESSION group_concat_max_len = 100000000; 
		
        SET _transactionId = if(_transactionId = "" OR _transactionId = NULL, '%', _transactionId);
        SET _featureActionId = if(_transactionId = "%" , '%', _featureActionId);

	    SET @companyId = (SELECT Organization_Id FROM customer WHERE id =_customerId);
	    
	    IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
        
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
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND  `generaltransaction`.`createdby` = ", _customerId , " AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`generaltransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `generaltransaction`.`status` = 'Pending' "),
                                        if(_queryType = 'rejected', concat(" AND `generaltransaction`.`status` = 'Rejected' "),
                                        '')));
        -- select @queryTypecondition;
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", '', 
								concat(" AND (`generaltransaction`.`payeeId` LIKE '%",_searchString,"%' OR `customeraccounts`.`accountName` LIKE '%",_searchString,"%' OR `feature`.`name` LIKE '%",_searchString,"%' OR `customer`.`userName` LIKE '%",_searchString,"%' )"));
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
        
	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts where Customer_id = _customerId);
	    
        IF @customerAcounts is NULL THEN      
			SET @customerAcounts = "";
		END IF;
        
        SET @accountsQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND FIND_IN_SET(`generaltransaction`.`fromAccountNumber`, \"", @customerAcounts, "\") "));
        SET @companyQuery = if(@companyId = "" OR @companyId = NULL, '', concat(" AND `generaltransaction`.`companyId` = '", @companyId ,"' "));
        
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
						`customeraccounts`.`AccountName` AS `accountName`,
						`customer`.`UserName` AS `userName`,
						`organisation`.`Name` AS `companyName`,
						`bbrequest`.`createdby` AS `requestCreatedby`

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
					SUM(
						CASE requestapprovalmatrix.receivedApprovals
							WHEN NULL OR \"\" THEN 0
							ELSE requestapprovalmatrix.receivedApprovals
						END
					) as receivedApprovals,
					SUM(
						CASE approvalrule.numberOfApprovals
							WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
							WHEN NULL OR \"\" THEN 0
							ELSE approvalrule.numberOfApprovals
						END
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

DROP PROCEDURE IF EXISTS `updateRequestApprovalMatrix_proc`;

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
		
		UPDATE temp_request_table SET `receivedApprovals` = `receivedApprovals` + 1;
		
		SELECT COUNT(*) AS counter FROM temp_request_table WHERE receivedApprovals >= numberOfApprovals;
	END IF;
	DROP TEMPORARY TABLE temp_request_table;

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_achtransactionrecords_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_achtransactionrecords_proc`(
  IN _transactionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 
	SELECT 
		`achtransactionrecord`.`transactionRecord_id` as `transactionRecord_id`,
        `achtransactionrecord`.`toAccountNumber` AS `toAccountNumber`,
        `achtransactionrecord`.`toAccountType` AS `toAccountType`,
		`achtransactionrecord`.`abatrcNumber` AS `abatrcNumber`,
		`achtransactionrecord`.`detail_id` AS `detail_id`,
		`achtransactionrecord`.`amount` AS `amount`,
		`achtransactionrecord`.`additionalInfo` AS `additionalInfo`,
		`achtransactionrecord`.`eIN` AS `eIN`,
		`achtransactionrecord`.`isZeroTaxDue` AS `isZeroTaxDue`,
        `achtransactionrecord`.`taxType_id` AS `taxType_id`,
		`achtransactionrecord`.`transaction_id` AS `transaction_id`,
        `achtransactionrecord`.`softDelete` AS `softDelete`,
		`achtransactionrecord`.`templateRequestType_id` AS `templateRequestType_id`,
        `achtransactionrecord`.`record_Name` AS `record_Name`,
		`bbtaxtype`.`taxType` AS `taxType`,
		`achaccountstype`.`accountType` AS `accountType`,
		`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`
	FROM 
    ((( `achtransactionrecord`
    LEFT JOIN `bbtaxtype` ON (`achtransactionrecord`.`taxType_id` = `bbtaxtype`.`id`))
    LEFT JOIN `achaccountstype` ON (`achtransactionrecord`.`toAccountType` = `achaccountstype`.`id`))
    LEFT JOIN `bbtemplaterequesttype` ON (`achtransactionrecord`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))
    WHERE `achtransactionrecord`.`softDelete` = '0'
    AND `achtransactionrecord`.`transaction_id` = _transactionId;
 
END $$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_achtransactionsubrecords_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_achtransactionsubrecords_proc`(
  IN _transactionRecordId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SELECT 
		`achtransactionsubrecord`.`transcationSubRecord_id` AS `transcationSubRecord_id`,
        `achtransactionsubrecord`.`amount` AS `amount`,
		`achtransactionsubrecord`.`transactionRecord_id` AS `transactionRecord_id`,
		`achtransactionsubrecord`.`taxSubCategory_id` AS `taxSubCategory_id`,
        `achtransactionsubrecord`.`softDelete` AS `softDelete`,
		`bbtaxsubtype`.`taxSubType` AS `taxSubType`
		 FROM
		(`achtransactionsubrecord`
		LEFT JOIN `bbtaxsubtype` ON (`achtransactionsubrecord`.`taxSubCategory_id` = `bbtaxsubtype`.`id`))
        WHERE `achtransactionsubrecord`.`softDelete` = '0'
		AND `achtransactionsubrecord`.`transactionRecord_id` =  _transactionRecordId;
END $$
DELIMITER ;

ALTER TABLE `bbtemplaterecord` 
	DROP FOREIGN KEY `FK_bbtemplaterecord_Template_id`;

ALTER TABLE `bbtemplaterecord` 
	ADD CONSTRAINT `FK_bbtemplaterecord_Template_id`
	  FOREIGN KEY (`template_id`)
	  REFERENCES `bbtemplate` (`templateId`)
	  ON DELETE CASCADE
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `bbtemplatesubrecord` 
	DROP FOREIGN KEY `FK_bbtemplatesubrecord_TemplateRecord_id`;
	
ALTER TABLE `bbtemplatesubrecord` 
	ADD INDEX `FK_bbtemplatesubrecord_TemplateRecord_id_idx` (`templateRecord_id` ASC),
	DROP INDEX `FK_bbtemplatesubrecord_TemplateRecord_id_idx` ;

ALTER TABLE `bbtemplatesubrecord` 
	ADD CONSTRAINT `FK_bbtemplatesubrecord_TemplateRecord_id`
	  FOREIGN KEY (`templateRecord_id`)
	  REFERENCES `bbtemplaterecord` (`templateRecord_id`)
	  ON DELETE CASCADE
	  ON UPDATE NO ACTION;
	  
ALTER TABLE `achtransactionrecord` 
	DROP FOREIGN KEY `FK_bbtransactionrecord_TransactionId`;

ALTER TABLE `achtransactionrecord` 
	ADD CONSTRAINT `FK_bbtransactionrecord_TransactionId`
	  FOREIGN KEY (`transaction_id`)
	  REFERENCES `achtransaction` (`transaction_id`)
	  ON DELETE CASCADE
	  ON UPDATE NO ACTION;

ALTER TABLE `achtransactionsubrecord` 
	DROP FOREIGN KEY `FK_bbtransactionsubrecord_TransactionRecord_id`;
ALTER TABLE `achtransactionsubrecord` 
	ADD CONSTRAINT `FK_bbtransactionsubrecord_TransactionRecord_id`
	  FOREIGN KEY (`transactionRecord_id`)
	  REFERENCES `achtransactionrecord` (`transactionRecord_id`)
	  ON DELETE CASCADE
	  ON UPDATE NO ACTION;



DROP PROCEDURE IF EXISTS `campaign_c360_datacontextsandattributes_get_proc`;

DELIMITER $$

CREATE PROCEDURE `campaign_c360_datacontextsandattributes_get_proc`()
BEGIN
	SELECT 
		`model`.`id` AS `modelId`,
		`model`.`name` AS `modelName`,
		`model`.`endpoint_url` AS `endpoint`,
		`attr`.`id` AS `attributeid`,
		`attr`.`endpoint_attribute_id` AS `attributendpoint`,
		`attr`.`name` AS `attributename`,
		`attr`.`attributetype` AS `attributetype`,
		`attr`.`range` AS `range`,
		`attr`.`helptext` AS `helptext`,
		`attr`.`criterias` AS `criterias`,
		`attr`.`options` AS `options`
		 from attribute attr
		inner join modelattribute ma on attr.id = ma.attribute_id
		inner join model model on ma.model_id = model.id
		order by model.name,attr.name desc;
END $$
DELIMITER ;	

DROP PROCEDURE IF EXISTS `authorizationCheckForRejectAndWithdrawl_proc`;

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
	
	SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE");
	
	IF @createActions is NULL THEN      
		SET @createActions = "";
	END IF;  
	
	SELECT * FROM bbrequest WHERE requestId = _requestId AND companyId = _companyId AND FIND_IN_SET(featureActionId, @createActions);

END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `action_limits_update_proc`;
DELIMITER $$
CREATE PROCEDURE `action_limits_update_proc`(
  IN _action varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
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
  UPDATE groupactionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE groupactionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE groupactionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
  UPDATE organisationactionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE organisationactionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE organisationactionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'PRE_APPROVED_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'PRE_APPROVED_DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'PRE_APPROVED_WEEKLY_LIMIT' AND value > _weeklyLimit; 
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND value > _weeklyLimit;
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit; 
END$$
DELIMITER ;

CREATE TABLE `compositeaction` (
  `id` varchar(50) NOT NULL,
  `Permission_id` varchar(50) NOT NULL,
  `Name` varchar(50) DEFAULT NULL,
  `Description` varchar(300) DEFAULT NULL,
  `Action_id` varchar(255) DEFAULT NULL,
  `Feature_id` VARCHAR(255) DEFAULT NULL,
  `isEnabled` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_CompositeAction_PermissionId_idx` (`Permission_id`),
  KEY `FK_CompositeAction_Action_idx` (`Action_id`),
  KEY `FK_CompositeAction_Feature_idx` (`Feature_id`),  
  CONSTRAINT `FK_CompositeAction_Action` FOREIGN KEY (`Action_id`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_CompositeAction_Feature` FOREIGN KEY (`Feature_id`) REFERENCES `feature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,  
  CONSTRAINT `FK_CompositeAction_PermissionId` FOREIGN KEY (`Permission_id`) REFERENCES `permission` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `rolecompositeaction` (
  `Role_id` varchar(50) NOT NULL,
  `CompositeAction_id` varchar(50) NOT NULL,
  `isEnabled` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Role_id`,`CompositeAction_id`),
  KEY `FK_RoleCompositeAction_CompositeAction_idx` (`CompositeAction_id`),
  CONSTRAINT `FK_RoleCompositeAction_CompositeAction` FOREIGN KEY (`CompositeAction_id`) REFERENCES `compositeaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_RoleCompositeAction_Role` FOREIGN KEY (`Role_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `usercompositeaction` (
  `User_id` varchar(50) NOT NULL,
  `CompositeAction_id` varchar(50) NOT NULL,
  `isEnabled` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`User_id`,`CompositeAction_id`),
  KEY `FK_UserCompositeAction_CompositeAction_idx` (`CompositeAction_id`),
  CONSTRAINT `FK_UserCompositeAction_CompositeAction` FOREIGN KEY (`CompositeAction_id`) REFERENCES `compositeaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_UserCompositeAction_User` FOREIGN KEY (`User_id`) REFERENCES `systemuser` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE VIEW `composite_actions_view` AS select `compositeaction`.`id` AS `id`,if(isnull(`compositeaction`.`Action_id`),`compositeaction`.`Name`,`featureaction`.`name`) AS `Name`,if(isnull(`compositeaction`.`Action_id`),`compositeaction`.`Description`,`featureaction`.`description`) AS `Description`,`compositeaction`.`Action_id` AS `Action_id`,`compositeaction`.`Feature_id` AS `Feature_id`,`compositeaction`.`Permission_id` AS `Permission_id`,`compositeaction`.`isEnabled` AS `isEnabled`,`compositeaction`.`createdby` AS `createdby`,`compositeaction`.`modifiedby` AS `modifiedby`,`compositeaction`.`createdts` AS `createdts`,`compositeaction`.`lastmodifiedts` AS `lastmodifiedts`,`compositeaction`.`synctimestamp` AS `synctimestamp`,`compositeaction`.`softdeleteflag` AS `softdeleteflag` from (`compositeaction` left join `featureaction` on((`featureaction`.`id` = `compositeaction`.`Action_id`)));


ALTER TABLE `service_permission_mapper` 
	DROP INDEX `UNIQUE_service_permission_mapper`;
	
ALTER TABLE `service_permission_mapper` 
	DROP COLUMN `user_type`;

ALTER TABLE `service_permission_mapper` 
	ADD UNIQUE INDEX `UNIQUE_service_permission_mapper` (`service_name` ASC, `object_name` ASC, `operation` ASC);
	
ALTER TABLE `customerapplication` ADD COLUMN `Party_id` VARCHAR(50) NULL AFTER `Customer_id`, ADD COLUMN `CoreCustomer_id` VARCHAR(50) NULL AFTER `Party_id`;


DROP procedure IF EXISTS `archived_request_search_proc`;

DELIMITER $$
CREATE PROCEDURE `archived_request_search_proc`(
  in _dateInitialPoint char(50), 
  in _dateFinalPoint char(50), 
  in _requestStatusID char(50), 
  in _requestCategory char(50), 
  in _offset char(50), 
  in _sortCriteria char(50), 
  in _sortOrder char(50), 
  in _requestAssignedTo char(50), 
  in _searchKey char(50), 
  in _messageRepliedBy char(50), 
  in _recordsPerPage varchar(50),
  in _queryType char(50)
)
BEGIN 
SET 
  @selectClause := IF(
    IFNULL(_queryType, '') = "count", 
    'count(acr.id) AS cnt', 
    'acr.id AS customerrequest_id'
  );
SET 
  @stmt := CONCAT(
    'SELECT ', @selectClause, ' FROM archivedcustomerrequest acr '
  );
SET 
  @whereclause := ' WHERE true';
SET 
  @joinCustomer := 0;
IF _dateInitialPoint != '' 
AND _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) >= ", 
    quote(_dateInitialPoint), " 
    AND ", 
    " TIMESTAMP(DATE(acr.createdts)) <= ", quote(_dateFinalPoint)
  );
ELSEIF _dateInitialPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) = ", 
    quote(_dateInitialPoint)
  );
ELSEIF _dateFinalPoint != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND TIMESTAMP(DATE(acr.createdts)) = ", 
    quote(_dateFinalPoint)
  );
END IF;
IF _requestStatusID != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.Status_id IN 
    (
      ", 
    func_escape_input_for_in_operator(_requestStatusID), "
    )
    "
  );
END IF;
IF _requestCategory != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.RequestCategory_id = ", 
    quote(_requestCategory)
  );
END IF;
IF _requestAssignedTo != '' THEN 
SET 
  @whereclause = CONCAT(
    @whereclause, " 
    AND acr.AssignedTo = ", 
    quote(_requestAssignedTo)
  );
END IF;
IF _messageRepliedBy != '' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN archivedrequestmessage ON (acr.id = archivedrequestmessage.CustomerRequest_id) '
  );
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND archivedrequestmessage.RepliedBy_id = ", 
    quote(_messageRepliedBy)
  );
END IF;
IF _searchKey != '' THEN 
SET 
  @joinCustomer = 1;
SET _searchKey=CONCAT("%",_searchKey,"%");
SET 
  @whereclause = CONCAT(
    @whereclause, " 
  AND 
  (
    acr.Customer_id LIKE ", 
    quote(_searchKey), " OR acr.id LIKE ", 
    quote(_searchKey), " OR customer.UserName LIKE ", 
    quote(_searchKey), ")"
  );
END IF;
IF _queryType != 'count' THEN IF _sortCriteria = 'customer_Fullname' THEN 
SET 
  @joinCustomer = 1;
END IF;

SET @sortColumn='acr.lastmodifiedts';
IF( _sortCriteria = 'customerrequest_Customer_id') THEN
    SET @sortColumn = 'acr.Customer_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo') THEN
    SET @sortColumn = 'acr.AssignedTo';
ELSEIF( _sortCriteria = 'customerrequest_createdts') THEN
    SET @sortColumn = 'acr.createdts';
ELSEIF( _sortCriteria = 'customerrequest_RequestCategory_id') THEN
    SET @sortColumn = 'acr.RequestCategory_id';
ELSEIF( _sortCriteria = 'customer_Fullname') THEN
    SET @sortColumn = 'CONCAT(customer.FirstName,customer.LastName)';
ELSEIF( _sortCriteria = 'customerrequest_Status_id') THEN
    SET @sortColumn = 'acr.Status_id';
ELSEIF( _sortCriteria = 'customerrequest_AssignedTo_Name') THEN
    SET @sortColumn = 'CONCAT(systemuser.FirstName,systemuser.LastName)';
END IF;

SET 
  @whereclause = CONCAT(
    @whereclause, 
    " 
  ORDER BY
    ", 
    IF(
      @sortColumn = '', 'acr.lastmodifiedts', 
      @sortColumn
    ), 
    ' ', 
    IF(
      @sortColumn = '' 
      OR _sortOrder = '', 
      'DESC', 
      _sortOrder
    )
  );
SET 
  @whereclause = CONCAT(
    @whereclause, 
    " LIMIT ", 
    IF(_offset = '', '0', _offset),
    ',',
    IF(_recordsPerPage = '', '10', _recordsPerPage)
  );
END IF;
IF @joinCustomer = 1 THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN customer ON (acr.Customer_id = customer.id) '
  );
END IF;
IF _sortCriteria = 'customerrequest_AssignedTo_Name' THEN 
SET 
  @stmt = CONCAT(
    @stmt, 'LEFT JOIN systemuser ON (acr.AssignedTo = systemuser.id) '
  );
END IF;
SET 
  @stmt = CONCAT(@stmt, @whereclause);
PREPARE stmt 
FROM 
  @stmt;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

ALTER TABLE `interbankfundtransfers` 
	ADD COLUMN `iban` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `swiftCode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bicCode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankId` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `paidBy` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `paymentType` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeAmount` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressNickName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressLine1` VARCHAR(200) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryCity` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryZipcode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiarycountry` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`;
	
ALTER TABLE `internationalfundtransfers` 
	ADD COLUMN `iban` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `swiftCode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bicCode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankId` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `paidBy` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `paymentType` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeAmount` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressNickName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressLine1` VARCHAR(200) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryCity` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryZipcode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiarycountry` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`;

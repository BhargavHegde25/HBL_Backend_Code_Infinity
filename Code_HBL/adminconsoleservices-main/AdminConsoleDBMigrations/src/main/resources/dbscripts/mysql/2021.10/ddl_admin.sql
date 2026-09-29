CREATE TABLE `internalfeature` (
 `id` varchar(255) NOT NULL,
 `name` varchar(100) NOT NULL,
 `description` varchar(400) DEFAULT NULL,
 `Status_id` varchar(50) DEFAULT 'SID_FEATURE_ACTIVE',
 `DisplaySequence` int(11) DEFAULT NULL,
 `isPrimary` tinyint(1) NOT NULL DEFAULT '0',
 `createdby` varchar(50) DEFAULT NULL,
 `modifiedby` varchar(50) DEFAULT NULL,
 `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
 PRIMARY KEY (`id`),
 KEY `FK_feature_status_id_idx` (`Status_id`),
 CONSTRAINT `FK_internalfeature_status_id` FOREIGN KEY (`Status_id`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `internalfeatureaction` (
 `id` varchar(255) NOT NULL,
 `Feature_id` varchar(255) DEFAULT NULL,
 `name` varchar(100) DEFAULT NULL,
 `description` varchar(300) DEFAULT NULL,
 `isPrimary` tinyint(1) NOT NULL DEFAULT '0',
 `DisplaySequence` int(11) DEFAULT NULL,
 `dependency` varchar(255) DEFAULT NULL,
 `status` varchar(100) DEFAULT 'SID_ACTION_ACTIVE',
 `accesspolicyId` varchar(50) DEFAULT NULL,
 `approveFeatureAction` varchar(255) DEFAULT NULL,
 `isApprovalAction` tinyint(1) NOT NULL DEFAULT '0',
 `createdby` varchar(50) DEFAULT NULL,
 `modifiedby` varchar(50) DEFAULT NULL,
 `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
 PRIMARY KEY (`id`),
 KEY `FK_internalfeatureaction_feature_id_idx` (`Feature_id`),
 KEY `FK_internalfeatureaction_status` (`status`),
 KEY `FK_internalfeatureaction_accesspolicyId` (`accesspolicyId`),
 CONSTRAINT `FK_internalfeatureaction_feature_id` FOREIGN KEY (`Feature_id`) REFERENCES `internalfeature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT `FK_internalfeatureaction_accesspolicyId` FOREIGN KEY (`accesspolicyId`) REFERENCES `accesspolicy` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT `FK_internalfeatureaction_status` FOREIGN KEY (`status`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `internalfeaturedisplaynamedescription` (
 `Feature_id` varchar(255) NOT NULL,
 `Locale_id` varchar(50) NOT NULL,
 `displayName` text NOT NULL,
 `displayDescription` text NOT NULL,
 `createdby` varchar(50) DEFAULT NULL,
 `modifiedby` varchar(50) DEFAULT NULL,
 `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
 PRIMARY KEY (`Feature_id`,`Locale_id`),
 KEY `FK_internalfeaturedisplaynamedescription_locale_idx` (`Locale_id`),
 CONSTRAINT `FK_internalfeaturedisplaynamedescription_Feature_id` FOREIGN KEY (`Feature_id`) REFERENCES `internalfeature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT `FK_internalfeaturedisplaynamedescription_locale` FOREIGN KEY (`Locale_id`) REFERENCES `locale` (`Code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `internalactiondisplaynamedescription` (
 `Action_id` varchar(255) NOT NULL,
 `Locale_id` varchar(50) NOT NULL,
 `displayName` varchar(100) NOT NULL,
 `displayDescription` varchar(300) NOT NULL,
 `createdby` varchar(50) DEFAULT NULL,
 `modifiedby` varchar(50) DEFAULT NULL,
 `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
 PRIMARY KEY (`Action_id`,`Locale_id`),
 KEY `FK_internalactiondisplaynamedescription_Locale_id` (`Locale_id`),
 CONSTRAINT `FK_internalactiondisplaynamedescription_Action_id` FOREIGN KEY (`Action_id`) REFERENCES `internalfeatureaction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT `FK_internalactiondisplaynamedescription_Locale_id` FOREIGN KEY (`Locale_id`) REFERENCES `locale` (`Code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `internalactiondependentactions` (
 `actionId` varchar(255) NOT NULL,
 `dependentactionId` varchar(255) NOT NULL,
 `featureId` varchar(255) NOT NULL,
 `createdby` varchar(50) DEFAULT NULL,
 `modifiedby` varchar(50) DEFAULT NULL,
 `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
 PRIMARY KEY (`actionId`,`dependentactionId`),
 KEY `FK_internalactiondependentactions_dependentactionId` (`dependentactionId`),
 KEY `FK_internalactiondependentactions_feature` (`featureId`),
 CONSTRAINT `FK_internalactiondependentactions_actionId` FOREIGN KEY (`actionId`) REFERENCES `internalfeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT `FK_internalactiondependentactions_dependentactionId` FOREIGN KEY (`dependentactionId`) REFERENCES `internalfeatureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT `FK_internalactiondependentactions_feature` FOREIGN KEY (`featureId`) REFERENCES `internalfeature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP VIEW IF EXISTS `internalusers_features_view`;
CREATE  VIEW `internalusers_features_view` AS
    SELECT 
        `internalfeature`.`id` AS `id`,
        `internalfeature`.`name` AS `name`,
        `internalfeature`.`description` AS `description`,
        `internalfeature`.`Status_id` AS `Status_id`,
		`internalfeature`.`DisplaySequence` AS `displaySequence`,
		`internalfeature`.`isPrimary` AS `isPrimary`,
        `internalfeaturedisplaynamedescription`.`Locale_id` AS `languageId`,
        `internalfeaturedisplaynamedescription`.`displayName` AS `displayName`,
        `internalfeaturedisplaynamedescription`.`displayDescription` AS `displayDescription`,
        (SELECT 
                COUNT(DISTINCT `internalfeatureaction`.`id`)
            FROM
                `internalfeatureaction`
            WHERE
                (`internalfeature`.`id` = `internalfeatureaction`.`Feature_id`)) AS `numberOfActions`
    FROM
        (`internalfeature`
        LEFT JOIN `internalfeaturedisplaynamedescription` ON ((`internalfeaturedisplaynamedescription`.`Feature_id` = `internalfeature`.`id`)))
        ORDER BY `internalfeature`.`name`;


DROP VIEW IF EXISTS `internalusers_dependentactions_view`;
CREATE VIEW `internalusers_dependentactions_view` AS
    SELECT 
        `internalactiondependentactions`.`actionId` AS `actionId`,
        `internalactiondependentactions`.`dependentactionId` AS `dependentactionId`,
        `internalactiondependentactions`.`featureId` AS `featureId`,
		`internalfeatureaction`.`name` AS `actionName`,
        `internalfeature`.`name` AS `featureName`
    FROM
        ((`internalactiondependentactions`
        LEFT JOIN `internalfeatureaction` ON ((`internalactiondependentactions`.`dependentactionId` = `internalfeatureaction`.`id`)))
        LEFT JOIN `internalfeature` ON ((`internalactiondependentactions`.`featureId` = `internalfeature`.`id`)));

DROP VIEW IF EXISTS `internalusers_actions_view`;
CREATE VIEW `internalusers_actions_view` AS
    SELECT 
        `internalfeatureaction`.`id` AS `actionId`,
        `internalfeatureaction`.`Feature_id` AS `featureId`,
        `internalfeatureaction`.`name` AS `actionName`,
        `internalfeature`.`name` AS `featureName`,
        `internalfeature`.`Status_id` AS `featureStatus`,
        `internalfeature`.`description` AS `featureDescription`,
        `internalfeatureaction`.`description` AS `actionDescription`,
        `internalfeatureaction`.`isPrimary` AS `isPrimary`,
		`internalfeatureaction`.`accesspolicyId` AS `accesspolicyId`,
		`internalfeatureaction`.`status` AS `actionStatus`,
        `internalfeatureaction`.`DisplaySequence` AS `actionDisplaySequence`,
		`internalactiondisplaynamedescription`.`Locale_id` AS `localeId`,
        `internalactiondisplaynamedescription`.`displayName` AS `displayName`,
        `internalactiondisplaynamedescription`.`displayDescription` AS `displayDescription`,
		`internalusers_dependentactions_view`.`dependentactionId` AS `dependentactionId`,
        `internalusers_dependentactions_view`.`featureId` AS `dependentFeatureId`,
        `internalusers_dependentactions_view`.`actionName` AS `dependentActionName`,
        `internalusers_dependentactions_view`.`featureName` AS `dependentFeatureName`
		
    FROM
        ((((`internalfeatureaction`
        LEFT JOIN `internalfeature` ON ((`internalfeature`.`id` = `internalfeatureaction`.`Feature_id`)))
        LEFT JOIN `internalactiondisplaynamedescription` ON ((`internalactiondisplaynamedescription`.`Action_id` = `internalfeatureaction`.`id`)))
        LEFT JOIN `accesspolicy` ON ((`internalfeatureaction`.`accesspolicyId` = `accesspolicy`.`id`)))
        LEFT JOIN `internalusers_dependentactions_view` ON ((`internalfeatureaction`.`id` = `internalusers_dependentactions_view`.`actionId`)));

DROP VIEW IF EXISTS `internalactiondependency_view`;
CREATE  VIEW `internalactiondependency_view` AS
    SELECT 
        `internalactiondependentactions`.`dependentactionId` AS `actionName`,
        `internalactiondependentactions`.`actionId` AS `dependencyAction`,
        `internalactiondependentactions`.`featureId` AS `featureId`,
        `internalfeatureaction`.`status` AS `actionStatus`,
        `internalfeature`.`Status_id` AS `featureStatus`
    FROM
        ((`internalactiondependentactions`
        LEFT JOIN `internalfeatureaction` ON ((`internalactiondependentactions`.`dependentactionId` = `internalfeatureaction`.`id`)))
        LEFT JOIN `internalfeature` ON ((`internalactiondependentactions`.`featureId` = `internalfeature`.`id`)));

DROP TABLE IF EXISTS `internalpermission`;
CREATE TABLE `internalpermission` (
  `id` varchar(50) NOT NULL,
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
  PRIMARY KEY (`id`),
  KEY `IXFK_Internalpermission_Internalpermission` (`Parent_id`),
  KEY `IXFK_Internalpermission_Status` (`Status_id`),
  CONSTRAINT `FK_Internalpermission_Internalpermission` FOREIGN KEY (`Parent_id`) REFERENCES `internalpermission` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_Internalpermission_Status` FOREIGN KEY (`Status_id`) REFERENCES `status` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permissionlegalentity` (
  `permissionId` varchar(50) NOT NULL,
  `legalEntityId` varchar(50) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` varchar(50) DEFAULT NULL,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`permissionId`,`legalEntityId`),
  KEY `IXFK_permissionlegalentity_legalentity` (`legalEntityId`),
  KEY `IXFK_permissionlegalentity_permission` (`permissionId`),
  CONSTRAINT `FK_permissionlegalentity_permission` FOREIGN KEY (`permissionId`) REFERENCES `internalpermission` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `permissionaction` (
  `id` varchar(50) NOT NULL,
  `permissionId` varchar(50) NOT NULL,
  `actionId` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_permissionaction` (`permissionId`,`actionId`),
  KEY `IXFK_permissionaction_permission` (`permissionId`),
  KEY `IXFK_permissionaction_Action` (`actionId`),
  KEY `IDX_permissionaction_permission_actionid` (`permissionId`,`actionId`),
  CONSTRAINT `FK_permissionaction_Action` FOREIGN KEY (`actionId`) REFERENCES `internalfeatureaction` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_permissionaction_Permission` FOREIGN KEY (`permissionId`) REFERENCES `internalpermission` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `userlegalentity` (
  `userId` varchar(50) NOT NULL,
  `legalEntityId` varchar(50) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` varchar(50) DEFAULT NULL,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`,`legalEntityId`),
  KEY `IXFK_userlegalentity_legalentity` (`legalEntityId`),
  KEY `IXFK_userlegalentity_user` (`userId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `userbranch` (
  `userId` varchar(50) NOT NULL,
  `branchId` varchar(50) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` varchar(50) DEFAULT NULL,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`userId`,`branchId`),
  KEY `IXFK_userbranch_user` (`userId`),
  KEY `IXFK_userbranch_branch` (`branchId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP procedure IF EXISTS `internalpermissionaction_delete_proc`;

DELIMITER $$
CREATE PROCEDURE`internalpermissionaction_delete_proc`(
in _permissionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DELETE FROM `permissionaction` where permissionId = _permissionId;
END$$
DELIMITER ;


DROP procedure IF EXISTS `permissionlegalentity_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `permissionlegalentity_delete_proc`(
in _permissionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DELETE FROM `permissionlegalentity` where permissionId = _permissionId;
END$$
DELIMITER ;

DROP VIEW IF EXISTS `internalpermission_view`;
CREATE VIEW `internalpermission_view` AS
    SELECT 
        `internalpermission`.`id` AS `permissionId`,
        `internalpermission`.`Name` AS `permissionName`,
        `internalpermission`.`Description` AS `description`,
        `internalpermission`.`Status_id` AS `status`,
        COUNT(DISTINCT `internalfeature`.`id`) AS `numberOfFeatures`,
        GROUP_CONCAT(DISTINCT `permissionlegalentity`.`legalEntityId`
            SEPARATOR ',') AS `legalEntityIds`
    FROM
        ((((`internalpermission`
        LEFT JOIN `permissionaction` ON ((`permissionaction`.`permissionId` = `internalpermission`.`id`)))
        LEFT JOIN `permissionlegalentity` ON ((`permissionlegalentity`.`permissionId` = `internalpermission`.`id`)))
        LEFT JOIN `internalfeatureaction` ON ((`internalfeatureaction`.`id` = `permissionaction`.`actionId`)))
        LEFT JOIN `internalfeature` ON ((`internalfeature`.`id` = `internalfeatureaction`.`Feature_id`)))
    GROUP BY `internalpermission`.`id`;

DROP VIEW IF EXISTS `internalpermission_featureaction_view`;
CREATE VIEW `internalpermission_featureaction_view` AS
    SELECT 
        `ifa`.`Feature_id` AS `featureId`,
        `pa`.`actionId` AS `actionId`,
        `pa`.`permissionId` AS `permissionId`
    FROM
        ((`permissionaction` `pa`
        JOIN `internalfeatureaction` `ifa` ON (((`pa`.`actionId` = `ifa`.`id`)
            AND (`ifa`.`status` = 'SID_ACTION_ACTIVE'))))
        JOIN `internalfeature` `ifes` ON (((`ifes`.`id` = `ifa`.`Feature_id`)
            AND (`ifes`.`Status_id` = 'SID_FEATURE_ACTIVE'))));


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
 from ((((((`groupactionlimit` left join `membergroup` on((`membergroup`.`id` = `groupactionlimit`.`Group_id`))) left join `featureaction` on((`featureaction`.`id` = `groupactionlimit`.`Action_id`))) left join `feature` on((`feature`.`id` = `featureaction`.`Feature_id`))) left join `accesspolicy` on ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`))) left join `limitgroup` on ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)) left join `actionlevel` on ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)))) ORDER BY `feature`.`name`;

DROP PROCEDURE IF EXISTS `reports_messages_received`;
DELIMITER $$
CREATE PROCEDURE `reports_messages_received`(
  in from_date VARCHAR(19), 
  in _todate VARCHAR(19), 
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
    createdby not in 
    (
      select
        Username 
      from
        systemuser
    )
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
    quote(STR_TO_DATE(_todate, '%m/%d/%Y %H:%i:%s'))
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


DROP PROCEDURE IF EXISTS `reports_messages_sent`;
DELIMITER $$
CREATE PROCEDURE `reports_messages_sent`(
  in from_date VARCHAR(19), 
  in _todate VARCHAR(19), 
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
      createdby in 
      (
        select
          Username 
        from
          systemuser
      )
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
    quote(STR_TO_DATE(_todate, '%m/%d/%Y %H:%i:%s'))
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


DROP PROCEDURE IF EXISTS `reports_threads_averageage`;
DELIMITER $$
CREATE PROCEDURE `reports_threads_averageage`(
  in from_date VARCHAR(19), 
  in _todate VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
      select
        AVG(thread_life_records.thread_life) threads_averageage_count 
      from
        (
          select
            DATEDIFF(MAX(createdts), MIN(createdts)) thread_life 
          from
            requestmessage 
          where
            true ";
if from_date != '' 
and from_date != '' then 
set 
  @queryStatement = concat(
    @queryStatement, 
    " 
            and createdts >= ", 
    quote(STR_TO_DATE(from_date, '%m/%d/%Y %H:%i:%s')), 
    " and createdts <= ", 
    quote(STR_TO_DATE(_todate, '%m/%d/%Y %H:%i:%s'))

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
set 
  @queryStatement = concat(
    @queryStatement, " 
          group by
            CustomerRequest_id) thread_life_records"
  );
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `reports_threads_new`;
DELIMITER $$
CREATE PROCEDURE `reports_threads_new`(
  in from_date VARCHAR(19), 
  in _todate VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
            SELECT
              count(id) threads_new_count 
            from
              customerrequest 
            where
              Status_id = 'SID_OPEN'";
if from_date != '' 
and from_date != '' then 
set 
  @queryStatement = concat(
    @queryStatement, 
    " 
              and createdts >= ", 
    quote(STR_TO_DATE(from_date, '%m/%d/%Y %H:%i:%s')), 
    " and createdts <= ", 
    quote(STR_TO_DATE(_todate, '%m/%d/%Y %H:%i:%s'))
  );
end if;
if category_id != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
              and RequestCategory_id = ", 
    quote(category_id)
  );
end if;
if csr_name != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
              and AssignedTo = ", 
    quote(csr_name)
  );
end if;
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `reports_threads_resolved`;
DELIMITER $$
CREATE PROCEDURE `reports_threads_resolved`(
  in from_date VARCHAR(19), 
  in to_date VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
              SELECT
                count(id) threads_resolved_count 
              from
                customerrequest 
              where
                Status_id = 'SID_RESOLVED'";
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
                and RequestCategory_id = ", 
    quote(category_id)
  );
end if;
if csr_name != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
                and AssignedTo = ", 
    quote(csr_name)
  );
end if;
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;

CREATE TABLE `rolepermissionou` (
  `roleId` varchar(50) NOT NULL,
  `permissionId` varchar(50) NOT NULL,
  `ouId` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`roleId`,`permissionId`,`ouId`),
  KEY `IXFK_rolepermissionou_permission` (`permissionId`),
  KEY `IXFK_rolepermissionou_role` (`roleId`),
  KEY `IDX_rolepermissionou_ou` (`ouId`),
  CONSTRAINT `FK_rolepermissionou_Role` FOREIGN KEY (`roleId`) REFERENCES `role` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `FK_rolepermissionou_Permission` FOREIGN KEY (`permissionId`) REFERENCES `internalpermission` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP VIEW IF EXISTS `internalrole_view`;
CREATE  VIEW `internalrole_view` AS
    SELECT 
        `role`.`id` AS `roleId`,
        `role`.`Name` AS `roleName`,
        `role`.`Description` AS `description`,
        `role`.`Status_id` AS `status`,
        GROUP_CONCAT(DISTINCT `rolepermissionou`.`ouId`
            SEPARATOR ',') AS `ouIds`,
        GROUP_CONCAT(DISTINCT `rolepermissionou`.`permissionId`
            SEPARATOR ',') AS `permissionIds`,
        GROUP_CONCAT(DISTINCT `internalpermission`.`Name`
            SEPARATOR ',') AS `permissionNames`
    FROM
        ((`role`
        LEFT JOIN `rolepermissionou` ON ((`rolepermissionou`.`roleId` = `role`.`id`)))
        LEFT JOIN `internalpermission` ON ((`internalpermission`.`id` = `rolepermissionou`.`permissionId`)))
    GROUP BY `role`.`id`;


DROP procedure IF EXISTS `rolepermissionou_delete_proc`;
DELIMITER $$
CREATE PROCEDURE`rolepermissionou_delete_proc`(
in _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DELETE FROM rolepermissionou where rolepermissionou.roleId = _roleId;
END$$

DELIMITER ;

alter table `requestmessage` add column `isPriorityMessage` varchar(20) default '0';
 DROP procedure IF EXISTS `customer_request_message_search_proc`;
DELIMITER $$

CREATE  PROCEDURE `customer_request_message_search_proc`(
	IN _customerID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _customerName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _customerFirstName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _customerMiddleName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _customerLastName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _customerUsername varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _messageRepliedBy varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestSubject varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestAssignedTo varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _requestCategory varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _requestID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _requestStatusID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _dateInitialPoint varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _dateFinalPoint varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN
 SET @queryStatement = "
		SELECT 
        `customerrequest`.`id` AS `customerrequest_id`,
        `customerrequest`.`RequestCategory_id` AS `customerrequest_RequestCategory_id`,
        `customerrequest`.`lastupdatedbycustomer` AS `customerrequest_lastupdatedbycustomer`,
        `requestcategory`.`Name` AS `requestcategory_Name`,
        `customerrequest`.`Customer_id` AS `customerrequest_Customer_id`,
        `customer`.`FirstName` AS `customer_FirstName`,
        `customer`.`MiddleName` AS `customer_MiddleName`,
        CONCAT(`customer`.`FirstName`,
                `customer`.`LastName`) AS `customer_Fullname`,
        CONCAT(`systemuser`.`FirstName`,
                `systemuser`.`LastName`) AS `customerrequest_AssignedTo_Name`,
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
        `customerrequest`.`Priority` AS `customerrequest_Priority`,
        `customerrequest`.`Status_id` AS `customerrequest_Status_id`,
        `customerrequest`.`AssignedTo` AS `customerrequest_AssignedTo`,
        `customerrequest`.`RequestSubject` AS `customerrequest_RequestSubject`,
        `customerrequest`.`Accountid` AS `customerrequest_Accountid`,
        `customerrequest`.`createdby` AS `customerrequest_createdby`,
        `customerrequest`.`modifiedby` AS `customerrequest_modifiedby`,
        `customerrequest`.`createdts` AS `customerrequest_createdts`,
        `customerrequest`.`lastmodifiedts` AS `customerrequest_lastmodifiedts`,
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
        `requestmessage`.`lastmodifiedts` AS `requestmessage_lastmodifiedts`,
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
        `media`.`lastmodifiedts` AS `media_lastmodifiedts`,
        `media`.`synctimestamp` AS `media_synctimestamp`,
        `media`.`softdeleteflag` AS `media_softdeleteflag`
    FROM
        (`customerrequest`
		JOIN `requestmessage` ON (`customerrequest`.`id` = `requestmessage`.`CustomerRequest_id`)
		JOIN `customer` ON (`customerrequest`.`Customer_id` = `customer`.`id`)
		JOIN `requestcategory` ON (`customerrequest`.`RequestCategory_id` = `requestcategory`.`id`)
        LEFT JOIN `systemuser` ON (`customerrequest`.`AssignedTo` = `systemuser`.`id`)
        LEFT JOIN `messageattachment` ON (`requestmessage`.`id` = `messageattachment`.`RequestMessage_id`)
        LEFT JOIN `media` ON (`messageattachment`.`Media_id` = `media`.`id`))
	WHERE true ";

IF _customerID != '' THEN
		SET @queryStatement = concat(@queryStatement," and customer.id = ", quote(_customerID));
  END if;

  IF _customerFirstName != '' THEN
		SET @queryStatement = concat(@queryStatement," and customer.FirstName = ", quote(_customerFirstName));
  END if;

  IF _customerMiddleName != '' THEN
		SET @queryStatement = concat(@queryStatement," and customer.MiddleName = ", quote(_customerMiddleName));
  END if;

  IF _customerLastName != '' THEN
		SET @queryStatement = concat(@queryStatement," and customer.LastName = ", quote(_customerLastName));
  END if;

  IF _customerUsername != '' THEN
		SET @queryStatement = concat(@queryStatement," and customer.LastName = ", quote(_customerUsername));
  END if;

  IF _messageRepliedBy != '' THEN
		SET @queryStatement = concat(@queryStatement," and requestmessage.RepliedBy = ", quote(_messageRepliedBy));
  END if;

  IF _requestSubject != '' THEN
		SET @queryStatement = concat(@queryStatement," and customerrequest.RequestSubject = ", quote(_requestSubject));
  END if;

  IF _requestAssignedTo != '' THEN
		SET @queryStatement = concat(@queryStatement," and customerrequest.AssignedTo = ", quote(_requestAssignedTo));
  END if;

 IF _requestCategory != '' THEN
		SET @queryStatement = concat(@queryStatement," and customerrequest.RequestCategory_id = ", quote(_requestCategory));
  END if;

   IF _requestID != '' THEN
		SET @queryStatement = concat(@queryStatement," and customerrequest.id = ", quote(_requestID));
  END if;

   IF _requestStatusID != '' THEN
		SET @queryStatement = concat(@queryStatement," and customerrequest.Status_id = ", quote(_requestStatusID));
  END if;
  
   IF _customerName != '' THEN
		SET @queryStatement = concat(@queryStatement," and  CONCAT(`customer`.`FirstName`,`customer`.`LastName`).Status_id = ", quote(_customerName));
  END if;
  
IF  _dateInitialPoint != '' THEN
     IF LOCATE("=", _dateInitialPoint)!=0 THEN
    	SET @queryStatement = concat(@queryStatement," and requestmessage.createdts = ", quote(_dateInitialPoint));
     ELSEIF LOCATE(">", _dateInitialPoint)!=0 THEN
        SET @queryStatement = concat(@queryStatement," and requestmessage.createdts > ", quote(_dateInitialPoint));
     ELSEIF LOCATE("<", _dateInitialPoint)!=0 THEN
        SET @queryStatement = concat(@queryStatement," and requestmessage.createdts < ", quote(_dateInitialPoint));
	 ELSEIF _dateFinalPoint != '' THEN
        SET @queryStatement = concat(@queryStatement," and requestmessage.createdts > ", quote(_dateInitialPoint), " and requestmessage.createdts < ", quote(_dateFinalPoint));
	 END IF;	
 END IF;	
 		
 		SET @queryStatement = concat(@queryStatement," ORDER BY `requestmessage`.`ReplySequence` DESC");
		PREPARE stmt FROM @queryStatement;
		execute stmt;
		DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

DROP VIEW IF EXISTS `customer_request_detailed_view`;
CREATE VIEW `customer_request_detailed_view` AS
    SELECT 
        `customerrequest`.`id` AS `customerrequest_id`,
        `customerrequest`.`RequestCategory_id` AS `customerrequest_RequestCategory_id`,
        `customerrequest`.`lastupdatedbycustomer` AS `customerrequest_lastupdatedbycustomer`,
        `requestcategory`.`Name` AS `requestcategory_Name`,
        `customerrequest`.`Customer_id` AS `customerrequest_Customer_id`,
        `customer`.`FirstName` AS `customer_FirstName`,
        `customer`.`MiddleName` AS `customer_MiddleName`,
        CONCAT(`customer`.`FirstName`,
                ' ',
                `customer`.`LastName`) AS `customer_Fullname`,
        CONCAT(`systemuser`.`FirstName`,
                ' ',
                `systemuser`.`LastName`) AS `customerrequest_AssignedTo_Name`,
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
        `customerrequest`.`Priority` AS `customerrequest_Priority`,
        `customerrequest`.`Status_id` AS `customerrequest_Status_id`,
        `customerrequest`.`AssignedTo` AS `customerrequest_AssignedTo`,
        `customerrequest`.`RequestSubject` AS `customerrequest_RequestSubject`,
        `customerrequest`.`Accountid` AS `customerrequest_Accountid`,
        `customerrequest`.`createdby` AS `customerrequest_createdby`,
        `customerrequest`.`modifiedby` AS `customerrequest_modifiedby`,
        `customerrequest`.`createdts` AS `customerrequest_createdts`,
        `customerrequest`.`lastmodifiedts` AS `customerrequest_lastmodifiedts`,
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
        `requestmessage`.`lastmodifiedts` AS `requestmessage_lastmodifiedts`,
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
        `media`.`lastmodifiedts` AS `media_lastmodifiedts`,
        `media`.`synctimestamp` AS `media_synctimestamp`,
        `media`.`softdeleteflag` AS `media_softdeleteflag`
    FROM
        ((((((`customerrequest`
        JOIN `customer` ON ((`customerrequest`.`Customer_id` = `customer`.`id`)))
        JOIN `requestcategory` ON ((`customerrequest`.`RequestCategory_id` = `requestcategory`.`id`)))
        JOIN `requestmessage` ON ((`customerrequest`.`id` = `requestmessage`.`CustomerRequest_id`)))
        LEFT JOIN `systemuser` ON ((`customerrequest`.`AssignedTo` = `systemuser`.`id`)))
        LEFT JOIN `messageattachment` ON ((`requestmessage`.`id` = `messageattachment`.`RequestMessage_id`)))
        LEFT JOIN `media` ON ((`messageattachment`.`Media_id` = `media`.`id`)));



DROP procedure IF EXISTS `customer_unread_message_count_proc`;

DELIMITER $$

CREATE  PROCEDURE `customer_unread_message_count_proc`(
IN _customerId  varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
   SELECT 
        COUNT(`requestmessage`.`id`) AS `messageCount`
    FROM
        (`customerrequest`
        JOIN `requestmessage` ON ((`requestmessage`.`CustomerRequest_id` = `customerrequest`.`id`)))
    WHERE
        (`requestmessage`.`IsRead` = 'FALSE' and `customerrequest`.`softdeleteflag`= 0 and `customerrequest`.`Customer_id`= _customerId 
        and `customerrequest`.`Status_id` != 'SID_DELETED');
  SELECT 
        COUNT(`requestmessage`.`id`) AS `priorityMessageCount`
    FROM
        (`customerrequest`
        JOIN `requestmessage` ON ((`requestmessage`.`CustomerRequest_id` = `customerrequest`.`id`)))
    WHERE
        (`requestmessage`.`IsRead` = 'FALSE' and `customerrequest`.`softdeleteflag`= 0 and `customerrequest`.`Customer_id`= _customerId 
        and `customerrequest`.`Status_id` != 'SID_DELETED' and `requestmessage`.`isPriorityMessage`="1" );
END$$

DELIMITER ;

DROP VIEW IF EXISTS `customerrequests_view`;
CREATE VIEW `customerrequests_view` AS
    SELECT 
        `customerrequest`.`id` AS `id`,
        `customerrequest`.`softdeleteflag` AS `softdeleteflag`,
        `customerrequest`.`Priority` AS `priority`,
        `customerrequest`.`createdts` AS `requestCreatedDate`,
        MAX(`requestmessage`.`createdts`) AS `recentMsgDate`,
        `requestcategory`.`Name` AS `requestcategory_id`,
        `customerrequest`.`Customer_id` AS `customer_id`,
        `customer`.`UserName` AS `username`,
        (SELECT 
                `status`.`Description`
            FROM
                `status`
            WHERE
                (`status`.`id` = `customerrequest`.`Status_id`)
            LIMIT 1) AS `status_id`,
        `customerrequest`.`id` AS `statusIdentifier`,
        `customerrequest`.`RequestSubject` AS `requestsubject`,
        `customerrequest`.`Accountid` AS `accountid`,
        `requestmessage`.`isPriorityMessage` AS `isPriorityMessage`,
        COUNT((CASE
            WHEN (`requestmessage`.`isPriorityMessage` = '1') THEN 0
        END)) AS `priorityCount`,
        CONCAT(`systemuser`.`FirstName`,
                ' ',
                `systemuser`.`LastName`) AS `assignTo`,
        COUNT(`requestmessage`.`id`) AS `totalmsgs`,
        COUNT((CASE
            WHEN (`requestmessage`.`IsRead` = 'true') THEN 1
        END)) AS `readmsgs`,
        COUNT((CASE
            WHEN (`requestmessage`.`IsRead` = 'false') THEN 1
        END)) AS `unreadmsgs`,
        SUBSTRING_INDEX(GROUP_CONCAT(`requestmessage`.`MessageDescription`
                    ORDER BY `requestmessage`.`createdts` ASC , `requestmessage`.`id` ASC
                    SEPARATOR '||'),
                '||',
                1) AS `firstMessage`,
        GROUP_CONCAT(`requestmessage`.`id`
            SEPARATOR ',') AS `msgids`,
        COUNT(`messageattachment`.`id`) AS `totalAttachments`
    FROM
        (((((`customerrequest`
        LEFT JOIN `requestmessage` ON ((`customerrequest`.`id` = `requestmessage`.`CustomerRequest_id`)))
        LEFT JOIN `requestcategory` ON ((`requestcategory`.`id` = `customerrequest`.`RequestCategory_id`)))
        LEFT JOIN `customer` ON ((`customer`.`id` = `customerrequest`.`Customer_id`)))
        LEFT JOIN `messageattachment` ON ((`messageattachment`.`RequestMessage_id` = `requestmessage`.`id`)))
        LEFT JOIN `systemuser` ON ((`systemuser`.`id` = `customerrequest`.`AssignedTo`)))
    GROUP BY `customerrequest`.`id`
    ORDER BY `customerrequest`.`createdts` DESC , `customerrequest`.`id`;

	
	
DROP procedure IF EXISTS `servicedefinition_delete_proc`;
DELIMITER $$
CREATE  PROCEDURE `servicedefinition_delete_proc`(
	IN _servicedefinitionid VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DELETE FROM `groupservicedefinition` WHERE  serviceDefinitionId = _servicedefinitionid;
DELETE FROM `servicedefinitionactionlimit` WHERE serviceDefinitionId = _servicedefinitionid;
DELETE FROM `userroleservicedefinition`  WHERE serviceDefinitionId = _servicedefinitionid;
DELETE FROM `servicedefinition`  WHERE id = _servicedefinitionid;
END$$
DELIMITER ;



DROP VIEW IF EXISTS `feature_action_roles_view`;

CREATE 
VIEW `feature_action_roles_view` AS
    SELECT 
        `featureaction`.`Feature_id` AS `feature_code`,
        `feature_view`.`Name` AS `feature_name`,
        `feature_view`.`Type_Id` AS `feature_type_id`,
        `feature_view`.`Status_Id` AS `feature_status_id`,
        `featureaction`.`id` AS `action_code`,
        `featureaction`.`name` AS `action_name`,
        `featureaction`.`description` AS `action_description`,
        `far`.`RoleType_id` AS `action_role_type_id`,
        `featureaction`.`Type_id` AS `category`,
        `featureaction`.`accesspolicyId` AS `accesspolicyId`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
        `featureaction`.`status` AS `status`,
        `actionlimit`.`LimitType_id` AS `limitType_id`,
        `actionlimit`.`value` AS `value`
    FROM
        (((`featureaction`
        LEFT JOIN `featureactionroletype` `far` ON ((`far`.`Action_id` = `featureaction`.`id`)))
        LEFT JOIN `feature_view` ON ((`feature_view`.`Code` = `featureaction`.`Feature_id`)))
        LEFT JOIN `actionlimit` ON ((`actionlimit`.`Action_id` = `featureaction`.`id`)));
		
ALTER TABLE `cardaccountrequest` DROP FOREIGN KEY `FK_CardAccountRequest_CustomerCommunication`;
ALTER TABLE `cardaccountrequest` DROP INDEX `IXFK_CardAccountRequest_CustomerCommunication` ;

DROP procedure IF EXISTS `approval_matrix_manual_cleanup_proc`;

DELIMITER $$
CREATE PROCEDURE `approval_matrix_manual_cleanup_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

UPDATE approvalmatrix set softdeleteflag = '1' where coreCustomerId = _coreCustomerId;

SET @customerAccounts = (SELECT group_concat(distinct accountId SEPARATOR ",") from contractaccounts WHERE (contractId = _contractId AND coreCustomerId=_coreCustomerId));

SET @customerActions = (SELECT group_concat(distinct actionId SEPARATOR ",") from contractactionlimit LEFT JOIN featureaction ON (featureaction.id = contractactionlimit.actionId) WHERE (contractId = _contractId AND coreCustomerId=_coreCustomerId) AND featureaction.approveFeatureAction IS NOT NULL);

call approvalmatrix_default_create_proc(@customerActions, _contractId, @customerAccounts, _coreCustomerId);

END$$

DELIMITER ;

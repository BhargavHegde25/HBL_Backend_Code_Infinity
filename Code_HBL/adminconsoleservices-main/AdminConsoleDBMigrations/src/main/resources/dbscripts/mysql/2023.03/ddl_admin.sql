ALTER TABLE `dbxalertcategory` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `dbxalertcategorytext` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `dbxalerttype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `dbxalerttypetext` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alerttypechannel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtypetext` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtypeapp` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtypecustomertype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtypeaccounttype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertsubtypechannel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customeralertchannel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `notification` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `usernotification` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertcategorychannel` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `customeralertfrequency` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertattribute` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertattributelistvalues` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `alertrecipienttype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';

-- dbxdb.alertsubtypetext_view source

DROP VIEW IF EXISTS `alertsubtypetext_view`;

create or replace
algorithm = UNDEFINED view `alertsubtypetext_view` as
select
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
    `alertsubtype`.`isAutoSubscribeEnabled` as `alertsubtype_isAutoSubscribeEnabled`,
    `alertsubtypetext`.`languageCode` as `alertsubtypetext_languageCode`,
    `alertsubtypetext`.`description` as `alertsubtypetext_description`,
    `alertsubtype`.`externalSystem` as `alertsubtype_externalSystem`,
    `alertsubtypetext`.`displayName` as `alertsubtypetext_displayName`,
    `alertsubtype`.`companyLegalUnit` as `alertsubtype_companyLegalUnit`
from
    (`alertsubtype`
join `alertsubtypetext` on
    ((`alertsubtypetext`.`alertSubTypeId` = `alertsubtype`.`id`) and 
    (`alertsubtypetext`.`companyLegalUnit` = `alertsubtype`.`companyLegalUnit`)));


-- dbxdb.alerttype_view source
DROP VIEW IF EXISTS `alerttype_view`;

create or replace
algorithm = UNDEFINED view `alerttype_view` as
select
    `dbxalerttype`.`id` as `alerttype_id`,
    `dbxalerttype`.`Name` as `alerttype_Name`,
    `dbxalerttype`.`AlertCategoryId` as `alerttype_AlertCategoryId`,
    `dbxalerttype`.`Status_id` as `alerttype_Status_id`,
    `dbxalerttype`.`IsGlobal` as `alerttype_IsGlobal`,
    `dbxalerttype`.`DisplaySequence` as `alerttype_DisplaySequence`,
    `dbxalerttype`.`defaultFrequencyId` as `alerttype_freqId`,
    `dbxalerttype`.`defaultFrequencyValue` as `alerttype_freqValue`,
    `dbxalerttype`.`defaultFrequencyTime` as `alerttype_freqTime`,
    `dbxalerttype`.`isAccountLevel` as `alerttype_isAccountLevel`,
    (
    select
        count(0)
    from
        `alertsubtype`
    where
        (
        (`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`) and
        (`alertsubtype`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`) and 
        (`alertsubtype`.`isGlobal` = 0)
        )
    ) as `userAlerts_count`,
    (
    select
        count(`alertsubtype`.`id`)
    from
        `alertsubtype`
    where
        (`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`) and
        (`alertsubtype`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`)
        ) as `Alerts_count`,
    `dbxalerttype`.`softdeleteflag` as `alerttype_softdeleteflag`,
    `dbxalerttypetext`.`LanguageCode` as `alerttypetext_LanguageCode`,
    `dbxalerttypetext`.`DisplayName` as `alerttypetext_DisplayName`,
    `dbxalerttypetext`.`Description` as `alerttypetext_Description`,
    `dbxalerttypetext`.`createdby` as `alerttypetext_createdby`,
    `dbxalerttypetext`.`modifiedby` as `alerttypetext_modifiedby`,
    `dbxalerttypetext`.`createdts` as `alerttypetext_createdts`,
    `dbxalerttypetext`.`lastmodifiedts` as `alerttypetext_lastmodifiedts`,
    `dbxalerttypetext`.`synctimestamp` as `alerttypetext_synctimestamp`,
    `dbxalerttypetext`.`softdeleteflag` as `alerttypetext_softdeleteflag`,
    `dbxalerttype`.`companyLegalUnit` as `alerttype_companyLegalUnit`
from
    (`dbxalerttypetext`
join `dbxalerttype` on
    ((`dbxalerttypetext`.`AlertTypeId` = `dbxalerttype`.`id`) and 
    (`dbxalerttypetext`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`) 
    ));
	
	

-- dbxdb.alertcategory_view source
DROP VIEW IF EXISTS `alertcategory_view`;

create or replace
algorithm = UNDEFINED view `alertcategory_view` as
select
    `dbxalertcategory`.`id` as `alertcategory_id`,
    `dbxalertcategory`.`status_id` as `alertcategory_status_id`,
    `dbxalertcategory`.`accountLevel` as `alertcategory_accountLevel`,
    `dbxalertcategory`.`DisplaySequence` as `alertcategory_DisplaySequence`,
    `dbxalertcategory`.`softdeleteflag` as `alertcategory_softdeleteflag`,
    `dbxalertcategory`.`Name` as `alertcategory_Name`,
    `dbxalertcategory`.`defaultFrequencyId` as `alertcategory_freqId`,
    `dbxalertcategory`.`defaultFrequencyValue` as `alertcategory_freqValue`,
    `dbxalertcategory`.`defaultFrequencyTime` as `alertcategory_freqTime`,
    (
    select
        count(`dbxalerttype`.`id`)
    from
        `dbxalerttype`
    where
        (`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`) and
        (`dbxalerttype`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)
        ) as `Groups_count`,
    (
    select
        sum(`alerttype_view`.`Alerts_count`)
    from
        `alerttype_view`
    where
        (`alerttype_view`.`alerttype_AlertCategoryId` = `dbxalertcategory`.`id`) and 
        (`alerttype_view`.`alerttype_companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)) as `Alerts_count`,
    `dbxalertcategorytext`.`LanguageCode` as `alertcategorytext_LanguageCode`,
    `dbxalertcategorytext`.`DisplayName` as `alertcategorytext_DisplayName`,
    `dbxalertcategorytext`.`Description` as `alertcategorytext_Description`,
    `dbxalertcategorytext`.`createdby` as `alertcategorytext_createdby`,
    `dbxalertcategorytext`.`modifiedby` as `alertcategorytext_modifiedby`,
    `dbxalertcategorytext`.`createdts` as `alertcategorytext_createdts`,
    `dbxalertcategorytext`.`lastmodifiedts` as `alertcategorytext_lastmodifiedts`,
    `dbxalertcategorytext`.`synctimestamp` as `alertcategorytext_synctimestamp`,
    `dbxalertcategorytext`.`softdeleteflag` as `alertcategorytext_softdeleteflag`,
    `dbxalertcategory`.`companyLegalUnit` as `alertcategory_companyLegalUnit`
from
    (`dbxalertcategorytext`
join `dbxalertcategory` on
    ((`dbxalertcategorytext`.`AlertCategoryId` = `dbxalertcategory`.`id`) and 
    (`dbxalertcategorytext`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)
    ));	

-- dbxdb.notificationview source

DROP VIEW IF EXISTS `notificationview`;

create or replace
algorithm = UNDEFINED view `notificationview` as
select
    `notification`.`notificationId` as `notificationId`,
    `notification`.`imageURL` as `imageURL`,
    `usernotification`.`isRead` as `isRead`,
    `notification`.`notificationActionLink` as `notificationActionLink`,
    `notification`.`notificationModule` as `notificationModule`,
    `notification`.`notificationSubject` as `notificationSubject`,
    `notification`.`notificationSubModule` as `notificationSubModule`,
    `notification`.`notificationText` as `notificationText`,
    `usernotification`.`receivedDate` as `receivedDate`,
    `usernotification`.`id` as `userNotificationId`,
    `usernotification`.`user_id` as `user_id`,
    `notification`.`notificationCategory` as `notificationCategory`,
    `notification`.`actionButtonLabelName` as `actionButtonLabelName`,
    `notification`.`companyLegalUnit` as `companyLegalUnit`
from
    (`usernotification`
join `notification` on
    ((`notification`.`notificationId` = `usernotification`.`notification_id`) and 
    (`notification`.`companyLegalUnit` = `usernotification`.`companyLegalUnit`) ));

ALTER TABLE `alertsubtype` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `companyLegalUnit`);

ALTER TABLE `alertsubtypetext` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`alertSubTypeId`, `languageCode`, `companyLegalUnit`);

ALTER TABLE `dbxalerttype` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `companyLegalUnit`);

ALTER TABLE `dbxalerttypetext` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`AlertTypeId`, `LanguageCode`, `companyLegalUnit`);

ALTER TABLE `dbxalertcategory` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `companyLegalUnit`);

ALTER TABLE `alertcategorychannel` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`ChannelID`, `AlertCategoryId`, `companyLegalUnit`);

ALTER TABLE `dbxalertcategorytext` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`AlertCategoryId`, `LanguageCode`, `companyLegalUnit`);

ALTER TABLE `alerttypechannel` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`channelId`, `alertTypeId`,`companyLegalUnit`);

ALTER TABLE `communicationtemplate` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`Id`, `companyLegalUnit`);

ALTER TABLE `alertsubtypeapp` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`appId`, `alertSubTypeId`, `companyLegalUnit`);

ALTER TABLE `alertsubtypecustomertype` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`customerTypeId`, `alertSubTypeId`, `companyLegalUnit`);

ALTER TABLE `alertsubtypeaccounttype` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`accountTypeId`, `alertSubTypeId`, `companyLegalUnit`);

ALTER TABLE `alertsubtypechannel` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`channelId`, `alertSubTypeId`, `companyLegalUnit`);

ALTER TABLE `dbxdb`.`dbxcustomeralertentitlement` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`Customer_id`, `alertCategoryId`, `AlertTypeId`, `alertSubTypeId`, `AccountId`, `AccountType`, `companyLegalUnit`);

ALTER TABLE `dbxdb`.`customeralertchannel` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `channelId`, `accountId`, `accountType`, `companyLegalUnit`);


DROP procedure IF EXISTS `customeralertchannel_sync`;

DELIMITER $$
CREATE PROCEDURE `customeralertchannel_sync`(
								in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in changedLevel varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in channelsStr varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,                                                        
                                in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                in companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SET @preference = (select `alertPreferenceView` from `customerviewalertconfiguration`);

IF operationType is null THEN
     SET operationType = "";
ELSEIF operationType = 'edit' and changedLevel is not null THEN 
SET @channels_list = ( select group_concat(channel.id SEPARATOR ',') from `channel` where (FIND_IN_SET(`channel`.`id`,channelsStr))) ; 
SET @channels_list = IF(@channels_list is null, '', @channels_list);

	IF (@preference = changedLevel AND changedLevel LIKE 'CATEGORY') THEN
	   DELETE FROM `customeralertchannel` where `alertCategoryId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list) and `companyLegalUnit` = companyLegalUnit;
	ELSEIF (@preference = changedLevel AND changedLevel LIKE 'GROUP') THEN
	   DELETE FROM `customeralertchannel` where `alertTypeId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list)  and `companyLegalUnit` = companyLegalUnit;
	ELSEIF (@preference = changedLevel AND changedLevel LIKE 'ALERT') THEN
	  DELETE FROM `customeralertchannel` where `alertSubTypeId` = filterValue AND FIND_IN_SET(`customeralertchannel`.`channelId`, @channels_list)  and `companyLegalUnit` = companyLegalUnit;
	END IF;
    
ELSEIF operationType = 'reassign' THEN
  IF (@preference != 'CATEGORY' ) THEN
   DELETE FROM `customeralertchannel` where `alertTypeId` = filterValue  and `companyLegalUnit` = companyLegalUnit;
  END IF;
 END IF;
END$$

DELIMITER ;


DROP VIEW IF EXISTS `alertattribute_view`;
CREATE VIEW `alertattribute_view` AS select `alertattribute`.`id` AS `alertattribute_id`,`alertattribute`.`LanguageCode` AS `alertattribute_LanguageCode`,`alertattribute`.`name` AS `alertattribute_name`,`alertattribute`.`type` AS `alertattribute_type`,`alertattribute`.`softdeleteflag` AS `alertattribute_softdeleteflag`,`alertattributelistvalues`.`id` AS `alertattributelistvalues_id`,`alertattributelistvalues`.`AlertAttributeId` AS `alertattributelistvalues_AlertAttributeId`,`alertattributelistvalues`.`LanguageCode` AS `alertattributelistvalues_LanguageCode`,`alertattributelistvalues`.`name` AS `alertattributelistvalues_name`,`alertattributelistvalues`.`softdeleteflag` AS `alertattributelistvalues_softdeleteflag`,`alertattribute`.`companyLegalUnit` as `alertattribute_companyLegalUnit`  from (`alertattribute` left join `alertattributelistvalues` on((`alertattribute`.`id` = `alertattributelistvalues`.`AlertAttributeId` and `alertattribute`.`companyLegalUnit` = `alertattributelistvalues`.`companyLegalUnit` )));


ALTER TABLE `alertattribute` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`, `LanguageCode`, `companyLegalUnit`);

ALTER TABLE `customeralertswitch` 
DROP PRIMARY KEY,
ADD PRIMARY KEY (`Customer_id`, `AccountID`, `AlertCategoryId`, `AccountType`, `companyLegalUnit`);


DROP VIEW IF EXISTS `communicationtemplate_channel_view`;

CREATE VIEW `communicationtemplate_channel_view` AS

 SELECT `communicationtemplate`.`Id` AS `communicationtemplate_id`,
 `communicationtemplate`.`Name` AS `communicationtemplate_Name`,
 `communicationtemplate`.`Text` AS `communicationtemplate_Text`,
 `communicationtemplate`.`AlertSubTypeId` AS `communicationtemplate_AlertSubTypeId`,
 `communicationtemplate`.`LanguageCode` AS `communicationtemplate_LanguageCode`,
 `communicationtemplate`.`ChannelID` AS `communicationtemplate_ChannelID`,
 `communicationtemplate`.`Status_id` AS `communicationtemplate_Status_id`,
 `communicationtemplate`.`Subject` AS `communicationtemplate_Subject`,
 `communicationtemplate`.`SenderName` AS `communicationtemplate_SenderName`,
 `communicationtemplate`.`SenderEmail` AS `communicationtemplate_SenderEmail`,
 `channeltext`.`Description` AS `channeltext_Description`,
 `channeltext`.`softdeleteflag` AS `channeltext_softdeleteflag`,
 `communicationtemplate`.`softdeleteflag` AS `communicationtemplate_softdeleteflag`,
 `communicationtemplate`.`companyLegalUnit` AS `communicationtemplate_companyLegalUnit`
 FROM 
 (`communicationtemplate` 
 JOIN `channeltext` ON
 (((`communicationtemplate`.`ChannelID` = `channeltext`.`channelID`)
 AND (`communicationtemplate`.`LanguageCode` = `channeltext`.`LanguageCode`))));


DROP PROCEDURE IF EXISTS `customeralertFrequency_sync`;
DELIMITER $$
CREATE PROCEDURE `customeralertFrequency_sync`( 
							in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
							in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                            in companyLegalUnit varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SET @preference = (select `alertPreferenceView` from `customerviewalertconfiguration`);
IF (operationType is null) THEN
  SET operationType = "";
ELSEIF (operationType LIKE 'edit' && @preference = 'ALERT') THEN
   DELETE FROM `customeralertfrequency` where `alertSubTypeId` = filterValue and `companyLegalUnit` = companyLegalUnit; 
ELSEIF (operationType LIKE 'reassign') THEN
  IF (@preference != 'CATEGORY') THEN
   DELETE FROM `customeralertfrequency` where `alertTypeId` = filterValue and `companyLegalUnit` = companyLegalUnit;
	END IF;
END IF;
end$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `dbxcustomeralertentitlement_sync`;
DELIMITER $$
CREATE PROCEDURE `dbxcustomeralertentitlement_sync`( 
								in operationType varchar(20) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in filterValue varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
								in companyLegalUnit varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
								
BEGIN
IF operationType = 'edit'  THEN	  
	  Delete from `dbxcustomeralertentitlement` where `alertSubTypeId`= filterValue and `companyLegalUnit` = companyLegalUnit;	
ELSEIF operationType = 'reassign' THEN
	  Delete from `dbxcustomeralertentitlement` where `AlertTypeId` = filterValue and `companyLegalUnit` = companyLegalUnit;
end IF;
END$$
DELIMITER ;

DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertcategorylevel`;

CREATE VIEW `alerts_fetch_globaldata_view_alertcategorylevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertcategorychannel`.`ChannelID` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`,
		`alertcategorychannel`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (((`dbxalertcategory`
        JOIN `alertcategorychannel` ON ((`alertcategorychannel`.`AlertCategoryId` = `dbxalertcategory`.`id`)
        AND (`alertcategorychannel`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)))
        JOIN `dbxalerttype` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)
        AND (`dbxalerttype`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)))
        JOIN `alertsubtype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)
        AND (`alertsubtype`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`)));
		

DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertgrouplevel`;
		
CREATE VIEW `alerts_fetch_globaldata_view_alertgrouplevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alerttypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`,
		`alerttypechannel`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (((`dbxalerttype`
        JOIN `alerttypechannel` ON ((`dbxalerttype`.`id` = `alerttypechannel`.`alertTypeId`)
		AND (`dbxalerttype`.`companyLegalUnit` = `alerttypechannel`.`companyLegalUnit`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)
		AND (`dbxalerttype`.`companyLegalUnit` = `alertsubtype`.`companyLegalUnit`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)
		AND (`dbxalerttype`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)));
		

DROP VIEW IF EXISTS `alerts_fetch_globaldata_view_alertlevel`;
CREATE VIEW `alerts_fetch_globaldata_view_alertlevel` AS
    SELECT 
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertsubtypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`,
		`alertsubtypechannel`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (((`alertsubtype`
        JOIN `alertsubtypechannel` ON ((`alertsubtype`.`id` = `alertsubtypechannel`.`alertSubTypeId`)
		AND (`alertsubtype`.`companyLegalUnit` = `alertsubtypechannel`.`companyLegalUnit`)))
        JOIN `dbxalerttype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)
		AND (`alertsubtype`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)
		AND (`dbxalerttype`.`companyLegalUnit` = `dbxalertcategory`.`companyLegalUnit`)));
		

DROP VIEW IF EXISTS `alertcustomerchannels_view_alertcategorylevel`;

CREATE VIEW `alertcustomerchannels_view_alertcategorylevel` AS
    SELECT 
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`,
		`dbxcustomeralertentitlement`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (((`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`)
			AND (`dbxcustomeralertentitlement`.`companyLegalUnit` = `customeralertchannel`.`companyLegalUnit`))))
        JOIN `dbxalerttype` ON ((`dbxcustomeralertentitlement`.`AlertTypeId` = `dbxalerttype`.`id`)
		AND (`dbxcustomeralertentitlement`.`companyLegalUnit` = `dbxalerttype`.`companyLegalUnit`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)
		AND (`dbxalerttype`.`companyLegalUnit` = `alertsubtype`.`companyLegalUnit`)));
		

DROP VIEW IF EXISTS `alertcustomerchannels_view_alertgrouplevel`;

CREATE VIEW `alertcustomerchannels_view_alertgrouplevel` AS
    SELECT 
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`,
		`dbxcustomeralertentitlement`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        ((`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`AlertTypeId` = `customeralertchannel`.`alertTypeId`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`)
			AND (`dbxcustomeralertentitlement`.`companyLegalUnit` = `customeralertchannel`.`companyLegalUnit`))))
        JOIN `alertsubtype` ON ((`dbxcustomeralertentitlement`.`AlertTypeId` = `alertsubtype`.`AlertTypeId`)
		AND (`dbxcustomeralertentitlement`.`companyLegalUnit` = `alertsubtype`.`companyLegalUnit`)));
		

DROP VIEW IF EXISTS `alertcustomerchannels_view_alertlevel`;

CREATE VIEW `alertcustomerchannels_view_alertlevel` AS
    SELECT 
        `dbxcustomeralertentitlement`.`alertSubTypeId` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`,
		`dbxcustomeralertentitlement`.`companyLegalUnit` AS `companyLegalUnit`
		
    FROM
        (`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertSubTypeId` = `customeralertchannel`.`alertSubTypeId`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`)
            AND (`dbxcustomeralertentitlement`.`AlertTypeId` = `customeralertchannel`.`alertTypeId`)
			AND (`dbxcustomeralertentitlement`.`companyLegalUnit` = `customeralertchannel`.`companyLegalUnit`))));

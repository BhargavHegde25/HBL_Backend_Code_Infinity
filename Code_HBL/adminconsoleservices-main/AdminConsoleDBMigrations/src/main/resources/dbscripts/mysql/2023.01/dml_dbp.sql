INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('PARTIAL_REPAYMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Early Partial Repayment' ,'Early Partial Repayment' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '0' , '0' , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('PARTIAL_REPAYMENT' ,'en-GB' ,'Early Partial Repayment' ,'Early Partial Repayment' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('PARTIAL_REPAYMENT' ,'de-DE' ,'Early Partial Repayment' ,'Early Partial Repayment' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('PARTIAL_REPAYMENT' ,'en-US' ,'Early Partial Repayment' ,'Early Partial Repayment' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('PARTIAL_REPAYMENT' ,'es-ES' ,'Early Partial Repayment' ,'Early Partial Repayment' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('PARTIAL_REPAYMENT' ,'fr-FR' ,'Early Partial Repayment' ,'Early Partial Repayment' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'PARTIAL_REPAYMENT');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_WEALTH', 'PARTIAL_REPAYMENT');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'PARTIAL_REPAYMENT');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE','PARTIAL_REPAYMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EARLY_PARTIAL_REPAYMENT-CREATE','Create Early Partial Repayment','Create Early Partial Repayment',0,0,null,0,10,null,0,'CREATE','CUSTOMERID_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW','PARTIAL_REPAYMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EARLY_PARTIAL_REPAYMENT-VIEW','View Early Partial Repayment','View Early Partial Repayment',0,0,null,0,10,null,0,'VIEW','CUSTOMERID_LEVEL');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE','en-US', 'Create Early Partial Repayment', 'Create Early Partial Repayment');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW','en-US', 'View Early Partial Repayment', 'View Early Partial Repayment');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_WEALTH','EARLY_PARTIAL_REPAYMENT-CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EARLY_PARTIAL_REPAYMENT-CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_RETAIL','EARLY_PARTIAL_REPAYMENT-CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_WEALTH','EARLY_PARTIAL_REPAYMENT-VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EARLY_PARTIAL_REPAYMENT-VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_RETAIL','EARLY_PARTIAL_REPAYMENT-VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('EARLY_PARTIAL_REPAYMENT-CREATE', 'EARLY_PARTIAL_REPAYMENT-CREATE', 'PARTIAL_REPAYMENT', 'Create Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('EARLY_PARTIAL_REPAYMENT-VIEW', 'EARLY_PARTIAL_REPAYMENT-VIEW', 'PARTIAL_REPAYMENT', 'View Early Partial Repayment', 'Early Partial Repayment');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID410','PID45', 'EARLY_PARTIAL_REPAYMENT-CREATE', 'PARTIAL_REPAYMENT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID411','PID45', 'EARLY_PARTIAL_REPAYMENT-VIEW', 'PARTIAL_REPAYMENT', '1');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('LCPT_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'LETTER_OF_CREDIT_PAYMENT_TERMS', 'Letter of Credit Payment Terms', '[{\"type\":\"Sight\",\"colorCode\":\"#E50033\"},{\"type\":\"Acceptance\",\"colorCode\":\"#FF8600\"},{\"type\":\"Deferred\",\"colorCode\":\"#229EAE\"},{\"type\":\"Negotiation Sight\",\"colorCode\":\"#0971A8\"},{\"type\":\"Negotiation Acceptance\",\"colorCode\":\"#BDBDBD\"}]', 'CLIENT', '1');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('EXLC_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'EXPORT_LC_CHART_DATA', 'Export LC Chart Data', '[{\"DisplayStatus\":\"Pending Requests\",\"LCStatus\":[\"New\",\"Submitted to Bank\",\"Processing by Bank\",\"Returned by Bank\"]},{\"DisplayStatus\":\"Approved\",\"LCStatus\":[\"Approved\"]},{\"DisplayStatus\":\"Settled\",\"LCStatus\":[\"Partially Settled\"]},{\"DisplayStatus\":\"Rejected\",\"LCStatus\":[\"Rejected\",\"Cancelled\"]}]', 'CLIENT', '1');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('OUCL_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'OUTWARDCOLLECTIONS_CHART_DATA', 'Outward Collections Chart Data', '[{\"DisplayStatus\":\"Pending\",\"outwardStatus\":[\"Submitted to Bank\",\"Returned by Bank\",\"Processing by Bank\",\"Overdue\"]}, {\"DisplayStatus\":\"Approved\",\"outwardStatus\":[\"Approved\"]},{\"DisplayStatus\":\"Settled\",\"outwardStatus\":[\"Settled\"]},{\"DisplayStatus\":\"Rejected\",\"outwardStatus\":[\"Rejected\",\"Cancelled\"]}]', 'CLIENT', '1');

INSERT INTO `rrole` (`id`) VALUES ('CLOSE_ACCOUNT-CREATE');
INSERT INTO `rrole` (`id`) VALUES ('VIEW_CLOSED_ACCOUNT');
INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`) VALUES ('CLOSE_ACCOUNT', 'RETAIL_AND_BUSINESS_BANKING', 'Close Account', 'Close Account', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '84', '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT', 'en-GB', 'Close Account', 'Close Account');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT', 'de-DE', 'Close Account', 'Close Account');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT', 'en-US', 'Close Account', 'Close Account');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT', 'es-ES', 'Close Account', 'Close Account');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT', 'fr-FR', 'Close Account', 'Close Account');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'CLOSE_ACCOUNT');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_WEALTH', 'CLOSE_ACCOUNT');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'CLOSE_ACCOUNT');
INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`) VALUES ('CLOSE_ACCOUNT-CREATE','CLOSE_ACCOUNT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CLOSE_ACCOUNT-CREATE','Create Close Account','Create Close Account','1', '0', '0', '48', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','CREATE','ACCOUNT_LEVEL');
INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`) VALUES ('VIEW_CLOSED_ACCOUNT','CLOSE_ACCOUNT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','VIEW_CLOSED_ACCOUNT','View Close Account','View Close Account','1', '0', '0', '49', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','ACCOUNT_LEVEL');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('CLOSE_ACCOUNT-CREATE','en-US', 'Create Close Account', 'Create Close Account');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('VIEW_CLOSED_ACCOUNT','en-US', 'View Close Account', 'View Close Account');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_WEALTH', 'CLOSE_ACCOUNT-CREATE');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_BUSINESS', 'CLOSE_ACCOUNT-CREATE');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_RETAIL', 'CLOSE_ACCOUNT-CREATE');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_WEALTH', 'VIEW_CLOSED_ACCOUNT');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_BUSINESS', 'VIEW_CLOSED_ACCOUNT');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_RETAIL', 'VIEW_CLOSED_ACCOUNT');
INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('CLOSE_ACCOUNT-CREATE', 'CLOSE_ACCOUNT-CREATE', 'CLOSE_ACCOUNT', 'Create Close Account', 'Close Account');
INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('VIEW_CLOSED_ACCOUNT', 'VIEW_CLOSED_ACCOUNT', 'CLOSE_ACCOUNT', 'View Close Account', 'Close Account');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID361','PID45', 'CLOSE_ACCOUNT-CREATE', 'CLOSE_ACCOUNT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID362','PID45', 'VIEW_CLOSED_ACCOUNT', 'CLOSE_ACCOUNT', '1');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (UUID(),'5801fa32-a416-45b6-af01-b22e2de93777', 'CLOSE_ACCOUNT-CREATE',null ,null);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`) VALUES (UUID(),'DEFAULT_GROUP', 'CLOSE_ACCOUNT-CREATE', null, null);
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (UUID(),'5801fa32-a416-45b6-af01-b22e2de93777', 'VIEW_CLOSED_ACCOUNT',null ,null);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`) VALUES (UUID(),'DEFAULT_GROUP', 'VIEW_CLOSED_ACCOUNT', null, null);

-- TO CREATE A EVENT ACCOUNT_CLOSURE
INSERT INTO `eventconsumertypes`  (`EventType`, `ServiceId`, `OperationId`) VALUES ('ACCOUNT_CLOSURE', 'Alerts', 'pushAlerts');
INSERT INTO `eventtype`  (`id`, `Name`, `ActivityType`) VALUES ('ACCOUNT_CLOSURE', 'Users account closure based alerts', 'CUSTOMER');
INSERT INTO `dbxalerttype`  (`id`, `Name`, `AlertCategoryId`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ACCOUNT_CLOSURE', 'Users account closure based alerts', 'ALERT_CAT_ACCOUNTS', 'SID_ACTIVE', '1', '2', 'DAILY', '10:00:00');
INSERT INTO `dbxalerttypetext`  (`AlertTypeId`, `DisplayName`, `Description`, `LanguageCode`) VALUES ('ACCOUNT_CLOSURE', 'Users account closure based alerts', 'Account closure approval and rejection alerts are included  ', 'en-US');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

-- TO CREATE A SUBEVENT ACCOUNT_CLOSURE_REJECTED
INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'ACCOUNT_CLOSURE', 'Account closure');
INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'ACCOUNT_CLOSURE', 'Account closure', 'The account closure request raised by user has been processed', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'Account closure', 'The account closure request raised by user has been processed', 'en-US', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

-- SUB EVENTS COMMUNICATION TEMPLATE SCRIPT ACCOUNT_CLOSURE_REJECTED
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'Account closure', 'This message is to inform you that we are not able to process your request for closing the account [#]Account Name-Account Number[/#] . Please contact the bank branch for further details', 'Account closure', '200000', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'Account closure', 'This message is to inform you that we are not able to process your request for closing the account [#]Account Name-Account Number[/#] . Please contact the bank branch for further details', 'Account closure', '200001', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'Account closure', 'This message is to inform you that we are not able to process your request for closing the account [#]Account Name-Account Number[/#] . Please contact the bank branch for further details', 'Account closure', '200002', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_REJECTED', 'Account closure', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> This message is to inform you that we are not able to process your request for closing the account [#]Account Name-Account Number[/#] . Please contact the bank branch for further details<br />Regards,<br /><br />Infinity Bank</p>', 'Account closure', '200003', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');

-- TO CREATE A SUBEVENT ACCOUNT_CLOSURE_APPROVED
INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'ACCOUNT_CLOSURE', 'Account closure');
INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'ACCOUNT_CLOSURE', 'Account closure', 'The account closure request raised by user has been processed', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'Account closure', 'The account closure request raised by user has been processed', 'en-US', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

-- SUB EVENTS COMMUNICATION TEMPLATE SCRIPT ACCOUNT_CLOSURE_APPROVED
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'Account closure', 'This message is to inform you that your request for closing the account [#]Account Name-Account Number[/#] is successful', 'Account closure', '200004', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'Account closure', 'This message is to inform you that your request for closing the account [#]Account Name-Account Number[/#] is successful', 'Account closure', '200005', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'Account closure', 'This message is to inform you that your request for closing the account [#]Account Name-Account Number[/#] is successful', 'Account closure', '200006', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_APPROVED', 'Account closure', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br />This message is to inform you that your request for closing the account [#]Account Name-Account Number[/#] is successful.<br />Regards,<br /><br />Infinity Bank</p>', 'Account closure', '200007', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');

-- TO CREATE A SUBEVENT ACCOUNT_CLOSURE_USER_SUSPENDED
INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'ACCOUNT_CLOSURE', 'Account closure');
INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'ACCOUNT_CLOSURE', 'Account closure', 'The account closure request raised by user has been processed', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'Account closure', 'The account closure request raised by user has been processed', 'en-US', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

-- SUB EVENTS COMMUNICATION TEMPLATE SCRIPT ACCOUNT_CLOSURE_USER_SUSPENDED
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'Account closure', 'Your request for account closure for the account [#]Account Name-Account Number[/#] is successful. You will no longer have access to the Internet Banking Facility.', 'Account closure', '200008', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'Account closure', 'Your request for account closure for the account [#]Account Name-Account Number[/#] is successful. You will no longer have access to the Internet Banking Facility.', 'Account closure', '200009', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'Account closure', 'Your request for account closure for the account [#]Account Name-Account Number[/#] is successful. You will no longer have access to the Internet Banking Facility.', 'Account closure', '200010', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('ACCOUNT_CLOSURE_USER_SUSPENDED', 'Account closure', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br />Your request for account closure for the account  [#]Account Name-Account Number[/#] is successful. You will no longer have access to the Internet Banking Facility.<br />Regards,<br /><br />Infinity Bank</p>', 'Account closure', '200011', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242km168393', 'TradeFinance', 'DashboardOverview', 'UpdateTradeFinanceConfiguration', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242sm168893', 'TradeFinance', 'DashboardOverview', 'GeneratePayablesList', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-024ekm163454', 'TradeFinance', 'DashboardOverview', 'GetDashboardDetails', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242si368393', 'TradeFinance', 'DashboardOverview', 'GenerateAllTradesList', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242si468493', 'TradeFinance', 'DashboardOverview', 'FetchPayables', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242swe68093', 'TradeFinance', 'DashboardOverview', 'FetchAllTradeDetails', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-02423r168393', 'TradeFinance', 'DashboardOverview', 'GenerateReceivablesList', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-024sdf368393', 'TradeFinance', 'DashboardOverview', 'FetchReceivables', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-024234r68393', 'TradeFinance', 'DashboardOverview', 'CreateTradeFinanceConfiguration', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242km16se23', 'TradeFinance', 'DashboardOverview', 'FetchTradeFinanceConfiguration', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242km16fg43', 'TradeFinance', 'DashboardOverview', 'FetchLimits', 'ALLOW');
INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('09357a9d-aq21-47g8-a43a-b34b2eba64e9', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACTIVITY_ID_SIMULATION', 'ActivityID', 'LENDING-APPLYPAYMENT-PR.PRINCIPAL.DECREASE', 'CLIENT', '1');

INSERT INTO `feature` (`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS', 'RETAIL_AND_BUSINESS_BANKING', 'Outward Collections', 'Create, Delete, View & Manage the Outward Collections', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0 , null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0) ;

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'OUTWARD_COLLECTIONS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_VIEW', 'View the Outward Collections', 'View the Outward Collections', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL') ;
INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'OUTWARD_COLLECTIONS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_UPDATE', 'Manage the Outward Collections', 'Manage the Outward Collections', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL') ;
INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'OUTWARD_COLLECTIONS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_CREATE', 'Create the Outward Collections', 'Create the Outward Collections', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL') ;
INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'OUTWARD_COLLECTIONS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_DELETE', 'Delete the Outward Collections', 'Delete the Outward Collections', 1, 0, null, 0, 10, null, 0, 'DELETE', 'ACCOUNT_LEVEL') ;

INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_CREATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_CREATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_DELETE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_DELETE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_DELETE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('OUTWARD_COLLECTIONS', 'en-GB', 'OUTWARD COLLECTIONS', 'Create, Delete, View & Manage the Outward Collections', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0') ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('OUTWARD_COLLECTIONS', 'en-US', 'OUTWARD COLLECTIONS', 'Create, Delete, View & Manage the Outward Collections', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0') ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'de-DE', 'Outward Collections View', 'Outward Collections View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'en-GB', 'Outward Collections View', 'Outward Collections View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'en-US', 'Outward Collections View', 'Outward Collections View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'es-ES', 'Outward Collections View', 'Outward Collections View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_VIEW', 'fr-FR', 'Outward Collections View', 'Outward Collections View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'de-DE', 'Outward Collections Update', 'Outward Collections Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'en-GB', 'Outward Collections Update', 'Outward Collections Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'en-US', 'Outward Collections Update', 'Outward Collections Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'es-ES', 'Outward Collections Update', 'Outward Collections Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_UPDATE', 'fr-FR', 'Outward Collections Update', 'Outward Collections Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'de-DE', 'Outward Collections Create', 'Outward Collections Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'en-GB', 'Outward Collections Create', 'Outward Collections Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'en-US', 'Outward Collections Create', 'Outward Collections Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'es-ES', 'Outward Collections Create', 'Outward Collections Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_CREATE', 'fr-FR', 'Outward Collections Create', 'Outward Collections Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'de-DE', 'Outward Collections Delete', 'Outward Collections Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'en-GB', 'Outward Collections Delete', 'Outward Collections Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'en-US', 'Outward Collections Delete', 'Outward Collections Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'es-ES', 'Outward Collections Delete', 'Outward Collections Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_DELETE', 'fr-FR', 'Outward Collections Delete', 'Outward Collections Delete' ) ;

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161003', 'TradeFinance', 'OutwardCollections', 'getCollections', 'OUTWARD_COLLECTIONS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm162003', 'TradeFinance', 'OutwardCollections', 'getCollectionById', 'OUTWARD_COLLECTIONS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm163003', 'TradeFinance', 'OutwardCollections', 'generateCollectionReport', 'OUTWARD_COLLECTIONS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm164003', 'TradeFinance', 'OutwardCollections', 'generateCollectionsList', 'OUTWARD_COLLECTIONS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm165003', 'TradeFinance', 'OutwardCollections', 'updateCollection', 'OUTWARD_COLLECTIONS_UPDATE' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm166003', 'TradeFinance', 'OutwardCollections', 'saveCollection', 'OUTWARD_COLLECTIONS_CREATE' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm167003', 'TradeFinance', 'OutwardCollections', 'deleteCollection', 'OUTWARD_COLLECTIONS_DELETE' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm168003', 'TradeFinance', 'OutwardCollections', 'createCollection', 'OUTWARD_COLLECTIONS_CREATE' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm169003', 'TradeFinance', 'OutwardCollections', 'requestCollectionStatus', 'OUTWARD_COLLECTIONS_UPDATE' );

INSERT INTO `feature` (`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS', 'RETAIL_AND_BUSINESS_BANKING', 'Outward Collections Amendments', 'Create, View & Manage the Outward Collections Amendments', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0 , null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0) ;

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'OUTWARD_COLLECTIONS_AMENDMENTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'View the Outward Collections Amendments', 'View the Outward Collections Amendments', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL') ;
INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'OUTWARD_COLLECTIONS_AMENDMENTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'Manage the Outward Collections Amendments', 'Manage the Outward Collections Amendments', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL') ;
INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'OUTWARD_COLLECTIONS_AMENDMENTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'Create the Outward Collections Amendments', 'Create the Outward Collections Amendments', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL') ;

INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS', 'en-GB', 'OUTWARD COLLECTIONS AMENDMENTS', 'Create, View & Manage the Outward Collections Amendments', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0') ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS', 'en-US', 'OUTWARD COLLECTIONS AMENDMENTS', 'Create, View & Manage the Outward Collections Amendments', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0') ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTIONS_AMENDMENTS' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'de-DE', 'Outward Collections Amendments View', 'Outward Collections Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'en-GB', 'Outward Collections Amendments View', 'Outward Collections Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'en-US', 'Outward Collections Amendments View', 'Outward Collections Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'es-ES', 'Outward Collections Amendments View', 'Outward Collections Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_VIEW', 'fr-FR', 'Outward Collections Amendments View', 'Outward Collections Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'de-DE', 'Outward Collections Amendments Update', 'Outward Collections Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'en-GB', 'Outward Collections Amendments Update', 'Outward Collections Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'en-US', 'Outward Collections Amendments Update', 'Outward Collections Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'es-ES', 'Outward Collections Amendments Update', 'Outward Collections Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE', 'fr-FR', 'Outward Collections Amendments Update', 'Outward Collections Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'de-DE', 'Outward Collections Amendments Create', 'Outward Collections Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'en-GB', 'Outward Collections Amendments Create', 'Outward Collections Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'en-US', 'Outward Collections Amendments Create', 'Outward Collections Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'es-ES', 'Outward Collections Amendments Create', 'Outward Collections Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('OUTWARD_COLLECTIONS_AMENDMENTS_CREATE', 'fr-FR', 'Outward Collections Amendments Create', 'Outward Collections Amendments Create' ) ;

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161103', 'TradeFinance', 'OutwardCollections', 'getAmendments', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161203', 'TradeFinance', 'OutwardCollections', 'getAmendmentById', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161303', 'TradeFinance', 'OutwardCollections', 'generateAmendmentReport', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161403', 'TradeFinance', 'OutwardCollections', 'generateAmendmentsList', 'OUTWARD_COLLECTIONS_AMENDMENTS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161503', 'TradeFinance', 'OutwardCollections', 'updateAmendment', 'OUTWARD_COLLECTIONS_AMENDMENTS_UPDATE' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm161603', 'TradeFinance', 'OutwardCollections', 'createAmendment', 'OUTWARD_COLLECTIONS_AMENDMENTS_CREATE' );

-- SECURE MESSAGES / TRADEFINANCE / NEW CATEGORIES
INSERT INTO `requestcategory` (`id`, `Name`) VALUES ('RCID_TF_LETTEROFCREDIT', 'TradeFinance - Letter of Credit');
INSERT INTO `requestcategory` (`id`, `Name`) VALUES ('RCID_TF_GUARANTEES', 'TradeFinance - Guarantees');
INSERT INTO `requestcategory` (`id`, `Name`) VALUES ('RCID_TF_COLLECTIONS', 'TradeFinance - Collections');

-- ALERT CATEGORY: ALERT_CAT_TRADEFINANCE
INSERT INTO `dbxalertcategory` (`id`, `Name`, `accountLevel`, `status_id`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ALERT_CAT_TRADEFINANCE', 'Trade Finance', '0', 'SID_ACTIVE', '5', 'DAILY', '10:00:00');
INSERT INTO `dbxalertcategorytext` (`AlertCategoryId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('ALERT_CAT_TRADEFINANCE', 'en-US', 'Trade Finance', 'Trade finance alerts are grouped here');

INSERT INTO `alertcategorychannel` (`ChannelID`, `AlertCategoryId`) VALUES ('CH_NOTIFICATION_CENTER', 'ALERT_CAT_TRADEFINANCE');
INSERT INTO `alertcategorychannel` (`ChannelID`, `AlertCategoryId`) VALUES ('CH_PUSH_NOTIFICATION', 'ALERT_CAT_TRADEFINANCE');

-- ALERT CONTENT FIELD: orderId, daysLeft
INSERT INTO `alertcontentfields` (`Code`, `Name`, `DefaultValue`) VALUES ('orderId', 'TradeFinance Record Id', '*********');
INSERT INTO `alertcontentfields` (`Code`, `Name`, `DefaultValue`) VALUES ('daysLeft', 'TradeFinance Days Left', '**');

-- ALERT TYPE: TRADEFINANCE_IMPORTLC
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_IMPORTLC');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC', 'Import Letter of Credits', 'CUSTOMER', 'Import Letter of Credits');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_IMPORTLC', 'Import Letter of Credits', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '1');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC', 'en-US', 'Import Letter of Credits', 'Import Letter of Credits');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_IMPORTLC');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_IMPORTLC');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_IMPORTLC');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_IMPORTLC');

-- ALERT SUBTYPE: IMPORT_LC_CREATED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_CREATED', 'TRADEFINANCE_IMPORTLC', 'Alert when Import LC has been Created', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_CREATED', 'TRADEFINANCE_IMPORTLC', 'Import Letter Of Credit Created', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_CREATED', 'en-US', 'Import Letter Of Credit Created', 'Import Letter Of Credit Created');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_CREATED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_CREATED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_CREATED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_CREATED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1001', 'en-US', 'IMPORT_LC_CREATED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC has been created successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1002', 'en-US', 'IMPORT_LC_CREATED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC has been created successfully', 'Import Letter Of Credit Created');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1003', 'en-US', 'IMPORT_LC_CREATED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC has been created successfully', 'Import Letter Of Credit Created');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1004', 'en-US', 'IMPORT_LC_CREATED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC has been created successfully', 'Import Letter Of Credit Created');

-- ALERT SUBTYPE: IMPORT_LC_SUBMITTED_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_SUBMITTED_APPROVAL', 'TRADEFINANCE_IMPORTLC', 'Alert when Import LC has been submitted for the approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_SUBMITTED_APPROVAL', 'TRADEFINANCE_IMPORTLC', 'Import LC has been submitted for the approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_SUBMITTED_APPROVAL', 'en-US', 'Import LC has been submitted for the approval', 'Import LC has been submitted for the approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_SUBMITTED_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_SUBMITTED_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_SUBMITTED_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_SUBMITTED_APPROVAL');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1005', 'en-US', 'IMPORT_LC_SUBMITTED_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1006', 'en-US', 'IMPORT_LC_SUBMITTED_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC has been submitted successfully', 'Import LC Submitted For Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1007', 'en-US', 'IMPORT_LC_SUBMITTED_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC has been submitted successfully', 'Import LC Submitted For Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1008', 'en-US', 'IMPORT_LC_SUBMITTED_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC has been submitted successfully', 'Import LC Submitted For Approval');

-- ALERT SUBTYPE: IMPORT_LC_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_APPROVED', 'TRADEFINANCE_IMPORTLC', 'Alert when Import LC submitted for approval has been approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_APPROVED', 'TRADEFINANCE_IMPORTLC', 'Import LC Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_APPROVED', 'en-US', 'Import LC Approved', 'IImport LC submitted for approval has been approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_APPROVED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1009', 'en-US', 'IMPORT_LC_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1010', 'en-US', 'IMPORT_LC_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been approved successfully', 'Import LC Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1011', 'en-US', 'IMPORT_LC_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been approved successfully', 'Import LC Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1012', 'en-US', 'IMPORT_LC_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been approved successfully', 'Import LC Approved');

-- ALERT SUBTYPE: IMPORT_LC_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_REJECTED', 'TRADEFINANCE_IMPORTLC', 'Alert when Import LC submitted for approval has been rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_REJECTED', 'TRADEFINANCE_IMPORTLC', 'Import LC Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_REJECTED', 'en-US', 'Import LC Rejected', 'Import LC submitted for approval has been rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_REJECTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1013', 'en-US', 'IMPORT_LC_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1014', 'en-US', 'IMPORT_LC_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been rejected', 'Import LC Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1017', 'en-US', 'IMPORT_LC_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been rejected', 'Import LC Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1016', 'en-US', 'IMPORT_LC_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC [#]orderId[/#] has been rejected', 'Import LC Rejected');

-- ALERT TYPE: TRADEFINANCE_IMPORTLC_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_IMPORTLC_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter of Credit Amendments', 'CUSTOMER', 'Import Letter of Credit Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter of Credit Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '2');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC_AMENDMENTS', 'en-US', 'Import Letter of Credit Amendments', 'Import Letter of Credit Amendments');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_IMPORTLC_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_IMPORTLC_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_IMPORTLC_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_IMPORTLC_AMENDMENTS');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendments has been Submitted for Bank Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Submitted for Bank Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'en-US', 'Import Letter Of Credit Amendment Submitted for Bank Approval', 'Import Letter Of Credit Amendment Submitted for Bank Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2001', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC Amendment has been submitted successfully for bank approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2002', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC Amendment has been submitted successfully for bank approval', 'Import Letter Of Credit Amendment Submitted for Bank Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2003', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC Amendment has been submitted successfully for bank approval', 'Import Letter Of Credit Amendment Submitted for Bank Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2004', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC Amendment has been submitted successfully for bank approval', 'Import Letter Of Credit Amendment Submitted for Bank Approval');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment has been Submitted with Self-consent', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Submitted with Self consent', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'en-US', 'Import Letter Of Credit Amendment Submitted with Self consent', 'Import Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2005', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2006', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Import Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2007', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Import Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2008', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Import Letter Of Credit Amendment Submitted with Self consent');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment has been Submitted to Bank for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Submitted to Bank for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'en-US', 'Import Letter Of Credit Amendment Submitted to Bank for Approval', 'Import Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2009', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2010', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approved successfully', 'Import Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2011', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approved successfully', 'Import Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2012', 'en-US', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approved successfully', 'Import Letter Of Credit Amendment Submitted to Bank for Approval');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment was Pending with bank for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Pending with bank for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'en-US', 'Import Letter Of Credit Amendment Pending with bank for Approval', 'Import Letter Of Credit Amendment Pending with bank for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2013', 'en-US', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been Pending with the bank for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2014', 'en-US', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been Pending with the bank for approval', 'Import Letter Of Credit Amendment Pending with bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2015', 'en-US', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been Pending with the bank for approval', 'Import Letter Of Credit Amendment Pending with bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2016', 'en-US', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been Pending with the bank for approval', 'Import Letter Of Credit Amendment Pending with bank for Approval');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_APPROVED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_APPROVED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_APPROVED', 'en-US', 'Import Letter Of Credit Amendment Approved', 'Import Letter Of Credit Amendment Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2017', 'en-US', 'IMPORT_LC_AMENDMENT_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC - [#]orderId[/#] has been approved and amended successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2018', 'en-US', 'IMPORT_LC_AMENDMENT_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC - [#]orderId[/#] has been approved and amended successfully', 'Import Letter Of Credit Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2019', 'en-US', 'IMPORT_LC_AMENDMENT_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC - [#]orderId[/#] has been approved and amended successfully', 'Import Letter Of Credit Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2020', 'en-US', 'IMPORT_LC_AMENDMENT_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC - [#]orderId[/#] has been approved and amended successfully', 'Import Letter Of Credit Amendment Approved');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_REJECTED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_REJECTED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_REJECTED', 'en-US', 'Import Letter Of Credit Amendment Rejected', 'Import Letter Of Credit Amendment Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2021', 'en-US', 'IMPORT_LC_AMENDMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2022', 'en-US', 'IMPORT_LC_AMENDMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2023', 'en-US', 'IMPORT_LC_AMENDMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2024', 'en-US', 'IMPORT_LC_AMENDMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Import LC- [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Amendment Rejected');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment Documents has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Documents Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'en-US', 'Import Letter Of Credit Amendment Documents Approved', 'Import Letter Of Credit Amendment Documents Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2025', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2026', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Import Letter Of Credit Amendment Documents Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2027', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Import Letter Of Credit Amendment Documents Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2028', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Import Letter Of Credit Amendment Documents Approved');

-- ALERT SUBTYPE: IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Alert when Import LC Amendment Documents has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'TRADEFINANCE_IMPORTLC_AMENDMENTS', 'Import Letter Of Credit Amendment Documents Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'en-US', 'Import Letter Of Credit Amendment Documents Rejected', 'Import Letter Of Credit Amendment Documents Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF2029', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2030', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Import Letter Of Credit Amendment Documents Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2031', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Import Letter Of Credit Amendment Documents Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF2032', 'en-US', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Import Letter Of Credit Amendment Documents Rejected');

-- ALERT TYPE: TRADEFINANCE_IMPORTLC_DRAWINGS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_IMPORTLC_DRAWINGS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC_DRAWINGS', 'Import Letter of Credit Drawings', 'CUSTOMER', 'Import Letter of Credit Drawings');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_IMPORTLC_DRAWINGS', 'Import Letter of Credit Drawings', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '3');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_IMPORTLC_DRAWINGS', 'en-US', 'Import Letter of Credit Drawings', 'Import Letter of Credit Drawings');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_IMPORTLC_DRAWINGS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_IMPORTLC_DRAWINGS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_IMPORTLC_DRAWINGS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_IMPORTLC_DRAWINGS');

-- ALERT SUBTYPE: IMPORT_LC_DRAWING_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_SUBMITTED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Alert when Import LC Drawing has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_SUBMITTED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Import Letter Of Credit Drawing Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_DRAWING_SUBMITTED', 'en-US', 'Import Letter Of Credit Drawing Submitted', 'Import Letter Of Credit Drawing Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_DRAWING_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_DRAWING_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_DRAWING_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF3001', 'en-US', 'IMPORT_LC_DRAWING_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3002', 'en-US', 'IMPORT_LC_DRAWING_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Import Letter Of Credit Drawing Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3003', 'en-US', 'IMPORT_LC_DRAWING_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Import Letter Of Credit Drawing Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3004', 'en-US', 'IMPORT_LC_DRAWING_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Import Letter Of Credit Drawing Submitted');

-- ALERT SUBTYPE: IMPORT_LC_DRAWING_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_APPROVED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Alert when Import LC Drawing has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_APPROVED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Import Letter Of Credit Drawing Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_DRAWING_APPROVED', 'en-US', 'Import Letter Of Credit Drawing Approved', 'Import Letter Of Credit Drawing Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_DRAWING_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_DRAWING_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_DRAWING_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF3005', 'en-US', 'IMPORT_LC_DRAWING_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3006', 'en-US', 'IMPORT_LC_DRAWING_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Import Letter Of Credit Drawing Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3007', 'en-US', 'IMPORT_LC_DRAWING_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Import Letter Of Credit Drawing Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3008', 'en-US', 'IMPORT_LC_DRAWING_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Import Letter Of Credit Drawing Approved');

-- ALERT SUBTYPE: IMPORT_LC_DRAWING_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_REJECTED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Alert when Import LC Drawing has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('IMPORT_LC_DRAWING_REJECTED', 'TRADEFINANCE_IMPORTLC_DRAWINGS', 'Import Letter Of Credit Drawing Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('IMPORT_LC_DRAWING_REJECTED', 'en-US', 'Import Letter Of Credit Drawing Rejected', 'Import Letter Of Credit Drawing Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'IMPORT_LC_DRAWING_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'IMPORT_LC_DRAWING_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'IMPORT_LC_DRAWING_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF3009', 'en-US', 'IMPORT_LC_DRAWING_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3010', 'en-US', 'IMPORT_LC_DRAWING_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Drawing Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3011', 'en-US', 'IMPORT_LC_DRAWING_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Drawing Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF3012', 'en-US', 'IMPORT_LC_DRAWING_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Import Letter Of Credit Drawing Rejected');

-- ALERT TYPE: TRADEFINANCE_EXPORTLC
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_EXPORTLC');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC', 'Export Letter of Credits', 'CUSTOMER', 'Export Letter of Credits');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_EXPORTLC', 'Export Letter of Credits', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '4');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC', 'en-US', 'Export Letter of Credits', 'Export Letter of Credits');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_EXPORTLC');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_EXPORTLC');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_EXPORTLC');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_EXPORTLC');

-- ALERT SUBTYPE: EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'TRADEFINANCE_EXPORTLC', 'Alert when Export LC Beneficiary Consent has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'TRADEFINANCE_EXPORTLC', 'Export Letter Of Credit Beneficiary Consent Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'en-US', 'Export Letter Of Credit Beneficiary Consent Submitted', 'Export Letter Of Credit Beneficiary Consent Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1601', 'en-US', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Beneficiary consent for Export LC has been submitted Successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1602', 'en-US', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Beneficiary consent for Export LC has been submitted Successfully', 'Export Letter Of Credit Beneficiary Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1603', 'en-US', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Beneficiary consent for Export LC has been submitted Successfully', 'Export Letter Of Credit Beneficiary Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1604', 'en-US', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Beneficiary consent for Export LC has been submitted Successfully', 'Export Letter Of Credit Beneficiary Consent Submitted');

-- ALERT TYPE: TRADEFINANCE_EXPORTLC_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_EXPORTLC_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter of Credit Amendments', 'CUSTOMER', 'Export Letter of Credit Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter of Credit Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '5');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC_AMENDMENTS', 'en-US', 'Export Letter of Credit Amendments', 'Export Letter of Credit Amendments');

INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_EXPORTLC_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_EXPORTLC_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_EXPORTLC_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_EXPORTLC_AMENDMENTS');

-- ALERT SUBTYPE: EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Alert when Export LC Amendment has been Submitted with Self-consent', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter Of Credit Amendment Submitted with Self consent', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'en-US', 'Export Letter Of Credit Amendment Submitted with Self consent', 'Export Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF4001', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4002', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Export Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4003', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Export Letter Of Credit Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4004', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Export Letter Of Credit Amendment Submitted with Self consent');

-- ALERT SUBTYPE: EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Alert when Export LC Amendment has been Submitted to Bank for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter Of Credit Amendment Submitted to Bank for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'en-US', 'Export Letter Of Credit Amendment Submitted to Bank for Approval', 'Export Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF4005', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approval successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4006', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approval successfully', 'Export Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4007', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approval successfully', 'Export Letter Of Credit Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4008', 'en-US', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for approval successfully', 'Export Letter Of Credit Amendment Submitted to Bank for Approval');

-- ALERT SUBTYPE: EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Alert when Export LC Amendment Documents has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter Of Credit Amendment Documents Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'en-US', 'Export Letter Of Credit Amendment Documents Approved', 'Export Letter Of Credit Amendment Documents Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF4009', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4010', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Export Letter Of Credit Amendment Documents Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4011', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Export Letter Of Credit Amendment Documents Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4012', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been approved successfully', 'Export Letter Of Credit Amendment Documents Approved');

-- ALERT SUBTYPE: EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Alert when Export LC Amendment Documents has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'TRADEFINANCE_EXPORTLC_AMENDMENTS', 'Export Letter Of Credit Amendment Documents Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'en-US', 'Export Letter Of Credit Amendment Documents Rejected', 'Export Letter Of Credit Amendment Documents Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF4013', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4014', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Export Letter Of Credit Amendment Documents Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4015', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Export Letter Of Credit Amendment Documents Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF4016', 'en-US', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Export Letter Of Credit Amendment Documents Rejected');

-- ALERT TYPE: TRADEFINANCE_EXPORTLC_DRAWINGS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_EXPORTLC_DRAWINGS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC_DRAWINGS', 'Export Letter of Credit Drawings', 'CUSTOMER', 'Export Letter of Credit Drawings');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_EXPORTLC_DRAWINGS', 'Export Letter of Credit Drawings', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '6');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_EXPORTLC_DRAWINGS', 'en-US', 'Export Letter of Credit Drawings', 'Export Letter of Credit Drawings');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_EXPORTLC_DRAWINGS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_EXPORTLC_DRAWINGS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_EXPORTLC_DRAWINGS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_EXPORTLC_DRAWINGS');

-- ALERT SUBTYPE: EXPORT_LC_DRAWING_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_SUBMITTED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Alert when Export LC Drawing has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_SUBMITTED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Export Letter Of Credit Drawing Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_DRAWING_SUBMITTED', 'en-US', 'Export Letter Of Credit Drawing Submitted', 'Export Letter Of Credit Drawing Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_DRAWING_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_DRAWING_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_DRAWING_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF5001', 'en-US', 'EXPORT_LC_DRAWING_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5002', 'en-US', 'EXPORT_LC_DRAWING_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Export Letter Of Credit Drawing Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5003', 'en-US', 'EXPORT_LC_DRAWING_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Export Letter Of Credit Drawing Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5004', 'en-US', 'EXPORT_LC_DRAWING_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Request for drawings has been submitted successfully', 'Export Letter Of Credit Drawing Submitted');

-- ALERT SUBTYPE: EXPORT_LC_DRAWING_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_APPROVED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Alert when Export LC Drawing has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_APPROVED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Export Letter Of Credit Drawing Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_DRAWING_APPROVED', 'en-US', 'Export Letter Of Credit Drawing Approved', 'Export Letter Of Credit Drawing Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_DRAWING_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_DRAWING_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_DRAWING_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF5005', 'en-US', 'EXPORT_LC_DRAWING_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5006', 'en-US', 'EXPORT_LC_DRAWING_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Export Letter Of Credit Drawing Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5007', 'en-US', 'EXPORT_LC_DRAWING_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Export Letter Of Credit Drawing Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5008', 'en-US', 'EXPORT_LC_DRAWING_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your drawings on - [#]orderId[/#] has been accepted by the bank', 'Export Letter Of Credit Drawing Approved');

-- ALERT SUBTYPE: EXPORT_LC_DRAWING_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_REJECTED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Alert when Export LC Drawing has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('EXPORT_LC_DRAWING_REJECTED', 'TRADEFINANCE_EXPORTLC_DRAWINGS', 'Export Letter Of Credit Drawing Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EXPORT_LC_DRAWING_REJECTED', 'en-US', 'Export Letter Of Credit Drawing Rejected', 'Export Letter Of Credit Drawing Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EXPORT_LC_DRAWING_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EXPORT_LC_DRAWING_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EXPORT_LC_DRAWING_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF5009', 'en-US', 'EXPORT_LC_DRAWING_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5010', 'en-US', 'EXPORT_LC_DRAWING_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Export Letter Of Credit Drawing Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5011', 'en-US', 'EXPORT_LC_DRAWING_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Export Letter Of Credit Drawing Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF5012', 'en-US', 'EXPORT_LC_DRAWING_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your drawings for - [#]orderId[/#] has been rejected by the bank', 'Export Letter Of Credit Drawing Rejected');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_ISSUED
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_ISSUED');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued', 'CUSTOMER', 'Guarantees Issued');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '7');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED', 'en-US', 'Guarantees Issued', 'Guarantees Issued');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_ISSUED');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_ISSUED');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_ISSUED');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_ISSUED');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Alert when Guarantees Issued has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_SUBMITTED', 'en-US', 'Guarantees Issued Submitted', 'Guarantees Issued Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF6001', 'en-US', 'GUARANTEES_ISSUED_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Guarantees/SBLC has been submitted to the bank successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6002', 'en-US', 'GUARANTEES_ISSUED_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Guarantees/SBLC has been submitted to the bank successfully', 'Guarantees Issued Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6003', 'en-US', 'GUARANTEES_ISSUED_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Guarantees/SBLC has been submitted to the bank successfully', 'Guarantees Issued Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6004', 'en-US', 'GUARANTEES_ISSUED_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Guarantees/SBLC has been submitted to the bank successfully', 'Guarantees Issued Submitted');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_APPROVED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Alert when Guarantees Issued has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_APPROVED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_APPROVED', 'en-US', 'Guarantees Issued Approved', 'Guarantees Issued Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF6005', 'en-US', 'GUARANTEES_ISSUED_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC - [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6006', 'en-US', 'GUARANTEES_ISSUED_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6007', 'en-US', 'GUARANTEES_ISSUED_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6008', 'en-US', 'GUARANTEES_ISSUED_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Approved');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Alert when Guarantees Issued has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_REJECTED', 'en-US', 'Guarantees Issued Rejected', 'Guarantees Issued Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF6009', 'en-US', 'GUARANTEES_ISSUED_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6010', 'en-US', 'GUARANTEES_ISSUED_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been rejected', 'Guarantees Issued Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6011', 'en-US', 'GUARANTEES_ISSUED_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been rejected', 'Guarantees Issued Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6012', 'en-US', 'GUARANTEES_ISSUED_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been rejected', 'Guarantees Issued Rejected');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_RETURNED_BY_BANK
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_RETURNED_BY_BANK', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Alert when Guarantees Issued has been Returned by Bank', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_RETURNED_BY_BANK', 'TRADEFINANCE_GUARANTEES_ISSUED', 'Guarantees Issued Returned by Bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_RETURNED_BY_BANK', 'en-US', 'Guarantees Issued Returned by Bank', 'Guarantees Issued Returned by Bank');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_RETURNED_BY_BANK');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_RETURNED_BY_BANK');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_RETURNED_BY_BANK');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_RETURNED_BY_BANK');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF6013', 'en-US', 'GUARANTEES_ISSUED_RETURNED_BY_BANK', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been returned by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6014', 'en-US', 'GUARANTEES_ISSUED_RETURNED_BY_BANK', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been returned by bank', 'Guarantees Issued Returned by Bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6015', 'en-US', 'GUARANTEES_ISSUED_RETURNED_BY_BANK', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been returned by bank', 'Guarantees Issued Returned by Bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF6016', 'en-US', 'GUARANTEES_ISSUED_RETURNED_BY_BANK', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been returned by bank', 'Guarantees Issued Returned by Bank');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendments', 'CUSTOMER', 'Guarantees Issued Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '8');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'en-US', 'Guarantees Issued Amendments', 'Guarantees Issued Amendments');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Alert when Guarantees Issued Amendment has been Submitted to Bank for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendment Submitted to Bank for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'en-US', 'Guarantees Issued Amendment Submitted to Bank for Approval', 'Guarantees Issued Amendment Submitted to Bank for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF7001', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7002', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Issued Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7003', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Issued Amendment Submitted to Bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7004', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Issued Amendment Submitted to Bank for Approval');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Alert when Guarantees Issued Amendment has been Submitted with Self-consent', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendment Submitted with Self consent', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'en-US', 'Guarantees Issued Amendment Submitted with Self consent', 'Guarantees Issued Amendment Submitted with Self consent');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF7005', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7006', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Guarantees Issued Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7007', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Guarantees Issued Amendment Submitted with Self consent');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7008', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Self-consent for the amendment has been submitted successfully', 'Guarantees Issued Amendment Submitted with Self consent');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Alert when Guarantees Issued Amendment was Pending with bank for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendment Pending with bank for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'en-US', 'Guarantees Issued Amendment Pending with bank for Approval', 'Guarantees Issued Amendment Pending with bank for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF7009', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been Pending with the bank for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7010', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been Pending with the bank for approval', 'Guarantees Issued Amendment Pending with bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7011', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been Pending with the bank for approval', 'Guarantees Issued Amendment Pending with bank for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7012', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been Pending with the bank for approval', 'Guarantees Issued Amendment Pending with bank for Approval');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_AMENDMENT_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Alert when Guarantees Issued Amendment has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendment Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'en-US', 'Guarantees Issued Amendment Approved', 'Guarantees Issued Amendment Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF7013', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been approved and amended by bank successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7014', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been approved and amended by bank successfully', 'Guarantees Issued Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7015', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been approved and amended by bank successfully', 'Guarantees Issued Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7016', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] has been approved and amended by bank successfully', 'Guarantees Issued Amendment Approved');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_AMENDMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Alert when Guarantees Issued Amendment has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_AMENDMENTS', 'Guarantees Issued Amendment Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'en-US', 'Guarantees Issued Amendment Rejected', 'Guarantees Issued Amendment Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF7017', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7018', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Issued Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7019', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Issued Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF7020', 'en-US', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Issued Amendment Rejected');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Issued Claims', 'CUSTOMER', 'Guarantees Issued Claims');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Issued Claims', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '9');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'en-US', 'Guarantees Issued Claims', 'Guarantees Issued Claims');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'en-US', 'Guarantees Received Claim Submitted', 'Guarantees Received Claim Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1201', 'en-US', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claim consent has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1202', 'en-US', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claim consent has been submitted successfully', 'Guarantees Received Claim Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1203', 'en-US', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claim consent has been submitted successfully', 'Guarantees Received Claim Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1204', 'en-US', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claim consent has been submitted successfully', 'Guarantees Received Claim Submitted');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Prsentation has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Presentation Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'en-US', 'Guarantees Received Claim Presentation Submitted', 'Guarantees Received Claim Presentation Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1205', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claim consent for presentation has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1206', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claim consent for presentation has been submitted successfully', 'Guarantees Received Claim Presentation Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1207', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claim consent for presentation has been submitted successfully', 'Guarantees Received Claim Presentation Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1208', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claim consent for presentation has been submitted successfully', 'Guarantees Received Claim Presentation Submitted');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'en-US', 'Guarantees Received Claim Accepted', 'Guarantees Received Claim Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1209', 'en-US', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1210', 'en-US', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1211', 'en-US', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1212', 'en-US', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Accepted');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Consent has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Consent Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'en-US', 'Guarantees Received Claim Consent Rejected', 'Guarantees Received Claim Consent Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1213', 'en-US', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1214', 'en-US', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Consent Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1215', 'en-US', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Consent Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1216', 'en-US', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Consent Rejected');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Prsentation has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Presentation Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'en-US', 'Guarantees Received Claim Presentation Accepted', 'Guarantees Received Claim Presentation Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1217', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1218', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Presentation Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1219', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Presentation Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1220', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Claim Presentation Accepted');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Prsentation has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Presentation Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'en-US', 'Guarantees Received Claim Presentation Rejected', 'Guarantees Received Claim Presentation Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1221', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1222', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Presentation Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1223', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Presentation Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1224', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Presentation Rejected');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Prsentation has been Accepted and Settled', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Presentation Accepted and Settled', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'en-US', 'Guarantees Received Claim Presentation Accepted and Settled', 'Guarantees Received Claim Presentation Accepted and Settled');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1225', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settled by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1226', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settled by the bank', 'Guarantees Received Claim Presentation Accepted and Settled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1227', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settled by the bank', 'Guarantees Received Claim Presentation Accepted and Settled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1228', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settled by the bank', 'Guarantees Received Claim Presentation Accepted and Settled');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim Prsentation has been Accepted and Settlement Extended', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Presentation Accepted and Settlement Extended', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'en-US', 'Guarantees Received Claim Presentation Accepted and Settlement Extended', 'Guarantees Received Claim Presentation Accepted and Settlement Extended');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1229', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settlement extend by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1230', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settlement extend by the bank', 'Guarantees Received Claim Presentation Accepted and Settlement Extended');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1231', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settlement extend by the bank', 'Guarantees Received Claim Presentation Accepted and Settlement Extended');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1232', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your consent for the claim presentation - [#]orderId[/#] has been accepted and claim settlement extend by the bank', 'Guarantees Received Claim Presentation Accepted and Settlement Extended');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_PROCESSING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PROCESSING', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Processing', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_PROCESSING', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Processing', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_PROCESSING', 'en-US', 'Guarantees Received Claim Processing', 'Guarantees Received Claim Processing');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_PROCESSING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PROCESSING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_PROCESSING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_PROCESSING');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1233', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PROCESSING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been Processing by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1234', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PROCESSING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been Processing by the bank', 'Guarantees Received Claim Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1235', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PROCESSING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been Processing by the bank', 'Guarantees Received Claim Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1236', 'en-US', 'GUARANTEES_ISSUED_CLAIM_PROCESSING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#]has been Processing by the bank', 'Guarantees Received Claim Processing');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_RETURNED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_RETURNED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Returned', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_RETURNED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Returned', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_RETURNED', 'en-US', 'Guarantees Received Claim Returned', 'Guarantees Received Claim Returned');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_RETURNED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_RETURNED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_RETURNED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_RETURNED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1237', 'en-US', 'GUARANTEES_ISSUED_CLAIM_RETURNED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been returned by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1238', 'en-US', 'GUARANTEES_ISSUED_CLAIM_RETURNED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been returned by the bank', 'Guarantees Received Claim Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1239', 'en-US', 'GUARANTEES_ISSUED_CLAIM_RETURNED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been returned by the bank', 'Guarantees Received Claim Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1240', 'en-US', 'GUARANTEES_ISSUED_CLAIM_RETURNED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been returned by the bank', 'Guarantees Received Claim Returned');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_REJECTED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_REJECTED', 'en-US', 'Guarantees Received Claim Rejected', 'Guarantees Received Claim Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1241', 'en-US', 'GUARANTEES_ISSUED_CLAIM_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1242', 'en-US', 'GUARANTEES_ISSUED_CLAIM_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1243', 'en-US', 'GUARANTEES_ISSUED_CLAIM_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1244', 'en-US', 'GUARANTEES_ISSUED_CLAIM_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Claim Rejected');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Extended for Payment', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Extended for Payment', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'en-US', 'Guarantees Received Claim Extended for Payment', 'Guarantees Received Claim Extended for Payment');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1245', 'en-US', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been extended for payment by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1246', 'en-US', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been extended for payment by the bank', 'Guarantees Received Claim Extended for Payment');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1247', 'en-US', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been extended for payment by the bank', 'Guarantees Received Claim Extended for Payment');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1248', 'en-US', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been extended for payment by the bank', 'Guarantees Received Claim Extended for Payment');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Honoured by Bank but Rejected by Applicant', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'en-US', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1249', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been honoured by the bank but rejected by the applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1250', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been honoured by the bank but rejected by the applicant', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1251', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been honoured by the bank but rejected by the applicant', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1252', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims received - [#]orderId[/#] has been honoured by the bank but rejected by the applicant', 'Guarantees Received Claim Honoured by Bank but Rejected by Applicant');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Honoured by Bank but Pending Consent by Applicant', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'en-US', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1253', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank but consent was pending by the applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1254', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank but consent was pending by the applicant', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1255', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank but consent was pending by the applicant', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1256', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank but consent was pending by the applicant', 'Guarantees Received Claim Honoured by Bank but Pending Consent by Applicant');

-- ALERT SUBTYPE: GUARANTEES_ISSUED_CLAIM_HONOURED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Alert when Guarantees Received Claim has been Honoured by Bank but Pending Consent by Applicant', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED', 'TRADEFINANCE_GUARANTEES_ISSUED_CLAIMS', 'Guarantees Received Claim Honoured', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_ISSUED_CLAIM_HONOURED', 'en-US', 'Guarantees Received Claim Honoured', 'Guarantees Received Claim Honoured');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_ISSUED_CLAIM_HONOURED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_ISSUED_CLAIM_HONOURED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_ISSUED_CLAIM_HONOURED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1257', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1258', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank successfully', 'Guarantees Received Claim Honoured');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1259', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank successfully', 'Guarantees Received Claim Honoured');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1260', 'en-US', 'GUARANTEES_ISSUED_CLAIM_HONOURED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claim received - [#]orderId[/#] has been honoured by the bank successfully', 'Guarantees Received Claim Honoured');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_RECEIVED
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_RECEIVED');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received', 'CUSTOMER', 'Guarantees Received');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '10');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED', 'en-US', 'Guarantees Received', 'Guarantees Received');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_RECEIVED');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_RECEIVED');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_RECEIVED');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_RECEIVED');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Alert when Guarantees Received has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_SUBMITTED', 'en-US', 'Guarantees Received Submitted', 'Guarantees Received Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF9001', 'en-US', 'GUARANTEES_RECEIVED_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Self-consent for the acceptance/rejection for Guarantee/SBLC has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9002', 'en-US', 'GUARANTEES_RECEIVED_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Self-consent for the acceptance/rejection for Guarantee/SBLC has been submitted successfully', 'Guarantees Received Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9003', 'en-US', 'GUARANTEES_RECEIVED_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Self-consent for the acceptance/rejection for Guarantee/SBLC has been submitted successfully', 'Guarantees Received Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9004', 'en-US', 'GUARANTEES_RECEIVED_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Self-consent for the acceptance/rejection for Guarantee/SBLC has been submitted successfully', 'Guarantees Received Submitted');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Alert when Guarantees Received has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_ACCEPTED', 'en-US', 'Guarantees Received Accepted', 'Guarantees Received Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF9005', 'en-US', 'GUARANTEES_RECEIVED_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9006', 'en-US', 'GUARANTEES_RECEIVED_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9007', 'en-US', 'GUARANTEES_RECEIVED_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9008', 'en-US', 'GUARANTEES_RECEIVED_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been accepted by the bank', 'Guarantees Received Accepted');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_REJECETED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_REJECETED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Alert when Guarantees Received has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_REJECETED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_REJECETED', 'en-US', 'Guarantees Received Rejected', 'Guarantees Received Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_REJECETED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_REJECETED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_REJECETED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_REJECETED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF9009', 'en-US', 'GUARANTEES_RECEIVED_REJECETED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9010', 'en-US', 'GUARANTEES_RECEIVED_REJECETED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9011', 'en-US', 'GUARANTEES_RECEIVED_REJECETED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9012', 'en-US', 'GUARANTEES_RECEIVED_REJECETED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Consent on Guarantee/SBLC received- [#]orderId[/#] has been rejected by the bank', 'Guarantees Received Rejected');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Alert when Guarantees Received has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_APPROVED', 'en-US', 'Guarantees Received Approved', 'Guarantees Received Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF9013', 'en-US', 'GUARANTEES_RECEIVED_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC received - [#]orderId[/#] has been approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9014', 'en-US', 'GUARANTEES_RECEIVED_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC received - [#]orderId[/#] has been approved by the bank', 'Guarantees Received Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9015', 'en-US', 'GUARANTEES_RECEIVED_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC received - [#]orderId[/#] has been approved by the bank', 'Guarantees Received Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9016', 'en-US', 'GUARANTEES_RECEIVED_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC received - [#]orderId[/#] has been approved by the bank', 'Guarantees Received Approved');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_PENDING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Alert when Guarantees Received has been Pending', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED', 'Guarantees Received Pending', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_PENDING', 'en-US', 'Guarantees Received Pending', 'Guarantees Received Pending');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_PENDING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_PENDING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_PENDING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_PENDING');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF9017', 'en-US', 'GUARANTEES_RECEIVED_PENDING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC- [#]orderId[/#] received has been pending with the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9018', 'en-US', 'GUARANTEES_RECEIVED_PENDING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC- [#]orderId[/#] received has been pending with the bank', 'Guarantees Received Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9019', 'en-US', 'GUARANTEES_RECEIVED_PENDING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC- [#]orderId[/#] received has been pending with the bank', 'Guarantees Received Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF9020', 'en-US', 'GUARANTEES_RECEIVED_PENDING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantee/SBLC- [#]orderId[/#] received has been pending with the bank', 'Guarantees Received Pending');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendments', 'CUSTOMER', 'Guarantees Received Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '11');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'en-US', 'Guarantees Received Amendments', 'Guarantees Received Amendments');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Alert when Guarantees Received Amendment has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendment Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'en-US', 'Guarantees Received Amendment Submitted', 'Guarantees Received Amendment Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1101', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1102', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Received Amendment Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1103', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Received Amendment Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1104', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Amendment has been submitted successfully for bank approval', 'Guarantees Received Amendment Submitted');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_AMENDMENT_PENDING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Alert when Guarantees Received Amendment has been Pending', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendment Pending', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'en-US', 'Guarantees Received Amendment Pending', 'Guarantees Received Amendment Pending');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1105', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1106', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1107', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1108', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Pending');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_AMENDMENT_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Alert when Guarantees Received Amendment has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendment Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'en-US', 'Guarantees Received Amendment Approved', 'Guarantees Received Amendment Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1109', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1110', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1111', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1112', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC- [#]orderId[/#] for amendment has been Pending with the bank for approval', 'Guarantees Received Amendment Approved');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_AMENDMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Alert when Guarantees Received Amendment has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendment Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'en-US', 'Guarantees Received Amendment Rejected', 'Guarantees Received Amendment Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1113', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1114', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Received Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1115', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Received Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1116', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC for amendment- [#]orderId[/#] has been rejected by the issuing bank', 'Guarantees Received Amendment Rejected');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Alert when Guarantees Received Amendment has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_AMENDMENTS', 'Guarantees Received Amendment Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'en-US', 'Guarantees Received Amendment Accepted', 'Guarantees Received Amendment Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1117', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC amendment - [#]orderId[/#] has been accepted a by bene bank ');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1118', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC amendment - [#]orderId[/#] has been accepted a by bene bank', 'Guarantees Received Amendment Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1119', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC amendment - [#]orderId[/#] has been accepted a by bene bank', 'Guarantees Received Amendment Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1120', 'en-US', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Guarantees/SBLC amendment - [#]orderId[/#] has been accepted a by bene bank', 'Guarantees Received Amendment Accepted');

-- ALERT TYPE: TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Received Claims', 'CUSTOMER', 'Guarantees Received Claims');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Received Claims', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '12');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'en-US', 'Guarantees Received Claims', 'Guarantees Received Claims');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'en-US', 'Guarantees Issued Claim Submitted', 'Guarantees Issued Claim Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8001', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Request for claim has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8002', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Request for claim has been submitted successfully', 'Guarantees Issued Claim Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8003', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Request for claim has been submitted successfully', 'Guarantees Issued Claim Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8004', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Request for claim has been submitted successfully', 'Guarantees Issued Claim Submitted');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document has been Submitted for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Submitted for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'en-US', 'Guarantees Issued Claim Document Submitted for Approval', 'Guarantees Issued Claim Document Submitted for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8005', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for claim approval successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8006', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for claim approval successfully', 'Guarantees Issued Claim Document Submitted for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8007', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for claim approval successfully', 'Guarantees Issued Claim Document Submitted for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8008', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank for claim approval successfully', 'Guarantees Issued Claim Document Submitted for Approval');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document has been Re-Submitted for Approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Re-Submitted for Approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'en-US', 'Guarantees Issued Claim Document Re-Submitted for Approval', 'Guarantees Issued Claim Document Re-Submitted for Approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8009', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Re-submission of documents for claim has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8010', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Re-submission of documents for claim has been submitted successfully', 'Guarantees Issued Claim Document Re-Submitted for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8011', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Re-submission of documents for claim has been submitted successfully', 'Guarantees Issued Claim Document Re-Submitted for Approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8012', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Re-submission of documents for claim has been submitted successfully', 'Guarantees Issued Claim Document Re-Submitted for Approval');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Accepted and Claim Honoured', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Accepted and Claim Honoured', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'en-US', 'Guarantees Issued Claim Accepted and Claim Honoured', 'Guarantees Issued Claim Accepted and Claim Honoured');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8013', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been accepted & claim honoured by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8014', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been accepted & claim honoured by the bank', 'Guarantees Issued Claim Accepted and Claim Honoured');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8015', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been accepted & claim honoured by the bank', 'Guarantees Issued Claim Accepted and Claim Honoured');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8016', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been accepted & claim honoured by the bank', 'Guarantees Issued Claim Accepted and Claim Honoured');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Declined and Closed', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Declined and Closed', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'en-US', 'Guarantees Issued Claim Declined and Closed', 'Guarantees Issued Claim Declined and Closed');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8017', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been declined & closed by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8018', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been declined & closed by the bank', 'Guarantees Issued Claim Declined and Closed');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8019', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been declined & closed by the bank', 'Guarantees Issued Claim Declined and Closed');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8020', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been declined & closed by the bank', 'Guarantees Issued Claim Declined and Closed');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_PENDING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Pending', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_PENDING', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Pending', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_PENDING', 'en-US', 'Guarantees Issued Claim Pending', 'Guarantees Issued Claim Pending');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_PENDING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_PENDING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_PENDING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_PENDING');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8021', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_PENDING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in pending for acceptance with the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8022', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_PENDING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in pending for acceptance with the bank', 'Guarantees Issued Claim Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8023', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_PENDING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in pending for acceptance with the bank', 'Guarantees Issued Claim Pending');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8024', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_PENDING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in pending for acceptance with the bank', 'Guarantees Issued Claim Pending');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_ACTIVE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Active', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Active', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'en-US', 'Guarantees Issued Claim Active', 'Guarantees Issued Claim Active');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8025', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in Active/Inprogress with the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8026', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in Active/Inprogress with the bank', 'Guarantees Issued Claim Active');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8027', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in Active/Inprogress with the bank', 'Guarantees Issued Claim Active');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8028', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been in Active/Inprogress with the bank', 'Guarantees Issued Claim Active');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_INACTIVE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Inactive', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Inactive', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'en-US', 'Guarantees Issued Claim Inactive', 'Guarantees Issued Claim Inactive');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8029', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Inactive with the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8030', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Inactive with the bank', 'Guarantees Issued Claim Inactive');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8031', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Inactive with the bank', 'Guarantees Issued Claim Inactive');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8032', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Inactive with the bank', 'Guarantees Issued Claim Inactive');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_APPROVED', 'en-US', 'Guarantees Issued Claim Approved', 'Guarantees Issued Claim Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8033', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8034', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Approved by the bank', 'Guarantees Issued Claim Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8035', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Approved by the bank', 'Guarantees Issued Claim Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8036', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your claims for - [#]orderId[/#] has been Approved by the bank', 'Guarantees Issued Claim Approved');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document Submitted has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Submitted was Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'en-US', 'Guarantees Issued Claim Document Submitted was Approved', 'Guarantees Issued Claim Document Submitted was Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8037', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for claim - [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8038', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for claim - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Claim Document Submitted was Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8039', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for claim - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Claim Document Submitted was Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8040', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for claim - [#]orderId[/#] has been approved successfully', 'Guarantees Issued Claim Document Submitted was Approved');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document Submitted has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Submitted was Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'en-US', 'Guarantees Issued Claim Document Submitted was Rejected', 'Guarantees Issued Claim Document Submitted was Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8041', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8042', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Guarantees Issued Claim Document Submitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8043', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Guarantees Issued Claim Document Submitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8044', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for - [#]orderId[/#] has been rejected', 'Guarantees Issued Claim Document Submitted was Rejected');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document Submitted has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Submitted was Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'en-US', 'Guarantees Issued Claim Document Submitted was Accepted', 'Guarantees Issued Claim Document Submitted was Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8045', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8046', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been accepted by the bank', 'Guarantees Issued Claim Document Submitted was Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8047', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been accepted by the bank', 'Guarantees Issued Claim Document Submitted was Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8048', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been accepted by the bank', 'Guarantees Issued Claim Document Submitted was Accepted');

-- ALERT SUBTYPE: GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Alert when Guarantees Issued Claim Document Resubmitted has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'TRADEFINANCE_GUARANTEES_RECEIVED_CLAIMS', 'Guarantees Issued Claim Document Resubmitted was Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'en-US', 'Guarantees Issued Claim Document Resubmitted was Rejected', 'Guarantees Issued Claim Document Resubmitted was Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF8049', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8050', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been rejected by the bank', 'Guarantees Issued Claim Document Resubmitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8051', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been rejected by the bank', 'Guarantees Issued Claim Document Resubmitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF8052', 'en-US', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for - [#]orderId[/#] has been rejected by the bank', 'Guarantees Issued Claim Document Resubmitted was Rejected');

-- ALERT TYPE: TRADEFINANCE_INWARD_COLLECTIONS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_INWARD_COLLECTIONS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collections', 'CUSTOMER', 'Inward Collections');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collections', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '13');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_INWARD_COLLECTIONS', 'en-US', 'Inward Collections', 'Inward Collections');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_INWARD_COLLECTIONS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_INWARD_COLLECTIONS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_INWARD_COLLECTIONS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_INWARD_COLLECTIONS');

-- ALERT SUBTYPE: INWARD_COLLECTION_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_SUBMITTED', 'en-US', 'Inward Collection Submitted', 'Inward Collection Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1301', 'en-US', 'INWARD_COLLECTION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1302', 'en-US', 'INWARD_COLLECTION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been submitted successfully', 'Inward Collection Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1303', 'en-US', 'INWARD_COLLECTION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been submitted successfully', 'Inward Collection Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1304', 'en-US', 'INWARD_COLLECTION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been submitted successfully', 'Inward Collection Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_CANCELLED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_CANCELLED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection has been Cancelled', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_CANCELLED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Cancelled', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_CANCELLED', 'en-US', 'Inward Collection Cancelled', 'Inward Collection Cancelled');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_CANCELLED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_CANCELLED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_CANCELLED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_CANCELLED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1305', 'en-US', 'INWARD_COLLECTION_CANCELLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1306', 'en-US', 'INWARD_COLLECTION_CANCELLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been cancelled', 'Inward Collection Cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1307', 'en-US', 'INWARD_COLLECTION_CANCELLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been cancelled', 'Inward Collection Cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1308', 'en-US', 'INWARD_COLLECTION_CANCELLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Request for new inward collection has been cancelled', 'Inward Collection Cancelled');

-- ALERT SUBTYPE: INWARD_COLLECTION_CONSENT_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_CONSENT_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Consent has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_CONSENT_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Consent Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_CONSENT_SUBMITTED', 'en-US', 'Inward Collection Consent Submitted', 'Inward Collection Consent Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_CONSENT_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_CONSENT_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1309', 'en-US', 'INWARD_COLLECTION_CONSENT_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your collection consent has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1310', 'en-US', 'INWARD_COLLECTION_CONSENT_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your collection consent has been submitted successfully', 'Inward Collection Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1311', 'en-US', 'INWARD_COLLECTION_CONSENT_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your collection consent has been submitted successfully', 'Inward Collection Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1312', 'en-US', 'INWARD_COLLECTION_CONSENT_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your collection consent has been submitted successfully', 'Inward Collection Consent Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Usance Acceptance has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Usance Acceptance Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'en-US', 'Inward Collection Usance Acceptance Submitted', 'Inward Collection Usance Acceptance Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1313', 'en-US', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Usance acceptance for collection has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1314', 'en-US', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Usance acceptance for collection has been submitted successfully', 'Inward Collection Usance Acceptance Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1315', 'en-US', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Usance acceptance for collection has been submitted successfully', 'Inward Collection Usance Acceptance Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1316', 'en-US', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Usance acceptance for collection has been submitted successfully', 'Inward Collection Usance Acceptance Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Payment Initiation has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Payment Initiation Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'en-US', 'Inward Collection Payment Initiation Submitted', 'Inward Collection Payment Initiation Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1317', 'en-US', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your payment initiation has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1318', 'en-US', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your payment initiation has been submitted successfully', 'Inward Collection Payment Initiation Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1319', 'en-US', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your payment initiation has been submitted successfully', 'Inward Collection Payment Initiation Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1320', 'en-US', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your payment initiation has been submitted successfully', 'Inward Collection Payment Initiation Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_REQUEST_RESUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Resubmitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Request Resubmitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED', 'en-US', 'Inward Collection Request Resubmitted', 'Inward Collection Request Resubmitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_REQUEST_RESUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_REQUEST_RESUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_REQUEST_RESUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1321', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Re-submission of request for inward collection has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1322', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Re-submission of request for inward collection has been submitted successfully', 'Inward Collection Request Resubmitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1323', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Re-submission of request for inward collection has been submitted successfully', 'Inward Collection Request Resubmitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1324', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Re-submission of request for inward collection has been submitted successfully', 'Inward Collection Request Resubmitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_PROCESSING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PROCESSING', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Processing', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PROCESSING', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Processing', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PROCESSING', 'en-US', 'Inward Collection Processing', 'Inward Collection Processing');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PROCESSING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PROCESSING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PROCESSING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PROCESSING');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1325', 'en-US', 'INWARD_COLLECTION_PROCESSING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been processing by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1326', 'en-US', 'INWARD_COLLECTION_PROCESSING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been processing by bank', 'Inward Collection Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1327', 'en-US', 'INWARD_COLLECTION_PROCESSING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been processing by bank', 'Inward Collection Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1328', 'en-US', 'INWARD_COLLECTION_PROCESSING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been processing by bank', 'Inward Collection Processing');

-- ALERT SUBTYPE: INWARD_COLLECTION_RETURNED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_RETURNED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Returned', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_RETURNED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Returned', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_RETURNED', 'en-US', 'Inward Collection Returned', 'Inward Collection Returned');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_RETURNED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_RETURNED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_RETURNED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_RETURNED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1329', 'en-US', 'INWARD_COLLECTION_RETURNED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been returned by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1330', 'en-US', 'INWARD_COLLECTION_RETURNED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been returned by bank', 'Inward Collection Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1331', 'en-US', 'INWARD_COLLECTION_RETURNED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been returned by bank', 'Inward Collection Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1332', 'en-US', 'INWARD_COLLECTION_RETURNED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been returned by bank', 'Inward Collection Returned');

-- ALERT SUBTYPE: INWARD_COLLECTION_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_REJECTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_REJECTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_REJECTED', 'en-US', 'Inward Collection Rejected', 'Inward Collection Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1333', 'en-US', 'INWARD_COLLECTION_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been rejected by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1334', 'en-US', 'INWARD_COLLECTION_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been rejected by bank', 'Inward Collection Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1335', 'en-US', 'INWARD_COLLECTION_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been rejected by bank', 'Inward Collection Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1336', 'en-US', 'INWARD_COLLECTION_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been rejected by bank', 'Inward Collection Rejected');

-- ALERT SUBTYPE: INWARD_COLLECTION_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_APPROVED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_APPROVED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_APPROVED', 'en-US', 'Inward Collection Approved', 'Inward Collection Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1337', 'en-US', 'INWARD_COLLECTION_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been approved by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1338', 'en-US', 'INWARD_COLLECTION_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been approved by bank', 'Inward Collection Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1339', 'en-US', 'INWARD_COLLECTION_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been approved by bank', 'Inward Collection Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1340', 'en-US', 'INWARD_COLLECTION_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been approved by bank', 'Inward Collection Approved');

-- ALERT SUBTYPE: INWARD_COLLECTION_SETTLED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_SETTLED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Settled', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_SETTLED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Settled', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_SETTLED', 'en-US', 'Inward Collection Settled', 'Inward Collection Settled');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_SETTLED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SETTLED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_SETTLED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_SETTLED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1341', 'en-US', 'INWARD_COLLECTION_SETTLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been settled by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1342', 'en-US', 'INWARD_COLLECTION_SETTLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been settled by bank', 'Inward Collection Settled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1343', 'en-US', 'INWARD_COLLECTION_SETTLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been settled by bank', 'Inward Collection Settled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1344', 'en-US', 'INWARD_COLLECTION_SETTLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been settled by bank', 'Inward Collection Settled');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYDUE_OR_OVERDUE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request has been Paydue/ Overdue', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Paydue Or Overdue', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'en-US', 'Inward Collection Paydue Or Overdue', 'Inward Collection Paydue Or Overdue');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1345', 'en-US', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been Pay due/over due by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1346', 'en-US', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been Pay due/over due by bank', 'Inward Collection Paydue Or Overdue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1347', 'en-US', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been Pay due/over due by bank', 'Inward Collection Paydue Or Overdue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1348', 'en-US', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your new inward collection request - [#]orderId[/#] has been Pay due/over due by bank', 'Inward Collection Paydue Or Overdue');

-- ALERT SUBTYPE: INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request Resubmitted has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Request Resubmitted was Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'en-US', 'Inward Collection Request Resubmitted was Accepted', 'Inward Collection Request Resubmitted was Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1349', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been accepted by the bank ');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1350', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Request Resubmitted was Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1351', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Request Resubmitted was Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1352', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Request Resubmitted was Accepted');

-- ALERT SUBTYPE: INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection Request Resubmitted has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Request Resubmitted was Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'en-US', 'Inward Collection Request Resubmitted was Rejected', 'Inward Collection Request Resubmitted was Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1353', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1354', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Request Resubmitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1355', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Request Resubmitted was Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1356', 'en-US', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your re submitted request for inward collection for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Request Resubmitted was Rejected');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYDUE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection was Paydue', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Paydue', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYDUE', 'en-US', 'Inward Collection Paydue', 'Inward Collection Paydue');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYDUE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYDUE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYDUE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1357', 'en-US', 'INWARD_COLLECTION_PAYDUE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in paydue status');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1358', 'en-US', 'INWARD_COLLECTION_PAYDUE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in paydue status', 'Inward Collection Paydue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1359', 'en-US', 'INWARD_COLLECTION_PAYDUE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in paydue status', 'Inward Collection Paydue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1360', 'en-US', 'INWARD_COLLECTION_PAYDUE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in paydue status', 'Inward Collection Paydue');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection was in Paydue with last 5 days and two notifications of last day', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Paydue with Last Five Days', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'en-US', 'Inward Collection Paydue with Last Five Days', 'Inward Collection Paydue with Last Five Days');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1361', 'en-US', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in pay due with [#]daysLeft[/#] days left');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1362', 'en-US', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in pay due with [#]daysLeft[/#] days left', 'Inward Collection Paydue with Last Five Days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1363', 'en-US', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in pay due with [#]daysLeft[/#] days left', 'Inward Collection Paydue with Last Five Days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1364', 'en-US', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been in pay due with [#]daysLeft[/#] days left', 'Inward Collection Paydue with Last Five Days');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYDUE_ON_LAST_DAY
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection was in Paydue on Last Day', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Paydue on Last Day', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'en-US', 'Inward Collection Paydue on Last Day', 'Inward Collection Paydue on Last Day');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1365', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your inward collection has been paydue on last day');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1366', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your inward collection has been paydue on last day', 'Inward Collection Paydue on Last Day');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1367', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your inward collection has been paydue on last day', 'Inward Collection Paydue on Last Day');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1368', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your inward collection has been paydue on last day', 'Inward Collection Paydue on Last Day');

-- ALERT SUBTYPE: INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection was in Paydue on Last Three Odd Days', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Paydue on Last Three Odd Days', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'en-US', 'Inward Collection Paydue on Last Three Odd Days', 'Inward Collection Paydue on Last Three Odd Days');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1369', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been overdue by [#]daysLeft[/#] days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1370', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Inward Collection Paydue on Last Three Odd Days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1371', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Inward Collection Paydue on Last Three Odd Days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1372', 'en-US', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Inward Collection Paydue on Last Three Odd Days');

-- ALERT SUBTYPE: INWARD_COLLECTION_SETTLED_FROM_PAYDUE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Alert when Inward Collection was Settled from Paydue', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'TRADEFINANCE_INWARD_COLLECTIONS', 'Inward Collection Settled from Paydue', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'en-US', 'Inward Collection Settled from Paydue', 'Inward Collection Settled from Paydue');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1373', 'en-US', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been settled from paydue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1374', 'en-US', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been settled from paydue', 'Inward Collection Settled from Paydue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1375', 'en-US', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been settled from paydue', 'Inward Collection Settled from Paydue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1376', 'en-US', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your inward collection for - [#]orderId[/#] has been settled from paydue', 'Inward Collection Settled from Paydue');

-- ALERT TYPE: TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendments', 'CUSTOMER', 'Inward Collection Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '14');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'en-US', 'Inward Collection Amendments', 'Inward Collection Amendments');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS');

-- ALERT SUBTYPE: INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Alert when Inward Collection Amendment Consent has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendment Consent Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'en-US', 'Inward Collection Amendment Consent Submitted', 'Inward Collection Amendment Consent Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1401', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your amendment consent has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1402', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your amendment consent has been submitted successfully', 'Inward Collection Amendment Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1403', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your amendment consent has been submitted successfully', 'Inward Collection Amendment Consent Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1404', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your amendment consent has been submitted successfully', 'Inward Collection Amendment Consent Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Alert when Inward Collection Amendment Cancel Request has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendment Cancel Request Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'en-US', 'Inward Collection Amendment Cancel Request Submitted', 'Inward Collection Amendment Cancel Request Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1405', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for cancel amendment has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1406', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for cancel amendment has been submitted successfully', 'Inward Collection Amendment Cancel Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1407', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for cancel amendment has been submitted successfully', 'Inward Collection Amendment Cancel Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1408', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for cancel amendment has been submitted successfully', 'Inward Collection Amendment Cancel Request Submitted');

-- ALERT SUBTYPE: INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Alert when Inward Collection Amendment Cancel Request has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendment Cancel Request Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'en-US', 'Inward Collection Amendment Cancel Request Accepted', 'Inward Collection Amendment Cancel Request Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1409', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1410', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Amendment Cancel Request Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1411', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Amendment Cancel Request Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1412', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been accepted by the bank', 'Inward Collection Amendment Cancel Request Accepted');

-- ALERT SUBTYPE: INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Alert when Inward Collection Amendment Cancel Request has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'TRADEFINANCE_INWARD_COLLECTION_AMENDMENTS', 'Inward Collection Amendment Cancel Request Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'en-US', 'Inward Collection Amendment Cancel Request Rejected', 'Inward Collection Amendment Cancel Request Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1413', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1414', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Amendment Cancel Request Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1415', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Amendment Cancel Request Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1416', 'en-US', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the cancellation of amendment for - [#]orderId[/#] has been rejected by the bank', 'Inward Collection Amendment Cancel Request Rejected');

-- ALERT TYPE: TRADEFINANCE_OUTWARD_COLLECTIONS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_OUTWARD_COLLECTIONS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collections', 'CUSTOMER', 'Outward Collections');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collections', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '15');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTIONS', 'en-US', 'Outward Collections', 'Outward Collections');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_OUTWARD_COLLECTIONS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_OUTWARD_COLLECTIONS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_OUTWARD_COLLECTIONS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_OUTWARD_COLLECTIONS');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_DOCUMENT_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Document has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Document Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'en-US', 'Outward Collection Document Submitted', 'Outward Collection Document Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1501', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank successfully for outward collection');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1502', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank successfully for outward collection', 'Outward Collection Document Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1503', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank successfully for outward collection', 'Outward Collection Document Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1504', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Documents submitted to the bank successfully for outward collection', 'Outward Collection Document Submitted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Document has been Resubmitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Document Resubmitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'en-US', 'Outward Collection Document Resubmitted', 'Outward Collection Document Resubmitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1505', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Re-submission of documents for outward collection incase of return has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1506', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Re-submission of documents for outward collection incase of return has been submitted successfully', 'Outward Collection Document Resubmitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1507', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Re-submission of documents for outward collection incase of return has been submitted successfully', 'Outward Collection Document Resubmitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1508', 'en-US', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Re-submission of documents for outward collection incase of return has been submitted successfully', 'Outward Collection Document Resubmitted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Submitted Document has been Approved', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Submitted Document Approved', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'en-US', 'Outward Collection Submitted Document Approved', 'Outward Collection Submitted Document Approved');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1509', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been approved successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1510', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been approved successfully', 'Outward Collection Submitted Document Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1511', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been approved successfully', 'Outward Collection Submitted Document Approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1512', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been approved successfully', 'Outward Collection Submitted Document Approved');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Submitted Document has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Submitted Document Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'en-US', 'Outward Collection Submitted Document Rejected', 'Outward Collection Submitted Document Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1513', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1514', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been rejected', 'Outward Collection Submitted Document Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1515', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been rejected', 'Outward Collection Submitted Document Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1516', 'en-US', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Documents submitted to the bank for outward collection [#]orderId[/#] has been rejected', 'Outward Collection Submitted Document Rejected');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Resubmitted Document has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Resubmitted Document Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'en-US', 'Outward Collection Resubmitted Document Accepted', 'Outward Collection Resubmitted Document Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1517', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1518', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been accepted by the bank', 'Outward Collection Resubmitted Document Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1519', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been accepted by the bank', 'Outward Collection Resubmitted Document Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1520', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been accepted by the bank', 'Outward Collection Resubmitted Document Accepted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection Resubmitted Document has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Resubmitted Document Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'en-US', 'Outward Collection Resubmitted Document Rejected', 'Outward Collection Resubmitted Document Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1521', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1522', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been rejected by the bank', 'Outward Collection Resubmitted Document Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1523', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been rejected by the bank', 'Outward Collection Resubmitted Document Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1524', 'en-US', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Re submitted documents for outward collection [#]orderId[/#] has been rejected by the bank', 'Outward Collection Resubmitted Document Rejected');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_OVERDUE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_OVERDUE', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Alert when Outward Collection has been Overdue', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_OVERDUE', 'TRADEFINANCE_OUTWARD_COLLECTIONS', 'Outward Collection Overdue', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_OVERDUE', 'en-US', 'Outward Collection Overdue', 'Outward Collection Overdue');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_OVERDUE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_OVERDUE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_OVERDUE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_OVERDUE');
 
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1525', 'en-US', 'OUTWARD_COLLECTION_OVERDUE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your outward collection for [#]orderId[/#] has been overdue by [#]daysLeft[/#] days');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1526', 'en-US', 'OUTWARD_COLLECTION_OVERDUE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your outward collection for [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Outward Collection Overdue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1527', 'en-US', 'OUTWARD_COLLECTION_OVERDUE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your outward collection for [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Outward Collection Overdue');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1528', 'en-US', 'OUTWARD_COLLECTION_OVERDUE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your outward collection for [#]orderId[/#] has been overdue by [#]daysLeft[/#] days', 'Outward Collection Overdue');

-- ALERT TYPE: TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendments', 'CUSTOMER', 'Outward Collection Amendments');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendments', 'ALERT_CAT_TRADEFINANCE', '0', 'SID_ACTIVE', '1', '16');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'en-US', 'Outward Collection Amendments', 'Outward Collection Amendments');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Amendment Request has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendment Request Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'en-US', 'Outward Collection Amendment Request Submitted', 'Outward Collection Amendment Request Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1701', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Request for amendment has been submitted Successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1702', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Request for amendment has been submitted Successfully', 'Outward Collection Amendment Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1703', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Request for amendment has been submitted Successfully', 'Outward Collection Amendment Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1704', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Request for amendment has been submitted Successfully', 'Outward Collection Amendment Request Submitted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Revised Amendment Request has been Submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Revised Amendment Request Submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'en-US', 'Outward Collection Revised Amendment Request Submitted', 'Outward Collection Revised Amendment Request Submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1705', 'en-US', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Request for revised amendment has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1706', 'en-US', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Request for revised amendment has been submitted successfully', 'Outward Collection Revised Amendment Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1707', 'en-US', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Request for revised amendment has been submitted successfully', 'Outward Collection Revised Amendment Request Submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1708', 'en-US', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Request for revised amendment has been submitted successfully', 'Outward Collection Revised Amendment Request Submitted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_AMENDMENT_ACCEPTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Amendment has been Accepted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendment Accepted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'en-US', 'Outward Collection Amendment Accepted', 'Outward Collection Amendment Accepted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1709', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been accepted by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1710', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been accepted by the bank', 'Outward Collection Amendment Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1711', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been accepted by the bank', 'Outward Collection Amendment Accepted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1712', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been accepted by the bank', 'Outward Collection Amendment Accepted');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_AMENDMENT_RETURNED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Amendment has been Returned', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendment Returned', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'en-US', 'Outward Collection Amendment Returned', 'Outward Collection Amendment Returned');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1713', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Returned by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1714', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Returned by the bank', 'Outward Collection Amendment Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1715', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Returned by the bank', 'Outward Collection Amendment Returned');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1716', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Returned by the bank', 'Outward Collection Amendment Returned');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_AMENDMENT_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Amendment has been Rejected', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendment Rejected', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'en-US', 'Outward Collection Amendment Rejected', 'Outward Collection Amendment Rejected');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1717', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1718', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Rejected by the bank', 'Outward Collection Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1719', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Rejected by the bank', 'Outward Collection Amendment Rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1720', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#] has been Rejected by the bank', 'Outward Collection Amendment Rejected');

-- ALERT SUBTYPE: OUTWARD_COLLECTION_AMENDMENT_PROCESSING
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Alert when Outward Collection Amendment has been Processing', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'TRADEFINANCE_OUTWARD_COLLECTION_AMENDMENTS', 'Outward Collection Amendment Processing', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'en-US', 'Outward Collection Amendment Processing', 'Outward Collection Amendment Processing');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TF1721', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#]has been Processing by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1722', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#]has been Processing by the bank', 'Outward Collection Amendment Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1723', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#]has been Processing by the bank', 'Outward Collection Amendment Processing');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TF1724', 'en-US', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your request for the amendment for - [#]orderId[/#]has been Processing by the bank', 'Outward Collection Amendment Processing');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IMLC_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'IMPORT_LC_CHART_DATA', 'Import LC Chart Data', '[{\"DisplayStatus\":\"Pending Requests\",\"LCStatus\":[\"Draft\",\"Submitted to Bank\",\"Processing by Bank\",\"Returned by Bank\"]},{\"DisplayStatus\":\"Approved\",\"LCStatus\":[\"Approved\"]},{\"DisplayStatus\":\"Settled\",\"LCStatus\":[\"Partially Settled\"]},{\"DisplayStatus\":\"Rejected\",\"LCStatus\":[\"Rejected\",\"Cancelled\"]}]', 'CLIENT', '1');

INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('EXPORT_LC_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('EXPORT_LC_UPDATE', 'EXPORT_LC', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'EXPORT_LC_UPDATE', 'Manage the Export Letter of Credits', 'View the Export Letter of Credits', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'EXPORT_LC_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'EXPORT_LC_UPDATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'EXPORT_LC_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'EXPORT_LC_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'EXPORT_LC_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EXPORT_LC_UPDATE', 'de-DE', 'Export LC Update', 'Export LC Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EXPORT_LC_UPDATE', 'en-GB', 'Export LC Update', 'Export LC Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EXPORT_LC_UPDATE', 'en-US', 'Export LC Update', 'Export LC Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EXPORT_LC_UPDATE', 'es-ES', 'Export LC Update', 'Export LC Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('EXPORT_LC_UPDATE', 'fr-FR', 'Export LC Update', 'Export LC Update');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e7qx7kia-4125-11yc-839j-0242lm145004', 'TradeFinance', 'ExportLetterOfCredit', 'submitBeneficiaryConsent', 'EXPORT_LC_UPDATE');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('SET_DEFAULT_ENTITY_PREFERENCE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('MULTI_ENTITY_PREFERENCE' ,'RETAIL_AND_BUSINESS_BANKING' ,'Multi Entity Preference' ,'Multi Entity Preference' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('MULTI_ENTITY_PREFERENCE' ,'en-GB' ,'Multi Entity Preference' ,'Multi Entity Preference' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('MULTI_ENTITY_PREFERENCE' ,'de-DE' ,'Multi Entity Preference' ,'Multi Entity Preference' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('MULTI_ENTITY_PREFERENCE' ,'en-US' ,'Multi Entity Preference' ,'Multi Entity Preference' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('MULTI_ENTITY_PREFERENCE' ,'es-ES' ,'Multi Entity Preference' ,'Multi Entity Preference' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('MULTI_ENTITY_PREFERENCE' ,'fr-FR' ,'Multi Entity Preference' ,'Multi Entity Preference' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'MULTI_ENTITY_PREFERENCE');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'MULTI_ENTITY_PREFERENCE');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_WEALTH', 'MULTI_ENTITY_PREFERENCE');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE', 'MULTI_ENTITY_PREFERENCE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SET_DEFAULT_ENTITY_PREFERENCE', 'Set Default Entity Preference', 'Set Default Entity Preference', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE','en-GB', 'Set Default Entity Preference', 'Set Default Entity Preference');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE','de-DE', 'Set Default Entity Preference', 'Set Default Entity Preference');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE','en-US', 'Set Default Entity Preference', 'Set Default Entity Preference');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE','es-ES', 'Set Default Entity Preference', 'Set Default Entity Preference');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SET_DEFAULT_ENTITY_PREFERENCE','fr-FR', 'Set Default Entity Preference', 'Set Default Entity Preference');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'SET_DEFAULT_ENTITY_PREFERENCE', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_RETAIL', 'SET_DEFAULT_ENTITY_PREFERENCE', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`) 
VALUES ('TYPE_ID_WEALTH', 'SET_DEFAULT_ENTITY_PREFERENCE', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('SET_DEFAULT_ENTITY_PREFERENCE', 'SET_DEFAULT_ENTITY_PREFERENCE', 'MULTI_ENTITY_PREFERENCE', 'Set Default Entity Preference', 'Multi Entity Preference');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID120', 'PID45', 'SET_DEFAULT_ENTITY_PREFERENCE', 'MULTI_ENTITY_PREFERENCE', '1', '0');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('37191a58-ec1c-41b0-93f4-c5dc8883d5f0', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ORDER_INITIATION_TYPE', 'Order Initiation Type', 'PRDECREASE', 'CLIENT', '1');

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100011', 'TradeFinance', 'LCSummary', 'generateImportLetterOfCreditsList', 'IMPORT_LC_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100033', 'TradeFinance', 'LCSummary', 'generateImportAmendmentsList', 'IMPORT_LC_AMENDMENT_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100065', 'TradeFinance', 'LCSummary', 'generateImportDrawingsList', 'IMPORT_LC_DRAWINGS_VIEW' );

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-02werrwe0901', 'TradeFinance', 'LCSummary', 'generate', 'IMPORT_LC_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0mnbvb100901', 'TradeFinance', 'LCSummary', 'generateImportLCAmendment', 'IMPORT_LC_AMENDMENT_VIEW' );

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-02gflm123023', 'TradeFinance', 'Guarantees', 'generateGuaranteeAmendmentList', 'LC_GUARANTEES_AMENDMENTS_VIEW' );

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-02ewlm123023', 'TradeFinance', 'ReceivedGuarantees', 'releaseLiability', 'RECEIVED_GUARANTEES_VIEW' );

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100901', 'TradeFinance', 'LCSummary', 'generateExportLetterOfCreditList', 'EXPORT_LC_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100152', 'TradeFinance', 'LCSummary', 'generateExportDrawingsList', 'EXPORT_LC_DRAWINGS_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-0242lm100234', 'TradeFinance', 'LCSummary', 'generateExportAmendmentList', 'EXPORT_LC_AMENDMENT_VIEW' );

INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-02weuir00901', 'TradeFinance', 'LCSummary', 'generateExportLC', 'EXPORT_LC_VIEW' );
INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('e7qx7kia-mo20-11yc-839j-02laoewer901', 'TradeFinance', 'ExportLetterOfCredit', 'generateExportLetterOfCreditDrawing', 'EXPORT_LC_DRAWINGS_VIEW' );
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'UPDATE_PRIMARY_ADDRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'DEFAULT_GROUP', 'UPDATE_PRIMARY_ADDRESS', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('ACCL_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACCOUNT_CLOSE_REASONS', 'Account Closure Reasons', '[{\"Unhappy with our service\",\"Dissatisfied with our Product Offering\", \"Minimum Balance\/Charges are on Higher side\",\"Other\"}]', 'CLIENT', '1');
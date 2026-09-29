INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('0bc23974-6483-11eb-ae93-0242ac130002','7321457251','1425958','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('15b97c30-6483-11eb-ae93-0242ac130002','7321457251','1578660','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('1b054ab6-6483-11eb-ae93-0242ac130002','4204010299','1065631','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('20004002-6483-11eb-ae93-0242ac130002','4204010299','1605506','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`) VALUES ('173', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'OVERRIDE_VALUES', 'Override Values', '{\"AC-OVERDRAFT.ON.ACCOUNT\":\"overdraft\",\"PI-UNAUTH.OVERDRAFT\":\"overdraft\",\"PI-CUT.OFF.TIME.BREACHED\":\"cutOfTimeBreached\",\"PI-CHNG.CUT.OFF.PRODUCT\":\"changeProduct\"}', 'SERVER', '0', 'UID10');

INSERT INTO `favouriteinstruments` (`userId`, `customerId`, `favInstrumentCodes`) VALUES ('103', '1002496540', 'AMZN.O:TSLA.OQ:GOOGL.O'); 

INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('80b2a729-c211-43b3-aae8-e3cc8d546b40','C360_CONFIG_BUNDLE','PREFERENCE','AUTO_SYNC_ACCOUNTS','param for default configuration to sync accounts implicitly or explicitly after addition at core.','false','SERVER',0,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

UPDATE `configurations` SET `config_value` = '{\"default\":\"Pending\",\"currentStatus\":{\"WareHouseOrder\":\"Scheduled\",\"Error\":\"Failed\",\"CancelOrder\":\"Failed\",\"AwaitingFunds\":\"Awaiting Funds\"},\"paymentStatus\":{\"PNDG\":\"Pending\",\"ACSC\":\"Completed\",\"RJCT\":\"Failed\"}}' WHERE (`configuration_id` = '171');

INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `Description`) VALUES ('STOP_NEXT_PAYMENT', 'MAKE_TRANSFER', 'Stop Next Payment', 'Logs Stop Next Payment Requests');



INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `Description`) VALUES ('EMAIL_CHANGE', 'PROFILE_UPDATE', 'Email Change', 'Triggered when email id is updated');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `Description`) VALUES ('PHONE_CHANGE', 'PROFILE_UPDATE', 'Phone Change', 'Triggered when phone number is updated');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `Description`) VALUES ('ADDRESS_CHANGE', 'PROFILE_UPDATE', 'Address Change', 'Triggered when address is updated');


UPDATE `alertsubtype` SET `Status_id` = 'SID_INACTIVE' WHERE (`id` = 'PRIMARY_ADDRESS_CHANGE');
UPDATE `alertsubtype` SET `Status_id` = 'SID_INACTIVE' WHERE (`id` = 'PRIMARY_EMAIL_CHANGE');
UPDATE `alertsubtype` SET `Status_id` = 'SID_INACTIVE' WHERE (`id` = 'PRIMARY_PHONE_CHANGE');


INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `Description`, `isGlobal`, `recipienttype`) VALUES ('ADDRESS_CHANGE', 'PROFILE_UPDATE', 'Address Change', '0', 'SID_ACTIVE', 'Alert when customer address is updated.', '0', '1');
INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `Description`, `isGlobal`, `recipienttype`) VALUES ('EMAIL_CHANGE', 'PROFILE_UPDATE', 'Email Change', '0', 'SID_ACTIVE', 'Alert when customer email is updated.', '0', '1');
INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `Description`, `isGlobal`, `recipienttype`) VALUES ('PHONE_CHANGE', 'PROFILE_UPDATE', 'Phone Change', '0', 'SID_ACTIVE', 'Alert when customer phone number is updated.', '0', '1');


INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('ADDRESS_CHANGE', 'en-US', 'Address Change', 'Alert when customer address is updated.');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('EMAIL_CHANGE', 'en-US', 'Email Change', 'Alert when customer email is updated.');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('PHONE_CHANGE', 'en-US', 'Phone Change', 'Alert when customer phone is updated.');


INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_EMAIL', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_SMS', 'ADDRESS_CHANGE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_EMAIL', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_SMS', 'EMAIL_CHANGE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_EMAIL', 'PHONE_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'PHONE_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'PHONE_CHANGE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_SMS', 'PHONE_CHANGE');

INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PHONE_CHANGE');


INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'PHONE_CHANGE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_RETAIL', 'ADDRESS_CHANGE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_RETAIL', 'EMAIL_CHANGE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_RETAIL', 'PHONE_CHANGE');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`,`softdeleteflag`) VALUES ('173', 'en-US', 'EMAIL_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Email Changed', 'Kony User','0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('174', 'en-US', 'EMAIL_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Email Changed', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('175', 'en-US', 'EMAIL_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'emailchanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].</p><p>Regards,<br />DBX Bank</p>', 'Email Changed', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `createdby`, `softdeleteflag`) VALUES ('176', 'en-US', 'EMAIL_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Kony User','0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `createdby`, `softdeleteflag`) VALUES ('177', 'en-US', 'ADDRESS_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Kony User','0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('178', 'en-US', 'ADDRESS_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Address Changed', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('179', 'en-US', 'ADDRESS_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Address Changed', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('180', 'en-US', 'ADDRESS_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'addresschanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your  address registered with DBX bank is successfully changed.</p><p>Regards,<br />DBX Bank</p>', 'Address Changed', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `createdby`, `softdeleteflag`) VALUES ('181', 'en-US', 'PHONE_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Kony User', '0');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('182', 'en-US', 'PHONE_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Phone Number Changed', 'Kony User', '0');


INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('183', 'en-US', 'PHONE_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Phone Number Changed', 'Kony User', '0');


INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Name`, `Text`, `Subject`, `createdby`, `softdeleteflag`) VALUES ('184', 'en-US', 'PHONE_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'phonenumberchanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].</p><p>Regards,<br />DBX Bank</p>', 'Phone Number Changed', 'Kony User', '0');

INSERT INTO `accounttype` (`TypeID`, `TypeDescription`, `displayName`) VALUES ('8', 'Investment', 'Investment');

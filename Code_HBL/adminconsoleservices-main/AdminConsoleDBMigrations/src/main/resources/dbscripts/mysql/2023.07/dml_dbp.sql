-- DBP BUNDLE CONFIGURATION: SMART BANKING ADVISORY
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('SBA_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_SIMULATION_CHANGE', 'Simulation change in Smart Banking ', '{\"valueChangeInSimulation\" : 100}', 'CLIENT', '1');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('SBA_2', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_TNC_DATA', 'Smart Banking Terms and Conditions', '[{\"heading\": \"Lorem Ipsum\",\"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}, {\"heading\": \"Lorem Ipsum\",\"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}]', 'CLIENT', '1');


-- TO CREATE A EVENT BENEFICIARY

INSERT INTO `eventconsumertypes`  (`EventType`, `ServiceId`, `OperationId`) VALUES ('BENEFICIARY', 'Alerts', 'pushAlerts');

INSERT INTO `eventtype`  (`id`, `Name`, `ActivityType`) VALUES ('BENEFICIARY', 'Beneficiary based alerts', 'TRANSACTIONAL');

INSERT INTO `dbxalerttype`  (`id`, `Name`, `AlertCategoryId`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY', 'Beneficiary based alerts', 'ALERT_CAT_TRANSACTIONAL', 'SID_ACTIVE', '1', '2', 'DAILY', '10:00:00');

INSERT INTO `dbxalerttypetext`  (`AlertTypeId`, `DisplayName`, `Description`, `LanguageCode`) VALUES ('BENEFICIARY', 'Beneficiary based alerts', 'Beneficiary creation/updatation/deletion alerts are included', 'en-US');

INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');


------- TO CREATE A SUBEVENT BENEFICIARY_CREATED

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_CREATED', 'BENEFICIARY', 'Beneficiary has been created');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_CREATED', 'BENEFICIARY', 'Beneficiary has been created', 'The Beneficiary has been created successfully', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'Beneficiary has been created', 'The Beneficiary has been created successfully', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATED', 'Beneficiary has been created', 'The Beneficiary has been created successfully.', 'Beneficiary has been created', '200012', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATED', 'Beneficiary has been created', 'The Beneficiary has been created successfully.', 'Beneficiary has been created', '200013', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATED', 'Beneficiary has been created', 'The Beneficiary has been created successfully.', 'Beneficiary has been created', '200014', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATED', 'Beneficiary has been created', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> The Beneficiary has been created successfully.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary has been created', '200015', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_CREATE_PENDING_TO_USER


INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary creation Request is submitted for approval');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary creation Request is submitted for approval', 'Your Request for Approval Towards Beneficiary Creation has been sent.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'Beneficiary creation Request is submitted for  approval','Your Request for Approval Towards Beneficiary Creation has been sent.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'Beneficiary creation Request is submitted for approval
', 'Your Request for Approval Towards Beneficiary Creation has been sent. Please log on to Internet Banking to view the details.', 'Beneficiary creation Request is submitted for approval', '200016', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'Beneficiary creation Request is submitted for approval', 'Your Request for Approval Towards Beneficiary Creation has been sent. Please log on to Internet Banking to view the details.', 'Beneficiary creation Request is submitted for approval', '200017', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'Beneficiary creation Request is submitted for approval', 'Your Request for Approval Towards Beneficiary Creation has been sent.', 'Beneficiary creation Request is submitted for approval', '200018', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_USER', 'Beneficiary creation Request is submitted for approval', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval Towards Beneficiary Creation has been sent. Please log on to Internet Banking to view the details.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary creation Request is submitted for approval', '200019', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');



------- TO CREATE A SUBEVENT BENEFICIARY_EDITED

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_EDITED', 'BENEFICIARY', 'Beneficiary has been amended');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_EDITED', 'BENEFICIARY', 'Beneficiary has been amended', 'The Beneficiary has been amended successfully.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'Beneficiary has been amended', 'The Beneficiary has been amended successfully.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDITED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDITED', 'Beneficiary has been amended', 'The Beneficiary has been amended successfully.', 'Beneficiary has been amended', '200020', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDITED', 'Beneficiary has been amended', 'The Beneficiary has been amended successfully.', 'Beneficiary has been amended', '200021', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDITED', 'Beneficiary has been amended', 'The Beneficiary has been amended successfully.', 'Beneficiary has been amended', '200022', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDITED', 'Beneficiary has been amended', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> The Beneficiary has been amended successfully.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary has been amended', '200023', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');



------- TO CREATE A SUBEVENT BENEFICIARY_LINKAGE_EDITED

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'BENEFICIARY', 'Beneficiary has been amended');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'BENEFICIARY', 'Beneficiary has been amended', 'The Beneficiary Linking/Delinking has been successfully updated.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'Beneficiary has been amended', 'The Beneficiary Linking/Delinking has been successfully updated.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'Beneficiary has been amended', 'The Beneficiary Linking/Delinking has been successfully updated.', 'Beneficiary has been amended', '200024', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'Beneficiary has been amended', 'The Beneficiary Linking/Delinking has been successfully updated.', 'Beneficiary has been amended', '200025', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'Beneficiary has been amended', 'The Beneficiary Linking/Delinking has been successfully updated.', 'Beneficiary has been amended', '200026', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDITED', 'Beneficiary has been amended', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> The Beneficiary Linking/Delinking has been successfully updated.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary has been amended', '200027', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_EDIT_PENDING_TO_USER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Amendment request is submitted for approval');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Amendment request is submitted for approval', 'Your Request for Approval Towards Beneficiary Amendment has been sent.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'Beneficiary Amendment request is submitted for approval', 'Your Request for Approval Towards Beneficiary Amendment has been sent.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'Beneficiary Amendment request is submitted for approval', 'Your Request for Approval Towards Beneficiary Amendment has been sent. Please log on to Internet Banking to view the details.','Beneficiary Amendment request is submitted for approval', '200028', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'Beneficiary Amendment request is submitted for approval', 'Your Request for Approval Towards Beneficiary Amendment has been sent. Please log on to Internet Banking to view the details.','Beneficiary Amendment request is submitted for approval', '200029', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'Beneficiary Amendment request is submitted for approval', 'Your Request for Approval Towards Beneficiary Amendment has been sent.','Beneficiary Amendment request is submitted for approval', '200030', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_USER', 'Beneficiary Amendment request is submitted for approval', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval Towards Beneficiary Amendment has been sent. Please log on to Internet Banking to view the details.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary Amendment request is submitted for approval', '200031', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');



------- TO CREATE A SUBEVENT BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Cust ID Linking request is submitted for approval');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Cust ID Linking request is submitted for approval', 'Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'Beneficiary Cust ID Linking request is submitted for approval', 'Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'Beneficiary Cust ID Linking request is submitted for approval', 'Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent. Please log on to Internet Banking to view the details.','Beneficiary Cust ID Linking request is submitted for approval', '200032', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'Beneficiary Cust ID Linking request is submitted for approval', 'Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent. Please log on to Internet Banking to view the details.','Beneficiary Cust ID Linking request is submitted for approval', '200033', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'Beneficiary Cust ID Linking request is submitted for approval', 'Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent.','Beneficiary Cust ID Linking request is submitted for approval', '200034', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_USER', 'Beneficiary Cust ID Linking request is submitted for approval', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval Towards Beneficiary Cust ID [#]action[/#] has been sent. Please log on to Internet Banking to view the details.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary Cust ID Linking request is submitted for approval', '200035', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_DELETED

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_DELETED', 'BENEFICIARY', 'Beneficiary has been deleted');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_DELETED', 'BENEFICIARY', 'Beneficiary has been deleted', 'The Beneficiary has been deleted successfully', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'Beneficiary has been deleted', 'The Beneficiary has been deleted successfully', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETED', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETED', 'Beneficiary has been deleted', 'The Beneficiary has been deleted successfully.', 'Beneficiary has been deleted', '200036', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETED', 'Beneficiary has been deleted', 'The Beneficiary has been deleted successfully.', 'Beneficiary has been deleted', '200037', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETED', 'Beneficiary has been deleted', 'The Beneficiary has been deleted successfully.', 'Beneficiary has been deleted', '200038', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETED', 'Beneficiary has been deleted', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> The Beneficiary has been deleted successfully.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary has been deleted', '200039', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_DELETE_PENDING_TO_USER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Deletion request is submitted for approval');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'BENEFICIARY', 'Beneficiary Deletion request is submitted for approval', 'Your Request for Approval Towards Beneficiary Removal has been sent.', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'Beneficiary Deletion request is submitted for approval', 'Your Request for Approval Towards Beneficiary Removal has been sent.', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');



INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'Beneficiary Deletion request is submitted for approval', 'Your Request for Approval Towards Beneficiary Removal has been sent. Please log on to Internet Banking to view the details.','Beneficiary Deletion request is submitted for approval', '200040', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'Beneficiary Deletion request is submitted for approval', 'Your Request for Approval Towards Beneficiary Removal has been sent. Please log on to Internet Banking to view the details.','Beneficiary Deletion request is submitted for approval', '200041', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'Beneficiary Deletion request is submitted for approval', 'Your Request for Approval Towards Beneficiary Removal has been sent.','Beneficiary Deletion request is submitted for approval', '200042', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_USER', 'Beneficiary Deletion request is submitted for approval', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval Towards Beneficiary Removal has been sent. Please log on to Internet Banking to view the details.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Beneficiary Deletion request is submitted for approval', '200043', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_CREATE_PENDING_TO_APPROVER


INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Create Beneficiary Request');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Create Beneficiary Request', 'Request for Approval towards Create Beneficiary has been sent by [#]initiatorUsername[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'Approve/Reject Create Beneficiary Request','Request for Approval towards Create Beneficiary has been sent by [#]initiatorUsername[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'Approve/Reject Create Beneficiary Request', 'Request for Approval towards Create Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Create Beneficiary Request', '200044', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'Approve/Reject Create Beneficiary Request', 'Request for Approval towards Create Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Create Beneficiary Request', '200045', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'Approve/Reject Create Beneficiary Request', 'Request for Approval towards towards Create Beneficiary has been sent by [#]initiatorUsername[/#].', 'Approve/Reject Create Beneficiary Request', '200046', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_CREATE_PENDING_TO_APPROVER', 'Approve/Reject Create Beneficiary Request', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Request for Approval towards Create Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Approve/Reject Create Beneficiary Request', '200047', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');



------- TO CREATE A SUBEVENT BENEFICIARY_EDIT_PENDING_TO_APPROVER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Beneficiary Amendment Request');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Beneficiary Amendment Request', 'Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Beneficiary Amendment Request','Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Beneficiary Amendment Request', 'Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Beneficiary Amendment Request', '200048', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Beneficiary Amendment Request', 'Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Beneficiary Amendment Request', '200049', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Beneficiary Amendment Request', 'Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].', 'Approve/Reject Beneficiary Amendment Request', '200050', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Beneficiary Amendment Request', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Request for Approval towards Amending the Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Approve/Reject Beneficiary Amendment Request', '200051', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Customer Linking/Delinking Request');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Customer Linking/Delinking Request', 'Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Customer Linking/Delinking Request','Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Customer Linking/Delinking Request', 'Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Customer Linking/Delinking Request', '200052', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Customer Linking/Delinking Request', 'Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Customer Linking/Delinking Request', '200053', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Customer Linking/Delinking Request', 'Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].', 'Approve/Reject Customer Linking/Delinking Request', '200054', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_LINKAGE_EDIT_PENDING_TO_APPROVER', 'Approve/Reject Customer Linking/Delinking Request', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Request for Approval towards [#]action[/#] of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Approve/Reject Customer Linking/Delinking Request', '200055', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_DELETE_PENDING_TO_APPROVER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Delete Beneficiary Request');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'BENEFICIARY', 'Approve/Reject Delete Beneficiary Request', 'Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'Approve/Reject Delete Beneficiary Request','Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'Approve/Reject Delete Beneficiary Request', 'Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Delete Beneficiary Request', '200056', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'Approve/Reject Delete Beneficiary Request', 'Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.', 'Approve/Reject Delete Beneficiary Request', '200057', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'Approve/Reject Delete Beneficiary Request', 'Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].', 'Approve/Reject Delete Beneficiary Request', '200058', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_DELETE_PENDING_TO_APPROVER', 'Approve/Reject Delete Beneficiary Request', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Request for Approval towards Deletion of Beneficiary has been sent by [#]initiatorUsername[/#].Please log on to Internet Banking to Approve/Reject.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Approve/Reject Delete Beneficiary Request', '200059', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_REQUEST_REJECTED_TO_USER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'BENEFICIARY', 'Request Rejected');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'BENEFICIARY', 'Request Rejected', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'Request Rejected','Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'Request Rejected', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].Please log on to Internet Banking to view the details.', 'Request Rejected', '200060', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'Request Rejected', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].Please log on to Internet Banking to view the details.', 'Request Rejected', '200061', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'Request Rejected', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].', 'Request Rejected', '200062', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_REJECTED_TO_USER', 'Request Rejected', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval towards [#]action[/#] of Beneficiary has been rejected by [#]approverUsername[/#].Please log on to Internet Banking to view the details.<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Request Rejected', '200063', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


------- TO CREATE A SUBEVENT BENEFICIARY_REQUEST_WITHDRAWN_TO_USER

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'BENEFICIARY', 'Request Withdrawn');

INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'BENEFICIARY', 'Request Withdrawn', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].', '1.0', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'Request Withdrawn','Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].', 'en-US', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'RETAIL_AND_BUSINESS_BANKING', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'TYPE_ID_BUSINESS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'TYPE_ID_RETAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`, `createdts`, `softdeleteflag`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'Request Withdrawn', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].', 'Request Withdrawn', '200064', 'en-US', 'CH_SMS', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'Request Withdrawn', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].', 'Request Withdrawn', '200065', 'en-US', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'Request Withdrawn', 'Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].', 'Request Withdrawn', '200066', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('BENEFICIARY_REQUEST_WITHDRAWN_TO_USER', 'Request Withdrawn', '<p>Dear [#]FirstName[/#] [#]LastName[/#],<br /><br /> Your Request for Approval towards [#]action[/#] of Beneficiary has been withdrawn successfully due to [#]comment[/#].<br />Regards,<br /><br />[#]BankName[/#]</p>', 'Request Withdrawn', '200067', 'en-US', 'CH_EMAIL', 'SID_EVENT_SUCCESS');


DROP VIEW IF EXISTS `username_view`;
CREATE VIEW `username_view` AS
    SELECT 
        `c`.`FirstName` AS `FirstName`,
        `c`.`LastName` AS `LastName`,
        `c`.`UserName` AS `UserName`,
        `bi`.`BackendId` AS `BackendId`,
        `c`.`id` AS `customerId`
    FROM
        (`customer` `c`
        JOIN `backendidentifier` `bi`)
    WHERE
        ((`c`.`id` = `bi`.`Customer_id`)
            AND (`bi`.`BackendType` = 'T24'));
			
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('SBA_3', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_LEARNMORE_DATA', 'Smart Banking Learn More Configuration', '[{\"heading\": \"LearnMore1\", \"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}, {\"heading\": \"LearnMore2\", \"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}]', 'CLIENT', '1');


INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('SBA_4', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_EVENTMINUTESAGO', 'Smart Banking Event Minutes Ago Configuration', '{\"ILP_CompleteEventMinutesAgo\":\360\, \"ILP_StartedEventMinutesAgo\":\60\}', 'CLIENT', '1');

UPDATE `model` set source = 'EXTERNAL' where `id` in ('DC001','DC002','DC003');

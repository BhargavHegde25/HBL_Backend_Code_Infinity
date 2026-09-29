INSERT INTO emailtemplates (`id`,`TemplateName`,`TemplateText`,`Subject`,`SenderName`,`SenderEmail`,`AlertChannel`,`AlertLanguageCode`,`Alert_id`) VALUES ('104','ONBOARDING_PROSPECT_USERNAME_APPICATIONID_TEMPLATE','<table style="width: 101.922%;" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#FFFFFF"><tbody><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 14px 24px; border-bottom: 3px solid #0A78D1; font-family: sans-serif; font-weight: 400;"><img style="display: inline-block; float: left; vertical-align: middle;" src="https://i.imgur.com/YWp6idv.png" alt="logo"/></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 25px 23px 0 23px; font-family: sans-serif; font-weight: 400;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Welcome&nbsp;</span><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">%firstName% %lastName%!</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 22px 23px 22px 23px; font-family: sans-serif; font-weight: 300;"><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">We have created an Temenos Digital Bank digital profile for you.&nbsp;</span></p><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Application number is "<strong>%applicationID%</strong>"</span></p><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Your username is "<strong>%userName%</strong>"</span></p></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 0 23px 0 23px; font-family: sans-serif; font-weight: 300;"><span style="font-size: 12px;">To get started, please look for the temporary password we have sent to your mobile phone</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 30px 23px 0 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Sincerely, </span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 2px 23px 27px 23px; font-family: sans-serif; font-weight: 300; border-bottom: 1px solid #0A78D1;"><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">Temenos Digital Bank</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 17px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 11px; font-weight: 400; line-height: 13px; color: #000000; display: inline-block;">This is a system generated mail. Please do not reply to this e-mail address.</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 7px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">Have any questions?</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 7px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><div style="width: 100%; display: inline-block; padding: 18px 11px; vertical-align: middle; text-align: left; border-radius: 3px; border: 1px solid #E6E6E6; background-color: #ffffff;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Call us at</span> <span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">&nbsp;1-800-412-5434&nbsp;</span> <span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">for assistance.</span></div></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 17px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 10px; font-weight: 400; line-height: 12px; color: #000000; display: inline-block;">CONFIDENTIALITY INFORMATION AND DISCLAIMER</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 5px 23px 18px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 10px; font-weight: 400; line-height: 12px; color: #000000; display: inline-block;">This e-mail message and its attachments may contain confidential, proprietary or legally privileged information and is intended solely for the use of the individual or entity to whom it is addressed. If you have erroneously received this message, please delete it immediately and notify the sender. If you are not the intended recipient of the e-mail message you should not disseminate, distribute or copy this e-mail. E-mail transmission cannot be guaranteed to be secure or error-free as information could be intercepted, corrupted, lost, destroyed, incomplete or contain viruses and the Temenos Digital accepts no liability for any damage caused by the limitations of the e-mail transmission.</span></td></tr></tbody></table>','Temenos Digital','Temenos Digital','dbx_cl@infinity.com',NULL,NULL,NULL);

UPDATE `emailtemplates` SET `TemplateText`= '%otp% is your temporary password to resume your application.' WHERE `id`='101';

UPDATE `configurations` SET `config_value` = '{\"default\":\"Pending\",\"currentStatus\":{\"WareHouseOrder\":\"Scheduled\",\"Error\":\"Failed\",\"CancelOrder\":\"Cancelled\",\"AwaitingFunds\":\"Awaiting Funds\"},\"paymentStatus\":{\"PNDG\":\"Pending\",\"ACSC\":\"Completed\",\"RJCT\":\"Failed\"}}' WHERE (`configuration_id` = '171');

UPDATE `service_permission_mapper` SET `permissions`='API_ACCESS,ALLOW' WHERE `id`='ef83b4c1-3b78-45ff-a9e4-b145f20340fc';
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('aa64eaee-edd5-4635-a44f-a348e1ef2e78', 'RBObjects', 'InfinityUser', 'resendActivationCode', 'ALLOW');

INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('ACCOUNTING.DR.TXN', 'Account Debited', 'CUSTOMER', 'Account Debited');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('ACCOUNTING.CR.TXN', 'Account Credited', 'CUSTOMER', 'Account Credited');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'CUSTOMER', 'Wealth Order Cancellation');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'CUSTOMER', 'Wealth Order Execution');

 

 

INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('ACCOUNT.DEBITED', 'ACCOUNTING.DR.TXN', 'Alert when  amount is debited from the account', '1');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('ACCOUNT.CREDITED','ACCOUNTING.CR.TXN', 'Alert when  amount is credited into the account', '1');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('PWM.CANCEL.ORDER','PWM.SEC.OPEN.ORDER', 'Alert when order is cancelled', '1');
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('PWM.EXECUTED.ORDER', 'PWM.EXECUTED.ORDER', 'Alert when order is executed', '1');


INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'ACCOUNTING.DR.TXN');
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'ACCOUNTING.CR.TXN');
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'PWM.SEC.OPEN.ORDER');
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'PWM.EXECUTED.ORDER');


INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`, `createdby`) VALUES ('ACCOUNTING.DR.TXN', 'Account Debited', 'ALERT_CAT_ACCOUNTS', '1', 'SID_ACTIVE', '0', '6', 'DAILY', '10:00:00', 'default');
INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`, `createdby`) VALUES ('ACCOUNTING.CR.TXN', 'Account Credited', 'ALERT_CAT_ACCOUNTS', '1', 'SID_ACTIVE', '0', '6', 'DAILY', '10:00:00', 'default');
INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`, `createdby`) VALUES ('PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'ALERT_CAT_ACCOUNTS', '1', 'SID_ACTIVE', '0', '6', 'DAILY', '10:00:00', 'default');
INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`, `createdby`) VALUES ('PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'ALERT_CAT_ACCOUNTS', '1', 'SID_ACTIVE', '0', '6', 'DAILY', '10:00:00', 'default');

 

 

INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`, `createdby`) VALUES ('ACCOUNTING.DR.TXN', 'en-US', 'Account Debited', 'Account Debited', 'default');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`, `createdby`) VALUES ('ACCOUNTING.CR.TXN', 'en-US', 'Account Credited', 'Account Credited', 'default');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`, `createdby`) VALUES ('PWM.SEC.OPEN.ORDER', 'en-US', 'Wealth Order Cancellation', 'Wealth Order Cancellation', 'default');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`, `createdby`) VALUES ('PWM.EXECUTED.ORDER', 'en-US', 'Wealth Order Execution', 'Wealth Order Execution', 'default');
 

 


INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_EMAIL', 'ACCOUNTING.DR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNTING.DR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNTING.DR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_SMS', 'ACCOUNTING.DR.TXN', 'default');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_EMAIL', 'ACCOUNTING.CR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNTING.CR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNTING.CR.TXN', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_SMS', 'ACCOUNTING.CR.TXN', 'default');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_EMAIL', 'PWM.SEC.OPEN.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.SEC.OPEN.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.SEC.OPEN.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_SMS', 'PWM.SEC.OPEN.ORDER', 'default');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_EMAIL', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`, `createdby`) VALUES ('CH_SMS', 'PWM.EXECUTED.ORDER', 'default');

 

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `createdby`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('ACCOUNT.DEBITED', 'ACCOUNTING.DR.TXN', 'Account Debited', '1', 'SID_ACTIVE', '0', '1', 'default', '1', '1');
INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `createdby`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('ACCOUNT.CREDITED', 'ACCOUNTING.CR.TXN', 'Account Credited', '1', 'SID_ACTIVE', '0', '1', 'default', '1', '1');
INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `createdby`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('PWM.CANCEL.ORDER', 'PWM.SEC.OPEN.ORDER', 'Order Cancelled', '1', 'SID_ACTIVE', '0', '1', 'default', '1', '1');
INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `createdby`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('PWM.EXECUTED.ORDER', 'PWM.EXECUTED.ORDER', 'Order Executed', '1', 'SID_ACTIVE', '0', '1', 'default', '1', '1');

 

 

INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`, `createdby`) VALUES ('ACCOUNT.DEBITED', 'en-US', 'Account Debited', 'Account Debited', 'default');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`, `createdby`) VALUES ('ACCOUNT.CREDITED', 'en-US', 'Account Credited', 'Account Credited', 'default');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`, `createdby`) VALUES ('PWM.CANCEL.ORDER', 'en-US', 'Order cancelled', 'Order cancelled', 'default');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`, `createdby`) VALUES ('PWM.EXECUTED.ORDER', 'en-US', 'Order executed', 'Order executed', 'default');

 

 


INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_EMAIL', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_SMS', 'ACCOUNT.DEBITED', 'default');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_EMAIL', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_SMS', 'ACCOUNT.CREDITED', 'default');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_EMAIL', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_SMS', 'PWM.CANCEL.ORDER', 'default');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_EMAIL', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`, `createdby`) VALUES ('CH_SMS', 'PWM.EXECUTED.ORDER', 'default');
 

 

INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`, `createdby`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`, `createdby`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`, `createdby`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`, `createdby`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PWM.EXECUTED.ORDER', 'default');



INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT.DEBITED', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT.CREDITED', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_BUSINESS', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_RETAIL', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_BUSINESS', 'PWM.EXECUTED.ORDER', 'default');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`, `createdby`) VALUES ('TYPE_ID_RETAIL', 'PWM.EXECUTED.ORDER', 'default');
 

 

INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby) VALUES ('2', 'ACCOUNT.DEBITED', 'default');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby) VALUES ('2', 'ACCOUNT.CREDITED', 'default');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby) VALUES ('2', 'PWM.CANCEL.ORDER', 'default');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby) VALUES ('2', 'PWM.EXECUTED.ORDER', 'default');
 

 


INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `createdby`) VALUES ('31000001', 'en-US', 'ACCOUNT.DEBITED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000002', 'en-US', 'ACCOUNT.DEBITED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000003', 'en-US', 'ACCOUNT.DEBITED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000004', 'en-US', 'ACCOUNT.DEBITED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited', 'default');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `createdby`) VALUES ('31000005', 'en-US', 'ACCOUNT.CREDITED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000006', 'en-US', 'ACCOUNT.CREDITED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000007', 'en-US', 'ACCOUNT.CREDITED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000008', 'en-US', 'ACCOUNT.CREDITED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited', 'default');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `createdby`) VALUES ('31000009', 'en-US', 'PWM.CANCEL.ORDER', 'CH_SMS', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000010', 'en-US', 'PWM.CANCEL.ORDER', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.','Order Cancelled', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000011', 'en-US', 'PWM.CANCEL.ORDER', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000012', 'en-US', 'PWM.CANCEL.ORDER', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled', 'default');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `createdby`) VALUES ('31000013', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_SMS', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000014', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.','Order Executed', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000015', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed', 'default');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`, `createdby`) VALUES ('31000016', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed', 'default');


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b25c7d86-42b7-48ad-8add-7d612df1909a', 'RBObjects', 'DownloadTransaction', 'generateTransactionDetails', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c65c7d89-42b7-48ad-8dcd-7d612df1809b', 'RBObjects', 'DownloadTransactionReport', 'generateTransactionReport', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('765t7d8u-42b7-41a4-8dcd-7d712df1719c', 'RBObjects', 'DownloadTransactionReport', 'get', 'ALLOW');

UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_EXECUTED') and (`eventtypeid` = 'CHECKBOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_TO_INITIATOR') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECK_STATUS') and (`eventtypeid` = 'CHECK');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DAILY_BALANCE') and (`eventtypeid` = 'DAILY_BALANCE');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSITS') and (`eventtypeid` = 'DEPOSIT_WITHDRAWAL');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSIT_MATURITY_REMINDER') and (`eventtypeid` = 'DEPOSIT_REMINDER');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'MAXIMUM_BALANCE_ALERT') and (`eventtypeid` = 'MAXIMUM_BALANCE');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'MINIMUM_BALANCE') and (`eventtypeid` = 'BALANCE');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'OVERDRAFT_ALERT') and (`eventtypeid` = 'OVERDRAFT');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'PAYMENT_DUE_DATE') and (`eventtypeid` = 'PAYMENT_DUE_DATE');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'PAYMENT_OVERDUE') and (`eventtypeid` = 'PAYMENT_OVERDUE');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAWAL') and (`eventtypeid` = 'DEPOSIT_WITHDRAWAL');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR') and (`eventtypeid` = 'APPROVAL_CHEQUE_BOOK_REQUEST');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAWAL_AMOUNT_ALERT') and (`eventtypeid` = 'WITHDRAWAL_AMOUNT');
UPDATE `eventsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSIT_AMOUNT_ALERT') and (`eventtypeid` = 'DEPOSIT_AMOUNT');


UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_EXECUTED');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECKBOOK_REQUEST_TO_INITIATOR');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'CHECK_STATUS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DAILY_BALANCE');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSITS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSIT_MATURITY_REMINDER');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'MAXIMUM_BALANCE_ALERT');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'MINIMUM_BALANCE');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'OVERDRAFT_ALERT');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'PAYMENT_DUE_DATE');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'PAYMENT_OVERDUE');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAWAL');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'DEPOSIT_AMOUNT_ALERT');
UPDATE `alertsubtype` SET `externalSystem` = '2' WHERE (`id` = 'WITHDRAWAL_AMOUNT_ALERT');

INSERT INTO `emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('105', 'DEVICE_REGISTRATION_ACTIVATIONCODE_TEMPLATE', 'Dear Customer, You have requested to register a new device. Your activation code is %otp%. Please do not share your activation code with anyone.', 'Temenos Digital', 'Temenos Digital', 'dbx_cl@infinity.com');

UPDATE `configurations` SET `config_value` = '{\"SAVINGS.PLAN\":\"Deposit\",\"DEPOSIT.CALL\":\"Deposit\",\"CURRENT.ACCOUNT\":\"Checking\",\"SS.ANNUAL\":\"Checking\",\"SS.ROLLOVER.01M\":\"Deposit\",\"NEGOTIABLE.LOAN\":\"Loan\",\"CURRENT.ACCOUNT.SME\":\"Checking\",\"PREMIUM.ACCOUNT\":\"Checking\",\"CURRENT.ACCOUNT.STUDENT\":\"Checking\",\"SAVINGS.ACCOUNT\":\"Savings\",\"CONS.SAVING\":\"Savings\",\"SAVINGS.SALARY.INFINITY\":\"Savings\",\"MORTGAGE.FLOATING\":\"Loan\",\"TERM.DEPOSIT\":\"Deposit\",\"CURRENT.ACCOUNT.STAFF\":\"Checking\",\"CURRENT.ACCOUNT.PREF\":\"Checking\",\"CONS.CHECKING\":\"Checking\",\"SAVINGS.ACCOUNT.WELCOME\":\"Savings\",\"SAVINGS.STANDARD.INFINITY\":\"Savings\",\"PREFER.ACCOUNT\":\"Checking\",\"STUDENT.ACCOUNT\":\"Checking\",\"MORTGAGE.FIX5Y.60LTV\":\"Loan\",\"DEPOSIT.SHORT\":\"Deposit\",\"DEPOSIT.5Y\":\"Deposit\",\"DEPOSIT.3Y\":\"Deposit\",\"ADVANCED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.PROMOTIONAL\":\"Savings\",\"SAVINGS.ACCOUNT.FCY\":\"Savings\",\"SAVINGS.ACCOUNT.MINOR\":\"Savings\",\"DEPOSIT.09M\":\"Deposit\",\"BASIC.CHECKING.ACCOUNT\":\"Checking\",\"SS.FIXED.TERM\":\"Deposit\",\"SS.MONTHLY\":\"Checking\",\"MORTGAGE\":\"Loan\",\"SS.SAVINGS.REGULAR\":\"Savings\",\"PERSONAL.LOAN\":\"Loan\",\"SS.PAYG\":\"Checking\",\"SAVINGS.PRIME.INFINITY\":\"Savings\",\"CONS.MM\":\"Savings\",\"DEPOSIT.LONG\":\"Deposit\",\"VEHICLE.LOAN\":\"Loan\",\"SAVINGS.DEFAULT\":\"Savings\",\"PREFERRED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.WLC\":\"Savings\",\"SS.SAVINGS.CHILD\":\"Savings\",\"SAVINGS.ACCOUNT.NOTICE\":\"Savings\",\"BONDS.A.6M\":\"Deposit\",\"BONDS.B.1Y\":\"Deposit\",\"BONDS.C.3Y\":\"Deposit\",\"CURRENT.ACCOUNT.GEN\":\"Checking\",\"CURRENT.ACCOUNT.LINK\":\"Checking\",\"CURRENT.DEFAULT\":\"Checking\",\"CURRENT.PARENT\":\"Checking\",\"CURRENT.PARENT.INFINITY\":\"Checking\",\"CURRENT.PARENT.PREF\":\"Checking\",\"CURRENT.PARENT.SME\":\"Checking\",\"CURRENT.PARENT.STD\":\"Checking\",\"CURRENT.SHADOW\":\"Checking\",\"DEPOSIT.03M\":\"Deposit\",\"DEPOSIT.06M\":\"Deposit\",\"DEPOSIT.12M\":\"Deposit\",\"DEPOSIT.18M\":\"Deposit\",\"DEPOSIT.2Y\":\"Deposit\",\"DEPOSIT.4Y\":\"Deposit\",\"DEPOSIT.DEFAULT\":\"Deposit\",\"DEPOSIT.MAT\":\"Deposit\",\"DEPOSIT.NEGOTIABLE\":\"Deposit\",\"DEPOSIT.PARENT\":\"Deposit\",\"EBKM.DEPOSIT\":\"Deposit\",\"EXT.BN.PARENT\":\"Deposit\",\"EXT.DEPOSIT.PARENT\":\"Deposit\",\"INSTALLMENT.12M\":\"Loan\",\"INSTALLMENT.3M\":\"Loan\",\"INSTALLMENT.6M\":\"Loan\",\"INSTALLMENT.LOAN.PARENT\":\"Loan\",\"MORTGAGE.ARM\":\"Mortgage\",\"MORTGAGE.CASHBACK\":\"Mortgage\",\"MORTGAGE.FACILITY\":\"Mortgage\",\"MORTGAGE.FACILITY.PARENT\":\"Mortgage\",\"MORTGAGE.FEP\":\"Mortgage\",\"MORTGAGE.LINK\":\"Mortgage\",\"MORTGAGE.OFFER\":\"Mortgage\",\"MORTGAGE.OFFSET\":\"Mortgage\",\"MORTGAGE.PARENT\":\"Mortgage\",\"MORTGAGE.SEASONAL\":\"Mortgage\",\"PERSONAL.LOAN.2W\":\"Loan\",\"PERSONAL.LOAN.FWD\":\"Loan\",\"PERSONAL.LOAN.LINK\":\"Loan\",\"SAVINGS.PACKAGE\":\"Savings\",\"SAVINGS.PARENT\":\"Savings\",\"SAVINGS.PARENT.INFINITY\":\"Savings\",\"SAVINGS.PARENT.PREF\":\"Savings\",\"SAVINGS.PARENT.STD\":\"Savings\",\"SMALL.BUSINESS.LOAN\":\"Loan\",\"SME.ACCOUNT\":\"Checking\",\"SSA.ACCOUNT\":\"Checking\",\"STAFF.ACCOUNT\":\"Savings\",\"CORP.CURRENT.ACCOUNT\":\"Checking\",\"CL.FACILITY\":\"Sprout\",\"STUDENT.LOAN\":\"Loan\"}' WHERE (`configuration_id` = '172');

INSERT INTO `favouriteinstruments` (`userId`, `customerId`, `favInstrumentCodes`) VALUES ('1002496540', '1002496540', 'AMZN.O:TSLA.OQ:GOOGL.O');
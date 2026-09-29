GO
SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] ON

INSERT INTO [${dbxschemaname}].[emailtemplates] ([id], [TemplateName], [TemplateText], [Subject], [SenderName], [SenderEmail], [AlertChannel], [AlertLanguageCode], [Alert_id]) VALUES (104, N'ONBOARDING_PROSPECT_USERNAME_APPICATIONID_TEMPLATE', N'<table style="width: 101.922%;" border="0" cellspacing="0" cellpadding="0" align="center" bgcolor="#FFFFFF"><tbody><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 14px 24px; border-bottom: 3px solid #0A78D1; font-family: sans-serif; font-weight: 400;"><img style="display: inline-block; float: left; vertical-align: middle;" src="https://i.imgur.com/YWp6idv.png" alt="logo"/></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 25px 23px 0 23px; font-family: sans-serif; font-weight: 400;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Welcome&nbsp;</span><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">%firstName% %lastName%!</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 22px 23px 22px 23px; font-family: sans-serif; font-weight: 300;"><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">We have created an Temenos Digital Bank digital profile for you.&nbsp;</span></p><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Application number is "<strong>%applicationID%</strong>"</span></p><p><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Your username is "<strong>%userName%</strong>"</span></p></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 0 23px 0 23px; font-family: sans-serif; font-weight: 300;"><span style="font-size: 12px;">To get started, please look for the temporary password we have sent to your mobile phone</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 30px 23px 0 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Sincerely, </span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 2px 23px 27px 23px; font-family: sans-serif; font-weight: 300; border-bottom: 1px solid #0A78D1;"><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">Temenos Digital Bank</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 17px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 11px; font-weight: 400; line-height: 13px; color: #000000; display: inline-block;">This is a system generated mail. Please do not reply to this e-mail address.</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 7px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">Have any questions?</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 7px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><div style="width: 100%; display: inline-block; padding: 18px 11px; vertical-align: middle; text-align: left; border-radius: 3px; border: 1px solid #E6E6E6; background-color: #ffffff;"><span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">Call us at</span> <span style="font-family: sans-serif; font-size: 12px; font-weight: bold; line-height: 14px; color: #000000; display: inline-block;">&nbsp;1-800-412-5434&nbsp;</span> <span style="font-family: sans-serif; font-size: 12px; font-weight: 400; line-height: 14px; color: #000000; display: inline-block;">for assistance.</span></div></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 17px 23px 0px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 10px; font-weight: 400; line-height: 12px; color: #000000; display: inline-block;">CONFIDENTIALITY INFORMATION AND DISCLAIMER</span></td></tr><tr><td style="width: 100%; vertical-align: middle; text-align: left; padding: 5px 23px 18px 23px; font-family: sans-serif; font-weight: 300;"><span style="font-family: sans-serif; font-size: 10px; font-weight: 400; line-height: 12px; color: #000000; display: inline-block;">This e-mail message and its attachments may contain confidential, proprietary or legally privileged information and is intended solely for the use of the individual or entity to whom it is addressed. If you have erroneously received this message, please delete it immediately and notify the sender. If you are not the intended recipient of the e-mail message you should not disseminate, distribute or copy this e-mail. E-mail transmission cannot be guaranteed to be secure or error-free as information could be intercepted, corrupted, lost, destroyed, incomplete or contain viruses and the Temenos Digital accepts no liability for any damage caused by the limitations of the e-mail transmission.</span></td></tr></tbody></table>', N'Temenos Digital', N'Temenos Digital', N'dbx_cl@infinity.com', NULL, NULL, NULL);

SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] OFF

GO

UPDATE [${dbxschemaname}].[emailtemplates] SET TemplateText = N'%otp% is your temporary password to resume your application.' WHERE (id = '101');

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{\"default\":\"Pending\",\"currentStatus\":{\"WareHouseOrder\":\"Scheduled\",\"Error\":\"Failed\",\"CancelOrder\":\"Cancelled\",\"AwaitingFunds\":\"Awaiting Funds\"},\"paymentStatus\":{\"PNDG\":\"Pending\",\"ACSC\":\"Completed\",\"RJCT\":\"Failed\"}}' WHERE ([configuration_id] = '171');

GO
-- Wealth alerts
GO
INSERT INTO [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType]) VALUES ('ACCOUNTING.DR.TXN', 'Account Debited', 'CUSTOMER');
INSERT INTO [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType]) VALUES ('ACCOUNTING.CR.TXN', 'Account Credited', 'CUSTOMER');
INSERT INTO [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType]) VALUES ('PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'CUSTOMER');
INSERT INTO [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType]) VALUES ('PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'CUSTOMER');

INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name]) VALUES ('ACCOUNT.DEBITED', 'ACCOUNTING.DR.TXN', 'Alert when  amount is debited from the account');
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name]) VALUES ('ACCOUNT.CREDITED', 'ACCOUNTING.CR.TXN', 'Alert when  amount is credited into the account');
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name]) VALUES ('PWM.CANCEL.ORDER', 'PWM.SEC.OPEN.ORDER', 'Alert when order is cancelled');
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name]) VALUES ('PWM.EXECUTED.ORDER', 'PWM.EXECUTED.ORDER', 'Alert when order is executed');



INSERT INTO [${dbxschemaname}].[eventconsumertypes] ([ServiceId], [OperationId], [EventType]) VALUES ('Alerts', 'pushAlerts', 'ACCOUNTING.DR.TXN');
INSERT INTO [${dbxschemaname}].[eventconsumertypes] ([ServiceId], [OperationId], [EventType]) VALUES ('Alerts', 'pushAlerts', 'ACCOUNTING.CR.TXN');
INSERT INTO [${dbxschemaname}].[eventconsumertypes] ([ServiceId], [OperationId], [EventType]) VALUES ('Alerts', 'pushAlerts', 'PWM.SEC.OPEN.ORDER');
INSERT INTO [${dbxschemaname}].[eventconsumertypes] ([ServiceId], [OperationId], [EventType]) VALUES ('Alerts', 'pushAlerts', 'PWM.EXECUTED.ORDER');


INSERT INTO [${dbxschemaname}].[dbxalerttype] ([id], [Name], [AlertCategoryId], [isAccountLevel], [AttributeId], [AlertConditionId], [Value1], [Value2], [Status_id], [IsGlobal], [DisplaySequence], [defaultFrequencyId], [defaultFrequencyValue], [defaultFrequencyTime]) VALUES
('ACCOUNTING.DR.TXN', 'Account Debited', 'ALERT_CAT_ACCOUNTS', '1', NULL, NULL, NULL, NULL, 'SID_ACTIVE', '0', '0', 'DAILY', NULL, '10:00:00');
INSERT INTO [${dbxschemaname}].[dbxalerttype] ([id], [Name], [AlertCategoryId], [isAccountLevel], [AttributeId], [AlertConditionId], [Value1], [Value2], [Status_id], [IsGlobal], [DisplaySequence], [defaultFrequencyId], [defaultFrequencyValue], [defaultFrequencyTime]) VALUES
('ACCOUNTING.CR.TXN', 'Account Credited', 'ALERT_CAT_ACCOUNTS', '1', NULL, NULL, NULL, NULL, 'SID_ACTIVE', '0', '0', 'DAILY', NULL, '10:00:00');
INSERT INTO [${dbxschemaname}].[dbxalerttype] ([id], [Name], [AlertCategoryId], [isAccountLevel], [AttributeId], [AlertConditionId], [Value1], [Value2], [Status_id], [IsGlobal], [DisplaySequence], [defaultFrequencyId], [defaultFrequencyValue], [defaultFrequencyTime]) VALUES
('PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'ALERT_CAT_ACCOUNTS', '1', NULL, NULL, NULL, NULL, 'SID_ACTIVE', '0', '0', 'DAILY', NULL, '10:00:00');
INSERT INTO [${dbxschemaname}].[dbxalerttype] ([id], [Name], [AlertCategoryId], [isAccountLevel], [AttributeId], [AlertConditionId], [Value1], [Value2], [Status_id], [IsGlobal], [DisplaySequence], [defaultFrequencyId], [defaultFrequencyValue], [defaultFrequencyTime]) VALUES
('PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'ALERT_CAT_ACCOUNTS', '1', NULL, NULL, NULL, NULL, 'SID_ACTIVE', '0', '0', 'DAILY', NULL, '10:00:00');

INSERT INTO [${dbxschemaname}].[dbxalerttypetext]  ([AlertTypeId], [DisplayName], [Description], [LanguageCode]) VALUES ('ACCOUNTING.DR.TXN', 'Account Debited', 'Account Debited', 'en-US');
INSERT INTO [${dbxschemaname}].[dbxalerttypetext]  ([AlertTypeId], [DisplayName], [Description], [LanguageCode]) VALUES ('ACCOUNTING.CR.TXN', 'Account Credited', 'Account Credited', 'en-US');
INSERT INTO [${dbxschemaname}].[dbxalerttypetext]  ([AlertTypeId], [DisplayName], [Description], [LanguageCode]) VALUES ('PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'Wealth Order Cancellation', 'en-US');
INSERT INTO [${dbxschemaname}].[dbxalerttypetext]  ([AlertTypeId], [DisplayName], [Description], [LanguageCode]) VALUES ('PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'Wealth Order Execution', 'en-US');


INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.DR.TXN', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.DR.TXN', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.DR.TXN', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.DR.TXN', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.CR.TXN', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.CR.TXN', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.CR.TXN', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('ACCOUNTING.CR.TXN', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.SEC.OPEN.ORDER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.SEC.OPEN.ORDER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.SEC.OPEN.ORDER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.SEC.OPEN.ORDER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.EXECUTED.ORDER', 'CH_SMS', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.EXECUTED.ORDER', 'CH_PUSH_NOTIFICATION', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.EXECUTED.ORDER', 'CH_NOTIFICATION_CENTER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId], [createdts], [softdeleteflag]) VALUES ('PWM.EXECUTED.ORDER', 'CH_EMAIL', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtype]  ([id], [AlertTypeId], [Name], [Description], [recipienttype], [Status_id], [isAccountLevel], [isGlobal], [defaultFrequencyId], [defaultFrequencyTime]) VALUES ('ACCOUNT.DEBITED', 'ACCOUNTING.DR.TXN', 'Account Debited', 'Account Debited', '1', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO [${dbxschemaname}].[alertsubtype]  ([id], [AlertTypeId], [Name], [Description], [recipienttype], [Status_id], [isAccountLevel], [isGlobal], [defaultFrequencyId], [defaultFrequencyTime]) VALUES ('ACCOUNT.CREDITED', 'ACCOUNTING.CR.TXN', 'Account Credited', 'Account Credited', '1', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO [${dbxschemaname}].[alertsubtype]  ([id], [AlertTypeId], [Name], [Description], [recipienttype], [Status_id], [isAccountLevel], [isGlobal], [defaultFrequencyId], [defaultFrequencyTime]) VALUES ('PWM.CANCEL.ORDER', 'PWM.SEC.OPEN.ORDER', 'Wealth Order Cancellation', 'Wealth Order Cancellation', '1', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');
INSERT INTO [${dbxschemaname}].[alertsubtype]  ([id], [AlertTypeId], [Name], [Description], [recipienttype], [Status_id], [isAccountLevel], [isGlobal], [defaultFrequencyId], [defaultFrequencyTime]) VALUES ('PWM.EXECUTED.ORDER', 'PWM.EXECUTED.ORDER', 'Wealth Order Execution', 'Wealth Order Execution', '1', 'SID_ACTIVE', '1', '1', 'DAILY', '10:00:00');

INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description], [createdts], [softdeleteflag]) VALUES ('ACCOUNT.DEBITED', 'en-US', 'Account Debited', 'Account Debited', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description], [createdts], [softdeleteflag]) VALUES ('ACCOUNT.CREDITED', 'en-US', 'Account Credited', 'Account Credited', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description], [createdts], [softdeleteflag]) VALUES ('PWM.CANCEL.ORDER', 'en-US', 'Wealth Order Cancellation', 'Wealth Order Cancellation', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] ([alertSubTypeId], [languageCode], [displayName], [description], [createdts], [softdeleteflag]) VALUES ('PWM.EXECUTED.ORDER', 'en-US', 'Wealth Order Execution', 'Wealth Order Execution', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_EMAIL', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_SMS', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_EMAIL', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_SMS', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_EMAIL', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_SMS', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_EMAIL', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_NOTIFICATION_CENTER', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_PUSH_NOTIFICATION', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] ([channelId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('CH_SMS', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');



INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] ([appId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');


INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT.DEBITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT.CREDITED', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'PWM.CANCEL.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] ([customerTypeId], [alertSubTypeId], [createdts], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'PWM.EXECUTED.ORDER', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'ACCOUNT.DEBITED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'ACCOUNT.CREDITED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'PWM.CANCEL.ORDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'PWM.EXECUTED.ORDER', 'infinityuser', CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000001', 'en-US', 'ACCOUNT.DEBITED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Account Debited','Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000002', 'en-US', 'ACCOUNT.DEBITED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Account Debited', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000003', 'en-US', 'ACCOUNT.DEBITED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Account Debited', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000004', 'en-US', 'ACCOUNT.DEBITED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Account Debited', 'Your account [#]ACCOUNT.NUMBER[/#] has been debited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Debited');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000005', 'en-US', 'ACCOUNT.CREDITED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Account Credited','Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000006', 'en-US', 'ACCOUNT.CREDITED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Account Credited', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000007', 'en-US', 'ACCOUNT.CREDITED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Account Credited', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000008', 'en-US', 'ACCOUNT.CREDITED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Account Credited', 'Your account [#]ACCOUNT.NUMBER[/#] has been credited with [#]TRANS.AMT[/#] [#]ACCT.CURRENCY[/#]', 'Account Credited');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000009', 'en-US', 'PWM.CANCEL.ORDER', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Order Cancelled','The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000010', 'en-US', 'PWM.CANCEL.ORDER', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Order Cancelled', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000011', 'en-US', 'PWM.CANCEL.ORDER', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Order Cancelled', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000012', 'en-US', 'PWM.CANCEL.ORDER', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Order Cancelled', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully cancelled.', 'Order Cancelled');

INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000013', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Order Executed','The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000014', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Order Executed', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000015', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Order Executed', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed');
INSERT INTO [${dbxschemaname}].[communicationtemplate] ([Id], [LanguageCode], [AlertSubTypeId], [ChannelID], [Status_id], [Name], [Text], [Subject]) VALUES ('31000016', 'en-US', 'PWM.EXECUTED.ORDER', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Order Executed', 'The order reference [#]ORDER.NUMBER[/#] for [#]NO.NOMINAL[/#] quantity for [#]INSTRUMENT.NAME[/#] has been successfully executed.', 'Order Executed');
GO
-- Wealth alerts

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('b25c7d86-42b7-48ad-8add-7d612df1909a', 'RBObjects', 'DownloadTransaction', 'generateTransactionDetails', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('c65c7d89-42b7-48ad-8dcd-7d612df1809b', 'RBObjects', 'DownloadTransactionReport', 'generateTransactionReport', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('765t7d8u-42b7-41a4-8dcd-7d712df1719c', 'RBObjects', 'DownloadTransactionReport', 'get', 'ALLOW');

UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='APPROVE_CHEQUE_BOOK_REQUEST' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='CHECKBOOK_REQUEST_EXECUTED' AND [eventtypeid]='CHECKBOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='CHECKBOOK_REQUEST_FOR_ALL_APPROVERS' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='CHECKBOOK_REQUEST_TO_INITIATOR' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='CHECK_STATUS' AND [eventtypeid]='CHECK';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='DAILY_BALANCE' AND [eventtypeid]='DAILY_BALANCE';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='DEPOSITS' AND [eventtypeid]='DEPOSIT_WITHDRAWAL';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='DEPOSIT_MATURITY_REMINDER' AND [eventtypeid]='DEPOSIT_REMINDER';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='MAXIMUM_BALANCE_ALERT' AND [eventtypeid]='MAXIMUM_BALANCE';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='MINIMUM_BALANCE' AND [eventtypeid]='BALANCE';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='OVERDRAFT_ALERT' AND [eventtypeid]='OVERDRAFT';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='PAYMENT_DUE_DATE' AND [eventtypeid]='PAYMENT_DUE_DATE';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='PAYMENT_OVERDUE' AND [eventtypeid]='PAYMENT_OVERDUE';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='REJECT_CHEQUE_BOOK_REQUEST_APPROVERS' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='REJECT_CHEQUE_BOOK_REQUEST_INITIATOR' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='WITHDRAWAL' AND [eventtypeid]='DEPOSIT_WITHDRAWAL';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR' AND [eventtypeid]='APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='WITHDRAWAL_AMOUNT_ALERT' and [eventtypeid]= 'WITHDRAWAL_AMOUNT';
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem]='2' WHERE [id]='DEPOSIT_AMOUNT_ALERT' and [eventtypeid]='DEPOSIT_AMOUNT';
GO

UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'APPROVE_CHEQUE_BOOK_REQUEST');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'CHECKBOOK_REQUEST_EXECUTED');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'CHECKBOOK_REQUEST_TO_INITIATOR');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'CHECK_STATUS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'DAILY_BALANCE');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'DEPOSITS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'DEPOSIT_MATURITY_REMINDER');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'MAXIMUM_BALANCE_ALERT');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'MINIMUM_BALANCE');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'OVERDRAFT_ALERT');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'PAYMENT_DUE_DATE');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'PAYMENT_OVERDUE');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'WITHDRAWAL');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'DEPOSIT_AMOUNT_ALERT');
UPDATE [${dbxschemaname}].[alertsubtype] SET [externalSystem] = '2' WHERE ([id] = 'WITHDRAWAL_AMOUNT_ALERT');
GO

SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] ON
INSERT INTO [${dbxschemaname}].emailtemplates (id, TemplateName, TemplateText, Subject, SenderName, SenderEmail) VALUES ('105', 'DEVICE_REGISTRATION_ACTIVATIONCODE_TEMPLATE', 'Dear Customer, You have requested to register a new device. Your activation code is %otp%. Please do not share your activation code with anyone.', 'Temenos Digital', 'Temenos Digital', 'dbx_cl@infinity.com');
SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] OFF
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('6dc5d704-2b29-11eb-adc1-0242ac120001', 'RBObjects', 'InfinityUser', 'createInfinityUser', 'ALLOW,API_ACCESS');
GO

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan"}' WHERE ([configuration_id] = '172'); 

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"default":"Pending","currentStatus":{"WareHouseOrder":"Scheduled","Error":"Failed","CancelOrder":"Cancelled","AwaitingFunds":"Awaiting Funds"},"paymentStatus":{"PNDG":"Pending","ACSC":"Completed","RJCT":"Failed"}}' WHERE ([configuration_id] = '171');

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"AC-OVERDRAFT.ON.ACCOUNT":"overdraft","PI-UNAUTH.OVERDRAFT":"overdraft","PI-CUT.OFF.TIME.BREACHED":"cutOfTimeBreached","PI-CHNG.CUT.OFF.PRODUCT":"changeProduct"}' WHERE ([configuration_id] = '173');

UPDATE [${dbxschemaname}].service_permission_mapper SET [${dbxschemaname}].service_permission_mapper.permissions='API_ACCESS,ALLOW' WHERE id='ef83b4c1-3b78-45ff-a9e4-b145f20340fc';

GO

INSERT INTO [${dbxschemaname}].[favouriteinstruments] ([userId], [customerId], [favInstrumentCodes]) VALUES ('1002496540', '1002496540', 'AMZN.O:TSLA.OQ:GOOGL.O');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('f3af30a2-cfcf-4df5-be03-645948u09y43', 'RBObjects', 'CardProducts', 'getCardProducts', 'ALLOW');
GO

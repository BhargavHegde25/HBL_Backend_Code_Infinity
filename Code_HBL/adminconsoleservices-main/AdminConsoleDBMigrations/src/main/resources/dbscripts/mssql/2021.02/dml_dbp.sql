GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id],[contractId],[coreCustomerId],[featureId],[actionId],[softdeleteflag]) VALUES ('0bc23974-6483-11eb-ae93-0242ac130002','7321457251','1425958','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id],[contractId],[coreCustomerId],[featureId],[actionId],[softdeleteflag]) VALUES ('15b97c30-6483-11eb-ae93-0242ac130002','7321457251','1578660','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id],[contractId],[coreCustomerId],[featureId],[actionId],[softdeleteflag]) VALUES ('1b054ab6-6483-11eb-ae93-0242ac130002','4204010299','1065631','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id],[contractId],[coreCustomerId],[featureId],[actionId],[softdeleteflag]) VALUES ('20004002-6483-11eb-ae93-0242ac130002','4204010299','1605506','DIRECT_DEBIT','SKIP_NEXT_PAYMENT','0');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdby]) VALUES ('173', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'OVERRIDE_VALUES', 'Override Values', '{\"AC-OVERDRAFT.ON.ACCOUNT\":\"overdraft\",\"PI-UNAUTH.OVERDRAFT\":\"overdraft\",\"PI-CUT.OFF.TIME.BREACHED\":\"cutOfTimeBreached\",\"PI-CHNG.CUT.OFF.PRODUCT\":\"changeProduct\"}', 'SERVER', '0', 'UID10');
GO

INSERT INTO [${dbxschemaname}].[favouriteinstruments] ([userId], [customerId], [favInstrumentCodes]) VALUES ('103', '1002496540', 'AMZN.O:TSLA.OQ:GOOGL.O');
GO

GO
SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] ON

INSERT INTO [${dbxschemaname}].[emailtemplates] ([id], [TemplateName], [TemplateText], [Subject], [SenderName], [SenderEmail], [AlertChannel], [AlertLanguageCode], [Alert_id]) VALUES (111, N'ENROLLMENT_USERNAME_TEMPLATE', N'<p>&nbsp;</p><center> <div style="width: 95%; height: 5px; margin: 0px 0px 0px 0px;"> <div> <table border="0" width="100%" cellspacing="0" cellpadding="0"> <tbody> <tr> <td style="background-color: #284e77; font-size: 1px; line-height: 1px; -webkit-text-size-adjust: none;" align="center" valign="top" bgcolor="#284e77" height="10">&nbsp;</td> </tr> </tbody> </table> </div> </div> <div style="width: 95%; margin: 0px 0px 50px 0px;"> <div style="display: inline-block; text-align: left;"> <table border="0" width="100%" cellspacing="0" cellpadding="0"> <tbody> <tr> <td style="background-color: #ffffff; -webkit-text-size-adjust: none;"> <div style="margin: 50px 20px 50px 20px; color: #333b44; font-size: 14px;"> <table style="width: 50%;" width="50%"> <tbody> <tr> <td width="200"><img style="text-align: right; width: 200px; border: 0;" src="https://retailbanking1.konycloud.com/dbimages/infinitydbxlogo.png" alt="temenos_logo" width="200" /></td> </tr> </tbody> </table> <br /><br />Hi %firstName% %lastName%,<br /><br />You are enrolled to Digital Banking Channel. Please activate your account now.<br /> <br /> %userName% is your username. Activation code is sent to you registered mobile number.<br /><br /> You are required to input your username & activation code in the link below.<br /><br /> <div style="text-align: center; border-width: 1px; border-radius: 4px;"> <table style="table-layout: fixed;" border="0" width="100%" cellspacing="0" cellpadding="0"> <tbody> <tr> <td style="color: #ffffff; background-color: #333b44; word-wrap: break-word; word-break: break-all; padding: 10px; line-height: 20px;" align="center"> <div style="margin: 10px 10px 10px 10px; font-size: 13px;">To activate your account and set a password,<a style="color: #11abeb;" href="%resetPasswordLink%">click here</a> <br /> or paste the following link on your browser: <br /><br /><a style="color: #11abeb;">%resetPasswordLink%</a></div> </td> </tr> </tbody> </table> </div> <br/> <center>The activation code will expire in %activationCodeExpiry% days , so activate it right away.</center> <br /> <br />Regards,<br />Temenos Banking Team <br /><br /><br /><br /><span style="color: #999999; font-size: 12px;">This is a system generated mail. If you are not the named addressee please notify the sender immediately by e-mail at support@temenosbank.com and then delete the e-mail from your system. Although the company has taken reasonable precautions to ensure no viruses are present in this email, the company cannot accept responsibility for any loss or damage arising from the use of this email or attachments. Temenos, Inc. www.temenos.com </span><br /><br /> <div align="center"><span style="color: #999999;">Copyright &copy; 2020 Temenos Digital DBX. All rights reserved.</span></div> </div> </td> </tr> </tbody> </table> </div> </div></center>', N'Account Activation', N'Temenos Digital', N'dbx_cl@infinity.com', NULL, NULL, NULL);
GO

INSERT INTO [${dbxschemaname}].[emailtemplates] ([id], [TemplateName], [TemplateText], [Subject], [SenderName], [SenderEmail], [AlertChannel], [AlertLanguageCode], [Alert_id]) VALUES (112, N'ENROLLMENT_ACTIVATIONCODE_TEMPLATE', N'Dear Customer, You are enrolled to digital banking channel. %otp% is your activation code. Use it to activate your profile. Username & activation link are sent to your registered email', N'Account Activation', N'Temenos Digital', N'dbx_cl@infinity.com', NULL, NULL, NULL);
GO

SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] OFF
GO

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{\"default\":\"Pending\",\"currentStatus\":{\"WareHouseOrder\":\"Scheduled\",\"Error\":\"Failed\",\"CancelOrder\":\"Failed\",\"AwaitingFunds\":\"Awaiting Funds\"},\"paymentStatus\":{\"PNDG\":\"Pending\",\"ACSC\":\"Completed\",\"RJCT\":\"Failed\"}}' WHERE ([configuration_id] = '171');

INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [Description]) VALUES ('STOP_NEXT_PAYMENT', 'MAKE_TRANSFER', 'Stop Next Payment', 'Logs Stop Next Payment Requests');
GO


INSERT INTO [${dbxschemaname}].[eventsubtype] (id, eventtypeid, Name, Description) VALUES ('EMAIL_CHANGE', 'PROFILE_UPDATE', 'Email Change', 'Triggered when email id is updated');
INSERT INTO [${dbxschemaname}].[eventsubtype] (id, eventtypeid, Name, Description) VALUES ('PHONE_CHANGE', 'PROFILE_UPDATE', 'Phone Change', 'Triggered when phone number is updated');
INSERT INTO [${dbxschemaname}].[eventsubtype] (id, eventtypeid, Name, Description) VALUES ('ADDRESS_CHANGE', 'PROFILE_UPDATE', 'Address Change', 'Triggered when address is updated');
GO

UPDATE [${dbxschemaname}].[alertsubtype] SET Status_id = 'SID_INACTIVE' WHERE id = 'PRIMARY_ADDRESS_CHANGE';
UPDATE [${dbxschemaname}].[alertsubtype] SET Status_id = 'SID_INACTIVE' WHERE id = 'PRIMARY_EMAIL_CHANGE';
UPDATE [${dbxschemaname}].[alertsubtype] SET Status_id = 'SID_INACTIVE' WHERE id = 'PRIMARY_PHONE_CHANGE';
GO

INSERT INTO [${dbxschemaname}].[alertsubtype] (id, AlertTypeId, Name, isAccountLevel, Status_id, Description, isGlobal, recipienttype) VALUES ('ADDRESS_CHANGE', 'PROFILE_UPDATE', 'Address Change', '0', 'SID_ACTIVE', 'Alert when customer address is updated.', '0', '1');
INSERT INTO [${dbxschemaname}].[alertsubtype] (id, AlertTypeId, Name, isAccountLevel, Status_id, Description, isGlobal, recipienttype) VALUES ('EMAIL_CHANGE', 'PROFILE_UPDATE', 'Email Change', '0', 'SID_ACTIVE', 'Alert when customer email is updated.', '0', '1');
INSERT INTO [${dbxschemaname}].[alertsubtype] (id, AlertTypeId, Name, isAccountLevel, Status_id, Description, isGlobal, recipienttype) VALUES ('PHONE_CHANGE', 'PROFILE_UPDATE', 'Phone Change', '0', 'SID_ACTIVE', 'Alert when customer phone number is updated.', '0', '1');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description) VALUES ('ADDRESS_CHANGE', 'en-US', 'Address Change', 'Alert when customer address is updated.');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description) VALUES ('EMAIL_CHANGE', 'en-US', 'Email Change', 'Alert when customer email is updated.');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description) VALUES ('PHONE_CHANGE', 'en-US', 'Phone Change', 'Alert when customer phone is updated.');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_EMAIL', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_NOTIFICATION_CENTER', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_PUSH_NOTIFICATION', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_SMS', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_EMAIL', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_NOTIFICATION_CENTER', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_PUSH_NOTIFICATION', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_SMS', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_EMAIL', 'PHONE_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_NOTIFICATION_CENTER', 'PHONE_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_PUSH_NOTIFICATION', 'PHONE_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId) VALUES ('CH_SMS', 'PHONE_CHANGE');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PHONE_CHANGE');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_BUSINESS', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_BUSINESS', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_BUSINESS', 'PHONE_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_RETAIL', 'ADDRESS_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_RETAIL', 'EMAIL_CHANGE');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId) VALUES ('TYPE_ID_RETAIL', 'PHONE_CHANGE');
GO


INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby,softdeleteflag) VALUES ('173', 'en-US', 'EMAIL_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Email Changed', 'Kony User','0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('174', 'en-US', 'EMAIL_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Email Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('175', 'en-US', 'EMAIL_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'emailchanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].</p><p>Regards,<br />DBX Bank</p>', 'Email Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, createdby, softdeleteflag) VALUES ('176', 'en-US', 'EMAIL_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'emailchanged', 'Dear [#]FirstName[/#] [#]LastName[/#], Your Email address registered with DBX bank is successfully changed to [#]UpdatedEmail[/#].', 'Kony User','0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, createdby, softdeleteflag) VALUES ('177', 'en-US', 'ADDRESS_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Kony User','0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('178', 'en-US', 'ADDRESS_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Address Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('179', 'en-US', 'ADDRESS_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'addresschanged', 'Your  address registered with DBX bank is successfully changed.', 'Address Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('180', 'en-US', 'ADDRESS_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'addresschanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your  address registered with DBX bank is successfully changed.</p><p>Regards,<br />DBX Bank</p>', 'Address Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, createdby, softdeleteflag) VALUES ('181', 'en-US', 'PHONE_CHANGE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('182', 'en-US', 'PHONE_CHANGE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Phone Number Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('183', 'en-US', 'PHONE_CHANGE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'phonenumberchanged', 'Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].', 'Phone Number Changed', 'Kony User', '0');

INSERT INTO [${dbxschemaname}].[communicationtemplate] (Id, LanguageCode, AlertSubTypeId, ChannelID, Status_id, Name, Text, Subject, createdby, softdeleteflag) VALUES ('184', 'en-US', 'PHONE_CHANGE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'phonenumberchanged', '<p>Dear [#]FirstName[/#] [#]LastName[/#],</p><p>Your  phone number registered with DBX bank is successfully changed to [#]UpdatedPhone[/#].</p><p>Regards,<br />DBX Bank</p>', 'Phone Number Changed', 'Kony User', '0');
GO

INSERT INTO [${dbxschemaname}].[accounttype] (TypeID, TypeDescription, displayName) VALUES ('8', 'Investment', 'Investment');

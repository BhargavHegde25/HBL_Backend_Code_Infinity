USE [${dbxdbname}]
GO

INSERT [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PARTY',N'Party',N'CUSTOMER', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT [${dbxschemaname}].[eventtype] ([id], [Name], [ActivityType], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PROSPECT',N'Prospect', N'CUSTOMER', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO

INSERT [${dbxschemaname}].[eventsubtype] ([id],[eventtypeid], [Name], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PARTY_CRETE',N'PARTY',N'Party Create', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT [${dbxschemaname}].[eventsubtype] ([id],[eventtypeid], [Name], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PARTY_UPDATE',N'PARTY',N'Party Update', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT [${dbxschemaname}].[eventsubtype] ([id],[eventtypeid], [Name], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PROSPECT_CRETE',N'PROSPECT',N'Prospect Create', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT [${dbxschemaname}].[eventsubtype] ([id],[eventtypeid], [Name], [Description], [createdby],[modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PROSPECT_UPDATE',N'PROSPECT',N'Prospect Update', NULL, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO

INSERT INTO [${dbxschemaname}].[alertfrequency] ([id]) VALUES ('DAILY');
INSERT INTO [${dbxschemaname}].[alertfrequency] ([id]) VALUES ('WEEKLY');
INSERT INTO [${dbxschemaname}].[alertfrequency] ([id]) VALUES ('MONTHLY');
GO

INSERT INTO [${dbxschemaname}].[alertfrequencytext] ([alertFrequencyId], [languageCode], [displayName], [description]) VALUES ('DAILY', 'en-US', 'Daily', 'Daily');
INSERT INTO [${dbxschemaname}].[alertfrequencytext] ([alertFrequencyId], [languageCode], [displayName], [description]) VALUES ('WEEKLY', 'en-US', 'Weekly', 'Weekly');
INSERT INTO [${dbxschemaname}].[alertfrequencytext] ([alertFrequencyId], [languageCode], [displayName], [description]) VALUES ('MONTHLY', 'en-US', 'Monthly', 'Monthly');
GO

INSERT INTO [${dbxschemaname}].alertfrequencyjobexectime ([id]) VALUES ('1');
GO

INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('1', 'Monday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('2', 'Tuesday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('3', 'Wednesday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('4', 'Thursday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('5', 'Friday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('6', 'Saturday');
INSERT INTO [${dbxschemaname}].[weekday] ([id], [Name]) VALUES ('7', 'Sunday');
GO


INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('1', 'en-US', 'Monday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('2', 'en-US', 'Tuesday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('3', 'en-US', 'Wednesday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('4', 'en-US', 'Thursday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('5', 'en-US', 'Friday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('6', 'en-US', 'Saturday');
INSERT INTO [${dbxschemaname}].[weekdayvalue] ([weekdayId], [languageCode], [displayName]) VALUES ('7', 'en-US', 'Sunday');
GO
UPDATE [${dbxschemaname}].[channel] SET [sequence] = '1' WHERE ([id] = 'CH_SMS');
UPDATE [${dbxschemaname}].[channel] SET [sequence] = '2' WHERE ([id] = 'CH_EMAIL');
UPDATE [${dbxschemaname}].[channel] SET [sequence] = '3' WHERE ([id] = 'CH_NOTIFICATION_CENTER');
UPDATE [${dbxschemaname}].[channel] SET [sequence] = '4' WHERE ([id] = 'CH_PUSH_NOTIFICATION');
GO
UPDATE [${dbxschemaname}].[alertfrequency] SET [sequence] = '1' WHERE ([id] = 'DAILY');
UPDATE [${dbxschemaname}].[alertfrequency] SET [sequence] = '2' WHERE ([id] = 'WEEKLY');
UPDATE [${dbxschemaname}].[alertfrequency] SET [sequence] = '3' WHERE ([id] = 'MONTHLY');
GO

INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('00:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('00:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('01:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('01:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('02:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('02:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('03:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('03:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('04:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('04:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('05:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('05:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('06:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('06:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('07:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('07:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('08:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('08:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('09:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('09:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('10:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('10:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('11:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('11:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('12:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('12:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('13:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('13:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('14:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('14:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('15:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('15:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('16:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('16:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('17:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('17:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('18:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('18:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('19:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('19:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('20:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('20:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('21:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('21:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('22:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('22:30:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('23:00:00');
INSERT INTO [${dbxschemaname}].[alertfrequencytime] ([id]) VALUES ('23:30:00');
GO

INSERT INTO [${dbxschemaname}].[eventtopicconfiguration] ([eventCode], [topic]) VALUES ('SCA_ACTIVATIONCODE', '/events/scaactivationcode');
GO

-- Alert Category
UPDATE [${dbxschemaname}].[dbxalertcategory] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_ACCOUNTS';
UPDATE [${dbxschemaname}].[dbxalertcategory] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_SECURITY';
UPDATE [${dbxschemaname}].[dbxalertcategory] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_TRANSACTIONAL';
GO

-- Alert Group

UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ACCOUNT';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='BILL_PAYEE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='COMBINED_ACCESS';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='CREDENTIAL_CHANGE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_AMOUNT';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='LOGIN'; 
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='MAKE_TRANSFER';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='P2P_RECIPIENT';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PROFILE_UPDATE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='SECURE_MESSAGE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='TRANSFER_RECIPIENT';
UPDATE [${dbxschemaname}].[dbxalerttype] SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE  id='WITHDRAWAL_AMOUNT';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='BALANCE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PAYMENT_DUE_DATE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PAYMENT_OVERDUE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='CHECK';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DAILY_BALANCE';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_REMINDER';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_WITHDRAWAL';
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='MAXIMUM_BALANCE'; 
UPDATE [${dbxschemaname}].[dbxalerttype] SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='OVERDRAFT';
GO

-- Alert Group Channel

INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BILL_PAYEE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BILL_PAYEE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BILL_PAYEE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BILL_PAYEE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CHECK', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CHECK', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CHECK', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CHECK', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'COMBINED_ACCESS', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'COMBINED_ACCESS', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'COMBINED_ACCESS', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'COMBINED_ACCESS', 'infinityuser', GETDATE(), '0');
GO

INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CREDENTIAL_CHANGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CREDENTIAL_CHANGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CREDENTIAL_CHANGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CREDENTIAL_CHANGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DAILY_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DAILY_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DAILY_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DAILY_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_REMINDER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_REMINDER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_REMINDER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_REMINDER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_WITHDRAWAL', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_WITHDRAWAL', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_WITHDRAWAL', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_WITHDRAWAL', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'LOGIN', 'infinityuser', GETDATE(), '0');
GO

INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'LOGIN', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'LOGIN', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'LOGIN', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAKE_TRANSFER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAKE_TRANSFER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAKE_TRANSFER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAKE_TRANSFER', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAXIMUM_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAXIMUM_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAXIMUM_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAXIMUM_BALANCE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OVERDRAFT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OVERDRAFT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OVERDRAFT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OVERDRAFT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'P2P_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'P2P_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'P2P_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'P2P_RECIPIENT', 'infinityuser', GETDATE(), '0');
GO

INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_DUE_DATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_DUE_DATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_DUE_DATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_DUE_DATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_OVERDUE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_OVERDUE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_OVERDUE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_OVERDUE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PROFILE_UPDATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PROFILE_UPDATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PROFILE_UPDATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PROFILE_UPDATE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SECURE_MESSAGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SECURE_MESSAGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SECURE_MESSAGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SECURE_MESSAGE', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSFER_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSFER_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSFER_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSFER_RECIPIENT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL_AMOUNT', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alerttypechannel] (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL_AMOUNT', 'infinityuser', GETDATE(), '0');
GO

-- Alert

UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='ACCOUNT_LOAD';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='UNSUPPORTED_ACCOUNT';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='TRANSACTIONS_NOT_AVAILABLE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='TRANSACTION_LOAD';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='REMOVE_ACCOUNT';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='USER_LINKED';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='USER_DELINKED';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='USER_DEACTIVATED';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='USERNAME_CHANGE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='PASSWORD_CHANGE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='ACCOUNT_LOCKED';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='LOGIN_ATTEMPT';
UPDATE [${dbxschemaname}].[alertsubtype] SET isGlobal='1'  WHERE id='SECURE_MESSAGE_ALERT';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1', attributeId='AMOUNT' , alertConditionId='EQUALS_TO' , value1='50' WHERE id='MINIMUM_BALANCE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='CHECK_STATUS';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1', defaultFrequencyId='DAILY' , defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DAILY_BALANCE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='DEPOSIT_MATURITY_REMINDER';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='DEPOSITS';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='WITHDRAWAL';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1', attributeId='AMOUNT' , alertConditionId='GREATER_THAN'  WHERE id='MAXIMUM_BALANCE_ALERT';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='PAYMENT_OVERDUE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='PAYMENT_DUE_DATE';
UPDATE [${dbxschemaname}].[alertsubtype] SET isAccountLevel='1'  WHERE id='OVERDRAFT_ALERT';
UPDATE [${dbxschemaname}].[alertsubtype] SET attributeId='AMOUNT' , alertConditionId='GREATER_THAN' WHERE id='DEPOSIT_AMOUNT_ALERT';
UPDATE [${dbxschemaname}].[alertsubtype] SET attributeId='AMOUNT' , alertConditionId='GREATER_THAN' WHERE id='WITHDRAWAL_AMOUNT_ALERT';
GO

-- Alert Text

INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('MINIMUM_BALANCE', 'en-US', 'Minimum Balance', 'Alert when account''s balance is below the defined value.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('CHECK_STATUS', 'en-US', 'Check Status', 'Check has been posted to my account or has been rejected', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DAILY_BALANCE', 'en-US', 'Daily Balance Alert', 'Notify the customer of their account balance daily.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSIT_MATURITY_REMINDER', 'en-US', 'Deposit Maturity Alert', 'Notify the customers prior to the maturity date of their time deposit.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSITS', 'en-US', 'Deposits', 'When an amount is credited INTO [${dbxschemaname}].the account.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('WITHDRAWAL', 'en-US', 'Withdrawal', 'When an amount is debited INTO [${dbxschemaname}].the account.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('MAXIMUM_BALANCE_ALERT', 'en-US', 'Maximum Balance', 'An alert is sent to when the account reaches the maximum balance amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('OVERDRAFT_ALERT', 'en-US', 'Overdraft Alerts', 'Alert when the account is overdrawn.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PAYMENT_DUE_DATE', 'en-US', 'Payment Due Date Alert', 'Notify the customer prior to the due date of the payment.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PAYMENT_OVERDUE', 'en-US', 'Payment Overdue', 'When the customer''s payment is overdue.', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_DEACTIVATED', 'en-US', 'The other user is deactivated', 'The other user is successfully deactivated', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_DELINKED', 'en-US', 'The combined user is delinked', 'The combined user is successfully delinked', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_LINKED', 'en-US', 'The combined user is linked', 'The combined user is successfully linked', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PASSWORD_CHANGE', 'en-US', 'Password Change', 'Alert when customer''s sign in password is changed', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USERNAME_CHANGE', 'en-US', 'Username Change', 'Alert when customer''s sign in username is changed', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ACCOUNT_LOCKED', 'en-US', 'Account Locked', 'Alert when customer exceeds maximum failed sign in attempts & the account gets locked', 'infinityuser', GETDATE(), '0');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('LOGIN_ATTEMPT', 'en-US', 'Sign In Attempt', 'Alert when a successful or failed sign in attempt is made', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_ADDRESS_CHANGE', 'en-US', 'Primary Address Change', 'Alert when customer''s primary address is changed/updated.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_EMAIL_CHANGE', 'en-US', 'Primary Email Change', 'Alert when customer''s primary email is changed/updated.', 'infinityuser', GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_PHONE_CHANGE', 'en-US', 'Primary Phone Number Change', 'Alert when customer''s primary phone number is changed/updated.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SECURE_MESSAGE_ALERT', 'en-US', 'Secure Message Alert', 'Send Alerts to customers when a secure message is received by the customer', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ACCOUNT_LOAD', 'en-US', 'Account Load', 'Account Load', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('REMOVE_ACCOUNT', 'en-US', 'Remove Account', 'Remove Account', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('TRANSACTIONS_NOT_AVAILABLE', 'en-US', 'Transactions Not Available', 'Transactions Not Available', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('TRANSACTION_LOAD', 'en-US', 'Transaction Load', 'Transaction Load', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('UNSUPPORTED_ACCOUNT', 'en-US', 'Unsupported Account', 'Unsupported Account', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('BILL_PAYEE_ADDED', 'en-US', 'Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('NON_REG_BILL_PAYEE_ADDED', 'en-US', 'Non Registered Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('REGISTERED_BILL_PAYEE_ADDED', 'en-US', 'Registered Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSIT_AMOUNT_ALERT', 'en-US', 'Deposit Amount Alert', 'An alert is sent to indicate a deposit amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ONETIME_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Onetime Transfer', 'Alert when a other bank onetime transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ONETIME_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Onetime Transfer', 'Alert when own account onetime transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('RECURRING_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Recurring Transfer', 'Alert when a other bank recurring transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('RECURRING_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Recurring Transfer', 'Alert when own account recurring transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SCHEDULED_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Scheduled Transfer', 'Alert when a other bank scheduled transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SCHEDULED_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Scheduled Transfer', 'Alert when own account scheduled transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('P2P_RECIPIENT_ADDED', 'en-US', 'P2P Recipient Added', 'Alert when P2P Recipient Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DOM_WIRE_RECIPIENT_ADDED', 'en-US', 'Domestic Wire Recipient Added', 'Alert when domestic wire recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('INT_TRANSFER_RECIPIENT_ADDED', 'en-US', 'International Transfer Recipient Added', 'Alert when international transfer recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('INT_WIRE_RECIPIENT_ADDED', 'en-US', 'International Wire Recipient Added', 'Alert when international wire recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('OTHER_BANK_RECIPIENT_ADDED', 'en-US', 'Other Bank Recipient Added', 'Alert when other bank transfer recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SAME_BANK_RECIPIENT_ADDED', 'en-US', 'Same Bank Recipeint Added', 'Alert when same bank recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypetext] (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('WITHDRAWAL_AMOUNT_ALERT', 'en-US', 'Withdrawal Amount Alert', 'An alert is sent to indicate a withdrawal amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


-- Alert Channel

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO

INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel] (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO

-- Alert and App Relation

INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp] (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


-- Alert and Customer Type Relation

INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype] (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO



-- Alert and Account Type Relation
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('3', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('6', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('3', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('6', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
GO


INSERT INTO [${dbxschemaname}].[customerviewalertconfiguration] (id, alertPreferenceView, enableFrequency, enableSeparateContact, createdby, softdeleteflag) VALUES ('1', 'GROUP', '0', '0', 'infinityuser', '0');
GO

INSERT INTO [${dbxschemaname}].[cardproducts] ([productName], [featureOverview], [featureDescription], [representativeLabel1], [representativeLabel2], [representativeLabel3], [representativeValue1], [representativeValue2], [representativeValue3], [withdrawlLimit],[withdrawalMinLimit],[withdrawalMaxLimit],[withdrawalStepLimit],[purchaseLimit],[purchaseMinLimit],[purchaseMaxLimit],[purchaseStepLimit]) VALUES (N'Maverick Debit Card', N'• Get up to $480 Cashback every year
• 5% Cashback on shopping via Shopzapp
', N'• 2.5% Cashback on all online spends
• 1% Cashback on all offline spends and Wallet reloads
', N'Daily Purchase Limit', N'Daily Withdrawal Limit', N'Accidental Health Insurance Cover', N'$10000', N'$20000', N'$100000',N'20000',N'0',N'20000',N'50',N'10000',N'0',N'10000', N'50');
INSERT INTO [${dbxschemaname}].[cardproducts] ([productName], [featureOverview], [featureDescription], [representativeLabel1], [representativeLabel2], [representativeLabel3], [representativeValue1], [representativeValue2], [representativeValue3], [withdrawlLimit],[withdrawalMinLimit],[withdrawalMaxLimit],[withdrawalStepLimit],[purchaseLimit],[purchaseMinLimit],[purchaseMaxLimit],[purchaseStepLimit]) VALUES (N'Shop@Ease Platinum Card', N'• 4 Complimentary Domestic Airport Lounge access annually 
 • Accidental insurance cover up to $ 100,000', N'• 2.5% Cashback on all online spends
• 1% Cashback on all offline spends and Wallet reloads
', N'Daily Purchase Limit', N'Daily Withdrawal Limit', N'Accidental Health Insurance Cover', N'$10000', N'$20000', N'$100000', N'20000', N'0', N'20000',N'50',N'10000',N'0',N'10000',N'50');
INSERT INTO [${dbxschemaname}].[cardproducts] ([productName], [featureOverview], [featureDescription], [representativeLabel1], [representativeLabel2], [representativeLabel3], [representativeValue1], [representativeValue2], [representativeValue3], [withdrawlLimit],[withdrawalMinLimit],[withdrawalMaxLimit],[withdrawalStepLimit],[purchaseLimit],[purchaseMinLimit],[purchaseMaxLimit],[purchaseStepLimit]) VALUES (N'Classic Cashback Card', N'• Upto 1% Cashback on retail and online shopping (Max $ 500/month)• Up to $ 25000 Personal Accidental Death Cover (rail/ road/ air)', N'Cash withdrawal facility can now be availed across merchant establishments with a maximum upper limit of $1000/ day on your Infinity Bank Debit Cards.', N'Daily Purchase Limit', N'Daily Withdrawal Limit', N'Accidental Health Insurance Cover', N'$10000', N'$20000', N'$100000',N'20000',N'0',N'20000',N'50',N'10000',N'0',N'10000',N'50');
INSERT INTO [${dbxschemaname}].[cardproducts] ([productName], [featureOverview], [featureDescription], [representativeLabel1], [representativeLabel2], [representativeLabel3], [representativeValue1], [representativeValue2], [representativeValue3], [withdrawlLimit],[withdrawalMinLimit],[withdrawalMaxLimit],[withdrawalStepLimit],[purchaseLimit],[purchaseMinLimit],[purchaseMaxLimit],[purchaseStepLimit]) VALUES (N'Rewards Priority Card', N' • Upto 12% of purchases can be converted as Airmiles for Leading Airlines.
 • Upto 20% Cashback on Food Delivery and Takeaway Food Orders.', N'Rewards bonus on $ 5000 Spend on Apparrels and Movie Tickets.', N'Daily Purchase Limit', N'Daily Withdrawal Limit', N'Accidental Health Insurance Cover', N'$10000', N'$20000', N'$100000',N'20000',N'0',N'20000',N'50',N'10000',N'0',N'10000',N'50');
GO

 
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '500', [withdrawalMinLimit] = '200', [withdrawalMaxLimit] = '3000', [withdrawalStepLimit] = '50', [purchaseLimit] = '2000', [purchaseMinLimit] = '200', [purchaseMaxLimit] = '5000', [purchaseStepLimit] = '50.00' WHERE ([Id] = '115');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '550', [withdrawalMinLimit] = '100', [withdrawalMaxLimit] = '2500', [withdrawalStepLimit] = '100', [purchaseLimit] = '2500', [purchaseMinLimit] = '300', [purchaseMaxLimit] = '5500', [purchaseStepLimit] = '200' WHERE ([Id] = '116');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '200', [withdrawalMaxLimit] = '500', [withdrawalStepLimit] = '20', [purchaseLimit] = '4000', [purchaseMinLimit] = '500', [purchaseMaxLimit] = '10000', [purchaseStepLimit] = '100' WHERE ([Id] = '951');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '150', [withdrawalMinLimit] = '100', [withdrawalMaxLimit] = '500', [withdrawalStepLimit] = '50', [purchaseLimit] = '3400', [purchaseMinLimit] = '300', [purchaseMaxLimit] = '7000', [purchaseStepLimit] = '100' WHERE ([Id] = '117');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '2000', [withdrawalMinLimit] = '100', [withdrawalMaxLimit] = '5000', [withdrawalStepLimit] = '50', [purchaseMinLimit] = '500', [purchaseMaxLimit] = '5000' WHERE ([Id] = '346');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '550', [withdrawalMinLimit] = '200', [withdrawalMaxLimit] = '4000', [withdrawalStepLimit] = '50', [purchaseLimit] = '4000', [purchaseMinLimit] = '400', [purchaseMaxLimit] = '6000' WHERE ([Id] = '347');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '1000', [withdrawalMinLimit] = '200', [withdrawalMaxLimit] = '7000', [withdrawalStepLimit] = '100', [purchaseLimit] = '3000', [purchaseMinLimit] = '300', [purchaseMaxLimit] = '7000' WHERE ([Id] = '348');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '1000', [withdrawalMinLimit] = '200', [withdrawalMaxLimit] = '3000', [withdrawalStepLimit] = '50', [purchaseLimit] = '1000', [purchaseMinLimit] = '500', [purchaseMaxLimit] = '10000', [purchaseStepLimit] = '50' WHERE ([Id] = '949');
UPDATE [${dbxschemaname}].[card] SET [withdrawlLimit] = '1500', [withdrawalMinLimit] = '200', [withdrawalMaxLimit] = '3500', [withdrawalStepLimit] = '20', [purchaseLimit] = '1500', [purchaseMinLimit] = '100', [purchaseMaxLimit] = '7000', [purchaseStepLimit] = '50' WHERE ([Id] = '950');
GO

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('CARD_MANAGEMENT_UPDATE_PURCHASE', 'CARD_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Update purchase limit', 'Update daily purchase limit', '0', '0', '0', '9', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'CARD_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Update withdrawal limit', 'Update daily withdrawal limit', '0', '0', '0', '12', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_UPDATE_PURCHASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_UPDATE_PURCHASE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('CAID160', 'PID45', 'CARD_MANAGEMENT_UPDATE_PURCHASE', 'CARD_MANAGEMENT', '1', 'Kony Dev', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('CAID161', 'PID45', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'CARD_MANAGEMENT', '1', 'Kony Dev', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('a9bc4949-791b-11ea-9300-00090faa0001', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_UPDATE_PURCHASE', 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('a9bc4950-791b-11ea-9300-00090faa0001', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO

INSERT INTO [${dbxschemaname}].[cardproducttype] ([productId],[accountType]) VALUES ('1','Savings');
INSERT INTO [${dbxschemaname}].[cardproducttype] ([productId],[accountType]) VALUES ('2','Savings');
INSERT INTO [${dbxschemaname}].[cardproducttype] ([productId],[accountType]) VALUES ('3','Checking');
INSERT INTO [${dbxschemaname}].[cardproducttype] ([productId],[accountType]) VALUES ('4','Checking');
GO

INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('1', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'DUE', '2020-08-28');
 
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('2', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'DUE', '2020-07-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('3', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'DUE', '2020-06-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('4', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2020-05-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('5', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2020-04-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('6', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2020-03-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('7', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2020-02-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('8', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2020-01-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('9', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-12-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('10', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-11-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('11', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-10-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('12', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-09-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('13', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-08-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('14', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'PAID', '2019-07-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('15', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'FUTURE', '2020-09-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('16', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'FUTURE', '2020-10-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('17', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'FUTURE', '2020-11-28');
INSERT INTO [${dbxschemaname}].[loanschedule] ([id], [AccountId], [Amount], [Principal], [Interest], [OutstandingBalance], [Charges], [Tax], [Insurance], [CumulativeInterest], [InstallmentType], [Date]) VALUES ('18', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'FUTURE', '2020-12-28');
INSERT INTO  [${dbxschemaname}].[customeraccounts] ([id], [Customer_id], [Account_id], [Organization_id], [AccountName], [FavouriteStatus], [IsViewAllowed], [IsDepositAllowed], [IsWithdrawAllowed], [IsOrganizationAccount], [IsOrgAccountUnLinked], [createdby], [createdts]) VALUES ('fe916c3e-2e10-401a-a49a-0e62651315gt', '1002496540', '190128223246822', '200728095903957', 'Turbo Auto Loan', '0', '1', '1', '1', '1', '0', 'admin', '2020-07-28 09:59:52');
GO

INSERT INTO  [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('LOAN_SCHEDULE', 'RETAIL_AND_BUSINESS_BANKING', 'Loan Schedule Transactions', 'Loan schedule', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '37', '1');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary]) VALUES ('VIEW_LOAN_SCHEDULE', 'LOAN_SCHEDULE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Loan schedule', 'Allows user to view loan schedule', '0', '0', '1');
INSERT INTO  [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('2a17f967-5ba9-47c0-ba8d-4ed723ef2d2e', 'DEFAULT_GROUP', 'VIEW_LOAN_SCHEDULE');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('LOAN_SCHEDULE', 'en-US', 'View Loan Schedule ', 'View Loan Schedule ');
INSERT INTO  [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('LOAN_SCHEDULE', 'en-GB', 'View Loan Schedule ', 'View Loan Schedule ');
INSERT INTO  [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_RETAIL', 'LOAN_SCHEDULE');
INSERT INTO  [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'LOAN_SCHEDULE');
INSERT INTO  [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [createdby], [modifiedby]) VALUES ('CAID163', 'PID45', 'VIEW_LOAN_SCHEDULE', 'LOAN_SCHEDULE', '1', 'Kony Dev', 'Kony Dev');
INSERT INTO  [${dbxschemaname}].[rolecompositeaction] ([Role_id], [CompositeAction_id], [isEnabled], [createdby], [modifiedby]) VALUES ('RID_SUPERADMIN', 'CAID163', '1', 'Kony Dev', 'Kony User');
INSERT INTO  [${dbxschemaname}].[rolecompositeaction] ([Role_id], [CompositeAction_id], [isEnabled], [createdby], [modifiedby]) VALUES ('RID_BUSINESS', 'CAID163', '1', 'Kony Dev', 'Kony User');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('002bea3b-7f21-462f-9a62-360039956333','RBObjects', 'Transactions', 'getLoanSchedule', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('cad530ef-7340-43d7-80f4-43a7aa7102f1', 'TransactionAdvice', 'TransactionStatement', 'get', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('cad530ef-7340-43d7-80f4-46a6aa7102f1', 'TransactionAdvice', 'TransactionStatement', 'getTransactionStatementsByYear', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ggf944-f522-75364-63ea-5dd627e92281h', 'RBObjects', 'DownloadAttachments', 'get', 'ALLOW');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('cad530ef-7340-43d7-80f4-43a6aa7102g1', 'TransactionAdvice', 'TransactionAdviceObject', 'get', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('cad530ef-7240-43d7-80f4-43a6aa7102f1', 'TransactionAdvice', 'TransactionAdviceObject', 'getBase64', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('002bea3b-7f21-462f-9a62-360039234901', 'LoanPayoff', 'LoanBillObject', 'getByParam', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('002bea3b-7f21-462f-9a62-360039234902', 'LoanPayoff', 'LoanSimulateObject', 'create', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1972', 'RBObjects', 'Transactions', 'getChequeBookRequests', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1976', 'RBObjects', 'Transactions', 'createStopChequePayments', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1978', 'RBObjects', 'Transactions', 'createChequeBookRequests', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1977', 'RBObjects', 'Transactions', 'getStopChequePayments', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1980', 'RBObjects', 'Transactions', 'getChequeTypes', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1981', 'RBObjects', 'Transactions', 'getChequeSupplements', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('6ef77f18-80bf-4319-cdtc-6d715649113fe', 'RBObjects', 'Security', 'VerifyCaptcha', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('7f88f98-80bf-4219-ddte-9d715649223fg', 'RBObjects', 'Security', 'getRiskScore', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name], [object_name], [operation], [permissions]) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1982', 'RBObjects', 'Transactions', 'getBlockedFunds', 'ALLOW');
GO

UPDATE [${dbxschemaname}].[appmappingaid] SET [Appid] = 'ONBOARDING', [aid] = 'Onboarding' WHERE ([id] = '7');  
GO


UPDATE [${dbxschemaname}].[appmappingaid] SET [aid] = 'OnlineBanking' WHERE ([id] = '1');
GO

UPDATE [${dbxschemaname}].[billermaster] SET [address] = '1500 Boltonfield St, Columbus, OH 43228' WHERE ([id] = '1');
UPDATE [${dbxschemaname}].[billermaster] SET [address] = '1801 66th Ave, Suite 103A, Plantation, FL 33313' WHERE ([id] = '2');
UPDATE [${dbxschemaname}].[billermaster] SET [address] = 'BOA, P.O. Box 15019, Wilmington, DE 19850-5019' WHERE ([id] = '3');
UPDATE [${dbxschemaname}].[billermaster] SET [address] = 'ABC Energy, 200 Post Rd, White Plains, NY, 10601' WHERE ([id] = '6');
GO

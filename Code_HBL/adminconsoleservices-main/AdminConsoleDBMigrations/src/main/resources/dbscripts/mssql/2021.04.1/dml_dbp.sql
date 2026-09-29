GO
SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] ON

INSERT INTO [${dbxschemaname}].emailtemplates (id, TemplateName, TemplateText, Subject, SenderName, SenderEmail) VALUES 
('106', 'LOST_DEVICE_ACTIVATIONCODE_TEMPLATE', 'Dear Customer, %otp% is your Activation code. You can use this to register a new device/ reset PIN.', 'Temenos Digital', 'Temenos Digital', 'dbx_cl@infinity.com');

SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] OFF
GO


UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem] = '1' WHERE ([id] = 'ACCOUNTING.DR.TXN');
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem] = '1' WHERE ([id] = 'ACCOUNTING.CR.TXN');
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem] = '1' WHERE ([id] = 'PWM.SEC.OPEN.ORDER');
UPDATE [${dbxschemaname}].[eventsubtype] SET [externalSystem] = '1' WHERE ([id] = 'PWM.EXECUTED.ORDER');
GO


UPDATE [${dbxschemaname}].[alertsubtype] SET [isGlobal] = '0' , [Description] = NULL, [defaultFrequencyId]= NULL, [defaultFrequencyTime] = NULL,[isAutoSubscribeEnabled]='1',[externalSystem]='1' , [createdby] ='default' WHERE ([id] = 'ACCOUNT.DEBITED');
UPDATE [${dbxschemaname}].[alertsubtype] SET [isGlobal] = '0' , [Description] = NULL, [defaultFrequencyId]= NULL, [defaultFrequencyTime] = NULL,[isAutoSubscribeEnabled]='1',[externalSystem]='1' , [createdby] ='default' WHERE ([id] = 'ACCOUNT.CREDITED');
UPDATE [${dbxschemaname}].[alertsubtype] SET [isGlobal] = '0' , [Description] = NULL, [defaultFrequencyId]= NULL, [defaultFrequencyTime] = NULL,[isAutoSubscribeEnabled]='1',[externalSystem]='1' , [createdby] ='default' WHERE ([id] = 'PWM.CANCEL.ORDER');
UPDATE [${dbxschemaname}].[alertsubtype] SET [isGlobal] = '0' , [Description] = NULL, [defaultFrequencyId]= NULL, [defaultFrequencyTime] = NULL,[isAutoSubscribeEnabled]='1',[externalSystem]='1' , [createdby] ='default' WHERE ([id] = 'PWM.EXECUTED.ORDER');
GO


/* Smart Banking Advisory Featuresactions assignment for specfic role */

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [isNewAction], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'SBA_SIMULATION_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [isNewAction], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'CASHFLOW_PREDICTION_CHART_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [isNewAction], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [isNewAction], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'SBA_INSIGHTS_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
GO


ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] DROP CONSTRAINT [FK_bulkpaymentfiles_featureActionId_idx];
GO
UPDATE [${dbxschemaname}].[privacypolicy] SET Description = 'At Temenos Digital Bank, the safeguarding of your account information is our top priority. We strive to keep our members informed on current security issues, as well as providing the tools to help protect yourself. Please check back periodically to view new information as it becomes available.<br /><h2>Your Account Security</h2><br />We protect your online security. Keeping financial and personal information about you secure and confidential is one of our most important responsibilities. Our systems are protected, so information remains secure.<ol><li>Computer virus protection detects and prevents computer viruses from entering our computer network systems.</li><li>Firewalls block unauthorized access by individuals or networks. Firewalls are just one way we protect our computer network systems that interact with the Internet.</li><li>Secure transmissions ensure information remains confidential. Temenos Digital Bank uses encryption technology such as Secure Socket Layer (SSL) on its Web sites to securely transmit information between you and the bank.</li><li>Send secure e-mail to almost any department in the bank through the Contact Us section. Because an Internet e-mail response back to you may not be secure, we will not include confidential account information in an e-mail response. In addition, you will never be asked for confidential information, such as passwords or PINs, through e-mail. Besides e-mail, you can contact us by phone or visiting any branch.</li><li>Regular evaluations of our security features and continuous research of new advances in security technology ensure that your personal information is protected.</li></ol>' WHERE id = 'PRIV_POL_ID1';

DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';
DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';
DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM [${dbxschemaname}].[dependentactions] WHERE dependentactionId = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';
GO

DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE' and [Feature_id] = 'INTRA_BANK_FUND_TRANSFER';
DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and [Feature_id] = 'INTRA_BANK_FUND_TRANSFER';
DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE' and [Feature_id] = 'INTER_BANK_ACCOUNT_FUND_TRANSFER';
DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and [Feature_id] = 'INTER_BANK_ACCOUNT_FUND_TRANSFER';
DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE' and [Feature_id] = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER';
DELETE FROM [${dbxschemaname}].[featureaction] where id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and [Feature_id] = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER';
GO

INSERT INTO [${dbxschemaname}].[feature]([id],[App_id],[name],[description],[Type_id],[Status_id],[DisplaySequence] ,[isPrimary] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] )
values ('BENEFICIARY_MANAGEMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Beneficiary Management' ,'Beneficiary Management' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO [${dbxschemaname}].[featuredisplayNamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription] , [createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('BENEFICIARY_MANAGEMENT' ,'en-GB' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featuredisplayNamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription], [createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('BENEFICIARY_MANAGEMENT' ,'de-DE' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featuredisplayNamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription], [createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('BENEFICIARY_MANAGEMENT' ,'en-US' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featuredisplayNamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription], [createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('BENEFICIARY_MANAGEMENT' ,'es-ES' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featuredisplayNamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription], [createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('BENEFICIARY_MANAGEMENT' ,'fr-FR' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'BENEFICIARY_MANAGEMENT');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_RETAIL', 'BENEFICIARY_MANAGEMENT');

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Domestic Transfer Self Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID996', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient International Transfer Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID995', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient International Transfer Self Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID994', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] (actionId, dependentactionId, featureId, actionName, featureName) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Domestic Transfer Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID993', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId],[actionName], [featureName]) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Same bank Transfer Self Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID992', 'PID45', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[accesspolicyId],[actionlevelId])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');

INSERT INTO [${dbxschemaname}].[actiondisplayNamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName]) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Same bank Transfer Approval', 'Beneficiary Management');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID991', 'PID45', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
GO


/*set the action levels of beneficiary management to customer level, instead of account level*/
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT';
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT';
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT';
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT';
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT';
UPDATE [${dbxschemaname}].[featureaction] SET isAccountLevel = 0, actionlevelId = 'CUSTOMERID_LEVEL' WHERE id = 'INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT';
GO

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1000', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1001', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1002', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTER_BANK_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1003', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1004', 'PID45', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTRA_BANK_FUND_TRANSFER', '1', '0');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [softdeleteflag]) VALUES ('CAID1005', 'PID45', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTRA_BANK_FUND_TRANSFER', '1', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');


INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby],[softdeleteflag]) VALUES (NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');
UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');
UPDATE [${dbxschemaname}].[featureaction] SET isApprovalAction = '1' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [softdeleteflag]) VALUES (NEWID(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');
GO
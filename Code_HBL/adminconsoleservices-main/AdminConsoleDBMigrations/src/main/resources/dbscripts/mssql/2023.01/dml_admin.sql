UPDATE [${dbxschemaname}].[mfa] SET Status_id = N'SID_INACTIVE' WHERE (id = '1080823475') and (App_id='ORIGINATION') and (Action_id = 'RESUME_AUTHENTICATION');
UPDATE [${dbxschemaname}].[mfa] SET Status_id = N'SID_INACTIVE' WHERE (id = '1080823476') and (App_id='ORIGINATION') and (Action_id = 'PROSPECT_EXPIRY');
UPDATE [${dbxschemaname}].[mfa] SET Status_id = N'SID_INACTIVE' WHERE (id = '1458000737') and (App_id='ORIGINATION') and (Action_id = 'FUNDING_AUTHENTICATION');
UPDATE [${dbxschemaname}].[mfa] SET Status_id = N'SID_INACTIVE' WHERE (id = '1702614193') and (App_id='ORIGINATION') and (Action_id = 'USER_VERIFICATION');


UPDATE [${dbxschemaname}].[alertsubtype] SET externalSystem = '3' WHERE AlertTypeId IN ('PWM.SEC.OPEN.ORDER','PWM.EXECUTED.ORDER');
GO
UPDATE [${dbxschemaname}].[eventsubtype] SET externalSystem = '3' WHERE eventtypeid IN ('PWM.SEC.OPEN.ORDER','PWM.EXECUTED.ORDER');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID516','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewViewBridgeLoanInfo','Permission To View Bridge Loan Info In Facility Overview','0','TRUE');

INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID517','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewEditBridgeLoanInfo','Permission To Edit Bridge Loan Info In Facility Overview','0','TRUE');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID517', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID517', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID517', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID516', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID517', N'0');
GO

INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID518',N'PER_TYPE_REQUEST_OVERVIEW',N'SID_ACTIVE',N'RequestOverviewViewFinancials',N'Permission to View Financials section in Request Overview',N'0',N'TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID518', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID518', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID518', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID518', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID518', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID518', N'0');
GO

INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES (N'PER_TYPE_CONNECTED_LOANS', N'Permission Connected Loans');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID519',N'PER_TYPE_CONNECTED_LOANS',N'SID_ACTIVE',N'RequestOverviewViewConnectedLoans',N'Permission to View Connected Loans section in Request Overview',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID520',N'PER_TYPE_CONNECTED_LOANS',N'SID_ACTIVE',N'FacilityOverviewViewConnectedLoans',N'Permission to View Connected Loans section in Facility Overview',N'0',N'TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID519', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID519', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID519', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID519', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID519', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID519', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID520', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID520', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID520', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID520', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID520', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID520', N'0');
GO

CREATE TABLE [${dbxschemaname}].[tradefinanceconfiguration] (
[userId] nvarchar(50) NOT NULL,
[quickLink] nvarchar(4000) DEFAULT NULL,
[needAttention] nvarchar(4000) DEFAULT NULL,
[currencies] nvarchar(4000) DEFAULT NULL
PRIMARY KEY ([userId]),
CONSTRAINT [customer_idx] FOREIGN KEY ([userId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('QR_PAYMENTS', 'RETAIL_AND_BUSINESS_BANKING', 'QR Payments', 'Create QR Payments', 'MONETARY', 'SID_FEATURE_ACTIVE', '85', '0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id],[Feature_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) values ('TYPE_ID_BUSINESS', 'QR_PAYMENTS', GETDATE(), GETDATE(), GETDATE(), '0' );
INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id],[Feature_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) values ('TYPE_ID_RETAIL', 'QR_PAYMENTS', GETDATE(), GETDATE(), GETDATE(), '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('QR_PAYMENTS', 'de-DE', 'QR Payments', 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('QR_PAYMENTS', 'en-GB', 'QR Payments', 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('QR_PAYMENTS', 'en-US', 'QR Payments', 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('QR_PAYMENTS', 'es-ES', 'QR Payments', 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('QR_PAYMENTS', 'fr-FR', 'QR Payments', 'Create QR Payments');

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('QR_PAYMENTS_CREATE',GETDATE(),GETDATE(),0);

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('QR_PAYMENTS_CREATE','QR_PAYMENTS','RETAIL_AND_BUSINESS_BANKING','MONETARY','QR_PAYMENTS_CREATE','QR Payments','Create QR Payments',1,0,null,0,0,null,0,'SID_ACTION_ACTIVE','CREATE','ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'DEFAULT_GROUP' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_AUTHORIZER' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0);
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_PLATINUM' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0);
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_SERVICE' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0);
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_TESTING' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0);
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_VIEWER' ,'QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0);

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'DEFAULT_GROUP' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_AUTHORIZER' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_PLATINUM' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_SERVICE' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_TESTING' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_VIEWER' ,'QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0) ;

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'DEFAULT_GROUP' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_AUTHORIZER' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_PLATINUM' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_SERVICE' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_TESTING' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_VIEWER' ,'QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0) ;

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'DEFAULT_GROUP' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_AUTHORIZER' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_PLATINUM' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_SERVICE' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_TESTING' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_VIEWER' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0) ;

INSERT INTO [${dbxschemaname}].[featureactionroletype]([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES ('TYPE_ID_BUSINESS' ,'QR_PAYMENTS_CREATE' ,null ,null ,GETDATE() ,GETDATE() ,GETDATE() ,'0');
INSERT INTO [${dbxschemaname}].[featureactionroletype]([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES ('TYPE_ID_RETAIL' ,'QR_PAYMENTS_CREATE' ,null ,null ,GETDATE() ,GETDATE() ,GETDATE() ,'0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'QR_PAYMENTS_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'QR_PAYMENTS_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'QR_PAYMENTS_CREATE', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'QR_PAYMENTS_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'QR_PAYMENTS_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'QR_PAYMENTS_CREATE', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'QR_PAYMENTS_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'QR_PAYMENTS_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'QR_PAYMENTS_CREATE', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('QR_PAYMENTS_CREATE' ,'de-DE' ,'QR Payments' , 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('QR_PAYMENTS_CREATE' ,'en-GB' ,'QR Payments' , 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('QR_PAYMENTS_CREATE' ,'en-US' ,'QR Payments' , 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('QR_PAYMENTS_CREATE' ,'es-ES' ,'QR Payments' , 'Create QR Payments');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('QR_PAYMENTS_CREATE' ,'fr-FR' ,'QR Payments' , 'Create QR Payments');

INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('QR_PAYMENTS_CREATE' ,'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('QR_PAYMENTS_CREATE' ,'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('QR_PAYMENTS_CREATE' ,'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('QR_PAYMENTS_CREATE' ,'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[compositeaction]([id],[Permission_id],[Action_id],[Feature_id],[isEnabled]) VALUES ('CAID900', 'PID45', 'QR_PAYMENTS_CREATE', 'QR_PAYMENTS', '0');

GO


INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('ACCOUNT_SWEEP', 'RETAIL_AND_BUSINESS_BANKING', 'Account Sweeps', 'Account Sweeps', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '84', '0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id],[Feature_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) values ('TYPE_ID_BUSINESS', 'ACCOUNT_SWEEP', GETDATE(), GETDATE(), GETDATE(), '0' );
INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id],[Feature_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) values ('TYPE_ID_RETAIL', 'ACCOUNT_SWEEP', GETDATE(), GETDATE(), GETDATE(), '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP', 'de-DE', 'Account Sweeps', 'Account Sweeps');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP', 'en-GB', 'Account Sweeps', 'Account Sweeps');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP', 'en-US', 'Account Sweeps', 'Account Sweeps');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP', 'es-ES', 'Account Sweeps', 'Account Sweeps');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP', 'fr-FR', 'Account Sweeps', 'Account Sweeps');

INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('ACCOUNT_SWEEP_VIEW');
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('ACCOUNT_SWEEP_CREATE');
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('ACCOUNT_SWEEP_EDIT');
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('ACCOUNT_SWEEP_DELETE');

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('ACCOUNT_SWEEP_VIEW','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_VIEW','View Account Sweep','View Account Sweep',1,0,0,0, GETDATE() ,GETDATE() ,GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','ACCOUNT_LEVEL');
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('ACCOUNT_SWEEP_CREATE','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_CREATE','Create Account Sweep','Create Account Sweep',1,0,0,0, GETDATE() ,GETDATE() ,GETDATE(),'0','SID_ACTION_ACTIVE','CREATE','ACCOUNT_LEVEL');
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('ACCOUNT_SWEEP_EDIT','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_EDIT','Edit Account Sweep','Edit Account Sweep',1,0,0,0, GETDATE() ,GETDATE() ,GETDATE(),'0','SID_ACTION_ACTIVE','CREATE','ACCOUNT_LEVEL');
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[isPrimary],[DisplaySequence],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag],[status],[accesspolicyId],[actionlevelId]) VALUES ('ACCOUNT_SWEEP_DELETE','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_DELETE','Delete Account Sweep','Delete Account Sweep',1,0,0,0, GETDATE() ,GETDATE() ,GETDATE(),'0','SID_ACTION_ACTIVE','DELETE','ACCOUNT_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_VIEW' ,'de-DE' ,'View Account Sweep','View Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_VIEW' ,'en-GB' ,'View Account Sweep','View Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_VIEW' ,'en-US' ,'View Account Sweep','View Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_VIEW' ,'es-ES' ,'View Account Sweep','View Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_VIEW' ,'fr-FR' ,'View Account Sweep','View Account Sweep' );

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_CREATE' ,'de-DE' ,'Create Account Sweep','Create Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_CREATE' ,'en-GB' ,'Create Account Sweep','Create Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_CREATE' ,'en-US' ,'Create Account Sweep','Create Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_CREATE' ,'es-ES' ,'Create Account Sweep','Create Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_CREATE' ,'fr-FR' ,'Create Account Sweep','Create Account Sweep' );

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_EDIT' ,'de-DE' ,'Edit Account Sweep','Edit Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_EDIT' ,'en-GB' ,'Edit Account Sweep','Edit Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_EDIT' ,'en-US' ,'Edit Account Sweep','Edit Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_EDIT' ,'es-ES' ,'Edit Account Sweep','Edit Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_EDIT' ,'fr-FR' ,'Edit Account Sweep','Edit Account Sweep' );

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_DELETE' ,'de-DE' ,'Delete Account Sweep','Delete Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_DELETE' ,'en-GB' ,'Delete Account Sweep','Delete Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_DELETE' ,'en-US' ,'Delete Account Sweep','Delete Account Sweep');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_DELETE' ,'es-ES' ,'Delete Account Sweep','Delete Account Sweep' );
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('ACCOUNT_SWEEP_DELETE' ,'fr-FR' ,'Delete Account Sweep','Delete Account Sweep' );


INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_VIEW',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');
INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_RETAIL','ACCOUNT_SWEEP_VIEW',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');

INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_CREATE',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');
INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_RETAIL','ACCOUNT_SWEEP_CREATE',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');

INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_EDIT',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');
INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_RETAIL','ACCOUNT_SWEEP_EDIT',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');

INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag])  
VALUES ('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_DELETE',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');
INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) 
VALUES ('TYPE_ID_RETAIL','ACCOUNT_SWEEP_DELETE',null,null,GETDATE() ,GETDATE() ,GETDATE(),'0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_ADMINISTRATOR', 'ACCOUNT_SWEEP_VIEW', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_CREATOR', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'DEFAULT_GROUP', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_AUTHORIZER', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_PLATINUM', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_SERVICE', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_TESTING', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_VIEWER', 'ACCOUNT_SWEEP_VIEW', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');


INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_ADMINISTRATOR', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_CREATOR', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'DEFAULT_GROUP', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_AUTHORIZER', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_PLATINUM', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_SERVICE', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_TESTING', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_VIEWER', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');


INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_ADMINISTRATOR', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_CREATOR', 'ACCOUNT_SWEEP_EDIT', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'DEFAULT_GROUP', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_AUTHORIZER', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_PLATINUM', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_SERVICE', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_TESTING', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_VIEWER', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_ADMINISTRATOR', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_CREATOR', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'DEFAULT_GROUP', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_AUTHORIZER', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_PLATINUM', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_SERVICE', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_TESTING', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID() , 'GROUP_VIEWER', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ACCOUNT_SWEEP_VIEW', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ACCOUNT_SWEEP_VIEW', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ACCOUNT_SWEEP_VIEW', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ACCOUNT_SWEEP_VIEW', 'UID10',GETDATE() ,GETDATE() ,GETDATE(), '0');



INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ACCOUNT_SWEEP_CREATE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');



INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]  ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ACCOUNT_SWEEP_EDIT', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');


INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ACCOUNT_SWEEP_DELETE', 'UID10', GETDATE() ,GETDATE() ,GETDATE(), '0');


INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId],[dependentactionId], [featureId], [actionName], [featureName], [createdts], [lastmodifiedts]) values ('ACCOUNT_SWEEP_CREATE', 'ACCOUNT_SWEEP_VIEW', 'ACCOUNT_SWEEP', 'Create Account Sweep','Account Sweeps', GETDATE(), CURRENT_TIMESTAMP);
INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId],[dependentactionId], [featureId], [actionName], [featureName], [createdts], [lastmodifiedts]) values ('ACCOUNT_SWEEP_EDIT', 'ACCOUNT_SWEEP_VIEW', 'ACCOUNT_SWEEP', 'Edit Account Sweep','Account Sweeps', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId],[dependentactionId], [featureId], [actionName], [featureName], [createdts], [lastmodifiedts]) values ('ACCOUNT_SWEEP_DELETE', 'ACCOUNT_SWEEP_VIEW', 'ACCOUNT_SWEEP', 'Delete Account Sweep','Account Sweeps', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

GO


INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('473b91d3-1e03-415b-ae7d-877263b01902', 'InternalusersObjService', 'Users', 'getPermissionsForLegalEntities', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('dc7d3591-8618-47fe-bf93-4588bc8e4cd0', 'AppConfigurationsObjService', 'LoginType', 'getAttribute', 'ALLOW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('b54d2428-9851-4e93-8f92-30a5e5d43ee3', 'MultiEntityObjService', 'MultiEntity', 'getLegalEntities', 'ALLOW');
GO


INSERT INTO [${dbxschemaname}].[configurationbundles]
([bundle_id], [bundle_name], [app_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit])
VALUES('323ade5c-0f85-49fc-bafc-eec40eeef17a', 'RetailBankingSFDC', 'RetailBankingSFDC', '9f883caa-6cc2-4863-b0af-e88eb5ac3370', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'ALL');
INSERT INTO [${dbxschemaname}].[configurations]
([configuration_id], [bundle_id], [config_type],[config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit])
VALUES('0a9ea3c9-78f2-4cb8-b069-7794698749dc', '323ade5c-0f85-49fc-bafc-eec40eeef17a', 'PREFERENCE', 'Config', 'Configurations', '{
  "messages": {
    "en": {
      "account_actions": "Available account actions",
      "account_holder": "Account Holder",
      "account_name": "ACCOUNT NAME",
      "account_number": "ACCOUNT NUMBER",
      "account_ownership": "OWNERSHIP",
      "account_status": "STATUS",
      "account_type": "ACCOUNT TYPE",
      "account_balance": "AVAILABLE BALANCE",
      "accounts": "Accounts",
      "actions": "ACTIONS",
      "address": "ADDRESS",
      "amount": "Amount",
      "apply": "Apply",
      "available_balance": "Available Balance",
      "back_to_account_list": "Back to customer account list",
      "cancel": "Cancel",
      "close": "Close",
      "col-umn_sort": "Mouse click or hit enter or space keys to sort by column",
      "current_account": "Current Account",
      "current_balance": "Current Balance",
      "cust_id": "CUSTOMER ID",
      "custom": "Custom",
      "customer_actions": "Available customer actions",
      "customer_details": "Customer Details",
      "customer_id": "CUSTOMER ID",
      "customer_name": "CUSTOMER NAME",
      "created_on": "Created On",
      "customer_link": "Click the link to see customer details",
      "date": "Date",
      "dates_validation": "Please insert a valid date in format:",
      "dates_validation_2": "To date should be later than from date",
      "dates_validation_3": "From date should be later than min date",
      "description": "Description",
      "divided_paid": "Divided Paid(YTD)",
      "divided_rate": "Divided Rate",
      "edit": "Edit",
      "edit_new_window": "Edit customer in new window",
      "field": "Field",
      "from_date": "From Date",
      "iban": "IBAN",
      "id": "Id",
      "industry": "INDUSTRY",
      "label": "Label",
      "last_30_days": "Last 30 Days",
      "last_7_days": "Last 7 Days",
      "last_dividend_paid": "Last Dividend Paid",
      "last_dividend_paid_on": "Last Dividend Paid On",
      "last_updated_on": "Last Updated On",
      "latest": "Latest",
      "no_matches": "No Matches",
      "no_results": "No Results",
      "ownership": "Ownership",
      "pending": "Pending",
      "phone_number": "PHONE NUMBER",
      "product_details": "Product Details",
      "recent": "Recent",
      "routing_number": "Routing Number",
      "save": "Save",
      "search": "Search...",
      "search_date": "Search...",
      "select_date_range": "Click to select a date range to filter transac-tions",
      "search_title": "Type three or more characters to find the text in all the visible content",
      "search_title_transaction": "Type three or more characters to find the text in all visible transactions",
      "select": "Select",
      "select_locale": "Select a Locale",
      "set": "Set",
      "settings": "Settings",
      "scheduled": "Scheduled",
      "showing": "Showing",
      "summary": "Summary",
      "swift_code": "Swift Code",
      "tax_id": "TAX ID",
      "to_date": "To Date",
      "today": "Today",
      "transaction_actions": "Available transaction actions",
      "transaction_amount": "ORIGINAL",
      "transaction_date": "DATE & TIME",
      "transaction_description": "TRANSACTION DESCRIPTION",
      "transaction_history": "Transaction History",
      "transac-tion_history_link": "Click on the account number to see the transaction list for this account",
      "transaction_id": "REF NO",
      "transaction_list": "Transaction List",
      "transaction_results": "Transaction Results",
      "transaction_type": "Type",
      "visible": "Visible",
      "yesterday": "Yesterday"
    }
  },
  "screens": {
    "common": {
      "currency": "USD",
      "dir": "ltr",
      "locale": "en",
      "longDateFormat": "yyyy-MM-dd hh:mm:ss",
      "shortDateFormat": "yyyy-MM-dd",
      "translations": {
        "no.results": "no_results",
        "no.matches": "no_matches"
      },
      "usePopup": false,
      "showActions": false
    },
    "customer": {
      "features": {
        "digitalContract": {
          "accountList": {
            "accountIdField": "accountId",
            "accountNameField": "accountName",
            "columns": [
              {
                "label": "account_number",
                "type": "link",
                "field": "accountId"
              },
              {
                "label": "account_type",
                "type": "string",
                "field": "accountType"
              },
              {
                "label": "account_status",
                "type": "indicator",
                "field": "accountStatus"
              },
              {
                "label": "account_ownership",
                "type": "string",
                "field": "ownership"
              },
              {
                "label": "account_name",
                "type": "string",
                "field": "accountName"
              },
              {
                "label": "actions",
                "type": "action",
                "icon": "more_ver",
                "field": "accountType",
                "key": "accountId"
              },
              {
                "label": "account_balance",
                "type": "amount",
                "field": "currentBalance",
                "currency": "currencyCode"
              }
            ],
            "pageSize": 50,
            "pagination": true,
            "serviceTitle": "account_actions",
            "serviceType": "CreditCard"
          },
          "accountsPropertyName": "coreCustomerAccounts",
          "customersPropertyName": "contractCustomers",
          "detailedSummary": {
            "rows": [
              [
                {
                  "label": "customer_name",
                  "field": "name"
                },
                {
                  "label": "customer_id",
                  "field": "id"
                },
                {
                  "label": "industry",
                  "field": "industry"
                }
              ],
              [
                {
                  "label": "phone_number",
                  "field": "phone"
                },
                {
                  "label": "address",
                  "field": "addressLine1,addressLine2"
                },
                {}
              ]
            ]
          },
          "editButton": {
            "url": "https://www.temenos.com/?",
            "visible": false
          },
          "permissions": [],
          "search": {
            "numberOfCharsStartsSearch": 3
          },
          "serviceTitle": "customer_actions",
          "serviceType": "Customer",
          "summary": {
            "rows": [
              {
                "label": "cust_id",
                "field": "id"
              },
              {
                "label": "tax_id",
                "field": "taxId"
              },
              {
                "label": "address",
                "field": "addressLine1,cityName,zipCode"
              }
            ]
          },
          "translations": {
            "account.list.link.title": "transaction_history_link",
            "account.list.title": "accounts",
            "customer.link.title": "customer_link",
            "detailed.summary.button.cancel": "cancel",
            "detailed.summary.button.close": "close",
            "detailed.summary.button.save": "save",
            "detailed.summary.title": "customer_details",
            "edit.button.label": "edit",
            "edit.button.title": "edit_new_window",
            "list.column.sort": "column_sort",
            "search.label": "search",
            "search.title": "search_title",
            "settings.select.locale": "select_locale",
            "settings.button.set": "set",
            "settings.button.cancel": "cancel",
            "settings.button.visible": "visible",
            "settings.title": "settings",
            "summary.title": "summary"
          }
        },
        "transactionList": {
          "columns": [
            {
              "label": "transaction_id",
              "type": "string",
              "field": "transactionId"
            },
            {
              "label": "transaction_date",
              "type": "string",
              "field": "transactionDate"
            },
            {
              "label": "transaction_description",
              "type": "string",
              "field": "description"
            },
            {
              "label": "transaction_amount",
              "type": "amount",
              "field": "amount",
              "currency": "transactionCurrency"
            },
            {
              "label": "actions",
              "type": "action",
              "icon": "more_ver",
              "field": "product",
              "key": "transactionId"
            }
          ],
          "defaultRange": {
            "value": "last_7_days"
          },
          "detail": {
            "rows": [
              [
                {
                  "label": "created_on",
                  "field": "createdOn"
                },
                {
                  "label": "current_balance",
                  "field": "currentBalance",
                  "type": "amount"
                },
                {
                  "label": "divided_rate",
                  "field": "dividedRate",
                  "type": "amount"
                }
              ],
              [
                {
                  "label": "ownership",
                  "field": "ownership"
                },
                {
                  "label": "available_balance",
                  "field": "availableBalance",
                  "type": "amount"
                },
                {
                  "label": "divided_paid",
                  "field": "dividedPaid",
                  "type": "amount"
                }
              ],
              [
                {
                  "label": "account_holder",
                  "field": "accountHolder"
                },
                {
                  "label": "routing_number",
                  "field": "routingNumber"
                },
                {
                  "label": "last_divided_paid",
                  "field": "lastDividedPaid",
                  "type": "amount"
                }
              ],
              [
                {
                  "label": "last_updated_on",
                  "field": "lastUpdatedOn"
                },
                {
                  "label": "swift_code",
                  "field": "swiftCode"
                },
                {
                  "label": "last_divided_paid_on",
                  "field": "lastDividedPaidOn"
                }
              ]
            ]
          },
          "fromDateMin": "2010-01-01",
          "IBANField": "IBAN",
          "pageSize": 50,
          "pagination": true,
          "search": {
            "numberOfCharsStartsSearch": 3,
            "showAbove": true
          },
          "serviceTitle": "transaction_actions",
          "serviceType": "Account",
          "translations": {
            "button.back": "back_to_account_list",
            "button.close": "close",
            "date.range.button.apply": "apply",
            "date.range.button.title": "select_date_range",
            "date.range.button.close": "cancel",
            "date.range.button.custom": "custom",
            "date.range.button.last.7.days": "last_7_days",
            "date.range.button.last.30.days": "last_30_days",
            "date.range.button.latest": "latest",
            "date.range.button.select": "select",
            "date.range.button.today": "today",
            "date.range.button.yesterday": "yesterday",
            "date.range.from.date": "from_date",
            "date.range.showing": "showing",
            "date.range.to.date": "to_date",
            "date.range.validation.1": "dates_validation",
            "date.range.validation.2": "dates_validation_2",
            "date.range.validation.3": "dates_validation_3",
            "header.account.number": "account_number",
            "header.iban": "iban",
            "list.column.sort": "column_sort",
            "pending": "pending",
            "product.details": "product_details",
            "recent": "recent",
            "search.label": "search",
            "search.title": "search_title",
            "scheduled": "scheduled",
            "transaction.history": "transaction_history",
            "transaction.list": "transaction_list"
          },
          "useBackButton": false
        }
      }
    }
  }
}

', 'CLIENT', 0, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0, 'ALL');

GO

/* SQL Scripts for InfinityWealth - Advisory Portfolios - Entitlements and Permissions */
-- Action :: STRATEGY_ALLOCATION_PERSONALIZE
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('STRATEGY_ALLOCATION-PERSONALIZE');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES
('STRATEGY_ALLOCATION_PERSONALIZE','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','STRATEGY_ALLOCATION-PERSONALIZE','Strategy Allocation Personalize','Strategy Allocation Personalize','0','0','0','225', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES
('STRATEGY_ALLOCATION_PERSONALIZE','en-GB', 'Strategy Allocation Personalize', 'Strategy Allocation Personalize', 'GB0010001'),
('STRATEGY_ALLOCATION_PERSONALIZE','en-US', 'Strategy Allocation Personalize', 'Strategy Allocation Personalize', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES
('TYPE_ID_WEALTH', 'STRATEGY_ALLOCATION_PERSONALIZE', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES
('CAID825', 'PID45', 'STRATEGY_ALLOCATION_PERSONALIZE', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES
(NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'STRATEGY_ALLOCATION_PERSONALIZE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001'),
(NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'STRATEGY_ALLOCATION_PERSONALIZE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES
(NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'STRATEGY_ALLOCATION_PERSONALIZE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001'),
(NEWID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'STRATEGY_ALLOCATION_PERSONALIZE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

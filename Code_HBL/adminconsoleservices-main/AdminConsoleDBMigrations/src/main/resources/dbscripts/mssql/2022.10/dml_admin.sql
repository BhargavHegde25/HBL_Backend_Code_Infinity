INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES ('PER_TYPE_SERVICEREQUEST', 'Permission Type Service Request');

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID503','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ViewServiceRequest','Permission to view Service Request in dashboard','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID504','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','UpdateServiceRequest','Permission to update Service Request','0','TRUE');

INSERT INTO  [${dbxschemaname}].[role] ([id], [Type_id], [Status_id], [Parent_id], [Name], [Description]) VALUES (N'RID_SERVICING_OPS', N'ROLE_TYPE_1', N'SID_ACTIVE', N'RID_SUPERADMIN', N'Servicing Ops', N'This role is used for Servicing Operation User'); 
   
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID503');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID504');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RELATIONSHIP_MANAGER', N'PID316', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID316', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_OPERATIONS', N'PID316', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID316', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SECRETARY', N'PID316', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID317', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID317', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID318', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID318', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID319', N'0');

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID505','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewViewAdditionalInstruction','Permission to view additional instruction in Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID506','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewAddAdditionalInstruction','Permission to add additional instruction in Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID507','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewUpdateAdditionalInstruction','Permission to update additional instruction in Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID508','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewDeleteAdditionalInstruction','Permission to delete additional instruction in Request Overview','0','TRUE');


INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID505');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_UNDERWRITER', 'PID505');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SUPERVISOR', 'PID505');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SECRETARY', 'PID505');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_OPERATIONS', 'PID505');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID506');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_UNDERWRITER', 'PID506');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID506');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SECRETARY', 'PID506');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_OPERATIONS', 'PID506');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_RELATIONSHIP_MANAGER', 'PID507');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_UNDERWRITER', 'PID507');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SUPERVISOR', 'PID507');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SECRETARY', 'PID507');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_OPERATIONS', 'PID507');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_RELATIONSHIP_MANAGER', 'PID508');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_UNDERWRITER', 'PID508');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID508');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SECRETARY', 'PID508');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_OPERATIONS', 'PID508');
	
GO
UPDATE [${dbxschemaname}].[configurations] SET config_value = '78efdef8cb1bc262fba18e5699c4bba2' WHERE (configuration_id = '09357a9d-a1a2-4748-a43a-b34b2eba64e9');

GO

INSERT INTO [${dbxschemaname}].[configurations]
([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit])
VALUES(N'110', N'DBP_CONFIG_BUNDLE', N'PREFERENCE', N'LEGAL_UNITS', N'Time to automatically unlock a customer (in minutes).', N'{ "id":"GB0010001", "comapnyName":"IN", "region":"India", "description":"Company entity for India region" }', N'SERVER', N'0', N'Kony dev', N'Kony dev', N'2022-08-25 10:08:25.827', N'2022-08-25 10:08:25.827', N'2022-08-25 10:08:25.827', N'0', N'ALL');

INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID509',N'PER_TYPE_FACILITY',N'SID_ACTIVE',N'FacilityOverviewViewFactFind',N'Permission to View Fact Find section in Facility Overview',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID510',N'PER_TYPE_FACILITY',N'SID_ACTIVE',N'FacilityOverviewUpdateFactFind',N'Permission to Update Fact Find section in Facility Overview',N'0',N'TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID509', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID509', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID509', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID509', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID509', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID509', N'0');
GO

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID510', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID510', N'0');
GO

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT',GETDATE(),GETDATE(),0);
INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT',GETDATE(),GETDATE(),0);
GO

INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[limitgroupId],[accesspolicyId],[actionlevelId]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','MONETARY','BULK_PAYMENT_SINGLE_SUBMIT','Bulk Payment Request Single Submit','Enables the user to submit the Single Bulk Payment Request',1,0,null,0,0,null,0,'BULK_PAYMENT','BULK_CREATE','ACCOUNT_LEVEL');
INSERT INTO [${dbxschemaname}].[featureaction]([id],[Feature_id],[App_id],[Type_id],[Rrole_id],[name],[description],[isAccountLevel],[isMFAApplicable],[MFA_id],[isPrimary],[DisplaySequence],[dependency],[softdeleteflag],[limitgroupId],[accesspolicyId],[actionlevelId]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','MONETARY','BULK_PAYMENT_MULTIPLE_SUBMIT','Bulk Payment Request Multiple Submit','Enables the user to submit the Multiple Bulk Payment Request',1,0,null,0,0,null,0,'BULK_PAYMENT','BULK_CREATE','ACCOUNT_LEVEL');
GO

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0 ) ;

INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'DAILY_LIMIT', '1000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit]([id],[Group_id],[Action_id],[LimitType_id],[value],[createdby],[modifiedby],[softdeleteflag]) VALUES (NEWID() ,'GROUP_CREATOR' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00','UID11' ,null ,0 ) ;
GO

INSERT INTO [${dbxschemaname}].[featureactionroletype]([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES ('TYPE_ID_BUSINESS' ,'BULK_PAYMENT_SINGLE_SUBMIT' ,null ,null ,GETDATE() ,GETDATE() ,GETDATE() ,'0' ) ;
INSERT INTO [${dbxschemaname}].[featureactionroletype]([RoleType_id],[Action_id],[createdby],[modifiedby],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES ('TYPE_ID_BUSINESS' ,'BULK_PAYMENT_MULTIPLE_SUBMIT' ,null ,null ,GETDATE() ,GETDATE() ,GETDATE() ,'0' ) ;
GO

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT', 'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT', 'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [limitTypeId], [value]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'de-DE' ,'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'en-GB' ,'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'en-US' ,'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'es-ES' ,'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'fr-FR' ,'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'de-DE' ,'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'en-GB' ,'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'en-US' ,'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'es-ES' ,'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id],[Locale_id],[displayName],[displayDescription]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'fr-FR' ,'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;

INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00') ;
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00') ;
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00') ;

INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'DAILY_LIMIT', '1000.00');
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MAX_TRANSACTION_LIMIT', '500.00') ;
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'MIN_TRANSACTION_LIMIT', '1.00') ;
INSERT INTO [${dbxschemaname}].[actionlimit]([Action_id],[LimitType_id],[value]) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' ,'WEEKLY_LIMIT', '5000.00') ;
GO

INSERT INTO [${dbxschemaname}].[compositeaction]([id],[Permission_id],[Action_id],[Feature_id],[isEnabled]) VALUES ('CAID400', 'PID45', 'BULK_PAYMENT_SINGLE_SUBMIT', 'BULK_PAYMENT_REQUEST', '0');
INSERT INTO [${dbxschemaname}].[compositeaction]([id],[Permission_id],[Action_id],[Feature_id],[isEnabled]) VALUES ('CAID401', 'PID45', 'BULK_PAYMENT_MULTIPLE_SUBMIT', 'BULK_PAYMENT_REQUEST', '0');
GO

INSERT INTO [${dbxschemaname}].[rrole] ([id],[createdts],[lastmodifiedts],[softdeleteflag]) VALUES ('BULK_PAYMENT_REQUEST',GETDATE(),GETDATE(),0);
UPDATE [${dbxschemaname}].[featureaction] SET [Rrole_id] ='BULK_PAYMENT_REQUEST' WHERE [Rrole_id] = 'BULK_PAYMENT_REQUEST_SUBMIT';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'BULK_PAYMENT_SINGLE_SUBMIT,BULK_PAYMENT_MULTIPLE_SUBMIT' WHERE [id] = 'e52a6ef1-7257-4b60-87ad-adb297cc35ab';
GO

DELETE FROM [${dbxschemaname}].[customeraction] WHERE ([Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT'); 
DELETE FROM [${dbxschemaname}].[contractactionlimit] WHERE ([actionId] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE FROM [${dbxschemaname}].[groupactionlimit] WHERE ([Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE ([actionId] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE FROM [${dbxschemaname}].[actiondisplaynamedescription] WHERE ([Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE FROM [${dbxschemaname}].[actionlimit] WHERE ([Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE FROM [${dbxschemaname}].[compositeaction] WHERE ([Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT');
DELETE from [${dbxschemaname}].[featureactionroletype] WHERE [Action_id]='BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from [${dbxschemaname}].[approvalmatrix] WHERE [actionId] ='BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from [${dbxschemaname}].[dependentactions] WHERE [actionId] ='BULK_PAYMENT_REQUEST_SUBMIT' 
DELETE from [${dbxschemaname}].[dependentactions] WHERE[dependentactionId] ='BULK_PAYMENT_REQUEST_SUBMIT';

DELETE from [${dbxschemaname}].[featureaction] WHERE [id]='BULK_PAYMENT_REQUEST_SUBMIT';
GO

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID511','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ServiceRequestOverviewGeneral','Permission to view Service Request Overview in Service Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID512','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ServiceRequestOverviewServiceRequestOverview','Permission to view Service Request Overview in Service Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID513','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ServiceRequestOverviewRequestDetails','Permission to view RequestDetails in Service Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID514','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ServiceRequestOverviewDocuments','Permission to view Documents in Service Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID515','PER_TYPE_SERVICEREQUEST','SID_ACTIVE','ServiceRequestOverviewDecisions','Permission to view Decisions in Service Request Overview','0','TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID511');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID512');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID513');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID514');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SERVICING_OPS', N'PID515');
GO


UPDATE [${dbxschemaname}].[configurations]
SET bundle_id=N'DBP_CONFIG_BUNDLE', config_type=N'PREFERENCE', config_key=N'LEGAL_UNITS', description=N'Time to automatically unlock a customer (in minutes).', config_value=N'[{"id": "GB0010001","companyName": "Europe","region": "Europe","typeId": "LEGALENTITY","parentId": "GR23698574","countryCode": "EU","baseCurrency": "Euro","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": "LE for Europe"}]', target=N'SERVER', isPreLoginConfiguration=0, createdby=N'Kony dev', modifiedby=N'Kony dev', createdts='2022-08-25 10:08:25.827', lastmodifiedts='2022-08-25 10:08:25.827', synctimestamp='2022-08-25 10:08:25.827', softdeleteflag=0, companyLegalUnit=N'ALL'
WHERE configuration_id=N'110';

INSERT INTO [${dbxschemaname}].[configurations]
(configuration_id, bundle_id, config_type, config_key, description, config_value, target, isPreLoginConfiguration, createdby, modifiedby, createdts, lastmodifiedts, synctimestamp, softdeleteflag, companyLegalUnit)
VALUES(N'111', N'DBP_CONFIG_BUNDLE', N'PREFERENCE', N'MULTILEGAL_UNITS', N'Time to automatically unlock a customer (in minutes).', N'[{"id": "GB0010001","companyName": "Europe","region": "Europe","typeId": "LEGALENTITY","parentId": "GR23698574","countryCode": "EU","baseCurrency": "Euro","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": "LE for Europe"},{"id": "LE23698571","companyName": "Singapore","region": "Singapore","typeId": "LEGALENTITY","parentId": "GR23698571","countryCode": "SG","baseCurrency": "SGD","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": ""},{"id": "LE23698573","companyName": "India","region": "India","typeId": "LEGALENTITY","parentId": "GR23698573","countryCode": "IN","baseCurrency": "INR","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": ""}]', N'SERVER', 0, N'Kony dev', N'Kony dev', '2022-09-20 16:38:42.107', '2022-09-20 16:38:42.107', '2022-09-20 16:38:42.107', 0, N'ALL');
GO

INSERT INTO [${dbxschemaname}].[userlob] ([userId], [lobId], [softdeleteflag], [companyLegalUnit]) VALUES (N'f7c2e82e-cda1-473e-a26d-eb493973f575', N'TYPE_ID_CORPORATE', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[userlob] ([userId], [lobId], [softdeleteflag], [companyLegalUnit]) VALUES (N'aa477169-4bae-46ca-8c98-f5a65323aea6', N'TYPE_ID_CORPORATE', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[userlob] ([userId], [lobId], [softdeleteflag], [companyLegalUnit]) VALUES (N'e15aa8ed-ff87-4a1c-a152-cd21d3659506', N'TYPE_ID_CORPORATE', N'0', N'ALL');

INSERT INTO [${dbxschemaname}].[internalusermanager] ([userId], [manager], [softdeleteflag], [companyLegalUnit]) VALUES (N'f7c2e82e-cda1-473e-a26d-eb493973f575', N'bfleck', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[internalusermanager] ([userId], [manager], [softdeleteflag], [companyLegalUnit]) VALUES (N'aa477169-4bae-46ca-8c98-f5a65323aea6', N'bfleck', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[internalusermanager] ([userId], [manager], [softdeleteflag], [companyLegalUnit]) VALUES (N'e15aa8ed-ff87-4a1c-a152-cd21d3659506', N'bfleck', N'0', N'ALL');

INSERT INTO [${dbxschemaname}].[useraddress] ([User_id], [Address_id], [Type_id], [softdeleteflag], [companyLegalUnit]) VALUES (N'f7c2e82e-cda1-473e-a26d-eb493973f575', N'DMSADDR1', N'ADR_TYPE_WORK',N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[useraddress] ([User_id], [Address_id], [Type_id], [softdeleteflag], [companyLegalUnit]) VALUES (N'aa477169-4bae-46ca-8c98-f5a65323aea6', N'DMSADDR1', N'ADR_TYPE_WORK',N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[useraddress] ([User_id], [Address_id], [Type_id], [softdeleteflag], [companyLegalUnit]) VALUES (N'e15aa8ed-ff87-4a1c-a152-cd21d3659506', N'DMSADDR1', N'ADR_TYPE_WORK',N'0', N'ALL');

INSERT INTO [${dbxschemaname}].[internalusertype] ([userId], [userType], [softdeleteflag], [companyLegalUnit]) VALUES (N'f7c2e82e-cda1-473e-a26d-eb493973f575', N'INTERNAL', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[internalusertype] ([userId], [userType], [softdeleteflag], [companyLegalUnit]) VALUES (N'aa477169-4bae-46ca-8c98-f5a65323aea6', N'INTERNAL', N'0', N'ALL');
INSERT INTO [${dbxschemaname}].[internalusertype] ([userId], [userType], [softdeleteflag], [companyLegalUnit]) VALUES (N'e15aa8ed-ff87-4a1c-a152-cd21d3659506', N'INTERNAL', N'0', N'ALL');
GO

/* SQL Scripts for InfinityWealth - Advisory Portfolios - Entitlements and Permissions */

GO
-- Action :: PORTFOLIO_HEALTH_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH-VIEW');

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','Portfolio Health','Portfolio Health', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '81', '0', 'GB0010001' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH', 'en-GB', 'Portfolio Health', 'Portfolio Health', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH', 'en-US', 'Portfolio Health', 'Portfolio Health', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH-VIEW','Portfolio Health View','Portfolio Health View','0','0','0','22', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_VIEW','en-GB', 'Portfolio Health View', 'Portfolio Health View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_VIEW','en-US', 'Portfolio Health View', 'Portfolio Health View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID800', 'PID45', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_SUMMARY_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_SUMMARY-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_SUMMARY-VIEW','Portfolio Health Summary View','Portfolio Health Summary View','0','0','0','23', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','en-GB', 'Portfolio Health Summary View', 'Portfolio Health Summary View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','en-US', 'Portfolio Health Summary View', 'Portfolio Health Summary View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID801', 'PID45', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_ASSET_ALLOCATION-VIEW','Portfolio Health Asset Allocation View','Portfolio Health Asset Allocation View','0','0','0','24', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','en-GB', 'Portfolio Health Asset Allocation View', 'Portfolio Health Asset Allocation View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','en-US', 'Portfolio Health Asset Allocation View', 'Portfolio Health Asset Allocation View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID802', 'PID45', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_RISK_ANALYSIS-VIEW','Portfolio Health Risk Analysis View','Portfolio Health Risk Analysis View','0','0','0','25', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','en-GB', 'Portfolio Health Risk Analysis View', 'Portfolio Health Risk Analysis View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','en-US', 'Portfolio Health Risk Analysis View', 'Portfolio Health Risk Analysis View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID803', 'PID45', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS-VIEW','Portfolio Health Investment Constraints View','Portfolio Health Investment Constraints View','0','0','0','26', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','en-GB', 'Portfolio Health Investment Constraints View', 'Portfolio Health Investment Constraints View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','en-US', 'Portfolio Health Investment Constraints View', 'Portfolio Health Investment Constraints View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID804', 'PID45', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS-VIEW','Portfolio Health Recommended Instruments View','Portfolio Health Recommended Instruments View','0','0','0','27', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','en-GB', 'Portfolio Health Recommended Instruments View', 'Portfolio Health Recommended Instruments View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','en-US', 'Portfolio Health Recommended Instruments View', 'Portfolio Health Recommended Instruments View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID805', 'PID45', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_CONTACT_ADVISOR-VIEW','Portfolio Health Contact Advisor View','Portfolio Health Contact Advisor View','0','0','0','28', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','en-GB', 'Portfolio Health Contact Advisor View', 'Portfolio Health Contact Advisor View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','en-US', 'Portfolio Health Contact Advisor View', 'Portfolio Health Contact Advisor View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID806', 'PID45', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: PORTFOLIO_REVIEW_STRATEGY_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('PORTFOLIO_REVIEW_STRATEGY-VIEW');

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary],[companyLegalUnit]) VALUES ('REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','Review Strategy','Review Strategy', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '82', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('REVIEW_STRATEGY', 'en-GB', 'Review Strategy', 'Review Strategy', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('REVIEW_STRATEGY', 'en-US', 'Review Strategy', 'Review Strategy', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'REVIEW_STRATEGY', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_REVIEW_STRATEGY-VIEW','Portfolio Review Strategy View','Portfolio Review Strategy View','0','0','0','29', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','en-GB', 'Portfolio Review Strategy View', 'Portfolio Review Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','en-US', 'Portfolio Review Strategy View', 'Portfolio Review Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID807', 'PID45', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: MY_STRATEGY_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('MY_STRATEGY-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('MY_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','MY_STRATEGY-VIEW','My Strategy View','My Strategy View','0','0','0','30', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('MY_STRATEGY_VIEW','en-GB', 'My Strategy View', 'My Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('MY_STRATEGY_VIEW','en-US', 'My Strategy View', 'My Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'MY_STRATEGY_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID808', 'PID45', 'MY_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'MY_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'MY_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'MY_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: MY_STRATEGY_CHANGE_STRATEGY_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('MY_STRATEGY_CHANGE_STRATEGY-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId],[companyLegalUnit]) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','MY_STRATEGY_CHANGE_STRATEGY-VIEW','My Strategy Change Strategy View','My Strategy Change Strategy View','0','0','0','31', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','en-GB', 'My Strategy Change Strategy View', 'My Strategy Change Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription],[companyLegalUnit]) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','en-US', 'My Strategy Change Strategy View', 'My Strategy Change Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id],[companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled],[companyLegalUnit]) VALUES ('CAID809', 'PID45', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag],[companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: RECOMMENDED_STRATEGY_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('RECOMMENDED_STRATEGY-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-VIEW','Recommended Strategy View','Recommended Strategy View','0','0','0','32', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_VIEW','en-GB', 'Recommended Strategy View', 'Recommended Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_VIEW','en-US', 'Recommended Strategy View', 'Recommended Strategy View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID810', 'PID45', 'RECOMMENDED_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: RECOMMENDED_STRATEGY_USE_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('RECOMMENDED_STRATEGY_USE-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY_USE-VIEW','Recommended Strategy Use View','Recommended Strategy Use View','0','0','0','33', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','en-GB', 'Recommended Strategy Use View', 'Recommended Strategy Use View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','en-US', 'Recommended Strategy Use View', 'Recommended Strategy Use View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_USE_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID811', 'PID45', 'RECOMMENDED_STRATEGY_USE_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: SUITABILITY_PROFILE_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('SUITABILITY_PROFILE-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SUITABILITY_PROFILE-VIEW','Suitability Profile View','Suitability Profile View','0','0','0','34', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001'); 

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_VIEW','en-GB', 'Suitability Profile View', 'Suitability Profile View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_VIEW','en-US', 'Suitability Profile View', 'Suitability Profile View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'SUITABILITY_PROFILE_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID812', 'PID45', 'SUITABILITY_PROFILE_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: SUITABILITY_PROFILE_REVIEW_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('SUITABILITY_PROFILE_REVIEW-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SUITABILITY_PROFILE_REVIEW-VIEW','Suitability Profile Review View','Suitability Profile Review View','0','0','0','35', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','en-GB', 'Suitability Profile Review View', 'Suitability Profile Review View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','en-US', 'Suitability Profile Review View', 'Suitability Profile Review View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID813', 'PID45', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: STRATEGY_ALLOCATION_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('STRATEGY_ALLOCATION-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('STRATEGY_ALLOCATION_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','STRATEGY_ALLOCATION-VIEW','Strategy Allocation View','Strategy Allocation View','0','0','0','36', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('STRATEGY_ALLOCATION_VIEW','en-GB', 'Strategy Allocation View', 'Strategy Allocation View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('STRATEGY_ALLOCATION_VIEW','en-US', 'Strategy Allocation View', 'Strategy Allocation View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'STRATEGY_ALLOCATION_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID814', 'PID45', 'STRATEGY_ALLOCATION_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: INVESTMENT_PROPOSAL_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('INVESTMENT_PROPOSAL-VIEW');

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','Investment Proposal','Investment Proposal', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '83', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL', 'en-GB', 'Investment Proposal', 'Investment Proposal', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL', 'en-US', 'Investment Proposal', 'Investment Proposal', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL-VIEW','Investment Proposal View','Investment Proposal View','0','0','0','37', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_VIEW','en-GB', 'Investment Proposal View', 'Investment Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_VIEW','en-US', 'Investment Proposal View', 'Investment Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID815', 'PID45', 'INVESTMENT_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001'); 

GO
-- Action :: INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL_PAST_PROPOSAL-VIEW','Investment Proposal Past Proposal View','Investment Proposal Past Proposal View','0','0','0','38', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','en-GB', 'Investment Proposal Past Proposal View', 'Investment Proposal Past Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','en-US', 'Investment Proposal Past Proposal View', 'Investment Proposal Past Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID816', 'PID45', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL-VIEW');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL_NEW_PROPOSAL-VIEW','Investment Proposal New Proposal View','Investment Proposal New Proposal View','0','0','0','39', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','en-GB', 'Investment Proposal New Proposal View', 'Investment Proposal New Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','en-US', 'Investment Proposal New Proposal View', 'Investment Proposal New Proposal View', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID817', 'PID45', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: RECOMMENDED_STRATEGY_CONFIRMATION
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('RECOMMENDED_STRATEGY-CONFIRMATION');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-CONFIRMATION','Recommended Strategy Confirmation','Recommended Strategy Confirmation','0','0','0','40', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','en-GB', 'Recommended Strategy Confirmation', 'Recommended Strategy Confirmation', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','en-US', 'Recommended Strategy Confirmation', 'Recommended Strategy Confirmation', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID818', 'PID45', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT','Recommended Strategy Acknowledgement','Recommended Strategy Acknowledgement','0','0','0','41', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','en-GB', 'Recommended Strategy Acknowledgement', 'Recommended Strategy Acknowledgement', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','en-US', 'Recommended Strategy Acknowledgement', 'Recommended Strategy Acknowledgement', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID819', 'PID45', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT_CHART');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT_CHART','Recommended Strategy Acknowledgement Chart','Recommended Strategy Acknowledgement Chart','0','0','0','42', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','en-GB', 'Recommended Strategy Acknowledgement Chart', 'Recommended Strategy Acknowledgement Chart', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','en-US', 'Recommended Strategy Acknowledgement Chart', 'Recommended Strategy Acknowledgement Chart', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID820', 'PID45', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: CHANGE_STRATEGY_CONFIRMATION
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('CHANGE_STRATEGY-CONFIRMATION');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('CHANGE_STRATEGY_CONFIRMATION','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHANGE_STRATEGY-CONFIRMATION','Change Strategy Confirmation','Change Strategy Confirmation','0','0','0','43', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHANGE_STRATEGY_CONFIRMATION','en-GB', 'Change Strategy Confirmation', 'Change Strategy Confirmation', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHANGE_STRATEGY_CONFIRMATION','en-US', 'Change Strategy Confirmation', 'Change Strategy Confirmation', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'CHANGE_STRATEGY_CONFIRMATION', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID821', 'PID45', 'CHANGE_STRATEGY_CONFIRMATION', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: CHOOSE_STRATEGY_ACKNOWLEDGEMENT
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('CHOOSE_STRATEGY-ACKNOWLEDGEMENT'); 

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHOOSE_STRATEGY-ACKNOWLEDGEMENT','Choose Strategy Acknowledgement','Choose Strategy Acknowledgement','0','0','0','44', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','en-GB', 'Choose Strategy Acknowledgement', 'Choose Strategy Acknowledgement', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','en-US', 'Choose Strategy Acknowledgement', 'Choose Strategy Acknowledgement', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID822', 'PID45', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('CHOOSE_STRATEGY-ACKNOWLEDGEMENT_CHART');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHOOSE_STRATEGY-ACKNOWLEDGEMENT_CHART','Choose Strategy Acknowledgement Chart','Choose Strategy Acknowledgement Chart','0','0','0','45', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','en-GB', 'Choose Strategy Acknowledgement Chart', 'Choose Strategy Acknowledgement Chart', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','en-US', 'Choose Strategy Acknowledgement Chart', 'Choose Strategy Acknowledgement Chart', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'GB0010001');

INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID823', 'PID45', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

GO
-- Action :: INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL-CREATE');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','MONETARY','INVESTMENT_PROPOSAL_NEW_PROPOSAL-CREATE','Investment Proposal New Proposal Create','Investment Proposal New Proposal Create','0','0','0','46', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','en-GB', 'Investment Proposal New Proposal Create', 'Investment Proposal New Proposal Create', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','en-US', 'Investment Proposal New Proposal Create', 'Investment Proposal New Proposal Create', 'GB0010001');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [companyLegalUnit]) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'GB0010001');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled], [companyLegalUnit]) VALUES ('CAID824', 'PID45', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL', '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES (NEWID(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', GETDATE(), GETDATE(), GETDATE(), '0', 'GB0010001');

INSERT INTO [${dbxschemaname}].[actionlimit] ([Action_id], [LimitType_id], [value], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'GB0010001');

INSERT INTO [${dbxschemaname}].[actionlimit] ([Action_id], [LimitType_id], [value], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'GB0010001');

INSERT INTO [${dbxschemaname}].[actionlimit] ([Action_id], [LimitType_id], [value], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'GB0010001');

INSERT INTO [${dbxschemaname}].[actionlimit] ([Action_id], [LimitType_id], [value], [companyLegalUnit]) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'GB0010001');


INSERT INTO [${dbxschemaname}].[externalalertsytem] ([systemtype], [systemid]) VALUES('Wealth FO', '3');

INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [softdeleteflag], [externalSystem]) VALUES('RISK_PROFILE_EXPIRY','TRANSFER_RECIPIENT','Risk Profile Expiry','0','3');
INSERT INTO [${dbxschemaname}].[eventsubtype] ([id], [eventtypeid], [Name], [softdeleteflag], [externalSystem]) VALUES('INVESTMENT_PROPOSAL','TRANSFER_RECIPIENT','Investment Proposal','0','3');



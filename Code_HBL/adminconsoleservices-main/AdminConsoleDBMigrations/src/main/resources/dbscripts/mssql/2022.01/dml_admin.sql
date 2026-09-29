GO
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'CardManagementServices',[object_name] = 'RequestCreditCard' ,[operation] = 'applyForCreditCard' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Cards' AND [operation] = 'applyForCreditCard';
GO
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES (N'PID437', N'PER_TYPE_FACILITY', N'SID_ACTIVE', N'FacilityOverviewViewFundingPosition', N'Permission to view funding position form in facility overview', N'0', N'TRUE');
GO
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES (N'PID438', N'PER_TYPE_FACILITY', N'SID_ACTIVE', N'FacilityOverviewAddFundingPosition', N'Permission to add funding position form in facility overview', N'0', N'TRUE');
GO
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES (N'PID439', N'PER_TYPE_FACILITY', N'SID_ACTIVE', N'FacilityOverviewUpdateFundingPosition', N'Permission to update funding position form in facility overview', N'0', N'TRUE');
GO
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES (N'PID440', N'PER_TYPE_FACILITY', N'SID_ACTIVE', N'FacilityOverviewDeleteFundingPosition', N'Permission to delete funding position form in facility overview', N'0', N'TRUE');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID438', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID439', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailUW', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailOps', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAIL_SYSTEM_ADMIN', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAIL_SYSTEM_ADMIN', N'PID438', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAIL_SYSTEM_ADMIN', N'PID439', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAIL_SYSTEM_ADMIN', N'PID440', N'0');
GO
DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = 'RID_RetailRM') and ([Permission_id] = 'PID438');
GO
DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = 'RID_RETAIL_SYSTEM_ADMIN') and ([Permission_id] = 'PID438');
GO
DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = 'RID_RETAIL_SYSTEM_ADMIN') and ([Permission_id] = 'PID440');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID439', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailSupervisor', N'PID437', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailSupervisor', N'PID439', N'0');
GO

INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES ('PER_TYPE_RISK_SCORECARD', 'Permission Type Risk Score card');

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID441','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','RequestOverviewViewRiskScorecard','Permission to view risk score card in Request Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID442','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','FacilityOverviewViewRiskScorecard','Permission to view risk score card in Facility Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID443','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','EntityOverviewViewRiskScorecard','Permission to view risk score card in Entity Overview','0','TRUE');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID441');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_UW', N'PID441');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_RM', N'PID441');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMERMSupervisor', N'PID441', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID441', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID442');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_UW', N'PID442');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_RM', N'PID442');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMERMSupervisor', N'PID442', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID442', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID443');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_UW', N'PID443');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_RM', N'PID443');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMERMSupervisor', N'PID443', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID443', N'0');
GO

INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES ('PER_TYPE_ADDITIONAL_INSTRUCTION' ,'Permission Type Additional Instruction');

INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID444','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewViewAdditionalInstruction','Permission to view additional instruction in Facility Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID445','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewAddAdditionalInstruction','Permission to add additional instruction in Facility Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID446','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewUpdateAdditionalInstruction','Permission to update additional instruction in Facility Overview','0','TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES ('PID447','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewDeleteAdditionalInstruction','Permission to delete additional instruction in Facility Overview','0','TRUE');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_UNDERWRITER', 'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SECRETARY', 'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_OPERATIONS', 'PID444');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_RM', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_UW', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_OPS', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID444');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailRM', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailUW', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailOps', N'PID444');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailSupervisor', N'PID444');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID445');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_OPERATIONS', 'PID445');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_OPS', N'PID445');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID445');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailRM', N'PID445');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailUW', N'PID445');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailOps', N'PID445');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailSupervisor', N'PID445');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID446');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_OPERATIONS', 'PID446');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_OPS', N'PID446');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID446');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailRM', N'PID446');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailUW', N'PID446');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailOps', N'PID446');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailSupervisor', N'PID446');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERVISOR', 'PID447');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_OPERATIONS', 'PID447');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_OPS', N'PID447');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_SME_SUPERVISOR', N'PID447');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailUW', N'PID447');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES (N'RID_RetailSupervisor', N'PID447');
GO

INSERT INTO [${dbxschemaname}].[feature]([id],[App_id],[name],[description],[Type_id],[Status_id],[Service_Fee] ,[DisplaySequence] ,[isPrimary] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) values ('IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings.' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO [${dbxschemaname}].[rrole] ([id] ,[createdts] ,[lastmodifiedts] ,[softdeleteflag] ) VALUES ('IMPORT_LC_DRAWINGS-VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO [${dbxschemaname}].[rrole] ([id] ,[createdts] ,[lastmodifiedts] ,[softdeleteflag] ) VALUES ('IMPORT_LC_DRAWINGS-SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO [${dbxschemaname}].[featureaction] ([id] ,[Feature_id] ,[App_id] ,[Type_id] ,[Rrole_id] ,[name] ,[description] ,[isAccountLevel] ,[isMFAApplicable] ,[MFA_id] ,[isPrimary] ,[DisplaySequence] ,[dependency] ,[softdeleteflag] ,[accesspolicyId] ,[actionlevelId] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' ,'IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_DRAWINGS-VIEW' ,'View the Import LC Drawings' ,'View the Import LC Drawings and their details' , 1 , 0 , null , 0 , 10 , 'IMPORT_LC_VIEW' , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;
INSERT INTO [${dbxschemaname}].[featureaction] ([id] ,[Feature_id] ,[App_id] ,[Type_id] ,[Rrole_id] ,[name] ,[description] ,[isAccountLevel] ,[isMFAApplicable] ,[MFA_id] ,[isPrimary] ,[DisplaySequence] ,[dependency] ,[softdeleteflag] ,[accesspolicyId] ,[actionlevelId] ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' ,'IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_DRAWINGS-SUBMIT' ,'Submit an Import LC Drawing' ,'Manage & Submit for approval an Import LC Drawing' , 1 , 0 , null , 0 , 10 , 'IMPORT_LC_DRAWINGS_VIEW' , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id] ,[Group_id] ,[Action_id] ,[LimitType_id] ,[value] ,[createdby] ,[modifiedby] ,[softdeleteflag] ) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id] ,[Group_id] ,[Action_id] ,[LimitType_id] ,[value] ,[createdby] ,[modifiedby] ,[softdeleteflag] ) VALUES (NEWID() ,'GROUP_CREATOR' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id] ,[Group_id] ,[Action_id] ,[LimitType_id] ,[value] ,[createdby] ,[modifiedby] ,[softdeleteflag] ) VALUES (NEWID() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id] ,[Group_id] ,[Action_id] ,[LimitType_id] ,[value] ,[createdby] ,[modifiedby] ,[softdeleteflag] ) VALUES (NEWID() ,'GROUP_CREATOR' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id] ,[Action_id] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id] ,[Action_id] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id] , [serviceDefinitionId] , [actionId] , [createdts] , [lastmodifiedts] , [synctimestamp] , [softdeleteflag] ) VALUES (NEWID() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id] , [serviceDefinitionId] , [actionId] , [createdts] , [lastmodifiedts] , [synctimestamp] , [softdeleteflag] ) VALUES (NEWID() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_DRAWINGS_SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id] , [serviceDefinitionId] , [actionId] , [createdts] , [lastmodifiedts] , [synctimestamp] , [softdeleteflag] ) VALUES (NEWID() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id] , [serviceDefinitionId] , [actionId] , [createdts] , [lastmodifiedts] , [synctimestamp] , [softdeleteflag] ) VALUES (NEWID() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_DRAWINGS_SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('IMPORT_LC_DRAWINGS' ,'en-GB' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id] ,[Locale_id] ,[displayName] ,[displayDescription] ,[createdby] ,[modifiedby] ,[createdts] ,[lastmodifiedts] ,[synctimestamp] ,[softdeleteflag] ) VALUES ('IMPORT_LC_DRAWINGS' ,'en-US' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a87zxc-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'getImportLCDrawings' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a87bfc-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'getImportLCDrawingById' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a87kat-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawingSummary' ,'generate' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a87k4d-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'createImportLCDrawing' ,'IMPORT_LC_DRAWINGS_SUBMIT' ) ;
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a879br-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'submitImportLCDrawing' ,'IMPORT_LC_DRAWINGS_SUBMIT' ) ;
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id] ,[service_name] ,[object_name] ,[operation] ,[permissions] ) VALUES ('e7a870v4-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LetterOfCredit' ,'getLetterOfCreditsById' ,'IMPORT_LC_VIEW' ) ;

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id] , [Feature_id] ) VALUES ('TYPE_ID_BUSINESS' , 'IMPORT_LC_DRAWINGS' ) ;

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'de-DE' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'en-GB' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'en-US' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'es-ES' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'fr-FR' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'de-DE' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'en-GB' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id], [Locale_id], [displayName], [displayDescription])  VALUES('IMPORT_LC_DRAWINGS_SUBMIT', 'en-US', 'Import LC Drawings Submit', 'Import LC Drawings Submit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id], [Locale_id], [displayName], [displayDescription])  VALUES('IMPORT_LC_DRAWINGS_SUBMIT', 'es-ES', 'Import LC Drawings Submit', 'Import LC Drawings Submit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription]([Action_id] , [Locale_id] , [displayName] , [displayDescription] ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'fr-FR' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;
GO

DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = 'RID_RETAILONBOARDING_RM') and ([Permission_id] = 'PID270');
DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = 'RID_RETAILONBOARDING_OPS') and ([Permission_id] = 'PID270');
GO

INSERT INTO [${dbxschemaname}].rrole ([id]) VALUES ('IMPORT_LC-APPROVE');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [status], [accesspolicyId], [actionlevelId], [isApprovalAction]) VALUES ('IMPORT_LC_APPROVE', 'IMPORT_LC', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'IMPORT_LC-APPROVE', 'Approve Import letter of credit', 'The clerk and manager can approve LC', '1', '0', '0', '10', 'SID_ACTION_ACTIVE', 'APPROVE', 'ACCOUNT_LEVEL', '1');

UPDATE [${dbxschemaname}].[featureaction] SET [approveFeatureAction] = 'IMPORT_LC_APPROVE' WHERE ([id] = 'IMPORT_LC_CREATE');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_APPROVE');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('IMPORT_LC_APPROVE', 'de-DE', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('IMPORT_LC_APPROVE', 'en-GB', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('IMPORT_LC_APPROVE', 'en-US', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('IMPORT_LC_APPROVE', 'es-ES', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('IMPORT_LC_APPROVE', 'fr-FR', 'Approve Letter of credit', 'Approve Letter of credit');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('16755fc0-6516-11fb-ae93-0242ac130002', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'IMPORT_LC_APPROVE');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('16756222-6516-11fb-ae93-0242ac130002', '5801fa32-a416-45b6-af01-b22e2de93777', 'IMPORT_LC_APPROVE');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby]) VALUES ('5346e16f-a234-41fb-93dc-80d5fd3c673s', 'GROUP_ADMINISTRATOR', 'IMPORT_LC_APPROVE', 'UID10');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby]) VALUES ('aac00784-791b-11fb-9300-00090faa1002', 'DEFAULT_GROUP', 'IMPORT_LC_APPROVE', 'UID11');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('d6704709-0015-45fb-9640-187a51fabbc3', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'IMPORT_LC_APPROVE');

INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('13611a1c-6517-11fb-ae93-0242ac130002', '7321457251', '1425958', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('13611c60-6517-11fb-ae93-0242ac130002', '7321457251', '1578660', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('13611e2c-6517-11fb-ae93-0242ac130002', '4204010299', '1065631', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('13611efe-6517-11fb-ae93-0242ac130002', '4204010299', '1605506', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
GO
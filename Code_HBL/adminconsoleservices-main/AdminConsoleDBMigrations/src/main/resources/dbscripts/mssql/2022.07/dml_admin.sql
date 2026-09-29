
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID489',N'PER_TYPE_FACILITY',N'SID_ACTIVE',N'FacilityOverviewViewRemortgageInformation',N'Permission to View Remortgage Information section in Facility Overview',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID490',N'PER_TYPE_FACILITY',N'SID_ACTIVE',N'FacilityOverviewEditRemortgageInformation',N'Permission to Update Remortgage Information section in Facility Overview',N'0',N'TRUE');
UPDATE [${dbxschemaname}].[permission] SET [Name] = 'FacilityOverviewUpdateRemortgageInformation' WHERE ([id] = N'PID490');
GO

INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID489', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID489', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_UW', N'PID489', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_CREDIT_APPROVER', N'PID489', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID489', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID489', N'0');
GO
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM', N'PID490', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_RM_SUPERVISIOR', N'PID490', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORTGAGE_OPS', N'PID490', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission]([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_MORGAGE_SUPERVISIOR', N'PID490', N'0');
GO

DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = N'RID_MORTGAGE_UW') and ([Permission_id] = N'PID439');
DELETE FROM [${dbxschemaname}].[rolepermission] WHERE ([Role_id] = N'RID_MORTGAGE_CREDIT_APPROVER') and ([Permission_id] = N'PID439');

INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_EARLYREPAYMENT','Early Repayment','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_CHANGEREPAYMENTDATE','Change Repayment Date','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_CHANGEREPAYMENTACCOUNT','Change Repayment Account','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_INTERESTRATECHANGE','Interest Rate Change','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_REMOVENAMEDPERSON','Remove Named Person','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_DISBURSEMENTREQUEST','Progressive Disbursement request','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_PROPERTYVALUATION','Property Valuation','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_ADDRESSCHANGE','Change of Address','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_REPAYMENTHOLIDAY','Repayment Holiday','Kony User','Kony Dev');
INSERT INTO [${dbxschemaname}].[requestcategory] ([id], [Name],[createdby],[modifiedby]) VALUES ('RCID_INCREASELOANAMOUNT','Increase Loan Amount','Kony User','Kony Dev');

GO

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue], [createdby]) VALUES ('PID498', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', 'EntityOverviewViewAccounts', 'Permission to view accounts in Request Overview', '0', 'TRUE', NULL);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue], [createdby]) VALUES ('PID495', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', 'EntityOverviewAddAccount', 'Permission to add accounts in Facility Overview', '0', 'TRUE', NULL);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue], [createdby]) VALUES ('PID496', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', 'EntityOverviewUpdateAccount', 'Permission to edit accounts in Entity Overview', '0', 'TRUE', NULL);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue], [createdby]) VALUES ('PID497', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', 'EntityOverviewDeleteAccount', 'Permission to delete accounts in Entity Overview', '0', 'TRUE', NULL);

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM_SUPERVISIOR', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM_SUPERVISIOR', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RM', N'PID498', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID498', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM_SUPERVISIOR', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM_SUPERVISIOR', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RM', N'PID495', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID495', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM_SUPERVISIOR', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM_SUPERVISIOR', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RM', N'PID496', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID496', N'0');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RETAILONBOARDING_RM_SUPERVISIOR', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SMEONBOARDING_RM_SUPERVISIOR', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRM', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RetailRMSupervisor', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RM', N'PID497', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SME_RMSUPERVISOR', N'PID497', N'0');
GO

INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES (N'PER_TYPE_EXPOSURES' ,N'Permission Type  exposure');
INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description]) VALUES (N'PER_TYPE_OPSEXPOSURES' ,N'Permission Type  exposure');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID502',N'PER_TYPE_EXPOSURES',N'SID_ACTIVE',N'RequestOverviewViewExposure',N'Permission to view exposure in request overview section',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID499',N'PER_TYPE_EXPOSURES',N'SID_ACTIVE',N'RequestOverviewUpdateExposure',N'Permission to update exposure in request overview section',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID500',N'PER_TYPE_EXPOSURES',N'SID_ACTIVE',N'EntityOverviewViewExposure',N'Permission to view exposure in entity overview section',N'0',N'TRUE');
INSERT INTO [${dbxschemaname}].[permission] ([id],[Type_id],[Status_id],[Name],[Description],[isComposite],[PermissionValue]) VALUES (N'PID501',N'PER_TYPE_OPSEXPOSURES',N'SID_ACTIVE',N'RequestOverviewFetchExposure',N'Permission to fetch exposure in request overview section',N'0',N'TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RELATIONSHIP_MANAGER', N'PID502', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID502', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_OPERATIONS', N'PID502', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID502', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SECRETARY', N'PID502', N'0');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RELATIONSHIP_MANAGER', N'PID499', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID499', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_OPERATIONS', N'PID499', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID499', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SECRETARY', N'PID499', N'0');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RELATIONSHIP_MANAGER', N'PID500', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID500', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_OPERATIONS', N'PID500', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID500', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SECRETARY', N'PID500', N'0');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_RELATIONSHIP_MANAGER', N'PID501', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_UNDERWRITER', N'PID501', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SUPERVISOR', N'PID501', N'0');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES (N'RID_SECRETARY', N'PID501', N'0');
GO


INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES ('PID700', 'PER_TYPE_ROLE', 'SID_ACTIVE', 'ApproveInternalUserRole', '[permission] to approve a newly created or updated role', '0', 'TRUE');
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [softdeleteflag]) VALUES ('RID_SUPERADMIN', 'PID700', '0');
GO

INSERT INTO [${dbxschemaname}].[permissionapprovals] ([expAPIOperationName], [expAPINickName], [approvalPermissionId], [approvalPermissionName], [isApprovalRequired]) VALUES ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'PID700', 'ApproveInternalUserRole', '1');
INSERT INTO [${dbxschemaname}].[permissionapprovals] ([expAPIOperationName], [expAPINickName], [approvalPermissionId], [approvalPermissionName], [isApprovalRequired]) VALUES ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'PID700', 'ApproveInternalUserRole', '1');
GO



INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery], [createdby]) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'role_approval', 'role', 'id', '1', '0', '1', '{\"query1\": {\"key\": \"id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery], [createdby]) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'proc', 'rolepermission_data_movement_approval_proc', '_requestId,_context', '2', '0', '1', '{}', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery], [createdby]) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id', '3', '0', '1', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery], [createdby]) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '4', '1', '0', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"ne\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"userIds\",\"arrayconcatinator\": \"or\"}}', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery], [createdby]) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '5', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"userIds\",\"arrayconcatinator\": \"or\"}}', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'role_approval', 'role', 'id', '1', '0', '1', '{\"query1\": {\"key\": \"id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id,servicedefinitionId', '2', '1', '0', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"servicedefinitionId\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\": \"removedServiceDefinitionsArray\",\"arrayconcatinator\": \"or\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id,servicedefinitionId', '3', '0', '1', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"servicedefinitionId\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\": \"removedServiceDefinitionsArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '4', '1', '0', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"ne\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"roleAssignedToUsersArray\",\"arrayconcatinator\": \"or\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '5', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\":\"roleRemovedFromUsersArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'proc', 'rolepermission_delete_approval_proc', '_roleId,_PermissionIds,_requestId,_context', '6', '1', '0', '{\"query1\": {\"key\": \"_roleId\",\"operator\": \"\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"\",\"query2\": {\"key\": \"_PermissionIds\",\"operator\": \"\",\"datatype\": \"string\",\"value\":\"permissionsRemoved\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'rolepermission_approval', 'rolepermission', 'Role_id,Permission_id', '7', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"Permission_id\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\": \"permissionsRemovedArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO [${dbxschemaname}].[permissionapprovalconfig] ([expAPIOperationName], [expAPINickName], [approvalTableName], [originalTableName], [keyColumnNames], [updateSequence], [cleanup], [update], [sqlquery]) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'rolecompositeaction_approval', 'rolecompositeaction', 'Role_id,CompositeAction_id', '8', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}');
GO


INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('4b76967e-f383-11ec-b939-0242ac120002', 'MakerCheckerWorkflow', 'ApprovalRequest', 'invokeApprovalWorkflow', 'ApproveInternalUserRole');
GO

UPDATE [${dbxschemaname}].[configurations] SET [config_value] = '{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"Mortgages"}' WHERE ([configuration_id] = '172');
GO

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]([id],[serviceDefinitionId],[actionId],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES (NEWID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_VIEW',GETDATE(),GETDATE(),GETDATE(),'0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]([id],[serviceDefinitionId],[actionId],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES (NEWID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_DELETE',GETDATE(),GETDATE(),GETDATE(),'0');
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit]([id],[serviceDefinitionId],[actionId],[createdts],[lastmodifiedts],[synctimestamp],[softdeleteflag]) VALUES (NEWID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_CREATE_EDIT',GETDATE(),GETDATE(),GETDATE(),'0');
GO

INSERT [${dbxschemaname}].[statustype] ([id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'SID_CUS_ERASURE_INPROGRESS', N'Customer Erasure Inprogress', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT [${dbxschemaname}].[statustype] ([id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'SID_CUS_ERASURE_COMPLETED', N'Customer Erasure Completed', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
GO
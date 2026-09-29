INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID489','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewViewRemortgageInformation','Permission to View Remortgage Information section in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID490','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewEditRemortgageInformation','Permission to Update Remortgage Information section in Facility Overview','0','TRUE');

UPDATE `permission` SET `Name` = 'FacilityOverviewUpdateRemortgageInformation' WHERE (`id` = 'PID490');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID489', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID489', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_UW', 'PID489', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_CREDIT_APPROVER', 'PID489', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_OPS', 'PID489', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORGAGE_SUPERVISIOR', 'PID489', '0');


INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID490', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID490', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_OPS', 'PID490', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORGAGE_SUPERVISIOR', 'PID490', '0');

DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_MORTGAGE_UW') and (`Permission_id` = 'PID439');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_MORTGAGE_CREDIT_APPROVER') and (`Permission_id` = 'PID439');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_EXPOSURES' ,'Permission Type  exposure');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID491','PER_TYPE_EXPOSURES','SID_ACTIVE','RequestOverviewViewExposure','Permission to view exposure in request overview section','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID492','PER_TYPE_EXPOSURES','SID_ACTIVE','RequestOverviewUpdateExposure','Permission to update exposure in request overview section','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID493','PER_TYPE_EXPOSURES','SID_ACTIVE','EntityOverviewViewExposure','Permission to view exposure in entity overview section','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID491', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID491', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_OPERATIONS', 'PID491', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID491', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SECRETARY', 'PID491', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID492', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID492', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_OPERATIONS', 'PID492', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID492', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SECRETARY', 'PID492', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID493', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID493', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_OPERATIONS', 'PID493', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID493', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SECRETARY', 'PID493', '0');

INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_EARLYREPAYMENT','Early Repayment','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_CHANGEREPAYMENTDATE','Change Repayment Date','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_CHANGEREPAYMENTACCOUNT','Change Repayment Account','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_INTERESTRATECHANGE','Interest Rate Change','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_REMOVENAMEDPERSON','Remove Named Person','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_DISBURSEMENTREQUEST','Progressive Disbursement request','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_PROPERTYVALUATION','Property Valuation','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_ADDRESSCHANGE','Change of Address','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_REPAYMENTHOLIDAY','Repayment Holiday','Kony User','Kony Dev');
INSERT INTO `requestcategory` (`id`, `Name`,`createdby`,`modifiedby`) VALUES ('RCID_INCREASELOANAMOUNT','Increase Loan Amount','Kony User','Kony Dev');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_OPSEXPOSURES' ,'Permission Type  exposure');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID494','PER_TYPE_OPSEXPOSURES','SID_ACTIVE','RequestOverviewFetchExposure','Permission to fetch exposure in request overview section','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID494', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID494', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID494', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SECRETARY', 'PID494', '0');

INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID498', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', NULL, 'EntityOverviewViewAccounts', 'Permission to view accounts in Entity Overview', '0', 'TRUE', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID495', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', NULL, 'EntityOverviewAddAccount', 'Permission to add accounts in Entity Overview', '0', 'TRUE', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID496', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', NULL, 'EntityOverviewUpdateAccount', 'Permission to edit accounts in Entity Overview', '0', 'TRUE', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID497', 'PER_TYPE_ACCOUNT', 'SID_ACTIVE', NULL, 'EntityOverviewDeleteAccount', 'Permission to delete accounts in Entity Overview', '0', 'TRUE', NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM_SUPERVISIOR', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RM', 'PID498', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID498', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM_SUPERVISIOR', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RM', 'PID495', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID495', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM_SUPERVISIOR', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RM', 'PID496', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID496', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAILONBOARDING_RM_SUPERVISIOR', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMEONBOARDING_RM_SUPERVISIOR', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RM', 'PID497', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID497', '0');


INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`) VALUES ('PID700', 'PER_TYPE_ROLE', 'SID_ACTIVE', 'ApproveInternalUserRole', 'Permission to approve a newly created or updated role', '0', 'TRUE');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERADMIN', 'PID700', '0');


INSERT INTO `permissionapprovals` (`expAPIOperationName`, `expAPINickName`, `approvalPermissionId`, `approvalPermissionName`, `isApprovalRequired`) VALUES ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'PID700', 'ApproveInternalUserRole', '1');
INSERT INTO `permissionapprovals` (`expAPIOperationName`, `expAPINickName`, `approvalPermissionId`, `approvalPermissionName`, `isApprovalRequired`) VALUES ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'PID700', 'ApproveInternalUserRole', '1');



INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`, `createdby`) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'role_approval', 'role', 'id', '1', '0', '1', '{\"query1\": {\"key\": \"id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}', 'Kony Dev');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`, `createdby`) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'proc', 'rolepermission_data_movement_approval_proc', '_requestId,_context', '2', '0', '1', '{}', 'Kony Dev');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`, `createdby`) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id', '3', '0', '1', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}', 'Kony Dev');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`, `createdby`) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '4', '1', '0', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"ne\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"userIds\",\"arrayconcatinator\": \"or\"}}', 'Kony Dev');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`, `createdby`) VALUES  ('RolesAndPermissionsObjService_role_createRole', 'Create Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '5', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"userIds\",\"arrayconcatinator\": \"or\"}}', 'Kony Dev');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'role_approval', 'role', 'id', '1', '0', '1', '{\"query1\": {\"key\": \"id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id,servicedefinitionId', '2', '1', '0', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"servicedefinitionId\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\": \"removedServiceDefinitionsArray\",\"arrayconcatinator\": \"or\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userroleservicedefinition_approval', 'userroleservicedefinition', 'UserRole_id,servicedefinitionId', '3', '0', '1', '{\"query1\": {\"key\": \"UserRole_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"servicedefinitionId\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\": \"removedServiceDefinitionsArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '4', '1', '0', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"ne\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"eq\",\"datatype\": \"array\",\"value\":\"roleAssignedToUsersArray\",\"arrayconcatinator\": \"or\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'userrole_approval', 'userrole', 'User_id,Role_id', '5', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"User_id\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\":\"roleRemovedFromUsersArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'proc', 'rolepermission_delete_approval_proc', '_roleId,_PermissionIds,_requestId,_context', '6', '1', '0', '{\"query1\": {\"key\": \"_roleId\",\"operator\": \"\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"\",\"query2\": {\"key\": \"_PermissionIds\",\"operator\": \"\",\"datatype\": \"string\",\"value\":\"permissionsRemoved\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'rolepermission_approval', 'rolepermission', 'Role_id,Permission_id', '7', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"},\"concateoperator1\": \"and\",\"query2\": {\"key\": \"Permission_id\",\"operator\": \"ne\",\"datatype\": \"array\",\"value\": \"permissionsRemovedArray\",\"arrayconcatinator\": \"and\"}}');
INSERT INTO `permissionapprovalconfig` (`expAPIOperationName`, `expAPINickName`, `approvalTableName`, `originalTableName`, `keyColumnNames`, `updateSequence`, `cleanup`, `update`, `sqlquery`) VALUES  ('RolesAndPermissionsObjService_role_manageRole', 'Manage Role', 'rolecompositeaction_approval', 'rolecompositeaction', 'Role_id,CompositeAction_id', '8', '0', '1', '{\"query1\": {\"key\": \"Role_id\",\"operator\": \"eq\",\"datatype\": \"string\",\"value\": \"id1\"}}');


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4b76967e-f383-11ec-b939-0242ac120002', 'MakerCheckerWorkflow', 'ApprovalRequest', 'invokeApprovalWorkflow', 'ApproveInternalUserRole');


UPDATE `configurations` SET `config_value` = '{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"Mortgages"}' WHERE (`configuration_id` = '172');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_CREATE_EDIT', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `status` (`id`, `Type_id`, `Code`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('SID_CUS_ERASURE_INPROGRESS','STID_CUSTOMERSTATUS',NULL,'Customer Erasure Inprogress','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `status` (`id`, `Type_id`, `Code`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('SID_CUS_ERASURE_COMPLETED','STID_CUSTOMERSTATUS',NULL,'Customer Erasure Completed','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

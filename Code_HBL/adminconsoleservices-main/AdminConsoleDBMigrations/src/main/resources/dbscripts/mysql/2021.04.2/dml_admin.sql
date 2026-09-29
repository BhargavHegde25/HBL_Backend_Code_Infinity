UPDATE `featureaction` SET `accesspolicyId` = 'VIEW', `actionlevelId` = 'CUSTOMERID_LEVEL' WHERE (`id` = 'PROSPECT_EXPIRY');

UPDATE `service_permission_mapper` SET `permissions` = 'SIGNATORY_GROUP_VIEW,API_ACCESS' WHERE (`id` = '9fa38ec1-958a-4eae-9080-8e002f637121');
UPDATE `service_permission_mapper` SET `permissions` = 'SIGNATORY_GROUP_VIEW,API_ACCESS' WHERE (`id` = '9fa38ec1-958a-4eae-9080-8e602f637122');
UPDATE `service_permission_mapper` SET `permissions` = 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS' WHERE (`id` = '9fa38ec1-959a-4eae-9080-8e022f637129');
UPDATE `service_permission_mapper` SET `permissions` = 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS' WHERE (`id` = '9fa38ec1-959a-4eae-9080-8r022f767123');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767124', 'SignatoryObject', 'SignatoryGroup', 'deleteSignatoryGroup', 'SIGNATORY_GROUP_DELETE,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767126', 'SignatoryObject', 'SignatoryGroup', 'fetchEligibleSignatoryUsers', 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767125', 'SignatoryObject', 'SignatoryGroup', 'isSignatoryGroupNameAvailable', 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767127', 'SignatoryObject', 'SignatoryGroup', 'fetchSignatoryGroupsbyCustomerIds', 'SIGNATORY_GROUP_VIEW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767128', 'SignatoryObject', 'SignatoryGroup', 'fetchAllSignatoryGroups', 'SIGNATORY_GROUP_VIEW,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767130', 'SignatoryObject', 'SignatoryGroup', 'isSignatoryGroupEligibleForDelete', 'SIGNATORY_GROUP_VIEW,SIGNATORY_GROUP_DELETE,API_ACCESS');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd11', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_VIEW', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd12', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_DELETE', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd13', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_CREATE_EDIT', '0');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_SIGNATORYGROUP', 'Permission Type SignatoryGroup');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_APPROVALMATRIX', 'Permission Type ApprovalMatrix');

INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID110', 'PER_TYPE_SIGNATORYGROUP', 'SID_ACTIVE', 'CreateSignatoryGroup', 'To Create a SignatoryGroup', '0', 'TRUE', 'Kony User', 'Kony Dev');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID111', 'PER_TYPE_SIGNATORYGROUP', 'SID_ACTIVE', 'UpdateSignatoryGroup', 'To Update a SignatoryGroup', '0', 'TRUE', 'Kony User', 'Kony Dev');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID112', 'PER_TYPE_SIGNATORYGROUP', 'SID_ACTIVE', 'ViewSignatoryGroup', 'To View a SignatoryGroup', '0', 'TRUE', 'Kony User', 'Kony Dev');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID113', 'PER_TYPE_APPROVALMATRIX', 'SID_ACTIVE', 'CreateApprovalMatrix', 'To Create  ApprovalMatrix', '0', 'TRUE', 'Kony User', 'Kony Dev');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID114', 'PER_TYPE_APPROVALMATRIX', 'SID_ACTIVE', 'UpdateApprovalMatrix', 'To Update a ApprovalMatrix', '0', 'TRUE', 'Kony User', 'Kony Dev');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`) VALUES ('PID115', 'PER_TYPE_APPROVALMATRIX', 'SID_ACTIVE', 'ViewApprovalMatrix', 'To View ApprovalMatrix', '0', 'TRUE', 'Kony User', 'Kony Dev');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID110', 'Kony User', 'Kony Dev');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID111', 'Kony User', 'Kony Dev');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID112', 'Kony User', 'Kony Dev');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID113', 'Kony User', 'Kony Dev');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID114', 'Kony User', 'Kony Dev');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'PID115', 'Kony User', 'Kony Dev');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('01b7baa1-8051-4b48-953f-defde1176245', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'createSignatoryGroup', 'CreateSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('3892b258-2a88-445a-ae27-273e7b9a4beb', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'updateSignatoryGroups', 'UpdateSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('b13f4167-efe5-4d1a-85a1-3a67ff3ec4d6', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'deleteSignatoryGroup', 'UpdateSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('494f14a4-d309-4add-a4fd-ac8078fe100e', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'isSignatoryGroupEligibleForDelete', 'ViewSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('6731303d-9a30-40b7-ae7f-f45cb6e66ba4', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getSignatoryGroupDetails', 'ViewSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('120d1663-3209-4870-8d9b-8f6771bdb7f7', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getAllSignatoryGroups', 'ViewSignatoryGroup');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('ddcfaec8-17e8-4c89-bd74-9144ab5b8edc', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getNoGroupUsers', 'ViewSignatoryGroup,ViewCustomer');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('a30e4e41-4b64-4601-b40f-e9e4587ed6f4', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getAllSignatoryGroupsbyCoreCustomerIds', 'ViewSignatoryGroup,ViewCustomer');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('4c0774db-dc04-4b3d-8c3a-451b5fbbfc1a', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getApprovalPermissionsForUser', 'ViewSignatoryGroup,ViewCustomer');


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('a452d995-2c39-48f5-a1f5-256d20bfbb8b', 'SignatoryGroupManageObjService', 'ApprovalMode', 'fetchApprovalMode', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('d3ff2f53-8e3a-47a5-997b-c0fa9ababf67', 'SignatoryGroupManageObjService', 'ApprovalMode', 'updateApprovalMode', 'UpdateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('1b76cf4f-9f1e-44c2-b450-163efd51e12d', 'SignatoryGroupManageObjService', 'ApprovalMode', 'deleteApprovalMode', 'UpdateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('de86dab1-91b1-4136-955c-e08911a78a95', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalMatrix', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('e3b3b92b-fa22-4fb4-a1ad-7b929ecc7a7a', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalRules', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('0fe58193-ec6e-483f-894a-508cdcc25a57', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'createApprovalRuleUserLevel', 'CreateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('baeb4b81-8e52-4d01-938f-47aff42466f4', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalRuleUserLevel', 'UpdateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('23c0f710-beb1-4225-8cad-1f8035d25130', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'createApprovalRuleSGLevel', 'CreateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('399ba39a-106c-4135-a765-4feb03f08aea', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalRuleSGLevel', 'UpdateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('f36f616d-0090-42e2-968d-b1a0cbeafc8c', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApproversInSignatoryGroup', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('448857cb-e397-42b3-ba37-df9cae1079c5', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalMatrixByContractId', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('773428a2-d970-4a67-820c-65bf3f1cbb9d', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getAccountActionCustomerApproverList', 'ViewApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('740ca605-3842-4279-8b45-981093939478', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalMatrixStatus', 'UpdateApprovalMatrix');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES 
('d8f8ebd2-5ad0-405a-b799-288be53fa8a8', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'isApprovalMatrixDisabled', 'ViewApprovalMatrix');

INSERT INTO `eventtype`  (`id`, `Name`, `ActivityType`) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and approval alerts', 'Customer');
INSERT INTO `eventconsumertypes`  (`EventType`, `ServiceId`, `OperationId`) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Alerts', 'pushAlerts');
INSERT INTO `dbxalerttype`  (`id`, `Name`, `AlertCategoryId`, `Status_id`, `IsGlobal`) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and approval alerts', 'ALERT_CAT_APPROVALS', 'SID_ACTIVE', '1');
INSERT INTO `dbxalerttypetext`  (`AlertTypeId`, `DisplayName`, `Description`, `LanguageCode`) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and Approval alerts', 'Alerts for request and approval notifications', 'en-US');
INSERT INTO `alerttypechannel`  (`alertTypeId`, `channelId`) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'CH_NOTIFICATION_CENTER');

INSERT INTO `eventsubtype`  (`id`, `EventTypeId`, `Name`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'REQUEST_AND_APPROVAL_ALERTS', 'Re-notify Single Approver for pending approval request');
INSERT INTO `alertsubtype`  (`id`, `AlertTypeId`, `Name`, `Description`, `recipienttype`, `Status_id`, `isAccountLevel`, `isGlobal`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'REQUEST_AND_APPROVAL_ALERTS', 'Renotify pending approval request', 'Alert is to send to approver as notifications for pending approval request', '1', 'SID_ACTIVE', '0', '1');
INSERT INTO `alertsubtypetext`  (`alertSubTypeId`, `displayName`, `description`, `languageCode`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'Renotify pending approval request', 'Alert is to send to approver as notifications for pending approval request', 'en-US');
INSERT INTO `alertsubtypeapp`  (`alertSubTypeId`, `appId`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'RETAIL_AND_BUSINESS_BANKING');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'TYPE_ID_BUSINESS');
INSERT INTO `alertsubtypecustomertype`  (`alertSubTypeId`, `customerTypeId`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'TYPE_ID_RETAIL');
INSERT INTO `alertsubtypechannel`  (`alertSubTypeId`, `channelId`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'CH_NOTIFICATION_CENTER');
INSERT INTO `communicationtemplate`  (`AlertSubTypeId`, `Name`, `Text`, `Subject`, `id`, `LanguageCode`, `ChannelID`, `Status_id`) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'Re-notification for pending approval request', 'You are re-notified to approve a pending [#]type[/#] request with transaction-Id [#]transactionId[/#]', 'Re-notification for pending approval request', '110001', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');

INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`, `createdts`) VALUES ('RID_TPP', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'TPP Access', 'This Role is only for TPP who want to view specific customer details. This Role has minimal permission.', 'Kony User', '2021-05-25 19:30:00');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`, `createdts`) VALUES ('RID_TPP', 'PID09', 'Kony User', 'Kony Dev', '2021-05-25 19:30:00');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '83c9b8d7-3715-480e-8c7d-3d6e61c00035');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '5801fa32-a416-45b6-af01-b22e2de93777');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', 'bef2fe82-9c21-4ccb-b599-3308de18de44');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '90356097-7fdf-4b8c-89bd-8a1065338a97');
INSERT INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', 'f85d8392-9afe-4128-b23e-a370f138784f');
INSERT INTO `systemuser` (`id`, `Status_id`, `Username`, `Password`, `Email`, `FirstName`, `LastName`, `FailedCount`, `createdby`, `modifiedby`) VALUES ('UID_TPP', 'SID_ACTIVE', 'admintpp', '$2a$11$IlgBrTIqqCdkWbSXW7/xvO9gtGqrnBOZrRq1WsDuaPyE9SFvTbdkW', 'c360admin@kony.com', 'Spotlight Administrator for TPP', 'TPP', '0', 'konydev', 'konydev');
INSERT INTO `userrole` (`User_id`, `Role_id`, `hasSuperAdminPrivilages`, `createdby`, `modifiedby`) VALUES ('UID_TPP', 'RID_TPP', '0', 'konydev', 'konydev');

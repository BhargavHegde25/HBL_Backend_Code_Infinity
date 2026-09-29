UPDATE [${dbxschemaname}].[featureaction] SET [accesspolicyId] = 'VIEW', [actionlevelId] = 'CUSTOMERID_LEVEL' WHERE (id = 'PROSPECT_EXPIRY');
GO

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'SIGNATORY_GROUP_VIEW,API_ACCESS' WHERE ([id] = '9fa38ec1-958a-4eae-9080-8e002f637121');
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'SIGNATORY_GROUP_VIEW,API_ACCESS' WHERE ([id] = '9fa38ec1-958a-4eae-9080-8e602f637122');
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS' WHERE ([id] = '9fa38ec1-959a-4eae-9080-8e022f637129');
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [permissions] = 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS' WHERE ([id] = '9fa38ec1-959a-4eae-9080-8r022f767123');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767124', 'SignatoryObject', 'SignatoryGroup', 'deleteSignatoryGroup', 'SIGNATORY_GROUP_DELETE,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767126', 'SignatoryObject', 'SignatoryGroup', 'fetchEligibleSignatoryUsers', 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767125', 'SignatoryObject', 'SignatoryGroup', 'isSignatoryGroupNameAvailable', 'SIGNATORY_GROUP_CREATE_EDIT,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767127', 'SignatoryObject', 'SignatoryGroup', 'fetchSignatoryGroupsbyCustomerIds', 'SIGNATORY_GROUP_VIEW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767128', 'SignatoryObject', 'SignatoryGroup', 'fetchAllSignatoryGroups', 'SIGNATORY_GROUP_VIEW,API_ACCESS');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767130', 'SignatoryObject', 'SignatoryGroup', 'isSignatoryGroupEligibleForDelete', 'SIGNATORY_GROUP_VIEW,SIGNATORY_GROUP_DELETE,API_ACCESS');
GO

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [softdeleteflag]) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd11', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_VIEW', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [softdeleteflag]) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd12', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_DELETE', '0');
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [softdeleteflag]) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1dd13', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_CREATE_EDIT', '0');
GO

INSERT INTO [${dbxschemaname}].[eventtype]  ([id], [Name], [ActivityType]) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and approval alerts', 'Customer');
INSERT INTO [${dbxschemaname}].[eventconsumertypes]  ([EventType], [ServiceId], [OperationId]) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Alerts', 'pushAlerts');
INSERT INTO [${dbxschemaname}].[dbxalerttype]  ([id], [Name], [AlertCategoryId], [Status_id], [IsGlobal]) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and approval alerts', 'ALERT_CAT_APPROVALS', 'SID_ACTIVE', '1');
INSERT INTO [${dbxschemaname}].[dbxalerttypetext]  ([AlertTypeId], [DisplayName], [Description], [LanguageCode]) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'Request and Approval alerts', 'Alerts for request and approval notifications', 'en-US');
INSERT INTO [${dbxschemaname}].[alerttypechannel]  ([alertTypeId], [channelId]) VALUES ('REQUEST_AND_APPROVAL_ALERTS', 'CH_NOTIFICATION_CENTER');

INSERT INTO [${dbxschemaname}].[eventsubtype]  ([id], [EventTypeId], [Name]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'REQUEST_AND_APPROVAL_ALERTS', 'Re-notify Single Approver for pending approval request');
INSERT INTO [${dbxschemaname}].[alertsubtype]  ([id], [AlertTypeId], [Name], [Description], [recipienttype], [Status_id], [isAccountLevel], [isGlobal]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'REQUEST_AND_APPROVAL_ALERTS', 'Renotify pending approval request', 'Alert is to send to approver as notifications for pending approval request', '1', 'SID_ACTIVE', '0', '1');
INSERT INTO [${dbxschemaname}].[alertsubtypetext]  ([alertSubTypeId], [displayName], [description], [languageCode]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'Renotify pending approval request', 'Alert is to send to approver as notifications for pending approval request', 'en-US');
INSERT INTO [${dbxschemaname}].[alertsubtypeapp]  ([alertSubTypeId], [appId]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'RETAIL_AND_BUSINESS_BANKING');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype]  ([alertSubTypeId], [customerTypeId]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'TYPE_ID_BUSINESS');
INSERT INTO [${dbxschemaname}].[alertsubtypecustomertype]  ([alertSubTypeId], [customerTypeId]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'TYPE_ID_RETAIL');
INSERT INTO [${dbxschemaname}].[alertsubtypechannel]  ([alertSubTypeId], [channelId]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'CH_NOTIFICATION_CENTER');
INSERT INTO [${dbxschemaname}].[communicationtemplate]  ([AlertSubTypeId], [Name], [Text], [Subject], [id], [LanguageCode], [ChannelID], [Status_id]) VALUES ('RENOTIFY_PENDING_APPROVAL_REQUEST', 'Re-notification for pending approval request', 'You are re-notified to approve a pending [#]type[/#] request with transaction-Id [#]transactionId[/#]', 'Re-notification for pending approval request', '110001', 'en-US', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper]([id], [service_name], [object_name], [operation], [permissions]) VALUES('672aabee-c1d9-11eb-8529-0242ac130003', 'ApprovalRequestObjects', 'MyRequests', 'RenotifyPendingApprovalRequest', 'ALLOW');
GO

INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description], [createdby], [modifiedby]) VALUES ('PER_TYPE_SIGNATORYGROUP', 'Permission Type SignatoryGroup', 'Kony User', 'Kony Dev');
INSERT INTO [${dbxschemaname}].[permissiontype] ([id], [Description], [createdby], [modifiedby]) VALUES ('PER_TYPE_APPROVALMATRIX', 'Permission Type ApprovalMatrix', 'Kony User', 'Kony Dev');
GO

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID110', N'PER_TYPE_SIGNATORYGROUP', N'SID_ACTIVE', NULL, N'CreateSignatoryGroup', N'To Create a SignatoryGroup', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID111', N'PER_TYPE_SIGNATORYGROUP', N'SID_ACTIVE', NULL, N'UpdateSignatoryGroup', N'To Update a SignatoryGroup', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID112', N'PER_TYPE_SIGNATORYGROUP', N'SID_ACTIVE', NULL, N'ViewSignatoryGroup', N'To View a SignatoryGroup', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID113', N'PER_TYPE_APPROVALMATRIX', N'SID_ACTIVE', NULL, N'CreateApprovalMatrix', N'To Create  ApprovalMatrix', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID114', N'PER_TYPE_APPROVALMATRIX', N'SID_ACTIVE', NULL, N'UpdateApprovalMatrix', N'To Update a ApprovalMatrix', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [DataType_id], [Name], [Description], [isComposite], [PermissionValue], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'PID115', N'PER_TYPE_APPROVALMATRIX', N'SID_ACTIVE', NULL, N'ViewApprovalMatrix', N'To View ApprovalMatrix', 0, N'TRUE', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID110', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID111', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID112', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID113', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID114', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_SUPERADMIN', N'PID115', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('01b7baa1-8051-4b48-953f-defde1176245', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'createSignatoryGroup', 'CreateSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('3892b258-2a88-445a-ae27-273e7b9a4beb', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'updateSignatoryGroups', 'UpdateSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('b13f4167-efe5-4d1a-85a1-3a67ff3ec4d6', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'deleteSignatoryGroup', 'UpdateSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('494f14a4-d309-4add-a4fd-ac8078fe100e', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'isSignatoryGroupEligibleForDelete', 'ViewSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('6731303d-9a30-40b7-ae7f-f45cb6e66ba4', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getSignatoryGroupDetails', 'ViewSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('120d1663-3209-4870-8d9b-8f6771bdb7f7', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getAllSignatoryGroups', 'ViewSignatoryGroup');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('ddcfaec8-17e8-4c89-bd74-9144ab5b8edc', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getNoGroupUsers', 'ViewSignatoryGroup,ViewCustomer');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('a30e4e41-4b64-4601-b40f-e9e4587ed6f4', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getAllSignatoryGroupsbyCoreCustomerIds', 'ViewSignatoryGroup,ViewCustomer');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('4c0774db-dc04-4b3d-8c3a-451b5fbbfc1a', 'SignatoryGroupManageObjService', 'SignatoryGroup', 'getApprovalPermissionsForUser', 'ViewSignatoryGroup,ViewCustomer');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('a452d995-2c39-48f5-a1f5-256d20bfbb8b', 'SignatoryGroupManageObjService', 'ApprovalMode', 'fetchApprovalMode', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('d3ff2f53-8e3a-47a5-997b-c0fa9ababf67', 'SignatoryGroupManageObjService', 'ApprovalMode', 'updateApprovalMode', 'UpdateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('1b76cf4f-9f1e-44c2-b450-163efd51e12d', 'SignatoryGroupManageObjService', 'ApprovalMode', 'deleteApprovalMode', 'UpdateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('de86dab1-91b1-4136-955c-e08911a78a95', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalMatrix', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('e3b3b92b-fa22-4fb4-a1ad-7b929ecc7a7a', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalRules', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('0fe58193-ec6e-483f-894a-508cdcc25a57', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'createApprovalRuleUserLevel', 'CreateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('baeb4b81-8e52-4d01-938f-47aff42466f4', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalRuleUserLevel', 'UpdateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('23c0f710-beb1-4225-8cad-1f8035d25130', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'createApprovalRuleSGLevel', 'CreateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('399ba39a-106c-4135-a765-4feb03f08aea', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalRuleSGLevel', 'UpdateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('f36f616d-0090-42e2-968d-b1a0cbeafc8c', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApproversInSignatoryGroup', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('448857cb-e397-42b3-ba37-df9cae1079c5', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getApprovalMatrixByContractId', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('773428a2-d970-4a67-820c-65bf3f1cbb9d', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'getAccountActionCustomerApproverList', 'ViewApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('740ca605-3842-4279-8b45-981093939478', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'updateApprovalMatrixStatus', 'UpdateApprovalMatrix');

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES 
('d8f8ebd2-5ad0-405a-b799-288be53fa8a8', 'ApprovalMatrixManageObjService', 'ApprovalMatrix', 'isApprovalMatrixDisabled', 'ViewApprovalMatrix');

GO

INSERT [${dbxschemaname}].[role] ([id], [Type_id], [Status_id], [Parent_id], [Name], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_TPP', N'ROLE_TYPE_1', N'SID_ACTIVE', N'RID_SUPERADMIN', N'TPP Access', N'This Role is only for TPP who want to view specific customer details. This Role has minimal permission', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'RID_TPP', N'PID09', N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a');
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', '83c9b8d7-3715-480e-8c7d-3d6e61c00035');
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', '5801fa32-a416-45b6-af01-b22e2de93777');
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', 'bef2fe82-9c21-4ccb-b599-3308de18de44');
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', '90356097-7fdf-4b8c-89bd-8a1065338a97');
INSERT INTO [${dbxschemaname}].[userroleservicedefinition] ([UserRole_id], [servicedefinitionId]) VALUES ('RID_TPP', 'f85d8392-9afe-4128-b23e-a370f138784f');
GO
INSERT [${dbxschemaname}].[systemuser] ([id], [Status_id], [Username], [Password], [Email], [Code], [FirstName], [MiddleName], [LastName], [FailedCount], [LastPasswordChangedts], [ResetpasswordLink], [ResetPasswordExpdts], [lastLogints], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'UID_TPP', N'SID_ACTIVE', N'admintpp', N'$2a$11$IlgBrTIqqCdkWbSXW7/xvO9gtGqrnBOZrRq1WsDuaPyE9SFvTbdkW', N'c360admin@kony.com', NULL, N'Spotlight Administrator for TPP', N'TPP', N'TPP', 0, CURRENT_TIMESTAMP, NULL, NULL, NULL, N'Kony User', N'Kony Dev', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
INSERT [${dbxschemaname}].[userrole] ([User_id], [Role_id], [hasSuperAdminPrivilages], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'UID_TPP', N'RID_TPP', 0, NULL, NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
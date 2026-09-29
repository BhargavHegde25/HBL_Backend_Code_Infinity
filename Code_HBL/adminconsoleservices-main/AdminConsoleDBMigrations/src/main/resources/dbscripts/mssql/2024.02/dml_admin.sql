INSERT INTO [${dbxschemaname}].[mcactiontext] ([id], [name], [language_code]) VALUES
('CREATE_CONTRACT', 'Create Contract', 'en-GB'),
('CREATE_CONTRACT', 'Create Contract', 'en-US'),
('CREATE_CONTRACT', 'Create Contract', 'es-ES'),
('CREATE_CONTRACT', 'Create Contract', 'fr-FR'),
('CREATE_CONTRACT', 'Create Contract', 'it-IT'),
('CREATE_CUSTOMER', 'Create Customer', 'en-GB'),
('CREATE_CUSTOMER', 'Create Customer', 'en-US'),
('CREATE_CUSTOMER', 'Create Customer', 'es-ES'),
('CREATE_CUSTOMER', 'Create Customer', 'fr-FR'),
('CREATE_CUSTOMER', 'Create Customer', 'it-IT'),
('EDIT_CONTRACT', 'Edit Contract', 'en-GB'),
('EDIT_CONTRACT', 'Edit Contract', 'en-US'),
('EDIT_CONTRACT', 'Edit Contract', 'es-ES'),
('EDIT_CONTRACT', 'Edit Contract', 'fr-FR'),
('EDIT_CONTRACT', 'Edit Contract', 'it-IT'),
('EDIT_CUSTOMER', 'Edit Customer', 'en-GB'),
('EDIT_CUSTOMER', 'Edit Customer', 'en-US'),
('EDIT_CUSTOMER', 'Edit Customer', 'es-ES'),
('EDIT_CUSTOMER', 'Edit Customer', 'fr-FR'),
('EDIT_CUSTOMER', 'Edit Customer', 'it-IT');

INSERT INTO [${dbxschemaname}].[mcmoduletext] ([id], [name], [language_code]) VALUES
('CONTRACT_MANAGEMENT', 'Contract Management', 'en-GB'),
('CONTRACT_MANAGEMENT', 'Contract Management', 'en-US'),
('CONTRACT_MANAGEMENT', 'Contract Management', 'es-ES'),
('CONTRACT_MANAGEMENT', 'Contract Management', 'fr-FR'),
('CONTRACT_MANAGEMENT', 'Contract Management', 'it-IT'),
('CUSTOMER_MANAGEMENT', 'Customer Management', 'en-GB'),
('CUSTOMER_MANAGEMENT', 'Customer Management', 'en-US'),
('CUSTOMER_MANAGEMENT', 'Customer Management', 'es-ES'),
('CUSTOMER_MANAGEMENT', 'Customer Management', 'fr-FR'),
('CUSTOMER_MANAGEMENT', 'Customer Management', 'it-IT');

UPDATE [${dbxschemaname}].[makercheckerconfig] SET [module] = 'CONTRACT_MANAGEMENT', [action] = 'CREATE_CONTRACT' WHERE ([id] = '1');
UPDATE [${dbxschemaname}].[makercheckerconfig] SET [module] = 'CUSTOMER_MANAGEMENT', [action] = 'CREATE_CUSTOMER' WHERE ([id] = '3');
UPDATE [${dbxschemaname}].[makercheckerconfig] SET [module] = 'CONTRACT_MANAGEMENT', [action] = 'EDIT_CONTRACT' WHERE ([id] = '2');
UPDATE [${dbxschemaname}].[makercheckerconfig] SET [module] = 'CUSTOMER_MANAGEMENT', [action] = 'EDIT_CUSTOMER' WHERE ([id] = '4');

INSERT INTO [${dbxschemaname}].[role] ([id], [Type_id], [Status_id], [Parent_id], [Name], [Description]) VALUES('RID_CLOS_USER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'CLOS USER', 'This role is used for CLOS user panel');
INSERT INTO [${dbxschemaname}].[role] ([id], [Type_id], [Status_id], [Parent_id], [Name], [Description]) VALUES('RID_CLOS_ADMIN', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'CLOS ADMIN', 'This role is used for CLOS admin panel');
 
INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES('PID800','PER_TYPE_CONFIGURATION','SID_ACTIVE','CLOSUserPanelConfiguration','Permission to show CLOS admin panel under customer management','0','TRUE');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_USER', 'PID800');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_ADMIN', 'PID800');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SUPERADMIN', 'PID800');

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES('PID801','PER_TYPE_CUSTOMER_ACTION','SID_ACTIVE','CLOSAdminPanelConfiguration','Permission to show CLOS admin panel under customer management','0','TRUE');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_USER', 'PID801');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_ADMIN', 'PID801');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SUPERADMIN', 'PID801');

INSERT INTO [${dbxschemaname}].[permission] ([id], [Type_id], [Status_id], [Name], [Description], [isComposite], [PermissionValue]) VALUES('PID806','PER_TYPE_CUSTOMER_ACTION','SID_ACTIVE','CLOSRole','Permission To Create Role In CLOS DB','0','TRUE');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_USER', 'PID806');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_CLOS_ADMIN', 'PID806');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES('RID_SUPERADMIN', 'PID806');

INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERADMIN', 'PID701');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERADMIN', 'PID702');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERADMIN', 'PID703');
INSERT INTO [${dbxschemaname}].[rolepermission] ([Role_id], [Permission_id]) VALUES ('RID_SUPERADMIN', 'PID704');

INSERT INTO [${dbxschemaname}].[scf_records_module_configurations] ([module_id], [allowed_fields]) VALUES ('AnchorFundingRequest', '["buyerId", "createdDate", "currency", "facilityId", "facilityAvailableLimit", "facilityCurrency", "facilityUtilisedLimit", "financingDate", "fundingDocuments", "fundingRequestAmount", "fundingRequestId", "invoiceReferences", "programName", "productId", "productName", "status", "supplierId", "updatedDate"]');
INSERT INTO [${dbxschemaname}].[scf_records_module_configurations] ([module_id], [allowed_fields]) VALUES ('AnchorInvoiceUpload', '["billReference", "billType", "buyerId", "buyerName", "createdDate", "invoiceAmount", "invoiceCurrency", "invoiceDocuments", "invoiceReference", "issueDate", "maturityDate", "role", "status", "supplierId", "supplierName", "updatedDate", "uploadedFrom"]');
INSERT INTO [${dbxschemaname}].[scf_records_module_configurations] ([module_id], [allowed_fields]) VALUES ('CounterPartyInvoiceUpload', '["billReference", "billType", "buyerId", "buyerName", "createdDate", "invoiceAmount", "invoiceCurrency", "invoiceDocuments", "invoiceReference", "issueDate", "maturityDate", "role", "status", "supplierId", "supplierName", "updatedDate", "uploadedFrom"]');
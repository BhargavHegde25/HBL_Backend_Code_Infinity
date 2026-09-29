USE [${dbxdbname}]
GO

UPDATE [${dbxschemaname}].[backendidentifier] SET [BackendId] = '1425958', [contractId] = '7321457251', [CompanyId] = 'GB0010001' WHERE ([id] = '002317d9-c469-4c91-b165-049286588332');
UPDATE [${dbxschemaname}].[backendidentifier] SET [BackendId] = '1425958', [BackendType] = 'MOCK', [contractId] = '7321457251', [CompanyId] = 'GB0010001' WHERE ([id] = '002317d9-c469-4c91-b165-0492865883321');

INSERT INTO [${dbxschemaname}].[contractcorecustomers] ([id], [contractId], [coreCustomerId], [coreCustomerName], [isPrimary], [isBusiness], [implicitAccountAccess], [companyLegalUnit]) VALUES ('990929051553867', '7321457251', '1425958', 'Mock User Primary', '1', '0', '0', 'ALL');
INSERT INTO [${dbxschemaname}].[contractcorecustomers] ([id], [contractId], [coreCustomerId], [coreCustomerName], [isPrimary], [isBusiness], [implicitAccountAccess], [companyLegalUnit]) VALUES ('299929951753867', '7321457251', '1578660', 'Mock User Non Primary', '0', '0', '0', 'ALL');
GO

INSERT INTO [${dbxschemaname}].[contractcorecustomers] ([id], [contractId], [coreCustomerId], [coreCustomerName], [isPrimary], [isBusiness], [implicitAccountAccess], [companyLegalUnit]) VALUES ('119929051513867', '4204010299', '1605506', 'Mock Business Primary', '1', '1', '0', 'ALL');
INSERT INTO [${dbxschemaname}].[contractcorecustomers] ([id], [contractId], [coreCustomerId], [coreCustomerName], [isPrimary], [isBusiness], [implicitAccountAccess], [companyLegalUnit]) VALUES ('109999051583867', '4204010299', '1065631', 'Mock Business Non Primary', '0', '1', '0', 'ALL');


INSERT INTO [${dbxschemaname}].[backendidentifier] ([id], [Customer_id], [sequenceNumber], [BackendId], [BackendType], [identifier_name], [contractId], [createdts], [lastmodifiedts], [isTypeBusiness], [CompanyId], [companyLegalUnit]) VALUES ('0823u7d9-cn69-7c91-g165-k49289688332', '11', '2', '1605506', 'CORE', 'customer_id', '4204010299', '2022-09-27 15:23:22', '2022-09-27 15:23:22', '1', 'GB0010001', 'ALL');
INSERT INTO [${dbxschemaname}].[backendidentifier] ([id], [Customer_id], [sequenceNumber], [BackendId], [BackendType], [identifier_name], [contractId], [createdts], [lastmodifiedts], [isTypeBusiness], [CompanyId], [companyLegalUnit]) VALUES ('j92317d9-c469-kcg1-b1u5-0i9o8608a720', '11', '1', '1605506', 'MOCK', 'customerId', '4204010299', '2022-09-27 15:23:22', '2022-09-27 15:23:22', '1', 'GB0010001', 'ALL');

INSERT INTO [${dbxschemaname}].[groupservicedefinition] ([Group_id], [serviceDefinitionId], [isDefaultGroup], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [companyLegalUnit]) VALUES ('DEFAULT_GROUP', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', '0', 'UID10', '2022-09-27 15:25:16', '2022-09-27 15:25:16', '2022-09-27 15:25:16', '0', 'ALL');
GO


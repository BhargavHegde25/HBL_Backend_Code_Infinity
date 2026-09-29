USE [${dbxdbname}]
GO

INSERT [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES (N'052b7u55-6432-42ae-8199-933d59oi09b3', N'CustomerManagementObjService', N'Customer', N'CSRAssistAuthorizationViewSpecAccount', N'CSRAssist');
GO
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','CARD_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CARD-CREATE','Add Card To Apple Wallet','Add Card To Apple Wallet','0','1','0','13', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','CREATE','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','en-GB', 'Add Card To Apple Wallet', 'Add Card To Apple Wallet');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','en-US', 'Add Card To Apple Wallet', 'Add Card To Apple Wallet');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','fr-FR', 'Add Card To Apple Wallet', 'Add Card To Apple Wallet');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','de-DE', 'Add Card To Apple Wallet', 'Add Card To Apple Wallet');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET','es-ES', 'Add Card To Apple Wallet', 'Add Card To Apple Wallet');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('2cceedbc-9006-4945-9cae-c6234aa22d8a', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('b1452b90-2c1d-4778-a4a8-f81b9ded2092', 'f85d8392-9afe-4128-b23e-a370f138784f', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('dfc78dad-5b15-4176-a9bb-7eadadc81b0b', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('4106f00c-b170-4a19-9a55-89842a0325b7', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('83327316-5f96-426d-9d56-e5b05ecbc2ff', '5801fa32-a416-45b6-af01-b22e2de93777', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('d69d078b-957d-41c0-861f-0ac75479ffdd', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('5a90570e-8aba-44e0-b0e6-ca36063e9dbc', 'GROUP_ADMINISTRATOR', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('177d6f6b-ad91-4e27-b8d1-8e9ccd62c302', '4204010299', '1065631', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('4f16d855-4e6a-4d98-b868-4b20dee8b835', '4204010299', '1605506', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('9a9c9cbd-a638-48e3-9999-12da71caeb99', '7321457251', '1425958', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('957f4fae-4d07-40e1-a23d-3c55e05cbd70', '7321457251', '1578660', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET');
GO
/*------Google Pay---------*/
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','CARD_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CARD-CREATE','Add Card To Google Pay','Add Card To Google Pay','0','1','0','14', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','CREATE','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','en-GB', 'Add Card To Google Pay', 'Add Card To Google Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','en-US', 'Add Card To Google Pay', 'Add Card To Google Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','fr-FR', 'Add Card To Google Pay', 'Add Card To Google Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','de-DE', 'Add Card To Google Pay', 'Add Card To Google Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY','es-ES', 'Add Card To Google Pay', 'Add Card To Google Pay');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('66419971-ca62-46ae-bcc3-f94e0a304b5b', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('b994b926-96ce-4db5-a032-8cfa202c5bfe', 'f85d8392-9afe-4128-b23e-a370f138784f', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('2cf79096-adf9-4bdb-88a5-81c285f66ec8', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('815eb6f8-5607-496b-83c9-7c5ee9a83008', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('0efb857f-0377-468b-888a-09a6532ad3cd', '5801fa32-a416-45b6-af01-b22e2de93777', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('bfc76b32-8af9-4802-aa7c-5ac41d83f358', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('78bcf4d6-2729-4c4d-a021-d4946dfe9f86', 'GROUP_ADMINISTRATOR', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('4a279fa9-7282-4d9e-9d4c-57f2f1f9889c', '4204010299', '1065631', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('75b0d2cb-fae9-48cd-98e0-c21bd08d0d19', '4204010299', '1605506', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('75819f36-7fc5-4faa-8c8c-5941c5c5da42', '7321457251', '1425958', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('790f00f5-2a2c-45f5-9093-0c8ffd6548b3', '7321457251', '1578660', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY');
GO
/*-----------Samsung Pay---------*/
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','CARD_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CARD-CREATE','Add Card To Samsung Pay','Add Card To Samsung Pay','0','1','0','15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','CREATE','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','en-GB', 'Add Card To Samsung Pay', 'Add Card To Samsung Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','en-US', 'Add Card To Samsung Pay', 'Add Card To Samsung Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','fr-FR', 'Add Card To Samsung Pay', 'Add Card To Samsung Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','de-DE', 'Add Card To Samsung Pay', 'Add Card To Samsung Pay');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY','es-ES', 'Add Card To Samsung Pay', 'Add Card To Samsung Pay');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('c8c39ea6-bfa9-4706-a247-d2cf35170661', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('12fd7401-948f-42a7-899c-3d2086ab9966', 'f85d8392-9afe-4128-b23e-a370f138784f', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('546810e3-c7bf-4a83-9504-a661b0ea9eca', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('cc1df3e1-844c-46f7-a20d-2a5b69806e05', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('5548e2df-2102-429d-8c7b-561ce7ac8067', '5801fa32-a416-45b6-af01-b22e2de93777', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('ada68531-cd1a-404f-83c8-27df52a95cce', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('d6db40d5-fcc9-43c6-a472-2c33345fbbe8', 'GROUP_ADMINISTRATOR', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('ef76faaa-b62a-47f4-99bc-f781f5a2a436', '4204010299', '1065631', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('cf76ad16-e1fd-4aac-9092-4627c088569b', '4204010299', '1605506', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('a3c2d2ca-9b0b-4ecc-b2bd-3eb25f9a3685', '7321457251', '1425958', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[contractactionlimit] ([id], [contractId], [coreCustomerId], [featureId], [actionId]) VALUES ('7f7773d7-969b-4c54-b754-c9d13bfe29d5', '7321457251', '1578660', 'CARD_MANAGEMENT', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac888998', 'WealthObjects', 'Portfolio', 'getTransactionDetails', 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac888997', 'WealthObjects', 'OrdersDetails', 'getOrdersDetails', 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW,WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac888995', 'WealthObjects', 'AccountActivity', 'getAccountActivityOperations', 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac888994', 'WealthObjects', 'Reports', 'getReportAndDownloadTypes', 'WEALTH_REPORT_MANAGEMENT_REPORT_CREATE,WEALTH_REPORT_MANAGEMENT_REPORT_DOWNLOAD');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac888996', 'WealthObjects', 'DownloadPDF', 'generatePDF', 'ALLOW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m1', 'WealthObjects', 'CurrencyDetails', 'getAddCurrency', 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m2', 'WealthObjects', 'CurrencyDetails', 'GetMarketRates', 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m3', 'WealthObjects', 'CurrencyDetails', 'getHistoricalData', 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m4', 'WealthObjects', 'Order', 'createOrder', 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m5', 'WealthObjects', 'StockNews', 'getstockNewsDetails', 'WEALTH_NEWS_AND_DOCUMENTS_STOCK_NEWS_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m6', 'WealthObjects', 'ProductDetails', 'getProductDetails', 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW,WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW,WEALTH_NEWS_AND_DOCUMENTS_STOCK_NEWS_VIEW,WEALTH_NEWS_AND_DOCUMENTS_DOCUMENTS_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94m7', 'WealthObjects', 'ProductDetails', 'getProductDetailsFromId', 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW,WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW,WEALTH_NEWS_AND_DOCUMENTS_STOCK_NEWS_VIEW,WEALTH_NEWS_AND_DOCUMENTS_DOCUMENTS_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh94b1', 'WealthObjects', 'Portfolio', 'getPortfolioHoldings', 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh95b2', 'WealthObjects', 'InstrumentDetails', 'getInstrumentTotal', 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh96b3', 'WealthObjects', 'Portfolio', 'getSearchInstrumentList', 'WEALTH_PRODUCT_DETAILS_INSTRUMENT_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh97b4', 'WealthObjects', 'OrdersDetails', 'cancelOrder', 'WEALTH_ORDER_MGMT_ORDER_CANCEL');
GO
INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id]) VALUES ('SIGNATORY_GROUP', 'RETAIL_AND_BUSINESS_BANKING', 'Signatory Groups', 'Manage Signatory Groups for an organization', 'NON_MONETARY', 'SID_FEATURE_ACTIVE');
GO
INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP');
GO
INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP');
GO
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('SIGNATORY_GROUP-VIEW');
GO
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('SIGNATORY_GROUP-DELETE');
GO
INSERT INTO [${dbxschemaname}].[rrole] ([id]) VALUES ('SIGNATORY_GROUP-CREATE_EDIT');
GO
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [status], [accesspolicyId], [actionlevelId]) VALUES ('SIGNATORY_GROUP_VIEW','SIGNATORY_GROUP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SIGNATORY_GROUP-VIEW','View Signatory Groups','View Signatory Groups','0','0','0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [dependency], [status], [accesspolicyId], [actionlevelId]) VALUES ('SIGNATORY_GROUP_DELETE','SIGNATORY_GROUP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SIGNATORY_GROUP-DELETE','Delete Signatory Groups','Delete Signatory Groups','0','0','0','SIGNATORY_GROUP_VIEW','SID_ACTION_ACTIVE','DELETE','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [dependency], [status], [accesspolicyId], [actionlevelId]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT','SIGNATORY_GROUP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SIGNATORY_GROUP-CREATE_EDIT','Create/Edit Signatory Groups','Create/Edit Signatory Groups','0','0','0','SIGNATORY_GROUP_VIEW','SID_ACTION_ACTIVE','CREATE','CUSTOMERID_LEVEL');
GO
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP', 'de-DE', 'Signatory Groups', 'Manage Signatory Groups for an organization');
GO
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP', 'en-GB', 'Signatory Groups', 'Manage Signatory Groups for an organization');
GO
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP', 'en-US', 'Signatory Groups', 'Manage Signatory Groups for an organization');
GO
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP', 'es-ES', 'Signatory Groups', 'Manage Signatory Groups for an organization');
GO
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP', 'fr-FR', 'Signatory Groups', 'Manage Signatory Groups for an organization');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_VIEW', 'de-DE', 'View Signatory Groups', 'View Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_VIEW', 'en-GB', 'View Signatory Groups', 'View Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_VIEW', 'en-US', 'View Signatory Groups', 'View Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_VIEW', 'es-ES', 'View Signatory Groups', 'View Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_VIEW', 'fr-FR', 'View Signatory Groups', 'View Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_DELETE', 'de-DE', 'Delete Signatory Groups', 'Delete Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_DELETE', 'en-GB', 'Delete Signatory Groups', 'Delete Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_DELETE', 'en-US', 'Delete Signatory Groups', 'Delete Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_DELETE', 'es-ES', 'Delete Signatory Groups', 'Delete Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_DELETE', 'fr-FR', 'Delete Signatory Groups', 'Delete Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'de-DE', 'Create/Edit Signatory Groups', 'Create/Edit Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'en-GB', 'Create/Edit Signatory Groups', 'Create/Edit Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'en-US', 'Create/Edit Signatory Groups', 'Create/Edit Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'es-ES', 'Create/Edit Signatory Groups', 'Create/Edit Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'fr-FR', 'Create/Edit Signatory Groups', 'Create/Edit Signatory Groups');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_VIEW');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_DELETE');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_CREATE_EDIT');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_VIEW');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_DELETE');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_CREATE_EDIT');
GO
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID350', 'PID45', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', '0');
GO
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID351', 'PID45', 'SIGNATORY_GROUP_DELETE', 'SIGNATORY_GROUP', '0');
GO
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID352', 'PID45', 'SIGNATORY_GROUP_CREATE_EDIT', 'SIGNATORY_GROUP', '0');
GO
INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName], [createdts], [lastmodifiedts]) VALUES ('SIGNATORY_GROUP_CREATE_EDIT', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'View Signatory Group', 'Signatory groups', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO [${dbxschemaname}].[dependentactions] ([actionId], [dependentactionId], [featureId], [actionName], [featureName], [createdts], [lastmodifiedts]) VALUES ('SIGNATORY_GROUP_DELETE', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'View Signatory Group', 'Signatory groups', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('5559a2d0-9eca-4b8b-b286-569f7b3a5982', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_VIEW');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('999125gg-e8c5-446d-a8c8-0fa783e1ddff', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_VIEW');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('5519a2d0-9eca-4b8b-b286-569f7b3a5982', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_DELETE');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('999gg5g1-e8c5-446d-a8c8-0fa783e1ddff', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_DELETE');
GO
INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('5559b2d0-9eca-4b8b-b286-569f7b3a5982', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_CREATE_EDIT');
GO
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('9991g51g-e8c5-446d-a8c8-0fa783e1ddff', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_CREATE_EDIT');
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Template' WHERE [id] = 'ACH_COLLECTION_CREATE_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Template' WHERE [Action_id] = 'ACH_COLLECTION_CREATE_TEMPLATE' AND [Locale_id] = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Template' WHERE [id] = 'ACH_COLLECTION_DELETE_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Template' WHERE [Action_id] = 'ACH_COLLECTION_DELETE_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Template' WHERE [id] = 'ACH_COLLECTION_VIEW_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Template' WHERE [Action_id] = 'ACH_COLLECTION_VIEW_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve ACH Collection' WHERE [id] = 'ACH_COLLECTION_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve ACH Collection' WHERE [Action_id] = 'ACH_COLLECTION_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated ACH Collection' WHERE [id] = 'ACH_COLLECTION_SELF_APPROVAL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Self-initiated ACH Collection' WHERE [Action_id] = 'ACH_COLLECTION_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate ACH Collection' WHERE [id] = 'ACH_COLLECTION_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Initiate ACH Collection' WHERE [Action_id] = 'ACH_COLLECTION_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Edit Template' WHERE [id] = 'ACH_COLLECTION_EDIT_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Edit Template' WHERE [Action_id] = 'ACH_COLLECTION_EDIT_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE [id] = 'ACH_COLLECTION_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View History' WHERE [Action_id] = 'ACH_COLLECTION_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve File Uploads' WHERE [id] = 'ACH_FILE_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve File Uploads'  WHERE [Action_id] = 'ACH_FILE_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated File Uploads' WHERE [id] = 'ACH_FILE_SELF_APPROVAL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Self-initiated File Uploads' WHERE [Action_id] = 'ACH_FILE_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Upload Files' WHERE [id] = 'ACH_FILE_UPLOAD';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Upload Files' WHERE [Action_id] = 'ACH_FILE_UPLOAD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE [id] = 'ACH_FILE_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View History' WHERE [Action_id] = 'ACH_FILE_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Template' WHERE [id] = 'ACH_PAYMENT_CREATE_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Template'  WHERE [Action_id] = 'ACH_PAYMENT_CREATE_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Template' WHERE [id] = 'ACH_PAYMENT_DELETE_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Template' WHERE [Action_id] = 'ACH_PAYMENT_DELETE_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Template' WHERE [id] = 'ACH_PAYMENT_VIEW_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Template' WHERE [Action_id] = 'ACH_PAYMENT_VIEW_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve ACH Payment' WHERE [id] = 'ACH_PAYMENT_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve ACH Payment' WHERE [Action_id] = 'ACH_PAYMENT_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated ACH Payment' WHERE [id] = 'ACH_PAYMENT_SELF_APPROVAL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Self-initiated ACH Payment' WHERE [Action_id] = 'ACH_PAYMENT_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate ACH Payment' WHERE [id] = 'ACH_PAYMENT_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Initiate ACH Payment' WHERE [Action_id] = 'ACH_PAYMENT_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Edit Template' WHERE [id] = 'ACH_PAYMENT_EDIT_TEMPLATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Edit Template' WHERE [Action_id] = 'ACH_PAYMENT_EDIT_TEMPLATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE [id] = 'ACH_PAYMENT_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View History' WHERE [Action_id] = 'ACH_PAYMENT_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Manage Alerts' WHERE [id] = 'ALERT_MANAGEMENT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Manage Alerts'  WHERE [Action_id] = 'ALERT_MANAGEMENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Manage Approval Matrix' WHERE [id] = 'APPROVAL_MATRIX_MANAGE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Manage Approval Matrix'  WHERE [Action_id] = 'APPROVAL_MATRIX_MANAGE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Approval Matrix' WHERE [id] = 'APPROVAL_MATRIX_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Approval Matrix'  WHERE [Action_id] = 'APPROVAL_MATRIX_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Activate or Deactivate Bill Pay' WHERE [id] = 'BILL_PAY_ACTIVATE_OR_DEACTIVATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Activate or Deactivate Bill Pay'  WHERE [Action_id] = 'BILL_PAY_ACTIVATE_OR_DEACTIVATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Payments' WHERE [id] = 'BILL_PAY_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Payments' WHERE [Action_id] = 'BILL_PAY_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Payments' WHERE [id] = 'BILL_PAY_SELF_APPROVAL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Self-initiated Payments' WHERE [Action_id] = 'BILL_PAY_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Activate or Deactivate Ebills' WHERE [id] = 'BILL_PAY_ACTIVATE_OR_DEACTIVATE_EBILL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Activate or Deactivate Ebills' WHERE [Action_id] = 'BILL_PAY_ACTIVATE_OR_DEACTIVATE_EBILL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Payees' WHERE [id] = 'BILL_PAY_CREATE_PAYEES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Payees' WHERE [Action_id] = 'BILL_PAY_CREATE_PAYEES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Payees' WHERE [id] = 'BILL_PAY_DELETE_PAYEES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Payees' WHERE [Action_id] = 'BILL_PAY_DELETE_PAYEES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate Bill Payments' WHERE [id] = 'BILL_PAY_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Initiate Bill Payments' WHERE [Action_id] = 'BILL_PAY_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Payees' WHERE [id] = 'BILL_PAY_VIEW_PAYEES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Payees' WHERE [Action_id] = 'BILL_PAY_VIEW_PAYEES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Payments' WHERE [id] = 'BILL_PAY_VIEW_PAYMENTS';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Payments' WHERE [Action_id] = 'BILL_PAY_VIEW_PAYMENTS' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate Bulk Bill Payments' WHERE [id] = 'BILL_PAY_BULK';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Initiate Bulk Bill Payments' WHERE [Action_id] = 'BILL_PAY_BULK' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Add New Payment Order' WHERE [id] = 'BULK_PAYMENT_REQUEST_ADD_PO';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Add New Payment Order'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_ADD_PO' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Request' WHERE [id] = 'BULK_PAYMENT_REQUEST_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Request'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Transfer' WHERE [id] = 'BULK_PAYMENT_REQUEST_CANCEL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Cancel Transfer'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_CANCEL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Edit Payment Order' WHERE [id] = 'BULK_PAYMENT_REQUEST_EDIT_PO';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Edit Payment Order'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_EDIT_PO' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Edit Request' WHERE [id] = 'BULK_PAYMENT_REQUEST_EDIT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Edit Request'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_EDIT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Remove Payment Order' WHERE [id] = 'BULK_PAYMENT_REQUEST_REMOVE_PO';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Remove Payment Order'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_REMOVE_PO' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Submit Request' WHERE [id] = 'BULK_PAYMENT_REQUEST_SUBMIT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Submit Request'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_SUBMIT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Requests' WHERE [id] = 'BULK_PAYMENT_REQUEST_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Requests'  WHERE [Action_id] = 'BULK_PAYMENT_REQUEST_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Add Stop Cheque Request' WHERE [id] = 'CHECK_MANAGEMENT_ADD_STOP_CHECK_REQUEST';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Add Stop Cheque Request'  WHERE [Action_id] = 'CHECK_MANAGEMENT_ADD_STOP_CHECK_REQUEST' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Disputed Checks' WHERE [id] = 'CHECK_MANAGEMENT_VIEW_DISPUTED_CHECKS';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Disputed Checks'  WHERE [Action_id] = 'CHECK_MANAGEMENT_VIEW_DISPUTED_CHECKS' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Request' WHERE [id] = 'CHEQUE_BOOK_REQUEST_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Request'  WHERE [Action_id] = 'CHEQUE_BOOK_REQUEST_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Requests' WHERE [id] = 'CHEQUE_BOOK_REQUEST_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Requests'  WHERE [Action_id] = 'CHEQUE_BOOK_REQUEST_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Apply Role to Users' WHERE [id] = 'CUSTOM_ROLES_APPLY';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Apply Role to Users'  WHERE [Action_id] = 'CUSTOM_ROLES_APPLY' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Roles' WHERE [id] = 'CUSTOM_ROLES_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Roles'  WHERE [Action_id] = 'CUSTOM_ROLES_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Roles' WHERE [id] = 'CUSTOM_ROLES_DELETE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Roles'  WHERE [Action_id] = 'CUSTOM_ROLES_DELETE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Roles' WHERE [id] = 'CUSTOM_ROLES_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Roles'  WHERE [Action_id] = 'CUSTOM_ROLES_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Reply to Messages' WHERE [id] = 'MESSAGES_CREATE_OR_REPLY';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create/Reply to Messages'  WHERE [Action_id] = 'MESSAGES_CREATE_OR_REPLY' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Message' WHERE [id] = 'MESSAGES_DELETE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Message'  WHERE [Action_id] = 'MESSAGES_DELETE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Manage Disputed Transactions' WHERE [id] = 'DISPUTE_TRANSACTIONS_MANAGE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Manage Disputed Transactions'  WHERE [Action_id] = 'DISPUTE_TRANSACTIONS_MANAGE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Dispute a Transaction' WHERE [id] = 'DISPUTE_TRANSACTIONS';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Dispute a Transaction'  WHERE [Action_id] = 'DISPUTE_TRANSACTIONS' AND [Locale_id]  = 'en-US'; 
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Disputed Transactions' WHERE [id] = 'DISPUTE_TRANSACTIONS_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Disputed Transactions'  WHERE [Action_id] = 'DISPUTE_TRANSACTIONS_VIEW' AND [Locale_id]  = 'en-US';  
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_APPROVE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Transfer'  WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_SELF_APPROVAL';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Self-initiated Transfer' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate Transfer' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_CREATE';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Initiate Transfer' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Bulk Templates' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create/Edit Bulk Templates' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_CREATE_RECEPIENT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Create Recipient' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Bulk Templates' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_DELETE_BULK_TEMPLATES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Bulk Templates' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_DELETE_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_DELETE_RECEPIENT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Delete Recipient' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Upload Bulk Files' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_UPLOAD_BULK_FILES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Upload Bulk Files' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_UPLOAD_BULK_FILES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Bulk Files' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Bulk Files' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_BULK_FILES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Bulk Templates' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Bulk Templates' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_RECEPIENT';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View Recipients' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE [id] = 'DOMESTIC_WIRE_TRANSFER_VIEW';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'View History' WHERE [Action_id] = 'DOMESTIC_WIRE_TRANSFER_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Authenticate Funding' WHERE [id] = 'FUNDING_AUTHENTICATION';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Authenticate Funding' WHERE [Action_id] = 'FUNDING_AUTHENTICATION' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName] = 'Approve Transfer' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cancel Transfer' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Transfer' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Cancellations' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Cancellations' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create/Edit Transfer' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Transfer' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View History' WHERE [Action_id]= 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE (id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Transfer' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cancel Transfer' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Transfer' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Cancellations' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Cancellations' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create/Edit Transfer' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Transfer' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View History' WHERE [Action_id]= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE (id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Transfer' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create/Edit Bulk Templates' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Bulk Templates' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_UPDATE_BULK_TEMPLATES');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Bulk Templates' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_DELETE_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Bulk Templates' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_DELETE_BULK_TEMPLATES');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Initiate Transfer' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate Transfer' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Upload Bulk Files' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_UPLOAD_BULK_FILES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'upload bulk files' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_UPLOAD_BULK_FILES');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Bulk Files' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Bulk Files' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_FILES');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Bulk Templates' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Bulk Templates' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_VIEW_BULK_TEMPLATES');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipient' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View History' WHERE [Action_id]= 'INTERNATIONAL_WIRE_TRANSFER_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE (id = 'INTERNATIONAL_WIRE_TRANSFER_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cancel Transfer' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_CANCEL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Transfer' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_CANCEL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Cancellations' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Cancellations' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Transfer' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create/Edit Transfer' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Transfer' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Transactions' WHERE [Action_id]= 'INTRA_BANK_FUND_TRANSFER_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Transactions' WHERE (id = 'INTRA_BANK_FUND_TRANSFER_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Loan Schedule' WHERE [Action_id]= 'VIEW_LOAN_SCHEDULE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Loan Schedule' WHERE (id = 'VIEW_LOAN_SCHEDULE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Installment Summary' WHERE [Action_id]= 'VIEW_INSTALLMENT_SUMMARY' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Installment Summary' WHERE (id = 'VIEW_INSTALLMENT_SUMMARY');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Activate Card' WHERE [Action_id]= 'CARD_MANAGEMENT_ACTIVATE_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Activate Card' WHERE (id = 'CARD_MANAGEMENT_ACTIVATE_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Apply For Debit Card' WHERE [Action_id]= 'CARD_MANAGEMENT_APPLY_FOR_DEBIT_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Apply For Debit Card' WHERE (id = 'CARD_MANAGEMENT_APPLY_FOR_DEBIT_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cancel Card' WHERE [Action_id]= 'CARD_MANAGEMENT_CANCEL_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Card' WHERE (id = 'CARD_MANAGEMENT_CANCEL_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Change PIN' WHERE [Action_id]= 'CARD_MANAGEMENT_CHANGE_PIN' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Change PIN' WHERE (id = 'CARD_MANAGEMENT_CHANGE_PIN');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Card Request' WHERE [Action_id]= 'CARD_MANAGEMENT_CREATE_CARD_REQUEST' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Card Request' WHERE (id = 'CARD_MANAGEMENT_CREATE_CARD_REQUEST');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Lock Card' WHERE [Action_id]= 'CARD_MANAGEMENT_LOCK_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Lock Card' WHERE (id = 'CARD_MANAGEMENT_LOCK_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Partial Update' WHERE [Action_id]= 'CARD_MANAGEMENT_PARTIAL_UPDATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Partial Update' WHERE (id = 'CARD_MANAGEMENT_PARTIAL_UPDATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Replace Card' WHERE [Action_id]= 'CARD_MANAGEMENT_REPLACE_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Replace Card' WHERE (id = 'CARD_MANAGEMENT_REPLACE_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Report Card Lost or Stolen' WHERE [Action_id]= 'CARD_MANAGEMENT_REPORT_CARD_STOLEN' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Report Card Lost or Stolen' WHERE (id = 'CARD_MANAGEMENT_REPORT_CARD_STOLEN');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Unlock Card' WHERE [Action_id]= 'CARD_MANAGEMENT_UNLOCK_CARD' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Unlock Card' WHERE (id = 'CARD_MANAGEMENT_UNLOCK_CARD');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Update Purchase Limit' WHERE [Action_id]= 'CARD_MANAGEMENT_UPDATE_PURCHASE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Update Purchase Limit' WHERE (id = 'CARD_MANAGEMENT_UPDATE_PURCHASE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Update Withdrawal Limit' WHERE [Action_id]= 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Update Withdrawal Limit' WHERE (id = 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Transfer' WHERE [Action_id]= 'P2P_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'P2P_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'P2P_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'P2P_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Initiate Transfer' WHERE [Action_id]= 'P2P_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Initiate Transfer' WHERE (id = 'P2P_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Activate P2P Service' WHERE [Action_id]= 'P2P_ACTIVATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Activate P2P Service' WHERE (id = 'P2P_ACTIVATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'P2P_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'P2P_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Deactivate P2P Service' WHERE [Action_id]= 'P2P_DEACTIVATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Deactivate P2P Service' WHERE (id = 'P2P_DEACTIVATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'P2P_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'P2P_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'P2P_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE (id = 'P2P_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Manage P2P Settings' WHERE [Action_id]= 'P2P_MANAGE_SETTINGS' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Manage P2P Settings' WHERE (id = 'P2P_MANAGE_SETTINGS');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View History' WHERE [Action_id]= 'P2P_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View History' WHERE (id = 'P2P_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Checking Prospect Credentials Expiry' WHERE [Action_id]= 'PROSPECT_EXPIRY' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Checking Prospect Credentials Expiry' WHERE (id = 'PROSPECT_EXPIRY');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Capture Remote Deposits' WHERE [Action_id]= 'RDC' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Capture Remote Deposits' WHERE (id = 'RDC');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Authenticate Resume Application' WHERE [Action_id]= 'RESUME_AUTHENTICATION' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Authenticate Resume Application' WHERE (id = 'RESUME_AUTHENTICATION');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Transfer' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Transfer' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cancel Transfer' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cancel Transfer' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Cancellations' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Cancellations' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create/Edit Transfer' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create/Edit Transfer' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Approve Self-initiated Transfer' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Approve Self-initiated Transfer' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Transactions' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Transactions' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create Recipient' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create Recipient' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Delete Recipient' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_DELETE_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Delete Recipient' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_DELETE_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Recipients' WHERE [Action_id]= 'TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW_RECEPIENT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Recipients' WHERE (id = 'TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW_RECEPIENT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Activate/Suspend User' WHERE [Action_id]= 'USER_MANAGEMENT_ACTIVATE_OR_SUSPEND' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Activate/Suspend User' WHERE (id = 'USER_MANAGEMENT_ACTIVATE_OR_SUSPEND');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Create User' WHERE [Action_id]= 'USER_MANAGEMENT_CREATE' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Create User' WHERE (id = 'USER_MANAGEMENT_CREATE');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Edit User' WHERE [Action_id]= 'USER_MANAGEMENT_EDIT' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Edit User' WHERE (id = 'USER_MANAGEMENT_EDIT');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View User' WHERE [Action_id]= 'USER_MANAGEMENT_VIEW' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View User' WHERE (id = 'USER_MANAGEMENT_VIEW');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Validate User' WHERE [Action_id]= 'USER_VERIFICATION' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Validate User' WHERE (id = 'USER_VERIFICATION');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'Cardless Cash Withdrawal' WHERE [Action_id]= 'WITHDRAW_CASH_CARDLESS_CASH' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'Cardless Cash Withdrawal' WHERE (id = 'WITHDRAW_CASH_CARDLESS_CASH');
GO
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [displayName]= 'View Withdrawal Summary' WHERE [Action_id]= 'WITHDRAW_CASH_VIEW_SUMMARY' AND [Locale_id]  = 'en-US';
GO
UPDATE [${dbxschemaname}].[featureaction] SET [name] = 'View Withdrawal Summary' WHERE (id = 'WITHDRAW_CASH_VIEW_SUMMARY');
GO
UPDATE [${dbxschemaname}].[featureaction] SET [status] = 'SID_ACTION_ACTIVE' WHERE ([id] = 'BULK_PAYMENT_REQUEST_ADD_PO');
GO
UPDATE [${dbxschemaname}].[featureaction] SET [status] = 'SID_ACTION_ACTIVE' WHERE ([id] = 'BULK_PAYMENT_REQUEST_REMOVE_PO');
UPDATE [${dbxschemaname}].[featureaction] SET [status] = 'SID_ACTION_ACTIVE' WHERE ([id] = 'BULK_PAYMENT_REQUEST_EDIT_PO');
GO
UPDATE [${dbxschemaname}].[compositeaction] SET [isEnabled]='1';
UPDATE [${dbxschemaname}].[feature] SET [Status_id]='SID_FEATURE_ACTIVE' where [Status_id]='SID_FEATURE_INACTIVE';
UPDATE [${dbxschemaname}].[featureaction] SET [status]='SID_ACTION_ACTIVE' where [status]='SID_ACTION_INACTIVE';
INSERT INTO  [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh99b6', 'WealthObjects', 'InstrumentDetails', 'getPortfolioAssetsCash', 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW,WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW');

INSERT INTO  [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('WEALTH_ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'Wealth Order Management', 'Wealth Order Management', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '71', '0');
INSERT INTO  [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('WEALTH_CASH_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'Wealth Cash and Currency Functions', 'Wealth Cash and Currency Functions', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '73', '0');
INSERT INTO  [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'Wealth Portfolio Management', 'Wealth Portfolio Management', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '65', '0');
INSERT INTO  [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary]) VALUES ('WEALTH_WATCHLIST', 'RETAIL_AND_BUSINESS_BANKING', 'Wealth Instrument Watch List', 'Wealth Instrument Watch List', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '75', '0');


INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_ORDER_MANAGEMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_ORDER_MANAGEMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_CASH_MANAGEMENT-VIEW');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_CASH_MANAGEMENT-CREATE');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_PORTFOLIO-VIEW');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_WATCHLIST-CREATE');
INSERT INTO  [${dbxschemaname}].[rrole] ([id]) VALUES ('WEALTH_WATCHLIST-VIEW');

UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([Feature_id] = 'ORDER_MANAGEMENT') and ([Locale_id] = 'en-GB');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([Feature_id] = 'ORDER_MANAGEMENT') and ([Locale_id] = 'en-US');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT' WHERE ([Feature_id] = 'CASH_MANAGEMENT') and ([Locale_id] = 'en-GB');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT' WHERE ([Feature_id] = 'CASH_MANAGEMENT') and ([Locale_id] = 'en-US');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_WATCHLIST' WHERE ([Feature_id] = 'WATCH_LIST') and ([Locale_id] = 'en-GB');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_WATCHLIST' WHERE ([Feature_id] = 'WATCH_LIST') and ([Locale_id] = 'en-US');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'PORTFOLIO') and ([Locale_id] = 'en-GB');
UPDATE [${dbxschemaname}].[featuredisplaynamedescription] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'PORTFOLIO') and ([Locale_id] = 'en-US');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([id] = 'ORDER_BLOTTER');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([id] = 'ORDER_MANAGEMENT');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_CASH_MANAGEMENT' WHERE ([id] = 'CASH_MANAGEMENT');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_WATCHLIST' WHERE ([id] = 'WATCH_LIST');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([id] = 'PORTFOLIO');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([id] = 'WEALTH_TRANSACTIONS');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([id] = 'HOLDINGS');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([id] = 'ACCOUNT_ACTIVITY');
UPDATE [${dbxschemaname}].[contractfeatures] SET [featureId] = 'WEALTH_PRODUCT_DETAILS' WHERE ([id] = 'WEALTH_POSITION_AND_PRICING_DATA');

INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Account Activity Summary View', 'Account Activity Summary View', '1', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_CASH_MGMT_CARD_BALANCE_VIEW', 'WEALTH_CASH_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_CASH_MANAGEMENT-VIEW', 'Cash Balance Card View', 'Cash Balance Card View', '1', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE', 'WEALTH_CASH_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'WEALTH_CASH_MANAGEMENT-CREATE', 'Convert Currency Create', 'Convert Currency Create', '1', '0', '0', '4', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE', 'WEALTH_CASH_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'WEALTH_CASH_MANAGEMENT-CREATE', 'Cash transfer Create', 'Cash transfer Create', '1', '0', '0', '3', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Holdings details View', 'Holdings details View', '0', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW', 'ORDER_BLOTTER', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'ORDER_BLOTTER-VIEW', 'Open Orders View', 'Open Orders View', '0', '0', '1', '2', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW', 'ORDER_BLOTTER', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'ORDER_BLOTTER-VIEW', 'History Orders View', 'History Orders View', '0', '0', '0', '3', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_BUY_ORDER_CREATE', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'ORDER_MANAGEMENT-CREATE', 'Create Buy Order Link', 'Create Buy Order Link', '1', '0', '0', '2', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'ORDER_MANAGEMENT-VIEW', 'Order Acknowledgement View', 'Order Acknowledgement View', '0', '0', '1', '7', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_CANCEL', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'ORDER_MANAGEMENT-VIEW', 'Open Order Cancel', 'Open Order Cancel', '1', '0', '1', '5', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_EDIT', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'ORDER_MANAGEMENT-CREATE', 'Modify Open Order', 'Modify Open Order', '1', '0', '0', '4', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_LINK_VIEW', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'ORDER_MANAGEMENT-VIEW', 'View Order link', 'View Order link', '0', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'ORDER_MANAGEMENT-VIEW', 'Verify Order View', 'Verify Order View', '0', '0', '1', '6', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_ORDER_MGMT_SELL_ORDER_CREATE', 'ORDER_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'ORDER_MANAGEMENT-CREATE', 'Create Sell Order Link', 'Create Sell Order Link', '1', '0', '0', '3', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Account Info View', 'Account Info View', '1', '0', '1', '5', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'ACCOUNT_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Asset Allocation View', 'Asset Allocation View', '0', '0', '1', '2', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Cash Balance View', 'Cash Balance View', '0', '0', '1', '3', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Portfolio Details View', 'Portfolio Details View', '0', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_WATCHLIST_INSTRUMENT_CREATE', 'WEALTH_WATCHLIST', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_WATCHLIST-CREATE', 'Adding an Instrument to watchlist', 'Adding an Instrument to watchlist', '0', '0', '0', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'CREATE', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_WATCHLIST_INSTRUMENT_VIEW', 'WEALTH_WATCHLIST', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_WATCHLIST-VIEW', 'Watchlist Instrument View', 'Watchlist Instrument View', '0', '0', '0', '2', '2021-03-26 12:07:50', '2021-03-26 12:07:50', '2021-03-26 12:07:50', '0', 'SID_ACTION_ACTIVE', 'CREATE', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW', 'WEALTH_PRODUCT_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PRODUCT_DETAILS-VIEW', 'Current Position View', 'Current Position View', '0', '0', '1', '2', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW', 'WEALTH_PRODUCT_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PRODUCT_DETAILS-VIEW', 'Pricing Data View', 'Pricing Data View', '0', '0', '1', '3', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW', 'WEALTH_PORTFOLIO_DETAILS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'WEALTH_PORTFOLIO-VIEW', 'Transaction Details View', 'Transaction Details View', '0', '0', '1', '1', GETDATE(), GETDATE(), GETDATE(), '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');

UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[customeraction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_EDIT', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW', [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW', [featureId] = 'WEALTH_CASH_MANAGEMENT' WHERE ([actionId] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE', [featureId] = 'WEALTH_CASH_MANAGEMENT' WHERE ([actionId] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE', [featureId] = 'WEALTH_CASH_MANAGEMENT' WHERE ([actionId] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW', [featureId] = 'WEALTH_WATCHLIST' WHERE ([actionId] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE', [featureId] = 'WEALTH_WATCHLIST' WHERE ([actionId] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW', [featureId] = 'WEALTH_PRODUCT_DETAILS' WHERE ([actionId] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW', [featureId] = 'WEALTH_PRODUCT_DETAILS' WHERE ([actionId] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW', [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([actionId] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');


UPDATE [${dbxschemaname}].[contractactionlimit] SET [featureId] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([featureId] = 'ORDER_BLOTTER');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [featureId] = 'WEALTH_CASH_MANAGEMENT' WHERE ([featureId] = 'CASH_MANAGEMENT');
UPDATE [${dbxschemaname}].[contractactionlimit] SET [featureId] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([featureId] = 'ACCOUNT_ACTIVITY');



UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([actionId] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([actionId] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([actionId] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([actionId] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([actionId] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([actionId] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([actionId] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([actionId] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([actionId] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([actionId] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([actionId] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([actionId] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([actionId] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([actionId] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([actionId] = 'PORTFOLIO_CASH_BALANCE_VIEW');

UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([actionId] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([actionId] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([actionId] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET [actionId] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([actionId] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');

UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[groupactionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');

UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');

UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[actiondisplaynamedescription] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');

UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[actionlimit] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');

UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([Feature_id] = 'ORDER_BLOTTER');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([Feature_id] = 'ORDER_MANAGEMENT');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT' WHERE ([Feature_id] = 'CASH_MANAGEMENT');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_WATCHLIST' WHERE ([Feature_id] = 'WATCH_LIST');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'PORTFOLIO');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'WEALTH_TRANSACTIONS');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'HOLDINGS');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'ACCOUNT_ACTIVITY');
UPDATE [${dbxschemaname}].[compositeaction] SET [Feature_id] = 'WEALTH_PRODUCT_DETAILS' WHERE ([Feature_id] = 'WEALTH_POSITION_AND_PRICING_DATA');

UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_SUMMARY_VIEW' WHERE ([Action_id] = 'ACCOUNT_ACTIVITY_SUMMARY_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_CARD_BALANCE_VIEW' WHERE ([Action_id] = 'CASH_MANAGEMENT_CARD_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE' WHERE ([Action_id] = 'CASH_MANAGEMENT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_HOLDINGS_VIEW' WHERE ([Action_id] = 'HOLDINGS_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW' WHERE ([Action_id] = 'ORDER_BLOTTER_ORDERS_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW' WHERE ([Action_id] = 'ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE' WHERE ([Action_id] = 'ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ACCOUNT_INFO_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ACCOUNT_INFO_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW' WHERE ([Action_id] = 'PORTFOLIO_ASSET_ALLOCATION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW' WHERE ([Action_id] = 'PORTFOLIO_CASH_BALANCE_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_SUMMARY_VIEW' WHERE ([Action_id] = 'PORTFOLIO_DETAILS_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_CREATE' WHERE ([Action_id] = 'WATCH_LIST_ADD_INSTRUMENT_CREATE');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_WATCHLIST_INSTRUMENT_VIEW' WHERE ([Action_id] = 'WATCH_LIST_INSTRUMENT_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_CURRENT_POSITION_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PRODUCT_DETAILS_PRICING_DATA_VIEW' WHERE ([Action_id] = 'WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW');
UPDATE [${dbxschemaname}].[featureactionroletype] SET [Action_id] = 'WEALTH_PORTFOLIO_DETAILS_TRANSACTIONS_VIEW' WHERE ([Action_id] = 'WEALTH_TRANSACTIONS_DETAILS_VIEW');

UPDATE [${dbxschemaname}].[featureroletype] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT' WHERE ([Feature_id] = 'ORDER_BLOTTER');
UPDATE [${dbxschemaname}].[featureroletype] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT' WHERE ([Feature_id] = 'CASH_MANAGEMENT');
UPDATE [${dbxschemaname}].[featureroletype] SET [Feature_id] = 'WEALTH_WATCHLIST' WHERE ([Feature_id] = 'WATCH_LIST');
UPDATE [${dbxschemaname}].[featureroletype] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS' WHERE ([Feature_id] = 'PORTFOLIO');

DELETE FROM [${dbxschemaname}].[customeraction] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[contractactionlimit] WHERE ([actionId] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[groupactionlimit] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE ([actionId] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[actiondisplaynamedescription] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[featureactionroletype] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[actionlimit] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');
DELETE FROM [${dbxschemaname}].[compositeaction] WHERE ([Action_id] = 'PORTFOLIO_PRODUCT_SEARCH_VIEW');

DELETE FROM [${dbxschemaname}].[featureaction] WHERE [id]in  ('ACCOUNT_ACTIVITY_SUMMARY_VIEW','CASH_MANAGEMENT_CARD_BALANCE_VIEW','CASH_MANAGEMENT_CURRENCY_COVERSION_CREATE','CASH_MANAGEMENT_TRANSFER_CASH_CREATE','HOLDINGS_DETAILS_VIEW','ORDER_BLOTTER_OPEN_ORDER_VIEW','ORDER_BLOTTER_ORDERS_HISTORY_VIEW','ORDER_MANAGEMENT_BUY_ORDER_LINK_CREATE','ORDER_MANAGEMENT_ORDER_ACKNOWLEDGEMENT_VIEW','ORDER_MANAGEMENT_ORDER_CANCEL','ORDER_MANAGEMENT_ORDER_EDIT','ORDER_MANAGEMENT_ORDER_LINK_VIEW','ORDER_MANAGEMENT_ORDER_VERIFICATION_VIEW','ORDER_MANAGEMENT_SELL_ORDER_LINK_CREATE','PORTFOLIO_ACCOUNT_INFO_VIEW','PORTFOLIO_ASSET_ALLOCATION_VIEW','PORTFOLIO_CASH_BALANCE_VIEW','PORTFOLIO_DETAILS_VIEW','WATCH_LIST_ADD_INSTRUMENT_CREATE','WATCH_LIST_INSTRUMENT_VIEW','WEALTH_POSITION_AND_PRICING_DATA_CURRENT_POSITION_VIEW','WEALTH_POSITION_AND_PRICING_DATA_PRICING_DATA_VIEW','WEALTH_TRANSACTIONS_DETAILS_VIEW');
DELETE FROM [${dbxschemaname}].[featuredisplaynamedescription] WHERE [Feature_id]in  ('ORDER_BLOTTER','ORDER_MANAGEMENT','CASH_MANAGEMENT','WATCH_LIST','PORTFOLIO','WEALTH_TRANSACTIONS','HOLDINGS','ACCOUNT_ACTIVITY','WEALTH_POSITION_AND_PRICING_DATA');
DELETE FROM [${dbxschemaname}].[featureroletype] WHERE [Feature_id]in  ('ORDER_BLOTTER','ORDER_MANAGEMENT','CASH_MANAGEMENT','WATCH_LIST','PORTFOLIO','WEALTH_TRANSACTIONS','HOLDINGS','ACCOUNT_ACTIVITY','WEALTH_POSITION_AND_PRICING_DATA');
DELETE FROM [${dbxschemaname}].[contractfeatures] WHERE [featureId]in  ('ORDER_BLOTTER','ORDER_MANAGEMENT','CASH_MANAGEMENT','WATCH_LIST','PORTFOLIO','WEALTH_TRANSACTIONS','HOLDINGS','ACCOUNT_ACTIVITY','WEALTH_POSITION_AND_PRICING_DATA');
DELETE FROM [${dbxschemaname}].[contractfeatures] WHERE [featureId]in  ('ORDER_BLOTTER','ORDER_MANAGEMENT','CASH_MANAGEMENT','WATCH_LIST','PORTFOLIO','WEALTH_TRANSACTIONS','HOLDINGS','ACCOUNT_ACTIVITY','WEALTH_POSITION_AND_PRICING_DATA');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'ORDER_BLOTTER_OPEN_ORDER_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT', [Rrole_id] = 'WEALTH_CASH_MANAGEMENT-VIEW' WHERE ([id] = 'CASH_MANAGEMENT_TRANSFER_CASH_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_CASH_MANAGEMENT', [Rrole_id] = 'WEALTH_CASH_MANAGEMENT-VIEW' WHERE ([id] = 'CASH_MANAGEMENT_CURRENCY_COVERSION_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'ORDER_BLOTTER_ORDERS_HISTORY_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_OPEN_ORDER_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_HISTORY_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-CREATE' WHERE ([id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_ACKNOWLEDGEMENT_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-CREATE' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_LINK_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-VIEW' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_VERIFICATION_VIEW');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_ORDER_MANAGEMENT', [Rrole_id] = 'WEALTH_ORDER_MANAGEMENT-CREATE' WHERE ([id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [Feature_id] = 'WEALTH_PORTFOLIO_DETAILS', [Rrole_id] = 'WEALTH_PORTFOLIO-VIEW' WHERE ([id] = 'ACCOUNT_ACTIVITY_DETAIL_VIEW');

UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'ORDER_BLOTTER_OPEN_ORDER_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_CASH_MGMT_CURRENCY_COVERSION_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_CASH_MGMT_TRANSFER_CASH_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_ORDER_MGMT_BUY_ORDER_CREATE');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_CANCEL');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_ORDER_MGMT_ORDER_EDIT');
UPDATE [${dbxschemaname}].[featureaction] SET [limitgroupId] = 'SINGLE_PAYMENT' WHERE ([id] = 'WEALTH_ORDER_MGMT_SELL_ORDER_CREATE');

DELETE FROM [${dbxschemaname}].[featureaction] WHERE [Feature_id] in  ('PORTFOLIO');
DELETE FROM [${dbxschemaname}].[feature] WHERE [id] in  ('ORDER_BLOTTER','ORDER_MANAGEMENT','CASH_MANAGEMENT','WATCH_LIST','PORTFOLIO','WEALTH_TRANSACTIONS','HOLDINGS','ACCOUNT_ACTIVITY','WEALTH_POSITION_AND_PRICING_DATA');

INSERT INTO  [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh91b7', 'WealthObjects', 'PortfolioPerformance', 'getPortfolioPerformance', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW');

INSERT INTO  [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [isPrimary], [DisplaySequence], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag], [status], [accesspolicyId], [actionlevelId]) VALUES ('WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW','WEALTH_PORTFOLIO_DETAILS','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','WEALTH_PORTFOLIO-VIEW','Performance View','Performance View','0','0','1','3', GETDATE(), GETDATE(), GETDATE(),'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL');
INSERT INTO  [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW','en-US', 'Performance View', 'Performance View');
INSERT INTO  [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription]) VALUES ('WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW','en-GB', 'Performance View', 'Performance View');
INSERT INTO  [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_WEALTH', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW');
INSERT INTO  [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id], [isEnabled]) VALUES ('CAID353', 'PID45', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW', 'WEALTH_PORTFOLIO_DETAILS', '0');
INSERT INTO  [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('0d1b9326-810e-4fbf-9465-30c4f5f61146', '90356097-7fdf-4b8c-89bd-8a1065338a97', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW', 'UID10', GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO  [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('91d9c502-3100-4c22-ad38-915cda31fc7a', '4dd6183f-61d2-410f-8a4f-871af67ac933', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW', 'UID10', GETDATE(), GETDATE(), GETDATE(), '0');
INSERT INTO  [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId]) VALUES ('15ba4d42-3e37-4180-bc3e-7a5afcfdb67c', 'f85d8392-9afe-4128-b23e-a370f138784f', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW');
INSERT INTO  [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id]) VALUES ('69d7e66b-070d-4002-a71b-850b1761e474', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW');
GO
--INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh99b6', 'WealthObjects', 'InstrumentDetails', 'getPortfolioAssetsCash', 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW,WEALTH_PORTFOLIO_DETAILS_ASSET_ALLOCATION_VIEW');
GO

--INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh91b7', 'WealthObjects', 'PortfolioPerformance', 'getPortfolioPerformance', 'WEALTH_PORTFOLIO_DETAILS_PERFORMANCE_VIEW');
GO

UPDATE [${dbxschemaname}].[dbxalerttype] SET [isAccountLevel] = '1', [IsGlobal] = '0' WHERE [id] = 'APPROVAL_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[dbxalerttype] SET [isAccountLevel] = '1', [IsGlobal] = '0' WHERE [id]= 'CHECKBOOK_REQUEST';

UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'APPROVE_CHEQUE_BOOK_REQUEST';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'CHECKBOOK_REQUEST_TO_INITIATOR';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS';
UPDATE [${dbxschemaname}].[alertSubType] SET [isAccountLevel] = '1', [isGlobal] = '0' WHERE [id] = 'CHECKBOOK_REQUEST_EXECUTED';

GO
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'APPROVE_CHEQUE_BOOK_REQUEST', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'APPROVE_CHEQUE_BOOK_REQUEST', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype]([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'CHECKBOOK_REQUEST_TO_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'CHECKBOOK_REQUEST_TO_INITIATOR', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId] , [softdeleteflag]) VALUES ('1', 'CHECKBOOK_REQUEST_EXECUTED', '0');
INSERT INTO [${dbxschemaname}].[alertsubtypeaccounttype] ([accountTypeId], [alertSubTypeId], [softdeleteflag]) VALUES ('2', 'CHECKBOOK_REQUEST_EXECUTED', '0');

GO


INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('MXN', 'Mexican peso', 'Mex$');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XAF', 'Central African CFA Franc', 'FCFA');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('ILS', 'Israeli Shekel', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('RSD', 'Serbian dinar', 'din');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('HUF', 'Hungarian forint', 'Ft');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('BGN', 'Bulgarian lev', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('ARS', 'Argentine peso', '$');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('DKK', 'Danish krone', 'Kr.');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('HKD', 'Hong Kong dollar', 'HK$');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('KWD', 'Kuwaiti Dinar', 'KD');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('LBP', 'Lebanese Pound', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('LKR', 'SriLankan Rupee', 'Rs');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('NPR', 'Nepali Rupee', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('PHP', 'Philippine Peso', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('PLN', 'Polish Zloty', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('SEK', 'Swedish krona', 'kr');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('THB', 'Thai baht', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('TWD', 'New Taiwan Dollar', 'NT$');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('ZAR', 'South African Rand', 'R');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('AMD', 'Armenian Dram', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('AZN', 'Azerbaijani manat', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('GEL', 'Georgian lari', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('KZT', 'Kazakhstani tenge', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('KGS', 'Kyrgyzstani som', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('UZS', 'Uzbekistani som', 'som');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('NOK', 'Norwegian krone', 'kr');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('JOD', 'Jordanian dinar', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('TND', 'Tunisian dinar', 'DT');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('TRY', 'Turkish lira', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XAV', 'Avios Points', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XIN', 'Internal Points', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XAI', 'Aimia Point', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XAG', 'SILVER in Ounces', '-');
INSERT INTO [${dbxschemaname}].[currency] ([code], [name], [symbol]) VALUES ('XAU', 'Gold in Ounces', '-');

GO
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('49', 'USD', 'MXN', 'Currency', '19.6519', '19.6019');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('50', 'USD', 'XAF', 'Currency', '599.05', '598.1');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('51', 'USD', 'XAF', 'TT', '599.9', '598.9');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('52', 'USD', 'ILS', 'Currency', '3.52', '3.48');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('53', 'USD', 'ILS', 'TT', '3.6', '3.5');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('54', 'USD', 'RSD', 'Currency', '103.55', '103.4');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('55', 'USD', 'HUF', 'Currency', '367.08', '365.08');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('56', 'USD', 'BGN', 'Currency', '1.66', '1.6');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('57', 'USD', 'ARS', 'Currency', '89.4416', '89.4411');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('58', 'USD', 'DKK', 'Currency', '6.1278', '6.1112');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('59', 'USD', 'HKD', 'Currency', '7.7539', '7.7521');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('60', 'USD', 'HKD', 'TT', '7.754', '7.752');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('61', 'USD', 'KWD', 'Currency', '0.3051', '0.3');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('62', 'USD', 'LBP', 'Currency', '1515.5', '1499.5');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('63', 'USD', 'LBP', 'TT', '1515.7', '1499.3');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('64', 'USD', 'LKR', 'Currency', '194.44', '192.54');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('65', 'USD', 'NPR', 'Currency', '116.34', '116.33');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('66', 'USD', 'PHP', 'Currency', '48.78', '48.59');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('67', 'USD', 'PLN', 'Currency', '3.71', '3.7');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('68', 'USD', 'SEK', 'Currency', '8.301', '8.298');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('69', 'USD', 'SEK', 'TT', '8.299', '8.298');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('70', 'USD', 'THB', 'Currency', '30.047', '30.001');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('71', 'USD', 'TWD', 'Currency', '27.84', '27.8');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('72', 'USD', 'TWD', 'TT', '27.94', '27.77');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('73', 'USD', 'ZAR', 'Currency', '14.76', '14.63');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('74', 'USD', 'ZAR', 'TT', '14.75', '14.65');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('75', 'USD', 'AMD', 'Currency', '520.1', '517.9');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('76', 'USD', 'AZN', 'Currency', '1.7', '1.69');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('77', 'USD', 'GEL', 'Currency', '3.33', '3.28');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('78', 'USD', 'KZT', 'Currency', '416.95', '412.85');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('79', 'USD', 'KGS', 'Currency', '84.75', '84.50');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('80', 'USD', 'UZS', 'Currency', '10563.96', '10535.96');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('81', 'USD', 'NOK', 'Currency', '8.49', '8.48');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('82', 'USD', 'JOD', 'Currency', '0.72', '0.68');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('83', 'USD', 'JOD', 'TT', '0.73', '0.67');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('84', 'USD', 'TND', 'Currency', '3.24', '2.14');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('85', 'USD', 'TND', 'TT', '3.44', '2.04');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('86', 'USD', 'TRY', 'Currency', '3.81', '3.8');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('87', 'USD', 'XAV', 'Currency', '1', '1');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('88', 'USD', 'XIN', 'Currency', '1', '1');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('89', 'USD', 'XAI', 'Currency', '1', '1');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('90', 'USD', 'XAG', 'Currency', '27.166', '28.566');
INSERT INTO [${dbxschemaname}].[currencymarketrates] ([id], [baseCurrencyCode], [quoteCurrencyCode], [marketId], [buyRate], [sellRate]) VALUES ('91', 'USD', 'XAG', 'Currency', '1806.665', '1806.825');
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-958a-4eae-9080-8e602f637122', 'SignatoryObject', 'SignatoryGroup', 'fetchSignatoryGroups', 'SIGNATORY_GROUP_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-958a-4eae-9080-8e002f637121', 'SignatoryObject', 'SignatoryGroup', 'fetchSignatoryGroupDetails', 'SIGNATORY_GROUP_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8e022f637129', 'SignatoryObject', 'SignatoryGroup', 'createSignatoryGroup', 'SIGNATORY_GROUP_VIEW,SIGNATORY_GROUP_CREATE_EDIT');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('9fa38ec1-959a-4eae-9080-8r022f767123', 'SignatoryObject', 'SignatoryGroup', 'updateSignatoryGroup', 'SIGNATORY_GROUP_VIEW,SIGNATORY_GROUP_CREATE_EDIT');

GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh97c8', 'WealthObjects', 'InstrumentDetails', 'getCashAccounts', 'WEALTH_PORTFOLIO_DETAILS_CASH_BALANCE_VIEW');
GO
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'PSD2_TPP_CONSENT_REVOKE');
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id]) VALUES ('TYPE_ID_BUSINESS', 'PSD2_TPP_CONSENT_VIEW');
GO
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22g1', 'WealthObjects', 'InstrumentDetails', 'getFavoriteInstruments', 'WEALTH_WATCHLIST_INSTRUMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22g2', 'WealthObjects', 'InstrumentDetails', 'getWealthDashboard', 'WEALTH_INVESTMENT_DETAILS_TOTAL_ASSETS_VIEW,WEALTH_INVESTMENT_DETAILS_INVESTMENT_SUMMARY_VIEW,WEALTH_MARKET_AND_NEWS_MARKET_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22g3', 'WealthObjects', 'InstrumentDetails', 'getDashboardGraphData', 'WEALTH_INVESTMENT_DETAILS_INVESTMENT_SUMMARY_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22g4', 'WealthObjects', 'InstrumentDetails', 'getAssetList', 'WEALTH_INVESTMENT_DETAILS_TOTAL_ASSETS_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22h1', 'WealthObjects', 'FavouriteInstruments', 'getUserFavouriteInstruments', 'WEALTH_WATCHLIST_INSTRUMENT_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh22h2', 'WealthObjects', 'FavouriteInstruments', 'updateUserFavouriteInstruments', 'WEALTH_WATCHLIST_INSTRUMENT_CREATE');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh23j1', 'WealthObjects', 'MarketNews', 'getTopMarketNews', 'WEALTH_MARKET_AND_NEWS_TOP_NEWS_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh24k1', 'WealthObjects', 'DailyMarket', 'getDailyMarket', 'WEALTH_MARKET_AND_NEWS_MARKET_VIEW');
INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('hzbyhsjn-4d43-a2cd-ce1f85a7hsbzvh25m1', 'WealthObjects', 'Portfolio', 'getDashboardRecentActivity', 'WEALTH_INVESTMENT_DETAILS_RECENT_ACTIVITY_VIEW');
GO
UPDATE [${dbxschemaname}].[compositeaction] SET [isEnabled] = '1' WHERE ([isEnabled] = '0');
GO

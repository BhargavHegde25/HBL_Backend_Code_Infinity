INSERT INTO [${dbxschemaname}].[mfaserviceconfig] ([serviceName], [transactionType]) VALUES ('P2P_CREATE', 'transactionobjects_transaction_p2ptransfer');

UPDATE [${dbxschemaname}].[alertrecipienttype] SET [servicename] = 'authProductServices' WHERE ([id] = '2');
GO


UPDATE [${dbxschemaname}].[alertsubtype] SET [value1] = '50' WHERE ([id] = 'MAXIMUM_BALANCE_ALERT');
UPDATE [${dbxschemaname}].[alertsubtype] SET [value1] = '50' WHERE ([id] = 'DEPOSIT_AMOUNT_ALERT');
UPDATE [${dbxschemaname}].[alertsubtype] SET [value1] = '50' WHERE ([id] = 'WITHDRAWAL_AMOUNT_ALERT');
GO

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'CardManagementServices',[object_name] = 'DebitCardProducts' ,[operation] = 'getProducts' WHERE [service_name] = 'RBObjects' AND [object_name] = 'CardProducts' AND [operation] = 'getCardProducts';
GO
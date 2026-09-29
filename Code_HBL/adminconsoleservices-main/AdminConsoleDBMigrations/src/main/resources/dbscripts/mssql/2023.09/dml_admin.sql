USE [${dbxschemaname}];
GO

UPDATE [${dbxschemaname}].[alertsubtype] SET [recipienttype] = '1', [isAutoSubscribeEnabled] = '0', [externalSystem] = '0' WHERE [alerttypeid] = 'LOGIN';
GO


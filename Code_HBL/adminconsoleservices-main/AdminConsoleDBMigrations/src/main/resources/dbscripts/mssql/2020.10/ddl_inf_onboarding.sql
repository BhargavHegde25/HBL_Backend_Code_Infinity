USE [${dbxdbname}]
GO
ALTER TABLE [${dbxschemaname}].[country] ADD [phoneCountryCode] VARCHAR(50) NULL;
GO
USE [${dbxdbname}]
GO

ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ALTER COLUMN [bankName] NVARCHAR(200);
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ALTER COLUMN [bankName] NVARCHAR(200);
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ALTER COLUMN [bankName] VARCHAR(200);
ALTER TABLE [${dbxschemaname}].[externalaccount] ALTER COLUMN [bankName] NVARCHAR(200);
GO

ALTER TABLE [${dbxschemaname}].[featureaction] DROP CONSTRAINT [FK_featureaction_rrole];
ALTER TABLE [${dbxschemaname}].[rrole] ALTER COLUMN [id] VARCHAR(100) NOT NULL ;
ALTER TABLE [${dbxschemaname}].[featureaction] ALTER COLUMN  [Rrole_id] VARCHAR(100) NULL ;
ALTER TABLE [${dbxschemaname}].[featureaction]  ADD DEFAULT NULL FOR [Rrole_id];
ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT FK_featureaction_rrole  FOREIGN KEY (Rrole_id)  REFERENCES [${dbxschemaname}].[rrole]([id]);
ALTER TABLE [${dbxschemaname}].[bbrequest] ALTER COLUMN [featureActionId] VARCHAR(64) NOT NULL ;
GO
GO
ALTER TABLE [${dbxschemaname}].[external_feature_actions] ALTER COLUMN id varchar(255) NOT NULL;
GO

GO
ALTER TABLE [${dbxschemaname}].[external_feature_actions] ADD PRIMARY KEY (id);
GO

GO
CREATE TABLE [${dbxschemaname}].[sca_alias](
    [id] [int] IDENTITY NOT NULL,
    [hashedvalue] VARCHAR(200) NOT NULL,
    [userId] VARCHAR(100) DEFAULT NULL,
    [partyId] VARCHAR(100) NOT NULL,
    [type] VARCHAR(100) DEFAULT NULL,
    [createdby] VARCHAR(100) DEFAULT NULL,
    [modifiedby] VARCHAR(100) DEFAULT NULL,
    [createdts] [datetime] NOT NULL,
    [lastmodifiedts] [datetime] NOT NULL,
    [synctimestamp] [datetime] NOT NULL,
    [softdeleteflag] tinyint NOT NULL DEFAULT '0',
      PRIMARY KEY ([id])
)

GO
CREATE TABLE [${dbxschemaname}].[Documents] (
  [documentId] nvarchar(50) NOT NULL,
  [referenceId] nvarchar(50) DEFAULT NULL,
  [content] varbinary(MAX) NULL,
  [documentName] nvarchar(150) DEFAULT NULL,
  [status] nvarchar(45) DEFAULT NULL,
  [createdts] datetime2(0) DEFAULT getdate() NOT NULL,
  [lastmodifiedts] datetime2(0) DEFAULT getdate() NOT NULL,
  [modifiedBy] nvarchar(50) DEFAULT NULL,
  [mimeType] nvarchar(50) DEFAULT NULL,
  [category] nvarchar(50) DEFAULT NULL,
  [version] nvarchar(50) DEFAULT NULL,
  [ownerSystemId] nvarchar(50) DEFAULT NULL,
  [documentGroup] nvarchar(50) DEFAULT NULL,
  [applicationId] nvarchar(50) DEFAULT NULL,
  PRIMARY KEY ([documentId])
);
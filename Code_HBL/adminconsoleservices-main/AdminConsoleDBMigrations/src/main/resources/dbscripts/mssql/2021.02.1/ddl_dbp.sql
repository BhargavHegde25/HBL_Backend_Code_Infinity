DROP TABLE IF EXISTS [${dbxschemaname}].[contractcustomrole];

CREATE TABLE [${dbxschemaname}].[contractcustomrole] (
  [id] VARCHAR(50) NOT NULL,
  [contractId] VARCHAR(50) NULL DEFAULT NULL,
  [customerId] VARCHAR(50) NULL DEFAULT NULL,
  [coreCustomerId] VARCHAR(45) NULL DEFAULT NULL,
  [customRoleId] VARCHAR(50) NULL DEFAULT NULL,
  [roleId] VARCHAR(50) NULL DEFAULT NULL,
  [autoSyncAccounts] bit DEFAULT 0 NOT NULL
);

GO

ALTER TABLE [${dbxschemaname}].[customrole] DROP CONSTRAINT customrole$customrole_ibfk_2
GO

ALTER TABLE [${dbxschemaname}].[customrole] alter column organization_id varchar(50) NULL;
GO
ALTER TABLE [${dbxschemaname}].[customrole] 
ADD CONSTRAINT default_null_organization_id DEFAULT NULL FOR organization_id;
GO

ALTER TABLE [${dbxschemaname}].[contractCustomrole] DROP COLUMN id
GO

ALTER TABLE [${dbxschemaname}].[contractCustomrole] ADD id INT IDENTITY(1,1)
GO

ALTER TABLE [${dbxschemaname}].[customroleaccounts] ADD accountType varchar(50)
GO
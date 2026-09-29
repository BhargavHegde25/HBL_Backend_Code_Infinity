ALTER TABLE [${dbxschemaname}].[paymentfiles]
ALTER COLUMN [paymentFileType] varchar(100) NOT NULL;
GO

USE [${dbxdbname}]

ALTER TABLE [${dbxschemaname}].[accounts] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customeraddress] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customercommunication] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractcustomers] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customeraction] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[excludedcustomeraccounts] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[excludedcustomeraction] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customergroup] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractcustomrole] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractaccounts] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contract] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractaddress] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractcorecustomers] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[excludedcontractaccounts] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractcommunication] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[address] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractactionlimit] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customerdevice] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customerpreference] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customerbusinesstype] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[contractfeatures] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

ALTER TABLE [${dbxschemaname}].[customerlimitgrouplimits] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';

GO

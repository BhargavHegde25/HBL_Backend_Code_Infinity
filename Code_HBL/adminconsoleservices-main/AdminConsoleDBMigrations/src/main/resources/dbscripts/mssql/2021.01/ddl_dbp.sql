/****** Object:  Table [${dbxschemaname}].[alertrecipienttype]    Script Date: 11/4/2020 4:20:35 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertrecipienttype] (
  [id] TINYINT NOT NULL,
  [name] VARCHAR(20) NOT NULL,
  [isaccountlevel] SMALLINT NULL DEFAULT '0',
  [servicename] VARCHAR(50) NULL,
  [operationname] VARCHAR(50) NULL ,
  [inputparamsmapping] VARCHAR(1000) NULL,
  [createdts] DATETIME2(0) NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NULL DEFAULT GETDATE(),
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtype] add [recipienttype] TINYINT DEFAULT 1;
GO

ALTER TABLE [${dbxschemaname}].[alertsubtype] WITH CHECK ADD CONSTRAINT [FK_alertsubtype_alertrecipienttype] FOREIGN KEY([recipienttype])
REFERENCES [${dbxschemaname}].[alertrecipienttype] ([id]) ;
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] DROP CONSTRAINT [FK_alertsubtypecustomertype_customertype] ;
GO


DROP TABLE IF EXISTS [${dbxschemaname}].[market];
CREATE TABLE [${dbxschemaname}].[market] (
  [id] varchar(20) NOT NULL,
  [name] varchar(50) NOT NULL,
  PRIMARY KEY ([id])
) ;

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[currencymarketrates];
CREATE TABLE [${dbxschemaname}].[currencymarketrates] (
  [id] int NOT NULL,
  [baseCurrencyCode] nvarchar(10) NOT NULL,
  [quoteCurrencyCode] nvarchar(10) NOT NULL,
  [marketId] varchar(20) NOT NULL,
  [buyRate] varchar(50) NOT NULL,
  [sellRate] varchar(50) NOT NULL,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_currency_code_recent_base] FOREIGN KEY ([baseCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_currency_code_recentquote] FOREIGN KEY ([quoteCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_market_id] FOREIGN KEY ([marketId]) REFERENCES [${dbxschemaname}].[market]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX [FK_currency_code_rates_idx] ON [${dbxschemaname}].[currencymarketrates]([baseCurrencyCode]);
CREATE INDEX [FK_currency_code_recentquote] ON [${dbxschemaname}].[currencymarketrates]([quoteCurrencyCode]);
CREATE INDEX [FK_market_id] ON [${dbxschemaname}].[currencymarketrates]([marketId]);

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[popularcurrencies];
CREATE TABLE [${dbxschemaname}].[popularcurrencies] (
  [id] int NOT NULL,
  [baseCurrencyCode] nvarchar(10) NOT NULL,
  [quoteCurrencyCode] nvarchar(10) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_currency_code_base] FOREIGN KEY ([baseCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_currency_code_quote] FOREIGN KEY ([quoteCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX [FK_currency_code_idx] ON [${dbxschemaname}].[popularcurrencies]([baseCurrencyCode]);
CREATE INDEX [FK_currency_code_quote] ON [${dbxschemaname}].[popularcurrencies]([quoteCurrencyCode]);

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[recentcurrencies];
CREATE TABLE [${dbxschemaname}].[recentcurrencies] (
  [id] varchar(60) NOT NULL ,
  [customerId] nvarchar(50) NOT NULL,
  [quoteCurrencyCode] nvarchar(10) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_currency_code_recent] FOREIGN KEY ([quoteCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_customer_id_recent] FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer]([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;

CREATE INDEX [FK_customer_id_idx] ON [${dbxschemaname}].[recentcurrencies]([customerId]);
CREATE INDEX [FK_currency_code_recent] ON [${dbxschemaname}].[recentcurrencies]([quoteCurrencyCode]);

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[countrybasecurrency];
CREATE TABLE [${dbxschemaname}].[countrybasecurrency] (
  [id] int NOT NULL,
  [countryCode] nvarchar(50) NOT NULL,
  [baseCurrencyCode] nvarchar(10) NOT NULL,
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_country_id] FOREIGN KEY ([countryCode]) REFERENCES [${dbxschemaname}].[country]([id]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_currency_code] FOREIGN KEY ([baseCurrencyCode]) REFERENCES [${dbxschemaname}].[currency]([code]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO

CREATE INDEX [FK_country_id_idx] ON [${dbxschemaname}].[countrybasecurrency]([countryCode]);
CREATE INDEX [FK_currency_code] ON [${dbxschemaname}].[countrybasecurrency]([baseCurrencyCode]);

GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contract]
(
   [id] nvarchar(50)  NOT NULL,
   [servicedefinitionId] nvarchar(50)  NULL,
   [serviceType] nvarchar(50)  NULL,
   [name] nvarchar(50)  NULL,
   [description] nvarchar(200)  NULL,
   [statusId] nvarchar(50)  NOT NULL DEFAULT 'SID_CONTRACT_PENDING',
   [faxId] nvarchar(45)  NULL,
   [createdby] nvarchar(50)  NULL,
   [createdts] datetime  NOT NULL DEFAULT GETDATE(),
   [rejectedby] nvarchar(50)  NULL,
   [rejectedts] datetime  NULL DEFAULT GETDATE(),
   [rejectedReason] nvarchar(45)  NULL, 
    CONSTRAINT PK_contract_id PRIMARY KEY ([id]),
	CONSTRAINT [Name_UNIQUE] UNIQUE  ([name])
)
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contractaccounts]
(
   [id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [accountId] nvarchar(50)  NOT NULL,
   [accountName] nvarchar(50)  NULL,
   [typeId] nvarchar(50)  NOT NULL,
   [coreCustomerId] nvarchar(50)  NOT NULL,
   [ownerType] nvarchar(50)  NULL,
   [statusDesc] nvarchar(50)  NULL DEFAULT 'Active',
   [arrangementId] nvarchar(50)  NULL,
   [createdts] datetime  NOT NULL DEFAULT GETDATE(),
   [modifiedby] nvarchar(50)  NULL,
   [lastmodifiedts] datetime  NOT NULL DEFAULT GETDATE(),
 
	CONSTRAINT PK_contractaccounts_id PRIMARY KEY ([id]),
	CONSTRAINT [accountId_UNIQUE] UNIQUE  ([accountId])
)
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contractactionlimit]
(
   [id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [coreCustomerId] nvarchar(50)  NOT NULL,
   [policyId] nvarchar(50)  NULL,
   [featureId] nvarchar(255)  NOT NULL,
   [actionId] nvarchar(255)  NOT NULL,
   [limitGroupId] nvarchar(45)  NULL,
   [limitTypeId] nvarchar(50)  NULL,
   [value] decimal(20, 2)  NULL,
   [createdby] nvarchar(50)  NULL,
   [modifiedby] nvarchar(50)  NULL,
   [createdts] datetime  NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime  NOT NULL DEFAULT GETDATE(),
   [synctimestamp] datetime  NOT NULL DEFAULT GETDATE(),
   [softdeleteflag] smallint  NOT NULL DEFAULT '0',
   
   CONSTRAINT PK_contractactionlimit_id PRIMARY KEY ([id]),
   CONSTRAINT [FK_contractactionlimit_contract_contractId] FOREIGN KEY ([contractId]) REFERENCES [${dbxschemaname}].[contract] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
)
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contractaddress]
(
   [id] int IDENTITY(62, 1)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [addressId] nvarchar(45)  NOT NULL,
   [durationOfStay] nvarchar(45)  NULL,
   [isPrimary] binary(1)  NOT NULL,
   [createdby] nvarchar(45)  NULL,
   [modifiedby] nvarchar(45)  NULL,
   [createdts] datetime  NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime  NULL DEFAULT GETDATE(),
   [typeId] nvarchar(45)  NULL,
   [synctimestamp] datetime  NULL DEFAULT GETDATE(),
   [softdeleteflag] binary(1)  NOT NULL DEFAULT 0,
   
   CONSTRAINT PK_contractaddress_id PRIMARY KEY ([id])
)
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contractcommunication]
(
   [id] int IDENTITY(124, 1)  NOT NULL,
   [typeId] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [sequence] int  NULL,
   [value] nvarchar(100)  NOT NULL,
   [extension] nvarchar(45)  NULL,
   [phoneCountryCode] nvarchar(10)  NULL,
   [description] nvarchar(45)  NULL,
   [isPreferredContactMethod] binary(1)  NULL DEFAULT 0,
   [preferredContactTime] nvarchar(50)  NULL,
   [createdby] nvarchar(50)  NULL,
   [modifiedby] nvarchar(50)  NULL,
   [createdts] datetime  NULL,
   [lastmodifiedts] datetime  NULL,
   [synctimestamp] datetime  NULL,
   [softdeleteflag] binary(1)  NULL DEFAULT 0,
   
   CONSTRAINT PK_contractcommunication_id PRIMARY KEY ([id])
)
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[contractcorecustomers]
(
   [id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [taxId] nvarchar(50)  NULL,
   [coreCustomerId] nvarchar(50)  NOT NULL,
   [coreCustomerName] nvarchar(50)  NOT NULL,
   [isPrimary] smallint  NOT NULL DEFAULT 0,
   [isBusiness] smallint  NOT NULL DEFAULT 0,
   [sectorId] nvarchar(50)  NULL,
   
    CONSTRAINT PK_contractcorecustomers_id PRIMARY KEY ([id])
)
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE 
[${dbxschemaname}].[contractfeatures]
(
   [id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NOT NULL,
   [coreCustomerId] nvarchar(50)  NOT NULL,
   [featureId] nvarchar(255)  NOT NULL,
   [createdts] datetime  NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime  NULL DEFAULT GETDATE(),
   [synctimestamp] datetime  NULL DEFAULT GETDATE(),
   
   CONSTRAINT PK_contractfeatures_id PRIMARY KEY ([id])
)
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[servicedefinition];
CREATE TABLE [${dbxschemaname}].[servicedefinition] (
  [id] varchar(50) NOT NULL,
  [name] varchar(50) DEFAULT NULL,
  [description] varchar(150) DEFAULT NULL,
  [serviceType] nvarchar(50) DEFAULT NULL,
  [status] varchar(50) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_servicedefinition_serviceType] FOREIGN KEY ([serviceType]) REFERENCES [${dbxschemaname}].[membergrouptype] ([id])  ON UPDATE NO ACTION ON DELETE NO ACTION
);
GO

/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]    Script Date: 12/10/2020 9:13:03 PM ******/
DROP VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
GO

/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]    Script Date: 12/10/2020 9:13:03 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertcategorychannel.ChannelID AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((dbxalertcategory
        JOIN alertcategorychannel ON ((alertcategorychannel.AlertCategoryId = dbxalertcategory.id)))
        JOIN dbxalerttype ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
        JOIN alertsubtype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))

GO


/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]    Script Date: 12/10/2020 9:16:22 PM ******/
DROP VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
GO

/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]    Script Date: 12/10/2020 9:16:22 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alerttypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((dbxalerttype
        JOIN alerttypechannel ON ((dbxalerttype.id = alerttypechannel.alertTypeId)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO

/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]    Script Date: 12/10/2020 9:17:35 PM ******/
DROP VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]
GO

/****** Object:  View [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]    Script Date: 12/10/2020 9:17:35 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]
  AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertsubtypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((alertsubtype
        JOIN alertsubtypechannel ON ((alertsubtype.id = alertsubtypechannel.alertSubTypeId)))
        JOIN dbxalerttype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO


CREATE PROCEDURE [${dbxschemaname}].[update_user_recent_currency]
  @_customerId nvarchar(50),
  @_currencyCode nvarchar(10)
AS
BEGIN

DECLARE @rowWithGivenCustomerAndCurrency INT = 0;
DECLARE @lengthOfRecentCurrencies INT = 0;
DECLARE @recentCurrencyToDelete varchar(60);

SELECT @rowWithGivenCustomerAndCurrency = COUNT(*) FROM [${dbxschemaname}].[recentcurrencies] rc WHERE rc.customerId = @_customerId AND rc.quoteCurrencyCode = @_currencyCode;

SELECT @lengthOfRecentCurrencies = COUNT(*) FROM [${dbxschemaname}].[recentCurrencies] rc WHERE rc.customerId = @_customerId ;

SELECT TOP 1 @recentCurrencyToDelete = rc.id FROM [${dbxschemaname}].[recentcurrencies] rc WHERE rc.customerId = @_customerId ORDER BY rc.createdts ASC ;

IF @rowWithGivenCustomerAndCurrency >= 1 BEGIN
  DELETE FROM [${dbxschemaname}].[recentcurrencies]  WHERE recentcurrencies.customerId = @_customerId AND recentcurrencies.quoteCurrencyCode = @_currencyCode;
END
ELSE IF @lengthOfRecentCurrencies >= 5 BEGIN
  DELETE FROM [${dbxschemaname}].[recentcurrencies]  WHERE recentcurrencies.id = @recentCurrencyToDelete;
END 

INSERT INTO [${dbxschemaname}].[recentcurrencies] ([id], [customerId], [quoteCurrencyCode]) VALUES (concat( @_currencyCode, @_customerId ), @_customerId, @_currencyCode);
END

GO

CREATE PROCEDURE [${dbxschemaname}].[forex_proc_get]
  @read_query varchar(8000)

AS
BEGIN
DECLARE @stmt varchar(8000);
SET @stmt = CONCAT('', @read_query);
EXECUTE @stmt;
END

GO

ALTER TABLE [${dbxschemaname}].[messageattachment] 
DROP CONSTRAINT messageattachment$FK_MessageAttachement_Media;

GO

CREATE TABLE [${dbxschemaname}].[accountsstatementfiles] (
  [id] NVARCHAR(50) NOT NULL,
  [userId] NVARCHAR(50)  NULL,
  [fileContent] nvarchar(MAX),
  [fileName] NVARCHAR(150)  NULL,
  [status] NVARCHAR(45)  NULL,
  [createdts]  DATETIME2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts]  DATETIME2(0) NOT NULL DEFAULT GETDATE(),
  [modifiedBy] NVARCHAR(50)  NULL,
  [fileType] NVARCHAR(50)  NULL,
  [failureMessage] NVARCHAR(250)  NULL,
  [fromDate] NVARCHAR(50)  NULL,
  [toDate] NVARCHAR(55)  NULL,
  [accountIds] NVARCHAR(MAX) NULL,
  CONSTRAINT PK_accountsstatementfiles_id PRIMARY KEY (id)
)

GO

create procedure [${dbxschemaname}].[sp_delete_default_constranit]
 @_tableName varchar(100),
 @_columnName varchar(100)
 AS
BEGIN
declare @constraintName varchar(100);
declare @deletequery varchar(500);
set @constraintName=(SELECT d.name   
FROM sys.default_constraints AS d  
INNER JOIN sys.columns AS c  
ON d.parent_object_id = c.object_id
AND d.parent_column_id = c.column_id  
WHERE d.parent_object_id = OBJECT_ID(@_tableName)  
AND c.name = @_columnName)
if @constraintName is not null
BEGIN
 set @deletequery = 'alter table '+@_tableName+' drop constraint '+@constraintName
 exec(@deletequery)
END
END
GO

EXEC sp_rename '[${dbxschemaname}].[wiretransferspayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.wiretransferspayee', 'companyId';
GO

EXEC sp_rename '[${dbxschemaname}].[wiretransferspayee].companyId', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[wiretransferspayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[billpaypayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[billpaypayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.billpaypayee', 'companyId';
GO

EXEC sp_rename '[${dbxschemaname}].[billpaypayee].companyId', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[billpaypayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[internationalpayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[internationalpayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.internationalpayee', 'companyId';
GO

EXEC sp_rename '[${dbxschemaname}].[internationalpayee].companyId', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[internationalpayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[intrabankpayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[intrabankpayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.intrabankpayee', 'companyId';
GO

EXEC sp_rename '[${dbxschemaname}].[intrabankpayee].companyId', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[intrabankpayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[interbankpayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[interbankpayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.interbankpayee', 'companyId';
GO

EXEC sp_rename '[${dbxschemaname}].[interbankpayee].companyId', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[interbankpayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[p2ppayee].customerId', 'createdBy', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[p2ppayee] 
ALTER COLUMN [createdBy] VARCHAR(50) NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.p2ppayee', 'createdts';
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.p2ppayee', 'updatedts';
GO

EXEC sp_rename '[${dbxschemaname}].[p2ppayee].createdts', 'contractId', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[p2ppayee] 
ALTER COLUMN [contractId] VARCHAR(50) NULL;

EXEC sp_rename '[${dbxschemaname}].[p2ppayee].updatedts', 'cif', 'COLUMN';
ALTER TABLE [${dbxschemaname}].[p2ppayee] 
ALTER COLUMN [cif] VARCHAR(50) NULL;

GO

ALTER TABLE [${dbxschemaname}].[application] 
ADD [newSettings] [bit] DEFAULT 0 NOT NULL;

GO

ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [addressLine2] VARCHAR(50) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [email] VARCHAR(50) DEFAULT NULL;

GO


ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD [contractId] VARCHAR(20) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD [contractTypeId] VARCHAR(20) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [contractId] VARCHAR(20) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [coreCustomerId] VARCHAR(20) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [isBusinessAccount] VARCHAR(20) DEFAULT NULL;

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[contractcustomers];

CREATE TABLE [${dbxschemaname}].[contractcustomers] (
  [id] NVARCHAR(50) NOT NULL,
  [contractId] VARCHAR(50) DEFAULT NULL,
  [customerId] NVARCHAR(50) DEFAULT NULL,
  [coreCustomerId] VARCHAR(45) DEFAULT NULL,
  [isAdmin] bit NOT NULL DEFAULT 0,
  [isOwner] bit NOT NULL DEFAULT 0,
  [isPrimary] bit DEFAULT 0,
  [isAuthSignatory] bit DEFAULT 0,
  [createdby] VARCHAR(50) DEFAULT NULL,
  [modifiedby] VARCHAR(50) DEFAULT NULL,
  [createdts] datetime2 NULL DEFAULT NULL,
  [lastmodifiedts] datetime2 NULL DEFAULT NULL,
  [synctimestamp] datetime2 NULL DEFAULT NULL,
  [softdeleteflag] bit NOT NULL DEFAULT 0,
  CONSTRAINT PK_contractcustomers_id PRIMARY KEY (id)
);

GO


ALTER TABLE [${dbxschemaname}].[customeraction] 
ADD [contractId] VARCHAR(45) NULL DEFAULT NULL,
 [coreCustomerId] VARCHAR(45) NULL DEFAULT NULL,
 [policyId] VARCHAR(45) NULL DEFAULT NULL,
 [limitGroupId] VARCHAR(45) NULL DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[customeraction]
DROP CONSTRAINT PK_customeraction_id;

ALTER TABLE [${dbxschemaname}].[customeraction]
ADD CONSTRAINT PK_customeraction_id PRIMARY KEY ([id], [synctimestamp]);

GO

ALTER TABLE [${dbxschemaname}].[customergroup] ADD [coreCustomerId] VARCHAR(20) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customergroup] ADD [contractId] VARCHAR(20) NULL DEFAULT NULL;

GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeraction_save_proc];
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customeraction_save_proc]  
   @_queryInput nvarchar(max)
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      SET @index = 0
	  set @_queryInput = REPLACE(@_queryInput,'\','')
	  set @_queryInput = REPLACE(@_queryInput,'"','''')

      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+[${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
		  SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitType_id,value) VALUES (') + (@recordsData) + (N')')
		  EXEC(@query)
               END
         END
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_action_limits_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_action_limits_delete_proc]  
   @_customerId nvarchar(max)
AS 
   BEGIN
    DELETE FROM [${dbxschemaname}].[customeraction] where [customeraction].[Customer_id] = @_customerId;
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_accounts_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_accounts_delete_proc]  
   @_customerId nvarchar(max)
AS 
   BEGIN
    DELETE FROM [${dbxschemaname}].[customeraccounts] where [customeraccounts].[Customer_id] = @_customerId;
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_group_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_group_delete_proc]  
   @_customerId nvarchar(max)
AS 
   BEGIN
    DELETE FROM [${dbxschemaname}].[customergroup] where [customergroup].[Customer_id] = @_customerId;
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_contract_delete_proc]  
   @_customerId nvarchar(max)
AS 
   BEGIN
    DELETE FROM [${dbxschemaname}].[contractcustomers] where [contractcustomers].[customerId] = @_customerId;
   END
GO

ALTER TABLE [${dbxschemaname}].[customergroup] DROP CONSTRAINT [customergroup$FK_CustomerGroup_Group];

GO


ALTER TABLE [${dbxschemaname}].[customeraction] 
ADD [featureId] VARCHAR(45) NULL DEFAULT NULL;

GO

ALTER TABLE [${dbxschemaname}].[customeraction]
DROP CONSTRAINT [customeraction$FK_CustomerActionLimit_customertype];

-- DROP INDEX [${dbxschemaname}].[customeraction].UNIQUE_customeractionlimit;

GO

ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [contractId] VARCHAR(50) NULL DEFAULT NULL;
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [coreCustomerId] VARCHAR(50) NULL DEFAULT NULL;
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [featureId] VARCHAR(50) NULL DEFAULT NULL;
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [policyId] VARCHAR(50) NULL DEFAULT NULL;
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [limitGroupId] VARCHAR(50) NULL DEFAULT NULL;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customrole_actionlimits_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customrole_actionlimits_create_proc]  
   @_queryInput nvarchar(max),
   @_customRoleId bigint
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      DELETE 
      FROM [${dbxschemaname}].customroleactionlimits
      WHERE customroleactionlimits.customRole_id = @_customRoleId

      SET @index = 0
	  SET @_queryInput = (SELECT REPLACE(@_queryInput,'"',''''))
      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
                  SET @query = ('INSERT INTO [${dbxschemaname}].customroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed, limitGroupId, limitType_id,value) VALUES (') + (@recordsData) + (N')')
				  EXEC(@query)
               END
         END
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_action_save_proc];
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customer_action_save_proc]  
   @_queryInput nvarchar(max)
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      SET @index = 0
	  set @_queryInput = REPLACE(@_queryInput,'\','')
	  set @_queryInput = REPLACE(@_queryInput,'"','''')

      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+[${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
		  SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value) VALUES (') + (@recordsData) + (N')')
		  EXEC(@query)
               END
         END
   END
GO

ALTER TABLE [${dbxschemaname}].[customeraction] DROP CONSTRAINT [customeraction$UNIQUE_customeractionlimit];
GO
ALTER TABLE [${dbxschemaname}].[customeraction]
DROP CONSTRAINT [customeraction$FK_CustomerActionLimit_Action];
GO
ALTER TABLE [${dbxschemaname}].[membership]
DROP CONSTRAINT [membership$taxId_UNIQUE];
GO
ALTER TABLE [${dbxschemaname}].[customeraction] ALTER COLUMN Action_id nvarchar(255);
GO  
ALTER TABLE [${dbxschemaname}].[customeraction]   
ADD CONSTRAINT UNIQUE_customeractionlimit UNIQUE (Customer_id ASC,coreCustomerId ASC, Action_id ASC, Account_id ASC,LimitType_id ASC);   
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeraction_save_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeraction_save_proc]  
   @_queryInput nvarchar(max)
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      SET @index = 0
	  set @_queryInput = REPLACE(@_queryInput,'\','')
	  set @_queryInput = REPLACE(@_queryInput,'"','''')

      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+[${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
		  SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value) VALUES (') + (@recordsData) + (N')')
		  EXEC(@query)
               END
         END
   END
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_contract_delete_proc]  
   @customerId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
		DECLARE @contract_statement nvarchar(max);
		DECLARE @accounts_statement nvarchar(max);
		DECLARE @group_statement nvarchar(max);
		DECLARE @action_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomers] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customeraccounts] where ';
		SET @group_statement = N'DELETE FROM [${dbxschemaname}].[customergroup] where ';
		SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customeraction] where ';
		SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where ';

		SET @where_clause = '';
		SET @where_clause1 = '';
		if(@customerId != '') 
		BEGIN
			SET @where_clause = @where_clause + (N' and customerId = ') + ((QUOTENAME((@customerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and Customer_id = ') + ((QUOTENAME((@customerId), '''')))
		END
		
		IF(@contractId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
		END
		
		IF(@coreCustomerId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
		END
		
		IF(@where_clause != '')
		BEGIN	
			SET @contract_statement = @contract_statement + @where_clause;	
			SET @accounts_statement = @accounts_statement + @where_clause1;	
			SET @group_statement = @group_statement + @where_clause1;	
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause1;	
		
			exec(@contract_statement);
			exec(@accounts_statement);
			exec(@group_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO




DROP TABLE IF EXISTS [${dbxschemaname}].[contractcustomrole];

CREATE TABLE [${dbxschemaname}].[contractcustomrole] (
  [id] VARCHAR(50) NOT NULL,
  [contractId] VARCHAR(50) NULL DEFAULT NULL,
  [customerId] VARCHAR(50) NULL DEFAULT NULL,
  [coreCustomerId] VARCHAR(45) NULL DEFAULT NULL,
  [customRoleId] VARCHAR(50) NULL DEFAULT NULL,
  [roleId] VARCHAR(50) NULL DEFAULT NULL,
  PRIMARY KEY (id)
);

GO


DROP TABLE IF EXISTS [${dbxschemaname}].[customroleaccounts];

GO

CREATE TABLE [${dbxschemaname}].[customroleaccounts] (
  [id] VARCHAR(50) NOT NULL,
  [customRoleId] VARCHAR(50)NULL DEFAULT NULL,
  [Account_id] varchar(50) NULL DEFAULT NULL,
  [AccountName] varchar(50) NULL DEFAULT NULL,
  [contractId] VARCHAR(50) NULL DEFAULT NULL,
  [coreCustomerId] VARCHAR(45)NULL DEFAULT NULL,
  [createdby] VARCHAR(50) DEFAULT NULL,
  [modifiedby] VARCHAR(50) DEFAULT NULL,
  [createdts] datetime2 NULL DEFAULT NULL,
  [lastmodifiedts] datetime2 NULL DEFAULT NULL,
  [synctimestamp] datetime2 NULL DEFAULT NULL,
  [softdeleteflag] bit NOT NULL DEFAULT 0,
  PRIMARY KEY (id)
);

GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_search_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50),
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer ' + (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
                        ELSE N''
                     END)
            
                  SET @queryStatement = (@queryStatement) + (N' WHERE 1=1 ')
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN address ON (customeraddress.Address_id = address.id)
					LEFT JOIN city ON (city.id = address.City_id)
					LEFT JOIN country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory  ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                        
                          
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              
                                 SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
        exec(@queryStatement)
     END

	GO

ALTER TABLE [${dbxschemaname}].[customer] ADD [contractId] SMALLINT NULL DEFAULT 1;

GO

ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [email] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [EStatementmentEnable] SMALLINT NULL DEFAULT 0;
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [accountType] VARCHAR(50) NULL;

GO
	
ALTER TABLE [${dbxschemaname}].[membership] ADD [firstName] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [lastName] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [dateOfBirth] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [ssn] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [faxId] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [industry] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD [status] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[membership] ADD CONSTRAINT UNIQUE_membership_id UNIQUE ( [id] );

DROP TABLE IF EXISTS [${dbxschemaname}].[membershiprelation];
CREATE TABLE [${dbxschemaname}].[membershiprelation] (
   [id] varchar(50) NOT NULL,
   [membershipId] varchar(50) NOT NULL,
   [relatedMebershipId] varchar(50) NOT NULL,
   [relationshipId] varchar(50) DEFAULT NULL,
   [modifiedby] varchar(50) DEFAULT NULL,
   [relationshipName] varchar(50) NOT NULL,
   CONSTRAINT PK_membershiprelation_id PRIMARY KEY ([id])
 );
 
 DROP PROCEDURE IF EXISTS [${dbxschemaname}].[membership_customer_search_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[membership_customer_search_proc](
	@_id nvarchar(50),
	@_name nvarchar(50),
	@_email nvarchar(50),
	@_phone nvarchar(50),
	@_dateOfBirth nvarchar(50),
	@_status nvarchar(50),
	@_city  nvarchar(50),
	@_country nvarchar(50),
	@_zipCode nvarchar(50),
	@_taxId nvarchar(50)
)
AS 
	BEGIN
	DECLARE @select_statement NVARCHAR(MAX);
	DECLARE @address_select_statement NVARCHAR(MAX);
	SET @select_statement = ('select [${dbxschemaname}].[membership].[id] , [${dbxschemaname}].[membership].[isBusinessType] , [${dbxschemaname}].[membership].[name], 
		[${dbxschemaname}].[membership].[industry] , [${dbxschemaname}].[membership].[firstName] , [${dbxschemaname}].[membership].[lastName] , 
		[${dbxschemaname}].[membership].[phone] , [${dbxschemaname}].[membership].[taxId], [${dbxschemaname}].[membership].[faxId], 
		[${dbxschemaname}].[membership].[email] , [${dbxschemaname}].[address].[addressLine1],
		[${dbxschemaname}].[address].[addressLine2] , [${dbxschemaname}].[address].[cityName] , [${dbxschemaname}].[address].[country] , 
		[${dbxschemaname}].[address].[zipCode] , [${dbxschemaname}].[address].[state]
		from [${dbxschemaname}].[membership] LEFT JOIN address on ([${dbxschemaname}].[membership].[addressId] = [${dbxschemaname}].[address].[id])
		where [${dbxschemaname}].[membership].[id] is not null');
	IF(@_id != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[id] = ',@_id);
    END;
    IF(@_name != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[name] LIKE %',@_name,'%');
    END;
    IF(@_email != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[email] = ',@_email);
    END;
    IF(@_phone != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[phone] = ',@_phone);
    END;
    IF(@_dateOfBirth != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[dateOfBirth] = ',@_dateOfBirth);
    END;	
	SET @address_select_statement = '';
	IF(@_city != '' OR @_country!='' OR @_zipCode!= '') BEGIN
		SET @address_select_statement = 'select address.id from address where address.id is not null';
        IF(@_city != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[cityName] = ',@_city);
        END;
        IF(@_country != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[country] = ',@_country);
        END;
        IF(@_zipCode != '') BEGIN
			SET @address_select_statement = concat(@address_select_statement , ' and [${dbxschemaname}].[address].[zipCode] = ',@_zipCode);
        END;
    END;
	IF(@address_select_statement != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[addressId] in (", @address_select_statement , ")');
    END;
    exec(@select_statement);
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[membership_relative_customer_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[membership_relative_customer_get_proc](
	@_id NVARCHAR(50)
)
AS BEGIN
    select  [${dbxschemaname}].[membership].[id] , 
			[${dbxschemaname}].[membership].[name] , 
            [${dbxschemaname}].[membership].[firstName] , 
            [${dbxschemaname}].[membership].[lastName] , 
            [${dbxschemaname}].[membership].[phone] , 
            [${dbxschemaname}].[membership].[email] ,
            [${dbxschemaname}].[membership].[dateOfBirth], 
			[${dbxschemaname}].[membership].[taxId],
			[${dbxschemaname}].[membership].[faxId],
            [${dbxschemaname}].[membership].[industry], 
			[${dbxschemaname}].[membership].[isBusinessType],
            [${dbxschemaname}].[membershiprelation].[relationshipId],
            [${dbxschemaname}].[membershiprelation].[relationshipName],
            [${dbxschemaname}].[address].[addressLine1] ,
			[${dbxschemaname}].[address].[addressLine2] , 
            [${dbxschemaname}].[address].[cityName] , 
            [${dbxschemaname}].[address].[country] , 
            [${dbxschemaname}].[address].[zipCode] , 
            [${dbxschemaname}].[address].[state]
			from 
            [${dbxschemaname}].[membership]
			LEFT JOIN [${dbxschemaname}].[address] on ([${dbxschemaname}].[membership].[addressId] = [${dbxschemaname}].[address].[id])
            JOIN [${dbxschemaname}].[membershiprelation] on ([${dbxschemaname}].[membershiprelation].[relatedMebershipId] = [${dbxschemaname}].[membership].[id])
			where [${dbxschemaname}].[membership].[id] in (select [${dbxschemaname}].[membershiprelation].[relatedMebershipId] from [${dbxschemaname}].[membershiprelation] where [${dbxschemaname}].[membershiprelation].[membershipId] = @_id)
            and [${dbxschemaname}].[membershiprelation].[membershipId] = @_id;
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_search_proc];
GO
CREATE  PROCEDURE [${dbxschemaname}].[contract_search_proc](
	@_contractId nvarchar(50),
	@_contractName nvarchar(50),
	@_coreCustomerId nvarchar(50),
	@_coreCustomerName nvarchar(50),
	@_email nvarchar(50),
	@_phoneCountryCode nvarchar(50),
	@_phoneNumber nvarchar(50),
	@_country nvarchar(50),
	@_serviceDefinitionId nvarchar(50)										
) AS BEGIN
	DECLARE @isWhereAppened BIT;
	DECLARE @shouldAndAppend BIT;
	DECLARE @select_statement NVARCHAR(MAX);
	SET @isWhereAppened = 0;
    SET @shouldAndAppend = 0;
	SET @select_statement = 'SELECT [contract].[id] as contractId , [contract].[name] as contractName , [contract].[servicedefinitionId] as serviceDefinitionId , 
										   [servicedefinition].[name] as serviceDefinitionName,
										   [emailcommunication].[value] as email , [contractcorecustomers].[coreCustomerName] as coreCustomerName 
									FROM [${dbxschemaname}].[contract]
                                    LEFT JOIN [${dbxschemaname}].[servicedefinition] ON ( [servicedefinition].[id] = [contract].[servicedefinitionId])
                                    LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ([contractcorecustomers].[contractId] = [contract].[id])
                                    LEFT JOIN [${dbxschemaname}].[contractcommunication] emailcommunication ON ([emailcommunication].[contractId] = [contract].[id] and [emailcommunication].[typeId] = ''COMM_TYPE_EMAIL'')
                                    LEFT JOIN [${dbxschemaname}].[contractcommunication] phonecommunication ON ([phonecommunication].[contractId] = [contract].[id] and [phonecommunication].[typeId] = ''COMM_TYPE_PHONE'')
                                    LEFT JOIN [${dbxschemaname}].[contractaddress] ON ([contractaddress].[contractId] = [contract].[id])
                                    LEFT JOIN [${dbxschemaname}].[address] ON ([address].[id] = [contractaddress].[addressId])';
    IF(@_contractId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' where contract.id = ', '''' , @_contractId , '''');
        SET @isWhereAppened = 1;
        SET @shouldAndAppend = 1;
    END;
    IF(@_contractName != '') BEGIN
		IF(@isWhereAppened = 0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened = 1 and @shouldAndAppend = 1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [contract].[name] like ', ''''  , '%',@_contractName,'%' , '''' );
        SET @shouldAndAppend = 1;
    END;
    IF(@_serviceDefinitionId != '') BEGIN
		IF(@isWhereAppened = 0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened = 1 and @shouldAndAppend = 1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [contract].[servicedefinitionId] = ','''' + @_serviceDefinitionId + '''' );
        SET @shouldAndAppend = 1;
    END;
    IF(@_coreCustomerId != '') BEGIN
		IF(@isWhereAppened =  0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [contractcorecustomers].[coreCustomerId] =', ''''  + @_coreCustomerId + '''' );
        SET @shouldAndAppend = 1;
    END;
    
    IF(@_coreCustomerName != '') BEGIN
		IF(@isWhereAppened =  0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [contractcorecustomers].[coreCustomerName] like ', '''' ,  '%',@_coreCustomerName, '%' , '''');
        SET @shouldAndAppend = 1;
    END;
    
    IF(@_country != '') BEGIN
		IF(@isWhereAppened =  0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [address].[country] = ', ''''  + @_country + '''' );
        SET @shouldAndAppend = 1;
    END;
    
    IF(@_email != '') BEGIN
		IF(@isWhereAppened =  0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [emailcommunication].[value] = ', ''''  + @_email + '''' );
        SET @shouldAndAppend = 1;
    END;
    
    IF(@_phoneCountryCode != '' and @_phoneNumber!= '') BEGIN
		IF(@isWhereAppened =  0) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 1;
		END;
        IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
		END;
        SET @select_statement = CONCAT(@select_statement , ' [phonecommunication].[value] = ', '''' + @_phoneNumber + '''' ,' and [phonecommunication].[phoneCountryCode] = ', '''' +@_phoneCountryCode+'''' );
        SET @shouldAndAppend = 1;
    END;
    SET @select_statement = CONCAT(@select_statement , ';');
    exec(@select_statement);
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[user_customers_view];
GO
CREATE VIEW [${dbxschemaname}].[user_customers_view] AS
    SELECT 
        [contractcustomers].[customerId] AS customerId,
        [contractcustomers].[coreCustomerId] AS coreCustomerId,
        [contractcustomers].[contractId] AS contractId,
		[contract].[name] AS contractName,
        [contractcustomers].[isPrimary] AS isPrimary,
        [contractcorecustomers].[coreCustomerName] AS coreCustomerName,
        [contractcorecustomers].[isBusiness] AS isBusiness,
        [contract].[servicedefinitionId] AS serviceDefinitionId,
        [servicedefinition].[name] AS serviceDefinitionName,
		[membergrouptype].[description] AS serviceDefinitionType,
		[membergroup].[id] AS roleId,
        [membergroup].[Name] AS userRole
    FROM
        ((([${dbxschemaname}].[contractcustomers]
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ((([contractcorecustomers].[contractId] = [contractcustomers].[contractId])
            AND ([contractcorecustomers].[coreCustomerId] = [contractcustomers].[coreCustomerId]))))
        LEFT JOIN [${dbxschemaname}].[contract] ON (([contract].[id] = [contractcorecustomers].[contractId])))
        LEFT JOIN [${dbxschemaname}].[servicedefinition] ON (([servicedefinition].[id] = [contract].[servicedefinitionId]))
		LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([membergrouptype].[id] = [servicedefinition].[serviceType]))
		LEFT JOIN [${dbxschemaname}].[customergroup] ON (([customergroup].[Customer_id] = [contractcustomers].[customerId] AND [customergroup].[contractId] = [contractcustomers].[contractId]
        AND [customergroup].[coreCustomerId] = [contractcustomers].[coreCustomerId]))
        LEFT JOIN [${dbxschemaname}].[membergroup] ON ([membergroup].[id] = [customergroup].[Group_id]));
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_associated_contractaccounts_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[get_associated_contractaccounts_proc](
	@_accountIdList NVARCHAR(50)
) AS BEGIN

    DECLARE @accountIdList NVARCHAR(50);
	SET @accountIdList = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].FIND_IN_SET([contractaccounts].[accountId],@_accountIdList) > 0);
	select @accountIdList As accountIdList;
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[infinityuser_contractdetails_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[infinityuser_contractdetails_get_proc](
	@_id nvarchar(50)
)AS BEGIN
	select
		[contract].[id] AS contractId,
        [contract].[name] AS contractName,
        [contract].[servicedefinitionId] AS servicedefinitionId,
        [servicedefinition].[name] AS serviceDefinitionName,
        [membergrouptype].[description] AS serviceDefinitionType,
        [contractcorecustomers].[coreCustomerId] AS coreCustomerId,
        [customergroup].[Group_id] AS userRole,
        [membergroup].[name] AS userRoleName,
        'true' AS isAssociated
	FROM
		[${dbxschemaname}].[contract]
        LEFT JOIN [${dbxschemaname}].[servicedefinition] ON ([servicedefinition].[id] = [contract].[servicedefinitionId])
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON ([membergrouptype].[id] = [servicedefinition].[serviceType])
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ([contractcorecustomers].[contractId] = [contract].[id])
        LEFT JOIN [${dbxschemaname}].[customergroup] ON ([customergroup].[contractId] = [contract].[id] AND [customergroup].[coreCustomerId] = [contractcorecustomers].[coreCustomerId])
        LEFT JOIN [${dbxschemaname}].[membergroup] ON ([membergroup].[id] = [customergroup].[Group_id])
        LEFT JOIN [${dbxschemaname}].[contractcustomers] ON ([contractcustomers].[contractId] = [contractcorecustomers].[contractId])
	WHERE
		[customergroup].[Customer_id] = @_id
        AND [contractcustomers].[customerId] = @_id
	UNION
    select
		[contract].[id] AS contractId,
        [contract].[name] AS contractName,
        [contract].[servicedefinitionId] AS servicedefinitionId,
        [servicedefinition].[name] AS serviceDefinitionName,
        [membergrouptype].[description] AS serviceDefinitionType,
        [contractcorecustomers].[coreCustomerId] AS coreCustomerId,
        NULL AS userRole,
        NULL AS userRoleName,
        'false' AS isAssociated
	FROM
		[${dbxschemaname}].[contract]
        LEFT JOIN [${dbxschemaname}].[servicedefinition] ON ([servicedefinition].[id] = [contract].[servicedefinitionId])
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON ([membergrouptype].[id] = [servicedefinition].[serviceType])
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ([contractcorecustomers].[contractId] = [contract].[id])
	WHERE
		[contractcorecustomers].[contractId] IN ( select [contractcustomers].[contractId] FROM [contractcustomers] WHERE [contractcustomers].[customerId ]= @_id);
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].fetch_restrictive_featureactionlimits_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].fetch_restrictive_featureactionlimits_proc(
	@_locale nvarchar(50),
    @_userId nvarchar(50),
    @_serviceDefinitionId nvarchar(50),
    @_roleId nvarchar(50),
    @_coreCustomerId nvarchar(50)
)
AS
BEGIN
	DECLARE @select_statement NVARCHAR(max);
	DECLARE @action_select_statement NVARCHAR(max);
	SET @select_statement = '';
    SET @action_select_statement = '';
	SET @action_select_statement = '(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id ))';
	IF(@_serviceDefinitionId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = ' , @_serviceDefinitionId);
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;	
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT('(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ',@_roleId);
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Action_id IN ');
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ',@_coreCustomerId);
        SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
	IF(@_userId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ',@_userId);
		IF(@_coreCustomerId != '') BEGIN 
			SET @select_statement = CONCAT(@select_statement , ' AND customeraction.coreCustomerId = ' ,@_coreCustomerId);
        END ;
        SET @select_statement = CONCAT(@select_statement , ' AND customeraction.isAllowed = ''1'' AND customeraction.Action_id IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;

    SET @select_statement = '( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue';
    if(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', null AS serviceLimitValue');
    END ;
	IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS groupLimitValue');
    END ;  
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS coreCustomerLimitValue');
    END ;
    SET @select_statement = CONCAT(@select_statement , 'FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id');
	IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , 'LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)');
    END ;
	SET @select_statement = CONCAT(@select_statement , 'WHERE featureaction.Type_id = ''NON_MONETARY'' AND featureaction.id IN ' , @action_select_statement , ')');
    
    SET @select_statement = CONCAT(@select_statement , ' UNION (SELECT 
								feature.id AS featureId,
                                featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue');
	IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', servicedefinitionactionlimit.value AS serviceLimitValue');
    END ;
    IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', groupactionlimit.value AS groupLimitValue');
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', contractactionlimit.value AS coreCustomerLimitValue');
    END ;
	SET @select_statement = CONCAT(@select_statement , 'FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)');
                                                                                                        
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)');
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)');
	END ;
	SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.Type_id = ''MONETARY''');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.serviceDefinitionId = ' ,@_serviceDefinitionId);
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Group_id = ' , @_roleId);
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.coreCustomerId = ' , @_coreCustomerId);
	END ;
    SET @select_statement = CONCAT(@select_statement , ' AND featureaction.id IN ' , @action_select_statement , ')'); 
   exec(@select_statement);
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].customeraccounts_corecustomerinfo_view;
GO
CREATE VIEW [${dbxschemaname}].[customeraccounts_corecustomerinfo_view] AS
SELECT
	[customeraccounts].[Customer_id] AS User_id,
	[customeraccounts].[Account_id] AS Account_id,
    [customeraccounts].[FavouriteStatus] AS FavouriteStatus,
    [contractcorecustomers].[coreCustomerId] AS Membership_id,
    [contractcorecustomers].[coreCustomerName] AS MembershipName,
	[contractcorecustomers].[isBusiness] AS isBusiness
FROM 
	[${dbxschemaname}].customeraccounts
    LEFT JOIN [${dbxschemaname}].contractcorecustomers ON ( contractcorecustomers.coreCustomerId = customeraccounts.coreCustomerId);
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[allaccountsview];
GO
CREATE VIEW [${dbxschemaname}].[allaccountsview] AS
    SELECT DISTINCT
		[accounts].[AccountHolder] AS AccountHolder,
        [accounts].[Account_id] AS Account_id,
        [accounts].[AccountName] AS AccountName,
        [accounts].[Type_id] AS Type_id,
        [accounts].[isBusinessAccount] AS IsOrganizationAccount,
        [accounts].[ownership] AS ownership,
        [accounts].[StatusDesc] AS accountStatus,
        [accounttype].[TypeDescription] AS accountType,
        [membershipaccounts].[membershipId] AS Membership_id,
        [membership].[taxId] AS Taxid,
        [membership].[phone] AS phoneNumber,
        [membership].[email] AS emailId,
        [membership].[name] AS name
    FROM
        ((([${dbxschemaname}].[accounts]
        LEFT JOIN [${dbxschemaname}].[membershipaccounts] ON (([accounts].[Account_id] = [membershipaccounts].[accountId])))
        LEFT JOIN [${dbxschemaname}].[accounttype] ON (([accounts].[Type_id] = [accounttype].[TypeID])))
        LEFT JOIN [${dbxschemaname}].[membership] ON (([membership].[id] = [membershipaccounts].[membershipId])));
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.mfaservice', 'securityQuestions';
GO

ALTER TABLE [${dbxschemaname}].[mfaservice] ALTER COLUMN [securityQuestions] nvarchar(MAX) NULL;

GO 

DROP VIEW IF EXISTS  [${dbxschemaname}].[billview];
GO
CREATE VIEW [${dbxschemaname}].[billview] (
   [balanceAmount], 
   [billDueDate], 
   [billGeneratedDate], 
   [description], 
   [dueAmount], 
   [ebillURL], 
   [id], 
   [paidAmount], 
   [paidDate], 
   [payeeId], 
   [currencyCode], 
   [fromAccountName], 
   [fromAccountNumber], 
   [User_id], 
   [payeeName], 
   [softDelete], 
   [ebillStatus], 
   [payeeNickName], 
   [payeeAddressLine1], 
   [billerCategory], 
   [billerCategoryId], 
   [ebillSupport])
AS 
   SELECT 
      bb.balanceAmount AS balanceAmount, 
      bb.billDueDate AS billDueDate, 
      bb.billGeneratedDate AS billGeneratedDate, 
      bb.description AS description, 
      bb.dueAmount AS dueAmount, 
      bb.ebillURL AS ebillURL, 
      bb.id AS id, 
      bb.paidAmount AS paidAmount, 
      bb.paidDate AS paidDate, 
      bb.Payee_id AS payeeId, 
      bb.currencyCode AS currencyCode, 
      ac.AccountName AS fromAccountName, 
      ac.Account_id AS fromAccountNumber, 
      ac.Customer_id AS User_id, 
      py.name AS payeeName, 
      py.softDelete AS softDelete, 
      py.eBillEnable AS ebillStatus, 
      py.nickName AS payeeNickName, 
      py.addressLine1 AS payeeAddressLine1, 
      bc.categoryName AS billerCategory, 
      bm.billerCategoryId AS billerCategoryId, 
      bm.ebillSupport AS ebillSupport
   FROM (((([${dbxschemaname}].bill  AS bb 
      CROSS JOIN [${dbxschemaname}].payee  AS py) 
      CROSS JOIN [${dbxschemaname}].customeraccounts  AS ac) 
      CROSS JOIN [${dbxschemaname}].billercategory  AS bc) 
      CROSS JOIN [${dbxschemaname}].billermaster  AS bm)
   WHERE 
      ((bb.Payee_id = py.Id) AND
      (bb.billerMaster_id = bm.id) AND 
      (bb.Account_id = ac.Account_id) AND 
      (bm.billerCategoryId = bc.id))
GO





INSERT INTO [${dbxschemaname}].[statustype] ([id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('STID_CONTRACTSTATUS', 'Contract status', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');

INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SID_CONTRACT_ACTIVE', 'STID_CONTRACTSTATUS', 'contract is active for use', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SID_CONTRACT_PENDING', 'STID_CONTRACTSTATUS', 'contract is pending for approval', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SID_CONTRACT_REJECTED', 'STID_CONTRACTSTATUS', 'contract is rejected', 'Kony User', 'Kony Dev', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '2020-09-09 16:13:26', '0');
GO

ALTER TABLE [${dbxschemaname}].customergroup DROP PK_customergroup_Customer_id;
GO



SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


DROP VIEW IF EXISTS  [${dbxschemaname}].[customeraccountsview];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].customeraccountsview (
   [Membership_id], 
   [MembershipName], 
   [Taxid], 
   [Customer_id], 
   [User_id], 
   [Account_id], 
   [isBusinessAccount], 
   [Type_id], 
   [userName], 
   [currencyCode], 
   [accountHolder], 
   [error], 
   [Address], 
   [Scheme], 
   [number], 
   [availableBalance], 
   [currentBalance], 
   [interestRate], 
   [availableCredit], 
   [minimumDue], 
   [dueDate], 
   [firstPaymentDate], 
   [closingDate], 
   [paymentTerm], 
   [openingDate], 
   [maturityDate], 
   [dividendLastPaidAmount], 
   [dividendLastPaidDate], 
   [dividendPaidYTD], 
   [dividendRate], 
   [dividendYTD], 
   [eStatementEnable], 
   [isOrganizationAccount], 
   [favouriteStatus], 
   [statusDesc], 
   [nickName], 
   [originalAmount], 
   [outstandingBalance], 
   [paymentDue], 
   [paymentMethod], 
   [swiftCode], 
   [totalCreditMonths], 
   [totalDebitsMonth], 
   [routingNumber], 
   [supportBillPay], 
   [supportCardlessCash], 
   [supportTransferFrom], 
   [supportTransferTo], 
   [supportDeposit], 
   [unpaidInterest], 
   [previousYearsDividends], 
   [principalBalance], 
   [principalValue], 
   [regularPaymentAmount], 
   [phoneId], 
   [lastDividendPaidDate], 
   [lastDividendPaidAmount], 
   [lastPaymentAmount], 
   [lastPaymentDate], 
   [lastStatementBalance], 
   [lateFeesDue], 
   [maturityAmount], 
   [maturityOption], 
   [payoffAmount], 
   [payOffCharge], 
   [pendingDeposit], 
   [pendingWithdrawal], 
   [jointHolders], 
   [isPFM], 
   [interestPaidYTD], 
   [interestPaidPreviousYTD], 
   [interestPaidLastYear], 
   [interestEarned], 
   [currentAmountDue], 
   [creditLimit], 
   [creditCardNumber], 
   [bsbNum], 
   [bondInterestLastYear], 
   [bondInterest], 
   [availablePoints], 
   [accountName], 
   [email], 
   [IBAN], 
   [adminProductId], 
   [UpdatedBy], 
   [LastUpdated], 
   [ActualUpdatedBY], 
   [bankname], 
   [accountPreference], 
   [transactionLimit], 
   [transferLimit], 
   [rates], 
   [termsAndConditions], 
   [typeDescription], 
   [supportChecks], 
   [displayName], 
   [accountSubType], 
   [description], 
   [schemeName], 
   [identification], 
   [secondaryIdentification], 
   [servicerSchemeName], 
   [servicerIdentification], 
   [dataCreditDebitIndicator], 
   [dataType], 
   [dataDateTime], 
   [dataCreditLineIncluded], 
   [dataCreditLineType], 
   [dataCreditLineAmount], 
   [dataCreditLineCurrency])
AS 
   SELECT DISTINCT 
      [${dbxschemaname}].contractcorecustomers.coreCustomerId AS Membership_id, 
      [${dbxschemaname}].contractcorecustomers.coreCustomerName AS MembershipName, 
      [${dbxschemaname}].accounts.TaxId AS Taxid, 
      [${dbxschemaname}].customeraccounts.Customer_id AS Customer_id, 
      [${dbxschemaname}].customeraccounts.Customer_id AS User_id, 
      [${dbxschemaname}].accounts.Account_id AS Account_id, 
      [${dbxschemaname}].contractcorecustomers.isBusiness AS isBusinessAccount, 
      [${dbxschemaname}].accounts.Type_id AS Type_id, 
      [${dbxschemaname}].accounts.UserName AS userName, 
      [${dbxschemaname}].accounts.CurrencyCode AS currencyCode, 
      [${dbxschemaname}].accounts.AccountHolder AS accountHolder, 
      [${dbxschemaname}].accounts.error AS error, 
      [${dbxschemaname}].accounts.Address AS Address, 
      [${dbxschemaname}].accounts.Scheme AS Scheme, 
      [${dbxschemaname}].accounts.Number AS number, 
      [${dbxschemaname}].accounts.AvailableBalance AS availableBalance, 
      [${dbxschemaname}].accounts.CurrentBalance AS currentBalance, 
      [${dbxschemaname}].accounts.InterestRate AS interestRate, 
      [${dbxschemaname}].accounts.AvailableCredit AS availableCredit, 
      [${dbxschemaname}].accounts.MinimumDue AS minimumDue, 
      [${dbxschemaname}].accounts.DueDate AS dueDate, 
      [${dbxschemaname}].accounts.FirstPaymentDate AS firstPaymentDate, 
      [${dbxschemaname}].accounts.ClosingDate AS closingDate, 
      [${dbxschemaname}].accounts.PaymentTerm AS paymentTerm, 
      [${dbxschemaname}].accounts.OpeningDate AS openingDate, 
      [${dbxschemaname}].accounts.MaturityDate AS maturityDate, 
      [${dbxschemaname}].accounts.DividendLastPaidAmount AS dividendLastPaidAmount, 
      [${dbxschemaname}].accounts.DividendLastPaidDate AS dividendLastPaidDate, 
      [${dbxschemaname}].accounts.DividendPaidYTD AS dividendPaidYTD, 
      [${dbxschemaname}].accounts.DividendRate AS dividendRate, 
      [${dbxschemaname}].accounts.DividendYTD AS dividendYTD, 
      [${dbxschemaname}].customeraccounts.EStatementmentEnable AS eStatementEnable, 
      [${dbxschemaname}].customeraccounts.IsOrganizationAccount AS isOrganizationAccount, 
      [${dbxschemaname}].customeraccounts.FavouriteStatus AS favouriteStatus, 
      [${dbxschemaname}].accounts.StatusDesc AS statusDesc, 
      [${dbxschemaname}].accounts.NickName AS nickName, 
      [${dbxschemaname}].accounts.OriginalAmount AS originalAmount, 
      [${dbxschemaname}].accounts.OutstandingBalance AS outstandingBalance, 
      [${dbxschemaname}].accounts.PaymentDue AS paymentDue, 
      [${dbxschemaname}].accounts.PaymentMethod AS paymentMethod, 
      [${dbxschemaname}].accounts.SwiftCode AS swiftCode, 
      [${dbxschemaname}].accounts.TotalCreditMonths AS totalCreditMonths, 
      [${dbxschemaname}].accounts.TotalDebitsMonth AS totalDebitsMonth, 
      [${dbxschemaname}].accounts.RoutingNumber AS routingNumber, 
      [${dbxschemaname}].accounts.SupportBillPay AS supportBillPay, 
      [${dbxschemaname}].accounts.SupportCardlessCash AS supportCardlessCash, 
      [${dbxschemaname}].accounts.SupportTransferFrom AS supportTransferFrom, 
      [${dbxschemaname}].accounts.SupportTransferTo AS supportTransferTo, 
      [${dbxschemaname}].accounts.SupportDeposit AS supportDeposit, 
      [${dbxschemaname}].accounts.UnpaidInterest AS unpaidInterest, 
      [${dbxschemaname}].accounts.PreviousYearsDividends AS previousYearsDividends, 
      [${dbxschemaname}].accounts.principalBalance AS principalBalance, 
      [${dbxschemaname}].accounts.PrincipalValue AS principalValue, 
      [${dbxschemaname}].accounts.RegularPaymentAmount AS regularPaymentAmount, 
      [${dbxschemaname}].accounts.phone AS phoneId, 
      [${dbxschemaname}].accounts.LastDividendPaidDate AS lastDividendPaidDate, 
      [${dbxschemaname}].accounts.LastDividendPaidAmount AS lastDividendPaidAmount, 
      [${dbxschemaname}].accounts.LastPaymentAmount AS lastPaymentAmount, 
      [${dbxschemaname}].accounts.LastPaymentDate AS lastPaymentDate, 
      [${dbxschemaname}].accounts.LastStatementBalance AS lastStatementBalance, 
      [${dbxschemaname}].accounts.LateFeesDue AS lateFeesDue, 
      [${dbxschemaname}].accounts.maturityAmount AS maturityAmount, 
      [${dbxschemaname}].accounts.MaturityOption AS maturityOption, 
      [${dbxschemaname}].accounts.payoffAmount AS payoffAmount, 
      [${dbxschemaname}].accounts.PayOffCharge AS payOffCharge, 
      [${dbxschemaname}].accounts.PendingDeposit AS pendingDeposit, 
      [${dbxschemaname}].accounts.PendingWithdrawal AS pendingWithdrawal, 
      [${dbxschemaname}].accounts.JointHolders AS jointHolders, 
      [${dbxschemaname}].accounts.IsPFM AS isPFM, 
      [${dbxschemaname}].accounts.InterestPaidYTD AS interestPaidYTD, 
      [${dbxschemaname}].accounts.InterestPaidPreviousYTD AS interestPaidPreviousYTD, 
      [${dbxschemaname}].accounts.InterestPaidLastYear AS interestPaidLastYear, 
      [${dbxschemaname}].accounts.InterestEarned AS interestEarned, 
      [${dbxschemaname}].accounts.CurrentAmountDue AS currentAmountDue, 
      [${dbxschemaname}].accounts.CreditLimit AS creditLimit, 
      [${dbxschemaname}].accounts.CreditCardNumber AS creditCardNumber, 
      [${dbxschemaname}].accounts.BsbNum AS bsbNum, 
      [${dbxschemaname}].accounts.BondInterestLastYear AS bondInterestLastYear, 
      [${dbxschemaname}].accounts.BondInterest AS bondInterest, 
      [${dbxschemaname}].accounts.AvailablePoints AS availablePoints, 
      [${dbxschemaname}].accounts.AccountName AS accountName, 
      [${dbxschemaname}].customeraccounts.email AS email, 
      [${dbxschemaname}].accounts.IBAN AS IBAN, 
      [${dbxschemaname}].accounts.adminProductId AS adminProductId, 
      [${dbxschemaname}].accounts.UpdatedBy AS UpdatedBy, 
      [${dbxschemaname}].accounts.LastUpdated AS LastUpdated, 
      [${dbxschemaname}].accounts.ActualUpdatedBY AS ActualUpdatedBY, 
      [${dbxschemaname}].bank.Description AS bankname, 
      [${dbxschemaname}].accounts.AccountPreference AS accountPreference, 
      [${dbxschemaname}].accounttype.transactionLimit AS transactionLimit, 
      [${dbxschemaname}].accounttype.transferLimit AS transferLimit, 
      [${dbxschemaname}].accounttype.rates AS rates, 
      [${dbxschemaname}].accounttype.termsAndConditions AS termsAndConditions, 
      [${dbxschemaname}].accounttype.TypeDescription AS typeDescription, 
      [${dbxschemaname}].accounttype.supportChecks AS supportChecks, 
      [${dbxschemaname}].accounttype.displayName AS displayName, 
      [${dbxschemaname}].accounts.accountSubType AS accountSubType, 
      [${dbxschemaname}].accounts.description AS description, 
      [${dbxschemaname}].accounts.schemeName AS schemeName, 
      [${dbxschemaname}].accounts.identification AS identification, 
      [${dbxschemaname}].accounts.secondaryIdentification AS secondaryIdentification, 
      [${dbxschemaname}].accounts.servicerSchemeName AS servicerSchemeName, 
      [${dbxschemaname}].accounts.servicerIdentification AS servicerIdentification, 
      [${dbxschemaname}].accounts.dataCreditDebitIndicator AS dataCreditDebitIndicator, 
      [${dbxschemaname}].accounts.dataType AS dataType, 
      [${dbxschemaname}].accounts.dataDateTime AS dataDateTime, 
      [${dbxschemaname}].accounts.dataCreditLineIncluded AS dataCreditLineIncluded, 
      [${dbxschemaname}].accounts.dataCreditLineType AS dataCreditLineType, 
      [${dbxschemaname}].accounts.dataCreditLineAmount AS dataCreditLineAmount, 
      [${dbxschemaname}].accounts.dataCreditLineCurrency AS dataCreditLineCurrency
   FROM (((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].customeraccounts 
      ON (([${dbxschemaname}].accounts.Account_id = [${dbxschemaname}].customeraccounts.Account_id ))) 
      INNER JOIN [${dbxschemaname}].accounttype 
      ON (([${dbxschemaname}].accounts.Type_id = [${dbxschemaname}].accounttype.TypeID ))) 
      LEFT JOIN [${dbxschemaname}].contractcorecustomers 
      ON (([${dbxschemaname}].customeraccounts.coreCustomerId = [${dbxschemaname}].contractcorecustomers.coreCustomerId))) 
      LEFT JOIN [${dbxschemaname}].bank 
      ON (([${dbxschemaname}].accounts.Bank_id = [${dbxschemaname}].bank.id )))

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[user_customers_proc]
	@_customerId nvarchar(50),
	@_coreCustomerId nvarchar(50)
AS BEGIN
	declare @select_statement nvarchar(100);
	declare @isWhereAppened nvarchar(100);
	declare @shouldAndAppend nvarchar(100);
	SET @select_statement = ('(SELECT 
        contractcustomers.customerId AS customerId,
        contractcustomers.coreCustomerId AS coreCustomerId,
        contractcustomers.contractId AS contractId,
        contract.name AS contractName,
        contractcustomers.isPrimary AS isPrimary,
        contractcorecustomers.coreCustomerName AS coreCustomerName,
        contractcorecustomers.isBusiness AS isBusiness,
        contract.servicedefinitionId AS serviceDefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        membergroup.id AS roleId,
        membergroup.Name AS userRole
    FROM
        (((((([${dbxschemaname}].contractcustomers
        LEFT JOIN [${dbxschemaname}].contractcorecustomers ON (((contractcorecustomers.contractId = contractcustomers.contractId)
            AND (contractcorecustomers.coreCustomerId = contractcustomers.coreCustomerId))))
        LEFT JOIN [${dbxschemaname}].contract ON ((contract.id = contractcorecustomers.contractId)))
        LEFT JOIN [${dbxschemaname}].servicedefinition ON ((servicedefinition.id = contract.servicedefinitionId)))
        LEFT JOIN [${dbxschemaname}].membergrouptype ON ((membergrouptype.id = servicedefinition.serviceType)))
        LEFT JOIN [${dbxschemaname}].customergroup ON (((customergroup.Customer_id = contractcustomers.customerId)
            AND (customergroup.contractId = contractcustomers.contractId)
            AND (customergroup.coreCustomerId = contractcustomers.coreCustomerId))))
        LEFT JOIN [${dbxschemaname}].membergroup ON ((membergroup.id = customergroup.Group_id)))');
	SET @isWhereAppened = 'false';
    SET @shouldAndAppend = 'false';
	IF(@_customerId != '') BEGIN
		IF(@isWhereAppened = 'false') BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @isWhereAppened = 'true';
            SET @shouldAndAppend = 'true';
		END;
      set @select_statement =  concat(@select_statement ,' contractcustomers.customerId = ',@_customerId);
    END;
    IF(@_coreCustomerId != '') BEGIN
		IF(@isWhereAppened = 'false') BEGIN
			SET @select_statement = CONCAT(@select_statement , ' where');
            SET @shouldAndAppend = 'true';
		END;
        IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
			SET @select_statement = CONCAT(@select_statement , ' and');
            SET @shouldAndAppend = 'true';
		END;
      set @select_statement =  concat(@select_statement ,' contractcustomers.coreCustomerId = ',@_coreCustomerId);
    END;
    set @select_statement =  concat(@select_statement ,');');
	exec(@select_statement);
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_user_corecustomer_actions];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_user_corecustomer_actions]
	@_userId nvarchar(100),
    @_coreCustomerId nvarchar(100)
AS
	BEGIN
     DECLARE @_serviceDefinitionId nvarchar(max);
     DECLARE @_roleId nvarchar(max);
	 DECLARE @action_select_statement nvarchar(max);
	 DECLARE @select_statement nvarchar(max);

    SET @_serviceDefinitionId = (select String_agg(CAST(contract.servicedefinitionId AS nvarchar(max)),',') from 
								[${dbxschemaname}].contract LEFT JOIN  [${dbxschemaname}].contractcorecustomers 
								on (contractcorecustomers.contractId = contract.id ) 
                                where contractcorecustomers.coreCustomerId = @_coreCustomerId);
	SET @_roleId = (select String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') from [${dbxschemaname}].customergroup where customergroup.Customer_id = @_userId
					and customergroup.coreCustomerId = @_coreCustomerId );

	SET @action_select_statement = ('(SELECT DISTINCT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id ))');
	IF(@_serviceDefinitionId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT DISTINCT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = ' , @_serviceDefinitionId);
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.actionId IN' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT('(SELECT DISTINCT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ',@_roleId);
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Action_id IN ');
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT DISTINCT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ',@_coreCustomerId);
        SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
	IF(@_userId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT DISTINCT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ',@_userId);
        IF(@_coreCustomerId != '') BEGIN 
			SET @select_statement = CONCAT(@select_statement , ' AND customeraction.coreCustomerId = ' , @_coreCustomerId);
        END
        SET @select_statement = CONCAT(@select_statement , ' AND customeraction.isAllowed = 1 AND customeraction.Action_id IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
	END
   EXEC(@select_statement)
END

GO

CREATE INDEX [IDX_contractcustomers_contractId] ON [${dbxschemaname}].contractcustomers 
(contractId ASC);

CREATE INDEX [IDX_customergroup_contractId] ON [${dbxschemaname}].customergroup 
(contractId ASC);

CREATE INDEX [IDX_customergroup_coreCustomerId] ON [${dbxschemaname}].customergroup 
(coreCustomerId ASC);
	
CREATE INDEX [IDX_actionIdAndCoreCustomerId] ON [${dbxschemaname}].contractactionlimit 
(actionId ASC, coreCustomerId ASC);

CREATE INDEX [IDX_actionid_customerId_isAllowed] ON [${dbxschemaname}].customeraction 
(Customer_id ASC, isAllowed ASC, Action_id ASC);

/****** Object: StoredProcedure [${dbxschemaname}].[customer_basic_info_proc] Script Date: 10/28/2020 5:04:56 PM ******/
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_basic_info_proc]

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]
	@_customerId nvarchar(50)
AS
	BEGIN
		SET XACT_ABORT ON
		SET NOCOUNT ON
		declare @accountLockoutThreshold nvarchar(max)
		declare @accountLockoutTime nvarchar(max)
		SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
		SET @accountLockoutTime =(SELECT accountLockoutTime from [${dbxschemaname}].passwordlockoutsettings)
		
		SELECT TOP (1)
			customer.UserName AS Username,
			customer.FirstName AS FirstName,
			customer.MiddleName AS MiddleName,
			customer.LastName AS LastName,
			(ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS Name,
			customer.Salutation AS Salutation,
			customer.id AS Customer_id,
			customer.Ssn AS SSN,
			customer.createdts AS CustomerSince,
			customer.Gender AS Gender,
			customer.DateOfBirth AS DateOfBirth,
			customer.isEnrolledFromSpotlight AS isEnrolledFromSpotlight,
			CASE
				WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
			ELSE CASE
				WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
			ELSE customer.Status_id
			END
			END AS CustomerStatus_id,
			customerstatus.Description AS CustomerStatus_name,
			customer.MaritalStatus_id AS MaritalStatus_id,
			maritalstatus.Description AS MaritalStatus_name,
			customer.SpouseName AS SpouseName,
			customer.DrivingLicenseNumber AS DrivingLicenseNumber,
			customer.lockedOn AS lockedOn,
			customer.lockCount AS lockCount,
			customer.EmployementStatus_id AS EmployementStatus_id,employementstatus.Description AS EmployementStatus_name,
			( SELECT String_agg(CAST(Status_id as nvarchar(max)),',')
				FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id) ) AS CustomerFlag_ids,
			( SELECT String_agg(CAST(Description as nvarchar(max)),',')
				FROM [${dbxschemaname}].status
				WHERE status.id IN
			(
				SELECT customerflagstatus.Status_id
					FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id)
			)) AS CustomerFlag,
			customer.IsEnrolledForOlb AS IsEnrolledForOlb,
			customer.isEnrolled AS isEnrolled,
			customer.IsStaffMember AS IsStaffMember,
			customer.Location_id AS Branch_id,
			location.Name AS Branch_name,
			location.Code AS Branch_code,
			customer.IsOlbAllowed AS IsOlbAllowed,
			customer.IsAssistConsented AS IsAssistConsented,
			customer.isEagreementSigned AS isEagreementSigned,
			customer.isCombinedUser AS isCombinedUser,
			ISNULL(customer.combinedUserId, N'') AS combinedUserId,
			IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS CustomerType_id,
			IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS CustomerType_Name,
			IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS CustomerType_Description,
			(SELECT membergroup.id FROM [${dbxschemaname}].membergroup WHERE membergroup.id in
			(SELECT customergroup.Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_RoleId,
			(SELECT membergroup.Name FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_Role,
			(SELECT membergroup.isEAgreementActive FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS isEAgreementRequired,
			customer.Organization_Id AS organisation_id,
			organisation.BusinessType_id AS BusinessType_id,
			businesstype.name AS BusinessType,
			organisation.Name AS organisation_name,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryEmailAddress,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryEmailAddress,
			customer.DocumentsSubmitted AS DocumentsSubmitted,
			customer.ApplicantChannel AS ApplicantChannel,
			customer.Product AS Product,
			customer.Reason AS Reason,
			@accountLockoutTime AS accountLockoutTime
		FROM ((((((([${dbxschemaname}].customer
		LEFT JOIN [${dbxschemaname}].location
		ON ((customer.Location_id = location.id)))
		LEFT JOIN [${dbxschemaname}].organisation
		ON ((customer.Organization_Id = organisation.id)))
		LEFT JOIN [${dbxschemaname}].businesstype ON ((businesstype.id = organisation.BusinessType_id)))
		INNER JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		LEFT JOIN [${dbxschemaname}].status AS customerstatus
		ON ((customer.Status_id = customerstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS maritalstatus
		ON ((customer.MaritalStatus_id = maritalstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS employementstatus
		ON ((customer.EmployementStatus_id = employementstatus.id)))
		WHERE customer.id = @_customerId 
END
GO

GO
/****** Object:  View [${dbxschemaname}].[customeraddress_view]    Script Date: 6/9/2020 2:00:26 PM ******/
DROP VIEW IF EXISTS [${dbxschemaname}].[customeraddress_view]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customeraddress_view] (
   [CustomerId], 
   [Address_id], 
   [isPrimary], 
   [AddressType], 
   [isTypeBusiness], 
   [AddressId], 
   [AddressLine1], 
   [AddressLine2], 
   [ZipCode], 
   [Region_id], 
   [City_id], 
   [Country_id], 
   [CityName], 
   [RegionName], 
   [RegionCode], 
   [CountryName], 
   [CountryCode])
AS 
   SELECT 
      c.id AS CustomerId, 
      ca.Address_id AS Address_id, 
      ca.isPrimary AS isPrimary, 
      ca.Type_id AS AddressType, 
      ca.isTypeBusiness AS isTypeBusiness, 
      a.id AS AddressId, 
      a.addressLine1 AS AddressLine1, 
      a.addressLine2 AS AddressLine2, 
      a.zipCode AS ZipCode, 
      a.Region_id AS Region_id, 
      a.City_id AS City_id,
	  iif((a.country IS NOT null), a.country, reg.Country_id) AS Country_id,
      a.cityName AS CityName, 
      reg.Name AS RegionName, 
      reg.Code AS RegionCode,
	  iif((a.country IS NOT null), country.Name, coun.Name) AS CountryName,
      iif((a.country IS NOT null), country.Code, coun.Code) AS CountryCode
   FROM (((([${dbxschemaname}].customeraddress  AS ca 
      INNER JOIN [${dbxschemaname}].customer  AS c 
      ON (ca.Customer_id = c.id)) 
      INNER JOIN [${dbxschemaname}].address  AS a 
      ON ((a.id = ca.Address_id)) 
      LEFT JOIN [${dbxschemaname}].region  AS reg 
      ON (reg.id = a.Region_id)) 
      LEFT JOIN [${dbxschemaname}].country  AS coun 
      ON (coun.id = reg.Country_id))
	  LEFT JOIN [${dbxschemaname}].country  AS country 
      ON (country.id = reg.Country_id))
GO

CREATE INDEX [FK_CoreCustID] ON [${dbxschemaname}].[contractcorecustomers] ([coreCustomerId]);
GO

--ALTER TABLE [${dbxschemaname}].[contract] ADD CONSTRAINT [FK_contract_status_statusId] FOREIGN KEY ([statusId]) REFERENCES --[${dbxschemaname}].[status] ([id]) 
--  ON DELETE NO ACTION
--  ON UPDATE NO ACTION;
--GO
CREATE INDEX [FK_contractactionlimit_featureaction_actionId_idx] ON [${dbxschemaname}].[contractactionlimit] ([actionId]);
CREATE INDEX [FK_contractactionlimit_feature_featureId_idx] ON [${dbxschemaname}].[contractactionlimit] ([featureId]);
GO

-- ALTER TABLE [${dbxschemaname}].[contractactionlimit] ADD CONSTRAINT [FK_contractactionlimit_featureaction_actionId] FOREIGN KEY ([actionId]) --REFERENCES [${dbxschemaname}].[featureaction] ([id]) 
--  ON DELETE NO ACTION
--  ON UPDATE NO ACTION;
-- GO

-- ALTER TABLE [${dbxschemaname}].[contractactionlimit] ADD CONSTRAINT [FK_contractactionlimit_feature_featureId] FOREIGN KEY ([featureId]) --REFERENCES [${dbxschemaname}].[feature] ([id]) 
--  ON DELETE NO ACTION
--  ON UPDATE NO ACTION;
--GO


CREATE INDEX [IDX_contractactionlimit_corecustomerId] ON [${dbxschemaname}].[contractactionlimit] ([coreCustomerId]);
CREATE INDEX [IDX_contractaccounts_corecustomerId] ON [${dbxschemaname}].[contractaccounts] ([coreCustomerId]);
CREATE INDEX [IDX_contractcustomers_corecustomerId] ON [${dbxschemaname}].[contractcustomers] ([coreCustomerId]);
GO


ALTER TABLE [${dbxschemaname}].[contractcustomers] ADD CONSTRAINT [FK_contractcustomers_customer_customerId] FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) 
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
GO

--ALTER TABLE [${dbxschemaname}].[contractfeatures] ADD CONSTRAINT [FK_contractfeatures_feature_featureId] FOREIGN KEY ([featureId]) REFERENCES [${dbxschemaname}].[feature] ([id]) 
--  ON DELETE NO ACTION
--  ON UPDATE NO ACTION;
--GO

CREATE INDEX [FK_contractfeatures_feature_featureId_idx] ON [${dbxschemaname}].[contractfeatures] ([featureId]);
GO

CREATE INDEX [IDX_contract_id_name] ON [${dbxschemaname}].[contract] ([id],[name]);
CREATE INDEX [IDX_contractaccounts_contractId_corecustomerId] ON [${dbxschemaname}].[contractaccounts] ([contractId],[coreCustomerId]);
CREATE INDEX [IDX_contractaccounts_accountId] ON [${dbxschemaname}].[contractaccounts] ([accountId]);
CREATE INDEX [IDX_contract_status] ON [${dbxschemaname}].[contract] ([statusId]);
CREATE INDEX [IDX_contractcorecustomers_contractId_corecustomerId_isPrimary] ON [${dbxschemaname}].[contractcorecustomers] ([contractId],[coreCustomerId],[isPrimary]);
CREATE INDEX [IDX_contractfeatures_contractId_corecustomerId] ON [${dbxschemaname}].[contractfeatures] ([contractId],[coreCustomerId]);
CREATE INDEX [IDX_contractactionlimit_contractId_corecustomerId] ON [${dbxschemaname}].[contractactionlimit] ([contractId],[coreCustomerId]);
CREATE INDEX [IDX_contractfeatures_contractId_corecustomerId_featureId] ON [${dbxschemaname}].[contractfeatures] ([contractId],[coreCustomerId],[featureId]);
CREATE INDEX [IDX_contractacttionlimit_contractId_corecustomerId_featuresId] ON [${dbxschemaname}].[contractactionlimit] ([contractId],[coreCustomerId],[featureId]);
CREATE INDEX [IDX_contractactionlimit_contractId_corecustomerId_actionId] ON [${dbxschemaname}].[contractactionlimit] ([contractId],[coreCustomerId],[featureId],[actionId]);
CREATE INDEX [FK_backendIdentifier_customer_customerId_idx] ON [${dbxschemaname}].[backendidentifier] ([Customer_id]);
GO

ALTER TABLE [${dbxschemaname}].[backendidentifier] ADD CONSTRAINT [FK_backendIdentifier_customer_customerId] FOREIGN KEY ([Customer_id]) REFERENCES [${dbxschemaname}].[customer] ([id]) 
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
GO
CREATE INDEX [IDX_backendIdentifier_customerId_backendType] ON [${dbxschemaname}].[backendidentifier] ([Customer_id],[BackendType]);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_search_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50),
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer ' + (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
                        ELSE N''
                     END)
            
                  SET @queryStatement = (@queryStatement) + (N' WHERE 1=1 ')
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id)
					LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id)
					LEFT JOIN [${dbxschemaname}].country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory,address.addressLine1, address.addressLine2, city.Name, address.zipCode, country.Name, customer.isEnrolled ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                        
                          
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              
                                 SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
        exec(@queryStatement)
     END

GO
    
CREATE TABLE 
[${dbxschemaname}].[customerlimitgrouplimits]
(
   [id] nvarchar(50)  NOT NULL,
   [Customer_id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(45)  NOT NULL,
   [coreCustomerId] nvarchar(45)  NULL,
   [limitGroupId] nvarchar(45)   NULL,
   [LimitType_id] nvarchar(50)   NULL,
   [value] decimal(20, 2)  NULL,
   [createdby] nvarchar(50)  NULL,
   [modifiedby] nvarchar(50)  NULL,
   [createdts] datetime  NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime  NOT NULL DEFAULT GETDATE(),
   [synctimestamp] datetime  NOT NULL DEFAULT GETDATE(),
   [softdeleteflag] smallint  NOT NULL DEFAULT '0'
)
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_restrictive_featureactionlimits_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_restrictive_featureactionlimits_proc](
	@_locale nvarchar(50),
    @_userId nvarchar(50),
    @_serviceDefinitionId nvarchar(50),
    @_roleId nvarchar(50),
    @_coreCustomerId nvarchar(50)
)
AS
BEGIN
	DECLARE @select_statement NVARCHAR(max);
	DECLARE @action_select_statement NVARCHAR(max);
	SET @select_statement = '';
    SET @action_select_statement = '';
	SET @action_select_statement = '(SELECT featureaction.id AS actionId FROM [${dbxschemaname}].featureaction LEFT JOIN [${dbxschemaname}].feature ON ( feature.id = featureaction.Feature_id ))';
	IF(@_serviceDefinitionId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT servicedefinitionactionlimit.actionId AS actionId FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId,'''');
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;	
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT('(SELECT groupactionlimit.Action_id AS actionId FROM [${dbxschemaname}].groupactionlimit WHERE groupactionlimit.Group_id = ','''',@_roleId,'''');
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Action_id IN ');
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT contractactionlimit.actionId AS actionId FROM [${dbxschemaname}].contractactionlimit WHERE contractactionlimit.coreCustomerId = ','''',@_coreCustomerId,'''');
        SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
	IF(@_userId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT customeraction.Action_id AS actionId FROM [${dbxschemaname}].customeraction WHERE customeraction.Customer_id = ','''',@_userId,'''');
		IF(@_coreCustomerId != '') BEGIN 
			SET @select_statement = CONCAT(@select_statement , ' AND customeraction.coreCustomerId = ' ,@_coreCustomerId);
        END ;
        SET @select_statement = CONCAT(@select_statement , ' AND customeraction.isAllowed = ''1'' AND customeraction.Action_id IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;

    SET @select_statement = '( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue';
    if(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', null AS serviceLimitValue');
    END ;
	IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS groupLimitValue');
    END ;  
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS coreCustomerLimitValue');
    END ;
    SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
															LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
	IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON (contractactionlimit.actionId = featureaction.id)');
    END ;
	SET @select_statement = CONCAT(@select_statement , 'WHERE featureaction.Type_id = ''NON_MONETARY'' AND featureaction.id IN ' , @action_select_statement , ')');
    
    SET @select_statement = CONCAT(@select_statement , ' UNION (SELECT 
								feature.id AS featureId,
                                featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue');
	IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', servicedefinitionactionlimit.value AS serviceLimitValue');
    END ;
    IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', groupactionlimit.value AS groupLimitValue');
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', contractactionlimit.value AS coreCustomerLimitValue');
    END ;
	SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
														LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN [${dbxschemaname}].actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)');
                                                                                                        
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)');
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)');
	END ;
	SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.Type_id = ''MONETARY''');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''',@_serviceDefinitionId ,'''');
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Group_id = ' , '''',@_roleId,'''');
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.coreCustomerId = ' ,'''', @_coreCustomerId,'''');
	END ;
    SET @select_statement = CONCAT(@select_statement , ' AND featureaction.id IN ' , @action_select_statement , ')'); 
   exec(@select_statement);
END
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[corecustomeraccounts_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[corecustomeraccounts_details_get_proc]  
   @_coreCustomerIdList nvarchar(max),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  DECLARE @selectstatement NVARCHAR(max);
	  DECLARE @corecustomersList NVARCHAR(max);

      SET @corecustomersList = 
         (

            SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',')
            FROM [${dbxschemaname}].contractcustomers
            WHERE contractcustomers.customerId = @_customerId AND [${dbxschemaname}].FIND_IN_SET(contractcustomers.coreCustomerId, @_coreCustomerIdList) > 0

         )
      

      SET @selectstatement = CONCAT('(SELECT
	       contractcorecustomers.coreCustomerId AS coreCustomerId ,
           contractcorecustomers.coreCustomerName AS coreCustomerName ,
           customeraccounts.email AS email ,
           customeraccounts.Customer_id AS customerId ,
           customeraccounts.FavouriteStatus AS favouriteStatus ,
           IIF ((customeraccounts.EStatementmentEnable) = ''1'' ,''true'' , ''false'') AS eStatementEnable,
           IIF ((contractcorecustomers.isBusiness) = ''1'' ,''true'' , ''false'') AS isBusinessAccount,
           contractaccounts.accountId AS accountId
           from [${dbxschemaname}].contractcorecustomers 
		   JOIN [${dbxschemaname}].contractaccounts ON (contractcorecustomers.coreCustomerId = contractaccounts.coreCustomerId)
           JOIN [${dbxschemaname}].customeraccounts ON (customeraccounts.Account_id = contractaccounts.accountId)
           where [${dbxschemaname}].FIND_IN_SET(contractcorecustomers.coreCustomerId, ','''',@corecustomersList,'''',')>0 AND customeraccounts.Customer_id = ','''',@_customerId,'''',')')
      
	  exec(@selectstatement);
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[membership_customer_search_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[membership_customer_search_proc](
	@_id nvarchar(50),
	@_name nvarchar(50),
	@_email nvarchar(50),
	@_phone nvarchar(50),
	@_dateOfBirth nvarchar(50),
	@_status nvarchar(50),
	@_country varchar(50),
	@_city  varchar(50),
	@_zipCode nvarchar(50)
)
AS 
	BEGIN
	DECLARE @select_statement NVARCHAR(MAX);
	DECLARE @address_select_statement NVARCHAR(MAX);
	SET @select_statement = ('select [${dbxschemaname}].[membership].[id] , [${dbxschemaname}].[membership].[isBusinessType] , [${dbxschemaname}].[membership].[name], 
		[${dbxschemaname}].[membership].[industry] , [${dbxschemaname}].[membership].[firstName] , [${dbxschemaname}].[membership].[lastName] , 
		[${dbxschemaname}].[membership].[phone] , [${dbxschemaname}].[membership].[taxId], [${dbxschemaname}].[membership].[faxId], 
		[${dbxschemaname}].[membership].[email] , [${dbxschemaname}].[address].[addressLine1],
		[${dbxschemaname}].[address].[addressLine2] , [${dbxschemaname}].[address].[cityName] , [${dbxschemaname}].[address].[country] , 
		[${dbxschemaname}].[address].[zipCode] , [${dbxschemaname}].[address].[state]
		from [${dbxschemaname}].[membership] LEFT JOIN [${dbxschemaname}].[address] on ([${dbxschemaname}].[membership].[addressId] = [${dbxschemaname}].[address].[id])
		where [${dbxschemaname}].[membership].[id] is not null');
	
	IF(ISNULL(@_id,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[id] = ','''' + @_id + '''');
    END;
    IF(ISNULL(@_name,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[name] LIKE %',@_name,'%');
    END;
    IF(ISNULL(@_email,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[email] = ','''' + @_email + '''');
    END;
    IF(ISNULL(@_phone,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[phone] = ','''' + @_phone+'''');
    END;
    IF(ISNULL(@_dateOfBirth,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[dateOfBirth] = ','''' + @_dateOfBirth + '''');
    END;	
	SET @address_select_statement = '';
	IF(ISNULL(@_city,'') != ''  OR ISNULL(@_country,'') != '' OR ISNULL(@_zipCode,'') != '') BEGIN
		SET @address_select_statement = 'select address.id from address where address.id is not null';
        IF(ISNULL(@_city,'') != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[cityName] = ','''' + @_city + '''');
        END;
        IF(ISNULL(@_country,'') != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[country] = ','''' + @_country+'''');
        END;
        IF(ISNULL(@_zipCode,'') != '') BEGIN
			SET @address_select_statement = concat(@address_select_statement , ' and [${dbxschemaname}].[address].[zipCode] = ','''' + @_zipCode + '''');
        END;
    END;
	IF(ISNULL(@address_select_statement,'') != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[addressId] in (", @address_select_statement , ")');
    END;

	SET @select_statement = CONCAT(@select_statement , ';');


    exec(@select_statement);
END;
GO

ALTER TABLE [${dbxschemaname}].[contractaddress] ADD  DEFAULT ((0)) FOR [isPrimary]
GO




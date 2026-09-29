ALTER TABLE [${dbxschemaname}].[membershiprelation] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[suspendedcustomers] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[transaction] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[sp_delete_primary_constranit]
@_tableName varchar(100)
AS
BEGIN
DECLARE @constraintName varchar(100);
DECLARE @deletequery varchar(500);
SET @constraintName=(SELECT name FROM sys.objects
					WHERE type = 'PK' 
					AND  parent_object_id = OBJECT_ID (@_tableName))
	IF @constraintName is not null
	BEGIN
		SET @deletequery = 'alter table '+@_tableName+' drop constraint '+@constraintName
		EXEC(@deletequery)
	END
END
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] 
	'${dbxschemaname}.[alertattribute]';
GO

ALTER TABLE [${dbxschemaname}].[alertattribute]
ADD CONSTRAINT PK_alertattribute_id
PRIMARY KEY (id,LanguageCode,companyLegalUnit);
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] DROP CONSTRAINT [FK_alertsubtype_alertrecipienttype]
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] 
	'${dbxschemaname}.[alertrecipienttype]';
GO
ALTER TABLE [${dbxschemaname}].[alertrecipienttype]
ADD CONSTRAINT PK_alertrecipienttype_id
PRIMARY KEY (id,companyLegalUnit);
GO

ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD CONSTRAINT [FK_alertsubtype_alertrecipienttype]
FOREIGN KEY([recipienttype],[companyLegalUnit]) REFERENCES [${dbxschemaname}].[alertrecipienttype] ([id],[companyLegalUnit])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtype] CHECK CONSTRAINT [FK_alertsubtype_alertrecipienttype]
GO

EXEC [${dbxschemaname}].[sp_delete_primary_constranit] 
	'${dbxschemaname}.[alertsubtypeaccounttype]';
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype]
ADD CONSTRAINT PK_alertsubtypeaccounttype_id
PRIMARY KEY (accountTypeId,alertSubTypeId,companyLegalUnit);
GO

EXECUTE [${dbxschemaname}].[sp_delete_primary_constranit] 
   '${dbxschemaname}.[alertsubtypecustomertype]'
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype]
ADD CONSTRAINT PK_alertsubtypecustomertype_id
PRIMARY KEY (customerTypeId,alertSubTypeId,companyLegalUnit);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_contracts_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_contracts_proc]
        @_customerId nvarchar(50),
        @_contractId nvarchar(50),
		@_legalEntityId nvarchar(100)
    AS BEGIN
        declare @select_statement nvarchar(MAX);
        declare @isWhereAppened nvarchar(100);
        declare @shouldAndAppend nvarchar(100);
		declare @filteredContracts nvarchar(MAX);
		
		SET @filteredContracts = (SELECT String_agg([${dbxschemaname}].[contractaccounts].[contractId], ',')
		FROM [${dbxschemaname}].[contractaccounts]
		WHERE [${dbxschemaname}].[contractaccounts].[statusDesc] != 'CLOSED' 
		AND [${dbxschemaname}].[contractaccounts].[coreCustomerId] IN
		(SELECT [${dbxschemaname}].[contractcustomers].[coreCustomerId] 
		FROM [${dbxschemaname}].[contractcustomers] WHERE [${dbxschemaname}].[contractcustomers].[customerId] = @_customerId));
		
        SET @select_statement = ('(SELECT 
            contractcustomers.customerId AS customerId,
            contractcustomers.coreCustomerId AS coreCustomerId,
			contractcustomers.companyLegalUnit AS companyLegalUnit,
            contractcustomers.contractId AS contractId,
			contractcustomers.autoSyncAccounts AS autoSyncAccounts,
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
            LEFT JOIN [${dbxschemaname}].contractcorecustomers ON ((([${dbxschemaname}].contractcorecustomers.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].contractcorecustomers.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].contract ON (([${dbxschemaname}].contract.id = [${dbxschemaname}].contractcorecustomers.contractId)))
            LEFT JOIN [${dbxschemaname}].servicedefinition ON (([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].contract.servicedefinitionId)))
            LEFT JOIN [${dbxschemaname}].membergrouptype ON (([${dbxschemaname}].membergrouptype.id = [${dbxschemaname}].servicedefinition.serviceType)))
            LEFT JOIN [${dbxschemaname}].customergroup ON ((([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].contractcustomers.customerId)
                AND ([${dbxschemaname}].customergroup.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].customergroup.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].membergroup ON (([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id)))');
        
        SET @isWhereAppened = 'false';
        SET @shouldAndAppend = 'false';
        IF(@_customerId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @isWhereAppened = 'true';
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.customerId = ', ''''+@_customerId+'''');
        END;
        IF(@_legalEntityId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @shouldAndAppend = 'true';
            END;
            IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' and');
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.companyLegalUnit = ',''''+@_legalEntityId)+'''';
        END;

        IF(@_contractId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @shouldAndAppend = 'true';
            END;
            IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' and');
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.contractId = ',''''+@_contractId)+'''';
        END;
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in (',''+@filteredContracts+'','));');
        exec(@select_statement);
    END;
GO    
    
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_contracts_withaccounts_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[user_contracts_withaccounts_proc] 
@_customerId nvarchar(50),
@_contractId nvarchar(50),
@_legalEntityId nvarchar(100)
AS BEGIN
declare @select_statement nvarchar(MAX);
declare @isWhereAppened nvarchar(100);
declare @shouldAndAppend nvarchar(100);
SET @select_statement = '(SELECT
contractcustomers.customerId AS customerId,
contractcustomers.companyLegalUnit AS companyLegalUnit,
contractcustomers.coreCustomerId AS coreCustomerId,
contractcustomers.contractId AS contractId,
contractcustomers.autoSyncAccounts AS autoSyncAccounts,
contract.name AS contractName,
contractcustomers.isPrimary AS isPrimary,
contractcorecustomers.coreCustomerName AS coreCustomerName,
contractcorecustomers.isBusiness AS isBusiness,
contract.servicedefinitionId AS serviceDefinitionId,
servicedefinition.name AS serviceDefinitionName,
membergrouptype.description AS serviceDefinitionType,
membergroup.id AS roleId,
membergroup.Name AS userRole,
contractaccounts.accountId,
contractaccounts.accountName,
contractaccounts.statusDesc,
contractaccounts.typeId
FROM
[${dbxschemaname}].contractcustomers
LEFT JOIN [${dbxschemaname}].contract ON ([${dbxschemaname}].contract.id = [${dbxschemaname}].contractcustomers.contractId)
LEFT JOIN [${dbxschemaname}].contractcorecustomers ON (([${dbxschemaname}].contractcorecustomers.contractId = [${dbxschemaname}].contractcustomers.contractId)
AND ([${dbxschemaname}].contractcorecustomers.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].contractaccounts ON ((contractaccounts.contractId = contractcustomers.contractId)
AND (contractaccounts.coreCustomerId = contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].servicedefinition ON ([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].contract.servicedefinitionId)
LEFT JOIN [${dbxschemaname}].membergrouptype ON ([${dbxschemaname}].membergrouptype.id = [${dbxschemaname}].servicedefinition.serviceType)
LEFT JOIN [${dbxschemaname}].customergroup ON (([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].contractcustomers.customerId)
AND ([${dbxschemaname}].customergroup.contractId = [${dbxschemaname}].contractcustomers.contractId)
AND ([${dbxschemaname}].customergroup.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].membergroup ON ([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id)';

SET @isWhereAppened = 'false';
SET @shouldAndAppend = 'false';
IF(@_customerId != '') BEGIN
IF(@isWhereAppened = 'false') BEGIN
SET @select_statement = CONCAT(@select_statement , ' where');
SET @isWhereAppened = 'true';
SET @shouldAndAppend = 'true';
END;
set @select_statement = concat(@select_statement ,' contractcustomers.customerId = ', ''''+@_customerId+'''');
END;

IF(@_legalEntityId != '') BEGIN
IF(@isWhereAppened = 'false') BEGIN
SET @select_statement = CONCAT(@select_statement , ' where');
SET @shouldAndAppend = 'true';
END;
IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
SET @select_statement = CONCAT(@select_statement , ' and');
SET @shouldAndAppend = 'true';
END;
set @select_statement = concat(@select_statement ,' contractcustomers.companyLegalUnit = ',''''+@_legalEntityId)+'''';
END;


IF(@_contractId != '') BEGIN
IF(@isWhereAppened = 'false') BEGIN
SET @select_statement = CONCAT(@select_statement , ' where');
SET @shouldAndAppend = 'true';
END;
IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
SET @select_statement = CONCAT(@select_statement , ' and');
SET @shouldAndAppend = 'true';
END;
set @select_statement = concat(@select_statement ,' contractcustomers.contractId = ',''''+@_contractId)+'''';
END;

set @select_statement = concat(@select_statement ,');');

exec(@select_statement);
END;

GO
    
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_customers_proc]
        @_customerId nvarchar(50),
        @_coreCustomerId nvarchar(50),
        @_legalEntityId nvarchar(100)
    AS BEGIN
	declare @select_statement nvarchar(MAX);
        declare @isWhereAppened nvarchar(100);
        declare @shouldAndAppend nvarchar(100);
        declare @filteredContracts nvarchar(MAX);

        SET @filteredContracts = (SELECT String_agg([contractaccounts].[contractId], ',')
        FROM [contractaccounts]
        WHERE [contractaccounts].[statusDesc] != 'CLOSED' 
        AND [contractaccounts].[coreCustomerId] IN
        (SELECT [contractcustomers].[coreCustomerId] 
        FROM [contractcustomers] WHERE [contractcustomers].[customerId] = @_customerId));
        SET @filteredContracts = '(select distinct value from STRING_SPLIT('''+@filteredContracts+''','',''))';
        SET @select_statement = ('(SELECT 
            contractcustomers.customerId AS customerId,
            contractcustomers.coreCustomerId AS coreCustomerId,
            contractcustomers.companyLegalUnit AS companyLegalUnit,
            contractcustomers.contractId AS contractId,
            contractcustomers.autoSyncAccounts AS autoSyncAccounts,
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
            LEFT JOIN [${dbxschemaname}].contractcorecustomers ON ((([${dbxschemaname}].contractcorecustomers.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].contractcorecustomers.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].contract ON (([${dbxschemaname}].contract.id = [${dbxschemaname}].contractcorecustomers.contractId)))
            LEFT JOIN [${dbxschemaname}].servicedefinition ON (([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].contract.servicedefinitionId)))
            LEFT JOIN [${dbxschemaname}].membergrouptype ON (([${dbxschemaname}].membergrouptype.id = [${dbxschemaname}].servicedefinition.serviceType)))
            LEFT JOIN [${dbxschemaname}].customergroup ON ((([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].contractcustomers.customerId)
                AND ([${dbxschemaname}].customergroup.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].customergroup.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].membergroup ON (([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id)))');

        SET @isWhereAppened = 'false';
        SET @shouldAndAppend = 'false';
        IF(@_customerId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @isWhereAppened = 'true';
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.customerId = ', ''''+@_customerId+'''');
        END;
        IF(@_legalEntityId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @shouldAndAppend = 'true';
            END;
            IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' and');
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.companyLegalUnit = ',''''+@_legalEntityId)+'''';
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
          set @select_statement =  concat(@select_statement ,' contractcustomers.coreCustomerId = ',''''+@_coreCustomerId)+'''';
        END;
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in ',''+@filteredContracts+'',');'); 
        exec(@select_statement);
    END;
GO

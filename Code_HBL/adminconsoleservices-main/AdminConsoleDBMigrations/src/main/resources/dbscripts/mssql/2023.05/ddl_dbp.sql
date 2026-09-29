DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_associated_contractaccounts_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[get_associated_contractaccounts_proc](
	@_accountIdList NVARCHAR(MAX) ,
    @_coreCustomerId NVARCHAR(MAX)
) AS BEGIN

    DECLARE @accountIdList NVARCHAR(MAX);
    DECLARE @excludedaccountIdList NVARCHAR(MAX);
    DECLARE @coreCustomerAccounts NVARCHAR(MAX);
	DECLARE @otherCoreCustomerAccounts NVARCHAR(MAX);
    
	SET @accountIdList = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].[contractaccounts].accountId in (select distinct value from STRING_SPLIT(@_accountIdList,',')));
							
	SET @excludedaccountIdList = (SELECT STRING_AGG([excludedcontractaccounts].[accountId], ',') from [${dbxschemaname}].[excludedcontractaccounts] 
							WHERE [${dbxschemaname}].[excludedcontractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) );
							
	SET @coreCustomerAccounts = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [dbxdb].[contractaccounts] 
							WHERE [dbxdb].[contractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) 
							and [dbxdb].[contractaccounts].[coreCustomerId] = @_coreCustomerId);

	SET @otherCoreCustomerAccounts = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [dbxdb].[contractaccounts] 
							WHERE [dbxdb].[contractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) 
							and [dbxdb].[contractaccounts].[coreCustomerId] <> @_coreCustomerId);

    select @accountIdList As accountIdList;
	select @excludedaccountIdList As excludedaccountIdList;
    select @coreCustomerAccounts As coreCustomerAccounts;
	select @otherCoreCustomerAccounts As otherCoreCustomerAccounts;
END
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
		AND [contractaccounts].[contractId] IN
		(SELECT [contractcustomers].[contractId] 
		FROM [contractcustomers] WHERE [contractcustomers].[customerId] = @_customerId));
		
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
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in (',''''+@filteredContracts+'''','));');
        exec(@select_statement);
    END;
GO	

ALTER TABLE [${dbxschemaname}].[customer] ADD [isHeavyUser] BIT;

ALTER TABLE [${dbxschemaname}].[customview] ADD [coreCustomerId] VARCHAR(45) NULL;

ALTER TABLE [${dbxschemaname}].[contractcustomers] ADD [FavouriteStatus] BIT DEFAULT 0;
GO
DROP PROCEDURE IF EXISTS  [${dbxschemaname}].[getCoreCustomerIdsAndAccounts];
GO
CREATE  PROCEDURE  [${dbxschemaname}].[getCoreCustomerIdsAndAccounts](
     @customerId nvarchar(50),
	 @companyLegalUnit nvarchar(50)	 
)AS BEGIN

SELECT 
[contractcustomers].[coreCustomerId] AS membership_id,
[contract].[name] AS membership_name,
[contractcustomers].[FavouriteStatus] AS favouriteStatus,
COUNT(*) AS accountsCount
FROM 
(([customeraccounts] JOIN [contractcustomers] ON [customeraccounts].[Customer_id]=@customerId AND [customeraccounts].[companyLegalUnit]=@companyLegalUnit AND [customeraccounts].[Customer_id]= [contractcustomers].[customerId] AND [customeraccounts].[coreCustomerId] =  [contractcustomers].[coreCustomerId]) 
JOIN [contract] ON [contract].[id] = [contractcustomers].[contractId])
GROUP BY [contractcustomers].[contractId],[contractcustomers].[coreCustomerId],[contract].[name] ,[contractcustomers].[FavouriteStatus];

END;
GO
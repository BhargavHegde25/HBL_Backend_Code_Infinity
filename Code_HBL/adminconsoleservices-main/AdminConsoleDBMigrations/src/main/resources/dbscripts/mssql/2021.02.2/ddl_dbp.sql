CREATE TABLE [${dbxschemaname}].[excludedcustomeraccounts] (
	id nvarchar(50) NOT NULL,
	Customer_id nvarchar(50) DEFAULT (NULL),
	Membership_id nvarchar(50) DEFAULT (NULL),
	Account_id nvarchar(50) DEFAULT (NULL),
	Organization_id nvarchar(45) DEFAULT (NULL),
	AccountName nvarchar(50) DEFAULT (NULL),
	FavouriteStatus int DEFAULT ((0)) NOT NULL,
	IsViewAllowed bit DEFAULT ((0)) NOT NULL,
	IsDepositAllowed bit DEFAULT ((0)) NOT NULL,
	IsWithdrawAllowed bit DEFAULT ((0)) NOT NULL,
	IsOrganizationAccount bit DEFAULT ((0)) NOT NULL,
	IsOrgAccountUnLinked bit DEFAULT ((0)),
	createdby nvarchar(50) DEFAULT (NULL),
	modifiedby nvarchar(50) DEFAULT (NULL),
	createdts datetime DEFAULT (NULL),
	lastmodifiedts datetime DEFAULT (NULL),
	contractId varchar(20) DEFAULT (NULL),
	coreCustomerId varchar(20) DEFAULT (NULL),
	isBusinessAccount varchar(20) DEFAULT (NULL),
	email varchar(50) DEFAULT (NULL),
	EStatementmentEnable smallint DEFAULT ((0)),
	accountType varchar(50),
	CONSTRAINT PK_excludedcustomeraccounts_id PRIMARY KEY (id)
);
GO

--ALTER TABLE [${dbxschemaname}].[contractcustomers] ADD autoSyncAccounts BIT DEFAULT 0 NOT NULL;

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
		DECLARE @excluded_accounts_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomers] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customeraccounts] where ';
		SET @excluded_accounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomeraccounts] where ';
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
			SET @excluded_accounts_statement = @excluded_accounts_statement + @where_clause1;			
			SET @group_statement = @group_statement + @where_clause1;	
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause1;	
		
			exec(@contract_statement);
			exec(@accounts_statement);
			exec(@excluded_accounts_statement);
			exec(@group_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO

CREATE TABLE [${dbxschemaname}].[excludedcustomroleaccounts] (
	id varchar(50) NOT NULL,
	customRoleId varchar(50) DEFAULT (NULL),
	Account_id varchar(50) DEFAULT (NULL),
	AccountName varchar(50) DEFAULT (NULL),
	contractId varchar(50) DEFAULT (NULL),
	coreCustomerId varchar(45) DEFAULT (NULL),
	createdby varchar(50) DEFAULT (NULL),
	modifiedby varchar(50) DEFAULT (NULL),
	createdts datetime2(7) DEFAULT (NULL),
	lastmodifiedts datetime2(7) DEFAULT (NULL),
	synctimestamp datetime2(7) DEFAULT (NULL),
	softdeleteflag bit DEFAULT ((0)) NOT NULL,
	CONSTRAINT PK__customro__3213E83FC1DF4F66 PRIMARY KEY (id)
);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customrole_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customrole_contract_delete_proc]  
   @customRoleId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
		DECLARE @contract_statement nvarchar(max);
		DECLARE @accounts_statement nvarchar(max);
		DECLARE @excludedaccounts_statement nvarchar(max);
		DECLARE @action_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		DECLARE @where_clause2 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomrole] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customroleaccounts] where ';
		SET @excludedaccounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomroleaccounts] where ';
		SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customroleactionlimits] where ';
		SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where ';

		SET @where_clause = '';
		SET @where_clause1 = '';
		if(@customRoleId != '') 
		BEGIN
			SET @where_clause = @where_clause + (N' and customRoleId = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and customRole_id = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and Customer_id = ') + ((QUOTENAME((@customRoleId), '''')))
			
		END
		
		IF(@contractId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and contractId = ') + ((QUOTENAME((@contractId), '''')))
		END
		
		IF(@coreCustomerId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause2 = @where_clause2 + (N' and coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
		END
		
		IF(@where_clause != '')
		BEGIN	
			SET @contract_statement = @contract_statement + @where_clause;	
			SET @accounts_statement = @accounts_statement + @where_clause;
			SET @excludedaccounts_statement = @excludedaccounts_statement + @where_clause;
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause2;	
		
			exec(@contract_statement);
			exec(@accounts_statement);
			exec(@excludedaccounts_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO

ALTER TABLE [${dbxschemaname}].[excludedcustomroleaccounts] ADD accountType varchar(50)
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_customers_proc]
        @_customerId nvarchar(50),
        @_coreCustomerId nvarchar(50)
    AS BEGIN
        declare @select_statement nvarchar(MAX);
        declare @isWhereAppened nvarchar(100);
        declare @shouldAndAppend nvarchar(100);
        SET @select_statement = ('(SELECT 
            contractcustomers.customerId AS customerId,
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
        set @select_statement =  concat(@select_statement ,');');
        exec(@select_statement);
    END;
GO	

DROP PROCEDURE IF EXISTS [${dbxschemaname}].default_autosync_accounts_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].default_autosync_accounts_create_proc(
                                @_customerId nvarchar(50),
        @_queryInput nvarchar(max)
    )AS BEGIN
        DECLARE  @index INT = 0
        DECLARE  @recordsData nvarchar(255) = N''
        DECLARE  @numOfRecords varchar(255) = N''
        DECLARE  @coreCustomerId varchar(255) = N''
        DECLARE  @accountId varchar(255) = N''
        DECLARE  @arrangementId varchar(255) = N''
        DECLARE  @accountName varchar(255) = N''
        DECLARE  @accountType varchar(255) = N''
        DECLARE  @contractId varchar(255) = N''
        DECLARE  @typeId varchar(255) = N''
        DECLARE  @contractaccounts varchar(255) = N''
                                DECLARE  @customerAccounts varchar(255) = N''
        DECLARE  @id varchar(255) = N''
        SET @numOfRecords = datalength(@_queryInput) - datalength(replace(@_queryInput, N'|', N'')) + 1
        WHILE (1 = 1) BEGIN
              set @index = @index + 1;
              IF @index = @numOfRecords + 1 
                BREAK
              else BEGIN
                set @recordsData = ([${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1));
                set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',1), ':', -1 );    
                set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',2), ':', -1 );
                set @arrangementId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',3), ':', -1 );
                set @accountName = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',4), ':', -1 );
                set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',5), ':', -1 );
                SET @contractId = (select [${dbxschemaname}].[contractcorecustomers].[contractId] from 
                [${dbxschemaname}].[contractcorecustomers] where [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] = @coreCustomerId);
                SET @typeId = (select [${dbxschemaname}].[accounttype].[TypeID] from [${dbxschemaname}].[accounttype] where [${dbxschemaname}].[accounttype].[TypeDescription]= @accountType);
                
                                                                
                SET @contractaccounts = (SELECT [${dbxschemaname}].[contractaccounts].[id] from [${dbxschemaname}].[contractaccounts] where [${dbxschemaname}].[contractaccounts].[coreCustomerId] = @coreCustomerId
                and [${dbxschemaname}].[contractaccounts].[accountId] = @accountId);
                if(ISNULL(@contractaccounts,'') = '') begin
                    SET @id = (SELECT left(newid(), 50))
                                    INSERT INTO [${dbxschemaname}].[contractaccounts](
                        [${dbxschemaname}].[contractaccounts].[id],
                        [${dbxschemaname}].[contractaccounts].[contractId],
                        [${dbxschemaname}].[contractaccounts].[accountId],
                        [${dbxschemaname}].[contractaccounts].[accountName],
                        [${dbxschemaname}].[contractaccounts].[typeId],
                        [${dbxschemaname}].[contractaccounts].[coreCustomerId],
                       [${dbxschemaname}].[contractaccounts].[ownerType],
                        [${dbxschemaname}].[contractaccounts].[statusDesc],
                        [${dbxschemaname}].[contractaccounts].[arrangementId])
                        VALUES (@id,@contractId,@accountId,@accountName,@typeId,@coreCustomerId,'Owner','Active',@arrangementId);
                end;

                                                                SET @customerAccounts = (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] from [${dbxschemaname}].[customeraccounts]
                                                                                                                                                                                where [${dbxschemaname}].[customeraccounts].[Account_id] = @accountId);
                                                                if(ISNULL(@customerAccounts,'') = '') begin
                                                                                SET @id = (SELECT left(newid(), 50))
                                                                                INSERT INTO [${dbxschemaname}].customeraccounts(
                                                                                [${dbxschemaname}].[customeraccounts].[id],
                                                                                [${dbxschemaname}].[customeraccounts].[Customer_id],
                                                                                [${dbxschemaname}].[customeraccounts].[Account_id],
                                                                                [${dbxschemaname}].[customeraccounts].[AccountName],
                                                                                [${dbxschemaname}].[customeraccounts].[contractId],
                                                                                [${dbxschemaname}].[customeraccounts].[coreCustomerId],
                                                                                [${dbxschemaname}].[customeraccounts].[accountType])
                                                                                VALUES (@id,@_customerId,@accountId,@accountName,@contractId,@coreCustomerId,@accountType);
                                                                                EXEC [${dbxschemaname}].user_account_default_actions_create_proc @_customerId,@accountId,@coreCustomerId,@contractId,'';
                                                                                END
                END
            END
    END
    GO
    
    ALTER PROCEDURE [${dbxschemaname}].user_account_default_actions_create_proc  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureActionId varchar(255) = N''

      DECLARE
         @actionslist varchar(max) = N''

      DECLARE
         @limitId varchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @accountId varchar(255) = N''

      DECLARE
         @actualLimitId varchar(255) = N''
         
      DECLARE
         @serviceDefinitionId varchar(255) = N''
         
      DECLARE
         @serviceType varchar(255) = N''
         
      DECLARE
         @validFIActions varchar(max) = N''
         
      DECLARE
         @validServiceDefinitionActions varchar(max) = N''
         
      DECLARE
         @validGroupActions varchar(max) = N''
         
      DECLARE
         @validActionsList varchar(max) = N''
         
      DECLARE
         @groupId varchar(255) = N''
         
       DECLARE
         @featureId varchar(255) = N''

      DECLARE
         @limitvalue varchar(255) = N''

      DECLARE
         @id varchar(255) = N''
         
         
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
      [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
      [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
      [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
      
      

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (                                                                                 
            SELECT String_agg(CAST([${dbxschemaname}].[customeraccounts].[Account_id] AS nvarchar(max)), ',') FROM [${dbxschemaname}].[customeraccounts] WHERE 
                               [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
                               [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
                               [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId
             )
                                                                                                                                
    SET @serviceDefinitionId = (SELECT [${dbxschemaname}].[contract].[servicedefinitionId] from [${dbxschemaname}].[contract] WHERE [${dbxschemaname}].[contract].[id] = @_contractId)
    SET @serviceType = (SELECT [${dbxschemaname}].[servicedefinition].[serviceType] from [${dbxschemaname}].[servicedefinition] WHERE [${dbxschemaname}].[servicedefinition].[id] = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
                        WHERE [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = @serviceDefinitionId AND [${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = '1')
                                                                                                                                                                                                                                
    SET @validFIActions =  (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction)
                                                                                                                                                                                                                                                                
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                        
    SET @_groupId =  (SELECT [${dbxschemaname}].groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  
                                                                                                                                                                                
    SET @validGroupActions =  (SELECT String_agg(CAST([${dbxschemaname}].groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
                                                                                                                                                                                                
    SET @validActionsList =  (SELECT String_agg(CAST([${dbxschemaname}].contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
                DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].[featureaction] where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].[featureaction].[isAccountLevel] = '1' or [${dbxschemaname}].[featureaction].[isAccountLevel] = 'true'));

    OPEN accounts     
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
    OPEN actions
    FETCH NEXT FROM actions into @featureActionId
                
      WHILE (@@FETCH_STATUS=0)
          BEGIN                                                                        
          SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
          SET @entryStatus = 0
                                  DECLARE limits CURSOR LOCAL
      FOR (select LimitType_id from [${dbxschemaname}].[actionlimit] where Action_id = @featureActionId);
    OPEN limits
    FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN                                                       
            SET @limitvalue = (SELECT [${dbxschemaname}].contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE [${dbxschemaname}].contractactionlimit.actionId = @featureActionId AND 
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId AND
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId)

            IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
               SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
               SET @id = (SELECT left(newid(), 50))
               INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].[customeraction].[id], 
                                             [${dbxschemaname}].[customeraction].[RoleType_id], 
                                             [${dbxschemaname}].[customeraction].[Customer_id], 
                                             [${dbxschemaname}].[customeraction].[contractId], 
                                             [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                             [${dbxschemaname}].[customeraction].[featureId], 
                                             [${dbxschemaname}].[customeraction].[Action_id], 
                                             [${dbxschemaname}].[customeraction].[Account_id], 
                                             [${dbxschemaname}].[customeraction].[isAllowed], 
                                             [${dbxschemaname}].[customeraction].[LimitType_id], 
                                             [${dbxschemaname}].[customeraction].[value])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
               SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
             IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
             SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
             SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                           [${dbxschemaname}].[customeraction].[RoleType_id], 
                                           [${dbxschemaname}].[customeraction].[Customer_id], 
                                           [${dbxschemaname}].[customeraction].[contractId], 
                                           [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                           [${dbxschemaname}].[customeraction].[featureId],
                                           [${dbxschemaname}].[customeraction].[Action_id], 
                                           [${dbxschemaname}].[customeraction].[Account_id], 
                                           [${dbxschemaname}].[customeraction].[isAllowed], 
                                           [${dbxschemaname}].[customeraction].[LimitType_id], 
                                           [${dbxschemaname}].[customeraction].[value])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
             SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
             END
             END
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                                      
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue) 
                                                                 
          SET @entryStatus = 1
          FETCH NEXT FROM limits into @limitId
          CONTINUE
          END
          CLOSE limits
          DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
          SET @id =(SELECT left(newid(), 50))
          INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                        [${dbxschemaname}].[customeraction].[RoleType_id], 
                                        [${dbxschemaname}].[customeraction].[Customer_id], 
                                        [${dbxschemaname}].[customeraction].[contractId], 
                                        [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                        [${dbxschemaname}].[customeraction].[featureId], 
                                        [${dbxschemaname}].[customeraction].[Action_id], 
                                        [${dbxschemaname}].[customeraction].[Account_id], 
                                        [${dbxschemaname}].[customeraction].[isAllowed])
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1);
           END
           SET @actionslist = @featureActionId + N',' + @actionslist
           FETCH NEXT FROM actions into @featureActionId
           CONTINUE
           END
           CLOSE actions
           DEALLOCATE actions
           FETCH NEXT FROM accounts into @accountId
           CONTINUE
           END
           CLOSE accounts
           DEALLOCATE accounts
END;
GO
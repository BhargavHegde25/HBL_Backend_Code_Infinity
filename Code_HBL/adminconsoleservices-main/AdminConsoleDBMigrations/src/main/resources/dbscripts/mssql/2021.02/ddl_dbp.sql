ALTER TABLE [${dbxschemaname}].[alerthistory] 
DROP CONSTRAINT [alerthistory$FK_alerthistory_alertsubtype];
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] 
ADD [coreCustomerId] VARCHAR(45);
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ALTER COLUMN EventId int NULL
GO
ALTER TABLE [${dbxschemaname}].[alerthistory]
ADD DEFAULT(NULL) FOR [EventId]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ALTER COLUMN AlertCategoryId nvarchar(50) NULL
GO
ALTER TABLE [${dbxschemaname}].[alerthistory]
ADD DEFAULT(NULL) FOR [AlertCategoryId]
GO
ALTER TABLE [${dbxschemaname}].[alerthistory]
DROP CONSTRAINT [alerthistory$FK_alerthistory_customer];
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] ALTER COLUMN Customer_Id nvarchar(50) NULL
GO
ALTER TABLE [${dbxschemaname}].[alerthistory] 
ADD DEFAULT(NULL) FOR [Customer_Id];
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[wealthuserpreferences];
CREATE TABLE [${dbxschemaname}].[wealthuserpreferences] (
[id] nvarchar(50) NOT NULL,
[userId] nvarchar(50) DEFAULT NULL,
[portfolioId] nvarchar(50) DEFAULT NULL,
[fieldOrder] nvarchar(150) DEFAULT NULL,
[createdby] nvarchar(50) DEFAULT NULL,
[modifiedby] nvarchar(50) DEFAULT NULL,
[createdts] datetime DEFAULT GETDATE(),
[lastmodifiedts] datetime DEFAULT GETDATE(),
[synctimestamp] datetime DEFAULT GETDATE(),
[softdeleteflag] bit DEFAULT 0,
PRIMARY KEY ([id]),
CONSTRAINT [FK1_wealthuserpreferences_userId] FOREIGN KEY ([userId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[favouriteinstruments];
CREATE TABLE [${dbxschemaname}].[favouriteinstruments] (
[id] int NOT NULL IDENTITY,
[userId] int DEFAULT NULL,
[customerId] nvarchar(50) DEFAULT NULL,
[favInstrumentCodes] nvarchar(2000) DEFAULT NULL,
[softdeleteflag] bit DEFAULT 0,
PRIMARY KEY ([id]),
CONSTRAINT FavoInstruments_UserId UNIQUE ([userId]),
CONSTRAINT [FK_FavoInstruments_UserId] FOREIGN KEY ([userId]) REFERENCES [${dbxschemaname}].[user] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
CONSTRAINT [FK_FavoInstruments_CustomerId] FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO

CREATE INDEX [FavoInstruments_CustomerId] ON [${dbxschemaname}].[favouriteinstruments]([customerId]);
GO


ALTER TABLE [${dbxschemaname}].[contractcorecustomers] add [implicitAccountAccess] smallint  NOT NULL DEFAULT 0;
GO

CREATE TABLE 
[${dbxschemaname}].[excludedcontractaccounts]
(
   [id] nvarchar(50)  NOT NULL,
   [contractId] nvarchar(50)  NULL,
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
 
	CONSTRAINT PK_excludedcontractaccounts_id PRIMARY KEY ([id]),
	CONSTRAINT [excludedcontractaccounts_accountId_UNIQUE] UNIQUE  ([accountId])
)
GO

ALTER TABLE [${dbxschemaname}].[contractcustomers] ADD autoSyncAccounts BIT DEFAULT 0 NOT NULL;
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_associated_corecustomeraccounts_info;
GO
CREATE PROCEDURE [${dbxschemaname}].[user_associated_corecustomeraccounts_info](
     @customerId nvarchar(50)
)AS BEGIN
    DECLARE @implictCIF NVARCHAR(MAX);
    SET @implictCIF = (SELECT String_agg(CAST([${dbxschemaname}].[contractcustomers].[coreCustomerId] AS nvarchar(max)), ',')
    FROM [${dbxschemaname}].[contractcustomers]
    WHERE [${dbxschemaname}].[contractcustomers].[customerId] = @customerId
    AND [${dbxschemaname}].[contractcustomers].[autoSyncAccounts] = '1' );
    
    SELECT [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] , [${dbxschemaname}].[contractcorecustomers].[contractId]  from 
    [${dbxschemaname}].[contractcorecustomers] where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[contractcorecustomers].[coreCustomerId] , @implictCIF) > 0;
    
    IF(ISNULL(@customerId,'') != '') BEGIN
        SELECT [customeraccounts].[Account_id] AS nonCIFAccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId
        AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].[coreCustomerId] , @implictCIF) = 0 ;
        
        SELECT [${dbxschemaname}].[contractaccounts].[accountId] AS contractaccounts
        FROM [${dbxschemaname}].[contractaccounts]
        WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[contractaccounts].[coreCustomerId] , @implictCIF) > 0 ; 
        
        SELECT [${dbxschemaname}].[excludedcontractaccounts].[accountId] AS excludedcontractaccounts
        FROM [${dbxschemaname}].[excludedcontractaccounts]
        WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[excludedcontractaccounts].[coreCustomerId] , @implictCIF) > 0 ; 
        
        SELECT [${dbxschemaname}].[customeraccounts].[Account_id] AS customeraccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].[coreCustomerId] , @implictCIF) > 0 
        AND [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId; 
        
        SELECT [${dbxschemaname}].[excludedcustomeraccounts].[Account_id] AS excludedcustomeraccounts
        FROM [${dbxschemaname}].[excludedcustomeraccounts]
        WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[excludedcustomeraccounts].[coreCustomerId] , @implictCIF) > 0 
        AND [${dbxschemaname}].[excludedcustomeraccounts].[Customer_id] = @customerId; 
    END;
    ELSE BEGIN
        SELECT [${dbxschemaname}].[customeraccounts].[Account_id] AS nonCIFAccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId;
    END;
END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].user_account_default_actions_create_proc  
	@_customerId nvarchar(50),
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
      FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE --schema 
      [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
      [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
      [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
      DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].[featureaction] where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].[featureaction].[isAccountLevel] = '1'));
      DECLARE limits CURSOR LOCAL
      FOR (select LimitType_id from [${dbxschemaname}].[actionlimit] where Action_id = @featureActionId);

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
                             [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId)  

													
    SET @validGroupActions =  (SELECT String_agg(CAST([${dbxschemaname}].groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
												
    SET @validActionsList =  (SELECT String_agg(CAST([${dbxschemaname}].contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
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
                if(ISNULL(@contractaccounts,'') != '') begin
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



ALTER TABLE [${dbxschemaname}].[alerthistory]
ADD DEFAULT(NULL) FOR [ChannelId]
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_default_account_actions_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_default_account_actions_proc](
    @_userId NVARCHAR(50),
    @_coreCustomerId NVARCHAR(50)
)AS BEGIN
    DECLARE @select_statement nvarchar(MAX);
    DECLARE @_contractId nvarchar(50);
    DECLARE @serviceDefinitionId nvarchar(50);
    DECLARE @serviceType nvarchar(50);
    DECLARE @_groupId nvarchar(50);
    SET @select_statement = 'select String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), '','') as defaultAccountActions from [${dbxschemaname}].featureaction where 
    ([${dbxschemaname}].featureaction.isAccountLevel = ''1'' OR [${dbxschemaname}].featureaction.isAccountLevel = ''true'') 
    AND featureaction.id IN ';
        
    SET @_contractId = (select [${dbxschemaname}].contractcorecustomers.contractId from [${dbxschemaname}].contractcorecustomers where 
    [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId);
    
    SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @_contractId);
    SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId);
    SET @_groupId = (select customergroup.Group_id from customergroup where customergroup.Customer_id = @_userId
                    and customergroup.coreCustomerId = @_coreCustomerId);
                            
    SET @select_statement = CONCAT(@select_statement , '(SELECT actionId FROM [${dbxschemaname}].servicedefinitionactionlimit
                        WHERE [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId= ','''',@serviceDefinitionId,'''','
                        AND [${dbxschemaname}].servicedefinitionactionlimit.actionId IN ');

    SET @select_statement = CONCAT(@select_statement , ' ( SELECT Action_id FROM [${dbxschemaname}].groupactionlimit WHERE [${dbxschemaname}].groupactionlimit.Group_id = ',
                                                                '''',@_groupId,'''','
                                                                AND [${dbxschemaname}].groupactionlimit.Action_id IN ');

    SET @select_statement = CONCAT(@select_statement ,' ( SELECT actionId  FROM [${dbxschemaname}].contractactionlimit WHERE 
                                [${dbxschemaname}].contractactionlimit.contractId = ','''',@_contractId,'''','
                                AND [${dbxschemaname}].contractactionlimit.coreCustomerId = ','''',@_coreCustomerId,'''');
    SET @select_statement = CONCAT(@select_statement ,')))');
    
    EXEC(@select_statement);
END
GO

ALTER TABLE [${dbxschemaname}].[credentialchecker] ADD CONSTRAINT CC_retryCount DEFAULT N'0' FOR retryCount;
ALTER TABLE [${dbxschemaname}].[card]
ADD [protectionEnabled] TINYINT NULL DEFAULT 0;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_associated_contractaccounts_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[get_associated_contractaccounts_proc](
	@_accountIdList NVARCHAR(50)
) AS BEGIN

    DECLARE @accountIdList NVARCHAR(MAX);
    DECLARE @excludedaccountIdList NVARCHAR(MAX);
    
	SET @accountIdList = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].FIND_IN_SET([contractaccounts].[accountId],@_accountIdList) > 0);
							
	SET @excludedaccountIdList = (SELECT STRING_AGG([excludedcontractaccounts].[accountId], ',') from [${dbxschemaname}].[excludedcontractaccounts] 
							WHERE [${dbxschemaname}].FIND_IN_SET([excludedcontractaccounts].[accountId],@_accountIdList) > 0);
							
	select @accountIdList As accountIdList;
	select @excludedaccountIdList As excludedaccountIdList;
END
GO

ALTER TABLE [${dbxschemaname}].[application] ADD [customerCreationMode] VARCHAR(50) NOT NULL CHECK ([customerCreationMode] IN('WITHOUT-RECORD', 'WITH-RECORD', 'HYBRID')) DEFAULT 'WITHOUT-RECORD';
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [${dbxschemaname}].[fetch_user_corecustomer_actions]
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
                                on ([${dbxschemaname}].contractcorecustomers.contractId = '''' + [${dbxschemaname}].contract.id + '''') 
                                where [${dbxschemaname}].contractcorecustomers.coreCustomerId = '''' + @_coreCustomerId + '''');

    SET @_roleId = (select String_agg(CAST([${dbxschemaname}].customergroup.Group_id AS nvarchar(max)), ',') from [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = '''' + @_userId + ''''
                    and [${dbxschemaname}].customergroup.coreCustomerId = '''' + @_coreCustomerId + '''');

    SET @action_select_statement = ('(SELECT DISTINCT [${dbxschemaname}].featureaction.id AS actionId FROM [${dbxschemaname}].featureaction LEFT JOIN [${dbxschemaname}].feature ON ( [${dbxschemaname}].feature.id = [${dbxschemaname}].featureaction.Feature_id ))');
    IF(@_serviceDefinitionId != '') BEGIN 
        SET @select_statement = CONCAT('(SELECT DISTINCT [${dbxschemaname}].servicedefinitionactionlimit.actionId AS actionId FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = ' ,''''+ @_serviceDefinitionId+'''');
        SET @select_statement = CONCAT(@select_statement , ' AND [${dbxschemaname}].servicedefinitionactionlimit.actionId IN' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
    IF(@_roleId != '') BEGIN
        SET @select_statement = CONCAT('(SELECT DISTINCT [${dbxschemaname}].groupactionlimit.Action_id AS actionId FROM [${dbxschemaname}].groupactionlimit WHERE [${dbxschemaname}].groupactionlimit.Group_id = ','''' + @_roleId+'''');
        SET @select_statement = CONCAT(@select_statement , ' AND [${dbxschemaname}].groupactionlimit.Action_id IN ');
        SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
    IF(@_coreCustomerId != '') BEGIN 
        SET @select_statement = CONCAT('(SELECT DISTINCT [${dbxschemaname}].contractactionlimit.actionId AS actionId FROM [${dbxschemaname}].contractactionlimit WHERE [${dbxschemaname}].contractactionlimit.coreCustomerId = ', '''' + @_coreCustomerId +'''');
        SET @select_statement = CONCAT(@select_statement , ' AND [${dbxschemaname}].contractactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
    IF(@_userId != '') BEGIN 
        SET @select_statement = CONCAT('(SELECT DISTINCT [${dbxschemaname}].customeraction.Action_id AS actionId FROM [${dbxschemaname}].customeraction WHERE [${dbxschemaname}].customeraction.Customer_id = ', '''' + @_userId +'''');
        IF(@_coreCustomerId != '') BEGIN 
            SET @select_statement = CONCAT(@select_statement , ' AND [${dbxschemaname}].customeraction.coreCustomerId = ' ,''''+  @_coreCustomerId +'''');
        END
        SET @select_statement = CONCAT(@select_statement , ' AND [${dbxschemaname}].customeraction.isAllowed = 1 AND [${dbxschemaname}].customeraction.Action_id IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END
   EXEC(@select_statement)
END
GO
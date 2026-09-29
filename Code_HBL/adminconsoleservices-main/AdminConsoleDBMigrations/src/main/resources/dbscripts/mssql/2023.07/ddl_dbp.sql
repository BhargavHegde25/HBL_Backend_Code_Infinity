ALTER TABLE [${dbxschemaname}].[excludedcontractaccounts] DROP CONSTRAINT [excludedcontractaccounts_accountId_UNIQUE]
GO

SET ANSI_PADDING ON
GO

ALTER TABLE [${dbxschemaname}].[excludedcontractaccounts] ADD  CONSTRAINT [excludedcontractaccounts_accountId_UNIQUE] UNIQUE NONCLUSTERED 
(
	[accountId],[contractId],[coreCustomerId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO




DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contractaccounts_exists_proc];
GO

CREATE  PROCEDURE [${dbxschemaname}].[contractaccounts_exists_proc]  
   @_accountsCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

		SELECT [contractaccounts].[accountId] AS accountId, [contractaccounts].[coreCustomerId] as coreCustomerId
        FROM [${dbxschemaname}].[contractaccounts]
        WHERE [${dbxschemaname}].[contractaccounts].[accountId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@_accountsCSV, ',')); 
       
     END
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[excludedcontractaccounts_delete_proc];
GO


CREATE  PROCEDURE [${dbxschemaname}].[excludedcontractaccounts_delete_proc](
 
  @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_accountId nvarchar(50)
)AS
BEGIN

DELETE 
      FROM [${dbxschemaname}].excludedcontractaccounts
      WHERE 
        [${dbxschemaname}].excludedcontractaccounts.contractId = @_contractId AND 
         [${dbxschemaname}].excludedcontractaccounts.accountId = @_accountId AND 
         [${dbxschemaname}].excludedcontractaccounts.coreCustomerId = @_coreCustomerId
END
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_valid_customeraccounts_get_proc];
GO

CREATE PROCEDURE  [${dbxschemaname}].[get_valid_customeraccounts_get_proc]  
   @_customeraccountsCSV nvarchar(max),
   @_customerId nvarchar(max),
   @_coreCustomerId nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE
         @list nvarchar(max) = N''
	  DECLARE
         @validcorecustomersCSV nvarchar(max) = N''
	 DECLARE
         @next nvarchar(max) = N''
	 DECLARE
         @nextlen int
     DECLARE
         @initiallength int
	DECLARE
         @value nvarchar(max) = N''
	DECLARE
         @customers nvarchar(max) = N''
	DECLARE
         @stringLength nvarchar(max) = N''      
      SET @list = 
         (
            SELECT @_customeraccountsCSV
         )
      SET @validcorecustomersCSV = N'' 
	 
      WHILE (1 = 1)      
         BEGIN            
            IF datalength(LTRIM(RTRIM(@list))) = 0 OR @list IS NULL
               BREAK           
		   SET @initiallength = LEN(@list)
            SET @next = [${dbxschemaname}].SUBSTRING_INDEX(@list, N',', 1)           
            SET @nextlen = LEN(@next)        
            SET @value = LTRIM(RTRIM(@next))               
            SET @customers =  (SELECT String_agg(CAST(customeraccounts.Account_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraccounts
                             where [${dbxschemaname}].customeraccounts.Account_id = @value and  [${dbxschemaname}].customeraccounts.Customer_id = @_customerId
							 and  [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId) 
							 
	      IF ISNULL(@customers,'')='' OR @customers =''
	         BEGIN
	            IF @validcorecustomersCSV = ''
                  SET @validcorecustomersCSV = @value
                ELSE 
                  SET @validcorecustomersCSV = (@validcorecustomersCSV) + (N',') + (@value)
             END                 
	    SET @stringLength = LEN(@list)
		IF @nextlen+2 < @initiallength
		    SET @list = SUBSTRING(@list,@nextlen + 2,@stringLength-@nextlen-1) 
		ELSE 
		    SET @list = ''
     END
	  SELECT @validcorecustomersCSV AS validAccounts
   END
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_associated_corecustomeraccounts_info];
GO


CREATE PROCEDURE [${dbxschemaname}].[user_associated_corecustomeraccounts_info](
     @customerId nvarchar(100)
)
AS BEGIN
    DECLARE @implictCIF NVARCHAR(MAX);
    DECLARE @accountIds NVARCHAR(MAX);
    
	SET @implictCIF = (SELECT 
            String_agg(CAST([contractcustomers].[coreCustomerId] AS nvarchar(max)), ',')
            FROM [${dbxschemaname}].[contractcustomers]
            WHERE [${dbxschemaname}].[contractcustomers].[customerId] = @customerId AND [${dbxschemaname}].[contractcustomers].[autoSyncAccounts] = '1' );

	
    SELECT 
               [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] , 
               [${dbxschemaname}].[contractcorecustomers].[contractId]  
               from 
    [${dbxschemaname}].[contractcorecustomers] 
               where 
               [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','));
    
    IF(ISNULL(@customerId,'') != '') BEGIN
	
        SELECT [customeraccounts].[Account_id] AS nonCIFAccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId
        AND [${dbxschemaname}].[customeraccounts].[coreCustomerId] NOT IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','));        
                              
        SELECT [contractaccounts].[accountId] AS contractaccounts, [contractaccounts].[coreCustomerId] as contractcustomer
        FROM [${dbxschemaname}].[contractaccounts]
        WHERE [${dbxschemaname}].[contractaccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ',')); 
        
        SELECT [excludedcontractaccounts].[accountId] AS excludedcontractaccounts,[excludedcontractaccounts].[coreCustomerId] as contractcustomer
        FROM [${dbxschemaname}].[excludedcontractaccounts]
        WHERE [${dbxschemaname}].[excludedcontractaccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','));
                                
        SELECT [${dbxschemaname}].[customeraccounts].[Account_id] AS customeraccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId; 
        
        SELECT [${dbxschemaname}].[excludedcustomeraccounts].[Account_id] AS excludedcustomeraccounts
        FROM [${dbxschemaname}].[excludedcustomeraccounts]
        WHERE [${dbxschemaname}].[excludedcustomeraccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','))
        AND [${dbxschemaname}].[excludedcustomeraccounts].[Customer_id] = @customerId; 
    END;
    ELSE BEGIN
        SELECT [${dbxschemaname}].[customeraccounts].[Account_id] AS nonCIFAccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId;
    END;
               
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
		DECLARE  @ownertype varchar(255) = N''
		DECLARE  @legalEntityId varchar(255) = N''
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
                set @ownertype = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',6), ':', -1 );
                set @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',7), ':', -1 );
                
                SET @contractId = (select [${dbxschemaname}].[contractcorecustomers].[contractId] from 
                [${dbxschemaname}].[contractcorecustomers] where [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] = @coreCustomerId);
                SET @typeId = (select [${dbxschemaname}].[accounttype].[TypeID] from [${dbxschemaname}].[accounttype] where [${dbxschemaname}].[accounttype].[TypeDescription]= @accountType);
                
				
                SET @contractaccounts = (SELECT [${dbxschemaname}].[contractaccounts].[id] from [${dbxschemaname}].[contractaccounts] where [${dbxschemaname}].[contractaccounts].[coreCustomerId] = @coreCustomerId
                and [${dbxschemaname}].[contractaccounts].[accountId] = @accountId
                and [${dbxschemaname}].[contractaccounts].[companyLegalUnit] = @legalEntityId );
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
                        [${dbxschemaname}].[contractaccounts].[arrangementId],
                        [${dbxschemaname}].[contractaccounts].[companyLegalUnit])
                        VALUES (@id,@contractId,@accountId,@accountName,@typeId,@coreCustomerId,'Owner','Active',@arrangementId,@legalEntityId);
                end;

				SET @customerAccounts = (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] from [${dbxschemaname}].[customeraccounts]
											where [${dbxschemaname}].[customeraccounts].[Account_id] = @accountId
											and [${dbxschemaname}].[customeraccounts].[companyLegalUnit] = @legalEntityId);
				 if(ISNULL(@customerAccounts,'') = '') begin
					SET @id = (SELECT left(newid(), 50))
					INSERT INTO [${dbxschemaname}].customeraccounts(
					[${dbxschemaname}].[customeraccounts].[id],
					[${dbxschemaname}].[customeraccounts].[Customer_id],
					[${dbxschemaname}].[customeraccounts].[Account_id],
					[${dbxschemaname}].[customeraccounts].[AccountName],
					[${dbxschemaname}].[customeraccounts].[contractId],
					[${dbxschemaname}].[customeraccounts].[coreCustomerId],
					[${dbxschemaname}].[customeraccounts].[accountType],
					[${dbxschemaname}].[customeraccounts].[companyLegalUnit])
					 VALUES (@id,@_customerId,@accountId,@accountName,@contractId,@coreCustomerId,@accountType,@legalEntityId);
					 EXEC [${dbxschemaname}].user_account_default_actions_create_proc @_customerId,@accountId,@coreCustomerId,@contractId,'',@legalEntityId;
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
   @_groupId nvarchar(50),
   @_legalEntityId nvarchar(50)
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
                                             [${dbxschemaname}].[customeraction].[value],
                                             [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00,@_legalEntityId)
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
                                           [${dbxschemaname}].[customeraction].[value],
                                           [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00,@_legalEntityId)
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
                                      [${dbxschemaname}].[customeraction].[value],
                                      [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue,@_legalEntityId)
                                      
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
                                      [${dbxschemaname}].[customeraction].[value],
                                      [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue,@_legalEntityId) 
                                                                 
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
                                        [${dbxschemaname}].[customeraction].[isAllowed],
                                        [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@_legalEntityId);
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



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_validcorecustomerslist_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[get_validcorecustomerslist_proc]  
   @_coreCustomersCSV nvarchar(max),
   @companyLegalUnit nvarchar(200)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE
         @list nvarchar(max) = N''

	  DECLARE
         @validcorecustomersCSV nvarchar(max) = N''

	 DECLARE
         @next nvarchar(max) = N''

	 DECLARE
         @nextlen int

     DECLARE
         @initiallength int

	DECLARE
         @value nvarchar(max) = N''

	DECLARE
         @customers nvarchar(max) = N''

	DECLARE
         @stringLength nvarchar(max) = N''
      
      SET @list = 
         (
            SELECT @_coreCustomersCSV
         )
      SET @validcorecustomersCSV = N''

	 
	 
      WHILE (1 = 1)
      
         BEGIN

            
            IF datalength(LTRIM(RTRIM(@list))) = 0 OR @list IS NULL
               BREAK
           
		   SET @initiallength = LEN(@list)
            SET @next = [${dbxschemaname}].SUBSTRING_INDEX(@list, N',', 1)
           
            SET @nextlen = LEN(@next)
        
            SET @value = LTRIM(RTRIM(@next))
               
            SET @customers =  (SELECT String_agg(CAST(contractcorecustomers.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @value and [${dbxschemaname}].contractcorecustomers.companyLegalUnit = @companyLegalUnit)    
              
	      IF ISNULL(@customers,'')='' OR @customers =''
	         BEGIN
	            IF @validcorecustomersCSV = ''
                  SET @validcorecustomersCSV = @value
                ELSE 
                  SET @validcorecustomersCSV = (@validcorecustomersCSV) + (N',') + (@value)
             END     
            
	    SET @stringLength = LEN(@list)
		IF @nextlen+2 < @initiallength
		    SET @list = SUBSTRING(@list,@nextlen + 2,@stringLength-@nextlen-1) 
		ELSE 
		    SET @list = ''

     END
	  SELECT @validcorecustomersCSV AS validCustomers
   END
GO

ALTER TABLE [${dbxschemaname}].[accountLevelActionLimit] ALTER COLUMN [isPortfolio] nvarchar(50)  NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS
   BEGIN -- proc level
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON

	  DECLARE @index int
	  DECLARE @query nvarchar(max)
	  DECLARE @numOfRecords int
	  DECLARE @id nvarchar(255) = N''
      DECLARE @tempLimitValue DECIMAL(20,2)
      DECLARE @group_concat_max_len bigint
	  DECLARE @serviceDefinitionId nvarchar(255) = N''
	  DECLARE @recordsData nvarchar(max) = N''
	  DECLARE @contractId nvarchar(max) = N''
	  DECLARE @customerId nvarchar(max) = N''
	  DECLARE @accountId nvarchar(max) = N''
	  DECLARE @featureId nvarchar(max) = N''
	  DECLARE @actionId nvarchar(max) = N''
	  DECLARE @isNewAction nvarchar(max)= N''
	  DECLARE @legalEntityId nvarchar(max)= N''
	  DECLARE @limitId nvarchar(max) = N''
	  DECLARE @limitValue nvarchar(max) = N''
	  DECLARE @limitAtFI nvarchar(max) = N''
	  DECLARE @contarctFeatures nvarchar(max) = N''  
	  DECLARE @limitATServiceDefinition nvarchar(max) = N''
	  DECLARE @recordsDataWithoutLimits nvarchar(max) = N'' 
	  DECLARE @serviceDefinitionActions nvarchar(max) = N'' 
	  DECLARE @existingActionLimitRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords1 nvarchar(max) = N''
	  DECLARE @existingAccountActionLimitRecords nvarchar(max) = N''

	  set @index = 0;
	  set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1; 
	  SET @_queryInput = REPLACE(@_queryInput, '"', ''''); 

	    WHILE (1 = 1)
      
         BEGIN -- while level

         set @index = @index + 1;

		  IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN -- outermost level

		   set @recordsData = concat('''',newid(),'''',',', [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 ));
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',2 ), '''', -1 );
		   set @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',3 ), ',''', -1 );   
		   set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',4 ), ',''', -1 ); 
		   set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',5), ',''', -1 );           
	       set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',6), ',''', -1 );           
	       set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',7), ',''', -1 ); 
		   set @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8), ',''', -1 );
	       set @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',9), ',''', -1 );          
	       set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ',''',-1 ), '''', 1 );              
	       set @recordsDataWithoutLimits = concat([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8),''''); 

		   IF @serviceDefinitionId is null or @serviceDefinitionId = '' BEGIN
				SET @serviceDefinitionId = (SELECT servicedefinitionId from [${dbxschemaname}].[contract] WHERE id = @contractId);
		   END 

		    IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN --111

					   SET @limitAtFI = (SELECT actionlimit.value FROM [${dbxschemaname}].[actionlimit] WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

					   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId        
					        AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
						AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);                   
					  					   

					   IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                     
                  END ; --111

                  IF 
                     CASE 
                        WHEN NOT 
                           CASE 
                              WHEN (@tempLimitValue) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE 
                           0
                     END <> 0 AND @tempLimitValue <> ''
                     SET @limitValue = @tempLimitValue


			SET @contarctFeatures = 
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId  AND 
							   [${dbxschemaname}].contractfeatures.companyLegalUnit = @legalEntityId  
                     )

		   SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND 
			 [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @legalEntityId 
                     ) 

             

			SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
                     )

					 SET @existingAccountActionLimitRecords = (SELECT String_agg(CAST(acl.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit acl WHERE acl.contractId = @contractId AND 
						acl.coreCustomerId = @customerId AND 
						acl.featureId = @featureId AND acl.actionId = @actionId AND 
						acl.limitTypeId = @limitId AND acl.companyLegalUnit = @legalEntityId);

					  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId 
                     )
					 
					  SET @existingActionRecords1 = 
                     (
                     			 
                        SELECT String_agg(CAST(accountlevelactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit WHERE 
                               [${dbxschemaname}].accountlevelactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @customerId AND
							   [${dbxschemaname}].accountlevelactionlimit.accountId =@accountId AND
                               [${dbxschemaname}].accountlevelactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].accountlevelactionlimit.actionId = @actionId AND 
								[${dbxschemaname}].accountlevelactionlimit.companyLegalUnit = @legalEntityId							   
                       
                     ) 

			IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> '' AND (@accountId IS NULL or @accountId = '')
                  
				  BEGIN -- 9
					IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
						ELSE
							BEGIN -- 6
							 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction,companyLegalUnit) VALUES (''') + (@recordsDataWithoutLimits) + (N''');')
                                  END
                                
							    
								   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction, contractactionlimit.companyLegalUnit,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@recordsData) + (N''');')
                                     
                                        END 
                                    END -- 6
							END  -- 9
					ELSE		
						BEGIN --11
							IF @existingAccountActionLimitRecords IS NOT NULL AND @existingAccountActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingAccountActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
					ELSE
					BEGIN -- 13

						 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords1 IS NULL OR @existingActionRecords1 = '')
                                     BEGIN
                                         SET @query =(N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,contractactionlimit.actionId, accountlevelactionlimit.isNewAction, accountlevelactionlimit.companyLegalUnit) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                    END
                                    
                           
							   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
                                           SET @query = (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,accountlevelactionlimit.actionId,accountlevelactionlimit.isNewAction,accountlevelactionlimit.companyLegalUnit,accountlevelactionlimit.limitTypeId,accountlevelactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                     
                                        END 
							
				                      END -- 13 
				              END --11
									 
						 exec(@query)
						
   END -- outermost level 

   END -- while level
   END -- proc level  

GO

/****** Object:  StoredProcedure [${dbxschemaname}].[user_customers_proc]    Script Date: 7/14/2023 12:33:04 PM ******/
DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_customers_proc;
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
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in (',''''+@filteredContracts+'''','));');
        exec(@select_statement);
    END;


GO	

ALTER TABLE [${dbxschemaname}].model ADD source nvarchar(50);

GO

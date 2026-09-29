

CREATE PROCEDURE [${dbxschemaname}].[servicedefinitionactions_get_proc](
	@_serviceDefinitionId nvarchar(50)
)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
	   DECLARE @select_statement nvarchar(max)

	   SET @select_statement = '(select feature.id as featureId, feature.name as featureName , feature.description as featureDescription ,feature.Status_id as featureStatus ,featureaction.id as actionId , featureaction.name as actionName , featureaction.description as actionDescription,featureaction.status as actionStatus from [${dbxschemaname}].feature
       left join [${dbxschemaname}].featureaction on (featureaction.Feature_id = feature.id)where featureaction.id in';
	

       SET @select_statement = CONCAT (@select_statement ,'(select servicedefinitionactionlimit.actionId
       from [${dbxschemaname}].servicedefinitionactionlimit where
       servicedefinitionactionlimit.serviceDefinitionId=','''',@_serviceDefinitionId,'''','))');
       
	   SELECT @select_statement;
	   exec(@select_statement);
    END
GO

ALTER TABLE [${dbxschemaname}].contractactionlimit ADD [isPortfolio] VARCHAR(45) NULL DEFAULT 'false' ,[accountId] VARCHAR(45) NULL;
GO

ALTER TABLE [${dbxschemaname}].contractaccounts ADD  [portfolioId] VARCHAR(45) NULL ,[productId] VARCHAR(45) NULL , [portfolioName] VARCHAR(45) NULL ;
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_action_limit_save_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[contract_action_limit_save_proc]
  @_queryInput nvarchar(max)
  
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
      DECLARE @index int
      DECLARE @numOfRecords int
      DECLARE @recordsData nvarchar(max)
      DECLARE @select_statement nvarchar(max)
      DECLARE @id varchar(255);
      set @index = 0;
	  set @_queryInput = REPLACE(@_queryInput,'\','');
	  set @_queryInput = REPLACE(@_queryInput,'"','''');

      set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1;
      WHILE 1=1 BEGIN
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 BEGIN 
				BREAK;
			END
			ELSE begin
            set @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+[${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1 );
            select @recordsData;
			
               set @select_statement = ('INSERT INTO [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (') + (@recordsData) + (N')');
           
			select(@select_statement);
			execute(@select_statement);
           END;
      END
    END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[default_contractactions_create_proc];

GO
CREATE PROCEDURE [${dbxschemaname}].[default_contractactions_create_proc]
@_contractId nvarchar(max)

AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 DECLARE @finished INT = 0 ;
 DECLARE @featureActionId varchar(255);
 DECLARE @featureId varchar(255) ;
 DECLARE @actionslist varchar(255) ;
 DECLARE @limitId varchar(255);
 DECLARE @entryStatus INT= 0 ;
 DECLARE @accountId varchar(255);
 DECLARE @actualLimitId varchar(255) ;
 DECLARE @_accountsCSV varchar(255) ;
 DECLARE @coreCustomerId varchar(255)  ;
 DECLARE @serviceDefinitionId nvarchar(50);
 DECLARE @serviceType varchar(255);
 DECLARE @validActionsList varchar(max);
 DECLARE @validFIActions varchar(max);
 DECLARE @limitvalue varchar(255);
 DECLARE @id varchar(255);
  

DECLARE coreCustomers CURSOR
         FOR (SELECT contractcorecustomers.coreCustomerId FROM [${dbxschemaname}].contractcorecustomers WHERE [${dbxschemaname}].contractcorecustomers.contractId = @_contractId );
	
 
SET @_accountsCSV = (SELECT String_agg(CAST([${dbxschemaname}].contractaccounts.accountId AS nvarchar(max)), ',') FROM 
[${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId);

SET @serviceDefinitionId = (SELECT [${dbxschemaname}].contract.servicedefinitionId from [${dbxschemaname}].contract WHERE [${dbxschemaname}].contract.id = @_contractId);
SET @serviceType = (SELECT [${dbxschemaname}].servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE [${dbxschemaname}].servicedefinition.id = @serviceDefinitionId);
                        
SET @validFIActions = (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') 
FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.Feature_id in (select
[${dbxschemaname}].contractfeatures.featureId from [${dbxschemaname}].contractfeatures
where [${dbxschemaname}].contractfeatures.contractId = @_contractId));

SET @validActionsList = (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId=
@serviceDefinitionId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,
@validFIActions)=1);


                      
SET @_accountsCSV = (SELECT String_agg(CAST([${dbxschemaname}].contractaccounts.accountId AS nvarchar(max)), ',') FROM 
[${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId);


OPEN coreCustomers;
   FETCH NEXT FROM coreCustomers into @coreCustomerId
   WHILE (@@FETCH_STATUS=0)
         BEGIN
		DECLARE transactionLimits CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET(featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1'));


	        OPEN transactionLimits; 
			   FETCH NEXT FROM transactionLimits into @featureActionId
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
            SET @limitvalue = (SELECT [${dbxschemaname}].servicedefinitionactionlimit.value FROM 
			[${dbxschemaname}].servicedefinitionactionlimit WHERE [${dbxschemaname}].servicedefinitionactionlimit.actionId = @featureActionId  
		    AND [${dbxschemaname}].servicedefinitionactionlimit.limitTypeId = @limitId 
			AND [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId);

               
               SET @id = (SELECT left(newid(), 50))
               INSERT [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,featureId,actionId,limitTypeId,value) VALUES
									(@id,@_contractId,@coreCustomerId,@featureId,@featureActionId,@limitId,@limitvalue);
               
              FETCH NEXT FROM limits into @limitId
              CONTINUE
            END
            
          
          CLOSE limits
          DEALLOCATE limits
          
				FETCH NEXT FROM transactionLimits into @featureActionId
		        CONTINUE
				END
        CLOSE transactionLimits;
        DEALLOCATE transactionLimits
      DECLARE accounts CURSOR
           FOR (SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId );
      
		
    OPEN accounts; 
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
       DECLARE accountLevelPermissions CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1'));

	  OPEN accountLevelPermissions; 

	  FETCH NEXT FROM accountLevelPermissions into @featureActionId
      WHILE (@@FETCH_STATUS=0)
          BEGIN
            
			SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
            SET @id = (SELECT left(newid(), 50));
	        INSERT [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId) VALUES
					(@id,@_contractId,@coreCustomerId,@accountId,@featureId,@featureActionId);
			
			    FETCH NEXT FROM accountLevelPermissions into @featureActionId
                CONTINUE
			
           END
          CLOSE accountLevelPermissions;
          DEALLOCATE accountLevelPermissions;
		      FETCH NEXT FROM accounts into @accountId
              CONTINUE
           END
  CLOSE accounts;
  DEALLOCATE accounts;

	  DECLARE globalLevelPermissions CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList)='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '0'));

	  OPEN globalLevelPermissions; 
	    
       FETCH NEXT FROM globalLevelPermissions into @featureActionId

       WHILE (@@FETCH_STATUS=0)
         BEGIN
  
		   SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.id = @featureActionId);
		   SET @id = (SELECT left(newid(), 50));
           INSERT [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,featureId,actionId) VALUES
				(@id,@_contractId,@coreCustomerId,@featureId,@featureActionId);
		
		     FETCH NEXT FROM globalLevelPermissions into @featureActionId
		     CONTINUE
	     END 
	  CLOSE globalLevelPermissions;
	  DEALLOCATE globalLevelPermissions;
	
END
CLOSE coreCustomers;
DEALLOCATE coreCustomers;
END
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contractactionlimit_delete_proc];

GO
CREATE PROCEDURE [${dbxschemaname}].[contractactionlimit_delete_proc]
@_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

       DELETE FROM [${dbxschemaname}].contractactionlimit where contractactionlimit.coreCustomerId = @_coreCustomerId;
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_account_default_actions_create_proc]; 
GO

CREATE PROCEDURE [${dbxschemaname}].[user_account_default_actions_create_proc]
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
	  DECLARE
         @limitGroupId varchar(255) = N''
	  DECLARE
         @coreCustomerId varchar(255) = N''

	 
	  
	  
								
      SET @serviceDefinitionId = (SELECT [${dbxschemaname}].[contract].[servicedefinitionId] from [${dbxschemaname}].[contract] WHERE [${dbxschemaname}].[contract].[id] = @_contractId)
      SET @serviceType = (SELECT [${dbxschemaname}].[servicedefinition].[serviceType] from [${dbxschemaname}].[servicedefinition] WHERE [${dbxschemaname}].[servicedefinition].[id] = @serviceDefinitionId)  

	  IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
                        WHERE [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = @serviceDefinitionId AND [${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = '1')

    														
      SET @validFIActions = (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') 
      FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.Feature_id in (select
      [${dbxschemaname}].contractfeatures.featureId from [${dbxschemaname}].contractfeatures
      where [${dbxschemaname}].contractfeatures.contractId = @_contractId));
	
	 SET @validActionsList = (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',')
     FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId=
     @serviceDefinitionId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,
     @validFIActions)=1);

   	  DECLARE accounts CURSOR LOCAL
            FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
            [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
            [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
            [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
            [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id,@_accountsCSV)='1'); 
       
     OPEN accounts     
      FETCH NEXT FROM accounts into @accountId
       WHILE (@@FETCH_STATUS=0)
         BEGIN
		   DECLARE accountLevelPermissions CURSOR 
		            FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1'));
          OPEN accountLevelPermissions
            FETCH NEXT FROM accountLevelPermissions into @featureActionId
	
             WHILE (@@FETCH_STATUS=0)
             BEGIN					
                 SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
                 SET @entryStatus = 0
                 SET @id = (SELECT left(newid(), 50));
	             INSERT  [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId) VALUES(@id,@_contractId,@_coreCustomerId,@accountId,@featureId,@featureActionId);
                  FETCH NEXT FROM accountLevelPermissions into @featureActionId
				  CONTINUE
			 END
         CLOSE accountLevelPermissions
         DEALLOCATE accountLevelPermissions		
      FETCH NEXT FROM accounts into @accountId
      CONTINUE
	   END
	   
	CLOSE accounts
    DEALLOCATE accounts	
	
	DECLARE accounts CURSOR LOCAL
            FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
            [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
            [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
            [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
            [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id,@_accountsCSV)='1'); 
      
	OPEN accounts     
     FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
		 DECLARE actions CURSOR 
               FOR (select id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList)='1' AND ([${dbxschemaname}].featureaction.isAccountLevel = '1' OR [${dbxschemaname}].featureaction.isAccountLevel = 'true' ));
	  
	     OPEN actions
            FETCH NEXT FROM actions into @featureActionId
		     WHILE (@@FETCH_STATUS=0)
             BEGIN
			   SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId);
               SET @limitGroupId = (SELECT [${dbxschemaname}].featureaction.limitgroupId FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId);
                DECLARE limits CURSOR 
                    FOR (select LimitType_id from actionlimit where Action_id = @featureActionId );
               OPEN limits
                  FETCH NEXT FROM limits into @limitId
				  WHILE (@@FETCH_STATUS=0)
                  BEGIN
				   SET @limitvalue = (SELECT [${dbxschemaname}].contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit WHERE contractactionlimit.actionId = @featureActionId AND contractactionlimit.limitTypeId = @limitId  AND contractactionlimit.contractId = @_contractId AND contractactionlimit.coreCustomerId = @_coreCustomerId);
				   
				   if (@limitId='MAX_TRANSACTION_LIMIT') 
				   BEGIN
               	    SET @actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
				    SET @id = ( SELECT left(newid(), 50));
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

               	  SET @actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
				end
                ELSE if (@limitId='DAILY_LIMIT')
				
				
				BEGIN
               	SET @actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT left(newid(), 50));
                
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
                SET @actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
				
				END

				ELSE IF (@limitId='WEEKLY_LIMIT') 
				BEGIN
     
               	SET @actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT left(newid(), 50));
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

               	SET @actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END
			    
			   if (@limitId!='MIN_TRANSACTION_LIMIT')
                 BEGIN
			       SET @id = (SELECT left(newid(), 50));
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
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                  
                
				 END

                 
                SET @entryStatus = 1;
                 FETCH NEXT FROM limits into @limitId
				 CONTINUE
                 END
                CLOSE limits
				DEALLOCATE limits

			          
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
	
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[forex_proc_get];
GO

CREATE PROCEDURE [${dbxschemaname}].[forex_proc_get]
  @read_query varchar(8000)
  
AS
BEGIN
DECLARE @stmt varchar(8000);
SET @stmt = CONCAT('', @read_query);
EXEC(@stmt);
END

GO


ALTER TABLE [${dbxschemaname}].[accountsstatementfiles] DROP COLUMN [fileContent];
ALTER TABLE [${dbxschemaname}].[accountsstatementfiles] ADD [fileContent] varbinary(MAX) NULL;
GO
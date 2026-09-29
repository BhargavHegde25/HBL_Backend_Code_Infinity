USE [${dbxschemaname}];

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_users_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].contract_users_details_get_proc  
   @_contractId nvarchar(50),
   @_backendType nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @customers nvarchar(max) = N''

      SET @customers =  (SELECT String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers WHERE 
                               [${dbxschemaname}].contractcustomers.contractId = @_contractId)

      SELECT 
         customer.id AS customerId, 
         customer.FirstName AS firstName, 
         customer.MiddleName AS middleName, 
         customer.LastName AS lastName, 
         customer.UserName AS userName, 
         customer.Status_id AS statusId, 
         customer.DateOfBirth AS dateOfBirth, 
         customer.Ssn AS Ssn, 
         backendidentifier.BackendId AS primaryCoreCustomerId,
         customercommunication.Value AS Email
      FROM 
         [${dbxschemaname}].customer 
            LEFT JOIN [${dbxschemaname}].customercommunication 
            ON ([${dbxschemaname}].customer.id = [${dbxschemaname}].customercommunication.Customer_id AND customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND
                customercommunication.isPrimary = '1')
            LEFT JOIN [${dbxschemaname}].backendidentifier 
            ON ([${dbxschemaname}].backendidentifier.Customer_id = [${dbxschemaname}].customer.id AND 
                [${dbxschemaname}].backendidentifier.BackendType = @_backendType)
         WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id, @customers) = '1'
   END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].approvalmatrix_default_create_proc(
@_actionIds NVARCHAR(max) , 
@_contractId NVARCHAR(50) ,
@_accountIds NVARCHAR(max) ,
@_cif NVARCHAR(50)
) AS
BEGIN
DECLARE @accountList VARCHAR(max) = 0;
DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT';
DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT';
DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT';
DECLARE @accountIndex INTEGER = 0;
DECLARE @actionIndex INTEGER = 0;
DECLARE @typeId VARCHAR(max) = '';
DECLARE @numOfAccounts int;
DECLARE @numOfActions int;
DECLARE @accountId NVARCHAR(50);
DECLARE @actionId NVARCHAR(255);

IF @_actionIds IS NULL OR  @_actionIds = ''
	GOTO MAINLABEL$leave
       
IF @_contractId IS NULL OR  @_contractId = ''
	GOTO MAINLABEL$leave
       
IF @_cif IS NULL OR  @_cif = ''
       	GOTO MAINLABEL$leave

IF @_accountIds IS NULL OR  @_accountIds = ''
       	GOTO MAINLABEL$leave
       
set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
getAccount: WHILE 1=1 BEGIN
       set @accountIndex = @accountIndex + 1;
       IF @accountIndex = @numOfAccounts + 1 BEGIN 
              BREAK;
       End
       Else Begin
              set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIndex), ',', -1 );
              set @actionIndex = 0; 
               getAction: WHILE 1=1 BEGIN
            set @actionIndex = @actionIndex + 1;
            IF @actionIndex = @numOfActions + 1 BEGIN
                           BREAK;
                     End
                     Else Begin
                           set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex), ',', -1 );
                SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
                IF @typeId = 'MONETARY' BEGIN               
                                  INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                                  (@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_1, '_', @_contractId), @accountId, @actionId, @limitTypeId_1,@_cif,'NO_APPROVAL');
                                  INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                                  (@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_2, '_', @_contractId), @accountId,@actionId,@limitTypeId_2,@_cif,'NO_APPROVAL');
                                  INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                                  (@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_3, '_', @_contractId),@accountId, @actionId, @limitTypeId_3,@_cif,'NO_APPROVAL');
                END
                ELSE IF @typeId = 'NON_MONETARY' BEGIN
                                  INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                    (@_contractId, concat(@actionId, '_', @accountId, '_', 'NON_MONETARY_LIMIT', '_', @_contractId), @accountId, @actionId, 'NON_MONETARY_LIMIT',@_cif,'NO_APPROVAL');
                           END 
                     END 
               END;
        set @accountList = CONCAT(@accountId,',',@accountList);
       END 
END;  

SET @accountList = (select SUBSTRING(@accountList,1,(LEN(@accountList)-1)));
select @accountList as accountList;

MAINLABEL$leave: 

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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeraction_save_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customeraction_save_proc]  @_queryInput nvarchar(max)
       AS 
       BEGIN
              SET  XACT_ABORT  ON
              SET  NOCOUNT  ON
              DECLARE @recordRow nvarchar(max)
              DECLARE @recordsData nvarchar(max)
              DECLARE @query nvarchar(max)
              set @_queryInput = REPLACE(@_queryInput,'\\','')
              set @_queryInput = REPLACE(@_queryInput,'"','''')
              DECLARE actions CURSOR LOCAL
              FOR (SELECT value FROM STRING_SPLIT(@_queryInput, '|'));
              OPEN actions
              FETCH NEXT FROM actions into @recordRow
              WHILE (@@FETCH_STATUS=0)
              BEGIN
                     SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+@recordRow
                    SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value) VALUES (') + (@recordsData) + (N')')
                     EXEC(@query)
                     FETCH NEXT FROM actions into @recordRow
                     CONTINUE
        END
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_contract_delete_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_contract_delete_proc]  
   @customerId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
              DECLARE @contract_statement nvarchar(max);
              DECLARE @suspended_statement nvarchar(max);
              DECLARE @accounts_statement nvarchar(max);
              DECLARE @group_statement nvarchar(max);
              DECLARE @action_statement nvarchar(max);
              DECLARE @excluded_accounts_statement nvarchar(max);
              DECLARE @limitgroup_statement nvarchar(max);
              DECLARE @where_clause nvarchar(max);
              DECLARE @where_clause1 nvarchar(max);
              
              SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomers] where';
              SET @suspended_statement = N'DELETE FROM [${dbxschemaname}].[suspendedcustomers] where';
              SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customeraccounts] where';
              SET @excluded_accounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomeraccounts] where';
              SET @group_statement = N'DELETE FROM [${dbxschemaname}].[customergroup] where';
              SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customeraction] where';
              SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where';

              SET @where_clause = '';
              SET @where_clause1 = '';
              if(@customerId != '') 
              BEGIN
                     SET @where_clause = @where_clause + (N' customerId = ') + ((QUOTENAME((@customerId), '''')))
                     SET @where_clause1 = @where_clause1 + (N' Customer_id = ') + ((QUOTENAME((@customerId), '''')))
              END
              
              IF(@contractId != '') 
              BEGIN
                     IF(@where_clause != '')
                     BEGIN
                           SET @where_clause = @where_clause + (N' AND ')
                           SET @where_clause1 = @where_clause1 + (N' AND ')
                     END
                     SET @where_clause = @where_clause + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
                     SET @where_clause1 = @where_clause1 + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
              END
              
              IF(@coreCustomerId != '') 
              BEGIN
                     IF(@where_clause != '')
                     BEGIN
                           SET @where_clause = @where_clause + (N' AND ')
                           SET @where_clause1 = @where_clause1 + (N' AND ')
                     END
                     SET @where_clause = @where_clause + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
                     SET @where_clause1 = @where_clause1 + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
              END
              
              IF(@where_clause != '')
              BEGIN  
                     SET @contract_statement = @contract_statement + @where_clause;
                     SET @suspended_statement = @suspended_statement + @where_clause;
                     SET @accounts_statement = @accounts_statement + @where_clause1;
                     SET @excluded_accounts_statement = @excluded_accounts_statement + @where_clause1;                   
                     SET @group_statement = @group_statement + @where_clause1;  
                     SET @action_statement = @action_statement + @where_clause1;       
                     SET @limitgroup_statement = @limitgroup_statement + @where_clause1;     
              
                     exec(@contract_statement);
                     exec(@suspended_statement);
                     exec(@accounts_statement);
                     exec(@excluded_accounts_statement);
                     exec(@group_statement);
                     exec(@action_statement);
                     exec(@limitgroup_statement)
              END
   END
GO

   
DROP PROCEDURE IF EXISTS [${dbxschemaname}].corecustomeraccounts_details_get_proc;
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
           IIF ((contract.serviceType) = ''TYPE_ID_BUSINESS'' ,''true'' , ''false'') AS isBusinessAccount,
           contractaccounts.accountId AS accountId
           from [${dbxschemaname}].contractcorecustomers 
                 JOIN [${dbxschemaname}].contractaccounts ON (contractcorecustomers.coreCustomerId = contractaccounts.coreCustomerId)
           JOIN [${dbxschemaname}].customeraccounts ON (customeraccounts.Account_id = contractaccounts.accountId)
                     JOIN [${dbxschemaname}].contract ON (contractcorecustomers.contractId = contract.id)
           where [${dbxschemaname}].FIND_IN_SET(contractcorecustomers.coreCustomerId, ','''',@corecustomersList,'''',')>0 AND customeraccounts.Customer_id = ','''',@_customerId,'''',')')
      
         exec(@selectstatement);
   END
GO   


DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_action_limit_update;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_action_limit_update](
  @_contractActionLimit NVARCHAR(max)
)
AS
BEGIN
  DECLARE @index1 INTEGER = 0;
  DECLARE @numOfRecords INTEGER = 0;
  DECLARE @contractValues NVARCHAR(max) ;
  DECLARE @contractId NVARCHAR(max) ;
  DECLARE @coreCustomerId NVARCHAR(max) ;
  DECLARE @featureId NVARCHAR(max) ;
  DECLARE @actionId NVARCHAR(max) ;
  DECLARE @limitTypeId NVARCHAR(max) ;
  DECLARE @limitValue NVARCHAR(max);
    set @numOfRecords = LEN(@_contractActionLimit) - LEN(REPLACE(@_contractActionLimit, '|', '')) + 1;
  WHILE 1=1 BEGIN
     set @index1 = @index1 + 1;
        IF @index1 = @numOfRecords + 1 BEGIN
            break ;
        end
        else begin
            set @contractValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractActionLimit, '|', @index1), '|', -1 );
            set @contractId = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',1);
            set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',2),',',-1);
            set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',3),',',-1);
            set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',4),',',-1);
            set @limitTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',5),',',-1);
            set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',-1);
--               select @limitValue beforeReplace
               set @limitValue = Replace(@limitValue,'"','')
--               select @limitValue as afterreplace
            Declare @num DECIMAL(13,1)
            set @num = cast(@limitValue AS DECIMAL(13,1))
--            select @num
            UPDATE contractactionlimit SET value = @num where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId;
        END

    END ;
END;
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_actionlimits_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS
   BEGIN
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON
​
         DECLARE
         @id nvarchar(255) = N''
​
              DECLARE
         @tempLimitValuenum nvarchar(255) = N''
        
      DECLARE
         @tempLimitValue DECIMAL(20,2)
     
      DECLARE
         @index int = 0
        
      DECLARE
         @numOfRecords int = 0
        
      DECLARE
         @serviceDefinitionId nvarchar(255) = N''
        
      DECLARE
         @recordsData nvarchar(255) = N''
        
      DECLARE
         @contractId nvarchar(255) = N''
        
      DECLARE
         @customerId nvarchar(255) = N''
        
      DECLARE
         @featureId nvarchar(255) = N''
        
      DECLARE
         @actionId nvarchar(255) = N''
        
      DECLARE
         @limitId nvarchar(255) = N''
     
      DECLARE
         @limitValuenum nvarchar(255) = N''
        
      DECLARE
         @limitValue DECIMAL(20,2)
        
      DECLARE
         @recordsDataWithoutLimits nvarchar(max) = N'' 
       
      DECLARE
         @limitAtFInum nvarchar(255) = N''  
         
      DECLARE
         @limitAtFI DECIMAL(20,2) 
​
       DECLARE
         @limitATServiceDefinitionnum nvarchar(255) = N''
        
        DECLARE
         @limitATServiceDefinition DECIMAL(20,2) 
​
       DECLARE
         @contarctFeatures nvarchar(max) = N''
   
    DECLARE
         @existingActionLimitRecords nvarchar(max) = N''
   
    DECLARE
         @serviceDefinitionActions nvarchar(max) = N''
​
       DECLARE
         @existingActionRecords nvarchar(max) = N'' 
​
   DECLARE
         @query nvarchar(max) = N''
         
      SET @index = 0
​
         SET @_queryInput = replace(@_queryInput, N'\', N'')
         SET @_queryInput = replace(@_queryInput, N'"', N'')
     
      SET @numOfRecords = LEN(@_queryInput) - LEN(replace(@_queryInput, N'|', N'')) + 1
     
​
      SET @serviceDefinitionId = N''
    
      WHILE (1 = 1)
     
         BEGIN
            SET @index = @index + 1
            IF @index = @numOfRecords + 1
               BREAK
            ELSE
               BEGIN
​
                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1)
​
                  SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 1), N'', -1)
​
                  SET @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 2), N',', -1)
                 
                  SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 3), N',', -1)
                 
                  SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 4), N',', -1)
                 
                  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 5), N',', -1)
                
                  SET @limitValuenum = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', -1), N'', 1)
                 
                  SET @recordsDataWithoutLimits = ([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 6)) + (N'')
 
                     SET @serviceDefinitionId =
                        (
                         
                           SELECT contract.servicedefinitionId
                           FROM [${dbxschemaname}].contract
                           WHERE contract.id = @contractId
                          
                        )
​
                  IF @limitId <> '@' AND @limitValuenum <> '@'
                     BEGIN
            		set @limitValue = cast(@limitValuenum AS DECIMAL(20,2))
                        SET @limitAtFInum =
                           (                            
                              SELECT actionlimit.value
                              FROM [${dbxschemaname}].actionlimit
                              WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId
                             
                           )
                       
                        set @limitAtFI = cast(@limitAtFInum AS DECIMAL(20,2))  
                        SET @limitATServiceDefinitionnum =
                           (
                              SELECT servicedefinitionactionlimit.value
                              FROM [${dbxschemaname}].servicedefinitionactionlimit
                              WHERE
                                 servicedefinitionactionlimit.actionId = @actionId AND
                                 servicedefinitionactionlimit.limitTypeId = @limitId AND
                                 servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
                            
                           )
                           set @limitATServiceDefinition = cast(@limitATServiceDefinitionnum AS DECIMAL(20,2))
                           IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                             
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                              
                          set @tempLimitValuenum = CAST(@tempLimitValue AS nvarchar);   
                     END
                                 
                  IF
                     CASE
                        WHEN NOT
                           CASE
                              WHEN (@tempLimitValuenum) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE
                           0
                     END <> 0 AND @tempLimitValuenum <> ''
                     SET @limitValue = @tempLimitValue
                    
                  SET @contarctFeatures =
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId  
                     )
                 
                  SET @serviceDefinitionActions =
                     (
                                                      
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
                     )
​
                  SET @existingActionLimitRecords =
                     (
                                  
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId
                     )
                 
​
                  SET @existingActionRecords =
                     (
                                         
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId
                       
                     )
                     
                 IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> ''
                    BEGIN
                          IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
                               BEGIN
                    ​                                  set @limitValuenum = CAST(@limitValue AS nvarchar)   
                                      SET @query = (@query)+
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                                                                          +
                                                                          (N'''')
                                                                          +
                                                                   (@limitValuenum)
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
                          		
                                IF (@limitId = '@' OR @limitValuenum = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @id = (SELECT left(newid(), 50))
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''');')
                                   
                                    END
                                ELSE
                                    IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValuenum) + (N''');')
                                     
                                      END
                                 
                    
                        END   
                 
              END
       END
       exec(@query)
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_limitgroup_limits_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].user_limitgroup_limits_create_proc  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
      
      DECLARE
         @singlePaymentsActions nvarchar(max) = N''
         
      DECLARE
         @bulkPaymentsActions nvarchar(max) = N''
         
      DECLARE
         @max_per_transaction_single_payment nvarchar(max) = N''
      
      DECLARE
         @max_per_transaction_single_paymentnum DECIMAL(20,2)     
         
      DECLARE
         @max_per_transaction_bulk_payment nvarchar(max) = N''
         
      DECLARE
         @max_per_transaction_bulk_paymentnum DECIMAL(20,2)     
            
      DECLARE
         @max_daily_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_daily_limit_single_paymentnum DECIMAL(20,2)     
           
      DECLARE
         @max_daily_limit_bulk_payment nvarchar(max) = N''
         
      DECLARE
         @max_daily_limit_bulk_paymentnum DECIMAL(20,2)     
     
      DECLARE
         @max_weekly_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_weekly_limit_single_paymentnum DECIMAL(20,2)     
         
      DECLARE
         @max_weekly_limit_bulk_payment nvarchar(max) = N''   
         
     DECLARE
         @max_weekly_limit_bulk_paymentnum DECIMAL(20,2)  
         
      SET @singlePaymentsActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction
                             where featureaction.limitgroupId = 'SINGLE_PAYMENT')  
                             
      SET @bulkPaymentsActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction
                             where featureaction.limitgroupId = 'BULK_PAYMENT')  

      SET @max_per_transaction_single_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
      set @max_per_transaction_single_paymentnum = cast(@max_per_transaction_single_payment AS DECIMAL(20,2)) 
      
      SET @max_per_transaction_bulk_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
       set @max_per_transaction_bulk_paymentnum = cast(@max_per_transaction_bulk_payment AS DECIMAL(20,2))
       
       SET @max_daily_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions ) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
               
        set @max_daily_limit_single_paymentnum = cast(@max_daily_limit_single_payment AS DECIMAL(20,2))
               
        SET @max_daily_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
            [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
        
        set @max_daily_limit_bulk_paymentnum = cast(@max_daily_limit_bulk_payment AS DECIMAL(20,2))
               
        SET @max_weekly_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
         
         
         set @max_weekly_limit_single_paymentnum = cast(@max_weekly_limit_single_payment AS DECIMAL(20,2))
          
         SET @max_weekly_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
         
         set @max_weekly_limit_bulk_paymentnum = cast(@max_weekly_limit_bulk_payment AS DECIMAL(20,2))
         
         IF @max_per_transaction_single_payment != ''
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_paymentnum)
         IF @max_per_transaction_bulk_payment != ''                    
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_paymentnum)
		IF @max_daily_limit_single_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_paymentnum)
		IF @max_daily_limit_bulk_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_paymentnum)
		IF @max_weekly_limit_single_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_paymentnum)
		IF @max_weekly_limit_bulk_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_paymentnum)


   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_features_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[contract_features_create_proc]  
   @_features nvarchar(max),
   @_contractId nvarchar(50),
   @_customerId nvarchar(50),
   @_serviceTypeId nvarchar(50),
   @_defaultActionsEnabled nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureId nvarchar(255) = N''

      DECLARE
         @featuresList nvarchar(max) = N''

      DECLARE
         @featureActionId nvarchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @limitId nvarchar(255) = N''

      DECLARE
         @tempLimitValue nvarchar(255) = N''

	  DECLARE
         @features_List nvarchar(max) = N''
         
      DECLARE
         @id nvarchar(255) = N''
         
      DECLARE
         @limitvalue nvarchar(255) = N''
         
      DECLARE
         @limitATServiceDefinition nvarchar(255) = N''  
         
      DECLARE
         @servicedefinitionId nvarchar(255) = N'' 

      DECLARE
         @validServicedefinitionActions nvarchar(max) = N''
         
      DECLARE
         @validFIActions nvarchar(max) = N''
         
      SET @features_list = 
         (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureroletype 
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_serviceTypeId)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 ))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END

         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) > 0
             )
             
         DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
             )
             
         DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
             )

 OPEN features
	  FETCH NEXT FROM features INTO @featureId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].contractfeatures([${dbxschemaname}].contractfeatures.id, [${dbxschemaname}].contractfeatures.contractId, [${dbxschemaname}].contractfeatures.coreCustomerId,[${dbxschemaname}].contractfeatures.featureId)
                             VALUES (@id,@_contractId,@_customerId,@featureId)
               SET @featuresList = @featureId + ',' + @featuresList
               FETCH NEXT FROM features INTO @featureId
               CONTINUE
            END
CLOSE features
DEALLOCATE features


SET @featuresList = (SELECT substring(@featuresList, 1,(case when len(@featuresList) > 0 then len(@featuresList) - 1 else 0 end)))

SELECT @featuresList AS featuresList;

SET @finished = 0;
                        
SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                        [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @featuresList) > 0 AND
                         featureaction.status = 'SID_ACTION_ACTIVE')

SET @servicedefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)

SET @validServicedefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                     servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                     [${dbxschemaname}].FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIActions) > 0)                                   


IF ISNULL(@_defaultActionsEnabled,'')<>'' AND  @_defaultActionsEnabled = 'true'
BEGIN  
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId)
           
            OPEN limits
                 FETCH NEXT FROM limits INTO @limitId 
                 WHILE (@@FETCH_STATUS=0)
		         BEGIN
                     SET @limitvalue = (SELECT actionlimit.value from [${dbxschemaname}].actionlimit WHERE actionlimit.Action_id = @featureActionId
                     AND actionlimit.LimitType_id = @limitId)
			                                       
                      SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value from [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                                    servicedefinitionactionlimit.actionId = @featureActionId AND
                                                    servicedefinitionactionlimit.limitTypeId = @limitId AND
                                                    servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId)
                    			  
			            IF @limitvalue > @limitATServiceDefinition 
					       BEGIN
						      SET @tempLimitValue = @limitvalue
                           END
					   ELSE 
					       BEGIN
						      SET @tempLimitValue = @limitATServiceDefinition
                           END
              
              
              
                       IF @tempLimitValue IS NOT NULL AND  @tempLimitValue <> ''
				           BEGIN
				              SET @limitvalue = @tempLimitValue
				           END
				
			           SET @id = (SELECT left(newid(), 50))
		               INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.limitTypeId,[${dbxschemaname}].contractactionlimit.value)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@limitId,@limitvalue)
                
                        SET @entryStatus = 1;
                        
                        FETCH NEXT FROM limits INTO @limitId 
                        CONTINUE
                 END
			CLOSE limits
            DEALLOCATE limits
                
            SET @finished = 0;
            
            IF @entryStatus = 0 
				 BEGIN
				     SET @id = (SELECT left(newid(), 50))
				     INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId)
				  END
            
       FETCH NEXT FROM actions INTO @featureActionId 
       CONTINUE
       END
 CLOSE actions
 DEALLOCATE actions
 END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_valid_customeraccounts_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[get_valid_customeraccounts_get_proc]  
   @_customeraccountsCSV nvarchar(max),
   @_customerId nvarchar(max)
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
                             where [${dbxschemaname}].customeraccounts.Account_id = @value and  [${dbxschemaname}].customeraccounts.Customer_id = @_customerId)    
              
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

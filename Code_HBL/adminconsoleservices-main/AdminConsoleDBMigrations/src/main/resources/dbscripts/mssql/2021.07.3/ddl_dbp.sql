USE [${dbxschemaname}];

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
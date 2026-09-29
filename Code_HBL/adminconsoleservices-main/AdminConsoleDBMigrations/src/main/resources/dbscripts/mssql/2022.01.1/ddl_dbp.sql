USE [${dbxschemaname}];

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
         @isNewAction nvarchar(255) = N''  
      
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
                 
                  SET @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 5), N',', -1)
                 
                  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 6), N',', -1)
                
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
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''',''') +(@isNewAction) + (N''');')
                                   
                                    END
                                ELSE
                                    IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ + (@isNewAction) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValuenum) + (N''');')
                                     
                                      END
                                 
                    
                        END   
                 
              END
       END
       IF @query IS NOT NULL AND @query <> ''
       BEGIN
       	exec(@query)
       END
   END
GO
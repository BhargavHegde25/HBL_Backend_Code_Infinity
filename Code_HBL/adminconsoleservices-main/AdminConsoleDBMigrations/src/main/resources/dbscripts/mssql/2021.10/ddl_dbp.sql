SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [${dbxschemaname}].[useraccounts_create_proc]  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @accountID varchar(255)

      DECLARE
         @finished int = 0
         
      DECLARE
         @id varchar(255)

      DECLARE
         @accounttypeid varchar(255)

	  DECLARE
         @accounttypename varchar(255)

      DECLARE
          accountData CURSOR LOCAL FORWARD_ONLY FOR 
             (   
                  SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE 
                               [${dbxschemaname}].contractaccounts.contractId = @_contractId AND
                               [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.accountId,@_accountsCSV) = '1'
             )
     
    OPEN accountData     
	FETCH NEXT FROM accountData into @accountID
      WHILE (1 = 1)
      
         BEGIN

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                 
                  SET @id = 
                     (
                        SELECT left(newid(), 50)
                     )
                 
				 set @accounttypeid = (select [${dbxschemaname}].contractaccounts.typeId from [${dbxschemaname}].contractaccounts where [${dbxschemaname}].contractaccounts.accountId =accountID);
				set @accounttypename = (select [${dbxschemaname}].accounttype.TypeDescription from [${dbxschemaname}].accounttype where [${dbxschemaname}].accounttype.TypeID  =@accounttypeid);
                  INSERT [${dbxschemaname}].customeraccounts(
                     id, 
                     Customer_id,                     Account_id, 
                     contractId, 
                     coreCustomerId,
					 accountType)
                     VALUES (
                        @id, 
                        @_userId, 
                        @accountID, 
                        @_contractId, 
                        @_coreCustomerId, 

						@accounttypename)
                 

               END
            FETCH NEXT FROM accountData into @accountID
            CONTINUE

         END
      CLOSE accountData
      DEALLOCATE accountData

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
            WHERE (feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 ))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END

         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) > 0
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


DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
             )
             
IF ISNULL(@_defaultActionsEnabled,'')<>'' AND  @_defaultActionsEnabled = 'true'
BEGIN  
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId)
          	 DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
             )
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
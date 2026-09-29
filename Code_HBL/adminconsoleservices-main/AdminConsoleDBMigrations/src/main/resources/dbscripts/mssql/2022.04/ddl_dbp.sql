

ALTER TABLE [${dbxschemaname}].[systemconfiguration] ALTER COLUMN [PropertyValue] varchar(100);
GO

USE [${dbxschemaname}]
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[user_accountactions_get_proc]    ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [${dbxschemaname}].[user_accountactions_get_proc]  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      
DECLARE
         @contractId nvarchar(255) = N''
         
DECLARE
         @serviceDefinitionId nvarchar(255) = N''
         
DECLARE
         @groupId nvarchar(255) = N''
         
DECLARE
         @validFIAccountLevelActions nvarchar(max) = N''
         
DECLARE
         @validServiceDefinitinActions nvarchar(max) = N''
         
DECLARE
         @validGroupActions nvarchar(max) = N''
         
DECLARE
         @validUserActions nvarchar(max) = N''
         
DECLARE
         @actionCondition nvarchar(max) = N''
         
DECLARE
         @select_statement nvarchar(max) = N''
         
      
SET @contractId =  (SELECT contractcorecustomers.contractId FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId)  

SET @serviceDefinitionId =  (SELECT contract.servicedefinitionId FROM [${dbxschemaname}].contract
                             where [${dbxschemaname}].contract.id = @contractId)  
                 
SET @groupId =  (SELECT customergroup.Group_id FROM [${dbxschemaname}].customergroup where 
                             [${dbxschemaname}].customergroup.contractId = @contractId AND
                             [${dbxschemaname}].customergroup.coreCustomerId = @_coreCustomerId AND
                             [${dbxschemaname}].customergroup.Customer_id = @_userId)  
                 
SET @groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  

IF @groupId IS NULL 
  SET @groupId = N''
                                      
SET @validFIAccountLevelActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                               ([${dbxschemaname}].featureaction.isAccountLevel = '1' OR [${dbxschemaname}].featureaction.isAccountLevel = 'true') AND
                               [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE')
                                                               
SET @validServiceDefinitinActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIAccountLevelActions) = '1')
                                                               
                                
SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitinActions) = '1')
                               
                                                                
SET @validUserActions =  (SELECT String_agg(CAST(Action_id AS nvarchar(max)), ',') FROM
								(select distinct [${dbxschemaname}].customeraction.Action_id from [${dbxschemaname}].customeraction WHERE 
                               [${dbxschemaname}].customeraction.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].customeraction.contractId = @contractId AND
                               ([${dbxschemaname}].customeraction.isAllowed = '1'  OR [${dbxschemaname}].customeraction.isAllowed = 'true') AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id,@validGroupActions) = '1')
							   as x);
                               

SET @actionCondition = (N'[${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].')+(N'customeraction.Action_id,''') + (@validUserActions) + (N''')')


set @select_statement = (N'SELECT 
        customeraction.Customer_id AS Customer_id,
         customeraction.contractId AS contractId,
		 customeraction.coreCustomerId AS coreCustomerId,
        customeraction.Account_id AS Account_id,
        customeraction.featureId AS featureId,
        customeraction.Action_id AS Action_id,
        customeraction.RoleType_id AS RoleType_id,
    	customeraction.LimitType_id AS LimitType_id,
        customeraction.value AS value
    FROM [${dbxschemaname}].customeraction where customeraction.Customer_id = ') +((QUOTENAME((@_userId), ''''))) + (N' and ') + (@actionCondition) + (N' >0 and customeraction.coreCustomerId =')  
    + (QUOTENAME((@_coreCustomerId), ''''))


exec(@select_statement)

   END

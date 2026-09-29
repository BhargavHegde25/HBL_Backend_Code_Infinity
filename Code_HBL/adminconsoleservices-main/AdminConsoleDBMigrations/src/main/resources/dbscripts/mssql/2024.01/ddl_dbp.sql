DROP PROCEDURE IF EXISTS [${dbxschemaname}].[default_contractactions_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[default_contractactions_create_proc]
@_contractId nvarchar(max),
@_legalEntityId nvarchar(50)

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
         FOR (SELECT contractcorecustomers.coreCustomerId FROM [${dbxschemaname}].contractcorecustomers WHERE [${dbxschemaname}].contractcorecustomers.contractId = @_contractId AND [${dbxschemaname}].contractcorecustomers.companyLegalUnit = @_legalEntityId );
 
SET @_accountsCSV = (SELECT String_agg(CAST([${dbxschemaname}].contractaccounts.accountId AS nvarchar(max)), ',') FROM 
[${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId AND [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId);

SET @serviceDefinitionId = (SELECT [${dbxschemaname}].contract.servicedefinitionId from [${dbxschemaname}].contract WHERE [${dbxschemaname}].contract.id = @_contractId AND [${dbxschemaname}].contract.companyLegalUnit = @_legalEntityId);
SET @serviceType = (SELECT [${dbxschemaname}].servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE [${dbxschemaname}].servicedefinition.id = @serviceDefinitionId);
                        
SET @validFIActions = (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') 
FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.Feature_id in (select
[${dbxschemaname}].contractfeatures.featureId from [${dbxschemaname}].contractfeatures
where [${dbxschemaname}].contractfeatures.contractId = @_contractId AND [${dbxschemaname}].contractfeatures.companyLegalUnit = @_legalEntityId));

SET @validActionsList = (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId=
@serviceDefinitionId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,
@validFIActions)=1);

OPEN coreCustomers;
   FETCH NEXT FROM coreCustomers into @coreCustomerId
   WHILE (@@FETCH_STATUS=0)
      BEGIN
      DECLARE accounts CURSOR
           FOR (SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId AND [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId );
      
		
    OPEN accounts; 
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
       DECLARE accountLevelPermissions CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1') AND [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId);

	  OPEN accountLevelPermissions; 

	  FETCH NEXT FROM accountLevelPermissions into @featureActionId
      WHILE (@@FETCH_STATUS=0)
          BEGIN
            
			SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId AND featureaction.companyLegalUnit = @_legalEntityId);
            SET @id = (SELECT left(newid(), 50));
	        INSERT [${dbxschemaname}].accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId,companyLegalUnit) VALUES
					(@id,@_contractId,@coreCustomerId,@accountId,@featureId,@featureActionId,@_legalEntityId);
			
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
	

END
CLOSE coreCustomers;
DEALLOCATE coreCustomers;
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[useractions_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].useractions_create_proc  
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

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (
            SELECT String_agg(CAST(customeraccounts.Account_id AS nvarchar(max)), ',') FROM 
							[${dbxschemaname}].customeraccounts WHERE 
                            [${dbxschemaname}].customeraccounts.Customer_id = @_userId AND
                            [${dbxschemaname}].customeraccounts.contractId = @_contractId AND
                            [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId AND
							[${dbxschemaname}].customeraccounts.companyLegalUnit = @_legalEntityId
             )
           
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT customeraccounts.Account_id FROM [${dbxschemaname}].customeraccounts WHERE 
      customeraccounts.contractId = @_contractId AND
      customeraccounts.coreCustomerId = @_coreCustomerId AND
      customeraccounts.Customer_id = @_userId AND
	  customeraccounts.companyLegalUnit = @_legalEntityId AND
      charindex(Account_id,@_accountsCSV)<>0);
    
    SET @serviceDefinitionId = (SELECT contract.servicedefinitionId FROM 
								[${dbxschemaname}].contract WHERE contract.id = @_contractId)
								
    SET @serviceType = (SELECT servicedefinition.serviceType FROM 
						[${dbxschemaname}].servicedefinition WHERE servicedefinition.id = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition
						WHERE groupservicedefinition.serviceDefinitionId = @serviceDefinitionId 
						AND groupservicedefinition.isDefaultGroup = '1'
						AND groupservicedefinition.companyLegalUnit = @_legalEntityId)
						 
    SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') 
							FROM [${dbxschemaname}].featureaction)
						
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') 
								FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId 
							   AND [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @_legalEntityId
							   AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                             
    SET @_groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                            [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
							[${dbxschemaname}].groupservicedefinition.Group_id = @_groupId AND 
							[${dbxschemaname}].groupservicedefinition.companyLegalUnit = @_legalEntityId)

                                
    SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') 
								FROM [${dbxschemaname}].groupactionlimit 
								WHERE [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND 
							   [${dbxschemaname}].groupactionlimit.companyLegalUnit = @_legalEntityId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
                                
    SET @validActionsList =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') 
							  FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @_legalEntityId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
                               
	DECLARE actions CURSOR LOCAL
      FOR (select id FROM 
		  [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
		  [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId AND
          ([${dbxschemaname}].featureaction.isAccountLevel = '1'));
         
    DECLARE nonaccountlevelactions CURSOR LOCAL
      FOR (select id FROM 
		  [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
		  [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId AND
          ([${dbxschemaname}].featureaction.isAccountLevel = '0'));
         
     
    OPEN accounts     
	FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
	OPEN actions
	FETCH NEXT FROM actions into @featureActionId
	 
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id 
							FROM [${dbxschemaname}].featureaction 
							WHERE featureaction.id = @featureActionId 
							AND featureaction.companyLegalUnit = @_legalEntityId);
         	
	      SET @entryStatus = 0
	      DECLARE limits CURSOR LOCAL
			FOR (select LimitType_id 
				FROM [${dbxschemaname}].actionlimit 
				where Action_id = @featureActionId 
				AND companyLegalUnit = @_legalEntityId);
					OPEN limits
					FETCH NEXT FROM limits into @limitId
					  WHILE (@@FETCH_STATUS=0)
						   BEGIN
							SET @limitvalue = (SELECT contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
											   WHERE contractactionlimit.actionId = @featureActionId AND 
											   contractactionlimit.limitTypeId = @limitId AND
											   contractactionlimit.contractId = @_contractId AND
											   contractactionlimit.coreCustomerId = @_coreCustomerId AND
											   contractactionlimit.companyLegalUnit = @_legalEntityId )

							IF (@limitId = 'MAX_TRANSACTION_LIMIT')
							SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
							ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
							SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
							ELSE IF (@limitId = 'DAILY_LIMIT')
							BEGIN
							   SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
							   SET @id = (SELECT left(newid(), 50))
							   INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
															 [${dbxschemaname}].customeraction.RoleType_id, 
															 [${dbxschemaname}].customeraction.Customer_id, 
															 [${dbxschemaname}].customeraction.contractId, 
															 [${dbxschemaname}].customeraction.coreCustomerId, 
															 [${dbxschemaname}].customeraction.featureId, 
															 [${dbxschemaname}].customeraction.Action_id, 
															 [${dbxschemaname}].customeraction.Account_id, 
															 [${dbxschemaname}].customeraction.isAllowed, 
															 [${dbxschemaname}].customeraction.LimitType_id, 
															 [${dbxschemaname}].customeraction.[value],
															 [${dbxschemaname}].customeraction.companyLegalUnit)
															 VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00, @_legalEntityId)
							   SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

							 END
							 ELSE 
							 BEGIN
							 IF (@limitId = 'WEEKLY_LIMIT')
							 BEGIN
							 
							 SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
							 SET @id = (SELECT left(newid(), 50))
							
							INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
														   [${dbxschemaname}].customeraction.RoleType_id, 
										[${dbxschemaname}].customeraction.Customer_id, 
														   [${dbxschemaname}].customeraction.contractId, 
														   [${dbxschemaname}].customeraction.coreCustomerId, 
														   [${dbxschemaname}].customeraction.featureId,
														   [${dbxschemaname}].customeraction.Action_id, 
														   [${dbxschemaname}].customeraction.Account_id, 
														   [${dbxschemaname}].customeraction.isAllowed, 
														   [${dbxschemaname}].customeraction.LimitType_id, 
														   [${dbxschemaname}].customeraction.[value],
														   [${dbxschemaname}].customeraction.companyLegalUnit)
														   VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00, @_legalEntityId)
							 SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
							 END
							 END
						SET @id =(SELECT left(newid(), 50))
						INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
													  [${dbxschemaname}].customeraction.RoleType_id, 
													  [${dbxschemaname}].customeraction.Customer_id,
													  [${dbxschemaname}].customeraction.contractId, 
													  [${dbxschemaname}].customeraction.coreCustomerId, 
													  [${dbxschemaname}].customeraction.featureId, 
													  [${dbxschemaname}].customeraction.Action_id, 
													  [${dbxschemaname}].customeraction.Account_id, 
													  [${dbxschemaname}].customeraction.isAllowed, 
													  [${dbxschemaname}].customeraction.LimitType_id, 
													  [${dbxschemaname}].customeraction.[value],
													  [${dbxschemaname}].customeraction.companyLegalUnit)
													  VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue, @_legalEntityId)
													  
						SET @id =(SELECT left(newid(), 50))
						INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
													  [${dbxschemaname}].customeraction.RoleType_id, 
													  [${dbxschemaname}].customeraction.Customer_id,
													  [${dbxschemaname}].customeraction.contractId, 
													  [${dbxschemaname}].customeraction.coreCustomerId, 
													  [${dbxschemaname}].customeraction.featureId, 
													  [${dbxschemaname}].customeraction.Action_id, 
													  [${dbxschemaname}].customeraction.Account_id, 
													  [${dbxschemaname}].customeraction.isAllowed, 
													  [${dbxschemaname}].customeraction.LimitType_id, 
													  [${dbxschemaname}].customeraction.[value],
													  [${dbxschemaname}].customeraction.companyLegalUnit)
													  VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue, @_legalEntityId) 
																				 
						  SET @entryStatus = 1
						  FETCH NEXT FROM limits into @limitId
						  CONTINUE
				  END
				  CLOSE limits
				  DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
		  SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.Account_id, 
                                        [${dbxschemaname}].customeraction.isAllowed,
										[${dbxschemaname}].customeraction.companyLegalUnit)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1, @_legalEntityId);
		   END
		   SET @actionslist = @featureActionId + N',' + @actionslist
		   FETCH NEXT FROM actions into @featureActionId
		   CONTINUE
           END
	       CLOSE actions
		  
		   FETCH NEXT FROM accounts into @accountId
		   CONTINUE
           END
		   CLOSE accounts
		   DEALLOCATE accounts
		   DEALLOCATE actions
		   
OPEN nonaccountlevelactions
	FETCH NEXT FROM nonaccountlevelactions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id 
							FROM [${dbxschemaname}].featureaction 
							WHERE featureaction.id = @featureActionId 
							AND featureaction.companyLegalUnit = @_legalEntityId);

          SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.isAllowed,
										[${dbxschemaname}].customeraction.companyLegalUnit)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,1, @_legalEntityId);
			FETCH NEXT FROM nonaccountlevelactions into @featureActionId
            CONTINUE

          END          
          
    CLOSE nonaccountlevelactions
		   DEALLOCATE nonaccountlevelactions
   END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[close_account_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[close_account_proc]
	@accountId nvarchar(50),
	@legalEntityId nvarchar(50),
	@statusFlag nvarchar(50)
AS
BEGIN	
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  declare @customers nvarchar(max)
	  declare @suspendedaccounts nvarchar(max)
	SET @customers = (SELECT String_agg(cast(customeraccounts.Customer_id  as NVARCHAR(MAX)), ',') FROM [${dbxschemaname}].customeraccounts WHERE 
	customeraccounts.Account_id=@accountId AND customeraccounts.companyLegalUnit=@legalEntityId);

	SET @customers = CONCAT(',',@customers,',');
	IF (@statusFlag = 'CLOSED') 
	BEGIN
	SELECT customeraccounts.Customer_id AS suspendedaccounts FROM [${dbxschemaname}].customeraccounts  WHERE 
	CHARINDEX(CONCAT(',',customeraccounts.Customer_id,','), @customers)>0 AND customeraccounts.companyLegalUnit = @legalEntityId AND 
	customeraccounts.accountStatus!='CLOSED' GROUP BY customeraccounts.Customer_id having COUNT(*)=1;

	SET @suspendedaccounts = (SELECT String_agg(cast(customeraccounts.Customer_id  as NVARCHAR(MAX)), ',') FROM [${dbxschemaname}].customeraccounts  WHERE 
	CHARINDEX(CONCAT(',',customeraccounts.Customer_id,','), @customers)>0 AND customeraccounts.companyLegalUnit = @legalEntityId AND 
	customeraccounts.accountStatus!='CLOSED' GROUP BY customeraccounts.Customer_id having COUNT(*)=1);	

	SET @suspendedaccounts = CONCAT(',',@suspendedaccounts,',');

	UPDATE [${dbxschemaname}].customerlegalentity SET customerlegalentity.Status_id='SID_CUS_SUSPENDED' WHERE 
	CHARINDEX(CONCAT(',',customerlegalentity.Customer_id,','), @suspendedaccounts) > 0 AND 
	customerlegalentity.legalEntityId = @legalEntityId;

	UPDATE customer SET customer.Status_id ='SID_CUS_SUSPENDED' WHERE
	CHARINDEX(CONCAT(',',customer.id,','), @suspendedaccounts) > 0 AND  
	customer.id NOT IN (
	SELECT customerlegalentity.Customer_id FROM [${dbxschemaname}].customerlegalentity where
	CHARINDEX(CONCAT(',',customerlegalentity.Customer_id,','), @suspendedaccounts) > 0
	AND customerlegalentity.Status_id<>'SID_CUS_SUSPENDED' GROUP BY customerlegalentity.Customer_id
	HAVING COUNT(*)>0); 

	END;

	UPDATE [${dbxschemaname}].customeraccounts SET customeraccounts.accountStatus = @statusFlag WHERE 
	customeraccounts.Account_id = @accountId AND 	customeraccounts.companyLegalUnit = @legalEntityId;

	UPDATE [${dbxschemaname}].contractaccounts SET contractaccounts.statusDesc = @statusFlag WHERE 
	contractaccounts.accountId = accountId AND 	contractaccounts.companyLegalUnit = @legalEntityId;	
END
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contract_features_create_proc]  
   @_features nvarchar(max),
   @_contractId nvarchar(50),
   @_customerId nvarchar(50),
   @_serviceTypeId nvarchar(50),
   @_defaultActionsEnabled nvarchar(50),
   @_legalEntityId nvarchar(50)
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
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_serviceTypeId and feature.companyLegalUnit = featureroletype.companyLegalUnit)
            WHERE (feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 AND feature.companyLegalUnit = @_legalEntityId))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END

         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) > 0 
			   AND feature.companyLegalUnit = @_legalEntityId
             )

 OPEN features
	  FETCH NEXT FROM features INTO @featureId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].contractfeatures([${dbxschemaname}].contractfeatures.id, [${dbxschemaname}].contractfeatures.contractId, [${dbxschemaname}].contractfeatures.coreCustomerId,[${dbxschemaname}].contractfeatures.featureId,[${dbxschemaname}].contractfeatures.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@_legalEntityId)
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

SET @servicedefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId and contract.companyLegalUnit = @_legalEntityId)

SET @validServicedefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                     servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                     [${dbxschemaname}].FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIActions) > 0)                                   


DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
			   AND featureaction.companyLegalUnit = @_legalEntityId
             )
             
IF ISNULL(@_defaultActionsEnabled,'')<>'' AND  @_defaultActionsEnabled = 'true'
BEGIN  
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId
			AND featureaction.companyLegalUnit = @_legalEntityId)
          	 DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
               AND actionlimit.companyLegalUnit = @_legalEntityId
             )
            OPEN limits
                 FETCH NEXT FROM limits INTO @limitId 
                 WHILE (@@FETCH_STATUS=0)
		         BEGIN
                     SET @limitvalue = (SELECT actionlimit.value from [${dbxschemaname}].actionlimit WHERE actionlimit.Action_id = @featureActionId
                     AND actionlimit.LimitType_id = @limitId
					 AND actionlimit.companyLegalUnit = @_legalEntityId)
			                                       
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
		               INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.limitTypeId,[${dbxschemaname}].contractactionlimit.value,[${dbxschemaname}].contractactionlimit.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@limitId,@limitvalue,@_legalEntityId)
                
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
				     INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@_legalEntityId)
				  END
            
       FETCH NEXT FROM actions INTO @featureActionId 
       CONTINUE
       END
 CLOSE actions
 DEALLOCATE actions
 END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
go

CREATE PROCEDURE [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
    @_contractId      NVARCHAR(128) ,
    @_coreCustomerId    NVARCHAR(128) ,
    @_featureActionId   NVARCHAR(128) ,
    @_accountIds       NVARCHAR(128) ,
    @_limitTypeId       NVARCHAR(128) ,
    @_limitValuesJSON   NVARCHAR(MAX)
AS
BEGIN
    DECLARE @accountIds NVARCHAR(max);
    DECLARE @isAccountLevelUpdate NVARCHAR(128);
    DECLARE @isAccountLevelFeature NVARCHAR(128);
    DECLARE @isGroupMatrix NVARCHAR(128);
    DECLARE @currency NVARCHAR(max);
    DECLARE @templateIdsForForceDelete NVARCHAR(max);
    DECLARE @templateIds NVARCHAR(max);
    DECLARE @sqlStmt NVARCHAR(max);
    DECLARE @limitsLength NVARCHAR(128);
    DECLARE @currentLimit NVARCHAR(max);
    DECLARE @limitIndex NVARCHAR(128);
    DECLARE @approvalRuleId NVARCHAR(128);
    DECLARE @lowerLimit NVARCHAR(128);
    DECLARE @upperLimit NVARCHAR(128);
    DECLARE @groupList NVARCHAR(max);
    DECLARE @groupRule NVARCHAR(max);
    DECLARE @approvers NVARCHAR(max);
    DECLARE @noOfApprovers NVARCHAR(128);
    DECLARE @currentUserId NVARCHAR(128);
    DECLARE @approverIndex NVARCHAR(128);
    DECLARE @noOfAccountIds NVARCHAR(128);
    DECLARE @lastTemplateId NVARCHAR(128);
    DECLARE @accountId NVARCHAR(128);
    DECLARE @accountIndex NVARCHAR(128);
    DECLARE @_requestMatrixDataJSON NVARCHAR(MAX);
    DECLARE @companyLegalUnit NVARCHAR(128);
    declare @singleQuote nvarchar(1);

    SET @singleQuote = '''';
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [dbxdb].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [dbxdb].[featureaction] WHERE id = @_featureActionId and companyLegalUnit = @companyLegalUnit);
    --SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [dbxdb].[featureaction] WHERE id = @_featureActionId);
    SET @isGroupMatrix = (SELECT DISTINCT([approvalmode].[isGroupLevel]) FROM [dbxdb].[approvalmode] WHERE coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    --SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [dbxdb].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    IF @_accountIds IS NULL OR @_accountIds = '' OR @_accountIds ='[]'
        BEGIN
            SET @isAccountLevelUpdate = 0;
            SET @accountIds = (SELECT STRING_AGG (CAST([contractaccounts].[accountId] AS NVARCHAR(max)),',') FROM [dbxdb].[contractaccounts] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
        END
    ELSE
        BEGIN
            SET @accountIds = @_accountIds;
        END
    IF @isAccountLevelFeature = 0
        BEGIN
            -- customer-level feature action rule is being added/updated, hence, no account ids are associated
            SET @accountIds = '';
        END
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    SET @currency = (SELECT DISTINCT([approvalmatrixtemplate].[currency]) FROM [dbxdb].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId and companyLegalUnit = @companyLegalUnit);
    IF @isAccountLevelUpdate = 0
        BEGIN
            -- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
            -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
            SET @templateIdsForForceDelete = (SELECT STRING_AGG(CAST([approvalmatrixtemplate].[id] AS NVARCHAR(max)),',')  FROM [dbxdb].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId);
            IF @templateIdsForForceDelete IS NOT NULL
                BEGIN
                    SET @templateIds = CONCAT('''', REPLACE(@templateIdsForForceDelete, ',', ''','''), '''');
                    SET @sqlStmt = CONCAT('DELETE FROM [dbxdb].[customerapprovalmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    SET @sqlStmt = CONCAT('DELETE FROM [dbxdb].[signatorygroupmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    DELETE FROM [dbxdb].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
                END
        END
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0
        BEGIN
            UPDATE [dbxdb].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
        END
    ELSE
        BEGIN
            UPDATE [dbxdb].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId AND [dbxdb].FIND_IN_SET(accountId, @accountIds)<>0;
        END
    SET @limitsLength = (select COUNT(*) FROM OPENJSON(@_limitValuesJSON));
    --select 'LimitsLength' + @limitsLength;
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    WHILE 1=1
        BEGIN
            IF @limitIndex = @limitsLength
                BEGIN
                    BREAK;
                END
            ELSE
                BEGIN

                    SET @currentLimit = (SELECT json.value FROM OPENJSON(@_limitValuesJSON) as json where json.[key] =@limitIndex) ;
                    --SET @currentLimit = JSON_VALUE(@_limitValuesJSON, CONCAT('$[', @limitIndex, ']'));
                    --select 'Current Limit:  ' + @currentLimit;
                    SET @limitIndex = @limitIndex + 1;
                    --select '@currentLimit' + @currentLimit;
                    --SET @approvalRuleId = (select json.value from OPENJSON(@currentLimit) as json where json.[key] = 'groupRule');
                    SET @approvalRuleId = REPLACE(JSON_VALUE(@currentLimit, '$.approvalruleId'),'"','');
                    SET @lowerLimit = JSON_VALUE(@currentLimit, '$.lowerlimit');
                    SET @upperLimit = JSON_VALUE(@currentLimit, '$.upperlimit');
                    SET @groupList = REPLACE(JSON_VALUE(@currentLimit, '$.groupList'),'"','');
                    SET @groupRule = REPLACE(JSON_VALUE(@currentLimit, '$.groupRule'),'"','');
                    --SET @approvalRuleId =  (SELECT DISTINCT([approvalrule].[id]) FROM [dbxdb].[approvalrule] WHERE numberOfApprovals =  REPLACE(REPLACE(@groupRule, '[', ''), ']', ''));
                    SET @approvers = JSON_VALUE(@currentLimit, '$.approvers');
                    -- error handling
                    -- if lowerLimit or upperLimit is null, then return an error signal
                    -- if group mode, then if groupList or groupRule is null, then return error signal
                    -- if user-based mode, then if approvalRuleId or approvers are null, then return error

                    IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@approvers IS NOT NULL AND REPLACE(@approvers,'"','') = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
                    IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0
                        BEGIN
                            INSERT INTO [dbxdb].[approvalmatrixtemplate](contractId, coreCustomerId, actionId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency, companyLegalUnit)
                            VALUES(@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId, @isGroupMatrix, (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency, @companyLegalUnit);
                            SET @lastTemplateId = (SELECT MAX(id) FROM [dbxdb].[approvalmatrixtemplate] ) ;
                            IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                BEGIN
                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            INSERT INTO [dbxdb].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
                                            VALUES(@lastTemplateId, @groupList, @groupRule);
                                        END
                                    ELSE
                                        BEGIN
                                            SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
                                            SET @currentUserId = '';
                                            SET @approverIndex = 0;
                                            WHILE (1=1)
                                                BEGIN
                                                    IF @approverIndex = @noOfApprovers
                                                        BEGIN
                                                            BREAK;
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                            INSERT INTO [dbxdb].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId)
                                                            VALUES (@currentUserId, @lastTemplateId);
                                                            SET @approverIndex = @approverIndex + 1;
                                                        END
                                                END
                                        END
                                END
                        END
                    IF @isAccountLevelFeature = 0
                        BEGIN
                            exec dbxdb.approvalmatrix_ids_checkncleanup_for_safedelete_proc
                                 _contractId,
                                 _coreCustomerId,
                                 _featureActionId,
                                 NULL;
                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                BEGIN
                                    INSERT INTO [dbxdb].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency,companyLegalUnit)
                                    VALUES (
                                               CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                               @_featureActionId, NULL, @_limitTypeId, @isGroupMatrix, (@approvalRuleId),
                                               CAST(@lowerLimit AS DECIMAL(20, 2)), CAST(@upperLimit AS DECIMAL(20, 2)), @currency,
                                               @companyLegalUnit);
                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [dbxdb].[approvalmatrix] );
                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                        BEGIN
                                            IF @isGroupMatrix = 1
                                                BEGIN
                                                    INSERT INTO [dbxdb].[signatorygroupmatrix] (approvalMatrixId, groupList, groupRule)
                                                    VALUES(@lastTemplateId, @groupList, (@groupRule));
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@approvers));
                                                    SET @currentUserId = '';
                                                    SET @approverIndex = 0;
                                                    WHILE (1=1)
                                                        BEGIN
                                                            IF @approverIndex = @noOfApprovers
                                                                BEGIN
                                                                    BREAK;
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                    INSERT INTO [dbxdb].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                    SET @approverIndex = @approverIndex + 1;
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                    ELSE
                        BEGIN
                            -- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
                            SET @noOfAccountIds = LEN(@accountIds) - LEN(REPLACE(@accountIds, ',', '')) + 1;
                            SET @accountIndex = 0;
                            WHILE (1=1)
                                BEGIN
                                    SET @accountIndex = @accountIndex + 1;
                                    IF @accountIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK;
                                        END
                                    ELSE
                                        BEGIN
                                            SET @accountId = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@accountIds, ',', @accountIndex), ',', -1);
                                            -- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId);
                                            -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                                BEGIN
                                                    --select '@accountId'+@accountId;
                                                    BEGIN
                                                        INSERT INTO [dbxdb].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId,
                                                                                                        limitTypeId, isGroupMatrix, approvalruleId, lowerLimit,
                                                                                                        upperLimit, currency, companyLegalUnit)
                                                        VALUES (CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                                                @_featureActionId, @accountId, @_limitTypeId, @isGroupMatrix,
                                                                @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20, 2)),
                                                                CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);

                                                    END
                                                    --COMMIT;
                                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [dbxdb].[approvalmatrix] );
                                                    --select '@lastTemplateId......' + @lastTemplateId;
                                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                                        BEGIN
                                                            IF @isGroupMatrix = 1
                                                                BEGIN
                                                                    set @sqlStmt = CONCAT('INSERT INTO [dbxdb].[signatorygroupmatrix] (approvalMatrixId, groupList,groupRule)
                                        VALUES(',@singleQuote,@lastTemplateId,@singleQuote,',',@singleQuote, @groupList,@singleQuote, ',',@singleQuote,@groupRule,@singleQuote,')');
                                                                    --select '@sqlStmt' + @sqlStmt;
                                                                    EXEC(@sqlStmt);
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON));
                                                                    SET @currentUserId = '';
                                                                    SET @approverIndex = 0;
                                                                    WHILE (1=1)
                                                                        BEGIN
                                                                            IF @approverIndex = @noOfApprovers
                                                                                BEGIN
                                                                                    BREAK;
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                                    INSERT INTO [dbxdb].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                                    SET @approverIndex = @approverIndex + 1;
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END;
go

ALTER TABLE [${dbxschemaname}].[card] ALTER COLUMN billingAddress nvarchar(150);

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkwiretemplate_update_proc];
GO


CREATE PROCEDURE [${dbxschemaname}].[bulkwiretemplate_update_proc](
    @_bulkwiretemplateValues nvarchar(max),
    @_update_bulkwiretemplatelineitemValues nvarchar(max),
    @_insert_bulkwiretemplatelineitemValues nvarchar(max)
)
AS
BEGIN
SET  XACT_ABORT  ON
SET  NOCOUNT  ON
    DECLARE @msg nvarchar(max)
    DECLARE @query0 nvarchar(max)
    DECLARE @query1 nvarchar(max)
    DECLARE @query2 nvarchar(max)
    DECLARE @bulkWiretemplateID nvarchar(max)
    DECLARE @bulkWiretemplateID2 nvarchar(max)
    DECLARE @DomCount int
    DECLARE @InternationalCount int
    DECLARE @totalCount int
    DECLARE @a nvarchar(max)
    DECLARE @b nvarchar(max)
	
    BEGIN TRY
        IF (@_bulkwiretemplateValues IS NULL OR LEN(@_bulkwiretemplateValues) = 0)
        BEGIN
            SELECT '@_bulkwiretemplateValues CANNOT BE NULL OR EMPTY';
        END
        ELSE
        BEGIN
            SET @a = REPLACE(@_bulkwiretemplateValues, '"', '''');
            SET @bulkWiretemplateID2 = [${dbxschemaname}].SUBSTRING_INDEX((@a), ',', 1);

            SET @query0 = 'MERGE INTO [${dbxschemaname}].bulkwiretemplate AS target
            USING (VALUES (' + @a + ')) AS source (bulkWireTemplateID, bulkWireTemplateName, createdBy, modifiedBy, lastmodifiedts, defaultFromAccount, defaultCurrency)
            ON target.bulkWireTemplateID = source.bulkWireTemplateID
            WHEN MATCHED THEN
                UPDATE SET
                    bulkWireTemplateName = source.bulkWireTemplateName,
                    modifiedBy = source.modifiedBy,
                    lastmodifiedts = source.lastmodifiedts,
                    defaultFromAccount = source.defaultFromAccount,
                    defaultCurrency = source.defaultCurrency
            WHEN NOT MATCHED THEN
                INSERT (bulkWireTemplateID, bulkWireTemplateName, createdBy, modifiedBy, lastmodifiedts, defaultFromAccount, defaultCurrency)
                VALUES (
                    source.bulkWireTemplateID, 
                    source.bulkWireTemplateName, 
                    source.createdBy, 
                    source.modifiedBy, 
                    source.lastmodifiedts, 
                    source.defaultFromAccount, 
                    source.defaultCurrency
                );';
            --SET @bulkWiretemplateID = RTRIM(LTRIM([${dbxschemaname}].SUBSTRING_INDEX((REPLACE(@_bulkwiretemplateValues, '"', '')), ',', 1)));
			--SELECT * FROM [${dbxschemaname}].bulkwiretemplate WHERE bulkWireTemplateID = '' + @bulkWiretemplateID + '';
		
            EXEC(@query0);
		--select LEN(@_update_bulkwiretemplatelineitemValues) AS LENGGHT;

            IF (@_update_bulkwiretemplatelineitemValues IS NOT NULL AND @_update_bulkwiretemplatelineitemValues != '')
            BEGIN
                SET @a = @_update_bulkwiretemplatelineitemValues;
                SET @query1 = 'MERGE INTO [${dbxschemaname}].bulkwiretemplatelineitems AS target
                USING (VALUES ' + @_update_bulkwiretemplatelineitemValues + ') AS source (
                    bulkWireTemplateLineItemID, bulkWireTemplateID, lastmodifiedts, swiftCode, 
                    bulkWireTransferType, transactionType, internationalRoutingNumber, 
                    recipientName, recipientAddressLine1, recipientAddressLine2, recipientCity, 
                    recipientState, recipientCountryName, recipientZipCode, recipientBankName, 
                    recipientBankAddress1, recipientBankAddress2, recipientBankZipCode, 
                    recipientBankcity, recipientBankstate, accountNickname, recipientAccountNumber, 
                    routingNumber, createdby, modifiedBy, payeeId, templateRecipientCategory
                )
                ON target.bulkWireTemplateLineItemID = source.bulkWireTemplateLineItemID
                WHEN MATCHED THEN
                    UPDATE SET
                        lastmodifiedts = source.lastmodifiedts,
                        swiftCode = source.swiftCode,
                        bulkWireTransferType = source.bulkWireTransferType,
                        transactionType = source.transactionType,
                        internationalRoutingNumber = source.internationalRoutingNumber,
                        recipientName = source.recipientName,
                        recipientAddressLine1 = source.recipientAddressLine1,
                        recipientAddressLine2 = source.recipientAddressLine2,
                        recipientCity = source.recipientCity,
                        recipientState = source.recipientState,
                        recipientCountryName = source.recipientCountryName,
                        recipientZipCode = source.recipientZipCode,
                        recipientBankName = source.recipientBankName,
                        recipientBankAddress1 = source.recipientBankAddress1,
                        recipientBankAddress2 = source.recipientBankAddress2,
                        recipientBankZipCode = source.recipientBankZipCode,
                        recipientBankcity = source.recipientBankcity,
                        recipientBankstate = source.recipientBankstate,
                        accountNickname = source.accountNickname,
                        recipientAccountNumber = source.recipientAccountNumber,
                        routingNumber = source.routingNumber,
                        createdby = source.createdby,
                        modifiedBy = source.modifiedBy,
                        payeeId = source.payeeId,
                        templateRecipientCategory = source.templateRecipientCategory
                WHEN NOT MATCHED THEN
                    INSERT (
                        bulkWireTemplateLineItemID, bulkWireTemplateID, lastmodifiedts, swiftCode, 
                        bulkWireTransferType, transactionType, internationalRoutingNumber, 
                        recipientName, recipientAddressLine1, recipientAddressLine2, recipientCity, 
                        recipientState, recipientCountryName, recipientZipCode, recipientBankName, 
                        recipientBankAddress1, recipientBankAddress2, recipientBankZipCode, 
                        recipientBankcity, recipientBankstate, accountNickname, recipientAccountNumber, 
                        routingNumber, createdby, modifiedBy, payeeId, templateRecipientCategory
                    )
                    VALUES (
                        source.bulkWireTemplateLineItemID, source.bulkWireTemplateID, source.lastmodifiedts, source.swiftCode, 
                        source.bulkWireTransferType, source.transactionType, source.internationalRoutingNumber, 
                        source.recipientName, source.recipientAddressLine1, source.recipientAddressLine2, source.recipientCity, 
                        source.recipientState, source.recipientCountryName, source.recipientZipCode, source.recipientBankName, 
                        source.recipientBankAddress1, source.recipientBankAddress2, source.recipientBankZipCode, 
                        source.recipientBankcity, source.recipientBankstate, source.accountNickname, source.recipientAccountNumber, 
                        source.routingNumber, source.createdby, source.modifiedBy, source.payeeId, source.templateRecipientCategory
                    );';

                EXEC(@query1);
            END

            IF (@_insert_bulkwiretemplatelineitemValues IS NOT NULL AND @_insert_bulkwiretemplatelineitemValues != '')
            BEGIN
			SET @a = REPLACE(@_insert_bulkwiretemplatelineitemValues, '"', '''');
                SET @query2 = 'INSERT INTO [${dbxschemaname}].bulkwiretemplatelineitems(bulkWireTemplateID,createdts,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values (' +@a + ')';
                
                EXEC(@query2);
            END
		END
        SET @bulkWiretemplateID = RTRIM(LTRIM([${dbxschemaname}].SUBSTRING_INDEX((REPLACE(@_bulkwiretemplateValues, '"', '')), ',', 1)));

        SELECT @DomCount = COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0;
        SELECT @InternationalCount = COUNT(*) FROM [${dbxschemaname}].bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'International' AND softdeleteflag = 0;

        SET @totalCount = @DomCount + @InternationalCount;
        UPDATE [${dbxschemaname}].bulkwiretemplate SET noOfTransactions = ''+@totalCount+'', noOfDomesticTransactions =''+ @DomCount+'', noOfInternationalTransactions = ''+@InternationalCount+'' WHERE bulkWireTemplateID =''+ @bulkWiretemplateID +'';

       SELECT * FROM [${dbxschemaname}].bulkwiretemplate WHERE bulkWireTemplateID = ''+ @bulkWiretemplateID + '';
		
	
    END TRY

    BEGIN CATCH
        SET @msg = (SELECT ERROR_MESSAGE());
        SELECT @msg as ErrorMessage;
    END CATCH
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_basic_info_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]
	@_customerId nvarchar(50),
	@_legalEntityId nvarchar(50)
AS
	BEGIN
		SET XACT_ABORT ON
		SET NOCOUNT ON
		declare @accountLockoutThreshold nvarchar(max)
		declare @accountLockoutTime nvarchar(max)
		SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
		SET @accountLockoutTime =(SELECT accountLockoutTime from [${dbxschemaname}].passwordlockoutsettings)
		
		SELECT TOP (1)
			customer.UserName AS Username,
			customer.FirstName AS FirstName,
			customer.MiddleName AS MiddleName,
			customer.LastName AS LastName,
			(ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS Name,
			customer.Salutation AS Salutation,
			customer.id AS Customer_id,
			customer.Ssn AS SSN,
			customer.createdts AS CustomerSince,
			customer.Gender AS Gender,
			customer.DateOfBirth AS DateOfBirth,
			customer.isEnrolledFromSpotlight AS isEnrolledFromSpotlight,
			CASE
				WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
			ELSE CASE
				WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
			ELSE customer.Status_id
			END
			END AS CustomerStatus_id,
			customerstatus.Description AS CustomerStatus_name,
			customer.MaritalStatus_id AS MaritalStatus_id,
			maritalstatus.Description AS MaritalStatus_name,
			customer.SpouseName AS SpouseName,
			customer.DrivingLicenseNumber AS DrivingLicenseNumber,
			customer.lockedOn AS lockedOn,
			customer.lockCount AS lockCount,
			customer.EmployementStatus_id AS EmployementStatus_id,employementstatus.Description AS EmployementStatus_name,
			( SELECT String_agg(CAST(Status_id as nvarchar(max)),',')
				FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id) ) AS CustomerFlag_ids,
			( SELECT String_agg(CAST(Description as nvarchar(max)),',')
				FROM [${dbxschemaname}].status
				WHERE status.id IN
			(
				SELECT customerflagstatus.Status_id
					FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id)
			)) AS CustomerFlag,
			customer.IsEnrolledForOlb AS IsEnrolledForOlb,
			customer.isEnrolled AS isEnrolled,
			customer.IsStaffMember AS IsStaffMember,
			customer.Location_id AS Branch_id,
			location.Name AS Branch_name,
			location.Code AS Branch_code,
			customer.IsOlbAllowed AS IsOlbAllowed,
			customer.IsAssistConsented AS IsAssistConsented,
			customer.isEagreementSigned AS isEagreementSigned,
			customer.companyLegalUnit AS companyLegalUnit,
			customer.isCombinedUser AS isCombinedUser,
			ISNULL(customer.combinedUserId, N'') AS combinedUserId,
			IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS CustomerType_id,
			IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS CustomerType_Name,
			IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS CustomerType_Description,
			(SELECT  membergroup.id FROM [${dbxschemaname}].membergroup WHERE membergroup.id in
			(SELECT top 1 customergroup.Group_id from [${dbxschemaname}].customergroup join [${dbxschemaname}].contractcustomers 
			on (customergroup.Customer_id = contractcustomers.customerid and 
			customergroup.contractId = contractcustomers.contractId and
			customergroup.corecustomerId = contractcustomers.corecustomerId )  
			where customergroup.Customer_id= @_customerId and contractcustomers.isPrimary = '1') AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_RoleId,
			(SELECT  membergroup.Name FROM [${dbxschemaname}].membergroup WHERE id in (SELECT top 1 Group_id 
  			from [${dbxschemaname}].customergroup join [${dbxschemaname}].contractcustomers 
			on (customergroup.Customer_id = contractcustomers.customerid and 
			customergroup.contractId = contractcustomers.contractId and
			customergroup.corecustomerId = contractcustomers.corecustomerId )  
			where customergroup.Customer_id= @_customerId and contractcustomers.isPrimary = '1' ) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_Role,
			(SELECT  membergroup.isEAgreementActive FROM [${dbxschemaname}].membergroup WHERE id in (SELECT top 1 Group_id 
			from [${dbxschemaname}].customergroup join [${dbxschemaname}].contractcustomers 
			on (customergroup.Customer_id = contractcustomers.customerid and 
			customergroup.contractId = contractcustomers.contractId and
			customergroup.corecustomerId = contractcustomers.corecustomerId )  
			where customergroup.Customer_id= @_customerId and contractcustomers.isPrimary = '1'
			) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS isEAgreementRequired,
			customer.Organization_Id AS organisation_id,
			organisation.BusinessType_id AS BusinessType_id,
			businesstype.name AS BusinessType,
			organisation.Name AS organisation_name,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryEmailAddress,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryEmailAddress,
			customer.DocumentsSubmitted AS DocumentsSubmitted,
			customer.ApplicantChannel AS ApplicantChannel,
			customer.Product AS Product,
			customer.Reason AS Reason,
			@accountLockoutTime AS accountLockoutTime
		FROM ((((((([${dbxschemaname}].customer
		LEFT JOIN [${dbxschemaname}].location
		ON ((customer.Location_id = location.id)))
		LEFT JOIN [${dbxschemaname}].organisation
		ON ((customer.Organization_Id = organisation.id)))
		LEFT JOIN [${dbxschemaname}].businesstype ON ((businesstype.id = organisation.BusinessType_id)))
		INNER JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		LEFT JOIN [${dbxschemaname}].status AS customerstatus
		ON ((customer.Status_id = customerstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS maritalstatus
		ON ((customer.MaritalStatus_id = maritalstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS employementstatus
		ON ((customer.EmployementStatus_id = employementstatus.id)))
		WHERE customer.id = @_customerId
END;
GO
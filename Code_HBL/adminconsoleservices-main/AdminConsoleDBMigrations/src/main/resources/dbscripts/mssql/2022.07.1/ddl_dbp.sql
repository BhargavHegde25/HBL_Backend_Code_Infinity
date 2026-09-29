USE [${dbxschemaname}]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER  PROCEDURE [${dbxschemaname}].[contract_address_communication_proc]  
   @_contractId nvarchar(max)
AS 
BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      SELECT c.*, 
                 s.name as servicedefinitionName 
                 from [${dbxschemaname}].contract c, [${dbxschemaname}].servicedefinition s
      WHERE c.servicedefinitionId = s.id AND c.Id = @_contractId
      
      SELECT * from  
      [${dbxschemaname}].contractcommunication
      WHERE contractcommunication.contractId = @_contractId
      
      SELECT * from  
      [${dbxschemaname}].address
      WHERE address.id IN (SELECT DISTINCT contractaddress.addressId FROM [${dbxschemaname}].contractaddress WHERE contractaddress.contractId = @_contractId);
                 
END;
GO
   
   
   
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER  PROCEDURE [${dbxschemaname}].[contract_corecustomer_accounts_proc]  
   @_contractId nvarchar(max)
AS 
BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
      SELECT * from  
      [${dbxschemaname}].contractcorecustomers
      WHERE [${dbxschemaname}].contractcorecustomers.contractId  = @_contractId
      
      SELECT * from  
      [${dbxschemaname}].contractaccounts
      WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId

END;
Go
   
   
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_search_proc];
Go 

   
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contract_search_proc](
               @_contractId nvarchar(50),
               @_contractName nvarchar(50),
               @_coreCustomerId nvarchar(50),
               @_coreCustomerName nvarchar(50),
               @_email nvarchar(50),
               @_phoneCountryCode nvarchar(50),
               @_phoneNumber nvarchar(50),
               @_country nvarchar(50),
               @_serviceDefinitionId nvarchar(50)
) AS 

BEGIN

DECLARE @isWhereAppened BIT;
DECLARE @shouldAndAppend BIT;
DECLARE @select_statement NVARCHAR(MAX);
DECLARE @Select_sub_query NVARCHAR(MAX);
SET @isWhereAppened = 0;
SET @shouldAndAppend = 0;

SET @select_statement = 'SELECT 
               contract.id as contractId , 
               contract.name as contractName , 
               contract.servicedefinitionId as serviceDefinitionId ,
               (select TOP 1 servicedefinition.name from [${dbxschemaname}].servicedefinition where servicedefinition.id = contract.servicedefinitionId) as serviceDefinitionName,
               (select TOP 1 contractcommunication.value from [${dbxschemaname}].contractcommunication where contractcommunication.contractId = contract.id and contractcommunication.typeId = ''COMM_TYPE_EMAIL'') as email ,
                                                (select TOP 1 contractcorecustomers.coreCustomerName from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.contractId = contract.id) as coreCustomerName
               FROM [${dbxschemaname}].contract
               WHERE '
               
               SET @isWhereAppened = 1;
                              
               IF(@_contractId != '') BEGIN
                              SET @Select_sub_query = CONCAT(' contract.id = ' , '''' , @_contractId , '''');
                              SET @shouldAndAppend = 1;
               END;

               IF(@_contractName != '') BEGIN
                              IF(@isWhereAppened = 1 and @shouldAndAppend = 1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.name like ', '''' , '%',@_contractName,'%' , '''' );
                              SET @shouldAndAppend = 1;
               END;
               
               IF(@_serviceDefinitionId != '') BEGIN
                              IF(@isWhereAppened = 1 and @shouldAndAppend = 1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.servicedefinitionId = ','''' + @_serviceDefinitionId + '''' );
                              SET @shouldAndAppend = 1;
               END;
               
               IF(@_coreCustomerId != '') BEGIN
                              IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.id IN (select DISTINCT contractcorecustomers.contractId from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.coreCustomerId =', '''' + @_coreCustomerId + ''')' );
                              SET @shouldAndAppend = 1;
               END;

               IF(@_coreCustomerName != '') BEGIN
                              IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.id IN (select DISTINCT contractcorecustomers.contractId from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.coreCustomerName like ', '''' , '%', @_coreCustomerName, '%' , ''')');
                              SET @shouldAndAppend = 1;
               END;

               IF(@_email != '') BEGIN
                              IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.id IN (select DISTINCT contractcommunication.contractId from [${dbxschemaname}].contractcommunication where contractcommunication.typeId = ''COMM_TYPE_EMAIL'' and contractcommunication.value = ', '''' + @_email + ''')' );
                              SET @shouldAndAppend = 1;
               END;

               IF(@_phoneCountryCode != '' and @_phoneNumber!= '') BEGIN
                              IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.id IN (select DISTINCT contractcommunication.contractId from [${dbxschemaname}].contractcommunication where contractcommunication.typeId = ''COMM_TYPE_PHONE'' and contractcommunication.value = ', '''' + @_phoneNumber + '''' ,' and contractcommunication.phoneCountryCode = ', '''' + @_phoneCountryCode + ''')' );
                              SET @shouldAndAppend = 1;
               END;

               IF(@_country != '') BEGIN
                              IF(@isWhereAppened=1 and @shouldAndAppend=1) BEGIN
                                             SET @Select_sub_query = CONCAT(@Select_sub_query , ' and');
                              END;
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' contract.id IN (select DISTINCT contractaddress.contractId FROM [${dbxschemaname}].contractaddress WHERE contractaddress.addressId IN (select DISTINCT address.id FROM [${dbxschemaname}].address WHERE address.country = ', '''' + @_country + '''', '))' );
                              SET @shouldAndAppend = 1;
               END;
               
               IF (@Select_sub_query != '')
                              SET @select_statement = @select_statement + ' ( ' + @Select_sub_query + ' );' ;

               exec(@select_statement);
END;
GO




DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_users_details_get_proc];
GO 



CREATE PROCEDURE [${dbxschemaname}].[contract_users_details_get_proc]
@_contractId nvarchar(50),
@_backendType nvarchar(50)
AS
BEGIN
​
SET XACT_ABORT ON
SET NOCOUNT ON
​
DECLARE
@customers nvarchar(max) = N''
​
SET @customers = (SELECT
String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].contractcustomers WHERE
[${dbxschemaname}].contractcustomers.contractId = @_contractId)

SET @customers = [${dbxschemaname}].DISTINCT_VALUE(@customers);
​
SELECT
customer.id AS customerId,
customer.FirstName AS firstName,
customer.MiddleName AS middleName,
customer.LastName AS lastName,
customer.UserName AS userName,
customer.Status_id AS statusId,
customer.DateOfBirth AS dateOfBirth,
customer.Ssn AS Ssn,
BI.BackendId AS primaryCoreCustomerId,
CC.Value AS Email
FROM
[${dbxschemaname}].customer
LEFT JOIN [${dbxschemaname}].customercommunication CC ON (CC.Customer_id = customer.id AND CC.Type_id = 'COMM_TYPE_EMAIL' AND CC.isPrimary = 1 AND CC.Customer_id IN (@customers))
LEFT JOIN [${dbxschemaname}].backendidentifier BI ON (BI.Customer_id = customer.id AND BI.BackendType = @_backendType and BI.Customer_id IN (@customers))
WHERE
customer.id IN (SELECT DISTINCT value FROM STRING_SPLIT(@customers, ','));
​
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_roles_for_servicedefs_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_roles_for_servicedefs_proc]  
	@_servicedefList NVARCHAR(MAX)
AS BEGIN

DECLARE @sql_query NVARCHAR(MAX);

IF (@_servicedefList != '' and @_servicedefList IS NOT NULL) BEGIN
set @sql_query = 'select * from [${dbxschemaname}].groupservicedefinition where serviceDefinitionId in ( select value from STRING_SPLIT(''' + @_servicedefList + ''' , '',''));';

END


EXEC(@sql_query);

END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_withaccounts_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[user_customers_withaccounts_proc] 
@_customerId nvarchar(50),
@_coreCustomerId nvarchar(50)
AS BEGIN
declare @select_statement nvarchar(MAX);
declare @isWhereAppened nvarchar(100);
declare @shouldAndAppend nvarchar(100);
SET @select_statement = '(SELECT
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
membergroup.Name AS userRole,
contractaccounts.accountId,
contractaccounts.accountName,
contractaccounts.statusDesc,
contractaccounts.typeId
FROM
[${dbxschemaname}].contractcustomers
LEFT JOIN [${dbxschemaname}].contract ON ([${dbxschemaname}].contract.id = [${dbxschemaname}].contractcustomers.contractId)
LEFT JOIN [${dbxschemaname}].contractcorecustomers ON (([${dbxschemaname}].contractcorecustomers.contractId = [${dbxschemaname}].contractcustomers.contractId)
AND ([${dbxschemaname}].contractcorecustomers.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].contractaccounts ON ((contractaccounts.contractId = contractcustomers.contractId)
AND (contractaccounts.coreCustomerId = contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].servicedefinition ON ([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].contract.servicedefinitionId)
LEFT JOIN [${dbxschemaname}].membergrouptype ON ([${dbxschemaname}].membergrouptype.id = [${dbxschemaname}].servicedefinition.serviceType)
LEFT JOIN [${dbxschemaname}].customergroup ON (([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].contractcustomers.customerId)
AND ([${dbxschemaname}].customergroup.contractId = [${dbxschemaname}].contractcustomers.contractId)
AND ([${dbxschemaname}].customergroup.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))
LEFT JOIN [${dbxschemaname}].membergroup ON ([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id)';

SET @isWhereAppened = 'false';
SET @shouldAndAppend = 'false';
IF(@_customerId != '') BEGIN
IF(@isWhereAppened = 'false') BEGIN
SET @select_statement = CONCAT(@select_statement , ' where');
SET @isWhereAppened = 'true';
SET @shouldAndAppend = 'true';
END;
set @select_statement = concat(@select_statement ,' contractcustomers.customerId = ', ''''+@_customerId+'''');
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
set @select_statement = concat(@select_statement ,' contractcustomers.coreCustomerId = ',''''+@_coreCustomerId)+'''';
END;

set @select_statement = concat(@select_statement ,');');

exec(@select_statement);
END;

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[infinityuser_contractdetails_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[infinityuser_contractdetails_get_proc] (
	@_id nvarchar(50)
)AS BEGIN
	select
		[contract].[id] AS contractId,
        [contract].[name] AS contractName,
        [contract].[servicedefinitionId] AS servicedefinitionId,
        [servicedefinition].[name] AS serviceDefinitionName,
        [membergrouptype].[description] AS serviceDefinitionType,
        [contractcorecustomers].[coreCustomerId] AS coreCustomerId,
		[contractcorecustomers].[coreCustomerName] AS coreCustomerName,
        [customergroup].[Group_id] AS userRole,
        [membergroup].[name] AS userRoleName,
        IIF( ( ([customergroup].[Group_id] = null or [customergroup].[Group_id] =  '') AND ([membergroup].[name] = '' or [membergroup].[name] = null)),'false','true') AS isAssociated
	FROM
		[${dbxschemaname}].[contract]
        LEFT JOIN [${dbxschemaname}].[contractcustomers] ON ([contractcustomers].[contractId] = [contract].[id] and [contractcustomers].[customerId] = @_id)
        LEFT JOIN [${dbxschemaname}].[servicedefinition] ON ([servicedefinition].[id] = [contract].[servicedefinitionId])
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON ([membergrouptype].[id] = [servicedefinition].[serviceType])
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ([contractcorecustomers].[contractId] = [contract].[id])
        LEFT JOIN [${dbxschemaname}].[customergroup] ON ([customergroup].[contractId] = [contract].[id] AND 
			[customergroup].[coreCustomerId] = [contractcorecustomers].[coreCustomerId] AND 
			[customergroup].[Customer_id] = @_id)
        LEFT JOIN [${dbxschemaname}].[membergroup] ON ([membergroup].[id] = [customergroup].[Group_id])
	WHERE
		[customergroup].[Customer_id] = @_id
        AND [contractcustomers].[customerId] = @_id
		
/*	UNION
    select
		[contract].[id] AS contractId,
        [contract].[name] AS contractName,
        [contract].[servicedefinitionId] AS servicedefinitionId,
        [servicedefinition].[name] AS serviceDefinitionName,
        [membergrouptype].[description] AS serviceDefinitionType,
        [contractcorecustomers].[coreCustomerId] AS coreCustomerId,
        NULL AS userRole,
        NULL AS userRoleName,
        'false' AS isAssociated
	FROM
		[${dbxschemaname}].[contract]
        LEFT JOIN [${dbxschemaname}].[servicedefinition] ON ([servicedefinition].[id] = [contract].[servicedefinitionId])
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON ([membergrouptype].[id] = [servicedefinition].[serviceType])
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] ON ([contractcorecustomers].[contractId] = [contract].[id])
	WHERE
		[contractcorecustomers].[contractId] IN ( select [contractcustomers].[contractId] FROM dbxdb.[contractcustomers] WHERE [contractcustomers].[customerId ]= '48c0f681-26b4-468d-bb2a-c64b1bc67f97');
		*/
END;
GO



CREATE  PROCEDURE [${dbxschemaname}].[get_associated_contractusers_proc](
	@_id NVARCHAR(MAX),
	@_backendType NVARCHAR(MAX)
) AS BEGIN

	select ca.Customer_id as id,
	ca.contractId as contractId,
	con.name as contractName,
	ca.coreCustomerId as coreCustomerId,
	concore.coreCustomerName as coreCustomerName,
	cc.customerId as customerId,
	cg.Group_id as groupId,
	cus.FirstName as firstName,
	cus.LastName as lastName,
	cus.UserName as userName ,
	cus.Lastlogintime as lastlogintime,
	cus.Status_id as statusId,
	b.BackendId as backendId
	from 
	[customeraction] ca
	left join [${dbxschemaname}].contractcustomers cc on
	(ca.contractId = cc.contractId and ca.coreCustomerId = cc.coreCustomerId)
	left join [${dbxschemaname}].contractcorecustomers concore on
	(ca.contractId = concore.contractId and ca.coreCustomerId = concore.coreCustomerId)
	left join [${dbxschemaname}].contract con on
	(concore.contractId = con.id)
	left join [${dbxschemaname}].customergroup cg on
	(cc.contractId = cg.contractId and cc.coreCustomerId = cg.coreCustomerId and cg.Customer_id = cc.customerId)
	left join [${dbxschemaname}].customer cus on
	(cg.Customer_id = cus.id)
	left join [${dbxschemaname}].backendidentifier b on
	(b.Customer_id = cus.id and b.BackendType = @_backendType)
	WHERE ca.Customer_id = @_id and ca.Action_id = 'USER_MANAGEMENT_VIEW';
	
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_associated_contractaccounts_proc];

GO


CREATE   PROCEDURE [${dbxschemaname}].[get_associated_contractaccounts_proc](
	@_accountIdList NVARCHAR(MAX),
	@_coreCustomerId NVARCHAR(MAX)
) AS BEGIN

    DECLARE @accountIdList NVARCHAR(MAX);
    DECLARE @excludedaccountIdList NVARCHAR(MAX);

	DECLARE @coreCustomerAccounts NVARCHAR(MAX);
	DECLARE @otherCoreCustomerAccounts NVARCHAR(MAX);
    
	SET @accountIdList = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].[contractaccounts].accountId in (select distinct value from STRING_SPLIT(@_accountIdList,',')));

							
							
	SET @excludedaccountIdList = (SELECT STRING_AGG([excludedcontractaccounts].[accountId], ',') from [${dbxschemaname}].[excludedcontractaccounts] 
							WHERE [${dbxschemaname}].[excludedcontractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) );
	
	SET @coreCustomerAccounts = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].[contractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) 
							and [${dbxschemaname}].[contractaccounts].[coreCustomerId] = @_coreCustomerId);

	SET @otherCoreCustomerAccounts = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].[contractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) 
							and [${dbxschemaname}].[contractaccounts].[coreCustomerId] <> @_coreCustomerId);
							
	select @accountIdList As accountIdList;
	select @excludedaccountIdList As excludedaccountIdList;
	select @coreCustomerAccounts As coreCustomerAccounts;
	select @otherCoreCustomerAccounts As otherCoreCustomerAccounts;

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[useractions_create_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].useractions_create_proc  
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
         
         
     


    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (
            SELECT String_agg(CAST(customeraccounts.Account_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraccounts WHERE 
                               [${dbxschemaname}].customeraccounts.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraccounts.contractId = @_contractId AND
                               [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId
             )
           
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT customeraccounts.Account_id FROM [${dbxschemaname}].customeraccounts WHERE 
      customeraccounts.contractId = @_contractId AND
      customeraccounts.coreCustomerId = @_coreCustomerId AND
      customeraccounts.Customer_id = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
    
    SET @serviceDefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)
    SET @serviceType = (SELECT servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE servicedefinition.id = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition
						WHERE groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND groupservicedefinition.isDefaultGroup = '1')
						 
    SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction)
						
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                             
    SET @_groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId)  

                                
    SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
                                
    SET @validActionsList =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
                               
	 	  DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '1'));
         
          DECLARE nonaccountlevelactions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '0'));
         
     
    OPEN accounts     
	FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
	OPEN actions
	FETCH NEXT FROM actions into @featureActionId
	 
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
         	
	      SET @entryStatus = 0
	      DECLARE limits CURSOR LOCAL
	  FOR (select LimitType_id from [${dbxschemaname}].actionlimit where Action_id = @featureActionId);
	OPEN limits
	FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN
	        SET @limitvalue = (SELECT contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE contractactionlimit.actionId = @featureActionId AND 
                               contractactionlimit.limitTypeId = @limitId AND
                               contractactionlimit.contractId = @_contractId AND
                               contractactionlimit.coreCustomerId = @_coreCustomerId)

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
                                             [${dbxschemaname}].customeraction.[value])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
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
                                           [${dbxschemaname}].customeraction.[value])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
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
                                      [${dbxschemaname}].customeraction.[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                                      
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
                                      [${dbxschemaname}].customeraction.[value])
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
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.Account_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1);
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
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );

          SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,1);
			FETCH NEXT FROM nonaccountlevelactions into @featureActionId
            CONTINUE

          END          
          
    CLOSE nonaccountlevelactions
		   DEALLOCATE nonaccountlevelactions
   END;
GO

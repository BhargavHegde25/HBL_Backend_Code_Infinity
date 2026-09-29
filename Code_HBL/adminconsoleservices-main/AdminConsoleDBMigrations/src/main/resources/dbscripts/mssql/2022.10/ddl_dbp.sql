DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_accountactions_get_proc;
GO
CREATE      PROCEDURE [${dbxschemaname}].[user_accountactions_get_proc]  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50)
    AS 
    BEGIN

    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
          
    DECLARE @contractId nvarchar(255) = N'' 
    DECLARE @serviceDefinitionId nvarchar(255) = N'' 
    DECLARE @groupId nvarchar(255) = N'' 
    DECLARE @validFIAccountLevelActions nvarchar(max) = N'' 
    DECLARE @validServiceDefinitinActions nvarchar(max) = N'' 
    DECLARE @validGroupActions nvarchar(max) = N'' 
    DECLARE @validUserActions nvarchar(max) = N'' 
    DECLARE @actionCondition nvarchar(max) = N'' 
    DECLARE @select_statement nvarchar(max) = N'' 
          
-- SET @contractId =  (SELECT contractcorecustomers.contractId FROM [${dbxschemaname}].contractcorecustomers where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId)  
               select @contractId = contractcorecustomers.contractId
                              from [${dbxschemaname}].contractcorecustomers
                              where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId;
               
--  SET @serviceDefinitionId =  (SELECT contract.servicedefinitionId FROM [${dbxschemaname}].contract where [${dbxschemaname}].contract.id = @contractId)  
               select @serviceDefinitionId = contract.servicedefinitionId
                              from [${dbxschemaname}].contract
                              where [${dbxschemaname}].contract.id = @contractId;
               
--  SET @groupId =  (SELECT customergroup.Group_id FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.contractId = @contractId AND [${dbxschemaname}].customergroup.coreCustomerId = @_coreCustomerId AND dbxschemaname].customergroup.Customer_id = @_userId)  
               select @groupId = customergroup.Group_id
               from [${dbxschemaname}].customergroup
               where
                              [${dbxschemaname}].customergroup.contractId = @contractId and
                              [${dbxschemaname}].customergroup.coreCustomerId = @_coreCustomerId and 
                              [${dbxschemaname}].customergroup.Customer_id = @_userId;
                              
--  SET @groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  
    select @groupId = groupservicedefinition.Group_id
                              from [${dbxschemaname}].groupservicedefinition
                              where
                                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId and
                                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId;
                                             
    IF @groupId IS NULL
                              SET @groupId = N''
                                          
--    SET @validFIAccountLevelActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
--                                   ([${dbxschemaname}].featureaction.isAccountLevel = '1' OR [${dbxschemaname}].featureaction.isAccountLevel = 'true') AND
--                                   [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE')
                                                                   
--    SET @validServiceDefinitinActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
--                                   [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
--                                   [${dbxschemaname}].servicedefinitionactionlimit.actionId in (select distinct value from STRING_SPLIT(@validFIAccountLevelActions, ',')))
                                                                   
                                    
--    SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
--                                   [${dbxschemaname}].groupactionlimit.Group_id = @groupId AND
--                                   [${dbxschemaname}].groupactionlimit.Action_id in (select distinct value from STRING_SPLIT(@validServiceDefinitinActions, ',')))

               select @validGroupActions = String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',')
                              from [${dbxschemaname}].groupactionlimit
                              join [${dbxschemaname}].servicedefinitionactionlimit on ([${dbxschemaname}].groupactionlimit.Action_id = [${dbxschemaname}].servicedefinitionactionlimit.actionId)
                              join [${dbxschemaname}].featureaction on ([${dbxschemaname}].featureaction.id = [${dbxschemaname}].servicedefinitionactionlimit.actionId)
                              where 
                                             [${dbxschemaname}].groupactionlimit.Group_id = @groupId and
                                             [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId and
                                             [${dbxschemaname}].featureaction.isAccountLevel = 1 and
                                             [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE';
                                                                    
--    SET @validUserActions =  (SELECT String_agg(CAST(Action_id AS nvarchar(max)), ',') FROM
--                                                                                                                     (select distinct [${dbxschemaname}].customeraction.Action_id from [${dbxschemaname}].customeraction WHERE 
--                                   [${dbxschemaname}].customeraction.Customer_id = @_userId AND
--                                   [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND
--                                   [${dbxschemaname}].customeraction.contractId = @contractId AND
--                                   ([${dbxschemaname}].customeraction.isAllowed = '1'  OR [${dbxschemaname}].customeraction.isAllowed = 'true') AND
--                                   [${dbxschemaname}].customeraction.Action_id in (select distinct value from STRING_SPLIT(@validGroupActions, ',')))
--                                                                                                         as x);
                                   
    
-- SET @actionCondition = (N'[${dbxschemaname}].')+(N'customeraction.Action_id in (select distinct value from STRING_SPLIT(') + (@validUserActions) + (N''','')')
    
    
               SELECT 
                              customeraction.Customer_id AS Customer_id,
                              customeraction.contractId AS contractId,
                              customeraction.coreCustomerId AS coreCustomerId,
                              customeraction.Account_id AS Account_id,
                              customeraction.featureId AS featureId,
                              customeraction.Action_id AS Action_id,
                              customeraction.RoleType_id AS RoleType_id,
                              customeraction.LimitType_id AS LimitType_id,
                              customeraction.value AS value
               from [${dbxschemaname}].customeraction 
               where 
                              [${dbxschemaname}].customeraction.Customer_id = @_userId and
                              ([${dbxschemaname}].customeraction.isAllowed =1) and
               [${dbxschemaname}].customeraction.Action_id in (select distinct value from STRING_SPLIT(@validGroupActions, ',')) and 
                              [${dbxschemaname}].customeraction.coreCustomerId =  @_coreCustomerId and 
                              [${dbxschemaname}].customeraction.contractId = @contractId;
    
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_associated_corecustomeraccounts_info;
GO

CREATE   PROCEDURE [${dbxschemaname}].[user_associated_corecustomeraccounts_info](
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
                              
        SELECT [contractaccounts].[accountId] AS contractaccounts
        FROM [${dbxschemaname}].[contractaccounts]
        WHERE [${dbxschemaname}].[contractaccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ',')); 
        
        SELECT [excludedcontractaccounts].[accountId] AS excludedcontractaccounts
        FROM [${dbxschemaname}].[excludedcontractaccounts]
        WHERE [${dbxschemaname}].[excludedcontractaccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','));
                                
        SELECT [${dbxschemaname}].[customeraccounts].[Account_id] AS customeraccounts
        FROM [${dbxschemaname}].[customeraccounts]
        WHERE [${dbxschemaname}].[customeraccounts].[coreCustomerId] IN (SELECT DISTINCT value FROM STRING_SPLIT(@implictCIF, ','))
        AND [${dbxschemaname}].[customeraccounts].[Customer_id] = @customerId; 
        
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


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[infinityuser_contractdetails_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[infinityuser_contractdetails_get_proc](
	@_id nvarchar(50),
	@_legalEntityId nvarchar(50)
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
        [contract].[companyLegalUnit] AS legalEntityId,
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
		AND [contract].[companyLegalUnit] = @_legalEntityId;
        
/*    UNION
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
        [contractcorecustomers].[contractId] IN ( select [contractcustomers].[contractId] FROM dbxschemaname.[contractcustomers] WHERE [contractcustomers].[customerId ]= '48c0f681-26b4-468d-bb2a-c64b1bc67f97');
        */
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_search_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_search_proc](
               @_contractId nvarchar(50),
               @_contractName nvarchar(50),
               @_coreCustomerId nvarchar(50),
               @_coreCustomerName nvarchar(50),
               @_email nvarchar(50),
               @_phoneCountryCode nvarchar(50),
               @_phoneNumber nvarchar(50),
               @_country nvarchar(50),
               @_serviceDefinitionId nvarchar(50),
			   @_legalEntityId nvarchar(50)
) AS 

BEGIN

DECLARE @select_statement NVARCHAR(MAX);
DECLARE @Select_sub_query NVARCHAR(MAX);

SET @select_statement = CONCAT('SELECT 
               contract.id as contractId , 
               contract.name as contractName , 
               contract.servicedefinitionId as serviceDefinitionId ,
               (select TOP 1 servicedefinition.name from [${dbxschemaname}].servicedefinition where servicedefinition.id = contract.servicedefinitionId) as serviceDefinitionName,
               (select TOP 1 contractcommunication.value from [${dbxschemaname}].contractcommunication where contractcommunication.contractId = contract.id and contractcommunication.typeId = ''COMM_TYPE_EMAIL'') as email ,
                                                (select TOP 1 contractcorecustomers.coreCustomerName from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.contractId = contract.id) as coreCustomerName
               FROM [${dbxschemaname}].contract
               WHERE contract.companyLegalUnit = ', '''' , @_legalEntityId , '''');         
                              
               IF(@_contractId != '') BEGIN
                              SET @Select_sub_query = CONCAT(' and contract.id = ' , '''' , @_contractId , '''');
                              
               END;

               IF(@_contractName != '') BEGIN
                           
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.name like ', '''' , '%',@_contractName,'%' , '''' );
                              
               END;
               
               IF(@_serviceDefinitionId != '') BEGIN
                         
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.servicedefinitionId = ','''' + @_serviceDefinitionId + '''' );
                              
               END;
               
               IF(@_coreCustomerId != '') BEGIN
                      
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.id IN (select DISTINCT contractcorecustomers.contractId from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.coreCustomerId =', '''' + @_coreCustomerId + ''')' );
                              
               END;

               IF(@_coreCustomerName != '') BEGIN
                     
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.id IN (select DISTINCT contractcorecustomers.contractId from [${dbxschemaname}].contractcorecustomers where contractcorecustomers.coreCustomerName like ', '''' , '%', @_coreCustomerName, '%' , ''')');
                              
               END;

               IF(@_email != '') BEGIN
                    
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.id IN (select DISTINCT contractcommunication.contractId from [${dbxschemaname}].contractcommunication where contractcommunication.typeId = ''COMM_TYPE_EMAIL'' and contractcommunication.value = ', '''' + @_email + ''')' );
                              
               END;

               IF(@_phoneCountryCode != '' and @_phoneNumber!= '') BEGIN
                       
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.id IN (select DISTINCT contractcommunication.contractId from [${dbxschemaname}].contractcommunication where contractcommunication.typeId = ''COMM_TYPE_PHONE'' and contractcommunication.value = ', '''' + @_phoneNumber + '''' ,' and contractcommunication.phoneCountryCode = ', '''' + @_phoneCountryCode + ''')' );
                              
               END;

               IF(@_country != '') BEGIN
                          
                              SET @Select_sub_query = CONCAT(@Select_sub_query , ' and contract.id IN (select DISTINCT contractaddress.contractId FROM [${dbxschemaname}].contractaddress WHERE contractaddress.addressId IN (select DISTINCT address.id FROM [${dbxschemaname}].address WHERE address.country = ', '''' + @_country + '''', '))' );
                              
               END;
               
               IF (@Select_sub_query != '')
                              SET @select_statement = @select_statement + @Select_sub_query + ';' ;

               exec(@select_statement);
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_search_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50), 
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint,
   @_legalEntityId varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

             SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer where customer.companyLegalUnit = '+''''+@_legalEntityId+''''+ (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
                        ELSE N''
                     END)
            
                 
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                        + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id)
					LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id)
					LEFT JOIN [${dbxschemaname}].country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory,address.addressLine1, address.addressLine2, city.Name, address.zipCode, country.Name, customer.isEnrolled ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                        
                          
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              
                                 SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
        exec(@queryStatement)
     END;
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
			(SELECT membergroup.id FROM [${dbxschemaname}].membergroup WHERE membergroup.id in
			(SELECT customergroup.Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_RoleId,
			(SELECT membergroup.Name FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_Role,
			(SELECT membergroup.isEAgreementActive FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS isEAgreementRequired,
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
		WHERE customer.id = @_customerId and customer.companyLegalUnit = @_legalEntityId
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_withaccounts_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[user_customers_withaccounts_proc] 
@_customerId nvarchar(50),
@_coreCustomerId nvarchar(50),
@_legalEntityId nvarchar(100)
AS BEGIN
declare @select_statement nvarchar(MAX);
declare @isWhereAppened nvarchar(100);
declare @shouldAndAppend nvarchar(100);
SET @select_statement = '(SELECT
contractcustomers.customerId AS customerId,
contractcustomers.companyLegalUnit AS companyLegalUnit,
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

IF(@_legalEntityId != '') BEGIN
IF(@isWhereAppened = 'false') BEGIN
SET @select_statement = CONCAT(@select_statement , ' where');
SET @shouldAndAppend = 'true';
END;
IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
SET @select_statement = CONCAT(@select_statement , ' and');
SET @shouldAndAppend = 'true';
END;
set @select_statement = concat(@select_statement ,' contractcustomers.companyLegalUnit = ',''''+@_legalEntityId)+'''';
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_validcorecustomerslist_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[get_validcorecustomerslist_proc]  
   @_coreCustomersCSV nvarchar(max),
   @companyLegalUnitId nvarchar(200)
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
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @value and [${dbxschemaname}].contractcorecustomers.companyLegalUnit = @companyLegalUnitId)    
              
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


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[membership_customer_search_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[membership_customer_search_proc](
	@_id nvarchar(50),
	@_name nvarchar(50),
	@_email nvarchar(50),
	@_phone nvarchar(50),
	@_dateOfBirth nvarchar(50),
	@_status nvarchar(50),
	@_country varchar(50),
	@_city  varchar(50),
	@_zipCode nvarchar(50),
	@_legalEntityId nvarchar(50)
)
AS 
	BEGIN
	DECLARE @select_statement NVARCHAR(MAX);
	DECLARE @address_select_statement NVARCHAR(MAX);
	SET @select_statement = ('select [${dbxschemaname}].[membership].[id] , [${dbxschemaname}].[membership].[companyLegalUnit] , [${dbxschemaname}].[membership].[isBusinessType] , [${dbxschemaname}].[membership].[name], 
		[${dbxschemaname}].[membership].[industry] , [${dbxschemaname}].[membership].[firstName] , [${dbxschemaname}].[membership].[lastName] , 
		[${dbxschemaname}].[membership].[phone] , [${dbxschemaname}].[membership].[taxId], [${dbxschemaname}].[membership].[faxId], 
		[${dbxschemaname}].[membership].[email] , [${dbxschemaname}].[address].[addressLine1],
		[${dbxschemaname}].[address].[addressLine2] , [${dbxschemaname}].[address].[cityName] , [${dbxschemaname}].[address].[country] , 
		[${dbxschemaname}].[address].[zipCode] , [${dbxschemaname}].[address].[state]
		from [${dbxschemaname}].[membership] LEFT JOIN [${dbxschemaname}].[address] on ([${dbxschemaname}].[membership].[addressId] = [${dbxschemaname}].[address].[id])
		where [${dbxschemaname}].[membership].[id] is not null and [${dbxschemaname}].[membership].[companyLegalUnit] = ''' + @_legalEntityId + '''');
	
	IF(ISNULL(@_id,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[id] = ','''' + @_id + '''');
    END;
    IF(ISNULL(@_name,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[name] LIKE %',@_name,'%');
    END;
    IF(ISNULL(@_email,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[email] = ','''' + @_email + '''');
    END;
    IF(ISNULL(@_phone,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[phone] = ','''' + @_phone+'''');
    END;
    IF(ISNULL(@_dateOfBirth,'') != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[dateOfBirth] = ','''' + @_dateOfBirth + '''');
    END;	
	SET @address_select_statement = '';
	IF(ISNULL(@_city,'') != ''  OR ISNULL(@_country,'') != '' OR ISNULL(@_zipCode,'') != '') BEGIN
		SET @address_select_statement = 'select address.id from address where address.id is not null';
        IF(ISNULL(@_city,'') != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[cityName] = ','''' + @_city + '''');
        END;
        IF(ISNULL(@_country,'') != '') BEGIN
			SET @address_select_statement = CONCAT(@address_select_statement , ' and [${dbxschemaname}].[address].[country] = ','''' + @_country+'''');
        END;
        IF(ISNULL(@_zipCode,'') != '') BEGIN
			SET @address_select_statement = concat(@address_select_statement , ' and [${dbxschemaname}].[address].[zipCode] = ','''' + @_zipCode + '''');
        END;
    END;
	IF(ISNULL(@address_select_statement,'') != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' and [${dbxschemaname}].[membership].[addressId] in (", @address_select_statement , ")');
    END;

	SET @select_statement = CONCAT(@select_statement , ';');


    exec(@select_statement);
END;
GO


ALTER TABLE [${dbxschemaname}].[membership] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_customers_proc]
        @_customerId nvarchar(50),
        @_coreCustomerId nvarchar(50),
		@_legalEntityId nvarchar(100)
    AS BEGIN
        declare @select_statement nvarchar(MAX);
        declare @isWhereAppened nvarchar(100);
        declare @shouldAndAppend nvarchar(100);
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
        set @select_statement =  concat(@select_statement ,');');
        exec(@select_statement);
    END;
GO	

ALTER TABLE [${dbxschemaname}].[customroleaccounts] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[excludedcustomroleaccounts] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] ADD [companyLegalUnit] VARCHAR(50) DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[excludedcustomroleactionlimits] ADD [companyLegalUnit] NVARCHAR(50) DEFAULT 'ALL';
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].get_associated_contractusers_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[get_associated_contractusers_proc](
	@_id NVARCHAR(MAX),
	@_backendType NVARCHAR(MAX),
	@_legalEntityId NVARCHAR(MAX)
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
	b.BackendId as backendId,
	ca.companyLegalUnit as companyLegalUnit
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
	WHERE ca.Customer_id = @_id and ca.Action_id = 'USER_MANAGEMENT_VIEW' and ca.companyLegalUnit = @_legalEntityId;
	
END;
GO
ALTER TABLE [${dbxschemaname}].[customview] ADD [legalEntityId] NVARCHAR(50) DEFAULT 'ALL';
GO
DROP VIEW IF EXISTS  [${dbxschemaname}].[customeraccountsview];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].customeraccountsview (
   [Membership_id], 
   [MembershipName], 
   [Taxid], 
   [Customer_id], 
   [User_id], 
   [Account_id], 
   [isBusinessAccount], 
   [Type_id], 
   [userName], 
   [currencyCode], 
   [accountHolder], 
   [error], 
   [Address], 
   [Scheme], 
   [number], 
   [availableBalance], 
   [currentBalance], 
   [interestRate], 
   [availableCredit], 
   [minimumDue], 
   [dueDate], 
   [firstPaymentDate], 
   [closingDate], 
   [paymentTerm], 
   [openingDate], 
   [maturityDate], 
   [dividendLastPaidAmount], 
   [dividendLastPaidDate], 
   [dividendPaidYTD], 
   [dividendRate], 
   [dividendYTD], 
   [eStatementEnable], 
   [isOrganizationAccount], 
   [favouriteStatus], 
   [statusDesc], 
   [nickName], 
   [originalAmount], 
   [outstandingBalance], 
   [paymentDue], 
   [paymentMethod], 
   [swiftCode], 
   [totalCreditMonths], 
   [totalDebitsMonth], 
   [routingNumber], 
   [supportBillPay], 
   [supportCardlessCash], 
   [supportTransferFrom], 
   [supportTransferTo], 
   [supportDeposit], 
   [unpaidInterest], 
   [previousYearsDividends], 
   [principalBalance], 
   [principalValue], 
   [regularPaymentAmount], 
   [phoneId], 
   [lastDividendPaidDate], 
   [lastDividendPaidAmount], 
   [lastPaymentAmount], 
   [lastPaymentDate], 
   [lastStatementBalance], 
   [lateFeesDue], 
   [maturityAmount], 
   [maturityOption], 
   [payoffAmount], 
   [payOffCharge], 
   [pendingDeposit], 
   [pendingWithdrawal], 
   [jointHolders], 
   [isPFM], 
   [interestPaidYTD], 
   [interestPaidPreviousYTD], 
   [interestPaidLastYear], 
   [interestEarned], 
   [currentAmountDue], 
   [creditLimit], 
   [creditCardNumber], 
   [bsbNum], 
   [bondInterestLastYear], 
   [bondInterest], 
   [availablePoints], 
   [accountName], 
   [email], 
   [IBAN], 
   [adminProductId], 
   [UpdatedBy], 
   [LastUpdated], 
   [ActualUpdatedBY], 
   [bankname], 
   [accountPreference], 
   [transactionLimit], 
   [transferLimit], 
   [rates], 
   [termsAndConditions], 
   [typeDescription], 
   [supportChecks], 
   [displayName], 
   [accountSubType], 
   [description], 
   [schemeName], 
   [identification], 
   [secondaryIdentification], 
   [servicerSchemeName], 
   [servicerIdentification], 
   [dataCreditDebitIndicator], 
   [dataType], 
   [dataDateTime], 
   [dataCreditLineIncluded], 
   [dataCreditLineType], 
   [dataCreditLineAmount], 
   [dataCreditLineCurrency],
   [legalEntityId])
AS 
   SELECT DISTINCT 
      [${dbxschemaname}].contractcorecustomers.coreCustomerId AS Membership_id, 
      [${dbxschemaname}].contractcorecustomers.coreCustomerName AS MembershipName, 
      [${dbxschemaname}].accounts.TaxId AS Taxid, 
      [${dbxschemaname}].customeraccounts.Customer_id AS Customer_id, 
      [${dbxschemaname}].customeraccounts.Customer_id AS User_id, 
      [${dbxschemaname}].accounts.Account_id AS Account_id, 
      [${dbxschemaname}].contractcorecustomers.isBusiness AS isBusinessAccount, 
      [${dbxschemaname}].accounts.Type_id AS Type_id, 
      [${dbxschemaname}].accounts.UserName AS userName, 
      [${dbxschemaname}].accounts.CurrencyCode AS currencyCode, 
      [${dbxschemaname}].accounts.AccountHolder AS accountHolder, 
      [${dbxschemaname}].accounts.error AS error, 
      [${dbxschemaname}].accounts.Address AS Address, 
      [${dbxschemaname}].accounts.Scheme AS Scheme, 
      [${dbxschemaname}].accounts.Number AS number, 
      [${dbxschemaname}].accounts.AvailableBalance AS availableBalance, 
      [${dbxschemaname}].accounts.CurrentBalance AS currentBalance, 
      [${dbxschemaname}].accounts.InterestRate AS interestRate, 
      [${dbxschemaname}].accounts.AvailableCredit AS availableCredit, 
      [${dbxschemaname}].accounts.MinimumDue AS minimumDue, 
      [${dbxschemaname}].accounts.DueDate AS dueDate, 
      [${dbxschemaname}].accounts.FirstPaymentDate AS firstPaymentDate, 
      [${dbxschemaname}].accounts.ClosingDate AS closingDate, 
      [${dbxschemaname}].accounts.PaymentTerm AS paymentTerm, 
      [${dbxschemaname}].accounts.OpeningDate AS openingDate, 
      [${dbxschemaname}].accounts.MaturityDate AS maturityDate, 
      [${dbxschemaname}].accounts.DividendLastPaidAmount AS dividendLastPaidAmount, 
      [${dbxschemaname}].accounts.DividendLastPaidDate AS dividendLastPaidDate, 
      [${dbxschemaname}].accounts.DividendPaidYTD AS dividendPaidYTD, 
      [${dbxschemaname}].accounts.DividendRate AS dividendRate, 
      [${dbxschemaname}].accounts.DividendYTD AS dividendYTD, 
      [${dbxschemaname}].customeraccounts.EStatementmentEnable AS eStatementEnable, 
      [${dbxschemaname}].customeraccounts.IsOrganizationAccount AS isOrganizationAccount, 
      [${dbxschemaname}].customeraccounts.FavouriteStatus AS favouriteStatus, 
      [${dbxschemaname}].accounts.StatusDesc AS statusDesc, 
      [${dbxschemaname}].accounts.NickName AS nickName, 
      [${dbxschemaname}].accounts.OriginalAmount AS originalAmount, 
      [${dbxschemaname}].accounts.OutstandingBalance AS outstandingBalance, 
      [${dbxschemaname}].accounts.PaymentDue AS paymentDue, 
      [${dbxschemaname}].accounts.PaymentMethod AS paymentMethod, 
      [${dbxschemaname}].accounts.SwiftCode AS swiftCode, 
      [${dbxschemaname}].accounts.TotalCreditMonths AS totalCreditMonths, 
      [${dbxschemaname}].accounts.TotalDebitsMonth AS totalDebitsMonth, 
      [${dbxschemaname}].accounts.RoutingNumber AS routingNumber, 
      [${dbxschemaname}].accounts.SupportBillPay AS supportBillPay, 
      [${dbxschemaname}].accounts.SupportCardlessCash AS supportCardlessCash, 
      [${dbxschemaname}].accounts.SupportTransferFrom AS supportTransferFrom, 
      [${dbxschemaname}].accounts.SupportTransferTo AS supportTransferTo, 
      [${dbxschemaname}].accounts.SupportDeposit AS supportDeposit, 
      [${dbxschemaname}].accounts.UnpaidInterest AS unpaidInterest, 
      [${dbxschemaname}].accounts.PreviousYearsDividends AS previousYearsDividends, 
      [${dbxschemaname}].accounts.principalBalance AS principalBalance, 
      [${dbxschemaname}].accounts.PrincipalValue AS principalValue, 
      [${dbxschemaname}].accounts.RegularPaymentAmount AS regularPaymentAmount, 
      [${dbxschemaname}].accounts.phone AS phoneId, 
      [${dbxschemaname}].accounts.LastDividendPaidDate AS lastDividendPaidDate, 
      [${dbxschemaname}].accounts.LastDividendPaidAmount AS lastDividendPaidAmount, 
      [${dbxschemaname}].accounts.LastPaymentAmount AS lastPaymentAmount, 
      [${dbxschemaname}].accounts.LastPaymentDate AS lastPaymentDate, 
      [${dbxschemaname}].accounts.LastStatementBalance AS lastStatementBalance, 
      [${dbxschemaname}].accounts.LateFeesDue AS lateFeesDue, 
      [${dbxschemaname}].accounts.maturityAmount AS maturityAmount, 
      [${dbxschemaname}].accounts.MaturityOption AS maturityOption, 
      [${dbxschemaname}].accounts.payoffAmount AS payoffAmount, 
      [${dbxschemaname}].accounts.PayOffCharge AS payOffCharge, 
      [${dbxschemaname}].accounts.PendingDeposit AS pendingDeposit, 
      [${dbxschemaname}].accounts.PendingWithdrawal AS pendingWithdrawal, 
      [${dbxschemaname}].accounts.JointHolders AS jointHolders, 
      [${dbxschemaname}].accounts.IsPFM AS isPFM, 
      [${dbxschemaname}].accounts.InterestPaidYTD AS interestPaidYTD, 
      [${dbxschemaname}].accounts.InterestPaidPreviousYTD AS interestPaidPreviousYTD, 
      [${dbxschemaname}].accounts.InterestPaidLastYear AS interestPaidLastYear, 
      [${dbxschemaname}].accounts.InterestEarned AS interestEarned, 
      [${dbxschemaname}].accounts.CurrentAmountDue AS currentAmountDue, 
      [${dbxschemaname}].accounts.CreditLimit AS creditLimit, 
      [${dbxschemaname}].accounts.CreditCardNumber AS creditCardNumber, 
      [${dbxschemaname}].accounts.BsbNum AS bsbNum, 
      [${dbxschemaname}].accounts.BondInterestLastYear AS bondInterestLastYear, 
      [${dbxschemaname}].accounts.BondInterest AS bondInterest, 
      [${dbxschemaname}].accounts.AvailablePoints AS availablePoints, 
      [${dbxschemaname}].accounts.AccountName AS accountName, 
      [${dbxschemaname}].customeraccounts.email AS email, 
      [${dbxschemaname}].accounts.IBAN AS IBAN, 
      [${dbxschemaname}].accounts.adminProductId AS adminProductId, 
      [${dbxschemaname}].accounts.UpdatedBy AS UpdatedBy, 
      [${dbxschemaname}].accounts.LastUpdated AS LastUpdated, 
      [${dbxschemaname}].accounts.ActualUpdatedBY AS ActualUpdatedBY, 
      [${dbxschemaname}].bank.Description AS bankname, 
      [${dbxschemaname}].accounts.AccountPreference AS accountPreference, 
      [${dbxschemaname}].accounttype.transactionLimit AS transactionLimit, 
      [${dbxschemaname}].accounttype.transferLimit AS transferLimit, 
      [${dbxschemaname}].accounttype.rates AS rates, 
      [${dbxschemaname}].accounttype.termsAndConditions AS termsAndConditions, 
      [${dbxschemaname}].accounttype.TypeDescription AS typeDescription, 
      [${dbxschemaname}].accounttype.supportChecks AS supportChecks, 
      [${dbxschemaname}].accounttype.displayName AS displayName, 
      [${dbxschemaname}].accounts.accountSubType AS accountSubType, 
      [${dbxschemaname}].accounts.description AS description, 
      [${dbxschemaname}].accounts.schemeName AS schemeName, 
      [${dbxschemaname}].accounts.identification AS identification, 
      [${dbxschemaname}].accounts.secondaryIdentification AS secondaryIdentification, 
      [${dbxschemaname}].accounts.servicerSchemeName AS servicerSchemeName, 
      [${dbxschemaname}].accounts.servicerIdentification AS servicerIdentification, 
      [${dbxschemaname}].accounts.dataCreditDebitIndicator AS dataCreditDebitIndicator, 
      [${dbxschemaname}].accounts.dataType AS dataType, 
      [${dbxschemaname}].accounts.dataDateTime AS dataDateTime, 
      [${dbxschemaname}].accounts.dataCreditLineIncluded AS dataCreditLineIncluded, 
      [${dbxschemaname}].accounts.dataCreditLineType AS dataCreditLineType, 
      [${dbxschemaname}].accounts.dataCreditLineAmount AS dataCreditLineAmount, 
      [${dbxschemaname}].accounts.dataCreditLineCurrency AS dataCreditLineCurrency,
	  [${dbxschemaname}].accounts.companyLegalUnit AS legalEntityId
   FROM (((([${dbxschemaname}].accounts 
      INNER JOIN [${dbxschemaname}].customeraccounts 
      ON (([${dbxschemaname}].accounts.Account_id = [${dbxschemaname}].customeraccounts.Account_id ))) 
      INNER JOIN [${dbxschemaname}].accounttype 
      ON (([${dbxschemaname}].accounts.Type_id = [${dbxschemaname}].accounttype.TypeID ))) 
      LEFT JOIN [${dbxschemaname}].contractcorecustomers 
      ON (([${dbxschemaname}].customeraccounts.coreCustomerId = [${dbxschemaname}].contractcorecustomers.coreCustomerId))) 
      LEFT JOIN [${dbxschemaname}].bank 
      ON (([${dbxschemaname}].accounts.Bank_id = [${dbxschemaname}].bank.id )))

GO


ALTER TABLE [${dbxschemaname}].[card] ADD [legalEntityId] NVARCHAR(50) DEFAULT 'ALL';
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_associated_contractaccounts_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[get_associated_contractaccounts_proc](
	@_accountIdList NVARCHAR(50)
) AS BEGIN

    DECLARE @accountIdList NVARCHAR(MAX);
    DECLARE @excludedaccountIdList NVARCHAR(MAX);
    
	SET @accountIdList = (SELECT STRING_AGG([contractaccounts].[accountId], ',') from [${dbxschemaname}].[contractaccounts] 
							WHERE [${dbxschemaname}].[contractaccounts].accountId in (select distinct value from STRING_SPLIT(@_accountIdList,',')));
							
	SET @excludedaccountIdList = (SELECT STRING_AGG([excludedcontractaccounts].[accountId], ',') from [${dbxschemaname}].[excludedcontractaccounts] 
							WHERE [${dbxschemaname}].[excludedcontractaccounts].[accountId]  in (select distinct value from STRING_SPLIT(@_accountIdList,',')) );
							
	select @accountIdList As accountIdList;
	select @excludedaccountIdList As excludedaccountIdList;
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_address_communication_proc];
GO

CREATE OR ALTER  PROCEDURE [${dbxschemaname}].[contract_address_communication_proc]( 
   @_contractId nvarchar(max),
   @_legalEntityId nvarchar(max)
) AS 
BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      SELECT c.*, 
                 s.name as servicedefinitionName 
                 from [${dbxschemaname}].contract c, [${dbxschemaname}].servicedefinition s
      WHERE c.servicedefinitionId = s.id AND c.Id = @_contractId AND c.companyLegalUnit = @_legalEntityId
      
      SELECT * from  
      [${dbxschemaname}].contractcommunication
      WHERE contractcommunication.contractId = @_contractId AND contractcommunication.companyLegalUnit = @_legalEntityId
      
      SELECT * from  
      [${dbxschemaname}].address
      WHERE address.id IN (SELECT DISTINCT contractaddress.addressId FROM [${dbxschemaname}].contractaddress WHERE contractaddress.contractId = @_contractId  AND contractaddress.companyLegalUnit = @_legalEntityId);
                 
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
        @legalEntityId nvarchar(255) = N''  
		
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
		SET @_queryInput = replace(@_queryInput, N'\\', N'')
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
                 
                  SET @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 6), N',', -1)
				  
				  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 7), N',', -1)
                
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
            		SET @limitValue = cast(@limitValuenum AS DECIMAL(20,2))
                        SET @limitAtFInum =
                           (                            
                              SELECT actionlimit.value
                              FROM [${dbxschemaname}].actionlimit
                              WHERE 
								actionlimit.Action_id = @actionId AND 
								actionlimit.LimitType_id = @limitId AND 
								actionlimit.companyLegalUnit = @legalEntityId
                           )
                       
                        SET @limitAtFI = cast(@limitAtFInum AS DECIMAL(20,2))  
                        SET @limitATServiceDefinitionnum =
                           (
                              SELECT servicedefinitionactionlimit.value
                              FROM [${dbxschemaname}].servicedefinitionactionlimit
                              WHERE
                                servicedefinitionactionlimit.actionId = @actionId AND
                                servicedefinitionactionlimit.limitTypeId = @limitId AND
                                servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
								servicedefinitionactionlimit.companyLegalUnit = @legalEntityId
                            
                           )
                           SET @limitATServiceDefinition = cast(@limitATServiceDefinitionnum AS DECIMAL(20,2))
                           IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                             
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                              
                          SET @tempLimitValuenum = CAST(@tempLimitValue AS nvarchar);   
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
                               [${dbxschemaname}].contractfeatures.featureId = @featureId AND
							   [${dbxschemaname}].contractfeatures.companyLegalUnit = @legalEntityId
                     )
                 
                  SET @serviceDefinitionActions =
                     (
                                                      
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
							   [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @legalEntityId
                     )
​
                  SET @existingActionLimitRecords =
                     (
                                  
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
                     )
                 
​
                  SET @existingActionRecords =
                     (
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
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
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction,contractactionlimit.companyLegalUnit) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''',''') + (@isNewAction) + (N''',''') + (@legalEntityId) + (N''');')
                                   
                                    END
                                ELSE
                                    IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction,contractactionlimit.limitTypeId,contractactionlimit.value,contractactionlimit.companyLegalUnit) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ + (@isNewAction) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValuenum) + (N''',''') + (@legalEntityId) + (N''');')
                                     
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
  DECLARE @isNewAction NVARCHAR(255) ;
  DECLARE @actionId NVARCHAR(max) ;
  DECLARE @limitTypeId NVARCHAR(max) ;
  DECLARE @limitValue NVARCHAR(max);
  DECLARE @legalEntityId NVARCHAR(max);
  SET @numOfRecords = LEN(@_contractActionLimit) - LEN(REPLACE(@_contractActionLimit, '|', '')) + 1;
  WHILE 1=1 BEGIN
     SET @index1 = @index1 + 1;
        IF @index1 = @numOfRecords + 1 BEGIN
            break ;
        END
        ELSE BEGIN
            SET @contractValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractActionLimit, '|', @index1), '|', -1 );
            SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',1);
            SET @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',2),',',-1);
            SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',3),',',-1);
            SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',4),',',-1);
			SET @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',5),',',-1);
            SET @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',6),',',-1);
			SET @limitTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',7),',',-1);
            SET @limitValue = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',-1);
			DECLARE @num DECIMAL(20,2)
            SET @num = cast(@limitValue AS DECIMAL(20,2))
            UPDATE contractactionlimit SET value = @num where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId AND companyLegalUnit = @legalEntityId;
        END

    END ;
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_action_limit_save_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_action_limit_save_proc]
@_queryInput VARCHAR(max)
AS
   BEGIN

     SET  XACT_ABORT  ON

     SET  NOCOUNT  ON

      DECLARE @index int
       DECLARE @numOfRecords int
       DECLARE @recordsData nvarchar(max)
       DECLARE @accountId varchar(max)
       DECLARE @query nvarchar(max)

     set @index = 0;
      set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1;
     
     WHILE (1 = 1)
      
         BEGIN

         set @index = @index + 1;
          IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN

           set @recordsData = concat(''',newid(),''',',', [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, '|', @index), '|', -1 ));
            set @accountId = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, '",',4 ), '"', -1 );
             
             select @accountId;
              IF(@accountId='')
               BEGIN
               set @query = concat('INSERT INTO contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (',@recordsData,');');
               END
              ELSE
                 BEGIN
                 set @query = concat('INSERT INTO accountlevelactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (',@recordsData,');');
                 END
            EXEC(@query)

           END

           END;
           
      END;
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS
   BEGIN
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

	  set @index = 0;
	  set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1;  

	    WHILE (1 = 1)
      
         BEGIN

         set @index = @index + 1;

		  IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN

		   set @recordsData = concat('\"',newid(),'\"',',', [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 ));
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',1 ), '\"', -1 );
		   set @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',2 ), ',\"', -1 );   
		   set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 ); 
		   set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',4), ',\"', -1 );           
	       set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );           
	       set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 ); 
	       set @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );          
	       set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );              
	       set @recordsDataWithoutLimits = concat([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '\",',6),'\"'); 


		   SET @serviceDefinitionId = (SELECT servicedefinitionId from [${dbxschemaname}].[contract] WHERE id = @contractId); 

		    IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN

					   SET @limitAtFI = (SELECT actionlimit.value FROM [${dbxschemaname}].[actionlimit] WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId);

					   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId        
					                                    AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId );                   
					  					   

					   IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                     
                  END ;

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
                               [${dbxschemaname}].contractfeatures.featureId = @featureId   
                     )

		   SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId 
                     ) 

             

			SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId 
                     )

					 

					  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId 
                     )
					 
					  SET @existingActionRecords1 = 
                     (
                     			 
                        SELECT String_agg(CAST(accountlevelactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit WHERE 
                               [${dbxschemaname}].accountlevelactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @customerId AND
							   [${dbxschemaname}].accountlevelactionlimit.accountId =@accountId AND
                               [${dbxschemaname}].accountlevelactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].accountlevelactionlimit.actionId = @actionId 
                       
                     ) 

			IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> ''
                  
				  BEGIN
					SET @query = (@query)+
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
                           

							 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
								
                                         SET @id = (SELECT left(newid(), 50))
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''',''') +(@isNewAction) + (N''');')
                                  
                                
							    
								   IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ + (@isNewAction) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValue) + (N''');')
                                     
                                        END 
                                    END
							
				

						 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords1 IS NULL OR @existingActionRecords1 = '')
                                     BEGIN
                                         SET @id = (SELECT left(newid(), 50))
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.featureId,contractactionlimit.actionId, accountlevelactionlimit.isNewAction) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''',''') +(@isNewAction) + (N''');')
                                    
                                    
                           
							    IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.featureId,accountlevelactionlimit.actionId,accountlevelactionlimit.isNewAction,accountlevelactionlimit.limitTypeId,accountlevelactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ + (@isNewAction) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValue) + (N''');')
                                     
                                        END 
							
				                      END 
				              END
									 
						 exec(@query)
						
   END  

   END
   END   

GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_corecustomer_actions_delete_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_corecustomer_actions_delete_proc] 
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50), 
   @_accountId nvarchar(50),
   @_actionsCSV nvarchar(max)
   

AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId

	IF(@_accountId='')
     BEGIN
      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE 
         [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId
  

	IF @_accountId IS NOT NULL
	    BEGIN 

		 DELETE 
         FROM [${dbxschemaname}].accountlevelactionlimit
         WHERE 
         [${dbxschemaname}].accountlevelactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].accountlevelactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @_coreCustomerId AND
		 [${dbxschemaname}].accountlevelactionlimit.accountId = @_accountId

          END 
      END

      
   END
GO







	 

GO
ALTER TABLE [${dbxschemaname}].[accountsstatementfiles] ADD [legalEntityId] NVARCHAR(50) DEFAULT 'ALL';
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_securityattributes_get_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[user_securityattributes_get_proc]
   @_userId nvarchar(50),
   @_legalEntityId nvarchar(50)
AS 
BEGIN
​
SET  XACT_ABORT  ON
SET  NOCOUNT  ON
​
DECLARE @userAssociatedCoreCustomers nvarchar(max) = N''
DECLARE @userAssociatedContracts nvarchar(max) = N''
DECLARE @userAssociatedServiceDefinitions nvarchar(max) = N''
DECLARE @userAssociatedGroups nvarchar(max) = N''
DECLARE @actionsAtCoreCustomers nvarchar(max) = N''
DECLARE @actionsAtServiceDefinitions nvarchar(max) = N''
DECLARE @actionsAtGroups nvarchar(max) = N''
DECLARE @actionsAtuser nvarchar(max) = N''
DECLARE @activeFeaturesAtFI nvarchar(max) = N''
DECLARE @intersectedUserActions nvarchar(max) = N''
DECLARE @intersectedUserFeatures nvarchar(max) = N''
DECLARE @newActionsAtCoreCustomers nvarchar(max) = N''
DECLARE @newActionsAtGroups nvarchar(max) = N''
​
SET @userAssociatedCoreCustomers =  (SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId and contractcustomers.companyLegalUnit = @_legalEntityId);
​
SET @userAssociatedContracts =  (SELECT String_agg(CAST(contractcustomers.contractId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId and contractcustomers.companyLegalUnit = @_legalEntityId);
​
SET @userAssociatedServiceDefinitions =  (SELECT String_agg(CAST(contract.servicedefinitionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contract where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contract.id,@userAssociatedContracts) = '1' AND [${dbxschemaname}].contract.statusId = 'SID_CONTRACT_ACTIVE'  );
​
SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId and customergroup.companyLegalUnit = @_legalEntityId);
​
SET @actionsAtCoreCustomers =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) = '1');
​
SET @newActionsAtCoreCustomers =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) = '1' AND [${dbxschemaname}].contractactionlimit.isNewAction = '1');
​
SET @actionsAtServiceDefinitions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions) = '1' and servicedefinitionactionlimit.companyLegalUnit = @_legalEntityId);
​
SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1' and groupactionlimit.companyLegalUnit = @_legalEntityId);
​
SET @newActionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1' AND [${dbxschemaname}].groupactionlimit.isNewAction = '1'  and groupactionlimit.companyLegalUnit = @_legalEntityId);
​
SET @actionsAtuser =  (SELECT String_agg(CAST(customeraction.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraction where [${dbxschemaname}].customeraction.Customer_id = @_userId AND ([${dbxschemaname}].customeraction.isAllowed = '1' OR [${dbxschemaname}].customeraction.isAllowed = '1') and  customeraction.companyLegalUnit = @_legalEntityId);
​
SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature where [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE' and feature.companyLegalUnit = @_legalEntityId );
​
SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtuser) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtServiceDefinitions) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtCoreCustomers)= '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');
​
SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND featureaction.companyLegalUnit = @_legalEntityId AND ([${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions) = '1' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@newActionsAtCoreCustomers) = '1' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@newActionsAtGroups) = '1') AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtServiceDefinitions)= '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');
​
SELECT @intersectedUserActions AS actions;
​
SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions)= '1');
​
SELECT @intersectedUserFeatures AS features;
​
END;

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[prospect_securityattributes_get_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[prospect_securityattributes_get_proc]
   @_userId nvarchar(50),
   @_legalEntityId nvarchar(50)
AS 
BEGIN

SET  XACT_ABORT  ON
SET  NOCOUNT  ON

DECLARE @userAssociatedGroups nvarchar(max) = N''
DECLARE @actionsAtGroups nvarchar(max) = N''
DECLARE @activeFeaturesAtFI nvarchar(max) = N''
DECLARE @intersectedUserActions nvarchar(max) = N''
DECLARE @intersectedUserFeatures nvarchar(max) = N''

SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId and customergroup.companyLegalUnit = @_legalEntityId);

	
SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1' and  groupactionlimit.companyLegalUnit = @_legalEntityId);

SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature where [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE'  and  feature.companyLegalUnit = @_legalEntityId);

SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1' and  featureaction.companyLegalUnit = @_legalEntityId);

SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions)= '1');

SELECT @intersectedUserFeatures AS features;

END;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[dbpevents_getCustomerData] 
GO

CREATE PROCEDURE [${dbxschemaname}].[dbpevents_getCustomerData] 
@_customerids nvarchar(max) ,@_usernames nvarchar(max)
AS
BEGIN
Select id as CustomerId, UserName, companyLegalUnit from customer where [${dbxschemaname}].FIND_IN_SET(customer.id,@_customerids) <> 0 or [${dbxschemaname}].FIND_IN_SET(customer.UserName,@_usernames)  <> 0
END

GO
DROP procedure IF EXISTS [${dbxschemaname}].[getCustomersIdFromCoreId] 
GO

CREATE PROCEDURE [${dbxschemaname}].[getCustomersIdFromCoreId]  
   @_corecustomers nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT backendidentifier.Customer_id AS custid, backendidentifier.BackendId AS corecustid, backendidentifier.companyLegalUnit
      FROM [${dbxschemaname}].backendidentifier
      WHERE [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId, @_corecustomers) <> 0
 
   END
GO
USE [${dbxdbname}]
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[organisation_actions_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[organisation_actions_create_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  SET  NOCOUNT  ON

	  DECLARE @limitvalue nvarchar(max)
	  DECLARE @id nvarchar(max)
      DECLARE @finished int = 0
	  DECLARE @featureActionId varchar(255) = N''
	  DECLARE @actionslist varchar(max) = N''
	  DECLARE @limitId varchar(255) = N''
	  DECLARE  @entryStatus int = 0
	  DECLARE @features_list nvarchar(max)
	  DECLARE @orgIds nvarchar(max)

      SET @features_list = 
         (  SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureroletype 
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features)) <> 0 OR feature.isPrimary = '1' OR feature.isPrimary = 'true')
 
      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END
  
DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @features_list) > 0
             )

      OPEN actions
	  FETCH NEXT FROM actions INTO @featureActionId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
				  SET @entryStatus = 0
				  DECLARE limits CURSOR LOCAL FOR 
              ( SELECT actionlimit.LimitType_id
                FROM [${dbxschemaname}].actionlimit
                WHERE actionlimit.Action_id = @featureActionId
              )
                  OPEN limits
				  FETCH NEXT FROM limits INTO @limitId
                  WHILE (@@FETCH_STATUS=0)
                     BEGIN
							 SET @limitvalue = (  SELECT actionlimit.value FROM [${dbxschemaname}].actionlimit
                             WHERE actionlimit.Action_id = @featureActionId AND actionlimit.LimitType_id = @limitId)
                             SET @id = (SELECT left(newid(), 50))
                           SET @orgIds =  (SELECT String_agg(CAST(organisationactionlimit.Organisation_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationactionlimit
                             where organisationactionlimit.Organisation_id = @_organisationId AND organisationactionlimit.Action_id = @featureActionId
							 AND organisationactionlimit.LimitType_id = @limitId AND organisationactionlimit.value = @limitvalue)  
						IF @orgIds IS NULL OR @orgIds = ''
						   BEGIN
                              INSERT [${dbxschemaname}].organisationactionlimit(
                                 [${dbxschemaname}].organisationactionlimit.id, 
                                 [${dbxschemaname}].organisationactionlimit.Organisation_id, 
                                 [${dbxschemaname}].organisationactionlimit.Action_id, 
                                 [${dbxschemaname}].organisationactionlimit.LimitType_id, 
                                 [${dbxschemaname}].organisationactionlimit.[value])
                                 VALUES (
                                    @id, 
                                    @_organisationId, 
                                    @featureActionId, 
                                    @limitId, 
                                    @limitvalue)
                              SET @entryStatus = 1
							END
							FETCH NEXT FROM limits INTO @limitId
                            CONTINUE
                      END
                  CLOSE limits
                  DEALLOCATE limits
                  IF @entryStatus = 0
                     BEGIN
                        SET @orgIds =  (SELECT String_agg(CAST(organisationactionlimit.Organisation_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationactionlimit
                        where organisationactionlimit.Organisation_id = @_organisationId AND organisationactionlimit.Action_id = @featureActionId)
						IF @orgIds IS NULL OR @orgIds = ''
						BEGIN
                             SET @id = (SELECT left(newid(), 50))
                             INSERT [${dbxschemaname}].organisationactionlimit([${dbxschemaname}].organisationactionlimit.id, [${dbxschemaname}].organisationactionlimit.Organisation_id, [${dbxschemaname}].organisationactionlimit.Action_id)
                             VALUES (@id, @_organisationId, @featureActionId)
					    END
                     END
                  SET @actionslist = @featureActionId + ',' + @actionslist
				  FETCH NEXT FROM actions INTO @featureActionId
                  CONTINUE
         END
      CLOSE actions
      DEALLOCATE actions
      SET @actionslist = (SELECT substring(@actionslist, 1,(case when len(@actionslist) > 0 then len(@actionslist) - 1 else 0 end)))
      SELECT @actionslist as actionslist
   END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[organisation_features_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[organisation_features_create_proc]  
   @_features nvarchar(max),
   @_organisationType nvarchar(50),
   @_organisationId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  DECLARE @featuresList nvarchar(max)=''
      DECLARE
         @finished int = 0

      DECLARE

         @featureId varchar(255) = N''

      DECLARE

         @features_List varchar(max) = N''
     
     DECLARE @orgIds nvarchar(max)
		 
      SET @features_list = 
         (
 
            SELECT String_agg(CAST(feature.id as NVARCHAR(max)), ',') 
            FROM 
               [${dbxschemaname}].feature 
                  INNER JOIN [${dbxschemaname}].featureroletype 
                  ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_organisationType)
            WHERE ([${dbxschemaname}].FIND_IN_SET(feature.id, @_features) <> 0 OR feature.isPrimary = '1' OR feature.isPrimary = 'true')

         )

      SET @features_list = 
         CASE 
            WHEN (@features_list IS NULL) THEN N''
            ELSE @features_list
         END

      DECLARE
          features CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) <> 0
             )
			 declare @id nvarchar(max)
			 open features
FETCH NEXT FROM features INTO @featureId
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
                  SET @orgIds =  (SELECT String_agg(CAST(organisationfeatures.organisationId AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationfeatures
                        where organisationfeatures.organisationId = @_organisationId AND organisationfeatures.featureId = @featureId)
                    IF @orgIds IS NULL OR @orgIds = ''
						BEGIN
                          INSERT [${dbxschemaname}].organisationfeatures([${dbxschemaname}].organisationfeatures.id, [${dbxschemaname}].organisationfeatures.organisationId, [${dbxschemaname}].organisationfeatures.featureId)
                          VALUES (@id, @_organisationId, @featureId)
 
                          SET @featuresList = @featureId + N',' + @featuresList
                       END
					FETCH NEXT FROM features INTO @featureId
                    CONTINUE
               END

         END

      CLOSE features

      DEALLOCATE features

      SET @featuresList = 
         (
            SELECT substring(@featuresList, 1, (case when len(@featuresList) > 0 then len(@featuresList) - 1 else 0 end))
         )

      SELECT @featuresList as featuresList

   END

GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

ALTER TABLE [${dbxschemaname}].[customer] ADD [isEnrolledFromSpotlight] TINYINT DEFAULT 0 NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_search_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
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
   @_pageSize bigint
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
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name,customer.Location_id AS branch_id,location.Name AS branch_name '


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

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
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

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups,customer.ApplicantChannel, customer.createdts ')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer ' + (
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
            
                  SET @queryStatement = (@queryStatement) + (N' WHERE 1=1 ')
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
                   

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
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory  ')
                       

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
     END

	GO


ALTER TABLE [${dbxschemaname}].[card] 
ADD
[withdrawalMinLimit] VARCHAR(50) NULL DEFAULT '0.00',
[withdrawalMaxLimit] VARCHAR(50) NULL DEFAULT '0.00',
[withdrawalStepLimit] VARCHAR(50) NULL DEFAULT '0.00',
[purchaseLimit] VARCHAR(50) NULL DEFAULT '0.00',
[purchaseMinLimit] VARCHAR(50) NULL DEFAULT '0.00',
[purchaseMaxLimit] VARCHAR(50) NULL DEFAULT '0.00',
[purchaseStepLimit] VARCHAR(50) NULL DEFAULT '0.00';
GO



DROP procedure IF EXISTS [${dbxschemaname}].[organisation_details_get_proc];

/****** Object:  StoredProcedure [${dbxschemaname}].[organisation_details_get_proc]    Script Date: 10/5/2020 4:13:36 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[organisation_details_get_proc]
 @_orgId nvarchar(max),
 @_orgName nvarchar(max),
 @_orgEmail nvarchar(max),
 @_orgTaxId nvarchar(max)
 AS 
    BEGIN
    SET  XACT_ABORT  ON
	SET  NOCOUNT  ON
    DECLARE @orgIdCSV nvarchar(max)
    
IF @_orgName <> '' begin
	SET @_orgName = '%'+ @_orgName +'%'
   SET @orgIdCSV = (SELECT String_agg(CAST(QUOTENAME(organisation.id) AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisation WHERE Name LIKE @_orgName )
END 

IF @_orgEmail <>'' begin
	SET @_orgEmail = '%'+ @_orgEmail +'%'
  IF @orgIdCSV is null or @orgIdCSV = '' BEGIN
      SET @orgIdCSV = (SELECT String_agg(CAST(QUOTENAME(organisationcommunication.Organization_id) AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationcommunication WHERE [Value] LIKE @_orgEmail AND Type_id = 'COMM_TYPE_EMAIL')
  END
  ELSE BEGIN
     SET @orgIdCSV =  @orgIdCSV + ',' +(SELECT String_agg(CAST(QUOTENAME(organisationcommunication.Organization_id) AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationcommunication WHERE [Value] LIKE @_orgEmail  AND Type_id = 'COMM_TYPE_EMAIL' 
                    AND [${dbxschemaname}].FIND_IN_SET(QUOTENAME(organisationcommunication.Organization_id), @orgIdCSV) > 0);            
  END 
END 

IF @_orgTaxId  <> '' begin
SET @_orgTaxId = '%'+ @_orgTaxId +'%'
  IF @orgIdCSV is null or @orgIdCSV = '' BEGIN
     SET @orgIdCSV = (SELECT String_agg(CAST(QUOTENAME(organisationmembership.Organization_id) AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationmembership WHERE Taxid LIKE @_orgTaxId )
  END
  ELSE BEGIN
     SET @orgIdCSV = @orgIdCSV + ',' +(SELECT String_agg(CAST(QUOTENAME(organisationmembership.Organization_id) AS nvarchar(max)), ',') FROM [${dbxschemaname}].organisationmembership WHERE Taxid LIKE @_orgTaxId  
                    AND [${dbxschemaname}].FIND_IN_SET(QUOTENAME(organisationmembership.Organization_id), @orgIdCSV) > 0)
  END 
END 

IF @_orgId <> '' begin
  SET @orgIdCSV = QUOTENAME(@_orgId);
END 

SELECT  organisation.id AS id,
        organisation.Name AS Name,
        organisation.Type_Id AS TypeId,
        organisation.StatusId AS orgStatus,
        organisation.FaxId AS faxId,
        phone.value AS Phone,
        email.value AS Email,
        address.cityName AS cityName,
        address.addressLine1 AS addressLine1,
        address.addressLine2 AS addressLine2,
        address.zipCode AS zipCode,
        address.id AS addressId,
        address.state AS state,
        address.country AS country,
        organisationaddress.IsPrimary AS IsPrimary,
        businesstype.name AS businessType,
        businesstype.id AS businessTypeId
    FROM
        [${dbxschemaname}].organisation AS organisation 
        LEFT JOIN [${dbxschemaname}].organisationcommunication AS phone ON phone.Type_id= 'COMM_TYPE_PHONE' AND organisation.id = phone.Organization_id
		LEFT JOIN [${dbxschemaname}].organisationcommunication AS email ON [${dbxschemaname}].FIND_IN_SET(QUOTENAME(organisation.id), @orgIdCSV) > 0 AND email.Type_id= 'COMM_TYPE_EMAIL' AND organisation.id = email.Organization_id
        LEFT JOIN [${dbxschemaname}].organisationaddress AS organisationaddress ON [${dbxschemaname}].FIND_IN_SET(QUOTENAME(organisationaddress.Organization_id), @orgIdCSV) > 0 AND organisation.id = organisationaddress.Organization_id
        LEFT JOIN [${dbxschemaname}].address AS address  ON organisationaddress.Address_id = address.id
        LEFT JOIN [${dbxschemaname}].businesstype AS businesstype ON businesstype.id = organisation.BusinessType_id  where [${dbxschemaname}].FIND_IN_SET(QUOTENAME(organisation.id), @orgIdCSV) > 0;
END
GO


CREATE TABLE [${dbxschemaname}].transactiontypemapping (
  [backendTransactionTypeId] VARCHAR(10) NOT NULL,
  [backendTransactionType] VARCHAR(45) NULL,
  [dbxTransactionType] VARCHAR(45) NOT NULL,
  PRIMARY KEY ([backendTransactionTypeId]));
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_action_save_proc];
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customer_action_save_proc]  
   @_queryInput nvarchar(max)
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      SET @index = 0
	  set @_queryInput = REPLACE(@_queryInput,'\','')
	  set @_queryInput = REPLACE(@_queryInput,'"','''')

      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+[${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
		  SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,action_id,account_id,isAllowed,limitType_id,value) VALUES (') + (@recordsData) + (N')')
		  EXEC(@query)
               END
         END
   END
GO




/****** Object:  Table [${dbxschemaname}].[alertfrequency]    Script Date: 9/30/2020 8:05:43 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertfrequency](
	[id] [varchar](10) NOT NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequency] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequency] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequency] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

/****** Object:  Table [${dbxschemaname}].[alertfrequencytext]    Script Date: 9/30/2020 9:47:30 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertfrequencytext](
	[alertFrequencyId] [varchar](10) NOT NULL,
	[languageCode] [nvarchar](10) NOT NULL,
	[displayName] [varchar](255) NULL,
	[description] [varchar](1000) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[alertFrequencyId] ASC,
	[languageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] ADD  DEFAULT (NULL) FOR [displayName]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] ADD  DEFAULT (NULL) FOR [description]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext]  WITH CHECK ADD  CONSTRAINT [FK_alertfrequencytext_alertfrequency] FOREIGN KEY([alertFrequencyId])
REFERENCES [${dbxschemaname}].[alertfrequency] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] CHECK CONSTRAINT [FK_alertfrequencytext_alertfrequency]
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext]  WITH CHECK ADD  CONSTRAINT [FK_alertfrequencytext_locale] FOREIGN KEY([languageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO

ALTER TABLE [${dbxschemaname}].[alertfrequencytext] CHECK CONSTRAINT [FK_alertfrequencytext_locale]
GO

/****** Object:  Table [${dbxschemaname}].[alerttypechannel]    Script Date: 10/1/2020 1:36:22 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alerttypechannel](
	[channelId] [nvarchar](50) NOT NULL,
	[alertTypeId] [nvarchar](50) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[channelId] ASC,
	[alertTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alerttypechannel_channel] FOREIGN KEY([channelId])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel] CHECK CONSTRAINT [FK_alerttypechannel_channel]
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alerttypechannel_dbxalerttype] FOREIGN KEY([alertTypeId])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alerttypechannel] CHECK CONSTRAINT [FK_alerttypechannel_dbxalerttype]
GO

/****** Object:  Table [${dbxschemaname}].[alertsubtypetext]    Script Date: 10/1/2020 1:39:18 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertsubtypetext](
	[alertSubTypeId] [nvarchar](75) NOT NULL,
	[languageCode] [nvarchar](10) NOT NULL,
	[displayName] [varchar](255) NULL,
	[description] [varchar](1000) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[alertSubTypeId] ASC,
	[languageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (NULL) FOR [displayName]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (NULL) FOR [description]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypetext_alertsubtype] FOREIGN KEY([alertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] CHECK CONSTRAINT [FK_alertsubtypetext_alertsubtype]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypetext_locale] FOREIGN KEY([languageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypetext] CHECK CONSTRAINT [FK_alertsubtypetext_locale]
GO

/****** Object:  Table [${dbxschemaname}].[alertsubtypechannel]    Script Date: 10/1/2020 1:41:36 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertsubtypechannel](
	[channelId] [nvarchar](50) NOT NULL,
	[alertSubTypeId] [nvarchar](75) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[channelId] ASC,
	[alertSubTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypechannel_alertsubtype] FOREIGN KEY([alertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] CHECK CONSTRAINT [FK_alertsubtypechannel_alertsubtype]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypechannel_channel] FOREIGN KEY([channelId])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] CHECK CONSTRAINT [FK_alertsubtypechannel_channel]
GO

/****** Object:  Table [${dbxschemaname}].[alertsubtypeapp]    Script Date: 10/1/2020 1:45:51 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertsubtypeapp](
	[appId] [nvarchar](50) NOT NULL,
	[alertSubTypeId] [nvarchar](75) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[appId] ASC,
	[alertSubTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypeapp_alertsubtype] FOREIGN KEY([alertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] CHECK CONSTRAINT [FK_alertsubtypeapp_alertsubtype]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypeapp_app] FOREIGN KEY([appId])
REFERENCES [${dbxschemaname}].[app] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] CHECK CONSTRAINT [FK_alertsubtypeapp_app]
GO

/****** Object:  Table [${dbxschemaname}].[alertsubtypecustomertype]    Script Date: 10/1/2020 1:48:32 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertsubtypecustomertype](
	[customerTypeId] [nvarchar](50) NOT NULL,
	[alertSubTypeId] [nvarchar](75) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[customerTypeId] ASC,
	[alertSubTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypecustomertype_alertsubtype] FOREIGN KEY([alertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] CHECK CONSTRAINT [FK_alertsubtypecustomertype_alertsubtype]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypecustomertype_customertype] FOREIGN KEY([customerTypeId])
REFERENCES [${dbxschemaname}].[customertype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] CHECK CONSTRAINT [FK_alertsubtypecustomertype_customertype]
GO

/****** Object:  Table [${dbxschemaname}].[alertsubtypeaccounttype]    Script Date: 10/1/2020 1:50:53 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertsubtypeaccounttype](
	[accountTypeId] [varchar](50) NOT NULL,
	[alertSubTypeId] [nvarchar](75) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[accountTypeId] ASC,
	[alertSubTypeId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypeaccounttype_alertsubtype] FOREIGN KEY([alertSubTypeId])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id])
GO

ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] CHECK CONSTRAINT [FK_alertsubtypeaccounttype_alertsubtype]
GO

/****** Object:  Table [${dbxschemaname}].[customerviewalertconfiguration]    Script Date: 10/1/2020 1:59:57 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[customerviewalertconfiguration](
	[id] [int] NOT NULL,
	[alertPreferenceView] [varchar](10) NOT NULL,
	[enableFrequency] [tinyint] NULL,
	[enableSeparateContact] [tinyint] NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [tinyint] NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT ('CATEGORY') FOR [alertPreferenceView]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration]  WITH CHECK ADD CHECK  (([alertPreferenceView]='ALERT' OR [alertPreferenceView]='GROUP' OR [alertPreferenceView]='CATEGORY'))
GO


/****** Object:  Table [${dbxschemaname}].[customeralertchannel]    Script Date: 10/1/2020 2:05:28 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[customeralertchannel](
	[customerId] [nvarchar](50) NOT NULL,
	[alertCategoryId] [nvarchar](50) NOT NULL,
	[alertTypeId] [varchar](50) NOT NULL,
	[alertSubTypeId] [varchar](75) NOT NULL,
	[channelId] [nvarchar](50) NOT NULL,
	[accountId] [varchar](50) NOT NULL,
	[accountType] [varchar](50) NOT NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[customerId] ASC,
	[alertCategoryId] ASC,
	[alertTypeId] ASC,
	[alertSubTypeId] ASC,
	[channelId] ASC,
	[accountId] ASC,
	[accountType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel]  WITH CHECK ADD  CONSTRAINT [FK_customeralertchannel_channel] FOREIGN KEY([channelId])
REFERENCES [${dbxschemaname}].[channel] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] CHECK CONSTRAINT [FK_customeralertchannel_channel]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel]  WITH CHECK ADD  CONSTRAINT [FK_customeralertchannel_customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] CHECK CONSTRAINT [FK_customeralertchannel_customer]
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel]  WITH CHECK ADD  CONSTRAINT [FK_customeralertchannel_dbxalertcategory] FOREIGN KEY([alertCategoryId])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertchannel] CHECK CONSTRAINT [FK_customeralertchannel_dbxalertcategory]
GO

/*Warning! The maximum key length for a clustered index is 900 bytes. The index 'PK__customer__ACAA01E56A0A9FD4' has maximum length of 1265 bytes. For some combination of large values, the insert/update operation will fail.

Completion time: 2020-10-01T14:03:48.8543817+05:30
*/
/****** Object:  Table [${dbxschemaname}].[customeralertfrequency]    Script Date: 10/1/2020 2:08:25 PM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[customeralertfrequency](
	[customerId] [nvarchar](50) NOT NULL,
	[alertCategoryId] [nvarchar](50) NOT NULL,
	[alertTypeId] [varchar](50) NOT NULL,
	[alertSubTypeId] [varchar](75) NOT NULL,
	[alertFrequencyId] [varchar](10) NOT NULL,
	[accountId] [varchar](50) NOT NULL,
	[accountType] [varchar](50) NOT NULL,
	[frequencyValue] [varchar](50) NULL,
	[frequencyTime] [time](0) NULL,
	[createdby] [varchar](50) NULL,
	[modifiedby] [varchar](50) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
	[softdeleteflag] [smallint] NULL,
PRIMARY KEY CLUSTERED 
(
	[customerId] ASC,
	[alertCategoryId] ASC,
	[alertTypeId] ASC,
	[alertSubTypeId] ASC,
	[accountId] ASC,
	[accountType] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (NULL) FOR [frequencyValue]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (NULL) FOR [frequencyTime]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (NULL) FOR [createdby]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (NULL) FOR [modifiedby]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD  DEFAULT ('0') FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency]  WITH CHECK ADD  CONSTRAINT [FK_customeralertfrequency_alertfrequency] FOREIGN KEY([alertFrequencyId])
REFERENCES [${dbxschemaname}].[alertfrequency] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] CHECK CONSTRAINT [FK_customeralertfrequency_alertfrequency]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency]  WITH CHECK ADD  CONSTRAINT [FK_customeralertfrequency_customer] FOREIGN KEY([customerId])
REFERENCES [${dbxschemaname}].[customer] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] CHECK CONSTRAINT [FK_customeralertfrequency_customer]
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency]  WITH CHECK ADD  CONSTRAINT [FK_customeralertfrequency_dbxalertcategory] FOREIGN KEY([alertCategoryId])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id])
GO

ALTER TABLE [${dbxschemaname}].[customeralertfrequency] CHECK CONSTRAINT [FK_customeralertfrequency_dbxalertcategory]
GO

	
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD [defaultFrequencyId] VARCHAR(10) NULL;
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD [defaultFrequencyValue] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD [defaultFrequencyTime] TIME NULL;
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD CONSTRAINT [FK_dbxalertcategory_alertfrequency]
  FOREIGN KEY ([defaultFrequencyId])
  REFERENCES [${dbxschemaname}].[alertfrequency]([id])
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
  

ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD [isAccountLevel] TINYINT NULL DEFAULT '0' ;
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD [defaultFrequencyId] VARCHAR(10) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD [defaultFrequencyValue] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  [defaultFrequencyTime] TIME NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[dbxalerttype]
ADD CONSTRAINT [FK_dbxalerttype_alertfrequency]
  FOREIGN KEY ([defaultFrequencyId])
  REFERENCES [${dbxschemaname}].[alertfrequency]([id])
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
    
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [isAccountLevel] TINYINT NULL DEFAULT '0';
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [attributeId] VARCHAR(50) NULL DEFAULT NULL; 
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [alertConditionId] VARCHAR(25) NULL DEFAULT NULL ;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [value1] VARCHAR(255)  NULL DEFAULT NULL; 
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [value2] VARCHAR(255)  NULL DEFAULT NULL ;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [isGlobal] TINYINT NULL DEFAULT '0' ;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [defaultFrequencyId] VARCHAR(10) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [defaultFrequencyValue] VARCHAR(50) NULL DEFAULT NULL ;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [defaultFrequencyTime] TIME NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ALTER COLUMN [Status_id]  NVARCHAR(50) NULL ;


/*There are no primary or candidate keys in the referenced table '${dbxschemaname}.alertattribute' that match the referencing column list in the foreign key 'FK_alertsubtype_alertattribute'.
Msg 1750, Level 16, State 1, Line 41
Could not create constraint or index. See previous errors.*/

/*
commenting this due to failure

ALTER TABLE [${dbxschemaname}].[alertsubtype] 
ADD CONSTRAINT [FK_alertsubtype_alertattribute]
  FOREIGN KEY ([attributeId])
  REFERENCES [${dbxschemaname}].[alertattribute] ([id])
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
ALTER TABLE [${dbxschemaname}].[alertsubtype] 
ADD CONSTRAINT [FK_alertsubtype_alertcondition]
  FOREIGN KEY ([alertConditionId])
  REFERENCES [${dbxschemaname}].[alertcondition] ([id])
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;
*/
ALTER TABLE [${dbxschemaname}].[alertsubtype] 
ADD CONSTRAINT [FK_alertsubtype_alertfrequency]
  FOREIGN KEY ([defaultFrequencyId])
  REFERENCES [${dbxschemaname}].[alertfrequency] ([id])
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] 
ADD [alertCategoryId]  VARCHAR(50) NOT NULL DEFAULT '*';
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] 
ADD [alertSubTypeId] VARCHAR(75) NOT NULL DEFAULT '*';
/*Msg 3728, Level 16, State 1, Line 90 'PK_dbxcustomeralertentitlement_Customer_id' is not a constraint.*/
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] DROP CONSTRAINT [PK_dbxcustomeralertentitlement_Customer_id];
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD PRIMARY KEY ([Customer_id], [alertCategoryId], [AlertTypeId], [alertSubTypeId], [AccountId], [AccountType]);

/****** Object:  Table [${dbxschemaname}].[alertfrequencyjobexectime]    Script Date: 10/5/2020 1:12:28 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertfrequencyjobexectime](
	[id] [int] NOT NULL,
	[lastExecTime] [datetime2](0) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

  
/****** Object:  Table [${dbxschemaname}].[weekday]    Script Date: 10/5/2020 1:14:34 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[weekday](
	[id] [int] NOT NULL,
	[Name] [varchar](20) NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

  
/****** Object:  Table [${dbxschemaname}].[weekdayvalue]    Script Date: 10/5/2020 1:15:05 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[weekdayvalue](
	[weekdayId] [int] NOT NULL,
	[languageCode] [nvarchar](10) NOT NULL,
	[displayName] [varchar](255) NULL,
	[createdts] [datetime2](0) NULL,
	[lastmodifiedts] [datetime2](0) NULL,
PRIMARY KEY CLUSTERED 
(
	[weekdayId] ASC,
	[languageCode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue] ADD  DEFAULT (getdate()) FOR [createdts]
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue]  WITH CHECK ADD  CONSTRAINT [FK_weekdayvalue_locale] FOREIGN KEY([languageCode])
REFERENCES [${dbxschemaname}].[locale] ([Code])
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue] CHECK CONSTRAINT [FK_weekdayvalue_locale]
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue]  WITH CHECK ADD  CONSTRAINT [FK_weekdayvalue_weekday] FOREIGN KEY([weekdayId])
REFERENCES [${dbxschemaname}].[weekday] ([id])
GO

ALTER TABLE [${dbxschemaname}].[weekdayvalue] CHECK CONSTRAINT [FK_weekdayvalue_weekday]
GO


/****** Object:  Table [${dbxschemaname}].[alertfrequencytime]    Script Date: 10/5/2020 1:18:03 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[alertfrequencytime](
	[id] [time](0) NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO

ALTER TABLE [${dbxschemaname}].[customercommunication] ADD [isAlertsRequired] TINYINT NULL DEFAULT '0';  

GO    


GO
ALTER TABLE [${dbxschemaname}].[channel] ADD [sequence] INT NULL DEFAULT (NULL)
GO
ALTER TABLE [${dbxschemaname}].[alertfrequency] ADD [sequence] INT NULL DEFAULT (NULL)
GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[channel_view];
GO
create view [${dbxschemaname}].[channel_view] as select
    [channel].[id] as channel_id,
    [channel].[status_id] as channel_status_id,
    [channel].[sequence] as channel_sequence,
    [channeltext].[LanguageCode] as channeltext_LanguageCode,
    [channeltext].[Description] as channeltext_Description,
    [channeltext].[createdby] as channeltext_createdby,
    [channeltext].[modifiedby] as channeltext_modifiedby,
    [channeltext].[createdts] as channeltext_createdts,
    [channeltext].[lastmodifiedts] as channeltext_lastmodifiedts,
    [channeltext].[synctimestamp] as channeltext_synctimestamp,
    [channeltext].[softdeleteflag] as channeltext_softdeleteflag
from
    (channeltext
inner join channel on
    (([channeltext].[channelID] = [channel].[id])));
GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertfrequency_view];
GO
create view [${dbxschemaname}].[alertfrequency_view] as select
    [alertfrequency].[id] as alertfrequency_id,
    [alertfrequency].[sequence] as alertfrequency_sequence,
    [alertfrequencytext].[languageCode] as alertfrequencytext_languageCode,
    [alertfrequencytext].[description] as alertfrequencytext_description,
    [alertfrequencytext].[displayName] as alertfrequencytext_displayName
from
    (alertfrequencytext
inner join alertfrequency on
    (([alertfrequencytext].[alertFrequencyId] = [alertfrequency].[id])));
GO


DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomersaccountchannels_view]
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view]
GO
	

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view]
GO
CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view]
	AS
    SELECT 
       dbxcustomeralertentitlement.alertSubTypeId AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId
    FROM
        (dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.alertSubTypeId = customeralertchannel.alertSubTypeId)))) 
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
GO

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alerttypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((dbxalerttype
        JOIN alerttypechannel ON ((dbxalerttype.id = alerttypechannel.alertTypeId)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel] 
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]
  AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertsubtypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((alertsubtype
        JOIN alertsubtypechannel ON ((alertsubtype.id = alertsubtypechannel.alertSubTypeId)))
        JOIN dbxalerttype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertcategorychannel.ChannelID AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel
    FROM
        (((dbxalertcategory
        JOIN alertcategorychannel ON ((alertcategorychannel.AlertCategoryId = dbxalertcategory.id)))
        JOIN dbxalerttype ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
        JOIN alertsubtype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))

GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view]
GO



DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertcategorylevel]
GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertcategorylevel]
 AS
    SELECT 
        alertsubtype.id AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId
    FROM
        (((dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId))))
        JOIN dbxalerttype ON ((dbxcustomeralertentitlement.alertCategoryId = dbxalerttype.AlertCategoryId)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)))
GO
		

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertgrouplevel]

GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertgrouplevel]
 AS
    SELECT 
        alertsubtype.id AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId
    FROM
        ((dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.AlertTypeId = customeralertchannel.alertTypeId)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId))))
        JOIN alertsubtype ON ((dbxcustomeralertentitlement.AlertTypeId = alertsubtype.AlertTypeId)))
GO
		
DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertlevel]
GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertlevel]
 AS
    SELECT 
        dbxcustomeralertentitlement.alertSubTypeId AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId
    FROM
        (dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.alertSubTypeId = customeralertchannel.alertSubTypeId)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId)
            AND (dbxcustomeralertentitlement.AlertTypeId = customeralertchannel.alertTypeId))))
GO
		
		
        
DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getCoreIdFromDbxIds]
GO



CREATE PROCEDURE [${dbxschemaname}].[subscriber_getCoreIdFromDbxIds]  
@_dbxids nvarchar(max)
AS
 BEGIN
   SET  XACT_ABORT  ON

   SET  NOCOUNT  ON

select BackendId,Customer_id  from backendidentifier where [${dbxschemaname}].FIND_IN_SET(backendidentifier.Customer_id,@_dbxids) <> 0 and BackendType is null

END
GO





DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getCoreIdFromDbxIds_coreSpecific]
GO



CREATE PROCEDURE [${dbxschemaname}].[subscriber_getCoreIdFromDbxIds_coreSpecific]
@_dbxids nvarchar(max),
@_coretype nvarchar(max)
AS
BEGIN
select BackendId,Customer_id  from backendidentifier where [${dbxschemaname}].FIND_IN_SET(backendidentifier.Customer_id,@_dbxids) <> 0 and BackendType = @_coretype 
END

GO
 




DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getCustIdFromCore]
GO


CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getCustIdFromCore]
@_backendids nvarchar(max)
AS
BEGIN
select BackendId,Customer_id  from backendidentifier where [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId,@_backendids) <> 0
END
GO
 


DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getAlertSubtypePreferences]
GO


CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getAlertSubtypePreferences]
@alerttypes nvarchar(max)
AS
BEGIN
Select id as alertsubtypeid ,AlertTypeId as alerttypeid ,  attributeId as attributeid, alertConditionId as alertconditionid, value1, value2, isGlobal as isglobal
 from alertsubtype where [${dbxschemaname}].FIND_IN_SET(alertsubtype.AlertTypeId,@alerttypes)   <> 0
END

 GO


DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getEntitleMentsNocustomer]
GO

CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getEntitleMentsNocustomer]
@alerttypes nvarchar(max)
AS
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2 FROM dbxcustomeralertentitlement where   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId,@alerttypes)   <> 0
END

GO


DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getEntitleMentsWithcustomer]
GO

CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getEntitleMentsWithcustomer]
@alerttypes nvarchar(max),
@custids nvarchar(max)
AS
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2  FROM dbxcustomeralertentitlement where   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId,@alerttypes) <> 0  and
   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.Customer_id,@custids) <> 0
END

GO

			




DROP procedure IF EXISTS [${dbxschemaname}].[getCustomersAlertFrequency_Sp_alertlevel]
GO
CREATE  PROCEDURE [${dbxschemaname}].[getCustomersAlertFrequency_Sp_alertlevel]
@startTime nvarchar(max) , @endTime nvarchar(max) , @scheduleDay nvarchar(max) , @scheduleDate int, @isLastDate nvarchar(max)
AS
BEGIN
 
 if (@isLastDate = 'true')
 begin
 select 
 *  
 from customeralertfrequency 
 where 
 customeralertfrequency.frequencyTime>@startTime 
 and customeralertfrequency.frequencyTime<=@endTime 
 and((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'));
 end
 else begin
  select
  *  
  from 
  customeralertfrequency 
  where 
  customeralertfrequency.frequencyTime>@startTime 
  and customeralertfrequency.frequencyTime<=@endTime 
  and((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'));
 end 
END
GO



DROP procedure IF EXISTS [${dbxschemaname}].[getCustomersAlertFrequency_Sp_grouplevel] 
GO

CREATE  PROCEDURE [${dbxschemaname}].[getCustomersAlertFrequency_Sp_grouplevel]
@startTime  nvarchar(max) , @endTime  nvarchar(max) , @scheduleDay  nvarchar(max) , @scheduleDate int, @isLastDate  nvarchar(max)
AS
BEGIN
 if (@isLastDate = 'true')
 begin
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 customeralertfrequency.alertTypeId, 
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId 
 from customeralertfrequency
 join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
 where 
 customeralertfrequency.frequencyTime>@startTime 
 and customeralertfrequency.frequencyTime<=@endTime 
 and((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
 and alertsubtype.defaultFrequencyId is not null 
 end
 else begin
  select 
  customeralertfrequency.customerId,
  customeralertfrequency.alertCategoryId,
  customeralertfrequency.alertTypeId, 
  customeralertfrequency.accountId,
  alertsubtype.id as alertSubTypeId 
  from 
  customeralertfrequency 
  join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
  where 
  customeralertfrequency.frequencyTime>@startTime 
  and customeralertfrequency.frequencyTime<=@endTime 
  and((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
  and alertsubtype.defaultFrequencyId is not null 
 end 
END
GO






DROP procedure IF EXISTS [${dbxschemaname}].[getCustomersAlertFrequency_Sp_categorylevel] 
GO

CREATE  PROCEDURE [${dbxschemaname}].[getCustomersAlertFrequency_Sp_categorylevel] 
@startTime  nvarchar(max) , @endTime  nvarchar(max) , @scheduleDay  nvarchar(max) , @scheduleDate int, @isLastDate  nvarchar(max) 
AS
BEGIN
 if (@isLastDate = 'true')
 begin
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 customeralertfrequency.frequencyTime>@startTime 
 and  customeralertfrequency.frequencyTime<=@endTime 
 and ((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >= @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
 and alertsubtype.defaultFrequencyId is not null 
 end
 else begin
  select 
  customeralertfrequency.customerId,
  customeralertfrequency.alertCategoryId,
  dbxalerttype.id as alertTypeId, 
  customeralertfrequency.accountId,
  alertsubtype.id as alertSubTypeId 
  from 
  customeralertfrequency 
  join dbxalerttype on customeralertfrequency.alertCategoryId = dbxalerttype.AlertCategoryId 
  join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
  where 
  customeralertfrequency.frequencyTime>@startTime 
  and customeralertfrequency.frequencyTime<=@endTime 
  and((customeralertfrequency.frequencyValue=@scheduleDay and customeralertfrequency.alertFrequencyId = 'WEEKLY') or customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDate and customeralertfrequency.alertFrequencyId ='MONTHLY'))
  and alertsubtype.defaultFrequencyId is not null  
 end 
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_alertlevel] 
GO

CREATE  PROCEDURE [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_alertlevel] 
@startTimePrev  nvarchar(max) , @startTimeCurr  nvarchar(max) , 
@endTimePrev  nvarchar(max) , @endTimeCurr  nvarchar(max) ,@scheduleDayPrev  nvarchar(max) ,@scheduleDayCurr  nvarchar(max) , 
@scheduleDatePrev int , @scheduleDateCurr int ,@isLastDate  nvarchar(max) ,@isPrevDateLastDate  nvarchar(max) 
AS
BEGIN
 if(@isLastDate = 'true') begin
 if(@isPrevDateLastDate = 'true' ) begin
select 
*  
from 
customeralertfrequency 
where 
(customeralertfrequency.frequencyTime>@startTimePrev and customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))
 or (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 end
 else begin
 select 
 *  
 from 
 customeralertfrequency 
 where 
 (customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=@scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 end 
 end
 else begin
 select 
 *  
 from 
 customeralertfrequency 
 where 
 (customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))) 
 end 
END

GO




DROP procedure IF EXISTS [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_grouplevel] 
GO

CREATE  PROCEDURE [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_grouplevel] 
@startTimePrev  nvarchar(max) , @startTimeCurr  nvarchar(max) , 
@endTimePrev  nvarchar(max) , @endTimeCurr  nvarchar(max) ,@scheduleDayPrev  nvarchar(max) ,@scheduleDayCurr  nvarchar(max) , 
@scheduleDatePrev int , @scheduleDateCurr int ,@isLastDate  nvarchar(max) ,@isPrevDateLastDate  nvarchar(max) 
AS
BEGIN
 if(@isLastDate = 'true') begin
 if(@isPrevDateLastDate = 'true' ) begin
select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
where 
((customeralertfrequency.frequencyTime>@startTimePrev and customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
and alertsubtype.defaultFrequencyId is not null 
 end
 else begin
 select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=@scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null 
 end 
 end
 else begin
 select 
customeralertfrequency.customerId,
customeralertfrequency.alertCategoryId,
customeralertfrequency.alertTypeId, 
customeralertfrequency.accountId,
alertsubtype.id as alertSubTypeId 
from customeralertfrequency
join alertsubtype on customeralertfrequency.alertTypeId = alertsubtype.AlertTypeId 
where 
 ((customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null 
 end 
END
GO





DROP procedure IF EXISTS [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_categorylevel] 
GO

CREATE  PROCEDURE [${dbxschemaname}].[getAllCustomersAlertFrequency_Sp_categorylevel] 
@startTimePrev  nvarchar(max) , @startTimeCurr  nvarchar(max) , 
@endTimePrev  nvarchar(max) , @endTimeCurr  nvarchar(max) ,@scheduleDayPrev  nvarchar(max) ,@scheduleDayCurr  nvarchar(max) , 
@scheduleDatePrev int , @scheduleDateCurr int ,@isLastDate  nvarchar(max) ,@isPrevDateLastDate  nvarchar(max) 
AS
BEGIN
 if(@isLastDate = 'true') begin
 if(@isPrevDateLastDate = 'true' ) begin
select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
where 
((customeralertfrequency.frequencyTime>@startTimePrev and customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue >=@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
and alertsubtype.defaultFrequencyId is not null 
 end
 else begin
 select 
 customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue =@scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY'))) or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue>=@scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null 
 end 
 end
 else begin
 select 
customeralertfrequency.customerId,
 customeralertfrequency.alertCategoryId,
 dbxalerttype.id as alertTypeId,
 customeralertfrequency.accountId,
 alertsubtype.id as alertSubTypeId
 from customeralertfrequency 
 join dbxalerttype on customeralertfrequency.alertCategoryId= dbxalerttype.AlertCategoryId
 join alertsubtype on dbxalerttype.id = alertsubtype.AlertTypeId 
 where 
 ((customeralertfrequency.frequencyTime>@startTimePrev and  customeralertfrequency.frequencyTime<=@endTimePrev and((customeralertfrequency.frequencyValue=@scheduleDayPrev and customeralertfrequency.alertFrequencyId = 'WEEKLY') or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDatePrev and customeralertfrequency.alertFrequencyId ='MONTHLY')))  or  (customeralertfrequency.frequencyTime>=@startTimeCurr and  customeralertfrequency.frequencyTime<=@endTimeCurr and((customeralertfrequency.frequencyValue=@scheduleDayCurr and customeralertfrequency.alertFrequencyId = 'WEEKLY')  or  customeralertfrequency.alertFrequencyId = 'DAILY' or (customeralertfrequency.frequencyValue = @scheduleDateCurr and customeralertfrequency.alertFrequencyId ='MONTHLY'))))
 and alertsubtype.defaultFrequencyId is not null 
 end 
END
GO


DROP procedure IF EXISTS [${dbxschemaname}].[dbpalerts_getCustomerData] 
GO
DROP procedure IF EXISTS [${dbxschemaname}].[dbpalerts_getCustIdFromCore] 
GO
DROP procedure IF EXISTS [${dbxschemaname}].[dbpalerts_getCustidFromAccount] 
GO


DROP procedure IF EXISTS [${dbxschemaname}].[dbpevents_getCustomerData] 
GO

CREATE PROCEDURE [${dbxschemaname}].[dbpevents_getCustomerData] 
@_customerids nvarchar(max) ,@_usernames nvarchar(max)
AS
BEGIN
Select id as CustomerId, UserName from customer where [${dbxschemaname}].FIND_IN_SET(customer.id,@_customerids) <> 0 or [${dbxschemaname}].FIND_IN_SET(customer.UserName,@_usernames)  <> 0
END
GO
DROP procedure IF EXISTS [${dbxschemaname}].[dbpevents_getCustIdFromCore] 
GO

CREATE PROCEDURE [${dbxschemaname}].[dbpevents_getCustIdFromCore] 
@_backendids nvarchar(max)
AS
BEGIN
select BackendId,Customer_id  from backendidentifier where [${dbxschemaname}].FIND_IN_SET(backendidentifier.BackendId,@_backendids)  <> 0
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[dbpevents_getCustidFromAccount] 
GO
CREATE PROCEDURE [${dbxschemaname}].[dbpevents_getCustidFromAccount] 
@_accounts nvarchar(max)
AS
BEGIN
Select Account_id, User_id, Type_id as accounttype_id from accounts where [${dbxschemaname}].FIND_IN_SET(accounts.Account_id,@_accounts)  <> 0
END
GO


CREATE INDEX [IDX_transaction_schdate] ON [${dbxschemaname}].[transaction] ([scheduledDate]);
GO

CREATE INDEX [IDX_transaction_personid] ON [${dbxschemaname}].[transaction] ([Person_ID]);
GO 

CREATE INDEX [IDX_orgcommunication_value] ON [${dbxschemaname}].[organisationcommunication] ([Value]);
GO 

DROP TABLE IF EXISTS [${dbxschemaname}].[cardproducttype];
GO
CREATE TABLE [${dbxschemaname}].[cardproducttype] (
	[id] INT NOT NULL IDENTITY,
	[productId] INT  NOT NULL,
	[accountType] VARCHAR(50) NOT NULL,
        [createdOn] DATETIME2(0) NULL DEFAULT GETDATE(),
        [updatedOn] DATETIME2(0) NULL DEFAULT GETDATE(),
        [createdBy] VARCHAR(45),
        [modifiedby] varchar(50) DEFAULT NULL,
        [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [softdeleteflag] bit NOT NULL DEFAULT 0,  
         PRIMARY KEY ([id])
);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[cardproducts];
GO
CREATE TABLE [${dbxschemaname}].[cardproducts] (
	[productId] INT NOT NULL IDENTITY,
	[productName] VARCHAR(200) NOT NULL,
	[featureOverview] NVARCHAR(max) NULL DEFAULT NULL,
	[featureDescription] NVARCHAR(max) NULL DEFAULT NULL,
	[representativeLabel1] NVARCHAR(2000) NULL DEFAULT NULL,
	[representativeLabel2] NVARCHAR(2000) NULL DEFAULT NULL,
	[representativeLabel3] NVARCHAR(2000) NULL DEFAULT NULL,
	[representativeValue1] NVARCHAR(2000) NULL DEFAULT NULL,
	[representativeValue2] NVARCHAR(2000) NULL DEFAULT NULL,
	[representativeValue3] NVARCHAR(2000) NULL DEFAULT NULL,
        [createdOn] DATETIME2(0) NULL DEFAULT GETDATE(),
        [updatedOn] DATETIME2(0) NULL DEFAULT GETDATE(),
        [createdBy] VARCHAR(45),
        [modifiedby] varchar(50) DEFAULT NULL,
        [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
        [softdeleteflag] bit NOT NULL DEFAULT 0,  
	 PRIMARY KEY ([productId])
);
GO

ALTER TABLE [${dbxschemaname}].[cardproducttype]
	ADD CONSTRAINT [FK_product_Id] FOREIGN KEY ([productId]) REFERENCES [${dbxschemaname}].[cardproducts] ([productId]) ON UPDATE NO ACTION ON DELETE NO ACTION;
GO	

ALTER TABLE [${dbxschemaname}].[cardproducts]
ADD 
[withdrawlLimit] VARCHAR(50) NULL DEFAULT NULL,
[withdrawalMinLimit] VARCHAR(50) NULL DEFAULT NULL,
[withdrawalMaxLimit] VARCHAR(50) NULL DEFAULT NULL,
[withdrawalStepLimit] VARCHAR(50) NULL DEFAULT NULL,
[purchaseLimit] VARCHAR(50) NULL DEFAULT NULL,
[purchaseMinLimit] VARCHAR(50) NULL DEFAULT NUll,
[purchaseMaxLimit] VARCHAR(50) NULL DEFAULT NULL,
[purchaseStepLimit] VARCHAR(50) NULL DEFAULT NULL;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[cardproductsview];
GO
CREATE VIEW [${dbxschemaname}].[cardproductsview] AS 
SELECT 
	cardproducts.productId AS productId,
	cardproducts.productName AS productName,
	cardproducttype.accountType AS accountType,
	cardproducts.featureOverview AS featureOverview,
	cardproducts.featureDescription AS featureDescription,
	cardproducts.representativeLabel1 AS representativeLabel1,
	cardproducts.representativeLabel2 AS representativeLabel2,
	cardproducts.representativeLabel3 AS representativeLabel3,
	cardproducts.representativeValue1 AS representativeValue1,
	cardproducts.representativeValue2 AS representativeValue2,
	cardproducts.representativeValue3 AS representativeValue3,
	cardproducts.withdrawlLimit AS withdrawlLimit,
	cardproducts.withdrawalMinLimit AS withdrawalMinLimit,
	cardproducts.withdrawalMaxLimit AS withdrawalMaxLimit,
	cardproducts.withdrawalStepLimit AS withdrawalStepLimit,
	cardproducts.purchaseLimit AS purchaseLimit,
	cardproducts.purchaseMinLimit AS purchaseMinLimit,
	cardproducts.purchaseMaxLimit AS purchaseMaxLimit,
	cardproducts.purchaseStepLimit AS purchaseStepLimit
FROM [${dbxschemaname}].cardproducts INNER  JOIN [${dbxschemaname}].cardproducttype ON (cardproducttype.productId = cardproducts.productId)
GO

ALTER TABLE [${dbxschemaname}].[card]
	ADD [cardDisplayName] VARCHAR(50) NULL DEFAULT NULL
GO

CREATE TABLE [${dbxschemaname}].[loanschedule](
  [id] INT NOT NULL ,
  [AccountId] VARCHAR(50) NULL,
  [Amount] VARCHAR(50) NULL,
  [Principal] VARCHAR(50) NULL,
  [Interest] VARCHAR(50) NULL,
  [OutstandingBalance] VARCHAR(50) NULL,
  [Charges] VARCHAR(50) NULL,
  [Tax] VARCHAR(50) NULL,
  [Insurance] VARCHAR(50) NULL,
  [CumulativeInterest] VARCHAR(50) NULL,
  [InstallmentType] VARCHAR(50) NULL,
  [Date] DATE NULL,
  PRIMARY KEY ([id]))
 GO
 
 CREATE TABLE [${dbxschemaname}].[paymentfiles] (
  [paymentFileID] VARCHAR(40) NOT NULL,
  [userId] VARCHAR(50) NOT NULL,
  [transactionId] VARCHAR(45) NOT NULL,
  [paymentFileName] VARCHAR(150) NOT NULL,
  [paymentFileType] VARCHAR(45) NOT NULL,
  [paymentFileContents] NVARCHAR(MAX) NOT NULL,
  [createdts] DATETIME2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] DATETIME2(0) NOT NULL DEFAULT GETDATE(),
  PRIMARY KEY ([paymentFileID]))  
GO

/****** Object: StoredProcedure [${dbxschemaname}].[customer_basic_info_proc] Script Date: 10/28/2020 5:04:56 PM ******/
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_basic_info_proc]

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]
	@_customerId nvarchar(50)
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
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isPrimary = 1 AND customercommunication.isTypeBusiness IS NULL
			AND customercommunication.Customer_id = customer.id) AS PrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = 1 AND customercommunication.isTypeBusiness IS NULL
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
END
GO
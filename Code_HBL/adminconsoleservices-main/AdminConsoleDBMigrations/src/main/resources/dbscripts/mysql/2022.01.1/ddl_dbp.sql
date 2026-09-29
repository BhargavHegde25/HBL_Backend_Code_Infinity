
DROP PROCEDURE IF EXISTS customer_search_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_search_proc`( 
in _searchType varchar(60),
in _id varchar(50), 
in _name varchar(50),  
in _SSN varchar(50),
in _username varchar(50), 
in _phone varchar(100), 
in _email varchar(100),
in _dateOfBirth varchar(100),
in _IsStaffMember varchar(10),
in _cardorAccountnumber varchar(50),
in _TIN varchar(50),
in _group varchar(40), 
in _IDType varchar(50),
in _IDValue varchar(50),
in _companyId varchar(50),
in _requestID varchar(50),
in _branchIDS varchar(2000), 
in _productIDS varchar(2000), 
in _cityIDS varchar(2000), 
in _entitlementIDS varchar(2000), 
in _groupIDS varchar(2000), 
in _customerStatus varchar(50), 
in _before varchar(20), 
in _after varchar(20), 
in _sortVariable varchar(100), 
in _sortDirection varchar(4),  
in _pageOffset bigint, 
in _pageSize bigint)
BEGIN
	
    DECLARE _maxLockCount varchar(50);
	IF _searchType LIKE 'GROUP_SEARCH%' then
		set @search_select_statement = "SELECT 
					customer.id, customer.FirstName, customer.MiddleName, customer.LastName,
					concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
					customer.UserName as Username, customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember,
					IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`,
					customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,
					GROUP_CONCAT(customergroup.Group_id) as assigned_group_ids,
					address.City_id, city.Name As City_name,
					address.addressLine1 As addressLine1,
					address.addressLine2 As addressLine2,
					city.Name As city,
					address.zipCode As zipCode,
					country.Name As county,
					customer.isEnrolled as isEnrolled,
					customer.Location_id AS branch_id,
					location.Name AS branch_name";
		set @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
		set @queryStatement = concat("
				FROM customer
                    JOIN (
						SELECT
							customer.id
						FROM
							customer ",
							IF( _groupIDS != '' OR _entitlementIDS != '', " LEFT JOIN customergroup ON (customergroup.Customer_id=customer.id)", ""),
							IF( _cityIDS != '', " LEFT JOIN customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
							LEFT JOIN address ON (customeraddress.Address_id = address.id)
							LEFT JOIN city ON (address.City_id = city.id)", ""),
							IF( _entitlementIDS != '', " LEFT JOIN customerentitlement ON (customerentitlement.Customer_id=customer.id) ", ""),
							IF( _productIDS != '', " LEFT JOIN customerproduct ON (customerproduct.Customer_id=customer.id) ", ""));
			set @whereclause = "WHERE true";
			
            IF _username != "" then
				
				set @whereclause = concat(@whereclause, " AND (customer.firstname like concat(",quote(_username),",'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.username like concat(",quote(_username)," ,'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.id like concat(", quote(_username) ,",'%')) ");
			end if;
            
            
			if _IsStaffMember != "" then
				if _IsStaffMember = "true" then
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '1'");
				else
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '0'");
				end if;
			end if;
            
			
			if _entitlementIDS != "" then
				set @whereclause = concat(@whereclause," AND (customerentitlement.Service_id in (",func_escape_input_for_in_operator(_entitlementIDS),") 
							OR customergroup.Group_id in ( select Group_id from groupentitlement where Service_id in (",func_escape_input_for_in_operator(_entitlementIDS)," )))");
			end if;
			
			
			if _groupIDS != "" then
				set @whereclause = concat(@whereclause," AND customergroup.Group_id in (",func_escape_input_for_in_operator(_groupIDS),") ");
			end if;
			
			
			if _productIDS != "" then
				set @whereclause = concat(@whereclause," AND customerproduct.Product_id in (",func_escape_input_for_in_operator(_productIDS),")");
			end if;
			
			
			if _branchIDS != "" then
				set @whereclause = concat(@whereclause, " AND customer.Location_id in (",func_escape_input_for_in_operator(_branchIDS),")");
			end if;
			
			
			if _customerStatus != "" then
				set @whereclause = concat(@whereclause, " AND customer.Status_id = ", quote(_customerStatus));
			end if;
			
			
			if _cityIDS != "" then
				set @whereclause = concat(@whereclause, " AND city.Name in (",func_escape_input_for_in_operator(_cityIDS),")");
			end if;
			
			
			if _before != "" and _after != "" then
				set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_before) ," and date(customer.createdts) <= date ", quote(_after));
			else 
				if _before != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) <= date ", quote(_before));
				elseif _after != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_after));
				end if;
			end if;
			
			set @queryStatement = concat(@queryStatement,@whereclause,
            ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
					
					LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
					LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
					LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
					LEFT JOIN address ON (customeraddress.Address_id = address.id)
					LEFT JOIN city ON (city.id = address.City_id)
					LEFT JOIN country ON (city.Country_id = country.id)
					LEFT JOIN location ON (location.id=customer.Location_id)  
			");
			
			IF _searchType = 'GROUP_SEARCH' THEN
                set @queryStatement2 = concat(@search_count_statement, @queryStatement);
				set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
				IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

			ELSEIF _searchType = 'GROUP_SEARCH_TOTAL_COUNT' then
				set @queryStatement = concat(@search_count_statement, @queryStatement);
			END IF;


	ELSEIF _searchType LIKE 'CUSTOMER_SEARCH%' then
			
			SELECT accountLockoutThreshold from passwordlockoutsettings where id='PLOCKID1' INTO _maxLockCount;
            
            SET @search_select_statement = concat("SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,
				concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
				customer.UserName as Username,customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight,customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender,CONCAT('****', RIGHT(customer.Ssn, 4)) as Ssn,
                IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`, 
                company.id as CompanyId, company.Name as CompanyName,
                organisationemployees.isAuthSignatory as isAuthSignatory, 
                IF(IFNULL(customer.lockCount,0) >= ",_maxLockCount,", 'SID_CUS_LOCKED',customer.Status_id) as Status_id,
				PrimaryPhone.value AS PrimaryPhoneNumber,
				PrimaryEmail.value AS PrimaryEmailAddress,
                GROUP_CONCAT(membergroup.Name) as `groups`,
				address.addressLine1 As addressLine1,
				address.addressLine2 As addressLine2,
				city.Name As city,
				address.zipCode As zipCode,
				country.Name As county,
				customer.isEnrolled as isEnrolled,
                customer.ApplicantChannel, customer.createdts");
            SET @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
			SET @queryStatement = concat("
			FROM customer
			JOIN (
				SELECT
					customer.id
				FROM customer ",
					IF( _phone != '', " JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE') ",""),
					IF(_email != '', "  JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL') ",""),
                    IF( _TIN != '', " LEFT JOIN organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)", ""),
                    IF( _cardorAccountnumber != '', " LEFT JOIN card ON (customer.id = card.User_id)
                    LEFT JOIN accounts ON (customer.id = accounts.User_id)
                    LEFT JOIN customeraccounts ON (customer.id = customeraccounts.Customer_id)", ""));

                SET @queryStatement = concat(@queryStatement," WHERE true");
                
                IF _id != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.id = ", quote(_id));
                end if;
                
                 IF _name != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.LastName like concat(", quote(_name),",'%')");
                end if;

                IF _SSN != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Ssn = ",quote(_SSN));
                end if;

                IF _username != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.username = ",quote(_username));
                end if;
                
                 IF _dateOfBirth != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.DateOfBirth = ",quote(_dateOfBirth));
                end if;
                
                IF _phone != '' THEN
					IF length(_phone) > 9 THEN
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value like concat('%',",quote(_phone),",'%')");
                    ELSE
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value = ",quote(_phone));
					end if;
                end if;

                IF _email != '' THEN
					set @queryStatement = concat(@queryStatement," and PrimaryEmail.value = ",quote(_email));
                end if;

                IF _companyId != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Organization_id = ",quote(_companyId));
                end if;

				IF _IDValue != '' THEN
                    IF _IDType = 'ID_DRIVING_LICENSE' THEN
						set @queryStatement = concat(@queryStatement," and (customer.DrivingLicenseNumber = ",quote(_IDValue)," or 
                        (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),"))");
                    ELSE
						set @queryStatement = concat(@queryStatement," and (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),")");
					end if;
                end if;
                
				IF _TIN != '' THEN
					set @queryStatement = concat(@queryStatement," and organisationmembership.Taxid = ",quote(_TIN));
                end if;
                
                IF _cardorAccountnumber != '' THEN
					set @queryStatement = concat(@queryStatement," and (card.cardNumber = ",quote(_cardorAccountnumber),
                    " or accounts.Account_id = ",quote(_cardorAccountnumber)," or customeraccounts.Account_id = ",quote(_cardorAccountnumber),")");
                end if;
                
                set @queryStatement = concat(@queryStatement,
                ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
			LEFT JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE')
			LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
			LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
			LEFT JOIN address ON (customeraddress.Address_id = address.id)
			LEFT JOIN city ON (city.id = address.City_id)
			LEFT JOIN country ON (city.Country_id = country.id)
			LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
			LEFT JOIN membergroup ON (membergroup.id=customergroup.group_id)
            LEFT JOIN organisation company ON (customer.Organization_id = company.id)
            LEFT JOIN organisationemployees ON (organisationemployees.Organization_id = company.id)");

            IF _searchType = 'CUSTOMER_SEARCH' THEN
				set @queryStatement2 = concat(@search_count_statement, @queryStatement);
            	set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
            	IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

            ELSEIF _searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT' THEN
            	set @queryStatement = concat(@search_count_statement, @queryStatement);
            END IF;
	END IF;

	PREPARE stmt FROM @queryStatement; EXECUTE stmt; 
     IF _searchType = 'CUSTOMER_SEARCH' OR _searchType = 'GROUP_SEARCH'THEN
          PREPARE stmt FROM @queryStatement2; EXECUTE stmt;
     END If;
    DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `reports_threads_resolved`;
DELIMITER $$
CREATE PROCEDURE `reports_threads_resolved`(
  in from_date VARCHAR(19), 
  in _todate VARCHAR(19), 
  in category_id VARCHAR(50), 
  in csr_name VARCHAR(50)
)
BEGIN 
set 
  @queryStatement = "
              SELECT
                count(id) threads_resolved_count 
              from
                customerrequest 
              where
                Status_id = 'SID_RESOLVED'";
if from_date != '' 
and from_date != '' then 
set 
  @queryStatement = concat(
    @queryStatement, 
    " 
                and createdts >= ", 
    quote(STR_TO_DATE(from_date, '%m/%d/%Y %H:%i:%s')), 
    " and createdts <= ", 
    quote(STR_TO_DATE(_todate, '%m/%d/%Y %H:%i:%s'))
  );
end if;
if category_id != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
                and RequestCategory_id = ", 
    quote(category_id)
  );
end if;
if csr_name != '' then 
set 
  @queryStatement = concat(
    @queryStatement, " 
                and AssignedTo = ", 
    quote(csr_name)
  );
end if;
PREPARE stmt 
FROM 
  @queryStatement;
EXECUTE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `verify_user_proc`;

DELIMITER $$
CREATE  PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendIdentifiers varchar(250) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @usersList = (select group_concat(backendidentifier.Customer_id SEPARATOR ",") from backendidentifier where BackendType=_backendType  
									 AND FIND_IN_SET(backendidentifier.BackendId ,_backendIdentifiers) );

(SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`,
        `customer`.`CustomerType_id` AS `CustomerType_id`
    FROM
        (`customer`
        LEFT JOIN `customercommunication` `primaryphone` ON ((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`Value` = _phone)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))
        LEFT JOIN `customercommunication` `primaryemail` ON ((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`Value` = _email)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')))
	where
		`customer`.`DateOfBirth` = _dateOfBirth
		and `primaryphone`.`Customer_id` = `primaryemail`.`Customer_id`) union SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`,
		`customer`.`CustomerType_id` AS `CustomerType_id`
    FROM `customer` where FIND_IN_SET(customer.id ,@usersList);
	
END$$

DELIMITER ;




DROP procedure IF EXISTS `contract_actionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_actionlimits_create_proc`(
  IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      
      DECLARE tempLimitValue varchar(255) DEFAULT "";
      
      SET SESSION group_concat_max_len = 100000000;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      set @serviceDefinitionId = "";
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\"',UUID(),'\"',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
            
            set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',2 ), '\"', -1 );
            set @customerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 );
            set @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4), ',\"', -1 );
            set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );
            set @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            set @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );
            set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            
             SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',6 ),'\"');
             
              IF isnull(@serviceDefinitionId) OR @serviceDefinitionId = "" THEN
                SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @contractId);
                     END IF;
             
                     IF @limitId != '@' AND  @limitValue != '@' THEN 
              SET @limitAtFI = (SELECT actionlimit.value FROM actionlimit WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId);
      
              SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId
                                                   AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId );
                       SET tempLimitValue = LEAST(@limitAtFI,@limitATServiceDefinition,@limitValue);
            END IF;
            
            IF !isnull(tempLimitValue) AND tempLimitValue !='' THEN
               SET @limitValue = tempLimitValue;
                     END IF;
            

            SET @contarctFeatures = (select group_concat(contractfeatures.id SEPARATOR ",") from contractfeatures where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId = @featureId);
                     
            
             
             SET @serviceDefinitionActions = (select group_concat(servicedefinitionactionlimit.id SEPARATOR ",") from servicedefinitionactionlimit where actionId=@actionId AND 
                                                               serviceDefinitionId =@serviceDefinitionId);
             
                      SET @existingActionLimitRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId AND 
                                      limitTypeId = @limitId );
             
             SET @existingActionRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit where contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId);
                                     
                     IF !isnull(@contarctFeatures) AND @contarctFeatures != "" AND !isnull(@serviceDefinitionActions) AND @serviceDefinitionActions != ""  THEN
                IF !isnull(@existingActionLimitRecords) AND @existingActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE contractactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingActionLimitRecords,'\'',';'); 
                           ELSE 
                                  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords) OR @existingActionRecords = "") THEN
                                       SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId, isNewAction) VALUES (',@recordsDataWithoutLimits,');');
                               ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
                                         SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId, isNewAction, limitTypeId,value) VALUES (',@recordsData,');');
                                  END IF;
                           END IF;
                     END IF;
            END IF;
            IF !isnull(@query) and @query != "" THEN
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            END IF;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;
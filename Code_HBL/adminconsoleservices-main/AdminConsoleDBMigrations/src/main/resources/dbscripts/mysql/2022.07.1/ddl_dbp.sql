DROP PROCEDURE IF EXISTS contract_address_communication_proc;

DELIMITER $$
$$
CREATE PROCEDURE `contract_address_communication_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SELECT c.*, s.name AS servicedefinitionName FROM `contract` as c JOIN `servicedefinition`as s ON (c.`servicedefinitionID` = s.`id`) WHERE c.`Id` = _contractId ;

SELECT * from `contractcommunication` WHERE `contractId` = _contractId;

SELECT * FROM `address` WHERE id IN (SELECT DISTINCT `addressId` from `contractaddress` WHERE `contractId` = _contractId);
 
                 
end $$
DELIMITER ;


DROP PROCEDURE IF EXISTS contract_corecustomer_accounts_proc;

DELIMITER $$
$$

CREATE PROCEDURE `contract_corecustomer_accounts_proc` (  
   IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
   BEGIN
   
   SELECT * FROM `contractcorecustomers` WHERE `contractcorecustomers`.`contractId` = _contractId;
   SELECT * FROM `contractaccounts` WHERE `contractaccounts`.`contractId` = _contractId;
   
end $$
DELIMITER ;


DROP PROCEDURE IF EXISTS contract_search_proc;

DELIMITER $$
$$
CREATE PROCEDURE `contract_search_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _contractName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phoneCountryCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phoneNumber varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
SET @isWhereAppened = false;
SET @shouldAndAppend = false;
SET @select_statement = "SELECT contract.id as contractId,contract.name as contractName,
contract.servicedefinitionId as serviceDefinitionId ,
(select servicedefinition.name from servicedefinition where servicedefinition.id = 
contract.servicedefinitionId limit 1) as serviceDefinitionName,
(select contractcommunication.value from contractcommunication where 
contractcommunication.contractId = contract.id and 
contractcommunication.typeId = 'COMM_TYPE_EMAIL' limit 1) as email ,
(select contractcorecustomers.coreCustomerName from contractcorecustomers 
 where contractcorecustomers.contractId = contract.id limit 1) as coreCustomerName 
 from contract where ";
IF(_contractId != '') then
SET @select_statement = CONCAT(@select_statement," contract.id = ", quote(_contractId));
SET @shouldAndAppend = true;
SET @isWhereAppened = true;
END IF;
    IF(_contractName != '') then
        IF(@isWhereAppened and @shouldAndAppend) THEN
			SET @select_statement = CONCAT(@select_statement , " and");
		END IF;
		SET @select_statement = CONCAT(@select_statement , " contract.name like '%",_contractName,"%'");
        SET @shouldAndAppend = true;
    END IF;
IF(_serviceDefinitionId != '') then
IF(@isWhereAppened = true and @shouldAndAppend = true) then
SET @select_statement = CONCAT(@select_statement , " and");
END IF;
SET @select_statement = CONCAT(@select_statement , " contract.servicedefinitionId = ",quote(_serviceDefinitionId));
SET @shouldAndAppend = true;
END IF;
IF(_coreCustomerId != '') then
IF(@isWhereAppened = true and @shouldAndAppend = true) then
SET @select_statement = CONCAT(@select_statement , " and");
END IF;
SET @select_statement = CONCAT(@select_statement ," contract.id IN (select DISTINCT contractcorecustomers.contractId from contractcorecustomers where  contractcorecustomers.coreCustomerId = ", quote(_coreCustomerId),")");
SET @shouldAndAppend = true;
END IF;
IF(_coreCustomerName != '') then
IF(@isWhereAppened=true and @shouldAndAppend=true) then
SET @select_statement = CONCAT(@select_statement , " and");
END IF;
SET @select_statement = CONCAT(@select_statement , " contract.id IN (select DISTINCT contractcorecustomers.contractId from contractcorecustomers where contractcorecustomers.coreCustomerName like '%",_coreCustomerName,"%')");
SET @shouldAndAppend = true;
END IF;
IF(_email != '') then
IF(@isWhereAppened = true and @shouldAndAppend = true) then
SET @select_statement = CONCAT(@select_statement , " and");
END IF;
SET @select_statement = CONCAT(@select_statement , " contract.id IN (select DISTINCT contractcommunication.contractId from contractcommunication where contractcommunication.typeId = 'COMM_TYPE_EMAIL' and contractcommunication.value = ",quote(_email),")");
SET @shouldAndAppend = true;
END IF;
 IF(_phoneCountryCode != '' and _phoneNumber!= '') THEN
        IF(@isWhereAppened = true and @shouldAndAppend = true) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
        END IF;
        SET @select_statement = CONCAT(@select_statement ,  " contract.id IN (select DISTINCT 
        contractcommunication.contractId from contractcommunication where 
        contractcommunication.typeId = 'COMM_TYPE_PHONE' and contractcommunication.value = ",quote(_phoneNumber),
        "and  contractcommunication.phoneCountryCode = ",quote(_phoneCountryCode),")");
        SET @shouldAndAppend = true;
    END IF;

    IF(_country != '') THEN
        IF(@isWhereAppened = true and @shouldAndAppend = true) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
        END IF;
        SET @select_statement = CONCAT(@select_statement , " contract.id IN (select DISTINCT contractaddress.contractId FROM contractaddress WHERE contractaddress.addressId IN (select DISTINCT address.id FROM address WHERE address.country = ",quote(_country),")",")");
        SET @shouldAndAppend = true;
    END IF;

IF (@select_statement != '')
THEN
SET @select_statement = CONCAT(@select_statement , ";") ;
end if;
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


end $$
DELIMITER ;


DROP PROCEDURE IF EXISTS contract_users_details_get_proc;

DELIMITER $$
$$
CREATE PROCEDURE `contract_users_details_get_proc`(
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
SET @customers = (select group_concat(contractcustomers.customerId SEPARATOR ",") from contractcustomers where
contractcustomers.contractId = _contractId);
set @customers = (select group_concat(distinct customerId SEPARATOR ",") from contractcustomers where FIND_IN_SET(contractcustomers.customerId,@customers));
SELECT
`customer`.`id` AS `customerId`,
`customer`.`FirstName` AS `firstName`,
`customer`.`MiddleName` AS `middleName`,
`customer`.`LastName` AS `lastName`,
`customer`.`UserName` AS `userName`,
`customer`.`Status_id` AS `statusId`,
`customer`.`DateOfBirth` AS `dateOfBirth`,
`customer`.`Ssn` AS `Ssn`,
`BI`.`BackendId` AS `primaryCoreCustomerId`,
`CC`.`Value` AS `Email`
FROM
(`customer`
LEFT JOIN `customercommunication` `CC` ON (`CC`.`Customer_id` = `customer`.`id` AND `CC`.`Type_id` = 'COMM_TYPE_EMAIL' AND `CC`.`isPrimary` = 1 AND FIND_IN_SET(`CC`.`Customer_id`,@customers))
LEFT JOIN `backendidentifier` `BI` ON (`BI`.`Customer_id` = `customer`.`id` AND `BI`.`BackendType` = _backendType and FIND_IN_SET(`BI`.`Customer_id`,@customers)))
WHERE
FIND_IN_SET(`customer`.`id`,@customers);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_roles_for_servicedefs_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_roles_for_servicedefs_proc` (
	IN _servicedefList LONGTEXT)
 BEGIN

	IF (_servicedefList != '' and _servicedefList IS NOT NULL) THEN
		set @sql_query = CONCAT("select * from `groupservicedefinition` where FIND_IN_SET(`serviceDefinitionId`,", quote(_servicedefList), ");");
	END IF;

PREPARE stmt FROM @sql_query;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

end $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `user_customers_withaccounts_proc`;
DELIMITER $$
CREATE PROCEDURE `user_customers_withaccounts_proc`(
    in _customerId varchar(50) character set UTF8 collate utf8_general_ci,
    in _coreCustomerId varchar(50) character set UTF8 collate utf8_general_ci
)
begin
    set @select_statement = "(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`contractId` AS `contractId`,
        `contractcustomers`.`autoSyncAccounts` AS `autoSyncAccounts`,
        `contract`.`name` AS `contractName`,
        `contractcustomers`.`isPrimary` AS `isPrimary`,
        `contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `contractcorecustomers`.`isBusiness` AS `isBusiness`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `membergroup`.`id` AS `roleId`,
        `membergroup`.`Name` AS `userRole`,
		`contractaccounts`.`accountId`,
		`contractaccounts`.`accountName`,
		`contractaccounts`.`statusDesc`,
		`contractaccounts`.`typeId`
    FROM
        ((`contractcustomers`
        LEFT JOIN `contract` ON (`contract`.`id` = `contractcustomers`.`contractId`)
		LEFT JOIN `contractcorecustomers` ON ((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `contractaccounts` ON ((`contractaccounts`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractaccounts`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `servicedefinition` ON (`servicedefinition`.`id` = `contract`.`servicedefinitionId`)
		LEFT JOIN `membergrouptype` ON (`membergrouptype`.`id` = `servicedefinition`.`serviceType`)
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
        AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
		AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))";

set @isWhereAppened = false;

set @shouldAndAppend = false;

if(_customerId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @isWhereAppened = true;

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`customerId` = ", quote(_customerId));
end if;

if(_coreCustomerId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @shouldAndAppend = true;
end if;

if(@isWhereAppened
and @shouldAndAppend) then
            set @select_statement = CONCAT(@select_statement , " and");

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`coreCustomerId` = ", quote(_coreCustomerId));
end if;

set @select_statement =  concat(@select_statement ,");");


prepare stmt from @select_statement;

execute stmt;

deallocate prepare stmt;
end $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `infinityuser_contractdetails_get_proc`;
DELIMITER $$
CREATE PROCEDURE `infinityuser_contractdetails_get_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.coreCustomerName AS coreCustomerName,
        customergroup.Group_id AS userRole,
        membergroup.name AS userRoleName,
		IF( ( (customergroup.Group_id = null or customergroup.Group_id =  '') AND (membergroup.name = '' or membergroup.name = null)),'false','true') AS isAssociated
	FROM
		contract
        LEFT JOIN contractcustomers ON (contractcustomers.contractId = contract.id and contractcustomers.customerId = _id)
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
        LEFT JOIN customergroup ON (customergroup.contractId = contract.id AND 
			customergroup.coreCustomerId = contractcorecustomers.coreCustomerId AND 
			customergroup.Customer_id = _id)
        LEFT JOIN membergroup ON (membergroup.id = customergroup.Group_id)
	WHERE
		customergroup.Customer_id = _id
        AND contractcustomers.customerId = _id;
	/*UNION
    select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.isPrimary AS isPrimary,
        NULL AS userRole,
        NULL AS userRoleName,
        NULL AS userRoleDescription,
        "false" AS isAssociated
	FROM
		contract
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
	WHERE
		contractcorecustomers.contractId IN ( select contractcustomers.contractId FROM contractcustomers WHERE contractcustomers.customerId = _id);
*/
end $$
DELIMITER ;


DROP PROCEDURE IF EXISTS get_associated_contractusers_proc;

DELIMITER $$
$$
create PROCEDURE `get_associated_contractusers_proc`(
	in _id varchar(50),
	in _backendType varchar(50)
)
BEGIN

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
	`customeraction` ca
	left join contractcustomers cc on
	(ca.contractId = cc.contractId and ca.coreCustomerId = cc.coreCustomerId)
	left join contractcorecustomers concore on
	(ca.contractId = concore.contractId and ca.coreCustomerId = concore.coreCustomerId)
	left join contract con on
	(concore.contractId = con.id)
	left join customergroup cg on
	(cc.contractId = cg.contractId and cc.coreCustomerId = cg.coreCustomerId and cg.Customer_id = cc.customerId)
	left join customer cus on
	(cg.Customer_id = cus.id)
	left join backendidentifier b on
	(b.Customer_id = cus.id and b.BackendType = _backendType)
	WHERE ca.Customer_id = _id and ca.Action_id = 'USER_MANAGEMENT_VIEW';
end $$
DELIMITER ;

DROP PROCEDURE IF EXISTS get_associated_contractaccounts_proc;

DELIMITER $$
$$
CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50)
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));
    SET @coreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts
							WHERE FIND_IN_SET(accountId,_accountIdList) and coreCustomerId = _coreCustomerId);
	set @otherCoreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts 
							WHERE FIND_IN_SET(accountId,_accountIdList) and coreCustomerId != _coreCustomerId);					

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;
    select @coreCustomerAccounts As coreCustomerAccounts;
    select @otherCoreCustomerAccounts As otherCoreCustomerAccounts;

end $$
DELIMITER ;



DROP PROCEDURE IF EXISTS verify_user_proc;

DELIMITER $$
$$
CREATE PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendIdentifiers varchar(5000) CHARACTER SET UTF8 COLLATE utf8_general_ci,
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
	
end $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `get_associated_contractusers_proc`;

DELIMITER $$

create PROCEDURE `get_associated_contractusers_proc`(
	in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

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
	`customeraction` ca
	left join contractcustomers cc on
	(ca.contractId = cc.contractId and ca.coreCustomerId = cc.coreCustomerId)
	left join contractcorecustomers concore on
	(ca.contractId = concore.contractId and ca.coreCustomerId = concore.coreCustomerId)
	left join contract con on
	(concore.contractId = con.id)
	left join customergroup cg on
	(cc.contractId = cg.contractId and cc.coreCustomerId = cg.coreCustomerId and cg.Customer_id = cc.customerId)
	left join customer cus on
	(cg.Customer_id = cus.id)
	left join backendidentifier b on
	(b.Customer_id = cus.id and b.BackendType = _backendType)
	WHERE ca.Customer_id = _id and ca.Action_id = 'USER_MANAGEMENT_VIEW';
end $$
DELIMITER ;

ALTER TABLE `excludedcontractaccounts` CHANGE COLUMN `coreCustomerId` `coreCustomerId` VARCHAR(50) CHARACTER SET 'utf8' COLLATE 'utf8_general_ci' NOT NULL ;

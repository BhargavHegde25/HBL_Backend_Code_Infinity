DROP procedure IF EXISTS `get_associated_contractaccounts_proc`;

DELIMITER $$
CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci ,
IN _coreCustomerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
   	
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));
    SET @coreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList) and contractaccounts.coreCustomerId = _coreCustomerId );
    SET @otherCoreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList) and contractaccounts.coreCustomerId <> _coreCustomerId );

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;
    select @coreCustomerAccounts As coreCustomerAccounts;
	select @otherCoreCustomerAccounts As otherCoreCustomerAccounts;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `user_customers_proc`;

DELIMITER $$
CREATE  PROCEDURE `user_customers_proc`(
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci
)
BEGIN
	SET SESSION group_concat_max_len = 1000000;
	
	SET @filteredContracts = (SELECT GROUP_CONCAT(DISTINCT `contractaccounts`.`contractId`) 
	FROM `contractaccounts` 
	WHERE `contractaccounts`.`statusDesc` != 'CLOSED' 
	AND `contractaccounts`.`contractId` IN
	(SELECT `contractcustomers`.`contractId` 
	FROM `contractcustomers` WHERE `contractcustomers`.`customerId` = _customerId));

    SET @select_statement = ("(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`companyLegalUnit` AS `companyLegalUnit`,
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
        `membergroup`.`Name` AS `userRole`
    FROM
        ((((((`contractcustomers`
        LEFT JOIN `contractcorecustomers` ON (((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
            AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `contract` ON ((`contract`.`id` = `contractcorecustomers`.`contractId`)))
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `contract`.`servicedefinitionId`)))
        LEFT JOIN `membergrouptype` ON ((`membergrouptype`.`id` = `servicedefinition`.`serviceType`)))
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
            AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
            AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))");
    SET @isWhereAppened = false;
    SET @shouldAndAppend = false;
    IF(_customerId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`customerId` = ",quote(_customerId));
    END IF;
    IF(_legalEntityId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`companyLegalUnit` = ",quote(_legalEntityId));
    END IF;

    IF(_coreCustomerId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      SET @select_statement =  concat(@select_statement ," `contractcustomers`.`coreCustomerId` = ",quote(_coreCustomerId));
    END IF;
    SET @select_statement =  concat(@select_statement," and `contractcustomers`.`contractId` in (", @filteredContracts,"));");
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER;

ALTER TABLE `customer` 
ADD COLUMN `isHeavyUser` BIT(1) NULL DEFAULT b'0' AFTER `isQRPaymentActivated`;

ALTER TABLE `customview` ADD COLUMN `coreCustomerId` VARCHAR(45) NULL;

ALTER TABLE `contractcustomers` ADD COLUMN `FavouriteStatus` BIT(1) DEFAULT b'0';

DELIMITER $$
CREATE PROCEDURE `update_customer_favorite_status`(
IN customer_Id VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN coreCustomer_Id VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN fav_status BIT,
IN legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

UPDATE contractcustomers SET FavouriteStatus = fav_status WHERE customerId = customer_Id and coreCustomerId= coreCustomer_Id and companyLegalUnit=legalEntityId;
END$$
DELIMITER;

DELIMITER $$
CREATE PROCEDURE `getCoreCustomerIdsAndAccounts`(
	IN `customerId` varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
	IN `companyLegalUnit` varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI 
)
BEGIN
SELECT 
`contractcustomers`.`coreCustomerId` AS `membership_id`,
`contract`.`name` AS `membership_name`,
`contractcustomers`.`FavouriteStatus` AS `favouriteStatus`,
COUNT(*) AS `accountsCount`
FROM 
((`customeraccounts` JOIN `contractcustomers` ON `customeraccounts`.`Customer_id`=customerId AND `customeraccounts`.`companyLegalUnit`=companyLegalUnit AND`customeraccounts`.`Customer_id`= `contractcustomers`.`customerId` AND `customeraccounts`.`coreCustomerId` =  `contractcustomers`.`coreCustomerId`) 
JOIN `contract` ON `contract`.`id` = `contractcustomers`.`contractId`)
GROUP BY `contractcustomers`.`contractId`;
END $$
DELIMITER ; 

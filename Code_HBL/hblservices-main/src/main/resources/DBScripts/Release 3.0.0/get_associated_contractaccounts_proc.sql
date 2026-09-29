USE `dbxdb`;
DROP procedure IF EXISTS `get_associated_contractaccounts_proc`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`get_associated_contractaccounts_proc`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci ,
IN _coreCustomerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;

	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));
    SET @coreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList) and contractaccounts.coreCustomerId = _coreCustomerId );
    SET @otherCoreCustomerAccounts = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList) and contractaccounts.coreCustomerId <> _coreCustomerId );

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;
    select @coreCustomerAccounts As coreCustomerAccounts;
	select @otherCoreCustomerAccounts As otherCoreCustomerAccounts;

END$$

DELIMITER ;
;


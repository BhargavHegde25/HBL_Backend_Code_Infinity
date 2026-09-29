USE `dbxdb`;
DROP procedure IF EXISTS `get_associated_contractaccounts_proc_bkp`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `get_associated_contractaccounts_proc_bkp`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;

END$$

DELIMITER ;


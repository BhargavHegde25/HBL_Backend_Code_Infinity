ALTER TABLE `wiretransfers` ADD COLUMN `transactionCurrency` VARCHAR(50) NULL;

DROP PROCEDURE IF EXISTS `fetch_nogroup_user_details_proc`;
DELIMITER $$    
CREATE PROCEDURE `fetch_nogroup_user_details_proc`(
in _customerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SELECT DISTINCT
  `customer`.`id` AS `userId`,
  `customer`.`isCombinedUser` AS `isCombinedUser`,
  `customer`.`UserName` AS `userName`,
  `customer`.`FirstName` AS `firstName`,
  `customer`.`LastName` AS `lastName`,
  `membergroup`.`Name` AS `role`
from
  `customer`
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id` )
  where 
      find_in_set(`customer`.`id`,_customerId)   AND  `customergroup`.`coreCustomerId` = _coreCustomerId ;
END$$
DELIMITER ;
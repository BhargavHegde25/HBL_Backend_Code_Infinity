USE `dbxdb`;
DROP procedure IF EXISTS `hbl_update_cardless_cash_expired_trx`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`hbl_update_cardless_cash_expired_trx`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinityqa`@`%` PROCEDURE `hbl_update_cardless_cash_expired_trx`()
BEGIN
    UPDATE transaction
    SET cashWithdrawalTransactionStatus = 'Expired'
    WHERE Type_id = 6
      AND LOWER(cashWithdrawalTransactionStatus) = 'pending'
      AND cashlessOTPValidDate <= NOW();

    -- Return the number of affected rows
    SELECT ROW_COUNT() AS affected_rows;
END$$

DELIMITER ;
;
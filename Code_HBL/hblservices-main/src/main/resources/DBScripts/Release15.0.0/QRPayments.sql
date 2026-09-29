QR Payments Transaction History
=====================================================================================================================================================

use dbxdb;
CREATE TABLE `qrtransaction_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `Customer_id` VARCHAR(50) NOT NULL,
  `fromAccountNumber` VARCHAR(34) NOT NULL,
  `toAccountNumber` VARCHAR(34) NOT NULL,
  `transactionType` VARCHAR(20) DEFAULT NULL,
  `amount` DECIMAL(20, 2) DEFAULT '0.00',
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fee` DECIMAL(20, 2) DEFAULT 0.00,
  `notes` TEXT DEFAULT NULL,
  `referenceId` VARCHAR(50) UNIQUE NOT NULL,
  `fromAccountName` VARCHAR(100) DEFAULT NULL,
  `toAccountName` VARCHAR(100) DEFAULT NULL,
  `fromAccountCurrency` varchar(45) DEFAULT NULL,
  `toAccountCurrency` varchar(45) DEFAULT NULL,
  `transactionCurrency` varchar(45) DEFAULT NULL,
  `description` TEXT DEFAULT NULL,
  `merchantCode` VARCHAR(15) DEFAULT NULL,
  `status` VARCHAR(20) NOT NULL,
  `errmsg` TEXT DEFAULT NULL,
  `createdby` VARCHAR(50) DEFAULT NULL,
  `modifiedby` VARCHAR(50) DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastsynctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_idx` (`Customer_id`),
  CONSTRAINT `FK_Transaction_Cust_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARSET=utf8mb3;

INSERT INTO qrtransaction_history
            (id,
             customer_id,
             fromaccountnumber,
             toaccountnumber,
             transactiontype,
             amount,
             date,
             fee,
             notes,
             referenceid,
             fromaccountname,
             toaccountname,
             fromaccountcurrency,
             toaccountcurrency,
             transactioncurrency,
             description,
             merchantcode,
             status,
             errmsg)
VALUES      ( 1000,
              '0018727159',
              '25127',
              '4902910000011495',
              '',
              100.00,
              '2025-02-05',
              0.00,
              'Twj5w7',
              'QRT24121O8V1Z',
              'JANAKI2318',
              'HOTEL ROYAL SINGI',
              'NPR',
              'NPR',
              'NPR',
              NULL,
              NULL,
              'Success',
              NULL ); 
			  
			  
			  
INSERT INTO qrtransaction_history
            (id,
             customer_id,
             fromaccountnumber,
             toaccountnumber,
             transactiontype,
             amount,
             date,
             fee,
             notes,
             referenceid,
             fromaccountname,
             toaccountname,
             fromaccountcurrency,
             toaccountcurrency,
             transactioncurrency,
             description,
             merchantcode,
             status,
             errmsg)
VALUES      ( 1001,
              '0018727159',
              '25127',
              '4902910000011495',
              '',
              1000.00,
              '2025-02-05',
              0.00,
              'qr payment',
              'QRT24121O8V1A',
              'JANAKI2318',
              'HOTEL ROYAL SINGI',
              'NPR',
              'NPR',
              'NPR',
              NULL,
              NULL,
              'Success',
              NULL ); 
			  
INSERT INTO qrtransaction_history
            (id,
             customer_id,
             fromaccountnumber,
             toaccountnumber,
             transactiontype,
             amount,
             date,
             fee,
             notes,
             referenceid,
             fromaccountname,
             toaccountname,
             fromaccountcurrency,
             toaccountcurrency,
             transactioncurrency,
             description,
             merchantcode,
             status,
             errmsg)
VALUES      ( 1002,
              '0018727159',
              '25127',
              '4902910000011495',
              '',
              10000.00,
              '2025-02-05',
              0.00,
              'qr payment MERCHANT',
              'QRT24121O8V1B',
              'JANAKI2318',
              'HOTEL ROYAL SINGI',
              'NPR',
              'NPR',
              'NPR',
              NULL,
              NULL,
              'Success',
              NULL ); 
==================
              
QRPayment charges
=======================================================================================================================
use dbxdb;

CREATE TABLE `qrpaymentcharges` (
  `id` int NOT NULL AUTO_INCREMENT,
  `aggregatorType` varchar(45) DEFAULT NULL,
  `minAmount` decimal(50,2) DEFAULT NULL,
  `maxAmount` decimal(50,2) DEFAULT NULL,
  `fee` decimal(50,2) DEFAULT NULL,
  `chargesType` varchar(45) DEFAULT NULL,
  `maxTransactionAmount` decimal(50,2) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci

INSERT INTO `qrpaymentcharges` (`id`, `aggregatorType`, `minAmount`, `maxAmount`, `fee`, `chargesType`, `maxTransactionAmount`) VALUES
(101, 'agg-1', 1001.00, 10000.00, 10.00, 'Slab', NULL),
(102, 'agg-1', 101.00, 1000.00, 5.00, 'Slab', NULL),
(103, 'agg-1', 1.00, 100.00, 1.00, 'Slab', NULL),
(104, 'agg-2', 1001.00, 10000.00, 5.00, 'Slab', 10.00),
(105, 'agg-2', 101.00, 1000.00, 2.00, 'Slab', 10.00),
(106, 'agg-2', 1.00, 100.00, 1.00, 'Slab', 10.00),
(107, 'agg-3', 1001.00, 10000.00, 5.00, 'Slab', 10.00),
(108, 'agg-3', 101.00, 1000.00, 3.00, 'Slab', 10.00),
(109, 'agg-3', 1.00, 100.00, 1.00, 'Slab', 10.00),
(110, 'agg-4', 1001.00, 10000.00, 5.00, 'Slab', 10.00),
(111, 'agg-4', 101.00, 1000.00, 4.00, 'Slab', 10.00),
(112, 'agg-4', 1.00, 100.00, 1.00, 'Slab', 10.00);

===============================================================================================================================================================

Added aggregatorName column to the existing qrpaymentcharges table and inserted its value for each aggregatorType.
ALTER TABLE dbxdb.qrpaymentcharges 
ADD COLUMN aggregatorName VARCHAR(100) NULL AFTER aggregatorType;

UPDATE qrpaymentcharges  
SET aggregatorName = 'Nepal Pay'  
WHERE id IN (101, 102, 103);

UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'Smart QR' WHERE (`id` = '104');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'Smart QR' WHERE (`id` = '105');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'Smart QR' WHERE (`id` = '106');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'SAME_BANK' WHERE (`id` = '107');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'SAME_BANK' WHERE (`id` = '108');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'SAME_BANK' WHERE (`id` = '109');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'OTHER_BANK' WHERE (`id` = '110');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'OTHER_BANK' WHERE (`id` = '111');
UPDATE `dbxdb`.`qrpaymentcharges` SET `aggregatorName` = 'OTHER_BANK' WHERE (`id` = '112');

==================================================================================================================================================================
Created a new table - qr_validation_results to store the validation results of QR Payments.

use dbxdb;
CREATE TABLE qr_validation_results (
    id               BIGINT AUTO_INCREMENT PRIMARY KEY,
    transactionId   VARCHAR(50) NOT NULL UNIQUE,
    aggregatorType  VARCHAR(20) NOT NULL,
    aggregatorName  VARCHAR(50) NOT NULL,
    validationResult TEXT NOT NULL,  
    status           VARCHAR(20) DEFAULT NULL,
    created_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at       TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB AUTO_INCREMENT=100 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

QR Payment Service
==============================================================================================================================================================================
---Added the new column validationRequest in the existing table - qr_validation_results to store the validation request.

ALTER TABLE dbxdb.qr_validation_results 
ADD COLUMN validationRequest TEXT NULL AFTER aggregatorName;

---Modified the column validationResult to nullable as it will be empty for personal qr transactions.

ALTER TABLE dbxdb.qr_validation_results 
MODIFY COLUMN validationResult TEXT NULL;

---Added the new column fromAccountNumber in the existing table - qr_validation_results to store the from account number.

ALTER TABLE dbxdb.qr_validation_results
ADD COLUMN fromAccountNumber VARCHAR(50) NOT NULL AFTER aggregatorName;

---Added the new columns amount, debitAmount, transactionFee in the existing table - qr_validation_results to store the amount and transaction fee details.

ALTER TABLE dbxdb.qr_validation_results
ADD COLUMN amount DOUBLE NOT NULL AFTER fromAccountNumber,
ADD COLUMN debitAmount DOUBLE NOT NULL AFTER amount,
ADD COLUMN transactionFee DOUBLE NOT NULL AFTER debitAmount;

---Since there’s no default value and the columns are NOT NULL, updating all existing rows with some value (like 0.0).
UPDATE dbxdb.qr_validation_results
SET amount = 0.0,
    debitAmount = 0.0,
    transactionFee = 0.0
WHERE amount IS NULL OR debitAmount IS NULL OR transactionFee IS NULL;

---
ALTER TABLE qrtransaction_history 
MODIFY referenceId varchar(50) DEFAULT NULL;

---Removed the hardcoded transactions from the transaction history table
UPDATE `dbxdb`.`qrtransaction_history` SET `softdeleteflag` = '1' WHERE (`id` = '1000');
UPDATE `dbxdb`.`qrtransaction_history` SET `softdeleteflag` = '1' WHERE (`id` = '1001');
UPDATE `dbxdb`.`qrtransaction_history` SET `softdeleteflag` = '1' WHERE (`id` = '1002');

---MFA Queries

INSERT INTO `dbxdb`.`mfa` (`id`, `App_id`, `Action_id`, `FrequencyType_id`, `FrequencyValue`, `Status_id`, `Description`, `PrimaryMFAType`, `SecondaryMFAType`, `SMSText`, `EmailSubject`, `EmailBody`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('1868396794', 'RETAIL_AND_BUSINESS_BANKING', 'QR_PAYMENTS_CREATE', 'VALUE_BASED', '5', 'SID_ACTIVE', 'Create QR Payments', 'TRANSACTION_PIN', 'SECURE_ACCESS_CODE', 'Your OTP is [#]OTP[/#]', 'OTP', 'HI,<br><br class=""><br>Your OTP is [#]OTP[/#]', 'UID10', 'admin1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'NP0010001');

UPDATE `dbxdb`.`featureaction` SET `MFA_id` = '1868396794' WHERE (`id` = 'QR_PAYMENTS_CREATE') and (`companyLegalUnit` = 'NP0010001');

INSERT INTO `dbxdb`.`dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`, `createdts`, `lastmodifiedts`, `companyLegalUnit`) VALUES ('QR_PAYMENTS_CREATE', 'QR_PAYMENTS_CREATE', 'QR_PAYMENTS', 'QR Payments', 'Create QR Payments', '2024-09-29 10:27:29', '2024-09-29 10:27:29', 'NP0010001');

---toAccountNumber can be null if transactionType is "NepalPay" in qrtransaction_history table 

ALTER TABLE dbxdb.qrtransaction_history 
MODIFY toAccountNumber varchar(34) NULL;

---Updated charges config for Personal QR - Same Bank Account Transfer (No fee applicable)

UPDATE `dbxdb`.`qrpaymentcharges` SET `fee` = '0.00' WHERE (`id` = '107');
UPDATE `dbxdb`.`qrpaymentcharges` SET `fee` = '0.00' WHERE (`id` = '108');
UPDATE `dbxdb`.`qrpaymentcharges` SET `fee` = '0.00' WHERE (`id` = '109');

---Alter QR Validation Results Table - Add new columns to capture full payload and request type
---Refresh Meta data after executing below scripts in Arrangements app for dbxdb.qr_validation_results
ALTER TABLE dbxdb.qr_validation_results 
    ADD actualPayload VARCHAR(4000),   -- Stores the decoded Base64 or raw JSON/EMV payload
    ADD requestType   VARCHAR(20);     -- Identifies the type of request: 'JSON' or 'EMV'
    
--- Modified column length of `transactionType` in `qrtransaction_history`
--- Increased length from VARCHAR(20) to VARCHAR(50) to avoid "Data too long for column 'transactionType'" errors.
ALTER TABLE dbxdb.qrtransaction_history 
    MODIFY transactionType VARCHAR(50);
    
--- Modified `referenceId` column in `qrtransaction_history` table to allow NULL values.
ALTER TABLE dbxdb.qrtransaction_history 
MODIFY COLUMN referenceId VARCHAR(50) NULL;

=====================================================================================================================================================================================================================================================================================================================================================================================================
Created new table qrtransaction_history to store all QR transaction details
========================================================================================================================================
use dbxdb;
CREATE TABLE `qrtransaction_history` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `Customer_id` VARCHAR(50) NOT NULL,
  `fromAccountNumber` VARCHAR(34) NOT NULL,
  `toAccountNumber` VARCHAR(34) NULL,
  `transactionType` VARCHAR(50) DEFAULT NULL,
  `amount` DECIMAL(20, 2) DEFAULT '0.00',
  `date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `fee` DECIMAL(20, 2) DEFAULT 0.00,
  `notes` TEXT DEFAULT NULL,
  `referenceId` VARCHAR(50) DEFAULT NULL,
  `fromAccountName` VARCHAR(100) DEFAULT NULL,
  `toAccountName` VARCHAR(100) DEFAULT NULL,
  `fromAccountCurrency` VARCHAR(45) DEFAULT NULL,
  `toAccountCurrency` VARCHAR(45) DEFAULT NULL,
  `transactionCurrency` VARCHAR(45) DEFAULT NULL,
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
  CONSTRAINT `FK_Transaction_Cust_id`
      FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=1000 CHARSET=utf8mb3;
========================================================================================================================================
Added fields for storing external service request and response payloads.

ALTER TABLE dbxdb.qrtransaction_history
    ADD COLUMN externalServiceRequest TEXT DEFAULT NULL AFTER errmsg,
    ADD COLUMN externalServiceResponse TEXT DEFAULT NULL AFTER externalServiceRequest;
========================================================================================================================================
Updated status column to include a default value 'PENDING'.

ALTER TABLE qrtransaction_history
MODIFY `status` varchar(20) NOT NULL DEFAULT 'PENDING';

========================================================================================================================================
A new column transactionId has been added to the qrtransaction_history table.

ALTER TABLE dbxdb.qrtransaction_history
ADD COLUMN transactionId VARCHAR(50) NOT NULL DEFAULT '' AFTER transactionType;

========================================================================================================================================
Modified the fromAccountNumber column to use a default empty string ('') while retaining the NOT NULL constraint.
This change supports recording transactions for the "Transaction has already been processed" scenario, where the fromAccountNumber may not be available, while still allowing the transaction history record to be inserted successfully.

ALTER TABLE dbxdb.qrtransaction_history
MODIFY COLUMN fromAccountNumber VARCHAR(34) NOT NULL DEFAULT '';

===========================================================================================================================================================================================================================================================================================



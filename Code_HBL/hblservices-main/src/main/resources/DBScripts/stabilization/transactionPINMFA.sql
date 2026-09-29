--------------------------- Transaction PIN MFA spotlight scripts--------------------------------


INSERT INTO `dbxdb`.`mfatype` (`id`, `Name`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'Transaction Pin', 'Transaction Pin', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');

INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'LOCK_USER', 'true', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'LOGOUT_USER', 'true', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'MAX_FAILED_ATTEMPTS_ALLOWED', '3', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'SAC_CODE_EXPIRES_AFTER', '5', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'SAC_CODE_LENGTH', '3', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'SAC_MAX_RESEND_REQUESTS_ALLOWED', '3', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');
INSERT INTO `dbxdb`.`mfaconfigurations` (`MFA_id`, `MFAKey_id`, `value`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSACTION_PIN', 'SAC_PREFERENCE_CRITERIA', 'DISPLAY_NO_VALUE', 'Kony Dev', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '2023-11-08 11:57:06', '0', 'NP0010001');

Note:
1. MFA Challange type for scenarios need to update primary as transaction pin
2. Make inactive security alert category of alerts module in spotlight
3. Alert content required to update in spotlight 


use dbxdb;
CREATE TABLE `transactionpin` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `pin` varchar(10) DEFAULT NULL,
  `pinType` varchar(45) DEFAULT NULL,
  `InvalidAttempt` int DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_idx` (`Customer_id`),
  CONSTRAINT `FK_Custm_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3

-----------------------------------

DELETE FROM `dbxdb`.`mfaconfigurations` WHERE (`MFA_id` = 'TRANSACTION_PIN') and (`MFAKey_id` = 'SAC_MAX_RESEND_REQUESTS_ALLOWED');

-------------Note----------------------
created get, update, delete operations for transactionpin table in rdbms services
/* Enrollment Requests, Disputed Transactions and Reset Transaction PIN menu options visible based on the permissions */

INSERT INTO `dbxdb`.`permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_VIEW_ENROLLMENT_REQUESTS', 'Permission Type View Enrollment Requests', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0');

INSERT INTO `dbxdb`.`permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('PID811', 'PER_TYPE_VIEW_ENROLLMENT_REQUESTS', 'SID_ACTIVE', 'ViewEnrollCustomer', 'Users with this permission can view enrollment requests', '0', 'TRUE', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0', 'NP0010001');

INSERT INTO `dbxdb`.`permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_UPDATE_ENROLLMENT_REQUESTS', 'Permission Type Enrollment Requests', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0');

INSERT INTO `dbxdb`.`permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('PID812', 'PER_TYPE_UPDATE_ENROLLMENT_REQUESTS', 'SID_ACTIVE', 'UpdateEnrollCustomer', 'Users with this permission update enrollment requests', '0', 'TRUE', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0', 'NP0010001');

INSERT INTO `dbxdb`.`permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_VIEW_CARD_REQ', 'Permission Type View Cards', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0');

INSERT INTO `dbxdb`.`permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('PID813', 'PER_TYPE_VIEW_CARD_REQ', 'SID_ACTIVE', 'ViewCardReq', 'Users with this permission can view cards requests', '0', 'TRUE', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0', 'NP0010001');

INSERT INTO `dbxdb`.`permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_UPDATE_CARD_REQ', 'Permission Type Update Card Requests', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0');

INSERT INTO `dbxdb`.`permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('PID814', 'PER_TYPE_UPDATE_CARD_REQ', 'SID_ACTIVE', 'UpdateCardReq', 'Users with this permission can update cards requests', '0', 'TRUE', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0', 'NP0010001');

INSERT INTO `dbxdb`.`permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_VIEW_TRANSACIONPIN_REQ', 'Permission Type View Transaction Pin Requests', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0');

INSERT INTO `dbxdb`.`permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('PID815', 'PER_TYPE_VIEW_TRANSACIONPIN_REQ', 'SID_ACTIVE', 'ViewTransactionPinReq', 'Users with this permission can update transaction pin requests', '0', 'TRUE', 'Kony User', 'Kony Dev', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '2024-09-29 10:24:42', '0', 'NP0010001');

/*Default current Time stamp changed in to CURRENT_TIMESTAMP */
ALTER TABLE dbxdb.contract 
MODIFY COLUMN createdts TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP;

/* Customer Global Limits-GetCumulativeTransactionAmount 
- Crate new dbxdb integration in TransactionsLimitDBService-dbxdb_GetCumulativeTransactionAmount- service in TransactionLimitsEngine.
- And update refreshMetaData and publish the TransactionLimitsEngine Fabric App
*/
USE `dbxdb`;
DROP procedure IF EXISTS `GetCumulativeTransactionAmount`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`GetCumulativeTransactionAmount`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinity`@`%` PROCEDURE `GetCumulativeTransactionAmount`(
IN in_start_Date  DATE,
IN in_end_Date  DATE,
IN in_companyId  VARCHAR(50),
IN in_paidBy  VARCHAR(50) 
)
BEGIN
  Select (SELECT COALESCE(SUM(amount), 0)
            FROM interbankfundtransfers
            WHERE 
                status IN ('Executed', 'Sent', 'Pending', 'Scheduled')
                AND scheduledDate >= in_start_Date 
                AND scheduledDate <  in_end_Date
                AND paidBy = in_paidBy
                AND companyId = in_companyId
        ) + 
        (
            -- Bill Pay Transfers
            SELECT COALESCE(SUM(amount), 0)
            FROM billpaytransfers
            WHERE 
                status IN ('Executed', 'Sent', 'Pending', 'Scheduled')
                AND scheduledDate >= in_start_Date
                AND scheduledDate <  in_end_Date 
                AND paidBy = in_paidBy
                AND companyId = in_companyId
        ) + 
        (
            -- QR Transaction History
            SELECT COALESCE(SUM(amount), 0)
            FROM qrtransaction_history
            WHERE 
                status IN ('Executed', 'Sent', 'Pending', 'Scheduled')
                AND `date` >= in_start_Date  
                AND `date` <  in_end_Date
                AND companyId = in_companyId
                AND paidBy = in_companyId
        ) + (
            -- Intrabank Transfers
            SELECT COALESCE(SUM(amount), 0)
            FROM intrabanktransfers
            WHERE 
                status IN ('Executed', 'Sent', 'Pending', 'Scheduled')
                AND scheduledDate >= in_start_Date
                AND scheduledDate <  in_end_Date
                AND paidBy = in_paidBy
                AND companyId = in_companyId
                AND transactionType != 'PARKING_ACCOUNT_TRANSFER'
        ) AS total_sum;
        END$$

DELIMITER ;
;



        
/* 
 * companyId column added in qrtransaction_history table in  for getting the exchausted limits based on the companyid in GetCumulativeTransactionAmount store proc.  
->Once added please update the refreshmetadata in dbpRbLocalServicesdb-dbxdb_qrtransaction_history_create service in Authentication App.
*/ 
        
ALTER TABLE `dbxdb`.`qrtransaction_history` 
ADD COLUMN `companyId` VARCHAR(50) NULL DEFAULT NULL AFTER `paidBy`;

        
        
/* Update Cummulative Limits while updating feature limits */

USE `dbxdb`;
DROP procedure IF EXISTS `hbl_cummulative_limits_update_proc`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`hbl_cummulative_limits_update_proc`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinity`@`%` PROCEDURE `hbl_cummulative_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2),
  IN _maxMBTxLimit decimal(20,2),
  IN _dailyMBLimit decimal(20,2),
  IN _weeklyMBLimit decimal(20,2) 
)
BEGIN
	-- updating groupactionlimits
  UPDATE groupactionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP'; 
  UPDATE groupactionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP';
  UPDATE groupactionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP';
  UPDATE groupactionlimit SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP'; 
  UPDATE groupactionlimit SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'MB_DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP';
  UPDATE groupactionlimit SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'MB_WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit And Group_id = 'DEFAULT_GROUP';
  -- updating servicedefinitionactionlimit
  UPDATE servicedefinitionactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit AND serviceDefinitionId = '5801fa32-a416-45b6-af01-b22e2de93777'; 
  UPDATE servicedefinitionactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit AND serviceDefinitionId = '5801fa32-a416-45b6-af01-b22e2de93777';
  UPDATE servicedefinitionactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit AND serviceDefinitionId = '5801fa32-a416-45b6-af01-b22e2de93777'; 
   UPDATE servicedefinitionactionlimit SET value = _maxMBTxLimit where actionId = _action AND limitTypeId = 'MB_MAX_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit AND serviceDefinitionId = '5801fa32-a416-45b6-af01-b22e2de93777'; 
  UPDATE servicedefinitionactionlimit SET value = _dailyMBLimit where actionId = _action AND limitTypeId = 'MB_DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit AND serviceDefinitionId = '5801fa32-a416-45b6-af01-b22e2de93777';
  UPDATE servicedefinitionactionlimit SET value = _weeklyMBLimit where actionId = _action AND limitTypeId = 'MB_WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit; 
  -- updating contractactionlimit
  UPDATE contractactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE contractactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit ;
  UPDATE contractactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE contractactionlimit SET value = _maxMBTxLimit where actionId = _action AND limitTypeId = 'MB_MAX_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE contractactionlimit SET value = _dailyMBLimit where actionId = _action AND limitTypeId = 'MB_DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE contractactionlimit SET value = _weeklyMBLimit where actionId = _action AND limitTypeId = 'MB_WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  -- updating customeraction
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customeraction SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'MB_DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'MB_WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_MB_TRANSACTION_LIMIT'  AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customeraction SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_MB_DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customeraction SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'AUTO_DENIED_MB_WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  -- updating customerlimitgrouplimits
  UPDATE customerlimitgrouplimits SET value = _maxTxLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customerlimitgrouplimits SET value = _dailyLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customerlimitgrouplimits SET value = _weeklyLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'WEEKLY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customerlimitgrouplimits SET value = _maxMBTxLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  UPDATE customerlimitgrouplimits SET value = _dailyMBLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'MB_DAILY_LIMIT'  AND companyLegalUnit = _companyLegalUnit ;
  UPDATE customerlimitgrouplimits SET value = _weeklyMBLimit where limitGroupId = 'SINGLE_PAYMENT' AND LimitType_id = 'MB_WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit ; 
  END$$

DELIMITER ;
;





/*
 * Contract table added new column for isConsentProvided
 * 
 * after executed below query need to be refresh metadata in contract integration services in dbprblocalservices in Authentication App.
 */
ALTER TABLE `dbxdb`.`contract` 
ADD COLUMN `isConsentProvided` VARCHAR(45) NULL DEFAULT NULL AFTER `channelAccess`;


/*
 * Delete All Customer data from dbxdb
 */
USE `dbxdb`;
DROP procedure IF EXISTS `DeleteCustomerAndContractDataByUsername`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`DeleteCustomerAndContractDataByUsername`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinity`@`%` PROCEDURE `DeleteCustomerAndContractDataByUsername`(
    IN p_UserName VARCHAR(255)
)
BEGIN
    DECLARE v_Customer_id VARCHAR(255);
    DECLARE v_contractId VARCHAR(255);
    DECLARE v_found_count INT DEFAULT 0;
    DECLARE v_step VARCHAR(100) DEFAULT '';
    DECLARE v_error_msg TEXT; 
    
    -- Proper error handler
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN
        GET DIAGNOSTICS CONDITION 1 v_error_msg = MESSAGE_TEXT;
        ROLLBACK;
        SELECT CONCAT('Failed at step: ', v_step, '. Error: ', v_error_msg) AS result;
    END;
    
    -- Debug: Check if username exists
    SELECT COUNT(*) INTO v_found_count 
    FROM dbxdb.customer 
    WHERE UserName = p_UserName;
    
    IF v_found_count = 0 THEN
        SELECT CONCAT('No customer found with UserName: ', p_UserName) AS result;
    ELSE
        -- Get customerId and contractId with better error handling
        SELECT cc.customerId, cc.contractId 
        INTO v_Customer_id, v_contractId
        FROM dbxdb.contractcustomers cc
        WHERE cc.customerId IN (
            SELECT id FROM dbxdb.customer WHERE UserName = p_UserName
        )
        LIMIT 1;
    
    -- Check if we found the contract customer record
        IF v_Customer_id IS NULL OR v_contractId IS NULL THEN
            SELECT CONCAT('Customer found but no contract relationship for UserName: ', p_UserName) AS result;
        ELSE
            START TRANSACTION;
            
            -- Debug output
            SELECT CONCAT('Starting deletion for CustomerId: ', v_Customer_id, ', ContractId: ', v_contractId) AS debug_info;
            
            -- Delete operations in logical order (child tables first)
        
        -- Delete operations for customer-related data
        SET v_step = 'customernote';
        DELETE FROM dbxdb.customernote WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerimage';
		DELETE FROM dbxdb.customerimage WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customernotification';
		DELETE FROM dbxdb.customernotification WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customeralertchannel';
		DELETE FROM dbxdb.customeralertchannel WHERE CustomerId = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerrequest';
		DELETE FROM dbxdb.customerrequest WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerproduct';
		DELETE FROM dbxdb.customerproduct WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerlegalentity';
		DELETE FROM dbxdb.customerlegalentity WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customeraction';
		DELETE FROM dbxdb.customeraction WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customeraccounts';
		DELETE FROM dbxdb.customeraccounts WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerentitlement';
		DELETE FROM dbxdb.customerentitlement WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customergroup';
		DELETE FROM dbxdb.customergroup WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerpreference';
		DELETE FROM dbxdb.customerpreference WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customercommunication';
		DELETE FROM dbxdb.customercommunication WHERE Customer_Id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'passwordhistory';
		DELETE FROM dbxdb.passwordhistory where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'backendidentifier';
		DELETE FROM dbxdb.backendidentifier WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'csrassistgrant';
		DELETE FROM dbxdb.csrassistgrant where customerId = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'pinhistory';
		DELETE FROM dbxdb.pinhistory where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'esewaApiAuditLog';
		DELETE FROM dbxdb.esewaApiAuditLog where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'requestnewcard';
		DELETE FROM dbxdb.requestnewcard where customerId = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'transactionpin';
		DELETE FROM dbxdb.transactionpin where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'customerdevice';
		DELETE FROM dbxdb.customerdevice where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'himalfixeddeposits';
		DELETE FROM dbxdb.himalfixeddeposits where Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'disputeTransactions';
		DELETE FROM dbxdb.disputeTransactions WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'normalfixeddeposits';
		DELETE FROM dbxdb.normalfixeddeposits WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'transactionpinResetReq';
		DELETE FROM dbxdb.transactionpinResetReq WHERE Customer_id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'recentcurrencies';
		DELETE FROM dbxdb.recentcurrencies WHERE customerId = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'ownaccounttransfers';
		DELETE FROM dbxdb.ownaccounttransfers WHERE createdby = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'interbankfundtransfers';
		DELETE FROM dbxdb.interbankfundtransfers WHERE createdby = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'intrabanktransfers';
		DELETE FROM dbxdb.intrabanktransfers WHERE createdby = CONVERT(v_Customer_id USING utf8);
        
        -- Delete operations for contract-related data
        SET v_step = 'contractactionlimit';
        DELETE FROM dbxdb.contractactionlimit WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractfeatures';
		DELETE FROM dbxdb.contractfeatures WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractaccounts';
		DELETE FROM dbxdb.contractaccounts WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractcustomers';
		DELETE FROM dbxdb.contractcustomers WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractaddress';
		DELETE FROM dbxdb.contractaddress WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractcommunication';
		DELETE FROM dbxdb.contractcommunication WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'contractcorecustomers';
		DELETE FROM dbxdb.contractcorecustomers WHERE contractId = CONVERT(v_contractId USING utf8);
        SET v_step = 'accountlevelactionlimit';
		DELETE FROM dbxdb.accountlevelactionlimit WHERE contractId = CONVERT(v_contractId USING utf8);
                
        -- Delete the main customer and contract records
        SET v_step = 'customer';
        DELETE FROM dbxdb.customer WHERE id = CONVERT(v_Customer_id USING utf8);
        SET v_step = 'contract';
        DELETE FROM dbxdb.contract WHERE id = CONVERT(v_contractId USING utf8);
        
        COMMIT;
            SELECT CONCAT('Successfully deleted data for UserName: ', p_UserName) AS result;
        END IF;
    END IF;
END$$

DELIMITER ;
;

/* Enroll email template changes */
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<p>&nbsp;</p><center><img src=\"https://ib1.himalayanbank.com/images/brand-login.png\" alt=\"hbl_logo\"  style=\"width:550px; display: block; margin-left: auto; margin-right: auto;\"/><br /><br /> <br /><table align=\"center\" style=\"width: 600px;\" ><tr><td style=\"border-style: solid; border-width: 6px; border-color: black;\"><br /><p  style=\"margin-left: 10px;\"> USER ACTIVATION NOTIFICATION <br /><br />Dear %firstName%,<br /><br />You are enrolled to Digital Banking Channel. Please activate your account now.<br /><br />%userName% is your username. Activation code is %otp% sent to your registered mobile number.<br /><br />You are required to input your username &amp; activation code in the link below.<br /><br /><center>To activate your account and set a password,<a style=\"color: #11abeb;\" href=\"%resetPasswordLink%\">click here</a> <br />or paste the following link on your browser: <br /><br /><a style=\"color: #11abeb;\">%resetPasswordLink%</a><br/><br/>The activation code will expire in %activationCodeExpiry% days , so activate it right away.</center></p><br /><br /><br /></td></tr><tr><td><br /><b>Auto-generated Email Notification. Please do not reply to this email address.</b> <br /><br />The information in this mail is confidential and is intended solely for the addressee. Access to this mail by anyone else is unauthorized. Copying or further distribution beyond the original recipient may be unlawful. <br /><br /></td></tr></table></center>' WHERE (`id` = '213');
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<p>&nbsp;</p><center><img src=\"https://ib1.himalayanbank.com/images/brand-login.png\" alt=\"hbl_logo\"  style=\"width:550px; display: block; margin-left: auto; margin-right: auto;\"/><br /><br /> <br /><table align=\"center\" style=\"width: 600px;\" ><tr><td style=\"border-style: solid; border-width: 6px; border-color: black;\"><br /><p  style=\"margin-left: 10px;\"> USER ACTIVATION NOTIFICATION <br /><br />Dear %firstName%,<br /><br />Please note that your registration request for HBL Digital Banking cannot be completed due to some limitations. For details, please contact your branch or call us @ 01-5971399 or contact us through viber / whatsapp on 9803560838.<br /><br />We apologize for any inconvenience caused to you.<br /><br/><br />Thank you.<br /><br />Himalayan Bank<br/>Card & Digital Channel Center.<br ></p><br /></td></tr><tr><td><br /><b>Auto-generated Email Notification. Please do not reply to this email address.</b> <br /><br />The information in this mail is confidential and is intended solely for the addressee. Access to this mail by anyone else is unauthorized. Copying or further distribution beyond the original recipient may be unlawful. <br /><br /></td></tr></table></center>' WHERE (`id` = '313');






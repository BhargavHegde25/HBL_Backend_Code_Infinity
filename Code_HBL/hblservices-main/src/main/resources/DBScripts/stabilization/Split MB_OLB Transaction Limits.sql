INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('MB_DAILY_LIMIT', 'Mobile Daily limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('MB_MAX_TRANSACTION_LIMIT', 'Mobile Max transaction limit', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('MB_MIN_TRANSACTION_LIMIT', 'Mobile Min transaction limit', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('MB_WEEKLY_LIMIT', 'Mobile Weekly limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');
INSERT INTO `dbxdb`.`actionlimit` (`Action_id`, `LimitType_id`, `value`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_DAILY_LIMIT', '1000.00', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actionlimit` (`Action_id`, `LimitType_id`, `value`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_MAX_TRANSACTION_LIMIT', '500.00', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actionlimit` (`Action_id`, `LimitType_id`, `value`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_MIN_TRANSACTION_LIMIT', '1.00', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actionlimit` (`Action_id`, `LimitType_id`, `value`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_WEEKLY_LIMIT', '5000.00', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '2023-11-08 12:00:17', '0', 'NP0010001');
INSERT INTO `dbxdb`.`groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `isNewAction`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('a9f869a5-791b-11ea-9300-00090faa0002', 'DEFAULT_GROUP', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_DAILY_LIMIT', '1000.00', '0', 'UID11', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '0', 'NP0010001');
INSERT INTO `dbxdb`.`groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `isNewAction`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('a9f7cef5-791b-11ea-9300-00090faa0002', 'DEFAULT_GROUP', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_MAX_TRANSACTION_LIMIT', '1000.00', '0', 'UID11', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '0', 'NP0010001');
INSERT INTO `dbxdb`.`groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `isNewAction`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('a9f90b8a-791b-11ea-9300-00090faa0002', 'DEFAULT_GROUP', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_WEEKLY_LIMIT', '20000.00', '0', 'UID11', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '2023-11-08 12:00:19', '0', 'NP0010001');
INSERT INTO `dbxdb`.`servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`, `isNewAction`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('b447d65a-d42c-454e-822f-274b41eb0f561', '5801fa32-a416-45b6-af01-b22e2de93777', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_DAILY_LIMIT', '500.00', '0', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '0', 'NP0010001');
INSERT INTO `dbxdb`.`servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`, `isNewAction`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('d7302290-6462-4c5b-8cfa-c4da14bd20341', '5801fa32-a416-45b6-af01-b22e2de93777', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_WEEKLY_LIMIT', '20000.00', '0', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '0', 'NP0010001');
INSERT INTO `dbxdb`.`servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`, `isNewAction`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('e4b284c7-62c0-4d5f-9a98-663911c2559f1', '5801fa32-a416-45b6-af01-b22e2de93777', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE', 'MB_MAX_TRANSACTION_LIMIT', '100.00', '0', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '2023-11-08 12:02:47', '0', 'NP0010001');


-------------------------------------------------------------------
SP-1
------------------------------------------------------------------
USE `dbxdb`;
DROP procedure IF EXISTS `hbl_limits_update_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `hbl_limits_update_proc`(
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
  UPDATE groupactionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE groupactionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE groupactionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE servicedefinitionactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE servicedefinitionactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND value > _dailyLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE servicedefinitionactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND value > _weeklyLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE contractactionlimit SET value = _maxTxLimit where actionId = _action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE contractactionlimit SET value = _dailyLimit where actionId = _action AND limitTypeId = 'DAILY_LIMIT' AND value > _dailyLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE contractactionlimit SET value = _weeklyLimit where actionId = _action AND limitTypeId = 'WEEKLY_LIMIT' AND value > _weeklyLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE customeraction SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > _maxTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE customeraction SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND value > _dailyLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE customeraction SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND value > _weeklyLimit AND companyLegalUnit = _companyLegalUnit;

  
  UPDATE groupactionlimit SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT' AND value > _maxMBTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE groupactionlimit SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'MB_DAILY_LIMIT' AND value > _dailyMBLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE groupactionlimit SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'MB_WEEKLY_LIMIT' AND value > _weeklyMBLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE servicedefinitionactionlimit SET value = _maxMBTxLimit where actionId = _action AND limitTypeId = 'MB_MAX_TRANSACTION_LIMIT' AND value > _maxMBTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE servicedefinitionactionlimit SET value = _dailyMBLimit where actionId = _action AND limitTypeId = 'MB_DAILY_LIMIT' AND value > _dailyMBLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE servicedefinitionactionlimit SET value = _weeklyMBLimit where actionId = _action AND limitTypeId = 'MB_WEEKLY_LIMIT' AND value > _weeklyMBLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE contractactionlimit SET value = _maxMBTxLimit where actionId = _action AND limitTypeId = 'MB_MAX_TRANSACTION_LIMIT' AND value > _maxMBTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE contractactionlimit SET value = _dailyMBLimit where actionId = _action AND limitTypeId = 'MB_DAILY_LIMIT' AND value > _dailyMBLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE contractactionlimit SET value = _weeklyMBLimit where actionId = _action AND limitTypeId = 'MB_WEEKLY_LIMIT' AND value > _weeklyMBLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE customeraction SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT' AND value > _maxMBTxLimit AND companyLegalUnit = _companyLegalUnit; 
  UPDATE customeraction SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'MB_DAILY_LIMIT' AND value > _dailyMBLimit AND companyLegalUnit = _companyLegalUnit;
  UPDATE customeraction SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'MB_WEEKLY_LIMIT' AND value > _weeklyMBLimit AND companyLegalUnit = _companyLegalUnit; 
  
  END$$

DELIMITER ;




-------------------------------------------------------------------
SP-2
-------------------------------------------------------------------
USE `dbxdb`;
DROP procedure IF EXISTS `hbl_feature_action_limits_update_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `hbl_feature_action_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _minTxLimit decimal(20,2),
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2),
  IN _minMBTxLimit decimal(20,2),
  IN _maxMBTxLimit decimal(20,2),
  IN _dailyMBLimit decimal(20,2),
  IN _weeklyMBLimit decimal(20,2)
)
BEGIN
  UPDATE actionlimit SET value = _minTxLimit where Action_id = _action AND LimitType_id = 'MIN_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT'AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
  UPDATE actionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
  UPDATE actionlimit SET value = _minMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MIN_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _maxMBTxLimit where Action_id = _action AND LimitType_id = 'MB_MAX_TRANSACTION_LIMIT'AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _dailyMBLimit where Action_id = _action AND LimitType_id = 'MB_DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
  UPDATE actionlimit SET value = _weeklyMBLimit where Action_id = _action AND LimitType_id = 'MB_WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
END$$

DELIMITER ;



------------------------------------------------------------------
SP3
--------------------------------------------------------------------
USE `dbxdb`;
DROP procedure IF EXISTS `hbl_user_limitgroup_limits_create_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `hbl_user_limitgroup_limits_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 100000000;

SET @singlePaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'SINGLE_PAYMENT');
                             
SET @bulkPaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'BULK_PAYMENT');

SET @max_per_transaction_single_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
SET @mb_max_per_transaction_single_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										 

SET @max_per_transaction_bulk_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
										 
SET @mb_max_per_transaction_bulk_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										 
                                         
SET @max_daily_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
										
SET @mb_max_daily_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										
                                         
SET @max_daily_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
										 
SET @mb_max_daily_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										 
                                         
SET @max_weekly_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
										 
SET @mb_max_weekly_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										 
                                         
SET @max_weekly_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @mb_max_weekly_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));										 

IF(@max_per_transaction_single_payment != 0) THEN 						
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment);
END IF;

IF(@mb_max_per_transaction_single_payment != 0) THEN 						
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'MB_MAX_TRANSACTION_LIMIT', @mb_max_per_transaction_single_payment);
END IF;

IF ( @max_per_transaction_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment);
END IF;

IF ( @mb_max_per_transaction_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'MB_MAX_TRANSACTION_LIMIT', @mb_max_per_transaction_bulk_payment);
END IF;

IF ( @max_daily_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment);
END IF;

IF ( @mb_max_daily_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'MB_DAILY_LIMIT', @mb_max_daily_limit_single_payment);
END IF;

IF ( @max_daily_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment);
END IF;

IF ( @mb_max_daily_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'MB_DAILY_LIMIT', @max_daily_limit_bulk_payment);
END IF;

IF ( @max_weekly_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment);
END IF;

IF ( @mb_max_weekly_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'MB_WEEKLY_LIMIT', @max_weekly_limit_single_payment);
END IF;

IF ( @max_weekly_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment);
END IF;

IF ( @mb_max_weekly_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'MB_WEEKLY_LIMIT', @mb_max_weekly_limit_bulk_payment);
END IF;



END$$

DELIMITER ;



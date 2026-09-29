DROP VIEW IF EXISTS `get_mc_approvalrequests_view`;
CREATE VIEW `get_mc_approvalrequests_view` AS
    SELECT 
        `mcconfig`.`action` AS `action`,
        COUNT(`ar`.`requestId`) AS `requestCount`,
        `ar`.`createdby` AS `createdby`,
        `ar`.`status` AS `status`,
        `ar`.`companyLegalUnit` AS `companyLegalUnit`,
        `mcconfig`.`approvalPermissionName` AS `approvalPermissionName`
    FROM
        (
          `approvalrequests` `ar` JOIN `makercheckerconfig` `mcconfig`
          ON (`ar`.`expAPIOperationName` = `mcconfig`.`expAPIOperationName` AND `ar`.`companyLegalUnit` = `mcconfig`.`companyLegalUnit`)
        )
    GROUP BY `mcconfig`.`action` , `ar`.`createdby` , `ar`.`status` , `ar`.`companyLegalUnit` , `mcconfig`.`approvalPermissionName`;
	
ALTER TABLE `approvalrequests` ADD COLUMN `action` varchar(50) AFTER `module`;

DROP TABLE IF EXISTS `mcactiontext`;
CREATE TABLE `mcactiontext` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `language_code` VARCHAR(10) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `language_code`));

DROP TABLE IF EXISTS `mcmoduletext`;
CREATE TABLE `mcmoduletext` (
  `id` VARCHAR(50) NOT NULL,
  `name` VARCHAR(50) NOT NULL,
  `language_code` VARCHAR(10) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`, `language_code`));

DROP PROCEDURE IF EXISTS `user_sba_securityattributes_get_proc`;
DELIMITER $$
CREATE PROCEDURE `user_sba_securityattributes_get_proc`(
in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DECLARE userAssociatedCoreCustomers VARCHAR(255);
SET @userAssociatedCoreCustomers =  (SELECT group_concat(distinct contractcustomers.coreCustomerId SEPARATOR ",") FROM contractcustomers WHERE contractcustomers.customerId = _userId and contractcustomers.companyLegalUnit = _legalEntityId);
SELECT customeraction.contractId,customeraction.coreCustomerId,
customeraction.Action_id,customeraction.featureId FROM customeraction
    WHERE customeraction.Customer_id =  _userId
        AND (customeraction.isAllowed = '1' OR customeraction.isAllowed = true)
        AND customeraction.companyLegalUnit = _legalEntityId
		AND FIND_IN_SET(customeraction.coreCustomerId,@userAssociatedCoreCustomers)
        AND (customeraction.Action_id LIKE 'SBA_%' OR customeraction.Action_id LIKE 'CASHFLOW_%');
    
SELECT backendidentifier.BackendId , backendidentifier.Customer_id,customer.sbaEnrolmentStatus from backendidentifier,customer where customer.id =backendidentifier.Customer_id AND BackendType = 'CORE' AND find_in_set(backendidentifier.BackendId,@userAssociatedCoreCustomers); 
END$$
DELIMITER ;
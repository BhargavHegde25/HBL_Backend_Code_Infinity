DROP PROCEDURE IF EXISTS `fetch_restrictive_featureactionlimits_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_restrictive_featureactionlimits_proc`(
	IN _locale varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _accessPolicyIdList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET @select_statement = '';
    SET @action_select_statement = '';
	SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id )");
	
	IF(_accessPolicyIdList != '') THEN 
		SET @action_select_statement = CONCAT(@action_select_statement , "WHERE FIND_IN_SET(featureaction.accessPolicyId ,",QUOTE(_accessPolicyIdList),")");
	END IF;
	
    SET @action_select_statement = CONCAT(@action_select_statement,")");
    
	IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT("(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
	IF(_userId != '') THEN 
		SET @select_statement = CONCAT("(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
		IF(_coreCustomerId != '') THEN 
			SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
        END IF;
        SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    SET @select_statement = "( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue";
    if(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
    END IF;
	IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
    END IF;  
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
    END IF;
    SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY' AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
       
    SET @select_statement = CONCAT(@select_statement , " UNION (SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue");
	IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
    END IF;
    IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
                                                                                                        
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)");
	END IF;
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Group_id = " , QUOTE(_roleId));
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.coreCustomerId = " , QUOTE(_coreCustomerId));
	END IF;
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
   PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    
END$$
DELIMITER ;

DROP procedure IF EXISTS `verify_user_proc`;

DELIMITER $$
CREATE  PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendIdentifiers varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @usersList = (select group_concat(backendidentifier.Customer_id SEPARATOR ",") from backendidentifier where BackendType=_backendType  
									 AND FIND_IN_SET(backendidentifier.BackendId ,_backendIdentifiers) );

(SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`,
        `customer`.`CustomerType_id` AS `CustomerType_id`
    FROM
        (`customer`
        LEFT JOIN `customercommunication` `primaryphone` ON ((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`Value` = _phone)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))
        LEFT JOIN `customercommunication` `primaryemail` ON ((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`Value` = _email)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')))
	where
		`customer`.`DateOfBirth` = _dateOfBirth
		and `primaryphone`.`Customer_id` = `primaryemail`.`Customer_id`) union SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`,
		`customer`.`CustomerType_id` AS `CustomerType_id`
    FROM `customer` where FIND_IN_SET(customer.id ,@usersList);
	
END$$

DELIMITER ;

CREATE TABLE `customrolesignatorygroup` (
  `customroleSignatoryGroupId` varchar(50) NOT NULL,
  `signatoryGroupId` varchar(50) DEFAULT NULL,
  `customRoleId` bigint(20) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`customroleSignatoryGroupId`),
  KEY `FK_customrolesignatorygroup_customerId` (`customroleId`),
  KEY `FK_customrolesignatorygroup_signaoryGroupId` (`signatoryGroupId`),
  CONSTRAINT `FK_customrolesignatorygroup_customroleId` FOREIGN KEY (`customroleId`) REFERENCES `customrole` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_customrolesignatorygroup_signaoryGroupId` FOREIGN KEY (`signatoryGroupId`) REFERENCES `signatorygroup` (`signatoryGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS `user_limitgroup_limits_create_proc`;

delimiter $$

CREATE PROCEDURE `user_limitgroup_limits_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));

SET @max_per_transaction_bulk_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         

IF(@max_per_transaction_single_payment != 0) THEN 						
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment);
END IF;

IF ( @max_per_transaction_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment);
END IF;

IF ( @max_daily_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment);
END IF;

IF ( @max_daily_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment);
END IF;

IF ( @max_weekly_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment);
END IF;

IF ( @max_weekly_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment);
END IF;



END$$

DELIMITER;

CREATE TABLE `eventtriggerconfiguration` (
  `id` int(11) NOT NULL,
  `service` varchar(255) DEFAULT NULL,
  `classname` varchar(45) DEFAULT NULL,
  `eventtype` varchar(45) DEFAULT NULL,
  `eventsubtype` varchar(45) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `servicecall` varchar(255) DEFAULT NULL,
  `hasfields` varchar(255) DEFAULT NULL,
  `conditions` varchar(255) DEFAULT NULL,
  `maskedfields` varchar(255) DEFAULT NULL,
  `excludedfields` varchar(255) DEFAULT NULL,
  `addFields` text,
  `passcustomerid` varchar(45) DEFAULT NULL,
  `accountLevelField` varchar(45) DEFAULT NULL,
  `appid` varchar(100) DEFAULT NULL,
  `isActive` tinyint(4) DEFAULT NULL,
  PRIMARY KEY (`id`)
);

DROP PROCEDURE IF EXISTS customer_basic_info_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_basic_info_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SELECT accountLockoutThreshold,accountLockoutTime into @accountLockoutThreshold,@accountLockoutTime from passwordlockoutsettings ;

SELECT 
        `customer`.`UserName` AS `Username`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        CONCAT(IFNULL(`customer`.`FirstName`, ''),
                ' ',
                IFNULL(`customer`.`MiddleName`, ''),
                ' ',
                IFNULL(`customer`.`LastName`, '')) AS `Name`,
        `customer`.`Salutation` AS `Salutation`,
        `customer`.`id` AS `Customer_id`,
        `customer`.`Ssn` AS `SSN`,
        `customer`.`createdts` AS `CustomerSince`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`isEnrolledFromSpotlight` AS `isEnrolledFromSpotlight`,
        IF(`customer`.`Status_id`= 'SID_CUS_SUSPENDED',`customer`.`Status_id`, 
        IF(`customer`.`lockCount`+1 >= @accountLockoutThreshold, 'SID_CUS_LOCKED',`customer`.`Status_id`)) AS `CustomerStatus_id`,
        `customerstatus`.`Description` AS `CustomerStatus_name`,
        `customer`.`MaritalStatus_id` AS `MaritalStatus_id`,
        `maritalstatus`.`Description` AS `MaritalStatus_name`,
        `customer`.`SpouseName` AS `SpouseName`,
        `customer`.`DrivingLicenseNumber` AS `DrivingLicenseNumber`,
        `customer`.`lockedOn` AS `lockedOn`,
        `customer`.`lockCount` AS `lockCount`,
        `customer`.`EmployementStatus_id` AS `EmployementStatus_id`,
        `employementstatus`.`Description` AS `EmployementStatus_name`,
        (SELECT 
                GROUP_CONCAT(`customerflagstatus`.`Status_id`, ' '
                        SEPARATOR ',')
            FROM
                `customerflagstatus`
            WHERE
                (`customerflagstatus`.`Customer_id` = `customer`.`id`)) AS `CustomerFlag_ids`,
        (SELECT 
                GROUP_CONCAT(`status`.`Description`, ' '
                        SEPARATOR ',')
            FROM
                `status`
            WHERE
                `status`.`id` IN (SELECT 
                        `customerflagstatus`.`Status_id`
                    FROM
                        `customerflagstatus`
                    WHERE
                        (`customerflagstatus`.`Customer_id` = `customer`.`id`))) AS `CustomerFlag`,
        `customer`.`IsEnrolledForOlb` AS `IsEnrolledForOlb`,
        `customer`.`isEnrolled` AS `isEnrolled`,
        `customer`.`IsStaffMember` AS `IsStaffMember`,
        `customer`.`Location_id` AS `Branch_id`,
        `location`.`Name` AS `Branch_name`,
        `location`.`Code` AS `Branch_code`,
        `customer`.`IsOlbAllowed` AS `IsOlbAllowed`,
        `customer`.`IsAssistConsented` AS `IsAssistConsented`,
        `customer`.`isEagreementSigned` AS `isEagreementSigned`,
	`customer`.`isCombinedUser` AS `isCombinedUser`,
	IFNULL(`customer`.`combinedUserId`, '') AS `combinedUserId`,	
        IF((`customer`.`isCombinedUser` = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            `customer`.`CustomerType_id`) AS `CustomerType_id`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail Banking,Business Banking',
            `customertype`.`Name`) AS `CustomerType_Name`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail and Business Banking User',
            `customertype`.`Description`) AS `CustomerType_Description`,        
        
		`customer`.`Organization_Id` AS `organisation_id`,
        `organisation`.`BusinessType_id` AS `BusinessType_id`,
        `businesstype`.`name` AS `BusinessType`,        
        `organisation`.`Name` AS `organisation_name`,
	(SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND  `customercommunication`.`isPrimary` = 1
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL'  AND  `customercommunication`.`isPrimary` = 1
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryEmailAddress`,                
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND `customercommunication`.`isTypeBusiness` = '1'
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `BusinessPrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL' AND `customercommunication`.`isTypeBusiness` = '1'
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `BusinessPrimaryEmailAddress`,
        `customer`.`DocumentsSubmitted` AS `DocumentsSubmitted`,
        `customer`.`ApplicantChannel` AS `ApplicantChannel`,
        `customer`.`Product` AS `Product`,
        `customer`.`Reason` AS `Reason`,
        @accountLockoutTime AS accountLockoutTime
    FROM
        (((((((`customer`
        LEFT JOIN `location` ON ((`customer`.`Location_id` = `location`.`id`)))
        LEFT JOIN `organisation` ON ((`customer`.`Organization_Id` = `organisation`.`id`)))
        LEFT JOIN `businesstype` ON ((`businesstype`.`id` = `organisation`.`BusinessType_id`)))
        JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
        LEFT JOIN `status` `customerstatus` ON ((`customer`.`Status_id` = `customerstatus`.`id`)))
        LEFT JOIN `status` `maritalstatus` ON ((`customer`.`MaritalStatus_id` = `maritalstatus`.`id`)))
        LEFT JOIN `status` `employementstatus` ON ((`customer`.`EmployementStatus_id` = `employementstatus`.`id`)))
    WHERE `customer`.`id` = _customerId
    LIMIT 1;

END$$
DELIMITER ;

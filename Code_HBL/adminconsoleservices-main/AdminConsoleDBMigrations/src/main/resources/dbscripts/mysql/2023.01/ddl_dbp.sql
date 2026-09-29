CREATE TABLE `customerlegalentity` (
  `id` VARCHAR(50) NOT NULL,
  `Customer_id` VARCHAR(50) NOT NULL,
  `Status_id` VARCHAR(50) NOT NULL,
  `legalEntityId` VARCHAR(50) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY (`Customer_id`,`legalEntityId`),
  KEY `IXFK_customerlegalentity_Customer` (`Customer_id`),
  CONSTRAINT `FK_customerlegalentity_Customer` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `customer` 
ADD COLUMN `homeLegalEntity` VARCHAR(50)  DEFAULT NULL AFTER `companyLegalUnit`,
ADD COLUMN `defaultLegalEntity` VARCHAR(50)  DEFAULT NULL AFTER `homeLegalEntity`;

DROP procedure IF EXISTS `update_userstatus_by_legalentity_proc`;

DELIMITER $$
CREATE PROCEDURE `update_userstatus_by_legalentity_proc`(
IN customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN statusId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN isOLB VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN legalEntityList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	IF isOLB = 'true' then
		UPDATE `customerlegalentity` SET `Status_id` = statusId WHERE `Customer_id` = customerId;
    ELSE
		UPDATE `customerlegalentity` SET `Status_id` = statusId WHERE `Customer_id` = customerId AND FIND_IN_SET(legalEntityId,legalEntityList);
		SELECT * from `customerlegalentity` WHERE `Customer_id` = customerId AND FIND_IN_SET(legalEntityId,legalEntityList);
    END IF;
END
$$

DELIMITER ;

DROP procedure IF EXISTS `customers_get_legalentities_proc`;


DELIMITER $$
CREATE PROCEDURE `customers_get_legalentities_proc`(
 IN _customerList LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
	 
	select `customerlegalentity`.`Customer_id` , `customerlegalentity`.`legalEntityId`  from `customerlegalentity` where FIND_IN_SET(Customer_id,_customerList);
END
$$

DELIMITER ;


DROP PROCEDURE IF EXISTS customer_basic_info_proc;
DELIMITER $$

CREATE PROCEDURE `customer_basic_info_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
	`customer`.`companyLegalUnit` AS `companyLegalUnit`,
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


DROP PROCEDURE IF EXISTS userId_Search_Proc;

DELIMITER $$

create procedure `userId_Search_Proc`(
in _userName varchar(50) character set
UTF8 collate utf8_general_ci
)
begin
	
(
select
	distinct
        `customerlegalentity`.`Customer_id` as `CustomerId`,
	`customerlegalentity`.`legalEntityId` as `legalEntityId`,
	`customer`.`Status_id` as `Status_id`,
	`customer`.`homeLegalEntity` as `homeLegalEntity`,
	`customer`.`defaultLegalEntity` as `defaultLegalEntity`,
	`customer`.`FirstName` as `FirstName`,
	`customer`.`MiddleName` as `MiddleName`,
	`customer`.`LastName` as `LastName`,
	`customer`.`UserName` as `UserName`,
	`customer`.`Gender` as `Gender`,
	`customer`.`CustomerType_id` as `CustomerType_id`,
	`customer`.`Ssn` as `Ssn`,
	`customer`.`DateOfBirth` as `DateOfBirth`,
	`customer`.`Ssn` as `Ssn`,
	`customer`.`isEnrolled` as `isEnrolled`,
	`primaryphone`.`Value` as `phnNo`,
	`primaryemail`.`Value` as `Email`
from
	(`customerlegalentity`
left join `customercommunication` `primaryphone` on
	((`primaryphone`.`Customer_id` = `customerlegalentity`.`Customer_id`)
		and (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE' )
			and `primaryphone`.`isPrimary` = '1')
left join `customercommunication` `primaryemail` on
	((`primaryemail`.`Customer_id` = `customerlegalentity`.`Customer_id`)
		and (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')
			and `primaryemail`.`isPrimary` = '1')
left join `customer` on
	(`customer`.`id` = `customerlegalentity`.`Customer_id`) )
where
	`customer`.`UserName` = _userName);
end$$
DELIMITER ;


DROP procedure IF EXISTS `customer_legalentities_get_proc`;


DELIMITER $$
CREATE PROCEDURE `customer_legalentities_get_proc`(
 IN customerid varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	 
	select 
	`cust`.`id`  as `customerid`,
	`cust`.`homeLegalEntity` as `homeLegalEntity` ,
	`custleg`.`legalEntityId` as `legalEntityId`,
	`custleg`.`Status_id`  as `statusId` 
	from `customer` `cust`
	left join `customerlegalentity` `custleg`  on (`cust`.`id` = `custleg`.`Customer_id`)
	where `cust`.`id` = customerid;
END
$$

DELIMITER ;

ALTER TABLE `customerimage`
ADD COLUMN `id` INT NOT NULL AUTO_INCREMENT FIRST,
ADD COLUMN `legalEntityId` VARCHAR(50) NOT NULL AFTER `Customer_id`,
DROP PRIMARY KEY,
ADD PRIMARY KEY (`id`) USING BTREE,
ADD UNIQUE INDEX `Customer_id` (`Customer_id`, `legalEntityId`);



DROP PROCEDURE IF EXISTS `update_customerstatus_default_legalentity`;

DELIMITER $$
CREATE PROCEDURE `update_customerstatus_default_legalentity`(
	IN customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	DECLARE FINISHED INTEGER DEFAULT 0;
    DECLARE statusId VARCHAR(255) DEFAULT "";
    DECLARE statuses CURSOR 
		FOR (select `Status_id` from `customerlegalentity` where Customer_id = customerId);
	DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET FINISHED = 1;
	SET @currentStatus = (select `Status_Id` from `customer` where  `customer`.`id` = customerId); 
    SET @activeStatus = 0;
    OPEN statuses;
    getStatus: LOOP
		FETCH statuses INTO statusId;
        IF FINISHED = 1 THEN
			LEAVE getStatus;
		ELSE
			IF statusId LIKE '%ACTIVE%' AND  @currentStatus LIKE '%ACTIVE%' THEN
				SET @activeStatus = 1;
				SET FINISHED = 1;
			END IF;
			IF statusId LIKE '%ACTIVE%' AND  @currentStatus NOT LIKE '%ACTIVE%' THEN
				SET @activeStatus = 1;
				UPDATE `customer` SET `Status_id` = 'SID_CUS_ACTIVE' 
				WHERE `customer`.`id` =  customerId;
			END IF;
		END IF;
	END LOOP getStatus;
    CLOSE statuses;
	IF @activeStatus = 0 THEN
		UPDATE `customer` SET `Status_id` = 'SID_CUS_SUSPENDED' 
		WHERE `customer`.`id` =  customerId
		AND @currentStatus NOT LIKE '%SID_CUS_NEW%';
	END IF;
END$$
DELIMITER ;



DROP PROCEDURE IF EXISTS `contract_users_details_get_proc`;

DELIMITER $$

CREATE PROCEDURE `contract_users_details_get_proc`(
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
SET @customers = (select group_concat(contractcustomers.customerId SEPARATOR ",") from contractcustomers where
contractcustomers.contractId = _contractId);
set @customers = (select group_concat(distinct customerId SEPARATOR ",") from contractcustomers where FIND_IN_SET(contractcustomers.customerId,@customers));
SELECT
`customer`.`id` AS `customerId`,
`customer`.`FirstName` AS `firstName`,
`customer`.`MiddleName` AS `middleName`,
`customer`.`LastName` AS `lastName`,
`customer`.`UserName` AS `userName`, 
`customer`.`Status_id` AS `statusId`,
`customer`.`DateOfBirth` AS `dateOfBirth`,
`customer`.`Ssn` AS `Ssn`,
`BI`.`BackendId` AS `primaryCoreCustomerId`,
`CC`.`Value` AS `Email`
FROM
(`customer`
LEFT JOIN `customercommunication` `CC` ON (`CC`.`Customer_id` = `customer`.`id` AND `CC`.`Type_id` = 'COMM_TYPE_EMAIL' AND FIND_IN_SET(`CC`.`Customer_id`,@customers))
LEFT JOIN `backendidentifier` `BI` ON (`BI`.`Customer_id` = `customer`.`id` AND `BI`.`BackendType` = _backendType and FIND_IN_SET(`BI`.`Customer_id`,@customers)))
WHERE
FIND_IN_SET(`customer`.`id`,@customers);
END$$
DELIMITER ;

DROP PROCEDURE if EXISTS `close_account_proc`;
DELIMITER $$

CREATE PROCEDURE `close_account_proc`(
	IN `accountId` varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
	IN `legalEntityId` varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
	IN `statusFlag` varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN	
	SET @customers = (SELECT GROUP_CONCAT(customeraccounts.Customer_id SEPARATOR ",") FROM customeraccounts WHERE 
	customeraccounts.Account_id=accountId AND customeraccounts.companyLegalUnit=legalEntityId);
	SET @customers = CONCAT(",",@customers,",");
	SELECT customeraccounts.Customer_id AS suspendedaccounts FROM customeraccounts  WHERE 
	INSTR(@customers,CONCAT(",",customeraccounts.Customer_id,","))>0 AND customeraccounts.companyLegalUnit = legalEntityId AND 
	customeraccounts.accountStatus!="CLOSED" GROUP BY customeraccounts.Customer_id having COUNT(*)=1 AND statusFlag="CLOSED";	
	UPDATE customer SET customer.Status_id="SID_CUS_SUSPENDED" WHERE customer.id IN 
	(SELECT customeraccounts.Customer_id FROM customeraccounts WHERE 
	INSTR(@customers,CONCAT(",",customeraccounts.Customer_id,","))>0 AND customeraccounts.companyLegalUnit = legalEntityId AND 
	customeraccounts.accountStatus!="CLOSED" GROUP BY customeraccounts.Customer_id having COUNT(*)=1) AND statusFlag="CLOSED";	
	UPDATE customeraccounts SET customeraccounts.accountStatus = statusFlag WHERE 
	customeraccounts.Account_id = accountId AND 	customeraccounts.companyLegalUnit = legalEntityId;	
END $$

DELIMITER ;


DROP procedure IF EXISTS `contract_users_details_get_proc`;

DELIMITER $$

CREATE PROCEDURE `contract_users_details_get_proc`(
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
SET @customers = (select group_concat(contractcustomers.customerId SEPARATOR ",") from contractcustomers where
contractcustomers.contractId = _contractId);
set @customers = (select group_concat(distinct customerId SEPARATOR ",") from contractcustomers where FIND_IN_SET(contractcustomers.customerId,@customers));
SELECT
`customer`.`id` AS `customerId`,
`customer`.`FirstName` AS `firstName`,
`customer`.`MiddleName` AS `middleName`,
`customer`.`LastName` AS `lastName`,
`customer`.`UserName` AS `userName`, 
`customer`.`Status_id` AS `statusId`,
`customer`.`DateOfBirth` AS `dateOfBirth`,
`customer`.`Ssn` AS `Ssn`,
`BI`.`BackendId` AS `primaryCoreCustomerId`,
`CC`.`Value` AS `Email`
FROM
(`customer`
LEFT JOIN `customercommunication` `CC` ON (`CC`.`Customer_id` = `customer`.`id` AND `CC`.`Type_id` = 'COMM_TYPE_EMAIL' AND FIND_IN_SET(`CC`.`Customer_id`,@customers))
LEFT JOIN `backendidentifier` `BI` ON (`BI`.`Customer_id` = `customer`.`id` AND `BI`.`BackendType` = _backendType AND `BI`.`companyLegalUnit` = `customer`.`companyLegalUnit` and FIND_IN_SET(`BI`.`Customer_id`,@customers)))
WHERE
FIND_IN_SET(`customer`.`id`,@customers);
END $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `useractions_create_proc`;

DELIMITER $$

CREATE  PROCEDURE `useractions_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,  
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT ""  ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE accountId varchar(255) DEFAULT "" ;
 DECLARE actualLimitId varchar(255) DEFAULT "" ;

DECLARE accounts CURSOR
         FOR (SELECT customeraccounts.Account_id FROM customeraccounts WHERE contractId = _contractId AND coreCustomerId = _coreCustomerId 
         AND Customer_id = _userId AND FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true ) and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE nonaccountlevelactions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '0' OR featureaction.isAccountLevel = false ) and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE limits CURSOR 
        FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
SET SESSION group_concat_max_len = 100000000;

IF(ISNULL(_accountsCSV) OR _accountsCSV='' ) THEN
SET _accountsCSV = (SELECT group_concat(distinct customeraccounts.Account_id SEPARATOR ",") FROM customeraccounts WHERE customeraccounts.Customer_id = _userId
              AND customeraccounts.contractId = _contractId AND  customeraccounts.coreCustomerId = _coreCustomerId );
END IF;
 

SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = _contractId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId);
IF(ISNULL(_groupId) OR _groupId='' ) THEN
SET _groupId = (SELECT Group_id FROM groupservicedefinition WHERE serviceDefinitionId = @serviceDefinitionId 
                       AND (isDefaultGroup = true OR isDefaultGroup = '1'));
END IF;
                       
SET @validFIActions = (SELECT group_concat(distinct id SEPARATOR ",") FROM featureaction);

SET @validServiceDefinitionActions = (SELECT group_concat(distinct actionId SEPARATOR ",") FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId= @serviceDefinitionId AND FIND_IN_SET(actionId,@validFIActions));

SET _groupId = (SELECT Group_id FROM groupservicedefinition WHERE serviceDefinitionId = @serviceDefinitionId 
                        AND  Group_id = _groupId );


SET @validGroupActions = (SELECT group_concat(distinct Action_id SEPARATOR ",") FROM groupactionlimit WHERE Group_id = _groupId 
                                AND FIND_IN_SET(Action_id,@validServiceDefinitionActions));
                                
SET @validActionsList = (SELECT group_concat(distinct actionId SEPARATOR ",") FROM contractactionlimit WHERE contractId = _contractId
                                AND coreCustomerId = _coreCustomerId AND FIND_IN_SET(actionId,@validGroupActions));
  
OPEN accounts; 
getAccount : LOOP
FETCH accounts INTO accountId;
IF finished = 1 THEN 
    LEAVE getAccount;
ELSE
  OPEN actions; 
  getAction: LOOP
        SET entryStatus = 0;
        FETCH actions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
      
        IF finished = 1 THEN 
            LEAVE getAction;
        else
            OPEN limits; 
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN 
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT distinct value FROM contractactionlimit WHERE actionId = featureActionId  COLLATE utf8_general_ci
               AND limitTypeId = limitId COLLATE utf8_general_ci 
               AND contractId = _contractId 
               AND coreCustomerId = _coreCustomerId
               AND companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
    
               if (limitId='MAX_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
               ELSEIF (limitId='MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,0.00,_legalEntityId);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,0.00,_legalEntityId);
               SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
               
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
               (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,@limitvalue,_legalEntityId);
                
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,limitId,@limitvalue,_legalEntityId);
                  
                SET entryStatus = 1;
               ITERATE  getlimit;
            END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
                   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,_legalEntityId);
            END IF;
            ITERATE  getAction;
        END IF;
  END LOOP getAction;
  CLOSE actions;
  SET finished = 0;
  ITERATE  getAccount;
 END IF;
END LOOP getAccount;
CLOSE accounts;

 
SET finished = 0;
OPEN nonaccountlevelactions; 
  getAction: LOOP
        FETCH nonaccountlevelactions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci 
       and companyLegalUnit COLLATE utf8_general_ci = _legalEntityId);
        IF finished = 1 THEN 
            LEAVE getAction;
        else
                   SET @id = (SELECT LEFT(UUID(), 50));
                   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,isAllowed,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,true,_legalEntityId);
        END IF;
        ITERATE  getAction;
END LOOP getAction;
CLOSE nonaccountlevelactions;
END$$
DELIMITER ;

DROP procedure IF EXISTS `update_default_legalentity_by_customerId_proc`;

DELIMITER $$

CREATE PROCEDURE `update_default_legalentity_by_customerId_proc`(
IN customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN legalEntityList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select customerId AS 'customerid';
select legalEntityList as 'legalentitylist';
UPDATE customer SET defaultLegalEntity = null WHERE `id` = customerId AND FIND_IN_SET(defaultLegalEntity,legalEntityList);
END $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `customer_search_proc`;
DELIMITER $$
CREATE  PROCEDURE `dbxdb`.`customer_search_proc`( 
in _searchType varchar(60),
in _id varchar(50), 
in _name varchar(50),  
in _SSN varchar(50),
in _username varchar(50), 
in _phone varchar(100), 
in _email varchar(100),
in _dateOfBirth varchar(100),
in _IsStaffMember varchar(10),
in _cardorAccountnumber varchar(50),
in _TIN varchar(50),
in _group varchar(40), 
in _IDType varchar(50),
in _IDValue varchar(50),
in _companyId varchar(50),
in _requestID varchar(50),
in _branchIDS varchar(2000), 
in _productIDS varchar(2000), 
in _cityIDS varchar(2000), 
in _entitlementIDS varchar(2000), 
in _groupIDS varchar(2000), 
in _customerStatus varchar(50), 
in _before varchar(20), 
in _after varchar(20), 
in _sortVariable varchar(100), 
in _sortDirection varchar(4),  
in _pageOffset bigint, 
in _pageSize bigint,
in _legalEntityId varchar(50))
BEGIN
	
    DECLARE _maxLockCount varchar(50);
	IF _searchType LIKE 'GROUP_SEARCH%' then
		set @search_select_statement = "SELECT 
					customer.id, customer.FirstName, customer.MiddleName, customer.LastName,
					concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
					customer.UserName as Username, customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember,
					IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`,
					customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,
					GROUP_CONCAT(customergroup.Group_id) as assigned_group_ids,
					address.City_id, city.Name As City_name,
					address.addressLine1 As addressLine1,
					address.addressLine2 As addressLine2,
					city.Name As city,
					address.zipCode As zipCode,
					country.Name As county,
					customer.isEnrolled as isEnrolled,
					customer.Location_id AS branch_id,
					location.Name AS branch_name,
				    customer.companyLegalUnit as legalEntityId ";
		set @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
		set @queryStatement = concat("
				FROM customer
                    JOIN (
						SELECT
							customer.id
						FROM
							customer ",
							IF( _groupIDS != '' OR _entitlementIDS != '', " LEFT JOIN customergroup ON (customergroup.Customer_id=customer.id)", ""),
							IF( _cityIDS != '', " LEFT JOIN customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
							LEFT JOIN address ON (customeraddress.Address_id = address.id)
							LEFT JOIN city ON (address.City_id = city.id)", ""),
							IF( _entitlementIDS != '', " LEFT JOIN customerentitlement ON (customerentitlement.Customer_id=customer.id) ", ""),
							IF( _productIDS != '', " LEFT JOIN customerproduct ON (customerproduct.Customer_id=customer.id) ", ""));
			set @whereclause = "WHERE true";
			
            IF _username != "" then
				
				set @whereclause = concat(@whereclause, " AND (customer.firstname like concat(",quote(_username),",'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.username like concat(",quote(_username)," ,'%')");
				
				
				set @whereclause = concat(@whereclause," OR customer.id like concat(", quote(_username) ,",'%')) ");
			end if;
            
            
			if _IsStaffMember != "" then
				if _IsStaffMember = "true" then
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '1'");
				else
					set @whereclause = concat(@whereclause," AND customer.IsStaffMember = '0'");
				end if;
			end if;
            
			
			if _entitlementIDS != "" then 
				set @whereclause = concat(@whereclause," AND (customerentitlement.Service_id in (",func_escape_input_for_in_operator(_entitlementIDS),") 
							OR customergroup.Group_id in ( select Group_id from groupentitlement where Service_id in (",func_escape_input_for_in_operator(_entitlementIDS)," )))");
			end if;
			
			
			if _groupIDS != "" then
				set @whereclause = concat(@whereclause," AND customergroup.Group_id in (",func_escape_input_for_in_operator(_groupIDS),") ");
			end if;
			
			
			if _productIDS != "" then
				set @whereclause = concat(@whereclause," AND customerproduct.Product_id in (",func_escape_input_for_in_operator(_productIDS),")");
			end if;
			
			
			if _branchIDS != "" then
				set @whereclause = concat(@whereclause, " AND customer.Location_id in (",func_escape_input_for_in_operator(_branchIDS),")");
			end if;
			
			
			if _customerStatus != "" then
				set @whereclause = concat(@whereclause, " AND customer.Status_id = ", quote(_customerStatus));
			end if;
			
			
			if _cityIDS != "" then
				set @whereclause = concat(@whereclause, " AND city.Name in (",func_escape_input_for_in_operator(_cityIDS),")");
			end if;
			
			
			if _before != "" and _after != "" then
				set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_before) ," and date(customer.createdts) <= date ", quote(_after));
			else 
				if _before != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) <= date ", quote(_before));
				elseif _after != "" then
					set @whereclause = concat(@whereclause, " AND date(customer.createdts) >= date ", quote(_after));
				end if;
			end if;
			
			set @queryStatement = concat(@queryStatement,@whereclause,
            ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
					
					LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
					LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
					LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
					LEFT JOIN address ON (customeraddress.Address_id = address.id)
					LEFT JOIN city ON (city.id = address.City_id)
					LEFT JOIN country ON (city.Country_id = country.id)
					LEFT JOIN location ON (location.id=customer.Location_id)  
			");
			
			IF _searchType = 'GROUP_SEARCH' THEN
                set @queryStatement2 = concat(@search_count_statement, @queryStatement);
				set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
				IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

			ELSEIF _searchType = 'GROUP_SEARCH_TOTAL_COUNT' then
				set @queryStatement = concat(@search_count_statement, @queryStatement);
			END IF;


	ELSEIF _searchType LIKE 'CUSTOMER_SEARCH%' then
			
			SELECT accountLockoutThreshold from passwordlockoutsettings where id='PLOCKID1' INTO _maxLockCount;
            
            SET @search_select_statement = concat("SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,
				concat(IFNULL(customer.FirstName,''), ' ', IFNULL(customer.MiddleName,''), ' ', IFNULL(customer.LastName,'')) as name,
				customer.UserName as Username,customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight,customer.isCombinedUser as isCombinedUser,IFNULL(customer.combinedUserId, '') as combinedUserId, customer.Salutation, customer.Gender,CONCAT('****', RIGHT(customer.Ssn, 4)) as Ssn,
                IF((`customer`.`isCombinedUser` = '1'),'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',`customer`.`CustomerType_id`) AS `CustomerTypeId`, 
                company.id as CompanyId, company.Name as CompanyName,
                organisationemployees.isAuthSignatory as isAuthSignatory, 
                IF(IFNULL(customer.lockCount,0) >= ",_maxLockCount,", 'SID_CUS_LOCKED',customer.Status_id) as Status_id,
				PrimaryPhone.value AS PrimaryPhoneNumber,
				PrimaryEmail.value AS PrimaryEmailAddress,
                GROUP_CONCAT(membergroup.Name) as `groups`,
				address.addressLine1 As addressLine1,
				address.addressLine2 As addressLine2,
				city.Name As city,
				address.zipCode As zipCode,
				country.Name As county,
				customer.isEnrolled as isEnrolled,
                customer.ApplicantChannel, customer.createdts,
				customerlegalentity.legalEntityId as companyLegalUnit ,
				customerlegalentity.legalEntityId as branchId ");
			
            SET @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
			SET @queryStatement = concat("
			FROM customer
			JOIN (
				SELECT
					c.id,cle.legalEntityId
				FROM customer c
				JOIN customerlegalentity cle ON (cle.Customer_id=c.id )",
					IF( _phone != '', " JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=cle.Customer_id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE') ",""),
					IF(_email != '', "  JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=cle.Customer_id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL') ",""),
                    IF( _TIN != '', " LEFT JOIN organisationmembership ON (c.Organization_id = organisationmembership.Organization_id)", ""),
                    IF( _cardorAccountnumber != '', " LEFT JOIN card ON (c.id = card.User_id)
                    LEFT JOIN accounts ON (c.id = accounts.User_id)
                    LEFT JOIN customeraccounts ON (c.id = customeraccounts.Customer_id)", ""));

                SET @queryStatement = concat(@queryStatement," WHERE cle.legalEntityId = ", quote(_legalEntityId));
                
                IF _id != '' THEN
					set @queryStatement = concat(@queryStatement," and c.id = ", quote(_id));
                end if;
                
                 IF _name != '' THEN
					set @queryStatement = concat(@queryStatement," and c.LastName like concat(", quote(_name),",'%')");
                end if;

                IF _SSN != '' THEN
					set @queryStatement = concat(@queryStatement," and c.Ssn = ",quote(_SSN));
                end if;

                IF _username != '' THEN
					set @queryStatement = concat(@queryStatement," and c.username = ",quote(_username));
                end if;
                
                 IF _dateOfBirth != '' THEN
					set @queryStatement = concat(@queryStatement," and c.DateOfBirth = ",quote(_dateOfBirth));
                end if;
                
                IF _phone != '' THEN
					IF length(_phone) > 9 THEN
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value like concat('%',",quote(_phone),",'%')");
                    ELSE
						set @queryStatement = concat(@queryStatement," and PrimaryPhone.value = ",quote(_phone));
					end if;
                end if;

                IF _email != '' THEN
					set @queryStatement = concat(@queryStatement," and PrimaryEmail.value = ",quote(_email));
                end if;

                IF _companyId != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Organization_id = ",quote(_companyId));
                end if;

				IF _IDValue != '' THEN
                    IF _IDType = 'ID_DRIVING_LICENSE' THEN
						set @queryStatement = concat(@queryStatement," and (customer.DrivingLicenseNumber = ",quote(_IDValue)," or 
                        (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),"))");
                    ELSE
						set @queryStatement = concat(@queryStatement," and (customer.IDType_id = ",quote(_IDType)," and customer.IDValue = ",quote(_IDValue),")");
					end if;
                end if;
                
				IF _TIN != '' THEN
					set @queryStatement = concat(@queryStatement," and organisationmembership.Taxid = ",quote(_TIN));
                end if;
                
                IF _cardorAccountnumber != '' THEN
					set @queryStatement = concat(@queryStatement," and (card.cardNumber = ",quote(_cardorAccountnumber),
                    " or accounts.Account_id = ",quote(_cardorAccountnumber)," or customeraccounts.Account_id = ",quote(_cardorAccountnumber),")");
                end if;
                
                set @queryStatement = concat(@queryStatement,
                ") paginatedCustomers ON (paginatedCustomers.id=customer.id)
			LEFT JOIN customerlegalentity ON (paginatedCustomers.id = customerlegalentity.Customer_id and paginatedCustomers.legalEntityId = customerlegalentity.legalEntityId)
			LEFT JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE' AND PrimaryPhone.companyLegalUnit  = paginatedCustomers.legalEntityId)
			LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL' AND PrimaryEmail.companyLegalUnit  = paginatedCustomers.legalEntityId)
			LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME' AND customeraddress.companyLegalUnit  = paginatedCustomers.legalEntityId )
			LEFT JOIN address ON (customeraddress.Address_id = address.id)
			LEFT JOIN city ON (city.id = address.City_id)
			LEFT JOIN country ON (city.Country_id = country.id)
			LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id AND customergroup.companyLegalUnit  = paginatedCustomers.legalEntityId)
			LEFT JOIN membergroup ON (membergroup.id=customergroup.group_id)
            LEFT JOIN organisation company ON (customer.Organization_id = company.id)
            LEFT JOIN organisationemployees ON (organisationemployees.Organization_id = company.id)");

            IF _searchType = 'CUSTOMER_SEARCH' THEN
				set @queryStatement2 = concat(@search_count_statement, @queryStatement);
            	set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ");
           
            IF _sortVariable = "DEFAULT" OR _sortVariable = "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY FirstName");
	            ELSEIF _sortVariable != "" THEN
					set @queryStatement = concat(@queryStatement, " ORDER BY ",_sortVariable);
	            end if;
	            IF _sortDirection != "" THEN
					set @queryStatement = concat(@queryStatement, " ",_sortDirection);
	            end if;
	            set @queryStatement = concat(@queryStatement, " LIMIT ",_pageOffset,",",_pageSize);

            ELSEIF _searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT' THEN
            	set @queryStatement = concat(@search_count_statement, @queryStatement);
            END IF;
	END IF;
	PREPARE stmt FROM @queryStatement; EXECUTE stmt; 
     IF _searchType = 'CUSTOMER_SEARCH' OR _searchType = 'GROUP_SEARCH'then
          PREPARE stmt FROM @queryStatement2; EXECUTE stmt;
     END If;
    DEALLOCATE PREPARE stmt;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS customer_basic_info_proc;
DELIMITER $$
CREATE  PROCEDURE `dbxdb`.`customer_basic_info_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
	`customer`.`companyLegalUnit` AS `companyLegalUnit`,
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
               `customercommunication`.companyLegalUnit = _legalEntityId AND  `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND  `customercommunication`.`isPrimary` = 1
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                `customercommunication`.companyLegalUnit = _legalEntityId and `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL'  AND  `customercommunication`.`isPrimary` = 1
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `PrimaryEmailAddress`,                
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                 `customercommunication`.companyLegalUnit = _legalEntityId and `customercommunication`.`Type_id` = 'COMM_TYPE_PHONE' AND `customercommunication`.`isTypeBusiness` = '1'
                AND `customercommunication`.`Customer_id` = `customer`.`id`) AS `BusinessPrimaryPhoneNumber`,
        (SELECT `customercommunication`.`Value` FROM
                `customercommunication`
            WHERE
                 `customercommunication`.companyLegalUnit = _legalEntityId and `customercommunication`.`Type_id` = 'COMM_TYPE_EMAIL' AND `customercommunication`.`isTypeBusiness` = '1'
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


DROP PROCEDURE IF EXISTS `fetch_restrictive_featureactionlimits_legalEntityId_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_restrictive_featureactionlimits_legalEntityId_proc`(
IN _locale varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accessPolicyIdList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci

)
BEGIN

SET @select_statement = '';
SET @action_select_statement = '';
SET @action_select_statement_output = '';
SET @FeatureActionList  = '';

SET @FeatureActionList = N'';
SET @select_statement = '';
SET @action_select_statement = '';



SET @select_statement = 'SELECT featureaction.id FROM featureaction WHERE featureaction.status = ''SID_ACTION_ACTIVE''';
IF(_accessPolicyIdList != '') THEN
SET @select_statement = CONCAT(@select_statement , " AND featureaction.accessPolicyId IN (select DISTINCT value from STRING_SPLIT(", _accessPolicyIdList , "))") ;
END IF;

IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND EXISTS
(
SELECT ''X'' FROM servicedefinitionactionlimit
WHERE servicedefinitionactionlimit.serviceDefinitionId = ''' , _serviceDefinitionId , ''' AND
servicedefinitionactionlimit.companyLegalUnit  =''' ,_legalEntityId,''' AND
featureaction.id = servicedefinitionactionlimit.actionId
) ');
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND EXISTS
(
SELECT ''X'' FROM groupactionlimit
WHERE groupactionlimit.Group_id = ''' , _roleId , ''' AND
groupactionlimit.companyLegalUnit = ''',_legalEntityId, '''  AND
featureaction.id = groupactionlimit.Action_id
) ');
END IF;

IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND EXISTS
(
SELECT ''X'' FROM contractactionlimit
WHERE contractactionlimit.coreCustomerId = ''' , _coreCustomerId , ''' AND
contractactionlimit.companyLegalUnit = ''',_legalEntityId,''' AND
featureaction.id = contractactionlimit.actionId
) ');
END IF;

IF(_userId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND EXISTS
(
SELECT ''X'' FROM customeraction
WHERE customeraction.isAllowed = 1 AND
customeraction.companyLegalUnit = ''',_legalEntityId,''' AND
featureaction.id = customeraction.Action_id AND
customeraction.Customer_id = ''' , _userId , ''')');
END IF;

/*IF (_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND (customeraction.coreCustomerId = ''' , _coreCustomerId , ''')');
END IF;
*/

SET @action_select_statement = @select_statement;



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
featureaction.companyLegalUnit as legalEntityId,
featureaction.actionlevelId as actionLevelId,
IF(featureaction.Type_id = 'NON_MONETARY',null,actionlimit.LimitType_id) as limitTypeId,
IF(featureaction.Type_id = 'NON_MONETARY',null,actionlimit.value) as fiLimitValue";
 
IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , ', IF(featureaction.Type_id = ''NON_MONETARY'',null,servicedefinitionactionlimit.value) AS serviceLimitValue');
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , ', IF(featureaction.Type_id = ''NON_MONETARY'',null,groupactionlimit.value) AS groupLimitValue');
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ', IF(featureaction.Type_id = ''NON_MONETARY'',null,contractactionlimit.value) AS coreCustomerLimitValue');
END IF;

IF _roleId != '' AND _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ', IF(groupactionlimit.isNewAction = ''1'' OR contractactionlimit.isNewAction = ''1'', ''1'', ''0'') as isNewAction');
ELSE
IF _roleId != '' THEN
SET @select_statement = CONCAT(@select_statement , ', groupactionlimit.isNewAction AS isNewAction');
ELSEIF _coreCustomerId != '' THEN
SET @select_statement = CONCAT(@select_statement , ', contractactionlimit.isNewAction AS isNewAction');
END IF;
END IF;


SET @select_statement = CONCAT(@select_statement , ' FROM feature
LEFT JOIN featuredisplaynamedescription ON (featuredisplaynamedescription.companyLegalUnit = feature.companyLegalUnit AND featuredisplaynamedescription.Feature_id = feature.id AND featuredisplaynamedescription.Locale_id = ', '''', _locale, '''', ')
LEFT JOIN featureaction ON (featureaction.companyLegalUnit = feature.companyLegalUnit AND featureaction.Feature_id = feature.id)
LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.companyLegalUnit = featureaction.companyLegalUnit AND actiondisplaynamedescription.Action_id = featureaction.id AND actiondisplaynamedescription.Locale_id = ', '''', _locale, '''' , ')');

IF(_serviceDefinitionId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN servicedefinitionactionlimit ON (servicedefinitionactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND servicedefinitionactionlimit.actionId = featureaction.id AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', _serviceDefinitionId , '''', ')');
END IF;
IF(_roleId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN groupactionlimit ON (groupactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND groupactionlimit.Action_id = featureaction.id AND groupactionlimit.Group_id = ' , '''', _roleId, '''',')');
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN contractactionlimit ON (contractactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND contractactionlimit.actionId = featureaction.id AND contractactionlimit.coreCustomerId = ', '''', _coreCustomerId, '''', ')');
END IF;

SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN actionlimit ON (actionlimit.companyLegalUnit = featureaction.companyLegalUnit AND actionlimit.Action_id = featureaction.id)');


SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.companyLegalUnit = ''' , _legalEntityId ,'''',' AND featureaction.id IN (', @action_select_statement ,')');

 IF(_serviceDefinitionId != '') THEN
 SET @select_statement = CONCAT(@select_statement , ' AND ((featureaction.Type_id = ''MONETARY'' AND servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', _serviceDefinitionId , ''') OR (featureaction.Type_id = ''NON_MONETARY''))');
 END IF;
IF(_roleId != '') THEN
  SET @select_statement = CONCAT(@select_statement , ' AND ((featureaction.Type_id = ''MONETARY'' AND groupactionlimit.LimitType_id = actionlimit.LimitType_id AND groupactionlimit.Group_id = ' , '''', _roleId, ''') OR (featureaction.Type_id = ''NON_MONETARY''))');
END IF;
IF(_coreCustomerId != '') THEN
SET @select_statement = CONCAT(@select_statement , ' AND ((featureaction.Type_id = ''MONETARY'' AND contractactionlimit.limitTypeId = actionlimit.LimitType_id AND contractactionlimit.coreCustomerId = ' , '''', _coreCustomerId,''') OR (featureaction.Type_id = ''NON_MONETARY''))');
END IF;


SET @select_statement = CONCAT(@select_statement , ')');

PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$
DELIMITER ;



DROP procedure IF EXISTS `contract_features_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_features_create_proc`(
IN _features MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _defaultActionsEnabled VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE finished INTEGER DEFAULT 0;
DECLARE featureId varchar(255) DEFAULT "";
DECLARE featuresList TEXT DEFAULT "";
DECLARE featureActionId varchar(255) DEFAULT "" ;
DECLARE entryStatus INTEGER DEFAULT 0 ;
DECLARE limitId varchar(255) DEFAULT "";
DECLARE tempLimitValue varchar(255) DEFAULT "";

DECLARE features CURSOR
        FOR (select id from feature where FIND_IN_SET(id  ,@features_list  ) and companyLegalUnit   = _legalEntityId );
DECLARE actions CURSOR
        FOR (select id from featureaction where FIND_IN_SET(featureaction.id ,@validServicedefinitionActions ) and companyLegalUnit   = _legalEntityId);
DECLARE limits CURSOR
        FOR (select LimitType_id from actionlimit where actionlimit.Action_id  = featureActionId );
DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id and featureroletype.RoleType_id = _serviceTypeId and feature.companyLegalUnit = featureroletype.companyLegalUnit )
                      where (FIND_IN_SET(feature.id  ,_features ) and feature.companyLegalUnit  = _legalEntityId)  
					 ) ;
                    
SET @features_list = IF(@features_list is null, '', @features_list);


OPEN features;
getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN
            LEAVE getFeature;
        else
            SET @id = (SELECT LEFT(UUID(), 50));
            INSERT INTO contractfeatures(id,contractId,coreCustomerId,featureId,companyLegalUnit) VALUES
            (@id,_contractId,_customerId,featureId,_legalEntityId);
            set featuresList = CONCAT(featureId,",",featuresList);
            ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;
SET finished = 0;
SET @validFIActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE
                        FIND_IN_SET(featureaction.Feature_id ,featuresList ) AND
                        featureaction.status = 'SID_ACTION_ACTIVE');

SET @servicedefinitionId = (SELECT servicedefinitionId from contract WHERE id  = _contractId and companyLegalUnit = _legalEntityId);

SET @validServicedefinitionActions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE
                                    servicedefinitionactionlimit.serviceDefinitionId  = @servicedefinitionId AND
                                    FIND_IN_SET(servicedefinitionactionlimit.actionId ,@validFIActions));

If !ISNULL(_defaultActionsEnabled) AND _defaultActionsEnabled = 'true' THEN

OPEN actions;
getAction: LOOP
		
        SET entryStatus = 0;
        FETCH actions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id  = featureActionId and companyLegalUnit  = _legalEntityId);
        IF finished = 1 THEN
            LEAVE getAction;
        else
            OPEN limits;
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id  = featureActionId
               AND LimitType_id  = limitId and companyLegalUnit  = _legalEntityId);
               
               SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE
                                                  servicedefinitionactionlimit.actionId  = featureActionId AND
                                                  servicedefinitionactionlimit.limitTypeId  = limitId AND
                                                   servicedefinitionactionlimit.serviceDefinitionId  = @servicedefinitionId );
              SET tempLimitValue = LEAST(@limitvalue,@limitATServiceDefinition);
              if(!isnull(tempLimitValue) AND tempLimitValue !='') THEN
                 SET @limitvalue = tempLimitValue;
              END IF;
                
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId,limitTypeId,value,companyLegalUnit) VALUES
                (@id,_contractId,_customerId,@featureId,featureActionId,limitId,@limitvalue,_legalEntityId);
                
                SET entryStatus = 1;
               ITERATE  getlimit;
            END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId,companyLegalUnit) VALUES
                (@id,_contractId,_customerId,@featureId,featureActionId,_legalEntityId);
            END IF;
            ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;
END IF;

END$$
DELIMITER ;



DELIMITER ;

DROP procedure IF EXISTS `customeraccounts_delete_proc`;


DELIMITER $$



CREATE  PROCEDURE `customeraccounts_delete_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
DELETE FROM `customeraccounts` 
	WHERE  Customer_id  = _customerId AND 
	coreCustomerId  = _coreCustomerId AND
	contractId  = _contractId AND
	companyLegalUnit  = _legalEntityId AND 
    FIND_IN_SET(Account_id,_accountIdList);
END
$$

DELIMITER ;

DROP procedure IF EXISTS `excludedcustomeraccounts_delete_proc`;


DELIMITER $$

CREATE  PROCEDURE `excludedcustomeraccounts_delete_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
DELETE FROM `excludedcustomeraccounts` 
	WHERE  Customer_id  = _customerId AND 
	coreCustomerId  = _coreCustomerId AND
	contractId  = _contractId AND
	companyLegalUnit  = _legalEntityId AND 
    FIND_IN_SET(Account_id,_accountIdList);
END
$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customer_contract_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_contract_delete_proc`(in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
       
       SET @contract_statement = 'DELETE FROM contractcustomers where';
       SET @suspended_statement = 'DELETE FROM suspendedcustomers where';
       SET @group_statement = 'DELETE FROM customergroup where ';
       SET @action_statement = 'DELETE FROM customeraction where ';
       SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
       SET @excluded_action_statement = 'DELETE FROM excludedcustomeraction where ';
       
	   SET @shouldAndAppend = false;
       SET @where_clause = CONCAT(' `companyLegalUnit` = ', quote(legalEntityId), ' AND ');
       SET @where_clause1 = CONCAT(' `companyLegalUnit` = ', quote(legalEntityId), ' AND ');
       if(customerId != '') THEN
              SET @where_clause = CONCAT(@where_clause , '`customerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(customerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`Customer_id` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(customerId));
              SET @shouldAndAppend = true;
       END IF;
       
       IF(contractId != '') THEN
              IF(@shouldAndAppend = true) THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(contractId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
              SET @shouldAndAppend = true;
       END IF;
       
       IF(coreCustomerId != '') THEN
              IF(@shouldAndAppend = true) THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
       END IF;
       
    
              SET @contract_statement = CONCAT(@contract_statement , @where_clause);
              SET @suspended_statement = CONCAT(@suspended_statement , @where_clause);
              SET @group_statement = CONCAT(@group_statement , @where_clause1); 
              SET @action_statement = CONCAT(@action_statement , @where_clause1);     
              SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);    
              SET @excluded_action_statement = CONCAT(@excluded_action_statement , @where_clause1);
                     
       
              PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @suspended_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$
DELIMITER ;


DROP procedure IF EXISTS `contract_features_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_features_create_proc`(
IN _features MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _defaultActionsEnabled VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE finished INTEGER DEFAULT 0;
DECLARE featureId varchar(255) DEFAULT "";
DECLARE featuresList TEXT DEFAULT "";
DECLARE featureActionId varchar(255) DEFAULT "" ;
DECLARE entryStatus INTEGER DEFAULT 0 ;
DECLARE limitId varchar(255) DEFAULT "";
DECLARE tempLimitValue varchar(255) DEFAULT "";

DECLARE features CURSOR
        FOR (select id from feature where FIND_IN_SET(id COLLATE utf8_general_ci ,@features_list COLLATE utf8_general_ci ) and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId );
DECLARE actions CURSOR
        FOR (select id from featureaction where FIND_IN_SET(featureaction.id COLLATE utf8_general_ci,@validServicedefinitionActions COLLATE utf8_general_ci) and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE limits CURSOR
        FOR (select LimitType_id from actionlimit where actionlimit.Action_id COLLATE utf8_general_ci = featureActionId );
DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id and featureroletype.RoleType_id = _serviceTypeId and feature.companyLegalUnit = featureroletype.companyLegalUnit )
                      where (FIND_IN_SET(feature.id COLLATE utf8_general_ci ,_features COLLATE utf8_general_ci) and feature.companyLegalUnit COLLATE utf8_general_ci = _legalEntityId)  
					 ) ;
                    
SET @features_list = IF(@features_list is null, '', @features_list);


OPEN features;
getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN
            LEAVE getFeature;
        else
            SET @id = (SELECT LEFT(UUID(), 50));
            INSERT INTO contractfeatures(id,contractId,coreCustomerId,featureId,companyLegalUnit) VALUES
            (@id,_contractId,_customerId,featureId,_legalEntityId);
            set featuresList = CONCAT(featureId,",",featuresList);
            ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;
SET finished = 0;
SET @validFIActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE
                        FIND_IN_SET(featureaction.Feature_id COLLATE utf8_general_ci,featuresList COLLATE utf8_general_ci) AND
                        featureaction.status = 'SID_ACTION_ACTIVE');

SET @servicedefinitionId = (SELECT servicedefinitionId from contract WHERE id  = _contractId and companyLegalUnit = _legalEntityId);

SET @validServicedefinitionActions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE
                                    servicedefinitionactionlimit.serviceDefinitionId COLLATE utf8_general_ci = @servicedefinitionId AND
                                    FIND_IN_SET(servicedefinitionactionlimit.actionId COLLATE utf8_general_ci,@validFIActions));

If !ISNULL(_defaultActionsEnabled) AND _defaultActionsEnabled = 'true' THEN

OPEN actions;
getAction: LOOP
		
        SET entryStatus = 0;
        FETCH actions INTO featureActionId;
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id COLLATE utf8_general_ci = featureActionId and companyLegalUnit COLLATE utf8_general_ci = _legalEntityId);
        IF finished = 1 THEN
            LEAVE getAction;
        else
            OPEN limits;
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id COLLATE utf8_general_ci = featureActionId
               AND LimitType_id COLLATE utf8_general_ci = limitId and companyLegalUnit COLLATE utf8_general_ci = _legalEntityId);
               
               SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE
                                                  servicedefinitionactionlimit.actionId COLLATE utf8_general_ci = featureActionId AND
                                                  servicedefinitionactionlimit.limitTypeId COLLATE utf8_general_ci = limitId AND
                                                   servicedefinitionactionlimit.serviceDefinitionId COLLATE utf8_general_ci = @servicedefinitionId );
              SET tempLimitValue = LEAST(@limitvalue,@limitATServiceDefinition);
              if(!isnull(tempLimitValue) AND tempLimitValue !='') THEN
                 SET @limitvalue = tempLimitValue;
              END IF;
                
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId,limitTypeId,value,companyLegalUnit) VALUES
                (@id,_contractId,_customerId,@featureId,featureActionId,limitId,@limitvalue,_legalEntityId);
                
                SET entryStatus = 1;
               ITERATE  getlimit;
            END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,contractactionlimit.featureId,actionId,companyLegalUnit) VALUES
                (@id,_contractId,_customerId,@featureId,featureActionId,_legalEntityId);
            END IF;
            ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;
END IF;

END$$
DELIMITER ;


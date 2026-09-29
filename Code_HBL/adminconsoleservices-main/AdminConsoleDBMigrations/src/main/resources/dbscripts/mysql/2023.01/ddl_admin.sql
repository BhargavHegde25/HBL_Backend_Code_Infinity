ALTER TABLE `customeraccounts` ADD COLUMN `accountStatus` VARCHAR(50) DEFAULT 'ACTIVE';
ALTER TABLE `customeraccounts` ADD COLUMN `isSweepCreated` BIT(1);

DROP procedure IF EXISTS `corecustomeraccounts_details_get_proc`;

DELIMITER $$
CREATE PROCEDURE `corecustomeraccounts_details_get_proc`(
IN _coreCustomerIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @corecustomersList = (SELECT group_concat(contractcustomers.coreCustomerId SEPARATOR ',') from contractcustomers WHERE contractcustomers.customerId = _customerId AND FIND_IN_SET(contractcustomers.coreCustomerId,_coreCustomerIdList));

	SET @selectstatement = concat("SELECT 
           `contractcorecustomers`.`coreCustomerId` AS coreCustomerId , 
           `contractcorecustomers`.`coreCustomerName` AS coreCustomerName ,
           `customeraccounts`.`email` AS email ,
           `customeraccounts`.`Customer_id` AS customerId ,
           `customeraccounts`.`FavouriteStatus` AS favouriteStatus ,
		   `customeraccounts`.`NickName` AS nickName ,
		   `customeraccounts`.`accountStatus` AS accountStatus ,
           IF ((`customeraccounts`.`EStatementmentEnable`) = '1' ,'true' , 'false') AS `eStatementEnable`,
		   IF ((`customeraccounts`.`isSweepCreated`) = '1' ,'true' , 'false') AS `isSweepCreated`,
           IF ((`contractcorecustomers`.`isBusiness`) = '1' ,'true' , 'false') AS `isBusinessAccount`,
           `contractaccounts`.`accountId` AS accountId
           from (`contractcorecustomers`) 
		   JOIN (`contractaccounts`) ON (`contractcorecustomers`.`coreCustomerId` = `contractaccounts`.`coreCustomerId`)
           JOIN (`customeraccounts`) ON (`customeraccounts`.`Account_id` = `contractaccounts`.`accountId`)
           where FIND_IN_SET(`contractcorecustomers`.`coreCustomerId`, '",@corecustomersList,"') AND `customeraccounts`.`Customer_id` = '",_customerId,"'");
           
           PREPARE stmt FROM @selectstatement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$

DELIMITER ;

CREATE TABLE `accountsweeps` (
`id` INT NOT NULL AUTO_INCREMENT,
`primaryAccountName` VARCHAR(50) NULL,
`primaryAccountNumber` VARCHAR(50) NOT NULL ,
`secondaryAccountName` VARCHAR(50) NULL,
`secondaryAccountNumber` VARCHAR(50) NULL,
`belowSweepAmount` VARCHAR(50) NULL,
`aboveSweepAmount` VARCHAR(50) NULL,
`currencyCode` VARCHAR(50) NULL,
`frequency` VARCHAR(50) NULL,
`startDate` VARCHAR(50) NULL,
`endDate` VARCHAR(50) NULL,
`serviceRequestId` VARCHAR(45) NULL,
`softDelete` BIT(1) NULL DEFAULT b'0',
UNIQUE INDEX `id_UNIQUE` (`id` ASC),
PRIMARY KEY (`primaryAccountNumber`));

ALTER TABLE `customer` ADD COLUMN `isQRPaymentActivated` BIT(1);
ALTER TABLE `customerpreference` ADD COLUMN `DefaultFromAccountQR` VARCHAR(50) NULL;

create or replace
algorithm = UNDEFINED view `feature_view` as
select
    `feature`.`id` as `Code`,
    group_concat(`membergrouptype`.`description` separator ',') as `Type`,
    group_concat(distinct `membergrouptype`.`id` separator ',') as `Type_Id`,
    `feature`.`name` as `Name`,
    `status`.`Description` as `Status`,
    `status`.`id` as `Status_Id`
from
    (((`feature`
left join `featureroletype` `fr` on
    ((`fr`.`Feature_id` = `feature`.`id`)))
left join `membergrouptype` on
    ((`membergrouptype`.`id` = `fr`.`RoleType_id`)))
left join `status` on
    ((`feature`.`Status_id` = `status`.`id`)))
group by
    `feature`.`id`;


DROP procedure IF EXISTS `systemroles_permission_proc`;
DELIMITER $$

CREATE PROCEDURE `systemroles_permission_proc`(
IN _roleIds varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 SELECT distinct
        `p`.`id` AS `id`,
        `p`.`Name` AS `name`,
		`rp`.`companyLegalUnit`  AS `companyLegalUnit`,
        `p`.`Status_id` AS `status`,
        `p`.`PermissionValue` AS `PermissionValue`,
        `p`.`isComposite` AS `isComposite`,
        `rp`.`Role_id` AS `Role_id`,
		 CAST(`p`.`softdeleteflag` AS unsigned) AS `softdeleteflag`
    FROM
    (`rolepermission` `rp` 
    JOIN `permission` `p` ON (`p`.`id` = `rp`.`Permission_id`)
    JOIN `role` `r` ON (`r`.`id` = `rp`.`Role_id` and `r`.`companyLegalUnit` = `rp`.`companyLegalUnit`))
    WHERE `p`.`Status_id`='SID_ACTIVE' AND `r`.`Status_id`='SID_ACTIVE' AND FIND_IN_SET(`rp`.`Role_id`,_roleIds) ORDER BY `id`;

END $$

DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `customeraccounts_sweepinfoupdate_proc`(
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _primaryAccount VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _secondaryAccount VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _sweepFlag VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _previousAccount VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
  set @sweepFlag = true;
  IF _sweepFlag='false' THEN
    set @sweepFlag = false;
  END IF;
  IF _primaryAccount IS NOT NULL AND _primaryAccount != "" THEN
    set @list = (SELECT id from customeraccounts WHERE Customer_id = _customerId and Account_id = _primaryAccount);
    UPDATE customeraccounts SET isSweepCreated = @sweepFlag WHERE id in (@list);
  END IF;
  IF _secondaryAccount IS NOT NULL AND _secondaryAccount != "" THEN
    set @list = (SELECT id from customeraccounts WHERE Customer_id = _customerId and Account_id = _secondaryAccount);
    UPDATE customeraccounts SET isSweepCreated = @sweepFlag WHERE id in (@list);
  END IF;
  IF _previousAccount IS NOT NULL AND _previousAccount != "" THEN
    set @list = (SELECT id from customeraccounts WHERE Customer_id = _customerId and Account_id = _previousAccount);
    UPDATE customeraccounts SET isSweepCreated = false WHERE id in (@list);
  END IF;
END$$
DELIMITER ;

DROP VIEW IF EXISTS `servicedefinition_view`;

CREATE  VIEW `servicedefinition_view` AS
SELECT
    `servicedefinition`.`id` AS `id`,
    `servicedefinition`.`name` AS `name`,
    `servicedefinition`.`description` AS `description`,
    `servicedefinition`.`serviceType` AS `serviceType`,
    `servicedefinition`.`status` AS `status`,
    `servicedefinition`.`companyLegalUnit` AS `companyLegalUnit`,
    (
   SELECT
        COUNT(`groupservicedefinition`.`Group_id`)
    FROM
        `groupservicedefinition`
     WHERE
        (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)) AS `numberOfRoles`,
    (
    SELECT
        COUNT(`membergroup`.`id`)
    FROM
        `membergroup`
    WHERE
        ((`membergroup`.`Status_id` LIKE 'SID_ACTIVE')
            AND `membergroup`.`id` IN (
            SELECT
                `groupservicedefinition`.`Group_id`
            FROM
                `groupservicedefinition`
            WHERE
                (`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)))) AS `numberOfActiveRoles`,
    (
    SELECT
        `groupservicedefinition`.`Group_id`
    FROM
        `groupservicedefinition`
    WHERE
        ((`groupservicedefinition`.`serviceDefinitionId` = `servicedefinition`.`id`)
            AND (`groupservicedefinition`.`isDefaultGroup` = 1) AND (`groupservicedefinition`.`companyLegalUnit` = `servicedefinition`.`companyLegalUnit`))) AS`defaultRole`,
    (
    SELECT
        COUNT(DISTINCT `servicedefinition_features_actions_view`.`featureId`)
    FROM
        `servicedefinition_features_actions_view`
    WHERE
        ((`servicedefinition`.`id` = `servicedefinition_features_actions_view`.`serviceDefinitionId`)
            AND (`servicedefinition_features_actions_view`.`softdelete` = '0'))) AS `numberOfFeatures`,
    (
    SELECT
        COUNT(`contract`.`id`)
    FROM
        `contract`
    WHERE
        (`contract`.`servicedefinitionId` = `servicedefinition`.`id`)) AS `numberOfContracts`
FROM
    `servicedefinition`;


DROP procedure IF EXISTS `servicedefinition_view_proc`;

DELIMITER $$

CREATE PROCEDURE `servicedefinition_view_proc`(
IN _typeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit VARCHAR(1000) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DECLARE select_statement TEXT;
DECLARE index1 INTEGER DEFAULT 0;
SET @select_statement= 'SELECT id, name, description, serviceType, companyLegalUnit, status,
(SELECT COUNT(Group_id) AS Expr1
FROM groupservicedefinition
WHERE (serviceDefinitionId = servicedefinition.id)) AS numberOfRoles,
(SELECT COUNT(id) AS Expr1
FROM membergroup
WHERE (Status_id LIKE ''SID_ACTIVE'') AND (id IN
(SELECT Group_id
FROM groupservicedefinition
WHERE (serviceDefinitionId = servicedefinition.id)))) AS numberOfActiveRoles,
(SELECT Group_id
FROM groupservicedefinition
WHERE (serviceDefinitionId = servicedefinition.id) AND (isDefaultGroup = 1) AND (`groupservicedefinition`.`companyLegalUnit` = `servicedefinition`.`companyLegalUnit`)) AS defaultRole,
(SELECT COUNT(DISTINCT featureId) AS Expr1
FROM servicedefinition_features_actions_view
WHERE (servicedefinition.id = serviceDefinitionId) AND (softdelete = ''0'')) AS numberOfFeatures,
(SELECT COUNT(id) AS Expr1
FROM contract
WHERE (servicedefinitionId = servicedefinition.id)) AS numberOfContracts
FROM servicedefinition';
SET @companyLegalUnits = "";
SET @numOfCompanyLegalUnits = 0;

IF LENGTH(_companyLegalUnit) > 0 THEN
    SET @numOfCompanyLegalUnits = LENGTH(_companyLegalUnit) - LENGTH(REPLACE(_companyLegalUnit, '|', '')) + 1;
END IF;

SET @companyLegalUnit_statement = '';
IF (@numOfCompanyLegalUnits >= 1) THEN 
    companyLegalUnitsLoop : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfCompanyLegalUnits + 1 THEN
            LEAVE companyLegalUnitsLoop;
        ELSE
            SET @companyLegalUnit = SUBSTRING_INDEX(SUBSTRING_INDEX(_companyLegalUnit, '|', index1), '|', -1 );
            SET @companyLegalUnits = CONCAT(@companyLegalUnits, '''', @companyLegalUnit, '''');
            IF index1 != @numOfCompanyLegalUnits THEN 
				SET @companyLegalUnits = CONCAT(@companyLegalUnits, ',');
            END IF;
        END IF;
    END LOOP companyLegalUnitsLoop;
    SET @select_statement = CONCAT(@select_statement, ' WHERE companyLegalUnit IN (', @companyLegalUnits,')');
END IF;


IF _typeId is not null and CHAR_LENGTH(RTRIM(_typeId))>0 THEN
    IF @numOfCompanyLegalUnits >= 1 THEN
	    SET @select_statement = CONCAT(@select_statement, ' AND serviceType =''', _typeId,'''');
    ELSE
        SET @select_statement = CONCAT(@select_statement, ' WHERE serviceType =''', _typeId,'''');
    END IF;
END IF;

PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

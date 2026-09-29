DROP procedure IF EXISTS `fetch_approvalmatrixtemplate_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalmatrixtemplate_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN 
    IF _cif = "" THEN
    SET _cif = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SET @isGroupMatrix = (select isGroupLevel from approvalmode where contractId = _contractId AND coreCustomerId = _cif);
	IF @isGroupMatrix is null THEN
    SET @isGroupMatrix = 0;
	END IF;
	
    IF @isGroupMatrix = 0 THEN
		SELECT 
			`approvalmatrixtemplate`.`id`,
			`approvalmatrixtemplate`.`contractId`,
			 `approvalmatrixtemplate`.`limitTypeId`,
			 `featureAction`.`id` AS `actionId`,
			 `featureAction`.`name` AS `actionName`,
			 `featureAction`.`description` AS `actionDescription`,
			 `featureAction`.`Feature_id` AS `featureId`,
			 `featureAction`.`Type_id` AS `actionType`,
             `featureAction`.`isAccountLevel` AS `isAccountLevel`,
			 `feature`.`name` AS `featureName`,
			 `feature`.`Status_id` AS `fifeaturestatus`,
			 `approvalRule`.`id` AS `approvalruleId`,
			 `approvalRule`.`numberOfApprovals`,
			 `approvalRule`.`name` AS `approvalRuleName`,
			 `approvalmatrixtemplate`.`lowerlimit`,
			 `approvalmatrixtemplate`.`upperlimit`,
			 `customer`.`id` AS `customerId`,
			 `customer`.`FirstName` AS `firstName`,
			 `customer`.`LastName` AS `lastName`,
			 `contractcorecustomers`.`coreCustomerId` AS `cifId`,
			 `contractcorecustomers`.`coreCustomerName` AS `cifName`,
			 `approvalmatrixtemplate`.`invalid`,
			 `approvalmatrixtemplate`.`isGroupMatrix`
			FROM (((((((`approvalmatrixtemplate` AS `approvalmatrixtemplate`
			LEFT JOIN
			`customerapprovalmatrixtemplate` AS `customerapprovalmatrixtemplate`
			ON `approvalmatrixtemplate`.`id` = `customerapprovalmatrixtemplate`.`approvalMatrixId`)
			LEFT JOIN
			`customer` AS `customer`
			ON `customerapprovalmatrixtemplate`.`customerId` = `customer`.`id`)
			LEFT JOIN
			`featureaction` AS `featureAction`
			ON `approvalmatrixtemplate`.`actionId` = `featureAction`.`id`)
			LEFT JOIN
			`approvalrule` AS `approvalRule`
			ON `approvalmatrixtemplate`.`approvalruleId` = `approvalRule`.`id`) 
		  LEFT JOIN
			`feature` AS `feature`
			ON `featureAction`.`Feature_id` = `feature`.`id`)
		  LEFT JOIN
			`contractfeatures` AS `contractfeatures`
			ON `feature`.`id` = `contractfeatures`.`featureId`
			  and  `approvalmatrixtemplate`.`contractId` = `contractfeatures`.`contractId`
			  and `approvalmatrixtemplate`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
		  LEFT JOIN
			`contractcorecustomers` AS `contractcorecustomers`
			ON `approvalmatrixtemplate`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalmatrixtemplate`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
		WHERE 
		`approvalmatrixtemplate`.`contractId` = `_contractId` AND
		`approvalmatrixtemplate`.`coreCustomerId` LIKE `_cif` AND
		FIND_IN_SET(`approvalmatrixtemplate`.`actionId`,`_actions`) > 0 AND 
		`approvalmatrixtemplate`.`limitTypeId` LIKE `_limitTypeId` AND 
		`approvalmatrixtemplate`.`softdeleteflag` = 0 AND
		 `featureAction`.`approveFeatureAction` is not null AND
		 `featureAction`.`approveFeatureAction` != '' AND
		`featureAction`.`status` = 'SID_ACTION_ACTIVE'
			ORDER BY `approvalmatrixtemplate`.`contractId`,`approvalmatrixtemplate`.`coreCustomerId`,`approvalmatrixtemplate`.`limitTypeId`,`approvalmatrixtemplate`.`actionId` ,`approvalmatrixtemplate`.`lowerlimit`;
	
    ELSEIF @isGroupMatrix = 1 THEN
		SELECT 
			`approvalmatrixtemplate`.`id`,
			`approvalmatrixtemplate`.`contractId`,
			 `approvalmatrixtemplate`.`limitTypeId`,
			 `featureAction`.`id` AS `actionId`,
			 `featureAction`.`name` AS `actionName`,
			 `featureAction`.`description` AS `actionDescription`,
			 `featureAction`.`Feature_id` AS `featureId`,
			 `featureAction`.`Type_id` AS `actionType`,
             `featureAction`.`isAccountLevel` AS `isAccountLevel`,
			 `feature`.`name` AS `featureName`,
			 `feature`.`Status_id` AS `fifeaturestatus`,
			 `approvalRule`.`id` AS `approvalruleId`,
			 `approvalRule`.`numberOfApprovals`,
			 `approvalRule`.`name` AS `approvalRuleName`,
			 `approvalmatrixtemplate`.`lowerlimit`,
			 `approvalmatrixtemplate`.`upperlimit`,
			 `signatorygroupmatrixtemplate`.`groupList` AS `groupList`,
			 `signatorygroupmatrixtemplate`.`groupRule` AS `groupRule`,
			 `contractcorecustomers`.`coreCustomerId` AS `cifId`,
			 `contractcorecustomers`.`coreCustomerName` AS `cifName`,
			 `approvalmatrixtemplate`.`invalid`,
			 `approvalmatrixtemplate`.`isGroupMatrix`
			FROM ((((((`approvalmatrixtemplate` AS `approvalmatrixtemplate`
			LEFT JOIN
			`signatorygroupmatrixtemplate` AS `signatorygroupmatrixtemplate`
			ON `approvalmatrixtemplate`.`id` = `signatorygroupmatrixtemplate`.`approvalMatrixId`)
			LEFT JOIN
			`featureaction` AS `featureAction`
			ON `approvalmatrixtemplate`.`actionId` = `featureAction`.`id`)
			LEFT JOIN
			`approvalrule` AS `approvalRule`
			ON `approvalmatrixtemplate`.`approvalruleId` = `approvalRule`.`id`) 
		  LEFT JOIN
			`feature` AS `feature`
			ON `featureAction`.`Feature_id` = `feature`.`id`)
		  LEFT JOIN
			`contractfeatures` AS `contractfeatures`
			ON `feature`.`id` = `contractfeatures`.`featureId`
			  and  `approvalmatrixtemplate`.`contractId` = `contractfeatures`.`contractId`
			  and `approvalmatrixtemplate`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
		  LEFT JOIN
			`contractcorecustomers` AS `contractcorecustomers`
			ON `approvalmatrixtemplate`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalmatrixtemplate`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
		WHERE 
		`approvalmatrixtemplate`.`contractId` = `_contractId` AND
		`approvalmatrixtemplate`.`coreCustomerId` LIKE `_cif` AND 
		`approvalmatrixtemplate`.`isGroupMatrix` = 1 AND
		 FIND_IN_SET(`approvalmatrixtemplate`.`actionId`,`_actions`) > 0 AND 
		`approvalmatrixtemplate`.`limitTypeId` LIKE `_limitTypeId` AND 
		`approvalmatrixtemplate`.`softdeleteflag` = 0 AND
		`featureAction`.`approveFeatureAction` is not null AND
		`featureAction`.`approveFeatureAction` != '' AND
		`featureAction`.`status` = 'SID_ACTION_ACTIVE'
		 ORDER BY `approvalmatrixtemplate`.`contractId`,`approvalmatrixtemplate`.`coreCustomerId`,`approvalmatrixtemplate`.`limitTypeId`,`approvalmatrixtemplate`.`actionId` ,`approvalmatrixtemplate`.`lowerlimit`;

    END IF;
END$$
DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_fetch_grouprecords_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_fetch_grouprecords_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN 
    IF _cif = "" THEN
    SET _cif = "%";
    END IF;
    
    IF _accountId = "" THEN
    SET _accountId = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SELECT 
    	
      	`approvalMatrix`.`id`,
        `approvalMatrix`.`contractId`,
         `approvalMatrix`.`accountId`,
         `approvalMatrix`.`limitTypeId`,
         `featureAction`.`id` AS `actionId`,
         `featureAction`.`name` AS `actionName`,
         `featureAction`.`description` AS `actionDescription`,
         `featureAction`.`Feature_id` AS `featureId`,
		 `featureAction`.`Type_id` AS `actionType`,
         `featureAction`.`isAccountLevel` AS `isAccountLevel`,
         `feature`.`name` AS `featureName`,
         `feature`.`Status_id` AS `fifeaturestatus`,
         `approvalRule`.`id` AS `approvalruleId`,
         `approvalRule`.`numberOfApprovals`,
         `approvalRule`.`name` AS `approvalRuleName`,
         `approvalMatrix`.`lowerlimit`,
         `approvalMatrix`.`upperlimit`,
		 `signatoryGroupMatrix`.`groupList` AS `groupList`,
		 `signatoryGroupMatrix`.`groupRule` AS `groupRule`,
         `contractcorecustomers`.`coreCustomerId` AS `cifId`,
         `contractcorecustomers`.`coreCustomerName` AS `cifName`,
         `approvalMatrix`.`invalid`,
         `approvalMatrix`.`isGroupMatrix`
        FROM ((((((`approvalmatrix` AS `approvalMatrix`
        LEFT JOIN
        `signatorygroupmatrix` AS `signatoryGroupMatrix`
        ON `approvalMatrix`.`id` = `signatoryGroupMatrix`.`approvalMatrixId`)
        LEFT JOIN
        `featureaction` AS `featureAction`
        ON `approvalMatrix`.`actionId` = `featureAction`.`id`)
        LEFT JOIN
        `approvalrule` AS `approvalRule`
        ON `approvalMatrix`.`approvalruleId` = `approvalRule`.`id`) 
      LEFT JOIN
        `feature` AS `feature`
        ON `featureAction`.`Feature_id` = `feature`.`id`)
      LEFT JOIN
        `contractfeatures` AS `contractfeatures`
        ON `feature`.`id` = `contractfeatures`.`featureId`
          and  `approvalMatrix`.`contractId` = `contractfeatures`.`contractId`
          and `approvalMatrix`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
      LEFT JOIN
        `contractcorecustomers` AS `contractcorecustomers`
        ON `approvalMatrix`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalMatrix`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
    WHERE 
    `approvalMatrix`.`contractId` = `_contractId` AND
    `approvalMatrix`.`coreCustomerId` LIKE `_cif` AND
    `approvalMatrix`.`accountId` LIKE `_accountId` AND 
    `approvalMatrix`.`isGroupMatrix` = 1 AND
    FIND_IN_SET(`approvalMatrix`.`actionId`,`_actions`) > 0 AND 
    `approvalMatrix`.`limitTypeId` LIKE `_limitTypeId` AND 
    `approvalMatrix`.`softdeleteflag` = 0 AND
	 `featureAction`.`approveFeatureAction` is not null AND
	 `featureAction`.`approveFeatureAction` != '' AND
	`featureAction`.`status` = 'SID_ACTION_ACTIVE'
        ORDER BY `approvalMatrix`.`contractId`,`approvalMatrix`.`coreCustomerId`,`approvalMatrix`.`accountId`,`approvalMatrix`.`limitTypeId`,`approvalMatrix`.`actionId` ,`approvalMatrix`.`lowerlimit`;
END$$
DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_fetch_records_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_fetch_records_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN 
    IF _cif = "" THEN
    SET _cif = "%";
    END IF;
    
    IF _accountId = "" THEN
    SET _accountId = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SELECT 
    	
      	`approvalMatrix`.`id`,
        `approvalMatrix`.`contractId`,
         `approvalMatrix`.`accountId`,
         `approvalMatrix`.`limitTypeId`,
         `featureAction`.`id` AS `actionId`,
         `featureAction`.`name` AS `actionName`,
         `featureAction`.`description` AS `actionDescription`,
         `featureAction`.`Feature_id` AS `featureId`,
		 `featureAction`.`Type_id` AS `actionType`,
         `featureAction`.`isAccountLevel` AS `isAccountLevel`,
         `feature`.`name` AS `featureName`,
         `feature`.`Status_id` AS `fifeaturestatus`,
         `approvalRule`.`id` AS `approvalruleId`,
         `approvalRule`.`numberOfApprovals`,
         `approvalRule`.`name` AS `approvalRuleName`,
         `approvalMatrix`.`lowerlimit`,
         `approvalMatrix`.`upperlimit`,
         `customer`.`id` AS `customerId`,
         `customer`.`FirstName` AS `firstName`,
         `customer`.`LastName` AS `lastName`,
         `contractcorecustomers`.`coreCustomerId` AS `cifId`,
         `contractcorecustomers`.`coreCustomerName` AS `cifName`,
         `approvalMatrix`.`invalid`,
         `approvalMatrix`.`isGroupMatrix`
        FROM (((((((`approvalmatrix` AS `approvalMatrix`
        LEFT JOIN
        `customerapprovalmatrix` AS `customerApprovalMatrix`
        ON `approvalMatrix`.`id` = `customerApprovalMatrix`.`approvalMatrixId`)
        LEFT JOIN
        `customer` AS `customer`
        ON `customerApprovalMatrix`.`customerId` = `customer`.`id`)
        LEFT JOIN
        `featureaction` AS `featureAction`
        ON `approvalMatrix`.`actionId` = `featureAction`.`id`)
        LEFT JOIN
        `approvalrule` AS `approvalRule`
        ON `approvalMatrix`.`approvalruleId` = `approvalRule`.`id`) 
      LEFT JOIN
        `feature` AS `feature`
        ON `featureAction`.`Feature_id` = `feature`.`id`)
      LEFT JOIN
        `contractfeatures` AS `contractfeatures`
        ON `feature`.`id` = `contractfeatures`.`featureId`
          and  `approvalMatrix`.`contractId` = `contractfeatures`.`contractId`
          and `approvalMatrix`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
      LEFT JOIN
        `contractcorecustomers` AS `contractcorecustomers`
        ON `approvalMatrix`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalMatrix`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
    WHERE 
    `approvalMatrix`.`contractId` = `_contractId` AND
    `approvalMatrix`.`coreCustomerId` LIKE `_cif` AND
    `approvalMatrix`.`accountId` LIKE `_accountId` AND 
    FIND_IN_SET(`approvalMatrix`.`actionId`,`_actions`) > 0 AND 
    `approvalMatrix`.`limitTypeId` LIKE `_limitTypeId` AND 
    `approvalMatrix`.`softdeleteflag` = 0 AND
	 `featureAction`.`approveFeatureAction` is not null AND
	 `featureAction`.`approveFeatureAction` != '' AND
	`featureAction`.`status` = 'SID_ACTION_ACTIVE'
        ORDER BY `approvalMatrix`.`contractId`,`approvalMatrix`.`coreCustomerId`,`approvalMatrix`.`accountId`,`approvalMatrix`.`limitTypeId`,`approvalMatrix`.`actionId` ,`approvalMatrix`.`lowerlimit`;
END$$
DELIMITER ;

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
           IF ((`customeraccounts`.`EStatementmentEnable`) = '1' ,'true' , 'false') AS `eStatementEnable`,
           IF ((`contractcorecustomers`.`isBusiness`) = '1' ,'true' , 'false') AS `isBusinessAccount`,
           `contractaccounts`.`accountId` AS accountId
           from (`contractcorecustomers`) 
		   JOIN (`contractaccounts`) ON (`contractcorecustomers`.`coreCustomerId` = `contractaccounts`.`coreCustomerId`)
           JOIN (`customeraccounts`) ON (`customeraccounts`.`Account_id` = `contractaccounts`.`accountId`)
           where FIND_IN_SET(`contractcorecustomers`.`coreCustomerId`, '",@corecustomersList,"') AND `customeraccounts`.`Customer_id` = '",_customerId,"'");
           
           PREPARE stmt FROM @selectstatement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$

DELIMITER ;


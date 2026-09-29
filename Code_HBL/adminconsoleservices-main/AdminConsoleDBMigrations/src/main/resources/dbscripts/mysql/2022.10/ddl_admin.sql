SET FOREIGN_KEY_CHECKS = 0;

ALTER TABLE `approvalmatrix` ADD COLUMN currency  VARCHAR(32) NOT NULL DEFAULT 'USD';
ALTER TABLE `approvalmatrixtemplate` ADD COLUMN currency  VARCHAR(32) NOT NULL DEFAULT 'USD';
DROP PROCEDURE IF EXISTS `approvalmatrixtemplate_default_create_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrixtemplate_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _approvalMode INT,
IN _currency VARCHAR(32)
)
MAINLABEL: BEGIN
 DECLARE accountList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 DECLARE actionIndex INTEGER DEFAULT 0;
 DECLARE isGroupMatrix INT DEFAULT 0;
 DECLARE typeId TEXT DEFAULT "";
 
 IF _actionIds IS NULL OR  _actionIds = '' THEN
	LEAVE MAINLABEL;
END IF;

IF _contractId IS NULL OR  _contractId = '' THEN
	LEAVE MAINLABEL;
END IF;
	
IF _cif IS NULL OR  _cif = '' THEN
	LEAVE MAINLABEL;
END IF;
 
IF _approvalMode = 0 THEN
	SET isGroupMatrix = 0;
ELSEIF _approvalMode = 1 THEN
	SET isGroupMatrix = 1;
END IF;
SET @legalEntityId = "ALL";
SELECT companyLegalUnit into @legalEntityId from contractcorecustomers where coreCustomerId = _cif and contractId = _contractId;
set @numOfActions = LENGTH(_actionIds) - LENGTH(REPLACE(_actionIds, ',', '')) + 1;
set actionIndex = 0; 
getAction: LOOP
	set actionIndex = actionIndex + 1;
	IF actionIndex = @numOfActions + 1 THEN
		LEAVE getAction;
	Else
		set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(_actionIds, ',', actionIndex), ',', -1 );
		SELECT Type_id INTO typeId FROM featureaction WHERE id = @actionId AND companyLegalUnit = @legalEntityId;
		IF typeId = "MONETARY" THEN			
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix, currency) VALUES
			(_contractId, @actionId, limitTypeId_1,_cif,'NO_APPROVAL',isGroupMatrix, _currency);
            INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix, currency) VALUES
			(_contractId, @actionId,limitTypeId_2,_cif,'NO_APPROVAL',isGroupMatrix, _currency);
            INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix, currency) VALUES
			(_contractId, @actionId, limitTypeId_3,_cif,'NO_APPROVAL',isGroupMatrix, _currency);
		ELSEIF typeId = "NON_MONETARY" THEN
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix, currency) VALUES
			(_contractId, @actionId, "NON_MONETARY_LIMIT",_cif,'NO_APPROVAL',isGroupMatrix, _currency);
        END IF;
	END IF;
 END LOOP getAction;
END$$

DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_approvalmatrixtemplate_proc`;
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
    SET @legalEntityId = "ALL";
    SELECT companyLegalUnit into @legalEntityId from contractcorecustomers where coreCustomerId = _cif and contractId = _contractId;
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
             `approvalmatrixtemplate`.`currency`,
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
		`featureAction`.`status` = 'SID_ACTION_ACTIVE' AND
        `featureAction`.`companyLegalUnit` = @legalEntityId AND
        `feature`.`companyLegalUnit` = @legalEntityId
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
             `approvalmatrixtemplate`.`currency`,
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
		`featureAction`.`status` = 'SID_ACTION_ACTIVE' AND
        `featureAction`.`companyLegalUnit` = @legalEntityId AND
        `feature`.`companyLegalUnit` = @legalEntityId
			ORDER BY `approvalmatrixtemplate`.`contractId`,`approvalmatrixtemplate`.`coreCustomerId`,`approvalmatrixtemplate`.`limitTypeId`,`approvalmatrixtemplate`.`actionId` ,`approvalmatrixtemplate`.`lowerlimit`;

    END IF;
END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_create_proc`;

DELIMITER $$

CREATE PROCEDURE `approvalmatrix_create_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _approverIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE index2 INTEGER DEFAULT 0;
	set @length = LENGTH(_matrixValues) - LENGTH(REPLACE(_matrixValues, ',', '')) + 1;
	getValues: LOOP
			set index1 = index1 + 1;
			IF index1 = @length + 1 THEN
				LEAVE getValues;
			else
				set @matrixRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixValues, ',', index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,approvalruleId,limitTypeId,lowerlimit,upperlimit,currency) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;

				SET @id = LAST_INSERT_ID();
				set @customerIds = SUBSTRING_INDEX( SUBSTRING_INDEX(_approverIds, ',', index1), ',', -1 );
                IF @customerIds IS NOT NULL AND @customerIds != '' THEN
					set @customerIdsComma = REPLACE(@customerIds, ';', ',');
						set @length2 = LENGTH(@customerIdsComma) - LENGTH(REPLACE(@customerIdsComma, ',', '')) + 1;
						set index2 = 0;
						getCustomerIds: LOOP
							set index2 = index2 + 1;
							IF index2 = @length2 + 1 THEN
								LEAVE getCustomerIds;
							else
								set @customerId = SUBSTRING_INDEX( SUBSTRING_INDEX(@customerIdsComma, ',', index2), ',', -1 );
								INSERT INTO customerapprovalmatrix(customerId,approvalMatrixId) values (@customerId,@id);
								ITERATE getCustomerIds;
							END IF;
						END LOOP getCustomerIds;
					END IF;
				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS approvalmatrix_fetch_records_proc;
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
         `approvalMatrix`.`currency`,
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

DROP PROCEDURE IF EXISTS `approvalmatrix_signatorygroupmatrixcreate_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrix_signatorygroupmatrixcreate_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _signatorymatrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE index2 INTEGER DEFAULT 0;
	set @length = LENGTH(_matrixValues) - LENGTH(REPLACE(_matrixValues, ',', '')) + 1;
	getValues: LOOP
			set index1 = index1 + 1;
			IF index1 = @length + 1 THEN
				LEAVE getValues;
			else
				set @matrixRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixValues, ',', index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,approvalruleId,isGroupMatrix,limitTypeId,lowerlimit,upperlimit,currency) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;

				set @sigValues = SUBSTRING_INDEX( SUBSTRING_INDEX(_signatorymatrixValues, '#', index1), '#', -1 );
				SET @id = LAST_INSERT_ID();
				SET @groupList = SUBSTRING_INDEX(@sigValues, ';', 1 );
				SET @groupRule = SUBSTRING_INDEX(@sigValues, ';', -1 );
				INSERT INTO signatorygroupmatrix(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);

				ITERATE  getValues;
			END IF;
	END LOOP getValues;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrixtemplate_create_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrixtemplate_create_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _matrixApprover TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _isGroupMatrix INT
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	DECLARE index2 INTEGER DEFAULT 0;

    set @length = LENGTH(_matrixValues) - LENGTH(REPLACE(_matrixValues, ',', '')) + 1;
	getValues: LOOP
			set index1 = index1 + 1;
			IF index1 = @length + 1 THEN
				LEAVE getValues;
			else
				set @matrixRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixValues, ',', index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				set @query = concat('INSERT INTO approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,currency,isGroupMatrix) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;

				SET @id = LAST_INSERT_ID();

                IF _isGroupMatrix = 0 THEN
					set @customerIds = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixApprover, ',', index1), ',', -1 );
					IF @customerIds IS NOT NULL AND @customerIds != '' THEN
						set @customerIdsComma = REPLACE(@customerIds, ';', ',');
							set @length2 = LENGTH(@customerIdsComma) - LENGTH(REPLACE(@customerIdsComma, ',', '')) + 1;
							set index2 = 0;
							getCustomerIds: LOOP
								set index2 = index2 + 1;
								IF index2 = @length2 + 1 THEN
									LEAVE getCustomerIds;
								else
									set @customerId = SUBSTRING_INDEX( SUBSTRING_INDEX(@customerIdsComma, ',', index2), ',', -1 );
									INSERT INTO customerapprovalmatrixtemplate(customerId,approvalMatrixId) values (@customerId,@id);
									ITERATE getCustomerIds;
								END IF;
							END LOOP getCustomerIds;
						END IF;
					ITERATE  getValues;
				ELSEIF _isGroupMatrix = 1 THEN
					set @sigValues = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixApprover, '#', index1), '#', -1 );
					SET @id = LAST_INSERT_ID();
					SET @groupList = SUBSTRING_INDEX(@sigValues, ';', 1 );
					SET @groupRule = SUBSTRING_INDEX(@sigValues, ';', -1 );
					INSERT INTO signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
               END IF;
			END IF;
	END LOOP getValues;
END$$
DELIMITER ;
DROP PROCEDURE IF EXISTS approvalmatrix_fetch_grouprecords_proc;
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
         `approvalMatrix`.`currency`,
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



ALTER TABLE `application` ADD COLUMN `isSingleEntity` BIT(1) NOT NULL DEFAULT b'0';
ALTER TABLE `feature` DROP PRIMARY KEY, ADD PRIMARY KEY (`id`, `companyLegalUnit`);
ALTER TABLE `featuredisplaynamedescription` DROP PRIMARY KEY, ADD PRIMARY KEY (`Feature_id`, `Locale_id`, `companyLegalUnit`);
ALTER TABLE `featureaction` DROP PRIMARY KEY, ADD PRIMARY KEY (`id`, `companyLegalUnit`);
ALTER TABLE `featureaction` DROP INDEX `featureaction_unique_index`;
ALTER TABLE `featureaction` ADD UNIQUE `featureaction_unique_index` (`App_id`,`id`, `companyLegalUnit`);
ALTER TABLE `featureroletype` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE `featureroletype` DROP PRIMARY KEY, ADD PRIMARY KEY (`RoleType_id`, `Feature_id`, `companyLegalUnit`);

DROP PROCEDURE IF EXISTS `systemroles_permission_proc`;
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
    `rolepermission` `rp` , `permission` `p`
    WHERE `p`.`id` = `rp`.`Permission_id` AND `p`.`Status_id`='SID_ACTIVE' AND FIND_IN_SET(`rp`.`Role_id`,_roleIds) ORDER BY `id`;
END$$
DELIMITER ;

DROP procedure IF EXISTS `group_features_actions_view_proc`;

DELIMITER $$
CREATE PROCEDURE `group_features_actions_view_proc`
(
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE select_statement TEXT;
SET @select_statement = 'SELECT groupactionlimit.Group_id, 
groupactionlimit.Action_id, 
groupactionlimit.LimitType_id, 
groupactionlimit.value, 
groupactionlimit.id AS groupactionlimit_id,
groupactionlimit.softdeleteflag AS softdelete, 
membergroup.Type_id, 
membergroup.Name AS Group_name,
membergroup.Description AS Group_description,
featureaction.name AS Action_name,
featureaction.description AS Action_description, 
featureaction.Type_id AS Action_Type_id, 
featureaction.Feature_id, 
featureaction.isMFAApplicable, 
featureaction.isAccountLevel,
featureaction.isPrimary, 
featureaction.DisplaySequence AS Action_displaysequence, 
featureaction.dependency AS Action_dependency, 
featureaction.status AS actionStatus,
featureaction.companyLegalUnit AS companyLegalUnit,
accesspolicy.name AS accessPolicy, 
featureaction.accesspolicyId, 
featureaction.limitgroupId, 
limitgroup.name AS limitGroup, 
actionlevel.name AS actionlevel, 
featureaction.actionlevelId,
feature.name AS featureName, 
feature.name AS Feature_name, 
feature.description AS Feature_description, 
feature.Type_id AS Feature_Type_id, 
feature.Status_id AS Feature_Status_id,
feature.DisplaySequence AS Feature_displaysequence, 
feature.isPrimary AS Feature_isPrimary
FROM groupactionlimit 
LEFT OUTER JOIN
membergroup ON (membergroup.id = groupactionlimit.Group_id) 
LEFT OUTER JOIN
featureaction ON (featureaction.id = groupactionlimit.Action_id) 
LEFT OUTER JOIN
feature ON (feature.id = featureaction.Feature_id) 
LEFT OUTER JOIN
accesspolicy ON (featureaction.accesspolicyId = accesspolicy.id) 
LEFT OUTER JOIN
limitgroup ON (featureaction.limitgroupId = limitgroup.id) 
LEFT OUTER JOIN
actionlevel ON (featureaction.actionlevelId = actionlevel.id)';

IF _groupId is not null and CHAR_LENGTH(RTRIM(_groupId))>0 THEN 
SET @select_statement = CONCAT (@select_statement ,'where groupactionlimit.Group_id =''', _groupId,'''', 'and `featureaction`.`companyLegalUnit` =''',_companyLegalUnit ,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `servicedefinition_defaultgroup_update_proc`;

DELIMITER $$
CREATE PROCEDURE `servicedefinition_defaultgroup_update_proc`(
 IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _isDefault varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
 IN _companyLegalUnit varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
 )
BEGIN
 IF(_isDefault = '0') THEN
     UPDATE groupservicedefinition SET isDefaultGroup = false where serviceDefinitionId = _serviceDefinitionId AND Group_id = _groupId AND companyLegalUnit = _companyLegalUnit;
 ELSE
     UPDATE groupservicedefinition SET isDefaultGroup = false where serviceDefinitionId = _serviceDefinitionId AND companyLegalUnit = _companyLegalUnit;
     UPDATE groupservicedefinition SET isDefaultGroup = true where serviceDefinitionId = _serviceDefinitionId AND Group_id = _groupId AND companyLegalUnit = _companyLegalUnit;     
 END IF;
 END$$
DELIMITER ;

DROP procedure IF EXISTS `get_feature_actions_view_proc`;

DELIMITER $$
CREATE PROCEDURE `get_feature_actions_view_proc`(IN _roleTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE select_statement TEXT;
SET @select_statement = 'SELECT 
    `featureaction`.`id` AS `actionId`, 
    `featureaction`.`Feature_id` AS `featureId`, 
    `featureaction`.`name` AS `actionName`, 
    `featureaction`.`isAccountLevel`, 
    `featureaction`.`description` AS `actionDescription`,
    `featureaction`.`isMFAApplicable`, 
	`featureaction`.`isPrimary`, 
	`featureaction`.`notes`, 
	`featureaction`.`Type_id` AS `typeId`, 
	`featureaction`.`DisplaySequence` AS `actionDisplaySequence`,
    `featureaction`.`dependency` AS `actionDependency`, 
	`featureaction`.`status` AS `actionStatus`,
    `featureaction`.`companyLegalUnit` AS `companyLegalUnit`,
	`featureactionroletype`.`RoleType_id` AS `actionType`, 
	`accesspolicy`.`name` AS `accesspolicy`,
    `limitgroup`.`name` AS `limitgroup`, 
	`featureaction`.`accesspolicyId`, 
	`featureaction`.`limitgroupId`, 
	`feature`.`Status_id` AS `featureStatus`, 
	`feature`.`name` AS `featureName`,
    `feature`.`description` AS `featureDescription`, 
	`feature`.`Type_id` AS `featureType`, 
	`featureroletype`.`RoleType_id` AS `featureGroup`, 
	`feature`.`DisplaySequence` AS `featureDisplaySequence`,
    `feature`.`isPrimary` AS `isFeaturePrimary`, 
	`actionlevel`.`name` AS `actionlevel`, 
	`featureaction`.`actionlevelId`, 
	`actiondisplaynamedescription`.`Locale_id` AS `localeId`,
    `actiondisplaynamedescription`.`displayName`, 
	`actiondisplaynamedescription`.`displayDescription`, 
	`actionlimit`.`LimitType_id` AS `limitTypeId`, 
	`actionlimit`.`value`,
    `dependentactions_view`.`dependentactionId`, 
	`dependentactions_view`.`featureName` AS `dependentFeatureName`, 
	`dependentactions_view`.`actionName` AS `dependentActionName`,
    `dependentactions_view`.`featureId` AS `dependentFeatureId`, 
	`termandcondition`.`Code` AS `termsAndConditionCode`, 
	`termandcondition`.`Title` AS `termsAndConditionTitle`,
    `termandcondition`.`Description` AS `termsAndConditionDescription`, 
	`membergrouptype`.`description` AS `roleTypeName`
FROM
    (((((((((((`featureaction`
LEFT JOIN `feature` on (((`feature`.`id` = `featureaction`.`Feature_id` ) and (`feature`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
LEFT JOIN `actiondisplaynamedescription` on  (((`actiondisplaynamedescription`.`Action_id` = `featureaction`.`id`) and (`actiondisplaynamedescription`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
LEFT JOIN `accesspolicy` on ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`))) 
LEFT JOIN `featureroletype` on (((`featureroletype`.`Feature_id` = `feature`.`id`) and (`featureroletype`.`companyLegalUnit` = `feature`.`companyLegalUnit`))))
LEFT JOIN `featureactionroletype` on (((`featureaction`.`id` = `featureactionroletype`.`Action_id`) and (`featureaction`.`companyLegalUnit` = `featureactionroletype`.`companyLegalUnit`))))
LEFT JOIN `termandcondition` on ((`featureaction`.`TermsAndConditions_id` = `termandcondition`.`id`)))
LEFT JOIN `limitgroup` on ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)))
LEFT JOIN `actionlevel` on ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)))
LEFT JOIN `dependentactions_view` on (((`featureaction`.`id` = `dependentactions_view`.`actionId`) and (`feature`.`companyLegalUnit` = `dependentactions_view`.`companyLegalUnit`))))
LEFT JOIN `membergrouptype` on ((`featureactionroletype`.`RoleType_id` = `membergrouptype`.`id`)))
LEFT JOIN `actionlimit` on (((`actionlimit`.`Action_id` = `featureaction`.`id`) and (`actionlimit`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))';
 
IF _roleTypeId is not null and CHAR_LENGTH(RTRIM(_roleTypeId))>0 THEN 
	SET @select_statement = CONCAT (@select_statement ,'where `featureaction`.`companyLegalUnit`= ''', _companyLegalUnit,'''', 'and `featureroletype`.`RoleType_Id` = ''', _roleTypeId,'''', 'and `featureactionroletype`.`RoleType_Id` =''',_roleTypeId ,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

ALTER TABLE `actionlimit` DROP PRIMARY KEY, ADD PRIMARY KEY (`Action_id`, `LimitType_id`, `companyLegalUnit`);
ALTER TABLE `actiondisplaynamedescription` DROP PRIMARY KEY, ADD PRIMARY KEY (`Action_id`, `Locale_id`, `companyLegalUnit`);
ALTER TABLE `dependentactions` DROP PRIMARY KEY, ADD PRIMARY KEY (`actionId`, `dependentactionId`, `companyLegalUnit`);

DROP procedure IF EXISTS `feature_action_limits_update_proc`;

DELIMITER $$
CREATE PROCEDURE `feature_action_limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _minTxLimit decimal(20,2),
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2)
)
BEGIN
  UPDATE actionlimit SET value = _minTxLimit where Action_id = _action AND LimitType_id = 'MIN_TRANSACTION_LIMIT' AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _maxTxLimit where Action_id = _action AND LimitType_id = 'MAX_TRANSACTION_LIMIT'AND companyLegalUnit = _companyLegalUnit; 
  UPDATE actionlimit SET value = _dailyLimit where Action_id = _action AND LimitType_id = 'DAILY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
  UPDATE actionlimit SET value = _weeklyLimit where Action_id = _action AND LimitType_id = 'WEEKLY_LIMIT' AND companyLegalUnit = _companyLegalUnit;
END$$

DELIMITER ;

DROP procedure IF EXISTS `limits_update_proc`;

DELIMITER $$
CREATE PROCEDURE `limits_update_proc`(
  IN _action varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _maxTxLimit decimal(20,2),
  IN _dailyLimit decimal(20,2),
  IN _weeklyLimit decimal(20,2)
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
END$$

DELIMITER ;

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
WHERE (serviceDefinitionId = servicedefinition.id) AND (isDefaultGroup = 1)) AS defaultRole,
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

DROP PROCEDURE IF EXISTS verify_user_proc;
DELIMITER $$
$$
CREATE PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendIdentifiers varchar(5000) CHARACTER SET UTF8 COLLATE utf8_general_ci,
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
		`customer`.`companyLegalUnit` AS `companyLegalUnit`,
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
		and `customer`.`companyLegalUnit` = _legalEntityId
        and `primaryphone`.`Customer_id` = `primaryemail`.`Customer_id`) union SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
		`customer`.`companyLegalUnit` AS `companyLegalUnit`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`,
        `customer`.`CustomerType_id` AS `CustomerType_id`
    FROM `customer` where FIND_IN_SET(customer.id ,@usersList) and `customer`.`companyLegalUnit` = _legalEntityId;
   
END $$
DELIMITER ;

ALTER TABLE `featureactionroletype` DROP PRIMARY KEY, ADD PRIMARY KEY (`RoleType_id`, `Action_id`, `companyLegalUnit`);

DROP VIEW IF EXISTS `dependentactions_view`;
CREATE VIEW `dependentactions_view` AS
    SELECT 
        `dependentactions`.`actionId` AS `actionId`,
        `dependentactions`.`dependentactionId` AS `dependentactionId`,
        `featureaction`.`name` AS `actionName`,
        `dependentactions`.`featureId` AS `featureId`,
        `feature`.`name` AS `featureName`,
        `dependentactions`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
		((`dependentactions` 
		LEFT JOIN `featureaction` ON (((`dependentactions`.`dependentactionId` = `featureaction`.`id`)))) 
		LEFT JOIN `feature` ON (((`dependentactions`.`featureId` = `feature`.`id`) and (`featureaction`.`companyLegalUnit`  = `feature`.`companyLegalUnit`))));

DROP VIEW IF EXISTS `get_feature_actions_view`;
CREATE VIEW `get_feature_actions_view` AS
    SELECT 
        `featureaction`.`id` AS `actionId`,
        `featureaction`.`Feature_id` AS `featureId`,
        `featureaction`.`name` AS `actionName`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`description` AS `actionDescription`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`notes` AS `notes`,
        `featureaction`.`Type_id` AS `typeId`,
        `featureaction`.`DisplaySequence` AS `actionDisplaySequence`,
        `featureaction`.`dependency` AS `actionDependency`,
        `featureaction`.`status` AS `actionStatus`,
        `featureaction`.`companyLegalUnit` AS `companyLegalUnit`,
        `featureactionroletype`.`RoleType_id` AS `actionType`,
        `featureaction`.`accesspolicyId` AS `accessPolicyId`,
        `accesspolicy`.`name` AS `accessPolicy`,
        `featureaction`.`limitgroupId` AS `limitGroupId`,
        `limitgroup`.`name` AS `limitGroup`,
        `feature`.`Status_id` AS `featureStatus`,
        `feature`.`name` AS `featureName`,
        `feature`.`description` AS `featureDescription`,
        `feature`.`Type_id` AS `featureType`,
        `featureroletype`.`RoleType_id` AS `featureGroup`,
        `feature`.`DisplaySequence` AS `featureDisplaySequence`,
        `feature`.`isPrimary` AS `isFeaturePrimary`,
        `actionlevel`.`name` AS `actionlevel`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
        `actiondisplaynamedescription`.`Locale_id` AS `localeId`,
        `actiondisplaynamedescription`.`displayName` AS `displayName`,
        `actiondisplaynamedescription`.`displayDescription` AS `displayDescription`,
        `actionlimit`.`LimitType_id` AS `limitTypeId`,
        `actionlimit`.`value` AS `value`,
        `dependentactions_view`.`dependentactionId` AS `dependentactionId`,
        `dependentactions_view`.`featureId` AS `dependentFeatureId`,
        `dependentactions_view`.`actionName` AS `dependentActionName`,
        `dependentactions_view`.`featureName` AS `dependentFeatureName`,
        `termandcondition`.`Code` AS `termsAndConditionCode`,
        `termandcondition`.`Title` AS `termsAndConditionTitle`,
        `termandcondition`.`Description` AS `termsAndConditionDescription`,
        `membergrouptype`.`description` AS `roleTypeName`
    FROM
        (((((((((((`featureaction`
        LEFT JOIN `feature` ON (((`feature`.`id` = `featureaction`.`Feature_id`)
            AND (`feature`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
        LEFT JOIN `actiondisplaynamedescription` ON (((`actiondisplaynamedescription`.`Action_id` = `featureaction`.`id`)
            AND (`actiondisplaynamedescription`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
        LEFT JOIN `accesspolicy` ON (`featureaction`.`accesspolicyId` = `accesspolicy`.`id`))
        LEFT JOIN `featureroletype` ON ((`featureroletype`.`Feature_id` = `feature`.`id`) 
            AND (`featureroletype`.`companyLegalUnit` = `feature`.`companyLegalUnit`)))
        LEFT JOIN `featureactionroletype` ON (((`featureaction`.`id` = `featureactionroletype`.`Action_id`)
            AND (`featureaction`.`companyLegalUnit` = `featureactionroletype`.`companyLegalUnit`))))
        LEFT JOIN `termandcondition` ON ((`featureaction`.`TermsAndConditions_id` = `termandcondition`.`id`)))
        LEFT JOIN `limitgroup` ON (`featureaction`.`limitgroupId` = `limitgroup`.`id`))
        LEFT JOIN `actionlevel` ON (`featureaction`.`actionlevelId` = `actionlevel`.`id`))
        LEFT JOIN `dependentactions_view` ON (((`featureaction`.`id` = `dependentactions_view`.`actionId`)
            AND (`feature`.`companyLegalUnit` = `dependentactions_view`.`companyLegalUnit`))))
        LEFT JOIN `membergrouptype` ON (`featureactionroletype`.`RoleType_id` = `membergrouptype`.`id`))
        LEFT JOIN `actionlimit` ON (((`actionlimit`.`Action_id` = `featureaction`.`id`)
            AND (`actionlimit`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))));

DROP VIEW IF EXISTS `feature_action_roles_view`;	
CREATE VIEW `feature_action_roles_view` AS
    SELECT
        `featureaction`.`Feature_id` AS `feature_code`,
        `feature_view`.`Name` AS `feature_name`,
        `feature_view`.`Type_Id` AS `feature_type_id`,
        `feature_view`.`Status_Id` AS `feature_status_id`,
        `featureaction`.`id` AS `action_code`,
        `featureaction`.`name` AS `action_name`,
        `featureaction`.`description` AS `action_description`,
        `far`.`RoleType_id` AS `action_role_type_id`,
        `featureaction`.`Type_id` AS `category`,
        `featureaction`.`accesspolicyId` AS `accesspolicyId`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
        `featureaction`.`status` AS `status`,
        `actionlimit`.`LimitType_id` AS `limitType_id`,
        `actionlimit`.`value` AS `value`,
        `featureaction`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (((`featureaction`
        LEFT JOIN `featureactionroletype` `far` ON (((`far`.`Action_id` = `featureaction`.`id`)
		AND (`far`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
        LEFT JOIN `feature_view` ON ((`feature_view`.`Code` = `featureaction`.`Feature_id`)))
        LEFT JOIN `actionlimit` ON (((`actionlimit`.`Action_id` = `featureaction`.`id`)
		AND (`actionlimit`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))));

ALTER TABLE `role` DROP PRIMARY KEY, ADD PRIMARY KEY (`id`, `companyLegalUnit`);
ALTER TABLE `rolepermission` DROP PRIMARY KEY, ADD PRIMARY KEY (`Role_id`, `Permission_id`, `companyLegalUnit`);
ALTER TABLE `userrole` DROP PRIMARY KEY, ADD PRIMARY KEY (`User_id`, `Role_id`, `companyLegalUnit`);

ALTER TABLE `rolecompositepermission` DROP PRIMARY KEY, ADD PRIMARY KEY (`Role_id`, `CompositePermission_id`, `companyLegalUnit`);

ALTER TABLE `rolepermissionou` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
DROP PRIMARY KEY,
ADD PRIMARY KEY (`roleId`, `permissionId`, `ouId`, `companyLegalUnit`);

ALTER TABLE `userrolecustomerrole`
ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL',
DROP PRIMARY KEY,
ADD PRIMARY KEY (`UserRole_id`, `CustomerRole_id`, `companyLegalUnit`);

DROP VIEW IF EXISTS `roles_view`;
CREATE VIEW `roles_view` AS
    SELECT
        `role`.`id` AS `role_id`,
        `role`.`Type_id` AS `roleType_id`,
        `role`.`Name` AS `role_Name`,
        `role`.`Description` AS `role_Desc`,
        `role`.`Status_id` AS `Status_id`,
        `role`.`companyLegalUnit` AS `companyLegalUnit`,
        `status`.`Description` AS `Status_Desc`,
        (SELECT
                COUNT(`rolepermission`.`Role_id`)
            FROM
                `rolepermission`
            WHERE
                ((`rolepermission`.`Role_id` = `role`.`id`)
                    AND (`rolepermission`.`companyLegalUnit` = `role`.`companyLegalUnit`))) AS `permission_Count`,
        (SELECT
                COUNT(`userrole`.`User_id`)
            FROM
                `userrole`
            WHERE
                (`userrole`.`Role_id` IN (SELECT
                        `rolepermission`.`Role_id`
                    FROM
                        `rolepermission`
                    WHERE
                        (`rolepermission`.`Role_id` = `role`.`id`))
                    AND
                    `userrole`.`companyLegalUnit` IN (SELECT
                        `rolepermission`.`companyLegalUnit`
                    FROM
                        `rolepermission`
                    WHERE
                        (`rolepermission`.`companyLegalUnit` = `role`.`companyLegalUnit`)))) AS `Users_Count`,
        (CASE `role`.`Status_id`
            WHEN 'SID_ACTIVE' THEN 'Active'
            ELSE 'Inactive'
        END) AS `Status`
    FROM
        (`role`
        JOIN `status` ON ((`role`.`Status_id` = `status`.`id`)));


DROP VIEW IF EXISTS `rolepermission_view`;
CREATE VIEW `rolepermission_view` AS
    SELECT 
        `role`.`Name` AS `Role_Name`,
        `role`.`Description` AS `Role_Description`,
        `role`.`Status_id` AS `Role_Status_id`,
        `rolepermission`.`Role_id` AS `Role_id`,
        `rolepermission`.`companyLegalUnit` AS `companyLegalUnit`,
        `permission`.`id` AS `Permission_id`,
        `permission`.`Type_id` AS `Permission_Type_id`,
        `permission`.`Status_id` AS `Permission_Status_id`,
        `permission`.`DataType_id` AS `DataType_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Description`,
        `permission`.`isComposite` AS `Permission_isComposite`,
        `permission`.`PermissionValue` AS `PermissionValue`,
        `permission`.`createdby` AS `Permission_createdby`,
        `permission`.`modifiedby` AS `Permission_modifiedby`,
        `permission`.`createdts` AS `Permission_createdts`,
        `permission`.`lastmodifiedts` AS `Permission_lastmodifiedts`,
        `permission`.`synctimestamp` AS `Permission_synctimestamp`,
        `permission`.`softdeleteflag` AS `Permission_softdeleteflag`
    FROM
        ((`rolepermission`
        JOIN `permission` ON ((`rolepermission`.`Permission_id` = `permission`.`id`)))
        JOIN `role` ON ((`role`.`id` = `rolepermission`.`Role_id`) and (`role`.`companyLegalUnit` = `rolepermission`.`companyLegalUnit`)));


DROP VIEW IF EXISTS `permissions_view`;
CREATE VIEW `permissions_view` AS
    SELECT 
        `permission`.`id` AS `Permission_id`,
        `permission`.`Type_id` AS `PermissionType_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Desc`,
        `permission`.`Status_id` AS `Status_id`,
        `permission`.`companyLegalUnit` AS `companyLegalUnit`,
        `status`.`Description` AS `Status_Desc`,
        (SELECT 
                COUNT(DISTINCT `rolepermission`.`Role_id`)
            FROM
                `rolepermission`
            WHERE
                (`rolepermission`.`Permission_id` = `permission`.`id`)) AS `Role_Count`,
        ((SELECT 
                COUNT(`userpermission`.`Permission_id`)
            FROM
                `userpermission`
            WHERE
                (`userpermission`.`Permission_id` = `permission`.`id`)) + (SELECT 
                COUNT(`userrole`.`User_id`)
            FROM
                `userrole`
            WHERE
                `userrole`.`Role_id` IN (SELECT 
                        `rolepermission`.`Role_id`
                    FROM
                        `rolepermission`
                    WHERE
                        (`rolepermission`.`Permission_id` = `permission`.`id`)))) AS `Users_Count`,
        (CASE `permission`.`Status_id`
            WHEN 'SID_ACTIVE' THEN 'Active'
            ELSE 'Inactive'
        END) AS `Status`
    FROM
        (`permission`
        JOIN `status` ON ((`permission`.`Status_id` = `status`.`id`)));


DROP VIEW IF EXISTS `get_all_features_view`;
CREATE VIEW `get_all_features_view` AS
    SELECT 
        `feature`.`id` AS `id`,
        `feature`.`name` AS `name`,
        `feature`.`description` AS `description`,
        `feature`.`Type_id` AS `Type_id`,
        `feature`.`Service_Fee` AS `Service_Fee`,
        `feature`.`Status_id` AS `Status_id`,
        `feature`.`companyLegalUnit` AS `companyLegalUnit`,
        `featureroletype`.`RoleType_id` AS `roleTypeId`,
        `featuredisplaynamedescription`.`Locale_id` AS `languageId`,
        `featuredisplaynamedescription`.`displayName` AS `displayName`,
        `featuredisplaynamedescription`.`displayDescription` AS `displayDescription`,
        `membergrouptype`.`description` AS `roleTypeName`,
        (SELECT 
                COUNT(DISTINCT `featureaction`.`id`)
            FROM
                `featureaction`
            WHERE
                ((`feature`.`id` = `featureaction`.`Feature_id`)
                    AND (`featureaction`.`Type_id` = 'MONETARY'))) AS `monetaryActions`,
        (SELECT 
                COUNT(DISTINCT `featureaction`.`id`)
            FROM
                `featureaction`
            WHERE
                ((`feature`.`id` = `featureaction`.`Feature_id`)
                    AND (`featureaction`.`Type_id` = 'NON_MONETARY'))) AS `nonMonetaryActions`
    FROM
        (((`feature`
        LEFT JOIN `featureroletype` ON ((`featureroletype`.`Feature_id` = `feature`.`id`)
            AND (`featureroletype`.`companyLegalUnit` = `feature`.`companyLegalUnit`)))
        LEFT JOIN `featuredisplaynamedescription` ON ((`featuredisplaynamedescription`.`Feature_id` = `feature`.`id`)
            AND (`featuredisplaynamedescription`.`companyLegalUnit` = `feature`.`companyLegalUnit`)))
        LEFT JOIN `membergrouptype` ON (`featureroletype`.`RoleType_id` = `membergrouptype`.`id`));

ALTER TABLE `bbrequest` ADD COLUMN `additionalMeta` LONGTEXT DEFAULT NULL;

DROP PROCEDURE IF EXISTS bbrequest_updatestatus_proc;
DELIMITER $$
CREATE PROCEDURE bbrequest_updatestatus_proc(
	IN `_requestId` VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_status` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_additionalMeta` LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    UPDATE `bbrequest` SET `status` = `_status` WHERE `requestId` = `_requestId`;
    -- ADP-7058 update additonal meta data as well
    IF `_additionalMeta` IS NOT NULL OR `_additionalMeta` != '' THEN
        UPDATE `bbrequest` SET `additionalMeta` = `_additionalMeta` WHERE `requestId` = `_requestId`;
    END IF;
  	SELECT * FROM `bbrequest` WHERE `requestId` = `_requestId`;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS bbrequest_updatecounter_proc;
DELIMITER $$
CREATE PROCEDURE bbrequest_updatecounter_proc(
    IN `_requestId` VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_counter` VARCHAR(16) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_additionalMeta` LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    SET @count = if(`_counter` = NULL OR `_counter` = '' , 0 , `_counter` * 1);
    SET @prevCount = 0;
    SET @reqCounts = 0;
    SELECT `requiredSets` into @reqCounts from `bbrequest` where `requestId` = `_requestId`;
    SELECT `receivedSets` into @prevCount from `bbrequest` where `requestId` = `_requestId`;
    IF @prevCount IS NULL THEN
        SET @prevCount = 0;
    END IF;
    SET @currCount = @prevCount + @count;
    UPDATE `bbrequest` SET `receivedSets` = @currCount WHERE `requestId` = `_requestId`;
    IF @currCount >= @reqCounts THEN
        UPDATE `bbrequest` set `status` = 'Approved' where `requestId` = `_requestId`;
    END IF;
    -- ADP-7058 update additonal meta data as well
    IF `_additionalMeta` IS NOT NULL OR `_additionalMeta` != '' THEN
        UPDATE `bbrequest` SET `additionalMeta` = `_additionalMeta` WHERE `requestId` = `_requestId`;
    END IF;
    SELECT * FROM `bbrequest` WHERE `requestId` = `_requestId`;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `bbrequest_updateadditionalmeta_proc`;
DELIMITER $$
CREATE PROCEDURE `bbrequest_updateadditionalmeta_proc`(
    IN _requestId VARCHAR(256) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _additionalMeta TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    UPDATE `bbrequest` set `additionalMeta` = _additionalMeta WHERE `requestId` = _requestId;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_signatory_eligible_users_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_signatory_eligible_users_proc`(
	IN _coreCustomerId VARCHAR(64) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _contractId VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SELECT DISTINCT
		`customer`.`id` AS `userId`,
		`customer`.`isCombinedUser` AS `isCombinedUser`,
		`customer`.`UserName` AS `userName`,
		`customer`.`FirstName` AS `firstName`,
		`customer`.`LastName` AS `lastName`,
		`membergroup`.`Name` AS `role`
	FROM `customer`
	LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
	LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id` )
		WHERE `customergroup`.`coreCustomerId`=_coreCustomerId  
		AND `customer`.`id` in (SELECT DISTINCT `customeraction`.`Customer_id` FROM `customeraction`
			WHERE `customeraction`.`contractId`=_contractId 
			AND `customeraction`.`coreCustomerId`=_coreCustomerId AND `customeraction`.`softdeleteflag` = '0'
			AND `customeraction`.`Action_id` IN (SELECT DISTINCT `featureaction`.`id` from `featureaction` WHERE (`featureaction`.`id` LIKE '%_APPROVE' OR `featureaction`.`id` LIKE '%_SELF_APPROVAL'))
			AND `customeraction`.`Customer_id` NOT IN 
				(SELECT `customersignatorygroup`.`customerId` FROM `customersignatorygroup` WHERE `customersignatorygroup`.`softdeleteflag` = '0' AND `customersignatorygroup`.`signatoryGroupId` IN 
					(SELECT `signatorygroup`.`signatoryGroupId` FROM `signatorygroup` WHERE `signatorygroup`.`softdeleteflag` = '0' AND `signatorygroup`.`contractId`=_contractId AND `signatorygroup`.`coreCustomerId`=_coreCustomerId)))
			ORDER BY `customer`.`id`;
END$$
DELIMITER ;


DROP procedure IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalqueue_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;
        
        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);
		
		IF @combinedIds is NULL THEN      
			SET @combinedIds = _customerId;
		ELSE 
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = "" OR @combinedIds = NULL, "''", @combinedIds);
        
        SET _transactionIds = if(_transactionIds = "" OR _transactionIds = NULL, "''", _transactionIds);
        SET _requestIds = if(_requestIds = "" OR _requestIds = NULL, "''", _requestIds);
       
        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN      
			SET @companyId = "";
		END IF;
        
        IF _featureactionlist is NULL THEN      
			LEAVE MAINLABEL;
		END IF;
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ",") FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN      
			SET @customerGroupIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE requestapprovalmatrix.isGroupRule = 0 
                                        AND FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        							AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
													OR
												(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
									);
		
		IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
		
        SET @groupIds = @customerGroupIds;
        do_this: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM signatorygrouprequestmatrix 
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0  
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN      
					SET @requestIds = "''";
				END IF;
				SET @approvalRequestIds = if(@approvalRequestIds = "" OR @approvalRequestIds IS NULL, @requestIds, CONCAT(@approvalRequestIds, CONCAT(',',@requestIds) ));
			SET @SubStrLen = LENGTH(SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = MID(@groupIds, @SubStrLen + 2, @strLen);
			IF LENGTH(@groupIds) <= 0 THEN
			  LEAVE do_this;
			END IF;
		END LOOP do_this;
        
        IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0);
        IF @monetaryActions is NULL THEN      
			SET @monetaryActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
        SET @requestIds = if(_requestIds = "''",@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = "''"
	        ,concat("FIND_IN_SET(bbrequest.requestId, \"",@requestIds,"\") ")
	        ,concat("FIND_IN_SET(bbrequest.transactionId, \"",_transactionIds, "\") AND  FIND_IN_SET(bbrequest.featureActionId, \"",@monetaryActions,"\")") );
        
		SET @select_statement = concat("SELECT 
					bbrequest.requestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    bbrequest.isGroupMatrix,
                    bbrequest.companyId,
					bbrequest.accountId,
                    bbrequest.additionalMeta,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (",@combinedIds,") THEN 'true'
						ELSE 'false'
					 END) as `amICreator`,
					(CASE 
						WHEN  ((bbrequest.requestId IN (",@approvalRequestIds,"))  
                        AND
                        (SELECT EXISTS(select id from customeraction where bbrequest.accountId = customeraction.Account_id and customeraction.Action_id = bbrequest.featureActionId AND customeraction.Customer_id IN (",@combinedIds,") and customeraction.contractId = SUBSTRING_INDEX(bbrequest.companyId, '_', 1) and customeraction.coreCustomerId = SUBSTRING_INDEX(bbrequest.companyId, '_', -1) and bbrequest.isGroupMatrix = 1 and  customeraction.isAllowed = 1 and customeraction.softdeleteflag =0))) 
                        THEN 'true' 
						ELSE 'false'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (",@alreadyApprovedIds,") THEN 'true'
						ELSE 'false'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = '0') 
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
									(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
									, 
									SUM(
										CASE approvalrule.numberOfApprovals
											WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
											WHEN NULL OR \"\" THEN 0
											ELSE approvalrule.numberOfApprovals
										END
									) 
								)
                        ELSE NULL
					END as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement;
    EXECUTE stmt; 
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;
ALTER TABLE `userroleservicedefinition` DROP PRIMARY KEY, ADD PRIMARY KEY (`UserRole_id`, `servicedefinitionId`, `companyLegalUnit`);

DROP VIEW IF EXISTS `internal_role_to_servicedefinition_mapping_view`;
CREATE VIEW `internal_role_to_servicedefinition_mapping_view` AS
    SELECT 
        `servicedefinition`.`id` AS `ServiceDefinition_id`,
        `servicedefinition`.`name` AS `ServiceDefinition_Name`,
        `servicedefinition`.`description` AS `ServiceDefinition_Description`,
        `servicedefinition`.`serviceType` AS `ServiceDefinition_Type_id`,
        `servicedefinition`.`status` AS `ServiceDefinition_Status_id`,
        `role`.`id` AS `InternalRole_id`,
        `role`.`Type_id` AS `InternalRole_Type_id`,
        `role`.`Status_id` AS `InternalRole_Status_id`,
        `role`.`Name` AS `InternalRole_Name`,
        `role`.`Description` AS `InternalRole_Description`,
        `role`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        ((`userroleservicedefinition`
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `userroleservicedefinition`.`servicedefinitionId`)))
        LEFT JOIN `role` ON (((`role`.`id` = `userroleservicedefinition`.`UserRole_id`)
            AND (`role`.`companyLegalUnit` = `userroleservicedefinition`.`companyLegalUnit`))));


ALTER TABLE `rolecompositeaction` DROP PRIMARY KEY, ADD PRIMARY KEY (`Role_id`, `CompositeAction_id`, `companyLegalUnit`);

DROP PROCEDURE IF EXISTS `rolePermissionDelete_proc`;
DELIMITER $$
CREATE PROCEDURE `rolePermissionDelete_proc`(
    IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
    IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
    IN _companyLegalUnit VARCHAR(1000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
DECLARE caid VARCHAR(50);
DECLARE isEnabled VARCHAR(10);
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM compositeaction c WHERE FIND_IN_SET(`Permission_id` COLLATE UTF8_GENERAL_CI, _PermissionIds));
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
SET SQL_SAFE_UPDATES = 0;
OPEN caids; 
MANAGECAIDS : LOOP
FETCH caids INTO caid, isEnabled;
IF finished = 1 THEN 
    LEAVE MANAGECAIDS;
END IF;
SET @caid_count =(SELECT COUNT(*) FROM compositeaction c,  rolepermission rp WHERE rp.Role_id COLLATE UTF8_GENERAL_CI = _roleId
AND rp.companyLegalUnit COLLATE UTF8_GENERAL_CI = _companyLegalUnit
AND rp.Permission_id COLLATE UTF8_GENERAL_CI =c.Permission_id COLLATE UTF8_GENERAL_CI
AND NOT FIND_IN_SET(rp.Permission_id COLLATE UTF8_GENERAL_CI ,_PermissionIds)
AND c.id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI);
IF @caid_count=0 THEN
DELETE FROM rolecompositeaction WHERE Role_id COLLATE UTF8_GENERAL_CI =_roleId AND companyLegalUnit COLLATE UTF8_GENERAL_CI =_companyLegalUnit AND CompositeAction_id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI;
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;
DELETE FROM rolepermission WHERE Role_id COLLATE UTF8_GENERAL_CI=_roleId AND companyLegalUnit COLLATE UTF8_GENERAL_CI=_companyLegalUnit AND FIND_IN_SET(Permission_id COLLATE UTF8_GENERAL_CI,_PermissionIds);
SET SQL_SAFE_UPDATES = 1;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `update_user_recent_currency`;
DELIMITER $$
CREATE PROCEDURE `update_user_recent_currency`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _currencyCode VARCHAR(10) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE rowWithGivenCustomerAndCurrency INT DEFAULT 0;
DECLARE lengthOfRecentCurrencies INT DEFAULT 0;
DECLARE recentCurrencyToDelete VARCHAR(60);

SELECT COUNT(*) INTO rowWithGivenCustomerAndCurrency FROM 
recentcurrencies rc WHERE rc.customerId = _customerId AND rc.quoteCurrencyCode = _currencyCode;

SELECT COUNT(*) INTO lengthOfRecentCurrencies FROM 
recentcurrencies rc WHERE rc.customerId = _customerId ;

SELECT  recentcurrencies.id INTO recentCurrencyToDelete FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId
													ORDER BY recentcurrencies.createdts asc limit 1;
IF rowWithGivenCustomerAndCurrency >= 1 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId AND recentcurrencies.quoteCurrencyCode = _currencyCode;
ELSEIF lengthOfRecentCurrencies >= 5 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.id = recentCurrencyToDelete COLLATE utf8_general_ci;
END IF;

END$$
DELIMITER ;

--INSERT INTO recentcurrencies (`id`, `customerId`, `quoteCurrencyCode`, `legalEntityId`) VALUES (concat( _currencyCode, _customerId ), _customerId, _currencyCode, _legalEntityId);




DROP procedure IF EXISTS `bulkassign_permissions_to_role`;
DELIMITER $$
CREATE PROCEDURE `bulkassign_permissions_to_role`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _permissions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPermissions = LENGTH(_permissions) - LENGTH(REPLACE(_permissions, '|', '')) + 1;
    if(@numOfPermissions > 0) then 
	insertPermissions : LOOP
	        SET index1 = index1 + 1;
	        IF index1 = @numOfPermissions + 1 THEN
	            LEAVE insertPermissions;
	        ELSE
	        SET @permissionsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_permissions, '|', index1), '|', -1 );
	        SET @permissionsData = CONCAT("'",@permissionsData,"'");
			SET @query = CONCAT('insert into rolepermission(role_id,permission_id,softdeleteflag,companyLegalUnit) 
				values (''',_roleId,''',',@permissionsData,',','0',',''',_companyLegalUnit,''');'); 
		
		PREPARE sql_query FROM @query; 
	            EXECUTE sql_query;
		
	         END IF;
    	END LOOP insertPermissions;
    END if;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `update_user_recent_currency`;
DELIMITER $$
CREATE PROCEDURE `update_user_recent_currency`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _currencyCode VARCHAR(10) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

DECLARE rowWithGivenCustomerAndCurrency INT DEFAULT 0;
DECLARE lengthOfRecentCurrencies INT DEFAULT 0;
DECLARE recentCurrencyToDelete VARCHAR(60);

SELECT COUNT(*) INTO rowWithGivenCustomerAndCurrency FROM 
recentcurrencies rc WHERE rc.customerId = _customerId AND rc.quoteCurrencyCode = _currencyCode;

SELECT COUNT(*) INTO lengthOfRecentCurrencies FROM 
recentcurrencies rc WHERE rc.customerId = _customerId ;

SELECT  recentcurrencies.id INTO recentCurrencyToDelete FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId
													ORDER BY recentcurrencies.createdts asc limit 1;
IF rowWithGivenCustomerAndCurrency >= 1 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.customerId = _customerId AND recentcurrencies.quoteCurrencyCode = _currencyCode;
ELSEIF lengthOfRecentCurrencies >= 5 THEN
	DELETE FROM recentcurrencies  WHERE recentcurrencies.id = recentCurrencyToDelete COLLATE utf8_general_ci;
END IF;

INSERT INTO recentcurrencies (`id`, `customerId`, `quoteCurrencyCode`, `legalEntityId`) VALUES (concat( _currencyCode, _customerId ), _customerId, _currencyCode, _legalEntityId);
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `GetExternalPayeesProc`;
DELIMITER $$
CREATE PROCEDURE `GetExternalPayeesProc`(IN userId VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,IN legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT * from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1 and ip.legalEntityId = legalEntityId)
UNION
SELECT * from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1 and ip.legalEntityId = legalEntityId)
UNION
SELECT * from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1 and ip.legalEntityId = legalEntityId);
END $$
DELIMITER ;

DROP procedure IF EXISTS `bulkpayment_template_po_create_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkpayment_template_po_create_proc`(
IN _povalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPOs = LENGTH(_povalues) - LENGTH(REPLACE(_povalues, '|', '')) + 1;
		insertPOs : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfPOs + 1 THEN
            LEAVE insertPOs;
        ELSE
        SET @posData = SUBSTRING_INDEX(SUBSTRING_INDEX(_povalues, '|', index1), '|', -1 );
            SET @query = CONCAT('INSERT INTO bulkpaymenttemplatepos(paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,featureActionId,companyId,roleId,status,createdby,beneficiaryName,paymentMethod,currency,amount,feesPaidBy,paymentReference,swift,beneficiaryNickName,beneficiaryAddress,accType,beneficiaryType,addToExistingFlag,beneficiaryIBAN,bankName,legalEntityId)
            VALUES (',@posData,');');
            PREPARE sql_query FROM @query;
            EXECUTE sql_query;
         END IF;
    END LOOP insertPOs;
END$$
DELIMITER ;

DROP procedure IF EXISTS `bulkpayment_request_po_create_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkpayment_request_po_create_proc`(
IN _povalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPOs = LENGTH(_povalues) - LENGTH(REPLACE(_povalues, '|', '')) + 1;
		insertPOs : LOOP
        SET index1 = index1 + 1;
        IF index1 = @numOfPOs + 1 THEN
            LEAVE insertPOs;
        ELSE
        SET @posData = SUBSTRING_INDEX(SUBSTRING_INDEX(_povalues, '|', index1), '|', -1 );
            SET @query = CONCAT('INSERT INTO bulkpaymentrequestpos(paymentrequestPOId,paymentrequestId,paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,bankName,swift,featureActionId,companyId,roleId,status,currency,amount,feesPaidBy,paymentReference,debitAccountIBAN,beneficiaryIBAN,beneficiaryName,beneficiaryNickName,beneficiaryAddress,accountWithBankBIC,customer,paymentMethod,accType,createdby,legalEntityId)
            VALUES (',@posData,');');
            PREPARE sql_query FROM @query;
            EXECUTE sql_query;
         END IF;
    END LOOP insertPOs;
END$$
DELIMITER ;
drop view if exists customeraccountsview;
CREATE VIEW `customeraccountsview` AS select distinct `accounts`.`Membership_id` AS `Membership_id`,
`accounts`.`MembershipName` AS `MembershipName`,
`accounts`.`TaxId` AS `Taxid`,
`customeraccounts`.`Customer_id` AS `Customer_id`,
`customeraccounts`.`Customer_id` AS `User_id`,
`accounts`.`Account_id` AS `Account_id`,
`accounts`.`isBusinessAccount` AS `isBusinessAccount`,
`accounts`.`Type_id` AS `Type_id`,
`accounts`.`UserName` AS `userName`,
`accounts`.`CurrencyCode` AS `currencyCode`,
`accounts`.`AccountHolder` AS `accountHolder`,
`accounts`.`error` AS `error`,
`accounts`.`Address` AS `Address`,
`accounts`.`Scheme` AS `Scheme`,
`accounts`.`Number` AS `number`,
`accounts`.`AvailableBalance` AS `availableBalance`,
`accounts`.`CurrentBalance` AS `currentBalance`,
`accounts`.`InterestRate` AS `interestRate`,
`accounts`.`AvailableCredit` AS `availableCredit`,
`accounts`.`MinimumDue` AS `minimumDue`,
`accounts`.`DueDate` AS `dueDate`,
`accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
`accounts`.`ClosingDate` AS `closingDate`,
`accounts`.`PaymentTerm` AS `paymentTerm`,`accounts`.`OpeningDate` AS `openingDate`,
`accounts`.`MaturityDate` AS `maturityDate`,`accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
`accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,`accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
`accounts`.`DividendRate` AS `dividendRate`,`accounts`.`DividendYTD` AS `dividendYTD`,
`accounts`.`EStatementmentEnable` AS `eStatementEnable`,
`customeraccounts`.`IsOrganizationAccount` AS `isOrganizationAccount`,
`customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,`accounts`.`StatusDesc` AS `statusDesc`,
`accounts`.`NickName` AS `nickName`,`accounts`.`OriginalAmount` AS `originalAmount`,
`accounts`.`OutstandingBalance` AS `outstandingBalance`,`accounts`.`PaymentDue` AS `paymentDue`,
`accounts`.`PaymentMethod` AS `paymentMethod`,
`accounts`.`SwiftCode` AS `swiftCode`,
`accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
`accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
`accounts`.`RoutingNumber` AS `routingNumber`,
`accounts`.`SupportBillPay` AS `supportBillPay`,
`accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
`accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
`accounts`.`SupportTransferTo` AS `supportTransferTo`,
`accounts`.`SupportDeposit` AS `supportDeposit`,
`accounts`.`UnpaidInterest` AS `unpaidInterest`,
`accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
`accounts`.`principalBalance` AS `principalBalance`,
`accounts`.`PrincipalValue` AS `principalValue`,
`accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
`accounts`.`phone` AS `phoneId`,`accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
`accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
`accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
`accounts`.`LastPaymentDate` AS `lastPaymentDate`,
`accounts`.`LastStatementBalance` AS `lastStatementBalance`,
`accounts`.`LateFeesDue` AS `lateFeesDue`,
`accounts`.`maturityAmount` AS `maturityAmount`,
`accounts`.`MaturityOption` AS `maturityOption`,
`accounts`.`payoffAmount` AS `payoffAmount`,
`accounts`.`PayOffCharge` AS `payOffCharge`,
`accounts`.`PendingDeposit` AS `pendingDeposit`,
`accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
`accounts`.`JointHolders` AS `jointHolders`,
`accounts`.`IsPFM` AS `isPFM`,`accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
`accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
`accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
`accounts`.`InterestEarned` AS `interestEarned`,`accounts`.`CurrentAmountDue` AS `currentAmountDue`,
`accounts`.`CreditLimit` AS `creditLimit`,`accounts`.`CreditCardNumber` AS `creditCardNumber`,
`accounts`.`BsbNum` AS `bsbNum`,`accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
`accounts`.`BondInterest` AS `bondInterest`,`accounts`.`AvailablePoints` AS `availablePoints`,
`accounts`.`AccountName` AS `accountName`,`accounts`.`email` AS `email`,
`accounts`.`IBAN` AS `IBAN`,`accounts`.`adminProductId` AS `adminProductId`,
`accounts`.`UpdatedBy` AS `UpdatedBy`,`accounts`.`LastUpdated` AS `LastUpdated`,
`accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,`bank`.`Description` AS `bankname`,
`accounts`.`AccountPreference` AS `accountPreference`,`accounttype`.`transactionLimit` AS `transactionLimit`,
`accounttype`.`transferLimit` AS `transferLimit`,`accounttype`.`rates` AS `rates`,`accounttype`.`termsAndConditions` AS `termsAndConditions`,
`accounttype`.`TypeDescription` AS `typeDescription`,`accounttype`.`supportChecks` AS `supportChecks`,
`accounttype`.`displayName` AS `displayName`,`accounts`.`accountSubType` AS `accountSubType`,
`accounts`.`description` AS `description`,`accounts`.`schemeName` AS `schemeName`,
`accounts`.`identification` AS `identification`,`accounts`.`secondaryIdentification` AS `secondaryIdentification`,
`accounts`.`servicerSchemeName` AS `servicerSchemeName`,`accounts`.`servicerIdentification` AS `servicerIdentification`,
`accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,`accounts`.`dataType` AS `dataType`,
`accounts`.`dataDateTime` AS `dataDateTime`,
`accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
`accounts`.`dataCreditLineType` AS `dataCreditLineType`,
`accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
`accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`, 
`accounts`.`companyLegalUnit` AS `legalEntityId` from (((((`accounts` join `customeraccounts`) join `accounttype`) left join `membershipaccounts` on((`accounts`.`Account_id` = `membershipaccounts`.`accountId`))) left join `membership` on((`membership`.`id` = `membershipaccounts`.`membershipId`))) left join `bank` on((`accounts`.`Bank_id` = `bank`.`id`))) where ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`) and (`accounts`.`Type_id` = `accounttype`.`TypeID`));

DROP PROCEDURE IF EXISTS `GetExternalPayeesProc`;
DELIMITER $$
CREATE PROCEDURE `GetExternalPayeesProc`(IN userId VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,IN legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
IF legalEntityId != "" THEN
SELECT * from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT * from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId
UNION
SELECT * from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1) and ip.legalEntityId = legalEntityId;
ELSE
SELECT * from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT * from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1)
UNION
SELECT * from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT" and isAllowed = 1);
END IF;
END $$
DELIMITER ;


DROP VIEW IF EXISTS `servicedefinition_features_actions_view`;
CREATE VIEW `servicedefinition_features_actions_view` AS
  SELECT 
        `servicedefinitionactionlimit`.`serviceDefinitionId` AS `serviceDefinitionId`,
        `servicedefinitionactionlimit`.`actionId` AS `actionId`,
        `servicedefinitionactionlimit`.`limitTypeId` AS `limitTypeId`,
        `servicedefinitionactionlimit`.`value` AS `value`,
        `servicedefinitionactionlimit`.`id` AS `serviceDefinitionActionLimitId`,
        `servicedefinitionactionlimit`.`softdeleteflag` AS `softdelete`,
        `servicedefinition`.`serviceType` AS `serviceType`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `servicedefinition`.`description` AS `serviceDefinitionDescription`,
        `featureaction`.`name` AS `actionName`,
        `featureaction`.`description` AS `actionDescription`,
        `featureaction`.`Type_id` AS `actionTypeId`,
        `featureaction`.`Feature_id` AS `featureId`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`DisplaySequence` AS `actionDisplaysequence`,
        `featureaction`.`dependency` AS `actionDependency`,
        `featureaction`.`status` AS `actionStatus`,
        `accesspolicy`.`name` AS `accessPolicy`,
        `featureaction`.`accesspolicyId` AS `accessPolicyId`,
        `featureaction`.`limitgroupId` AS `limitGroupId`,
        `limitgroup`.`name` AS `limitGroup`,
        `actionlevel`.`name` AS `actionlevel`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
		`dependentactions`.`dependentactionId` AS `dependentactionId`,
        `dependentactions`.`featureId` AS `dependentFeatureId`,
        `dependentactions`.`actionName` AS `dependentActionName`,
        `dependentactions`.`featureName` AS `dependentFeatureName`,
        `feature`.`name` AS `featureName`,
        `feature`.`description` AS `featureDescription`,
        `feature`.`Type_id` AS `featureTypeId`,
        `feature`.`Status_id` AS `featureStatusId`,
        `feature`.`DisplaySequence` AS `featureDisplaysequence`,
        `feature`.`isPrimary` AS `featureIsPrimary`,
		`feature`.`companyLegalUnit` as `companyLegalUnit`
    FROM
        (((((((`servicedefinitionactionlimit`
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `servicedefinitionactionlimit`.`serviceDefinitionId`  AND `servicedefinition`.`companyLegalUnit` = `servicedefinitionactionlimit`.`companyLegalUnit`)))
        LEFT JOIN `featureaction` ON ((`featureaction`.`id` = `servicedefinitionactionlimit`.`actionId`  AND `featureaction`.`companyLegalUnit` = `servicedefinitionactionlimit`.`companyLegalUnit`)))
		LEFT JOIN `dependentactions` ON ((`featureaction`.`id` = `dependentactions`.`actionId`)))
        LEFT JOIN `feature` ON ((`feature`.`id` = `featureaction`.`Feature_id`   AND `featureaction`.`companyLegalUnit` = `feature`.`companyLegalUnit`)))
        LEFT JOIN `accesspolicy` ON ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`)))
        LEFT JOIN `limitgroup` ON ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)))
        LEFT JOIN `actionlevel` ON ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)));
		
		
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
    JOIN `role` `r` ON (`r`.`id` = `rp`.`Role_id`))
    WHERE `p`.`Status_id`='SID_ACTIVE' AND `r`.`Status_id`='SID_ACTIVE' AND FIND_IN_SET(`rp`.`Role_id`,_roleIds) ORDER BY `id`;
END $$
DELIMITER ;

ALTER TABLE `featureaction` 
DROP FOREIGN KEY `FK_action_feature_id`;
ALTER TABLE `featureaction` 
DROP INDEX `FK_action_feature_id_idx` ,
ADD INDEX `FK_action_feature_id_idx` (`Feature_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `featureaction` 
ADD CONSTRAINT `FK_action_feature_id`
FOREIGN KEY (`Feature_id` , `companyLegalUnit`)
REFERENCES `feature` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

DELETE FROM `featureroletype` WHERE (`RoleType_id` = 'TYPE_ID_WEALTH') and (`Feature_id` = 'MANAGE_E_STATEMENTS') and (`companyLegalUnit` = 'ALL');

ALTER TABLE `featureroletype` 
DROP FOREIGN KEY `FK_featureroletype_Feature_id`;
ALTER TABLE `featureroletype` 
DROP INDEX `FK_featureroletype_Feature_id_idx` ,
ADD INDEX `FK_featureroletype_Feature_id_idx` (`Feature_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `featureroletype` 
ADD CONSTRAINT `FK_featureroletype_Feature_id`
FOREIGN KEY (`Feature_id` , `companyLegalUnit`)
REFERENCES `feature` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `featuredisplaynamedescription` 
DROP FOREIGN KEY `FK_featuredisplaynamedescription_Feature_id`;

ALTER TABLE `featuredisplaynamedescription` 
ADD UNIQUE INDEX `featuredisplaynamedescription_unique_idx` (`Feature_id` ASC, `Locale_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `featuredisplaynamedescription` 
ADD CONSTRAINT `FK_featuredisplaynamedescription_Feature_id`
FOREIGN KEY (`Feature_id` , `companyLegalUnit`)
REFERENCES `feature` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

DELETE FROM `featureactionroletype` WHERE (`RoleType_id` = 'TYPE_ID_WEALTH') and (`Action_id` = 'MANAGE_E_STATEMENTS') and (`companyLegalUnit` = 'ALL');

ALTER TABLE `featureactionroletype` 
DROP FOREIGN KEY `FK_featureactionroletype_Action_id`;
ALTER TABLE `featureactionroletype` 
DROP INDEX `FK_featureactionroletype_Action_id_idx` ,
ADD INDEX `FK_featureactionroletype_Action_id_idx` (`Action_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `featureactionroletype` 
ADD CONSTRAINT `FK_featureactionroletype_Action_id`
FOREIGN KEY (`Action_id` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `actiondisplaynamedescription` 
DROP FOREIGN KEY `FK_actiondisplaynamedescription_Action_id`;

ALTER TABLE `actiondisplaynamedescription` 
ADD UNIQUE INDEX `actiondisplaynamedescription_unique_idx` (`Action_id` ASC, `Locale_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `actiondisplaynamedescription` 
ADD CONSTRAINT `FK_actiondisplaynamedescription_Action_id`
FOREIGN KEY (`Action_id` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

DELETE FROM `groupactionlimit` WHERE (`id` = '090fe22f-4725-4131-ade0-cff2a3450c14');

ALTER TABLE `groupactionlimit` 
DROP FOREIGN KEY `FK_groupactionlimit_Action`;
ALTER TABLE `groupactionlimit` 
DROP INDEX `IXFK_groupactionlimit_Action` ,
ADD INDEX `IXFK_groupactionlimit_Action` (`Action_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `groupactionlimit` 
ADD CONSTRAINT `FK_groupactionlimit_Action`
FOREIGN KEY (`Action_id` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `groupactionlimit` 
DROP INDEX `UNIQUE_groupactionlimit` ,
ADD UNIQUE INDEX `UNIQUE_groupactionlimit` (`Group_id`, `Action_id`, `LimitType_id`, `companyLegalUnit`);

ALTER TABLE `servicedefinitionactionlimit` 
DROP FOREIGN KEY `FK_servicedefinitionactionlimit_action`;
ALTER TABLE `servicedefinitionactionlimit` 
DROP INDEX `IXFK_servicedefinitionactionlimit_action` ,
ADD INDEX `IXFK_servicedefinitionactionlimit_action` (`actionId` ASC, `companyLegalUnit` ASC);

ALTER TABLE `servicedefinitionactionlimit` 
ADD CONSTRAINT `FK_servicedefinitionactionlimit_action`
FOREIGN KEY (`actionId` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `servicedefinitionactionlimit` 
DROP INDEX `UNIQUE_servicedefinitionactionlimit` ,
ADD UNIQUE INDEX `UNIQUE_servicedefinitionactionlimit` (`serviceDefinitionId`,`actionId`,`limitTypeId`, `companyLegalUnit`);

ALTER TABLE `actionlimit` 
DROP FOREIGN KEY `FK_actionlimit_featureaction`;
ALTER TABLE `actionlimit` 
DROP INDEX `IDX_actionlimit_actionId_limitTypeId` ,
ADD INDEX `IDX_actionlimit_actionId_limitTypeId` (`Action_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `actionlimit` 
ADD CONSTRAINT `FK_actionlimit_featureaction`
FOREIGN KEY (`Action_id` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

DELETE FROM `customeraction` WHERE (`id` = '8344def7-45b5-11eb-b85d-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:51');
DELETE FROM `customeraction` WHERE (`id` = '839cbed1-45b5-11eb-b85d-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:51');
DELETE FROM `customeraction` WHERE (`id` = '8b45fad7-45b5-11eb-b85d-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:50');
DELETE FROM `customeraction` WHERE (`id` = '8bc85ffd-45b5-11eb-b85d-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:50');
DELETE FROM `customeraction` WHERE (`id` = '9e4188c1-ea71-4336-a45d-d85e8657bca4') and (`synctimestamp` = '2022-11-16 17:09:33');
DELETE FROM `customeraction` WHERE (`id` = 'df68857a-4833-11eb-b6c6-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:51');
DELETE FROM `customeraction` WHERE (`id` = 'e0cd1376-4833-11eb-b6c6-0205857feb80') and (`synctimestamp` = '2022-11-16 17:09:51');

ALTER TABLE `customeraction` 
DROP FOREIGN KEY `FK_CustomerActionLimit_Action`;
ALTER TABLE `customeraction` 
DROP INDEX `IXFK_CustomerActionLimit_Service` ,
ADD INDEX `IXFK_CustomerActionLimit_Service` (`Action_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `customeraction` 
ADD CONSTRAINT `FK_CustomerActionLimit_Action`
FOREIGN KEY (`Action_id` , `companyLegalUnit`)
REFERENCES `featureaction` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `userrole` 
DROP FOREIGN KEY `FK_UserRole_Role`;
ALTER TABLE `userrole` 
DROP INDEX `IXFK_UserRole_Role` ,
ADD INDEX `IXFK_UserRole_Role` (`Role_id` ASC, `companyLegalUnit` ASC);

ALTER TABLE `userrole` 
ADD CONSTRAINT `FK_UserRole_Role`
FOREIGN KEY (`Role_id` , `companyLegalUnit`)
REFERENCES `role` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `rolepermission` 
DROP FOREIGN KEY `FK_RolePermission_Role`;

ALTER TABLE `rolepermission` 
ADD CONSTRAINT `FK_RolePermission_Role`
FOREIGN KEY (`Role_id` , `companyLegalUnit`)
REFERENCES `role` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `rolecompositeaction` 
DROP FOREIGN KEY `FK_RoleCompositeAction_Role`;

ALTER TABLE `rolecompositeaction` 
ADD CONSTRAINT `FK_RoleCompositeAction_Role`
FOREIGN KEY (`Role_id` , `companyLegalUnit`)
REFERENCES `role` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;
  
ALTER TABLE `userroleservicedefinition` 
DROP FOREIGN KEY `userroleservicedefinition_userrole_id`;

ALTER TABLE `userroleservicedefinition` 
ADD CONSTRAINT `userroleservicedefinition_userrole_id`
FOREIGN KEY (`UserRole_id` , `companyLegalUnit`)
REFERENCES `role` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE `rolecompositepermission` 
DROP FOREIGN KEY `FK_RoleCompositePermission_Role`;

ALTER TABLE `rolecompositepermission` 
ADD CONSTRAINT `FK_RoleCompositePermission_Role`
FOREIGN KEY (`Role_id` , `companyLegalUnit`)
REFERENCES `role` (`id` , `companyLegalUnit`)
ON DELETE CASCADE
ON UPDATE CASCADE;

DROP procedure IF EXISTS `get_actions_with_approvefeatureaction_proc`;

DELIMITER $$

CREATE PROCEDURE `get_actions_with_approvefeatureaction_proc`(
IN _featureActions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _companyLegalUnit varchar(255) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @actionsList = (SELECT group_concat(id SEPARATOR ",") from featureaction WHERE 
                      FIND_IN_SET(id,_featureActions) AND companyLegalUnit = _companyLegalUnit AND
					(!ISNULL(featureaction.approveFeatureAction) || featureaction.approveFeatureAction != ''));

select @actionsList As actions;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_signatorygroup_for_customer_and_request_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroup_for_customer_and_request_proc`(
	IN _requestId VARCHAR(256),
    IN _customerId VARCHAR(256)
)
BEGIN
	SET @signatoryGroupList = (SELECT GROUP_CONCAT(REPLACE((REPLACE(REPLACE(groupList, ']', ''''),'[', '''')), ',', ''',''')) as `groupList` FROM `signatorygroupmatrix` WHERE `approvalMatrixId` IN (SELECT `approvalMatrixId` FROM `requestapprovalmatrix` WHERE `requestId` = _requestId AND `softdeleteflag`='0') AND `softdeleteflag`='0');
	SET @sqlStmt = CONCAT('SELECT 
					csg.customerSignatoryGroupId,
                    csg.signatoryGroupId,
                    sg.signatoryGroupName,
                    csg.customerId,
                    csg.createdby,
                    csg.createdts,
                    csg.modifiedby,
                    csg.lastmodifiedts,
                    csg.synctimestamp,
                    csg.softdeleteflag FROM customersignatorygroup csg
					INNER JOIN signatorygroup sg ON csg.signatoryGroupId = sg.signatoryGroupId
						WHERE csg.signatoryGroupId IN (', @signatoryGroupList, ')
						AND csg.softdeleteflag = 0
						AND sg.softdeleteflag = 0
						AND csg.customerId = \'', _customerId , '\'');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;

CREATE OR REPLACE VIEW `groups_view` AS
    SELECT 
        `membergroup`.`id` AS `Group_id`,
        `membergroup`.`Type_id` AS `Type_id`,
        `membergrouptype`.`description` AS `Type_Name`,
        `membergroup`.`Description` AS `Group_Desc`,
        `membergroup`.`Status_id` AS `Status_id`,
        `membergroup`.`Name` AS `Group_Name`,
        `membergroup`.`companyLegalUnit` AS `companyLegalUnit`,
        `membergroup`.`isEAgreementActive` AS `isEAgreementActive`,
        `membergroup`.`isApplicabletoAllServices` AS `isApplicabletoAllServices`,
        (SELECT 
                COUNT(`groupentitlement`.`Group_id`)
            FROM
                `groupentitlement`
            WHERE
                (`groupentitlement`.`Group_id` = `membergroup`.`id`)) AS `Entitlements_Count`,
        (SELECT 
                COUNT(DISTINCT `customergroup`.`Customer_id`)
            FROM
                `customergroup`
            WHERE
                (`customergroup`.`Group_id` = `membergroup`.`id`)) AS `Customers_Count`,
        (CASE `membergroup`.`Status_id`
            WHEN 'SID_ACTIVE' THEN 'Active'
            ELSE 'Inactive'
        END) AS `Status`
    FROM
        (`membergroup`
        JOIN `membergrouptype` ON ((`membergroup`.`Type_id` = `membergrouptype`.`id`)));

DROP VIEW IF EXISTS `feature_actions_view`;	
CREATE VIEW `feature_actions_view` AS
    SELECT 
        `featureaction`.`id` AS `id`,
        `featureaction`.`Feature_id` AS `Feature_id`,
        `featureaction`.`name` AS `action_name`,
        `featureaction`.`description` AS `action_description`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`notes` AS `notes`,
        `featureaction`.`Type_id` AS `action_Type_id`,
        `featureaction`.`DisplaySequence` AS `action_displaysequence`,
        `featureaction`.`dependency` AS `action_dependency`,
        `feature`.`Status_id` AS `feature_status_id`,
        `feature`.`name` AS `feature_name`,
        `feature`.`description` AS `feature_description`,
        `feature`.`Type_id` AS `feature_Type_id`,
        `feature`.`DisplaySequence` AS `feature_displaysequence`,
        `feature`.`isPrimary` AS `feature_isPrimary`,
        `feature`.`companyLegalUnit` AS `companyLegalUnit`,
        `actionlimit`.`LimitType_id` AS `LimitType_id`,
        `actionlimit`.`value` AS `value`
    FROM
        ((`featureaction`
        LEFT JOIN `feature` ON (((`feature`.`id` = `featureaction`.`Feature_id`)
		AND (`feature`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
        LEFT JOIN `actionlimit` ON (((`actionlimit`.`Action_id` = `featureaction`.`id`)
		AND (`actionlimit`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))));
			
DROP VIEW IF EXISTS `feature_details_view`;
CREATE VIEW `feature_details_view` AS
    SELECT 
        `feature`.`id` AS `id`,
        `feature`.`name` AS `name`,
        `feature`.`description` AS `description`,
        `feature`.`Type_id` AS `Type_id`,
        `feature`.`Status_id` AS `Status_id`,
        `featuredisplaynamedescription`.`Locale_id` AS `Locale_id`,
        `featuredisplaynamedescription`.`displayName` AS `displayName`,
        `featuredisplaynamedescription`.`displayDescription` AS `displayDescription`,
		`feature`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        (`feature`
        LEFT JOIN `featuredisplaynamedescription` ON (((`featuredisplaynamedescription`.`Feature_id` = `feature`.`id`)
		 AND (`featuredisplaynamedescription`.`companyLegalUnit` = `feature`.`companyLegalUnit`))));

DROP VIEW IF EXISTS `group_features_actions_view`;
CREATE VIEW `group_features_actions_view` AS
    SELECT 
        `groupactionlimit`.`Group_id` AS `Group_id`,
        `groupactionlimit`.`Action_id` AS `Action_id`,
        `groupactionlimit`.`LimitType_id` AS `LimitType_id`,
        `groupactionlimit`.`value` AS `value`,
        `groupactionlimit`.`id` AS `groupactionlimit_id`,
        `groupactionlimit`.`softdeleteflag` AS `softdelete`,
        `membergroup`.`Type_id` AS `Type_id`,
        `membergroup`.`Name` AS `Group_name`,
        `membergroup`.`Description` AS `Group_description`,
        `featureaction`.`name` AS `Action_name`,
        `featureaction`.`description` AS `Action_description`,
        `featureaction`.`Type_id` AS `Action_Type_id`,
        `featureaction`.`Feature_id` AS `Feature_id`,
        `featureaction`.`isMFAApplicable` AS `isMFAApplicable`,
        `featureaction`.`isAccountLevel` AS `isAccountLevel`,
        `featureaction`.`isPrimary` AS `isPrimary`,
        `featureaction`.`DisplaySequence` AS `Action_displaysequence`,
        `featureaction`.`dependency` AS `Action_dependency`,
        `featureaction`.`status` AS `actionStatus`,
        `accesspolicy`.`name` AS `accessPolicy`,
        `featureaction`.`accesspolicyId` AS `accessPolicyId`,
        `featureaction`.`limitgroupId` AS `limitGroupId`,
        `limitgroup`.`name` AS `limitGroup`,
        `actionlevel`.`name` AS `actionlevel`,
        `featureaction`.`actionlevelId` AS `actionlevelId`,
        `feature`.`name` AS `Feature_name`,
        `feature`.`description` AS `Feature_description`,
        `feature`.`Type_id` AS `Feature_Type_id`,
        `feature`.`Status_id` AS `Feature_Status_id`,
        `feature`.`DisplaySequence` AS `Feature_displaysequence`,
        `feature`.`isPrimary` AS `Feature_isPrimary`
    FROM
        ((((((`groupactionlimit`
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `groupactionlimit`.`Group_id`)))
        LEFT JOIN `featureaction` ON (((`featureaction`.`id` = `groupactionlimit`.`Action_id`)
		AND (`featureaction`.`companyLegalUnit` = `groupactionlimit`.`companyLegalUnit`))))
        LEFT JOIN `feature` ON (((`feature`.`id` = `featureaction`.`Feature_id`)
		AND (`feature`.`companyLegalUnit` = `featureaction`.`companyLegalUnit`))))
        LEFT JOIN `accesspolicy` ON ((`featureaction`.`accesspolicyId` = `accesspolicy`.`id`)))
        LEFT JOIN `limitgroup` ON ((`featureaction`.`limitgroupId` = `limitgroup`.`id`)))
        LEFT JOIN `actionlevel` ON ((`featureaction`.`actionlevelId` = `actionlevel`.`id`)))
    ORDER BY `feature`.`name`;	

DROP VIEW IF EXISTS `internaluserdetails_view`;
CREATE VIEW `internaluserdetails_view` AS
    SELECT 
        `systemuser`.`id` AS `id`,
        `systemuser`.`Username` AS `Username`,
        `systemuser`.`Email` AS `Email`,
        `systemuser`.`Status_id` AS `Status_id`,
        `systemuser`.`Password` AS `Password`,
        `systemuser`.`Code` AS `Code`,
        `systemuser`.`FirstName` AS `FirstName`,
        `systemuser`.`MiddleName` AS `MiddleName`,
        `systemuser`.`LastName` AS `LastName`,
        `systemuser`.`FailedCount` AS `FailedCount`,
        `systemuser`.`LastPasswordChangedts` AS `LastPasswordChangedts`,
        `systemuser`.`ResetpasswordLink` AS `ResetpasswordLink`,
        `systemuser`.`ResetPasswordExpdts` AS `ResetPasswordExpdts`,
        `systemuser`.`lastLogints` AS `lastLogints`,
        `systemuser`.`createdby` AS `createdby`,
        `systemuser`.`createdts` AS `createdts`,
        `systemuser`.`modifiedby` AS `modifiedby`,
        `systemuser`.`lastmodifiedts` AS `lastmodifiedts`,
        `systemuser`.`synctimestamp` AS `synctimestamp`,
        `systemuser`.`softdeleteflag` AS `softdeleteflag`,
        `userrole`.`Role_id` AS `Role_id`,
        `userrole`.`hasSuperAdminPrivilages` AS `hasSuperAdminPrivilages`,
        `role`.`Name` AS `Role_Name`,
        `role`.`Status_id` AS `Role_Status_id`,
		`role`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        ((`systemuser`
        LEFT JOIN `userrole` ON ((`userrole`.`User_id` = `systemuser`.`id`)))
        LEFT JOIN `role` ON ((`userrole`.`Role_id` = `role`.`id`)));	

DROP VIEW IF EXISTS `userpermission_view`;	
CREATE VIEW `userpermission_view` AS
    SELECT 
        `userrole`.`User_id` AS `User_id`,
        `userrole`.`Role_id` AS `Role_id`,
        `rolepermission`.`Permission_id` AS `Permission_id`,
        `permission`.`Name` AS `Permission_Name`,
        `permission`.`Description` AS `Permission_Desc`,
        `permission`.`isComposite` AS `Permission_isComposite`,
		`rolepermission`.`companyLegalUnit` AS `companyLegalUnit`
    FROM
        ((`userrole`
        JOIN `rolepermission` ON ((`userrole`.`Role_id` = `rolepermission`.`Role_id`)))
        JOIN `permission` ON ((`permission`.`id` = `rolepermission`.`Permission_id`)));	

DROP procedure IF EXISTS `group_actions_proc`;
DELIMITER $$
CREATE PROCEDURE `group_actions_proc`(
in _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _isOnlyPremissions varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

if _isOnlyPremissions = 'true' then
    select DISTINCT `groupactionlimit`.`Action_id` AS `actionId` FROM `groupactionlimit` where `groupactionlimit`.`Group_id` = _groupId;
else
    SET @select_statement = concat("SELECT
        `groupactionlimit`.`Group_id` AS `groupId`,
        `groupactionlimit`.`LimitType_id` AS `limitTyeId`,
        `groupactionlimit`.`value` AS `value`,
        `featureaction`.`id` AS `actionId`,
        `featureaction`.`Type_id` AS `actionType`,
        `featureaction`.`name` AS `actionName`,
        `featureaction`.`description` AS `actionDescription`,    
        `feature`.`id` AS `featureId`
    FROM
        (`groupactionlimit`
        LEFT JOIN `featureaction` ON (`featureaction`.`id` = `groupactionlimit`.`Action_id` and  `featureaction`.`companyLegalUnit`= `groupactionlimit`.`companyLegalUnit` )
        LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id` and `featureaction`.`companyLegalUnit`= `feature`.`companyLegalUnit`))
        where `feature`.`Status_id` = 'SID_FEATURE_ACTIVE' and `groupactionlimit`.`Group_id`=",quote(_groupId));


       IF (_actionType != '' ) THEN
            set @select_statement =  concat(@select_statement ," and `featureaction`.`Type_id` = ",quote(_actionType));
        END IF;
        
         IF (_actionId != '' ) THEN
            set @select_statement =  concat(@select_statement ," and `featureaction`.`id` = ",quote(_actionId));
        END IF;
        PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
end if;
END $$
DELIMITER ;		

DROP procedure IF EXISTS `servicedefinitionactions_get_proc`;
DELIMITER $$
CREATE PROCEDURE `servicedefinitionactions_get_proc`(
IN _serviceDefinitionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

   select feature.id as featureId, feature.name as featureName , feature.description as featureDescription ,
    feature.Status_id as featureStatus ,
    featureaction.id as actionId , featureaction.name as actionName , featureaction.description as actionDescription,
    featureaction.status as actionStatus
    from feature
    left join featureaction on (featureaction.Feature_id = feature.id and featureaction.companyLegalUnit=feature.companyLegalUnit)
    where featureaction.id in ( select servicedefinitionactionlimit.actionId
    from servicedefinitionactionlimit where
    servicedefinitionactionlimit.serviceDefinitionId=_serviceDefinitionId and servicedefinitionactionlimit.companyLegalUnit=featureaction.companyLegalUnit);    

END $$
DELIMITER ;	


DROP procedure IF EXISTS `useractions_create_proc`;
DELIMITER $$
CREATE PROCEDURE `useractions_create_proc`(
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
               SET @limitvalue = (SELECT value FROM contractactionlimit WHERE actionId = featureActionId  COLLATE utf8_general_ci
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
END $$
DELIMITER ;	

ALTER TABLE `bbrequest`
CHANGE COLUMN `requiredSets` `requiredSets` INT(11) NOT NULL DEFAULT 0 ,
CHANGE COLUMN `receivedSets` `receivedSets` INT(11) NOT NULL DEFAULT 0 ;

DROP PROCEDURE IF EXISTS `fetch_accountlevelcustomerlimits_for_featureaction_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_accountlevelcustomerlimits_for_featureaction_proc`(
    IN `_customerId`		VARCHAR(128) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN `_featureActionId`	TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINEXEC : BEGIN
	SET @orgId = (SELECT DISTINCT(`Organization_Id`) FROM `customer` WHERE `id` = `_customerId`);
    SET @sqlStmt = CONCAT('SELECT 
			`ca`.`Customer_id` AS `customerId`,
			`cg`.`Group_id` AS `roleId`,
			`sdal`.`serviceDefinitionId`,',
			IF(@orgId IS NULL OR @orgId = '', ' ', '`oal`.`Organisation_id` AS `organisationId`,'),
			'`ca`.`Account_id` AS `accountId`,
			`ca`.`contractId`,
			`ca`.`coreCustomerId`,
			`al`.`Action_id` AS `baseActionId`,
			`sdal`.`actionId` AS `serviceDefActionId`,',
            IF(@orgId IS NULL OR @orgId = '', '', '`oal`.`Action_id` AS `organisationActionId`,'),
			'`cal`.`actionId` AS `contractActionId`,
			`gal`.`Action_id` AS `roleActionId`,
			`al`.`LimitType_id` AS `baseLimitTypeId`,
			`sdal`.`limitTypeId` AS `serviceDefLimitTypeId`,',
            IF(@orgId IS NULL OR @orgId = '', '', '`oal`.`LimitType_id` AS `organisationLimitTypeId`,'),
			'`cal`.`limitTypeId` AS `contractLimitTypeId`,
			`gal`.`LimitType_id` AS `roleLimitTypeId`,
            `fa`.`limitgroupId` AS `limitGroupId`,
			`al`.`value` AS `baseLimitValue`,
			`sdal`.`value` AS `serviceDefLimitValue`,',
            IF(@orgId IS NULL OR @orgId = '', '', '`oal`.`value` AS `organisationLimitValue`,'),
			'`cal`.`value` AS `contractLimitValue`,
			`gal`.`value` AS `roleLimitValue`,
			LEAST(`al`.`value`, `gal`.`value`, `sdal`.`value`, `cal`.`value`', IF(@orgId IS NULL OR @orgId = '', '', ', `oal`.`value`'), ') AS `minLimitValue`
			FROM `customeraccounts` AS `ca`
		INNER JOIN `customergroup` AS `cg` ON 
			`ca`.`Customer_id` = `cg`.`Customer_id` AND `ca`.`Customer_id` = \'', `_customerId` , '\'
		INNER JOIN `groupactionlimit` AS `gal` ON 
			`gal`.`Group_id` = `cg`.`Group_id` AND `gal`.`Action_id` = \'', `_featureActionId`, '\'
		INNER JOIN `contract` AS `c` ON 
			`c`.`id` = `ca`.`contractId`
		INNER JOIN `servicedefinitionactionlimit` AS `sdal` ON 
			`sdal`.`serviceDefinitionId` = `c`.`servicedefinitionId` AND `sdal`.`actionId` = \'', `_featureActionId`,'\' AND `sdal`.`actionId` = `gal`.`Action_id` AND `sdal`.`limitTypeId` = `gal`.`LimitType_id`
		INNER JOIN `contractactionlimit` AS `cal` ON
			`cal`.`contractId` = `ca`.`contractId` AND `cal`.`actionId` = \'', `_featureActionId`,'\' AND `cal`.`actionId` = `sdal`.`actionId` AND `cal`.`limitTypeId` = `sdal`.`limitTypeId`
		INNER JOIN `actionlimit` AS `al` ON
			`al`.`Action_id` = \'', `_featureActionId`,'\' AND `al`.`Action_id` = `cal`.`actionId` AND `al`.`LimitType_id` = `cal`.`limitTypeId` AND `al`.`companyLegalUnit` = `ca`.`companyLegalUnit`
        INNER JOIN `featureaction` AS `fa` ON
            `fa`.`id` = \'', `_featureActionId`,'\' AND `fa`.`id` = `al`.`Action_id` AND `fa`.`companyLegalUnit` = `al`.`companyLegalUnit` AND `fa`.`status` = \'SID_ACTION_ACTIVE\'
		INNER JOIN `customer` AS `cu` ON
			`cu`.`id` = `ca`.`Customer_id` AND `cu`.`id` = \'', `_customerId`, '\'',
		IF(@orgId IS NULL OR @orgId = '', '', CONCAT(' LEFT JOIN `organisationactionlimit` AS `oal` ON
			`oal`.`Organisation_id` = `cu`.`Organization_id` AND `oal`.`Action_id` = \'', `_featureActionId`,'\' AND `oal`.`Action_id` = `al`.`Action_id` AND `oal`.`LimitType_id` = `al`.`LimitType_id`')),
		' ORDER BY `ca`.`contractId`, `ca`.`coreCustomerId`, `ca`.`Account_id`, `al`.`LimitType_id`');
    PREPARE STMT FROM @sqlStmt;
    EXECUTE STMT;
    DEALLOCATE PREPARE STMT;
END$$
DELIMITER ;
SET FOREIGN_KEY_CHECKS = 1;
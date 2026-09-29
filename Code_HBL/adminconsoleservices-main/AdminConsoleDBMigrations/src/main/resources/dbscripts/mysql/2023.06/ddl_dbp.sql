DROP PROCEDURE IF EXISTS `user_customers_proc`;

DELIMITER $$
CREATE  PROCEDURE `user_customers_proc`(
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci
)
BEGIN
	SET SESSION group_concat_max_len = 1000000;
	
	SET @filteredContracts = (SELECT GROUP_CONCAT(DISTINCT `contractaccounts`.`contractId`) 
	FROM `contractaccounts` 
	WHERE `contractaccounts`.`statusDesc` != 'CLOSED' 
	AND `contractaccounts`.`coreCustomerId` IN
	(SELECT `contractcustomers`.`coreCustomerId` 
	FROM `contractcustomers` WHERE `contractcustomers`.`customerId` = _customerId));

    SET @select_statement = ("(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
        `contractcustomers`.`companyLegalUnit` AS `companyLegalUnit`,
        `contractcustomers`.`contractId` AS `contractId`,
        `contractcustomers`.`autoSyncAccounts` AS `autoSyncAccounts`,
        `contract`.`name` AS `contractName`,
        `contractcustomers`.`isPrimary` AS `isPrimary`,
        `contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `contractcorecustomers`.`isBusiness` AS `isBusiness`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `membergroup`.`id` AS `roleId`,
        `membergroup`.`Name` AS `userRole`
    FROM
        ((((((`contractcustomers`
        LEFT JOIN `contractcorecustomers` ON (((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
            AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `contract` ON ((`contract`.`id` = `contractcorecustomers`.`contractId`)))
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `contract`.`servicedefinitionId`)))
        LEFT JOIN `membergrouptype` ON ((`membergrouptype`.`id` = `servicedefinition`.`serviceType`)))
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
            AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
            AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))");
    SET @isWhereAppened = false;
    SET @shouldAndAppend = false;
    IF(_customerId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @isWhereAppened = true;
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`customerId` = ",quote(_customerId));
    END IF;
    IF(_legalEntityId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`companyLegalUnit` = ",quote(_legalEntityId));
    END IF;

    IF(_coreCustomerId != '') THEN
        IF(!@isWhereAppened) THEN
            SET @select_statement = CONCAT(@select_statement , " where");
            SET @shouldAndAppend = true;
        END IF;
        IF(@isWhereAppened and @shouldAndAppend) THEN
            SET @select_statement = CONCAT(@select_statement , " and");
            SET @shouldAndAppend = true;
        END IF;
      SET @select_statement =  concat(@select_statement ," `contractcustomers`.`coreCustomerId` = ",quote(_coreCustomerId));
    END IF;
    SET @select_statement =  concat(@select_statement," and `contractcustomers`.`contractId` in (", @filteredContracts,"));");
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER;

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
SET @select_statement  = CONCAT(@select_statement , ' AND EXISTS 
( 
SELECT ''X'' FROM featureaction  f
WHERE FIND_IN_SET(featureaction.accessPolicyId ,',QUOTE(_accessPolicyIdList),')
 AND f.id = featureaction.id)' );
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

SET @should_append_and = FALSE;
IF (_serviceDefinitionId != '' OR _roleId != '' OR _coreCustomerId != '') THEN
	SET @select_statement = CONCAT(@select_statement , ' AND (featureaction.Type_id = ''NON_MONETARY'') OR ');
END IF;

IF(_serviceDefinitionId != '') THEN
	SET @select_statement = CONCAT(@select_statement , '(featureaction.Type_id = ''MONETARY'' AND servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', _serviceDefinitionId , ''')');
	SET @should_append_and = TRUE;
END IF;
IF(_roleId != '') THEN
	IF(@should_append_and) THEN
 		SET @select_statement = CONCAT(@select_statement,' AND ');
 	END IF;
  SET @select_statement = CONCAT(@select_statement , '(featureaction.Type_id = ''MONETARY'' AND groupactionlimit.LimitType_id = actionlimit.LimitType_id AND groupactionlimit.Group_id = ' , '''', _roleId, ''')');
  SET @should_append_and = TRUE;
END IF;

IF(_coreCustomerId != '') THEN
	IF(@should_append_and) THEN
 		SET @select_statement = CONCAT(@select_statement,' AND ');
 	END IF;
SET @select_statement = CONCAT(@select_statement , '(featureaction.Type_id = ''MONETARY'' AND contractactionlimit.limitTypeId = actionlimit.LimitType_id AND contractactionlimit.coreCustomerId = ' , '''', _coreCustomerId,''')');
END IF;


SET @select_statement = CONCAT(@select_statement , ')');

PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
 
END $$
DELIMITER ;

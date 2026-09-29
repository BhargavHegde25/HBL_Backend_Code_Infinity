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
	IF (statusFlag = "CLOSED") then
	SELECT customeraccounts.Customer_id AS suspendedaccounts FROM customeraccounts  WHERE 
	INSTR(@customers,CONCAT(",",customeraccounts.Customer_id,","))>0 AND customeraccounts.companyLegalUnit = legalEntityId AND 
	customeraccounts.accountStatus!="CLOSED" GROUP BY customeraccounts.Customer_id having COUNT(*)=1;
	SET @suspendedaccounts = (SELECT GROUP_CONCAT(customeraccounts.Customer_id SEPARATOR ",") FROM customeraccounts  WHERE 
	INSTR(@customers,CONCAT(",",customeraccounts.Customer_id,","))>0 AND customeraccounts.companyLegalUnit = legalEntityId AND 
	customeraccounts.accountStatus!="CLOSED" GROUP BY customeraccounts.Customer_id having COUNT(*)=1);	
	SET @suspendedaccounts = CONCAT(",",@suspendedaccounts,",");
	UPDATE customerlegalentity SET customerlegalentity.Status_id="SID_CUS_SUSPENDED" WHERE 
	INSTR(@suspendedaccounts,CONCAT(",",customerlegalentity.Customer_id,",")) AND 
	customerlegalentity.legalEntityId = legalEntityId;
	UPDATE customer SET customer.Status_id ="SID_CUS_SUSPENDED" WHERE
	INSTR(@suspendedaccounts,CONCAT(",",customer.id,",")) AND  
	customer.id NOT IN (
	SELECT customerlegalentity.Customer_id FROM customerlegalentity where
	INSTR(@suspendedaccounts,CONCAT(",",customerlegalentity.Customer_id,","))
	AND customerlegalentity.Status_id<>"SID_CUS_SUSPENDED" GROUP BY customerlegalentity.Customer_id
	HAVING COUNT(*)>0); 
	END IF;
	UPDATE customeraccounts SET customeraccounts.accountStatus = statusFlag WHERE 
	customeraccounts.Account_id = accountId AND 	customeraccounts.companyLegalUnit = legalEntityId;
	UPDATE contractaccounts SET contractaccounts.statusDesc = statusFlag WHERE 
	contractaccounts.accountId = accountId AND 	contractaccounts.companyLegalUnit = legalEntityId;	
END $$
DELIMITER ;



DROP PROCEDURE IF EXISTS `get_associated_contractusers_proc`;

DELIMITER $$

CREATE PROCEDURE `get_associated_contractusers_proc`(
	in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	select ca.Customer_id as id,
	ca.contractId as contractId,
	con.name as contractName,
	ca.coreCustomerId as coreCustomerId,
	concore.coreCustomerName as coreCustomerName,
	cc.customerId as customerId,
	cg.Group_id as groupId,
	cus.FirstName as firstName,
	cus.LastName as lastName,
	cus.UserName as userName ,
	cus.Lastlogintime as lastlogintime,
	cus.Status_id as statusId,
	b.BackendId as backendId,
	ca.companyLegalUnit as companyLegalUnit 
	from 
	`customeraction` ca
	left join contractcustomers cc on
	(ca.contractId = cc.contractId and ca.coreCustomerId = cc.coreCustomerId)
	left join contractaccounts conact on
	(ca.contractId = conact.contractId and ca.coreCustomerId = conact.coreCustomerId)
	left join contractcorecustomers concore on
	(ca.contractId = concore.contractId and ca.coreCustomerId = concore.coreCustomerId)
	left join contract con on
	(concore.contractId = con.id)
	left join customergroup cg on
	(cc.contractId = cg.contractId and cc.coreCustomerId = cg.coreCustomerId and cg.Customer_id = cc.customerId)
	left join customer cus on
	(cg.Customer_id = cus.id) 
	left join backendidentifier b on
	(b.Customer_id = cus.id and b.BackendType = _backendType)
	WHERE ca.Customer_id = _id and ca.Action_id = 'USER_MANAGEMENT_VIEW' and ca.companyLegalUnit = _legalEntityId and conact.statusDesc != 'CLOSED';
END $$
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
 
END $$
DELIMITER ;

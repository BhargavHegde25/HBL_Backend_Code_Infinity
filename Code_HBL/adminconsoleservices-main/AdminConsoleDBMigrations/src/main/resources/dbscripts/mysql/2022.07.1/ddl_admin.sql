DROP TABLE IF EXISTS `queryconfig`;
CREATE TABLE `queryconfig` (
  `dbname` VARCHAR(50) NOT NULL,
  `tablename` VARCHAR(100) NOT NULL,
  `querystring` VARCHAR(1000) NOT NULL,
  `primaryKey` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`tablename`));

Drop procedure if exists `SDP_Report_get`;
DELIMITER $$

create procedure `SDP_Report_get`(
    IN _input TEXT(1000000000)
    )
BEGIN 
	Declare st varchar(1000);
	set @customerId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.partyId'));
    set @personalData = JSON_EXTRACT(_input, '$.personalData');
    set @personalDataIndex = 0;
    set @personalDataSize = JSON_LENGTH(@personalData, '$');
	set @queryString = '';
    set @entityId = '';
    set @response = '';
    set @dbname = '';
    iteratePersonalData: LOOP
		set @columnsArray ='';
        set @columnsArraySize = 0;
		if @personalDataIndex = @personalDataSize then
			leave iteratePersonalData;
		else
			set @queryString = '';
            set @personalDataObject = JSON_EXTRACT(@personalData, concat('$[',@personalDataIndex,']')); -- Object which contains data definitions
			set @tableName = JSON_UNQUOTE(JSON_EXTRACT(@personalDataObject, '$.dataEntityName')); -- extracting table name from table object
			set @updateQuery = 'select querystring,primarykey,dbname from queryconfig where tablename = ? into @queryString,@entityId,@dbname';
            prepare st from @updateQuery;
            execute st using @tableName;
            if @queryString <> '' then
				set @columnsArray = JSON_EXTRACT(@personalDataObject, '$.dataEntityFields');
				set @columnsArraySize = JSON_LENGTH(@columnsArray);
				-- select @columnsArraySize;
				set @columnIndex = 0;
				set @tablenameList = '';
				iterateColumns: LOOP
					if @columnIndex = @columnsArraySize then
						LEAVE iterateColumns;
					else
						set @columnCondition = '';
						set @currentVar ='';
						set @fieldObject = JSON_EXTRACT(@columnsArray, concat('$[',@columnIndex,']'));
						set @fieldName = JSON_UNQUOTE(JSON_EXTRACT(@fieldObject, '$.dataEntityFieldName'));
						if @tablenameList = '' then
						   set @tablenameList = @fieldName;
						else 
						   set @tablenameList = CONCAT(@tablenameList,',',@fieldName);
						end if;
					end if;
						set @columnIndex = @columnIndex + 1;
					END LOOP;
					set @execQuery = concat('select "',@tableName,'" as tableName, `',@entityId,'` as entityId,',@tablenameList,' from ',@dbname,'.',@tableName,' ', @queryString);
					prepare st from @execQuery;
					execute st using @customerId;
				end if;
		end if;
        set @personalDataIndex = @personalDataIndex + 1;
    END LOOP;
END $$

DELIMITER ;

DROP procedure IF EXISTS `erasure_proc`;
DELIMITER $$
CREATE PROCEDURE `erasure_proc`(
    IN _input TEXT(1000000000)
    )
BEGIN 
	set @customerId = JSON_UNQUOTE(JSON_EXTRACT(_input, '$.partyId'));
    set @personalData = JSON_EXTRACT(_input, '$.personalData');
    set @personalDataIndex = 0;
    set @personalDataSize = JSON_LENGTH(@personalData, '$');
    set @updateQuery = 'update customer set Status_id="SID_CUS_ERASURE_INPROGRESS" where id = ? ';
    prepare st from @updateQuery;
    execute st using @customerId;
    iteratePersonalData: LOOP
		if @personalDataIndex = @personalDataSize then
			leave iteratePersonalData;
		else
            set @personalDataObject = JSON_EXTRACT(@personalData, concat('$[',@personalDataIndex,']'));
            set @dataDefinitionsArray = JSON_EXTRACT(@personalDataObject, '$.dataDefinitions');
            set @dataDefinitionsLength = JSON_LENGTH(@dataDefinitionsArray, '$');
            set @datadefindex = 0;
            iterateTables: LOOP
				set @queryString = '';
                set @columnsArray ='';
                set @dbname = '';
                set @columnsArraySize = 0;
                if @datadefindex = @dataDefinitionsLength then
                    Leave iterateTables;
                else
                    set @tableObject = JSON_EXTRACT(@dataDefinitionsArray, concat('$[',@datadefindex,']'));
                    set @tableName = JSON_UNQUOTE(JSON_EXTRACT(@tableObject, '$.dataEntityName'));
                    set @updateQuery = 'select dbname,querystring from queryconfig where tablename = ? into @dbname,@queryString';
                    prepare st from @updateQuery;
                    execute st using @tableName;
                    set @columnsArray = JSON_EXTRACT(@tableObject, '$.dataEntityFields');
                    set @columnsArraySize = JSON_LENGTH(@columnsArray);
				end if;
                if @queryString <> '' then
                    set @columnIndex = 0;
                    set @columnQuery = '';
                    iterateColumns: LOOP
                        if @columnIndex = @columnsArraySize then
                            LEAVE iterateColumns;
                        else
                            set @columnCondition = '';
                            set @fieldObject = JSON_EXTRACT(@columnsArray, concat('$[',@columnIndex,']'));
                            set @fieldName = JSON_UNQUOTE(JSON_EXTRACT(@fieldObject, '$.dataEntityFieldName'));
                            set @fieldType = JSON_UNQUOTE(JSON_EXTRACT(@fieldObject, '$.dataEntityFieldDataType'));
                            set @erasureOptionsId = JSON_UNQUOTE(JSON_EXTRACT(@fieldObject, '$.erasureOptions.optionsId'));
                            set @erasureOptionsValue = JSON_UNQUOTE(JSON_EXTRACT(@fieldObject, '$.erasureOptions.optionsValue'));
                            set @updatedValue = '';
                            if @erasureOptionsId <> 'NO ACTION' then
                                if @erasureOptionsId = 'NULLIFY' then
                                    set @columnCondition = concat(@fieldName,'=null');
                                else
                                    if @tableName = 'customerdevice' and @fieldName = 'LastLoginTime' then
                                        set @updatedValue = '0000-00-00 00:00:00';
                                    elseif @tableName = 'auditactivity' and @fieldName = 'EventData' then
                                        set @updatedValue = '{}';
                                    elseif @tableName = 'customer' and @fieldName = 'UserName' then
										set @updatedValue = '';
										set @randomStringQuery = 'SELECT upper(concat(LEFT(MD5(RAND()), 8),''-'',LEFT(MD5(RAND()), 8))) into @updatedValue';
                                        prepare st from @randomStringQuery;
                                        execute st;
                                    else
                                        case @fieldType
                                            when 'ALPHA' then
                                                set @updatedValue = REPEAT(@erasureOptionsValue, 10);
                                            when 'NUMBER' then
                                                set @updatedValue = 9;
                                            when 'DATE' then
                                                set @updatedValue = '9999-12-31';
                                        end case;
                                    end if;
                                    set @columnCondition = concat(@fieldName,'=\'',@updatedValue,'\'');
                                end if;
                                if @columnQuery <> '' then
                                    set @columnQuery = concat(@columnQuery,',');
                                end if;
                                set @columnQuery = concat(@columnQuery,@columnCondition);
                            end if;
                        end if;
                        set @columnIndex = @columnIndex + 1;
                    END LOOP;
                    set @execQuery = concat('update ',@dbname,'.',@tableName,' set ',@columnQuery,' ', @queryString);
                    prepare st from @execQuery;
                    execute st using @customerId;
                end if;
                set @datadefindex = @datadefindex + 1;
            END LOOP;
        end if;
        set @personalDataIndex = @personalDataIndex + 1;
    END LOOP;
END $$

DELIMITER ;


DROP PROCEDURE IF EXISTS `update_customer_erasure_status_proc`;

DELIMITER $$
CREATE PROCEDURE `update_customer_erasure_status_proc`(
    IN _customerId VARCHAR(100),
    IN _erasureStatus VARCHAR(100)
)
BEGIN 
    DECLARE st varchar(1000);
    IF _customerId != '' THEN
        SET @currentStatusId = '';
        SET @customerId = _customerId;
        
        SET @executeQuery = 'SELECT Status_id FROM customer where id = ? INTO @currentStatusId';
        prepare st from @executeQuery;
        EXECUTE st using @customerId;

        IF @currentStatusId != '' THEN
            IF _erasureStatus = 'ERASED' THEN
                IF @currentStatusId = 'SID_CUS_ERASURE_INPROGRESS' THEN
                    SET @updatedStatusId = 'SID_CUS_ERASURE_COMPLETED';
                    SET @executeQuery = CONCAT('UPDATE customer SET Status_id = ''',@updatedStatusId,''' WHERE id = ''',_customerId,'''');
                    prepare st from @executeQuery;
                    EXECUTE st;
                ELSEIF @currentStatusId != 'SID_CUS_ERASURE_COMPLETED' THEN
                    SELECT "ERASURE_NOT_INITIATED" AS 'errmsg';
                END If;
            ELSEIF _erasureStatus = 'ERASURE.IN.PROGRESS' THEN
                IF(@currentStatusId != 'SID_CUS_ERASURE_COMPLETED' AND @currentStatusId != 'SID_CUS_ERASURE_INPROGRESS') THEN
                    SET @updatedStatusId = 'SID_CUS_ERASURE_INPROGRESS';
                    SET @executeQuery = CONCAT('UPDATE customer SET Status_id = ''',@updatedStatusId,''' WHERE id = ''',_customerId,'''');
                    prepare st from @executeQuery;
                    EXECUTE st;
                ELSEIF @currentStatusId = 'SID_CUS_ERASURE_COMPLETED' THEN
                    SELECT "ALREADY_ERASED" AS 'errmsg';
                END IF;
            END IF;
        END IF;
    END IF;
END $$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `GetExternalPayeesProc`(IN userId VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT * from internationalpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
      where isInternationalAccount = 1 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
      and Action_id = "INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT"
      and isAllowed = 1)
UNION
SELECT * from interbankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
      and Action_id = "INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT"
      and isAllowed = 1)
UNION
SELECT * from intrabankpayee as ip inner join externalaccount as ea on ip.payeeId = ea.Id
where isSameBankAccount = 1  and softDelete = 0 and cif in (SELECT coreCustomerId from customeraction where Customer_id = userId
      and Action_id = "INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT"
      and isAllowed = 1);
END $$
DELIMITER ;

CREATE INDEX `idx_interbankpayee_cif`  ON `interbankpayee` (cif);
CREATE INDEX `idx_internationalpayee_cif`  ON `internationalpayee` (cif);
CREATE INDEX `idx_intrabankpayee_cif`  ON `intrabankpayee` (cif);
CREATE INDEX `idx_corporatepayees_cif`  ON `corporatepayees` (cif);

USE `dbxdb`;
DROP procedure IF EXISTS `fetch_contractcorecustomers_for_user_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `fetch_contractcorecustomers_for_user_proc`(
IN _contractIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    IF (_contractIdList != '' and _contractIdList IS NOT NULL)
    THEN
        SET @sql_query = concat('select * from contractcustomers where ', 'find_in_set(contractId,''',_contractIdList,''');');
    ELSE
        IF ((_contractId IS NOT NULL and LENGTH(_contractId)>0) AND (_coreCustomerId IS NOT NULL and LENGTH(_coreCustomerId)>0) )
        THEN
            SET @sql_query = concat('select * from contractcustomers where contractId = ''', _contractId,''' and coreCustomerId =   ''',_coreCustomerId ,''' ;');
        END IF;
    END IF;
    PREPARE stmt FROM @sql_query; EXECUTE stmt; DEALLOCATE
    PREPARE stmt;
END $$

DELIMITER ;


USE `dbxdb`;
DROP procedure IF EXISTS `fetch_contractaccounts_for_user_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `fetch_contractaccounts_for_user_proc`(
IN _contractIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    IF (_contractIdList != '' and _contractIdList IS NOT NULL)
    THEN
SET @sql_query = concat('select * from contractaccounts where ', 'find_in_set(contractId,''',_contractIdList,''');');
    ELSE
        IF ((_contractId IS NOT NULL and LENGTH(_contractId)>0) AND (_coreCustomerId IS NOT NULL and LENGTH(_coreCustomerId)>0) )
        THEN
            SET @sql_query = concat('select * from contractaccounts where contractId = ''', _contractId,''' and coreCustomerId =   ''',_coreCustomerId ,''' ;');
        END IF;
    END IF;
    PREPARE stmt FROM @sql_query; EXECUTE stmt; DEALLOCATE
    PREPARE stmt;
END $$

DELIMITER ;

USE `dbxdb`;
DROP procedure IF EXISTS `get_feature_actions_view_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `get_feature_actions_view_proc`(IN _roleTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
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
	`featureactionroletype`.`RoleType_id` AS `actionType`, 
	`accesspolicy`.`name` AS `accessPolicy`,
    `limitgroup`.`name` AS `limitGroup`, 
	`featureaction`.`accessPolicyId`, 
	`featureaction`.`limitGroupId`, 
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
FROM (`featureaction` 
LEFT OUTER JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`) 
LEFT OUTER JOIN `actiondisplaynamedescription` ON (`actiondisplaynamedescription`.`Action_id` = `featureaction`.`id`) 
LEFT OUTER JOIN `accesspolicy` ON (`featureaction`.`accesspolicyId` = `accesspolicy`.`id`)
LEFT OUTER JOIN `featureroletype` ON (`featureroletype`.`Feature_id` = `feature`.`id`) 
LEFT OUTER JOIN `featureactionroletype` ON (`featureaction`.id = `featureactionroletype`.Action_id) 
LEFT OUTER JOIN `termandcondition` ON (`featureaction`.`TermsAndConditions_id` = `termandcondition`.`id`) 
LEFT OUTER JOIN `limitgroup` ON (`featureaction`.`limitgroupId` = `limitgroup`.`id`) 
LEFT OUTER JOIN `actionlevel` ON (`featureaction`.`actionlevelId` = `actionlevel`.`id`) 
LEFT OUTER JOIN `dependentactions_view` ON (`featureaction`.`id` = `dependentactions_view`.`actionId`) 
LEFT OUTER JOIN `membergrouptype` ON (`featureactionroletype`.`RoleType_id` = `membergrouptype`.`id`) 
LEFT OUTER JOIN `actionlimit` ON (`actionlimit`.`Action_id` = `featureaction`.`id`))';
IF _roleTypeId is not null and CHAR_LENGTH(RTRIM(_roleTypeId))>0 THEN 
	SET @select_statement = CONCAT (@select_statement ,'where `featureroletype`.`RoleType_Id` = ''', _roleTypeId,'''', 'and `featureactionroletype`.`RoleType_Id` =''',_roleTypeId ,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

USE `dbxdb`;
DROP procedure IF EXISTS `group_features_actions_view_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `group_features_actions_view_proc`(IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
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
SET @select_statement = CONCAT (@select_statement ,'where groupactionlimit.Group_id =''', _groupId,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;

USE `dbxdb`;
DROP procedure IF EXISTS `servicedefinition_view_proc`;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `servicedefinition_view_proc`(IN _typeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DECLARE select_statement TEXT;
SET @select_statement= 'SELECT id, name, description, serviceType, status,
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

IF _typeId is not null and CHAR_LENGTH(RTRIM(_typeId))>0 THEN 
SET @select_statement = concat(@select_statement, ' where serviceType =''', _typeId,''';');
END IF;
PREPARE stmt FROM @select_statement;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;


DROP PROCEDURE IF EXISTS `userPermissionsAndActionsCreate_proc`;
DELIMITER $$
CREATE PROCEDURE `userPermissionsAndActionsCreate_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _loggedInUserId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _listOfAddPermissions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE finished INTEGER DEFAULT 0 ;
DECLARE permissionId varchar(255) DEFAULT "" ;
DECLARE permissionActionId varchar(255) DEFAULT "" ;
DECLARE is_Enabled varchar(1) DEFAULT "" ;
DECLARE Name varchar(55) DEFAULT "" ;
DECLARE actionslist TEXT DEFAULT "" ;
DECLARE rowCount INTEGER DEFAULT 0 ;
DECLARE done INT DEFAULT 0;


DECLARE permissions CURSOR FOR (SELECT id FROM permission WHERE FIND_IN_SET(id,_listOfAddPermissions));

DECLARE permissionActions CURSOR
         FOR (SELECT id,Name,isEnabled FROM compositeaction WHERE Permission_id=permissionId);
         
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

OPEN permissions; 
getPermission : LOOP
FETCH permissions INTO permissionId;
IF finished = 1 THEN 
    LEAVE getPermission;
ELSE
  OPEN permissionActions; 
  getPermissionAction: LOOP
  FETCH permissionActions INTO permissionActionId,Name,is_Enabled;
  IF finished = 1 THEN 
    LEAVE getPermissionAction;
  ELSE
   
   SELECT count(*) FROM usercompositeaction WHERE CompositeAction_id=permissionActionId AND User_id=_userId INTO rowCount;
    if(rowCount=0) THEN
        /*record does not exist for the user, so create a new record */
        INSERT IGNORE INTO usercompositeaction(CompositeAction_id,User_id,createdby,modifiedby,isEnabled) VALUES
        (permissionActionId,_userId,_loggedInUserId,_loggedInUserId,is_Enabled);
    ELSE
        /*record does exist for the user, so update the record */
        
        UPDATE usercompositeaction SET isEnabled=is_Enabled where CompositeAction_id=permissionActionId AND User_id=_userId;

    END IF;
    
  END IF;
  END LOOP getPermissionAction;
  CLOSE permissionActions;
  SET finished = 0;
  
  	INSERT IGNORE INTO USERPERMISSION(User_id,Permission_id,createdby,modifiedby) VALUES
        (_userId,permissionId,_loggedInUserId,_loggedInUserId);

END IF;

END LOOP getPermission;
CLOSE permissions;
END $$
DELIMITER ;

DROP procedure IF EXISTS `fetch_achtransaction_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_achtransaction_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _queryType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByParam TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _filterByValue TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _sortByParam VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _sortOrder VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageSize VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _pageOffset VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL:BEGIN
		SET SESSION group_concat_max_len = 100000000;

        SET @combinedIds = (select group_concat(id SEPARATOR ",") from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN
			SET @combinedIds = _customerId;
		ELSE
			SET @combinedIds = concat(_customerId , "," ,@combinedIds);
        END IF;

		SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from application);

		IF @isSelfApprovalEnabled is FALSE THEN
            SET @notCreatedBySelf = concat(" AND NOT FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"')");
        ELSE
            SET @notCreatedBySelf = "";
		END IF;

		SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
        SET _transactionId = if(_transactionId = "" OR _transactionId = NULL, '%', _transactionId);

        SET @numOfParams = 0;
        IF LENGTH(_filterByParam) > 0 THEN
			SET @numOfParams = LENGTH(_filterByParam) - LENGTH(REPLACE(_filterByParam, ',', '')) + 1;
		END IF;

        SET @searchQuery = "";
		SET @idx = 1;
		filterParams:LOOP
			IF @idx > @numOfParams THEN
				LEAVE filterParams;
			END IF;

			SET @filterParam = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByParam, ',', @idx), ',', -1 );
			SET @filterValue = SUBSTRING_INDEX(SUBSTRING_INDEX(_filterByValue, ',', @idx), ',', -1 );
			SET @searchQuery = concat(@searchQuery, " AND (",@filterParam," LIKE '",@filterValue,"' )");
			SET @idx = @idx + 1;

		END LOOP filterParams;

        SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	    IF @companyId is NULL THEN
			SET @companyId = "";
		END IF;

        IF _featureactionlist is NULL THEN
			LEAVE MAINLABEL;
		END IF;

        SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'createdts', _sortByParam);
		SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);

        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix where FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN
			SET @customerMatrixIds = "";
		END IF;

        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest where FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
        IF @alreadyApprovedIds is NULL THEN
			SET @alreadyApprovedIds = "";
		END IF;

        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",")
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds)
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId))
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							);
        IF @approvalRequestIds is NULL THEN
			SET @approvalRequestIds = "''";
		END IF;

        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achtransaction`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achtransaction`.`status` = 'Pending' ",@notCreatedBySelf),
                                        if(_queryType = 'rejected', concat(" AND `achtransaction`.`status` = 'Rejected' "),
                                        if(_transactionId = '%' , concat(" AND NOT `achtransaction`.`status` = 'Withdrawn'"),
                                        ''))));

        SET @validAccountsJoin = if(_queryType = '', concat(" INNER JOIN (SELECT DISTINCT Account_id,
									REPLACE(Action_id, '_VIEW', '_CREATE') as Action_id
									FROM
									customeraction
									WHERE Customer_id = ",_customerId,"
									AND isAllowed = '1'
									AND Account_id is NOT null
									AND Action_id like '%_VIEW') as
								`can` ON (`achtransaction`.`featureActionId` = `can`.`Action_id`
											AND `achtransaction`.`fromAccount` = `can`.`Account_id`) "), '');

        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery,
								concat(@searchQuery, " AND (`achtransaction`.`templateName` LIKE '%",_searchString,"%' OR `achtransaction`.`confirmationNumber` LIKE '%",_searchString,"%' OR `customeraccounts`.`AccountName` LIKE '%",_searchString,"%' OR `bbtemplaterequesttype`.`templateRequestTypeName` LIKE '%",_searchString,"%' OR `achtransaction`.`transaction_id` LIKE '%",_searchString,"%' OR `achtransaction`.`fromAccount` LIKE '%",_searchString,"%' )"));

        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize));

        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN
			SET @features = "";
		END IF;

        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_CREATE");
        IF @createActions is NULL THEN
			SET @createActions = "";
		END IF;

		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        IF @companyRequestIds is NULL THEN
            SET @companyRequestIds = "";
        END IF;

	    SET @customerAcounts = (SELECT group_concat(Account_id SEPARATOR ",") FROM customeraccounts where FIND_IN_SET(Customer_id, @combinedIds));
	    IF @customerAcounts is NULL THEN
			SET @customerAcounts = "";
		END IF;

		SET @select_statement = concat("SELECT * FROM

        (SELECT
			`achtransaction`.`transaction_id` AS `transaction_id`,
			`achtransaction`.`fromAccount` AS `fromAccount`,
			`achtransaction`.`effectiveDate` AS `effectiveDate`,
			`achtransaction`.`requestId` AS `requestId`,
			`achtransaction`.`createdby` AS `createdby`,
			`achtransaction`.`createdts` AS `createdts`,
			`achtransaction`.`maxAmount` AS `maxAmount`,
            (CASE
				WHEN `bbrequest`.`status` is NULL THEN `achtransaction`.`status`
                ELSE `bbrequest`.`status`
			END) AS `status`,
			`achtransaction`.`transactionType_id` AS `transactionType_id`,
			`achtransaction`.`templateType_id` AS `templateType_id`,
			`achtransaction`.`companyId` AS `companyId`,
			`achtransaction`.`templateRequestType_id` AS `templateRequestType_id`,
			`achtransaction`.`softDelete` AS `softDelete`,
			`achtransaction`.`templateName` AS `templateName`,
			`achtransaction`.`template_id` AS `template_id`,
			`achtransaction`.`totalAmount` AS `totalAmount`,
			`achtransaction`.`featureActionId` AS `featureActionId`,
			`achtransaction`.`confirmationNumber` AS `confirmationNumber`,
			( CASE
				WHEN `customeraccounts`.`AccountName` is NULL THEN 'AccountName'
                ELSE `customeraccounts`.`AccountName`
			END ) AS `accountName`,
			`customer`.`UserName` AS `userName`,
			`bbtransactiontype`.`transactionTypeName` AS `transactionTypeName`,
			`bbtemplatetype`.`templateTypeName` AS `templateTypeName`,
			`bbtemplaterequesttype`.`templateRequestTypeName` AS `templateRequestTypeName`,
			`bbrequest`.`createdby` AS `requestCreatedby`,
			(CASE
				WHEN `achtransaction`.`createdby` IN (",@combinedIds,") THEN 'true'
				ELSE 'false'
			 END) as `amICreator`,
			(CASE
				WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
        		ELSE 'false'
	 		END)
	 		 as `amIApprover`
		FROM
			((((((`achtransaction`
			LEFT JOIN `customer` ON (`achtransaction`.`createdby` = `customer`.`id`))
			LEFT JOIN `customeraccounts` ON ((`achtransaction`.`createdby` = `customeraccounts`.`Customer_id`)
				AND (`achtransaction`.`fromAccount` = `customeraccounts`.`Account_id`)))
			LEFT JOIN `bbtransactiontype` ON (`achtransaction`.`transactionType_id` = `bbtransactiontype`.`transactionType_id`))
			LEFT JOIN `bbtemplatetype` ON (`achtransaction`.`templateType_id` = `bbtemplatetype`.`templateType_id`))
			LEFT JOIN `bbtemplaterequesttype` ON (`achtransaction`.`templateRequestType_id` = `bbtemplaterequesttype`.`templateRequestType_id`))"
            ,@validAccountsJoin,
            "
			LEFT JOIN `bbrequest` ON (`achtransaction`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achtransaction`.`softDelete` = '0'
				AND ( FIND_IN_SET(`achtransaction`.`companyId` , '", @companyId ,"') OR FIND_IN_SET(`achtransaction`.`createdby`, '",@combinedIds,"'))
				AND `achtransaction`.`transaction_id` LIKE '",_transactionId ,"'
				AND FIND_IN_SET(`achtransaction`.`fromAccount`, \"", @customerAcounts, "\")
                AND FIND_IN_SET(`achtransaction`.`featureActionId`, \"", @createActions, "\") ",
                @queryTypecondition, " ",
                @searchQuery, " ) AS t1
            LEFT JOIN
            (
				SELECT
					bbrequest.requestId,
                    (select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId)
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId))
						,
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						)
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE FIND_IN_SET(bbrequest.requestId, \"",@companyRequestIds,"\")
				GROUP BY bbrequest.requestId
            ) AS t2
            ON
            `t1`.`requestId` = `t2`.`requestId`
            ORDER BY ", _sortByParam, " ", _sortOrder , " ",
            @paginationQuery);

	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END $$
DELIMITER ;

DROP procedure IF EXISTS `fetch_signatorygroups_in_approvalrule_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroups_in_approvalrule_proc`()
BEGIN
    SELECT signatorygroup.signatoryGroupId FROM signatorygroup
        WHERE FIND_IN_SET(signatorygroup.signatoryGroupId, (SELECT group_concat(distinct(REPLACE(REPLACE(REPLACE(groupList,']',''),'[',''),'"','')))
            FROM signatorygroupmatrix WHERE approvalMatrixId IN (SELECT id FROM approvalmatrix WHERE softdeleteflag='0')
        ));
END$$
DELIMITER ;

DROP procedure IF EXISTS `delete_prospect_data_proc`;
DELIMITER $$
CREATE PROCEDURE `delete_prospect_data_proc`(
    IN _input VARCHAR(4000)
    )
BEGIN 
	set @tableNames = '["backendidentifier","passwordhistory","customerpreference","customeraddress","customergroup","customercommunication","customer"]';
    set @tableSize = JSON_LENGTH(@tableNames);
    set @inputLength = JSON_LENGTH(_input);
    if @inputLength >= 1 then
		set @dpiIndex = 0;
        iterateDPI: LOOP
			if @dpiIndex = @inputLength then
				leave iterateDPI;
			else
				set @customerType = '';
				set @dpi = JSON_UNQUOTE(JSON_EXTRACT(_input, concat('$[',@dpiIndex,']')));
				set @typeSelectQuery = concat('select CustomerType_id from customer where id = ',@dpi,' into @customerType');
                prepare st from @typeSelectQuery;
				execute st;
                if @customerType = 'TYPE_ID_PROSPECT' then
                    set @tableIndex = 0;
					iterateTables: LOOP
						if @tableIndex = @tableSize then
							leave iterateTables;
						else
							set @tableName = JSON_UNQUOTE(JSON_EXTRACT(@tableNames, concat('$[',@tableIndex,']')));
							set @queryString = '';
							set @selectQuery = 'select querystring from queryconfig where tablename = ? into @queryString';
							prepare st from @selectQuery;
							execute st using @tableName;
							set @deleteQuery = concat('delete from ',@tableName,' ',@queryString);
							prepare st from @deletequery;
							execute st using @dpi;
						end if;
						set @tableIndex = @tableIndex +1;
					END LOOP;
				end if;
			end if;
            set @dpiIndex = @dpiIndex +1;
        END LOOP;
    end if;
END $$
DELIMITER ;
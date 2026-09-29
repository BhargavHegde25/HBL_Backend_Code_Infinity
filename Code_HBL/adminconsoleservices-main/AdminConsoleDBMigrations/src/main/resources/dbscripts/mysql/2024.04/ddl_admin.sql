ALTER TABLE `makercheckerconfig` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE `mcmoduletext` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE `mcactiontext` CONVERT TO CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
ALTER TABLE `externalaccount` ADD `payeeVerification` varchar(45) NULL;
DROP VIEW IF EXISTS `makercheckerconfig_view`;
CREATE VIEW `makercheckerconfig_view` AS
SELECT
    `mcconfig`.`id` AS `id`,
	`mcmodule`.`name` AS `moduleName`,
    `mcaction`.`name` AS `actionName`,
	`mcaction`.`language_code` AS `languageCode`,
	`mcconfig`.`isApprovalRequired` AS `isApprovalRequired`,
	`mcconfig`.`companyLegalUnit` AS `companyLegalUnit`
FROM
    (
    `makercheckerconfig` `mcconfig` JOIN `mcmoduletext` `mcmodule`
    ON (
        `mcconfig`.`module` = `mcmodule`.`id`
	) JOIN `mcactiontext` `mcaction`
    ON (
       `mcconfig`.`action` = `mcaction`.`id`
	   AND `mcmodule`.`language_code` = `mcaction`.`language_code`)
	);

ALTER TABLE `makercheckerconfig` ADD COLUMN `auditModule` VARCHAR(50) DEFAULT NULL, ADD COLUMN `auditEvent` VARCHAR(50) DEFAULT NULL;

DROP VIEW IF EXISTS `update_approvalrequests_proc`;

DELIMITER $$
CREATE PROCEDURE `update_approvalrequests_proc`(
  IN _status varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _reason longtext CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _requestId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _currentStatus varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _checkedBy varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	UPDATE `approvalrequests` 
	SET `status`=_status, `reason`=_reason, `checkedBy`=_checkedBy
	where `requestId`=_requestId and `status`=_currentStatus;
	select ROW_COUNT() as result;
END$$
DELIMITER ;
	
ALTER TABLE `makercheckerconfig` ADD COLUMN `excludedParams` VARCHAR(200) DEFAULT NULL;

DROP procedure IF EXISTS `fetch_maker_pending_requests_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_maker_pending_requests_proc`(
    IN _legalEntityId VARCHAR(1024),  
    IN _module VARCHAR(1024),
    IN _action VARCHAR(1024),
    IN _userName VARCHAR(50),
    IN _submittedDate DATE,
	IN _pageOffset INT,
	IN _pageSize INT
)
BEGIN
    DECLARE userId_Id VARCHAR(50);
    DECLARE LegalEntity_Id VARCHAR(1024); 
    DECLARE Request_Module VARCHAR(1024);
    DECLARE Request_Action VARCHAR(1024);
    DECLARE submittedDate DATE;
    DECLARE value VARCHAR(50);
    DROP TEMPORARY TABLE IF EXISTS temp_maker_requests_filters;
     CREATE TEMPORARY TABLE temp_maker_requests_filters (
        LegalEntityId VARCHAR(50),
        ModuleName VARCHAR(255),
        ActionName VARCHAR(255)
    );
    SET userId_Id = _userName;
    SET LegalEntity_Id = _legalEntityId;
    SET Request_Module = _module;
    SET Request_Action = _action;
    SET submittedDate = _submittedDate;
    WHILE LENGTH(LegalEntity_Id) > 0 DO
        SET value = TRIM(SUBSTRING_INDEX(LegalEntity_Id, ',', 1));
        SET LegalEntity_Id = TRIM(SUBSTRING(LegalEntity_Id, LENGTH(value) + 2));
        INSERT INTO temp_maker_requests_filters (LegalEntityId) VALUES (value);
 
    IF Request_Module IS NOT NULL THEN
            SET value = TRIM(SUBSTRING_INDEX(Request_Module, ',', 1));
            SET Request_Module = TRIM(SUBSTRING(Request_Module, LENGTH(value) + 2));
            INSERT INTO temp_maker_requests_filters (ModuleName)VALUES (value);
    END IF;
 
    IF Request_Action IS NOT NULL THEN
            SET value = TRIM(SUBSTRING_INDEX(Request_Action, ',', 1));
            SET Request_Action = TRIM(SUBSTRING(Request_Action, LENGTH(value) + 2));
            INSERT INTO temp_maker_requests_filters (ActionName)VALUES (value);
    END IF;
	END WHILE;
    SET @sql_query = CONCAT('
        SELECT requestId, module, action, createdts, createdby, companyLegalUnit
        FROM approvalrequests 
        WHERE status = ''SID_PENDING'' AND createdby = ''', userId_Id, ''' 
        AND (',
            (SELECT GROUP_CONCAT('FIND_IN_SET(''', LegalEntityId, ''', companyLegalUnit) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
        ')');
	SET @sql_query_count = CONCAT('
        SELECT count(*) AS totalRecords
        FROM approvalrequests 
        WHERE status = ''SID_PENDING'' AND createdby = ''', userId_Id, ''' 
        AND (',
            (SELECT GROUP_CONCAT('FIND_IN_SET(''', LegalEntityId, ''', companyLegalUnit) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
        ')');
 
    IF Request_Module IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ModuleName, ''', module) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
            ')');
		SET @sql_query_count = CONCAT(@sql_query_count, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ModuleName, ''', module) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
            ')');
    END IF;
 
    IF Request_Action IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ActionName, ''', action) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
            ')');
		SET @sql_query_count = CONCAT(@sql_query_count, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ActionName, ''', action) > 0' SEPARATOR ' OR ') FROM temp_maker_requests_filters),
            ')');
    END IF;
 
    IF submittedDate IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
            AND DATE(createdts) = ''', submittedDate, '''');
		 SET @sql_query_count = CONCAT(@sql_query_count, '
            AND DATE(createdts) = ''', submittedDate, '''');
    END IF;
		
	PREPARE stmtCount FROM @sql_query_count;
	EXECUTE stmtCount ;
	
    DEALLOCATE PREPARE stmtCount;
	
    SET @sql_query = CONCAT(@sql_query, ' ORDER BY createdts ASC LIMIT ', _pageOffset, ',', _pageSize);
	
    PREPARE stmt FROM @sql_query;
	EXECUTE stmt ;
    DEALLOCATE PREPARE stmt;
	
    DROP TEMPORARY TABLE IF EXISTS temp_maker_requests_filters;
END$$
DELIMITER ;
 
DROP procedure IF EXISTS `get_checkerpending_requests_proc`;
DELIMITER $$
CREATE PROCEDURE `get_checkerpending_requests_proc`(
    IN _legalEntityId VARCHAR(1024),  
    IN _module VARCHAR(1024),
    IN _action VARCHAR(1024),
    IN _userName VARCHAR(50),
    IN _submittedDate DATE,
    IN _pageOffset INT,
    IN _pageSize INT
)
BEGIN
    DECLARE userId_Id VARCHAR(50);
    DECLARE Legal_Id VARCHAR(1024); 
    DECLARE module_val VARCHAR(1024);
    DECLARE action_val VARCHAR(1024);
    DECLARE submittedDate DATE;
    DECLARE value VARCHAR(50);
	
    DROP TEMPORARY TABLE IF EXISTS temp_checker_requests_filters;
     CREATE TEMPORARY TABLE temp_checker_requests_filters (
        LegalEntityId VARCHAR(50),
        ModuleName VARCHAR(255),
        ActionName VARCHAR(255)
    );
    SET userId_Id = _userName;
    SET Legal_Id = _legalEntityId;
    SET module_val = _module;
    SET action_val = _action;
    SET submittedDate = _submittedDate;
    WHILE LENGTH(Legal_Id) > 0 DO
        SET value = TRIM(SUBSTRING_INDEX(Legal_Id, ',', 1));
        SET Legal_Id = TRIM(SUBSTRING(Legal_Id, LENGTH(value) + 2));
        INSERT INTO temp_checker_requests_filters (LegalEntityId) VALUES (value);
    IF module_val IS NOT NULL THEN
            SET value = TRIM(SUBSTRING_INDEX(module_val, ',', 1));
            SET module_val = TRIM(SUBSTRING(module_val, LENGTH(value) + 2));
            INSERT INTO temp_checker_requests_filters (ModuleName)VALUES (value);
    END IF;
    IF action_val IS NOT NULL THEN
            SET value = TRIM(SUBSTRING_INDEX(action_val, ',', 1));
            SET action_val = TRIM(SUBSTRING(action_val, LENGTH(value) + 2));
            INSERT INTO temp_checker_requests_filters (ActionName)VALUES (value);
    END IF;
	END WHILE;
    SET @sql_query = CONCAT('
        SELECT requestId, module, action, permissionName, createdts, createdby, companyLegalUnit
        FROM approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''', userId_Id, '''
        AND (',
            (SELECT GROUP_CONCAT('FIND_IN_SET(''', LegalEntityId, ''', companyLegalUnit) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
        ')');
	SET @sql_query_count = CONCAT('
        SELECT count(*) AS totalRecords
        FROM approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''', userId_Id, '''
        AND (',
            (SELECT GROUP_CONCAT('FIND_IN_SET(''', LegalEntityId, ''', companyLegalUnit) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
        ')');
    IF module_val IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
             AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ModuleName, ''', module) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
            ')');
		SET @sql_query_count = CONCAT(@sql_query_count, '
             AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ModuleName, ''', module) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
            ')');
    END IF;
    IF action_val IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ActionName, ''', action) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
            ')');
		SET @sql_query_count = CONCAT(@sql_query_count, '
            AND (',
                (SELECT GROUP_CONCAT('FIND_IN_SET(''', ActionName, ''', action) > 0' SEPARATOR ' OR ') FROM temp_checker_requests_filters),
            ')');
    END IF;
    IF submittedDate IS NOT NULL THEN
        SET @sql_query = CONCAT(@sql_query, '
            AND DATE(createdts) = ''', submittedDate, '''');
		SET @sql_query_count = CONCAT(@sql_query_count, '
            AND DATE(createdts) = ''', submittedDate, '''');
    END IF;
	
	PREPARE stmtCount FROM @sql_query_count;
	EXECUTE stmtCount ;
	
    DEALLOCATE PREPARE stmtCount;
	
    SET @sql_query = CONCAT(@sql_query, ' ORDER BY createdts ASC LIMIT ', _pageOffset, ',', _pageSize);
	
    PREPARE stmt FROM @sql_query;
	EXECUTE stmt ;
	
    DEALLOCATE PREPARE stmt;
    DROP TEMPORARY TABLE IF EXISTS temp_checker_requests_filters;
END$$
DELIMITER ;

CREATE VIEW `get_mc_requestshistory_view` AS
    SELECT 
        `mcconfig`.`action` AS `action`,
        `ar`.`createdby` AS `createdBy`,
        UPPER(`ar`.`status`) AS `status`,
        `ar`.`companyLegalUnit` AS `companyLegalUnit`,
        `mcconfig`.`approvalPermissionName` AS `approvalPermissionName`,
        `ar`.`requestId` AS `requestId`,
        `ar`.`module` AS `module`,
        CAST(`ar`.`createdts` AS DATE) AS `createdDate`,
        `ar`.`createdts` AS `createdTs`,
        `ar`.`reason` AS `reason`,
        `ar`.`checkedBy` AS `checkedBy`,
        `ar`.`checkedts` AS `actionedDate`
    FROM
        (`approvalrequests` `ar`
        JOIN `makercheckerconfig` `mcconfig` ON (((`ar`.`expAPIOperationName` = `mcconfig`.`expAPIOperationName`)
            AND (`ar`.`companyLegalUnit` = `mcconfig`.`companyLegalUnit`))));
	
DROP VIEW IF EXISTS `get_mc_moduleactionname_view`;


CREATE VIEW `get_mc_moduleactionname_view` AS
    SELECT DISTINCT
        `mcconfig`.`action` AS `actionid`,
        `mcconfig`.`module` AS `moduleid`,
        `mcmodule`.`name` AS `modulename`,
        `mcaction`.`name` AS `actionname`,
        `mcmodule`.`language_code` AS `languagecode`
    FROM
        ((`makercheckerconfig` `mcconfig`
        JOIN `mcmoduletext` `mcmodule` ON ((`mcmodule`.`id` = `mcconfig`.`module`)))
        JOIN `mcactiontext` `mcaction` ON ((`mcaction`.`id` = `mcconfig`.`action`)));

DROP PROCEDURE IF EXISTS `update_makercheckerconfig_proc`;
DELIMITER $$
CREATE PROCEDURE `update_makercheckerconfig_proc` (
    IN _input longtext
)
BEGIN
    DECLARE _idx INT DEFAULT 0;
    DECLARE _maxIdx INT;
    DECLARE _companyLegalUnit VARCHAR(255);
    DECLARE _isApprovalRequired TINYINT;
    DECLARE _id INT;
    DECLARE _input_json JSON;
   
    set _input_json = CAST(_input as JSON);
    set @_mc_data = JSON_EXTRACT(_input_json, '$[0].updates');
   	set _maxIdx = JSON_LENGTH(@_mc_data);
    WHILE _idx < _maxIdx DO
        SET _companyLegalUnit = JSON_UNQUOTE(JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].companyLegalUnit')));
        SET _isApprovalRequired = JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].isApprovalRequired'));
        SET _id = JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].id'));

        UPDATE `makercheckerconfig` 
        SET `isApprovalRequired` = _isApprovalRequired
        WHERE `id` = _id and `companyLegalUnit` = _companyLegalUnit;

        SET _idx = _idx + 1;
    END WHILE;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS dbxdb.checkforpendingrequests_proc;
DELIMITER $$
CREATE PROCEDURE checkforpendingrequests_proc(
	IN _module VARCHAR(1024) CHARACTER SET UTF8 COLLATE utf8mb3_general_ci,
    IN _action VARCHAR(1024) CHARACTER SET UTF8 COLLATE utf8mb3_general_ci,
    IN _record VARCHAR(1024) CHARACTER SET UTF8 COLLATE utf8mb3_general_ci,
    IN _companyLegalUnitId VARCHAR(1024) CHARACTER SET UTF8 COLLATE utf8mb3_general_ci
)
BEGIN
	DECLARE i INT DEFAULT 1;
    DECLARE num_records INT;
    DECLARE current_module VARCHAR(50);
    DECLARE current_action VARCHAR(50);
    DECLARE current_legalUnit VARCHAR(50);
    DECLARE current_record VARCHAR(255);
    DECLARE requestDetails VARCHAR(1024);
    DECLARE temp_requestId VARCHAR(50);
    SET num_records = (SELECT MAX(LENGTH(_module) - LENGTH(REPLACE(_module, '|', ''))) + 1);
    DROP TEMPORARY TABLE IF EXISTS temp_checkforpendingrequests_results;
    CREATE TEMPORARY TABLE temp_checkforpendingrequests_results (
        requestId VARCHAR(50),
        requestDetails VARCHAR(1024)
    );
    WHILE i <= num_records DO
		SET temp_requestId = null;
        SET current_module = SUBSTRING_INDEX(SUBSTRING_INDEX(_module, '|', i), '|', -1);
        SET current_action = SUBSTRING_INDEX(SUBSTRING_INDEX(_action, '|', i), '|', -1);
        SET current_legalUnit = SUBSTRING_INDEX(SUBSTRING_INDEX(_companyLegalUnitId, '|', i), '|', -1);
        SET current_record = SUBSTRING_INDEX(SUBSTRING_INDEX(_record, '|', i), '|', -1);
        SET requestDetails = CONCAT(current_module, '_', current_action, '_', current_legalUnit, '_', current_record);
        SELECT requestId INTO temp_requestId FROM approvalrequests WHERE
            `recordId` COLLATE utf8_general_ci = current_record AND
            `module` COLLATE utf8_general_ci = current_module AND
            `companyLegalUnit` COLLATE utf8_general_ci = current_legalUnit AND
            `action` COLLATE utf8_general_ci = current_action AND
            `status` COLLATE utf8_general_ci = 'SID_PENDING';
        INSERT INTO temp_checkforpendingrequests_results (requestId, requestDetails)VALUES (temp_requestId, requestDetails);
        SET i = i + 1;
    END WHILE;
    SELECT * FROM temp_checkforpendingrequests_results;
    DROP TEMPORARY TABLE IF EXISTS temp_checkforpendingrequests_results;
END$$
DELIMITER ;

CREATE TABLE `tf_records_module_configurations` (
    `module_id` VARCHAR(255) PRIMARY KEY,
    `allowed_fields` JSON
);

CREATE TABLE `tf_records` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `module_id` VARCHAR(255),
    `record_data` JSON,
    `created_by` VARCHAR(255),
    `updated_by` VARCHAR(255),
    `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `tf_fk_module_id` FOREIGN KEY (`module_id`) REFERENCES `tf_records_module_configurations`(`module_id`),
    CONSTRAINT `tf_record_data` CHECK (JSON_VALID(`record_data`))
)AUTO_INCREMENT = 1000000;

DROP procedure IF EXISTS `account_action_approvers_proc`;

DELIMITER $$

CREATE PROCEDURE `account_action_approvers_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountIds text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 1000000;

SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",") from (`customeraction`)
						where 
							`customeraction`.`isAllowed` = '0'
						and `customeraction`.`Action_id` = _approvalActionList
                        and FIND_IN_SET(`customeraction`.`Account_id`, _accountIds)
                        and `customeraction`.`contractId` = _contractId
                        and `customeraction`.`coreCustomerId` = _cif);

SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);
SET @NumberOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;

SET @customerIdListWithNoAccountAccess = (SELECT group_concat(DISTINCT Customer_id SEPARATOR ",") from
                                            ( SELECT Customer_id, Account_id
                                            from customeraccounts
                                            where FIND_IN_SET(Account_id, _accountIds)
                                            group by Customer_id having
                                            count(Account_id) != @NumberOfAccounts ) AS tempcustomeraccounts);
										  
SET @customerIdListWithNoAccountAccess = IF(@customerIdListWithNoAccountAccess is null, '', @customerIdListWithNoAccountAccess);
SELECT 
	DISTINCT (`customer`.`id` ) AS id , (`customer`.`username`) AS userName , (`membergroup`.`Name`) AS groupId,
										(`customer`.`FirstName`) AS firstName , (`customer`.`LastName`) AS lastName
from 
	(`customer`
LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and 
`contractcustomers`.`contractId` = _contractId and `contractcustomers`.`coreCustomerId` = _cif)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
LEFT JOIN `contractfeatures` ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
	where 
        `contractfeatures`.`contractId` = _contractId
        and `contractfeatures`.`coreCustomerId` = _cif
        and `contractfeatures`.`featureId` = _featureId
		and FIND_IN_SET(`customeraccounts`.`Account_id`,  _accountIds)
		and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
        and NOT FIND_IN_SET(`customer`.`id`,  @customerIdList)
		and NOT FIND_IN_SET(`customer`.`id`,  @customerIdListWithNoAccountAccess)
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0
		and FIND_IN_SET(`customeraction`.`Action_id`,_approvalActionList) > 0;      

END$$

DELIMITER ;

CREATE TABLE `ld_records_module_configurations` (
    `module_id` VARCHAR(255) PRIMARY KEY,
    `allowed_fields` JSON
);

CREATE TABLE `ld_records` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `module_id` VARCHAR(255),
    `record_data` JSON,
    `created_by` VARCHAR(255),
    `updated_by` VARCHAR(255),
    `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    CONSTRAINT `ld_fk_module_id` FOREIGN KEY (`module_id`) REFERENCES `ld_records_module_configurations`(`module_id`),
    CONSTRAINT `ld_record_data` CHECK (JSON_VALID(`record_data`))
)AUTO_INCREMENT = 10000000;

DROP PROCEDURE IF EXISTS `contracts_with_activeaccounts`;
DELIMITER $$
$$

CREATE PROCEDURE `contracts_with_activeaccounts`(
IN _contractIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin

DECLARE FINISHED INTEGER DEFAULT 0;
DECLARE contractIds varchar(255) DEFAULT "" ;
DECLARE statusPoint VARCHAR(255) DEFAULT "";
DECLARE statuses CURSOR 
		FOR (select `accountId` from `contractaccounts` where FIND_IN_SET(contractId COLLATE utf8_general_ci,_contractIdList COLLATE utf8_general_ci));
DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET FINISHED = 1;

OPEN statuses;
getStatus: LOOP
FETCH statuses INTO statusPoint; 

IF FINISHED = 1 then
	LEAVE getStatus;
ELSE

	set @contracts = (select contractId from contractaccounts where CONVERT( `contractaccounts`.`accountId` USING utf8) = CONVERT(statusPoint USING utf8) 
COLLATE utf8_general_ci and statusDesc != 'closed');
	if @contracts is null then
	   set FINISHED = 0;
	else 
	    set contractIds = CONCAT(contractIds, IF(LENGTH(contractIds)>0, ',', ''), @contracts);
	
	END IF;
	END IF;
END LOOP getStatus;
CLOSE statuses;
select contractIds;
END$$
DELIMITER ;

ALTER TABLE `approvalrequests` MODIFY `reason` longtext;

DROP procedure IF EXISTS `fetch_request_history_proc`;
DELIMITER $$

create procedure fetch_request_history_proc(IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, IN _requestId varchar(50)  CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

	SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	IF @companyId is NULL THEN
	SET @companyId = "";
	END IF;

	SELECT
		`bbactedrequest`.`approvalId` AS `approvalId`,
		`bbactedrequest`.`requestId` AS `requestId`,
		`bbactedrequest`.`companyId` AS `companyId`,
		`bbactedrequest`.`createdby` AS `requestActedby`,
		`bbactedrequest`.`status` AS `status`,
		`bbactedrequest`.`comments` AS `comments`,
		`bbactedrequest`.`action` AS `action`,
        `bbactedrequest`.`groupName` AS `groupName`,
		`bbactedrequest`.`createdts` AS `actionts`,
		`bbactedrequest`.`softdeleteflag` AS `softdeleteflag`,
        `customer`.`UserName` AS `userName`,
        CASE
			WHEN `bbactedrequest`.`createdby` IS null THEN "System"
			ELSE
			concat_ws(
				" ",
				IF(LENGTH(`customer`.`FirstName`),`customer`.`FirstName`,NULL),
				IF(LENGTH(`customer`.`MiddleName`),`customer`.`MiddleName`,NULL),
				IF(LENGTH(`customer`.`LastName`),`customer`.`LastName`,NULL)
			)
        END AS `customerName`,
        `customer`.`FullName` AS `customerFullName`
	FROM
    ( `bbactedrequest`
    LEFT JOIN `customer` ON (`bbactedrequest`.`createdby` = `customer`.`id`))
    WHERE (`bbactedrequest`.`requestId` = _requestId OR `bbactedrequest`.`assocRequestId` = _requestId)
    AND  FIND_IN_SET(`bbactedrequest`.`companyId`, @companyId);

END$$
DELIMITER ;

DROP procedure IF EXISTS `systemroles_permission_proc`;
DELIMITER $$

CREATE PROCEDURE `systemroles_permission_proc`(
IN _roleIds varchar(500) CHARACTER SET UTF8 COLLATE utf8_general_ci)
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

DROP PROCEDURE IF EXISTS `GetEnrollCustomerViewDetailsInfo`;
DELIMITER $$
CREATE PROCEDURE `GetEnrollCustomerViewDetailsInfo`(
    IN _servicedefId VARCHAR(500) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
    IN _roleId VARCHAR(500) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
    IN _cif VARCHAR(500) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
    IN _companyLegalUnit VARCHAR(500) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN
	
    SELECT s.id, s.name serviceDefinitionName, s.serviceType, m.description serviceTypeName 
	FROM servicedefinition s, membergrouptype m 
	WHERE m.id = s.serviceType AND FIND_IN_SET(s.id, _servicedefId) > 0;

    SELECT id, name roleName FROM dbxdb.membergroup mg 
    WHERE FIND_IN_SET(mg.id, _roleId) > 0;

    SELECT coreCustomerId, accountId ,statusDesc accountStatus, ownerType  FROM contractaccounts c 
    WHERE FIND_IN_SET(c.coreCustomerId, _cif) > 0;

    SELECT coreCustomerId, contractId, signatoryGroupName FROM signatorygroup sig 
    WHERE FIND_IN_SET(sig.coreCustomerId, _cif) > 0;

    SELECT f.id feature, fd.displayName featureName, f.Status_id featureStatus, a.Action_id action,
    fa.status actionStatus, a.displayDescription actionDesc, a.displayName actionName, 
    fa.Type_id actionType, fa.companyLegalUnit 
    FROM feature f, featureaction fa, actiondisplaynamedescription a, featuredisplaynamedescription fd
    WHERE f.id = fa.Feature_id AND f.id = fd.Feature_id AND fa.id = a.Action_id 
    AND f.companyLegalUnit = fa.companyLegalUnit AND fa.companyLegalUnit = a.companyLegalUnit 
	AND a.companyLegalUnit = fd.companyLegalUnit
    AND a.Locale_id = 'en-US'
	AND fd.Locale_id = 'en-US'
    AND f.companyLegalUnit = _companyLegalUnit;

    SELECT limitGroupId, displayName limitGroupName FROM limitgroupdisplaynamedescription l WHERE l.localeId = 'en-US';

END$$
DELIMITER ;

/* SQL Scripts for DigitalWealth - Favorite Customer */
DROP TABLE IF EXISTS `inf_wlth_favorite_customer`;
CREATE TABLE `inf_wlth_favorite_customer` (
  `id` int(11) AUTO_INCREMENT,
  `customerId` VARCHAR(50) NOT NULL,
  `favoriteCustomerId` VARCHAR(2000),  
  `createdby` VARCHAR(50) NULL DEFAULT NULL,
  `createdts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` VARCHAR(50) NULL DEFAULT NULL,
  `lastmodifiedts` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_favorite_customer_id` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS `fetch_approvalqueue_proc`;
DELIMITER $$

CREATE PROCEDURE `fetch_approvalqueue_proc`(IN _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                               IN _transactionIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                               IN _requestIds varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                IN _featureactionlist text CHARACTER SET UTF8 COLLATE utf8_general_ci
                                                                )
MAINLABEL:BEGIN
        SET SESSION group_concat_max_len = 100000000;

        SET @combinedIds = (select group_concat(id SEPARATOR ',') from customer where combinedUserId = _customerId);

		IF @combinedIds is NULL THEN
			SET @combinedIds = _customerId;
		ELSE
			SET @combinedIds = concat(_customerId , ',' ,@combinedIds);
        END IF;

        SET @combinedIds = if(@combinedIds = '' OR @combinedIds = NULL, '\'\'', @combinedIds);

        SET _transactionIds = if(_transactionIds = '' OR _transactionIds = NULL, '\'\'', _transactionIds);
        SET _requestIds = if(_requestIds = '' OR _requestIds = NULL, '\'\'', _requestIds);

        SET @companyId = (select group_concat(concat(contractId,'_',coreCustomerId) SEPARATOR ',') from contractcustomers where customerId =_customerId);
        IF @companyId is NULL THEN
			SET @companyId = '';
		END IF;

        IF _featureactionlist is NULL THEN
			LEAVE MAINLABEL;
		END IF;

        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ',') FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN
			SET @customerMatrixIds = '';
		END IF;

        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ',') FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN
			SET @customerGroupIds = '';
		END IF;

        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ',') from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN
			SET @alreadyApprovedIds = '\'\'';
		END IF;

        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM requestapprovalmatrix
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
			SET @approvalRequestIds = '\'\'';
		END IF;

        SET @groupIds = @customerGroupIds;
        GROUPREQUESTS_EXTRACT: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ',') FROM signatorygrouprequestmatrix
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN
					SET @requestIds = '\'\'';
				END IF;
				SET @approvalRequestIds = if(@approvalRequestIds = '' OR @approvalRequestIds IS NULL, @requestIds, CONCAT(@approvalRequestIds, CONCAT(',',@requestIds) ));
			SET @SubStrLen = LENGTH(SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = MID(@groupIds, @SubStrLen + 2, @strLen);
			IF LENGTH(@groupIds) <= 0 THEN
			  LEAVE GROUPREQUESTS_EXTRACT;
			END IF;
		END LOOP GROUPREQUESTS_EXTRACT;

        IF @approvalRequestIds is NULL THEN
			SET @approvalRequestIds = '''';
		END IF;

        SET @features = (SELECT group_concat(Feature_id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN
			SET @features = '';
		END IF;

        SET @monetaryActions = (SELECT group_concat(id SEPARATOR ',') FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0);
        IF @monetaryActions is NULL THEN
			SET @monetaryActions = '';
		END IF;

		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ',') FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @monetaryActions));

        IF @companyRequestIds is NULL THEN
            SET @companyRequestIds = '';
        END IF;

        SET @requestIds = if(_requestIds = '\'\'',@companyRequestIds,_requestIds);
        SET @query = if(_transactionIds = '\'\''
	        ,concat('FIND_IN_SET(bbrequest.requestId, \'',@requestIds,'\') ')
	        ,concat('FIND_IN_SET(bbrequest.transactionId, \'',_transactionIds, '\') AND  FIND_IN_SET(bbrequest.featureActionId, \'',@monetaryActions,'\')') );

		SET @select_statement = concat('SELECT
					bbrequest.requestId,
                    bbrequest.assocRequestId,
					bbrequest.transactionId,
					bbrequest.status,
					bbrequest.featureActionId,
                    bbrequest.isGroupMatrix,
                    bbrequest.companyId,
					bbrequest.accountId,
                    bbrequest.additionalMeta,
                    bbrequest.companyLegalUnit,
					bbrequest.createdts,
                    (CASE
						WHEN `bbrequest`.`createdby` IN (',@combinedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `amICreator`,
					(CASE
						WHEN  bbrequest.requestId IN (',@approvalRequestIds,') THEN \'true\'
						ELSE \'false\'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (',@alreadyApprovedIds,') THEN \'true\'
						ELSE \'false\'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = \'Approved\' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = \'0\')
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
							(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId))
							,
							SUM(
								CASE approvalrule.numberOfApprovals
									WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
									WHEN NULL OR \'\' THEN 0
									ELSE approvalrule.numberOfApprovals
								END
							)
						)
                        ELSE NULL
					END as requiredApprovals,
                    (select CONCAT(cst.FirstName, \' \', cst.LastName) from customer cst where id = bbrequest.createdby) as sentByName,
                    (select cst.userName from customer cst where id = bbrequest.createdby) as sentByUserName,
                    bbrequest.createdts
				FROM bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.assocRequestId'
				);

	PREPARE stmt FROM @select_statement;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `edit_customer_view_details_proc`;
DELIMITER $$
CREATE PROCEDURE `edit_customer_view_details_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN

SELECT DISTINCT
        `customer`.`UserName` AS `userName`,
        `customer`.`FirstName` AS `firstName`,
        `customer`.`MiddleName` AS `middleName`,
        `customer`.`LastName` AS `lastName`,
        CONCAT(IFNULL(`customer`.`FirstName`, ''),
                ' ',
                IFNULL(`customer`.`MiddleName`, ''),
                ' ',
                IFNULL(`customer`.`LastName`, '')) AS `name`,
        `customer`.`id` AS `customerId`,
        `customer`.`Ssn` AS `ssn`,
        `customer`.`createdts` AS `customerSince`,
        `customer`.`DateOfBirth` AS `dateOfBirth`,
		`customer`.`companyLegalUnit` AS `companyLegalUnit`,	
        IF((`customer`.`isCombinedUser` = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            `customer`.`CustomerType_id`) AS `customerTypeId`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail Banking,Business Banking',
            `customertype`.`Name`) AS `customerTypeName`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail and Business Banking User', 
            `customertype`.`Description`) AS `customerTypeDescription`,     
	    `customeraccounts`.`AccountName` AS `accountName`,
        `customeraccounts`.`Account_id` AS `accountId`,
        `customeraccounts`.`accountType` AS `accountType`,
        `customeraccounts`.`accountStatus` AS `accountStatus`,
		`contract`.`id` AS `contractId`,
        `contract`.`name` AS `contractName`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `contractcorecustomers`.`coreCustomerId` AS `coreCustomerId`,
		`contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `customergroup`.`Group_id` AS `userRole`,
        `membergroup`.`name` AS `userRoleName`,
        `contract`.`companyLegalUnit` as `legalEntityId`
    FROM
        (((((`customer`
        JOIN `backendidentifier` ON ((`customer`.`id` = `backendidentifier`.`Customer_id`)))
		JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
		JOIN `customeraccounts` ON ((`customer`.`id` = `customeraccounts`.`Customer_id`)))
		JOIN `contract` ON  ((`customeraccounts`.`contractId` = `contract`.`id`))
		LEFT JOIN `contractcustomers` ON (`contractcustomers`.`contractId` = `contract`.`id` and `contractcustomers`.`customerId` = _customerId)
		LEFT JOIN `servicedefinition` ON (`servicedefinition`.`id` = `contract`.`servicedefinitionId`)
        LEFT JOIN `membergrouptype` ON (`membergrouptype`.`id` = `servicedefinition`.`serviceType`)
        LEFT JOIN `contractcorecustomers` ON (`contractcorecustomers`.`contractId` = `contract`.`id`)
        LEFT JOIN `customergroup` ON (`customergroup`.`contractId` = `contract`.`id` AND 
			  `customergroup`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId` AND 
			  `customergroup`.`Customer_id` = _customerId)
        LEFT JOIN `membergroup` ON (`membergroup`.id = `customergroup`.`Group_id`)))
    WHERE  `customer`.`id` = _customerId 
		   AND `customergroup`.`Customer_id` = _customerId
		   AND `contractcustomers`.`customerId` = _customerId
           AND `contract`.`companyLegalUnit` = _legalEntityId
           AND `customeraccounts`.`companyLegalUnit` = _legalEntityId 
           AND `customeraccounts`.`Customer_id` = `customer`.`id`
           AND `customeraccounts`.`contractId` = `contractcustomers`.`contractId`;
END$$
DELIMITER ;

ALTER TABLE `approvalrequests` ADD COLUMN `viewDetailsResponse` longtext ;

DROP procedure IF EXISTS `edit_customer_view_details_proc`;
DELIMITER $$
CREATE PROCEDURE `edit_customer_view_details_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN

SELECT accountLockoutThreshold into @accountLockoutThreshold from passwordlockoutsettings ;

SELECT DISTINCT
        `customer`.`UserName` AS `userName`,
        `customer`.`FirstName` AS `firstName`,
        `customer`.`MiddleName` AS `middleName`,
        `customer`.`LastName` AS `lastName`,
        CONCAT(IFNULL(`customer`.`FirstName`, ''),
                ' ',
                IFNULL(`customer`.`MiddleName`, ''),
                ' ',
                IFNULL(`customer`.`LastName`, '')) AS `name`,
        `customer`.`id` AS `customerId`,
        `customer`.`Ssn` AS `ssn`,
        `customer`.`createdts` AS `customerSince`,
        `customer`.`DateOfBirth` AS `dateOfBirth`,
		`customer`.`companyLegalUnit` AS `companyLegalUnit`,	
        IF(`customer`.`Status_id`= 'SID_CUS_SUSPENDED',`customer`.`Status_id`, 
        IF(`customer`.`lockCount`+1 >= @accountLockoutThreshold, 'SID_CUS_LOCKED',`customer`.`Status_id`)) AS `customerStatusId`,
        `customerstatus`.`Description` AS `customerStatusName`,
        IF((`customer`.`isCombinedUser` = '1'),
            'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',
            `customer`.`CustomerType_id`) AS `customerTypeId`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail Banking,Business Banking',
            `customertype`.`Name`) AS `customerTypeName`,
        IF((`customer`.`isCombinedUser` = '1'),
            'Retail and Business Banking User', 
            `customertype`.`Description`) AS `customerTypeDescription`,     
	    `customeraccounts`.`AccountName` AS `accountName`,
        `customeraccounts`.`Account_id` AS `accountId`,
        `customeraccounts`.`accountType` AS `accountType`,
        `customeraccounts`.`accountStatus` AS `accountStatus`,
		`contract`.`id` AS `contractId`,
        `contract`.`name` AS `contractName`,
        `contract`.`servicedefinitionId` AS `serviceDefinitionId`,
        `servicedefinition`.`name` AS `serviceDefinitionName`,
        `membergrouptype`.`description` AS `serviceDefinitionType`,
        `contractcorecustomers`.`coreCustomerId` AS `coreCustomerId`,
		`contractcorecustomers`.`coreCustomerName` AS `coreCustomerName`,
        `customergroup`.`Group_id` AS `userRole`,
        `membergroup`.`name` AS `userRoleName`,
        `contract`.`companyLegalUnit` as `legalEntityId`
    FROM
        ((((((`customer`
        JOIN `backendidentifier` ON ((`customer`.`id` = `backendidentifier`.`Customer_id`)))
		JOIN `customertype` ON ((`customer`.`CustomerType_id` = `customertype`.`id`)))
		JOIN `customeraccounts` ON ((`customer`.`id` = `customeraccounts`.`Customer_id`)))
		JOIN `contract` ON  ((`customeraccounts`.`contractId` = `contract`.`id`))
        LEFT JOIN `status` `customerstatus` ON ((`customer`.`Status_id` = `customerstatus`.`id`)))
		LEFT JOIN `contractcustomers` ON (`contractcustomers`.`contractId` = `contract`.`id` and `contractcustomers`.`customerId` = _customerId)
		LEFT JOIN `servicedefinition` ON (`servicedefinition`.`id` = `contract`.`servicedefinitionId`)
        LEFT JOIN `membergrouptype` ON (`membergrouptype`.`id` = `servicedefinition`.`serviceType`)
        LEFT JOIN `contractcorecustomers` ON (`contractcorecustomers`.`contractId` = `contract`.`id`)
        LEFT JOIN `customergroup` ON (`customergroup`.`contractId` = `contract`.`id` AND 
			  `customergroup`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId` AND 
			  `customergroup`.`Customer_id` = _customerId)
        LEFT JOIN `membergroup` ON (`membergroup`.id = `customergroup`.`Group_id`)))
    WHERE  `customer`.`id` = _customerId 
		   AND `customergroup`.`Customer_id` = _customerId
		   AND `contractcustomers`.`customerId` = _customerId
           AND `contract`.`companyLegalUnit` = _legalEntityId
           AND `customeraccounts`.`companyLegalUnit` = _legalEntityId 
           AND `customeraccounts`.`Customer_id` = `customer`.`id`
           AND `customeraccounts`.`contractId` = `contractcustomers`.`contractId`;
END$$
DELIMITER ;

drop procedure if exists fetch_new_signatory_group_user_details;
DELIMITER $$

create procedure fetch_new_signatory_group_user_details(IN customerIdList varchar(4096) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                      coreCustomerIdInput varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    SET SESSION group_concat_max_len = 100000000;
    SELECT
    customer.UserName as username,
    membergroup.Name AS role,
    customer.id as customerId,
    CURRENT_TIMESTAMP as addedts,
    CONCAT(customer.FirstName, ' ', customer.LastName) as customerName
FROM customer
JOIN customergroup ON customer.id = customergroup.Customer_id
JOIN membergroup ON customergroup.Group_id = membergroup.id
WHERE customergroup.coreCustomerId = coreCustomerIdInput
  AND FIND_IN_SET(customer.id, customerIdList);
END$$
DELIMITER ;


DROP PROCEDURE if exists get_create_approvalrule_req_view_details;
DELIMITER $$
CREATE PROCEDURE get_create_approvalrule_req_view_details(
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _actionId varchar(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _groupIds varchar(1000) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

select c.name contractName, s.name servicedefinitionName , ct.Name serviceType , 
c.id , cc.coreCustomerId , cc.coreCustomerName, cc.isPrimary 
from 
contract c, 
contractcorecustomers cc , 
servicedefinition s , 
customertype ct
where 
c.id = cc.contractId and 
c.servicedefinitionId = s.id and 
c.serviceType = ct.id and
cc.coreCustomerId = _coreCustomerId and 
c.id = _contractId ;

select distinct Feature_id , feature_name , feature_status_id , feature_Type_id , action_name , action_Type_id 
from feature_actions_view fav where id= _actionId and companyLegalUnit = _legalEntityId;
 
select s.signatoryGroupId , s.signatoryGroupName  from signatorygroup  s where find_in_set(signatoryGroupId, _groupIds) ;

select c.ownerType, c.accountName, a.displayName from contractaccounts c join accounttype a on c.typeId = a.TypeID
where c.accountId = _accountId;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_signatory_viewdetails_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_signatory_viewdetails_proc`(
in contractId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in customerIdList varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI,
in action varchar(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
)
BEGIN

DECLARE approvedCustIdList VARCHAR(50);

		IF signatoryGroupId IS NOT NULL THEN
		
			SELECT 	
				sg.signatoryGroupId, 
				sg.signatoryGroupName, 
				sg.signatoryGroupDescription,
				sg.coreCustomerId,
				c.id AS contractId,
				c.name AS contractName,
				c.serviceType,
				ct.name AS serviceTypeName,
				c.servicedefinitionid,
				s.name AS serviceName,
				cc.coreCustomerName 
				FROM signatorygroup AS sg
					LEFT JOIN contract AS c
					ON sg.contractId = c.id
					LEFT JOIN servicedefinition AS s
					ON c.servicedefinitionid = s.id
					LEFT JOIN contractcorecustomers AS cc
					ON cc.contractId = c.id
					LEFT JOIN customertype AS ct
					ON ct.id = c.serviceType
				WHERE sg.signatoryGroupId = signatoryGroupId;
				
			IF customerIdList IS NULL THEN
				SET customerIdList = (SELECT group_concat(customerId SEPARATOR ',')
					FROM customersignatorygroup AS csg
					WHERE csg.signatoryGroupId = signatoryGroupId);
			END IF;		
	
			IF coreCustomerId IS NULL THEN
				SET coreCustomerId = (SELECT sg.coreCustomerId FROM signatorygroup AS sg				
					WHERE sg.signatoryGroupId = signatoryGroupId);
			END IF;

			IF action = 'EDIT_SIGNATORY_GROUP' THEN	
				
				SET approvedCustIdList = (SELECT group_concat(customerId SEPARATOR ',')
					FROM customersignatorygroup AS csg
					WHERE csg.signatoryGroupId = signatoryGroupId);
			
				SELECT cg.Customer_id,c.UserName AS userName,
						mg.name AS role, 
						concat(c.FirstName,' ', c.LastName) AS customerName
					FROM customergroup AS cg
					LEFT JOIN membergroup AS mg
					ON cg.Group_id = mg.id
					LEFT JOIN customer AS c
					ON cg.Customer_id = c.id
					WHERE cg.coreCustomerId = coreCustomerId AND find_in_set(cg.Customer_id, approvedCustIdList);	
			END IF;	
			
		END IF;
		
		IF signatoryGroupId IS NULL THEN

			SELECT 
				c.id AS contractId,
				c.name AS contractName,
				c.serviceType,
				ct.name AS serviceTypeName,
				c.servicedefinitionid,
				s.name AS serviceName,
				cc.coreCustomerName 
				FROM contract AS c 
					LEFT JOIN servicedefinition AS s
					ON c.servicedefinitionid = s.id
					LEFT JOIN contractcorecustomers AS cc
					ON cc.contractId = c.id
					LEFT JOIN customertype AS ct
					ON ct.id = c.serviceType
				WHERE c.id = contractId AND 
					cc.coreCustomerId = coreCustomerId;
				
		END IF;		
	IF (customerIdList IS NOT NULL AND coreCustomerId IS NOT NULL) THEN
		SELECT cg.Customer_id,c.UserName AS userName,
				mg.name AS role, 
				concat(c.FirstName,' ', c.LastName) AS customerName
			FROM customergroup AS cg
			LEFT JOIN membergroup AS mg
			ON cg.Group_id = mg.id
			LEFT JOIN customer AS c
			ON cg.Customer_id = c.id
			WHERE cg.coreCustomerId = coreCustomerId AND find_in_set(cg.Customer_id, customerIdList);
	END IF;

END$$
DELIMITER ;


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
            	set @queryStatement = concat(@search_select_statement, @queryStatement, " group by paginatedCustomers.id ,
				organisationemployees.isAuthSignatory,
				PrimaryPhone.value,PrimaryEmail.value ,
				address.addressLine1 ,
				address.addressLine2 ,
				city.Name,
				address.zipCode ,
				country.Name ,
				customer.isEnrolled ,
                customer.ApplicantChannel, customer.createdts,
				customerlegalentity.legalEntityId,
				customerlegalentity.legalEntityId 
				");
           
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

ALTER TABLE `makercheckerconfig` modify `viewDetailsAPI` varchar(200);

ALTER TABLE `signatorygroup` modify `signatoryGroupDescription` varchar(200);

DROP procedure IF EXISTS `account_action_approvers_proc`;
DELIMITER $$

CREATE PROCEDURE `account_action_approvers_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountIds text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    SET SESSION group_concat_max_len = 1000000;

    IF _accountIds is NULL THEN
        SET _accountIds = '';
    END IF;


    SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",")
                           from (`customeraction`)
                           where
                            `customeraction`.`Action_id` = _approvalActionList
                             and `customeraction`.`isAllowed` = '1'
                             -- and FIND_IN_SET(`customeraction`.`Account_id`, _accountIds)
                             and `customeraction`.`contractId` = _contractId
                             and `customeraction`.`coreCustomerId` = _cif);

    IF _accountIds = '' THEN
        SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",")
                               from (`customeraction`)
                               where `customeraction`.`Action_id` = _approvalActionList
                                 and `customeraction`.`contractId` = _contractId
                                 and `customeraction`.`coreCustomerId` = _cif);
        -- select @customerIdList;
    END IF;


    SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);
    SET @NumberOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;
    #     select @NumberOfAccounts;
#     select @_accountIds;
    SET @customerIdListWithNoAccountAccess = (SELECT group_concat(DISTINCT Customer_id SEPARATOR ",")
                                              from (SELECT Customer_id, Account_id
                                                    from customeraccounts
                                                    where FIND_IN_SET(Account_id, _accountIds)
                                                    group by Customer_id
                                                    having count(Account_id) != @NumberOfAccounts) AS tempcustomeraccounts);
# select @customerIdListWithNoAccountAccess;
    SET @customerIdListWithNoAccountAccess =
            IF(@customerIdListWithNoAccountAccess is null, '', @customerIdListWithNoAccountAccess);

# select @customerIdListWithNoAccountAccess;
    IF _accountIds = '' THEN
        SELECT DISTINCT (`customer`.`id`)        AS id,
                        (`customer`.`username`)  AS userName,
                        (`membergroup`.`Name`)   AS groupId,
                        (`customer`.`FirstName`) AS firstName,
                        (`customer`.`LastName`)  AS lastName
        from (`customer`
            LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and
                                              `contractcustomers`.`contractId` = _contractId and
                                              `contractcustomers`.`coreCustomerId` = _cif)
            LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
            LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
            INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `contractfeatures`
              ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
        where `contractfeatures`.`contractId` = _contractId
          and `contractfeatures`.`coreCustomerId` = _cif
          and `contractfeatures`.`featureId` = _featureId
#           and FIND_IN_SET(`customeraccounts`.`Account_id`, _accountIds)
          and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
          and

            ( FIND_IN_SET(`customer`.`id`, @customerIdList)
          OR FIND_IN_SET(`customer`.`id`, @customerIdListWithNoAccountAccess) )

          and FIND_IN_SET(`groupactionlimit`.`Action_id`, _approvalActionList) > 0
          and FIND_IN_SET(`customeraction`.`Action_id`, _approvalActionList) > 0;
    ELSE
        SELECT DISTINCT (`customer`.`id`)        AS id,
                        (`customer`.`username`)  AS userName,
                        (`membergroup`.`Name`)   AS groupId,
                        (`customer`.`FirstName`) AS firstName,
                        (`customer`.`LastName`)  AS lastName
        from (`customer`
            LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and
                                              `contractcustomers`.`contractId` = _contractId and
                                              `contractcustomers`.`coreCustomerId` = _cif)
            LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
            LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
            INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `contractfeatures`
              ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
        where `contractfeatures`.`contractId` = _contractId
          and `contractfeatures`.`coreCustomerId` = _cif
          and `contractfeatures`.`featureId` = _featureId
          and FIND_IN_SET(`customeraccounts`.`Account_id`, _accountIds)
          and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
          and
            ( FIND_IN_SET(`customer`.`id`, @customerIdList)
          OR FIND_IN_SET(`customer`.`id`, @customerIdListWithNoAccountAccess) )
          and FIND_IN_SET(`groupactionlimit`.`Action_id`, _approvalActionList) > 0
          and FIND_IN_SET(`customeraction`.`Action_id`, _approvalActionList) > 0;
    END IF;


END$$

DELIMITER ;

drop procedure approvalmatrix_fetch_records_proc;
DELIMITER $$

create procedure approvalmatrix_fetch_records_proc(IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                        IN _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                        IN _accountId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                        IN _limitTypeId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                                                        IN _actions text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    IF _cif IS NULL THEN
        SET _cif = "%";
    END IF;

    IF _accountId IS NULL THEN
        SET _accountId = "%";
    END IF;

    IF _limitTypeId IS NULL THEN
        SET _limitTypeId = "%";
    END IF;

    IF _cif = "" THEN
        SET _cif = "%";
    END IF;

    IF _accountId = "" THEN
        SET _accountId = "%";
    END IF;

    IF _limitTypeId = "" THEN
        SET _limitTypeId = "%";
    END IF;

    SELECT `approvalMatrix`.`id`,
           `approvalMatrix`.`contractId`,
           `approvalMatrix`.`accountId`,
           `approvalMatrix`.`limitTypeId`,
           `featureAction`.`id`                       AS `actionId`,
           `featureAction`.`name`                     AS `actionName`,
           `featureAction`.`description`              AS `actionDescription`,
           `featureAction`.`Feature_id`               AS `featureId`,
           `featureAction`.`Type_id`                  AS `actionType`,
           `featureAction`.`isAccountLevel`           AS `isAccountLevel`,
           `feature`.`name`                           AS `featureName`,
           `feature`.`Status_id`                      AS `fifeaturestatus`,
           `approvalRule`.`id`                        AS `approvalruleId`,
           `approvalRule`.`numberOfApprovals`,
           `approvalRule`.`name`                      AS `approvalRuleName`,
           `approvalMatrix`.`lowerlimit`,
           `approvalMatrix`.`upperlimit`,
           `approvalMatrix`.`currency`,
           `customer`.`id`                            AS `customerId`,
           `customer`.`FirstName`                     AS `firstName`,
           `customer`.`LastName`                      AS `lastName`,
           `contractcorecustomers`.`coreCustomerId`   AS `cifId`,
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
               and `approvalMatrix`.`contractId` = `contractfeatures`.`contractId`
               and `approvalMatrix`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
        LEFT JOIN
        `contractcorecustomers` AS `contractcorecustomers`
          ON `approvalMatrix`.`contractId` = `contractcorecustomers`.`contractId` AND
             `approvalMatrix`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)
    WHERE `approvalMatrix`.`contractId` = `_contractId`
      AND `approvalMatrix`.`coreCustomerId` LIKE `_cif`
      AND `approvalMatrix`.`accountId` LIKE `_accountId`
      AND FIND_IN_SET(`approvalMatrix`.`actionId`, `_actions`) > 0
      AND `approvalMatrix`.`limitTypeId` LIKE `_limitTypeId`
      AND `approvalMatrix`.`softdeleteflag` = 0
      AND `featureAction`.`approveFeatureAction` is not null
      AND `featureAction`.`approveFeatureAction` != ''
      AND `featureAction`.`status` = 'SID_ACTION_ACTIVE'
    ORDER BY `approvalMatrix`.`contractId`, `approvalMatrix`.`coreCustomerId`, `approvalMatrix`.`accountId`,
             `approvalMatrix`.`limitTypeId`, `approvalMatrix`.`actionId`, `approvalMatrix`.`lowerlimit`;
END $$
DELIMITER ;


DROP procedure IF EXISTS `signatorygroup_create_proc`;

DELIMITER $$
create procedure signatorygroup_create_proc(IN signatoryGroupValues text CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN customerSignatoryGroupValues text CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupDescription varchar(200) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN createdby varchar(50)CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    DECLARE index1 INTEGER DEFAULT 0;

    IF signatoryGroupValues IS NOT NULL AND signatoryGroupValues != '' THEN

        set @matrixRecord = SUBSTRING_INDEX(signatoryGroupValues, ',', 1);
        set @matrixComma = REPLACE(@matrixRecord, ';', ',');
        set @query = concat(
                'INSERT INTO signatorygroup(signatoryGroupId,signatoryGroupName,signatoryGroupDescription,coreCustomerId,contractId,createdby) values (',
                @matrixComma, ');');
prepare sql_query from @query;
execute sql_query;
END IF;

    IF signatoryGroupValues IS NULL OR signatoryGroupValues = '' THEN

        INSERT INTO signatorygroup(signatorygroup.signatoryGroupId, signatorygroup.signatoryGroupName,
                                   signatorygroup.signatoryGroupDescription, signatorygroup.coreCustomerId,
                                   signatorygroup.contractId, signatorygroup.createdby)
        values (signatoryGroupId, signatoryGroupName, signatoryGroupDescription, coreCustomerId, contractId, createdby);

END IF;


    IF customerSignatoryGroupValues IS NOT NULL AND customerSignatoryGroupValues != '' THEN
        set @length1 = LENGTH(customerSignatoryGroupValues) - LENGTH(REPLACE(customerSignatoryGroupValues, ',', ''));
        set index1 = 0;
        addSignatories:
        LOOP
            set index1 = index1 + 1;
            IF index1 = @length1 + 1 THEN
                LEAVE addSignatories;
ELSE
                set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(customerSignatoryGroupValues, ',', index1), ',', -1);
                set @signatoriesComma = REPLACE(@signatory, ';', ',');
                set @query = concat(
                        'INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (',
                        @signatoriesComma, ');');
prepare sql_query from @query;
execute sql_query;
ITERATE addSignatories;
END IF;
END LOOP addSignatories;
END IF;
END $$
DELIMITER ;

DROP PROCEDURE IF EXISTS `get_campaign_proc`;
DELIMITER $$

CREATE PROCEDURE `get_campaign_proc`(
	_eventCode varchar(50),
	_status varchar(50)
)
BEGIN
	
if(length(_eventCode)>0 and length(_status)>0)
then
	select cd.*
	from
	`campaigndefinition` `cd`,
	`campaigneventtrigger` `cet`,
	`eventtriggers` `et`
	where
	`cd`.`campaignId` = `cet`.`campaignId` and
	`cet`.`eventTriggerId` = `et`.`eventTriggerId` and
	`et`.`eventCode` = _eventCode and
	`cd`.`campaignStatus` = _status;
	
elseif(length(_eventCode)>0)
then
	select cd.*
	from
	`campaigndefinition` `cd`,
	`campaigneventtrigger` `cet`,
	`eventtriggers` `et`
	where
	`cd`.`campaignId` = `cet`.`campaignId` and
	`cet`.`eventTriggerId` = `et`.`eventTriggerId` and
	`et`.`eventCode` = _eventCode;
    
elseif(length(_status)>0)
then
	select * from `campaigndefinition` where campaignStatus = _status;
else
	select * from `campaigndefinition`;
end if;


select * from `campaigneventtrigger` ;
select * from `campaignprofile`; 
select * from `campaignchanneltype`; 
select * from `campaignchanneldetails`;  
select * from `offlinetemplate`; 
select * from `onlinecontent` where `campaignId`  is not null;

select * from `profile` where `profileId`  in (select `profileId`  from `campaignprofile`);
select * from `profilecondition` where `profileId`  in (select `profileId`  from `campaignprofile`) ;
select * from `placeholder` where `placeholderId`  in (select `placeholderId`  from `onlinecontent` where `campaignId`  is not null);

if(length(_eventCode)>0 and length(_status)>0)
then
	select * from `eventtriggers` where eventTriggerId  in (select cet.eventTriggerId
	from
	campaigneventtrigger cet left outer join
	eventtriggers et
	on
	cet.eventTriggerId = et.eventTriggerId and
	et.eventCode = _eventCode where cet.campaignId in (select campaignId  from campaigndefinition c where campaignStatus =_status));
	
elseif(length(_eventCode)>0)
then
	select * from `eventtriggers` where eventTriggerId  in (select cet.eventTriggerId
	from
	campaigneventtrigger cet left outer join
	eventtriggers et
	on
	cet.eventTriggerId = et.eventTriggerId and
	et.eventCode = _eventCode);
elseif (length(_status)>0)
then
	select * from eventtriggers where eventTriggerId in (select eventTriggerId from campaigneventtrigger c where campaignId in (select campaignId  from campaigndefinition c where campaignStatus =_status));
else
	select * from `eventtriggers` where eventTriggerId  in (select `eventTriggerId` from `campaigneventtrigger`);
end if;

select * from `datacontext` where `dataContextId`  in (select `dataContextId`  from `profilecondition` where `profileId`  in (select `profileId`  from `campaignprofile`) );

END $$
DELIMITER ;


DROP procedure IF EXISTS `signatorygroup_create_proc`;

DELIMITER $$
create procedure signatorygroup_create_proc(IN signatoryGroupValues text CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN customerSignatoryGroupValues text CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN signatoryGroupDescription varchar(200) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
                                            IN createdby varchar(50)CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
    DECLARE index1 INTEGER DEFAULT 0;

    IF signatoryGroupValues IS NOT NULL AND signatoryGroupValues != '' THEN

        set @matrixRecord = SUBSTRING_INDEX(signatoryGroupValues, ',', 1);
        set @matrixComma = REPLACE(@matrixRecord, ';', ',');
        set @query = concat(
                'INSERT INTO signatorygroup(signatoryGroupId,signatoryGroupName,signatoryGroupDescription,coreCustomerId,contractId,createdby) values (',
                @matrixComma, ');');
prepare sql_query from @query;
execute sql_query;
END IF;

    IF signatoryGroupValues IS NULL OR signatoryGroupValues = '' THEN

        INSERT INTO signatorygroup(signatorygroup.signatoryGroupId, signatorygroup.signatoryGroupName,
                                   signatorygroup.signatoryGroupDescription, signatorygroup.coreCustomerId,
                                   signatorygroup.contractId, signatorygroup.createdby)
        values (signatoryGroupId, signatoryGroupName, signatoryGroupDescription, coreCustomerId, contractId, createdby);

END IF;


    IF customerSignatoryGroupValues IS NOT NULL AND customerSignatoryGroupValues != '' THEN
        set @length1 = LENGTH(customerSignatoryGroupValues) - LENGTH(REPLACE(customerSignatoryGroupValues, ',', ''));
        set index1 = 0;
        addSignatories:
        LOOP
            set index1 = index1 + 1;
            IF index1 = @length1 + 1 THEN
                LEAVE addSignatories;
ELSE
                set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(customerSignatoryGroupValues, ',', index1), ',', -1);
                set @signatoriesComma = REPLACE(@signatory, ';', ',');
                set @query = concat(
                        'INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (',
                        @signatoriesComma, ');');
prepare sql_query from @query;
execute sql_query;
ITERATE addSignatories;
END IF;
END LOOP addSignatories;
END IF;
END $$
DELIMITER ;


DROP procedure IF EXISTS `signatorygroup_update_proc`;

DELIMITER $$
create PROCEDURE `signatorygroup_update_proc`(
    IN _sigGroupValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _newSigValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _deleteSigValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    DECLARE index1 INTEGER DEFAULT 0;
    SET _sigGroupValues = REPLACE(_sigGroupValues, '""', '"');
    set @sigGroupId = SUBSTRING_INDEX(_sigGroupValues, ';', 1);
    set @SigGroupName = SUBSTRING_INDEX(SUBSTRING_INDEX(_sigGroupValues, ';', 2), ';', -1);
    set @SigGroupDes = SUBSTRING_INDEX(SUBSTRING_INDEX(_sigGroupValues, ';', 3), ';', -1);
    set @sigCreatedBy = SUBSTRING_INDEX(_sigGroupValues, ';', -1);
    IF @SigGroupName IS NOT NULL AND @SigGroupName != '' THEN
        set @query =
                concat('UPDATE signatorygroup SET signatoryGroupName = ', @SigGroupName, ' WHERE signatoryGroupId = ',
                       @sigGroupId, '');
prepare sql_query from @query;
execute sql_query;
END IF;
    IF @sigGroupDes IS NOT NULL AND @sigGroupDes != '' THEN
        set @query = concat('UPDATE signatorygroup SET signatoryGroupDescription = ', @sigGroupDes,
                            ',lastmodifiedts = CURRENT_TIMESTAMP, modifiedby = ', @sigCreatedBy,
                            '  WHERE signatoryGroupId = ', @sigGroupId, '');
prepare sql_query from @query;
execute sql_query;
END IF;
    IF _newSigValues IS NOT NULL AND _newSigValues != '' THEN
        set @length1 = LENGTH(_newSigValues) - LENGTH(REPLACE(_newSigValues, ',', '')) + 1;
        set index1 = 0;
        addSignatories:
        LOOP
            set index1 = index1 + 1;
            IF index1 = @length1 + 1 THEN
                LEAVE addSignatories;
ELSE
                set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(_newSigValues, ',', index1), ',', -1);
                set @signatoriesComma = REPLACE(@signatory, ';', ',');
                set @query = concat(
                        'INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (',
                        @signatoriesComma, ');');
prepare sql_query from @query;
execute sql_query;
ITERATE addSignatories;
END IF;
END LOOP addSignatories;
END IF;
    IF _deleteSigValues IS NOT NULL AND _deleteSigValues != '' THEN
        set @length1 = LENGTH(_deleteSigValues) - LENGTH(REPLACE(_deleteSigValues, ',', '')) + 1;
        set index1 = 0;
        deleteSignatories:
        LOOP
            set index1 = index1 + 1;
            IF index1 = @length1 + 1 THEN
                LEAVE deleteSignatories;
ELSE
                set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(_deleteSigValues, ',', index1), ',', -1);
                set @sigGroupId = SUBSTRING_INDEX(@signatory, ';', 1);
                set @custId = SUBSTRING_INDEX(SUBSTRING_INDEX(@signatory, ';', 2), ';', -1);
                set @query = concat('DELETE FROM customersignatorygroup WHERE signatoryGroupId =', @sigGroupId,
                                    'AND customerId=', @custId, '');
prepare sql_query from @query;
execute sql_query;
ITERATE deleteSignatories;
END IF;
END LOOP deleteSignatories;
END IF;

END $$
DELIMITER ;

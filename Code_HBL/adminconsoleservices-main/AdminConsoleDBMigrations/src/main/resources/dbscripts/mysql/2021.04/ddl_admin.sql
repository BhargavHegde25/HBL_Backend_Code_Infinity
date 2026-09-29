ALTER TABLE `csrassistgrant` ADD COLUMN `accountId` varchar(50) DEFAULT NULL AFTER `userName`;

ALTER TABLE `approvalmatrix` ADD COLUMN `isGroupMatrix` tinyint(1) NOT NULL DEFAULT '0' AFTER `approvalruleId`;
ALTER TABLE `requestapprovalmatrix` ADD COLUMN `isGroupRule` tinyint(1) NOT NULL DEFAULT '0' AFTER `receivedApprovals`;
ALTER TABLE `bbactedrequest` ADD COLUMN `groupName` varchar(55) NULL DEFAULT NULL AFTER `action`;
ALTER TABLE `application` MODIFY COLUMN `newSettings` TINYINT(1) NOT NULL DEFAULT '1';

DROP TABLE IF EXISTS `signatorygroup`;
CREATE TABLE `signatorygroup` (
    `signatoryGroupId` VARCHAR(50) NOT NULL,
    `signatoryGroupName` VARCHAR(50) NULL DEFAULT NULL,
    `signatoryGroupDescription` VARCHAR(150) NULL DEFAULT NULL,
    `coreCustomerId` VARCHAR(50) NULL DEFAULT NULL,
    `contractId` VARCHAR(50) NULL DEFAULT NULL,
	`createdby` varchar(50) DEFAULT NULL,
	`modifiedby` varchar(50) DEFAULT NULL,
	`createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`signatoryGroupId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `customersignatorygroup`;
CREATE TABLE `customersignatorygroup` (
    `customerSignatoryGroupId` VARCHAR(50) NOT NULL,
    `signatoryGroupId` VARCHAR(50) NULL DEFAULT NULL,
    `customerId` VARCHAR(50) NULL DEFAULT NULL,
	`createdby` varchar(50) DEFAULT NULL,
	`modifiedby` varchar(50) DEFAULT NULL,
	`createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`customerSignatoryGroupId`),    
    KEY `FK_customersignatorygroup_customerId` (`customerId`),
    KEY `FK_customersignatorygroup_signaoryGroupId` (`signatoryGroupId`),
    CONSTRAINT `FK_customersignatorygroup_customerId` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT `FK_customersignatorygroup_signaoryGroupId` FOREIGN KEY (`signatoryGroupId`) REFERENCES `signatorygroup` (`signatoryGroupId`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `signatorygroupmatrix`;
CREATE TABLE `signatorygroupmatrix` (
    `signatoryGroupMatrixId` BIGINT(20) NOT NULL AUTO_INCREMENT,
    `approvalMatrixId` BIGINT(20) NULL DEFAULT NULL,
    `groupList` TEXT NULL DEFAULT NULL,
    `groupRule` LONGTEXT NULL DEFAULT NULL,
	`createdby` varchar(50) DEFAULT NULL,
	`modifiedby` varchar(50) DEFAULT NULL,
	`createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`signatoryGroupMatrixId`),  
	KEY `FK_signatoryGroupMatrix_approvalMatrixId` (`approvalMatrixId`),
    CONSTRAINT `FK_signatoryGroupMatrix_approvalMatrixId` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrix` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `signatorygrouprequestmatrix`;
CREATE TABLE `signatorygrouprequestmatrix` (
    `signatoryGroupRequestMatrixId` VARCHAR(50) NOT NULL,
    `requestId` VARCHAR(50) NULL DEFAULT NULL,
    `approvalMatrixId` BIGINT(20) NULL DEFAULT NULL,
	`groupList` TEXT NULL DEFAULT NULL,
    `groupRuleValue` LONGTEXT NULL DEFAULT NULL,
	`pendingGroupList` TEXT NULL DEFAULT NULL,
    `isApproved` TINYINT(1) NOT NULL DEFAULT '0',
	`createdby` varchar(50) DEFAULT NULL,
	`modifiedby` varchar(50) DEFAULT NULL,
	`createdts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
	`lastmodifiedts` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	`synctimestamp` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
	`softdeleteflag` TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`signatoryGroupRequestMatrixId`),
	KEY `FK_signatoryGroupRequestMatrix_requestId` (`requestId`),
    KEY `FK_signatoryGroupRequestMatrix_approvalMatrixId` (`approvalMatrixId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

alter table `bulkpaymentrecord` add COLUMN `transactionAmount` varchar(45) DEFAULT NULL, add column `serviceCharge` varchar(45) DEFAULT NULL, 
add column `transactionCurrency` varchar(50) DEFAULT NULL;

alter table `achfile` add COLUMN `transactionAmount` varchar(45) DEFAULT NULL, add column `serviceCharge` varchar(45) DEFAULT NULL, 
add column `transactionCurrency` varchar(50) DEFAULT NULL;

alter table `achtransaction` add COLUMN `transactionAmount` varchar(45) DEFAULT NULL, add column `serviceCharge` varchar(45) DEFAULT NULL, 
add column `transactionCurrency` varchar(50) DEFAULT NULL;

DROP procedure IF EXISTS `get_signatorygroup_approvers_proc`;
DELIMITER $$
CREATE  PROCEDURE `get_signatorygroup_approvers_proc`(
in _groupList text
)
BEGIN
    set @execStmt  = concat("select customerId from customersignatorygroup where FIND_IN_SET(signatoryGroupId,'" , REPLACE(REPLACE(REPLACE(_groupList, '[', ''),']',''),' ','') , "')");
    PREPARE stmt FROM @execStmt;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP TABLE IF EXISTS `approvalmode`;
CREATE TABLE `approvalmode` (
    `id` VARCHAR(50) NOT NULL,
    `coreCustomerId` VARCHAR(50) NOT NULL,
    `contractId` VARCHAR(50) NOT NULL,
    `isGroupLevel` TINYINT(1) NOT NULL DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS `fetch_pending_group_list_proc`;
DELIMITER $$
CREATE PROCEDURE fetch_pending_group_list_proc
(in _requestId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)

BEGIN
	SELECT signatorygroup.signatoryGroupId, signatorygroup.signatoryGroupName  
	FROM signatorygroup 
	WHERE FIND_IN_SET(signatorygroup.signatoryGroupId,
	(SELECT group_concat(REPLACE(REPLACE(REPLACE(pendingGroupList,']',''),'[',''),'"',''))
		FROM signatorygrouprequestmatrix WHERE 
		signatorygrouprequestmatrix.requestId = _requestId AND signatorygrouprequestmatrix.isApproved = false));
END$$

DELIMITER ;

ALTER TABLE `bulkpaymentsubrecord` ADD COLUMN `errorDescription` varchar(100) ;
ALTER TABLE `bulkpaymentsubrecordmock` ADD COLUMN `errorDescription` varchar(100) ;

DROP PROCEDURE IF EXISTS `increment_receivedapprovals_proc`;

DELIMITER $$
CREATE PROCEDURE `increment_receivedapprovals_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _approvalMatrixId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	UPDATE requestapprovalmatrix SET `receivedApprovals` = `receivedApprovals` + 1 WHERE `requestId` = _requestId AND `approvalMatrixId` = _approvalMatrixId;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `fetch_customersignatorygroup_details_proc`;
DELIMITER $$
CREATE  PROCEDURE `fetch_customersignatorygroup_details_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SELECT 
	`customersignatorygroup`.`customerSignatoryGroupId` AS `customerSignatoryGroupId`,
	`customersignatorygroup`.`signatoryGroupId` AS `signatoryGroupId`,
	`customersignatorygroup`.`customerId` AS `customerId`,
	`signatorygroup`.`coreCustomerId` AS `coreCustomerId`,
	`signatorygroup`.`contractId` AS `contractId`,
	`signatorygroup`.`signatoryGroupName` AS `signatoryGroupName`,
	`signatorygroup`.`signatoryGroupDescription` AS `signatoryGroupDescription`
from 
	`customersignatorygroup`
LEFT JOIN `signatorygroup` ON (`signatorygroup`.`signatoryGroupId` = `customersignatorygroup`.`signatoryGroupId`)
	where 
        `customersignatorygroup`.`customerId` = _customerId;           
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS fetch_request_history_proc;

DELIMITER $$
CREATE PROCEDURE `fetch_request_history_proc`(
  IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
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
    WHERE `bbactedrequest`.`requestId` = _requestId
    AND  FIND_IN_SET(`bbactedrequest`.`companyId`, @companyId);
 
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

DROP procedure IF EXISTS `fetch_approvalqueueforgroup_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_approvalqueueforgroup_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _transactionIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _requestIds VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _featureactionlist TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL : BEGIN
        SET SESSION group_concat_max_len = 100000000 ;
        
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
        
        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ",") FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN      
			SET @customerGroupIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
		END IF;
        
        SET @approvalRequestIds = "";
        SET @groupIds = @customerGroupIds;
        do_this: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM signatorygrouprequestmatrix 
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0 );
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
                    (CASE
						WHEN `bbrequest`.`createdby` IN (",@combinedIds,") THEN 'true'
						ELSE 'false'
					 END) as `amICreator`,
					(CASE 
						WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
						ELSE 'false'
					END)
					 as `amIApprover`,
					(CASE
						WHEN `bbrequest`.`requestId` IN (",@alreadyApprovedIds,") THEN 'true'
						ELSE 'false'
					 END) as `actedByMeAlready`,
					(select count(DISTINCT(createdby)) from bbactedrequest where bbactedrequest.action = 'Approved' AND  bbactedrequest.requestId = bbrequest.requestId AND softdeleteflag = '0') 
							as receivedApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP VIEW IF EXISTS `alertsubtypetext_view`;		
CREATE VIEW `alertsubtypetext_view` as select
    `alertsubtype`.`id` as `alertsubtype_id`,
    `alertsubtype`.`AlertTypeId` as `alertsubtype_alertTypeId`,
    `alertsubtype`.`Name` as `alertsubtype_Name`,
    `alertsubtype`.`Status_id` as `alertsubtype_StatusId`,
    `alertsubtype`.`isAccountLevel` as `alertsubtype_isAccountLevel`,
    `alertsubtype`.`attributeId` as `alertsubtype_attributeId`,
    `alertsubtype`.`alertConditionId` as `alertsubtype_alertConditionId`,
    `alertsubtype`.`value1` as `alertsubtype_value1`,
    `alertsubtype`.`value2` as `alertsubtype_value2`,
    `alertsubtype`.`isGlobal` as `alertsubtype_isGlobal`,
    `alertsubtype`.`defaultFrequencyId` as `alertsubtype_defaultFrequencyId`,
    `alertsubtype`.`defaultFrequencyValue` as `alertsubtype_defaultFrequencyValue`,
    `alertsubtype`.`defaultFrequencyTime` as `alertsubtype_defaultFrequencyTime`,
    `alertsubtype`.`createdby` as `alertsubtype_createdby`,
    `alertsubtype`.`modifiedby` as `alertsubtype_modifiedby`,
    `alertsubtype`.`createdts` as `alertsubtype_createdts`,
    `alertsubtype`.`lastmodifiedts` as `alertsubtype_lastmodifiedts`,
    `alertsubtype`.`synctimestamp` as `alertsubtype_synctimestamp`,
    `alertsubtype`.`softdeleteflag` as `alertsubtype_softdeleteflag`,
    `alertsubtype`.`isAutoSubscribeEnabled` AS `alertsubtype_isAutoSubscribeEnabled`,
    `alertsubtypetext`.`languageCode` as `alertsubtypetext_languageCode`,
    `alertsubtypetext`.`description` as `alertsubtypetext_description`,
    `alertsubtype`.`externalSystem` AS `alertsubtype_externalSystem`,
    `alertsubtypetext`.`displayName` as `alertsubtypetext_displayName`
from
    (`alertsubtype`
join `alertsubtypetext` on
    ((`alertsubtypetext`.`alertSubTypeId` = `alertsubtype`.`id`)));
    
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

ALTER TABLE `bulkpaymentfiles` ADD COLUMN `requestStatus` varchar(20) ;
ALTER TABLE `bulkpaymentfilesmock` ADD COLUMN `requestStatus` varchar(20) ;

ALTER TABLE `bulkpaymentrecord` ADD COLUMN `errorDescription` varchar(250) ;
ALTER TABLE `bulkpaymentrecordmock` ADD COLUMN `errorDescription` varchar(250) ;

DROP procedure IF EXISTS `approvalmatrix_signatorygroupmatrixcreate_proc`;

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
				set @query = concat('INSERT INTO approvalmatrix(name,contractId,coreCustomerId,actionId,accountId,isGroupMatrix,limitTypeId,lowerlimit,upperlimit) VALUES (',@matrixComma,');');
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

DROP procedure IF EXISTS `signatorygroup_create_proc`;

DELIMITER $$

CREATE PROCEDURE `signatorygroup_create_proc`(
    IN signatoryGroupValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN customerSignatoryGroupValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE index1 INTEGER DEFAULT 0;
set @matrixRecord = SUBSTRING_INDEX(signatoryGroupValues, ',', 1 );
set @matrixComma = REPLACE(@matrixRecord, ';', ',');
set @query = concat('INSERT INTO signatorygroup(signatoryGroupId,signatoryGroupName,signatoryGroupDescription,coreCustomerId,contractId,createdby) values (',@matrixComma,');');
prepare sql_query from @query;
execute sql_query;   
IF customerSignatoryGroupValues IS NOT NULL AND customerSignatoryGroupValues != '' THEN
 set @length1 = LENGTH(customerSignatoryGroupValues) - LENGTH(REPLACE(customerSignatoryGroupValues, ',', ''));
 set index1 = 0;
 addSignatories: LOOP
 set index1 = index1 + 1;
   IF index1 = @length1 + 1 THEN
    LEAVE addSignatories;
   ELSE
    set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(customerSignatoryGroupValues, ',', index1), ',', -1);
    set @signatoriesComma = REPLACE(@signatory, ';', ',');
    set @query = concat('INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (',@signatoriesComma,');');
	prepare sql_query from @query;
    execute sql_query; 
	ITERATE addSignatories;
    END IF;
    END LOOP addSignatories;
END IF;
END$$

DELIMITER ;

DROP procedure IF EXISTS `signatorygroup_update_proc`;

DELIMITER $$

CREATE PROCEDURE `signatorygroup_update_proc`(
   IN _sigGroupValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
   IN _newSigValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
   IN _deleteSigValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE index1 INTEGER DEFAULT 0;
set @sigGroupId = SUBSTRING_INDEX(_sigGroupValues, ';', 1 );
set @SigGroupName = SUBSTRING_INDEX(SUBSTRING_INDEX(_sigGroupValues, ';', 2 ),';',-1);
set @sigGroupDes = SUBSTRING_INDEX(_sigGroupValues, ';', -1 );
IF @SigGroupName IS NOT NULL AND @SigGroupName != '' THEN
set @query = concat('UPDATE signatorygroup SET signatoryGroupName = ',@SigGroupName,' WHERE signatoryGroupId = ',@sigGroupId,'');
prepare sql_query from @query;
execute sql_query;
END IF;
IF @sigGroupDes IS NOT NULL AND @sigGroupDes != '' THEN
set @query = concat('UPDATE signatorygroup SET signatoryGroupDescription = ',@sigGroupDes,' WHERE signatoryGroupId = ',@sigGroupId,'');
prepare sql_query from @query;
execute sql_query;
END IF;
IF _newSigValues IS NOT NULL AND _newSigValues != '' THEN
set @length1 = LENGTH(_newSigValues) - LENGTH(REPLACE(_newSigValues, ',', ''))+1;
set index1 = 0;
addSignatories: LOOP
set index1 = index1 + 1;
IF index1 = @length1 + 1 THEN
LEAVE addSignatories;
ELSE
set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(_newSigValues, ',', index1), ',', -1);
set @signatoriesComma = REPLACE(@signatory, ';', ',');
set @query = concat('INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (',@signatoriesComma,');');
prepare sql_query from @query;
execute sql_query;
ITERATE addSignatories;
END IF;
END LOOP addSignatories;
END IF;
IF _deleteSigValues IS NOT NULL AND _deleteSigValues != '' THEN
set @length1 = LENGTH(_deleteSigValues) - LENGTH(REPLACE(_deleteSigValues, ',', ''))+1;
set index1 = 0;
deleteSignatories: LOOP
set index1 = index1 + 1;
IF index1 = @length1 + 1 THEN
LEAVE deleteSignatories;
ELSE
set @signatory = SUBSTRING_INDEX(SUBSTRING_INDEX(_deleteSigValues, ',', index1), ',', -1);
set @sigGroupId = SUBSTRING_INDEX(@signatory, ';', 1 );
set @custId = SUBSTRING_INDEX(SUBSTRING_INDEX(@signatory, ';', 2 ),';',-1);
set @query = concat('DELETE FROM customersignatorygroup WHERE signatoryGroupId =',@sigGroupId,'AND customerId=',@custId,'');
prepare sql_query from @query;
execute sql_query;
ITERATE deleteSignatories;
END IF;
END LOOP deleteSignatories;
END IF;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `systemuser_permission_proc`;
DELIMITER $$
CREATE PROCEDURE `systemuser_permission_proc`(
IN _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 SELECT 
        `up`.`Permission_id` AS `id`,
        `p`.`Name` AS `name`,
        `p`.`Status_id` AS `status`,
        `p`.`PermissionValue` as `PermissionValue`,
        `p`.`softdeleteflag` AS `softdeleteflag`
    FROM
        (`userpermission` `up`
        JOIN `permission` `p` ON (`up`.`Permission_id` = `p`.`id`)) where `up`.`User_id`= _userId and `p`.`Status_id`='SID_ACTIVE'
    UNION SELECT 
        `p`.`id` AS `id`,
        `p`.`Name` AS `name`,
        `p`.`Status_id` AS `status`,
        `p`.`PermissionValue` as `PermissionValue`,
        `p`.`softdeleteflag` AS `softdeleteflag`
    FROM
        (`userrole` `ur` JOIN `role` `r` ON (`r`.`id` = `ur`.`Role_id`)
        JOIN `rolepermission` `rp` ON (`ur`.`Role_id` = `rp`.`Role_id`)
        JOIN `permission` `p` ON (`rp`.`Permission_id` = `p`.`id`)) where `ur`.`User_id`= _userId and `p`.`Status_id`='SID_ACTIVE' ;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `updateRequestApprovalMatrix_proc`;

DROP PROCEDURE IF EXISTS `update_requestapprovalmatrix_proc`;

DELIMITER $$
CREATE PROCEDURE `update_requestapprovalmatrix_proc`(
IN _requestApprovalMatrixId VARCHAR(250) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	UPDATE requestapprovalmatrix SET `receivedApprovals` = `receivedApprovals` + 1 WHERE FIND_IN_SET(id , _requestApprovalMatrixId);
END$$

DELIMITER ;


DROP PROCEDURE IF EXISTS `fetch_requestapprovalmatrix_details_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_requestapprovalmatrix_details_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	SELECT bb.requestId AS requestId,
	am.id AS approvalMatrixId,
	ram.id AS requestApprovalMatrixId,
	ram.receivedApprovals AS receivedApprovals,
	IF(ar.numberOfApprovals = -1, na.numberOfApprovals, ar.numberOfApprovals) AS numberOfApprovals,
	cam.customerId AS customerId  
	FROM bbrequest AS bb JOIN 
	requestapprovalmatrix AS ram 
	JOIN approvalmatrix AS am JOIN 
	customerapprovalmatrix AS cam JOIN 
	approvalrule AS ar JOIN
	(SELECT approvalmatrixId, COUNT(DISTINCT(customerId)) AS numberOfApprovals FROM customerapprovalmatrix GROUP BY approvalmatrixId) as na
	WHERE bb.requestId = ram.requestId AND 
	ram.approvalMatrixId = am.id 
	AND cam.approvalMatrixId = am.id 
	AND am.approvalruleId = ar.id 
	AND na.approvalmatrixId = am.id
	AND bb.requestId = _requestId 
	AND cam.customerId = _customerId COLLATE utf8_general_ci;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `systemroles_permission_proc`;

DELIMITER $$
CREATE PROCEDURE `systemroles_permission_proc`(
IN _roleIds varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 SELECT distinct
        `p`.`id` AS `id`,
        `p`.`Name` AS `name`,
        `p`.`Status_id` AS `status`,
        `p`.`PermissionValue` AS `PermissionValue`,
         CAST(`p`.`softdeleteflag` AS unsigned) AS `softdeleteflag`
    FROM
    `rolepermission` `rp` , `permission` `p`
    where `p`.`id` = `rp`.`Permission_id` and `p`.`Status_id`='SID_ACTIVE' and FIND_IN_SET(`rp`.`Role_id`,_roleIds) order by `id`;
END$$
DELIMITER ;

DROP procedure IF EXISTS `rolePermissionDelete_proc`;

DELIMITER $$

CREATE PROCEDURE `rolePermissionDelete_proc`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI, 
IN _PermissionIds VARCHAR(5000) CHARACTER SET UTF8 COLLATE UTF8_GENERAL_CI
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
AND rp.Permission_id COLLATE UTF8_GENERAL_CI =c.Permission_id COLLATE UTF8_GENERAL_CI
AND NOT FIND_IN_SET(rp.Permission_id COLLATE UTF8_GENERAL_CI ,_PermissionIds)
AND c.id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI);

IF @caid_count=0 THEN
DELETE FROM rolecompositeaction WHERE Role_id COLLATE UTF8_GENERAL_CI =_roleId AND CompositeAction_id COLLATE UTF8_GENERAL_CI=caid COLLATE UTF8_GENERAL_CI;
END IF;
END LOOP MANAGECAIDS;
CLOSE caids;

DELETE FROM rolepermission WHERE Role_id COLLATE UTF8_GENERAL_CI=_roleId AND FIND_IN_SET(Permission_id COLLATE UTF8_GENERAL_CI,_PermissionIds);
SET SQL_SAFE_UPDATES = 1;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `update_transaction_status_proc`;

DELIMITER $$
CREATE PROCEDURE `update_transaction_status_proc`(
    IN _featureId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _status VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _confirmationNumber VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
	)
BEGIN
	SET @isSuccess = "true";
	CASE _featureId
		WHEN  'ACH_COLLECTION' THEN 
			UPDATE `achtransaction` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
        WHEN  'ACH_PAYMENT' THEN 
			UPDATE `achtransaction` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'ACH_FILES' THEN   
			UPDATE `achfile` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'BILL_PAY' THEN   
			UPDATE `billpaytransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'DOMESTIC_WIRE_TRANSFER' THEN   
			UPDATE `wiretransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'INTERNATIONAL_WIRE_TRANSFER' THEN   
			UPDATE `wiretransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER' THEN   
			UPDATE `internationalfundtransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'INTER_BANK_ACCOUNT_FUND_TRANSFER' THEN   
			UPDATE `interbankfundtransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'INTRA_BANK_FUND_TRANSFER' THEN   
			UPDATE `intrabanktransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'P2P' THEN   
			UPDATE `p2ptransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		WHEN 'TRANSFER_BETWEEN_OWN_ACCOUNT' THEN   
			UPDATE `ownaccounttransfers` SET `status` = _status WHERE `confirmationNumber` = _confirmationNumber;
		ELSE
			SET @isSuccess = "false";
    END CASE;
    SELECT @isSuccess AS isSuccess;
END$$

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

END$$
DELIMITER ;


DROP procedure IF EXISTS `fetch_achfiles_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_achfiles_proc`(
	IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _achFile_id VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
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
			SET @notCreatedBySelf = concat(" AND NOT FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"')");
		ELSE                
			SET @notCreatedBySelf = "";
		END IF;
		
        SET _filterByParam = IF(_filterByParam = NULL , '', _filterByParam);
        SET _filterByValue = IF(_filterByValue = NULL , '', _filterByValue);
        SET _achFile_id = if(_achFile_id = "" OR _achFile_id = NULL, '%', _achFile_id);
       
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
        
        SET @customerMatrixIds = (SELECT group_concat(approvalMatrixId SEPARATOR ",") FROM customerapprovalmatrix WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerMatrixIds is NULL THEN      
			SET @customerMatrixIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND action = 'Approved');
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
        
        SET @queryTypecondition = if(_queryType = 'myRequests', concat(" AND FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"') AND (`bbrequest`.`status` = 'Pending' OR `bbrequest`.`status` = 'Approved' OR `bbrequest`.`status` = 'Rejected' ) "),
										if(_queryType = 'pendingForMyApprovals', concat(" AND FIND_IN_SET(`achfile`.`requestId`,  \"",@approvalRequestIds,"\")  AND `achfile`.`status` = 'Pending' ", @notCreatedBySelf),
                                        if(_queryType = 'rejected', concat("AND `achfile`.`status` = 'Rejected' "),
                                        if(_achFile_id = '%' , concat(" AND NOT `achfile`.`status` = 'Withdrawn'"),
                                        ''))));
        
        SET @searchQuery = if(_searchString = NULL OR _searchString = "", @searchQuery, 
                concat(@searchQuery, " AND (`achfile`.`achFileName` LIKE '%",_searchString,"%' OR `achfile`.`requestType` LIKE '%",_searchString,"%')"));
        
        SET @paginationQuery = if(_pageOffset = NULL OR _pageOffset = "" OR _pageSize = NULL OR _pageSize = "", '', concat(" LIMIT ",_pageOffset, ", " ,_pageSize)); 
        
        SET @features = (SELECT group_concat(Feature_id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(id,_featureactionlist) > 0);
        IF @features is NULL THEN      
			SET @features = "";
		END IF;
        
        SET @createActions = (SELECT group_concat(id SEPARATOR ",") FROM featureaction WHERE FIND_IN_SET(Feature_id, @features) > 0 AND id LIKE "%_UPLOAD");
        IF @createActions is NULL THEN      
			SET @createActions = "";
		END IF;
		
		SET @companyRequestIds = (SELECT group_concat(requestId SEPARATOR ",") FROM bbrequest WHERE FIND_IN_SET(companyId, @companyId) AND FIND_IN_SET(bbrequest.featureActionId, @createActions));
        
        IF @companyRequestIds is NULL THEN      
            SET @companyRequestIds = "";
        END IF;
        
		SET @select_statement = concat("SELECT * FROM
        
        (SELECT 
			`achfile`.`achFile_id` AS `achFile_id`,
			`achfile`.`achFileName` AS `achFileName`,
			`achfile`.`featureActionId` AS `featureActionId`,
			`achfile`.`debitAmount` AS `debitAmount`,
			`achfile`.`approvalAccounts` AS `approvalAccounts`,
			`achfile`.`debitAccounts` AS `debitAccounts`,
			`achfile`.`createdby` AS `createdby`,
			`achfile`.`createdts` AS `createdts`,
			`achfile`.`requestType` AS `requestType`,
			`achfile`.`numberOfCredits` AS `numberOfCredits`,
			`achfile`.`numberOfDebits` AS `numberOfDebits`,
			`achfile`.`numberOfPrenotes` AS `numberOfPrenotes`,
			`achfile`.`requestId` AS `requestId`,
			`achfile`.`contents` AS `contents`,
			`achfile`.`fileSize` AS `fileSize`,
			`achfile`.`softDelete` AS `softDelete`,
			`achfile`.`creditAmount` AS `creditAmount`,
			`achfile`.`numberOfRecords` AS `numberOfRecords`,
			`achfile`.`achFileFormatType_id` AS `achFileFormatType_id`,
            ( CASE 
				WHEN `bbrequest`.`status` is NULL THEN `achfile`.`status`
                ELSE `bbrequest`.`status` 
			END ) AS `status`,
            `achfile`.`companyId` AS `companyId`,
            `achfile`.`confirmationNumber` AS `confirmationNumber`,
			`customer`.`UserName` AS `userName`,
			`achfileformattype`.`fileType` AS `achFileFormatType`,
			`bbrequest`.`createdby` AS `requestCreatedby`,
			(CASE
				WHEN `achfile`.`createdby` IN (",@combinedIds,") THEN 'true'
				ELSE 'false'
			 END) as `amICreator`,
			(CASE 
				WHEN `bbrequest`.`requestId` IN (",@approvalRequestIds,") THEN 'true'
        		ELSE 'false'
	 		END)
	 		 as `amIApprover`
		FROM
			(((`achfile`
			LEFT JOIN `customer` ON (`achfile`.`createdby` = `customer`.`id`))
			LEFT JOIN `achfileformattype` ON (`achfile`.`achFileFormatType_id` = `achfileformattype`.`id`))
			LEFT JOIN `bbrequest` ON (`achfile`.`requestId` = `bbrequest`.`requestId`))
			WHERE `achfile`.`softDelete` = '0'
				AND (FIND_IN_SET(`achfile`.`companyId`, '", @companyId ,"') OR FIND_IN_SET(`achfile`.`createdby`, '",@combinedIds,"'))
				AND `achfile`.`achFile_id` LIKE '",_achFile_id ,"'
                AND FIND_IN_SET(`achfile`.`featureActionId`, \"", @createActions, "\") ",
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
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrix_default_create_proc`;

DELIMITER $$

CREATE PROCEDURE `approvalmatrix_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL: BEGIN
 DECLARE accountList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 DECLARE accountIndex INTEGER DEFAULT 0;
 DECLARE actionIndex INTEGER DEFAULT 0;
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

IF _accountIds IS NULL OR  _accountIds = '' THEN
	LEAVE MAINLABEL;
END IF;
 
set @numOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;
set @numOfActions = LENGTH(_actionIds) - LENGTH(REPLACE(_actionIds, ',', '')) + 1;
getAccount: LOOP
	set accountIndex = accountIndex + 1;
	IF accountIndex = @numOfAccounts + 1 THEN 
		LEAVE getAccount;
	Else
		set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(_accountIds, ',', accountIndex), ',', -1 );
		set actionIndex = 0; 
		 getAction: LOOP
            set actionIndex = actionIndex + 1;
            IF actionIndex = @numOfActions + 1 THEN
				LEAVE getAction;
			Else
				set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(_actionIds, ',', actionIndex), ',', -1 );
                SELECT Type_id INTO typeId FROM featureaction WHERE id = @actionId;
                IF typeId = "MONETARY" THEN			
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_1, "_", _contractId), @accountId, @actionId, limitTypeId_1,_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_2, "_", _contractId), @accountId,@actionId,limitTypeId_2,_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(_contractId, concat(@actionId, "_", @accountId, "_", limitTypeId_3, "_", _contractId),@accountId, @actionId, limitTypeId_3,_cif,'NO_APPROVAL');
                ELSEIF typeId = "NON_MONETARY" THEN
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                    (_contractId, concat(@actionId, "_", @accountId, "_", "NON_MONETARY_LIMIT", "_", _contractId), @accountId, @actionId, "NON_MONETARY_LIMIT",_cif,'NO_APPROVAL');
				END IF;
			END IF;
		 END LOOP getAction;
        set accountList = CONCAT(@accountId,",",accountList);
	END IF;
END LOOP getAccount;  

SET accountList = (select SUBSTRING(accountList FROM 1 FOR (CHAR_LENGTH(accountList)-1)));
select accountList;

END$$

DELIMITER ;

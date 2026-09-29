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
         `approvalMatrix`.`invalid`
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

DROP procedure IF EXISTS `get_actions_with_approvefeatureaction_proc`;

DELIMITER $$

CREATE PROCEDURE `get_actions_with_approvefeatureaction_proc`(
IN _featureActions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @actionsList = (SELECT group_concat(id SEPARATOR ",") from featureaction WHERE 
                      FIND_IN_SET(id,_featureActions) AND
					(!ISNULL(featureaction.approveFeatureAction) || featureaction.approveFeatureAction != ''));

select @actionsList As actions;

END$$

DELIMITER ;

DROP procedure IF EXISTS `approvalmatrix_default_create_proc`;

DELIMITER $$

CREATE PROCEDURE `approvalmatrix_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE accountList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 DECLARE accountIndex INTEGER DEFAULT 0;
 DECLARE actionIndex INTEGER DEFAULT 0;
 DECLARE typeId TEXT DEFAULT "";
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

ALTER TABLE `alertsubtype` ADD COLUMN `isAutoSubscribeEnabled` BOOLEAN NULL DEFAULT 0;
ALTER TABLE `alertsubtype` ADD COLUMN `externalSystem` int(1) NULL DEFAULT '0';
ALTER TABLE `eventsubtype` ADD COLUMN `externalSystem` int(1) NULL DEFAULT '0';

DROP TABLE IF EXISTS `externalalertsytem`;

CREATE TABLE `externalalertsytem` (
  `systemtype` varchar(50) NOT NULL,
  `systemid` int(1) NOT NULL DEFAULT '0',
  
  PRIMARY KEY (`systemid`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
    `alertsubtypetext`.`languageCode` as `alertsubtypetext_languageCode`,
    `alertsubtypetext`.`description` as `alertsubtypetext_description`,
    `alertsubtype`.`externalSystem` AS `alertsubtype_externalSystem`,
    `alertsubtypetext`.`displayName` as `alertsubtypetext_displayName`
from
    (`alertsubtype`
join `alertsubtypetext` on
    ((`alertsubtypetext`.`alertSubTypeId` = `alertsubtype`.`id`)));
    
ALTER TABLE `application` ADD COLUMN `stateManagementAvailable` TINYINT(1) NOT NULL DEFAULT '0' AFTER `isSelfApprovalEnabled`;

DROP TABLE IF EXISTS `userroleservicedefinition`;
CREATE TABLE `userroleservicedefinition` (
  `UserRole_id` varchar(50) NOT NULL,
  `servicedefinitionId` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`UserRole_id`,`servicedefinitionId`),
  KEY `userroleservicedefinition_servicedefinition_id_idx` (`servicedefinitionId`),
  KEY `userroleservicedefinition_userrole_id_idx` (`UserRole_id`),
  CONSTRAINT `userroleservicedefinition_servicedefinition_id` FOREIGN KEY (`servicedefinitionId`) REFERENCES `servicedefinition` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `userroleservicedefinition_userrole_id` FOREIGN KEY (`UserRole_id`) REFERENCES `role` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

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
        `role`.`Description` AS `InternalRole_Description`
    FROM
        ((`userroleservicedefinition`
        LEFT JOIN `servicedefinition` ON ((`servicedefinition`.`id` = `userroleservicedefinition`.`servicedefinitionId`)))
        LEFT JOIN `role` ON ((`role`.`id` = `userroleservicedefinition`.`UserRole_id`)));

DROP procedure IF EXISTS `internal_user_access_to_customer_on_servicedefinition_proc`;

DELIMITER $$

CREATE PROCEDURE `internal_user_access_to_customer_on_servicedefinition_proc`(
IN _roleIds VARCHAR(500) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerUsername  VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

DECLARE isCustomerAccessible VARCHAR(6);
SET SESSION group_concat_max_len = 100000000;

IF(_customerId = '' AND _customerUsername != '') THEN
	SET _customerId = (SELECT id FROM customer WHERE UserName = _customerUsername);
END IF;

SET @servicedefinitionIds = (SELECT group_concat(distinct servicedefinitionId SEPARATOR ",")   
							FROM userroleservicedefinition 
                            WHERE FIND_IN_SET(UserRole_id, _roleIds));

SET @contractIdList = (SELECT group_concat(id SEPARATOR ",") FROM contract WHERE FIND_IN_SET(servicedefinitionId, @servicedefinitionIds));

SET isCustomerAccessible = (_customerId IN (SELECT custId FROM 
(SELECT DISTINCT customerId  AS custId FROM contractcustomers 
WHERE customerId IS NOT NULL AND FIND_IN_SET(contractId, @contractIdList)
UNION ALL
SELECT DISTINCT coreCustomerId  AS custId FROM contractcustomers 
WHERE coreCustomerId IS NOT NULL AND FIND_IN_SET(contractId, @contractIdList) ) T));

IF ISNULL(isCustomerAccessible) OR isCustomerAccessible = 0 THEN
	SET isCustomerAccessible = 'false';
ELSEIF(isCustomerAccessible = 1) THEN
	SET isCustomerAccessible = 'true';
END IF;

SELECT isCustomerAccessible;
END$$

DELIMITER ;

DROP VIEW IF EXISTS `groups_view`;
CREATE VIEW `groups_view` AS
    SELECT 
        `membergroup`.`id` AS `Group_id`,
        `membergroup`.`Type_id` AS `Type_id`,
        `membergrouptype`.`description` AS `Type_Name`,
        `membergroup`.`Description` AS `Group_Desc`,
        `membergroup`.`Status_id` AS `Status_id`,
        `membergroup`.`Name` AS `Group_Name`,
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


DROP procedure IF EXISTS `fetch_wiretransfer_details_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_wiretransfer_details_proc`(
in _transactionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _wireFileExecution_id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET @companyId = (select group_concat(concat(contractId,"_",coreCustomerId) SEPARATOR ",") from contractcustomers where customerId =_customerId);
	
	IF _transactionId is NULL THEN
		SET _transactionId = "";
	END IF;
    
    IF _wireFileExecution_id is NULL THEN
		SET _wireFileExecution_id = "";
	END IF;
	
	IF @companyId is NULL THEN   
		SET @companyId = "";
	END IF;
     
	SET @filter = if(_transactionId != "", concat("transactionId = ", _transactionId , " ",@companyId,"')" ), 
					if( _wireFileExecution_id != "", concat("wireFileExecution_id = ", _wireFileExecution_id , " AND FIND_IN_SET(`companyId`, '",@companyId,"')"), '')) ;
                    
	set @select_statement = concat("SELECT 
		wiretransfers.transactionId,
		wiretransfers.featureActionId,
        wiretransfers.confirmationNumber,
        wiretransfers.companyId,
        wiretransfers.createdby,
        wiretransfers.requestId,
        wiretransfers.status,
        wiretransfers.onetime_id,
        wiretransfers.wireFileExecution_id,
        wiretransfers.notes,
        wiretransfers.amount,
        wiretransfers.fromAccountNumber,
        wiretransfers.payeeAccountNumber,
        wiretransfers.transactionType,
        wiretransfers.payeeId,
        wiretransfers.payeeCurrency,
        onetimepayee.payeeName,
		onetimepayee.payeeNickName,
		onetimepayee.payeeType,
		onetimepayee.wireAccountType,
		onetimepayee.swiftCode,
		onetimepayee.routingNumber,
		onetimepayee.zipCode,
		onetimepayee.cityName,
		onetimepayee.state,
		onetimepayee.country,
		onetimepayee.payeeAddressLine1,
		onetimepayee.payeeAddressLine2,
		onetimepayee.bankName,
		onetimepayee.internationalRoutingCode,
		onetimepayee.bankAddressLine1,
		onetimepayee.bankAddressLine2,
		onetimepayee.bankCity,
		onetimepayee.bankState,
		onetimepayee.bankZip
        
    FROM
        (`wiretransfers`
		LEFT JOIN `onetimepayee` ON (`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`))
		WHERE ", @filter);
        
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
     
END$$

DELIMITER ;

DROP table IF EXISTS `alertmessagetypeconfig`;
create table `alertmessagetypeconfig` ( `messagetype` varchar(10) NOT NULL, `alerttype` varchar(50) NOT NULL, `alertsubtype` varchar(75) NOT NULL, PRIMARY KEY (`messagetype`)) ;

DROP VIEW IF EXISTS `autosubscribedalertsview`;
CREATE OR REPLACE VIEW `autosubscribedalertsview` AS select `a`.`id` AS `categoryid`,`b`.`id` AS `groupid`,`c`.`id` AS `alertsubtypeid`,`c`.`Name` AS `name`,`c`.`isAccountLevel` AS `isAccountLevel`,`c`.`attributeId` AS `attributeId`,`c`.`alertConditionId` AS `alertConditionId`,`c`.`value1` AS `value1`,`c`.`value2` AS `value2`,`c`.`Status_id` AS `Status_id`,`c`.`Description` AS `Description`,`c`.`isGlobal` AS `isGlobal`,`c`.`defaultFrequencyId` AS `defaultFrequencyId`,`c`.`defaultFrequencyValue` AS `defaultFrequencyValue`,`c`.`defaultFrequencyTime` AS `defaultFrequencyTime`,`c`.`recipienttype` AS `recipienttype`,`c`.`createdby` AS `createdby`,`c`.`modifiedby` AS `modifiedby`,`c`.`createdts` AS `createdts`,`c`.`lastmodifiedts` AS `lastmodifiedts`,`c`.`synctimestamp` AS `synctimestamp`,`c`.`softdeleteflag` AS `softdeleteflag`,`c`.`isAutoSubscribeEnabled` AS `isAutoSubscribeEnabled`,`c`.`externalSystem` AS `externalSystem` from ((`dbxalertcategory` `a` join `dbxalerttype` `b`) join `alertsubtype` `c`) where ((`a`.`id` = `b`.`AlertCategoryId`) and (`b`.`id` = `c`.`AlertTypeId`)) order by `a`.`id`,`b`.`id`;

CREATE OR REPLACE VIEW `alertsubtypeaccounttype_view` AS select `accounttype`.`displayName` AS `accountTypeId`,`alertsubtypeaccounttype`.`alertSubTypeId` AS `alertSubTypeId` from (`alertsubtypeaccounttype` join `accounttype`) where (`alertsubtypeaccounttype`.`accountTypeId` = `accounttype`.`TypeID`);

alter table `dbxcustomeralertentitlement` add `alertRequestId` varchar(255) ;

DROP procedure IF EXISTS `infinity_customer_from_servicedefinition_proc`;

DELIMITER $$

CREATE PROCEDURE `infinity_customer_from_servicedefinition_proc`(
IN _servicedefinitionIds varchar(500) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @contractIdList = (SELECT group_concat(id SEPARATOR ",") FROM contract WHERE 
						 FIND_IN_SET(servicedefinitionId, _servicedefinitionIds) );
SELECT DISTINCT customerId  FROM contractcustomers 
WHERE customerId IS NOT NULL AND FIND_IN_SET(contractId, @contractIdList) ;
END$$

DELIMITER ;

ALTER TABLE `intrabanktransfers` 
	ADD COLUMN `iban` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bicCode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `bankId` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeCurrency` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `paymentType` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `feeAmount` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressNickName` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryAddressLine1` VARCHAR(200) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryCity` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiaryZipcode` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`,
	ADD COLUMN `beneficiarycountry` VARCHAR(50) NULL DEFAULT NULL AFTER `isScheduled`;
	
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
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
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
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER ;

DROP procedure IF EXISTS `update_bbactedrequest_proc`;

DELIMITER $$
CREATE  PROCEDURE `update_bbactedrequest_proc`(
IN _requestId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
update bbactedrequest set softdeleteflag=1 where requestId=_requestId ;
END$$

DELIMITER ;

DROP procedure IF EXISTS `account_action_approvers_proc`;

DELIMITER $$

CREATE PROCEDURE `account_action_approvers_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountIds text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0;      

END$$

DELIMITER ;

ALTER TABLE `externalaccount` MODIFY `Id` VARCHAR(50);
ALTER TABLE `payperson` MODIFY `id` VARCHAR(50);

ALTER TABLE `bulkwiretemplatelineitems` DROP FOREIGN KEY `FK2_bulkwiretemplatelineitems_payeeID`;
ALTER TABLE `payee` MODIFY `Id` VARCHAR(50);
ALTER TABLE `bulkwiretemplatelineitems` MODIFY `payeeId` VARCHAR(50);
ALTER TABLE `bulkwiretemplatelineitems` ADD CONSTRAINT `FK2_bulkwiretemplatelineitems_payeeID` FOREIGN KEY (`payeeId`) REFERENCES `payee` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION;


DROP VIEW IF EXISTS `alerttype_view`;
CREATE VIEW `alerttype_view` AS
    SELECT 
        `dbxalerttype`.`id` AS `alerttype_id`,
        `dbxalerttype`.`Name` AS `alerttype_Name`,
        `dbxalerttype`.`AlertCategoryId` AS `alerttype_AlertCategoryId`,
        `dbxalerttype`.`Status_id` AS `alerttype_Status_id`,
        `dbxalerttype`.`IsGlobal` AS `alerttype_IsGlobal`,
        `dbxalerttype`.`DisplaySequence` AS `alerttype_DisplaySequence`,
		`dbxalerttype`.`defaultFrequencyId` as `alerttype_freqId`,
        `dbxalerttype`.`defaultFrequencyValue` as `alerttype_freqValue`,
        `dbxalerttype`.`defaultFrequencyTime` as `alerttype_freqTime`,
        `dbxalerttype`.`isAccountLevel` as `alerttype_isAccountLevel`,
        (SELECT 
                COUNT(*)
            FROM
                `alertsubtype`
            WHERE
                (`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`) AND(`alertsubtype`.`IsGlobal` = 0)) AS `userAlerts_count`,
		(SELECT 
                COUNT(`alertsubtype`.`id`)
            FROM
                `alertsubtype`
            WHERE
                (`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)) AS `Alerts_count`,
        `dbxalerttype`.`softdeleteflag` AS `alerttype_softdeleteflag`,
        `dbxalerttypetext`.`LanguageCode` AS `alerttypetext_LanguageCode`,
        `dbxalerttypetext`.`DisplayName` AS `alerttypetext_DisplayName`,
        `dbxalerttypetext`.`Description` AS `alerttypetext_Description`,
        `dbxalerttypetext`.`createdby` AS `alerttypetext_createdby`,
        `dbxalerttypetext`.`modifiedby` AS `alerttypetext_modifiedby`,
        `dbxalerttypetext`.`createdts` AS `alerttypetext_createdts`,
        `dbxalerttypetext`.`lastmodifiedts` AS `alerttypetext_lastmodifiedts`,
        `dbxalerttypetext`.`synctimestamp` AS `alerttypetext_synctimestamp`,
        `dbxalerttypetext`.`softdeleteflag` AS `alerttypetext_softdeleteflag`
    FROM
        (`dbxalerttypetext`
        JOIN `dbxalerttype` ON ((`dbxalerttypetext`.`AlertTypeId` = `dbxalerttype`.`id`)));
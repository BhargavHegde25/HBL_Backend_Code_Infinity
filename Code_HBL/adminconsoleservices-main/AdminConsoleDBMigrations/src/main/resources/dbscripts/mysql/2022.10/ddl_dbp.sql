SET FOREIGN_KEY_CHECKS = 0;
CREATE TABLE `accountlevelactionlimit` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `policyId` varchar(50) DEFAULT NULL,
  `isPortfolio` varchar(45) DEFAULT 'false',
  `accountId` varchar(45) DEFAULT NULL,
  `featureId` varchar(50) NOT NULL,
  `actionId` varchar(255) NOT NULL,
  `limitGroupId` varchar(45) DEFAULT NULL,
  `limitTypeId` varchar(50) DEFAULT NULL,
  `value` decimal(20,2) DEFAULT NULL,
  `isNewAction` tinyint(1) NOT NULL DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_accountlevelactionlimit_contract_contractId_idx` (`contractId`),
  KEY `FK_accountlevelactionlimit_featureaction_actionId_idx` (`actionId`),
  KEY `FK_accountlevelactionlimit_feature_featureId_idx` (`featureId`),
  KEY `IDX_accountlevelactionlimit_corecustomerId` (`coreCustomerId`),
  KEY `IDX_actionIdAndCoreCustomerId` (`actionId`,`coreCustomerId`),
  KEY `IDX_accountlevelactionlimit_contractId_corecustomerId` (`contractId`,`coreCustomerId`),
  KEY `IDX_contractacttionlimit_contractId_corecustomerId_featuresId` (`contractId`,`coreCustomerId`,`featureId`),
  KEY `IDX_accountlevelactionlimit_contractId_corecustomerId_actionId` (`contractId`,`coreCustomerId`,`featureId`,`actionId`),
  CONSTRAINT `FK_accountlevelactionlimit_contract_contractId` FOREIGN KEY (`contractId`) REFERENCES `contract` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_accountlevelactionlimit_feature_featureId` FOREIGN KEY (`featureId`) REFERENCES `feature` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_accountlevelactionlimit_featureaction_actionId` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
    
	
	DROP procedure IF EXISTS `contract_action_limit_save_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_action_limit_save_proc`(
IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  )
BEGIN
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN
            LEAVE insertRecords;
          else
            set @recordsData = concat('\'',UUID(),'\'',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4 ), '\"', -1 );
              select @accountId;
              IF(@accountId='')THEN
               set @query = concat('INSERT INTO contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (',@recordsData,');');
               ELSE
               set @query = concat('INSERT INTO accountlevelactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (',@recordsData,');');
            END IF;
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;

END$$
DELIMITER ;

DROP procedure IF EXISTS `contract_actionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_actionlimits_create_proc`(
IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

      DECLARE tempLimitValue varchar(255) DEFAULT "";

      SET SESSION group_concat_max_len = 100000000;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      set @serviceDefinitionId = "";
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\"',UUID(),'\"',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));

            set @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',2 ), '\"', -1 );
            set @customerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 );
             set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4 ), ',\"', -1 );
             set @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );
            set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            set @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );
            set @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',8), ',\"', -1 );
            set @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            select @featureId;
             SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',7),'\"');

              IF isnull(@serviceDefinitionId) OR @serviceDefinitionId = "" THEN
                SET @serviceDefinitionId = (SELECT servicedefinitionId FROM contract WHERE id = @contractId);
                     END IF;

                     IF @limitId != '@' AND  @limitValue != '@' THEN 
              SET @limitAtFI = (SELECT actionlimit.value FROM actionlimit WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId);

              SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId
                                                   AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId );
                       SET tempLimitValue = LEAST(@limitAtFI,@limitATServiceDefinition,@limitValue);
            END IF;

            IF !isnull(tempLimitValue) AND tempLimitValue !='' THEN
               SET @limitValue = tempLimitValue;
                     END IF;

            SET @contarctFeatures = (select group_concat(contractfeatures.id SEPARATOR ",") from contractfeatures WHERE contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId = @featureId);



             SET @serviceDefinitionActions = (select group_concat(servicedefinitionactionlimit.id SEPARATOR ",") from servicedefinitionactionlimit WHERE actionId=@actionId AND 
                                                               serviceDefinitionId =@serviceDefinitionId);

                      SET @existingActionLimitRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit WHERE contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId AND 
                                      limitTypeId = @limitId );

             SET @existingActionRecords = (select group_concat(contractactionlimit.id SEPARATOR ",") from contractactionlimit WHERE contractId=@contractId AND coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId);
              
                           SET @existingActionRecords1 = (select group_concat(accountlevelactionlimit.id SEPARATOR ",") from accountlevelactionlimit WHERE contractId=@contractId AND 
                                                               coreCustomerId =@customerId AND accountId =@accountId AND featureId= @featureId AND actionId= @actionId);

                     IF !isnull(@contarctFeatures) AND @contarctFeatures != "" AND !isnull(@serviceDefinitionActions) AND @serviceDefinitionActions != ""  THEN
                IF !isnull(@existingActionLimitRecords) AND @existingActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE contractactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingActionLimitRecords,'\'',';'); 
                           ELSE 
                                  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords) OR @existingActionRecords = "") THEN
                                       SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction) VALUES (',@recordsDataWithoutLimits,');');
                               ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
                                         SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, limitTypeId,value) VALUES (',@recordsData,');');
                                  END IF;
                           END IF;
                            IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords1) OR @existingActionRecords1 = "")  THEN
                                       SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction) VALUES (',@recordsDataWithoutLimits,');');
                               ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
                                         SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, limitTypeId,value) VALUES (',@recordsData,');');

                                  END IF;

                           END IF;
                     END IF;
            END IF;
            IF !isnull(@query) and @query != "" THEN
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            END IF;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;



DROP procedure IF EXISTS `contract_corecustomer_actions_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_corecustomer_actions_delete_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _accountId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _actionsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM `customeraction`
    WHERE  contractId = _contractId AND FIND_IN_SET(Action_id,_actionsCSV) AND coreCustomerId = _coreCustomerId ;

IF(_accountId='')
THEN
DELETE FROM `contractactionlimit`
    WHERE  contractId = _contractId AND FIND_IN_SET(actionId,_actionsCSV) AND coreCustomerId = _coreCustomerId ;
       ELSE
DELETE FROM `accountlevelactionlimit`
    WHERE  contractId = _contractId AND FIND_IN_SET(actionId,_actionsCSV) AND coreCustomerId = _coreCustomerId
    AND accountId =_accountId ;

END IF;
END$$
DELIMITER ;

DROP procedure IF EXISTS `infinityuser_contractdetails_get_proc`;

DELIMITER $$
CREATE PROCEDURE `infinityuser_contractdetails_get_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
	select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.coreCustomerName AS coreCustomerName,
        customergroup.Group_id AS userRole,
        membergroup.name AS userRoleName,
        contract.companyLegalUnit as legalEntityId,
		IF( ( (customergroup.Group_id = null or customergroup.Group_id =  '') AND (membergroup.name = '' or membergroup.name = null)),'false','true') AS isAssociated
	FROM
		contract
        LEFT JOIN contractcustomers ON (contractcustomers.contractId = contract.id and contractcustomers.customerId = _id)
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
        LEFT JOIN customergroup ON (customergroup.contractId = contract.id AND 
			customergroup.coreCustomerId = contractcorecustomers.coreCustomerId AND 
			customergroup.Customer_id = _id)
        LEFT JOIN membergroup ON (membergroup.id = customergroup.Group_id)
	WHERE
		customergroup.Customer_id = _id
        AND contractcustomers.customerId = _id
        and contract.companyLegalUnit = _legalEntityId;
	/*UNION
    select
		contract.id AS contractId,
        contract.name AS contractName,
        contract.servicedefinitionId AS servicedefinitionId,
        servicedefinition.name AS serviceDefinitionName,
        membergrouptype.description AS serviceDefinitionType,
        contractcorecustomers.coreCustomerId AS coreCustomerId,
		contractcorecustomers.isPrimary AS isPrimary,
        NULL AS userRole,
        NULL AS userRoleName,
        NULL AS userRoleDescription,
        "false" AS isAssociated
	FROM
		contract
        LEFT JOIN servicedefinition ON (servicedefinition.id = contract.servicedefinitionId)
        LEFT JOIN membergrouptype ON (membergrouptype.id = servicedefinition.serviceType)
        LEFT JOIN contractcorecustomers ON (contractcorecustomers.contractId = contract.id)
	WHERE
		contractcorecustomers.contractId IN ( select contractcustomers.contractId FROM contractcustomers WHERE contractcustomers.customerId = _id);
*/
end $$
DELIMITER;

DROP PROCEDURE IF EXISTS `contract_search_proc`;

DELIMITER $$

CREATE PROCEDURE `contract_search_proc`(
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _coreCustomerName varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _phoneCountryCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _phoneNumber varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN


SET @select_statement = CONCAT("SELECT contract.id as contractId,contract.name as contractName,
contract.servicedefinitionId as serviceDefinitionId ,
(select servicedefinition.name from servicedefinition where servicedefinition.id = 
contract.servicedefinitionId limit 1) as serviceDefinitionName,
(select contractcommunication.value from contractcommunication where 
contractcommunication.contractId = contract.id and 
contractcommunication.typeId = 'COMM_TYPE_EMAIL' limit 1) as email ,
(select contractcorecustomers.coreCustomerName from contractcorecustomers 
 where contractcorecustomers.contractId = contract.id limit 1) as coreCustomerName,
contract.companyLegalUnit as legalEntityId 
from contract where contract.companyLegalUnit = ", QUOTE(_legalEntityId)) ;

	IF(_contractId != '') then
		SET @select_statement = CONCAT(@select_statement," and contract.id = ", quote(_contractId));
	END IF;

    IF(_contractName != '') then
		SET @select_statement = CONCAT(@select_statement , " and contract.name like '%",_contractName,"%'");    
    END IF;
   
	IF(_serviceDefinitionId != '') then
		SET @select_statement = CONCAT(@select_statement , " and contract.servicedefinitionId = ",quote(_serviceDefinitionId));
	END IF;

	IF(_coreCustomerId != '') then
		SET @select_statement = CONCAT(@select_statement ," and contract.id IN (select DISTINCT contractcorecustomers.contractId from contractcorecustomers where  contractcorecustomers.coreCustomerId = ", quote(_coreCustomerId),")");
	END IF;

	IF(_coreCustomerName != '') then
		SET @select_statement = CONCAT(@select_statement , " and contract.id IN (select DISTINCT contractcorecustomers.contractId from contractcorecustomers where contractcorecustomers.coreCustomerName like '%",_coreCustomerName,"%')");
	END IF;

	IF(_email != '') then
		SET @select_statement = CONCAT(@select_statement , " and contract.id IN (select DISTINCT contractcommunication.contractId from contractcommunication where contractcommunication.typeId = 'COMM_TYPE_EMAIL' and contractcommunication.value = ",quote(_email),")");
	END IF;

 	IF(_phoneCountryCode != '' and _phoneNumber!= '') THEN
        SET @select_statement = CONCAT(@select_statement ,  " and contract.id IN (select DISTINCT 
        contractcommunication.contractId from contractcommunication where 
        contractcommunication.typeId = 'COMM_TYPE_PHONE' and contractcommunication.value = ",quote(_phoneNumber),
        "and  contractcommunication.phoneCountryCode = ",quote(_phoneCountryCode),")");     
    END IF;
   
    IF(_country != '') THEN
        SET @select_statement = CONCAT(@select_statement , " and contract.id IN (select DISTINCT contractaddress.contractId FROM contractaddress WHERE contractaddress.addressId IN (select DISTINCT address.id FROM address WHERE address.country = ",quote(_country),")",")");    
    END IF;

	IF (@select_statement != '')
	THEN
		SET @select_statement = CONCAT(@select_statement , ";") ;
	END IF;

PREPARE stmt FROM @select_statement; 
EXECUTE stmt; 
DEALLOCATE PREPARE stmt;

END$$

DELIMITER;

ALTER TABLE `accountlevelactionlimit` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

DROP procedure IF EXISTS `contract_action_limit_save_proc`;

DELIMITER $$

CREATE PROCEDURE `contract_action_limit_save_proc`(
IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  )
BEGIN
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN
            LEAVE insertRecords;
          else
            set @recordsData = concat('\'',UUID(),'\'',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4 ), '\"', -1 );
              select @accountId;
              IF(@accountId='')THEN
               set @query = concat('INSERT INTO contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value,companyLegalUnit) VALUES (',@recordsData,');');
               ELSE
               set @query = concat('INSERT INTO accountlevelactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value,companyLegalUnit) VALUES (',@recordsData,');');
            END IF;
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;

END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS  `contract_features_create_proc`;
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
        FOR (select id from feature where FIND_IN_SET(id COLLATE utf8_general_ci ,@features_list COLLATE utf8_general_ci ));
DECLARE actions CURSOR
        FOR (select id from featureaction where FIND_IN_SET(featureaction.id COLLATE utf8_general_ci,@validServicedefinitionActions COLLATE utf8_general_ci));
DECLARE limits CURSOR
        FOR (select LimitType_id from actionlimit where actionlimit.Action_id COLLATE utf8_general_ci = featureActionId );
DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _serviceTypeId)
                      where (FIND_IN_SET(feature.id COLLATE utf8_general_ci ,_features COLLATE utf8_general_ci))) ;
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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id COLLATE utf8_general_ci = featureActionId );
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
               AND LimitType_id COLLATE utf8_general_ci = limitId);
               
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

DROP procedure IF EXISTS `default_contractactions_create_proc`;

DELIMITER $$

CREATE PROCEDURE `default_contractactions_create_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE featureId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT "" ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE accountId varchar(255) DEFAULT "" ;
 DECLARE actualLimitId varchar(255) DEFAULT "" ;
 DECLARE _accountsCSV varchar(255) DEFAULT ""  ;
 DECLARE coreCustomerId varchar(255) DEFAULT "" ;

DECLARE coreCustomers CURSOR
         FOR (SELECT contractcorecustomers.coreCustomerId FROM contractcorecustomers WHERE contractcorecustomers.contractId = _contractId and contractcorecustomers.companyLegalUnit = _legalEntityId);
DECLARE accounts CURSOR
         FOR (SELECT contractaccounts.accountId  FROM contractaccounts WHERE contractaccounts.contractId  = _contractId and contractaccounts.companyLegalUnit = _legalEntityId );

DECLARE transactionLimits CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction  where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id  = 'MONETARY' and featureaction.isAccountLevel = '1'));
DECLARE accountLevelPermissions CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id = 'NON_MONETARY' and featureaction.isAccountLevel = '1'));
DECLARE globalLevelPermissions CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id  = 'NON_MONETARY' and featureaction.isAccountLevel  = '0'));
DECLARE limits CURSOR 
			FOR (select LimitType_id COLLATE utf8_general_ci from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId  );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

SET _accountsCSV = (SELECT group_concat(distinct contractaccounts.accountId SEPARATOR ",") FROM contractaccounts WHERE contractaccounts.contractId  = _contractId and contractaccounts.companyLegalUnit  = _legalEntityId );

 
SET @serviceDefinitionId = (SELECT servicedefinitionId  from contract WHERE id = _contractId and companyLegalUnit = _legalEntityId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId );
                        
SET @validFIActions = (SELECT group_concat(distinct id  SEPARATOR ",") FROM featureaction
								where featureaction.Feature_id  in (select
									contractfeatures.featureId  from contractfeatures
                                    where contractfeatures.contractId  = _contractId and contractfeatures.companyLegalUnit = _legalEntityId));

SET @validActionsList = (SELECT group_concat(distinct actionId  SEPARATOR ",") FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId = @serviceDefinitionId  AND FIND_IN_SET(actionId COLLATE utf8_general_ci,@validFIActions COLLATE utf8_general_ci));
                 
select @validFIActions;
select @validActionsList;

OPEN coreCustomers;
getCoreCustomer : LOOP
FETCH coreCustomers INTO coreCustomerId;
IF finished = 1 THEN
	LEAVE getCoreCustomer;
ELSE
	OPEN transactionLimits; 
	transactionLimitActions: LOOP
		FETCH transactionLimits INTO featureActionId;
        select featureActionId;
		SET featureId = (select Feature_id  from featureaction where featureaction.id COLLATE utf8_general_ci= featureActionId );
        
		IF finished = 1 THEN 
			LEAVE transactionLimitActions;
		ELSE
			OPEN limits; 
			getlimit: LOOP
			FETCH limits INTO limitId;
				IF finished = 1 THEN 
				   LEAVE getlimit;
				ELSE
				   select limitId;
				   SET @limitvalue = (SELECT value  FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.actionId COLLATE utf8_general_ci = featureActionId
				   AND servicedefinitionactionlimit.limitTypeId COLLATE utf8_general_ci= limitId  AND servicedefinitionactionlimit.serviceDefinitionId COLLATE utf8_general_ci= @serviceDefinitionId );
				   
				   SET @id = (SELECT LEFT(UUID(), 50));
				   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId,limitTypeId,value,companyLegalUnit) VALUES
							(@id,_contractId,coreCustomerId,featureId,featureActionId,limitId,@limitvalue,_legalEntityId);
				   ITERATE  getlimit;
				END IF;
			SET finished = 0;
			END LOOP getlimit;
			CLOSE limits;
		ITERATE  transactionLimitActions;
		END IF;
END LOOP transactionLimitActions;
CLOSE transactionLimits;

SET finished = 0;
OPEN accounts; 
	getAccount : LOOP
	FETCH accounts INTO accountId;
	IF finished = 1 THEN 
		LEAVE getAccount;
	ELSE
	  OPEN accountLevelPermissions; 
	  accountLevelPermissionsActions: LOOP
			FETCH accountLevelPermissions INTO featureActionId;
            SET featureId = (select Feature_id  from featureaction where featureaction.id COLLATE utf8_general_ci= featureActionId );
        
			IF finished = 1 THEN 
				LEAVE accountLevelPermissionsActions;
			else
				SET @id = (SELECT LEFT(UUID(), 50));
					   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId,companyLegalUnit) VALUES
					(@id,_contractId,coreCustomerId,accountId,featureId,featureActionId,_legalEntityId);
			END IF;
			ITERATE  accountLevelPermissionsActions;
	  END LOOP accountLevelPermissionsActions;
	  CLOSE accountLevelPermissions;
	  SET finished = 0;
	 ITERATE  getAccount;
	END IF;
END LOOP getAccount;
CLOSE accounts;

	 
SET finished = 0;
OPEN globalLevelPermissions; 
  globalLevelPermissionsActions : LOOP
		FETCH globalLevelPermissions INTO featureActionId;
		SET featureId = (SELECT Feature_id FROM featureaction WHERE id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
		SELECT featureId;
		IF finished = 1 THEN 
			LEAVE globalLevelPermissionsActions;
		else
				   SET @id = (SELECT LEFT(UUID(), 50));
				   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId,companyLegalUnit) VALUES
				(@id,_contractId,coreCustomerId,featureId,featureActionId,_legalEntityId);
		END IF;
        SET finished = 0;
		ITERATE  globalLevelPermissionsActions;
	END LOOP globalLevelPermissionsActions;
	CLOSE globalLevelPermissions;
ITERATE getCoreCustomer;
END IF;
END LOOP getCoreCustomer;
CLOSE coreCustomers;

END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `customer_search_proc`;
DELIMITER $$

CREATE PROCEDURE `customer_search_proc`( 
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
				customer.companyLegalUnit ");
			
            SET @search_count_statement = "SELECT count(distinct customer.id) as SearchMatchs";
			SET @queryStatement = concat("
			FROM customer
			JOIN (
				SELECT
					customer.id
				FROM customer ",
					IF( _phone != '', " JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE') ",""),
					IF(_email != '', "  JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL') ",""),
                    IF( _TIN != '', " LEFT JOIN organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)", ""),
                    IF( _cardorAccountnumber != '', " LEFT JOIN card ON (customer.id = card.User_id)
                    LEFT JOIN accounts ON (customer.id = accounts.User_id)
                    LEFT JOIN customeraccounts ON (customer.id = customeraccounts.Customer_id)", ""));

                SET @queryStatement = concat(@queryStatement," WHERE customer.companyLegalUnit = ", quote(_legalEntityId));
                
                IF _id != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.id = ", quote(_id));
                end if;
                
                 IF _name != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.LastName like concat(", quote(_name),",'%')");
                end if;

                IF _SSN != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.Ssn = ",quote(_SSN));
                end if;

                IF _username != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.username = ",quote(_username));
                end if;
                
                 IF _dateOfBirth != '' THEN
					set @queryStatement = concat(@queryStatement," and customer.DateOfBirth = ",quote(_dateOfBirth));
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
			LEFT JOIN customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id='COMM_TYPE_PHONE')
			LEFT JOIN customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id='COMM_TYPE_EMAIL')
			LEFT JOIN customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id='ADR_TYPE_HOME')
			LEFT JOIN address ON (customeraddress.Address_id = address.id)
			LEFT JOIN city ON (city.id = address.City_id)
			LEFT JOIN country ON (city.Country_id = country.id)
			LEFT JOIN customergroup ON (customergroup.Customer_id=paginatedCustomers.id)
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
     IF _searchType = 'CUSTOMER_SEARCH' OR _searchType = 'GROUP_SEARCH'THEN
          PREPARE stmt FROM @queryStatement2; EXECUTE stmt;
     END If;
    DEALLOCATE PREPARE stmt;

END$$
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
    WHERE `customer`.`id` = _customerId and `customer`.`companyLegalUnit` = _legalEntityId
    LIMIT 1;

END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `user_customers_withaccounts_proc`;
DELIMITER $$
CREATE  PROCEDURE `user_customers_withaccounts_proc`(
    in _customerId varchar(50) character set UTF8 collate utf8_general_ci,
    in _coreCustomerId varchar(50) character set UTF8 collate utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci
)
begin
    set @select_statement = "(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
        `contractcustomers`.`companyLegalUnit` AS `companyLegalUnit`,
        `contractcustomers`.`coreCustomerId` AS `coreCustomerId`,
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
        `membergroup`.`Name` AS `userRole`,
		`contractaccounts`.`accountId`,
		`contractaccounts`.`accountName`,
		`contractaccounts`.`statusDesc`,
		`contractaccounts`.`typeId`
    FROM
        ((`contractcustomers`
        LEFT JOIN `contract` ON (`contract`.`id` = `contractcustomers`.`contractId`)
		LEFT JOIN `contractcorecustomers` ON ((`contractcorecustomers`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractcorecustomers`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `contractaccounts` ON ((`contractaccounts`.`contractId` = `contractcustomers`.`contractId`)
		AND (`contractaccounts`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))
		LEFT JOIN `servicedefinition` ON (`servicedefinition`.`id` = `contract`.`servicedefinitionId`)
		LEFT JOIN `membergrouptype` ON (`membergrouptype`.`id` = `servicedefinition`.`serviceType`)
        LEFT JOIN `customergroup` ON (((`customergroup`.`Customer_id` = `contractcustomers`.`customerId`)
        AND (`customergroup`.`contractId` = `contractcustomers`.`contractId`)
		AND (`customergroup`.`coreCustomerId` = `contractcustomers`.`coreCustomerId`))))
        LEFT JOIN `membergroup` ON ((`membergroup`.`id` = `customergroup`.`Group_id`)))";

set @isWhereAppened = false;

set @shouldAndAppend = false;



if(_customerId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

           
set @isWhereAppened = true;

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`customerId` = ", quote(_customerId));
end if;




if(_legalEntityId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @shouldAndAppend = true;
end if;

if(@isWhereAppened
and @shouldAndAppend) then
            set @select_statement = CONCAT(@select_statement , " and");

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`companyLegalUnit` = ", quote(_legalEntityId));
end if;



if(_coreCustomerId != '') then
        if(! @isWhereAppened) then
            set @select_statement = CONCAT(@select_statement , " where");

set @shouldAndAppend = true;
end if;

if(@isWhereAppened
and @shouldAndAppend) then
            set @select_statement = CONCAT(@select_statement , " and");

set @shouldAndAppend = true;
end if;

set @select_statement = concat(@select_statement , " `contractcustomers`.`coreCustomerId` = ", quote(_coreCustomerId));
end if;

set @select_statement =  concat(@select_statement ,");");


prepare stmt from @select_statement;

execute stmt;

deallocate prepare stmt;
end$$
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
       SET @accounts_statement = 'DELETE FROM customeraccounts where ';
       SET @excluded_accounts_statement = 'DELETE FROM excludedcustomeraccounts where ';
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
              SET @accounts_statement = CONCAT(@accounts_statement , @where_clause1); 
              SET @excluded_accounts_statement = CONCAT(@excluded_accounts_statement , @where_clause1);
              SET @group_statement = CONCAT(@group_statement , @where_clause1); 
              SET @action_statement = CONCAT(@action_statement , @where_clause1);     
              SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);    
              SET @excluded_action_statement = CONCAT(@excluded_action_statement , @where_clause1);
                     
       
              PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @suspended_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;


END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customrole_contract_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `customrole_contract_delete_proc`(in customRoleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
       
       SET @contract_statement = 'DELETE FROM contractcustomrole where';
       SET @accounts_statement = 'DELETE FROM customroleaccounts where ';
       SET @excludedaccounts_statement = 'DELETE FROM excludedcustomroleaccounts where ';
       SET @action_statement = 'DELETE FROM customroleactionlimits where ';
       SET @excluded_action_statement = 'DELETE FROM excludedcustomroleactionlimits where ';
       SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
       
       SET @shouldAndAppend = false;
       SET @where_clause = CONCAT(' `companyLegalUnit` = ', quote(legalEntityId), ' AND ');
       SET @where_clause1 = CONCAT(' `companyLegalUnit` = ', quote(legalEntityId), ' AND ');
       SET @where_clause2 = CONCAT(' `companyLegalUnit` = ', quote(legalEntityId), ' AND ');
       if(customRoleId != '') THEN
              SET @where_clause = CONCAT(@where_clause , '`customRoleId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(customRoleId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`customRole_id` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(customRoleId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`Customer_id` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(customRoleId));
              SET @shouldAndAppend = true;
       
       END IF;
       
       IF(contractId != '') THEN
              IF(@shouldAndAppend = true) THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
                     SET @where_clause2 = CONCAT(@where_clause2 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(contractId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`contractId` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(contractId));
              SET @shouldAndAppend = true;
       END IF;
       
       IF(coreCustomerId != '') THEN
              IF(@shouldAndAppend = true) THEN
                     SET @where_clause = CONCAT(@where_clause , ' AND ');
                     SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
                     SET @where_clause2 = CONCAT(@where_clause2 , ' AND ');
              END if;
              SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
              SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
              SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
              SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
              SET @where_clause2 = CONCAT(@where_clause2 , '`coreCustomerId` = ');
              SET @where_clause2 = CONCAT(@where_clause2 , quote(coreCustomerId));
       END IF;
       

              SET @contract_statement = CONCAT(@contract_statement , @where_clause);  
              SET @accounts_statement = CONCAT(@accounts_statement , @where_clause);
              SET @excludedaccounts_statement = CONCAT(@excludedaccounts_statement , @where_clause);
              SET @action_statement = CONCAT(@action_statement , @where_clause1);
              SET @excluded_action_statement = CONCAT(@excluded_action_statement , @where_clause1);
              SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause2);    
       
              PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excludedaccounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @excluded_action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
              PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `customeraction_save_proc`;

DELIMITER $$

CREATE PROCEDURE `customeraction_save_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\'',UUID(),'\'',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
           	set @query = concat('INSERT INTO customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId, limitType_id,value,companyLegalUnit) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `excluded_customeraction_save_proc`;

DELIMITER $$

CREATE PROCEDURE `excluded_customeraction_save_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = concat('\'',UUID(),'\'',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
             set @query = concat('INSERT INTO excludedcustomeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,companyLegalUnit) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `membership_customer_search_proc`;

DELIMITER $$

CREATE PROCEDURE `membership_customer_search_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _name varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _status varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _city varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _zipCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

		SET @select_statement = concat("select membership.id , membership.industry,membership.name , membership.firstName , membership.lastName , membership.phone , membership.isBusinessType , membership.taxId , membership.faxId , membership.email , address.addressLine1 ,address.addressLine2 , address.cityName , address.country , address.zipCode , address.state from membership LEFT JOIN address on (membership.addressId = address.id) where membership.id is not null and membership.companyLegalEntity = ",quote(_legalEntityId));

	IF(_id != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.id = ",quote(_id));
    END IF;
    
    IF(_name != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.name like '%",_name,"%'");
    END IF;
    
    IF(_email != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.email = ",quote(_email));
    END IF;
    
    IF(_phone != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.phone = ",quote(_phone));
    END IF;
    
    IF(_dateOfBirth != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.dateOfBirth = ",quote(_dateOfBirth));
    END IF;
		
	SET @address_select_statement = "";
	IF(_city != "" OR _country!= "" OR _zipCode!= "") THEN
    
		SET @address_select_statement = concat("select address.id from address where address.id is not null");
        IF(_city != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.cityName = ",quote(_city));
        END IF;
        
        IF(_country != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.country = ",quote(_country));
        END IF;
        
        IF(_zipCode != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.zipCode = ",quote(_zipCode));
        END IF;
        
    END IF;
    
	IF(@address_select_statement != "") THEN 
		SET @select_statement = concat(@select_statement , " and membership.addressId in (", @address_select_statement , ")");
    END IF;
    
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
      
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `get_validcorecustomerslist_proc`;

DELIMITER $$
CREATE PROCEDURE `get_validcorecustomerslist_proc`(
IN _coreCustomersCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN companyLegalUnitId VARCHAR(200) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @list = (SELECT _coreCustomersCSV);
SET @validcorecustomersCSV = '';

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    SET @customers = (SELECT group_concat(id SEPARATOR ",") from contractcorecustomers WHERE (coreCustomerId=@value
	and companyLegalUnit = companyLegalUnitId));
    IF ISNULL(@customers)>0 OR @customers="" THEN 
        IF @validcorecustomersCSV='' THEN
           SET @validcorecustomersCSV = @value;
		ELSE 
           SET @validcorecustomersCSV = CONCAT(@validcorecustomersCSV,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

select @validcorecustomersCSV As validCustomers;
END$$
DELIMITER ;

ALTER TABLE `suspendedcustomers` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';
ALTER TABLE `membership` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

DROP PROCEDURE IF EXISTS `user_customers_proc`;

delimiter $$
CREATE  PROCEDURE `user_customers_proc`(
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _legalEntityId varchar(100) character set UTF8 collate utf8_general_ci

)
BEGIN
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
      set @select_statement =  concat(@select_statement ," `contractcustomers`.`coreCustomerId` = ",quote(_coreCustomerId));
    END IF;
    set @select_statement =  concat(@select_statement ,");");
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER;



ALTER TABLE `membershiprelation` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';


DROP PROCEDURE IF EXISTS `membership_relative_customer_get_proc`;

DELIMITER $$

CREATE PROCEDURE `membership_relative_customer_get_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci 
)
BEGIN

    select  membership.id , 
			membership.name , 
            membership.firstName , 
            membership.lastName , 
            membership.phone , 
            membership.email , 
			membership.dateOfBirth, 
			membership.taxId,
			membership.faxId,
            membership.industry, 
			membership.isBusinessType,
            membershiprelation.relationshipId,
            membershiprelation.relationshipName, 
            membershiprelation.companyLegalUnit,
            address.addressLine1 ,
			address.addressLine2 , 
            address.cityName , 
            address.country , 
            address.zipCode , 
            address.state 
			from 
            membership 
			LEFT JOIN address on (membership.addressId = address.id)
            JOIN membershiprelation on (membershiprelation.relatedMebershipId  = membership.id)
			where membership.id in (select membershiprelation.relatedMebershipId  from membershiprelation where membershiprelation.membershipId = _id)
            and membershiprelation.membershipId  = _id and membershiprelation.companyLegalUnit  = _legalEntityId;
      
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
	SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id )");
	
	IF(_accessPolicyIdList != '') THEN 
		SET @action_select_statement = CONCAT(@action_select_statement , "WHERE FIND_IN_SET(featureaction.accessPolicyId ,",QUOTE(_accessPolicyIdList),")");
	END IF;
	
    SET @action_select_statement = CONCAT(@action_select_statement,")");
    
	IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT("(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
	IF(_userId != '') THEN 
		SET @select_statement = CONCAT("(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
		IF(_coreCustomerId != '') THEN 
			SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
        END IF;
        SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    SET @select_statement = "( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue";
    if(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
    END IF;
	IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
    END IF;  
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
    END IF;
    SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY' AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.companyLegalUnit = ",QUOTE(_legalEntityId),
    " AND contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId),")");
       
    SET @select_statement = CONCAT(@select_statement , " UNION (SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue");
	IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
    END IF;
    IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
                                                                                                        
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)");
	END IF;
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Group_id = " , QUOTE(_roleId));
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.coreCustomerId = " , QUOTE(_coreCustomerId));
	END IF;
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.companyLegalUnit = ",QUOTE(_legalEntityId),
    " AND contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId),")");
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
   
    
END$$
DELIMITER ;

ALTER TABLE `customroleaccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';
ALTER TABLE `excludedcustomroleaccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';
ALTER TABLE `customroleactionlimits` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';
ALTER TABLE `excludedcustomroleactionlimits` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

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
	WHERE ca.Customer_id = _id and ca.Action_id = 'USER_MANAGEMENT_VIEW' and ca.companyLegalUnit = _legalEntityId;
END$$

DELIMITER ;

ALTER TABLE `customview` ADD COLUMN `legalEntityId` VARCHAR(50) default 'ALL';
ALTER TABLE `card` ADD COLUMN `legalEntityId` VARCHAR(50) default 'ALL';



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
	SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id )");
	
	IF(_accessPolicyIdList != '') THEN 
		SET @action_select_statement = CONCAT(@action_select_statement , "WHERE FIND_IN_SET(featureaction.accessPolicyId ,",QUOTE(_accessPolicyIdList),")");
	END IF;
	
    SET @action_select_statement = CONCAT(@action_select_statement,")");
    
	IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT("(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
	IF(_userId != '') THEN 
		SET @select_statement = CONCAT("(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
		IF(_coreCustomerId != '') THEN 
			SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
        END IF;
        SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    SET @select_statement = "( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue";
    if(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
    END IF;
	IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
    END IF;  
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
    END IF;
    SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
   
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY' AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.companyLegalUnit = ",QUOTE(_legalEntityId),
  ")");
       
    SET @select_statement = CONCAT(@select_statement , " UNION (SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue");
	IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
    END IF;
    IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
                                                                                                        
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
	END IF;
    
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)");

	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Group_id = " , QUOTE(_roleId));
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.coreCustomerId = " , QUOTE(_coreCustomerId));
	END IF;
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.companyLegalUnit = ",QUOTE(_legalEntityId),")");
   PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    
   
    
END$$
DELIMITER ;


DROP procedure IF EXISTS `get_associated_contractaccounts_proc`;

DELIMITER $$
CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `membership_customer_search_proc`;

DELIMITER $$

CREATE PROCEDURE `membership_customer_search_proc`(
in _id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _name varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _status varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _city varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _country varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _zipCode varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

		SET @select_statement = concat("select membership.id , membership.industry,membership.name , membership.firstName , membership.lastName , membership.phone , membership.isBusinessType , membership.taxId , membership.faxId , membership.email , address.addressLine1 ,address.addressLine2 , address.cityName , address.country , address.zipCode , address.state from membership LEFT JOIN address on (membership.addressId = address.id) where membership.id is not null and membership.companyLegalUnit = ",quote(_legalEntityId));

	IF(_id != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.id = ",quote(_id));
    END IF;
    
    IF(_name != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.name like '%",_name,"%'");
    END IF;
    
    IF(_email != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.email = ",quote(_email));
    END IF;
    
    IF(_phone != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.phone = ",quote(_phone));
    END IF;
    
    IF(_dateOfBirth != "") THEN
		SET @select_statement = concat(@select_statement , " and membership.dateOfBirth = ",quote(_dateOfBirth));
    END IF;
		
	SET @address_select_statement = "";
	IF(_city != "" OR _country!= "" OR _zipCode!= "") THEN
    
		SET @address_select_statement = concat("select address.id from address where address.id is not null");
        IF(_city != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.cityName = ",quote(_city));
        END IF;
        
        IF(_country != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.country = ",quote(_country));
        END IF;
        
        IF(_zipCode != "") THEN
			SET @address_select_statement = concat(@address_select_statement , " and address.zipCode = ",quote(_zipCode));
        END IF;
        
    END IF;
    
	IF(@address_select_statement != "") THEN 
		SET @select_statement = concat(@select_statement , " and membership.addressId in (", @address_select_statement , ")");
    END IF;
    
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
      
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS contract_address_communication_proc;
DELIMITER $$

CREATE PROCEDURE `contract_address_communication_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SELECT c.*, s.name AS servicedefinitionName FROM `contract` as c JOIN `servicedefinition`as s ON (c.`servicedefinitionID` = s.`id`) WHERE c.`Id` = _contractId AND c.`companyLegalUnit` = _legalEntityId;

SELECT * from `contractcommunication` WHERE `contractId` = _contractId AND `companyLegalUnit` = _legalEntityId;

SELECT * FROM `address` WHERE id IN (SELECT DISTINCT `addressId` from `contractaddress` WHERE `contractId` = _contractId AND `companyLegalUnit` = _legalEntityId);
                 
end $$
DELIMITER ;


DROP procedure IF EXISTS `contract_actionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_actionlimits_create_proc`(
	IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

      DECLARE tempLimitValue varchar(255) DEFAULT "";

      SET SESSION group_concat_max_len = 100000000;
      SET @index = 0;
      SET @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      SET @serviceDefinitionId = "";
      insertRecords : LOOP
          SET @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            SET @recordsData = concat('\"',UUID(),'\"',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));

            SET @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',2 ), '\"', -1 );
            SET @customerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 );
            SET @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4 ), ',\"', -1 );
            SET @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );
            SET @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            SET @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );
			SET @legalEntityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',8), ',\"', -1 );
            SET @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',9), ',\"', -1 );
            SET @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            SELECT @featureId;
            SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',8),'\"');

            IF isnull(@serviceDefinitionId) OR @serviceDefinitionId = "" THEN
                SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @contractId);
                     END IF;

                     IF @limitId != '@' AND  @limitValue != '@' THEN 
                SET @limitAtFI = (SELECT actionlimit.value FROM actionlimit WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

                SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE 
					servicedefinitionactionlimit.actionId = @actionId AND 
					servicedefinitionactionlimit.limitTypeId = @limitId AND 
					servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);
                SET tempLimitValue = LEAST(@limitAtFI,@limitATServiceDefinition,@limitValue);
            END IF;

            IF !isnull(tempLimitValue) AND tempLimitValue !='' THEN
               SET @limitValue = tempLimitValue;
                     END IF;

            SET @contarctFeatures = (SELECT group_concat(contractfeatures.id SEPARATOR ",") FROM contractfeatures WHERE 
				contractId = @contractId AND 
				coreCustomerId = @customerId AND 
				featureId = @featureId AND 
				companyLegalUnit = @legalEntityId);

			SET @serviceDefinitionActions = (SELECT group_concat(servicedefinitionactionlimit.id SEPARATOR ",") FROM servicedefinitionactionlimit WHERE 
			 actionId = @actionId AND 
			 serviceDefinitionId = @serviceDefinitionId AND 
			 companyLegalUnit = @legalEntityId);

			SET @existingActionLimitRecords = (SELECT group_concat(contractactionlimit.id SEPARATOR ",") FROM contractactionlimit WHERE contractId = @contractId AND 
			coreCustomerId = @customerId AND 
			featureId = @featureId AND actionId = @actionId AND 
			limitTypeId = @limitId AND companyLegalUnit = @legalEntityId);

            SET @existingActionRecords = (SELECT group_concat(contractactionlimit.id SEPARATOR ",") FROM contractactionlimit WHERE contractId=@contractId AND coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId  AND companyLegalUnit = @legalEntityId);
              
			SET @existingActionRecords1 = (SELECT group_concat(accountlevelactionlimit.id SEPARATOR ",") FROM accountlevelactionlimit WHERE contractId = @contractId AND 
			coreCustomerId = @customerId AND 
			accountId = @accountId AND 
			featureId= @featureId AND 
			actionId= @actionId  AND 
			companyLegalUnit = @legalEntityId);

			IF !isnull(@contarctFeatures) AND @contarctFeatures != "" AND !isnull(@serviceDefinitionActions) AND @serviceDefinitionActions != ""  THEN
                IF !isnull(@existingActionLimitRecords) AND @existingActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE contractactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingActionLimitRecords,'\'',';'); 
                ELSE 
					  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords) OR @existingActionRecords = "") THEN
						   SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit) VALUES (',@recordsDataWithoutLimits,');');
					  ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
							 SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit, limitTypeId,value) VALUES (',@recordsData,');');
					  END IF;
                END IF;
					IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords1) OR @existingActionRecords1 = "")  THEN
							   SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit) VALUES (',@recordsDataWithoutLimits,');');
					   ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
								 SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit, limitTypeId,value) VALUES (',@recordsData,');');
					END IF;
				END IF;
			 END IF;
            END IF;
            IF !isnull(@query) and @query != "" THEN
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            END IF;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `contract_action_limit_update`;

DELIMITER $$
CREATE PROCEDURE `contract_action_limit_update`(
  IN _contractActionLimit LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
       SET @numOfRecords = LENGTH(_contractActionLimit) - LENGTH(REPLACE(_contractActionLimit, '|', '')) + 1;
  updateRecords : LOOP 
     SET index1 = index1 + 1;
              IF index1 = @numOfRecords + 1 THEN 
                     LEAVE updateRecords;
              ELSE
                  SET @contractValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_contractActionLimit, '|', index1), '|', -1 );
                     SET @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',1 ), '\"', -1 );
                     SET @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',2), ',\"', -1 );
                     SET @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',3), ',\"', -1 );
                     SET @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',4), ',\"', -1 );
                     SET @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',5), ',\"', -1 );
                     SET @legalEntityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',6), ',\"', -1 );
					 SET @limitTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',7), ',\"', -1 );
                     SET @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, ',\"',-1 ), '\"', 1 );
            SET @num = cast(@limitValue AS DECIMAL(20,2));
                     UPDATE contractactionlimit SET value = @num WHERE contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId AND companyLegalUnit = @legalEntityId; 
              END IF;
              
       END LOOP updateRecords;
END$$
DELIMITER ;
ALTER TABLE `accountsstatementfiles` ADD COLUMN `legalEntityId` VARCHAR(50) default 'ALL';


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
	SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id )");
	
	IF(_accessPolicyIdList != '') THEN 
		SET @action_select_statement = CONCAT(@action_select_statement , "WHERE FIND_IN_SET(featureaction.accessPolicyId ,",QUOTE(_accessPolicyIdList),")");
	END IF;
	
    SET @action_select_statement = CONCAT(@action_select_statement,")");
    
	IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT("(SELECT contractactionlimit.actionId AS actionId FROM contractactionlimit WHERE contractactionlimit.coreCustomerId = ",QUOTE(_coreCustomerId));
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
	IF(_userId != '') THEN 
		SET @select_statement = CONCAT("(SELECT customeraction.Action_id AS actionId FROM customeraction WHERE customeraction.Customer_id = ",QUOTE(_userId));
		IF(_coreCustomerId != '') THEN 
			SET @select_statement = CONCAT(@select_statement , " AND customeraction.coreCustomerId = " , QUOTE(_coreCustomerId));
        END IF;
        SET @select_statement = CONCAT(@select_statement , " AND customeraction.isAllowed = '1' AND customeraction.Action_id IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;
    SET @select_statement = "( SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue";
    if(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
    END IF;
	IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
    END IF;  
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
    END IF;
    SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
   
	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY' AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.companyLegalUnit = ",QUOTE(_legalEntityId),
  ")");
       
    SET @select_statement = CONCAT(@select_statement , " UNION (SELECT 
								feature.id AS featureId,
								featuredisplaynamedescription.displayName AS featureName,
                                featuredisplaynamedescription.displayDescription AS featureDescription,
                                feature.Status_id AS featureStatus,
                                featureaction.status AS actionStatus,
                                featureaction.id AS actionId,
                                contractactionlimit.companyLegalUnit AS legalEntityId,  
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue");
	IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
    END IF;
    IF(_roleId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
    END IF;
    IF(_coreCustomerId != '') THEN 
		SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");
    END IF;
	SET @select_statement = CONCAT(@select_statement , " FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
                                                                                                        
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
	END IF;
    
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)");

	SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
    IF(_serviceDefinitionId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.serviceDefinitionId = " , QUOTE(_serviceDefinitionId));
	END IF;
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Group_id = " , QUOTE(_roleId));
	END IF;
    IF(_coreCustomerId != '') THEN
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.coreCustomerId = " , QUOTE(_coreCustomerId));
	END IF;
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.id IN " , @action_select_statement);
    SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale));
    SET @select_statement = CONCAT(@select_statement , " AND featureaction.companyLegalUnit = ",QUOTE(_legalEntityId),")");
   PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
    
   
    
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS user_securityattributes_get_proc;

DELIMITER $$

CREATE  PROCEDURE `dbxdb`.`user_securityattributes_get_proc`(
in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)



BEGIN
	
	
	SET SESSION group_concat_max_len = 10000000;

SET @userAssociatedCoreCustomers =  (SELECT group_concat(distinct contractcustomers.coreCustomerId SEPARATOR ",") FROM contractcustomers WHERE contractcustomers.customerId = _userId and contractcustomers.companyLegalUnit = _legalEntityId);

SET @userAssociatedContracts =  (SELECT group_concat(distinct contractcustomers.contractId SEPARATOR ",") FROM contractcustomers WHERE contractcustomers.customerId = _userId and contractcustomers.companyLegalUnit = _legalEntityId);

SET @userAssociatedServiceDefinitions = (SELECT group_concat(distinct contract.servicedefinitionId SEPARATOR ",") FROM contract WHERE FIND_IN_SET(contract.id,@userAssociatedContracts) 
                                        AND contract.statusId = 'SID_CONTRACT_ACTIVE' and contract.companyLegalUnit = _legalEntityId);

SET @userAssociatedGroups =  (SELECT group_concat(distinct customergroup.Group_id SEPARATOR ",") FROM customergroup WHERE customergroup.Customer_id = _userId and customergroup.companyLegalUnit = _legalEntityId);

SET @actionsAtCoreCustomers = (SELECT group_concat(distinct contractactionlimit.actionId SEPARATOR ",") FROM contractactionlimit WHERE FIND_IN_SET(contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers));

SET @newActionsAtCoreCustomers = (SELECT group_concat(distinct contractactionlimit.actionId SEPARATOR ",") FROM contractactionlimit WHERE FIND_IN_SET(contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) and contractactionlimit.isNewAction = '1');

SET @actionsAtServiceDefinitions = (SELECT group_concat(distinct servicedefinitionactionlimit.actionId SEPARATOR ",") FROM servicedefinitionactionlimit WHERE FIND_IN_SET(servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions) and servicedefinitionactionlimit.companyLegalUnit = _legalEntityId);

SET @actionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups) and groupactionlimit.companyLegalUnit = _legalEntityId);

SET @newActionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups) and groupactionlimit.isNewAction = '1' and groupactionlimit.companyLegalUnit = _legalEntityId);

SET @actionsAtuser = (SELECT group_concat(distinct customeraction.Action_id SEPARATOR ",") FROM customeraction WHERE customeraction.Customer_id = _userId 
                     AND (customeraction.isAllowed ='1' OR customeraction.isAllowed = true) and customeraction.companyLegalUnit = _legalEntityId);
                     
SET @activeFeaturesAtFI = (SELECT group_concat(distinct feature.id SEPARATOR ",") FROM feature WHERE Status_id = 'SID_FEATURE_ACTIVE' and feature.companyLegalUnit = _legalEntityId); 

SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE' AND
                                featureaction.companyLegalUnit  = _legalEntityId AND
                                FIND_IN_SET(featureaction.id,@actionsAtuser) AND
                                FIND_IN_SET(featureaction.id,@actionsAtGroups) AND
                                FIND_IN_SET(featureaction.id,@actionsAtServiceDefinitions) AND
                                FIND_IN_SET(featureaction.id,@actionsAtCoreCustomers) AND 
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));
                               
SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE'
                                AND featureaction.companyLegalUnit  = _legalEntityId AND(
                                FIND_IN_SET(featureaction.id,@intersectedUserActions) OR
                                FIND_IN_SET(featureaction.id,@newActionsAtCoreCustomers) OR
                                FIND_IN_SET(featureaction.id,@newActionsAtGroups)) AND
                                FIND_IN_SET(featureaction.id,@actionsAtServiceDefinitions) AND 
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));                               
                                
SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (SELECT group_concat(distinct featureaction.Feature_id SEPARATOR ",") FROM featureaction WHERE 
                                  FIND_IN_SET(featureaction.id,@intersectedUserActions));
                                  
SELECT @intersectedUserFeatures AS features;

	
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `prospect_securityattributes_get_proc`;
DELIMITER $$

CREATE PROCEDURE `prospect_securityattributes_get_proc`(
in _userId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	
	SET SESSION group_concat_max_len = 10000000;

SET @userAssociatedGroups =  (SELECT group_concat(distinct customergroup.Group_id SEPARATOR ",") FROM customergroup WHERE customergroup.Customer_id = _userId and customergroup.companyLegalUnit  = _legalEntityId);


SET @actionsAtGroups = (SELECT group_concat(distinct groupactionlimit.Action_id SEPARATOR ",") FROM groupactionlimit WHERE FIND_IN_SET(groupactionlimit.Group_id,@userAssociatedGroups) and groupactionlimit.companyLegalUnit  = _legalEntityId);


SET @activeFeaturesAtFI = (SELECT group_concat(distinct feature.id SEPARATOR ",") FROM feature WHERE Status_id = 'SID_FEATURE_ACTIVE' and feature.companyLegalUnit = _legalEntityId); 

SET @intersectedUserActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") FROM featureaction WHERE 
                                featureaction.status = 'SID_ACTION_ACTIVE' AND
                                featureaction.companyLegalUnit = _legalEntityId AND
                                FIND_IN_SET(featureaction.id,@actionsAtGroups) AND
                                FIND_IN_SET(featureaction.Feature_id,@activeFeaturesAtFI));
                               
SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (SELECT group_concat(distinct featureaction.Feature_id SEPARATOR ",") FROM featureaction WHERE 
                                  FIND_IN_SET(featureaction.id,@intersectedUserActions));
								  
SELECT @intersectedUserFeatures AS features;
                       
END$$
DELIMITER ;



DROP PROCEDURE IF EXISTS `customrole_actionlimits_create_proc`;

DELIMITER $$

CREATE PROCEDURE `customrole_actionlimits_create_proc`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _customRoleId bigint(20))
BEGIN
      DELETE FROM customroleactionlimits where customroleactionlimits.customRole_id = _customRoleId;
      set @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            set @query = concat('INSERT INTO customroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed, limitGroupId, limitType_id,value,companyLegalUnit) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
          END IF;
      END LOOP insertRecords; 
END$$
DELIMITER ;


DROP procedure IF EXISTS `dbpevents_getCustomerData`;
DELIMITER $$
CREATE PROCEDURE `dbpevents_getCustomerData` (_customerids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,_usernames TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select id as CustomerId, UserName,companyLegalUnit from customer where FIND_IN_SET(`customer`.`id`,_customerids) or FIND_IN_SET(`customer`.`UserName`,_usernames) ;
END$$
DELIMITER ;

DROP procedure IF EXISTS `getCustomersIdFromCoreId`;
DELIMITER $$
CREATE PROCEDURE `getCustomersIdFromCoreId`(_corecustomers  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as custid ,BackendId as corecustid, companyLegalUnit FROM backendidentifier where FIND_IN_SET (`backendidentifier`.`BackendId`,_corecustomers) ; 
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

DROP procedure IF EXISTS `default_contractactions_create_proc`;
DELIMITER $$
CREATE PROCEDURE `default_contractactions_create_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE featureId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT "" ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE accountId varchar(255) DEFAULT "" ;
 DECLARE actualLimitId varchar(255) DEFAULT "" ;
 DECLARE _accountsCSV varchar(255) DEFAULT ""  ;
 DECLARE coreCustomerId varchar(255) DEFAULT "" ;

DECLARE coreCustomers CURSOR
         FOR (SELECT contractcorecustomers.coreCustomerId FROM contractcorecustomers WHERE contractcorecustomers.contractId = _contractId and contractcorecustomers.companyLegalUnit = _legalEntityId);
DECLARE accounts CURSOR
         FOR (SELECT contractaccounts.accountId  FROM contractaccounts WHERE contractaccounts.contractId  = _contractId and contractaccounts.companyLegalUnit = _legalEntityId );

DECLARE transactionLimits CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction  where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id  = 'MONETARY' and featureaction.isAccountLevel = '1') and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE accountLevelPermissions CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id = 'NON_MONETARY' and featureaction.isAccountLevel = '1') and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId) ;
DECLARE globalLevelPermissions CURSOR 
		  FOR (select id COLLATE utf8_general_ci from featureaction where FIND_IN_SET(id COLLATE utf8_general_ci,@validActionsList COLLATE utf8_general_ci) AND (featureaction.Type_id  = 'NON_MONETARY' and featureaction.isAccountLevel  = '0') and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId);
DECLARE limits CURSOR 
			FOR (select LimitType_id COLLATE utf8_general_ci from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId  and companyLegalUnit  COLLATE utf8_general_ci = _legalEntityId );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;

SET _accountsCSV = (SELECT group_concat(distinct contractaccounts.accountId SEPARATOR ",") FROM contractaccounts WHERE contractaccounts.contractId  = _contractId and contractaccounts.companyLegalUnit  = _legalEntityId );

 
SET @serviceDefinitionId = (SELECT servicedefinitionId  from contract WHERE id = _contractId and companyLegalUnit = _legalEntityId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId );
                        
SET @validFIActions = (SELECT group_concat(distinct id  SEPARATOR ",") FROM featureaction
								where featureaction.Feature_id  in (select
									contractfeatures.featureId  from contractfeatures
                                    where contractfeatures.contractId  = _contractId and contractfeatures.companyLegalUnit = _legalEntityId));

SET @validActionsList = (SELECT group_concat(distinct actionId  SEPARATOR ",") FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId = @serviceDefinitionId  AND FIND_IN_SET(actionId COLLATE utf8_general_ci,@validFIActions COLLATE utf8_general_ci));
                 


OPEN coreCustomers;
getCoreCustomer : LOOP
FETCH coreCustomers INTO coreCustomerId;
IF finished = 1 THEN
	LEAVE getCoreCustomer;
ELSE
	

SET finished = 0;
OPEN accounts; 
	getAccount : LOOP
	FETCH accounts INTO accountId;
	IF finished = 1 THEN 
		LEAVE getAccount;
	ELSE
	  OPEN accountLevelPermissions; 
	  accountLevelPermissionsActions: LOOP
			FETCH accountLevelPermissions INTO featureActionId;
            SET featureId = (select Feature_id  from featureaction where featureaction.id COLLATE utf8_general_ci= featureActionId and companyLegalUnit COLLATE utf8_general_ci = _legalEntityId);
        
			IF finished = 1 THEN 
				LEAVE accountLevelPermissionsActions;
			else
				SET @id = (SELECT LEFT(UUID(), 50));
					   INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId,companyLegalUnit) VALUES
					(@id,_contractId,coreCustomerId,accountId,featureId,featureActionId,_legalEntityId);
			END IF;
			ITERATE  accountLevelPermissionsActions;
	  END LOOP accountLevelPermissionsActions;
	  CLOSE accountLevelPermissions;
	  SET finished = 0;
	 ITERATE  getAccount;
	END IF;
END LOOP getAccount;
CLOSE accounts;

	 SET finished = 0;
ITERATE getCoreCustomer;
END IF;
END LOOP getCoreCustomer;
CLOSE coreCustomers;

END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `contract_action_limit_update`;

DELIMITER $$
CREATE PROCEDURE `contract_action_limit_update`(
  IN _contractActionLimit LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
  
)
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
       SET @numOfRecords = LENGTH(_contractActionLimit) - LENGTH(REPLACE(_contractActionLimit, '|', '')) + 1;
  updateRecords : LOOP 
     SET index1 = index1 + 1;
              IF index1 = @numOfRecords + 1 THEN 
                     LEAVE updateRecords;
              ELSE
                  SET @contractValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_contractActionLimit, '|', index1), '|', -1 );
                     SET @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',1 ), '\"', -1 );
                     SET @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',2), ',\"', -1 );
                    SET @accid = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',3), ',\"', -1 );
                     SET @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',4), ',\"', -1 );
                     SET @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',5), ',\"', -1 );
                     SET @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',6), ',\"', -1 );
                     SET @legalEntityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',7), ',\"', -1 );
					 SET @limitTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, '\",',8), ',\"', -1 );
                     SET @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@contractValues, ',\"',-1 ), '\"', 1 );
					 SET @num = cast(@limitValue AS DECIMAL(20,2));
                     UPDATE contractactionlimit SET value = @num WHERE contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId AND companyLegalUnit = @legalEntityId; 
              END IF;
              
       END LOOP updateRecords;
END$$
DELIMITER ;


DROP procedure IF EXISTS `contract_actionlimits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_actionlimits_create_proc`(
IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

      DECLARE tempLimitValue varchar(255) DEFAULT "";

      SET SESSION group_concat_max_len = 100000000;
      SET @index = 0;
      SET @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      SET @serviceDefinitionId = "";
      insertRecords : LOOP
          SET @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            SET @recordsData = concat('\"',UUID(),'\"',',', SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));

            SET @contractId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',2 ), '\"', -1 );
            SET @customerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',3 ), ',\"', -1 );
            SET @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',4 ), ',\"', -1 );
            SET @featureId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',5), ',\"', -1 );
            SET @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',6), ',\"', -1 );
            SET @isNewAction = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',7), ',\"', -1 );
			SET @legalEntityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',8), ',\"', -1 );
            SET @limitId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '\",',9), ',\"', -1 );
            SET @limitValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',\"',-1 ), '\"', 1 );
            SET @recordsDataWithoutLimits = concat(SUBSTRING_INDEX(@recordsData, '\",',8),'\"');
            IF isnull(@serviceDefinitionId) OR @serviceDefinitionId = "" THEN
                SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @contractId);
                     END IF;

                     IF @limitId != '@' AND  @limitValue != '@' THEN 
                SET @limitAtFI = (SELECT actionlimit.value FROM actionlimit WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

                SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM servicedefinitionactionlimit WHERE 
					servicedefinitionactionlimit.actionId = @actionId AND 
					servicedefinitionactionlimit.limitTypeId = @limitId AND 
					servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);
                SET tempLimitValue = LEAST(@limitAtFI,@limitATServiceDefinition,@limitValue);
            END IF;
            IF !isnull(tempLimitValue) AND tempLimitValue !='' THEN
               SET @limitValue = tempLimitValue;
                     END IF;

            SET @contarctFeatures = (SELECT group_concat(contractfeatures.id SEPARATOR ",") FROM contractfeatures WHERE 
				contractId = @contractId AND 
				coreCustomerId = @customerId AND 
				featureId = @featureId AND 
				companyLegalUnit = @legalEntityId);

			SET @serviceDefinitionActions = (SELECT group_concat(servicedefinitionactionlimit.id SEPARATOR ",") FROM servicedefinitionactionlimit WHERE 
			 actionId = @actionId AND 
			 serviceDefinitionId = @serviceDefinitionId AND 
			 companyLegalUnit = @legalEntityId);

			SET @existingActionLimitRecords = (SELECT group_concat(contractactionlimit.id SEPARATOR ",") FROM contractactionlimit WHERE contractId = @contractId AND 
			coreCustomerId = @customerId AND 
			featureId = @featureId AND actionId = @actionId AND 
			limitTypeId = @limitId AND companyLegalUnit = @legalEntityId);
		
			SET @existingAccountActionLimitRecords = (SELECT group_concat(accountlevelactionlimit.id SEPARATOR ",") FROM accountlevelactionlimit WHERE contractId = @contractId AND 
			coreCustomerId = @customerId AND 
			featureId = @featureId AND actionId = @actionId AND 
			limitTypeId = @limitId AND companyLegalUnit = @legalEntityId);

            SET @existingActionRecords = (SELECT group_concat(contractactionlimit.id SEPARATOR ",") FROM contractactionlimit WHERE contractId=@contractId AND coreCustomerId =@customerId AND featureId= @featureId AND actionId= @actionId  AND companyLegalUnit = @legalEntityId);
              
			SET @existingActionRecords1 = (SELECT group_concat(accountlevelactionlimit.id SEPARATOR ",") FROM accountlevelactionlimit WHERE contractId = @contractId AND 
			coreCustomerId = @customerId AND 
			accountId = @accountId AND 
			featureId= @featureId AND 
			actionId= @actionId  AND 
			companyLegalUnit = @legalEntityId);
			IF !isnull(@contarctFeatures) AND @contarctFeatures != "" AND !isnull(@serviceDefinitionActions) AND @serviceDefinitionActions != "" AND (isnull(@accountId) OR @accountId = "") THEN
                IF !isnull(@existingActionLimitRecords) AND @existingActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE contractactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingActionLimitRecords,'\'',';'); 
                ELSE 
					  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords) OR @existingActionRecords = "") THEN
						   SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit) VALUES (',@recordsDataWithoutLimits,');');
					  ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
							 SET @query = concat('INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit, limitTypeId,value) VALUES (',@recordsData,');');
					  		END IF;
               		 END IF;					
				END IF;
			
			ELSE
				IF !isnull(@existingAccountActionLimitRecords) AND @existingAccountActionLimitRecords != "" THEN
                    SET @query = concat('UPDATE accountlevelactionlimit SET value = ','\'',@limitValue,'\'','WHERE id = ','\'',@existingAccountActionLimitRecords,'\'',';'); 
                ELSE 
					  IF (@limitId = '@' OR @limitValue = '@') AND (isnull(@existingActionRecords1) OR @existingActionRecords1 = "") THEN
						   SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit) VALUES (',@recordsDataWithoutLimits,');');
					  ELSE  IF @limitId != '@' AND  @limitValue != '@' AND !isnull(tempLimitValue) AND tempLimitValue !='' THEN    
							 SET @query = concat('INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId, isNewAction, companyLegalUnit, limitTypeId,value) VALUES (',@recordsData,');');
					  		END IF;
                	END IF;					
				 END IF;
            END IF;
            
            IF !isnull(@query) and @query != "" THEN
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            END IF;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;
SET FOREIGN_KEY_CHECKS = 1;
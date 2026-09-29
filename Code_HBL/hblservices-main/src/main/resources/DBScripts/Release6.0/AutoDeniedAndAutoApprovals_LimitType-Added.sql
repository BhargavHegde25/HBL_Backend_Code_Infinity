INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('AUTO_DENIED_MB_DAILY_LIMIT', 'Mobile Auto denied daily limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('AUTO_DENIED_MB_TRANSACTION_LIMIT', 'Mobile Auto denied transaction limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('AUTO_DENIED_MB_WEEKLY_LIMIT', 'Mobile Auto denied weekly limit', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PRE_APPROVED_MB_DAILY_LIMIT', 'Mobile Pre approved daily limit', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '2023-11-08 12:00:16', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PRE_APPROVED_MB_TRANSACTION_LIMIT', 'Mobile Pre approved trasaction limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');
INSERT INTO `dbxdb`.`limittype` (`id`, `description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PRE_APPROVED_MB_WEEKLY_LIMIT', 'Mobile Pre approved weekly limit', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '2023-11-08 12:00:15', '0');



USE `dbxdb`;
DROP procedure IF EXISTS `useractions_create_proc`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`useractions_create_proc`;
;

DELIMITER $$
USE `dbxdb`$$
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
 DECLARE minTxLimit varchar(255) DEFAULT ""  ;

DECLARE accounts CURSOR
         FOR (SELECT customeraccounts.Account_id FROM customeraccounts WHERE contractId = _contractId AND coreCustomerId = _coreCustomerId 
         AND Customer_id = _userId AND FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true ) and companyLegalUnit  = _legalEntityId);
DECLARE nonaccountlevelactions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '0' OR featureaction.isAccountLevel = false ) and companyLegalUnit = _legalEntityId);
DECLARE limits CURSOR 
        FOR (select LimitType_id from actionlimit where Action_id  = featureActionId  and companyLegalUnit = _legalEntityId);
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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId  and companyLegalUnit = _legalEntityId);
      
        IF finished = 1 THEN 
            LEAVE getAction;
        else
            OPEN limits; 
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN 
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT distinct value FROM contractactionlimit WHERE actionId = featureActionId 
               AND limitTypeId = limitId  
               AND contractId = _contractId 
               AND coreCustomerId = _coreCustomerId
               AND companyLegalUnit = _legalEntityId);
               
               SET minTxLimit = (SELECT value FROM contractactionlimit WHERE actionId = featureActionId 
               AND limitTypeId = 'MIN_TRANSACTION_LIMIT'
               AND contractId = _contractId 
               AND coreCustomerId = _coreCustomerId
               AND companyLegalUnit = _legalEntityId);
    
               if (limitId='MAX_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
               ELSEIF (limitId='MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
			   
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,minTxLimit,_legalEntityId);
				SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
				
			   ELSEIF (limitId='MB_MAX_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'AUTO_DENIED_MB_TRANSACTION_LIMIT';
               ELSEIF (limitId='MB_MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_MB_TRANSACTION_LIMIT';
               ELSEIF (limitId='MB_DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_MB_DAILY_LIMIT';
			   
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,minTxLimit,_legalEntityId);
				 SET actualLimitId = 'AUTO_DENIED_MB_DAILY_LIMIT';
			  
			    ELSEIF (limitId='MB_WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_MB_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,minTxLimit,_legalEntityId);
               SET actualLimitId = 'AUTO_DENIED_MB_WEEKLY_LIMIT';
			    SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,minTxLimit,_legalEntityId);
				
				 ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
			    SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,actualLimitId,minTxLimit,_legalEntityId);
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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId  
       and companyLegalUnit = _legalEntityId);
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
END$$

DELIMITER ;
;




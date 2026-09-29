ALTER TABLE `paymentfiles`
MODIFY COLUMN `paymentFileType` VARCHAR(100) NOT NULL;

ALTER TABLE `accounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `backendidentifier` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customeraddress` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customercommunication` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractcustomers` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customeraction` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customeraccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `excludedcustomeraccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `excludedcustomeraction` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customergroup` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractcustomrole` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractaccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contract` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractaddress` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractcorecustomers` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `excludedcontractaccounts` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractcommunication` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `address` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractactionlimit` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customerdevice` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customerpreference` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customerbusinesstype` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `contractfeatures` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';

ALTER TABLE `customerlimitgrouplimits` ADD COLUMN `companyLegalUnit` VARCHAR(50) default 'ALL';



DROP procedure IF EXISTS `user_account_default_actions_create_proc`;

DELIMITER $$
CREATE PROCEDURE `user_account_default_actions_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,  
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT ""  ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;
 DECLARE accountId varchar(255) DEFAULT "" ;
 DECLARE actualLimitId varchar(255) DEFAULT "" ;
 DECLARE featureId varchar(255) DEFAULT "" ;
 DECLARE coreCustomerId_value varchar(255) DEFAULT "" ;
  
DECLARE accounts CURSOR
         FOR (SELECT ca.Account_id FROM customeraccounts ca WHERE ca.contractId = _contractId AND ca.coreCustomerId = _coreCustomerId 
         AND ca.Customer_id = _userId AND FIND_IN_SET(ca.Account_id,_accountsCSV));
DECLARE accountLevelPermissions CURSOR 
		  FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.Type_id = 'NON_MONETARY' and featureaction.isAccountLevel = '1'));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true ));
DECLARE limits CURSOR 
        FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

SET SESSION group_concat_max_len = 100000000;


SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = _contractId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId);
                                      
SET @validFIActions = (SELECT group_concat(distinct id SEPARATOR ",") FROM featureaction
								where featureaction.Feature_id in (select
									contractfeatures.featureId from contractfeatures
                                    where contractfeatures.contractId = _contractId));
SET @validActionsList = (SELECT group_concat(distinct actionId SEPARATOR ",") FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId= @serviceDefinitionId AND FIND_IN_SET(actionId,@validFIActions));
                              
select @validActionsList;


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
			SET featureId = (select featureaction.Feature_id from featureaction where featureaction.id = featureActionId COLLATE utf8_general_ci);
			IF finished = 1 THEN 
				LEAVE accountLevelPermissionsActions;
			else
				SET @id = (SELECT LEFT(UUID(), 50));
			    INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId) VALUES
					(@id,_contractId,_coreCustomerId,accountId,featureId,featureActionId);
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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci);
       SET @limitGroupId = (SELECT limitgroupId FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci);
        IF finished = 1 THEN 
            LEAVE getAction;
        else
            OPEN limits; 
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN 
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT cal.value FROM contractactionlimit cal WHERE cal.actionId = featureActionId  COLLATE utf8_general_ci
               AND cal.limitTypeId = limitId COLLATE utf8_general_ci AND cal.contractId = _contractId AND cal.coreCustomerId = _coreCustomerId);
    
               if (limitId='MAX_TRANSACTION_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
				SET @id = (SELECT LEFT(UUID(), 50));
				INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value) VALUES
				(@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00);
               	SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
               	INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00);
               	SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
               
              if (limitId!='MIN_TRANSACTION_LIMIT') THEN
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value) VALUES
               (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,@limitvalue);
                
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,limitId,@limitvalue);
				END IF;
                SET entryStatus = 1;
               ITERATE  getlimit;
            END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
                   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true);
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

END$$

DELIMITER ;



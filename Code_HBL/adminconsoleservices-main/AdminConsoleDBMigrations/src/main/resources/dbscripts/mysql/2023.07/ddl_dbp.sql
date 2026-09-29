ALTER TABLE `excludedcontractaccounts` DROP KEY accountId_UNIQUE;



ALTER TABLE `excludedcontractaccounts` ADD CONSTRAINT accountId_UNIQUE UNIQUE KEY (accountId,contractId,coreCustomerId);



DROP PROCEDURE IF EXISTS `contractaccounts_exists_proc`;

DELIMITER $$

CREATE  PROCEDURE `contractaccounts_exists_proc`(

in _accountsCSV varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)


BEGIN
	
	SELECT `contractaccounts`.`accountId` AS accountId, `contractaccounts`.`coreCustomerId` as coreCustomerId
        FROM `contractaccounts`
        WHERE find_in_set(`contractaccounts`.`accountId`,_accountsCSV); 

END$$
DELIMITER;


DROP PROCEDURE IF EXISTS `excludedcontractaccounts_delete_proc`;

DELIMITER $$

CREATE PROCEDURE `excludedcontractaccounts_delete_proc`(
in _contractId varchar(50)  CHARACTER SET UTF8 COLLATE utf8_general_ci,
in   _coreCustomerId varchar(50)  CHARACTER SET UTF8 COLLATE utf8_general_ci,
in   _accountId varchar(50)  CHARACTER SET UTF8 COLLATE utf8_general_ci

)
BEGIN
	
	DELETE 
      FROM `excludedcontractaccounts`
      WHERE 
        `excludedcontractaccounts`.`contractId` = _contractId COLLATE utf8_general_ci AND 
         `excludedcontractaccounts`.`accountId` = _accountId COLLATE utf8_general_ci AND 
         `excludedcontractaccounts`.`coreCustomerId` = _coreCustomerId COLLATE utf8_general_ci;
         
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `get_valid_customeraccounts_get_proc`;

DELIMITER $$

CREATE  PROCEDURE `get_valid_customeraccounts_get_proc`(
	IN _customeraccountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _customerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _coreCustomerId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @list = (SELECT _customeraccountsCSV);
SET @validcustomeraccountsCSV = '';

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    SET @accountIdList = (SELECT GROUP_CONCAT(`customeraccounts`.`Account_id` SEPARATOR ",") from `customeraccounts` WHERE (`Account_id`=@value) and `Customer_id` = _customerId and  `coreCustomerId` = _coreCustomerId);
    IF ISNULL(@accountIdList)>0 OR @accountIdList="" THEN 
        IF @validcustomeraccountsCSV='' THEN
           SET @validcustomeraccountsCSV = @value;
		ELSE 
           SET @validcustomeraccountsCSV = CONCAT(@validcustomeraccountsCSV,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

select @validcustomeraccountsCSV As validAccounts;
END$$
DELIMITER ;




DROP PROCEDURE IF EXISTS `user_associated_corecustomeraccounts_info`;

DELIMITER $$
$$
CREATE  PROCEDURE `user_associated_corecustomeraccounts_info`(
    in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    
    SET @implictCIF = (SELECT GROUP_CONCAT(`contractcustomers`.`coreCustomerId` SEPARATOR ",")
    FROM `contractcustomers`
    WHERE `contractcustomers`.`customerId` = customerId
    AND `contractcustomers`.`autoSyncAccounts` = '1' );
    
    SELECT `contractcorecustomers`.`coreCustomerId` , `contractcorecustomers`.`contractId`  from 
    `contractcorecustomers` where FIND_IN_SET(`contractcorecustomers`.`coreCustomerId` , @implictCIF) > 0;
    

    IF( @implictCIF) THEN
    
        SELECT `customeraccounts`.`Account_id` AS nonCIFAccounts
        FROM `customeraccounts` 
        WHERE `customeraccounts`.`Customer_id` = customerId
        AND FIND_IN_SET(`customeraccounts`.`coreCustomerId` , @implictCIF) = 0 ;
        
        SELECT `contractaccounts`.`accountId` AS contractaccounts, `contractaccounts`.`coreCustomerId` as contractcustomer
        FROM `contractaccounts`
        WHERE FIND_IN_SET(`contractaccounts`.`coreCustomerId` , @implictCIF) > 0 ; 
        
        SELECT `excludedcontractaccounts`.`accountId` AS excludedcontractaccounts, `excludedcontractaccounts`.`coreCustomerId` as contractcustomer
        FROM `excludedcontractaccounts`
        WHERE FIND_IN_SET(`excludedcontractaccounts`.`coreCustomerId` COLLATE utf8_general_ci , @implictCIF) > 0 ; 
        
        SELECT `customeraccounts`.`Account_id` AS customeraccounts
        FROM `customeraccounts`
        WHERE `customeraccounts`.`Customer_id` = customerId; 
        
        SELECT `excludedcustomeraccounts`.`Account_id` AS excludedcustomeraccounts
        FROM `excludedcustomeraccounts`
        WHERE FIND_IN_SET(`excludedcustomeraccounts`.`coreCustomerId` , @implictCIF) > 0 
        AND `excludedcustomeraccounts`.`Customer_id` = customerId; 
    ELSE
        SELECT `customeraccounts`.`Account_id` AS nonCIFAccounts
        FROM `customeraccounts` 
        WHERE `customeraccounts`.`Customer_id` = customerId;
    END IF;    
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS default_autosync_accounts_create_proc;

DELIMITER $$
$$
CREATE PROCEDURE `default_autosync_accounts_create_proc`(
    IN _queryInput LONGTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    SET SESSION group_concat_max_len = 100000000;
    set @index = 0;
    set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
    insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = CONCAT(SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 ));
            set @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',1), ':', -1 );    
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',2), ':', -1 );
            set @arrangementId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',3), ':', -1 );
            set @accountName = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',4), ':', -1 );
            set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',5), ':', -1 );
            set @ownerType = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',6), ':', -1 );
            set @legalEntityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ':',7), ':', -1 );
            SET @contractId = (select contractcorecustomers.contractId from contractcorecustomers where contractcorecustomers.coreCustomerId = @coreCustomerId);
            SET @typeId = (select accounttype.TypeID from accounttype where accounttype.TypeDescription= @accountType);
            
            SET @contractaccounts = (SELECT contractaccounts.id from contractaccounts where contractaccounts.coreCustomerId = @coreCustomerId
            and contractaccounts.accountId = @accountId and contractaccounts.companyLegalUnit = @legalEntityId);
            if(ISNULL(@contractaccounts)) then
                INSERT INTO contractaccounts(id,contractId,accountId,accountName,typeId,coreCustomerId,ownerType,statusDesc,arrangementId,companyLegalUnit)
                                            VALUES (UUID(),@contractId,@accountId,@accountName,@typeId,@coreCustomerId,@ownerType,'Active',@arrangementId,@legalEntityId);
            end if;
            
            INSERT INTO customeraccounts(id,Customer_id,Account_id,AccountName,contractId,coreCustomerId,accountType,companyLegalUnit)
                                            VALUES (UUID(),_customerId,@accountId,@accountName,@contractId,@coreCustomerId,@accountType,@legalEntityId);
            
            call user_account_default_actions_create_proc(_customerId,@accountId,@coreCustomerId,@contractId,'',@legalEntityId);
           END IF;
    END LOOP insertRecords;
END$$

DELIMITER;


DROP procedure IF EXISTS `user_account_default_actions_create_proc`;

DELIMITER $$
CREATE  PROCEDURE  `user_account_default_actions_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,  
IN _groupId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
        FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci  );
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
			SET featureId = (select featureaction.Feature_id from featureaction where featureaction.id = featureActionId COLLATE utf8_general_ci
		     and featureaction.companyLegalUnit = _legalEntityId  COLLATE utf8_general_ci );
			IF finished = 1 THEN 
				LEAVE accountLevelPermissionsActions;
			else
				SET @id = (SELECT LEFT(UUID(), 50));
			    INSERT IGNORE INTO accountlevelactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId,companyLegalUnit) VALUES
					(@id,_contractId,_coreCustomerId,accountId,featureId,featureActionId,_legalEntityId);
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
        SET @featureId = (SELECT Feature_id FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci  and companyLegalUnit = _legalEntityId  COLLATE utf8_general_ci);
		SET @limitGroupId = (SELECT limitgroupId FROM featureaction WHERE id = featureActionId COLLATE utf8_general_ci  and companyLegalUnit = _legalEntityId  COLLATE utf8_general_ci );
        IF finished = 1 THEN 
            LEAVE getAction;
        else
            OPEN limits;
            getlimit: LOOP
            FETCH limits INTO limitId;
            IF finished = 1 THEN 
               LEAVE getlimit;
            else
               SET @limitvalue = (SELECT cal.value FROM contractactionlimit cal WHERE cal.actionId = featureActionId COLLATE utf8_general_ci
               AND cal.limitTypeId = limitId COLLATE utf8_general_ci AND 
               cal.contractId = _contractId  AND 
               cal.coreCustomerId = _coreCustomerId ) ;
    
               if (limitId='MAX_TRANSACTION_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
				SET @id = (SELECT LEFT(UUID(), 50));
				INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value,companyLegalUnit) VALUES
				(@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00,_legalEntityId);
               	SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00,_legalEntityId);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               	SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
               	INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,0.00,_legalEntityId);
               	SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
              if (limitId!='MIN_TRANSACTION_LIMIT') THEN
               SET @id = (SELECT LEFT(UUID(), 50));
               INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value,companyLegalUnit) VALUES
               (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,actualLimitId,@limitvalue,_legalEntityId);
                
                SET @id = (SELECT LEFT(UUID(), 50));
                INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,contractId,coreCustomerId,featureId,Action_id,Account_id,isAllowed,limitGroupId,LimitType_id,value,companyLegalUnit) VALUES
                (@id,@serviceType,_userId,_contractId,_coreCustomerId,@featureId,featureActionId,accountId,true,@limitGroupId,limitId,@limitvalue,_legalEntityId);
				END IF;
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
END$$

DELIMITER ;



DROP PROCEDURE IF EXISTS `get_validcorecustomerslist_proc`;

DELIMITER $$
CREATE PROCEDURE `get_validcorecustomerslist_proc`(
IN _coreCustomersCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN companyLegalUnit VARCHAR(200) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
	and companyLegalUnit = companyLegalUnit));
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


alter table model add column source varchar(50) not null;
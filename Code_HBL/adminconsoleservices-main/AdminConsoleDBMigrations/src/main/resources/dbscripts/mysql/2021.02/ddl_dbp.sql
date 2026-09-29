CREATE TABLE `excludedcustomeraccounts` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) DEFAULT NULL,
  `Membership_id` varchar(50) DEFAULT NULL,
  `Account_id` varchar(50) DEFAULT NULL,
  `Organization_id` varchar(45) DEFAULT NULL,
  `AccountName` varchar(50) DEFAULT NULL,
  `FavouriteStatus` int(11) NOT NULL DEFAULT '0',
  `IsViewAllowed` tinyint(1) NOT NULL DEFAULT '0',
  `IsDepositAllowed` tinyint(1) NOT NULL DEFAULT '0',
  `IsWithdrawAllowed` tinyint(1) NOT NULL DEFAULT '0',
  `IsOrganizationAccount` tinyint(1) NOT NULL DEFAULT '0',
  `IsOrgAccountUnLinked` tinyint(1) DEFAULT '0',
  `contractId` varchar(20) DEFAULT NULL,
  `coreCustomerId` varchar(20) DEFAULT NULL,
  `isBusinessAccount` varchar(20) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT NULL,
  `lastmodifiedts` timestamp NULL DEFAULT NULL,
  `accountType` varchar(20) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `EStatementmentEnable` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_customerId_accountId` (`Customer_id`,`Account_id`),
  KEY `idx_customeraccounts_Customer_id` (`Customer_id`),
  KEY `idx_customeraccounts_Account_id` (`Account_id`),
  KEY `customeraccounts_IsOrganizationAccount_IDX` (`IsOrganizationAccount`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE contractcustomers ADD autoSyncAccounts BIT DEFAULT 0 NOT NULL;

DROP PROCEDURE IF EXISTS customer_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_contract_delete_proc`(in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
	SET @contract_statement = 'DELETE FROM contractcustomers where';
	SET @accounts_statement = 'DELETE FROM customeraccounts where ';
	SET @excluded_accounts_statement = 'DELETE FROM excludedcustomeraccounts where ';
	SET @group_statement = 'DELETE FROM customergroup where ';
	SET @action_statement = 'DELETE FROM customeraction where ';
	SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
	

	SET @where_clause = '';
	SET @where_clause1 = '';
	if(customerId != '') THEN
		SET @where_clause = CONCAT(@where_clause , '`customerId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(customerId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`Customer_id` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(customerId));
	END IF;
	
	IF(contractId != '') THEN
		IF(@where_clause != '') THEN
			SET @where_clause = CONCAT(@where_clause , ' AND ');
			SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
		END if;
		SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(contractId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
	END IF;
	
	IF(coreCustomerId != '') THEN
		IF(@where_clause != '') THEN
			SET @where_clause = CONCAT(@where_clause , ' AND ');
			SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
		END if;
		SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
	END IF;
	
	IF(@where_clause != '') THEN	
		SET @contract_statement = CONCAT(@contract_statement , @where_clause);	
		SET @accounts_statement = CONCAT(@accounts_statement , @where_clause1);	
		SET @excluded_accounts_statement = CONCAT(@excluded_accounts_statement , @where_clause1);
		SET @group_statement = CONCAT(@group_statement , @where_clause1);	
		SET @action_statement = CONCAT(@action_statement , @where_clause1);	
		SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);	
	
	
		PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @excluded_accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	END if;

END$$
DELIMITER ;

ALTER TABLE `alerthistory` 
DROP FOREIGN KEY `FK_alerthistory_alerttype`,
DROP FOREIGN KEY `FK_alerthistory_alertsubtype`,
DROP FOREIGN KEY `FK_alerthistory_alertcategory`;
ALTER TABLE `alerthistory` 
ADD COLUMN `coreCustomerId` VARCHAR(45) NULL AFTER `Customer_Id`,
DROP INDEX `FK_alerthistory_alertcategory_idx` ,
DROP INDEX `FK_alerthistory_alerttype_idx` ,
DROP INDEX `FK_alerthistory_alertsubtype_idx` ;
;

ALTER TABLE `alerthistory` 
CHANGE COLUMN `EventId` `EventId` VARCHAR(100) NULL ,
CHANGE COLUMN `AlertCategoryId` `AlertCategoryId` VARCHAR(100) NULL ;

ALTER TABLE `alerthistory` 
DROP FOREIGN KEY `FK_alerthistory_customer`;
ALTER TABLE `alerthistory` 
CHANGE COLUMN `Customer_Id` `Customer_Id` VARCHAR(50) NULL ,
DROP INDEX `FK_alerthistory_customer_idx` ;

CREATE TABLE `wealthuserpreferences` (
  `id` varchar(50) NOT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `portfolioId` varchar(50) DEFAULT NULL,
  `fieldOrder` varchar(150) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK1_wealthuserpreferences_userId` (`userId`),
  CONSTRAINT `FK1_wealthuserpreferences_userId` FOREIGN KEY (`userId`) REFERENCES `customer` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `favouriteinstruments`;
CREATE TABLE `favouriteinstruments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` int(11) DEFAULT NULL,
  `customerId` VARCHAR(50) DEFAULT NULL,
  `favInstrumentCodes` VARCHAR(2000) DEFAULT NULL,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY FavoInstruments_UserId (`userId`),
  KEY FavoInstruments_CustomerId (`customerId`),
  CONSTRAINT `FK_FavoInstruments_UserId` FOREIGN KEY (`userId`) REFERENCES `user` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_FavoInstruments_CustomerId` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;


ALTER TABLE `contractcorecustomers` 
ADD COLUMN `implicitAccountAccess` TINYINT(1) NOT NULL DEFAULT '0' AFTER `isBusiness`;


CREATE TABLE `excludedcontractaccounts` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) DEFAULT NULL,
  `accountId` varchar(50) NOT NULL,
  `accountName` varchar(50) DEFAULT NULL,
  `typeId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(50) NOT NULL,
  `ownerType` varchar(50) DEFAULT NULL,
  `statusDesc` varchar(50) DEFAULT 'Active',
  `arrangementId` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedby` varchar(50) DEFAULT NULL,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `accountId_UNIQUE` (`accountId`)
);

DROP PROCEDURE IF EXISTS user_associated_corecustomeraccounts_info;

DELIMITER $$
$$
CREATE PROCEDURE `user_associated_corecustomeraccounts_info`(
    in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    
    SET @implictCIF = (SELECT GROUP_CONCAT(contractcustomers.coreCustomerId SEPARATOR ",")
    FROM contractcustomers
    WHERE contractcustomers.customerId = customerId
    AND contractcustomers.autoSyncAccounts = '1' );
    
    SELECT contractcorecustomers.coreCustomerId , contractcorecustomers.contractId  from 
    contractcorecustomers where FIND_IN_SET(contractcorecustomers.coreCustomerId , @implictCIF) > 0;
    

    IF( @implictCIF) THEN
    
        SELECT customeraccounts.Account_id AS nonCIFAccounts
        FROM customeraccounts 
        WHERE customeraccounts.Customer_id = customerId
        AND FIND_IN_SET(customeraccounts.coreCustomerId , @implictCIF) = 0 ;
        
        SELECT contractaccounts.accountId AS contractaccounts
        FROM contractaccounts
        WHERE FIND_IN_SET(contractaccounts.coreCustomerId , @implictCIF) > 0 ; 
        
        SELECT excludedcontractaccounts.accountId AS excludedcontractaccounts
        FROM excludedcontractaccounts
        WHERE FIND_IN_SET(excludedcontractaccounts.coreCustomerId COLLATE utf8_general_ci , @implictCIF) > 0 ; 
        
        SELECT customeraccounts.Account_id AS customeraccounts
        FROM customeraccounts
        WHERE FIND_IN_SET(customeraccounts.coreCustomerId , @implictCIF) > 0 
        AND customeraccounts.Customer_id = customerId; 
        
        SELECT excludedcustomeraccounts.Account_id AS excludedcustomeraccounts
        FROM excludedcustomeraccounts
        WHERE FIND_IN_SET(excludedcustomeraccounts.coreCustomerId , @implictCIF) > 0 
        AND excludedcustomeraccounts.Customer_id = customerId; 
    ELSE
        SELECT customeraccounts.Account_id AS nonCIFAccounts
        FROM customeraccounts 
        WHERE customeraccounts.Customer_id = customerId;
    END IF;
    
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS user_account_default_actions_create_proc;

DELIMITER $$
$$
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

DECLARE accounts CURSOR
         FOR (SELECT customeraccounts.Account_id FROM customeraccounts WHERE contractId = _contractId AND coreCustomerId = _coreCustomerId 
         AND Customer_id = _userId AND FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList) AND (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true ));
DECLARE limits CURSOR 
        FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci );
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
               SET @limitvalue = (SELECT value FROM contractactionlimit WHERE actionId = featureActionId  COLLATE utf8_general_ci
               AND limitTypeId = limitId COLLATE utf8_general_ci AND contractId = _contractId AND coreCustomerId = _coreCustomerId);
    
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
            SET @contractId = (select contractcorecustomers.contractId from contractcorecustomers where contractcorecustomers.coreCustomerId = @coreCustomerId);
            SET @typeId = (select accounttype.TypeID from accounttype where accounttype.TypeDescription= @accountType);
            
            SET @contractaccounts = (SELECT contractaccounts.id from contractaccounts where contractaccounts.coreCustomerId = @coreCustomerId
            and contractaccounts.accountId = @accountId);
            if(ISNULL(@contractaccounts)) then
                INSERT INTO contractaccounts(id,contractId,accountId,accountName,typeId,coreCustomerId,ownerType,statusDesc,arrangementId)
                                            VALUES (UUID(),@contractId,@accountId,@accountName,@typeId,@coreCustomerId,@ownerType,'Active',@arrangementId);
            end if;
            
            INSERT INTO customeraccounts(id,Customer_id,Account_id,AccountName,contractId,coreCustomerId,accountType)
                                            VALUES (UUID(),_customerId,@accountId,@accountName,@contractId,@coreCustomerId,@accountType);
            
            call user_account_default_actions_create_proc(_customerId,@accountId,@coreCustomerId,@contractId,'');
           END IF;
    END LOOP insertRecords;
END$$

DELIMITER;

DROP PROCEDURE IF EXISTS `user_customers_proc`;

delimiter $$

CREATE PROCEDURE `user_customers_proc`(
    in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    in _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    SET @select_statement = ("(SELECT 
        `contractcustomers`.`customerId` AS `customerId`,
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


ALTER TABLE `alerthistory` 
CHANGE COLUMN `ChannelId` `ChannelId` VARCHAR(100) NULL ;

DROP PROCEDURE IF EXISTS `fetch_default_account_actions_proc`;
delimiter $$

CREATE PROCEDURE `fetch_default_account_actions_proc`(
    IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
    IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    SET SESSION group_concat_max_len = 100000000;

    SET @select_statement = "select group_concat(distinct featureaction.id SEPARATOR ',') as defaultAccountActions from featureaction where 
    (featureaction.isAccountLevel = '1' OR featureaction.isAccountLevel = true) 
    AND featureaction.id IN ";
        
    SET @_contractId = (select contractcorecustomers.contractId from contractcorecustomers where contractcorecustomers.coreCustomerId = _coreCustomerId);
    
    SET @serviceDefinitionId = (SELECT servicedefinitionId from contract WHERE id = @_contractId);
    SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId);
    SET @_groupId = (select customergroup.Group_id from customergroup where customergroup.Customer_id = _userId
                    and customergroup.coreCustomerId = _coreCustomerId);
                            
    SET @select_statement = CONCAT(@select_statement , "(SELECT actionId FROM servicedefinitionactionlimit
                        WHERE serviceDefinitionId= ",QUOTE(@serviceDefinitionId),"
                        AND servicedefinitionactionlimit.actionId IN ");

    SET @select_statement = CONCAT(@select_statement , " ( SELECT Action_id FROM groupactionlimit WHERE groupactionlimit.Group_id = ",QUOTE(@_groupId),"
                                                                AND groupactionlimit.Action_id IN ");

    SET @select_statement = CONCAT(@select_statement ," ( SELECT actionId  FROM contractactionlimit WHERE 
                                contractId = ",QUOTE(@_contractId),"
                                AND coreCustomerId = ",QUOTE(_coreCustomerId));
    SET @select_statement = CONCAT(@select_statement ,")))");
       
    PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER;


DROP procedure IF EXISTS `get_associated_contractaccounts_proc`;

DELIMITER $$
CREATE PROCEDURE `get_associated_contractaccounts_proc`(
IN _accountIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	SET SESSION group_concat_max_len = 1000000;
    
	SET @accountIdList = (SELECT group_concat(accountId SEPARATOR ',') from contractaccounts WHERE FIND_IN_SET(accountId,_accountIdList));
    SET @excludedaccountIdList = (SELECT group_concat(accountId SEPARATOR ',') from excludedcontractaccounts WHERE FIND_IN_SET(accountId COLLATE utf8_general_ci,_accountIdList));

	select @accountIdList As accountIdList;
    select @excludedaccountIdList As excludedaccountIdList;

END$$

DELIMITER ;
;

ALTER TABLE `card`
ADD COLUMN `protectionEnabled` TINYINT(2) NULL AFTER `cardDisplayName`;


DROP VIEW IF EXISTS  `alertcustomerchannels_view_alertcategorylevel`;
CREATE VIEW `alertcustomerchannels_view_alertcategorylevel` AS
    SELECT 
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `dbxcustomeralertentitlement`.`Customer_id` AS `Customer_id`,
        `dbxcustomeralertentitlement`.`AccountId` AS `AccountId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `customeralertchannel`.`channelId` AS `ChannelId`
    FROM
        (((`dbxcustomeralertentitlement`
        JOIN `customeralertchannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertchannel`.`customerId`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertchannel`.`accountId`)
            AND (`dbxcustomeralertentitlement`.`AccountType` = `customeralertchannel`.`accountType`)
            AND (`dbxcustomeralertentitlement`.`alertCategoryId` = `customeralertchannel`.`alertCategoryId`))))
        JOIN `dbxalerttype` ON ((`dbxcustomeralertentitlement`.`AlertTypeId` = `dbxalerttype`.`id`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)));

DROP PROCEDURE IF EXISTS `servicedefinitionactions_get_proc`;

delimiter $$

CREATE PROCEDURE `servicedefinitionactions_get_proc`(
IN _serviceDefinitionId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	select feature.id as featureId, feature.name as featureName , feature.description as featureDescription , 
    feature.Status_id as featureStatus , 
    featureaction.id as actionId , featureaction.name as actionName , featureaction.description as actionDescription,
    featureaction.status as actionStatus
    from feature
    left join featureaction on (featureaction.Feature_id = feature.id)
    where featureaction.id in ( select servicedefinitionactionlimit.actionId
    from servicedefinitionactionlimit where
    servicedefinitionactionlimit.serviceDefinitionId=_serviceDefinitionId);    

END$$

DELIMITER;

ALTER TABLE `contractactionlimit` 
ADD COLUMN `isPortfolio` VARCHAR(45) NULL DEFAULT 'false' AFTER `policyId`,
ADD COLUMN `accountId` VARCHAR(45) NULL AFTER `isPortfolio`;

ALTER TABLE `contractaccounts` 
ADD COLUMN `portfolioId` VARCHAR(45) NULL AFTER `accountId`,
ADD COLUMN `productId` VARCHAR(45) NULL AFTER `typeId`,
ADD COLUMN `portfolioName` VARCHAR(45) NULL AFTER `portfolioId`;

DROP PROCEDURE IF EXISTS `contract_action_limit_save_proc`;

delimiter $$

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
            select @recordsData;
               set @query = concat('INSERT INTO contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$

DELIMITER;

DROP PROCEDURE IF EXISTS `default_contractactions_create_proc`;

delimiter $$

CREATE PROCEDURE `default_contractactions_create_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
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
         FOR (SELECT contractcorecustomers.coreCustomerId FROM contractcorecustomers WHERE contractcorecustomers.contractId = _contractId );
DECLARE accounts CURSOR
         FOR (SELECT contractaccounts.accountId  FROM contractaccounts WHERE contractaccounts.contractId  = _contractId );

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

SET _accountsCSV = (SELECT group_concat(distinct contractaccounts.accountId SEPARATOR ",") FROM contractaccounts WHERE contractaccounts.contractId  = _contractId );

 
SET @serviceDefinitionId = (SELECT servicedefinitionId  from contract WHERE id = _contractId);
SET @serviceType = (SELECT serviceType from servicedefinition WHERE id = @serviceDefinitionId );
                        
SET @validFIActions = (SELECT group_concat(distinct id  SEPARATOR ",") FROM featureaction
								where featureaction.Feature_id  in (select
									contractfeatures.featureId  from contractfeatures
                                    where contractfeatures.contractId  = _contractId ));

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
				   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId,limitTypeId,value) VALUES
							(@id,_contractId,coreCustomerId,featureId,featureActionId,limitId,@limitvalue);
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
					   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId) VALUES
					(@id,_contractId,coreCustomerId,accountId,featureId,featureActionId);
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
				   INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,featureId,actionId) VALUES
				(@id,_contractId,coreCustomerId,featureId,featureActionId);
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



DELIMITER;

DROP PROCEDURE IF EXISTS `contractactionlimit_delete_proc`;

delimiter $$

CREATE PROCEDURE `contractactionlimit_delete_proc`(

IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci

)
BEGIN

delete from contractactionlimit where contractactionlimit.coreCustomerId = _coreCustomerId;
    
END$$

DELIMITER;

DROP PROCEDURE IF EXISTS `user_account_default_actions_create_proc`;

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
 DECLARE featureId varchar(255) DEFAULT "" ;
 DECLARE coreCustomerId varchar(255) DEFAULT "" ;
  
DECLARE accounts CURSOR
         FOR (SELECT customeraccounts.Account_id FROM customeraccounts WHERE contractId = _contractId AND coreCustomerId = _coreCustomerId 
         AND Customer_id = _userId AND FIND_IN_SET(Account_id,_accountsCSV));
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
            select featureActionId;
			SET featureId = (select featureaction.Feature_id from featureaction where featureaction.id = featureActionId COLLATE utf8_general_ci);
            select featureId;
			SET coreCustomerId = (SELECT value FROM contractactionlimit WHERE actionId = featureActionId  COLLATE utf8_general_ci
                AND contractId = _contractId AND coreCustomerId = _coreCustomerId);
			IF finished = 1 THEN 
				LEAVE accountLevelPermissionsActions;
			else
				SET @id = (SELECT LEFT(UUID(), 50));
			    INSERT IGNORE INTO contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId) VALUES
					(@id,_contractId,coreCustomerId,accountId,featureId,featureActionId);
			END IF;
			ITERATE  accountLevelPermissionsActions;
	     END LOOP accountLevelPermissionsActions;
	  CLOSE accountLevelPermissions;
    SET finished = 0;
    ITERATE  getAccount;
    END IF;
END LOOP getAccount;
CLOSE accounts;


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

DELIMITER;

DROP procedure IF EXISTS `dbxdb`.`fetch_accountrestrictive_featureactionlimits_proc`;

DELIMITER $$

CREATE PROCEDURE `fetch_accountrestrictive_featureactionlimits_proc` (
    IN _locale varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _serviceDefinitionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _roleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN

    
    SET @action_select_statement = ("(SELECT featureaction.id AS actionId FROM featureaction LEFT JOIN feature ON ( feature.id = featureaction.Feature_id ) )");
    IF(_serviceDefinitionId != '') THEN 
		SET @select_statement = CONCAT("(SELECT servicedefinitionactionlimit.actionId AS actionId FROM servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = ",quote(_serviceDefinitionId));
		SET @select_statement = CONCAT(@select_statement , " AND servicedefinitionactionlimit.actionId IN" , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ")");
        SET @action_select_statement = @select_statement;
    END IF;	
    IF(_roleId != '') THEN
		SET @select_statement = CONCAT("(SELECT groupactionlimit.Action_id AS actionId FROM groupactionlimit WHERE groupactionlimit.Group_id = ",quote(_roleId));
		SET @select_statement = CONCAT(@select_statement , " AND groupactionlimit.Action_id IN ");
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
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
                                actiondisplaynamedescription.displayName AS actionName,
                                actiondisplaynamedescription.displayDescription AS actionDescription,
                                featureaction.isAccountLevel AS isAccountLevel,
                                featureaction.Type_id AS typeId,
                                featureaction.limitgroupId as limitGroupId,
                                featureaction.accesspolicyId as accessPolicyId,
                                featureaction.actionlevelId as actionLevelId,
								contractactionlimit.accountId as accountId,
                                null as limitTypeId, 
                                null as fiLimitValue";
   
		SET @select_statement = CONCAT(@select_statement , ", null AS serviceLimitValue");
   
	
		SET @select_statement = CONCAT(@select_statement , ", null AS groupLimitValue");
    
		SET @select_statement = CONCAT(@select_statement , ", null AS coreCustomerLimitValue");
    
        SET @select_statement = CONCAT(@select_statement , " FROM feature
															LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
	
		SET @select_statement = CONCAT(@select_statement , "LEFT JOIN contractactionlimit ON (contractactionlimit.actionId = featureaction.id)");
    
	    SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'NON_MONETARY'");
        SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.corecustomerId= ",quote(_coreCustomerId));
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
        SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
        
        SET @select_statement=CONCAT(@select_statement , "UNION ( SELECT 
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
                                featureaction.actionlevelId as actionLevelId,
								contractactionlimit.accountId as accountId,
                                actionlimit.LimitType_id as limitTypeId, 
                                actionlimit.value as fiLimitValue");
                                
	    SET @select_statement = CONCAT(@select_statement , ", servicedefinitionactionlimit.value AS serviceLimitValue");
    
        SET @select_statement = CONCAT(@select_statement , ", groupactionlimit.value AS groupLimitValue");
 
        SET @select_statement = CONCAT(@select_statement , ", contractactionlimit.value AS coreCustomerLimitValue");

	    SET @select_statement = CONCAT(@select_statement , " FROM feature
														LEFT JOIN featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN featureaction ON (featureaction.Feature_id = feature.id)
                                                         LEFT JOIN actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)");
    	
		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)");
																										
                                                                                                        

		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND groupactionlimit.LimitType_id = actionlimit.LimitType_id)");
																										

		SET @select_statement = CONCAT(@select_statement , " LEFT JOIN contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND contractactionlimit.limitTypeId = actionlimit.LimitType_id)");
		SET @select_statement = CONCAT(@select_statement , " WHERE featureaction.Type_id = 'MONETARY'");
    	SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.corecustomerId= ",quote(_coreCustomerId));
		SET @select_statement = CONCAT(@select_statement , " AND contractactionlimit.actionId IN " , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , " AND featuredisplaynamedescription.Locale_id = ",QUOTE(_locale));
        SET @select_statement = CONCAT(@select_statement , " AND actiondisplaynamedescription.Locale_id = ",QUOTE(_locale) ,")");
		
	

      PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
     


END$$

DELIMITER;




DROP TABLE IF EXISTS `excludedcustomroleaccounts`;

CREATE TABLE `excludedcustomroleaccounts` (
`id` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
`customRoleId` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`Account_id` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`AccountName` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`accountType` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`contractId` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`coreCustomerId` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
`createdby` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
`modifiedby` varchar(45) COLLATE utf8_unicode_ci DEFAULT NULL,
`createdts` timestamp NULL DEFAULT NULL,
`lastmodifiedts` timestamp NULL DEFAULT NULL,
`synctimestamp` timestamp NULL DEFAULT NULL,
PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
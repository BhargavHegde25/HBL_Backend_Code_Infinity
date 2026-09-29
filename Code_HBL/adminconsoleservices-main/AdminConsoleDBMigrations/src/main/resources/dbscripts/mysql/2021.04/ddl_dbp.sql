-- ALTER TABLE `card` 
-- ADD COLUMN `protectionEnabled` TINYINT(1) NULL DEFAULT 0 AFTER `cardDisplayName`;

CREATE TABLE `suspendedcustomers` (
  `id` varchar(50) NOT NULL,
  `contractId` varchar(50) DEFAULT NULL,
  `customerId` varchar(50) NOT NULL,
  `coreCustomerId` varchar(45) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT NULL,
  `lastmodifiedts` timestamp NULL DEFAULT NULL,
  `synctimestamp` timestamp NULL DEFAULT NULL,
  `softdeleteflag` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `suspendedcustomers_un` (`contractId`,`customerId`,`coreCustomerId`),
  KEY `suspendedcustomers_fk` (`customerId`),
  CONSTRAINT `suspendedcustomers_fk` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS customer_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customer_contract_delete_proc`(in customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
	SET @contract_statement = 'DELETE FROM contractcustomers where';
	SET @suspended_statement = 'DELETE FROM suspendedcustomers where';
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
		SET @suspended_statement = CONCAT(@suspended_statement , @where_clause);
		SET @accounts_statement = CONCAT(@accounts_statement , @where_clause1);	
		SET @excluded_accounts_statement = CONCAT(@excluded_accounts_statement , @where_clause1);
		SET @group_statement = CONCAT(@group_statement , @where_clause1);	
		SET @action_statement = CONCAT(@action_statement , @where_clause1);	
		SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause1);	
	
	
		PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @suspended_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @excluded_accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @group_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	END if;

END$$
DELIMITER ;


DROP VIEW IF EXISTS  `alerts_fetch_globaldata_view_alertcategorylevel`;
CREATE VIEW `alerts_fetch_globaldata_view_alertcategorylevel` AS
    SELECT
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertcategorychannel`.`ChannelID` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`
    FROM
        (((`dbxalertcategory`
        JOIN `alertcategorychannel` ON ((`alertcategorychannel`.`AlertCategoryId` = `dbxalertcategory`.`id`)))
        JOIN `dbxalerttype` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)))
        JOIN `alertsubtype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)));
       
       
DROP VIEW IF EXISTS  `alerts_fetch_globaldata_view_alertgrouplevel`;       
CREATE VIEW `alerts_fetch_globaldata_view_alertgrouplevel` AS
    SELECT
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alerttypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`
    FROM
        (((`dbxalerttype`
        JOIN `alerttypechannel` ON ((`dbxalerttype`.`id` = `alerttypechannel`.`alertTypeId`)))
        JOIN `alertsubtype` ON ((`dbxalerttype`.`id` = `alertsubtype`.`AlertTypeId`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)));
       
       
DROP VIEW IF EXISTS  `alerts_fetch_globaldata_view_alertlevel`;
CREATE VIEW `alerts_fetch_globaldata_view_alertlevel` AS
    SELECT
        `dbxalerttype`.`id` AS `AlertTypeId`,
        `dbxalerttype`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`Name` AS `alertGroupName`,
        `alertsubtype`.`attributeId` AS `AttributeId`,
        `alertsubtype`.`alertConditionId` AS `AlertConditionId`,
        `alertsubtype`.`recipienttype` AS `recipienttype`,
        `alertsubtype`.`value1` AS `Value1`,
        `alertsubtype`.`value2` AS `Value2`,
        `alertsubtype`.`Name` AS `alertName`,
        `dbxalerttype`.`Status_id` AS `alerttype_status_id`,
        `alertsubtype`.`isGlobal` AS `IsGlobal`,
        `dbxalertcategory`.`status_id` AS `alertcategory_status_id`,
        `alertsubtypechannel`.`channelId` AS `ChannelId`,
        `alertsubtype`.`id` AS `AlertSubTypeId`,
        `alertsubtype`.`Status_id` AS `alertsubtypetype_status_id`,
        `alertsubtype`.`isAccountLevel` AS `accountLevel`,
        `alertsubtype`.`externalSystem` AS `externalSystem`
    FROM
        (((`alertsubtype`
        JOIN `alertsubtypechannel` ON ((`alertsubtype`.`id` = `alertsubtypechannel`.`alertSubTypeId`)))
        JOIN `dbxalerttype` ON ((`alertsubtype`.`AlertTypeId` = `dbxalerttype`.`id`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)));
		
ALTER TABLE `alertmessagetypeconfig` ADD COLUMN `passVariableData` BOOLEAN NULL DEFAULT 0 , ADD COLUMN `subject` VARCHAR(255);

DROP procedure IF EXISTS `contract_users_details_get_proc`;

DELIMITER $$
CREATE PROCEDURE `contract_users_details_get_proc`(
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _backendType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @customers = (SELECT group_concat(DISTINCT customerId SEPARATOR ",") from contractcustomers WHERE contractId = _contractId);

SELECT distinct customer.id AS customerId,FirstName AS firstName,MiddleName AS middleName, 
 LastName AS lastName, UserName AS userName, Status_id AS statusId, DateOfBirth AS dateOfBirth , Ssn AS Ssn ,
 backendidentifier.BackendId AS primaryCoreCustomerId,
 Value AS Email 
 FROM customer 
 LEFT JOIN customercommunication ON (customer.id = customercommunication.Customer_id AND customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = '1') 
 LEFT JOIN backendidentifier ON (backendidentifier.Customer_id = customer.id AND backendidentifier.BackendType = _backendType)
 WHERE FIND_IN_SET(customer.id ,@customers) group by customer.id ;
END$$

DELIMITER ;
;

ALTER TABLE `favouriteinstruments` 
ADD COLUMN `favInstrumentIds` VARCHAR(2000) NULL AFTER `customerId`;


DROP PROCEDURE IF EXISTS `dbxcustomeralertentitlement_insertbulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `dbxcustomeralertentitlement_insertbulk_proc`(
IN _recordvalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
	
	DECLARE EXIT HANDLER for SQLEXCEPTION
     BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
     END;

	IF  _recordvalues is not null AND _recordvalues !='' THEN
	         SET @query =  CONCAT('INSERT INTO dbxcustomeralertentitlement(`Customer_id`,`alertCategoryId`,`AlertTypeId`,`alertSubTypeId`,`AccountId`,`AccountType`,`Value1`,`Value2`,`alertRequestId`,`createdby`) VALUES ',_recordvalues,';');
	         PREPARE sql_query FROM @query; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;
	End IF ;

end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `dbxcustomeralertentitlement_updatebulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `dbxcustomeralertentitlement_updatebulk_proc`( 
IN _updateRecords TEXT)
begin
	
 DECLARE index1 INTEGER DEFAULT 0;	
 DECLARE EXIT HANDLER for SQLEXCEPTION
    BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
   END;	
 
    set @numOfRecords = LENGTH(_updateRecords) - LENGTH(REPLACE(_updateRecords, '|', ''));
  updateRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE updateRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_updateRecords, '|', index1), '|', -1 );
            set @numOfParams = LENGTH(@rowValues) - LENGTH(REPLACE(@rowValues, ',', '')) ;
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
			set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );
		    set @alertRequestId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',7), ',\"', -1 );
		  
            set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		
           	SET @query = CONCAT('update `dbxcustomeralertentitlement` set  `modifiedby` =', quote(@modifiedby));
           
           IF STRCMP(@alertRequestId, 'null') != 0 or STRCMP(@alertRequestId, 'NULL') != 0 Then  
                   SET @query = CONCAT( @query,', `alertRequestId` = ', quote(@alertRequestId));	
			END IF;

           
           IF @numOfParams > 7 THEN 
              set @value1 = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );
              SET @query = CONCAT( @query,', `Value1` = ', quote(@value1));	
			END IF;
		
		   IF @numOfParams > 8 THEN
		   set @value2 = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',9), ',\"', -1 );
		   SET @query = CONCAT(@query,', `Value2` = ', quote(@value2));
		   END IF;
			
		  SET @whereCondition =  CONCAT(' where `Customer_id` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `AlertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `AccountId` = ',quote(@accountId),'  and `AccountType` = ',quote(@accountType),' ;');		
		  SET @query = CONCAT(@query,'  ' , @whereCondition);
		  
		
          PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
			 				 
        END if;       
    END LOOP updateRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `dbxcustomeralertentitlement_deletebulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `dbxcustomeralertentitlement_deletebulk_proc`( 
IN _deleteRecords TEXT )
BEGIN
  DECLARE index1 INTEGER DEFAULT 0;
 	
  DECLARE EXIT HANDLER for SQLEXCEPTION
    BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
   END;	
 
    set @numOfRecords = LENGTH(_deleteRecords) - LENGTH(REPLACE(_deleteRecords, '|', '')) ;
  deleteRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE deleteRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_deleteRecords, '|', index1), '|', -1 );
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
		    set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		                   			
		  SET @query  =  CONCAT('delete from `dbxcustomeralertentitlement` where ' ,'`Customer_id` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `AlertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `AccountId` = ',quote(@accountId),'  and `AccountType` = ',quote(@accountType),' ;');		
	
		 PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;	  
			 
        END if;       
    END LOOP deleteRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customeralertchannel_insertbulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `customeralertchannel_insertbulk_proc`(
IN _recordvalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
	
	DECLARE EXIT HANDLER for SQLEXCEPTION
     BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
     END;

	IF  _recordvalues is not null AND _recordvalues !='' THEN
	  SET @query =  CONCAT('insert into  `customeralertchannel` ( `customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `accountId`, `accountType`,`channelId`, `createdby`) values  ',_recordvalues,';');	       
	  PREPARE sql_query FROM @query; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;
    End IF ;
	   
end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customeralertchannel_updatebulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `customeralertchannel_updatebulk_proc`( 
IN _updateRecords TEXT)
begin
	
 DECLARE index1 INTEGER DEFAULT 0;	
  
    set @numOfRecords = LENGTH(_updateRecords) - LENGTH(REPLACE(_updateRecords, '|', ''));
  updateRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE updateRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_updateRecords, '|', index1), '|', -1 );
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
			set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );
		    set @channelId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',7), ',\"', -1 );		  
            set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			           
           	SET @queryu = CONCAT('update `customeralertchannel` set  `modifiedby` =', quote(@modifiedby),' where `customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),'  and `channelId` = ',quote(@channelId),' ;');		
		     PREPARE stmt FROM @queryu; EXECUTE stmt; DEALLOCATE PREPARE stmt;
					 
        END if;       
    END LOOP updateRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customeralertchannel_deletebulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `customeralertchannel_deletebulk_proc`(
IN _deleteRecords TEXT )
begin
	
  DECLARE index1 INTEGER DEFAULT 0;
 	
  DECLARE EXIT HANDLER for SQLEXCEPTION
    BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
   END;	
    set @numOfRecords = LENGTH(_deleteRecords) - LENGTH(REPLACE(_deleteRecords, '|', '')) ;
    deleteRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE deleteRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_deleteRecords, '|', index1), '|', -1 );
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
            set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );
		    set @channelId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		                   			
		  SET @query  =  CONCAT('delete from `customeralertchannel` where ' ,'`customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),'  and `channelId` = ',quote(@channelId),' ;');		
	
		 PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;	  			 
        END if;       
    END LOOP deleteRecords;
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS customeralertfrequency_insertbulk_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customeralertfrequency_insertbulk_proc`(
IN _recordvalues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
begin
	
	DECLARE EXIT HANDLER for SQLEXCEPTION
     BEGIN
      GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
      SELECT @text as errmsg;
     END;

	IF  _recordvalues is not null AND _recordvalues !='' THEN
	 SET @query =  CONCAT( 'INSERT INTO `customeralertfrequency` (`customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `accountId`, `accountType`,`alertFrequencyId`, `frequencyValue`, `frequencyTime`, `createdby`) values  ',_recordvalues,';');    	         
    select @query;
	PREPARE sql_query FROM @query; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;	
	End IF;	   
end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `customeralertfrequency_updatebulk_proc`;

DELIMITER $$
$$
CREATE PROCEDURE `customeralertfrequency_updatebulk_proc`(
IN _updateRecords TEXT)
begin
	
 DECLARE index1 INTEGER DEFAULT 0;	
  
    set @numOfRecords = LENGTH(_updateRecords) - LENGTH(REPLACE(_updateRecords, '|', ''));
  updateRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE updateRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_updateRecords, '|', index1), '|', -1 );
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
			set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );
		    set @alertFrequencyId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',7), ',\"', -1 );		  
		    set @frequencyValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );	
		    set @frequencyTime = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',9), ',\"', -1 );	
            set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			
            IF STRCMP(@frequencyValue, 'null') != 0 or STRCMP(@frequencyValue, 'NULL') != 0 Then  
                   SET @frequencyValue = quote(@frequencyValue);	
		    END IF;  
		   
		   IF STRCMP(@frequencyTime, 'null') != 0 or STRCMP(@frequencyTime, 'NULL') != 0 Then  
                   SET @frequencyTime = quote(@frequencyTime);	
		    END IF;
           
           	SET @queryu = CONCAT('UPDATE `customeralertfrequency` set  `modifiedby` =', quote(@modifiedby),',`alertFrequencyId` = ',quote(@alertFrequencyId),',`frequencyValue`=',@frequencyValue,',`frequencyTime`=',@frequencyTime,' where `customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),' ;');		
			PREPARE sql_query FROM @queryu; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;	
          
				 
        END if;       
    END LOOP updateRecords;
end$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `customeralertfrequency_deletebulk_proc`;

DELIMITER $$
$$

CREATE PROCEDURE `customeralertfrequency_deletebulk_proc`( 
IN _deleteRecords TEXT)
begin
	
 DECLARE index1 INTEGER DEFAULT 0;	
  
    set @numOfRecords = LENGTH(_deleteRecords) - LENGTH(REPLACE(_deleteRecords, '|', ''));
  deleteRecords : LOOP
     set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE deleteRecords;
        else
            set @rowValues = SUBSTRING_INDEX(SUBSTRING_INDEX(_deleteRecords, '|', index1), '|', -1 );
            set @customer_id = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',1 ), '\"', -1 );
            set @alertCategoryId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',2), ',\"', -1 );
            set @alertTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',3), ',\"', -1 );
            set @alertSubTypeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',4), ',\"', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',5), ',\"', -1 );
			set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			
            SET @query = CONCAT('delete from `customeralertfrequency` where ' ,'`customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),' ;');		
			PREPARE sql_query FROM @query; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;
				 
        END if;       
    END LOOP deleteRecords;
end$$
DELIMITER ;

ALTER TABLE `backendcertificate` ADD COLUMN `PublicKeyServiceURL` LONGTEXT AFTER `CertPublicKey`,ADD COLUMN `CertificateEncryptionKey` LONGTEXT AFTER `CertPublicKey`,ADD COLUMN JWSAlgorithm VARCHAR(100) AFTER `CertPublicKey`;

DROP PROCEDURE IF EXISTS customrole_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customrole_contract_delete_proc`(in customRoleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
	SET @contract_statement = 'DELETE FROM contractcustomrole where';
	SET @accounts_statement = 'DELETE FROM customroleaccounts where ';
	SET @excludedaccounts_statement = 'DELETE FROM excludedcustomroleaccounts where ';
	SET @action_statement = 'DELETE FROM customroleactionlimits where ';
	SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
	
	SET @where_clause = '';
	SET @where_clause1 = '';
	SET @where_clause2 = '';
	if(customRoleId != '') THEN
		SET @where_clause = CONCAT(@where_clause , '`customRoleId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(customRoleId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`customRole_id` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(customRoleId));
		SET @where_clause2 = CONCAT(@where_clause2 , '`Customer_id` = ');
		SET @where_clause2 = CONCAT(@where_clause2 , quote(customRoleId));
	
	END IF;
	
	IF(contractId != '') THEN
		IF(@where_clause != '') THEN
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
	END IF;
	
	IF(coreCustomerId != '') THEN
		IF(@where_clause != '') THEN
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
	 
	IF(@where_clause != '') THEN	
		SET @contract_statement = CONCAT(@contract_statement , @where_clause);	
		SET @accounts_statement = CONCAT(@accounts_statement , @where_clause);
		SET @excludedaccounts_statement = CONCAT(@excludedaccounts_statement , @where_clause);
		SET @action_statement = CONCAT(@action_statement , @where_clause1);	
		SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause2);	
	
		PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @excludedaccounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	END if;

END$$
DELIMITER ;


ALTER TABLE customeraccounts
ADD NickName VARCHAR(50) NULL DEFAULT NULL;

DROP PROCEDURE IF EXISTS getAccountNicknamesByAccountIds;
DELIMITER &&
CREATE PROCEDURE `getAccountNicknamesByAccountIds`(
	IN `_accountIds` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_customerid` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SELECT Account_id,NickName FROM customeraccounts WHERE Customer_id=_customerid and FIND_IN_SET(Account_id, _accountIds) COLLATE utf8_general_ci;
END &&
DELIMITER ;

DROP procedure IF EXISTS `verify_user_proc`;

DELIMITER $$
CREATE PROCEDURE `verify_user_proc`(
in _phone varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _email varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _dateOfBirth varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendIdentifiers varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _backendType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @usersList = (select group_concat(backendidentifier.Customer_id SEPARATOR ",") from backendidentifier where BackendType=_backendType  
									 AND FIND_IN_SET(backendidentifier.BackendId ,_backendIdentifiers) );

(SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`
    FROM
        (`customer`
        LEFT JOIN `customercommunication` `primaryphone` ON ((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`Value` = _phone)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))
        LEFT JOIN `customercommunication` `primaryemail` ON ((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`Value` = _email)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL')))
	where
		`customer`.`DateOfBirth` = _dateOfBirth
		and `primaryphone`.`Customer_id` = `primaryemail`.`Customer_id`) union SELECT 
        `customer`.`id` AS `id`,
        `customer`.`FirstName` AS `FirstName`,
        `customer`.`MiddleName` AS `MiddleName`,
        `customer`.`LastName` AS `LastName`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`Gender` AS `Gender`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`Ssn` AS `Ssn`,
        `customer`.`Status_id` AS `Status_id`
    FROM `customer` where FIND_IN_SET(customer.id ,@usersList);
	
END$$

DELIMITER ;
;

DROP PROCEDURE IF EXISTS fetch_bulkwiretemplatelineitems_proc;
DELIMITER &&
CREATE PROCEDURE `fetch_bulkwiretemplatelineitems_proc`(
	IN `bulkWireTemplateID` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `searchString` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortByParam` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortOrder` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `groupBy` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE lineItemId,payeeId,swiftCodeVal,intRoutingNumVal,recAccntnumVal,routingNumVal,recNicknameVal VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci;
DECLARE recNameVal,recAddLine1Val,recAddLine2Val,recCityVal,recStateVal,recCountryVal,recBankNameVal,recBankAdd1Val,recBankAdd2Val,recBankCityVal,recBankStateVal  VARCHAR(100) CHARACTER SET UTF8 COLLATE utf8_general_ci;
DECLARE recZipVal,recBankZipVal VARCHAR(20) CHARACTER SET UTF8 COLLATE utf8_general_ci;
DECLARE isPayeeDeleted INTEGER DEFAULT 1;
DECLARE finished INTEGER DEFAULT 0;

DECLARE curTemplateLineItem 
		CURSOR FOR 
			SELECT bulkWireTemplateLineItemID FROM bulkwiretemplatelineitems
            WHERE bulkWireTemplateID = bulkWireTemplateID AND softdeleteflag = 0 AND templateRecipientCategory = "EXISTINGRECIPIENT";

	
	DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

	OPEN curTemplateLineItem;

	getLineItem: LOOP
		FETCH curTemplateLineItem INTO lineItemId;
		IF finished = 1 THEN 
			LEAVE getLineItem;
		END IF;
	    SELECT bulkwiretemplatelineitems.payeeId INTO payeeId FROM bulkwiretemplatelineitems WHERE bulkwiretemplatelineitems.bulkWireTemplateLineItemID = lineItemId;
		SELECT payee.softDelete INTO isPayeeDeleted FROM payee WHERE payee.Id = payeeId;
		IF isPayeeDeleted = 0 THEN
        SELECT payee.swiftCode,payee.internationalRoutingCode,payee.name,payee.addressLine1,payee.addressLine2,payee.cityName,payee.state,payee.country,payee.zipCode,payee.bankName,payee.bankAddressLine1,payee.bankAddressLine2,payee.bankZip,payee.bankCity,payee.bankState,payee.nickName,payee.accountNumber,payee.routingCode INTO swiftCodeVal,intRoutingNumVal,recNameVal,recAddLine1Val,recAddLine2Val,recCityVal,recStateVal,recCountryVal,recZipVal,recBankNameVal,recBankAdd1Val,recBankAdd2Val,recBankZipVal,recBankCityVal,recBankStateVal,recNicknameVal,recAccntnumVal,routingNumVal FROM payee where payee.Id = payeeId;
		UPDATE bulkwiretemplatelineitems
        SET swiftCode = swiftCodeVal,internationalRoutingNumber = intRoutingNumVal,recipientName = recNameVal,recipientAddressLine1 = recAddLine1Val,recipientAddressLine2  = recAddLine2Val,
            recipientCity =recCityVal,recipientState = recStateVal,recipientCountryName = recCountryVal,recipientZipCode = recZipVal,recipientBankName = recBankNameVal,recipientBankAddress1 = recBankAdd1Val,
            recipientBankAddress2 = recBankAdd2Val,recipientBankZipCode = recBankZipVal,recipientBankcity = recBankCityVal,recipientBankstate = recBankStateVal,accountNickname = recNicknameVal,
			recipientAccountNumber= recAccntnumVal,routingNumber = routingNumVal
        WHERE bulkWireTemplateLineItemID = lineItemId;
        ELSE
        UPDATE bulkwiretemplatelineitems
        SET softdeleteflag = 1 WHERE bulkWireTemplateLineItemID = lineItemId;
        END IF;
	END LOOP getLineItem;
	CLOSE curTemplateLineItem;
   
SET @queryFilter = concat("`bulkwiretemplatelineitems`.`bulkWireTemplateID` = \"", bulkWireTemplateID, "\" AND `bulkwiretemplatelineitems`.`softdeleteflag` = 0 ");
SET sortByParam = if (sortByParam = "" OR sortByParam = NULL, 'recipientName', sortByParam);
SET sortOrder = if (sortOrder = "" OR sortOrder = NULL, 'ASC', sortOrder);
SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));   

SET @orderByWithGrouping = concat(" ORDER BY ",groupBy,",",sortByParam," ",sortOrder);
SET @orderByWithoutGrouping = concat(" ORDER BY ",sortByParam," ",sortOrder);
SET @orderBy = if(groupBy = "" OR groupBy = NULL, @orderByWithoutGrouping, @orderByWithGrouping);
SET @searchQuery = concat("(`bulkwiretemplatelineitems`.`recipientName` LIKE ",searchString," OR `bulkwiretemplatelineitems`.`swiftCode` LIKE ",searchString," OR `bulkwiretemplatelineitems`.`recipientAccountNumber` LIKE ",searchString," OR `bulkwiretemplatelineitems`.`routingNumber` LIKE ",searchString," OR `bulkwiretemplatelineitems`.`internationalRoutingNumber` LIKE ",searchString," OR
`bulkwiretemplatelineitems`.`bulkWireTransferType` LIKE ",searchString,"  OR 
`bulkwiretemplatelineitems`.`transactionType` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientAddressLine1` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientAddressLine2` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientCity` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientState` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientZipCode` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientBankName` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientBankAddress1` LIKE ",searchString,"  OR
`bulkwiretemplatelineitems`.`recipientBankAddress2` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientBankcity` LIKE ",searchString,"  OR
`bulkwiretemplatelineitems`.`recipientBankZipCode` LIKE ",searchString,"  OR `bulkwiretemplatelineitems`.`recipientBankstate` LIKE ",searchString,")");
	
SET @defaultFilter = concat(@queryFilter, @orderBy);

SET @searchFilter = concat(@queryFilter," AND ",@searchQuery,@orderBy);

SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

SET @select_statement = concat("SELECT 
bulkwiretemplatelineitems.bulkWireTemplateLineItemID,
bulkwiretemplatelineitems.swiftCode,
bulkwiretemplatelineitems.recipientCountryName,
bulkwiretemplatelineitems.recipientName,
bulkwiretemplatelineitems.bulkWireTransferType,
bulkwiretemplatelineitems.transactionType,
bulkwiretemplatelineitems.internationalRoutingNumber,
bulkwiretemplatelineitems.recipientAddressLine1,
bulkwiretemplatelineitems.recipientAddressLine2,
bulkwiretemplatelineitems.recipientCity,
bulkwiretemplatelineitems.recipientState,
bulkwiretemplatelineitems.recipientZipCode,
bulkwiretemplatelineitems.recipientBankName,
bulkwiretemplatelineitems.recipientBankAddress1,
bulkwiretemplatelineitems.recipientBankAddress2,
bulkwiretemplatelineitems.recipientBankZipCode,
bulkwiretemplatelineitems.recipientBankcity,
bulkwiretemplatelineitems.recipientBankstate,
bulkwiretemplatelineitems.recipientAccountNumber,
bulkwiretemplatelineitems.routingNumber,
bulkwiretemplatelineitems.templateRecipientCategory,
bulkwiretemplatelineitems.accountNickname,
bulkwiretemplatelineitems.payeeId
FROM `bulkwiretemplatelineitems` WHERE ", @filter);

PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;

END &&
DELIMITER ;

DROP TABLE IF EXISTS `favouriteinstruments`;
CREATE TABLE `favouriteinstruments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `userId` VARCHAR(50) DEFAULT NULL,
  `customerId` VARCHAR(50) DEFAULT NULL,
  `favInstrumentIds` VARCHAR(2000) DEFAULT NULL,
  `favInstrumentCodes` VARCHAR(2000) DEFAULT NULL,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY FavoInstruments_UserId (`userId`),
  CONSTRAINT `FK_FavoInstruments_UserId` FOREIGN KEY (`userId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;


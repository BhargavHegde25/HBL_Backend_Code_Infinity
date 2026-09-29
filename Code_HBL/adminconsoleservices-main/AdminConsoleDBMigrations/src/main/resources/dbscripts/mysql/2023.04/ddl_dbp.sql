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
	  SET @query =  CONCAT('insert into  `customeralertchannel` ( `customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `accountId`, `accountType`,`channelId`, `createdby`, `companyLegalUnit`) values  ',_recordvalues,';');	       
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
		    set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );		
            set @companyLegalUnit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			           
           	SET @queryu = CONCAT('update `customeralertchannel` set  `modifiedby` =', quote(@modifiedby),' where `customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `companyLegalUnit` = ',quote(@companyLegalUnit),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),'  and `channelId` = ',quote(@channelId),' ;');		
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
            set @channelId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',7), ',\"', -1 );
            set @createdby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );
		    set @companyLegalUnit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		                   			
		  SET @query  =  CONCAT('delete from `customeralertchannel` where ' ,'`customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `companyLegalUnit` = ',quote(@companyLegalUnit),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),'  and `channelId` = ',quote(@channelId),' ;');		
	
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
	 SET @query =  CONCAT( 'INSERT INTO `customeralertfrequency` (`customerId`, `alertCategoryId`, `alertTypeId`, `alertSubTypeId`, `accountId`, `accountType`,`alertFrequencyId`, `frequencyValue`, `frequencyTime`, `createdby`, `companyLegalUnit`) values  ',_recordvalues,';');    	         
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
		    set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',10), ',\"', -1 );
            set @companylegalunit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			
            IF STRCMP(@frequencyValue, 'null') != 0 or STRCMP(@frequencyValue, 'NULL') != 0 Then  
                   SET @frequencyValue = quote(@frequencyValue);	
		    END IF;  
		   
		   IF STRCMP(@frequencyTime, 'null') != 0 or STRCMP(@frequencyTime, 'NULL') != 0 Then  
                   SET @frequencyTime = quote(@frequencyTime);	
		    END IF;
           
           	SET @queryu = CONCAT('UPDATE `customeralertfrequency` set  `modifiedby` =', quote(@modifiedby),',`alertFrequencyId` = ',quote(@alertFrequencyId),',`frequencyValue`=',@frequencyValue,',`frequencyTime`=',@frequencyTime,' where `customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `companyLegalUnit` = ',quote(@companylegalunit),'  and `accountType` = ',quote(@accountType),' ;');		
			PREPARE sql_query FROM @queryu; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;	
          
				 
        END if;       
    END LOOP updateRecords;
END$$
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
			set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );
		    set @alertFrequencyId = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',7), ',\"', -1 );		  
		    set @frequencyValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );	
		    set @frequencyTime = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',9), ',\"', -1 );	
		    set @modifiedby = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',10), ',\"', -1 );
            set @companylegalunit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
			
            SET @query = CONCAT('delete from `customeralertfrequency` where ' ,'`customerId` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `companyLegalUnit` = ',quote(@companylegalunit),'  and `alertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `accountId` = ',quote(@accountId),'  and `accountType` = ',quote(@accountType),' ;');		
			PREPARE sql_query FROM @query; EXECUTE sql_query; DEALLOCATE PREPARE sql_query;
				 
        END if;       
    END LOOP deleteRecords;
end$$
DELIMITER ;

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
	         SET @query =  CONCAT('INSERT INTO dbxcustomeralertentitlement(`Customer_id`,`alertCategoryId`,`AlertTypeId`,`alertSubTypeId`,`AccountId`,`AccountType`,`Value1`,`Value2`,`alertRequestId`,`createdby`,`companyLegalUnit`) VALUES ',_recordvalues,';');
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
		    set @value1 = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',8), ',\"', -1 );
		    set @value2 = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',9), ',\"', -1 );
		    set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',10), ',\"', -1 );
            set @companylegalunit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		
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
			
		  SET @whereCondition =  CONCAT(' where `Customer_id` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `companyLegalUnit` = ',quote(@companylegalunit),'  and `AlertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `AccountId` = ',quote(@accountId),'  and `AccountType` = ',quote(@accountType),' ;');		
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
            set @accountType = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, '\",',6), ',\"', -1 );

		    set @companylegalunit = SUBSTRING_INDEX(SUBSTRING_INDEX(@rowValues, ',\"',-1 ), '\"', 1 );
		                   			
		  SET @query  =  CONCAT('delete from `dbxcustomeralertentitlement` where ' ,'`Customer_id` = ',quote(@customer_id),'  and `alertCategoryId` = ',quote(@alertCategoryId),'  and `companyLegalUnit` = ',quote(@companylegalunit),'  and `AlertTypeId` = ',quote(@alertTypeId),'  and `alertSubTypeId` = ',quote(@alertSubTypeId),'  and `AccountId` = ',quote(@accountId),'  and `AccountType` = ',quote(@accountType),' ;');		
	
		 PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;	  
			 
        END if;       
    END LOOP deleteRecords;
END$$
DELIMITER ;

DROP procedure IF EXISTS `subscriber_getEntitleMentsNocustomer`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getEntitleMentsNocustomer`(alerttypes TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2,companyLegalUnit as companyLegalUnit FROM dbxcustomeralertentitlement where   FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId`,alerttypes)  ;
END$$
DELIMITER ;

DROP procedure IF EXISTS `subscriber_getEntitleMentsWithcustomer`;

DELIMITER $$
CREATE  PROCEDURE `subscriber_getEntitleMentsWithcustomer`(alerttypes TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,custids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2,companyLegalUnit as companyLegalUnit FROM dbxcustomeralertentitlement where   FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId`,alerttypes)  and
   FIND_IN_SET(`dbxcustomeralertentitlement`.`Customer_id`,custids);
END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `contracts_with_activeaccounts`;

DELIMITER $$

create PROCEDURE `contracts_with_activeaccounts`(
IN _contractIdList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin

DECLARE FINISHED INTEGER DEFAULT 0;
DECLARE contractIds varchar(255) DEFAULT "" ;
DECLARE statusPoint VARCHAR(255) DEFAULT "";
DECLARE statuses CURSOR 
		FOR (select `accountId` from `contractaccounts` where FIND_IN_SET(contractId,_contractIdList));
DECLARE CONTINUE HANDLER
        FOR NOT FOUND SET FINISHED = 1;

OPEN statuses;
getStatus: LOOP
FETCH statuses INTO statusPoint;

IF FINISHED = 1 then
	LEAVE getStatus;
ELSE
	set @contracts = (select contractId from contractaccounts where accountId = statusPoint and statusDesc != 'closed');
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


DROP PROCEDURE IF EXISTS contracts_with_activeaccounts;

DELIMITER $$

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
	set @contracts = (select contractId from contractaccounts where accountId = statusPoint COLLATE utf8_general_ci and statusDesc != 'closed');
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

ALTER TABLE `contract` MODIFY COLUMN `name` VARCHAR(200);
ALTER TABLE `contractcorecustomers` MODIFY COLUMN `coreCustomerName` VARCHAR(200);
ALTER TABLE `contractaccounts` MODIFY COLUMN `accountName` VARCHAR(200);
ALTER TABLE `accounts` MODIFY COLUMN `MembershipName` VARCHAR(200);

DROP VIEW IF EXISTS `travelnotifications_view`;

CREATE VIEW `travelnotifications_view` AS
select
    `travelnotification`.`id` AS `notificationId`,
    `travelnotification`.`PlannedDepartureDate` AS `startDate`,
    `travelnotification`.`PlannedReturnDate` AS `endDate`,
    `travelnotification`.`Destinations` AS `destinations`,
    `travelnotification`.`AdditionalNotes` AS `additionalNotes`,
    `travelnotification`.`Status_id` AS `Status_id`,
    `travelnotification`.`phonenumber` AS `contactNumber`,
    `notificationcardinfo`.`Customer_id` AS `customerId`,
    `travelnotification`.`createdts` AS `date`,
    group_concat(concat(`notificationcardinfo`.`CardName`, ' ', `notificationcardinfo`.`CardNumber`) separator ',') AS `cardNumber`,
    count(`notificationcardinfo`.`CardNumber`) AS `cardCount`,
    `status`.`Description` AS `status`
from
    ((`travelnotification`
join `notificationcardinfo` on
    ((`travelnotification`.`id` = `notificationcardinfo`.`Notification_id`)))
join `status` on
    ((`travelnotification`.`Status_id` = `status`.`id`)))
group by
    `travelnotification`.`id`,
    `status`.`Description`,
    `notificationcardinfo`.`Customer_id`;


DROP VIEW IF EXISTS `travelnotifications_view`;

CREATE VIEW `travelnotifications_view` AS
select
    `travelnotification`.`id` AS `notificationId`,
    `travelnotification`.`PlannedDepartureDate` AS `startDate`,
    `travelnotification`.`PlannedReturnDate` AS `endDate`,
    `travelnotification`.`Destinations` AS `destinations`,
    `travelnotification`.`AdditionalNotes` AS `additionalNotes`,
    `travelnotification`.`Status_id` AS `Status_id`,
    `travelnotification`.`phonenumber` AS `contactNumber`,
    `notificationcardinfo`.`Customer_id` AS `customerId`,
    `travelnotification`.`createdts` AS `date`,
    group_concat(concat(`notificationcardinfo`.`CardName`, ' ', `notificationcardinfo`.`CardNumber`) separator ',') AS `cardNumber`,
    count(`notificationcardinfo`.`CardNumber`) AS `cardCount`,
    `status`.`Description` AS `status`
from
    ((`travelnotification`
join `notificationcardinfo` on
    ((`travelnotification`.`id` = `notificationcardinfo`.`Notification_id`)))
join `status` on
    ((`travelnotification`.`Status_id` = `status`.`id`)))
group by
    `travelnotification`.`id`,
    `status`.`Description`,
    `notificationcardinfo`.`Customer_id`;


DROP PROCEDURE IF EXISTS `user_limitgroup_limits_create_proc`;

DELIMITER $$
CREATE PROCEDURE `user_limitgroup_limits_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 100000000;

SET @singlePaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'SINGLE_PAYMENT');
                             
SET @bulkPaymentsActions = (SELECT group_concat(distinct featureaction.id SEPARATOR ",") from featureaction where
                             featureaction.limitgroupId = 'BULK_PAYMENT');

SET @max_per_transaction_single_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));

SET @max_per_transaction_bulk_payment = (SELECT MAX(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_daily_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_single_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@singlePaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         
SET @max_weekly_limit_bulk_payment = (SELECT SUM(value) FROM customeraction WHERE 
                                         customeraction.contractId = _contractId AND
                                         customeraction.coreCustomerId = _coreCustomerId AND
                                         customeraction.Customer_id = _userId AND
										 customeraction.companyLegalUnit = _legalEntityId AND
                                         FIND_IN_SET(customeraction.Action_id,@bulkPaymentsActions) AND
                                         customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND
                                         !isnull(customeraction.Account_id) AND
                                         !isnull(customeraction.value));
                                         

IF(@max_per_transaction_single_payment != 0) THEN 						
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment);
END IF;

IF ( @max_per_transaction_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment);
END IF;

IF ( @max_daily_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment);
END IF;

IF ( @max_daily_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment);
END IF;

IF ( @max_weekly_limit_single_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment);
END IF;

IF ( @max_weekly_limit_bulk_payment != 0) THEN
INSERT INTO `customerlimitgrouplimits` (`id`, `Customer_id`, `contractId`, `coreCustomerId`, `companyLegalUnit`, `limitGroupId`, `LimitType_id`, `value`) VALUES 
(LEFT(UUID(), 50), _userId, _contractId, _coreCustomerId, _legalEntityId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment);
END IF;



END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `useraccounts_create_proc`;

DELIMITER $$
CREATE PROCEDURE `useraccounts_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _legalEntityId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE accountID varchar(255) ;
DECLARE finished INTEGER DEFAULT 0 ;

DECLARE accountData CURSOR FOR (SELECT contractaccounts.accountId FROM contractaccounts WHERE contractId = _contractId
        AND coreCustomerId = _coreCustomerId AND find_in_set(contractaccounts.accountId,_accountsCSV) AND companyLegalUnit = _legalEntityId);
	
DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
         
OPEN accountData; 
getAccount : LOOP
fetch accountData into accountID; 
     IF finished = 1 THEN 
	     LEAVE getAccount;
	 ELSE
         SET @id = (SELECT LEFT(UUID(), 50));
         set @accounttypeid = (select contractaccounts.typeId from contractaccounts where contractaccounts.accountId COLLATE utf8_general_ci =accountID);
         set @accounttypename = (select accounttype.TypeDescription from accounttype where accounttype.TypeID COLLATE utf8_general_ci =@accounttypeid);
		 INSERT INTO `customeraccounts` (`id`, `Customer_id`, `Account_id`,`contractId`, `coreCustomerId`, `companyLegalUnit`,`accountType`)
            VALUES (@id, _userId, accountID, _contractId, _coreCustomerId,_legalEntityId,@accounttypename);
      END IF;
      ITERATE  getAccount;
END LOOP getAccount;
CLOSE accountData;
END$$
DELIMITER ;

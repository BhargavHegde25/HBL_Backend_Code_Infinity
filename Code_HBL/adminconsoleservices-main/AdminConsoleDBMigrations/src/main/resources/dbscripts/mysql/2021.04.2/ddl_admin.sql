DROP TABLE IF EXISTS `approvalmatrixtemplate`;
CREATE TABLE `approvalmatrixtemplate` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `contractId` varchar(50) NOT NULL,
   `coreCustomerId` varchar(50) NOT NULL,
   `actionId` varchar(255) NOT NULL,
   `approvalruleId` varchar(50) DEFAULT NULL,
   `isGroupMatrix` tinyint(1) NOT NULL DEFAULT '0',
   `limitTypeId` varchar(50) NOT NULL,
   `lowerlimit` decimal(20,2) NOT NULL DEFAULT '-1.00',
   `upperlimit` decimal(20,2) NOT NULL DEFAULT '-1.00',
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   `invalid` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`),
   KEY `FK_approvalmatrixtemplate_approvalruleid_idx` (`approvalruleId`),
   KEY `FK_approvalmatrixtemplate_action_idx` (`actionId`),
   KEY `FK_approvalmatrixtemplate_limittype_idx` (`limitTypeId`),
   KEY `approvalmatrixtemplate_contractId_idx` (`contractId`),
   CONSTRAINT `FK_approvalmatrixtemplate_actionid` FOREIGN KEY (`actionId`) REFERENCES `featureaction` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_approvalmatrixtemplate_approvalruleid` FOREIGN KEY (`approvalruleId`) REFERENCES `approvalrule` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT `FK_approvalmatrixtemplate_limittypeid` FOREIGN KEY (`limitTypeId`) REFERENCES `limittype` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=1066 DEFAULT CHARSET=utf8;
 
DROP TABLE IF EXISTS `customerapprovalmatrixtemplate`;
CREATE TABLE `customerapprovalmatrixtemplate` (
   `id` bigint(20) NOT NULL AUTO_INCREMENT,
   `customerId` varchar(50) NOT NULL,
   `approvalMatrixId` bigint(20) NOT NULL,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`id`),
   KEY `FK_customerapprovalmatrixtemplate_customer_idx` (`customerId`),
   KEY `FK_customerapprovalmatrixtemplate_approvalmatrixtemplate_idx` (`approvalMatrixId`),
   CONSTRAINT `FK_customerapprovalmatrixtemplate_approvalmatrixtemplate` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrixtemplate` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
   CONSTRAINT `FK_customerapprovalmatrixtemplate_customer` FOREIGN KEY (`customerId`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=1306 DEFAULT CHARSET=utf8;
 
DROP TABLE IF EXISTS `signatorygroupmatrixtemplate`;
CREATE TABLE `signatorygroupmatrixtemplate` (
   `signatoryGroupMatrixId` bigint(20) NOT NULL AUTO_INCREMENT,
   `approvalMatrixId` bigint(20) DEFAULT NULL,
   `groupList` text,
   `groupRule` longtext,
   `createdby` varchar(50) DEFAULT NULL,
   `modifiedby` varchar(50) DEFAULT NULL,
   `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
   `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
   `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
   PRIMARY KEY (`signatoryGroupMatrixId`),
   KEY `FK_signatorygroupmatrixtemplate_approvalMatrixId` (`approvalMatrixId`),
   CONSTRAINT `FK_signatorygroupmatrixtemplate_approvalMatrixId` FOREIGN KEY (`approvalMatrixId`) REFERENCES `approvalmatrixtemplate` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
 
DROP PROCEDURE IF EXISTS `approvalmatrixtemplate_default_create_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrixtemplate_default_create_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _approvalMode INT
)
MAINLABEL: BEGIN
 DECLARE accountList TEXT DEFAULT "";
 DECLARE limitTypeId_1 varchar(255) DEFAULT "DAILY_LIMIT";
 DECLARE limitTypeId_2 varchar(255) DEFAULT "MAX_TRANSACTION_LIMIT";
 DECLARE limitTypeId_3 varchar(255) DEFAULT "WEEKLY_LIMIT";
 DECLARE actionIndex INTEGER DEFAULT 0;
 DECLARE isGroupMatrix INT DEFAULT 0;
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
 
IF _approvalMode = 0 THEN
	SET isGroupMatrix = 0;
ELSEIF _approvalMode = 1 THEN
	SET isGroupMatrix = 1;
END IF;

set @numOfActions = LENGTH(_actionIds) - LENGTH(REPLACE(_actionIds, ',', '')) + 1;
set actionIndex = 0; 
getAction: LOOP
	set actionIndex = actionIndex + 1;
	IF actionIndex = @numOfActions + 1 THEN
		LEAVE getAction;
	Else
		set @actionId = SUBSTRING_INDEX(SUBSTRING_INDEX(_actionIds, ',', actionIndex), ',', -1 );
		SELECT Type_id INTO typeId FROM featureaction WHERE id = @actionId;
		IF typeId = "MONETARY" THEN			
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(_contractId, @actionId, limitTypeId_1,_cif,'NO_APPROVAL',isGroupMatrix);
            INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(_contractId, @actionId,limitTypeId_2,_cif,'NO_APPROVAL',isGroupMatrix);
            INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(_contractId, @actionId, limitTypeId_3,_cif,'NO_APPROVAL',isGroupMatrix);
		ELSEIF typeId = "NON_MONETARY" THEN
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(_contractId, @actionId, "NON_MONETARY_LIMIT",_cif,'NO_APPROVAL',isGroupMatrix);
        END IF;
	END IF;
 END LOOP getAction;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrixtemplate_cleanup_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrixtemplate_cleanup_proc`(
IN _actionIds TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _limitTypeId TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
MAINLABEL: BEGIN
	UPDATE approvalmatrix SET softdeleteflag = 1 WHERE contractId= _contractId AND 
													 coreCustomerId = _cif AND
													 FIND_IN_SET(actionId, _actionIds) AND
													 FIND_IN_SET(limitTypeId, _limitTypeId) AND
													 softdeleteflag = 0;										
    
    UPDATE approvalmatrixtemplate SET softdeleteflag = 1 WHERE contractId= _contractId AND 
																coreCustomerId = _cif AND
                                                                FIND_IN_SET(actionId, _actionIds)AND
																FIND_IN_SET(limitTypeId, _limitTypeId) AND
																softdeleteflag = 0;				
									
END$$

DELIMITER ;

ALTER TABLE `customersignatorygroup` DROP FOREIGN KEY `FK_customersignatorygroup_signaoryGroupId`;
ALTER TABLE `customersignatorygroup` ADD CONSTRAINT `FK_customersignatorygroup_signaoryGroupId` FOREIGN KEY (`signatoryGroupId`) REFERENCES `signatorygroup` (`signatoryGroupId`) ON DELETE CASCADE ON UPDATE NO ACTION;

ALTER TABLE `bbrequest` ADD COLUMN `isGroupMatrix` tinyint(1) NOT NULL DEFAULT '0';

DROP procedure IF EXISTS `fetch_signatorygroups_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroups_proc`(
  IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
  IN _coreCustomerId longtext CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	DECLARE index1 INTEGER DEFAULT 1;
	IF _contractId = "" THEN
		SET _contractId = "%";
    END IF;

    set @numOfRecords = LENGTH(_coreCustomerId) - LENGTH(REPLACE(_coreCustomerId, ',', '')) + 1;
    set @concatstring = concat ("\"",SUBSTRING_INDEX(SUBSTRING_INDEX(_coreCustomerId, ',', index1), ',', -1 ),"\"");
        customerId :  LOOP
        set index1 = index1 + 1;
        IF index1 = @numOfRecords + 1 THEN
            LEAVE customerId;
        else
            set @customerId = (SUBSTRING_INDEX(SUBSTRING_INDEX(_coreCustomerId, ',', index1), ',', -1 ));
            set @concatstring = Concat(@concatstring, ",\"",@customerId,"\"");
        END IF;
    END LOOP customerId;
	set @execStmt=concat ('select sg.signatoryGroupId, sg.signatoryGroupName, sg.signatoryGroupDescription, cc.coreCustomerId, cc.coreCustomerName,
	c.id contractId, c.name contractName, sg.createdby, sg.createdts, sg.lastmodifiedts, sg.softdeleteflag ,
   ( select count(*) from customersignatorygroup cs1 where cs1.signatoryGroupId=sg.signatoryGroupId  group by cs1.signatoryGroupId ) as noOfUsers
	from contract c 
	left join contractcorecustomers cc on cc.contractId=c.id 
	left join signatorygroup sg  on c.id=sg.contractId and sg.coreCustomerId=cc.coreCustomerId
	where c.id LIKE "',_contractId,'"');
	
    IF _coreCustomerId != "" THEN
		SET  @execStmt =  concat (@execStmt, ' and cc.coreCustomerId IN (', @concatstring,' )');
    END IF;
	
    PREPARE stmt FROM @execStmt;
    EXECUTE stmt;
    DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_signatorygroup_details_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroup_details_proc`(
  IN _signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select signatorygroup.signatoryGroupId as `signatoryGroupId`,
	signatorygroup.signatoryGroupName as `signatoryGroupName`,
	signatorygroup.signatoryGroupDescription as `signatoryGroupDescription`,
	signatorygroup.coreCustomerId as `coreCustomerId`,
	contractcorecustomers.coreCustomerName as `coreCustomerName`,
	signatorygroup.createdts as `createdts`,
	signatorygroup.createdby as `createdby`,
	signatorygroup.lastmodifiedts as `lastmodifiedts`,
	customersignatorygroup.customerSignatoryGroupId as `customerSignatoryGroupId`,
	customersignatorygroup.customerId as `customerId`,
	customerbasicinfo_view.Name AS `fullName`,
	customerbasicinfo_view.Username AS `userName`,
	customerbasicinfo_view.Customer_Role as `customerRole`,
	customersignatorygroup.createdts as `signatoryaddedts`
	  from signatorygroup  
	  left join customersignatorygroup  on signatorygroup.signatoryGroupId=customersignatorygroup.signatoryGroupId
	  left join contractcorecustomers  on contractcorecustomers.coreCustomerId=signatorygroup.coreCustomerId 
	  left join customerbasicinfo_view  on customerbasicinfo_view.Customer_id=customersignatorygroup.customerId
	  where signatorygroup.signatoryGroupId=_signatoryGroupId;  
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
set @SigGroupDes = SUBSTRING_INDEX(SUBSTRING_INDEX(_sigGroupValues, ';', 3 ),';',-1);
set @sigCreatedBy = SUBSTRING_INDEX(_sigGroupValues, ';', -1 );
IF @SigGroupName IS NOT NULL AND @SigGroupName != '' THEN
set @query = concat('UPDATE signatorygroup SET signatoryGroupName = ',@SigGroupName,' WHERE signatoryGroupId = ',@sigGroupId,'');
prepare sql_query from @query;
execute sql_query;
END IF;
IF @sigGroupDes IS NOT NULL AND @sigGroupDes != '' THEN
set @query = concat('UPDATE signatorygroup SET signatoryGroupDescription = ',@sigGroupDes,',lastmodifiedts = CURRENT_TIMESTAMP, modifiedby = ',@sigCreatedBy,'  WHERE signatoryGroupId = ',@sigGroupId,'');
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

DROP PROCEDURE IF EXISTS `fetch_approvalmatrixtemplate_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_approvalmatrixtemplate_proc`(
    IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _cif VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _limitTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN _actions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
    )
BEGIN 
    IF _cif = "" THEN
    SET _cif = "%";
    END IF;
    
    IF _limitTypeId = "" THEN
    SET _limitTypeId = "%";
    END IF;
    
    SET @isGroupMatrix = (select isGroupLevel from approvalmode where contractId = _contractId AND coreCustomerId = _cif);
	IF @isGroupMatrix is null THEN
    SET @isGroupMatrix = 0;
	END IF;
	
    IF @isGroupMatrix = 0 THEN
		SELECT 
			`approvalmatrixtemplate`.`id`,
			`approvalmatrixtemplate`.`contractId`,
			 `approvalmatrixtemplate`.`limitTypeId`,
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
			 `approvalmatrixtemplate`.`lowerlimit`,
			 `approvalmatrixtemplate`.`upperlimit`,
			 `customer`.`id` AS `customerId`,
			 `customer`.`FirstName` AS `firstName`,
			 `customer`.`LastName` AS `lastName`,
			 `contractcorecustomers`.`coreCustomerId` AS `cifId`,
			 `contractcorecustomers`.`coreCustomerName` AS `cifName`,
			 `approvalmatrixtemplate`.`invalid`,
			 `approvalmatrixtemplate`.`isGroupMatrix`
			FROM (((((((`approvalmatrixtemplate` AS `approvalmatrixtemplate`
			LEFT JOIN
			`customerapprovalmatrixtemplate` AS `customerapprovalmatrixtemplate`
			ON `approvalmatrixtemplate`.`id` = `customerapprovalmatrixtemplate`.`approvalMatrixId`)
			LEFT JOIN
			`customer` AS `customer`
			ON `customerapprovalmatrixtemplate`.`customerId` = `customer`.`id`)
			LEFT JOIN
			`featureaction` AS `featureAction`
			ON `approvalmatrixtemplate`.`actionId` = `featureAction`.`id`)
			LEFT JOIN
			`approvalrule` AS `approvalRule`
			ON `approvalmatrixtemplate`.`approvalruleId` = `approvalRule`.`id`) 
		  LEFT JOIN
			`feature` AS `feature`
			ON `featureAction`.`Feature_id` = `feature`.`id`)
		  LEFT JOIN
			`contractfeatures` AS `contractfeatures`
			ON `feature`.`id` = `contractfeatures`.`featureId`
			  and  `approvalmatrixtemplate`.`contractId` = `contractfeatures`.`contractId`
			  and `approvalmatrixtemplate`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
		  LEFT JOIN
			`contractcorecustomers` AS `contractcorecustomers`
			ON `approvalmatrixtemplate`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalmatrixtemplate`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
		WHERE 
		`approvalmatrixtemplate`.`contractId` = `_contractId` AND
		`approvalmatrixtemplate`.`coreCustomerId` LIKE `_cif` AND
		FIND_IN_SET(`approvalmatrixtemplate`.`actionId`,`_actions`) > 0 AND 
		`approvalmatrixtemplate`.`limitTypeId` LIKE `_limitTypeId` AND 
		`approvalmatrixtemplate`.`softdeleteflag` = 0 AND
		 `featureAction`.`approveFeatureAction` is not null AND
		 `featureAction`.`approveFeatureAction` != '' AND
		`featureAction`.`status` = 'SID_ACTION_ACTIVE'
			ORDER BY `approvalmatrixtemplate`.`contractId`,`approvalmatrixtemplate`.`coreCustomerId`,`approvalmatrixtemplate`.`limitTypeId`,`approvalmatrixtemplate`.`actionId` ,`approvalmatrixtemplate`.`lowerlimit`;
	
    ELSEIF @isGroupMatrix = 1 THEN
		SELECT 
			`approvalmatrixtemplate`.`id`,
			`approvalmatrixtemplate`.`contractId`,
			 `approvalmatrixtemplate`.`limitTypeId`,
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
			 `approvalmatrixtemplate`.`lowerlimit`,
			 `approvalmatrixtemplate`.`upperlimit`,
			 `signatorygroupmatrixtemplate`.`groupList` AS `groupList`,
			 `signatorygroupmatrixtemplate`.`groupRule` AS `groupRule`,
			 `contractcorecustomers`.`coreCustomerId` AS `cifId`,
			 `contractcorecustomers`.`coreCustomerName` AS `cifName`,
			 `approvalmatrixtemplate`.`invalid`,
			 `approvalmatrixtemplate`.`isGroupMatrix`
			FROM ((((((`approvalmatrixtemplate` AS `approvalmatrixtemplate`
			LEFT JOIN
			`signatorygroupmatrixtemplate` AS `signatorygroupmatrixtemplate`
			ON `approvalmatrixtemplate`.`id` = `signatorygroupmatrixtemplate`.`approvalMatrixId`)
			LEFT JOIN
			`featureaction` AS `featureAction`
			ON `approvalmatrixtemplate`.`actionId` = `featureAction`.`id`)
			LEFT JOIN
			`approvalrule` AS `approvalRule`
			ON `approvalmatrixtemplate`.`approvalruleId` = `approvalRule`.`id`) 
		  LEFT JOIN
			`feature` AS `feature`
			ON `featureAction`.`Feature_id` = `feature`.`id`)
		  LEFT JOIN
			`contractfeatures` AS `contractfeatures`
			ON `feature`.`id` = `contractfeatures`.`featureId`
			  and  `approvalmatrixtemplate`.`contractId` = `contractfeatures`.`contractId`
			  and `approvalmatrixtemplate`.`coreCustomerId` = `contractfeatures`.`coreCustomerId`)
		  LEFT JOIN
			`contractcorecustomers` AS `contractcorecustomers`
			ON `approvalmatrixtemplate`.`contractId`  = `contractcorecustomers`.`contractId` AND `approvalmatrixtemplate`.`coreCustomerId` = `contractcorecustomers`.`coreCustomerId`)          
		WHERE 
		`approvalmatrixtemplate`.`contractId` = `_contractId` AND
		`approvalmatrixtemplate`.`coreCustomerId` LIKE `_cif` AND 
		`approvalmatrixtemplate`.`isGroupMatrix` = 1 AND
		FIND_IN_SET(`approvalmatrixtemplate`.`actionId`,`_actions`) > 0 AND 
		`approvalmatrixtemplate`.`limitTypeId` LIKE `_limitTypeId` AND 
		`approvalmatrixtemplate`.`softdeleteflag` = 0 AND
		 `featureAction`.`approveFeatureAction` is not null AND
		 `featureAction`.`approveFeatureAction` != '' AND
		`featureAction`.`status` = 'SID_ACTION_ACTIVE'
			ORDER BY `approvalmatrixtemplate`.`contractId`,`approvalmatrixtemplate`.`coreCustomerId`,`approvalmatrixtemplate`.`limitTypeId`,`approvalmatrixtemplate`.`actionId` ,`approvalmatrixtemplate`.`lowerlimit`;

    END IF;
END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS `approvalmatrixtemplate_create_proc`;
DELIMITER $$
CREATE PROCEDURE `approvalmatrixtemplate_create_proc`(
IN _matrixValues TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _matrixApprover TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _isGroupMatrix INT
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
				set @query = concat('INSERT INTO approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,isGroupMatrix) VALUES (',@matrixComma,');');
				prepare sql_query from @query;
				execute sql_query;
				
				SET @id = LAST_INSERT_ID();
                
                IF _isGroupMatrix = 0 THEN
					set @customerIds = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixApprover, ',', index1), ',', -1 );
					IF @customerIds IS NOT NULL AND @customerIds != '' THEN
						set @customerIdsComma = REPLACE(@customerIds, ';', ',');
							set @length2 = LENGTH(@customerIdsComma) - LENGTH(REPLACE(@customerIdsComma, ',', '')) + 1;
							set index2 = 0;
							getCustomerIds: LOOP
								set index2 = index2 + 1;
								IF index2 = @length2 + 1 THEN 
									LEAVE getCustomerIds;
								else
									set @customerId = SUBSTRING_INDEX( SUBSTRING_INDEX(@customerIdsComma, ',', index2), ',', -1 );
									INSERT INTO customerapprovalmatrixtemplate(customerId,approvalMatrixId) values (@customerId,@id);							
									ITERATE getCustomerIds;
								END IF;
							END LOOP getCustomerIds;
						END IF;
					ITERATE  getValues;
				ELSEIF _isGroupMatrix = 1 THEN
					set @sigValues = SUBSTRING_INDEX( SUBSTRING_INDEX(_matrixApprover, '#', index1), '#', -1 );
					SET @id = LAST_INSERT_ID();
					SET @groupList = SUBSTRING_INDEX(@sigValues, ';', 1 );
					SET @groupRule = SUBSTRING_INDEX(@sigValues, ';', -1 );
					INSERT INTO signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
               END IF; 
			END IF;
	END LOOP getValues;
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_signatorygroups_in_approvalrule_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroups_in_approvalrule_proc`()
BEGIN
	SELECT signatorygroup.signatoryGroupId
	FROM signatorygroup 
	WHERE FIND_IN_SET(signatorygroup.signatoryGroupId,
	(SELECT group_concat(distinct(REPLACE(REPLACE(REPLACE(groupList,']',''),'[',''),'"','')))
		FROM signatorygroupmatrix WHERE 
		signatorygroupmatrix.softdeleteflag = false));
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_approvalgroups_for_pendingtxn_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_approvalgroups_for_pendingtxn_proc`()
BEGIN
	SELECT signatorygroup.signatoryGroupId
	FROM signatorygroup 
	WHERE FIND_IN_SET(signatorygroup.signatoryGroupId,
	(SELECT group_concat(distinct(REPLACE(REPLACE(REPLACE(pendingGroupList,']',''),'[',''),'"','')))
		FROM signatorygrouprequestmatrix WHERE 
		signatorygrouprequestmatrix.isApproved = false));
END$$
DELIMITER ;

ALTER TABLE signatorygroup ADD COLUMN status tinyint DEFAULT 1;

DROP procedure IF EXISTS `update_signatorygroup_for_user_proc`;

DELIMITER $$
CREATE PROCEDURE `update_signatorygroup_for_user_proc`(
IN _coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _customerId longtext CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _signatorygroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DECLARE index1 INTEGER DEFAULT 0;
set @length = LENGTH(_customerId) - LENGTH(REPLACE(_customerId, ',', '')) + 1;
users: LOOP
	set index1 = index1 + 1;
	IF index1 = @length + 1 THEN 
		LEAVE users;
	else
		set @cusRecord = SUBSTRING_INDEX( SUBSTRING_INDEX(_customerId, ',', index1), ',', -1 );
		set @customerId = REPLACE(@cusRecord, ';', ',');

		delete from customersignatorygroup where customerId = @customerId and signatoryGroupId 
		in (select signatoryGroupId from signatorygroup where coreCustomerId=_coreCustomerId and contractId=_contractId);

		if(_signatorygroupId !='') THEN
			INSERT INTO customersignatorygroup(customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (UUID(),_signatorygroupId,@customerId,@customerId);		
		END IF;
		
		ITERATE  users;
	END IF;
END LOOP users;
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_signatorygroup_customer_details_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_signatorygroup_customer_details_proc`(
in _signatoryGroupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SELECT 
	`customer`.`id` AS `userId`,
    `customer`.`isCombinedUser` AS `isCombinedUser`,
	`customer`.`UserName` AS `userName`,
    `customer`.`FirstName` AS `firstName`,
	`customer`.`LastName` AS `lastName`,
    `membergroup`.`Name` AS `role`,
	`customerimage`.`UserImage` AS `userImage`
from 
	`customersignatorygroup`
LEFT JOIN `customer` ON (`customer`.`id` = `customersignatorygroup`.`customerId`)
LEFT JOIN `signatorygroup` ON (`signatorygroup`.`signatoryGroupId` =  `customersignatorygroup`.`signatoryGroupId`)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customersignatorygroup`.`customerId` AND `customergroup`.`coreCustomerId` = `signatorygroup`.`coreCustomerId`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
LEFT JOIN `customerimage` ON (`customerimage`.`Customer_id` = `customer`.`id`)
	WHERE 
        `customersignatorygroup`.`signatoryGroupId` = _signatoryGroupId ;           
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_approvalqueue_proc`;
DROP procedure IF EXISTS `fetch_approvalqueueforgroup_proc`;
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
        
        SET @customerGroupIds = (SELECT group_concat(signatoryGroupId SEPARATOR ",") FROM customersignatorygroup WHERE FIND_IN_SET(customerId, @combinedIds));
        IF @customerGroupIds is NULL THEN      
			SET @customerGroupIds = "";
		END IF;
        
        SET @alreadyApprovedIds = (select GROUP_CONCAT(DISTINCT(requestId) SEPARATOR ",") from bbactedrequest WHERE FIND_IN_SET(createdby, @combinedIds) AND NOT action = 'Pending' AND softdeleteflag = 0);
        IF @alreadyApprovedIds is NULL THEN      
			SET @alreadyApprovedIds = "''";
		END IF;
        
        SET @approvalRequestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") 
        							FROM requestapprovalmatrix
        							INNER JOIN approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
	        							WHERE requestapprovalmatrix.isGroupRule = 0 
                                        AND FIND_IN_SET(requestapprovalmatrix.approvalMatrixId,  @customerMatrixIds) 
	        							AND NOT FIND_IN_SET(requestapprovalmatrix.requestId, @alreadyApprovedIds)
	        							AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
													OR
												(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
									);
		
		IF @approvalRequestIds is NULL THEN      
			SET @approvalRequestIds = "''";
		END IF;
		
        SET @groupIds = @customerGroupIds;
        do_this: LOOP
			SET @strLen = LENGTH(@groupIds);
				SET @requestIds = (SELECT group_concat(DISTINCT(requestId) SEPARATOR ",") FROM signatorygrouprequestmatrix 
					WHERE NOT FIND_IN_SET(requestId, @approvalRequestIds) AND isApproved = '0' AND FIND_IN_SET( SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE(pendingGroupList,'[',''),']',''),' ','')) > 0  
                    AND requestId NOT in (@alreadyApprovedIds) );
				IF @requestIds is NULL THEN      
					SET @requestIds = "''";
				END IF;
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
                    bbrequest.isGroupMatrix,
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
					CASE bbrequest.isGroupMatrix
						WHEN 0 THEN LEAST(
									(SELECT COUNT(DISTINCT(customerId)) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
									, 
									SUM(
										CASE approvalrule.numberOfApprovals
											WHEN -1 THEN (SELECT COUNT(*) FROM customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
											WHEN NULL OR \"\" THEN 0
											ELSE approvalrule.numberOfApprovals
										END
									) 
								)
                        ELSE NULL
					END as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ",@query,"
				GROUP BY bbrequest.requestId"
				);
            
	-- select @select_statement;
	PREPARE stmt FROM @select_statement;
    EXECUTE stmt; 
    DEALLOCATE PREPARE stmt;
END $$
DELIMITER ;
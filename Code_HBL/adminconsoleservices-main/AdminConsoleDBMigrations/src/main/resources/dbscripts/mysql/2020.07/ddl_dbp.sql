CREATE OR REPLACE VIEW `customeraddress_view` AS
select
    `c`.`id` AS `CustomerId`,
    `ca`.`Address_id` AS `Address_id`,
    `ca`.`isPrimary` AS `isPrimary`,
    `ca`.`Type_id` AS `AddressType`,
    `a`.`id` AS `AddressId`,
    `a`.`addressLine1` AS `AddressLine1`,
    `a`.`addressLine2` AS `AddressLine2`,
    `a`.`zipCode` AS `ZipCode`,
    `a`.`Region_id` AS `Region_id`,
    `a`.`City_id` AS `City_id`,
    `coun`.`id` AS `Country_id`,
    `a`.`cityName` AS `CityName`,
    `reg`.`Name` AS `RegionName`,
    `reg`.`Code` AS `RegionCode`,
    `coun`.`Name` AS `CountryName`,
    `coun`.`Code` AS `CountryCode`
from
    ((((`customeraddress` `ca`
join `customer` `c` on
    ((`ca`.`Customer_id` = `c`.`id`)))
join `address` `a` on
    ((`a`.`id` = `ca`.`Address_id`)))
join `region` `reg` on
    ((`reg`.`id` = `a`.`Region_id`)))
join `country` `coun` on
    ((`coun`.`id` = `reg`.`Country_id`)));
	
	
ALTER TABLE address DROP FOREIGN KEY FK_Address_City;

ALTER TABLE `bulkwirefiletransactdetails` 
DROP FOREIGN KEY `FK_bulkwiretransaction_bulkWireFileID`;
ALTER TABLE `bulkwirefiletransactdetails` 
DROP INDEX `FK_bulkwiretransaction_bulkWireFileID_idx` ;


ALTER TABLE `bulkwirefilelineitems` 
DROP FOREIGN KEY `FK_bulkwirelineitems_bulkWireFileID`;
ALTER TABLE `bulkwirefilelineitems` 
DROP INDEX `FK_bulkwirelineitems_bulkWireFileID_idx` ;



ALTER TABLE `bulkwirefiles` 
CHANGE COLUMN `bulkWireFileID` `bulkWireFileID` VARCHAR(50) NOT NULL ,
ADD UNIQUE INDEX `bulkWireFileID_UNIQUE` (`bulkWireFileID` ASC);

ALTER TABLE `bulkwirefilelineitems` 
CHANGE COLUMN `bulkWireFileID` `bulkWireFileID` VARCHAR(50) NOT NULL ,
ADD INDEX `FK_bulkwirelineitems_bulkwirefileid_idx` (`bulkWireFileID` ASC);


ALTER TABLE `bulkwirefilelineitems` 
ADD CONSTRAINT `FK_bulkwirelineitems_bulkwirefileid`
  FOREIGN KEY (`bulkWireFileID`)
  REFERENCES `bulkwirefiles` (`bulkWireFileID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;


ALTER TABLE `bulkwirefiletransactdetails` 
CHANGE COLUMN `bulkWireFileID` `bulkWireFileID` VARCHAR(50) NOT NULL ,
ADD INDEX `FK_transactionID_bulkwirefileID_idx` (`bulkWireFileID` ASC);

ALTER TABLE `bulkwirefiletransactdetails` 
ADD CONSTRAINT `FK_transactionID_bulkwirefileID`
  FOREIGN KEY (`bulkWireFileID`)
  REFERENCES `bulkwirefiles` (`bulkWireFileID`)
  ON DELETE NO ACTION
  ON UPDATE NO ACTION;

ALTER TABLE `transaction` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `Reference_id`;
ALTER TABLE `useraccounts` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `Account_id`;
ALTER TABLE `userserviceprefernces` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT NULL AFTER `Service_id`;


DROP procedure IF EXISTS `fetch_bulkwires_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwires_proc`(
	IN `createdBy` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `searchString` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortByParam` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortOrder` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `pageOffset` int,
	IN `pageSize` int,
	IN `bulkWireCategoryFilter` VARCHAR(50),
    IN `fileDomesticView` TINYINT(1),
    IN `fileInternationalView` TINYINT(1),
    IN `templateDomesticView` TINYINT(1),
    IN `templateInternationalView` TINYINT(1)
)
BEGIN
    SET @companyId = (SELECT Organization_Id FROM customer WHERE id = createdby);
    
	SET @isSMEUser = if(@companyId != "" OR @companyId != NULL , true, false);
    
    SET @remove0DomesticFile = concat(" AND `bulkwirefiles`.`noOfDomesticTransactions` <> 0");
    SET @remove0InternationalFile = concat(" AND `bulkwirefiles`.`noOfInternationalTransactions` <> 0");
    SET @remove0DomesticTemplate = concat(" AND `bulkwiretemplate`.`noOfDomesticTransactions` <> 0");
    SET @remove0InternationalTemplate = concat(" AND `bulkwiretemplate`.`noOfInternationalTransactions` <> 0");
    
    SET @fileAddQuery = if(fileDomesticView,@remove0DomesticFile,@remove0InternationalFile);
    SET @fileAdd = if(fileDomesticView && fileInternationalView,"",@fileAddQuery);
    
    SET @templateAddQuery = if(templateDomesticView,@remove0DomesticTemplate,@remove0InternationalTemplate);
    SET @templateAdd = if(templateDomesticView && templateInternationalView,"",@templateAddQuery);
    
    SET @filterRetailFile = concat("`bulkwirefiles`.`createdBy` = ", createdby , " AND `bulkwirefiles`.`softdeleteflag` = 0",@fileAdd);
				
	SET @filterSMEFile = concat("`bulkwirefiles`.`company_id` = ", @companyId  , " AND `bulkwirefiles`.`softdeleteflag` = 0",@fileAdd);

	SET @filterRetailTemplate = concat("`bulkwiretemplate`.`createdBy` = ", createdby , " AND `bulkwiretemplate`.`softdeleteflag` = 0",@templateAdd);
				
	SET @filterSMETemplate = concat("`bulkwiretemplate`.`company_id` = ", @companyId  , " AND `bulkwiretemplate`.`softdeleteflag` = 0",@templateAdd);
	
    SET @getByIdFilterFile = if(@isSMEUser, @filterSMEFile, @filterRetailFile);
    SET @getByIdFilterTemplate = if(@isSMEUser, @filterSMETemplate, @filterRetailTemplate);
    
    SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'createdts', sortByParam);
    SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
    SET sortByParam = if(sortByParam = 'username',concat('firstName ',sortOrder,',lastname'), sortByParam);
    
     SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));               
    
    SET @orderBy = concat(" ORDER BY ", sortByParam , " ", sortOrder);
    
    SET @paginationQuery = if((pageOffset != NULL OR pageOffset != "" AND pageSize != NULL OR pageSize != ""),
							concat(" LIMIT ",pageSize, " OFFSET " ,pageOffset),
                            ''); 
    
    
	SET @searchQueryFile = concat("(`bulkwirefiles`.`bulkWireFileName` LIKE ",searchString," OR `bulkwirefiles`.`noOfTransactions` LIKE ",
    searchString," OR `bulkwirefiles`.`noOfDomesticTransactions` LIKE ",searchString," OR `bulkwirefiles`.`noOfInternationalTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");
	
    SET @searchQueryTemplate = concat("(`bulkwiretemplate`.`bulkWireTemplateName` LIKE ",searchString," OR `bulkwiretemplate`.`noOfTransactions` LIKE ",
    searchString," OR `bulkwiretemplate`.`noOfDomesticTransactions` LIKE ",searchString," OR `bulkwiretemplate`.`noOfInternationalTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");

	SET @onlyFileWithoutSearch = concat(@getByIdFilterFile, @orderBy , @paginationQuery);
	SET @searchFilterFile = concat(@getByIdFilterFile," AND ",@searchQueryFile);
    SET @filterFile = if(searchString = "",@getByIdFilterFile,@searchFilterFile);
    SET @OnlyFile = if(searchString = "",@onlyFileWithoutSearch,@searchFilterFile);
	SET @finalFile = if(bulkWireCategoryFilter = "Files",@OnlyFile,@filterFile);
    
	SET @defaultFilterTemplate = concat(@getByIdFilterTemplate, @orderBy , @paginationQuery);
	SET @searchFilterTemplate = concat(@getByIdFilterTemplate," AND ",@searchQueryTemplate, @orderBy);
    SET @filterTemplate = if(searchString = "",@defaultFilterTemplate,@searchFilterTemplate);

    
    SET @select_files = concat("SELECT
       bulkwirefiles.bulkWireFileID as bulkWireID,
        bulkwirefiles.bulkWireFileName as bulkWireName,
        bulkwirefiles.noOfTransactions,
        bulkwirefiles.noOfDomesticTransactions,
        bulkwirefiles.noOfInternationalTransactions,
        bulkwirefiles.createdts,
        bulkwirefiles.lastmodifiedts,
        bulkwirefiles.lastExecutedOn,
		NULL as defaultFromAccount,
		NULL as defaultCurrency,
        'Files' as bulkWireCategory,
        customer.FirstName as firstname,
        customer.LastName as lastname
     FROM
        (`bulkwirefiles`
        LEFT JOIN `customer` ON (`bulkwirefiles`.`createdBy` = `customer`.`id`))
		WHERE ",@finalFile);
	SET @select_templates = concat("SELECT
	       bulkwiretemplate.bulkWireTemplateID as bulkWireID,
	        bulkwiretemplate.bulkWireTemplateName as bulkWireName,
	        bulkwiretemplate.noOfTransactions,
	        bulkwiretemplate.noOfDomesticTransactions,
	        bulkwiretemplate.noOfInternationalTransactions,
	        bulkwiretemplate.createdts,
	        bulkwiretemplate.lastmodifiedts,
            bulkwiretemplate.lastExecutedOn,
            bulkwiretemplate.defaultFromAccount,
            bulkwiretemplate.defaultCurrency,
	        'Templates' as bulkWireCategory,
	        customer.FirstName as firstname,
	        customer.LastName as lastname
	     FROM
	        (`bulkwiretemplate`
	        LEFT JOIN `customer` ON (`bulkwiretemplate`.`createdBy` = `customer`.`id`))
			WHERE ", @filterTemplate);
			
	IF bulkWireCategoryFilter = 'Files' THEN
		SET @select_statement = @select_files;
	ELSEIF bulkWireCategoryFilter = 'Templates' THEN
		SET @select_statement = @select_templates;
	ELSE
		SET @select_statement = concat(@select_files," UNION ALL ",@select_templates);
	END IF;
	    
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_bulkwire_filelineitems_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_filelineitems_proc`(
    IN bulkWireFileID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @queryFilter = concat("`bulkwirefilelineitems`.`bulkWireFileID` = \"", bulkWireFileID, "\" AND `bulkwirefilelineitems`.`softdeleteflag` = 0 ");
SET sortByParam = if (sortByParam = "" OR sortByParam = NULL, 'recipientName', sortByParam);
SET sortOrder = if (sortOrder = "" OR sortOrder = NULL, 'ASC', sortOrder);
SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));   

SET @orderBy = concat(" ORDER BY bulkWireTransferType,", sortByParam, " ", sortOrder);

SET @searchQuery = concat("(`bulkwirefilelineitems`.`recipientName` LIKE ",searchString," OR  `bulkwirefilelineitems`.`fromAccountNumber` LIKE ",searchString," OR `bulkwirefilelineitems`.`swiftCode` LIKE ",searchString," OR `bulkwirefilelineitems`.`recipientAccountNumber` LIKE ",searchString," OR `bulkwirefilelineitems`.`routingNumber` LIKE ",searchString," OR `bulkwirefilelineitems`.`internationalRoutingNumber` LIKE ",searchString," OR `bulkwirefilelineitems`.`note` LIKE ",searchString,"  OR 
`bulkwirefilelineitems`.`transactionType` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientAddressLine1` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientAddressLine2` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientCity` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientState` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientZipCode` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientBankName` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientBankAddress1` LIKE ",searchString,"  OR
`bulkwirefilelineitems`.`recipientBankAddress2` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientBankcity` LIKE ",searchString,"  OR
`bulkwirefilelineitems`.`recipientBankZipCode` LIKE ",searchString,"  OR `bulkwirefilelineitems`.`recipientBankstate` LIKE ",searchString,")");
	
SET @defaultFilter = concat(@queryFilter, @orderBy);

SET @searchFilter = concat(@queryFilter," AND ",@searchQuery,@orderBy);

SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

SET @select_statement = concat("SELECT * FROM `bulkwirefilelineitems` WHERE ", @filter);
        
 -- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_bulkwire_files_transct_detail_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_files_transct_detail_proc`(
in  bulkWireFileID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  pageOffset int,
in  pageSize int
)
BEGIN
    
    SET @qfilter = concat("`bulkwirefiletransactdetails`.`bulkWireFileID` = \"", bulkWireFileID, "\" AND `bulkwirefiletransactdetails`.`softdeleteflag` = 0 ");
      
    SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'transactionDate', sortByParam);
    SET sortByParam = if(sortByParam = 'username', 'firstname,lastname', sortByParam);
    SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
    
    SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));   
    
    SET @orderBy = concat(" ORDER BY ", sortByParam , " ", sortOrder);
    
    SET @paginationQuery = if((pageOffset != NULL OR pageOffset != "" AND pageSize != NULL OR pageSize != ""),
							concat(" LIMIT ",pageSize, " OFFSET " ,pageOffset),
                            ''); 
    
    SET @searchQuery = concat("(`bulkwirefiletransactdetails`.`transactionDate` LIKE ",searchString," OR `bulkwirefiletransactdetails`.`totalCountOfTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");
    
	SET @defaultFilter = concat(@qfilter, @orderBy , @paginationQuery);

	SET @searchFilter = concat(@qfilter," AND ",@searchQuery,@orderBy);

    SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

	SET @select_statement = concat("SELECT 
	    bulkwirefiletransactdetails.bulkWireTransactionID,
		bulkwirefiletransactdetails.bulkWireFileID, 
		bulkwirefiletransactdetails.initiatedBy, 
		bulkwirefiletransactdetails.createdts,
		bulkwirefiletransactdetails.lastmodifiedts,
		bulkwirefiletransactdetails.transactionDate, 
		bulkwirefiletransactdetails.totalCountOfTransactions,
		bulkwirefiletransactdetails.totalCountOfDomesticTransactions,
		bulkwirefiletransactdetails.totalCountOfInternationalTransactions,
		customer.FirstName as firstname,
		customer.LastName as lastname
    FROM
        (`bulkwirefiletransactdetails`
		LEFT JOIN `customer` ON (`bulkwirefiletransactdetails`.`initiatedBy` = `customer`.`id`))
		WHERE ", @filter);
		
 -- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;


DROP procedure IF EXISTS `fetch_bulkWireTransactionsExecution_details_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkWireTransactionsExecution_details_proc`( 
	in BulkWireFileExecution_id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in statusFilter varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `isDomesticPermitted` INT,
	IN `isInternationalPermitted` INT
)
BEGIN

SET @query1= concat("`wiretransfers`.`wireFileExecution_id` = ", BulkWireFileExecution_id," AND `wiretransfers`.`softdeleteflag` = 0 ");
SET @query2= concat("`wiretransfers`.`wireFileExecution_id` = ", BulkWireFileExecution_id," AND `wiretransfers`.`status` ='",statusFilter,"' AND `wiretransfers`.`softdeleteflag` = 0 ");
SET @query3= concat("`wiretransfers`.`wireFileExecution_id` = ", BulkWireFileExecution_id," AND `wiretransfers`.`status` in ('Failed','Denied')"," AND `wiretransfers`.`softdeleteflag` = 0 ");

SET sortByParam =if (sortByParam = "" OR sortByParam = NULL, 'transactionId', sortByParam);
SET sortOrder = if (sortOrder = "" OR sortOrder = NULL, 'ASC', sortOrder);
SET searchString = if (searchString = "" OR searchString = NULL, "", concat("'%",searchString,"%'"));

SET @orderBy = concat(" ORDER BY ", sortByParam, " ", sortOrder);
SET @searchQuery = concat("(`wiretransfers`.`amount` LIKE ",searchString," OR `wiretransfers`.`notes` LIKE ",searchString," OR `wiretransfers`.`fromAccountNumber` LIKE ",searchString," OR `wiretransfers`.`payeeAccountNumber` LIKE ",searchString," OR `wiretransfers`.`transactionType` LIKE ",searchString," OR `onetimepayee`.`payeeName` LIKE ",searchString," OR `onetimepayee`.`payeeType` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine1` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine2` LIKE ",searchString," OR `onetimepayee`.`cityName` LIKE ",searchString," OR `onetimepayee`.`state` LIKE ",searchString," OR `onetimepayee`.`zipCode` LIKE ",searchString," OR `onetimepayee`.`bankName` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine1` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine2` LIKE ",searchString," OR `onetimepayee`.`bankZip` LIKE ",searchString," OR `onetimepayee`.`bankState` LIKE ",searchString," OR `onetimepayee`.`bankCity` LIKE ",searchString," OR  `onetimepayee`.`routingNumber` LIKE ",searchString," OR `onetimepayee`.`internationalRoutingCode` LIKE ",searchString," OR `onetimepayee`.`swiftCode` LIKE ",searchString,")");
SET @statusQuery = if(statusFilter="Failed", @query3, @query2);
SET @finalQueryFilter = if(statusFilter="", @query1, @statusQuery);
SET @defaultFilter = concat(@finalQueryFilter, @orderBy);
SET @searchFilter = concat(@finalQueryFilter, " AND ", @searchQuery, @orderBy);
SET @filter = if (searchString = "", @defaultFilter, @searchFilter);

SET @DomQuery = CONCAT("SELECT COUNT(*) FROM (`wiretransfers` LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) WHERE onetimepayee.wireAccountType = 'Domestic' AND ", @filter, " INTO @DomCount;");
PREPARE stmt1 FROM @DomQuery; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;

SET @InternationalQuery = CONCAT("SELECT COUNT(*) FROM (`wiretransfers` LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) WHERE onetimepayee.wireAccountType = 'International' AND ", @filter, " INTO @InternationalCount;");
PREPARE stmt2 FROM @InternationalQuery; EXECUTE stmt2; DEALLOCATE PREPARE stmt2;

IF (isDomesticPermitted = 0 AND isInternationalPermitted = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSEIF (isDomesticPermitted = 0 AND @InternationalCount = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSEIF (isInternationalPermitted = 0 AND @DomCount = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSE
	set @select_statement = concat("SELECT 
			wiretransfers.transactionId,
			wiretransfers.confirmationNumber,
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
			FROM (`wiretransfers` 
			LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) 
			WHERE ", @filter);
			
	 -- select @select_statement;
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END IF;
  
END$$
DELIMITER ;


DROP TABLE IF EXISTS `bulkwiretemplate`;
CREATE TABLE `bulkwiretemplate` (
  `bulkWireTemplateID` varchar(50) NOT NULL,
  `bulkWireTemplateName` varchar(150) NOT NULL,
  `noOfTransactions` int(11) NOT NULL DEFAULT '0',
  `noOfDomesticTransactions` int(11) NOT NULL DEFAULT '0',
  `noOfInternationalTransactions` int(11) NOT NULL DEFAULT '0',
  `createdBy` varchar(50) NOT NULL,
  `modifiedBy` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` varchar(50) DEFAULT NULL,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  `lastExecutedOn` timestamp NULL DEFAULT NULL,
  `defaultFromAccount` varchar(50) NOT NULL,
  `defaultCurrency` varchar(10) NOT NULL,
  `deleteUniqueValue` VARCHAR(50) NOT NULL DEFAULT "NA",
  PRIMARY KEY (`bulkWireTemplateID`),
  UNIQUE KEY `bulkWireTemplateID` (`bulkWireTemplateID`),
  KEY `FK1_bulkwiretemplate_CreatedBy` (`createdBy`),
  KEY `FK3_bulkwiretemplate_ModifiedBy` (`modifiedBy`),
  KEY `FK4_bulkwiretemplate_DefaultCurrency` (`defaultCurrency`),
  KEY `FK2_bulkwiretemplate_CompanyID` (`company_id`),
  CONSTRAINT `FK1_bulkwiretemplate_CreatedBy` FOREIGN KEY (`createdBy`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK2_bulkwiretemplate_CompanyID` FOREIGN KEY (`company_id`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK3_bulkwiretemplate_ModifiedBy` FOREIGN KEY (`modifiedBy`) REFERENCES `customer` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK4_bulkwiretemplate_DefaultCurrency` FOREIGN KEY (`defaultCurrency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `bulkwiretemplate`
ADD CONSTRAINT TemplateName_ForCompany UNIQUE (bulkWireTemplateName, company_id, deleteUniqueValue);

ALTER TABLE `bulkwiretemplate`
ADD CONSTRAINT TemplateName_ForUser UNIQUE (bulkWireTemplateName, createdBy, deleteUniqueValue);

DROP TABLE IF EXISTS `bulkwiretemplatelineitems`;
CREATE TABLE `bulkwiretemplatelineitems` (
  `bulkWireTemplateLineItemID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireTemplateID` varchar(50) NOT NULL,
  `swiftCode` varchar(50) DEFAULT NULL,
  `bulkWireTransferType` varchar(50) DEFAULT NULL,
  `transactionType` varchar(50) DEFAULT NULL,
  `internationalRoutingNumber` varchar(50) DEFAULT NULL,
  `recipientName` varchar(100) DEFAULT NULL,
  `recipientAddressLine1` varchar(100) DEFAULT NULL,
  `recipientAddressLine2` varchar(100) DEFAULT NULL,
  `recipientCity` varchar(100) DEFAULT NULL,
  `recipientState` varchar(100) DEFAULT NULL,
  `recipientCountryName` varchar(100) DEFAULT NULL,
  `recipientZipCode` varchar(20) DEFAULT NULL,
  `recipientBankName` varchar(100) DEFAULT NULL,
  `recipientBankAddress1` varchar(100) DEFAULT NULL,
  `recipientBankAddress2` varchar(100) DEFAULT NULL,
  `recipientBankZipCode` varchar(20) DEFAULT NULL,
  `recipientBankcity` varchar(100) DEFAULT NULL,
  `recipientBankstate` varchar(100) DEFAULT NULL,
  `accountNickname` varchar(50) DEFAULT NULL,
  `recipientAccountNumber` varchar(50) DEFAULT NULL,
  `routingNumber` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) NOT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `payeeId` int(11) DEFAULT NULL,
  `templateRecipientCategory` enum('EXISTINGRECIPIENT','MANUALLYADDED','EXTRACTEDFROMFILE') NOT NULL,
  PRIMARY KEY (`bulkWireTemplateLineItemID`),
  KEY `FK1_bulkwiretemplatelineitems_bulkwireTemplateID` (`bulkWireTemplateID`),
  KEY `FK2_bulkwiretemplatelineitems_payeeID` (`payeeId`),
  CONSTRAINT `FK1_bulkwiretemplatelineitems_bulkwireTemplateID` FOREIGN KEY (`bulkWireTemplateID`) REFERENCES `bulkwiretemplate` (`bulkWireTemplateID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK2_bulkwiretemplatelineitems_payeeID` FOREIGN KEY (`payeeId`) REFERENCES `payee` (`Id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `bulkwiretemplatetransactdetails`;
CREATE TABLE `bulkwiretemplatetransactdetails` (
  `bulkWireTransactionID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireTemplateID` varchar(50) NOT NULL,
  `transactionDate` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `initiatedBy` varchar(50) NOT NULL,
  `totalCountOfTransactions` int(11) NOT NULL,
  `totalCountOfDomesticTransactions` int(11) DEFAULT '0',
  `totalCountOfInternationalTransactions` int(11) DEFAULT '0',
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bulkWireTransactionID`),
  KEY `FK1_bulkwiretemplatetransactdetails_bulkwiretemplateID` (`bulkWireTemplateID`),
  CONSTRAINT `FK1_bulkwiretemplatetransactdetails_bulkwiretemplateID` FOREIGN KEY (`bulkWireTemplateID`) REFERENCES `bulkwiretemplate` (`bulkWireTemplateID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `wiretransfers`
   ADD COLUMN `wireTemplateExecution_id` int(11) DEFAULT NULL AFTER wireFileExecution_id;
ALTER TABLE `wiretransfers`
	ADD INDEX `FK_wiretransfers_wiretemplateexecution_id_idx` (`wireTemplateExecution_id`);
ALTER TABLE `bulkwiresamplefile`
ADD COLUMN `fileCategory` enum('BULKWIRE_FILE','BULKWIRE_TEMPLATE') NOT NULL;
 
DROP procedure IF EXISTS `businesstyperole_get_proc_`;

DELIMITER $$

CREATE PROCEDURE `businesstyperole_get_proc` (
	in _businessTypeId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

select groupbusinesstype.BusinessType_id as businessTypeId,
	   membergroup.id as groupId,
       membergroup.Name as groupName,
       membergroup.Description as groupDescription,
       groupbusinesstype.isDefaultGroup as isDefaultGroup
from 
groupbusinesstype 
LEFT JOIN membergroup ON ( groupbusinesstype.Group_id = membergroup.id)
where
groupbusinesstype.BusinessType_id = _businessTypeId;

END$$

DELIMITER ;

DROP PROCEDURE IF EXISTS bulkwiretemplate_create_proc;
DELIMITER $$
CREATE PROCEDURE `bulkwiretemplate_create_proc`(
	IN `_bulkwiretemplateValues` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_bulkwiretemplatelineitemValues` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE index1 INTEGER DEFAULT 0;
	
	DECLARE EXIT HANDLER for SQLEXCEPTION
	 BEGIN
	  GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
	  SELECT @text as ErrorMessage;
	 END;
	
	SET @query1 = CONCAT('INSERT INTO bulkwiretemplate(bulkWireTemplateId,bulkWireTemplateName,noOfTransactions,noOfDomesticTransactions,noOfInternationalTransactions,createdBy,modifiedBy,company_id,createdts,lastmodifiedts,defaultFromAccount,defaultCurrency) VALUES (',_bulkwiretemplateValues,');');
	PREPARE sql_query FROM @query1;
	EXECUTE sql_query;
	
	SET @id = TRIM(BOTH '"' FROM (SELECT SUBSTRING_INDEX(_bulkwiretemplateValues, ",", 1)));
	
	SET @query2 = CONCAT('INSERT INTO bulkwiretemplatelineitems(bulkWireTemplateID,createdts,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values ',_bulkwiretemplatelineitemValues,';');	
	PREPARE sql_query2 FROM @query2;
	EXECUTE sql_query2;

SELECT * FROM bulkwiretemplate WHERE bulkWireTemplateID = @id;

END$$
DELIMITER ;

DROP TABLE IF EXISTS `membership`;
CREATE TABLE `membership` (
  `id` varchar(50) NOT NULL,
  `isCustomerCentric` tinyint(1) NOT NULL DEFAULT '0',
  `name` varchar(45) DEFAULT NULL,
  `taxId` varchar(45) DEFAULT NULL,
  `phone` varchar(45) DEFAULT NULL,
  `email` varchar(45) DEFAULT NULL,
  `isBusinessType` tinyint(1) NOT NULL DEFAULT '0',
  `addressId` varchar(50) DEFAULT NULL,
  `createdby` varchar(45) DEFAULT NULL,
  `modifiedby` varchar(45) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  UNIQUE KEY `taxId_UNIQUE` (`taxId`),
  KEY `FK_membership_address_addressId_idx` (`addressId`),
  CONSTRAINT `FK_membership_address_addressId` FOREIGN KEY (`addressId`) REFERENCES `address` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `membershipaccounts`;
CREATE TABLE `membershipaccounts` (
  `id` varchar(50) NOT NULL,
  `membershipId` varchar(50) NOT NULL,
  `accountId` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_membershipaccounts_Account_id` (`accountId`),
  KEY `FK_membershipaccounts_membership_membershipId_idx` (`membershipId`),
  CONSTRAINT `FK_membershipaccounts_membership_membershipId` FOREIGN KEY (`membershipId`) REFERENCES `membership` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP TABLE IF EXISTS `membershipowner`;
CREATE TABLE `membershipowner` (
  `id` varchar(50) NOT NULL,
  `membershipId` varchar(50) NOT NULL,
  `userName` varchar(50) NOT NULL,
  `firstName` varchar(50) DEFAULT NULL,
  `lastName` varchar(50) NOT NULL,
  `dateOfBirth` date NOT NULL,
  `ssn` varchar(50) DEFAULT NULL,
  `taxId` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `email` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_membershipowner_membership_idx` (`membershipId`),
  CONSTRAINT `FK_membershipowner_membership_membershipId` FOREIGN KEY (`membershipId`) REFERENCES `membership` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


DROP VIEW IF EXISTS `allaccountsview`;
CREATE VIEW `allaccountsview` AS
    SELECT DISTINCT
        `accounts`.`AccountHolder` AS `AccountHolder`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`AccountName` AS `AccountName`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`isBusinessAccount` AS `IsOrganizationAccount`,
        `accounttype`.`TypeDescription` AS `accountType`,
        `membershipaccounts`.`membershipId` AS `Membership_id`,
        `membership`.`taxId` AS `Taxid`,
        `membership`.`phone` AS `phoneNumber`,
        `membership`.`email` AS `emailId`,
        `membership`.`name` AS `name`
    FROM
        (((`accounts`
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `accounttype` ON ((`accounts`.`Type_id` = `accounttype`.`TypeID`)))
        LEFT JOIN `membership` ON ((`membership`.`id` = `membershipaccounts`.`membershipId`)));

DROP VIEW IF EXISTS `customeraccountsview`;          
CREATE VIEW `customeraccountsview` AS
    SELECT DISTINCT
        `membership`.`id` AS `Membership_id`,
        `membership`.`taxId` AS `Taxid`,
        `customeraccounts`.`Customer_id` AS `Customer_id`,
        `customeraccounts`.`Customer_id` AS `User_id`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`isBusinessAccount` AS `isBusinessAccount`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`UserName` AS `userName`,
        `accounts`.`CurrencyCode` AS `currencyCode`,
        `accounts`.`AccountHolder` AS `accountHolder`,
        `accounts`.`error` AS `error`,
        `accounts`.`Address` AS `Address`,
        `accounts`.`Scheme` AS `Scheme`,
        `accounts`.`Number` AS `number`,
        `accounts`.`AvailableBalance` AS `availableBalance`,
        `accounts`.`CurrentBalance` AS `currentBalance`,
        `accounts`.`InterestRate` AS `interestRate`,
        `accounts`.`AvailableCredit` AS `availableCredit`,
        `accounts`.`MinimumDue` AS `minimumDue`,
        `accounts`.`DueDate` AS `dueDate`,
        `accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
        `accounts`.`ClosingDate` AS `closingDate`,
        `accounts`.`PaymentTerm` AS `paymentTerm`,
        `accounts`.`OpeningDate` AS `openingDate`,
        `accounts`.`MaturityDate` AS `maturityDate`,
        `accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
        `accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,
        `accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
        `accounts`.`DividendRate` AS `dividendRate`,
        `accounts`.`DividendYTD` AS `dividendYTD`,
        `accounts`.`EStatementmentEnable` AS `eStatementEnable`,
        `customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,
        `accounts`.`StatusDesc` AS `statusDesc`,
        `accounts`.`NickName` AS `nickName`,
        `accounts`.`OriginalAmount` AS `originalAmount`,
        `accounts`.`OutstandingBalance` AS `outstandingBalance`,
        `accounts`.`PaymentDue` AS `paymentDue`,
        `accounts`.`PaymentMethod` AS `paymentMethod`,
        `accounts`.`SwiftCode` AS `swiftCode`,
        `accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
        `accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
        `accounts`.`RoutingNumber` AS `routingNumber`,
        `accounts`.`SupportBillPay` AS `supportBillPay`,
        `accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
        `accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
        `accounts`.`SupportTransferTo` AS `supportTransferTo`,
        `accounts`.`SupportDeposit` AS `supportDeposit`,
        `accounts`.`UnpaidInterest` AS `unpaidInterest`,
        `accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
        `accounts`.`principalBalance` AS `principalBalance`,
        `accounts`.`PrincipalValue` AS `principalValue`,
        `accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
        `accounts`.`phone` AS `phoneId`,
        `accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
        `accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
        `accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
        `accounts`.`LastPaymentDate` AS `lastPaymentDate`,
        `accounts`.`LastStatementBalance` AS `lastStatementBalance`,
        `accounts`.`LateFeesDue` AS `lateFeesDue`,
        `accounts`.`maturityAmount` AS `maturityAmount`,
        `accounts`.`MaturityOption` AS `maturityOption`,
        `accounts`.`payoffAmount` AS `payoffAmount`,
        `accounts`.`PayOffCharge` AS `payOffCharge`,
        `accounts`.`PendingDeposit` AS `pendingDeposit`,
        `accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
        `accounts`.`JointHolders` AS `jointHolders`,
        `accounts`.`IsPFM` AS `isPFM`,
        `accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
        `accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
        `accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
        `accounts`.`InterestEarned` AS `interestEarned`,
        `accounts`.`CurrentAmountDue` AS `currentAmountDue`,
        `accounts`.`CreditLimit` AS `creditLimit`,
        `accounts`.`CreditCardNumber` AS `creditCardNumber`,
        `accounts`.`BsbNum` AS `bsbNum`,
        `accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
        `accounts`.`BondInterest` AS `bondInterest`,
        `accounts`.`AvailablePoints` AS `availablePoints`,
        `accounts`.`AccountName` AS `accountName`,
        `accounts`.`email` AS `email`,
        `accounts`.`IBAN` AS `IBAN`,
        `accounts`.`adminProductId` AS `adminProductId`,
        `accounts`.`UpdatedBy` AS `UpdatedBy`,
        `accounts`.`LastUpdated` AS `LastUpdated`,
        `accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,
        `bank`.`Description` AS `bankname`,
        `accounts`.`AccountPreference` AS `accountPreference`,
        `accounttype`.`transactionLimit` AS `transactionLimit`,
        `accounttype`.`transferLimit` AS `transferLimit`,
        `accounttype`.`rates` AS `rates`,
        `accounttype`.`termsAndConditions` AS `termsAndConditions`,
        `accounttype`.`TypeDescription` AS `typeDescription`,
        `accounttype`.`supportChecks` AS `supportChecks`,
        `accounttype`.`displayName` AS `displayName`,
        `accounts`.`accountSubType` AS `accountSubType`,
        `accounts`.`description` AS `description`,
        `accounts`.`schemeName` AS `schemeName`,
        `accounts`.`identification` AS `identification`,
        `accounts`.`secondaryIdentification` AS `secondaryIdentification`,
        `accounts`.`servicerSchemeName` AS `servicerSchemeName`,
        `accounts`.`servicerIdentification` AS `servicerIdentification`,
        `accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,
        `accounts`.`dataType` AS `dataType`,
        `accounts`.`dataDateTime` AS `dataDateTime`,
        `accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
        `accounts`.`dataCreditLineType` AS `dataCreditLineType`,
        `accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
        `accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`
    FROM
        (((((`accounts`
        JOIN `customeraccounts`)
        JOIN `accounttype`)
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `membership` ON (`membership`.`id` = `membershipaccounts`.`membershipId`))
        LEFT JOIN `bank` ON ((`accounts`.`Bank_id` = `bank`.`id`)))
    WHERE
        ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`)
            AND (`accounts`.`Type_id` = `accounttype`.`TypeID`));


DROP procedure IF EXISTS `dbpalerts_customercommunication`;
DELIMITER $$
CREATE PROCEDURE `dbpalerts_customercommunication` (_customers TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select Customer_id, Type_id, Value from customercommunication   where FIND_IN_SET(`customercommunication`.`Customer_id`,_customers) and isPrimary = '1' order by  Type_id; 
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpalerts_getCustomerData`;
DELIMITER $$
CREATE PROCEDURE `dbpalerts_getCustomerData` (_customerids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,_usernames TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select id as CustomerId, UserName from customer where FIND_IN_SET(`customer`.`id`,_customerids) or FIND_IN_SET(`customer`.`UserName`,_usernames) ;
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpalerts_getCustIdFromCore`;
DELIMITER $$
CREATE PROCEDURE `dbpalerts_getCustIdFromCore` (_backendids TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select BackendId,Customer_id  from backendidentifier where FIND_IN_SET(`backendidentifier`.`BackendId`,_backendids);
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpalerts_getCustidFromAccount`;
DELIMITER $$
CREATE PROCEDURE `dbpalerts_getCustidFromAccount` (_accounts TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
Select Account_id, User_id, Type_id as accounttype_id from accounts where FIND_IN_SET(`accounts`.`Account_id`,_accounts);
END$$
DELIMITER ;

DROP procedure IF EXISTS `dbpalerts_getNotificationId`;
DELIMITER $$
CREATE  PROCEDURE `dbpalerts_getNotificationId`()
BEGIN
SELECT LAST_INSERT_ID() as lastid;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `bulkwiretemplate_update_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkwiretemplate_update_proc`(
	IN `_bulkwiretemplateValues` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_update_bulkwiretemplatelineitemValues` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_insert_bulkwiretemplatelineitemValues` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	DECLARE EXIT HANDLER for SQLEXCEPTION
	 BEGIN
	  GET DIAGNOSTICS CONDITION 1 @text = MESSAGE_TEXT;
	  SELECT @text as ErrorMessage;
	 END;
	 
	IF _bulkwiretemplateValues IS NULL OR _bulkwiretemplateValues = '' THEN
		SELECT "_bulkwiretemplateValues CANNOT BE NULL OR EMPTY";
	ELSE
		SET @query0 = CONCAT('INSERT INTO bulkwiretemplate(bulkWireTemplateID,bulkWireTemplateName,createdBy,modifiedBy,lastmodifiedts,defaultFromAccount,defaultCurrency) VALUES (',_bulkwiretemplateValues,') 
			ON DUPLICATE KEY UPDATE bulkWireTemplateName = VALUES(bulkWireTemplateName), modifiedBy = VALUES(modifiedBy), lastmodifiedts = VALUES(lastmodifiedts), defaultFromAccount = VALUES(defaultFromAccount), defaultCurrency = VALUES(defaultCurrency);');
			PREPARE sql_query0 FROM @query0;
			EXECUTE sql_query0;

		IF _update_bulkwiretemplatelineitemValues IS NOT NULL AND _update_bulkwiretemplatelineitemValues != '' THEN
			SET @query1 = CONCAT('INSERT INTO bulkwiretemplatelineitems(bulkWireTemplateLineItemID,bulkWireTemplateID,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values ',_update_bulkwiretemplatelineitemValues,'
			ON DUPLICATE KEY UPDATE lastmodifiedts = VALUES(lastmodifiedts), swiftCode = VALUES(swiftCode), bulkWireTransferType = VALUES(bulkWireTransferType), transactionType = VALUES(transactionType), internationalRoutingNumber = VALUES(internationalRoutingNumber), recipientName = VALUES(recipientName), recipientAddressLine1 = VALUES(recipientAddressLine1), recipientAddressLine2 = VALUES(recipientAddressLine2), recipientCity = VALUES(recipientCity), recipientState = VALUES(recipientState), recipientCountryName = VALUES(recipientCountryName), recipientZipCode = VALUES(recipientZipCode), recipientBankName = VALUES(recipientBankName), recipientBankAddress1 = VALUES(recipientBankAddress1), recipientBankAddress2 = VALUES(recipientBankAddress2), recipientBankZipCode = VALUES(recipientBankZipCode), recipientBankcity = VALUES(recipientBankcity), recipientBankstate = VALUES(recipientBankstate), accountNickname = VALUES(accountNickname), recipientAccountNumber = VALUES(recipientAccountNumber), routingNumber = VALUES(routingNumber), createdby = VALUES(createdby), modifiedBy = VALUES(modifiedBy), payeeId = VALUES(payeeId), templateRecipientCategory = VALUES(templateRecipientCategory);');
			
			PREPARE sql_query1 FROM @query1;
			EXECUTE sql_query1;
		END IF;
		
		
		IF _insert_bulkwiretemplatelineitemValues IS NOT NULL AND _insert_bulkwiretemplatelineitemValues != '' THEN
			SET @query2 = CONCAT('INSERT INTO bulkwiretemplatelineitems(bulkWireTemplateID,createdts,lastmodifiedts,swiftCode,bulkWireTransferType,transactionType,internationalRoutingNumber,recipientName,recipientAddressLine1,recipientAddressLine2,recipientCity,recipientState,recipientCountryName,recipientZipCode,recipientBankName,recipientBankAddress1,recipientBankAddress2,recipientBankZipCode,recipientBankcity,recipientBankstate,accountNickname,recipientAccountNumber,routingNumber,createdby,modifiedBy,payeeId,templateRecipientCategory) values ',_insert_bulkwiretemplatelineitemValues,';');	
			PREPARE sql_query2 FROM @query2;
			EXECUTE sql_query2;
		END IF;
	
			
		SET @bulkWiretemplateID = TRIM(BOTH '"' FROM (SELECT SUBSTRING_INDEX(_bulkwiretemplateValues, ",", 1)));
		
		SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0 INTO @DomCount;
		SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE bulkWireTemplateID = @bulkWiretemplateID AND bulkWireTransferType = 'International' AND softdeleteflag = 0 INTO @InternationalCount;
		SET @totalCount = @DomCount + @InternationalCount;
		
		UPDATE bulkwiretemplate SET noOfTransactions = @totalCount, noOfDomesticTransactions = @DomCount, noOfInternationalTransactions = @InternationalCount WHERE bulkWireTemplateID = @bulkWiretemplateID;
	
		SELECT * FROM bulkwiretemplate WHERE bulkWireTemplateID = @bulkWiretemplateID;
	END IF;

END$$
DELIMITER ;

ALTER TABLE `organisationemployees` 
ADD COLUMN `isAuthSignatory` BIT(1) NULL DEFAULT b'0' AFTER `softdeleteflag`;

ALTER TABLE `accounts` 
ADD COLUMN `Membership_id` VARCHAR(50) NULL DEFAULT NULL AFTER `Organization_id`;

ALTER TABLE `customer` 
ADD COLUMN `isCombinedUser` BIT(1) NULL DEFAULT b'0' AFTER `CustomerType_id`,
ADD COLUMN `organizationType` VARCHAR(45) NULL DEFAULT NULL AFTER `Organization_Id`;

DROP PROCEDURE IF EXISTS `bulkwiretemplate_delete_proc`;
DELIMITER $$
CREATE PROCEDURE `bulkwiretemplate_delete_proc`(
	IN `_bulkwiretemplateID` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_bulkwiretemplatelineitemIDs` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	IF _bulkwiretemplateID IS NOT NULL AND _bulkwiretemplateID != '' THEN
		IF _bulkwiretemplatelineitemIDs IS NOT NULL AND _bulkwiretemplatelineitemIDs != '' THEN
			UPDATE bulkwiretemplatelineitems SET softdeleteflag = 1 WHERE FIND_IN_SET(bulkWireTemplateLineItemID, _bulkwiretemplatelineitemIDs) COLLATE utf8_general_ci;
			SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE bulkWireTemplateID = _bulkwiretemplateID AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0  COLLATE utf8_general_ci INTO @DomCount;
			SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE bulkWireTemplateID = _bulkwiretemplateID AND bulkWireTransferType = 'International' AND softdeleteflag = 0  COLLATE utf8_general_ci INTO @InternationalCount;
			SET @totalCount = @DomCount + @InternationalCount;
			UPDATE bulkwiretemplate SET noOfTransactions = @totalCount, noOfDomesticTransactions = @DomCount, noOfInternationalTransactions = @InternationalCount WHERE bulkWireTemplateID = _bulkwiretemplateID COLLATE utf8_general_ci;
			SELECT * FROM bulkwiretemplate WHERE bulkWireTemplateID = _bulkwiretemplateID COLLATE utf8_general_ci;
			
		ELSE
			UPDATE bulkwiretemplate SET deleteUniqueValue = _bulkwiretemplateID WHERE bulkWireTemplateID = _bulkwiretemplateID COLLATE utf8_general_ci;
			UPDATE bulkwiretemplate SET softdeleteflag = 1 WHERE bulkWireTemplateID = _bulkwiretemplateID COLLATE utf8_general_ci;
			UPDATE bulkwiretemplatelineitems SET softdeleteflag = 1 WHERE bulkWireTemplateID = _bulkwiretemplateID COLLATE utf8_general_ci;
			SELECT "SUCCESS";
		END IF;		
		
	ELSE
		SELECT "FAILED";
	END IF;

END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_bulkwiretemplatelineitems_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwiretemplatelineitems_proc`(
IN bulkWireTemplateID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN groupBy varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE lineItemId,payeeId,swiftCodeVal,intRoutingNumVal,recAccntnumVal,routingNumVal,recNicknameVal VARCHAR(50);
DECLARE recNameVal,recAddLine1Val,recAddLine2Val,recCityVal,recStateVal,recCountryVal,recBankNameVal,recBankAdd1Val,recBankAdd2Val,recBankCityVal,recBankStateVal  VARCHAR(100);
DECLARE recZipVal,recBankZipVal VARCHAR(20);
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

END$$
DELIMITER ;


DROP PROCEDURE IF EXISTS `getRequestApprovers_proc`;
DELIMITER $$
CREATE PROCEDURE `getRequestApprovers_proc`(
	IN `_requestId` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_status` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	IF _status IS NULL OR _status = '' THEN
		SELECT bb.requestId,cam.customerId AS approvers, c.FirstName, c.LastName FROM bbrequest AS bb JOIN requestapprovalmatrix AS ram JOIN customerapprovalmatrix AS cam JOIN customer AS c WHERE bb.requestId = ram.requestId AND ram.approvalMatrixId = cam.approvalMatrixId AND cam.customerId = c.id AND bb.requestId = _requestId GROUP BY approvers;
	ELSE
		SELECT bb.createdby AS approvers, c.FirstName, c.LastName FROM bbactedrequest AS bb JOIN customer AS c WHERE bb.createdby = c.id AND requestId = _requestId AND status = _status GROUP BY approvers;
	END IF;

END$$
DELIMITER ;

ALTER TABLE `membershipowner` 
ADD COLUMN `memberType` VARCHAR(45) NULL DEFAULT NULL AFTER `lastmodifiedts`,
ADD COLUMN `salutation` VARCHAR(45) NULL DEFAULT NULL AFTER `memberType`,
ADD COLUMN `maritalStatus` VARCHAR(45) NULL DEFAULT NULL AFTER `salutation`,
ADD COLUMN `employmentStatus` VARCHAR(45) NULL DEFAULT NULL AFTER `maritalStatus`;

ALTER TABLE `organisationmembership` CHANGE COLUMN `id` `id` VARCHAR(50) NOT NULL ;

DROP procedure IF EXISTS `fetch_bulkwire_template_transct_detail_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_template_transct_detail_proc`(
in  bulkWireTemplateID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in  pageOffset int,
in  pageSize int
)
BEGIN
    
    SET @qfilter = concat("`bulkwiretemplatetransactdetails`.`bulkWireTemplateID` = \"", bulkWireTemplateID, "\" AND `bulkwiretemplatetransactdetails`.`softdeleteflag` = 0 ");
      
    SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'transactionDate', sortByParam);
    SET sortByParam = if(sortByParam = 'username', 'firstname,lastname', sortByParam);
    SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
    
    SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));   
    
    SET @orderBy = concat(" ORDER BY ", sortByParam , " ", sortOrder);
    
    SET @paginationQuery = if((pageOffset != NULL OR pageOffset != "" AND pageSize != NULL OR pageSize != ""),
							concat(" LIMIT ",pageSize, " OFFSET " ,pageOffset),
                            ''); 
    
    SET @searchQuery = concat("(`bulkwiretemplatetransactdetails`.`transactionDate` LIKE ",searchString," OR `bulkwiretemplatetransactdetails`.`totalCountOfTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");
    
	SET @defaultFilter = concat(@qfilter, @orderBy , @paginationQuery);

	SET @searchFilter = concat(@qfilter," AND ",@searchQuery,@orderBy);

    SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

	SET @select_statement = concat("SELECT 
	    bulkwiretemplatetransactdetails.bulkWireTransactionID,
		bulkwiretemplatetransactdetails.bulkWireTemplateID, 
		bulkwiretemplatetransactdetails.initiatedBy, 
		bulkwiretemplatetransactdetails.createdts,
		bulkwiretemplatetransactdetails.lastmodifiedts,
		bulkwiretemplatetransactdetails.transactionDate, 
		bulkwiretemplatetransactdetails.totalCountOfTransactions,
		bulkwiretemplatetransactdetails.totalCountOfDomesticTransactions,
		bulkwiretemplatetransactdetails.totalCountOfInternationalTransactions,
		customer.FirstName as firstname,
		customer.LastName as lastname
    FROM
        (`bulkwiretemplatetransactdetails`
		LEFT JOIN `customer` ON (`bulkwiretemplatetransactdetails`.`initiatedBy` = `customer`.`id`))
		WHERE ", @filter);
		
 -- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_bulkWireTemplateTransactionsExecution_details_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkWireTemplateTransactionsExecution_details_proc`( 
	in BulkWireTemplateExecution_id varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in statusFilter varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	in sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `isDomesticPermitted` INT,
	IN `isInternationalPermitted` INT
)
BEGIN

SET @query1= concat("`wiretransfers`.`wireTemplateExecution_id` = ", BulkWireTemplateExecution_id," AND `wiretransfers`.`softdeleteflag` = 0 ");
SET @query2= concat("`wiretransfers`.`wireTemplateExecution_id` = ", BulkWireTemplateExecution_id," AND `wiretransfers`.`status` ='",statusFilter,"' AND `wiretransfers`.`softdeleteflag` = 0 ");
SET @query3= concat("`wiretransfers`.`wireTemplateExecution_id` = ", BulkWireTemplateExecution_id," AND `wiretransfers`.`status` in ('Failed','Denied')"," AND `wiretransfers`.`softdeleteflag` = 0 ");

SET sortByParam =if (sortByParam = "" OR sortByParam = NULL, 'transactionId', sortByParam);
SET sortByParam = if(sortByParam = 'payeeName', 'wiretransfers.payeeName', sortByParam);
SET sortOrder = if (sortOrder = "" OR sortOrder = NULL, 'ASC', sortOrder);
SET searchString = if (searchString = "" OR searchString = NULL, "", concat("'%",searchString,"%'"));

SET @orderBy = concat(" ORDER BY ", sortByParam, " ", sortOrder);
SET @searchQuery = concat("(`wiretransfers`.`amount` LIKE ",searchString," OR `wiretransfers`.`notes` LIKE ",searchString," OR `wiretransfers`.`fromAccountNumber` LIKE ",searchString," OR `wiretransfers`.`payeeAccountNumber` LIKE ",searchString," OR `wiretransfers`.`transactionType` LIKE ",searchString," OR `onetimepayee`.`payeeName` LIKE ",searchString," OR `onetimepayee`.`payeeType` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine1` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine2` LIKE ",searchString," OR `onetimepayee`.`cityName` LIKE ",searchString," OR `onetimepayee`.`state` LIKE ",searchString," OR `onetimepayee`.`zipCode` LIKE ",searchString," OR `onetimepayee`.`bankName` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine1` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine2` LIKE ",searchString," OR `onetimepayee`.`bankZip` LIKE ",searchString," OR `onetimepayee`.`bankState` LIKE ",searchString," OR `onetimepayee`.`bankCity` LIKE ",searchString," OR  `onetimepayee`.`routingNumber` LIKE ",searchString," OR `onetimepayee`.`internationalRoutingCode` LIKE ",searchString," OR `onetimepayee`.`swiftCode` LIKE ",searchString,")");
SET @statusQuery = if(statusFilter="Failed", @query3, @query2);
SET @finalQueryFilter = if(statusFilter="", @query1, @statusQuery);
SET @defaultFilter = concat(@finalQueryFilter, @orderBy);
SET @searchFilter = concat(@finalQueryFilter, " AND ", @searchQuery, @orderBy);
SET @filter = if (searchString = "", @defaultFilter, @searchFilter);

SET @DomQuery = CONCAT("SELECT COUNT(*) FROM (`wiretransfers` LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) WHERE onetimepayee.wireAccountType = 'Domestic' AND ", @filter, " INTO @DomCount;");
PREPARE stmt1 FROM @DomQuery; EXECUTE stmt1; DEALLOCATE PREPARE stmt1;

SET @InternationalQuery = CONCAT("SELECT COUNT(*) FROM (`wiretransfers` LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) WHERE onetimepayee.wireAccountType = 'International' AND ", @filter, " INTO @InternationalCount;");
PREPARE stmt2 FROM @InternationalQuery; EXECUTE stmt2; DEALLOCATE PREPARE stmt2;

IF (isDomesticPermitted = 0 AND isInternationalPermitted = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSEIF (isDomesticPermitted = 0 AND @InternationalCount = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSEIF (isInternationalPermitted = 0 AND @DomCount = 0) THEN
	SELECT "UNAUTHORIZED" AS ErrorMessage;
ELSE
	set @select_statement = concat("SELECT 
			wiretransfers.transactionId,
			wiretransfers.confirmationNumber,
			wiretransfers.requestId,
			wiretransfers.status,
			wiretransfers.onetime_id,
			wiretransfers.wireTemplateExecution_id,
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
			FROM (`wiretransfers` 
			LEFT JOIN `onetimepayee` ON(`wiretransfers`.`onetime_id` = `onetimepayee`.`onetime_id`)) 
			WHERE ", @filter);
			
	 -- select @select_statement;
		PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END IF; 
END$$
DELIMITER ;

DROP VIEW IF EXISTS `customeraccountsview`;
CREATE VIEW `customeraccountsview` AS
    SELECT DISTINCT
        `accounts`.`Membership_id` AS `Membership_id`,
        `accounts`.`TaxId` AS `Taxid`,
        `customeraccounts`.`Customer_id` AS `Customer_id`,
        `customeraccounts`.`Customer_id` AS `User_id`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`isBusinessAccount` AS `isBusinessAccount`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`UserName` AS `userName`,
        `accounts`.`CurrencyCode` AS `currencyCode`,
        `accounts`.`AccountHolder` AS `accountHolder`,
        `accounts`.`error` AS `error`,
        `accounts`.`Address` AS `Address`,
        `accounts`.`Scheme` AS `Scheme`,
        `accounts`.`Number` AS `number`,
        `accounts`.`AvailableBalance` AS `availableBalance`,
        `accounts`.`CurrentBalance` AS `currentBalance`,
        `accounts`.`InterestRate` AS `interestRate`,
        `accounts`.`AvailableCredit` AS `availableCredit`,
        `accounts`.`MinimumDue` AS `minimumDue`,
        `accounts`.`DueDate` AS `dueDate`,
        `accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
        `accounts`.`ClosingDate` AS `closingDate`,
        `accounts`.`PaymentTerm` AS `paymentTerm`,
        `accounts`.`OpeningDate` AS `openingDate`,
        `accounts`.`MaturityDate` AS `maturityDate`,
        `accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
        `accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,
        `accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
        `accounts`.`DividendRate` AS `dividendRate`,
        `accounts`.`DividendYTD` AS `dividendYTD`,
        `accounts`.`EStatementmentEnable` AS `eStatementEnable`,
        `customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,
        `accounts`.`StatusDesc` AS `statusDesc`,
        `accounts`.`NickName` AS `nickName`,
        `accounts`.`OriginalAmount` AS `originalAmount`,
        `accounts`.`OutstandingBalance` AS `outstandingBalance`,
        `accounts`.`PaymentDue` AS `paymentDue`,
        `accounts`.`PaymentMethod` AS `paymentMethod`,
        `accounts`.`SwiftCode` AS `swiftCode`,
        `accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
        `accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
        `accounts`.`RoutingNumber` AS `routingNumber`,
        `accounts`.`SupportBillPay` AS `supportBillPay`,
        `accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
        `accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
        `accounts`.`SupportTransferTo` AS `supportTransferTo`,
        `accounts`.`SupportDeposit` AS `supportDeposit`,
        `accounts`.`UnpaidInterest` AS `unpaidInterest`,
        `accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
        `accounts`.`principalBalance` AS `principalBalance`,
        `accounts`.`PrincipalValue` AS `principalValue`,
        `accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
        `accounts`.`phone` AS `phoneId`,
        `accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
        `accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
        `accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
        `accounts`.`LastPaymentDate` AS `lastPaymentDate`,
        `accounts`.`LastStatementBalance` AS `lastStatementBalance`,
        `accounts`.`LateFeesDue` AS `lateFeesDue`,
        `accounts`.`maturityAmount` AS `maturityAmount`,
        `accounts`.`MaturityOption` AS `maturityOption`,
        `accounts`.`payoffAmount` AS `payoffAmount`,
        `accounts`.`PayOffCharge` AS `payOffCharge`,
        `accounts`.`PendingDeposit` AS `pendingDeposit`,
        `accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
        `accounts`.`JointHolders` AS `jointHolders`,
        `accounts`.`IsPFM` AS `isPFM`,
        `accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
        `accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
        `accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
        `accounts`.`InterestEarned` AS `interestEarned`,
        `accounts`.`CurrentAmountDue` AS `currentAmountDue`,
        `accounts`.`CreditLimit` AS `creditLimit`,
        `accounts`.`CreditCardNumber` AS `creditCardNumber`,
        `accounts`.`BsbNum` AS `bsbNum`,
        `accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
        `accounts`.`BondInterest` AS `bondInterest`,
        `accounts`.`AvailablePoints` AS `availablePoints`,
        `accounts`.`AccountName` AS `accountName`,
        `accounts`.`email` AS `email`,
        `accounts`.`IBAN` AS `IBAN`,
        `accounts`.`adminProductId` AS `adminProductId`,
        `accounts`.`UpdatedBy` AS `UpdatedBy`,
        `accounts`.`LastUpdated` AS `LastUpdated`,
        `accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,
        `bank`.`Description` AS `bankname`,
        `accounts`.`AccountPreference` AS `accountPreference`,
        `accounttype`.`transactionLimit` AS `transactionLimit`,
        `accounttype`.`transferLimit` AS `transferLimit`,
        `accounttype`.`rates` AS `rates`,
        `accounttype`.`termsAndConditions` AS `termsAndConditions`,
        `accounttype`.`TypeDescription` AS `typeDescription`,
        `accounttype`.`supportChecks` AS `supportChecks`,
        `accounttype`.`displayName` AS `displayName`,
        `accounts`.`accountSubType` AS `accountSubType`,
        `accounts`.`description` AS `description`,
        `accounts`.`schemeName` AS `schemeName`,
        `accounts`.`identification` AS `identification`,
        `accounts`.`secondaryIdentification` AS `secondaryIdentification`,
        `accounts`.`servicerSchemeName` AS `servicerSchemeName`,
        `accounts`.`servicerIdentification` AS `servicerIdentification`,
        `accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,
        `accounts`.`dataType` AS `dataType`,
        `accounts`.`dataDateTime` AS `dataDateTime`,
        `accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
        `accounts`.`dataCreditLineType` AS `dataCreditLineType`,
        `accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
        `accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`
    FROM
        (((((`accounts`
        JOIN `customeraccounts`)
        JOIN `accounttype`)
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `membership` ON ((`membership`.`id` = `membershipaccounts`.`membershipId`)))
        LEFT JOIN `bank` ON ((`accounts`.`Bank_id` = `bank`.`id`)))
    WHERE
        ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`)
            AND (`accounts`.`Type_id` = `accounttype`.`TypeID`));


        
        
DROP procedure IF EXISTS `customeractions_create_proc`;

DELIMITER $$
CREATE PROCEDURE `customeractions_create_proc`(
IN _customerActionsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _businessTypeId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
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
         FOR (SELECT Account_id FROM accounts WHERE FIND_IN_SET(Account_id,_accountsCSV));
DECLARE actions CURSOR 
      FOR (select id from featureaction where FIND_IN_SET(id,@validActionsList));
DECLARE limits CURSOR 
		FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci );
DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;

IF(ISNULL(_groupId) OR _groupId='' ) THEN
SET @groupId = (SELECT Group_id FROM groupbusinesstype WHERE BusinessType_id COLLATE utf8_general_ci = _businessTypeId 
                       AND isDefaultGroup = true);
ELSE
SET @groupId = _groupId ;
END IF;
 
SET @groupId = (SELECT id FROM membergroup WHERE id = @groupId AND Type_id = 'TYPE_ID_BUSINESS' 
                       AND Status_id = 'SID_ACTIVE' );
                       
SET @validActionsList = (SELECT group_concat(distinct Action_id SEPARATOR ",") FROM groupactionlimit WHERE Group_id COLLATE utf8_general_ci =@groupId COLLATE utf8_general_ci 
                       AND (FIND_IN_SET(Action_id,_customerActionsCSV)));
                       
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
               AND LimitType_id COLLATE utf8_general_ci = limitId );
               
			   if (limitId='MAX_TRANSACTION_LIMIT') THEN 
			   SET actualLimitId = 'AUTO_DENIED_TRANSACTION_LIMIT';
			   ELSEIF (limitId='MIN_TRANSACTION_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_TRANSACTION_LIMIT';
               ELSEIF (limitId='DAILY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_DAILY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,0.00);
                SET actualLimitId = 'AUTO_DENIED_DAILY_LIMIT';
               ELSEIF (limitId='WEEKLY_LIMIT') THEN 
               SET actualLimitId = 'PRE_APPROVED_WEEKLY_LIMIT';
                SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,0.00);
               SET actualLimitId = 'AUTO_DENIED_WEEKLY_LIMIT';
               END IF;
               
               
               
			   SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed,LimitType_id,value) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true,actualLimitId,@limitvalue);
                SET entryStatus = 1;
			   ITERATE  getlimit;
			END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
			      INSERT IGNORE INTO customeraction(id,RoleType_id,Customer_id,Action_id,Account_id,isAllowed) VALUES
		        (@id,'TYPE_ID_BUSINESS',_customerId,featureActionId,accountId,true);
            END IF;
			set actionslist = CONCAT(featureActionId,",",actionslist);
			ITERATE  getAction;
        END IF;
  END LOOP getAction;
  CLOSE actions;
  ITERATE  getAccount;
 END IF;
END LOOP getAccount;
CLOSE accounts;

SET actionslist = (select SUBSTRING(actionslist FROM 1 FOR (CHAR_LENGTH(actionslist)-1)));

select actionslist;
END$$

DELIMITER ;


DROP procedure IF EXISTS `get_valid_orgaccounts_list_proc`;

DELIMITER $$
CREATE PROCEDURE `get_valid_orgaccounts_list_proc`(
IN _accountsList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 4294967295;
SET @list = (SELECT _accountsList);
SET @NONDBXAccounts = '';
SET @tableAccountsList = (SELECT group_concat(Account_id SEPARATOR ",") from accounts);

iterator: LOOP
  IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
    LEAVE iterator;
  END IF;
    SET @next = SUBSTRING_INDEX(@list,',',1);
    SET @nextlen = LENGTH(@next);
    SET @value = TRIM(@next);
    IF NOT find_in_set(@value,@tableAccountsList) THEN 
        IF @NONDBXAccounts='' THEN
           SET @NONDBXAccounts = @value;
		ELSE 
           SET @NONDBXAccounts = CONCAT(@NONDBXAccounts,",",@value);
	    END IF;
	END IF;
    SET @list = INSERT(@list,1,@nextlen + 1,'');
END LOOP;

SET @DBXAccounts = (SELECT group_concat(Account_id SEPARATOR ",") from accounts WHERE FIND_IN_SET(Account_id,_accountsList)
    AND (ISNULL(Organization_id)>0 OR Organization_id=""));
    
    
IF(ISNULL(@DBXAccounts) OR @DBXAccounts='' ) THEN
SELECT @NONDBXAccounts AS accountsList;
ELSEIF(ISNULL(@NONDBXAccounts) OR @NONDBXAccounts='') THEN
SELECT @DBXAccounts AS accountsList;
ELSE 
SELECT CONCAT(@DBXAccounts,",",@NONDBXAccounts) AS accountsList;
END IF;


END$$

DELIMITER ;

DROP VIEW IF EXISTS `customeraccountsview`;
CREATE VIEW `customeraccountsview` AS
    SELECT DISTINCT
        `accounts`.`Membership_id` AS `Membership_id`,
        `accounts`.`TaxId` AS `Taxid`,
        `customeraccounts`.`Customer_id` AS `Customer_id`,
        `customeraccounts`.`Customer_id` AS `User_id`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`isBusinessAccount` AS `isBusinessAccount`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`UserName` AS `userName`,
        `accounts`.`CurrencyCode` AS `currencyCode`,
        `accounts`.`AccountHolder` AS `accountHolder`,
        `accounts`.`error` AS `error`,
        `accounts`.`Address` AS `Address`,
        `accounts`.`Scheme` AS `Scheme`,
        `accounts`.`Number` AS `number`,
        `accounts`.`AvailableBalance` AS `availableBalance`,
        `accounts`.`CurrentBalance` AS `currentBalance`,
        `accounts`.`InterestRate` AS `interestRate`,
        `accounts`.`AvailableCredit` AS `availableCredit`,
        `accounts`.`MinimumDue` AS `minimumDue`,
        `accounts`.`DueDate` AS `dueDate`,
        `accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
        `accounts`.`ClosingDate` AS `closingDate`,
        `accounts`.`PaymentTerm` AS `paymentTerm`,
        `accounts`.`OpeningDate` AS `openingDate`,
        `accounts`.`MaturityDate` AS `maturityDate`,
        `accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
        `accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,
        `accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
        `accounts`.`DividendRate` AS `dividendRate`,
        `accounts`.`DividendYTD` AS `dividendYTD`,
        `accounts`.`EStatementmentEnable` AS `eStatementEnable`,
        `customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,
        `accounts`.`StatusDesc` AS `statusDesc`,
        `accounts`.`NickName` AS `nickName`,
        `accounts`.`OriginalAmount` AS `originalAmount`,
        `accounts`.`OutstandingBalance` AS `outstandingBalance`,
        `accounts`.`PaymentDue` AS `paymentDue`,
        `accounts`.`PaymentMethod` AS `paymentMethod`,
        `accounts`.`SwiftCode` AS `swiftCode`,
        `accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
        `accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
        `accounts`.`RoutingNumber` AS `routingNumber`,
        `accounts`.`SupportBillPay` AS `supportBillPay`,
        `accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
        `accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
        `accounts`.`SupportTransferTo` AS `supportTransferTo`,
        `accounts`.`SupportDeposit` AS `supportDeposit`,
        `accounts`.`UnpaidInterest` AS `unpaidInterest`,
        `accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
        `accounts`.`principalBalance` AS `principalBalance`,
        `accounts`.`PrincipalValue` AS `principalValue`,
        `accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
        `accounts`.`phone` AS `phoneId`,
        `accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
        `accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
        `accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
        `accounts`.`LastPaymentDate` AS `lastPaymentDate`,
        `accounts`.`LastStatementBalance` AS `lastStatementBalance`,
        `accounts`.`LateFeesDue` AS `lateFeesDue`,
        `accounts`.`maturityAmount` AS `maturityAmount`,
        `accounts`.`MaturityOption` AS `maturityOption`,
        `accounts`.`payoffAmount` AS `payoffAmount`,
        `accounts`.`PayOffCharge` AS `payOffCharge`,
        `accounts`.`PendingDeposit` AS `pendingDeposit`,
        `accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
        `accounts`.`JointHolders` AS `jointHolders`,
        `accounts`.`IsPFM` AS `isPFM`,
        `accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
        `accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
        `accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
        `accounts`.`InterestEarned` AS `interestEarned`,
        `accounts`.`CurrentAmountDue` AS `currentAmountDue`,
        `accounts`.`CreditLimit` AS `creditLimit`,
        `accounts`.`CreditCardNumber` AS `creditCardNumber`,
        `accounts`.`BsbNum` AS `bsbNum`,
        `accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
        `accounts`.`BondInterest` AS `bondInterest`,
        `accounts`.`AvailablePoints` AS `availablePoints`,
        `accounts`.`AccountName` AS `accountName`,
        `accounts`.`email` AS `email`,
        `accounts`.`IBAN` AS `IBAN`,
        `accounts`.`adminProductId` AS `adminProductId`,
        `accounts`.`UpdatedBy` AS `UpdatedBy`,
        `accounts`.`LastUpdated` AS `LastUpdated`,
        `accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,
        `bank`.`Description` AS `bankname`,
        `accounts`.`AccountPreference` AS `accountPreference`,
        `accounttype`.`transactionLimit` AS `transactionLimit`,
        `accounttype`.`transferLimit` AS `transferLimit`,
        `accounttype`.`rates` AS `rates`,
        `accounttype`.`termsAndConditions` AS `termsAndConditions`,
        `accounttype`.`TypeDescription` AS `typeDescription`,
        `accounttype`.`supportChecks` AS `supportChecks`,
        `accounttype`.`displayName` AS `displayName`,
        `accounts`.`accountSubType` AS `accountSubType`,
        `accounts`.`description` AS `description`,
        `accounts`.`schemeName` AS `schemeName`,
        `accounts`.`identification` AS `identification`,
        `accounts`.`secondaryIdentification` AS `secondaryIdentification`,
        `accounts`.`servicerSchemeName` AS `servicerSchemeName`,
        `accounts`.`servicerIdentification` AS `servicerIdentification`,
        `accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,
        `accounts`.`dataType` AS `dataType`,
        `accounts`.`dataDateTime` AS `dataDateTime`,
        `accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
        `accounts`.`dataCreditLineType` AS `dataCreditLineType`,
        `accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
        `accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`
    FROM
        (((((`accounts`
        JOIN `customeraccounts`)
        JOIN `accounttype`)
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `membership` ON ((`membership`.`id` = `membershipaccounts`.`membershipId`)))
        LEFT JOIN `bank` ON ((`accounts`.`Bank_id` = `bank`.`id`)))
    WHERE
        ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`)
            AND (`accounts`.`Type_id` = `accounttype`.`TypeID`));

DROP PROCEDURE IF EXISTS `fetch_unselectedPayees_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_unselectedPayees_proc`(
	IN `_bulkwiretemplateID` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `User_Id` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortByParam` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `sortOrder` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `searchString` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `isDomesticPermitted` INT,
	IN `isInternationalPermitted` INT
)
proc_label:BEGIN

	SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'nickName', sortByParam);
	SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
	SET @searchAndOrderBy = CONCAT(" ORDER BY ", sortByParam , " ", sortOrder);

	IF searchString != "" OR searchString IS NOT NULL THEN
		SET searchString = CONCAT("'%",searchString,"%'");
		SET @searchQuery = CONCAT(" AND (`name` LIKE ",searchString," OR `bankName` LIKE ",searchString," OR `wireAccountType` LIKE ",searchString," OR `firstName` LIKE ",searchString," OR `lastName` LIKE ",searchString," OR `nickName` LIKE ",searchString,")");
		SET @searchAndOrderBy = CONCAT(@searchQuery, " ", @searchAndOrderBy);
	END IF;
	
	SET @permissionQuery = "";
	
	IF isDomesticPermitted = 0 AND isInternationalPermitted = 1 THEN
		SET @permissionQuery = " AND wireAccountType = 'International' ";
	ELSEIF isDomesticPermitted = 1 AND isInternationalPermitted = 0 THEN
		SET @permissionQuery = " AND wireAccountType = 'Domestic' ";
	ELSEIF isDomesticPermitted = 1 AND isInternationalPermitted = 1 THEN
		SET @permissionQuery = " AND wireAccountType IN ('Domestic', 'International') ";
	ELSE
		SELECT "";
		LEAVE proc_label;
	END IF;
	
	SET @query1 = CONCAT("SELECT * FROM payee WHERE isWiredRecepient = 1 AND User_Id = '",User_Id,"' AND Id NOT IN (SELECT payeeId FROM bulkwiretemplatelineitems WHERE bulkWireTemplateID = '",_bulkwiretemplateID,"' AND templateRecipientCategory = 'EXISTINGRECIPIENT' AND softdeleteflag = 0) ",@permissionQuery, @searchAndOrderBy);
	PREPARE sql_query FROM @query1;
	EXECUTE sql_query;

END$$
DELIMITER ;

DROP VIEW IF EXISTS `payeetransactionview`;
CREATE VIEW `payeetransactionview` AS select `t`.`Id` AS `Id`,
`t`.`isScheduled` AS `isScheduled`,`t`.`Customer_id` AS `Customer_id`,
`t`.`ExpenseCategory_id` AS `ExpenseCategory_id`,
`t`.`Payee_id` AS `Payee_id`,
`t`.`Bill_id` AS `Bill_id`,
`t`.`Type_id` AS `Type_id`,
`t`.`Reference_id` AS `Reference_id`,
`t`.`fromAccountNumber` AS `fromAccountNumber`,
`t`.`fromAccountBalance` AS `fromAccountBalance`,
`t`.`isTypeBusiness` AS `isTypeBusiness`,
`t`.`toAccountNumber` AS `toAccountNumber`,
`t`.`toAccountBalance` AS `toAccountBalance`,
`t`.`amount` AS `amount`,
`t`.`Status_id` AS `Status_id`,
`t`.`statusDesc` AS `statusDesc`,
`t`.`notes` AS `notes`,
`t`.`checkNumber` AS `checkNumber`,
`t`.`imageURL1` AS `imageURL1`,
`t`.`imageURL2` AS `imageURL2`,
`t`.`hasDepositImage` AS `hasDepositImage`,
`t`.`description` AS `description`,
`t`.`scheduledDate` AS `scheduledDate`,
`t`.`transactionDate` AS `transactionDate`,
`t`.`createdDate` AS `createdDate`,
`t`.`transactionComments` AS `transactionComments`,
`t`.`toExternalAccountNumber` AS `toExternalAccountNumber`,
`t`.`Person_Id` AS `Person_Id`,
`t`.`frequencyType` AS `frequencyType`,
`t`.`numberOfRecurrences` AS `numberOfRecurrences`,
`t`.`frequencyStartDate` AS `frequencyStartDate`,
`t`.`frequencyEndDate` AS `frequencyEndDate`,
`t`.`checkImage` AS `checkImage`,
`t`.`checkImageBack` AS `checkImageBack`,
`t`.`cashlessOTPValidDate` AS `cashlessOTPValidDate`,
`t`.`cashlessOTP` AS `cashlessOTP`,
`t`.`cashlessPhone` AS `cashlessPhone`,
`t`.`cashlessEmail` AS `cashlessEmail`,
`t`.`cashlessPersonName` AS `cashlessPersonName`,
`t`.`cashlessMode` AS `cashlessMode`,
`t`.`cashlessSecurityCode` AS `cashlessSecurityCode`,
`t`.`cashWithdrawalTransactionStatus` AS `cashWithdrawalTransactionStatus`,
`t`.`cashlessPin` AS `cashlessPin`,
`t`.`category` AS `category`,
`t`.`billCategory` AS `billCategory`,
`t`.`recurrenceDesc` AS `recurrenceDesc`,
`t`.`deliverBy` AS `deliverBy`,
`t`.`p2pContact` AS `p2pContact`,
`t`.`p2pRequiredDate` AS `p2pRequiredDate`,
`t`.`requestCreatedDate` AS `requestCreatedDate`,
`t`.`penaltyFlag` AS `penaltyFlag`,
`t`.`payoffFlag` AS `payoffFlag`,
`t`.`viewReportLink` AS `viewReportLink`,
`t`.`isPaypersonDeleted` AS `isPaypersonDeleted`,
`t`.`fee` AS `fee`,`p`.`Id` AS `payeeId`,
`p`.`Type_id` AS `payeeType`,`p`.`name` AS `name`,
`p`.`nickName` AS `nickName`,`p`.`phone` AS `phone`,
`p`.`email` AS `email`,`p`.`accountNumber` AS `accountNumber`,
`p`.`billerId` AS `billerId`,
`p`.`billermaster_id` AS `billermaster_id`,
`p`.`softDelete` AS `softDelete`,
`p`.`User_Id` AS `User_Id`,
`p`.`addressLine1` AS `addressLine1`,
`p`.`addressLine2` AS `addressLine2`,
`p`.`eBillEnable` AS `eBillEnable`,
`p`.`phoneExtension` AS `phoneExtension`,
`p`.`phoneCountryCode` AS `phoneCountryCode`,
`bm`.`ebillSupport` AS `ebillSupport`,
`tt`.`description` AS `transactionType` from (((`transaction` `t` left join `payee` `p` on((`t`.`Payee_id` = `p`.`Id`))) join `transactiontype` `tt` on((`t`.`Type_id` = `tt`.`Id`))) left join `billermaster` `bm` on((`bm`.`id` = `p`.`billermaster_id`)));

DROP VIEW IF EXISTS `paypersontransactionview`;
CREATE VIEW `paypersontransactionview` AS select `t`.`Id` AS `Id`,
`t`.`isScheduled` AS `isScheduled`,
`t`.`Customer_id` AS `Customer_id`,
`t`.`ExpenseCategory_id` AS `ExpenseCategory_id`,
`t`.`Payee_id` AS `Payee_id`,`t`.`Bill_id` AS `Bill_id`,
`t`.`Type_id` AS `Type_id`,`t`.`Reference_id` AS `Reference_id`,
`t`.`fromAccountNumber` AS `fromAccountNumber`,
`t`.`fromAccountBalance` AS `fromAccountBalance`,
`t`.`isTypeBusiness` AS `isTypeBusiness`,
`t`.`toAccountNumber` AS `toAccountNumber`,
`t`.`toAccountBalance` AS `toAccountBalance`,
`t`.`amount` AS `amount`,`t`.`Status_id` AS `Status_id`,
`t`.`statusDesc` AS `statusDesc`,`t`.`notes` AS `notes`,
`t`.`checkNumber` AS `checkNumber`,
`t`.`imageURL1` AS `imageURL1`,
`t`.`imageURL2` AS `imageURL2`,
`t`.`hasDepositImage` AS `hasDepositImage`,
`t`.`description` AS `description`,
`t`.`scheduledDate` AS `scheduledDate`,
`t`.`transactionDate` AS `transactionDate`,
`t`.`createdDate` AS `createdDate`,
`t`.`transactionComments` AS `transactionComments`,
`t`.`toExternalAccountNumber` AS `toExternalAccountNumber`,
`t`.`Person_Id` AS `Person_Id`,
`t`.`frequencyType` AS `frequencyType`,
`t`.`numberOfRecurrences` AS `numberOfRecurrences`,
`t`.`frequencyStartDate` AS `frequencyStartDate`,
`t`.`frequencyEndDate` AS `frequencyEndDate`,
`t`.`checkImage` AS `checkImage`,
`t`.`checkImageBack` AS `checkImageBack`,
`t`.`cashlessOTPValidDate` AS `cashlessOTPValidDate`,
`t`.`cashlessOTP` AS `cashlessOTP`,
`t`.`cashlessPhone` AS `cashlessPhone`,
`t`.`cashlessEmail` AS `cashlessEmail`,
`t`.`cashlessPersonName` AS `cashlessPersonName`,
`t`.`cashlessMode` AS `cashlessMode`,
`t`.`cashlessSecurityCode` AS `cashlessSecurityCode`,
`t`.`cashWithdrawalTransactionStatus` AS `cashWithdrawalTransactionStatus`,
`t`.`cashlessPin` AS `cashlessPin`,
`t`.`category` AS `category`,
`t`.`billCategory` AS `billCategory`,
`t`.`recurrenceDesc` AS `recurrenceDesc`,
`t`.`deliverBy` AS `deliverBy`,
`t`.`p2pContact` AS `p2pContact`,
`t`.`p2pRequiredDate` AS `p2pRequiredDate`,
`t`.`requestCreatedDate` AS `requestCreatedDate`,
`t`.`penaltyFlag` AS `penaltyFlag`,
`t`.`payoffFlag` AS `payoffFlag`,
`t`.`viewReportLink` AS `viewReportLink`,
`t`.`isPaypersonDeleted` AS `isPaypersonDeleted`,
`t`.`fee` AS `fee`,`p`.`id` AS `paypersonID`,
`p`.`firstName` AS `firstName`,
`p`.`lastName` AS `lastName`,`p`.`phone` AS `phone`,
`p`.`email` AS `email`,`p`.`User_id` AS `User_id`,
`p`.`secondaryEmail` AS `secondaryEmail`,
`p`.`secondoryPhoneNumber` AS `secondoryPhoneNumber`,
`p`.`primaryContactForSending` AS `primaryContactForSending`,
`p`.`nickName` AS `nickName`,`p`.`isSoftDelete` AS `isSoftDelete`,
`tt`.`description` AS `transactionType` from ((`transaction` `t` left join `payperson` `p` on((`t`.`Person_Id` = `p`.`id`))) join `transactiontype` `tt` on((`t`.`Type_id` = `tt`.`Id`)));

DROP VIEW IF EXISTS `wirecustaccounttransactionview`;
 CREATE VIEW `wirecustaccounttransactionview` AS select distinct `customeraccounts`.`Customer_id` AS `User_id`,
 `transaction`.`Id` AS `transactionId`,
 `transaction`.`Type_id` AS `transactiontype`,
 `transaction`.`Customer_id` AS `Customer_id`,
 `transaction`.`ExpenseCategory_id` AS `ExpenseCategory_id`,
 `transaction`.`billid` AS `Bill_id`,
 `transaction`.`Reference_id` AS `Reference_id`,
 `transaction`.`fromAccountNumber` AS `fromAccountNumber`,
 `transaction`.`fromAccountBalance` AS `fromAccountBalance`,
 `transaction`.`toAccountNumber` AS `toAccountNumber`,
 `transaction`.`toAccountBalance` AS `toAccountBalance`,
 `transaction`.`amount` AS `amount`,`transaction`.`Status_id` AS `Status_id`,
 `transaction`.`statusDesc` AS `statusDesc`,
 `transaction`.`isTypeBusiness` AS `isTypeBusiness`,
 `transaction`.`isScheduled` AS `isScheduled`,
 `transaction`.`category` AS `category`,
 `transaction`.`billCategory` AS `billCategory`,
 `transaction`.`toExternalAccountNumber` AS `ExternalAccountNumber`,
 `transaction`.`Person_Id` AS `Person_Id`,
 `transaction`.`frequencyType` AS `frequencyType`,
 `transaction`.`createdDate` AS `createdDate`,
 `transaction`.`cashlessEmail` AS `cashlessEmail`,
 `transaction`.`cashlessMode` AS `cashlessMode`,
 `transaction`.`cashlessOTP` AS `cashlessOTP`,
 `transaction`.`cashlessOTPValidDate` AS `cashlessOTPValidDate`,
 `transaction`.`cashlessPersonName` AS `cashlessPersonName`,
 `transaction`.`cashlessPhone` AS `cashlessPhone`,
 `transaction`.`cashlessSecurityCode` AS `cashlessSecurityCode`,
 `transaction`.`cashWithdrawalTransactionStatus` AS `cashWithdrawalTransactionStatus`,
 `transaction`.`frequencyEndDate` AS `frequencyEndDate`,
 `transaction`.`frequencyStartDate` AS `frequencyStartDate`,
 `transaction`.`hasDepositImage` AS `hasDepositImage`,
 `transaction`.`Payee_id` AS `payeeId`,
 `transaction`.`payeeName` AS `payeeName`,
 `transaction`.`p2pContact` AS `p2pContact`,
 `transaction`.`Person_Id` AS `personId`,
 `transaction`.`recurrenceDesc` AS `recurrenceDesc`,
 `transaction`.`numberOfRecurrences` AS `numberOfRecurrences`,
 `transaction`.`scheduledDate` AS `scheduledDate`,
 `transaction`.`transactionComments` AS `transactionComments`,
 `transaction`.`notes` AS `transactionsNotes`,
 `transaction`.`description` AS `transDescription`,
 `transaction`.`transactionDate` AS `transactionDate`,
 `transaction`.`frontImage1` AS `frontImage1`,
 `transaction`.`frontImage2` AS `frontImage2`,
 `transaction`.`backImage1` AS `backImage1`,
 `transaction`.`backImage2` AS `backImage2`,
 `transaction`.`checkDesc` AS `checkDesc`,
 `transaction`.`checkNumber1` AS `checkNumber1`,
 `transaction`.`checkNumber2` AS `checkNumber2`,
 `transaction`.`checkNumber` AS `checkNumber`,
 `transaction`.`checkReason` AS `checkReason`,
 `transaction`.`requestValidity` AS `requestValidity`,
 `transaction`.`checkDateOfIssue` AS `checkDateOfIssue`,
 `transaction`.`bankName1` AS `bankName1`,
 `transaction`.`bankName2` AS `bankName2`,
 `transaction`.`withdrawlAmount1` AS `withdrawlAmount1`,
 `transaction`.`withdrawlAmount2` AS `withdrawlAmount2`,
 `transaction`.`cashAmount` AS `cashAmount`,
 `transaction`.`amountRecieved` AS `amountRecieved`,
 `transaction`.`payeeCurrency` AS `payeeCurrency`,
 `transaction`.`fee` AS `fee`,
 `transaction`.`isDisputed` AS `isDisputed`,
 `transaction`.`disputeReason` AS `disputeReason`,
 `transaction`.`disputeDescription` AS `disputeDescription`,
 `transaction`.`disputeDate` AS `disputeDate`,
 `transaction`.`disputeStatus` AS `disputeStatus`,
 `transactiontype`.`description` AS `description`,
 `payee`.`nickName` AS `nickName`,
 `payee`.`accountNumber` AS `payeeAccountNumber`,
 `payee`.`Type_id` AS `payeeType`,
 `payee`.`addressLine1` AS `payeeAddressLine2`,
 `payee`.`addressLine2` AS `payeeAddressLine1` from (((`customeraccounts` join `transaction`) join `transactiontype`) join `payee`) where (((`customeraccounts`.`Account_id` = `transaction`.`fromAccountNumber`) or (`customeraccounts`.`Account_id` = `transaction`.`toAccountNumber`)) and (`transaction`.`Type_id` = `transactiontype`.`Id`) and (`transaction`.`Payee_id` = `payee`.`Id`)) order by `transaction`.`createdDate` desc;
 
DROP PROCEDURE IF EXISTS `fetch_bwtemplate_domesticInternationalCount_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bwtemplate_domesticInternationalCount_proc`(
	IN `_bulkwiretemplateID` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_bulkwiretemplatelineitemIDs` TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

	IF _bulkwiretemplatelineitemIDs != "" AND _bulkwiretemplatelineitemIDs IS NOT NULL THEN
		SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE FIND_IN_SET(bulkWireTemplateLineItemID,_bulkwiretemplatelineitemIDs) AND bulkWireTransferType = 'Domestic' AND softdeleteflag = 0 INTO @DomCount;
		SELECT COUNT(*) FROM bulkwiretemplatelineitems WHERE FIND_IN_SET(bulkWireTemplateLineItemID,_bulkwiretemplatelineitemIDs) AND bulkWireTransferType = 'International' AND softdeleteflag = 0 INTO @InternationalCount;
		SELECT @DomCount AS noOfDomesticTransactions, @InternationalCount AS noOfInternationalTransactions;
		
	ELSEIF _bulkwiretemplateID != "" AND _bulkwiretemplateID IS NOT NULL THEN
		SELECT noOfDomesticTransactions,noOfInternationalTransactions FROM bulkwiretemplate WHERE bulkWireTemplateID = `_bulkwiretemplateID`;
		
	ELSE
		SELECT 0 AS noOfDomesticTransactions, 0 AS noOfInternationalTransactions;
	END IF;

END$$
DELIMITER ;

ALTER TABLE `accounts` ADD COLUMN `MembershipName` VARCHAR(45) NULL DEFAULT NULL AFTER `Membership_id`;

DROP PROCEDURE IF EXISTS `fetch_bwfile_domesticInternationalCount_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bwfile_domesticInternationalCount_proc`(
	IN `_bulkwirefileID` VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
    IF _bulkwirefileID != "" AND _bulkwirefileID IS NOT NULL THEN
		SELECT noOfDomesticTransactions,noOfInternationalTransactions FROM bulkwirefiles WHERE bulkWireFileID = `_bulkwirefileID`;
	ELSE
		SELECT 0 AS noOfDomesticTransactions, 0 AS noOfInternationalTransactions;
	END IF;

END$$
DELIMITER ;


CREATE OR REPLACE 
VIEW `customeraccountsview` AS
    SELECT DISTINCT
        `accounts`.`Membership_id` AS `Membership_id`,
        `accounts`.`MembershipName` AS `MembershipName`,
        `accounts`.`TaxId` AS `Taxid`,
        `customeraccounts`.`Customer_id` AS `Customer_id`,
        `customeraccounts`.`Customer_id` AS `User_id`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`isBusinessAccount` AS `isBusinessAccount`,
        `accounts`.`Type_id` AS `Type_id`,
        `accounts`.`UserName` AS `userName`,
        `accounts`.`CurrencyCode` AS `currencyCode`,
        `accounts`.`AccountHolder` AS `accountHolder`,
        `accounts`.`error` AS `error`,
        `accounts`.`Address` AS `Address`,
        `accounts`.`Scheme` AS `Scheme`,
        `accounts`.`Number` AS `number`,
        `accounts`.`AvailableBalance` AS `availableBalance`,
        `accounts`.`CurrentBalance` AS `currentBalance`,
        `accounts`.`InterestRate` AS `interestRate`,
        `accounts`.`AvailableCredit` AS `availableCredit`,
        `accounts`.`MinimumDue` AS `minimumDue`,
        `accounts`.`DueDate` AS `dueDate`,
        `accounts`.`FirstPaymentDate` AS `firstPaymentDate`,
        `accounts`.`ClosingDate` AS `closingDate`,
        `accounts`.`PaymentTerm` AS `paymentTerm`,
        `accounts`.`OpeningDate` AS `openingDate`,
        `accounts`.`MaturityDate` AS `maturityDate`,
        `accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,
        `accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,
        `accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,
        `accounts`.`DividendRate` AS `dividendRate`,
        `accounts`.`DividendYTD` AS `dividendYTD`,
        `accounts`.`EStatementmentEnable` AS `eStatementEnable`,
        `customeraccounts`.`IsOrganizationAccount` AS `isOrganizationAccount`,
        `customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,
        `accounts`.`StatusDesc` AS `statusDesc`,
        `accounts`.`NickName` AS `nickName`,
        `accounts`.`OriginalAmount` AS `originalAmount`,
        `accounts`.`OutstandingBalance` AS `outstandingBalance`,
        `accounts`.`PaymentDue` AS `paymentDue`,
        `accounts`.`PaymentMethod` AS `paymentMethod`,
        `accounts`.`SwiftCode` AS `swiftCode`,
        `accounts`.`TotalCreditMonths` AS `totalCreditMonths`,
        `accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,
        `accounts`.`RoutingNumber` AS `routingNumber`,
        `accounts`.`SupportBillPay` AS `supportBillPay`,
        `accounts`.`SupportCardlessCash` AS `supportCardlessCash`,
        `accounts`.`SupportTransferFrom` AS `supportTransferFrom`,
        `accounts`.`SupportTransferTo` AS `supportTransferTo`,
        `accounts`.`SupportDeposit` AS `supportDeposit`,
        `accounts`.`UnpaidInterest` AS `unpaidInterest`,
        `accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,
        `accounts`.`principalBalance` AS `principalBalance`,
        `accounts`.`PrincipalValue` AS `principalValue`,
        `accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,
        `accounts`.`phone` AS `phoneId`,
        `accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,
        `accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,
        `accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,
        `accounts`.`LastPaymentDate` AS `lastPaymentDate`,
        `accounts`.`LastStatementBalance` AS `lastStatementBalance`,
        `accounts`.`LateFeesDue` AS `lateFeesDue`,
        `accounts`.`maturityAmount` AS `maturityAmount`,
        `accounts`.`MaturityOption` AS `maturityOption`,
        `accounts`.`payoffAmount` AS `payoffAmount`,
        `accounts`.`PayOffCharge` AS `payOffCharge`,
        `accounts`.`PendingDeposit` AS `pendingDeposit`,
        `accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,
        `accounts`.`JointHolders` AS `jointHolders`,
        `accounts`.`IsPFM` AS `isPFM`,
        `accounts`.`InterestPaidYTD` AS `interestPaidYTD`,
        `accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,
        `accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,
        `accounts`.`InterestEarned` AS `interestEarned`,
        `accounts`.`CurrentAmountDue` AS `currentAmountDue`,
        `accounts`.`CreditLimit` AS `creditLimit`,
        `accounts`.`CreditCardNumber` AS `creditCardNumber`,
        `accounts`.`BsbNum` AS `bsbNum`,
        `accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,
        `accounts`.`BondInterest` AS `bondInterest`,
        `accounts`.`AvailablePoints` AS `availablePoints`,
        `accounts`.`AccountName` AS `accountName`,
        `accounts`.`email` AS `email`,
        `accounts`.`IBAN` AS `IBAN`,
        `accounts`.`adminProductId` AS `adminProductId`,
        `accounts`.`UpdatedBy` AS `UpdatedBy`,
        `accounts`.`LastUpdated` AS `LastUpdated`,
        `accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,
        `bank`.`Description` AS `bankname`,
        `accounts`.`AccountPreference` AS `accountPreference`,
        `accounttype`.`transactionLimit` AS `transactionLimit`,
        `accounttype`.`transferLimit` AS `transferLimit`,
        `accounttype`.`rates` AS `rates`,
        `accounttype`.`termsAndConditions` AS `termsAndConditions`,
        `accounttype`.`TypeDescription` AS `typeDescription`,
        `accounttype`.`supportChecks` AS `supportChecks`,
        `accounttype`.`displayName` AS `displayName`,
        `accounts`.`accountSubType` AS `accountSubType`,
        `accounts`.`description` AS `description`,
        `accounts`.`schemeName` AS `schemeName`,
        `accounts`.`identification` AS `identification`,
        `accounts`.`secondaryIdentification` AS `secondaryIdentification`,
        `accounts`.`servicerSchemeName` AS `servicerSchemeName`,
        `accounts`.`servicerIdentification` AS `servicerIdentification`,
        `accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,
        `accounts`.`dataType` AS `dataType`,
        `accounts`.`dataDateTime` AS `dataDateTime`,
        `accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,
        `accounts`.`dataCreditLineType` AS `dataCreditLineType`,
        `accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,
        `accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`
    FROM
        (((((`accounts`
        JOIN `customeraccounts`)
        JOIN `accounttype`)
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`accountId`)))
        LEFT JOIN `membership` ON ((`membership`.`id` = `membershipaccounts`.`membershipId`)))
        LEFT JOIN `bank` ON ((`accounts`.`Bank_id` = `bank`.`id`)))
    WHERE
        ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`)
            AND (`accounts`.`Type_id` = `accounttype`.`TypeID`));


DROP PROCEDURE IF EXISTS `customer_group_org_actionlimits_proc`;

delimiter $$

CREATE PROCEDURE `customer_group_org_actionlimits_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _organisationId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _isOnlyPremissions varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET SESSION group_concat_max_len = 10000000;

SET @organization_actions = (SELECT GROUP_CONCAT(DISTINCT organisationactionlimit.Action_id SEPARATOR ",")  FROM organisationactionlimit WHERE 
							organisationactionlimit.Organisation_id=_organisationId and 
							organisationactionlimit.Action_id NOT IN (
									SELECT featureaction.id FROM
									featureaction
									LEFT JOIN feature on (featureaction.Feature_id = feature.id)
									LEFT JOIN organisationfeatures on (organisationfeatures.featureId = feature.id)
									WHERE
									organisationfeatures.organisationId = _organisationId
									AND organisationfeatures.featureStatus='SID_FEATURE_SUSPENDED'
									OR feature.Status_id != 'SID_FEATURE_ACTIVE'
							));
                                    
SET @organization_actions = concat("'", IF(@organization_actions IS NULL, '', @organization_actions), "'");

SET @business_customer_enabled_actions = (SELECT GROUP_CONCAT(Action_id SEPARATOR "','") FROM customeraction WHERE isAllowed = '1' AND Customer_id =_customerId AND FIND_IN_SET(Action_id, @organization_actions));

IF @business_customer_enabled_actions IS NOT NULL THEN 
  	SET @business_customer_enabled_actions = concat("'", IF(@business_customer_enabled_actions is null, '', @business_customer_enabled_actions), "'");
END IF;

SET @groups = (SELECT GROUP_CONCAT(customergroup.Group_id SEPARATOR ",") FROM customergroup WHERE Customer_id=_customerId);

IF(@groups != '' ) THEN
    SET @groups =  concat(@groups,",");
END IF;

SET @list = (SELECT @groups);

SET @select_statement = '';
	groupIdIteration : LOOP
		IF LENGTH(TRIM(@list)) = 0 OR @list IS NULL THEN
		LEAVE groupIdIteration;
	  END IF;
		SET @next = SUBSTRING_INDEX(@list,',',1);
		SET @nextlen = LENGTH(@next);
		SET @value = TRIM(@next);
		
		set @roleType = (SELECT membergroup.Type_id FROM membergroup WHERE membergroup.id= @value );

		IF @roleType = ('TYPE_ID_BUSINESS') THEN
			IF _isOnlyPremissions = 'true' THEN
				SELECT DISTINCT customeraction.Action_id AS actionId FROM customeraction WHERE isAllowed = '1' AND Customer_id =_customerId AND FIND_IN_SET(Action_id, @organization_actions)
                AND (customeraction.Action_id IN (SELECT DISTINCT groupactionlimit.Action_id FROM groupactionlimit WHERE groupactionlimit.Group_id = @value));
			ELSE
			SET @select_statement =  concat(@select_statement , "SELECT DISTINCT
								`feature`.`id` as `featureId`,
								`feature`.`name` AS `featureName`,
								`feature`.`description` AS `featureDescription`,
								`featureaction`.`id` as `actionId`,
								`featureaction`.`Type_id` as `actionType`,
								`featureaction`.`description` AS `actionDescription`,
								`featureaction`.`name` AS `actionName`,
								IF(`featureaction`.`id` in (", @business_customer_enabled_actions,"), 'true', 'false') AS `isActionAllowed`,
								IF(`featureaction`.`isAccountLevel`= 1 , 'true','false') AS `isAccountLevel`,
								if(`customeraction`.`isAllowed` = 1 , 'true','false') as `isAllowedForCustomer`,
								`customeraction`.`Account_id` as `accountId`,
								`customeraction`.`LimitType_id` as `limitTypeId`,
								`customeraction`.`value` as `value`
						   FROM
							(`customeraction` 
							LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customeraction`.`Customer_id`)
							LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
							LEFT JOIN `featureaction` ON (`featureaction`.`id` = `customeraction`.`Action_id`)
							LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
						where 
							`customeraction`.`Customer_id` = ",quote(_customerId),"
							and (`customeraction`.`Action_id` in (
							select DISTINCT `groupactionlimit`.`Action_id` from 
							`groupactionlimit` where 
							`groupactionlimit`.`Group_id` = ",quote(@value),"))
							and (`featureaction`.`id` in (", @business_customer_enabled_actions,"))");
			PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
            END IF;
		END IF;
	SET @list = INSERT(@list,1,@nextlen + 1,'');  
	END LOOP;
END$$

DELIMITER ;
DROP PROCEDURE IF EXISTS `group_actions_proc`;

delimiter $$

CREATE PROCEDURE `group_actions_proc`(
in _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _isOnlyPremissions varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

if _isOnlyPremissions = 'true' then
	select DISTINCT `groupactionlimit`.`Action_id` AS `actionId` FROM `groupactionlimit` where `groupactionlimit`.`Group_id` = _groupId;
else
	SET @select_statement = concat("SELECT
		`groupactionlimit`.`Group_id` AS `groupId`,
		`groupactionlimit`.`LimitType_id` AS `limitTyeId`,
		`groupactionlimit`.`value` AS `value`,
		`featureaction`.`id` AS `actionId`,
		`featureaction`.`Type_id` AS `actionType`,
		`featureaction`.`name` AS `actionName`,
		`featureaction`.`description` AS `actionDescription`,	
		`feature`.`id` AS `featureId`
	FROM
		(`groupactionlimit`
		LEFT JOIN `featureaction` ON (`featureaction`.`id` = `groupactionlimit`.`Action_id`)
		LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`))
		where `feature`.`Status_id` = 'SID_FEATURE_ACTIVE' and `groupactionlimit`.`Group_id`=",quote(_groupId));

		IF (_actionType != '' ) THEN
			set @select_statement =  concat(@select_statement ," and `featureaction`.`Type_id` = ",quote(_actionType));
		END IF;
		
		 IF (_actionId != '' ) THEN
			set @select_statement =  concat(@select_statement ," and `featureaction`.`id` = ",quote(_actionId));
		END IF;
        PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
end if;
END$$
DELIMITER ;

DELIMITER $$
CREATE PROCEDURE `userlinking_proc`(
    IN _combinedUser VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _otherUser VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _isCombinedUserBusiness VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE success_flag INT(8);

	SET SESSION group_concat_max_len = 100000000;
	SET SQL_SAFE_UPDATES = 0;
	
	UPDATE customerrequest SET Customer_id = _combinedUser
    WHERE Customer_id = _otherUser;
	UPDATE cardaccountrequest SET Customer_id = _combinedUser
    WHERE Customer_id = _otherUser;
	UPDATE notificationcardinfo SET Customer_id = _combinedUser
    WHERE Customer_id = _otherUser;
	SET @deviceIds = (select group_concat(id SEPARATOR ",") from customerdevice where Customer_id = _combinedUser);
    UPDATE customerdevice SET Customer_id = _combinedUser
    WHERE Customer_id = _otherUser AND NOT FIND_IN_SET(id, @deviceIds);
    
	UPDATE `payee` SET `User_Id` = _combinedUser WHERE `User_Id` = _otherUser;
    UPDATE `externalaccount` SET `User_id` = _combinedUser WHERE `User_id` = _otherUser;
    UPDATE `billpaypayee` SET `customerId` = _combinedUser WHERE `customerId` = _otherUser;
    UPDATE `wiretransferspayee` SET `customerId` = _combinedUser WHERE `customerId` = _otherUser;
    UPDATE `interbankpayee` SET `customerId` = _combinedUser WHERE `customerId` = _otherUser;
    UPDATE `intrabankpayee` SET `customerId` = _combinedUser WHERE `customerId` = _otherUser;
	UPDATE `internationalpayee` SET `customerId` = _combinedUser WHERE `customerId` = _otherUser;
	IF _isCombinedUserBusiness = 'false' THEN
		UPDATE customeraction SET Customer_id = _combinedUser
		WHERE Customer_id = _otherUser;	
    END IF;
    
    SET SQL_SAFE_UPDATES = 1;
	
END$$
DELIMITER ;

ALTER TABLE `backendidentifier` ADD COLUMN `isTypeBusiness` VARCHAR(45) NULL DEFAULT 0 AFTER `lastmodifiedts`;



DROP procedure IF EXISTS `combinedaccess_deleteAndUpdatePreferences`;
DELIMITER $$
CREATE PROCEDURE `combinedaccess_deleteAndUpdatePreferences`(newCustomerId varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci, accountId_Del varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci, accountType_Del varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci,deactivatedCustomerId varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 DECLARE newCustomerId  varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT newCustomerId;
 DECLARE accountId_Del varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT accountId_Del;
 DECLARE accountType_Del varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT accountType_Del;
 DECLARE deactivatedCustomerId  varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT deactivatedCustomerId;
DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
  ROLLBACK;
  RESIGNAL;
END;
 
DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
 ROLLBACK;
 RESIGNAL;
END;
START TRANSACTION;
 delete from dbxcustomeralertentitlement  where  dbxcustomeralertentitlement.Customer_id = deactivatedCustomerId and dbxcustomeralertentitlement.AccountId = accountId_Del and dbxcustomeralertentitlement.AccountType = accountType_Del;
 delete from customeralertswitch where  customeralertswitch.Customer_id =  deactivatedCustomerId and customeralertswitch.AccountID = accountId_Del  and customeralertswitch.AccountType = accountType_Del;
 delete from customeralertcategorychannel where customeralertcategorychannel.Customer_id = deactivatedCustomerId and customeralertcategorychannel.AccountId = accountId_Del and customeralertcategorychannel.AccountType = accountType_Del;
 update  dbxcustomeralertentitlement  set dbxcustomeralertentitlement.Customer_id = newCustomerId where dbxcustomeralertentitlement.Customer_id =   deactivatedCustomerId;
 update  customeralertswitch  set customeralertswitch.Customer_id = newCustomerId where customeralertswitch.Customer_id =   deactivatedCustomerId;
 update  customeralertcategorychannel  set customeralertcategorychannel.Customer_id = newCustomerId where customeralertcategorychannel.Customer_id = deactivatedCustomerId ;
COMMIT;
 END$$
DELIMITER ;


DROP procedure IF EXISTS `combinedaccess_updatePreferences_Delink`;

DELIMITER $$
CREATE  PROCEDURE `combinedaccess_updatePreferences_Delink`(newCustomerId varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci, accountIds varchar(200) CHARACTER SET UTF8 COLLATE utf8_general_ci,combinedCustomerId varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
 DECLARE newCustomerId  varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT newCustomerId;
 DECLARE accountIds varchar(200) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT accountIds;
 DECLARE combinedCustomerId  varchar(150) CHARACTER SET UTF8 COLLATE utf8_general_ci DEFAULT combinedCustomerId;
DECLARE exit handler for sqlexception
  BEGIN
    -- ERROR
  ROLLBACK;
  RESIGNAL;
END;
 
DECLARE exit handler for sqlwarning
 BEGIN
    -- WARNING
 ROLLBACK;
 RESIGNAL;
END;
START TRANSACTION;
 update  dbxcustomeralertentitlement  set dbxcustomeralertentitlement.Customer_id = newCustomerId where dbxcustomeralertentitlement.Customer_id = combinedCustomerId and FIND_IN_SET(dbxcustomeralertentitlement.AccountId ,accountIds);
 update  customeralertswitch  set customeralertswitch.Customer_id = newCustomerId where customeralertswitch.Customer_id = combinedCustomerId and FIND_IN_SET(customeralertswitch.AccountID,accountIds);
 update  customeralertcategorychannel  set customeralertcategorychannel.Customer_id = newCustomerId where customeralertcategorychannel.Customer_id = combinedCustomerId and FIND_IN_SET(customeralertcategorychannel.AccountId,accountIds);
COMMIT;
 END$$

DELIMITER ;

ALTER TABLE `batchalertobject` 
ADD COLUMN `operationName` VARCHAR(100) NULL AFTER `objectType`;

DELIMITER $$
CREATE PROCEDURE `userdelinking_proc`(
IN _newUser VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _combinedUser VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET SQL_SAFE_UPDATES = 0;
	
    SET @groupId = (select Group_id from `customergroup` join `membergroup` on (`customergroup`.`Group_id`  = `membergroup`.`id` and `membergroup`.`Type_id` ='TYPE_ID_RETAIL') where `Customer_id`= _combinedUser );
    DELETE FROM `customergroup` WHERE `Customer_id`= _combinedUser and `Group_id` = @groupId;
    INSERT INTO `customergroup` (`Customer_id`, `Group_id`) VALUES (_newUser, @groupId);    
    UPDATE `payee` SET `User_Id` = _newUser WHERE `User_Id` = _combinedUser AND `organizationId` IS NULL;
    UPDATE `externalaccount` SET `User_id` = _newUser WHERE `User_id` = _combinedUser AND `organizationId` IS NULL;
    UPDATE `billpaypayee` SET `customerId` = _newUser WHERE `customerId` = _combinedUser AND `isBusinessPayee` = '0' AND `companyId` IS NULL;
    UPDATE `wiretransferspayee` SET `customerId` = _newUser WHERE `customerId` = _combinedUser AND `isBusinessPayee` = '0' AND `companyId` IS NULL;
    UPDATE `interbankpayee` SET `customerId` = _newUser WHERE `customerId` = _combinedUser AND `isBusinessPayee` = '0' AND `companyId` IS NULL;
    UPDATE `intrabankpayee` SET `customerId` = _newUser WHERE `customerId` = _combinedUser AND `isBusinessPayee` = '0' AND `companyId` IS NULL;
    UPDATE `internationalpayee` SET `customerId` = _newUser WHERE `customerId` = _combinedUser AND `isBusinessPayee` = '0' AND `companyId` IS NULL;
    
    SET SQL_SAFE_UPDATES = 1;
END$$

DELIMITER ;


ALTER TABLE `membershipowner` 
ADD COLUMN `memberTypeId` VARCHAR(45) NULL DEFAULT NULL AFTER `employmentStatus`,
ADD COLUMN `memberTypeName` VARCHAR(45) NULL DEFAULT NULL AFTER `memberTypeId`;

DROP PROCEDURE IF EXISTS `customer_eagreement_get_proc`;
delimiter $$

CREATE PROCEDURE `customer_eagreement_get_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select membergroup.isEAgreementActive AS isEAgreementActive ,membergroup.name as name, membergroup.id as id from 
	membergroup 
    LEFT JOIN customergroup on (customergroup.Group_id = membergroup.id)
    where
    customergroup.Customer_id = _customerId AND membergroup.Type_id = 'TYPE_ID_BUSINESS';
END$$

DELIMITER;

DROP PROCEDURE IF EXISTS Update_BulkWireTemplateRecipient_Count;
DELIMITER $$
CREATE PROCEDURE `Update_BulkWireTemplateRecipient_Count`(
	IN `_userID` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_orgID` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
	IN `_payeeID` varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
proc_label: BEGIN

	IF (_userID IS NULL OR _userID = "") AND (_orgID IS NULL OR _orgID = "") THEN
		SELECT "Both user_id and company_id cannot be empty" AS Response;
		LEAVE proc_label;
	END IF;
	IF _payeeID IS NULL OR _payeeID = "" THEN
		SELECT "payeeId cannot be empty" AS Response;
		LEAVE proc_label;
	END IF;
	
	IF _orgID IS NOT NULL AND _orgID != "" THEN
		SET @filterLineItemQuery = CONCAT("WHERE payeeId = '",_payeeID,"' AND bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM bulkwiretemplate WHERE company_id = '",_orgID,"')");
		SET @filterTemplateQuery = CONCAT("WHERE payeeId = '",_payeeID,"') AND company_id = '",_orgID,"'");
	ELSE
		SET @filterLineItemQuery = CONCAT("WHERE payeeId = '",_payeeID,"' AND createdby = '",_userID,"'");
		SET @filterTemplateQuery = CONCAT("WHERE payeeId = '",_payeeID,"' AND createdby = '",_userID,"')");
	END IF;
	
	SELECT bulkWireTransferType FROM bulkwiretemplatelineitems WHERE payeeId = _payeeID LIMIT 1 INTO @transferType;
	
	IF @transferType = "Domestic" THEN
		SET @updateTransferTypeTransactions = "UPDATE bulkwiretemplate SET noOfDomesticTransactions = noOfDomesticTransactions - 1 ";
	ELSE
		SET @updateTransferTypeTransactions = "UPDATE bulkwiretemplate SET noOfInternationalTransactions = noOfInternationalTransactions - 1 ";
	END IF;
	
	SET @updatelineitemQuery = CONCAT("UPDATE bulkwiretemplatelineitems SET softdeleteflag = 1 ",@filterLineItemQuery);
	
	SET @updateTemplateQuery = CONCAT("UPDATE bulkwiretemplate SET noOfTransactions = noOfTransactions - 1 WHERE bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM bulkwiretemplatelineitems ",@filterTemplateQuery);
	
	SET @updateTransferTypeTemplateQuery = CONCAT(@updateTransferTypeTransactions," WHERE bulkWireTemplateID IN (SELECT bulkWireTemplateID FROM bulkwiretemplatelineitems ",@filterTemplateQuery);
	
	PREPARE sql_query FROM @updatelineitemQuery;
	EXECUTE sql_query;
	
	PREPARE sql_query1 FROM @updateTemplateQuery;
	EXECUTE sql_query1;
	
	PREPARE sql_query2 FROM @updateTransferTypeTemplateQuery;
	EXECUTE sql_query2;
	
	SELECT "SUCCESS" AS Response;
	
	

END$$
DELIMITER ;

CREATE INDEX customeraccounts_IsOrganizationAccount_IDX USING BTREE ON customeraccounts (IsOrganizationAccount);

CREATE OR REPLACE VIEW `orgemployeedetails` AS
select
    `c`.`id` AS `id`,
    `c`.`FirstName` AS `FirstName`,
    `c`.`MiddleName` AS `MiddleName`,
    `c`.`LastName` AS `LastName`,
    `c`.`UserName` AS `Username`,
    `c`.`Gender` AS `Gender`,
    `c`.`DateOfBirth` AS `DateOfBirth`,
    `c`.`DrivingLicenseNumber` AS `DrivingLicenseNumber`,
    `c`.`Ssn` AS `Ssn`,
    `c`.`UserCompany` AS `UserCompany`,
    `c`.`Lastlogintime` AS `Lastlogintime`,
    `c`.`Status_id` AS `Status`,
    `ca`.`Account_id` AS `Account_id`,
    `ca`.`AccountName` AS `AccountName`,
    `c`.`createdby` AS `createdby`
from
    `customer` `c` join `customeraccounts` `ca`
    where `c`.`id` = `ca`.`Customer_id` and `ca`.IsOrganizationAccount = '1';
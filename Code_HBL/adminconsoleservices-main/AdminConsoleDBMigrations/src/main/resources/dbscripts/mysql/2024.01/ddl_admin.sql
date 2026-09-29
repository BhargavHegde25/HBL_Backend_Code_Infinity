CREATE TABLE `makercheckerconfig` (
  `id` int(11) NOT NULL,
  `expAPIOperationName` varchar(250) DEFAULT NULL,
  `module` varchar(250) DEFAULT NULL,
  `action` varchar(250) DEFAULT NULL,
  `approvalPermissionId` varchar(10) DEFAULT NULL,
  `approvalPermissionName` varchar(50) DEFAULT NULL,
  `keyColumnNames` varchar(200) NOT NULL,
  `isApprovalRequired` tinyint(1) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `approvalrequests` ADD COLUMN `reqPayload` longtext NOT NULL AFTER recordId, ADD COLUMN `companyLegalUnit` varchar(50) NOT NULL DEFAULT 'ALL';

ALTER TABLE `customer` ADD COLUMN `sbaEnrolmentStatus` VARCHAR(70) NULL DEFAULT NULL ;

ALTER TABLE `productInformation` ADD availableFromDate varchar(100) NULL;
ALTER TABLE `productInformation` ADD availableToDate varchar(100) NULL;
ALTER TABLE `productInformation` ADD purposeData varchar(100) NULL;

CREATE TABLE `Facilities` (
  `facilityId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `code` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `facilityName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `numOfFeatures` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`code`)
) ;

CREATE TABLE `productFacility` (
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `facilityId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `sequenceNo` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `code` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productFacilityId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `facilityName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `isMandatory` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `defaultValue` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `optionDispType` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`productFacilityId`),
  KEY `productFacility_FK` (`code`),
  KEY `productFacility_FK_1` (`productRef`),
  CONSTRAINT `productFacility_FK` FOREIGN KEY (`code`) REFERENCES `Facilities` (`code`),
  CONSTRAINT `productFacility_FK_1` FOREIGN KEY (`productRef`) REFERENCES `productInformation` (`productRef`)
);

CREATE TABLE `productDescription` (
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `description` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `detailedDesc` mediumtext COLLATE utf8_unicode_ci,
  `disclosure` longtext COLLATE utf8_unicode_ci,
  `notes` longtext COLLATE utf8_unicode_ci,
  `termsConditions` longtext COLLATE utf8_unicode_ci,
  KEY `productDescription_FK` (`productRef`),
  CONSTRAINT `productDescription_FK` FOREIGN KEY (`productRef`) REFERENCES `productInformation` (`productRef`)
);
CREATE TABLE `productImage` (
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `imageType` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `height` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `width` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imageUrl` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `imageId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productId` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`productRef`,`imageType`),
  CONSTRAINT `productImage_FK` FOREIGN KEY (`productRef`) REFERENCES `productInformation` (`productRef`)
);
CREATE TABLE `productAdditionalAttributes` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `key` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `additionalAttributes_FK` (`productRef`),
  CONSTRAINT `additionalAttributes_FK` FOREIGN KEY (`productRef`) REFERENCES `productInformation` (`productRef`)
);
CREATE TABLE `productOptionValues` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `productFacilityId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `value` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `desc` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `extensionData` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `optionValues_FK` (`productFacilityId`),
  CONSTRAINT `optionValues_FK` FOREIGN KEY (`productFacilityId`) REFERENCES `productFacility` (`productFacilityId`)
);
DROP PROCEDURE IF EXISTS `delete_marketingdata`;
DELIMITER $$
CREATE  PROCEDURE `delete_marketingdata`()
BEGIN
    DELETE FROM productAdditionalAttributes ;
	  DELETE FROM productOptionValues ;
	  DELETE FROM productFeatureActions ;
	  DELETE FROM productFeatures ;
    DELETE FROM productDescription ;
	  DELETE FROM productImage ;
    DELETE FROM productFacility;
    DELETE FROM Facilities ;
    DELETE FROM productInformation;
    DELETE FROM productGroup ;
    DELETE FROM productLine;
     
END$$
DELIMITER ;
DROP PROCEDURE IF EXISTS `insertProductInformationSch`;
DELIMITER $$
CREATE PROCEDURE `insertProductInformationSch`(
  IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
      SET @index = 0;
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            set @query = concat('INSERT INTO productInformation(productId,productRef,branchRef,productName,productLineRef,productGroupRef,status,availableFromDate,availableToDate) VALUES (',@recordsData,');');
            PREPARE stmt FROM @query; EXECUTE stmt; DEALLOCATE PREPARE stmt;
           END IF;
      END LOOP insertRecords;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `insertProductFacilitiesSch`;
DELIMITER $$
CREATE PROCEDURE `insertProductFacilitiesSch`(IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
       SET @index = 0;
      SET @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
           set @facilityId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',1 ), ',', -1 );
           set @code = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',2 ), ',', -1 );
           set @facilityName = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',3 ), ',', -1 );
           set @description = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',4 ), ',', -1 );
           set @numOfFeatures = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',5 ), ',', -1 );
           set @productRef = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',6 ), ',', -1 );
           set @sequenceNo = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',7 ), ',', -1 );
           set @isMandatory = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',8 ), ',', -1 );
           set @productFacilityId=SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, ',',9 ), ',', -1 );
           set @codec=REPLACE(@code , '\"', '');
      SET @facilitiesInserted = 0;
     SELECT @codec COLLATE utf8_general_ci;
   
          IF NOT EXISTS (
    SELECT code
    FROM Facilities f
    WHERE code = @codec COLLATE utf8_unicode_ci
) THEN
                SET @queryFacilities = CONCAT('INSERT INTO Facilities(facilityId,code,facilityName,description,numOfFeatures) VALUES (', @facilityId, ',', @code, ',', @facilityName, ',', @description, ',', @numOfFeatures, ');');
                SET @facilitiesInserted = 1;
            END IF;

           SET @queryProductFacility = CONCAT('INSERT INTO productFacility(productRef,facilityId,sequenceNo,code,productFacilityId,description,facilityName,isMandatory) VALUES (', @productRef, ',', @facilityId, ',', @sequenceNo, ',', @code, ',', @productFacilityId, ',', @description, ',', @facilityName, ',', @isMandatory, ');');

          
            IF @facilitiesInserted = 1 THEN
                PREPARE stmtFacilities FROM @queryFacilities;
                EXECUTE stmtFacilities;
                DEALLOCATE PREPARE stmtFacilities;
            END IF;

            -- Execute the productFacility query
            PREPARE stmtProductFacility FROM @queryProductFacility;
            EXECUTE stmtProductFacility;
            DEALLOCATE PREPARE stmtProductFacility;
        END IF;
    END LOOP insertRecords;
END$$
DELIMITER;

DROP PROCEDURE IF EXISTS `facilities_get_proc`;
DELIMITER $$
CREATE PROCEDURE `facilities_get_proc`(  IN _facilityId varchar(50) )
BEGIN   
if(_facilityId is null || _facilityId='') then
    SELECT * FROM Facilities;
else
    SELECT * FROM Facilities where facilityId = _facilityId;
end if;
end$$
DELIMITER;
DROP PROCEDURE IF EXISTS `product_details_proc`;
DELIMITER $$
CREATE  PROCEDURE `product_details_proc`(  
   IN _productRef varchar(50) )
BEGIN
   
SELECT productId,pl.productLineId,pl.productLineRef,pl.productLineName,pl.externalIndicator,pg.productGroupId,pg.productGroupRef,pg.productGroupName,pg.description,pg.detailedDesc,pi.productRef,productName,pi.status, pi.branchRef,availableFromDate,availableToDate,purposeData from productInformation pi,productLine pl,productGroup pg where pi.productRef=_productRef and pl.productLineRef = pi.productLineRef and pg.productGroupRef=pi.productGroupRef;

SELECT * from productAdditionalAttributes where productRef=_productRef;

SELECT * from productImage where productRef=_productRef;   

SELECT * from productFacility where productRef = _productRef;
SELECT * from productOptionValues where productFacilityId in (select productFacilityId from productFacility where productRef = _productRef);

SELECT pd.description,pd.detailedDesc,pd.disclosure,pd.notes,pd.termsConditions from productDescription pd where pd.productRef=_productRef;

SELECT * from productFeatures where productRef = _productRef;
SELECT pfa.actionsId,pfa.productId,pfa.featureId,pfa.extensionData,pfa.status,pf.featureCode from productFeatureActions pfa,productFeatures pf where pf.productRef = _productRef and pfa.productId = pf.productId and pfa.featureId = pf.featureId;

end$$
DELIMITER;

DROP PROCEDURE IF EXISTS `product_update_proc`;
DELIMITER $$
CREATE PROCEDURE `product_update_proc`(
	IN `_description` VARCHAR(100),
	IN `_detailedDesc` MEDIUMTEXT,
	IN `_notes` longtext,
	IN `_termsConditions` longtext,
	IN `_disclosure` longtext,
	IN `_productRef` VARCHAR(50),
	IN `_productName` VARCHAR(50),
	IN `_availableFrom` VARCHAR(50),
	IN `_availableTo` VARCHAR(50),
	IN `_purposes` VARCHAR(50),
	IN `_addAttributes` MEDIUMTEXT
)
BEGIN
SET @pd_count = (SELECT COUNT(`productRef`) FROM `productDescription` WHERE productRef=_productRef);
IF @pd_count = 0 THEN
    INSERT INTO `productDescription` (`description`, `detailedDesc`, `notes`, `termsConditions`, `disclosure`, `productRef`)
VALUES (`_description`, `_detailedDesc`, `_notes`, `_termsConditions`, `_disclosure`, `_productRef`);
ELSE
    UPDATE `productDescription` SET `description` = `_description`, `detailedDesc` = `_detailedDesc`, `notes` = `_notes`, `termsConditions` = `_termsConditions`, `disclosure` = `_disclosure` WHERE `productRef` = `_productRef`;  
END IF;
    UPDATE `productInformation` SET `productName` = `_productName`, `availableFromDate` = `_availableFrom`, `availableToDate` = `_availableTo`,  `purposeData` = `_purposes` WHERE `productRef` = `_productRef`;
	
		DELETE FROM `productAdditionalAttributes` WHERE productRef=_productRef;
 
 		SET @index = 0;
		SET @numOfRecords = LENGTH(_addAttributes) - LENGTH(REPLACE(_addAttributes, '|^', '')) + 1;
		insertRecords : LOOP
			SET @index = @index + 1;
			IF @index = @numOfRecords  THEN 
				LEAVE insertRecords;
			ELSE
				set @recordsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_addAttributes, '|^', @index), '|^', -1 );
				set @attributeId = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '#$',1 ), '#$', -1 );
				set @attributeValue = SUBSTRING_INDEX(SUBSTRING_INDEX(@recordsData, '#$',2 ), '#$', -1 );
				INSERT INTO `productAdditionalAttributes`(`productRef`,`key`,`value`) values(_productRef,@attributeId,@attributeValue);
 			END IF;
		END LOOP insertRecords;

	
    SELECT `productId` FROM `productInformation` where `productRef` = `_productRef`;
END$$
DELIMITER ;

CREATE TABLE `productFeatures` (
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `featureId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `featureCode` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'Active',
  PRIMARY KEY (`productId`,`featureId`)
);

CREATE TABLE `productFeatureActions` (
  `actionsId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `featureId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `extensionData` varchar(100) COLLATE utf8_unicode_ci NOT NULL DEFAULT '{}',
  `status` varchar(10) COLLATE utf8_unicode_ci DEFAULT 'Active',
  PRIMARY KEY (`productId`,`featureId`,`actionsId`)
);

DROP PROCEDURE IF EXISTS `product_features_proc`;
DELIMITER $$
CREATE  PROCEDURE `product_features_proc`(  
   IN _productId varchar(50) )
BEGIN
 
SELECT * from productFeatures where productId = _productId;
SELECT * from productFeatureActions  where productId = _productId and featureId in (SELECT featureId from productFeatures where productId = _productId);

end$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `product_features_delete_proc`;
DELIMITER $$
CREATE  PROCEDURE `product_features_delete_proc`(  
   IN _productId varchar(50),
   IN _featureId varchar(50))
BEGIN
 
DELETE from productFeatures where productId = _productId AND featureId=_featureId;
DELETE from productFeatureActions  where productId = _productId and featureId=_featureId;

end$$
DELIMITER ;

DROP procedure IF EXISTS `fetch_distinct_customeraccounts`;
DELIMITER $$
CREATE PROCEDURE `fetch_distinct_customeraccounts`(
    IN _queryInput MEDIUMTEXT CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _companyLegalunit VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
     set @index = 0;
     set @whereClause = '';
      set @numOfRecords = LENGTH(_queryInput) - LENGTH(REPLACE(_queryInput, '|', '')) + 1;
      insertRecords : LOOP
          set @index = @index + 1;
          IF @index = @numOfRecords + 1 THEN 
            LEAVE insertRecords;
          else
            set @currLoopPayload = SUBSTRING_INDEX(SUBSTRING_INDEX(_queryInput, '|', @index), '|', -1 );
            
            set @contractId =  SUBSTRING_INDEX(SUBSTRING_INDEX(@currLoopPayload, ',', 1), ',', -1 );
            set @coreCustomerId = SUBSTRING_INDEX(SUBSTRING_INDEX(@currLoopPayload, ',', 2), ',', -1 );
            set @accountId = SUBSTRING_INDEX(SUBSTRING_INDEX(@currLoopPayload, ',', 3), ',', -1 );
            
            IF @index != 1
            THEN 
                    set @whereClause = CONCAT( @whereClause , ' or ');
            END IF;
            
            set @whereClause = CONCAT(@whereClause, "(contractId= " , @contractId , " and corecustomerId =  " , 
            @coreCustomerId , " and Account_id = " ,  @accountId , ")");
           
            set @query = "select distinct customer_id, account_id from customeraccounts where (" ;
            set @query = CONCAT(@query , @whereClause);
        
          IF @index = @numOfRecords 
          THEN
            set @query = CONCAT(@query , ") and companyLegalUnit = '", _companyLegalunit ,"'");
          END IF;
          END IF;
      END LOOP insertRecords;
     
         PREPARE stmt FROM @query; 
         EXECUTE stmt; 
         DEALLOCATE PREPARE stmtt;
    
END$$
DELIMITER ;

CREATE TABLE `scf_records_module_configurations` (
    `module_id` VARCHAR(255) PRIMARY KEY,
    `allowed_fields` JSON
);

CREATE TABLE `scf_records` (
    `record_id` INT AUTO_INCREMENT PRIMARY KEY,
    `module_id` VARCHAR(255),
    `record_data` JSON,
    `created_by` VARCHAR(255),
    `updated_by` VARCHAR(255),
    `created_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (`module_id`) REFERENCES `scf_records_module_configurations`(`module_id`),
    CONSTRAINT `record_data` CHECK (JSON_VALID(`record_data`))
)AUTO_INCREMENT = 1000000;

DELIMITER $$
CREATE TRIGGER before_scf_records_insert
BEFORE INSERT ON scf_records
FOR EACH ROW
BEGIN
    DECLARE field_count INT;
    
    -- Get the allowed fields for the module
    SELECT COUNT(*) INTO field_count
    FROM scf_records_module_configurations WHERE module_id = NEW.module_id
      AND JSON_CONTAINS(scf_records_module_configurations.allowed_fields, JSON_KEYS(NEW.record_data)) = 0;

    -- If field_count > 0, it means there are disallowed fields
    IF field_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Insert failed. Disallowed fields in record data.';
    END IF;
END;
$$
DELIMITER ;

DELIMITER $$
CREATE TRIGGER before_scf_records_update
BEFORE UPDATE ON scf_records
FOR EACH ROW
BEGIN
    DECLARE field_count INT;
    
    -- Get the allowed fields for the module
    SELECT COUNT(*) INTO field_count 
    FROM scf_records_module_configurations WHERE module_id = NEW.module_id
      AND JSON_CONTAINS(scf_records_module_configurations.allowed_fields, JSON_KEYS(NEW.record_data)) = 0;

    -- If field_count > 0, it means there are disallowed fields
    IF field_count > 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Update failed. Disallowed fields in updated record data.';
    END IF;
END;
$$
DELIMITER ;
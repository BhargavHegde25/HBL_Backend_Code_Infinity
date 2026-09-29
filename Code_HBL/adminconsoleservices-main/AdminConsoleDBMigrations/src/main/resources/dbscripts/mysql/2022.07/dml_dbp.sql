ALTER TABLE `swiftcode` 
ADD COLUMN `bankAddress` VARCHAR(500) NULL,
ADD COLUMN `branchName` VARCHAR(100) NULL,
ADD COLUMN `zipcode` VARCHAR(50) NULL;

UPDATE `swiftcode` SET `bankAddress` = 'Avance, floor1', `branchName` = 'Tungabhadra', `zipcode` = '500090' WHERE (`id` = '1');
UPDATE `swiftcode` SET `bankAddress` = 'Pheonix, floor7', `branchName` = 'Indus', `zipcode` = '500091' WHERE (`id` = '2');
UPDATE `swiftcode` SET `bankAddress` = 'Madhapur, floor3', `branchName` = 'Pearl', `zipcode` = '500092' WHERE (`id` = '3');
UPDATE `swiftcode` SET `bankAddress` = 'Saharanpur, Up', `branchName` = 'Team Stark', `zipcode` = '500093' WHERE (`id` = '4');
UPDATE `swiftcode` SET `bankAddress` = 'Whitefields, Kondapur', `branchName` = 'Whitefields', `zipcode` = '500094' WHERE (`id` = '5');
UPDATE `swiftcode` SET `bankAddress` = 'Anjaiah Nagar, Gachibowli Street no 15', `branchName` = 'Team Stark branch', `zipcode` = '500095' WHERE (`id` = '6');
UPDATE `swiftcode` SET `bankAddress` = 'Siddqui, Gachibowli Street no 14', `branchName` = 'Jade', `zipcode` = '500096' WHERE (`id` = '7');
UPDATE `swiftcode` SET `bankAddress` = 'Shilpa Ramam, HitechCity Street no 13', `branchName` = 'Diamond', `zipcode` = '500097' WHERE (`id` = '8');
UPDATE `swiftcode` SET `bankAddress` = 'VanasthaliPuram, LB Nagar Street no 12', `branchName` = 'Agni', `zipcode` = '500098' WHERE (`id` = '9');
UPDATE `swiftcode` SET `bankAddress` = 'Nagole, LB Nagar Street no 11', `branchName` = 'Team Stark', `zipcode` = '500099' WHERE (`id` = '10');
UPDATE `swiftcode` SET `bankAddress` = 'Kony, HitechCity Street no 10', `branchName` = 'Kony', `zipcode` = '500080' WHERE (`id` = '11');
UPDATE `swiftcode` SET `bankAddress` = 'Madhapur, Hyderabad Street no 9', `branchName` = 'Kony', `zipcode` = '500081' WHERE (`id` = '12');
UPDATE `swiftcode` SET `bankAddress` = 'Nizampet, Hyderebad Street no 8', `branchName` = 'Temenos', `zipcode` = '500082' WHERE (`id` = '13');
UPDATE `swiftcode` SET `bankAddress` = 'Kukatpllay, Hyderabad Street no 7', `branchName` = 'Team Stark', `zipcode` = '500083' WHERE (`id` = '14');
UPDATE `swiftcode` SET `bankAddress` = 'Pragathi Nagar, Kukatpally Street no 6', `branchName` = 'Kony', `zipcode` = '500084' WHERE (`id` = '15');
UPDATE `swiftcode` SET `bankAddress` = 'Assam, Meghalaya Street no 5', `branchName` = 'Temenos', `zipcode` = '500085' WHERE (`id` = '16');
UPDATE `swiftcode` SET `bankAddress` = 'Secundrabad, Hyderabad Street no 4', `branchName` = 'Stark', `zipcode` = '500086' WHERE (`id` = '17');
UPDATE `swiftcode` SET `bankAddress` = 'Tirupathi, Andhra Street no 3', `branchName` = 'ASquare', `zipcode` = '500087' WHERE (`id` = '18');
UPDATE `swiftcode` SET `bankAddress` = 'Sathupally, Khammam Street no 2', `branchName` = 'BSquare', `zipcode` = '500088' WHERE (`id` = '19');
UPDATE `swiftcode` SET `bankAddress` = 'Vijayawada, Andhra Street no 1', `branchName` = 'Team Stark', `zipcode` = '500089' WHERE (`id` = '20');
UPDATE `swiftcode` SET `bankAddress` = 'Indore, MadhyaPradesh Street no 1', `branchName` = 'Temenos', `zipcode` = '500070' WHERE (`id` = '21');
UPDATE `swiftcode` SET `bankAddress` = 'Team Stark, USA Street no 1', `branchName` = 'Kony', `zipcode` = '500071' WHERE (`id` = '22');
UPDATE `swiftcode` SET `bankAddress` = 'Agni, India Street no 1', `branchName` = 'Temenos', `zipcode` = '500072' WHERE (`id` = '23');
UPDATE `swiftcode` SET `bankAddress` = 'Diamond, India Road no 12', `branchName` = 'Team Stark', `zipcode` = '500073' WHERE (`id` = '24');
UPDATE `swiftcode` SET `bankAddress` = 'Jade, India Road no 11', `branchName` = 'Diamond', `zipcode` = '500074' WHERE (`id` = '25');
UPDATE `swiftcode` SET `bankAddress` = 'PatrikaNagar, Madhapur Road no 10', `branchName` = 'Agni', `zipcode` = '500075' WHERE (`id` = '54');
UPDATE `swiftcode` SET `bankAddress` = 'Oracle, Madhapur Road no 9', `branchName` = 'Yamuna', `zipcode` = '500076' WHERE (`id` = '55');
UPDATE `swiftcode` SET `bankAddress` = 'Vemsur, Madhapur Road no 8', `branchName` = 'Krishna', `zipcode` = '500077' WHERE (`id` = '56');
UPDATE `swiftcode` SET `bankAddress` = 'Guntur, Andhra Road no 7', `branchName` = 'Tapati', `zipcode` = '500078' WHERE (`id` = '57');
UPDATE `swiftcode` SET `bankAddress` = 'Prakasam, Andhra Road no 6', `branchName` = 'Musi', `zipcode` = '500079' WHERE (`id` = '58');
UPDATE `swiftcode` SET `bankAddress` = 'Vizag, Andhra Road no 5', `branchName` = 'Ellora', `zipcode` = '500060' WHERE (`id` = '59');
UPDATE `swiftcode` SET `bankAddress` = 'Rajamundry, Andhra Road no 4', `branchName` = 'Sarkar', `zipcode` = '500061' WHERE (`id` = '87');
UPDATE `swiftcode` SET `bankAddress` = 'Bhadrachalam, USA Road no 3', `branchName` = 'Janatha', `zipcode` = '500062' WHERE (`id` = '88');
UPDATE `swiftcode` SET `bankAddress` = 'Palvancha, USA Road no 2', `branchName` = 'Vishaka', `zipcode` = '500063' WHERE (`id` = '89');
UPDATE `swiftcode` SET `bankAddress` = 'Sathupally, USA Road no 1', `branchName` = 'Simha', `zipcode` = '500064' WHERE (`id` = '126');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`) 
VALUES ('176', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SWIFT_TAG_ENABLE', 'Enable or Disable Swift Tags for TradeFinance - Import Letter of Credits', '{\"Swift Enable\":\"True\"}', 'CLIENT', '0', 'UID10');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`) 
VALUES ('174', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SWIFT_TAG', 'Swift Tags for TradeFinance - Import Letter of Credits', '{\"referenceNumber\":\"20\",\"currencyAmount\":\"32B\",\"tolerancePercentage\":\"39A\",\"maximumCreditAmount\":\"39B\",\"currencyAdditionalPayableAmount\":\"39C\",\"paymentTerms\":\"40A\",\"availableWith\":\"41A\",\"issueDate\":\"31C\",\"expiryDate\":\"31D\",\"expiryPlace\":\"31D\",\"chargesAccount\":\"71D\",\"commissionAccount\":\"NA\",\"marginAccount\":\"NA\",\"messageToBank\":\"NA\",\"beneficiaryName\":\"59\",\"beneficiaryAddress\":\"59\",\"beneficiaryCity\":\"59\",\"beneficiaryState\":\"59\",\"beneficiaryZipCode\":\"59\",\"beneficiaryBankName\":\"57A\",\"beneficiaryBankAddress\":\"57A\",\"beneficiaryBankCity\":\"57A\",\"beneficiaryBankState\":\"57A\",\"beneficiaryBankZipCode\":\"57A\",\"placeOfTakingInCharge\":\"44A\",\"portOfLoading\":\"44E\",\"portOfDischarge\":\"44F\",\"placeOfFinalDelivery\":\"44B\",\"latestShipmentDate\":\"44C\",\"transhipment\":\"43T\",\"partialShipment\":\"43P\",\"incoTerms\":\"44D\",\"modeOfShipment\":\"NA\",\"descriptionOfGoods\":\"45A\",\"documentsRequired\":\"46A\",\"additionalCondition\":\"47A\",\"otherAdditionalCondition\":\"NA\",\"charges\":\"71D\",\"confirmationInstructions\":\"49\",\"transferable\":\"NA\",\"standByLC\":\"NA\",\"uploadedDocuments\":\"NA\",\"sequenceOfTotal\":\"27\",\"applicableRules\":\"40E\",\"applicant\":\"50\",\"draftsAt\":\"42C\",\"drawee\":\"42D\",\"loadonBoard\":\"44A\",\"detailsofcharges\":\"71B\",\"periodforPresentation\":\"48\",\"reimbursingBank\":\"53A\",\"instructionsToThePay\":\"78\",\"senderToReceiverInformation\":\"72\"}', 'CLIENT', '0', 'UID10');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EXPORT_LC_AMENDMENT_VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EXPORT_LC_AMENDMENT_UPDATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('EXPORT_LC_AMENDMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Export LC Amendment' ,'View & Manage the Export LC Amendment.' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EXPORT_LC_AMENDMENT_VIEW','EXPORT_LC_AMENDMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EXPORT_LC_AMENDMENT_VIEW','View Export LC Amendment','The clerk, Manager & soletraders can view the export LC Amendment',1,0,null,0,10,null,0,'VIEW','ACCOUNT_LEVEL');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EXPORT_LC_AMENDMENT_UPDATE','EXPORT_LC_AMENDMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EXPORT_LC_AMENDMENT_UPDATE','View Export LC Amendment','The clerk, Manager & soletraders can view the export LC Amendment',1,0,null,0,10,null,0,'VIEW','ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','EXPORT_LC_AMENDMENT_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','EXPORT_LC_AMENDMENT_VIEW',null,null,'UID11',null,0 );

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','EXPORT_LC_AMENDMENT_UPDATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','EXPORT_LC_AMENDMENT_UPDATE',null,null,'UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EXPORT_LC_AMENDMENT_VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EXPORT_LC_AMENDMENT_UPDATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'EXPORT_LC_AMENDMENT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'EXPORT_LC_AMENDMENT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'EXPORT_LC_AMENDMENT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'EXPORT_LC_AMENDMENT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('EXPORT_LC_AMENDMENT' ,'en-GB' ,'Export LC Amendments' ,'View & Manage the Export LC Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('EXPORT_LC_AMENDMENT' ,'en-US' ,'Export LC Amendments' ,'View & Manage the Export LC Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'EXPORT_LC_AMENDMENT' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_VIEW' , 'de-DE' , 'Export LC Amendments View' , 'Export LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_VIEW' , 'en-GB' , 'Export LC Amendments View' , 'Export LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_VIEW' , 'en-US' , 'Export LC Amendments View' , 'Export LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_VIEW' , 'es-ES' , 'Export LC Amendments View' , 'Export LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_VIEW' , 'fr-FR' , 'Export LC Amendments View' , 'Export LC Amendments View' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_UPDATE' , 'de-DE' , 'Export LC Amendments Update' , 'Export LC Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_UPDATE' , 'en-GB' , 'Export LC Amendments Update' , 'Export LC Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_UPDATE' , 'en-US' , 'Export LC Amendments Update' , 'Export LC Amendments  Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_UPDATE' , 'es-ES' , 'Export LC Amendments Update' , 'Export LC Amendments Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_AMENDMENT_UPDATE' , 'fr-FR' , 'Export LC Amendments Update' , 'Export LC Amendments Update' ) ;

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7t87b62-4125-11yc-973a-0242lm130003','TradeFinance','ExportLetterOfCredit','getExportLCAmmendments','EXPORT_LC_AMENDMENT_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7i87b62-4125-11yc-973a-0242li130003','TradeFinance','ExportLetterOfCredit','getExportLCAmendmentById','EXPORT_LC_AMENDMENT_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7z87b62-4125-11yc-973a-0242li130003','TradeFinance','ExportLetterOfCredit','generateExportLetterOfCreditAmendment','EXPORT_LC_AMENDMENT_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7x87b62-4125-11yc-973a-0242li130003','TradeFinance','ExportLetterOfCredit','updateExportLCAmendment','EXPORT_LC_AMENDMENT_UPDATE' );

CREATE TABLE `corporatepayees` (`id` INT NOT NULL AUTO_INCREMENT,`accountNumber` VARCHAR(75) NULL,`contractId` VARCHAR(75) NULL,`customerId` VARCHAR(75) NULL,`coreCustomerId` VARCHAR(75) NULL,`cif` VARCHAR(400) NULL,`name` VARCHAR(100) NOT NULL,
`firstName` VARCHAR(75) NULL,`lastName` VARCHAR(75) NULL,`nickName` VARCHAR(45) NULL,`address1` VARCHAR(300) NULL,`address2` VARCHAR(300) NULL,`city` VARCHAR(80) NULL,`state` VARCHAR(80) NULL,`country` VARCHAR(80) NULL,`zipcode` VARCHAR(80) NULL,
`phoneNumber` VARCHAR(45) NULL,`email` VARCHAR(75) NULL,`companyName` VARCHAR(100) NULL,`organizationId` VARCHAR(45) NULL,`iban` VARCHAR(75) NULL,`swiftcode` VARCHAR(75) NULL,`bankName` VARCHAR(100) NULL,`bankAddressLine1` VARCHAR(100) NULL,
`bankAddressLine2` VARCHAR(100) NULL,`bankCity` VARCHAR(85) NULL,`bankState` VARCHAR(85) NULL,`bankZip` VARCHAR(50) NULL,`internationalRoutingCode` VARCHAR(80) NULL,`phoneCountryCode` VARCHAR(45) NULL,`phoneExtension` VARCHAR(45) NULL,`softDelete` VARCHAR(45) NULL,
PRIMARY KEY (`id`)) AUTO_INCREMENT = 1000;

CREATE TABLE `clauses` (`clauseId` INT NOT NULL AUTO_INCREMENT,`clauseType` VARCHAR(35) NULL,`clauseDescription` TEXT NULL,
`clauseTitle` VARCHAR(100) NULL,PRIMARY KEY (`clauseId`)) AUTO_INCREMENT=1000;

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_CREATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_UPDATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_DELETE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('LC_GUARANTEES' ,'RETAIL_AND_BUSINESS_BANKING' ,'Lc Guarantees' ,'Create View & Manage the Lc Guarantees' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_CREATE','LC_GUARANTEES','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_CREATE','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_VIEW','LC_GUARANTEES','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_VIEW','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees',1,0,null,0,10,null,0,'VIEW','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_UPDATE','LC_GUARANTEES','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_UPDATE','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_DELETE','LC_GUARANTEES','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_DELETE','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees',1,0,null,0,10,null,0,'DELETE','ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_UPDATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_UPDATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_DELETE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_DELETE',null,null,'UID11',null,0 );


INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_UPDATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_DELETE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('LC_GUARANTEES' ,'en-GB' ,'LC GUARANTEES' ,'Create View & Manage the LC Guarantees' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('LC_GUARANTEES' ,'en-US' ,'LC GUARANTEES' ,'Create View & Manage the LC Guarantees' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'LC_GUARANTEES' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_CREATE' , 'de-DE' , 'Guarantees Create' ,'Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_CREATE' , 'en-GB' , 'Guarantees Create' ,'Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_CREATE' , 'en-US' , 'Guarantees Create' ,'Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_CREATE' , 'es-ES' , 'Guarantees Create' ,'Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_CREATE' , 'fr-FR' , 'Guarantees Create' ,'Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_VIEW' , 'de-DE' , 'Guarantees View' ,'Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_VIEW' , 'en-GB' , 'Guarantees View' ,'Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_VIEW' , 'en-US' , 'Guarantees View' ,'Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_VIEW' , 'es-ES' , 'Guarantees View' ,'Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_VIEW' , 'fr-FR' , 'Guarantees View' ,'Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_UPDATE' , 'de-DE' , 'Guarantees Update' ,'Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_UPDATE' , 'en-GB' , 'Guarantees Update' ,'Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_UPDATE' , 'en-US' , 'Guarantees Update' ,'Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_UPDATE' , 'es-ES' , 'Guarantees Update' ,'Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_UPDATE' , 'fr-FR' , 'Guarantees Update' ,'Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_DELETE' , 'de-DE' , 'Guarantees Delete' ,'Guarantees Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_DELETE' , 'en-GB' , 'Guarantees Delete' ,'Guarantees Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_DELETE' , 'en-US' , 'Guarantees Delete' ,'Guarantees Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_DELETE' , 'es-ES' , 'Guarantees Delete' ,'Guarantees Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_DELETE' , 'fr-FR' , 'Guarantees Delete' ,'Guarantees Delete' ) ;

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7qw7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'createGuarantees' ,'LC_GUARANTEES_CREATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7qx7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'getGuaranteesById' ,'LC_GUARANTEES_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7qz7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'getGuarantees' ,'LC_GUARANTEES_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7qk7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'saveGuarantees' ,'LC_GUARANTEES_UPDATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7ql7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'DeleteGuaranteeLetterOfCredit' ,'LC_GUARANTEES_DELETE' );

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('CORPORATE_RECIPIENT_CREATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('CORPORATE_RECIPIENT_VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('CORPORATE_RECIPIENT_UPDATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('CORPORATE_RECIPIENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Corporate Payee' ,'Create View & Manage the Corporate Payee' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('CORPORATE_RECIPIENT_CREATE','CORPORATE_RECIPIENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CORPORATE_RECIPIENT_CREATE','Create View & Manage the Corporate Payee','Create View & Manage the Corporate Payee',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('CORPORATE_RECIPIENT_VIEW','CORPORATE_RECIPIENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CORPORATE_RECIPIENT_VIEW','Create View & Manage the Corporate Payee','Create View & Manage the Corporate Payee',1,0,null,0,10,null,0,'VIEW','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('CORPORATE_RECIPIENT_UPDATE','CORPORATE_RECIPIENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CORPORATE_RECIPIENT_UPDATE','Create Update & Manage the Corporate Payee','Create Update & Manage the Corporate Payee',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','CORPORATE_RECIPIENT_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','CORPORATE_RECIPIENT_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','CORPORATE_RECIPIENT_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','CORPORATE_RECIPIENT_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','CORPORATE_RECIPIENT_UPDATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','CORPORATE_RECIPIENT_UPDATE',null,null,'UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','CORPORATE_RECIPIENT_CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','CORPORATE_RECIPIENT_VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','CORPORATE_RECIPIENT_UPDATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CORPORATE_RECIPIENT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CORPORATE_RECIPIENT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CORPORATE_RECIPIENT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CORPORATE_RECIPIENT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CORPORATE_RECIPIENT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'CORPORATE_RECIPIENT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('CORPORATE_RECIPIENT' ,'en-GB' ,'Corporate Payee' ,'Create View & Manage the Corporate Payee' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('CORPORATE_RECIPIENT' ,'en-US' ,'Corporate Payee' ,'Create View & Manage the Corporate Payee' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'CORPORATE_RECIPIENT' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_CREATE' , 'de-DE' , 'Create Corporate Payee' ,'Create Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_CREATE' , 'en-GB' , 'Create Corporate Payee' ,'Create Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_CREATE' , 'en-US' , 'Create Corporate Payee' ,'Create Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_CREATE' , 'es-ES' , 'Create Corporate Payee' ,'Create Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_CREATE' , 'fr-FR' , 'Create Corporate Payee' ,'Create Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_VIEW' , 'de-DE' , 'View Corporate Payee' ,'View Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_VIEW' , 'en-GB' , 'View Corporate Payee' ,'View Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_VIEW' , 'en-US' , 'View Corporate Payee' ,'View Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_VIEW' , 'es-ES' , 'View Corporate Payee' ,'View Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_VIEW' , 'fr-FR' , 'View Corporate Payee' ,'View Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_UPDATE' , 'de-DE' , 'Update Corporate Payee' ,'Update Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_UPDATE' , 'en-GB' , 'Update Corporate Payee' ,'Update Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_UPDATE' , 'en-US' , 'Update Corporate Payee' ,'Update Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_UPDATE' , 'es-ES' , 'Update Corporate Payee' ,'Update Corporate Payee' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('CORPORATE_RECIPIENT_UPDATE' , 'fr-FR' , 'Update Corporate Payee' ,'Update Corporate Payee' ) ;

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7pa7kia-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'Payee' ,'createCorporatePayee' ,'CORPORATE_RECIPIENT_CREATE' ) ;
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7pb7kia-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'Payee' ,'getCorporatePayees' ,'CORPORATE_RECIPIENT_VIEW' ) ;

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('GUA_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'GUARANTEE_BILL_TYPE', 'Guarantee Bill Type', '[{\"type\":\"Performance\",\"colorCode\":\"#E50033\"},{\"type\":\"BID\",\"colorCode\":\"#FF8600\"},{\"type\":\"Advance\",\"colorCode\":\"#229EAE\"},{\"type\":\"Shipping\",\"colorCode\":\"#0971A8\"}]', 'CLIENT', '1');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('GUA_2', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'GUARANTEE_STATUS', 'Guarantee Status', '[{\"DisplayStatus\":\"Pending\",\"LCStatus\":[\"Pending Cust Auth\",\"Returned Cust Auth\",\"Submitted to Bank\",\"Returned by Bank\",\"Processing with Bank\"]},{\"DisplayStatus\":\"Drafts\",\"LCStatus\":[\"Draft\"]},{\"DisplayStatus\":\"Approved\",\"LCStatus\":[\"Approved\"]},{\"DisplayStatus\":\"Rejected\",\"LCStatus\":[\"Rejected by Bank\"]}]', 'CLIENT', '1');


INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Regulatory Condition', 'SUCH PAYMENT SHALL BE MADE WITHOUT SET-OFF AND CLEAR OF ANY DEDUCTIONS OR CHARGES, FEES OR LEVIES, COLLECTED, WITHHELD OR ASSESSED BY GOVERNMENT OF <COUNTRY OF ISSUING BANK> OR ANY POLITICAL SUB-DIVISION OR AUTHORITY THEREOF OR THEREIN.', 'Regulatory Condition');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Transferable Conditions', 'THIS STANDBY LETTER OF CREDIT IS TRANSFERABLE AND ASSIGNABLE WITHOUT PRESENTATION TO US OR PAYMENT OF ANY TRANSFER OR ASSIGNABLE FEE.', 'Transferable Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Rules Conditions', 'THIS STANDBY LETTER OF CREDIT IS SUBJECT TO THE UNIFORM CUSTOMS AND PRACTICE FOR DOCUMENTARY CREDITS (1993 VERSION) OF THE INTERNATIONAL CHAMBER OF COMMERCE, PUBLICATION NO. 600 OR ITS LATEST REVISION.', 'Rules Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Regulated Country Conditions', 'THIS STANDBY LETTER OF CREDIT SHALL BE GOVERNED BY AND SHALL BE CONSTRUED IN ACCORDANCE WITH THE LAWS OF <COUNTRY OF ISSUING BANK>.', 'Regulated Country Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('SBLC undertaking Conditions', 'FOR VALUE RECEIVED WE, (NAME & ADDRESS OF ISSUING BANK) HEREBY ISSUE OUR, IRREVOCABLE, UNCONDITIONAL, TRANSFERABLE, CONFIRMED, AND ASSIGNABLESTANDBY LETTER OF CREDIT, AND WITHOUT PROTEST OR NOTIFICATION PROMISE TO PAY AGAINST THIS STANDBY LETTER OF CREDIT ON TIME, IN FULL AND WITHOUT DELAY, TO THE ORDER OF XXXXXXXXXXX THE BEARER OR HOLDER THEREOF, AT MATURITY THE SUM OF US$XXXXXXXXXX (AMOUNT IN WORDS) IN THE LAWFUL CURRENCY OF THE UNITED STATES OF AMERICA.SUCH PAYMENT WILL BE MADE UPON PRESENTATION AND SURRENDER OF THIS STANDBY LETTER OF CREDIT AT THE OFFICE OF (NAME AND ADDRESS OF ISSUING BANK), WITHOUT SET-OFF AND FREE AND CLEAR OF ANY DEDUCTIONS, CHARGES, FEES OR WITH BANK CHARGES OF ANY NATURE NOW OR HEREAFTER IMPOSED, LEVIED, COLLECTED, WITHHELD OR ASSESSED BY THE GOVERMENT OF THE ISSUING OR PAYING BANK OR ANY POLITICAL SUBDIVISION OR AUTHORITY THEREOF OR THEREIN. THIS STANDBY LETTER OF CREDIT SHALL BE GOVERNED AND BE CONSTRUED IN ACCORDANCE WITH THE UNIFORM RULES FOR DEMAND STANDBY LETTER OF CREDIT (URDG), AS SET FORTH BY INTERNATIONAL CHAMBER OF COMMERCE, PARIS, FRANCE. I.C.C.PUBLICATION NUMBER 600.', 'SBLC undertaking Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Delivery Conditions', 'THE ORIGINAL HARD COPY OF THIS CASH BACKED STANDBY LETTER OF CREDIT WILL FOLLOW BY BANK BONDED COURIER DELIVERY TO COORDINATES AS PER YOUR INSTRUCTION FOR DELIVERY WITHIN SEVEN (7) DAYS OF THIS DATE OF MT760 ISSUE.', 'Delivery Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Charges Conditions', 'THIS STANDBY LETTER OF CREDIT IS AN OPERATIVE INSTRUMENT AND ALL CHARGES ARE FOR THE ACCOUNT OF APPLICANT.', 'Charges Conditions');

INSERT INTO `clauses` (`clauseType`, `clauseDescription`, `clauseTitle`) VALUES ('Issuing Undertaking Confirmation', 'FOR VALUE RECEIVED, WE, THE UNDERSIGNED <ISSUING BANK>, <BANK ADDRESS>, HEREBY ISSUE OUR IRREVOCABLE, UNCONDITIONAL, ASSIGNABLE, TRANSFERABLE AND CASH BACKED STANDBY LETTER OF CREDIT NO.: _____ AND WITHOUT PROTEST OR NOTIFICATION PROMISE TO PAY AGAINST THIS STANDBY LETTER OF CREDIT TO THE ORDER OF ________ AND FOR THE BENEFIT OF ________ OR THE BEARER OR HOLDER THEREOF AT MATURITY <DD/MM/YY> THE SUM OF ______ HUNDRED MILLION EURO ONLY (€___,000,000.00 ONLY) IN THE LAWFUL CURRENCY OF THE EUROPEAN UNION UPON PRESENTATION AND SURRENDER OF THIS STANDBY LETTER OF CREDIT AT THE OFFICE OF <ISSUING BANK>, <BANK ADDRESS>, ON MATURITY DATE, BUT NOT LATER THAN FIFTEEN DAYS AFTER THE MATURITY DATE.', 'Issuing Undertaking Confirmation');

ALTER TABLE `clauses` 
ADD COLUMN `createdBy` VARCHAR(45) NULL AFTER `clauseTitle`;

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7pc7kia-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'Payee' ,'editPayee' ,'CORPORATE_RECIPIENT_UPDATE') ;

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('LC_GUARANTEES_AMENDMENTS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Lc Guarantees Amendments' ,'Create View & Manage the Lc Guarantees Amendments' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE','LC_GUARANTEES_AMENDMENTS','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_AMENDMENTS_CREATE','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees Amendments',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW','LC_GUARANTEES_AMENDMENTS','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_AMENDMENTS_VIEW','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees Amendments',1,0,null,0,10,null,0,'VIEW','ACCOUNT_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE','LC_GUARANTEES_AMENDMENTS','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','LC_GUARANTEES_AMENDMENTS_UPDATE','Create View & Manage the Lc Guarantees','Create View & Manage the Lc Guarantees Amendments',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_AMENDMENTS_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_AMENDMENTS_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_AMENDMENTS_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_AMENDMENTS_VIEW',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','LC_GUARANTEES_AMENDMENTS_UPDATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','LC_GUARANTEES_AMENDMENTS_UPDATE',null,null,'UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_AMENDMENTS_CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_AMENDMENTS_VIEW',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','LC_GUARANTEES_AMENDMENTS_UPDATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_AMENDMENTS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_AMENDMENTS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_AMENDMENTS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_AMENDMENTS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'LC_GUARANTEES_AMENDMENTS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LC_GUARANTEES_AMENDMENTS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('LC_GUARANTEES_AMENDMENTS' ,'en-GB' ,'LC GUARANTEES AMENDMENT' ,'Create View & Manage the LC Guarantees Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('LC_GUARANTEES_AMENDMENTS' ,'en-US' ,'LC GUARANTEES AMENDMENT' ,'Create View & Manage the LC Guarantees Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'LC_GUARANTEES_AMENDMENTS' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE' , 'de-DE' , 'Amendments Guarantees Create' ,'Amendments Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE' , 'en-GB' , 'Amendments Guarantees Create' ,'Amendments Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE' , 'en-US' , 'Amendments Guarantees Create' ,'Amendments Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE' , 'es-ES' , 'Amendments Guarantees Create' ,'Amendments Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_CREATE' , 'fr-FR' , 'Amendments Guarantees Create' ,'Amendments Guarantees Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW' , 'de-DE' , 'Amendments Guarantees View' ,'Amendments Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW' , 'en-GB' , 'Amendments Guarantees View' ,'Amendments Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW' , 'en-US' , 'Amendments Guarantees View' ,'Amendments Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW' , 'es-ES' , 'Amendments Guarantees View' ,'Amendments Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_VIEW' , 'fr-FR' , 'Amendments Guarantees View' ,'Amendments Guarantees View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE' , 'de-DE' , 'Amendments Guarantees Update' ,'Amendments Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE' , 'en-GB' , 'Amendments Guarantees Update' ,'Amendments Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE' , 'en-US' , 'Amendments Guarantees Update' ,'Amendments Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE' , 'es-ES' , 'Amendments Guarantees Update' ,'Amendments Guarantees Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('LC_GUARANTEES_AMENDMENTS_UPDATE' , 'fr-FR' , 'Amendments Guarantees Update' ,'Amendments Guarantees Update' ) ;

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7ka7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'createGuaranteeAmendment' ,'LC_GUARANTEES_AMENDMENTS_CREATE');
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7kb7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'getGuaranteeAmendmentById' ,'LC_GUARANTEES_AMENDMENTS_VIEW');
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7kc7kia-4125-11yc-973a-0242lm130003','TradeFinance','Guarantees' ,'getGuaranteeAmendments' ,'LC_GUARANTEES_AMENDMENTS_VIEW');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`) 
VALUES ('175', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SWIFT_GUARANTEES_TAG_ENABLE', 'Enable or Disable Swift Tags for TradeFinance - Guarantees and Standby LC', '{\"Swift Enable\":\"True\"}', 'CLIENT', '0', 'UID10');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`)
VALUES ('177', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SWIFT_GUARANTEES_TAG', 'Swift Tags for TradeFinance - Guarantees and Standby LC', '{\"New Sequence\":\"15A\",\"Sequence of total\":\"27\",\"Purpose of Message\":\"22A\",\"New Sequence1\":\"15B\",\"Undertaking Number\":\"15B\",\"Date of issue\":\"30\",\"Form of Undertaking\":\"22D\",\"Applicable Rules\":\"40C\",\"Expiry Type\":\"23B\",\"Date of Expiry\":\"31E\",\"Applicant\":\"50\",\"Issue\":\"52A\",\"Beneficiary\":\"59A\",\"Advising Bank\":\"56A\",\"Advise Through Bank\":\"57A\",\"Undertaking Amount\":\"32B\",\"Available With\":\"41A\",\"Charges\":\"71D\",\"Document and Presentation Instructions\":\"45C\",\"Undertaking Terms and Conditions\":\"77U\",\"Confirmation Instructions\":\"49\",\"Governing Law and/or Placed of Jurisdiction\":\"44H\",\"Automatic Extension Period\":\"23F\",\"Automatic Extension Non-Extension Notification\":\"78\",\"Automatic Extension Notification Period\":\"26E\",\"Automatic Extension Final Expiry Date\":\"31S\",\"Demand Indicator\":\"48B\",\"Underlying Transaction Details\":\"45L\",\"Delivery of Original Undertaking\":\"24E\",\"Delivery ToCollection By\":\"24G\"}', 'CLIENT', '0', 'UID10');

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352kml12907','TradeFinance','Guarantees','getClauses','LC_GUARANTEES_CREATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352lpo12907','TradeFinance','Guarantees','createClause','LC_GUARANTEES_CREATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352tol12907','TradeFinance','Guarantees','updateClause','LC_GUARANTEES_CREATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352hik12907','TradeFinance','Guarantees','getLimitInstructions','LC_GUARANTEES_CREATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352lew12907','TradeFinance','Guarantees','updateGuaranteeLetterOfCredit','LC_GUARANTEES_UPDATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352zpe12907','TradeFinance','Guarantees','updateGuaranteeAmendment','LC_GUARANTEES_AMENDMENTS_UPDATE' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352elk12907','TradeFinance','Guarantees','generateGuarantees','LC_GUARANTEES_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352qkl12907','TradeFinance','LCSummary','generateGuaranteesList','LC_GUARANTEES_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352mak12907','TradeFinance','Guarantees','generateGuaranteeAmendment','LC_GUARANTEES_AMENDMENTS_VIEW' );
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('m7e34i71-3178-45em-820y-1352arv12907','TradeFinance','Guarantees','generateGuaranteeAmendmentsList','LC_GUARANTEES_AMENDMENTS_VIEW' );

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('DOCUMENTS',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('DOCUMENTS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Document' ,'View Document' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '75' , '0' , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('DOCUMENTS' ,'en-GB' ,'Documents' ,'View Documents' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('DOCUMENTS' ,'de-DE' ,'Documents' ,'View Documents' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('DOCUMENTS' ,'en-US' ,'Documents' ,'View Documents' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('DOCUMENTS' ,'es-ES' ,'Documents' ,'View Documents' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('DOCUMENTS' ,'fr-FR' ,'Documents' ,'View Documents' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'DOCUMENTS');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_WEALTH', 'DOCUMENTS');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'DOCUMENTS');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('VIEW_DOCUMENTS','DOCUMENTS','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','DOCUMENTS','View Document','View Document',1,0,null,0,10,null,0,'VIEW','CUSTOMERID_LEVEL');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('VIEW_DOCUMENTS','en-US', 'View Document', 'View Document');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_WEALTH','VIEW_DOCUMENTS',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','VIEW_DOCUMENTS',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_RETAIL','VIEW_DOCUMENTS',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('VIEW_DOCUMENTS', 'VIEW_DOCUMENTS', 'DOCUMENTS', 'View Document', 'Documents');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID354','PID45', 'VIEW_DOCUMENTS', 'DOCUMENTS', '1');
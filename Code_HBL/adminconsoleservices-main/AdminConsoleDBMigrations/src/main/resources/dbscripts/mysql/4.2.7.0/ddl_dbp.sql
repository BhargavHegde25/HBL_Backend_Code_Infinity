ALTER TABLE `achfile` 
DROP FOREIGN KEY `FK_achfile_Company_id`;
ALTER TABLE `achfile` 
DROP INDEX `FK_achfile_Company_id_idx` ;

ALTER TABLE `bbactedrequest` 
DROP FOREIGN KEY `FK_bbactedrequest_Organisation_id`;
ALTER TABLE `bbactedrequest` 
DROP INDEX `FK_bbactedrequest_Organisation_id_idx` ;

ALTER TABLE `bbrequest` 
DROP FOREIGN KEY `FK_bbrequest_businessbankingcompany`;
ALTER TABLE `bbrequest` 
DROP INDEX `FK_bbrequest_businessbankingcompany_idx` ;

ALTER TABLE `bbtemplate` 
DROP FOREIGN KEY `FK_bbtemplate_Organization`;
ALTER TABLE `bbtemplate` 
DROP INDEX `FK_bbtemplate_Organization_idx` ;

ALTER TABLE `bbtransaction` 
DROP FOREIGN KEY `FK_bbtransaction_Organization`;
ALTER TABLE `bbtransaction` 
DROP INDEX `FK_bbtransaction_Organization_idx` ;

DROP TABLE IF EXISTS `bbgeneraltransaction`;



ALTER TABLE `organisation` 
CHANGE COLUMN `id` `id` VARCHAR(50) NOT NULL ;

ALTER TABLE `accounts` 
ADD COLUMN `isBusinessAccount` TINYINT(1) NOT NULL DEFAULT 0 AFTER `Name`;

DROP VIEW IF EXISTS `getaccountsview`;
CREATE VIEW `getaccountsview` AS select `accounts`.`Account_id` AS `Account_id`,`accounts`.`Type_id` AS `Type_id`,`accounts`.`UserName` AS `userName`,`accounts`.`CurrencyCode` AS `currencyCode`,`accounts`.`AccountHolder` AS `accountHolder`,`accounts`.`isBusinessAccount` AS `isBusinessAccount`,`accounts`.`error` AS `error`,`accounts`.`Address` AS `Address`,`accounts`.`Scheme` AS `Scheme`,`accounts`.`Number` AS `number`,`accounts`.`AvailableBalance` AS `availableBalance`,`accounts`.`CurrentBalance` AS `currentBalance`,`accounts`.`InterestRate` AS `interestRate`,`accounts`.`AvailableCredit` AS `availableCredit`,`accounts`.`MinimumDue` AS `minimumDue`,`accounts`.`DueDate` AS `dueDate`,`accounts`.`FirstPaymentDate` AS `firstPaymentDate`,`accounts`.`ClosingDate` AS `closingDate`,`accounts`.`PaymentTerm` AS `paymentTerm`,`accounts`.`OpeningDate` AS `openingDate`,`accounts`.`MaturityDate` AS `maturityDate`,`accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,`accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,`accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,`accounts`.`DividendRate` AS `dividendRate`,`accounts`.`DividendYTD` AS `dividendYTD`,`accounts`.`EStatementmentEnable` AS `eStatementEnable`,`accounts`.`FavouriteStatus` AS `favouriteStatus`,`accounts`.`StatusDesc` AS `statusDesc`,`accounts`.`NickName` AS `nickName`,`accounts`.`User_id` AS `User_id`,`accounts`.`OriginalAmount` AS `originalAmount`,`accounts`.`OutstandingBalance` AS `outstandingBalance`,`accounts`.`PaymentDue` AS `paymentDue`,`accounts`.`PaymentMethod` AS `paymentMethod`,`accounts`.`SwiftCode` AS `swiftCode`,`accounts`.`TotalCreditMonths` AS `totalCreditMonths`,`accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,`accounts`.`RoutingNumber` AS `routingNumber`,`accounts`.`SupportBillPay` AS `supportBillPay`,`accounts`.`SupportCardlessCash` AS `supportCardlessCash`,`accounts`.`SupportTransferFrom` AS `supportTransferFrom`,`accounts`.`SupportTransferTo` AS `supportTransferTo`,`accounts`.`SupportDeposit` AS `supportDeposit`,`accounts`.`UnpaidInterest` AS `unpaidInterest`,`accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,`accounts`.`principalBalance` AS `principalBalance`,`accounts`.`PrincipalValue` AS `principalValue`,`accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,`accounts`.`phone` AS `phoneId`,`accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,`accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,`accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,`accounts`.`LastPaymentDate` AS `lastPaymentDate`,`accounts`.`LastStatementBalance` AS `lastStatementBalance`,`accounts`.`LateFeesDue` AS `lateFeesDue`,`accounts`.`maturityAmount` AS `maturityAmount`,`accounts`.`MaturityOption` AS `maturityOption`,`accounts`.`payoffAmount` AS `payoffAmount`,`accounts`.`PayOffCharge` AS `payOffCharge`,`accounts`.`PendingDeposit` AS `pendingDeposit`,`accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,`accounts`.`JointHolders` AS `jointHolders`,`accounts`.`IsPFM` AS `isPFM`,`accounts`.`InterestPaidYTD` AS `interestPaidYTD`,`accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,`accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,`accounts`.`InterestEarned` AS `interestEarned`,`accounts`.`CurrentAmountDue` AS `currentAmountDue`,`accounts`.`CreditLimit` AS `creditLimit`,`accounts`.`CreditCardNumber` AS `creditCardNumber`,`accounts`.`BsbNum` AS `bsbNum`,`accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,`accounts`.`BondInterest` AS `bondInterest`,`accounts`.`AvailablePoints` AS `availablePoints`,`accounts`.`AccountName` AS `accountName`,`accounts`.`email` AS `email`,`accounts`.`IBAN` AS `IBAN`,`accounts`.`adminProductId` AS `adminProductId`,`bank`.`Description` AS `bankname`,`accounts`.`AccountPreference` AS `accountPreference`,`accounttype`.`transactionLimit` AS `transactionLimit`,`accounttype`.`transferLimit` AS `transferLimit`,`accounttype`.`rates` AS `rates`,`accounttype`.`termsAndConditions` AS `termsAndConditions`,`accounttype`.`TypeDescription` AS `typeDescription`,`accounttype`.`supportChecks` AS `supportChecks`,`accounttype`.`displayName` AS `displayName`,`accounts`.`accountSubType` AS `accountSubType`,`accounts`.`description` AS `description`,`accounts`.`schemeName` AS `schemeName`,`accounts`.`identification` AS `identification`,`accounts`.`secondaryIdentification` AS `secondaryIdentification`,`accounts`.`servicerSchemeName` AS `servicerSchemeName`,`accounts`.`servicerIdentification` AS `servicerIdentification`,`accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,`accounts`.`dataType` AS `dataType`,`accounts`.`dataDateTime` AS `dataDateTime`,`accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,`accounts`.`dataCreditLineType` AS `dataCreditLineType`,`accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,`accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency`,`accounts`.`UpdatedBy` AS `UpdatedBy`,`accounts`.`LastUpdated` AS `LastUpdated`,`accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY` from ((`accounts` join `accounttype`) join `bank`) where ((`accounts`.`Type_id` = `accounttype`.`TypeID`) and (`accounts`.`Bank_id` = `bank`.`id`));

DROP VIEW IF EXISTS `customeraccountsview`;
CREATE VIEW `customeraccountsview` AS select distinct `membershipaccounts`.`Membership_id` AS `Membership_id`,`membershipaccounts`.`Taxid` AS `Taxid`,`customeraccounts`.`Customer_id` AS `Customer_id`,`customeraccounts`.`Customer_id` AS `User_id`,`accounts`.`Account_id` AS `Account_id`,`accounts`.`isBusinessAccount` AS `isBusinessAccount`,`accounts`.`Type_id` AS `Type_id`,`accounts`.`UserName` AS `userName`,`accounts`.`CurrencyCode` AS `currencyCode`,`accounts`.`AccountHolder` AS `accountHolder`,`accounts`.`error` AS `error`,`accounts`.`Address` AS `Address`,`accounts`.`Scheme` AS `Scheme`,`accounts`.`Number` AS `number`,`accounts`.`AvailableBalance` AS `availableBalance`,`accounts`.`CurrentBalance` AS `currentBalance`,`accounts`.`InterestRate` AS `interestRate`,`accounts`.`AvailableCredit` AS `availableCredit`,`accounts`.`MinimumDue` AS `minimumDue`,`accounts`.`DueDate` AS `dueDate`,`accounts`.`FirstPaymentDate` AS `firstPaymentDate`,`accounts`.`ClosingDate` AS `closingDate`,`accounts`.`PaymentTerm` AS `paymentTerm`,`accounts`.`OpeningDate` AS `openingDate`,`accounts`.`MaturityDate` AS `maturityDate`,`accounts`.`DividendLastPaidAmount` AS `dividendLastPaidAmount`,`accounts`.`DividendLastPaidDate` AS `dividendLastPaidDate`,`accounts`.`DividendPaidYTD` AS `dividendPaidYTD`,`accounts`.`DividendRate` AS `dividendRate`,`accounts`.`DividendYTD` AS `dividendYTD`,`accounts`.`EStatementmentEnable` AS `eStatementEnable`,`customeraccounts`.`FavouriteStatus` AS `favouriteStatus`,`accounts`.`StatusDesc` AS `statusDesc`,`accounts`.`NickName` AS `nickName`,`accounts`.`OriginalAmount` AS `originalAmount`,`accounts`.`OutstandingBalance` AS `outstandingBalance`,`accounts`.`PaymentDue` AS `paymentDue`,`accounts`.`PaymentMethod` AS `paymentMethod`,`accounts`.`SwiftCode` AS `swiftCode`,`accounts`.`TotalCreditMonths` AS `totalCreditMonths`,`accounts`.`TotalDebitsMonth` AS `totalDebitsMonth`,`accounts`.`RoutingNumber` AS `routingNumber`,`accounts`.`SupportBillPay` AS `supportBillPay`,`accounts`.`SupportCardlessCash` AS `supportCardlessCash`,`accounts`.`SupportTransferFrom` AS `supportTransferFrom`,`accounts`.`SupportTransferTo` AS `supportTransferTo`,`accounts`.`SupportDeposit` AS `supportDeposit`,`accounts`.`UnpaidInterest` AS `unpaidInterest`,`accounts`.`PreviousYearsDividends` AS `previousYearsDividends`,`accounts`.`principalBalance` AS `principalBalance`,`accounts`.`PrincipalValue` AS `principalValue`,`accounts`.`RegularPaymentAmount` AS `regularPaymentAmount`,`accounts`.`phone` AS `phoneId`,`accounts`.`LastDividendPaidDate` AS `lastDividendPaidDate`,`accounts`.`LastDividendPaidAmount` AS `lastDividendPaidAmount`,`accounts`.`LastPaymentAmount` AS `lastPaymentAmount`,`accounts`.`LastPaymentDate` AS `lastPaymentDate`,`accounts`.`LastStatementBalance` AS `lastStatementBalance`,`accounts`.`LateFeesDue` AS `lateFeesDue`,`accounts`.`maturityAmount` AS `maturityAmount`,`accounts`.`MaturityOption` AS `maturityOption`,`accounts`.`payoffAmount` AS `payoffAmount`,`accounts`.`PayOffCharge` AS `payOffCharge`,`accounts`.`PendingDeposit` AS `pendingDeposit`,`accounts`.`PendingWithdrawal` AS `pendingWithdrawal`,`accounts`.`JointHolders` AS `jointHolders`,`accounts`.`IsPFM` AS `isPFM`,`accounts`.`InterestPaidYTD` AS `interestPaidYTD`,`accounts`.`InterestPaidPreviousYTD` AS `interestPaidPreviousYTD`,`accounts`.`InterestPaidLastYear` AS `interestPaidLastYear`,`accounts`.`InterestEarned` AS `interestEarned`,`accounts`.`CurrentAmountDue` AS `currentAmountDue`,`accounts`.`CreditLimit` AS `creditLimit`,`accounts`.`CreditCardNumber` AS `creditCardNumber`,`accounts`.`BsbNum` AS `bsbNum`,`accounts`.`BondInterestLastYear` AS `bondInterestLastYear`,`accounts`.`BondInterest` AS `bondInterest`,`accounts`.`AvailablePoints` AS `availablePoints`,`accounts`.`AccountName` AS `accountName`,`accounts`.`email` AS `email`,`accounts`.`IBAN` AS `IBAN`,`accounts`.`adminProductId` AS `adminProductId`,`accounts`.`UpdatedBy` AS `UpdatedBy`,`accounts`.`LastUpdated` AS `LastUpdated`,`accounts`.`ActualUpdatedBY` AS `ActualUpdatedBY`,`bank`.`Description` AS `bankname`,`accounts`.`AccountPreference` AS `accountPreference`,`accounttype`.`transactionLimit` AS `transactionLimit`,`accounttype`.`transferLimit` AS `transferLimit`,`accounttype`.`rates` AS `rates`,`accounttype`.`termsAndConditions` AS `termsAndConditions`,`accounttype`.`TypeDescription` AS `typeDescription`,`accounttype`.`supportChecks` AS `supportChecks`,`accounttype`.`displayName` AS `displayName`,`accounts`.`accountSubType` AS `accountSubType`,`accounts`.`description` AS `description`,`accounts`.`schemeName` AS `schemeName`,`accounts`.`identification` AS `identification`,`accounts`.`secondaryIdentification` AS `secondaryIdentification`,`accounts`.`servicerSchemeName` AS `servicerSchemeName`,`accounts`.`servicerIdentification` AS `servicerIdentification`,`accounts`.`dataCreditDebitIndicator` AS `dataCreditDebitIndicator`,`accounts`.`dataType` AS `dataType`,`accounts`.`dataDateTime` AS `dataDateTime`,`accounts`.`dataCreditLineIncluded` AS `dataCreditLineIncluded`,`accounts`.`dataCreditLineType` AS `dataCreditLineType`,`accounts`.`dataCreditLineAmount` AS `dataCreditLineAmount`,`accounts`.`dataCreditLineCurrency` AS `dataCreditLineCurrency` from ((((`accounts` join `customeraccounts`) join `accounttype`) left join `membershipaccounts` on((`accounts`.`Account_id` = `membershipaccounts`.`Account_id`))) left join `bank` on((`accounts`.`Bank_id` = `bank`.`id`))) where ((`accounts`.`Account_id` = `customeraccounts`.`Account_id`) and (`accounts`.`Type_id` = `accounttype`.`TypeID`));

CREATE TABLE `customerimage` (
  `Customer_id` varchar(50) NOT NULL,
  `UserImage` longtext,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`Customer_id`),
  FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `application` 
ADD COLUMN `isAccountTypeLevelAlerts` VARCHAR(45) NULL DEFAULT 'false' AFTER `isAlertAccountIDLevel`,
ADD COLUMN `isprofileImageAvailable` VARCHAR(45) NULL DEFAULT 'true' AFTER `isAccountTypeLevelAlerts`;

DROP PROCEDURE IF EXISTS `organization_actions_proc`;
DELIMITER $$
CREATE PROCEDURE `organization_actions_proc`(
in _organizationId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @select_statement = concat("SELECT
	`feature`.`id` AS `featureId`,
    `feature`.`name` AS `featureName`,
    `feature`.`description` AS `featureDescription`,
    `feature`.`Status_id` AS `fiFeatureStatus`,
    `organisationfeatures`.`featureStatus` AS `orgFeatureStatus`,
    `featureaction`.`Type_id` AS `actionType`,
    `featureaction`.`id` AS `actionId`,
    `featureaction`.`name` AS `actionName`,
    `featureaction`.`description` AS `actionDescription`,	
    `featureaction`.`isAccountLevel` AS `isAccountLevel`,
	`actionlimit`.`LimitType_id` AS `limitTypeId`,
    `organisationactionlimit`.`value` AS `orgLimitValue`,
    `actionlimit`.`value` AS `fiLimitValue`
	FROM
		(`organisationactionlimit`
		LEFT JOIN `featureaction` ON (`featureaction`.`id` = `organisationactionlimit`.`Action_id`)
		LEFT JOIN `feature` ON (`feature`.`id` = `featureaction`.`Feature_id`)
        LEFT JOIN `organisationfeatures` ON (`organisationfeatures`.`featureId` = `feature`.`id`)
        LEFT JOIN `actionlimit` ON (`actionlimit`.`Action_id` = `organisationactionlimit`.`Action_id` and `actionlimit`.`LimitType_id` = `organisationactionlimit`.`LimitType_id`))
	where `organisationactionlimit`.`Organisation_id` = ",quote(_organizationId)," and `organisationfeatures`.`organisationId` = ",quote(_organizationId),"");
    
	IF (_actionType != '' ) THEN
		set @select_statement =  concat(@select_statement ," and `featureaction`.`Type_id` = ",quote(_actionType));
    END IF;   
    
    IF (_actionId != '' ) THEN
		set @select_statement =  concat(@select_statement ," and `featureaction`.`id` = ",quote(_actionId));
    END IF;
        
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `group_actions_proc`;
DELIMITER $$
CREATE PROCEDURE `group_actions_proc`(
in _groupId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionType varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _actionId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

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
END$$
DELIMITER ;

DROP PROCEDURE IF EXISTS `account_action_approvers_proc`;
DELIMITER $$

CREATE  PROCEDURE `account_action_approvers_proc`(
in _organizationId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 1000000;

SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",") from (`customeraction`)
						where 
							`customeraction`.`isAllowed` = '0'
						and `customeraction`.`Action_id` = _approvalActionList
                        and `customeraction`.`Account_id` = _accountId);

SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);

SELECT 
	DISTINCT (`customer`.`id` ) AS id , (`customer`.`username`) AS userName , (`membergroup`.`Name`) AS groupId,
										(`customer`.`FirstName`) AS firstName , (`customer`.`LastName`) AS lastName
from 
	(`customer`
LEFT JOIN `organisation` ON (`organisation`.`id` = `customer`.`Organization_Id`)
LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
LEFT JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
LEFT JOIN `organisationfeatures` ON (`organisationfeatures`.`organisationId` = `customer`.`Organization_Id`))
	where 
		`organisation`.`id` = _organizationId
		and `organisationfeatures`.`featureId` = _featureId
		and (`organisationfeatures`.`featureStatus` is null or LENGTH(`organisationfeatures`.`featureStatus`) = 0)
		and `customeraccounts`.`Account_id` = _accountId
		and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
        and `customer`.`id` not in (@customerIdList)
		and FIND_IN_SET(`groupactionlimit`.`Action_id`,_approvalActionList) > 0;       
END$$
DELIMITER ;


ALTER TABLE accounts ADD COLUMN `Organization_id` VARCHAR(50) NULL DEFAULT NULL AFTER `ActualUpdatedBY`;
ALTER TABLE `accounts` 
ADD INDEX `FK_accounts_organizationId_idx` (`Organization_id` ASC);



DROP procedure IF EXISTS `organisation_actions_create_proc`;

DELIMITER $$

CREATE PROCEDURE `organisation_actions_create_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureActionId varchar(255) DEFAULT "" ;
 DECLARE actionslist TEXT DEFAULT "" ;
 DECLARE limitId varchar(255) DEFAULT ""  ;
 DECLARE entryStatus INTEGER DEFAULT 0 ;

 DECLARE actions CURSOR 
		FOR (select id from featureaction where FIND_IN_SET(Feature_id,@features_list) COLLATE utf8_general_ci);
DECLARE limits CURSOR 
		FOR (select LimitType_id from actionlimit where Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
 
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features)) COLLATE utf8_general_ci) ; 
SET @features_list = IF(@features_list is null, '', @features_list) COLLATE utf8_general_ci;

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
               SET @limitvalue = (SELECT value FROM actionlimit WHERE Action_id = featureActionId COLLATE utf8_general_ci 
               AND LimitType_id= limitId COLLATE utf8_general_ci);
			   SET @id = (SELECT LEFT(UUID(), 50));
			   INSERT INTO organisationactionlimit(id,Organisation_id,Action_id,LimitType_id,value) VALUES
		        (@id,_organisationId,featureActionId,limitId,@limitvalue);
                SET entryStatus = 1;
			   ITERATE  getlimit;
			END IF;
                END LOOP getlimit;
                CLOSE limits;
                
            SET finished = 0;
            IF entryStatus = 0 THEN
                  SET @id = (SELECT LEFT(UUID(), 50));
			      INSERT INTO organisationactionlimit(id,Organisation_id,Action_id) VALUES
		          (@id,_organisationId,featureActionId);
            END IF;
			set actionslist = CONCAT(featureActionId,",",actionslist) COLLATE utf8_general_ci;
			ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;
  
SET actionslist = (select SUBSTRING(actionslist FROM 1 FOR (CHAR_LENGTH(actionslist)-1)));

select actionslist;

END$$

DELIMITER ;


--
-- Table structure for table `bulkwirefileformattype`
--

DROP TABLE IF EXISTS `bulkwirefileformattype`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bulkwirefileformattype` (
  `bulkWiresFileFormatTypeCode` varchar(50) NOT NULL,
  `bulkWiresFileFormatTypeName` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bulkWiresFileFormatTypeCode`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bulkwirefiles`
--

DROP TABLE IF EXISTS `bulkwirefiles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bulkwirefiles` (
  `bulkWireFileID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireFileName` varchar(150) NOT NULL,
  `noOfTransactions` int(11) NOT NULL DEFAULT '0',
  `noOfDomesticTransactions` int(11) NOT NULL DEFAULT '0',
  `noOfInternationalTransactions` int(11) NOT NULL DEFAULT '0',
  `fileFormatCode` varchar(50) NOT NULL,
  `createdBy` varchar(50) NOT NULL,
  `modifiedBy` varchar(50) NOT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `company_id` varchar(50) DEFAULT NULL,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  `bulkWireFileContents` mediumtext NOT NULL,
  `lastExecutedOn` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`bulkWireFileID`),
  KEY `FK_bulkwirefiles_FileFormat_idx` (`fileFormatCode`),
  KEY `FK_bulkwirefiles_CompanyID_idx` (`company_id`),
  CONSTRAINT `FK_bulkwirefiles_CompanyID` FOREIGN KEY (`company_id`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkwirefiles_FileFormat` FOREIGN KEY (`fileFormatCode`) REFERENCES `bulkwirefileformattype` (`bulkWiresFileFormatTypeCode`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bulkwirefilelineitems`
--

DROP TABLE IF EXISTS `bulkwirefilelineitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bulkwirefilelineitems` (
  `bulkWireFileLineItemID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireFileID` int(11) NOT NULL,
  `swiftCode` varchar(50) DEFAULT NULL,
  `bulkWireTransferType` varchar(50) DEFAULT NULL,
  `transactionType` varchar(50) DEFAULT NULL,
  `internationalRoutingNumber` varchar(50) DEFAULT NULL,
  `amount` decimal(20,2) DEFAULT '0.00',
  `fromAccountNumber` varchar(50) DEFAULT NULL,
  `note` varchar(100) DEFAULT '',
  `recipientName` varchar(100) DEFAULT NULL,
  `recipientAddressLine1` varchar(100) DEFAULT NULL,
  `recipientAddressLine2` varchar(100) DEFAULT NULL,
  `recipientCity` varchar(100) DEFAULT NULL,
  `recipientState` varchar(100) DEFAULT NULL,
  `recipientCountryName` varchar(100) DEFAULT NULL,
  `recipientZipCode` varchar(20) DEFAULT NULL,
  `recipientBankName` varchar(100) DEFAULT NULL,
  `recipientBankAddress1` varchar(100) DEFAULT NULL,
  `recipientBankAddress2` varchar(100) NOT NULL,
  `recipientBankZipCode` varchar(20) DEFAULT NULL,
  `recipientBankcity` varchar(100) DEFAULT NULL,
  `recipientBankstate` varchar(100) DEFAULT NULL,
  `currency` varchar(10) NOT NULL,
  `accountNickname` varchar(50) DEFAULT NULL,
  `recipientAccountNumber` varchar(50) DEFAULT NULL,
  `routingNumber` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`bulkWireFileLineItemID`),
  KEY `FK_bulkwirelineitems_bulkWireFileID_idx` (`bulkWireFileID`),
  KEY `FK_bulkwirelineitems_curency_idx` (`currency`),
  CONSTRAINT `FK_bulkwirelineitems_bulkWireFileID` FOREIGN KEY (`bulkWireFileID`) REFERENCES `bulkwirefiles` (`bulkWireFileID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_bulkwirelineitems_curency` FOREIGN KEY (`currency`) REFERENCES `currency` (`code`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


--
-- Table structure for table `bulkwirefiletransactdetails`
--

DROP TABLE IF EXISTS `bulkwirefiletransactdetails`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bulkwirefiletransactdetails` (
  `bulkWireTransactionID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireFileID` int(11) NOT NULL,
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
  KEY `FK_bulkwiretransaction_bulkWireFileID_idx` (`bulkWireFileID`),
  CONSTRAINT `FK_bulkwiretransaction_bulkWireFileID` FOREIGN KEY (`bulkWireFileID`) REFERENCES `bulkwirefiles` (`bulkWireFileID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `bulkwiresamplefile`
--

DROP TABLE IF EXISTS `bulkwiresamplefile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `bulkwiresamplefile` (
  `bulkWireSampleFileID` int(11) NOT NULL AUTO_INCREMENT,
  `bulkWireSampleFileName` varchar(50) NOT NULL,
  `bulkWireSampleFileFormatCode` varchar(50) NOT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `sampleFileContents` mediumtext NOT NULL,
  PRIMARY KEY (`bulkWireSampleFileID`),
  KEY `FK_bulkwiresample_Fileformat_idx` (`bulkWireSampleFileFormatCode`),
  CONSTRAINT `FK_bulkwiresample_Fileformat` FOREIGN KEY (`bulkWireSampleFileFormatCode`) REFERENCES `bulkwirefileformattype` (`bulkWiresFileFormatTypeCode`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;


DROP procedure IF EXISTS `organisation_actions_delete_proc`;
DELIMITER $$
CREATE PROCEDURE `organisation_actions_delete_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureActionId varchar(255) DEFAULT "";
 DECLARE actionslist TEXT DEFAULT "";

 DECLARE actions CURSOR 
		FOR (select id from featureaction where FIND_IN_SET(Feature_id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id COLLATE utf8_general_ci =feature.id COLLATE utf8_general_ci and featureroletype.RoleType_id COLLATE utf8_general_ci = _organisationType COLLATE utf8_general_ci) 
                      where (FIND_IN_SET(feature.id COLLATE utf8_general_ci,_features COLLATE utf8_general_ci))) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

SET @customer_list = (select group_concat(Customer_id SEPARATOR ",") from organisationemployees where (Organization_id = _organisationId));  

SET @customer_list = IF(@customer_list is null, '', @customer_list);

OPEN actions; 
 getAction: LOOP
        FETCH actions INTO featureActionId;
        IF finished = 1 THEN 
            LEAVE getAction;
	    else
			DELETE FROM organisationactionlimit WHERE (Organisation_id COLLATE utf8_general_ci = _organisationId COLLATE utf8_general_ci AND
            Action_id COLLATE utf8_general_ci = featureActionId COLLATE utf8_general_ci);
			set actionslist = CONCAT(featureActionId,",",actionslist);
			ITERATE  getAction;
        END IF;
END LOOP getAction;
CLOSE actions;

SET actionslist = (select SUBSTRING(actionslist FROM 1 FOR (CHAR_LENGTH(actionslist)-1)));

delete from customeraction where (RoleType_id =_organisationType and FIND_IN_SET(Action_id COLLATE utf8_general_ci,actionslist COLLATE utf8_general_ci) and FIND_IN_SET(Customer_id COLLATE utf8_general_ci,@customer_list COLLATE utf8_general_ci));
    
select actionslist;

END$$

DELIMITER ;


DROP procedure IF EXISTS `organisation_features_create_proc`;

DELIMITER $$
CREATE PROCEDURE `organisation_features_create_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureId varchar(255) DEFAULT "";
 DECLARE featuresList TEXT DEFAULT "";

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features))) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
            SET @id = (SELECT LEFT(UUID(), 50));
			INSERT INTO organisationfeatures(id,organisationId,featureId) VALUES
		    (@id,_organisationId,featureId);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

END$$

DELIMITER ;


DROP procedure IF EXISTS `organisation_features_delete_proc`;

DELIMITER $$
CREATE PROCEDURE `organisation_features_delete_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0 ;
 DECLARE featureId varchar(255) DEFAULT "" ;
 DECLARE featuresList TEXT DEFAULT "" ;

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
 
SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id  and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features))) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
			DELETE FROM organisationfeatures WHERE (organisationfeatures.organisationId COLLATE utf8_general_ci =_organisationId COLLATE utf8_general_ci AND 
            organisationfeatures.featureId COLLATE utf8_general_ci = featureId COLLATE utf8_general_ci);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

END$$

DELIMITER ;


DROP procedure IF EXISTS `organisation_features_suspend_proc`;

DELIMITER $$
CREATE PROCEDURE `organisation_features_suspend_proc`(
IN _features TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationType VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
 DECLARE finished INTEGER DEFAULT 0;
 DECLARE featureId varchar(255) DEFAULT "";
 DECLARE featuresList TEXT DEFAULT "";

 DECLARE features CURSOR 
		FOR (select id from feature where FIND_IN_SET(id,@features_list));
 DECLARE CONTINUE HANDLER 
        FOR NOT FOUND SET finished = 1;
        
UPDATE organisationfeatures SET featureStatus = NULL WHERE organisationId = _organisationId;

SET @features_list = (select group_concat(feature.id SEPARATOR ",") from feature JOIN featureroletype ON (featureroletype.Feature_id =feature.id and featureroletype.RoleType_id = _organisationType) 
                      where (FIND_IN_SET(feature.id,_features))) ; 
SET @features_list = IF(@features_list is null, '', @features_list);

OPEN features; 
 getFeature: LOOP
        FETCH features INTO featureId;
        IF finished = 1 THEN 
            LEAVE getFeature;
	    else
			UPDATE organisationfeatures set featureStatus = "SID_FEATURE_SUSPENDED" WHERE (organisationfeatures.organisationId COLLATE utf8_general_ci =_organisationId AND 
            organisationfeatures.featureId COLLATE utf8_general_ci = featureId COLLATE utf8_general_ci);
			set featuresList = CONCAT(featureId,",",featuresList);
			ITERATE  getFeature;
        END IF;
END LOOP getFeature;
CLOSE features;
  
SET featuresList = (select SUBSTRING(featuresList FROM 1 FOR (CHAR_LENGTH(featuresList)-1)));
select featuresList;

END$$

DELIMITER ;


CREATE 
     OR REPLACE ALGORITHM = UNDEFINED
    SQL SECURITY DEFINER
VIEW `allaccountsview` AS
    SELECT DISTINCT
        `accounts`.`AccountHolder` AS `AccountHolder`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`AccountName` AS `AccountName`,
        `accounts`.`Type_id` AS `Type_id`,
        `membershipaccounts`.`Membership_id` AS `Membership_id`,
        `membershipaccounts`.`Taxid` AS `Taxid`,
        `membershipaccounts`.`IsOrganizationAccount` AS `IsOrganizationAccount`
    FROM
        (`accounts`
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`Account_id`)))
    WHERE
        (ISNULL(`accounts`.`Organization_id`)
            OR (`accounts`.`Organization_id` = ''));

ALTER TABLE `organisationmembership` 
CHANGE COLUMN `Taxid` `Taxid` VARCHAR(50) NULL ;



DROP procedure IF EXISTS `fetch_bulkwire_files_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_files_proc`(
in createdBy varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in searchString varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in pageOffset int,
in pageSize int
)
BEGIN
    SET @companyId = (SELECT Organization_Id FROM customer WHERE id = createdby);
    
	SET @isSMEUser = if(@companyId != "" OR @companyId != NULL , true, false);
    
    SET @filterRetail = concat("`bulkwirefiles`.`createdBy` = ", createdby , " AND `bulkwirefiles`.`softdeleteflag` = 0 ");
				
	SET @filterSME = concat("`bulkwirefiles`.`company_id` = ", @companyId  , " AND `bulkwirefiles`.`softdeleteflag` = 0");
    
    SET @getByIdFilter = if(@isSMEUser, @filterSME, @filterRetail);
    
    SET sortByParam = if(sortByParam = "" OR sortByParam = NULL, 'createdts', sortByParam);
	SET sortByParam = if(sortByParam = 'username', 'firstName,lastname', sortByParam);
    SET sortOrder = if(sortOrder = "" OR sortOrder = NULL, 'DESC', sortOrder);
    
     SET searchString = if(searchString = "" OR searchString = NULL,"",concat("'%",searchString,"%'"));               
    
    SET @orderBy = concat(" ORDER BY ", sortByParam , " ", sortOrder);
    
    SET @paginationQuery = if((pageOffset != NULL OR pageOffset != "" AND pageSize != NULL OR pageSize != ""),
							concat(" LIMIT ",pageSize, " OFFSET " ,pageOffset),
                            ''); 
    
    
	SET @searchQuery = concat("(`bulkwirefiles`.`bulkWireFileName` LIKE ",searchString," OR `bulkwirefiles`.`noOfTransactions` LIKE ",
    searchString," OR `bulkwirefiles`.`noOfDomesticTransactions` LIKE ",searchString," OR `bulkwirefiles`.`noOfInternationalTransactions` LIKE ",searchString," OR  `customer`.`firstname` LIKE ",searchString," OR `customer`.`lastname` LIKE ",searchString,")");
    
    SET @defaultFilter = concat(@getByIdFilter, @orderBy , @paginationQuery);
	SET @searchFilter = concat(@getByIdFilter," AND ",@searchQuery, @orderBy);
    SET @filter = if(searchString = "",@defaultFilter,@searchFilter);

    
    SET @select_statement = concat("SELECT 
	    bulkwirefiles.bulkWireFileID,
		bulkwirefiles.bulkWireFileName, 
		bulkwirefiles.noOfTransactions, 
		bulkwirefiles.noOfDomesticTransactions,
		bulkwirefiles.noOfInternationalTransactions,
		bulkwirefiles.createdts,
		bulkwirefiles.lastmodifiedts,
		bulkwirefiles.lastExecutedOn,
        customer.id, 
		customer.FirstName as firstname,
		customer.LastName as lastname
     FROM
        (`bulkwirefiles`
		LEFT JOIN `customer` ON (`bulkwirefiles`.`createdBy` = `customer`.`id`))
		WHERE ", @filter);
    
	-- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
  
END$$
DELIMITER ;
ALTER TABLE `payee` 
ADD COLUMN `transitDays` VARCHAR(50) NULL DEFAULT NULL AFTER `IBAN`;

DROP TABLE IF EXISTS `holidays`;

CREATE TABLE `holidays` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `holidayDate` TIMESTAMP NULL,
  `createdOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  `updatedOn` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `createdBy` VARCHAR(45),
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',  
  PRIMARY KEY (`id`)) 
  ENGINE = InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;

DROP PROCEDURE IF EXISTS `customer_group_org_actionlimits_proc`;

delimiter $$

CREATE PROCEDURE `customer_group_org_actionlimits_proc`(
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _organisationId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET SESSION group_concat_max_len = 1000000;

SET @organization_actions = (select group_concat(DISTINCT organisationactionlimit.Action_id SEPARATOR "','")  from organisationactionlimit where 
							organisationactionlimit.Organisation_id=_organisationId and 
							organisationactionlimit.Action_id not in (
									select featureaction.id from 
									featureaction
									left join feature on (featureaction.Feature_id = feature.id)
									left join organisationfeatures on (organisationfeatures.featureId = feature.id)
									where
									organisationfeatures.organisationId = _organisationId
									and organisationfeatures.featureStatus='SID_FEATURE_SUSPENDED'
									or feature.Status_id != 'SID_FEATURE_ACTIVE'
							));
                                    
SET @organization_actions = concat("'", IF(@organization_actions is null, '', @organization_actions), "'");

SET @groupId = (select customergroup.Group_id from customergroup where Customer_id=_customerId);

	SET @select_statement =  concat(
    "SELECT 
		`feature`.`id` as `featureId`,
		`feature`.`name` AS `featureName`,
		`feature`.`description` AS `featureDescription`,
        `featureaction`.`id` as `actionId`,
        `featureaction`.`Type_id` as `actionType`,
		`featureaction`.`description` AS `actionDescription`,
        `featureaction`.`name` AS `actionName`,
        IF(`membergroup`.`Type_id` = 'TYPE_ID_SMALL_BUSINESS' OR `membergroup`.`Type_id` = 'TYPE_ID_MICRO_BUSINESS', 
			IF(`featureaction`.`id` in (", @organization_actions, "), 'true', 'false') , 'true') AS `isActionAllowed`,
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
    `groupactionlimit`.`Group_id` = ",quote(@groupId),"))"
    );
    
    -- select @select_statement;
	PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
END$$

DELIMITER;

ALTER TABLE `customer` ADD `isdcode` varchar(10) NULL;
ALTER TABLE `customer` ADD `taxid` varchar(10) NULL; 

DROP procedure IF EXISTS `get_monetary_actions_proc`;

DELIMITER $$
CREATE PROCEDURE `get_monetary_actions_proc`(
IN _featureActions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @monetaryActionList = (SELECT group_concat(id SEPARATOR ",") from featureaction WHERE (Type_id = "MONETARY" AND FIND_IN_SET(id,_featureActions)));

select @monetaryActionList As monetaryActions;
END$$

DELIMITER ;



CREATE TABLE `organisationaccounts` (
  `id` varchar(50) NOT NULL,
  `Organization_id` varchar(50) NOT NULL,
  `Account_id` varchar(50) NOT NULL,
  `AccountName` varchar(50) DEFAULT NULL,
  `TypeID` varchar(50) NOT NULL,
  `Membership_id` varchar(50) NOT NULL,
  `Taxid` varchar(50) NOT NULL,
  `SearchCriteria` enum('Account_id','Membership_id','Taxid') DEFAULT NULL,
  `SearchValue` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `StatusDesc` varchar(45) DEFAULT 'Active',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNIQUE_AccountId_TypeId` (`Account_id`,`TypeID`),
  KEY `FK_organisationaccounts_organisation_idx` (`Organization_id`),
  KEY `FK_organisationaccounts_accounttype_idx` (`TypeID`),
  KEY `IX_oranisationaccounts_AccountId` (`Account_id`),
  CONSTRAINT `FK_organisationaccounts_accounttype` FOREIGN KEY (`TypeID`) REFERENCES `accounttype` (`TypeID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `FK_organisationaccounts_organisation` FOREIGN KEY (`Organization_id`) REFERENCES `organisation` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


ALTER TABLE `mfaservice` MODIFY COLUMN `payload` LONGTEXT NULL;

ALTER TABLE `mfaserviceconfig` 
CHANGE COLUMN `transactionType` `transactionType` VARCHAR(120) NULL DEFAULT NULL ;

DROP procedure IF EXISTS `fetch_bulkwire_filelineitems_proc`;
DELIMITER $$
CREATE PROCEDURE `fetch_bulkwire_filelineitems_proc`(
    IN bulkWireFileID varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN searchString VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN sortByParam varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @queryFilter = concat("`bulkwirefilelineitems`.`bulkWireFileID` = ", bulkWireFileID, " AND `bulkwirefilelineitems`.`softdeleteflag` = 0 ");
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
    
    SET @qfilter = concat("`bulkwirefiletransactdetails`.`bulkWireFileID` = ", bulkWireFileID , " AND `bulkwirefiletransactdetails`.`softdeleteflag` = 0 ");
      
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
in sortOrder varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @query1= concat("`wiretransfers`.`wireFileExecution_id` = ", BulkWireFileExecution_id," AND `wiretransfers`.`softdeleteflag` = 0 ");
SET @query2= concat("`wiretransfers`.`wireFileExecution_id` = ", BulkWireFileExecution_id," AND `wiretransfers`.`status` ='",statusFilter,"' AND `wiretransfers`.`softdeleteflag` = 0 ");

SET sortByParam =if (sortByParam = "" OR sortByParam = NULL, 'transactionId', sortByParam);
SET sortOrder = if (sortOrder = "" OR sortOrder = NULL, 'ASC', sortOrder);
SET searchString = if (searchString = "" OR searchString = NULL, "", concat("'%",searchString,"%'"));

SET @orderBy = concat(" ORDER BY ", sortByParam, " ", sortOrder);
SET @searchQuery = concat("(`wiretransfers`.`amount` LIKE ",searchString," OR `wiretransfers`.`notes` LIKE ",searchString," OR `wiretransfers`.`fromAccountNumber` LIKE ",searchString," OR `wiretransfers`.`payeeAccountNumber` LIKE ",searchString," OR `wiretransfers`.`transactionType` LIKE ",searchString," OR `onetimepayee`.`payeeName` LIKE ",searchString," OR `onetimepayee`.`payeeType` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine1` LIKE ",searchString," OR `onetimepayee`.`payeeAddressLine2` LIKE ",searchString," OR `onetimepayee`.`cityName` LIKE ",searchString," OR `onetimepayee`.`state` LIKE ",searchString," OR `onetimepayee`.`zipCode` LIKE ",searchString," OR `onetimepayee`.`bankName` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine1` LIKE ",searchString," OR `onetimepayee`.`bankAddressLine2` LIKE ",searchString," OR `onetimepayee`.`bankZip` LIKE ",searchString," OR `onetimepayee`.`bankState` LIKE ",searchString," OR `onetimepayee`.`bankCity` LIKE ",searchString," OR  `onetimepayee`.`routingNumber` LIKE ",searchString," OR `onetimepayee`.`internationalRoutingCode` LIKE ",searchString," OR `onetimepayee`.`swiftCode` LIKE ",searchString,")");
SET @finalQueryFilter = if(statusFilter="", @query1, @query2);
SET @defaultFilter = concat(@finalQueryFilter, @orderBy);
SET @searchFilter = concat(@finalQueryFilter, " AND ", @searchQuery, @orderBy);
SET @filter = if (searchString = "", @defaultFilter, @searchFilter);

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
  
END$$
DELIMITER ;


CREATE TABLE `eventtopicconfiguration` (
  `eventCode` varchar(100) NOT NULL,
  `topic` varchar(255) NOT NULL,
  PRIMARY KEY (`eventCode`,`topic`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `application` ADD COLUMN `bwFileTransactionsLimit` INT(11) NULL AFTER `isprofileImageAvailable`;



DROP procedure IF EXISTS `get_alert_sub_types`;
DELIMITER $$
CREATE  PROCEDURE `get_alert_sub_types`(in _alertTypes text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select `alertsubtype`.`id` as `alertSubType`
	from `alertsubtype` 
    where FIND_IN_SET(`alertsubtype`.`AlertTypeId` ,_alertTypes);

END$$
DELIMITER ;

DROP procedure IF EXISTS `get_AlertTypes`;
DELIMITER $$
CREATE  PROCEDURE `get_AlertTypes`( in _alertTypes text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

SELECT `dbxalerttype`.`id` as `id`,
		`dbxalerttype`.`AttributeId` as `attributeId`,
        `dbxalerttype`.`AlertConditionId` as `alertConditionId`,
        `dbxalerttype`.`Value1` as `value1`,
        `dbxalerttype`.`Value2` as `value2`,
        `dbxalerttype`.`IsGlobal` as `isGlobal`
        from  `dbxalerttype`
        where  FIND_IN_SET(`dbxalerttype`.`id` ,_alertTypes);

END$$
DELIMITER ;

DROP procedure IF EXISTS `getAllBackendIdentifiers`;
DELIMITER $$
CREATE  PROCEDURE `getAllBackendIdentifiers`()
BEGIN
	select `Customer_id` as `customerId`,
			`BackendId` as `backendId`
            from 
			`backendidentifier`;

END$$
DELIMITER ;


DROP procedure IF EXISTS `get_backendidentifiers_for_customerids`;
DELIMITER $$
CREATE  PROCEDURE `get_backendidentifiers_for_customerids`(in _customerIds text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select `backendidentifier`.`Customer_id` as `customerId`,
	`backendidentifier`.`BackendId` as `backendId`
    from `backendidentifier`
    where FIND_IN_SET(`backendidentifier`.`Customer_id`, _customerIds);

END$$
DELIMITER ;


DROP procedure IF EXISTS `get_customerAlertEntitlementForAlertTypes`;
DELIMITER $$
CREATE  PROCEDURE `get_customerAlertEntitlementForAlertTypes`(in _alertTypes text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

select `dbxcustomeralertentitlement`.`AlertTypeId` as `alertTypeId`,
		`dbxcustomeralertentitlement`.`Customer_id` as `customerId`,
        `dbxcustomeralertentitlement`.`value1` as `value1`,
        `dbxcustomeralertentitlement`.`value2` as `value2`,
        `dbxcustomeralertentitlement`.`AccountId` as `accountId`
        from `dbxcustomeralertentitlement`
        where FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId` ,_alertTypes);

END$$
DELIMITER ;


DROP procedure IF EXISTS `get_customerids_for_backendidentifiers`;
DELIMITER $$
CREATE  PROCEDURE `get_customerids_for_backendidentifiers`(in _backendIds text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
select `backendidentifier`.`Customer_id` as `customerId`,
	`backendidentifier`.`BackendId` as `backendId`
    from `backendidentifier`
    where FIND_IN_SET(`backendidentifier`.`BackendId`, _backendIds);

END$$
DELIMITER ;


DROP procedure IF EXISTS `get_entitlements_for_customerids_and_alerttypes`;
DELIMITER $$
CREATE  PROCEDURE `get_entitlements_for_customerids_and_alerttypes`(in _alertTypes text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _customerIds text CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN

select `dbxcustomeralertentitlement`.`AlertTypeId` as `alertTypeId`,
		`dbxcustomeralertentitlement`.`Customer_id` as `customerId`,
        `dbxcustomeralertentitlement`.`value1` as `value1`,
        `dbxcustomeralertentitlement`.`value2` as `value2`,
        `dbxcustomeralertentitlement`.`AccountId` as `accountId`
        from `dbxcustomeralertentitlement`
        where FIND_IN_SET(`dbxcustomeralertentitlement`.`AlertTypeId` ,_alertTypes) and 
         FIND_IN_SET(`dbxcustomeralertentitlement`.`Customer_id` ,_customerIds);

END$$
DELIMITER ;


DROP VIEW IF EXISTS `alertcustomersaccountchannels_view`;
CREATE VIEW `alertcustomersaccountchannels_view` AS select `dbxcustomeralertentitlement`.`AlertTypeId` AS `AlertTypeId`,`dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,`dbxalerttype`.`Name` AS `Name`,`customeralertcategorychannel`.`Customer_id` AS `Customer_id`,`customeralertcategorychannel`.`AlertCategoryId` AS `AlertCategoryId`,`dbxalerttype`.`AttributeId` AS `AttributeId`,`dbxalerttype`.`AlertConditionId` AS `AlertConditionId`,`dbxcustomeralertentitlement`.`Value1` AS `Value1`,`dbxcustomeralertentitlement`.`Value2` AS `Value2`,`dbxalerttype`.`IsGlobal` AS `IsGlobal`,`customeralertcategorychannel`.`ChannelId` AS `ChannelId`,`customeralertcategorychannel`.`AccountId` AS `AccountId`,`status`.`Type_id` AS `Status_id`,`dbxalertcategory`.`accountLevel` AS `accountLevel` from ((((`dbxcustomeralertentitlement` join `dbxalerttype` on((`dbxcustomeralertentitlement`.`AlertTypeId` = `dbxalerttype`.`id`))) join `dbxalertcategory` on((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`))) join `customeralertcategorychannel` on(((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertcategorychannel`.`Customer_id`) and (`dbxcustomeralertentitlement`.`AccountId` = `customeralertcategorychannel`.`AccountId`) and (`dbxalertcategory`.`id` = `customeralertcategorychannel`.`AlertCategoryId`)))) join `status` on((`dbxalerttype`.`Status_id` = `status`.`id`)));


DROP procedure IF EXISTS `getCustomerCommunicationData`;
DELIMITER $$
CREATE PROCEDURE `getCustomerCommunicationData`(_customers TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as custid ,Value as value,Type_id as type FROM customercommunication  where isPrimary = 1 
   and  FIND_IN_SET(`customercommunication`.`Customer_id`,_customers) ; 
END$$
DELIMITER ;

DROP procedure IF EXISTS `getCustomerInfo`;
DELIMITER $$
CREATE  PROCEDURE `getCustomerInfo`(_customers TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT id as custid ,FirstName as fname,LastName as lname  , CountryCode as country FROM customer  where FIND_IN_SET(`customer`.`id`,_customers) ; 
END$$
DELIMITER ;

DROP procedure IF EXISTS `getCustomersIdFromCoreId`;
DELIMITER $$
CREATE PROCEDURE `getCustomersIdFromCoreId`(_corecustomers  TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
SELECT Customer_id as custid ,BackendId as corecustid FROM backendidentifier where FIND_IN_SET (`backendidentifier`.`BackendId`,_corecustomers) ; 
END$$
DELIMITER ;

DROP procedure IF EXISTS `organisation_actions_get_proc`;

DELIMITER $$
CREATE PROCEDURE `organisation_actions_get_proc`(
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @actionList = (SELECT group_concat(DISTINCT organisationactionlimit.Action_id SEPARATOR ",") from organisationactionlimit 
                     where Organisation_id COLLATE utf8_general_ci = _organisationId);

SELECT @actionList AS actionslist;
END$$

DELIMITER ;





DROP procedure IF EXISTS `organisation_customeraccounts_delete_proc`;

DELIMITER $$

CREATE PROCEDURE `organisation_customeraccounts_delete_proc`(
IN _accounts TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

SET @orgemployees_list = (select group_concat(organisationemployees.Customer_id SEPARATOR ",") from organisationemployees 
                      where Organization_id COLLATE utf8_general_ci = _organisationId); 
SET @orgemployees_list = IF(@orgemployees_list is null, '', @orgemployees_list);  

select @orgemployees_list;

DELETE FROM `customeraction` 
	WHERE  FIND_IN_SET(`customeraction`.`Account_id`,_accounts) 
    AND FIND_IN_SET(`customeraction`.`Customer_id` ,@orgemployees_list);
		
DELETE FROM `customeraccounts` 
	WHERE  FIND_IN_SET(`customeraccounts`.`Account_id`,_accounts) 
    AND FIND_IN_SET(`customeraccounts`.`Customer_id` ,@orgemployees_list);

END$$

DELIMITER ;

CREATE TABLE IF NOT EXISTS `batchalertdefinition` (
  `alertType` varchar(255) NOT NULL,
  `objectType` varchar(50) NOT NULL,
  `columnName` varchar(255) DEFAULT NULL,
  `condition` varchar(50) DEFAULT NULL,
  `value` varchar(255) DEFAULT NULL,
  `dueDateChecktype` varchar(4) DEFAULT NULL,
  `dueDateParamName` varchar(255) DEFAULT NULL,
  `createdby` varchar(255) DEFAULT NULL,
  `modifiedby` varchar(255) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updatedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`alertType`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `eventconsumertypes` 
CHANGE COLUMN `EventType` `EventType` VARCHAR(255) NOT NULL ;




ALTER TABLE `service_permission_mapper` 
CHANGE COLUMN `user_type` `user_type` VARCHAR(50) NOT NULL DEFAULT '' ;

DROP VIEW IF EXISTS `fetch_achtemplate_details_view`;
DROP VIEW IF EXISTS `fetch_achfilesdetails_view`;
DROP VIEW IF EXISTS `fetch_achtemplatesubrecord_details_view`;
DROP VIEW IF EXISTS `fetch_achtransaction_details_view`;
DROP VIEW IF EXISTS `fetch_achtransactionsubrecord_details_view`;
DROP VIEW IF EXISTS `fetch_generaltransactions_Details_view`;
DROP VIEW IF EXISTS `fetch_templaterecord_details_view`;
DROP VIEW IF EXISTS `fetch_transactionrecord_details_view`;


DROP VIEW IF EXISTS `allaccountsview`;

CREATE 
	OR REPLACE ALGORITHM = UNDEFINED 
    SQL SECURITY DEFINER
VIEW `allaccountsview` AS
    SELECT DISTINCT
        `accounts`.`AccountHolder` AS `AccountHolder`,
        `accounts`.`Account_id` AS `Account_id`,
        `accounts`.`AccountName` AS `AccountName`,
        `accounts`.`Type_id` AS `Type_id`,
		`accounttype`.`TypeDescription` AS `accountType`,
        `membershipaccounts`.`Membership_id` AS `Membership_id`,
        `membershipaccounts`.`Taxid` AS `Taxid`,
        `membershipaccounts`.`IsOrganizationAccount` AS `IsOrganizationAccount`
    FROM
        ((`accounts`
        LEFT JOIN `membershipaccounts` ON ((`accounts`.`Account_id` = `membershipaccounts`.`Account_id`)))
        LEFT JOIN `accounttype` ON ((`accounts`.`Type_id` = `accounttype`.`TypeID`)))
    WHERE
        (ISNULL(`accounts`.`Organization_id`)
            OR (`accounts`.`Organization_id` = ''));
   

ALTER TABLE `custcompletedcampaign` DROP FOREIGN KEY `fk_campaignid`;
ALTER TABLE `custcompletedcampaign` DROP INDEX `fk_campaignid` ;


Drop PROCEDURE IF EXISTS weekend_proc;
DELIMITER $$
CREATE PROCEDURE `weekend_proc`()
BEGIN
set @startDate=CURDATE();
set @count = 0;
while @count < 100 do
if(WEEKDAY(@startDate) = 6 or WEEKDAY(@startDate) = 5) then
insert into holidays(`holidayDate`,`createdBy`,`modifiedby`) values(@startDate,'priya','priya');
set @count = @count+1;
end if;
set @startDate=DATE_ADD(@startDate, INTERVAL 1 DAY);
end while;
end $$

DELIMITER ;

call weekend_proc();

CREATE TABLE `batchalertobject` (
  `objectType` VARCHAR(50) NOT NULL,
  `lastSyncTimestamp` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`objectType`));
  
  
DROP VIEW IF EXISTS `alertcustomerchannels_view`;
CREATE VIEW `alertcustomerchannels_view` AS SELECT 
        `dbxcustomeralertentitlement`.`AlertTypeId` AS `AlertTypeId`,
        `dbxcustomeralertentitlement`.`AccountType` AS `AccountType`,
        `dbxalerttype`.`Name` AS `Name`,
        `customeralertcategorychannel`.`Customer_id` AS `Customer_id`,
        `customeralertcategorychannel`.`AlertCategoryId` AS `AlertCategoryId`,
        `dbxalerttype`.`AttributeId` AS `AttributeId`,
        `dbxalerttype`.`AlertConditionId` AS `AlertConditionId`,
        `dbxcustomeralertentitlement`.`Value1` AS `Value1`,
        `dbxcustomeralertentitlement`.`Value2` AS `Value2`,
        `dbxalerttype`.`IsGlobal` AS `IsGlobal`,
        `customeralertcategorychannel`.`ChannelId` AS `ChannelId`,
        `customeralertcategorychannel`.`AccountId` AS `AccountId`,
        `status`.`Type_id` AS `Status_id`,
        `dbxalertcategory`.`accountLevel` AS `accountLevel`
    FROM
        ((((`dbxcustomeralertentitlement`
        JOIN `dbxalerttype` ON ((`dbxcustomeralertentitlement`.`AlertTypeId` = `dbxalerttype`.`id`)))
        JOIN `dbxalertcategory` ON ((`dbxalerttype`.`AlertCategoryId` = `dbxalertcategory`.`id`)))
        JOIN `customeralertcategorychannel` ON (((`dbxcustomeralertentitlement`.`Customer_id` = `customeralertcategorychannel`.`Customer_id`)
            AND (`dbxcustomeralertentitlement`.`AccountId` = `customeralertcategorychannel`.`AccountId`)
            AND (`dbxalertcategory`.`id` = `customeralertcategorychannel`.`AlertCategoryId`))))
        JOIN `status` ON ((`dbxalerttype`.`Status_id` = `status`.`id`)));

Drop PROCEDURE IF EXISTS customer_actions_delete;
DELIMITER $$
CREATE PROCEDURE `customer_actions_delete`(in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
DELETE FROM customeraction where customeraction.Customer_id = _customerId;
END $$
DELIMITER ;


DROP procedure IF EXISTS `get_organisation_employees_proc`;

DELIMITER $$
CREATE PROCEDURE `get_organisation_employees_proc`(
IN _organisationId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnName VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _filterColumnValue VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
SET @org_employees_list = (SELECT group_concat(Customer_id SEPARATOR ",") FROM organisationemployees WHERE (Organization_id = _organisationId)); 
SET @org_employees_list = IF(@org_employees_list is null, '', @org_employees_list); 

IF _filterColumnName = "Ssn" THEN
      SET @customersList = (SELECT group_concat(id SEPARATOR ",") FROM customer where FIND_IN_SET(id,@org_employees_list) AND FIND_IN_SET(Ssn,_filterColumnValue));
      SET @customersList = IF(@customersList is null, '', @customersList); 
END IF;

SELECT @customersList AS employeesList;

END$$

DELIMITER ;

ALTER TABLE `accounts` 
ADD COLUMN `ownership` VARCHAR(45) NULL DEFAULT NULL AFTER `Organization_id`;

ALTER TABLE `accounts` 
ADD COLUMN `arrangementId` VARCHAR(45) NULL DEFAULT NULL AFTER `ownership`;

ALTER TABLE `payee` 
CHANGE COLUMN `transitDays` `transitDays` VARCHAR(50) NULL DEFAULT '3' ;

DROP procedure IF EXISTS `customer_eagreement_get_proc`;

DELIMITER $$

CREATE PROCEDURE `customer_eagreement_get_proc` (
in _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	select membergroup.isEAgreementActive AS isEAgreementActive from 
	membergroup
    LEFT JOIN customergroup on (customergroup.Group_id = membergroup.id)
    where
    customergroup.Customer_id = _customerId;
END$$

DELIMITER ;




DROP procedure IF EXISTS `get_valid_orgaccounts_list_proc`;

DELIMITER $$
CREATE PROCEDURE `get_valid_orgaccounts_list_proc`(
IN _accountsList TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

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


ALTER TABLE `externalaccount` ADD COLUMN `addressNickName` VARCHAR(45) NULL DEFAULT NULL  AFTER `phoneExtension` , ADD COLUMN `addressLine1` VARCHAR(200) NULL DEFAULT NULL  AFTER `addressNickName` , ADD COLUMN `city` VARCHAR(45) NULL DEFAULT NULL  AFTER `addressLine1` , ADD COLUMN `zipcode` VARCHAR(45) NULL DEFAULT NULL  AFTER `city` , ADD COLUMN `country` VARCHAR(45) NULL DEFAULT NULL  AFTER `zipcode` , ADD COLUMN `externalaccountcol` VARCHAR(45) NULL  AFTER `country` ;

ALTER TABLE `transaction` ADD COLUMN `bicCode` VARCHAR(50) NULL DEFAULT NULL  AFTER `billerId` , ADD COLUMN `paidBy` VARCHAR(50) NULL DEFAULT NULL  AFTER `bicCode` , ADD COLUMN `paymentType` VARCHAR(50) NULL DEFAULT NULL  AFTER `paidBy` , ADD COLUMN `feeAmount` VARCHAR(50) NULL DEFAULT NULL  AFTER `paymentType` , ADD COLUMN `beneficiaryAddressNickName` VARCHAR(50) NULL DEFAULT NULL  AFTER `feeAmount` , ADD COLUMN `beneficiaryAddressLine1` VARCHAR(200) NULL DEFAULT NULL  AFTER `beneficiaryAddressNickName` , ADD COLUMN `beneficiaryCity` VARCHAR(50) NULL DEFAULT NULL  AFTER `beneficiaryAddressLine1` , ADD COLUMN `beneficiaryZipcode` VARCHAR(50) NULL DEFAULT NULL  AFTER `beneficiaryCity` , ADD COLUMN `beneficiarycountry` VARCHAR(50) NULL DEFAULT NULL  AFTER `beneficiaryZipcode` , ADD COLUMN `bankId` VARCHAR(50) NULL DEFAULT NULL  AFTER `beneficiarycountry` ;

DROP TABLE IF EXISTS `swiftcode`;
CREATE TABLE `swiftcode` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bankName` varchar(100) DEFAULT NULL,
  `city` varchar(50) DEFAULT NULL,
  `country` varchar(50) DEFAULT NULL,
  `bic` varchar(50) DEFAULT NULL,
  `countryCode` varchar(2) DEFAULT NULL,
  `countryRegion` enum('DOMESTIC','INTERNATIONAL') DEFAULT 'INTERNATIONAL',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;

ALTER TABLE `externalaccount` 
CHANGE COLUMN `nickName` `nickName` VARCHAR(100) NULL DEFAULT NULL ;

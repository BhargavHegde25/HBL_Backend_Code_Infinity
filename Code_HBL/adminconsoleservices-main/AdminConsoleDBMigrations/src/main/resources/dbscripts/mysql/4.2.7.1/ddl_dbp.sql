ALTER TABLE `card` ADD COLUMN `cvv` INT(5) NULL DEFAULT '000' AFTER `cardHolderName`;

ALTER TABLE `card` CHANGE COLUMN `card_Status` `card_Status` ENUM('Active','Inactive','Cancelled','Reported Lost','Replaced','Cancel Request Sent','Replace Request Sent','Locked','Issued','Expired') NOT NULL AFTER `Id`;

CREATE TABLE `cardstatements` (   `id` INT(11) NOT NULL AUTO_INCREMENT,   `description` VARCHAR(100) NULL DEFAULT NULL,   `statementLink` VARCHAR(100) NULL DEFAULT NULL,   `Card_id` BIGINT(16) NOT NULL,   `month` VARCHAR(100) NULL DEFAULT NULL,   PRIMARY KEY (`id`),   INDEX `Card_id` (`id` ASC),   INDEX `FK_CardStatements_Cards_idx` (`Card_id` ASC),   CONSTRAINT `FK_CardStatements_Cards`     FOREIGN KEY (`Card_id`)     REFERENCES `card` (`cardNumber`)     ON DELETE NO ACTION     ON UPDATE NO ACTION);

ALTER TABLE `application` 
ADD COLUMN `cardStatementYears` VARCHAR(45) NULL AFTER `isprofileImageAvailable`;

CREATE TABLE `cardtransaction` (   `cardNumber` varchar(45) NOT NULL,   `transactionDescription` varchar(20) DEFAULT NULL,   `transactionBalance` decimal(11,2) DEFAULT NULL,   `transactionMerchantAddressName` varchar(25) DEFAULT NULL,   `transactionMerchantCity` varchar(16) DEFAULT NULL,   `merchantCategory` varchar(16) DEFAULT NULL,   `transactionStatus` varchar(1) DEFAULT NULL,   `transactionType` varchar(1) DEFAULT NULL,   `transactionCategory` varchar(1) DEFAULT NULL,   `transactionDetailDescription` varchar(45) DEFAULT NULL,   `transactionIndicator` varchar(1) DEFAULT NULL,   `transactionDate` timestamp NULL DEFAULT NULL,   `transactionTime` time DEFAULT NULL,   `transactionAmount` decimal(18,2) DEFAULT '0.00',   `transactionReferenceNumber` varchar(22) NOT NULL,   `transactionCurrencyCode` varchar(3) DEFAULT NULL,   `transactionExchangeRate` decimal(16,7) DEFAULT '0.0000000',   `exchangeCurrency` varchar(3) DEFAULT NULL,   `exchangeAmount` decimal(18,2) DEFAULT '0.00',   `transactionTaxIndicator` varchar(1) DEFAULT NULL,   `taxPercentage` decimal(8,5) DEFAULT '0.00000',   `transactionTaxAmount` decimal(18,2) DEFAULT '0.00',   `transactionTerminalID` varchar(20) DEFAULT NULL,   `cardType` varchar(45) DEFAULT NULL,   PRIMARY KEY (`transactionReferenceNumber`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

ALTER TABLE `card` 
ADD COLUMN `currentBalance` VARCHAR(45) NULL AFTER `cvv`,
ADD COLUMN `rewardPoints` VARCHAR(45) NULL AFTER `currentBalance`,
ADD COLUMN `paymentDueDate` TIMESTAMP NULL AFTER `rewardPoints`;

ALTER TABLE `card` 
CHANGE COLUMN `rewardPoints` `rewardsPoint` VARCHAR(45) NULL DEFAULT NULL ,
ADD COLUMN `availableBalance` VARCHAR(45) NULL DEFAULT NULL AFTER `paymentDueDate`;


ALTER TABLE `notification` 
ADD COLUMN `notificationCategory` VARCHAR(255) NULL DEFAULT NULL AFTER `notificationActionLink`,
ADD COLUMN `actionButtonLabelName` VARCHAR(255) NULL DEFAULT NULL AFTER `notificationCategory`;

ALTER TABLE `card` 
ADD COLUMN `currencyCode` VARCHAR(45) NULL DEFAULT NULL AFTER `availableBalance`;

ALTER TABLE `interbankfundtransfers`  ADD COLUMN `pdf` LONGBLOB NULL;
ALTER TABLE `intrabanktransfers`  ADD COLUMN `pdf` LONGBLOB NULL;
ALTER TABLE `ownaccounttransfers`  ADD COLUMN `pdf` LONGBLOB NULL;
ALTER TABLE `internationalfundtransfers`  ADD COLUMN `pdf` LONGBLOB NULL;

DELIMITER $$
CREATE PROCEDURE `fetch_cardtransaction_proc`(
in _cardNumber varchar(50),
in _pageOffset int,
in _pageSize int,
in _sortOrder varchar(50),
in _sortByParam varchar(50)
)
BEGIN
    SET @filter = concat("cardtransaction.cardNumber = ", _cardNumber);
     
    SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'transactionDate', _sortByParam);
    SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
   
    SET @orderBy = concat(" ORDER BY ", _sortByParam , " ", _sortOrder);
   
    SET @paginationQuery = if((_pageOffset != NULL OR _pageOffset != "" AND _pageSize != NULL OR _pageSize != ""),
concat(" LIMIT ",_pageSize, " OFFSET " ,_pageOffset),
                            '');
   
SET @select_statement = concat("SELECT *
    FROM cardtransaction cardtransaction
WHERE ", @filter, @orderBy, @paginationQuery);
       
-- select @select_statement;
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
     
END$$
DELIMITER ;

DROP VIEW IF EXISTS `notificationview`;

CREATE  VIEW `notificationview` AS
    SELECT 
        `notification`.`notificationId` AS `notificationId`,
        `notification`.`imageURL` AS `imageURL`,
        `usernotification`.`isRead` AS `isRead`,
        `notification`.`notificationActionLink` AS `notificationActionLink`,
        `notification`.`notificationModule` AS `notificationModule`,
        `notification`.`notificationSubject` AS `notificationSubject`,
        `notification`.`notificationSubModule` AS `notificationSubModule`,
        `notification`.`notificationText` AS `notificationText`,
        `usernotification`.`receivedDate` AS `receivedDate`,
        `usernotification`.`id` AS `userNotificationId`,
        `usernotification`.`user_id` AS `user_id`,
        `notification`.`notificationCategory` AS `notificationCategory`,
        `notification`.`actionButtonLabelName` AS `actionButtonLabelName`
        
    FROM
        (`usernotification`
        JOIN `notification` ON ((`notification`.`notificationId` = `usernotification`.`notification_id`)));

DROP procedure IF EXISTS `fetch_cardtransaction_proc`;

DELIMITER $$
CREATE PROCEDURE `fetch_cardtransaction_proc`(
in _cardNumber varchar(50),
in _pageOffset int,
in _pageSize int,
in _sortOrder varchar(50),
in _sortByParam varchar(50)
)
BEGIN
    SET @filter = concat("cardtransaction.cardNumber = ", _cardNumber);
     
    SET _sortByParam = if(_sortByParam = "" OR _sortByParam = NULL, 'transactionDate', _sortByParam);
    SET _sortOrder = if(_sortOrder = "" OR _sortOrder = NULL, 'DESC', _sortOrder);
   
    SET @orderBy = concat(" ORDER BY ", _sortByParam , " ", _sortOrder);
   
    SET @paginationQuery = if((_pageOffset != NULL OR _pageOffset != "" AND _pageSize != NULL OR _pageSize != ""),
concat(" LIMIT ",_pageSize, " OFFSET " ,_pageOffset),
                            '');
   
SET @select_statement = concat("SELECT `cardtransaction`.`transactionDescription`,
    `cardtransaction`.`transactionBalance`,
    `cardtransaction`.`transactionMerchantAddressName`,
    `cardtransaction`.`transactionMerchantCity`,
    `cardtransaction`.`merchantCategory`,
    `cardtransaction`.`transactionStatus`,
    `cardtransaction`.`transactionType`,
    `cardtransaction`.`transactionCategory`,
    `cardtransaction`.`transactionDetailDescription`,
    `cardtransaction`.`transactionIndicator`,
    `cardtransaction`.`transactionDate`,
    `cardtransaction`.`transactionTime`,
    `cardtransaction`.`transactionAmount`,
    `cardtransaction`.`transactionReferenceNumber`,
    `cardtransaction`.`transactionCurrencyCode`,
    `cardtransaction`.`transactionExchangeRate`,
    `cardtransaction`.`exchangeCurrency`,
    `cardtransaction`.`exchangeAmount`,
    `cardtransaction`.`transactionTaxIndicator`,
    `cardtransaction`.`taxPercentage`,
    `cardtransaction`.`transactionTaxAmount`,
    `cardtransaction`.`transactionTerminalID`,
    `cardtransaction`.`cardType`
    FROM cardtransaction cardtransaction
WHERE ", @filter, @orderBy, @paginationQuery);
       
-- select @select_statement;
PREPARE stmt FROM @select_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
     
END$$

DELIMITER ;



ALTER TABLE `customertermsandconditions` ADD COLUMN `companyLegalUnit` VARCHAR(50) NOT NULL DEFAULT 'ALL';
CREATE TABLE `productLine` (
  `productLineId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productLineRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productLineName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `externalIndicator` bit(1) DEFAULT NULL,
  PRIMARY KEY (`productLineRef`)
);
-- dbxdb.productGroup definition

CREATE TABLE `productGroup` (
  `productGroupId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productGroupRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productGroupName` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `description` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL,
  `branchRef` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  `detailedDesc` varchar(300) COLLATE utf8_unicode_ci DEFAULT NULL,
  `productLineRef` varchar(100) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`productGroupRef`),
  KEY `productGroup_FK` (`productLineRef`),
  CONSTRAINT `productGroup_FK` FOREIGN KEY (`productLineRef`) REFERENCES `productLine` (`productLineRef`)
);
-- dbxdb.productInformation definition

CREATE TABLE `productInformation` (
  `productId` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `branchRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productName` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productLineRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `productGroupRef` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `status` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`productRef`),
  KEY `productInformation_FK` (`productLineRef`),
  KEY `productInformation_FK_1` (`productGroupRef`),
  CONSTRAINT `productInformation_FK` FOREIGN KEY (`productLineRef`) REFERENCES `productLine` (`productLineRef`),
  CONSTRAINT `productInformation_FK_1` FOREIGN KEY (`productGroupRef`) REFERENCES `productGroup` (`productGroupRef`)
);

-- dbxdb.productinformationview source

CREATE OR REPLACE
ALGORITHM = UNDEFINED VIEW `productinformationview` AS
select
    `productInformation`.`productId` AS `productId`,
    `productInformation`.`productName` AS `productName`,
    `productInformation`.`productLineRef` AS `productLineRef`,
    `productInformation`.`productGroupRef` AS `productGroupRef`,
    `productInformation`.`branchRef` AS `branchRef`,
    `productInformation`.`status` AS `status`,
    `productInformation`.`productRef` AS `productRef`,
    `productGroup`.`productGroupName` AS `productGroupName`,
    `productGroup`.`productGroupId` AS `productGroupId`,
    `productLine`.`productLineName` AS `productLineName`,
    `productLine`.`productLineId` AS `productLineId`
from
    ((`productInformation`
join `productGroup` on
    ((`productInformation`.`productGroupRef` = `productGroup`.`productGroupRef`)))
join `productLine` on
    ((`productInformation`.`productLineRef` = `productLine`.`productLineRef`)))
order by
    `productInformation`.`productLineRef`,
    `productInformation`.`productGroupRef`;

ALTER TABLE `paymentfiles` MODIFY COLUMN `paymentFileContents` LONGTEXT NOT NULL;
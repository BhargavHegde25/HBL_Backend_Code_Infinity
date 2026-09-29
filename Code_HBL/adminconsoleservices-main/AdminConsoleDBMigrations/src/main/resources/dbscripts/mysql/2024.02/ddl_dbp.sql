CREATE TABLE `Documents` (
  `documentId` varchar(50) NOT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `content` longblob,
  `documentName` varchar(150) DEFAULT NULL,
  `status` varchar(45) DEFAULT NULL,
  `createdts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `modifiedBy` varchar(50) DEFAULT NULL,
  `mimeType` varchar(50) DEFAULT NULL,
  `category` varchar(50) DEFAULT NULL,
  `version` varchar(50) DEFAULT NULL,
  `ownerSystemId` varchar(50) DEFAULT NULL,
  `documentGroup` varchar(50) DEFAULT NULL,
  `applicationId` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`documentId`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
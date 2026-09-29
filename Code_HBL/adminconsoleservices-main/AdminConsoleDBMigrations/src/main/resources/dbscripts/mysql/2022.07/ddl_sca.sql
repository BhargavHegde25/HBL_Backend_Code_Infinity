ALTER TABLE external_feature_actions MODIFY COLUMN id varchar(255) NOT NULL;
ALTER TABLE external_feature_actions ADD PRIMARY KEY (id);

CREATE TABLE `sca_alias` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `hashedvalue` varchar(100) NOT NULL,
  `userId` varchar(50) DEFAULT NULL,
  `partyId` varchar(50) NOT NULL,
  `type` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2122234738 DEFAULT CHARSET=utf8;
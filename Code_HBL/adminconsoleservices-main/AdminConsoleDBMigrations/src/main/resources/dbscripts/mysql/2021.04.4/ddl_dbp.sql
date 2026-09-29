ALTER TABLE `groupactionlimit` ADD COLUMN `isNewAction` TINYINT(1) NOT NULL DEFAULT '0' AFTER `value`;
ALTER TABLE `contractactionlimit` ADD COLUMN `isNewAction` TINYINT(1) NOT NULL DEFAULT '0' AFTER `value`;
ALTER TABLE `contractfeatures` ADD COLUMN `isNewFeature` TINYINT(1) NOT NULL DEFAULT '0' AFTER `featureId`; 
ALTER TABLE `servicedefinitionactionlimit` ADD COLUMN `isNewAction` TINYINT(1) NOT NULL DEFAULT '0' AFTER `value`;

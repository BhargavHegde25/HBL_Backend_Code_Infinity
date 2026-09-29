DROP VIEW IF EXISTS `configuration_view`;

CREATE VIEW `configuration_view` AS
    SELECT 
        `configurations`.`configuration_id` AS `configuration_id`,
        `configurations`.`bundle_id` AS `bundle_id`,
        `configurationbundles`.`bundle_name` AS `bundle_name`,
        `configurations`.`config_type` AS `type`,
        `configurations`.`config_key` AS `key`,
        `configurations`.`description` AS `description`,
        `configurations`.`config_value` AS `value`,
        `configurations`.`target` AS `target`,
        `configurations`.`isPreLoginConfiguration` AS `isPreLoginConfiguration`,
        `configurationbundles`.`app_id` AS `app_id`,
        `configurationbundles`.`companyLegalUnit` AS `companyLegalUnit`,
        `configurations`.`lastmodifiedts` AS `lastmodifiedts`
    FROM
        (`configurations`
        LEFT JOIN `configurationbundles` ON ((`configurationbundles`.`bundle_id` = `configurations`.`bundle_id`)));
		
ALTER TABLE `featureaction` DROP FOREIGN KEY `FK_featureaction_rrole`;
ALTER TABLE `rrole` CHANGE COLUMN `id` `id` VARCHAR(100) NOT NULL ;
ALTER TABLE `featureaction` CHANGE COLUMN `Rrole_id` `Rrole_id` VARCHAR(100) NULL DEFAULT NULL ;
ALTER TABLE `featureaction` ADD CONSTRAINT `FK_featureaction_rrole`  FOREIGN KEY (`Rrole_id`)  REFERENCES `rrole`(`id`);

ALTER TABLE `configurations` ADD COLUMN `tncTimeStamp` VARCHAR(20);

DROP VIEW IF EXISTS `configuration_view`;

CREATE VIEW `configuration_view` AS
    SELECT 
        `configurations`.`configuration_id` AS `configuration_id`,
        `configurations`.`bundle_id` AS `bundle_id`,
        `configurationbundles`.`bundle_name` AS `bundle_name`,
        `configurations`.`config_type` AS `type`,
        `configurations`.`config_key` AS `key`,
        `configurations`.`description` AS `description`,
        `configurations`.`config_value` AS `value`,
        `configurations`.`target` AS `target`,
        `configurations`.`isPreLoginConfiguration` AS `isPreLoginConfiguration`,
        `configurationbundles`.`app_id` AS `app_id`,
        `configurationbundles`.`companyLegalUnit` AS `companyLegalUnit`,
        `configurations`.`lastmodifiedts` AS `lastmodifiedts`,
		(CASE
            WHEN
                ((`configurations`.`tncTimeStamp` IS NULL)
                    AND (`configurations`.`configuration_id` = 'TNC_REFRESH_1'))
            THEN
                `configurations`.`lastmodifiedts`
            ELSE `configurations`.`tncTimeStamp`
        END) AS `tncTimeStamp`
    FROM
        (`configurations`
        LEFT JOIN `configurationbundles` ON ((`configurationbundles`.`bundle_id` = `configurations`.`bundle_id`)));
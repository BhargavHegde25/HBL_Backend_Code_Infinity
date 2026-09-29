DROP VIEW IF EXISTS `branch_view`;
CREATE VIEW `branch_view` AS
SELECT 
		`loc`.`id` AS `Branch_id`,
        IFNULL(`loc`.`Type_id`, '') AS `Branch_Typeid`,
        IFNULL(`loc`.`Name`, '') AS `Branch_Name`,
        IFNULL(`loc`.`DisplayName`, '') AS `Branch_DisplayName`,
        IFNULL(`loc`.`Description`, '') AS `Branch_Description`,
        IFNULL(`loc`.`Code`, '') AS `Branch_Code`,
        IFNULL(`loc`.`PhoneNumber`, '') AS `Branch_PhoneNumber`,
        IFNULL(`loc`.`EmailId`, '') AS `Branch_EmailId`,
        IFNULL(`loc`.`Status_id`, '') AS `Branch_Status_id`,
        IFNULL(`loc`.`IsMainBranch`, '') AS `Branch_IsMainBranch`,
        IFNULL(`loc`.`WorkSchedule_id`, '') AS `Branch_WorkSchedule_id`,
        IFNULL(`loc`.`MainBranchCode`, '') AS `Branch_MainBranchCode`,
        IFNULL(`loc`.`WebSiteUrl`, '') AS `Branch_WebSiteUrl`,
        `address`.`City_id` AS `City_id`,
        `address`.`cityName` AS `City_Name`,
        `loc`.`Address_id` AS `Address_id`,
        CONCAT(`address`.`addressLine1`,
                ', ',
                IFNULL(`address`.`addressLine2`, ''),
                ', ',
                `address`.`cityName`,
                ', ',
                `region`.`Name`,
                ', ',
                `country`.`Name`,
                ', ',
                IFNULL(`address`.`zipCode`, '')) AS `Branch_Complete_Addr`,
        IFNULL(`loc`.`createdby`, '') AS `Branch_createdby`,
        IFNULL(`loc`.`modifiedby`, '') AS `Branch_modifiedby`,
        IFNULL(`loc`.`createdts`, '') AS `Branch_createdts`,
        IFNULL(`loc`.`lastmodifiedts`, '') AS `Branch_lastmodifiedts`,
        IFNULL(`loc`.`synctimestamp`, '') AS `Branch_synctimestamp`
    FROM
        (((`location` `loc`
        JOIN `address` ON (((`loc`.`Address_id` = `address`.`id`)
            AND (`loc`.`Type_id` = 'Branch'))))
        JOIN `region` ON ((`region`.`id` = `address`.`Region_id`)))
        JOIN `country` ON ((`region`.`Country_id` = `country`.`id`)));
		

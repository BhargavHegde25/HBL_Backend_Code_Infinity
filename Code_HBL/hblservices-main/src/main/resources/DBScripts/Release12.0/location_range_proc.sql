USE `dbxdb`;
DROP procedure IF EXISTS `location_range_proc`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`location_range_proc`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE PROCEDURE `location_range_proc`(
  in _currLatitude TEXT(50), 
  in _currLongitude TEXT(50), 
  in _radius float(50) )
BEGIN DECLARE rowLatitide TEXT(2000);
DECLARE rowLongitude TEXT(2000);
DECLARE b int(1);
DECLARE pipeFlag int(1);
set _radius=_radius/1000;
SELECT 
  location.id AS locationId, 
  (
    SELECT 
      GROUP_CONCAT(
        DISTINCT CONCAT(
          UCASE(
            LEFT(dayschedule.weekdayname, 1)
          ), 
          LCASE(
            SUBSTRING(dayschedule.weekdayname, 2)
          ), 
          ':', 
          SUBSTRING(dayschedule.StartTime, 1, 5), 
          '-', 
          SUBSTRING(dayschedule.endTime, 1, 5)
        ) SEPARATOR ' || '
      ) 
    FROM 
      dayschedule, 
      location 
    WHERE 
      dayschedule.WorkSchedule_id = location.WorkSchedule_id 
      and location.id = locationId
  ) AS workingHours, 
  location.name AS informationTitle, 
  location.phoneNumber AS phone, 
  location.emailId AS email, 
  CASE `location`.`softdeleteflag` WHEN '0' THEN 'OPEN' ELSE 'CLOSED' END AS status, 
  CASE `location`.`type_id` WHEN 'ATM' THEN 'ATM' ELSE 'BRANCH'  END AS type,
  location.status_id AS isVisible,
 (SELECT 
                GROUP_CONCAT(`facility`.`name`
                        ORDER BY `facility`.`id` ASC
                        SEPARATOR '||')
            FROM
                (`facility`
                JOIN `locationfacility`)
            WHERE
                ((`facility`.`id` = CONVERT( `locationfacility`.`facility_id` USING UTF8))
                    AND (CONVERT( `locationfacility`.`Location_id` USING UTF8) =`location`.`id`))) AS  services, 
  (
    SELECT 
      address.cityName
    FROM 
      address
    WHERE 
      address.id = location.Address_id
  ) AS city, 
  address.addressLine1 AS addressLine1, 
  address.addressLine3 AS addressLine3, 
  address.zipCode AS zipCode, 
  address.latitude AS latitude, 
  address.logitude AS longitude, 
  
  CONCAT(`address`.`addressLine1`,
                ', ',
                IFNULL(`address`.`cityName`, ''),
                ', ',
                (SELECT 
                        `region`.`Name`
                    FROM
                        `region`
                    WHERE
                        (`region`.`id` = `address`.`Region_id`)),
                ', ',
                (SELECT 
                        `country`.`Name`
                    FROM
                        `country`
                    WHERE
                        `country`.`id` IN (SELECT 
                                `region`.`Country_id`
                            FROM
                                `region`
                            WHERE
                                (`region`.`id` = `address`.`Region_id`))),
                ', ',
                `address`.`zipCode`) AS `addressLine2`,
  (
    6371 * 2 * ASIN(
      SQRT(
        POWER(
          SIN(
            (
              latitude - ABS(_currLatitude)
            ) * PI() / 180 / 2
          ), 
          2
        ) + COS(
          latitude * PI() / 180
        ) * COS(
          ABS(_currLatitude) * PI() / 180
        ) * POWER(
          SIN(
            (Logitude - _currLongitude) * PI() / 180 / 2
          ), 
          2
        )
      )
    )
  ) AS distance 
FROM 
  address, 
  location 
WHERE 
  address.id = location.address_id 
HAVING 
  distance <= _radius AND isVisible= 'SID_ACTIVE';
  
  
  END$$

DELIMITER ;
;


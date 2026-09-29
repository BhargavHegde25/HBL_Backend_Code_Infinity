USE [${dbxdbname}]
GO
/****** Object:  View [${dbxschemaname}].[locationdetails_view]    Script Date: 7/28/2020 3:42:08 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[locationdetails_view];
GO
CREATE VIEW [${dbxschemaname}].[locationdetails_view]
      AS 
         SELECT DISTINCT 
            
               location.id AS locationId, 
               
                  (
                     SELECT 
                        string_agg( 
                        dayschedule.WeekDayName+
                           ':'+
                           CONVERT(varchar(max),dayschedule.StartTime)+
                           '-'+
                           CONVERT(varchar(max),dayschedule.EndTime),'||' )
                     FROM ([${dbxschemaname}].dayschedule 
                         JOIN [${dbxschemaname}].location l
                     on (dayschedule.WorkSchedule_id = l.WorkSchedule_id AND l.id = location.id)))
                   AS workingHours, 
               location.Name AS informationTitle,
			   location.Name AS name,
               location.Description AS Description, 
               location.Code AS Code, 
               location.PhoneNumber AS phoneNumber, 
               location.EmailId AS email, 
               (
                  CASE 
                     location.Status_id
                        WHEN 'SID_ACTIVE' THEN 'OPEN'
                        ELSE 'CLOSED'
                  END) AS status, 
				location.Status_id AS isVisible,
               location.Type_id AS type, 
               location.isMobile AS isMobile, 
               location.IsMainBranch AS IsMainBranch, 
               
                  (
                     SELECT 
                       string_agg(facility.name , '||')
                     FROM [${dbxschemaname}].facility 
                        JOIN [${dbxschemaname}].locationfacility
                     on ((facility.id = CONVERT(varchar,locationfacility.facility_id)) AND (CONVERT(varchar,locationfacility.Location_id ) = location.id))
                  ) AS services, 
               
                  (
                     SELECT 
                        string_agg(currency.code , ',')
                     FROM [${dbxschemaname}].currency 
                        JOIN [${dbxschemaname}].locationcurrency
                     on ((currency.code = CONVERT(varchar,locationcurrency.currency_code)) AND (CONVERT(varchar,locationcurrency.Location_id)  = location.id))
                  ) AS currencies, 
               
                  (
                     SELECT 
                        string_agg(CAST(customersegment.type as nvarchar(max)) , ',')
                     FROM [${dbxschemaname}].customersegment 
                        JOIN [${dbxschemaname}].locationcustomersegment
                     on ((customersegment.id = CONVERT(varchar,locationcustomersegment.segment_id )) AND (CONVERT(varchar,locationcustomersegment.Location_id) = location.id))
                  ) AS segments, 
               
                  (
                     SELECT 
                       string_agg(CAST(facility.name as nvarchar(max)) , ',')
                     FROM [${dbxschemaname}].facility 
                        JOIN [${dbxschemaname}].locationfacility
                     on ((facility.id = CONVERT(varchar,locationfacility.facility_id)) AND (CONVERT(varchar,locationfacility.Location_id ) = location.id))
                  ) AS facilities_names, 
               
                  (
                     SELECT 
                        string_agg(facility.id ,',')
                     FROM [${dbxschemaname}].facility 
                        JOIN [${dbxschemaname}].locationfacility
                     on ((facility.id = CONVERT(varchar,locationfacility.facility_id )) AND (CONVERT(varchar,locationfacility.Location_id) = location.id))
                  ) AS facilities_codes, 
             
                  (
    SELECT 
              city.Name
                     FROM [${dbxschemaname}].city
                     WHERE (city.id = address.City_id)
                  ) AS city, 
               
                  (
                     SELECT 
                        region.Name
   FROM [${dbxschemaname}].region
                     WHERE (region.id = address.Region_id)
                  ) AS region, 
               
                  (
                     SELECT 
                        country.Name
                     FROM [${dbxschemaname}].country
                     WHERE (country.id = 
                        (
                           SELECT 
                              region.Country_id
             
                           WHERE (region.id = address.Region_id)
                        ))
                  ) AS country, 
               address.addressLine1 AS addressLine1, 
               address.addressLine2 AS addressLine2, 
               address.addressLine3 AS addressLine3, 
               address.zipCode AS zipCode, 
               address.latitude AS latitude, 
               address.logitude AS longitude
         FROM [${dbxschemaname}].address 
            INNER JOIN [${dbxschemaname}].location on
			 address.id = location.Address_id
            INNER JOIN [${dbxschemaname}].region 
         on region.id = address.Region_id
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkassign_permissions_to_role];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[bulkassign_permissions_to_role]  
    @_roleId nvarchar(50),
	@_permissions nvarchar(MAX)
AS 
   BEGIN
	DECLARE @index1 INT;
	DECLARE @numOfPermissions INT;
	DECLARE @permissionsData NVARCHAR(MAX);;
	DECLARE @query NVARCHAR(MAX);;
	
	SET @numOfPermissions = LEN(@_permissions) - LEN(REPLACE(@_permissions, '|', '')) + 1;
	SET @index1 =0;
      
	WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPermissions + 1
               BREAK
            ELSE 
               BEGIN
				SET @permissionsData = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_permissions, '|', @index1), '|', -1 );
				SET @query = ('insert into [dbxdb].rolepermission(role_id,permission_id,softdeleteflag) 
			values (''') +@_roleId + (''',''') + @permissionsData + (''',0);');
				
			EXEC(@query)
			END 
	END
	  
   END
GO
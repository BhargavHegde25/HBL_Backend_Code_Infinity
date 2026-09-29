DROP PROCEDURE IF EXISTS `bulkassign_permissions_to_role`;
DELIMITER $$

CREATE PROCEDURE `bulkassign_permissions_to_role`(
IN _roleId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _permissions TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci)
BEGIN
	DECLARE index1 INTEGER DEFAULT 0;
    SET @numOfPermissions = LENGTH(_permissions) - LENGTH(REPLACE(_permissions, '|', '')) + 1;
    if(@numOfPermissions > 0) then 
	insertPermissions : LOOP
	        SET index1 = index1 + 1;
	        IF index1 = @numOfPermissions + 1 THEN
	            LEAVE insertPermissions;
	        ELSE
	        SET @permissionsData = SUBSTRING_INDEX(SUBSTRING_INDEX(_permissions, '|', index1), '|', -1 );
	        SET @permissionsData = CONCAT("'",@permissionsData,"'");
			SET @query = CONCAT('insert into rolepermission(role_id,permission_id,softdeleteflag) 
				values (''',_roleId,''',',@permissionsData,',','0',');'); 
		
		PREPARE sql_query FROM @query; 
	            EXECUTE sql_query;
		
	         END IF;
    	END LOOP insertPermissions;
    end if;
END$$
DELIMITER ;

DROP procedure IF EXISTS [${dbxschemaname}].[bulkassign_permissions_to_role]
GO

CREATE PROCEDURE [${dbxschemaname}].[bulkassign_permissions_to_role]  
    @_roleId nvarchar(50),
	@_permissions nvarchar(MAX)
AS 
   BEGIN
	DECLARE @index1 INT;
	DECLARE @numOfPermissions INT;
	DECLARE @permissionsData NVARCHAR(MAX);;
	DECLARE @query NVARCHAR(MAX);;
	
	if(@numOfPermissions IS NULL or @numOfPermissions IS NULL)
		return;

	SET @numOfPermissions = LEN(@_permissions) - LEN(REPLACE(@_permissions, '|', '')) + 1;
	SET @index1 = 0;
    
	WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPermissions + 1
               BREAK
            ELSE 
               BEGIN
				SET @permissionsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_permissions, '|', @index1), '|', -1 );
				SET @query = ('insert into [${dbxschemaname}].rolepermission(role_id,permission_id,softdeleteflag) 
			values (''') +@_roleId + (''',''') + @permissionsData + (''',0);');
				
			EXEC(@query)
			END 
	END
	  
   END

GO
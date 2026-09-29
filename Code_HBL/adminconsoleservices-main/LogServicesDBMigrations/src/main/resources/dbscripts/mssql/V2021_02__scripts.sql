USE [${logdbname}]
GO
create procedure [${logschemaname}].[sp_delete_default_constranit]
 @_tableName varchar(100),
 @_columnName varchar(100)
 AS
BEGIN
declare @constraintName varchar(100);
declare @deletequery varchar(500);
set @constraintName=(SELECT d.name   
FROM sys.default_constraints AS d  
INNER JOIN sys.columns AS c  
ON d.parent_object_id = c.object_id
AND d.parent_column_id = c.column_id  
WHERE d.parent_object_id = OBJECT_ID(@_tableName)  
AND c.name = @_columnName)
if @constraintName is not null
BEGIN
 set @deletequery = 'alter table '+@_tableName+' drop constraint '+@constraintName
 exec(@deletequery)
END
END
GO

EXEC [${logschemaname}].sp_delete_default_constranit '${logschemaname}.[adminactivity]', 'userRole';
GO
ALTER TABLE [${logschemaname}].[adminactivity] ALTER COLUMN [userRole] VARCHAR (500)
GO
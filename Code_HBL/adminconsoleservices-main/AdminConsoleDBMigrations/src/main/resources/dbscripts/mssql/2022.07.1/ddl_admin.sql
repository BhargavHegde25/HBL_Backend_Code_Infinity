DROP TABLE IF EXISTS [${dbxschemaname}].[queryconfig];

CREATE TABLE [${dbxschemaname}].[queryconfig] (
    [dbname] VARCHAR(50) NOT NULL,
    [tablename] VARCHAR(100) NOT NULL,
    [querystring] VARCHAR(1000) NOT NULL,
    [primaryKey] VARCHAR(100) NOT NULL,
    PRIMARY KEY ([tablename]),
);


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[SDP_Report_get];
GO

CREATE PROCEDURE [${dbxschemaname}].[SDP_Report_get] @_input VARCHAR(MAX)
AS
BEGIN 
    DECLARE @customerId NVARCHAR(MAX) = '';
    DECLARE @personalData NVARCHAR(MAX) = '';
    DECLARE @personalDataIndex INT = 0;
    DECLARE @personalDataSize INT;
    DECLARE @queryString NVARCHAR(MAX) = '';
    DECLARE @entityId NVARCHAR(MAX) = '';
    DECLARE @dbName NVARCHAR(MAX) = '';
	DECLARE @execQuery NVARCHAR(MAX) = '';

    SET @customerId = JSON_VALUE(@_input, '$.partyId');
    SET @personalData = JSON_QUERY(@_input, '$.personalData');

	SELECT @personalDataSize = COUNT(*) FROM OPENJSON(@personalData, '$');

    WHILE (@personalDataIndex <> @personalDataSize)
    BEGIN
        DECLARE @columnsArray NVARCHAR(MAX) = '';
        DECLARE @columnsArraySize INT;
        DECLARE @columnIndex INT = 0;
        DECLARE @tableName NVARCHAR(MAX) = '';
        DECLARE @columnNamesList NVARCHAR(MAX) = '';
        DECLARE @personalDataObject NVARCHAR(MAX);
        DECLARE @updateQuery NVARCHAR(MAX) = '';
        DECLARE @ParmDefinition NVARCHAR(MAX) = '';

        SET @personalDataObject = JSON_QUERY(@personalData, CONCAT('$[',@personalDataIndex,']')); -- Object which contains data definitions
        SET @tableName = JSON_VALUE(@personalDataObject, '$.dataEntityName'); -- extracting table name from table object

        SELECT @queryString = querystring, @entityId = primaryKey, @dbName = dbname FROM [${dbxschemaname}].[queryconfig] WHERE tablename = @tableName;

        if @queryString <> ''
        BEGIN
            SET @columnsArray = JSON_QUERY(@personalDataObject, '$.dataEntityFields');
            SELECT @columnsArraySize = COUNT(*) FROM OPENJSON(@columnsArray, '$');
         
            WHILE (@columnIndex <> @columnsArraySize)
            BEGIN
                DECLARE @columnCondition NVARCHAR(MAX) = '';
                DECLARE @fieldObject NVARCHAR(MAX) = '';
                DECLARE @fieldName NVARCHAR(MAX) = '';
                    
                SET @fieldObject = JSON_QUERY(@columnsArray, CONCAT('$[',@columnIndex,']'));
                SET @fieldName = JSON_VALUE(@fieldObject, '$.dataEntityFieldName');

                IF @columnNamesList = ''
                BEGIN
                    SET @columnNamesList = @fieldName;
                END
                ELSE
                BEGIN
                    SET @columnNamesList = CONCAT(@columnNamesList,',',@fieldName);
                END
                
                SET @columnIndex = @columnIndex + 1;
            END
            SET @queryString = REPLACE(@queryString, '?', @customerId);
            SET @execQuery = CONCAT('SELECT ''',@tableName,''' as tableName, ''',@entityId,''' as entityId,', @columnNamesList, ' FROM [', @dbName, '].[', @tableName, '] ', @queryString);
            EXEC sp_executesql @execQuery;
        END
        SET @personalDataIndex = @personalDataIndex + 1;
    END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_customer_erasure_status_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[update_customer_erasure_status_proc] @_customerId VARCHAR(MAX), @_erasureStatus VARCHAR(MAX)
AS 
BEGIN
    DECLARE @currentStatusId NVARCHAR(100) = '';
    DECLARE @executeQuery NVARCHAR(MAX) = '';
    DECLARE @updatedStatusId VARCHAR(100) = '';
    IF @_customerId <> ''
    BEGIN
        SELECT @currentStatusId = Status_id FROM [${dbxschemaname}].[customer] where id = @_customerId;

        IF @currentStatusId <> ''
        BEGIN
            IF @_erasureStatus = 'ERASED'
            BEGIN
                IF @currentStatusId = 'SID_CUS_ERASURE_INPROGRESS' 
                BEGIN
                    SET @updatedStatusId = 'SID_CUS_ERASURE_COMPLETED';
                    SET @executeQuery = CONCAT('UPDATE [${dbxschemaname}].[customer] SET Status_id = ''',@updatedStatusId,''' WHERE id = ''',@_customerId,'''');
                    EXEC(@executeQuery);
                END
                ELSE IF @currentStatusId <> 'SID_CUS_ERASURE_COMPLETED'
                BEGIN
                    SELECT 'ERASURE_NOT_INITIATED' AS 'errmsg';
                END
            END
            ELSE IF @_erasureStatus = 'ERASURE.IN.PROGRESS'
            BEGIN
                IF @currentStatusId <> 'SID_CUS_ERASURE_INPROGRESS' AND @currentStatusId <> 'SID_CUS_ERASURE_COMPLETED'
                BEGIN
                    SET @updatedStatusId = 'SID_CUS_ERASURE_INPROGRESS';
                    SET @executeQuery = CONCAT('UPDATE [${dbxschemaname}].[customer] SET Status_id = ''',@updatedStatusId,''' WHERE id = ''',@_customerId,'''');
				    EXEC(@executeQuery);
                END
                ELSE IF @currentStatusId = 'SID_CUS_ERASURE_COMPLETED'
                BEGIN
                    SELECT 'ALREADY_ERASED' AS 'errmsg';
                END   
            END
        END
    END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[erasure_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[erasure_proc] @_input VARCHAR(MAX)
AS
    BEGIN 
        DECLARE @customerId NVARCHAR(MAX) = '';
        DECLARE @personalData NVARCHAR(MAX) = '';
        DECLARE @personalDataIndex INT = 0;
        DECLARE @personalDataSize INT;
        DECLARE @statusQuery NVARCHAR(MAX) = '';

        SET @customerId = JSON_VALUE(@_input, '$.partyId');
        SET @personalData = JSON_QUERY(@_input, '$.personalData');

		SELECT @personalDataSize = COUNT(*) FROM OPENJSON(@personalData, '$');

        SET @statusQuery = CONCAT('UPDATE [${dbxschemaname}].[customer] SET Status_id = ''SID_CUS_ERASURE_INPROGRESS'' WHERE id = ''',@customerId,'''');
		EXEC sp_executesql @statusQuery;

        WHILE (@personalDataIndex <> @personalDataSize)
            BEGIN
                DECLARE @personalDataObject NVARCHAR(MAX) = '';
                DECLARE @dataDefinitionsArray NVARCHAR(MAX) = '';
                DECLARE @datadefindex INT = 0;
                DECLARE @dataDefinitionsLength INT;
            
                SET @personalDataObject = JSON_QUERY(@personalData, CONCAT('$[',@personalDataIndex,']'));
                SET @dataDefinitionsArray = JSON_QUERY(@personalDataObject, '$.dataDefinitions');
                
                SELECT @dataDefinitionsLength = COUNT(*) FROM OPENJSON(@dataDefinitionsArray, '$');
				
                WHILE(@datadefindex <> @dataDefinitionsLength)
                    BEGIN
                        DECLARE @queryString NVARCHAR(MAX) = '';
                        DECLARE @columnsArray NVARCHAR(MAX) = '';
                        DECLARE @columnIndex INT = 0;
                        DECLARE @columnsArraySize INT;
                        DECLARE @tableObject NVARCHAR(MAX) = '';
                        DECLARE @tableName NVARCHAR(MAX) = '';
                        DECLARE @dbName NVARCHAR(MAX) = '';
                        DECLARE @updateQuery NVARCHAR(MAX) = '';
                        DECLARE @columnQuery NVARCHAR(MAX) = '';
                        DECLARE @ParmDefinition NVARCHAR(MAX) = '';
                        DECLARE @execQuery NVARCHAR(MAX) = '';

                        SET @tableObject = JSON_QUERY(@dataDefinitionsArray, CONCAT('$[',@datadefindex,']')); -- Getting table object
                        SET @tableName = JSON_VALUE(@tableObject, '$.dataEntityName'); -- extracting table name from table object
                        
                        SELECT @queryString = querystring, @dbName = dbname FROM [${dbxschemaname}].[queryconfig] WHERE tablename = @tableName;
                        
                        IF @queryString <> ''
                            BEGIN
                                SET @columnsArray = JSON_QUERY(@tableObject, '$.dataEntityFields');

                                SELECT @columnsArraySize = COUNT(*) FROM OPENJSON(@columnsArray, '$');

                                WHILE(@columnIndex <> @columnsArraySize)
                                    BEGIN
                                        DECLARE @columnCondition NVARCHAR(MAX) = '';
                                        DECLARE @fieldObject NVARCHAR(MAX) = '';
                                        DECLARE @fieldName NVARCHAR(MAX) = '';
                                        DECLARE @fieldType NVARCHAR(MAX) = '';
                                        DECLARE @erasureOptionsId NVARCHAR(MAX) = '';
                                        DECLARE @erasureOptionsValue NVARCHAR(MAX) = '';
                                        DECLARE @updatedValue NVARCHAR(100) = '';
                                        
                                        SET @fieldObject = JSON_QUERY(@columnsArray, CONCAT('$[',@columnIndex,']'));
                                        SET @fieldName = JSON_VALUE(@fieldObject, '$.dataEntityFieldName');
                                        SET @fieldType = JSON_VALUE(@fieldObject, '$.dataEntityFieldDataType');
                                        SET @erasureOptionsId = JSON_VALUE(@fieldObject, '$.erasureOptions.optionsId');
                                        SET @erasureOptionsValue = JSON_VALUE(@fieldObject, '$.erasureOptions.optionsValue');

                                        IF @erasureOptionsId <> 'NO ACTION'
                                            BEGIN
                                            IF @erasureOptionsId = 'NULLIFY'
                                                BEGIN
                                                    SET @columnCondition = CONCAT(@fieldName, '=NULL');
                                                END
                                            ELSE
                                                BEGIN
                                                    IF @tableName = 'customerdevice' and @fieldName = 'LastLoginTime'
                                                        BEGIN
                                                            SET @updatedValue = '1753-01-01 00:00:00';
                                                        END
                                                    ELSE IF @tableName = 'auditactivity' and @fieldName = 'EventData'
                                                        BEGIN
                                                            SET @updatedValue = '{}';
                                                        END
                                                    ELSE IF @tableName = 'customer' and @fieldName = 'UserName'
                                                        BEGIN
                                                            select @updatedValue = CONVERT(varchar(150),SUBSTRING(CONVERT(varchar(150), NEWID()), 0, 8)) + '-' + CONVERT(varchar(150),SUBSTRING(CONVERT(varchar(150), NEWID()), 0, 8));
                                                        END
                                                    ELSE IF @fieldType = 'ALPHA'
                                                        BEGIN
                                                            SET @updatedValue = REPLICATE(@erasureOptionsValue,10);
                                                        END
                                                    ELSE IF @fieldType = 'NUMBER'
                                                        BEGIN
                                                            SET @updatedValue = 9;
                                                        END
                                                    ELSE IF @fieldType = 'DATE'
                                                        BEGIN
                                                            SET @updatedValue =	CAST(@updatedValue as date);
                                                            SET @updatedValue = '9999-12-31';
                                                        END
                                                    SET @columnCondition = CONCAT(@fieldName,'=''',@updatedValue,'''');
                                                END
                                            END
                                        IF @columnQuery <> ''
                                            SET @columnQuery = CONCAT(@columnQuery,',');
                                        SET @columnQuery = CONCAT(@columnQuery,@columnCondition);

                                        SET @columnIndex = @columnIndex + 1;
                                    END
                            
                                SET @queryString = REPLACE(@queryString, '?', CONCAT('''',@customerId,''''));
                                SET @execQuery = CONCAT('UPDATE [${dbxschemaname}].[', @tableName, '] SET ', @columnQuery, ' ', @queryString, ' ');
                                EXEC sp_executesql @execQuery
                                SET @datadefindex = @datadefindex + 1;
                            END
                    END
                SET @personalDataIndex = @personalDataIndex + 1;
            END
    END
GO

CREATE PROCEDURE [${dbxschemaname}].[GetExternalPayeesProc]

  @userId varchar(500)

AS

BEGIN

    SELECT * from internationalpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 1 and softDelete = 0 and
    cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1)

    UNION

    SELECT * from interbankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and
    cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1)

    UNION

    SELECT * from intrabankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id where isSameBankAccount = 1  and softDelete = 0 and
    cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1)

    ;

END

GO

CREATE INDEX [idx_interbankpayee_cif] ON [${dbxschemaname}].[interbankpayee] ([cif]);
CREATE INDEX [idx_internationalpayee_cif] ON [${dbxschemaname}].[internationalpayee] ([cif]);
CREATE INDEX [idx_intrabankpayee_cif] ON [${dbxschemaname}].[intrabankpayee] ([cif]);
CREATE INDEX [idx_corporatepayees_cif] ON [${dbxschemaname}].[corporatepayees] ([cif]);

USE [${dbxschemaname}]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[fetch_contractaccounts_for_user_proc]
	@_contractIdList NVARCHAR(MAX),
	@_contractId NVARCHAR(50),
	@_coreCustomerId NVARCHAR(50)
AS BEGIN

DECLARE @sql_query NVARCHAR(MAX);

IF (@_contractIdList != '' and @_contractIdList IS NOT NULL) BEGIN
set @sql_query = 'select * from [${dbxschemaname}].contractaccounts where contractId in ( select value from STRING_SPLIT(''' + @_contractIdList + ''' , '',''));';

END
ELSE BEGIN
IF ((@_contractId IS NOT NULL and LEN(@_contractId)>0) AND (@_coreCustomerId IS NOT NULL and LEN(@_coreCustomerId)>0) )
set @sql_query = 'select * from [${dbxschemaname}].contractaccounts where contractId = ''' + @_contractId+ ''' and coreCustomerId ='''+ @_coreCustomerId+ ''';';

END;
EXEC(@sql_query);

END;
GO

USE [${dbxschemaname}]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[fetch_contractcorecustomers_for_user_proc]
	@_contractIdList NVARCHAR(MAX),
	@_contractId NVARCHAR(50),
	@_coreCustomerId NVARCHAR(50)
AS BEGIN

DECLARE @sql_query NVARCHAR(MAX);

IF (@_contractIdList != '' and @_contractIdList IS NOT NULL) BEGIN
set @sql_query = 'select * from [${dbxschemaname}].contractcustomers where contractId in ( select value from STRING_SPLIT(''' + @_contractIdList + ''' , '',''));';

END
ELSE BEGIN
IF ((@_contractId IS NOT NULL and LEN(@_contractId)>0) AND (@_coreCustomerId IS NOT NULL and LEN(@_coreCustomerId)>0) )
set @sql_query = 'select * from [${dbxschemaname}].contractcustomers where contractId = ''' + @_contractId+ ''' and coreCustomerId ='''+ @_coreCustomerId+ ''';';
END;

EXEC(@sql_query);

END;
GO

USE [${dbxschemaname}]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[get_feature_actions_view_proc]
@_roleTypeId nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)

SET @select_statement = 'SELECT ${dbxschemaname}.featureaction.id AS actionId, ${dbxschemaname}.featureaction.Feature_id AS featureId, ${dbxschemaname}.featureaction.name AS actionName, ${dbxschemaname}.featureaction.isAccountLevel, ${dbxschemaname}.featureaction.description AS actionDescription,
${dbxschemaname}.featureaction.isMFAApplicable, ${dbxschemaname}.featureaction.isPrimary, ${dbxschemaname}.featureaction.notes, ${dbxschemaname}.featureaction.Type_id AS typeId, ${dbxschemaname}.featureaction.DisplaySequence AS actionDisplaySequence,
${dbxschemaname}.featureaction.dependency AS actionDependency, ${dbxschemaname}.featureaction.status AS actionStatus, ${dbxschemaname}.featureactionroletype.RoleType_id AS actionType, ${dbxschemaname}.accesspolicy.name AS accessPolicy,
${dbxschemaname}.limitgroup.name AS limitGroup, ${dbxschemaname}.featureaction.accessPolicyId, ${dbxschemaname}.featureaction.limitGroupId, ${dbxschemaname}.feature.Status_id AS featureStatus, ${dbxschemaname}.feature.name AS featureName,
${dbxschemaname}.feature.description AS featureDescription, ${dbxschemaname}.feature.Type_id AS featureType, ${dbxschemaname}.featureroletype.RoleType_id AS featureGroup, ${dbxschemaname}.feature.DisplaySequence AS featureDisplaySequence,
${dbxschemaname}.feature.isPrimary AS isFeaturePrimary, ${dbxschemaname}.actionlevel.name AS actionlevel, ${dbxschemaname}.featureaction.actionlevelId, ${dbxschemaname}.actiondisplaynamedescription.Locale_id AS localeId,
${dbxschemaname}.actiondisplaynamedescription.displayName, ${dbxschemaname}.actiondisplaynamedescription.displayDescription, ${dbxschemaname}.actionlimit.LimitType_id AS limitTypeId, ${dbxschemaname}.actionlimit.value,
${dbxschemaname}.dependentactions_view.dependentactionId, ${dbxschemaname}.dependentactions_view.featureName AS dependentFeatureName, ${dbxschemaname}.dependentactions_view.actionName AS dependentActionName,
${dbxschemaname}.dependentactions_view.featureId AS dependentFeatureId, ${dbxschemaname}.termandcondition.Code AS termsAndConditionCode, ${dbxschemaname}.termandcondition.Title AS termsAndConditionTitle,
${dbxschemaname}.termandcondition.Description AS termsAndConditionDescription, ${dbxschemaname}.membergrouptype.description AS roleTypeName
FROM ${dbxschemaname}.featureaction LEFT OUTER JOIN
${dbxschemaname}.feature ON ${dbxschemaname}.feature.id = ${dbxschemaname}.featureaction.Feature_id LEFT OUTER JOIN
${dbxschemaname}.actiondisplaynamedescription ON ${dbxschemaname}.actiondisplaynamedescription.Action_id = ${dbxschemaname}.featureaction.id LEFT OUTER JOIN
${dbxschemaname}.accesspolicy ON ${dbxschemaname}.featureaction.accesspolicyId = ${dbxschemaname}.accesspolicy.id LEFT OUTER JOIN
${dbxschemaname}.featureroletype ON ${dbxschemaname}.featureroletype.Feature_id = ${dbxschemaname}.feature.id LEFT OUTER JOIN
${dbxschemaname}.featureactionroletype ON ${dbxschemaname}.featureaction.id = ${dbxschemaname}.featureactionroletype.Action_id LEFT OUTER JOIN
${dbxschemaname}.termandcondition ON ${dbxschemaname}.featureaction.TermsAndConditions_id = ${dbxschemaname}.termandcondition.id LEFT OUTER JOIN
${dbxschemaname}.limitgroup ON ${dbxschemaname}.featureaction.limitgroupId = ${dbxschemaname}.limitgroup.id LEFT OUTER JOIN
${dbxschemaname}.actionlevel ON ${dbxschemaname}.featureaction.actionlevelId = ${dbxschemaname}.actionlevel.id LEFT OUTER JOIN
${dbxschemaname}.dependentactions_view ON ${dbxschemaname}.featureaction.id = ${dbxschemaname}.dependentactions_view.actionId LEFT OUTER JOIN
${dbxschemaname}.membergrouptype ON ${dbxschemaname}.featureactionroletype.RoleType_id = ${dbxschemaname}.membergrouptype.id LEFT OUTER JOIN
${dbxschemaname}.actionlimit ON ${dbxschemaname}.actionlimit.Action_id = ${dbxschemaname}.featureaction.id';

if @_roleTypeId is not null and len(@_roleTypeId)>0
	SET @select_statement = CONCAT (@select_statement ,' where ${dbxschemaname}.featureroletype.RoleType_Id = ''', @_roleTypeId,''' and ${dbxschemaname}.featureactionroletype.RoleType_Id = ''',@_roleTypeId,''';');

exec(@select_statement);

END;
GO

USE [${dbxschemaname}]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[group_features_actions_view_proc]
@_groupId nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)


SET @select_statement = 'SELECT ${dbxschemaname}.groupactionlimit.Group_id, ${dbxschemaname}.groupactionlimit.Action_id, ${dbxschemaname}.groupactionlimit.LimitType_id, ${dbxschemaname}.groupactionlimit.value, ${dbxschemaname}.groupactionlimit.id AS groupactionlimit_id,
${dbxschemaname}.groupactionlimit.softdeleteflag AS softdelete, ${dbxschemaname}.membergroup.Type_id, ${dbxschemaname}.membergroup.Name AS Group_name, ${dbxschemaname}.membergroup.Description AS Group_description, ${dbxschemaname}.featureaction.name AS Action_name,
${dbxschemaname}.featureaction.description AS Action_description, ${dbxschemaname}.featureaction.Type_id AS Action_Type_id, ${dbxschemaname}.featureaction.Feature_id, ${dbxschemaname}.featureaction.isMFAApplicable, ${dbxschemaname}.featureaction.isAccountLevel,
${dbxschemaname}.featureaction.isPrimary, ${dbxschemaname}.featureaction.DisplaySequence AS Action_displaysequence, ${dbxschemaname}.featureaction.dependency AS Action_dependency, ${dbxschemaname}.featureaction.status AS actionStatus,
${dbxschemaname}.accesspolicy.name AS accessPolicy, ${dbxschemaname}.featureaction.accesspolicyId, ${dbxschemaname}.featureaction.limitgroupId, ${dbxschemaname}.limitgroup.name AS limitGroup, ${dbxschemaname}.actionlevel.name AS actionlevel, ${dbxschemaname}.featureaction.actionlevelId,
${dbxschemaname}.feature.name AS featureName, ${dbxschemaname}.feature.name AS Feature_name, ${dbxschemaname}.feature.description AS Feature_description, ${dbxschemaname}.feature.Type_id AS Feature_Type_id, ${dbxschemaname}.feature.Status_id AS Feature_Status_id,
${dbxschemaname}.feature.DisplaySequence AS Feature_displaysequence, ${dbxschemaname}.feature.isPrimary AS Feature_isPrimary
FROM ${dbxschemaname}.groupactionlimit LEFT OUTER JOIN
${dbxschemaname}.membergroup ON ${dbxschemaname}.membergroup.id = ${dbxschemaname}.groupactionlimit.Group_id LEFT OUTER JOIN
${dbxschemaname}.featureaction ON ${dbxschemaname}.featureaction.id = ${dbxschemaname}.groupactionlimit.Action_id LEFT OUTER JOIN
${dbxschemaname}.feature ON ${dbxschemaname}.feature.id = ${dbxschemaname}.featureaction.Feature_id LEFT OUTER JOIN
${dbxschemaname}.accesspolicy ON ${dbxschemaname}.featureaction.accesspolicyId = ${dbxschemaname}.accesspolicy.id LEFT OUTER JOIN
${dbxschemaname}.limitgroup ON ${dbxschemaname}.featureaction.limitgroupId = ${dbxschemaname}.limitgroup.id LEFT OUTER JOIN
${dbxschemaname}.actionlevel ON ${dbxschemaname}.featureaction.actionlevelId = ${dbxschemaname}.actionlevel.id';

if @_groupId is not null and len(@_groupId)>0
SET @select_statement = CONCAT (@select_statement ,' where ${dbxschemaname}.groupactionlimit.Group_id = ''', @_groupId,''';');

exec(@select_statement);

END;
GO

USE [${dbxschemaname}]
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[servicedefinition_view_proc]    Script Date: 6/9/2022 4:03:11 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[servicedefinition_view_proc]
@_typeId nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)

SET @select_statement = 'SELECT id, name, description, serviceType, status,
(SELECT COUNT(Group_id) AS Expr1
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id)) AS numberOfRoles,
(SELECT COUNT(id) AS Expr1
FROM ${dbxschemaname}.membergroup
WHERE (Status_id LIKE ''SID_ACTIVE'') AND (id IN
(SELECT Group_id
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id)))) AS numberOfActiveRoles,
(SELECT Group_id
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id) AND (isDefaultGroup = 1)) AS defaultRole,
(SELECT COUNT(DISTINCT featureId) AS Expr1
FROM ${dbxschemaname}.servicedefinition_features_actions_view
WHERE (${dbxschemaname}.servicedefinition.id = serviceDefinitionId) AND (softdelete = ''0'')) AS numberOfFeatures,
(SELECT COUNT(id) AS Expr1
FROM ${dbxschemaname}.contract
WHERE (servicedefinitionId = ${dbxschemaname}.servicedefinition.id)) AS numberOfContracts
FROM ${dbxschemaname}.servicedefinition';

if @_typeId is not null and len(@_typeId)>0
SET @select_statement = CONCAT (@select_statement ,' where serviceType = ''', @_typeId, ''';');

exec(@select_statement);

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[userPermissionsAndActionsCreate_proc]
GO
/****** Object:  StoredProcedure [dbxdb].[userPermissionsAndActionsCreate_proc]    Script Date: 8/17/2022 10:15:09 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Sai Kiran Kaladi>
-- Create date: <Create Date,,>
-- Description:	<DCreated Stored procedure for optimizin createPermissionsAndActions Java method>
-- =============================================
CREATE PROCEDURE [${dbxschemaname}].[userPermissionsAndActionsCreate_proc] 
	-- Add the parameters for the stored procedure here
	@_userId nvarchar(50),
	@_loggedInUserId nvarchar(50),
    @_listOfAddedPermissions nvarchar(max)
   
AS
BEGIN
	-- SET NOCOUNT ON added to prevent extra result sets from
	-- interfering with SELECT statements.
	SET NOCOUNT ON
	SET  NOCOUNT  ON
     DECLARE
         @finished int = 0
      DECLARE
         @permissionActionId varchar(255) = N''
      DECLARE
         @permissionId varchar(255) = N''
      DECLARE
         @actionslist varchar(max) = N''
      DECLARE
         @is_Enabled varchar(1) = N''
      DECLARE
         @Name varchar(50) = N''
       DECLARE
         @rowCount int = 0
	   DECLARE
         @done int = 0

DECLARE permissions CURSOR LOCAL
FOR (SELECT id FROM [${dbxschemaname}].permission WHERE charindex(id,@_listOfAddedPermissions)<>0);

OPEN permissions
FETCH NEXT FROM permissions into @permissionId

WHILE (@@FETCH_STATUS=0)

         BEGIN
		/*RAISERROR('START1',0,1)WITH NOWAIT;*/
		
		DECLARE permissionActions CURSOR LOCAL
		FOR (SELECT compositeaction.id,compositeaction.Name,compositeaction.isEnabled FROM [${dbxschemaname}].compositeaction 
		WHERE Permission_id=@permissionId);
		
		 OPEN permissionActions

		 FETCH NEXT FROM permissionActions into @permissionActionID,@Name,@is_Enabled
		 
		 WHILE (@@FETCH_STATUS=0)

			BEGIN
				/*RAISERROR('START2',0,1)WITH NOWAIT;*/
				SET @rowCount =  (SELECT count(*) from [${dbxschemaname}].usercompositeaction where CompositeAction_id=@permissionActionId AND User_id=@_userId);
				
				IF (@rowCount = 0)
					BEGIN
						INSERT [${dbxschemaname}].usercompositeaction([${dbxschemaname}].usercompositeaction.CompositeAction_id,
												   [${dbxschemaname}].usercompositeaction.User_id,
												   [${dbxschemaname}].usercompositeaction.createdby,
												   [${dbxschemaname}].usercompositeaction.modifiedby,
												   [${dbxschemaname}].usercompositeaction.isEnabled)
												   VALUES (@permissionActionId,@_userId,@_loggedInUserId,@_loggedInUserId,@is_Enabled)
					END
				ELSE
					BEGIN
						UPDATE [${dbxschemaname}].usercompositeaction SET isEnabled=@is_Enabled WHERE [${dbxschemaname}].usercompositeaction.CompositeAction_id=@permissionActionId AND [${dbxschemaname}].usercompositeaction.User_id=@_userId
					END

				

				FETCH NEXT FROM permissionActions into @permissionActionID,@Name,@is_Enabled
				CONTINUE
			END
			CLOSE permissionActions
			DEALLOCATE permissionActions

	BEGIN TRY
	INSERT [${dbxschemaname}].userpermission([${dbxschemaname}].userpermission.User_id,
							  [${dbxschemaname}].userpermission.Permission_id,
							  [${dbxschemaname}].userpermission.createdby,
							  [${dbxschemaname}].userpermission.modifiedby) 
							  VALUES  (@_userId,@permissionId,@_loggedInUserId,@_loggedInUserId)
							 

	END TRY
	BEGIN CATCH
	RAISERROR('Permission Already Exists',0,1)WITH NOWAIT;
	END CATCH
FETCH NEXT FROM permissions into @permissionId
CONTINUE
END
CLOSE permissions
DEALLOCATE permissions

END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatorygroups_in_approvalrule_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroups_in_approvalrule_proc]
AS
BEGIN
	
	SELECT [signatorygroup].[signatoryGroupId] 
	FROM [${dbxschemaname}].[signatorygroup] 
	WHERE [${dbxschemaname}].FIND_IN_SET([signatorygroup].[signatoryGroupId],
	(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygroupmatrix].[groupList],']',''),'[',''),'"',''),',')
		FROM [${dbxschemaname}].[signatorygroupmatrix] 
        WHERE [signatorygroupmatrix].[approvalMatrixId] IN (SELECT [approvalmatrix].[id] FROM [${dbxschemaname}].[approvalmatrix] where [approvalmatrix].[softdeleteflag] = 'false')
		AND [signatorygroupmatrix].[softdeleteflag] = 'false')) > 0 ;
END
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[delete_prospect_data_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[delete_prospect_data_proc] @_input VARCHAR(MAX)
AS
    BEGIN 
        DECLARE @customerIds NVARCHAR(MAX) = '';
        DECLARE @tableNames NVARCHAR(MAX) = '';
        DECLARE @tableSize INT;
        DECLARE @execQuery NVARCHAR(MAX) = '';
		DECLARE @tableName NVARCHAR(MAX) = '';
        DECLARE @queryString NVARCHAR(MAX) = '';
        DECLARE @dpiIndex INT = 0;
        DECLARE @customerIdsLength INT;

		set @tableNames = '["backendidentifier","passwordhistory","customerpreference","customeraddress","customergroup","customercommunication","customer"]';
        SET @customerIds = @_input;
        SELECT @customerIdsLength = COUNT(*) FROM OPENJSON(@customerIds, '$');
        SELECT @tableSize = COUNT(*) FROM OPENJSON(@tableNames, '$');
        while(@dpiIndex <> @customerIdsLength)
        	BEGIN
	        	DECLARE @customerId NVARCHAR(MAX);
	        	DECLARE @customerType NVARCHAR(MAX);
	         	SET @customerId = JSON_VALUE(@customerIds, CONCAT('$[',@dpiIndex,']'));
        		select @customerType = CustomerType_id from [${dbxschemaname}].[customer] WHERE id = @customerId;
        		if @customerType = 'TYPE_ID_PROSPECT'
    			BEGIN
    				DECLARE @tableIndex INT = 0;
    				WHILE (@tableIndex <> @tableSize)
		            BEGIN
						SET @tableName = JSON_VALUE(@tableNames, CONCAT('$[',@tableIndex,']'));
		                SELECT @queryString = querystring FROM [${dbxschemaname}].[queryconfig] WHERE tablename = @tableName;
						SET @queryString = REPLACE(@queryString, '?', CONCAT('''',@customerId,''''));
		                SET @execQuery = CONCAT('delete from [${dbxschemaname}].[', @tableName, '] ',@queryString, ' ');
						EXEC sp_executesql @execQuery
		                SET @tableIndex = @tableIndex + 1;
		            END
    			END
        	END
    END
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[makercheckerconfig_view];
GO

CREATE VIEW [${dbxschemaname}].[makercheckerconfig_view](
    [id],
    [moduleName],
    [actionName],
    [languageCode],
    [isApprovalRequired],
    [companyLegalUnit]
    ) AS
    SELECT
        [mcconfig].[id] AS [id],
        [mcmodule].[name] AS [moduleName],
        [mcaction].[name] AS [actionName],
        [mcaction].[language_code] AS [languageCode],
        [mcconfig].[isApprovalRequired] AS [isApprovalRequired],
        [mcconfig].[companyLegalUnit] AS [companyLegalUnit]
    FROM
    [${dbxschemaname}].[makercheckerconfig] as [mcconfig] 
    JOIN [${dbxschemaname}].[mcmoduletext] as [mcmodule] ON ([mcconfig].[module] = [mcmodule].[id])
    JOIN [${dbxschemaname}].[mcactiontext] as [mcaction] ON ([mcconfig].[action] = [mcaction].[id] 
        AND [mcmodule].[language_code] = [mcaction].[language_code]);
GO

ALTER TABLE [${dbxschemaname}].[makercheckerconfig] ADD [auditModule] VARCHAR(50) NULL;
GO
ALTER TABLE [${dbxschemaname}].[makercheckerconfig] ADD [auditEvent] VARCHAR(50) NULL;
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [payeeVerification] VARCHAR(45) NULL;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[update_approvalrequests_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[update_approvalrequests_proc]
   @_status nvarchar(50),
   @_reason nvarchar(max),
   @_requestId nvarchar(50),
   @_currentStatus varchar(50),
   @_checkedBy varchar(50)
   AS
 
BEGIN
SET  XACT_ABORT  ON
SET  NOCOUNT  ON
update [${dbxschemaname}].[approvalrequests] set status=@_status, reason=@_reason, checkedBy=@_checkedBy, checkedts=current_timestamp where requestId=@_requestId and status = @_currentStatus;
select @@ROWCOUNT as result;
END
GO

ALTER TABLE [${dbxschemaname}].[makercheckerconfig] ADD [excludedParams] VARCHAR(200) DEFAULT NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_maker_pending_requests_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_maker_pending_requests_proc]
    @_legalEntityId VARCHAR(1024),  
    @_module VARCHAR(1024),
    @_action VARCHAR(1024),
    @_userName VARCHAR(50),
    @_submittedDate DATE,
    @_pageOffset INT,
    @_pageSize INT
AS
BEGIN
	SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
    DECLARE @userId_Id VARCHAR(50);
    DECLARE @LegalEntity_Id VARCHAR(1024); 
    DECLARE @Request_Module VARCHAR(1024);
    DECLARE @Request_Action VARCHAR(1024);
    DECLARE @submittedDateParam DATE;
    DECLARE @value VARCHAR(50);
    DECLARE @sql_query NVARCHAR(MAX);
	DECLARE @sql_query_count NVARCHAR(MAX);

    DROP TABLE IF EXISTS #temp_maker_requests;
    
    CREATE TABLE #temp_maker_requests (
        LegalEntityId VARCHAR(50),
        ModuleName VARCHAR(255),
        ActionName VARCHAR(255)
    );
 
    SET @userId_Id = @_userName;
    SET @LegalEntity_Id = @_legalEntityId;
    SET @Request_Module = @_module;
    SET @Request_Action = @_action;
    SET @submittedDateParam = @_submittedDate;
 
    WHILE LEN(@LegalEntity_Id) > 0
    BEGIN
        SET @value = LTRIM(RTRIM(SUBSTRING(@LegalEntity_Id, 1, CHARINDEX(',', @LegalEntity_Id + ',') - 1)));
        SET @LegalEntity_Id = LTRIM(RTRIM(SUBSTRING(@LegalEntity_Id, CHARINDEX(',', @LegalEntity_Id + ',') + 1, LEN(@LegalEntity_Id))));
        INSERT INTO #temp_maker_requests (LegalEntityId) VALUES (@value);
    END;
    
    IF @Request_Module IS NOT NULL
    BEGIN
        WHILE LEN(@Request_Module) > 0
        BEGIN
            SET @value = LTRIM(RTRIM(SUBSTRING(@Request_Module, 1, CHARINDEX(',', @Request_Module + ',') - 1)));
            SET @Request_Module = LTRIM(RTRIM(SUBSTRING(@Request_Module, CHARINDEX(',', @Request_Module + ',') + 1, LEN(@Request_Module))));
            INSERT INTO #temp_maker_requests (ModuleName) VALUES (@value);
        END;
    END;

    IF @Request_Action IS NOT NULL
    BEGIN
        WHILE LEN(@Request_Action) > 0
        BEGIN
            SET @value = LTRIM(RTRIM(SUBSTRING(@Request_Action, 1, CHARINDEX(',', @Request_Action + ',') - 1)));
            SET @Request_Action = LTRIM(RTRIM(SUBSTRING(@Request_Action, CHARINDEX(',', @Request_Action + ',') + 1, LEN(@Request_Action))));
            INSERT INTO #temp_maker_requests (ActionName) VALUES (@value);
        END;
    END;
 
    SET @sql_query = '
        SELECT requestId, module, action, createdts, createdby, companyLegalUnit
        FROM [${dbxschemaname}].approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby = ''' + @userId_Id + ''' AND (';
    
    SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + LegalEntityId + ''', companyLegalUnit) > 0', ' OR ') FROM #temp_maker_requests;
    
	SET @sql_query = @sql_query + ')';
	
	SET @sql_query_count = '
        SELECT count(*) AS totalRecords
        FROM [${dbxschemaname}].approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby = ''' + @userId_Id + ''' AND (';
    
    SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + LegalEntityId + ''', companyLegalUnit) > 0', ' OR ') FROM #temp_maker_requests;
    
    SET @sql_query_count = @sql_query_count + ')';
    
    IF @Request_Module IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND (';
        SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + ModuleName + ''', module) > 0', ' OR ') FROM #temp_maker_requests;
        SET @sql_query = @sql_query + ')';
		
		SET @sql_query_count = @sql_query_count + '
            AND (';
        SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + ModuleName + ''', module) > 0', ' OR ') FROM #temp_maker_requests;
        SET @sql_query_count = @sql_query_count + ')';
    END;

    IF @Request_Action IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND (';
        SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + ActionName + ''', action) > 0', ' OR ') FROM #temp_maker_requests;
        SET @sql_query = @sql_query + ')';
		
        SET @sql_query_count = @sql_query_count + '
            AND (';
        SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + ActionName + ''', action) > 0', ' OR ') FROM #temp_maker_requests;
        SET @sql_query_count = @sql_query_count + ')';
    END;

    IF @submittedDateParam IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND CONVERT(DATE, createdts) = ''' + CONVERT(VARCHAR, @submittedDateParam, 23) + '''';
		SET @sql_query_count = @sql_query_count + '
            AND CONVERT(DATE, createdts) = ''' + CONVERT(VARCHAR, @submittedDateParam, 23) + '''';
    END;
	
	EXEC sp_executesql @sql_query_count;
    
    SET @sql_query = @sql_query + ' ORDER BY createdts ASC OFFSET ' + CAST(@_pageOffset AS NVARCHAR) + ' ROWS FETCH NEXT ' + CAST(@_pageSize AS NVARCHAR) + ' ROWS ONLY;';
 
    EXEC sp_executesql @sql_query;
 
    DROP TABLE IF EXISTS #temp_maker_requests;
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_checkerpending_requests_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[get_checkerpending_requests_proc]
    @_legalEntityId VARCHAR(1024),  
    @_module VARCHAR(1024),
    @_action VARCHAR(1024),
    @_userName VARCHAR(50),
    @_submittedDate DATE,
    @_pageOffset INT,
    @_pageSize INT
AS
BEGIN
	SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
    DECLARE @userId_Id VARCHAR(50);
    DECLARE @LegalEntity_Id VARCHAR(1024); 
    DECLARE @Request_Module VARCHAR(1024);
    DECLARE @Request_Action VARCHAR(1024);
    DECLARE @submittedDate DATE;
    DECLARE @value VARCHAR(50);
    DECLARE @sql_query NVARCHAR(MAX);
	DECLARE @sql_query_count NVARCHAR(MAX);

    DROP TABLE IF EXISTS #temp_checker_requests_filters;
    
    CREATE TABLE #temp_checker_requests_filters (
        LegalEntityId VARCHAR(50),
        ModuleName VARCHAR(255),
        ActionName VARCHAR(255)
    );
 
    SET @userId_Id = @_userName;
    SET @LegalEntity_Id = @_legalEntityId;
    SET @Request_Module = @_module;
    SET @Request_Action = @_action;
    SET @submittedDate = @_submittedDate;
 
    WHILE LEN(@LegalEntity_Id) > 0
    BEGIN
        SET @value = LTRIM(RTRIM(SUBSTRING(@LegalEntity_Id, 1, CHARINDEX(',', @LegalEntity_Id + ',') - 1)));
        SET @LegalEntity_Id = LTRIM(RTRIM(SUBSTRING(@LegalEntity_Id, CHARINDEX(',', @LegalEntity_Id + ',') + 1, LEN(@LegalEntity_Id))));
        INSERT INTO #temp_checker_requests_filters (LegalEntityId) VALUES (@value);
    END;
    
    IF @Request_Module IS NOT NULL
    BEGIN
        WHILE LEN(@Request_Module) > 0
        BEGIN
            SET @value = LTRIM(RTRIM(SUBSTRING(@Request_Module, 1, CHARINDEX(',', @Request_Module + ',') - 1)));
            SET @Request_Module = LTRIM(RTRIM(SUBSTRING(@Request_Module, CHARINDEX(',', @Request_Module + ',') + 1, LEN(@Request_Module))));
            INSERT INTO #temp_checker_requests_filters (ModuleName) VALUES (@value);
        END;
    END;

    IF @Request_Action IS NOT NULL
    BEGIN
        WHILE LEN(@Request_Action) > 0
        BEGIN
            SET @value = LTRIM(RTRIM(SUBSTRING(@Request_Action, 1, CHARINDEX(',', @Request_Action + ',') - 1)));
            SET @Request_Action = LTRIM(RTRIM(SUBSTRING(@Request_Action, CHARINDEX(',', @Request_Action + ',') + 1, LEN(@Request_Action))));
            INSERT INTO #temp_checker_requests_filters (ActionName) VALUES (@value);
        END;
    END;
 
    SET @sql_query = '
        SELECT requestId, module, action, permissionName, createdts, createdby, companyLegalUnit
        FROM [${dbxschemaname}].approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''' + @userId_Id + ''' AND (';
    
    SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + LegalEntityId + ''', companyLegalUnit) > 0', ' OR ') FROM #temp_checker_requests_filters;
    
	SET @sql_query = @sql_query + ')';
	
	SET @sql_query_count = '
        SELECT count(*) AS totalRecords
        FROM [${dbxschemaname}].approvalrequests
        WHERE status = ''SID_PENDING'' AND createdby != ''' + @userId_Id + ''' AND (';
    
    SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + LegalEntityId + ''', companyLegalUnit) > 0', ' OR ') FROM #temp_checker_requests_filters;
    
    SET @sql_query_count = @sql_query_count + ')';
    
    IF @Request_Module IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND (';
        SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + ModuleName + ''', module) > 0', ' OR ') FROM #temp_checker_requests_filters;
        SET @sql_query = @sql_query + ')';
		
		SET @sql_query_count = @sql_query_count + '
            AND (';
        SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + ModuleName + ''', module) > 0', ' OR ') FROM #temp_checker_requests_filters;
        SET @sql_query_count = @sql_query_count + ')';
    END;

    IF @Request_Action IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND (';
        SELECT @sql_query = @sql_query + STRING_AGG('CHARINDEX(''' + ActionName + ''', action) > 0', ' OR ') FROM #temp_checker_requests_filters;
        SET @sql_query = @sql_query + ')';
		
        SET @sql_query_count = @sql_query_count + '
            AND (';
        SELECT @sql_query_count = @sql_query_count + STRING_AGG('CHARINDEX(''' + ActionName + ''', action) > 0', ' OR ') FROM #temp_checker_requests_filters;
        SET @sql_query_count = @sql_query_count + ')';
    END;

    IF @submittedDate IS NOT NULL
    BEGIN
        SET @sql_query = @sql_query + '
            AND CONVERT(DATE, createdts) = ''' + CONVERT(VARCHAR, @submittedDate, 23) + '''';
		SET @sql_query_count = @sql_query_count + '
            AND CONVERT(DATE, createdts) = ''' + CONVERT(VARCHAR, @submittedDate, 23) + '''';
    END;
	
	EXEC sp_executesql @sql_query_count;
    
    SET @sql_query = @sql_query + ' ORDER BY createdts ASC OFFSET ' + CAST(@_pageOffset AS NVARCHAR) + ' ROWS FETCH NEXT ' + CAST(@_pageSize AS NVARCHAR) + ' ROWS ONLY;';
 
    EXEC sp_executesql @sql_query;
 
    DROP TABLE IF EXISTS #temp_checker_requests_filters;
END;
GO

CREATE VIEW [${dbxschemaname}].[get_mc_requestshistory_view](
    [action],
    [createdby],
    [status],
    [companyLegalUnit],
    [approvalPermissionName],
	[requestId],
	[module],
	[createdDate],
	[createdTs],
	[reason],
	[checkedBy],
	[actionedDate]
    ) AS
    SELECT 
        [mcconfig].[action] AS [action],
        [ar].[createdby] AS [createdby],
        UPPER([ar].[status]) AS [status],
        [ar].[companyLegalUnit] AS [companyLegalUnit],
        [mcconfig].[approvalPermissionName] AS [approvalPermissionName],
		[ar].[requestId] AS [requestId],
		[ar].[module] AS [module],
		CAST([ar].[createdts] AS DATE) AS [createdDate],
		[ar].[createdts] AS [createdTs],
		[ar].[reason] AS [reason],
		[ar].[checkedBy] AS [checkedBy],
		[ar].[checkedts] AS [actionedDate]
		
    FROM
    [${dbxschemaname}].[approvalrequests] as [ar] JOIN [${dbxschemaname}].[makercheckerconfig] as [mcconfig]
		ON ([ar].[expAPIOperationName] = [mcconfig].[expAPIOperationName] AND [ar].[companyLegalUnit] = [mcconfig].[companyLegalUnit]);
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[get_mc_moduleactionname_view];
GO


CREATE VIEW [${dbxschemaname}].[get_mc_moduleactionname_view] AS
    SELECT DISTINCT
	    mcconfig.module AS moduleid,
        mcconfig.action AS actionid,
        mcmodule.name AS modulename,
        mcaction.name AS actionname,
        mcmodule.language_code AS languagecode
    FROM
        [${dbxschemaname}].makercheckerconfig mcconfig
        LEFT JOIN [${dbxschemaname}].mcmoduletext mcmodule ON mcmodule.id = mcconfig.module
        JOIN [${dbxschemaname}].mcactiontext mcaction ON mcaction.id = mcconfig.action;
		
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_makercheckerconfig_proc];
GO
CREATE PROCEDURE [dbxdb].[update_makercheckerconfig_proc]
    @_input NVARCHAR(MAX)
AS
BEGIN
    DECLARE @_idx INT = 0;
    DECLARE @_maxIdx INT;
    DECLARE @_companyLegalUnit VARCHAR(50);
    DECLARE @_isApprovalRequired SMALLINT;
    DECLARE @_id INT;
    DECLARE @_mc_data NVARCHAR(MAX);

   	SET @_mc_data = JSON_QUERY(@_input, '$.updates');
   	SELECT @_maxIdx = COUNT(*) FROM OPENJSON(@_mc_data, '$');

    WHILE @_idx < @_maxIdx
    BEGIN
        SELECT @_companyLegalUnit = JSON_VALUE(@_mc_data, CONCAT('$[', @_idx, '].companyLegalUnit'));
        SELECT @_isApprovalRequired = JSON_VALUE(@_mc_data, CONCAT('$[', @_idx, '].isApprovalRequired'));
        SELECT @_id = JSON_VALUE(@_mc_data, CONCAT('$[', @_idx, '].id'));
        SELECT @_mc_data, @_maxIdx, @_companyLegalUnit, @_isApprovalRequired, @_id  ;
        
        UPDATE [${dbxschemaname}].[makercheckerconfig]
        SET isApprovalRequired = @_isApprovalRequired
        WHERE id = @_id AND companyLegalUnit = @_companyLegalUnit;

        SET @_idx = @_idx + 1;
    END;
END;
GO

ALTER PROCEDURE [${dbxschemaname}].[checkforpendingrequests_proc](
    @module NVARCHAR(1024),
    @action NVARCHAR(1024),
    @record NVARCHAR(1024),
    @CompanyLegalUnitId NVARCHAR(1024)
)
AS
BEGIN
	  DECLARE @i INT = 1;
    DECLARE @num_records INT;
    DECLARE @current_module NVARCHAR(50);
    DECLARE @current_action NVARCHAR(50);
    DECLARE @current_legalUnit NVARCHAR(50);
    DECLARE @current_record NVARCHAR(255);
    DECLARE @requestDetails NVARCHAR(1024);
    DECLARE @temp_requestId NVARCHAR(50);
 
    SET @num_records = (SELECT MAX(LEN(@module) - LEN(REPLACE(@module, '|', ''))) + 1);
 
    IF OBJECT_ID('tempdb..#temp_checkforpendingrequests_results') IS NOT NULL
        DROP TABLE #temp_checkforpendingrequests_results;
 
    CREATE TABLE #temp_checkforpendingrequests_results (
        requestId NVARCHAR(50),
        requestDetails NVARCHAR(1024)
    );
 
    WHILE @i <= @num_records
    BEGIN
		SET @temp_requestId = null;
        SET @current_module = PARSENAME(REPLACE(@module, '|', '.'), @i);
        SET @current_action = PARSENAME(REPLACE(@action, '|', '.'), @i);
        SET @current_legalUnit = PARSENAME(REPLACE(@CompanyLegalUnitId, '|', '.'), @i);
        SET @current_record = PARSENAME(REPLACE(@record, '|', '.'), @i);
 
        SET @requestDetails = CONCAT(@current_module, '_', @current_action, '_', @current_legalUnit, '_', @current_record);
 
        SELECT TOP 1 @temp_requestId = requestId
        FROM approvalrequests
        WHERE
            recordId COLLATE SQL_Latin1_General_CP1_CI_AS = @current_record AND
            module COLLATE SQL_Latin1_General_CP1_CI_AS = @current_module AND
            companyLegalUnit COLLATE SQL_Latin1_General_CP1_CI_AS = @current_legalUnit AND
            action COLLATE SQL_Latin1_General_CP1_CI_AS = @current_action AND
            status COLLATE SQL_Latin1_General_CP1_CI_AS = 'SID_PENDING';
 
        INSERT INTO #temp_checkforpendingrequests_results (requestId, requestDetails) VALUES (@temp_requestId, @requestDetails);
        SET @i = @i + 1;
    END;
    SELECT * FROM #temp_checkforpendingrequests_results;
    IF OBJECT_ID('tempdb..#temp_checkforpendingrequests_results') IS NOT NULL
        DROP TABLE #temp_checkforpendingrequests_results;
END
GO

CREATE TABLE [${dbxschemaname}].[tf_records_module_configurations] (
    [module_id] VARCHAR(255) PRIMARY KEY,
    [allowed_fields] VARCHAR(MAX)
);

CREATE TABLE [${dbxschemaname}].[tf_records] (
    [record_id] INT PRIMARY KEY IDENTITY(1000000,1),
    [module_id] VARCHAR(255),
    [record_data] VARCHAR(MAX),
    [created_by] VARCHAR(255),
    [updated_by] VARCHAR(255),
    [created_date] DATETIME2 DEFAULT GETDATE(),
    [updated_date] DATETIME2 DEFAULT GETDATE(),
    CONSTRAINT [tf_fk_module_id] FOREIGN KEY ([module_id]) REFERENCES [${dbxschemaname}].[tf_records_module_configurations]([module_id]),
    CONSTRAINT [tf_record_data_validate] CHECK (ISJSON([record_data]) > 0)
);

CREATE TABLE [${dbxschemaname}].[ld_records_module_configurations] (
    [module_id] VARCHAR(255) PRIMARY KEY,
    [allowed_fields] VARCHAR(MAX)
);

CREATE TABLE [${dbxschemaname}].[ld_records] (
    [record_id] INT PRIMARY KEY IDENTITY(10000000,1),
    [module_id] VARCHAR(255),
    [record_data] VARCHAR(MAX),
    [created_by] VARCHAR(255),
    [updated_by] VARCHAR(255),
    [created_date] DATETIME2 DEFAULT GETDATE(),
    [updated_date] DATETIME2 DEFAULT GETDATE(),
    CONSTRAINT [ld_fk_module_id] FOREIGN KEY ([module_id]) REFERENCES [${dbxschemaname}].[ld_records_module_configurations]([module_id]),
    CONSTRAINT [ld_record_data_validate] CHECK (ISJSON([record_data]) > 0)
);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_new_composite_request_in_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[create_new_composite_request_in_approvalqueue_proc](
    @_contractCifMatrixIdsJSON nvarchar(4000),
    @_assocRequestId varchar(64),
    @_confirmationNumber varchar(128),
    @_featureActionId varchar(64),
    @_accountId varchar(45),
    @_createdBy varchar(32),
    @_comments varchar(512),
    @_additionalMetaJSON nvarchar(max)
)
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @currentSchema NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId BIT;
	DECLARE @isActiveRulesFetch BIT;
	DECLARE @sub_requestIds NVARCHAR(MAX);
	DECLARE @sqlStmt NVARCHAR(MAX);
	DECLARE @isCifLevelFilter BIT;
	DECLARE @sub_contractIds NVARCHAR(MAX);
	DECLARE @sub_cifIds NVARCHAR(MAX);
	DECLARE @sub_noOfContractIds INT;
	DECLARE @sub_contractIdIndex INT;
	DECLARE @sub_contractJSON NVARCHAR(MAX);
	DECLARE @sub_contractId NVARCHAR(MAX);
	DECLARE @sub_cifsJSON NVARCHAR(MAX);
	DECLARE @sub_noOfCifIds INT;
	DECLARE @sub_cifIndex INT;
	DECLARE @sub_cifJSON NVARCHAR(MAX);
	DECLARE @sub_cifId NVARCHAR(MAX);
	DECLARE @sub_requestId NVARCHAR(MAX);
	DECLARE @sub_isGroupMatrix BIT;
	DECLARE @sqlSubStmt NVARCHAR(MAX);
	DECLARE @noOfRequestIds INT;
	DECLARE @sub_requestIdIndex INT;
	DECLARE @singleQuote nvarchar(1);
    SET @singleQuote = '''';


    DECLARE @requestIds varchar(MAX) = '';
    DECLARE @assocRequestId nvarchar(64);

    IF @_assocRequestId IS NULL OR @_assocRequestId = ''
        BEGIN
            SET @assocRequestId = NEWID();
        END
    ELSE
        BEGIN
            SET @assocRequestId = CAST(@_assocRequestId AS uniqueidentifier);
        END

--DECLARE @noOfContractIds int = JSON_LENGTH(@_contractCifMatrixIdsJSON);
    DECLARE @noOfContractIds int = 0;
    SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMatrixIdsJSON);

    DECLARE @contractIdIndex int = 0;

    WHILE @contractIdIndex < @noOfContractIds
        BEGIN
            --DECLARE @contractId nvarchar(50) = JSON_VALUE(@contractJSON, '$.contractId');
            DECLARE @contractJSON NVARCHAR(MAX);
            SELECT @contractJSON = [value]
            FROM (SELECT [value], ROW_NUMBER() OVER (ORDER BY [key]) AS RowNum
                  FROM OPENJSON(@_contractCifMatrixIdsJSON)) AS subquery
            WHERE RowNum = @contractIdIndex + 1;

            SET @contractIdIndex = @contractIdIndex + 1;
            --DECLARE @contractId nvarchar(50) = JSON_VALUE(@contractJSON, '$.contractId');

            DECLARE @contractId NVARCHAR(MAX);
            SELECT @contractId = value FROM OPENJSON(@contractJSON) WHERE [key] = 'contractId';

            IF @contractId IS NULL OR @contractId = ''
                BEGIN
                    -- return sql error signal
                    RETURN;
                END;

            --DECLARE @cifsJSON nvarchar(MAX) = JSON_VALUE(@contractJSON, '$.cifs');
            DECLARE @cifsJSON NVARCHAR(MAX);
            SELECT @cifsJSON = value FROM OPENJSON(@contractJSON) WHERE [key] = 'cifs';
            --DECLARE @noOfCifIds int = JSON_LENGTH(@cifsJSON);
            DECLARE @noOfCifIds int;
            SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);
            DECLARE @cifIndex int = 0;

            WHILE @cifIndex < @noOfCifIds
                BEGIN
                    --DECLARE @cifJSON nvarchar(MAX) = JSON_VALUE(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
                    DECLARE @cifJSON NVARCHAR(MAX);
                    SELECT @cifJSON = [value]
                    FROM (SELECT [value], ROW_NUMBER() OVER (ORDER BY [key]) AS RowNum
                          FROM OPENJSON(@cifsJSON)) AS subquery
                    WHERE RowNum = @cifIndex + 1;

                    SET @cifIndex += 1;

                    --DECLARE @cifId nvarchar(50) = JSON_VALUE(@cifJSON, '$.id');
                    DECLARE @cifId NVARCHAR(MAX);
                    SELECT @cifId = value FROM OPENJSON(@cifJSON) WHERE [key] = 'id';

                    IF @cifId IS NULL OR @cifId = ''
                        BEGIN
                            -- return sql error signal
                            RETURN;
                        END;

                    DECLARE @companyLegalUnit nvarchar(50);
                    DECLARE @isGroupMatrix bit;

                    SET @companyLegalUnit = (SELECT DISTINCT companyLegalUnit
                                             FROM [${dbxschemaname}].contractcorecustomers
                                             WHERE contractId = @contractId
                                               AND coreCustomerId = @cifId);

                    SET @isGroupMatrix = (SELECT DISTINCT isGroupLevel
                                          FROM [${dbxschemaname}].approvalmode
                                          WHERE contractId = @contractId
                                            AND coreCustomerId = @cifId);

                    DECLARE @matrixIdsJSON NVARCHAR(MAX);
                    SELECT @matrixIdsJSON = value FROM OPENJSON(@cifJSON) WHERE [key] = 'matrixIds';

                    DECLARE @noOfMatrixIds int;
                    SELECT @noOfMatrixIds = COUNT(*) FROM OPENJSON(@matrixIdsJSON);
                    IF @noOfMatrixIds != 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].bbrequest
                            (assocRequestId, transactionId, featureActionId, createdby, companyId, requiredSets, createdts,
                             receivedSets, status, accountId, isGroupMatrix, additionalMeta, companyLegalUnit)
                            VALUES (@assocRequestId, @_confirmationNumber, @_featureActionId, @_createdBy,
                                    CONCAT(@contractId, '_', @cifId), @noOfMatrixIds, CURRENT_TIMESTAMP, 0, 'Pending', @_accountId,
                                    @isGroupMatrix, @_additionalMetaJSON, @companyLegalUnit);

                            DECLARE @requestId INT;
                            SELECT TOP 1 @requestId = requestId
                            FROM bbrequest
                            WHERE assocRequestId = @assocRequestId
                            ORDER BY requestId DESC;

                            IF @requestIds = ''
                                SET @requestIds = CAST(@requestId AS varchar(50));
                            ELSE
                                SET @requestIds = CONCAT(@requestIds, ',', CAST(@requestId AS varchar(50)));

                            DECLARE @matrixIdIndex int = 0;

                            WHILE @matrixIdIndex < @noOfMatrixIds
                                BEGIN
                                    DECLARE @matrixId NVARCHAR(MAX);
                                    SELECT @matrixId = JSON_VALUE(@matrixIdsJSON, '$[' + CAST(@matrixIdIndex AS VARCHAR(10)) + '].id');
                                    SET @matrixIdIndex += 1;

                                    INSERT INTO [${dbxschemaname}].requestapprovalmatrix
                                    (approvalMatrixId, requestId, receivedApprovals, isGroupRule, createdby)
                                    VALUES (@matrixId, @requestId, 0, @isGroupMatrix, @_createdBy);

                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            DECLARE @signatoryGroupRequestMatrixId nvarchar(50);
                                            set @signatoryGroupRequestMatrixId = newid();
                                            INSERT INTO [${dbxschemaname}].signatorygrouprequestmatrix
                                            (signatoryGroupRequestMatrixId, requestId, approvalMatrixId,
                                             groupList, groupRuleValue, pendingGroupList, createdby)
                                            SELECT NEWID(),
                                                   @requestId,
                                                   @matrixId,
                                                   sgm.groupList,
                                                   sgm.groupRule,
                                                   sgm.groupList,
                                                   @_createdBy
                                            FROM [${dbxschemaname}].signatorygroupmatrix AS sgm
                                            WHERE sgm.approvalMatrixId = @matrixId;
                                        END;
                                END;

                            INSERT INTO [${dbxschemaname}].bbactedrequest
                            (requestId, assocRequestId, companyId, [status],
                             comments, createdby, [action], companyLegalUnit)
                            VALUES (@requestId, @assocRequestId, CONCAT(@contractId, '_', @cifId),
                                    'Pending', @_comments, @_createdBy, 'Pending', @companyLegalUnit);
                        END;
                END;
        END;

    	DECLARE @_requestIds NVARCHAR(MAX) = @requestIds;
	DECLARE @_isAssociationId NVARCHAR(2) = '0';
	DECLARE @_contractCifMapJSON NVARCHAR(MAX) = '';
	DECLARE @_isActiveRulesFetch NVARCHAR(2) = '0';

    IF @_isAssociationId IS NULL OR @_isAssociationId = ''
		SET @isAssocId = 0;
	ELSE
		SET @isAssocId = CAST(@_isAssociationId AS INT);
	IF @_isActiveRulesFetch IS NULL OR @_isActiveRulesFetch = ''
		SET @isActiveRulesFetch = 0;
	ELSE
		SET @isActiveRulesFetch = CAST(@_isActiveRulesFetch AS BIT);
	SET @sub_requestIds = @_requestIds;

	IF @isAssocId = 1
	BEGIN
		SET @sub_requestIds = CONCAT('''', REPLACE(@_requestIds, ',', ''','''), '''');
		SET @sqlStmt = CONCAT('SELECT STRING_AGG(DISTINCT requestId, '','') WITHIN GROUP (ORDER BY requestId) INTO @sub_requestIds FROM bbrequest WHERE assocRequestId IN (', @sub_requestIds, ')');
		EXEC (@sqlStmt);
	END;

	IF @sub_requestIds IS NULL
	BEGIN
		SET @sub_requestIds = '';
	END;

	SET @isCifLevelFilter = 1;
	SET @sub_contractIds = '';
	SET @sub_cifIds = '';

	IF @_contractCifMapJSON IS NULL OR @_contractCifMapJSON = ''
	BEGIN
		SET @isCifLevelFilter = 0;
	END
	ELSE
	BEGIN
		SELECT @sub_noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMapJSON);
		SET @sub_contractIdIndex = 0;
		WHILE @sub_contractIdIndex < @sub_noOfContractIds
		BEGIN
SELECT @sub_contractJSON = [value]
FROM OPENJSON(@_contractCifMapJSON)
WHERE [key] = @sub_contractIdIndex;
			SET @sub_contractIdIndex = @sub_contractIdIndex + 1;
            SET @sub_contractId = JSON_VALUE(@sub_contractJSON, '$.contractId');

            IF @sub_contractId IS NULL OR @sub_contractId = ''
			BEGIN
				RETURN;
			END;

			IF @sub_contractIds = ''
			BEGIN
				SET @sub_contractIds = @sub_contractId;
			END
			ELSE
			BEGIN
				SET @sub_contractIds = CONCAT(@sub_contractIds, ',', @sub_contractId);
			END;
            SET @sub_cifsJSON = JSON_QUERY(@sub_contractJSON, '$.cifs');

            SELECT @sub_noOfCifIds = COUNT(*) FROM OPENJSON(@sub_cifsJSON);
			SET @sub_cifIndex = 0;

			WHILE @sub_cifIndex < @sub_noOfCifIds
			BEGIN
				SET @sub_cifJSON = JSON_QUERY(@sub_cifsJSON, CONCAT('$[', @sub_cifIndex, ']'));
				SELECT @sub_cifJSON = [value] FROM OPENJSON(@sub_cifsJSON) WHERE [key] = @sub_cifIndex;
				SET @sub_cifIndex = @sub_cifIndex + 1;
                SET @sub_cifId = JSON_VALUE(@sub_cifJSON, '$.id');

				IF @sub_cifId IS NULL OR @sub_cifId = ''
				BEGIN
					RETURN;
				END;

				IF @sub_cifIds = ''
				BEGIN
					SET @sub_cifIds = @sub_cifId;
				END
				ELSE
				BEGIN
					SET @sub_cifIds = CONCAT(@sub_cifIds, ',', @sub_cifId);
				END;
			END;
		END;

		SET @sub_contractIds = CONCAT('''', REPLACE(@sub_contractIds, ',', ''','''), '''');
		SET @sub_cifIds = CONCAT('''', REPLACE(@sub_cifIds, ',', ''','''), '''');
	END;

	SET @sqlStmt = '';
	SET @noOfRequestIds = LEN(@sub_requestIds) - LEN(REPLACE(@sub_requestIds, ',', '')) + 1;
	SET @sub_requestIdIndex = 1;

	WHILE @sub_requestIdIndex <= @noOfRequestIds
	BEGIN
		SET @sub_requestId = SUBSTRING(@sub_requestIds, CHARINDEX(',', @sub_requestIds, 1) + 1, LEN(@sub_requestIds));

		IF @sub_requestIdIndex = @noOfRequestIds
		BEGIN
			SET @sub_requestId = @sub_requestIds;
		END;

		SET @sub_requestIdIndex = @sub_requestIdIndex + 1;
		SET @sub_isGroupMatrix = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @sub_requestId);
		SET @sqlSubStmt = '';

		IF @sub_isGroupMatrix = 0
		BEGIN
			SET @sqlSubStmt = CONCAT('(SELECT br.requestId, br.assocRequestId, br.transactionId, am.contractId, am.coreCustomerId, br.featureActionId, br.accountId, br.status, br.createdby, br.createdts, br.requiredSets, br.receivedSets, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ram.receivedApprovals, CAST(br.isGroupMatrix AS UNSIGNED) AS isGroupMatrix, NULL AS groupList, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, GROUP_CONCAT(cam.customerId) AS approverIds, br.additionalMeta,',
                        ' (SELECT GROUP_CONCAT(' , @singleQuote , '{"','cam.customerId : c.userName','}',') FROM customer AS c WHERE c.id = cam.customerId) AS approverUserNames
                            FROM bbrequest AS br LEFT JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                            LEFT JOIN customerapprovalmatrix AS cam ON cam.approvalMatrixId = ram.approvalMatrixId LEFT JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId WHERE br.requestId = ',@singleQuote, '@sub_requestId', @singleQuote,
                        'IF(@isCifLevelFilter = 1, CONCAT(', @singleQuote, 'AND am.contractId IN (', '@sub_contractIds', ') AND am.coreCustomerId IN (', '@sub_cifIds', '))', ')', ' GROUP BY br.requestId, ram.approvalMatrixId)');
		END
		ELSE
		BEGIN
			SET @sqlSubStmt = concat('
    SELECT
        br.requestId,
        br.assocRequestId,
        br.transactionId,
        am.contractId,
        am.coreCustomerId,
        br.featureActionId,
        br.accountId,
        br.status,
        br.createdby,
        br.createdts,
        br.requiredSets,
        br.receivedSets,
        ram.approvalMatrixId,
        am.limitTypeId,
        am.approvalruleId,
        ram.receivedApprovals,
        br.additionalMeta,
        sgrm.groupList,
        sgrm.pendingGroupList,
        sgrm.groupRuleValue,
        CAST(br.isGroupMatrix AS INT) AS isGroupMatrix,
        CAST(sgrm.isApproved AS INT) AS isGroupRuleApproved,' +
                                             '(SELECT STUFF((SELECT '','' + CAST(cust.customerid AS VARCHAR)
                                                 FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                 WHERE CHARINDEX('','' + CAST(cust.signatoryGroupId AS VARCHAR) + '','', (REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', ''''))) > 0
                                                 FOR XML PATH('''')), 1, 1, '''')
                                                 ) AS approverIds,',
                                             '(SELECT STUFF((SELECT '','' + ''{"'' + CAST(cust.customerid AS VARCHAR) + ''":"'' + c.userName + ''"}''
                                                    FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                    INNER JOIN ' ,@currentSchema,'.customer AS c ON c.id = cust.customerid
                                                    WHERE CHARINDEX(cust.signatoryGroupId, REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', '''')) > 0
                                                    FOR XML PATH('''')), 1, 1, ''''))
                                                  AS approverUserNames ',
                                             'FROM ' ,@currentSchema,'. bbrequest AS br  ',
                                             'LEFT JOIN ' ,@currentSchema,'.requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                                             LEFT JOIN  ' ,@currentSchema,'.signatorygrouprequestmatrix AS sgrm ON (ram.approvalMatrixId = sgrm.approvalMatrixId AND sgrm.requestId = br.requestId)
                                             LEFT JOIN ' ,@currentSchema,'.approvalmatrix AS am ON am.id = ram.approvalMatrixId
                                         WHERE br.requestId = ',
                                            @singleQuote, @sub_requestId, @singleQuote)
		END
		IF @sqlStmt = ''
		BEGIN
			SET @sqlStmt = @sqlSubStmt;
		END
		ELSE
		BEGIN
			SET @sqlStmt = CONCAT(@sqlStmt, ' UNION ALL ', @sqlSubStmt);
		END;
	END;

	EXEC (@sqlStmt);

END;
GO

ALTER PROCEDURE [${dbxschemaname}].user_account_default_actions_create_proc  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_groupId nvarchar(50),
   @_legalEntityId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureActionId varchar(255) = N''

      DECLARE
         @actionslist varchar(max) = N''

      DECLARE
         @limitId varchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @accountId varchar(255) = N''

      DECLARE
         @actualLimitId varchar(255) = N''
         
      DECLARE
         @serviceDefinitionId varchar(255) = N''
         
      DECLARE
         @serviceType varchar(255) = N''
         
      DECLARE
         @validFIActions varchar(max) = N''
         
      DECLARE
         @validServiceDefinitionActions varchar(max) = N''
         
      DECLARE
         @validGroupActions varchar(max) = N''
         
      DECLARE
         @validActionsList varchar(max) = N''
         
      DECLARE
         @groupId varchar(255) = N''
         
       DECLARE
         @featureId varchar(255) = N''

      DECLARE
         @limitvalue varchar(255) = N''

      DECLARE
         @id varchar(255) = N''
         
         
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
      [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
      [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
      [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
     
      DECLARE accounts_1 CURSOR LOCAL
      FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
      [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
      [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
      [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
      
      

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (						
            SELECT String_agg(CAST([${dbxschemaname}].[customeraccounts].[Account_id] AS nvarchar(max)), ',') FROM [${dbxschemaname}].[customeraccounts] WHERE 
                               [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
                               [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
                               [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId
             )
								
    SET @serviceDefinitionId = (SELECT [${dbxschemaname}].[contract].[servicedefinitionId] from [${dbxschemaname}].[contract] WHERE [${dbxschemaname}].[contract].[id] = @_contractId)
    SET @serviceType = (SELECT [${dbxschemaname}].[servicedefinition].[serviceType] from [${dbxschemaname}].[servicedefinition] WHERE [${dbxschemaname}].[servicedefinition].[id] = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
                        WHERE [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = @serviceDefinitionId AND [${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = '1')
														
    SET @validFIActions =  (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId)
																
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                        
    SET @_groupId =  (SELECT [${dbxschemaname}].groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  
											
    SET @validGroupActions =  (SELECT String_agg(CAST([${dbxschemaname}].groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
												
    SET @validActionsList =  (SELECT String_agg(CAST([${dbxschemaname}].contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
	DECLARE actions CURSOR LOCAL
      FOR (select  id from [${dbxschemaname}].[featureaction] where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].[featureaction].[isAccountLevel] = '1' or [${dbxschemaname}].[featureaction].[isAccountLevel] = 'true' ) and [${dbxschemaname}].[featureaction].[companyLegalUnit] = @_legalEntityId);
         
   	DECLARE actions_1 CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].[featureaction] where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].[featureaction].[isAccountLevel] = '1' or [${dbxschemaname}].[featureaction].[isAccountLevel] = 'true' ) and [${dbxschemaname}].[featureaction].[companyLegalUnit] = @_legalEntityId);

    OPEN accounts_1     
    FETCH NEXT FROM accounts_1 into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
    OPEN actions_1
    FETCH NEXT FROM actions_1 into @featureActionId
	
      WHILE (@@FETCH_STATUS=0)
          BEGIN					
          SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId and featureaction.companyLegalUnit = @_legalEntityId);
          BEGIN
          SET @id =(SELECT left(newid(), 50))
          INSERT [${dbxschemaname}].[accountlevelactionlimit]([${dbxschemaname}].[accountlevelactionlimit].[id], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[contractId], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[coreCustomerId], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[accountId], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[featureId], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[Actionid], 
                                        [${dbxschemaname}].[accountlevelactionlimit].[companyLegalUnit])
                                        VALUES (@id,@_contractId,@_coreCustomerId,@accountId,@featureId,@featureActionId,@_legalEntityId);
           END
           SET @actionslist = @featureActionId + N',' + @actionslist
           FETCH NEXT FROM actions_1 into @featureActionId
           CONTINUE
           END
           CLOSE actions_1
           DEALLOCATE actions_1
           FETCH NEXT FROM accounts_1 into @accountId
           CONTINUE
           END
           CLOSE accounts_1
           DEALLOCATE accounts_1 
         
    OPEN accounts     
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
    OPEN actions
    FETCH NEXT FROM actions into @featureActionId
	
    
      WHILE (@@FETCH_STATUS=0)
          BEGIN					
          SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId and featureaction.companyLegalUnit = @_legalEntityId);
          SET @entryStatus = 0
		  DECLARE limits CURSOR LOCAL
      FOR (select LimitType_id from [${dbxschemaname}].[actionlimit] where Action_id = @featureActionId and companyLegalUnit = @_legalEntityId);
    OPEN limits
    FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN				
            SET @limitvalue = (SELECT [${dbxschemaname}].contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE [${dbxschemaname}].contractactionlimit.actionId = @featureActionId AND 
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId AND
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId)
			IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
               SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
               SET @id = (SELECT left(newid(), 50))
               INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].[customeraction].[id], 
                                             [${dbxschemaname}].[customeraction].[RoleType_id], 
                                             [${dbxschemaname}].[customeraction].[Customer_id], 
                                             [${dbxschemaname}].[customeraction].[contractId], 
                                             [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                             [${dbxschemaname}].[customeraction].[featureId], 
                                             [${dbxschemaname}].[customeraction].[Action_id], 
                                             [${dbxschemaname}].[customeraction].[Account_id], 
                                             [${dbxschemaname}].[customeraction].[isAllowed], 
                                             [${dbxschemaname}].[customeraction].[LimitType_id], 
                                             [${dbxschemaname}].[customeraction].[value],
                                             [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00,@_legalEntityId)
               SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
             IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
             SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
             SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                           [${dbxschemaname}].[customeraction].[RoleType_id], 
                                           [${dbxschemaname}].[customeraction].[Customer_id], 
                                           [${dbxschemaname}].[customeraction].[contractId], 
                                           [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                           [${dbxschemaname}].[customeraction].[featureId],
                                           [${dbxschemaname}].[customeraction].[Action_id], 
                                           [${dbxschemaname}].[customeraction].[Account_id], 
                                           [${dbxschemaname}].[customeraction].[isAllowed], 
                                           [${dbxschemaname}].[customeraction].[LimitType_id], 
                                           [${dbxschemaname}].[customeraction].[value],
                                           [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00,@_legalEntityId)
             SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
             END
             END
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value],
                                      [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue,@_legalEntityId)
                                      
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value],
                                      [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue,@_legalEntityId) 
                                                                 
          SET @entryStatus = 1
          FETCH NEXT FROM limits into @limitId
          CONTINUE
          END
          CLOSE limits
          DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
          SET @id =(SELECT left(newid(), 50))
          INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                        [${dbxschemaname}].[customeraction].[RoleType_id], 
                                        [${dbxschemaname}].[customeraction].[Customer_id], 
                                        [${dbxschemaname}].[customeraction].[contractId], 
                                        [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                        [${dbxschemaname}].[customeraction].[featureId], 
                                        [${dbxschemaname}].[customeraction].[Action_id], 
                                        [${dbxschemaname}].[customeraction].[Account_id], 
                                        [${dbxschemaname}].[customeraction].[isAllowed],
                                        [${dbxschemaname}].[customeraction].[companyLegalUnit])
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@_legalEntityId);
           END
    SET @actionslist = @featureActionId + N',' + @actionslist
           FETCH NEXT FROM actions into @featureActionId
           CONTINUE
           END
           CLOSE actions
           DEALLOCATE actions
           FETCH NEXT FROM accounts into @accountId
           CONTINUE
           END
           CLOSE accounts
           DEALLOCATE accounts
END;
GO

ALTER TABLE [${dbxschemaname}].[approvalrequests] ALTER COLUMN [reason] nvarchar(max);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
    @_contractId      NVARCHAR(128) ,
    @_coreCustomerId    NVARCHAR(128) ,
    @_featureActionId   NVARCHAR(128) ,
    @_accountIds       NVARCHAR(128) ,
    @_limitTypeId       NVARCHAR(128) ,
    @_limitValuesJSON   NVARCHAR(MAX)
AS
BEGIN
    DECLARE @accountIds NVARCHAR(max);
    DECLARE @isAccountLevelUpdate NVARCHAR(128);
    DECLARE @isAccountLevelFeature NVARCHAR(128);
    DECLARE @isGroupMatrix NVARCHAR(128);
    DECLARE @currency NVARCHAR(max);
    DECLARE @templateIdsForForceDelete NVARCHAR(max);
    DECLARE @templateIds NVARCHAR(max);
    DECLARE @sqlStmt NVARCHAR(max);
    DECLARE @limitsLength NVARCHAR(128);
    DECLARE @currentLimit NVARCHAR(max);
    DECLARE @limitIndex NVARCHAR(128);
    DECLARE @approvalRuleId NVARCHAR(128);
    DECLARE @lowerLimit NVARCHAR(128);
    DECLARE @upperLimit NVARCHAR(128);
    DECLARE @groupList NVARCHAR(max);
    DECLARE @groupRule NVARCHAR(max);
    DECLARE @approvers NVARCHAR(max);
    DECLARE @noOfApprovers NVARCHAR(128);
    DECLARE @currentUserId NVARCHAR(128);
    DECLARE @approverIndex NVARCHAR(128);
    DECLARE @noOfAccountIds NVARCHAR(128);
    DECLARE @lastTemplateId NVARCHAR(128);
    DECLARE @accountId NVARCHAR(128);
    DECLARE @accountIndex NVARCHAR(128);
    DECLARE @_requestMatrixDataJSON NVARCHAR(MAX);
    DECLARE @companyLegalUnit NVARCHAR(128);
    DECLARE @approvalMode NVARCHAR(128);
    declare @singleQuote nvarchar(1);

    SET @singleQuote = '''';
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId and companyLegalUnit = @companyLegalUnit);

    --SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId);

    SET @isGroupMatrix = (SELECT DISTINCT([approvalmode].[isGroupLevel]) FROM [${dbxschemaname}].[approvalmode] WHERE coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    --SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    IF @_accountIds IS NULL OR @_accountIds = '' OR @_accountIds ='[]'
        BEGIN
            SET @isAccountLevelUpdate = 0;
            SET @accountIds = (SELECT STRING_AGG (CAST([contractaccounts].[accountId] AS NVARCHAR(max)),',') FROM [${dbxschemaname}].[contractaccounts] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
        END
    ELSE
        BEGIN
            SET @accountIds = @_accountIds;
        END
    IF @isAccountLevelFeature = 0
        BEGIN
            -- customer-level feature action rule is being added/updated, hence, no account ids are associated
            SET @accountIds = '';
        END
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    --SET @currency = (SELECT DISTINCT([approvalmatrixtemplate].[currency]) FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId and companyLegalUnit = @companyLegalUnit);
    SET @currency = (SELECT TOP 1 [approvalmatrixtemplate].[currency]
                     FROM [${dbxschemaname}].[approvalmatrixtemplate]
                     WHERE contractId = @_contractId
                       AND coreCustomerId = @_coreCustomerId
                       AND companyLegalUnit = @companyLegalUnit
                     GROUP BY [approvalmatrixtemplate].[currency]
                     ORDER BY COUNT(*) DESC);

    set @approvalMode = (select isGroupLevel from ${dbxschemaname}.approvalmode where contractId = @_contractId
                       AND coreCustomerId = @_coreCustomerId);

    IF @isAccountLevelUpdate = 0
        BEGIN
            -- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
            -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
            SET @templateIdsForForceDelete = (SELECT STRING_AGG(CAST([approvalmatrixtemplate].[id] AS NVARCHAR(max)),',')  FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId);
            IF @templateIdsForForceDelete IS NOT NULL
                BEGIN
                    SET @templateIds = CONCAT('''', REPLACE(@templateIdsForForceDelete, ',', ''','''), '''');
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[customerapprovalmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[signatorygroupmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    DELETE FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
                END
        END
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
        END
    ELSE
        BEGIN
            --select @_limitTypeId as cuid;
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId AND [${dbxschemaname}].FIND_IN_SET(accountId, @accountIds)<>0;
        END
    SET @limitsLength = (select COUNT(*) FROM OPENJSON(@_limitValuesJSON));
    --select 'LimitsLength' + @limitsLength;
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    WHILE 1=1
        BEGIN
            IF @limitIndex = @limitsLength
                BEGIN
                    BREAK;
                END
            ELSE
                BEGIN

                    SET @currentLimit = (SELECT json.value FROM OPENJSON(@_limitValuesJSON) as json where json.[key] =@limitIndex) ;
                    --SET @currentLimit = JSON_VALUE(@_limitValuesJSON, CONCAT('$[', @limitIndex, ']'));
                    --select 'Current Limit:  ' + @currentLimit;
                    SET @limitIndex = @limitIndex + 1;
                    --select '@currentLimit' + @currentLimit;
                    --SET @approvalRuleId = (select json.value from OPENJSON(@currentLimit) as json where json.[key] = 'groupRule');
                    SET @approvalRuleId = REPLACE(JSON_VALUE(@currentLimit, '$.approvalruleId'),'"','');
                    SET @lowerLimit = JSON_VALUE(@currentLimit, '$.lowerlimit');
                    SET @upperLimit = JSON_VALUE(@currentLimit, '$.upperlimit');
                    SET @groupList = REPLACE(JSON_VALUE(@currentLimit, '$.groupList'),'"','');
                    SET @groupRule = REPLACE(JSON_VALUE(@currentLimit, '$.groupRule'),'"','');
                    SET @_requestMatrixDataJSON = (SELECT approvers FROM OPENJSON(@currentLimit) WITH (approvers nvarchar(MAX) AS JSON));

--                     select @currentLimit as currentLimit;
--                     IF (@approvalMode = 0)
--                     BEGIN
--                         SET @approvalRuleId =  (SELECT DISTINCT([approvalrule].[id]) FROM [${dbxschemaname}].[approvalrule] WHERE numberOfApprovals =  REPLACE(REPLACE(@groupRule, '[', ''), ']', ''));
--                     END
                    IF (@approvalMode = 0)
                    BEGIN
                        SET @approvalRuleId =  (SELECT JSON_VALUE(@currentLimit, '$.approvalruleId'));
                    END
                    SET @approvers = JSON_VALUE(@currentLimit, '$.approvers');
                    -- error handling
                    -- if lowerLimit or upperLimit is null, then return an error signal
                    -- if group mode, then if groupList or groupRule is null, then return error signal
                    -- if user-based mode, then if approvalRuleId or approvers are null, then return error

                    IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@approvers IS NOT NULL AND REPLACE(@approvers,'"','') = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
                    IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].[approvalmatrixtemplate](contractId, coreCustomerId, actionId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency, companyLegalUnit)
                            VALUES(@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId, @isGroupMatrix, (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency, @companyLegalUnit);
                            SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrixtemplate] ) ;
                            IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                BEGIN
                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            INSERT INTO [${dbxschemaname}].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
                                            VALUES(@lastTemplateId, @groupList, @groupRule);
                                        END
                                    ELSE
                                        BEGIN
                                            SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
                                            SET @currentUserId = '';
                                            SET @approverIndex = 0;
                                            WHILE (1=1)
                                                BEGIN
                                                    IF @approverIndex = @noOfApprovers
                                                        BEGIN
                                                            BREAK;
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            --SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                            SET @currentUserId = (SELECT JSON_VALUE(value, '$.approverId') FROM OPENJSON(@_requestMatrixDataJSON) WHERE [key] = @approverIndex);
                                                            
                                                            INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId) VALUES (@currentUserId, @lastTemplateId);
                                                            SET @approverIndex = @approverIndex + 1;
                                                        END
                                                END
                                        END
                                END
                        END
                    IF @isAccountLevelFeature = 0
                        BEGIN
                            exec ${dbxschemaname}.approvalmatrix_ids_checkncleanup_for_safedelete_proc
                                 _contractId,
                                 _coreCustomerId,
                                 _featureActionId,
                                 NULL;
                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                BEGIN
                                    --select @approvalRuleId as cuid;
                                    INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency,companyLegalUnit)
                                    VALUES (
                                               CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                               @_featureActionId, NULL, @_limitTypeId, @isGroupMatrix, (@approvalRuleId),
                                               CAST(@lowerLimit AS DECIMAL(20, 2)), CAST(@upperLimit AS DECIMAL(20, 2)), @currency,
                                               @companyLegalUnit);
                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                        BEGIN
                                            IF @isGroupMatrix = 1
                                                BEGIN
                                                    INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList, groupRule)
                                                    VALUES(@lastTemplateId, @groupList, (@groupRule));
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@approvers));
                                                    SET @currentUserId = '';
                                                    SET @approverIndex = 0;
                                                    WHILE (1=1)
                                                        BEGIN
                                                            IF @approverIndex = @noOfApprovers
                                                                BEGIN
                                                                    BREAK;
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @currentUserId = (SELECT JSON_VALUE(value, '$.approverId') FROM OPENJSON(@_requestMatrixDataJSON) WHERE [key] = @approverIndex);
                                                                    --select @currentUserId as customerId, @lastTemplateId as approvalMatrixId;
                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId) VALUES (@currentUserId, @lastTemplateId);
                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId) VALUES (@currentUserId, @lastTemplateId);
                                                                    SET @approverIndex = @approverIndex + 1;
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                    ELSE
                        BEGIN
                            -- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
                            SET @noOfAccountIds = LEN(@accountIds) - LEN(REPLACE(@accountIds, ',', '')) + 1;
                            SET @accountIndex = 0;
                            WHILE (1=1)
                                BEGIN
                                    SET @accountIndex = @accountIndex + 1;
                                    IF @accountIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK;
                                        END
                                    ELSE
                                        BEGIN
                                            -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                                BEGIN
                                                    --select '@accountId'+@accountId;
                                                    BEGIN
                                                        INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId,
                                                                                                        limitTypeId, isGroupMatrix, approvalruleId, lowerLimit,
                                                                                                        upperLimit, currency, companyLegalUnit)
                                                        VALUES (CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                                                @_featureActionId, @accountId, @_limitTypeId, @isGroupMatrix,
                                                                @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20, 2)),
                                                                CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);
                                                        --select @approvalRuleId as cuid;

                                                    END
                                                    --COMMIT;
                                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                                    --select '@lastTemplateId......' + @lastTemplateId;
                                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                                        BEGIN
                                                            IF @isGroupMatrix = 1
                                                                BEGIN
                                                                    set @sqlStmt = CONCAT('INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList,groupRule)
										VALUES(',@singleQuote,@lastTemplateId,@singleQuote,',',@singleQuote, @groupList,@singleQuote, ',',@singleQuote,@groupRule,@singleQuote,')');
                                                                    --select '@sqlStmt' + @sqlStmt;
                                                                    EXEC(@sqlStmt);
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON));
                                                                    SET @currentUserId = '';
                                                                    SET @approverIndex = 0;

                                                                    WHILE (1=1)
                                                                        BEGIN
                                                                            IF @approverIndex = @noOfApprovers
                                                                                BEGIN
                                                                                    BREAK;
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    SET @currentUserId = (SELECT JSON_VALUE(value, '$.approverId') FROM OPENJSON(@_requestMatrixDataJSON) WHERE [key] = @approverIndex);
                                                                                    --select @currentUserId as cuid;
                                                                                    --select @currentUserId as currentUserId;
                                                                                    --REPLACE(JSON_VALUE(JSON_VALUE(@_requestMatrixDataJSON, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                                    --select @currentUserId as customerId, @lastTemplateId as approvalMatrixId;
                                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId) VALUES (@currentUserId, @lastTemplateId);
                                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId) VALUES (@currentUserId, @lastTemplateId);
                                                                                    SET @approverIndex = @approverIndex + 1;
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
    @_contractId      NVARCHAR(128) ,
    @_coreCustomerId    NVARCHAR(128) ,
    @_featureActionId   NVARCHAR(128) ,
    @_accountIds       NVARCHAR(128) ,
    @_limitTypeId       NVARCHAR(128) ,
    @_limitValuesJSON   NVARCHAR(MAX)
AS
BEGIN
    DECLARE @accountIds NVARCHAR(max);
    DECLARE @isAccountLevelUpdate NVARCHAR(128);
    DECLARE @isAccountLevelFeature NVARCHAR(128);
    DECLARE @isGroupMatrix NVARCHAR(128);
    DECLARE @currency NVARCHAR(max);
    DECLARE @templateIdsForForceDelete NVARCHAR(max);
    DECLARE @templateIds NVARCHAR(max);
    DECLARE @sqlStmt NVARCHAR(max);
    DECLARE @limitsLength NVARCHAR(128);
    DECLARE @currentLimit NVARCHAR(128);
    DECLARE @limitIndex NVARCHAR(128);
    DECLARE @approvalRuleId NVARCHAR(128);
    DECLARE @lowerLimit NVARCHAR(128);
    DECLARE @upperLimit NVARCHAR(128);
    DECLARE @groupList NVARCHAR(max);
    DECLARE @groupRule NVARCHAR(max);
    DECLARE @approvers NVARCHAR(max);
    DECLARE @noOfApprovers NVARCHAR(128);
    DECLARE @currentUserId NVARCHAR(128);
    DECLARE @approverIndex NVARCHAR(128);
    DECLARE @noOfAccountIds NVARCHAR(128);
    DECLARE @lastTemplateId NVARCHAR(128);
    DECLARE @accountId NVARCHAR(128);
    DECLARE @accountIndex NVARCHAR(128);
    DECLARE @_requestMatrixDataJSON NVARCHAR(MAX);
    DECLARE @companyLegalUnit NVARCHAR(128);
    declare @singleQuote nvarchar(1);

    SET @singleQuote = '''';
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId and companyLegalUnit = @companyLegalUnit);
    --SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId);
    SET @isGroupMatrix = (SELECT DISTINCT([approvalmode].[isGroupLevel]) FROM [${dbxschemaname}].[approvalmode] WHERE coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    --SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    IF @_accountIds IS NULL OR @_accountIds = '' OR @_accountIds ='[]'
        BEGIN
            SET @isAccountLevelUpdate = 0;
            SET @accountIds = (SELECT STRING_AGG (CAST([contractaccounts].[accountId] AS NVARCHAR(max)),',') FROM [${dbxschemaname}].[contractaccounts] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
        END
    ELSE
        BEGIN
            SET @accountIds = @_accountIds;
        END
    IF @isAccountLevelFeature = 0
        BEGIN
            -- customer-level feature action rule is being added/updated, hence, no account ids are associated
            SET @accountIds = '';
        END
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    SET @currency = (SELECT DISTINCT([approvalmatrixtemplate].[currency]) FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId and companyLegalUnit = @companyLegalUnit);
    IF @isAccountLevelUpdate = 0
        BEGIN
            -- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
            -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
            SET @templateIdsForForceDelete = (SELECT STRING_AGG(CAST([approvalmatrixtemplate].[id] AS NVARCHAR(max)),',')  FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId);
            IF @templateIdsForForceDelete IS NOT NULL
                BEGIN
                    SET @templateIds = CONCAT('''', REPLACE(@templateIdsForForceDelete, ',', ''','''), '''');
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[customerapprovalmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[signatorygroupmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    DELETE FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
                END
        END
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
        END
    ELSE
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId AND [${dbxschemaname}].FIND_IN_SET(accountId, @accountIds)<>0;
        END
    SET @limitsLength = (select COUNT(*) FROM OPENJSON(@_limitValuesJSON));
    --select 'LimitsLength' + @limitsLength;
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    WHILE 1=1
        BEGIN
            IF @limitIndex = @limitsLength
                BEGIN
                    BREAK;
                END
            ELSE
                BEGIN

                    SET @currentLimit = (SELECT json.value FROM OPENJSON(@_limitValuesJSON) as json where json.[key] =@limitIndex) ;
                    --SET @currentLimit = JSON_VALUE(@_limitValuesJSON, CONCAT('$[', @limitIndex, ']'));
                    --select 'Current Limit:  ' + @currentLimit;
                    SET @limitIndex = @limitIndex + 1;
                    --select '@currentLimit' + @currentLimit;
                    --SET @approvalRuleId = (select json.value from OPENJSON(@currentLimit) as json where json.[key] = 'groupRule');
                    SET @approvalRuleId = REPLACE(JSON_VALUE(@currentLimit, '$.approvalruleId'),'"','');
                    SET @lowerLimit = JSON_VALUE(@currentLimit, '$.lowerlimit');
                    SET @upperLimit = JSON_VALUE(@currentLimit, '$.upperlimit');
                    SET @groupList = REPLACE(JSON_VALUE(@currentLimit, '$.groupList'),'"','');
                    SET @groupRule = REPLACE(JSON_VALUE(@currentLimit, '$.groupRule'),'"','');
                    SET @approvalRuleId =  (SELECT DISTINCT([approvalrule].[id]) FROM [${dbxschemaname}].[approvalrule] WHERE numberOfApprovals =  REPLACE(REPLACE(@groupRule, '[', ''), ']', ''));
                    SET @approvers = JSON_VALUE(@currentLimit, '$.approvers');
                    -- error handling
                    -- if lowerLimit or upperLimit is null, then return an error signal
                    -- if group mode, then if groupList or groupRule is null, then return error signal
                    -- if user-based mode, then if approvalRuleId or approvers are null, then return error

                    IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@approvers IS NOT NULL AND REPLACE(@approvers,'"','') = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
                    IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].[approvalmatrixtemplate](contractId, coreCustomerId, actionId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency, companyLegalUnit)
                            VALUES(@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId, @isGroupMatrix, (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency, @companyLegalUnit);
                            SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrixtemplate] ) ;
                            IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                BEGIN
                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            INSERT INTO [${dbxschemaname}].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
                                            VALUES(@lastTemplateId, @groupList, @groupRule);
                                        END
                                    ELSE
                                        BEGIN
                                            SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
                                            SET @currentUserId = '';
                                            SET @approverIndex = 0;
                                            WHILE (1=1)
                                                BEGIN
                                                    IF @approverIndex = @noOfApprovers
                                                        BEGIN
                                                            BREAK;
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                            INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId)
                                                            VALUES (@currentUserId, @lastTemplateId);
                                                            SET @approverIndex = @approverIndex + 1;
                                                        END
                                                END
                                        END
                                END
                        END
                    IF @isAccountLevelFeature = 0
                        BEGIN
                            exec ${dbxschemaname}.approvalmatrix_ids_checkncleanup_for_safedelete_proc
                                 _contractId,
                                 _coreCustomerId,
                                 _featureActionId,
                                 NULL;
                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                BEGIN
                                    INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency,companyLegalUnit)
                                    VALUES (
                                               CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                               @_featureActionId, NULL, @_limitTypeId, @isGroupMatrix, (@approvalRuleId),
                                               CAST(@lowerLimit AS DECIMAL(20, 2)), CAST(@upperLimit AS DECIMAL(20, 2)), @currency,
                                               @companyLegalUnit);
                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                        BEGIN
                                            IF @isGroupMatrix = 1
                                                BEGIN
                                                    INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList, groupRule)
                                                    VALUES(@lastTemplateId, @groupList, (@groupRule));
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@approvers));
                                                    SET @currentUserId = '';
                                                    SET @approverIndex = 0;
                                                    WHILE (1=1)
                                                        BEGIN
                                                            IF @approverIndex = @noOfApprovers
                                                                BEGIN
                                                                    BREAK;
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                    SET @approverIndex = @approverIndex + 1;
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                    ELSE
                        BEGIN
                            -- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
                            SET @noOfAccountIds = LEN(@accountIds) - LEN(REPLACE(@accountIds, ',', '')) + 1;
                            SET @accountIndex = 0;
                            WHILE (1=1)
                                BEGIN
                                    SET @accountIndex = @accountIndex + 1;
                                    IF @accountIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK;
                                        END
                                    ELSE
                                        BEGIN
                                            SET @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@accountIds, ',', @accountIndex), ',', -1);
                                            -- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId);
                                            -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                                BEGIN
                                                    --select '@accountId'+@accountId;
                                                    BEGIN
                                                        INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId,
                                                                                             limitTypeId, isGroupMatrix, approvalruleId, lowerLimit,
                                                                                             upperLimit, currency, companyLegalUnit)
                                                        VALUES (CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                                                @_featureActionId, @accountId, @_limitTypeId, @isGroupMatrix,
                                                                @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20, 2)),
                                                                CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);

                                                    END
                                                    --COMMIT;
                                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                                    --select '@lastTemplateId......' + @lastTemplateId;
                                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                                        BEGIN
                                                            IF @isGroupMatrix = 1
                                                                BEGIN
                                                                    set @sqlStmt = CONCAT('INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList,groupRule)
										VALUES(',@singleQuote,@lastTemplateId,@singleQuote,',',@singleQuote, @groupList,@singleQuote, ',',@singleQuote,@groupRule,@singleQuote,')');
                                                                    --select '@sqlStmt' + @sqlStmt;
                                                                    EXEC(@sqlStmt);
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON));
                                                                    SET @currentUserId = '';
                                                                    SET @approverIndex = 0;
                                                                    WHILE (1=1)
                                                                        BEGIN
                                                                            IF @approverIndex = @noOfApprovers
                                                                                BEGIN
                                                                                    BREAK;
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                                    SET @approverIndex = @approverIndex + 1;
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[systemroles_permission_proc]  
   @_roleIds varchar(500)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 SELECT distinct
        [p].[id] AS id,
        [p].[Name] AS name,
		[rp].[companyLegalUnit]  AS companyLegalUnit,
        [p].[Status_id] AS status,
        [p].[PermissionValue] AS PermissionValue,
        [p].[isComposite] AS isComposite,
        [rp].[Role_id] AS Role_id,
		[p].[softdeleteflag] AS softdeleteflag
    FROM
    (rolepermission rp
	JOIN permission p ON ([p].[id] = [rp].[Permission_id])
    JOIN role r ON ([r].[id] = [rp].[Role_id] AND [r].[companyLegalUnit] = [rp].[companyLegalUnit]))
    WHERE [p].[Status_id]='SID_ACTIVE' AND [r].[Status_id]='SID_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) > 0 ORDER BY [id];
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[GetEnrollCustomerViewDetailsInfo];
GO
CREATE PROCEDURE [${dbxschemaname}].[GetEnrollCustomerViewDetailsInfo]
	@_servicedefId nvarchar(max),
   	@_roleId nvarchar(max),
   	@_cif nvarchar(max),
   	@_companyLegalUnit nvarchar(max)
AS
BEGIN
	SET  XACT_ABORT  ON
	SET  NOCOUNT  ON
 
	SELECT s.id, s.name serviceDefinitionName, s.serviceType, m.description serviceTypeName 
	FROM [${dbxschemaname}].servicedefinition s, [${dbxschemaname}].membergrouptype m 
	WHERE m.id = s.serviceType AND [${dbxschemaname}].FIND_IN_SET(s.id, @_servicedefId) > 0 ;

	SELECT id, name roleName FROM [${dbxschemaname}].membergroup mg
	WHERE [${dbxschemaname}].FIND_IN_SET(mg.id, @_roleId) >0;

	SELECT coreCustomerId, accountId ,statusDesc accountStatus, ownerType FROM [${dbxschemaname}].contractaccounts c 
	WHERE [${dbxschemaname}].FIND_IN_SET(c.coreCustomerId, @_cif) >0;
    
	SELECT coreCustomerId, contractId, signatoryGroupName FROM [${dbxschemaname}].signatorygroup sig 
	WHERE [${dbxschemaname}].FIND_IN_SET(sig.coreCustomerId, @_cif) >0;
	
    SELECT f.id feature, fd.displayName featureName, f.Status_id featureStatus, a.Action_id action ,
    fa.status actionStatus, a.displayDescription actionDesc, a.displayName actionName, 
    fa.Type_id actionType ,fa.companyLegalUnit 
	FROM [${dbxschemaname}].feature f, [${dbxschemaname}].featureaction fa, 
    [${dbxschemaname}].actiondisplaynamedescription a , [${dbxschemaname}].featuredisplaynamedescription fd
	WHERE f.id = fa.Feature_id AND f.id = fd.Feature_id AND fa.id = a.Action_id 
	AND f.companyLegalUnit = fa.companyLegalUnit AND fa.companyLegalUnit = a.companyLegalUnit 
	AND a.companyLegalUnit = fd.companyLegalUnit
	AND a.Locale_id ='en-US'
	AND fd.Locale_id ='en-US'
	AND f.companyLegalUnit =@_companyLegalUnit;
	
	SELECT limitGroupId, displayName limitGroupName FROM [${dbxschemaname}].limitgroupdisplaynamedescription l WHERE l.localeId ='en-US';
 
END
GO

/* SQL Scripts for DigitalWealth - Favorite Customer */
DROP TABLE IF EXISTS [${dbxschemaname}].inf_wlth_favorite_customer;
CREATE TABLE [${dbxschemaname}].[inf_wlth_favorite_customer] (
  [id] int IDENTITY,
  [customerId] nvarchar(50) NOT NULL,
  [favoriteCustomerId] varchar(2000),
  [createdby] varchar(50) DEFAULT NULL,
  [createdts] datetime NOT NULL DEFAULT GETDATE(),
  [modifiedby] varchar(50) DEFAULT NULL,
  [lastmodifiedts] datetime NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT FK_favorite_customer_id FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
)
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc] @_customerId nvarchar(50),
    @_transactionIds nvarchar(max),
    @_requestIds nvarchar(max),
    @_featureactionlist nvarchar(max)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @combinedIds nvarchar(max)
    DECLARE @alreadyApprovedIds nvarchar(max)
    DECLARE @companyId nvarchar(max)
    DECLARE @customerMatrixIds nvarchar(max)
    DECLARE @customerGroupIds nvarchar(max)
    DECLARE @approvalRequestIds nvarchar(max)
    DECLARE @features nvarchar(max)
    DECLARE @monetaryActions nvarchar(max)
    DECLARE @groupIds nvarchar(max)
    DECLARE @strLen nvarchar(20)
    DECLARE @reqIds nvarchar(max)
    DECLARE @SubStrLen nvarchar(20)
--     DECLARE @logmessage nvarchar(max)
--     DECLARE @drop_temp nvarchar(100)

--     SET @logmessage =
--             'Customer id = ' + @_customerId + ' Featurelist = ' + @_featureactionlist + ' TransactionList = ' +
--             @_transactionIds + ' Requestids = ' + @_requestIds;
    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc',
    -- @AdditionalInfo = 'Incoming Arguemnts ' + @logmessage;

    SET @combinedIds = (select String_agg(customer.id, ',')
                        from [${dbxschemaname}].customer
where [${dbxschemaname}].customer.combinedUserId = @_customerId)
IF @combinedIds is NULL
SET @combinedIds = @_customerId;
ELSE
        SET @combinedIds = (@_customerId + ',' + @combinedIds);

    IF (@combinedIds IS NULL OR @combinedIds = '')
        SET @combinedIds = '';
ELSE
        SET @combinedIds = @combinedIds;

    IF (@_transactionIds IS NULL OR @_transactionIds = '')
        SET @_transactionIds = '';
ELSE
        SET @_transactionIds = @_transactionIds;

    IF (@_requestIds IS NULL OR @_requestIds = '')
        SET @_requestIds = '';
ELSE
        SET @_requestIds = @_requestIds;

    IF (@_featureactionlist IS NULL OR @_featureactionlist = '')
        GOTO MAINLABEL$leave

    SET @customerMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)), ',')
                              FROM [${dbxschemaname}].customerapprovalmatrix
WHERE customerapprovalmatrix.customerId IN
(select VALUE from STRING_SPLIT(@combinedIds, ',')))
IF @customerMatrixIds IS NULL
SET @customerMatrixIds = ''

SET @customerGroupIds = (SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)), ',')
FROM [${dbxschemaname}].customersignatorygroup
WHERE customersignatorygroup.customerId IN
(select VALUE FROM STRING_SPLIT(@combinedIds, ',')));
IF @customerGroupIds IS NULL
        SET @customerGroupIds = ''

    SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)), ',')
                               FROM [${dbxschemaname}].bbactedrequest
WHERE bbactedrequest.createdby IN (SELECT VALUE FROM STRING_SPLIT(@combinedIds, ','))
AND bbactedrequest.action != 'Pending'
AND bbactedrequest.softdeleteflag = 0);

IF @alreadyApprovedIds IS NULL
        SET @alreadyApprovedIds = '';

    IF @customerMatrixIds = ''
        SET @approvalRequestIds = '';
ELSE
        SET @approvalRequestIds =
                (SELECT String_agg(CAST(RQAMX.requestId as nvarchar(max)), ',')
                 FROM [${dbxschemaname}].requestapprovalmatrix AS RQAMX
INNER JOIN [${dbxschemaname}].approvalmatrix AS APMX ON (RQAMX.approvalMatrixId = APMX.id and APMX.id IN
(SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ',')))
INNER JOIN [${dbxschemaname}].approvalrule AS APRL ON (APMX.approvalruleId = APRL.id)
WHERE RQAMX.isGroupRule = 0
AND RQAMX.approvalMatrixId IN (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ','))
AND RQAMX.requestId NOT IN (SELECT VALUE FROM STRING_SPLIT(@alreadyApprovedIds, ','))
AND ((APRL.numberOfApprovals = -1 AND
RQAMX.receivedApprovals < (SELECT COUNT(DISTINCT (customerapprovalmatrix.customerId))
FROM [${dbxschemaname}].customerapprovalmatrix
WHERE customerapprovalmatrix.approvalMatrixId = RQAMX.approvalMatrixId)) OR
(APRL.numberOfApprovals != -1 AND RQAMX.receivedApprovals < APRL.numberOfApprovals)));

IF @approvalRequestIds IS NULL
        SET @approvalRequestIds = '';

    -- GROUP BASED APPROVAL Trxns
    SET @groupIds = @customerGroupIds;

    DECLARE @CurrentGroupId NVARCHAR(MAX);
    DECLARE @PendingGroupList NVARCHAR(MAX);
    SET @PendingGroupList = '';

    do_this:
    WHILE 1 = 1 BEGIN
        SET @strLen = LEN(@groupIds);
        SET @CurrentGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1);
IF @CurrentGroupId != ''
BEGIN
                SET @CurrentGroupId = '%' + @CurrentGroupId + '%';

                IF @PendingGroupList = ''
                    SET @PendingGroupList =
                            ' signatorygrouprequestmatrix.pendingGroupList LIKE ''' + @CurrentGroupId + ''''
                ELSE
                    SET @PendingGroupList =
                            @PendingGroupList + ' OR signatorygrouprequestmatrix.pendingGroupList LIKE ''' +
                            @CurrentGroupId + ''''
END

        SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
        IF LEN(@groupIds) <= 0
BEGIN
                BREAK;
END
END;

    IF @PendingGroupList != ''
BEGIN
            DECLARE @sql_stmt NVARCHAR(MAX);
            SET @sql_stmt = ' SELECT @reqIds_out = (String_agg(CAST(signatorygrouprequestmatrix.requestId as nvarchar(max)) , '',''))
            FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE signatorygrouprequestmatrix.isApproved = 0 AND ( ' +
                            @PendingGroupList + ' ) ;';
EXECUTE sp_executesql @sql_stmt, N'@reqIds_out NVARCHAR(MAX) OUTPUT', @reqIds_out = @reqIds OUTPUT
END

    IF @reqIds IS NULL
        SET @reqIds = ''

    IF (@approvalRequestIds = '' OR @approvalRequestIds IS NULL)
        SET @approvalRequestIds = @reqIds;
ELSE
BEGIN
            IF @reqIds != ''
                SET @approvalRequestIds = CONCAT(@approvalRequestIds, ',', @reqIds);
END

    SET @approvalRequestIds = [${dbxschemaname}].DISTINCT_VALUE(@approvalRequestIds); -- Remove duplicates
DECLARE @requestIds nvarchar(max)
    DECLARE @companyRequestIds nvarchar(max)
    DECLARE @query nvarchar(max)

    SET @query = ''

    IF (@_transactionIds != '')
BEGIN
            SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                             FROM [${dbxschemaname}].featureaction
WHERE featureaction.id IN (select value FROM STRING_SPLIT(@_featureactionlist, ',')))

IF @features IS NULL
SET @features = ''

IF @features != ''
SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
FROM [${dbxschemaname}].featureaction
WHERE featureaction.Feature_id IN
(select value FROM STRING_SPLIT(@features, ',')))

IF @monetaryActions IS NULL
SET @monetaryActions = ''

IF @monetaryActions != ''
SET @query = ' bbrequest.transactionId IN ( select VALUE from STRING_SPLIT(''' + @_transactionIds + ''', '','')) AND
				               bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions +
''', '','')); '
END
ELSE
BEGIN
            IF @_requestIds = ''
BEGIN

                    SET @companyId = (SELECT String_agg(CAST(concat(contractcustomers.contractId, '_',
                                                                    contractcustomers.coreCustomerId) as nvarchar(max)),
                                                        ',')
                                      FROM [${dbxschemaname}].contractcustomers
WHERE contractcustomers.customerId = @_customerId)

IF @companyId IS NULL
SET @companyId = '';
ELSE
                        SET @companyId = [${dbxschemaname}].DISTINCT_VALUE(@companyId);

SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                                     FROM [${dbxschemaname}].featureaction
WHERE featureaction.id IN
(select value FROM STRING_SPLIT(@_featureactionlist, ',')))

IF @features IS NULL
SET @features = ''

IF @features != ''
SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
FROM [${dbxschemaname}].featureaction
WHERE featureaction.Feature_id IN
(select value FROM STRING_SPLIT(@features, ',')))

IF @monetaryActions IS NULL
SET @monetaryActions = ''

IF @companyId != '' AND @monetaryActions != ''
SET @query = ' bbrequest.companyId IN (SELECT value FROM STRING_SPLIT(''' + @companyId + ''', '','')) and
                                       bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' +
@monetaryActions + ''', '','')); '
END
ELSE
BEGIN
                    SET @requestIds = @_requestIds
                    IF @requestIds != ''
                        SET @query = ' bbrequest.requestId IN ( select VALUE from STRING_SPLIT(''' + @requestIds +
                                     ''', '','')); '
END
END

    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Query Stmt = ' + @query;
    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Combined Ids = ' + @combinedIds;
    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Approval Req = ' + @approvalRequestIds;
    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Already Approved Req = ' + @alreadyApprovedIds;

    DECLARE @select_statement nvarchar(max)
    DECLARE @req_ids NVARCHAR(MAX)

    IF @query != ''
BEGIN
            SET @select_statement =
                    'select @reqout_ids = (string_agg(CAST(bbrequest.requestId as nvarchar(max)), '','')) from [${dbxschemaname}].bbrequest where ' +
                    @query;
EXECUTE sp_executesql @select_statement, N'@reqout_ids NVARCHAR(MAX) OUTPUT', @reqout_ids = @req_ids OUTPUT
END
ELSE
        GOTO MAINLABEL$leave

    IF @req_ids IS NULL
        GOTO MAINLABEL$leave

    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Req_ids = ' + @req_ids;

-- Option 1 , Use TABLE variable

    DECLARE @temp_customeraction TABLE
                                 (
                                     id         NVARCHAR(65),
                                     Account_id NVARCHAR(65),
                                     Action_id  NVARCHAR(255),
                                     companyId  NVARCHAR(255)
                                 );

INSERT INTO @temp_customeraction (id, Account_id, Action_id, companyId)
SELECT customeraction.id,
       customeraction.Account_id,
       customeraction.Action_id,
       (customeraction.contractId + '_' + customeraction.coreCustomerId) as companyId
from [${dbxschemaname}].customeraction
where customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(@combinedIds, ','))
  and customeraction.isAllowed = 1
  AND customeraction.softdeleteflag = 0;

-- Option 2, Use CTE

--  WITH temp_customeraction (id, Account_id, Action_id, companyId)
--	AS (
--		SELECT
--			customeraction.id,
--			customeraction.Account_id,
--			customeraction.Action_id,
--			(customeraction.contractId + ''_'' + customeraction.coreCustomerId) as companyId
--			from [${dbxschemaname}].customeraction
--		where
--			customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(''' + @combinedIds + ''', '','')) and
--			customeraction.isAllowed = 1 AND
--			customeraction.softdeleteflag = 0
--	)

SELECT bbrequest.requestId,
       bbrequest.transactionId,
       bbrequest.status,
       bbrequest.featureActionId,
       bbrequest.isGroupMatrix,
       bbrequest.additionalMeta,
	   bbrequest.createdts,

       (CASE
            WHEN (select COUNT(1) FROM STRING_SPLIT(@combinedIds, ',') where value = bbrequest.createdby) > 0
                THEN 'true'
            ELSE 'false'
           END) as amICreator,

       (CASE
            WHEN
                ((select COUNT(1) FROM STRING_SPLIT(@approvalRequestIds, ',') WHERE VALUE = bbrequest.requestId) >
                 0) AND
                ((select TOP 1 tca.id
                  from @temp_customeraction tca
                  -- Remove @ symbol, in case if we use CTE variable instead of TABLE variable here
                  where tca.Account_id = bbrequest.accountId
                    AND tca.Action_id = (select distinct(featureaction.approveFeatureAction)
                                         from [${dbxschemaname}].featureaction
           where featureaction.id = bbrequest.featureActionId
                                               and featureaction.approveFeatureAction IS NOT NULL)
           AND tca.companyId = bbrequest.companyId) IS NOT NULL)
                    THEN 'true'
                ELSE 'false'
END) as amIApprover,

           (CASE
                WHEN (select COUNT(1) FROM STRING_SPLIT(@alreadyApprovedIds, ',') WHERE VALUE = bbrequest.requestId) > 0
                    THEN 'true'
                ELSE 'false'
               END) as actedByMeAlready,

           (select count(DISTINCT (createdby))
            from [${dbxschemaname}].bbactedrequest
where bbactedrequest.action = 'Approved'
and bbactedrequest.requestId = bbrequest.requestId
AND bbactedrequest.softdeleteflag = 0)
as receivedApprovals,

null     as requiredApprovals

FROM [${dbxschemaname}].bbrequest
WHERE bbrequest.requestId IN (SELECT value FROM STRING_SPLIT(@req_ids, ','));

End
    MAINLABEL$leave:
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] DROP CONSTRAINT [FK_bulkpaymentrecord_featureActionId_idx];
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[bulkpaymentrecord]', 'companyLegalUnit';
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] DROP COLUMN companyLegalUnit;
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[bulkpaymentrecordmock]', 'companyLegalUnit';
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] DROP CONSTRAINT [FK_bulkpaymentrecordmock_featureActionId_idx];
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] DROP COLUMN companyLegalUnit;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[edit_customer_view_details_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[edit_customer_view_details_proc]
	@_customerId nvarchar(50),
	@_legalEntityId nvarchar(50)
AS
BEGIN
	 SELECT DISTINCT
	 customer.UserName AS userName,
     customer.FirstName AS firstName,
     customer.MiddleName AS middleName,
     customer.LastName AS lastName,
	 (ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS name,
	 customer.id AS customerId,
     customer.Ssn AS ssn,
     customer.createdts AS customerSince,
     customer.DateOfBirth AS dateOfBirth,
     customer.companyLegalUnit AS companyLegalUnit,
	 IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS customerTypeId,
	 IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS customerTypeName,
	 IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS customerTypeDescription,
	 customeraccounts.AccountName AS accountName,
     customeraccounts.Account_id AS accountId,
     customeraccounts.accountType AS accountType,
     customeraccounts.accountStatus AS accountStatus,
	 contract.id AS contractId,
     contract.name AS contractName,
     contract.servicedefinitionId AS serviceDefinitionId,
     servicedefinition.name AS serviceDefinitionName,
     membergrouptype.description AS serviceDefinitionType,
     contractcorecustomers.coreCustomerId AS coreCustomerId,
	 contractcorecustomers.coreCustomerName AS coreCustomerName,
     customergroup.Group_id AS userRole,
     membergroup.name AS userRoleName,
     contract.companyLegalUnit as legalEntityId,
	 IIF( ( ([customergroup].[Group_id] = null or [customergroup].[Group_id] =  '') AND ([membergroup].[name] = '' or [membergroup].[name] = null)),'false','true') AS isAssociated
	 FROM ((((([${dbxschemaname}].customer
		JOIN [${dbxschemaname}].backendidentifier
		ON ((customer.id = backendidentifier.Customer_id)))
		JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		JOIN [${dbxschemaname}].customeraccounts 
		ON ((customer.id = customeraccounts.Customer_id)))
		JOIN [${dbxschemaname}].contract
		ON ((customeraccounts.contractId = contract.id))
	    LEFT JOIN [${dbxschemaname}].[contractcustomers] 
		ON ((contractcustomers.contractId = contract.id and contractcustomers.customerId = @_customerId))
	    LEFT JOIN [${dbxschemaname}].[servicedefinition] 
		ON ((servicedefinition.id = contract.servicedefinitionId))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] 
		ON ((membergrouptype.id = servicedefinition.serviceType))
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] 
		ON ((contractcorecustomers.contractId = contract.id))
        LEFT JOIN [${dbxschemaname}].[customergroup] 
		ON ((customergroup.contractId = contract.id AND
            customergroup.coreCustomerId = contractcorecustomers.coreCustomerId AND
            customergroup.Customer_id = @_customerId))
        LEFT JOIN [${dbxschemaname}].[membergroup] 
		ON ((membergroup.id = customergroup.Group_id))))
		WHERE customer.id = @_customerId
		      AND customergroup.Customer_id = @_customerId
			  AND contractcustomers.customerId = @_customerId
              AND contract.companyLegalUnit = @_legalEntityId
              AND customeraccounts.companyLegalUnit = @_legalEntityId 
              AND customeraccounts.Customer_id = customer.id
              AND customeraccounts.contractId = contractcustomers.contractId
END;
GO

ALTER TABLE [${dbxschemaname}].[approvalrequests] ADD [viewDetailsResponse] varchar(max);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[edit_customer_view_details_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[edit_customer_view_details_proc]
	@_customerId nvarchar(50),
	@_legalEntityId nvarchar(50)
AS
BEGIN
	 declare @accountLockoutThreshold nvarchar(max)
     SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
	 SELECT DISTINCT
	 customer.UserName AS userName,
     customer.FirstName AS firstName,
     customer.MiddleName AS middleName,
     customer.LastName AS lastName,
	 (ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS name,
	 customer.id AS customerId,
     customer.Ssn AS ssn,
     customer.createdts AS customerSince,
     customer.DateOfBirth AS dateOfBirth,
     customer.companyLegalUnit AS companyLegalUnit,
	 CASE
		WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
	 ELSE CASE
		WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
	 ELSE customer.Status_id
	 END
	 END AS customerStatusId,
	 customerstatus.Description AS customerStatusName,
	 IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS customerTypeId,
	 IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS customerTypeName,
	 IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS customerTypeDescription,
	 customeraccounts.AccountName AS accountName,
     customeraccounts.Account_id AS accountId,
     customeraccounts.accountType AS accountType,
     customeraccounts.accountStatus AS accountStatus,
	 contract.id AS contractId,
     contract.name AS contractName,
     contract.servicedefinitionId AS serviceDefinitionId,
     servicedefinition.name AS serviceDefinitionName,
     membergrouptype.description AS serviceDefinitionType,
     contractcorecustomers.coreCustomerId AS coreCustomerId,
	 contractcorecustomers.coreCustomerName AS coreCustomerName,
     customergroup.Group_id AS userRole,
     membergroup.name AS userRoleName,
     contract.companyLegalUnit as legalEntityId,
	 IIF( ( ([customergroup].[Group_id] = null or [customergroup].[Group_id] =  '') AND ([membergroup].[name] = '' or [membergroup].[name] = null)),'false','true') AS isAssociated
	 FROM (((((([${dbxschemaname}].customer
		JOIN [${dbxschemaname}].backendidentifier
		ON ((customer.id = backendidentifier.Customer_id)))
		JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		JOIN [${dbxschemaname}].customeraccounts 
		ON ((customer.id = customeraccounts.Customer_id)))
		JOIN [${dbxschemaname}].contract
		ON ((customeraccounts.contractId = contract.id))
		LEFT JOIN [${dbxschemaname}].status AS customerstatus
		ON ((customer.Status_id = customerstatus.id)))
	    LEFT JOIN [${dbxschemaname}].[contractcustomers] 
		ON ((contractcustomers.contractId = contract.id and contractcustomers.customerId = @_customerId))
	    LEFT JOIN [${dbxschemaname}].[servicedefinition] 
		ON ((servicedefinition.id = contract.servicedefinitionId))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] 
		ON ((membergrouptype.id = servicedefinition.serviceType))
        LEFT JOIN [${dbxschemaname}].[contractcorecustomers] 
		ON ((contractcorecustomers.contractId = contract.id))
        LEFT JOIN [${dbxschemaname}].[customergroup] 
		ON ((customergroup.contractId = contract.id AND
            customergroup.coreCustomerId = contractcorecustomers.coreCustomerId AND
            customergroup.Customer_id = @_customerId))
        LEFT JOIN [${dbxschemaname}].[membergroup] 
		ON ((membergroup.id = customergroup.Group_id))))
		WHERE customer.id = @_customerId
		      AND customergroup.Customer_id = @_customerId
			  AND contractcustomers.customerId = @_customerId
              AND contract.companyLegalUnit = @_legalEntityId
              AND customeraccounts.companyLegalUnit = @_legalEntityId 
              AND customeraccounts.Customer_id = customer.id
              AND customeraccounts.contractId = contractcustomers.contractId
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_new_signatory_group_user_details];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_new_signatory_group_user_details] @customerIdList varchar(max),
                                                                  @coreCustomerIdInput varchar(50)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    SELECT customer.UserName as username,
           membergroup.Name as role,
           customer.id as customerId,
           CURRENT_TIMESTAMP as addedts,
           customer.FirstName + ' ' + customer.LastName as customerName
    FROM customer
             JOIN customergroup ON customer.id = customergroup.Customer_id
             JOIN membergroup ON customergroup.Group_id = membergroup.id
    WHERE customergroup.coreCustomerId = @coreCustomerIdInput
      AND customer.id IN (SELECT value FROM STRING_SPLIT(@customerIdList, ','))
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_create_approvalrule_req_view_details];
GO

CREATE PROCEDURE [${dbxschemaname}].get_create_approvalrule_req_view_details
@_contractId varchar(50),
@_coreCustomerId varchar(50),
@_actionId varchar(500),
@_groupIds varchar(1000),
@_legalEntityId varchar(50),
@_accountId varchar(50) 
AS
BEGIN

SET  XACT_ABORT  ON
SET  NOCOUNT  ON
	
select c.name contractName, s.name servicedefinitionName , ct.Name serviceType , 
c.id , cc.coreCustomerId , cc.coreCustomerName, cc.isPrimary 
from 
[${dbxschemaname}].contract c, 
[${dbxschemaname}].contractcorecustomers cc , 
[${dbxschemaname}].servicedefinition s , 
[${dbxschemaname}].customertype ct
where 
c.id = cc.contractId and 
c.servicedefinitionId = s.id and 
c.serviceType = ct.id and
cc.coreCustomerId = @_coreCustomerId and 
c.id = @_contractId ;

select distinct Feature_id , feature_name , feature_status_id , feature_Type_id , action_name , action_Type_id from [${dbxschemaname}].feature_actions_view fav where id= @_actionId and companyLegalUnit = @_legalEntityId;

select s.signatoryGroupId , s.signatoryGroupName from [${dbxschemaname}].signatorygroup  s where [${dbxschemaname}].find_in_set(signatoryGroupId, @_groupIds) >0  ;

select c.ownerType, c.accountName, a.displayName from
[${dbxschemaname}].contractaccounts c join [${dbxschemaname}].accounttype a on c.typeId = a.TypeID
where c.accountId = @_accountId;

END;

GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatory_viewdetails_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatory_viewdetails_proc](
	@contractId nvarchar(50),
	@coreCustomerId nvarchar(50),
	@customerIdList varchar(50),
	@signatoryGroupId varchar(50),
	@action varchar(50))

AS BEGIN

SET  XACT_ABORT  ON
SET  NOCOUNT  ON

		IF @signatoryGroupId IS NOT NULL
		BEGIN
		
		SELECT 	
			sg.signatoryGroupId, 
			sg.signatoryGroupName, 
			sg.signatoryGroupDescription,
			sg.coreCustomerId,
			c.id AS contractId,
			c.name AS contractName,
			c.serviceType,
			ct.name AS serviceTypeName,
			c.servicedefinitionid,
			s.name AS serviceName,
			cc.coreCustomerName 
			FROM [${dbxschemaname}].signatorygroup AS sg
				LEFT JOIN [${dbxschemaname}].contract AS c
				ON sg.contractId = c.id
				LEFT JOIN [${dbxschemaname}].servicedefinition AS s
				ON c.servicedefinitionid = s.id
				LEFT JOIN [${dbxschemaname}].contractcorecustomers AS cc
				ON cc.contractId = c.id
				LEFT JOIN [${dbxschemaname}].customertype AS ct
				ON ct.id = c.serviceType
			WHERE sg.signatoryGroupId = @signatoryGroupId;
			
		IF @customerIdList IS NULL
			SET @customerIdList = (SELECT String_agg(CAST(csg.customerId as nvarchar(max)), ',')
					FROM [${dbxschemaname}].customersignatorygroup AS csg
					WHERE csg.signatoryGroupId = @signatoryGroupId);
	
		IF @coreCustomerId IS NULL
			SET @coreCustomerId = (SELECT sg.coreCustomerId FROM [${dbxschemaname}].signatorygroup AS sg				
				WHERE sg.signatoryGroupId = @signatoryGroupId);
				
		IF @action = 'EDIT_SIGNATORY_GROUP'
			BEGIN
				
				DECLARE @approvedCustIdList nvarchar(50);
				
				
				SET @approvedCustIdList = (SELECT String_agg(CAST(csg.customerId as nvarchar(max)), ',')
					FROM [${dbxschemaname}].customersignatorygroup AS csg
					WHERE csg.signatoryGroupId = @signatoryGroupId);
			
				SELECT cg.Customer_id,c.UserName AS userName,
					mg.name AS role, 
					concat(c.FirstName,' ', c.LastName) AS customerName
				FROM [${dbxschemaname}].customergroup AS cg
				LEFT JOIN [${dbxschemaname}].membergroup AS mg
				ON cg.Group_id = mg.id
				LEFT JOIN [${dbxschemaname}].customer AS c
				ON cg.Customer_id = c.id
				WHERE cg.coreCustomerId = @coreCustomerId AND cg.Customer_id IN (SELECT value FROM STRING_SPLIT(@approvedCustIdList, ','));	
			END;		
			
		END;
		
		IF @signatoryGroupId IS NULL
		BEGIN
		
			SELECT 
				c.id AS contractId,
				c.name AS contractName,
				c.serviceType,
				ct.name AS serviceTypeName,
				c.servicedefinitionid,
				s.name AS serviceName,
				cc.coreCustomerName 
				FROM [${dbxschemaname}].contract AS c 
					LEFT JOIN [${dbxschemaname}].servicedefinition AS s
					ON c.servicedefinitionid = s.id
					LEFT JOIN [${dbxschemaname}].contractcorecustomers AS cc
					ON cc.contractId = c.id
					LEFT JOIN [${dbxschemaname}].customertype AS ct
					ON ct.id = c.serviceType
				WHERE c.id = @contractId AND 
					cc.coreCustomerId = @coreCustomerId;
		
		END;

	IF (@coreCustomerId IS NOT NULL AND @customerIdList IS NOT NULL)
		BEGIN		

		SELECT cg.Customer_id,c.UserName AS userName,
			mg.name AS role, 
			concat(c.FirstName,' ', c.LastName) AS customerName
		FROM [${dbxschemaname}].customergroup AS cg
		LEFT JOIN [${dbxschemaname}].membergroup AS mg
		ON cg.Group_id = mg.id
		LEFT JOIN [${dbxschemaname}].customer AS c
		ON cg.Customer_id = c.id
		WHERE cg.coreCustomerId = @coreCustomerId AND cg.Customer_id IN (SELECT value FROM STRING_SPLIT(@customerIdList, ','));				
	END;
	
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_requests_with_approvalmatrixinfo_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_requests_with_approvalmatrixinfo_proc]
	@_requestIds NVARCHAR(MAX),
	@_isAssociationId NVARCHAR(2),
	@_contractCifMapJSON NVARCHAR(MAX),
	@_isActiveRulesFetch NVARCHAR(2)
AS
BEGIN
	SET NOCOUNT ON;
    DECLARE @currentSchema NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId BIT;
	DECLARE @isActiveRulesFetch BIT;
	DECLARE @requestIds NVARCHAR(MAX);
	DECLARE @sqlStmt NVARCHAR(MAX);
	DECLARE @isCifLevelFilter BIT;
	DECLARE @contractIds NVARCHAR(MAX);
	DECLARE @cifIds NVARCHAR(MAX);
	DECLARE @noOfContractIds INT;
	DECLARE @contractIdIndex INT;
	DECLARE @contractJSON NVARCHAR(MAX);
	DECLARE @contractId NVARCHAR(MAX);
	DECLARE @cifsJSON NVARCHAR(MAX);
	DECLARE @noOfCifIds INT;
	DECLARE @cifIndex INT;
	DECLARE @cifJSON NVARCHAR(MAX);
	DECLARE @cifId NVARCHAR(MAX);
	DECLARE @requestId NVARCHAR(MAX);
	DECLARE @isGroupMatrix BIT;
	DECLARE @sqlSubStmt NVARCHAR(MAX);
	DECLARE @noOfRequestIds INT;
	DECLARE @requestIdIndex INT;
	DECLARE @singleQuote nvarchar(1);
    SET @singleQuote = '''';
	DECLARE @temp_requestIds TABLE (id varchar(max));

	IF @_isAssociationId IS NULL OR @_isAssociationId = ''
		SET @isAssocId = 0;
	ELSE
		SET @isAssocId = CAST(@_isAssociationId AS INT);
	IF @_isActiveRulesFetch IS NULL OR @_isActiveRulesFetch = ''
		SET @isActiveRulesFetch = 0;
	ELSE
		SET @isActiveRulesFetch = CAST(@_isActiveRulesFetch AS BIT);
	SET @requestIds = @_requestIds;

	IF @isAssocId = 1
	BEGIN
		--SET @requestIds = CONCAT('''', REPLACE(@_requestIds, ',', ''','''), '''');
		--SET @sqlStmt = CONCAT('SELECT STRING_AGG(DISTINCT requestId, '','') WITHIN GROUP (ORDER BY requestId) INTO @requestIds FROM bbrequest WHERE assocRequestId IN (', @requestIds, ')');
		--EXEC (@sqlStmt);
        INSERT INTO @temp_requestIds (id) (SELECT DISTINCT bbr.requestId FROM bbrequest bbr WHERE assocRequestId IN (SELECT value FROM STRING_SPLIT(@_requestIds, ',')));
        SELECT @requestIds = STRING_AGG(id, ',') FROM @temp_requestIds;
	END;

	IF @requestIds IS NULL
	BEGIN
		SET @requestIds = '';
	END;

	SET @isCifLevelFilter = 1;
	SET @contractIds = '';
	SET @cifIds = '';

	IF @_contractCifMapJSON IS NULL OR @_contractCifMapJSON = ''
	BEGIN
		SET @isCifLevelFilter = 0;
	END
	ELSE
	BEGIN
		SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMapJSON);
		SET @contractIdIndex = 0;
		WHILE @contractIdIndex < @noOfContractIds
		BEGIN
-- 			SELECT @contractJSON = [value] FROM OPENJSON(@_contractCifMapJSON) WITH (Value NVARCHAR(MAX) '$[' + CAST(@contractIdIndex AS NVARCHAR(10)) + ']');
--SET @contractJSON = JSON_QUERY(_contractCifMapJSON, CONCAT('$[', @contractIdIndex, ']'));
SELECT @contractJSON = [value]
FROM OPENJSON(@_contractCifMapJSON)
WHERE [key] = @contractIdIndex;
			SET @contractIdIndex = @contractIdIndex + 1;

			--SELECT @contractId = value FROM OPENJSON(@contractJSON) WITH (contractId NVARCHAR(MAX) '$.contractId');
            SET @contractId = JSON_VALUE(@contractJSON, '$.contractId');

            IF @contractId IS NULL OR @contractId = ''
			BEGIN
				-- return error signal
				RETURN;
			END;

			IF @contractIds = ''
			BEGIN
				SET @contractIds = @contractId;
			END
			ELSE
			BEGIN
				SET @contractIds = CONCAT(@contractIds, ',', @contractId);
			END;
			--SELECT @cifsJSON = [value] FROM OPENJSON(@contractJSON) WITH (cifs NVARCHAR(MAX) '$.cifs');
			--DECLARE @cifsJSON NVARCHAR(MAX);
            SET @cifsJSON = JSON_QUERY(@contractJSON, '$.cifs');

            SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);
			SET @cifIndex = 0;

			WHILE @cifIndex < @noOfCifIds
			BEGIN
				SET @cifJSON = JSON_QUERY(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
				SELECT @cifJSON = [value] FROM OPENJSON(@cifsJSON) WHERE [key] = @cifIndex;
				SET @cifIndex = @cifIndex + 1;
				--SELECT @contractId = value FROM OPENJSON(@cifJSON) WITH (contractId NVARCHAR(MAX) '$.id');
				--DECLARE @cifId NVARCHAR(MAX);
                SET @cifId = JSON_VALUE(@cifJSON, '$.id');

				IF @cifId IS NULL OR @cifId = ''
				BEGIN
					-- return error signal
					RETURN;
				END;

				IF @cifIds = ''
				BEGIN
					SET @cifIds = @cifId;
				END
				ELSE
				BEGIN
					SET @cifIds = CONCAT(@cifIds, ',', @cifId);
				END;
			END;
		END;

		SET @contractIds = CONCAT('''', REPLACE(@contractIds, ',', ''','''), '''');
		SET @cifIds = CONCAT('''', REPLACE(@cifIds, ',', ''','''), '''');
	END;

	SET @sqlStmt = '';
	SET @noOfRequestIds = LEN(@requestIds) - LEN(REPLACE(@requestIds, ',', '')) + 1;
	SET @requestIdIndex = 1;

	WHILE @requestIdIndex <= @noOfRequestIds
	BEGIN
		SET @requestId = SUBSTRING(@requestIds, CHARINDEX(',', @requestIds, 1) + 1, LEN(@requestIds));

		IF @requestIdIndex = @noOfRequestIds
		BEGIN
			SET @requestId = @requestIds;
		END;

		SET @requestIdIndex = @requestIdIndex + 1;
		SET @isGroupMatrix = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @requestId);
		SET @sqlSubStmt = '';

		IF @isGroupMatrix = 0
		BEGIN
			SET @sqlSubStmt = CONCAT('(SELECT br.requestId, br.assocRequestId, br.transactionId, am.contractId, am.coreCustomerId, br.featureActionId, br.accountId, br.status, br.createdby, br.createdts, br.requiredSets, br.receivedSets, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ram.receivedApprovals, CAST(br.isGroupMatrix AS UNSIGNED) AS isGroupMatrix, NULL AS groupList, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, GROUP_CONCAT(cam.customerId) AS approverIds, br.additionalMeta,',
                        ' (SELECT GROUP_CONCAT(' , @singleQuote , '{"','cam.customerId : c.userName','}',') FROM customer AS c WHERE c.id = cam.customerId) AS approverUserNames
                            FROM bbrequest AS br LEFT JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                            LEFT JOIN customerapprovalmatrix AS cam ON cam.approvalMatrixId = ram.approvalMatrixId LEFT JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId WHERE br.requestId = ',@singleQuote, '@requestId', @singleQuote,
                        'IF(@isCifLevelFilter = 1, CONCAT(', @singleQuote, 'AND am.contractId IN (', '@contractIds', ') AND am.coreCustomerId IN (', '@cifIds', '))', ')', ' GROUP BY br.requestId, ram.approvalMatrixId)');
		END
		ELSE
		BEGIN
			SET @sqlSubStmt = concat('
    SELECT
        br.requestId,
        br.assocRequestId,
        br.transactionId,
        am.contractId,
        am.coreCustomerId,
        br.featureActionId,
        br.accountId,
        br.status,
        br.createdby,
        br.createdts,
        br.requiredSets,
        br.receivedSets,
        ram.approvalMatrixId,
        am.limitTypeId,
        am.approvalruleId,
        ram.receivedApprovals,
        br.additionalMeta,
        sgrm.groupList,
        sgrm.pendingGroupList,
        sgrm.groupRuleValue,
        CAST(br.isGroupMatrix AS INT) AS isGroupMatrix,
        CAST(sgrm.isApproved AS INT) AS isGroupRuleApproved,' +
                                             '(SELECT STUFF((SELECT '','' + CAST(cust.customerid AS VARCHAR)
                                                 FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                 WHERE CHARINDEX('','' + CAST(cust.signatoryGroupId AS VARCHAR) + '','', (REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', ''''))) > 0
                                                 FOR XML PATH('''')), 1, 1, '''')
                                                 ) AS approverIds,',
                                             '(SELECT STUFF((SELECT '','' + ''{"'' + CAST(cust.customerid AS VARCHAR) + ''":"'' + c.userName + ''"}''
                                                    FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                    INNER JOIN ' ,@currentSchema,'.customer AS c ON c.id = cust.customerid
                                                    WHERE CHARINDEX(cust.signatoryGroupId, REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', '''')) > 0
                                                    FOR XML PATH('''')), 1, 1, ''''))
                                                  AS approverUserNames ',
                                             'FROM ' ,@currentSchema,'. bbrequest AS br  ',
                                             'LEFT JOIN ' ,@currentSchema,'.requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                                             LEFT JOIN  ' ,@currentSchema,'.signatorygrouprequestmatrix AS sgrm ON (ram.approvalMatrixId = sgrm.approvalMatrixId AND sgrm.requestId = br.requestId)
                                             LEFT JOIN ' ,@currentSchema,'.approvalmatrix AS am ON am.id = ram.approvalMatrixId
                                         WHERE br.requestId = ',
                                            @singleQuote, @requestId, @singleQuote)
		END
		IF @sqlStmt = ''
		BEGIN
			SET @sqlStmt = @sqlSubStmt;
		END
		ELSE
		BEGIN
			SET @sqlStmt = CONCAT(@sqlStmt, ' UNION ALL ', @sqlSubStmt);
		END;
	END;

  	   -- select @sqlStmt
--     select OBJECT_SCHEMA_NAME(@@PROCID);
--         select @currentSchema;
	EXEC (@sqlStmt);

    END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[withdraw_pendingrequests_in_approvalqueue_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[withdraw_pendingrequests_in_approvalqueue_proc]
    @_requestMatrixDataJSON nvarchar(MAX),
    @_customerId varchar(64)
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @noOfRequests int;
    DECLARE @sqlStmt nvarchar(MAX);
    DECLARE @requestIndex int;
    DECLARE @requestIds nvarchar(MAX);
    DECLARE @assocRequestIds nvarchar(MAX);
	DECLARE @singleQuote nvarchar(1);
    SET @singleQuote = '''';
    DECLARE @currentSchema NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);

    SELECT @noOfRequests = COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON);
    --SELECT COUNT(*) AS ElementCount FROM OPENJSON(@_requestMatrixDataJSON);

--    select @noOfRequests;
    SET @sqlStmt = '';
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';

    WHILE @requestIndex < @noOfRequests
    BEGIN
--        select 'came here';
        SET @requestIndex = @requestIndex + 1;
        DECLARE @requestJSON nvarchar(MAX);
        SET @requestJSON = JSON_QUERY(@_requestMatrixDataJSON, CONCAT('$[', @requestIndex - 1, ']'));
        DECLARE @requestId varchar(64);
        DECLARE @comments nvarchar(MAX);
        DECLARE @isGroupMatrix int;
        DECLARE @assocRequestId varchar(64);
        DECLARE @companyId varchar(64);
        DECLARE @companyLegalUnit varchar(64);


        SELECT @requestId = JSON_VALUE(@requestJSON, '$.requestId');
        SELECT @comments = JSON_VALUE(@requestJSON, '$.comments');

        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';

        SELECT @assocRequestId = assocRequestId, @companyId = companyId, @isGroupMatrix = isGroupMatrix, @companyLegalUnit = companyLegalUnit
        FROM bbrequest
        WHERE requestId = @requestId;

        IF @assocRequestIds = ''
            SET @assocRequestIds = @assocRequestId;
        ELSE
            SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);

        INSERT INTO bbactedrequest (requestId, assocRequestId, companyId, status, comments, createdby, [action], companyLegalUnit)
        VALUES (@requestId, @assocRequestId, @companyId, 'Withdrawn', @comments, @_customerId, 'Withdrawn', @companyLegalUnit);
    END;

	DECLARE @temp_requestIds TABLE (id varchar(max));
     INSERT INTO @temp_requestIds (id) (SELECT DISTINCT bbr.requestId FROM bbrequest bbr WHERE assocRequestId IN (SELECT value FROM STRING_SPLIT(@assocRequestIds, ',')));
        SELECT @requestIds = STRING_AGG(id, ',') FROM @temp_requestIds;

    UPDATE bbrequest SET status = 'Withdrawn' WHERE requestId IN (SELECT value FROM STRING_SPLIT(@requestIds, ','));

    -- EXECUTE fetch_requests_with_approvalmatrixinfo_proc @assocRequestIds, '1', '', '0';
    -- mini proc here

    BEGIN
    DECLARE @_requestIds_mini NVARCHAR(MAX);
	DECLARE @_isAssociationId_mini NVARCHAR(2);
	DECLARE @_contractCifMapJSON_mini NVARCHAR(MAX);
	DECLARE @_isActiveRulesFetch_mini NVARCHAR(2);

    SET @_requestIds_mini = @assocRequestIds
    SET @_isAssociationId_mini = '1'
    SET @_contractCifMapJSON_mini = ''
    SET @_isActiveRulesFetch_mini = '0'


    DECLARE @currentSchema_mini NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId_mini BIT;
	DECLARE @isActiveRulesFetch_mini BIT;
	DECLARE @requestId_minis_mini NVARCHAR(MAX);
	DECLARE @sqlStmt_mini NVARCHAR(MAX);
	DECLARE @isCifLevelFilter_mini BIT;
	DECLARE @contractId_minis_mini NVARCHAR(MAX);
	DECLARE @cifId_minis_mini NVARCHAR(MAX);
	DECLARE @noOfContractIds_mini INT;
	DECLARE @contractId_miniIndex_mini INT;
	DECLARE @contractJSON_mini NVARCHAR(MAX);
	DECLARE @contractId_mini NVARCHAR(MAX);
	DECLARE @cifsJSON_mini NVARCHAR(MAX);
	DECLARE @noOfCifIds_mini INT;
	DECLARE @cifIndex_mini INT;
	DECLARE @cifJSON_mini NVARCHAR(MAX);
	DECLARE @cifId_mini NVARCHAR(MAX);
	DECLARE @requestId_mini NVARCHAR(MAX);
	DECLARE @isGroupMatrix_mini BIT;
	DECLARE @sqlSubStmt_mini NVARCHAR(MAX);
	DECLARE @noOfRequestIds_mini INT;
	DECLARE @requestId_Index_mini INT;
	DECLARE @singleQuote_mini nvarchar(1);
    SET @singleQuote_mini = '''';
	DECLARE @temp_requestIds_mini TABLE (id varchar(max));

	IF @_isAssociationId_mini IS NULL OR @_isAssociationId_mini = ''
		SET @isAssocId_mini = 0;
	ELSE
		SET @isAssocId_mini = CAST(@_isAssociationId_mini AS INT);
	IF @_isActiveRulesFetch_mini IS NULL OR @_isActiveRulesFetch_mini = ''
		SET @isActiveRulesFetch_mini = 0;
	ELSE
		SET @isActiveRulesFetch_mini = CAST(@_isActiveRulesFetch_mini AS BIT);
	SET @requestId_minis_mini = @_requestIds_mini;

	IF @isAssocId_mini = 1
	BEGIN
		--SET @requestId_minis_mini = CONCAT('''', REPLACE(@_requestIds_mini, ',', ''','''), '''');
		--SET @sqlStmt_mini = CONCAT('SELECT STRING_AGG(DISTINCT requestId, '','') WITHIN GROUP (ORDER BY requestId) INTO @requestId_minis_mini FROM bbrequest WHERE assocRequestId IN (', @requestId_minis_mini, ')');
		--EXEC (@sqlStmt_mini);
        INSERT INTO @temp_requestIds_mini (id) (SELECT DISTINCT bbr.requestId FROM bbrequest bbr WHERE assocRequestId IN (SELECT value FROM STRING_SPLIT(@_requestIds_mini, ',')));
        SELECT @requestId_minis_mini = STRING_AGG(id, ',') FROM @temp_requestIds_mini;
	END;

	IF @requestId_minis_mini IS NULL
	BEGIN
		SET @requestId_minis_mini = '';
	END;

	SET @isCifLevelFilter_mini = 1;
	SET @contractId_minis_mini = '';
	SET @cifId_minis_mini = '';

	IF @_contractCifMapJSON_mini IS NULL OR @_contractCifMapJSON_mini = ''
	BEGIN
		SET @isCifLevelFilter_mini = 0;
	END
	ELSE
	BEGIN
		SELECT @noOfContractIds_mini = COUNT(*) FROM OPENJSON(@_contractCifMapJSON_mini);
		SET @contractId_miniIndex_mini = 0;
		WHILE @contractId_miniIndex_mini < @noOfContractIds_mini
		BEGIN
-- 			SELECT @contractJSON_mini = [value] FROM OPENJSON(@_contractCifMapJSON_mini) WITH (Value NVARCHAR(MAX) '$[' + CAST(@contractId_miniIndex_mini AS NVARCHAR(10)) + ']');
--SET @contractJSON_mini = JSON_QUERY(_contractCifMapJSON, CONCAT('$[', @contractId_miniIndex_mini, ']'));
SELECT @contractJSON_mini = [value]
FROM OPENJSON(@_contractCifMapJSON_mini)
WHERE [key] = @contractId_miniIndex_mini;
			SET @contractId_miniIndex_mini = @contractId_miniIndex_mini + 1;

			--SELECT @contractId_mini = value FROM OPENJSON(@contractJSON_mini) WITH (contractId NVARCHAR(MAX) '$.contractId');
            SET @contractId_mini = JSON_VALUE(@contractJSON_mini, '$.contractId');

            IF @contractId_mini IS NULL OR @contractId_mini = ''
			BEGIN
				-- return error signal
				RETURN;
			END;

			IF @contractId_minis_mini = ''
			BEGIN
				SET @contractId_minis_mini = @contractId_mini;
			END
			ELSE
			BEGIN
				SET @contractId_minis_mini = CONCAT(@contractId_minis_mini, ',', @contractId_mini);
			END;
			--SELECT @cifsJSON_mini = [value] FROM OPENJSON(@contractJSON_mini) WITH (cifs NVARCHAR(MAX) '$.cifs');
			--DECLARE @cifsJSON_mini NVARCHAR(MAX);
            SET @cifsJSON_mini = JSON_QUERY(@contractJSON_mini, '$.cifs');

            SELECT @noOfCifIds_mini = COUNT(*) FROM OPENJSON(@cifsJSON_mini);
			SET @cifIndex_mini = 0;

			WHILE @cifIndex_mini < @noOfCifIds_mini
			BEGIN
				SET @cifJSON_mini = JSON_QUERY(@cifsJSON_mini, CONCAT('$[', @cifIndex_mini, ']'));
				SELECT @cifJSON_mini = [value] FROM OPENJSON(@cifsJSON_mini) WHERE [key] = @cifIndex_mini;
				SET @cifIndex_mini = @cifIndex_mini + 1;
				--SELECT @contractId_mini = value FROM OPENJSON(@cifJSON_mini) WITH (contractId NVARCHAR(MAX) '$.id');
				--DECLARE @cifId_mini NVARCHAR(MAX);
                SET @cifId_mini = JSON_VALUE(@cifJSON_mini, '$.id');

				IF @cifId_mini IS NULL OR @cifId_mini = ''
				BEGIN
					-- return error signal
					RETURN;
				END;

				IF @cifId_minis_mini = ''
				BEGIN
					SET @cifId_minis_mini = @cifId_mini;
				END
				ELSE
				BEGIN
					SET @cifId_minis_mini = CONCAT(@cifId_minis_mini, ',', @cifId_mini);
				END;
			END;
		END;

		SET @contractId_minis_mini = CONCAT('''', REPLACE(@contractId_minis_mini, ',', ''','''), '''');
		SET @cifId_minis_mini = CONCAT('''', REPLACE(@cifId_minis_mini, ',', ''','''), '''');
	END;

	SET @sqlStmt_mini = '';
	SET @noOfRequestIds_mini = LEN(@requestId_minis_mini) - LEN(REPLACE(@requestId_minis_mini, ',', '')) + 1;
	SET @requestId_Index_mini = 1;

	WHILE @requestId_Index_mini <= @noOfRequestIds_mini
	BEGIN
		SET @requestId_mini = SUBSTRING(@requestId_minis_mini, CHARINDEX(',', @requestId_minis_mini, 1) + 1, LEN(@requestId_minis_mini));

		IF @requestId_Index_mini = @noOfRequestIds_mini
		BEGIN
			SET @requestId_mini = @requestId_minis_mini;
		END;

		SET @requestId_Index_mini = @requestId_Index_mini + 1;
		SET @isGroupMatrix_mini = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @requestId_mini);
		SET @sqlSubStmt_mini = '';

		IF @isGroupMatrix_mini = 0
		BEGIN
			SET @sqlSubStmt_mini = CONCAT('(SELECT br.requestId, br.assocRequestId, br.transactionId, am.contractId, am.coreCustomerId, br.featureActionId, br.accountId, br.status, br.createdby, br.createdts, br.requiredSets, br.receivedSets, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ram.receivedApprovals, CAST(br.isGroupMatrix AS UNSIGNED) AS isGroupMatrix, NULL AS groupList, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, GROUP_CONCAT(cam.customerId) AS approverIds, br.additionalMeta,',
                        ' (SELECT GROUP_CONCAT(' , @singleQuote_mini , '{"','cam.customerId : c.userName','}',') FROM customer AS c WHERE c.id = cam.customerId) AS approverUserNames
                            FROM bbrequest AS br LEFT JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                            LEFT JOIN customerapprovalmatrix AS cam ON cam.approvalMatrixId = ram.approvalMatrixId LEFT JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId WHERE br.requestId = ',@singleQuote_mini, '@requestId_mini', @singleQuote_mini,
                        'IF(@isCifLevelFilter_mini = 1, CONCAT(', @singleQuote_mini, 'AND am.contractId IN (', '@contractId_minis_mini', ') AND am.coreCustomerId IN (', '@cifId_minis_mini', '))', ')', ' GROUP BY br.requestId, ram.approvalMatrixId)');
		END
		ELSE
		BEGIN
			SET @sqlSubStmt_mini = concat('
    SELECT
        br.requestId,
        br.assocRequestId,
        br.transactionId,
        am.contractId,
        am.coreCustomerId,
        br.featureActionId,
        br.accountId,
        br.status,
        br.createdby,
        br.createdts,
        br.requiredSets,
        br.receivedSets,
        ram.approvalMatrixId,
        am.limitTypeId,
        am.approvalruleId,
        ram.receivedApprovals,
        br.additionalMeta,
        sgrm.groupList,
        sgrm.pendingGroupList,
        sgrm.groupRuleValue,
        CAST(br.isGroupMatrix AS INT) AS isGroupMatrix,
        CAST(sgrm.isApproved AS INT) AS isGroupRuleApproved,' +
                                             '(SELECT STUFF((SELECT '','' + CAST(cust.customerid AS VARCHAR)
                                                 FROM ',@currentSchema_mini, '.customersignatorygroup AS cust
                                                 WHERE CHARINDEX('','' + CAST(cust.signatoryGroupId AS VARCHAR) + '','', (REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', ''''))) > 0
                                                 FOR XML PATH('''')), 1, 1, '''')
                                                 ) AS approverIds,',
                                             '(SELECT STUFF((SELECT '','' + ''{"'' + CAST(cust.customerid AS VARCHAR) + ''":"'' + c.userName + ''"}''
                                                    FROM ',@currentSchema_mini, '.customersignatorygroup AS cust
                                                    INNER JOIN ' ,@currentSchema_mini,'.customer AS c ON c.id = cust.customerid
                                                    WHERE CHARINDEX(cust.signatoryGroupId, REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', '''')) > 0
                                                    FOR XML PATH('''')), 1, 1, ''''))
                                                  AS approverUserNames ',
                                             'FROM ' ,@currentSchema_mini,'. bbrequest AS br  ',
                                             'LEFT JOIN ' ,@currentSchema_mini,'.requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                                             LEFT JOIN  ' ,@currentSchema_mini,'.signatorygrouprequestmatrix AS sgrm ON (ram.approvalMatrixId = sgrm.approvalMatrixId AND sgrm.requestId = br.requestId)
                                             LEFT JOIN ' ,@currentSchema_mini,'.approvalmatrix AS am ON am.id = ram.approvalMatrixId
                                         WHERE br.requestId = ',
                                            @singleQuote_mini, @requestId_mini, @singleQuote_mini)
		END
		IF @sqlStmt_mini = ''
		BEGIN
			SET @sqlStmt_mini = @sqlSubStmt_mini;
		END
		ELSE
		BEGIN
			SET @sqlStmt_mini = CONCAT(@sqlStmt_mini, ' UNION ALL ', @sqlSubStmt_mini);
		END;
	END;
	EXEC (@sqlStmt_mini);

    end
    END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reject_pendingrequests_in_approvalqueue_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[reject_pendingrequests_in_approvalqueue_proc]
    @_requestMatrixDataJSON NVARCHAR(MAX),
    @_customerId VARCHAR(64)
AS
BEGIN
    SET NOCOUNT ON;
    DECLARE @noOfRequests INT;
    DECLARE @sqlStmt NVARCHAR(MAX);
    DECLARE @requestIndex INT;
    DECLARE @requestIds NVARCHAR(MAX);
    DECLARE @assocRequestIds NVARCHAR(MAX);

	DECLARE @temp_sgn TABLE (id varchar(50));

    SELECT @noOfRequests = COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON);

    SET @sqlStmt = '';
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';

    WHILE @requestIndex < @noOfRequests
    BEGIN
        SET @requestIndex = @requestIndex + 1;

        DECLARE @requestJSON NVARCHAR(MAX);
        DECLARE @requestId NVARCHAR(MAX);
        DECLARE @comments NVARCHAR(MAX);
        DECLARE @isGroupMatrix INT;
        DECLARE @assocRequestId NVARCHAR(MAX);
        DECLARE @companyId NVARCHAR(MAX);
        DECLARE @companyLegalUnit NVARCHAR(MAX);
        DECLARE @actingGroupsCSV NVARCHAR(MAX);
        DECLARE @actingGroups NVARCHAR(MAX);

        SET @requestJSON = JSON_QUERY(@_requestMatrixDataJSON, CONCAT('$[', @requestIndex - 1, ']'));
        SET @requestId = JSON_VALUE(@requestJSON, '$.requestId');
        SET @comments = JSON_VALUE(@requestJSON, '$.comments');
        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';

        SELECT @assocRequestId = assocRequestId, @companyId = companyId, @isGroupMatrix = isGroupMatrix, @companyLegalUnit = companyLegalUnit
        FROM ${dbxschemaname}.bbrequest
        WHERE requestId = @requestId;

        IF @assocRequestIds = ''
            SET @assocRequestIds = @assocRequestId;
        ELSE
            SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);

        IF @isGroupMatrix = 1
        BEGIN
            SET @actingGroupsCSV = JSON_VALUE(@requestJSON, '$.actingGroupsCSV');

            IF @actingGroupsCSV IS NULL
                CONTINUE;

            SET @actingGroups = '';

            INSERT INTO @temp_sgn (id) (select signatoryGroupName FROM signatorygroup WHERE signatoryGroupId IN (SELECT value FROM STRING_SPLIT(@actingGroupsCSV, ',')));
            SELECT @actingGroups = STRING_AGG(id, ',') FROM @temp_sgn;

            INSERT INTO ${dbxschemaname}.bbactedrequest (requestId, assocRequestId, companyId, status, comments, createdby, groupName, action, companyLegalUnit)
            VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments, @_customerId, @actingGroups, 'Rejected', @companyLegalUnit);
        END
        ELSE
        BEGIN
            INSERT INTO ${dbxschemaname}.bbactedrequest (requestId, assocRequestId, companyId, status, comments, createdby, action, companyLegalUnit)
            VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments, @_customerId, 'Rejected', @companyLegalUnit);
        END;
    END;

        	DECLARE @temp_requestIds TABLE (id varchar(max));
     INSERT INTO @temp_requestIds (id) (SELECT DISTINCT bbr.requestId FROM bbrequest bbr WHERE assocRequestId IN (SELECT value FROM STRING_SPLIT(@assocRequestIds, ',')));
        SELECT @requestIds = STRING_AGG(id, ',') FROM @temp_requestIds;

    UPDATE bbrequest SET status = 'Rejected' WHERE requestId IN (SELECT value FROM STRING_SPLIT(@requestIds, ','));

--     SET @assocRequestIds = CONCAT('''', REPLACE(@assocRequestIds, ',', ''','''), '''');
--     SET @sqlStmt = CONCAT('SELECT @requestIds = STRING_AGG(requestId, '''') FROM bbrequest WHERE assocRequestId IN (', @assocRequestIds, ')');
--     EXEC sp_executesql @sqlStmt, N'@requestIds NVARCHAR(MAX) OUTPUT', @requestIds OUTPUT;
--
--     SET @requestIds = CONCAT('''', REPLACE(@requestIds, ',', ''','''), '''');
--     SET @sqlStmt = CONCAT('UPDATE bbrequest SET status = ''Rejected'' WHERE requestId IN (', @requestIds, ')');
--     EXEC sp_executesql @sqlStmt;

--    EXEC fetch_requests_with_approvalmatrixinfo_proc @assocRequestIds, '1', '', '0';



BEGIN
    DECLARE @_requestIds_mini NVARCHAR(MAX);
	DECLARE @_isAssociationId_mini NVARCHAR(2);
	DECLARE @_contractCifMapJSON_mini NVARCHAR(MAX);
	DECLARE @_isActiveRulesFetch_mini NVARCHAR(2);

    SET @_requestIds_mini = @assocRequestIds
    SET @_isAssociationId_mini = '1'
    SET @_contractCifMapJSON_mini = ''
    SET @_isActiveRulesFetch_mini = '0'


    DECLARE @currentSchema_mini NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId_mini BIT;
	DECLARE @isActiveRulesFetch_mini BIT;
	DECLARE @requestId_minis_mini NVARCHAR(MAX);
	DECLARE @sqlStmt_mini NVARCHAR(MAX);
	DECLARE @isCifLevelFilter_mini BIT;
	DECLARE @contractId_minis_mini NVARCHAR(MAX);
	DECLARE @cifId_minis_mini NVARCHAR(MAX);
	DECLARE @noOfContractIds_mini INT;
	DECLARE @contractId_miniIndex_mini INT;
	DECLARE @contractJSON_mini NVARCHAR(MAX);
	DECLARE @contractId_mini NVARCHAR(MAX);
	DECLARE @cifsJSON_mini NVARCHAR(MAX);
	DECLARE @noOfCifIds_mini INT;
	DECLARE @cifIndex_mini INT;
	DECLARE @cifJSON_mini NVARCHAR(MAX);
	DECLARE @cifId_mini NVARCHAR(MAX);
	DECLARE @requestId_mini NVARCHAR(MAX);
	DECLARE @isGroupMatrix_mini BIT;
	DECLARE @sqlSubStmt_mini NVARCHAR(MAX);
	DECLARE @noOfRequestIds_mini INT;
	DECLARE @requestId_Index_mini INT;
	DECLARE @singleQuote_mini nvarchar(1);
    SET @singleQuote_mini = '''';
	DECLARE @temp_requestIds_mini TABLE (id varchar(max));

	IF @_isAssociationId_mini IS NULL OR @_isAssociationId_mini = ''
		SET @isAssocId_mini = 0;
	ELSE
		SET @isAssocId_mini = CAST(@_isAssociationId_mini AS INT);
	IF @_isActiveRulesFetch_mini IS NULL OR @_isActiveRulesFetch_mini = ''
		SET @isActiveRulesFetch_mini = 0;
	ELSE
		SET @isActiveRulesFetch_mini = CAST(@_isActiveRulesFetch_mini AS BIT);
	SET @requestId_minis_mini = @_requestIds_mini;

	IF @isAssocId_mini = 1
	BEGIN
        INSERT INTO @temp_requestIds_mini (id) (SELECT DISTINCT bbr.requestId FROM bbrequest bbr WHERE assocRequestId IN (SELECT value FROM STRING_SPLIT(@_requestIds_mini, ',')));
        SELECT @requestId_minis_mini = STRING_AGG(id, ',') FROM @temp_requestIds_mini;
	END;

	IF @requestId_minis_mini IS NULL
	BEGIN
		SET @requestId_minis_mini = '';
	END;

	SET @isCifLevelFilter_mini = 1;
	SET @contractId_minis_mini = '';
	SET @cifId_minis_mini = '';

	IF @_contractCifMapJSON_mini IS NULL OR @_contractCifMapJSON_mini = ''
	BEGIN
		SET @isCifLevelFilter_mini = 0;
	END
	ELSE
	BEGIN
		SELECT @noOfContractIds_mini = COUNT(*) FROM OPENJSON(@_contractCifMapJSON_mini);
		SET @contractId_miniIndex_mini = 0;
		WHILE @contractId_miniIndex_mini < @noOfContractIds_mini
		BEGIN
-- 			SELECT @contractJSON_mini = [value] FROM OPENJSON(@_contractCifMapJSON_mini) WITH (Value NVARCHAR(MAX) '$[' + CAST(@contractId_miniIndex_mini AS NVARCHAR(10)) + ']');
--SET @contractJSON_mini = JSON_QUERY(_contractCifMapJSON, CONCAT('$[', @contractId_miniIndex_mini, ']'));
SELECT @contractJSON_mini = [value]
FROM OPENJSON(@_contractCifMapJSON_mini)
WHERE [key] = @contractId_miniIndex_mini;
			SET @contractId_miniIndex_mini = @contractId_miniIndex_mini + 1;

			--SELECT @contractId_mini = value FROM OPENJSON(@contractJSON_mini) WITH (contractId NVARCHAR(MAX) '$.contractId');
            SET @contractId_mini = JSON_VALUE(@contractJSON_mini, '$.contractId');

            IF @contractId_mini IS NULL OR @contractId_mini = ''
			BEGIN
				-- return error signal
				RETURN;
			END;

			IF @contractId_minis_mini = ''
			BEGIN
				SET @contractId_minis_mini = @contractId_mini;
			END
			ELSE
			BEGIN
				SET @contractId_minis_mini = CONCAT(@contractId_minis_mini, ',', @contractId_mini);
			END;
			--SELECT @cifsJSON_mini = [value] FROM OPENJSON(@contractJSON_mini) WITH (cifs NVARCHAR(MAX) '$.cifs');
			--DECLARE @cifsJSON_mini NVARCHAR(MAX);
            SET @cifsJSON_mini = JSON_QUERY(@contractJSON_mini, '$.cifs');

            SELECT @noOfCifIds_mini = COUNT(*) FROM OPENJSON(@cifsJSON_mini);
			SET @cifIndex_mini = 0;

			WHILE @cifIndex_mini < @noOfCifIds_mini
			BEGIN
				SET @cifJSON_mini = JSON_QUERY(@cifsJSON_mini, CONCAT('$[', @cifIndex_mini, ']'));
				SELECT @cifJSON_mini = [value] FROM OPENJSON(@cifsJSON_mini) WHERE [key] = @cifIndex_mini;
				SET @cifIndex_mini = @cifIndex_mini + 1;
				--SELECT @contractId_mini = value FROM OPENJSON(@cifJSON_mini) WITH (contractId NVARCHAR(MAX) '$.id');
				--DECLARE @cifId_mini NVARCHAR(MAX);
                SET @cifId_mini = JSON_VALUE(@cifJSON_mini, '$.id');

				IF @cifId_mini IS NULL OR @cifId_mini = ''
				BEGIN
					-- return error signal
					RETURN;
				END;

				IF @cifId_minis_mini = ''
				BEGIN
					SET @cifId_minis_mini = @cifId_mini;
				END
				ELSE
				BEGIN
					SET @cifId_minis_mini = CONCAT(@cifId_minis_mini, ',', @cifId_mini);
				END;
			END;
		END;

		SET @contractId_minis_mini = CONCAT('''', REPLACE(@contractId_minis_mini, ',', ''','''), '''');
		SET @cifId_minis_mini = CONCAT('''', REPLACE(@cifId_minis_mini, ',', ''','''), '''');
	END;

	SET @sqlStmt_mini = '';
	SET @noOfRequestIds_mini = LEN(@requestId_minis_mini) - LEN(REPLACE(@requestId_minis_mini, ',', '')) + 1;
	SET @requestId_Index_mini = 1;

	WHILE @requestId_Index_mini <= @noOfRequestIds_mini
	BEGIN
		SET @requestId_mini = SUBSTRING(@requestId_minis_mini, CHARINDEX(',', @requestId_minis_mini, 1) + 1, LEN(@requestId_minis_mini));

		IF @requestId_Index_mini = @noOfRequestIds_mini
		BEGIN
			SET @requestId_mini = @requestId_minis_mini;
		END;

		SET @requestId_Index_mini = @requestId_Index_mini + 1;
		SET @isGroupMatrix_mini = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @requestId_mini);
		SET @sqlSubStmt_mini = '';

		IF @isGroupMatrix_mini = 0
		BEGIN
			SET @sqlSubStmt_mini = CONCAT('(SELECT br.requestId, br.assocRequestId, br.transactionId, am.contractId, am.coreCustomerId, br.featureActionId, br.accountId, br.status, br.createdby, br.createdts, br.requiredSets, br.receivedSets, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ram.receivedApprovals, CAST(br.isGroupMatrix AS UNSIGNED) AS isGroupMatrix, NULL AS groupList, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, GROUP_CONCAT(cam.customerId) AS approverIds, br.additionalMeta,',
                        ' (SELECT GROUP_CONCAT(' , @singleQuote_mini , '{"','cam.customerId : c.userName','}',') FROM customer AS c WHERE c.id = cam.customerId) AS approverUserNames
                            FROM bbrequest AS br LEFT JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                            LEFT JOIN customerapprovalmatrix AS cam ON cam.approvalMatrixId = ram.approvalMatrixId LEFT JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId WHERE br.requestId = ',@singleQuote_mini, '@requestId_mini', @singleQuote_mini,
                        'IF(@isCifLevelFilter_mini = 1, CONCAT(', @singleQuote_mini, 'AND am.contractId IN (', '@contractId_minis_mini', ') AND am.coreCustomerId IN (', '@cifId_minis_mini', '))', ')', ' GROUP BY br.requestId, ram.approvalMatrixId)');
		END
		ELSE
		BEGIN
			SET @sqlSubStmt_mini = concat('
    SELECT
        br.requestId,
        br.assocRequestId,
        br.transactionId,
        am.contractId,
        am.coreCustomerId,
        br.featureActionId,
        br.accountId,
        br.status,
        br.createdby,
        br.createdts,
        br.requiredSets,
        br.receivedSets,
        ram.approvalMatrixId,
        am.limitTypeId,
        am.approvalruleId,
        ram.receivedApprovals,
        br.additionalMeta,
        sgrm.groupList,
        sgrm.pendingGroupList,
        sgrm.groupRuleValue,
        CAST(br.isGroupMatrix AS INT) AS isGroupMatrix,
        CAST(sgrm.isApproved AS INT) AS isGroupRuleApproved,' +
                                             '(SELECT STUFF((SELECT '','' + CAST(cust.customerid AS VARCHAR)
                                                 FROM ',@currentSchema_mini, '.customersignatorygroup AS cust
                                                 WHERE CHARINDEX('','' + CAST(cust.signatoryGroupId AS VARCHAR) + '','', (REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', ''''))) > 0
                                                 FOR XML PATH('''')), 1, 1, '''')
                                                 ) AS approverIds,',
                                             '(SELECT STUFF((SELECT '','' + ''{"'' + CAST(cust.customerid AS VARCHAR) + ''":"'' + c.userName + ''"}''
                                                    FROM ',@currentSchema_mini, '.customersignatorygroup AS cust
                                                    INNER JOIN ' ,@currentSchema_mini,'.customer AS c ON c.id = cust.customerid
                                                    WHERE CHARINDEX(cust.signatoryGroupId, REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', '''')) > 0
                                                    FOR XML PATH('''')), 1, 1, ''''))
                                                  AS approverUserNames ',
                                             'FROM ' ,@currentSchema_mini,'. bbrequest AS br  ',
                                             'LEFT JOIN ' ,@currentSchema_mini,'.requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                                             LEFT JOIN  ' ,@currentSchema_mini,'.signatorygrouprequestmatrix AS sgrm ON (ram.approvalMatrixId = sgrm.approvalMatrixId AND sgrm.requestId = br.requestId)
                                             LEFT JOIN ' ,@currentSchema_mini,'.approvalmatrix AS am ON am.id = ram.approvalMatrixId
                                         WHERE br.requestId = ',
                                            @singleQuote_mini, @requestId_mini, @singleQuote_mini)
		END
		IF @sqlStmt_mini = ''
		BEGIN
			SET @sqlStmt_mini = @sqlSubStmt_mini;
		END
		ELSE
		BEGIN
			SET @sqlStmt_mini = CONCAT(@sqlStmt_mini, ' UNION ALL ', @sqlSubStmt_mini);
		END;
	END;
	EXEC (@sqlStmt_mini);
    end
    END;
GO

ALTER TABLE [${dbxschemaname}].[signatorygroup] ALTER COLUMN [signatoryGroupDescription] nvarchar(200) NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[signatorygroup_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[signatorygroup_create_proc] @signatoryGroupValues NVARCHAR(max),
    @customerSignatoryGroupValues NVARCHAR(max),
    @signatoryGroupId NVARCHAR(50),
    @signatoryGroupName NVARCHAR(50),
    @signatoryGroupDescription NVARCHAR(200),
    @coreCustomerId NVARCHAR(50),
    @contractId NVARCHAR(50),
    @createdby NVARCHAR(50)
AS
BEGIN


    DECLARE @index1 INTEGER = 0;
    DECLARE @length1 INTEGER = 0;
    DECLARE @matrixRecord nvarchar(max);
    DECLARE @matrixComma nvarchar(max);
    DECLARE @matrixComma1 nvarchar(max);
    DECLARE @query nvarchar(max);
    DECLARE @signatory nvarchar(max);
    DECLARE @signatoriesComma nvarchar(max);
    DECLARE @signatoriesComma1 nvarchar(max);

    IF (@signatoryGroupValues IS NOT NULL AND @signatoryGroupValues != '')
BEGIN
            set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX(@signatoryGroupValues, ',', 1);
set @matrixComma1 = REPLACE(@matrixRecord, ';', ',');
            set @matrixComma = REPLACE(@matrixComma1, '"', '''');
            set @query =
                    'INSERT INTO [${dbxschemaname}].signatorygroup(signatorygroup.signatoryGroupId,signatorygroup.signatoryGroupName,signatorygroup.signatoryGroupDescription,signatorygroup.coreCustomerId,signatorygroup.contractId,signatorygroup.createdby) values (' +
                    @matrixComma + ')';
execute (@query);
END;

    IF (@signatoryGroupValues IS NULL OR @signatoryGroupValues = '')
BEGIN

INSERT INTO [${dbxschemaname}].signatorygroup(signatoryGroupId, signatoryGroupName,
                                              signatoryGroupDescription, coreCustomerId,
                                              contractId, createdby)
values (@signatoryGroupId, @signatoryGroupName, @signatoryGroupDescription, @coreCustomerId, @contractId,
    @createdby);

END;
    IF (@customerSignatoryGroupValues IS NOT NULL AND @customerSignatoryGroupValues != '')
BEGIN
            set @length1 = LEN(@customerSignatoryGroupValues) - LEN(REPLACE(@customerSignatoryGroupValues, ',', ''));
            WHILE @index1 != @length1 BEGIN
                set @index1 = @index1 + 1;
                set @signatory =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@customerSignatoryGroupValues, ',', @index1),
',', -1);
set @signatoriesComma1 = REPLACE(@signatory, ';', ',');
                set @signatoriesComma = REPLACE(@signatoriesComma1, '"', '''');
                set @query =
                        'INSERT INTO [${dbxschemaname}].customersignatorygroup(customersignatorygroup.customerSignatoryGroupId, customersignatorygroup.signatoryGroupId, customersignatorygroup.customerId, customersignatorygroup.createdby) values (' +
                        @signatoriesComma + ')';
execute (@query);
END
END;
END
go

DROP VIEW IF EXISTS [${dbxschemaname}].[feature_actions_view];
GO

CREATE VIEW [${dbxschemaname}].[feature_actions_view] (
   [id], 
   [Feature_id], 
   [action_name], 
   [action_description], 
   [isAccountLevel], 
   [isMFAApplicable], 
   [isPrimary], 
   [notes], 
   [action_Type_id], 
   [action_displaysequence], 
   [action_dependency], 
   [feature_status_id], 
   [feature_name], 
   [feature_description], 
   [feature_Type_id], 
   [feature_displaysequence], 
   [feature_isPrimary], 
   [LimitType_id], 
   [companyLegalUnit],
   [value])
AS 
   SELECT 
      featureaction.id AS id, 
      featureaction.Feature_id AS Feature_id, 
      featureaction.name AS action_name, 
      featureaction.description AS action_description, 
      featureaction.isAccountLevel AS isAccountLevel, 
      featureaction.isMFAApplicable AS isMFAApplicable, 
      featureaction.isPrimary AS isPrimary, 
      featureaction.notes AS notes, 
      featureaction.Type_id AS action_Type_id, 
      featureaction.DisplaySequence AS action_displaysequence, 
      featureaction.dependency AS action_dependency, 
      feature.Status_id AS feature_status_id, 
      feature.name AS feature_name, 
      feature.description AS feature_description, 
      feature.Type_id AS feature_Type_id, 
      feature.DisplaySequence AS feature_displaysequence, 
      feature.isPrimary AS feature_isPrimary, 
      actionlimit.LimitType_id AS LimitType_id, 
	  featureaction.companyLegalUnit AS companyLegalUnit,
      actionlimit.value AS value
   FROM (([${dbxschemaname}].featureaction 
      LEFT JOIN [${dbxschemaname}].feature 
      ON ((feature.id = featureaction.Feature_id))) 
      LEFT JOIN [${dbxschemaname}].actionlimit 
      ON ((actionlimit.Action_id = featureaction.id)));
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_requests_assign_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_requests_assign_proc]  
   @_requestIds nvarchar(max),
   @_csrID nvarchar(200)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  declare @updateclause nvarchar(max)
      SET  NOCOUNT  ON
      SET @updateclause = (N'UPDATE dbxdb.customerrequest SET AssignedTo = ') + ((QUOTENAME((@_csrID), ''''))) + (N' where id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_requestIds)) + (N')')
      
      EXEC sp_executesql @updateclause;

   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_campaign_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE  PROCEDURE [${dbxschemaname}].[get_campaign_proc](
    @_eventCode nvarchar(50),
	@_status nvarchar(50))
AS
BEGIN
	SET  XACT_ABORT  ON

    SET  NOCOUNT  ON

if (LEN(@_eventCode)>0 AND LEN(@_status)>0)
BEGIN
    select cd.* 
	from 
	[${dbxschemaname}].campaigndefinition cd, 
	[${dbxschemaname}].campaigneventtrigger cet, 
	[${dbxschemaname}].eventtriggers et 
	where 
	cd.campaignId = cet.campaignId and 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode and 
	cd.campaignStatus = @_status;
END
else if (LEN(@_eventCode)>0)
BEGIN
    select cd.* 
	from 
	[${dbxschemaname}].campaigndefinition cd, 
	[${dbxschemaname}].campaigneventtrigger cet, 
	[${dbxschemaname}].eventtriggers et 
	where 
	cd.campaignId = cet.campaignId and 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode;
END
else if (LEN(@_status)>0)
BEGIN
    select * from [${dbxschemaname}].campaigndefinition where campaignStatus = @_status;
END
else
BEGIN
    select * from [${dbxschemaname}].campaigndefinition;
END

select * from [${dbxschemaname}].[campaigneventtrigger] ;
select * from [${dbxschemaname}].[campaignprofile]; 
select * from [${dbxschemaname}].[campaignchanneltype]; 
select * from [${dbxschemaname}].[campaignchanneldetails];  
select * from [${dbxschemaname}].[offlinetemplate]; 
select * from [${dbxschemaname}].[onlinecontent] where [campaignId]  is not null;

select * from [${dbxschemaname}].[profile] where [profileId]  in (select [profileId]  from [${dbxschemaname}].[campaignprofile]);
select * from [${dbxschemaname}].[profilecondition] where [profileId]  in (select [profileId]  from [${dbxschemaname}].[campaignprofile]) ;
select * from [${dbxschemaname}].[placeholder] where [placeholderId]  in (select [placeholderId]  from [${dbxschemaname}].[onlinecontent] where [campaignId]  is not null);
if (LEN(@_eventCode)>0)
BEGIN
    select * from [${dbxschemaname}].[eventtriggers] where eventTriggerId  in (select cet.eventTriggerId 
	from 
	[${dbxschemaname}].campaigneventtrigger cet left outer join
	[${dbxschemaname}].eventtriggers et 
	on 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode where cet.campaignId in (select campaignId  from [${dbxschemaname}].campaigndefinition c where campaignStatus =@_status));
END
else if (LEN(@_eventCode)>0)
BEGIN
    select * from [${dbxschemaname}].[eventtriggers] where eventTriggerId  in (select cet.eventTriggerId 
	from 
	[${dbxschemaname}].campaigneventtrigger cet left outer join
	[${dbxschemaname}].eventtriggers et 
	on 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode);
END
else if (LEN(@_status)>0)
BEGIN
    select * from [${dbxschemaname}].eventtriggers where eventTriggerId in (select eventTriggerId from [${dbxschemaname}].campaigneventtrigger c where campaignId in (select campaignId  from [${dbxschemaname}].campaigndefinition c where campaignStatus =@_status));
END
else
BEGIN
    select * from [${dbxschemaname}].[eventtriggers] where eventTriggerId  in (select [eventTriggerId] from [${dbxschemaname}].[campaigneventtrigger]);
END

select * from [${dbxschemaname}].[datacontext] where [dataContextId]  in (select [dataContextId]  from [${dbxschemaname}].[profilecondition] where [profileId] in (select [profileId]  from [${dbxschemaname}].[campaignprofile]) );


END;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[locationservices_view];
GO

CREATE VIEW [${dbxschemaname}].[locationservices_view] AS 
         SELECT 
            
               location.id AS Location_id, 
               location.Name AS Location_Name, 
               location.DisplayName AS Location_Display_Name, 
               location.Description AS Location_Description, 
               location.PhoneNumber AS Location_Phone_Number, 
               location.EmailId AS Location_EmailId, 
               address.latitude AS Location_Latitude, 
               address.logitude AS Location_Longitude, 
               address.id AS Location_Address_id, 
               location.IsMainBranch AS Location_IsMainBranch, 
               location.isMobile AS Location_IsMobile, 
               location.Status_id AS Location_Status_id, 
               location.Type_id AS Location_Type_id, 
               location.Code AS Location_Code, 
               location.softdeleteflag AS Location_DeleteFlag, 
               location.WorkSchedule_id AS Location_WorkScheduleId, 
               facility.id AS Facility_id, 
               facility.code AS Facility_code, 
               facility.name AS Facility_name, 
			   facility.companyLegalUnit AS companyLegalUnit,
               facility.description AS Facility_description, 
               
                  (
                     SELECT 
                        string_agg(currency.code ,',')
                     FROM [${dbxschemaname}].currency 
                        JOIN locationcurrency
                     on currency.code = locationcurrency.currency_code AND locationcurrency.Location_id = location.id
                  ) AS currencies, 
               
                  (
                     SELECT 
                        string_agg(CAST(customersegment.[type] as nvarchar(max)) , ',')
                     FROM customersegment 
                        JOIN locationcustomersegment
                     on customersegment.id = locationcustomersegment.segment_id AND locationcustomersegment.Location_id = location.id
                  ) AS Location_CustomerSegement, 
               weekday.StartTime AS Weekday_StartTime, 
               weekday.EndTime AS Weekday_EndTime, 
               sunday.StartTime AS Sunday_StartTime, 
               sunday.EndTime AS Sunday_EndTime, 
               saturday.StartTime AS Saturday_StartTime, 
               saturday.EndTime AS Saturday_EndTime, 
               (address.addressLine1+
                  ', '+
                  isnull(address.cityName,'')+ 
                  ', '+
                     (
                        SELECT 
                           region.Name
                        FROM region
                        WHERE (region.id = address.Region_id)
                     )+
                  ', '+
                     (
                        SELECT 
                           country.Name
                        FROM country
                        WHERE country.id IN 
                           (
                              SELECT 
                                 region.Country_id
                              FROM region
                              WHERE (region.id = address.Region_id)
                           )
                     )+
                  ', '+
                  address.zipCode) AS ADDRESS
         FROM [${dbxschemaname}].location 
            LEFT JOIN [${dbxschemaname}].locationfacility ON location.id = locationfacility.Location_id
            LEFT JOIN [${dbxschemaname}].facility ON locationfacility.facility_id = facility.id
            LEFT JOIN [${dbxschemaname}].dayschedule  weekday ON location.WorkSchedule_id = weekday.WorkSchedule_id AND weekday.WeekDayName = 'MONDAY'
     LEFT JOIN [${dbxschemaname}].dayschedule  sunday ON location.WorkSchedule_id = sunday.WorkSchedule_id AND sunday.WeekDayName = 'SUNDAY'
            LEFT JOIN [${dbxschemaname}].dayschedule  saturday ON location.WorkSchedule_id = saturday.WorkSchedule_id AND saturday.WeekDayName = 'SATURDAY' 
            JOIN [${dbxschemaname}].address ON address.id = location.Address_id;
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_request_search_proc]  
   
   @_dateInitialPoint varchar(50),
   @_dateFinalPoint varchar(50),
   @_requestStatusID varchar(50),
   @_requestCategory varchar(50),
   @_offset varchar(50),
   @_sortCriteria varchar(50),
   @_sortOrder varchar(50),
   @_requestAssignedTo varchar(50),
   @_searchKey varchar(50),
   @_messageRepliedBy varchar(50),
   @_recordsPerPage varchar(50),
   @_queryType varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE @selectClause nvarchar(max)
	  DECLARE @stmt nvarchar(max)
	  DECLARE @whereclause nvarchar(max)
	  DECLARE @joinCustomer int

      SET @selectClause = 
         CASE 
            WHEN (ISNULL(@_queryType, N'') = 'count') THEN N'count(cr.id) AS cnt'
            ELSE N'cr.id AS customerrequest_id'
         END
	
      SET @stmt = (N'SELECT ') + (@selectClause) + (N' FROM [${dbxschemaname}].customerrequest cr ')
 
      SET @whereclause = N' WHERE 1=1 '
 
      SET @joinCustomer = 0
	  

      IF @_dateInitialPoint <> '' AND @_dateFinalPoint <> ''

         SET @whereclause = 
            (@whereclause)
             + 
            (N'  AND cr.createdts >= ')
             + 
            ((QUOTENAME((@_dateInitialPoint), '''')))
             + 
            (N'  AND ')
             + 
            (N' cr.createdts <= ')
             + 
            ((QUOTENAME((@_dateFinalPoint), '''')))

      ELSE 
         IF @_dateInitialPoint <> ''

            SET @whereclause = (@whereclause) + (+N'  AND cr.createdts = ''') + CONVERT(datetime,@_dateInitialPoint,120)+''''
         ELSE 
            BEGIN
               IF @_dateFinalPoint <> ''

                  SET @whereclause = (@whereclause) + (+N'  AND cr.createdts = ''') + CONVERT(datetime,@_dateFinalPoint,120)+'''' 
            END


      IF @_requestStatusID <> ''
 
         SET @whereclause = (@whereclause) + (N'  AND cr.Status_id IN  ( ') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_requestStatusID)) + (+N'  ) ')


      IF @_requestCategory <> ''
   
         SET @whereclause = (@whereclause) + (+N'  AND cr.RequestCategory_id = ') + ''''+(@_requestCategory)+''''

      IF @_requestAssignedTo <> ''
 
         SET @whereclause = (@whereclause) + (+N'  AND cr.AssignedTo = ') + ''''+(@_requestAssignedTo)+''''


      IF @_messageRepliedBy <> ''
         BEGIN


            SET @stmt = (@stmt) + (N'JOIN [${dbxschemaname}].requestmessage ON (cr.id = requestmessage.CustomerRequest_id) ')
 
            SET @whereclause = (@whereclause) + (+N'  AND requestmessage.RepliedBy_id = ') + ''''+(@_messageRepliedBy)+''''


         END

      IF @_searchKey <> ''
         BEGIN

            SET @joinCustomer = 1


            SET @_searchKey = N'%' + @_searchKey + N'%'

            SET @whereclause = 
               (@whereclause)
                + 
               (+N'  AND '+N'  ('+N'    cr.Customer_id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR cr.id LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N' OR customer.UserName LIKE ')
                + 
               ''''+(@_searchKey)+''''
                + 
               (N')')

         END
	DECLARE @sortColumn nvarchar(max)
      IF @_queryType <> 'count'
         BEGIN

            IF @_sortCriteria = 'customer_Fullname'

               SET @joinCustomer = 1

            SET @sortColumn = N'cr.lastmodifiedts'

            IF (@_sortCriteria = 'customerrequest_Customer_id')

               SET @sortColumn = N'cr.Customer_id'

            ELSE 
               IF (@_sortCriteria = 'customerrequest_AssignedTo')
        
                  SET @sortColumn = N'cr.AssignedTo'

               ELSE 
                  IF (@_sortCriteria = 'customerrequest_createdts')

                     SET @sortColumn = N'cr.createdts'
     
                  ELSE 
                     IF (@_sortCriteria = 'customerrequest_RequestCategory_id')
            
                        SET @sortColumn = N'cr.RequestCategory_id'
                     
                     ELSE 
                        IF (@_sortCriteria = 'customer_Fullname')
                      
                           SET @sortColumn = 'customer.FirstName,'+'customer.LastName'
                       
                        ELSE 
                           IF (@_sortCriteria = 'customerrequest_Status_id')
                             
                              SET @sortColumn = N'cr.Status_id'
                              
                           ELSE 
                              BEGIN
                                 IF (@_sortCriteria = 'customerrequest_AssignedTo_Name')
                                   
                                    SET @sortColumn = N'systemuser.FirstName,'+'systemuser.LastName'
                                    
                              END

            SET @whereclause = (@whereclause) + (+N' ORDER BY '+N'  ') + (
               CASE 
                  WHEN (ISNULL(@sortColumn,'') = '') THEN N'cr.lastmodifiedts'
                  ELSE @sortColumn
               END) + (N' ') + (
               CASE 
                  WHEN ((ISNULL(@sortColumn,'') = '') OR @_sortOrder = '') THEN N'DESC'
                  ELSE @_sortOrder
               END)
           
            SET @whereclause = (@whereclause) + (N' offset ') + (
               CASE 
                  WHEN (@_offset = '') THEN N'0'
                  ELSE @_offset
               END) + (N' rows fetch next ') + (
               CASE 
                  WHEN (@_recordsPerPage = '') THEN N'10'
                  ELSE @_recordsPerPage
               END) + (N' rows only')
          
         END

      IF @joinCustomer = 1
        
         SET @stmt = (@stmt) + (N'JOIN [${dbxschemaname}].customer ON (cr.Customer_id = customer.id) ')
      

      IF @_sortCriteria = 'customerrequest_AssignedTo_Name'
     
         SET @stmt = (@stmt) + (N'LEFT JOIN [${dbxschemaname}].systemuser ON (cr.AssignedTo = systemuser.id) ')
         
      SET @stmt = (@stmt) + (@whereclause)
	
	EXEC(@stmt)
  
   END



GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_corecustomer_actions_delete_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_corecustomer_actions_delete_proc] 
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50), 
   @_accountId nvarchar(50),
   @_actionsCSV nvarchar(max)
   

AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId

	IF(@_accountId='')
     BEGIN
      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE 
         [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId
  
	END
	IF @_accountId IS NOT NULL
	    BEGIN 

		 DELETE 
         FROM [${dbxschemaname}].accountlevelactionlimit
         WHERE 
         [${dbxschemaname}].accountlevelactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].accountlevelactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @_coreCustomerId AND
		 [${dbxschemaname}].accountlevelactionlimit.accountId = @_accountId

         
      END

      
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS

BEGIN -- proc level
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON

	  DECLARE @index int
	  DECLARE @query nvarchar(max)
	  DECLARE @numOfRecords int
	  DECLARE @id nvarchar(255) = N''
      DECLARE @tempLimitValue DECIMAL(20,2)
      DECLARE @group_concat_max_len bigint
	  DECLARE @serviceDefinitionId nvarchar(255) = N''
	  DECLARE @recordsData nvarchar(max) = N''
	  DECLARE @contractId nvarchar(max) = N''
	  DECLARE @customerId nvarchar(max) = N''
	  DECLARE @accountId nvarchar(max) = N''
	  DECLARE @featureId nvarchar(max) = N''
	  DECLARE @actionId nvarchar(max) = N''
	  DECLARE @isNewAction nvarchar(max)= N''
	  DECLARE @legalEntityId nvarchar(max)= N''
	  DECLARE @limitId nvarchar(max) = N''
	  DECLARE @limitValue nvarchar(max) = N''
	  DECLARE @limitAtFI nvarchar(max) = N''
	  DECLARE @contarctFeatures nvarchar(max) = N''  
	  DECLARE @limitATServiceDefinition nvarchar(max) = N''
	  DECLARE @recordsDataWithoutLimits nvarchar(max) = N'' 
	  DECLARE @serviceDefinitionActions nvarchar(max) = N'' 
	  DECLARE @existingActionLimitRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords1 nvarchar(max) = N''
	  DECLARE @existingAccountActionLimitRecords nvarchar(max) = N''

	  set @index = 0;
	  set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1; 
	  SET @_queryInput = REPLACE(@_queryInput, '"', ''''); 

	    WHILE (1 = 1)
      
         BEGIN -- while level

         set @index = @index + 1;

		  IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN -- outermost level

		   set @recordsData = concat('''',newid(),'''',',', [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 ));
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',2 ), '''', -1 );
		   set @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',3 ), ',''', -1 );   
		   set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',4 ), ',''', -1 ); 
		   set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',5), ',''', -1 );           
	       set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',6), ',''', -1 );           
	       set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',7), ',''', -1 ); 
		   set @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8), ',''', -1 );
	       set @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',9), ',''', -1 );          
	       set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ',''',-1 ), '''', 1 );              
	       set @recordsDataWithoutLimits = concat([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8),''''); 

		   IF @serviceDefinitionId is null or @serviceDefinitionId = '' BEGIN
				SET @serviceDefinitionId = (SELECT servicedefinitionId from [${dbxschemaname}].[contract] WHERE id = @contractId);
		   END 
			IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN --111
					   SET @limitAtFI = (SELECT actionlimit.value FROM [${dbxschemaname}].[actionlimit] WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

					   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId        
					        AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
						AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);                   
					  					   

					   IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                              
                            SET @limitValue = @tempLimitValue;
                  END ; --111
			     /* IF 
                     CASE 
                        WHEN NOT 
                           CASE 
                              WHEN (@tempLimitValue) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE 
                           0
                     END <> 0 AND @tempLimitValue <> ''
                     */
                     


			SET @contarctFeatures = 
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId  AND 
							   [${dbxschemaname}].contractfeatures.companyLegalUnit = @legalEntityId  
                     )

		   SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND 
			 [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @legalEntityId 
                     ) 

             

			SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
                     )

					 SET @existingAccountActionLimitRecords = (SELECT String_agg(CAST(acl.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit acl WHERE acl.contractId = @contractId AND 
						acl.coreCustomerId = @customerId AND 
						acl.featureId = @featureId AND acl.actionId = @actionId AND 
						acl.limitTypeId = @limitId AND acl.companyLegalUnit = @legalEntityId);

					  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId 
                     )
					 
					  SET @existingActionRecords1 = 
                     (
                     			 
                        SELECT String_agg(CAST(accountlevelactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit WHERE 
                               [${dbxschemaname}].accountlevelactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @customerId AND
							   [${dbxschemaname}].accountlevelactionlimit.accountId =@accountId AND
                               [${dbxschemaname}].accountlevelactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].accountlevelactionlimit.actionId = @actionId AND 
								[${dbxschemaname}].accountlevelactionlimit.companyLegalUnit = @legalEntityId							   
                       
                     ) 
                     
            
			IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> '' AND (@accountId IS NULL or @accountId = '')
                  
				  BEGIN -- 9
					IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
						ELSE
							BEGIN -- 6
							 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.accountId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction,companyLegalUnit) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                  END
                                
							    
								   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
	        					         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.accountId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction, contractactionlimit.companyLegalUnit,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                     
                                        END 
                                    END -- 6
							END  -- 9
					ELSE		
						BEGIN --11
							IF @existingAccountActionLimitRecords IS NOT NULL AND @existingAccountActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingAccountActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
					ELSE
					BEGIN -- 13

						 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords1 IS NULL OR @existingActionRecords1 = '')
                                     BEGIN
                                         SET @query =(N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,contractactionlimit.actionId, accountlevelactionlimit.isNewAction, accountlevelactionlimit.companyLegalUnit) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                    END
                                    
                           
							   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
                                           SET @query = (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,accountlevelactionlimit.actionId,accountlevelactionlimit.isNewAction,accountlevelactionlimit.companyLegalUnit,accountlevelactionlimit.limitTypeId,accountlevelactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                     
                                        END 
							
				                      END -- 13 
				              END --11
							
			             
						exec(@query);
						SET @query = '';
						
   END -- outermost level 

   END -- while level
   END -- proc level  

  GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[signatorygroup_update_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[signatorygroup_update_proc]
                                                     @_sigGroupValues nvarchar(max),
                                                     @_newSigValues nvarchar(max),
                                                     @_deleteSigValues nvarchar(max)
AS
BEGIN
    DECLARE @index1 int = 0
    DECLARE @length1 bigint
    DECLARE @signatoriesComma nvarchar(max)
    DECLARE @custId nvarchar(100)
    DECLARE @sigGroupId nvarchar(max)
    DECLARE @SigGroupName varchar(max)
    DECLARE @sigGroupDes nvarchar(max)
    DECLARE @signatory nvarchar(max)
    DECLARE @query nvarchar(max)
    DECLARE @sigCreatedBy nvarchar(max)
    set @_sigGroupValues = REPLACE(@_sigGroupValues, '""', '"');
    set @sigGroupId = [dbxdb].SUBSTRING_INDEX(@_sigGroupValues, ';', 1);
    set @SigGroupName = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_sigGroupValues, ';', 2), ';', -1);
    set @SigGroupDes = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_sigGroupValues, ';', 3), ';', -1);
    set @sigCreatedBy = [dbxdb].SUBSTRING_INDEX(@_sigGroupValues, ';', -1);
    set @SigGroupId = REPLACE(@SigGroupId, '"', '''');
    IF @SigGroupName IS NOT NULL AND @SigGroupName != ''
BEGIN
            set @SigGroupName = REPLACE(@SigGroupName, '"', '''');
            set @sigCreatedBy = REPLACE(@sigCreatedBy, '"', '''');
            set @query = ('UPDATE [dbxdb].signatorygroup SET signatorygroup.signatoryGroupName = ') + @SigGroupName +
                         (' , signatorygroup.lastmodifiedts = CURRENT_TIMESTAMP, signatorygroup.modifiedby = ') +
                         @sigCreatedBy + (' WHERE signatorygroup.signatoryGroupId = ') + @sigGroupId + ('');
execute (@query);
END
    IF @sigGroupDes IS NOT NULL AND @sigGroupDes != ''
BEGIN
            set @sigGroupDes = REPLACE(@sigGroupDes, '"', '''');
            set @sigCreatedBy = REPLACE(@sigCreatedBy, '"', '''');
            set @query =
                    ('UPDATE [dbxdb].signatorygroup SET signatorygroup.signatoryGroupDescription = ') + @sigGroupDes +
                    (' , signatorygroup.lastmodifiedts = CURRENT_TIMESTAMP, signatorygroup.modifiedby = ') +
                    @sigCreatedBy + (' WHERE signatorygroup.signatoryGroupId = ') + @sigGroupId + ('');
execute (@query);
END

    IF @_newSigValues IS NOT NULL AND @_newSigValues != ''
BEGIN
            set @length1 = LEN(@_newSigValues) - LEN(REPLACE(@_newSigValues, ',', '')) + 1;
            set @index1 = 0;
            WHILE (1 = 1)
BEGIN
                    set @index1 = @index1 + 1;
                    IF @index1 = @length1 + 1
                        BREAK
                    ELSE
BEGIN
                            set @signatory =
                                    [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_newSigValues, ',', @index1), ',',
                                                            -1);
                            set @signatoriesComma = REPLACE(@signatory, ';', ',');
                            set @signatoriesComma = REPLACE(@signatoriesComma, '"', '''');
                            set @query =
                                    ('INSERT INTO [dbxdb].customersignatorygroup(customersignatorygroup.customerSignatoryGroupId, customersignatorygroup.signatoryGroupId, customersignatorygroup.customerId, customersignatorygroup.createdby) values (') +
                                    @signatoriesComma + (')');
execute (@query);
CONTINUE
END
END
END

    IF @_deleteSigValues IS NOT NULL AND @_deleteSigValues != ''
BEGIN
            set @length1 = LEN(@_deleteSigValues) - LEN(REPLACE(@_deleteSigValues, ',', '')) + 1;
            set @index1 = 0;
            WHILE (1 = 1)
BEGIN
                    set @index1 = @index1 + 1
                    IF @index1 = @length1 + 1
                        BREAK
                    ELSE
BEGIN
                            set @signatory =
                                    [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_deleteSigValues, ',', @index1),
                                                            ',', -1);
                            set @sigGroupId = [dbxdb].SUBSTRING_INDEX(@signatory, ';', 1);
                            set @custId = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@signatory, ';', 2), ';', -1);
                            set @sigGroupId = REPLACE(@sigGroupId, '"', '''');
                            set @custId = REPLACE(@custId, '"', '''');
                            set @query =
                                    ('DELETE FROM [dbxdb].customersignatorygroup WHERE customersignatorygroup.signatoryGroupId =') +
                                    @sigGroupId + ('AND customersignatorygroup.customerId=') + @custId + ('')
                            execute (@query);
CONTINUE
END
END
END
END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_corecustomer_actions_delete_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_corecustomer_actions_delete_proc] 
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50), 
   @_accountId nvarchar(50),
   @_actionsCSV nvarchar(max)
   

AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId

	IF(@_accountId='')
     BEGIN
      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE 
         [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId
  
	END
	IF @_accountId IS NOT NULL
	    BEGIN 

		 DELETE 
         FROM [${dbxschemaname}].accountlevelactionlimit
         WHERE 
         [${dbxschemaname}].accountlevelactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].accountlevelactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @_coreCustomerId AND
		 [${dbxschemaname}].accountlevelactionlimit.accountId = @_accountId

         
      END

      
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS

BEGIN -- proc level
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON

	  DECLARE @index int
	  DECLARE @query nvarchar(max)
	  DECLARE @numOfRecords int
	  DECLARE @id nvarchar(255) = N''
      DECLARE @tempLimitValue DECIMAL(20,2)
      DECLARE @group_concat_max_len bigint
	  DECLARE @serviceDefinitionId nvarchar(255) = N''
	  DECLARE @recordsData nvarchar(max) = N''
	  DECLARE @contractId nvarchar(max) = N''
	  DECLARE @customerId nvarchar(max) = N''
	  DECLARE @accountId nvarchar(max) = N''
	  DECLARE @featureId nvarchar(max) = N''
	  DECLARE @actionId nvarchar(max) = N''
	  DECLARE @isNewAction nvarchar(max)= N''
	  DECLARE @legalEntityId nvarchar(max)= N''
	  DECLARE @limitId nvarchar(max) = N''
	  DECLARE @limitValue nvarchar(max) = N''
	  DECLARE @limitAtFI nvarchar(max) = N''
	  DECLARE @contarctFeatures nvarchar(max) = N''  
	  DECLARE @limitATServiceDefinition nvarchar(max) = N''
	  DECLARE @recordsDataWithoutLimits nvarchar(max) = N'' 
	  DECLARE @serviceDefinitionActions nvarchar(max) = N'' 
	  DECLARE @existingActionLimitRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords1 nvarchar(max) = N''
	  DECLARE @existingAccountActionLimitRecords nvarchar(max) = N''

	  set @index = 0;
	  set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1; 
	  SET @_queryInput = REPLACE(@_queryInput, '"', ''''); 

	    WHILE (1 = 1)
      
         BEGIN -- while level

         set @index = @index + 1;

		  IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN -- outermost level

		   set @recordsData = concat('''',newid(),'''',',', [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 ));
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',2 ), '''', -1 );
		   set @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',3 ), ',''', -1 );   
		   set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',4 ), ',''', -1 ); 
		   set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',5), ',''', -1 );           
	       set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',6), ',''', -1 );           
	       set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',7), ',''', -1 ); 
		   set @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8), ',''', -1 );
	       set @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',9), ',''', -1 );          
	       set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ',''',-1 ), '''', 1 );              
	       set @recordsDataWithoutLimits = concat([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8),''''); 

		   IF @serviceDefinitionId is null or @serviceDefinitionId = '' BEGIN
				SET @serviceDefinitionId = (SELECT servicedefinitionId from [${dbxschemaname}].[contract] WHERE id = @contractId);
		   END 
			IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN --111
					   SET @limitAtFI = (SELECT actionlimit.value FROM [${dbxschemaname}].[actionlimit] WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

					   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId        
					        AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
						AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);                   
					  					   

					   IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                              
                            SET @limitValue = @tempLimitValue;
                  END ; --111
			     /* IF 
                     CASE 
                        WHEN NOT 
                           CASE 
                              WHEN (@tempLimitValue) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE 
                           0
                     END <> 0 AND @tempLimitValue <> ''
                     */
                     


			SET @contarctFeatures = 
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId  AND 
							   [${dbxschemaname}].contractfeatures.companyLegalUnit = @legalEntityId  
                     )

		   SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND 
			 [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @legalEntityId 
                     ) 

             

			SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
                     )

					 SET @existingAccountActionLimitRecords = (SELECT String_agg(CAST(acl.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit acl WHERE acl.contractId = @contractId AND 
						acl.coreCustomerId = @customerId AND 
						acl.featureId = @featureId AND acl.actionId = @actionId AND 
						acl.limitTypeId = @limitId AND acl.companyLegalUnit = @legalEntityId);

					  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId 
                     )
					 
					  SET @existingActionRecords1 = 
                     (
                     			 
                        SELECT String_agg(CAST(accountlevelactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit WHERE 
                               [${dbxschemaname}].accountlevelactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @customerId AND
							   [${dbxschemaname}].accountlevelactionlimit.accountId =@accountId AND
                               [${dbxschemaname}].accountlevelactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].accountlevelactionlimit.actionId = @actionId AND 
								[${dbxschemaname}].accountlevelactionlimit.companyLegalUnit = @legalEntityId							   
                       
                     ) 
                     
            
			IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> '' AND (@accountId IS NULL or @accountId = '')
                  
				  BEGIN -- 9
					IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
						ELSE
							BEGIN -- 6
							 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.accountId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction,companyLegalUnit) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                  END
                                
							    
								   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
	        					         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.accountId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction, contractactionlimit.companyLegalUnit,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                     
                                        END 
                                    END -- 6
							END  -- 9
					ELSE		
						BEGIN --11
							IF @existingAccountActionLimitRecords IS NOT NULL AND @existingAccountActionLimitRecords <> ''
					BEGIN
					SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingAccountActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
					ELSE
					BEGIN -- 13

						 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords1 IS NULL OR @existingActionRecords1 = '')
                                     BEGIN
                                         SET @query =(N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,contractactionlimit.actionId, accountlevelactionlimit.isNewAction, accountlevelactionlimit.companyLegalUnit) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                    END
                                    
                           
							   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
                                           SET @query = (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.accountId,accountlevelactionlimit.featureId,accountlevelactionlimit.actionId,accountlevelactionlimit.isNewAction,accountlevelactionlimit.companyLegalUnit,accountlevelactionlimit.limitTypeId,accountlevelactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                     
                                        END 
							
				                      END -- 13 
				              END --11
							
			             
						exec(@query);
						SET @query = '';
						
   END -- outermost level 

   END -- while level
   END -- proc level  

  GO
  
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_pending_approvalsqueue_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_pending_approvalsqueue_proc] @_customerId nvarchar(50),
                                                             @_transactionIds nvarchar(max),
                                                             @_requestIds nvarchar(max),
                                                             @_featureactionlist nvarchar(max)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @combinedIds nvarchar(max)
    DECLARE @alreadyApprovedIds nvarchar(max)
    DECLARE @companyId nvarchar(max)
    DECLARE @customerMatrixIds nvarchar(max)
    DECLARE @customerGroupIds nvarchar(max)
    DECLARE @approvalRequestIds nvarchar(max)
    DECLARE @features nvarchar(max)
    DECLARE @monetaryActions nvarchar(max)
    DECLARE @groupIds nvarchar(max)
    DECLARE @strLen nvarchar(20)
    DECLARE @reqIds nvarchar(max)
    DECLARE @SubStrLen nvarchar(20)
    DECLARE @temp_featureactionlist TABLE
                                    (
                                        id NVARCHAR(512)
                                    );
    DECLARE @temp_combinedIds TABLE
                              (
                                  id NVARCHAR(512)
                              );
    --     DECLARE @logmessage nvarchar(max)
--     DECLARE @drop_temp nvarchar(100)

--     SET @logmessage =
--             'Customer id = ' + @_customerId + ' Featurelist = ' + @_featureactionlist + ' TransactionList = ' +
--             @_transactionIds + ' Requestids = ' + @_requestIds;
    --EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc',
    -- @AdditionalInfo = 'Incoming Arguemnts ' + @logmessage;

    SET @combinedIds = (select String_agg(customer.id, ',')
                        from [${dbxschemaname}].customer
                        where [${dbxschemaname}].customer.combinedUserId = @_customerId)
    IF @combinedIds is NULL
        SET @combinedIds = @_customerId;
    ELSE
        SET @combinedIds = (@_customerId + ',' + @combinedIds);

    IF (@combinedIds IS NULL OR @combinedIds = '')
        SET @combinedIds = '';
    ELSE
        SET @combinedIds = @combinedIds;

    INSERT INTO @temp_combinedIds (id) (SELECT VALUE FROM STRING_SPLIT(@combinedIds, ','));

    --@temp_combinedIds

    IF (@_transactionIds IS NULL OR @_transactionIds = '')
        SET @_transactionIds = '';
    ELSE
        SET @_transactionIds = @_transactionIds;

    IF (@_requestIds IS NULL OR @_requestIds = '')
        SET @_requestIds = '';
    ELSE
        SET @_requestIds = @_requestIds;

    IF (@_featureactionlist IS NULL OR @_featureactionlist = '')
        GOTO MAINLABEL$leave

    INSERT INTO @temp_featureactionlist (id) SELECT VALUE FROM STRING_SPLIT(@_featureactionlist, ',');

    SET @customerMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)), ',')
                              FROM [${dbxschemaname}].customerapprovalmatrix
                              WHERE customerapprovalmatrix.customerId IN
                                    (select id from @temp_combinedIds))

    IF @customerMatrixIds IS NULL
        SET @customerMatrixIds = ''

    SET @customerGroupIds = (SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)), ',')
                             FROM [${dbxschemaname}].customersignatorygroup
                             WHERE customersignatorygroup.customerId IN
                                   (select id from @temp_combinedIds));
    IF @customerGroupIds IS NULL
        SET @customerGroupIds = ''

    SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)), ',')
                               FROM [${dbxschemaname}].bbactedrequest
                               WHERE bbactedrequest.createdby IN (select id from @temp_combinedIds)
                                 AND bbactedrequest.action != 'Pending'
                                 AND bbactedrequest.softdeleteflag = 0);

    IF @alreadyApprovedIds IS NULL
        SET @alreadyApprovedIds = '';

    IF @customerMatrixIds = ''
        SET @approvalRequestIds = '';
    ELSE
        SET @approvalRequestIds =
                (SELECT String_agg(CAST(RQAMX.requestId as nvarchar(max)), ',')
                 FROM [${dbxschemaname}].requestapprovalmatrix AS RQAMX
                          INNER JOIN [${dbxschemaname}].approvalmatrix AS APMX ON (RQAMX.approvalMatrixId = APMX.id and APMX.id IN
                                                                                                             (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ',')))
                          INNER JOIN [${dbxschemaname}].approvalrule AS APRL ON (APMX.approvalruleId = APRL.id)
                 WHERE RQAMX.isGroupRule = 0
                   AND RQAMX.approvalMatrixId IN (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ','))
                   AND RQAMX.requestId NOT IN (SELECT VALUE FROM STRING_SPLIT(@alreadyApprovedIds, ','))
                   AND ((APRL.numberOfApprovals = -1 AND
                         RQAMX.receivedApprovals < (SELECT COUNT(DISTINCT (customerapprovalmatrix.customerId))
                                                    FROM [${dbxschemaname}].customerapprovalmatrix
                                                    WHERE customerapprovalmatrix.approvalMatrixId = RQAMX.approvalMatrixId)) OR
                        (APRL.numberOfApprovals != -1 AND RQAMX.receivedApprovals < APRL.numberOfApprovals)));

    IF @approvalRequestIds IS NULL
        SET @approvalRequestIds = '';

    -- GROUP BASED APPROVAL Trxns
    SET @groupIds = @customerGroupIds;

    DECLARE @CurrentGroupId NVARCHAR(MAX);
    DECLARE @PendingGroupList NVARCHAR(MAX);
    SET @PendingGroupList = '';

    do_this:
    WHILE 1 = 1 BEGIN
        SET @strLen = LEN(@groupIds);
        SET @CurrentGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1);
        IF @CurrentGroupId != ''
            BEGIN
                SET @CurrentGroupId = '%' + @CurrentGroupId + '%';

                IF @PendingGroupList = ''
                    SET @PendingGroupList =
                            ' signatorygrouprequestmatrix.pendingGroupList LIKE ''' + @CurrentGroupId + ''''
                ELSE
                    SET @PendingGroupList =
                            @PendingGroupList + ' OR signatorygrouprequestmatrix.pendingGroupList LIKE ''' +
                            @CurrentGroupId + ''''
            END

        SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
        SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
        IF LEN(@groupIds) <= 0
            BEGIN
                BREAK;
            END
    END;

    IF @PendingGroupList != ''
        BEGIN
            DECLARE @sql_stmt NVARCHAR(MAX);
            SET @sql_stmt = ' SELECT @reqIds_out = (String_agg(CAST(signatorygrouprequestmatrix.requestId as nvarchar(max)) , '',''))
            FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE signatorygrouprequestmatrix.isApproved = 0 AND ( ' +
                            @PendingGroupList + ' ) ;';
            EXECUTE sp_executesql @sql_stmt, N'@reqIds_out NVARCHAR(MAX) OUTPUT', @reqIds_out = @reqIds OUTPUT
        END

    IF @reqIds IS NULL
        SET @reqIds = ''

    IF (@approvalRequestIds = '' OR @approvalRequestIds IS NULL)
        SET @approvalRequestIds = @reqIds;
    ELSE
        BEGIN
            IF @reqIds != ''
                SET @approvalRequestIds = CONCAT(@approvalRequestIds, ',', @reqIds);
        END

    SET @approvalRequestIds = [${dbxschemaname}].DISTINCT_VALUE(@approvalRequestIds); -- Remove duplicates
    DECLARE @requestIds nvarchar(max)
    DECLARE @companyRequestIds nvarchar(max)
    DECLARE @query nvarchar(max)

    SET @query = ''

    IF (@_transactionIds != '')
        BEGIN
            SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                             FROM [${dbxschemaname}].featureaction
                             WHERE featureaction.id IN (select id FROM @temp_featureactionlist))

            IF @features IS NULL
                SET @features = ''

            IF @features != ''
                SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
                                        FROM [${dbxschemaname}].featureaction
                                        WHERE featureaction.Feature_id IN
                                              (select value FROM STRING_SPLIT(@features, ',')))

            IF @monetaryActions IS NULL
                SET @monetaryActions = ''

            IF @monetaryActions != ''
                SET @query = ' bbrequest.transactionId IN ( select VALUE from STRING_SPLIT(''' + @_transactionIds + ''', '','')) AND
				               bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions +
                             ''', '','')); '
        END
    ELSE
        BEGIN
            IF @_requestIds = ''
                BEGIN

                    SET @companyId = (SELECT String_agg(CAST(concat(contractcustomers.contractId, '_',
                                                                    contractcustomers.coreCustomerId) as nvarchar(max)),
                                                        ',')
                                      FROM [${dbxschemaname}].contractcustomers
                                      WHERE contractcustomers.customerId = @_customerId)

                    IF @companyId IS NULL
                        SET @companyId = '';
                    ELSE
                        SET @companyId = [${dbxschemaname}].DISTINCT_VALUE(@companyId);

                    SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                                     FROM [${dbxschemaname}].featureaction
                                     WHERE featureaction.id IN
                                           (select id FROM @temp_featureactionlist));

                    IF @features IS NULL
                        SET @features = ''

                    IF @features != ''
                        SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
                                                FROM [${dbxschemaname}].featureaction
                                                WHERE featureaction.Feature_id IN
                                                      (select value FROM STRING_SPLIT(@features, ',')))

                    IF @monetaryActions IS NULL
                        SET @monetaryActions = ''

                    IF @companyId != '' AND @monetaryActions != ''
                        SET @query = ' bbrequest.companyId IN (SELECT value FROM STRING_SPLIT(''' + @companyId + ''', '','')) and
                                       bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' +
                                     @monetaryActions + ''', '','')); '
                END
            ELSE
                BEGIN
                    SET @requestIds = @_requestIds
                    IF @requestIds != ''
                        SET @query = ' bbrequest.requestId IN ( select VALUE from STRING_SPLIT(''' + @requestIds +
                                     ''', '','')); '
                END
        END

    DECLARE @select_statement nvarchar(max)
    DECLARE @req_ids NVARCHAR(MAX)

    IF @query != ''
        BEGIN
            SET @select_statement =
                    'select @reqout_ids = (string_agg(CAST(bbrequest.requestId as nvarchar(max)), '','')) from [${dbxschemaname}].bbrequest where ' +
                    @query;
            EXECUTE sp_executesql @select_statement, N'@reqout_ids NVARCHAR(MAX) OUTPUT', @reqout_ids = @req_ids OUTPUT
        END
    ELSE
        GOTO MAINLABEL$leave

    IF @req_ids IS NULL
        GOTO MAINLABEL$leave


    DECLARE @temp_customeraction TABLE
                                 (
                                     id         NVARCHAR(65),
                                     Account_id NVARCHAR(65),
                                     Action_id  NVARCHAR(255),
                                     companyId  NVARCHAR(255)
                                 );

    INSERT INTO @temp_customeraction (id, Account_id, Action_id, companyId)
    SELECT customeraction.id,
           customeraction.Account_id,
           customeraction.Action_id,
           (customeraction.contractId + '_' + customeraction.coreCustomerId) as companyId
    from [${dbxschemaname}].customeraction
    where customeraction.Customer_id IN (select id from @temp_combinedIds)
      and customeraction.isAllowed = 1
      AND customeraction.softdeleteflag = 0;

    SELECT bbrequest.requestId,
           bbrequest.transactionId,
           bbrequest.status,
           bbrequest.featureActionId,
           bbrequest.isGroupMatrix,
           bbrequest.additionalMeta,
           bbrequest.createdts,

           IIF(EXISTS (SELECT 1
                       FROM @temp_combinedIds
                       WHERE id = bbrequest.createdby), 'true', 'false')                     as amICreator,

           (IIF(((select COUNT(1) FROM STRING_SPLIT(@approvalRequestIds, ',') WHERE VALUE = bbrequest.requestId) >
                 0) AND
                ((select TOP 1 tca.id
                  from @temp_customeraction tca
                  -- Remove @ symbol, in case if we use CTE variable instead of TABLE variable here
                  where ((tca.Account_id = bbrequest.accountId) OR -- NULL MUST NOT BE COMPARED WITH NULL by =
                         ((tca.Account_id IS NULL) AND (bbrequest.accountId IS NULL)))
                    AND tca.Action_id = (select distinct(featureaction.approveFeatureAction)
                                         from [${dbxschemaname}].featureaction
                                         where featureaction.id = bbrequest.featureActionId
                                           and featureaction.approveFeatureAction IS NOT NULL)
                    AND tca.companyId = bbrequest.companyId) IS NOT NULL), 'true', 'false')) as amIApprover,

           (IIF((select COUNT(1) FROM STRING_SPLIT(@alreadyApprovedIds, ',') WHERE VALUE = bbrequest.requestId) > 0,
                'true', 'false'))                                                            as actedByMeAlready,

           (select count(DISTINCT (createdby))
            from [${dbxschemaname}].bbactedrequest
            where bbactedrequest.action = 'Approved'
              and bbactedrequest.requestId = bbrequest.requestId
              AND bbactedrequest.softdeleteflag = 0)
                                                                                             as receivedApprovals,

           null                                                                              as requiredApprovals

    FROM [${dbxschemaname}].bbrequest
    WHERE bbrequest.requestId IN (SELECT value FROM STRING_SPLIT(@req_ids, ','));

End
    MAINLABEL$leave:
go

CREATE NONCLUSTERED INDEX [IX_customerapprovalmatrix_customerId_approvalMatrixId]
    ON [${dbxschemaname}].customerapprovalmatrix (customerId)
    INCLUDE (approvalMatrixId);

CREATE NONCLUSTERED INDEX [IX_customersignatorygroup_customerId_signatoryGroupId]
    ON [${dbxschemaname}].customersignatorygroup (customerId)
    INCLUDE (signatoryGroupId);

CREATE NONCLUSTERED INDEX [IX_bbactedrequest_action_softdeleteflag_requestId]
    ON [${dbxschemaname}].bbactedrequest (action, softdeleteflag)
    INCLUDE (requestId);

CREATE NONCLUSTERED INDEX [IX_bbactedrequest_requestId]
    ON [${dbxschemaname}].bbactedrequest (requestId);

CREATE NONCLUSTERED INDEX [IX_contractcustomers_customerId_contractId_coreCustomerId]
    ON [${dbxschemaname}].contractcustomers (customerId)
    INCLUDE (contractId, coreCustomerId);

CREATE NONCLUSTERED INDEX [IX_customerapprovalmatrix_approvalMatrixId]
    ON [${dbxschemaname}].customerapprovalmatrix (approvalMatrixId);

CREATE NONCLUSTERED INDEX [IX_featureaction_id_Feature_id]
    ON [${dbxschemaname}].featureaction (id)
    INCLUDE (Feature_id);

CREATE NONCLUSTERED INDEX [IX_bbrequest_companyId_requestId]
    ON [${dbxschemaname}].bbrequest (companyId)
    INCLUDE (requestId);

CREATE NONCLUSTERED INDEX [IX_customeraction_CustomerId_isAllowed_softdeleteflag]
    ON [${dbxschemaname}].customeraction (Customer_id, isAllowed, softdeleteflag);


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_search_proc]
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50), 
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint,
   @_legalEntityId varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

    SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist, customer.companyLegalUnit as companyLegalUnit, customerlegalentity.legalEntityId as branchId')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN [${dbxschemaname}].customerlegalentity on (customer.id = customerlegalentity.customer_id and customerlegalentity.legalEntityId ='+''''+@_legalEntityId+''''+ ') JOIN (SELECT customer.id FROM [${dbxschemaname}].customer  '+ (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')  where customer.id =  customer.id'
                      ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') where customer.id =  customer.id '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' left join [${dbxschemaname}].card on (card.User_id = customer.id)  LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)  where customer.id =  customer.id'
                        ELSE N''
                     END)
            
                 
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                         (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
            + 
                           (N' and customer.IDValue = ')
                        + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id)
					LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id)
					LEFT JOIN [${dbxschemaname}].country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customerlegalentity.legalEntityId, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory,address.addressLine1, address.addressLine2, city.Name, address.zipCode, country.Name, customer.isEnrolled, customer.companyLegalUnit ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
       
                
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''

                          SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
       exec(@queryStatement2)
     END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc] @_contractId NVARCHAR(128),
                                                                                          @_coreCustomerId NVARCHAR(128),
                                                                                          @_featureActionId NVARCHAR(128),
                                                                                          @_accountIds NVARCHAR(128),
                                                                                          @_limitTypeId NVARCHAR(128),
                                                                                          @_limitValuesJSON NVARCHAR(MAX)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @accountIds NVARCHAR(max);
    DECLARE @isAccountLevelUpdate NVARCHAR(128);
    DECLARE @isAccountLevelFeature NVARCHAR(128);
    DECLARE @isGroupMatrix NVARCHAR(128);
    DECLARE @currency NVARCHAR(max);
    DECLARE @templateIdsForForceDelete NVARCHAR(max);
    DECLARE @templateIds NVARCHAR(max);
    DECLARE @sqlStmt NVARCHAR(max);
    DECLARE @limitsLength NVARCHAR(max);
    DECLARE @currentLimit NVARCHAR(max);
    DECLARE @limitIndex NVARCHAR(128);
    DECLARE @approvalRuleId NVARCHAR(128);
    DECLARE @lowerLimit NVARCHAR(128);
    DECLARE @upperLimit NVARCHAR(128);
    DECLARE @groupList NVARCHAR(max);
    DECLARE @groupRule NVARCHAR(max);
    DECLARE @approvers NVARCHAR(max);
    DECLARE @noOfApprovers NVARCHAR(128);
    DECLARE @currentUserId NVARCHAR(128);
    DECLARE @currentJSONElementForUserId NVARCHAR(max);
    DECLARE @approverIndex NVARCHAR(128);
    DECLARE @noOfAccountIds NVARCHAR(128);
    DECLARE @lastTemplateId NVARCHAR(128);
    DECLARE @accountId NVARCHAR(128);
    DECLARE @accountIndex NVARCHAR(128);
    DECLARE @_requestMatrixDataJSON NVARCHAR(MAX);
    DECLARE @companyLegalUnit NVARCHAR(128);
    declare @singleQuote nvarchar(1);

    SET @singleQuote = '''';
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit])
                             FROM [${dbxschemaname}].[contractcustomers]
                             WHERE coreCustomerId = @_coreCustomerId
                               AND contractId = @_contractId);
    SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel])
                                  FROM [${dbxschemaname}].[featureaction]
                                  WHERE id = @_featureActionId
                                    and companyLegalUnit = @companyLegalUnit);
    --SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId);
    SET @isGroupMatrix = (SELECT DISTINCT([approvalmode].[isGroupLevel])
                          FROM [${dbxschemaname}].[approvalmode]
                          WHERE coreCustomerId = @_coreCustomerId
                            AND contractId = @_contractId);
    --SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    IF @_accountIds IS NULL OR @_accountIds = '' OR @_accountIds = '[]'
        BEGIN
            SET @isAccountLevelUpdate = 0;
            SET @accountIds = (SELECT STRING_AGG(CAST([contractaccounts].[accountId] AS NVARCHAR(max)), ',')
                               FROM [${dbxschemaname}].[contractaccounts]
                               WHERE contractId = @_contractId
                                 AND coreCustomerId = @_coreCustomerId);
        END
    ELSE
        BEGIN
            SET @accountIds = @_accountIds;
        END
    IF @isAccountLevelFeature = 0
        BEGIN
            -- customer-level feature action rule is being added/updated, hence, no account ids are associated
            SET @accountIds = '';
        END
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    SET @currency = (SELECT DISTINCT([approvalmatrixtemplate].[currency])
                     FROM [${dbxschemaname}].[approvalmatrixtemplate]
                     WHERE contractId = @_contractId
                       AND coreCustomerId = @_coreCustomerId
                       and companyLegalUnit = @companyLegalUnit);
    IF @isAccountLevelUpdate = 0
        BEGIN
            -- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
            -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
            SET @templateIdsForForceDelete =
                    (SELECT STRING_AGG(CAST([approvalmatrixtemplate].[id] AS NVARCHAR(max)), ',')
                     FROM [${dbxschemaname}].[approvalmatrixtemplate]
                     WHERE contractId = @_contractId
                       AND coreCustomerId = @_coreCustomerId
                       AND actionId = @_featureActionId
                       AND isGroupMatrix = @isGroupMatrix
                       AND limitTypeId = @_limitTypeId);
            IF @templateIdsForForceDelete IS NOT NULL
                BEGIN
                    SET @templateIds = CONCAT('''', REPLACE(@templateIdsForForceDelete, ',', ''','''), '''');
                    SET @sqlStmt =
                            CONCAT('DELETE FROM [${dbxschemaname}].[customerapprovalmatrixtemplate] WHERE approvalMatrixId IN (',
                                   @templateIds, ')');
                    EXEC (@sqlStmt);
                    SET @sqlStmt =
                            CONCAT('DELETE FROM [${dbxschemaname}].[signatorygroupmatrixtemplate] WHERE approvalMatrixId IN (',
                                   @templateIds, ')');
                    EXEC (@sqlStmt);
                    DELETE
                    FROM [${dbxschemaname}].[approvalmatrixtemplate]
                    WHERE contractId = @_contractId
                      AND coreCustomerId = @_coreCustomerId
                      AND actionId = @_featureActionId
                      AND isGroupMatrix = @isGroupMatrix
                      AND limitTypeId = @_limitTypeId;
                END
        END
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix]
            SET softdeleteflag = 1
            WHERE contractId = @_contractId
              AND coreCustomerId = @_coreCustomerId
              AND actionId = @_featureActionId
              AND isGroupMatrix = @isGroupMatrix
              AND limitTypeId = @_limitTypeId;
        END
    ELSE
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix]
            SET softdeleteflag = 1
            WHERE contractId = @_contractId
              AND coreCustomerId = @_coreCustomerId
              AND actionId = @_featureActionId
              AND isGroupMatrix = @isGroupMatrix
              AND limitTypeId = @_limitTypeId
              AND [${dbxschemaname}].FIND_IN_SET(accountId, @accountIds) <> 0;
        END
    SET @limitsLength = (select COUNT(*) FROM OPENJSON(@_limitValuesJSON));
    --select 'LimitsLength' + @limitsLength;
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    WHILE 1 = 1
        BEGIN
            IF @limitIndex = @limitsLength
                BEGIN
                    BREAK;
                END
            ELSE
                BEGIN

                    SET @currentLimit =
                            (SELECT json.value FROM OPENJSON(@_limitValuesJSON) as json where json.[key] = @limitIndex);
                    --SET @currentLimit = JSON_VALUE(@_limitValuesJSON, CONCAT('$[', @limitIndex, ']'));
                    --select 'Current Limit:  ' + @currentLimit;
                    SET @limitIndex = @limitIndex + 1;
                    --select '@currentLimit' + @currentLimit;
                    --SET @approvalRuleId = (select json.value from OPENJSON(@currentLimit) as json where json.[key] = 'groupRule');
                    SET @approvalRuleId = REPLACE(JSON_VALUE(@currentLimit, '$.approvalruleId'), '"', '');
                    SET @lowerLimit = JSON_VALUE(@currentLimit, '$.lowerlimit');
                    SET @upperLimit = JSON_VALUE(@currentLimit, '$.upperlimit');

                    IF @isGroupMatrix = 1
                        BEGIN
                            SET @groupList = REPLACE(JSON_VALUE(@currentLimit, '$.groupList'), '"', '');
                            SET @groupRule = REPLACE(JSON_VALUE(@currentLimit, '$.groupRule'), '"', '');
                            SET @approvalRuleId = (SELECT DISTINCT([approvalrule].[id])
                                                   FROM [${dbxschemaname}].[approvalrule]
                                                   WHERE numberOfApprovals = REPLACE(REPLACE(@groupRule, '[', ''), ']', ''));
                        END;
                    SET @approvers = JSON_QUERY(@currentLimit, '$.approvers');
                    -- error handling
                    -- if lowerLimit or upperLimit is null, then return an error signal
                    -- if group mode, then if groupList or groupRule is null, then return error signal
                    -- if user-based mode, then if approvalRuleId or approvers are null, then return error
--                         select @approvalRuleId;
                    IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@approvers IS NOT NULL AND REPLACE(@approvers, '"', '') = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
                    IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0
                        BEGIN
                            --                             INSERT INTO dbxdb.approvalmatrix(name, contractId, actionId, accountId, approvalruleId,
--                                                              limitTypeId, lowerlimit, upperlimit,
--                                                              createdby, modifiedby, softdeleteflag, invalid,
--                                                              coreCustomerId, isGroupMatrix, currency, companyLegalUnit)
--                             VALUES (concat(@_contractId, @_featureActionId), @_contractId, @_featureActionId,
--                                     NULLIF(@_accountIds, ''), @approvalRuleId, @_limitTypeId,
--                                     CAST(@lowerLimit AS DECIMAL(20, 2)),
--                                     CAST(@upperLimit AS DECIMAL(20, 2)), NULL, NULL, 0, 0, @_coreCustomerId,
--                                     @isGroupMatrix, @currency, @companyLegalUnit);
--                             SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix]);
--
                            INSERT INTO [${dbxschemaname}].[approvalmatrixtemplate](contractId, coreCustomerId, actionId,
                                                                         limitTypeId, isGroupMatrix, approvalruleId,
                                                                         lowerLimit, upperLimit, currency,
                                                                         companyLegalUnit)
                            VALUES (@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId, @isGroupMatrix,
                                    (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20, 2)),
                                    CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);
                            SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrixtemplate]);

                            IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                BEGIN
                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            INSERT INTO [${dbxschemaname}].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
                                            VALUES (@lastTemplateId, @groupList, @groupRule);
                                        END
                                    ELSE
                                        BEGIN
                                            SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
                                            SET @currentUserId = '';
                                            SET @approverIndex = 0;
                                            WHILE (1 = 1)
                                                BEGIN
                                                    IF @approverIndex = @noOfApprovers
                                                        BEGIN
                                                            BREAK;
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            SET @currentJSONElementForUserId =
                                                                    (SELECT JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'));
                                                            SET @currentUserId = (SELECT JSON_VALUE(
                                                                                                 JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'),
                                                                                                 '$.approverId') AS 'approverId');
                                                            INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId)
                                                            VALUES (@currentUserId, @lastTemplateId);
                                                            SET @approverIndex = @approverIndex + 1;
                                                        END
                                                END
                                            CONTINUE;
                                        END
                                END
                        END
                    IF @isAccountLevelFeature = 0
                        BEGIN
                            --                             BEGIN
--                                 INSERT INTO [${dbxschemaname}].[approvalmatrixtemplate](contractId, coreCustomerId, actionId,
--                                                                              limitTypeId,
--                                                                              isGroupMatrix, approvalruleId, lowerLimit,
--                                                                              upperLimit, currency, companyLegalUnit)
--                                 VALUES (@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId,
--                                         @isGroupMatrix,
--                                         (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20, 2)),
--                                         CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);
--                                 SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrixtemplate]);
--                                 IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
--                                     BEGIN
--                                         IF @isGroupMatrix = 1
--                                             BEGIN
--                                                 INSERT INTO [${dbxschemaname}].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
--                                                 VALUES (@lastTemplateId, @groupList, @groupRule);
--                                             END
--                                         ELSE
--                                             BEGIN
--                                                 SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
--                                                 SET @currentUserId = '';
--                                                 SET @approverIndex = 0;
-- --                                                 select @approvers as apid;
--                                                 WHILE (1 = 1)
--                                                     BEGIN
--                                                         IF @approverIndex = @noOfApprovers
--                                                             BEGIN
--                                                                 BREAK;
--                                                             END
--                                                         ELSE
--                                                             BEGIN
--                                                                 --                                                                 SET @currentUserId = REPLACE(JSON_VALUE(
-- --                                                                                                      JSON_VALUE(@approvers, CONCAT('$[', @approverIndex, ']')),
-- --                                                                                                      '$.approverId'),
-- --                                                                                              '"', '');
--                                                                 SET @currentJSONElementForUserId =
--                                                                         (SELECT JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'));
--                                                                 SET @currentUserId = (SELECT JSON_VALUE(
--                                                                                                      JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'),
--                                                                                                      '$.approverId') AS 'approverId');
--
--                                                                 INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId)
--                                                                 VALUES (@currentUserId, @lastTemplateId);
--                                                                 SET @approverIndex = @approverIndex + 1;
--                                                             END
--                                                     END
--                                             END
--                                     END
--                             END
                            IF NOT (@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND
                                    @lowerLimit = -1 AND @upperLimit = -1)
                                BEGIN
                                    INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId,
                                                                         accountId, limitTypeId, isGroupMatrix,
                                                                         approvalruleId, lowerLimit, upperLimit,
                                                                         currency, companyLegalUnit)
                                    VALUES (CONCAT(@_contractId, '-', @_featureActionId), @_contractId,
                                            @_coreCustomerId,
                                            @_featureActionId, NULL, @_limitTypeId, @isGroupMatrix, (@approvalRuleId),
                                            CAST(@lowerLimit AS DECIMAL(20, 2)), CAST(@upperLimit AS DECIMAL(20, 2)),
                                            @currency,
                                            @companyLegalUnit);
                                    SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix]);
                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                        BEGIN
                                            IF @isGroupMatrix = 1
                                                BEGIN
                                                    INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList, groupRule)
                                                    VALUES (@lastTemplateId, @groupList, (@groupRule));
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@approvers));
                                                    SET @currentUserId = '';
                                                    SET @approverIndex = 0;
                                                    WHILE (1 = 1)
                                                        BEGIN
                                                            IF @approverIndex = @noOfApprovers
                                                                BEGIN
                                                                    BREAK;
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    --                                                                     SET @currentUserId = REPLACE(JSON_VALUE(
--                                                                                                          JSON_VALUE(@approvers, CONCAT('$[', @approverIndex, ']')),
--                                                                                                          '$.approverId'),
--                                                                                                  '"', '');
                                                                    SET @currentJSONElementForUserId =
                                                                            (SELECT JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'));
                                                                    SET @currentUserId = (SELECT JSON_VALUE(
                                                                                                         JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'),
                                                                                                         '$.approverId') AS 'approverId');

                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                    SET @approverIndex = @approverIndex + 1;
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                    ELSE
                        BEGIN
                            -- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
                            SET @noOfAccountIds = LEN(@accountIds) - LEN(REPLACE(@accountIds, ',', '')) + 1;
                            SET @accountIndex = 0;
                            WHILE (1 = 1)
                                BEGIN
                                    SET @accountIndex = @accountIndex + 1;
                                    IF @accountIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK;
                                        END
                                    ELSE
                                        BEGIN
                                            SET @accountId = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@accountIds, ',', @accountIndex), ',', -1);
                                            -- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId);
                                            -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                                            IF NOT (@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND
                                                    @lowerLimit = -1 AND @upperLimit = -1)
                                                BEGIN
                                                    --select '@accountId'+@accountId;
                                                    BEGIN
                                                        INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId,
                                                                                             coreCustomerId, actionId,
                                                                                             accountId,
                                                                                             limitTypeId, isGroupMatrix,
                                                                                             approvalruleId, lowerLimit,
                                                                                             upperLimit, currency,
                                                                                             companyLegalUnit)
                                                        VALUES (CONCAT(@_contractId, '-', @_featureActionId),
                                                                @_contractId, @_coreCustomerId,
                                                                @_featureActionId, @accountId, @_limitTypeId,
                                                                @isGroupMatrix,
                                                                @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20, 2)),
                                                                CAST(@upperLimit AS DECIMAL(20, 2)), @currency,
                                                                @companyLegalUnit);

                                                    END
                                                    --COMMIT;
                                                    SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix]);
                                                    --select '@lastTemplateId......' + @lastTemplateId;
                                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                                        BEGIN
                                                            IF @isGroupMatrix = 1
                                                                BEGIN
                                                                    set @sqlStmt = CONCAT('INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList,groupRule)
										VALUES(', @singleQuote, @lastTemplateId, @singleQuote, ',', @singleQuote,
                                                                                          @groupList, @singleQuote, ',',
                                                                                          @singleQuote, @groupRule,
                                                                                          @singleQuote, ')');
                                                                    --select '@sqlStmt' + @sqlStmt;
                                                                    EXEC (@sqlStmt);
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @noOfApprovers =
                                                                            (SELECT COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON));
                                                                    SET @currentUserId = '';
                                                                    SET @approverIndex = 0;
                                                                    WHILE (1 = 1)
                                                                        BEGIN
                                                                            IF @approverIndex = @noOfApprovers
                                                                                BEGIN
                                                                                    BREAK;
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    --                                                                                     SET @currentUserId = REPLACE(
--                                                                                             JSON_VALUE(
--                                                                                                     JSON_VALUE(@approvers, CONCAT('$[', @approverIndex, ']')),
--                                                                                                     '$.approverId'),
--                                                                                             '"', '');
                                                                                    SET @currentJSONElementForUserId =
                                                                                            (SELECT JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'));
                                                                                    SET @currentUserId =
                                                                                            (SELECT JSON_VALUE(
                                                                                                            JSON_QUERY(@approvers, '$[' + CAST(@approverIndex AS VARCHAR) + ']'),
                                                                                                            '$.approverId') AS 'approverId');

                                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                                    VALUES (@currentUserId,
                                                                                            @lastTemplateId);
                                                                                    SET @approverIndex = @approverIndex + 1;
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END;
go



CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_search_proc]  
 @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50), 
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint,
   @_legalEntityId varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

  SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist, customer.companyLegalUnit as companyLegalUnit, customerlegalentity.legalEntityId as branchId')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN [${dbxschemaname}].customerlegalentity on (customer.id = customerlegalentity.customer_id and customerlegalentity.legalEntityId ='+''''+@_legalEntityId+''''+ ') JOIN (SELECT customer.id FROM [${dbxschemaname}].customer where customer.id =  customer.id '+ (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')  where customer.id =  customer.id'
                   ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') where customer.id =  customer.id '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' left join [${dbxschemaname}].card on (card.User_id = customer.id)  LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)  where customer.id =  customer.id'
                        ELSE N''
                     END)
            
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
            (@queryStatement)
                            + 
                         (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
            + 
                           (N' and customer.IDValue = ')
                        + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id)
					LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id)
					LEFT JOIN [${dbxschemaname}].country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 
                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customerlegalentity.legalEntityId, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory,address.addressLine1, address.addressLine2, city.Name, address.zipCode, country.Name, customer.isEnrolled, customer.companyLegalUnit ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
       
                
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              

                          SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                       -- IF @_sortDirection <> ''
                         
                       --    SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)

                       -- SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
       exec(@queryStatement2)
     END;
GO








USE [dbxdb];
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_create_proc](
    @_actionIds NVARCHAR(max),
    @_contractId NVARCHAR(50),
    @_accountIds NVARCHAR(max),
    @_cif NVARCHAR(50)
) AS
BEGIN
    DECLARE @accountList VARCHAR(max) = 0;
    DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT';
    DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT';
    DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT';
    DECLARE @accountIndex INTEGER = 0;
    DECLARE @actionIndex INTEGER = 0;
    DECLARE @typeId VARCHAR(max) = '';
    DECLARE @numOfAccounts int;
    DECLARE @numOfActions int;
    DECLARE @accountId NVARCHAR(50);
    DECLARE @actionId NVARCHAR(255);
    DECLARE @companyLegalUnit NVARCHAR(50);


    IF @_actionIds IS NULL OR @_actionIds = ''
        GOTO MAINLABEL$leave

    IF @_contractId IS NULL OR @_contractId = ''
        GOTO MAINLABEL$leave

    IF @_cif IS NULL OR @_cif = ''
        GOTO MAINLABEL$leave

    IF @_accountIds IS NULL OR @_accountIds = ''
        GOTO MAINLABEL$leave


    set @companyLegalUnit = (SELECT companyLegalUnit
                             from contractcorecustomers
                             where coreCustomerId = @_cif and contractId = @_contractId);
    set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
    set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
    getAccount:
    WHILE 1 = 1 BEGIN
        set @accountIndex = @accountIndex + 1;
        IF @accountIndex = @numOfAccounts + 1
            BEGIN
                BREAK;
            End
        Else
            Begin
                set @accountId =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIndex), ',', -1);
                set @actionIndex = 0;
                getAction:
                WHILE 1 = 1 BEGIN
                    set @actionIndex = @actionIndex + 1;
                    IF @actionIndex = @numOfActions + 1
                        BEGIN
                            BREAK;
                        End
                    Else
                        Begin
                            set @actionId =
                                    [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex),
                                                            ',', -1);
                            SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
                            IF @typeId = 'MONETARY'
                                BEGIN
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_1, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL',
                                            @companyLegalUnit);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_2, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL',
                                            @companyLegalUnit);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_3, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL',
                                            @companyLegalUnit);
                                END
                            ELSE
                                IF @typeId = 'NON_MONETARY'
                                    BEGIN
                                        INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                    coreCustomerId, approvalruleId, companyLegalUnit)
                                        VALUES (@_contractId,
                                                concat(@actionId, '_', @accountId, '_', 'NON_MONETARY_LIMIT', '_',
                                                       @_contractId), @accountId, @actionId, 'NON_MONETARY_LIMIT',
                                                @_cif, 'NO_APPROVAL', @companyLegalUnit);
                                    END
                        END
                END;
                set @accountList = CONCAT(@accountId, ',', @accountList);
            END
    END;

    SET @accountList = (select SUBSTRING(@accountList, 1, (LEN(@accountList) - 1)));
    select @accountList as accountList;

    MAINLABEL$leave:

END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_delete_proc] @_contractId nvarchar(50),
                                                              @_cif nvarchar(50),
                                                              @_filterColumnIds nvarchar(max),
                                                              @_filterColumnName nvarchar(50)
AS
BEGIN

    DECLARE @companyLegalUnit NVARCHAR(50);
    set @companyLegalUnit = (SELECT companyLegalUnit from [${dbxschemaname}].contract WHERE [${dbxschemaname}].contract.id = @_contractId);

    SET XACT_ABORT ON
    SET NOCOUNT ON
    IF @_filterColumnName = 'actionId'
        begin
            DELETE
            FROM [${dbxschemaname}].approvalmatrix
            WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
              AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
              AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
              AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.actionId, @_filterColumnIds) <> 0
        end
    ELSE
        IF @_filterColumnName = 'accountId'
            BEGIN
                DELETE
                FROM [${dbxschemaname}].approvalmatrix
                WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
                  AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
                  AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
                  AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.accountId, @_filterColumnIds) <> 0
            END
        ELSE
            IF @_filterColumnName = 'cif'
                BEGIN
                    DELETE
                    FROM [${dbxschemaname}].approvalmatrix
                    WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
                      AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
                      AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.coreCustomerId, @_filterColumnIds) <> 0
                END
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc] @_contractId nvarchar(50),
                                                                     @_cif nvarchar(50),
                                                                     @_accountIds nvarchar(max),
                                                                     @_actionId nvarchar(50),
                                                                     @_limitTypeId nvarchar(50)
AS
BEGIN

    DECLARE @companyLegalUnit NVARCHAR(50);
    SET @companyLegalUnit =
            (SELECT companyLegalUnit from [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.id = @_actionId);

    SET XACT_ABORT ON
    SET NOCOUNT ON
    UPDATE [${dbxschemaname}].approvalmatrix
    SET softdeleteflag = 1
    WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
      AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
      AND [${dbxschemaname}].FIND_IN_SET(CAST(approvalmatrix.accountId AS nvarchar(max)), @_accountIds) > 0
      AND [${dbxschemaname}].approvalmatrix.actionId = @_actionId
      AND [${dbxschemaname}].approvalmatrix.limitTypeId = @_limitTypeId
      AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc] @_matrixValues NVARCHAR(max),
                                                                          @_signatorymatrixValues NVARCHAR(max)
AS
BEGIN

    DECLARE @index1 INTEGER = 0;
    DECLARE @index2 INTEGER = 0;
    DECLARE @length INTEGER = 0;
    DECLARE @matrixRecord nvarchar(max);
    DECLARE @matrixComma varchar(max);
    DECLARE @query nvarchar(max);
    DECLARE @sigValues nvarchar(max);
    DECLARE @id nvarchar(max);
    DECLARE @groupList nvarchar(max);
    DECLARE @groupRule nvarchar(max);
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);
    set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;


    WHILE (1 = 1)
        BEGIN
            set @index1 = @index1 + 1;
            IF @index1 = @length + 1
                BREAK
            ELSE
                BEGIN
                    set @matrixRecord =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                    set @matrixComma = REPLACE(@matrixRecord, ';', ',');
                    set @matrixComma = REPLACE(@matrixComma, '"', '''');
                    set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 2), ',', -1);
                    set @contractId = REPLACE(@contractId, '''', '');
                    set @companyLegalUnit =
                            (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
                    set @query = concat(
                            'INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.approvalruleId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit,approvalmatrix.currency, approvalmatrix.companyLegalUnit) VALUES (',
                            @matrixComma, ',''', @companyLegalUnit, ''');');


                    execute (@query);
                    set @sigValues =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_signatorymatrixValues, '#', @index1), '#',
                                                    -1);
                    SET @id = @@IDENTITY;
                    SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1);
                    SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1);
                    set @id = REPLACE(@id, '"', '''');
                    set @groupList = REPLACE(@groupList, '"', '''');
                    set @groupRule = REPLACE(@groupRule, '"', '''');
                    set @query =
                                ('INSERT INTO [${dbxschemaname}].signatorygroupmatrix(signatorygroupmatrix.approvalMatrixId,signatorygroupmatrix.groupList,signatorygroupmatrix.groupRule, signatorygroupmatrix.companyLegalUnit) VALUES (') +
                                @id + ',' + '''' + @groupList + '''' + ',' + '''' + @groupRule + '''' + ')';
                    execute (@query);
                    CONTINUE
                END
        END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrixtemplate_create_proc](
    @_matrixValues NVARCHAR(MAX),
    @_matrixApprover NVARCHAR(MAX),
    @_isGroupMatrix INTEGER
) AS
BEGIN
    DECLARE @index1 INTEGER = 0;
    DECLARE @index2 INTEGER = 0;
    DECLARE @length BIGINT = 0;
    DECLARE @length2 INTEGER = 0;
    DECLARE @matrixRecord NVARCHAR(MAX);
    DECLARE @matrixComma NVARCHAR(MAX);
    DECLARE @query NVARCHAR(MAX);
    DECLARE @id NVARCHAR(50);
    DECLARE @customerIds NVARCHAR(255);
    DECLARE @customerIdsComma NVARCHAR(255);
    DECLARE @customerId NVARCHAR(50);
    DECLARE @sigValues NVARCHAR(255);
    DECLARE @groupList NVARCHAR(255);
    DECLARE @groupRule NVARCHAR(255);
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);

    set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;
    getValues:
    WHILE 1 = 1 BEGIN
        set @index1 = @index1 + 1;
        IF @index1 = @length + 1
            BEGIN
                BREAK;
            end
        else
            begin
                set @matrixRecord =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                set @matrixComma = REPLACE(@matrixRecord, ';', ',');
                SET @matrixComma = replace(@matrixComma, '"', '''');
                set @contractId = [${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 1);
                set @contractId = REPLACE(@contractId, '''', '');
                set @companyLegalUnit = (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
                set @query = concat(
                        'INSERT INTO [${dbxschemaname}].approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,currency,isGroupMatrix,companyLegalUnit) VALUES (',
                        @matrixComma, ',''', @companyLegalUnit, ''');');
                execute (@query);

                SET @id = @@IDENTITY

                IF @_isGroupMatrix = 0
                    BEGIN
                        set @customerIds =
                                [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, ',', @index1), ',',
                                                        -1);
                        IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0)
                            BEGIN
                                set @customerIdsComma = REPLACE(@customerIds, ';', ',');
                                set @length2 = LEN(@customerIdsComma) - LEN(REPLACE(@customerIdsComma, ',', '')) + 1;
                                set @index2 = 0;
                                getCustomerIds:
                                WHILE 1 = 1 BEGIN
                                    set @index2 = @index2 + 1;
                                    IF @index2 = @length2 + 1
                                        BEGIN
                                            BREAK;
                                        end
                                    else
                                        begin
                                            set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1);
                                            INSERT INTO [${dbxschemaname}].customerapprovalmatrixtemplate(customerId, approvalMatrixId)
                                            values (@customerId, @id);
                                            CONTINUE
                                        END
                                END;
                            END
                        CONTINUE
                    END
                ELSE
                    IF @_isGroupMatrix = 1
                        BEGIN
                            set @sigValues =
                                    [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, '#', @index1),
                                                            '#', -1);
                            SET @id = @@IDENTITY
                            SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1);
                            SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1);
                            INSERT INTO [${dbxschemaname}].signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule)
                            values (@id, @groupList, @groupRule);
                        END
            END
    END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_create_proc] @_matrixValues nvarchar(max),
                                                      @_approverIds nvarchar(max)
AS
BEGIN

    SET NOCOUNT ON

    DECLARE @index1 int = 0
    DECLARE @length bigint
    DECLARE @index2 int = 0
    DECLARE @id int
    DECLARE @customerIds nvarchar(max)
    DECLARE @customerIdsComma nvarchar(max)
    DECLARE @length2 int
    DECLARE @customerId nvarchar(100)
    DECLARE @matrixRecord nvarchar(max)
    DECLARE @matrixComma varchar(max)
    DECLARE @query nvarchar(max)
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);

    SET @length = LEN(@_matrixValues) - LEN(replace(@_matrixValues, ',', '')) + 1

    WHILE (1 = 1)
        BEGIN
            SET @index1 = @index1 + 1
            IF @index1 = @length + 1
                BREAK
            ELSE
                BEGIN
                    set @matrixRecord =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                    SET @matrixComma = replace(@matrixRecord, ';', ',');
                    SET @matrixComma = replace(@matrixComma, '"', '''');
                    set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 2), ',', -1);
                    set @contractId = REPLACE(@contractId, '''', '');
                    set @companyLegalUnit =
                            (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
                    SET @query = ('
                     INSERT [${dbxschemaname}].approvalmatrix(
                        approvalmatrix.name,
                        approvalmatrix.contractId,
						approvalmatrix.coreCustomerId,
                        approvalmatrix.actionId,
                        approvalmatrix.accountId,
                        approvalmatrix.approvalruleId,
                        approvalmatrix.limitTypeId,
                        approvalmatrix.lowerlimit,
                        approvalmatrix.upperlimit,
                        approvalmatrix.currency.
						approvalmatrix.companyLegalUnit
                     ) VALUES (') + (@matrixComma) + (',''') + (@companyLegalUnit) + (''');')


                    execute (@query)
                    SET @id = @@IDENTITY

                    set @customerIds =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_approverIds, ',', @index1), ',', -1);
                    IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0)
                        BEGIN
                            SET @customerIdsComma = replace(@customerIds, ';', ',')
                            SET @length2 = LEN(@customerIdsComma) - LEN(replace(@customerIdsComma, ',', '')) + 1
                            SET @index2 = 0
                            WHILE (1 = 1)
                                BEGIN
                                    SET @index2 = @index2 + 1
                                    IF @index2 = @length2 + 1
                                        BREAK
                                    ELSE
                                        BEGIN
                                            set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1);
                                            INSERT INTO [${dbxschemaname}].customerapprovalmatrix([${dbxschemaname}].customerapprovalmatrix.customerId, [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId)
                                            VALUES (@customerId, @id)
                                            CONTINUE
                                        END
                                END
                        END
                    CONTINUE
                END
        END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_ids_checkncleanup_for_safedelete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_ids_checkncleanup_for_safedelete_proc](
    @_contractId nvarchar(50),
    @_coreCustomerId nvarchar(50),
    @_featureActionId nvarchar(50),
    @_accountId nvarchar(50)
)
AS
BEGIN

    DECLARE @legalEntityId nvarchar(50);
    DECLARE @actionLevel nvarchar(50);
    DECLARE @matrixIdsSafeToDelete nvarchar(50);
    DECLARE @sqlStmt nvarchar(50);


    SET @legalEntityId = 'ALL';

    SELECT @legalEntityId = companyLegalUnit
    FROM [${dbxschemaname}].contractcorecustomers
    WHERE coreCustomerId = @_coreCustomerId
      AND contractId = @_contractId;
    SET @actionLevel = (SELECT DISTINCT(actionlevelId)
                        FROM [${dbxschemaname}].featureaction
                        WHERE id = @_featureActionId
                          AND companyLegalUnit = @legalEntityId);

    SET @matrixIdsSafeToDelete = '';

    IF @_accountId = '' OR @_accountId IS NULL
        BEGIN
            SET @matrixIdsSafeToDelete = (SELECT STRING_AGG(am.id, ',')
                                          FROM [${dbxschemaname}].approvalmatrix AS am
                                                   LEFT JOIN [${dbxschemaname}].requestapprovalmatrix AS ram ON am.id = ram.approvalMatrixId
                                                   LEFT JOIN [${dbxschemaname}].bbrequest AS br ON br.requestId = ram.requestId
                                          WHERE am.softdeleteflag = 1
                                            AND (br.status != 'Pending' OR br.status IS NULL)
                                            AND am.contractId = @_contractId
                                            AND am.coreCustomerId = @_coreCustomerId
                                            AND am.actionId = @_featureActionId);
        END
    ELSE
        BEGIN
            SET @matrixIdsSafeToDelete = (SELECT STRING_AGG(am.id, ',')
                                          FROM [${dbxschemaname}].approvalmatrix AS am
                                                   LEFT JOIN [${dbxschemaname}].requestapprovalmatrix AS ram ON am.id = ram.approvalMatrixId
                                                   LEFT JOIN [${dbxschemaname}].bbrequest AS br ON br.requestId = ram.requestId
                                          WHERE am.softdeleteflag = 1
                                            AND (br.status != 'Pending' OR br.status IS NULL)
                                            AND am.contractId = @_contractId
                                            AND am.coreCustomerId = @_coreCustomerId
                                            AND am.actionId = @_featureActionId
                                            AND am.accountId = @_accountId);
        END

    IF @matrixIdsSafeToDelete IS NOT NULL OR @matrixIdsSafeToDelete != ''
        BEGIN
            SET @matrixIdsSafeToDelete = CONCAT('''', REPLACE(@matrixIdsSafeToDelete, '\', ''''), '''');

            SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].signatorygroupmatrix WHERE approvalMatrixId IN (',
                                  @matrixIdsSafeToDelete, ')');
            EXECUTE (@sqlStmt);
            SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].customerapprovalmatrix WHERE approvalMatrixId IN (',
                                  @matrixIdsSafeToDelete, ')');
            EXECUTE (@sqlStmt);
            SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE approvalMatrixId IN (',
                                  @matrixIdsSafeToDelete, ')');
            EXECUTE (@sqlStmt);
            SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].requestapprovalmatrix WHERE approvalMatrixId IN (',
                                  @matrixIdsSafeToDelete, ')');
            EXECUTE (@sqlStmt);
            SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].approvalmatrix WHERE id IN (', @matrixIdsSafeToDelete, ')');
            EXECUTE (@sqlStmt);
        END
END;
GO

USE [${dbxschemaname}]
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].approvalmatrix_default_create_proc(
    @_actionIds NVARCHAR(max),
    @_contractId NVARCHAR(50),
    @_accountIds NVARCHAR(max),
    @_cif NVARCHAR(50)
) AS
BEGIN
    DECLARE @accountList VARCHAR(max) = 0;
    DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT';
    DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT';
    DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT';
    DECLARE @accountIndex INTEGER = 0;
    DECLARE @actionIndex INTEGER = 0;
    DECLARE @typeId VARCHAR(max) = '';
    DECLARE @numOfAccounts int;
    DECLARE @numOfActions int;
    DECLARE @accountId NVARCHAR(50);
    DECLARE @actionId NVARCHAR(255);
    DECLARE @legalEntityId NVARCHAR(50);

    IF @_actionIds IS NULL OR @_actionIds = ''
        GOTO MAINLABEL$leave

    IF @_contractId IS NULL OR @_contractId = ''
        GOTO MAINLABEL$leave

    IF @_cif IS NULL OR @_cif = ''
        GOTO MAINLABEL$leave

    IF @_accountIds IS NULL OR @_accountIds = ''
        GOTO MAINLABEL$leave

    set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
    set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
    getAccount:
    WHILE 1 = 1 BEGIN
        set @accountIndex = @accountIndex + 1;
        IF @accountIndex = @numOfAccounts + 1
            BEGIN
                BREAK;
            End
        Else
            Begin

                set @accountId =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIndex), ',', -1);
                set @actionIndex = 0;
                getAction:
                WHILE 1 = 1 BEGIN
                    set @actionIndex = @actionIndex + 1;
                    IF @actionIndex = @numOfActions + 1
                        BEGIN
                            BREAK;
                        End
                    Else
                        Begin
                            set @actionId =
                                    [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex),
                                                            ',', -1);
                            SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
                            SET @legalEntityId = (SELECT companyLegalUnit
                                                  from contractcorecustomers
                                                  where coreCustomerId = @_cif
                                                    and contractId = @_contractId);

                            IF @legalEntityId IS NULL OR @legalEntityId = ''
                                BEGIN
                                    SET @legalEntityId = 'ALL'
                                END

                            IF @typeId = 'MONETARY'
                                BEGIN
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_1, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_2, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_3, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                END
                            ELSE
                                IF @typeId = 'NON_MONETARY'
                                    BEGIN
                                        INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                    coreCustomerId, approvalruleId, companyLegalUnit)
                                        VALUES (@_contractId,
                                                concat(@actionId, '_', @accountId, '_', 'NON_MONETARY_LIMIT', '_',
                                                       @_contractId), @accountId, @actionId, 'NON_MONETARY_LIMIT',
                                                @_cif, 'NO_APPROVAL', @legalEntityId);
                                    END
                        END
                END;
                set @accountList = CONCAT(@accountId, ',', @accountList);
            END
    END;

    SET @accountList = (select SUBSTRING(@accountList, 1, (LEN(@accountList) - 1)));
    select @accountList as accountList;

    MAINLABEL$leave:

END;
GO



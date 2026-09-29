ALTER TABLE [${dbxschemaname}].[accountsstatementfiles] ADD inputPayload nvarchar(500); 
INSERT INTO [${dbxschemaname}].[eventtopicconfiguration] (eventCode, topic) values ('ADHOC_STATEMENT','/events/adhocstatement'); 
ALTER TABLE [${dbxschemaname}].[accountsstatementfiles] ADD statementType nvarchar(10) DEFAULT 'COMBINED'; 

GO


ALTER TABLE [${dbxschemaname}].[externalaccount] ADD isApproved nvarchar(1) DEFAULT '1';
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[GetExternalPayeesProc];
GO
CREATE PROCEDURE [${dbxschemaname}].[GetExternalPayeesProc]
    @_userId VARCHAR(500),
    @_legalEntityId VARCHAR(50)
AS
BEGIN
    IF @_legalEntityId != ''
    BEGIN
        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].internationalpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isInternationalAccount = 1
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            )
            AND ip.legalEntityId = @_legalEntityId

        UNION

        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].interbankpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isInternationalAccount = 0
            AND isSameBankAccount = 0
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            )
            AND ip.legalEntityId = @_legalEntityId

        UNION

        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].intrabankpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isSameBankAccount = 1
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            )
            AND ip.legalEntityId = @_legalEntityId;
    END
    ELSE
    BEGIN
        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].internationalpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isInternationalAccount = 1
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            )

        UNION

        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].interbankpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isInternationalAccount = 0
            AND isSameBankAccount = 0
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            )

        UNION

        SELECT *,
               CASE
                   WHEN EXISTS(
                           SELECT requestId
                           FROM [${dbxschemaname}].bbrequest
                           WHERE transactionId = ea.Id
                               AND status = 'Pending'
                       ) THEN 'Pending'
                   ELSE NULL
               END AS payeeRequestStatus
        FROM [${dbxschemaname}].intrabankpayee AS ip
        INNER JOIN [${dbxschemaname}].externalaccount AS ea ON ip.payeeId = ea.Id
        WHERE isSameBankAccount = 1
            AND softDelete = 0 AND isApproved=1
            AND cif IN (
                SELECT coreCustomerId
                FROM [${dbxschemaname}].customeraction
                WHERE Customer_id = @_userId
                    AND Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT'
                    AND isAllowed = 1
            );
    END
END;
Go


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_customers_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_customers_proc]
        @_customerId nvarchar(50),
        @_coreCustomerId nvarchar(50),
        @_legalEntityId nvarchar(100)
    AS BEGIN
	declare @select_statement nvarchar(MAX);
        declare @isWhereAppened nvarchar(100);
        declare @shouldAndAppend nvarchar(100);
        declare @filteredContracts nvarchar(MAX);

        SET @filteredContracts = (SELECT String_agg([contractaccounts].[contractId], ',')
        FROM [${dbxschemaname}].[contractaccounts]
        WHERE [contractaccounts].[statusDesc] != 'CLOSED' 
        AND [contractaccounts].[coreCustomerId] IN
        (SELECT [contractcustomers].[coreCustomerId] 
        FROM [${dbxschemaname}].[contractcustomers] WHERE [contractcustomers].[customerId] = @_customerId));
        SET @filteredContracts = '(select distinct value from STRING_SPLIT('''+@filteredContracts+''','',''))';
        SET @select_statement = ('(SELECT 
            contractcustomers.customerId AS customerId,
            contractcustomers.coreCustomerId AS coreCustomerId,
            contractcustomers.companyLegalUnit AS companyLegalUnit,
            contractcustomers.contractId AS contractId,
            contractcustomers.autoSyncAccounts AS autoSyncAccounts,
            contract.name AS contractName,
            contractcustomers.isPrimary AS isPrimary,
            contractcorecustomers.coreCustomerName AS coreCustomerName,
            contractcorecustomers.isBusiness AS isBusiness,
            contract.servicedefinitionId AS serviceDefinitionId,
            servicedefinition.name AS serviceDefinitionName,
            membergrouptype.description AS serviceDefinitionType,
            membergroup.id AS roleId,
            membergroup.Name AS userRole
        FROM
            (((((([${dbxschemaname}].contractcustomers
            LEFT JOIN [${dbxschemaname}].contractcorecustomers ON ((([${dbxschemaname}].contractcorecustomers.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].contractcorecustomers.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].contract ON (([${dbxschemaname}].contract.id = [${dbxschemaname}].contractcorecustomers.contractId)))
            LEFT JOIN [${dbxschemaname}].servicedefinition ON (([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].contract.servicedefinitionId)))
            LEFT JOIN [${dbxschemaname}].membergrouptype ON (([${dbxschemaname}].membergrouptype.id = [${dbxschemaname}].servicedefinition.serviceType)))
            LEFT JOIN [${dbxschemaname}].customergroup ON ((([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].contractcustomers.customerId)
                AND ([${dbxschemaname}].customergroup.contractId = [${dbxschemaname}].contractcustomers.contractId)
                AND ([${dbxschemaname}].customergroup.coreCustomerId = [${dbxschemaname}].contractcustomers.coreCustomerId))))
            LEFT JOIN [${dbxschemaname}].membergroup ON (([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id)))');

        SET @isWhereAppened = 'false';
        SET @shouldAndAppend = 'false';
        IF(@_customerId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @isWhereAppened = 'true';
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.customerId = ', ''''+@_customerId+'''');
        END;
        IF(@_legalEntityId != '') BEGIN
            IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @shouldAndAppend = 'true';
            END;
            IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' and');
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.companyLegalUnit = ',''''+@_legalEntityId)+'''';
        END;

 

        IF(@_coreCustomerId != '') BEGIN
          IF(@isWhereAppened = 'false') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' where');
                SET @shouldAndAppend = 'true';
            END;
            IF(@isWhereAppened = 'true' and @shouldAndAppend = 'true') BEGIN
                SET @select_statement = CONCAT(@select_statement , ' and');
                SET @shouldAndAppend = 'true';
            END;
          set @select_statement =  concat(@select_statement ,' contractcustomers.coreCustomerId = ',''''+@_coreCustomerId)+'''';
        END;
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in ',''+@filteredContracts+'',');'); 
        exec(@select_statement);
    END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[userId_Search_Proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[userId_Search_Proc]
 @_userName nvarchar(50)
AS
BEGIN
select
	distinct
    customerlegalentity.Customer_id as CustomerId,
	customerlegalentity.legalEntityId as legalEntityId,
	customer.Status_id as Status_id,
	customer.homeLegalEntity as homeLegalEntity,
	customer.defaultLegalEntity as defaultLegalEntity,
	customer.FirstName as FirstName,
	customer.MiddleName as MiddleName,
	customer.LastName as LastName,
	customer.UserName as UserName,
	customer.Gender as Gender,
	customer.CustomerType_id as CustomerType_id,
	customer.Ssn as Ssn,
	customer.DateOfBirth as DateOfBirth,
	customer.Ssn as Ssn,
	customer.isEnrolled as isEnrolled,
	primaryphone.Value as phnNo,
	primaryemail.Value as Email
from
	([${dbxschemaname}].customerlegalentity
left join [${dbxschemaname}].customercommunication primaryphone on
	((primaryphone.Customer_id = customerlegalentity.Customer_id)
		and (primaryphone.Type_id = 'COMM_TYPE_PHONE' )
			and primaryphone.isPrimary = '1')
left join [${dbxschemaname}].customercommunication primaryemail on
	((primaryemail.Customer_id = customerlegalentity.Customer_id)
		and (primaryemail.Type_id = 'COMM_TYPE_EMAIL')
			and primaryemail.isPrimary = '1')
left join [${dbxschemaname}].customer on
	(customer.id = customerlegalentity.Customer_id) )
where
	customer.UserName = @_userName;
END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
go

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
                            exec dbxdb.approvalmatrix_ids_checkncleanup_for_safedelete_proc
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
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_new_composite_request_in_approvalqueue_proc]
go

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

                    --SELECT @companyLegalUnit = (DISTINCT (companyLegalUnit))
                    --FROM [${dbxschemaname}].contractcorecustomers
                    --WHERE contractId = @contractId AND coreCustomerId = @cifId;
                    SET @companyLegalUnit = (SELECT DISTINCT companyLegalUnit
                                             FROM [${dbxschemaname}].contractcorecustomers
                                             WHERE contractId = @contractId
                                               AND coreCustomerId = @cifId);

                    --SELECT @isGroupMatrix = DISTINCT isGroupLevel
                    --FROM approvalmode
                    --WHERE contractId = @contractId AND coreCustomerId = @cifId;
                    SET @isGroupMatrix = (SELECT DISTINCT isGroupLevel
                                          FROM [${dbxschemaname}].approvalmode
                                          WHERE contractId = @contractId
                                            AND coreCustomerId = @cifId);


                    --DECLARE @matrixIdsJSON nvarchar(MAX) = JSON_VALUE(@cifJSON, '$.matrixIds');
                    DECLARE @matrixIdsJSON NVARCHAR(MAX);
                    SELECT @matrixIdsJSON = value FROM OPENJSON(@cifJSON) WHERE [key] = 'matrixIds';
                    --DECLARE @noOfMatrixIds int = JSON_LENGTH(@matrixIdsJSON);
                    DECLARE @noOfMatrixIds int;
                    SELECT @noOfMatrixIds = COUNT(*) FROM OPENJSON(@matrixIdsJSON);
                    IF @noOfMatrixIds != 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].bbrequest
                            (assocRequestId, transactionId, featureActionId, createdby, companyId, requiredSets,
                             receivedSets, status, accountId, isGroupMatrix, additionalMeta, companyLegalUnit)
                            VALUES (@assocRequestId, @_confirmationNumber, @_featureActionId, @_createdBy,
                                    CONCAT(@contractId, '_', @cifId), @noOfMatrixIds, 0, 'Pending', @_accountId,
                                    @isGroupMatrix, @_additionalMetaJSON, @companyLegalUnit);

                            --DECLARE @requestId int = SCOPE_IDENTITY();
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
                                    --                                     DECLARE @matrixId nvarchar(50) = JSON_VALUE(
--                                             JSON_VALUE(@matrixIdsJSON, CONCAT('$[', @matrixIdIndex, ']')), '$.id');
                                    --DECLARE @matrixIdsJSON NVARCHAR(MAX) = '[{"id":"1126"},{"id":"1127"}]';
--DECLARE @matrixIdIndex INT = 0;

DECLARE @matrixId NVARCHAR(MAX);

DECLARE @firstOpen nvarchar(max);

select @firstOpen = value FROM OPENJSON(@matrixIdsJSON) ;

select @matrixId = value FROM OPENJSON(@firstOpen) ;

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

--EXEC [${dbxschemaname}].fetch_requests_with_approvalmatrixinfo_proc @requestIds, '0', '', '0';
---Issue with proc within proc

declare @_requestIds nvarchar(max) = @requestIds;
declare @_isAssociationId nvarchar(2) = '0';
declare @_contractCifMapJSON nvarchar(max) = '';
declare @_isActiveRulesFetch nvarchar(2) = '0';


DECLARE @currentSchema NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId BIT;
	DECLARE @isActiveRulesFetch BIT;
	select @requestIds = '',@noOfContractIds = 0;
	DECLARE @sqlStmt NVARCHAR(MAX);
	DECLARE @isCifLevelFilter BIT;
	select @requestIds = '';
	DECLARE @cifIds NVARCHAR(MAX);
	select @noOfContractIds  = 0;
	select @contractIdIndex  = 0;
	select @contractJSON = '';
    select @contractId = '';
    select @cifsJSON = '';
    select @noOfCifIds = '';
    select @cifJSON = '';
    select @cifIndex = 0;
	--DECLARE @contractId NVARCHAR(MAX);
	--DECLARE @cifsJSON NVARCHAR(MAX);
	--DECLARE @noOfCifIds INT;
	--DECLARE @cifIndex INT;
	--DECLARE @cifJSON NVARCHAR(MAX);
	select @cifId = '';
	select @requestId ='';
	select @isGroupMatrix = '1';
	DECLARE @sqlSubStmt NVARCHAR(MAX);
	DECLARE @noOfRequestIds INT;
	DECLARE @requestIdIndex INT;
	DECLARE @singleQuote nvarchar(1);
    SET @singleQuote = '''';

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
		SET @requestIds = CONCAT('''', REPLACE(@_requestIds, ',', ''','''), '''');
		SET @sqlStmt = CONCAT('SELECT STRING_AGG(DISTINCT requestId, '','') WITHIN GROUP (ORDER BY requestId) INTO @requestIds FROM bbrequest WHERE assocRequestId IN (', @requestIds, ')');
		EXEC (@sqlStmt);
	END;

	IF @requestIds IS NULL
	BEGIN
		SET @requestIds = '';
	END;

	SET @isCifLevelFilter = 1;
    DECLARE @contractIds NVARCHAR(MAX);
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


    END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_pending_approvalsqueue_proc];
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
	DECLARE @logmessage nvarchar(max)
	DECLARE @drop_temp nvarchar(100)

	SET @logmessage = 'Customer id = ' + @_customerId + ' Featurelist = ' +  @_featureactionlist + ' TransactionList = ' +  @_transactionIds + ' Requestids = ' + @_requestIds;
	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Incoming Arguemnts ' + @logmessage;

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
                                        WHERE featureaction.Feature_id IN (select value FROM STRING_SPLIT(@features, ',')))

            IF @monetaryActions IS NULL
                SET @monetaryActions = ''

            IF @monetaryActions != ''
                SET @query = ' bbrequest.transactionId IN ( select VALUE from STRING_SPLIT(''' + @_transactionIds + ''', '','')) AND
				               bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '','')); '
        END
    ELSE
        BEGIN
            IF @_requestIds = ''
                BEGIN

                    SET @companyId = (SELECT String_agg(CAST(concat(contractcustomers.contractId, '_', contractcustomers.coreCustomerId) as nvarchar(max)),',')
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
                                       bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '','')); '
                END
            ELSE
                BEGIN
                    SET @requestIds = @_requestIds
                    IF @requestIds != ''
                        SET @query = ' bbrequest.requestId IN ( select VALUE from STRING_SPLIT(''' + @requestIds + ''', '','')); '
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
			SET @select_statement = 'select @reqout_ids = (string_agg(CAST(bbrequest.requestId as nvarchar(max)), '','')) from [${dbxschemaname}].bbrequest where ' + @query;
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
		id NVARCHAR(65),
		Account_id NVARCHAR(65),
		Action_id NVARCHAR(255),
		companyId NVARCHAR(255)
	);

	INSERT INTO @temp_customeraction (id, Account_id, Action_id, companyId)
	SELECT
		customeraction.id,
		customeraction.Account_id,
		customeraction.Action_id,
		(customeraction.contractId + '_' + customeraction.coreCustomerId) as companyId
	from [${dbxschemaname}].customeraction
	where
		customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(@combinedIds, ',')) and
		customeraction.isAllowed = 1 AND
		customeraction.softdeleteflag = 0;

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

	SELECT
		bbrequest.requestId,
		bbrequest.transactionId,
		bbrequest.status,
		bbrequest.featureActionId,
		bbrequest.isGroupMatrix,
		bbrequest.additionalMeta,

        (CASE
        WHEN (select COUNT(1) FROM STRING_SPLIT(@combinedIds, ',') where value = bbrequest.createdby) > 0
        THEN 'true'
        ELSE 'false'
        END) as amICreator,

        (CASE
        WHEN
        ((select COUNT(1) FROM STRING_SPLIT(@approvalRequestIds, ',') WHERE VALUE = bbrequest.requestId) > 0) AND
        ((select TOP 1 tca.id
		from @temp_customeraction tca -- Remove @ symbol, in case if we use CTE variable instead of TABLE variable here
		where
		tca.Account_id = bbrequest.accountId AND
		tca.Action_id = (select distinct(featureaction.approveFeatureAction)  from [${dbxschemaname}].featureaction where featureaction.id = bbrequest.featureActionId) AND
		tca.companyId = bbrequest.companyId) IS NOT NULL)
		THEN 'true'
		ELSE 'false'
		END) as amIApprover,

        (CASE
        WHEN (select COUNT(1) FROM STRING_SPLIT(@alreadyApprovedIds, ',') WHERE VALUE = bbrequest.requestId) > 0
        THEN 'true'
        ELSE 'false'
        END) as actedByMeAlready,

		(select count(DISTINCT(createdby)) from
		[${dbxschemaname}].bbactedrequest where bbactedrequest.action = 'Approved' and
		bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0)
		as receivedApprovals,

		null as requiredApprovals

	FROM
	[${dbxschemaname}].bbrequest WHERE
		bbrequest.requestId IN (SELECT value FROM STRING_SPLIT(@req_ids, ','));

End
    MAINLABEL$leave:
Go

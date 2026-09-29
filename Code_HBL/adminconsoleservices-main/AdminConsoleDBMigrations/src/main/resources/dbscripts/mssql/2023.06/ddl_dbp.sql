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
		FROM [contractaccounts]
		WHERE [contractaccounts].[statusDesc] != 'CLOSED' 
		AND [contractaccounts].[coreCustomerId] IN
		(SELECT [contractcustomers].[coreCustomerId] 
		FROM [contractcustomers] WHERE [contractcustomers].[customerId] = @_customerId));
		
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
        set @select_statement =  concat(@select_statement ,'and contractcustomers.contractId in (',''''+@filteredContracts+'''','));');
        exec(@select_statement);
    END;
GO	

/* Object:  StoredProcedure [${dbxschemaname}].[fetch_restrictive_featureactionlimits_legalEntityId_proc] 
*/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER  PROCEDURE [${dbxschemaname}].[fetch_restrictive_featureactionlimits_legalEntityId_proc](
@_locale nvarchar(50),
@_userId nvarchar(50),
@_serviceDefinitionId nvarchar(50),
@_roleId nvarchar(50),
@_coreCustomerId nvarchar(50),
@_accessPolicyIdList nvarchar(50),
@_legalEntityId nvarchar(50)
)
AS
BEGIN
DECLARE @select_statement NVARCHAR(max);
DECLARE @action_select_statement NVARCHAR(max);
DECLARE @action_select_statement_output NVARCHAR(max);
DECLARE @FeatureActionList NVARCHAR(max);
DECLARE @should_append_and SMALLINT;

SET @FeatureActionList = N'';
SET @select_statement = '';
SET @action_select_statement = '';



SET @select_statement = 'SELECT featureaction.id FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = ''SID_ACTION_ACTIVE'''
IF(@_accessPolicyIdList != '') BEGIN
SET @select_statement = @select_statement + ' AND [${dbxschemaname}].featureaction.accessPolicyId IN (select DISTINCT value from STRING_SPLIT(@_accessPolicyIdList, '',''))'
END;

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].servicedefinitionactionlimit
WHERE servicedefinitionactionlimit.serviceDefinitionId = ''' + @_serviceDefinitionId + ''' AND
servicedefinitionactionlimit.companyLegalUnit  =''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].servicedefinitionactionlimit.actionId
) '
END;
IF(@_roleId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].groupactionlimit
WHERE [${dbxschemaname}].groupactionlimit.Group_id = ''' + @_roleId + ''' AND
groupactionlimit.companyLegalUnit = '''+ @_legalEntityId + '''  AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].groupactionlimit.Action_id
) '
END
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].contractactionlimit
WHERE [${dbxschemaname}].contractactionlimit.coreCustomerId = ''' + @_coreCustomerId + ''' AND
contractactionlimit.companyLegalUnit = ''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].contractactionlimit.actionId
) '
END;
IF(@_userId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].customeraction
WHERE [${dbxschemaname}].customeraction.isAllowed = 1 AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].customeraction.Action_id AND
customeraction.companyLegalUnit = ''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].customeraction.Customer_id = ''' + @_userId + '''' + ')'
END
/*
IF (@_coreCustomerId != '')
SET @select_statement = @select_statement + ' AND [${dbxschemaname}].customeraction.coreCustomerId = ''' + @_coreCustomerId + ''')'
ELSE
SET @select_statement = @select_statement + ')'
END;
*/

SET @action_select_statement = @select_statement;

-- SELECT @select_statement AS 'Action_statement'

SET @select_statement = '';

SET @select_statement = '( SELECT
feature.id AS featureId,
featuredisplaynamedescription.displayName AS featureName,
featuredisplaynamedescription.displayDescription AS featureDescription,
feature.Status_id AS featureStatus,
featureaction.status AS actionStatus,
featureaction.id AS actionId,
actiondisplaynamedescription.displayName AS actionName,
actiondisplaynamedescription.displayDescription AS actionDescription,
featureaction.isAccountLevel AS isAccountLevel,
featureaction.Type_id AS typeId,
featureaction.limitgroupId as limitGroupId,
featureaction.accesspolicyId as accessPolicyId,
featureaction.companyLegalUnit as legalEntityId,
featureaction.actionlevelId as actionLevelId,
IIF(featureaction.Type_id = ''NON_MONETARY'',null,actionlimit.LimitType_id) as limitTypeId,
IIF(featureaction.Type_id = ''NON_MONETARY'',null,actionlimit.value) as fiLimitValue';
IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,servicedefinitionactionlimit.value) AS serviceLimitValue');
END ;
IF(@_roleId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,groupactionlimit.value) AS groupLimitValue');
END ;
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,contractactionlimit.value) AS coreCustomerLimitValue');
END ;
IF @_roleId != '' AND @_coreCustomerId != ''
SET @select_statement = CONCAT(@select_statement , ', IIF([${dbxschemaname}].groupactionlimit.isNewAction = ''1'' OR contractactionlimit.isNewAction = ''1'', ''1'', ''0'') as isNewAction');
ELSE
BEGIN
IF @_roleId != ''
SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].groupactionlimit.isNewAction AS isNewAction');
ELSE IF @_coreCustomerId != ''
SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].contractactionlimit.isNewAction AS isNewAction');
END ;

SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON (  featuredisplaynamedescription.companyLegalUnit = feature.companyLegalUnit AND 
featuredisplaynamedescription.Feature_id = feature.id AND [${dbxschemaname}].featuredisplaynamedescription.Locale_id = ', '''', @_locale, '''', ')
LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.companyLegalUnit = feature.companyLegalUnit AND featureaction.Feature_id = feature.id)
LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.companyLegalUnit = featureaction.companyLegalUnit AND actiondisplaynamedescription.Action_id = featureaction.id AND [${dbxschemaname}].actiondisplaynamedescription.Locale_id = ', '''', @_locale, '''' , ')');

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].servicedefinitionactionlimit ON (servicedefinitionactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND servicedefinitionactionlimit.actionId = featureaction.id  AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId , '''', ')');
END;
IF(@_roleId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND groupactionlimit.Action_id = featureaction.id AND groupactionlimit.Group_id = ' , '''', @_roleId, '''',')');
END;
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON (contractactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND contractactionlimit.actionId = featureaction.id  AND contractactionlimit.coreCustomerId = ', '''', @_coreCustomerId, '''', ')');
END ;

SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].actionlimit ON (actionlimit.companyLegalUnit = featureaction.companyLegalUnit AND actionlimit.Action_id = featureaction.id)');

SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.companyLegalUnit = ''' + @_legalEntityId + ''''+' AND  [${dbxschemaname}].featureaction.id IN ('+ @action_select_statement +')');

SET @should_append_and = 0;

IF (@_serviceDefinitionId != '' OR @_roleId != '' OR @_coreCustomerId != '') BEGIN
	SET @select_statement = CONCAT(@select_statement , ' AND ([${dbxschemaname}].featureaction.Type_id = ''NON_MONETARY'') OR ');
END ;

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , '([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId , ''')');
SET @should_append_and = 1;
END ;
IF(@_roleId != '') BEGIN
	IF(@should_append_and = 1) BEGIN
 		SET @select_statement = CONCAT(@select_statement,' AND ');
 	END ;
SET @select_statement = CONCAT(@select_statement , '([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].groupactionlimit.LimitType_id = actionlimit.LimitType_id AND groupactionlimit.Group_id = ' , '''', @_roleId, ''')');
SET @should_append_and = 1;
END ;
IF(@_coreCustomerId != '') BEGIN
	IF(@should_append_and = 1) BEGIN
 		SET @select_statement = CONCAT(@select_statement,' AND ');
 	END ;
SET @select_statement = CONCAT(@select_statement , '([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].contractactionlimit.limitTypeId = actionlimit.LimitType_id AND contractactionlimit.coreCustomerId = ' , '''', @_coreCustomerId,''')');
END ;

SET @select_statement = CONCAT(@select_statement , ')');

exec(@select_statement);

END
GO


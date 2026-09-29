DROP PROCEDURE IF EXISTS [${dbxschemaname}].[getRequestApprovers_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[getRequestApprovers_proc]
    @_requestId nvarchar(max),
    @_status nvarchar(max)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @isGroupMatrix NVARCHAR(50)
    IF @_status IS NULL
       OR @_status = ''
    BEGIN
        SET @isGroupMatrix =
        (
            SELECT isGroupMatrix
            from [${dbxschemaname}].bbrequest
            where [bbrequest].requestId = @_requestId
        );

        IF @isGroupMatrix IS NOT NULL AND @isGroupMatrix = '1'
        BEGIN
            SELECT @_requestId AS requestId,
                   csg.customerId AS approvers,
                   c.FirstName AS FirstName,
                   c.LastName AS LastName
            FROM [${dbxschemaname}].[customersignatorygroup] AS csg
                LEFT JOIN [${dbxschemaname}].[customer] AS c
                    ON (csg.[customerId] = [c].[id])
            WHERE [${dbxschemaname}].FIND_IN_SET(
                                                    [csg].signatoryGroupId,
                  (
                      SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].pendingGroupList, ']', ''),'[',''),'"',''),',')
                      FROM [${dbxschemaname}].[signatorygrouprequestmatrix]
                      WHERE [signatorygrouprequestmatrix].requestId = @_requestId
                            AND [signatorygrouprequestmatrix].isApproved = 'false'
                  )) > 0;
        END
        ELSE
            SELECT min(bb.requestId) AS requestId,
                   cam.customerId AS approvers,
                   min(c.FirstName) AS FirstName,
                   min(c.LastName) AS LastName
            FROM [${dbxschemaname}].bbrequest AS bb
                CROSS JOIN [${dbxschemaname}].requestapprovalmatrix AS ram
                CROSS JOIN [${dbxschemaname}].customerapprovalmatrix AS cam
                CROSS JOIN [${dbxschemaname}].customer AS c
            WHERE bb.requestId = ram.requestId
                  AND ram.approvalMatrixId = cam.approvalMatrixId
                  AND cam.customerId = c.id
                  AND CAST(bb.requestId AS nvarchar(max)) = @_requestId
            GROUP BY cam.customerId
            ORDER BY cam.customerId
    END
    ELSE
        SELECT bb.createdby AS approvers,
               min(c.FirstName) AS FirstName,
               min(c.LastName) AS LastName
        FROM [${dbxschemaname}].bbactedrequest AS bb
            CROSS JOIN [${dbxschemaname}].customer AS c
        WHERE bb.createdby = c.id
              AND CAST(bb.requestId AS nvarchar(max)) = @_requestId
              AND bb.status = @_status
        GROUP BY bb.createdby
        ORDER BY bb.createdby
END
GO


SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[get_feature_actions_view];
GO
CREATE VIEW [${dbxschemaname}].[get_feature_actions_view] AS
    SELECT 
        [${dbxschemaname}].[featureaction].[id] AS [actionId],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[notes] AS [notes],
        [${dbxschemaname}].[featureaction].[Type_id] AS [typeId],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaySequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[featureactionroletype].[RoleType_id] AS [actionType],
        [${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
        [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus],
        [${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[description] AS [featureDescription],
        [${dbxschemaname}].[feature].[Type_id] AS [featureType],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [featureGroup],
        [${dbxschemaname}].[feature].[DisplaySequence] AS featureDisplaySequence,
        [${dbxschemaname}].[feature].[isPrimary] AS [isFeaturePrimary],
        [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
        [${dbxschemaname}].[actiondisplaynamedescription].[Locale_id] AS [localeId],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[actionlimit].[LimitType_id] AS [limitTypeId],
        [${dbxschemaname}].[actionlimit].[value] AS [value],
        [${dbxschemaname}].[dependentactions_view].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[dependentactions_view].[featureName] AS [dependentFeatureName],
        [${dbxschemaname}].[dependentactions_view].[actionName] AS [dependentActionName],
		[${dbxschemaname}].[dependentactions_view].[featureId] AS [dependentFeatureId],
        [${dbxschemaname}].[termandcondition].[Code] AS [termsAndConditionCode],
        [${dbxschemaname}].[termandcondition].[Title] AS [termsAndConditionTitle],
        [${dbxschemaname}].[termandcondition].[Description] AS [termsAndConditionDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName]
    FROM
        (((((((((((featureaction
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
        LEFT JOIN [${dbxschemaname}].[actiondisplaynamedescription] ON (([${dbxschemaname}].[actiondisplaynamedescription].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[featureactionroletype].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[termandcondition] ON (([${dbxschemaname}].[featureaction].[TermsAndConditions_id] = [${dbxschemaname}].[termandcondition].[id])))
        LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])))
        LEFT JOIN [${dbxschemaname}].[dependentactions_view] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions_view].[actionId])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureactionroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
		ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;;
GO
 
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[group_features_actions_view]; 
GO

CREATE VIEW [${dbxschemaname}].[group_features_actions_view] AS
    SELECT 
        [${dbxschemaname}].[groupactionlimit].[Group_id] AS [Group_id],
        [${dbxschemaname}].[groupactionlimit].[Action_id] AS [Action_id],
        [${dbxschemaname}].[groupactionlimit].[LimitType_id] AS [LimitType_id],
		[${dbxschemaname}].[groupactionlimit].[value] AS [value],
        [${dbxschemaname}].[groupactionlimit].[id] AS [groupactionlimit_id],
        [${dbxschemaname}].[groupactionlimit].[softdeleteflag] AS [softdelete],
		[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
        [${dbxschemaname}].[membergroup].[Name] AS [Group_name],
        [${dbxschemaname}].[membergroup].[Description] AS [Group_description],
        [${dbxschemaname}].[featureaction].[name] AS [Action_name],
        [${dbxschemaname}].[featureaction].[description] AS [Action_description],
        [${dbxschemaname}].[featureaction].[Type_id] AS [Action_Type_id],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [Feature_id],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [Action_displaysequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [Action_dependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
		[${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
		[${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
		[${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[name] AS [Feature_name],
        [${dbxschemaname}].[feature].[description] AS [Feature_description],
        [${dbxschemaname}].[feature].[Type_id] AS [Feature_Type_id],
        [${dbxschemaname}].[feature].[Status_id] AS [Feature_Status_id],
        [${dbxschemaname}].[feature].[DisplaySequence] AS [Feature_displaysequence],
        [${dbxschemaname}].[feature].[isPrimary] AS [Feature_isPrimary]
    FROM
        (((((([${dbxschemaname}].[groupactionlimit]
        LEFT JOIN [${dbxschemaname}].[membergroup] ON (([${dbxschemaname}].[membergroup].[id] = [${dbxschemaname}].[groupactionlimit].[Group_id])))
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[groupactionlimit].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
		LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))	
		LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [limitgroup].[id])))
		LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [actionlevel].[id])))
        ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;
GO


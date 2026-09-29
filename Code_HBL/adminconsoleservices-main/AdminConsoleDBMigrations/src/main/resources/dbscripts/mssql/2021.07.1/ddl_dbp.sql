USE [${dbxdbname}];
GO
DROP PROCEDURE IF EXISTS [${dbxdbname}].[prospect_securityattributes_get_proc];

GO
CREATE PROCEDURE [${dbxdbname}].[prospect_securityattributes_get_proc]
   @_userId nvarchar(50)
AS 
BEGIN

SET  XACT_ABORT  ON
SET  NOCOUNT  ON

DECLARE @userAssociatedGroups nvarchar(max) = N''
DECLARE @actionsAtGroups nvarchar(max) = N''
DECLARE @activeFeaturesAtFI nvarchar(max) = N''
DECLARE @intersectedUserActions nvarchar(max) = N''
DECLARE @intersectedUserFeatures nvarchar(max) = N''

SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxdbname}].customergroup where [${dbxdbname}].customergroup.Customer_id = @_userId);

	
SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxdbname}].groupactionlimit where [${dbxdbname}].FIND_IN_SET([${dbxdbname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1');

SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxdbname}].feature where [${dbxdbname}].feature.Status_id = 'SID_FEATURE_ACTIVE');

SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxdbname}].featureaction WHERE [${dbxdbname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxdbname}].FIND_IN_SET([${dbxdbname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxdbname}].FIND_IN_SET([${dbxdbname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');

SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxdbname}].featureaction where [${dbxdbname}].FIND_IN_SET([${dbxdbname}].featureaction.id,@intersectedUserActions)= '1');

SELECT @intersectedUserFeatures AS features;

END;

GO

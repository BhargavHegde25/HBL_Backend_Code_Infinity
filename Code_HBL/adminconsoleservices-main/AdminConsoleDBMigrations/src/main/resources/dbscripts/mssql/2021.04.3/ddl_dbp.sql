GO

CREATE PROCEDURE [${dbxschemaname}].[prospect_securityattributes_get_proc]
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

SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId);

	
SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1');

SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature where [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE');

SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');

SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions)= '1');

SELECT @intersectedUserFeatures AS features;

END;

GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_users_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].contract_users_details_get_proc  
   @_contractId nvarchar(50),
   @_backendType nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @customers nvarchar(max) = N''

      SET @customers =  (SELECT String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers WHERE 
                               [${dbxschemaname}].contractcustomers.contractId = @_contractId)

      SELECT 
         customer.id AS customerId, 
         customer.FirstName AS firstName, 
         customer.MiddleName AS middleName, 
         customer.LastName AS lastName, 
         customer.UserName AS userName, 
         customer.Status_id AS statusId, 
         customer.DateOfBirth AS dateOfBirth, 
         customer.Ssn AS Ssn, 
         backendidentifier.BackendId AS primaryCoreCustomerId,
         customercommunication.Value AS Email
      FROM 
         [${dbxschemaname}].customer 
            LEFT JOIN [${dbxschemaname}].customercommunication 
            ON ([${dbxschemaname}].customer.id = [${dbxschemaname}].customercommunication.Customer_id AND customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND
                customercommunication.isPrimary = '1')
            LEFT JOIN [${dbxschemaname}].backendidentifier 
            ON ([${dbxschemaname}].backendidentifier.Customer_id = [${dbxschemaname}].customer.id AND 
                [${dbxschemaname}].backendidentifier.BackendType = @_backendType)
         WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id, @customers) = '1'
   END;

GO
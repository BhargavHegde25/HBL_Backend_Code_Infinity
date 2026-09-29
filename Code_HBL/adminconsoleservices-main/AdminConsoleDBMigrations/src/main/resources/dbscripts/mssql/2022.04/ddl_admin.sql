DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalmatrixtemplate_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalmatrixtemplate_proc](
    @_contractId NVARCHAR(50),
    @_cif NVARCHAR(50),
    @_limitTypeId NVARCHAR(50),
	@_actions NVARCHAR(MAX)
) AS
BEGIN 
	DECLARE @isGroupMatrix INT;

    IF @_cif = '' BEGIN
    SET @_cif = '%';
    END 
    
    IF @_limitTypeId = '' BEGIN
    SET @_limitTypeId = '%';
    END 
    
    select @isGroupMatrix = isGroupLevel from approvalmode where contractId = @_contractId AND coreCustomerId = @_cif;
	IF @isGroupMatrix IS NULL BEGIN
    SET @isGroupMatrix = 0;
	END

    IF @isGroupMatrix = 0 BEGIN
		SELECT 
			[approvalmatrixtemplate].[id],
			[approvalmatrixtemplate].[contractId],
			 [approvalmatrixtemplate].[limitTypeId],
			 [featureAction].[id] AS [actionId],
			 [featureAction].[name] AS [actionName],
			 [featureAction].[description] AS [actionDescription],
			 [featureAction].[Feature_id] AS [featureId],
			 [featureAction].[Type_id] AS [actionType],
			 [featureAction].[isAccountLevel] AS [isAccountLevel],
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
			 [customer].[id] AS [customerId],
			 [customer].[FirstName] AS [firstName],
			 [customer].[LastName] AS [lastName],
			 [contractcorecustomers].[coreCustomerId] AS [cifId],
			 [contractcorecustomers].[coreCustomerName] AS [cifName],
			 [approvalmatrixtemplate].[invalid],
			 [approvalmatrixtemplate].[isGroupMatrix]
			FROM ((((((([${dbxschemaname}].[approvalmatrixtemplate] AS [approvalmatrixtemplate]
			LEFT JOIN
			[${dbxschemaname}].[customerapprovalmatrixtemplate] AS [customerapprovalmatrixtemplate]
			ON [approvalmatrixtemplate].[id] = [customerapprovalmatrixtemplate].[approvalMatrixId])
			LEFT JOIN
			[${dbxschemaname}].[customer] AS [customer]
			ON [customerapprovalmatrixtemplate].[customerId] = [customer].[id])
			LEFT JOIN
			[${dbxschemaname}].[featureaction] AS [featureAction]
			ON [approvalmatrixtemplate].[actionId] = [featureAction].[id])
			LEFT JOIN
			[${dbxschemaname}].[approvalrule] AS [approvalRule]
			ON [approvalmatrixtemplate].[approvalruleId] = [approvalRule].[id]) 
		  LEFT JOIN
			[${dbxschemaname}].[feature] AS [feature]
			ON [featureAction].[Feature_id] = [feature].[id])
		  LEFT JOIN
			[${dbxschemaname}].[contractfeatures] AS [contractfeatures]
			ON [feature].[id] = [contractfeatures].[featureId]
			  and  [approvalmatrixtemplate].[contractId] = [contractfeatures].[contractId]
			  and [approvalmatrixtemplate].[coreCustomerId] = [contractfeatures].[coreCustomerId])
		  LEFT JOIN
			[${dbxschemaname}].[contractcorecustomers] AS [contractcorecustomers]
			ON [approvalmatrixtemplate].[contractId]  = [contractcorecustomers].[contractId] AND [approvalmatrixtemplate].[coreCustomerId] = [contractcorecustomers].[coreCustomerId])           
		WHERE 
		[approvalmatrixtemplate].[contractId] = @_contractId AND
		[approvalmatrixtemplate].[coreCustomerId] LIKE @_cif AND
		[${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[actionId],@_actions) > 0 AND 
		[approvalmatrixtemplate].[limitTypeId] LIKE @_limitTypeId AND 
		[approvalmatrixtemplate].[softdeleteflag] = 0 AND
		 [featureAction].[approveFeatureAction] is not null AND
		 [featureAction].[approveFeatureAction] != '' AND
		[featureAction].[status] = 'SID_ACTION_ACTIVE'
			ORDER BY [approvalmatrixtemplate].[contractId],[approvalmatrixtemplate].[coreCustomerId],[approvalmatrixtemplate].[limitTypeId],[approvalmatrixtemplate].[actionId] ,[approvalmatrixtemplate].[lowerlimit];
	
    END
    ELSE IF @isGroupMatrix = 1 BEGIN
		-- SQLINES LICENSE FOR EVALUATION USE ONLY
		SELECT 
			[approvalmatrixtemplate].[id],
			[approvalmatrixtemplate].[contractId],
			 [approvalmatrixtemplate].[limitTypeId],
			 [featureAction].[id] AS [actionId],
			 [featureAction].[name] AS [actionName],
			 [featureAction].[description] AS [actionDescription],
			 [featureAction].[Feature_id] AS [featureId],
			 [featureAction].[Type_id] AS [actionType],
			 [featureAction].[isAccountLevel] AS [isAccountLevel],
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
			 [signatorygroupmatrixtemplate].[groupList] AS [groupList],
			 [signatorygroupmatrixtemplate].[groupRule] AS [groupRule],
			 [contractcorecustomers].[coreCustomerId] AS [cifId],
			 [contractcorecustomers].[coreCustomerName] AS [cifName],
			 [approvalmatrixtemplate].[invalid],
			 [approvalmatrixtemplate].[isGroupMatrix]
			FROM (((((([${dbxschemaname}].[approvalmatrixtemplate] AS [approvalmatrixtemplate]
			LEFT JOIN
			[${dbxschemaname}].[signatorygroupmatrixtemplate] AS [signatorygroupmatrixtemplate]
			ON [approvalmatrixtemplate].[id] = [signatorygroupmatrixtemplate].[approvalMatrixId])
			LEFT JOIN
			[${dbxschemaname}].[featureaction] AS [featureAction]
			ON [approvalmatrixtemplate].[actionId] = [featureAction].[id])
			LEFT JOIN
			[${dbxschemaname}].[approvalrule] AS [approvalRule]
			ON [approvalmatrixtemplate].[approvalruleId] = [approvalRule].[id]) 
		  LEFT JOIN
			[${dbxschemaname}].[feature] AS [feature]
			ON [featureAction].[Feature_id] = [feature].[id])
		  LEFT JOIN
			[${dbxschemaname}].[contractfeatures] AS [contractfeatures]
			ON [feature].[id] = [contractfeatures].[featureId]
			  and  [approvalmatrixtemplate].[contractId] = [contractfeatures].[contractId]
			  and [approvalmatrixtemplate].[coreCustomerId] = [contractfeatures].[coreCustomerId])
		  LEFT JOIN
			[${dbxschemaname}].[contractcorecustomers] AS [contractcorecustomers]
			ON [approvalmatrixtemplate].[contractId]  = [contractcorecustomers].[contractId] AND [approvalmatrixtemplate].[coreCustomerId] = [contractcorecustomers].[coreCustomerId])           
		WHERE 
		[approvalmatrixtemplate].[contractId] = @_contractId AND
		[approvalmatrixtemplate].[coreCustomerId] LIKE @_cif AND 
		[approvalmatrixtemplate].[isGroupMatrix] = 1 AND
		[${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[actionId],@_actions) > 0 AND 
		[approvalmatrixtemplate].[limitTypeId] LIKE @_limitTypeId AND 
		[approvalmatrixtemplate].[softdeleteflag] = 0 AND
		 [featureAction].[approveFeatureAction] is not null AND
		 [featureAction].[approveFeatureAction] != '' AND
		[featureAction].[status] = 'SID_ACTION_ACTIVE'
			ORDER BY [approvalmatrixtemplate].[contractId],[approvalmatrixtemplate].[coreCustomerId],[approvalmatrixtemplate].[limitTypeId],[approvalmatrixtemplate].[actionId] ,[approvalmatrixtemplate].[lowerlimit];

    END 
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_grouprecords_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_grouprecords_proc](
	@_contractId nvarchar(50),
	@_cif nvarchar(50),
	@_accountId nvarchar(50),
	@_limitTypeId nvarchar(50),
	@_actions nvarchar(max))
AS BEGIN

		SET XACT_ABORT ON
		IF @_cif = ''
		SET @_cif = '%'

		SET NOCOUNT ON
		IF @_accountId = ''
		SET @_accountId = '%'
		IF @_limitTypeId = ''
		SET @_limitTypeId = '%';

		SELECT
			approvalmatrix.id,
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			featureAction.id AS actionId,
			featureAction.name AS actionName,
			featureAction.description AS actionDescription,
			featureAction.Feature_id AS featureId,
			featureAction.Type_id AS actionType,
			featureAction.isAccountLevel AS isAccountLevel,
			feature.name AS featureName,
			feature.Status_id AS fifeaturestatus,
			approvalRule.id AS approvalruleId,
			approvalRule.numberOfApprovals,
			approvalRule.name AS approvalRuleName,
			approvalMatrix.lowerlimit,
			approvalMatrix.upperlimit,
			signatoryGroupMatrix.groupList AS groupList,
		    signatoryGroupMatrix.groupRule AS groupRule,
			contractcorecustomers.coreCustomerId AS cifId,
			contractcorecustomers.coreCustomerName AS cifName,
			approvalMatrix.invalid,
			approvalMatrix.isGroupMatrix
		FROM (((((([${dbxschemaname}].approvalmatrix AS approvalMatrix
			LEFT JOIN [${dbxschemaname}].signatorygroupmatrix AS signatoryGroupMatrix
			ON approvalMatrix.id = signatoryGroupMatrix.approvalMatrixId)
			LEFT JOIN [${dbxschemaname}].featureaction AS featureAction
			ON approvalMatrix.actionId = featureAction.id)
			LEFT JOIN [${dbxschemaname}].approvalrule AS approvalRule
			ON approvalMatrix.approvalruleId = approvalRule.id)
			LEFT JOIN [${dbxschemaname}].feature AS feature
			ON featureAction.Feature_id = feature.id)
			LEFT JOIN [${dbxschemaname}].contractfeatures AS contractfeatures
			ON feature.id = contractfeatures.featureId and approvalMatrix.contractId = contractfeatures.contractId
			and approvalMatrix.coreCustomerId = contractfeatures.coreCustomerId)
			LEFT JOIN contractcorecustomers AS contractcorecustomers
			ON approvalMatrix.contractId = contractcorecustomers.contractId AND approvalMatrix.coreCustomerId = contractcorecustomers.coreCustomerId)
		WHERE
			approvalmatrix.contractId = @_contractId AND
			approvalmatrix.coreCustomerId LIKE @_cif AND
			approvalmatrix.isGroupMatrix = 1 AND
			approvalmatrix.accountId LIKE @_accountId AND
			[${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND
			approvalmatrix.limitTypeId LIKE @_limitTypeId AND
			approvalmatrix.softdeleteflag = 0 AND
			featureAction.approveFeatureAction is not null AND
			featureAction.approveFeatureAction != '' AND
			featureAction.status = 'SID_ACTION_ACTIVE'
		ORDER BY
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			approvalMatrix.actionId,
			approvalMatrix.lowerlimit
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_records_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_records_proc](
	@_contractId nvarchar(50),
	@_cif nvarchar(50),
	@_accountId nvarchar(50),
	@_limitTypeId nvarchar(50),
	@_actions nvarchar(max))
AS BEGIN

		SET XACT_ABORT ON
		IF @_cif = ''
		SET @_cif = '%'

		SET NOCOUNT ON
		IF @_accountId = ''
		SET @_accountId = '%'
		IF @_limitTypeId = ''
		SET @_limitTypeId = '%';

		SELECT
			approvalmatrix.id,
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			featureAction.id AS actionId,
			featureAction.name AS actionName,
			featureAction.description AS actionDescription,
			featureAction.Feature_id AS featureId,
			featureAction.Type_id AS actionType,
			featureAction.isAccountLevel AS isAccountLevel,
			feature.name AS featureName,
			feature.Status_id AS fifeaturestatus,
			approvalRule.id AS approvalruleId,
			approvalRule.numberOfApprovals,
			approvalRule.name AS approvalRuleName,
			approvalMatrix.lowerlimit,
			approvalMatrix.upperlimit,
			customer.id AS customerId,
			customer.FirstName AS firstName,
			customer.LastName AS lastName,
			contractcorecustomers.coreCustomerId AS cifId,
			contractcorecustomers.coreCustomerName AS cifName,
			approvalMatrix.invalid,
			approvalMatrix.isGroupMatrix
		FROM ((((((([${dbxschemaname}].approvalmatrix AS approvalMatrix
			LEFT JOIN [${dbxschemaname}].customerapprovalmatrix AS customerApprovalMatrix
			ON approvalMatrix.id = customerApprovalMatrix.approvalMatrixId)
			LEFT JOIN [${dbxschemaname}].customer AS customer
			ON customerApprovalMatrix.customerId = customer.id)
			LEFT JOIN [${dbxschemaname}].featureaction AS featureAction
			ON approvalMatrix.actionId = featureAction.id)
			LEFT JOIN [${dbxschemaname}].approvalrule AS approvalRule
			ON approvalMatrix.approvalruleId = approvalRule.id)
			LEFT JOIN [${dbxschemaname}].feature AS feature
			ON featureAction.Feature_id = feature.id)
			LEFT JOIN [${dbxschemaname}].contractfeatures AS contractfeatures
			ON feature.id = contractfeatures.featureId and approvalMatrix.contractId = contractfeatures.contractId
			and approvalMatrix.coreCustomerId = contractfeatures.coreCustomerId)
			LEFT JOIN contractcorecustomers AS contractcorecustomers
			ON approvalMatrix.contractId = contractcorecustomers.contractId AND approvalMatrix.coreCustomerId = contractcorecustomers.coreCustomerId)
		WHERE
			approvalmatrix.contractId = @_contractId AND
			approvalmatrix.coreCustomerId LIKE @_cif AND
			approvalmatrix.accountId LIKE @_accountId AND
			[${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND
			approvalmatrix.limitTypeId LIKE @_limitTypeId AND
			approvalmatrix.softdeleteflag = 0 AND
			featureAction.approveFeatureAction is not null AND
			featureAction.approveFeatureAction != '' AND
			featureAction.status = 'SID_ACTION_ACTIVE'
		ORDER BY
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			approvalMatrix.actionId,
			approvalMatrix.lowerlimit
END;
GO

ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD NickName nvarchar(50);

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[corecustomeraccounts_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[corecustomeraccounts_details_get_proc]  
   @_coreCustomerIdList nvarchar(max),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  DECLARE @selectstatement NVARCHAR(max);
	  DECLARE @corecustomersList NVARCHAR(max);

      SET @corecustomersList = 
         (

            SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',')
            FROM [${dbxschemaname}].contractcustomers
            WHERE contractcustomers.customerId = @_customerId AND [${dbxschemaname}].FIND_IN_SET(contractcustomers.coreCustomerId, @_coreCustomerIdList) > 0

         )
      

      SET @selectstatement = CONCAT('(SELECT
	       contractcorecustomers.coreCustomerId AS coreCustomerId ,
           contractcorecustomers.coreCustomerName AS coreCustomerName ,
           customeraccounts.email AS email ,
           customeraccounts.Customer_id AS customerId ,
           customeraccounts.FavouriteStatus AS favouriteStatus ,
		   customeraccounts.NickName AS nickName ,
           IIF ((customeraccounts.EStatementmentEnable) = ''1'' ,''true'' , ''false'') AS eStatementEnable,
           IIF ((contractcorecustomers.isBusiness) = ''1'' ,''true'' , ''false'') AS isBusinessAccount,
           contractaccounts.accountId AS accountId
           from [${dbxschemaname}].contractcorecustomers 
		   JOIN [${dbxschemaname}].contractaccounts ON (contractcorecustomers.coreCustomerId = contractaccounts.coreCustomerId)
           JOIN [${dbxschemaname}].customeraccounts ON (customeraccounts.Account_id = contractaccounts.accountId)
           where [${dbxschemaname}].FIND_IN_SET(contractcorecustomers.coreCustomerId, ','''',@corecustomersList,'''',')>0 AND customeraccounts.Customer_id = ','''',@_customerId,'''',')')
      
	  exec(@selectstatement);
   END
GO

ALTER PROCEDURE [${dbxschemaname}].[location_range_proc]  
   @_currLatitude varchar(50),
   @_currLongitude varchar(50),
   @_radius int
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE


         @rowLatitide varchar(2000)

      DECLARE


         @rowLongitude varchar(2000)

      DECLARE
         @b int

      DECLARE
         @pipeFlag int


     SELECT 
         (location.id) AS locationId, 
         
            (
               SELECT DISTINCT(String_agg((upper(LEFT(dayschedule.weekdayname, 1))+lower(SUBSTRING(dayschedule.weekdayname,2,DATALENGTH(dayschedule.weekdayname)))+ ':'+ 
          SUBSTRING(CAST(dayschedule.StartTime AS VARCHAR(max)), 1, 5)+'-'+ SUBSTRING(CAST(dayschedule.endTime AS VARCHAR(max)), 1, 5)) , ' || ')) 
               FROM [${dbxschemaname}].dayschedule, [${dbxschemaname}].location
               WHERE dayschedule.WorkSchedule_id = location.WorkSchedule_id AND location.id = location.id
            ) AS workingHours, 
         (location.Name) AS informationTitle, 
         (location.PhoneNumber) AS phone, 
         (location.EmailId) AS email, 
         CASE (location.Status_id)
            WHEN N'SID_ACTIVE' THEN N'OPEN'
            ELSE N'CLOSED'
         END AS status, 
         (location.Type_id) AS type, 
		 (location.status_id) AS isVisible,
         
            (  SELECT String_agg(CAST(facility.name as nvarchar(max)),'||') WITHIN GROUP (ORDER BY facility.id ASC)
               FROM [${dbxschemaname}].facility , [${dbxschemaname}].locationfacility
               WHERE facility.id = locationfacility.facility_id AND locationfacility.Location_id =( location.id)
            )  AS services, 
         
            (
               SELECT city.Name
               FROM [${dbxschemaname}].city
               WHERE city.id = (address.City_id)
            ) AS city, 
         (address.addressLine1) AS addressLine1, 
         (address.addressLine2) AS addressLine2, 
         (address.addressLine3) AS addressLine3, 
         (address.zipCode) AS zipCode, 
         (address.latitude) AS latitude, 
         (address.logitude) AS longitude, 
         (6371 * 2 * asin(sqrt(power(sin((CAST((address.latitude) AS float(53)) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos(CAST((address.latitude) AS float(53)) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin((CAST((address.logitude) AS float(53)) - CAST(@_currLongitude AS float(53))) * pi() / 180 / 2), 2)))) AS distance
      FROM [${dbxschemaname}].address, [${dbxschemaname}].location
      WHERE address.id = location.Address_id
      AND (6371 * 2 * asin(sqrt(power(sin((CAST((address.latitude) AS float(53)) - abs(CAST(@_currLatitude AS float(53)))) * pi() / 180 / 2), 2) + cos(CAST((address.latitude) AS float(53)) * pi() / 180) * cos(abs(CAST(@_currLatitude AS float(53))) * pi() / 180) * power(sin((CAST((address.logitude) AS float(53)) - CAST(@_currLongitude AS float(53))) * pi() / 180 / 2), 2)))) <= @_radius
      AND location.status_id ='SID_ACTIVE';
   END

GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_records_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_records_proc](
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_accountId nvarchar(50),
   @_limitTypeId nvarchar(50),
   @_actions nvarchar(max))
AS BEGIN

      SET  XACT_ABORT  ON
	  IF @_cif = ''
		SET @_cif = '%'

      SET  NOCOUNT  ON
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
         approvalMatrix.invalid
      FROM ((((((([${dbxschemaname}].approvalmatrix  AS approvalMatrix 
         LEFT JOIN [${dbxschemaname}].customerapprovalmatrix  AS customerApprovalMatrix 
         ON approvalMatrix.id = customerApprovalMatrix.approvalMatrixId) 
         LEFT JOIN [${dbxschemaname}].customer  AS customer 
         ON customerApprovalMatrix.customerId = customer.id) 
         LEFT JOIN [${dbxschemaname}].featureaction  AS featureAction 
         ON approvalMatrix.actionId = featureAction.id) 
         LEFT JOIN [${dbxschemaname}].approvalrule  AS approvalRule 
         ON approvalMatrix.approvalruleId = approvalRule.id) 
         LEFT JOIN [${dbxschemaname}].feature  AS feature 
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
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_actions_with_approvefeatureaction_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].get_actions_with_approvefeatureaction_proc(
 @_featureActions NVARCHAR(max)) AS
BEGIN
DECLARE @actionsList NVARCHAR(max)
SET @actionsList = (						
            SELECT String_agg(CAST([${dbxschemaname}].[featureaction].[id] AS nvarchar(max)), ',') FROM [${dbxschemaname}].[featureaction] WHERE 
                               [${dbxschemaname}].FIND_IN_SET(id,@_featureActions) = 1 AND
					(isnull(featureaction.approvefeatureaction,'') != ''));


select @actionsList As actions;

END;
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].approvalmatrix_default_create_proc(
 @_actionIds NVARCHAR(max) , 
@_contractId NVARCHAR(50) ,
@_accountIds NVARCHAR(50) ,
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
set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
set @numOfActions = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
getAccount: WHILE 1=1 BEGIN
	set @accountIndex = @accountIndex + 1;
	IF @accountIndex = @numOfAccounts + 1 BEGIN 
		BREAK;
	End
	Else Begin
		set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIndex), ',', -1 );
		set @actionIndex = 0; 
		 getAction: WHILE 1=1 BEGIN
            set @actionIndex = @actionIndex + 1;
            IF @actionIndex = @numOfActions + 1 BEGIN
				BREAK;
			End
			Else Begin
				set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex), ',', -1 );
                SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
                IF @typeId = 'MONETARY' BEGIN			
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_1, '_', @_contractId), @accountId, @actionId, @limitTypeId_1,@_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_2, '_', @_contractId), @accountId,@actionId,@limitTypeId_2,@_cif,'NO_APPROVAL');
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
					(@_contractId, concat(@actionId, '_', @accountId, '_', @limitTypeId_3, '_', @_contractId),@accountId, @actionId, @limitTypeId_3,@_cif,'NO_APPROVAL');
                END
                ELSE IF @typeId = 'NON_MONETARY' BEGIN
					INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,coreCustomerId, approvalruleId) VALUES
                    (@_contractId, concat(@actionId, '_', @accountId, '_', 'NON_MONETARY_LIMIT', '_', @_contractId), @accountId, @actionId, 'NON_MONETARY_LIMIT',@_cif,'NO_APPROVAL');
				END 
			END 
		 END;
        set @accountList = CONCAT(@accountId,',',@accountList);
	END 
END;  

SET @accountList = (select SUBSTRING(@accountList,1,(LEN(@accountList)-1)));
select @accountList as accountList;
END;
GO


ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [isAutoSubscribeEnabled] BIT DEFAULT 0;
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [externalSystem] smallint NULL DEFAULT '0';

ALTER TABLE [${dbxschemaname}].[eventsubtype] ADD [externalSystem] smallint NULL DEFAULT '0';
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[externalalertsytem];

CREATE TABLE [${dbxschemaname}].[externalalertsytem] (
  [systemtype] varchar(50) NOT NULL,
  [systemid] smallint NOT NULL,
  
  PRIMARY KEY ([systemid])
) ;
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[alertsubtypetext_view];
GO
CREATE VIEW [${dbxschemaname}].[alertsubtypetext_view] AS select
    [${dbxschemaname}].alertsubtype.id AS alertsubtype_id,
    [${dbxschemaname}].alertsubtype.AlertTypeId AS alertsubtype_alertTypeId,
    [${dbxschemaname}].alertsubtype.Name AS alertsubtype_Name,
    [${dbxschemaname}].alertsubtype.Status_id AS alertsubtype_StatusId,
    [${dbxschemaname}].alertsubtype.isAccountLevel AS alertsubtype_isAccountLevel,
    [${dbxschemaname}].alertsubtype.attributeId AS alertsubtype_attributeId,
    [${dbxschemaname}].alertsubtype.alertConditionId AS alertsubtype_alertConditionId,
    [${dbxschemaname}].alertsubtype.value1 AS alertsubtype_value1,
    [${dbxschemaname}].alertsubtype.value2 AS alertsubtype_value2,
    [${dbxschemaname}].alertsubtype.isGlobal AS alertsubtype_isGlobal,
    [${dbxschemaname}].alertsubtype.defaultFrequencyId AS alertsubtype_defaultFrequencyId,
    [${dbxschemaname}].alertsubtype.defaultFrequencyValue AS alertsubtype_defaultFrequencyValue,
    [${dbxschemaname}].alertsubtype.defaultFrequencyTime AS alertsubtype_defaultFrequencyTime,
    [${dbxschemaname}].alertsubtype.createdby AS alertsubtype_createdby,
    [${dbxschemaname}].alertsubtype.modifiedby AS alertsubtype_modifiedby,
    [${dbxschemaname}].alertsubtype.createdts AS alertsubtype_createdts,
    [${dbxschemaname}].alertsubtype.lastmodifiedts AS alertsubtype_lastmodifiedts,
    [${dbxschemaname}].alertsubtype.synctimestamp AS alertsubtype_synctimestamp,
    [${dbxschemaname}].alertsubtype.softdeleteflag AS alertsubtype_softdeleteflag,
    [${dbxschemaname}].alertsubtypetext.languageCode AS alertsubtypetext_languageCode,
    [${dbxschemaname}].alertsubtypetext.description AS alertsubtypetext_description,
    [${dbxschemaname}].alertsubtype.externalSystem AS alertsubtype_externalSystem,
    [${dbxschemaname}].alertsubtypetext.displayName AS alertsubtypetext_displayName
from
    ([${dbxschemaname}].[alertsubtype]
join [${dbxschemaname}].[alertsubtypetext] on
    ((alertsubtypetext.alertSubTypeId = alertsubtype.id)));
    
GO 

ALTER TABLE [${dbxschemaname}].[APPLICATION] ADD [stateManagementAvailable] [TINYINT] NOT NULL DEFAULT (0)
GO
   
DROP VIEW if exists [${dbxschemaname}].[groups_view];
GO
CREATE VIEW [${dbxschemaname}].[groups_view] AS 
select [${dbxschemaname}].[membergroup].[id] AS [Group_id],
[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
[${dbxschemaname}].[membergrouptype].[description] AS [Type_Name],
[${dbxschemaname}].[membergroup].[Description] AS [Group_Desc],
[${dbxschemaname}].[membergroup].[Status_id] AS [Status_id],
[${dbxschemaname}].[membergroup].[Name] AS [Group_Name],
[${dbxschemaname}].[membergroup].[isEAgreementActive] AS [isEAgreementActive],
[${dbxschemaname}].[membergroup].[isApplicabletoAllServices] As [isApplicabletoAllServices],
(select count([${dbxschemaname}].[groupentitlement].[Group_id]) from [${dbxschemaname}].[groupentitlement] where
 ([${dbxschemaname}].[groupentitlement].[Group_id] = [${dbxschemaname}].[membergroup].[id])) AS [Entitlements_Count],
 (select count(distinct([${dbxschemaname}].[customergroup].[Customer_id])) from [${dbxschemaname}].[customergroup] 
 where ([${dbxschemaname}].[customergroup].[Group_id] = [membergroup].[id])) AS [Customers_Count],
 (case [${dbxschemaname}].[membergroup].[Status_id] when 'SID_ACTIVE' then 'Active' else 'Inactive' end) AS [Status] 
 from ([${dbxschemaname}].[membergroup] join [membergrouptype] on(([${dbxschemaname}].[membergroup].[Type_id] = [membergrouptype].[id])));

GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_wiretransfer_details_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_wiretransfer_details_proc]  
   @_transactionId nvarchar(50),
   @_wireFileExecution_id nvarchar(50),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @companyId nvarchar(max)
      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
		 
      IF @_transactionId IS NULL
         SET @_transactionId = N''

      IF @_wireFileExecution_id IS NULL
         SET @_wireFileExecution_id = N''

      IF @companyId IS NULL

         SET @companyId = N''
	 
	  declare @filter nvarchar(max)
      SET @filter = 
         CASE 
            WHEN (@_transactionId <> '') THEN (N'transactionId = ') + (@_transactionId) + ' AND [${dbxschemaname}].FIND_IN_SET(companyId, '''+@companyId+''') <> 0'
            ELSE 
               CASE 
                  WHEN (@_wireFileExecution_id <> '') THEN (N'wireFileExecution_id = ') + (@_wireFileExecution_id) + ' AND [${dbxschemaname}].FIND_IN_SET(companyId, '''+@companyId+''') <> 0'
                  ELSE N''
               END
         END
		
      
	  declare @select_statement nvarchar(max)
      SET @select_statement = (N'SELECT '+N'wiretransfers.transactionId,'+N'wiretransfers.featureActionId,'+N'        wiretransfers.confirmationNumber,'+N'        wiretransfers.companyId,'+N'        wiretransfers.createdby,'+N'        wiretransfers.requestId,'+N'        wiretransfers.status,'+N'        wiretransfers.onetime_id,'+N'        wiretransfers.wireFileExecution_id,'+N'        wiretransfers.notes,'+N'        wiretransfers.amount,'+N'        wiretransfers.fromAccountNumber,'+N'        wiretransfers.payeeAccountNumber,'+N'        wiretransfers.transactionType,'+N'        wiretransfers.payeeId,'+N'        wiretransfers.payeeCurrency,'+N'        onetimepayee.payeeName,'
	  +N'onetimepayee.payeeNickName,'+N'onetimepayee.payeeType,'+N'onetimepayee.wireAccountType,'+N'onetimepayee.swiftCode,'+N'onetimepayee.routingNumber,'+N'onetimepayee.zipCode,'+N'onetimepayee.cityName,'+N'onetimepayee.state,'+N'onetimepayee.country,'+N'onetimepayee.payeeAddressLine1,'+N'onetimepayee.payeeAddressLine2,'+N'onetimepayee.bankName,'+N'onetimepayee.internationalRoutingCode,'+N'onetimepayee.bankAddressLine1,'+N'onetimepayee.bankAddressLine2,'+N'onetimepayee.bankCity,'+N'onetimepayee.bankState,'+N'onetimepayee.bankZip'+N'        '+N'    FROM'+N'        ([${dbxschemaname}].wiretransfers'+N' LEFT JOIN [${dbxschemaname}].onetimepayee ON (wiretransfers.onetime_id = onetimepayee.onetime_id))'+N' WHERE ') + (@filter)

	  EXEC(@select_statement)


   END
GO

CREATE TABLE [${dbxschemaname}].[alertmessagetypeconfig] (
  [messagetype] VARCHAR(10) NOT NULL,
  [alerttype] VARCHAR(50) NOT NULL,
  [alertsubtype] VARCHAR(75) NOT NULL,
    CONSTRAINT PK_alertmessagetypeconfig_messagetype PRIMARY KEY (messagetype)
)
GO

ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD [alertRequestId] varchar(255)
GO


CREATE VIEW [${dbxschemaname}].[autosubscribedalertsview] AS select [${dbxschemaname}].[dbxalertcategory].[id] as categoryid , [${dbxschemaname}].[dbxalerttype].[id] as groupid , [${dbxschemaname}].[alertsubtype].[id] as alertsubtypeid , [${dbxschemaname}].[alertsubtype].[name] , [${dbxschemaname}].[alertsubtype].[isAccountLevel] , [${dbxschemaname}].[alertsubtype].[attributeId] , [${dbxschemaname}].[alertsubtype].[alertConditionId] , [${dbxschemaname}].[alertsubtype].[value1] , [${dbxschemaname}].[alertsubtype].[value2] , [${dbxschemaname}].[alertsubtype].[Status_id] , [${dbxschemaname}].[alertsubtype].[Description] , [${dbxschemaname}].[alertsubtype].[isGlobal] , [${dbxschemaname}].[alertsubtype].[defaultFrequencyId] , [${dbxschemaname}].[alertsubtype].[defaultFrequencyValue] , [${dbxschemaname}].[alertsubtype].[defaultFrequencyTime] , [${dbxschemaname}].[alertsubtype].[recipienttype] , [${dbxschemaname}].[alertsubtype].[createdby] , [${dbxschemaname}].[alertsubtype].[modifiedby] , [${dbxschemaname}].[alertsubtype].[createdts] , [${dbxschemaname}].[alertsubtype].[lastmodifiedts] , [${dbxschemaname}].[alertsubtype].[synctimestamp] , [${dbxschemaname}].[alertsubtype].[softdeleteflag] , [${dbxschemaname}].[alertsubtype].[isAutoSubscribeEnabled] , [${dbxschemaname}].[alertsubtype].[externalSystem] from 
[${dbxschemaname}].[dbxalertcategory] INNER JOIN [${dbxschemaname}].[dbxalerttype] ON ([${dbxschemaname}].[dbxalertcategory].[id] = [${dbxschemaname}].[dbxalerttype].[AlertCategoryId]) INNER JOIN [${dbxschemaname}].[alertsubtype] ON ([${dbxschemaname}].[dbxalerttype].[id] = [${dbxschemaname}].[alertsubtype].[alerttypeid])
GO

CREATE  VIEW  [${dbxschemaname}].[alertsubtypeaccounttype_view] AS select [${dbxschemaname}].[accounttype].[displayName] AS accountTypeId,[${dbxschemaname}].[alertsubtypeaccounttype].[alertSubTypeId] AS alertSubTypeId from [${dbxschemaname}].[alertsubtypeaccounttype] inner join [${dbxschemaname}].[accounttype] ON ([${dbxschemaname}].[alertsubtypeaccounttype].[accountTypeId] = [${dbxschemaname}].[accounttype].[TypeID])
GO


ALTER TABLE  [${dbxschemaname}].[intrabanktransfers]
ADD
[iban] VARCHAR(50) NULL DEFAULT NULL,
[bicCode] VARCHAR(50) NULL DEFAULT NULL,
[bankName] VARCHAR(50) NULL DEFAULT NULL,
[bankId] VARCHAR(50) NULL DEFAULT NULL,
[feeCurrency] VARCHAR(50) NULL DEFAULT NULL,
[beneficiaryName] VARCHAR(50) NULL DEFAULT NULL,
[paymentType] VARCHAR(50) NULL DEFAULT NULL,
[feeAmount] VARCHAR(50) NULL DEFAULT NULL,
[beneficiaryAddressNickName] VARCHAR(50) NULL DEFAULT NULL,
[beneficiaryAddressLine1] VARCHAR(200) NULL DEFAULT NULL,
[beneficiaryCity] VARCHAR(50) NULL DEFAULT NULL,
[beneficiaryZipcode] VARCHAR(50) NULL DEFAULT NULL,
[beneficiarycountry] VARCHAR(50) NULL DEFAULT NULL;

GO
DROP procedure IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc]  
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(50),
   @_requestIds nvarchar(max),
   @_featureactionlist nvarchar(50)
AS 
   BEGIN
     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
	 DECLARE @companyId nvarchar(max)
	 DECLARE @customerMatrixIds nvarchar(max)
	 DECLARE @approvalRequestIds nvarchar(max)
	 DECLARE @features nvarchar(max)
	 DECLARE @monetaryActions nvarchar(max)

     
     SET @combinedIds = (select String_agg(customer.id, ',') from [${dbxschemaname}].customer where [${dbxschemaname}].customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
	IF (@combinedIds IS NULL OR @combinedIds = '')
		SET @combinedIds = ''''
	ELSE
		SET @combinedIds = @combinedIds

	IF (@_transactionIds IS NULL OR @_transactionIds = '')
		SET @_transactionIds = ''''
	ELSE
		SET @_transactionIds = @_transactionIds
	
	IF (@_requestIds IS NULL OR @_requestIds = '')
		SET @_requestIds = ''''
	ELSE
		SET @_requestIds = @_requestIds

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') <> 1 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''''
      END

      SET @approvalRequestIds = 
         (  
         SELECT String_agg(CAST(requestapprovalmatrix.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

     
      SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0)
      IF @monetaryActions IS NULL
        BEGIN
          SET @monetaryActions = ''
        END

	DECLARE @companyRequestIds nvarchar(50)
      SET @companyRequestIds = ( SELECT String_agg(bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @monetaryActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END

	DECLARE @requestIds nvarchar(50)
	IF @_requestIds = ''''
          SET @requestIds = @companyRequestIds
    ELSE
		SET @requestIds = @_requestIds

	DECLARE @query nvarchar(50)
	IF @_transactionIds = '''' 
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.requestId,',@_requestIds) 
	ELSE
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.transactionId,',@_transactionIds,'AND [${dbxschemaname}].FIND_IN_SET(bbrequest.featureActionId,',@monetaryActions);

   DECLARE @select_statement nvarchar(50)
	 SET @select_statement = CONCAT(
			'SELECT 
             bbrequest.requestId,
             bbrequest.transactionId,
             bbrequest.status,
			 bbrequest.featureActionId,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
            END) as amICreator,
         
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from [${dbxschemaname}].bbactedrequest where bbactedrequest.action = Approved AND  bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM [${dbxschemaname}].requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.requestId'
				);
    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
    exec(@select_statement)
	End
    MAINLABEL$leave: 
  
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_bbactedrequest_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[update_bbactedrequest_proc]
@_requestId varchar(50)

AS
BEGIN
UPDATE [${dbxschemaname}].[bbactedrequest] SET [softdeleteflag] = '1' WHERE ([requestId] = @_requestId);
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [${dbxschemaname}].[userroleservicedefinition](
	[UserRole_id] [nvarchar](50) NOT NULL,
	[servicedefinitionId] [varchar](50) NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] [datetime] NOT NULL,
	[lastmodifiedts] [datetime] NOT NULL,
	[synctimestamp] [datetime] NOT NULL,
	[softdeleteflag] [bit] NOT NULL,
 CONSTRAINT [PK_userroleservicedefinition_UserRole_id] PRIMARY KEY CLUSTERED 
(
	[UserRole_id] ASC,
	[servicedefinitionId] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT (NULL) FOR [createdby]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT (NULL) FOR [modifiedby]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT (getdate()) FOR [createdts]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT (getdate()) FOR [lastmodifiedts]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT (getdate()) FOR [synctimestamp]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD  DEFAULT ((0)) FOR [softdeleteflag]
GO

ALTER TABLE [${dbxschemaname}].[userroleservicedefinition]  WITH NOCHECK ADD  CONSTRAINT [userroleservicedefinition$userroleservicedefinition_servicedefinition_id] FOREIGN KEY([servicedefinitionId])
REFERENCES [${dbxschemaname}].[servicedefinition] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] CHECK CONSTRAINT [userroleservicedefinition$userroleservicedefinition_servicedefinition_id]
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition]  WITH NOCHECK ADD  CONSTRAINT [userroleservicedefinition$userroleservicedefinition_userrole_id] FOREIGN KEY([UserRole_id])
REFERENCES [${dbxschemaname}].[role] ([id])
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] CHECK CONSTRAINT [userroleservicedefinition$userroleservicedefinition_userrole_id]
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internal_role_to_servicedefinition_mapping_view] (
   [ServiceDefinition_id], 
   [ServiceDefinition_Name], 
   [ServiceDefinition_Description], 
   [ServiceDefinition_Type_id], 
   [ServiceDefinition_Status_id], 
   [InternalRole_id], 
   [InternalRole_Type_id], 
   [InternalRole_Status_id], 
   [InternalRole_Name], 
   [InternalRole_Description])
AS 
   SELECT 
      [${dbxschemaname}].servicedefinition.id AS ServiceDefinition_id, 
      [${dbxschemaname}].servicedefinition.name AS ServiceDefinition_Name, 
      [${dbxschemaname}].servicedefinition.description AS ServiceDefinition_Description, 
      [${dbxschemaname}].servicedefinition.serviceType AS ServiceDefinition_Type_id, 
      [${dbxschemaname}].servicedefinition.status AS ServiceDefinition_Status_id, 
      [${dbxschemaname}].role.id AS InternalRole_id, 
      [${dbxschemaname}].role.Type_id AS InternalRole_Type_id, 
      [${dbxschemaname}].role.Status_id AS InternalRole_Status_id, 
      [${dbxschemaname}].role.Name AS InternalRole_Name, 
      [${dbxschemaname}].role.Description AS InternalRole_Description
   FROM (([${dbxschemaname}].userroleservicedefinition 
      LEFT JOIN [${dbxschemaname}].servicedefinition 
      ON (([${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].userroleservicedefinition.servicedefinitionId))) 
      LEFT JOIN [${dbxschemaname}].role 
      ON (([${dbxschemaname}].role.id = [${dbxschemaname}].userroleservicedefinition.UserRole_id)))
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[internal_user_access_to_customer_on_servicedefinition_proc];

GO
CREATE PROCEDURE [${dbxschemaname}].[internal_user_access_to_customer_on_servicedefinition_proc]
@_roleIds varchar(500),
@_customerId  nvarchar(50) ,
@_customerUsername  nvarchar(50) 

AS
BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE @isCustomerAccessible varchar(6)
	  DECLARE @group_concat_max_len bigint
      SET @group_concat_max_len = 100000000
	  DECLARE @servicedefinitionIds nvarchar(max)
	  DECLARE @contractIdList nvarchar(max)
	  
if(@_customerId = '' and @_customerUsername != '')
	SET @_customerId = (SELECT id from [${dbxschemaname}].customer where [${dbxschemaname}].customer.UserName = @_customerUsername)
	
SET @servicedefinitionIds = (SELECT STRING_AGG( UserRole_id,',') FROM (SELECT DISTINCT UserRole_id
							FROM [${dbxschemaname}].userroleservicedefinition
							WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].userroleservicedefinition.UserRole_id, @_roleIds) > 0) AS T)

SET @contractIdList = (SELECT STRING_AGG(id, ',')  FROM [${dbxschemaname}].contract WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contract.servicedefinitionId, @servicedefinitionIds) > 0)

if @_customerId in (SELECT custId FROM 
(SELECT DISTINCT customerId  AS custId FROM [${dbxschemaname}].contractcustomers 
WHERE customerId IS NOT NULL AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractcustomers.contractId, @contractIdList) > 0
UNION ALL
SELECT DISTINCT coreCustomerId  AS custId FROM [${dbxschemaname}].contractcustomers 
WHERE coreCustomerId IS NOT NULL AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractcustomers.contractId, @contractIdList) > 0) T)
	select 'true' as isCustomerAccessible
ELSE  
	select 'false' as isCustomerAccessible
END
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[infinity_customer_from_servicedefinition_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[infinity_customer_from_servicedefinition_proc]
@_servicedefinitionIds varchar(500)

AS
BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE @group_concat_max_len bigint
      SET @group_concat_max_len = 100000000
	  DECLARE @contractIdList nvarchar(max)
	  

SET @contractIdList = (SELECT STRING_AGG(id, ',') FROM [${dbxschemaname}].contract WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contract.servicedefinitionId, @_servicedefinitionIds) > 0)

SELECT DISTINCT customerId  AS custId FROM [${dbxschemaname}].contractcustomers 
WHERE customerId IS NOT NULL AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractcustomers.contractId, @contractIdList) > 0 

END

GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[account_action_approvers_proc];

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc](
 @_contractId varchar(50),
 @_cif varchar(50),
 @_accountIds text,
 @_approvalActionList varchar(50),
 @_featureId varchar(50)
)
AS
BEGIN
 declare @customerIdList nvarchar(max);
 declare @customerIdListWithNoAccountAccess nvarchar(max);
 declare @NumberOfAccounts int;
 SELECT @customerIdList = STRING_AGG([${dbxschemaname}].[customeraction].Customer_id, ',') from [${dbxschemaname}].[customeraction]
						where 
							[${dbxschemaname}].[customeraction].isAllowed = '0'
						and [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
                        and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].[Account_id], @_accountIds) > 0
                        and [${dbxschemaname}].[customeraction].contractId = @_contractId
                        and [${dbxschemaname}].[customeraction].coreCustomerId = @_cif;

SET @customerIdList = CASE WHEN @customerIdList is null THEN  '' ELSE  @customerIdList END;
SET @NumberOfAccounts = LEN(cast(@_accountIds as nvarchar(max))) - LEN(REPLACE(cast(@_accountIds as nvarchar(max)), ',', '')) + 1;

 select @customerIdListWithNoAccountAccess = (SELECT STRING_AGG(Customer_id, ',') from
                                            ( SELECT Customer_id, Account_id
                                            from [${dbxschemaname}].[customeraccounts]
                                            where [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountIds) > 0
                                            group by Customer_id,Account_id having
                                            count(Account_id) != @NumberOfAccounts ) AS tempcustomeraccounts);
										  
SET @customerIdListWithNoAccountAccess = CASE WHEN @customerIdListWithNoAccountAccess is null THEN  '' ELSE  @customerIdListWithNoAccountAccess END;

SELECT 
	DISTINCT ([${dbxschemaname}].[customer].id ) AS id , ([${dbxschemaname}].[customer].username) AS userName , ([${dbxschemaname}].[membergroup].Name) AS groupId,
										([customer].[FirstName]) AS firstName , ([customer].[LastName]) AS lastName
from 
	([${dbxschemaname}].[customer]
LEFT JOIN [${dbxschemaname}].[contractcustomers] ON ([${dbxschemaname}].[contractcustomers].customerId = [${dbxschemaname}].[customer].[id] and 
[${dbxschemaname}].[contractcustomers].[contractId] = @_contractId and [${dbxschemaname}].[contractcustomers].[coreCustomerId] = @_cif)
LEFT JOIN [${dbxschemaname}].[customergroup] ON ([${dbxschemaname}].[customergroup].[Customer_id] = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[membergroup] ON ([${dbxschemaname}].[membergroup].id = [${dbxschemaname}].[customergroup].Group_id)
LEFT JOIN [${dbxschemaname}].[groupactionlimit] ON ([${dbxschemaname}].[groupactionlimit].Group_id = [${dbxschemaname}].[customergroup].Group_id)
INNER JOIN [${dbxschemaname}].[customeraccounts] ON ([${dbxschemaname}].[customeraccounts].Customer_id = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[contractfeatures] ON ([${dbxschemaname}].[contractfeatures].contractId = @_contractId and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif))
	where 
        [${dbxschemaname}].[contractfeatures].contractId = @_contractId
        and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif
        and [${dbxschemaname}].[contractfeatures].featureId = @_featureId
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id,  @_accountIds) > 0
		and [${dbxschemaname}].[customer].Status_id = 'SID_CUS_ACTIVE'
        and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].[id],  @customerIdList) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].id,  @customerIdListWithNoAccountAccess) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[groupactionlimit].Action_id,@_approvalActionList) > 0;      
END

GO

ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [newId] NVARCHAR(50) NULL;
GO
EXEC sp_executesql N'UPDATE [${dbxschemaname}].[externalaccount] SET [newId] = Id';
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ALTER COLUMN  [newId] nVARCHAR(50) NOT NULL;
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] DROP CONSTRAINT PK_externalaccount_Id;
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] DROP COLUMN [Id];
GO
EXEC sp_rename '[${dbxschemaname}].[externalaccount].newId', 'Id', 'COLUMN';
GO
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD CONSTRAINT [PK_externalaccount_Id] PRIMARY KEY CLUSTERED ([Id] ASC)
GO

ALTER TABLE [${dbxschemaname}].[bulkwiretemplatelineitems] DROP CONSTRAINT [bulkwiretemplatelineitems$FK2_bulkwiretemplatelineitems_payeeID];
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD [newId] NVARCHAR(50) NULL;
GO
EXEC sp_executesql N'UPDATE [${dbxschemaname}].[payee] SET [newId] = Id';
GO
ALTER TABLE [${dbxschemaname}].[payee] ALTER COLUMN  [newId] nVARCHAR(50) NOT NULL;
GO
ALTER TABLE [${dbxschemaname}].[payee] DROP CONSTRAINT PK_payee_Id;
GO
ALTER TABLE [${dbxschemaname}].[payee] DROP COLUMN [Id];
GO
EXEC sp_rename '[${dbxschemaname}].[payee].newId', 'Id', 'COLUMN';
GO
ALTER TABLE [${dbxschemaname}].[payee] ADD CONSTRAINT [PK_payee_Id] PRIMARY KEY CLUSTERED ([Id] ASC)
GO

ALTER TABLE [${dbxschemaname}].[payperson] ADD [newId] NVARCHAR(50) NULL;
GO
EXEC sp_executesql N'UPDATE [${dbxschemaname}].[payperson] SET [newId] = id';
GO
ALTER TABLE [${dbxschemaname}].[payperson] ALTER COLUMN  [newId] nVARCHAR(50) NOT NULL;
GO
ALTER TABLE [${dbxschemaname}].[payperson] DROP CONSTRAINT PK_payperson_Id;
GO
ALTER TABLE [${dbxschemaname}].[payperson] DROP COLUMN [id];
GO
EXEC sp_rename '[${dbxschemaname}].[payperson].newId', 'id', 'COLUMN';
GO
ALTER TABLE [${dbxschemaname}].[payperson] ADD CONSTRAINT [PK_payperson_Id] PRIMARY KEY CLUSTERED ( [id] ASC );
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerttype_view];   
GO
CREATE VIEW [${dbxschemaname}].[alerttype_view] (
   [alerttype_id], 
   [alerttype_Name], 
   [alerttype_AlertCategoryId], 
   [alerttype_Status_id], 
   [alerttype_IsGlobal], 
   [alerttype_DisplaySequence], 
   [alerttype_freqId], 
   [alerttype_freqValue], 
   [alerttype_freqTime], 
   [alerttype_isAccountLevel],
   [userAlerts_count],
   [Alerts_count], 
   [alerttype_softdeleteflag], 
   [alerttypetext_LanguageCode], 
   [alerttypetext_DisplayName], 
   [alerttypetext_Description], 
   [alerttypetext_createdby], 
   [alerttypetext_modifiedby], 
   [alerttypetext_createdts], 
   [alerttypetext_lastmodifiedts], 
   [alerttypetext_synctimestamp],
   [alerttypetext_softdeleteflag])AS
    SELECT 
        [${dbxschemaname}].dbxalerttype.id AS alerttype_id,
        [${dbxschemaname}].dbxalerttype.Name AS alerttype_Name,
        [${dbxschemaname}].dbxalerttype.AlertCategoryId AS alerttype_AlertCategoryId,
        [${dbxschemaname}].dbxalerttype.Status_id AS alerttype_Status_id,
        [${dbxschemaname}].dbxalerttype.IsGlobal AS alerttype_IsGlobal,
        [${dbxschemaname}].dbxalerttype.DisplaySequence AS alerttype_DisplaySequence,
		[${dbxschemaname}].dbxalerttype.defaultFrequencyId AS alerttype_freqId,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyValue AS alerttype_freqValue,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyTime AS alerttype_freqTime,
        [${dbxschemaname}].dbxalerttype.isAccountLevel AS alerttype_isAccountLevel,                 
		(SELECT 
                COUNT(*)
            FROM
                [${dbxschemaname}].alertsubtype
            WHERE
                (alertsubtype.AlertTypeId = dbxalerttype.id) AND (alertsubtype.IsGlobal = 'true') ) AS userAlerts_count,
		(SELECT 
                COUNT(alertsubtype.id)
            FROM
                [${dbxschemaname}].alertsubtype
            WHERE
                (alertsubtype.AlertTypeId = dbxalerttype.id)) AS Alerts_count,
        [${dbxschemaname}].dbxalerttype.softdeleteflag AS alerttype_softdeleteflag,
        [${dbxschemaname}].dbxalerttypetext.LanguageCode AS alerttypetext_LanguageCode,
        [${dbxschemaname}].dbxalerttypetext.DisplayName AS alerttypetext_DisplayName,
        [${dbxschemaname}].dbxalerttypetext.Description AS alerttypetext_Description,
        [${dbxschemaname}].dbxalerttypetext.createdby AS alerttypetext_createdby,
        [${dbxschemaname}].dbxalerttypetext.modifiedby AS alerttypetext_modifiedby,
        [${dbxschemaname}].dbxalerttypetext.createdts AS alerttypetext_createdts,
        [${dbxschemaname}].dbxalerttypetext.lastmodifiedts AS alerttypetext_lastmodifiedts,
        [${dbxschemaname}].dbxalerttypetext.synctimestamp AS alerttypetext_synctimestamp,
        [${dbxschemaname}].dbxalerttypetext.softdeleteflag AS alerttypetext_softdeleteflag
    FROM
        ([${dbxschemaname}].dbxalerttypetext
        INNER JOIN [${dbxschemaname}].dbxalerttype ON (dbxalerttypetext.AlertTypeId = dbxalerttype.id));
GO

ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit]  add CONSTRAINT [FK_servicedefinitionactionlimit_action] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION;

GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD  [paymentStatus] VARCHAR(50)  NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD  [paymentStatus] VARCHAR(50)  NULL;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord]
ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord]
ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord]
ADD [transactionCurrency] VARCHAR(50) DEFAULT NULL;
GO

ALTER TABLE [${dbxschemaname}].[achfile]
ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[achfile]
ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[achfile]
ADD [transactionCurrency] VARCHAR(50) DEFAULT NULL;
GO

ALTER TABLE [${dbxschemaname}].[achtransaction]
ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[achtransaction]
ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[achtransaction]
ADD [transactionCurrency] VARCHAR(50) DEFAULT NULL;
GO

USE [${dbxdbname}]
GO

ALTER TABLE [${dbxschemaname}].[approvalMatrix] ADD [isGroupMatrix] BIT NOT NULL DEFAULT '0';
ALTER TABLE [${dbxschemaname}].[requestapprovalmatrix] ADD [isGroupRule] BIT NOT NULL DEFAULT '0';
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD [groupName] varchar(55) NULL DEFAULT NULL;
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[signatorygroup];
CREATE TABLE [${dbxschemaname}].[signatorygroup] (
    [signatoryGroupId] NVARCHAR(50) NOT NULL,
    [signatoryGroupName] NVARCHAR(50) DEFAULT NULL,
    [signatoryGroupDescription] NVARCHAR(150) DEFAULT NULL,
    [coreCustomerId] NVARCHAR(50) DEFAULT NULL,
    [contractId] NVARCHAR(50) DEFAULT NULL,
    [createdby] NVARCHAR(50) DEFAULT NULL,
    [modifiedby] NVARCHAR(50) DEFAULT NULL,
    [createdts] DATETIME2 DEFAULT GETDATE(),
    [lastmodifiedts] DATETIME2 DEFAULT GETDATE(),
    [synctimestamp] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [softdeleteflag] BIT NOT NULL DEFAULT '0',
    PRIMARY KEY ([signatoryGroupId])
);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[customersignatorygroup];
CREATE TABLE [${dbxschemaname}].[customersignatorygroup] (
    [customerSignatoryGroupId] NVARCHAR(50) NOT NULL,
    [signatoryGroupId] NVARCHAR(50) DEFAULT NULL,
    [customerId] NVARCHAR(50) DEFAULT NULL,
    [createdby] NVARCHAR(50) DEFAULT NULL,
    [modifiedby] NVARCHAR(50) DEFAULT NULL,
    [createdts] DATETIME2 DEFAULT GETDATE(),
    [lastmodifiedts] DATETIME2 DEFAULT GETDATE(),
    [synctimestamp] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [softdeleteflag] BIT NOT NULL DEFAULT '0',
    PRIMARY KEY ([customerSignatoryGroupId]),
    CONSTRAINT [FK_customersignatorygroup_customerId] FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT [FK_customersignatorygroup_signaoryGroupId] FOREIGN KEY ([signatoryGroupId]) REFERENCES [${dbxschemaname}].[signatorygroup] ([signatoryGroupId]) ON DELETE NO ACTION ON UPDATE NO ACTION
    
);
    
CREATE INDEX [FK_customersignatorygroup_customerId] ON [${dbxschemaname}].[customersignatorygroup] ([customerId])
CREATE INDEX [FK_customersignatorygroup_signaoryGroupId] ON [${dbxschemaname}].[customersignatorygroup] ([signatoryGroupId])
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[signatorygroupmatrix];
CREATE TABLE [${dbxschemaname}].[signatorygroupmatrix] (
    [signatoryGroupMatrixId] BIGINT NOT NULL IDENTITY,
    [approvalMatrixId] BIGINT DEFAULT NULL,
    [groupList] NVARCHAR(max) DEFAULT NULL,
    [groupRule] NVARCHAR(max) DEFAULT NULL,
    [createdby] NVARCHAR(50) DEFAULT NULL,
    [modifiedby] NVARCHAR(50) DEFAULT NULL,
    [createdts] DATETIME2 DEFAULT GETDATE(),
    [lastmodifiedts] DATETIME2 DEFAULT GETDATE(),
    [synctimestamp] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [softdeleteflag] BIT NOT NULL DEFAULT '0',
    PRIMARY KEY ([signatoryGroupMatrixId]),
    CONSTRAINT [FK_signatoryGroupMatrix_approvalMatrixId] FOREIGN KEY ([approvalMatrixId]) REFERENCES [${dbxschemaname}].[approvalmatrix] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
 );

CREATE INDEX [FK_signatoryGroupMatrix_approvalMatrixId] ON [${dbxschemaname}].[signatorygroupmatrix] ([approvalMatrixId])
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[signatorygrouprequestmatrix];
CREATE TABLE [${dbxschemaname}].[signatorygrouprequestmatrix] (
    [signatoryGroupRequestMatrixId] NVARCHAR(50) NOT NULL,
    [requestId] NVARCHAR(50) DEFAULT NULL,
    [approvalMatrixId] BIGINT DEFAULT NULL,
    [groupList] NVARCHAR(max) DEFAULT NULL,
    [groupRuleValue] NVARCHAR(max) DEFAULT NULL,
    [pendingGroupList] NVARCHAR(max) DEFAULT NULL,
    [isApproved] BIT NOT NULL DEFAULT '0',
    [createdby] NVARCHAR(50) DEFAULT NULL,
    [modifiedby] NVARCHAR(50) DEFAULT NULL,
    [createdts] DATETIME2 DEFAULT GETDATE(),
    [lastmodifiedts] DATETIME2 DEFAULT GETDATE(),
    [synctimestamp] DATETIME2 NOT NULL DEFAULT GETDATE(),
    [softdeleteflag] BIT NOT NULL DEFAULT '0',
    PRIMARY KEY ([signatoryGroupRequestMatrixId])
);
CREATE INDEX [FK_signatoryGroupRequestMatrix_requestId] ON [${dbxschemaname}].[signatorygrouprequestmatrix] ([requestId])
CREATE INDEX [FK_signatoryGroupRequestMatrix_approvalMatrixId] ON [${dbxschemaname}].[signatorygrouprequestmatrix] ([approvalMatrixId])
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ADD [accountId] VARCHAR(50) DEFAULT NULL;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[get_signatorygroup_approvers_proc];
GO
CREATE  PROCEDURE [${dbxschemaname}].[get_signatorygroup_approvers_proc](
	@_groupList NVARCHAR(max)
)
AS BEGIN
    DECLARE @execStmt NVARCHAR(max);
    set @execStmt  = 'select customerId from [${dbxschemaname}].customersignatorygroup where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customersignatorygroup.signatoryGroupId ,''' + REPLACE(REPLACE(REPLACE(@_groupList, '[', ''),']',''),' ','') + ''' ) <> 0';
	exec(@execStmt);
END
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] ADD  [errorDescription] VARCHAR(100)  NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] ADD  [errorDescription] VARCHAR(100)  NULL;

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[approvalmode];
CREATE TABLE [${dbxschemaname}].[approvalmode] (
    [id] NVARCHAR(50) NOT NULL,
    [coreCustomerId] NVARCHAR(50) NOT NULL,
    [contractId] NVARCHAR(50) NOT NULL,
    [isGroupLevel] BIT NOT NULL DEFAULT '0',
    PRIMARY KEY ([id])
);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_pending_group_list_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_pending_group_list_proc]
 @_requestId nvarchar(50) 
AS

BEGIN
	
	SELECT [signatorygroup].signatoryGroupId, [signatorygroup].signatoryGroupName  
	FROM [${dbxschemaname}].[signatorygroup] 
	WHERE [${dbxschemaname}].FIND_IN_SET([signatorygroup].signatoryGroupId,
	(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].pendingGroupList,']',''),'[',''),'"',''),',')
		FROM [${dbxschemaname}].[signatorygrouprequestmatrix] WHERE 
		[signatorygrouprequestmatrix].requestId = @_requestId AND [signatorygrouprequestmatrix].isApproved = 'false')) > 0;
		
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[increment_receivedapprovals_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[increment_receivedapprovals_proc]
 @_requestId nvarchar(50),
 @_approvalMatrixId nvarchar(50) 
AS

BEGIN
	UPDATE [${dbxschemaname}].[requestapprovalmatrix] SET [receivedApprovals] = [receivedApprovals] + 1 WHERE ([requestId] = @_requestId AND [approvalMatrixId] = @_approvalMatrixId);
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_customersignatorygroup_details_proc]
GO
CREATE  PROCEDURE [${dbxschemaname}].[fetch_customersignatorygroup_details_proc]
 @_customerId nvarchar(50)
AS
BEGIN
SELECT 
	[customersignatorygroup].[customerSignatoryGroupId] AS customerSignatoryGroupId,
	[customersignatorygroup].[signatoryGroupId] AS signatoryGroupId,
	[customersignatorygroup].[customerId] AS customerId,
	[signatorygroup].[coreCustomerId] AS coreCustomerId,
	[signatorygroup].[contractId] AS contractId,
	[signatorygroup].[signatoryGroupName] AS signatoryGroupName,
	[signatorygroup].[signatoryGroupDescription] AS signatoryGroupDescription
from 
	[${dbxschemaname}].[customersignatorygroup]
LEFT JOIN [${dbxschemaname}].[signatorygroup] ON [signatorygroup].signatoryGroupId = [customersignatorygroup].signatoryGroupId
	where 
        [customersignatorygroup].customerId = @_customerId;           
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_request_history_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_request_history_proc]  
   @_customerId nvarchar(50),
   @_requestId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @companyId nvarchar(max)

      SET @companyId = (select String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)), ',') from [${dbxschemaname}].contractcustomers where contractcustomers.customerId = @_customerId)
      IF @companyId is NULL      
        SET @companyId = '';

	  SELECT 
         [bbactedrequest].[approvalId] AS approvalId, 
         [bbactedrequest].[requestId] AS requestId, 
         [bbactedrequest].[companyId] AS companyId, 
         [bbactedrequest].[createdby] AS requestActedby, 
         [bbactedrequest].[status] AS status, 
         [bbactedrequest].[comments] AS comments, 
         [bbactedrequest].[action] AS action,
		 [bbactedrequest].[groupName] AS groupName,		 
         [bbactedrequest].[createdts] AS actionts, 
         [bbactedrequest].[softdeleteflag] AS softdeleteflag, 
         [customer].[UserName] AS userName, 
         CASE 
            WHEN [${dbxschemaname}].[bbactedrequest].[createdby] IS NULL THEN 'System'
            ELSE (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[FirstName]) <> 0) THEN [${dbxschemaname}].[customer].[FirstName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[MiddleName]) <> 0) THEN [${dbxschemaname}].[customer].[MiddleName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[LastName]) <> 0) THEN [${dbxschemaname}].[customer].[LastName]
               ELSE ''
            END)
         END AS customerName, 
         [customer].[FullName] AS customerFullName
      FROM ([${dbxschemaname}].[bbactedrequest] 
         LEFT JOIN [${dbxschemaname}].[customer] 
         ON ([bbactedrequest].[createdby] = [customer].[id]))
      WHERE 
         CAST([bbactedrequest].[requestId] as nvarchar(max))= @_requestId AND 
         [${dbxschemaname}].FIND_IN_SET(bbactedrequest.companyId , @companyId) <> 0
   END
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
    [${dbxschemaname}].alertsubtype.isAutoSubscribeEnabled AS alertsubtype_isAutoSubscribeEnabled,
    [${dbxschemaname}].alertsubtypetext.languageCode AS alertsubtypetext_languageCode,
    [${dbxschemaname}].alertsubtypetext.description AS alertsubtypetext_description,
    [${dbxschemaname}].alertsubtype.externalSystem AS alertsubtype_externalSystem,
    [${dbxschemaname}].alertsubtypetext.displayName AS alertsubtypetext_displayName
from
    ([${dbxschemaname}].[alertsubtype]
join [${dbxschemaname}].[alertsubtypetext] on
    ((alertsubtypetext.alertSubTypeId = alertsubtype.id)));
    
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_approvalqueueforgroup_proc]
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueueforgroup_proc]  
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(50),
   @_requestIds nvarchar(max),
   @_featureactionlist nvarchar(max)
AS 
   BEGIN
     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
	 DECLARE @companyId nvarchar(max)
	 DECLARE @customerGroupIds nvarchar(max)
	 DECLARE @approvalRequestIds nvarchar(max)
	 DECLARE @features nvarchar(max)
	 DECLARE @monetaryActions nvarchar(max)
	 DECLARE @groupIds nvarchar(max)
	 DECLARE @strLen nvarchar(20)
	 DECLARE @reqIds nvarchar(max)
	 DECLARE @SubStrLen nvarchar(20)

	
     SET @combinedIds = (select String_agg(customer.id, ',') from [${dbxschemaname}].customer where [${dbxschemaname}].customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
	IF (@combinedIds IS NULL OR @combinedIds = '')
		SET @combinedIds = ''
	ELSE
		SET @combinedIds = @combinedIds

	IF (@_transactionIds IS NULL OR @_transactionIds = '')
		SET @_transactionIds = ''
	ELSE
		SET @_transactionIds = @_transactionIds
	
	IF (@_requestIds IS NULL OR @_requestIds = '')
		SET @_requestIds = ''
	ELSE
		SET @_requestIds = @_requestIds

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId);
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

	  SET @customerGroupIds = ( SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)),',') FROM [${dbxschemaname}].customersignatorygroup WHERE [${dbxschemaname}].FIND_IN_SET(customersignatorygroup.customerId, @combinedIds) <> 0);
        IF @customerGroupIds IS NULL
         SET @customerGroupIds = ''
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') <> 1 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END

	  SET @approvalRequestIds = '';
        SET @groupIds = @customerGroupIds;
        do_this: WHILE 1=1 BEGIN
			SET @strLen = LEN(@groupIds);
 			SET @reqIds = ( SELECT Distinct (String_agg( CAST([signatorygrouprequestmatrix].[requestId] as nvarchar(max)) , ',')) FROM [${dbxschemaname}].signatorygrouprequestmatrix 
					WHERE NOT [${dbxschemaname}].FIND_IN_SET(signatorygrouprequestmatrix.requestId, @approvalRequestIds) > 0 AND signatorygrouprequestmatrix.isApproved = '0' AND [${dbxschemaname}].FIND_IN_SET( [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].[pendingGroupList],'[',''),']',''),' ','')) > 0 );
				if (@approvalRequestIds = '' OR @approvalRequestIds IS NULL) 
					SET @approvalRequestIds = @reqIds; 
				else  
					SET @approvalRequestIds = CONCAT(@approvalRequestIds, CONCAT(',',@reqIds) );
			SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
			IF LEN(@groupIds) <= 0 BEGIN
			  BREAK;
			END 
		END;

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
	IF @_requestIds = ''
          SET @requestIds = @companyRequestIds
    ELSE
		SET @requestIds = @_requestIds

	DECLARE @query nvarchar(max)
	IF @_transactionIds = '' 
	SET @query = '[${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),''' + @requestIds + ''') > 0 '
	ELSE
	SET @query = '[${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.transactionId AS nvarchar(max)),''' + @_transactionIds + ''') > 0 AND [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.featureActionId AS nvarchar(max)),''' + @monetaryActions + ''') > 0 '
	
    DECLARE @select_statement nvarchar(max)
	 SET @select_statement = 
			'SELECT 
             distinct bbrequest.requestId, 
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
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from [${dbxschemaname}].bbactedrequest where bbactedrequest.action = ''Approved'' AND  bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0) 
							as receivedApprovals
				FROM
				 [${dbxschemaname}].bbrequest
				LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN [${dbxschemaname}].approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ' + @query

    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
    exec(@select_statement)
	End
    MAINLABEL$leave: 
	
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] ADD  [requestStatus] VARCHAR(20)  NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ADD  [requestStatus] VARCHAR(20)  NULL;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD  [errorDescription] VARCHAR(250)  NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD  [errorDescription] VARCHAR(250)  NULL;
GO

  
DROP procedure IF EXISTS  [${dbxschemaname}].[signatorygroup_create_proc];
GO



CREATE PROCEDURE  [${dbxschemaname}].[signatorygroup_create_proc]
@signatoryGroupValues NVARCHAR(max),
@customerSignatoryGroupValues NVARCHAR(max)
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
set @matrixRecord =  [${dbxschemaname}].SUBSTRING_INDEX(@signatoryGroupValues, ',', 1 );
set @matrixComma1 = REPLACE(@matrixRecord, ';', ',');
set @matrixComma = REPLACE(@matrixComma1, '"', '''');
set @query = 'INSERT INTO [${dbxschemaname}].signatorygroup(signatorygroup.signatoryGroupId,signatorygroup.signatoryGroupName,signatorygroup.signatoryGroupDescription,signatorygroup.coreCustomerId,signatorygroup.contractId,signatorygroup.createdby) values ('+@matrixComma+')';
execute (@query); 
set @length1 = LEN(@customerSignatoryGroupValues) - LEN(REPLACE(@customerSignatoryGroupValues, ',', ''));
WHILE @index1 != @length1 BEGIN
set @index1 = @index1 + 1;
set @signatory = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@customerSignatoryGroupValues, ',', @index1), ',', -1);
set @signatoriesComma1 = REPLACE(@signatory, ';', ',');
set @signatoriesComma = REPLACE(@signatoriesComma1, '"', ''''); 
set @query = 'INSERT INTO [${dbxschemaname}].customersignatorygroup(customersignatorygroup.customerSignatoryGroupId, customersignatorygroup.signatoryGroupId, customersignatorygroup.customerId, customersignatorygroup.createdby) values ('+@signatoriesComma+')';
execute (@query);
END
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc]
@_matrixValues NVARCHAR(max),
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
set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;

WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1;
IF @index1 = @length + 1 
BREAK
ELSE
BEGIN
set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
set @matrixComma = REPLACE(@matrixRecord, ';', ',');
set @matrixComma = REPLACE(@matrixComma,'"','''');
set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit) VALUES (',@matrixComma,');');
execute (@query);
set @sigValues = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_signatorymatrixValues, '#', @index1), '#', -1 );
SET @id = @@IDENTITY;
SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1 );
SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1 );
set @id = REPLACE(@id,'"','''');
set @groupList = REPLACE(@groupList,'"','''');
set @groupRule = REPLACE(@groupRule,'"','''');
set @query = ('INSERT INTO [${dbxschemaname}].signatorygroupmatrix(signatorygroupmatrix.approvalMatrixId,signatorygroupmatrix.groupList,signatorygroupmatrix.groupRule) VALUES (')+@id+','+''''+@groupList+''''+','+''''+@groupRule+''''+')';
execute (@query);
CONTINUE
END
END
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[signatorygroup_update_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[signatorygroup_update_proc]
   @_sigGroupValues  nvarchar(max), 
   @_newSigValues nvarchar(max),
   @_deleteSigValues  nvarchar(max)
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
set @sigGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', 1 );
set @SigGroupName = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', 2 ),';',-1);
set @sigGroupDes = [${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', -1 );
set @SigGroupId = REPLACE(@SigGroupId, '"', '''');
IF @SigGroupName IS NOT NULL AND @SigGroupName != ''
BEGIN
set @SigGroupName = REPLACE(@SigGroupName, '"', ''''); 
set @query = ('UPDATE [${dbxschemaname}].signatorygroup SET signatorygroup.signatoryGroupName = ')+@SigGroupName+(' WHERE signatorygroup.signatoryGroupId = ')+@sigGroupId+('');
execute (@query);
END
IF @sigGroupDes IS NOT NULL AND @sigGroupDes != ''
BEGIN
set @sigGroupDes = REPLACE(@sigGroupDes, '"', ''''); 
set @query = ('UPDATE [${dbxschemaname}].signatorygroup SET signatorygroup.signatoryGroupDescription = ')+@sigGroupDes+(' WHERE signatorygroup.signatoryGroupId = ')+@sigGroupId+('');
execute (@query);
END

IF @_newSigValues IS NOT NULL AND @_newSigValues != ''
BEGIN
set @length1 = LEN(@_newSigValues) - LEN(REPLACE(@_newSigValues, ',', ''))+1;
set @index1 = 0;
WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1;
IF @index1 = @length1 + 1 
BREAK
ELSE
BEGIN
set @signatory = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_newSigValues, ',', @index1), ',', -1);
set @signatoriesComma = REPLACE(@signatory, ';', ',');
set @signatoriesComma = REPLACE(@signatoriesComma, '"', ''''); 
set @query = ('INSERT INTO [${dbxschemaname}].customersignatorygroup(customersignatorygroup.customerSignatoryGroupId, customersignatorygroup.signatoryGroupId, customersignatorygroup.customerId, customersignatorygroup.createdby) values (')+@signatoriesComma+(')');
execute (@query);
CONTINUE
END
END
END
 
IF @_deleteSigValues IS NOT NULL AND @_deleteSigValues != ''
BEGIN
set @length1 = LEN(@_deleteSigValues) - LEN(REPLACE(@_deleteSigValues, ',', ''))+1;
set @index1 = 0;
WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1
IF @index1 = @length1 + 1 
BREAK
ELSE
BEGIN
set @signatory = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_deleteSigValues, ',', @index1), ',', -1);
set @sigGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@signatory, ';', 1 );
set @custId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@signatory, ';', 2 ),';',-1);
set @sigGroupId = REPLACE(@sigGroupId, '"', '''');
set @custId = REPLACE(@custId, '"', '''');
set @query = ('DELETE FROM [${dbxschemaname}].customersignatorygroup WHERE customersignatorygroup.signatoryGroupId =')+@sigGroupId+('AND customersignatorygroup.customerId=')+@custId+('')
execute (@query);
CONTINUE
END
END
END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemuser_permission_proc];
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[systemuser_permission_proc]  
   @_userId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
         up.Permission_id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].userpermission  AS up 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (up.Permission_id = p.id))
      WHERE up.User_id = @_userId AND p.Status_id = 'SID_ACTIVE'
       UNION
      SELECT 
         p.id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].userrole  AS ur 
         INNER JOIN [${dbxschemaname}].role  AS r 
         ON (r.id = ur.Role_id) 
         INNER JOIN [${dbxschemaname}].rolepermission  AS rp 
         ON (ur.Role_id = rp.Role_id) 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (rp.Permission_id = p.id))
      WHERE ur.User_id = @_userId AND p.Status_id = 'SID_ACTIVE'

   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[updateRequestApprovalMatrix_proc];
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_requestapprovalmatrix_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[update_requestapprovalmatrix_proc]
 @_requestApprovalMatrixId nvarchar(250)
AS
	
BEGIN
	UPDATE [${dbxschemaname}].[requestapprovalmatrix] SET [receivedApprovals] = [receivedApprovals] + 1 WHERE [${dbxschemaname}].FIND_IN_SET(requestapprovalmatrix.id, @_requestApprovalMatrixId) > 0;
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_requestapprovalmatrix_details_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_requestapprovalmatrix_details_proc]  
   @_requestId nvarchar(50),
   @_customerId nvarchar(50)
AS 
   BEGIN

	 SELECT 
		bb.requestId AS requestId, 
		am.id AS approvalMatrixId, 
		ram.id AS requestApprovalMatrixId, 
		ram.receivedApprovals AS receivedApprovals, 
		IIF( ar.numberOfApprovals = -1, na.numberOfApprovals, ar.numberOfApprovals ) AS numberOfApprovals, 
		cam.customerId AS customerId
	 FROM [${dbxschemaname}].bbrequest  AS bb 
	 JOIN [${dbxschemaname}].requestapprovalmatrix  AS ram on
		bb.requestId = ram.requestId and CAST(bb.requestId as nvarchar(max)) = @_requestId
	 JOIN [${dbxschemaname}].approvalmatrix  AS am on
		ram.approvalMatrixId = am.id
	 JOIN [${dbxschemaname}].customerapprovalmatrix  AS cam on
		cam.customerId = @_customerId
	 JOIN [${dbxschemaname}].approvalrule  AS ar on
		am.approvalruleId = ar.id
	 JOIN (
			SELECT approvalMatrixId, count_big(DISTINCT (customerId)) AS numberOfApprovals
			FROM [${dbxschemaname}].customerapprovalmatrix
			GROUP BY approvalmatrixId
		   ) AS na on 
	    na.approvalMatrixId = am.id
		   
   END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[systemroles_permission_proc]  
   @_roleIds varchar(100)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT DISTINCT
         p.id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].rolepermission  AS rp 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (rp.Permission_id = p.id))
      WHERE p.Status_id = 'SID_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) <> 0 order by [id]
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_transaction_status_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[update_transaction_status_proc]
	@_featureId NVARCHAR(50),
	@_status NVARCHAR(50),
	@_confirmationNumber NVARCHAR(50)
AS
	BEGIN
		DECLARE @isSuccess NVARCHAR(50);
		
		SET @isSuccess = 'true';
		 
		IF (@_featureId = 'ACH_COLLECTION') 
			UPDATE [${dbxschemaname}].[achtransaction] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'ACH_PAYMENT') 
			UPDATE [${dbxschemaname}].[achtransaction] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'ACH_FILES')   
			UPDATE [${dbxschemaname}].[achfile] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'BILL_PAY')   
			UPDATE [${dbxschemaname}].[billpaytransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'DOMESTIC_WIRE_TRANSFER')   
			UPDATE [${dbxschemaname}].[wiretransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'INTERNATIONAL_WIRE_TRANSFER')   
			UPDATE [${dbxschemaname}].[wiretransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER')   
			UPDATE [${dbxschemaname}].[internationalfundtransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'INTER_BANK_ACCOUNT_FUND_TRANSFER')   
			UPDATE [${dbxschemaname}].[interbankfundtransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'INTRA_BANK_FUND_TRANSFER')   
			UPDATE [${dbxschemaname}].[intrabanktransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'P2P')   
			UPDATE [${dbxschemaname}].[p2ptransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE IF (@_featureId = 'TRANSFER_BETWEEN_OWN_ACCOUNT')   
			UPDATE [${dbxschemaname}].[ownaccounttransfers] SET [status] = @_status WHERE [confirmationNumber] = @_confirmationNumber;
		ELSE
			SET @isSuccess = 'false';
		
		SELECT @isSuccess AS isSuccess;
	END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_achtransaction_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_achtransaction_proc]  
   @_customerId nvarchar(50),
   @_transactionId nvarchar(max),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(max),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(max),
   @_sortByParam nvarchar(max),
   @_sortOrder nvarchar(max),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   
   BEGIN
	SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     DECLARE @validAccountsJoin nvarchar(max)
	 DECLARE @isSelfApprovalEnabled bit
	 DECLARE @notCreatedBySelf nvarchar(max)
	 

      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

      DECLARE
            @db_null_statement int

            DECLARE
               @db_null_statement$2 int

      SET @combinedIds = ( SELECT String_agg(customer.id, ',') FROM [${dbxschemaname}].customer WHERE customer.combinedUserId = @_customerId)
      IF @combinedIds IS NULL
         BEGIN
            SET @combinedIds = @_customerId
         END
      ELSE 
         SET @combinedIds = (@_customerId + ',' + @combinedIds)

	  SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from [${dbxschemaname}].application);

	  IF (@isSelfApprovalEnabled = 0)
		SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(achtransaction.createdby, ''', @combinedIds,''') = 0')
	  ELSE
		SET @notCreatedBySelf = '';

		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='') THEN '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE @_filterByValue END
        SET @_transactionId = CASE WHEN (@_transactionId = '' OR @_transactionId IS NULL) THEN '%' ELSE @_transactionId END
		SET @numOfParams = 0;

      IF LEN(@_filterByParam) > 0
         BEGIN
            SET @numOfParams = LEN(@_filterByParam) - LEN(replace(@_filterByParam, ',', '')) + 1
         END

	SET @searchQuery = ''

      WHILE (@idx <= @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx), ',', -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',', @idx), ',', -1 );
            SET @searchQuery = (@searchQuery + ' AND (' + @filterParam + ' LIKE ''' + @filterValue + ''' )')
            SET @idx = @idx + 1

         END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         BEGIN         
            SET @companyId = ''
         END

      IF @_featureactionlist IS NULL
         BEGIN
            GOTO MAINLABEL$leave
         END

		SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam is NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder is NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         BEGIN
            SET @customerMatrixIds = ''
         END

      SET @alreadyApprovedIds = ( SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].bbactedrequest.action = 'Approved')

      IF @alreadyApprovedIds IS NULL
         BEGIN
            SET @alreadyApprovedIds = ''
         END
      SET @approvalRequestIds = 
         (
            SELECT String_agg(CAST(requestId as nvarchar(max)) ,',')
            FROM 
               [${dbxschemaname}].requestapprovalmatrix 
                  INNER JOIN [${dbxschemaname}].approvalmatrix 
                  ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id 
                  INNER JOIN [${dbxschemaname}].approvalrule 
                  ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
            WHERE 
               [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)), @customerMatrixIds) <> 0 AND 
               NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0 AND 
               (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < 
               (
                  SELECT count_big(DISTINCT ([${dbxschemaname}].customerapprovalmatrix.customerId))
                  FROM [${dbxschemaname}].customerapprovalmatrix
                  WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
               )) OR ([${dbxschemaname}].approvalrule.numberOfApprovals <> -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < [${dbxschemaname}].approvalrule.numberOfApprovals))            
         )
      IF @approvalRequestIds IS NULL
         BEGIN
            SET @approvalRequestIds = ''
         END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, ''') + (@combinedIds) + (''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achtransaction.requestId AS nvarchar(max)),  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].achtransaction.status = ''Pending'' ') + @notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN (' AND [${dbxschemaname}].achtransaction.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_transactionId = '%') THEN (' AND NOT [${dbxschemaname}].achtransaction.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END
      SET @validAccountsJoin = 
         CASE 
            WHEN (@_queryType = '') THEN 
            (
               ' INNER JOIN (SELECT DISTINCT Account_id, REPLACE(Action_id, ''_VIEW'', ''_CREATE'') as Action_id
                 FROM [${dbxschemaname}].customeraction WHERE [${dbxschemaname}].customeraction.Customer_id = '''+@_customerId+''' AND [${dbxschemaname}].customeraction.isAllowed = ''1''
                 AND [${dbxschemaname}].customeraction.Account_id is NOT null AND [${dbxschemaname}].customeraction.Action_id like ''%_VIEW'') as can ON ([${dbxschemaname}].achtransaction.featureActionId = can.Action_id
                 AND [${dbxschemaname}].achtransaction.fromAccount = can.Account_id) '
            )
            ELSE ''
         END

		 SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery ELSE 
							(@searchQuery+ ' AND (achtransaction.templateName LIKE ''%'+@_searchString+'%'' OR customeraccounts.AccountName LIKE ''%'+
							@_searchString+'%'' OR bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR 
							achtransaction.transaction_id LIKE ''%'+@_searchString+'%'' OR achtransaction.fromAccount LIKE ''%'+@_searchString+'%'' )') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' ELSE
								(' OFFSET '+@_pageOffset+ '   ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY ') END 
      SET @features = 
         (
            SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0
         )
      IF @features IS NULL
         BEGIN
            SET @features = ''
         END
      SET @createActions = 
         (
            SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE'
         )
      IF @createActions IS NULL
         BEGIN
            SET @createActions = ''
         END
      SET @companyRequestIds = 
         (
            SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].bbrequest
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
         )
      IF @companyRequestIds IS NULL
         BEGIN
            SET @companyRequestIds = ''
         END         

      SET @customerAcounts = 
         (
            SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].customeraccounts
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id, @combinedIds) <> 0
         )
      IF @customerAcounts IS NULL
         BEGIN
            SET @customerAcounts = ''
         END

	
	SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

      SET @select_statement = '

         SELECT * FROM
            (SELECT 
         [${dbxschemaname}].achtransaction.transaction_id AS transaction_id,
         [${dbxschemaname}].achtransaction.fromAccount AS fromAccount,
         [${dbxschemaname}].achtransaction.effectiveDate AS effectiveDate,
         [${dbxschemaname}].achtransaction.requestId AS requestId,
         [${dbxschemaname}].achtransaction.createdby AS createdby,
         [${dbxschemaname}].achtransaction.createdts AS createdts,
         [${dbxschemaname}].achtransaction.maxAmount AS maxAmount,
		 ( CASE 
				WHEN [${dbxschemaname}].bbrequest.status is NULL THEN [${dbxschemaname}].achtransaction.status
                ELSE [${dbxschemaname}].bbrequest.status 
			END ) AS status,
         [${dbxschemaname}].achtransaction.transactionType_id AS transactionType_id,
         [${dbxschemaname}].achtransaction.templateType_id AS templateType_id,
         [${dbxschemaname}].achtransaction.companyId AS companyId,
         [${dbxschemaname}].achtransaction.templateRequestType_id AS templateRequestType_id,
         [${dbxschemaname}].achtransaction.softDelete AS softDelete,
         [${dbxschemaname}].achtransaction.templateName AS templateName,
         [${dbxschemaname}].achtransaction.template_id AS template_id,
         [${dbxschemaname}].achtransaction.totalAmount AS totalAmount,
         [${dbxschemaname}].achtransaction.featureActionId AS featureActionId,
         [${dbxschemaname}].achtransaction.confirmationNumber AS confirmationNumber,
         ( CASE 
            WHEN [${dbxschemaname}].customeraccounts.AccountName is NULL THEN ''AccountName'' 
                ELSE [${dbxschemaname}].customeraccounts.AccountName 
            END ) AS accountName,
         [${dbxschemaname}].customer.UserName AS userName,
         [${dbxschemaname}].bbtransactiontype.transactionTypeName AS transactionTypeName,
         [${dbxschemaname}].bbtemplatetype.templateTypeName AS templateTypeName,
         [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
         [${dbxschemaname}].bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achtransaction.createdby AS nvarchar(max)),CAST(''' + @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)), CAST('''+ @approvalRequestIds +''' as nvarchar(max))) >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         (((((([${dbxschemaname}].achtransaction
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customeraccounts.Customer_id)
            AND ([${dbxschemaname}].achtransaction.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
         LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].achtransaction.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].achtransaction.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].achtransaction.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achtransaction.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achtransaction.softDelete = ''0''
            AND ( [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.companyId, '''+ @companyId + ''') <> 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, CAST('''+ @combinedIds + ''' as nvarchar(max)))<>0)
            AND [${dbxschemaname}].achtransaction.transaction_id LIKE '''+ @_transactionId + '''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.fromAccount, CAST('''+ @customerAcounts + ''' as nvarchar(max)))<>0 
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.featureActionId, CAST(''' + @createActions + ''' as nvarchar(max)))<>0 ' + 
                @queryTypecondition + ' ' +
                @searchQuery + ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)), CAST(''' + @companyRequestIds + ''' as nvarchar(max)))<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' ' + @_sortOrder + ' ' +
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	 DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:

GO


DROP procedure IF EXISTS [${dbxschemaname}].[fetch_achfiles_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_achfiles_proc]  
   @_customerId nvarchar(50),
   @_achFile_id nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 

   BEGIN

     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
	 DECLARE @isSelfApprovalEnabled bit
	 DECLARE @notCreatedBySelf nvarchar(max)
     
      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

     SET @combinedIds = (select String_agg(CAST(customer.id as nvarchar(max)), ',') from [${dbxschemaname}].customer where customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)

	   SET @isSelfApprovalEnabled = (select [isSelfApprovalEnabled] from [${dbxschemaname}].application)

	   IF(@isSelfApprovalEnabled = 0)
		BEGIN
			SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(achfile.createdby, ''',@combinedIds,''')=0')
		END
	   ELSE 
		BEGIN
			SET @notCreatedBySelf = '';
		END
		
		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='' ) THEN  '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE  @_filterByValue END
        SET @_achFile_id = CASE WHEN (@_achFile_id = '' OR @_achFile_id IS NULL) THEN '%' ELSE @_achFile_id END


      IF datalength(@_filterByParam) > 0
         SET @numOfParams = datalength(@_filterByParam) - datalength(replace(@_filterByParam, ',', '')) + 1
      
      WHILE (@idx < @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByParam, @idx), -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByValue,   @idx), -1 );
            SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
            SET @idx = @idx + 1   
         END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

        SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam IS NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder IS NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
      SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END


      SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							)

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, ''' + (@combinedIds) + ''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achfile.requestId AS nvarchar(max)),  ''' + (@approvalRequestIds) + ''')<>0  AND [${dbxschemaname}].achfile.status = ''Pending'' ') + @notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN ('AND [${dbxschemaname}].achfile.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_achFile_id = '%') THEN (' AND NOT [${dbxschemaname}].achfile.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END


		SET @searchQuery = CASE WHEN (@_searchString IS NULL OR @_searchString = '') THEN @searchQuery ELSE 
                (@searchQuery+ ' AND (achfile.achFileName LIKE ''%'+@_searchString+'%'' OR achfile.requestType LIKE ''%'+@_searchString+'%'')') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset IS NULL OR @_pageOffset = '' OR @_pageSize IS NULL OR @_pageSize = '') THEN
		'' ELSE (' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT  ' +@_pageSize+'  ROWS ONLY') END


      SET @features = ( SELECT String_agg([${dbxschemaname}].featureaction.Feature_id ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg([${dbxschemaname}].featureaction.id,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg([${dbxschemaname}].bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END


	 SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='''' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

	  
	  
	 SET @select_statement = ' 
	  SELECT * FROM
        (SELECT 
         achfile.achFile_id AS achFile_id,
         achfile.achFileName AS achFileName,
         achfile.featureActionId AS featureActionId,
         achfile.debitAmount AS debitAmount,
         achfile.approvalAccounts AS approvalAccounts,
         achfile.debitAccounts AS debitAccounts,
         achfile.createdby AS createdby,
         achfile.createdts AS createdts,
         achfile.requestType AS requestType,
         achfile.numberOfCredits AS numberOfCredits,
         achfile.numberOfDebits AS numberOfDebits,
         achfile.numberOfPrenotes AS numberOfPrenotes,
         achfile.requestId AS requestId,
         achfile.contents AS contents,
         achfile.fileSize AS fileSize,
         achfile.softDelete AS softDelete,
         achfile.creditAmount AS creditAmount,
         achfile.numberOfRecords AS numberOfRecords,
         achfile.achFileFormatType_id AS achFileFormatType_id,
		 ( CASE 
				WHEN bbrequest.status is NULL THEN achfile.status
                ELSE bbrequest.status 
			END ) AS status,
         achfile.companyId AS companyId,
         achfile.confirmationNumber AS confirmationNumber,
         customer.UserName AS userName,
         achfileformattype.fileType AS achFileFormatType,
         bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         (((([${dbxschemaname}].achfile
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achfile.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].achfileformattype ON ([${dbxschemaname}].achfile.achFileFormatType_id = [${dbxschemaname}].achfileformattype.id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achfile.softDelete = ''0''
            AND ([${dbxschemaname}].FIND_IN_SET(achfile.companyId, '''+ @companyId + ''') <> 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, '''+ @combinedIds + ''')<>0)
            AND [${dbxschemaname}].achfile.achFile_id LIKE '''+@_achFile_id + '''
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.featureActionId, '''+ @createActions + ''')<>0 '+
                @queryTypecondition + ' '+
                @searchQuery + ') AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+ @companyRequestIds +''')<>0 
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' '+ @_sortOrder + ' ' +
            @paginationQuery
            EXEC(@select_statement)
	DROP TABLE #approvalCount1;
	DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkpayment_template_po_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[bulkpayment_template_po_create_proc](
	@_povalues NVARCHAR(MAX)
)
AS
	BEGIN
		DECLARE @index1 INT;
		DECLARE @numOfPos INT;
		DECLARE @posData NVARCHAR(MAX);
		DECLARE @query NVARCHAR(MAX);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		SET @index1 = 0;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO [${dbxschemaname}].bulkpaymenttemplatepos(paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,featureActionId,companyId,roleId,status,createdby,beneficiaryName,paymentMethod,currency,amount,feesPaidBy,paymentReference,swift,beneficiaryNickName,beneficiaryAddress,accType,beneficiaryType,addToExistingFlag,beneficiaryIBAN,bankName)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkpayment_request_po_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[bulkpayment_request_po_create_proc](
	@_povalues NVARCHAR(MAX)
)
AS
	BEGIN
		DECLARE @index1 INT;
		DECLARE @numOfPos INT;
		DECLARE @posData NVARCHAR(MAX);
		DECLARE @query NVARCHAR(MAX);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		SET @index1 =0;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO [${dbxschemaname}].bulkpaymentrequestpos(paymentrequestPOId,paymentrequestId,paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,bankName,swift,featureActionId,companyId,roleId,status,currency,amount,feesPaidBy,paymentReference,debitAccountIBAN,beneficiaryIBAN,beneficiaryName,beneficiaryNickName,beneficiaryAddress,accountWithBankBIC,customer,paymentMethod,accType,createdby)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].approvalmatrix_default_create_proc(
 @_actionIds NVARCHAR(max) , 
@_contractId NVARCHAR(50) ,
@_accountIds NVARCHAR(max) ,
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
 
IF @_actionIds IS NULL OR  @_actionIds = ''
	GOTO MAINLABEL$leave
	
IF @_contractId IS NULL OR  @_contractId = ''
	GOTO MAINLABEL$leave
	
IF @_cif IS NULL OR  @_cif = ''
	GOTO MAINLABEL$leave

IF @_accountIds IS NULL OR  @_accountIds = ''
	GOTO MAINLABEL$leave
	
set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
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

MAINLABEL$leave: 

END;
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
                (alertsubtype.AlertTypeId = dbxalerttype.id) AND (alertsubtype.isGlobal = 1) ) AS userAlerts_count,
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
(case [${dbxschemaname}].[membergroup].[isApplicabletoAllServices] when '1' then 'true' else 'false' end) As [isApplicabletoAllServices],
(select count([${dbxschemaname}].[groupentitlement].[Group_id]) from [${dbxschemaname}].[groupentitlement] where
 ([${dbxschemaname}].[groupentitlement].[Group_id] = [${dbxschemaname}].[membergroup].[id])) AS [Entitlements_Count],
 (select count(distinct([${dbxschemaname}].[customergroup].[Customer_id])) from [${dbxschemaname}].[customergroup] 
 where ([${dbxschemaname}].[customergroup].[Group_id] = [membergroup].[id])) AS [Customers_Count],
 (case [${dbxschemaname}].[membergroup].[Status_id] when 'SID_ACTIVE' then 'Active' else 'Inactive' end) AS [Status] 
 from ([${dbxschemaname}].[membergroup] join [membergrouptype] on(([${dbxschemaname}].[membergroup].[Type_id] = [membergrouptype].[id])));

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
	
SET @servicedefinitionIds = (SELECT STRING_AGG( servicedefinitionId,',') FROM (SELECT DISTINCT servicedefinitionId
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

DROP VIEW if exists [${dbxschemaname}].[feature_view];
GO

CREATE VIEW [${dbxschemaname}].[feature_view] (
   [Code], 
   [Type], 
   [Type_Id], 
   [Name], 
   [Status], 
   [Status_Id])
AS 
   SELECT 
      feature.id AS Code, 
      STRING_AGG(CAST( membergrouptype.description as nvarchar(max)), ',') AS Type, 
      STRING_AGG(CAST( membergrouptype.id as nvarchar(max)), ',')  AS Type_Id, 
      min(feature.name) AS Name, 
      min(status.Description) AS Status, 
      min(status.id) AS Status_Id
   FROM ((([${dbxschemaname}].feature 
      LEFT JOIN [${dbxschemaname}].featureroletype  AS fr 
      ON ((fr.Feature_id = feature.id))) 
      LEFT JOIN [${dbxschemaname}].membergrouptype 
      ON ((membergrouptype.id = fr.RoleType_id))) 
      LEFT JOIN [${dbxschemaname}].status 
      ON ((feature.Status_id = status.id)))
   GROUP BY feature.id
GO

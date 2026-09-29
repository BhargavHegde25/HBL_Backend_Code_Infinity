
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
                (alertsubtype.AlertTypeId = dbxalerttype.id) AND (alertsubtype.IsGlobal = '0') ) AS userAlerts_count,
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc]  
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

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') = 0 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END
	  
      SET @approvalRequestIds = 
         (  
         SELECT String_agg(CAST(requestapprovalmatrix.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId))
			OR (approvalrule.numberOfApprovals != -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
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

	DECLARE @requestIds nvarchar(max)
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
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM [${dbxschemaname}].requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						CASE approvalrule.numberOfApprovals
							WHEN -1 THEN (SELECT COUNT(*) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
							WHEN NULL THEN 0
							WHEN '''' THEN 0
							ELSE approvalrule.numberOfApprovals
						END		 
					) as requiredApprovals
				FROM
				 [${dbxschemaname}].bbrequest
				LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN [${dbxschemaname}].approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ' + @query ;
	
    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
    exec(@select_statement)
	
	End
    MAINLABEL$leave: 
	
GO

EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].application', 'stateManagementAvailable';
GO
ALTER TABLE [${dbxschemaname}].[application] alter column  [stateManagementAvailable] [BIT] NOT NULL ;
GO
Alter table [${dbxschemaname}].[application] ADD CONSTRAINT stateManagementAvailable_default  DEFAULT 0 FOR [stateManagementAvailable];
GO


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
set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.approvalruleId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit) VALUES (',@matrixComma,');');
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_nogroup_user_details_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_nogroup_user_details_proc]
 @_customerId nvarchar(max), 
 @_coreCustomerId nvarchar(max)
AS

BEGIN
	SELECT 
		[customer].[id] AS userId,
		[customer].[isCombinedUser] AS isCombinedUser,
		[customer].[UserName] AS userName,
		[customer].[FirstName] AS firstName,
		[customer].[LastName] AS lastName,
		[membergroup].[Name] AS role
	FROM 
		[${dbxschemaname}].[customer] 
	LEFT JOIN [${dbxschemaname}].[customergroup] ON ([customergroup].[Customer_id] = [customer].[id])
	LEFT JOIN [${dbxschemaname}].[membergroup] ON ([membergroup].[id] = [customergroup].[Group_id])
		WHERE 
			 [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].[id],@_customerId)>0 and [customergroup].[coreCustomerId] = @_coreCustomerId ;
			
END
GO

EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].manageapprovalmatrix', 'isDisabled';
GO
ALTER TABLE [${dbxschemaname}].[manageapprovalmatrix] alter column  [isDisabled] [BIT] NOT NULL ;
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
	(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].pendingGroupList,']',''),'[',''),'"',''),' ', ''),',')
		FROM [${dbxschemaname}].[signatorygrouprequestmatrix] WHERE 
		[signatorygrouprequestmatrix].requestId = @_requestId AND [signatorygrouprequestmatrix].isApproved = 'false')) > 0;
		
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_create_proc];
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
	
	set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;
	getValues: WHILE 1=1 BEGIN
			set @index1 = @index1 + 1;
			IF @index1 = @length + 1 BEGIN 
				BREAK;
			end
			else begin
				set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				SET @matrixComma = replace(@matrixComma, '"', '''');
				set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,isGroupMatrix) VALUES (',@matrixComma,');');
				execute(@query);
				
				SET @id = @@IDENTITY
                
                IF @_isGroupMatrix = 0 BEGIN
					set @customerIds = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, ',', @index1), ',', -1 );
					IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0) BEGIN
						set @customerIdsComma = REPLACE(@customerIds, ';', ',');
							set @length2 = LEN(@customerIdsComma) - LEN(REPLACE(@customerIdsComma, ',', '')) + 1;
							set @index2 = 0;
							getCustomerIds: WHILE 1=1 BEGIN
								set @index2 = @index2 + 1;
								IF @index2 = @length2 + 1 BEGIN 
									BREAK;
								end
								else begin
									set @customerId = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1 );
									INSERT INTO [${dbxschemaname}].customerapprovalmatrixtemplate(customerId,approvalMatrixId) values (@customerId,@id);							
									CONTINUE
								END 
							END;
						END 
					CONTINUE
				END
				ELSE IF @_isGroupMatrix = 1 BEGIN
					set @sigValues = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, '#', @index1), '#', -1 );
					SET @id = @@IDENTITY
					SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1 );
					SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1 );
					INSERT INTO [${dbxschemaname}].signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
               END  
			END 
	END 
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc]
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc]  
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(max),
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

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''

      SET @customerGroupIds = ( SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)),',') FROM [${dbxschemaname}].customersignatorygroup WHERE [${dbxschemaname}].FIND_IN_SET(customersignatorygroup.customerId, @combinedIds) <> 0);
        IF @customerGroupIds IS NULL
         SET @customerGroupIds = ''
      
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
         WHERE [${dbxschemaname}].requestapprovalmatrix.isGroupRule = 0 
         AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId))
            OR (approvalrule.numberOfApprovals != -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
         )

        IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END
      
      SET @groupIds = @customerGroupIds;
        do_this: WHILE 1=1 BEGIN
            SET @strLen = LEN(@groupIds);
             SET @reqIds = ( SELECT Distinct (String_agg( CAST([signatorygrouprequestmatrix].[requestId] as nvarchar(max)) , ',')) FROM [${dbxschemaname}].signatorygrouprequestmatrix 
                    WHERE NOT [${dbxschemaname}].FIND_IN_SET(signatorygrouprequestmatrix.requestId, @approvalRequestIds) > 0 AND signatorygrouprequestmatrix.isApproved = '0' AND [${dbxschemaname}].FIND_IN_SET( [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].[pendingGroupList],'[',''),']',''),' ','')) > 0 
                    AND [signatorygrouprequestmatrix].[requestId] NOT IN (@alreadyApprovedIds) );
                    
                IF @reqIds IS NULL
                  BEGIN
                    SET @reqIds = ''
                  END
                  
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

    DECLARE @companyRequestIds nvarchar(max)
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
             bbrequest.isGroupMatrix,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
            END) as amICreator,         
         (CASE 
            WHEN 
	            ([${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0) AND
	            ((select TOP 1 id from [${dbxschemaname}].customeraction where bbrequest.accountId = customeraction.Account_id 
	            AND customeraction.Action_id = (select featureaction.approveFeatureAction from [${dbxschemaname}].featureaction where featureaction.id = bbrequest.featureActionId) 
	            AND [${dbxschemaname}].FIND_IN_SET(CAST(customeraction.Customer_id AS nvarchar(max)),'''+ @combinedIds +''') > 0 
	            AND customeraction.contractId = substring(bbrequest.companyId, 1,CHARINDEX(''_'', bbrequest.companyId)-1) 
	            AND customeraction.coreCustomerId = substring(bbrequest.companyId, CHARINDEX(''_'', bbrequest.companyId)+1, len(bbrequest.companyId)) 
	            AND customeraction.isAllowed = 1 AND customeraction.softdeleteflag =0) IS NOT NULL)
            THEN ''true''
            ELSE ''false''
         END)
          as amIApprover,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from [${dbxschemaname}].bbactedrequest where bbactedrequest.action = ''Approved'' AND  bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0) 
                            as receivedApprovals,
                    CASE bbrequest.isGroupMatrix
                    WHEN 0 then [${dbxschemaname}].LEASTINT(
                                (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM [${dbxschemaname}].requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
                                , 
                                CASE approvalrule.numberOfApprovals
                                    WHEN -1 THEN (SELECT COUNT(*) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
                                    WHEN NULL THEN 0
                                    WHEN '''' THEN 0
                                    ELSE approvalrule.numberOfApprovals
                                END         
                            ) 
                    ELSE NULL
                    END as requiredApprovals
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalgroups_for_pendingtxn_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalgroups_for_pendingtxn_proc]
AS
BEGIN

	SELECT 
		[signatorygroup].[signatoryGroupId]
	FROM 
		[${dbxschemaname}].[signatorygroup]
	WHERE 
			[${dbxschemaname}].FIND_IN_SET([signatorygroup].[signatoryGroupId],

(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([s].[pendingGroupList],']',''),'[',''),'"',''),',')
FROM [${dbxschemaname}].[signatorygrouprequestmatrix] AS s
inner join [${dbxschemaname}].[bbrequest] AS b on (s.requestId = b.requestId)
WHERE [s].[isApproved] = 'false' and [b].[status] = 'Pending' )) > 0;

END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatorygroup_details_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroup_details_proc](
@_signatoryGroupId AS nvarchar(max)
) AS
BEGIN
select [signatorygroup].[signatoryGroupId] as [signatoryGroupId],
[signatorygroup].[signatoryGroupName] as [signatoryGroupName],
[signatorygroup].[signatoryGroupDescription] as [signatoryGroupDescription],
[signatorygroup].[coreCustomerId] as [coreCustomerId],
[contractcorecustomers].[coreCustomerName] as [coreCustomerName],
[signatorygroup].[createdts] as [createdts],
(select ([customer].[FirstName] + ' '+ ISNULL([customer].[LastName], N'')) from [${dbxschemaname}].[customer] AS cust where [cust].[id]=[signatorygroup].[createdby]) as [createdby],
[signatorygroup].[lastmodifiedts] as [lastmodifiedts],
[customersignatorygroup].[customerSignatoryGroupId] as [customerSignatoryGroupId],
[customersignatorygroup].[customerId] as [customerId],
[customer].[UserName] AS [userName],
[customer].[FirstName] + ' '+ ISNULL([customer].[MiddleName], N'') + ' '+ ISNULL([customer].[LastName], N'') AS [fullName],
[membergroup].[Name] as [customerRole],
[customersignatorygroup].[createdts] as [signatoryaddedts]
from [${dbxschemaname}].[signatorygroup]
left join [${dbxschemaname}].[customersignatorygroup] on [signatorygroup].[signatoryGroupId] = [customersignatorygroup].[signatoryGroupId]
left join [${dbxschemaname}].[contractcorecustomers] on [contractcorecustomers].[coreCustomerId] = [signatorygroup].[coreCustomerId]
left join [${dbxschemaname}].[customer] on [customer].[id] = [customersignatorygroup].[customerId]
left join [${dbxschemaname}].[customergroup] on [customergroup].[Customer_id] = [customersignatorygroup].[customerId] and [customergroup].[coreCustomerId] =[signatorygroup].[coreCustomerId]
left join [${dbxschemaname}].[membergroup] on [membergroup].[id] = [customergroup].[Group_id]
where [signatorygroup].[signatoryGroupId] = @_signatoryGroupId;
END
GO

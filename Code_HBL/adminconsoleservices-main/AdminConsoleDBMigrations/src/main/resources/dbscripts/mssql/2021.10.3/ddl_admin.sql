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

EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].[manageapprovalmatrix]', 'isDisabled';
GO
ALTER TABLE [${dbxschemaname}].[manageapprovalmatrix] ALTER COLUMN isDisabled bit not null; 
GO
ALTER TABLE [${dbxschemaname}].[requestmessage] add [isPriorityMessage] varchar(20) default '0';
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[customerrequests_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customerrequests_view] (
   [id], 
   [softdeleteflag], 
   [priority], 
   [requestCreatedDate], 
   [recentMsgDate], 
   [requestcategory_id], 
   [customer_id], 
   [username], 
   [status_id], 
   [statusIdentifier], 
   [requestsubject], 
   [accountid], 
   [isPriorityMessage],
   [priorityCount],
   [assignTo], 
   [totalmsgs], 
   [readmsgs], 
   [unreadmsgs],
   [firstMessage],
   [msgids],
   [totalAttachments])
AS 
   SELECT TOP 100 PERCENT
      ([${dbxschemaname}].customerrequest.id ) AS id, 
       [${dbxschemaname}].customerrequest.softdeleteflag AS softdeleteflag, 
      ([${dbxschemaname}].customerrequest.Priority) AS [priority], 
      ([${dbxschemaname}].customerrequest.createdts) AS requestCreatedDate, 
      max([${dbxschemaname}].requestmessage.createdts) AS recentMsgDate, 
      ([${dbxschemaname}].requestcategory.Name) AS requestcategory_id, 
      ([${dbxschemaname}].customerrequest.Customer_id) AS customer_id, 
      ([${dbxschemaname}].customer.UserName) AS username, 
      (SELECT TOP (1) [status].[Description]
            FROM [${dbxschemaname}].[status]
            WHERE ([status].id = min([${dbxschemaname}].customerrequest.Status_id))
         ) AS status_id, 
      ([${dbxschemaname}].customerrequest.id) AS statusIdentifier, 
      ([${dbxschemaname}].customerrequest.RequestSubject) AS requestsubject, 
      ([${dbxschemaname}].customerrequest.Accountid) AS accountid, 
	  ([${dbxschemaname}].requestmessage.isPriorityMessage) AS isPriorityMessage, 
	  count((
		CASE
            WHEN ([${dbxschemaname}].requestmessage.isPriorityMessage = '1') THEN 0
        END)) AS priorityCount,
      ([${dbxschemaname}].systemuser.FirstName) + (N' ') + min([${dbxschemaname}].systemuser.LastName) AS assignTo, 
      count([${dbxschemaname}].requestmessage.id) AS totalmsgs, 
      count((
         CASE 
            WHEN ([${dbxschemaname}].requestmessage.IsRead = 'true') THEN 1
         END)) AS readmsgs, 
      count((
         CASE 
            WHEN ([${dbxschemaname}].requestmessage.IsRead = 'false') THEN 1
         END)) AS unreadmsgs,
		 [${dbxschemaname}].substring_index(
   (String_agg(
      CAST(requestmessage.MessageDescription as nvarchar(max)),'||') WITHIN GROUP (order by requestmessage.createdts ASC, requestmessage.id ASC) ),'||', 1) AS firstMessage, 
  String_agg(CAST(requestmessage.id as nvarchar(max)),',') AS msgids, 
  count(messageattachment.id) AS totalAttachments  

  FROM 
  ((((( [${dbxschemaname}].customerrequest 
  left join [${dbxschemaname}].requestmessage on((
  customerrequest.id = requestmessage.CustomerRequest_id))) 
  left join [${dbxschemaname}].requestcategory on((
  requestcategory.id = customerrequest.RequestCategory_id))) 
  left join [${dbxschemaname}].customer on((
  customer.id = customerrequest.Customer_id))) 
  left join [${dbxschemaname}].messageattachment on((
  messageattachment.RequestMessage_id = requestmessage.id))) 
  left join [${dbxschemaname}].systemuser on((
  systemuser.id = customerrequest.AssignedTo))) 
group by 
  customerrequest.id ,customerrequest.softdeleteflag,customerrequest.[Priority],[${dbxschemaname}].customerrequest.createdts,requestcategory.[Name],customerrequest.Customer_id,
  customer.UserName,customerrequest.RequestSubject,customerrequest.Accountid,systemuser.FirstName,[${dbxschemaname}].requestmessage.isPriorityMessage

order by 
  customerrequest.createdts DESC,
  customerrequest.id;
GO
/** commenting since it is added in 2021.10.3
alter table [${dbxschemaname}].[requestmessage] add [isPriorityMessage] varchar(20) default '0';
**/

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_request_message_search_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[customer_request_message_search_proc]  
   @_customerID nvarchar(50),
   @_customerName nvarchar(50),
   @_customerFirstName nvarchar(50),
   @_customerMiddleName nvarchar(50),
   @_customerLastName nvarchar(50),
   @_customerUsername nvarchar(50),
   @_messageRepliedBy nvarchar(50),
   @_requestSubject nvarchar(50),
   @_requestAssignedTo nvarchar(50),
   @_requestCategory nvarchar(50),
   @_requestID nvarchar(50),
   @_requestStatusID nvarchar(50),
   @_dateInitialPoint nvarchar(50),
   @_dateFinalPoint nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT customerrequest.id AS customerrequest_id,customerrequest.RequestCategory_id AS customerrequest_RequestCategory_id,customerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer,requestcategory.Name AS requestcategory_Name,customerrequest.Customer_id AS customerrequest_Customer_id,customer.FirstName AS customer_FirstName,customer.MiddleName AS customer_MiddleName,(customer.FirstName+customer.LastName) AS customer_Fullname,(systemuser.FirstName+systemuser.LastName) AS customerrequest_AssignedTo_Name,customer.LastName AS customer_LastName,customer.UserName AS customer_Username,customer.Salutation AS customer_Salutation,customer.Gender AS customer_Gender,customer.DateOfBirth AS customer_DateOfBirth,customer.Status_id AS customer_Status_id,customer.Ssn AS customer_Ssn,customer.MaritalStatus_id AS customer_MaritalStatus_id, customer.SpouseName AS customer_SpouseName,customer.EmployementStatus_id AS customer_EmployementStatus_id,customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb,customer.IsStaffMember AS customer_IsStaffMember,customer.Location_id AS customer_Location_id,customer.PreferredContactMethod AS customer_PreferredContactMethod,customer.PreferredContactTime AS customer_PreferredContactTime,customerrequest.Priority AS customerrequest_Priority,customerrequest.Status_id AS customerrequest_Status_id,customerrequest.AssignedTo AS customerrequest_AssignedTo,customerrequest.RequestSubject AS customerrequest_RequestSubject,customerrequest.Accountid AS customerrequest_Accountid,customerrequest.createdby AS customerrequest_createdby,customerrequest.modifiedby AS customerrequest_modifiedby,customerrequest.createdts AS customerrequest_createdts,customerrequest.lastmodifiedts AS customerrequest_lastmodifiedts,customerrequest.synctimestamp AS customerrequest_synctimestamp,customerrequest.softdeleteflag AS customerrequest_softdeleteflag,requestmessage.id AS requestmessage_id,requestmessage.isPriorityMessage AS requestmessage_isPriorityMessage, requestmessage.RepliedBy AS requestmessage_RepliedBy,requestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name,requestmessage.MessageDescription AS requestmessage_MessageDescription,      requestmessage.ReplySequence AS requestmessage_ReplySequence,requestmessage.IsRead AS requestmessage_IsRead,requestmessage.createdby AS requestmessage_createdby,requestmessage.modifiedby AS requestmessage_modifiedby, requestmessage.createdts AS requestmessage_createdts,requestmessage.lastmodifiedts AS requestmessage_lastmodifiedts,    requestmessage.synctimestamp AS requestmessage_synctimestamp,requestmessage.softdeleteflag AS requestmessage_softdeleteflag,     messageattachment.id AS messageattachment_id,messageattachment.AttachmentType_id AS messageattachment_AttachmentType_id,     messageattachment.Media_id AS messageattachment_Media_id,messageattachment.createdby AS messageattachment_createdby,     messageattachment.modifiedby AS messageattachment_modifiedby,messageattachment.createdts AS messageattachment_createdts,     messageattachment.lastmodifiedts AS messageattachment_lastmodifiedts,messageattachment.softdeleteflag AS messageattachment_softdeleteflag, media.id AS media_id,media.Name AS media_Name,media.Size AS media_Size,media.Type AS media_Type,  media.Description AS media_Description,media.Url AS media_Url,media.createdby AS media_createdby,media.modifiedby AS media_modifiedby,media.lastmodifiedts AS media_lastmodifiedts,media.synctimestamp AS media_synctimestamp,media.softdeleteflag AS media_softdeleteflag FROM ([${dbxschemaname}].customerrequest JOIN [${dbxschemaname}].requestmessage ON (customerrequest.id = requestmessage.CustomerRequest_id) JOIN [${dbxschemaname}].customer ON (customerrequest.Customer_id = customer.id) JOIN [${dbxschemaname}].requestcategory ON (customerrequest.RequestCategory_id = requestcategory.id) LEFT JOIN [${dbxschemaname}].systemuser ON (customerrequest.AssignedTo = systemuser.id)  LEFT JOIN [${dbxschemaname}].messageattachment ON (requestmessage.id = messageattachment.RequestMessage_id)  LEFT JOIN [${dbxschemaname}].media ON (messageattachment.Media_id = media.id)) WHERE 1=1 '


      IF @_customerID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_customerID), '''')))

      IF @_customerFirstName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.FirstName = ') + ((QUOTENAME((@_customerFirstName), '''')))

      IF @_customerMiddleName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.MiddleName = ') + ((QUOTENAME((@_customerMiddleName), '''')))


      IF @_customerLastName <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ((QUOTENAME((@_customerLastName), '''')))
        

      IF @_customerUsername <> ''

         SET @queryStatement = (@queryStatement) + (N' and customer.LastName = ') + ''''+(@_customerUsername)+''''
   

      IF @_messageRepliedBy <> ''
        
         SET @queryStatement = (@queryStatement) + (N' and requestmessage.RepliedBy = ') + ''''+(@_messageRepliedBy)+''''
         

      IF @_requestSubject <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.RequestSubject = ') + ''''+(@_requestSubject)+''''


      IF @_requestAssignedTo <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.AssignedTo = ') + ''''+(@_requestAssignedTo)+''''


      IF @_requestCategory <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.RequestCategory_id = ') + ''''+(@_requestCategory)+''''

      IF @_requestID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.id = ') + ((QUOTENAME((@_requestID), '''')))

      IF @_requestStatusID <> ''

         SET @queryStatement = (@queryStatement) + (N' and customerrequest.Status_id = ') + ((QUOTENAME((@_requestStatusID), '''')))

      IF @_customerName <> ''

         SET @queryStatement = (@queryStatement) + (N' and  (customer.FirstName+customer.LastName).Status_id = ') + ''''+(@_customerName)+''''

      IF @_dateInitialPoint <> ''
         IF [${dbxschemaname}].FIND_IN_SET(N'=', @_dateInitialPoint) <> 0
 
            SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts = ') + ((QUOTENAME((@_dateInitialPoint), '''')))
   
         ELSE 
            IF [${dbxschemaname}].FIND_IN_SET(N'>', @_dateInitialPoint) <> 0
 
               SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), '''')))

            ELSE 
               IF [${dbxschemaname}].FIND_IN_SET(N'<', @_dateInitialPoint) <> 0
 
                  SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts < ') + ((QUOTENAME((@_dateInitialPoint), '''')))

               ELSE 
                  BEGIN
                     IF @_dateFinalPoint <> ''

                        SET @queryStatement = (@queryStatement) + (N' and requestmessage.createdts > ') + ((QUOTENAME((@_dateInitialPoint), ''''))) + (N' and requestmessage.createdts < ') + ((QUOTENAME((@_dateFinalPoint), '''')))

                  END


      SET @queryStatement = (@queryStatement) + (N' ORDER BY requestmessage.ReplySequence DESC')
       EXEC(@queryStatement)

   END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[customer_request_detailed_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[customer_request_detailed_view] (
   [customerrequest_id], 
   [customerrequest_RequestCategory_id], 
   [customerrequest_lastupdatedbycustomer], 
   [requestcategory_Name], 
   [customerrequest_Customer_id], 
   [customer_FirstName], 
   [customer_MiddleName], 
   [customer_Fullname], 
   [customerrequest_AssignedTo_Name], 
   [customer_LastName], 
   [customer_Username], 
   [customer_Salutation], 
   [customer_Gender], 
   [customer_DateOfBirth], 
   [customer_Status_id], 
   [customer_Ssn], 
   [customer_MaritalStatus_id], 
   [customer_SpouseName], 
   [customer_EmployementStatus_id], 
   [customer_IsEnrolledForOlb], 
   [customer_IsStaffMember], 
   [customer_Location_id], 
   [customer_PreferredContactMethod], 
   [customer_PreferredContactTime], 
   [customerrequest_Priority], 
   [customerrequest_Status_id], 
   [customerrequest_AssignedTo], 
   [customerrequest_RequestSubject], 
   [customerrequest_Accountid], 
   [customerrequest_createdby], 
   [customerrequest_modifiedby], 
   [customerrequest_createdts], 
   [customerrequest_lastmodifiedts], 
   [customerrequest_synctimestamp], 
   [customerrequest_softdeleteflag], 
   [requestmessage_id], 
   [requestmessage_isPriorityMessage],
   [requestmessage_RepliedBy], 
   [requestmessage_RepliedBy_Name], 
   [requestmessage_MessageDescription], 
   [requestmessage_ReplySequence], 
   [requestmessage_IsRead], 
   [requestmessage_createdby], 
   [requestmessage_modifiedby], 
   [requestmessage_createdts], 
   [requestmessage_lastmodifiedts], 
   [requestmessage_synctimestamp], 
   [requestmessage_softdeleteflag], 
   [messageattachment_id], 
   [messageattachment_AttachmentType_id], 
   [messageattachment_Media_id], 
   [messageattachment_createdby], 
   [messageattachment_modifiedby], 
   [messageattachment_createdts], 
   [messageattachment_lastmodifiedts], 
   [messageattachment_softdeleteflag], 
   [media_id], 
   [media_Name], 
   [media_Size], 
   [media_Type], 
   [media_Description], 
   [media_Url], 
   [media_createdby], 
   [media_modifiedby], 
   [media_lastmodifiedts], 
   [media_synctimestamp], 
   [media_softdeleteflag])
AS 
   SELECT 
      customerrequest.id AS customerrequest_id, 
      customerrequest.RequestCategory_id AS customerrequest_RequestCategory_id, 
      customerrequest.lastupdatedbycustomer AS customerrequest_lastupdatedbycustomer, 
      requestcategory.Name AS requestcategory_Name, 
      customerrequest.Customer_id AS customerrequest_Customer_id, 
      customer.FirstName AS customer_FirstName, 
      customer.MiddleName AS customer_MiddleName, 
      customer.FirstName + N' ' + customer.LastName AS customer_Fullname, 
      systemuser.FirstName + N' ' + systemuser.LastName AS customerrequest_AssignedTo_Name, 
      customer.LastName AS customer_LastName, 
      customer.UserName AS customer_Username, 
      customer.Salutation AS customer_Salutation, 
      customer.Gender AS customer_Gender, 
      customer.DateOfBirth AS customer_DateOfBirth, 
      customer.Status_id AS customer_Status_id, 
      NULL AS customer_Ssn, 
      customer.MaritalStatus_id AS customer_MaritalStatus_id, 
      customer.SpouseName AS customer_SpouseName, 
      customer.EmployementStatus_id AS customer_EmployementStatus_id, 
      customer.IsEnrolledForOlb AS customer_IsEnrolledForOlb, 
      customer.IsStaffMember AS customer_IsStaffMember, 
      customer.Location_id AS customer_Location_id, 
      customer.PreferredContactMethod AS customer_PreferredContactMethod, 
      customer.PreferredContactTime AS customer_PreferredContactTime, 
      customerrequest.Priority AS customerrequest_Priority, 
      customerrequest.Status_id AS customerrequest_Status_id, 
      customerrequest.AssignedTo AS customerrequest_AssignedTo, 
      customerrequest.RequestSubject AS customerrequest_RequestSubject, 
      customerrequest.Accountid AS customerrequest_Accountid, 
      customerrequest.createdby AS customerrequest_createdby, 
      customerrequest.modifiedby AS customerrequest_modifiedby, 
      customerrequest.createdts AS customerrequest_createdts, 
      customerrequest.lastmodifiedts AS customerrequest_lastmodifiedts, 
      customerrequest.synctimestamp AS customerrequest_synctimestamp, 
      customerrequest.softdeleteflag AS customerrequest_softdeleteflag, 
      requestmessage.id AS requestmessage_id, 
	  requestmessage.isPriorityMessage AS requestmessage_isPriorityMessage, 
      requestmessage.RepliedBy AS requestmessage_RepliedBy, 
      requestmessage.RepliedBy_Name AS requestmessage_RepliedBy_Name, 
      requestmessage.MessageDescription AS requestmessage_MessageDescription, 
      requestmessage.ReplySequence AS requestmessage_ReplySequence, 
      requestmessage.IsRead AS requestmessage_IsRead, 
      requestmessage.createdby AS requestmessage_createdby, 
      requestmessage.modifiedby AS requestmessage_modifiedby, 
      requestmessage.createdts AS requestmessage_createdts, 
      requestmessage.lastmodifiedts AS requestmessage_lastmodifiedts, 
      requestmessage.synctimestamp AS requestmessage_synctimestamp, 
      requestmessage.softdeleteflag AS requestmessage_softdeleteflag, 
      messageattachment.id AS messageattachment_id, 
      messageattachment.AttachmentType_id AS messageattachment_AttachmentType_id, 
      messageattachment.Media_id AS messageattachment_Media_id, 
      messageattachment.createdby AS messageattachment_createdby, 
      messageattachment.modifiedby AS messageattachment_modifiedby, 
      messageattachment.createdts AS messageattachment_createdts, 
      messageattachment.lastmodifiedts AS messageattachment_lastmodifiedts, 
      messageattachment.softdeleteflag AS messageattachment_softdeleteflag, 
      media.id AS media_id, 
      media.Name AS media_Name, 
      media.Size AS media_Size, 
      media.Type AS media_Type, 
      media.Description AS media_Description, 
      media.Url AS media_Url, 
      media.createdby AS media_createdby, 
      media.modifiedby AS media_modifiedby, 
      media.lastmodifiedts AS media_lastmodifiedts, 
      media.synctimestamp AS media_synctimestamp, 
      media.softdeleteflag AS media_softdeleteflag
   FROM (((((([${dbxschemaname}].customerrequest 
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customerrequest.Customer_id = customer.id))) 
      INNER JOIN [${dbxschemaname}].requestcategory 
      ON ((customerrequest.RequestCategory_id = requestcategory.id))) 
      INNER JOIN [${dbxschemaname}].requestmessage 
      ON ((customerrequest.id = requestmessage.CustomerRequest_id))) 
      LEFT JOIN [${dbxschemaname}].systemuser 
      ON ((customerrequest.AssignedTo = systemuser.id))) 
      LEFT JOIN [${dbxschemaname}].messageattachment 
      ON ((requestmessage.id = messageattachment.RequestMessage_id))) 
      LEFT JOIN [${dbxschemaname}].media 
      ON ((messageattachment.Media_id = media.id)))
GO

DROP procedure IF EXISTS [${dbxschemaname}].[customer_unread_message_count_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_unread_message_count_proc]  
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT 
        COUNT(requestmessage.id) AS messageCount
    FROM
        ([${dbxschemaname}].customerrequest
        JOIN [${dbxschemaname}].requestmessage ON (([requestmessage].[CustomerRequest_id] = [customerrequest].[id])))
    WHERE
        ([requestmessage].[IsRead] = 'FALSE' and [customerrequest].[softdeleteflag]= 0 and [customerrequest].[Customer_id]= @_customerId 
        and [customerrequest].[Status_id] <> 'SID_DELETED');
	SELECT 
        COUNT([requestmessage].[id]) AS priorityMessageCount
    FROM
        ([${dbxschemaname}].customerrequest
        JOIN [${dbxschemaname}].requestmessage ON (([requestmessage].[CustomerRequest_id] = [customerrequest].[id])))
    WHERE
        ([requestmessage].[IsRead] = 'FALSE' and [customerrequest].[softdeleteflag]= 0 and [customerrequest].[Customer_id]= @_customerId 
        and [customerrequest].[Status_id] <> 'SID_DELETED' and requestmessage.isPriorityMessage='1' );

   END
GO
GO
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [transactionCurrency] VARCHAR(50) NULL;
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
EXEC [${dbxschemaname}].sp_delete_default_constranit '[${dbxschemaname}].[manageapprovalmatrix]', 'isDisabled';
GO
ALTER TABLE [${dbxschemaname}].[manageapprovalmatrix] ALTER COLUMN isDisabled bit not null; 
GO
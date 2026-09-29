ALTER TABLE [${dbxschemaname}].[bbrequest] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bbactedrequest] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';


ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest]
ADD [companyLegalUnit] [varchar](50) NOT NULL;
GO

ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] ADD  DEFAULT ('ALL') FOR [companyLegalUnit]
GO
 
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage]
ADD [frominternaluser] [tinyint] NOT NULL,
	[isPriorityMessage] [varchar](20) NULL,
	[companyLegalUnit] [varchar](50) NOT NULL;
	
GO
ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT ('0') FOR [frominternaluser]
GO

ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT ('0') FOR [isPriorityMessage]
GO

ALTER TABLE [${dbxschemaname}].[archivedrequestmessage] ADD  DEFAULT ('ALL') FOR [companyLegalUnit]
GO
 
ALTER TABLE [${dbxschemaname}].[archivedmessageattachment]
ADD [companyLegalUnit] [varchar](50) NOT NULL;
GO

ALTER TABLE [${dbxschemaname}].[archivedmessageattachment] ADD  DEFAULT ('ALL') FOR [companyLegalUnit]
GO
 
ALTER TABLE [${dbxschemaname}].[archivedmedia]
ADD [companyLegalUnit] [varchar](50) NOT NULL;

GO

ALTER TABLE [${dbxschemaname}].[archivedmedia] ADD  DEFAULT ('ALL') FOR [companyLegalUnit]
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_request_archive_proc];
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_request_archive_proc]
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @customer_request_cursor_done int = 0;

      DECLARE
         @customer_requestmessage_cursor_done int = 0;

      DECLARE
         @customer_requestmessageattachment_cursor_done int = 0

      DECLARE
         @curr_request_id nvarchar(50) = N''

      DECLARE
         @curr_requestmessage_id nvarchar(50) = N''

      DECLARE
         @curr_media_id nvarchar(50) = N''

      DECLARE
         @curr_messageattachment_id nvarchar(50) = N'0'

      DECLARE
         @media_count int = 0


 DECLARE
          customer_request_cursor CURSOR LOCAL FORWARD_ONLY FOR 


            SELECT id
            FROM [${dbxschemaname}].[customerrequest]
            WHERE Status_id ='SID_RESOLVED' AND datediff(second, lastmodifiedts, getdate()) >= 15552000;

      OPEN customer_request_cursor


      WHILE (1 = 1)
      
         BEGIN/*2*/
SET @customer_request_cursor_done = 0;

            FETCH customer_request_cursor
                INTO @curr_request_id

            if @@FETCH_STATUS = 0

                     

            
               BEGIN /*3*/
SET @customer_request_cursor_done = 1;
                     INSERT  INTO [${dbxschemaname}].[archivedcustomerrequest]
                        SELECT 
                           *
                        FROM [${dbxschemaname}].[customerrequest]
                        WHERE id = @curr_request_id

                  UPDATE [${dbxschemaname}].[archivedcustomerrequest]
                     SET 
                        Status_id = 'SID_ARCHIVED'
                  WHERE id = @curr_request_id
                  

END; /*3*/

                  SELECT @curr_request_id
				
				 
				 /* REQUEST MEESSAGE */
				 
				   DECLARE
                         customer_requestmessage_cursor CURSOR LOCAL FORWARD_ONLY FOR 
                           SELECT id
                           FROM [${dbxschemaname}].[requestmessage]
                           WHERE CustomerRequest_id = @curr_request_id

                     OPEN customer_requestmessage_cursor

                     WHILE (1 = 1)
                     
                        BEGIN /*4*/
SET  @customer_requestmessage_cursor_done=0;
                           FETCH customer_requestmessage_cursor
                               INTO @curr_requestmessage_id

                           IF @@FETCH_STATUS =0
 
                             

                           
                              BEGIN /*5*/
							  SET  @customer_requestmessage_cursor_done=1;
								    INSERT  INTO [${dbxschemaname}].[archivedrequestmessage]
                                       SELECT 
                                          *
                                       FROM [${dbxschemaname}].[requestmessage]
                                       WHERE id = @curr_requestmessage_id
				 
				 END;/*5*/
				 
				 
				  /*MESSAGE ATTACHMENT*/
				 
				  DECLARE
                                        customer_messageattachment_cursor CURSOR LOCAL FORWARD_ONLY FOR 
                                          SELECT id
                                          FROM [${dbxschemaname}].[messageattachment]
                                          WHERE RequestMessage_id = @curr_requestmessage_id

                                    OPEN customer_messageattachment_cursor
				 
				 WHILE (1 = 1)
                                    
                                       BEGIN /*6*/
SET @customer_requestmessageattachment_cursor_done=0;
                                          FETCH customer_messageattachment_cursor
                     INTO @curr_messageattachment_id


                                          IF @@FETCH_STATUS =0
				  BEGIN /*7*/
				SET @customer_requestmessageattachment_cursor_done=1;
				
				
				
/* media table changes */
SELECT @curr_media_id = Media_id FROM [${dbxschemaname}].[messageattachment]  WHERE id = @curr_messageattachment_id;
                                                SELECT @media_count = count_big(id)
                                                FROM [${dbxschemaname}].[archivedmedia]
                                                WHERE id = @curr_media_id
       

                                                IF @media_count = 0
									BEGIN /* 8*/
                                                      INSERT  INTO [${dbxschemaname}].[archivedmedia]
                                                         SELECT 
                                                            *
                                                         FROM [${dbxschemaname}].[media]
                                                         WHERE id = @curr_media_id
				
				END;/*8*/
				 	 
					 /* STOP MEDIA*/
													   INSERT  INTO [${dbxschemaname}].[archivedmessageattachment]
                                                      SELECT 
                                                         *
                                                      FROM [${dbxschemaname}].[messageattachment]
                                                      WHERE id = @curr_messageattachment_id
                                                



     
                                                DELETE 
                                                FROM [${dbxschemaname}].[messageattachment]
                                                WHERE id = @curr_messageattachment_id
				 
				  SELECT @media_count = count_big(id)
                                                FROM [${dbxschemaname}].[messageattachment]
                                                WHERE Media_id = @curr_media_id
                                               

                                                IF @media_count = 0
                                        
                                                   DELETE 
                                                   FROM [${dbxschemaname}].[media]
                                                   WHERE id = @curr_media_id
				 END;/*7*/
				 IF   @customer_requestmessageattachment_cursor_done =0
                              BREAK;
				 END;/* 6 while message attach*/
				 
				 CLOSE customer_messageattachment_cursor

                     DEALLOCATE customer_messageattachment_cursor 
				 /*STOP MESSSAGE ATTAC*/
				 
				 
				 
				 
				 
				 
				    DELETE 
                                 FROM [${dbxschemaname}].[requestmessage]
                                 WHERE id = @curr_requestmessage_id;
                      

				  IF   @customer_requestmessage_cursor_done =0
                              BREAK;
				 END; /*4  while request message */
				 
				 CLOSE customer_requestmessage_cursor

                     DEALLOCATE customer_requestmessage_cursor 
				 /*STOP REQUEST MESSAGE*/
				
				 
				 
				 
				 
				 
 
			DELETE 
                  FROM [${dbxschemaname}].[customerrequest]
                  WHERE id = @curr_request_id
       
               

            IF   @customer_request_cursor_done =0
                              BREAK;
           

        END; /*2 while customer request loop*/
	   CLOSE customer_request_cursor

      DEALLOCATE customer_request_cursor

END;
GO


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
                   (select companyLegalUnit from [${dbxschemaname}].[bbrequest] where [bbrequest].requestId = @_requestId) AS companyLegalUnit,
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
                   min(bb.companyLegalUnit) AS companyLegalUnit,
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
               min(bb.companyLegalUnit) AS companyLegalUnit,
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

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[log_bbactedrequest_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[log_bbactedrequest_proc] @_input VARCHAR(MAX)
AS
BEGIN 
	DECLARE @requestId NVARCHAR(MAX) = '';
	DECLARE @companyId NVARCHAR(MAX) = '';
	DECLARE @statusId NVARCHAR(MAX) = '';
	DECLARE @comments NVARCHAR(MAX) = '';
	DECLARE @createdby NVARCHAR(MAX) = '';
	DECLARE @actionId NVARCHAR(MAX) = '';
	DECLARE @groupName NVARCHAR(MAX) = '';
	DECLARE @legalEntityId NVARCHAR(MAX) = '';

	SET @requestId = JSON_VALUE(@_input, '$.requestId');
	SET @companyId = JSON_VALUE(@_input, '$.companyId');
	SET @statusId = JSON_VALUE(@_input, '$.status');
	SET @comments = JSON_VALUE(@_input, '$.comments');
	SET @createdby = JSON_VALUE(@_input, '$.createdby');
	SET @actionId = JSON_VALUE(@_input, '$.action');
	SET @groupName = JSON_VALUE(@_input, '$.groupName');

	SELECT @legalEntityId = [companyLegalUnit] FROM [${dbxschemaname}].[bbrequest] WHERE requestId = @requestId;

	INSERT INTO [${dbxschemaname}].[bbactedrequest] ([requestId], [companyId], [status], [comments], [createdby], [action], [groupName], [companyLegalUnit]) 
	VALUES (@requestId, @companyId, @statusId, @comments, @createdby, @actionId, @groupName ,@legalEntityId);

    END
GO

/* SQL Scripts for InfinityWealth - Strategy - Model Constraint */
DROP TABLE IF EXISTS [${dbxschemaname}].inf_wlth_model_constraint;
CREATE TABLE [${dbxschemaname}].[inf_wlth_model_constraint] (
  [portfolioId] varchar(50) NOT NULL,
  [portfolioCode] varchar(50) NOT NULL,
  [customerId] nvarchar(50) NOT NULL ,
  [constraintId] varchar(50) ,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [lastmodifiedts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [synctimestamp] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [softdeleteflag] tinyint NOT NULL DEFAULT '0',
  CONSTRAINT PK_portfolio_id PRIMARY KEY ([portfolioId]),
  CONSTRAINT FK_model_constraint_customer_id FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
)

INSERT INTO [${dbxschemaname}].[channeltext] ([channelID], [LanguageCode], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'CH_EMAIL', N'en-GB', N'Email', N'Kony User', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)

INSERT INTO [${dbxschemaname}].[channeltext] ([channelID], [LanguageCode], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'CH_NOTIFICATION_CENTER', N'en-GB', N'Notification Center', N'Kony User', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)

INSERT INTO [${dbxschemaname}].[channeltext] ([channelID], [LanguageCode], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'CH_PUSH_NOTIFICATION', N'en-GB', N'Push Notification', N'Kony User', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)

INSERT INTO [${dbxschemaname}].[channeltext] ([channelID], [LanguageCode], [Description], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (N'CH_SMS', N'en-GB', N'SMS/Text', N'Kony User', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0)

GO

CREATE INDEX [IX_backendidentifier_BackendId_BackendType] ON [${dbxschemaname}].[${dbxschemaname}].[backendidentifier] ([BackendId], [BackendType]);

CREATE INDEX [IX_customercommunication_Type_id_Customer_id_isPrimary] ON [${dbxschemaname}].[${dbxschemaname}].[customercommunication] ([Type_id], [Customer_id], [isPrimary], [isTypeBusiness]) INCLUDE ([Value]);

CREATE INDEX [IX_contractactionlimit_contractId_action] ON [${dbxschemaname}].[${dbxschemaname}].[contractactionlimit] ([contractId], [ActionId]);

CREATE INDEX [IX_contractcustomers_customerId_autoSyncAccounts] ON [${dbxschemaname}].[${dbxschemaname}].[contractcustomers] ([customerId], [autoSyncAccounts]);

CREATE INDEX [IX_customer_combinedUserId] ON [${dbxschemaname}].[${dbxschemaname}].[customer] ([combinedUserId]);

CREATE INDEX [IX_customeraccounts_Account_id] ON [${dbxschemaname}].[${dbxschemaname}].[customeraccounts] ([Account_id]) INCLUDE ([Customer_id]);

CREATE INDEX [IX_customerlimitgrouplimits_Customer_id_contractId_coreCustomerId] ON [${dbxschemaname}].[${dbxschemaname}].[customerlimitgrouplimits] ([Customer_id], [contractId], [coreCustomerId]);

CREATE INDEX [IX_featureaction_typeid] ON [${dbxschemaname}].[${dbxschemaname}].[featureaction] ([type_id]);

CREATE INDEX [IX_customeraction_cusaccactionid] ON [${dbxschemaname}].[${dbxschemaname}].[customeraction] ([customer_id], [contractid], [action_id], [isAllowed],[LimitType_id]);

CREATE INDEX [IX_contractactionlimit_contractId_actions] ON [${dbxschemaname}].[${dbxschemaname}].[contractactionlimit] ([contractId], [ActionId], [limitTypeId]);
GO

delete from [${dbxschemaname}].[groupactionlimit] where [id]  = '428f5248-7cf8-11ea-bc55-0242ac130003';
delete from [${dbxschemaname}].[groupactionlimit] where [id]  = '1b548792-3d55-11ea-acf5-00090faa0001';
delete from [${dbxschemaname}].[groupactionlimit] where [id]  = '1b550ab0-3d55-11ea-acf5-00090faa0001';
delete from [${dbxschemaname}].[groupactionlimit] where [id]  = '1b564773-3d55-11ea-acf5-00090faa0001';
delete from [${dbxschemaname}].[groupactionlimit] where [id]  = '1b56c8fd-3d55-11ea-acf5-00090faa0001';
delete from [${dbxschemaname}].[groupactionlimit] where [id]  = 'fb8380b1-dad4-11ed-a526-0299a76678fd';
delete from [${dbxschemaname}].[groupactionlimit] where [Group_id]  = 'GROUP_CREATOR' and [Action_id] = 'ADD_USER_ANOTHER_ENTITY';


GO

-- Enable the constraints for feature/featureaction related tables 
ALTER TABLE [${dbxschemaname}].compositeaction CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].featureroletype CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].featuredisplaynamedescription CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].featureaction CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].actionlimit CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].actiondisplaynamedescription CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].featureactionroletype CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].servicedefinitionactionlimit CHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].groupactionlimit CHECK CONSTRAINT ALL;
GO


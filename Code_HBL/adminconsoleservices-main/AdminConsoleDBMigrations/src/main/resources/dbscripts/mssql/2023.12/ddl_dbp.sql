CREATE INDEX customerrequest_Customer_id_IDX ON [${dbxschemaname}].customerrequest (Customer_id);
CREATE INDEX requestmessage_CustomerRequest_id_IDX ON [${dbxschemaname}].requestmessage (CustomerRequest_id);
CREATE INDEX usernotification_user_id_IDX ON [${dbxschemaname}].usernotification (user_id);

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_messages_notifications_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_messages_notifications_proc]
@_customerId nvarchar(50),
@_startdate datetime,
@_enddate datetime,
@_serverAppId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT count_big(requestmessage.id) AS messageCount
      FROM ([${dbxschemaname}].customerrequest INNER JOIN [${dbxschemaname}].requestmessage 
      ON ((requestmessage.CustomerRequest_id = customerrequest.id)))
      WHERE 
      (customerrequest.Customer_id = @_customerId AND
      requestmessage.IsRead = 'FALSE' AND 
      customerrequest.softdeleteflag = 0 AND 
      customerrequest.Status_id <> 'SID_DELETED');

      SELECT count_big(requestmessage.id) AS priorityMessageCount
      FROM ([${dbxschemaname}].customerrequest INNER JOIN [${dbxschemaname}].requestmessage 
      ON ((requestmessage.CustomerRequest_id = customerrequest.id)))
      WHERE 
      (customerrequest.Customer_id = @_customerId AND
      (requestmessage.isPriorityMessage = '1' OR requestmessage.isPriorityMessage = 'TRUE') AND
      requestmessage.IsRead = 'FALSE' AND 
      customerrequest.softdeleteflag = 0 AND 
      customerrequest.Status_id <> 'SID_DELETED');
        
        
      select count_big(*) as notificationCount from  [${dbxschemaname}].usernotification where 
      (usernotification.user_id = @_customerId
      and (usernotification.receiveddate >= @_startdate 
      and usernotification.receiveddate < @_enddate)
      and usernotification.isRead = '0');
      
      
    
  SELECT 
      outagemessage.id AS id, 
      outagemessage.name AS name, 
      outagemessage.startTime AS startTime, 
      outagemessage.endTime AS endTime, 
      outagemessageapp.App_id AS app_id, 
      app.Name AS app_name, 
      outagemessage.Status_id AS Status_id, 
      outagemessage.MessageText AS MessageText, 
      outagemessage.createdby AS createdby, 
      outagemessage.modifiedby AS modifiedby, 
      outagemessage.createdts AS createdts, 
      outagemessage.lastmodifiedts AS lastmodifiedts
   FROM [${dbxschemaname}].outagemessage
   join [${dbxschemaname}].outagemessageapp
   on (outagemessageapp.Outagemessage_id = outagemessage.id )
   join [${dbxschemaname}].app on (app.id = outagemessageapp.App_id)
   where outagemessage.Status_id = 'SID_OUTAGE_SCHEDULED_ACTIVE_COMPLETED' and
   (outagemessage.startTime <= @_enddate and outagemessage.endTime >= @_enddate)
   and outagemessageapp.App_id = @_serverAppId;

END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[useraccounts_create_proc];
GO


CREATE PROCEDURE [${dbxschemaname}].[useraccounts_create_proc]  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_legalEntityId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @accountID varchar(255)

      DECLARE
         @finished int = 0
         
      DECLARE
         @id varchar(255)

      DECLARE
         @accounttypeid varchar(255)

      DECLARE
         @accounttypename varchar(255)

      DECLARE
          accountData CURSOR LOCAL FORWARD_ONLY FOR 
             (   
                  SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE 
                               [${dbxschemaname}].contractaccounts.contractId = @_contractId AND
                               [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.accountId,@_accountsCSV) > '0'
             )

    OPEN accountData     
    FETCH NEXT FROM accountData into @accountID
      WHILE (1 = 1)
         BEGIN
            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN
               SET @id = (SELECT left(newid(), 50))
               SET @accounttypeid = (SELECT [${dbxschemaname}].contractaccounts.typeId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.accountId = @accountID);
                SET @accounttypename = (SELECT [${dbxschemaname}].accounttype.TypeDescription from [${dbxschemaname}].accounttype WHERE [${dbxschemaname}].accounttype.TypeID  = @accounttypeid);
                  INSERT [${dbxschemaname}].customeraccounts(
                     id, 
                     Customer_id,                     
                     Account_id, 
                     contractId, 
                     coreCustomerId,
                     companyLegalUnit,
                     accountType)
                     VALUES (
                        @id, 
                        @_userId, 
                        @accountID, 
                        @_contractId, 
                        @_coreCustomerId,
                        @_legalEntityId,
                        @accounttypename)
               END
            FETCH NEXT FROM accountData into @accountID
            CONTINUE

         END
      CLOSE accountData
      DEALLOCATE accountData

   END
GO


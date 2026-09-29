CREATE INDEX IDX_customerrequest_Customer_id ON customerrequest (Customer_id);
CREATE INDEX IDX_requestmessage_CustomerRequest_id ON requestmessage (CustomerRequest_id);
CREATE INDEX IDX_usernotification_user_id ON usernotification (user_id);

DROP PROCEDURE IF EXISTS `customer_messages_notifications_proc`;

DELIMITER $$
CREATE PROCEDURE `customer_messages_notifications_proc`(
   _customerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
   _startdate varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
   _enddate varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
   _serverAppId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
   SELECT count(requestmessage.id) AS messageCount
   FROM (customerrequest INNER JOIN requestmessage 
   ON ((requestmessage.CustomerRequest_id = customerrequest.id)))
   WHERE (customerrequest.Customer_id = _customerId AND
   requestmessage.IsRead = 'FALSE' AND
   customerrequest.softdeleteflag = 0 AND 
   customerrequest.Status_id <> 'SID_DELETED');

   SELECT count(requestmessage.id) AS priorityMessageCount
   FROM (customerrequest INNER JOIN requestmessage 
   ON ((requestmessage.CustomerRequest_id = customerrequest.id)))
   WHERE (customerrequest.Customer_id = _customerId AND
   requestmessage.IsRead = 'FALSE' AND
   requestmessage.isPriorityMessage = '1' AND 
   customerrequest.softdeleteflag = 0 AND 
   customerrequest.Status_id <> 'SID_DELETED');

   select count(usernotification.id) as notificationCount from usernotification 
   where (usernotification.user_id = _customerId
   and (usernotification.receiveddate >= cast(_startdate as datetime) 
   and usernotification.receiveddate < cast(_enddate as datetime))
   and usernotification.isRead = '0');

   SELECT outagemessage.id AS id, outagemessage.name AS name, outagemessage.startTime AS startTime, 
   outagemessage.endTime AS endTime, outagemessageapp.App_id AS app_id, app.Name AS app_name, 
   outagemessage.Status_id AS Status_id, outagemessage.MessageText AS MessageText, 
   outagemessage.createdby AS createdby, outagemessage.modifiedby AS modifiedby, 
   outagemessage.createdts AS createdts, outagemessage.lastmodifiedts AS lastmodifiedts
   FROM outagemessage join outagemessageapp on (outagemessageapp.Outagemessage_id = outagemessage.id ) join app on (app.id = outagemessageapp.App_id)
   where outagemessage.Status_id = 'SID_OUTAGE_SCHEDULED_ACTIVE_COMPLETED' and
   (outagemessage.startTime <= cast(_enddate as datetime) and outagemessage.endTime >= cast(_enddate as datetime)) and outagemessageapp.App_id = _serverAppId;
END$$
DELIMITER ;
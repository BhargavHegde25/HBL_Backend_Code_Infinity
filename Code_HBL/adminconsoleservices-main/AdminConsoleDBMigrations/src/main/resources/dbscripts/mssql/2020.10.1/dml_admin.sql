USE [${dbxdbname}];
GO

INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id], [service_name], [object_name], [operation], [permissions]) VALUES ('67f4068c-b7ba-44cc-b0b6-904647fef407', 'BusinessConfigObjService', 'businessconfiguration', 'getAlertConfigurations', 'ViewAppContent,API_ACCESS');
GO
INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id],  [name], [description], [isAccountLevel], [isMFAApplicable]) VALUES ('NOTIFICATION_DELETE', 'NOTIFICATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Delete Notification', 'Delete Notification', '0', '0');
INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id],[Action_id]) VALUES ('TYPE_ID_RETAIL', 'NOTIFICATION_DELETE'),('TYPE_ID_BUSINESS', 'NOTIFICATION_DELETE');
INSERT INTO [${dbxschemaname}].[compositeaction] ([id], [Permission_id], [Action_id], [Feature_id],[isEnabled], [createdby], [modifiedby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES 
('CAID164', 'PID45', 'NOTIFICATION_DELETE', 'NOTIFICATION', '1', 'Kony Dev', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby],  [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES 
('b4b355be-0cad-11eb-adc1-0242ac120002', 'DEFAULT_GROUP', 'NOTIFICATION_DELETE', NULL, NULL, 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES 
('b4b35820-0cad-11eb-adc1-0242ac120002', 'GROUP_ADMINISTRATOR', 'NOTIFICATION_DELETE', NULL, NULL, 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES 
('b4b35910-0cad-11eb-adc1-0242ac120002', 'GROUP_AUTHORIZER', 'NOTIFICATION_DELETE', NULL, NULL, 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [LimitType_id], [value], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES
('b4b359e2-0cad-11eb-adc1-0242ac120002', 'GROUP_CREATOR', 'NOTIFICATION_DELETE', NULL, NULL, 'UID11', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
	
INSERT INTO [${dbxschemaname}].[organisationactionlimit] ([id], [Organisation_id], [Action_id], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES 
('b4b35aaa-0cad-11eb-adc1-0242ac120002', '1', 'NOTIFICATION_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);
GO
DELETE FROM [${dbxschemaname}].[alertattribute] WHERE id='FREQUENCY';
GO
UPDATE [${dbxschemaname}].[termandconditiontext] SET [Content] = '<b class="">Consent for Access to Account Information</b><br /><br />In order to offer this service, Temenos needs your approval to access through its trusted partner Salt Edge Inc. the following information from the accounts you hold :<ul class=""><li class="">Your Account Details<br class="" />(account name, number and sort code, card number, balances, etc.)</li><li class="">Your Account Transactions<br class="" />(incoming and outgoing transactions details from 90 days, statement details, etc.)</li><li class="">All or some of your accounts will be accessed according to your bank’s terms and conditions<br class="" />(please contact them for full details)</li></ul><br class="" />The above information may be updated several times a day when you are offline. The Consent to Access that you&#39;re giving will be valid until deletion of the access connection.<br class="" />By clicking Confirm, you are consenting toTemenos Inc. to make this request to your financial institution to access the above information. Please read Temenos Inc. <a href="https://www.saltedge.com/pages/end_user_license_terms" title="https://www.saltedge.com/pages/end_user_license_terms" class="" rel="nofollow">End User License Agreement</a> and our <a href="https://www.saltedge.com/pages/privacy_policy" title="https://www.saltedge.com/pages/privacy_policy" class="" rel="nofollow">Privacy Policy</a> for details.' WHERE id = '3450023';
GO
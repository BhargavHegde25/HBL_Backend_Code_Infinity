GO
SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] ON

INSERT INTO [${dbxschemaname}].[emailtemplates] ([id], [TemplateName], [TemplateText], [Subject], [SenderName], [SenderEmail], [AlertChannel], [AlertLanguageCode], [Alert_id]) VALUES (108, N'ONBOARDING_PROSPECT_ACTIVATIONCODE_APPLICATIONID_TEMPLATE', N'%otp% is your temporary password to resume the "%applicationid%" application.', N'Temenos Digital', N'Temenos Digital', N'dbx_cl@infinity.com', NULL, NULL, NULL);

SET IDENTITY_INSERT [${dbxschemaname}].[emailtemplates] OFF
GO

INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration]) VALUES ('200', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'TRANSFER_FLOW_TYPE', 'UTF : Unified Transfer flow, CTF : Combined Transfer flow', 'UTF', 'CLIENT', '1');

DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE (serviceDefinitionId = 'f85d8392-9afe-4128-b23e-a370f138784f') and (actionId = 'ACCESS_CASH_POSITION');
DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE (serviceDefinitionId = 'f85d8392-9afe-4128-b23e-a370f138784f') and (actionId = 'APPROVAL_MATRIX_VIEW');
GO


INSERT INTO [${dbxschemaname}].mfa (id,App_id,Action_id,FrequencyType_id,FrequencyValue,Status_id,Description,PrimaryMFAType,SecondaryMFAType,SMSText,EmailSubject,EmailBody,createdby,modifiedby,softdeleteflag) VALUES
	 (N'1815812345',N'RETAIL_AND_BUSINESS_BANKING',N'PROFILE_SETTINGS_UPDATE',N'ALWAYS',NULL,N'SID_INACTIVE',N'profile update',N'SECURE_ACCESS_CODE',N'SECURITY_QUESTIONS',N'[#]OTP[/#]',N'Secure access code',N'[#]OTP[/#]',N'UID10',N'admin1',0);
	 
INSERT INTO [${dbxschemaname}].mfa (id,App_id,Action_id,FrequencyType_id,FrequencyValue,Status_id,Description,PrimaryMFAType,SecondaryMFAType,SMSText,EmailSubject,EmailBody,createdby,modifiedby,softdeleteflag) VALUES
	 (N'1812345678',N'RETAIL_AND_BUSINESS_BANKING',N'ONLINE_BANKING_ACCESS_DISABLE',N'ALWAYS',NULL,N'SID_INACTIVE',N'Infinity User Status Update',N'SECURE_ACCESS_CODE',N'SECURITY_QUESTIONS',N'[#]OTP[/#]',N'Secure access code',N'[#]OTP[/#]',N'UID10',N'admin1',0);

INSERT INTO [${dbxschemaname}].mfaserviceconfig (serviceName,transactionType,field,value,appId) VALUES
	 (N'PROFILE_SETTINGS_UPDATE',N'externalusermanagement_externalusers_updatedetails',NULL,NULL,NULL);
	
INSERT INTO [${dbxschemaname}].mfaserviceconfig (serviceName,transactionType,field,value,appId) VALUES
	 (N'ONLINE_BANKING_ACCESS_DISABLE',N'externalusermanagement_externalusers_2_updateuserstatus',NULL,NULL,NULL);
	
	-- Auto-generated SQL script #202107010827
UPDATE [${dbxschemaname}].featureaction
	SET MFA_id=N'1815812345',isMFAApplicable=1
	WHERE id=N'PROFILE_SETTINGS_UPDATE';
UPDATE [${dbxschemaname}].featureaction
	SET MFA_id=N'1812345678',isMFAApplicable=1
	WHERE id=N'ONLINE_BANKING_ACCESS_DISABLE';
	
INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'80b2a729-8143-43b3-aae8-e3cc8d546b1',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'DEFAULT_BUSINESS_SERVICE_ID',N'Deafult Business Service ID.',N'5801fa32-a416-45b6-af01-b22e2de93777',N'SERVER',0,NULL,NULL,'2020-12-07 11:34:52.000','2020-12-07 11:34:52.000','2020-12-07 11:34:52.000',0);

INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'80b2a729-c211-1234-aae8-e3cc8d546b1',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'AUTO_SYNC_BUSINESS_ACCOUNTS',N'Auto Sync Retail Accounts..',N'false',N'SERVER',0,NULL,NULL,'2020-12-07 11:34:52.000','2020-12-07 11:34:52.000','2020-12-07 11:34:52.000',0);

INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'80b2a729-7465-43b3-aae8-e3cc8d546b1',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'AUTO_SYNC_RETAIL_ACCOUNTS',N'Auto Sync Business Accounts.',N'false',N'SERVER',0,NULL,NULL,'2020-12-07 11:34:52.000','2020-12-07 11:34:52.000','2020-12-07 11:34:52.000',0);
	
INSERT INTO [${dbxschemaname}].service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
 (N'5beb589b-81d6-8143-aef6-886c0792f141',N'RBObjects',N'Accounts',N'getInfinityAccounts',N'API_ACCESS');
 
INSERT INTO [${dbxschemaname}].service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
 (N'5beb589b-81d6-418f-8143-886c0792f141',N'RBObjects',N'InfinityUser',N'assignInfinityUserToPrimaryRetailContract',N'API_ACCESS');
 
INSERT INTO [${dbxschemaname}].service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
 (N'5beb589b-8143-418f-aef6-886c0792f141',N'RBObjects',N'InfinityUser',N'createBusinessContract',N'API_ACCESS');
 
GO

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'AlertsManagement',[object_name] = 'Alerts' ,[operation] = 'getCategories' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUserAlerts' AND [operation] = 'getCustomerAlertCategoryPreference';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'AlertsManagement',[object_name] = 'Alerts' ,[operation] = 'getArrangementsStatus' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUserAlerts' AND [operation] = 'getCustomerAccountAlertPreference';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'AlertsManagement',[object_name] = 'Alerts' ,[operation] = 'getPreferences' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUserAlerts' AND [operation] = 'getCustomerAlertTypePreference';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'AlertsManagement',[object_name] = 'Alerts' ,[operation] = 'setPreferences' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUserAlerts' AND [operation] = 'setAlertPreferences';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'requestResetPasswordOTP' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'requestResetPasswordOTP';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'verifyUser' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'verifyDbxUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'resetUserPassword' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'resetDbxUserPassword';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'getPasswordRulesAndPolicy' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'getPasswordRulesAndPolicy';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'getPasswordLockoutSettings' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'getPasswordLockoutSettings';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'validateActivationCodeForEnrollment' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'validateActivationCodeForEnrollment';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'UpdatePasswordForActivationFlow' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'UpdatePasswordForActivationFlow';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'requestEnrollOTP' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'requestEnrollOTP';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'verifyOTPPreLogin' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'verifyOTPPreLogin';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'getUserNameAndPasswordRulesAndPolicies' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'getUserNameAndPasswordRulesAndPolicies';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'createUser' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'createDbxCustomer';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'updateUserStatus' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'updateDBXUserStatus';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'updateUserProfileImage' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'updateUserProfileImage';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'getUserProfileImage' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'getUserProfileImage';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'deleteUserProfileImage' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'deleteUserProfileImage';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'updateUserPassword' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'updateDBXUserPassword';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'requestUpdateSecurityQuestionsOTP' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'requestUpdateSecurityQuestionsOTP';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'verifyUpdateSecurityQuestionsOTP' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'verifyUpdateSecurityQuestionsOTP';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_2' ,[operation] = 'OFACAndCIPChecks' WHERE [service_name] = 'RBObjects' AND [object_name] = 'DbxUser' AND [operation] = 'OFACAndCIPChecks';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'getUsersFromContract' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getAssociatedContractUsers';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'getRelatedCustomers' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getAllEligibleRelationalCustomers';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'GetListCoreCustomerFeatureActionLimits' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'GetListCoreCustomerFeatureActionLimits';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'createUser' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'createInfinityUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'editUser' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'editInfinityUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'getCustomersForUser' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getAssociatedCustomers';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'getUserDetails' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getInfinityUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers_1' ,[operation] = 'resendActivationCode' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'resendActivationCode';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Roles' ,[operation] = 'createRole' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'createInfinityCustomRole';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Roles' ,[operation] = 'getRolesFromContract' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getCompanyLevelCustomRoles';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Roles' ,[operation] = 'getRoleDetails' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'getInfinityCustomRoleDetails';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Roles' ,[operation] = 'updateRole' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'updateInfinityCustomRole';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Roles' ,[operation] = 'verifyRoleName' WHERE [service_name] = 'RBObjects' AND [object_name] = 'InfinityUser' AND [operation] = 'verifyInfinityCustomRoleName';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'Security' ,[operation] = 'generateCaptcha' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Security' AND [operation] = 'generateCaptcha';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'SecurityQuestions' ,[operation] = 'getSecurityQuestions' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecurityQuestions' AND [operation] = 'getSecurityQuestions';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'SecurityQuestions' ,[operation] = 'updateSecurityQuestions' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecurityQuestions' AND [operation] = 'resetSecurityQuestions';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'TrackDeviceRegistration' ,[operation] = 'trackDeviceRegistration' WHERE [service_name] = 'RBObjects' AND [object_name] = 'TrackDeviceRegistration' AND [operation] = 'trackDeviceRegistration';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences_1' ,[operation] = 'activateBillPay' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'activateBillPaymentForUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences_1' ,[operation] = 'updateBillPayPreferredAccount' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'updatePreferredBillPayAccount';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences_1' ,[operation] = 'deactivateP2P' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'deactivateP2P';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences_1' ,[operation] = 'activateP2P' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'activateP2PForUser';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences_1' ,[operation] = 'updateP2PPreferredAccount' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'updatePreferredP2PAccounts';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'checkUserEnrolled' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'checkUserEnrolled';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'create' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'create';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'get' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'get';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'UpdateDetails' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'updateCustomerDetails';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'verifyExistingPassword' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'verifyExistingPassword';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'partialupdate' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'partialupdate';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ExternalUserManagement',[object_name] = 'ExternalUsers' ,[operation] = 'verifyPin' WHERE [service_name] = 'RBObjects' AND [object_name] = 'User' AND [operation] = 'verifyPin';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'ArrangementPreferences' ,[operation] = 'UpdateDetails' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Accounts' AND [operation] = 'updateUserAccountSettings';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'Holdings',[object_name] = 'DigitalArrangements' ,[operation] = 'get' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Accounts' AND [operation] = 'get';

GO

UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'createRequest' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'createCustomerRequest';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'deleteAttachement' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'discardMessageAttachments';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'getAllMessagesForARequest' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'getAllMessagesForARequest';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'getRequestCategory' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'getRequestCategory';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'getRequests' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'getRequests';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'getUnreadMessageCount' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'getUnreadMessageCount';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'updateRequest' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'updateRequest';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'SecureMessaging',[object_name] = 'Message' ,[operation] = 'addAttachment' WHERE [service_name] = 'RBObjects' AND [object_name] = 'SecureMessaging' AND [operation] = 'uploadMessageBinary';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Information' ,[operation] = 'getContactUs' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Informationcontent' AND [operation] = 'getContactUs';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Information' ,[operation] = 'getFAQs' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Informationcontent' AND [operation] = 'getFAQs';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Information' ,[operation] = 'getPrivacyPolicy' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Informationcontent' AND [operation] = 'getPrivacyPolicy';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'TermsAndConditions' ,[operation] = 'getPreLogin' WHERE [service_name] = 'RBObjects' AND [object_name] = 'TermsAndConditions' AND [operation] = 'getCustomerTermsAndConditionsPreLogin';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'TermsAndConditions' ,[operation] = 'getPostLogin' WHERE [service_name] = 'RBObjects' AND [object_name] = 'TermsAndConditions' AND [operation] = 'getCustomerTermsAndConditionsPostLogin';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'TermsAndConditions' ,[operation] = 'createCustomerTNCForLogin' WHERE [service_name] = 'RBObjects' AND [object_name] = 'TermsAndConditions' AND [operation] = 'createCustomerTNCForLogin';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'CustomerFeedback' ,[operation] = 'createFeedback' WHERE [service_name] = 'CustomerFeedback' AND [object_name] = 'Feedback' AND [operation] = 'createCustomerFeedback';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getList' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getLocationList';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getDetails' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getLocationDetails';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getQuery' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getLocationsQuery';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ForeignExchange',[object_name] = 'Forex' ,[operation] = 'fetchBaseCurrency' WHERE [service_name] = 'ForexObjects' AND [object_name] = 'Forex' AND [operation] = 'fetchBaseCurrency';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ForeignExchange',[object_name] = 'Forex' ,[operation] = 'fetchDashboardCurrencyList' WHERE [service_name] = 'ForexObjects' AND [object_name] = 'Forex' AND [operation] = 'fetchDashboardCurrencyList';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ForeignExchange',[object_name] = 'Forex' ,[operation] = 'fetchDashboardCurrencyRates' WHERE [service_name] = 'ForexObjects' AND [object_name] = 'Forex' AND [operation] = 'fetchDashboardCurrencyRates';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ForeignExchange',[object_name] = 'Forex' ,[operation] = 'fetchCurrencyRates' WHERE [service_name] = 'ForexObjects' AND [object_name] = 'Forex' AND [operation] = 'fetchCurrencyRates';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ForeignExchange',[object_name] = 'Forex' ,[operation] = 'updateRecentCurrencies' WHERE [service_name] = 'ForexObjects' AND [object_name] = 'Forex' AND [operation] = 'updateRecentCurrencies';
GO
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getAddressSuggestions' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getAddressSuggestions';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getAddress' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getLocationAddress';
UPDATE [${dbxschemaname}].[service_permission_mapper] SET [service_name] = 'ContentManagement',[object_name] = 'Locations' ,[operation] = 'getRange' WHERE [service_name] = 'RBObjects' AND [object_name] = 'Locations' AND [operation] = 'getLocationRange';
UPDATE [${dbxschemaname}].[alertsubtype] SET [isGlobal] = 0 WHERE [isGlobal] IS NULL;
GO
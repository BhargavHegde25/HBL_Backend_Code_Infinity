INSERT INTO emailtemplates (`id`,`TemplateName`,`TemplateText`,`Subject`,`SenderName`,`SenderEmail`,`AlertChannel`,`AlertLanguageCode`,`Alert_id`) VALUES ('108','ONBOARDING_PROSPECT_ACTIVATIONCODE_APPLICATIONID_TEMPLATE','%otp% is your temporary password to resume the "%applicationid%" application.','Temenos Digital','Temenos Digital','dbx_cl@infinity.com',NULL,NULL,NULL);

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('200', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'TRANSFER_FLOW_TYPE', 'UTF : Unified Transfer flow, CTF : Combined Transfer flow', 'UTF', 'CLIENT', '1');

DELETE from `servicedefinitionactionlimit` where `serviceDefinitionId`='f85d8392-9afe-4128-b23e-a370f138784f'
and`actionId`='ACCESS_CASH_POSITION';

DELETE from `servicedefinitionactionlimit` where `serviceDefinitionId`='f85d8392-9afe-4128-b23e-a370f138784f'
and`actionId`='APPROVAL_MATRIX_VIEW';

INSERT IGNORE INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d9268184-abf0-46eb-abd5-01cc6841caf5', 'RBObjects', 'DownloadTransactionPDF', 'get', 'ALLOW');

INSERT INTO mfa (id,App_id,Action_id,FrequencyType_id,FrequencyValue,Status_id,Description,PrimaryMFAType,SecondaryMFAType,SMSText,EmailSubject,EmailBody,createdby,modifiedby,softdeleteflag) VALUES
	 ('1815812345','RETAIL_AND_BUSINESS_BANKING','PROFILE_SETTINGS_UPDATE','ALWAYS','','SID_INACTIVE','Profile Update','SECURE_ACCESS_CODE','SECURITY_QUESTIONS','[#]OTP[/#]','Secure OTP','[#]OTP[/#]','UID10','admin1',0);
	 
INSERT INTO mfa (id,App_id,Action_id,FrequencyType_id,FrequencyValue,Status_id,Description,PrimaryMFAType,SecondaryMFAType,SMSText,EmailSubject,EmailBody,createdby,modifiedby,softdeleteflag) VALUES
	 ('1812345678','RETAIL_AND_BUSINESS_BANKING','ONLINE_BANKING_ACCESS_DISABLE','ALWAYS','','SID_INACTIVE','Infinity User Status Update','SECURE_ACCESS_CODE','SECURITY_QUESTIONS','[#]OTP[/#]','Secure OTP','[#]OTP[/#]','UID10','admin1',0);

INSERT INTO mfaserviceconfig (serviceName,transactionType,field,value,appId) VALUES
	 ('PROFILE_SETTINGS_UPDATE','externalusermanagement_externalusers_updatedetails',NULL,NULL,NULL);

INSERT INTO mfaserviceconfig (serviceName,transactionType,field,value,appId) VALUES
	 ('ONLINE_BANKING_ACCESS_DISABLE','externalusermanagement_externalusers_2_updateuserstatus',NULL,NULL,NULL);
	 
--  Auto-generated SQL script #202106291825
UPDATE featureaction
	SET isMFAApplicable=1,MFA_id='1815812345'
	WHERE id='PROFILE_SETTINGS_UPDATE';
UPDATE featureaction
	SET isMFAApplicable=1,MFA_id='1812345678'
	WHERE id='ONLINE_BANKING_ACCESS_DISABLE';
	
INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 ('80b2a729-c211-8143-aae8-e3cc8d546b1','C360_CONFIG_BUNDLE','PREFERENCE','DEFAULT_BUSINESS_SERVICE_ID','Deafult Business Service ID.','707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','SERVER',0,NULL,NULL,'2020-12-07 11:34:52','2020-12-07 11:34:52','2020-12-07 11:34:52',0);

INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
 ('80b2a729-c211-6578-aae8-e3cc8d546b1','C360_CONFIG_BUNDLE','PREFERENCE','AUTO_SYNC_BUSINESS_ACCOUNTS','Auto Sync Retail Accounts.','false','SERVER',0,NULL,NULL,'2020-12-07 11:34:52','2020-12-07 11:34:52','2020-12-07 11:34:52',0);
	
	
INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
 ('80b2a729-c211-1234-aae8-e3cc8d546b1','C360_CONFIG_BUNDLE','PREFERENCE','AUTO_SYNC_RETAIL_ACCOUNTS','Auto Sync Business Accounts.','false','SERVER',0,NULL,NULL,'2020-12-07 11:34:52','2020-12-07 11:34:52','2020-12-07 11:34:52',0);	
 
INSERT INTO service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
	 ('5beb589b-81d6-8143-aef6-886c0792f141','RBObjects','Accounts','getInfinityAccounts','API_ACCESS');	 
	
INSERT INTO service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
	 ('5beb589b-81d6-1234-aef6-886c0792f141','RBObjects','InfinityUser','assignInfinityUserToPrimaryRetailContract','API_ACCESS');
	
INSERT INTO service_permission_mapper (id,service_name,object_name,operation,permissions) VALUES
	 ('5beb589b-8143-8143-aef6-886c0792f141','RBObjects','InfinityUser','createBusinessContract','API_ACCESS'); 
	 
UPDATE `service_permission_mapper` SET `service_name` = 'AlertsManagement',`object_name` = 'Alerts' ,`operation` = 'getCategories' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUserAlerts' AND `operation` = 'getCustomerAlertCategoryPreference';
UPDATE `service_permission_mapper` SET `service_name` = 'AlertsManagement',`object_name` = 'Alerts' ,`operation` = 'getArrangementsStatus' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUserAlerts' AND `operation` = 'getCustomerAccountAlertPreference';
UPDATE `service_permission_mapper` SET `service_name` = 'AlertsManagement',`object_name` = 'Alerts' ,`operation` = 'getPreferences' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUserAlerts' AND `operation` = 'getCustomerAlertTypePreference';
UPDATE `service_permission_mapper` SET `service_name` = 'AlertsManagement',`object_name` = 'Alerts' ,`operation` = 'setPreferences' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUserAlerts' AND `operation` = 'setAlertPreferences';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'requestResetPasswordOTP' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'requestResetPasswordOTP';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'verifyUser' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'verifyDbxUser';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'resetUserPassword' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'resetDbxUserPassword';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'getPasswordRulesAndPolicy' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'getPasswordRulesAndPolicy';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'getPasswordLockoutSettings' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'getPasswordLockoutSettings';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'validateActivationCodeForEnrollment' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'validateActivationCodeForEnrollment';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'UpdatePasswordForActivationFlow' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'UpdatePasswordForActivationFlow';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'requestEnrollOTP' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'requestEnrollOTP';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'verifyOTPPreLogin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'verifyOTPPreLogin';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'getUserNameAndPasswordRulesAndPolicies' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'getUserNameAndPasswordRulesAndPolicies';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'createUser' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'createDbxCustomer';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'updateUserStatus' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'updateDBXUserStatus';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'updateUserProfileImage' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'updateUserProfileImage';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'getUserProfileImage' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'getUserProfileImage';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'deleteUserProfileImage' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'deleteUserProfileImage';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'updateUserPassword' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'updateDBXUserPassword';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'requestUpdateSecurityQuestionsOTP' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'requestUpdateSecurityQuestionsOTP';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'verifyUpdateSecurityQuestionsOTP' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'verifyUpdateSecurityQuestionsOTP';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_2' ,`operation` = 'OFACAndCIPChecks' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DbxUser' AND `operation` = 'OFACAndCIPChecks';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'getUsersFromContract' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getAssociatedContractUsers';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'getRelatedCustomers' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getAllEligibleRelationalCustomers';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'GetListCoreCustomerFeatureActionLimits' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'GetListCoreCustomerFeatureActionLimits';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'createUser' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'createInfinityUser';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'editUser' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'editInfinityUser';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'getCustomersForUser' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getAssociatedCustomers';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'getUserDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getInfinityUser';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers_1' ,`operation` = 'resendActivationCode' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'resendActivationCode';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Roles' ,`operation` = 'createRole' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'createInfinityCustomRole';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Roles' ,`operation` = 'getRolesFromContract' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getCompanyLevelCustomRoles';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Roles' ,`operation` = 'getRoleDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'getInfinityCustomRoleDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Roles' ,`operation` = 'updateRole' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'updateInfinityCustomRole';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Roles' ,`operation` = 'verifyRoleName' WHERE `service_name` = 'RBObjects' AND `object_name` = 'InfinityUser' AND `operation` = 'verifyInfinityCustomRoleName';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Security' ,`operation` = 'generateCaptcha' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Security' AND `operation` = 'generateCaptcha';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'SecurityQuestions' ,`operation` = 'getSecurityQuestions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecurityQuestions' AND `operation` = 'getSecurityQuestions';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'SecurityQuestions' ,`operation` = 'updateSecurityQuestions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecurityQuestions' AND `operation` = 'resetSecurityQuestions';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'TrackDeviceRegistration' ,`operation` = 'trackDeviceRegistration' WHERE `service_name` = 'RBObjects' AND `object_name` = 'TrackDeviceRegistration' AND `operation` = 'trackDeviceRegistration';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences_1' ,`operation` = 'activateBillPay' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'activateBillPaymentForUser';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences_1' ,`operation` = 'updateBillPayPreferredAccount' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'updatePreferredBillPayAccount';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences_1' ,`operation` = 'deactivateP2P' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'deactivateP2P';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences_1' ,`operation` = 'activateP2P' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'activateP2PForUser';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences_1' ,`operation` = 'updateP2PPreferredAccount' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'updatePreferredP2PAccounts';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'checkUserEnrolled' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'checkUserEnrolled';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'create' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'create';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'UpdateDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'updateCustomerDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'verifyExistingPassword' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'verifyExistingPassword';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'partialupdate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'partialupdate';
UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'ExternalUsers' ,`operation` = 'verifyPin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'User' AND `operation` = 'verifyPin';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementPreferences' ,`operation` = 'UpdateDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'updateUserAccountSettings';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'DigitalArrangements' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'get';

UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'createCustomerRequest';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'deleteAttachement' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'discardMessageAttachments';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'getAllMessagesForARequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'getAllMessagesForARequest';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'getRequestCategory' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'getRequestCategory';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'getRequests' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'getRequests';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'getUnreadMessageCount' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'getUnreadMessageCount';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'updateRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'updateRequest';
UPDATE `service_permission_mapper` SET `service_name` = 'SecureMessaging',`object_name` = 'Message' ,`operation` = 'addAttachment' WHERE `service_name` = 'RBObjects' AND `object_name` = 'SecureMessaging' AND `operation` = 'uploadMessageBinary';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Information' ,`operation` = 'getContactUs' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Informationcontent' AND `operation` = 'getContactUs';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Information' ,`operation` = 'getFAQs' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Informationcontent' AND `operation` = 'getFAQs';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Information' ,`operation` = 'getPrivacyPolicy' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Informationcontent' AND `operation` = 'getPrivacyPolicy';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'TermsAndConditions' ,`operation` = 'getPreLogin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'TermsAndConditions' AND `operation` = 'getCustomerTermsAndConditionsPreLogin';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'TermsAndConditions' ,`operation` = 'getPostLogin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'TermsAndConditions' AND `operation` = 'getCustomerTermsAndConditionsPostLogin';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'TermsAndConditions' ,`operation` = 'createCustomerTNCForLogin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'TermsAndConditions' AND `operation` = 'createCustomerTNCForLogin';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'CustomerFeedback' ,`operation` = 'createFeedback' WHERE `service_name` = 'CustomerFeedback' AND `object_name` = 'Feedback' AND `operation` = 'createCustomerFeedback';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getList' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getLocationList';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getLocationDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getQuery' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getLocationsQuery';
UPDATE `service_permission_mapper` SET `service_name` = 'ForeignExchange',`object_name` = 'Forex' ,`operation` = 'fetchBaseCurrency' WHERE `service_name` = 'ForexObjects' AND `object_name` = 'Forex' AND `operation` = 'fetchBaseCurrency';
UPDATE `service_permission_mapper` SET `service_name` = 'ForeignExchange',`object_name` = 'Forex' ,`operation` = 'fetchDashboardCurrencyList' WHERE `service_name` = 'ForexObjects' AND `object_name` = 'Forex' AND `operation` = 'fetchDashboardCurrencyList';
UPDATE `service_permission_mapper` SET `service_name` = 'ForeignExchange',`object_name` = 'Forex' ,`operation` = 'fetchDashboardCurrencyRates' WHERE `service_name` = 'ForexObjects' AND `object_name` = 'Forex' AND `operation` = 'fetchDashboardCurrencyRates';
UPDATE `service_permission_mapper` SET `service_name` = 'ForeignExchange',`object_name` = 'Forex' ,`operation` = 'fetchCurrencyRates' WHERE `service_name` = 'ForexObjects' AND `object_name` = 'Forex' AND `operation` = 'fetchCurrencyRates';
UPDATE `service_permission_mapper` SET `service_name` = 'ForeignExchange',`object_name` = 'Forex' ,`operation` = 'updateRecentCurrencies' WHERE `service_name` = 'ForexObjects' AND `object_name` = 'Forex' AND `operation` = 'updateRecentCurrencies';

UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getAddressSuggestions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getAddressSuggestions';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getAddress' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getLocationAddress';
UPDATE `service_permission_mapper` SET `service_name` = 'ContentManagement',`object_name` = 'Locations' ,`operation` = 'getRange' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Locations' AND `operation` = 'getLocationRange';
INSERT INTO `dbxdb`.`feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `Service_Fee`, `DisplaySequence`, `isPrimary`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN', 'RETAIL_AND_BUSINESS_BANKING', 'Cant Sign In', 'Cant Sign In', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '0.00', '90', '0', 'admin1', '2024-12-03 18:50:43', '2025-08-31 16:03:33', '2024-12-03 18:50:43', '0', 'NP0010001');

INSERT INTO `dbxdb`.`mfa` (`id`, `App_id`, `Action_id`, `FrequencyType_id`, `Status_id`, `Description`, `PrimaryMFAType`, `SecondaryMFAType`, `SMSText`, `EmailSubject`, `EmailBody`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('1868396797', 'RETAIL_AND_BUSINESS_BANKING', 'CANT_SIGN_IN_ACTIVATE', 'ALWAYS', 'SID_ACTIVE', 'Cant Sign In Flow', 'TRANSACTION_PIN', 'SECURE_ACCESS_CODE', '[#]OTP[/#]', 'Your Secure Access Code', '[#]OTP[/#]', 'UID10', 'user1', '2023-11-08 11:57:50', '2024-07-18 15:17:12', '2023-11-08 11:57:50', '0', 'NP0010001');
INSERT INTO `dbxdb`.`featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN', 'en-US', 'Cant Sign In', 'Cant Sign In', 'NP0010001');
INSERT INTO `dbxdb`.`featureroletype` (`RoleType_id`, `Feature_id`,`companyLegalUnit`) VALUES ('TYPE_ID_RETAIL', 'CANT_SIGN_IN', 'NP0010001');
INSERT INTO `dbxdb`.`rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('CANT_SIGN_IN-CREATE', '2024-09-10 20:47:13', '2024-10-29 09:28:26', '0');
INSERT INTO `dbxdb`.`featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `isApprovalAction`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'CANT_SIGN_IN', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'CANT_SIGN_IN-CREATE', 'Cant Sign In activate', 'Enable or disable Cant Sign In', '1', '0', '1868396797', '0', '3', '2024-09-26 12:09:43', '2024-09-26 12:09:43', '2024-09-26 12:09:43', '0', 'SID_ACTION_ACTIVE', 'CREATE', 'CUSTOMERID_LEVEL', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'de-DE', 'Cant Sign In', 'Cant Sign In flow for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'en-GB', 'Cant Sign In', 'Cant Sign In flow for customer', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'en-US', 'Cant Sign In', 'Cant Sign In flow for customer', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'es-ES', 'Cant Sign In', 'Cant Sign In flow for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'fr-FR', 'Cant Sign In', 'Cant Sign In flow for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`, `createdts`, `lastmodifiedts`, `companyLegalUnit`) VALUES ('CANT_SIGN_IN_ACTIVATE', 'CANT_SIGN_IN_ACTIVATE', 'CANT_SIGN_IN', 'Cant Sign In', 'Cant Sign In', '2024-09-10 20:47:42', '2024-09-10 20:47:42', 'NP0010001');
INSERT INTO `dbxdb`.`featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TYPE_ID_RETAIL', 'CANT_SIGN_IN_ACTIVATE', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');
UPDATE `dbxdb`.`mfa` SET `Action_id` = 'CANT_SIGN_IN_ACTIVATE' WHERE (`id` = '1868396797') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CANT_SIGN_IN');
UPDATE `dbxdb`.`featureaction` SET `isAccountLevel` = '0', `isMFAApplicable` = '1' WHERE (`id` = 'CANT_SIGN_IN_ACTIVATE') and (`companyLegalUnit` = 'NP0010001');


INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('415', 'CANTSIGININ_MFA', 'Your Secure Access Code %OTP%', 'Your Secure Access Code', 'himalayanbank', 'ibanking@himalayanbank.com');


---------------

Below changes are made for Mantis defect #1015 fix:

Changes Implemented
----------------
Made visualizer changes.
Changes are made in CustomerUpdateDBPStatus.java in adminconsole-service.jar.
Removed preprocessor from the following API:
https://infinityqa.himalayanbank.com/services/data/v1/CustomerManagementObjService/operations/Customer/updateDBPUserStatus
Database Changes
--------------------
Execute the below script and refresh metadata for the customer table from the Admin Console Services CRUD layer.

ALTER TABLE dbxdb.customer ADD COLUMN remarksForSuspend VARCHAR(200) NULL AFTER lockCount;


/** Added a new column in the contract table:  **/
ALTER TABLE `dbxdb`.`contract`
ADD COLUMN `infinityAccess` VARCHAR(50) NULL AFTER `rejectedReason`;


/** Refreshed metadata and published the dbpRBlocalservices integration service.**/
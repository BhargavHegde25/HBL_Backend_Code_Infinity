--------------------- Open fixed Deposit T&C------------------

INSERT INTO `dbxdb`.`termandcondition` (`id`, `Code`, `Title`, `Description`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('1920044', 'Fixed_deposit', 'Open Fixed Deposit', 'Open Fixed Deposit Account', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '0', 'NP0010001');

INSERT INTO `dbxdb`.`termandconditionapp` (`id`, `TermAndConditionId`, `AppId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('2312374', '1920044', 'RETAIL_AND_BUSINESS_BANKING', 'Kony user', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '0', 'NP0010001');

INSERT INTO `dbxdb`.`termandconditiontext` (`id`, `TermAndConditionId`, `LanguageCode`, `Version_Id`, `Content`, `ContentType_id`, `ContentModifiedBy`, `ContentModifiedOn`, `Status_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('3450067', '1920044', 'en-US', '1.0', 'Sample text', 'TEXT', 'Kony User', '2024-09-10 15:16:00', 'SID_TANDC_ACTIVE', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '2024-09-10 15:16:00', '0', 'NP0010001');



---------- OPEN FIXED DEPOSIT ENTITELEMNT SCRIPTS OF SPOTLIGHT----------

INSERT INTO `dbxdb`.`feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('FIXED_DEPOSIT', 'RETAIL_AND_BUSINESS_BANKING', 'Open Fixed Deposit', 'Open Fixed Deposit', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '88', '0', '2024-12-03 18:50:43', '2024-12-03 18:50:43', '2024-12-03 18:50:43', '0', 'NP0010001');
INSERT INTO `dbxdb`.`featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('FIXED_DEPOSIT', 'en-US', 'Open Fixed Deposit', 'Open Fixed Deposit', 'NP0010001');
INSERT INTO `dbxdb`.`featureroletype` (`RoleType_id`, `Feature_id`,`companyLegalUnit`) VALUES ('TYPE_ID_RETAIL', 'FIXED_DEPOSIT', 'NP0010001');
INSERT INTO `dbxdb`.`rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('FIXED_DEPOSIT-CREATE', '2024-09-10 20:47:13', '2024-10-29 09:28:26', '0');
INSERT INTO `dbxdb`.`mfa` (`id`, `App_id`, `Action_id`, `FrequencyType_id`, `FrequencyValue`, `Status_id`, `Description`, `PrimaryMFAType`, `SecondaryMFAType`, `SMSText`, `EmailSubject`, `EmailBody`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('1692872787', 'RETAIL_AND_BUSINESS_BANKING', 'FIXED_DEPOSIT', 'ALWAYS', NULL, 'SID_ACTIVE', 'Enable or disable open fixed deposit', 'TRANSACTION_PIN', 'SECURE_ACCESS_CODE', 'test', 'test', 'test', 'UID10', 'user1', '2023-11-08 17:27:50', '2024-07-18 20:47:12', '2023-11-08 17:27:50', '0', 'NP0010001');
INSERT INTO `dbxdb`.`featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `isApprovalAction`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'FIXED_DEPOSIT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'FIXED_DEPOSIT-CREATE', 'Open fixed deposit activate', 'Enable or disable open fixed deposit', '1', '0', '1692872787', '0', '3', '2024-09-26 12:09:43', '2024-09-26 12:09:43', '2024-09-26 12:09:43', '0', 'SID_ACTION_ACTIVE', 'CREATE', 'CUSTOMERID_LEVEL', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'de-DE', 'Open fixed deposit', 'Open fixed deposit account for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'en-GB', 'Open fixed deposit', 'Open fixed deposit account for customer', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'en-US', 'Open fixed deposit', 'Open fixed deposit account for customer', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'es-ES', 'Open fixed deposit', 'Open fixed deposit account for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'fr-FR', 'Open fixed deposit', 'Open fixed deposit account for customer', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '2024-09-10 20:47:42', '0', 'NP0010001');
INSERT INTO `dbxdb`.`dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`, `createdts`, `lastmodifiedts`, `companyLegalUnit`) VALUES ('OPEN_FIXED_DEPOSIT_ACTIVATE', 'OPEN_FIXED_DEPOSIT_ACTIVATE', 'FIXED_DEPOSIT', 'Open fixed deposit', 'Open fixed deposit', '2024-09-10 20:47:42', '2024-09-10 20:47:42', 'NP0010001');
INSERT INTO `dbxdb`.`featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES ('TYPE_ID_RETAIL', 'OPEN_FIXED_DEPOSIT_ACTIVATE', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '2024-09-10 20:47:41', '0', 'NP0010001');

UPDATE `dbxdb`.`featureaction` SET `isAccountLevel` = '0' WHERE (`id` = 'OPEN_FIXED_DEPOSIT_ACTIVATE') and (`companyLegalUnit` = 'NP0010001');


--------------------stop cheque reasons update-------------

UPDATE `dbxdb`.`application` SET `stopReasons` = '{\"Cheque Stolen\" : \"CHKSTO\",\"Cheque Lost\" : \"CHKLOST\",\"Cheque Destroyed\" : \"CHKDES\"}' WHERE (`id` = '2');









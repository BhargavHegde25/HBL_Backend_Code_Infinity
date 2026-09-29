INSERT INTO `service_permission_mapper` (`id`,`service_name`,`object_name`,`operation`,`permissions`) VALUES ('34fad7ec-d2ff-4142-b5b6-9c7fa656049c','Utility','Application','getServerTimeZoneOffset','ALLOW');

UPDATE `privacypolicy` SET `Description` = 'At Temenos Digital Bank, the safeguarding of your account information is our top priority. We strive to keep our members informed on current security issues, as well as providing the tools to help protect yourself. Please check back periodically to view new information as it becomes available.<br /><h2>Your Account Security</h2><br />We protect your online security. Keeping financial and personal information about you secure and confidential is one of our most important responsibilities. Our systems are protected, so information remains secure.<ol><li>Computer virus protection detects and prevents computer viruses from entering our computer network systems.</li><li>Firewalls block unauthorized access by individuals or networks. Firewalls are just one way we protect our computer network systems that interact with the Internet.</li><li>Secure transmissions ensure information remains confidential. Temenos Digital Bank uses encryption technology such as Secure Socket Layer (SSL) on its Web sites to securely transmit information between you and the bank.</li><li>Send secure e-mail to almost any department in the bank through the Contact Us section. Because an Internet e-mail response back to you may not be secure, we will not include confidential account information in an e-mail response. In addition, you will never be asked for confidential information, such as passwords or PINs, through e-mail. Besides e-mail, you can contact us by phone or visiting any branch.</li><li>Regular evaluations of our security features and continuous research of new advances in security technology ensure that your personal information is protected.</li></ol>' WHERE `id` = 'PRIV_POL_ID1';

DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';
DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';
DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE';
DELETE FROM `dependentactions` WHERE `dependentactionId` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL';


DELETE FROM `featureaction` where `id` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE' and `Feature_id` = 'INTRA_BANK_FUND_TRANSFER';
DELETE FROM `featureaction` where `id` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and `Feature_id` = 'INTRA_BANK_FUND_TRANSFER';
DELETE FROM `featureaction` where `id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE' and `Feature_id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER';
DELETE FROM `featureaction` where `id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and `Feature_id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER';
DELETE FROM `featureaction` where `id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE' and `Feature_id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER';
DELETE FROM `featureaction` where `id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL' and `Feature_id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER';

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('BENEFICIARY_MANAGEMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Beneficiary Management' ,'Beneficiary Management' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('BENEFICIARY_MANAGEMENT' ,'en-GB' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('BENEFICIARY_MANAGEMENT' ,'de-DE' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('BENEFICIARY_MANAGEMENT' ,'en-US' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('BENEFICIARY_MANAGEMENT' ,'es-ES' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('BENEFICIARY_MANAGEMENT' ,'fr-FR' ,'Beneficiary Management' ,'Beneficiary Management' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'BENEFICIARY_MANAGEMENT');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'BENEFICIARY_MANAGEMENT');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient Domestic Transfer Self Approval', 'Create Recipient Domestic Transfer Self Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Domestic Transfer Self Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID996', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient International Transfer Approval', 'Create Recipient International Transfer Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient International Transfer Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID995', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient International Transfer Self Approval', 'Create Recipient International Transfer Self Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient International Transfer Self Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID994', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient Domestic Transfer Approval', 'Create Recipient Domestic Transfer Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Domestic Transfer Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID993', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-GB', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','de-DE', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','en-US', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','es-ES', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL','fr-FR', 'Create Recipient Same bank Transfer Self Approval', 'Create Recipient Same bank Transfer Self Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Same bank Transfer Self Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID992', 'PID45', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT-APPROVE', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval', 0, 0, null, 0, 50, null, 0, 'APPROVE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','en-GB', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','de-DE', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','en-US', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','es-ES', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE','fr-FR', 'Create Recipient Same bank Transfer Approval', 'Create Recipient Same bank Transfer Approval');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', '0');

INSERT INTO `dependentactions` (`actionId`, `dependentactionId`, `featureId`, `actionName`, `featureName`) VALUES ('INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', 'Create Recipient Same bank Transfer Approval', 'Beneficiary Management');


INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID991', 'PID45', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'BENEFICIARY_MANAGEMENT', '1', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1000', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1001', 'PID45', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1002', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTER_BANK_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1003', 'PID45', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER', '1', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1004', 'PID45', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'INTRA_BANK_FUND_TRANSFER', '1', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `softdeleteflag`) VALUES ('CAID1005', 'PID45', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'INTRA_BANK_FUND_TRANSFER', '1', '0');


INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');


INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');



INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');

/* Features and permissions for Smart Banking Advisory */

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('SBA_XAI_DASHBOARD' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_XAI_DASHBOARD' ,'en-GB' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_XAI_DASHBOARD' ,'de-DE' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_XAI_DASHBOARD' ,'en-US' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_XAI_DASHBOARD' ,'es-ES' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_XAI_DASHBOARD' ,'fr-FR' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'SBA_XAI_DASHBOARD');


INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('CASHFLOW_PREDICTION_CHART-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW', 'SBA_XAI_DASHBOARD', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'CASHFLOW_PREDICTION_CHART-VIEW', 'SBA XAI Dashboard', 'SBA XAI Dashboard', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-GB', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','de-DE', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-US', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','es-ES', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','fr-FR', 'View XAI Dashboard', 'View XAI Dashboard');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'CASHFLOW_PREDICTION_CHART_VIEW', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CASHFLOW_PREDICTION_CHART_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



/* Features and permissions for Smart Banking Advisory */

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('SBA_BUSINESS_HEALTH_SCORE' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-GB' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'de-DE' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-US' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'es-ES' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'fr-FR' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE');


INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('SBA_BUSINESS_HEALTH_SCORE-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW', 'SBA_BUSINESS_HEALTH_SCORE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_BUSINESS_HEALTH_SCORE-VIEW', 'Smart Banking Business Health Score', 'Smart Banking Business Health Score', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-GB', 'View Business Health Score', 'View Business Health Score');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','de-DE', 'View Business Health Score', 'View Business Health Score');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-US', 'View Business Health Score', 'View Business Health Score');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','es-ES', 'View Business Health Score', 'View Business Health Score');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','fr-FR', 'View Business Health Score', 'View Business Health Score');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


/* Features and permissions for Smart Banking Advisory */


INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('SBA_SIMULATION' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_SIMULATION' ,'en-GB' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_SIMULATION' ,'de-DE' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_SIMULATION' ,'en-US' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_SIMULATION' ,'es-ES' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_SIMULATION' ,'fr-FR' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION');


INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('SBA_SIMULATION-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('SBA_SIMULATION-EDIT',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('SBA_SIMULATION_VIEW', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-VIEW', 'Smart Banking Simultation', 'Smart Banking Simultation', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_VIEW','en-GB', 'View Simulation', 'View Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_VIEW','de-DE', 'View Simulation', 'View Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_VIEW','en-US', 'View Simulation', 'View Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_VIEW','es-ES', 'View Simulation', 'View Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_VIEW','fr-FR', 'View Simulation', 'View Simulation');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('SBA_SIMULATION_EDIT', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-EDIT', 'Smart Banking Simulation', 'Smart Banking Simulation', 0, 0, null, 0, 50, null, 0, 'CREATE', 'CUSTOMERID_LEVEL');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_EDIT','en-GB', 'Perform Simulation', 'Perform Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_EDIT','de-DE', 'Perform Simulation', 'Perform Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_EDIT','en-US', 'Perform Simulation', 'Perform Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_EDIT','es-ES', 'Perform Simulation', 'Perform Simulation');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_SIMULATION_EDIT','fr-FR', 'Perform Simulation', 'Perform Simulation');



INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_VIEW', '0');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_EDIT', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_EDIT', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


/* Features and permissions for Smart Banking Advisory */

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`DisplaySequence` ,`isPrimary` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('SBA_INSIGHTS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` , `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_INSIGHTS' ,'en-GB' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_INSIGHTS' ,'de-DE' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_INSIGHTS' ,'en-US' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_INSIGHTS' ,'es-ES' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription`, `createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('SBA_INSIGHTS' ,'fr-FR' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS');


INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('SBA_INSIGHTS-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`)
VALUES ('SBA_INSIGHTS_VIEW', 'SBA_INSIGHTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_INSIGHTS-VIEW', 'SBA Insights', 'SBA Insights', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_INSIGHTS_VIEW','en-GB', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_INSIGHTS_VIEW','de-DE', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_INSIGHTS_VIEW','en-US', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_INSIGHTS_VIEW','es-ES', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`)
VALUES ('SBA_INSIGHTS_VIEW','fr-FR', 'Smart Banking Insights', 'Smart Banking Insights');


INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`)
VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS_VIEW', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_INSIGHTS_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_LINKAGE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_EDIT_RECEPIENT_OPTIONAL', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_APPROVE', 'UID10', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_AUTHORIZER', 'INTRA_BANK_FUND_TRANSFER_RECEPIENT_SELF_APPROVAL', 'UID10', '0');

UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'RequestCreditCard' ,`operation` = 'applyForCreditCard' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'applyForCreditCard';
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID437','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewViewFundingPosition','Permission to view funding position form in facility overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID438','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewAddFundingPosition','Permission to Add Funding Position form in facility overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID439','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewUpdateFundingPosition','Permission to update funding position form in facility overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID440','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewDeleteFundingPosition','Permission to delete funding position form in facility overview','0','TRUE');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID438', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID439', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailUW', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailOps', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAIL_SYSTEM_ADMIN', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAIL_SYSTEM_ADMIN', 'PID438', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAIL_SYSTEM_ADMIN', 'PID439', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RETAIL_SYSTEM_ADMIN', 'PID440', '0');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RetailRM') and (`Permission_id` = 'PID438');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RETAIL_SYSTEM_ADMIN') and (`Permission_id` = 'PID438');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RETAIL_SYSTEM_ADMIN') and (`Permission_id` = 'PID440');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID439', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailSupervisor', 'PID437', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailSupervisor', 'PID439', '0');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_RISK_SCORECARD' ,'Permission Type Risk Score card');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID441','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','RequestOverviewViewRiskScorecard','Permission to view risk score card in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID442','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','FacilityOverviewViewRiskScorecard','Permission to view risk score card in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID443','PER_TYPE_RISK_SCORECARD','SID_ACTIVE','EntityOverviewViewRiskScorecard','Permission to view risk score card in Entity Overview','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID441');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID441');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID441');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMERMSupervisor', 'PID441', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID441', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID442');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID442');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID442');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMERMSupervisor', 'PID442', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID442', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID443');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID443');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID443');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SMERMSupervisor', 'PID443', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SME_RMSUPERVISOR', 'PID443', '0');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_ADDITIONAL_INSTRUCTION' ,'Permission Type Additional Instruction');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID444','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewViewAdditionalInstruction','Permission to view additional instruction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID445','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewAddAdditionalInstruction','Permission to add additional instruction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID446','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewUpdateAdditionalInstruction','Permission to update additional instruction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID447','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','FacilityOverviewDeleteAdditionalInstruction','Permission to delete additional instruction in Facility Overview','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID444');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID444');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID444');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID444');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID445');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID445');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID445');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID445');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID445');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID445');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID445');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID445');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID446');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID446');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID446');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID446');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID446');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID446');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID446');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID446');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID447');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID447');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID447');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID447');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID447');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID447');

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) values ('IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings.' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('IMPORT_LC_DRAWINGS-VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('IMPORT_LC_DRAWINGS-SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' ,'IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_DRAWINGS-VIEW' ,'View the Import LC Drawings' ,'View the Import LC Drawings and their details' , 1 , 0 , null , 0 , 10 , 'IMPORT_LC_VIEW' , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;
INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' ,'IMPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_DRAWINGS-SUBMIT' ,'Submit an Import LC Drawing' ,'Manage & Submit for approval an Import LC Drawing' , 1 , 0 , null , 0 , 10 , 'IMPORT_LC_DRAWINGS_VIEW' , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_DRAWINGS_VIEW' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_DRAWINGS_SUBMIT' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_DRAWINGS_SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_DRAWINGS_SUBMIT' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('IMPORT_LC_DRAWINGS' ,'en-GB' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('IMPORT_LC_DRAWINGS' ,'en-US' ,'Import LC Drawings' ,'View & Manage the Import LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87zxc-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'getImportLCDrawings' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87bfc-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'getImportLCDrawingById' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87kat-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawingSummary' ,'generate' ,'IMPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87k4d-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'createImportLCDrawing' ,'IMPORT_LC_DRAWINGS_SUBMIT' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a879br-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LCImportDrawing' ,'submitImportLCDrawing' ,'IMPORT_LC_DRAWINGS_SUBMIT' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a870v4-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LetterOfCredit' ,'getLetterOfCreditsById' ,'IMPORT_LC_VIEW' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'IMPORT_LC_DRAWINGS' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'de-DE' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'en-GB' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'en-US' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'es-ES' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_VIEW' , 'fr-FR' , 'Import LC Drawings View' , 'Import LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'de-DE' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'en-GB' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;
INSERT INTO `actiondisplaynamedescription`(`Action_id`, `Locale_id`, `displayName`, `displayDescription`)  VALUES('IMPORT_LC_DRAWINGS_SUBMIT', 'en-US', 'Import LC Drawings Submit', 'Import LC Drawings Submit');
INSERT INTO `actiondisplaynamedescription`(`Action_id`, `Locale_id`, `displayName`, `displayDescription`)  VALUES('IMPORT_LC_DRAWINGS_SUBMIT', 'es-ES', 'Import LC Drawings Submit', 'Import LC Drawings Submit');
INSERT INTO `actiondisplaynamedescription`(`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_DRAWINGS_SUBMIT' , 'fr-FR' , 'Import LC Drawings Submit' , 'Import LC Drawings Submit' ) ;

DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RETAILONBOARDING_RM') and (`Permission_id` = 'PID270');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RETAILONBOARDING_OPS') and (`Permission_id` = 'PID270');

INSERT INTO `rrole` (`id`, `softdeleteflag`) VALUES ('IMPORT_LC-APPROVE', '0');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `status`, `accesspolicyId`, `actionlevelId`, `isApprovalAction`) VALUES ('IMPORT_LC_APPROVE', 'IMPORT_LC', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'IMPORT_LC-APPROVE', 'Approve Import letter of credit', 'The clerk and manager can approve LC', '1', '0', '0', '10', 'SID_ACTION_ACTIVE', 'APPROVE', 'ACCOUNT_LEVEL', '1');

UPDATE `featureaction` SET `approveFeatureAction` = 'IMPORT_LC_APPROVE' WHERE (`id` = 'IMPORT_LC_CREATE');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_BUSINESS', 'IMPORT_LC_APPROVE');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('IMPORT_LC_APPROVE', 'de-DE', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('IMPORT_LC_APPROVE', 'en-GB', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('IMPORT_LC_APPROVE', 'en-US', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('IMPORT_LC_APPROVE', 'es-ES', 'Approve Letter of credit', 'Approve Letter of credit');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('IMPORT_LC_APPROVE', 'fr-FR', 'Approve Letter of credit', 'Approve Letter of credit');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`) VALUES ('16755fc0-6516-11fb-ae93-0242ac130002', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'IMPORT_LC_APPROVE');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`) VALUES ('16756222-6516-11fb-ae93-0242ac130002', '5801fa32-a416-45b6-af01-b22e2de93777', 'IMPORT_LC_APPROVE');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`) VALUES ('5346e16f-a234-41fb-93dc-80d5fd3c673s', 'GROUP_ADMINISTRATOR', 'IMPORT_LC_APPROVE', 'UID10');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`) VALUES ('aac00784-791b-11fb-9300-00090faa1002', 'DEFAULT_GROUP', 'IMPORT_LC_APPROVE', 'UID11');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`) VALUES ('d6704709-0015-45fb-9640-187a51fabbc3', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'IMPORT_LC_APPROVE');

INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`) VALUES ('13611a1c-6517-11fb-ae93-0242ac130002', '7321457251', '1425958', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`) VALUES ('13611c60-6517-11fb-ae93-0242ac130002', '7321457251', '1578660', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`) VALUES ('13611e2c-6517-11fb-ae93-0242ac130002', '4204010299', '1065631', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`) VALUES ('13611efe-6517-11fb-ae93-0242ac130002', '4204010299', '1605506', 'IMPORT_LC', 'IMPORT_LC_APPROVE');
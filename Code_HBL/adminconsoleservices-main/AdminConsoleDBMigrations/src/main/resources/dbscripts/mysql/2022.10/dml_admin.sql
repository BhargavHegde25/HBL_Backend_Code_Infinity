SET FOREIGN_KEY_CHECKS = 0;
INSERT INTO `permissiontype` (`id`, `Description`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PER_TYPE_SERVICEREQUEST','Permission Type Service Request','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID503','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ViewServiceRequest','Permission to view Service Request in dashboard','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID504','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'UpdateServiceRequest','Permission to update Service Request','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SERVICING_OPS', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Servicing Ops', 'This role is used for Servicing Operation User');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID503');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID504');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID505','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewViewAdditionalInstruction','Permission to view additional instruction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID506','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewAddAdditionalInstruction','Permission to add additional instruction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID507','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewUpdateAdditionalInstruction','Permission to update additional instruction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID508','PER_TYPE_ADDITIONAL_INSTRUCTION','SID_ACTIVE','RequestOverviewDeleteAdditionalInstruction','Permission to delete additional instruction in Request Overview','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID505');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID505');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID505');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID505');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID505');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID506');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID506');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID506');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID506');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID506');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID507');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID507');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID507');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID507');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID507');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID508');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID508');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID508');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID508');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID508');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID316', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID316', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_OPERATIONS', 'PID316', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID316', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SECRETARY', 'PID316', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID317', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID317', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_UNDERWRITER', 'PID318', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID318', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_SUPERVISOR', 'PID319', '0');

UPDATE `configurations` SET `config_value` = '78efdef8cb1bc262fba18e5699c4bba2' WHERE (`configuration_id` = '09357a9d-a1a2-4748-a43a-b34b2eba64e9');

INSERT IGNORE INTO `configurations`
(configuration_id, bundle_id, config_type, config_key, description, config_value, target, isPreLoginConfiguration, createdby, modifiedby, createdts, lastmodifiedts, synctimestamp, softdeleteflag, companyLegalUnit)
VALUES('110', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'LEGAL_UNITS', 'Available legal units in the system', '{ "id":"GB0010001", "comapnyName":"IN", "region":"India", "description":"Company entity for India region" }', 'SERVER', 0, 'Kony dev', 'Kony dev', '2022-09-01 12:19:11', '2022-09-01 12:19:11', '2022-09-01 12:19:11', 0, 'ALL');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID509','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewViewFactFind','Permission to View Fact Find section in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID510','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewUpdateFactFind','Permission to Update Fact Find section in Facility Overview','0','TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID509', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID509', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_UW', 'PID509', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_CREDIT_APPROVER', 'PID509', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_OPS', 'PID509', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORGAGE_SUPERVISIOR', 'PID509', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID510', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID510', '0');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `limitgroupId`,`accesspolicyId`, `actionlevelId`, `approveFeatureAction`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','MONETARY','BULK_PAYMENT_SINGLE_SUBMIT','Bulk Payment Request Single Submit','Enables the user to submit the Single Bulk Payment Request',1,0,0,0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','BULK_PAYMENT','BULK_CREATE','ACCOUNT_LEVEL','BULK_PAYMENT_REQUEST_APPROVE');
INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `limitgroupId`,`accesspolicyId`, `actionlevelId`, `approveFeatureAction`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','MONETARY','BULK_PAYMENT_MULTIPLE_SUBMIT','Bulk Payment Request Multiple Submit','Enables the user to submit the Multiple Bulk Payment Request',1,0,0,0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','BULK_PAYMENT','BULK_CREATE','ACCOUNT_LEVEL','BULK_PAYMENT_REQUEST_APPROVE');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_SINGLE_SUBMIT','DAILY_LIMIT', '1000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_SINGLE_SUBMIT','DAILY_LIMIT', '1000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_SINGLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_SINGLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_SINGLE_SUBMIT','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_SINGLE_SUBMIT','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_SINGLE_SUBMIT','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_SINGLE_SUBMIT','WEEKLY_LIMIT', '5000.00','UID11',null,0 );

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','DAILY_LIMIT', '1000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','DAILY_LIMIT', '1000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_MULTIPLE_SUBMIT','WEEKLY_LIMIT', '5000.00','UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_SINGLE_SUBMIT',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_MULTIPLE_SUBMIT',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');


INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT','DAILY_LIMIT', '1000.00');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_SINGLE_SUBMIT','WEEKLY_LIMIT', '5000.00');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT','DAILY_LIMIT', '1000.00');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT','MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (uuid(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_MULTIPLE_SUBMIT','WEEKLY_LIMIT', '5000.00');

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' , 'de-DE' , 'Submit Single Bulk Payment Request' , 'Submit Single Bulk  Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' , 'en-GB' , 'Submit Single Bulk Payment Request' , 'Submit Single Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' , 'en-US' , 'Submit Single Bulk Payment Request' , 'Submit Single Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' , 'es-ES' , 'Submit Single Bulk Payment Request' , 'Submit Single Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT' , 'fr-FR' , 'Submit Single Bulk Payment Request' , 'Submit Single Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' , 'de-DE' , 'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription`(`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' , 'en-GB' , 'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' , 'en-US' , 'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription`(`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' , 'es-ES' , 'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT' , 'fr-FR' , 'Submit Multiple Bulk Payment Request' , 'Submit Multiple Bulk Payment Request' ) ;

INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT', 'DAILY_LIMIT', '1000.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_SINGLE_SUBMIT', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT', 'DAILY_LIMIT', '1000.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`) VALUES ('BULK_PAYMENT_MULTIPLE_SUBMIT', 'WEEKLY_LIMIT', '5000.00');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID400', 'PID45', 'BULK_PAYMENT_MULTIPLE_SUBMIT', 'BULK_PAYMENT_REQUEST', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID401', 'PID45', 'BULK_PAYMENT_SINGLE_SUBMIT', 'BULK_PAYMENT_REQUEST', '0');

INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
UPDATE `featureaction` SET Rrole_id ="BULK_PAYMENT_REQUEST" WHERE Rrole_id = "BULK_PAYMENT_REQUEST_SUBMIT";
UPDATE `service_permission_mapper` SET `permissions`= 'BULK_PAYMENT_SINGLE_SUBMIT,BULK_PAYMENT_MULTIPLE_SUBMIT' WHERE `id` = 'e52a6ef1-7257-4b60-87ad-adb297cc35ab';

DELETE FROM `customeraction` WHERE Action_id = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `contractactionlimit` WHERE actionId = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `groupactionlimit` WHERE Action_id = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `servicedefinitionactionlimit` WHERE actionId = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `actiondisplaynamedescription` WHERE Action_id = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `actionlimit` WHERE Action_id = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE FROM `compositeaction` WHERE Action_id = 'BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from `featureactionroletype` WHERE Action_id='BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from `approvalmatrix` WHERE actionId ='BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from `dependentactions` WHERE actionId ='BULK_PAYMENT_REQUEST_SUBMIT';
DELETE from `dependentactions` WHERE dependentactionId ='BULK_PAYMENT_REQUEST_SUBMIT';

DELETE from `featureaction` WHERE id='BULK_PAYMENT_REQUEST_SUBMIT';

INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID511','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ServiceRequestOverviewGeneral','Permission to view Service Request Overview in Service Request Overview','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID512','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ServiceRequestOverviewServiceRequestOverview','Permission to view Service Request Overview in Service Request Overview','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID513','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ServiceRequestOverviewRequestDetails','Permission to view RequestDetails in Service Request Overview','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID514','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ServiceRequestOverviewDocuments','Permission to view Documents in Service Request Overview','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `DataType_id`, `Name`, `Description`, `PermissionValue`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('PID515','PER_TYPE_SERVICEREQUEST','SID_ACTIVE',NULL,'ServiceRequestOverviewDecisions','Permission to view Decisions in Service Request Overview','TRUE','Kony User','Kony Dev',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID511');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID512');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID513');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID514');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SERVICING_OPS', 'PID515');

UPDATE `configurations`
SET `bundle_id`='DBP_CONFIG_BUNDLE', `config_type`='PREFERENCE', `config_key`='LEGAL_UNITS', `description`='Available legal units in the system', `config_value`='[{"id": "GB0010001","companyName": "Europe","region": "Europe","typeId": "LEGALENTITY","parentId": "GR23698574","countryCode": "EU","baseCurrency": "Euro","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": "LE for Europe"}]', `target`='SERVER', `isPreLoginConfiguration`=0, `createdby`='Kony dev', `modifiedby`='Kony dev', `createdts`='2022-09-01 12:19:11', `lastmodifiedts`='2022-09-01 12:19:11', `synctimestamp`='2022-09-01 12:19:11', `softdeleteflag`=0, `companyLegalUnit`='ALL'
WHERE `configuration_id` = '110';

INSERT IGNORE INTO `configurations`
(`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`)
VALUES('111', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MULTILEGAL_UNITS', 'Available legal units in the system', '[{"id": "GB0010001","companyName": "Europe","region": "Europe","typeId": "LEGALENTITY","parentId": "GR23698574","countryCode": "EU","baseCurrency": "Euro","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": "LE for Europe"},{"id": "LE23698571","companyName": "Singapore","region": "Singapore","typeId": "LEGALENTITY","parentId": "GR23698571","countryCode": "SG","baseCurrency": "SGD","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": ""},{"id": "LE23698573","companyName": "India","region": "India","typeId": "LEGALENTITY","parentId": "GR23698573","countryCode": "IN","baseCurrency": "INR","language": "EN","effectiveDate": "1990-09-20","closeDate": "2045-09-20","description": ""}]', 'SERVER', 0, 'Kony dev', 'Kony dev', '2022-09-01 12:19:11', '2022-09-01 12:19:11', '2022-09-01 12:19:11', 0, 'ALL');

INSERT INTO `userlob` (`userId`, `lobId`, `softdeleteflag`, `companyLegalUnit`) VALUES ('f7c2e82e-cda1-473e-a26d-eb493973f575', 'TYPE_ID_CORPORATE', '0', 'ALL');
INSERT INTO `userlob` (`userId`, `lobId`, `softdeleteflag`, `companyLegalUnit`) VALUES ('aa477169-4bae-46ca-8c98-f5a65323aea6', 'TYPE_ID_CORPORATE', '0', 'ALL');
INSERT INTO `userlob` (`userId`, `lobId`, `softdeleteflag`, `companyLegalUnit`) VALUES ('e15aa8ed-ff87-4a1c-a152-cd21d3659506', 'TYPE_ID_CORPORATE', '0', 'ALL');


INSERT INTO `internalusermanager` (`userId`, `manager`, `softdeleteflag`, `companyLegalUnit`) VALUES ('f7c2e82e-cda1-473e-a26d-eb493973f575', 'bfleck', '0', 'ALL');
INSERT INTO `internalusermanager` (`userId`, `manager`, `softdeleteflag`, `companyLegalUnit`) VALUES ('aa477169-4bae-46ca-8c98-f5a65323aea6', 'bfleck', '0', 'ALL');
INSERT INTO `internalusermanager` (`userId`, `manager`, `softdeleteflag`, `companyLegalUnit`) VALUES ('e15aa8ed-ff87-4a1c-a152-cd21d3659506', 'bfleck', '0', 'ALL');


INSERT INTO `internalusertype` (`userId`, `userType`, `softdeleteflag`, `companyLegalUnit`) VALUES ('f7c2e82e-cda1-473e-a26d-eb493973f575', 'INTERNAL', '0', 'ALL');
INSERT INTO `internalusertype` (`userId`, `userType`, `softdeleteflag`, `companyLegalUnit`) VALUES ('aa477169-4bae-46ca-8c98-f5a65323aea6', 'INTERNAL', '0', 'ALL');
INSERT INTO `internalusertype` (`userId`, `userType`, `softdeleteflag`, `companyLegalUnit`) VALUES ('e15aa8ed-ff87-4a1c-a152-cd21d3659506', 'INTERNAL', '0', 'ALL');


INSERT INTO `useraddress` (`User_id`, `Address_id`,`Type_id`, `softdeleteflag`, `companyLegalUnit`) VALUES ('f7c2e82e-cda1-473e-a26d-eb493973f575', 'DMSADDR1', 'ADR_TYPE_WORK','0', 'ALL');
INSERT INTO `useraddress` (`User_id`, `Address_id`,`Type_id`, `softdeleteflag`, `companyLegalUnit`) VALUES ('aa477169-4bae-46ca-8c98-f5a65323aea6', 'DMSADDR1', 'ADR_TYPE_WORK','0', 'ALL');
INSERT INTO `useraddress` (`User_id`, `Address_id`,`Type_id`, `softdeleteflag`, `companyLegalUnit`) VALUES ('e15aa8ed-ff87-4a1c-a152-cd21d3659506', 'DMSADDR1', 'ADR_TYPE_WORK','0', 'ALL');


/* SQL Scripts for InfinityWealth - Advisory Portfolios - Entitlements and Permissions */

-- Action :: PORTFOLIO_HEALTH_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH-VIEW");

INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','Portfolio Health','Portfolio Health', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '81', '0', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH', 'en-GB', 'Portfolio Health', 'Portfolio Health', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH', 'en-US', 'Portfolio Health', 'Portfolio Health', 'GB0010001');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH', 'GB0010001');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH-VIEW','Portfolio Health View','Portfolio Health View','0','0','0','22', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_VIEW','en-GB', 'Portfolio Health View', 'Portfolio Health View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_VIEW','en-US', 'Portfolio Health View', 'Portfolio Health View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID800', 'PID45', 'PORTFOLIO_HEALTH_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');


-- Action :: PORTFOLIO_HEALTH_SUMMARY_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_SUMMARY-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_SUMMARY-VIEW','Portfolio Health Summary View','Portfolio Health Summary View','0','0','0','23', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','en-GB', 'Portfolio Health Summary View', 'Portfolio Health Summary View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_SUMMARY_VIEW','en-US', 'Portfolio Health Summary View', 'Portfolio Health Summary View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID801', 'PID45', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_SUMMARY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_ASSET_ALLOCATION-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_ASSET_ALLOCATION-VIEW','Portfolio Health Asset Allocation View','Portfolio Health Asset Allocation View','0','0','0','24', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL' , 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','en-GB', 'Portfolio Health Asset Allocation View', 'Portfolio Health Asset Allocation View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW','en-US', 'Portfolio Health Asset Allocation View', 'Portfolio Health Asset Allocation View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID802', 'PID45', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_ASSET_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_RISK_ANALYSIS-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_RISK_ANALYSIS-VIEW','Portfolio Health Risk Analysis View','Portfolio Health Risk Analysis View','0','0','0','25', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','en-GB', 'Portfolio Health Risk Analysis View', 'Portfolio Health Risk Analysis View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW','en-US', 'Portfolio Health Risk Analysis View', 'Portfolio Health Risk Analysis View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID803', 'PID45', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_RISK_ANALYSIS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS-VIEW','Portfolio Health Investment Constraints View','Portfolio Health Investment Constraints View','0','0','0','26', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','en-GB', 'Portfolio Health Investment Constraints View', 'Portfolio Health Investment Constraints View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW','en-US', 'Portfolio Health Investment Constraints View', 'Portfolio Health Investment Constraints View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID804', 'PID45', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_INVESTMENT_CONSTRAINTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS-VIEW','Portfolio Health Recommended Instruments View','Portfolio Health Recommended Instruments View','0','0','0','27', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','en-GB', 'Portfolio Health Recommended Instruments View', 'Portfolio Health Recommended Instruments View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW','en-US', 'Portfolio Health Recommended Instruments View', 'Portfolio Health Recommended Instruments View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID805', 'PID45', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_RECOMMENDED_INSTRUMENTS_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW

INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_HEALTH_CONTACT_ADVISOR-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','PORTFOLIO_HEALTH','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_HEALTH_CONTACT_ADVISOR-VIEW','Portfolio Health Contact Advisor View','Portfolio Health Contact Advisor View','0','0','0','28', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','en-GB', 'Portfolio Health Contact Advisor View', 'Portfolio Health Contact Advisor View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW','en-US', 'Portfolio Health Contact Advisor View', 'Portfolio Health Contact Advisor View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID806', 'PID45', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'PORTFOLIO_HEALTH', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_HEALTH_CONTACT_ADVISOR_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: PORTFOLIO_REVIEW_STRATEGY_VIEW
INSERT INTO `rrole` (`id`) VALUES ("PORTFOLIO_REVIEW_STRATEGY-VIEW");

INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`, `companyLegalUnit`) VALUES ('REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','Review Strategy','Review Strategy', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '82', '0', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('REVIEW_STRATEGY', 'en-GB', 'Review Strategy', 'Review Strategy', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('REVIEW_STRATEGY', 'en-US', 'Review Strategy', 'Review Strategy', 'GB0010001');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'REVIEW_STRATEGY', 'GB0010001');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','PORTFOLIO_REVIEW_STRATEGY-VIEW','Portfolio Review Strategy View','Portfolio Review Strategy View','0','0','0','29', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','en-GB', 'Portfolio Review Strategy View', 'Portfolio Review Strategy View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('PORTFOLIO_REVIEW_STRATEGY_VIEW','en-US', 'Portfolio Review Strategy View', 'Portfolio Review Strategy View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID807', 'PID45', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PORTFOLIO_REVIEW_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: MY_STRATEGY_VIEW
INSERT INTO `rrole` (`id`) VALUES ("MY_STRATEGY-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('MY_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','MY_STRATEGY-VIEW','My Strategy View','My Strategy View','0','0','0','30', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('MY_STRATEGY_VIEW','en-GB', 'My Strategy View', 'My Strategy View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('MY_STRATEGY_VIEW','en-US', 'My Strategy View', 'My Strategy View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'MY_STRATEGY_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID808', 'PID45', 'MY_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'MY_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'MY_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'MY_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'MY_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: MY_STRATEGY_CHANGE_STRATEGY_VIEW
INSERT INTO `rrole` (`id`) VALUES ("MY_STRATEGY_CHANGE_STRATEGY-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','MY_STRATEGY_CHANGE_STRATEGY-VIEW','My Strategy Change Strategy View','My Strategy Change Strategy View','0','0','0','31', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','en-GB', 'My Strategy Change Strategy View', 'My Strategy Change Strategy View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('MY_STRATEGY_CHANGE_STRATEGY_VIEW','en-US', 'My Strategy Change Strategy View', 'My Strategy Change Strategy View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID809', 'PID45', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'MY_STRATEGY_CHANGE_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: RECOMMENDED_STRATEGY_VIEW
INSERT INTO `rrole` (`id`) VALUES ("RECOMMENDED_STRATEGY-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-VIEW','Recommended Strategy View','Recommended Strategy View','0','0','0','32', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_VIEW','en-GB', 'Recommended Strategy View', 'Recommended Strategy View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_VIEW','en-US', 'Recommended Strategy View', 'Recommended Strategy View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID810', 'PID45', 'RECOMMENDED_STRATEGY_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: RECOMMENDED_STRATEGY_USE_VIEW
INSERT INTO `rrole` (`id`) VALUES ("RECOMMENDED_STRATEGY_USE-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY_USE-VIEW','Recommended Strategy Use View','Recommended Strategy Use View','0','0','0','33', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','en-GB', 'Recommended Strategy Use View', 'Recommended Strategy Use View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_USE_VIEW','en-US', 'Recommended Strategy Use View', 'Recommended Strategy Use View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_USE_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID811', 'PID45', 'RECOMMENDED_STRATEGY_USE_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_USE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: SUITABILITY_PROFILE_VIEW
INSERT INTO `rrole` (`id`) VALUES ("SUITABILITY_PROFILE-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SUITABILITY_PROFILE-VIEW','Suitability Profile View','Suitability Profile View','0','0','0','34', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_VIEW','en-GB', 'Suitability Profile View', 'Suitability Profile View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_VIEW','en-US', 'Suitability Profile View', 'Suitability Profile View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'SUITABILITY_PROFILE_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID812', 'PID45', 'SUITABILITY_PROFILE_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SUITABILITY_PROFILE_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: SUITABILITY_PROFILE_REVIEW_VIEW
INSERT INTO `rrole` (`id`) VALUES ("SUITABILITY_PROFILE_REVIEW-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','SUITABILITY_PROFILE_REVIEW-VIEW','Suitability Profile Review View','Suitability Profile Review View','0','0','0','35', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','en-GB', 'Suitability Profile Review View', 'Suitability Profile Review View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('SUITABILITY_PROFILE_REVIEW_VIEW','en-US', 'Suitability Profile Review View', 'Suitability Profile Review View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID813', 'PID45', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SUITABILITY_PROFILE_REVIEW_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: STRATEGY_ALLOCATION_VIEW
INSERT INTO `rrole` (`id`) VALUES ("STRATEGY_ALLOCATION-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('STRATEGY_ALLOCATION_VIEW','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','STRATEGY_ALLOCATION-VIEW','Strategy Allocation View','Strategy Allocation View','0','0','0','36', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('STRATEGY_ALLOCATION_VIEW','en-GB', 'Strategy Allocation View', 'Strategy Allocation View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('STRATEGY_ALLOCATION_VIEW','en-US', 'Strategy Allocation View', 'Strategy Allocation View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'STRATEGY_ALLOCATION_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID814', 'PID45', 'STRATEGY_ALLOCATION_VIEW', 'REVIEW_STRATEGY', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'STRATEGY_ALLOCATION_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

-- Action :: INVESTMENT_PROPOSAL_VIEW
INSERT INTO `rrole` (`id`) VALUES ("INVESTMENT_PROPOSAL-VIEW");

INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','Investment Proposal','Investment Proposal', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '83', '0', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL', 'en-GB', 'Investment Proposal', 'Investment Proposal', 'GB0010001');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL', 'en-US', 'Investment Proposal', 'Investment Proposal', 'GB0010001');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL', 'GB0010001');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL-VIEW','Investment Proposal View','Investment Proposal View','0','0','0','37', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_VIEW','en-GB', 'Investment Proposal View', 'Investment Proposal View', 'GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_VIEW','en-US', 'Investment Proposal View', 'Investment Proposal View', 'GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_VIEW', 'GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `companyLegalUnit`) VALUES ('CAID815', 'PID45', 'INVESTMENT_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001'); 

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0', 'GB0010001'); 

-- Action :: INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW
INSERT INTO `rrole` (`id`) VALUES ("INVESTMENT_PROPOSAL_PAST_PROPOSAL-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL_PAST_PROPOSAL-VIEW','Investment Proposal Past Proposal View','Investment Proposal Past Proposal View','0','0','0','38', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','en-GB', 'Investment Proposal Past Proposal View', 'Investment Proposal Past Proposal View','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','en-US', 'Investment Proposal Past Proposal View', 'Investment Proposal Past Proposal View','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID816', 'PID45', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_PAST_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW
INSERT INTO `rrole` (`id`) VALUES ("INVESTMENT_PROPOSAL_NEW_PROPOSAL-VIEW");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','INVESTMENT_PROPOSAL_NEW_PROPOSAL-VIEW','Investment Proposal New Proposal View','Investment Proposal New Proposal View','0','0','0','39', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','en-GB', 'Investment Proposal New Proposal View', 'Investment Proposal New Proposal View','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','en-US', 'Investment Proposal New Proposal View', 'Investment Proposal New Proposal View','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID817', 'PID45', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'INVESTMENT_PROPOSAL', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_VIEW', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: RECOMMENDED_STRATEGY_CONFIRMATION
INSERT INTO `rrole` (`id`) VALUES ("RECOMMENDED_STRATEGY-CONFIRMATION");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-CONFIRMATION','Recommended Strategy Confirmation','Recommended Strategy Confirmation','0','0','0','40', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','en-GB', 'Recommended Strategy Confirmation', 'Recommended Strategy Confirmation','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_CONFIRMATION','en-US', 'Recommended Strategy Confirmation', 'Recommended Strategy Confirmation','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_CONFIRMATION','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID818', 'PID45', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT
INSERT INTO `rrole` (`id`) VALUES ("RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT','Recommended Strategy Acknowledgement','Recommended Strategy Acknowledgement','0','0','0','41', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','en-GB', 'Recommended Strategy Acknowledgement', 'Recommended Strategy Acknowledgement','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','en-US', 'Recommended Strategy Acknowledgement', 'Recommended Strategy Acknowledgement','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID819', 'PID45', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART
INSERT INTO `rrole` (`id`) VALUES ("RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT_CHART");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','RECOMMENDED_STRATEGY-ACKNOWLEDGEMENT_CHART','Recommended Strategy Acknowledgement Chart','Recommended Strategy Acknowledgement Chart','0','0','0','42', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','en-GB', 'Recommended Strategy Acknowledgement Chart', 'Recommended Strategy Acknowledgement Chart','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','en-US', 'Recommended Strategy Acknowledgement Chart', 'Recommended Strategy Acknowledgement Chart','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID820', 'PID45', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'RECOMMENDED_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: CHANGE_STRATEGY_CONFIRMATION
INSERT INTO `rrole` (`id`) VALUES ("CHANGE_STRATEGY-CONFIRMATION");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('CHANGE_STRATEGY_CONFIRMATION','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHANGE_STRATEGY-CONFIRMATION','Change Strategy Confirmation','Change Strategy Confirmation','0','0','0','43', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHANGE_STRATEGY_CONFIRMATION','en-GB', 'Change Strategy Confirmation', 'Change Strategy Confirmation','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHANGE_STRATEGY_CONFIRMATION','en-US', 'Change Strategy Confirmation', 'Change Strategy Confirmation','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'CHANGE_STRATEGY_CONFIRMATION','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID821', 'PID45', 'CHANGE_STRATEGY_CONFIRMATION', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHANGE_STRATEGY_CONFIRMATION', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: CHOOSE_STRATEGY_ACKNOWLEDGEMENT
INSERT INTO `rrole` (`id`) VALUES ("CHOOSE_STRATEGY-ACKNOWLEDGEMENT"); 

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHOOSE_STRATEGY-ACKNOWLEDGEMENT','Choose Strategy Acknowledgement','Choose Strategy Acknowledgement','0','0','0','44', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','en-GB', 'Choose Strategy Acknowledgement', 'Choose Strategy Acknowledgement','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT','en-US', 'Choose Strategy Acknowledgement', 'Choose Strategy Acknowledgement','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID822', 'PID45', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART
INSERT INTO `rrole` (`id`) VALUES ("CHOOSE_STRATEGY-ACKNOWLEDGEMENT_CHART");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','REVIEW_STRATEGY','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','CHOOSE_STRATEGY-ACKNOWLEDGEMENT_CHART','Choose Strategy Acknowledgement Chart','Choose Strategy Acknowledgement Chart','0','0','0','45', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','en-GB', 'Choose Strategy Acknowledgement Chart', 'Choose Strategy Acknowledgement Chart','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','en-US', 'Choose Strategy Acknowledgement Chart', 'Choose Strategy Acknowledgement Chart','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART','GB0010001');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID823', 'PID45', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'REVIEW_STRATEGY', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'CHOOSE_STRATEGY_ACKNOWLEDGEMENT_CHART', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

-- Action :: INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE
INSERT INTO `rrole` (`id`) VALUES ("INVESTMENT_PROPOSAL_NEW_PROPOSAL-CREATE");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','INVESTMENT_PROPOSAL','RETAIL_AND_BUSINESS_BANKING','MONETARY','INVESTMENT_PROPOSAL_NEW_PROPOSAL-CREATE','Investment Proposal New Proposal Create','Investment Proposal New Proposal Create','0','0','0','46', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','VIEW','CUSTOMERID_LEVEL','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','en-GB', 'Investment Proposal New Proposal Create', 'Investment Proposal New Proposal Create','GB0010001');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`,`companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','en-US', 'Investment Proposal New Proposal Create', 'Investment Proposal New Proposal Create','GB0010001');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`,`companyLegalUnit`) VALUES ('TYPE_ID_WEALTH', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE','GB0010001');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`,`companyLegalUnit`) VALUES ('CAID824', 'PID45', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'INVESTMENT_PROPOSAL', '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '90356097-7fdf-4b8c-89bd-8a1065338a97', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), '4dd6183f-61d2-410f-8a4f-871af67ac933', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`,`companyLegalUnit`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'InfinityWealth', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0','GB0010001');

INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'DAILY_LIMIT', 1000.00, 'GB0010001');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MAX_TRANSACTION_LIMIT', 500.00, 'GB0010001');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'MIN_TRANSACTION_LIMIT', 1.00, 'GB0010001');
INSERT INTO `actionlimit` (`Action_id`, `LimitType_id`, `value`, `companyLegalUnit`) VALUES ('INVESTMENT_PROPOSAL_NEW_PROPOSAL_CREATE', 'WEEKLY_LIMIT', 5000.00, 'GB0010001');


DELETE FROM `rolepermission` WHERE Permission_id in ( 'PID72','PID71') ;

INSERT INTO `externalalertsytem`(systemtype, systemid) VALUES ('Wealth FO', '3');

INSERT INTO `eventsubtype`
(id, eventtypeid, Name, softdeleteflag, externalSystem)
VALUES('RISK_PROFILE_EXPIRY', 'TRANSFER_RECIPIENT', 'Risk Profile Expiry', '0', '3');

INSERT INTO `eventsubtype`
(id, eventtypeid, Name, softdeleteflag, externalSystem)
VALUES('INVESTMENT_PROPOSAL', 'TRANSFER_RECIPIENT', 'Investment Proposal', '0', '3');
SET FOREIGN_KEY_CHECKS = 1;
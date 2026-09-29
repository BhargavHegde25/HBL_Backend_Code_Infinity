
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EXPORT_LC-CREATE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('EXPORT_LC-DELETE',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EXPORT_LC_CREATE','EXPORT_LC','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EXPORT_LC-CREATE','Create Export LC','The clerk, Manager & soletraders can create the export LC',1,0,null,0,10,null,0,'CREATE','ACCOUNT_LEVEL');

INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) 
VALUES ('EXPORT_LC_DELETE','EXPORT_LC','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','EXPORT_LC-DELETE','Delete Export LC','The clerk, Manager & soletraders can delete the export LC',1,0,null,0,10,null,0,'DELETE','ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','EXPORT_LC_CREATE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','EXPORT_LC_CREATE',null,null,'UID11',null,0 );

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','EXPORT_LC_DELETE',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','EXPORT_LC_DELETE',null,null,'UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EXPORT_LC_CREATE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) 
VALUES ('TYPE_ID_BUSINESS','EXPORT_LC_DELETE',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'EXPORT_LC_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'EXPORT_LC_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'EXPORT_LC_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) 
VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'EXPORT_LC_DELETE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7a87k62-4125-11eb-973n-0242kc130003','TradeFinance','ExportLetterOfCredit','createExportLetterOfCredit','EXPORT_LC_CREATE' );

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7t87b62-4125-11yc-973a-0242lc130003','TradeFinance','ExportLetterOfCredit','deleteExportLetterOfCredit','EXPORT_LC_DELETE' );

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`) 
VALUES ('e7v87b62-4125-11yc-973a-0242lf130003','TradeFinance','ExportLetterOfCredit','getExportLetterOfCreditsById','EXPORT_LC_VIEW' );

UPDATE `service_permission_mapper` SET `object_name` = 'ExportLetterOfCredit', `operation` = 'getExportLetterOfCredits' WHERE (`id` = 'e7a87c62-4125-11ec-973a-0242ac130003');

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_CREATE' , 'de-DE' , 'Export LC Create' , 'Export LC Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_CREATE' , 'en-GB' , 'Export LC Create' , 'Export LC Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_CREATE' , 'en-US' , 'Export LC Create' , 'Export LC Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_CREATE' , 'es-ES' , 'Export LC Create' , 'Export LC Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_CREATE' , 'fr-FR' , 'Export LC Create' , 'Export LC Create' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DELETE' , 'de-DE' , 'Export LC Delete' , 'Export LC Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DELETE' , 'en-GB' , 'Export LC Delete' , 'Export LC Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DELETE' , 'en-US' , 'Export LC Delete' , 'Export LC Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DELETE' , 'es-ES' , 'Export LC Delete' , 'Export LC Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DELETE' , 'fr-FR' , 'Export LC Delete' , 'Export LC Delete' ) ;

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('EXPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Export LC Drawings' ,'View & Manage the Export LC Drawings.' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS-CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS-VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS-UPDATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS-DELETE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` )
VALUES ('EXPORT_LC_DRAWINGS_CREATE' ,'EXPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EXPORT_LC_DRAWINGS-CREATE' ,'Create an Export LC Drawing' ,'create the Export LC Drawing' , 1 , 0 , null , 0 , 10 , null , 0 ,'CREATE' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` )
VALUES ('EXPORT_LC_DRAWINGS_VIEW' ,'EXPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EXPORT_LC_DRAWINGS-VIEW' ,'View the Export LC Drawings' ,'View the Export LC Drawings and their details' , 1 , 0 , null , 0 , 10 , null , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` )
VALUES ('EXPORT_LC_DRAWINGS_UPDATE' ,'EXPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EXPORT_LC_DRAWINGS-UPDATE' ,'Update an Export LC Drawing' ,'Update the Export LC Drawing' , 1 , 0 , null , 0 , 10 , null , 0 ,'CREATE' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` )
VALUES ('EXPORT_LC_DRAWINGS_DELETE' ,'EXPORT_LC_DRAWINGS' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'EXPORT_LC_DRAWINGS-DELETE' ,'Delete an Export LC Drawing' ,'Delete the Export LC Drawing' , 1 , 0 , null , 0 , 10 , null , 0 ,'CREATE' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'EXPORT_LC_DRAWINGS_CREATE' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'EXPORT_LC_DRAWINGS_CREATE' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'EXPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'EXPORT_LC_DRAWINGS_VIEW' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'EXPORT_LC_DRAWINGS_UPDATE' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'EXPORT_LC_DRAWINGS_UPDATE' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'EXPORT_LC_DRAWINGS_DELETE' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'EXPORT_LC_DRAWINGS_DELETE' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'EXPORT_LC_DRAWINGS_CREATE' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'EXPORT_LC_DRAWINGS_VIEW' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'EXPORT_LC_DRAWINGS_UPDATE' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'EXPORT_LC_DRAWINGS_DELETE' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;


INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'EXPORT_LC_DRAWINGS_CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'EXPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'EXPORT_LC_DRAWINGS_UPDATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'EXPORT_LC_DRAWINGS_DELETE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'EXPORT_LC_DRAWINGS_CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'EXPORT_LC_DRAWINGS_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'EXPORT_LC_DRAWINGS_UPDATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'EXPORT_LC_DRAWINGS_DELETE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS' ,'en-GB' ,'Export LC Drawings' ,'View & Manage the Export LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('EXPORT_LC_DRAWINGS' ,'en-US' ,'Export LC Drawings' ,'View & Manage the Export LC Drawings' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'EXPORT_LC_DRAWINGS' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_CREATE' , 'de-DE' , 'Export LC Drawings Create' , 'Export LC Drawings Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_CREATE' , 'en-GB' , 'Export LC Drawings Create' , 'Export LC Drawings Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_CREATE' , 'en-US' , 'Export LC Drawings Create' , 'Export LC Drawings Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_CREATE' , 'es-ES' , 'Export LC Drawings Create' , 'Export LC Drawings Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_CREATE' , 'fr-FR' , 'Export LC Drawings Create' , 'Export LC Drawings Create' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_VIEW' , 'de-DE' , 'Export LC Drawings View' , 'Export LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_VIEW' , 'en-GB' , 'Export LC Drawings View' , 'Export LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_VIEW' , 'en-US' , 'Export LC Drawings View' , 'Export LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_VIEW' , 'es-ES' , 'Export LC Drawings View' , 'Export LC Drawings View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_VIEW' , 'fr-FR' , 'Export LC Drawings View' , 'Export LC Drawings View' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_UPDATE' , 'de-DE' , 'Export LC Drawings Update' , 'Export LC Drawings Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_UPDATE' , 'en-GB' , 'Export LC Drawings Update' , 'Export LC Drawings Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_UPDATE' , 'en-US' , 'Export LC Drawings Update' , 'Export LC Drawings Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_UPDATE' , 'es-ES' , 'Export LC Drawings Update' , 'Export LC Drawings Update' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_UPDATE' , 'fr-FR' , 'Export LC Drawings Update' , 'Export LC Drawings Update' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_DELETE' , 'de-DE' , 'Export LC Drawings Delete' , 'Export LC Drawings Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_DELETE' , 'en-GB' , 'Export LC Drawings Delete' , 'Export LC Drawings Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_DELETE' , 'en-US' , 'Export LC Drawings Delete' , 'Export LC Drawings Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_DELETE' , 'es-ES' , 'Export LC Drawings Delete' , 'Export LC Drawings Delete' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('EXPORT_LC_DRAWINGS_DELETE' , 'fr-FR' , 'Export LC Drawings Delete' , 'Export LC Drawings Delete' ) ;

INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7e87fhc-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'ExportLetterOfCredit' ,'getExportLetterOfCreditDrawings' ,'EXPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7n87ion-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'ExportLetterOfCredit' ,'getExportLetterOfCreditDrawingById' ,'EXPORT_LC_DRAWINGS_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87kia-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'ExportLetterOfCredit' ,'createExportLetterOfCreditDrawing' ,'EXPORT_LC_DRAWINGS_CREATE' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87grs-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'ExportLetterOfCredit' ,'updateExportLetterOfCreditDrawing' ,'EXPORT_LC_DRAWINGS_UPDATE' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87wgt-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'ExportLetterOfCredit' ,'deleteExportLetterOfCreditDrawing' ,'EXPORT_LC_DRAWINGS_DELETE' ) ;

INSERT INTO `feature`(`id`,`App_id`,`name`,`description`,`Type_id`,`Status_id`,`Service_Fee` ,`DisplaySequence` ,`isPrimary` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` )
values ('IMPORT_LC_AMENDMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'Import LC Amendment' ,'View & Manage the Import LC Amendment.' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , null , null , 0 , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('IMPORT_LC_AMENDMENT-VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;
INSERT INTO `rrole` (`id` ,`createdts` ,`lastmodifiedts` ,`softdeleteflag` ) VALUES ('IMPORT_LC_AMENDMENT-CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , 0 ) ;

INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' ,'IMPORT_LC_AMENDMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_AMENDMENT-VIEW' ,'View the Import LC Amendment' ,'View the Import LC Amendment and their details' , 1 , 0 , null , 0 , 10 , null , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;
INSERT INTO `featureaction` (`id` ,`Feature_id` ,`App_id` ,`Type_id` ,`Rrole_id` ,`name` ,`description` ,`isAccountLevel` ,`isMFAApplicable` ,`MFA_id` ,`isPrimary` ,`DisplaySequence` ,`dependency` ,`softdeleteflag` ,`accesspolicyId` ,`actionlevelId` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' ,'IMPORT_LC_AMENDMENT' ,'RETAIL_AND_BUSINESS_BANKING' ,'NON_MONETARY' ,'IMPORT_LC_AMENDMENT-CREATE' ,'Create an Import LC Amendment' ,'Create the Import LC Amendment' , 1 , 0 , null , 0 , 10 , null , 0 ,'VIEW' ,'ACCOUNT_LEVEL' ) ;

INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_AMENDMENT_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'IMPORT_LC_AMENDMENT_VIEW' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_ADMINISTRATOR' ,'IMPORT_LC_AMENDMENT_CREATE' , null , null ,'UID11' , null , 0 ) ;
INSERT INTO `groupactionlimit` (`id` ,`Group_id` ,`Action_id` ,`LimitType_id` ,`value` ,`createdby` ,`modifiedby` ,`softdeleteflag` ) VALUES (uuid() ,'GROUP_CREATOR' ,'IMPORT_LC_AMENDMENT_CREATE' , null , null ,'UID11' , null , 0 ) ;

INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_AMENDMENT_VIEW' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featureactionroletype` (`RoleType_id` ,`Action_id` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('TYPE_ID_BUSINESS' ,'IMPORT_LC_AMENDMENT_CREATE' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_AMENDMENT_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a' , 'IMPORT_LC_AMENDMENT_CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_AMENDMENT_VIEW' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;
INSERT INTO `servicedefinitionactionlimit` (`id` , `serviceDefinitionId` , `actionId` , `createdts` , `lastmodifiedts` , `synctimestamp` , `softdeleteflag` ) VALUES (uuid() , '83c9b8d7-3715-480e-8c7d-3d6e61c00035' , 'IMPORT_LC_AMENDMENT_CREATE' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' ) ;

INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('IMPORT_LC_AMENDMENT' ,'en-GB' ,'Import LC Amendments' ,'View & Manage the Import LC Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;
INSERT INTO `featuredisplaynamedescription` (`Feature_id` ,`Locale_id` ,`displayName` ,`displayDescription` ,`createdby` ,`modifiedby` ,`createdts` ,`lastmodifiedts` ,`synctimestamp` ,`softdeleteflag` ) VALUES ('IMPORT_LC_AMENDMENT' ,'en-US' ,'Import LC Amendments' ,'View & Manage the Import LC Amendments' , null , null , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0' ) ;

INSERT INTO `featureroletype` (`RoleType_id` , `Feature_id` ) VALUES ('TYPE_ID_BUSINESS' , 'IMPORT_LC_AMENDMENT' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' , 'de-DE' , 'Import LC Amendments Create' , 'Import LC Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' , 'en-GB' , 'Import LC Amendments Create' , 'Import LC Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' , 'en-US' , 'Import LC Amendments Create' , 'Import LC Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' , 'es-ES' , 'Import LC Amendments Create' , 'Import LC Amendments Create' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_CREATE' , 'fr-FR' , 'Import LC Amendments Create' , 'Import LC Amendments Create' ) ;

INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' , 'de-DE' , 'Import LC Amendments View' , 'Import LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' , 'en-GB' , 'Import LC Amendments View' , 'Import LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' , 'en-US' , 'Import LC Amendments View' , 'Import LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' , 'es-ES' , 'Import LC Amendments View' , 'Import LC Amendments View' ) ;
INSERT INTO `actiondisplaynamedescription` (`Action_id` , `Locale_id` , `displayName` , `displayDescription` ) VALUES ('IMPORT_LC_AMENDMENT_VIEW' , 'fr-FR' , 'Import LC Amendments View' , 'Import LC Amendments View' ) ;

INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7e87nht-4125-11ec-973a-0242ac130003' ,'TradeFinance' ,'LetterOfCredit' ,'getImportLetterOfCreditAmendments' ,'IMPORT_LC_AMENDMENT_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7n87ion-4125-11ec-973a-0242dk130003' ,'TradeFinance' ,'LetterOfCredit' ,'getImportLetterOfCreditAmendmentsById' ,'IMPORT_LC_AMENDMENT_VIEW' ) ;
INSERT INTO `service_permission_mapper` (`id` ,`service_name` ,`object_name` ,`operation` ,`permissions` ) VALUES ('e7a87efb-4125-11ec-973a-0242ef130003' ,'TradeFinance' ,'LetterOfCredit' ,'createImportLetterOfCreditAmendment' ,'IMPORT_LC_AMENDMENT_CREATE' ) ;

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('PAY_202', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'PAYMENT_METHOD', 'Payment Method', '{\"EU\":[{\"currency\":\"default\",\"payMethod\":[\"SWIFT\"]},{\"currency\":\"EUR\",\"payMethod\":[\"SEPA\",\"INSTANT\",\"SWIFT\"]}],\"UK\":[{\"currency\":\"default\",\"payMethod\":[\"SWIFT\"]},{\"currency\":\"GBP\",\"payMethod\":[\"Faster\",\"CHAPS\"]},{\"currency\":\"EUR\",\"payMethod\":[\"SEPA\",\"INSTANT\",\"SWIFT\"]}],\"US\":[{\"currency\":\"default\",\"payMethod\":[\"SWIFT\"]},{\"currency\":\"USD\",\"payMethod\":[\"ACH\",\"FEDWIRE\"]}],\"AU\":[{\"currency\":\"default\",\"payMethod\":[\"SWIFT\"]},{\"currency\":\"AUD\",\"payMethod\":[\"BECS\",\"NPP\"]}],\"IN\":[{\"currency\":\"default\",\"payMethod\":[\"SWIFT\"]},{\"currency\":\"INR\",\"payMethod\":[\"NEFT\",\"RTGS\",\"IMPS\"]}]}', 'CLIENT', '1');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('PAY_203', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'PAYMENT_REGION', 'Payment Region', 'EU', 'CLIENT', '1');

UPDATE `transactiontypemapping` SET `dbxTransactionType` = 'Transfers' WHERE (`backendTransactionTypeId` = '5027');
UPDATE `transactiontypemapping` SET `dbxTransactionType` = 'Transfers' WHERE (`backendTransactionTypeId` = '5028');

UPDATE `systemconfiguration` SET `PropertyValue` = 'a85VsWqDDKzBG8fNjwiQKmBKgm4Y7sd4PQLUCFEPGe7Havjj' WHERE (`PropertyName` = 'API_ACCESS_TOKEN');
UPDATE `systemconfiguration` SET `PropertyValue` = 'S8gr1x70FmvgK_PGfJx3odePvhwDL2Q0dpMIciIU9VYg8GBv' WHERE (`PropertyName` = 'DBP_API_ACCESS_TOKEN');

UPDATE `backendcertificate` SET `CertPrivateKey` = '4JHbZihhNG7tb15MCEtsHsJKHojH9pdAah_e-pDqGH_A0oFHKiQfYF89ED91_dYT7QPOU4tStULWJBdefPQWEvG-ZIH1xUEQ_-PjEtaVC_0XwhOdg7j2HisgT7Lty6XiTG_p3Q6R5ai1kgfVAcfl8WfHVRia5G7mERcEjX-hyVcXp5IUtCYpKEvAH37yPdAysp5Of5J3v8mejuLjCYSD_dbICubAfeygXiTsy3UTAl4OXWstf21NwT0Hd9jlwBIW46DD2cbtsXBsVKZ-E9iFH9tEBekGOATkXp2SJYcjruOx-w8Zyyoer73M7hTLBq6afJrVwXfHVJgs2N8T_dn2n4z8mAz09CqnzDdr7pJpSzJ8lyD5kc589aqLNo4IzJ9Tp8-Lm97jvoGAg4og-v_M_40-vPs0CL9_KFeCQZw8KJf6Yk7H_eZO1xeJR3fBGDmdDunboll3umyLKk_oMpoN7zOLcfRi75zQQ-4RGwoHEQXgqpNR3SQBlFJqjjz9pb713_2Hk3Xrdtxo6Awf4_2dZ8jSMZyudfwqLDf26MwQl_L6uAgIFMOfPcPXv5VRuMYa4aRw717lYfbKlZdmrAICcCppNqctCmfHdNU9JrXQrqRsLITimlvz5IxIUR9VcDtGVV_UNfz1etlwJ53v5OWW9Zg1yMR-SHfG4_yT_PJTcaQTmiWSIWyCExHUtKWAOSiWt2X8pVSTeH887O2FQpUbqQplP9Bb8tJN5XmbJ0T3Uu1jaGZK1E6ep-UT525VQ-zJso9rlpQEzdvY8SUWUNnfiqcEoGg3dFqhKnmbJXa47liMWird-4CLNCk6EauTAkIy0qMThPClHEpljsMJUX8X2r3pE6g0YNtr_9dY2wpUGpHF4hSt0bsS6TgPWMLTQPNlZl2niZtY7znm1yMiOELJOTPzXGODnEXBqfgnvvcUEuX8lwdmGxUJbfF5V9gq3AkosdkyVaPQUZM1wslBqcaDDux5N5YV7GJMMjxu1fXnY7-qaZBo1PW-jIgVKGarzl1gSqdW2fv7IXFkP-GwwMulY6CA1snV1je8FanzrS8W0ac2IL-3Ti0ivag9CdtCj-WC64g3CbDjjuRbU7EkI9ZWl32HWR-M_yWkmIC71wJO0xb8dmAfgEdX74FlToTzedtBeOsb5S3ji-lS1kHD3GnDL7TaoF_I3tKcUTxjPXSQeX8MpU2RoJ-nd3PQ_QzNXqyQS_RNgE-LKztvNd9CZWO42_avUfyUt8QaWjqPLeQm7G2DvF3xYCADDfXxHrieuPy991MkBgPuXSezEN8omtL89QjzdZC7x_RwX7FMDwmcWkr24wJbWRH5q5efn9q-0CCsglTd0jks_WgzR0P_D2qfcW3wHND2KCoZjseeTq0ZVE6gmYjAcByrC3uGtd_Yu-URthNuCEMnB7BfhV6vwFQLGyMVUbo8rZKUGg4rH5gXyQsRz5F9-rB5_6ogY12lvR66IzMGROvR6Jo7Y19BUdLM4YGybpXQ8rF2XEPSCuY5NaHPsw1LBvd2qlQHnW8kVtIsfipEJzNikuqEY8CCYHWwbGz2vYkfTHoDvXolaluEwZxUxFS3PVq5Hp7rP1HQNFMUG1L7ucwDv4o-A2Lba3nOa_J4ZgZ8y2CFWVReIlRQSITtvnyEYYDcLCzgfuShc57ZniNw3PgybSRjiQt5LaCH2B7zzZhfxsrOD4jdDtNjHfRbLv8rE-zCJ0vty7E9Z0I9wuTohLcNcp47tTQiS9BqSLH4lFGF488lGUmtrbprJghq0jy5LhWCoEgICGAP09c0KtCnbpSftg4VCv35PKVULwqhRqJ74L-NU0xZQY9VWzzNE160RTcaXHkN1pKm6b_pKb8wUYqbtfFGmGF1lfkVfHsp4nhV4SpqGXodPfeTMc3jYx_Zq_s38uMiWxFgbEzxmjSYYr7JxajiI5BmxsAopXoX9uoOpN5dj3bZA1qJbu4rQarI5xeG9d_XlprusrqvbrqtjIF5bHzoYaKi1KOBB1-8EY-klldrr1wBYgKreLM6msV_FHrQCmO_co95sJdB4oQiWrW-IXI3IQhX-UbkWMqTXBNTUVSzQ6NAIMsaapytkHhBvRhfEA0TMoGSdJgi1SOCVJchpNwvDopveERLB5VZO018XTAUd2rUxUI_wcyVW12tkIl78yWSSv0dy-2bCh6h86hCuRVVV5JZrh7R8mwOOT4QjWIlRnFyvfk60JmRzG1BPZ6EFoyFZI3JpFimzkvTX8HmYvJFIuZZjOHikg' WHERE `BackendName` = 'T24' and `id` = '1';

UPDATE `systemconfiguration` SET `PropertyValue` = 'a7JZnnLvPoq8E-nb1AO9IXoHhj8jPXRE0l5BCWIo4bEnRqsC' WHERE (`PropertyName` = 'ORIGINATION_API_ACCESS_TOKEN');

UPDATE `service_permission_mapper` SET `service_name` = 'ExternalUserManagement',`object_name` = 'Permission' ,`operation` = 'verifyAccountPermissions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'verifyAccountPermissions';

UPDATE `dbxalerttype` SET `DisplaySequence`='2' where `id`='REQUEST_AND_APPROVAL_ALERTS'; 
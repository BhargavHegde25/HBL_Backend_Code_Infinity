INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK','Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request',0,0,null,0,9,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC','Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request',0,0,null,0,10,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL','BULK_PAYMENT_REQUEST','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL','Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request',0,0,null,0,11,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');



INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL',null,null,'UID11',null,0 );



INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');


INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'de-DE', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'en-GB', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'en-US', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'es-ES', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'fr-FR', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'de-DE', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'en-GB', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'en-US', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'es-ES', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'fr-FR', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'de-DE', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'en-GB', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'en-US', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'es-ES', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'fr-FR', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk request', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);
INSERT INTO `rrole` (`id`,`createdts`,`lastmodifiedts`,`softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);


INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK','BULK_PAYMENT_TEMPLATE','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK','Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template',0,0,null,0,7,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC','BULK_PAYMENT_TEMPLATE','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC','Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template',0,0,null,0,8,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');
INSERT INTO `featureaction`(`id`,`Feature_id`,`App_id`,`Type_id`,`Rrole_id`,`name`,`description`,`isAccountLevel`,`isMFAApplicable`,`MFA_id`,`isPrimary`,`DisplaySequence`,`dependency`,`softdeleteflag`,`accesspolicyId`,`actionlevelId`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL','BULK_PAYMENT_TEMPLATE','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL','Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template',0,0,null,0,9,null,0,'BULK_CREATE','CUSTOMERID_LEVEL');

INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_ADMINISTRATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL',null,null,'UID11',null,0 );
INSERT INTO `groupactionlimit`(`id`,`Group_id`,`Action_id`,`LimitType_id`,`value`,`createdby`,`modifiedby`,`softdeleteflag`) VALUES(uuid(),'GROUP_CREATOR','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL',null,null,'UID11',null,0 );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');
INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');


INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'de-DE', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'en-GB', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'en-US', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'es-ES', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'fr-FR', 'Add New Same Bank Payment order','Enable the user to add a Same Bank Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'de-DE', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'en-GB', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'en-US', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'es-ES', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'fr-FR', 'Add New External Bank -Domestic Payment order','Enable the user to add a External Bank -Domestic Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'de-DE', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'en-GB', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'en-US', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'es-ES', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'fr-FR', 'Add New External Bank -International Payment order','Enable the user to add a External Bank -International Payment order to bulk template', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



UPDATE `service_permission_mapper` SET `permissions`= 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT,INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT,TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW_RECEPIENT,INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT,BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK,BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC,BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL,BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK,BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC,BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL' WHERE `permissions` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT,INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT,TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW_RECEPIENT,INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and `service_name` = 'PayeeObjects'; 


INSERT IGNORE INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`, `createdts`) VALUES ('RID_TPP', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'TPP Access', 'This Role is only for TPP who want to view specific customer details. This Role has minimal permission.', 'Kony User', '2021-05-25 19:30:00');

INSERT IGNORE INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `modifiedby`, `createdts`) VALUES ('RID_TPP', 'PID09', 'Kony User', 'Kony Dev', '2021-05-25 19:30:00');

INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a');
INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '83c9b8d7-3715-480e-8c7d-3d6e61c00035');
INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '5801fa32-a416-45b6-af01-b22e2de93777');
INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', 'bef2fe82-9c21-4ccb-b599-3308de18de44');
INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', '90356097-7fdf-4b8c-89bd-8a1065338a97');
INSERT IGNORE INTO `userroleservicedefinition` (`UserRole_id`, `servicedefinitionId`) VALUES ('RID_TPP', 'f85d8392-9afe-4128-b23e-a370f138784f');

INSERT IGNORE INTO `systemuser` (`id`, `Status_id`, `Username`, `Password`, `Email`, `FirstName`, `LastName`, `FailedCount`, `createdby`, `modifiedby`) VALUES ('UID_TPP', 'SID_ACTIVE', 'admintpp', '$2a$11$IlgBrTIqqCdkWbSXW7/xvO9gtGqrnBOZrRq1WsDuaPyE9SFvTbdkW', 'c360admin@kony.com', 'Spotlight Administrator for TPP', 'TPP', '0', 'konydev', 'konydev');
INSERT IGNORE INTO `userrole` (`User_id`, `Role_id`, `hasSuperAdminPrivilages`, `createdby`, `modifiedby`) VALUES ('UID_TPP', 'RID_TPP', '0', 'konydev', 'konydev');


INSERT INTO `service_permission_mapper`(`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES('672aabee-c1d9-11eb-8529-0242ac130003', 'ApprovalRequestObjects', 'MyRequests', 'RenotifyPendingApprovalRequest', 'ALLOW');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_REQUEST_OVERVIEW' ,'Permission Type Request Overview');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_FACILITY' ,'Permission Type Facility');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_COLLATERAL' ,'Permission Type Collateral');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_FINANCIAL_RATIO_RESULTS' ,'Permission Type Financial Ratio Results');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_DOCUMENT' ,'Permission Type Document');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_NARRATIVE' ,'Permission Type Narrative');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_PARTY' ,'Permission Type Party');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_LIMIT' ,'Permission Type Limit');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_BORROWER_FEE' ,'Permission Type Borrower Fee');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_RISK_RATING' ,'Permission Type Risk Rating');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_DECISION' ,'Permission Type Decision');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_CONDITION' ,'Permission Type Condition');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_COVENANTS' ,'Permission Type Covenants');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_POLICY_EXCEPTIONS' ,'Permission Type Policy Exceptions');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_COMPLIANCES' ,'Permission Type Compliances');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_INTEGRATION' ,'Permission Type Integration');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_CHECKLIST' ,'Permission Type Checklist');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_WITHDRAW' ,'Permission Type Withdraw');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_CREATE_APPLICATION' ,'Permission Type Create Applicaion');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_DRAW_RESTRICTION' ,'Permission Type Draw Restriction');

INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_FUNDING' ,'Permission Type Funding');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_PRICING' ,'Permission Type Pricing');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_ENTITY_OVERVIEW' ,'Permission Type Entity Overview');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_ADDRESS' ,'Permission Type Address');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_CONTACT' ,'Permission Type Contact');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_CREDIT_HISTORY' ,'Permission Type Credit History');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_KYC' ,'Permission Type Kyc');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_ACCOUNT' ,'Permission Type Account');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_HISTORY' ,'Permission Type History');
INSERT INTO `permissiontype` (`id`, `Description`) VALUES ('PER_TYPE_EXPOSURE_HISTORY' ,'Permission Type Exposure History');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID131','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','RequestOverviewViewRequestOverview','Permission to view request overview section in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID132','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','RequestOverviewUpdateRequestOverview','Permission to update request overview section in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID228','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','FacilityOverviewViewRequestOverview','Permission to view request overview section in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID229','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','FacilityOverviewUpdateRequestOverview','Permission to update request overview section in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID230','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','EntityOverviewViewRequestOverview','Permission to view  request overview section in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID231','PER_TYPE_REQUEST_OVERVIEW','SID_ACTIVE','EntityOverviewUpdateRequestOverview','Permission to update request overview section in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID133','PER_TYPE_FACILITY','SID_ACTIVE','RequestOverviewViewFacility','Permission to view facility in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID134','PER_TYPE_FACILITY','SID_ACTIVE','RequestOverviewUpdateFacility','Permission to update facility in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID135','PER_TYPE_FACILITY','SID_ACTIVE','RequestOverviewDeleteFacility','Permission to delete facility in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID232','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewViewFacility','Permission to view facility in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID233','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewUpdateFacility','Permission to update facility in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID234','PER_TYPE_FACILITY','SID_ACTIVE','FacilityOverviewDeleteFacility','Permission to delete facility in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID235','PER_TYPE_FACILITY','SID_ACTIVE','EntityOverviewViewFacility','Permission to view facility in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID236','PER_TYPE_FACILITY','SID_ACTIVE','EntityOverviewUpdateFacility','Permission to update facility in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID237','PER_TYPE_FACILITY','SID_ACTIVE','EntityOverviewDeleteFacility','Permission to delete facility in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID136','PER_TYPE_COLLATERAL','SID_ACTIVE','RequestOverviewViewCollateral','Permission to view collateral in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID137','PER_TYPE_COLLATERAL','SID_ACTIVE','RequestOverviewAddCollateral','Permission to add collateral in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID138','PER_TYPE_COLLATERAL','SID_ACTIVE','RequestOverviewUpdateCollateral','Permission to update collateral in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID139','PER_TYPE_COLLATERAL','SID_ACTIVE','RequestOverviewDeleteCollateral','Permission to delete collateral in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID238','PER_TYPE_COLLATERAL','SID_ACTIVE','FacilityOverviewViewCollateral','Permission to view collateral in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID239','PER_TYPE_COLLATERAL','SID_ACTIVE','FacilityOverviewAddCollateral','Permission to add collateral in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID240','PER_TYPE_COLLATERAL','SID_ACTIVE','FacilityOverviewUpdateCollateral','Permission to update collateral in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID241','PER_TYPE_COLLATERAL','SID_ACTIVE','FacilityOverviewDeleteCollateral','Permission to delete collateral in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID242','PER_TYPE_COLLATERAL','SID_ACTIVE','EntityOverviewViewCollateral','Permission to view collateral in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID243','PER_TYPE_COLLATERAL','SID_ACTIVE','EntityOverviewAddCollateral','Permission to add collateral in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID244','PER_TYPE_COLLATERAL','SID_ACTIVE','EntityOverviewUpdateCollateral','Permission to update collateral in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID245','PER_TYPE_COLLATERAL','SID_ACTIVE','EntityOverviewDeleteCollateral','Permission to delete collateral in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID140','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','RequestOverviewViewRatio','Permission to view ratio in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID141','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','RequestOverviewCompareRatio','Permission to compare ratio in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID246','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','FacilityOverviewViewRatio','Permission to view ratio in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID247','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','FacilityOverviewCompareRatio','Permission to compare ratio in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID248','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','EntityOverviewViewRatio','Permission to view ratio in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID249','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','EntityOverviewCompareRatio','Permission to compare ratio in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID142','PER_TYPE_DOCUMENT','SID_ACTIVE','RequestOverviewViewDocument','Permission to view document in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID143','PER_TYPE_DOCUMENT','SID_ACTIVE','RequestOverviewUploadDocument','Permission to upload document in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID144','PER_TYPE_DOCUMENT','SID_ACTIVE','RequestOverviewUpdateDocument','Permission to update document in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID145','PER_TYPE_DOCUMENT','SID_ACTIVE','RequestOverviewDeleteDocument','Permission to delete document in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID250','PER_TYPE_DOCUMENT','SID_ACTIVE','FacilityOverviewViewDocument','Permission to view document in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID251','PER_TYPE_DOCUMENT','SID_ACTIVE','FacilityOverviewUploadDocument','Permission to upload document in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID252','PER_TYPE_DOCUMENT','SID_ACTIVE','FacilityOverviewUpdateDocument','Permission to update document in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID253','PER_TYPE_DOCUMENT','SID_ACTIVE','FacilityOverviewDeleteDocument','Permission to delete document in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID254','PER_TYPE_DOCUMENT','SID_ACTIVE','EntityOverviewViewDocument','Permission to view document in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID255','PER_TYPE_DOCUMENT','SID_ACTIVE','EntityOverviewUploadDocument','Permission to upload document in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID256','PER_TYPE_DOCUMENT','SID_ACTIVE','EntityOverviewUpdateDocument','Permission to update document in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID257','PER_TYPE_DOCUMENT','SID_ACTIVE','EntityOverviewDeleteDocument','Permission to delete document in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID146','PER_TYPE_NARRATIVE','SID_ACTIVE','RequestOverviewViewNarrative','Permission to view narrative in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID147','PER_TYPE_NARRATIVE','SID_ACTIVE','RequestOverviewAddNarrative','Permission to add narrative in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID148','PER_TYPE_NARRATIVE','SID_ACTIVE','RequestOverviewUpdateNarrative','Permission to update narrative in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID149','PER_TYPE_NARRATIVE','SID_ACTIVE','RequestOverviewDeleteNarrative','Permission to delete narrative in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID258','PER_TYPE_NARRATIVE','SID_ACTIVE','FacilityOverviewViewNarrative','Permission to view narrative in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID259','PER_TYPE_NARRATIVE','SID_ACTIVE','FacilityOverviewAddNarrative','Permission to add narrative in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID260','PER_TYPE_NARRATIVE','SID_ACTIVE','FacilityOverviewUpdateNarrative','Permission to update narrative in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID261','PER_TYPE_NARRATIVE','SID_ACTIVE','FacilityOverviewDeleteNarrative','Permission to delete narrative in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID262','PER_TYPE_NARRATIVE','SID_ACTIVE','EntityOverviewViewNarrative','Permission to view narrative in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID263','PER_TYPE_NARRATIVE','SID_ACTIVE','EntityOverviewAddNarrative','Permission to add narrative in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID264','PER_TYPE_NARRATIVE','SID_ACTIVE','EntityOverviewUpdateNarrative','Permission to update narrative in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID265','PER_TYPE_NARRATIVE','SID_ACTIVE','EntityOverviewDeleteNarrative','Permission to delete narrative in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID150','PER_TYPE_PARTY','SID_ACTIVE','RequestOverviewViewParty','Permission to view party in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID151','PER_TYPE_PARTY','SID_ACTIVE','RequestOverviewAddParty','Permission to add party in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID152','PER_TYPE_PARTY','SID_ACTIVE','RequestOverviewUpdateParty','Permission to update party in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID153','PER_TYPE_PARTY','SID_ACTIVE','RequestOverviewDeleteParty','Permission to delete party in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID266','PER_TYPE_PARTY','SID_ACTIVE','FacilityOverviewViewParty','Permission to view party in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID267','PER_TYPE_PARTY','SID_ACTIVE','FacilityOverviewAddParty','Permission to add party in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID268','PER_TYPE_PARTY','SID_ACTIVE','FacilityOverviewUpdateParty','Permission to update party in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID269','PER_TYPE_PARTY','SID_ACTIVE','FacilityOverviewDeleteParty','Permission to delete party in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID270','PER_TYPE_PARTY','SID_ACTIVE','EntityOverviewViewParty','Permission to view party in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID271','PER_TYPE_PARTY','SID_ACTIVE','EntityOverviewAddParty','Permission to add party in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID272','PER_TYPE_PARTY','SID_ACTIVE','EntityOverviewUpdateParty','Permission to update party in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID273','PER_TYPE_PARTY','SID_ACTIVE','EntityOverviewDeleteParty','Permission to delete party in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID154','PER_TYPE_LIMIT','SID_ACTIVE','RequestOverviewViewLimit','Permission to view limits in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID155','PER_TYPE_LIMIT','SID_ACTIVE','RequestOverviewAddLimit','Permission to add limits in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID156','PER_TYPE_LIMIT','SID_ACTIVE','RequestOverviewUpdateLimit','Permission to update limits in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID157','PER_TYPE_LIMIT','SID_ACTIVE','RequestOverviewDeleteLimit','Permission to delete limits in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID274','PER_TYPE_LIMIT','SID_ACTIVE','FacilityOverviewViewLimit','Permission to view limits in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID275','PER_TYPE_LIMIT','SID_ACTIVE','FacilityOverviewAddLimit','Permission to add limits in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID276','PER_TYPE_LIMIT','SID_ACTIVE','FacilityOverviewUpdateLimit','Permission to update limits in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID277','PER_TYPE_LIMIT','SID_ACTIVE','FacilityOverviewDeleteLimit','Permission to delete limits in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID278','PER_TYPE_LIMIT','SID_ACTIVE','EntityOverviewViewLimit','Permission to view limits in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID279','PER_TYPE_LIMIT','SID_ACTIVE','EntityOverviewAddLimit','Permission to add limits in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID280','PER_TYPE_LIMIT','SID_ACTIVE','EntityOverviewUpdateLimit','Permission to update limits in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID281','PER_TYPE_LIMIT','SID_ACTIVE','EntityOverviewDeleteLimit','Permission to delete limits in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID158','PER_TYPE_BORROWER_FEE','SID_ACTIVE','RequestOverviewViewFee','Permission to view fee in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID159','PER_TYPE_BORROWER_FEE','SID_ACTIVE','RequestOverviewAddFee','Permission to add fee in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID160','PER_TYPE_BORROWER_FEE','SID_ACTIVE','RequestOverviewUpdateFee','Permission to update fee in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID161','PER_TYPE_BORROWER_FEE','SID_ACTIVE','RequestOverviewDeleteFee','Permission to delete fee in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID282','PER_TYPE_BORROWER_FEE','SID_ACTIVE','FacilityOverviewViewFee','Permission to view fee in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID283','PER_TYPE_BORROWER_FEE','SID_ACTIVE','FacilityOverviewAddFee','Permission to add fee in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID284','PER_TYPE_BORROWER_FEE','SID_ACTIVE','FacilityOverviewUpdateFee','Permission to update fee in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID285','PER_TYPE_BORROWER_FEE','SID_ACTIVE','FacilityOverviewDeleteFee','Permission to delete fee in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID286','PER_TYPE_BORROWER_FEE','SID_ACTIVE','EntityOverviewViewFee','Permission to view fee in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID287','PER_TYPE_BORROWER_FEE','SID_ACTIVE','EntityOverviewAddFee','Permission to add fee in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID288','PER_TYPE_BORROWER_FEE','SID_ACTIVE','EntityOverviewUpdateFee','Permission to update fee in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID289','PER_TYPE_BORROWER_FEE','SID_ACTIVE','EntityOverviewDeleteFee','Permission to delete fee in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID162','PER_TYPE_RISK_RATING','SID_ACTIVE','RequestOverviewViewRiskRating','Permission to view risk rating in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID290','PER_TYPE_RISK_RATING','SID_ACTIVE','FacilityOverviewViewRiskRating','Permission to view risk rating in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID291','PER_TYPE_RISK_RATING','SID_ACTIVE','EntityOverviewViewRiskRating','Permission to view risk rating in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID163','PER_TYPE_DECISION','SID_ACTIVE','RequestOverviewViewDecision','Permission to view decision in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID164','PER_TYPE_DECISION','SID_ACTIVE','RequestOverviewAddDecision','Permission to add decision in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID165','PER_TYPE_DECISION','SID_ACTIVE','RequestOverviewUpdateDecision','Permission to update decision in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID166','PER_TYPE_DECISION','SID_ACTIVE','RequestOverviewDeleteDecision','Permission to delete decision in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID292','PER_TYPE_DECISION','SID_ACTIVE','FacilityOverviewViewDecision','Permission to view decision in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID293','PER_TYPE_DECISION','SID_ACTIVE','FacilityOverviewAddDecision','Permission to add decision in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID294','PER_TYPE_DECISION','SID_ACTIVE','FacilityOverviewUpdateDecision','Permission to update decision in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID295','PER_TYPE_DECISION','SID_ACTIVE','FacilityOverviewDeleteDecision','Permission to delete decision in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID296','PER_TYPE_DECISION','SID_ACTIVE','EntityOverviewViewDecision','Permission to view decision in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID297','PER_TYPE_DECISION','SID_ACTIVE','EntityOverviewAddDecision','Permission to add decision in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID298','PER_TYPE_DECISION','SID_ACTIVE','EntityOverviewUpdateDecision','Permission to update decision in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID299','PER_TYPE_DECISION','SID_ACTIVE','EntityOverviewDeleteDecision','Permission to delete decision in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID167','PER_TYPE_CONDITION','SID_ACTIVE','RequestOverviewViewCondition','Permission to view condition in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID168','PER_TYPE_CONDITION','SID_ACTIVE','RequestOverviewUpdateCondition','Permission to update condition in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID169','PER_TYPE_CONDITION','SID_ACTIVE','RequestOverviewAddCondition','Permission to add condition in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID170','PER_TYPE_CONDITION','SID_ACTIVE','RequestOverviewDeleteCondition','Permission to delete condition in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID300','PER_TYPE_CONDITION','SID_ACTIVE','FacilityOverviewViewCondition','Permission to view condition in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID301','PER_TYPE_CONDITION','SID_ACTIVE','FacilityOverviewUpdateCondition','Permission to update condition in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID302','PER_TYPE_CONDITION','SID_ACTIVE','FacilityOverviewAddCondition','Permission to add condition in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID303','PER_TYPE_CONDITION','SID_ACTIVE','FacilityOverviewDeleteCondition','Permission to delete condition in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID304','PER_TYPE_CONDITION','SID_ACTIVE','EntityOverviewViewCondition','Permission to view condition in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID305','PER_TYPE_CONDITION','SID_ACTIVE','EntityOverviewUpdateCondition','Permission to update condition in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID306','PER_TYPE_CONDITION','SID_ACTIVE','EntityOverviewAddCondition','Permission to add condition in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID307','PER_TYPE_CONDITION','SID_ACTIVE','EntityOverviewDeleteCondition','Permission to delete condition in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID171','PER_TYPE_COVENANTS','SID_ACTIVE','RequestOverviewViewCovenant','Permission to view covenant in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID172','PER_TYPE_COVENANTS','SID_ACTIVE','RequestOverviewAddCovenant','Permission to add covenant in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID173','PER_TYPE_COVENANTS','SID_ACTIVE','RequestOverviewUpdateCovenant','Permission to update covenant in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID174','PER_TYPE_COVENANTS','SID_ACTIVE','RequestOverviewDeleteCovenant','Permission to delete covenant in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID308','PER_TYPE_COVENANTS','SID_ACTIVE','FacilityOverviewViewCovenant','Permission to view covenant in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID309','PER_TYPE_COVENANTS','SID_ACTIVE','FacilityOverviewAddCovenant','Permission to add covenant in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID310','PER_TYPE_COVENANTS','SID_ACTIVE','FacilityOverviewUpdateCovenant','Permission to update covenant in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID311','PER_TYPE_COVENANTS','SID_ACTIVE','FacilityOverviewDeleteCovenant','Permission to delete covenant in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID312','PER_TYPE_COVENANTS','SID_ACTIVE','EntityOverviewViewCovenant','Permission to view covenant in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID313','PER_TYPE_COVENANTS','SID_ACTIVE','EntityOverviewAddCovenant','Permission to add covenant in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID314','PER_TYPE_COVENANTS','SID_ACTIVE','EntityOverviewUpdateCovenant','Permission to update covenant in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID315','PER_TYPE_COVENANTS','SID_ACTIVE','EntityOverviewDeleteCovenant','Permission to delete covenant in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID175','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','RequestOverviewViewException','Permission to view exception in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID176','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','RequestOverviewAddException','Permission to add exception in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID177','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','RequestOverviewUpdateException','Permission to update exception in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID178','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','RequestOverviewDeleteException','Permission to delete exception in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID316','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','FacilityOverviewViewException','Permission to view exception in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID317','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','FacilityOverviewAddException','Permission to add exception in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID318','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','FacilityOverviewUpdateException','Permission to update exception in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID319','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','FacilityOverviewDeleteException','Permission to delete exception in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID320','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','EntityOverviewViewException','Permission to view exception in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID321','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','EntityOverviewAddException','Permission to add exception in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID322','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','EntityOverviewUpdateException','Permission to update exception in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID323','PER_TYPE_POLICY_EXCEPTIONS','SID_ACTIVE','EntityOverviewDeleteException','Permission to delete exception in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID179','PER_TYPE_COMPLIANCES','SID_ACTIVE','RequestOverviewViewCompliance','Permission to view compliance in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID180','PER_TYPE_COMPLIANCES','SID_ACTIVE','RequestOverviewUpdateCompliance','Permission to edit compliance in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID181','PER_TYPE_COMPLIANCES','SID_ACTIVE','RequestOverviewAddCompliance','Permission to add compliance in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID182','PER_TYPE_COMPLIANCES','SID_ACTIVE','RequestOverviewDeleteCompliance','Permission to delete compliance in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID324','PER_TYPE_COMPLIANCES','SID_ACTIVE','FacilityOverviewViewCompliance','Permission to view compliance in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID325','PER_TYPE_COMPLIANCES','SID_ACTIVE','FacilityOverviewUpdateCompliance','Permission to edit compliance in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID326','PER_TYPE_COMPLIANCES','SID_ACTIVE','FacilityOverviewAddCompliance','Permission to add compliance in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID327','PER_TYPE_COMPLIANCES','SID_ACTIVE','FacilityOverviewDeleteCompliance','Permission to delete compliance in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID328','PER_TYPE_COMPLIANCES','SID_ACTIVE','EntityOverviewViewCompliance','Permission to view compliance in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID329','PER_TYPE_COMPLIANCES','SID_ACTIVE','EntityOverviewUpdateCompliance','Permission to edit compliance in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID330','PER_TYPE_COMPLIANCES','SID_ACTIVE','EntityOverviewAddCompliance','Permission to add compliance in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID331','PER_TYPE_COMPLIANCES','SID_ACTIVE','EntityOverviewDeleteCompliance','Permission to delete compliance in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID183','PER_TYPE_INTEGRATION','SID_ACTIVE','RequestOverviewViewExternalService','Permission to view services in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID332','PER_TYPE_INTEGRATION','SID_ACTIVE','FacilityOverviewViewExternalService','Permission to view services in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID333','PER_TYPE_INTEGRATION','SID_ACTIVE','EntityOverviewViewExternalService','Permission to view services in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID184','PER_TYPE_CHECKLIST','SID_ACTIVE','RequestOverviewViewTask','Permission to view task in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID185','PER_TYPE_CHECKLIST','SID_ACTIVE','RequestOverviewUpdateTask','Permission to edit task in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID186','PER_TYPE_CHECKLIST','SID_ACTIVE','RequestOverviewAddTask','Permission to add task in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID187','PER_TYPE_CHECKLIST','SID_ACTIVE','RequestOverviewDeleteTask','Permission to delete task in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID334','PER_TYPE_CHECKLIST','SID_ACTIVE','FacilityOverviewViewTask','Permission to view task in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID335','PER_TYPE_CHECKLIST','SID_ACTIVE','FacilityOverviewUpdateTask','Permission to edit task in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID336','PER_TYPE_CHECKLIST','SID_ACTIVE','FacilityOverviewAddTask','Permission to add task in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID337','PER_TYPE_CHECKLIST','SID_ACTIVE','FacilityOverviewDeleteTask','Permission to delete task in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID338','PER_TYPE_CHECKLIST','SID_ACTIVE','EntityOverviewViewTask','Permission to view task in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID339','PER_TYPE_CHECKLIST','SID_ACTIVE','EntityOverviewUpdateTask','Permission to edit task in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID340','PER_TYPE_CHECKLIST','SID_ACTIVE','EntityOverviewAddTask','Permission to add task in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID341','PER_TYPE_CHECKLIST','SID_ACTIVE','EntityOverviewDeleteTask','Permission to delete task in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID188','PER_TYPE_WITHDRAW','SID_ACTIVE','RequestOverviewWithdrawApplication','Permission to withdraw application in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID342','PER_TYPE_WITHDRAW','SID_ACTIVE','FacilityOverviewWithdrawApplication','Permission to withdraw application in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID343','PER_TYPE_WITHDRAW','SID_ACTIVE','EntityOverviewWithdrawApplication','Permission to withdraw application in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID189','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','RequestOverviewViewRestriction','Permission to view restriction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID190','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','RequestOverviewAddRestriction','Permission to add restriction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID191','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','RequestOverviewUpdateRestriction','Permission to update restriction in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID192','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','RequestOverviewDeleteRestriction','Permission to delete restriction in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID344','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','FacilityOverviewViewRestriction','Permission to view restriction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID345','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','FacilityOverviewAddRestriction','Permission to add restriction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID346','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','FacilityOverviewUpdateRestriction','Permission to update restriction in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID347','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','FacilityOverviewDeleteRestriction','Permission to delete restriction in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID348','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','EntityOverviewViewRestriction','Permission to view restriction in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID349','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','EntityOverviewAddRestriction','Permission to add restriction in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID350','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','EntityOverviewUpdateRestriction','Permission to update restriction in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID351','PER_TYPE_DRAW_RESTRICTION','SID_ACTIVE','EntityOverviewDeleteRestriction','Permission to delete restriction in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID193','PER_TYPE_FUNDING','SID_ACTIVE','RequestOverviewViewFunding','Permission to view funding in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID194','PER_TYPE_FUNDING','SID_ACTIVE','RequestOverviewAddFunding','Permission to add funding in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID195','PER_TYPE_FUNDING','SID_ACTIVE','RequestOverviewUpdateFunding','Permission to update funding in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID196','PER_TYPE_FUNDING','SID_ACTIVE','RequestOverviewDeleteFunding','Permission to delete funding in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID352','PER_TYPE_FUNDING','SID_ACTIVE','FacilityOverviewViewFunding','Permission to view funding in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID353','PER_TYPE_FUNDING','SID_ACTIVE','FacilityOverviewAddFunding','Permission to add funding in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID354','PER_TYPE_FUNDING','SID_ACTIVE','FacilityOverviewUpdateFunding','Permission to update funding in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID355','PER_TYPE_FUNDING','SID_ACTIVE','FacilityOverviewDeleteFunding','Permission to delete funding in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID356','PER_TYPE_FUNDING','SID_ACTIVE','EntityOverviewViewFunding','Permission to view funding in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID357','PER_TYPE_FUNDING','SID_ACTIVE','EntityOverviewAddFunding','Permission to add funding in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID358','PER_TYPE_FUNDING','SID_ACTIVE','EntityOverviewUpdateFunding','Permission to update funding in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID359','PER_TYPE_FUNDING','SID_ACTIVE','EntityOverviewDeleteFunding','Permission to delete funding in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID197','PER_TYPE_PRICING','SID_ACTIVE','RequestOverviewViewPricing','Permission to view pricing in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID198','PER_TYPE_PRICING','SID_ACTIVE','RequestOverviewAddPricing','Permission to add pricing in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID199','PER_TYPE_PRICING','SID_ACTIVE','RequestOverviewUpdatePricing','Permission to update pricing in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID200','PER_TYPE_PRICING','SID_ACTIVE','RequestOverviewDeletePricing','Permission to delete pricing in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID360','PER_TYPE_PRICING','SID_ACTIVE','FacilityOverviewViewPricing','Permission to view pricing in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID361','PER_TYPE_PRICING','SID_ACTIVE','FacilityOverviewAddPricing','Permission to add pricing in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID362','PER_TYPE_PRICING','SID_ACTIVE','FacilityOverviewUpdatePricing','Permission to update pricing in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID363','PER_TYPE_PRICING','SID_ACTIVE','FacilityOverviewDeletePricing','Permission to delete pricing in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID364','PER_TYPE_PRICING','SID_ACTIVE','EntityOverviewViewPricing','Permission to view pricing in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID365','PER_TYPE_PRICING','SID_ACTIVE','EntityOverviewAddPricing','Permission to add pricing in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID366','PER_TYPE_PRICING','SID_ACTIVE','EntityOverviewUpdatePricing','Permission to update pricing in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID367','PER_TYPE_PRICING','SID_ACTIVE','EntityOverviewDeletePricing','Permission to delete pricing in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID201','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','RequestOverviewViewEntityOverview','Permission to view entity overview in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID202','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','RequestOverviewUpdateEntityOverview','Permission to update entity overview in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID368','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','FacilityOverviewViewEntityOverview','Permission to view entity overview in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID369','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','FacilityOverviewUpdateEntityOverview','Permission to update entity overview in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID370','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','EntityOverviewViewEntityOverview','Permission to view entity overview section in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID371','PER_TYPE_ENTITY_OVERVIEW','SID_ACTIVE','EntityOverviewUpdateEntityOverview','Permission to update entity overview section in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID203','PER_TYPE_ADDRESS','SID_ACTIVE','RequestOverviewViewAddress','Permission to view address in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID204','PER_TYPE_ADDRESS','SID_ACTIVE','RequestOverviewAddAddress','Permission to add address in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID205','PER_TYPE_ADDRESS','SID_ACTIVE','RequestOverviewUpdateAddress','Permission to update address in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID206','PER_TYPE_ADDRESS','SID_ACTIVE','RequestOverviewDeleteAddress','Permission to delete address in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID372','PER_TYPE_ADDRESS','SID_ACTIVE','FacilityOverviewViewAddress','Permission to view address in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID373','PER_TYPE_ADDRESS','SID_ACTIVE','FacilityOverviewAddAddress','Permission to add address in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID374','PER_TYPE_ADDRESS','SID_ACTIVE','FacilityOverviewUpdateAddress','Permission to update address in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID375','PER_TYPE_ADDRESS','SID_ACTIVE','FacilityOverviewDeleteAddress','Permission to delete address in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID376','PER_TYPE_ADDRESS','SID_ACTIVE','EntityOverviewViewAddress','Permission to view address in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID377','PER_TYPE_ADDRESS','SID_ACTIVE','EntityOverviewAddAddress','Permission to add address in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID378','PER_TYPE_ADDRESS','SID_ACTIVE','EntityOverviewUpdateAddress','Permission to update address in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID379','PER_TYPE_ADDRESS','SID_ACTIVE','EntityOverviewDeleteAddress','Permission to delete address in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID207','PER_TYPE_CONTACT','SID_ACTIVE','RequestOverviewViewContact','Permission to view contact in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID208','PER_TYPE_CONTACT','SID_ACTIVE','RequestOverviewAddContact','Permission to add contact in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID209','PER_TYPE_CONTACT','SID_ACTIVE','RequestOverviewUpdateContact','Permission to update contact in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID210','PER_TYPE_CONTACT','SID_ACTIVE','RequestOverviewDeleteContact','Permission to delete contact in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID380','PER_TYPE_CONTACT','SID_ACTIVE','FacilityOverviewViewContact','Permission to view contact in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID381','PER_TYPE_CONTACT','SID_ACTIVE','FacilityOverviewAddContact','Permission to add contact in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID382','PER_TYPE_CONTACT','SID_ACTIVE','FacilityOverviewUpdateContact','Permission to update contact in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID383','PER_TYPE_CONTACT','SID_ACTIVE','FacilityOverviewDeleteContact','Permission to delete contact in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID384','PER_TYPE_CONTACT','SID_ACTIVE','EntityOverviewViewContact','Permission to view contact in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID385','PER_TYPE_CONTACT','SID_ACTIVE','EntityOverviewAddContact','Permission to add contact in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID386','PER_TYPE_CONTACT','SID_ACTIVE','EntityOverviewUpdateContact','Permission to update contact in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID387','PER_TYPE_CONTACT','SID_ACTIVE','EntityOverviewDeleteContact','Permission to delete contact in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID211','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','RequestOverviewAddRatio','Permission to add ratio in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID212','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','RequestOverviewUpdateRatio','Permission to update ratio in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID213','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','RequestOverviewDeleteRatio','Permission to delete ratio in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID388','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','FacilityOverviewAddRatio','Permission to add ratio in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID389','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','FacilityOverviewUpdateRatio','Permission to update ratio in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID390','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','FacilityOverviewDeleteRatio','Permission to delete ratio in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID391','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','EntityOverviewAddRatio','Permission to add ratio in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID392','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','EntityOverviewUpdateRatio','Permission to update ratio in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID393','PER_TYPE_FINANCIAL_RATIO_RESULTS','SID_ACTIVE','EntityOverviewDeleteRatio','Permission to delete ratio in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID214','PER_TYPE_CREDIT_HISTORY','SID_ACTIVE','RequestOverviewViewCreditHistory','Permission to view credit history in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID394','PER_TYPE_CREDIT_HISTORY','SID_ACTIVE','FacilityOverviewViewCreditHistory','Permission to view credit history in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID395','PER_TYPE_CREDIT_HISTORY','SID_ACTIVE','EntityOverviewViewCreditHistory','Permission to view credit history in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID215','PER_TYPE_KYC','SID_ACTIVE','RequestOverviewViewKYC','Permission to view kyc in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID216','PER_TYPE_KYC','SID_ACTIVE','RequestOverviewAddKYC','Permission to add kyc in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID217','PER_TYPE_KYC','SID_ACTIVE','RequestOverviewUpdateKYC','Permission to update kyc in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID218','PER_TYPE_KYC','SID_ACTIVE','RequestOverviewDeleteKYC','Permission to delete kyc in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID396','PER_TYPE_KYC','SID_ACTIVE','FacilityOverviewViewKYC','Permission to view kyc in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID397','PER_TYPE_KYC','SID_ACTIVE','FacilityOverviewAddKYC','Permission to add kyc in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID398','PER_TYPE_KYC','SID_ACTIVE','FacilityOverviewUpdateKYC','Permission to update kyc in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID399','PER_TYPE_KYC','SID_ACTIVE','FacilityOverviewDeleteKYC','Permission to delete kyc in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID400','PER_TYPE_KYC','SID_ACTIVE','EntityOverviewViewKYC','Permission to view kyc in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID401','PER_TYPE_KYC','SID_ACTIVE','EntityOverviewAddKYC','Permission to add kyc in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID402','PER_TYPE_KYC','SID_ACTIVE','EntityOverviewUpdateKYC','Permission to update kyc in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID403','PER_TYPE_KYC','SID_ACTIVE','EntityOverviewDeleteKYC','Permission to delete kyc in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID219','PER_TYPE_RISK_RATING','SID_ACTIVE','RequestOverviewAddRiskRating','Permission to add risk rating in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID220','PER_TYPE_RISK_RATING','SID_ACTIVE','RequestOverviewUpdateRiskRating','Permission to update risk rating in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID221','PER_TYPE_RISK_RATING','SID_ACTIVE','RequestOverviewDeleteRiskRating','Permission to delete risk rating in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID404','PER_TYPE_RISK_RATING','SID_ACTIVE','FacilityOverviewAddRiskRating','Permission to add risk rating in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID405','PER_TYPE_RISK_RATING','SID_ACTIVE','FacilityOverviewUpdateRiskRating','Permission to update risk rating in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID406','PER_TYPE_RISK_RATING','SID_ACTIVE','FacilityOverviewDeleteRiskRating','Permission to delete risk rating in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID407','PER_TYPE_RISK_RATING','SID_ACTIVE','EntityOverviewAddRiskRating','Permission to add risk rating in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID408','PER_TYPE_RISK_RATING','SID_ACTIVE','EntityOverviewUpdateRiskRating','Permission to update risk rating in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID409','PER_TYPE_RISK_RATING','SID_ACTIVE','EntityOverviewDeleteRiskRating','Permission to delete risk rating in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID222','PER_TYPE_ACCOUNT','SID_ACTIVE','RequestOverviewViewAccount','Permission to viewa ccoun in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID223','PER_TYPE_ACCOUNT','SID_ACTIVE','RequestOverviewAddAccount','Permission to add account in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID224','PER_TYPE_ACCOUNT','SID_ACTIVE','RequestOverviewUpdateAccount','Permission to update account in Request Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID225','PER_TYPE_ACCOUNT','SID_ACTIVE','RequestOverviewDeleteAccount','Permission to delete account in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID410','PER_TYPE_ACCOUNT','SID_ACTIVE','FacilityOverviewViewAccount','Permission to viewa ccount in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID411','PER_TYPE_ACCOUNT','SID_ACTIVE','FacilityOverviewAddAccount','Permission to add account in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID412','PER_TYPE_ACCOUNT','SID_ACTIVE','FacilityOverviewUpdateAccount','Permission to update account in Facility Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID413','PER_TYPE_ACCOUNT','SID_ACTIVE','FacilityOverviewDeleteAccount','Permission to delete account in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID414','PER_TYPE_ACCOUNT','SID_ACTIVE','EntityOverviewViewAccount','Permission to viewa ccount in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID415','PER_TYPE_ACCOUNT','SID_ACTIVE','EntityOverviewAddAccount','Permission to add account in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID416','PER_TYPE_ACCOUNT','SID_ACTIVE','EntityOverviewUpdateAccount','Permission to update account in Entity Overview','0','TRUE');
INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID417','PER_TYPE_ACCOUNT','SID_ACTIVE','EntityOverviewDeleteAccount','Permission to delete account in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID226','PER_TYPE_HISTORY','SID_ACTIVE','RequestOverviewViewHistory','Permission to view history in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID418','PER_TYPE_HISTORY','SID_ACTIVE','FacilityOverviewViewHistory','Permission to view history in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID419','PER_TYPE_HISTORY','SID_ACTIVE','EntityOverviewViewHistory','Permission to view history in Entity Overview','0','TRUE');




INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID227','PER_TYPE_EXPOSURE_HISTORY','SID_ACTIVE','RequestOverviewViewExposureHstory','Permission to view exposure history in Request Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID420','PER_TYPE_EXPOSURE_HISTORY','SID_ACTIVE','FacilityOverviewViewExposureHstory','Permission to view exposure history in Facility Overview','0','TRUE');

INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID421','PER_TYPE_EXPOSURE_HISTORY','SID_ACTIVE','EntityOverviewViewExposureHstory','Permission to view exposure history in Entity Overview','0','TRUE');



INSERT INTO `permission` (`id`,`Type_id`,`Status_id`,`Name`,`Description`,`isComposite`,`PermissionValue`) VALUES ('PID422','PER_TYPE_CREATE_APPLICATION','SID_ACTIVE','CreateApplication','Permission to create a new application','0','TRUE');

INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_UNDERWRITER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'UnderWriter', 'This role is for underwriter');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_RELATIONSHIP_MANAGER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Relationship Manager', 'This role is for a relationship manager');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SUPERVISOR', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Supervisor', 'This role is for a supervisor');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SECRETARY', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Secretary', 'This role is for a secretary');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_OPERATIONS', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Operations', 'This role is for a operations');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID136');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID137');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID138');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID154');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID167');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID168');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID183');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID189');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID238');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID239');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID240');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID242');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID243');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID244');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID278');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID301');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID344');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID362');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID371');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID395');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID421');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RELATIONSHIP_MANAGER', 'PID422');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID135');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID136');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID137');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID138');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID154');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID155');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID156');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID157');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID167');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID168');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID169');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID170');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID172');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID173');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID183');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID189');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID190');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID191');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID192');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID234');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID238');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID239');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID240');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID242');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID278');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID301');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID302');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID303');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID309');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID310');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID314');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID344');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID362');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID393');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID395');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_UNDERWRITER', 'PID421');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID135');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID136');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID137');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID138');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID139');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID145');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID149');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID154');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID155');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID156');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID157');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID167');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID168');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID169');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID170');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID172');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID173');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID174');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID178');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID182');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID183');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID189');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID190');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID191');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID192');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID234');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID238');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID239');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID240');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID241');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID242');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID243');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID244');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID245');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID253');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID257');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID261');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID265');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID278');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID301');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID302');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID303');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID309');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID310');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID311');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID314');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID315');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID327');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID344');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID345');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID346');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID347');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID362');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID371');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID393');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID395');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID409');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID417');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SUPERVISOR', 'PID421');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID136');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID154');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID167');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID183');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID189');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID238');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID242');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID278');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID344');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID395');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SECRETARY', 'PID421');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID136');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID137');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID138');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID139');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID145');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID154');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID167');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID168');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID173');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID183');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID189');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID238');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID239');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID240');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID241');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID242');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID243');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID244');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID245');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID253');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID257');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID278');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID301');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID310');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID314');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID344');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID395');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_OPERATIONS', 'PID421');

INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `isPrimary`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'RETAIL_AND_BUSINESS_BANKING', 'Conversational Banking', 'Conversational Banking', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '0', '0');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`) VALUES ('ACTIVATE_CHAT_BOT', 'CONVERSATIONAL_BANKING', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Activate Chat Bot', 'Activate Chat Bot', '0', '0', '0', '0', 'SID_ACTION_ACTIVE', 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`, `softdeleteflag`) VALUES ('TYPE_ID_RETAIL', 'CONVERSATIONAL_BANKING', '0');
INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`, `softdeleteflag`) VALUES ('TYPE_ID_WEALTH', 'CONVERSATIONAL_BANKING', '0');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`) VALUES ('TYPE_ID_RETAIL', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `softdeleteflag`) VALUES ('TYPE_ID_WEALTH', 'ACTIVATE_CHAT_BOT', '0');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'de-DE', 'Conversational Banking', 'Conversational Banking', '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'en-GB', 'Conversational Banking', 'Conversational Banking', '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'en-US', 'Conversational Banking', 'Conversational Banking', '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'es-ES', 'Conversational Banking', 'Conversational Banking', '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('CONVERSATIONAL_BANKING', 'fr-FR', 'Conversational Banking', 'Conversational Banking', '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('ACTIVATE_CHAT_BOT', 'de-DE', 'Activate Chat Bot', 'Activate Chat Bot', '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('ACTIVATE_CHAT_BOT', 'en-GB', 'Activate Chat Bot', 'Activate Chat Bot', '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('ACTIVATE_CHAT_BOT', 'en-US', 'Activate Chat Bot', 'Activate Chat Bot', '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('ACTIVATE_CHAT_BOT', 'es-ES', 'Activate Chat Bot', 'Activate Chat Bot', '0');
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`, `softdeleteflag`) VALUES ('ACTIVATE_CHAT_BOT', 'fr-FR', 'Activate Chat Bot', 'Activate Chat Bot', '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `softdeleteflag`) VALUES ('292c1c1e-c504-11eb-8529-0242ac130003', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `softdeleteflag`) VALUES ('292c215a-c504-11eb-8529-0242ac130003', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `softdeleteflag`) VALUES ('292c227c-c504-11eb-8529-0242ac130003', '5801fa32-a416-45b6-af01-b22e2de93777', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `softdeleteflag`) VALUES ('292c234e-c504-11eb-8529-0242ac130003', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ACTIVATE_CHAT_BOT', '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('af3e359c-c50b-11eb-8529-0242ac130003', 'DEFAULT_GROUP', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('af3e3862-c50b-11eb-8529-0242ac130003', 'GROUP_ADMINISTRATOR', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `softdeleteflag`) VALUES ('af3e3a2e-c50b-11eb-8529-0242ac130003', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'ACTIVATE_CHAT_BOT', '0');

INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('5ea4f1ec-c50c-11eb-8529-0242ac130003', '7321457251', '1425958', 'CONVERSATIONAL_BANKING', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('5ea4f606-c50c-11eb-8529-0242ac130003', '7321457251', '1578660', 'CONVERSATIONAL_BANKING', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('5ea4f700-c50c-11eb-8529-0242ac130003', '4204010299', '1065631', 'CONVERSATIONAL_BANKING', 'ACTIVATE_CHAT_BOT', '0');
INSERT INTO `contractactionlimit` (`id`, `contractId`, `coreCustomerId`, `featureId`, `actionId`, `softdeleteflag`) VALUES ('5ea4f7c8-c50c-11eb-8529-0242ac130003', '4204010299', '1605506', 'CONVERSATIONAL_BANKING', 'ACTIVATE_CHAT_BOT', '0');

INSERT INTO `contractfeatures` (`id`, `contractId`, `coreCustomerId`, `featureId`) VALUES ('01dd758c-c50d-11eb-8529-0242ac130003', '7321457251', '1425958', 'CONVERSATIONAL_BANKING');
INSERT INTO `contractfeatures` (`id`, `contractId`, `coreCustomerId`, `featureId`) VALUES ('01dd780c-c50d-11eb-8529-0242ac130003', '7321457251', '1578660', 'CONVERSATIONAL_BANKING');
INSERT INTO `contractfeatures` (`id`, `contractId`, `coreCustomerId`, `featureId`) VALUES ('01dd7aaa-c50d-11eb-8529-0242ac130003', '4204010299', '1065631', 'CONVERSATIONAL_BANKING');
INSERT INTO `contractfeatures` (`id`, `contractId`, `coreCustomerId`, `featureId`) VALUES ('01dd7b86-c50d-11eb-8529-0242ac130003', '4204010299', '1605506', 'CONVERSATIONAL_BANKING');


-- update model and attribiute details 
update `model` set endpoint_url='http://40.127.187.34/APIServices/odata/Tenant1/XAIAttritionResultsDetailed' where `id`='DC002';
INSERT INTO `attributeoption` (`id`, `endpoint_attributeoption_id`, `name`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('ATTR_OPT_1_LOW', '1_Low', '1_Low', 'Kony App', 'Kony App', '2021-05-03 06:22:44', '2021-05-03 06:22:44', '2021-05-03 06:22:44', '0');
INSERT INTO `attributeoption` (`id`, `endpoint_attributeoption_id`, `name`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('ATTR_OPT_1_HIGH', '1_High', '1_High', 'Kony App', 'Kony App', '2021-05-03 06:22:44', '2021-05-03 06:22:44', '2021-05-03 06:22:44', '0');
INSERT INTO `modelattribute` (`model_id`, `attribute_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('DC002', 'ATTRIBUTE_ATTRITION_GROUP', 'Kony App', 'Kony App', '2021-01-19 08:35:53', '2021-01-19 08:35:53', '2021-01-19 08:35:53', '0');
INSERT INTO `attribute` (`id`, `endpoint_attribute_id`, `name`, `attributetype`, `options`, `criterias`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('ATTRIBUTE_ATTRITION_GROUP', 'attritiongroup', 'Attrition Group', 'ALPHABETICAL', '[{\"Select\":\"i18n.frmAdManagement.Select\"},{\"ATTR_OPT_4_HIGH\":\"i18n.frmAdManagement.High\"},{\"ATTR_OPT_MEDIUM\":\"i18n.frmAdManagement.Medium\"},\r {\"ATTR_OPT_1_LOW\":\"1_Low\"}]', '[{\"eq\":\"i18n.frmAdManagement.Equal\"}]', '2021-01-19 08:34:41', '2021-01-19 08:34:41', '2021-01-19 08:34:41', '0');
-- end of update model and attribiute details 


UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_COLLECTION_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_COLLECTION_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_FILE_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_FILE_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_PAYMENT_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'ACH_PAYMENT_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'BILL_PAY_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'BILL_PAY_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'BULK_PAYMENT_REQUEST_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'CHEQUE_BOOK_REQUEST_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'DOMESTIC_WIRE_TRANSFER_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'DOMESTIC_WIRE_TRANSFER_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_WIRE_TRANSFER_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTRA_BANK_FUND_TRANSFER_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'P2P_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'P2P_SELF_APPROVAL');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE');
UPDATE `featureaction` SET `isApprovalAction` = '1' WHERE (`id` = 'TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL');

-- card management remapping

UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'DebitCardProducts' ,`operation` = 'getProducts' WHERE `service_name` = 'RBObjects' AND `object_name` = 'CardProducts' AND `operation` = 'getCardProducts';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ListOfCards' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ActivateCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'activateCards';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'CancelCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'cancelCard';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ReplaceCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'replaceCard';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ChangePIN' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'changePIN';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ReportLost' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'reportLost';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'LockCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'lockCard';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'UnlockCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'unlockCard';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'WithdrawalLimit' ,`operation` = 'updateLimit' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'updateWithdrawalLimit';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'PurchaseLimit' ,`operation` = 'updateLimit' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'updatePurchaseLimit';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'TravelNotifications' ,`operation` = 'createPlan' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'createTravelNotification';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'TravelNotifications' ,`operation` = 'getPlan' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'getTravelNotification';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'TravelNotifications' ,`operation` = 'updatePlan' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'updateTravelNotification';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'TravelNotifications' ,`operation` = 'deletePlan' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'deleteTravelNotification';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'RequestDebitCard' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'applyForDebitCard';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ApplePay' ,`operation` = 'AddCard' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'createCardDataForApplePay';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'GooglePay' ,`operation` = 'AddCard' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'createCardDataForGooglePay';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'SamsungPay' ,`operation` = 'AddCard' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'createCardDataForSamsungPay';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'CardIssuer' ,`operation` = 'EnrollWithIssuer' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'enrollCard';

-- card management remapping

INSERT INTO `usertype` (`id`, `name`) VALUES ('INTERNAL', 'Internal'),('EXTERNAL', 'External'),('CONTRACT', 'Contract');
INSERT INTO `lob` (`id`) VALUES ('TYPE_ID_RETAIL'), ('TYPE_ID_WEALTH'),('TYPE_ID_CORPORATE'),('TYPE_ID_MID_CORPORATE'),('TYPE_ID_BUSINESS');
INSERT INTO `lobtext` (`lobId`, `languageCode`, `displayName`, `description`) VALUES ('TYPE_ID_RETAIL', 'en-US', 'Retail Banking', 'Retail Banking'),
('TYPE_ID_WEALTH', 'en-US', 'Wealth Banking', 'Wealth Banking'),
('TYPE_ID_CORPORATE', 'en-US', 'Corporate Banking', 'Corporate Banking'),
('TYPE_ID_MID_CORPORATE', 'en-US', 'Mid Corporate Banking', 'Mid Corporate Banking'),
('TYPE_ID_BUSINESS', 'en-US', 'SME Banking', 'SME Banking');
INSERT INTO `userlob` (`userId`, `lobId`) VALUES ('UID10', 'TYPE_ID_RETAIL'),('UID10', 'TYPE_ID_WEALTH'), ('UID10', 'TYPE_ID_CORPORATE'), ('UID10', 'TYPE_ID_MID_CORPORATE'),('UID10', 'TYPE_ID_BUSINESS');



-- savings pot remapping

UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'Pot' ,`operation` = 'createPot' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPot' AND `operation` = 'createSavingsPot';
UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'Pot' ,`operation` = 'updatePot' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPot' AND `operation` = 'updateSavingsPot';
UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'Pot' ,`operation` = 'getPot' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPot' AND `operation` = 'getAllSavingsPot';
UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'Pot' ,`operation` = 'closePot' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPot' AND `operation` = 'closeSavingsPot';
UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'PotBalance' ,`operation` = 'updateBalance' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPot' AND `operation` = 'updateSavingsPotBalance';
UPDATE `service_permission_mapper` SET `service_name` = 'SavingsPot',`object_name` = 'PotCategories' ,`operation` = 'getCategories' WHERE `service_name` = 'SavingsPot' AND `object_name` = 'SavingsPotCategories' AND `operation` = 'getCategoriesForGoal';

-- savings pot remapping

-- payee management remapping

INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`)VALUES('0d4e1123-c90d-11eb-9825-8cec4bd683f3','PayeeManagement','Payees','createPayee','INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT,TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE_RECEPIENT,INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT,INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT');
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`)VALUES('0d4e2234-c90d-11eb-9825-8cec4bd683f3','PayeeManagement','Payees','deletePayee','INTER_BANK_ACCOUNT_FUND_TRANSFER_DELETE_RECEPIENT,INTERNATIONAL_WIRE_TRANSFER_DELETE_RECEPIENT,TRANSFER_BETWEEN_OWN_ACCOUNT_DELETE_RECEPIENT,INTRA_BANK_FUND_TRANSFER_DELETE_RECEPIENT');
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`)VALUES('0d4e3345-c90d-11eb-9825-8cec4bd683f3','PayeeManagement','Payees','editPayee','TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE_RECEPIENT,INTRA_BANK_FUND_TRANSFER_CREATE_RECEPIENT,INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT,INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE_RECEPIENT');
INSERT INTO `service_permission_mapper`(`id`,`service_name`,`object_name`,`operation`,`permissions`)VALUES('0d4e4456-c90d-11eb-9825-8cec4bd683f3','PayeeManagement','Payees','getPayees','INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT,INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT,TRANSFER_BETWEEN_OWN_ACCOUNT_VIEW_RECEPIENT,INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT');

-- payee management remapping

-- Dashboard services remapping
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'createView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'createCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'getView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'getCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'updateView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'updateCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'deleteView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'deleteCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CashPositions' ,`operation` = 'getDetails' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'CashPositions' AND `operation` = 'getCashPositions';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMPieChart' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMPieChart' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMAccounts' ,`operation` = 'getPFMAccounts' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMAccounts' AND `operation` = 'getPFMAccounts';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMAccounts' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMAccounts' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMBarGraph' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMBarGraph' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMBudgetGraph' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMBudgetGraph' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'updateBulkPFMTransaction' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'updateBulkPFMTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'partialupdate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'partialupdate';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMCategory' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMCategory' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'createView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'createCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'getView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'getCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'updateView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'updateCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CustomView' ,`operation` = 'deleteView' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'deleteCustomView';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'CashPositions' ,`operation` = 'getDetails' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'CashPositions' AND `operation` = 'getCashPositions';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMPieChart' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMPieChart' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMAccounts' ,`operation` = 'getPFMAccounts' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMAccounts' AND `operation` = 'getPFMAccounts';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMAccounts' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMAccounts' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMBarGraph' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMBarGraph' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMBudgetGraph' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMBudgetGraph' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'updateBulkPFMTransaction' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'updateBulkPFMTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMTransactions' ,`operation` = 'partialupdate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMTransactions' AND `operation` = 'partialupdate';
UPDATE `service_permission_mapper` SET `service_name` = 'PFM',`object_name` = 'PFMCategory' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'PFMCategory' AND `operation` = 'get';

-- IC-1762 

UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CombinedStatements' ,`operation` = 'generate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'CombinedStatements' AND `operation` = 'generateCombinedStatement';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CombinedStatements' ,`operation` = 'getStatements' WHERE `service_name` = 'RBObjects' AND `object_name` = 'CombinedStatements' AND `operation` = 'getCombinedStatementDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CombinedStatements' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'CombinedStatements' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'EStatements' ,`operation` = 'getStatementList' WHERE `service_name` = 'TransactionAdvice' AND `object_name` = 'TransactionStatement' AND `operation` = 'getTransactionStatementsByYear';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'EStatements' ,`operation` = 'get' WHERE `service_name` = 'TransactionAdvice' AND `object_name` = 'TransactionStatement' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CustomerAdvice' ,`operation` = 'generate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DownloadAttachments' AND `operation` = 'retrieveAttachments';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CustomerAdvice' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DownloadAttachments' AND `operation` = 'get';

-- IC-1762 

-- Alerts and Notifications remapping
UPDATE `service_permission_mapper` SET `service_name` = 'Notifications',`object_name` = 'NotificationList' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Notifications' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'Notifications',`object_name` = 'NotificationList' ,`operation` = 'deleteNotification' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Notifications' AND `operation` = 'deleteNotification';
UPDATE `service_permission_mapper` SET `service_name` = 'Notifications',`object_name` = 'UnreadNotifications' ,`operation` = 'getCount' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Notifications' AND `operation` = 'getUnreadNotificationCount';
UPDATE `service_permission_mapper` SET `service_name` = 'Notifications',`object_name` = 'UnreadNotifications' ,`operation` = 'partialupdate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Notifications' AND `operation` = 'partialupdate';
-- Alerts and Notifications remapping

-- Cheque Managements remapping
UPDATE `service_permission_mapper` SET `service_name` = 'ChequeManagement',`object_name` = 'ChequeBook' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'createChequeBookRequests';
UPDATE `service_permission_mapper` SET `service_name` = 'ChequeManagement',`object_name` = 'ChequeBook' ,`operation` = 'getRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getChequeBookRequests';
UPDATE `service_permission_mapper` SET `service_name` = 'ChequeManagement',`object_name` = 'StopPayment' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'createStopChequePayments';
UPDATE `service_permission_mapper` SET `service_name` = 'ChequeManagement',`object_name` = 'StopPayment' ,`operation` = 'getRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getStopChequePayments';
UPDATE `service_permission_mapper` SET `service_name` = 'ChequeManagement',`object_name` = 'RevokeStopPayment' ,`operation` = 'createRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'revokeStopChequePayments';
-- Cheque Managements remapping

-- Transfers remapping
UPDATE `service_permission_mapper` SET `service_name` = 'Transfers',`object_name` = 'OneTimeTransfer' ,`operation` = 'getTransfers' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getRecentUserTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'Transfers',`object_name` = 'StandingInstruction' ,`operation` = 'getInstructions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getScheduledUserTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'Transfers',`object_name` = 'OneTimeTransfer' ,`operation` = 'Create' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'createOneTimeTransfer';
-- Transfers remapping

-- Consent Mangement remapping
UPDATE `service_permission_mapper` SET `service_name` = 'ConsentManagement',`object_name` = 'CDPConsent' ,`operation` = 'getConsent' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Consents' AND `operation` = 'getCDPConsents';
UPDATE `service_permission_mapper` SET `service_name` = 'ConsentManagement',`object_name` = 'CDPConsent' ,`operation` = 'updateConsent' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Consents' AND `operation` = 'updateCDPConsent';
UPDATE `service_permission_mapper` SET `service_name` = 'ConsentManagement',`object_name` = 'PSD2Consent' ,`operation` = 'getConsent' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Consents' AND `operation` = 'getPSDConsents';
UPDATE `service_permission_mapper` SET `service_name` = 'ConsentManagement',`object_name` = 'PSD2Consent' ,`operation` = 'updateConsent' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Consents' AND `operation` = 'updatePSDConsent';
-- Consent Management remapping

-- ADP-4338

UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'DigitalArrangements' ,`operation` = 'getList' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'getAccountsPostLogin';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'ArrangementDetails' ,`operation` = 'getDetails' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Accounts' AND `operation` = 'getAccountDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'BlockFunds' ,`operation` = 'getList' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getBlockedFunds';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'getRecent' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getAccountTransactionByType';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'get';

-- ADP-4338

UPDATE `service_permission_mapper` SET `service_name` = 'PayeeManagement',`object_name` = 'Payees' ,`operation` = 'getIntraInterBankPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'getIntraInterBankPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'Transfers',`object_name` = 'Payment_Multi' ,`operation` = 'createMultiTransfers' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'createBulkTransfer';

UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Biller' ,`operation` = 'searchBillerByName' WHERE `service_name` = 'RBObjects' AND `object_name` = 'BillerMaster' AND `operation` = 'searchBillerByName';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payee_BillPay' ,`operation` = 'createBillPayPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'createBillPayPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payee_BillPay' ,`operation` = 'getBillPayPayees' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'getBillPayPayees';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payee_BillPay' ,`operation` = 'updateBillPayPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'editBillPayPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payee_BillPay' ,`operation` = 'deleteBillPayPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'deleteBillPayPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment' ,`operation` = 'createPayment' WHERE `service_name` = 'TransactionObjects' AND `object_name` = 'Transaction' AND `operation` = 'BillPayTransfer';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment' ,`operation` = 'createPayment_bulk' WHERE `service_name` = 'TransactionObjects' AND `object_name` = 'Transaction' AND `operation` = 'BulkBillPayTransfer';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment' ,`operation` = 'updatePayment' WHERE `service_name` = 'TransactionObjects' AND `object_name` = 'Transaction' AND `operation` = 'BillPayTransferEdit';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment' ,`operation` = 'updatePayment_delete' WHERE `service_name` = 'TransactionObjects' AND `object_name` = 'Transaction' AND `operation` = 'BillPayTransferDelete';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment_BillPayList' ,`operation` = 'getPayeePayments' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getPayeeBills';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment_BillPayList' ,`operation` = 'getCompletedPayments' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getUserCompletedBillHistory';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment_BillPayList' ,`operation` = 'getScheduledPayments' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getUsersScheduledBill';
UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payment_ebills' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Bills' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'CustomerAdvice_BillPay' ,`operation` = 'generate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DownloadTransactionReport' AND `operation` = 'generateTransactionReport';

INSERT INTO `mfaserviceconfig` (`serviceName`, `transactionType`) VALUES ('BILL_PAY_CREATE', 'billpay_payment_createpayment');
INSERT INTO `mfaserviceconfig` (`serviceName`, `transactionType`) VALUES ('BILL_PAY_CREATE', 'billpay_payment_updatepayment');
INSERT INTO `mfaserviceconfig` (`serviceName`, `transactionType`) VALUES ('BILL_PAY_CREATE', 'billpay_payment_createpayment_bulk');
INSERT INTO `mfaserviceconfig` (`serviceName`, `transactionType`) VALUES ('PAY_MULTIPLE_BENEFICIARIES_CREATE_TRANSFER', 'transfers_payment_multi_createmultitransfers');

UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payee_P2P' ,`operation` = 'createP2PPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'createP2PPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payee_P2P' ,`operation` = 'getP2PPayees' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'getP2PPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payee_P2P' ,`operation` = 'deleteP2PPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'deleteP2PPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payee_P2P' ,`operation` = 'updateP2PPayee' WHERE `service_name` = 'PayeeObjects' AND `object_name` = 'Recipients' AND `operation` = 'editP2PPayee';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payment_P2P' ,`operation` = 'createTransfer' WHERE `service_name` = 'TransactionObjects' AND `object_name` = 'Transaction' AND `operation` = 'P2PTransfer';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payment_P2PList' ,`operation` = 'getTransfers' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getPayPersonHistory';
UPDATE `service_permission_mapper` SET `service_name` = 'DigitalTransfer',`object_name` = 'Payment_P2PList' ,`operation` = 'updateTransfer' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'deleteTransaction';

UPDATE `service_permission_mapper` SET `service_name` = 'BillPay',`object_name` = 'Payee_BillPayRecent' ,`operation` = 'getRecentPayee' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Payee' AND `operation` = 'getRecentPayee';

UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'createDisputeRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'createDisputedTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'getDisputeRequest' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getDisputedTransactions';

UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'DownloadTransactions' ,`operation` = 'get' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DownloadTransaction' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'DocumentManagement',`object_name` = 'DownloadTransactions' ,`operation` = 'generate' WHERE `service_name` = 'RBObjects' AND `object_name` = 'DownloadTransaction' AND `operation` = 'generateTransactionDetails';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'getPostedUserTransactions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getPostedUserTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'Holdings',`object_name` = 'TransactionsList' ,`operation` = 'getPendingUserTransactions' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Transactions' AND `operation` = 'getPendingUserTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'TravelNotifications' ,`operation` = 'getStatus' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'getTravelNotificationStatus';
UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ListOfCards' ,`operation` = 'getActiveCards' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'getActiveCards';

INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SME_UW', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'SME UW', 'This role is for SME underwriter');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SME_RM', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'SME RM', 'This role is for SME relationship manager');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SME_OPS', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'SME Ops', 'This role is for SME Operations');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_SME_SUPERVISOR', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'SME Supervisor', 'This role is for SME Supervisor');


INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID236');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID230');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID231');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_RM', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID172');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID173');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID236');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID309');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID310');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID230');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID393');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID313');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID314');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_UW', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID230');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_OPS', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID135');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID140');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID141');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID145');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID149');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID171');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID172');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID173');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID174');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID178');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID182');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID188');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID234');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID236');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID253');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID261');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID308');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID309');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID310');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID311');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID327');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID230');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID231');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID248');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID391');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID392');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID393');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID257');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID265');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID312');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID313');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID314');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID315');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID409');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID417');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_SME_SUPERVISOR', 'PID419');


INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_RetailRM', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Retail RM', 'This role is for Retail Relationship manager');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_RetailUW', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Retail UW', 'This role is for Retail underwriter');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_RetailOps', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Retail Ops', 'This role is for Retail Operations');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`) VALUES ('RID_RetailSupervisor', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Retail Supervisor', 'This role is for Retail Supervisor');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID371');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailRM', 'PID188');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID234');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID135');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID178');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID182');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailUW', 'PID188');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailOps', 'PID179');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID370');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID371');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID376');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID377');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID379');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID378');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID384');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID385');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID386');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID387');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID254');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID256');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID257');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID262');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID263');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID264');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID265');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID270');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID271');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID273');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID400');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID401');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID402');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID403');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID291');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID407');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID408');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID409');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID414');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID415');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID416');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID417');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID419');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID255');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID228');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID229');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID232');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID234');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID233');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID250');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID251');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID252');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID253');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID258');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID259');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID260');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID261');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID266');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID267');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID268');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID269');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID360');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID361');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID363');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID282');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID283');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID284');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID285');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID290');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID292');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID293');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID294');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID295');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID352');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID353');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID354');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID355');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID324');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID326');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID325');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID327');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID300');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID131');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID132');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID133');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID135');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID134');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID142');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID143');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID144');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID145');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID146');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID147');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID148');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID149');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID150');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID151');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID152');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID153');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID158');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID159');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID160');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID161');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID162');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID163');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID164');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID165');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID166');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID175');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID176');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID177');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID178');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID179');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID181');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID180');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID182');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`) VALUES ('RID_RetailSupervisor', 'PID188');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_RM_MANAGER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'RM Manager', 'This role is for RM Manager', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_OPERATIONS_ASSOCIATE', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Operations Associate', 'This role is for Operations Associate', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_IT_ADMINISTRATOR', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'IT Administrator', 'This role is for IT Administrator', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `role` (`id`, `Type_id`, `Status_id`, `Parent_id`, `Name`, `Description`, `createdby`,  `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_OPERATIONS_MANAGER', 'ROLE_TYPE_1', 'SID_ACTIVE', 'RID_SUPERADMIN', 'Operations Manager', 'This role is for Operations Manager', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_RM_MANAGER', 'PID01', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_OPERATIONS_ASSOCIATE', 'PID01', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_IT_ADMINISTRATOR', 'PID01', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RID_OPERATIONS_MANAGER', 'PID01', 'Kony User', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1b2d347b-ade5-4a22-8844-cb6351d4fd1d', 'InternalusersObjService', 'Users', 'getUserType', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('24c41728-530b-43ff-a7a9-9355d0ec3b71', 'InternalusersObjService', 'LOB', 'getLOB', 'ALLOW');

UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHAccountTypes' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHAccountTypes' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFile' ,`operation` = 'getAllACHFiles' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFile' AND `operation` = 'getAllACHFiles';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFile' ,`operation` = 'UploadACHFile' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFile' AND `operation` = 'UploadACHFile';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFile' ,`operation` = 'getFileDetailsByID' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFile' AND `operation` = 'getFileDetailsByID';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFileFormats' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFileFormats' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFileRecord' ,`operation` = 'fetchACHFileRecords' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFileRecord' AND `operation` = 'fetchACHFileRecords';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHFileSubRecord' ,`operation` = 'fetchACHFileSubRecords' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHFileSubRecord' AND `operation` = 'fetchACHFileSubRecords';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'Execute' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'Execute';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'getTemplateDetailsById' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'getTemplateDetailsById';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'editACHTemplate' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'editACHTemplate';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'getAllACHTemplates' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'getAllACHTemplates';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'createACHTemplate' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'createACHTemplate';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTemplates' ,`operation` = 'deleteACHTemplate' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTemplates' AND `operation` = 'deleteACHTemplate';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'getACHTransactionDetailsById' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'getACHTransactionDetailsById';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'createACHTransaction' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'createACHTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'getAllACHTransactions' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'getAllACHTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'RejectedTransactions' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'RejectedTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'ACHTransactions' ,`operation` = 'SaveAsTemplate' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'ACHTransactions' AND `operation` = 'SaveAsTemplate';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TaxSubType' ,`operation` = 'FetchTaxSubTypes' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TaxSubType' AND `operation` = 'FetchTaxSubTypes';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TaxType' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TaxType' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TemplateRecords' ,`operation` = 'fetchTemplateRecordById' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TemplateRecords' AND `operation` = 'fetchTemplateRecordById';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TemplateRequestTypes' ,`operation` = 'FetchTemplateRequestTypes' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TemplateRequestTypes' AND `operation` = 'FetchTemplateRequestTypes';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TemplateSubRecord' ,`operation` = 'fetchTemplateSubRecords' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TemplateSubRecord' AND `operation` = 'fetchTemplateSubRecords';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TemplateTypes' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TemplateTypes' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TransactionRecords' ,`operation` = 'fetchTransactionRecordsById' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TransactionRecords' AND `operation` = 'fetchTransactionRecordsById';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TransactionSubRecord' ,`operation` = 'fetchTransactionSubRecords' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TransactionSubRecord' AND `operation` = 'fetchTransactionSubRecords';
UPDATE `service_permission_mapper` SET `service_name` = 'ACH',`object_name` = 'TransactionTypes' ,`operation` = 'get' WHERE `service_name` = 'ACHObjects' AND `object_name` = 'TransactionTypes' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalMatrix' ,`operation` = 'updateApprovalMatrixStatus' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalMatrix' AND `operation` = 'updateApprovalMatrixStatus';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalMatrix' ,`operation` = 'getApprovalMatrix' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalMatrix' AND `operation` = 'getApprovalMatrix';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalMatrix' ,`operation` = 'getApprovalMatrixByContractId' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalMatrix' AND `operation` = 'getApprovalMatrixByContractId';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalMatrix' ,`operation` = 'isApprovalMatrixDisabled' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalMatrix' AND `operation` = 'isApprovalMatrixDisabled';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalMatrix' ,`operation` = 'updateApprovalMatrix' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalMatrix' AND `operation` = 'updateApprovalMatrix';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalMatrix',`object_name` = 'ApprovalRules' ,`operation` = 'getApprovalRules' WHERE `service_name` = 'ApprovalMatrixObjects' AND `object_name` = 'ApprovalRules' AND `operation` = 'getApprovalRules';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'Counts' ,`operation` = 'getCounts' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'Counts' AND `operation` = 'getCounts';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'getRejectedACHTransactions' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'getRejectedACHTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'approveBBGeneralTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'approveBBGeneralTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'fetchAllMyPendingApprovals' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'fetchAllMyPendingApprovals';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'rejectBBGeneralTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'rejectBBGeneralTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'getACHTransactionsForApproval' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'getACHTransactionsForApproval';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'RejectedFiles' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'RejectedFiles';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'fetchAllMyApprovalHistory' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'fetchAllMyApprovalHistory';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'getACHTransactionsPendingForApproval' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'getACHTransactionsPendingForApproval';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'rejectACHTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'rejectACHTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'getACHFiles' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'getACHFiles';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'approveACHTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'approveACHTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'Reject' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'Reject';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'approveACHFile' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'approveACHFile';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'rejectACHFile' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'rejectACHFile';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'getGeneralTransactions' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'getGeneralTransactions';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyApprovals' ,`operation` = 'Approve' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyApprovals' AND `operation` = 'Approve';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'withdrawACHTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'withdrawACHTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'getGeneralTransactionsRequestedByMe' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'getGeneralTransactionsRequestedByMe';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'getRequestsHistory' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'getRequestsHistory';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'Withdraw' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'Withdraw';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'get' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'get';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'fetchAllMyRequestHistory' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'fetchAllMyRequestHistory';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'fetchAllMyPendingRequests' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'fetchAllMyPendingRequests';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'RenotifyPendingApprovalRequest' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'RenotifyPendingApprovalRequest';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'getACHTransactionsRequestedByMe' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'getACHTransactionsRequestedByMe';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'withdrawGeneralTransaction' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'withdrawGeneralTransaction';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'withdrawACHFile' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'withdrawACHFile';
UPDATE `service_permission_mapper` SET `service_name` = 'ApprovalsAndRequests',`object_name` = 'MyRequests' ,`operation` = 'getACHFilesRequestedByMe' WHERE `service_name` = 'ApprovalRequestObjects' AND `object_name` = 'MyRequests' AND `operation` = 'getACHFilesRequestedByMe';


delete from `service_permission_mapper` where `service_name`='ApprovalMatrixObjects' AND `object_name` in ('accounts', 'actions', 'approvers', 'limits','limitTypes');

UPDATE `mfaserviceconfig` SET `transactionType` = 'digitaltransfer_payment_p2p_createtransfer' WHERE `id` = '17';


-- Deleting Micro User Roles DBB-9719
UPDATE `customergroup` SET `Group_id` = 'GROUP_ADMINISTRATOR' WHERE `Group_id` = 'GROUP_MICRO_ADMINISTRATOR';
UPDATE `customergroup` SET `Group_id` = 'GROUP_AUTHORIZER' WHERE `Group_id` = 'GROUP_MICRO_AUTHORIZER';
UPDATE `customergroup` SET `Group_id` = 'GROUP_CREATOR' WHERE `Group_id` = 'GROUP_MICRO_CREATOR';
UPDATE `customergroup` SET `Group_id` = 'GROUP_VIEWER' WHERE `Group_id` = 'GROUP_MICRO_VIEWER';

DELETE FROM `groupentitlement` WHERE (`Group_id` = 'GROUP_MICRO_ADMINISTRATOR');
DELETE FROM `groupentitlement` WHERE (`Group_id` = 'GROUP_MICRO_AUTHORIZER');
DELETE FROM `groupentitlement` WHERE (`Group_id` = 'GROUP_MICRO_CREATOR');
DELETE FROM `groupentitlement` WHERE (`Group_id` = 'GROUP_MICRO_VIEWER');

DELETE FROM `groupservicedefinition` WHERE (`Group_id` = 'GROUP_MICRO_ADMINISTRATOR');
DELETE FROM `groupservicedefinition` WHERE (`Group_id` = 'GROUP_MICRO_AUTHORIZER');
DELETE FROM `groupservicedefinition` WHERE (`Group_id` = 'GROUP_MICRO_CREATOR');
DELETE FROM `groupservicedefinition` WHERE (`Group_id` = 'GROUP_MICRO_VIEWER');

DELETE FROM `groupactionlimit` WHERE (`Group_id` = 'GROUP_MICRO_ADMINISTRATOR');
DELETE FROM `groupactionlimit` WHERE (`Group_id` = 'GROUP_MICRO_AUTHORIZER');
DELETE FROM `groupactionlimit` WHERE (`Group_id` = 'GROUP_MICRO_CREATOR');
DELETE FROM `groupactionlimit` WHERE (`Group_id` = 'GROUP_MICRO_VIEWER');

DELETE FROM `membergroup` WHERE (`id` = 'GROUP_MICRO_ADMINISTRATOR');
DELETE FROM `membergroup` WHERE (`id` = 'GROUP_MICRO_AUTHORIZER');
DELETE FROM `membergroup` WHERE (`id` = 'GROUP_MICRO_CREATOR');
DELETE FROM `membergroup` WHERE (`id` = 'GROUP_MICRO_VIEWER');
-- DBB-9719 end

UPDATE `service_permission_mapper` SET `service_name` = 'CardManagementServices',`object_name` = 'ChangePIN' ,`operation` = 'updateCreditCardPin' WHERE `service_name` = 'RBObjects' AND `object_name` = 'Cards' AND `operation` = 'createCardRequest';

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('10cd0357-61cb-4ca0-9980-b47d7c43fd9a', 'CustomerManagementObjService', 'Customer', 'getInfinityAccounts', 'ViewCustomer');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1c50a77d-dd23-4c59-92ad-e58d241b6e43', 'BankProductManagment', 'bankFacility', 'getFacilities', 'ViewProduct,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('c63d4ce6-916d-4404-95c6-16ad619e2e7d', 'BankProductManagment', 'bankFacility', 'createFacility', 'CreateUpdateProduct');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('746e8082-64d3-48e4-ad31-9b77b0c4278a', 'BankProductManagment', 'bankFacility', 'editFacility', 'CreateUpdateProduct');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('2dc0ca1d-a618-4fb2-b158-ff1e9f5430ec', 'FeatureObjService', 'feature', 'getAccountLevelFeatureAction', 'ViewFeatureConfig');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('0c4bdb17-5f14-4a71-8333-f8f764bfdd6a', 'BankProductManagment', 'bankProduct', 'createProduct', 'CreateUpdateProduct');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('e89fcdde-6a4b-4db6-822f-babbd7e59eb6', 'BankProductManagment', 'bankProduct', 'getProducts', 'ViewProduct,API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7b764dff-cc14-41a6-bad1-0c4a2fd628b2', 'BankProductManagment', 'bankProduct', 'updateProduct', 'CreateUpdateProduct');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('0e0ac81d-7b3e-471b-8791-f03dce67e31f', 'BankProductManagment', 'bankProduct', 'deleteProductFacility', 'CreateUpdateProduct');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7500ebaa-d513-4811-856a-3390129135d6', 'BankProductManagment', 'bankProduct', 'updateProductFacility', 'CreateUpdateProduct');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('0a8cb567-9ba3-4ca1-ac67-b6b0dee1baec', 'BankProductManagment', 'bankProduct', 'createProductFacility', 'CreateUpdateProduct');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID190', 'PID45', 'FUNDING_AUTHENTICATION', 'ACCESS_CASH_POSITION', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID191', 'PID45', 'PROSPECT_EXPIRY', 'ACCESS_ENGAGE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID192', 'PID45', 'RESUME_AUTHENTICATION', 'CONVERSATIONAL_BANKING', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID193', 'PID45', 'USER_VERIFICATION', 'BULK_PAYMENT_FILES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID194', 'PID45', 'ACCESS_CASH_POSITION', 'BULK_PAYMENT_FILES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID195', 'PID45', 'ACCESS_ENGAGE', 'BULK_PAYMENT_FILES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID196', 'PID45', 'ACTIVATE_CHAT_BOT', 'BULK_PAYMENT_FILES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID197', 'PID45', 'BULK_PAYMENT_FILES_MULTI_UPLOAD_CSV', 'BULK_PAYMENT_FILES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID198', 'PID45', 'BULK_PAYMENT_FILES_MULTI_UPLOAD_XML', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID199', 'PID45', 'BULK_PAYMENT_FILES_SINGLE_UPLOAD_CSV', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID205', 'PID45', 'BULK_PAYMENT_REQUEST_ADD_PO_SAMEBANK', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID206', 'PID45', 'BULK_PAYMENT_REQUEST_APPROVE', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID207', 'PID45', 'BULK_PAYMENT_REQUEST_CANCEL', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID208', 'PID45', 'BULK_PAYMENT_REQUEST_EDIT', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID209', 'PID45', 'BULK_PAYMENT_REQUEST_EDIT_PO', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID211', 'PID45', 'BULK_PAYMENT_REQUEST_SUBMIT', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID212', 'PID45', 'BULK_PAYMENT_REQUEST_VIEW', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID213', 'PID45', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_DOMESTIC', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID214', 'PID45', 'BULK_PAYMENT_TEMPLATE_ADD_PO_EXTERNAL_INATIONAL', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID215', 'PID45', 'BULK_PAYMENT_TEMPLATE_ADD_PO_SAMEBANK', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID216', 'PID45', 'BULK_PAYMENT_TEMPLATE_DELETE', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID217', 'PID45', 'BULK_PAYMENT_TEMPLATE_DOWNLOAD', 'BULK_PAYMENT_TEMPLATE', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID218', 'PID45', 'BULK_PAYMENT_TEMPLATE_EDIT', 'CALL_BANK', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID219', 'PID45', 'BULK_PAYMENT_TEMPLATE_MULTIPLE_CREATE', 'CARD_MANAGEMENT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID220', 'PID45', 'BULK_PAYMENT_TEMPLATE_SINGLE_CREATE', 'CARD_MANAGEMENT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID221', 'PID45', 'BULK_PAYMENT_TEMPLATE_VIEW', 'CARD_MANAGEMENT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID222', 'PID45', 'CALL_BANK', 'CARD_MANAGEMENT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID223', 'PID45', 'CARD_MANAGEMENT_ADD_CARD_APPLE_WALLET', 'CHEQUE_BOOK_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID224', 'PID45', 'CARD_MANAGEMENT_ADD_CARD_GOOGLE_PAY', 'CUSTOM_VIEW', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID225', 'PID45', 'CARD_MANAGEMENT_ADD_CARD_SAMSUNG_PAY', 'INITIATE_FUNDING', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID226', 'PID45', 'CARD_MANAGEMENT_APPLY_FOR_DEBIT_CARD', 'FX_RATES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID227', 'PID45', 'CHEQUE_BOOK_REQUEST_APPROVE', 'FX_RATES', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID228', 'PID45', 'CUSTOM_VIEW_MANAGE', 'PROSPECT_EXPIRY', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID229', 'PID45', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_DOMESTIC', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID230', 'PID45', 'FX_RATES_VIEW_CALCULATOR', 'DIRECT_DEBIT', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID231', 'PID45', 'SKIP_NEXT_PAYMENT', 'USER_VERIFICATION', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID232', 'PID45', 'VIEW_COMBINED_STATEMENTS', 'MANAGE_ACCOUNT_STATEMENTS', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID233', 'PID45', 'VIEW_ESTATEMENTS', 'MANAGE_ACCOUNT_STATEMENTS', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID234', 'PID45', 'WITHDRAW_CASH_CARDLESS_CASH', 'WITHDRAW_CASH', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID235', 'PID45', 'WITHDRAW_CASH_VIEW_SUMMARY', 'WITHDRAW_CASH', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID236', 'PID45', 'BULK_PAYMENT_FILES_SINGLE_UPLOAD_XML', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID237', 'PID45', 'BULK_PAYMENT_FILES_VIEW', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID238', 'PID45', 'BULK_PAYMENT_REQUEST_ADD_PO', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID239', 'PID45', 'FX_RATES_VIEW', 'RESUME_AUTHENTICATION', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID240', 'PID45', 'BULK_PAYMENT_REQUEST_ADD_PO_EXTERNAL_INATIONAL', 'BULK_PAYMENT_REQUEST', '1');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`) VALUES ('CAID241', 'PID45', 'BULK_PAYMENT_REQUEST_REMOVE_PO', 'BULK_PAYMENT_TEMPLATE', '1');

INSERT INTO `rrole` (`id`) VALUES ('ACTIVATE_CHAT_BOT-VIEW');
UPDATE `featureaction` SET `Rrole_id` = 'ACTIVATE_CHAT_BOT-VIEW' WHERE (`id` = 'ACTIVATE_CHAT_BOT');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`) VALUES ('e68e9926-eeda-11eb-9a03-0242ac130003', 'f85d8392-9afe-4128-b23e-a370f138784f', 'ACTIVATE_CHAT_BOT');

UPDATE `mfaserviceconfig` SET `transactionType` = 'consentmanagement_psd2consent_updateconsent' WHERE (`id` = '26');

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_BUSINESS', 'CDP_CONSENT_VIEW');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`) VALUES ('TYPE_ID_BUSINESS', 'CDP_CONSENT_EDIT');

DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RetailRM') and (`Permission_id` = 'PID300');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RetailOps') and (`Permission_id` = 'PID300');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RetailSupervisor') and (`Permission_id` = 'PID300');
DELETE FROM `rolepermission` WHERE (`Role_id` = 'RID_RetailUW') and (`Permission_id` = 'PID300');
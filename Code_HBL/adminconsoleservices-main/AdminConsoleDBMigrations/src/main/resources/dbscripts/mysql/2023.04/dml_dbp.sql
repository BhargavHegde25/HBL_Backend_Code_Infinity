INSERT INTO `rrole` (`id`) VALUES ("ADD_USER_ANOTHER_ENTITY");

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`, `status`, `accesspolicyId`, `actionlevelId`) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ADD_USER_ANOTHER_ENTITY','Add user to another entity','Add user to another entity',0,0,0,0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP,'0','SID_ACTION_ACTIVE','ADMIN','CUSTOMERID_LEVEL');


INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'de-DE' ,'Add user to another entity','Add user to another entity' );
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'en-GB' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'en-US' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'es-ES' ,'Add user to another entity','Add user to another entity' ); 
INSERT INTO `actiondisplaynamedescription` (`Action_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('ADD_USER_ANOTHER_ENTITY' ,'fr-FR' ,'Add user to another entity','Add user to another entity' );

INSERT INTO `featureactionroletype` (`RoleType_id`,`Action_id`,`createdby`,`modifiedby`,`createdts`,`lastmodifiedts`,`synctimestamp`,`softdeleteflag`) VALUES ('TYPE_ID_BUSINESS','ADD_USER_ANOTHER_ENTITY',null,null,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,'0');



INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0'); 

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0'); 

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '5801fa32-a416-45b6-af01-b22e2de93777', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'f85d8392-9afe-4128-b23e-a370f138784f', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'ADD_USER_ANOTHER_ENTITY', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


INSERT INTO `dependentactions` (`actionId`,`dependentactionId`,`featureId`,`actionName`,`featureName`) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','USER_MANAGEMENT','Add user to another entity','Add user to another entity');
	
INSERT INTO `dependentactions` (`actionId`,`dependentactionId`,`featureId`,`actionName`,`featureName`) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT_VIEW','USER_MANAGEMENT','Add user to another entity','Add user to another entity');



INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (UUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CLOSE_ACCOUNT-CREATE',null ,null);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`) VALUES (UUID(),'GROUP_ADMINISTRATOR', 'CLOSE_ACCOUNT-CREATE', null, null);
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `limitTypeId`, `value`) VALUES (UUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'VIEW_CLOSED_ACCOUNT',null ,null);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`) VALUES (UUID(),'GROUP_ADMINISTRATOR', 'VIEW_CLOSED_ACCOUNT', null, null);

-- DBP BUNDLE CONFIGURATION: RECEIVABLE BILLS CHART DATA
INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`)
VALUES ('TSF_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'RECEIVABLES_BILLS_CHART_DATA', 'Receivable Bills Chart Data', '{\"billStatus\":{\"Draft\":\"Draft\",\"Returned by Bank\":\"Returned by Bank\",\"Approved\":\"Approved\",\"Processing by Bank\":\"Processing\",\"Submitted to Bank\":\"Processing\",\"New\":\"Processing\"},\"billColorCode\":{\"Single\":\"#4176A4\",\"Batch\":\"#A0BBD2\"}}', 'CLIENT', '1');

-- SECURE MESSAGE CATEGORY / TRADESUPPLYFINANCE / BILLS
INSERT INTO `requestcategory` (`id`, `Name`) VALUES ('RCID_TSF_BILLS', 'Trade Supply Finance - Bills');

-- FEATURE / TRADESUPPLYFINANCE / RECEIVABLE BILLS
INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `Service_Fee`, `DisplaySequence`, `isPrimary`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'Receivable Bills', 'View & Manage the Receivable Bills', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'de-DE', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'en-GB', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'en-US', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'es-ES', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS', 'fr-FR', 'Receivable Bills', 'View & Manage the Receivable Bills', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureroletype` (`RoleType_id`,  `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / CREATE
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLE_BILLS_CREATE', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_CREATE', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_CREATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_CREATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_CREATE', 'de-DE', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_CREATE', 'en-GB', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_CREATE', 'en-US', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_CREATE', 'es-ES', 'Receivable Bills Create', 'Receivable Bills Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_CREATE', 'fr-FR', 'Receivable Bills Create', 'Receivable Bills Create');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / UPDATE
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_UPDATE', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_UPDATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'de-DE', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'en-GB', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'en-US', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'es-ES', 'Receivable Bills Update', 'Receivable Bills Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_UPDATE', 'fr-FR', 'Receivable Bills Update', 'Receivable Bills Update');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE BILLS / VIEW
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLE_BILLS_VIEW', 'RECEIVABLE_BILLS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLE_BILLS_VIEW', 'Manage the Receivable Bills', 'View the Receivable Bills', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLE_BILLS_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLE_BILLS_VIEW', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLE_BILLS_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLE_BILLS_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_VIEW', 'de-DE', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_VIEW', 'en-GB', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_VIEW', 'en-US', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_VIEW', 'es-ES', 'Receivable Bills View', 'Receivable Bills View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLE_BILLS_VIEW', 'fr-FR', 'Receivable Bills View', 'Receivable Bills View');

-- CUSTOM VERBS / TRADESUPPLYFINANCE / ReceivableSingleBills
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf001', 'TradeSupplyFinance', 'ReceivableSingleBills', 'saveBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf002', 'TradeSupplyFinance', 'ReceivableSingleBills', 'deleteBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf003', 'TradeSupplyFinance', 'ReceivableSingleBills', 'createBill', 'RECEIVABLE_BILLS_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf004', 'TradeSupplyFinance', 'ReceivableSingleBills', 'reviseBill', 'RECEIVABLE_BILLS_UPDATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf007', 'TradeSupplyFinance', 'ReceivableSingleBills', 'requestBillCancellation', 'RECEIVABLE_BILLS_UPDATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf005', 'TradeSupplyFinance', 'ReceivableSingleBills', 'getBill', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf006', 'TradeSupplyFinance', 'ReceivableSingleBills', 'getBills', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf008', 'TradeSupplyFinance', 'ReceivableSingleBills', 'generateBillReport', 'RECEIVABLE_BILLS_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242lotsf009', 'TradeSupplyFinance', 'ReceivableSingleBills', 'generateBillsList', 'RECEIVABLE_BILLS_VIEW');

UPDATE `configurations` SET `config_value` = '{\"SAVINGS.PLAN\":\"Deposit\",\"DEPOSIT.CALL\":\"Deposit\",\"CURRENT.ACCOUNT\":\"Checking\",\"SS.ANNUAL\":\"Checking\",\"SS.ROLLOVER.01M\":\"Deposit\",\"NEGOTIABLE.LOAN\":\"Loan\",\"CURRENT.ACCOUNT.SME\":\"Checking\",\"PREMIUM.ACCOUNT\":\"Checking\",\"CURRENT.ACCOUNT.STUDENT\":\"Checking\",\"SAVINGS.ACCOUNT\":\"Savings\",\"CONS.SAVING\":\"Savings\",\"SAVINGS.SALARY.INFINITY\":\"Savings\",\"MORTGAGE.FLOATING\":\"Loan\",\"TERM.DEPOSIT\":\"Deposit\",\"CURRENT.ACCOUNT.STAFF\":\"Checking\",\"CURRENT.ACCOUNT.PREF\":\"Checking\",\"CONS.CHECKING\":\"Checking\",\"SAVINGS.ACCOUNT.WELCOME\":\"Savings\",\"SAVINGS.STANDARD.INFINITY\":\"Savings\",\"PREFER.ACCOUNT\":\"Checking\",\"STUDENT.ACCOUNT\":\"Checking\",\"MORTGAGE.FIX5Y.60LTV\":\"Loan\",\"DEPOSIT.SHORT\":\"Deposit\",\"DEPOSIT.5Y\":\"Deposit\",\"DEPOSIT.3Y\":\"Deposit\",\"ADVANCED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.PROMOTIONAL\":\"Savings\",\"SAVINGS.ACCOUNT.FCY\":\"Savings\",\"SAVINGS.ACCOUNT.MINOR\":\"Savings\",\"DEPOSIT.09M\":\"Deposit\",\"BASIC.CHECKING.ACCOUNT\":\"Checking\",\"SS.FIXED.TERM\":\"Deposit\",\"SS.MONTHLY\":\"Checking\",\"MORTGAGE\":\"Loan\",\"SS.SAVINGS.REGULAR\":\"Savings\",\"PERSONAL.LOAN\":\"Loan\",\"SS.PAYG\":\"Checking\",\"SAVINGS.PRIME.INFINITY\":\"Savings\",\"CONS.MM\":\"Savings\",\"DEPOSIT.LONG\":\"Deposit\",\"VEHICLE.LOAN\":\"Loan\",\"SAVINGS.DEFAULT\":\"Savings\",\"PREFERRED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.WLC\":\"Savings\",\"SS.SAVINGS.CHILD\":\"Savings\",\"SAVINGS.ACCOUNT.NOTICE\":\"Savings\",\"BONDS.A.6M\":\"Deposit\",\"BONDS.B.1Y\":\"Deposit\",\"BONDS.C.3Y\":\"Deposit\",\"CURRENT.ACCOUNT.GEN\":\"Checking\",\"CURRENT.ACCOUNT.LINK\":\"Checking\",\"CURRENT.DEFAULT\":\"Checking\",\"CURRENT.PARENT\":\"Checking\",\"CURRENT.PARENT.INFINITY\":\"Checking\",\"CURRENT.PARENT.PREF\":\"Checking\",\"CURRENT.PARENT.SME\":\"Checking\",\"CURRENT.PARENT.STD\":\"Checking\",\"CURRENT.SHADOW\":\"Checking\",\"DEPOSIT.03M\":\"Deposit\",\"DEPOSIT.06M\":\"Deposit\",\"DEPOSIT.12M\":\"Deposit\",\"DEPOSIT.18M\":\"Deposit\",\"DEPOSIT.2Y\":\"Deposit\",\"DEPOSIT.4Y\":\"Deposit\",\"DEPOSIT.DEFAULT\":\"Deposit\",\"DEPOSIT.MAT\":\"Deposit\",\"DEPOSIT.NEGOTIABLE\":\"Deposit\",\"DEPOSIT.PARENT\":\"Deposit\",\"EBKM.DEPOSIT\":\"Deposit\",\"EXT.BN.PARENT\":\"Deposit\",\"EXT.DEPOSIT.PARENT\":\"Deposit\",\"INSTALLMENT.12M\":\"Loan\",\"INSTALLMENT.3M\":\"Loan\",\"INSTALLMENT.6M\":\"Loan\",\"INSTALLMENT.LOAN.PARENT\":\"Loan\",\"MORTGAGE.ARM\":\"Mortgage\",\"MORTGAGE.CASHBACK\":\"Mortgage\",\"MORTGAGE.FACILITY.PARENT\":\"Mortgage\",\"MORTGAGE.FEP\":\"Mortgage\",\"MORTGAGE.LINK\":\"Mortgage\",\"MORTGAGE.OFFER\":\"Mortgage\",\"MORTGAGE.OFFSET\":\"Mortgage\",\"MORTGAGE.PARENT\":\"Mortgage\",\"MORTGAGE.SEASONAL\":\"Mortgage\",\"PERSONAL.LOAN.2W\":\"Loan\",\"PERSONAL.LOAN.FWD\":\"Loan\",\"PERSONAL.LOAN.LINK\":\"Loan\",\"SAVINGS.PACKAGE\":\"Savings\",\"SAVINGS.PARENT\":\"Savings\",\"SAVINGS.PARENT.INFINITY\":\"Savings\",\"SAVINGS.PARENT.PREF\":\"Savings\",\"SAVINGS.PARENT.STD\":\"Savings\",\"SMALL.BUSINESS.LOAN\":\"Loan\",\"SME.ACCOUNT\":\"Checking\",\"SSA.ACCOUNT\":\"Checking\",\"STAFF.ACCOUNT\":\"Savings\",\"CORP.CURRENT.ACCOUNT\":\"Checking\",\"CL.FACILITY\":\"Sprout\",\"STUDENT.LOAN\":\"Loan\",\"MORTGAGE.FACILITY\":\"mortgageFacility\",\"BB.SMALL.BUSINESS.LOAN\":\"Loan\",\"BB.PREMIUM.ACCOUNT\":\"Checking\",\"BB.STANDARD.ACCOUNT\":\"Checking\",\"BB.START.UP.ACCOUNT\":\"Checking\"}' WHERE (`configuration_id` = '172');

-- FEATURE / TRADESUPPLYFINANCE / RECEIVABLE BILLS
INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `Service_Fee`, `DisplaySequence`, `isPrimary`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'de-DE', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'en-GB', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'en-US', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'es-ES', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT', 'fr-FR', 'Receivables CSV Import', 'Create & View the Receivable Bills From CSV', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `featureroletype` (`RoleType_id`,  `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT');
-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / CREATE
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_CREATE', 'Manage the Receivable Bills by Importing CSV', 'View the Receivable Bills by Importing CSV', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_CREATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_CREATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'de-DE', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'en-GB', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'en-US', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'es-ES', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_CREATE', 'fr-FR', 'Receivable Bills by Importing CSV - Create', 'Receivable Bills by Importing CSV - Create');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / UPDATE
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_UPDATE', 'Manage the Receivable Bills by Importing CSV', 'View the Receivable Bills by Importing CSV', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_UPDATE', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_UPDATE', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'de-DE', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'en-GB', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'en-US', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'es-ES', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_UPDATE', 'fr-FR', 'Receivable Bills by Importing CSV - Update', 'Receivable Bills by Importing CSV - Update');

-- ACTION / TRADESUPPLYFINANCE / RECEIVABLE CSV IMPORT / VIEW
INSERT INTO `rrole` (`id`, `createdts`, `lastmodifiedts`, `softdeleteflag`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, 0);

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `Rrole_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `MFA_id`, `isPrimary`, `DisplaySequence`, `dependency`, `softdeleteflag`, `accesspolicyId`, `actionlevelId`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'RECEIVABLES_CSV_IMPORT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'RECEIVABLES_CSV_IMPORT_VIEW', 'Manage the Receivable Bills from Imported CSV', 'View the Receivable Bills from Imported CSV', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_ADMINISTRATOR', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, 'UID11', null, 0);
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `LimitType_id`, `value`, `createdby`, `modifiedby`, `softdeleteflag`) VALUES (uuid(), 'GROUP_CREATOR', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, 'UID11', null, 0);

INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'RECEIVABLES_CSV_IMPORT_VIEW', null, null, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO `servicedefinitionactionlimit` (`id`, `serviceDefinitionId`, `actionId`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES (uuid(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'RECEIVABLES_CSV_IMPORT_VIEW', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'de-DE', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'en-GB', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'en-US', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'es-ES', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');
INSERT INTO `actiondisplaynamedescription` (`Action_id`,  `Locale_id`,  `displayName`,  `displayDescription`) VALUES ('RECEIVABLES_CSV_IMPORT_VIEW', 'fr-FR', 'Receivable Bills from Imported CSV - View', 'Receivable Bills from Imported CSV - View');

-- CUSTOM VERBS / TRADESUPPLYFINANCE / ReceivablesCsvImport
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242locsv001', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'createBills', 'RECEIVABLES_CSV_IMPORT_CREATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242locsv002', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'submitCsvImport', 'RECEIVABLES_CSV_IMPORT_UPDATE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242locsv003', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'getCsvImportById', 'RECEIVABLES_CSV_IMPORT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242locsv004', 'TradeSupplyFinance', 'ReceivablesCsvImport', 'getCsvImports', 'RECEIVABLES_CSV_IMPORT_VIEW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('23047kia-3482-39yc-0102-0242locsv005', 'TradeSupplyFinance', 'ReceivableSingleBills', 'submitImportedBill', 'RECEIVABLES_CSV_IMPORT_UPDATE');

-- ALERT CATEGORY: ALERT_CAT_TRADESUPPLYFINANCE
INSERT INTO `dbxalertcategory` (`id`, `Name`, `accountLevel`, `status_id`, `DisplaySequence`, `defaultFrequencyId`, `defaultFrequencyTime`) VALUES ('ALERT_CAT_TRADESUPPLYFINANCE', 'Trade Supply Finance', '0', 'SID_ACTIVE', '6', 'DAILY', '10:00:00');
INSERT INTO `dbxalertcategorytext` (`AlertCategoryId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('ALERT_CAT_TRADESUPPLYFINANCE', 'en-US', 'Trade Supply Finance', 'Trade Supply Finance alerts are grouped here');

INSERT INTO `alertcategorychannel` (`ChannelID`, `AlertCategoryId`) VALUES ('CH_NOTIFICATION_CENTER', 'ALERT_CAT_TRADESUPPLYFINANCE');
INSERT INTO `alertcategorychannel` (`ChannelID`, `AlertCategoryId`) VALUES ('CH_PUSH_NOTIFICATION', 'ALERT_CAT_TRADESUPPLYFINANCE');

-- ALERT TYPE: TSF_RECEIVABLE_SINGLE_BILLS
INSERT INTO `eventconsumertypes` (`ServiceId`, `OperationId`, `EventType`) VALUES ('Alerts', 'pushAlerts', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO `eventtype` (`id`, `Name`, `ActivityType`, `Description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'Receivable bills', 'CUSTOMER', 'Receivable bills');

INSERT INTO `dbxalerttype` (`id`, `Name`, `AlertCategoryId`, `isAccountLevel`, `Status_id`, `IsGlobal`, `DisplaySequence`) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'Receivable bills', 'ALERT_CAT_TRADESUPPLYFINANCE', '0', 'SID_ACTIVE', '1', '1');
INSERT INTO `dbxalerttypetext` (`AlertTypeId`, `LanguageCode`, `DisplayName`, `Description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILLS', 'en-US', 'Receivable bills', 'Receivable bills');
INSERT INTO `alerttypeapp` (`AppId`, `AlertTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO `alerttypecustomertype` (`CustomerTypeId`, `AlertTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILLS');

INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILLS');
INSERT INTO `alerttypechannel` (`channelId`, `alertTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILLS');


-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user created a bill and submitted', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user created a bill and submitted', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'en-US', 'Corporate user created a bill and submitted', 'Corporate user created a bill and submitted');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1001', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1002', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1003', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1004', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bill has been submitted successfully', 'Corporate user created a bill and submitted');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_DELETED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user permanently deleted the record before submission for approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user permanently deleted the record before submission for approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'en-US', 'Corporate user permanently deleted the record before submission for approval', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1011', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1012', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1013', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1014', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_DELETED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bill - [#]orderId[/#] has been deleted permanently', 'Corporate user permanently deleted the record before submission for approval');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user resubmit the updated/changed bills for approval', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user resubmit the updated/changed bills for approval', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'en-US', 'Corporate user resubmit the updated/changed bills for approval', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1021', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1022', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1023', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1024', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REVISION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your revised bill had been submitted successfully', 'Corporate user resubmit the updated/changed bills for approval');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user submits a cancellation request for the bill', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user submits a cancellation request for the bill', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'en-US', 'Corporate user submits a cancellation request for the bill', 'Corporate user submits a cancellation request for the bill');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1031', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1032', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1033', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1034', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request has been submitted successfully', 'Corporate user submits a cancellation request for the bill');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user selected a file for upload to create a bill', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user selected a file for upload to create a bill', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'en-US', 'Corporate user selected a file for upload to create a bill', 'Corporate user selected a file for upload to create a bill');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1041', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your file import has done');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1042', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1043', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1044', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DONE', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your file import has done', 'Corporate user selected a file for upload to create a bill');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user uploaded a file and import has failed', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user uploaded a file and import has failed', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'en-US', 'Corporate user uploaded a file and import has failed', 'Corporate user uploaded a file and import has failed');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1051', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1052', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.', 'DESCRIPTIONN');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1053', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Unfortunately, the file import has been failed.', 'DESCRIPTIONN');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1054', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_FAILED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Unfortunately,the file import has been failed.', 'DESCRIPTIONN');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'en-US', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1061', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your file import has been deleted');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1062', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1063', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1064', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORT_DELETED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your file import has been deleted', 'Corporate user can delete the already extracted details to create bill from the file import tab before review & submit action');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Corporate user reviewed and submitted all the bill created from the file uploaded', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Corporate user reviewed and submitted all the bill created from the file uploaded', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'en-US', 'Corporate user reviewed and submitted all the bill created from the file uploaded', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1071', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1072', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1073', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1074', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_IMPORTED_BILLS_SUBMITTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your bills submitted successfully', 'Corporate user reviewed and submitted all the bill created from the file uploaded');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been returned by the bank for some changes required', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been returned by the bank for some changes required', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'en-US', 'Submitted bill had been returned by the bank for some changes required', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1081', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1082', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1083', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1084', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_RETURNED_BY_BANK', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been returned by bank', 'Submitted bill had been returned by the bank for some changes required');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been Approved by the bank', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been Approved by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'en-US', 'Submitted bill had been Approved by the bank', 'Submitted bill had been Approved by the bank');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1091', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1092', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1093', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1094', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been approved by bank', 'Submitted bill had been Approved by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submited bill had been Rejected by the bank', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submited bill had been Rejected by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'en-US', 'Submited bill had been Rejected by the bank', 'Submited bill had been Rejected by the bank');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1101', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1102', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1103', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1104', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been rejected by bank', 'Submited bill had been Rejected by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved but still it was not financed means', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved but still it was not financed means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'en-US', 'Submitted bill had been approved but still it was not financed means', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1111', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1112', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1113', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1114', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCE_REQUESTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been still in finance requested status', 'Submitted bill had been approved but still it was not financed means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_FINANCED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved but still it was financed means', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved but still it was financed means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'en-US', 'Submitted bill had been approved but still it was financed means', 'Submitted bill had been approved but still it was financed means');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1121', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1122', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1123', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1124', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_FINANCED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been financed', 'Submitted bill had been approved but still it was financed means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_SETTLED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been approved and finally it has been settled means', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been approved and finally it has been settled means', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'en-US', 'Submitted bill had been approved and finally it has been settled means', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1131', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1132', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1133', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1134', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_SETTLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been settled', 'Submitted bill had been approved and finally it has been settled means');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill had been cancelled', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill had been cancelled', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'en-US', 'Submitted bill had been cancelled', 'Submitted bill had been cancelled');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1141', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1142', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1143', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1144', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your Bill - [#]orderId[/#] has been cancelled', 'Submitted bill had been cancelled');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill for request cancellation had been approved by the bank', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill for request cancellation had been approved by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'en-US', 'Submitted bill for request cancellation had been approved by the bank', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1151', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1152', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1153', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1154', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_APPROVED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been approved', 'Submitted bill for request cancellation had been approved by the bank');

-- ALERT SUBTYPE: TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED
INSERT INTO `eventsubtype` (`id`, `eventtypeid`, `Name`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Alert when Submitted bill for request cancellation had been rejected by the bank', '0');

INSERT INTO `alertsubtype` (`id`, `AlertTypeId`, `Name`, `isAccountLevel`, `Status_id`, `isGlobal`, `recipienttype`, `isAutoSubscribeEnabled`, `externalSystem`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'TSF_RECEIVABLE_SINGLE_BILLS', 'Submitted bill for request cancellation had been rejected by the bank', '0', 'SID_ACTIVE', '1', '1', '1', '0');
INSERT INTO `alertsubtypetext` (`alertSubTypeId`, `languageCode`, `displayName`, `description`) VALUES ('TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'en-US', 'Submitted bill for request cancellation had been rejected by the bank', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO `alertsubtypeapp` (`appId`, `alertSubTypeId`) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');
INSERT INTO `alertsubtypecustomertype` (`customerTypeId`, `alertSubTypeId`) VALUES ('TYPE_ID_BUSINESS', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');

INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_NOTIFICATION_CENTER', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');
INSERT INTO `alertsubtypechannel` (`channelId`, `alertSubTypeId`) VALUES ('CH_PUSH_NOTIFICATION', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED');

INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`) VALUES ('TSF1161', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_SMS', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1162', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_EMAIL', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1163', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_PUSH_NOTIFICATION', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');
INSERT INTO `communicationtemplate` (`Id`, `LanguageCode`, `AlertSubTypeId`, `ChannelID`, `Status_id`, `Text`, `Subject`) VALUES ('TSF1164', 'en-US', 'TSF_RECEIVABLE_SINGLE_BILL_CANCELLATION_REJECTED', 'CH_NOTIFICATION_CENTER', 'SID_EVENT_SUCCESS', 'Your cancellation request for bill - [#]orderId[/#] has been rejected', 'Submitted bill for request cancellation had been rejected by the bank');

UPDATE `configurations` SET `config_value` = '[\"Unhappy with our service\",\"Dissatisfied with our Product Offering\", \"Minimum Balance\/Charges are on Higher side\",\"Other\"]' WHERE (`configuration_id` = 'ACCL_1');

UPDATE `configurations` SET `config_value` = '{\"SAVINGS.PLAN\":\"Deposit\",\"DEPOSIT.CALL\":\"Deposit\",\"CURRENT.ACCOUNT\":\"Checking\",\"SS.ANNUAL\":\"Checking\",\"SS.ROLLOVER.01M\":\"Deposit\",\"NEGOTIABLE.LOAN\":\"Loan\",\"CURRENT.ACCOUNT.SME\":\"Checking\",\"PREMIUM.ACCOUNT\":\"Checking\",\"CURRENT.ACCOUNT.STUDENT\":\"Checking\",\"SAVINGS.ACCOUNT\":\"Savings\",\"CONS.SAVING\":\"Savings\",\"SAVINGS.SALARY.INFINITY\":\"Savings\",\"MORTGAGE.FLOATING\":\"Loan\",\"TERM.DEPOSIT\":\"Deposit\",\"CURRENT.ACCOUNT.STAFF\":\"Checking\",\"CURRENT.ACCOUNT.PREF\":\"Checking\",\"CONS.CHECKING\":\"Checking\",\"SAVINGS.ACCOUNT.WELCOME\":\"Savings\",\"SAVINGS.STANDARD.INFINITY\":\"Savings\",\"PREFER.ACCOUNT\":\"Checking\",\"STUDENT.ACCOUNT\":\"Checking\",\"MORTGAGE.FIX5Y.60LTV\":\"Loan\",\"DEPOSIT.SHORT\":\"Deposit\",\"DEPOSIT.5Y\":\"Deposit\",\"DEPOSIT.3Y\":\"Deposit\",\"ADVANCED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.PROMOTIONAL\":\"Savings\",\"SAVINGS.ACCOUNT.FCY\":\"Savings\",\"SAVINGS.ACCOUNT.MINOR\":\"Savings\",\"DEPOSIT.09M\":\"Deposit\",\"BASIC.CHECKING.ACCOUNT\":\"Checking\",\"SS.FIXED.TERM\":\"Deposit\",\"SS.MONTHLY\":\"Checking\",\"MORTGAGE\":\"Loan\",\"SS.SAVINGS.REGULAR\":\"Savings\",\"PERSONAL.LOAN\":\"Loan\",\"SS.PAYG\":\"Checking\",\"SAVINGS.PRIME.INFINITY\":\"Savings\",\"CONS.MM\":\"Savings\",\"DEPOSIT.LONG\":\"Deposit\",\"VEHICLE.LOAN\":\"Loan\",\"SAVINGS.DEFAULT\":\"Savings\",\"PREFERRED.CHECKING.ACCOUNT\":\"Checking\",\"SAVINGS.ACCOUNT.WLC\":\"Savings\",\"SS.SAVINGS.CHILD\":\"Savings\",\"SAVINGS.ACCOUNT.NOTICE\":\"Savings\",\"BONDS.A.6M\":\"Deposit\",\"BONDS.B.1Y\":\"Deposit\",\"BONDS.C.3Y\":\"Deposit\",\"CURRENT.ACCOUNT.GEN\":\"Checking\",\"CURRENT.ACCOUNT.LINK\":\"Checking\",\"CURRENT.DEFAULT\":\"Checking\",\"CURRENT.PARENT\":\"Checking\",\"CURRENT.PARENT.INFINITY\":\"Checking\",\"CURRENT.PARENT.PREF\":\"Checking\",\"CURRENT.PARENT.SME\":\"Checking\",\"CURRENT.PARENT.STD\":\"Checking\",\"CURRENT.SHADOW\":\"Checking\",\"DEPOSIT.03M\":\"Deposit\",\"DEPOSIT.06M\":\"Deposit\",\"DEPOSIT.12M\":\"Deposit\",\"DEPOSIT.18M\":\"Deposit\",\"DEPOSIT.2Y\":\"Deposit\",\"DEPOSIT.4Y\":\"Deposit\",\"DEPOSIT.DEFAULT\":\"Deposit\",\"DEPOSIT.MAT\":\"Deposit\",\"DEPOSIT.NEGOTIABLE\":\"Deposit\",\"DEPOSIT.PARENT\":\"Deposit\",\"EBKM.DEPOSIT\":\"Deposit\",\"EXT.BN.PARENT\":\"Deposit\",\"EXT.DEPOSIT.PARENT\":\"Deposit\",\"INSTALLMENT.12M\":\"Loan\",\"INSTALLMENT.3M\":\"Loan\",\"INSTALLMENT.6M\":\"Loan\",\"INSTALLMENT.LOAN.PARENT\":\"Loan\",\"MORTGAGE.ARM\":\"Mortgage\",\"MORTGAGE.CASHBACK\":\"Mortgage\",\"MORTGAGE.FACILITY.PARENT\":\"Mortgage\",\"MORTGAGE.FEP\":\"Mortgage\",\"MORTGAGE.LINK\":\"Mortgage\",\"MORTGAGE.OFFER\":\"Mortgage\",\"MORTGAGE.OFFSET\":\"Mortgage\",\"MORTGAGE.PARENT\":\"Mortgage\",\"MORTGAGE.SEASONAL\":\"Mortgage\",\"PERSONAL.LOAN.2W\":\"Loan\",\"PERSONAL.LOAN.FWD\":\"Loan\",\"PERSONAL.LOAN.LINK\":\"Loan\",\"SAVINGS.PACKAGE\":\"Savings\",\"SAVINGS.PARENT\":\"Savings\",\"SAVINGS.PARENT.INFINITY\":\"Savings\",\"SAVINGS.PARENT.PREF\":\"Savings\",\"SAVINGS.PARENT.STD\":\"Savings\",\"SMALL.BUSINESS.LOAN\":\"Loan\",\"SME.ACCOUNT\":\"Checking\",\"SSA.ACCOUNT\":\"Checking\",\"STAFF.ACCOUNT\":\"Savings\",\"CORP.CURRENT.ACCOUNT\":\"Checking\",\"CL.FACILITY\":\"Sprout\",\"STUDENT.LOAN\":\"Loan\",\"MORTGAGE.FACILITY\":\"Mortgages\",\"BB.SMALL.BUSINESS.LOAN\":\"Loan\",\"BB.PREMIUM.ACCOUNT\":\"Checking\",\"BB.STANDARD.ACCOUNT\":\"Checking\",\"BB.START.UP.ACCOUNT\":\"Checking\",\"RES.SAVINGS.ACCOUNT-20200101\":\"Savings\",\"RES.NOTICE.ACCOUNT-20200101\":\"Savings\",\"RES.FCY.SAVINGS.ACCOUNT-20200101\":\"Savings\",\"RES.SAVINGS.PARENT-20200101\":\"Savings\",\"RES.CURRENT.ACCOUNT-20200101\":\"Checking\",\"RES.PREMIUM.ACCOUNT-20200101\":\"Checking\",\"RES.MULTI.CURRENCY.ACCOUNT-20200101\":\"Checking\",\"RES.CURRENT.PARENT-20200101\":\"Checking\",\"RES.MCY.SUB.ACCOUNT-20200101\":\"Checking\",\"RES.SHORT.TERM.AUTO.ROLL-20200101\":\"Deposit\",\"RES.SHORT.TERM.MANUAL.ROLL-20200101\":\"Deposit\",\"RES.LONG.TERM.FLEXI.PAY-20200101\":\"Deposit\",\"RES.LONG.TERM.HIGH.EARN-20200101\":\"Deposit\",\"RES.SAVINGS.PLAN-20200101\":\"Deposit\",\"RES.CONSUMER.LOAN-20200101\":\"Loan\",\"RES.CREDIT.LINE.PARENT-20200101\":\"Loan\",\"RES.MORTGAGE.FACILITIES-20210101\":\"Mortgages\",\"RES.10Y.FIXED.LINEAR-20210101\":\"Mortgages\",\"RES.2Y.FIXED.CONST-20210101\":\"Mortgages\",\"RES.5Y.TRACKER.CONST-20210101\":\"Mortgages\",\"RES.BNPL.FACILITY-20210101\":\"Loan\",\"RES.PAYIN.3-20210101\":\"Loan\",\"RES.PAYIN.3M-20210101\":\"Loan\",\"RES.PAST.PURCHASE-20210101\":\"Loan\",\"RES.CONSUMER.LOAN.PARENT-20200101\":\"Loan\",\"RES.CREDIT.LINE-20200101\":\"Loan\"}' WHERE (`configuration_id` = '172');

UPDATE `service_permission_mapper` SET `permissions` = 'API_ACCESS,USER_MANAGEMENT_VIEW' WHERE (`id` = 'ggf944-f522-75364-63ea-5dd627e92281h1');

UPDATE `service_permission_mapper` SET `permissions` = 'API_ACCESS,USER_MANAGEMENT_VIEW' WHERE (`id` = 'ggf944-f522-75364-63ea-5dd627e92281h2');

INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','addNewFeaturesTOContractFromSD','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','DBXCustomerCommunicationDetails','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','getCoreCustomerDetails','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','getCoreCustomerProductRolesFeatureActionLimits','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','getProductPermissions','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','Contract','getServiceDefinitionProductIdPermissions','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','EnrollmentSecurity','generateCaptchaForEnrollment','ALLOW,API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','ExternalUsers','getCustomerIdentifiers','API_ACCESS,ALLOW');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','ExternalUsers_1','enrollRetailUser','ALLOW');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','ExternalUsers_1','getInfinityUserAccountsForCorecustomer','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','ExternalUsers_1','getInfinityUserServiceDefsRoles','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','ExternalUsers_2','getAddressTypes','ALLOW,API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','search','CustomerLegalEntitiesGetOperation','ALLOW,API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','search','customerSearchByUserName','API_ACCESS');
INSERT INTO service_permission_mapper (`id`, `service_name`,`object_name`,`operation`,`permissions`) VALUES
(uuid(),'ExternalUserManagement','search','UserIdSearchOperationDetailedData','API_ACCESS');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`)VALUES (uuid(), 'Login', 'Users', 'getLegalEntities', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'Login', 'Security', 'verifyCaptcha', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'Login', 'Users', 'getFeaturesAndPermissions', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'Login', 'Users_2', 'regenerateActivationCode', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'SecureMessaging', 'Message', 'deleteAttachement', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'SecureMessaging', 'Message', 'get', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'MessageBinary', 'media', 'update', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'MessageBinary', 'media', 'get', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'MessageBinary', 'media', 'delete', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES (uuid(), 'MessageBinary', 'media', 'updateBinary', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`)
VALUES (uuid(), 'MessageBinary', 'media', 'deleteBinary', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`)VALUES (uuid(), 'ChequeManagement', 'ChequeBook', 'getChequeType', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`)VALUES (uuid(), 'ExternalUserManagement', 'ExternalUsers', 'geRequestStatus', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `operation`, `permissions`)VALUES (uuid(), 'ServiceRequestJavaService', 'triggerForStatus', 'ALLOW');



INSERT INTO "feature" ("id", "App_id", "name", "description", "Type_id", "Status_id", "Service_Fee", "DisplaySequence", "isPrimary", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'de-DE', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'en-GB', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'en-US', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'es-ES', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard', 'fr-FR', 'Supply Chain Finance-Counterparty Dashboard', 'Supply Chain Finance-Counterparty Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "featureroletype" ("RoleType_id",  "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SCF_Counterparty_Dashboard');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('SCF_Counterparty_Dashboard_VIEW', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SCF_Counterparty_Dashboard_VIEW', 'View', 'View the Counterparty Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SCF_Counterparty_Dashboard_VIEW', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'SCF_Counterparty_Dashboard_VIEW', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'SCF_Counterparty_Dashboard_VIEW', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SCF_Counterparty_Dashboard_VIEW', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'de-DE', 'Counterparty Dashboard View', 'Counterparty Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'en-GB', 'Counterparty Dashboard View', 'Counterparty Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'en-US', 'Counterparty Dashboard View', 'Counterparty Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'es-ES', 'Counterparty Dashboard View', 'Counterparty Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Counterparty_Dashboard_VIEW', 'fr-FR', 'Counterparty Dashboard View', 'Counterparty Dashboard View');


INSERT INTO "feature" ("id", "App_id", "name", "description", "Type_id", "Status_id", "Service_Fee", "DisplaySequence", "isPrimary", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'de-DE', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'en-GB', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'en-US', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'es-ES', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard', 'fr-FR', 'Supply Chain Finance-Anchor Dashboard', 'Supply Chain Finance-Anchor Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "featureroletype" ("RoleType_id",  "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SCF_Anchor_Dashboard');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('SCF_Anchor_Dashboard_VIEW', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('SCF_Anchor_Dashboard_VIEW', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SCF_Anchor_Dashboard_VIEW', 'View', 'View the Anchor Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SCF_Anchor_Dashboard_VIEW', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'SCF_Anchor_Dashboard_VIEW', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'SCF_Anchor_Dashboard_VIEW', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SCF_Anchor_Dashboard_VIEW', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Anchor_Dashboard_VIEW', 'de-DE', 'Anchor Dashboard View', 'Anchor Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Anchor_Dashboard_VIEW', 'en-GB', 'Anchor Dashboard View', 'Anchor Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Anchor_Dashboard_VIEW', 'en-US', 'Anchor Dashboard View', 'Anchor Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Anchor_Dashboard_VIEW', 'es-ES', 'Anchor Dashboard View', 'Anchor Dashboard View');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('SCF_Anchor_Dashboard_VIEW', 'fr-FR', 'Anchor Dashboard View', 'Anchor Dashboard View');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration")VALUES ('ACC_7', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'EARLY_PAYOFF_SIMULATE_DAYS', 'Simulate Statement generated for future date, up to 20 days', '20', 'CLIENT', '1');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Anchor_Funding_Request_Create', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Anchor_Funding_Request_Create', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_Funding_Request_Create', 'Create', 'Create Funding Request', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Anchor_Funding_Request_Create', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Anchor_Funding_Request_Create', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_Funding_Request_Create', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag")
 VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_Funding_Request_Create', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Create', 'de-DE', 'Create Funding Request', 'Create Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Create', 'en-GB', 'Create Funding Request', 'Create Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Create', 'en-US', 'Create Funding Request', 'Create Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Create', 'es-ES', 'Create Funding Request', 'Create Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Create', 'fr-FR', 'Create Funding Request', 'Create Funding Request for Anchor Dashboard');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Anchor_Funding_Request_View', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Anchor_Funding_Request_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_Funding_Request_View', 'View', 'View Funding Request', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Anchor_Funding_Request_View', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Anchor_Funding_Request_View', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_Funding_Request_View', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag")
 VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_Funding_Request_View', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_View', 'de-DE', 'View Funding Request', 'View Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_View', 'en-GB', 'View Funding Request', 'View Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_View', 'en-US', 'View Funding Request', 'View Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_View', 'es-ES', 'View Funding Request', 'View Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_View', 'fr-FR', 'View Funding Request', 'View Funding Request for Anchor Dashboard');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration")
VALUES ('SCF_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SCF_Document_category', 'Supply chain Finance Document Category', '{"allowedDocTypes":["pdf","jpeg","png","tiff","doc","xls","csv"],"allowedDocCategory":[{"key":"FundingRequest","displayName":"Funding Request Documents"},{"key":"SettlementInstructions","displayName":"Settlement Instructions"},{"key":"ReceivablesInfo","displayName":"Receivables Info"},{"key":"Others","displayName":"Others"}]}', 'CLIENT', '1');

INSERT INTO "service_permission_mapper"("id", "service_name", "object_name", "operation", "permissions") VALUES ('m7e34i71-3178-45em-820y-1352kml13907', 'TradeSupplyFinance', 'AnchorFundingRequest', 'getAllFundingRequests', 'Anchor_Funding_Request_View');
INSERT INTO "service_permission_mapper"("id", "service_name", "object_name", "operation", "permissions") VALUES ('m7e34i71-3178-45em-820y-1352lpo13907', 'TradeSupplyFinance', 'AnchorFundingRequest', 'saveFundingRequest', 'Anchor_Funding_Request_Create');
INSERT INTO "service_permission_mapper"("id", "service_name", "object_name", "operation", "permissions") VALUES ('m7e34i71-3178-45em-820y-1352tol13907', 'TradeSupplyFinance', 'AnchorFundingRequest', 'submitFundingRequest', 'Anchor_Funding_Request_Create');
INSERT INTO "service_permission_mapper"("id", "service_name", "object_name", "operation", "permissions") VALUES ('m7e34i71-3178-45em-820y-1352tol12344', 'TradeSupplyFinance', 'AnchorFundingRequest', 'cancelFundingRequest', 'Anchor_Funding_Request_Create');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Anchor_Funding_Request_Edit', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Anchor_Funding_Request_Edit', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_Funding_Request_Edit', 'Edit', 'Edit Funding Request', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Anchor_Funding_Request_Edit', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Anchor_Funding_Request_Edit', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_Funding_Request_Edit', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag")
 VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_Funding_Request_Edit', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Edit', 'de-DE', 'Edit Funding Request', 'Edit Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Edit', 'en-GB', 'Edit Funding Request', 'Edit Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Edit', 'en-US', 'Edit Funding Request', 'Edit Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Edit', 'es-ES', 'Edit Funding Request', 'Edit Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Edit', 'fr-FR', 'Edit Funding Request', 'Edit Funding Request for Anchor Dashboard');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Anchor_Funding_Request_Cancel', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Anchor_Funding_Request_Cancel', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_Funding_Request_Cancel', 'Cancel', 'Cancel Funding Request', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Anchor_Funding_Request_Cancel', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Anchor_Funding_Request_Cancel', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_Funding_Request_Cancel', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag")
 VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_Funding_Request_Cancel', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Cancel', 'de-DE', 'Cancel Funding Request', 'Cancel Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Cancel', 'en-GB', 'Cancel Funding Request', 'Cancel Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Cancel', 'en-US', 'Cancel Funding Request', 'Cancel Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Cancel', 'es-ES', 'Cancel Funding Request', 'Cancel Funding Request for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Anchor_Funding_Request_Cancel', 'fr-FR', 'Cancel Funding Request', 'Cancel Funding Request for Anchor Dashboard');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_COLLECTION_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_COLLECTION_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_FILE_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_FILE_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_PAYMENT_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'ACH_PAYMENT_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'BILL_PAY_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'BILL_PAY_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'DOMESTIC_WIRE_TRANSFER_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'IMPORT_LC_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTERNATIONAL_WIRE_TRANSFER_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTERNATIONAL_WIRE_TRANSFER_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CANCEL_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTER_BANK_ACCOUNT_FUND_TRANSFER_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTRA_BANK_FUND_TRANSFER_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTRA_BANK_FUND_TRANSFER_CANCEL_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'INTRA_BANK_FUND_TRANSFER_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'TRANSFER_BETWEEN_OWN_ACCOUNT_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'TRANSFER_BETWEEN_OWN_ACCOUNT_CANCEL_APPROVE', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_RETAIL', 'TRANSFER_BETWEEN_OWN_ACCOUNT_SELF_APPROVAL', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration")
VALUES ('SCF_2', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SCF_INVOICE_APPROVAL', 'Supply chain Finance Invoice Approval', '[{"Uploaded by":"Counterparty","Role":"Supplier","Approval Required":"Yes"}]', 'CLIENT', '1');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") 
VALUES ('SCF_3', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SCF_INVOICE_UPLOAD_CONFIG', 'Supply chain Finance Invoive Upload Config', '{"BILL_TYPE":["Invoice","Bill of Exchange"],"CURRENCY":["USD","EUR","AED","AMD","ARS","AUD","AZN","BGN","CAD","CHF","CNY","DKK","GBP","GEL","HKD","HUF","ILS","INR","JOD","JPY","KGS","KWD","KZT","LBP","LKR","MXN","NOK","NPR","NZD","PHP","PLN","QAR","RSD","RUB","SAR","SEK","SGD","THB","TND","TRY","TWD","UZS","XAF","XAG","XAI","XAU","XAV","XIN","ZAR"]}', 'CLIENT', '1');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Upload_Invoice_Counterparty', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Upload_Invoice_Counterparty', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Upload_Invoice_Counterparty', 'Create', 'Upload Invoices', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Upload_Invoice_Counterparty', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Upload_Invoice_Counterparty', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Upload_Invoice_Counterparty', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Upload_Invoice_Counterparty', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Counterparty', 'de-DE', 'Upload Invoices', 'Upload Invoice for the Counterparty Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Counterparty', 'en-GB', 'Upload Invoices', 'Upload Invoice for the Counterparty Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Counterparty', 'en-US', 'Upload Invoices', 'Upload Invoice for the Counterparty Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Counterparty', 'es-ES', 'Upload Invoices', 'Upload Invoice for the Counterparty Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Counterparty', 'fr-FR', 'Upload Invoices', 'Upload Invoice for the Counterparty Dashboard');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Upload_Invoice_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Upload_Invoice_Anchor', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Upload_Invoice_Anchor', 'Create', 'Upload Invoices', 1, 0, null, 0, 10, null, 0, 'CREATE', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Upload_Invoice_Anchor', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Upload_Invoice_Anchor', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Upload_Invoice_Anchor', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Upload_Invoice_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Anchor', 'de-DE', 'Upload Invoices', 'Upload Invoice for the Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Anchor', 'en-GB', 'Upload Invoices', 'Upload Invoice for the Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Anchor', 'en-US', 'Upload Invoices', 'Upload Invoice for the Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Anchor', 'es-ES', 'Upload Invoices', 'Upload Invoice for the Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Upload_Invoice_Anchor', 'fr-FR', 'Upload Invoices', 'Upload Invoice for the Anchor Dashboard');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('r7e34i71-3178-28op-ucfv-1352tol12365', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'Save', 'Upload_Invoice_Anchor');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('u7e34i71-3178-23op-ucfv-1352tol12366', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'delete', 'Upload_Invoice_Anchor');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('l7e34i71-3178-22op-ucfv-1352tol12367', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'SubmitAll', 'Upload_Invoice_Anchor');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('p7e34i71-3178-27op-ucfv-1352tol12368', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'Save', 'Upload_Invoice_Counterparty');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('f7e34i71-3178-29op-ucfv-1352tol12369', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'delete', 'Upload_Invoice_Counterparty');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('h7e34i71-3178-28op-ucfv-1352tol12370', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'SubmitAll', 'Upload_Invoice_Counterparty');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Approve_Invoices_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Approve_Invoices_Anchor', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Approve_Invoices_Anchor', 'Update', 'Approve Invoice for the Anchor Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Approve_Invoices_Anchor', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Approve_Invoices_Anchor', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Approve_Invoices_Anchor', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Approve_Invoices_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Approve_Invoices_Anchor', 'de-DE', 'Approve Invoices', 'Approve Invoices for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Approve_Invoices_Anchor', 'en-GB', 'Approve Invoices', 'Approve Invoices for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Approve_Invoices_Anchor', 'en-US', 'Approve Invoices', 'Approve Invoices for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Approve_Invoices_Anchor', 'es-ES', 'Approve Invoices', 'Approve Invoices for Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('Approve_Invoices_Anchor', 'fr-FR', 'Approve Invoices', 'Approve Invoices for Anchor Dashboard');

INSERT INTO "eventsubtype" ("id", "eventtypeid", "Name", "Description", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('LOAN_PAYOFF_SIMULATION', 'ACCOUNT_ACTION', 'Loan Payoff simulation', 'Loan Payoff simulation', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0);

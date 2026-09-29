INSERT INTO "feature" ("id", "App_id", "name", "description", "Type_id", "Status_id", "Service_Fee", "DisplaySequence", "isPrimary", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'Lending Dashboard', 'Lending Dashboard', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', null, null, 0,  null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'de-DE', 'Lending Dashboard', 'Lending Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'en-GB', 'Lending Dashboard', 'Lending Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'en-US', 'Lending Dashboard', 'Lending Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'es-ES', 'Lending Dashboard', 'Lending Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('Lending_Dashboard', 'fr-FR', 'Lending Dashboard', 'Lending Dashboard', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "featureroletype" ("RoleType_id",  "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'Lending_Dashboard');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Balance_Widget', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Balance_Widget', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Balance_Widget', 'View Balance Widget', 'View Balance Widget', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Balance_Widget', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Balance_Widget', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Balance_Widget', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Balance_Widget', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Balance_Widget', 'de-DE', 'View Balance Widget', 'View Balance Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Balance_Widget', 'en-GB', 'View Balance Widget', 'View Balance Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Balance_Widget', 'en-US', 'View Balance Widget', 'View Balance Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Balance_Widget', 'es-ES', 'View Balance Widget', 'View Balance Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Balance_Widget', 'fr-FR', 'View Balance Widget', 'View Balance Widget');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_My_Loans_Widget', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_My_Loans_Widget', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_My_Loans_Widget', 'View My Loans Widget', 'View My Loans Widget', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_My_Loans_Widget', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_My_Loans_Widget', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_My_Loans_Widget', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_My_Loans_Widget', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Widget', 'de-DE', 'View My Loans Widget', 'View My Loans Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Widget', 'en-GB', 'View My Loans Widget', 'View My Loans Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Widget', 'en-US', 'View My Loans Widget', 'View My Loans Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Widget', 'es-ES', 'View My Loans Widget', 'View My Loans Widget');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Widget', 'fr-FR', 'View My Loans Widget', 'View My Loans Widget');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_My_Loans_Tab', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_My_Loans_Tab', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_My_Loans_Tab', 'View My Loans Tab', 'View My Loans Tab', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_My_Loans_Tab', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_My_Loans_Tab', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_My_Loans_Tab', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_My_Loans_Tab', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Tab', 'de-DE', 'View My Loans Widget', 'View My Loans Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Tab', 'en-GB', 'View My Loans Widget', 'View My Loans Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Tab', 'en-US', 'View My Loans Widget', 'View My Loans Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Tab', 'es-ES', 'View My Loans Tab', 'View My Loans Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_My_Loans_Tab', 'fr-FR', 'View My Loans Tab', 'View My Loans Tab');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Upcoming_Maturity_Tab', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Upcoming_Maturity_Tab', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Upcoming_Maturity_Tab', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Upcoming_Maturity_Tab', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Upcoming_Maturity_Tab', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Upcoming_Maturity_Tab', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Upcoming_Maturity_Tab', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Maturity_Tab', 'de-DE', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Maturity_Tab', 'en-GB', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Maturity_Tab', 'en-US', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Maturity_Tab', 'es-ES', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Maturity_Tab', 'fr-FR', 'View Upcoming Maturity Tab', 'View Upcoming Maturity Tab');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Upcoming_Payments_Tab', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Upcoming_Payments_Tab', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Upcoming_Payments_Tab', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Upcoming_Payments_Tab', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Upcoming_Payments_Tab', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Upcoming_Payments_Tab', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Upcoming_Payments_Tab', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Payments_Tab', 'de-DE', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Payments_Tab', 'en-GB', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Payments_Tab', 'en-US', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Payments_Tab', 'es-ES', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Upcoming_Payments_Tab', 'fr-FR', 'View Upcoming Payments Tab', 'View Upcoming Payments Tab');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Quick_Links_Drawdown_Request', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Quick_Links_Drawdown_Request', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Quick_Links_Drawdown_Request', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Quick_Links_Drawdown_Request', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Quick_Links_Drawdown_Request', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Quick_Links_Drawdown_Request', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'de-DE', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'en-GB', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'en-US', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'es-ES', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Drawdown_Request', 'fr-FR', 'View Quick Links - Drawdown Request', 'View Quick Links - Drawdown Request');



INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Quick_Links_Payments', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Quick_Links_Payments', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Quick_Links_Payments', 'View Quick Links – Payments', 'View Quick Links – Payments', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Quick_Links_Payments', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Quick_Links_Payments', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Quick_Links_Payments', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Quick_Links_Payments', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Payments', 'de-DE', 'View Quick Links – Payments', 'View Quick Links – Payments');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Payments', 'en-GB', 'View Quick Links – Payments', 'View Quick Links – Payments');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Payments', 'en-US', 'View Quick Links – Payments', 'View Quick Links – Payments');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Payments', 'es-ES', 'View Quick Links – Payments', 'View Quick Links – Payments');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Payments', 'fr-FR', 'View Quick Links – Payments', 'View Quick Links – Payments');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('LD_View_Quick_Links_Rollover_Request', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('LD_View_Quick_Links_Rollover_Request', 'Lending_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LD_View_Quick_Links_Rollover_Request', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LD_View_Quick_Links_Rollover_Request', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'LD_View_Quick_Links_Rollover_Request', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'LD_View_Quick_Links_Rollover_Request', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'LD_View_Quick_Links_Rollover_Request', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Rollover_Request', 'de-DE', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Rollover_Request', 'en-GB', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Rollover_Request', 'en-US', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Rollover_Request', 'es-ES', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LD_View_Quick_Links_Rollover_Request', 'fr-FR', 'View Quick Links - Rollover Request', 'View Quick Links - Rollover Request');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('2bc30144-0f70-4cdf-8015-2ff01c73c264', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'VERIFY_PAYEE_CONFIG', 'Verify Payee Configurations', '{"Entity":[{"GB0010001":"Enabled", "NL0020001":"Not Required"}],"PaymentType":{"Domestic Transfer":{"PayeeVerification":"Optional", "CountryCodes":[{"GB":"Mandatory","DE":"Optional"}]},"Within Same Bank":{"PayeeVerification":"Not Required", "CountryCodes":[]},"International Transfer":{"PayeeVerification":"Not Required","CountryCodes":[]}}}', 'CLIENT', '1');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('APPORVAL_MATRIX_MANAGE-APPROVE',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL', 'APPROVAL_MATRIX', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'APPORVAL_MATRIX_MANAGE-APPROVE', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL','en-GB', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL','de-DE', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL','en-US', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL','es-ES', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL','fr-FR', 'Create Approval Matrix Manage Approval', 'Create Approval Matrix Manage Approval');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'APPORVAL_MATRIX_MANAGE_APPROVAL', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'APPORVAL_MATRIX_MANAGE_APPROVAL', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('APPORVAL_MATRIX_MANAGE_APPROVAL', 'APPROVAL_MATRIX_VIEW', 'APPROVAL_MATRIX', 'Create Approval Matrix Manage Approval', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID560', 'PID45', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'APPROVAL_MATRIX', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'APPORVAL_MATRIX_MANAGE_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('APPROVAL_MATRIX_MANAGE_SELF-APPROVE',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'APPROVAL_MATRIX', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'APPROVAL_MATRIX_MANAGE_SELF-APPROVE', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL','en-GB', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL','de-DE', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL','en-US', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL','es-ES', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL','fr-FR', 'Create Approval Matrix Manage Self Approval', 'Create Approval Matrix Manage Self Approval');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'APPROVAL_MATRIX_VIEW', 'APPROVAL_MATRIX', 'Create Approval Matrix Manage Self Approval', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID561', 'PID45', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'APPROVAL_MATRIX', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'APPROVAL_MATRIX_MANAGE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('SIGNATORY_GROUP_CREATE_EDIT-APPROVE',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'SIGNATORY_GROUP', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SIGNATORY_GROUP_CREATE_EDIT-APPROVE', 'Signatory group create edit Approve', 'Signatory group create edit Approve', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE','en-GB', 'Signatory group create edit Approve', 'Signatory group create edit Approve');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE','de-DE', 'Signatory group create edit Approve', 'Signatory group create edit Approve');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE','en-US', 'Signatory group create edit Approve', 'Signatory group create edit Approve');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE','es-ES', 'Signatory group create edit Approve', 'Signatory group create edit Approve');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE','fr-FR', 'Signatory group create edit Approve', 'Signatory group create edit Approve');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'Signatory group create edit Approve', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID562', 'PID45', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'SIGNATORY_GROUP', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_CREATE_EDIT_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF-APPROVE',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'SIGNATORY_GROUP', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SIGNATORY_GROUP_CREATE_EDIT_SELF-APPROVE', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE','en-GB', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE','de-DE', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE','en-US', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE','es-ES', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE','fr-FR', 'Signatory group create edit self Approval', 'Signatory group create edit self Approval');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'Signatory group create edit self Approval', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID563', 'PID45', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'SIGNATORY_GROUP', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_CREATE_EDIT_SELF_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('SIGNATORY_GROUP_DELETE-APPROVE',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE', 'SIGNATORY_GROUP', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SIGNATORY_GROUP_DELETE-APPROVE', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE','en-GB', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE','de-DE', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE','en-US', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE','es-ES', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_APPROVE','fr-FR', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_DELETE_APPROVE', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_DELETE_APPROVE', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('SIGNATORY_GROUP_DELETE_APPROVE', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'Delete Signatory Group Approval', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID564', 'PID45', 'SIGNATORY_GROUP_DELETE_APPROVE', 'SIGNATORY_GROUP', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_DELETE_APPROVE', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL',0);',0);

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId","isApprovalAction")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'SIGNATORY_GROUP', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'INSERT INTO "rrole" ("id","softdeleteflag") VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL',0);', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval', 0, 0, NULL, 0, 50, NULL, 0, 'APPROVE', 'CUSTOMERID_LEVEL',1);

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL','en-GB', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL','de-DE', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL','en-US', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL','es-ES', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL','fr-FR', 'Delete Signatory Group Approval', 'Delete Signatory Group Approval');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', '0');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_RETAIL', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', '0');

INSERT INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName", "featureName") VALUES ('SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'SIGNATORY_GROUP_VIEW', 'SIGNATORY_GROUP', 'Delete Signatory Group Approval', 'Beneficiary Management');

INSERT INTO "compositeaction" ("id", "Permission_id", "Action_id", "Feature_id", "isEnabled", "softdeleteflag") VALUES ('CAID565', 'PID45', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'SIGNATORY_GROUP', '1', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('1e50f8dc-72c6-4f06-8db0-8bea1de2f6a1', 'GROUP_ADMINISTRATOR', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('51b1f42d-ff99-4a3b-a93d-cb10d287ea5a', 'GROUP_CREATOR', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('aefec853-fb45-4879-b74e-8e53450c9b13', 'a759860a-683a-4d41-81f8-fbd97d53b608', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('bc5695a8-034c-4e0c-b0ee-7241f6e08b36', '5801fa32-a416-45b6-af01-b22e2de93777', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('8d1a6922-8e0c-4e94-b23c-fac9ebaf2b2f', 'f85d8392-9afe-4128-b23e-a370f138784f', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('6f774cd8-4a9d-4f5f-a5a1-143f4987f0e1', 'bef2fe82-9c21-4ccb-b599-3308de18de44', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('b4f2bc2e-ee92-4f8e-9c0b-5ebef04e5c65', '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('c1856d20-d6d3-4cd5-a8f2-19d58f5372e8', '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'SIGNATORY_GROUP_DELETE_SELF_APPROVAL', 'UID10', NULL, NULL, NULL, '0');

-- DBP BUNDLE CONFIGURATION: for Digital Wealth
INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration")
VALUES ('WEALTH_001', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'INF_WLTH_PAGE_LIMIT', 'Number of customers to display per page in Multi Customer Dashboard', '10', 'CLIENT', '1');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration")
VALUES ('WEALTH_002', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'INF_WLTH_BANK_DATE', 'Flag to indicate in fetching bank date from backend system, default value is true', 'true', 'CLIENT', '1');


INSERT INTO "feature" ("id", "App_id", "name", "description", "Type_id", "Status_id", "DisplaySequence", "isPrimary") VALUES ('LOAN_REPAY','RETAIL_AND_BUSINESS_BANKING','Loan Repayment','This feature will enable the user to do loan repayment', 'MONETARY', 'SID_FEATURE_ACTIVE', '87', '0');

INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'LOAN_REPAY');
INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_WEALTH', 'LOAN_REPAY');
INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_RETAIL', 'LOAN_REPAY');

INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription") VALUES ('LOAN_REPAY', 'en-GB', 'Loan Repayment', 'Loan Repayment');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription") VALUES ('LOAN_REPAY', 'de-DE', 'Loan Repayment', 'Loan Repayment');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription") VALUES ('LOAN_REPAY', 'en-US', 'Loan Repayment', 'Loan Repayment');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription") VALUES ('LOAN_REPAY', 'es-ES', 'Loan Repayment', 'Loan Repayment');
INSERT INTO "featuredisplaynamedescription" ("Feature_id", "Locale_id", "displayName", "displayDescription") VALUES ('LOAN_REPAY', 'fr-FR', 'Loan Repayment', 'Loan Repayment');

INSERT INTO "rrole" ("id") VALUES ('PAY_DUE_VIEW');
INSERT INTO "rrole" ("id") VALUES ('PAY_DUE_CREATE');
INSERT INTO "rrole" ("id") VALUES ('PAY_OTHER_VIEW');
INSERT INTO "rrole" ("id") VALUES ('PAY_OTHER_CREATE');
INSERT INTO "rrole" ("id") VALUES ('PAY_OFF_VIEW');
INSERT INTO "rrole" ("id") VALUES ('PAY_OFF_CREATE');
INSERT INTO "rrole" ("id") VALUES ('LOAN_REPAY_APPROVE');
INSERT INTO "rrole" ("id") VALUES ('LOAN_REPAY_SELF_APPROVAL');

INSERT INTO "mfa" ("id", "App_id", "Action_id", "FrequencyType_id", "FrequencyValue", "Status_id", "Description", "PrimaryMFAType", "SecondaryMFAType", "SMSText", "EmailSubject", "EmailBody") VALUES ('1745986963', 'RETAIL_AND_BUSINESS_BANKING', 'PAY_DUE_CREATE', 'VALUE_BASED', '1', 'SID_INACTIVE', 'Create internal transfer', 'SECURITY_QUESTIONS', 'SECURE_ACCESS_CODE', '[#]OTP[/#]', 'Your Secure Access Code', '[#]OTP[/#]');
INSERT INTO "mfa" ("id", "App_id", "Action_id", "FrequencyType_id", "FrequencyValue", "Status_id", "Description", "PrimaryMFAType", "SecondaryMFAType", "SMSText", "EmailSubject", "EmailBody") VALUES ('1745986964', 'RETAIL_AND_BUSINESS_BANKING', 'PAY_OTHER_CREATE', 'VALUE_BASED', '1', 'SID_INACTIVE', 'Create internal transfer', 'SECURITY_QUESTIONS', 'SECURE_ACCESS_CODE', '[#]OTP[/#]', 'Your Secure Access Code', '[#]OTP[/#]');
INSERT INTO "mfa" ("id", "App_id", "Action_id", "FrequencyType_id", "FrequencyValue", "Status_id", "Description", "PrimaryMFAType", "SecondaryMFAType", "SMSText", "EmailSubject", "EmailBody") VALUES ('1745986965', 'RETAIL_AND_BUSINESS_BANKING', 'PAY_OFF_CREATE', 'VALUE_BASED', '1', 'SID_INACTIVE', 'Create internal transfer', 'SECURITY_QUESTIONS', 'SECURE_ACCESS_CODE', '[#]OTP[/#]', 'Your Secure Access Code', '[#]OTP[/#]');

INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('40', 'PAY_DUE_CREATE', 'InternalTransfer');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('41', 'PAY_DUE_CREATE', 'transactionobjects_transaction_transfertoownaccounts');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('42', 'PAY_DUE_CREATE', 'transactionobjects_transaction_transfertoownaccountsedit');

INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('43', 'PAY_OTHER_CREATE', 'InternalTransfer');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('44', 'PAY_OTHER_CREATE', 'transactionobjects_transaction_transfertoownaccounts');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('45', 'PAY_OTHER_CREATE', 'transactionobjects_transaction_transfertoownaccountsedit');

INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('46', 'PAY_OFF_CREATE', 'InternalTransfer');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('47', 'PAY_OFF_CREATE', 'transactionobjects_transaction_transfertoownaccounts');
INSERT INTO "mfaserviceconfig" ("id", "serviceName", "transactionType") VALUES ('48', 'PAY_OFF_CREATE', 'transactionobjects_transaction_transfertoownaccountsedit');

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId","isApprovalAction") VALUES ('PAY_DUE_VIEW', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'PAY_DUE_VIEW', 'View Loan Pay Due', 'To view Transactions of Loan Account using Pay Due Option', '1','0', '0', '51','SID_ACTION_ACTIVE', NULL, 'VIEW', 'ACCOUNT_LEVEL','0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId", "approveFeatureAction", "isApprovalAction") VALUES ('PAY_DUE_CREATE', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'PAY_DUE_CREATE', 'Create Loan Pay Due', 'Transfer to a Loan Account using Pay Due Option', '1','1', '1745986963', '0', '51','SID_ACTION_ACTIVE', 'SINGLE_PAYMENT', 'CREATE', 'ACCOUNT_LEVEL', 'LOAN_REPAY_APPROVE', '0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId","isApprovalAction") VALUES ('PAY_OTHER_VIEW', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'PAY_OTHER_VIEW', 'View Loan Pay Other', 'To view Transactions to a  Loan Account using Pay Other Option', '1','0', '0', '51','SID_ACTION_ACTIVE', NULL, 'VIEW', 'ACCOUNT_LEVEL', '0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable","MFA_id", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId", "approveFeatureAction", "isApprovalAction") VALUES ('PAY_OTHER_CREATE', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'PAY_OTHER_CREATE', 'Create Loan Pay Other', 'Transfer to a Loan Account using Pay Other Option', '1','1','1745986964', '0', '51','SID_ACTION_ACTIVE', 'SINGLE_PAYMENT', 'CREATE', 'ACCOUNT_LEVEL', 'LOAN_REPAY_APPROVE', '0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId","isApprovalAction") VALUES ('PAY_OFF_VIEW', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'PAY_OFF_VIEW', 'View Loan Pay Off', 'View Loan Pay off', '1','0', '0', '51','SID_ACTION_ACTIVE', NULL, 'VIEW', 'ACCOUNT_LEVEL', '0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable","MFA_id", "isPrimary", "DisplaySequence","status", "limitgroupId", "accesspolicyId", "actionlevelId", "approveFeatureAction", "isApprovalAction") VALUES ('PAY_OFF_CREATE', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'MONETARY', 'PAY_OFF_CREATE', 'Create Loan Pay Off', 'Create Loan Pay off', '1','1','1745986965', '0', '51','SID_ACTION_ACTIVE', 'SINGLE_PAYMENT', 'CREATE', 'ACCOUNT_LEVEL', 'LOAN_REPAY_APPROVE', '0');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "isPrimary", "DisplaySequence","status", "accesspolicyId", "actionlevelId","isApprovalAction") VALUES ('LOAN_REPAY_APPROVE', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LOAN_REPAY_APPROVE', 'Approve Loan Repayment', 'Approve Loan Repayment', '1','0', '0', '51','SID_ACTION_ACTIVE','APPROVE', 'ACCOUNT_LEVEL', '1');
INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "isPrimary", "DisplaySequence","status", "accesspolicyId", "actionlevelId","isApprovalAction") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'LOAN_REPAY', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'LOAN_REPAY_SELF_APPROVAL', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiated Loan Repayment', '1','0', '0', '51','SID_ACTION_ACTIVE','APPROVE', 'ACCOUNT_LEVEL', '1');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_VIEW', 'de-DE', 'View Loan Pay Due', 'View Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_VIEW', 'en-GB', 'View Loan Pay Due', 'View Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_VIEW', 'en-US', 'View Loan Pay Due', 'View Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_VIEW', 'es-ES', 'View Loan Pay Due', 'View Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_VIEW', 'fr-FR', 'View Loan Pay Due', 'View Loan Pay Due');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_CREATE', 'de-DE', 'Create Loan Pay Due', 'Create Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_CREATE', 'en-GB', 'Create Loan Pay Due', 'Create Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_CREATE', 'en-US', 'Create Loan Pay Due', 'Create Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_CREATE', 'es-ES', 'Create Loan Pay Due', 'Create Loan Pay Due');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_DUE_CREATE', 'fr-FR', 'Create Loan Pay Due', 'Create Loan Pay Due');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_VIEW', 'de-DE', 'View Loan Pay Other', 'View Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_VIEW', 'en-GB', 'View Loan Pay Other', 'View Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_VIEW', 'en-US', 'View Loan Pay Other', 'View Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_VIEW', 'es-ES', 'View Loan Pay Other', 'View Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_VIEW', 'fr-FR', 'View Loan Pay Other', 'View Loan Pay Other');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_CREATE', 'de-DE', 'Create Loan Pay Other', 'Create Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_CREATE', 'en-GB', 'Create Loan Pay Other', 'Create Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_CREATE', 'en-US', 'Create Loan Pay Other', 'Create Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_CREATE', 'es-ES', 'Create Loan Pay Other', 'Create Loan Pay Other');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OTHER_CREATE', 'fr-FR', 'Create Loan Pay Other', 'Create Loan Pay Other');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_VIEW', 'de-DE', 'View Loan Pay Off', 'View Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_VIEW', 'en-GB', 'View Loan Pay Off', 'View Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_VIEW', 'en-US', 'View Loan Pay Off', 'View Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_VIEW', 'es-ES', 'View Loan Pay Off', 'View Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_VIEW', 'fr-FR', 'View Loan Pay Off', 'View Loan Pay Off');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_CREATE', 'de-DE', 'Create Loan Pay Off', 'Create Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_CREATE', 'en-GB', 'Create Loan Pay Off', 'Create Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_CREATE', 'en-US', 'Create Loan Pay Off', 'Create Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_CREATE', 'es-ES', 'Create Loan Pay Off', 'Create Loan Pay Off');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('PAY_OFF_CREATE', 'fr-FR', 'Create Loan Pay Off', 'Create Loan Pay Off');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_APPROVE', 'de-DE', 'Approve Loan Repayment', 'Approve Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_APPROVE', 'en-GB', 'Approve Loan Repayment', 'Approve Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_APPROVE', 'en-US', 'Approve Loan Repayment', 'Approve Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_APPROVE', 'es-ES', 'Approve Loan Repayment', 'Approve Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_APPROVE', 'fr-FR', 'Approve Loan Repayment', 'Approve Loan Repayment');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'de-DE', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiated Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'en-GB', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiated Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'en-US', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiated Loan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'es-ES', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiatedLoan Repayment');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") VALUES ('LOAN_REPAY_SELF_APPROVAL', 'fr-FR', 'Approve Self-initiated Loan Repayment', 'Approve Self-initiated Loan Repayment');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_DUE_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_DUE_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_OTHER_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_OTHER_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_OFF_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'PAY_OFF_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'LOAN_REPAY_APPROVE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_BUSINESS', 'LOAN_REPAY_SELF_APPROVAL');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_DUE_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_DUE_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_OTHER_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_OTHER_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_OFF_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_WEALTH', 'PAY_OFF_CREATE');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_DUE_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_DUE_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_OTHER_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_OTHER_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_OFF_VIEW');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'PAY_OFF_CREATE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'LOAN_REPAY_APPROVE');
INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id") VALUES ('TYPE_ID_RETAIL', 'LOAN_REPAY_SELF_APPROVAL');



INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_DUE_VIEW', NULL, NULL);

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OTHER_VIEW', NULL, NULL);

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '1000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value") VALUES (SYS_GUID(), 'a759860a-683a-4d41-81f8-fbd97d53b608', 'PAY_OFF_VIEW', NULL, NULL);

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value") VALUES (SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_DUE_CREATE','DAILY_LIMIT', '1000.00'); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',NULL,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_DUE_CREATE','WEEKLY_LIMIT', '5000.00','UID11',NULL,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_DUE_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',NULL,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_DUE_VIEW',NULL, NULL,'UID11',NULL,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OTHER_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OTHER_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OTHER_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OTHER_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OFF_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OFF_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OFF_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_ADMINISTRATOR','PAY_OFF_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_DUE_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_DUE_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_DUE_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_DUE_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OTHER_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OTHER_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OTHER_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OTHER_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OFF_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OFF_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OFF_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_CREATOR','PAY_OFF_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_DUE_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_DUE_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_DUE_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_DUE_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OTHER_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OTHER_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OTHER_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OTHER_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OFF_CREATE','DAILY_LIMIT', '1000.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT', '500.00','UID11',null,0 ); 
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OFF_CREATE','WEEKLY_LIMIT', '5000.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OFF_CREATE','MIN_TRANSACTION_LIMIT', '1.00','UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_AUTHORIZER','PAY_OFF_VIEW',NULL, NULL,'UID11',null,0 );

INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_VIEWER','PAY_DUE_VIEW',NULL, NULL,'UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_VIEWER','PAY_OTHER_VIEW',NULL, NULL,'UID11',null,0 );
INSERT INTO "groupactionlimit"("id","Group_id","Action_id","LimitType_id","value","createdby","modifiedby","softdeleteflag") VALUES(SYS_GUID(),'GROUP_VIEWER','PAY_OFF_VIEW',NULL, NULL,'UID11',null,0 );


INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_AUTHORIZER', 'LOAN_REPAY_APPROVE', 'UID10', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_AUTHORIZER', 'LOAN_REPAY_SELF_APPROVAL', 'UID10', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_EUROPE_GROUP', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_DUE_VIEW', NULL, NULL, 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OTHER_VIEW', NULL, NULL, 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'DEFAULT_GROUP', 'PAY_OFF_VIEW', NULL, NULL, 'Kony Dev', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_BANKING_CUSTOMERS', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_BANKING_CUSTOMERS', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_BANKING_CUSTOMERS', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_BANKING_CUSTOMERS', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_HNW_CUSTOMERS', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_HNW_CUSTOMERS', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_HNW_CUSTOMERS', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_HNW_CUSTOMERS', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_PRIORITY_MEMBERS', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_PRIORITY_MEMBERS', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GID_PRIORITY_MEMBERS', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GID_PRIORITY_MEMBERS', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_PLATINUM', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_PLATINUM', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_PLATINUM', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_PLATINUM', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_DUE_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_TESTING', 'PAY_DUE_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OTHER_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_TESTING', 'PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '100000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OFF_CREATE', 'DAILY_LIMIT', '200000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(),'GROUP_TESTING', 'PAY_OFF_CREATE', 'WEEKLY_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_TESTING', 'PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1000000.00', 'UID11', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LOAN_REPAY_APPROVE', 'UID10', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'LOAN_REPAY_SELF_APPROVAL', 'UID10', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');


INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_DUE_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_DUE_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_DUE_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OTHER_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OTHER_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OTHER_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OFF_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OFF_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','PAY_OFF_VIEW',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','LOAN_REPAY_APPROVE',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'5801fa32-a416-45b6-af01-b22e2de93777','LOAN_REPAY_SELF_APPROVAL',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_DUE_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_DUE_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_DUE_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OTHER_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OTHER_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OTHER_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OFF_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OFF_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','PAY_OFF_VIEW',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','LOAN_REPAY_APPROVE',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'707dfea8-d0fe-4154-89c3-e7d7ef2ee16a','LOAN_REPAY_SELF_APPROVAL',NULL,NULL);


INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_DUE_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_DUE_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_DUE_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OTHER_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OTHER_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OTHER_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OFF_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OFF_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','PAY_OFF_VIEW',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','LOAN_REPAY_APPROVE',NULL,NULL);
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'83c9b8d7-3715-480e-8c7d-3d6e61c00035','LOAN_REPAY_SELF_APPROVAL',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_DUE_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_DUE_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_DUE_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OTHER_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OTHER_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OTHER_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OFF_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OFF_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'bef2fe82-9c21-4ccb-b599-3308de18de44','PAY_OFF_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_DUE_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_DUE_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_DUE_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_DUE_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OTHER_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OTHER_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OTHER_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OTHER_VIEW',NULL,NULL);

INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OFF_CREATE','DAILY_LIMIT','1000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OFF_CREATE','WEEKLY_LIMIT','5000');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OFF_CREATE','MAX_TRANSACTION_LIMIT','500');
INSERT INTO "servicedefinitionactionlimit" ("id","serviceDefinitionId","actionId","limitTypeId","value") VALUES  (SYS_GUID(),'f85d8392-9afe-4128-b23e-a370f138784f','PAY_OFF_VIEW',NULL,NULL);

INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_DUE_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_DUE_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_DUE_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_DUE_CREATE', 'DAILY_LIMIT', '1000.00');

INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OTHER_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OTHER_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OTHER_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OTHER_CREATE', 'DAILY_LIMIT', '1000.00');

INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OFF_CREATE', 'MAX_TRANSACTION_LIMIT', '500.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OFF_CREATE', 'MIN_TRANSACTION_LIMIT', '1.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OFF_CREATE', 'WEEKLY_LIMIT', '5000.00');
INSERT INTO "actionlimit" ("Action_id", "LimitType_id", "value") VALUES ('PAY_OFF_CREATE', 'DAILY_LIMIT', '1000.00');

INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('PAY_DUE_CREATE','PAY_DUE_VIEW','LOAN_REPAY','Create Loan Pay Due','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('PAY_OTHER_CREATE','PAY_OTHER_VIEW','LOAN_REPAY','Create Loan Pay Other','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('PAY_OFF_CREATE','PAY_OFF_VIEW','LOAN_REPAY','Create Loan Pay Off','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_APPROVE','PAY_DUE_VIEW','LOAN_REPAY','Approve Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_APPROVE','PAY_OTHER_VIEW','LOAN_REPAY','Approve Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_APPROVE','PAY_OFF_VIEW','LOAN_REPAY','Approve Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_SELF_APPROVAL','PAY_DUE_VIEW','LOAN_REPAY','Approve Self-initiated Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_SELF_APPROVAL','PAY_OTHER_VIEW','LOAN_REPAY','Approve Self-initiated Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_SELF_APPROVAL','PAY_OFF_VIEW','LOAN_REPAY','Approve Self-initiated Loan Repayment','Loan Repayment');
INSERT IGNORE INTO "dependentactions" ("actionId", "dependentactionId", "featureId", "actionName","featureName") VALUES('LOAN_REPAY_SELF_APPROVAL','LOAN_REPAY_APPROVE','LOAN_REPAY','Approve Self-initiated Loan Repayment','Loan Repayment');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('ce845a5d-4811-4fc3-b0c3-6d660f9933f7', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'LOAN_REPAY_ORDER_INITIATION_TYPE', 'Loan Repay Order Initiation Type', 'LOANREPAY', 'CLIENT', '1');
INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('VPEM_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'VERIFY_PAYEE_ERR_MAPPING', 'Verify Payee Error Mapping', '{"ANNM":"Account name does not match","AC01":"Incorrect account number","OPTO":"Opted out of CoP scheme","CASS":"Account has been switched","CLOSE_MATCH":"Account name does not match"}', 'SERVER', '0');
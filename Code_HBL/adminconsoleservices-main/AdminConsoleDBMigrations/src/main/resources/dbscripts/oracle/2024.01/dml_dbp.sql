-- DBP BUNDLE CONFIGURATION: SMART BANKING ADVISORY
INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('SBA_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_SIMULATION_CHANGE', 'Simulation change in Smart Banking ', '{\"valueChangeInSimulation\" : 100}', 'CLIENT', '1');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('SBA_2', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_TNC_DATA', 'Smart Banking Terms and Conditions', '[{\"heading\": \"Lorem Ipsum\",\"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}, {\"heading\": \"Lorem Ipsum\",\"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}]', 'CLIENT', '1');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('SBA_3', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_LEARNMORE_DATA', 'Smart Banking Learn More Configuration', '[{\"heading\": \"LearnMore1\", \"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}, {\"heading\": \"LearnMore2\", \"description\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industrys standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has\"}]', 'CLIENT', '1');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration") VALUES ('SBA_4', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SBA_EVENTMINUTESAGO', 'Smart Banking Event Minutes Ago Configuration', '{\"ILP_CompleteEventMinutesAgo\":\360\, \"ILP_StartedEventMinutesAgo\":\60\}', 'CLIENT', '1');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('l7e34i71-3178-22op-ucfv-1352tol12372', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'GetAll', 'SCF_Anchor_Dashboard_View,Anchor_My_Invoices_View');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('h7e34i71-3178-28op-ucfv-1352tol12371', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'GetAll', 'SCF_Counterparty_Dashboard_View');

UPDATE "featureaction" SET "name" = 'Need Attention - Approve Invoice' WHERE ("id" = 'Approve_Invoices_Anchor');

UPDATE "actiondisplaynamedescription" SET "displayName" = 'Need Attention - Approve Invoice' WHERE ("Locale_id" = 'de-DE');
UPDATE "actiondisplaynamedescription" SET "displayName" = 'Need Attention - Approve Invoice' WHERE ("Locale_id" = 'en-GB');
UPDATE "actiondisplaynamedescription" SET "displayName" = 'Need Attention - Approve Invoice' WHERE ("Locale_id" = 'en-US');
UPDATE "actiondisplaynamedescription" SET "displayName" = 'Need Attention - Approve Invoice' WHERE ("Locale_id" = 'es-ES');
UPDATE "actiondisplaynamedescription" SET "displayName" = 'Need Attention - Approve Invoice' WHERE ("Locale_id" = 'fr-FR');

UPDATE "service_permission_mapper" SET "permissions" = 'SCF_Anchor_Dashboard_View,Anchor_Funding_Request_View' WHERE ("id" = 'm7e34i71-3178-45em-820y-1352kml13907');
UPDATE "service_permission_mapper" SET "permissions" = 'Anchor_Funding_Request_Create,Anchor_Funding_Request_Edit' WHERE ("id" = 'm7e34i71-3178-45em-820y-1352lpo13907');
UPDATE "service_permission_mapper" SET "permissions" = 'Anchor_Funding_Request_Cancel' WHERE ("id" = 'm7e34i71-3178-45em-820y-1352tol12344');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") 
VALUES ('Invoice_Pending_Approval_View', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Invoice_Pending_Approval_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Invoice_Pending_Approval_View', 'Need Attention - View Invoice Pending Approval', 'Need Attention for View Invoice Pending Approval', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Invoice_Pending_Approval_View', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Invoice_Pending_Approval_View', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Invoice_Pending_Approval_View', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Invoice_Pending_Approval_View', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Invoice_Pending_Approval_View', 'de-DE','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Invoice_Pending_Approval_View', 'en-GB','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Invoice_Pending_Approval_View', 'en-US','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Invoice_Pending_Approval_View', 'es-ES','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Invoice_Pending_Approval_View', 'fr-FR','Need Attention - View Invoice Pending Approval','Invoice Pending Approval');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") 
VALUES ('Reject_Invoices_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Reject_Invoices_Anchor', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Reject_Invoices_Anchor', 'Need attention - Reject Invoice', 'Reject Invoice Pending Approval', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Reject_Invoices_Anchor', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Reject_Invoices_Anchor', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Reject_Invoices_Anchor', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Reject_Invoices_Anchor', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Reject_Invoices_Anchor', 'de-DE','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Reject_Invoices_Anchor', 'en-GB','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Reject_Invoices_Anchor', 'en-US','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Reject_Invoices_Anchor', 'es-ES','Need attention - Reject Invoice','Reject Invoices');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Reject_Invoices_Anchor', 'fr-FR','Need attention - Reject Invoice','Reject Invoices');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") 
VALUES ('View_Invoice_Donut_Chart', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('View_Invoice_Donut_Chart', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'View_Invoice_Donut_Chart', 'My Invoices - View Invoice Donut Graph', 'Invoice Donut Graph for Counterparty Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'View_Invoice_Donut_Chart', null, null, 'UID11', null, 0);

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_CREATOR', 'View_Invoice_Donut_Chart', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'View_Invoice_Donut_Chart', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'View_Invoice_Donut_Chart', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('View_Invoice_Donut_Chart', 'de-DE','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('View_Invoice_Donut_Chart', 'en-GB','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('View_Invoice_Donut_Chart', 'en-US','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('View_Invoice_Donut_Chart', 'es-ES','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('View_Invoice_Donut_Chart', 'fr-FR','My Invoices - View Invoice Donut Graph','Invoice Donut Graph');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('u7e34i71-3178-23op-ucfv-1352tol12368', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'Approve', 'Approve_Invoices_Anchor');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('l7e34i71-3178-22op-ucfv-1352tol12369', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'Reject', 'Reject_Invoices_Anchor');


INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") 
VALUES ('Anchor_My_Invoices_View', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") 
VALUES ('Anchor_My_Invoices_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Anchor_My_Invoices_View', 'My Invoices - View Invoice', 'View My Invoices in Anchor Dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Anchor_My_Invoices_View', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") 
VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Anchor_My_Invoices_View', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('TYPE_ID_BUSINESS', 'Anchor_My_Invoices_View', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Anchor_My_Invoices_View', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Anchor_My_Invoices_View', 'de-DE','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Anchor_My_Invoices_View', 'en-GB','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Anchor_My_Invoices_View', 'en-US','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Anchor_My_Invoices_View', 'es-ES','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id",  "Locale_id",  "displayName",  "displayDescription") 
VALUES ('Anchor_My_Invoices_View', 'fr-FR','My Invoices - View Invoice','View My Invoices in Anchor Dashboard');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-28op-ucfv-3244sdf54265', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'DownloadSampleXlsx', 'Upload_Invoice_Anchor');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-23op-ucfv-3244sdf54266', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'ParseBulkUploadedXlsx', 'Upload_Invoice_Anchor');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-22op-ucfv-3244sdf54267', 'TradeSupplyFinance', 'AnchorFundingInvoices', 'CreateBulkInvoices', 'Upload_Invoice_Anchor');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-27op-ucfv-3244sdf54268', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'DownloadSampleXlsx', 'Upload_Invoice_Counterparty');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-29op-ucfv-3244sdf54269', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'ParseBulkUploadedXlsx', 'Upload_Invoice_Counterparty');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES ('w7e34i71-3178-28op-ucfv-3244sdf54270', 'TradeSupplyFinance', 'CounterPartyFundingInvoices', 'CreateBulkInvoices', 'Upload_Invoice_Counterparty');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocations_Anchor_View', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocations_Anchor_View', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocations_Anchor_View', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocations_Anchor_View', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocations_Anchor_View', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocations_Anchor_View', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocations_Anchor_View', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocations_Anchor_View', 'de-DE', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocations_Anchor_View', 'en-GB', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocations_Anchor_View', 'en-US', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocations_Anchor_View', 'es-ES', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocations_Anchor_View', 'fr-FR', 'Payment Allocation tab : View Payment Allocation', 'View payment allocation tab of anchor dashboard');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Edit_Submitted', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Edit_Submitted', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Edit_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'de-DE', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'en-GB', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'en-US', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'es-ES', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Edit_Submitted', 'fr-FR', 'Payment Allocation tab : Edit Payment Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Anchor_Request_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Request_Documentation', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Request_Documentation', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Request_Documentation', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Request_Documentation', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Request_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'de-DE', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'en-GB', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'en-US', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'es-ES', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Request_Documentation', 'fr-FR', 'Pending Payment Allocation : Request Documentation for Allocation', 'Request to counterparty for submission of payment allocation documents');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_Submit_Documentation', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_Submit_Documentation', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_Submit_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'de-DE', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'en-GB', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'en-US', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'es-ES', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_Submit_Documentation', 'fr-FR', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit Documentation for a particular cash receipt');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Anchor_View_Pending', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Anchor_View_Pending', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_View_Pending', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_View_Pending', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_View_Pending', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_View_Pending', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_View_Pending', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Pending', 'de-DE', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Pending', 'en-GB', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Pending', 'en-US', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Pending', 'es-ES', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Pending', 'fr-FR', 'Need Attention : View Pending Payment Allocation', 'View new cash receipts details received');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Anchor_View_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'SCF_Anchor_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Anchor_View_Submitted', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Anchor_View_Submitted', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Anchor_View_Submitted', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Anchor_View_Submitted', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Anchor_View_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'de-DE', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'en-GB', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'en-US', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'es-ES', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Anchor_View_Submitted', 'fr-FR', 'Payment Allocation tab : View Submitted Allocation', 'View allocation documents submitted by counterparty');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_Edit_Submitted', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_Edit_Submitted', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_Edit_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'de-DE', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'en-GB', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'en-US', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'es-ES', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Edit_Submitted', 'fr-FR', 'Payment Allocation Tab : Edit Submitted Allocation', 'Edit already submitted allocation Documentation for a particular cash receipt');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_Submit_Documentation', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_Submit_Documentation', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_Submit_Documentation', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'de-DE', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'en-GB', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'en-US', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'es-ES', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_Submit_Documentation', 'fr-FR', 'Pending Payment Allocation : Submit Documentation for Allocation', 'Submit allocation Documentation');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Counterparty_View', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Counterparty_View', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_View', 'Need Attention : View Payment Allocation', 'View cash receipts details', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_View', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_View', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_View', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_View', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View', 'de-DE', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View', 'en-GB', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View', 'en-US', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View', 'es-ES', 'Need Attention : View Payment Allocation', 'View cash receipts details');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View', 'fr-FR', 'Need Attention : View Payment Allocation', 'View cash receipts details');

INSERT INTO "rrole" ("id", "createdts", "lastmodifiedts", "softdeleteflag") VALUES ('Payment_Allocation_Counterparty_View_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, 0);

INSERT INTO "featureaction" ("id", "Feature_id", "App_id", "Type_id", "Rrole_id", "name", "description", "isAccountLevel", "isMFAApplicable", "MFA_id", "isPrimary", "DisplaySequence", "dependency", "softdeleteflag", "accesspolicyId", "actionlevelId") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'SCF_Counterparty_Dashboard', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Payment_Allocation_Counterparty_View_Submitted', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt', 1, 0, null, 0, 10, null, 0, 'VIEW', 'ACCOUNT_LEVEL');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'Payment_Allocation_Counterparty_View_Submitted', null, null, 'UID11', null, 0);
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "LimitType_id", "value", "createdby", "modifiedby", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_CREATOR', 'Payment_Allocation_Counterparty_View_Submitted', null, null, 'UID11', null, 0);

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "createdby", "modifiedby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES ('TYPE_ID_BUSINESS', 'Payment_Allocation_Counterparty_View_Submitted', null, null, SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');
INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '83c9b8d7-3715-480e-8c7d-3d6e61c00035', 'Payment_Allocation_Counterparty_View_Submitted', SYSTIMESTAMP, SYSTIMESTAMP, SYSTIMESTAMP, '0');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'de-DE', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'en-GB', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'en-US', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'es-ES', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription") VALUES ('Payment_Allocation_Counterparty_View_Submitted', 'fr-FR', 'Payment Allocation Tab : View Payment Allocation', 'View submitted allocation Documentation for a particular cash receipt');

INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES (SYS_GUID(), 'TradeSupplyFinance', 'PaymentAllocations', 'requestDocumentation', 'Payment_Allocation_Anchor_Request_Documentation');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES (SYS_GUID(), 'TradeSupplyFinance', 'PaymentAllocations', 'submitDocumentation', 'Payment_Allocation_Anchor_Edit_Submitted,Payment_Allocation_Anchor_Submit_Documentation,Payment_Allocation_Counterparty_Edit_Submitted,Payment_Allocation_Counterparty_Submit_Documentation');
INSERT INTO "service_permission_mapper" ("id", "service_name", "object_name", "operation", "permissions") VALUES (SYS_GUID(), 'TradeSupplyFinance', 'PaymentAllocations', 'getPaymentAllocations', 'Payment_Allocation_Anchor_View_Pending,Payment_Allocation_Anchor_View_Submitted,Payment_Allocation_Counterparty_View,Payment_Allocation_Counterparty_View_Submitted,Payment_Allocations_Anchor_View');
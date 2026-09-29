INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration", "companyLegalUnit") VALUES ('IA_65', 'INFINITY_ASSIST_CONFIG_BUNDLE', 'PREFERENCE', 'NOTIFY_TO_RETAIL_USER', 'Notify Retail user for any failures', 'rmuser', 'CLIENT', '1', 'ALL');

INSERT INTO "configurations" ("configuration_id", "bundle_id", "config_type", "config_key", "description", "config_value", "target", "isPreLoginConfiguration", "companyLegalUnit") VALUES ('IA_66', 'INFINITY_ASSIST_CONFIG_BUNDLE', 'PREFERENCE', 'NOTIFY_TO_SME_USER', 'Notify SME user for any failures', 'smermuser', 'CLIENT', '1', 'ALL');

/* Features and permissions for Smart Banking Advisory */

INSERT INTO "feature" ("id", "App_id", "name", "description", "Type_id", "Status_id", "DisplaySequence", "isPrimary", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") 
VALUES ('SBA_XAI_DASHBOARD', 'RETAIL_AND_BUSINESS_BANKING', 'Smart Banking', 'Smart Banking', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '34', 0 , CURRENT_TIMESTAMP ,CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) 
VALUES ('SBA_XAI_DASHBOARD' ,'en-GB' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) 
VALUES ('SBA_XAI_DASHBOARD' ,'de-DE' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) 
VALUES ('SBA_XAI_DASHBOARD' ,'en-US' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) 
VALUES ('SBA_XAI_DASHBOARD' ,'es-ES' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) 
VALUES ('SBA_XAI_DASHBOARD' ,'fr-FR' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featureroletype" ("RoleType_id" , "Feature_id") VALUES ('TYPE_ID_BUSINESS' , 'SBA_XAI_DASHBOARD');

INSERT INTO "rrole" ("id" , "createdts", "lastmodifiedts" , "softdeleteflag") VALUES ('CASHFLOW_PREDICTION_CHART-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO "featureaction" ("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId") 
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW', 'SBA_XAI_DASHBOARD', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'CASHFLOW_PREDICTION_CHART-VIEW', 'SBA XAI Dashboard', 'SBA XAI Dashboard', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-GB', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','de-DE', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-US', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','es-ES', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','fr-FR', 'View XAI Dashboard', 'View XAI Dashboard');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'CASHFLOW_PREDICTION_CHART_VIEW', '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CASHFLOW_PREDICTION_CHART_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

/* Features and permissions for Smart Banking Advisory */

INSERT INTO "feature"("id","App_id","name","description","Type_id","Status_id","DisplaySequence" ,"isPrimary" ,"createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" )
values ('SBA_BUSINESS_HEALTH_SCORE' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-GB' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'de-DE' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-US' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'es-ES' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'fr-FR' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE');

INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_BUSINESS_HEALTH_SCORE-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW', 'SBA_BUSINESS_HEALTH_SCORE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_BUSINESS_HEALTH_SCORE-VIEW', 'Smart Banking Business Health Score', 'Smart Banking Business Health Score', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-GB', 'View Business Health Score', 'View Business Health Score');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','de-DE', 'View Business Health Score', 'View Business Health Score');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-US', 'View Business Health Score', 'View Business Health Score');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','es-ES', 'View Business Health Score', 'View Business Health Score');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','fr-FR', 'View Business Health Score', 'View Business Health Score');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

/* Features and permissions for Smart Banking Advisory */

INSERT INTO "feature"("id","App_id","name","description","Type_id","Status_id","DisplaySequence" ,"isPrimary" ,"createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" )
values ('SBA_SIMULATION' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_SIMULATION' ,'en-GB' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_SIMULATION' ,'de-DE' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_SIMULATION' ,'en-US' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_SIMULATION' ,'es-ES' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_SIMULATION' ,'fr-FR' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION');

INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_SIMULATION-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');
INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_SIMULATION-EDIT',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_SIMULATION_VIEW', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-VIEW', 'Smart Banking Simultation', 'Smart Banking Simultation', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_VIEW','en-GB', 'View Simulation', 'View Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_VIEW','de-DE', 'View Simulation', 'View Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_VIEW','en-US', 'View Simulation', 'View Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_VIEW','es-ES', 'View Simulation', 'View Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_VIEW','fr-FR', 'View Simulation', 'View Simulation');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_SIMULATION_EDIT', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-EDIT', 'Smart Banking Simulation', 'Smart Banking Simulation', 0, 0, null, 0, 50, null, 0, 'CREATE', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_EDIT','en-GB', 'Perform Simulation', 'Perform Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_EDIT','de-DE', 'Perform Simulation', 'Perform Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_EDIT','en-US', 'Perform Simulation', 'Perform Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_EDIT','es-ES', 'Perform Simulation', 'Perform Simulation');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_SIMULATION_EDIT','fr-FR', 'Perform Simulation', 'Perform Simulation');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_VIEW', '0');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_EDIT', '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_EDIT', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
/* Features and permissions for Smart Banking Advisory */

INSERT INTO "feature"("id","App_id","name","description","Type_id","Status_id","DisplaySequence" ,"isPrimary" ,"createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" )
values ('SBA_INSIGHTS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_INSIGHTS' ,'en-GB' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_INSIGHTS' ,'de-DE' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_INSIGHTS' ,'en-US' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_INSIGHTS' ,'es-ES' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_INSIGHTS' ,'fr-FR' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS');

INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_INSIGHTS-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_INSIGHTS_VIEW', 'SBA_INSIGHTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_INSIGHTS-VIEW', 'SBA Insights', 'SBA Insights', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_INSIGHTS_VIEW','en-GB', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_INSIGHTS_VIEW','de-DE', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_INSIGHTS_VIEW','en-US', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_INSIGHTS_VIEW','es-ES', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_INSIGHTS_VIEW','fr-FR', 'Smart Banking Insights', 'Smart Banking Insights');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS_VIEW', '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_INSIGHTS_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

/* Smart Banking Advisory Featuresactions assignment for specfic role */

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "isNewAction", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'CASHFLOW_PREDICTION_CHART_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "isNewAction", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "isNewAction", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SBA_INSIGHTS_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "isNewAction", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SBA_SIMULATION_VIEW', '0', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "feature"("id","App_id","name","description","Type_id","Status_id","DisplaySequence" ,"isPrimary" ,"createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" )
values ('SBA_AID' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription" , "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_AID' ,'en-GB' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_AID' ,'de-DE' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_AID' ,'en-US' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_AID' ,'es-ES' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO "featuredisplaynamedescription" ("Feature_id" ,"Locale_id" ,"displayName" ,"displayDescription", "createdts" ,"lastmodifiedts" ,"synctimestamp" ,"softdeleteflag" ) VALUES ('SBA_AID' ,'fr-FR' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO "featureroletype" ("RoleType_id", "Feature_id") VALUES ('TYPE_ID_BUSINESS', 'SBA_AID');

INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_RECEVIABLES-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');
INSERT INTO "rrole" ("id","createdts","lastmodifiedts","softdeleteflag") VALUES ('SBA_PAYABLES-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_RECEVIABLES_VIEW', 'SBA_AID', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_RECEVIABLES-VIEW', 'Smart Banking Receviables', 'Smart Banking Receviables', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_RECEVIABLES_VIEW','en-GB', 'View Receviables', 'View Receviables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_RECEVIABLES_VIEW','de-DE', 'View Receviables', 'View Receviables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_RECEVIABLES_VIEW','en-US', 'View Receviables', 'View Receviables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_RECEVIABLES_VIEW','es-ES', 'View Receviables', 'View Receviables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_RECEVIABLES_VIEW','fr-FR', 'View Receviables', 'View Receviables');

INSERT INTO "featureaction"("id","Feature_id","App_id","Type_id","Rrole_id","name","description","isAccountLevel","isMFAApplicable","MFA_id","isPrimary","DisplaySequence","dependency","softdeleteflag","accesspolicyId","actionlevelId")
VALUES ('SBA_PAYABLES_VIEW', 'SBA_AID', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_PAYABLES-VIEW', 'Smart Banking Payables', 'Smart Banking Payables', 0, 0, null, 0, 50, null, 0, 'CREATE', 'CUSTOMERID_LEVEL');

INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_PAYABLES_VIEW','en-GB', 'View Payables', 'View Payables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_PAYABLES_VIEW','de-DE', 'View Payables', 'View Payables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_PAYABLES_VIEW','en-US', 'View Payables', 'View Payables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_PAYABLES_VIEW','es-ES', 'View Payables', 'View Payables');
INSERT INTO "actiondisplaynamedescription" ("Action_id", "Locale_id", "displayName", "displayDescription")
VALUES ('SBA_PAYABLES_VIEW','fr-FR', 'View Payables', 'View Payables');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_RECEVIABLES_VIEW', '0');

INSERT INTO "featureactionroletype" ("RoleType_id", "Action_id", "softdeleteflag")
VALUES ('TYPE_ID_BUSINESS', 'SBA_PAYABLES_VIEW', '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SBA_RECEVIABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "groupactionlimit" ("id", "Group_id", "Action_id", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), 'GROUP_ADMINISTRATOR', 'SBA_PAYABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_RECEVIABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO "servicedefinitionactionlimit" ("id", "serviceDefinitionId", "actionId", "createdby", "createdts", "lastmodifiedts", "synctimestamp", "softdeleteflag") VALUES (SYS_GUID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_PAYABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

/* Updated name and description for Smart Banking Features and permissions */

UPDATE "feature" SET "name" = 'Smart Banking AID',"description" = 'Smart Banking AID' WHERE ("id" = 'SBA_AID');
UPDATE "feature" SET "name" = 'Smart Banking Dashboard',"description" = 'Smart Banking Dashboard' WHERE ("id" = 'SBA_XAI_DASHBOARD');
UPDATE "feature" SET "name" = 'Smart Banking Business Health Score',"description" = 'Smart Banking Business Health Score' WHERE ("id" = 'SBA_BUSINESS_HEALTH_SCORE');
UPDATE "feature" SET "name" = 'Smart Banking Simulation',"description" = 'Smart Banking Simulation' WHERE ("id" = 'SBA_SIMULATION');
UPDATE "feature" SET "name" = 'Smart Banking Insights',"description" = 'Smart Banking Insights' WHERE ("id" = 'SBA_INSIGHTS');

INSERT INTO "makercheckerconfig" ("id", "expAPIOperationName", "module", "action", "approvalPermissionId", "approvalPermissionName", "keyColumnNames", "isApprovalRequired") VALUES 
('1','ContractManagementObjService_Contract_createContract','Contract Management','Create Contract','PID701','ApproveCreateContract','coreCustomerIds','1'),
('2','ContractManagementObjService_Contract_editContract','Contract Management','Edit Contract','PID702', 'ApproveUpdateContract','contractId','1'),
('3','CustomerManagementObjService_InfinityUser_createInfinityUser','Customer Management','Create Customer', 'PID703','ApproveCreateCustomer', 'coreCustomerId', '1'),
('4','CustomerManagementObjService_InfinityUser_editInfinityUser','Customer Management','Edit Customer','PID704', 'ApproveUpdateCustomer','customerId','1');

INSERT INTO "permissiontype" ("id", "Description") VALUES ('PER_TYPE_MAKERCHECKER','Permission Type Maker Checker');

INSERT INTO "permission" ("id", "Type_id", "Status_id", "Name", "Description", "isComposite", "PermissionValue") VALUES
('PID701', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateContract', 'Permission to approve a newly created contract', '0', 'TRUE'),
('PID702', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateContract', 'Permission to approve updated contract', '0', 'TRUE'),
('PID703', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveCreateCustomer', 'Permission to approve a newly created customer', '0', 'TRUE'),
('PID704', 'PER_TYPE_MAKERCHECKER', 'SID_ACTIVE', 'ApproveUpdateCustomer', 'Permission to approve updated customer', '0', 'TRUE');

INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920038', 'AccountClosure_TnC', 'Account Closure', 'Account Closure');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920039', 'EarlyPayOffSimulation_TnC', 'Early PayOff Simulation', 'Early PayOff Simulation');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920040', 'Mortgage_PartialRepayment_Simulation_TnC', 'Mortgage Partial Repayment Simulation', 'Mortgage Partial Repayment Simulation');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920041', 'Mortgage_PartialRepayment_TnC', 'Mortgage Partial Repayment', 'Mortgage Partial Repayment');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920042', 'QRPayment_Activation_TnC', 'QR Payment Activation', 'QR Payment Activation');


INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450061', '1920038', 'en-US', '1.0', 'Access to Internet Banking linked to this account will be Cancelled.<br />Destroy ATM Debit Card associated with this account.<br />
All ATM Debit Cards linked to this account will be cancelled.All the used unused not paid postdated check which are surrendered not surrendered will be treated as cancelled destroyed.<br />Any Standing Instructions Linked to this account will be cancelled.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES
('3450062', '1920039', 'en-US', '1.7', 'Early payoff simulation provides the information to the borrower on the Total remaining outstanding balance payable which would include any applicable interest, early payoff penalty amount, other charges payable.<br class=\"\"><br class=\"\">Full early repayment: This option would include the total 
remaining outstanding balance of the mortgage loan balance which must be repaid on a specific selected date. The borrower will not want to make any changes on the payment schedules.<br class=\"\"><br class=\"\">Interest fees: In case of an early repayment, borrower may be entitled to pay an interest adjustment based upon the selected date 
of repayment, this exact interest fees will be determined by the bank.<br class=\"\"><br class=\"\">Prepayment penalty : If there is a pre-payment penalty associated with the mortgage loan, it will be added as part of the simulation result generated to the borrower. It is important to please note that loan payoff simulation provides an estimation 
based on the calculations made on the opted payoff date. Actual repayment may vary on daily basis depending on factors such as Interest rate changes if any, additional payments made.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450063', '1920040', 'en-US', '1.0', 'The simulated result which is being displayed is valid till 31-12-2024.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450064', '1920041', 'en-US', '1.0', 'Partially repaying off the debt under the conditions as specified in the note, which includes the interest rate and payment schedule.<br />Indicating the property that the mortgagor (borrower) is using as security.<br />Adequately maintaining the property', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES 
('3450065', '1920042', 'en-US', '1.0', 'Please read the following agreement. It contains specific information regarding Terms and Conditions of your QR payment services.<br />Terms and Conditions for the QR Payments!<br /><div>By agreeing that you have read these Terms and Conditions and continuing eStatement / eNotices enrollment, you signify your agreement to all the terms, conditions, and notices contained or referenced in this document and accept responsibility for your use of the service. 
If you choose not to agree that you have read these Terms and Conditions, you will not be enrolled in the Service and will have no further responsibility to the Bank.Access to the Service and use of the Service is subject to all applicable federal, state and local laws and regulations. Unauthorized use of the Service or information accessed via the Service is strictly prohibited.</div>', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312368', '1920038', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312369', '1920039', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312370', '1920040', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312371', '1920041', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312372', '1920042', 'RETAIL_AND_BUSINESS_BANKING', 'admin');

INSERT INTO "rolepermission" ("Role_id", "Permission_id", "softdeleteflag") VALUES ('RID_MORTGAGE_RM', 'PID422', '0');

INSERT INTO "scf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('SCF_PAYMENT_ALLOCATIONS', '["beneficiaryId", "beneficiaryName", "createdDate", "currency", "fundingDocuments", "originalAmount", "paymentAllocationId", "receiptAmount", "senderId", "senderName", "status", "transactionId", "updatedDate", "uploadedBy", "uploadedFrom", "valueDate"]');

UPDATE "application" SET "isCountryCodeEnabled" = '1' WHERE "id" = '2';


INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920038', 'AccountClosure_TnC', 'Account Closure', 'Account Closure');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920039', 'EarlyPayOffSimulation_TnC', 'Early PayOff Simulation', 'Early PayOff Simulation');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920040', 'Mortgage_PartialRepayment_Simulation_TnC', 'Mortgage Partial Repayment Simulation', 'Mortgage Partial Repayment Simulation');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920041', 'Mortgage_PartialRepayment_TnC', 'Mortgage Partial Repayment', 'Mortgage Partial Repayment');
INSERT INTO "termandcondition" ("id", "Code", "Title", "Description") VALUES ('1920042', 'QRPayment_Activation_TnC', 'QR Payment Activation', 'QR Payment Activation');


INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450061', '1920038', 'en-US', '1.0', 'Access to Internet Banking linked to this account will be Cancelled.<br />Destroy ATM Debit Card associated with this account.<br />
All ATM Debit Cards linked to this account will be cancelled.All the used unused not paid postdated check which are surrendered not surrendered will be treated as cancelled destroyed.<br />Any Standing Instructions Linked to this account will be cancelled.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES
('3450062', '1920039', 'en-US', '1.7', 'Early payoff simulation provides the information to the borrower on the Total remaining outstanding balance payable which would include any applicable interest, early payoff penalty amount, other charges payable.<br class=\"\"><br class=\"\">Full early repayment: This option would include the total 
remaining outstanding balance of the mortgage loan balance which must be repaid on a specific selected date. The borrower will not want to make any changes on the payment schedules.<br class=\"\"><br class=\"\">Interest fees: In case of an early repayment, borrower may be entitled to pay an interest adjustment based upon the selected date 
of repayment, this exact interest fees will be determined by the bank.<br class=\"\"><br class=\"\">Prepayment penalty : If there is a pre-payment penalty associated with the mortgage loan, it will be added as part of the simulation result generated to the borrower. It is important to please note that loan payoff simulation provides an estimation 
based on the calculations made on the opted payoff date. Actual repayment may vary on daily basis depending on factors such as Interest rate changes if any, additional payments made.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450063', '1920040', 'en-US', '1.0', 'The simulated result which is being displayed is valid till 31-12-2024.', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES ('3450064', '1920041', 'en-US', '1.0', 'Partially repaying off the debt under the conditions as specified in the note, which includes the interest rate and payment schedule.<br />Indicating the property that the mortgagor (borrower) is using as security.<br />Adequately maintaining the property', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditiontext" ("id", "TermAndConditionId", "LanguageCode", "Version_Id", "Content", "ContentType_id", "ContentModifiedBy", "Status_id") VALUES 
('3450065', '1920042', 'en-US', '1.0', 'Please read the following agreement. It contains specific information regarding Terms and Conditions of your QR payment services.<br />Terms and Conditions for the QR Payments!<br /><div>By agreeing that you have read these Terms and Conditions and continuing eStatement / eNotices enrollment, you signify your agreement to all the terms, conditions, and notices contained or referenced in this document and accept responsibility for your use of the service. 
If you choose not to agree that you have read these Terms and Conditions, you will not be enrolled in the Service and will have no further responsibility to the Bank.Access to the Service and use of the Service is subject to all applicable federal, state and local laws and regulations. Unauthorized use of the Service or information accessed via the Service is strictly prohibited.</div>', 'TEXT', 'admin', 'SID_TANDC_ACTIVE');

INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312368', '1920038', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312369', '1920039', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312370', '1920040', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312371', '1920041', 'RETAIL_AND_BUSINESS_BANKING', 'admin');
INSERT INTO "termandconditionapp" ("id", "TermAndConditionId", "AppId", "createdby") VALUES ('2312372', '1920042', 'RETAIL_AND_BUSINESS_BANKING', 'admin');

INSERT INTO "rolepermission" ("Role_id", "Permission_id", "softdeleteflag") VALUES ('RID_MORTGAGE_RM', 'PID422', '0');

INSERT INTO "scf_records_module_configurations" ("module_id", "allowed_fields") VALUES ('SCF_PAYMENT_ALLOCATIONS', '["beneficiaryId", "beneficiaryName", "createdDate", "currency", "fundingDocuments", "originalAmount", "paymentAllocationId", "receiptAmount", "senderId", "senderName", "status", "transactionId", "updatedDate", "uploadedBy", "uploadedFrom", "valueDate"]');

UPDATE "configurations" SET "config_value" = '{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings","BONDS.A.6M":"Deposit","BONDS.B.1Y":"Deposit","BONDS.C.3Y":"Deposit","CURRENT.ACCOUNT.GEN":"Checking","CURRENT.ACCOUNT.LINK":"Checking","CURRENT.DEFAULT":"Checking","CURRENT.PARENT":"Checking","CURRENT.PARENT.INFINITY":"Checking","CURRENT.PARENT.PREF":"Checking","CURRENT.PARENT.SME":"Checking","CURRENT.PARENT.STD":"Checking","CURRENT.SHADOW":"Checking","DEPOSIT.03M":"Deposit","DEPOSIT.06M":"Deposit","DEPOSIT.12M":"Deposit","DEPOSIT.18M":"Deposit","DEPOSIT.2Y":"Deposit","DEPOSIT.4Y":"Deposit","DEPOSIT.DEFAULT":"Deposit","DEPOSIT.MAT":"Deposit","DEPOSIT.NEGOTIABLE":"Deposit","DEPOSIT.PARENT":"Deposit","EBKM.DEPOSIT":"Deposit","EXT.BN.PARENT":"Deposit","EXT.DEPOSIT.PARENT":"Deposit","INSTALLMENT.12M":"Loan","INSTALLMENT.3M":"Loan","INSTALLMENT.6M":"Loan","INSTALLMENT.LOAN.PARENT":"Loan","MORTGAGE.ARM":"Mortgage","MORTGAGE.CASHBACK":"Mortgage","MORTGAGE.FACILITY.PARENT":"Mortgage","MORTGAGE.FEP":"Mortgage","MORTGAGE.LINK":"Mortgage","MORTGAGE.OFFER":"Mortgage","MORTGAGE.OFFSET":"Mortgage","MORTGAGE.PARENT":"Mortgage","MORTGAGE.SEASONAL":"Mortgage","PERSONAL.LOAN.2W":"Loan","PERSONAL.LOAN.FWD":"Loan","PERSONAL.LOAN.LINK":"Loan","SAVINGS.PACKAGE":"Savings","SAVINGS.PARENT":"Savings","SAVINGS.PARENT.INFINITY":"Savings","SAVINGS.PARENT.PREF":"Savings","SAVINGS.PARENT.STD":"Savings","SMALL.BUSINESS.LOAN":"Loan","SME.ACCOUNT":"Checking","SSA.ACCOUNT":"Checking","STAFF.ACCOUNT":"Savings","CORP.CURRENT.ACCOUNT":"Checking","CL.FACILITY":"Sprout","STUDENT.LOAN":"Loan","MORTGAGE.FACILITY":"mortgageFacility","BB.SMALL.BUSINESS.LOAN":"Loan","BB.PREMIUM.ACCOUNT":"Checking","BB.STANDARD.ACCOUNT":"Checking","BB.START.UP.ACCOUNT":"Checking","RES.SAVINGS.ACCOUNT":"Savings","RES.NOTICE.ACCOUNT":"Savings","RES.FCY.SAVINGS.ACCOUNT":"Savings","RES.SAVINGS.PARENT":"Savings","RES.CURRENT.ACCOUNT":"Checking","RES.PREMIUM.ACCOUNT":"Checking","RES.MULTI.CURRENCY.ACCOUNT":"Checking","RES.CURRENT.PARENT":"Checking","RES.MCY.SUB.ACCOUNT":"Checking","RES.SHORT.TERM.AUTO.ROLL":"Deposit","RES.SHORT.TERM.MANUAL.ROLL":"Deposit","RES.LONG.TERM.FLEXI.PAY":"Deposit","RES.LONG.TERM.HIGH.EARN":"Deposit","RES.SAVINGS.PLAN":"Deposit","RES.CONSUMER.LOAN":"Loan","RES.CREDIT.LINE.PARENT":"Loan","RES.MORTGAGE.FACILITIES":"Mortgage","RES.10Y.FIXED.LINEAR":"Mortgage","RES.2Y.FIXED.CONST":"Mortgage","RES.5Y.TRACKER.CONST":"Mortgage","RES.BNPL.FACILITY":"Loan","RES.PAYIN.3":"Loan","RES.PAYIN.3M":"Loan","RES.PAST.PURCHASE":"Loan","RES.CONSUMER.LOAN.PARENT":"Loan","RES.CREDIT.LINE":"Loan"}' WHERE ("configuration_id" = '172');
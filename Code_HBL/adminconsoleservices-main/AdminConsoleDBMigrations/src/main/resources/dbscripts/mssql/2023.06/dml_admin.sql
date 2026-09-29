INSERT INTO [${dbxschemaname}].[service_permission_mapper] ([id],[service_name],[object_name],[operation],[permissions]) VALUES ('34fad7ec-d2ff-4142-b5b6-9c7fa656049c','Utility','Application','getServerTimeZoneOffset','ALLOW');

GO

UPDATE [${dbxschemaname}].[privacypolicy] SET [Description] = 'At Temenos Digital DBX Bank, the safeguarding of your account information is our top priority. We strive to keep our members informed on current security issues, as well as providing the tools to help protect yourself. Please check back periodically to view new information as it becomes available.<br /><h2>Your Account Security</h2><br />We protect your online security. Keeping financial and personal information about you secure and confidential is one of our most important responsibilities. Our systems are protected, so information remains secure.<ol><li>Computer virus protection detects and prevents computer viruses from entering our computer network systems.</li><li>Firewalls block unauthorized access by individuals or networks. Firewalls are just one way we protect our computer network systems that interact with the Internet.</li><li>Secure transmissions ensure information remains confidential. Temenos Digital DBX Bank uses encryption technology such as Secure Socket Layer (SSL) on its Web sites to securely transmit information between you and the bank.</li><li>Send secure e-mail to almost any department in the bank through the Contact Us section. Because an Internet e-mail response back to you may not be secure, we will not include confidential account information in an e-mail response. In addition, you will never be asked for confidential information, such as passwords or PINs, through e-mail. Besides e-mail, you can contact us by phone or visiting any branch.</li><li>Regular evaluations of our security features and continuous research of new advances in security technology ensure that your personal information is protected.</li></ol>' WHERE [id] = 'PRIV_POL_ID1';

GO

/* Features and permissions for Smart Banking Advisory */

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'en-GB' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'de-DE' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'en-US' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'es-ES' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_XAI_DASHBOARD' ,'fr-FR' ,'Smart Banking XAI Dashboard' ,'Smart Banking XAI Dashboard' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SBA_XAI_DASHBOARD');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('CASHFLOW_PREDICTION_CHART-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW', 'SBA_XAI_DASHBOARD', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'CASHFLOW_PREDICTION_CHART-VIEW', 'SBA XAI Dashboard', 'SBA XAI Dashboard', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-GB', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','de-DE', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','en-US', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','es-ES', 'View XAI Dashboard', 'View XAI Dashboard');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('CASHFLOW_PREDICTION_CHART_VIEW','fr-FR', 'View XAI Dashboard', 'View XAI Dashboard');


INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'CASHFLOW_PREDICTION_CHART_VIEW', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'CASHFLOW_PREDICTION_CHART_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



/* Features and permissions for Smart Banking Advisory */

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-GB' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'de-DE' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'en-US' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'es-ES' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE' ,'fr-FR' ,'Smart Banking Business Health Score' ,'Smart Banking Business Health Score' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_BUSINESS_HEALTH_SCORE-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW', 'SBA_BUSINESS_HEALTH_SCORE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_BUSINESS_HEALTH_SCORE-VIEW', 'Smart Banking Business Health Score', 'Smart Banking Business Health Score', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-GB', 'View Business Health Score', 'View Business Health Score');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','de-DE', 'View Business Health Score', 'View Business Health Score');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','en-US', 'View Business Health Score', 'View Business Health Score');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','es-ES', 'View Business Health Score', 'View Business Health Score');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_BUSINESS_HEALTH_SCORE_VIEW','fr-FR', 'View Business Health Score', 'View Business Health Score');


INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_BUSINESS_HEALTH_SCORE_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');



/* Features and permissions for Smart Banking Advisory */


INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'en-GB' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'de-DE' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'en-US' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'es-ES' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_SIMULATION' ,'fr-FR' ,'Smart Banking Simulation' ,'Smart Banking Simulation' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_SIMULATION-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_SIMULATION-EDIT',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_SIMULATION_VIEW', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-VIEW', 'Smart Banking Simultation', 'Smart Banking Simultation', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_VIEW','en-GB', 'View Simulation', 'View Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_VIEW','de-DE', 'View Simulation', 'View Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_VIEW','en-US', 'View Simulation', 'View Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_VIEW','es-ES', 'View Simulation', 'View Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_VIEW','fr-FR', 'View Simulation', 'View Simulation');


INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_SIMULATION_EDIT', 'SBA_SIMULATION', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_SIMULATION-EDIT', 'Smart Banking Simulation', 'Smart Banking Simulation', 0, 0, null, 0, 50, null, 0, 'CREATE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_EDIT','en-GB', 'Perform Simulation', 'Perform Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_EDIT','de-DE', 'Perform Simulation', 'Perform Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_EDIT','en-US', 'Perform Simulation', 'Perform Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_EDIT','es-ES', 'Perform Simulation', 'Perform Simulation');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_SIMULATION_EDIT','fr-FR', 'Perform Simulation', 'Perform Simulation');



INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_VIEW', '0');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_SIMULATION_EDIT', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_SIMULATION_EDIT', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');


/* Features and permissions for Smart Banking Advisory */

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'en-GB' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'de-DE' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'en-US' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'es-ES' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_INSIGHTS' ,'fr-FR' ,'Smart Banking Insights' ,'Smart Banking Insights' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');


INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS');


INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_INSIGHTS-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');


INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_INSIGHTS_VIEW', 'SBA_INSIGHTS', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_INSIGHTS-VIEW', 'SBA Insights', 'SBA Insights', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');


INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_INSIGHTS_VIEW','en-GB', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_INSIGHTS_VIEW','de-DE', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_INSIGHTS_VIEW','en-US', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_INSIGHTS_VIEW','es-ES', 'Smart Banking Insights', 'Smart Banking Insights');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_INSIGHTS_VIEW','fr-FR', 'Smart Banking Insights', 'Smart Banking Insights');


INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_INSIGHTS_VIEW', '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_INSIGHTS_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

GO
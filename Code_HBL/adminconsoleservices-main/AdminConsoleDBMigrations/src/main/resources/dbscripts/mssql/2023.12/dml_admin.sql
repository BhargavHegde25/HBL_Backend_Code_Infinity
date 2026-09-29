UPDATE [${dbxschemaname}].[application] SET [isSingleEntity] = '1' WHERE [id] = '2';

INSERT INTO [${dbxschemaname}].[feature] ([id], [App_id], [name], [description], [Type_id], [Status_id], [DisplaySequence], [isPrimary], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'RETAIL_AND_BUSINESS_BANKING' ,'Smart Banking' ,'Smart Banking' ,'NON_MONETARY' ,'SID_FEATURE_ACTIVE' , '34' , 0 ,  CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , '0' );

INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'en-GB' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'de-DE' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'en-US' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'es-ES' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');
INSERT INTO [${dbxschemaname}].[featuredisplaynamedescription] ([Feature_id], [Locale_id], [displayName], [displayDescription], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES ('SBA_AID' ,'fr-FR' ,'Smart Banking AID' ,'Smart Banking AID' , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[featureroletype] ([RoleType_id], [Feature_id]) VALUES ('TYPE_ID_BUSINESS', 'SBA_AID');

INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_RECEVIABLES-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');
INSERT INTO [${dbxschemaname}].[rrole] ([id], [createdts], [lastmodifiedts], [softdeleteflag]) VALUES ('SBA_PAYABLES-VIEW',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_RECEVIABLES_VIEW', 'SBA_AID', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_RECEVIABLES-VIEW', 'Smart Banking Receviables', 'Smart Banking Receviables', 0, 0, null, 0, 50, null, 0, 'VIEW', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_RECEVIABLES_VIEW','en-GB', 'View Receviables', 'View Receviables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_RECEVIABLES_VIEW','de-DE', 'View Receviables', 'View Receviables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_RECEVIABLES_VIEW','en-US', 'View Receviables', 'View Receviables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_RECEVIABLES_VIEW','es-ES', 'View Receviables', 'View Receviables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_RECEVIABLES_VIEW','fr-FR', 'View Receviables', 'View Receviables');

INSERT INTO [${dbxschemaname}].[featureaction] ([id], [Feature_id], [App_id], [Type_id], [Rrole_id], [name], [description], [isAccountLevel], [isMFAApplicable], [MFA_id], [isPrimary], [DisplaySequence], [dependency], [softdeleteflag], [accesspolicyId], [actionlevelId])
VALUES ('SBA_PAYABLES_VIEW', 'SBA_AID', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'SBA_PAYABLES-VIEW', 'Smart Banking Payables', 'Smart Banking Payables', 0, 0, null, 0, 50, null, 0, 'CREATE', 'CUSTOMERID_LEVEL');

INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_PAYABLES_VIEW','en-GB', 'View Payables', 'View Payables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_PAYABLES_VIEW','de-DE', 'View Payables', 'View Payables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_PAYABLES_VIEW','en-US', 'View Payables', 'View Payables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_PAYABLES_VIEW','es-ES', 'View Payables', 'View Payables');
INSERT INTO [${dbxschemaname}].[actiondisplaynamedescription] ([Action_id], [Locale_id], [displayName], [displayDescription])
VALUES ('SBA_PAYABLES_VIEW','fr-FR', 'View Payables', 'View Payables');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_RECEVIABLES_VIEW', '0');

INSERT INTO [${dbxschemaname}].[featureactionroletype] ([RoleType_id], [Action_id], [softdeleteflag])
VALUES ('TYPE_ID_BUSINESS', 'SBA_PAYABLES_VIEW', '0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(),'GROUP_ADMINISTRATOR' ,'SBA_RECEVIABLES_VIEW' ,'UID10', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[groupactionlimit] ([id], [Group_id], [Action_id], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(),'GROUP_ADMINISTRATOR' ,'SBA_PAYABLES_VIEW' ,'UID10', CURRENT_TIMESTAMP , CURRENT_TIMESTAMP , CURRENT_TIMESTAMP ,'0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_RECEVIABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

INSERT INTO [${dbxschemaname}].[servicedefinitionactionlimit] ([id], [serviceDefinitionId], [actionId], [createdby], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES (NEWID(), '707dfea8-d0fe-4154-89c3-e7d7ef2ee16a', 'SBA_PAYABLES_VIEW', 'UID10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');

/* Updated name and description for Smart Banking Features and permissions */

UPDATE [${dbxschemaname}].[feature] SET [name] = 'Smart Banking AID',[description] = 'Smart Banking AID' WHERE [id] = 'SBA_AID';
UPDATE [${dbxschemaname}].[feature] SET [name] = 'Smart Banking Dashboard',[description] = 'Smart Banking Dashboard' WHERE [id] = 'SBA_XAI_DASHBOARD';
UPDATE [${dbxschemaname}].[feature] SET [name] = 'Smart Banking Business Health Score',[description] = 'Smart Banking Business Health Score' WHERE [id] = 'SBA_BUSINESS_HEALTH_SCORE';
UPDATE [${dbxschemaname}].[feature] SET [name] = 'Smart Banking Simulation',[description] = 'Smart Banking Simulation' WHERE [id] = 'SBA_SIMULATION';
UPDATE [${dbxschemaname}].[feature] SET [name] = 'Smart Banking Insights',[description] = 'Smart Banking Insights' WHERE [id] = 'SBA_INSIGHTS';


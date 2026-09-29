INSERT INTO [${dbxschemaname}].[statustype] ([id], [Description]) VALUES ('STID_JOBSTATUS', 'Job Status');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description]) VALUES ('SID_JOB_PENDING', 'STID_JOBSTATUS', 'Pending');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description]) VALUES ('SID_JOB_COMPLETED', 'STID_JOBSTATUS', 'Completed');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description]) VALUES ('SID_JOB_INPROGRESS', 'STID_JOBSTATUS', 'In Progress');
INSERT INTO [${dbxschemaname}].[status] ([id], [Type_id], [Description]) VALUES ('SID_JOB_FAILED', 'STID_JOBSTATUS', 'Failed');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration], [createdts], [lastmodifiedts], [synctimestamp], [softdeleteflag]) VALUES
('08ee79a2-24d4-11ec-8150-0205857feb80', 'C360_CONFIG_BUNDLE', 'PREFERENCE', 'CONTRACT_JOB_SCHEDULING_CONFIG', 'Configuration to schedule a job for updating contracts', 'IMMEDIATE', 'SERVER', '0', GETDATE(), GETDATE(), GETDATE(), '0');
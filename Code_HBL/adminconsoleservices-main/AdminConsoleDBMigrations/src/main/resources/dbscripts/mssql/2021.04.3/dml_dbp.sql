USE [${dbxschemaname}];
GO

INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'80b2a729-c211-43b3-aae6-e3cc8d546b1',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'DEFAULT_PROSPECT_GROUP',N'Deafult Group For Prospect',N'DEFAULT_GROUP',N'SERVER',0,NULL,NULL,'2020-12-07 11:34:52.000','2020-12-07 11:34:52.000','2020-12-07 11:34:52.000',0);
GO


DELETE FROM  [${dbxschemaname}].configurations  WHERE bundle_id = 'C360_CONFIG_BUNDLE' AND config_key = 'AUTO_SYNC_BUSINESS_ACCOUNTS';

INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'3e411011-cb7a-4a4e-a785-ea196c7634cf',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'AUTO_SYNC_BUSINESS_ACCOUNTS',N'Auto Sync Retail Accounts..',N'true',N'SERVER',0,NULL,NULL,'2021-12-31 17:34:52.000','2021-12-31 17:34:52.000','2021-12-31 17:34:52.000',0);

DELETE FROM  [${dbxschemaname}].configurations  WHERE bundle_id = 'C360_CONFIG_BUNDLE' AND config_key = 'AUTO_SYNC_RETAIL_ACCOUNTS';

INSERT INTO [${dbxschemaname}].configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
	 (N'444001b0-531d-4c3a-9ea8-b8a58e76936a',N'C360_CONFIG_BUNDLE',N'PREFERENCE',N'AUTO_SYNC_RETAIL_ACCOUNTS',N'Auto Sync Business Accounts.',N'true',N'SERVER',0,NULL,NULL,'2021-12-31 17:34:52.000','2021-12-31 17:34:52.000','2021-12-31 17:34:52.000',0);

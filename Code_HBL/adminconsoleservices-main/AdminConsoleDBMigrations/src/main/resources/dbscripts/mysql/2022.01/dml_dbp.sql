



DELETE FROM  configurations  WHERE bundle_id = 'C360_CONFIG_BUNDLE' AND config_key = 'AUTO_SYNC_BUSINESS_ACCOUNTS';


INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
 ('3e411011-cb7a-4a4e-a785-ea196c7634cf','C360_CONFIG_BUNDLE','PREFERENCE','AUTO_SYNC_BUSINESS_ACCOUNTS','Auto Sync Retail Accounts.','true','SERVER',0,NULL,NULL,'2021-12-31 17:34:52','2021-12-31 17:34:52','2021-12-31 17:34:52',0);
	

DELETE FROM  configurations  WHERE bundle_id = 'C360_CONFIG_BUNDLE' AND config_key = 'AUTO_SYNC_RETAIL_ACCOUNTS';


INSERT INTO configurations (configuration_id,bundle_id,config_type,config_key,description,config_value,target,isPreLoginConfiguration,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES
 ('444001b0-531d-4c3a-9ea8-b8a58e76936a','C360_CONFIG_BUNDLE','PREFERENCE','AUTO_SYNC_RETAIL_ACCOUNTS','Auto Sync Business Accounts.','true','SERVER',0,NULL,NULL,'2021-12-31 17:34:52','2021-12-31 17:34:52','2021-12-31 17:34:52',0);	

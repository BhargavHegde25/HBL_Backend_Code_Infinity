INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_20','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','CURRENCY_CODE','Currencies support configuration','US Dollar','CLIENT',1);

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_21','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','GLOBAL_SEARCH_INFO_TEXT','This property is used to fetch the global search info message on hovering of the search icon','You can search an application via: Request ID, Facility/Product ID, Application No. or Name. All searches are case sensitive.\n\nFor Individual Party- Name Search supported by “Last Name” only \nFor Non-Individual Party (Business)- Name Search supported by “Full Company Name”','CLIENT',1);

UPDATE `configurations` SET `config_value`='USD' WHERE `configuration_id`='IA_20';

INSERT IGNORE INTO `configurations` (`configuration_id`,`bundle_id`,`config_type`,`config_key`,`description`,`config_value`,`target`,`isPreLoginConfiguration`) VALUES ('gd23fec2-4ce4-545f-5416-g28036bdd15h','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','RETAIL_CUSTOMER_STATUS','Retail Customer status after verification','ACTIVE','SERVER','1');

INSERT IGNORE INTO `configurations` (`configuration_id`,`bundle_id`,`config_type`,`config_key`,`description`,`config_value`,`target`,`isPreLoginConfiguration`) VALUES ('hd23fec2-5ce7-445g-4417-f28036bdd15s','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','SME_CUSTOMER_STATUS','SME Customer status after verification','ACTIVE','SERVER','1');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_23','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','RETENTION_PERIOD_WITHDRAWN_APPLICATIONS','Config value to set retention period for withdrawn applications','20','CLIENT',1);


INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_24','INFINITY_ASSIST_CONFIG_BUNDLE','PREFERENCE','RETENTION_PERIOD_REJECTED_APPLICATIONS','Config value to set retention period for rejected applications','30','CLIENT',1);
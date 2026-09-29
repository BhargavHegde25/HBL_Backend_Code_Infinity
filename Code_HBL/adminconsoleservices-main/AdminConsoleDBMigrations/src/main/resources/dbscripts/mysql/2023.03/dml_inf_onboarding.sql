INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('392', 'NUO_CONFIG_SME_BUNDLE','PREFERENCE','SME_T24_CUSTOMER_CONFIG', 'Configuration for SME T24 Customer','{\"externalSystemId\":\"PARTYMS\",\"language\": \"1\",\"accountOfficerId\": \"251\",\"industryId\":\"1000\",\"target\":\"4\",\"sectorTypeSme\":\"2015\",\"sectorTypeCompany\":\"2001\",\"sectorTypeRelatedCompany\":\"2003\",\"customerStatus\":\"71\",\"infinitySystemId\":\"DBX\"}', 'SERVER', 1);
	
	
INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('393', 'NUO_CONFIG_BUNDLE','PREFERENCE','RETAIL_T24_CUSTOMER_CONFIG', 'Configuration for Retail T24 Customer','{\"externalSystemId\":\"PARTYMS\",\"language\": \"1\",\"accountOfficerId\": \"26\",\"industryId\":\"1000\",\"target\":\"4\",\"sectorTypeRetail\":\"1000\",\"customerStatus\":\"4\",\"infinitySystemId\":\"DBX\"}', 'SERVER', 1);
INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('394', 'NUO_CONFIG_BUNDLE','PREFERENCE','OTHER_INCOME', 'Configuration for Other Income Options','[\"Bonus year1\",\"Bonus year 2\",\"Commission\",\"Investment Income\",\"Rental Income\",\"Pension\"]', 'CLIENT', 1);

UPDATE `configurations` set `config_value` = '{"DEPOSITS":["DEPOSITS","RETAIL.DEPOSIT"],"CHECKING":["CURRENT.ACCOUNTS","RETAIL.CURRENT.ACCOUNT"],"SAVINGS":["SAVINGS.ACCOUNTS","RETAIL.SAVINGS.ACCOUNT"],"CREDITCARDS":["CREDIT.CARDS"],"LOANS":["SMALL.BUSINESS.LOANS","BUSINESS.BANKING.LOAN"],"OVERDRAFT":["OVERDRAFT.ACCOUNTS"]}' where "config_key" = 'DEFAULT_PRODUCT_GROUPID' and "configuration_id" = 'SME_17';

UPDATE `configurations` SET `config_value` = '{"DEPOSITS":["DEPOSITS","RETAIL.DEPOSIT"],"CHECKING":["CURRENT.ACCOUNTS","RETAIL.CURRENT.ACCOUNT"],"SAVINGS":["SAVINGS.ACCOUNTS","RETAIL.SAVINGS.ACCOUNT"],"CREDITCARDS":["CREDIT.CARDS"],"LOANS":["SMALL.BUSINESS.LOANS","BUSINESS.BANKING.LOAN"],"OVERDRAFT":["OVERDRAFT.ACCOUNTS"]}' WHERE (`configuration_id` = 'SME_17');


UPDATE `configurations` SET `config_value` = '{\"BUSINESS.BANKING.LOAN\" :{ \"loanPurpose\": [\"Working Capital Finance\",\"Capital Expenditure\",\"Technology up-gradation\",\"Expansion\",\"Others\"]}}' WHERE (`configuration_id` = 'SME_96');


UPDATE `configurations` SET `config_value` = '[SMALL.BUSINESS.LOANS,OVERDRAFT.ACCOUNTS,CREDIT.CARDS,BUSINESS.BANKING.LOAN]' WHERE (`configuration_id` = 'c631b8a5-e2f2-494e-a3b1-3b9bcd83ba09');

INSERT IGNORE INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('395', 'NUO_CONFIG_BUNDLE','PREFERENCE','INCOME_SOURCE', 'Configuration for  Income Source Options','[\"Dividends\",\"Interest\",\"Investor\"]', 'CLIENT', 1);
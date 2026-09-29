INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_58', 'INFINITY_ASSIST_CONFIG_BUNDLE', 'PREFERENCE', 'RETAIL_NETAFFORDABILITY_FORMULA', 'Formula for calculating netaffordability', '(Net Income- expenses- committed expenses- debt)/Net Income)*100', 'CLIENT', '1');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_59', 'INFINITY_ASSIST_CONFIG_BUNDLE', 'PREFERENCE', 'RETAIL_CREDIT_COMPONENTS', 'Credit Components visibility config', '{"creditComponents": [{"id": "LTV","name":"Loan to Value Ratio","isVisible": "true"},{"id": "DTI","name":"Debt to Income Ratio","isVisible": "true"},{"id": "NetAffordability","name":"Net Affordability Ratio","isVisible": "true"},{"id": "CreditScore","name":"Credit Score","isVisible": "true"}]}', 'CLIENT', '1');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('IA_60', 'INFINITY_ASSIST_CONFIG_BUNDLE', 'PREFERENCE', 'RETAIL_TAX_SLABS', 'Tax slabs', '{"taxSlabs": [{"lowerLimit":"0","upperLimit": "12000","taxRate": "0"},{"lowerLimit":"12000","upperLimit": "50000","taxRate": "20"},{"lowerLimit":"50000","upperLimit": "150000","taxRate": "40"},{"lowerLimit":"150000","taxRate": "45"}]}', 'CLIENT', '1');

INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`) VALUES ('PID521', 'PER_TYPE_ENTITY_OVERVIEW', 'SID_ACTIVE', 'EntityOverviewViewFinancials', 'Permission to View Financials section in Entity Overview', '0', 'TRUE');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`) VALUES ('PID522', 'PER_TYPE_ENTITY_OVERVIEW', 'SID_ACTIVE', 'EntityOverviewAddFinancials', 'Permission to Add Financials section in Entity Overview', '0', 'TRUE');
INSERT INTO `permission` (`id`, `Type_id`, `Status_id`, `Name`, `Description`, `isComposite`, `PermissionValue`) VALUES ('PID523', 'PER_TYPE_ENTITY_OVERVIEW', 'SID_ACTIVE', 'EntityOverviewUpdateFinancials', 'Permission to Update Financials section in Entity Overview', '0', 'TRUE');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORGAGE_SUPERVISIOR', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_CREDIT_APPROVER', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_OPS', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_UW', 'PID521', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID522', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID522', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM', 'PID523', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_MORTGAGE_RM_SUPERVISIOR', 'PID523', '0');



INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailOps', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailSupervisor', 'PID521', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailUW', 'PID521', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID522', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID522', '0');

INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRM', 'PID523', '0');
INSERT INTO `rolepermission` (`Role_id`, `Permission_id`, `softdeleteflag`) VALUES ('RID_RetailRMSupervisor', 'PID523', '0');
UPDATE `configurations` SET `config_value` = '[\"Bonus\",\"Commission\",\"Rental Income\",\"Investment Income\"]' WHERE (`configuration_id` = '395');

UPDATE `configurations` SET `config_value` = '{\"BASIC.CHECKING.ACCOUNT\" : \"RETAIL.UNSECURED\",\"SAVINGS.SALARY.INFINITY\" : \"RETAIL.UNSECURED\",\"INFINITY.UNLIMITED.REWARDS.CARD\" : \"SME.CREDITCARD\",\"OVERDRAFT.ACCOUNT.SME\" : \"SME.OVERDRAFT\",\"SAVINGS.STANDARD.INFINITY\" : \"RETAIL.UNSECURED\",\"PERSONAL.LOAN\" : \"RETAIL.UNSECURED\",\"MORTGAGE.FACILITY\" : \"RETAIL.SECURED\",\"OVERDRAFT.ACCOUNT\" : \"RETAIL.UNSECURED\",\"INFINITY.VIBRANIUM.CARD\" : \"RETAIL.UNSECURED\",\"SMALL.BUSINESS.LOAN\" : \"SME.LOAN\",\"INFINITY.BESKAR.CARD\" : \"RETAIL.UNSECURED\",\"SAVINGS.PRIME.INFINITY\" : \"RETAIL.UNSECURED\",\"INFINITY.FUSION.BUSINESS.CARD\" : \"SME.CREDITCARD\",\"INFINITY.TRAVEL.CARD\" : \"SME.CREDITCARD\",\"INFINITY.ADAMANTIUM.CARD\" : \"RETAIL.UNSECURED\",\"ADVANCED.CHECKING.ACCOUNT\" : \"RETAIL.UNSECURED\",\"PREFERRED.CHECKING.ACCOUNT\" : \"RETAIL.UNSECURED\",\"BRIDGE.LOAN\" : \"RETAIL.SECURED\",\"BB.SMALL.BUSINESS.LOAN\":\"BB.SMALL.BUSINESS.LOAN\",\"BB.PREMIUM.ACCOUNT\":\"BB.PREMIUM.ACCOUNT\",\"BB.STANDARD.ACCOUNT\":\"BB.STANDARD.ACCOUNT\",\"BB.START.UP.ACCOUNT\":\"BB.START.UP.ACCOUNT\",\"RES.CREDIT.LINE\":\"RETAIL.UNSECURED\",\"RES.CONSUMER.LOAN\":\"RETAIL.UNSECURED\"}' WHERE (`configuration_id` = 'IA_12');
-- DBP BUNDLE CONFIGURATION: ACCOUNTS
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`)
VALUES ('ACC_6', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACCOUNTS_PER_PAGE', 'Configuration to display number of Accounts per page in Accounts Dashboard', '10', 'CLIENT', '1');


INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`)
VALUES ('TNC_REFRESH_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'TNC_REFRESH_PERIOD', 'Terms and Conditions Refresh period', '30', 'CLIENT', '1');

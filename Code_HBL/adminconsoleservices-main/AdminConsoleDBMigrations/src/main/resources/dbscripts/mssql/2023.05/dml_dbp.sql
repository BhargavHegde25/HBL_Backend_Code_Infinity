-- DBP BUNDLE CONFIGURATION: ACCOUNTS
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('ACC_1', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'ACCOUNTS_COUNT_COMPACT_DASHBOARD', 'Number of Accounts to display Compact Dashboard', '20', 'CLIENT', '1');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('ACC_2', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'SEARCH_CUSTOMERID_CONFIG_COUNT', 'Search Customers Configuration', '10', 'CLIENT', '1');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('ACC_3', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'FAVOURITE_CUSTOMERID_CONFIG_COUNT', 'Favourite Customers Configuration', '10', 'CLIENT', '1');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('ACC_4', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'DROPDOWN_CUSTOMERID_CONFIG_COUNT', 'Dropdown Customers Configuration', '4', 'CLIENT', '1');
INSERT INTO [${dbxschemaname}].[configurations] ([configuration_id], [bundle_id], [config_type], [config_key], [description], [config_value], [target], [isPreLoginConfiguration])
VALUES ('ACC_5', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'CUSTOMERIDS_PER_PAGE', 'Configuration to display number of Customers per page', '4', 'CLIENT', '1');

Go
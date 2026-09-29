UPDATE [${dbxschemaname}].[configurations] SET config_value = N'false' WHERE (configuration_id = 'NUO_SFDC_22');

UPDATE [${dbxschemaname}].[configurations] SET config_value= N'[CREDIT.CARDS,PERSONAL.LOANS,OVERDRAFT.ACCOUNTS,BRIDGE]' WHERE (configuration_id = 'NUO_SFDC_101' AND config_key='INCOME_EMPLOYMENT_REQUIRED');
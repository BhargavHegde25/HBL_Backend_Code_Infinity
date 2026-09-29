UPDATE [${dbxschemaname}].[configurations] set config_value = '{\"Existent\":[\"INTRA\",\"INTER\",\"MANUAL\"],\"Prospect\":[\"INTER\",\"MANUAL\"]}' WHERE (configuration_id = 'NUO_SFDC_24');
GO

/*Removed Minimum length validation for Password rules 
AdminConsoleServices.jar file has few changes need to be deployed */
UPDATE `dbxdb`.`passwordrules` SET `minLength` = '6' WHERE (`id` = 'PRULEID1');
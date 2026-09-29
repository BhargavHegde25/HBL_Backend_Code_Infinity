USE [${dbxdbname}];
update [${dbxschemaname}].featureaction set status = 'SID_ACTION_ACTIVE' where id != 'abc';

GO

UPDATE [${dbxschemaname}].configurations
	SET config_value=N'{"SAVINGS.PLAN":"Deposit","DEPOSIT.CALL":"Deposit","CURRENT.ACCOUNT":"Checking","SS.ANNUAL":"Checking","SS.ROLLOVER.01M":"Deposit","NEGOTIABLE.LOAN":"Loan","CURRENT.ACCOUNT.SME":"Checking","PREMIUM.ACCOUNT":"Checking","CURRENT.ACCOUNT.STUDENT":"Checking","SAVINGS.ACCOUNT":"Savings","CONS.SAVING":"Savings","SAVINGS.SALARY.INFINITY":"Savings","MORTGAGE.FLOATING":"Loan","TERM.DEPOSIT":"Deposit","CURRENT.ACCOUNT.STAFF":"Checking","CURRENT.ACCOUNT.PREF":"Checking","CONS.CHECKING":"Checking","SAVINGS.ACCOUNT.WELCOME":"Savings","SAVINGS.STANDARD.INFINITY":"Savings","PREFER.ACCOUNT":"Checking","STUDENT.ACCOUNT":"Checking","MORTGAGE.FIX5Y.60LTV":"Loan","DEPOSIT.SHORT":"Deposit","DEPOSIT.5Y":"Deposit","DEPOSIT.3Y":"Deposit","ADVANCED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.PROMOTIONAL":"Savings","SAVINGS.ACCOUNT.FCY":"Savings","SAVINGS.ACCOUNT.MINOR":"Savings","DEPOSIT.09M":"Deposit","BASIC.CHECKING.ACCOUNT":"Checking","SS.FIXED.TERM":"Deposit","SS.MONTHLY":"Checking","MORTGAGE":"Loan","SS.SAVINGS.REGULAR":"Savings","CL.FACILITY":"Sprout","PERSONAL.LOAN":"Loan","SS.PAYG":"Checking","SAVINGS.PRIME.INFINITY":"Savings","CONS.MM":"Savings","DEPOSIT.LONG":"Deposit","VEHICLE.LOAN":"Loan","SAVINGS.DEFAULT":"Savings","PREFERRED.CHECKING.ACCOUNT":"Checking","SAVINGS.ACCOUNT.WLC":"Savings","SS.SAVINGS.CHILD":"Savings","SAVINGS.ACCOUNT.NOTICE":"Savings"}'
	WHERE configuration_id=N'172';
GO	

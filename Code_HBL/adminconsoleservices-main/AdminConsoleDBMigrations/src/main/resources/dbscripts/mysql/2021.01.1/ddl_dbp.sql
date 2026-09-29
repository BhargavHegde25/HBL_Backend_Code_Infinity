--  Auto-generated SQL script #202101292029
DELETE FROM customeraccounts
	WHERE id='1';
DELETE FROM customeraccounts
	WHERE id='2';
DELETE FROM customeraccounts
	WHERE id='3';
DELETE FROM customeraccounts
	WHERE id='4';
DELETE FROM customeraccounts
	WHERE id='5';
DELETE FROM customeraccounts
	WHERE id='6';
DELETE FROM customeraccounts
	WHERE id='7';
DELETE FROM customeraccounts
	WHERE id='8';
DELETE FROM customeraccounts
	WHERE id='9';
DELETE FROM customeraccounts
	WHERE id='10';
DELETE FROM customeraccounts
	WHERE id='11';

ALTER TABLE `customeraccounts`
ADD UNIQUE INDEX `UNIQUE_customerId_accountId` (`Customer_id` ASC, `Account_id` ASC);
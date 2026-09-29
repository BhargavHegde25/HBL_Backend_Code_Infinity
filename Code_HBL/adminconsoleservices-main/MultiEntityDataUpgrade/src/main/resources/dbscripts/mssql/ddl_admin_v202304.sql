USE dbxdb
GO
DROP TABLE IF EXISTS [dbxdb].[alerttablelist];
GO
CREATE TABLE [dbxdb].[alerttablelist](
	tableName varchar(50) NOT NULL
);
GO
INSERT INTO [dbxdb].[alerttablelist] (tableName) VALUES ('dbxalertcategory'),
('dbxalertcategorytext'),('dbxalerttype'),('dbxalerttypetext'),('alerttypechannel'),('alertsubtype'),('alertsubtypetext'),('communicationtemplate'),('alertsubtypeapp'),('alertsubtypecustomertype'),('alertsubtypeaccounttype'),('alertsubtypechannel'),('alertcategorychannel'),('customerbusinesstype'),('dbxcustomeralertentitlement'),('customerviewalertconfiguration'),('customeralertswitch'),('notification'),('usernotification'),('customeralertchannel'),('customeralertfrequency'),('alertattribute'),('alertattributelistvalues'),('alertrecipienttype');

GO
DROP PROCEDURE IF EXISTS [dbxdb].[dataUpgradeForMultiEntityAlerts];
GO
CREATE PROCEDURE [dbxdb].[dataUpgradeForMultiEntityAlerts]
	@_companyLegalunit nvarchar(50)
AS
BEGIN 

ALTER TABLE dbxdb.alertrecipienttype NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtype NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypechannel NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alerttypechannel NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.dbxalerttype NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypeaccounttype NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypecustomertype NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypeapp NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypetext NOCHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.dbxalertcategory NOCHECK CONSTRAINT ALL;

UPDATE [dbxdb].[dbxalertcategory] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null ;
UPDATE [dbxdb].[dbxalertcategorytext] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[dbxalerttype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[dbxalerttypetext] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alerttypechannel] set companyLegalUnit=@_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtypetext] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[communicationtemplate] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtypeapp] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtypecustomertype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtypeaccounttype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertsubtypechannel] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertcategorychannel] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[customerbusinesstype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null or companyLegalUnit = NULL;
UPDATE [dbxdb].[dbxcustomeralertentitlement] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[customerviewalertconfiguration] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[notification] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[usernotification] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[customeralertswitch] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[customeralertchannel] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[customeralertfrequency] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertattribute] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertattributelistvalues] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;
UPDATE [dbxdb].[alertrecipienttype] set companyLegalUnit = @_companyLegalunit where companyLegalUnit =  'ALL' or companyLegalUnit is null;

ALTER TABLE dbxdb.alertrecipienttype CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtype CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypechannel CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alerttypechannel CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.dbxalerttype CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypeaccounttype CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypecustomertype CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypeapp CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.alertsubtypetext CHECK CONSTRAINT ALL;
ALTER TABLE dbxdb.dbxalertcategory CHECK CONSTRAINT ALL;

END
GO


DROP PROCEDURE IF EXISTS [dbxdb].[alertsDataUpgradeReconcile];
GO
CREATE OR ALTER PROCEDURE [dbxdb].[alertsDataUpgradeReconcile]
AS
BEGIN
	DECLARE @VarSQL nvarchar(max);
	DECLARE @tname nvarchar(max);
	DECLARE @tlist nvarchar(max);
	DECLARE @VarCount INT;
	DECLARE tableList CURSOR LOCAL FOR (SELECT tablename FROM [dbxdb].alerttablelist);
	OPEN tableList;
		FETCH NEXT FROM tableList INTO @tname;
		WHILE (@@FETCH_STATUS=0)
        BEGIN
			SET @VarCount = 0;
			IF EXISTS(SELECT 1 FROM [information_schema].[columns] WHERE column_name = N'companyLegalUnit' AND TABLE_NAME = @tname)
				SET @VarSQL = concat('(SELECT @count = COUNT(DISTINCT companyLegalUnit) FROM [dbxdb].[', @tname, '] where companyLegalUnit = ''ALL'' or companyLegalUnit = '''' );');
			ELSE 
				SET @VarSQL = concat('(SELECT @count = COUNT(DISTINCT legalEntityId) FROM [dbxdb].[', @tname, '] where legalEntityId = ''ALL'' or companyLegalUnit = '''' );');
			EXECUTE SP_EXECUTESQL @VarSQL,N'@count INT output',@count=@VarCount output;
			IF @VarCount = 1 BEGIN
				SET @tlist = CONCAT(@tname,',',@tlist);
			END;
			FETCH NEXT FROM tableList INTO @tname;
		END;
    CLOSE tableList;
    SELECT @tlist;
END
GO

DROP PROCEDURE IF EXISTS [dbxdb].ME202304MasterDataCreateProc;
GO
CREATE PROCEDURE [dbxdb].ME202304MasterDataCreateProc
@_companyLegalunit varchar(50)
AS
BEGIN

	INSERT INTO [dbxdb].alertattribute
	(id, LanguageCode, name, type, createdby, modifiedby, softdeleteflag, companyLegalUnit)
	VALUES('AMOUNT', 'en-US', 'Amount', 'AMOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit);

	INSERT INTO [dbxdb].alertrecipienttype
	(id, name, isaccountlevel, servicename, operationname, inputparamsmapping, companyLegalUnit)
	VALUES
	((SELECT MAX( id ) FROM [dbxdb].alertrecipienttype) +1, 'Active Users', 0, NULL, NULL, NULL, @_companyLegalunit)
	INSERT INTO [dbxdb].alertrecipienttype
	(id, name, isaccountlevel, servicename, operationname, inputparamsmapping, companyLegalUnit)
	VALUES
	((SELECT MAX( id ) FROM [dbxdb].alertrecipienttype) +1, 'All CIF Users', 0, 'authProductServices', 'GetCustomerInformation', '{  "coreCustomerId": "corecustomerid",  "id": "customerId","legalEntityId":"companyLegalUnit"}', @_companyLegalunit)
	INSERT INTO [dbxdb].alertrecipienttype
	(id, name, isaccountlevel, servicename, operationname, inputparamsmapping, companyLegalUnit)
	VALUES
	((SELECT MAX( id ) FROM [dbxdb].alertrecipienttype) +1, 'All AccountUsers', 1, 'dbpProductServices', 'GetAccountIdAssociatedCustomers', '{"accountId":"accountnumber","legalEntityId":"companyLegalUnit"}', @_companyLegalunit)
	INSERT INTO [dbxdb].alertrecipienttype
	(id, name, isaccountlevel, servicename, operationname, inputparamsmapping, companyLegalUnit)
	VALUES
	((SELECT MAX( id ) FROM [dbxdb].alertrecipienttype) +1, 'All Approvers', 0, 'dbpApprovalRequestServices', 'fetchApprovers', '{"requestId":"requestId"}', @_companyLegalunit);

	INSERT INTO [dbxdb].alertsubtypeaccounttype
	(accountTypeId, alertSubTypeId, createdby, modifiedby, softdeleteflag, companyLegalUnit)
	VALUES('1', 'APPROVE_CHEQUE_BOOK_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'CHECKBOOK_REQUEST_EXECUTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'CHECKBOOK_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'CHECK_STATUS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'DAILY_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'MAXIMUM_BALANCE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'MINIMUM_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'WITHDRAWAL_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('1', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'ACCOUNT.CREDITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'ACCOUNT.DEBITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'APPROVE_CHEQUE_BOOK_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'CHECKBOOK_REQUEST_EXECUTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'CHECKBOOK_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'CHECK_STATUS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'DAILY_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'MAXIMUM_BALANCE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'MINIMUM_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'PWM.CANCEL.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'PWM.EXECUTED.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'WITHDRAWAL_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('2', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('3', 'PAYMENT_DUE_DATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('3', 'PAYMENT_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('4', 'DEPOSITS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('4', 'DEPOSIT_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('4', 'DEPOSIT_MATURITY_REMINDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('4', 'OVERDRAFT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('4', 'WITHDRAWAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('6', 'PAYMENT_DUE_DATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('6', 'PAYMENT_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit);

	INSERT INTO [dbxdb].alertsubtypecustomertype
	(customerTypeId, alertSubTypeId, createdby, modifiedby, softdeleteflag, companyLegalUnit)
	VALUES('TYPE_ID_BUSINESS', 'ACCOUNT.CREDITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT.DEBITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_CLOSURE_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_CLOSURE_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_CLOSURE_USER_SUSPENDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_LOAD', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_LOCKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACCOUNT_SUSPENDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACHFILE_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACHFILE_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACHTRANSACTION_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ACHTRANSACTION_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ADDRESS_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_ACH_TRANSACTION', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_ACH_TRANSACTION_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_ACH_TRANSACTION_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CHEQUE_BOOK_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_DOMESTIC_WIRE_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_ACH_FILE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_ACH_FILE_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_ACH_FILE_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_BILLPAY', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_BILLPAY_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_REQUEST_BILLPAY_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BILLPAY_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BILLPAY_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_APPROVE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_CANCEL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_EDIT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_INITIATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_REJECT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_WAITING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'BULK_PAYMENT_REQUEST_WAITING_ACK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CHECKBOOK_REQUEST_EXECUTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CHECKBOOK_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CHECK_STATUS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTERNATIONAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTER_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTRA_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_OWNACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTERNATIONAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTER_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTRA_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_OWNACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'CREATE_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'DAILY_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'DEPOSITS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'DEPOSIT_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'DEPOSIT_MATURITY_REMINDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'DOM_WIRE_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EMAIL_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_BENEFICIARY_CONSENT_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'EXPORT_LC_DRAWING_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_CONSENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_EXTENDED_FOR_PAYMENT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_PENDING_CONSENT_BY_APPLICANT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_HONOURED_BY_BANK_REJECTED_BY_APPLICANT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_ACCEPTED_AND_SETTLEMENT_EXTENDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PRESENTATION_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_PROCESSING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_RETURNED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_CLAIM_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_RETURNED_BY_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_ISSUED_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_PENDING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_AMENDMENT_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_ACCEPTED_AND_CLAIM_HONOURED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_ACTIVE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DECLINED_AND_CLOSED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_RESUBMITTED_WAS_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_DOCUMENT_SUBMITTED_WAS_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_INACTIVE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_PENDING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_CLAIM_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_PENDING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_REJECETED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'GUARANTEES_RECEIVED_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_DOCUMENTS_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_DOCUMENTS_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_PENDING_WITH_BANK_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_FOR_BANK_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_AMENDMENT_SUBMITTED_WITH_SELFCONSENT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_CREATED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_DRAWING_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'IMPORT_LC_SUBMITTED_APPROVAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTERNATIONAL_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTERTRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INTRATRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INT_TRANSFER_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INT_WIRE_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CANCEL_REQUEST_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_AMENDMENT_CONSENT_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_CANCELLED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_CONSENT_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_LAST_FIVE_DAYS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_ON_LAST_DAY', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_ON_LAST_THREE_ODD_DAYS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYDUE_OR_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PAYMENT_INITIATION_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_PROCESSING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_REQUEST_RESUBMITTED_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_RETURNED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SETTLED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SETTLED_FROM_PAYDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'INWARD_COLLECTION_USANCE_ACCEPTANCE_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'LOGIN_ATTEMPT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'MAXIMUM_BALANCE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'MINIMUM_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'NON_REG_BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ONETIME_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OTHER_BANK_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_PROCESSING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_REQUEST_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_AMENDMENT_RETURNED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_DOCUMENT_RESUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_DOCUMENT_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_ACCEPTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_RESUBMITTED_DOCUMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_REVISED_AMENDMENT_REQUEST_SUBMITTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OUTWARD_COLLECTION_SUBMITTED_DOCUMENT_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OVERDRAFT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'OWNACCOUNT_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'P2P_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PASSWORD_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PAYMENT_DUE_DATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PAYMENT_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PHONE_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PRIMARY_ADDRESS_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PRIMARY_EMAIL_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PRIMARY_PHONE_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PWM.CANCEL.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'PWM.EXECUTED.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'RECURRING_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTERNATIONAL_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTERTRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_INTRATRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REC_OWNACCOUNT_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REGISTERED_BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_ACH_TRANSACTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_ACH_TRANSACTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_REQUEST_ACH_FILE_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_REQUEST_ACH_FILE_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_REQUEST_BILLPAY_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_REQUEST_BILLPAY_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'REMOVE_ACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'RENOTIFY_PENDING_APPROVAL_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SAME_BANK_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SCHEDULED_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SECURE_MESSAGE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'SUBMIT_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'TRANSACTIONS_NOT_AVAILABLE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'TRANSACTION_LOAD', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'UNSUPPORTED_ACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'USERNAME_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'USER_DEACTIVATED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'USER_DELINKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'USER_LINKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WIRETRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WIRETRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAWAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAWAL_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_ACH_TRANSACTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_ACH_TRANSACTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_REQUEST_ACH_FILE_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_REQUEST_ACH_FILE_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_REQUEST_BILLPAY_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_REQUEST_BILLPAY_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_BUSINESS', 'WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT.CREDITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT.DEBITED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_CLOSURE_APPROVED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_CLOSURE_REJECTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_CLOSURE_USER_SUSPENDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_LOAD', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_LOCKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACCOUNT_SUSPENDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACHFILE_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACHFILE_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACHTRANSACTION_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ACHTRANSACTION_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ADDRESS_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_ACH_TRANSACTION', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_ACH_TRANSACTION_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_ACH_TRANSACTION_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CHEQUE_BOOK_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_DOMESTIC_WIRE_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_ACH_FILE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_ACH_FILE_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_ACH_FILE_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_BILLPAY', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_BILLPAY_EXECUTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_REQUEST_BILLPAY_EXECUTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'APPROVE_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_APPROVE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_CANCEL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_EDIT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_INITIATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_REJECT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_WAITING', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'BULK_PAYMENT_REQUEST_WAITING_ACK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CHECKBOOK_REQUEST_EXECUTED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CHECKBOOK_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CHECKBOOK_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CHECK_STATUS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTERNATIONAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTER_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTRA_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_OWNACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTERNATIONAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTER_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTRA_BANK', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_OWNACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'DAILY_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'DEPOSITS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'DEPOSIT_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'DEPOSIT_MATURITY_REMINDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'DOM_WIRE_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'EMAIL_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTERNATIONAL_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTERTRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INTRATRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INT_TRANSFER_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'INT_WIRE_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'LOGIN_ATTEMPT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'MAXIMUM_BALANCE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'MINIMUM_BALANCE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'NON_REG_BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ONETIME_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'OTHER_BANK_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'OVERDRAFT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'OWNACCOUNT_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'P2P_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PASSWORD_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PAYMENT_DUE_DATE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PAYMENT_OVERDUE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PHONE_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PRIMARY_ADDRESS_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PRIMARY_EMAIL_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PRIMARY_PHONE_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PWM.CANCEL.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'PWM.EXECUTED.ORDER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'RECURRING_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTERNATIONAL_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTERNATIONAL_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTERTRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTERTRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTRATRANSFER_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_INTRATRANSFER_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_OWNACCOUNT_REQUEST_FOR_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REC_OWNACCOUNT_REQUEST_TO_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REGISTERED_BILL_PAYEE_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_ACH_TRANSACTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_ACH_TRANSACTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_REQUEST_ACH_FILE_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_REQUEST_ACH_FILE_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_REQUEST_BILLPAY_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_REQUEST_BILLPAY_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REJECT_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'REMOVE_ACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'RENOTIFY_PENDING_APPROVAL_REQUEST', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SAME_BANK_RECIPIENT_ADDED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SCHEDULED_OTHER_BANK_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SECURE_MESSAGE_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'SUBMIT_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'TRANSACTIONS_NOT_AVAILABLE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'TRANSACTION_LOAD', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'UNSUPPORTED_ACCOUNT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'USERNAME_CHANGE', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'USER_DEACTIVATED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'USER_DELINKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'USER_LINKED', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAWAL', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAWAL_AMOUNT_ALERT', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_ACH_TRANSACTION_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_ACH_TRANSACTION_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTERNATIONAL_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTERNATIONAL_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTER_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTER_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTRA_BANK_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_INTRA_BANK_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CANCEL_RECUR_OWNACCOUNT_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CHEQUE_BOOK_REQUEST_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_CHEQUE_BOOK_REQUEST_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_DOMESTIC_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_DOMESTIC_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_INTERNATIONAL_WIRE_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECURRING_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_RECUR_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_REQUEST_ACH_FILE_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_REQUEST_ACH_FILE_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_REQUEST_BILLPAY_ALL_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_REQUEST_BILLPAY_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTERNATIONAL_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTER_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTER_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_INTRA_BANK_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_APPROVERS', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'WITHDRAW_SINGLE_OWN_ACCOUNT_TRANSFER_INITIATOR', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit),
	('TYPE_ID_RETAIL', 'CREATE_CANCEL_OWNACCOUNT_APPROVER', 'MultiEntityUpgradeTool', 'MultiEntityUpgradeTool', 0, @_companyLegalunit);

	INSERT INTO [dbxdb].[featureaction] (id, Feature_id, App_id, Type_id, Rrole_id, name, description, isAccountLevel, isMFAApplicable, MFA_id,TermsAndConditions_id, notes, isPrimary, DisplaySequence, dependency, status, limitgroupId, accesspolicyId, actionlevelId, approveFeatureAction, isApprovalAction, companyLegalUnit) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ADD_USER_ANOTHER_ENTITY','Add user to another entity','Add user to another entity',0,0,NULL,NULL,NULL,1,1,NULL,'SID_ACTION_ACTIVE',NULL,'ADMIN','CUSTOMERID_LEVEL',NULL,0,@_companyLegalunit);


	INSERT INTO [dbxdb].[featureactionroletype] (RoleType_id, Action_id, companyLegalUnit) VALUES ('TYPE_ID_BUSINESS', 'ADD_USER_ANOTHER_ENTITY', @_companyLegalunit);

	INSERT INTO [dbxdb].[actiondisplaynamedescription] (Action_id, Locale_id, displayName, displayDescription, companyLegalUnit) VALUES
	('ADD_USER_ANOTHER_ENTITY','de-DE','Add user to another entity','Add user to another entity',@_companyLegalunit),
	('ADD_USER_ANOTHER_ENTITY','en-GB','Add user to another entity','Add user to another entity',@_companyLegalunit),
	('ADD_USER_ANOTHER_ENTITY','en-US','Add user to another entity','Add user to another entity',@_companyLegalunit),
	('ADD_USER_ANOTHER_ENTITY','es-ES','Add user to another entity','Add user to another entity',@_companyLegalunit),
	('ADD_USER_ANOTHER_ENTITY','fr-FR','Add user to another entity','Add user to another entity',@_companyLegalunit);

	INSERT INTO [dbxdb].[dependentactions] (actionId,dependentactionId,featureId,actionName,featureName,companyLegalUnit) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT','USER_MANAGEMENT','Add user to another entity','Add user to another entity',@_companyLegalunit);
	
	INSERT INTO [dbxdb].[dependentactions] (actionId,dependentactionId,featureId,actionName,featureName,companyLegalUnit) VALUES ('ADD_USER_ANOTHER_ENTITY','USER_MANAGEMENT_VIEW','USER_MANAGEMENT','Add user to another entity','Add user to another entity',@_companyLegalunit);

	INSERT INTO [dbxdb].[feature] (id, App_id, name, description, Type_id, Status_id, Service_Fee, DisplaySequence, isPrimary, companyLegalUnit) VALUES
	('QR_PAYMENTS','RETAIL_AND_BUSINESS_BANKING','QR Payments','QR Payments','MONETARY','SID_FEATURE_ACTIVE',NULL,85,0,@_companyLegalunit);

	INSERT INTO [dbxdb].[featuredisplaynamedescription] (Feature_id, Locale_id, displayName, displayDescription, companyLegalUnit) VALUES
	('QR_PAYMENTS','de-DE','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS','en-GB','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS','en-US','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS','es-ES','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS','fr-FR','QR Payments','Create QR Payments',@_companyLegalunit);

	INSERT INTO [dbxdb].[featureroletype] (RoleType_id, Feature_id, companyLegalUnit) VALUES
	('TYPE_ID_BUSINESS', 'QR_PAYMENTS', @_companyLegalunit),
	('TYPE_ID_RETAIL', 'QR_PAYMENTS', @_companyLegalunit);


	INSERT INTO [dbxdb].[feature] (id, App_id, name, description, Type_id, Status_id, Service_Fee, DisplaySequence, isPrimary, companyLegalUnit) values
	('ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','Account Sweeps','Account Sweeps','NON_MONETARY','SID_FEATURE_ACTIVE',NULL,84,0,@_companyLegalunit);

	INSERT INTO [dbxdb].[featuredisplaynamedescription] (Feature_id, Locale_id, displayName, displayDescription, companyLegalUnit) VALUES
	('ACCOUNT_SWEEP','de-DE','Account Sweeps','Account Sweeps',@_companyLegalunit),
	('ACCOUNT_SWEEP','en-GB','Account Sweeps','Account Sweeps',@_companyLegalunit),
	('ACCOUNT_SWEEP','en-US','Account Sweeps','Account Sweeps',@_companyLegalunit),
	('ACCOUNT_SWEEP','es-ES','Account Sweeps','Account Sweeps',@_companyLegalunit),
	('ACCOUNT_SWEEP','fr-FR','Account Sweeps','Account Sweeps',@_companyLegalunit);


	INSERT INTO [dbxdb].[featureroletype] (RoleType_id, Feature_id, companyLegalUnit) VALUES
	('TYPE_ID_BUSINESS','ACCOUNT_SWEEP',@_companyLegalunit),
	('TYPE_ID_RETAIL','ACCOUNT_SWEEP',@_companyLegalunit);

	INSERT INTO [dbxdb].[featureaction] (id, Feature_id, App_id, Type_id, Rrole_id, name, description, isAccountLevel, isMFAApplicable, MFA_id,TermsAndConditions_id, notes, isPrimary, DisplaySequence, dependency, status, limitgroupId, accesspolicyId, actionlevelId, approveFeatureAction, isApprovalAction, companyLegalUnit) VALUES 
	('QR_PAYMENTS_CREATE','QR_PAYMENTS','RETAIL_AND_BUSINESS_BANKING','MONETARY','QR_PAYMENTS_CREATE','QR Payments','Create QR Payments',1,1,NULL,NULL,NULL,0,0,NULL,'SID_ACTION_ACTIVE',NULL,'CREATE','ACCOUNT_LEVEL',NULL,0,@_companyLegalunit);


	INSERT INTO [dbxdb].[featureactionroletype] (RoleType_id, Action_id, companyLegalUnit) VALUES 
	('TYPE_ID_BUSINESS','QR_PAYMENTS_CREATE',@_companyLegalunit),
	('TYPE_ID_RETAIL','QR_PAYMENTS_CREATE',@_companyLegalunit);

	INSERT INTO [dbxdb].[actiondisplaynamedescription] (Action_id, Locale_id, displayName, displayDescription, companyLegalUnit) VALUES
	('QR_PAYMENTS_CREATE','de-DE','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS_CREATE','en-GB','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS_CREATE','en-US','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS_CREATE','es-ES','QR Payments','Create QR Payments',@_companyLegalunit),
	('QR_PAYMENTS_CREATE','fr-FR','QR Payments','Create QR Payments',@_companyLegalunit);



	INSERT INTO [dbxdb].[featureaction] (id, Feature_id, App_id, Type_id, Rrole_id, name, description, isAccountLevel, isMFAApplicable, MFA_id,TermsAndConditions_id, notes, isPrimary, DisplaySequence, dependency, status, limitgroupId, accesspolicyId, actionlevelId, approveFeatureAction, isApprovalAction, companyLegalUnit) VALUES 
	('ACCOUNT_SWEEP_CREATE','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_CREATE','Create Account Sweep','Create Account Sweep',1,0,NULL,NULL,NULL,0,0,NULL,'SID_ACTION_ACTIVE',NULL,'CREATE','ACCOUNT_LEVEL',NULL,0,@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_DELETE','Delete Account Sweep','Delete Account Sweep',1,0,NULL,NULL,NULL,0,0,NULL,'SID_ACTION_ACTIVE',NULL,'DELETE','ACCOUNT_LEVEL',NULL,0,@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_EDIT','Edit Account Sweep','Edit Account Sweep',1,0,NULL,NULL,NULL,0,0,NULL,'SID_ACTION_ACTIVE',NULL,'CREATE','ACCOUNT_LEVEL',NULL,0,@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','ACCOUNT_SWEEP','RETAIL_AND_BUSINESS_BANKING','NON_MONETARY','ACCOUNT_SWEEP_VIEW','View Account Sweep','View Account Sweep',1,0,NULL,NULL,NULL,0,0,NULL,'SID_ACTION_ACTIVE',NULL,'VIEW','ACCOUNT_LEVEL',NULL,0,@_companyLegalunit);


	INSERT INTO [dbxdb].[featureactionroletype] (RoleType_id, Action_id, companyLegalUnit) VALUES 
	('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_CREATE',@_companyLegalunit),
	('TYPE_ID_RETAIL','ACCOUNT_SWEEP_CREATE',@_companyLegalunit),
	('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_DELETE',@_companyLegalunit),
	('TYPE_ID_RETAIL','ACCOUNT_SWEEP_DELETE',@_companyLegalunit),
	('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_EDIT',@_companyLegalunit),
	('TYPE_ID_RETAIL','ACCOUNT_SWEEP_EDIT',@_companyLegalunit),
	('TYPE_ID_BUSINESS','ACCOUNT_SWEEP_VIEW',@_companyLegalunit),
	('TYPE_ID_RETAIL','ACCOUNT_SWEEP_VIEW',@_companyLegalunit);

	INSERT INTO [dbxdb].[actiondisplaynamedescription] (Action_id, Locale_id, displayName, displayDescription, companyLegalUnit) VALUES
	('ACCOUNT_SWEEP_CREATE','de-DE','Create Account Sweep','Create Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_CREATE','en-GB','Create Account Sweep','Create Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_CREATE','en-US','Create Account Sweep','Create Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_CREATE','es-ES','Create Account Sweep','Create Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_CREATE','fr-FR','Create Account Sweep','Create Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','de-DE','Delete Account Sweep','Delete Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','en-GB','Delete Account Sweep','Delete Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','en-US','Delete Account Sweep','Delete Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','es-ES','Delete Account Sweep','Delete Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_DELETE','fr-FR','Delete Account Sweep','Delete Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','de-DE','Edit Account Sweep','Edit Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','en-GB','Edit Account Sweep','Edit Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','en-US','Edit Account Sweep','Edit Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','es-ES','Edit Account Sweep','Edit Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_EDIT','fr-FR','Edit Account Sweep','Edit Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','de-DE','View Account Sweep','View Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','en-GB','View Account Sweep','View Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','en-US','View Account Sweep','View Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','es-ES','View Account Sweep','View Account Sweep',@_companyLegalunit),
	('ACCOUNT_SWEEP_VIEW','fr-FR','View Account Sweep','View Account Sweep',@_companyLegalunit);

	INSERT INTO [dbxdb].[actionlimit] (Action_id, LimitType_id, value, companyLegalUnit) VALUES
	('QR_PAYMENTS_CREATE','DAILY_LIMIT',1000.00,@_companyLegalunit),
	('QR_PAYMENTS_CREATE','MAX_TRANSACTION_LIMIT',500.00,@_companyLegalunit),
	('QR_PAYMENTS_CREATE','MIN_TRANSACTION_LIMIT',1.00,@_companyLegalunit),
	('QR_PAYMENTS_CREATE','WEEKLY_LIMIT',5000.00,@_companyLegalunit);

END
GO
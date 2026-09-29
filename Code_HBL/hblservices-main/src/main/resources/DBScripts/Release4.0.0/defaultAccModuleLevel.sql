ALTER table dbxdb.customerpreference 

ADD COLUMN DefaultAccountDashboard varchar(50) NULL AFTER DefaultAccountCardPayment,
ADD COLUMN DefaultAccountQRPayment varchar(50) NULL AFTER DefaultAccountDashboard,
ADD COLUMN DefaultAccountCheckDeposit varchar(50) NULL AFTER DefaultAccountQRPayment,		
ADD COLUMN DefaultAccountCashWithdrawl varchar(50) NULL AFTER DefaultAccountCheckDeposit;		



/* Updated customerpreferencesview view by reading default_account_loanpayment, default_account_cardpayment,default_account_checkmanagement  */

USE `dbxdb`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
      SQL SECURITY DEFINER
VIEW `dbxdb`.`customerpreferencesview` AS
    SELECT DISTINCT
        `dbxdb`.`address`.`addressLine1` AS `addressLine1`,
        `dbxdb`.`address`.`addressLine2` AS `addressLine2`,
        `dbxdb`.`address`.`state` AS `state`,
        `dbxdb`.`address`.`cityName` AS `city`,
        `dbxdb`.`address`.`country` AS `country`,
        `dbxdb`.`address`.`zipCode` AS `zipcode`,
        `dbxdb`.`customer`.`areUserAlertsTurnedOn` AS `areUserAlertsTurnedOn`,
        `dbxdb`.`customer`.`areAccountStatementTermsAccepted` AS `areAccountStatementTermsAccepted`,
        `dbxdb`.`customer`.`areDepositTermsAccepted` AS `areDepositTermsAccepted`,
        `dbxdb`.`customerpreference`.`DefaultAccountDeposit` AS `default_account_deposit`,
        `dbxdb`.`customerpreference`.`DefaultAccountBillPay` AS `default_account_billPay`,
        `dbxdb`.`customerpreference`.`DefaultAccountPayments` AS `default_account_payments`,
        `dbxdb`.`customerpreference`.`DefaultAccountCardless` AS `default_account_cardless`,
        `dbxdb`.`customerpreference`.`DefaultAccountTransfers` AS `default_account_transfers`,
		
		`dbxdb`.`customerpreference`.`DefaultAccountLoanPayment` AS `default_account_loanpayment`,
		`dbxdb`.`customerpreference`.`DefaultAccountCardPayment` AS `default_account_cardpayment`,
		`dbxdb`.`customerpreference`.`DefaultAccountCheckManagement` AS `default_account_checkmanagement`,
		
		`dbxdb`.`customerpreference`.`DefaultAccountDashboard` AS `default_account_dashboard`,
		`dbxdb`.`customerpreference`.`DefaultAccountQRPayment` AS `default_account_qrpayment`,
		`dbxdb`.`customerpreference`.`DefaultAccountCheckDeposit` AS `default_account_checkdeposit`,
		`dbxdb`.`customerpreference`.`DefaultAccountCashWithdrawl` AS `default_account_cashwithdrawl`,
		
        `dbxdb`.`customerpreference`.`DefaultModule_id` AS `DefaultModule_id`,
        `dbxdb`.`customerpreference`.`DefaultAccountWire` AS `default_account_wire`,
        `dbxdb`.`customerpreference`.`DefaultFromAccountP2P` AS `default_from_account_p2p`,
        `dbxdb`.`customerpreference`.`DefaultToAccountP2P` AS `default_to_account_p2p`,
        `dbxdb`.`customer`.`isP2PActivated` AS `isP2PActivated`,
        `dbxdb`.`customer`.`isP2PSupported` AS `isP2PSupported`,
        `dbxdb`.`customer`.`isBillPaySupported` AS `isBillPaySupported`,
        `dbxdb`.`customer`.`isBillPayActivated` AS `isBillPayActivated`,
        `dbxdb`.`customer`.`isWireTransferActivated` AS `isWireTransferActivated`,
        `dbxdb`.`customer`.`isWireTransferEligible` AS `isWireTransferEligible`,
        `dbxdb`.`customerpreference`.`ShowBillPayFromAccPopup` AS `showBillPayFromAccPopup`,
        `dbxdb`.`customer`.`FirstName` AS `userFirstName`,
        `dbxdb`.`customer`.`LastName` AS `userLastName`,
        `dbxdb`.`customer`.`Gender` AS `gender`,
        `dbxdb`.`customer`.`IsPinSet` AS `isPinSet`,
        `dbxdb`.`customer`.`DateOfBirth` AS `DateOfBirth`,
        `dbxdb`.`customer`.`NoOfDependents` AS `noofdependents`,
        `dbxdb`.`customer`.`SpouseName` AS `spousefirstname`,
        `dbxdb`.`customer`.`Ssn` AS `ssn`,
        `dbxdb`.`customer`.`CountryCode` AS `CountryCode`,
        `dbxdb`.`customer`.`UserImage` AS `userImage`,
        `dbxdb`.`customer`.`UserImageURL` AS `userImageURL`,
        `dbxdb`.`customer`.`isEagreementSigned` AS `isEagreementSigned`,
        `dbxdb`.`customer`.`id` AS `id`,
        `dbxdb`.`customer`.`UserName` AS `UserName`,
        `dbxdb`.`customer`.`MaritalStatus_id` AS `maritalstatus`,
        `dbxdb`.`customer`.`Lastlogintime` AS `lastlogintime`,
        `dbxdb`.`customer`.`Bank_id` AS `Bank_id`,
        `primaryphone`.`Value` AS `Phone`,
        `primaryemail`.`Value` AS `Email`
    FROM
        ((((`dbxdb`.`customer`
        LEFT JOIN `dbxdb`.`customerpreference` ON ((`dbxdb`.`customerpreference`.`Customer_id` = `dbxdb`.`customer`.`id`)))
        LEFT JOIN (`dbxdb`.`customeraddress`
        JOIN `dbxdb`.`address` ON ((`dbxdb`.`address`.`id` = `dbxdb`.`customeraddress`.`Address_id`))) ON (((`dbxdb`.`customer`.`id` = `dbxdb`.`customeraddress`.`Customer_id`)
            AND (`dbxdb`.`customeraddress`.`isPrimary` = 1))))
        LEFT JOIN `dbxdb`.`customercommunication` `primaryphone` ON (((`primaryphone`.`Customer_id` = `dbxdb`.`customer`.`id`)
            AND (`primaryphone`.`isPrimary` = 1)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))))
        LEFT JOIN `dbxdb`.`customercommunication` `primaryemail` ON (((`primaryemail`.`Customer_id` = `dbxdb`.`customer`.`id`)
            AND (`primaryemail`.`isPrimary` = 1)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL'))))
ALTER table dbxdb.customerpreference 

ADD COLUMN DefaultAccountLoanPayment varchar(50) NULL AFTER DefaultAccountDeposit,
ADD COLUMN DefaultAccountCardPayment varchar(50) NULL AFTER DefaultAccountLoanPayment,
ADD COLUMN DefaultAccountCheckManagement varchar(50) NULL AFTER DefaultAccountCardPayment;			

---------------------------------------------------
// Updated customerpreferencesview view by reading default_account_loanpayment, default_account_cardpayment,default_account_checkmanagement 

USE `dbxdb`;
CREATE 
     OR REPLACE ALGORITHM = UNDEFINED 
    DEFINER = `root`@`localhost` 
    SQL SECURITY DEFINER
VIEW `customerpreferencesview` AS
    SELECT DISTINCT
        `address`.`addressLine1` AS `addressLine1`,
        `address`.`addressLine2` AS `addressLine2`,
        `address`.`state` AS `state`,
        `address`.`cityName` AS `city`,
        `address`.`country` AS `country`,
        `address`.`zipCode` AS `zipcode`,
        `customer`.`areUserAlertsTurnedOn` AS `areUserAlertsTurnedOn`,
        `customer`.`areAccountStatementTermsAccepted` AS `areAccountStatementTermsAccepted`,
        `customer`.`areDepositTermsAccepted` AS `areDepositTermsAccepted`,
        `customerpreference`.`DefaultAccountDeposit` AS `default_account_deposit`,
        `customerpreference`.`DefaultAccountBillPay` AS `default_account_billPay`,
        `customerpreference`.`DefaultAccountPayments` AS `default_account_payments`,
        `customerpreference`.`DefaultAccountCardless` AS `default_account_cardless`,
        `customerpreference`.`DefaultAccountTransfers` AS `default_account_transfers`,
        `customerpreference`.`DefaultAccountLoanPayment` AS `default_account_loanpayment`,
		`customerpreference`.`DefaultAccountCardPayment` AS `default_account_cardpayment`,
		`customerpreference`.`DefaultAccountCheckManagement` AS `default_account_checkmanagement`,
        `customerpreference`.`DefaultModule_id` AS `DefaultModule_id`,
        `customerpreference`.`DefaultAccountWire` AS `default_account_wire`,
        `customerpreference`.`DefaultFromAccountP2P` AS `default_from_account_p2p`,
        `customerpreference`.`DefaultToAccountP2P` AS `default_to_account_p2p`,
        `customer`.`isP2PActivated` AS `isP2PActivated`,
        `customer`.`isP2PSupported` AS `isP2PSupported`,
        `customer`.`isBillPaySupported` AS `isBillPaySupported`,
        `customer`.`isBillPayActivated` AS `isBillPayActivated`,
        `customer`.`isWireTransferActivated` AS `isWireTransferActivated`,
        `customer`.`isWireTransferEligible` AS `isWireTransferEligible`,
        `customerpreference`.`ShowBillPayFromAccPopup` AS `showBillPayFromAccPopup`,
        `customer`.`FirstName` AS `userFirstName`,
        `customer`.`LastName` AS `userLastName`,
        `customer`.`Gender` AS `gender`,
        `customer`.`IsPinSet` AS `isPinSet`,
        `customer`.`DateOfBirth` AS `DateOfBirth`,
        `customer`.`NoOfDependents` AS `noofdependents`,
        `customer`.`SpouseName` AS `spousefirstname`,
        `customer`.`Ssn` AS `ssn`,
        `customer`.`CountryCode` AS `CountryCode`,
        `customer`.`UserImage` AS `userImage`,
        `customer`.`UserImageURL` AS `userImageURL`,
        `customer`.`isEagreementSigned` AS `isEagreementSigned`,
        `customer`.`id` AS `id`,
        `customer`.`UserName` AS `UserName`,
        `customer`.`MaritalStatus_id` AS `maritalstatus`,
        `customer`.`Lastlogintime` AS `lastlogintime`,
        `customer`.`Bank_id` AS `Bank_id`,
        `primaryphone`.`Value` AS `Phone`,
        `primaryemail`.`Value` AS `Email`
    FROM
        ((((`customer`
        LEFT JOIN `customerpreference` ON ((`customerpreference`.`Customer_id` = `customer`.`id`)))
        LEFT JOIN (`customeraddress`
        JOIN `address` ON ((`address`.`id` = `customeraddress`.`Address_id`))) ON (((`customer`.`id` = `customeraddress`.`Customer_id`)
            AND (`customeraddress`.`isPrimary` = 1))))
        LEFT JOIN `customercommunication` `primaryphone` ON (((`primaryphone`.`Customer_id` = `customer`.`id`)
            AND (`primaryphone`.`isPrimary` = 1)
            AND (`primaryphone`.`Type_id` = 'COMM_TYPE_PHONE'))))
        LEFT JOIN `customercommunication` `primaryemail` ON (((`primaryemail`.`Customer_id` = `customer`.`id`)
            AND (`primaryemail`.`isPrimary` = 1)
            AND (`primaryemail`.`Type_id` = 'COMM_TYPE_EMAIL'))));

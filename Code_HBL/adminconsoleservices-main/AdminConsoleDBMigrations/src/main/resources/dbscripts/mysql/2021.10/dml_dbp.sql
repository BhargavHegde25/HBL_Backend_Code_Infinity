INSERT INTO `mfaserviceconfig` (`id`, `serviceName`, `transactionType`) VALUES ('99', 'P2P_CREATE', 'transactionobjects_transaction_p2ptransfer');

UPDATE `alertrecipienttype` SET `servicename` = 'authProductServices' WHERE (`id` = '2');


UPDATE `alertsubtype` SET `value1` = '50' WHERE (`id` = 'MAXIMUM_BALANCE_ALERT');
UPDATE `alertsubtype` SET `value1` = '50' WHERE (`id` = 'DEPOSIT_AMOUNT_ALERT');
UPDATE `alertsubtype` SET `value1` = '50' WHERE (`id` = 'WITHDRAWAL_AMOUNT_ALERT');
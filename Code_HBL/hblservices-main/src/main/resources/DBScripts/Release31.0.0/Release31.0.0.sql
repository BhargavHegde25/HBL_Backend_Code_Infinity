/* Added few columns to store backendrequest and response in interbankfundtransfers */
ALTER TABLE `dbxdb`.`interbankfundtransfers`
ADD COLUMN `paymentId` VARCHAR(100) NULL AFTER `legalEntityId`;
ALTER TABLE `dbxdb`.`interbankfundtransfers`
ADD COLUMN `transactionNotes` VARCHAR(250) NULL AFTER `paymentId`;
ALTER TABLE `dbxdb`.`interbankfundtransfers`
ADD COLUMN `externalServicePayload` VARCHAR(1000) NULL AFTER `transactionNotes`;
ALTER TABLE `dbxdb`.`interbankfundtransfers`
ADD COLUMN `externalServiceResponse` VARCHAR(1000) NULL AFTER `externalServicePayload`;
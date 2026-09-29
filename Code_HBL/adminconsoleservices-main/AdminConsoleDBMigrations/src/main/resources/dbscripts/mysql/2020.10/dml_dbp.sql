INSERT INTO `alertfrequency` (`id`) VALUES ('DAILY');
INSERT INTO `alertfrequency` (`id`) VALUES ('WEEKLY');
INSERT INTO `alertfrequency` (`id`) VALUES ('MONTHLY');

INSERT INTO `alertfrequencytext` (`alertFrequencyId`, `languageCode`, `displayName`, `description`) VALUES ('DAILY', 'en-US', 'Daily', 'Daily');
INSERT INTO `alertfrequencytext` (`alertFrequencyId`, `languageCode`, `displayName`, `description`) VALUES ('WEEKLY', 'en-US', 'Weekly', 'Weekly');
INSERT INTO `alertfrequencytext` (`alertFrequencyId`, `languageCode`, `displayName`, `description`) VALUES ('MONTHLY', 'en-US', 'Monthly', 'Monthly');
INSERT INTO `requestcategory` (`id`, `Name`) VALUES ('RCID_DISPUTETRANSACTION', 'Dispute Transacation');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('92', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'DISPUTE_SCENARIOS_CONFIG', 'Configure dispute transaction scenarios', '{\"disputeScenariosConfig\":[{\"id\" : \"1\",\"description\" : \"InternalTransfer\",\"isSupported\" : \"true\"},{\"id\" : \"2\",\"description\" : \"BillPay\",\"isSupported\" : \"true\"},{\"id\" : \"3\",\"description\" : \"ExternalTransfer\",\"isSupported\" : \"true\"},{\"id\" : \"4\",\"description\" : \"Deposit\",\"isSupported\" : \"false\"},{\"id\" : \"5\",\"description\" : \"P2P\",\"isSupported\" : \"true\"},{\"id\" : \"6\",\"description\" : \"Cardless\",\"isSupported\" : \"true\"},{\"id\" : \"7\",\"description\" : \"CheckWithdrawal\",\"isSupported\" : \"true\"},{\"id\" : \"8\",\"description\" : \"Withdrawal\",\"isSupported\" : \"true\"},{\"id\" : \"9\",\"description\" : \"Interest\",\"isSupported\" : \"false\"},{\"id\" : \"10\",\"description\" : \"Request\",\"isSupported\" : \"false\"},{\"id\" : \"11\",\"description\" : \"Loan\",\"isSupported\" : \"false\"},{\"id\" : \"12\",\"description\" : \"ReceivedP2P\",\"isSupported\" : \"false\"},{\"id\" : \"13\",\"description\" : \"ReceivedRequest\",\"isSupported\" : \"false\"},{\"id\" : \"14\",\"description\" : \"StopCheckPaymentRequest\",\"isSupported\" : \"false\"},{\"id\" : \"15\",\"description\" : \"Wire\",\"isSupported\" : \"true\"},{\"id\" : \"16\",\"description\" : \"Credit\",\"isSupported\" : \"false\"},{\"id\" : \"17\",\"description\" : \"InternetTransaction\",\"isSupported\" : \"true\"},{\"id\" : \"18\",\"description\" : \"POS\",\"isSupported\" : \"true\"},{\"id\" : \"19\",\"description\" : \"CardPayment\",\"isSupported\" : \"true\"},{\"id\" : \"20\",\"description\" : \"Tax\",\"isSupported\" : \"false\"},{\"id\" : \"21\",\"description\" : \"Fee\",\"isSupported\" : \"false\"},{\"id\" : \"22\",\"description\" : \"SwiftPayment\",\"isSupported\" : \"true\"},{\"id\" : \"23\",\"description\" : \"Draft\",\"isSupported\" : \"true\"}]}', 'CLIENT', '0');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('93', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'DISPUTE_DURATION', 'Duration for which dispute transactions are maintained in days', '90', 'CLIENT', '0');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`) VALUES ('94', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'DISPUTE_ERROR_SCENARIOS', 'Error scenarios for dispute transaction', '{\"disputeTransactionErrors\":[{\"id\": \"disputeReason1\",\"value\": \"I don\'t recognize this transaction\"},{\"id\": \"disputeReason2\",\"value\": \"Goods and services not received\"},{\"id\": \"disputeReason3\",\"value\": \"Billing error\"},{\"id\": \"disputeReason4\",\"value\": \"Duplicate transaction\"},{\"id\": \"disputeReason5\",\"value\": \"Recurring debit which was cancelled\"}]}', 'CLIENT', '0');

INSERT INTO `alertfrequencyjobexectime` (`id`) VALUES ('1');



INSERT INTO `weekday` (`id`, `Name`) VALUES ('1', 'Monday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('2', 'Tuesday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('3', 'Wednesday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('4', 'Thursday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('5', 'Friday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('6', 'Saturday');
INSERT INTO `weekday` (`id`, `Name`) VALUES ('7', 'Sunday');



INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('1', 'en-US', 'Monday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('2', 'en-US', 'Tuesday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('3', 'en-US', 'Wednesday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('4', 'en-US', 'Thursday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('5', 'en-US', 'Friday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('6', 'en-US', 'Saturday');
INSERT INTO `weekdayvalue` (`weekdayId`, `languageCode`, `displayName`) VALUES ('7', 'en-US', 'Sunday');

UPDATE `channel` SET `sequence` = '1' WHERE (`id` = 'CH_SMS');
UPDATE `channel` SET `sequence` = '2' WHERE (`id` = 'CH_EMAIL');
UPDATE `channel` SET `sequence` = '3' WHERE (`id` = 'CH_NOTIFICATION_CENTER');
UPDATE `channel` SET `sequence` = '4' WHERE (`id` = 'CH_PUSH_NOTIFICATION');

UPDATE `alertfrequency` SET `sequence` = '1' WHERE (`id` = 'DAILY');
UPDATE `alertfrequency` SET `sequence` = '2' WHERE (`id` = 'WEEKLY');
UPDATE `alertfrequency` SET `sequence` = '3' WHERE (`id` = 'MONTHLY');


INSERT INTO `alertfrequencytime` (`id`) VALUES ('00:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('00:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('01:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('01:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('02:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('02:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('03:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('03:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('04:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('04:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('05:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('05:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('06:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('06:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('07:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('07:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('08:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('08:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('09:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('09:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('10:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('10:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('11:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('11:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('12:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('12:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('13:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('13:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('14:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('14:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('15:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('15:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('16:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('16:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('17:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('17:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('18:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('18:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('19:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('19:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('20:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('20:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('21:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('21:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('22:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('22:30:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('23:00:00');
INSERT INTO `alertfrequencytime` (`id`) VALUES ('23:30:00');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('95', 'DBP_CONFIG_BUNDLE', 'PREFERENCE', 'DISPUTE_TRANSFER_CONFIG', 'Configure dispute transfer scenarios', '{\"disputeTransferConfig\":[{\"id\" : \"1\",\"description\" : \"both\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"2\",\"description\" : \"debit\",\"isDisputeAllowed\" : \"true\"}\",{\"id\" : \"3\",\"description\" : \"credit\",\"isDisputeAllowed\" : \"true\"}]}', 'CLIENT', '0', '0');
UPDATE `configurations` SET `config_value` = '{\"disputeScenariosConfig\":[{\"id\" : \"1\",\"description\" : \"InternalTransfer\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"2\",\"description\" : \"BillPay\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"3\",\"description\" : \"ExternalTransfer\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"4\",\"description\" : \"Deposit\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"5\",\"description\" : \"P2P\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"6\",\"description\" : \"Cardless\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"7\",\"description\" : \"CheckWithdrawal\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"8\",\"description\" : \"Withdrawal\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"9\",\"description\" : \"Interest\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"10\",\"description\" : \"Request\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"11\",\"description\" : \"Loan\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"12\",\"description\" : \"ReceivedP2P\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"13\",\"description\" : \"ReceivedRequest\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"14\",\"description\" : \"StopCheckPaymentRequest\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"15\",\"description\" : \"Wire\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"16\",\"description\" : \"Credit\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"17\",\"description\" : \"InternetTransaction\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"18\",\"description\" : \"POS\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"19\",\"description\" : \"CardPayment\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"20\",\"description\" : \"Tax\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"21\",\"description\" : \"Fee\",\"isDisputeAllowed\" : \"false\"},{\"id\" : \"22\",\"description\" : \"SwiftPayment\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"23\",\"description\" : \"Draft\",\"isDisputeAllowed\" : \"true\"}]}' WHERE (`configuration_id` = '92');

INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('1', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'overdue', '2020-08-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('2', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'overdue', '2020-07-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('3', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'overdue', '2020-06-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('4', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2020-05-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('5', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2020-04-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('6', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2020-03-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('7', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2020-02-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('8', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2020-01-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('9', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-12-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('10', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-11-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('11', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-10-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('12', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-09-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('13', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-08-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('14', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'paid', '2019-07-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('15', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'future', '2020-09-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('16', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'future', '2020-10-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('17', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'future', '2020-11-28');
INSERT INTO `loanschedule` (`id`, `AccountId`, `Amount`, `Principal`, `Interest`, `OutstandingBalance`, `Charges`, `Tax`, `Insurance`, `CumulativeInterest`, `InstallmentType`, `Date`) VALUES ('18', '190128223246822', '9850', '5960', '3450', '144300', '250', '440', '440', '94300', 'future', '2020-12-28');
INSERT INTO `customeraccounts` (`id`, `Customer_id`, `Account_id`, `Organization_id`, `AccountName`, `FavouriteStatus`, `IsViewAllowed`, `IsDepositAllowed`, `IsWithdrawAllowed`, `IsOrganizationAccount`, `IsOrgAccountUnLinked`, `createdby`, `createdts`) VALUES ('fe916c3e-2e10-401a-a49a-0e62651315gt', '1002496540', '190128223246822', '200728095903957', 'Turbo Auto Loan', '0', '1', '1', '1', '1', '0', 'admin', '2020-07-28 09:59:52');

UPDATE `card` SET `withdrawlLimit` = '500', `withdrawalMinLimit` = '200', `withdrawalMaxLimit` = '3000', `withdrawalStepLimit` = '50', `purchaseLimit` = '2000', `purchaseMinLimit` = '200', `purchaseMaxLimit` = '5000', `purchaseStepLimit` = '50.00' WHERE (`Id` = '115');
UPDATE `card` SET `withdrawlLimit` = '550', `withdrawalMinLimit` = '100', `withdrawalMaxLimit` = '2500', `withdrawalStepLimit` = '100', `purchaseLimit` = '2500', `purchaseMinLimit` = '300', `purchaseMaxLimit` = '5500', `purchaseStepLimit` = '200' WHERE (`Id` = '116');
UPDATE `card` SET `withdrawlLimit` = '200', `withdrawalMaxLimit` = '500', `withdrawalStepLimit` = '20', `purchaseLimit` = '4000', `purchaseMinLimit` = '500', `purchaseMaxLimit` = '10000', `purchaseStepLimit` = '100' WHERE (`Id` = '951');
UPDATE `card` SET `withdrawlLimit` = '150', `withdrawalMinLimit` = '100', `withdrawalMaxLimit` = '500', `withdrawalStepLimit` = '50', `purchaseLimit` = '3400', `purchaseMinLimit` = '300', `purchaseMaxLimit` = '7000', `purchaseStepLimit` = '100' WHERE (`Id` = '117');

INSERT INTO `emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('22', 'ENROLLMENT_USERNAME_TEMPLATE', '<p>&nbsp;</p><center> <div style=\"width: 95%; height: 5px; margin: 0px 0px 0px 0px;\"> <div> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"background-color: #284e77; font-size: 1px; line-height: 1px; -webkit-text-size-adjust: none;\" align=\"center\" valign=\"top\" bgcolor=\"#284e77\" height=\"10\">&nbsp;</td> </tr> </tbody> </table> </div> </div> <div style=\"width: 95%; margin: 0px 0px 50px 0px;\"> <div style=\"display: inline-block; text-align: left;\"> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"background-color: #ffffff; -webkit-text-size-adjust: none;\"> <div style=\"margin: 50px 20px 50px 20px; color: #333b44; font-size: 14px;\"> <table style=\"width: 50%;\" width=\"50%\"> <tbody> <tr> <td width=\"200\"><img style=\"text-align: right; width: 200px; border: 0;\" src=\"https://retailbanking1.konycloud.com/dbimages/infinitydbxlogo.png\" alt=\"temenos_logo\" width=\"200\" /></td> </tr> </tbody> </table> <br /><br />Hi %firstName% %lastName%,<br /><br />You are enrolled to Digital Banking Channel. Please activate your account now.<br /> <br /> %userName% is your username. Activation code is sent to you registered mobile number.<br /><br /> You are required to input your username & activation code in the link below.<br /><br /> <div style=\"text-align: center; border-width: 1px; border-radius: 4px;\"> <table style=\"table-layout: fixed;\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"color: #ffffff; background-color: #333b44; word-wrap: break-word; word-break: break-all; padding: 10px; line-height: 20px;\" align=\"center\"> <div style=\"margin: 10px 10px 10px 10px; font-size: 13px;\">To activate your account and set a password,<a style=\"color: #11abeb;\" href=\"%resetPasswordLink%\">click here</a> <br /> or paste the following link on your browser: <br /><br /><a style=\"color: #11abeb;\">%resetPasswordLink%</a></div> </td> </tr> </tbody> </table> </div> <br /> <br />Regards,<br />Temenos Banking Team <br /><br /><br /><br /><span style=\"color: #999999; font-size: 12px;\">This is a system generated mail. If you are not the named addressee please notify the sender immediately by e-mail at support@temenosbank.com and then delete the e-mail from your system. Although the company has taken reasonable precautions to ensure no viruses are present in this email, the company cannot accept responsibility for any loss or damage arising from the use of this email or attachments. Temenos, Inc. www.temenos.com </span><br /><br /> <div align=\"center\"><span style=\"color: #999999;\">Copyright &copy; 2020 Temenos Digital DBX. All rights reserved.</span></div> </div> </td> </tr> </tbody> </table> </div> </div></center>', 'Account Activation', 'Temenos Digital', 'dbx_cl@infinity.com');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('5c4dc967-543d-48db-a8d9-9d4169634375', 'C360_CONFIG_BUNDLE', 'PREFERENCE', 'ACTIVATIONCODE_EXPIRYTIME', 'Time indicating the expiry time of activation code', '43800', 'SERVER', '1', 'Kony dev', 'Kony dev', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '0');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('0e6a9d6e-d261-4795-9806-a04b37835cf5', 'C360_CONFIG_BUNDLE', 'PREFERENCE', 'ACTIVATIONCODE_LENGTH', 'Length of the activation code to be generated from enrollment flow', '5', 'SERVER', '0', 'Kony dev', 'Kony dev', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '0');
INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('a5f3b851-acb6-4a03-aeb2-4ad6d0fc8cbb', 'C360_CONFIG_BUNDLE', 'PREFERENCE', 'ACTIVATIONCODE_VALIDATIONATTEMPTS', 'Count indicating number of times activation code can be sent', '5', 'SERVER', '0', 'Kony dev', 'Kony dev', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '2020-08-27 18:54:54', '0');

INSERT INTO `systemconfiguration` (`id`, `PropertyName`, `PropertyValue`) VALUES ('14', 'USERNAME_LENGTH', '8');

INSERT INTO `emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('23', 'ENROLLMENT_ACTIVATIONCODE_TEMPLATE', 'Dear Customer, You are enrolled to digital banking channel. %otp% is your activation code. Use it to activate your profile. Username & activation link are sent to your registered email', 'Account Activation', 'Temenos Digital', 'dbx_cl@infinity.com');

UPDATE `configurations` SET `config_value` = '{\"disputeTransferConfig\":[{\"id\" : \"1\",\"description\" : \"both\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"2\",\"description\" : \"debit\",\"isDisputeAllowed\" : \"true\"},{\"id\" : \"3\",\"description\" : \"credit\",\"isDisputeAllowed\" : \"true\"}]}' WHERE (`configuration_id` = '95');

INSERT INTO `cardproducts` (`productId`, `productName`, `featureOverview`, `featureDescription`, `representativeLabel1`, `representativeLabel2`, `representativeLabel3`, `representativeValue1`, `representativeValue2`, `representativeValue3`, `withdrawlLimit`,`withdrawalMinLimit`,`withdrawalMaxLimit`,`withdrawalStepLimit`,`purchaseLimit`,`purchaseMinLimit`,`purchaseMaxLimit`,`purchaseStepLimit`) VALUES ('1','Maverick Debit Card', '• Get up to $480 Cashback every year
• 5% Cashback on shopping via Shopzapp
', '• 2.5% Cashback on all online spends
• 1% Cashback on all offline spends and Wallet reloads
', 'Daily Purchase Limit', 'Daily Withdrawal Limit', 'Accidental Health Insurance Cover', '$10000', '$20000', '$100000','20000','0','20000','','10000','0','10000','');
INSERT INTO `cardproducts` (`productId`,`productName`, `featureOverview`, `featureDescription`, `representativeLabel1`, `representativeLabel2`, `representativeLabel3`, `representativeValue1`, `representativeValue2`, `representativeValue3`, `withdrawlLimit`,`withdrawalMinLimit`,`withdrawalMaxLimit`,`withdrawalStepLimit`,`purchaseLimit`,`purchaseMinLimit`,`purchaseMaxLimit`,`purchaseStepLimit`) VALUES ('2','Shop@Ease Platinum Card', '• 4 Complimentary Domestic Airport Lounge access annually 
 • Accidental insurance cover up to $ 100,000', '• 2.5% Cashback on all online spends
• 1% Cashback on all offline spends and Wallet reloads
', 'Daily Purchase Limit', 'Daily Withdrawal Limit', 'Accidental Health Insurance Cover', '$10000', '$20000', '$100000','20000','0','20000','','10000','0','10000','');
INSERT INTO `cardproducts` (`productId`,`productName`, `featureOverview`, `featureDescription`, `representativeLabel1`, `representativeLabel2`, `representativeLabel3`, `representativeValue1`, `representativeValue2`, `representativeValue3`, `withdrawlLimit`,`withdrawalMinLimit`,`withdrawalMaxLimit`,`withdrawalStepLimit`,`purchaseLimit`,`purchaseMinLimit`,`purchaseMaxLimit`,`purchaseStepLimit`) VALUES ('3','Classic Cashback Card', '• Upto 1% Cashback on retail and online shopping (Max $ 500/month)• Up to $ 25000 Personal Accidental Death Cover (rail/ road/ air)', 'Cash withdrawal facility can now be availed across merchant establishments with a maximum upper limit of $1000/ day on your Infinity Bank Debit Cards.', 'Daily Purchase Limit', 'Daily Withdrawal Limit', 'Accidental Health Insurance Cover', '$10000', '$20000', '$100000','20000','0','20000','','10000','0','10000','');
INSERT INTO `cardproducts` (`productId`,`productName`, `featureOverview`, `featureDescription`, `representativeLabel1`, `representativeLabel2`, `representativeLabel3`, `representativeValue1`, `representativeValue2`, `representativeValue3`, `withdrawlLimit`,`withdrawalMinLimit`,`withdrawalMaxLimit`,`withdrawalStepLimit`,`purchaseLimit`,`purchaseMinLimit`,`purchaseMaxLimit`,`purchaseStepLimit`) VALUES ('4','Rewards Priority Card', ' • Upto 12% of purchases can be converted as Airmiles for Leading Airlines.
 • Upto 20% Cashback on Food Delivery and Takeaway Food Orders.', 'Rewards bonus on $ 5000 Spend on Apparrels and Movie Tickets.', 'Daily Purchase Limit', 'Daily Withdrawal Limit', 'Accidental Health Insurance Cover', '$10000', '$20000', '$100000','20000','0','20000','','10000','0','10000','');
 
 
 INSERT INTO `cardproducttype` (`productId`,`accountType`) VALUES ('1','Savings');
 INSERT INTO `cardproducttype` (`productId`,`accountType`) VALUES ('2','Savings');
 INSERT INTO `cardproducttype` (`productId`,`accountType`) VALUES ('3','Checking');
 INSERT INTO `cardproducttype` (`productId`,`accountType`) VALUES ('4','Checking');

UPDATE `emailtemplates` SET `TemplateText`='<p>&nbsp;</p><center> <div style=\"width: 95%; height: 5px; margin: 0px 0px 0px 0px;\"> <div> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"background-color: #284e77; font-size: 1px; line-height: 1px; -webkit-text-size-adjust: none;\" align=\"center\" valign=\"top\" bgcolor=\"#284e77\" height=\"10\">&nbsp;</td> </tr> </tbody> </table> </div> </div> <div style=\"width: 95%; margin: 0px 0px 50px 0px;\"> <div style=\"display: inline-block; text-align: left;\"> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"background-color: #ffffff; -webkit-text-size-adjust: none;\"> <div style=\"margin: 50px 20px 50px 20px; color: #333b44; font-size: 14px;\"> <table style=\"width: 50%;\" width=\"50%\"> <tbody> <tr> <td width=\"200\"><img style=\"text-align: right; width: 200px; border: 0;\" src=\"https://retailbanking1.konycloud.com/dbimages/infinitydbxlogo.png\" alt=\"temenos_logo\" width=\"200\" /></td> </tr> </tbody> </table> <br /><br />Hi %firstName% %lastName%,<br /><br />You are enrolled to Digital Banking Channel. Please activate your account now.<br /> <br /> %userName% is your username. Activation code is sent to you registered mobile number.<br /><br /> You are required to input your username & activation code in the link below.<br /><br /> <div style=\"text-align: center; border-width: 1px; border-radius: 4px;\"> <table style=\"table-layout: fixed;\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <tbody> <tr> <td style=\"color: #ffffff; background-color: #333b44; word-wrap: break-word; word-break: break-all; padding: 10px; line-height: 20px;\" align=\"center\"> <div style=\"margin: 10px 10px 10px 10px; font-size: 13px;\">To activate your account and set a password,<a style=\"color: #11abeb;\" href=\"%resetPasswordLink%\">click here</a> <br /> or paste the following link on your browser: <br /><br /><a style=\"color: #11abeb;\">%resetPasswordLink%</a></div> </td> </tr> </tbody> </table> </div> <br/> <center>The activation code will expire in %activationCodeExpiry% days , so activate it right away.</center> <br /> <br />Regards,<br />Temenos Banking Team <br /><br /><br /><br /><span style=\"color: #999999; font-size: 12px;\">This is a system generated mail. If you are not the named addressee please notify the sender immediately by e-mail at support@temenosbank.com and then delete the e-mail from your system. Although the company has taken reasonable precautions to ensure no viruses are present in this email, the company cannot accept responsibility for any loss or damage arising from the use of this email or attachments. Temenos, Inc. www.temenos.com </span><br /><br /> <div align=\"center\"><span style=\"color: #999999;\">Copyright &copy; 2020 Temenos Digital DBX. All rights reserved.</span></div> </div> </td> </tr> </tbody> </table> </div> </div></center>' WHERE `id`='22';

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7cab1e8a-ee68-11ea-adc1-0242ac120002', 'RBObjects', 'CardProducts', 'getCardProducts', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d25638ce-ee68-11ea-adc1-0242ac120002', 'RBObjects', 'Cards', 'applyForDebitCard', 'ALLOW');

UPDATE `card` SET `withdrawlLimit` = '2000', `withdrawalMinLimit` = '100', `withdrawalMaxLimit` = '5000', `withdrawalStepLimit` = '50', `purchaseMinLimit` = '500', `purchaseMaxLimit` = '5000' WHERE (`Id` = '346');
UPDATE `card` SET `withdrawlLimit` = '550', `withdrawalMinLimit` = '200', `withdrawalMaxLimit` = '4000', `withdrawalStepLimit` = '50', `purchaseLimit` = '4000', `purchaseMinLimit` = '400', `purchaseMaxLimit` = '6000' WHERE (`Id` = '347');
UPDATE `card` SET `withdrawlLimit` = '1000', `withdrawalMinLimit` = '200', `withdrawalMaxLimit` = '7000', `withdrawalStepLimit` = '100', `purchaseLimit` = '3000', `purchaseMinLimit` = '300', `purchaseMaxLimit` = '7000' WHERE (`Id` = '348');
UPDATE `card` SET `withdrawlLimit` = '1000', `withdrawalMinLimit` = '200', `withdrawalMaxLimit` = '3000', `withdrawalStepLimit` = '50', `purchaseLimit` = '1000', `purchaseMinLimit` = '500', `purchaseMaxLimit` = '10000', `purchaseStepLimit` = '50' WHERE (`Id` = '949');
UPDATE `card` SET `withdrawlLimit` = '1500', `withdrawalMinLimit` = '200', `withdrawalMaxLimit` = '3500', `withdrawalStepLimit` = '20', `purchaseLimit` = '1500', `purchaseMinLimit` = '100', `purchaseMaxLimit` = '7000', `purchaseStepLimit` = '50' WHERE (`Id` = '950');

INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CARD_MANAGEMENT_UPDATE_PURCHASE', 'CARD_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Update purchase limit', 'Update daily purchase limit', '0', '0', '0', '9', '2020-09-08 07:35:56', '2020-09-08 07:35:56', '2020-09-08 07:35:56', '0');
INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`, `DisplaySequence`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'CARD_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Update withdrawal limit', 'Update daily withdrawal limit', '0', '0', '0', '12', '2020-08-31 12:24:35', '2020-08-31 12:24:35', '2020-08-31 12:24:35', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_UPDATE_PURCHASE', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_BUSINESS', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_UPDATE_PURCHASE', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '0');
INSERT INTO `featureactionroletype` (`RoleType_id`, `Action_id`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('TYPE_ID_RETAIL', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '2020-08-31 12:25:19', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CAID160', 'PID45', 'CARD_MANAGEMENT_UPDATE_PURCHASE', 'CARD_MANAGEMENT', '1', 'Kony Dev', 'Kony User', '2020-08-31 12:24:41', '2020-08-31 12:24:41', '2020-08-31 12:24:41', '0');
INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('CAID161', 'PID45', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'CARD_MANAGEMENT', '1', 'Kony Dev', 'Kony User', '2020-08-31 12:24:41', '2020-08-31 12:24:41', '2020-08-31 12:24:41', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('a9bc4949-791b-11ea-9300-00090faa0001', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_UPDATE_PURCHASE', 'UID11', '2020-08-31 12:24:42', '2020-08-31 12:24:42', '2020-08-31 12:24:42', '0');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('a9bc4950-791b-11ea-9300-00090faa0001', 'DEFAULT_GROUP', 'CARD_MANAGEMENT_UPDATE_WITHDRAWAL', 'UID11', '2020-08-31 12:24:42', '2020-08-31 12:24:42', '2020-08-31 12:24:42', '0');

INSERT INTO `eventtopicconfiguration` (`eventCode`, `topic`) VALUES ('SCA_ACTIVATIONCODE', '/events/scaactivationcode');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('d044798a-1357-4d16-aa09-a86205035078', 'RBObjects', 'SwiftCode', 'getBICFromBankDetails', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7b07fa1e-d789-437a-b548-1aad086c7c8c', 'RBObjects', 'ExternalAccounts', 'getBeneficiaryName', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('f6ce7cde-eca7-4ba8-beb0-c43dd7e27a09', 'RBObjects', 'Transactions', 'getBankDate', 'ALLOW');

-- Alert Category
UPDATE dbxalertcategory SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_ACCOUNTS';
UPDATE dbxalertcategory SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_SECURITY';
UPDATE dbxalertcategory SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ALERT_CAT_TRANSACTIONAL';

-- Alert Group

UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='ACCOUNT';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='BILL_PAYEE';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='COMBINED_ACCESS';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='CREDENTIAL_CHANGE';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_AMOUNT';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='LOGIN'; 
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='MAKE_TRANSFER';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='P2P_RECIPIENT';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PROFILE_UPDATE';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='SECURE_MESSAGE';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='TRANSFER_RECIPIENT';
UPDATE dbxalerttype SET defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE  id='WITHDRAWAL_AMOUNT';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='BALANCE';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PAYMENT_DUE_DATE';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='PAYMENT_OVERDUE';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='CHECK';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DAILY_BALANCE';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_REMINDER';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DEPOSIT_WITHDRAWAL';
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='MAXIMUM_BALANCE'; 
UPDATE dbxalerttype SET isAccountLevel='1', defaultFrequencyId='DAILY', defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='OVERDRAFT';

-- Alert Group Channel

INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BILL_PAYEE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BILL_PAYEE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BILL_PAYEE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BILL_PAYEE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CHECK', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CHECK', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CHECK', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CHECK', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'COMBINED_ACCESS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'COMBINED_ACCESS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'COMBINED_ACCESS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'COMBINED_ACCESS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CREDENTIAL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CREDENTIAL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CREDENTIAL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CREDENTIAL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'LOGIN', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'LOGIN', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'LOGIN', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'LOGIN', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAKE_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAKE_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAKE_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAKE_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAXIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAXIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAXIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAXIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OVERDRAFT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OVERDRAFT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OVERDRAFT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OVERDRAFT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'P2P_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'P2P_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'P2P_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'P2P_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PROFILE_UPDATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PROFILE_UPDATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PROFILE_UPDATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PROFILE_UPDATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SECURE_MESSAGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SECURE_MESSAGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SECURE_MESSAGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SECURE_MESSAGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSFER_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSFER_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSFER_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSFER_RECIPIENT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alerttypechannel (channelId, alertTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL_AMOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');

-- Alert

UPDATE alertsubtype SET isGlobal='1'  WHERE id='ACCOUNT_LOAD';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='UNSUPPORTED_ACCOUNT';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='TRANSACTIONS_NOT_AVAILABLE';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='TRANSACTION_LOAD';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='REMOVE_ACCOUNT';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='USER_LINKED';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='USER_DELINKED';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='USER_DEACTIVATED';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='USERNAME_CHANGE';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='PASSWORD_CHANGE';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='ACCOUNT_LOCKED';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='LOGIN_ATTEMPT';
UPDATE alertsubtype SET isGlobal='1'  WHERE id='SECURE_MESSAGE_ALERT';
UPDATE alertsubtype SET isAccountLevel='1', attributeId='AMOUNT' , alertConditionId='EQUALS_TO' , value1='50' WHERE id='MINIMUM_BALANCE';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='CHECK_STATUS';
UPDATE alertsubtype SET isAccountLevel='1', defaultFrequencyId='DAILY' , defaultFrequencyValue=NULL, defaultFrequencyTime='10:00:00' WHERE id='DAILY_BALANCE';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='DEPOSIT_MATURITY_REMINDER';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='DEPOSITS';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='WITHDRAWAL';
UPDATE alertsubtype SET isAccountLevel='1', attributeId='AMOUNT' , alertConditionId='GREATER_THAN'  WHERE id='MAXIMUM_BALANCE_ALERT';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='PAYMENT_OVERDUE';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='PAYMENT_DUE_DATE';
UPDATE alertsubtype SET isAccountLevel='1'  WHERE id='OVERDRAFT_ALERT';
UPDATE alertsubtype SET attributeId='AMOUNT' , alertConditionId='GREATER_THAN' WHERE id='DEPOSIT_AMOUNT_ALERT';
UPDATE alertsubtype SET attributeId='AMOUNT' , alertConditionId='GREATER_THAN' WHERE id='WITHDRAWAL_AMOUNT_ALERT';

-- Alert Text

INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('MINIMUM_BALANCE', 'en-US', 'Minimum Balance', 'Alert when account\'s balance is below the defined value.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('CHECK_STATUS', 'en-US', 'Check Status', 'Check has been posted to my account or has been rejected', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DAILY_BALANCE', 'en-US', 'Daily Balance Alert', 'Notify the customer of their account balance daily.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSIT_MATURITY_REMINDER', 'en-US', 'Deposit Maturity Alert', 'Notify the customers prior to the maturity date of their time deposit.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSITS', 'en-US', 'Deposits', 'When an amount is credited into the account.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('WITHDRAWAL', 'en-US', 'Withdrawal', 'When an amount is debited into the account.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('MAXIMUM_BALANCE_ALERT', 'en-US', 'Maximum Balance', 'An alert is sent to when the account reaches the maximum balance amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('OVERDRAFT_ALERT', 'en-US', 'Overdraft Alerts', 'Alert when the account is overdrawn.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PAYMENT_DUE_DATE', 'en-US', 'Payment Due Date Alert', 'Notify the customer prior to the due date of the payment.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PAYMENT_OVERDUE', 'en-US', 'Payment Overdue', 'When the customer\'s payment is overdue.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_DEACTIVATED', 'en-US', 'The other user is deactivated', 'The other user is successfully deactivated', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_DELINKED', 'en-US', 'The combined user is delinked', 'The combined user is successfully delinked', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USER_LINKED', 'en-US', 'The combined user is linked', 'The combined user is successfully linked', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PASSWORD_CHANGE', 'en-US', 'Password Change', 'Alert when customer\'s sign in password is changed', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('USERNAME_CHANGE', 'en-US', 'Username Change', 'Alert when customer\'s sign in username is changed', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ACCOUNT_LOCKED', 'en-US', 'Account Locked', 'Alert when customer exceeds maximum failed sign in attempts & the account gets locked', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('LOGIN_ATTEMPT', 'en-US', 'Sign In Attempt', 'Alert when a successful or failed sign in attempt is made', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_ADDRESS_CHANGE', 'en-US', 'Primary Address Change', 'Alert when customer\'s primary address is changed/updated.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_EMAIL_CHANGE', 'en-US', 'Primary Email Change', 'Alert when customer\'s primary email is changed/updated.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('PRIMARY_PHONE_CHANGE', 'en-US', 'Primary Phone Number Change', 'Alert when customer\'s primary phone number is changed/updated.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SECURE_MESSAGE_ALERT', 'en-US', 'Secure Message Alert', 'Send Alerts to customers when a secure message is received by the customer', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ACCOUNT_LOAD', 'en-US', 'Account Load', 'Account Load', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('REMOVE_ACCOUNT', 'en-US', 'Remove Account', 'Remove Account', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('TRANSACTIONS_NOT_AVAILABLE', 'en-US', 'Transactions Not Available', 'Transactions Not Available', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('TRANSACTION_LOAD', 'en-US', 'Transaction Load', 'Transaction Load', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('UNSUPPORTED_ACCOUNT', 'en-US', 'Unsupported Account', 'Unsupported Account', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('BILL_PAYEE_ADDED', 'en-US', 'Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('NON_REG_BILL_PAYEE_ADDED', 'en-US', 'Non Registered Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('REGISTERED_BILL_PAYEE_ADDED', 'en-US', 'Registered Bill Payee Added', 'Alert when Bill Payee Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DEPOSIT_AMOUNT_ALERT', 'en-US', 'Deposit Amount Alert', 'An alert is sent to indicate a deposit amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ONETIME_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Onetime Transfer', 'Alert when a other bank onetime transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('ONETIME_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Onetime Transfer', 'Alert when own account onetime transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('RECURRING_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Recurring Transfer', 'Alert when a other bank recurring transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('RECURRING_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Recurring Transfer', 'Alert when own account recurring transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SCHEDULED_OTHER_BANK_TRANSFER', 'en-US', 'Other Bank Scheduled Transfer', 'Alert when a other bank scheduled transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SCHEDULED_OWN_ACCOUNT_TRANSFER', 'en-US', 'Own Account Scheduled Transfer', 'Alert when own account scheduled transfer is made.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('P2P_RECIPIENT_ADDED', 'en-US', 'P2P Recipient Added', 'Alert when P2P Recipient Added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('DOM_WIRE_RECIPIENT_ADDED', 'en-US', 'Domestic Wire Recipient Added', 'Alert when domestic wire recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('INT_TRANSFER_RECIPIENT_ADDED', 'en-US', 'International Transfer Recipient Added', 'Alert when international transfer recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('INT_WIRE_RECIPIENT_ADDED', 'en-US', 'International Wire Recipient Added', 'Alert when international wire recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('OTHER_BANK_RECIPIENT_ADDED', 'en-US', 'Other Bank Recipient Added', 'Alert when other bank transfer recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('SAME_BANK_RECIPIENT_ADDED', 'en-US', 'Same Bank Recipeint Added', 'Alert when same bank recipient is added.', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypetext (alertSubTypeId, languageCode, displayName, description, createdby, createdts, softdeleteflag) VALUES ('WITHDRAWAL_AMOUNT_ALERT', 'en-US', 'Withdrawal Amount Alert', 'An alert is sent to indicate a withdrawal amount that meets the threshold requirements.', 'infinityuser', CURRENT_TIMESTAMP, '0');


-- Alert Channel

INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_EMAIL', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_NOTIFICATION_CENTER', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_PUSH_NOTIFICATION', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypechannel (channelId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('CH_SMS', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');

-- Alert and App Relation

INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeapp (appId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('RETAIL_AND_BUSINESS_BANKING', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');


-- Alert and Customer Type Relation

INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'REMOVE_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'TRANSACTIONS_NOT_AVAILABLE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'TRANSACTION_LOAD', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'UNSUPPORTED_ACCOUNT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'NON_REG_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'REGISTERED_BILL_PAYEE_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_DEACTIVATED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_DELINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USER_LINKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PASSWORD_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'USERNAME_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ACCOUNT_LOCKED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'LOGIN_ATTEMPT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ONETIME_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'ONETIME_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'RECURRING_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'RECURRING_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SCHEDULED_OTHER_BANK_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SCHEDULED_OWN_ACCOUNT_TRANSFER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'P2P_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_ADDRESS_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_EMAIL_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'PRIMARY_PHONE_CHANGE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SECURE_MESSAGE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'DOM_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'INT_TRANSFER_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'INT_WIRE_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'OTHER_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'SAME_BANK_RECIPIENT_ADDED', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_BUSINESS', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypecustomertype (customerTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('TYPE_ID_RETAIL', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');


-- Alert and Account Type Relation
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'MINIMUM_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'CHECK_STATUS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'DAILY_BALANCE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSIT_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSIT_MATURITY_REMINDER', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'DEPOSITS', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'WITHDRAWAL', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'MAXIMUM_BALANCE_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('4', 'OVERDRAFT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('3', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('6', 'PAYMENT_DUE_DATE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('3', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('6', 'PAYMENT_OVERDUE', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('1', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');
INSERT INTO alertsubtypeaccounttype (accountTypeId, alertSubTypeId, createdby, createdts, softdeleteflag) VALUES ('2', 'WITHDRAWAL_AMOUNT_ALERT', 'infinityuser', CURRENT_TIMESTAMP, '0');

INSERT INTO `configurations` (`configuration_id`, `bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `createdby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('fb1c78c2-af07-4dd4-b7db-abcd6769e993', 'C360_CONFIG_BUNDLE', 'PREFERENCE', 'CAPTCHA_LENGTH', 'Length of the captcha', '5', 'SERVER', '1', 'UID10', '2020-09-11 05:35:26', '2020-09-11 05:35:26', '2020-09-11 05:35:26', '0');

INSERT INTO `customerviewalertconfiguration` (`id`, `alertPreferenceView`, `enableFrequency`, `enableSeparateContact`, `createdby`, `softdeleteflag`) VALUES ('1', 'GROUP', '0', '0', 'infinityuser', '0');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ef83b4c1-3b78-45ff-a9e4-b145f20340fc', 'RBObjects', 'DbxUser', 'sendActivationCodeForEnrollment', 'API_ACCESS');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('99e26596-27a2-48ec-8fd4-2e014c35f07b', 'RBObjects', 'DbxUser', 'validateActivationCodeForEnrollment', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b4510268-0c44-4460-b052-9ff11cfeeb7c', 'RBObjects', 'DbxUser', 'UpdatePasswordForActivationFlow', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('1ef10822-8bf9-41cf-bd42-5b45372ed009', 'RBObjects', 'DbxUser', 'regenerateActivationCode', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('5bd77f16-80bf-4309-bd6c-fd425649103f', 'RBObjects', 'Security', 'generateCaptcha', 'ALLOW');

UPDATE `loanschedule` SET `InstallmentType` = 'DUE' WHERE (`id` = '1');
UPDATE `loanschedule` SET `InstallmentType` = 'DUE' WHERE (`id` = '2');
UPDATE `loanschedule` SET `InstallmentType` = 'DUE' WHERE (`id` = '3');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '4');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '5');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '6');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '7');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '8');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '9');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '10');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '11');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '12');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '13');
UPDATE `loanschedule` SET `InstallmentType` = 'PAID' WHERE (`id` = '14');
UPDATE `loanschedule` SET `InstallmentType` = 'FUTURE' WHERE (`id` = '15');
UPDATE `loanschedule` SET `InstallmentType` = 'FUTURE' WHERE (`id` = '16');
UPDATE `loanschedule` SET `InstallmentType` = 'FUTURE' WHERE (`id` = '17');
UPDATE `loanschedule` SET `InstallmentType` = 'FUTURE' WHERE (`id` = '18');

INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('0b254305-a331-4fcb-acd2-e28fd7d26a49','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MIN_GOAL_AMOUNT', 'Minimum Goal Amount', '1', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('42ec72f4-890e-481f-8790-ae048ed57c65','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MAX_GOAL_AMOUNT', 'Maximum Goal Amount', '12000000', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('6d9deb94-5ae6-4e66-9e03-4f856fd61b93','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MIN_BUDGET_AMOUNT', 'Minimum Budget Amount', '1', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('a6b6f7df-7023-4beb-8078-35219a3d1d83','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MIN_MONTHS_ForGOAL', 'Minimum Number of Months', '1', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('321bdc34-2d00-4310-99d1-e12b5a86aba7','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MAX_MONTHS_ForGOAL', 'Maximum Number of Months', '120', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('6db3a650-0b8c-4d23-b07d-43c1ec4fd094','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MIN_MONTHLYDEBIT_AMOUNT', 'Minimum Monthly Amount for debit', '1', 'CLIENT', '0', '0');
INSERT INTO `configurations` (`configuration_id`,`bundle_id`, `config_type`, `config_key`, `description`, `config_value`, `target`, `isPreLoginConfiguration`, `softdeleteflag`) VALUES ('635af2f4-e29b-49dc-a9a9-6d621f65e71c','DBP_CONFIG_BUNDLE', 'PREFERENCE', 'MAX_MONTHLYDEBIT_AMOUNT', 'Maximum Monthly Amount for debit', '100000', 'CLIENT', '0', '0');

INSERT INTO `feature` (`id`, `App_id`, `name`, `description`, `Type_id`, `Status_id`, `DisplaySequence`, `isPrimary`) VALUES ('LOAN_SCHEDULE', 'RETAIL_AND_BUSINESS_BANKING', 'Loan Schedule Transactions', 'Loan schedule', 'NON_MONETARY', 'SID_FEATURE_ACTIVE', '37', '1');
INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`, `name`, `description`, `isAccountLevel`, `isMFAApplicable`, `isPrimary`) VALUES ('VIEW_LOAN_SCHEDULE', 'LOAN_SCHEDULE', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Loan schedule', 'Allows user to view loan schedule', '0', '0', '1');
INSERT INTO `groupactionlimit` (`id`, `Group_id`, `Action_id`) VALUES ('2a17f967-5ba9-47c0-ba8d-4ed723ef2d2e', 'DEFAULT_GROUP', 'VIEW_LOAN_SCHEDULE');


INSERT INTO eventtype (id,`Name`,`ActivityType`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PARTY','Party','CUSTOMER',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO eventtype (id,`Name`,`ActivityType`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PROSPECT','Prospect','CUSTOMER',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO eventsubtype (id,eventtypeid,`Name`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PARTY_CRETE','PARTY','Party Create',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO eventsubtype (id,eventtypeid,`Name`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PARTY_UPDATE','PARTY','Party Update',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO eventsubtype (id,eventtypeid,`Name`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PROSPECT_CRETE','PROSPECT','Prospect Create',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);

INSERT INTO eventsubtype (id,eventtypeid,`Name`,`Description`,createdby,modifiedby,createdts,lastmodifiedts,synctimestamp,softdeleteflag) VALUES 
('PROSPECT_UPDATE','PROSPECT','Prospect Update',NULL,NULL,NULL,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,0);


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('0a0ab988-0161-11eb-adc1-0242ac120002', 'RBObjects', 'Configurations', 'getSystemConfigurations', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b1aaa963-c370-459b-8dd9-861ba64179b3', 'RBObjects', 'DbxUserAlerts', 'getAlertChannels', 'ALLOW');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('LOAN_SCHEDULE', 'en-US', 'View Loan Schedule ', 'View Loan Schedule ');

INSERT INTO `featuredisplaynamedescription` (`Feature_id`, `Locale_id`, `displayName`, `displayDescription`) VALUES ('LOAN_SCHEDULE', 'en-GB', 'View Loan Schedule ', 'View Loan Schedule ');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_RETAIL', 'LOAN_SCHEDULE');

INSERT INTO `featureroletype` (`RoleType_id`, `Feature_id`) VALUES ('TYPE_ID_BUSINESS', 'LOAN_SCHEDULE');

INSERT INTO `compositeaction` (`id`, `Permission_id`, `Action_id`, `Feature_id`, `isEnabled`, `createdby`, `modifiedby`) VALUES ('CAID162', 'PID45', 'VIEW_LOAN_SCHEDULE', 'LOAN_SCHEDULE', '1', 'Kony Dev', 'Kony Dev');

INSERT INTO `rolecompositeaction` (`Role_id`, `CompositeAction_id`, `isEnabled`, `createdby`, `modifiedby`) VALUES ('RID_SUPERADMIN', 'CAID162', '1', 'Kony Dev', 'Kony User');

INSERT INTO `rolecompositeaction` (`Role_id`, `CompositeAction_id`, `isEnabled`, `createdby`, `modifiedby`) VALUES ('RID_BUSINESS', 'CAID162', '1', 'Kony Dev', 'Kony User');

UPDATE `customercommunication` SET `Extension` = 'Mobile' WHERE (`Type_id` = 'COMM_TYPE_PHONE' AND (ISNULL(`Extension`) OR `Extension` = 'Personal' ));


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7278713f-3b37-4698-9f80-91596739b616', 'RBObjects', 'Accounts', 'getCreditCardAccounts', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('836e87c2-4b86-4bff-bfa7-b30cb3ad0a2f', 'RBObjects', 'Transactions', 'createCreditCardTransfer', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cb725968-a69c-4ceb-a55c-d3c3897eb0f1', 'RBObjects', 'Transactions', 'createOneTimeTransfer', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('4530fa94-7c11-44cb-b44a-da492aefed6a', 'RBObjects', 'Accounts', 'getCustomView', 'CUSTOM_VIEW_MANAGE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('18b82daf-b5a2-4727-8fef-585a26e752cf', 'RBObjects', 'Accounts', 'updateCustomView', 'CUSTOM_VIEW_MANAGE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('18fc7485-c696-4b98-adc7-15d67ee9feac', 'RBObjects', 'Accounts', 'createCustomView', 'CUSTOM_VIEW_MANAGE');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('22b19df7-f3af-4fe9-b2cb-db92575d5b24', 'RBObjects', 'Accounts', 'deleteCustomView', 'CUSTOM_VIEW_MANAGE');

INSERT INTO `service_permission_mapper` (`id`,`service_name`, `object_name`, `operation`, `permissions`) VALUES ('002bea3b-7f21-462f-9a62-360039956333','RBObjects', 'Transactions', 'getLoanSchedule', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cad530ef-7340-43d7-80f4-43a7aa7102f1', 'TransactionAdvice', 'TransactionStatement', 'get', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cad530ef-7340-43d7-80f4-46a6aa7102f1', 'TransactionAdvice', 'TransactionStatement', 'getTransactionStatementsByYear', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ggf944-f522-75364-63ea-5dd627e92281h', 'RBObjects', 'DownloadAttachments', 'get', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7cab1e8a-e968-11ea-adc1-0242ac120902', 'SavingsPot', 'SavingsPot', 'createSavingsPot', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7cab1e8a-ez68-11ea-adc1-0242ac120002', 'SavingsPot', 'SavingsPot', 'closeSavingsPot', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7caz1e8a-ee68-11ea-adc1-0242ac120002', 'SavingsPot', 'SavingsPot', 'getAllSavingsPot', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7zab1e8a-ee68-11ea-adc1-0242ac120002', 'SavingsPot', 'SavingsPot', 'updateSavingsPotBalance', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7cab1e8a-ee68-11za-adc1-0242ac120002', 'SavingsPot', 'SavingsPot', 'updateSavingsPot', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7cab1e8a-ee68-11ea-adc1-0242zc120002', 'SavingsPot', 'SavingsPotCategories', 'getCategoriesForGoal', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cad530ef-7340-43d7-80f4-43a6aa7102g1', 'TransactionAdvice', 'TransactionAdviceObject', 'get', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('cad530ef-7240-43d7-80f4-43a6aa7102f1', 'TransactionAdvice', 'TransactionAdviceObject', 'getBase64', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('002bea3b-7f21-462f-9a62-360039234901', 'LoanPayoff', 'LoanBillObject', 'getByParam', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('002bea3b-7f21-462f-9a62-360039234902', 'LoanPayoff', 'LoanSimulateObject', 'create', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1972', 'RBObjects', 'Transactions', 'getChequeBookRequests', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1976', 'RBObjects', 'Transactions', 'createStopChequePayments', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1978', 'RBObjects', 'Transactions', 'createChequeBookRequests', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1977', 'RBObjects', 'Transactions', 'getStopChequePayments', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1980', 'RBObjects', 'Transactions', 'getChequeTypes', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1981', 'RBObjects', 'Transactions', 'getChequeSupplements', 'ALLOW');
                                                                                                    
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('ec69de3f-7ad7-41e6-8d9f-8d0a389c1982', 'RBObjects', 'Transactions', 'getBlockedFunds', 'ALLOW');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('6ef77f18-80bf-4319-cdtc-6d715649113fe', 'RBObjects', 'Security', 'VerifyCaptcha', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('7f88f98-80bf-4219-ddte-9d715649223fg', 'RBObjects', 'Security', 'getRiskScore', 'ALLOW');


INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('80400bb0-0c62-11eb-adc1-0242ac120002', 'MessageBinary', 'media', 'create', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b18d1244-0c62-11eb-adc1-0242ac120002', 'MessageBinary', 'media', 'createBinary', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b641794c-0c62-11eb-adc1-0242ac120002', 'MessageBinary', 'media', 'getBinary', 'ALLOW');

UPDATE appmappingaid SET Appid = 'ONBOARDING', aid = 'Onboarding' WHERE (id = '7');

INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b8ca2c32-0c89-11eb-adc1-0242ac120002', 'RBObjects', 'Accounts', 'newAccountShortTermDeposit', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b8ca2eb2-0c89-11eb-adc1-0242ac120002', 'RBObjects', 'Accounts', 'newLoanAccount', 'ALLOW');
INSERT INTO `service_permission_mapper` (`id`, `service_name`, `object_name`, `operation`, `permissions`) VALUES ('b8ca2fb6-0c89-11eb-adc1-0242ac120002', 'RBObjects', 'Accounts', 'newCashIncentiveAccount', 'ALLOW');



UPDATE appmappingaid SET aid = 'OnlineBanking' WHERE (id = '1');

UPDATE `billermaster` SET `address`='1500 Boltonfield St, Columbus, OH 43228' WHERE `id`='1';
UPDATE `billermaster` SET `address`='1801 66th Ave, Suite 103A, Plantation, FL 33313' WHERE `id`='2';
UPDATE `billermaster` SET `address`='BOA, P.O. Box 15019, Wilmington, DE 19850-5019' WHERE `id`='3';
UPDATE `billermaster` SET `address`='ABC Energy, 200 Post Rd, White Plains, NY, 10601' WHERE `id`='6';

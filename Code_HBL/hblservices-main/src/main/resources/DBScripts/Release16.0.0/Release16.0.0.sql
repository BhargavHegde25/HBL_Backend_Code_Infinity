/* Enrolment Activation email templates   */
INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('224', 'BCT_ENROLLMENT_ACTIVATIONCODE_TEMPLATE', 'Dear %firstName%, You are enrolled to HBL digital banking. To activate profile, use the activation code %otp%. Your username and the activation link have been sent to your registered email.', 'Account Activation', 'Temenos Digital', 'dbx_cl@infinity.com');
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<p>&nbsp;</p><center><img src=\"https://ib1.himalayanbank.com/images/brand-login.png\" alt=\"hbl_logo\"  style=\"width:550px; display: block; margin-left: auto; margin-right: auto;\"/><br /><br /> <br /><table align=\"center\" style=\"width: 600px;\" ><tr><td style=\"border-style: solid; border-width: 6px; border-color: black;\"><br /><p  style=\"margin-left: 10px;\"> USER ACTIVATION NOTIFICATION <br /><br />Dear %firstName%,<br /><br />You are enrolled to Digital Banking Channel. Please activate your account now.<br /><br />%userName% is your username. Activation code is %otp% sent to your registered mobile number.<br /><br />You are required to input your username &amp; activation code in the link below.<br /><br /><center>To activate your account and set a password,<a style=\"color: #11abeb;\" href=\"%resetPasswordLink%\">click here</a> <br />or paste the following link on your browser: <br /><br /><a style=\"color: #11abeb;\">%resetPasswordLink%</a><br/><br/>The activation code will expire in %activationCodeExpiry% days , so activate it right away.</center></p><br /><br /><br /></td></tr><tr><td><br /><b>Auto-generated Email Notification. Please do not reply to this email address.</b> <br /><br />The information in this mail is confidential and is intended solely for the addressee. Access to this mail by anyone else is unauthorized. Copying or further distribution beyond the original recipient may be unlawful. <br /><br /></td></tr></table></center>' WHERE (`id` = '213');

/* BillPayments and history and favorite merchant */

ALTER TABLE `dbxdb`.`payee`
CHANGE COLUMN `billermaster_id` `billermaster_id` VARCHAR(250) NULL DEFAULT '1' ;

ALTER TABLE `dbxdb`.`payee`
CHANGE COLUMN `bankAddressLine1` `bankAddressLine1` VARCHAR(250) NULL DEFAULT NULL ,
CHANGE COLUMN `bankAddressLine2` `bankAddressLine2` VARCHAR(250) NULL DEFAULT NULL ;

/* Store external service payload and response for billpayment transfers */
ALTER TABLE `dbxdb`.`billpaytransfers`
ADD COLUMN `externalServicePayload` VARCHAR(10000) NULL AFTER `legalEntityId`;
ALTER TABLE `dbxdb`.`billpaytransfers`
ADD COLUMN `externalServiceResponse` VARCHAR(5000) NULL AFTER `externalServicePayload`;

/* Added new column for paymentId in billpaytransfers and intrabanktransfers tables*/
ALTER TABLE `dbxdb`.`intrabanktransfers`
ADD COLUMN `paymentId` VARCHAR(250) NULL AFTER `legalEntityId`;

ALTER TABLE `dbxdb`.`billpaytransfers`
ADD COLUMN `paymentId` VARCHAR(250) NULL AFTER `externalServiceResponse`;

ALTER TABLE `dbxdb`.`intrabanktransfers`
ADD COLUMN `paymentSystemId` VARCHAR(250) NULL AFTER `paymentId`;




------------ Table created for card limit in spotlight functionality---------

CREATE TABLE `cardConfigLimit` (
  `id` varchar(50) NOT NULL,
  `cardType` varchar(50) DEFAULT NULL,
  `cardCategory` varchar(50) DEFAULT NULL,
  `cardDescription` varchar(1000) DEFAULT NULL,
  `cardLimits` varchar(1000) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)

) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3

------------- Fixed Deposit MFA ---------

UPDATE `dbxdb`.`mfa` SET `Action_id` = 'OPEN_FIXED_DEPOSIT_ACTIVATE' WHERE (`id` = '1692872787') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'FIXED_DEPOSIT');


 
 


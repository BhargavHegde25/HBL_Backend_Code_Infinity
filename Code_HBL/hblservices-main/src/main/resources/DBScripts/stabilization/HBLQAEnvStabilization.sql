/*

AdminconsoleservicesExt jar to be there in new environment.
This jar to be link to business banking Integration service of KonybankingAdminconsole application and publish.
This is resolved features update failure issue at service definition and customrole level.

*/

/*
Can't Sign In flow Fix after Migrated Authentication Fabric Application
------------------------------------------------------------------------
1. Unlink Login Object from target environment
2. Goto Api Management and Click on Object Services And Search Login Object and Click On Delete All Versions
3. Export Login Object Service from Working environment  
4. Goto API Managment and Object Services and Click on Import Service and Drag the Login Object Service Which is Exported in previoud step.
5. Click On Import
6. Goto Authentication App and Click on Object Service and Click on Use Exisisting And Search with Login select  Login check box and click on Add button and click Close button
7. Publish the Authentication App

*/

/*HBL VIEW ONLY Role Scripts */
insert into  `dbxdb`.`membergroup` ( `id`, `Name`, `Description`, `Type_id`, `Status_id`, `isEAgreementActive`, `createdby`, `isApplicabletoAllServices`, `companyLegalUnit` ) values ('HBL_VIEW_ONLY', 'HBL View Only', 'this is HBL View Only role', 'TYPE_ID_RETAIL', 'SID_ACTIVE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', false, 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('eb199d8f-3bca-49e8-a2ae-dde1a8e86e44', 'HBL_VIEW_ONLY', 'ACCOUNT_SETTINGS_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('5ef22f6c-3d14-4e03-8779-1aa02607bf44', 'HBL_VIEW_ONLY', 'ACCOUNT_SETTINGS_EDIT', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('db95043f-4a4d-4ccf-bab3-d896395d9f04', 'HBL_VIEW_ONLY', 'ALERT_MANAGEMENT', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('98c78123-d98a-4d86-b6c9-707ed8572bb3', 'HBL_VIEW_ONLY', 'MESSAGES_DELETE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('5227f25e-9805-4a57-b3b4-4eb155c9e266', 'HBL_VIEW_ONLY', 'MESSAGES_CREATE_OR_REPLY', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('93b0413f-088d-4ba4-995e-0d8ba99dfa97', 'HBL_VIEW_ONLY', 'MESSAGES_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('9fda986f-258d-4c1d-b8a4-a0ef171d2ebb', 'HBL_VIEW_ONLY', 'DISPUTE_TRANSACTIONS', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('55776830-a830-45ce-8332-45a0bd68734c', 'HBL_VIEW_ONLY', 'DISPUTE_TRANSACTIONS_MANAGE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('8ac0acaa-cd08-449c-89a2-f11292a53687', 'HBL_VIEW_ONLY', 'DISPUTE_TRANSACTIONS_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('b507abe9-9238-46c0-9e63-d8968458f1d7', 'HBL_VIEW_ONLY', 'FX_RATES_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('82e3e6fa-85f0-4f58-b9df-0a36e96a5453', 'HBL_VIEW_ONLY', 'FX_RATES_VIEW_CALCULATOR', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('2f1926e6-ca74-4d44-9965-410ca5d2ce91', 'HBL_VIEW_ONLY', 'FEEDBACK_SUBMIT', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('f456c1cc-16ae-4ec0-95fd-185403324d09', 'HBL_VIEW_ONLY', 'NOTIFICATION_UPDATE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('eeb7944c-bd2c-40c0-98e2-650ff30dc2a5', 'HBL_VIEW_ONLY', 'NOTIFICATION_DELETE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('4c66a7e2-fa7c-41c1-a840-5bc7d79c275a', 'HBL_VIEW_ONLY', 'NOTIFICATION_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('336b716f-934f-47ef-adeb-ae46461ce193', 'HBL_VIEW_ONLY', 'ONLINE_BANKING_ACCESS_DISABLE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('79cbd9ec-6a72-4b02-819c-354d612ebef0', 'HBL_VIEW_ONLY', 'PASSWORD_UPDATE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('a2dbb80e-835b-490b-93d7-185cfbc52ee1', 'HBL_VIEW_ONLY', 'PROFILE_SETTINGS_VIEW', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupactionlimit` ( `id`, `Group_id`, `Action_id`, `isNewAction`, `createdby`, `companyLegalUnit` ) values ('c6a0b11a-9889-4dc5-8601-2c27b9c75154', 'HBL_VIEW_ONLY', 'PROFILE_SETTINGS_UPDATE', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );
insert into  `dbxdb`.`groupservicedefinition` ( `Group_id`, `serviceDefinitionId`, `isDefaultGroup`, `createdby`, `companyLegalUnit` ) values ('HBL_VIEW_ONLY', '5801fa32-a416-45b6-af01-b22e2de93777', false, '9f883caa-6cc2-4863-b0af-e88eb5ac3370', 'NP0010001' );


/**** check below script ****/

/***
SELECT @@GLOBAL.sql_mode;

Make sure ONLY_FULL_GROUP_BY mode should not be exists in the modes list.
 Else customer created messages get service will fail in the messages modules.
 
 ***/

/***** New card dispute Email template ***/
 
  INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('230', 'disputeCardEmailToCustomer', '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Hi %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">We have received your dispute transaction request and is currently under review. Please find the details below</span> </strong> </p> </td> <td><b>Dispute ID:</b> %id%</td> </br> </td> <td><b>Dispute Reason:</b> %reason%</td> </br> <td><b>Requested Date:</b> %transactionDate%</td> </br> <td><b>Transaction Details:</b></td> </br> <ul> <li> <td><b>Transaction Description:</b> %notes%</td> </li> <li> <td><b>Status:</b> Disputed</td> </li> <li> <td><b>Transaction Date:</b> %date%</td> </li> <li> <td><b>Amount:</b> %amount%</td> </li> <li> <td><b>Transaction Type:</b> %transactionType%</td> </li> <li> <td><b>Reference Number:</b> %referenceNumber%</td> </li> <li> <td><b>Card Number:</b> %fromAccountr%</td> </li> <li> <td><b>To (Beneficiary / Payee / Recipient):</b> %toAccount%</td> </li> <li> <td><b>Dispute Description:</b> %notes%</td> </li> </ul> </br> <l>Best Regards,</l> </br> <l>Himalayan Bank Limited,</l> </br> </tr> </tbody>', 'Dispute Transaction Request', 'himalayanbank', 'ibanking@himalayanbank.com');
  
  /* To fix the issue - Entity value is missing in getSystemConfigurations service response.
 Executed below query - */

UPDATE `dbxdb`.`configurations` SET `config_value` = '{\"Entity\":[{\"GB0010001\":\"Enabled\",\"NL0020001\":\"Not Required\",\"NP0010001\":\"Enabled\"}],\"PaymentType\":{\"Domestic Transfer\":{\"PayeeVerification\":\"Optional\", \"CountryCodes\":[{\"GB\":\"Mandatory\",\"DE\":\"Optional\"}]},\"Within Same Bank\":{\"PayeeVerification\":\"Not Required\", \"CountryCodes\":[]},\"International Transfer\":{\"PayeeVerification\":\"Not Required\",\"CountryCodes\":[]}}}' WHERE (`configuration_id` = '2bc30144-0f70-4cdf-8015-2ff01c73c264');



/******** Notes ***********/
//Make sure all the email templates to be update with proper navigation application OLB URL


updated email template subjects:
-------------------------------

UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Confirmation Of New Fixed Deposit Request.' WHERE (`id` = '225');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Acknowledgment Of New Himal Remit Fixed Deposit Request.' WHERE (`id` = '226');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Transaction PIN Reset Request Rejected.' WHERE (`id` = '228');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Transaction PIN Reset Request Approved.' WHERE (`id` = '229');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Transaction PIN Reset Request Received.' WHERE (`id` = '227');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Dispute Transaction Request Received.' WHERE (`id` = '219');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Acknowledgment of Dispute Transaction Request.' WHERE (`id` = '218');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Acknowledgment of Dispute Transaction Request.' WHERE (`id` = '230');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'New Card Request Received.' WHERE (`id` = '221');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Acknowledgement Of  New Card Request.' WHERE (`id` = '222');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Acknowledgement Of  New Card Request.' WHERE (`id` = '220');


-----------------dispute email template changes--------

UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">A new dispute request has been raised by %customerName% with details as follows:</span> </strong> </p> </td> <td><b>Dispute ID:</b> %id%</td> </br> </td> <td><b>Dispute Reason:</b> %reason%</td> </br> <td><b>Requested Date:</b> %requestedDate%</td> </br> <td><b>Transaction Details:</b></td> </br> <ul> <li> <td><b>Date:</b> %date%</td> </li> <li> <td><b>Amount:</b> %amount%</td> </li> <li> <td><b>Reference Number:</b> %referenceNumber%</td> </li> <li> <td><b>Transaction Type:</b> %transactionType%</td> </li> <li> <td><b>From Account:</b> %fromAccountr%</td> </li> <li> <td><b>To Account:</b> %toAccount%</td> </li> <li> <td><b>Dispute Description:</b> %disputeDescription%</td> </li> </ul>' WHERE (`id` = '219');
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Hi %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">We have received your dispute transaction request and is currently under review. Please find the details below</span> </strong> </p> </td> <td><b>Dispute ID:</b> %id%</td> </br> </td> <td><b>Dispute Reason:</b> %reason%</td> </br> <td><b>Requested Date:</b> %requestedDate%</td> </br> <td><b>Transaction Details:</b></td> </br> <ul> <li> <td><b>Date:</b> %date%</td> </li> <li> <td><b>Amount:</b> %amount%</td> </li> <li> <td><b>Reference Number:</b> %referenceNumber%</td> </li> <li> <td><b>Transaction Type:</b> %transactionType%</td> </li> <li> <td><b>From Account:</b> %fromAccountr%</td> </li> <li> <td><b>To Account:</b> %toAccount%</td> </li> <li> <td><b>Dispute Description:</b> %disputeDescription%</td> </li> </ul> </br> <l>Best Regards,</l></br> <l>Himalayan Bank Limited,</l></br> </tr> </tbody>' WHERE (`id` = '218');



-----------new card request email templates for prepaid cards-----------------

INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('231', 'requestPrepaidCardEmailToCustomer', '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Dear %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Thank you for reaching out to HBL. We have received your request for a new %cardType%. Please find the details below:</span> </strong> </p> </td> <ul> <li> <td><b>Application Reference No::</b> %referenceNumber%</td> </li> <li> <td><b>Requested Card Type:</b> %cardType%</td> </li> <li> <td><b>Requested Card Name:</b> %cardName%</td> </li> <li> <td><b>Expected Name on the Card:</b> %nameOnTheCard%</td> </li> <li> <td><b>Date of Request:</b> %requestdate%</td> </li> </ul> </br> <l>Our team is currently processing your request, and we will notify you once your card has been issued and shipped. Please allow %estimatedTime%  for delivery.</l> </br> </br> <l>If you have any questions or need further assistance in the meantime, feel free to contact us at %customerCareNumber%</l> </br> </br> <l>Best Regards,</l> </br> <l>Himalayan Bank Limited,</l> </br> </tr> </tbody>', 'Acknowledgement Of  New Card Request.', 'himalayanbank', 'ibanking@himalayanbank.com');

INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('232', 'requestPrepaidCardEmailToBank', '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear Card Processing Team,</l> </br> </br> <l>This is to notify you of a new %cardType%  request for the following customer:</l> </br> <ul> <li> <td><b>Customer Name:</b> %coreIdentifier%</td> </li> <li> <td><b>Application Reference No:</b> %referenceNumber%</td> </li> <li> <td><b>Requested Card Type:</b> %cardType%</td> </li> <li> <td><b>Requested Card Name:</b> %cardName%</td> </li> <li> <td><b>Expected Name on the Card:</b> %nameOnTheCard%</td> </li> <li> <td><b>Date of Request:</b> %requestdate%</td> </li> </ul> </br> <l>Please proceed with processing the %cardType% request and ensure that all necessary steps are followed.</l> </br> </br> <l>Best Regards,</l> </br> <l>%cardTeamName%</l> </br> </tr> </tbody>', 'New Card Request Received.', 'himalayanbank', 'ibanking@himalayanbank.com');


----------- unlock email template updated ------------

UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '\' <center> <div style=\"width: 95%; height: 10px; margin: 50px 0px 0px 0px;\"> <div> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]> <center> <tr><td><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"95%\"> <![endif]--> <tbody> <tr> <td style=\"background-color: #284e77; font-size: 1px; line-height: 1px; -webkit-text-size-adjust: none;\" align=\"center\" valign=\"top\" bgcolor=\"#284e77\" height=\"10\">&nbsp;</td> </tr> </tbody> </table> <!-- [if mso]></div> </td> </tr> </center> </table> <![endif]--> </div> </div> <div style=\"width: 95%; margin: 0px 0px 50px 0px;\"> <div style=\"display: inline-block; text-align: left;\"> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]> <center><tr> <td> <div> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"95%\"><![endif]--> <tbody> <tr> <td style=\"background-color: #ffffff; -webkit-text-size-adjust: none;\"> <div style=\"margin: 50px 20px 50px 20px; color: #333b44; font-size: 13px;\"> <!-- [if mso]><font face=\"Lato\"> <![endif]--> <table style=\"width: 50%;\" width=\"50%\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> <!-- <td style=\"font-size:28px;\">[if mso]> <table height=\"200\"><tr>&nbsp;</tr> <tr> <td style=\"font-size:28px;\"> &nbsp;BANK</td> </tr> </table><div style=\"display:none\"><![endif]&nbsp;BANK [if mso]> </div><![endif] </td> --> </tr> </tbody> </table> <br /><br /> <!-- [if mso]> <div style=\"margin: -50px 0px 0px 0px;\"> <![endif]--> Hi %firstName%, <br /><br />We have received a request to unlock your account. Use this link to Reset your Password to unlock your account.&nbsp;&nbsp;<br /><br /> <!-- [if mso]><div style=\"margin: -50px 0px 0px 0px;\"> <![endif]--> <div style=\"text-align: center; border-width: 1px; border-radius: 4px;\"> <table style=\"table-layout: fixed;\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]><center> <tr><td width=\"95%\" style=\"color: #ffffff; background-color: #333b44; word-wrap:break-word; word-break:break-all;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout:fixed;\"><![endif]--> <tbody> <tr> <td style=\"color: #ffffff; background-color: #851A1C; word-wrap: break-word; word-break: break-all; padding: 10px; line-height: 20px;\" align=\"center\"> <div style=\"margin: 10px 10px 10px 10px; font-size: 13px;\">To unlock your account, <a style=\"color: #11abeb;\" href=\"%unlockAccountLink%\">click here</a> <br /> or paste the following link on your browser: <br /><br /> %unlockAccountLink%</div> </td> </tr> </tbody> </table> <!-- [if mso]> </td> <td width=\"10\">&nbsp;&nbsp;&nbsp;</td> </tr> </center></table> <![endif]--> </div> <!-- [if mso]></div> <![endif]--> <br /> <div align=\"center\">The link will expire in %linkExpiry% hours, so please use it right away.</div> <br /> Regards, <br /> Himalayan Bank team <!-- <br><br><br> <span>PS: </span><span style=\"color: #999999;\">We appreciate you might not want these emails, if that\"s the case <a href=\"\" style=\"color: #11abeb;\">click here</a> and you won\"t receive them.</span> --> <br /><br /><br /><br /> <!-- [if mso]> </div><![endif]--> <!-- [if mso]> </font> <![endif]--> </div> </td> </tr> </tbody> </table> <!-- [if mso]></div></td> </tr> </center></table> <![endif]--> </div> </div> </center> \'', `Subject` = 'Unlock HBL Account', `SenderName` = 'himalayanbank', `SenderEmail` = 'ibanking@himalayanbank.com' WHERE (`id` = '13');

UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '\' <center> <div style=\"width: 95%; height: 10px; margin: 50px 0px 0px 0px;\"> <div> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]> <center> <tr><td><div><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"95%\"> <![endif]--> <tbody> <tr> <td style=\"background-color: #284e77; font-size: 1px; line-height: 1px; -webkit-text-size-adjust: none;\" align=\"center\" valign=\"top\" bgcolor=\"#284e77\" height=\"10\">&nbsp;</td> </tr> </tbody> </table> <!-- [if mso]></div> </td> </tr> </center> </table> <![endif]--> </div> </div> <div style=\"width: 95%; margin: 0px 0px 50px 0px;\"> <div style=\"display: inline-block; text-align: left;\"> <table border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]> <center><tr> <td> <div> <table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"95%\"><![endif]--> <tbody> <tr> <td style=\"background-color: #ffffff; -webkit-text-size-adjust: none;\"> <div style=\"margin: 50px 20px 50px 20px; color: #333b44; font-size: 13px;\"> <!-- [if mso]><font face=\"Lato\"> <![endif]--> <table style=\"width: 50%;\" width=\"50%\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> <!-- <td style=\"font-size:28px;\">[if mso]> <table height=\"200\"><tr>&nbsp;</tr> <tr> <td style=\"font-size:28px;\"> &nbsp;BANK</td> </tr> </table><div style=\"display:none\"><![endif]&nbsp;BANK [if mso]> </div><![endif] </td> --> </tr> </tbody> </table> <br /><br /> <!-- [if mso]> <div style=\"margin: -50px 0px 0px 0px;\"> <![endif]--> Hi %firstName%, <br /><br />We have received a request to unlock your account. Use this link to Reset your Password to unlock your account.&nbsp;&nbsp;<br /><br /> <!-- [if mso]><div style=\"margin: -50px 0px 0px 0px;\"> <![endif]--> <div style=\"text-align: center; border-width: 1px; border-radius: 4px;\"> <table style=\"table-layout: fixed;\" border=\"0\" width=\"100%\" cellspacing=\"0\" cellpadding=\"0\"> <!-- [if mso]><center> <tr><td width=\"95%\" style=\"color: #ffffff; background-color: #333b44; word-wrap:break-word; word-break:break-all;\"><table border=\"0\" cellpadding=\"0\" cellspacing=\"0\" width=\"100%\" style=\"table-layout:fixed;\"><![endif]--> <tbody> <tr> <td style=\"color: #ffffff; background-color: #851A1C; word-wrap: break-word; word-break: break-all; padding: 10px; line-height: 20px;\" align=\"center\"> <div style=\"margin: 10px 10px 10px 10px; font-size: 13px;\">To unlock your account, <a style=\"color: #11abeb;\" href=\"%unlockAccountLink%\">click here</a> <br /> or paste the following link on your browser: <br /><br /> %unlockAccountLink%</div> </td> </tr> </tbody> </table> <!-- [if mso]> </td> <td width=\"10\">&nbsp;&nbsp;&nbsp;</td> </tr> </center></table> <![endif]--> </div> <!-- [if mso]></div> <![endif]--> <br /> <br /> Regards, <br /> Himalayan Bank team <!-- <br><br><br> <span>PS: </span><span style=\"color: #999999;\">We appreciate you might not want these emails, if that\"s the case <a href=\"\" style=\"color: #11abeb;\">click here</a> and you won\"t receive them.</span> --> <br /><br /><br /><br /> <!-- [if mso]> </div><![endif]--> <!-- [if mso]> </font> <![endif]--> </div> </td> </tr> </tbody> </table> <!-- [if mso]></div></td> </tr> </center></table> <![endif]--> </div> </div> </center> \'' WHERE (`id` = '13');



-------------- Below scripts are required to disable Security Questions MFA from spotlight-----------------

Make sure if any entries available with Security question from below table, then please delete or update. Below are sample scripts for DEV environment

DELETE FROM `dbxdb`.`mfaconfigurations` WHERE (`MFA_id` = 'SECURITY_QUESTIONS') and (`MFAKey_id` = 'LOGOUT_USER');
DELETE FROM `dbxdb`.`mfaconfigurations` WHERE (`MFA_id` = 'SECURITY_QUESTIONS') and (`MFAKey_id` = 'LOCK_USER');
DELETE FROM `dbxdb`.`mfaconfigurations` WHERE (`MFA_id` = 'SECURITY_QUESTIONS') and (`MFAKey_id` = 'MAX_FAILED_ATTEMPTS_ALLOWED');
DELETE FROM `dbxdb`.`mfaconfigurations` WHERE (`MFA_id` = 'SECURITY_QUESTIONS') and (`MFAKey_id` = 'SQ_NUMBER_OF_QUESTION_ASKED');


UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1043829291') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTRA_BANK_FUND_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1069008617') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'USERNAME_UPDATE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1080823475') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'RESUME_AUTHENTICATION');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1080823476') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'PROSPECT_EXPIRY');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1261437124') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_REPLACE_CARD');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1339866089') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTERNATIONAL_WIRE_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1458000737') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'FUNDING_AUTHENTICATION');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1505112016') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PAY_MULTIPLE_BENEFICIARIES_CREATE_TRANSFER');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1702614193') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'USER_VERIFICATION');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1815885335') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'LOGIN');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1745986965') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PAY_OFF_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1745986964') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PAY_OTHER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1745986963') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PAY_DUE_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1710928803') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1518695287') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_CANCEL_CARD');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396786') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PSD2_TPP_CONSENT_REVOKE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396784') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_APPROVE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396783') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_EDIT');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396782') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_SUBMIT');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396778') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'P2P_CREATE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1815812345') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PROFILE_SETTINGS_UPDATE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1812345678') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'ONLINE_BANKING_ACCESS_DISABLE');


UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1080823475') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'RESUME_AUTHENTICATION');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1080823476') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'PROSPECT_EXPIRY');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1233611240') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_ACTIVATE_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1261437124') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_REPLACE_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1263952855') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'DOMESTIC_WIRE_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1283611941') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_APPLY_FOR_DEBIT_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1318584743') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_UNLOCK_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1333023862') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1339866089') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTERNATIONAL_WIRE_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1452783723') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BILL_PAY_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1458000737') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'FUNDING_AUTHENTICATION');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1505112016') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PAY_MULTIPLE_BENEFICIARIES_CREATE_TRANSFER');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1588624560') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_LOCK_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1692872967') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PASSWORD_UPDATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1702614193') and (`App_id` = 'ORIGINATION') and (`Action_id` = 'USER_VERIFICATION');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1745986962') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'TRANSFER_BETWEEN_OWN_ACCOUNT_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1812345678') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'ONLINE_BANKING_ACCESS_DISABLE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1815812345') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PROFILE_SETTINGS_UPDATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396778') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'P2P_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396779') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'ACH_PAYMENT_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396780') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'ACH_COLLECTION_CREATE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396781') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'ACH_FILE_UPLOAD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396782') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_SUBMIT');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396782') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'WITHDRAW_CASH_CARDLESS_CASH');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396783') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_EDIT');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396784') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'BULK_PAYMENT_REQUEST_APPROVE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN', `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1868396786') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'PSD2_TPP_CONSENT_REVOKE');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396787') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_APPLY_FOR_VIRTUAL_DOLLAR_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396788') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_APPLY_FOR_PREPAID_CARD');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396789') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_EMI_REQUEST');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396790') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_PREPAID_DOMESTIC_TOPUP');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396791') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_VIRTUAL_DOLLAR_CARD_TOPUP');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396792') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_CARD_PAYMENT');
UPDATE `dbxdb`.`mfa` SET `PrimaryMFAType` = 'TRANSACTION_PIN' WHERE (`id` = '1868396793') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'CARD_MANAGEMENT_REPORT_CARD_STOLEN');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1043829291') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'INTRA_BANK_FUND_TRANSFER_CREATE');
UPDATE `dbxdb`.`mfa` SET `SecondaryMFAType` = 'SECURE_ACCESS_CODE' WHERE (`id` = '1815885335') and (`App_id` = 'RETAIL_AND_BUSINESS_BANKING') and (`Action_id` = 'LOGIN');



DELETE FROM `dbxdb`.`mfatype` WHERE (`id` = 'SECURITY_QUESTIONS');


------------------ update dispute email template ---------------
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">Hi %customerName% ,</span> </strong> </p> </td> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">We have received your dispute transaction request and is currently under review. Please find the details below</span> </strong> </p> </td> <td><b>Dispute ID:</b> %id%</td> </br> </td> <td><b>Dispute Reason:</b> %reason%</td> </br> <td><b>Requested Date:</b> %requestedDate%</td> </br> <td><b>Transaction Details:</b></td> </br> <ul> <li> <td><b>Transaction Date:</b> %date%</td> </li> <li> <td><b>Amount:</b> %amount%</td> </li> <li> <td><b>Reference Number:</b> %referenceNumber%</td> </li> <li> <td><b>Transaction Type:</b> %transactionType%</td> </li> <li> <td><b>From Account:</b> %fromAccountr%</td> </li> <li> <td><b>To Account:</b> %toAccount%</td> </li> <li> <td><b>Dispute Description:</b> %disputeDescription%</td> </li> </ul> </br> <l>Best Regards,</l></br> <l>Himalayan Bank Limited,</l></br> </tr> </tbody>' WHERE (`id` = '218');
UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">A new dispute request has been raised by %customerName% with details as follows:</span> </strong> </p> </td> <td><b>Dispute ID:</b> %id%</td> </br> </td> <td><b>Dispute Reason:</b> %reason%</td> </br> <td><b>Requested Date:</b> %requestedDate%</td> </br> <td><b>Transaction Details:</b></td> </br> <ul> <li> <td><b>Transaction Date:</b> %date%</td> </li> <li> <td><b>Amount:</b> %amount%</td> </li> <li> <td><b>Reference Number:</b> %referenceNumber%</td> </li> <li> <td><b>Transaction Type:</b> %transactionType%</td> </li> <li> <td><b>From Account:</b> %fromAccountr%</td> </li> <li> <td><b>To Account:</b> %toAccount%</td> </li> <li> <td><b>Dispute Description:</b> %disputeDescription%</td> </li> </ul>' WHERE (`id` = '219');

----------paper statement next request validation---- do the refreshmetada for the contractaccounts post below script execution----

ALTER TABLE `dbxdb`.`contractaccounts` 
CHANGE COLUMN `requestdate` `requestdate` VARCHAR(50) NULL DEFAULT NULL ;


----------- Fixed deposit creation DB entries -------------

CREATE TABLE `normalfixeddeposits` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `core_identifier` varchar(50) NOT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `productId` varchar(50) DEFAULT NULL,
  `intrestRate` varchar(50) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  `tenure` varchar(50) DEFAULT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `arrangementId` varchar(50) DEFAULT NULL,
  `transactionStatus` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `externalServicePayload` varchar(10000) DEFAULT NULL,
  `externalServiceResponse` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_nfd_idx` (`Customer_id`),
  CONSTRAINT `FK_Custr_nfd_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3


CREATE TABLE `himalfixeddeposits` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `core_identifier` varchar(50) NOT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `productId` varchar(50) DEFAULT NULL,
  `intrestRate` varchar(50) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  `tenure` varchar(50) DEFAULT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `arrangementId` varchar(50) DEFAULT NULL,
  `transactionStatus` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `externalServicePayload` varchar(10000) DEFAULT NULL,
  `externalServiceResponse` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_hfd_idx` (`Customer_id`),
  CONSTRAINT `FK_Custr_hfd_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3

CREATE TABLE `structuredfixeddeposits` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `core_identifier` varchar(50) NOT NULL,
  `currency` varchar(50) DEFAULT NULL,
  `productId` varchar(50) DEFAULT NULL,
  `intrestRate` varchar(50) DEFAULT NULL,
  `fromAccount` varchar(50) DEFAULT NULL,
  `amount` varchar(50) DEFAULT NULL,
  `tenure` varchar(50) DEFAULT NULL,
  `referenceId` varchar(50) DEFAULT NULL,
  `arrangementId` varchar(50) DEFAULT NULL,
  `transactionStatus` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  `externalServicePayload` varchar(10000) DEFAULT NULL,
  `externalServiceResponse` varchar(5000) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_sfd_idx` (`Customer_id`),
  CONSTRAINT `FK_Custr_sfd_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3

------- cross border api audit log table ---------------


CREATE TABLE `crossborderapilog` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `username` varchar(50) NOT NULL,
  `core_identifier` varchar(50) NOT NULL,
  `vpa` varchar(50) DEFAULT NULL,
  `requestBody` varchar(10000) DEFAULT NULL,
  `messageSignature` varchar(500) DEFAULT NULL,
  `responseBody` varchar(10000) DEFAULT NULL,
   `status` varchar(50) DEFAULT NULL,
  `serviceType` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `synctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_crs_idx` (`Customer_id`),
  CONSTRAINT `FK_Custr_crs_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3



--------cardLimit changes with shortDescription-------
ALTER table dbxdb.cardConfigLimit 
ADD COLUMN shortDescription varchar(50) NULL AFTER cardDescription;

--Please do refresh metadata post above query----------

------Email templates clean up by removing dbx references ---------
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'HBL Onboarding process' WHERE (`id` = '8');
UPDATE `dbxdb`.`emailtemplates` SET `Subject` = 'Reset HBL Password' WHERE (`id` = '1');


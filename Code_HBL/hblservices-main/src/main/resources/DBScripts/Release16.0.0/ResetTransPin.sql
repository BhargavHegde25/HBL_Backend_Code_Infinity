use dbxdb;
CREATE TABLE `transactionpinResetReq` (
  `id` varchar(50) NOT NULL,
  `Customer_id` varchar(50) NOT NULL,
  `coreIdentifier` varchar(50) NOT NULL,
  `customerName` varchar(50) NOT NULL,
  `channel` varchar(20) DEFAULT NULL,
  `browser` varchar(20) DEFAULT NULL,
  `os` varchar(20) DEFAULT NULL,
  `status` varchar(20) DEFAULT NULL,
  `requestDate` varchar(50) DEFAULT NULL,
  `createdby` varchar(50) DEFAULT NULL,
  `modifiedby` varchar(50) DEFAULT NULL,
  `createdts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `lastmodifiedts` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `lastsynctimestamp` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `softdeleteflag` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `FK_Cust_id_idx` (`Customer_id`),
  CONSTRAINT `FK_cstmr_id` FOREIGN KEY (`Customer_id`) REFERENCES `customer` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3


INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('227', 'emailTempleteResetPINReq', '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear Admin,</l> </br> </br> <l>A new Transaction PIN reset request has been raised for CIF %customerid% with the following details:</l> </br> <ul> <li> <td><b>Request ID:</b> %requestId%</td> </li> <li> <td><b>Customer Name:</b> %customername%</td> </li> <li> <td><b>Channel:</b> %channel%</td> </li> <li> <td><b>Browser/Device Details:</b> %browser%</td> </li> <li> <td><b>Browser/OS Version:</b> %osversion%</td> </li> <li> <td><b>Request Date & Time:</b> %reqDate%</td> </li> </ul> </br> <l>Kindly proceed with the necessary validation and processing of this request.</l> </br></br> <l>Best Regards,</l> </br> <l>Himalayan Bank Limited.</l> </br> </tr> </tbody>', 'Transaction PIN Reset Request', 'himalayanbank', 'ibanking@himalayanbank.com');

INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('228', 'emailTempleteResetPINReqReject', '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear %customername%,</l> </br> </br> <l>We regret to inform you that your request to reset the Transaction PIN, with reference ID %referenceid%, has been rejected by our bank staff due to security concerns. If you have any questions or need further assistance, please feel free to contact our support team at %customecarenumber%. We are here to assist you! </l></br></br> <l>Thank you for your understanding.</l></br> </br> <l> Warm regards,</l></br> <l> Himalayan Bank Limited</l> </tbody>', 'Transaction PIN Reset Request - Rejected', 'himalayanbank', 'ibanking@himalayanbank.com');

INSERT INTO `dbxdb`.`emailtemplates` (`id`, `TemplateName`, `TemplateText`, `Subject`, `SenderName`, `SenderEmail`) VALUES ('229', 'emailTempleteResetPINReqApproved', '<tbody> <tr> <td style="padding:0 0 15pt 0;"> <div align="center"> <table border="0" cellspacing="0" cellpadding="0" style="width:100%;"> <tbody> <tr> <td style="padding:0 0 15pt 0;"> <p align="center" style="font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;"> <img data-imagetype="External" src="https://ib1.himalayanbank.com/Public/client/emailBrand.png" border="0" id="x__x0000_i1025" style="width:390.99pt;height:72.99pt;"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <l>Dear %customername%,</l> </br> </br> <l> We are pleased to inform you that your request to reset your Transaction PIN has been successfully approved.</l></br><l> For your convenience, your temporary Transaction PIN is %temporaryPin%, and it will remain valid for %resetTemporaryValidity% hours.</l></br><l> Please click <a href="https://infinity.himalayanbank.com/apps/OnlineBanking/#/OnlineBanking/?data=resetTransactionPin" target="_blank" rel="noopener noreferrer" data-auth="NotApplicable" data-linkindex="0">here</a> to set up your new PIN.</l></br><l> Kindly ensure that you reset your PIN at your earliest convenience to maintain the security of your account.</l></br><l> If you have any questions or need further assistance, please feel free to contact our support team at %customecarenumber%. </l></br></br><l>We are here to assist you!</l> </br> </br> <l> Warm regards,</l> </br> <l> Himalayan Bank Limited</l> </tbody>', 'Transaction PIN Reset Request - Approved', 'himalayanbank', 'ibanking@himalayanbank.com');


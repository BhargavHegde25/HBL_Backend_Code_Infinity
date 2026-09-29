------------------------------------------------------------
 
USE `dbxdb`;

DROP procedure IF EXISTS `update_customerstatus_default_legalentity`;
 
USE `dbxdb`;

DROP procedure IF EXISTS `dbxdb`.`update_customerstatus_default_legalentity`;

;
 
DELIMITER $$

USE `dbxdb`$$

CREATE PROCEDURE `update_customerstatus_default_legalentity`(

	IN customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci

)

BEGIN

	DECLARE FINISHED INTEGER DEFAULT 0;

    DECLARE statusId VARCHAR(255) DEFAULT "";

    DECLARE statuses CURSOR 

		FOR (select `Status_id` from `customerlegalentity` where Customer_id = customerId);

	DECLARE CONTINUE HANDLER

        FOR NOT FOUND SET FINISHED = 1;

	SET @currentStatus = (select `Status_Id` from `customer` where  `customer`.`id` = customerId); 

    SET @activeStatus = 0;

    SET @defaultLegalEntity = (select `legalEntityId` from `customerlegalentity` where  Customer_id = customerId); 

    OPEN statuses;

    getStatus: LOOP

		FETCH statuses INTO statusId;

        IF FINISHED = 1 THEN

			LEAVE getStatus;

		ELSE

			IF statusId LIKE '%ACTIVE%' AND  @currentStatus LIKE '%ACTIVE%' THEN

				SET @activeStatus = 1;

				SET FINISHED = 1;

			END IF;

			IF statusId LIKE '%ACTIVE%' AND  @currentStatus NOT LIKE '%ACTIVE%' THEN

				SET @activeStatus = 1;

				UPDATE `customer` SET `Status_id` = 'SID_CUS_ACTIVE', `defaultLegalEntity` = @defaultLegalEntity 

                WHERE `customer`.`id` =  customerId;

			END IF;

		END IF;

	END LOOP getStatus;

    CLOSE statuses;

	IF @activeStatus = 0 THEN

		UPDATE `customer` SET `Status_id` = 'SID_CUS_SUSPENDED' , `defaultLegalEntity` = @defaultLegalEntity 

		WHERE `customer`.`id` =  customerId 

		AND @currentStatus NOT LIKE '%SID_CUS_NEW%';

	END IF;

END$$
 
DELIMITER ;

;
 
 -------------------------------------
 
 Backup
 ---------------


USE `dbxdb`;

DROP procedure IF EXISTS `dbxdb`.`update_customerstatus_default_legalentity_bkp`;

;
 
DELIMITER $$

USE `dbxdb`$$

CREATE PROCEDURE `update_customerstatus_default_legalentity_bkp`(

	IN customerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci

)

BEGIN

	DECLARE FINISHED INTEGER DEFAULT 0;

    DECLARE statusId VARCHAR(255) DEFAULT "";

    DECLARE statuses CURSOR 

		FOR (select `Status_id` from `customerlegalentity` where Customer_id = customerId);

	DECLARE CONTINUE HANDLER

        FOR NOT FOUND SET FINISHED = 1;

	SET @currentStatus = (select `Status_Id` from `customer` where  `customer`.`id` = customerId); 

    SET @activeStatus = 0;

    OPEN statuses;

    getStatus: LOOP

		FETCH statuses INTO statusId;

        IF FINISHED = 1 THEN

			LEAVE getStatus;

		ELSE

			IF statusId LIKE '%ACTIVE%' AND  @currentStatus LIKE '%ACTIVE%' THEN

				SET @activeStatus = 1;

				SET FINISHED = 1;

			END IF;

			IF statusId LIKE '%ACTIVE%' AND  @currentStatus NOT LIKE '%ACTIVE%' THEN

				SET @activeStatus = 1;

				UPDATE `customer` SET `Status_id` = 'SID_CUS_ACTIVE' 

				WHERE `customer`.`id` =  customerId;

			END IF;

		END IF;

	END LOOP getStatus;

    CLOSE statuses;

	IF @activeStatus = 0 THEN

		UPDATE `customer` SET `Status_id` = 'SID_CUS_SUSPENDED' 

		WHERE `customer`.`id` =  customerId

		AND @currentStatus NOT LIKE '%SID_CUS_NEW%';

	END IF;

END$$
 
DELIMITER ;

;




---------------------------------------

UPDATE `dbxdb`.`emailtemplates` SET `TemplateText` = '<tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <div align=\"center\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 15pt 0;\"> <p align=\"center\" style=\"font-size:12pt;font-family:Aptos,sans-serif;text-align:center;margin:0;\"> <img data-imagetype=\"External\" src=\"https://ib1.himalayanbank.com/Public/client/emailBrand.png\" border=\"0\" id=\"x__x0000_i1025\" style=\"width:390.99pt;height:72.99pt;\"> </p> </td> </tr> </tbody> </table> </div> </td> </tr> <tr> <td style=\"padding:15pt 11.25pt;border:4.5pt solid black;\"> <p> <strong> <span style=\"font-family:Aptos,sans-serif;\">HBL I-BANKING THIRD PARTY AUTHENTICATION NOTIFICATION</span> </strong> </p> <p>Dear %userName%, Your third party authentication has been reset.</p> <p aria-hidden=\"true\">&nbsp;</p> <p>Go to the I-Banking login page &amp; click on&nbsp; <a href=\"http://20.40.42.137/:8080/apps/Onlinebanking/#/OnlineBanking/frmActivateThirdParty?data=thirdpartyauth\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" data-linkindex=\"0\">Reset Third Party Auth?</a>. You will be redirected to a new page and&nbsp;you have to login with existing credentials to setup OTP. Please scan a QR code via Google Authenticator mobile app. </p> <p>Download the <strong> <span style=\"font-family:Aptos,sans-serif;\">Google Authenticator</span> </strong> app from Google PlayStore/AppleStore before starting the activation process. </p> <ul type=\"disc\" style=\"margin-bottom:0;\"> <li style=\"font-size:12pt;font-family:Aptos,sans-serif;margin:0;\">Google Playstore: <a href=\"https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" data-linkindex=\"1\">https://play.google.com/store/apps/details?id=com.google.android.apps.authenticator2</a> </li> <li style=\"font-size:12pt;font-family:Aptos,sans-serif;margin:0;\">Apple Store: <a href=\"https://apps.apple.com/us/app/google-authenticator/id388497605\" target=\"_blank\" rel=\"noopener noreferrer\" data-auth=\"NotApplicable\" data-linkindex=\"2\">https://apps.apple.com/us/app/google-authenticator/id388497605</a> </li> </ul> </td> </tr> <tr> <td style=\"padding:15pt 0 0 0;\"> <table border=\"0\" cellspacing=\"0\" cellpadding=\"0\" style=\"width:100%;\"> <tbody> <tr> <td style=\"padding:0 0 7.5pt 0;\"> <p style=\"font-size:12pt;font-family:Aptos,sans-serif;margin:0;\"> <b>Auto-generated Email Notification. Please do not reply to this email address.</b> </p> </td> </tr> <tr> <td style=\"padding:0;\"> <p style=\"font-size:12pt;font-family:Aptos,sans-serif;margin:0;\"> <span style=\"font-size:10.5pt;\">The information in this mail is confidential and is intended solely for the addressee. Access to this mail by anyone else is unauthorized. Copying or further distribution beyond the original recipient may be unlawful. Any opinion expressed in this mail is that of the sender and does not necessarily reflect that of EXPRESS BANKING </span> </p> </td> </tr> </tbody> </table> </td> </tr> </tbody>' WHERE (`id` = '212');



 
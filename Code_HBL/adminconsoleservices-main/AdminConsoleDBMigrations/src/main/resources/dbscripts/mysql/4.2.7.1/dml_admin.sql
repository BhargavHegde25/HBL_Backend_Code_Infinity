INSERT INTO `featureaction` (`id`, `Feature_id`, `App_id`, `Type_id`,  `name`, `description`, `isAccountLevel`, `isMFAApplicable`) VALUES ('CARD_MANAGEMENT_ACTIVATE_CARD', 'CARD_MANAGEMENT', 'RETAIL_AND_BUSINESS_BANKING', 'NON_MONETARY', 'Card Management Activate Card', 'Activate a new credit card', '0', '0');

DELIMITER $$
CREATE PROCEDURE insertActivateCardMFAScenario()
BEGIN

	IF(SELECT count(*) from `mfa` where `App_id`='RETAIL_AND_BUSINESS_BANKING' AND `Action_id` = 'CARD_MANAGEMENT_ACTIVATE_CARD' ) = 0  THEN 
        INSERT IGNORE INTO `mfa` (`id`, `App_id`, `Action_id`, `FrequencyType_id`, `Status_id`, `Description`, `PrimaryMFAType`, `SecondaryMFAType`, `SMSText`, `EmailSubject`, `EmailBody`, `createdby`, `modifiedby`, `createdts`, `lastmodifiedts`, `synctimestamp`, `softdeleteflag`) VALUES ('1233611240', 'RETAIL_AND_BUSINESS_BANKING', 'CARD_MANAGEMENT_ACTIVATE_CARD', 'ALWAYS', 'SID_ACTIVE', 'Card Management- Lock Card', 'SECURITY_QUESTIONS', 'SECURE_ACCESS_CODE', '[#]OTP[/#]\n', 'OTP', '<br>[#]OTP[/#]<br><br class=\"\">', 'UID10', 'admin1', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '0');
    END IF; 

	

	DELETE from `mfa` where `App_id` = '' and `Action_id` = '';
END$$
DELIMITER ;
call insertActivateCardMFAScenario();
DROP PROCEDURE IF EXISTS `insertActivateCardMFAScenario`;

UPDATE `featureaction` SET `isMFAApplicable` = '1', `MFA_id` = '1233611240' WHERE (`id` = 'CARD_MANAGEMENT_LOCK_CARD');

UPDATE `featureaction` SET `isMFAApplicable` = '1', `MFA_id` = '1233611240' WHERE (`id` = 'CARD_MANAGEMENT_ACTIVATE_CARD');



USE `dbxdb`;
DROP procedure IF EXISTS `hbl_verify_user_device_login`;

DELIMITER $$
USE `dbxdb`$$

CREATE PROCEDURE `hbl_verify_user_device_login`(
IN _username VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _deviceId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
	SET SESSION group_concat_max_len = 100000000;
	SET @customerId = (SELECT id FROM `dbxdb`.`customer` WHERE `username`=_username);
    
     IF @customerId is NULL THEN
        SET @isExistingDevice = (SELECT COUNT(*) FROM `dbxdb`.`customerdevice` WHERE `Channel_id`='CH_ID_MOB' 
			AND `softdeleteflag`='0' AND `Status_id`!='SID_DEVICE_DEREGISTERED' AND `id`=_deviceId);
       
		IF @isExistingDevice > 0 THEN
			SET @loginStatusCd = 0;
			SET @loginStatus = "Customer not found, Existing device login";
		ELSE
			SET @loginStatusCd = 0;
			SET @loginStatus = "New Customer or Device Login";
		END IF;
        
	ELSE
		SET @isValidCustDevice = (SELECT COUNT(*) FROM `customerdevice` WHERE `Channel_id`='CH_ID_MOB' 
			AND `softdeleteflag`='0' AND `Status_id`!='SID_DEVICE_DEREGISTERED' AND `id`=_deviceId AND `Customer_id`=@customerId);

		   IF @isValidCustDevice >= 1 THEN
				SET @loginStatusCd = 0;
                SET @loginStatus = "Username, DeviceID Matched";
			ELSE
				SET @loginStatusCd = 1;
                SET @loginStatus = "Username, DeviceID Mismatch";
			END IF;
	END IF;
	
SELECT 
    @loginStatusCd AS statusCd,
    @loginStatus AS loginStatus;
END
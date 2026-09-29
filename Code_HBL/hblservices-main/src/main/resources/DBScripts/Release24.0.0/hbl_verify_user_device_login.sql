USE `dbxdb`;
DROP procedure IF EXISTS `hbl_verify_user_device_login`;

USE `dbxdb`;
DROP procedure IF EXISTS `dbxdb`.`hbl_verify_user_device_login`;
;

DELIMITER $$
USE `dbxdb`$$
CREATE DEFINER=`infinitydev`@`%` PROCEDURE `hbl_verify_user_device_login`(
    IN _username VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    IN _deviceId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
) 
BEGIN
    -- Declare local variables first
    DECLARE customerId VARCHAR(20);
    DECLARE loginStatusCd INT;
    DECLARE loginStatus VARCHAR(255);

    -- Error handler for SQL exceptions
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        INSERT INTO dbxdb.mobile_device_login_audit (
            username, deviceId, loginStatusCd, loginStatus, audit_ts, error_msg
        )
        VALUES (
            _username, _deviceId, -1, 'Procedure Error', NOW(), 'SQL Exception occurred'
        );

        SELECT -1 AS statusCd, 'An error occurred, please try again later ...' AS loginStatus;
    END;

    -- Step 1: Fetch customer ID
    SELECT id INTO customerId
    FROM dbxdb.customer
    WHERE username = _username;

    -- Step 2: Determine login status
    IF customerId IS NULL THEN
        -- Customer not found, check if device exists
        IF EXISTS (
            SELECT 1 FROM dbxdb.customerdevice 
            WHERE Channel_id = 'CH_ID_MOB' 
              AND softdeleteflag = '0' 
              AND Status_id != 'SID_DEVICE_DE-REGISTERED' 
              AND id = _deviceId
        ) THEN
            SET loginStatusCd = 0;
            SET loginStatus = 'Customer not found, Existing device login';
        ELSE
            SET loginStatusCd = 0;
            SET loginStatus = 'New Customer or Device Login 1';
        END IF;
    ELSE
        -- Customer found, check if device matches
        IF EXISTS (
            SELECT 1 FROM dbxdb.customerdevice 
            WHERE Channel_id = 'CH_ID_MOB' 
              AND softdeleteflag = '0' 
              AND Status_id != 'SID_DEVICE_DE-REGISTERED' 
              AND id = _deviceId 
              AND Customer_id = customerId
        ) THEN
            SET loginStatusCd = 0;
            SET loginStatus = 'Username, DeviceID Matched';
        ELSE
            -- Check if customer has any registered devices
            IF NOT EXISTS (
                SELECT 1 FROM dbxdb.customerdevice 
                WHERE Channel_id = 'CH_ID_MOB' 
                  AND Status_id != 'SID_DEVICE_DE-REGISTERED' 
                  AND Customer_id = customerId
            ) THEN
                SET loginStatusCd = 0;
                SET loginStatus = 'New Customer or Device Login';
            ELSE
                SET loginStatusCd = 1;
                SET loginStatus = 'Username, DeviceID Mismatch';
            END IF;
        END IF;
    END IF;

    -- Step 3: Audit logging
    INSERT INTO dbxdb.mobile_device_login_audit (
        username, deviceId, loginStatusCd, loginStatus, audit_ts
    )
    VALUES (
        _username, _deviceId, loginStatusCd, loginStatus, NOW()
    );

    -- Final output
    SELECT loginStatusCd AS statusCd, loginStatus AS loginStatus;
END$$

DELIMITER ;
;


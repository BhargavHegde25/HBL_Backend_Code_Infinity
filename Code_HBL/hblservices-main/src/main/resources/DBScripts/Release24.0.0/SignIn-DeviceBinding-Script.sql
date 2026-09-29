ALTER TABLE `dbxdb`.`customerdevice` 
CHANGE COLUMN `id` `id` VARCHAR(60) NOT NULL ;

CREATE TABLE dbxdb.mobile_device_login_audit (
    audit_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    deviceId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
    loginStatusCd INT,
    loginStatus VARCHAR(255),
    audit_ts DATETIME DEFAULT CURRENT_TIMESTAMP,
    error_msg TEXT NULL
);
USE `dbxdb`;

DROP procedure IF EXISTS `dbxdb`.`update_makercheckerconfig_proc`;

;
 
DELIMITER $$

USE `dbxdb`$$


CREATE PROCEDURE `update_makercheckerconfig_proc`(
    IN _input longtext
)
BEGIN
    DECLARE _idx INT DEFAULT 0;
    DECLARE _maxIdx INT;
    DECLARE _companyLegalUnit VARCHAR(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci;
    DECLARE _isApprovalRequired TINYINT;
    DECLARE _id INT;
    DECLARE _input_json JSON;
   
    set _input_json = CAST(_input as JSON);
    set @_mc_data = JSON_EXTRACT(_input_json, '$[0].updates');
   	set _maxIdx = JSON_LENGTH(@_mc_data);
    WHILE _idx < _maxIdx DO
        SET _companyLegalUnit = JSON_UNQUOTE(JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].companyLegalUnit')));
        SET _isApprovalRequired = JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].isApprovalRequired'));
        SET _id = JSON_EXTRACT(@_mc_data, CONCAT('$[', _idx, '].id'));

        UPDATE `makercheckerconfig` 
        SET `isApprovalRequired` = _isApprovalRequired
        WHERE `id` = _id and `companyLegalUnit` = _companyLegalUnit COLLATE utf8mb4_general_ci;

        SET _idx = _idx + 1;
    END WHILE;
END
DROP PROCEDURE IF EXISTS customrole_contract_delete_proc;

DELIMITER $$
$$
CREATE PROCEDURE `customrole_contract_delete_proc`(in customRoleId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in coreCustomerId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci)
begin
	
	SET @contract_statement = 'DELETE FROM contractcustomrole where';
	SET @accounts_statement = 'DELETE FROM customroleaccounts where ';
	SET @action_statement = 'DELETE FROM customroleactionlimits where ';
	SET @limitgroup_statement = 'DELETE FROM customerlimitgrouplimits where ';
	
	SET @where_clause = '';
	SET @where_clause1 = '';
	SET @where_clause2 = '';
	if(customRoleId != '') THEN
		SET @where_clause = CONCAT(@where_clause , '`customRoleId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(customRoleId));
		SET @where_clause1 = CONCAT(@where_clause , '`customRole_id` = ');
		SET @where_clause1 = CONCAT(@where_clause , quote(customRoleId));
		SET @where_clause2 = CONCAT(@where_clause1 , '`Customer_id` = ');
		SET @where_clause2 = CONCAT(@where_clause1 , quote(customRoleId));
	
	END IF;
	
	IF(contractId != '') THEN
		IF(@where_clause != '') THEN
			SET @where_clause = CONCAT(@where_clause , ' AND ');
			SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
			SET @where_clause1 = CONCAT(@where_clause2 , ' AND ');
		END if;
		SET @where_clause = CONCAT(@where_clause , '`contractId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(contractId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`contractId` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(contractId));
		SET @where_clause2 = CONCAT(@where_clause2 , '`contractId` = ');
		SET @where_clause2 = CONCAT(@where_clause2 , quote(contractId));
	END IF;
	
	IF(coreCustomerId != '') THEN
		IF(@where_clause != '') THEN
			SET @where_clause = CONCAT(@where_clause , ' AND ');
			SET @where_clause1 = CONCAT(@where_clause1 , ' AND ');
		END if;
		SET @where_clause = CONCAT(@where_clause , '`coreCustomerId` = ');
		SET @where_clause = CONCAT(@where_clause , quote(coreCustomerId));
		SET @where_clause1 = CONCAT(@where_clause1 , '`coreCustomerId` = ');
		SET @where_clause1 = CONCAT(@where_clause1 , quote(coreCustomerId));
		SET @where_clause2 = CONCAT(@where_clause2 , '`coreCustomerId` = ');
		SET @where_clause2 = CONCAT(@where_clause2 , quote(coreCustomerId));
	END IF;
	
	IF(@where_clause != '') THEN	
		SET @contract_statement = CONCAT(@contract_statement , @where_clause);	
		SET @accounts_statement = CONCAT(@contract_statement , @where_clause);	
		SET @action_statement = CONCAT(@action_statement , @where_clause1);	
		SET @limitgroup_statement = CONCAT(@limitgroup_statement , @where_clause2);	
	
	
		PREPARE stmt FROM @contract_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @accounts_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @action_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
		PREPARE stmt FROM @limitgroup_statement; EXECUTE stmt; DEALLOCATE PREPARE stmt;
	END if;

END$$
DELIMITER ;
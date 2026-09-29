DROP PROCEDURE IF EXISTS `useraccounts_create_proc`;

delimiter $$

CREATE PROCEDURE `useraccounts_create_proc`(
IN _userId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _accountsCSV TEXT CHARACTER SET UTF8 COLLATE utf8_general_ci, 
IN _coreCustomerId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
IN _contractId VARCHAR(50) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN
DECLARE accountID varchar(255) ;
DECLARE finished INTEGER DEFAULT 0 ;

DECLARE accountData CURSOR FOR (SELECT contractaccounts.accountId FROM contractaccounts WHERE contractId = _contractId
        AND coreCustomerId = _coreCustomerId AND find_in_set(contractaccounts.accountId,_accountsCSV) );
	
DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
         
OPEN accountData; 
getAccount : LOOP
fetch accountData into accountID; 
     IF finished = 1 THEN 
	     LEAVE getAccount;
	 ELSE
         SET @id = (SELECT LEFT(UUID(), 50));
         set @accounttypeid = (select contractaccounts.typeId from contractaccounts where contractaccounts.accountId COLLATE utf8_general_ci =accountID);
         set @accounttypename = (select accounttype.TypeDescription from accounttype where accounttype.TypeID COLLATE utf8_general_ci =@accounttypeid);
		 INSERT INTO `customeraccounts` (`id`, `Customer_id`, `Account_id`,`contractId`, `coreCustomerId`,`accountType`)
            VALUES (@id, _userId, accountID, _contractId, _coreCustomerId,@accounttypename);
      END IF;
      ITERATE  getAccount;
END LOOP getAccount;
CLOSE accountData;
END$$

DELIMITER;
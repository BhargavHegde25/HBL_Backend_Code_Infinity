DROP procedure IF EXISTS `account_action_approvers_proc`;

DELIMITER $$
CREATE PROCEDURE `account_action_approvers_proc`(
in _contractId varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _cif varchar(50) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _accountIds text CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _approvalActionList varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci,
in _featureId varchar(100) CHARACTER SET UTF8 COLLATE utf8_general_ci
)
BEGIN

    SET SESSION group_concat_max_len = 1000000;

    IF _accountIds is NULL THEN
        SET _accountIds = '';
    END IF;


    SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",")
                           from (`customeraction`)
                           where
                            `customeraction`.`Action_id` = _approvalActionList
                             and `customeraction`.`isAllowed` = '1'
                             -- and FIND_IN_SET(`customeraction`.`Account_id`, _accountIds)
                             and `customeraction`.`contractId` = _contractId
                             and `customeraction`.`coreCustomerId` = _cif);

    IF _accountIds = '' THEN
        SET @customerIdList = (SELECT group_concat(DISTINCT `customeraction`.`Customer_id` SEPARATOR ",")
                               from (`customeraction`)
                               where `customeraction`.`Action_id` = _approvalActionList
                                 and `customeraction`.`contractId` = _contractId
                                 and `customeraction`.`coreCustomerId` = _cif);
        -- select @customerIdList;
    END IF;


    SET @customerIdList = IF(@customerIdList is null, '', @customerIdList);
    SET @NumberOfAccounts = LENGTH(_accountIds) - LENGTH(REPLACE(_accountIds, ',', '')) + 1;
    #     select @NumberOfAccounts;
#     select @_accountIds;
    SET @customerIdListWithNoAccountAccess = (SELECT group_concat(DISTINCT Customer_id SEPARATOR ",")
                                              from (SELECT Customer_id, Account_id
                                                    from customeraccounts
                                                    where FIND_IN_SET(Account_id, _accountIds)
                                                    group by Customer_id
                                                    having count(Account_id) != @NumberOfAccounts) AS tempcustomeraccounts);
# select @customerIdListWithNoAccountAccess;
    SET @customerIdListWithNoAccountAccess =
            IF(@customerIdListWithNoAccountAccess is null, '', @customerIdListWithNoAccountAccess);

# select @customerIdListWithNoAccountAccess;
    IF _accountIds = '' THEN
        SELECT DISTINCT (`customer`.`id`)        AS id,
                        (`customer`.`username`)  AS userName,
                        (`membergroup`.`Name`)   AS groupId,
                        (`customer`.`FirstName`) AS firstName,
                        (`customer`.`LastName`)  AS lastName
        from (`customer`
            LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and
                                              `contractcustomers`.`contractId` = _contractId and
                                              `contractcustomers`.`coreCustomerId` = _cif)
            LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
            LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
            INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `contractfeatures`
              ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
        where `contractfeatures`.`contractId` = _contractId
          and `contractfeatures`.`coreCustomerId` = _cif
          and `contractfeatures`.`featureId` = _featureId
#           and FIND_IN_SET(`customeraccounts`.`Account_id`, _accountIds)
          and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
          and

            ( FIND_IN_SET(`customer`.`id`, @customerIdList)
          OR FIND_IN_SET(`customer`.`id`, @customerIdListWithNoAccountAccess) )

          and FIND_IN_SET(`groupactionlimit`.`Action_id`, _approvalActionList) > 0
          and FIND_IN_SET(`customeraction`.`Action_id`, _approvalActionList) > 0;
    ELSE
        SELECT DISTINCT (`customer`.`id`)        AS id,
                        (`customer`.`username`)  AS userName,
                        (`membergroup`.`Name`)   AS groupId,
                        (`customer`.`FirstName`) AS firstName,
                        (`customer`.`LastName`)  AS lastName
        from (`customer`
            LEFT JOIN `contractcustomers` ON (`contractcustomers`.`customerId` = `customer`.`id` and
                                              `contractcustomers`.`contractId` = _contractId and
                                              `contractcustomers`.`coreCustomerId` = _cif)
            LEFT JOIN `customergroup` ON (`customergroup`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `customeraction` ON (`customeraction`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `membergroup` ON (`membergroup`.`id` = `customergroup`.`Group_id`)
            LEFT JOIN `groupactionlimit` ON (`groupactionlimit`.`Group_id` = `customergroup`.`Group_id`)
            INNER JOIN `customeraccounts` ON (`customeraccounts`.`Customer_id` = `customer`.`id`)
            LEFT JOIN `contractfeatures`
              ON (`contractfeatures`.`contractId` = _contractId and `contractfeatures`.`coreCustomerId` = _cif))
        where `contractfeatures`.`contractId` = _contractId
          and `contractfeatures`.`coreCustomerId` = _cif
          and `contractfeatures`.`featureId` = _featureId
          and FIND_IN_SET(`customeraccounts`.`Account_id`, _accountIds)
          and `customer`.`Status_id` = 'SID_CUS_ACTIVE'
          and
            ( FIND_IN_SET(`customer`.`id`, @customerIdList)
          OR FIND_IN_SET(`customer`.`id`, @customerIdListWithNoAccountAccess) )
          and FIND_IN_SET(`groupactionlimit`.`Action_id`, _approvalActionList) > 0
          and FIND_IN_SET(`customeraction`.`Action_id`, _approvalActionList) > 0;
    END IF;


END$$
DELIMITER ;
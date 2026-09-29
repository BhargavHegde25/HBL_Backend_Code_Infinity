GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[account_action_approvers_proc](
	@_contractId varchar(50),
    @_cif varchar(50),
    @_accountIds nvarchar(max),
    @_approvalActionList varchar(256),
    @_featureId varchar(256)
)
AS
BEGIN
 
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @customerIdList nvarchar(max);
    DECLARE @customerIdListWithNoAccountAccess nvarchar(max);
    DECLARE @NumberOfAccounts int;
 
    IF COALESCE(@_accountIds, '') = ''
    BEGIN
        DECLARE @temp_account_id TABLE (id NVARCHAR(65));
        INSERT INTO @temp_account_id (id)
        SELECT DISTINCT Account_id as id
        FROM [${dbxschemaname}].customeraction
        WHERE Action_id = @_approvalActionList
          AND contractId = @_contractId
          AND coreCustomerId = @_cif;
        SET @_accountIds = (SELECT STRING_AGG(id, ',') FROM @temp_account_id);
    END;
 
 
    SELECT @customerIdList = STRING_AGG(CAST(temp.Customer_id as nvarchar(max)), ',')
    FROM (
        SELECT DISTINCT [${dbxschemaname}].[customeraction].Customer_id as Customer_id
        FROM [${dbxschemaname}].[customeraction]
        WHERE [${dbxschemaname}].[customeraction].isAllowed = '1'
          AND [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
          AND [${dbxschemaname}].[customeraction].contractId = @_contractId
          AND [${dbxschemaname}].[customeraction].coreCustomerId = @_cif
    ) AS temp;
 
 
    IF COALESCE(@_accountIds, '') <> ''
    BEGIN
        SELECT @NumberOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
        SELECT @customerIdListWithNoAccountAccess = STRING_AGG(CAST(temp.Customer_id as nvarchar(max)), ',')
        FROM (
            SELECT DISTINCT [${dbxschemaname}].[customeraction].Customer_id
            FROM [${dbxschemaname}].[customeraction]
            WHERE [${dbxschemaname}].[customeraction].isAllowed = '1'
              AND [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
              AND [${dbxschemaname}].[customeraction].contractId = @_contractId
              AND [${dbxschemaname}].[customeraction].coreCustomerId = @_cif
              AND NOT EXISTS (
                  SELECT 1
                  FROM [${dbxschemaname}].[customeraccounts]
                  WHERE [${dbxschemaname}].[customeraccounts].Customer_id = [${dbxschemaname}].[customeraction].Customer_id
                    AND dbxdb.FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id, @_accountIds) > 0
              )
        ) AS temp;
    END;
 
    SET @customerIdList = COALESCE(@customerIdList, '');
    SET @customerIdListWithNoAccountAccess = COALESCE(@customerIdListWithNoAccountAccess, '');
 
 
    SELECT DISTINCT
        [${dbxschemaname}].[customer].id AS id,
        [${dbxschemaname}].[customer].username AS userName,
        [${dbxschemaname}].[membergroup].Name AS groupId,
        [customer].FirstName AS firstName,
        [customer].LastName AS lastName
    FROM [${dbxschemaname}].customer
    LEFT JOIN [${dbxschemaname}].contractcustomers ON [${dbxschemaname}].[customer].id = [${dbxschemaname}].[contractcustomers].customerId
                                         AND [${dbxschemaname}].[contractcustomers].contractId = @_contractId
                                         AND [${dbxschemaname}].[contractcustomers].coreCustomerId = @_cif
    LEFT JOIN [${dbxschemaname}].customergroup ON [${dbxschemaname}].[customer].id = [${dbxschemaname}].[customergroup].Customer_id
	LEFT JOIN [${dbxschemaname}].[customeraction] ON ([${dbxschemaname}].[customeraction].[Customer_id] = [${dbxschemaname}].[customer].id)
    LEFT JOIN [${dbxschemaname}].membergroup ON [${dbxschemaname}].[membergroup].id = [${dbxschemaname}].[customergroup].Group_id
    LEFT JOIN [${dbxschemaname}].groupactionlimit ON [${dbxschemaname}].[groupactionlimit].Group_id = [${dbxschemaname}].[customergroup].Group_id
    INNER JOIN [${dbxschemaname}].customeraccounts  ON [${dbxschemaname}].[customer].id = [${dbxschemaname}].[customeraccounts].Customer_id
    LEFT JOIN [${dbxschemaname}].contractfeatures ON [${dbxschemaname}].[contractfeatures].contractId = @_contractId
                                         AND [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif
    WHERE [${dbxschemaname}].[contractfeatures].contractId = @_contractId
      AND [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif
      AND [${dbxschemaname}].[contractfeatures].featureId = @_featureId
      AND [${dbxschemaname}].[customer].Status_id = 'SID_CUS_ACTIVE'
      AND (
            [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].id, @customerIdList) > 0
            OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].id, @customerIdListWithNoAccountAccess) > 0
          )
      AND [${dbxschemaname}].[groupactionlimit].Action_id IN (SELECT value FROM STRING_SPLIT(@_approvalActionList, ','))
      and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[groupactionlimit].Action_id,@_approvalActionList) > 0
  	  and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].Action_id,@_approvalActionList) > 0;
 
END
GO
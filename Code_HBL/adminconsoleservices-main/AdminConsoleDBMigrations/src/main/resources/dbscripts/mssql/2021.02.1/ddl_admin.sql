DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc];

GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc]  
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_accountIds nvarchar(max),
   @_actionId nvarchar(50),
   @_limitTypeId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      UPDATE [${dbxschemaname}].approvalmatrix
         SET 
            softdeleteflag = 1
      WHERE 
         [${dbxschemaname}].approvalmatrix.contractId = @_contractId AND 
		 [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif AND 
		 [${dbxschemaname}].FIND_IN_SET(CAST(approvalmatrix.accountId AS nvarchar(max)), @_accountIds) > 0 AND
         [${dbxschemaname}].approvalmatrix.actionId = @_actionId AND 
         [${dbxschemaname}].approvalmatrix.limitTypeId = @_limitTypeId

   END
GO

GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[account_action_approvers_proc];

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc](
 @_contractId varchar(50),
 @_cif varchar(50),
 @_accountIds text,
 @_approvalActionList varchar(50),
 @_featureId varchar(50)
)
AS
BEGIN
 declare @customerIdList nvarchar(max);
 declare @customerIdListWithNoAccountAccess nvarchar(max);
 declare @NumberOfAccounts int;
 SELECT @customerIdList =  STRING_AGG( CAST([${dbxschemaname}].[customeraction].Customer_id as nvarchar(max)), ',') WITHIN GROUP (ORDER BY [${dbxschemaname}].[customeraction].Customer_id ASC) from [${dbxschemaname}].[customeraction]
						where 
							[${dbxschemaname}].[customeraction].isAllowed = '0'
						and [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
                        and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].[Account_id], @_accountIds) > 0
                        and [${dbxschemaname}].[customeraction].contractId = @_contractId
                        and [${dbxschemaname}].[customeraction].coreCustomerId = @_cif;

SET @customerIdList = CASE WHEN @customerIdList is null THEN  '' ELSE  @customerIdList END;
SET @NumberOfAccounts = LEN(cast(@_accountIds as nvarchar(max))) - LEN(REPLACE(cast(@_accountIds as nvarchar(max)), ',', '')) + 1;

 select @customerIdListWithNoAccountAccess = (SELECT STRING_AGG(CAST(Customer_id as nvarchar(max)), ',') WITHIN GROUP (ORDER BY Customer_id ASC) from
                                            ( SELECT Customer_id, Account_id
                                            from [${dbxschemaname}].[customeraccounts]
                                            where [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountIds) > 0
                                            group by Customer_id,Account_id having
                                            count(Account_id) != @NumberOfAccounts ) AS tempcustomeraccounts);
										  
SET @customerIdListWithNoAccountAccess = CASE WHEN @customerIdListWithNoAccountAccess is null THEN  '' ELSE  @customerIdListWithNoAccountAccess END;

SELECT 
	DISTINCT ([${dbxschemaname}].[customer].id ) AS id , ([${dbxschemaname}].[customer].username) AS userName , ([${dbxschemaname}].[membergroup].Name) AS groupId,
										([customer].[FirstName]) AS firstName , ([customer].[LastName]) AS lastName
from 
	([${dbxschemaname}].[customer]
LEFT JOIN [${dbxschemaname}].[contractcustomers] ON ([${dbxschemaname}].[contractcustomers].customerId = [${dbxschemaname}].[customer].[id] and 
[${dbxschemaname}].[contractcustomers].[contractId] = @_contractId and [${dbxschemaname}].[contractcustomers].[coreCustomerId] = @_cif)
LEFT JOIN [${dbxschemaname}].[customergroup] ON ([${dbxschemaname}].[customergroup].[Customer_id] = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[membergroup] ON ([${dbxschemaname}].[membergroup].id = [${dbxschemaname}].[customergroup].Group_id)
LEFT JOIN [${dbxschemaname}].[groupactionlimit] ON ([${dbxschemaname}].[groupactionlimit].Group_id = [${dbxschemaname}].[customergroup].Group_id)
INNER JOIN [${dbxschemaname}].[customeraccounts] ON ([${dbxschemaname}].[customeraccounts].Customer_id = [${dbxschemaname}].[customer].id)
LEFT JOIN [${dbxschemaname}].[contractfeatures] ON ([${dbxschemaname}].[contractfeatures].contractId = @_contractId and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif))
	where 
        [${dbxschemaname}].[contractfeatures].contractId = @_contractId
        and [${dbxschemaname}].[contractfeatures].coreCustomerId = @_cif
        and [${dbxschemaname}].[contractfeatures].featureId = @_featureId
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraccounts].Account_id,  @_accountIds) > 0
		and [${dbxschemaname}].[customer].Status_id = 'SID_CUS_ACTIVE'
        and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].[id],  @customerIdList) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customer].id,  @customerIdListWithNoAccountAccess) = 0
		and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[groupactionlimit].Action_id,@_approvalActionList) > 0;      
END

GO
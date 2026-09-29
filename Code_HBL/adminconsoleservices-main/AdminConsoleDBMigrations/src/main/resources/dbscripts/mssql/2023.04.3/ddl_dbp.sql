SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER   PROCEDURE [${dbxschemaname}].[contract_users_details_get_proc]
@_contractId nvarchar(50),
@_backendType nvarchar(50)
AS
	BEGIN
		SET XACT_ABORT ON
		SET NOCOUNT ON 
		DECLARE @customers nvarchar(max) = N''
		
		SET @customers = (SELECT
		String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',')
		FROM [${dbxschemaname}].contractcustomers WHERE
		[${dbxschemaname}].contractcustomers.contractId = @_contractId)
		
		SET @customers = (SELECT DISTINCT String_agg(CAST(contractcustomers.				customerId AS nvarchar(max)), ',')
						FROM [${dbxschemaname}].contractcustomers 
						WHERE [${dbxschemaname}].[contractcustomers].customerId IN 
						(SELECT DISTINCT value FROM STRING_SPLIT(@customers, ',')))
		
		SELECT
		customer.id AS customerId,
		customer.FirstName AS firstName,
		customer.MiddleName AS middleName,
		customer.LastName AS lastName,
		customer.UserName AS userName,
		customer.Status_id AS statusId,
		customer.DateOfBirth AS dateOfBirth,
		customer.Ssn AS Ssn,
		BI.BackendId AS primaryCoreCustomerId,
		CC.Value AS Email
		FROM
		[${dbxschemaname}].customer
		LEFT JOIN [${dbxschemaname}].customercommunication CC ON (CC.Customer_id = customer.id AND CC.Type_id = 'COMM_TYPE_EMAIL' AND CC.Customer_id IN (@customers))
		LEFT JOIN [${dbxschemaname}].backendidentifier BI ON (BI.Customer_id = customer.id AND BI.BackendType = @_backendType AND BI.Customer_id IN (@customers))
		WHERE
		customer.id IN (SELECT DISTINCT value FROM STRING_SPLIT(@customers, ','));
	END;
GO
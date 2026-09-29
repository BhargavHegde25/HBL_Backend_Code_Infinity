CREATE TABLE [${dbxschemaname}].[customerlegalentity] (
  [id] UNIQUEIDENTIFIER NOT NULL default NEWID(),
  [Customer_id] nvarchar(50) NOT NULL,		
  [Status_id] nvarchar(50) NOT NULL,
  [legalEntityId] nvarchar(50) NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  PRIMARY KEY ([id]),
  UNIQUE ([Customer_id],[legalEntityId]),
  CONSTRAINT [FK_customerlegalentity_Customer] FOREIGN KEY ([Customer_id]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO

ALTER TABLE [${dbxschemaname}].[customer] ADD [homeLegalEntity] NVARCHAR(50) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[customer] ADD [defaultLegalEntity] NVARCHAR(50) DEFAULT NULL;
GO

CREATE PROCEDURE [${dbxschemaname}].[update_userstatus_by_legalentity_proc]  
	@customerId nvarchar(50),
	@statusId  nvarchar(50),
	@isOLB  nvarchar(50),
	@legalEntityList  nvarchar(50)
AS 
	BEGIN
	    SET  XACT_ABORT  ON
		SET  NOCOUNT  ON
		
		IF @isOLB = 'true'
			BEGIN
				UPDATE [customerlegalentity] SET Status_id = @statusId WHERE Customer_id = @customerId
			END
		ELSE
			BEGIN
				UPDATE [${dbxschemaname}].[customerlegalentity] SET Status_id = @statusId 
				WHERE Customer_id = @customerId AND 
				[${dbxschemaname}].FIND_IN_SET(legalEntityId,@legalEntityList)='1';
				SELECT * FROM [${dbxschemaname}].[customerlegalentity] 
				WHERE Customer_id = @customerId AND
				[${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerlegalentity.legalEntityId,@legalEntityList) = '1';
			END
	END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[customers_get_legalentities_proc] 
GO

CREATE PROCEDURE [${dbxschemaname}].[customers_get_legalentities_proc]  
   @_customerList nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  select customerlegalentity.Customer_id , customerlegalentity.legalEntityId  
	  from [${dbxschemaname}].customerlegalentity where
	  customerlegalentity.Customer_id IN (SELECT DISTINCT value FROM STRING_SPLIT(@_customerList, ',')); 
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_basic_info_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]
	@_customerId nvarchar(50),
	@_legalEntityId nvarchar(50)
AS
	BEGIN
		SET XACT_ABORT ON
		SET NOCOUNT ON
		declare @accountLockoutThreshold nvarchar(max)
		declare @accountLockoutTime nvarchar(max)
		SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
		SET @accountLockoutTime =(SELECT accountLockoutTime from [${dbxschemaname}].passwordlockoutsettings)
		
		SELECT TOP (1)
			customer.UserName AS Username,
			customer.FirstName AS FirstName,
			customer.MiddleName AS MiddleName,
			customer.LastName AS LastName,
			(ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS Name,
			customer.Salutation AS Salutation,
			customer.id AS Customer_id,
			customer.Ssn AS SSN,
			customer.createdts AS CustomerSince,
			customer.Gender AS Gender,
			customer.DateOfBirth AS DateOfBirth,
			customer.isEnrolledFromSpotlight AS isEnrolledFromSpotlight,
			CASE
				WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
			ELSE CASE
				WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
			ELSE customer.Status_id
			END
			END AS CustomerStatus_id,
			customerstatus.Description AS CustomerStatus_name,
			customer.MaritalStatus_id AS MaritalStatus_id,
			maritalstatus.Description AS MaritalStatus_name,
			customer.SpouseName AS SpouseName,
			customer.DrivingLicenseNumber AS DrivingLicenseNumber,
			customer.lockedOn AS lockedOn,
			customer.lockCount AS lockCount,
			customer.EmployementStatus_id AS EmployementStatus_id,employementstatus.Description AS EmployementStatus_name,
			( SELECT String_agg(CAST(Status_id as nvarchar(max)),',')
				FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id) ) AS CustomerFlag_ids,
			( SELECT String_agg(CAST(Description as nvarchar(max)),',')
				FROM [${dbxschemaname}].status
				WHERE status.id IN
			(
				SELECT customerflagstatus.Status_id
					FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id)
			)) AS CustomerFlag,
			customer.IsEnrolledForOlb AS IsEnrolledForOlb,
			customer.isEnrolled AS isEnrolled,
			customer.IsStaffMember AS IsStaffMember,
			customer.Location_id AS Branch_id,
			location.Name AS Branch_name,
			location.Code AS Branch_code,
			customer.IsOlbAllowed AS IsOlbAllowed,
			customer.IsAssistConsented AS IsAssistConsented,
			customer.isEagreementSigned AS isEagreementSigned,
			customer.companyLegalUnit AS companyLegalUnit,
			customer.isCombinedUser AS isCombinedUser,
			ISNULL(customer.combinedUserId, N'') AS combinedUserId,
			IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS CustomerType_id,
			IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS CustomerType_Name,
			IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS CustomerType_Description,
			(SELECT membergroup.id FROM [${dbxschemaname}].membergroup WHERE membergroup.id in
			(SELECT customergroup.Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_RoleId,
			(SELECT membergroup.Name FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS Customer_Role,
			(SELECT membergroup.isEAgreementActive FROM [${dbxschemaname}].membergroup WHERE id in (SELECT Group_id from [${dbxschemaname}].customergroup where customergroup.Customer_id= @_customerId) AND membergroup.Type_id = 'TYPE_ID_BUSINESS' ) AS isEAgreementRequired,
			customer.Organization_Id AS organisation_id,
			organisation.BusinessType_id AS BusinessType_id,
			businesstype.name AS BusinessType,
			organisation.Name AS organisation_name,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryEmailAddress,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryEmailAddress,
			customer.DocumentsSubmitted AS DocumentsSubmitted,
			customer.ApplicantChannel AS ApplicantChannel,
			customer.Product AS Product,
			customer.Reason AS Reason,
			@accountLockoutTime AS accountLockoutTime
		FROM ((((((([${dbxschemaname}].customer
		LEFT JOIN [${dbxschemaname}].location
		ON ((customer.Location_id = location.id)))
		LEFT JOIN [${dbxschemaname}].organisation
		ON ((customer.Organization_Id = organisation.id)))
		LEFT JOIN [${dbxschemaname}].businesstype ON ((businesstype.id = organisation.BusinessType_id)))
		INNER JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		LEFT JOIN [${dbxschemaname}].status AS customerstatus
		ON ((customer.Status_id = customerstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS maritalstatus
		ON ((customer.MaritalStatus_id = maritalstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS employementstatus
		ON ((customer.EmployementStatus_id = employementstatus.id)))
		WHERE customer.id = @_customerId
END;
GO



DROP procedure IF EXISTS [${dbxschemaname}].[customer_legalentities_get_proc] 
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_legalentities_get_proc]  
   @customerid nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  select 
		cust.id  as customerid,
		cust.homeLegalEntity as homeLegalEntity ,
		custleg.legalEntityId as legalEntityId,
		custleg.Status_id  as statusId 
	 from [${dbxschemaname}].customer cust
	 left join [${dbxschemaname}].customerlegalentity custleg  on (cust.id = custleg.Customer_id)
	 where cust.id = @customerid;
	  
	  
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_customerstatus_default_legalentity];
GO

CREATE PROCEDURE [${dbxschemaname}].[update_customerstatus_default_legalentity]
	@customerId nvarchar(50)
AS
	BEGIN
		SET  XACT_ABORT  ON
		SET  NOCOUNT  ON
		DECLARE @statusId nvarchar(255) = N''
		DECLARE @currentStatus nvarchar(255) = N''
		DECLARE @activeStatus INT = 0

		DECLARE statuses CURSOR 
		LOCAL FOR
		(SELECT customerlegalentity.Status_id 
		FROM customerlegalentity 
		WHERE Customer_id = @customerId)

		SET @currentStatus = (SELECT [${dbxschemaname}].[customer].[Status_Id] FROM [${dbxschemaname}].[customer] WHERE  [${dbxschemaname}].[customer].[id] = @customerId)
		SET @activeStatus = 0
		OPEN statuses
		FETCH NEXT FROM statuses INTO @statusId
		WHILE (@@FETCH_STATUS=0)
			BEGIN
				IF @statusId LIKE '%ACTIVE%' AND  @currentStatus LIKE '%ACTIVE%'
					BEGIN
						SET @activeStatus = 1
					END
				IF @statusId LIKE '%ACTIVE%' AND  @currentStatus NOT LIKE '%ACTIVE%'
					BEGIN
						SET @activeStatus = 1
						UPDATE [${dbxschemaname}].[customer] SET Status_id = 'SID_CUS_ACTIVE' 
						WHERE [${dbxschemaname}].[customer].[id] =  @customerId
					END 
				FETCH NEXT FROM statuses INTO @statusId
			END
		CLOSE statuses
		DEALLOCATE statuses
		IF @activeStatus = 0
			BEGIN
				UPDATE [${dbxschemaname}].[customer] SET Status_id = 'SID_CUS_SUSPENDED' 
				WHERE [${dbxschemaname}].[customer].[id] =  @customerId
				AND @currentStatus NOT LIKE '%SID_CUS_NEW%'
			END 
	END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_users_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[contract_users_details_get_proc]
@_contractId nvarchar(50),
@_backendType nvarchar(50)
AS
BEGIN
​
SET XACT_ABORT ON
SET NOCOUNT ON
​
DECLARE
@customers nvarchar(max) = N''
​
SET @customers = (SELECT
String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].contractcustomers WHERE
[${dbxschemaname}].contractcustomers.contractId = @_contractId)

SET @customers = [${dbxschemaname}].DISTINCT_VALUE(@customers);
​
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
LEFT JOIN [${dbxschemaname}].customercommunication CC ON (CC.Customer_id = customer.id AND CC.Type_id = 'COMM_TYPE_EMAIL' AND CC.isPrimary = 1 AND CC.Customer_id IN (@customers))
LEFT JOIN [${dbxschemaname}].backendidentifier BI ON (BI.Customer_id = customer.id AND BI.BackendType = @_backendType AND BI.companyLegalUnit = customer.companyLegalUnit and BI.Customer_id IN (@customers))
WHERE
customer.id IN (SELECT DISTINCT value FROM STRING_SPLIT(@customers, ','));
​
END
GO

ALTER TABLE [${dbxschemaname}].[customerimage] ADD [id] INT NOT NULL IDENTITY(1,1), [legalEntityId] VARCHAR(50) NOT NULL;
ALTER TABLE [${dbxschemaname}].[customerimage] DROP CONSTRAINT [PK_customerimage_Customer_id];
ALTER TABLE [${dbxschemaname}].[customerimage] ADD PRIMARY KEY ([id]);
CREATE UNIQUE INDEX UI_customerimage_Customer_id ON [${dbxschemaname}].[customerimage] ([Customer_id], [legalEntityId]);
GO
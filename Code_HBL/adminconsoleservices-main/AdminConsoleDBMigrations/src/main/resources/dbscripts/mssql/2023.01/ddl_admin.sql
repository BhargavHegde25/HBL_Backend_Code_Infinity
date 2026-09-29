ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [accountStatus] VARCHAR(50) DEFAULT 'ACTIVE';
ALTER TABLE [${dbxschemaname}].[customeraccounts] ADD [isSweepCreated] BIT;


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[corecustomeraccounts_details_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[corecustomeraccounts_details_get_proc]  
   @_coreCustomerIdList nvarchar(max),
   @_customerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  DECLARE @selectstatement NVARCHAR(max);
	  DECLARE @corecustomersList NVARCHAR(max);

      SET @corecustomersList = 
         (

            SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',')
            FROM [${dbxschemaname}].contractcustomers
            WHERE contractcustomers.customerId = @_customerId AND [${dbxschemaname}].FIND_IN_SET(contractcustomers.coreCustomerId, @_coreCustomerIdList) > 0

         )
      

      SET @selectstatement = CONCAT('(SELECT
	       contractcorecustomers.coreCustomerId AS coreCustomerId ,
           contractcorecustomers.coreCustomerName AS coreCustomerName ,
           customeraccounts.email AS email ,
           customeraccounts.Customer_id AS customerId ,
           customeraccounts.FavouriteStatus AS favouriteStatus ,
		   customeraccounts.NickName AS nickName ,
		   customeraccounts.accountStatus AS accountStatus ,
           IIF ((customeraccounts.EStatementmentEnable) = ''1'' ,''true'' , ''false'') AS eStatementEnable,
		   IIF ((customeraccounts.isSweepCreated) = ''1'' ,''true'' , ''false'') AS isSweepCreated,
           IIF ((contractcorecustomers.isBusiness) = ''1'' ,''true'' , ''false'') AS isBusinessAccount,
           contractaccounts.accountId AS accountId
           from [${dbxschemaname}].contractcorecustomers 
		   JOIN [${dbxschemaname}].contractaccounts ON (contractcorecustomers.coreCustomerId = contractaccounts.coreCustomerId)
           JOIN [${dbxschemaname}].customeraccounts ON (customeraccounts.Account_id = contractaccounts.accountId)
           where [${dbxschemaname}].FIND_IN_SET(contractcorecustomers.coreCustomerId, ','''',@corecustomersList,'''',')>0 AND customeraccounts.Customer_id = ','''',@_customerId,'''',')')
      
	  exec(@selectstatement);
   END
GO
CREATE TABLE [${dbxschemaname}].[accountsweeps] (
[id] int IDENTITY,
[primaryAccountName] nvarchar(50) DEFAULT NULL,
[primaryAccountNumber] nvarchar(50) NOT NULL,
[secondaryAccountName] nvarchar(50) DEFAULT NULL,
[secondaryAccountNumber] nvarchar(50) DEFAULT NULL,
[belowSweepAmount] nvarchar(50) DEFAULT NULL,
[aboveSweepAmount] nvarchar(50) DEFAULT NULL,
[currencyCode] nvarchar(50) DEFAULT NULL,
[frequency] nvarchar(50) DEFAULT NULL,
[startDate] nvarchar(50) DEFAULT NULL,
[endDate] nvarchar(50) DEFAULT NULL,
[serviceRequestId] nvarchar(50) DEFAULT NULL,
[softDelete] bit DEFAULT 0,
CONSTRAINT id_UNIQUE UNIQUE ([id]),
PRIMARY KEY ([primaryAccountNumber])
) ;
GO

ALTER TABLE [${dbxschemaname}].[customer] ADD [isQRPaymentActivated] BIT;
ALTER TABLE [${dbxschemaname}].[customerpreference] ADD [DefaultFromAccountQR] VARCHAR(50) NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[systemroles_permission_proc]  
   @_roleIds varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 SELECT distinct
        [p].[id] AS id,
        [p].[Name] AS name,
		[rp].[companyLegalUnit]  AS companyLegalUnit,
        [p].[Status_id] AS status,
        [p].[PermissionValue] AS PermissionValue,
        [p].[isComposite] AS isComposite,
        [rp].[Role_id] AS Role_id,
		[p].[softdeleteflag] AS softdeleteflag
    FROM
    (rolepermission rp
	JOIN permission p ON ([p].[id] = [rp].[Permission_id])
    JOIN role r ON ([r].[id] = [rp].[Role_id] AND [r].[companyLegalUnit] = [rp].[companyLegalUnit]))
    WHERE [p].[Status_id]='SID_ACTIVE' AND [r].[Status_id]='SID_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) > 0 ORDER BY [id];
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[servicedefinition_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE VIEW [${dbxschemaname}].[servicedefinition_view] AS
     SELECT 
    [${dbxschemaname}].[servicedefinition].[id] AS [id],
    [${dbxschemaname}].[servicedefinition].[name] AS [name],
    [${dbxschemaname}].[servicedefinition].[description] AS [description],
    [${dbxschemaname}].[servicedefinition].[serviceType] AS [serviceType],
    [${dbxschemaname}].[servicedefinition].[status] AS [status],
	[${dbxschemaname}].[servicedefinition].[companyLegalUnit] AS [companyLegalUnit],
    (SELECT COUNT([${dbxschemaname}].[groupservicedefinition].[Group_id]) FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfRoles],
	(SELECT COUNT([${dbxschemaname}].[membergroup].[id]) FROM [${dbxschemaname}].[membergroup] WHERE Status_id LIKE 'SID_ACTIVE' AND id in (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM 
		[${dbxschemaname}].[groupservicedefinition] WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]))) AS [numberOfActiveRoles],
    (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE (([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]) AND ([${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = 1) AND ([${dbxschemaname}].[groupservicedefinition].[companyLegalUnit] = [${dbxschemaname}].[servicedefinition].[companyLegalUnit]))) AS [defaultRole],
    (SELECT COUNT(DISTINCT [${dbxschemaname}].[servicedefinition_features_actions_view].[featureId]) FROM [${dbxschemaname}].[servicedefinition_features_actions_view]
        WHERE (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinition_features_actions_view].[serviceDefinitionId]) AND ([${dbxschemaname}].[servicedefinition_features_actions_view].[softdelete] = '0'))) AS [numberOfFeatures],
    (SELECT COUNT([${dbxschemaname}].[contract].[id]) FROM [${dbxschemaname}].[contract] 
      WHERE ([${dbxschemaname}].[contract].[servicedefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfContracts]
  FROM [${dbxschemaname}].[servicedefinition];
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[servicedefinition_view_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[servicedefinition_view_proc]
@_typeId nvarchar(50),
@_companyLegalUnit nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @index1 INT;
DECLARE @numOfCompanyLegalUnits INT;
DECLARE @companyLegalUnitId NVARCHAR(MAX);
DECLARE @companyLegalUnits NVARCHAR(MAX);
DECLARE @select_statement nvarchar(max);

SET @select_statement = 'SELECT id, name, description, serviceType, status, companyLegalUnit,
(SELECT COUNT(Group_id) AS Expr1
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id)) AS numberOfRoles,
(SELECT COUNT(id) AS Expr1
FROM ${dbxschemaname}.membergroup
WHERE (Status_id LIKE ''SID_ACTIVE'') AND (id IN
(SELECT Group_id
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id)))) AS numberOfActiveRoles,
(SELECT Group_id
FROM ${dbxschemaname}.groupservicedefinition
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id) AND (isDefaultGroup = 1) AND (${dbxschemaname}.groupservicedefinition.companyLegalUnit = ${dbxschemaname}.servicedefinition.companyLegalUnit)) AS defaultRole,
(SELECT COUNT(DISTINCT featureId) AS Expr1
FROM ${dbxschemaname}.servicedefinition_features_actions_view
WHERE (${dbxschemaname}.servicedefinition.id = serviceDefinitionId) AND (softdelete = ''0'')) AS numberOfFeatures,
(SELECT COUNT(id) AS Expr1
FROM ${dbxschemaname}.contract
WHERE (servicedefinitionId = ${dbxschemaname}.servicedefinition.id)) AS numberOfContracts
FROM ${dbxschemaname}.servicedefinition';

SET @companyLegalUnits = '';
SET @numOfCompanyLegalUnits = 0;

IF LEN(@_companyLegalUnit) > 0
BEGIN
    SET @numOfCompanyLegalUnits = LEN(@_companyLegalUnit) - LEN(REPLACE(@_companyLegalUnit, '|', '')) + 1;
END
SET @index1 = 0;
IF (@numOfCompanyLegalUnits >= 1)
BEGIN 
    WHILE (1 = 1)
    BEGIN
        SET @index1 = @index1 + 1;
        IF @index1 = @numOfCompanyLegalUnits + 1
            BREAK
        ELSE 
        BEGIN
            SET @companyLegalUnitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_companyLegalUnit, '|', @index1), '|', -1);
            SET @companyLegalUnits = CONCAT(@companyLegalUnits, '''', @companyLegalUnitId, '''');
            IF @index1 != @numOfCompanyLegalUnits
            BEGIN
                SET @companyLegalUnits = CONCAT(@companyLegalUnits, ',');
            END;
        END 
    END
    SET @select_statement = CONCAT(@select_statement, ' WHERE companyLegalUnit IN (', @companyLegalUnits,')');
END
IF @_typeId is not null and len(@_typeId)>0  
BEGIN
    IF @numOfCompanyLegalUnits >= 1
        SET @select_statement = CONCAT(@select_statement, ' AND serviceType =''', @_typeId,'''');
    ELSE
        SET @select_statement = CONCAT(@select_statement, ' WHERE serviceType =''', @_typeId,'''');
END;
exec(@select_statement);
END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[customeraccounts_sweepinfoupdate_proc]
  @_customerId VARCHAR(50),
  @_primaryAccount VARCHAR(50),
  @_secondaryAccount VARCHAR(50),
  @_sweepFlag VARCHAR(50),
  @_previousAccount VARCHAR(50)
AS
BEGIN
  declare @sweepFlag BIT;

  set @sweepFlag = 1;
  IF (@_sweepFlag='false')
    set @sweepFlag = 0;

  IF (@_primaryAccount IS NOT NULL AND @_primaryAccount != '')
    UPDATE [${dbxschemaname}].customeraccounts SET [isSweepCreated] = @sweepFlag WHERE id in
    (SELECT id from customeraccounts WHERE Customer_id = @_customerId and Account_id = @_primaryAccount);

  IF (@_secondaryAccount IS NOT NULL AND @_secondaryAccount != '')
    UPDATE [${dbxschemaname}].customeraccounts SET [isSweepCreated] = @sweepFlag WHERE id in
    (SELECT id from customeraccounts WHERE Customer_id = @_customerId and Account_id = @_secondaryAccount);

  IF (@_previousAccount IS NOT NULL AND @_previousAccount != '')
    UPDATE [${dbxschemaname}].customeraccounts SET [isSweepCreated] = 0 WHERE id in
    (SELECT id from customeraccounts WHERE Customer_id = @_customerId and Account_id = @_previousAccount);

END
GO

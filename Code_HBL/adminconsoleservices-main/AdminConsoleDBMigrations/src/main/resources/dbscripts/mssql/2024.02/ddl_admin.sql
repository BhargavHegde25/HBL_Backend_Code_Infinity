USE [${dbxschemaname}];
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[get_mc_approvalrequests_view];
GO

CREATE VIEW [${dbxschemaname}].[get_mc_approvalrequests_view](
    [action],
    [requestCount],
    [createdby],
    [status],
    [companyLegalUnit],
    [approvalPermissionName]
    ) AS
    SELECT 
        [mcconfig].[action] AS [action],
        COUNT([ar].[requestId]) AS [requestCount],
        [ar].[createdby] AS [createdby],
        [ar].[status] AS [status],
        [ar].[companyLegalUnit] AS [companyLegalUnit],
        [mcconfig].[approvalPermissionName] AS [approvalPermissionName]
    FROM
    [${dbxschemaname}].[approvalrequests] as [ar] JOIN [${dbxschemaname}].[makercheckerconfig] as [mcconfig]
		ON ([ar].[expAPIOperationName] = [mcconfig].[expAPIOperationName] AND [ar].[companyLegalUnit] = [mcconfig].[companyLegalUnit])
    GROUP BY [mcconfig].[action] , [ar].[createdby] , [ar].[status] , [ar].[companyLegalUnit] , [mcconfig].[approvalPermissionName];
GO
	
ALTER TABLE [${dbxschemaname}].[approvalrequests] ADD [action] varchar(50);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[mcactiontext];
GO

CREATE TABLE [${dbxschemaname}].[mcactiontext] (
  [id] VARCHAR(50) NOT NULL,
  [name] VARCHAR(50) NOT NULL,
  [language_code] VARCHAR(10) NOT NULL,
  [createdts] datetime2 NOT NULL DEFAULT getDate(),
  [lastmodifiedts] datetime2 NOT NULL DEFAULT getDate(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id], [language_code]));
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[mcmoduletext];
GO

CREATE TABLE [${dbxschemaname}].[mcmoduletext] (
  [id] VARCHAR(50) NOT NULL,
  [name] VARCHAR(50) NOT NULL,
  [language_code] VARCHAR(10) NOT NULL,
  [createdts] datetime2 NOT NULL DEFAULT getDate(),
  [lastmodifiedts] datetime2 NOT NULL DEFAULT getDate(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id], [language_code]));
GO

-- Drop the procedure if it exists
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_sba_securityattributes_get_proc];
GO

-- Create the procedure
CREATE PROCEDURE [${dbxschemaname}].[user_sba_securityattributes_get_proc]
  @_userId NVARCHAR(50),
  @_legalEntityId NVARCHAR(50)
AS
BEGIN
DECLARE @userAssociatedCoreCustomers NVARCHAR(255);
SELECT @userAssociatedCoreCustomers = STRING_AGG(coreCustomerId, ',') 
    FROM [dbxdb].contractcustomers 
    WHERE customerId = @_userId  AND companyLegalUnit = @_legalEntityId;
 
SELECT contractId,coreCustomerId,Action_id,featureId FROM [dbxdb].customeraction WHERE Customer_id = @_userId
        AND (isAllowed = '1' OR isAllowed = 'true')
        AND companyLegalUnit = @_legalEntityId
        AND coreCustomerId IN (SELECT value FROM STRING_SPLIT(@userAssociatedCoreCustomers, ','))
        AND (Action_id LIKE 'SBA_%' OR Action_id LIKE 'CASHFLOW_%');
 
SELECT backendidentifier.BackendId , backendidentifier.Customer_id,customer.sbaEnrolmentStatus from 
[dbxdb].backendidentifier,[dbxdb].customer where customer.id =backendidentifier.Customer_id AND BackendType = 'CORE' 
AND backendidentifier.BackendId IN (SELECT value FROM STRING_SPLIT(@userAssociatedCoreCustomers, ','));
END;
GO
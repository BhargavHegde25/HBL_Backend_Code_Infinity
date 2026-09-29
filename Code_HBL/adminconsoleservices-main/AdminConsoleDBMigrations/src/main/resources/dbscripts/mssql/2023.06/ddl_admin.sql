DROP VIEW IF EXISTS [${dbxschemaname}].[configuration_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [${dbxschemaname}].[configuration_view]
      AS 
         SELECT 
            
               configurations.configuration_id AS configuration_id, 
               configurations.bundle_id AS bundle_id, 
               configurationbundles.bundle_name AS bundle_name, 
               configurations.config_type AS [type], 
               configurations.config_key AS [key], 
               configurations.description AS description, 
               configurations.config_value AS [value], 
               configurations.target AS target, 
               configurations.isPreLoginConfiguration AS isPreLoginConfiguration,
			   configurations.companyLegalUnit AS companyLegalUnit,
               configurationbundles.app_id AS app_id,
			   configurations.lastmodifiedts AS lastmodifiedts

         FROM [${dbxschemaname}].configurations 
            LEFT JOIN [${dbxschemaname}].configurationbundles ON configurationbundles.bundle_id = configurations.bundle_id;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_default_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrixtemplate_default_create_proc](
	@_actionIds NVARCHAR(max) , 
	@_contractId NVARCHAR(50) ,
	@_cif NVARCHAR(50),
	@_approvalMode int,
	@_currency VARCHAR(32)
) AS
BEGIN
 DECLARE @accountList VARCHAR(max) = 0;
 DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT';
 DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT';
 DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT';
 DECLARE @actionIndex INTEGER = 0;
 DECLARE @isGroupMatrix int = 0;
 DECLARE @typeId VARCHAR(max) = '';
 DECLARE @numOfAccounts int;
 DECLARE @numOfActions int;
 DECLARE @accountId NVARCHAR(50);
 DECLARE @actionId NVARCHAR(255);
 DECLARE @legalEntityId NVARCHAR(255);
 
IF @_actionIds IS NULL OR  @_actionIds = ''
	GOTO MAINLABEL$leave
	
IF @_contractId IS NULL OR  @_contractId = ''
	GOTO MAINLABEL$leave
	
IF @_cif IS NULL OR  @_cif = ''
	GOTO MAINLABEL$leave

SET @legalEntityId = (SELECT companyLegalUnit from contractcorecustomers where coreCustomerId = @_cif and contractId = @_contractId);

IF @_approvalMode = 0 BEGIN
	SET @isGroupMatrix = 0;
END
ElSE BEGIN
	SET @isGroupMatrix = 1;
END
	
set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
set @actionIndex = 0;
getAction: WHILE 1=1 BEGIN
	set @actionIndex = @actionIndex + 1;
	IF @actionIndex = @numOfActions + 1 BEGIN 
		BREAK;
	End
	Else Begin
		set @actionId = [dbxdb].SUBSTRING_INDEX([dbxdb].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex), ',', -1 ); 
		SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId and companyLegalUnit = @legalEntityId;
        IF @typeId = 'MONETARY' BEGIN			
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency,companyLegalUnit) VALUES
			(@_contractId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency,@legalEntityId);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency,companyLegalUnit) VALUES
			(@_contractId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency,@legalEntityId);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency,companyLegalUnit) VALUES
			(@_contractId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency,@legalEntityId);
        END
        ELSE IF @typeId = 'NON_MONETARY' BEGIN
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency,companyLegalUnit) VALUES
            (@_contractId, @actionId, 'NON_MONETARY_LIMIT', @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency,@legalEntityId);
		END
	END 
END;  
MAINLABEL$leave: 
END;
GO

ALTER TABLE [${dbxschemaname}].[configurations] ADD [tncTimeStamp] VARCHAR(20);

DROP VIEW IF EXISTS [${dbxschemaname}].[configuration_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE VIEW [${dbxschemaname}].[configuration_view]
      AS 
         SELECT 
            
               configurations.configuration_id AS configuration_id, 
               configurations.bundle_id AS bundle_id, 
               configurationbundles.bundle_name AS bundle_name, 
               configurations.config_type AS [type], 
               configurations.config_key AS [key], 
               configurations.description AS description, 
               configurations.config_value AS [value], 
               configurations.target AS target, 
               configurations.isPreLoginConfiguration AS isPreLoginConfiguration,
			   configurations.companyLegalUnit AS companyLegalUnit,
               configurationbundles.app_id AS app_id,
			   configurations.lastmodifiedts AS lastmodifiedts,
			   (CASE
            WHEN
                ((configurations.tncTimeStamp IS NULL)
                    AND (configurations.configuration_id = 'TNC_REFRESH_1'))
            THEN
                configurations.lastmodifiedts
            ELSE configurations.tncTimeStamp
        END) AS tncTimeStamp

         FROM [${dbxschemaname}].configurations 
            LEFT JOIN [${dbxschemaname}].configurationbundles ON configurationbundles.bundle_id = configurations.bundle_id;
GO

ALTER TABLE [${dbxschemaname}].[approvalmatrix]
ADD [currency] VARCHAR(32) NOT NULL DEFAULT 'USD';
ALTER TABLE [${dbxschemaname}].[approvalmatrixtemplate]
ADD [currency] VARCHAR(32) NOT NULL DEFAULT 'USD';

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
		set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex), ',', -1 ); 
		SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId and companyLegalUnit = @legalEntityId;
        IF @typeId = 'MONETARY' BEGIN			
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency) VALUES
			(@_contractId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency) VALUES
			(@_contractId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency) VALUES
			(@_contractId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency);
        END
        ELSE IF @typeId = 'NON_MONETARY' BEGIN
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix,currency) VALUES
            (@_contractId, @actionId, 'NON_MONETARY_LIMIT', @_cif, 'NO_APPROVAL', @isGroupMatrix,@_currency);
		END
	END 
END;  
MAINLABEL$leave: 
END;
GO




DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalmatrixtemplate_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalmatrixtemplate_proc](
    @_contractId NVARCHAR(50),
    @_cif NVARCHAR(50),
    @_limitTypeId NVARCHAR(50),
	@_actions NVARCHAR(MAX)
) AS
BEGIN 
	DECLARE @isGroupMatrix INT;
    DECLARE @legalEntityId NVARCHAR(255);

    IF @_cif = '' BEGIN
    SET @_cif = '%';
    END 

    SET @legalEntityId = (SELECT companyLegalUnit from contractcorecustomers where coreCustomerId = @_cif and contractId = @_contractId);
    
    IF @_limitTypeId = '' BEGIN
    SET @_limitTypeId = '%';
    END 
    
    select @isGroupMatrix = isGroupLevel from approvalmode where contractId = @_contractId AND coreCustomerId = @_cif;
	IF @isGroupMatrix IS NULL BEGIN
    SET @isGroupMatrix = 0;
	END

    IF @isGroupMatrix = 0 BEGIN
		SELECT 
			[approvalmatrixtemplate].[id],
			[approvalmatrixtemplate].[contractId],
			 [approvalmatrixtemplate].[limitTypeId],
			 [featureAction].[id] AS [actionId],
			 [featureAction].[name] AS [actionName],
			 [featureAction].[description] AS [actionDescription],
			 [featureAction].[Feature_id] AS [featureId],
			 [featureAction].[Type_id] AS [actionType],
			 [featureAction].[isAccountLevel] AS [isAccountLevel],
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
			 [approvalmatrixtemplate].[currency],
			 [customer].[id] AS [customerId],
			 [customer].[FirstName] AS [firstName],
			 [customer].[LastName] AS [lastName],
			 [contractcorecustomers].[coreCustomerId] AS [cifId],
			 [contractcorecustomers].[coreCustomerName] AS [cifName],
			 [approvalmatrixtemplate].[invalid],
			 [approvalmatrixtemplate].[isGroupMatrix]
			FROM ((((((([${dbxschemaname}].[approvalmatrixtemplate] AS [approvalmatrixtemplate]
			LEFT JOIN
			[${dbxschemaname}].[customerapprovalmatrixtemplate] AS [customerapprovalmatrixtemplate]
			ON [approvalmatrixtemplate].[id] = [customerapprovalmatrixtemplate].[approvalMatrixId])
			LEFT JOIN
			[${dbxschemaname}].[customer] AS [customer]
			ON [customerapprovalmatrixtemplate].[customerId] = [customer].[id])
			LEFT JOIN
			[${dbxschemaname}].[featureaction] AS [featureAction]
			ON [approvalmatrixtemplate].[actionId] = [featureAction].[id])
			LEFT JOIN
			[${dbxschemaname}].[approvalrule] AS [approvalRule]
			ON [approvalmatrixtemplate].[approvalruleId] = [approvalRule].[id]) 
		  LEFT JOIN
			[${dbxschemaname}].[feature] AS [feature]
			ON [featureAction].[Feature_id] = [feature].[id])
		  LEFT JOIN
			[${dbxschemaname}].[contractfeatures] AS [contractfeatures]
			ON [feature].[id] = [contractfeatures].[featureId]
			  and  [approvalmatrixtemplate].[contractId] = [contractfeatures].[contractId]
			  and [approvalmatrixtemplate].[coreCustomerId] = [contractfeatures].[coreCustomerId])
		  LEFT JOIN
			[${dbxschemaname}].[contractcorecustomers] AS [contractcorecustomers]
			ON [approvalmatrixtemplate].[contractId]  = [contractcorecustomers].[contractId] AND [approvalmatrixtemplate].[coreCustomerId] = [contractcorecustomers].[coreCustomerId])           
		WHERE 
		[approvalmatrixtemplate].[contractId] = @_contractId AND
		[approvalmatrixtemplate].[coreCustomerId] LIKE @_cif AND
		[${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[actionId],@_actions) > 0 AND 
		[approvalmatrixtemplate].[limitTypeId] LIKE @_limitTypeId AND 
		[approvalmatrixtemplate].[softdeleteflag] = 0 AND
		 [featureAction].[approveFeatureAction] is not null AND
		 [featureAction].[approveFeatureAction] != '' AND
		[featureAction].[status] = 'SID_ACTION_ACTIVE' AND
        [featureAction].[companyLegalUnit] = @legalEntityId AND
        [feature].[companyLegalUnit] = @legalEntityId
			ORDER BY [approvalmatrixtemplate].[contractId],[approvalmatrixtemplate].[coreCustomerId],[approvalmatrixtemplate].[limitTypeId],[approvalmatrixtemplate].[actionId] ,[approvalmatrixtemplate].[lowerlimit];
	
    END
    ELSE IF @isGroupMatrix = 1 BEGIN
		-- SQLINES LICENSE FOR EVALUATION USE ONLY
		SELECT 
			[approvalmatrixtemplate].[id],
			[approvalmatrixtemplate].[contractId],
			 [approvalmatrixtemplate].[limitTypeId],
			 [featureAction].[id] AS [actionId],
			 [featureAction].[name] AS [actionName],
			 [featureAction].[description] AS [actionDescription],
			 [featureAction].[Feature_id] AS [featureId],
			 [featureAction].[Type_id] AS [actionType],
			 [featureAction].[isAccountLevel] AS [isAccountLevel],
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
			 [approvalmatrixtemplate].[currency],
			 [signatorygroupmatrixtemplate].[groupList] AS [groupList],
			 [signatorygroupmatrixtemplate].[groupRule] AS [groupRule],
			 [contractcorecustomers].[coreCustomerId] AS [cifId],
			 [contractcorecustomers].[coreCustomerName] AS [cifName],
			 [approvalmatrixtemplate].[invalid],
			 [approvalmatrixtemplate].[isGroupMatrix]
			FROM (((((([${dbxschemaname}].[approvalmatrixtemplate] AS [approvalmatrixtemplate]
			LEFT JOIN
			[${dbxschemaname}].[signatorygroupmatrixtemplate] AS [signatorygroupmatrixtemplate]
			ON [approvalmatrixtemplate].[id] = [signatorygroupmatrixtemplate].[approvalMatrixId])
			LEFT JOIN
			[${dbxschemaname}].[featureaction] AS [featureAction]
			ON [approvalmatrixtemplate].[actionId] = [featureAction].[id])
			LEFT JOIN
			[${dbxschemaname}].[approvalrule] AS [approvalRule]
			ON [approvalmatrixtemplate].[approvalruleId] = [approvalRule].[id]) 
		  LEFT JOIN
			[${dbxschemaname}].[feature] AS [feature]
			ON [featureAction].[Feature_id] = [feature].[id])
		  LEFT JOIN
			[${dbxschemaname}].[contractfeatures] AS [contractfeatures]
			ON [feature].[id] = [contractfeatures].[featureId]
			  and  [approvalmatrixtemplate].[contractId] = [contractfeatures].[contractId]
			  and [approvalmatrixtemplate].[coreCustomerId] = [contractfeatures].[coreCustomerId])
		  LEFT JOIN
			[${dbxschemaname}].[contractcorecustomers] AS [contractcorecustomers]
			ON [approvalmatrixtemplate].[contractId]  = [contractcorecustomers].[contractId] AND [approvalmatrixtemplate].[coreCustomerId] = [contractcorecustomers].[coreCustomerId])           
		WHERE 
		[approvalmatrixtemplate].[contractId] = @_contractId AND
		[approvalmatrixtemplate].[coreCustomerId] LIKE @_cif AND 
		[approvalmatrixtemplate].[isGroupMatrix] = 1 AND
		[${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[actionId],@_actions) > 0 AND 
		[approvalmatrixtemplate].[limitTypeId] LIKE @_limitTypeId AND 
		[approvalmatrixtemplate].[softdeleteflag] = 0 AND
		 [featureAction].[approveFeatureAction] is not null AND
		 [featureAction].[approveFeatureAction] != '' AND
		[featureAction].[status] = 'SID_ACTION_ACTIVE' AND
        [featureAction].[companyLegalUnit] = @legalEntityId AND
        [feature].[companyLegalUnit] = @legalEntityId
			ORDER BY [approvalmatrixtemplate].[contractId],[approvalmatrixtemplate].[coreCustomerId],[approvalmatrixtemplate].[limitTypeId],[approvalmatrixtemplate].[actionId] ,[approvalmatrixtemplate].[lowerlimit];

    END 
END
GO



DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_create_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_create_proc]  
   @_matrixValues nvarchar(max),
   @_approverIds nvarchar(max)
AS 
   BEGIN

      SET  NOCOUNT  ON

      DECLARE @index1 int = 0
		DECLARE @length bigint
      DECLARE @index2 int = 0
      DECLARE @id int
      DECLARE @customerIds nvarchar(max)
      DECLARE @customerIdsComma nvarchar(max)
      DECLARE @length2 int
      DECLARE @customerId nvarchar(100)
      DECLARE @matrixRecord nvarchar(max)
      DECLARE @matrixComma varchar(max)
      DECLARE @query nvarchar(max)

      SET @length = LEN(@_matrixValues) - LEN(replace(@_matrixValues, ',', '')) + 1

      WHILE (1 = 1)
         BEGIN
            SET @index1 = @index1 + 1
            IF @index1 = @length + 1
               BREAK
            ELSE 
               BEGIN
				  set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
                  SET @matrixComma = replace(@matrixRecord, ';', ',')
				  SET @matrixComma = replace(@matrixComma, '"', '''')
                  SET @query = ('
                     INSERT [${dbxschemaname}].approvalmatrix(
                        approvalmatrix.name,
                        approvalmatrix.contractId,
						approvalmatrix.coreCustomerId,
                        approvalmatrix.actionId,
                        approvalmatrix.accountId,
                        approvalmatrix.approvalruleId,
                        approvalmatrix.limitTypeId,
                        approvalmatrix.lowerlimit,
                        approvalmatrix.upperlimit,
                        approvalmatrix.currency
                     ) VALUES (') + (@matrixComma) + (')')
					
                  execute (@query)
                  SET @id = @@IDENTITY
				  
				  set @customerIds = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_approverIds, ',', @index1), ',', -1 );
				  IF  (@customerIds IS NOT NULL AND LEN(@customerIds) > 0)
				  BEGIN
					  SET @customerIdsComma = replace(@customerIds, ';', ',')
					  SET @length2 = LEN(@customerIdsComma) - LEN(replace(@customerIdsComma, ',', '')) + 1
					  SET @index2 = 0
					  WHILE (1 = 1)
						 BEGIN
							SET @index2 = @index2 + 1
							IF @index2 = @length2 + 1
							   BREAK
							ELSE 
							   BEGIN
								  set @customerId = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1 );
								  INSERT INTO [${dbxschemaname}].customerapprovalmatrix([${dbxschemaname}].customerapprovalmatrix.customerId, [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId)
									 VALUES (@customerId, @id)
								  CONTINUE
							   END
						 END
					END
                  CONTINUE
               END
         END
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_records_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_records_proc](
	@_contractId nvarchar(50),
	@_cif nvarchar(50),
	@_accountId nvarchar(50),
	@_limitTypeId nvarchar(50),
	@_actions nvarchar(max))
AS BEGIN

		SET XACT_ABORT ON
		IF @_cif = ''
		SET @_cif = '%'

		SET NOCOUNT ON
		IF @_accountId = ''
		SET @_accountId = '%'
		IF @_limitTypeId = ''
		SET @_limitTypeId = '%';

		SELECT
			approvalmatrix.id,
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			featureAction.id AS actionId,
			featureAction.name AS actionName,
			featureAction.description AS actionDescription,
			featureAction.Feature_id AS featureId,
			featureAction.Type_id AS actionType,
			featureAction.isAccountLevel AS isAccountLevel,
			feature.name AS featureName,
			feature.Status_id AS fifeaturestatus,
			approvalRule.id AS approvalruleId,
			approvalRule.numberOfApprovals,
			approvalRule.name AS approvalRuleName,
			approvalMatrix.lowerlimit,
			approvalMatrix.upperlimit,
			approvalMatrix.currency,
			customer.id AS customerId,
			customer.FirstName AS firstName,
			customer.LastName AS lastName,
			contractcorecustomers.coreCustomerId AS cifId,
			contractcorecustomers.coreCustomerName AS cifName,
			approvalMatrix.invalid,
			approvalMatrix.isGroupMatrix
		FROM ((((((([${dbxschemaname}].approvalmatrix AS approvalMatrix
			LEFT JOIN [${dbxschemaname}].customerapprovalmatrix AS customerApprovalMatrix
			ON approvalMatrix.id = customerApprovalMatrix.approvalMatrixId)
			LEFT JOIN [${dbxschemaname}].customer AS customer
			ON customerApprovalMatrix.customerId = customer.id)
			LEFT JOIN [${dbxschemaname}].featureaction AS featureAction
			ON approvalMatrix.actionId = featureAction.id)
			LEFT JOIN [${dbxschemaname}].approvalrule AS approvalRule
			ON approvalMatrix.approvalruleId = approvalRule.id)
			LEFT JOIN [${dbxschemaname}].feature AS feature
			ON featureAction.Feature_id = feature.id)
			LEFT JOIN [${dbxschemaname}].contractfeatures AS contractfeatures
			ON feature.id = contractfeatures.featureId and approvalMatrix.contractId = contractfeatures.contractId
			and approvalMatrix.coreCustomerId = contractfeatures.coreCustomerId)
			LEFT JOIN contractcorecustomers AS contractcorecustomers
			ON approvalMatrix.contractId = contractcorecustomers.contractId AND approvalMatrix.coreCustomerId = contractcorecustomers.coreCustomerId)
		WHERE
			approvalmatrix.contractId = @_contractId AND
			approvalmatrix.coreCustomerId LIKE @_cif AND
			approvalmatrix.accountId LIKE @_accountId AND
			[${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND
			approvalmatrix.limitTypeId LIKE @_limitTypeId AND
			approvalmatrix.softdeleteflag = 0 AND
			featureAction.approveFeatureAction is not null AND
			featureAction.approveFeatureAction != '' AND
			featureAction.status = 'SID_ACTION_ACTIVE'
		ORDER BY
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			approvalMatrix.actionId,
			approvalMatrix.lowerlimit
END;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc]
@_matrixValues NVARCHAR(max),
@_signatorymatrixValues NVARCHAR(max)
AS
BEGIN

DECLARE @index1 INTEGER = 0;
DECLARE @index2 INTEGER = 0;
DECLARE @length INTEGER = 0;
DECLARE @matrixRecord nvarchar(max);
DECLARE @matrixComma varchar(max);
DECLARE @query nvarchar(max);
DECLARE @sigValues nvarchar(max);
DECLARE @id nvarchar(max);
DECLARE @groupList nvarchar(max);
DECLARE @groupRule nvarchar(max);
set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;

WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1;
IF @index1 = @length + 1 
BREAK
ELSE
BEGIN
set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
set @matrixComma = REPLACE(@matrixRecord, ';', ',');
set @matrixComma = REPLACE(@matrixComma,'"','''');
set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.approvalruleId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit,approvalmatrix.currency) VALUES (',@matrixComma,');');
execute (@query);
set @sigValues = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_signatorymatrixValues, '#', @index1), '#', -1 );
SET @id = @@IDENTITY;
SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1 );
SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1 );
set @id = REPLACE(@id,'"','''');
set @groupList = REPLACE(@groupList,'"','''');
set @groupRule = REPLACE(@groupRule,'"','''');
set @query = ('INSERT INTO [${dbxschemaname}].signatorygroupmatrix(signatorygroupmatrix.approvalMatrixId,signatorygroupmatrix.groupList,signatorygroupmatrix.groupRule) VALUES (')+@id+','+''''+@groupList+''''+','+''''+@groupRule+''''+')';
execute (@query);
CONTINUE
END
END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrixtemplate_create_proc](
	@_matrixValues NVARCHAR(MAX), 
	@_matrixApprover NVARCHAR(MAX),
	@_isGroupMatrix INTEGER
) AS
BEGIN
	DECLARE @index1 INTEGER = 0;
	DECLARE @index2 INTEGER = 0;
	DECLARE @length BIGINT = 0;
	DECLARE @length2 INTEGER = 0;
	DECLARE @matrixRecord NVARCHAR(MAX);
	DECLARE @matrixComma NVARCHAR(MAX);
	DECLARE @query NVARCHAR(MAX);
	DECLARE @id NVARCHAR(50);
	DECLARE @customerIds NVARCHAR(255);
	DECLARE @customerIdsComma NVARCHAR(255);
	DECLARE @customerId NVARCHAR(50);
	DECLARE @sigValues NVARCHAR(255);
	DECLARE @groupList NVARCHAR(255);
	DECLARE @groupRule NVARCHAR(255);
	
	set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;
	getValues: WHILE 1=1 BEGIN
			set @index1 = @index1 + 1;
			IF @index1 = @length + 1 BEGIN 
				BREAK;
			end
			else begin
				set @matrixRecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1 );
				set @matrixComma = REPLACE(@matrixRecord, ';', ',');
				SET @matrixComma = replace(@matrixComma, '"', '''');
				set @query = concat('INSERT INTO [${dbxschemaname}].approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,currency,isGroupMatrix) VALUES (',@matrixComma,');');
				execute(@query);
				
				SET @id = @@IDENTITY
                
                IF @_isGroupMatrix = 0 BEGIN
					set @customerIds = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, ',', @index1), ',', -1 );
					IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0) BEGIN
						set @customerIdsComma = REPLACE(@customerIds, ';', ',');
							set @length2 = LEN(@customerIdsComma) - LEN(REPLACE(@customerIdsComma, ',', '')) + 1;
							set @index2 = 0;
							getCustomerIds: WHILE 1=1 BEGIN
								set @index2 = @index2 + 1;
								IF @index2 = @length2 + 1 BEGIN 
									BREAK;
								end
								else begin
									set @customerId = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1 );
									INSERT INTO [${dbxschemaname}].customerapprovalmatrixtemplate(customerId,approvalMatrixId) values (@customerId,@id);							
									CONTINUE
								END 
							END;
						END 
					CONTINUE
				END
				ELSE IF @_isGroupMatrix = 1 BEGIN
					set @sigValues = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, '#', @index1), '#', -1 );
					SET @id = @@IDENTITY
					SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1 );
					SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1 );
					INSERT INTO [${dbxschemaname}].signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
               END  
			END 
	END 
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_grouprecords_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_grouprecords_proc](
	@_contractId nvarchar(50),
	@_cif nvarchar(50),
	@_accountId nvarchar(50),
	@_limitTypeId nvarchar(50),
	@_actions nvarchar(max))
AS BEGIN

		SET XACT_ABORT ON
		IF @_cif = ''
		SET @_cif = '%'

		SET NOCOUNT ON
		IF @_accountId = ''
		SET @_accountId = '%'
		IF @_limitTypeId = ''
		SET @_limitTypeId = '%';

		SELECT
			approvalmatrix.id,
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			featureAction.id AS actionId,
			featureAction.name AS actionName,
			featureAction.description AS actionDescription,
			featureAction.Feature_id AS featureId,
			featureAction.Type_id AS actionType,
			featureAction.isAccountLevel AS isAccountLevel,
			feature.name AS featureName,
			feature.Status_id AS fifeaturestatus,
			approvalRule.id AS approvalruleId,
			approvalRule.numberOfApprovals,
			approvalRule.name AS approvalRuleName,
			approvalMatrix.lowerlimit,
			approvalMatrix.upperlimit,
			approvalMatrix.currency,
			signatoryGroupMatrix.groupList AS groupList,
		    signatoryGroupMatrix.groupRule AS groupRule,
			contractcorecustomers.coreCustomerId AS cifId,
			contractcorecustomers.coreCustomerName AS cifName,
			approvalMatrix.invalid,
			approvalMatrix.isGroupMatrix
		FROM (((((([${dbxschemaname}].approvalmatrix AS approvalMatrix
			LEFT JOIN [${dbxschemaname}].signatorygroupmatrix AS signatoryGroupMatrix
			ON approvalMatrix.id = signatoryGroupMatrix.approvalMatrixId)
			LEFT JOIN [${dbxschemaname}].featureaction AS featureAction
			ON approvalMatrix.actionId = featureAction.id)
			LEFT JOIN [${dbxschemaname}].approvalrule AS approvalRule
			ON approvalMatrix.approvalruleId = approvalRule.id)
			LEFT JOIN [${dbxschemaname}].feature AS feature
			ON featureAction.Feature_id = feature.id)
			LEFT JOIN [${dbxschemaname}].contractfeatures AS contractfeatures
			ON feature.id = contractfeatures.featureId and approvalMatrix.contractId = contractfeatures.contractId
			and approvalMatrix.coreCustomerId = contractfeatures.coreCustomerId)
			LEFT JOIN contractcorecustomers AS contractcorecustomers
			ON approvalMatrix.contractId = contractcorecustomers.contractId AND approvalMatrix.coreCustomerId = contractcorecustomers.coreCustomerId)
		WHERE
			approvalmatrix.contractId = @_contractId AND
			approvalmatrix.coreCustomerId LIKE @_cif AND
			approvalmatrix.isGroupMatrix = 1 AND
			approvalmatrix.accountId LIKE @_accountId AND
			[${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND
			approvalmatrix.limitTypeId LIKE @_limitTypeId AND
			approvalmatrix.softdeleteflag = 0 AND
			featureAction.approveFeatureAction is not null AND
			featureAction.approveFeatureAction != '' AND
			featureAction.status = 'SID_ACTION_ACTIVE'
		ORDER BY
			approvalMatrix.contractId,
			approvalMatrix.accountId,
			approvalMatrix.limitTypeId,
			approvalMatrix.actionId,
			approvalMatrix.lowerlimit
END;
GO






GO
USE [${dbxschemaname}];
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
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
		[rp].[companyLegalUnit]  AS [companyLegalUnit],
        [p].[Status_id] AS status,
        [p].[PermissionValue] AS PermissionValue,
        [p].[isComposite] AS isComposite,
        [rp].[Role_id] AS Role_id,
		[p].[softdeleteflag] AS softdeleteflag
    FROM
    rolepermission rp , permission p
    WHERE [p].[id] = [rp].[Permission_id] AND [p].[Status_id]='SID_ACTIVE' 
	and 
		 [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) > 0 ORDER BY [id];
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[group_features_actions_view_proc]
@_groupId nvarchar(50),
@_companyLegalUnit  nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)


SET @select_statement = 'SELECT ${dbxschemaname}.groupactionlimit.Group_id, ${dbxschemaname}.groupactionlimit.Action_id, ${dbxschemaname}.groupactionlimit.LimitType_id, ${dbxschemaname}.groupactionlimit.value, ${dbxschemaname}.groupactionlimit.id AS groupactionlimit_id,
${dbxschemaname}.groupactionlimit.softdeleteflag AS softdelete, ${dbxschemaname}.membergroup.Type_id, ${dbxschemaname}.membergroup.Name AS Group_name, ${dbxschemaname}.membergroup.Description AS Group_description, ${dbxschemaname}.featureaction.name AS Action_name,
${dbxschemaname}.featureaction.description AS Action_description, ${dbxschemaname}.featureaction.Type_id AS Action_Type_id, ${dbxschemaname}.featureaction.Feature_id, ${dbxschemaname}.featureaction.isMFAApplicable, ${dbxschemaname}.featureaction.isAccountLevel,
${dbxschemaname}.featureaction.isPrimary, ${dbxschemaname}.featureaction.DisplaySequence AS Action_displaysequence, ${dbxschemaname}.featureaction.dependency AS Action_dependency, ${dbxschemaname}.featureaction.status AS actionStatus,
${dbxschemaname}.accesspolicy.name AS accessPolicy, ${dbxschemaname}.featureaction.accesspolicyId, ${dbxschemaname}.featureaction.limitgroupId, ${dbxschemaname}.featureaction.companyLegalUnit AS companyLegalUnit, ${dbxschemaname}.limitgroup.name AS limitGroup, ${dbxschemaname}.actionlevel.name AS actionlevel, ${dbxschemaname}.featureaction.actionlevelId,
${dbxschemaname}.feature.name AS featureName, ${dbxschemaname}.feature.name AS Feature_name, ${dbxschemaname}.feature.description AS Feature_description, ${dbxschemaname}.feature.Type_id AS Feature_Type_id, ${dbxschemaname}.feature.Status_id AS Feature_Status_id,
${dbxschemaname}.feature.DisplaySequence AS Feature_displaysequence, ${dbxschemaname}.feature.isPrimary AS Feature_isPrimary
FROM ${dbxschemaname}.groupactionlimit LEFT OUTER JOIN
${dbxschemaname}.membergroup ON ${dbxschemaname}.membergroup.id = ${dbxschemaname}.groupactionlimit.Group_id LEFT OUTER JOIN
${dbxschemaname}.featureaction ON ${dbxschemaname}.featureaction.id = ${dbxschemaname}.groupactionlimit.Action_id LEFT OUTER JOIN
${dbxschemaname}.feature ON ${dbxschemaname}.feature.id = ${dbxschemaname}.featureaction.Feature_id LEFT OUTER JOIN
${dbxschemaname}.accesspolicy ON ${dbxschemaname}.featureaction.accesspolicyId = ${dbxschemaname}.accesspolicy.id LEFT OUTER JOIN
${dbxschemaname}.limitgroup ON ${dbxschemaname}.featureaction.limitgroupId = ${dbxschemaname}.limitgroup.id LEFT OUTER JOIN
${dbxschemaname}.actionlevel ON ${dbxschemaname}.featureaction.actionlevelId = ${dbxschemaname}.actionlevel.id';

if @_groupId is not null and len(@_groupId)>0
SET @select_statement = CONCAT (@select_statement ,' where ${dbxschemaname}.groupactionlimit.Group_id = ''', @_groupId,'''', 'and ${dbxschemaname}.featureaction.companyLegalUnit =''',@_companyLegalUnit ,''';');

exec(@select_statement);

END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[servicedefinition_defaultgroup_update_proc]
 @_serviceDefinitionId nvarchar(50),
 @_groupId nvarchar(50),
 @_isDefault nvarchar(50),
 @_companyLegalUnit nvarchar(50)
AS
BEGIN
IF (@_isDefault = '0') 
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '0' where [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId AND [${dbxschemaname}].groupservicedefinition.companyLegalUnit = @_companyLegalUnit;
ELSE
    BEGIN
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '0' where 
     [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.companyLegalUnit = @_companyLegalUnit;
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '1' where [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId AND [${dbxschemaname}].groupservicedefinition.companyLegalUnit = @_companyLegalUnit;    
    END
END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[get_feature_actions_view_proc]
@_roleTypeId nvarchar(50),
@_companyLegalUnit nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON

SET NOCOUNT ON
DECLARE @select_statement nvarchar(max)

SET @select_statement = 'SELECT 
        [${dbxschemaname}].[featureaction].[id] AS [actionId],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[notes] AS [notes],
        [${dbxschemaname}].[featureaction].[Type_id] AS [typeId],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaySequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[featureaction].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[featureactionroletype].[RoleType_id] AS [actionType],
        [${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
        [${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],        
        [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus],
        [${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[description] AS [featureDescription],
        [${dbxschemaname}].[feature].[Type_id] AS [featureType],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [featureGroup],
        [${dbxschemaname}].[feature].[DisplaySequence] AS featureDisplaySequence,
        [${dbxschemaname}].[feature].[isPrimary] AS [isFeaturePrimary],
        [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
        [${dbxschemaname}].[actiondisplaynamedescription].[Locale_id] AS [localeId],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[actionlimit].[LimitType_id] AS [limitTypeId],
        [${dbxschemaname}].[actionlimit].[value] AS [value],
        [${dbxschemaname}].[dependentactions_view].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[dependentactions_view].[featureName] AS [dependentFeatureName],
        [${dbxschemaname}].[dependentactions_view].[actionName] AS [dependentActionName],
		[${dbxschemaname}].[dependentactions_view].[featureId] AS [dependentFeatureId],
        [${dbxschemaname}].[termandcondition].[Code] AS [termsAndConditionCode],
        [${dbxschemaname}].[termandcondition].[Title] AS [termsAndConditionTitle],
        [${dbxschemaname}].[termandcondition].[Description] AS [termsAndConditionDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName]
    FROM
        ((((((((((([${dbxschemaname}].featureaction
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
            AND ([${dbxschemaname}].[feature].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[actiondisplaynamedescription] ON (([${dbxschemaname}].[actiondisplaynamedescription].[Action_id] = [${dbxschemaname}].[featureaction].[id])
            AND ([${dbxschemaname}].[actiondisplaynamedescription].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])
            AND ([${dbxschemaname}].[featureroletype].[companyLegalUnit] = [${dbxschemaname}].[feature].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[featureactionroletype].[Action_id])
            AND ([${dbxschemaname}].[featureaction].[companyLegalUnit] = [${dbxschemaname}].[featureactionroletype].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[termandcondition] ON (([${dbxschemaname}].[featureaction].[TermsAndConditions_id] = [${dbxschemaname}].[termandcondition].[id])))
        LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])))
        LEFT JOIN [${dbxschemaname}].[dependentactions_view] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions_view].[actionId])
            AND ([${dbxschemaname}].[feature].[companyLegalUnit] = [${dbxschemaname}].[dependentactions_view].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureactionroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])
            AND ([${dbxschemaname}].[actionlimit].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))';

if @_roleTypeId is not null and len(@_roleTypeId)>0
	SET @select_statement = CONCAT (@select_statement ,' where [${dbxschemaname}].featureaction.companyLegalUnit = ''', @_companyLegalUnit,''' and [${dbxschemaname}].featureroletype.RoleType_Id = ''', @_roleTypeId,''' and [${dbxschemaname}].featureactionroletype.RoleType_Id = ''',@_roleTypeId,''';');

exec(@select_statement);

END;
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
DECLARE @select_statement nvarchar(max)

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
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id) AND (isDefaultGroup = 1)) AS defaultRole,
(SELECT COUNT(DISTINCT featureId) AS Expr1
FROM ${dbxschemaname}.servicedefinition_features_actions_view
WHERE (${dbxschemaname}.servicedefinition.id = serviceDefinitionId) AND (softdelete = ''0'')) AS numberOfFeatures,
(SELECT COUNT(id) AS Expr1
FROM ${dbxschemaname}.contract
WHERE (servicedefinitionId = ${dbxschemaname}.servicedefinition.id)) AS numberOfContracts
FROM ${dbxschemaname}.servicedefinition';
SET @select_statement = CONCAT (@select_statement ,' where companyLegalUnit = ''', @_companyLegalUnit, '''');
if @_typeId is not null and len(@_typeId)>0
SET @select_statement = CONCAT (@select_statement ,' and serviceType = ''', @_typeId, '''');

exec(@select_statement);

END;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[rolepermission_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[rolepermission_view] AS
SELECT [${dbxschemaname}].role.Name AS Role_Name, [${dbxschemaname}].role.Description AS Role_Description, [${dbxschemaname}].role.Status_id AS Role_Status_id, [${dbxschemaname}].rolepermission.Role_id, [${dbxschemaname}].permission.id AS Permission_id, [${dbxschemaname}].permission.Type_id AS Permission_Type_id, 
             [${dbxschemaname}].permission.Status_id AS Permission_Status_id, [${dbxschemaname}].permission.DataType_id, [${dbxschemaname}].permission.Name AS Permission_Name, [${dbxschemaname}].permission.Description AS Permission_Description, [${dbxschemaname}].permission.isComposite AS Permission_isComposite, 
             [${dbxschemaname}].permission.PermissionValue,[${dbxschemaname}].role.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].permission.createdby AS Permission_createdby, [${dbxschemaname}].permission.modifiedby AS Permission_modifiedby, [${dbxschemaname}].permission.createdts AS Permission_createdts, [${dbxschemaname}].permission.lastmodifiedts AS Permission_lastmodifiedts, 
             [${dbxschemaname}].permission.synctimestamp AS Permission_synctimestamp, [${dbxschemaname}].permission.softdeleteflag AS Permission_softdeleteflag
FROM   [${dbxschemaname}].rolepermission INNER JOIN
             [${dbxschemaname}].permission ON [${dbxschemaname}].rolepermission.Permission_id = [${dbxschemaname}].permission.id INNER JOIN
             [${dbxschemaname}].role ON [${dbxschemaname}].role.id = [${dbxschemaname}].rolepermission.Role_id;
GO



ALTER TABLE [${dbxschemaname}].bbrequest ADD additionalMeta NVARCHAR(MAX) DEFAULT NULL;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bbrequest_updatestatus_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[bbrequest_updatestatus_proc]  
   @_requestId bigint,
   @_status varchar(50),
   @_additionalMeta nvarchar(max)
AS 
    BEGIN
   
        SET  XACT_ABORT  ON

        SET  NOCOUNT  ON

        UPDATE [${dbxschemaname}].bbrequest SET [${dbxschemaname}].bbrequest.[status] = @_status WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId;
        
        -- ADP-7058 update additonal meta data as well
        IF @_additionalMeta IS NOT NULL
            UPDATE [${dbxschemaname}].bbrequest SET [${dbxschemaname}].bbrequest.[additionalMeta] = @_additionalMeta WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId;
        
        SELECT
            [${dbxschemaname}].bbrequest.requestId, 
            [${dbxschemaname}].bbrequest.transactionId, 
            [${dbxschemaname}].bbrequest.featureActionId, 
            [${dbxschemaname}].bbrequest.createdby, 
            [${dbxschemaname}].bbrequest.companyId, 
            [${dbxschemaname}].bbrequest.requiredSets, 
            [${dbxschemaname}].bbrequest.receivedSets, 
            [${dbxschemaname}].bbrequest.createdts, 
            [${dbxschemaname}].bbrequest.status, 
            [${dbxschemaname}].bbrequest.softDelete, 
            [${dbxschemaname}].bbrequest.accountId,
            [${dbxschemaname}].bbrequest.additionalMeta
        FROM [${dbxschemaname}].bbrequest
        WHERE CAST([${dbxschemaname}].bbrequest.requestId as nvarchar(max)) = @_requestId

   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bbrequest_updatecounter_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[bbrequest_updatecounter_proc]  
    @_requestId bigint,
    @_counter int,
    @_additionalMeta nvarchar(max)
AS 
    BEGIN

        SET  XACT_ABORT  ON
        SET  NOCOUNT  ON

        UPDATE [${dbxschemaname}].bbrequest SET [${dbxschemaname}].bbrequest.receivedSets = [${dbxschemaname}].bbrequest.receivedSets + @_counter WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId

        -- ADP-7058 update additonal meta data as well
        IF @_additionalMeta IS NOT NULL
            UPDATE [${dbxschemaname}].bbrequest SET [${dbxschemaname}].bbrequest.[additionalMeta] = @_additionalMeta WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId;
        
        SELECT * FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].bbrequest.requestId = @_requestId
    END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bbrequest_updateadditionalmeta_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[bbrequest_updateadditionalmeta_proc](
    @_requestId NVARCHAR(256),
    @_additionalMeta NVARCHAR(MAX)
)
AS
BEGIN
    UPDATE [${dbxschemaname}].[bbrequest] set additionalMeta = @_additionalMeta WHERE requestId = @_requestId;
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc] 
go
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc] 
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(max),
   @_requestIds nvarchar(max),
   @_featureactionlist nvarchar(max)
AS
   BEGIN
     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @customerGroupIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @monetaryActions nvarchar(max)
     DECLARE @groupIds nvarchar(max)
     DECLARE @strLen nvarchar(20)
     DECLARE @reqIds nvarchar(max)
     DECLARE @SubStrLen nvarchar(20)
 
    
    SET @combinedIds = (select String_agg(customer.id, ',') from [${dbxschemaname}].customer where [${dbxschemaname}].customer.combinedUserId = @_customerId)
    IF @combinedIds is NULL     
        SET @combinedIds = @_customerId;
   ELSE
        SET @combinedIds =(@_customerId + ',' +@combinedIds);
   
    IF (@combinedIds IS NULL OR @combinedIds = '')
        SET @combinedIds = '';
    ELSE
        SET @combinedIds = @combinedIds;
 
    IF (@_transactionIds IS NULL OR @_transactionIds = '')
        SET @_transactionIds = '';
    ELSE
        SET @_transactionIds = @_transactionIds;
   
    IF (@_requestIds IS NULL OR @_requestIds = '')
        SET @_requestIds = '';
    ELSE
        SET @_requestIds = @_requestIds;
 
     IF (@_featureactionlist IS NULL OR @_featureactionlist = '')
        GOTO MAINLABEL$leave
                             
     SET @customerMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.customerId IN (select VALUE from STRING_SPLIT(@combinedIds, ',')) )
     IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
 
     SET @customerGroupIds = (SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)),',') FROM [${dbxschemaname}].customersignatorygroup WHERE customersignatorygroup.customerId IN (select VALUE FROM STRING_SPLIT(@combinedIds, ',')) );
     IF @customerGroupIds IS NULL
         SET @customerGroupIds = ''
     
     SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',')  FROM [${dbxschemaname}].bbactedrequest
                                WHERE bbactedrequest.createdby IN (SELECT VALUE FROM STRING_SPLIT(@combinedIds, ',')) AND bbactedrequest.action != 'Pending' AND bbactedrequest.softdeleteflag = 0 );
                                                           
     IF @alreadyApprovedIds IS NULL
         SET @alreadyApprovedIds = '';
    
    IF @customerMatrixIds = ''
        SET @approvalRequestIds = '';
    ELSE
        SET @approvalRequestIds =
            (SELECT String_agg(CAST(RQAMX.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix AS RQAMX
            INNER JOIN [${dbxschemaname}].approvalmatrix AS APMX ON ( RQAMX.approvalMatrixId = APMX.id and APMX.id IN (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ',')) )
            INNER JOIN [${dbxschemaname}].approvalrule AS APRL ON ( APMX.approvalruleId = APRL.id )
            WHERE RQAMX.isGroupRule = 0  AND
                  RQAMX.approvalMatrixId IN (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ',')) AND
                  RQAMX.requestId NOT IN (SELECT VALUE FROM STRING_SPLIT(@alreadyApprovedIds, ',')) AND
                  ((APRL.numberOfApprovals = -1 AND RQAMX.receivedApprovals < ( SELECT COUNT(DISTINCT(customerapprovalmatrix.customerId)) 
                  FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = RQAMX.approvalMatrixId)) OR
                  (APRL.numberOfApprovals != -1 AND RQAMX.receivedApprovals < APRL.numberOfApprovals)) 
            );
 
     IF @approvalRequestIds IS NULL
                              SET @approvalRequestIds = '';
     
      -- GROUP BASED APPROVAL Trxns
    SET @groupIds = @customerGroupIds;
                
               DECLARE @CurrentGroupId NVARCHAR(MAX);
               DECLARE @PendingGroupList NVARCHAR(MAX);
               SET @PendingGroupList = '';
                
    do_this: WHILE 1=1 BEGIN
        SET @strLen = LEN(@groupIds);
            SET @CurrentGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1);
            IF @CurrentGroupId != '' BEGIN
                SET @CurrentGroupId = '%' + @CurrentGroupId + '%';

                IF @PendingGroupList = ''
                    SET @PendingGroupList = ' signatorygrouprequestmatrix.pendingGroupList LIKE ''' + @CurrentGroupId + ''''
                ELSE
                    SET @PendingGroupList = @PendingGroupList + ' OR signatorygrouprequestmatrix.pendingGroupList LIKE ''' + @CurrentGroupId + ''''
            END
                             
        SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
        SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
        IF LEN(@groupIds) <= 0 BEGIN
          BREAK;
        END
    END;
              
    IF @PendingGroupList != '' 
    BEGIN
        DECLARE @sql_stmt NVARCHAR(MAX);
        SET @sql_stmt =  ' SELECT @reqIds_out = (String_agg(CAST(signatorygrouprequestmatrix.requestId as nvarchar(max)) , '',''))
            FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE signatorygrouprequestmatrix.isApproved = 0 AND ( ' + @PendingGroupList + ' ) ;';
        EXECUTE sp_executesql @sql_stmt, N'@reqIds_out NVARCHAR(MAX) OUTPUT', @reqIds_out = @reqIds OUTPUT
    END      
                                                            
    IF @reqIds IS NULL
        SET @reqIds = ''
            
    IF (@approvalRequestIds = '' OR @approvalRequestIds IS NULL)
        SET @approvalRequestIds = @reqIds;
    ELSE BEGIN
        IF @reqIds != ''
            SET @approvalRequestIds = CONCAT(@approvalRequestIds, ',', @reqIds);
    END
                                                                        
    SET @approvalRequestIds = [${dbxschemaname}].DISTINCT_VALUE(@approvalRequestIds); -- Remove duplicates
    DECLARE @requestIds nvarchar(max)
    DECLARE @companyRequestIds nvarchar(max)  
    DECLARE @query nvarchar(max)
              
    SET @query = ''
              
    IF (@_transactionIds != '') 
        BEGIN
            SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE featureaction.id IN (select value FROM STRING_SPLIT(@_featureactionlist, ',')) )
            
            IF @features IS NULL
                SET @features = ''
            
            IF @features != ''
                SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE featureaction.Feature_id IN (select value FROM STRING_SPLIT(@features, ',')) )
            
            IF @monetaryActions IS NULL
                SET @monetaryActions = ''
                        
            IF @monetaryActions != ''
                SET @query = ' bbrequest.transactionId IN ( select VALUE from STRING_SPLIT(''' + @_transactionIds + ''', '','')) AND bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '','')) '
        END
    ELSE BEGIN
        IF @_requestIds = '' BEGIN
                
            SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId )                          
            
            IF @companyId IS NULL
                SET @companyId = '';
            ELSE
                SET @companyId = [${dbxschemaname}].DISTINCT_VALUE(@companyId);
            
            SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE featureaction.id IN (select value FROM STRING_SPLIT(@_featureactionlist, ',')) )
            
            IF @features IS NULL
                SET @features = ''

            IF @features != ''
                SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE featureaction.Feature_id IN (select value FROM STRING_SPLIT(@features, ',')) )
                             
            IF @monetaryActions IS NULL
                SET @monetaryActions = ''
                                            
            IF @companyId != '' AND @monetaryActions != ''
                SET @query = ' bbrequest.companyId IN (SELECT value FROM STRING_SPLIT(''' + @companyId + ''', '','')) and bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '',''))'                                
     
        END
        ELSE BEGIN
            SET @requestIds = @_requestIds                    
            IF @requestIds != ''
                SET @query = ' bbrequest.requestId IN ( select VALUE from STRING_SPLIT(''' + @requestIds + ''', '','')) '
        END
    END                     
   
    DECLARE @select_statement nvarchar(max)
   
        IF @query != ''
            SET @select_statement = 
                'If(OBJECT_ID(''tempdb..#temp_customeraction'') Is Not Null) BEGIN
                Drop Table #temp_customeraction;
                DROP INDEX IF EXISTS idx_temp_customeraction ON #temp_customeraction;
                END;
 
                CREATE TABLE #temp_customeraction(
                id VARCHAR(65),
                Account_id VARCHAR(65),
                Action_id VARCHAR(65),
                companyId VARCHAR(40));
                             
                CREATE INDEX idx_temp_customeraction ON #temp_customeraction (Account_id, Action_id, companyId);
                             
                INSERT INTO #temp_customeraction
                SELECT
                customeraction.id,
                customeraction.Account_id,
                customeraction.Action_id,
                (customeraction.contractId + ''_'' + customeraction.coreCustomerId) as companyId
                from [${dbxschemaname}].customeraction
                where
                customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(''' + @combinedIds + ''', '','')) and
                customeraction.isAllowed = 1 AND
                customeraction.softdeleteflag = 0;
                                                                          
            SELECT
            bbrequest.requestId,
            bbrequest.transactionId,
            bbrequest.status,
            bbrequest.featureActionId,
            bbrequest.isGroupMatrix,
			bbrequest.additionalMeta,
                                            
            (CASE
            WHEN (select COUNT(1) FROM STRING_SPLIT(''' + @combinedIds + ''', '','') where value = bbrequest.createdby) > 0
            THEN ''true''
            ELSE ''false''
            END) as amICreator,
                                            
            (CASE
            WHEN
            ((select COUNT(1) FROM STRING_SPLIT(''' + @approvalRequestIds + ''', '','') WHERE VALUE = bbrequest.requestId) > 0) AND                                             
            ((select TOP 1 #temp_customeraction.id
            from #temp_customeraction
            where
            #temp_customeraction.Account_id = bbrequest.accountId AND
            #temp_customeraction.Action_id = (select featureaction.approveFeatureAction  from [${dbxschemaname}].featureaction 
                where featureaction.id = bbrequest.featureActionId) AND #temp_customeraction.companyId = bbrequest.companyId) IS NOT NULL)
            THEN ''true''
            ELSE ''false''
            END) as amIApprover,
                                            
            (CASE
            WHEN (select COUNT(1) FROM STRING_SPLIT(''' + @alreadyApprovedIds + ''', '','') WHERE VALUE = bbrequest.requestId) > 0
            THEN ''true''
            ELSE ''false''
            END) as actedByMeAlready,
                                            
        (select count(DISTINCT(createdby)) from
        [${dbxschemaname}].bbactedrequest where bbactedrequest.action = ''Approved'' AND 
        bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0)
        as receivedApprovals,
                                            
        null as requiredApprovals
                             
        FROM
        [${dbxschemaname}].bbrequest WHERE ' + @query ;
                     
    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
   
    exec(@select_statement)
 
End
MAINLABEL$leave:
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatory_eligible_users_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatory_eligible_users_proc](
	@_coreCustomerId NVARCHAR(128),
    @_contractId NVARCHAR(128)
)
AS
BEGIN
	SELECT DISTINCT
		[${dbxschemaname}].[customer].[id] AS [userId],
		[${dbxschemaname}].[customer].[isCombinedUser] AS [isCombinedUser],
		[${dbxschemaname}].[customer].[UserName] AS [userName],
		[${dbxschemaname}].[customer].[FirstName] AS [firstName],
		[${dbxschemaname}].[customer].[LastName] AS [lastName],
		[${dbxschemaname}].[membergroup].[Name] AS [role]
	FROM [${dbxschemaname}].[customer]
	LEFT JOIN [${dbxschemaname}].[customergroup] ON ([${dbxschemaname}].[customergroup].[Customer_id] = [${dbxschemaname}].[customer].[id])
	LEFT JOIN [${dbxschemaname}].[membergroup] ON ([${dbxschemaname}].[membergroup].[id] = [${dbxschemaname}].[customergroup].[Group_id] )
		WHERE [${dbxschemaname}].[customergroup].[coreCustomerId]=@_coreCustomerId  
		AND [${dbxschemaname}].[customer].[id] in (SELECT DISTINCT [${dbxschemaname}].[customeraction].[Customer_id] FROM [${dbxschemaname}].[customeraction]
			WHERE [${dbxschemaname}].[customeraction].[contractId]=@_contractId 
			AND [${dbxschemaname}].[customeraction].[coreCustomerId]=@_coreCustomerId AND [${dbxschemaname}].[customeraction].[softdeleteflag] = '0'
			AND [${dbxschemaname}].[customeraction].[Action_id] IN (SELECT DISTINCT [${dbxschemaname}].[featureaction].[id] from [${dbxschemaname}].[featureaction] WHERE ([${dbxschemaname}].[featureaction].[id] LIKE '%_APPROVE' OR [${dbxschemaname}].[featureaction].[id] LIKE '%_SELF_APPROVAL'))
			AND [${dbxschemaname}].[customeraction].[Customer_id] NOT IN 
				(SELECT [${dbxschemaname}].[customersignatorygroup].[customerId] FROM [${dbxschemaname}].[customersignatorygroup] WHERE [${dbxschemaname}].[customersignatorygroup].[softdeleteflag] = '0' AND [${dbxschemaname}].[customersignatorygroup].[signatoryGroupId] IN 
					(SELECT [${dbxschemaname}].[signatorygroup].[signatoryGroupId] FROM [${dbxschemaname}].[signatorygroup] WHERE [${dbxschemaname}].[signatorygroup].[softdeleteflag] = '0' AND [${dbxschemaname}].[signatorygroup].[contractId]=@_contractId AND [${dbxschemaname}].[signatorygroup].[coreCustomerId]=@_coreCustomerId)))
			ORDER BY [${dbxschemaname}].[customer].[id];
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[update_user_recent_currency];
GO
CREATE PROCEDURE [${dbxschemaname}].[update_user_recent_currency]
  @_customerId NVARCHAR(50),
  @_currencyCode NVARCHAR(10),
  @_legalEntityId NVARCHAR(50)
AS
BEGIN

DECLARE @rowWithGivenCustomerAndCurrency INT = 0;
DECLARE @lengthOfRecentCurrencies INT = 0;
DECLARE @recentCurrencyToDelete varchar(60);

SELECT @rowWithGivenCustomerAndCurrency = COUNT(*) FROM [${dbxschemaname}].[recentcurrencies] rc WHERE rc.customerId = @_customerId AND rc.quoteCurrencyCode = @_currencyCode;

SELECT @lengthOfRecentCurrencies = COUNT(*) FROM [${dbxschemaname}].[recentCurrencies] rc WHERE rc.customerId = @_customerId ;

SELECT TOP 1 @recentCurrencyToDelete = rc.id FROM [${dbxschemaname}].[recentcurrencies] rc WHERE rc.customerId = @_customerId ORDER BY rc.createdts ASC ;

IF @rowWithGivenCustomerAndCurrency >= 1 BEGIN
  DELETE FROM [${dbxschemaname}].[recentcurrencies]  WHERE recentcurrencies.customerId = @_customerId AND recentcurrencies.quoteCurrencyCode = @_currencyCode;
END
ELSE IF @lengthOfRecentCurrencies >= 5 BEGIN
  DELETE FROM [${dbxschemaname}].[recentcurrencies]  WHERE recentcurrencies.id = @recentCurrencyToDelete;
END 

INSERT INTO [${dbxschemaname}].[recentcurrencies] ([id], [customerId], [quoteCurrencyCode], [legalEntityId]) VALUES (concat( @_currencyCode, @_customerId ), @_customerId, @_currencyCode, @_legalEntityId);
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[GetExternalPayeesProc];
GO
CREATE PROCEDURE [${dbxschemaname}].[GetExternalPayeesProc]
  @userId varchar(500),
  @legalEntityId varchar(50)
AS
BEGIN

 SELECT * from internationalpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 1 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1 and ip.legalEntityId = @legalEntityId)

 UNION

 SELECT * from interbankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1 and ip.legalEntityId = @legalEntityId)

 UNION

 SELECT * from intrabankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id where isSameBankAccount = 1  and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1 and ip.legalEntityId = @legalEntityId);

END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkpayment_template_po_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[bulkpayment_template_po_create_proc](
	@_povalues NVARCHAR(MAX)
)
AS
	BEGIN
		DECLARE @index1 INT;
		DECLARE @numOfPos INT;
		DECLARE @posData NVARCHAR(MAX);
		DECLARE @query NVARCHAR(MAX);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		SET @index1 = 0;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO [${dbxschemaname}].bulkpaymenttemplatepos(paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,featureActionId,companyId,roleId,status,createdby,beneficiaryName,paymentMethod,currency,amount,feesPaidBy,paymentReference,swift,beneficiaryNickName,beneficiaryAddress,accType,beneficiaryType,addToExistingFlag,beneficiaryIBAN,bankName,legalEntityId)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[bulkpayment_request_po_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[bulkpayment_request_po_create_proc](
	@_povalues NVARCHAR(MAX)
)
AS
	BEGIN
		DECLARE @index1 INT;
		DECLARE @numOfPos INT;
		DECLARE @posData NVARCHAR(MAX);
		DECLARE @query NVARCHAR(MAX);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		SET @index1 =0;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO [${dbxschemaname}].bulkpaymentrequestpos(paymentrequestPOId,paymentrequestId,paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,bankName,swift,featureActionId,companyId,roleId,status,currency,amount,feesPaidBy,paymentReference,debitAccountIBAN,beneficiaryIBAN,beneficiaryName,beneficiaryNickName,beneficiaryAddress,accountWithBankBIC,customer,paymentMethod,accType,createdby,legalEntityId)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[GetExternalPayeesProc];
GO
CREATE PROCEDURE [${dbxschemaname}].[GetExternalPayeesProc]
  @userId varchar(500),
  @legalEntityId varchar(50)
AS
BEGIN
IF (@legalEntityId != '')
 SELECT * from internationalpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 1 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1) and ip.legalEntityId = @legalEntityId

 UNION

 SELECT * from interbankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1) and ip.legalEntityId = @legalEntityId

 UNION

 SELECT * from intrabankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id where isSameBankAccount = 1  and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1) and ip.legalEntityId = @legalEntityId;
 
 ELSE
 SELECT * from internationalpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 1 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTERNATIONAL_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1)

 UNION

 SELECT * from interbankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id  where isInternationalAccount = 0 and isSameBankAccount = 0 and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and  Action_id = 'INTER_BANK_ACCOUNT_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1)

 UNION

 SELECT * from intrabankpayee  as ip inner join externalaccount as ea on ip.payeeId=ea.Id where isSameBankAccount = 1  and softDelete = 0 and
 cif in (SELECT coreCustomerId from customeraction where  Customer_id = @userId and Action_id = 'INTRA_BANK_FUND_TRANSFER_VIEW_RECEPIENT' and isAllowed = 1);

END
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
		[rp].[companyLegalUnit]  AS [companyLegalUnit],
        [p].[Status_id] AS status,
        [p].[PermissionValue] AS PermissionValue,
        [p].[isComposite] AS isComposite,
        [rp].[Role_id] AS Role_id,
		[p].[softdeleteflag] AS softdeleteflag
    FROM
    (rolepermission rp
	JOIN permission p ON ([p].[id] = [rp].[Permission_id])
    JOIN role r ON (r.id = rp.Role_id))
    WHERE [p].[Status_id]='SID_ACTIVE' AND [r].[Status_id]='SID_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) > 0 ORDER BY [id];
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatorygroup_for_customer_and_request_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroup_for_customer_and_request_proc](
    @_requestId NVARCHAR(256),
    @_customerId NVARCHAR(256)
)
AS
BEGIN
	SET  XACT_ABORT  ON
    SET  NOCOUNT  ON

    DECLARE @signatoryGroupsList NVARCHAR(1024);
    DECLARE @signatoryGroupsQuery NVARCHAR(2048);
    SET @signatoryGroupsList = (SELECT STRING_AGG(REPLACE((REPLACE(REPLACE(groupList, ']', ''''),'[', '''')), ',', ''','''), ',') FROM [${dbxschemaname}].[signatorygroupmatrix] WHERE approvalMatrixId IN (SELECT approvalMatrixId FROM [${dbxschemaname}].[requestapprovalmatrix] WHERE requestId = @_requestId));
    SET @signatoryGroupsQuery = CONCAT('SELECT 
                    csg.customerSignatoryGroupId,
                    csg.signatoryGroupId,
                    sg.signatoryGroupName,
                    csg.customerId,
                    csg.createdby,
                    csg.createdts,
                    csg.modifiedby,
                    csg.lastmodifiedts,
                    csg.synctimestamp,
                    csg.softdeleteflag FROM 
                    [${dbxschemaname}].[customersignatorygroup] csg 
                    INNER JOIN [${dbxschemaname}].[signatorygroup] sg 
                    ON csg.signatoryGroupId = sg.signatoryGroupId 
                        WHERE csg.signatoryGroupId IN (', @signatoryGroupsList,') 
                        AND csg.softdeleteflag = 0 
                        AND sg.softdeleteflag = 0 
                        AND csg.customerId = ''', @_customerId, ''';');
    EXEC(@signatoryGroupsQuery);
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[verify_user_proc];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[verify_user_proc]
   @_phone nvarchar(max),
   @_email nvarchar(max),
   @_dateOfBirth nvarchar(max),
   @_legalEntityId nvarchar(max),
   @_backendIdentifiers nvarchar(max),
   @_backendType nvarchar(max)
AS
   BEGIN
   
   SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
   DECLARE
         @usersList nvarchar(max) = N''
    SET @usersList =
                     (
                        SELECT String_agg(CAST(backendidentifier.Customer_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].backendidentifier WHERE
                               [${dbxschemaname}].backendidentifier.BackendType = @_backendType AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].backendidentifier.BackendId ,@_backendIdentifiers) = '1'
                     )
     
    SELECT
        customer.id AS id,
        customer.FirstName AS FirstName,
        customer.MiddleName AS MiddleName,
        customer.LastName AS LastName,
        customer.UserName AS UserName,
        customer.Gender AS Gender,
        customer.DateOfBirth AS DateOfBirth,
		customer.companyLegalUnit AS companyLegalUnit,
        customer.Ssn AS Ssn,
        customer.Status_id AS Status_id,
      customer.CustomerType_id AS CustomerType_id
    FROM
        ([${dbxschemaname}].customer
        LEFT JOIN [${dbxschemaname}].customercommunication primaryphone ON ((primaryphone.Customer_id = customer.id)
            AND (primaryphone.Value = @_phone)
            AND (primaryphone.Type_id = 'COMM_TYPE_PHONE'))
        LEFT JOIN [${dbxschemaname}].customercommunication primaryemail ON ((primaryemail.Customer_id = customer.id)
            AND (primaryemail.Value = @_email)
            AND (primaryemail.Type_id = 'COMM_TYPE_EMAIL')))
   where
      [${dbxschemaname}].customer.DateOfBirth = @_dateOfBirth
	  and customer.companyLegalUnit = @_legalEntityId
      and primaryphone.Customer_id = primaryemail.Customer_id  UNION
       SELECT
         customer.id AS id,
        customer.FirstName AS FirstName,
        customer.MiddleName AS MiddleName,
        customer.LastName AS LastName,
        customer.UserName AS UserName,
        customer.Gender AS Gender,
        customer.DateOfBirth AS DateOfBirth,
		customer.companyLegalUnit AS companyLegalUnit,
        customer.Ssn AS Ssn,
        customer.Status_id AS Status_id,
      customer.CustomerType_id AS CustomerType_id
    FROM [${dbxschemaname}].customer where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id ,@usersList) = '1' and [${dbxschemaname}].customer.companyLegalUnit = @_legalEntityId;
     
   END
GO

ALTER TABLE [${dbxschemaname}].[application] ADD [isSingleEntity] bit NOT NULL DEFAULT 0;
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] DROP CONSTRAINT [userrolecustomerrole$userrolecustomerrole_userrole_id];
GO
ALTER TABLE [${dbxschemaname}].[userrole] DROP CONSTRAINT [userrole$FK_UserRole_Role];
GO
ALTER TABLE [${dbxschemaname}].[rolepermissionou] DROP CONSTRAINT [FK_rolepermissionou_Role];
GO
ALTER TABLE [${dbxschemaname}].[rolepermission] DROP CONSTRAINT [rolepermission$FK_RolePermission_Role];
GO
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] DROP CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_Role];
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] DROP CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_Role];
GO
ALTER TABLE [${dbxschemaname}].[role] DROP CONSTRAINT [role$FK_Role_Role];
GO
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] DROP CONSTRAINT [userroleservicedefinition$userroleservicedefinition_userrole_id];
GO

ALTER TABLE [${dbxschemaname}].[role] DROP CONSTRAINT [PK_role_id];
GO
ALTER TABLE [${dbxschemaname}].[role] ADD CONSTRAINT [PK_role_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[companyLegalUnit] ASC
);
GO

ALTER TABLE [${dbxschemaname}].[role]  WITH NOCHECK ADD  CONSTRAINT [role$FK_Role_Role] FOREIGN KEY([Parent_id],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id],[companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[role] CHECK CONSTRAINT [role$FK_Role_Role];


ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] DROP CONSTRAINT [PK_userroleservicedefinition_UserRole_id];
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] ADD CONSTRAINT [PK_userroleservicedefinition_UserRole_id] PRIMARY KEY CLUSTERED 
(
	[UserRole_id] ASC,
	[servicedefinitionId] ASC,
	[companyLegalUnit] ASC
);
GO

ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] WITH NOCHECK ADD CONSTRAINT [userroleservicedefinition$userroleservicedefinition_userrole_id] FOREIGN KEY([UserRole_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[userroleservicedefinition] CHECK CONSTRAINT [userroleservicedefinition$userroleservicedefinition_userrole_id];
GO

ALTER TABLE [${dbxschemaname}].[rolecompositeaction] DROP CONSTRAINT [PK_rolecompositeaction_Role_id];
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD CONSTRAINT [PK_rolecompositeaction_Role_id] PRIMARY KEY CLUSTERED 
(
	[Role_id] ASC,
	[CompositeAction_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[rolecompositeaction]  WITH NOCHECK ADD CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_Role] FOREIGN KEY([Role_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] CHECK CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_Role];

ALTER TABLE [${dbxschemaname}].[rolecompositepermission] DROP CONSTRAINT [PK_rolecompositepermission_Role_id];
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] ADD CONSTRAINT [PK_rolecompositepermission_Role_id] PRIMARY KEY CLUSTERED
(
    [Role_id] ASC,
    [CompositePermission_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[rolecompositepermission]  WITH NOCHECK ADD CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_Role] FOREIGN KEY([Role_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[rolecompositepermission] CHECK CONSTRAINT [rolecompositepermission$FK_RoleCompositePermission_Role];

ALTER TABLE [${dbxschemaname}].[rolepermission] DROP CONSTRAINT [PK_rolepermission_Role_id];
ALTER TABLE [${dbxschemaname}].[rolepermission] ADD CONSTRAINT [PK_rolepermission_Role_id] PRIMARY KEY CLUSTERED 
(
	[Role_id] ASC,
	[Permission_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[rolepermission] WITH NOCHECK ADD CONSTRAINT [rolepermission$FK_RolePermission_Role] FOREIGN KEY([Role_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[rolepermission] CHECK CONSTRAINT [rolepermission$FK_RolePermission_Role];


ALTER TABLE [${dbxschemaname}].[rolepermissionou] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
DECLARE @constraint varchar(255);
SELECT @constraint = name  FROM sys.key_constraints WHERE type = 'PK' AND OBJECT_NAME(parent_object_id) = N'rolepermissionou';
DECLARE @sql nvarchar(max);
SET @sql='ALTER TABLE [${dbxschemaname}].[rolepermissionou] DROP CONSTRAINT '+ @constraint;
EXEC(@SQL);
GO

ALTER TABLE [${dbxschemaname}].[rolepermissionou] ADD CONSTRAINT [PK_rolepermissionou_Role_id] PRIMARY KEY (
    [roleId],
    [permissionId],
    [ouId],
    [companyLegalUnit]
);

ALTER TABLE [${dbxschemaname}].[rolepermissionou] ADD CONSTRAINT [FK_rolepermissionou_Role] FOREIGN KEY ([roleId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]) ON DELETE CASCADE ON UPDATE CASCADE;


ALTER TABLE [${dbxschemaname}].[userrole] DROP CONSTRAINT [PK_userrole_User_id];
ALTER TABLE [${dbxschemaname}].[userrole] ADD CONSTRAINT [PK_userrole_User_id] PRIMARY KEY CLUSTERED
(
	[User_id] ASC,
	[Role_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[userrole] WITH NOCHECK ADD CONSTRAINT [userrole$FK_UserRole_Role] FOREIGN KEY([Role_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
ALTER TABLE [${dbxschemaname}].[userrole] CHECK CONSTRAINT [userrole$FK_UserRole_Role];


ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] DROP CONSTRAINT [PK_userrolecustomerrole_UserRole_id];
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] ADD CONSTRAINT [PK_userrolecustomerrole_UserRole_id] PRIMARY KEY CLUSTERED 
(
	[UserRole_id] ASC,
	[CustomerRole_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] WITH NOCHECK ADD CONSTRAINT [userrolecustomerrole$userrolecustomerrole_userrole_id] FOREIGN KEY([UserRole_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[role] ([id], [companyLegalUnit]);
GO
ALTER TABLE [${dbxschemaname}].[userrolecustomerrole] CHECK CONSTRAINT [userrolecustomerrole$userrolecustomerrole_userrole_id];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolePermissionDelete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolePermissionDelete_proc] 
 @_roleId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000),
 @_companyLegalUnit VARCHAR(1000)
AS 
   BEGIN
    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
	DECLARE @caid VARCHAR(50);
	DECLARE @isEnabled VARCHAR(10);
	DECLARE @finished INTEGER = 0 ;
	DECLARE @caid_count INTEGER = 0 ;
	DECLARE caids CURSOR FOR (SELECT c.id, c.isEnabled FROM [${dbxschemaname}].[compositeaction] c WHERE [${dbxschemaname}].FIND_IN_SET([Permission_id], @_PermissionIds) > 0);
	
	OPEN caids; 
	MANAGECAIDS:
	WHILE 1=1
	BEGIN
	FETCH caids INTO @caid, @isEnabled;
	IF @@FETCH_STATUS <> 0 BEGIN 
		 GOTO MANAGECAIDS$LEAVE
	END 
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  [${dbxschemaname}].[rolepermission] rp WHERE rp.Role_id= @_roleId AND rp.companyLegalUnit = @_companyLegalUnit
	AND rp.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(rp.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	DELETE FROM [${dbxschemaname}].[rolecompositeaction] WHERE Role_id=@_roleId AND companyLegalUnit = @_companyLegalUnit AND CompositeAction_id=@caid;
	END 
	END
	MANAGECAIDS$LEAVE:
	CLOSE caids
	DEALLOCATE caids;
	DELETE FROM [${dbxschemaname}].[rolepermission] WHERE Role_id=@_roleId AND companyLegalUnit = @_companyLegalUnit AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
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
WHERE (serviceDefinitionId = ${dbxschemaname}.servicedefinition.id) AND (isDefaultGroup = 1)) AS defaultRole,
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


DROP VIEW IF EXISTS [${dbxschemaname}].[internal_role_to_serviceDefinition_mapping_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[internal_role_to_serviceDefinition_mapping_view] AS
SELECT [${dbxschemaname}].servicedefinition.id AS ServiceDefinition_id, 
	   [${dbxschemaname}].servicedefinition.name AS ServiceDefinition_Name, 
	   [${dbxschemaname}].servicedefinition.description AS ServiceDefinition_Description, 
	   [${dbxschemaname}].servicedefinition.serviceType AS ServiceDefinition_Type_id, 
       [${dbxschemaname}].servicedefinition.status AS ServiceDefinition_Status_id, 
	   [${dbxschemaname}].role.id AS InternalRole_id, 
	   [${dbxschemaname}].role.Type_id AS InternalRole_Type_id, 
	   [${dbxschemaname}].role.Status_id AS InternalRole_Status_id, 
	   [${dbxschemaname}].role.Name AS InternalRole_Name, 
       [${dbxschemaname}].role.Description AS InternalRole_Description, 
	   [${dbxschemaname}].role.companyLegalUnit AS companyLegalUnit
FROM   [${dbxschemaname}].userroleservicedefinition LEFT OUTER JOIN
             [${dbxschemaname}].servicedefinition ON [${dbxschemaname}].servicedefinition.id = [${dbxschemaname}].userroleservicedefinition.servicedefinitionId 
			 LEFT OUTER JOIN [${dbxschemaname}].role ON ([${dbxschemaname}].role.id = [${dbxschemaname}].userroleservicedefinition.UserRole_id 
			 AND [${dbxschemaname}].role.companyLegalUnit = [${dbxschemaname}].userroleservicedefinition.companyLegalUnit);
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[roles_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[roles_view] AS
SELECT [${dbxschemaname}].role.id AS role_id, [${dbxschemaname}].role.Type_id AS roleType_id, [${dbxschemaname}].role.Name AS role_Name, [${dbxschemaname}].role.Description AS role_Desc, [${dbxschemaname}].role.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].role.Status_id, [${dbxschemaname}].status.Description AS Status_Desc,
                 (SELECT COUNT(Role_id) AS Expr1
                 FROM    [${dbxschemaname}].rolepermission
                 WHERE ((Role_id = [${dbxschemaname}].role.id) AND (companyLegalUnit = [${dbxschemaname}].role.companyLegalUnit))) AS permission_Count,
                 (SELECT COUNT(User_id) AS Expr1
                 FROM    [${dbxschemaname}].userrole
                 WHERE (Role_id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].rolepermission
                                  WHERE (Role_id = [${dbxschemaname}].role.id))
                                  AND companyLegalUnit IN (SELECT companyLegalUnit
                                  FROM [${dbxschemaname}].rolepermission
                                  WHERE (companyLegalUnit = [${dbxschemaname}].role.companyLegalUnit)))) AS Users_Count,
                CASE (role.Status_id) WHEN N'SID_ACTIVE' THEN N'Active' ELSE N'Inactive' END AS Status
FROM   [${dbxschemaname}].role INNER JOIN
             [${dbxschemaname}].status ON [${dbxschemaname}].role.Status_id = [${dbxschemaname}].status.id;
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[rolepermission_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[rolepermission_view] AS
SELECT [${dbxschemaname}].role.Name AS Role_Name, 
       [${dbxschemaname}].role.Description AS Role_Description, 
       [${dbxschemaname}].role.Status_id AS Role_Status_id,
       [${dbxschemaname}].rolepermission.companyLegalUnit AS companyLegalUnit, 
       [${dbxschemaname}].rolepermission.Role_id, 
       [${dbxschemaname}].permission.id AS Permission_id, 
       [${dbxschemaname}].permission.Type_id AS Permission_Type_id, 
       [${dbxschemaname}].permission.Status_id AS Permission_Status_id, 
       [${dbxschemaname}].permission.DataType_id AS DataType_id, 
       [${dbxschemaname}].permission.Name AS Permission_Name, 
       [${dbxschemaname}].permission.Description AS Permission_Description, 
       [${dbxschemaname}].permission.isComposite AS Permission_isComposite, 
       [${dbxschemaname}].permission.PermissionValue AS PermissionValue,
       [${dbxschemaname}].permission.createdby AS Permission_createdby, 
       [${dbxschemaname}].permission.modifiedby AS Permission_modifiedby, 
       [${dbxschemaname}].permission.createdts AS Permission_createdts, 
       [${dbxschemaname}].permission.lastmodifiedts AS Permission_lastmodifiedts, 
       [${dbxschemaname}].permission.synctimestamp AS Permission_synctimestamp, 
       [${dbxschemaname}].permission.softdeleteflag AS Permission_softdeleteflag
FROM   [${dbxschemaname}].rolepermission 
       INNER JOIN [${dbxschemaname}].permission ON [${dbxschemaname}].rolepermission.Permission_id = [${dbxschemaname}].permission.id 
       INNER JOIN [${dbxschemaname}].role ON [${dbxschemaname}].role.id = [${dbxschemaname}].rolepermission.Role_id AND [${dbxschemaname}].role.companyLegalUnit = [${dbxschemaname}].rolepermission.companyLegalUnit;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[bulkassign_permissions_to_role]
GO

CREATE PROCEDURE [${dbxschemaname}].[bulkassign_permissions_to_role]  
    @_roleId nvarchar(50),
	@_permissions nvarchar(MAX),
	@_companyLegalUnit varchar(255)
AS 
   BEGIN
	DECLARE @index1 INT;
	DECLARE @numOfPermissions INT;
	DECLARE @permissionsData NVARCHAR(MAX);;
	DECLARE @query NVARCHAR(MAX);

	SET @numOfPermissions = LEN(@_permissions) - LEN(REPLACE(@_permissions, '|', '')) + 1;
	SET @index1 = 0;

    if(@numOfPermissions IS NULL or @numOfPermissions IS NULL)
	return;
    
	WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPermissions + 1
               BREAK
            ELSE 
               BEGIN
				SET @permissionsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_permissions, '|', @index1), '|', -1 );
				SET @query = ('insert into [${dbxschemaname}].rolepermission(role_id,permission_id,companyLegalUnit,softdeleteflag) 
			values (''') +@_roleId + (''',''') + @permissionsData + (''',''') + @_companyLegalUnit + (''',0);');
				
			EXEC(@query)
			END 
	END
   END
GO

ALTER TABLE [${dbxschemaname}].[compositeaction] DROP CONSTRAINT [compositeaction$FK_CompositeAction_Feature];
ALTER TABLE [${dbxschemaname}].[dependentactions] DROP CONSTRAINT [FK_dependentactions_feature];
ALTER TABLE [${dbxschemaname}].[featureroletype] DROP CONSTRAINT [featureroletype$FK_featureroletype_Feature_id];
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] DROP CONSTRAINT [featuredisplaynamedescription$FK_featuredisplaynamedescription_Feature_id];
ALTER TABLE [${dbxschemaname}].[featureaction] DROP CONSTRAINT [featureaction$FK_action_feature_id];
ALTER TABLE [${dbxschemaname}].[organisationfeatures] DROP CONSTRAINT [organisationfeatures$FK_featureId];

ALTER TABLE [${dbxschemaname}].[feature]
DROP CONSTRAINT [PK_feature_id];

ALTER TABLE [${dbxschemaname}].[feature] 
ADD CONSTRAINT [PK_feature_id] PRIMARY KEY CLUSTERED
(
	[id] ASC,
	[companyLegalUnit] ASC
);


ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription]
DROP CONSTRAINT [PK_featuredisplaynamedescription_Feature_id];
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription]
ADD CONSTRAINT [PK_featuredisplaynamedescription_Feature_id] PRIMARY KEY CLUSTERED
(
	[Feature_id] ASC,
	[Locale_id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] DROP CONSTRAINT [FK_bulkpaymentsubrecordmock_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[bulkpaymentrequestpos] DROP CONSTRAINT [FK_bulkpaymentrequestpos_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[compositeaction] DROP CONSTRAINT [compositeaction$FK_CompositeAction_Action];
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] DROP CONSTRAINT [FK_bulkpaymentrecordmock_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] DROP CONSTRAINT [FK_organisationactionlimit_action];
ALTER TABLE [${dbxschemaname}].[featureactionroletype] DROP CONSTRAINT [FK_featureactionroletype_Action_id];
ALTER TABLE [${dbxschemaname}].[groupactionlimit] DROP CONSTRAINT [FK_groupactionlimit_Action];
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] DROP CONSTRAINT [FK_actiondisplaynamedescription_Action_id];
ALTER TABLE [${dbxschemaname}].[bulkpaymentrequest] DROP CONSTRAINT [FK_bulkpaymentrequest_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[dependentactions] DROP CONSTRAINT [FK_dependentactions_dependentactionId];
ALTER TABLE [${dbxschemaname}].[dependentactions] DROP CONSTRAINT [FK_dependentactions_actionId];
ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] DROP CONSTRAINT [FK_bulkpaymentfilesmock_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] DROP CONSTRAINT [FK_bulkpaymenttemplatepos_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] DROP CONSTRAINT [FK_bulkpaymentsubrecord_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[customroleactionlimits] DROP CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_3];
ALTER TABLE [${dbxschemaname}].[approvalmatrix] DROP CONSTRAINT [approvalmatrix$FK_approvalmatrix_actionid];
ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit] DROP CONSTRAINT [FK_servicedefinitionactionlimit_action];
ALTER TABLE [${dbxschemaname}].[customeractionlimits] DROP CONSTRAINT [FK_CustomerActionLimits_Action];
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] DROP CONSTRAINT [FK_bulkpaymentrecord_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplate] DROP CONSTRAINT [FK_bulkpaymenttemplate_featureActionId_idx];
ALTER TABLE [${dbxschemaname}].[approvalmatrixtemplate] DROP CONSTRAINT [FK_approvalmatrixtemplate_actionid];
ALTER TABLE [${dbxschemaname}].[actionlimit] DROP CONSTRAINT [actionlimit$FK_actionlimit_featureaction];
ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] DROP CONSTRAINT [FK_bulkpaymentfiles_featureActionId_idx];

ALTER TABLE [${dbxschemaname}].[featureaction]
DROP CONSTRAINT [PK_featureaction_id];

ALTER TABLE [${dbxschemaname}].[featureaction]
ADD CONSTRAINT [PK_featureaction_id] PRIMARY KEY CLUSTERED
(
	[id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[featureaction]
DROP CONSTRAINT [featureaction$featureaction_unique_index];
ALTER TABLE [${dbxschemaname}].[featureaction]
ADD CONSTRAINT [featureaction$featureaction_unique_index] UNIQUE NONCLUSTERED
(
	[App_id] ASC,
	[id] ASC,
	[companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[actionlimit]
DROP CONSTRAINT [PK_actionlimit_Action_id];
ALTER TABLE [${dbxschemaname}].[actionlimit]
ADD CONSTRAINT [PK_actionlimit_Action_id] PRIMARY KEY CLUSTERED
(
    [Action_id] ASC,
    [LimitType_id] ASC,
    [companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
DROP CONSTRAINT [PK_actiondisplaynamedescription_Action_id];
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
ADD CONSTRAINT [PK_actiondisplaynamedescription_Action_id] PRIMARY KEY CLUSTERED
(
    [Action_id] ASC,
    [Locale_id] ASC,
    [companyLegalUnit] ASC
);


declare @constraint nvarchar(255);
SELECT @constraint = name  FROM sys.key_constraints  WHERE type = 'PK' AND OBJECT_NAME(parent_object_id) = N'dependentactions';
declare @sql nvarchar(max);
set @sql='ALTER TABLE [${dbxschemaname}].[dependentactions] DROP CONSTRAINT '+ @constraint ;
exec(@SQL);

ALTER TABLE [${dbxschemaname}].[dependentactions]
ADD CONSTRAINT [PK_dependentactions] PRIMARY KEY CLUSTERED
(
    [actionId] ASC,
    [dependentactionId] ASC,
    [companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[featureactionroletype]
DROP CONSTRAINT [PK_featureactionroletype_RoleType_id];
ALTER TABLE [${dbxschemaname}].[featureactionroletype]
ADD CONSTRAINT [PK_featureactionroletype_RoleType_id] PRIMARY KEY CLUSTERED
(
    [RoleType_id] ASC,
    [Action_id] ASC,
    [companyLegalUnit] ASC
);

ALTER TABLE [${dbxschemaname}].[featureroletype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[featureroletype]
DROP CONSTRAINT [PK_featureroletype_RoleType_id];
ALTER TABLE [${dbxschemaname}].[featureroletype]
ADD CONSTRAINT [PK_featureroletype_RoleType_id] PRIMARY KEY CLUSTERED
(
    [RoleType_id] ASC,
    [Feature_id] ASC,
    [companyLegalUnit] ASC
);
GO

ALTER TABLE [${dbxschemaname}].[compositeaction] WITH NOCHECK ADD CONSTRAINT [compositeaction$FK_CompositeAction_Feature] 
FOREIGN KEY([Feature_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit]);
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] CHECK CONSTRAINT [compositeaction$FK_CompositeAction_Feature];
GO

ALTER TABLE [${dbxschemaname}].[compositeaction] WITH NOCHECK ADD  CONSTRAINT [compositeaction$FK_CompositeAction_Action] FOREIGN KEY([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit]);
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] CHECK CONSTRAINT [compositeaction$FK_CompositeAction_Action];
GO

ALTER TABLE [${dbxschemaname}].[dependentactions] 
ADD CONSTRAINT [FK_dependentactions_feature] 
FOREIGN KEY ([featureId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[dependentactions] 
ADD CONSTRAINT [FK_dependentactions_actionId] 
FOREIGN KEY ([actionId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[dependentactions] 
ADD CONSTRAINT [FK_dependentactions_dependentactionId] 
FOREIGN KEY ([dependentactionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[featureroletype] WITH NOCHECK ADD CONSTRAINT [featureroletype$FK_featureroletype_Feature_id] 
FOREIGN KEY([Feature_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[featureroletype] CHECK CONSTRAINT [featureroletype$FK_featureroletype_Feature_id]
GO

ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription]  WITH NOCHECK ADD  CONSTRAINT [featuredisplaynamedescription$FK_featuredisplaynamedescription_Feature_id] FOREIGN KEY([Feature_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] CHECK CONSTRAINT [featuredisplaynamedescription$FK_featuredisplaynamedescription_Feature_id]
GO

ALTER TABLE [${dbxschemaname}].[featureaction] WITH NOCHECK ADD CONSTRAINT [featureaction$FK_action_feature_id] 
FOREIGN KEY ([Feature_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[featureaction] CHECK CONSTRAINT [featureaction$FK_action_feature_id]
GO

ALTER TABLE [${dbxschemaname}].[organisationfeatures] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures]  WITH NOCHECK ADD  CONSTRAINT [organisationfeatures$FK_featureId] FOREIGN KEY([featureId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[feature] ([id], [companyLegalUnit]);
GO
ALTER TABLE [${dbxschemaname}].[organisationfeatures] CHECK CONSTRAINT [organisationfeatures$FK_featureId];
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock]
ADD CONSTRAINT [FK_bulkpaymentsubrecordmock_featureActionId_idx] 
FOREIGN KEY ([featureActionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrequestpos]
ADD CONSTRAINT [FK_bulkpaymentrequestpos_featureActionId_idx] 
FOREIGN KEY ([featureActionId], [legalEntityId])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] 
ADD CONSTRAINT [FK_bulkpaymentrecordmock_featureActionId_idx] 
FOREIGN KEY ([featureActionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[organisationactionlimit] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] WITH NOCHECK
ADD CONSTRAINT [FK_organisationactionlimit_action]
FOREIGN KEY ([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
ON UPDATE CASCADE;
GO
ALTER TABLE [${dbxschemaname}].[organisationactionlimit] CHECK CONSTRAINT [FK_organisationactionlimit_action];
GO

ALTER TABLE [${dbxschemaname}].[featureactionroletype]
ADD CONSTRAINT [FK_featureactionroletype_Action_id]
FOREIGN KEY ([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
ON UPDATE CASCADE;
GO

ALTER TABLE [${dbxschemaname}].[groupactionlimit]
ADD CONSTRAINT [FK_groupactionlimit_Action]
FOREIGN KEY ([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
ON UPDATE CASCADE;
GO

ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
ADD CONSTRAINT [FK_actiondisplaynamedescription_Action_id]
FOREIGN KEY ([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
ON UPDATE CASCADE;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrequest]
ADD CONSTRAINT [FK_bulkpaymentrequest_featureActionId_idx]
FOREIGN KEY ([featureActionId], [legalEntityId])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock]
ADD CONSTRAINT [FK_bulkpaymentfilesmock_featureActionId_idx]
FOREIGN KEY ([featureActionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos]
ADD CONSTRAINT [FK_bulkpaymenttemplatepos_featureActionId_idx]
FOREIGN KEY ([featureActionId], [legalEntityId])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord]
ADD CONSTRAINT [FK_bulkpaymentsubrecord_featureActionId_idx]
FOREIGN KEY ([featureActionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[customroleactionlimits] WITH NOCHECK ADD CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_3] FOREIGN KEY([action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
GO

ALTER TABLE [${dbxschemaname}].[customroleactionlimits] CHECK CONSTRAINT [customroleactionlimits$customroleactionlimits_ibfk_3]
GO

ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[approvalmatrix] WITH NOCHECK ADD CONSTRAINT [approvalmatrix$FK_approvalmatrix_actionid] FOREIGN KEY([actionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit]);
GO
ALTER TABLE [${dbxschemaname}].[approvalmatrix] CHECK CONSTRAINT [approvalmatrix$FK_approvalmatrix_actionid];
GO

ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit] 
ADD CONSTRAINT [FK_servicedefinitionactionlimit_action] 
FOREIGN KEY ([actionId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit]) 
ON DELETE NO ACTION 
ON UPDATE NO ACTION;

ALTER TABLE [${dbxschemaname}].[customeractionlimits] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customeractionlimits] WITH CHECK ADD  CONSTRAINT [FK_CustomerActionLimits_Action] FOREIGN KEY([Action_id], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[customeractionlimits] CHECK CONSTRAINT [FK_CustomerActionLimits_Action]
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD CONSTRAINT [FK_bulkpaymentrecord_featureActionId_idx] 
FOREIGN KEY ([featureActionId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit]) 
ON DELETE NO ACTION 
ON UPDATE NO ACTION;

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplate]  
ADD CONSTRAINT [FK_bulkpaymenttemplate_featureActionId_idx]
FOREIGN KEY ([featureActionId], [legalEntityId])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE NO ACTION 
ON UPDATE NO ACTION;

ALTER TABLE [${dbxschemaname}].[approvalmatrixtemplate] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[approvalmatrixtemplate]
ADD CONSTRAINT [FK_approvalmatrixtemplate_actionid]
FOREIGN KEY ([actionId], [companyLegalUnit])
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE [${dbxschemaname}].[actionlimit] WITH NOCHECK ADD CONSTRAINT [actionlimit$FK_actionlimit_featureaction]
FOREIGN KEY ([Action_id], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[actionlimit] CHECK CONSTRAINT [actionlimit$FK_actionlimit_featureaction]
GO


ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] 
ADD CONSTRAINT [FK_bulkpaymentfiles_featureActionId_idx] 
FOREIGN KEY ([featureActionId], [companyLegalUnit]) 
REFERENCES [${dbxschemaname}].[featureaction] ([id], [companyLegalUnit]) 
ON DELETE NO ACTION 
ON UPDATE NO ACTION;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[feature_action_limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[feature_action_limits_update_proc](
@_action varchar(255),
@_companyLegalUnit varchar(255),
@_minTxLimit decimal(20,2),
@_maxTxLimit decimal(20,2),
@_dailyLimit decimal(20,2),
@_weeklyLimit decimal(20,2)
)
AS
BEGIN
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_minTxLimit where Action_id = @_action AND LimitType_id = 'MIN_TRANSACTION_LIMIT' AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND companyLegalUnit = @_companyLegalUnit;	
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[limits_update_proc](
  @_action varchar(255),
  @_companyLegalUnit varchar(255),
  @_maxTxLimit decimal(20,2),
  @_dailyLimit decimal(20,2),
  @_weeklyLimit decimal(20,2)
)
AS
BEGIN
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_maxTxLimit where actionId = @_action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_dailyLimit where actionId = @_action AND limitTypeId = 'DAILY_LIMIT' AND value > @_dailyLimit AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_weeklyLimit where actionId = @_action AND limitTypeId = 'WEEKLY_LIMIT' AND value > @_weeklyLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_maxTxLimit where actionId = @_action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_dailyLimit where actionId = @_action AND limitTypeId = 'DAILY_LIMIT' AND value > @_dailyLimit AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_weeklyLimit where actionId = @_action AND limitTypeId = 'WEEKLY_LIMIT' AND value > @_weeklyLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit AND companyLegalUnit = @_companyLegalUnit; 
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit AND companyLegalUnit = @_companyLegalUnit;
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit AND companyLegalUnit = @_companyLegalUnit; 
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[dependentactions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[dependentactions_view] AS
     SELECT 
        [${dbxschemaname}].[dependentactions].[actionId] AS [actionId],
        [${dbxschemaname}].[dependentactions].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[dependentactions].[featureId] AS [featureId],
        [${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[dependentactions].[companyLegalUnit] AS [companyLegalUnit]
    FROM
        (([${dbxschemaname}].[dependentactions]
        LEFT JOIN [${dbxschemaname}].[featureaction] ON ([${dbxschemaname}].[dependentactions].[dependentactionId] = [${dbxschemaname}].[featureaction].[id]))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[dependentactions].[featureId] = [${dbxschemaname}].[feature].[id])
            AND ([${dbxschemaname}].[featureaction].[companyLegalUnit] = [${dbxschemaname}].[feature].[companyLegalUnit])));
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[get_feature_actions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[get_feature_actions_view] AS
    SELECT 
        [${dbxschemaname}].[featureaction].[id] AS [actionId],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[notes] AS [notes],
        [${dbxschemaname}].[featureaction].[Type_id] AS [typeId],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaySequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[featureaction].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[featureactionroletype].[RoleType_id] AS [actionType],
        [${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
        [${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],        
        [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus],
        [${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[description] AS [featureDescription],
        [${dbxschemaname}].[feature].[Type_id] AS [featureType],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [featureGroup],
        [${dbxschemaname}].[feature].[DisplaySequence] AS featureDisplaySequence,
        [${dbxschemaname}].[feature].[isPrimary] AS [isFeaturePrimary],
        [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
        [${dbxschemaname}].[actiondisplaynamedescription].[Locale_id] AS [localeId],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[actiondisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[actionlimit].[LimitType_id] AS [limitTypeId],
        [${dbxschemaname}].[actionlimit].[value] AS [value],
        [${dbxschemaname}].[dependentactions_view].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[dependentactions_view].[featureName] AS [dependentFeatureName],
        [${dbxschemaname}].[dependentactions_view].[actionName] AS [dependentActionName],
		[${dbxschemaname}].[dependentactions_view].[featureId] AS [dependentFeatureId],
        [${dbxschemaname}].[termandcondition].[Code] AS [termsAndConditionCode],
        [${dbxschemaname}].[termandcondition].[Title] AS [termsAndConditionTitle],
        [${dbxschemaname}].[termandcondition].[Description] AS [termsAndConditionDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName]
    FROM
        ((((((((((([${dbxschemaname}].featureaction
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
            AND ([${dbxschemaname}].[feature].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[actiondisplaynamedescription] ON (([${dbxschemaname}].[actiondisplaynamedescription].[Action_id] = [${dbxschemaname}].[featureaction].[id])
            AND ([${dbxschemaname}].[actiondisplaynamedescription].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])
            AND ([${dbxschemaname}].[featureroletype].[companyLegalUnit] = [${dbxschemaname}].[feature].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[featureactionroletype].[Action_id])
            AND ([${dbxschemaname}].[featureaction].[companyLegalUnit] = [${dbxschemaname}].[featureactionroletype].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[termandcondition] ON (([${dbxschemaname}].[featureaction].[TermsAndConditions_id] = [${dbxschemaname}].[termandcondition].[id])))
        LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])))
        LEFT JOIN [${dbxschemaname}].[dependentactions_view] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions_view].[actionId])
            AND ([${dbxschemaname}].[feature].[companyLegalUnit] = [${dbxschemaname}].[dependentactions_view].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureactionroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])
            AND ([${dbxschemaname}].[actionlimit].[companyLegalUnit] = [${dbxschemaname}].[featureaction].[companyLegalUnit])))
		ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[get_all_features_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[get_all_features_view] AS
    SELECT 
        [${dbxschemaname}].[feature].[id] AS [id],
        [${dbxschemaname}].[feature].[name] AS [name],
        [${dbxschemaname}].[feature].[description] AS [description],
        [${dbxschemaname}].[feature].[Type_id] AS [Type_id],
        [${dbxschemaname}].[feature].[Service_Fee] AS [Service_Fee],
        [${dbxschemaname}].[feature].[Status_id] AS [Status_id],
		[${dbxschemaname}].[feature].[companyLegalUnit] AS [companyLegalUnit],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [roleTypeId],
        [${dbxschemaname}].[featuredisplaynamedescription].[Locale_id] AS [languageId],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                [${dbxschemaname}].[featureaction]
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'MONETARY'))) AS [monetaryActions],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                [${dbxschemaname}].[featureaction]
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'NON_MONETARY'))) AS nonMonetaryActions
    FROM
        ((([${dbxschemaname}].[feature]
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])
            AND ([${dbxschemaname}].[featureroletype].[companyLegalUnit] = [${dbxschemaname}].[feature].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[featuredisplaynamedescription] ON (([${dbxschemaname}].[featuredisplaynamedescription].[Feature_id] = [${dbxschemaname}].[feature].[id])
            AND ([${dbxschemaname}].[featuredisplaynamedescription].[companyLegalUnit] = [${dbxschemaname}].[feature].[companyLegalUnit])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])));
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[feature_action_roles_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[feature_action_roles_view] AS
    SELECT
        [${dbxschemaname}].[featureaction].[Feature_id] AS [feature_code],
        [${dbxschemaname}].[feature_view].[Name] AS [feature_name],
        [${dbxschemaname}].[feature_view].[Type_Id] AS [feature_type_id],
        [${dbxschemaname}].[feature_view].[Status_Id] AS [feature_status_id],
        [${dbxschemaname}].[featureaction].[id] AS [action_code],
        [${dbxschemaname}].[featureaction].[name] AS [action_name],
        [${dbxschemaname}].[featureaction].[description] AS [action_description],
        [far].[RoleType_id] AS [action_role_type_id],
        [${dbxschemaname}].[featureaction].[Type_id] AS [category],
        [${dbxschemaname}].[featureaction].[accesspolicyId] AS [accesspolicyId],
        [${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
        [${dbxschemaname}].[featureaction].[status] AS [status],
        [${dbxschemaname}].[actionlimit].[LimitType_id] AS [limitType_id],
        [${dbxschemaname}].[actionlimit].[value] AS [value],
        [${dbxschemaname}].[featureaction].[companyLegalUnit] AS [companyLegalUnit]
    FROM
        ((([${dbxschemaname}].[featureaction]
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] [far] ON (([far].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[feature_view] ON (([${dbxschemaname}].[feature_view].[Code] = [${dbxschemaname}].[featureaction].[Feature_id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[permissions_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[permissions_view] AS
SELECT [${dbxschemaname}].permission.id AS Permission_id, [${dbxschemaname}].permission.Type_id AS PermissionType_id, [${dbxschemaname}].permission.Name AS Permission_Name, [${dbxschemaname}].permission.Description AS Permission_Desc, [${dbxschemaname}].permission.Status_id, [${dbxschemaname}].permission.companyLegalUnit AS companyLegalUnit, [${dbxschemaname}].status.Description AS Status_Desc,
                 (SELECT COUNT(DISTINCT Role_id) AS Expr1
                 FROM    [${dbxschemaname}].rolepermission
                 WHERE (Permission_id = [${dbxschemaname}].permission.id)) AS Role_Count,
                 (SELECT COUNT(Permission_id) AS Expr1
                 FROM    [${dbxschemaname}].userpermission
                 WHERE (Permission_id = [${dbxschemaname}].permission.id)) +
                 (SELECT COUNT(User_id) AS Expr1
                 FROM    [${dbxschemaname}].userrole
                 WHERE (Role_id IN
                                  (SELECT Role_id
                                  FROM    [${dbxschemaname}].rolepermission
                                  WHERE (Permission_id = [${dbxschemaname}].permission.id)))) AS Users_Count, CASE (permission.Status_id) WHEN N'SID_ACTIVE' THEN N'Active' ELSE N'Inactive' END AS Status
FROM   [${dbxschemaname}].permission INNER JOIN
             [${dbxschemaname}].status ON [${dbxschemaname}].permission.Status_id = [${dbxschemaname}].status.id;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_actions_with_approvefeatureaction_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].get_actions_with_approvefeatureaction_proc(
@_featureActions NVARCHAR(max),
@_companyLegalUnit NVARCHAR(255)) AS
BEGIN
DECLARE @actionsList NVARCHAR(max)
SET @actionsList = (
            SELECT String_agg(CAST([${dbxschemaname}].[featureaction].[id] AS nvarchar(max)), ',') FROM [${dbxschemaname}].[featureaction] WHERE
                               [${dbxschemaname}].FIND_IN_SET(id,@_featureActions) = 1 AND companyLegalUnit = @_companyLegalUnit AND
                    (isnull(featureaction.approvefeatureaction,'') != ''));
select @actionsList As actions;
END;

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_accountlevelcustomerlimits_for_featureaction_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_accountlevelcustomerlimits_for_featureaction_proc]
    @_customerId        NVARCHAR(128),
    @_featureActionId   NVARCHAR(256)
AS
BEGIN
	DECLARE @orgId VARCHAR(128);
	SET @orgId = (SELECT DISTINCT(Organization_Id) FROM [${dbxschemaname}].[customer] WHERE [id] = @_customerId);
	IF @orgId IS NULL OR @orgId = ''
		SELECT 
			[ca].[Customer_id] AS 'customerId',
			[cg].[Group_id] AS 'roleId',
			[sdal].[serviceDefinitionId],
			[ca].[Account_id] AS 'accountId',
			[ca].[contractId],
			[ca].[coreCustomerId],
			[al].[Action_id] AS 'baseActionId',
			[sdal].[actionId] AS 'serviceDefActionId',
			[cal].[actionId] AS 'contractActionId',
			[gal].[Action_id] AS 'roleActionId',
			[al].[LimitType_id] AS 'baseLimitTypeId',
            [sdal].[limitTypeId] AS 'serviceDefLimitTypeId',
			[cal].[limitTypeId] AS 'contractLimitTypeId',
			[gal].[LimitType_id] AS 'roleLimitTypeId',
			[al].[value] AS 'baseLimitValue',
			[sdal].[value] AS 'serviceDevLimitValue',
			[cal].[value] AS 'contractLimitValue',
			[gal].[value] AS 'roleLimitValue',
			(SELECT MIN(val) FROM (VALUES ([al].[value]), ([gal].[value]), ([sdal].[value]), ([cal].[value])) AS v (val)) AS 'minLimitValue'
			FROM [${dbxschemaname}].[customeraccounts] AS [ca]
		INNER JOIN [${dbxschemaname}].[customergroup] AS cg ON 
			[ca].[Customer_id] = [cg].[Customer_id]
		INNER JOIN [${dbxschemaname}].[groupactionlimit] AS [gal] ON 
			[gal].[Group_id] = [cg].[Group_id]
		INNER JOIN [${dbxschemaname}].[contract] AS [c] ON 
			[c].[id] = [ca].[contractId]
		INNER JOIN [${dbxschemaname}].[servicedefinitionactionlimit] AS [sdal] ON 
			[sdal].[serviceDefinitionId] = [c].[servicedefinitionId] AND [sdal].[actionId] = [gal].[Action_id] AND [sdal].[limitTypeId] = [gal].[LimitType_id]
		INNER JOIN [${dbxschemaname}].[contractactionlimit] AS [cal] ON
			[cal].[contractId] = [ca].[contractId] AND [cal].[actionId] = [sdal].[actionId] AND [cal].[limitTypeId] = [sdal].[limitTypeId]
		INNER JOIN [${dbxschemaname}].[actionlimit] AS [al] ON
			[al].[Action_id] = [cal].[actionId] AND [al].[LimitType_id] = [cal].[limitTypeId] AND [al].[companyLegalUnit] = [ca].[companyLegalUnit]
		WHERE [ca].[Customer_id] = @_customerId AND [al].[Action_id] = @_featureActionId ORDER BY [ca].[Account_id];
	ELSE
		SELECT 
			[ca].[Customer_id] AS 'customerId',
			[cg].[Group_id] AS 'roleId',
			[sdal].[serviceDefinitionId],
			[oal].[Organisation_id] AS 'organizationId',
			[ca].[Account_id] AS 'accountId',
			[ca].[contractId],
			[ca].[coreCustomerId],
			[al].[Action_id] AS 'baseActionId',
			[sdal].[actionId] AS 'serviceDefActionId',
			[oal].[Action_id] AS 'organizationActionId',
			[cal].[actionId] AS 'contractActionId',
			[gal].[Action_id] AS 'roleActionId',
			[al].[LimitType_id] AS 'baseLimitTypeId',
			[sdal].[limitTypeId] AS 'serviceDefLimitTypeId',
			[oal].[LimitType_id] AS 'organisationLimitTypeId',
			[cal].[limitTypeId] AS 'contractLimitTypeId',
			[gal].[LimitType_id] AS 'roleLimitTypeId',
			[al].[value] AS 'baseLimitValue',
			[sdal].[value] AS 'serviceDevLimitValue',
			[oal].[value] AS 'organisationLimitValue',
			[cal].[value] AS 'contractLimitValue',
			[gal].[value] AS 'roleLimitValue',
			(SELECT MIN(val) FROM (VALUES ([al].[value]), ([gal].[value]), ([sdal].[value]), ([oal].[value]), ([cal].[value])) AS v (val)) AS 'minLimitValue'
			FROM [${dbxschemaname}].[customeraccounts] AS [ca]
		INNER JOIN [${dbxschemaname}].[customergroup] AS cg ON 
			[ca].[Customer_id] = [cg].[Customer_id]
		INNER JOIN [${dbxschemaname}].[groupactionlimit] AS [gal] ON 
			[gal].[Group_id] = [cg].[Group_id]
		INNER JOIN [${dbxschemaname}].[contract] AS [c] ON 
			[c].[id] = [ca].[contractId]
		INNER JOIN [${dbxschemaname}].[servicedefinitionactionlimit] AS [sdal] ON 
			[sdal].[serviceDefinitionId] = [c].[servicedefinitionId] AND [sdal].[actionId] = [gal].[Action_id] AND [sdal].[limitTypeId] = [gal].[LimitType_id]
		INNER JOIN [${dbxschemaname}].[contractactionlimit] AS [cal] ON
			[cal].[contractId] = [ca].[contractId] AND [cal].[actionId] = [sdal].[actionId] AND [cal].[limitTypeId] = [sdal].[limitTypeId]
		INNER JOIN [${dbxschemaname}].[actionlimit] AS [al] ON
			[al].[Action_id] = [cal].[actionId] AND [al].[LimitType_id] = [cal].[limitTypeId] AND [al].[companyLegalUnit] = [ca].[companyLegalUnit]
		INNER JOIN [${dbxschemaname}].[customer] AS [cu] ON
			[cu].[id] = [ca].[Customer_id]
		INNER JOIN [${dbxschemaname}].[organisationactionlimit] AS [oal] ON
			[oal].[Organisation_id] = [cu].[Organization_Id] AND [oal].[Action_id] = [al].[Action_id] AND [oal].[LimitType_id] = [al].[LimitType_id]
		WHERE [ca].[Customer_id] = @_customerId AND [al].[Action_id] = @_featureActionId ORDER BY [ca].[Account_id];
END
GO

USE [${dbxdbname}]
GO

DROP procedure IF EXISTS [${dbxschemaname}].[account_action_approvers_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[account_action_approvers_proc]  
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_accountIds nvarchar(50),
   @_approvalActionList nvarchar(50),
   @_featureId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      declare @group_concat_max_len bigint
      declare @customerIdList nvarchar(max)
	  declare @NumberOfAccounts bigint
	  declare @customerIdListWithNoAccountAccess nvarchar(max)
      SET  NOCOUNT  ON

      SET @group_concat_max_len = 1000000
      SET @customerIdList = 
         (
            SELECT String_agg(CAST(customeraction.Customer_id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.isAllowed = 0 AND 
               [${dbxschemaname}].customeraction.Action_id = @_approvalActionList AND 
			   [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Account_id, @_accountIds) > 0 AND
               [${dbxschemaname}].customeraction.contractId = @_contractId AND
			   [${dbxschemaname}].customeraction.coreCustomerId = @_cif
         )
		 
      SET @customerIdList = 
         CASE 
            WHEN (@customerIdList IS NULL) THEN ''
            ELSE @customerIdList
         END
	  SET @NumberOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;

	  SET @customerIdListWithNoAccountAccess = (SELECT String_agg(CAST(Customer_id  as nvarchar(max)),',')
										  from [${dbxschemaname}].customeraccounts 
										  where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Account_id, @_accountIds) > 0
										  group by Customer_id having 
										  count(Account_id) != @NumberOfAccounts);
										  
	  IF(@customerIdListWithNoAccountAccess is null)
		SET @customerIdListWithNoAccountAccess =  ''
	  ELSE
		SET @customerIdListWithNoAccountAccess = @customerIdListWithNoAccountAccess

      SELECT DISTINCT 
         ([${dbxschemaname}].customer.id) AS id, 
         ([${dbxschemaname}].customer.UserName) AS userName, 
         ([${dbxschemaname}].membergroup.Name) AS groupId, 
         ([${dbxschemaname}].customer.FirstName) AS firstName, 
         ([${dbxschemaname}].customer.LastName) AS lastName
      FROM ([${dbxschemaname}].customer 
         LEFT JOIN [${dbxschemaname}].contractcustomers 
         ON ([${dbxschemaname}].contractcustomers.id = [${dbxschemaname}].customer.id AND 
			[${dbxschemaname}].contractcustomers.contractId = @_contractId and [${dbxschemaname}].contractcustomers.coreCustomerId = @_cif) 
         LEFT JOIN [${dbxschemaname}].customergroup 
         ON ([${dbxschemaname}].customergroup.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].membergroup 
         ON ([${dbxschemaname}].membergroup.id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].groupactionlimit 
         ON ([${dbxschemaname}].groupactionlimit.Group_id = [${dbxschemaname}].customergroup.Group_id) 
         LEFT JOIN [${dbxschemaname}].customeraccounts 
         ON ([${dbxschemaname}].customeraccounts.Customer_id = [${dbxschemaname}].customer.id) 
         LEFT JOIN [${dbxschemaname}].contractfeatures 
         ON ([${dbxschemaname}].contractfeatures.contractId = @_contractId AND [${dbxschemaname}].contractfeatures.coreCustomerId = @_cif))
      WHERE 
		 [${dbxschemaname}].contractfeatures.contractId = @_contractId AND
         [${dbxschemaname}].contractfeatures.coreCustomerId = @_cif AND
         [${dbxschemaname}].contractfeatures.featureId = @_featureId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Account_id, @_accountIds) > 0  AND 
         [${dbxschemaname}].customer.Status_id = 'SID_CUS_ACTIVE' AND 
		 [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id , @customerIdList) > 0 AND 
		 [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id , @customerIdListWithNoAccountAccess) > 0  AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id, @_approvalActionList) > 0

   END
GO

ALTER TABLE [${dbxschemaname}].[approvalmatrix] ADD [coreCustomerId] VARCHAR(50) NULL;
GO

SP_RENAME '[${dbxschemaname}].approvalmatrix.companyId', 'contractId', 'column';
GO

DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_fetch_records_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_fetch_records_proc]  
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_accountId nvarchar(50),
   @_limitTypeId nvarchar(50),
   @_actions nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
	  IF @_cif = ''
		SET @_cif = '%'

      SET  NOCOUNT  ON
      IF @_accountId = ''
         SET @_accountId = '%'
      IF @_limitTypeId = ''
         SET @_limitTypeId = '%'

      SELECT 
         approvalMatrix.id, 
         approvalMatrix.contractId, 
         approvalMatrix.accountId, 
         approvalMatrix.limitTypeId, 
         featureAction.id AS actionId, 
         featureAction.name AS actionName, 
         featureAction.description AS actionDescription, 
         featureAction.Feature_id AS featureId, 
         feature.name AS featureName, 
         feature.Status_id AS fifeaturestatus, 
         approvalRule.id AS approvalruleId, 
         approvalRule.numberOfApprovals, 
         approvalRule.name AS approvalRuleName, 
         approvalMatrix.lowerlimit, 
         approvalMatrix.upperlimit, 
         customer.id AS customerId, 
         customer.FirstName AS firstName, 
         customer.LastName AS lastName,
		 contractcorecustomers.coreCustomerId AS cifId,
         contractcorecustomers.coreCustomerName AS cifName,
         approvalMatrix.invalid
      FROM ((((((([${dbxschemaname}].approvalmatrix  AS approvalMatrix 
         LEFT JOIN [${dbxschemaname}].customerapprovalmatrix  AS customerApprovalMatrix 
         ON approvalMatrix.id = customerApprovalMatrix.approvalMatrixId) 
         LEFT JOIN [${dbxschemaname}].customer  AS customer 
         ON customerApprovalMatrix.customerId = customer.id) 
         LEFT JOIN [${dbxschemaname}].featureaction  AS featureAction 
         ON approvalMatrix.actionId = featureAction.id) 
         LEFT JOIN [${dbxschemaname}].approvalrule  AS approvalRule 
         ON approvalMatrix.approvalruleId = approvalRule.id) 
         LEFT JOIN [${dbxschemaname}].feature  AS feature 
         ON featureAction.Feature_id = feature.id) 
         LEFT JOIN [${dbxschemaname}].contractfeatures AS contractfeatures 
         ON feature.id = contractfeatures.featureId and approvalMatrix.contractId = contractfeatures.contractId
          and approvalMatrix.coreCustomerId = contractfeatures.coreCustomerId)
		 LEFT JOIN contractcorecustomers AS contractcorecustomers
         ON approvalMatrix.contractId = contractcorecustomers.contractId AND approvalMatrix.coreCustomerId = contractcorecustomers.coreCustomerId) 
      WHERE 
         approvalMatrix.contractId = @_contractId AND
		 approvalMatrix.coreCustomerId LIKE @_cif AND
         approvalMatrix.accountId LIKE @_accountId AND 
         [${dbxschemaname}].FIND_IN_SET(approvalMatrix.actionId, @_actions) > 0 AND 
         approvalMatrix.limitTypeId LIKE @_limitTypeId AND 
         approvalMatrix.softdeleteflag = 0 AND
		 featureAction.status = 'SID_ACTION_ACTIVE'
         ORDER BY 
            approvalMatrix.contractId, 
            approvalMatrix.accountId, 
            approvalMatrix.limitTypeId, 
            approvalMatrix.actionId, 
            approvalMatrix.lowerlimit
   END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_create_proc]  
   @_actionIds nvarchar(max),
   @_contractId nvarchar(50),
   @_accountId nvarchar(50),
   @_cif nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE @accountList varchar(max) = ''
      DECLARE @finished int = 0
      DECLARE @actionId varchar(255) = ''
      DECLARE @actionList varchar(max) = ''
      DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT'
      DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT'
      DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT'
	  DECLARE @numOfAccounts int = 0
	  DECLARE @numOfActions int = 0
	  DECLARE @accountIndex int = 0
	  DECLARE @actionIndex int = 0

      DECLARE
          actions CURSOR LOCAL FORWARD_ONLY FOR 
             (
               SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_actionIds) <> 0
             )

      OPEN actions

      WHILE (1 = 1)
      
         BEGIN

            FETCH actions
                INTO @actionId

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.contractId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId,
					 [${dbxschemaname}].approvalmatrix.coreCustomerId,
					 [${dbxschemaname}].approvalmatrix.approvalruleId)
                     VALUES (
                        @_contractId, 
                        @actionId + '_'+ @_accountId+ '_'+ @limitTypeId_1+ '_'+ @_contractId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_1,
						@_cif,
						'NO_APPROVAL'
                     )

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.contractId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId,
					 [${dbxschemaname}].approvalmatrix.coreCustomerId,
					 [${dbxschemaname}].approvalmatrix.approvalruleId)
                     VALUES (
                        @_contractId, 
                        @actionId+ '_'+ @_accountId+ '_' + @limitTypeId_2 + '_'+ @_contractId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_2,
						@_cif,
						'NO_APPROVAL'
                     )

                  INSERT [${dbxschemaname}].approvalmatrix(
                     [${dbxschemaname}].approvalmatrix.contractId, 
                     [${dbxschemaname}].approvalmatrix.name, 
                     [${dbxschemaname}].approvalmatrix.accountId, 
                     [${dbxschemaname}].approvalmatrix.actionId, 
                     [${dbxschemaname}].approvalmatrix.limitTypeId,
					 [${dbxschemaname}].approvalmatrix.coreCustomerId,
					 [${dbxschemaname}].approvalmatrix.approvalruleId)
                     VALUES (
                        @_contractId, 
                        @actionId + '_' + @_accountId + '_' + @limitTypeId_3 + '_' + @_contractId, 
                        @_accountId, 
                        @actionId, 
                        @limitTypeId_3,
						@_cif,
						'NO_APPROVAL'
                     )

                  SET @actionList = @actionId + ',' + @actionList
                  CONTINUE
               END

         END
      CLOSE actions
      DEALLOCATE actions
      SET @actionList = 
         (
            SELECT substring(@actionList, 1, LEN(@actionList)-1)
         )
      SELECT @actionList AS actionList

   END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_create_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_create_proc]  
   @_matrixValues nvarchar(max),
   @_approverIds nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

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
                        approvalmatrix.upperlimit
                     ) VALUES (') + (@matrixComma) + (')')
					
                  execute (@query)
                  SET @id = @@IDENTITY
				  
				  set @customerIds = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_approverIds, ',', @index1), ',', -1 );
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
                  CONTINUE
               END
         END
   END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc];

GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc]  
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_accountId nvarchar(50),
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
         [${dbxschemaname}].approvalmatrix.accountId = @_accountId AND 
         [${dbxschemaname}].approvalmatrix.actionId = @_actionId AND 
         [${dbxschemaname}].approvalmatrix.limitTypeId = @_limitTypeId

   END
GO

GO
DROP procedure IF EXISTS [${dbxschemaname}].[approvalmatrix_default_delete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_delete_proc]  
   @_contractId nvarchar(50),
   @_cif nvarchar(50),
   @_filterColumnIds nvarchar(max),
   @_filterColumnName nvarchar(50)
AS 
   BEGIN
	  
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      IF  @_filterColumnName = 'actionId'
		 begin
         DELETE FROM [${dbxschemaname}].approvalmatrix
         WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.actionId, @_filterColumnIds) <> 0
		end
      ELSE IF @_filterColumnName = 'accountId'
         BEGIN
               DELETE FROM [${dbxschemaname}].approvalmatrix
               WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.accountId, @_filterColumnIds) <> 0
         END
	  ELSE IF @_filterColumnName = 'cif'
         BEGIN
               DELETE FROM [${dbxschemaname}].approvalmatrix
               WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.coreCustomerId, @_filterColumnIds) <> 0
         END
   END
GO

ALTER TABLE [${dbxschemaname}].[approvalmatrix]
DROP CONSTRAINT [approvalmatrix$FK_approvalmatrix_Companyid];
GO
DROP INDEX IF EXISTS FK_approvalmatrix_companyIdx ON [${dbxschemaname}].[approvalmatrix];

GO

CREATE INDEX approvalmatrix_contractId_idx on [${dbxschemaname}].[approvalmatrix](contractId ASC) ;
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[servicedefinition_view]; 
GO

CREATE VIEW [${dbxschemaname}].[servicedefinition_view] AS
     SELECT 
    [${dbxschemaname}].[servicedefinition].[id] AS [id],
    [${dbxschemaname}].[servicedefinition].[name] AS [name],
    [${dbxschemaname}].[servicedefinition].[description] AS [description],
    [${dbxschemaname}].[servicedefinition].[serviceType] AS [serviceType],
    [${dbxschemaname}].[servicedefinition].[status] AS [status],
    (SELECT COUNT([${dbxschemaname}].[groupservicedefinition].[Group_id]) FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfRoles],
	(SELECT COUNT([${dbxschemaname}].[membergroup].[id]) FROM [${dbxschemaname}].[membergroup] WHERE Status_id LIKE 'SID_ACTIVE' AND id in (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM 
		[${dbxschemaname}].[groupservicedefinition] WHERE ([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]))) AS [numberOfActiveRoles],
    (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE (([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]) AND ([${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = 1))) AS [defaultRole],
    (SELECT COUNT(DISTINCT [${dbxschemaname}].[servicedefinition_features_actions_view].[featureId]) FROM [${dbxschemaname}].[servicedefinition_features_actions_view]
        WHERE (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinition_features_actions_view].[serviceDefinitionId]) AND ([${dbxschemaname}].[servicedefinition_features_actions_view].[softdelete] = '0'))) AS [numberOfFeatures],
    (SELECT COUNT([${dbxschemaname}].[contract].[id]) FROM [${dbxschemaname}].[contract] 
      WHERE ([${dbxschemaname}].[contract].[servicedefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfContracts]
  FROM [${dbxschemaname}].[servicedefinition];
		
GO

EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[servicedefinitionactionlimit]', 'softdeleteflag';
GO

ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit]
alter column [softdeleteflag] bit not null;
GO

ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit]
ADD CONSTRAINT df_servdefactionlimit_softdelete
DEFAULT '0' FOR [softdeleteflag];
GO


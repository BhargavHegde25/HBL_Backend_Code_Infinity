DROP procedure IF EXISTS [${dbxschemaname}].[servicedefinition_defaultgroup_update_proc]
GO

CREATE  PROCEDURE [${dbxschemaname}].[servicedefinition_defaultgroup_update_proc]
 @_serviceDefinitionId nvarchar(50),
 @_groupId nvarchar(50),
 @_isDefault nvarchar(50)
 AS
 BEGIN
 IF (@_isDefault = '0') 
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '0' where [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId ;
 ELSE
    BEGIN
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '0' where 
     [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId;
     UPDATE [${dbxschemaname}].groupservicedefinition SET isDefaultGroup = '1' where [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @_serviceDefinitionId AND [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId;     
    END 
 
 END
 GO

 DROP TABLE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate];
CREATE TABLE [${dbxschemaname}].[approvalmatrixtemplate](
	[id] [bigint] IDENTITY(109,1) NOT NULL,
	[contractId] [nvarchar](50) NOT NULL,
	[coreCustomerId] [nvarchar](50) NOT NULL,
	[actionId] [nvarchar](255) NOT NULL,
	[approvalruleId] [nvarchar](50) DEFAULT NULL,
	[isGroupMatrix] [bit] NOT NULL DEFAULT '0',
	[limitTypeId] [nvarchar](50) NOT NULL,
	[lowerlimit] [decimal](20, 2) NOT NULL DEFAULT '-1.00',
	[upperlimit] [decimal](20, 2) NOT NULL DEFAULT '-1.00',
	[createdby] [nvarchar](50) DEFAULT NULL,
	[modifiedby] [nvarchar](50) DEFAULT NULL,
	[createdts] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,
	[lastmodifiedts] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,
	[synctimestamp] [datetime] NOT NULL DEFAULT CURRENT_TIMESTAMP,
	[softdeleteflag] [bit] NOT NULL DEFAULT '0',
	[invalid] [bit] NOT NULL DEFAULT '0',
 CONSTRAINT [PK_approvalmatrixtemplate_id] PRIMARY KEY CLUSTERED 
	([id] ASC) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
	INDEX [FK_approvalmatrixtemplate_approvalruleid_idx] ([approvalruleId]),
	INDEX [FK_approvalmatrixtemplate_action_idx] ([actionId]),
	INDEX [FK_approvalmatrixtemplate_limittype_idx] ([limitTypeId]),
	INDEX [approvalmatrixtemplate_contractId_idx] ([contractId]),
	INDEX [approvalmatrixtemplate_coreCustomerId_idx] ([coreCustomerId]),
	CONSTRAINT [FK_approvalmatrixtemplate_actionid] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE CASCADE ON UPDATE CASCADE ,
	CONSTRAINT [FK_approvalmatrixtemplate_approvalruleid] FOREIGN KEY ([approvalruleId]) REFERENCES [${dbxschemaname}].[approvalrule] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
	CONSTRAINT [FK_approvalmatrixtemplate_limittypeid] FOREIGN KEY ([limitTypeId]) REFERENCES [${dbxschemaname}].[limittype] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ON [PRIMARY] 
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[customerapprovalmatrixtemplate];
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE [${dbxschemaname}].[customerapprovalmatrixtemplate](
   [id] [bigint] IDENTITY(109,1) NOT NULL,
   [customerId] nvarchar(50) NOT NULL,
   [approvalMatrixId] bigint NOT NULL,
   [createdby] nvarchar(50) DEFAULT NULL,
   [modifiedby] nvarchar(50) DEFAULT NULL,
   [createdts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   [lastmodifiedts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   [synctimestamp] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   [softdeleteflag] tinyint NOT NULL DEFAULT '0',
   CONSTRAINT [PK_customerapprovalmatrixtemplate_id] PRIMARY KEY CLUSTERED 
   ([id] ASC) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY],
   INDEX [FK_customerapprovalmatrixtemplate_customer_idx] ([customerId]),
   INDEX [FK_customerapprovalmatrixtemplate_approvalmatrixtemplate_idx] ([approvalMatrixId]),
   CONSTRAINT [FK_customerapprovalmatrixtemplate_approvalmatrixtemplate] FOREIGN KEY ([approvalMatrixId]) REFERENCES [${dbxschemaname}].[approvalmatrixtemplate] ([id]) ON DELETE CASCADE ON UPDATE NO ACTION,
   CONSTRAINT [FK_customerapprovalmatrixtemplate_customer] FOREIGN KEY ([customerId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ON [PRIMARY] 
GO
 
DROP TABLE IF EXISTS [${dbxschemaname}].[signatorygroupmatrixtemplate];
-- SQLINES LICENSE FOR EVALUATION USE ONLY
CREATE TABLE [${dbxschemaname}].[signatorygroupmatrixtemplate] (
   [signatoryGroupMatrixId] bigint IDENTITY(109,1) NOT NULL ,
   [approvalMatrixId] bigint DEFAULT NULL,
   [groupList] nvarchar(max),
   [groupRule] nvarchar(max),
   [createdby] nvarchar(50) DEFAULT NULL,
   [modifiedby] nvarchar(50) DEFAULT NULL,
   [createdts] datetime NULL DEFAULT CURRENT_TIMESTAMP,
   [lastmodifiedts] datetime NULL DEFAULT CURRENT_TIMESTAMP,
   [synctimestamp] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
   [softdeleteflag] tinyint NOT NULL DEFAULT '0',
   PRIMARY KEY ([signatoryGroupMatrixId]),
   INDEX [FK_signatorygroupmatrixtemplate_approvalMatrixId_Idx] ([approvalMatrixId]),
   CONSTRAINT [FK_signatorygroupmatrixtemplate_approvalMatrixId] FOREIGN KEY ([approvalMatrixId]) REFERENCES [${dbxschemaname}].[approvalmatrixtemplate] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
 )
 GO
 
 DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_default_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrixtemplate_default_create_proc](
	@_actionIds NVARCHAR(max) , 
	@_contractId NVARCHAR(50) ,
	@_cif NVARCHAR(50),
	@_approvalMode int
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
 
IF @_actionIds IS NULL OR  @_actionIds = ''
	GOTO MAINLABEL$leave
	
IF @_contractId IS NULL OR  @_contractId = ''
	GOTO MAINLABEL$leave
	
IF @_cif IS NULL OR  @_cif = ''
	GOTO MAINLABEL$leave

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
		SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
        IF @typeId = 'MONETARY' BEGIN			
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(@_contractId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL', @isGroupMatrix);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(@_contractId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL', @isGroupMatrix);
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
			(@_contractId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL', @isGroupMatrix);
        END
        ELSE IF @typeId = 'NON_MONETARY' BEGIN
			INSERT INTO approvalmatrixtemplate (contractId, actionId, limitTypeId,coreCustomerId, approvalruleId, isGroupMatrix) VALUES
            (@_contractId, @actionId, 'NON_MONETARY_LIMIT', @_cif, 'NO_APPROVAL', @isGroupMatrix);
		END
	END 
END;  
MAINLABEL$leave: 
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_cleanup_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrixtemplate_cleanup_proc](
	@_actionIds NVARCHAR(MAX), 
	@_contractId NVARCHAR(50),
	@_cif NVARCHAR(50),
	@_limitTypeId NVARCHAR(MAX)
) AS
MAINLABEL: BEGIN
	UPDATE [${dbxschemaname}].[approvalmatrix] SET [approvalmatrix].[softdeleteflag] = 1 WHERE [approvalmatrix].[contractId] = @_contractId AND 
													 [approvalmatrix].[coreCustomerId] = @_cif AND
													 [${dbxschemaname}].FIND_IN_SET([approvalmatrix].[actionId], @_actionIds) > 0 AND
													 [${dbxschemaname}].FIND_IN_SET([approvalmatrix].[limitTypeId], @_limitTypeId) > 0 AND
													 [approvalmatrix].[softdeleteflag] = 0;										
    
    UPDATE [${dbxschemaname}].[approvalmatrixtemplate] SET [approvalmatrixtemplate].[softdeleteflag] = 1 WHERE [approvalmatrixtemplate].[contractId] = @_contractId AND 
																	[approvalmatrixtemplate].[coreCustomerId] = @_cif AND
                                                                    [${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[actionId], @_actionIds) > 0 AND
																	[${dbxschemaname}].FIND_IN_SET([approvalmatrixtemplate].[limitTypeId], @_limitTypeId) > 0 AND
																	[approvalmatrixtemplate].[softdeleteflag] = 0;
																									
END
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

    IF @_cif = '' BEGIN
    SET @_cif = '%';
    END 
    
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
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
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
		[featureAction].[status] = 'SID_ACTION_ACTIVE'
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
			 [feature].[name] AS [featureName],
			 [feature].[Status_id] AS [fifeaturestatus],
			 [approvalRule].[id] AS [approvalruleId],
			 [approvalRule].[numberOfApprovals],
			 [approvalRule].[name] AS [approvalRuleName],
			 [approvalmatrixtemplate].[lowerlimit],
			 [approvalmatrixtemplate].[upperlimit],
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
		[featureAction].[status] = 'SID_ACTION_ACTIVE'
			ORDER BY [approvalmatrixtemplate].[contractId],[approvalmatrixtemplate].[coreCustomerId],[approvalmatrixtemplate].[limitTypeId],[approvalmatrixtemplate].[actionId] ,[approvalmatrixtemplate].[lowerlimit];

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
				set @query = concat('INSERT INTO approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,isGroupMatrix) VALUES (',@matrixComma,');');
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
									INSERT INTO customerapprovalmatrixtemplate(customerId,approvalMatrixId) values (@customerId,@id);							
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
					INSERT INTO signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule) values (@id, @groupList, @groupRule);
               END  
			END 
	END 
END
GO
ALTER TABLE [${dbxschemaname}].[customersignatorygroup] DROP CONSTRAINT [FK_customersignatorygroup_signaoryGroupId];
GO
ALTER TABLE [${dbxschemaname}].[customersignatorygroup] ADD CONSTRAINT [FK_customersignatorygroup_signaoryGroupId] FOREIGN KEY ([signatoryGroupId]) REFERENCES [${dbxschemaname}].[signatorygroup] ([signatoryGroupId]) ON DELETE CASCADE ON UPDATE NO ACTION;
GO

ALTER TABLE [${dbxschemaname}].[bbrequest] ADD [isGroupMatrix] BIT NOT NULL DEFAULT '0';
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_signatorygroups_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroups_proc] (
  @_contractId AS  nvarchar(50) ,
  @_coreCustomerId nvarchar(max)
) AS
BEGIN
	DECLARE @index1 INTEGER = 1;
	DECLARE @numOfRecords INT;
	DECLARE @concatstring NVARCHAR(max);
	DECLARE @customerId NVARCHAR(max);
	DECLARE @execStmt NVARCHAR(max);

	IF @_contractId = '' BEGIN
		SET @_contractId = '%';
    END 

    set @numOfRecords = LEN(@_coreCustomerId) - LEN(REPLACE(@_coreCustomerId, ',', '')) + 1;
    set @concatstring = '''' + [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_coreCustomerId, ',', @index1), ',', -1 ) + '''';
        customerId:  WHILE 1=1 BEGIN
        set @index1 = @index1 + 1;
        IF @index1 = @numOfRecords + 1 BEGIN
            BREAK;
        end
        else begin
            set @customerId = ([${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_coreCustomerId, ',', @index1), ',', -1 ));
            set @concatstring = @concatstring+ ','''+@customerId+'''';
        END 
    END 
	set @execStmt = 'select sg.signatoryGroupId, sg.signatoryGroupName, sg.signatoryGroupDescription, cc.coreCustomerId, cc.coreCustomerName,
	c.id contractId, c.name contractName, sg.createdby, sg.createdts, sg.lastmodifiedts, sg.softdeleteflag ,
   ( select count(*) from [${dbxschemaname}].customersignatorygroup cs1 where cs1.signatoryGroupId=sg.signatoryGroupId  group by cs1.signatoryGroupId ) as noOfUsers
	from [${dbxschemaname}].contract c 
	left join [${dbxschemaname}].contractcorecustomers cc on c.id=cc.contractId
	left join [${dbxschemaname}].signatorygroup sg on c.id=sg.contractId and cc.coreCustomerId=sg.coreCustomerId
	where c.id LIKE '''+ @_contractId + '''';
	IF @_coreCustomerId != '' BEGIN
		set @execStmt = @execStmt + ' and cc.coreCustomerId IN (' + @concatstring +' )';
    END 
	exec(@execStmt);
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_signatorygroup_details_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroup_details_proc](
  @_signatoryGroupId AS  nvarchar(50)
) AS
BEGIN
	select [signatorygroup].[signatoryGroupId] as [signatoryGroupId],
	[signatorygroup].[signatoryGroupName] as [signatoryGroupName],
	[signatorygroup].[signatoryGroupDescription] as [signatoryGroupDescription],
	[signatorygroup].[coreCustomerId] as [coreCustomerId],
	[contractcorecustomers].[coreCustomerName] as [coreCustomerName],
	[signatorygroup].[createdts] as [createdts],
	[signatorygroup].[createdby] as [createdby],
	[signatorygroup].[lastmodifiedts] as [lastmodifiedts],
	[customersignatorygroup].[customerSignatoryGroupId] as [customerSignatoryGroupId],
	[customersignatorygroup].[customerId] as [customerId],
	[customerbasicinfo_view].[Name] AS [fullName],
	[customerbasicinfo_view].[Username] AS [userName],
	[customerbasicinfo_view].[Customer_Role] as [customerRole],
	[customersignatorygroup].[createdts] as [signatoryaddedts]
	  from [${dbxschemaname}].[signatorygroup]  
	  left join [${dbxschemaname}].[customersignatorygroup]  on [signatorygroup].[signatoryGroupId] = [customersignatorygroup].[signatoryGroupId]
	  left join [${dbxschemaname}].[contractcorecustomers]  on [contractcorecustomers].[coreCustomerId] = [signatorygroup].[coreCustomerId]
	  left join [${dbxschemaname}].[customerbasicinfo_view]  on [customerbasicinfo_view].[Customer_id] = [customersignatorygroup].[customerId]
	  where [signatorygroup].[signatoryGroupId] = @_signatoryGroupId;  
END
GO


DROP procedure IF EXISTS [${dbxschemaname}].[signatorygroup_update_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[signatorygroup_update_proc]
   @_sigGroupValues  nvarchar(max), 
   @_newSigValues nvarchar(max),
   @_deleteSigValues  nvarchar(max)
AS
BEGIN
	DECLARE @index1 int = 0
	DECLARE @length1 bigint
	DECLARE @signatoriesComma nvarchar(max)
	DECLARE @custId nvarchar(100)
	DECLARE @sigGroupId nvarchar(max)
	DECLARE @SigGroupName varchar(max)
	DECLARE @sigGroupDes nvarchar(max)
	DECLARE @signatory nvarchar(max)
	DECLARE @query nvarchar(max)
	DECLARE @sigCreatedBy nvarchar(max)
	set @sigGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', 1 );
	set @SigGroupName = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', 2 ),';',-1);
	set @SigGroupDes = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', 3 ),';',-1);
	set @sigCreatedBy = [${dbxschemaname}].SUBSTRING_INDEX(@_sigGroupValues, ';', -1 );
	set @SigGroupId = REPLACE(@SigGroupId, '"', '''');
	IF @SigGroupName IS NOT NULL AND @SigGroupName != ''
	BEGIN
		set @SigGroupName = REPLACE(@SigGroupName, '"', ''''); 
		set @sigCreatedBy = REPLACE(@sigCreatedBy, '"', '''');
		set @query = ('UPDATE [${dbxschemaname}].signatorygroup SET signatorygroup.signatoryGroupName = ')+@SigGroupName+(' , signatorygroup.lastmodifiedts = CURRENT_TIMESTAMP, signatorygroup.modifiedby = ')+@sigCreatedBy+(' WHERE signatorygroup.signatoryGroupId = ')+@sigGroupId+('');
		execute (@query);
	END
	IF @sigGroupDes IS NOT NULL AND @sigGroupDes != ''
	BEGIN
		set @sigGroupDes = REPLACE(@sigGroupDes, '"', ''''); 
		set @sigCreatedBy = REPLACE(@sigCreatedBy, '"', '''');
		set @query = ('UPDATE [${dbxschemaname}].signatorygroup SET signatorygroup.signatoryGroupDescription = ')+@sigGroupDes+(' , signatorygroup.lastmodifiedts = CURRENT_TIMESTAMP, signatorygroup.modifiedby = ')+@sigCreatedBy+(' WHERE signatorygroup.signatoryGroupId = ')+@sigGroupId+('');
		execute (@query);
	END

	IF @_newSigValues IS NOT NULL AND @_newSigValues != ''
	BEGIN
		set @length1 = LEN(@_newSigValues) - LEN(REPLACE(@_newSigValues, ',', ''))+1;
		set @index1 = 0;
		WHILE (1 = 1)
		BEGIN
			set @index1 = @index1 + 1;
			IF @index1 = @length1 + 1 
				BREAK
			ELSE
			BEGIN
				set @signatory = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_newSigValues, ',', @index1), ',', -1);
				set @signatoriesComma = REPLACE(@signatory, ';', ',');
				set @signatoriesComma = REPLACE(@signatoriesComma, '"', ''''); 
				set @query = ('INSERT INTO [${dbxschemaname}].customersignatorygroup(customersignatorygroup.customerSignatoryGroupId, customersignatorygroup.signatoryGroupId, customersignatorygroup.customerId, customersignatorygroup.createdby) values (')+@signatoriesComma+(')');
				execute (@query);
				CONTINUE
			END
		END
	END
 
	IF @_deleteSigValues IS NOT NULL AND @_deleteSigValues != ''
	BEGIN
		set @length1 = LEN(@_deleteSigValues) - LEN(REPLACE(@_deleteSigValues, ',', ''))+1;
		set @index1 = 0;
		WHILE (1 = 1)
		BEGIN
			set @index1 = @index1 + 1
			IF @index1 = @length1 + 1 
				BREAK
			ELSE
			BEGIN
				set @signatory = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_deleteSigValues, ',', @index1), ',', -1);
				set @sigGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@signatory, ';', 1 );
				set @custId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@signatory, ';', 2 ),';',-1);
				set @sigGroupId = REPLACE(@sigGroupId, '"', '''');
				set @custId = REPLACE(@custId, '"', '''');
				set @query = ('DELETE FROM [${dbxschemaname}].customersignatorygroup WHERE customersignatorygroup.signatoryGroupId =')+@sigGroupId+('AND customersignatorygroup.customerId=')+@custId+('')
				execute (@query);
				CONTINUE
			END
		END
	END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalgroups_for_pendingtxn_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalgroups_for_pendingtxn_proc]
AS
BEGIN
	
	SELECT [signatorygroup].[signatoryGroupId] 
	FROM [${dbxschemaname}].[signatorygroup] 
	WHERE [${dbxschemaname}].FIND_IN_SET([signatorygroup].[signatoryGroupId],
	(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].[pendingGroupList],']',''),'[',''),'"',''),',')
		FROM [${dbxschemaname}].[signatorygrouprequestmatrix] WHERE 
		[signatorygrouprequestmatrix].[isApproved] = 'false')) > 0 ;
		
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatorygroups_in_approvalrule_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroups_in_approvalrule_proc]
AS
BEGIN
	
	SELECT [signatorygroup].[signatoryGroupId] 
	FROM [${dbxschemaname}].[signatorygroup] 
	WHERE [${dbxschemaname}].FIND_IN_SET([signatorygroup].[signatoryGroupId],
	(SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygroupmatrix].[groupList],']',''),'[',''),'"',''),',')
		FROM [${dbxschemaname}].[signatorygroupmatrix] WHERE 
		[signatorygroupmatrix].[softdeleteflag] = 'false')) > 0 ;
		
END
GO
ALTER TABLE [${dbxschemaname}].[signatorygroup] ADD [status] bit DEFAULT 1;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_signatorygroup_for_user_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[update_signatorygroup_for_user_proc]
@_coreCustomerId nvarchar(50),
@_contractId nvarchar(50),
@_customerId nvarchar(max),
@_signatorygroupId nvarchar(50) 
AS
BEGIN
DECLARE @index1 INTEGER = 0;
DECLARE @length INTEGER = 0;
DECLARE @cusrecord nvarchar(max);
DECLARE @cus varchar(max);
set @length = LEN(@_customerId) - LEN(REPLACE(@_customerId, ',', '')) + 1;

WHILE (1 = 1)
BEGIN
set @index1 = @index1 + 1;
IF @index1 = @length + 1 
	BREAK
ELSE
	BEGIN
	set @cusrecord = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX(@_customerId, ',', @index1), ',', -1 );
	set @cus = REPLACE(@cusrecord, ';', ',');
	set @cus = REPLACE(@cus,'"','''');
	
	DELETE from [${dbxschemaname}].[customersignatorygroup] where customerId=@cus and signatoryGroupId 
	in (select signatoryGroupId from [${dbxschemaname}].[signatorygroup] where coreCustomerId=@_coreCustomerId and contractId=@_contractId);

	IF @_signatorygroupId != ''
    BEGIN
		INSERT INTO [${dbxschemaname}].[customersignatorygroup](customerSignatoryGroupId, signatoryGroupId, customerId, createdby) values (NEWID(),@_signatorygroupId,@cus,@cus);
    END

	CONTINUE
	END
END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_signatorygroup_customer_details_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_signatorygroup_customer_details_proc]
 @_signatoryGroupId nvarchar(50) 
AS

BEGIN
	SELECT 
		[customer].[id] AS userId,
		[customer].[isCombinedUser] AS isCombinedUser,
		[customer].[UserName] AS userName,
		[customer].[FirstName] AS firstName,
		[customer].[LastName] AS lastName,
		[membergroup].[Name] AS role,
		[customerimage].[UserImage] AS userImage
	FROM 
		[${dbxschemaname}].[customersignatorygroup] 
	LEFT JOIN [${dbxschemaname}].[customer] ON ([customer].[id] = [customersignatorygroup].[customerId])
	LEFT JOIN [${dbxschemaname}].[signatorygroup] ON ([signatorygroup].[signatoryGroupId] =  [customersignatorygroup].[signatoryGroupId])
	LEFT JOIN [${dbxschemaname}].[customergroup] ON ([customergroup].[Customer_id] = [customersignatorygroup].[customerId] AND [customergroup].[coreCustomerId] = [signatorygroup].[coreCustomerId])
	LEFT JOIN [${dbxschemaname}].[membergroup] ON ([membergroup].[id] = [customergroup].[Group_id])
	LEFT JOIN [${dbxschemaname}].[customerimage] ON ([customerimage].[Customer_id] = [customer].[id])
		WHERE 
			[customersignatorygroup].[signatoryGroupId] = @_signatoryGroupId ;   
			
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc]
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueueforgroup_proc]
GO
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
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
	IF (@combinedIds IS NULL OR @combinedIds = '')
		SET @combinedIds = ''
	ELSE
		SET @combinedIds = @combinedIds

	IF (@_transactionIds IS NULL OR @_transactionIds = '')
		SET @_transactionIds = ''
	ELSE
		SET @_transactionIds = @_transactionIds
	
	IF (@_requestIds IS NULL OR @_requestIds = '')
		SET @_requestIds = ''
	ELSE
		SET @_requestIds = @_requestIds

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''

	  SET @customerGroupIds = ( SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)),',') FROM [${dbxschemaname}].customersignatorygroup WHERE [${dbxschemaname}].FIND_IN_SET(customersignatorygroup.customerId, @combinedIds) <> 0);
        IF @customerGroupIds IS NULL
         SET @customerGroupIds = ''
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') = 0 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END
      SET @approvalRequestIds = 
         (  
         SELECT String_agg(CAST(requestapprovalmatrix.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].requestapprovalmatrix.isGroupRule = 0 
		 AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId))
			OR (approvalrule.numberOfApprovals != -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
         )

		IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END
	  
	  SET @groupIds = @customerGroupIds;
        do_this: WHILE 1=1 BEGIN
			SET @strLen = LEN(@groupIds);
 			SET @reqIds = ( SELECT Distinct (String_agg( CAST([signatorygrouprequestmatrix].[requestId] as nvarchar(max)) , ',')) FROM [${dbxschemaname}].signatorygrouprequestmatrix 
					WHERE NOT [${dbxschemaname}].FIND_IN_SET(signatorygrouprequestmatrix.requestId, @approvalRequestIds) > 0 AND signatorygrouprequestmatrix.isApproved = '0' AND [${dbxschemaname}].FIND_IN_SET( [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1) ,REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].[pendingGroupList],'[',''),']',''),' ','')) > 0 
					AND [signatorygrouprequestmatrix].[requestId] NOT IN (@alreadyApprovedIds) );
					
				IF @reqIds IS NULL
				  BEGIN
					SET @reqIds = ''
				  END
				  
				if (@approvalRequestIds = '' OR @approvalRequestIds IS NULL) 
					SET @approvalRequestIds = @reqIds; 
				else  
					SET @approvalRequestIds = CONCAT(@approvalRequestIds, CONCAT(',',@reqIds) );
			SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
			SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
			IF LEN(@groupIds) <= 0 BEGIN
			  BREAK;
			END 
	  END;
	
     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

     
      SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0)
      IF @monetaryActions IS NULL
        BEGIN
          SET @monetaryActions = ''
        END

	DECLARE @companyRequestIds nvarchar(max)
      SET @companyRequestIds = ( SELECT String_agg(bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @monetaryActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END

	DECLARE @requestIds nvarchar(max)
	IF @_requestIds = ''
          SET @requestIds = @companyRequestIds
    ELSE
		SET @requestIds = @_requestIds

	DECLARE @query nvarchar(max)
	IF @_transactionIds = '' 
	SET @query = '[${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),''' + @requestIds + ''') > 0 '
	ELSE
	SET @query = '[${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.transactionId AS nvarchar(max)),''' + @_transactionIds + ''') > 0 AND [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.featureActionId AS nvarchar(max)),''' + @monetaryActions + ''') > 0 '
	
   DECLARE @select_statement nvarchar(max)
	 SET @select_statement =
			'SELECT 
             distinct bbrequest.requestId,
             bbrequest.transactionId,
             bbrequest.status,
			 bbrequest.featureActionId,
			 bbrequest.isGroupMatrix,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
            END) as amICreator,
         
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from [${dbxschemaname}].bbactedrequest where bbactedrequest.action = ''Approved'' AND  bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0) 
							as receivedApprovals,
					CASE bbrequest.isGroupMatrix
					WHEN 0 [${dbxschemaname}].LEASTINT(
								(SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM [${dbxschemaname}].requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
								, 
								CASE approvalrule.numberOfApprovals
									WHEN -1 THEN (SELECT COUNT(*) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
									WHEN NULL THEN 0
									WHEN '''' THEN 0
									ELSE approvalrule.numberOfApprovals
								END		 
							) 
					ELSE NULL
					END as requiredApprovals
				FROM
				 [${dbxschemaname}].bbrequest
				LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN [${dbxschemaname}].approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ' + @query ;
	
IF @select_statement IS NULL
	GOTO MAINLABEL$leave
exec(@select_statement)

End
MAINLABEL$leave: 
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[account_action_approvers_proc];
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
 SELECT @customerIdList = STRING_AGG( CAST(temp.Customer_id as nvarchar(max)), ',') from (SELECT DISTINCT [customeraction].Customer_id as Customer_id from [${dbxschemaname}].[customeraction]
			where
				[${dbxschemaname}].[customeraction].isAllowed = '0'
				and [${dbxschemaname}].[customeraction].Action_id = @_approvalActionList
				and [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].[customeraction].[Account_id], @_accountIds) > 0
				and [${dbxschemaname}].[customeraction].contractId = @_contractId
				and [${dbxschemaname}].[customeraction].coreCustomerId = @_cif ) as temp;

SET @customerIdList = CASE WHEN @customerIdList is null THEN  '' ELSE  @customerIdList END;
SET @NumberOfAccounts = LEN(cast(@_accountIds as nvarchar(max))) - LEN(REPLACE(cast(@_accountIds as nvarchar(max)), ',', '')) + 1;

 select @customerIdListWithNoAccountAccess = STRING_AGG(CAST(temp.Customer_id as nvarchar(max)), ',') from (SELECT DISTINCT tempCustomers.Customer_id from (SELECT Customer_id, count(Account_id) as countAccounts
				from [${dbxschemaname}].[customeraccounts]
				where [${dbxschemaname}].FIND_IN_SET(Account_id, @_accountIds) > 0
				group by [customeraccounts].Customer_id) as tempCustomers
				where tempCustomers.countAccounts != @NumberOfAccounts) as temp;
										  
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
                        approvalmatrix.upperlimit
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
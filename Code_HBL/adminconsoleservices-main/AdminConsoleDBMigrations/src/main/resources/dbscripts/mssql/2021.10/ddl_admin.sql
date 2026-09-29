
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[group_features_actions_view]; 
GO

CREATE VIEW [${dbxschemaname}].[group_features_actions_view] AS
    SELECT 
        [${dbxschemaname}].[groupactionlimit].[Group_id] AS [Group_id],
        [${dbxschemaname}].[groupactionlimit].[Action_id] AS [Action_id],
        [${dbxschemaname}].[groupactionlimit].[LimitType_id] AS [LimitType_id],
		[${dbxschemaname}].[groupactionlimit].[value] AS [value],
        [${dbxschemaname}].[groupactionlimit].[id] AS [groupactionlimit_id],
        [${dbxschemaname}].[groupactionlimit].[softdeleteflag] AS [softdelete],
		[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
        [${dbxschemaname}].[membergroup].[Name] AS [Group_name],
        [${dbxschemaname}].[membergroup].[Description] AS [Group_description],
        [${dbxschemaname}].[featureaction].[name] AS [Action_name],
        [${dbxschemaname}].[featureaction].[description] AS [Action_description],
        [${dbxschemaname}].[featureaction].[Type_id] AS [Action_Type_id],
        [${dbxschemaname}].[featureaction].[Feature_id] AS [Feature_id],
        [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
        [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
        [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
        [${dbxschemaname}].[featureaction].[DisplaySequence] AS [Action_displaysequence],
        [${dbxschemaname}].[featureaction].[dependency] AS [Action_dependency],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
		[${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
		[${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
		[${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
		[${dbxschemaname}].[feature].[name] AS [featureName],
        [${dbxschemaname}].[feature].[name] AS [Feature_name],
        [${dbxschemaname}].[feature].[description] AS [Feature_description],
        [${dbxschemaname}].[feature].[Type_id] AS [Feature_Type_id],
        [${dbxschemaname}].[feature].[Status_id] AS [Feature_Status_id],
        [${dbxschemaname}].[feature].[DisplaySequence] AS [Feature_displaysequence],
        [${dbxschemaname}].[feature].[isPrimary] AS [Feature_isPrimary]
    FROM
        (((((([${dbxschemaname}].[groupactionlimit]
        LEFT JOIN [${dbxschemaname}].[membergroup] ON (([${dbxschemaname}].[membergroup].[id] = [${dbxschemaname}].[groupactionlimit].[Group_id])))
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[groupactionlimit].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
		LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))	
		LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [limitgroup].[id])))
		LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [actionlevel].[id])))
        ORDER BY [${dbxschemaname}].[feature].[name] OFFSET 0 ROWS;
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_received] ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_messages_received]; 
GO

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_received]  

   @from_date varchar(19),
   @_todate varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @queryStatement nvarchar(max)
	  
      SET @queryStatement = 'SELECT count(id) messages_received_count FROM [${dbxschemaname}].requestmessage where createdby not in ( select Username from [${dbxschemaname}].systemuser)'
   
      IF (@from_date <> '' AND @from_date <> '')
      SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '),120),'''') + (' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@_todate,11,0,' '),120),'''')
	  

      IF (@category_id <> '')
      SET @queryStatement = (@queryStatement) + ' and CustomerRequest_id in (select id  from [${dbxschemaname}].customerrequest where RequestCategory_id = ' + ((QUOTENAME((@category_id), '''')))
         

      IF @csr_name <> ''
      
         SET @queryStatement = (@queryStatement) + (' and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	    EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_sent] ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_messages_sent]; 
GO

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_sent]  

   @from_date varchar(19),
   @_todate varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT count(id) messages_sent_count FROM [${dbxschemaname}].requestmessage where createdby in (select Username from
	  [${dbxschemaname}].systemuser) '
 

      IF @from_date <> '' AND @from_date <> ''
      
         SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120),'''') + (N' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@_todate,11,0,' '), 120),'''')
      
      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (' and CustomerRequest_id in (select id from [${dbxschemaname}].customerrequest where RequestCategory_id = ') + ((QUOTENAME((@category_id), ''''))) + ' )'
         
      IF @csr_name <> ''
         
         SET @queryStatement = (@queryStatement) + ('  and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	     EXEC(@queryStatement)
   END

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_averageage] ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_threads_averageage]; 
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_averageage]  

   @from_date varchar(19),
   @_todate varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)
	  
      SET @queryStatement = N' select'+N' AVG(thread_life_records.thread_life) threads_averageage_count '+N'  from'+N'  ('+N'  select'+N'   DATEDIFF(day, MIN(createdts), MAX(createdts)) thread_life '+N' from'+N'  [${dbxschemaname}].requestmessage '+N'  where'+N' 1=1 '
  
      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + (N' '+N'  and createdts >= ') + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@_todate,11,0,' '), 120), ''''))

      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (N' '+N'  and CustomerRequest_id in '+N' ('+N' select'+N' id '+N' from'+N'  [${dbxschemaname}].customerrequest '+N'   where'+N' RequestCategory_id = ') + ((QUOTENAME((@category_id), ''''))) + (N'   )'+N'  ')
       
      IF @csr_name <> ''
        
         SET @queryStatement = (@queryStatement) + (N' '+N'  and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
       
      SET @queryStatement = (@queryStatement) + (N' '+N' group by'+N'  CustomerRequest_id) thread_life_records')
	  EXEC(@queryStatement)

   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_new] ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_threads_new]; 
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_new]  
   @from_date varchar(19),
   @_todate varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @queryStatement nvarchar(max)

      SET @queryStatement = N' SELECT'+N' count(id) threads_new_count '+N' from'+N' [${dbxschemaname}].customerrequest '+N' where'+N' Status_id = ''SID_OPEN'''

      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + (N' '+N' and createdts >= ') + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@_todate,11,0,' '), 120), ''''))
      
      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (N' '+N' and RequestCategory_id = ') + ((QUOTENAME((@category_id), '''')))
       
      IF @csr_name <> ''
       
         SET @queryStatement = (@queryStatement) + (N' '+N' and AssignedTo = ') + ((QUOTENAME((@csr_name), '''')))
         EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_threads_resolved] ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_threads_resolved]; 
GO
/*
*   SSMA informational messages:
*   M2SS0003: The following SQL clause was ignored during conversion:
*   DEFINER = `root`@`localhost`.
*/

CREATE PROCEDURE [${dbxschemaname}].[reports_threads_resolved]  
   @from_date varchar(19),
   @_todate varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)

AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
	  DECLARE @queryStatement nvarchar(max)

      SET @queryStatement ='SELECT count(id) threads_resolved_count from [${dbxschemaname}].customerrequest where Status_id = ''SID_RESOLVED'''
  
      IF @from_date <> '' AND @from_date <> ''

         SET @queryStatement = (@queryStatement) + ' and createdts >= ' + (QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120), '''')) + (N' and createdts <= ') + (QUOTENAME(convert(datetime,STUFF(@_todate,11,0,' '), 120),''''))
         
      IF @category_id <> ''
       
         SET @queryStatement = (@queryStatement) + (N' '+N' and RequestCategory_id = ') + ((QUOTENAME((@category_id), '''')))
        
      IF @csr_name <> ''
        
         SET @queryStatement = (@queryStatement) + (N' '+N' and AssignedTo = ') + ((QUOTENAME((@csr_name), '''')))
        
		 EXEC(@queryStatement)
   END
GO
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[internalfeature];
GO
CREATE TABLE [${dbxschemaname}].[internalfeature] (
 [id] nvarchar(255) NOT NULL,
 [name] varchar(100) NOT NULL,
 [description] varchar(400) DEFAULT NULL,
 [Status_id] nvarchar(50) DEFAULT 'SID_FEATURE_ACTIVE',
 [DisplaySequence] int DEFAULT NULL,
 [isPrimary] smallint NOT NULL DEFAULT '0',
 [createdby] varchar(50) DEFAULT NULL,
 [modifiedby] varchar(50) DEFAULT NULL,
 [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [softdeleteflag] smallint NOT NULL DEFAULT '0',
 PRIMARY KEY ([id])
);
GO
CREATE INDEX [FK_feature_status_id_idx] ON [${dbxschemaname}].[internalfeature] ([Status_id]);
GO
CREATE TABLE [${dbxschemaname}].[internalfeatureaction] (
 [id] varchar(255) NOT NULL,
 [Feature_id] nvarchar(255) DEFAULT NULL,
 [name] varchar(100) DEFAULT NULL,
 [description] varchar(300) DEFAULT NULL,
 [isPrimary] smallint NOT NULL DEFAULT '0',
 [DisplaySequence] int DEFAULT NULL,
 [dependency] varchar(255) DEFAULT NULL,
 [status] nvarchar(50) DEFAULT 'SID_ACTION_ACTIVE',
 [accesspolicyId] varchar(50) DEFAULT NULL,
 [approveFeatureAction] varchar(255) DEFAULT NULL,
 [isApprovalAction] smallint NOT NULL DEFAULT '0',
 [createdby] varchar(50) DEFAULT NULL,
 [modifiedby] varchar(50) DEFAULT NULL,
 [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [softdeleteflag] smallint NOT NULL DEFAULT '0',
 PRIMARY KEY ([id]),
 CONSTRAINT [FK_internalfeatureaction_feature_id] FOREIGN KEY ([Feature_id]) REFERENCES [${dbxschemaname}].internalfeature ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT [FK_internalfeatureaction_accesspolicyId] FOREIGN KEY ([accesspolicyId]) REFERENCES [${dbxschemaname}].accesspolicy ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [FK_internalfeatureaction_feature_id_idx] ON [${dbxschemaname}].[internalfeatureaction] ([Feature_id]);
CREATE INDEX [FK_internalfeatureaction_status] ON [${dbxschemaname}].[internalfeatureaction] ([status]);
CREATE INDEX [FK_internalfeatureaction_accesspolicyId] ON [${dbxschemaname}].[internalfeatureaction] ([accesspolicyId]);
GO

CREATE TABLE [${dbxschemaname}].[internalfeaturedisplaynamedescription] (
 [Feature_id] nvarchar(255) NOT NULL,
 [Locale_id] nvarchar(10) NOT NULL,
 [displayName] varchar(max) NOT NULL,
 [displayDescription] varchar(max) NOT NULL,
 [createdby] varchar(50) DEFAULT NULL,
 [modifiedby] varchar(50) DEFAULT NULL,
 [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [softdeleteflag] smallint NOT NULL DEFAULT '0',
 PRIMARY KEY ([Feature_id],[Locale_id]),
 CONSTRAINT [FK_internalfeaturedisplaynamedescription_Feature_id] FOREIGN KEY ([Feature_id]) REFERENCES [${dbxschemaname}].internalfeature ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT [FK_internalfeaturedisplaynamedescription_locale] FOREIGN KEY ([Locale_id]) REFERENCES [${dbxschemaname}].locale ([Code]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO
CREATE INDEX [FK_internalfeaturedisplaynamedescription_locale_idx] ON [${dbxschemaname}].[internalfeaturedisplaynamedescription] ([Locale_id]);
GO
CREATE TABLE [${dbxschemaname}].[internalactiondisplaynamedescription] (
 [Action_id] varchar(255) NOT NULL,
 [Locale_id] nvarchar(10) NOT NULL,
 [displayName] varchar(100) NOT NULL,
 [displayDescription] varchar(300) NOT NULL,
 [createdby] varchar(50) DEFAULT NULL,
 [modifiedby] varchar(50) DEFAULT NULL,
 [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [softdeleteflag] smallint NOT NULL DEFAULT '0',
 PRIMARY KEY ([Action_id],[Locale_id])
,
 CONSTRAINT [FK_internalactiondisplaynamedescription_Action_id] FOREIGN KEY ([Action_id]) REFERENCES [${dbxschemaname}].internalfeatureaction ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
 CONSTRAINT [FK_internalactiondisplaynamedescription_Locale_id] FOREIGN KEY ([Locale_id]) REFERENCES [${dbxschemaname}].locale ([Code]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [FK_internalactiondisplaynamedescription_Locale_id] ON [${dbxschemaname}].[internalactiondisplaynamedescription] ([Locale_id]);
GO

CREATE TABLE [${dbxschemaname}].[internalactiondependentactions] (
 [actionId] varchar(255) NOT NULL,
 [dependentactionId] varchar(255) NOT NULL,
 [featureId] nvarchar(255) NOT NULL,
 [createdby] varchar(50) DEFAULT NULL,
 [modifiedby] varchar(50) DEFAULT NULL,
 [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
 PRIMARY KEY ([actionId],[dependentactionId]),
 CONSTRAINT [FK_internalactiondependentactions_actionId] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].internalfeatureaction ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT [FK_internalactiondependentactions_dependentactionId] FOREIGN KEY ([dependentactionId]) REFERENCES [${dbxschemaname}].internalfeatureaction ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
 CONSTRAINT [FK_internalactiondependentactions_feature] FOREIGN KEY ([featureId]) REFERENCES [${dbxschemaname}].internalfeature ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [FK_internalactiondependentactions_dependentactionId] ON [${dbxschemaname}].[internalactiondependentactions] ([dependentactionId]);
CREATE INDEX [FK_internalactiondependentactions_feature] ON [${dbxschemaname}].[internalactiondependentactions] ([featureId]);


GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalusers_features_view];
GO
CREATE  VIEW [${dbxschemaname}].[internalusers_features_view] AS
    SELECT 
        [internalfeature].[id] AS id,
        [internalfeature].[name] AS name,
        [internalfeature].[description] AS description,
        [internalfeature].[Status_id] AS Status_id,
		[internalfeature].[DisplaySequence] AS displaySequence,
		[internalfeature].[isPrimary] AS isPrimary,
        [internalfeaturedisplaynamedescription].[Locale_id] AS languageId,
        [internalfeaturedisplaynamedescription].[displayName] AS displayName,
        [internalfeaturedisplaynamedescription].[displayDescription] AS displayDescription,
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[internalfeatureaction].[id])
            FROM
                [${dbxschemaname}].internalfeatureaction
            WHERE
                ([internalfeature].[id] = [internalfeatureaction].[Feature_id])) AS numberOfActions
    FROM
        ([${dbxschemaname}].internalfeature
        LEFT JOIN [${dbxschemaname}].internalfeaturedisplaynamedescription ON (([internalfeaturedisplaynamedescription].[Feature_id] = [internalfeature].[id]))) 
        ORDER BY [${dbxschemaname}].[internalfeature].[name] OFFSET 0 ROWS;

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalusers_dependentactions_view];
GO
CREATE VIEW [${dbxschemaname}].[internalusers_dependentactions_view] AS
    SELECT 
        [internalactiondependentactions].[actionId] AS actionId,
        [internalactiondependentactions].[dependentactionId] AS dependentactionId,
        [internalactiondependentactions].[featureId] AS featureId,
		[internalfeatureaction].[name] AS actionName,
        [internalfeature].[name] AS featureName
    FROM
        (([${dbxschemaname}].internalactiondependentactions
        LEFT JOIN [${dbxschemaname}].internalfeatureaction ON (([internalactiondependentactions].[dependentactionId] = [internalfeatureaction].[id])))
        LEFT JOIN [${dbxschemaname}].internalfeature ON (([internalactiondependentactions].[featureId] = [internalfeature].[id]))) ;

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalusers_actions_view];
GO
CREATE VIEW [${dbxschemaname}].[internalusers_actions_view] AS
    SELECT 
        [internalfeatureaction].[id] AS actionId,
        [internalfeatureaction].[Feature_id] AS featureId,
        [internalfeatureaction].[name] AS actionName,
        [internalfeature].[name] AS featureName,
        [internalfeature].[Status_id] AS featureStatus,
        [internalfeature].[description] AS featureDescription,
        [internalfeatureaction].[description] AS actionDescription,
        [internalfeatureaction].[isPrimary] AS isPrimary,
		[internalfeatureaction].[accesspolicyId] AS accesspolicyId,
		[internalfeatureaction].[status] AS actionStatus,
        [internalfeatureaction].[DisplaySequence] AS actionDisplaySequence,
		[internalactiondisplaynamedescription].[Locale_id] AS localeId,
        [internalactiondisplaynamedescription].[displayName] AS displayName,
        [internalactiondisplaynamedescription].[displayDescription] AS displayDescription,
		[internalusers_dependentactions_view].[dependentactionId] AS dependentactionId,
        [internalusers_dependentactions_view].[featureId] AS dependentFeatureId,
        [internalusers_dependentactions_view].[actionName] AS dependentActionName,
        [internalusers_dependentactions_view].[featureName] AS dependentFeatureName
		
    FROM
        (((([${dbxschemaname}].internalfeatureaction
        LEFT JOIN [${dbxschemaname}].internalfeature ON (([internalfeature].[id] = [internalfeatureaction].[Feature_id])))
        LEFT JOIN [${dbxschemaname}].internalactiondisplaynamedescription ON (([internalactiondisplaynamedescription].[Action_id] = [internalfeatureaction].[id])))
        LEFT JOIN [${dbxschemaname}].accesspolicy ON (([internalfeatureaction].[accesspolicyId] = [accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].internalusers_dependentactions_view ON (([internalfeatureaction].[id] = [internalusers_dependentactions_view].[actionId]))) ;

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalactiondependency_view];
GO
CREATE  VIEW [${dbxschemaname}].[internalactiondependency_view] AS
    SELECT 
        [internalactiondependentactions].[dependentactionId] AS actionName,
        [internalactiondependentactions].[actionId] AS dependencyAction,
        [internalactiondependentactions].[featureId] AS featureId,
        [internalfeatureaction].[status] AS actionStatus,
        [internalfeature].[Status_id] AS featureStatus
    FROM
        (([${dbxschemaname}].internalactiondependentactions
        LEFT JOIN [${dbxschemaname}].internalfeatureaction ON (([internalactiondependentactions].[dependentactionId] = [internalfeatureaction].[id])))
        LEFT JOIN [${dbxschemaname}].internalfeature ON (([internalactiondependentactions].[featureId] = [internalfeature].[id]))) ;
GO

DROP TABLE IF EXISTS [internalpermission];
GO
CREATE TABLE [${dbxschemaname}].[internalpermission] (
  [id] varchar(50) NOT NULL,
  [Status_id] nvarchar(50) NOT NULL,
  [Parent_id] varchar(50) DEFAULT NULL,
  [Name] varchar(50) NOT NULL,
  [Description] varchar(300) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT [FK_Internalpermission_Internalpermission] FOREIGN KEY ([Parent_id]) REFERENCES [${dbxschemaname}].internalpermission ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [IXFK_Internalpermission_Internalpermission] ON [${dbxschemaname}].internalpermission ([Parent_id]);
GO
CREATE INDEX [IXFK_Internalpermission_Status] ON [${dbxschemaname}].internalpermission ([Status_id]);
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[permissionlegalentity];
GO
CREATE TABLE [${dbxschemaname}].[permissionlegalentity] (
  [permissionId] varchar(50) NOT NULL,
  [legalEntityId] varchar(50) NOT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [modifiedby] varchar(50) DEFAULT NULL,
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  PRIMARY KEY ([permissionId],[legalEntityId]),
  CONSTRAINT [FK_permissionlegalentity_permission] FOREIGN KEY ([permissionId]) REFERENCES [${dbxschemaname}].internalpermission ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [IXFK_permissionlegalentity_legalentity] ON [${dbxschemaname}].permissionlegalentity ([legalEntityId]);
GO
CREATE INDEX [IXFK_permissionlegalentity_permission] ON [${dbxschemaname}].permissionlegalentity ([permissionId]);
GO


GO
DROP TABLE IF EXISTS [${dbxschemaname}].[permissionaction];
GO
CREATE TABLE [${dbxschemaname}].[permissionaction] (
  [id] varchar(50) NOT NULL,
  [permissionId] varchar(50) NOT NULL,
  [actionId] varchar(255) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([id]),
  CONSTRAINT [UNIQUE_permissionaction] UNIQUE  ([permissionId],[actionId]),
  CONSTRAINT [FK_permissionaction_Action] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].internalfeatureaction ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_permissionaction_Permission] FOREIGN KEY ([permissionId]) REFERENCES [${dbxschemaname}].internalpermission ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
) ;
GO
CREATE INDEX [IXFK_permissionaction_permission] ON [${dbxschemaname}].permissionaction ([permissionId]);
GO
CREATE INDEX [IXFK_permissionaction_Action] ON [${dbxschemaname}].permissionaction ([actionId]);
GO
CREATE INDEX [IDX_permissionaction_permission_actionid] ON [${dbxschemaname}].permissionaction ([permissionId],[actionId]);
GO
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[userlegalentity];
GO
CREATE TABLE [${dbxschemaname}].[userlegalentity] (
  [userId] varchar(50) NOT NULL,
  [legalEntityId] varchar(50) NOT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [modifiedby] varchar(50) DEFAULT NULL,
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  PRIMARY KEY ([userId],[legalEntityId])
) ;
GO
CREATE INDEX [IXFK_userlegalentity_legalentity] ON [${dbxschemaname}].userlegalentity ([legalEntityId]);
GO
CREATE INDEX [IXFK_userlegalentity_user] ON [${dbxschemaname}].userlegalentity ([userId]);
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[userbranch];
GO
CREATE TABLE [${dbxschemaname}].[userbranch] (
  [userId] varchar(50) NOT NULL,
  [branchId] varchar(50) NOT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [modifiedby] varchar(50) DEFAULT NULL,
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  PRIMARY KEY ([userId],[branchId])
) ;
GO
CREATE INDEX [IXFK_userbranch_user] ON [${dbxschemaname}].userbranch ([userId]);
GO
CREATE INDEX [IXFK_userbranch_branch] ON [${dbxschemaname}].userbranch ([branchId]);
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[internalpermission_featureaction_view];
GO
CREATE VIEW [${dbxschemaname}].[internalpermission_featureaction_view] AS
    SELECT 
        [ifa].[Feature_id] AS featureId,
        [pa].[actionId] AS actionId,
        [pa].[permissionId] AS permissionId
    FROM
        (([${dbxschemaname}].permissionaction pa
        JOIN [${dbxschemaname}].internalfeatureaction [ifa] ON ((([pa].[actionId] = [ifa].[id])
            AND ([ifa].[status] = 'SID_ACTION_ACTIVE'))))
        JOIN [${dbxschemaname}].internalfeature [ifes] ON ((([ifes].[id] = [ifa].[Feature_id])
            AND ([ifes].[Status_id] = 'SID_FEATURE_ACTIVE'))));
GO



DROP procedure IF EXISTS [${dbxschemaname}].[internalpermissionaction_delete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[internalpermissionaction_delete_proc](
 @_permissionId VARCHAR(50)
) AS
BEGIN
	DELETE FROM [${dbxschemaname}].[permissionaction] where permissionId = @_permissionId;
END
GO


DROP procedure IF EXISTS [${dbxschemaname}].[permissionlegalentity_delete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[permissionlegalentity_delete_proc](
  @_permissionId VARCHAR(50)
) AS
BEGIN
	DELETE FROM [${dbxschemaname}].[permissionlegalentity] where permissionId = @_permissionId;
END

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[cardaccountrequest]', 'Communication_id';
GO

DROP VIEW IF EXISTS [feature_action_roles_view];
GO
CREATE VIEW [${dbxschemaname}].[feature_action_roles_view] AS
SELECT
[featureaction].[Feature_id] AS feature_code,
[feature_view].[Name] AS feature_name,
[feature_view].[Type_Id] AS feature_type_id,
[feature_view].[Status_Id] AS feature_status_id,
[featureaction].[id] AS action_code,
[featureaction].[name] AS action_name,
[featureaction].[description] AS action_description,
[far].[RoleType_id] AS action_role_type_id,
[featureaction].[Type_id] AS category,
[featureaction].[accesspolicyId] AS accesspolicyId,
[featureaction].[actionlevelId] AS actionlevelId,
[featureaction].[status] AS status,
[actionlimit].[LimitType_id] AS limitType_id,
[actionlimit].[value] AS value
FROM
((([${dbxschemaname}].featureaction
LEFT JOIN [${dbxschemaname}].featureactionroletype [far] ON (([far].[Action_id] = [featureaction].[id])))
LEFT JOIN [${dbxschemaname}].feature_view ON (([feature_view].[Code] = [featureaction].[Feature_id])))
LEFT JOIN [${dbxschemaname}].actionlimit ON (([actionlimit].[Action_id] = [featureaction].[id]))) ;

GO


DROP procedure IF EXISTS [${dbxschemaname}].[approval_matrix_manual_cleanup_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approval_matrix_manual_cleanup_proc](
 @_contractId VARCHAR(max) ,
 @_coreCustomerId VARCHAR(max) 
)
AS 
BEGIN
declare
 @customerAccounts nvarchar(max),
  @customerActions nvarchar(max)
UPDATE [${dbxschemaname}].approvalmatrix set softdeleteflag = '1' where approvalmatrix.coreCustomerId = @_coreCustomerId;

 SET @customerAccounts = (select distinct String_agg(contractaccounts.accountId ,',') from [${dbxschemaname}].contractaccounts WHERE (contractId = @_contractId AND coreCustomerId=@_coreCustomerId));

 SET @customerActions = (select distinct String_agg(actionId,',') from [${dbxschemaname}].contractactionlimit LEFT JOIN [${dbxschemaname}].featureaction ON ([${dbxschemaname}].featureaction.id = [${dbxschemaname}].contractactionlimit.actionId) WHERE (contractId = @_contractId AND coreCustomerId=@_coreCustomerId) AND [${dbxschemaname}].featureaction.approveFeatureAction IS NOT NULL);

Exec [${dbxschemaname}].approvalmatrix_default_create_proc @customerActions, @_contractId, @customerAccounts, @_coreCustomerId;
END

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalpermission_view];
GO
CREATE VIEW [${dbxschemaname}].[internalpermission_view] AS
    SELECT 
        [internalpermission].[id] AS permissionId,
        [internalpermission].[Name] AS permissionName,
        [internalpermission].[Description] AS description,
        [internalpermission].[Status_id] AS status,
		(SELECT COUNT(DISTINCT(id))as numberOfFeatures from [${dbxschemaname}].[internalfeature]) as numberOfFeatures,
			(SELECT String_agg(cast(legalEntityId as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].[permissionlegalentity]) AS [legalEntityIds]
    FROM
        (((([${dbxschemaname}].[internalpermission]
        LEFT JOIN [${dbxschemaname}].[permissionaction] ON (([${dbxschemaname}].[permissionaction].[permissionId] = [${dbxschemaname}].[internalpermission].[id])))
        LEFT JOIN [${dbxschemaname}].[permissionlegalentity] ON (([${dbxschemaname}].[permissionlegalentity].[permissionId] = [${dbxschemaname}].[internalpermission].[id])))
        LEFT JOIN [${dbxschemaname}].[internalfeatureaction] ON (([${dbxschemaname}].[internalfeatureaction].[id] = [${dbxschemaname}].[permissionaction].[actionId])))
        LEFT JOIN [${dbxschemaname}].[internalfeature] ON (([${dbxschemaname}].[internalfeature].[id] = [${dbxschemaname}].[internalfeatureaction].[Feature_id])))
    ;

GO

GO
DROP VIEW IF EXISTS [${dbxschemaname}].[internalpermission_featureaction_view];
GO
CREATE VIEW [${dbxschemaname}].[internalpermission_featureaction_view] AS
    SELECT 
        [ifa].[Feature_id] AS featureId,
        [pa].[actionId] AS actionId,
        [pa].[permissionId] AS permissionId
    FROM
        (([${dbxschemaname}].permissionaction pa
        JOIN [${dbxschemaname}].internalfeatureaction [ifa] ON ((([pa].[actionId] = [ifa].[id])
            AND ([ifa].[status] = 'SID_ACTION_ACTIVE'))))
        JOIN [${dbxschemaname}].internalfeature [ifes] ON ((([ifes].[id] = [ifa].[Feature_id])
            AND ([ifes].[Status_id] = 'SID_FEATURE_ACTIVE')))) ;

GO
CREATE TABLE [${dbxschemaname}].rolepermissionou (
  [roleId] nvarchar(50) NOT NULL,
  [permissionId] varchar(50) NOT NULL,
  [ouId] varchar(50) NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  PRIMARY KEY ([roleId],[permissionId],[ouId])
 ,
  CONSTRAINT [FK_rolepermissionou_Role] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].role ([id]) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT [FK_rolepermissionou_Permission] FOREIGN KEY ([permissionId]) REFERENCES [${dbxschemaname}].internalpermission ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
) ;

CREATE INDEX [IXFK_rolepermissionou_permission] ON [${dbxschemaname}].rolepermissionou ([permissionId]);
CREATE INDEX [IXFK_rolepermissionou_role] ON [${dbxschemaname}].rolepermissionou ([roleId]);
CREATE INDEX [IDX_rolepermissionou_ou] ON [${dbxschemaname}].rolepermissionou ([ouId]);

GO
DROP procedure IF EXISTS [${dbxschemaname}].[rolepermissionou_delete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolepermissionou_delete_proc](
 @_roleId VARCHAR(50)
) AS
BEGIN
	DELETE FROM [${dbxschemaname}].rolepermissionou where rolepermissionou.roleId = @_roleId;
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[servicedefinition_delete_proc];
GO
CREATE  PROCEDURE [${dbxschemaname}].[servicedefinition_delete_proc](
 @_servicedefinitionid VARCHAR(50)
)AS
BEGIN
DELETE FROM [${dbxschemaname}].[groupservicedefinition] WHERE  serviceDefinitionId = @_servicedefinitionid;
DELETE FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE serviceDefinitionId = @_servicedefinitionid;
DELETE FROM [${dbxschemaname}].[userroleservicedefinition]  WHERE serviceDefinitionId = @_servicedefinitionid;
DELETE FROM [${dbxschemaname}].[servicedefinition]  WHERE id = @_servicedefinitionid;
END
GO
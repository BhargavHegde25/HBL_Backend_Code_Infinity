USE [${dbxdbname}]
GO
DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymenttemplatepos];
DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymenttemplate];
CREATE TABLE [${dbxschemaname}].[bulkpaymenttemplate] (
  [templateId] varchar(50) NOT NULL,
  [templateName] varchar(50) NOT NULL,
  [processingMode] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [paymentId] varchar(50) DEFAULT NULL,
  [description] varchar(100) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [paymentDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [scheduledDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [fileId] varchar(50) NULL,
  [reviewedBy] nvarchar(50) DEFAULT NULL,
  [initiatedBy] varchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] bigint DEFAULT NULL,
  [totalAmount] bigint DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [requestId] varchar(50) DEFAULT NULL,
  [paymentStatus] varchar(50) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL, 
  [bulkType] varchar(50) DEFAULT NULL,
  [updateReference] varchar(50) DEFAULT NULL,
  [creditReference] varchar(50) DEFAULT NULL,
  [debitReference] varchar(50) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  
  PRIMARY KEY ([templateId])
 ,
  CONSTRAINT [FK_bulkpaymenttemplate_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymenttemplate_reviewedBy_idx] FOREIGN KEY ([reviewedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymenttemplate_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymenttemplate_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymenttemplate_requestId] ON [${dbxschemaname}].[bulkpaymenttemplate] ([requestId]);
CREATE INDEX [FK_bulkpaymenttemplate_fromAccount] ON [${dbxschemaname}].[bulkpaymenttemplate] ([fromAccount]);
CREATE INDEX [FK_bulkpaymenttemplate_confirmationNumber] ON [${dbxschemaname}].[bulkpaymenttemplate] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymenttemplate_featureActionId] ON [${dbxschemaname}].[bulkpaymenttemplate] ([featureActionId]);
CREATE INDEX [FK_bulkpaymenttemplate_companyId] ON [${dbxschemaname}].[bulkpaymenttemplate] ([companyId]);
CREATE INDEX [FK_bulkpaymenttemplate_reviewedBy] ON [${dbxschemaname}].[bulkpaymenttemplate] ([reviewedBy]);
CREATE INDEX [FK_bulkpaymenttemplate_initiatedBy] ON [${dbxschemaname}].[bulkpaymenttemplate] ([initiatedBy]);
CREATE INDEX [FK_bulkpaymenttemplate_status] ON [${dbxschemaname}].[bulkpaymenttemplate] ([status]);
CREATE INDEX [FK_bulkpaymenttemplate_roleId] ON [${dbxschemaname}].[bulkpaymenttemplate] ([roleId]);
CREATE INDEX [FK_bulkpaymenttemplate_fileId] ON [${dbxschemaname}].[bulkpaymenttemplate] ([fileId]);

GO
CREATE TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] (
  [paymentOrderId] varchar(50) NOT NULL,
  [templateId] varchar(50) DEFAULT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [recipientName] varchar(50) NOT NULL,
  [accountNumber] varchar(50) NOT NULL,
  [bankName] varchar(50) DEFAULT NULL,
  [swift] varchar(50) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [status] varchar(20) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL,
  [amount] bigint DEFAULT NULL,
  [feesPaidBy] varchar(50) DEFAULT NULL,
  [paymentReference] varchar(50) DEFAULT NULL,
  [debitAccountIBAN] varchar(50) DEFAULT NULL,
  [beneficiaryIBAN] varchar(50) DEFAULT NULL,
  [beneficiaryName] varchar(50) DEFAULT NULL,
  [beneficiaryNickName] varchar(50) DEFAULT NULL,
  [beneficiaryAddress] varchar(125) DEFAULT NULL,
  [accountWithBankBIC] varchar(50) DEFAULT NULL,
  [customer] varchar(50) DEFAULT NULL,
  [paymentMethod] varchar(50) DEFAULT NULL,
  [accType] varchar(50) DEFAULT NULL,
  [createdby] nvarchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',

  PRIMARY KEY ([paymentOrderId])
 ,
  CONSTRAINT [FK_bulkpaymenttemplatepos_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymenttemplatepos_createdby_idx] FOREIGN KEY ([createdby]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymenttemplatepos_templateId_idx] FOREIGN KEY ([templateId]) REFERENCES [${dbxschemaname}].[bulkpaymenttemplate] ([templateId])ON DELETE  CASCADE,
  CONSTRAINT [FK_bulkpaymenttemplatepos_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymenttemplatepos_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymenttemplatepos_accountNumber] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([accountNumber]);
CREATE INDEX [FK_bulkpaymenttemplatepos_confirmationNumber] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymenttemplatepos_featureActionId] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([featureActionId]);
CREATE INDEX [FK_bulkpaymenttemplatepos_companyId] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([companyId]);
CREATE INDEX [FK_bulkpaymenttemplatepos_recipientName] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([recipientName]);
CREATE INDEX [FK_bulkpaymenttemplatepos_status] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([status]);
CREATE INDEX [FK_bulkpaymenttemplatepos_createdby] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([createdby]);
CREATE INDEX [FK_bulkpaymenttemplatepos_roleId] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([roleId]);
CREATE INDEX [FK_bulkpaymenttemplatepos_templateId] ON [${dbxschemaname}].[bulkpaymenttemplatepos] ([templateId]);

DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentrequestpos];
DROP TABLE IF EXISTS [${dbxschemaname}].[bulkpaymentrequest];
CREATE TABLE [${dbxschemaname}].[bulkpaymentrequest] (
  [paymentrequestId] varchar(50) NOT NULL,
  [templateId] varchar(50) NOT NULL,
  [templateName] varchar(50) NOT NULL,
  [processingMode] varchar(50) NOT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [paymentId] varchar(50) DEFAULT NULL,
  [description] varchar(100) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [paymentDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [scheduledDate] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [fileId] varchar(50) NULL,
  [reviewedBy] nvarchar(50) DEFAULT NULL,
  [initiatedBy] nvarchar(50) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [status] varchar(20) DEFAULT NULL,
  [fromAccount] bigint DEFAULT NULL,
  [totalAmount] bigint DEFAULT NULL,
  [totalTransactions] bigint DEFAULT NULL,
  [paymentStatus] varchar(50) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL, 
  [bulkType] varchar(50) DEFAULT NULL,
  [updateReference] varchar(50) DEFAULT NULL,
  [creditReference] varchar(50) DEFAULT NULL,
  [debitReference] varchar(50) DEFAULT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  
  PRIMARY KEY ([paymentrequestId])
 ,
  CONSTRAINT [FK_bulkpaymentrequest_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrequest_reviewedBy_idx] FOREIGN KEY ([reviewedBy]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentrequest_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentreuest_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentrequest_fromAccount] ON [${dbxschemaname}].[bulkpaymentrequest] ([fromAccount]);
CREATE INDEX [FK_bulkpaymentrequest_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentrequest] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentrequest_featureActionId] ON [${dbxschemaname}].[bulkpaymentrequest] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentrequest_companyId] ON [${dbxschemaname}].[bulkpaymentrequest] ([companyId]);
CREATE INDEX [FK_bulkpaymentrequest_reviewedBy] ON [${dbxschemaname}].[bulkpaymentrequest] ([reviewedBy]);
CREATE INDEX [FK_bulkpaymentrequest_initiatedBy] ON [${dbxschemaname}].[bulkpaymentrequest] ([initiatedBy]);
CREATE INDEX [FK_bulkpaymentrequest_status] ON [${dbxschemaname}].[bulkpaymentrequest] ([status]);
CREATE INDEX [FK_bulkpaymentrequest_roleId] ON [${dbxschemaname}].[bulkpaymentrequest] ([roleId]);
CREATE INDEX [FK_bulkpaymentrequest_templateId] ON [${dbxschemaname}].[bulkpaymentrequest] ([templateId]);

CREATE TABLE [${dbxschemaname}].[bulkpaymentrequestpos] (
  [paymentrequestPOId] varchar(50) NOT NULL,
  [paymentrequestId] varchar(50) NOT NULL,
  [paymentOrderId] varchar(50) NOT NULL,
  [templateId] varchar(50) DEFAULT NULL,
  [confirmationNumber] varchar(50) DEFAULT NULL,
  [recipientName] varchar(50) NOT NULL,
  [accountNumber] varchar(50) NOT NULL,
  [bankName] varchar(50) DEFAULT NULL,
  [swift] varchar(50) DEFAULT NULL,
  [featureActionId] nvarchar(255) DEFAULT NULL,
  [companyId] nvarchar(50) DEFAULT NULL,
  [roleId] nvarchar(50) DEFAULT NULL,
  [status] varchar(20) DEFAULT NULL,
  [currency] varchar(50) DEFAULT NULL,
  [amount] bigint DEFAULT NULL,
  [feesPaidBy] varchar(50) DEFAULT NULL,
  [paymentReference] varchar(50) DEFAULT NULL,
  [debitAccountIBAN] varchar(50) DEFAULT NULL,
  [beneficiaryIBAN] varchar(50) DEFAULT NULL,
  [beneficiaryName] varchar(50) DEFAULT NULL,
  [beneficiaryNickName] varchar(50) DEFAULT NULL,
  [beneficiaryAddress] varchar(125) DEFAULT NULL,
  [accountWithBankBIC] varchar(50) DEFAULT NULL,
  [customer] varchar(50) DEFAULT NULL,
  [paymentMethod] varchar(50) DEFAULT NULL,
  [accType] varchar(50) DEFAULT NULL,
  [createdby] nvarchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',

  PRIMARY KEY ([paymentrequestPOId])
 ,
  CONSTRAINT [FK_bulkpaymentrequestpos_companyIdx] FOREIGN KEY ([companyId]) REFERENCES [${dbxschemaname}].[organisation] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrequestpos_createdby_idx] FOREIGN KEY ([createdby]) REFERENCES [${dbxschemaname}].[customer] ([id]),
  CONSTRAINT [FK_bulkpaymentrequestpos_paymentrequestId] FOREIGN KEY ([paymentrequestId]) REFERENCES [${dbxschemaname}].[bulkpaymentrequest] ([paymentrequestId]) ON DELETE CASCADE,
  CONSTRAINT [FK_bulkpaymentrequestpos_featureActionId_idx] FOREIGN KEY ([featureActionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_bulkpaymentrequestpos_roleId_idx] FOREIGN KEY ([roleId]) REFERENCES [${dbxschemaname}].[membergroup] ([id]) ON DELETE SET NULL ON UPDATE CASCADE
) ;

CREATE INDEX [FK_bulkpaymentrequestpos_accountNumber] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([accountNumber]);
CREATE INDEX [FK_bulkpaymentrequestpos_confirmationNumber] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([confirmationNumber]);
CREATE INDEX [FK_bulkpaymentrequestpos_featureActionId] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([featureActionId]);
CREATE INDEX [FK_bulkpaymentrequestpos_companyId] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([companyId]);
CREATE INDEX [FK_bulkpaymentrequestpos_recipientName] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([recipientName]);
CREATE INDEX [FK_bulkpaymentrequestpos_status] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([status]);
CREATE INDEX [FK_bulkpaymentrequestpos_roleId] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([roleId]);
CREATE INDEX [FK_bulkpaymentrequestpos_paymentrequestId] ON [${dbxschemaname}].[bulkpaymentrequestpos] ([paymentrequestId]);

ALTER TABLE [${dbxschemaname}].[groupbusinesstype] DROP CONSTRAINT PK_groupbusinesstype_Group_id;
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ALTER COLUMN [BusinessType_id] nvarchar(50) NOT NULL;
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD CONSTRAINT FK_groupbusinesstype_Businesstype_id FOREIGN KEY ([BusinessType_id]) REFERENCES [${dbxschemaname}].[businesstype] ([id])  ON UPDATE NO ACTION ON DELETE CASCADE;
GO
ALTER TABLE [${dbxschemaname}].[groupbusinesstype] ADD CONSTRAINT PK_groupbusinesstype_Group_id PRIMARY KEY ([Group_id] ASC,
	[BusinessType_id] ASC);
GO

ALTER TABLE [${dbxschemaname}].[businesstype] ADD [description] TEXT NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[businesstype] ADD [serviceType] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[businesstype] ADD [status] VARCHAR(50) NULL DEFAULT NULL;
GO

ALTER TABLE [${dbxschemaname}].[featureaction] ADD [status] varchar(100)  DEFAULt 'SID_ACTION_ACTIVE';
--ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT [FK_featureaction_status] FOREIGN KEY ([status]) REFERENCES [${dbxschemaname}].[status] ([id])  ON UPDATE NO ACTION ON DELETE NO ACTION;

GO

DROP TABLE IF EXISTS [${dbxschemaname}].groupservicedefinition;
CREATE TABLE [${dbxschemaname}].[groupservicedefinition] (
  [Group_id] nvarchar(50) NOT NULL,
  [serviceDefinitionId] varchar(50) NOT NULL,
  [isDefaultGroup] tinyint NOT NULL DEFAULT '0',
  [createdby] varchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [lastmodifiedts] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [synctimestamp] datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  [softdeleteflag] tinyint NOT NULL DEFAULT '0',
  CONSTRAINT PK_groupservicedefinition_id PRIMARY KEY ([Group_id],[serviceDefinitionId]),
  CONSTRAINT FK_groupservicedefinition_Businesstype_id FOREIGN KEY ([serviceDefinitionId]) REFERENCES [${dbxschemaname}].[servicedefinition] ([id]) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT FK_groupservicedefinition_Group FOREIGN KEY ([Group_id]) REFERENCES [${dbxschemaname}].membergroup ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
)
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[servicedefinitionactionlimit];
CREATE TABLE [${dbxschemaname}].[servicedefinitionactionlimit] (
   [id] varchar(50) NOT NULL,
   [serviceDefinitionId] varchar(50) NOT NULL,
   [actionId] nvarchar(255) NOT NULL,
   [limitTypeId] nvarchar(50) DEFAULT NULL,
   [value] decimal(20,2) DEFAULT NULL,
   [createdby] varchar(50) DEFAULT NULL,
   [modifiedby] varchar(50) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [softdeleteflag] smallint NOT NULL DEFAULT '0',
   PRIMARY KEY ([id]),
   CONSTRAINT [UNIQUE_servicedefinitionactionlimit] UNIQUE  ([serviceDefinitionId],[actionId],[limitTypeId])
  ,
  -- CONSTRAINT [FK_servicedefinitionactionlimit_action] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_servicedefinitionactionlimit_limitType] FOREIGN KEY ([limitTypeId]) REFERENCES [${dbxschemaname}].[limittype] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_servicedefinitionactionlimit_serviceDefinition] FOREIGN KEY ([serviceDefinitionId]) REFERENCES [${dbxschemaname}].[servicedefinition] ([id]) ON DELETE CASCADE ON UPDATE NO ACTION
 ) 

CREATE INDEX [IXFK_servicedefinitionactionlimit_businesstype] ON [${dbxschemaname}].[servicedefinitionactionlimit] ([serviceDefinitionId]);
CREATE INDEX [IXFK_servicedefinitionactionlimit_action] ON [${dbxschemaname}].[servicedefinitionactionlimit] ([actionId]);
CREATE INDEX [FK_servicedefinitionactionlimit_limitType_idx] ON [${dbxschemaname}].[servicedefinitionactionlimit] ([limitTypeId]);


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_actions_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[customer_actions_proc]  
   @_customerId nvarchar(50),
   @_actionId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @orgId nvarchar(max)

      SET @orgId = 
         (
            SELECT customer.Organization_Id
            FROM [${dbxschemaname}].customer
            WHERE customer.id = @_customerId
         )

      IF @orgId IS NULL
         SET @orgId = N''

		declare @active_features nvarchar(max)
      SET @active_features = 
         (

            SELECT String_agg(cast(feature.id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].feature
            WHERE feature.Status_id = 'SID_FEATURE_ACTIVE'

         )

      IF @active_features IS NULL
         SET @active_features = N''

		 declare @active_actions nvarchar(max)
      SET @active_actions = 
         (

            SELECT String_agg(cast(featureaction.id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @active_features) <> 0

         )
 
      IF @active_actions IS NULL
         SET @active_actions = N''

		 declare @organization_active_features nvarchar(max)
      SET @organization_active_features = 
         (

            SELECT String_agg(cast(organisationfeatures.featureId as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].organisationfeatures
            WHERE 
               (organisationfeatures.featureStatus IS NULL OR organisationfeatures.featureStatus = 'SID_FEATURE_ACTIVE') AND 
               [${dbxschemaname}].FIND_IN_SET(organisationfeatures.featureId, @active_features) <> 0 AND 
               organisationfeatures.organisationId = @orgId

         )

      IF @organization_active_features IS NULL
         SET @organization_active_features = N''
       
	   declare @organization_active_actions nvarchar(max)
      SET @organization_active_actions = 
         (

            SELECT String_agg(cast(featureaction.id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @organization_active_features) <> 0

         )

      IF @organization_active_actions IS NULL
         SET @organization_active_actions = N''

		 declare @business_customer_enabled_actions nvarchar(max)
      SET @business_customer_enabled_actions = 
         (

            SELECT String_agg(cast(customeraction.Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               customeraction.isAllowed = 1 AND 
               customeraction.Customer_id = @_customerId AND 
               [${dbxschemaname}].FIND_IN_SET(customeraction.Action_id, @organization_active_actions) <> 0
  
         )

      IF @business_customer_enabled_actions IS NULL
         SET @business_customer_enabled_actions = N''

		 declare @customer_disabled_actions nvarchar(max)
      SET @customer_disabled_actions = 
         (

            SELECT String_agg(cast(customeraction.Action_id as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customeraction
            WHERE 
               customeraction.isAllowed = 0 AND 
               customeraction.Account_id IS NULL AND 
               customeraction.Customer_id = @_customerId
  
         )

      IF @customer_disabled_actions IS NULL
         SET @customer_disabled_actions = N''

		 declare @customer_groups nvarchar(max)
      SET @customer_groups = 
         (

            SELECT String_agg(cast(customergroup.Group_id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].customergroup
            WHERE customergroup.Customer_id = @_customerId
 
         )

      IF @customer_groups IS NULL
         SET @customer_groups = N''

		 declare @group_actions nvarchar(max)
      SET @group_actions = 
         (

            SELECT String_agg(cast(groupactionlimit.Action_id  as NVARCHAR(MAX)),',')
            FROM [${dbxschemaname}].groupactionlimit
            WHERE [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Group_id, @customer_groups) <> 0

         )

      IF @group_actions IS NULL
         SET @group_actions = N''

 declare @active_feature_condition nvarchar(max)
 declare @select_statement nvarchar(max)

      IF @orgId = ''
         SET @active_feature_condition = 'feature.Status_id =''SID_FEATURE_ACTIVE'' AND [${dbxschemaname}].FIND_IN_SET(featureaction.id,CAST(''' + (@group_actions) + (''' as nvarchar(max))) <> 0')
	  ELSE 
      SET @active_feature_condition = '[${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST(''' + (@organization_active_actions) + (''' as nvarchar(max))) <> 0 AND [${dbxschemaname}].FIND_IN_SET(featureaction.id,CAST( ''' + (@group_actions) + (''' as nvarchar(max))) <> 0'))
      SET @select_statement = 'SELECT customeraction.Customer_id AS Customer_id,customeraction.Account_id AS Account_id, case when customeraction.isAllowed = ''1'' then ''true'' else ''false'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,customeraction.RoleType_id AS RoleType_id,customeraction.LimitType_id AS LimitType_id,customeraction.value AS value FROM [${dbxschemaname}].customeraction LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = customeraction.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id) where customeraction.Customer_id = ''' + (@_customerId) + (''' and featureaction.status = ''SID_ACTION_ACTIVE'' and ') + (@active_feature_condition)

      IF (@_actionId <> '')
 
         SET @select_statement = (@select_statement) + (N' and customeraction.Action_id = ') + ''''+@_actionId+''''

      SET @select_statement = 
         (@select_statement)
          + 
         ' UNION SELECT customergroup.Customer_id AS Customer_id,NULL AS Account_id, case when (membergroup.Type_id = ''TYPE_ID_BUSINESS'') then (case when [${dbxschemaname}].FIND_IN_SET(featureaction.id, CAST('''
          + 
         (@business_customer_enabled_actions)
          + 
         (''' as nvarchar(max))) <> 0 then ''true'' else ''false'' end) else ''true'' end AS isAllowed,featureaction.id AS Action_id, case when featureaction.isAccountLevel = ''1'' then ''true'' else ''false'' end AS isAccountLevel,feature.Status_id AS Feature_Status_id,feature.id AS Feature_id,membergroup.Type_id AS RoleType_id,groupactionlimit.LimitType_id AS LimitType_id,groupactionlimit.value AS value FROM ([${dbxschemaname}].customergroup LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.Group_id = customergroup.Group_id) LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.id = groupactionlimit.Action_id) LEFT JOIN [${dbxschemaname}].feature ON (feature.id = featureaction.Feature_id)) where customergroup.Customer_id ='''
          + 
         (@_customerId)
          + 
         ''' and feature.Status_id = ''SID_FEATURE_ACTIVE'' and featureaction.status = ''SID_ACTION_ACTIVE'' and [${dbxschemaname}].FIND_IN_SET(groupactionlimit.Action_id, CAST(''')
          + 
         (@customer_disabled_actions)
          + 
         (''' as nvarchar(max))) = 0 and ')
          + 
         (@active_feature_condition)
  

      IF (@_actionId <> '')

         SET @select_statement = (@select_statement) + (N' and groupactionlimit.Action_id = ') + ''''+(@_actionId)+''''

      exec(@select_statement)

   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvers_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvers_proc](
	@_requestId nvarchar(50) 
)
AS
	BEGIN
		SELECT customerapprovalmatrix.customerId as customerId 
		FROM customerapprovalmatrix  WHERE 
		customerapprovalmatrix.approvalMatrixId 
		IN (
			SELECT requestapprovalmatrix.approvalMatrixId 
			FROM requestapprovalmatrix WHERE 
			requestapprovalmatrix.requestId = @_requestId
		   );
	END
GO

 
 DROP TABLE IF EXISTS [${dbxschemaname}].[manageapprovalmatrix];
 CREATE TABLE [${dbxschemaname}].[manageapprovalmatrix] (
   [contractId] varchar(50) NOT NULL,
   [coreCustomerId] varchar(50) NOT NULL,
   [isDisabled] smallint NOT NULL DEFAULT '1',
   [createdby] varchar(50) DEFAULT NULL,
   [modifiedby] varchar(50) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [updatedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [softdeleteflag] smallint NOT NULL DEFAULT '0',
   PRIMARY KEY ([contractId],[coreCustomerId]),
 ) ;
 
 DROP TABLE IF EXISTS [${dbxschemaname}].[accesspolicy];
CREATE TABLE [${dbxschemaname}].[accesspolicy] (
   [id] varchar(50) NOT NULL,
   [name] varchar(50) NOT NULL,
   [description] varchar(100) NOT NULL,
   [createdby] varchar(45) DEFAULT NULL,
   [modifiedby] varchar(45) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   CONSTRAINT [PK_accesspolicy_id] PRIMARY KEY ([id])
 ) ;
 
 DROP TABLE IF EXISTS [${dbxschemaname}].[actionlevel];
 CREATE TABLE [${dbxschemaname}].[actionlevel] (
   [id] varchar(50) NOT NULL,
   [name] varchar(50) NOT NULL,
   [description] varchar(100) NOT NULL,
   [createdby] varchar(45) DEFAULT NULL,
   [modifiedby] varchar(45) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   CONSTRAINT [PK_actionlevel_id] PRIMARY KEY ([id])
 ) ;
 
 
 DROP TABLE IF EXISTS [${dbxschemaname}].[limitgroup];
 CREATE TABLE [${dbxschemaname}].[limitgroup] (
   [id] varchar(50) NOT NULL,
   [name] varchar(50) NOT NULL,
   [description] varchar(100) NOT NULL,
   [createdby] varchar(45) DEFAULT NULL,
   [modifiedby] varchar(45) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   CONSTRAINT [PK_limitgroup_id] PRIMARY KEY ([id])
 ) ;
 
 DROP TABLE IF EXISTS [${dbxschemaname}].[limitgroupdisplaynamedescription];
 CREATE TABLE [${dbxschemaname}].[limitgroupdisplaynamedescription] (
   [limitGroupId] varchar(50) NOT NULL,
   [localeId] nvarchar(10) NOT NULL,
   [displayName] varchar(100) NOT NULL,
   [displayDescription] varchar(300) NOT NULL,
   [createdby] varchar(50) DEFAULT NULL,
   [modifiedby] varchar(50) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [softdeleteflag] smallint NOT NULL DEFAULT '0',
   CONSTRAINT [PK_LGDD_lg_id_lid] PRIMARY KEY ([limitGroupId],[localeId])
  ,
   CONSTRAINT [FK_limitgroupdisplaynamedescription_limitGroupId] FOREIGN KEY ([limitGroupId]) REFERENCES [${dbxschemaname}].[limitgroup] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_limitgroupdisplaynamedescription_localeId] FOREIGN KEY ([localeId]) REFERENCES [${dbxschemaname}].[locale] ([Code]) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ;

CREATE INDEX [limitgroupdisplaynamedescription_localeId] ON [${dbxschemaname}].[limitgroupdisplaynamedescription] ([localeId]);


ALTER TABLE [${dbxschemaname}].[featureaction] ADD limitgroupId varchar(50) default NULL;
ALTER TABLE [${dbxschemaname}].[featureaction] ADD accesspolicyId varchar(50) default NULL;
ALTER TABLE [${dbxschemaname}].[featureaction] ADD actionlevelId varchar(50) default NULL;
ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT [FK_featureaction_accesspolicyId] FOREIGN KEY ([accesspolicyId]) REFERENCES [${dbxschemaname}].[accesspolicy] ([id])  ON UPDATE NO ACTION ON DELETE NO 
ACTION;  
ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT [FK_featureaction_actionlevelId] FOREIGN KEY ([actionlevelId]) REFERENCES [${dbxschemaname}].[actionlevel] ([id])  ON UPDATE NO ACTION ON DELETE NO ACTION;  
ALTER TABLE [${dbxschemaname}].[featureaction] ADD CONSTRAINT [FK_featureaction_limitgroupId] FOREIGN KEY ([limitgroupId]) REFERENCES [${dbxschemaname}].[limitgroup] ([id])  ON UPDATE NO ACTION ON DELETE NO ACTION; 	

ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
ALTER COLUMN [displayName] VARCHAR(100) NOT NULL;
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
ALTER COLUMN  [displayDescription] VARCHAR(300) NOT NULL ;

DROP procedure IF EXISTS [${dbxschemaname}].[manageapprovalmatrix_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[manageapprovalmatrix_update_proc](
	@_contractId nvarchar(50) ,
	@_cifList nvarchar(max) ,
	@_isDisabledflag nvarchar(50) 
)
AS
	BEGIN
		DECLARE @index1 INT=0;
		DECLARE @numOfRecords INT;
		DECLARE @recordsData NVARCHAR(MAX);
		DECLARE @count INT=0;
		DECLARE @query NVARCHAR(MAX);
		DECLARE @query2 NVARCHAR(MAX);
		DECLARE @cquery NVARCHAR(MAX);
		DECLARE @var NVARCHAR(MAX);
		DECLARE @mod_contractId NVARCHAR(MAX)= Quotename(@_contractId,'''');
		DECLARE @resultcount table ([rowcount] int);
		set @numOfRecords = LEN(@_cifList) - LEN(REPLACE(@_cifList, ',', '')) + 1;
		WHILE (1 = 1)
			BEGIN
				set @index1 = @index1 + 1;
				IF @index1 = @numOfRecords + 1 
					BREAK
				ELSE
					BEGIN
						set @recordsData = Quotename([${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_cifList, ',', @index1), ',', -1 ),'''');
						set @var=concat('select count(*) from [${dbxschemaname}].[manageapprovalmatrix] where contractId=',@mod_contractId,' and coreCustomerId=',@recordsData);
						insert into @resultcount ([rowcount])
						exec (@var);
						set @count = (select top (1) [rowcount] from @resultcount);
						if @count = 0
						BEGIN
							set @query = concat('INSERT INTO [${dbxschemaname}].[manageapprovalmatrix](contractId,coreCustomerId,isDisabled) VALUES ('+@mod_contractId+','+@recordsData+','+@_isDisabledflag,');');
							EXEC(@query)
						END
						else
							set @query2=concat('UPDATE [${dbxschemaname}].[manageapprovalmatrix] set isDisabled=',@_isDisabledflag,' where contractId=',@mod_contractId,' and coreCustomerId=',@recordsData);
							EXEC(@query2)
					END
			END
	END
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] ADD [beneficiaryType] VARCHAR(50);
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] ADD [addToExistingFlag] VARCHAR(5);
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
		DECLARE @query NVARCHAR(100);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO bulkpaymenttemplatepos(paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,featureActionId,companyId,roleId,status,createdby,beneficiaryName,paymentMethod,currency,amount,feesPaidBy,paymentReference,swift,beneficiaryNickName,beneficiaryAddress,accType,beneficiaryType,addToExistingFlag,beneficiaryIBAN,bankName)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END

GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] ADD [batchMode] NVARCHAR(15);
ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ADD [batchMode] NVARCHAR(15);
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [batchMode] NVARCHAR(15);
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [batchMode] NVARCHAR(15);
GO

 
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
		LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [actionlevel].[id])));
GO

ALTER TABLE [${dbxschemaname}].[membergroup] ADD [isApplicabletoAllServices] smallint DEFAULt '0';
GO

ALTER TABLE [${dbxschemaname}].[groupactionlimit] DROP CONSTRAINT [groupactionlimit$FK_groupactionlimit_Group];
ALTER TABLE [${dbxschemaname}].[groupactionlimit] ADD CONSTRAINT FK_groupactionlimit_Group FOREIGN KEY ([Group_id]) REFERENCES [${dbxschemaname}].membergroup ([id])  ON UPDATE NO ACTION ON DELETE CASCADE;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_filtered_locations_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[get_filtered_locations_proc](
	@_searchText NVARCHAR(MAX)
)
AS
BEGIN
	select TOP 20 * from [${dbxschemaname}].location where DisplayName like Concat('%', @_searchText,'%') order by DisplayName;
END
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[limitgroups_view];
GO
CREATE VIEW [${dbxschemaname}].[limitgroups_view] AS
    SELECT 
        [${dbxschemaname}].[limitgroup].[id] AS [id],
        [${dbxschemaname}].[limitgroup].[name] AS [name],
        [${dbxschemaname}].[limitgroup].[description] AS [description],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[localeId] AS [localeId],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[limitgroupdisplaynamedescription].[displayDescription] AS [displayDescription]
    FROM
        ([${dbxschemaname}].[limitgroup]
       LEFT JOIN [${dbxschemaname}].[limitgroupdisplaynamedescription] ON (([${dbxschemaname}].[limitgroupdisplaynamedescription].[limitGroupId] = [${dbxschemaname}].[limitgroup].[id]))) ;
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[dependentactions];
CREATE TABLE [${dbxschemaname}].[dependentactions] (
   [actionId] nvarchar(255) NOT NULL,
   [dependentactionId] nvarchar(255) NOT NULL,
   [featureId] nvarchar(255) NOT NULL,
   [actionName] varchar(255) NOT NULL,
   [featureName] varchar(255) NOT NULL,
   [createdby] varchar(50) DEFAULT NULL,
   [modifiedby] varchar(50) DEFAULT NULL,
   [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
   PRIMARY KEY ([actionId],[dependentactionId])
  ,
   CONSTRAINT [FK_dependentactions_actionId] FOREIGN KEY ([actionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_dependentactions_dependentactionId] FOREIGN KEY ([dependentactionId]) REFERENCES [${dbxschemaname}].[featureaction] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
   CONSTRAINT [FK_dependentactions_feature] FOREIGN KEY ([featureId]) REFERENCES [${dbxschemaname}].[feature] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
 ) ;

CREATE INDEX [FK_dependentactions_dependentactionId] ON [${dbxschemaname}].[dependentactions] ([dependentactionId]);
CREATE INDEX [FK_dependentactions_feature] ON [${dbxschemaname}].[dependentactions] ([featureId]);

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[servicedefinition_features_actions_view]; 
GO

CREATE VIEW [${dbxschemaname}].[servicedefinition_features_actions_view] AS
    SELECT 
    [${dbxschemaname}].[servicedefinitionactionlimit].[serviceDefinitionId] AS [serviceDefinitionId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[actionId] AS [actionId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[limitTypeId] AS [limitTypeId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[value] AS [value],
    [${dbxschemaname}].[servicedefinitionactionlimit].[id] AS [serviceDefinitionActionLimitId],
    [${dbxschemaname}].[servicedefinitionactionlimit].[softdeleteflag] AS [softdelete],
    [${dbxschemaname}].[servicedefinition].[serviceType] AS [serviceType],
    [${dbxschemaname}].[servicedefinition].[name] AS [serviceDefinitionName],
    [${dbxschemaname}].[servicedefinition].[description] AS [serviceDefinitionDescription],
    [${dbxschemaname}].[featureaction].[name] AS [actionName],
    [${dbxschemaname}].[featureaction].[description] AS [actionDescription],
    [${dbxschemaname}].[featureaction].[Type_id] AS [actionTypeId],
    [${dbxschemaname}].[featureaction].[Feature_id] AS [featureId],
    [${dbxschemaname}].[featureaction].[isMFAApplicable] AS [isMFAApplicable],
    [${dbxschemaname}].[featureaction].[isAccountLevel] AS [isAccountLevel],
    [${dbxschemaname}].[featureaction].[isPrimary] AS [isPrimary],
    [${dbxschemaname}].[featureaction].[DisplaySequence] AS [actionDisplaysequence],
    [${dbxschemaname}].[featureaction].[dependency] AS [actionDependency],
    [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
	[${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
	[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
    [${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
    [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
    [${dbxschemaname}].[actionlevel].[name] AS [actionlevel],
    [${dbxschemaname}].[featureaction].[actionlevelId] AS [actionlevelId],
	[${dbxschemaname}].[dependentactions].[dependentactionId] AS [dependentactionId],
    [${dbxschemaname}].[dependentactions].[featureId] AS [dependentFeatureId],
    [${dbxschemaname}].[dependentactions].[actionName] AS [dependentActionName],
    [${dbxschemaname}].[dependentactions].[featureName] AS [dependentFeatureName],
    [${dbxschemaname}].[feature].[name] AS [featureName],
    [${dbxschemaname}].[feature].[description] AS [featureDescription],
    [${dbxschemaname}].[feature].[Type_id] AS [featureTypeId],
    [${dbxschemaname}].[feature].[Status_id] AS [featureStatusId],
    [${dbxschemaname}].[feature].[DisplaySequence] AS [featureDisplaysequence],
    [${dbxschemaname}].[feature].[isPrimary] AS [featureIsPrimary]
  FROM
    (((((((servicedefinitionactionlimit
    LEFT JOIN [${dbxschemaname}].[servicedefinition] ON (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinitionactionlimit].[serviceDefinitionId])))
    LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[servicedefinitionactionlimit].[actionId])))
	LEFT JOIN [${dbxschemaname}].[dependentactions] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions].[actionId])))
    LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
	LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
	LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
    LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])));
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
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
    (SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
        WHERE (([${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = [${dbxschemaname}].[servicedefinition].[id]) AND ([${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = 1))) AS [defaultRole],
    (SELECT COUNT(DISTINCT [${dbxschemaname}].[servicedefinition_features_actions_view].[featureId]) FROM [${dbxschemaname}].[servicedefinition_features_actions_view]
        WHERE (([${dbxschemaname}].[servicedefinition].[id] = [${dbxschemaname}].[servicedefinition_features_actions_view].[serviceDefinitionId]) AND ([${dbxschemaname}].[servicedefinition_features_actions_view].[softdelete] = '0'))) AS [numberOfFeatures],
    (SELECT COUNT([${dbxschemaname}].[contract].[id]) FROM [${dbxschemaname}].[contract] 
      WHERE ([${dbxschemaname}].[contract].[servicedefinitionId] = [${dbxschemaname}].[servicedefinition].[id])) AS [numberOfContracts]
  FROM [${dbxschemaname}].[servicedefinition];
		
GO
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[dependentactions_view];
GO
CREATE VIEW [${dbxschemaname}].[dependentactions_view] AS
     SELECT 
        [${dbxschemaname}].[dependentactions].[actionId] AS [actionId],
        [${dbxschemaname}].[dependentactions].[dependentactionId] AS [dependentactionId],
        [${dbxschemaname}].[featureaction].[name] AS [actionName],
        [${dbxschemaname}].[dependentactions].[featureId] AS [featureId],
        [${dbxschemaname}].[feature].[name] AS [featureName]
    FROM
        (([${dbxschemaname}].[dependentactions]
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[dependentactions].[dependentactionId] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[dependentactions].[featureId] = [${dbxschemaname}].[feature].[id])));
GO
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[get_feature_actions_view];
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
        [${dbxschemaname}].[featureactionroletype].[RoleType_id] AS [actionType],
        [${dbxschemaname}].[accesspolicy].[name] AS [accessPolicy],
        [${dbxschemaname}].[limitgroup].[name] AS [limitGroup],
		[${dbxschemaname}].[featureaction].[accesspolicyId] AS [accessPolicyId],
		[${dbxschemaname}].[featureaction].[limitgroupId] AS [limitGroupId],
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
        (((((((((((featureaction
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])))
        LEFT JOIN [${dbxschemaname}].[actiondisplaynamedescription] ON (([${dbxschemaname}].[actiondisplaynamedescription].[Action_id] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[accesspolicy] ON (([${dbxschemaname}].[featureaction].[accesspolicyId] = [${dbxschemaname}].[accesspolicy].[id])))
        LEFT JOIN [${dbxschemaname}].[featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [${dbxschemaname}].[featureactionroletype] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[featureactionroletype].[Action_id])))
        LEFT JOIN [${dbxschemaname}].[termandcondition] ON (([${dbxschemaname}].[featureaction].[TermsAndConditions_id] = [${dbxschemaname}].[termandcondition].[id])))
        LEFT JOIN [${dbxschemaname}].[limitgroup] ON (([${dbxschemaname}].[featureaction].[limitgroupId] = [${dbxschemaname}].[limitgroup].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlevel] ON (([${dbxschemaname}].[featureaction].[actionlevelId] = [${dbxschemaname}].[actionlevel].[id])))
        LEFT JOIN [${dbxschemaname}].[dependentactions_view] ON (([${dbxschemaname}].[featureaction].[id] = [${dbxschemaname}].[dependentactions_view].[actionId])))
        LEFT JOIN [${dbxschemaname}].[membergrouptype] ON (([${dbxschemaname}].[featureactionroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])))
        LEFT JOIN [${dbxschemaname}].[actionlimit] ON (([${dbxschemaname}].[actionlimit].[Action_id] = [${dbxschemaname}].[featureaction].[id])));
GO
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO		
DROP VIEW IF EXISTS [${dbxschemaname}].[get_all_features_view];
GO
CREATE VIEW [${dbxschemaname}].[get_all_features_view] AS
    SELECT 
        [${dbxschemaname}].[feature].[id] AS [id],
        [${dbxschemaname}].[feature].[name] AS [name],
        [${dbxschemaname}].[feature].[description] AS [description],
        [${dbxschemaname}].[feature].[Type_id] AS [Type_id],
        [${dbxschemaname}].[feature].[Service_Fee] AS [Service_Fee],
        [${dbxschemaname}].[feature].[Status_id] AS [Status_id],
        [${dbxschemaname}].[featureroletype].[RoleType_id] AS [roleTypeId],
        [${dbxschemaname}].[featuredisplaynamedescription].[Locale_id] AS [languageId],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayName] AS [displayName],
        [${dbxschemaname}].[featuredisplaynamedescription].[displayDescription] AS [displayDescription],
        [${dbxschemaname}].[membergrouptype].[description] AS [roleTypeName],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                [featureaction]
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'MONETARY'))) AS [monetaryActions],
        (SELECT 
                COUNT(DISTINCT [${dbxschemaname}].[featureaction].[id])
            FROM
                featureaction
            WHERE
                (([${dbxschemaname}].[feature].[id] = [${dbxschemaname}].[featureaction].[Feature_id])
                    AND ([${dbxschemaname}].[featureaction].[Type_id] = 'NON_MONETARY'))) AS nonMonetaryActions
    FROM
        (((feature
        LEFT JOIN [featureroletype] ON (([${dbxschemaname}].[featureroletype].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [featuredisplaynamedescription] ON (([${dbxschemaname}].[featuredisplaynamedescription].[Feature_id] = [${dbxschemaname}].[feature].[id])))
        LEFT JOIN [membergrouptype] ON (([${dbxschemaname}].[featureroletype].[RoleType_id] = [${dbxschemaname}].[membergrouptype].[id])));
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ALTER COLUMN [content] NVARCHAR(MAX) NOT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] ALTER COLUMN [content] NVARCHAR(MAX) NOT NULL;		
ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] ALTER COLUMN [sysGeneratedFileName] VARCHAR(150);
		
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplate] ADD [totalBeneficiaries] VARCHAR(20);
ALTER TABLE [${dbxschemaname}].[bulkpaymentrequest] ADD [totalBeneficiaries] VARCHAR(20);
GO 
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[actiondependency_view];
GO
CREATE VIEW [${dbxschemaname}].[actiondependency_view] AS
SELECT 
        [${dbxschemaname}].[dependentactions].[dependentactionId] AS [actionName],
        [${dbxschemaname}].[dependentactions].[actionId] AS [dependencyAction],
        [${dbxschemaname}].[dependentactions].[featureId] AS [featureId],
        [${dbxschemaname}].[featureaction].[status] AS [actionStatus],
        [${dbxschemaname}].[feature].[Status_id] AS [featureStatus]
    FROM
        ((dependentactions
        LEFT JOIN [${dbxschemaname}].[featureaction] ON (([${dbxschemaname}].[dependentactions].[dependentactionId] = [${dbxschemaname}].[featureaction].[id])))
        LEFT JOIN [${dbxschemaname}].[feature] ON (([${dbxschemaname}].[dependentactions].[featureId] = [${dbxschemaname}].[feature].[id])));
GO
		
CREATE TABLE [${dbxschemaname}].[cancellationreason] (
  [id] varchar(50) NOT NULL,
  [reason] varchar(50) NOT NULL,
  [createdby] nvarchar(50) DEFAULT NULL,
  [modifiedby] varchar(50) DEFAULT NULL,
  [createdts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [lastmodifiedts] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [synctimestamp] datetime2(0) NOT NULL DEFAULT GETDATE(),
  [softdeleteflag] smallint NOT NULL DEFAULT '0',
  
  PRIMARY KEY ([id])
) ;
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP VIEW if exists [${dbxschemaname}].[groups_view];
GO
CREATE VIEW [${dbxschemaname}].[groups_view] AS 
select [${dbxschemaname}].[membergroup].[id] AS [Group_id],
[${dbxschemaname}].[membergroup].[Type_id] AS [Type_id],
[${dbxschemaname}].[customertype].[Name] AS [Type_Name],
[${dbxschemaname}].[membergroup].[Description] AS [Group_Desc],
[${dbxschemaname}].[membergroup].[Status_id] AS [Status_id],
[${dbxschemaname}].[membergroup].[Name] AS [Group_Name],
[${dbxschemaname}].[membergroup].[isEAgreementActive] AS [isEAgreementActive],
[${dbxschemaname}].[membergroup].[isApplicabletoAllServices] As [isApplicabletoAllServices],
(select count([${dbxschemaname}].[groupentitlement].[Group_id]) from [${dbxschemaname}].[groupentitlement] where
 ([${dbxschemaname}].[groupentitlement].[Group_id] = [${dbxschemaname}].[membergroup].[id])) AS [Entitlements_Count],
 (select count(distinct([${dbxschemaname}].[customergroup].[Customer_id])) from [${dbxschemaname}].[customergroup] 
 where ([${dbxschemaname}].[customergroup].[Group_id] = [membergroup].[id])) AS [Customers_Count],
 (case [${dbxschemaname}].[membergroup].[Status_id] when 'SID_ACTIVE' then 'Active' else 'Inactive' end) AS [Status] 
 from ([${dbxschemaname}].[membergroup] join [customertype] on(([${dbxschemaname}].[membergroup].[Type_id] = [customertype].[id])));

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
		DECLARE @query NVARCHAR(100);
		SET @_povalues = ( select replace(@_povalues, '"', ''''))
		SET @numOfPos = LEN(@_povalues) - LEN(REPLACE(@_povalues, '|', '')) + 1;
		WHILE (1 = 1)
		BEGIN
			SET @index1 = @index1 + 1;
			 IF @index1 = @numOfPos + 1
               BREAK
            ELSE 
               BEGIN
				SET @posData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_povalues, '|', @index1), '|', -1 );
				SET @query = ('INSERT INTO bulkpaymentrequestpos(paymentrequestPOId,paymentrequestId,paymentOrderId,templateId,confirmationNumber,recipientName,accountNumber,bankName,swift,featureActionId,companyId,roleId,status,currency,amount,feesPaidBy,paymentReference,debitAccountIBAN,beneficiaryIBAN,beneficiaryName,beneficiaryNickName,beneficiaryAddress,accountWithBankBIC,customer,paymentMethod,accType,createdby)
				VALUES (') +@posData+ (');');
			EXEC(@query)
			END 
	END
END

GO 

ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
DROP CONSTRAINT [actiondisplaynamedescription$FK_actiondisplaynamedescription_Action_id];
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription]
ADD CONSTRAINT [FK_actiondisplaynamedescription_Action_id]
FOREIGN KEY (Action_id)
REFERENCES [${dbxschemaname}].[featureaction] (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

--ALTER TABLE [${dbxschemaname}].[customeraction]
--DROP CONSTRAINT [customeraction$FK_CustomerActionLimit_Action];
--ALTER TABLE [${dbxschemaname}].[customeraction]
--ADD CONSTRAINT [FK_CustomerActionLimit_Action]
--FOREIGN KEY (Action_id)
--REFERENCES [${dbxschemaname}].[featureaction] (id)
--ON DELETE CASCADE
--ON UPDATE CASCADE;

ALTER TABLE [${dbxschemaname}].[groupactionlimit]
DROP CONSTRAINT [groupactionlimit$FK_groupactionlimit_Action];
ALTER TABLE [${dbxschemaname}].[groupactionlimit]
ADD CONSTRAINT [FK_groupactionlimit_Action]
FOREIGN KEY (Action_id)
REFERENCES [${dbxschemaname}].[featureaction] (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE [${dbxschemaname}].[featureactionroletype]
DROP CONSTRAINT [featureactionroletype$FK_featureactionroletype_Action_id];
ALTER TABLE [${dbxschemaname}].[featureactionroletype]
ADD CONSTRAINT [FK_featureactionroletype_Action_id]
FOREIGN KEY (Action_id)
REFERENCES [${dbxschemaname}].[featureaction] (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

ALTER TABLE [${dbxschemaname}].[organisationactionlimit]
DROP CONSTRAINT [organisationactionlimit$FK_organisationactionlimit_action];
ALTER TABLE [${dbxschemaname}].[organisationactionlimit]
ADD CONSTRAINT [FK_organisationactionlimit_action]
FOREIGN KEY (Action_id)
REFERENCES [${dbxschemaname}].[featureaction] (id)
ON DELETE CASCADE
ON UPDATE CASCADE;

GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[customergroups_view];
GO
CREATE VIEW [${dbxschemaname}].[customergroups_view] as
Select  count(distinct([${dbxschemaname}].[customergroup].[Customer_id])) as [customerCount], 
 [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] as [servicedefinitionId],
 [${dbxschemaname}].[groupservicedefinition].[Group_id] as [groupId] from
 [${dbxschemaname}].[groupservicedefinition] 
 join  [${dbxschemaname}].[contract]  on  [${dbxschemaname}].[contract].[servicedefinitionId]=  [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] 
 join  [${dbxschemaname}].[customergroup]   on [${dbxschemaname}].[customergroup].[Group_id] = [${dbxschemaname}].[groupservicedefinition].[Group_id] and  
 [${dbxschemaname}].[customergroup].[contractId]=[${dbxschemaname}].[contract].[id]
 group by  [${dbxschemaname}].[groupservicedefinition].[Group_id],  [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId];
 
 GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [comments] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [comments] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [cancellationreason] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [cancellationreason] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [rejectioncomments] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [rejectioncomments] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] ADD [rejectionreason] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [rejectionreason] VARCHAR(50) NULL DEFAULT NULL;

GO
ALTER TABLE [${dbxschemaname}].[achfile] ALTER COLUMN [contents] VARCHAR(MAX) NULL;
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD [serviceCharge] VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD [transactionAmount] VARCHAR(45) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] ADD [convertedAmount] VARCHAR(45) NULL DEFAULT NULL;

GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[transaction]', 'convertedAmount';
GO

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] ADD [fileName] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[transaction] ALTER COLUMN [convertedAmount] VARCHAR(45) NULL ;
ALTER TABLE [${dbxschemaname}].[transaction] ADD [serviceCharge] VARCHAR(45) NULL DEFAULT NULL;
GO

ALTER TABLE [${dbxschemaname}].[application] ADD [isSelfApprovalEnabled] [bit] NOT NULL DEFAULT (1)
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_generaltranscation_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_generaltranscation_proc]  
   @_customerId nvarchar(50),
   @_transactionId nvarchar(50),
   @_featureActionId nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
  
   BEGIN
   
	 MainLabel:
   
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  declare @customerMatrixIds nvarchar(max)
	  declare @alreadyApprovedIds nvarchar(max)
	  declare @companyId nvarchar(max)
	  declare @approvalRequestIds nvarchar(max)
	  declare @queryTypecondition nvarchar(max)
	  declare @combinedIds nvarchar(max)
	  declare @numOfParams int = 0;
	  declare @searchQuery nvarchar(max) = ''
	  declare @idx int = 1
	  declare @filterParam nvarchar(max) = ''
	  declare @filterValue nvarchar(max) = ''
	  declare @paginationQuery nvarchar(max)
	  declare @features nvarchar(max)
	  declare @companyRequestIds nvarchar(max)
	  declare @customerAcounts nvarchar(max)
	  declare @isSelfApprovalEnabled bit
	  declare @notCreatedBySelf nvarchar(max)

	  SET @combinedIds = (select STRING_AGG(CAST(id as nvarchar(max)), ',') from [${dbxschemaname}].customer where combinedUserId = Quotename(@_customerId,''''))

	  IF @combinedIds is NULL or @combinedIds = ''
		SET @combinedIds = @_customerId
	  ELSE
		SET @combinedIds = @_customerId + ',' + @combinedIds

	  SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from [${dbxschemaname}].application);

	  IF (@isSelfApprovalEnabled=0)
		SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby, ''', @combinedIds,''')=0')
	  ELSE
		SET @notCreatedBySelf = '';

	SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='') THEN '' ELSE @_filterByParam END
    SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN  '' ELSE @_filterByValue END
	SET @_transactionId= CASE WHEN ( @_transactionId='' or @_transactionId is null) THEN '%' ELSE @_transactionId END
    SET @_featureActionId = CASE WHEN (@_transactionId = '%') THEN '%' ELSE @_featureActionId  END
	SET @numOfParams = 0;
	IF datalength(@_filterByParam) > 0 
	SET @numOfParams = datalength(@_filterByParam) - datalength(REPLACE(@_filterByParam, ',', '')) + 1;


		WHILE @idx <= @numOfParams
		BEGIN
			SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_filterByParam, ',', @idx), ',', -1 )
			SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_filterByValue, ',', @idx), ',', -1 )
			SET @searchQuery = @searchQuery + ' AND (' + @filterParam + ' LIKE '''+@filterValue +''' )'
			SET @idx = @idx + 1;
		END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)

      IF @companyId IS NULL

         SET @companyId = ''
 
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

      SET @_sortByParam = case when @_sortByParam='' or @_sortByParam is null then 'createdts' else @_sortByParam end

      SET @_sortOrder = case when @_sortOrder = '' or @_sortOrder is null then 'DESC' else @_sortOrder end

      SET @customerMatrixIds = 
         (

            SELECT String_agg(CAST(approvalMatrixId as nvarchar(max)) ,',')
            FROM [${dbxschemaname}].customerapprovalmatrix
            WHERE [${dbxschemaname}].FIND_IN_SET(customerapprovalmatrix.customerId , @combinedIds) <> 0

         )

      IF @customerMatrixIds IS NULL

         SET @customerMatrixIds = ''

      SET @alreadyApprovedIds = 
         (

            SELECT String_agg(CAST(requestId as nvarchar(max)),',')
            FROM [${dbxschemaname}].bbactedrequest
            WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby , @combinedIds) <> 0 AND bbactedrequest.action = 'Approved'

         )
		 
      IF @alreadyApprovedIds IS NULL

         SET @alreadyApprovedIds = ''

	SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							) 
        IF @approvalRequestIds is NULL      
			SET @approvalRequestIds = ''



      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND  [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby, ''') + (@combinedIds) + (''') <> 0 AND (bbrequest.status = ''Pending'' OR bbrequest.status = ''Approved'' OR bbrequest.[status] = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST(generaltransaction.requestId as NVARCHAR(MAX)),  ''') + (@approvalRequestIds) + (''') <> 0  AND generaltransaction.[status] = ''Pending'' ')+@notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN (' AND generaltransaction.status = ''Rejected'' ')
                        ELSE
							CASE
								WHEN (@_transactionId = '%') THEN (' AND NOT generaltransaction.status = ''Withdrawn''')
								ELSE ''
							END
                     END
               END
         END
		
      set @searchQuery = case when @searchQuery='' or @searchQuery is null then '' else 
	  'AND (generaltransaction.payeeId LIKE ''%'+@_searchString+'%'' OR customeraccounts.accountName LIKE ''%'+@_searchString+'%'' OR feature.name LIKE ''%'+@_searchString+'%'' OR customer.userName LIKE ''%'+@_searchString+'%'' )' end
	  
      SET @paginationQuery = case when (@_pageOffset ='' or @_pageOffset is null) and (@_pageSize ='' or @_pageSize is null) then 
	  '' else ' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT '+@_pageSize + ' ROWS ONLY ' end
	  

      SET @features = 
         (

            SELECT String_agg(CAST(Feature_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureactionlist) <> 0

         )

      IF @features IS NULL
 
         SET @features = ''

	declare @createActions nvarchar(max)
      SET @createActions = 
         (

            SELECT String_agg(CAST(id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @features) <> 0 AND featureaction.id LIKE '%_CREATE'

         )

      IF @createActions IS NULL

         SET @createActions = ''

		 
      SET @companyRequestIds = 
         (

            SELECT String_Agg(CAST(requestId  as nvarchar(max)),',')
            FROM [${dbxschemaname}].bbrequest
            WHERE [${dbxschemaname}].FIND_IN_SET(bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbrequest.featureActionId, @createActions) <> 0

         )

      IF @companyRequestIds IS NULL

         SET @companyRequestIds = ''
		 
      SET @customerAcounts = 
         (

            SELECT String_agg(CAST(Account_id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].customeraccounts
            WHERE [${dbxschemaname}].FIND_IN_SET(customeraccounts.Customer_id, @combinedIds) <> 0

         )

      IF @customerAcounts IS NULL

         SET @customerAcounts = ''

		 declare @accountsQuery nvarchar(max)
		 declare @companyQuery nvarchar(max)
		 declare @select_statement nvarchar(max)

      SET @accountsQuery = case when (@companyId='' or @companyId is null) then '' else ' AND [${dbxschemaname}].FIND_IN_SET(generaltransaction.fromAccountNumber, '''+@customerAcounts+''') <> 0' end

      SET @companyQuery = case when (@companyId='' or @companyId is null) then '' else ' AND ([${dbxschemaname}].FIND_IN_SET(generaltransaction.companyId,'''+@companyId +''') <> 0 OR [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby, '''+ @combinedIds +''') <> 0) ' end

	
	 
	 declare @numberOfApprovals nvarchar(max) = ''
	 SET @numberOfApprovals = ( SELECT COUNT(*) FROM [${dbxschemaname}].bbrequest inner join [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId) inner join [${dbxschemaname}].customerapprovalmatrix on (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId as nvarchar(max)), @companyRequestIds) <> 0)



	  SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1


	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2


     SET @select_statement = '	 
	 SELECT * FROM
        
        (SELECT 
						generaltransaction.transactionId,
						generaltransaction.featureActionId,
						feature.name as featureName,
						generaltransaction.fromAccountNumber,
						generaltransaction.amount,
						generaltransaction.requestId,
						generaltransaction.createdby,
						generaltransaction.createdts,
						generaltransaction.frequencyTypeId,
						generaltransaction.status,
						generaltransaction.numberOfRecurrences,
						generaltransaction.payeeId,
						generaltransaction.companyId,
						generaltransaction.scheduledDate,
						( CASE 
							WHEN customeraccounts.AccountName is NULL THEN ''AccountName'' 
			                ELSE customeraccounts.AccountName 
						END ) AS accountName,
						customer.UserName AS userName,
						bbrequest.createdby AS requestCreatedby,
						(CASE
							WHEN [${dbxschemaname}].FIND_IN_SET(generaltransaction.createdby,  CAST('''+@combinedIds+''' as nvarchar(max))) > 0 THEN ''true''
							ELSE ''false''
						 END) as amICreator,
						(CASE 
							WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)), CAST('''+@approvalRequestIds+''' as nvarchar(max))) > 0 THEN ''true''
			        		ELSE ''false''
				 		END)
				 		 as amIApprover

				FROM

				( SELECT 
						billpaytransfers.transactionId AS transactionId,
						billpaytransfers.featureActionId AS featureActionId,
						billpaytransfers.fromAccountNumber AS fromAccountNumber,
						billpaytransfers.amount AS amount,
						billpaytransfers.requestId AS requestId,
						billpaytransfers.createdby AS createdby,
						billpaytransfers.createdts AS createdts,
						billpaytransfers.frequencyTypeId AS frequencyTypeId,
						billpaytransfers.status AS status,
						billpaytransfers.numberOfRecurrences AS numberOfRecurrences,
                        (CASE 
							WHEN billpaytransfers.payeeName IS NULL OR billpaytransfers.payeeName = '''' THEN 
							CASE
								WHEN billpaytransfers.billerId IS NULL OR billpaytransfers.billerId = '''' THEN 
									CASE 
										WHEN billpaytransfers.payeeId IS NULL OR billpaytransfers.payeeId = '''' THEN billpaytransfers.toAccountNumber
										ELSE billpaytransfers.payeeId
									END
								ELSE billpaytransfers.billerId
							END
							ELSE billpaytransfers.payeeName
						END) 
                        AS payeeId,
						billpaytransfers.companyId AS companyId,
		                billpaytransfers.softdeleteflag AS softdeleteflag,
						billpaytransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].billpaytransfers
				UNION
					SELECT 
						p2ptransfers.transactionId AS transactionId,
						p2ptransfers.featureActionId AS featureActionId,
						p2ptransfers.fromAccountNumber AS fromAccountNumber,
						p2ptransfers.amount AS amount,
						p2ptransfers.requestId AS requestId,
						p2ptransfers.createdby AS createdby,
						p2ptransfers.createdts AS createdts,
						p2ptransfers.frequencyTypeId AS frequencyTypeId,
						p2ptransfers.status AS status,
						p2ptransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN p2ptransfers.payeeName IS NULL OR p2ptransfers.payeeName = '''' THEN 
							CASE
								WHEN p2ptransfers.personId IS NULL OR p2ptransfers.personId = '''' THEN 
									CASE 
										WHEN p2ptransfers.p2pContact IS NULL OR p2ptransfers.p2pContact = '''' THEN p2ptransfers.toAccountNumber
										ELSE p2ptransfers.p2pContact
									END
								ELSE p2ptransfers.personId
							END
							ELSE p2ptransfers.payeeName
						END 
                        AS payeeId,
						p2ptransfers.companyId AS companyId,
		                p2ptransfers.softdeleteflag AS softdeleteflag,
						p2ptransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].p2ptransfers
				UNION
					SELECT 
						wiretransfers.transactionId AS transactionId,
						wiretransfers.featureActionId AS featureActionId,
						wiretransfers.fromAccountNumber AS fromAccountNumber,
						wiretransfers.amount AS amount,
						wiretransfers.requestId AS requestId,
						wiretransfers.createdby AS createdby,
						wiretransfers.createdts AS createdts,
						null AS frequencyTypeId,
						wiretransfers.status AS status,
						null AS numberOfRecurrences,
                        CASE 
							WHEN wiretransfers.payeeName IS NULL OR wiretransfers.payeeName = '''' THEN 
							CASE
								WHEN wiretransfers.payPersonName IS NULL OR wiretransfers.payPersonName = '''' THEN 
									CASE 
										WHEN wiretransfers.payeeId IS NULL OR wiretransfers.payeeId = '''' THEN wiretransfers.payeeAccountNumber
										ELSE wiretransfers.payeeId
									END
								ELSE wiretransfers.payPersonName
							END
							ELSE wiretransfers.payeeName
						END 
                        AS payeeId,
						wiretransfers.companyId AS companyId,
		                wiretransfers.softdeleteflag AS softdeleteflag,
						wiretransfers.createdts AS scheduledDate
					FROM
						[${dbxschemaname}].wiretransfers
				UNION
					SELECT 
						intrabanktransfers.transactionId AS transactionId,
						intrabanktransfers.featureActionId AS featureActionId,
						intrabanktransfers.fromAccountNumber AS fromAccountNumber,
						intrabanktransfers.amount AS amount,
						intrabanktransfers.requestId AS requestId,
						intrabanktransfers.createdby AS createdby,
						intrabanktransfers.createdts AS createdts,
						intrabanktransfers.frequencyTypeId AS frequencyTypeId,
						intrabanktransfers.status AS status,
						intrabanktransfers.numberOfRecurrences AS numberOfRecurrences,
                        CASE 
							WHEN intrabanktransfers.payeeName IS NULL OR intrabanktransfers.payeeName = '''' THEN 
							CASE
								WHEN intrabanktransfers.payPersonName IS NULL OR intrabanktransfers.payPersonName = '''' THEN 
									CASE 
										WHEN intrabanktransfers.personId IS NULL OR intrabanktransfers.personId = '''' THEN intrabanktransfers.toAccountNumber
										ELSE intrabanktransfers.personId
									END
								ELSE intrabanktransfers.payPersonName
							END
							ELSE intrabanktransfers.payeeName
						END 
                        AS payeeId,
						intrabanktransfers.companyId AS companyId,
		                intrabanktransfers.softdeleteflag AS softdeleteflag,
						intrabanktransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].intrabanktransfers
				UNION
					SELECT 
						interbankfundtransfers.transactionId AS transactionId,
						interbankfundtransfers.featureActionId AS featureActionId,
						interbankfundtransfers.fromAccountNumber AS fromAccountNumber,
						interbankfundtransfers.amount AS amount,
						interbankfundtransfers.requestId AS requestId,
						interbankfundtransfers.createdby AS createdby,
						interbankfundtransfers.createdts AS createdts,
						interbankfundtransfers.frequencyTypeId AS frequencyTypeId,
						interbankfundtransfers.status AS status,
						interbankfundtransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN interbankfundtransfers.payeeName IS NULL OR interbankfundtransfers.payeeName = '''' THEN 
							CASE
								WHEN interbankfundtransfers.payPersonName IS NULL OR interbankfundtransfers.payPersonName = '''' THEN 
									CASE 
										WHEN interbankfundtransfers.personId IS NULL OR interbankfundtransfers.personId = '''' THEN interbankfundtransfers.toAccountNumber
										ELSE interbankfundtransfers.personId
									END
								ELSE interbankfundtransfers.payPersonName
							END
							ELSE interbankfundtransfers.payeeName
						END 
                        AS payeeId,
						interbankfundtransfers.companyId AS companyId,
		                interbankfundtransfers.softdeleteflag AS softdeleteflag,
						interbankfundtransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].interbankfundtransfers
				UNION
					SELECT 
						internationalfundtransfers.transactionId AS transactionId,
						internationalfundtransfers.featureActionId AS featureActionId,
						internationalfundtransfers.fromAccountNumber AS fromAccountNumber,
						internationalfundtransfers.amount AS amount,
						internationalfundtransfers.requestId AS requestId,
						internationalfundtransfers.createdby AS createdby,
						internationalfundtransfers.createdts AS createdts,
						internationalfundtransfers.frequencyTypeId AS frequencyTypeId,
						internationalfundtransfers.status AS status,
						internationalfundtransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN internationalfundtransfers.payeeName IS NULL OR internationalfundtransfers.payeeName = '''' THEN 
							CASE
								WHEN internationalfundtransfers.payPersonName IS NULL OR internationalfundtransfers.payPersonName = '''' THEN 
									CASE 
										WHEN internationalfundtransfers.personId IS NULL OR internationalfundtransfers.personId = '''' THEN internationalfundtransfers.toAccountNumber
										ELSE internationalfundtransfers.personId
									END
								ELSE internationalfundtransfers.payPersonName 
							END
							ELSE internationalfundtransfers.payeeName
						END 
                        AS payeeId,
						internationalfundtransfers.companyId AS companyId,
		                internationalfundtransfers.softdeleteflag AS softdeleteflag,
						internationalfundtransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].internationalfundtransfers
				UNION
					SELECT 
						ownaccounttransfers.transactionId AS transactionId,
						ownaccounttransfers.featureActionId AS featureActionId,
						ownaccounttransfers.fromAccountNumber AS fromAccountNumber,
						ownaccounttransfers.amount AS amount,
						ownaccounttransfers.requestId AS requestId,
						ownaccounttransfers.createdby AS createdby,
						ownaccounttransfers.createdts AS createdts,
						ownaccounttransfers.frequencyTypeId AS frequencyTypeId,
						ownaccounttransfers.status AS status,
						ownaccounttransfers.numberOfRecurrences AS numberOfRecurrences,
						CASE 
							WHEN ownaccounttransfers.payeeName IS NULL OR ownaccounttransfers.payeeName = '''' THEN 
							CASE
								WHEN ownaccounttransfers.payPersonName IS NULL OR ownaccounttransfers.payPersonName = '''' THEN 
									CASE 
										WHEN ownaccounttransfers.personId IS NULL OR ownaccounttransfers.personId = '''' THEN ownaccounttransfers.toAccountNumber
										ELSE ownaccounttransfers.personId
									END
								ELSE ownaccounttransfers.payPersonName
							END
							ELSE ownaccounttransfers.payeeName
						END 
                        AS payeeId,
						ownaccounttransfers.companyId AS companyId,
		                ownaccounttransfers.softdeleteflag AS softdeleteflag,
						ownaccounttransfers.scheduledDate AS scheduledDate
					FROM
						[${dbxschemaname}].ownaccounttransfers
				) AS generaltransaction
		        LEFT JOIN [${dbxschemaname}].customer ON (generaltransaction.createdby = customer.id)
		        LEFT JOIN [${dbxschemaname}].customeraccounts ON ((generaltransaction.createdby = customeraccounts.Customer_id)
						AND (generaltransaction.fromAccountNumber = customeraccounts.Account_id))
		        LEFT JOIN [${dbxschemaname}].bbrequest ON (generaltransaction.requestId = bbrequest.requestId)
		        LEFT JOIN [${dbxschemaname}].featureaction ON (generaltransaction.featureActionId = featureaction.id)
                LEFT JOIN [${dbxschemaname}].feature ON (featureaction.Feature_id = feature.id)
        
        	WHERE generaltransaction.softdeleteflag = 0
				'+@companyQuery+'
				 AND generaltransaction.transactionId LIKE '''+@_transactionId +''' AND generaltransaction.featureActionId LIKE '''+@_featureActionId
				+''' '+@accountsQuery
                +' AND [${dbxschemaname}].FIND_IN_SET(generaltransaction.featureActionId, CAST('''+ @createActions+''' as nvarchar(max))) <> 0 '+
                @queryTypecondition +' '+
                @searchQuery+' ) AS t1
            INNER JOIN
            ( 
				SELECT 
					bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
				FROM
				 [${dbxschemaname}].bbrequest
				LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN [${dbxschemaname}].approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId as nvarchar(max)), CAST('''+@companyRequestIds+''' as nvarchar(max))) <> 0
				GROUP BY bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' ' + @_sortOrder + ' ' +
            @paginationQuery
         exec(@select_statement)
		 DROP TABLE #approvalCount1;
		DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_achtransaction_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_achtransaction_proc]  
   @_customerId nvarchar(50),
   @_transactionId nvarchar(max),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(max),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(max),
   @_sortByParam nvarchar(max),
   @_sortOrder nvarchar(max),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   
   BEGIN
	SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     DECLARE @validAccountsJoin nvarchar(max)
	 DECLARE @isSelfApprovalEnabled bit
	 DECLARE @notCreatedBySelf nvarchar(max)
	 

      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

      DECLARE
            @db_null_statement int

            DECLARE
               @db_null_statement$2 int

      SET @combinedIds = ( SELECT String_agg(customer.id, ',') FROM [${dbxschemaname}].customer WHERE customer.combinedUserId = @_customerId)
      IF @combinedIds IS NULL
         BEGIN
            SET @combinedIds = @_customerId
         END
      ELSE 
         SET @combinedIds = (@_customerId + ',' + @combinedIds)

	  SET @isSelfApprovalEnabled = (select isSelfApprovalEnabled from [${dbxschemaname}].application);

	  IF (@isSelfApprovalEnabled = 0)
		SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(achtransaction.createdby, ''', @combinedIds,''') = 0')
	  ELSE
		SET @notCreatedBySelf = '';

		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='') THEN '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE @_filterByValue END
        SET @_transactionId = CASE WHEN (@_transactionId = '' OR @_transactionId IS NULL) THEN '%' ELSE @_transactionId END
		SET @numOfParams = 0;

      IF LEN(@_filterByParam) > 0
         BEGIN
            SET @numOfParams = LEN(@_filterByParam) - LEN(replace(@_filterByParam, ',', '')) + 1
         END

	SET @searchQuery = ''

      WHILE (@idx <= @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx), ',', -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',', @idx), ',', -1 );
            SET @searchQuery = (@searchQuery + ' AND (' + @filterParam + ' LIKE ''' + @filterValue + ''' )')
            SET @idx = @idx + 1

         END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         BEGIN         
            SET @companyId = ''
         END

      IF @_featureactionlist IS NULL
         BEGIN
            GOTO MAINLABEL$leave
         END

		SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam is NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder is NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         BEGIN
            SET @customerMatrixIds = ''
         END

      SET @alreadyApprovedIds = ( SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].bbactedrequest.action = 'Approved')

      IF @alreadyApprovedIds IS NULL
         BEGIN
            SET @alreadyApprovedIds = ''
         END
      SET @approvalRequestIds = 
         (
            SELECT String_agg(CAST(requestId as nvarchar(max)) ,',')
            FROM 
               [${dbxschemaname}].requestapprovalmatrix 
                  INNER JOIN [${dbxschemaname}].approvalmatrix 
                  ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id 
                  INNER JOIN [${dbxschemaname}].approvalrule 
                  ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
            WHERE 
               [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)), @customerMatrixIds) <> 0 AND 
               NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0 AND 
               (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < 
               (
                  SELECT count_big(DISTINCT ([${dbxschemaname}].customerapprovalmatrix.customerId))
                  FROM [${dbxschemaname}].customerapprovalmatrix
                  WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
               )) OR ([${dbxschemaname}].approvalrule.numberOfApprovals <> -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < [${dbxschemaname}].approvalrule.numberOfApprovals))            
         )
      IF @approvalRequestIds IS NULL
         BEGIN
            SET @approvalRequestIds = ''
         END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, ''') + (@combinedIds) + (''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achtransaction.requestId AS nvarchar(max)),  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].achtransaction.status = ''Pending'' ') + @notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN (' AND [${dbxschemaname}].achtransaction.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_transactionId = '%') THEN (' AND NOT [${dbxschemaname}].achtransaction.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END
      SET @validAccountsJoin = 
         CASE 
            WHEN (@_queryType = '') THEN 
            (
               ' INNER JOIN (SELECT DISTINCT Account_id, REPLACE(Action_id, ''_VIEW'', ''_CREATE'') as Action_id
                 FROM [${dbxschemaname}].customeraction WHERE [${dbxschemaname}].customeraction.Customer_id = '''+@_customerId+''' AND [${dbxschemaname}].customeraction.isAllowed = ''1''
                 AND [${dbxschemaname}].customeraction.Account_id is NOT null AND [${dbxschemaname}].customeraction.Action_id like ''%_VIEW'') as can ON ([${dbxschemaname}].achtransaction.featureActionId = can.Action_id
                 AND [${dbxschemaname}].achtransaction.fromAccount = can.Account_id) '
            )
            ELSE ''
         END

		 SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery ELSE 
							(@searchQuery+ ' AND (achtransaction.templateName LIKE ''%'+@_searchString+'%'' OR customeraccounts.AccountName LIKE ''%'+
							@_searchString+'%'' OR bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR 
							achtransaction.transaction_id LIKE ''%'+@_searchString+'%'' OR achtransaction.fromAccount LIKE ''%'+@_searchString+'%'' )') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' ELSE
								(' OFFSET '+@_pageOffset+ '   ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY ') END 
      SET @features = 
         (
            SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0
         )
      IF @features IS NULL
         BEGIN
            SET @features = ''
         END
      SET @createActions = 
         (
            SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE'
         )
      IF @createActions IS NULL
         BEGIN
            SET @createActions = ''
         END
      SET @companyRequestIds = 
         (
            SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)), ',')
            FROM [${dbxschemaname}].bbrequest
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
         )
      IF @companyRequestIds IS NULL
         BEGIN
            SET @companyRequestIds = ''
         END         

      SET @customerAcounts = 
         (
            SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)), ',')
            FROM [${dbxschemaname}].customeraccounts
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id, @combinedIds) <> 0
         )
      IF @customerAcounts IS NULL
         BEGIN
            SET @customerAcounts = ''
         END

	
	SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

      SET @select_statement = '

         SELECT * FROM
            (SELECT 
         [${dbxschemaname}].achtransaction.transaction_id AS transaction_id,
         [${dbxschemaname}].achtransaction.fromAccount AS fromAccount,
         [${dbxschemaname}].achtransaction.effectiveDate AS effectiveDate,
         [${dbxschemaname}].achtransaction.requestId AS requestId,
         [${dbxschemaname}].achtransaction.createdby AS createdby,
         [${dbxschemaname}].achtransaction.createdts AS createdts,
         [${dbxschemaname}].achtransaction.maxAmount AS maxAmount,
         [${dbxschemaname}].achtransaction.status AS status,
         [${dbxschemaname}].achtransaction.transactionType_id AS transactionType_id,
         [${dbxschemaname}].achtransaction.templateType_id AS templateType_id,
         [${dbxschemaname}].achtransaction.companyId AS companyId,
         [${dbxschemaname}].achtransaction.templateRequestType_id AS templateRequestType_id,
         [${dbxschemaname}].achtransaction.softDelete AS softDelete,
         [${dbxschemaname}].achtransaction.templateName AS templateName,
         [${dbxschemaname}].achtransaction.template_id AS template_id,
         [${dbxschemaname}].achtransaction.totalAmount AS totalAmount,
         [${dbxschemaname}].achtransaction.featureActionId AS featureActionId,
         [${dbxschemaname}].achtransaction.confirmationNumber AS confirmationNumber,
         ( CASE 
            WHEN [${dbxschemaname}].customeraccounts.AccountName is NULL THEN ''AccountName'' 
                ELSE [${dbxschemaname}].customeraccounts.AccountName 
            END ) AS accountName,
         [${dbxschemaname}].customer.UserName AS userName,
         [${dbxschemaname}].bbtransactiontype.transactionTypeName AS transactionTypeName,
         [${dbxschemaname}].bbtemplatetype.templateTypeName AS templateTypeName,
         [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
         [${dbxschemaname}].bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achtransaction.createdby AS nvarchar(max)),CAST(''' + @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)), CAST('''+ @approvalRequestIds +''' as nvarchar(max))) >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         (((((([${dbxschemaname}].achtransaction
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].achtransaction.createdby = [${dbxschemaname}].customeraccounts.Customer_id)
            AND ([${dbxschemaname}].achtransaction.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
         LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].achtransaction.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].achtransaction.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
         LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].achtransaction.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achtransaction.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achtransaction.softDelete = ''0''
            AND ( [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.companyId, '''+ @companyId + ''') <> 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.createdby, CAST('''+ @combinedIds + ''' as nvarchar(max)))<>0)
            AND [${dbxschemaname}].achtransaction.transaction_id LIKE '''+ @_transactionId + '''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.fromAccount, CAST('''+ @customerAcounts + ''' as nvarchar(max)))<>0 
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achtransaction.featureActionId, CAST(''' + @createActions + ''' as nvarchar(max)))<>0 ' + 
                @queryTypecondition + ' ' +
                @searchQuery + ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)), CAST(''' + @companyRequestIds + ''' as nvarchar(max)))<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' ' + @_sortOrder + ' ' +
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	 DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:



GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_achfiles_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_achfiles_proc]  
   @_customerId nvarchar(50),
   @_achFile_id nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 

   BEGIN

     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
	 DECLARE @isSelfApprovalEnabled bit
	 DECLARE @notCreatedBySelf nvarchar(max)
     
      SET @numOfParams = 0
      SET @searchQuery = ''
      SET @idx = 1
      SET @paginationQuery = 0

     SET @combinedIds = (select String_agg(CAST(customer.id as nvarchar(max)), ',') from [${dbxschemaname}].customer where customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)

	   SET @isSelfApprovalEnabled = (select [isSelfApprovalEnabled] from [${dbxschemaname}].application)

	   IF(@isSelfApprovalEnabled = 0)
		BEGIN
			SET @notCreatedBySelf = concat(' AND [${dbxschemaname}].FIND_IN_SET(achfile.createdby, ''',@combinedIds,''')=0')
		END
	   ELSE 
		BEGIN
			SET @notCreatedBySelf = '';
		END
		
		SET @_filterByParam = CASE WHEN (@_filterByParam IS NULL OR @_filterByParam='' ) THEN  '' ELSE @_filterByParam END
        SET @_filterByValue = CASE WHEN (@_filterByValue IS NULL OR @_filterByValue='') THEN '' ELSE  @_filterByValue END
        SET @_achFile_id = CASE WHEN (@_achFile_id = '' OR @_achFile_id IS NULL) THEN '%' ELSE @_achFile_id END


      IF datalength(@_filterByParam) > 0
         SET @numOfParams = datalength(@_filterByParam) - datalength(replace(@_filterByParam, ',', '')) + 1
      
      WHILE (@idx < @numOfParams)
         BEGIN
            SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByParam, @idx), -1 );
            SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX(',', [${dbxschemaname}].SUBSTRING_INDEX(',', @_filterByValue,   @idx), -1 );
            SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
            SET @idx = @idx + 1   
         END

      SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
      IF @companyId IS NULL
         SET @companyId = ''
      IF @_featureactionlist IS NULL
         GOTO MAINLABEL$leave

        SET @_sortByParam = CASE WHEN (@_sortByParam = '' OR @_sortByParam IS NULL) THEN 'createdts' ELSE @_sortByParam END
		SET @_sortOrder = CASE WHEN (@_sortOrder = '' OR @_sortOrder IS NULL) THEN 'DESC' ELSE @_sortOrder END



      SET @customerMatrixIds = ( SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customerapprovalmatrix.customerId, @combinedIds) <> 0)
      IF @customerMatrixIds IS NULL
         SET @customerMatrixIds = ''
      
      SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END


      SET @approvalRequestIds = (SELECT STRING_AGG(CAST(requestId as nvarchar(max)), ',') 
        							FROM [${dbxschemaname}].requestapprovalmatrix
        							INNER JOIN [${dbxschemaname}].approvalmatrix ON requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
									INNER JOIN [${dbxschemaname}].approvalrule ON approvalmatrix.approvalruleId = approvalrule.id
        								WHERE [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.approvalMatrixId as nvarchar(max)),  @customerMatrixIds)  <> 0
        								AND [${dbxschemaname}].FIND_IN_SET(CAST(requestapprovalmatrix.requestId as nvarchar(max)), @alreadyApprovedIds) = 0
        								AND ((approvalrule.numberOfApprovals = -1 AND requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)) 
												OR
											(approvalrule.numberOfApprovals != -1 AND requestapprovalmatrix.receivedApprovals < approvalrule.numberOfApprovals))
        							)

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

      SET @queryTypecondition = 
         CASE 
            WHEN (@_queryType = 'myRequests') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, ''' + (@combinedIds) + ''')<>0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'' ) ')
            ELSE 
               CASE 
                  WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].achfile.requestId AS nvarchar(max)),  ''' + (@approvalRequestIds) + ''')<>0  AND [${dbxschemaname}].achfile.status = ''Pending'' ') + @notCreatedBySelf
                  ELSE 
                     CASE 
                        WHEN (@_queryType = 'rejected') THEN ('AND [${dbxschemaname}].achfile.status = ''Rejected'' ')
                        ELSE 
                           CASE 
                              WHEN (@_achFile_id = '%') THEN (' AND NOT [${dbxschemaname}].achfile.status = ''Withdrawn''')
                              ELSE ''
                           END
                     END
               END
         END


		SET @searchQuery = CASE WHEN (@_searchString IS NULL OR @_searchString = '') THEN @searchQuery ELSE 
                (@searchQuery+ ' AND (achfile.achFileName LIKE ''%'+@_searchString+'%'' OR achfile.requestType LIKE ''%'+@_searchString+'%'')') END
        
        SET @paginationQuery = CASE WHEN (@_pageOffset IS NULL OR @_pageOffset = '' OR @_pageSize IS NULL OR @_pageSize = '') THEN
		'' ELSE (' OFFSET '+@_pageOffset+'  ROWS FETCH NEXT  ' +@_pageSize+'  ROWS ONLY') END


      SET @features = ( SELECT String_agg([${dbxschemaname}].featureaction.Feature_id ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg([${dbxschemaname}].featureaction.id,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_UPLOAD')
      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg([${dbxschemaname}].bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END


	 SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='''' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2

	  
	  
	 SET @select_statement = ' 
	  SELECT * FROM
        (SELECT 
         achfile.achFile_id AS achFile_id,
         achfile.achFileName AS achFileName,
         achfile.featureActionId AS featureActionId,
         achfile.debitAmount AS debitAmount,
         achfile.approvalAccounts AS approvalAccounts,
         achfile.debitAccounts AS debitAccounts,
         achfile.createdby AS createdby,
         achfile.createdts AS createdts,
         achfile.requestType AS requestType,
         achfile.numberOfCredits AS numberOfCredits,
         achfile.numberOfDebits AS numberOfDebits,
         achfile.numberOfPrenotes AS numberOfPrenotes,
         achfile.requestId AS requestId,
         achfile.contents AS contents,
         achfile.fileSize AS fileSize,
         achfile.softDelete AS softDelete,
         achfile.creditAmount AS creditAmount,
         achfile.numberOfRecords AS numberOfRecords,
         achfile.achFileFormatType_id AS achFileFormatType_id,
         achfile.status AS status,
         achfile.companyId AS companyId,
         achfile.confirmationNumber AS confirmationNumber,
         customer.UserName AS userName,
         achfileformattype.fileType AS achFileFormatType,
         bbrequest.createdby AS requestCreatedby,
         (CASE
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @combinedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as amICreator,
         (CASE 
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),'''+ @approvalRequestIds +''') >0 THEN ''true''
            ELSE ''false''
         END)
          as amIApprover
      FROM
         (((([${dbxschemaname}].achfile
         LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].achfile.createdby = [${dbxschemaname}].customer.id))
         LEFT JOIN [${dbxschemaname}].achfileformattype ON ([${dbxschemaname}].achfile.achFileFormatType_id = [${dbxschemaname}].achfileformattype.id))
         LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].achfile.requestId = [${dbxschemaname}].bbrequest.requestId))
         WHERE [${dbxschemaname}].achfile.softDelete = ''0''
            AND ([${dbxschemaname}].FIND_IN_SET(achfile.companyId, '''+ @companyId + ''') <> 0 OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.createdby, '''+ @combinedIds + ''')<>0)
            AND [${dbxschemaname}].achfile.achFile_id LIKE '''+@_achFile_id + '''
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].achfile.featureActionId, '''+ @createActions + ''')<>0 '+
                @queryTypecondition + ' '+
                @searchQuery + ') AS t1
            LEFT JOIN
            ( 
            SELECT 
               bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
             [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+ @companyRequestIds +''')<>0 
            GROUP BY [${dbxschemaname}].bbrequest.requestId
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam + ' '+ @_sortOrder + ' ' +
            @paginationQuery
            EXEC(@select_statement)
	DROP TABLE #approvalCount1;
	DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc]  
   @_customerId nvarchar(50),
   @_transactionIds nvarchar(50),
   @_requestIds nvarchar(max),
   @_featureactionlist nvarchar(50)
AS 
   BEGIN
     SET  XACT_ABORT  ON
     SET  NOCOUNT  ON
     DECLARE @combinedIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
	 DECLARE @companyId nvarchar(max)
	 DECLARE @customerMatrixIds nvarchar(max)
	 DECLARE @approvalRequestIds nvarchar(max)
	 DECLARE @features nvarchar(max)
	 DECLARE @monetaryActions nvarchar(max)

     
     SET @combinedIds = (select String_agg(customer.id, ',') from [${dbxschemaname}].customer where [${dbxschemaname}].customer.combinedUserId = @_customerId)
     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
	IF (@combinedIds IS NULL OR @combinedIds = '')
		SET @combinedIds = ''''
	ELSE
		SET @combinedIds = @combinedIds

	IF (@_transactionIds IS NULL OR @_transactionIds = '')
		SET @_transactionIds = ''''
	ELSE
		SET @_transactionIds = @_transactionIds
	
	IF (@_requestIds IS NULL OR @_requestIds = '')
		SET @_requestIds = ''''
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
      
	  SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(bbactedrequest.createdby, @combinedIds) <> 0 AND [${dbxschemaname}].FIND_IN_SET(bbactedrequest.action, 'Pending') <> 1 AND bbactedrequest.softdeleteflag = 0)
      IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''''
      END

      SET @approvalRequestIds = 
         (  
         SELECT String_agg(CAST(requestapprovalmatrix.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND NOT [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) <> 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT([${dbxschemaname}].customerapprovalmatrix.customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

     IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

     
      SET @features = ( SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) <> 0 )
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @monetaryActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.Type_id = 'MONETARY')
      IF @monetaryActions IS NULL
        BEGIN
          SET @monetaryActions = ''
        END

	DECLARE @companyRequestIds nvarchar(50)
      SET @companyRequestIds = ( SELECT String_agg(bbrequest.requestId ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @monetaryActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END

	DECLARE @requestIds nvarchar(50)
	IF @_requestIds = ''''
          SET @requestIds = @companyRequestIds
    ELSE
		SET @requestIds = @_requestIds

	DECLARE @query nvarchar(50)
	IF @_transactionIds = '''' 
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.requestId,',@_requestIds) 
	ELSE
	SET @query = CONCAT('[${dbxschemaname}].FIND_IN_SET(bbrequest.transactionId,',@_transactionIds,'AND [${dbxschemaname}].FIND_IN_SET(bbrequest.featureActionId,',@monetaryActions);

   DECLARE @select_statement nvarchar(50)
	 SET @select_statement = CONCAT(
			'SELECT 
             bbrequest.requestId,
             bbrequest.transactionId,
             bbrequest.status,
			 bbrequest.featureActionId,
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
            WHEN [${dbxschemaname}].FIND_IN_SET(CAST(achfile.createdby AS nvarchar(max)),''' + @alreadyApprovedIds +''') > 0 THEN ''true''
            ELSE ''false''
          END) as actedByMeAlready,
          (select count(DISTINCT(createdby)) from [${dbxschemaname}].bbactedrequest where bbactedrequest.action = Approved AND  bbactedrequest.requestId = bbrequest.requestId) 
							as receivedApprovals,
                    LEAST(
						(SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId in (SELECT approvalMatrixId FROM [${dbxschemaname}].requestapprovalmatrix where requestapprovalmatrix.requestId = bbrequest.requestId)) 
						, 
						SUM(
							CASE approvalrule.numberOfApprovals
								WHEN -1 THEN (SELECT COUNT(*) FROM [${dbxschemaname}].customerapprovalmatrix WHERE customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
								WHEN NULL OR \"\" THEN 0
								ELSE approvalrule.numberOfApprovals
							END
						) 
					) as requiredApprovals
				FROM
				 bbrequest
				LEFT JOIN requestapprovalmatrix ON (bbrequest.requestId = requestapprovalmatrix.requestId)
				LEFT JOIN approvalmatrix ON (requestapprovalmatrix.approvalMatrixId = approvalmatrix.id)
				LEFT JOIN approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE ',@query,'
				GROUP BY bbrequest.requestId'
				);
    IF @select_statement IS NULL
        GOTO MAINLABEL$leave
    exec(@select_statement)
	End
    MAINLABEL$leave: 
  
GO

ALTER TABLE [${dbxschemaname}].[featureaction] ADD [approveFeatureAction] VARCHAR(255) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [serviceCharge] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [transactionAmount] VARCHAR(45) DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [convertedAmount] VARCHAR(45) DEFAULT NULL;

GO

DROP procedure IF EXISTS [${dbxschemaname}].[group_actions_remove_proc];
GO
CREATE PROCEDURE  [${dbxschemaname}].[group_actions_remove_proc](  
@_action varchar(255),
@_customerIdValues nvarchar(MAX) 
)
AS
BEGIN
  DECLARE @index1 INTEGER = 0;
  DECLARE @numOfRecords INT;
  DECLARE @customerValues NVARCHAR(MAX);
  DECLARE @customerId NVARCHAR(MAX);
  DECLARE @coreCustomerId NVARCHAR(MAX);
	set @numOfRecords = LEN(@_customerIdValues) - LEN(REPLACE(@_customerIdValues, ',', '')) + 1;
  WHILE 1=1 BEGIN 
     set @index1 = @index1 + 1;
		IF @index1 = @numOfRecords + 1 BEGIN 
			break;
		end
		else begin
		    set @customerValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_customerIdValues, ',', @index1), ',', -1 );
			set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(@customerValues,'.', 1 );
			set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX(@customerValues,'.', -1 );
			delete from [${dbxschemaname}].[customeraction] where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = @_action;
		END 
		
	END 
END

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[group_limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[group_limits_update_proc](
@_action varchar(255),
@_maxTxLimit decimal(20,2),
@_dailyLimit decimal(20,2),
@_weeklyLimit decimal(20,2),
@_customerIdValues nvarchar(max)
  
)
AS
BEGIN
  DECLARE @index1 INTEGER = 0;
  DECLARE @numOfRecords INT;
  DECLARE @customerValues NVARCHAR(MAX);
  DECLARE @customerId NVARCHAR(MAX);
  DECLARE @coreCustomerId NVARCHAR(MAX);
	set @numOfRecords = LEN(@_customerIdValues) - LEN(REPLACE(@_customerIdValues, ',', '')) + 1;
  WHILE 1=1 BEGIN 
     set @index1 = @index1 + 1;
		IF @index1 = @numOfRecords + 1 BEGIN 
			break;
		end
		else begin
		    set @customerValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_customerIdValues, ',', @index1), ',', -1 );
			set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(@customerValues,'.',1);
			set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX(@customerValues,'.',-1);
			UPDATE [${dbxschemaname}].[customeraction] SET value = @_maxTxLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
			UPDATE [${dbxschemaname}].[customeraction] SET value = @_dailyLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit;
			UPDATE [${dbxschemaname}].[customeraction] SET value = @_weeklyLimit where Customer_id = @customerId AND coreCustomerId = @coreCustomerId AND Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
		END 
		
	END 
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[feature_action_limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[feature_action_limits_update_proc](
@_action varchar(255),
@_minTxLimit decimal(20,2),
@_maxTxLimit decimal(20,2),
@_dailyLimit decimal(20,2),
@_weeklyLimit decimal(20,2)
)
AS
BEGIN
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_minTxLimit where Action_id = @_action AND LimitType_id = 'MIN_TRANSACTION_LIMIT'; 
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT'; 
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT';
  UPDATE [${dbxschemaname}].[actionlimit] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT';	
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[limits_update_proc](
  @_action varchar(255) ,
  @_maxTxLimit decimal(20,2),
  @_dailyLimit decimal(20,2),
  @_weeklyLimit decimal(20,2)
)
AS
BEGIN
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit;
  UPDATE [${dbxschemaname}].[groupactionlimit] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit;
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_maxTxLimit where actionId = @_action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_dailyLimit where actionId = @_action AND limitTypeId = 'DAILY_LIMIT' AND value > @_dailyLimit;
  UPDATE [${dbxschemaname}].[servicedefinitionactionlimit] SET value = @_weeklyLimit where actionId = @_action AND limitTypeId = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_maxTxLimit where actionId = @_action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_dailyLimit where actionId = @_action AND limitTypeId = 'DAILY_LIMIT' AND value > @_dailyLimit;
  UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_weeklyLimit where actionId = @_action AND limitTypeId = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_maxTxLimit where Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_dailyLimit where Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit;
  UPDATE [${dbxschemaname}].[customeraction] SET value = @_weeklyLimit where Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[servicedefinition_action_limits_update_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[servicedefinition_action_limits_update_proc](
  @_action varchar(255),
  @_maxTxLimit decimal(20,2),
  @_dailyLimit decimal(20,2),
  @_weeklyLimit decimal(20,2),
  @_contractIdValues nvarchar(max)
)
AS
BEGIN
  DECLARE @index1 INTEGER = 0;
  DECLARE @numOfRecords INT;
  DECLARE @contractId nvarchar(50);
  set @numOfRecords = LEN(@_contractIdValues) - LEN(REPLACE(@_contractIdValues, ',', '')) + 1;
   WHILE 1=1 BEGIN 
     set @index1 = @index1 + 1;
		IF @index1 = @numOfRecords + 1 BEGIN 
			break ;
  		end
		  else begin
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractIdValues, ',', @index1), ',', -1 );
           UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_maxTxLimit where contractId = @contractId AND actionId = @_action AND limitTypeId = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
           UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_dailyLimit where contractId = @contractId AND actionId = @_action AND limitTypeId = 'DAILY_LIMIT' AND value > @_dailyLimit;
           UPDATE [${dbxschemaname}].[contractactionlimit] SET value = @_weeklyLimit where contractId = @contractId AND actionId = @_action AND limitTypeId = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
           UPDATE [${dbxschemaname}].[customeraction] SET value = @_maxTxLimit where contractId = @contractId AND Action_id = @_action AND LimitType_id = 'MAX_TRANSACTION_LIMIT' AND value > @_maxTxLimit; 
           UPDATE [${dbxschemaname}].[customeraction] SET value = @_dailyLimit where contractId = @contractId AND Action_id = @_action AND LimitType_id = 'DAILY_LIMIT' AND value > @_dailyLimit;
           UPDATE [${dbxschemaname}].[customeraction] SET value = @_weeklyLimit where contractId = @contractId AND Action_id = @_action AND LimitType_id = 'WEEKLY_LIMIT' AND value > @_weeklyLimit; 
		   END 
	END 
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[servicedefinition_remove_actions_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[servicedefinition_remove_actions_proc](
@_action varchar(255),
@_contractIdValues nvarchar(max)
)
AS
BEGIN
  DECLARE @numOfRecords INT;
  DECLARE @contractId nvarchar(50);
  DECLARE @index1 INTEGER = 0;
  set @numOfRecords = LEN(@_contractIdValues) - LEN(REPLACE(@_contractIdValues, ',', '')) + 1;
   WHILE 1=1 BEGIN 
     set @index1 = @index1 + 1;
		IF @index1 = @numOfRecords + 1 BEGIN 
			break;
  		end
		  else begin
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractIdValues, ',', @index1), ',', -1 );
		   delete from [${dbxschemaname}].[contractactionlimit] where contractId = @contractId AND actionId = @_action;
		   delete from [${dbxschemaname}].[customeraction] where contractId = @contractId AND Action_id = @_action;
		   END 
	END 
END
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_request_history_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_request_history_proc]  
   @_customerId nvarchar(50),
   @_requestId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      DECLARE @companyId nvarchar(max)

      SET @companyId = (select String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)), ',') from [${dbxschemaname}].contractcustomers where contractcustomers.customerId = @_customerId)
      IF @companyId is NULL      
        SET @companyId = '';

	  SELECT 
         [bbactedrequest].[approvalId] AS approvalId, 
         [bbactedrequest].[requestId] AS requestId, 
         [bbactedrequest].[companyId] AS companyId, 
         [bbactedrequest].[createdby] AS requestActedby, 
         [bbactedrequest].[status] AS status, 
         [bbactedrequest].[comments] AS comments, 
         [bbactedrequest].[action] AS action, 
         [bbactedrequest].[createdts] AS actionts, 
         [bbactedrequest].[softdeleteflag] AS softdeleteflag, 
         [customer].[UserName] AS userName, 
         CASE 
            WHEN [${dbxschemaname}].[bbactedrequest].[createdby] IS NULL THEN 'System'
            ELSE (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[FirstName]) <> 0) THEN [${dbxschemaname}].[customer].[FirstName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[MiddleName]) <> 0) THEN [${dbxschemaname}].[customer].[MiddleName]
               ELSE ''
            END) + ' ' + (CASE 
               WHEN (datalength([${dbxschemaname}].[customer].[LastName]) <> 0) THEN [${dbxschemaname}].[customer].[LastName]
               ELSE ''
            END)
         END AS customerName, 
         [customer].[FullName] AS customerFullName
      FROM ([${dbxschemaname}].[bbactedrequest] 
         LEFT JOIN [${dbxschemaname}].[customer] 
         ON ([bbactedrequest].[createdby] = [customer].[id]))
      WHERE 
         CAST([bbactedrequest].[requestId] as nvarchar(max))= @_requestId AND 
         [${dbxschemaname}].FIND_IN_SET(bbactedrequest.companyId , @companyId) <> 0
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_bulkwire_files_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_bulkwire_files_proc] (  
   @createdBy nvarchar(50),
   @searchString nvarchar(50),
   @sortByParam nvarchar(50),
   @sortOrder nvarchar(50),
   @pageOffset int,
   @pageSize int
   )
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @companyId nvarchar(max)
	  declare @isSMEUser bit
	  declare @filterRetail nvarchar(max)
	  declare @filterSME nvarchar(max)
	  declare @getByIdFilter nvarchar(max)

      SET @companyId = (select String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)), ',') from [${dbxschemaname}].contractcustomers where contractcustomers.customerId = @createdby)
		if @companyId is NULL      
        SET @companyId = '';
		
	  SET @companyId = (select concat(@companyId,',',customer.Organization_Id) from [${dbxschemaname}].customer where customer.id =@createdby);
    
      SET @isSMEUser = case when @companyId='' or @companyId is null then 0 else 1 end

      SET @filterRetail = (N'bulkwirefiles.createdBy = ') + (@createdby) + (N' AND bulkwirefiles.softdeleteflag = 0 ')

      SET @filterSME = (N'FIND_IN_SET(bulkwirefiles.company_id,') + (@companyId) +' <> 0 '+ (N' AND bulkwirefiles.softdeleteflag = 0')

      SET @getByIdFilter = 
         CASE 
            WHEN (@isSMEUser <> 0) THEN @filterSME
            ELSE @filterRetail
         END

      SET @sortByParam = 0

      SET @sortByParam = 
         CASE 
            WHEN (@sortByParam = 'username') THEN N'firstName,lastname'
            ELSE @sortByParam
         END

      SET @sortOrder = case when @sortOrder='' or @sortOrder is null then 'DESC' else @sortOrder end

      SET @searchString = case when @searchString ='' or @searchString is null then '' else '%'+@searchString+'%' end
	  declare @orderBy nvarchar(max)
	  declare @paginationQuery nvarchar(max)
	  declare @searchQuery nvarchar(max)

      SET @orderBy = (N' ORDER BY ') + (@sortByParam) + (N' ') + (@sortOrder)

      SET @paginationQuery = case when (@pageOffset ='' or @pageOffset  is null) and (@pageSize ='' or @pageSize is null)
	  then '' else 'OFFSET '+ @pageOffset +' ROWS FETCH NEXT '+@pageSize + ' ROWS ONLY' end

      SET @searchQuery = 
         (N'(bulkwirefiles.bulkWireFileName LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfDomesticTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR bulkwirefiles.noOfInternationalTransactions LIKE ')
          + 
         (@searchString)
          + 
         (N' OR  customer.firstname LIKE ')
          + 
         (@searchString)
          + 
         (N' OR customer.lastname LIKE ')
          + 
         (@searchString)
          + 
         (N')')
		 declare @defaultFilter nvarchar(max)
		 declare @searchFilter nvarchar(max)
		 declare @filter nvarchar(max)
		 declare @select_statement nvarchar(max)
      SET @defaultFilter = (@getByIdFilter) + (@orderBy) + (@paginationQuery)
 
      SET @searchFilter = (@getByIdFilter) + (N' AND ') + (@searchQuery) + (@orderBy)

      SET @filter = 
         CASE 
            WHEN (@searchString = '') THEN @defaultFilter
            ELSE @searchFilter
         END

      SET @select_statement = (N'SELECT bulkwirefiles.bulkWireFileID,bulkwirefiles.bulkWireFileName, bulkwirefiles.noOfTransactions, bulkwirefiles.noOfDomesticTransactions,bulkwirefiles.noOfInternationalTransactions,bulkwirefiles.createdts,bulkwirefiles.lastmodifiedts,bulkwirefiles.lastExecutedOn,customer.id, customer.FirstName as firstname,customer.LastName as lastname FROM ([${dbxschemaname}].bulkwirefiles LEFT JOIN [${dbxschemaname}].customer ON (bulkwirefiles.createdBy = customer.id)) WHERE ') + (@filter)

	  exec(@select_statement)

   END
GO


ALTER TABLE [${dbxschemaname}].[bulkwirefiles] 
DROP CONSTRAINT [bulkwirefiles$FK_bulkwirefiles_CompanyID];

ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] 
DROP CONSTRAINT [ownaccounttransfers$FK_internaltransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[bbrequest] 
DROP CONSTRAINT [bbrequest$FK_bbrequest_businessbankingcompany];

ALTER TABLE [${dbxschemaname}].[bbactedrequest] 
DROP CONSTRAINT [bbactedrequest$FK_bbactedrequest_Organisation_id];

ALTER TABLE [${dbxschemaname}].[interbankfundtransfers] 
DROP CONSTRAINT [interbankfundtransfers$FK_interbankfundtransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[intrabanktransfers] 
DROP CONSTRAINT [intrabanktransfers$FK_intrabanktransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[internationalfundtransfers] 
DROP CONSTRAINT [internationalfundtransfers$FK_externaltransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[p2ptransfers] 
DROP CONSTRAINT [p2ptransfers$FK_p2ptransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[billpaytransfers] 
DROP CONSTRAINT [billpaytransfers$FK_billpaytranfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[wiretransfers] 
DROP CONSTRAINT [wiretransfers$FK_wiretransfers_companyIdx];

ALTER TABLE [${dbxschemaname}].[achtransaction] 
DROP CONSTRAINT [achtransaction$FK_bbtransaction_Organization];

ALTER TABLE [${dbxschemaname}].[bbtemplate] 
DROP CONSTRAINT [bbtemplate$FK_bbtemplate_Organization];

ALTER TABLE [${dbxschemaname}].[achfile] 
DROP CONSTRAINT [achfile$FK_achfile_Company_id];

ALTER TABLE [${dbxschemaname}].[bulkpaymentfiles] 
DROP CONSTRAINT [FK_bulkpaymentfiles_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentfilesmock] 
DROP CONSTRAINT [FK_bulkpaymentfilesmock_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecord] 
DROP CONSTRAINT [FK_bulkpaymentrecord_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentrecordmock] 
DROP CONSTRAINT [FK_bulkpaymentrecordmock_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentrequest] 
DROP CONSTRAINT [FK_bulkpaymentrequest_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentrequestpos] 
DROP CONSTRAINT [FK_bulkpaymentrequestpos_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecord] 
DROP CONSTRAINT [FK_bulkpaymentsubrecord_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymentsubrecordmock] 
DROP CONSTRAINT [FK_bulkpaymentsubrecordmock_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplate] 
DROP CONSTRAINT [FK_bulkpaymenttemplate_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] 
DROP CONSTRAINT [FK_bulkpaymenttemplatepos_companyIdx];

ALTER TABLE [${dbxschemaname}].[bulkwiretemplate] 
DROP CONSTRAINT [bulkwiretemplate$FK2_bulkwiretemplate_CompanyID];

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]
GO  

CREATE PROCEDURE [${dbxschemaname}].[authorizationCheckForRejectAndWithdrawl_proc]  
   @_requestId nvarchar(50),
   @_companyId nvarchar(max),
   @_featureactionlist nvarchar(max)
AS 
   BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE @group_concat_max_len bigint
      SET @group_concat_max_len = 100000000
      DECLARE @features nvarchar(max)
      DECLARE @createActions nvarchar(max)

      SET @features = 
         (
            SELECT string_agg(CAST(featureaction.Feature_id as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureactionlist) > 0
         )

      IF @features IS NULL
         SET @features = ''
		 SET @createActions = 
         (
            SELECT string_agg(CAST(featureaction.id  as nvarchar(max)),',')
            FROM [${dbxschemaname}].featureaction
            WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) > 0 AND [${dbxschemaname}].featureaction.approveFeatureAction IS NOT NULL
         )
         
      IF @createActions IS NULL
         SET @createActions = ''
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
         [${dbxschemaname}].bbrequest.accountId
      FROM [${dbxschemaname}].bbrequest
      WHERE 
         [${dbxschemaname}].bbrequest.requestId = CAST(@_requestId AS float(53)) AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @_companyId) <> 0 AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_bbtemplate_proc]
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_bbtemplate_proc]  
   @_customerId nvarchar(50),
   @_templateId nvarchar(50),
   @_featureactionlist nvarchar(max),
   @_queryType nvarchar(50),
   @_filterByParam nvarchar(max),
   @_filterByValue nvarchar(max),
   @_searchString nvarchar(50),
   @_sortByParam nvarchar(50),
   @_sortOrder nvarchar(50),
   @_pageSize nvarchar(50),
   @_pageOffset nvarchar(50)
AS 
   BEGIN

     MAINLABEL: 
      
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

     DECLARE @group_concat_max_len bigint
     DECLARE @combinedIds nvarchar(max)
     DECLARE @numOfParams int
     DECLARE @searchQuery nvarchar(max)
     DECLARE @idx int
     DECLARE @filterParam nvarchar(max)
     DECLARE @filterValue nvarchar(max)
     DECLARE @companyId nvarchar(max)
     DECLARE @customerMatrixIds nvarchar(max)
     DECLARE @alreadyApprovedIds nvarchar(max)
     DECLARE @approvalRequestIds nvarchar(max)
     DECLARE @queryTypecondition nvarchar(max)
     DECLARE @paginationQuery nvarchar(max)
     DECLARE @features nvarchar(max)
     DECLARE @createActions nvarchar(max)
     DECLARE @companyRequestIds nvarchar(max)
     DECLARE @customerAcounts nvarchar(max)
     DECLARE @select_statement nvarchar(max)
     

     SET @group_concat_max_len = 100000000
     SET @combinedIds = (select String_agg(id, ',') from [${dbxschemaname}].customer where combinedUserId = @_customerId)

     IF @combinedIds is NULL      
        SET @combinedIds = @_customerId;
     ELSE 
        SET @combinedIds =(@_customerId + ',' +@combinedIds)
  
    SET @_filterByParam = CASE WHEN @_filterByParam IS NULL OR @_filterByParam='' THEN  '' ELSE @_filterByParam END
    SET @_filterByValue = CASE WHEN @_filterByValue IS NULL OR @_filterByValue='' THEN '' ELSE @_filterByValue END
    SET @_templateId = CASE WHEN @_templateId = '' OR @_templateId IS NULL THEN '%' ELSE @_templateId END
    SET @numOfParams = 0

    IF datalength(@_filterByParam) > 0
      SET @numOfParams = datalength(@_filterByParam) - datalength(REPLACE(@_filterByParam, ',', '')) + 1

    SET @searchQuery = ''
    SET @idx = 1
    WHILE(@idx <= @numOfParams) 
    BEGIN       
    SET @filterParam = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByParam, ',', @idx),',', -1 );
      SET @filterValue = [${dbxschemaname}].SUBSTRING_INDEX( [${dbxschemaname}].SUBSTRING_INDEX( @_filterByValue, ',',   @idx),',', -1 );
      SET @searchQuery = @searchQuery+ ' AND ('+@filterParam+' LIKE '''+@filterValue+''' )'
      SET @idx = @idx + 1   
    END

    SET @companyId = ( SELECT String_agg(CAST(concat(contractcustomers.contractId,'_',contractcustomers.coreCustomerId) as nvarchar(max)),',') FROM [${dbxschemaname}].contractcustomers WHERE contractcustomers.customerId = @_customerId)
	

    IF @companyId IS NULL
      BEGIN
        SET @companyId = ''
      END

    IF @_featureactionlist IS NULL
      BEGIN
         GOTO MAINLABEL$leave
      END

    SET @_sortByParam = CASE WHEN @_sortByParam = '' OR @_sortByParam is NULL THEN 'createdts' ELSE @_sortByParam END
    SET @_sortOrder = CASE WHEN @_sortOrder = '' OR @_sortOrder is NULL THEN 'DESC' ELSE @_sortOrder END  
    SET @customerMatrixIds = ( SELECT String_agg(CAST(approvalMatrixId  as nvarchar(max)),',') FROM [${dbxschemaname}].customerapprovalmatrix WHERE [${dbxschemaname}].FIND_IN_SET(customerId, @combinedIds) <> 0)

    IF @customerMatrixIds IS NULL
      BEGIN
        SET @customerMatrixIds = ''
      END
  
    SET @alreadyApprovedIds = (SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbactedrequest WHERE [${dbxschemaname}].FIND_IN_SET(createdby, @combinedIds) <> 0 AND bbactedrequest.action = 'Approved')

    IF @alreadyApprovedIds IS NULL
      BEGIN
         SET @alreadyApprovedIds = ''
      END

    SET @approvalRequestIds = (  
         SELECT String_agg(CAST(requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].requestapprovalmatrix
         INNER JOIN [${dbxschemaname}].approvalmatrix ON [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = approvalmatrix.id
         INNER JOIN [${dbxschemaname}].approvalrule ON [${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id
         WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId AS nvarchar(max)),  @customerMatrixIds) <>0
         AND [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].requestapprovalmatrix.requestId AS nvarchar(max)), @alreadyApprovedIds) = 0
         AND (([${dbxschemaname}].approvalrule.numberOfApprovals = -1 AND [${dbxschemaname}].requestapprovalmatrix.receivedApprovals < (SELECT COUNT(DISTINCT(customerId)) FROM [${dbxschemaname}].customerapprovalmatrix 
         WHERE [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId)))
         )

    IF @approvalRequestIds IS NULL
      BEGIN
        SET @approvalRequestIds = ''
      END

    SET @queryTypecondition = 
      CASE WHEN (@_queryType = 'myRequests') THEN 
            (' AND  [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby,') + (@combinedIds) + (') <> 0 AND ([${dbxschemaname}].bbrequest.status = ''Pending'' OR [${dbxschemaname}].bbrequest.status = ''Approved'' OR [${dbxschemaname}].bbrequest.status = ''Rejected'') ')
        ELSE 
            CASE WHEN (@_queryType = 'pendingForMyApprovals') THEN (' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.requestId,  ''') + (@approvalRequestIds) + (''')<>0  AND [${dbxschemaname}].bbtemplate.status = ''Pending'' ')
              ELSE 
               CASE WHEN @_templateId = '%' THEN ' AND NOT [${dbxschemaname}].bbtemplate.status = ''Withdrawn''' 
                ELSE '' 
               END
            END
        END

      SET @searchQuery = CASE WHEN (@_searchString is NULL OR @_searchString = '') THEN @searchQuery 
                  ELSE 
                   @searchQuery+ ' AND ([${dbxschemaname}].bbtemplate.templateName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].customeraccounts.AccountName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplaterequesttype.templateRequestTypeName LIKE ''%'+@_searchString+'%'' OR [${dbxschemaname}].bbtemplate.fromAccount LIKE ''%'+@_searchString+'%'')' 
                  END

      SET @paginationQuery = CASE WHEN (@_pageOffset is NULL OR @_pageOffset = '' OR @_pageSize is NULL OR @_pageSize = '') THEN '' 
                  ELSE
                    ' OFFSET '+ @_pageOffset +'  ROWS FETCH NEXT ' +@_pageSize+' ROWS ONLY' 
                  END

      SET @features = ( SELECT String_agg(CAST(Feature_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @_featureactionlist) <> 0 )
      
      IF @features IS NULL
        BEGIN
          SET @features = ''
        END
 
      SET @createActions = ( SELECT String_agg(CAST(featureaction.id as nvarchar(max)),',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id, @features) <> 0 AND [${dbxschemaname}].featureaction.id LIKE '%_CREATE_TEMPLATE')

      IF @createActions IS NULL
        BEGIN
          SET @createActions = ''
        END

      SET @companyRequestIds = ( SELECT String_agg(CAST(bbrequest.requestId as nvarchar(max)) ,',') FROM [${dbxschemaname}].bbrequest WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.companyId, @companyId) <> 0 AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbrequest.featureActionId, @createActions) <> 0)
      IF @companyRequestIds IS NULL
        BEGIN
          SET @companyRequestIds = ''
        END
      SET @customerAcounts = ( SELECT String_agg(CAST(customeraccounts.Account_id as nvarchar(max)) ,',') FROM [${dbxschemaname}].customeraccounts WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Customer_id , @_customerId) <> 0)

      IF @customerAcounts IS NULL
        BEGIN
          SET @customerAcounts = ''
        END

		SELECT  customerCnt, requestId into #approvalCount1 from
	 (SELECT COUNT(DISTINCT(customerId)) as customerCnt ,requestapprovalmatrix.requestId as requestId FROM [${dbxschemaname}].customerapprovalmatrix INNER JOIN [${dbxschemaname}].requestapprovalmatrix ON (customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId ) WHERE [${dbxschemaname}].FIND_IN_SET(cast(requestapprovalmatrix.requestId as nvarchar(max)), @companyRequestIds) <> 0 group by requestapprovalmatrix.requestId) as approvalCount1

	 SELECT  requestId, totalCnt into #approvalCount2 
	from (select requestId, sum(t) as totalCnt from (
	select bbrequest.requestId as requestId,
         CASE WHEN
			[${dbxschemaname}].approvalrule.numberOfApprovals = -1
			THEN (SELECT count(*)  FROM [${dbxschemaname}].customerapprovalmatrix where customerapprovalmatrix.approvalMatrixId = requestapprovalmatrix.approvalMatrixId)
          WHEN [${dbxschemaname}].approvalrule.numberOfApprovals IS NULL OR [${dbxschemaname}].approvalrule.numberOfApprovals='' THEN 0
              ELSE [${dbxschemaname}].approvalrule.numberOfApprovals
          END as t
     from [${dbxschemaname}].customerapprovalmatrix
	 LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId = [${dbxschemaname}].requestapprovalmatrix.approvalMatrixId
	 LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
	 LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
	 LEFT JOIN [${dbxschemaname}].approvalrule ON ([${dbxschemaname}].approvalmatrix.approvalruleId = [${dbxschemaname}].approvalrule.id)
     WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),@companyRequestIds)<>0 ) approvals group by requestId) as approvalCount2


      SET @select_statement = 
        '
		SELECT * FROM
          (SELECT 
            bbtemplate.templateId AS templateId,
            bbtemplate.templateName AS templateName,
            bbtemplate.templateDescription AS templateDescription,
            bbtemplate.featureActionId AS featureActionId,
            bbtemplate.fromAccount AS fromAccount,
            bbtemplate.effectiveDate AS effectiveDate,
            bbtemplate.requestId AS requestId,
            bbtemplate.createdby AS createdby,
            bbtemplate.updatedBy AS updatedBy,
            bbtemplate.createdts AS createdts,
            bbtemplate.maxAmount AS maxAmount,
            bbtemplate.status AS status,
            bbtemplate.transactionType_id AS transactionType_id,
            bbtemplate.templateType_id AS templateType_id,
            bbtemplate.companyId AS companyId,
            bbtemplate.templateRequestType_id AS templateRequestType_id,
            bbtemplate.softDelete AS softDelete,
            bbtemplate.totalAmount AS totalAmount,
            ( 
            CASE 
               WHEN customeraccounts.AccountName is NULL THEN ''AccountName'' 
               ELSE customeraccounts.AccountName 
            END 
            ) AS accountName,
            customer.UserName AS userName,
            bbtransactiontype.transactionTypeName AS transactionTypeName,
            bbtemplatetype.templateTypeName AS templateTypeName,
            bbtemplaterequesttype.templateRequestTypeName AS templateRequestTypeName,
            bbrequest.createdby AS requestCreatedby,
            (
            CASE
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbtemplate.createdby as nvarchar(max)),CAST('''+ @combinedIds +''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amICreator,
            (
            CASE 
              WHEN [${dbxschemaname}].FIND_IN_SET(CAST(bbrequest.requestId AS nvarchar(max)),CAST('''+@approvalRequestIds+''' as nvarchar(max))) > 0 THEN ''true''
              ELSE ''false''
            END
            ) as amIApprover
            FROM
            ((((((([${dbxschemaname}].bbtemplate
            LEFT JOIN [${dbxschemaname}].customer ON ([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customer.id))
            LEFT JOIN [${dbxschemaname}].customeraccounts ON (([${dbxschemaname}].bbtemplate.createdby = [${dbxschemaname}].customeraccounts.Customer_id) AND ([${dbxschemaname}].bbtemplate.fromAccount = [${dbxschemaname}].customeraccounts.Account_id)))
            LEFT JOIN [${dbxschemaname}].bbtransactiontype ON ([${dbxschemaname}].bbtemplate.transactionType_id = [${dbxschemaname}].bbtransactiontype.transactionType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplatetype ON ([${dbxschemaname}].bbtemplate.templateType_id = [${dbxschemaname}].bbtemplatetype.templateType_id))
            LEFT JOIN [${dbxschemaname}].bbtemplaterequesttype ON ([${dbxschemaname}].bbtemplate.templateRequestType_id = [${dbxschemaname}].bbtemplaterequesttype.templateRequestType_id))
            LEFT JOIN [${dbxschemaname}].organisation ON ([${dbxschemaname}].bbtemplate.companyId = [${dbxschemaname}].organisation.id))
            LEFT JOIN [${dbxschemaname}].bbrequest ON ([${dbxschemaname}].bbtemplate.requestId = [${dbxschemaname}].bbrequest.requestId))
            WHERE [${dbxschemaname}].bbtemplate.softDelete = ''0''
            AND ([${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.companyId, @companyId) OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.createdby, '''+@combinedIds+''')>0)
            AND [${dbxschemaname}].bbtemplate.templateId LIKE '''+@_templateId +'''
            AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.fromAccount,'''+ @customerAcounts+ ''')<>0
                AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].bbtemplate.featureActionId,'''+ @createActions+''')<>0'+
                @queryTypecondition+ ' '+
                @searchQuery+ ' ) AS t1
            LEFT JOIN
            ( 
            SELECT 
               [${dbxschemaname}].bbrequest.requestId,
               (select count(DISTINCT([${dbxschemaname}].bbactedrequest.createdby)) from [${dbxschemaname}].bbactedrequest where [${dbxschemaname}].bbactedrequest.action = ''Approved'' AND  [${dbxschemaname}].bbactedrequest.requestId = [${dbxschemaname}].bbrequest.requestId) 
                     as receivedApprovals,
                    ([${dbxschemaname}].LEASTINT((select customerCnt from #approvalCount1 where requestid=bbrequest.requestId ),(select totalCnt from #approvalCount2 where requestid=bbrequest.requestId ))) as requiredApprovals
            FROM
            [${dbxschemaname}].bbrequest
            LEFT JOIN [${dbxschemaname}].requestapprovalmatrix ON ([${dbxschemaname}].bbrequest.requestId = [${dbxschemaname}].requestapprovalmatrix.requestId)
            LEFT JOIN [${dbxschemaname}].approvalmatrix ON ([${dbxschemaname}].requestapprovalmatrix.approvalMatrixId = [${dbxschemaname}].approvalmatrix.id)
            LEFT JOIN [${dbxschemaname}].approvalrule ON (approvalmatrix.approvalruleId = approvalrule.id)
                WHERE [${dbxschemaname}].FIND_IN_SET(CAST([${dbxschemaname}].bbrequest.requestId AS nvarchar(max)),'''+@companyRequestIds+''')<>0
            GROUP BY [${dbxschemaname}].bbrequest.requestId,[${dbxschemaname}].requestapprovalmatrix.approvalMatrixId,[${dbxschemaname}].approvalrule.numberOfApprovals
            ) AS t2
            ON 
            t1.requestId = t2.requestId
            ORDER BY '+ @_sortByParam+' '+ @_sortOrder + ' '+
            @paginationQuery
      EXEC(@select_statement)
	  DROP TABLE #approvalCount1;
	  DROP TABLE #approvalCount2;
   END
   MAINLABEL$leave:

GO   

ALTER TABLE [${dbxschemaname}].[customerrequest] DROP CONSTRAINT [customerrequest$FK_CustomerRequest_AssignedTo];

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[systemroles_permission_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[systemroles_permission_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[systemroles_permission_proc]  
   @_roleIds varchar(100)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      SELECT DISTINCT
         p.id AS id, 
         p.Name AS name, 
         p.Status_id AS status, 
         p.PermissionValue AS PermissionValue, 
         p.softdeleteflag AS softdeleteflag
      FROM ([${dbxschemaname}].rolepermission  AS rp 
         INNER JOIN [${dbxschemaname}].permission  AS p 
         ON (rp.Permission_id = p.id))
      WHERE [${dbxschemaname}].FIND_IN_SET(rp.Role_id, @_roleIds) <> 0 order by [id]
   END
GO
	


ALTER TABLE [${dbxschemaname}].[application] ADD [isKeyCloakEnabled] TINYINT NOT NULL DEFAULT '0';


GO
/****** Object:  StoredProcedure [${dbxschemaname}].[rolescompositeactions_proc]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolescompositeactions_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolescompositeactions_proc]  
 @_roleIds varchar(500) ,
 @_isEnabled TINYINT
AS 
   BEGIN

    SET  XACT_ABORT  ON

    SET  NOCOUNT  ON

	SELECT 
		CompositeAction_id AS CompositeAction_id,
		case when isEnabled = '1' then 'true' else 'false' end AS isEnabled		
	FROM
		rolecompositeaction
	WHERE
		[${dbxschemaname}].FIND_IN_SET(rolecompositeaction.role_id, @_roleIds) > 0
			AND [${dbxschemaname}].rolecompositeaction.isEnabled = case when @_isEnabled is null then 1 else null end 
	UNION SELECT 
		CompositeAction_id AS CompositeAction_id, CASE WHEN isEnabled = '1' THEN 'true' ELSE 'false' END AS isEnabled
	FROM
		rolecompositeaction
	WHERE
		[${dbxschemaname}].FIND_IN_SET(rolecompositeaction.role_id, @_roleIds) > 0
			AND [${dbxschemaname}].rolecompositeaction.CompositeAction_id NOT IN (SELECT 
				CompositeAction_id AS CompositeAction_id
			FROM
				rolecompositeaction
			WHERE
				[${dbxschemaname}].FIND_IN_SET(rolecompositeaction.role_id, @_roleIds) > 0
					AND [${dbxschemaname}].rolecompositeaction.isEnabled  = case when @_isEnabled is null then 1 else null end)
			AND [${dbxschemaname}].rolecompositeaction.isEnabled = case when @_isEnabled is null then 0 else null end ;
  END
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[csrassistgrant]', 'userRoleId';
GO
ALTER TABLE [${dbxschemaname}].[csrassistgrant] ALTER COLUMN [userRoleId] VARCHAR(500);
GO
ALTER TABLE [${dbxschemaname}].[usercompositeaction] DROP CONSTRAINT [usercompositeaction$FK_UserCompositeAction_CompositeAction];
GO
ALTER TABLE [${dbxschemaname}].[rolecompositeaction] DROP CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_CompositeAction]
GO
ALTER TABLE [${dbxschemaname}].[compositeaction] DROP CONSTRAINT [PK_compositeaction_id];
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD CONSTRAINT [PK_compositeaction_id] PRIMARY KEY ([id],[Permission_id]);
ALTER TABLE [${dbxschemaname}].[compositeaction] ADD CONSTRAINT [FK_compositeaction_Permission_id] FOREIGN KEY ([Permission_id]) REFERENCES [${dbxschemaname}].permission([id]);

GO
--ALTER TABLE [${dbxschemaname}].[usercompositeaction] ADD CONSTRAINT [usercompositeaction$FK_UserCompositeAction_CompositeAction] FOREIGN KEY([CompositeAction_id])
--REFERENCES [${dbxschemaname}].[compositeaction] ([id]);
--GO
--ALTER TABLE [${dbxschemaname}].[rolecompositeaction] ADD CONSTRAINT [rolecompositeaction$FK_RoleCompositeAction_CompositeAction] FOREIGN KEY([CompositeAction_id])
--REFERENCES [${dbxschemaname}].[compositeaction] ([id]);
--GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[rolePermissionDelete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[rolePermissionDelete_proc] 
 @_roleId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000)
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
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  rolepermission rp WHERE rp.Role_id= @_roleId
	AND rp.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(rp.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	DELETE FROM [${dbxschemaname}].[rolecompositeaction] WHERE Role_id=@_roleId AND CompositeAction_id=@caid;
	END 
	MANAGECAIDS$LEAVE:
	CLOSE caids;
	DELETE FROM [${dbxschemaname}].[rolepermission] WHERE Role_id=@_roleId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
	END
GO

GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[userPermissionDelete_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[userPermissionDelete_proc] 
 @_userId VARCHAR(50), 
 @_PermissionIds VARCHAR(5000)
AS 
   BEGIN
    SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
	DECLARE @caid VARCHAR(50);
	DECLARE @isEnabled VARCHAR(10);
	DECLARE @finished INTEGER = 0 ;
	DECLARE @caid_count INTEGER = 0 ;
	DECLARE caids CURSOR FOR (SELECT c.id FROM [${dbxschemaname}].[compositeaction] c WHERE [${dbxschemaname}].FIND_IN_SET([Permission_id], @_PermissionIds) > 0);

	OPEN caids; 
	MANAGECAIDS: 
	WHILE 1=1
	BEGIN
	FETCH caids INTO @caid;
	IF @@FETCH_STATUS <> 0 BEGIN 
		 GOTO MANAGECAIDS$LEAVE
	END
	SELECT @caid_count = COUNT(*) FROM [${dbxschemaname}].[compositeaction] c,  userpermission up WHERE up.User_id = @_userId
	AND up.Permission_id=c.Permission_id
	AND [${dbxschemaname}].FIND_IN_SET(up.Permission_id,@_PermissionIds) = 0
	AND c.id=@caid;

	IF @caid_count=0 BEGIN 
	DELETE FROM [${dbxschemaname}].[usercompositeaction] WHERE User_id=@_userId AND CompositeAction_id=@caid;
	END 
	MANAGECAIDS$LEAVE:
	CLOSE caids;
	DELETE FROM [${dbxschemaname}].[userpermission] WHERE User_id=@_userId AND [${dbxschemaname}].FIND_IN_SET(Permission_id,@_PermissionIds) > 0;
	END
	END
GO


ALTER TABLE [${dbxschemaname}].[requestmessage] ADD [frominternaluser] TINYINT NOT NULL DEFAULT '0';

GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_received]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_messages_received];
GO

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_received]  
   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE @queryStatement nvarchar(max)
	  
      SET @queryStatement = 'SELECT count(id) messages_received_count FROM [${dbxschemaname}].requestmessage where frominternaluser <> "1"'
   
      IF (@from_date <> '' AND @from_date <> '')
      SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '),120),'''') + (' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '),120),'''')
	  

      IF (@category_id <> '')
      SET @queryStatement = (@queryStatement) + ' and CustomerRequest_id in (select id  from [${dbxschemaname}].customerrequest where RequestCategory_id = ' + ((QUOTENAME((@category_id), '''')))
         

      IF @csr_name <> ''
      
         SET @queryStatement = (@queryStatement) + (' and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	    EXEC(@queryStatement)
   END
GO
/****** Object:  StoredProcedure [${dbxschemaname}].[reports_messages_sent]    Script Date: 7/28/2020 4:04:09 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reports_messages_sent];
GO

CREATE PROCEDURE [${dbxschemaname}].[reports_messages_sent]  

   @from_date varchar(19),
   @to_date varchar(19),
   @category_id varchar(50),
   @csr_name varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
	  declare @queryStatement nvarchar(max)

      SET @queryStatement = 'SELECT count(id) messages_sent_count FROM [${dbxschemaname}].requestmessage where frominternaluser = "1") '
 

      IF @from_date <> '' AND @from_date <> ''
      
         SET @queryStatement = (@queryStatement) + (' and createdts >= ') + QUOTENAME(convert(datetime,STUFF(@from_date,11,0,' '), 120),'''') + (N' and createdts <= ') + QUOTENAME(convert(datetime,STUFF(@to_date,11,0,' '), 120),'''')
      
      IF @category_id <> ''
      
         SET @queryStatement = (@queryStatement) + (' and CustomerRequest_id in (select id from [${dbxschemaname}].customerrequest where RequestCategory_id = ') + ((QUOTENAME((@category_id), ''''))) + ' )'
         
      IF @csr_name <> ''
         
         SET @queryStatement = (@queryStatement) + ('  and RepliedBy_id = ') + ((QUOTENAME((@csr_name), '''')))
	     EXEC(@queryStatement)
   END

GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[internal_user_access_on_given_customer_proc];

GO

CREATE PROCEDURE [${dbxschemaname}].[internal_user_access_on_given_customer_proc]
@_roleIds varchar(500),
@_customerId  nvarchar(50) ,
@_customerUsername  nvarchar(50) 

AS
BEGIN
      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  DECLARE @isCustomerAccessible varchar(6);
if(@_customerId = '' and @_customerUsername != '')
	SET @_customerId = (SELECT id from [${dbxschemaname}].customer where UserName = @_customerUsername)

if @_customerId in (SELECT customergroup.Customer_id FROM [${dbxschemaname}].userrole LEFT JOIN [${dbxschemaname}].userrolecustomerrole ON userrolecustomerrole.UserRole_id = userrole.Role_id
LEFT JOIN [${dbxschemaname}].customergroup ON customergroup.Group_id = userrolecustomerrole.CustomerRole_id
WHERE  [${dbxschemaname}].FIND_IN_SET(userrolecustomerrole.UserRole_id, @_roleIds) <> 0)
	select 'true' as isCustomerAccessible
ELSE  
	select 'false' as isCustomerAccessible
END
GO


ALTER TABLE [${dbxschemaname}].[customernote] DROP CONSTRAINT [customernote$FK_CustomerNote_Createdby];
ALTER TABLE [${dbxschemaname}].[archivedcustomerrequest] DROP CONSTRAINT [archivedcustomerrequest$FK_ArchivedCustomerRequest_AssignedTo];

GO

ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD [paidBy] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[billpaytransfers] ADD [swiftCode] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD [paidBy] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[ownaccounttransfers] ADD [swiftCode] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD [paidBy] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[intrabanktransfers] ADD [swiftCode] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD [paidBy] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[p2ptransfers] ADD [swiftCode] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [paidBy] VARCHAR(50) NULL DEFAULT NULL;
ALTER TABLE [${dbxschemaname}].[wiretransfers] ADD [swiftCode] VARCHAR(50) NULL DEFAULT NULL;

GO
/****** Object:  View [${dbxschemaname}].[customernotesfetch_view]    Script Date: ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
DROP VIEW IF EXISTS [${dbxschemaname}].[customernotesfetch_view];

GO

CREATE VIEW [${dbxschemaname}].[customernotesfetch_view] (
   [id], 
   [Note], 
   [Customer_id], 
   [Customer_FirstName], 
   [Customer_MiddleName], 
   [Customer_LastName], 
   [Customer_Username], 
   [Customer_Status_id], 
   [InternalUser_id], 
   [createdts], 
   [synctimestamp], 
   [softdeleteflag])
AS 
   SELECT 
      customernote.id AS id, 
      customernote.Note AS Note, 
      customernote.Customer_id AS Customer_id, 
      customer.FirstName AS Customer_FirstName, 
      customer.MiddleName AS Customer_MiddleName, 
      customer.LastName AS Customer_LastName, 
      customer.UserName AS Customer_Username, 
      customer.Status_id AS Customer_Status_id, 
      customernote.createdby AS InternalUser_id, 
      customernote.createdts AS createdts, 
      customernote.synctimestamp AS synctimestamp, 
      customernote.softdeleteflag AS softdeleteflag
   FROM ([${dbxschemaname}].customernote  
      INNER JOIN [${dbxschemaname}].customer 
      ON ((customernote.Customer_id = customer.id)))
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_action_limit_update];
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_action_limit_update](
  @_contractActionLimit NVARCHAR(max)
)
AS
BEGIN
  DECLARE @index1 INTEGER = 0;
  DECLARE @numOfRecords INTEGER = 0;
  DECLARE @contractValues NVARCHAR(max) ;
  DECLARE @contractId NVARCHAR(max) ;
  DECLARE @coreCustomerId NVARCHAR(max) ;
  DECLARE @featureId NVARCHAR(max) ;
  DECLARE @actionId NVARCHAR(max) ;
  DECLARE @limitTypeId NVARCHAR(max) ;
  DECLARE @limitValue NVARCHAR(max);
    set @numOfRecords = LEN(@_contractActionLimit) - LEN(REPLACE(@_contractActionLimit, '|', '')) + 1;
  WHILE 1=1 BEGIN
     set @index1 = @index1 + 1;
        IF @index1 = @numOfRecords + 1 BEGIN
            break ;
        end
        else begin
            set @contractValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractActionLimit, '|', @index1), '|', -1 );
            set @contractId = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',1);
            set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',2),',',-1);
            set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',3),',',-1);
            set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',4),',',-1);
            set @limitTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',5),',',-1);
            set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',-1);
            UPDATE customeraction SET value = @limitValue where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND Action_id = @actionId AND LimitType_id = @limitTypeId AND value > @limitValue;
        END
       
    END ;
END
GO
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.bulkpaymenttemplate', 'totalAmount';
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.bulkpaymenttemplatepos', 'amount';
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.bulkpaymentrequest', 'totalAmount';
EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.bulkpaymentrequestpos', 'amount';
GO
ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplate] ALTER COLUMN [totalAmount] DECIMAL(19,2);
ALTER TABLE [${dbxschemaname}].[bulkpaymenttemplatepos] ALTER COLUMN [amount] DECIMAL(19,2);
ALTER TABLE [${dbxschemaname}].[bulkpaymentrequest] ALTER COLUMN [totalAmount] DECIMAL(19,2);
ALTER TABLE [${dbxschemaname}].[bulkpaymentrequestpos] ALTER COLUMN [amount] DECIMAL(19,2);

GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_features_create_proc  
   @_features nvarchar(max),
   @_contractId nvarchar(50),
   @_customerId nvarchar(50),
   @_serviceTypeId nvarchar(50),
   @_defaultActionsEnabled nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureId nvarchar(255) = N''

      DECLARE
         @featuresList nvarchar(max) = N''

      DECLARE
         @featureActionId nvarchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @limitId nvarchar(255) = N''

      DECLARE
         @tempLimitValue nvarchar(255) = N''

	  DECLARE
         @features_List nvarchar(max) = N''
         
      DECLARE
         @id nvarchar(255) = N''
         
      DECLARE
         @limitvalue nvarchar(255) = N''
         
      DECLARE
         @limitATServiceDefinition nvarchar(255) = N''  
         
      DECLARE
         @servicedefinitionId nvarchar(255) = N'' 

      DECLARE
         @validServicedefinitionActions nvarchar(max) = N''
         
      DECLARE
         @validFIActions nvarchar(max) = N''
         
      SET @features_list = 
         (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureroletype 
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_serviceTypeId)
            WHERE (feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 ))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END


         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].[FIND_IN_SET](feature.id, @features_list) > 0
             )
             
         DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
             )
             
         DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
             )

 OPEN features
	  FETCH NEXT FROM features INTO @featureId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].contractfeatures([${dbxschemaname}].contractfeatures.id, [${dbxschemaname}].contractfeatures.contractId, [${dbxschemaname}].contractfeatures.coreCustomerId,[${dbxschemaname}].contractfeatures.featureId)
                             VALUES (@id,@_contractId,@_customerId,@featureId)
               SET @featuresList = @featureId + ',' + @featuresList
               FETCH NEXT FROM features INTO @featureId
               CONTINUE
            END
CLOSE features
DEALLOCATE features


SET @featuresList = (SELECT substring(@featuresList, 1,(case when len(@featuresList) > 0 then len(@featuresList) - 1 else 0 end)))

SELECT @featuresList AS featuresList;

SET @finished = 0;
                        
SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                        [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @featuresList) > 0 AND
                         featureaction.status = 'SID_ACTION_ACTIVE')

SET @servicedefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)

SET @validServicedefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                     servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                     [${dbxschemaname}].FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIActions) > 0)                                   


IF @_defaultActionsEnabled IS NOT NULL AND  @_defaultActionsEnabled = 'true'
BEGIN        
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId)
           
            OPEN limits
                 FETCH NEXT FROM limits INTO @limitId 
                 WHILE (@@FETCH_STATUS=0)
		         BEGIN
                     SET @limitvalue = (SELECT actionlimit.value from [${dbxschemaname}].actionlimit WHERE actionlimit.Action_id = @featureActionId
                     AND actionlimit.LimitType_id = @limitId)
			                                       
                      SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value from [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                                    servicedefinitionactionlimit.actionId = @featureActionId AND
                                                    servicedefinitionactionlimit.limitTypeId = @limitId AND
                                                    servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId)
                    			  
			            IF @limitvalue > @limitATServiceDefinition 
					       BEGIN
						      SET @tempLimitValue = @limitvalue
                           END
					   ELSE 
					       BEGIN
						      SET @tempLimitValue = @limitATServiceDefinition
                           END
              
              
              
                       IF @tempLimitValue IS NOT NULL AND  @tempLimitValue <> ''
				           BEGIN
				              SET @limitvalue = @tempLimitValue
				           END
				
			           SET @id = (SELECT left(newid(), 50))
		               INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.limitTypeId,[${dbxschemaname}].contractactionlimit.value)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@limitId,@limitvalue)
                
                        SET @entryStatus = 1;
                        
                        FETCH NEXT FROM limits INTO @limitId 
                        CONTINUE
                 END
			CLOSE limits
            DEALLOCATE limits
                
            SET @finished = 0;
            
            IF @entryStatus = 0 
				 BEGIN
				     SET @id = (SELECT left(newid(), 50))
				     INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId)
				  END
            
       FETCH NEXT FROM actions INTO @featureActionId 
       CONTINUE
       END
   END
 CLOSE actions
 DEALLOCATE actions

END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].contract_corecustomers_delete_proc  
   @_contractCustomersList nvarchar(max),
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
     
      DELETE 
      FROM [${dbxschemaname}].customergroup
      WHERE customergroup.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customergroup.coreCustomerId, @_contractCustomersList) = '1'
     
     
      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE customeraction.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.coreCustomerId, @_contractCustomersList) = '1'
      

      DELETE 
      FROM [${dbxschemaname}].customeraccounts
      WHERE customeraccounts.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.coreCustomerId, @_contractCustomersList) = '1'
      
      
      DELETE 
      FROM [${dbxschemaname}].contractcustomers
      WHERE contractcustomers.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractcustomers.coreCustomerId, @_contractCustomersList) = '1'
      
      DELETE 
      FROM [${dbxschemaname}].contractcorecustomers
      WHERE contractcorecustomers.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractcorecustomers.coreCustomerId, @_contractCustomersList) = '1'
      
      DELETE 
      FROM [${dbxschemaname}].contractaccounts
      WHERE contractaccounts.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.coreCustomerId, @_contractCustomersList) = '1'
     
      DELETE 
      FROM [${dbxschemaname}].contractfeatures
      WHERE contractfeatures.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractfeatures.coreCustomerId, @_contractCustomersList) = '1'
      
      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE contractactionlimit.contractId = @_contractId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId, @_contractCustomersList) = '1'
     

   END
GO



SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_communication_address_delete_proc  
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].contractcommunication
      WHERE contractcommunication.contractId = @_contractId

      DELETE 
      FROM [${dbxschemaname}].contractaddress
      WHERE contractaddress.contractId = @_contractId

   END
GO



SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].contract_corecustomer_accounts_delete_proc  
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_accountsCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Account_id, @_accountsCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId
      

      DELETE 
      FROM [${dbxschemaname}].customeraccounts
      WHERE 
         [${dbxschemaname}].customeraccounts.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraccounts.Account_id, @_accountsCSV) = '1' AND 
         [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId
     

      DELETE 
      FROM [${dbxschemaname}].contractaccounts
      WHERE 
         [${dbxschemaname}].contractaccounts.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.accountId, @_accountsCSV) = '1' AND 
         [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId
     
   END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_corecustomer_features_delete_proc  
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_featuresCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.featureId, @_featuresCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId
     
      DELETE 
      FROM [${dbxschemaname}].contractfeatures
      WHERE 
         [${dbxschemaname}].contractfeatures.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractfeatures.featureId, @_featuresCSV) = '1' AND 
         [${dbxschemaname}].contractfeatures.coreCustomerId = @_coreCustomerId

      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE 
         [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.featureId, @_featuresCSV) = '1' AND 
         [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId
     
   END
GO



SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_corecustomer_actions_delete_proc  
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_actionsCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DELETE 
      FROM [${dbxschemaname}].customeraction
      WHERE 
         [${dbxschemaname}].customeraction.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId

      DELETE 
      FROM [${dbxschemaname}].contractactionlimit
      WHERE 
         [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND 
         [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId, @_actionsCSV) = '1' AND 
         [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId
      
   END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].user_limitgroup_limits_create_proc  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
      
      DECLARE
         @singlePaymentsActions nvarchar(max) = N''
         
      DECLARE
         @bulkPaymentsActions nvarchar(max) = N''
         
      DECLARE
         @max_per_transaction_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_per_transaction_bulk_payment nvarchar(max) = N''
            
      DECLARE
         @max_daily_limit_single_payment nvarchar(max) = N''
           
      DECLARE
         @max_daily_limit_bulk_payment nvarchar(max) = N''
     
      DECLARE
         @max_weekly_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_weekly_limit_bulk_payment nvarchar(max) = N''   
         
     
      SET @singlePaymentsActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction
                             where featureaction.limitgroupId = 'SINGLE_PAYMENT')  
                             
      SET @bulkPaymentsActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction
                             where featureaction.limitgroupId = 'BULK_PAYMENT')  

      SET @max_per_transaction_single_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
      
      
      SET @max_per_transaction_bulk_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
       
       SET @max_daily_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions ) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
               
               
        SET @max_daily_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
      
        SET @max_weekly_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
          
         SET @max_weekly_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
          
          
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment)
                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment)
                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment)
                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment)
                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment)
                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment)


   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].get_actions_with_approvefeatureaction_proc  
   @_featureActions nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
      
      DECLARE
         @actionsList nvarchar(max) = N''
      
     SET @actionsList =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction
                             where [${dbxschemaname}].featureaction.Type_id = 'MONETARY' AND
                             [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id, @_featureActions) = '1' AND 
                             [${dbxschemaname}].featureaction.approveFeatureAction IS NOT NULL AND 
                             [${dbxschemaname}].featureaction.approveFeatureAction <> '')    

      SELECT @actionsList AS actions
    
   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].get_validcorecustomerslist_proc  
   @_coreCustomersCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE
         @list nvarchar(max) = N''

	  DECLARE
         @validcorecustomersCSV nvarchar(max) = N''

	 DECLARE
         @next nvarchar(max) = N''

	 DECLARE
         @nextlen int

	DECLARE
         @value nvarchar(max) = N''

	DECLARE
         @customers nvarchar(max) = N''

	DECLARE
         @stringLength nvarchar(max) = N''
      
      SET @list = 
         (
            SELECT @_coreCustomersCSV
         )
      SET @validcorecustomersCSV = N''

      WHILE (1 = 1)
      
         BEGIN

            
            IF datalength(LTRIM(RTRIM(@list))) = 0 OR @list IS NULL
               BREAK
            
            SET @next = [${dbxschemaname}].SUBSTRING_INDEX(@list, N',', 1)
            

            SET @nextlen = datalength(@next)
            
            SET @value = LTRIM(RTRIM(@next))
               
            SET @customers =  (SELECT String_agg(CAST(contractcorecustomers.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @value)    
              
	
	      IF @customers IS NULL OR @customers <>''
	         BEGIN
	            IF @validcorecustomersCSV = 0
                  SET @validcorecustomersCSV = @value
                ELSE 
                  SET @validcorecustomersCSV = (@validcorecustomersCSV) + (N',') + (@value)
             END     
            
	    SET @stringLength = datalength(@list)
		SET @list = SUBSTRING(@list,@nextlen + 2,@stringLength-@nextlen-1) 
            
         SELECT @validcorecustomersCSV AS validCustomers
      
     END
   END
GO

-- user_securityattributes_get_proc

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[user_securityattributes_get_proc]
   @_userId nvarchar(50)
AS 
BEGIN

SET  XACT_ABORT  ON
SET  NOCOUNT  ON

DECLARE @userAssociatedCoreCustomers nvarchar(max) = N''
DECLARE @userAssociatedContracts nvarchar(max) = N''
DECLARE @userAssociatedServiceDefinitions nvarchar(max) = N''
DECLARE @userAssociatedGroups nvarchar(max) = N''
DECLARE @actionsAtCoreCustomers nvarchar(max) = N''
DECLARE @actionsAtServiceDefinitions nvarchar(max) = N''
DECLARE @actionsAtGroups nvarchar(max) = N''
DECLARE @actionsAtuser nvarchar(max) = N''
DECLARE @activeFeaturesAtFI nvarchar(max) = N''
DECLARE @intersectedUserActions nvarchar(max) = N''
DECLARE @intersectedUserFeatures nvarchar(max) = N''

SET @userAssociatedCoreCustomers =  (SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId);

SET @userAssociatedContracts =  (SELECT String_agg(CAST(contractcustomers.contractId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId);

SET @userAssociatedServiceDefinitions =  (SELECT String_agg(CAST(contract.servicedefinitionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contract where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contract.id,@userAssociatedContracts) = '1' AND [${dbxschemaname}].contract.statusId = 'SID_CONTRACT_ACTIVE');

SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId);

SET @actionsAtCoreCustomers =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) = '1');

SET @actionsAtServiceDefinitions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions) = '1');

SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1');

SET @actionsAtuser =  (SELECT String_agg(CAST(customeraction.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraction where [${dbxschemaname}].customeraction.Customer_id = @_userId AND ([${dbxschemaname}].customeraction.isAllowed = '1' OR [${dbxschemaname}].customeraction.isAllowed = '1'));

SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature where [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE');

SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtuser) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtServiceDefinitions) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtCoreCustomers)= '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');

SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions)= '1');

SELECT @intersectedUserFeatures AS features;

END
GO


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].user_accountactions_get_proc  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      
DECLARE
         @contractId nvarchar(255) = N''
         
DECLARE
         @serviceDefinitionId nvarchar(255) = N''
         
DECLARE
         @groupId nvarchar(255) = N''
         
DECLARE
         @validFIAccountLevelActions nvarchar(max) = N''
         
DECLARE
         @validServiceDefinitinActions nvarchar(max) = N''
         
DECLARE
         @validGroupActions nvarchar(max) = N''
         
DECLARE
         @validUserActions nvarchar(max) = N''
         
DECLARE
         @actionCondition nvarchar(max) = N''
         
DECLARE
         @select_statement nvarchar(max) = N''
         
      
SET @contractId =  (SELECT contractcorecustomers.contractId FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId)  

SET @serviceDefinitionId =  (SELECT contract.servicedefinitionId FROM [${dbxschemaname}].contract
                             where [${dbxschemaname}].contract.id = @contractId)  
                 
SET @groupId =  (SELECT customergroup.Group_id FROM [${dbxschemaname}].customergroup where 
                             [${dbxschemaname}].customergroup.contractId = @contractId AND
                             [${dbxschemaname}].customergroup.coreCustomerId = @_coreCustomerId AND
                             [${dbxschemaname}].customergroup.Customer_id = @_userId)  
                 
SET @groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  

IF @groupId IS NULL 
  SET @groupId = N''
                                      
SET @validFIAccountLevelActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                               ([${dbxschemaname}].featureaction.isAccountLevel = '1' OR [${dbxschemaname}].featureaction.isAccountLevel = 'true') AND
                               [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE')
                                                               
SET @validServiceDefinitinActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIAccountLevelActions) = '1')
                                                               
                                
SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitinActions) = '1')
                               
                                                                
SET @validUserActions =  (SELECT String_agg(CAST(customeraction.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraction WHERE 
                               [${dbxschemaname}].customeraction.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].customeraction.contractId = @contractId AND
                               ([${dbxschemaname}].customeraction.isAllowed = '1'  OR [${dbxschemaname}].customeraction.isAllowed = 'true') AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id,@validGroupActions) = '1')
                               

SET @actionCondition = (N'[${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].')+(N'customeraction.Action_id,''') + (@validUserActions) + (N''')')


set @select_statement = (N'SELECT 
        customeraction.Customer_id AS Customer_id,
         customeraction.contractId AS contractId,
		 customeraction.coreCustomerId AS coreCustomerId,
        customeraction.Account_id AS Account_id,
        customeraction.featureId AS featureId,
        customeraction.Action_id AS Action_id,
        customeraction.RoleType_id AS RoleType_id,
    	customeraction.LimitType_id AS LimitType_id,
        customeraction.value AS value
    FROM [${dbxschemaname}].customeraction where customeraction.Customer_id = ') +((QUOTENAME((@_userId), ''''))) + (N' and ') + (@actionCondition) + (N' and customeraction.coreCustomerId =')  
    + (QUOTENAME((@_coreCustomerId), ''''))


exec(@select_statement)

   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_corecustomer_details_get_proc  
   @_contractId nvarchar(50),
   @_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      
      DECLARE
         @customerAccounts nvarchar(max) = N''
         
      DECLARE
         @customerFeatures nvarchar(max) = N''
         
      DECLARE
         @customerActions nvarchar(max) = N''
      
      SET @customerAccounts =  (SELECT String_agg(CAST(contractaccounts.accountId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractaccounts WHERE 
                               [${dbxschemaname}].contractaccounts.contractId = @_contractId AND
                               [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId)

      SET @customerFeatures =  (SELECT String_agg(CAST(contractfeatures.featureId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @_contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @_coreCustomerId)
   
      SET @customerActions =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId)
     
     select @customerAccounts As coreCustomerAccounts;
     select @customerFeatures As coreCustomerFeatures;
     select @customerActions As coreCustomerActions;

   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_users_details_get_proc  
   @_contractId nvarchar(50),
   @_backendType nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      
      DECLARE
         @customers nvarchar(max) = N''
         
      SET @customers =  (SELECT String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers WHERE 
                               [${dbxschemaname}].contractcustomers.contractId = @_contractId)

      SELECT 
         customer.id AS customerId, 
         customer.FirstName AS firstName, 
         customer.MiddleName AS middleName, 
         customer.LastName AS lastName, 
         customer.UserName AS userName, 
         customer.Status_id AS statusId, 
         customer.DateOfBirth AS dateOfBirth, 
         customer.Ssn AS Ssn, 
         backendidentifier.BackendId AS primaryCoreCustomerId,
         customercommunication.Value AS Email
      FROM 
         [${dbxschemaname}].customer 
            LEFT JOIN [${dbxschemaname}].customercommunication 
            ON ([${dbxschemaname}].customer.id = [${dbxschemaname}].customercommunication.Customer_id AND
                [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id, @customers) = '1')
            LEFT JOIN [${dbxschemaname}].backendidentifier 
            ON ([${dbxschemaname}].backendidentifier.Customer_id = [${dbxschemaname}].customer.id AND 
                [${dbxschemaname}].backendidentifier.BackendType = @_backendType)
      WHERE [${dbxschemaname}].customercommunication.Type_id = 'COMM_TYPE_EMAIL' 
   
   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].useractions_create_proc  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureActionId varchar(255) = N''

      DECLARE
         @actionslist varchar(max) = N''

      DECLARE
         @limitId varchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @accountId varchar(255) = N''

      DECLARE
         @actualLimitId varchar(255) = N''
         
      DECLARE
         @serviceDefinitionId varchar(255) = N''
         
      DECLARE
         @serviceType varchar(255) = N''
         
      DECLARE
         @validFIActions varchar(max) = N''
         
      DECLARE
         @validServiceDefinitionActions varchar(max) = N''
         
      DECLARE
         @validGroupActions varchar(max) = N''
         
      DECLARE
         @validActionsList varchar(max) = N''
         
      DECLARE
         @groupId varchar(255) = N''
         
       DECLARE
         @featureId varchar(255) = N''

	  DECLARE
         @limitvalue varchar(255) = N''

	  DECLARE
         @id varchar(255) = N''
         
         
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT customeraccounts.Account_id FROM [${dbxschemaname}].customeraccounts WHERE 
      customeraccounts.contractId = @_contractId AND
      customeraccounts.coreCustomerId = @_coreCustomerId AND
      customeraccounts.Customer_id = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
	  DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '1'));
      DECLARE nonaccountlevelactions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].featureaction where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].featureaction.isAccountLevel = '0'));
	  DECLARE limits CURSOR LOCAL
	  FOR (select LimitType_id from [${dbxschemaname}].actionlimit where Action_id = @featureActionId);

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (
            SELECT String_agg(CAST(customeraccounts.Account_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraccounts WHERE 
                               [${dbxschemaname}].customeraccounts.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraccounts.contractId = @_contractId AND
                               [${dbxschemaname}].customeraccounts.coreCustomerId = @_coreCustomerId
             )
    
    SET @serviceDefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)
    SET @serviceType = (SELECT servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE servicedefinition.id = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition
						WHERE groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND groupservicedefinition.isDefaultGroup = '1')
						 
    SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction)
						
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                             
    SET @_groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @_groupId)  

                                
    SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
                                
    SET @validActionsList =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
    OPEN accounts     
	FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
	OPEN actions
	FETCH NEXT FROM actions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
	      SET @entryStatus = 0
	OPEN limits
	FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN
	        SET @limitvalue = (SELECT contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE contractactionlimit.actionId = @featureActionId AND 
                               contractactionlimit.limitTypeId = @limitId AND
                               contractactionlimit.contractId = @_contractId AND
                               contractactionlimit.coreCustomerId = @_coreCustomerId)

            IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
	           SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                             [${dbxschemaname}].customeraction.RoleType_id, 
                                             [${dbxschemaname}].customeraction.Customer_id, 
                                             [${dbxschemaname}].customeraction.contractId, 
                                             [${dbxschemaname}].customeraction.coreCustomerId, 
                                             [${dbxschemaname}].customeraction.featureId, 
                                             [${dbxschemaname}].customeraction.Action_id, 
                                             [${dbxschemaname}].customeraction.Account_id, 
                                             [${dbxschemaname}].customeraction.isAllowed, 
                                             [${dbxschemaname}].customeraction.LimitType_id, 
                                             [${dbxschemaname}].customeraction.[value])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
			   SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
			 IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
			 SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
			 SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                           [${dbxschemaname}].customeraction.RoleType_id, 
                                           [${dbxschemaname}].customeraction.Customer_id, 
                                           [${dbxschemaname}].customeraction.contractId, 
                                           [${dbxschemaname}].customeraction.coreCustomerId, 
                                           [${dbxschemaname}].customeraction.featureId,
                                           [${dbxschemaname}].customeraction.Action_id, 
                                           [${dbxschemaname}].customeraction.Account_id, 
                                           [${dbxschemaname}].customeraction.isAllowed, 
                                           [${dbxschemaname}].customeraction.LimitType_id, 
                                           [${dbxschemaname}].customeraction.[value])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
			 SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
			 END
             END
		SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                      [${dbxschemaname}].customeraction.RoleType_id, 
                                      [${dbxschemaname}].customeraction.Customer_id,
                                      [${dbxschemaname}].customeraction.contractId, 
                                      [${dbxschemaname}].customeraction.coreCustomerId, 
                                      [${dbxschemaname}].customeraction.featureId, 
                                      [${dbxschemaname}].customeraction.Action_id, 
                                      [${dbxschemaname}].customeraction.Account_id, 
                                      [${dbxschemaname}].customeraction.isAllowed, 
                                      [${dbxschemaname}].customeraction.LimitType_id, 
                                      [${dbxschemaname}].customeraction.[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                                      
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                      [${dbxschemaname}].customeraction.RoleType_id, 
                                      [${dbxschemaname}].customeraction.Customer_id,
                                      [${dbxschemaname}].customeraction.contractId, 
                                      [${dbxschemaname}].customeraction.coreCustomerId, 
                                      [${dbxschemaname}].customeraction.featureId, 
                                      [${dbxschemaname}].customeraction.Action_id, 
                                      [${dbxschemaname}].customeraction.Account_id, 
                                      [${dbxschemaname}].customeraction.isAllowed, 
                                      [${dbxschemaname}].customeraction.LimitType_id, 
                                      [${dbxschemaname}].customeraction.[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue) 
                                                                 
		  SET @entryStatus = 1
		  FETCH NEXT FROM limits into @limitId
		  CONTINUE
		  END
	      CLOSE limits
		  DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
		  SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.Account_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1);
		   END
		   SET @actionslist = @featureActionId + N',' + @actionslist
		   FETCH NEXT FROM actions into @featureActionId
		   CONTINUE
           END
	       CLOSE actions
		   DEALLOCATE actions
		   FETCH NEXT FROM accounts into @accountId
		   CONTINUE
           END
		   CLOSE accounts
		   DEALLOCATE accounts
		   
OPEN nonaccountlevelactions
	FETCH NEXT FROM nonaccountlevelactions into @featureActionId
	  WHILE (@@FETCH_STATUS=0)
          BEGIN
          SET @featureId = (SELECT featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );

          SET @id =(SELECT left(newid(), 50))
		  INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].customeraction.id, 
                                        [${dbxschemaname}].customeraction.RoleType_id, 
                                        [${dbxschemaname}].customeraction.Customer_id, 
                                        [${dbxschemaname}].customeraction.contractId, 
                                        [${dbxschemaname}].customeraction.coreCustomerId, 
                                        [${dbxschemaname}].customeraction.featureId, 
                                        [${dbxschemaname}].customeraction.Action_id, 
                                        [${dbxschemaname}].customeraction.isAllowed)
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,1);
			FETCH NEXT FROM nonaccountlevelactions into @featureActionId
            CONTINUE

          END          
          
    CLOSE nonaccountlevelactions
		   DEALLOCATE nonaccountlevelactions
   END
GO




SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].useraccounts_create_proc  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @accountID varchar(255)

      DECLARE
         @finished int = 0
         
      DECLARE
         @id varchar(255)

      

      DECLARE
          accountData CURSOR LOCAL FORWARD_ONLY FOR 
             (   
                  SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE 
                               [${dbxschemaname}].contractaccounts.contractId = @_contractId AND
                               [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.accountId,@_accountsCSV) = '1'
             )
     
    OPEN accountData     
	FETCH NEXT FROM accountData into @accountID
      WHILE (1 = 1)
      
         BEGIN

            IF @@FETCH_STATUS <> 0
               SET @finished = 1

            IF @finished = 1
               BREAK
            ELSE 
               BEGIN

                 
                  SET @id = 
                     (
                        SELECT left(newid(), 50)
                     )
                 
                  INSERT [${dbxschemaname}].customeraccounts(
                     id, 
                     Customer_id, 
                     Account_id, 
                     contractId, 
                     coreCustomerId)
                     VALUES (
                        @id, 
                        @_userId, 
                        @accountID, 
                        @_contractId, 
                        @_coreCustomerId)
                 

               END
            FETCH NEXT FROM accountData into @accountID
            CONTINUE

         END
      CLOSE accountData
      DEALLOCATE accountData

   END
GO



SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].contract_actionlimits_create_proc  
   @_queryInput nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @tempLimitValue nvarchar(255) = N''
      
      DECLARE
         @index int = 0
         
      DECLARE
         @numOfRecords int = 0
         
      DECLARE
         @serviceDefinitionId nvarchar(255) = N''
         
      DECLARE
         @recordsData nvarchar(255) = N''
         
      DECLARE
         @contractId nvarchar(255) = N''
         
      DECLARE
         @customerId nvarchar(255) = N''
         
      DECLARE
         @featureId nvarchar(255) = N''
         
      DECLARE
         @actionId nvarchar(255) = N''
         
      DECLARE
         @limitId nvarchar(255) = N''
         
      DECLARE
         @limitValue nvarchar(255) = N''  
      
      DECLARE
         @recordsDataWithoutLimits nvarchar(max) = N''  
       
      DECLARE
         @limitAtFI nvarchar(255) = N''  

	 DECLARE
         @limitATServiceDefinition nvarchar(255) = N''  

	DECLARE
         @contarctFeatures nvarchar(max) = N'' 
   
    DECLARE
         @existingActionLimitRecords nvarchar(max) = N'' 
   
    DECLARE
         @serviceDefinitionActions nvarchar(max) = N'' 

	DECLARE
         @existingActionRecords nvarchar(max) = N''  

   DECLARE
         @query nvarchar(max) = N'' 
         
      SET @index = 0
      
      SET @numOfRecords = datalength(@_queryInput) - datalength(replace(@_queryInput, N'|', N'')) + 1
      

      SET @serviceDefinitionId = N''
     
      WHILE (1 = 1)
      
         BEGIN
            SET @index = @index + 1
            IF @index = @numOfRecords + 1
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = (N'\"') + (CONVERT(varchar(255), newid())) + (N'\"') + (N',') + ([${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1))
                 
                  SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 2), N'\"', -1)
                  
                  SET @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 3), N',\"', -1)
                  
                  SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 4), N',\"', -1)
                  
                  SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 5), N',\"', -1)
                  
                  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 6), N',\"', -1)
                 
                  SET @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',\"', -1), N'\"', 1)
                  
                  SET @recordsDataWithoutLimits = ([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N'\",', 5)) + (N'\"')
                  
                  IF 
                     CASE 
                        WHEN (@serviceDefinitionId) IS NULL THEN 1
                        ELSE 0
                     END <> 0 OR @serviceDefinitionId = 0
                     
                     SET @serviceDefinitionId = 
                        (
                          
                           SELECT contract.servicedefinitionId
                           FROM [${dbxschemaname}].contract
                           WHERE contract.id = @contractId
                           
                        )

              
                  IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN
                        SET @limitAtFI = 
                           (                             
                              SELECT actionlimit.value
                              FROM [${dbxschemaname}].actionlimit
                              WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId
                              
                           )
                      
                        SET @limitATServiceDefinition = 
                           (
                              SELECT servicedefinitionactionlimit.value
                              FROM [${dbxschemaname}].servicedefinitionactionlimit
                              WHERE 
                                 servicedefinitionactionlimit.actionId = @actionId AND 
                                 servicedefinitionactionlimit.limitTypeId = @limitId AND 
                                 servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
                             
                           )
                           
                           IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                     END
                  
                  IF 
                     CASE 
                        WHEN NOT 
                           CASE 
                              WHEN (@tempLimitValue) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE 
                           0
                     END <> 0 AND @tempLimitValue <> ''
                     SET @limitValue = @tempLimitValue
                                                                                                  

                  SET @contarctFeatures = 
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId   
                     )
                  
                  SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId 
                     )

                  SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId 
                     )
                  

                  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId 
                       
                     )
                     
                     
                 IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> ''
                    BEGIN
                          IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
                               BEGIN
                              
                                      SET @query = 
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                               END
                         
                          ELSE
                                IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                    
                                         SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId) VALUES (') + (@recordsDataWithoutLimits) + (N');')
                                    
                                    END
                                ELSE 
                                    IF  @limitId <> '@' AND  @limitValue <> '@' AND @tempLimitValue IS NOT NULL AND @tempLimitValue <>'' 
                                      BEGIN
                                           SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (') + (@recordsData) + (N');')
                                      
                                      END
                                  
                    
                        END    
                 
              END 
       END    
       exec(@query)
   END
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[get_validcorecustomerslist_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[get_validcorecustomerslist_proc]  
   @_coreCustomersCSV nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

	  DECLARE
         @list nvarchar(max) = N''

	  DECLARE
         @validcorecustomersCSV nvarchar(max) = N''

	 DECLARE
         @next nvarchar(max) = N''

	 DECLARE
         @nextlen int

     DECLARE
         @initiallength int

	DECLARE
         @value nvarchar(max) = N''

	DECLARE
         @customers nvarchar(max) = N''

	DECLARE
         @stringLength nvarchar(max) = N''
      
      SET @list = 
         (
            SELECT @_coreCustomersCSV
         )
      SET @validcorecustomersCSV = N''

	 
	 
      WHILE (1 = 1)
      
         BEGIN

            
            IF datalength(LTRIM(RTRIM(@list))) = 0 OR @list IS NULL
               BREAK
           
		   SET @initiallength = LEN(@list)
            SET @next = [${dbxschemaname}].SUBSTRING_INDEX(@list, N',', 1)
           
            SET @nextlen = LEN(@next)
        
            SET @value = LTRIM(RTRIM(@next))
               
            SET @customers =  (SELECT String_agg(CAST(contractcorecustomers.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @value)    
              
	      IF ISNULL(@customers,'')='' OR @customers =''
	         BEGIN
	            IF @validcorecustomersCSV = ''
                  SET @validcorecustomersCSV = @value
                ELSE 
                  SET @validcorecustomersCSV = (@validcorecustomersCSV) + (N',') + (@value)
             END     
            
	    SET @stringLength = LEN(@list)
		IF @nextlen+2 < @initiallength
		    SET @list = SUBSTRING(@list,@nextlen + 2,@stringLength-@nextlen-1) 
		ELSE 
		    SET @list = ''

     END
	  SELECT @validcorecustomersCSV AS validCustomers
   END
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_features_create_proc];
GO


CREATE PROCEDURE [${dbxschemaname}].[contract_features_create_proc]  
   @_features nvarchar(max),
   @_contractId nvarchar(50),
   @_customerId nvarchar(50),
   @_serviceTypeId nvarchar(50),
   @_defaultActionsEnabled nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureId nvarchar(255) = N''

      DECLARE
         @featuresList nvarchar(max) = N''

      DECLARE
         @featureActionId nvarchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @limitId nvarchar(255) = N''

      DECLARE
         @tempLimitValue nvarchar(255) = N''

	  DECLARE
         @features_List nvarchar(max) = N''
         
      DECLARE
         @id nvarchar(255) = N''
         
      DECLARE
         @limitvalue nvarchar(255) = N''
         
      DECLARE
         @limitATServiceDefinition nvarchar(255) = N''  
         
      DECLARE
         @servicedefinitionId nvarchar(255) = N'' 

      DECLARE
         @validServicedefinitionActions nvarchar(max) = N''
         
      DECLARE
         @validFIActions nvarchar(max) = N''
         
      SET @features_list = 
         (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature 
            INNER JOIN [${dbxschemaname}].featureroletype 
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_serviceTypeId)
            WHERE (feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 ))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END

         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) > 0
             )
             
         DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
             )
             
         DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
             )

 OPEN features
	  FETCH NEXT FROM features INTO @featureId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].contractfeatures([${dbxschemaname}].contractfeatures.id, [${dbxschemaname}].contractfeatures.contractId, [${dbxschemaname}].contractfeatures.coreCustomerId,[${dbxschemaname}].contractfeatures.featureId)
                             VALUES (@id,@_contractId,@_customerId,@featureId)
               SET @featuresList = @featureId + ',' + @featuresList
               FETCH NEXT FROM features INTO @featureId
               CONTINUE
            END
CLOSE features
DEALLOCATE features


SET @featuresList = (SELECT substring(@featuresList, 1,(case when len(@featuresList) > 0 then len(@featuresList) - 1 else 0 end)))

SELECT @featuresList AS featuresList;

SET @finished = 0;
                        
SET @validFIActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                        [${dbxschemaname}].FIND_IN_SET(featureaction.Feature_id, @featuresList) > 0 AND
                         featureaction.status = 'SID_ACTION_ACTIVE')

SET @servicedefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId)

SET @validServicedefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                     servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                     [${dbxschemaname}].FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIActions) > 0)                                   


IF ISNULL(@_defaultActionsEnabled,'')<>'' AND  @_defaultActionsEnabled = 'true'
BEGIN  
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId)
           
            OPEN limits
                 FETCH NEXT FROM limits INTO @limitId 
                 WHILE (@@FETCH_STATUS=0)
		         BEGIN
                     SET @limitvalue = (SELECT actionlimit.value from [${dbxschemaname}].actionlimit WHERE actionlimit.Action_id = @featureActionId
                     AND actionlimit.LimitType_id = @limitId)
			                                       
                      SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value from [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                                    servicedefinitionactionlimit.actionId = @featureActionId AND
                                                    servicedefinitionactionlimit.limitTypeId = @limitId AND
                                                    servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId)
                    			  
			            IF @limitvalue > @limitATServiceDefinition 
					       BEGIN
						      SET @tempLimitValue = @limitvalue
                           END
					   ELSE 
					       BEGIN
						      SET @tempLimitValue = @limitATServiceDefinition
                           END
              
              
              
                       IF @tempLimitValue IS NOT NULL AND  @tempLimitValue <> ''
				           BEGIN
				              SET @limitvalue = @tempLimitValue
				           END
				
			           SET @id = (SELECT left(newid(), 50))
		               INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.limitTypeId,[${dbxschemaname}].contractactionlimit.value)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@limitId,@limitvalue)
                
                        SET @entryStatus = 1;
                        
                        FETCH NEXT FROM limits INTO @limitId 
                        CONTINUE
                 END
			CLOSE limits
            DEALLOCATE limits
                
            SET @finished = 0;
            
            IF @entryStatus = 0 
				 BEGIN
				     SET @id = (SELECT left(newid(), 50))
				     INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId)
				  END
            
       FETCH NEXT FROM actions INTO @featureActionId 
       CONTINUE
       END
 CLOSE actions
 DEALLOCATE actions
 END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_actionlimits_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc]  
   @_queryInput nvarchar(max)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

	  DECLARE
         @id nvarchar(255) = N''

      DECLARE
         @tempLimitValue nvarchar(255) = N''
      
      DECLARE
         @index int = 0
         
      DECLARE
         @numOfRecords int = 0
         
      DECLARE
         @serviceDefinitionId nvarchar(255) = N''
         
      DECLARE
         @recordsData nvarchar(255) = N''
         
      DECLARE
         @contractId nvarchar(255) = N''
         
      DECLARE
         @customerId nvarchar(255) = N''
         
      DECLARE
         @featureId nvarchar(255) = N''
         
      DECLARE
         @actionId nvarchar(255) = N''
         
      DECLARE
         @limitId nvarchar(255) = N''
         
      DECLARE
         @limitValue nvarchar(255) = N''  
      
      DECLARE
         @recordsDataWithoutLimits nvarchar(max) = N''  
       
      DECLARE
         @limitAtFI nvarchar(255) = N''  

	 DECLARE
         @limitATServiceDefinition nvarchar(255) = N''  

	DECLARE
         @contarctFeatures nvarchar(max) = N'' 
   
    DECLARE
         @existingActionLimitRecords nvarchar(max) = N'' 
   
    DECLARE
         @serviceDefinitionActions nvarchar(max) = N'' 

	DECLARE
         @existingActionRecords nvarchar(max) = N''  

   DECLARE
         @query nvarchar(max) = N'' 
         
      SET @index = 0

	  SET @_queryInput = replace(@_queryInput, N'\', N'')
	  SET @_queryInput = replace(@_queryInput, N'"', N'')
      
      SET @numOfRecords = LEN(@_queryInput) - LEN(replace(@_queryInput, N'|', N'')) + 1
      

      SET @serviceDefinitionId = N''
     
      WHILE (1 = 1)
      
         BEGIN
            SET @index = @index + 1
            IF @index = @numOfRecords + 1
               BREAK
            ELSE 
               BEGIN

                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1)

                  SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 1), N'', -1)

                  SET @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 2), N',', -1)
                  
                  SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 3), N',', -1)
                  
                  SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 4), N',', -1)
                  
                  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 5), N',', -1)
                 
                  SET @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', -1), N'', 1)
                  
                  SET @recordsDataWithoutLimits = ([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 5)) + (N'')
  
                     SET @serviceDefinitionId = 
                        (
                          
                           SELECT contract.servicedefinitionId
                           FROM [${dbxschemaname}].contract
                           WHERE contract.id = @contractId
                           
                        )

              
                  IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN
                        SET @limitAtFI = 
                           (                             
                              SELECT actionlimit.value
                              FROM [${dbxschemaname}].actionlimit
                              WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId
                              
                           )
                      
                        SET @limitATServiceDefinition = 
                           (
                              SELECT servicedefinitionactionlimit.value
                              FROM [${dbxschemaname}].servicedefinitionactionlimit
                              WHERE 
                                 servicedefinitionactionlimit.actionId = @actionId AND 
                                 servicedefinitionactionlimit.limitTypeId = @limitId AND 
                                 servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
                             
                           )
                           
                           IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                     END
                  
                  IF 
                     CASE 
                        WHEN NOT 
                           CASE 
                              WHEN (@tempLimitValue) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE 
                           0
                     END <> 0 AND @tempLimitValue <> ''
                     SET @limitValue = @tempLimitValue
                                                                                                  

                  SET @contarctFeatures = 
                     (
                        SELECT String_agg(CAST(contractfeatures.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractfeatures WHERE 
                               [${dbxschemaname}].contractfeatures.contractId = @contractId AND
                               [${dbxschemaname}].contractfeatures.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractfeatures.featureId = @featureId   
                     )
                  
                  SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId 
                     )

                  SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId 
                     )
                  

                  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId 
                       
                     )
                     
                     
                 IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> ''
                    BEGIN
                          IF @existingActionLimitRecords IS NOT NULL AND @existingActionLimitRecords <> ''
                               BEGIN
                              
                                      SET @query = (@query)+
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                            							+ 
                           							(N'''')
                            							+ 
                          							(@limitValue)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N'WHERE contractactionlimit.id = ')
                            							+ 
                           							(N'''')
                            							+ 
                           							(@existingActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                               END
                         
                          ELSE
                                IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @id = (SELECT left(newid(), 50))
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''');')
                                    
                                    END
                                ELSE 
                                    IF  @limitId <> '@' AND  @limitValue <> '@' AND @tempLimitValue IS NOT NULL AND @tempLimitValue <>'' 
                                      BEGIN
									       SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValue) + (N''');')
                                      
                                      END
                                  
                    
                        END    
                 
              END 
       END 
       exec(@query)
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_accountactions_get_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[user_accountactions_get_proc]  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
      
DECLARE
         @contractId nvarchar(255) = N''
         
DECLARE
         @serviceDefinitionId nvarchar(255) = N''
         
DECLARE
         @groupId nvarchar(255) = N''
         
DECLARE
         @validFIAccountLevelActions nvarchar(max) = N''
         
DECLARE
         @validServiceDefinitinActions nvarchar(max) = N''
         
DECLARE
         @validGroupActions nvarchar(max) = N''
         
DECLARE
         @validUserActions nvarchar(max) = N''
         
DECLARE
         @actionCondition nvarchar(max) = N''
         
DECLARE
         @select_statement nvarchar(max) = N''
         
      
SET @contractId =  (SELECT contractcorecustomers.contractId FROM [${dbxschemaname}].contractcorecustomers
                             where [${dbxschemaname}].contractcorecustomers.coreCustomerId = @_coreCustomerId)  

SET @serviceDefinitionId =  (SELECT contract.servicedefinitionId FROM [${dbxschemaname}].contract
                             where [${dbxschemaname}].contract.id = @contractId)  
                 
SET @groupId =  (SELECT customergroup.Group_id FROM [${dbxschemaname}].customergroup where 
                             [${dbxschemaname}].customergroup.contractId = @contractId AND
                             [${dbxschemaname}].customergroup.coreCustomerId = @_coreCustomerId AND
                             [${dbxschemaname}].customergroup.Customer_id = @_userId)  
                 
SET @groupId =  (SELECT groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  

IF @groupId IS NULL 
  SET @groupId = N''
                                      
SET @validFIAccountLevelActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE 
                               ([${dbxschemaname}].featureaction.isAccountLevel = '1' OR [${dbxschemaname}].featureaction.isAccountLevel = 'true') AND
                               [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE')
                                                               
SET @validServiceDefinitinActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIAccountLevelActions) = '1')
                                                               
                                
SET @validGroupActions =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitinActions) = '1')
                               
                                                                
SET @validUserActions =  (SELECT String_agg(CAST(customeraction.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraction WHERE 
                               [${dbxschemaname}].customeraction.Customer_id = @_userId AND
                               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND
                               [${dbxschemaname}].customeraction.contractId = @contractId AND
                               ([${dbxschemaname}].customeraction.isAllowed = '1'  OR [${dbxschemaname}].customeraction.isAllowed = 'true') AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id,@validGroupActions) = '1')
                               

SET @actionCondition = (N'[${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].')+(N'customeraction.Action_id,''') + (@validUserActions) + (N''')')


set @select_statement = (N'SELECT 
        customeraction.Customer_id AS Customer_id,
         customeraction.contractId AS contractId,
		 customeraction.coreCustomerId AS coreCustomerId,
        customeraction.Account_id AS Account_id,
        customeraction.featureId AS featureId,
        customeraction.Action_id AS Action_id,
        customeraction.RoleType_id AS RoleType_id,
    	customeraction.LimitType_id AS LimitType_id,
        customeraction.value AS value
    FROM [${dbxschemaname}].customeraction where customeraction.Customer_id = ') +((QUOTENAME((@_userId), ''''))) + (N' and ') + (@actionCondition) + (N' >0 and customeraction.coreCustomerId =')  
    + (QUOTENAME((@_coreCustomerId), ''''))


exec(@select_statement)

   END
GO

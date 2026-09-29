ALTER TABLE [${dbxschemaname}].[alerttypechannel] DROP CONSTRAINT [FK_alerttypechannel_dbxalerttype]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] DROP CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_alerttype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] DROP CONSTRAINT [alertsubtype$FK_alertsubtype_alerttype]
GO
ALTER TABLE [${dbxschemaname}].dbxalerttype DROP CONSTRAINT [PK_dbxalerttype_id]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD  CONSTRAINT [PK_dbxalerttype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[alerttypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alerttypechannel_dbxalerttype] FOREIGN KEY([alertTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alerttypechannel] CHECK CONSTRAINT [FK_alerttypechannel_dbxalerttype]
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement]  WITH NOCHECK ADD  CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_alerttype] FOREIGN KEY([AlertTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] CHECK CONSTRAINT [dbxcustomeralertentitlement$FK_dbxcustomeralertentitlement_alerttype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype]  WITH NOCHECK ADD  CONSTRAINT [alertsubtype$FK_alertsubtype_alerttype] FOREIGN KEY([AlertTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalerttype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] CHECK CONSTRAINT [alertsubtype$FK_alertsubtype_alerttype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] DROP CONSTRAINT [FK_alertsubtypeaccounttype_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] DROP CONSTRAINT [FK_alertsubtypecustomertype_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] DROP CONSTRAINT [FK_alertsubtypeapp_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] DROP CONSTRAINT [FK_alertsubtypechannel_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypetext] DROP CONSTRAINT [FK_alertsubtypetext_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] DROP CONSTRAINT [communicationtemplate$FK_communicationtemplate_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] DROP CONSTRAINT [PK_alertsubtype_id]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD  CONSTRAINT [PK_alertsubtype_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypeaccounttype_alertsubtype] FOREIGN KEY([alertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] CHECK CONSTRAINT [FK_alertsubtypeaccounttype_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypecustomertype_alertsubtype] FOREIGN KEY([alertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] CHECK CONSTRAINT [FK_alertsubtypecustomertype_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeapp]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypeapp_alertsubtype] FOREIGN KEY([alertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] CHECK CONSTRAINT [FK_alertsubtypeapp_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypechannel]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypechannel_alertsubtype] FOREIGN KEY([alertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] CHECK CONSTRAINT [FK_alertsubtypechannel_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypetext]  WITH CHECK ADD  CONSTRAINT [FK_alertsubtypetext_alertsubtype] FOREIGN KEY([alertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypetext] CHECK CONSTRAINT [FK_alertsubtypetext_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate]  WITH NOCHECK ADD  CONSTRAINT [communicationtemplate$FK_communicationtemplate_alertsubtype] FOREIGN KEY([AlertSubTypeId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[alertsubtype] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] CHECK CONSTRAINT [communicationtemplate$FK_communicationtemplate_alertsubtype]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] DROP CONSTRAINT [PK_dbxalerttypetext_AlertTypeId]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD  CONSTRAINT [PK_dbxalerttypetext_AlertTypeId] PRIMARY KEY CLUSTERED 
(
	[AlertTypeId] ASC,
	[LanguageCode] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[customeralertchannel] DROP CONSTRAINT [FK_customeralertchannel_dbxalertcategory]
GO
ALTER TABLE [${dbxschemaname}].[customeralertfrequency] DROP CONSTRAINT [FK_customeralertfrequency_dbxalertcategory]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] DROP CONSTRAINT [dbxalerttype$FK_alerttype_alertcategory]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] DROP CONSTRAINT [PK_dbxalertcategory_id]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD  CONSTRAINT [PK_dbxalertcategory_id] PRIMARY KEY CLUSTERED 
(
	[id] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype]  WITH NOCHECK ADD  CONSTRAINT [dbxalerttype$FK_alerttype_alertcategory] FOREIGN KEY([AlertCategoryId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[dbxalerttype] CHECK CONSTRAINT [dbxalerttype$FK_alerttype_alertcategory]
GO
ALTER TABLE [${dbxschemaname}].[customeralertfrequency]  WITH CHECK ADD  CONSTRAINT [FK_customeralertfrequency_dbxalertcategory] FOREIGN KEY([alertCategoryId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[customeralertfrequency] CHECK CONSTRAINT [FK_customeralertfrequency_dbxalertcategory]
GO
ALTER TABLE [${dbxschemaname}].[customeralertchannel]  WITH CHECK ADD  CONSTRAINT [FK_customeralertchannel_dbxalertcategory] FOREIGN KEY([alertCategoryId],[companyLegalUnit])
REFERENCES [${dbxschemaname}].[dbxalertcategory] ([id],[companyLegalUnit])
GO
ALTER TABLE [${dbxschemaname}].[customeralertchannel] CHECK CONSTRAINT [FK_customeralertchannel_dbxalertcategory]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] DROP CONSTRAINT [PK_dbxalertcategorytext_AlertCategoryId]
GO
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD  CONSTRAINT [PK_dbxalertcategorytext_AlertCategoryId] PRIMARY KEY CLUSTERED 
(
	[AlertCategoryId] ASC,
	[LanguageCode] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] DROP CONSTRAINT [PK_communicationtemplate_Id]
GO
ALTER TABLE [${dbxschemaname}].[communicationtemplate] ADD  CONSTRAINT [PK_communicationtemplate_Id] PRIMARY KEY CLUSTERED 
(
	[Id] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] DROP CONSTRAINT [PK_alertcategorychannel_ChannelID]
GO
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD  CONSTRAINT [PK_alertcategorychannel_ChannelID] PRIMARY KEY CLUSTERED 
(
	[ChannelID] ASC,
	[AlertCategoryId] ASC,
	[companyLegalUnit] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[alertsubtypetext]';
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD CONSTRAINT [PK_alertsubtypetext_alertsubtypeid] PRIMARY KEY CLUSTERED 
(
	[alertSubTypeId] ASC,
	[languageCode] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[alerttypechannel]';
GO
ALTER TABLE [${dbxschemaname}].[alerttypechannel] ADD CONSTRAINT [PK_alerttypechannel_channelId] PRIMARY KEY CLUSTERED
(
	[channelId] ASC,
	[alertTypeId] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[alertsubtypeapp]';
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] ADD CONSTRAINT [PK_alertsubtypeapp_appId] PRIMARY KEY CLUSTERED
(
	[appId] ASC,
	[alertSubTypeId] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[alertsubtypechannel]';
GO
ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] ADD CONSTRAINT [PK_alertsubtypechannel_channelId] PRIMARY KEY CLUSTERED
(
	[channelId] ASC,
	[alertSubTypeId] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[dbxcustomeralertentitlement]';
GO
ALTER TABLE [${dbxschemaname}].[dbxcustomeralertentitlement] ADD CONSTRAINT [PK_dbxcustomeralertentitlement_customerId] PRIMARY KEY CLUSTERED
(
	[Customer_id] ASC,
	[alertCategoryId] ASC,
	[AlertTypeId] ASC,
	[alertSubTypeId] ASC,
	[AccountId] ASC,
	[AccountType] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[customerviewalertconfiguration]';
GO
ALTER TABLE [${dbxschemaname}].[customerviewalertconfiguration] ADD CONSTRAINT [PK_customerviewalertconfiguration_id] PRIMARY KEY CLUSTERED
(
	[id] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO
EXEC [${dbxschemaname}].[sp_delete_primary_constranit] '[${dbxschemaname}].[customeralertchannel]';
GO
ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD CONSTRAINT [PK_customeralertchannel_customerId] PRIMARY KEY CLUSTERED
(
	[customerId] ASC,
	[alertCategoryId] ASC,
	[alertTypeId] ASC,
	[alertSubTypeId] ASC,
	[channelId] ASC,
	[accountId] ASC,
	[accountType] ASC,
	[companyLegalUnit] ASC
) WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, SORT_IN_TEMPDB = OFF, IGNORE_DUP_KEY = OFF, ONLINE = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY];
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
GO

CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        dbxalerttype.Name as alertGroupName,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        alertsubtype.Name as alertName,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alerttypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem,
		alerttypechannel.companyLegalUnit AS companyLegalUnit
    FROM
        (((dbxalerttype
        JOIN alerttypechannel ON ((dbxalerttype.id = alerttypechannel.alertTypeId)
		AND (dbxalerttype.companyLegalUnit = alerttypechannel.companyLegalUnit)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)
		AND (dbxalerttype.companyLegalUnit = alertsubtype.companyLegalUnit)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)
		AND (dbxalerttype.companyLegalUnit = dbxalertcategory.companyLegalUnit)));
		
GO

UPDATE [${dbxschemaname}].alertsubtype set externalSystem=0 where id='PHONE_CHANGE';
UPDATE [${dbxschemaname}].alertsubtype set isAccountLevel=0 where id='LOGIN_ATTEMPT';
UPDATE [${dbxschemaname}].dbxalerttype set isAccountLevel=0 where id='LOGIN_ATTEMPT';
UPDATE [${dbxschemaname}].dbxalerttype set isAccountLevel=0 where id='PROFILE_UPDATE';

GO

DROP VIEW IF EXISTS [${dbxschemaname}].[bulkpaymentrecord_view]
GO

CREATE VIEW [${dbxschemaname}].[bulkpaymentrecord_view] AS 
    SELECT
        bulkpaymentrecord.featureActionId AS featureActionId,
        bulkpaymentrecord.companyId AS companyId,
        bulkpaymentrecord.createdby AS createdby,
        bulkpaymentrecord.createdts AS createdts,
        bulkpaymentrecord.status AS status,
        bulkpaymentrecord.roleId AS roleId,
        bulkpaymentrecord.paymentDate AS scheduledDate,
        bulkpaymentrecord.totalAmount AS amount,
        bulkpaymentrecord.fromAccount AS fromAccountNumber
    FROM
        bulkpaymentrecord;
GO

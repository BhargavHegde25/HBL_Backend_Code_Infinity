ALTER TABLE [${dbxschemaname}].[dbxalertcategory] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[dbxalertcategorytext] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[dbxalerttype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[dbxalerttypetext] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alerttypechannel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtypetext] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtypeapp] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtypecustomertype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtypeaccounttype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertsubtypechannel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customeralertchannel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[notification] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[usernotification] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertcategorychannel] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[customeralertfrequency] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertattribute] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertattributelistvalues] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
ALTER TABLE [${dbxschemaname}].[alertrecipienttype] ADD [companyLegalUnit] VARCHAR(50) NOT NULL DEFAULT 'ALL';
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[alertsubtypetext_view];

GO

CREATE VIEW [${dbxschemaname}].[alertsubtypetext_view]
AS
SELECT [${dbxschemaname}].alertsubtype.id AS alertsubtype_id, [${dbxschemaname}].alertsubtype.AlertTypeId AS alertsubtype_alertTypeId, [${dbxschemaname}].alertsubtype.Name AS alertsubtype_Name, [${dbxschemaname}].alertsubtype.Status_id AS alertsubtype_StatusId, 
                  [${dbxschemaname}].alertsubtype.isAccountLevel AS alertsubtype_isAccountLevel, [${dbxschemaname}].alertsubtype.attributeId AS alertsubtype_attributeId, [${dbxschemaname}].alertsubtype.alertConditionId AS alertsubtype_alertConditionId, 
                  [${dbxschemaname}].alertsubtype.value1 AS alertsubtype_value1, [${dbxschemaname}].alertsubtype.value2 AS alertsubtype_value2, [${dbxschemaname}].alertsubtype.isGlobal AS alertsubtype_isGlobal, [${dbxschemaname}].alertsubtype.defaultFrequencyId AS alertsubtype_defaultFrequencyId, 
                  [${dbxschemaname}].alertsubtype.defaultFrequencyValue AS alertsubtype_defaultFrequencyValue, [${dbxschemaname}].alertsubtype.defaultFrequencyTime AS alertsubtype_defaultFrequencyTime, [${dbxschemaname}].alertsubtype.createdby AS alertsubtype_createdby, 
                  [${dbxschemaname}].alertsubtype.modifiedby AS alertsubtype_modifiedby, [${dbxschemaname}].alertsubtype.createdts AS alertsubtype_createdts, [${dbxschemaname}].alertsubtype.lastmodifiedts AS alertsubtype_lastmodifiedts, 
                  [${dbxschemaname}].alertsubtype.synctimestamp AS alertsubtype_synctimestamp, [${dbxschemaname}].alertsubtype.softdeleteflag AS alertsubtype_softdeleteflag, [${dbxschemaname}].alertsubtype.isAutoSubscribeEnabled AS alertsubtype_isAutoSubscribeEnabled, 
                  [${dbxschemaname}].alertsubtypetext.languageCode AS alertsubtypetext_languageCode, [${dbxschemaname}].alertsubtypetext.description AS alertsubtypetext_description, [${dbxschemaname}].alertsubtype.externalSystem AS alertsubtype_externalSystem, 
                  [${dbxschemaname}].alertsubtypetext.displayName AS alertsubtypetext_displayName, [${dbxschemaname}].alertsubtype.companyLegalUnit AS alertsubtype_companyLegalUnit
FROM     [${dbxschemaname}].alertsubtype INNER JOIN
                  [${dbxschemaname}].alertsubtypetext ON [${dbxschemaname}].alertsubtypetext.alertSubTypeId = [${dbxschemaname}].alertsubtype.id AND [${dbxschemaname}].alertsubtypetext.companyLegalUnit = [${dbxschemaname}].alertsubtype.companyLegalUnit
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerttype_view];

GO
CREATE VIEW [${dbxschemaname}].[alerttype_view] (
   [alerttype_id], 
   [alerttype_Name], 
   [alerttype_AlertCategoryId], 
   [alerttype_Status_id], 
   [alerttype_IsGlobal], 
   [alerttype_DisplaySequence], 
   [alerttype_freqId], 
   [alerttype_freqValue], 
   [alerttype_freqTime], 
   [alerttype_isAccountLevel],
   [userAlerts_count],
   [Alerts_count], 
   [alerttype_softdeleteflag], 
   [alerttypetext_LanguageCode], 
   [alerttypetext_DisplayName], 
   [alerttypetext_Description], 
   [alerttypetext_createdby], 
   [alerttypetext_modifiedby], 
   [alerttypetext_createdts], 
   [alerttypetext_lastmodifiedts], 
   [alerttypetext_synctimestamp],
   [alerttypetext_softdeleteflag],
   [alerttype_companyLegalUnit])AS
    SELECT 
        [${dbxschemaname}].dbxalerttype.id AS alerttype_id,
        [${dbxschemaname}].dbxalerttype.Name AS alerttype_Name,
        [${dbxschemaname}].dbxalerttype.AlertCategoryId AS alerttype_AlertCategoryId,
        [${dbxschemaname}].dbxalerttype.Status_id AS alerttype_Status_id,
        [${dbxschemaname}].dbxalerttype.IsGlobal AS alerttype_IsGlobal,
        [${dbxschemaname}].dbxalerttype.DisplaySequence AS alerttype_DisplaySequence,
		[${dbxschemaname}].dbxalerttype.defaultFrequencyId AS alerttype_freqId,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyValue AS alerttype_freqValue,
        [${dbxschemaname}].dbxalerttype.defaultFrequencyTime AS alerttype_freqTime,
        [${dbxschemaname}].dbxalerttype.isAccountLevel AS alerttype_isAccountLevel,                 
		(SELECT 
                COUNT(*)
            FROM
                [${dbxschemaname}].alertsubtype
            WHERE
                (alertsubtype.AlertTypeId = dbxalerttype.id) AND (alertsubtype.IsGlobal = '0') ) AS userAlerts_count,
		(SELECT 
                COUNT(alertsubtype.id)
            FROM
                [${dbxschemaname}].alertsubtype
            WHERE
                (alertsubtype.AlertTypeId = dbxalerttype.id)) AS Alerts_count,
        [${dbxschemaname}].dbxalerttype.softdeleteflag AS alerttype_softdeleteflag,
        [${dbxschemaname}].dbxalerttypetext.LanguageCode AS alerttypetext_LanguageCode,
        [${dbxschemaname}].dbxalerttypetext.DisplayName AS alerttypetext_DisplayName,
        [${dbxschemaname}].dbxalerttypetext.Description AS alerttypetext_Description,
        [${dbxschemaname}].dbxalerttypetext.createdby AS alerttypetext_createdby,
        [${dbxschemaname}].dbxalerttypetext.modifiedby AS alerttypetext_modifiedby,
        [${dbxschemaname}].dbxalerttypetext.createdts AS alerttypetext_createdts,
        [${dbxschemaname}].dbxalerttypetext.lastmodifiedts AS alerttypetext_lastmodifiedts,
        [${dbxschemaname}].dbxalerttypetext.synctimestamp AS alerttypetext_synctimestamp,
        [${dbxschemaname}].dbxalerttypetext.softdeleteflag AS alerttypetext_softdeleteflag,
		[${dbxschemaname}].dbxalerttype.companyLegalUnit AS alerttype_companyLegalUnit
    FROM
        ([${dbxschemaname}].dbxalerttypetext
        INNER JOIN [${dbxschemaname}].dbxalerttype ON (dbxalerttypetext.AlertTypeId = dbxalerttype.id and dbxalerttypetext.companyLegalUnit = dbxalerttype.companyLegalUnit));
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alertcategory_view];

GO

CREATE VIEW [${dbxschemaname}].[alertcategory_view] AS
    SELECT 
        [${dbxschemaname}].dbxalertcategory.id AS alertcategory_id,
        [${dbxschemaname}].dbxalertcategory.status_id AS alertcategory_status_id,
        [${dbxschemaname}].dbxalertcategory.accountLevel AS alertcategory_accountLevel,
        [${dbxschemaname}].dbxalertcategory.DisplaySequence AS alertcategory_DisplaySequence,
        [${dbxschemaname}].dbxalertcategory.softdeleteflag AS alertcategory_softdeleteflag,
        [${dbxschemaname}].dbxalertcategory.Name AS alertcategory_Name,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyId AS alertcategory_freqId,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyValue AS alertcategory_freqValue,
        [${dbxschemaname}].dbxalertcategory.defaultFrequencyTime AS alertcategory_freqTime,
        (SELECT 
                COUNT(dbxalerttype.id)
            FROM
                [${dbxschemaname}].dbxalerttype
            WHERE
                (dbxalerttype.AlertCategoryId = dbxalertcategory.id)) AS Groups_count,
        (SELECT 
                SUM([${dbxschemaname}].alerttype_view.Alerts_count)
            FROM
                [${dbxschemaname}].alerttype_view
            WHERE
                (alerttype_view.alerttype_AlertCategoryId = dbxalertcategory.id)) AS Alerts_count,
        [${dbxschemaname}].dbxalertcategorytext.LanguageCode AS alertcategorytext_LanguageCode,
        [${dbxschemaname}].dbxalertcategorytext.DisplayName AS alertcategorytext_DisplayName,
        [${dbxschemaname}].dbxalertcategorytext.Description AS alertcategorytext_Description,
        [${dbxschemaname}].dbxalertcategorytext.createdby AS alertcategorytext_createdby,
        [${dbxschemaname}].dbxalertcategorytext.modifiedby AS alertcategorytext_modifiedby,
        [${dbxschemaname}].dbxalertcategorytext.createdts AS alertcategorytext_createdts,
        [${dbxschemaname}].dbxalertcategorytext.lastmodifiedts AS alertcategorytext_lastmodifiedts,
        [${dbxschemaname}].dbxalertcategorytext.synctimestamp AS alertcategorytext_synctimestamp,
        [${dbxschemaname}].dbxalertcategorytext.softdeleteflag AS alertcategorytext_softdeleteflag,
		[${dbxschemaname}].dbxalertcategory.companyLegalUnit AS alertcategory_companyLegalUnit
    FROM
        ([${dbxschemaname}].dbxalertcategorytext
        JOIN [${dbxschemaname}].dbxalertcategory ON (([${dbxschemaname}].dbxalertcategorytext.AlertCategoryId = [${dbxschemaname}].dbxalertcategory.id) 
		and ([${dbxschemaname}].dbxalertcategorytext.companyLegalUnit = [${dbxschemaname}].dbxalertcategory.companyLegalUnit) ));

GO

DROP VIEW IF EXISTS [${dbxschemaname}].[notificationview];

GO

CREATE VIEW [${dbxschemaname}].[notificationview] (
   [notificationId], 
   [imageURL], 
   [isRead], 
   [notificationActionLink], 
   [notificationModule], 
   [notificationSubject], 
   [notificationSubModule], 
   [notificationText], 
   [receivedDate], 
   [userNotificationId], 
   [user_id], 
   [notificationCategory], 
   [actionButtonLabelName],
   [companyLegalUnit])
AS 
   SELECT 
      notification.notificationId AS notificationId, 
      notification.imageURL AS imageURL, 
      usernotification.isRead AS isRead, 
      notification.notificationActionLink AS notificationActionLink, 
      notification.notificationModule AS notificationModule, 
      notification.notificationSubject AS notificationSubject, 
      notification.notificationSubModule AS notificationSubModule, 
      notification.notificationText AS notificationText, 
      usernotification.receivedDate AS receivedDate, 
      usernotification.id AS userNotificationId, 
      usernotification.user_id AS user_id, 
      notification.notificationCategory AS notificationCategory, 
      notification.actionButtonLabelName AS actionButtonLabelName,
	  notification.companyLegalUnit AS companyLegalUnit
   FROM ([${dbxschemaname}].usernotification 
      INNER JOIN [${dbxschemaname}].notification 
      ON ((notification.notificationId = usernotification.notification_id) and 
	  (notification.companyLegalUnit = usernotification.companyLegalUnit)))
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[alertattribute_view];

GO

CREATE VIEW [${dbxschemaname}].[alertattribute_view] (
   [alertattribute_id], 
   [alertattribute_LanguageCode], 
   [alertattribute_name], 
   [alertattribute_type], 
   [alertattribute_softdeleteflag], 
   [alertattributelistvalues_id], 
   [alertattributelistvalues_AlertAttributeId], 
   [alertattributelistvalues_LanguageCode], 
   [alertattributelistvalues_name], 
   [alertattributelistvalues_softdeleteflag],
   [alertattribute_companyLegalUnit])
AS 
   SELECT 
      alertattribute.id AS alertattribute_id, 
      alertattribute.LanguageCode AS alertattribute_LanguageCode, 
      alertattribute.name AS alertattribute_name, 
      alertattribute.type AS alertattribute_type, 
      alertattribute.softdeleteflag AS alertattribute_softdeleteflag, 
      alertattributelistvalues.id AS alertattributelistvalues_id, 
      alertattributelistvalues.AlertAttributeId AS alertattributelistvalues_AlertAttributeId, 
      alertattributelistvalues.LanguageCode AS alertattributelistvalues_LanguageCode, 
      alertattributelistvalues.name AS alertattributelistvalues_name, 
      alertattributelistvalues.softdeleteflag AS alertattributelistvalues_softdeleteflag,
      alertattribute.companyLegalUnit AS alertattribute_companyLegalUnit
   FROM ([${dbxschemaname}].alertattribute 
      LEFT JOIN [${dbxschemaname}].alertattributelistvalues 
      ON ((alertattribute.id = alertattributelistvalues.AlertAttributeId and alertattribute.companyLegalUnit = alertattributelistvalues.companyLegalUnit)))
GO


GO
ALTER PROCEDURE [${dbxschemaname}].[customeralertchannel_sync]
								@operationType varchar(20),
								@changedLevel varchar(20),
								@channelsStr varchar(255),                                                        
                                @filterValue varchar(255),
								@companyLegalUnit varchar(255)
							AS
BEGIN

  SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
  declare @preference nvarchar(max)
  DECLARE @channels_list nvarchar(max)    
  set @preference = (select alertPreferenceView from [${dbxschemaname}].[customerviewalertconfiguration] where 1 = 1);

 IF @operationType is null 
  BEGIN
     SET @operationType = '';
  END
  
ELSE IF @operationType = 'edit' and @changedLevel is not null 
  BEGIN 
   SET @channels_list = ( SELECT String_agg(CAST(id as nvarchar(max)),',') FROM [${dbxschemaname}].channel WHERE [${dbxschemaname}].FIND_IN_SET(id,@channelsStr) <> 0);
    IF @channels_list IS NULL
      BEGIN
        SET @channels_list = ''
      END	 
    END
	IF (@preference = @changedLevel AND @changedLevel LIKE 'CATEGORY') 
	BEGIN
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertCategoryId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0 and companyLegalUnit = @companyLegalUnit;  
	END
	ELSE IF (@preference = @changedLevel AND @changedLevel LIKE 'GROUP') 
	BEGIN
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertTypeId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0 and companyLegalUnit = @companyLegalUnit;
	END
	ELSE IF (@preference = @changedLevel AND @changedLevel LIKE 'ALERT') 
	BEGIN
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertSubTypeId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0 and companyLegalUnit = @companyLegalUnit;	  
	END 
  
  ELSE IF @operationType = 'reassign' BEGIN
	  IF (@preference != 'CATEGORY' ) BEGIN
	   DELETE FROM [${dbxschemaname}].customeralertchannel where alertTypeId = @filterValue and companyLegalUnit = @companyLegalUnit;
	  END 
  END 
END

GO

DROP VIEW IF EXISTS [${dbxschemaname}].[communicationtemplate_channel_view];
GO

CREATE VIEW [${dbxschemaname}].[communicationtemplate_channel_view] (
   [communicationtemplate_id], 
   [communicationtemplate_Name], 
   [communicationtemplate_Text], 
   [communicationtemplate_AlertSubTypeId], 
   [communicationtemplate_LanguageCode], 
   [communicationtemplate_ChannelID], 
   [communicationtemplate_Status_id], 
   [communicationtemplate_Subject], 
   [communicationtemplate_SenderName], 
   [communicationtemplate_SenderEmail], 
   [channeltext_Description], 
   [channeltext_softdeleteflag], 
   [communicationtemplate_softdeleteflag],
   [communicationtemplate_companyLegalUnit])
AS 
   SELECT 
      communicationtemplate.Id AS communicationtemplate_id, 
      communicationtemplate.Name AS communicationtemplate_Name, 
      communicationtemplate.Text AS communicationtemplate_Text, 
      communicationtemplate.AlertSubTypeId AS communicationtemplate_AlertSubTypeId, 
      communicationtemplate.LanguageCode AS communicationtemplate_LanguageCode, 
      communicationtemplate.ChannelID AS communicationtemplate_ChannelID, 
      communicationtemplate.Status_id AS communicationtemplate_Status_id, 
      communicationtemplate.Subject AS communicationtemplate_Subject, 
      communicationtemplate.SenderName AS communicationtemplate_SenderName, 
      communicationtemplate.SenderEmail AS communicationtemplate_SenderEmail, 
      channeltext.Description AS channeltext_Description, 
      channeltext.softdeleteflag AS channeltext_softdeleteflag, 
      communicationtemplate.softdeleteflag AS communicationtemplate_softdeleteflag,
	  communicationtemplate.companyLegalUnit AS communicationtemplate_companyLegalUnit
   FROM ([${dbxschemaname}].communicationtemplate 
      INNER JOIN [${dbxschemaname}].channeltext 
      ON (((communicationtemplate.ChannelID = channeltext.channelID) AND (communicationtemplate.LanguageCode = channeltext.LanguageCode))))
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertFrequency_sync];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertFrequency_sync]
                              @operationType varchar(20),
                              @filterValue varchar(255),
                              @companyLegalUnit varchar(255)
                           AS
BEGIN
   SET  XACT_ABORT  ON
   SET  NOCOUNT  ON
  
   declare @preference nvarchar(max);
   set @preference = (select alertPreferenceView from [${dbxschemaname}].[customerviewalertconfiguration] where 1 = 1);
   IF @operationType is null
   BEGIN
      SET @operationType = '';
   END
   ELSE IF (@operationType LIKE 'edit' AND @preference = 'ALERT')
   BEGIN
      DELETE FROM [${dbxschemaname}].customeralertfrequency where alertSubTypeId = @filterValue and companyLegalUnit = @companyLegalUnit;
   END
   ELSE IF (@operationType LIKE 'reassign')
   BEGIN
      IF (@preference != 'CATEGORY')
      BEGIN
         DELETE FROM [${dbxschemaname}].customeralertfrequency where alertTypeId = @filterValue and companyLegalUnit = @companyLegalUnit;
      END
   END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[dbxcustomeralertentitlement_sync];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[dbxcustomeralertentitlement_sync]
                              @operationType varchar(20),
                              @filterValue varchar(255),
                              @companyLegalUnit varchar(255)
                           AS
BEGIN

   SET  XACT_ABORT  ON
   SET  NOCOUNT  ON
   IF @operationType is null
   BEGIN
      SET @operationType = '';
   END
   ELSE IF (@operationType LIKE 'edit')
   BEGIN
      DELETE FROM [${dbxschemaname}].dbxcustomeralertentitlement where alertSubTypeId = @filterValue and companyLegalUnit = @companyLegalUnit;
   END
   ELSE IF (@operationType LIKE 'reassign')
   BEGIN
      DELETE FROM [${dbxschemaname}].dbxcustomeralertentitlement where AlertTypeId = @filterValue and companyLegalUnit = @companyLegalUnit;
   END
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertcategorychannel.ChannelID AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem,
		alertcategorychannel.companyLegalUnit AS companyLegalUnit
    FROM
        (((dbxalertcategory
        JOIN alertcategorychannel ON ((alertcategorychannel.AlertCategoryId = dbxalertcategory.id)
        AND (alertcategorychannel.companyLegalUnit = dbxalertcategory.companyLegalUnit)))
        JOIN dbxalerttype ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)
        AND (dbxalerttype.companyLegalUnit = dbxalertcategory.companyLegalUnit)))
        JOIN alertsubtype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)
        AND (alertsubtype.companyLegalUnit = dbxalerttype.companyLegalUnit)))
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
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
		AND (dbxalerttype.companyLegalUnit = dbxalertcategory.companyLegalUnit)))
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel] 
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]
  AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        alertsubtype.attributeId AS AttributeId,
		alertsubtype.recipienttype as recipienttype,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertsubtypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem,
		alertsubtypechannel.companyLegalUnit AS companyLegalUnit
    FROM
        (((alertsubtype
        JOIN alertsubtypechannel ON ((alertsubtype.id = alertsubtypechannel.alertSubTypeId)
		AND (alertsubtype.companyLegalUnit = alertsubtypechannel.companyLegalUnit)))
        JOIN dbxalerttype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)
		AND (alertsubtype.companyLegalUnit = dbxalerttype.companyLegalUnit)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)
		AND (dbxalerttype.companyLegalUnit = dbxalertcategory.companyLegalUnit)))
GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertcategorylevel]
GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertcategorylevel]
 AS
    SELECT 
        alertsubtype.id AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId,
		dbxcustomeralertentitlement.companyLegalUnit AS companyLegalUnit
    FROM
       (((dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId)
			AND (dbxcustomeralertentitlement.companyLegalUnit = customeralertchannel.companyLegalUnit))))
        JOIN dbxalerttype ON ((dbxcustomeralertentitlement.AlertTypeId = dbxalerttype.id)
		AND (dbxcustomeralertentitlement.companyLegalUnit = dbxalerttype.companyLegalUnit)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)
		AND (dbxalerttype.companyLegalUnit = alertsubtype.companyLegalUnit)))
GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertgrouplevel]

GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertgrouplevel]
 AS
    SELECT 
        alertsubtype.id AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId,
		dbxcustomeralertentitlement.companyLegalUnit AS companyLegalUnit
    FROM
       ((dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.AlertTypeId = customeralertchannel.alertTypeId)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId)
			AND (dbxcustomeralertentitlement.companyLegalUnit = customeralertchannel.companyLegalUnit))))
        JOIN alertsubtype ON ((dbxcustomeralertentitlement.AlertTypeId = alertsubtype.AlertTypeId)
		AND (dbxcustomeralertentitlement.companyLegalUnit = alertsubtype.companyLegalUnit)))
GO

DROP VIEW IF EXISTS  [${dbxschemaname}].[alertcustomerchannels_view_alertlevel]
GO

CREATE VIEW [${dbxschemaname}].[alertcustomerchannels_view_alertlevel]
 AS
    SELECT 
        dbxcustomeralertentitlement.alertSubTypeId AS AlertSubTypeId,
        dbxcustomeralertentitlement.Customer_id AS Customer_id,
        dbxcustomeralertentitlement.AccountId AS AccountId,
        dbxcustomeralertentitlement.AccountType AS AccountType,
        dbxcustomeralertentitlement.Value1 AS Value1,
        dbxcustomeralertentitlement.Value2 AS Value2,
        customeralertchannel.channelId AS ChannelId,
		dbxcustomeralertentitlement.companyLegalUnit AS companyLegalUnit
    FROM
        (dbxcustomeralertentitlement
        JOIN customeralertchannel ON (((dbxcustomeralertentitlement.Customer_id = customeralertchannel.customerId)
            AND (dbxcustomeralertentitlement.AccountId = customeralertchannel.accountId)
            AND (dbxcustomeralertentitlement.AccountType = customeralertchannel.accountType)
            AND (dbxcustomeralertentitlement.alertSubTypeId = customeralertchannel.alertSubTypeId)
            AND (dbxcustomeralertentitlement.alertCategoryId = customeralertchannel.alertCategoryId)
            AND (dbxcustomeralertentitlement.AlertTypeId = customeralertchannel.alertTypeId)
			AND (dbxcustomeralertentitlement.companyLegalUnit = customeralertchannel.companyLegalUnit))))
GO
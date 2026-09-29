CREATE TABLE  [${dbxschemaname}].[placeholder]([placeholderId] VARCHAR(255), [application] VARCHAR(255),[channelSubType] VARCHAR(255),[placeholderDescription] varchar(255),[placeholderIdentifier] varchar(255),[placeholderName] varchar(255),[imageResolution] VARCHAR(255),[imageScale] varchar(255),[imageSize] varchar(255), PRIMARY KEY ([placeholderId]));
CREATE TABLE  [${dbxschemaname}].[onlinecontent]([onlineContentId] VARCHAR(255), [targetURL] varchar(255), [campaignId] varchar(255), [placeholderId] varchar(255), [imageURL] varchar(255), [imageIndex] int, [callToActionButtonLabel] varchar(255), [callToActionTargetURL] varchar(255), [showReadLaterButton] varchar(255), [showCloseIcon] varchar(255), [bannerTitle] varchar(255), [bannerDescription] varchar(255), PRIMARY KEY ([onlineContentId]));
CREATE TABLE  [${dbxschemaname}].[datacontext]([dataContextId] varchar(255), [dataContextName] varchar(255), [dataContextDescription] varchar(255), [dataContextSource] varchar(255), [dataContextServiceName] varchar(255), [dataContextEndPoints] varchar(255), PRIMARY KEY ([dataContextId]));
CREATE TABLE  [${dbxschemaname}].[eventtriggers]([eventTriggerId] varchar(255), [eventCode] varchar(255), [eventDescription]  varchar(255), [eventName] varchar(255), [eventSource] varchar(255), [eventTriggerType] varchar(255), PRIMARY KEY ([eventTriggerId])); 


CREATE TABLE [${dbxschemaname}].[campaigndefinition]
  (
	campaignId		VARCHAR(255) NOT NULL,
	campaignName		VARCHAR(255),
	campaignDescription		VARCHAR(255),
	objectiveType		VARCHAR(255),
	productId		VARCHAR(255),
	productGroupId		VARCHAR(255),
	campaignPriority		INTEGER,
	campaignType		VARCHAR(255),
	campaignStatus		VARCHAR(255),
	startDate		DATETIME,
	endDate		DATETIME,
	PRIMARY KEY (campaignId)
 );

CREATE TABLE [${dbxschemaname}].[campaignchanneltype]
(
		campaignId		VARCHAR(255) NOT NULL ,
		channelType		VARCHAR(255),
		PRIMARY KEY (campaignId, channelType)
);
CREATE TABLE [${dbxschemaname}].[campaignchanneldetails]
(
		campaignId		VARCHAR(255) ,
		channelPriority		INT ,
		channelSubType		VARCHAR(255),
		PRIMARY KEY (campaignId,channelSubType)		
);
CREATE TABLE [${dbxschemaname}].[campaigneventtrigger]
(
		campaignId		VARCHAR(255) NOT NULL ,
		eventTriggerId		VARCHAR(255) NOT NULL ,
		PRIMARY KEY (campaignId,eventTriggerId)	
);
CREATE TABLE [${dbxschemaname}].[campaignprofile]
(
		campaignId		VARCHAR(255) NOT NULL ,
		profileId		VARCHAR(255) NOT NULL ,
		PRIMARY KEY (campaignId,profileId)
);
CREATE TABLE [${dbxschemaname}].[profile]
  (
			profileId		VARCHAR(255) NOT NULL,
			profileName		VARCHAR(255),
			profileDescription		VARCHAR(255),
			profileStatus		VARCHAR(255),
			numberOfUsers		INTEGER,
			profileCreationDate		DATETIME,
			profileDeactivatedDate		DATETIME,
			PRIMARY KEY (profileId)
 );

CREATE TABLE [${dbxschemaname}].[profilecondition]
  (
			profileId		VARCHAR(255),
			dataContextId		VARCHAR(255),
			profileConditionId		VARCHAR(255) NOT NULL,
			conditionExpression		VARCHAR(255),
			PRIMARY KEY (profileConditionId)
 );

CREATE TABLE [${dbxschemaname}].[offlinetemplate]
  (
			offlineTemplateId		VARCHAR(255) NOT NULL,
			campaignId		VARCHAR(255),
			channelSubType		VARCHAR(255),
			subject		VARCHAR(255),
			content		VARCHAR(MAX),
			PRIMARY KEY (offlineTemplateId)
 );
 
CREATE INDEX campaignId_index ON [${dbxschemaname}].[campaigndefinition] (campaignId);
CREATE INDEX profileId_index ON [${dbxschemaname}].[profile] (profileId);
CREATE INDEX profileConditionId_index ON [${dbxschemaname}].[profilecondition] (profileConditionId);
CREATE INDEX placeholderId_index ON [${dbxschemaname}].[placeholder] (placeholderId);
CREATE INDEX dataContextId_index ON [${dbxschemaname}].[datacontext] (dataContextId);
CREATE INDEX offlineTemplateId_index ON [${dbxschemaname}].[offlinetemplate] (offlineTemplateId);
CREATE INDEX eventTriggerId_index ON [${dbxschemaname}].[eventtriggers] (eventTriggerId);
CREATE INDEX onlineContentId_index  ON [${dbxschemaname}].[onlinecontent] (onlineContentId);

ALTER TABLE [${dbxschemaname}].[campaigneventtrigger] ADD CONSTRAINT campaignId_cet_fk FOREIGN KEY (campaignId) REFERENCES [${dbxschemaname}].[campaigndefinition] (campaignId);
ALTER TABLE [${dbxschemaname}].[campaigneventtrigger] ADD CONSTRAINT eventTriggerId_cet_fk FOREIGN KEY (eventTriggerId) REFERENCES [${dbxschemaname}].[eventtriggers] (eventTriggerId);
ALTER TABLE [${dbxschemaname}].[campaignprofile] ADD CONSTRAINT campaignId_cp_fk FOREIGN KEY (campaignId) REFERENCES [${dbxschemaname}].[campaigndefinition] (campaignId);
ALTER TABLE [${dbxschemaname}].[campaignprofile] ADD CONSTRAINT profileId_cp_fk FOREIGN KEY (profileId) REFERENCES [${dbxschemaname}].[profile] (profileId);
ALTER TABLE [${dbxschemaname}].[profilecondition] ADD CONSTRAINT profileId_pc_fk FOREIGN KEY (profileId) REFERENCES [${dbxschemaname}].[profile] ( profileId);
ALTER TABLE [${dbxschemaname}].[profilecondition] ADD CONSTRAINT dataContextId_pc_fk FOREIGN KEY (dataContextId) REFERENCES [${dbxschemaname}].[datacontext] ( dataContextId);
ALTER TABLE [${dbxschemaname}].[offlinetemplate] ADD CONSTRAINT campaignId_ot_fk FOREIGN KEY (campaignId) REFERENCES [${dbxschemaname}].[campaigndefinition] ( campaignId);
ALTER TABLE [${dbxschemaname}].[onlinecontent]	ADD CONSTRAINT campaignId_oc_fk FOREIGN KEY (campaignId) REFERENCES [${dbxschemaname}].[campaigndefinition] (campaignId);
ALTER TABLE [${dbxschemaname}].[onlinecontent]	ADD CONSTRAINT placeholderId_oc_fk FOREIGN KEY (placeholderId) REFERENCES [${dbxschemaname}].[placeHolder] ( placeholderId);

ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [clearingCode] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [clearingIdentifierCode] VARCHAR(100) NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [streetName] VARCHAR(140) NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [townName] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [bankCountryName] VARCHAR(50) NULL;
ALTER TABLE [${dbxschemaname}].[externalaccount] ADD [intermediaryBIC] VARCHAR(45) NULL;


CREATE TABLE [${dbxschemaname}].[financialinstitutiontype] (
	[finInstitutionTypeId] nvarchar(50) NOT NULL,
	[description] nvarchar(50) NOT NULL,
	[level] nvarchar(500) NOT NULL,
	[prefix] nvarchar(50) NOT NULL,
	[createdts] datetime2(0) DEFAULT getdate() NOT NULL,
	[modifiedts] datetime2(0) DEFAULT getdate() NOT NULL,
	[softdeleteflag] tinyint DEFAULT 0  NOT NULL,
	CONSTRAINT financialinstitutiontype_PK PRIMARY KEY (finInstitutionTypeId)
);
GO


CREATE TABLE [${dbxschemaname}].[financialinstitution] (
  
  [finInstitutionId] nvarchar(50) NOT NULL,		
  [name] nvarchar(50) NOT NULL,
  [shortName] nvarchar(50) NOT NULL,
  [typeId] nvarchar(50)  NOT NULL,
  [parentId] nvarchar(50) NOT NULL,
  [countryCode] nvarchar(50) NOT NULL,
  [baseCurrency] nvarchar(50) NOT NULL,
  [language] nvarchar(50) NOT NULL,
  [effectiveDate] datetime2(0) NOT NULL,
  [closeDate] datetime2(0) NOT NULL,
  [comments] nvarchar(50) NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([finInstitutionId]),
  CONSTRAINT [FK_financialinstitution_CountryCode] FOREIGN KEY ([countryCode]) REFERENCES [${dbxschemaname}].[country] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_financialinstitution_TypeId] FOREIGN KEY ([typeId]) REFERENCES [${dbxschemaname}].[financialinstitutiontype] ([finInstitutionTypeId]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO




CREATE TABLE [${dbxschemaname}].[organisationalunittype] (
	[organisationUnitTypeId] nvarchar(50) NOT NULL,
	[description] nvarchar(50) NOT NULL,
	[sharing] nvarchar(500) NOT NULL,
	[createdts] datetime2(0) DEFAULT getdate() NOT NULL,
	[modifiedts] datetime2(0) DEFAULT getdate() NOT NULL,
	[softdeleteflag] tinyint DEFAULT 0  NOT NULL,
	CONSTRAINT organisationalunittype_PK PRIMARY KEY (organisationUnitTypeId)
);
GO


CREATE TABLE [${dbxschemaname}].[organisationunit] (
  
  [organisationalUnitId] nvarchar(50) NOT NULL,		
  [name] nvarchar(50) NOT NULL,
  [shortName] nvarchar(50) NOT NULL,
  [usageType] nvarchar(50)  NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([organisationalUnitId])
  );
GO


CREATE TABLE [${dbxschemaname}].[financialinstitutionrelationship] (
  [relationshipId] nvarchar(50) NOT NULL,		
  [legalEntityId] nvarchar(50) NOT NULL,
  [relatedLegalEntityId] nvarchar(50) NOT NULL,
  [relationshipType] nvarchar(50)  NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([relationshipId]),
  CONSTRAINT [FK_financialinstitutionrelationship_LegalEntityId] FOREIGN KEY ([legalEntityId]) REFERENCES [${dbxschemaname}].[financialinstitution] ([finInstitutionId]) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT [FK_financialinstitutionrelationship_RelatedLegalEntityId] FOREIGN KEY ([relatedLegalEntityId]) REFERENCES [${dbxschemaname}].[financialinstitution] ([finInstitutionId]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO


CREATE TABLE [${dbxschemaname}].[financialinstitutionaltkey] (
  [alternateName] nvarchar(50) NOT NULL,		
  [alternateKey] nvarchar(50) NOT NULL,
  [entityId] nvarchar(50) NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([alternateName],[alternateKey]),
  CONSTRAINT [FK_financialinstitutionaltkey_EntityId] FOREIGN KEY ([entityId]) REFERENCES [${dbxschemaname}].[financialinstitution] ([finInstitutionId]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO


CREATE TABLE [${dbxschemaname}].[financialinstitutionorganisationunits] (
  [finInstitutionId] nvarchar(50) NOT NULL,		
  [organisationalUnitId] nvarchar(50) NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([finInstitutionId],[organisationalUnitId]),
  CONSTRAINT [FK_financialinstitutionorganisationunits_FinInstitutionId] FOREIGN KEY ([finInstitutionId]) REFERENCES [${dbxschemaname}].[financialinstitution] ([finInstitutionId]) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT [FK_financialinstitutionorganisationunits_organisationalUnitId] FOREIGN KEY ([organisationalUnitId]) REFERENCES [${dbxschemaname}].[organisationunit] ([organisationalUnitId]) ON DELETE NO ACTION ON UPDATE NO ACTION

);
GO




CREATE TABLE [${dbxschemaname}].[organisationalunittypemapping] (
  [organisationalUnitId] nvarchar(50) NOT NULL,		
  [organisationUnitTypeId] nvarchar(50) NOT NULL,
  [createdts] datetime2(0) DEFAULT GETDATE(),
  [modifiedts] datetime2(0) DEFAULT GETDATE(),
  [softdeleteflag] tinyint DEFAULT 0  NOT NULL,
  PRIMARY KEY ([organisationalUnitId],[organisationUnitTypeId]),
  CONSTRAINT [FK_organisationalunittypemapping_OrganisationalUnitId] FOREIGN KEY ([organisationalUnitId]) REFERENCES [${dbxschemaname}].[organisationunit] ([organisationalUnitId]) ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT [FK_organisationalunittypemapping_OrganisationUnitTypeId] FOREIGN KEY ([organisationUnitTypeId]) REFERENCES [${dbxschemaname}].[organisationalunittype] ([organisationUnitTypeId]) ON DELETE NO ACTION ON UPDATE NO ACTION

);
GO

ALTER TABLE [${dbxschemaname}].[customer] ALTER COLUMN [sbaEnrolmentStatus] VARCHAR(100) NULL ;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[financialinstitution_get_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[financialinstitution_get_proc] AS
BEGIN
	SELECT 
	[${dbxschemaname}].[financialinstitution].name as companyName,
	[${dbxschemaname}].[financialinstitution].shortName as shortName,
	[${dbxschemaname}].[financialinstitution].typeId as typeId,
	[${dbxschemaname}].[financialinstitution].parentId as parentId,
	[${dbxschemaname}].[financialinstitution].countryCode as countryCode,
	[${dbxschemaname}].[financialinstitution].baseCurrency as baseCurrency,
	[${dbxschemaname}].[financialinstitution].language as language,
	[${dbxschemaname}].[financialinstitution].effectiveDate as effectiveDate,
	[${dbxschemaname}].[financialinstitution].closeDate as closeDate,
	[${dbxschemaname}].[financialinstitutionaltkey].alternateKey as id
	FROM 
	[${dbxschemaname}].[financialinstitution] JOIN 
	[${dbxschemaname}].[financialinstitutionaltkey]
	on ([${dbxschemaname}].[financialinstitution].finInstitutionId = [${dbxschemaname}].[financialinstitutionaltkey].entityId)
	WHERE [${dbxschemaname}].[financialinstitution].softdeleteflag = 0 and [${dbxschemaname}].[financialinstitutionaltkey].softdeleteflag = 0;
END
GO



CREATE  PROCEDURE [${dbxschemaname}].[GetAllProductGroups_Campaign_proc]
AS
BEGIN
	
	select distinct [pg].[productGroupId], [pg].[productGroupName] from [productInformation] [pinf] 
	join [productGroup] [pg] on ([pinf].[productGroupRef] = [pg].[productGroupRef] )
	where [pinf].[purposeData] like '%Campaigns%';
END
GO


CREATE  PROCEDURE [${dbxschemaname}].[getProductsByProductGroup_proc](
@productGroups nvarchar(50)
)
AS
BEGIN
	select [pinf].[productId],[pinf].[productName], [pg].[productGroupId], [pg].[productGroupName] from [productInformation] [pinf] 
	join [productGroup] [pg] on ([pinf].[productGroupRef] = [pg].[productGroupRef] )
	where [pg].[productGroupId]  = @productGroups and [pinf].[purposeData] like '%Campaigns%';

END
GO


CREATE PROCEDURE [${dbxschemaname}].[create_campaign_proc](
@eventTriggerIdList nvarchar(max),
@profileIdList nvarchar(max),
@channelType nvarchar(max),
@offlineTemplate nvarchar(max),
@onlineContent nvarchar(max),
@channelDetails nvarchar(max),
@campaignId nvarchar(50),
@campaignName nvarchar(50),
@campaignDescription nvarchar(1000),
@campaignPriority int ,
@startDate nvarchar(50),
@endDate nvarchar(50),
@campaignType nvarchar(50),
@objectiveType nvarchar(50),
@productId nvarchar(50),
@productGroupId nvarchar(50),
@campaignStatus nvarchar(50)
)
AS
BEGIN
	SET  XACT_ABORT  ON

    SET  NOCOUNT  ON

	DECLARE @index int
    DECLARE @numOfRecords int
    DECLARE @recordsData nvarchar(max)
	DECLARE @campaignPriorityCur int = 0
	DECLARE @campaignIdcur nvarchar(50) = N''
	DECLARE @id nvarchar(max)
	DECLARE @profileId nvarchar(max)
	DECLARE @chnlType nvarchar(max)
	DECLARE @offlineTemplateId nvarchar(max)
	DECLARE @channelSubType nvarchar(max)
	DECLARE @subject nvarchar(max)
	DECLARE @messageContent nvarchar(max)
	DECLARE @onlineContentId nvarchar(max)
	DECLARE @placeholderId nvarchar(max)
	DECLARE @targetURL nvarchar(max)
	DECLARE @imageURL nvarchar(max)
	DECLARE @callToActionButtonLabel nvarchar(max)
	DECLARE @callToActionTargetURL nvarchar(max)
	DECLARE @showReadLaterButton nvarchar(max)
	DECLARE @showCloseIcon nvarchar(max)
	DECLARE @bannerTitle nvarchar(max)
	DECLARE @bannerDescription nvarchar(max)
	DECLARE  @imageIndex nvarchar(max) = N''
	DECLARE  @channelPriority nvarchar(max)
	DECLARE  @campPriority nvarchar(max)
	
	
	DECLARE
         @finished int = 0
    
	  INSERT INTO [${dbxschemaname}].[campaigndefinition]([campaignId],[campaignName],[campaignDescription],[objectiveType],[productId],[productGroupId],[campaignPriority],[campaignType],[campaignStatus],[startDate],[endDate] ) 
	  values (@campaignId, @campaignName,@campaignDescription,@objectiveType,@productId,@productGroupId,@campaignPriority,@campaignType,@campaignStatus,@startDate,@endDate);
      
	  set @index = 0;
	  set @numOfRecords = LEN(@eventTriggerIdList) - LEN(REPLACE(@eventTriggerIdList, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @eventTriggerIdList = ''
              BREAK
		  ELSE
          BEGIN
            set @id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@eventTriggerIdList, '|', @index), '|', -1 );
            INSERT INTO [${dbxschemaname}].[campaigneventtrigger]([campaignId],[eventTriggerId]) values (@campaignId, @id);
          END 
      END 

	  set @index = 0;
	  set @numOfRecords = LEN(@profileIdList) - LEN(REPLACE(@profileIdList, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @profileIdList = ''
              BREAK
		  ELSE
          BEGIN
            set @profileId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@profileIdList, '|', @index), '|', -1 );
            INSERT INTO [${dbxschemaname}].[campaignprofile]([campaignId],[profileId]) values (@campaignId, @profileId);
          END 
      END 
	
	  set @index = 0;
	  set @numOfRecords = LEN(@channelType) - LEN(REPLACE(@channelType, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @channelType = ''
              BREAK
		  ELSE
          BEGIN
            set @chnlType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@channelType, '|', @index), '|', -1 );
            INSERT INTO [${dbxschemaname}].[campaignchanneltype]([campaignId],[channelType]) values (@campaignId, @chnlType);
          END 
      END 
	  
      set @index = 0;
	  set @numOfRecords = LEN(@offlineTemplate) - LEN(REPLACE(@offlineTemplate, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @offlineTemplate = '' 
              BREAK
		  ELSE
          BEGIN
			set @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@offlineTemplate, '|', @index), '|', -1 );
			set @offlineTemplateId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 1), '$', -1 );
			set @channelSubType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 2), '$', -1 );
			set @subject = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 3), '$', -1 );
			set @messageContent = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 4), '$', -1 );
            INSERT INTO [${dbxschemaname}].[offlinetemplate]([offlineTemplateId],[campaignId],[channelSubType],[subject],[content]) values (@offlineTemplateId,@campaignId, @channelSubType,@subject,@messageContent);
           END
      END
     
	  set @index = 0;
	  set @numOfRecords = LEN(@onlineContent) - LEN(REPLACE(@onlineContent, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @onlineContent = ''
              BREAK
		  ELSE
          BEGIN
			set @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@onlineContent, '|', @index), '|', -1 );
			
			set @onlineContentId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 1), '$', -1 );
			set @placeholderId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 2), '$', -1 );
			set @targetURL = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 3), '$', -1 );
			set @imageURL = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 4), '$', -1 );
			set @callToActionButtonLabel = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 5), '$', -1 );
			set @callToActionTargetURL = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 6), '$', -1 );
			set @showReadLaterButton = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 7), '$', -1 );
			set @showCloseIcon = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 8), '$', -1 );
			set @bannerTitle = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 9), '$', -1 );
			set @bannerDescription = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 10), '$', -1 );
            INSERT INTO [${dbxschemaname}].[onlinecontent]([onlineContentId],[targetURL],[campaignId],[placeholderId],[imageURL],[imageIndex],[callToActionButtonLabel],[callToActionTargetURL],[showReadLaterButton],[showCloseIcon],[bannerTitle],[bannerDescription]) 
           	values 
          	(@onlineContentId, @targetURL,@campaignId,@placeholderId,@imageURL,@imageIndex,@callToActionButtonLabel,@callToActionTargetURL,@showReadLaterButton,@showCloseIcon,@bannerTitle,@bannerDescription);
           END
      END
     
	  set @index = 0;
	  set @numOfRecords = LEN(@channelDetails) - LEN(REPLACE(@channelDetails, '|', '')) + 1; 
      WHILE (1 = 1)
      BEGIN
          set @index = @index + 1;
		  IF @index = @numOfRecords + 1 or @channelDetails = ''
              BREAK
		  ELSE
          BEGIN
			set @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@channelDetails, '|', @index), '|', -1 );
			
			set @channelSubType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 1), '$', -1 );
			set @channelPriority = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '$', 2), '$', -1 );
			
            INSERT INTO [${dbxschemaname}].[campaignchanneldetails]([campaignId],[channelPriority],[channelSubType]) values (@campaignId,@channelPriority,@channelSubType);
           END
      END
 DECLARE higherCampaigns CURSOR LOCAL FOR 
             
SELECT
	campaigndefinition.campaignId,
	campaigndefinition.campaignPriority
FROM
	[${dbxschemaname}].[campaigndefinition]
where
	[${dbxschemaname}].[campaigndefinition].[campaignId] != @campaignId
	and [${dbxschemaname}].[campaigndefinition].[campaignPriority] >= @campaignPriority 
	order by campaigndefinition.campaignPriority 
             
set  @index = @campaignPriority;
 OPEN higherCampaigns
	  FETCH NEXT FROM higherCampaigns INTO @campaignIdcur,@campPriority
      WHILE (@@FETCH_STATUS=0)
			BEGIN
		if @campPriority = @index 
			begin
    		update [${dbxschemaname}].[campaigndefinition] set [${dbxschemaname}].[campaigndefinition].[campaignPriority] = @campPriority+1 where [${dbxschemaname}].[campaigndefinition].[campaignId] = @campaignIdcur;
    		set @index  = @index  + 1;
    		FETCH NEXT FROM higherCampaigns INTO @campaignIdcur,@campPriority
               CONTINUE
    		end
 		else
     		BREAK;
    	 
    END
CLOSE higherCampaigns
DEALLOCATE higherCampaigns
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_create_proc] @_matrixValues nvarchar(max),
                                                      @_approverIds nvarchar(max)
AS
BEGIN

    SET NOCOUNT ON

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
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);

    SET @length = LEN(@_matrixValues) - LEN(replace(@_matrixValues, ',', '')) + 1

    WHILE (1 = 1)
        BEGIN
            SET @index1 = @index1 + 1
            IF @index1 = @length + 1
                BREAK
            ELSE
                BEGIN
                    set @matrixRecord =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                    SET @matrixComma = replace(@matrixRecord, ';', ',');
                    SET @matrixComma = replace(@matrixComma, '"', '''');
                    set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 2), ',', -1);
                    set @contractId = REPLACE(@contractId, '''', '');
                    set @companyLegalUnit =
                            (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
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
                        approvalmatrix.currency.
						approvalmatrix.companyLegalUnit
                     ) VALUES (') + (@matrixComma) + (',''') + (@companyLegalUnit) + (''');')


                    execute (@query)
                    SET @id = @@IDENTITY

                    set @customerIds =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_approverIds, ',', @index1), ',', -1);
                    IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0)
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
                                            set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1);
                                            INSERT INTO [${dbxschemaname}].customerapprovalmatrix([${dbxschemaname}].customerapprovalmatrix.customerId, [${dbxschemaname}].customerapprovalmatrix.approvalMatrixId)
                                            VALUES (@customerId, @id)
                                            CONTINUE
                                        END
                                END
                        END
                    CONTINUE
                END
        END
END;
GO


USE [${dbxschemaname}]
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].approvalmatrix_default_create_proc(
    @_actionIds NVARCHAR(max),
    @_contractId NVARCHAR(50),
    @_accountIds NVARCHAR(max),
    @_cif NVARCHAR(50)
) AS
BEGIN
    DECLARE @accountList VARCHAR(max) = 0;
    DECLARE @limitTypeId_1 varchar(255) = 'DAILY_LIMIT';
    DECLARE @limitTypeId_2 varchar(255) = 'MAX_TRANSACTION_LIMIT';
    DECLARE @limitTypeId_3 varchar(255) = 'WEEKLY_LIMIT';
    DECLARE @accountIndex INTEGER = 0;
    DECLARE @actionIndex INTEGER = 0;
    DECLARE @typeId VARCHAR(max) = '';
    DECLARE @numOfAccounts int;
    DECLARE @numOfActions int;
    DECLARE @accountId NVARCHAR(50);
    DECLARE @actionId NVARCHAR(255);
    DECLARE @legalEntityId NVARCHAR(50);

    IF @_actionIds IS NULL OR @_actionIds = ''
        GOTO MAINLABEL$leave

    IF @_contractId IS NULL OR @_contractId = ''
        GOTO MAINLABEL$leave

    IF @_cif IS NULL OR @_cif = ''
        GOTO MAINLABEL$leave

    IF @_accountIds IS NULL OR @_accountIds = ''
        GOTO MAINLABEL$leave

    set @numOfAccounts = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
    set @numOfActions = LEN(@_actionIds) - LEN(REPLACE(@_actionIds, ',', '')) + 1;
    getAccount:
    WHILE 1 = 1 BEGIN
        set @accountIndex = @accountIndex + 1;
        IF @accountIndex = @numOfAccounts + 1
            BEGIN
                BREAK;
            End
        Else
            Begin

                set @accountId =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIndex), ',', -1);
                set @actionIndex = 0;
                getAction:
                WHILE 1 = 1 BEGIN
                    set @actionIndex = @actionIndex + 1;
                    IF @actionIndex = @numOfActions + 1
                        BEGIN
                            BREAK;
                        End
                    Else
                        Begin
                            set @actionId =
                                    [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_actionIds, ',', @actionIndex),
                                                            ',', -1);
                            SELECT @typeId = Type_id FROM featureaction WHERE id = @actionId;
                            SET @legalEntityId = (SELECT companyLegalUnit
                                                  from contractcorecustomers
                                                  where coreCustomerId = @_cif
                                                    and contractId = @_contractId);

                            IF @legalEntityId IS NULL OR @legalEntityId = ''
                                BEGIN
                                    SET @legalEntityId = 'ALL'
                                END

                            IF @typeId = 'MONETARY'
                                BEGIN
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_1, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_1, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_2, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_2, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                    INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                coreCustomerId, approvalruleId, companyLegalUnit)
                                    VALUES (@_contractId,
                                            concat(@actionId, '_', @accountId, '_', @limitTypeId_3, '_', @_contractId),
                                            @accountId, @actionId, @limitTypeId_3, @_cif, 'NO_APPROVAL',
                                            @legalEntityId);
                                END
                            ELSE
                                IF @typeId = 'NON_MONETARY'
                                    BEGIN
                                        INSERT INTO approvalmatrix (contractId, name, accountId, actionId, limitTypeId,
                                                                    coreCustomerId, approvalruleId, companyLegalUnit)
                                        VALUES (@_contractId,
                                                concat(@actionId, '_', @accountId, '_', 'NON_MONETARY_LIMIT', '_',
                                                       @_contractId), @accountId, @actionId, 'NON_MONETARY_LIMIT',
                                                @_cif, 'NO_APPROVAL', @legalEntityId);
                                    END
                        END
                END;
                set @accountList = CONCAT(@accountId, ',', @accountList);
            END
    END;

    SET @accountList = (select SUBSTRING(@accountList, 1, (LEN(@accountList) - 1)));
    select @accountList as accountList;

    MAINLABEL$leave:

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_delete_proc] @_contractId nvarchar(50),
                                                              @_cif nvarchar(50),
                                                              @_filterColumnIds nvarchar(max),
                                                              @_filterColumnName nvarchar(50)
AS
BEGIN

    DECLARE @companyLegalUnit NVARCHAR(50);
    set @companyLegalUnit = (SELECT companyLegalUnit from [${dbxschemaname}].contract WHERE [${dbxschemaname}].contract.id = @_contractId);

    SET XACT_ABORT ON
    SET NOCOUNT ON
    IF @_filterColumnName = 'actionId'
        begin
            DELETE
            FROM [${dbxschemaname}].approvalmatrix
            WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
              AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
              AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
              AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.actionId, @_filterColumnIds) <> 0
        end
    ELSE
        IF @_filterColumnName = 'accountId'
            BEGIN
                DELETE
                FROM [${dbxschemaname}].approvalmatrix
                WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
                  AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
                  AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
                  AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.accountId, @_filterColumnIds) <> 0
            END
        ELSE
            IF @_filterColumnName = 'cif'
                BEGIN
                    DELETE
                    FROM [${dbxschemaname}].approvalmatrix
                    WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
                      AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit
                      AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].approvalmatrix.coreCustomerId, @_filterColumnIds) <> 0
                END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_ids_checkncleanup_for_safedelete_proc]
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_ids_checkncleanup_for_safedelete_proc] (
    @_contractId        nvarchar(128),
    @_coreCustomerId    nvarchar(128),
    @_featureActionId    nvarchar(128),
    @_accountId          nvarchar(128)
)
AS BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @legalEntityId VARCHAR(50);
    DECLARE @actionLevel VARCHAR(50);
    DECLARE @matrixIdsSafeToDelete NVARCHAR(MAX);
    DECLARE @sqlStmt NVARCHAR(MAX);
    declare @singleQuote nvarchar(1);

    MAINEXEC: BEGIN
        SET @singleQuote = '''';
        SET @legalEntityId = 'ALL';
        -- Check whether the given featureActionId is an account level action or a customer level action
        SET @legalEntityId = (SELECT companyLegalUnit FROM contractcorecustomers
                              WHERE coreCustomerId = @_coreCustomerId AND contractId = @_contractId);

        SET  @actionLevel= (SELECT DISTINCT(actionlevelId) FROM featureaction WHERE id = @_featureActionId AND companyLegalUnit = @legalEntityId);

        IF @actionLevel = 'ACCOUNT_LEVEL' AND (@_accountId = '' OR @_accountId IS NULL)
            BEGIN
                --select '45000 - No Account ID passed for an account-level feature';
                RETURN;
            END ;

        SET @matrixIdsSafeToDelete = '';
        -- fetch all the approvalmatrix id's which are marked for soft delete, and no pending requests are associated with it.
        -- select all the unused or used matrix ids whose associated requests are not in Pending state and the matrix id is marked for soft delete. These are unused rules and can be deleted safely.
        IF @_accountId = '' OR @_accountId IS NULL
            BEGIN
                SET @matrixIdsSafeToDelete = ( SELECT String_agg(CAST(am.id AS nvarchar(max)), ',')
                                               FROM approvalmatrix AS am
                                                        LEFT JOIN requestapprovalmatrix AS ram ON am.id = ram.approvalMatrixId
                                                        LEFT JOIN bbrequest AS br ON br.requestId = ram.requestId
                                               WHERE am.softdeleteflag = 1
                                                 AND (br.status != 'Pending' OR br.status IS NULL)
                                                 AND am.contractId = @_contractId
                                                 AND am.coreCustomerId = @_coreCustomerId
                                                 AND am.actionId = @_featureActionId
                );
            END
        ELSE
            BEGIN
                SET @matrixIdsSafeToDelete = (SELECT String_agg(CAST(am.id AS nvarchar(max)), ',') FROM approvalmatrix AS am
                                                                                                            LEFT JOIN requestapprovalmatrix AS ram ON am.id = ram.approvalMatrixId
                                                                                                            LEFT JOIN bbrequest AS br ON br.requestId = ram.requestId
                                              WHERE am.softdeleteflag = 1
                                                AND (br.status != 'Pending' OR br.status IS NULL)
                                                AND am.contractId = @_contractId
                                                AND am.coreCustomerId = @_coreCustomerId
                                                AND am.actionId = @_featureActionId
                                                AND am.accountId =@_accountId);
            END;

        --Select @matrixIdsSafeToDelete as mistd;

        IF @matrixIdsSafeToDelete IS NOT NULL OR @matrixIdsSafeToDelete != ''
            BEGIN
                SET @matrixIdsSafeToDelete =  STRING_AGG(@matrixIdsSafeToDelete, ',');
                SET @matrixIdsSafeToDelete = REPLACE(@matrixIdsSafeToDelete, ',', @singleQuote+','+@singleQuote);
                -- proceed to delete rules from approvalmatrix, signatorygroupmatrix, and customerapprovalmatrix (signatorygroupmatrix || customerapprovalmatrix > requestapprovalmatrix > approvalmatrix : Order is to be maintained)
                SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].signatorygroupmatrix WHERE approvalMatrixId IN (',@singleQuote, @matrixIdsSafeToDelete,@singleQuote, ')');
                exec(@sqlStmt);

                SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].customerapprovalmatrix WHERE approvalMatrixId IN (', @singleQuote, @matrixIdsSafeToDelete,@singleQuote, ')');
                exec(@sqlStmt);

                SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE approvalMatrixId IN (', @singleQuote, @matrixIdsSafeToDelete,@singleQuote, ')');
                exec(@sqlStmt);

                SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].requestapprovalmatrix WHERE approvalMatrixId IN (', @singleQuote, @matrixIdsSafeToDelete,@singleQuote, ')');
                exec(@sqlStmt);

                SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].approvalmatrix WHERE id IN (', @singleQuote, @matrixIdsSafeToDelete,@singleQuote, ')');
                exec(@sqlStmt);
            END
    END
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_signatorygroupmatrixcreate_proc] @_matrixValues NVARCHAR(max),
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
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);
    set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;


    WHILE (1 = 1)
        BEGIN
            set @index1 = @index1 + 1;
            IF @index1 = @length + 1
                BREAK
            ELSE
                BEGIN
                    set @matrixRecord =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                    set @matrixComma = REPLACE(@matrixRecord, ';', ',');
                    set @matrixComma = REPLACE(@matrixComma, '"', '''');
                    set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 2), ',', -1);
                    set @contractId = REPLACE(@contractId, '''', '');
                    set @companyLegalUnit =
                            (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
                    set @query = concat(
                            'INSERT INTO [${dbxschemaname}].approvalmatrix(approvalmatrix.name,approvalmatrix.contractId,approvalmatrix.coreCustomerId,approvalmatrix.actionId,approvalmatrix.accountId,approvalmatrix.approvalruleId,approvalmatrix.isGroupMatrix,approvalmatrix.limitTypeId,approvalmatrix.lowerlimit,approvalmatrix.upperlimit,approvalmatrix.currency, approvalmatrix.companyLegalUnit) VALUES (',
                            @matrixComma, ',''', @companyLegalUnit, ''');');


                    execute (@query);
                    set @sigValues =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_signatorymatrixValues, '#', @index1), '#',
                                                    -1);
                    SET @id = @@IDENTITY;
                    SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1);
                    SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1);
                    set @id = REPLACE(@id, '"', '''');
                    set @groupList = REPLACE(@groupList, '"', '''');
                    set @groupRule = REPLACE(@groupRule, '"', '''');
                    set @query =
                                ('INSERT INTO [${dbxschemaname}].signatorygroupmatrix(signatorygroupmatrix.approvalMatrixId,signatorygroupmatrix.groupList,signatorygroupmatrix.groupRule, signatorygroupmatrix.companyLegalUnit) VALUES (') +
                                @id + ',' + '''' + @groupList + '''' + ',' + '''' + @groupRule + '''' + ')';
                    execute (@query);
                    CONTINUE
                END
        END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_update_softdeleteflag_proc] @_contractId nvarchar(50),
                                                                     @_cif nvarchar(50),
                                                                     @_accountIds nvarchar(max),
                                                                     @_actionId nvarchar(50),
                                                                     @_limitTypeId nvarchar(50)
AS
BEGIN

    DECLARE @companyLegalUnit NVARCHAR(50);
    SET @companyLegalUnit =
            (SELECT companyLegalUnit from [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.id = @_actionId);

    SET XACT_ABORT ON
    SET NOCOUNT ON
    UPDATE [${dbxschemaname}].approvalmatrix
    SET softdeleteflag = 1
    WHERE [${dbxschemaname}].approvalmatrix.contractId = @_contractId
      AND [${dbxschemaname}].approvalmatrix.coreCustomerId = @_cif
      AND [${dbxschemaname}].FIND_IN_SET(CAST(approvalmatrix.accountId AS nvarchar(max)), @_accountIds) > 0
      AND [${dbxschemaname}].approvalmatrix.actionId = @_actionId
      AND [${dbxschemaname}].approvalmatrix.limitTypeId = @_limitTypeId
      AND [${dbxschemaname}].approvalmatrix.companyLegalUnit = @companyLegalUnit

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrixtemplate_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
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
    DECLARE @companyLegalUnit nvarchar(50);
    DECLARE @contractId nvarchar(max);

    set @length = LEN(@_matrixValues) - LEN(REPLACE(@_matrixValues, ',', '')) + 1;
    getValues:
    WHILE 1 = 1 BEGIN
        set @index1 = @index1 + 1;
        IF @index1 = @length + 1
            BEGIN
                BREAK;
            end
        else
            begin
                set @matrixRecord =
                        [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixValues, ',', @index1), ',', -1);
                set @matrixComma = REPLACE(@matrixRecord, ';', ',');
                SET @matrixComma = replace(@matrixComma, '"', '''');
                set @contractId = [${dbxschemaname}].SUBSTRING_INDEX(@matrixComma, ',', 1);
                set @contractId = REPLACE(@contractId, '''', '');
                set @companyLegalUnit = (SELECT contract.companyLegalUnit from [${dbxschemaname}].contract where id = @contractId);
                set @query = concat(
                        'INSERT INTO [${dbxschemaname}].approvalmatrixtemplate(contractId,coreCustomerId,actionId,approvalruleId,limitTypeId,lowerlimit,upperlimit,currency,isGroupMatrix,companyLegalUnit) VALUES (',
                        @matrixComma, ',''', @companyLegalUnit, ''');');
                execute (@query);

                SET @id = @@IDENTITY

                IF @_isGroupMatrix = 0
                    BEGIN
                        set @customerIds =
                                [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, ',', @index1), ',',
                                                        -1);
                        IF (@customerIds IS NOT NULL AND LEN(@customerIds) > 0)
                            BEGIN
                                set @customerIdsComma = REPLACE(@customerIds, ';', ',');
                                set @length2 = LEN(@customerIdsComma) - LEN(REPLACE(@customerIdsComma, ',', '')) + 1;
                                set @index2 = 0;
                                getCustomerIds:
                                WHILE 1 = 1 BEGIN
                                    set @index2 = @index2 + 1;
                                    IF @index2 = @length2 + 1
                                        BEGIN
                                            BREAK;
                                        end
                                    else
                                        begin
                                            set @customerId = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@customerIdsComma, ',', @index2), ',', -1);
                                            INSERT INTO [${dbxschemaname}].customerapprovalmatrixtemplate(customerId, approvalMatrixId)
                                            values (@customerId, @id);
                                            CONTINUE
                                        END
                                END;
                            END
                        CONTINUE
                    END
                ELSE
                    IF @_isGroupMatrix = 1
                        BEGIN
                            set @sigValues =
                                    [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_matrixApprover, '#', @index1),
                                                            '#', -1);
                            SET @id = @@IDENTITY
                            SET @groupList = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', 1);
                            SET @groupRule = [${dbxschemaname}].SUBSTRING_INDEX(@sigValues, ';', -1);
                            INSERT INTO [${dbxschemaname}].signatorygroupmatrixtemplate(approvalMatrixId, groupList, groupRule)
                            values (@id, @groupList, @groupRule);
                        END
            END
    END
END;
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


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approve_pendingrequests_in_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approve_pendingrequests_in_approvalqueue_proc]
    @_requestMatrixDataJSON NVARCHAR(MAX),
    @_customerId VARCHAR(64)
AS
BEGIN
    DECLARE @noOfRequests INT
    DECLARE @requestIndex INT
    DECLARE @requestIds NVARCHAR(MAX)
    DECLARE @assocRequestIds NVARCHAR(MAX)
    DECLARE @requestJSON NVARCHAR(MAX)
    DECLARE @requestId VARCHAR(64)
    DECLARE @comments NVARCHAR(MAX)
    DECLARE @isGroupMatrix BIT
    DECLARE @assocRequestId VARCHAR(64)
    DECLARE @receivedSetsCount INT
    DECLARE @requiredSetsCount INT
    DECLARE @requestStatus NVARCHAR(50)
    DECLARE @companyId VARCHAR(64)
    DECLARE @companyLegalUnit NVARCHAR(100)
    DECLARE @matrixEntriesJSON NVARCHAR(MAX)
    DECLARE @noOfMatrixEntries INT
    DECLARE @matrixIndex INT
    DECLARE @matrixEntryJSON NVARCHAR(MAX)
    DECLARE @matrixId VARCHAR(64)
    DECLARE @receivedApprovals NVARCHAR(MAX)
    DECLARE @pendingGroupList NVARCHAR(MAX)
    DECLARE @groupRuleValue NVARCHAR(MAX)
    DECLARE @isApproved BIT
    DECLARE @actingGroupsCSV NVARCHAR(MAX)
    DECLARE @actingGroups NVARCHAR(MAX)
    DECLARE @sqlStmt NVARCHAR(MAX)

    SELECT @noOfRequests = COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON);--JSON_LENGTH(@requestMatrixDataJSON)
    SET @requestIndex = 0
    SET @requestIds = ''
    SET @assocRequestIds = ''

    WHILE @requestIndex < @noOfRequests
    BEGIN
        SET @requestJSON = JSON_QUERY(@_requestMatrixDataJSON, CONCAT('$[', @requestIndex, ']'))
        SET @requestIndex = @requestIndex + 1
        SET @requestId = JSON_VALUE(@requestJSON, '$.requestId')

        IF @requestIds = ''
            SET @requestIds = @requestId
        ELSE
            SET @requestIds = CONCAT(@requestIds, ',', @requestId)

        SET @comments = JSON_VALUE(@requestJSON, '$.comments')
        SET @isGroupMatrix = 0
        SET @assocRequestId = ''
        SET @receivedSetsCount = 0
        SET @requiredSetsCount = 0
        SET @requestStatus = ''
        SET @companyId = ''
        SET @companyLegalUnit = ''

        SELECT @assocRequestId = assocRequestId,
            @companyId = companyId,
            @isGroupMatrix = isGroupMatrix,
            @requiredSetsCount = requiredSets,
            @receivedSetsCount = receivedSets,
            @requestStatus = [status],
            @companyLegalUnit = companyLegalUnit
        FROM [${dbxschemaname}].bbrequest
        WHERE requestId = @requestId

        SET @matrixEntriesJSON = JSON_QUERY(@requestJSON, '$.matrixData')

        IF @matrixEntriesJSON IS NULL
            REturn; -- RETURN SQL SIGNAL

        IF @assocRequestIds = ''
            SET @assocRequestIds = @assocRequestId
        ELSE
            SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId)

        SELECT @noOfMatrixEntries = COUNT(*) FROM OPENJSON(@matrixEntriesJSON); --SET @noOfMatrixEntries = JSON_LENGTH(@matrixEntriesJSON)
        SET @matrixIndex = 0

        WHILE @matrixIndex < @noOfMatrixEntries
        BEGIN
            SET @matrixEntryJSON = JSON_QUERY(@matrixEntriesJSON, CONCAT('$[', @matrixIndex, ']'))
            SET @matrixIndex = @matrixIndex + 1
            SET @matrixId = JSON_VALUE(@matrixEntryJSON, '$.matrixId')
            SET @receivedApprovals = JSON_VALUE(@matrixEntryJSON, '$.receivedApprovals')
            SET @pendingGroupList = JSON_VALUE(@matrixEntryJSON, '$.pendingGroupList')
            SET @groupRuleValue = JSON_VALUE(@matrixEntryJSON, '$.groupRuleValue')
            SET @isApproved = JSON_VALUE(@matrixEntryJSON, '$.isApproved')

            IF @isApproved IS NULL
                SET @isApproved = 0
            ELSE
                SET @isApproved = CAST(@isApproved AS BIT)

            IF @isGroupMatrix = 1
            BEGIN
                IF @pendingGroupList IS NULL OR @groupRuleValue IS NULL
                    BREAK; -- RETURN SQL SIGNAL
                ELSE
                    UPDATE [${dbxschemaname}].signatorygrouprequestmatrix
                    SET pendingGroupList = @pendingGroupList,
                        groupRuleValue = @groupRuleValue,
                        isApproved = @isApproved
                    WHERE requestId = @requestId AND approvalMatrixId = @matrixId
                END
            END

            UPDATE [${dbxschemaname}].requestapprovalmatrix
            SET receivedApprovals = @receivedApprovals
            WHERE requestId = @requestId AND approvalMatrixId = @matrixId

            SET @receivedSetsCount = @receivedSetsCount + @isApproved
        END

        IF @receivedSetsCount >= @requiredSetsCount
            SET @requestStatus = 'Approved'

        UPDATE [${dbxschemaname}].bbrequest
        SET receivedSets = @receivedSetsCount,
            [status] = @requestStatus
        WHERE requestId = @requestId

        IF @isGroupMatrix = 1
        BEGIN
            SET @actingGroupsCSV = JSON_VALUE(@requestJSON, '$.actingGroupsCSV')

            IF @actingGroupsCSV IS NULL
                return; -- RETURN SQL SIGNAL
            ELSE
                SET @actingGroups = ''

                SET @sqlStmt = CONCAT('SELECT @actingGroups = STUFF((SELECT '','' + signatoryGroupName FROM signatorygroup WHERE signatoryGroupId IN (', REPLACE(@actingGroupsCSV, ',', '","'), ') FOR XML PATH('''')), 1, 1, '''')')

                EXEC sp_executesql @sqlStmt, N'@actingGroups NVARCHAR(MAX) OUTPUT', @actingGroups OUTPUT

                INSERT INTO [${dbxschemaname}].bbactedrequest (requestId, assocRequestId, companyId, [status], comments, createdby, groupName, [action], companyLegalUnit)
                VALUES (@requestId, @assocRequestId, @companyId, 'Approved', @comments, @_customerId, @actingGroups, 'Approved', @companyLegalUnit)
            END
        ELSE
        BEGIN
            INSERT INTO [${dbxschemaname}].bbactedrequest (requestId, assocRequestId, companyId, [status], comments, createdby, [action], companyLegalUnit)
            VALUES (@requestId, @assocRequestId, @companyId, 'Approved', @comments, @_customerId, 'Approved', @companyLegalUnit)
        END

        declare @_requestIds4fetch_requests_with_approvalmatrixinfo_proc nvarchar(4000);
        declare @_isAssociationId4fetch_requests_with_approvalmatrixinfo_proc nvarchar(1);
        declare @_contractCifMapJSON4fetch_requests_with_approvalmatrixinfo_proc nvarchar(1);
        declare @_isActiveRulesFetch4fetch_requests_with_approvalmatrixinfo_proc nvarchar(1);
        set @_requestIds4fetch_requests_with_approvalmatrixinfo_proc = @assocRequestIds;
        set @_isAssociationId4fetch_requests_with_approvalmatrixinfo_proc = '1';
        set @_contractCifMapJSON4fetch_requests_with_approvalmatrixinfo_proc = '';
        set @_isActiveRulesFetch4fetch_requests_with_approvalmatrixinfo_proc = '0';

        execute [${dbxschemaname}].fetch_requests_with_approvalmatrixinfo_proc
            @_requestIds4fetch_requests_with_approvalmatrixinfo_proc,
            @_isAssociationId4fetch_requests_with_approvalmatrixinfo_proc,
            @_contractCifMapJSON4fetch_requests_with_approvalmatrixinfo_proc,
            @_isActiveRulesFetch4fetch_requests_with_approvalmatrixinfo_proc
        ;
END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_approvalmode_for_contractandcif_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[update_approvalmode_for_contractandcif_proc] @_contractId VARCHAR(128),
                                                                   @_coreCustomerId VARCHAR(128),
                                                                   @_isGroupMatrix VARCHAR(2)
AS
BEGIN
    -- tasks
    -- 1. mark all rules in approvalmatrix for that contract and cif as soft deleted
    -- 2. delete the existing approvalmatrixtemplate entry for that contract and cif - first from customerapprovalmatrixtemplate and signatorygrouprequestmatrixtemplate, and then from approvalmatrixtemplate
    -- 3. call approvalmatrixtemplate_default_create_proc

    DECLARE @_isGroupLevel INT;
    DECLARE @_oldIsGroupMatrix INT;
    DECLARE @_oldCurrency NVARCHAR(MAX);
    DECLARE @_existingFeatureActionIds NVARCHAR(MAX);

    SET @_isGroupLevel =
            CASE WHEN @_isGroupMatrix IS NULL OR @_isGroupMatrix = '' THEN 0 ELSE CAST(@_isGroupMatrix AS INT) END;

    SET @_oldIsGroupMatrix = (SELECT CAST(isGroupLevel AS INT)
                              FROM approvalmode
                              WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);

    IF @_oldIsGroupMatrix = @_isGroupLevel
        BEGIN
            RETURN;
        END;

    SET @_oldCurrency = (SELECT DISTINCT currency
                         FROM approvalmatrixtemplate
                         WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
    --SET @_existingFeatureActionIds = (SELECT STRING_AGG(DISTINCT actionId, ',') FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
--DECLARE @existingFeatureActionIds NVARCHAR(MAX);

    SELECT @_existingFeatureActionIds = STUFF((SELECT DISTINCT ',' + CAST(actionId AS NVARCHAR(MAX))
                                               FROM approvalmatrixtemplate
                                               WHERE contractId = @_contractId
                                                 AND coreCustomerId = @_coreCustomerId
                                               FOR XML PATH(''), TYPE).value('.', 'NVARCHAR(MAX)'), 1, 1, '');


    UPDATE approvalmode
    SET isGroupLevel = @_isGroupLevel
    WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId;

    -- mark all rules in approvalmatrix for that contract and cif as soft deleted
    UPDATE approvalmatrix SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId;

    -- delete the existing approvalmatrixtemplate entry for that contract and cif - first from customerapprovalmatrixtemplate and signatorygrouprequestmatrixtemplate, and then from approvalmatrixtemplate
    DELETE
    FROM customerapprovalmatrixtemplate
    WHERE approvalMatrixId IN
          (SELECT id FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
    DELETE
    FROM signatorygroupmatrixtemplate
    WHERE approvalMatrixId IN
          (SELECT id FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
    DELETE FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId;

    -- call approvalmatrixtemplate_default_create_proc
    EXEC approvalmatrixtemplate_default_create_proc @_existingFeatureActionIds, @_contractId, @_coreCustomerId,
         @_isGroupMatrix, @_oldCurrency;
END;
GO




DROP PROCEDURE IF EXISTS [${dbxschemaname}].[approvalmatrix_default_init_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[approvalmatrix_default_init_proc] @_contractId VARCHAR(128), -- the contract for which the matrix is being initialized
                                                  @_coreCustomerIds nvarchar(max), -- list of cifs for the contract
                                                  @_featureActionIds nvarchar(max), -- list of featureActionIds to be created IN approvalmatrixtemplate
                                                  @_accountIds nvarchar(max), -- list of accounts for which approvalmatrix is to be initiated
                                                  @_isGroupRule VARCHAR(2), -- 0-user based, 1-group based
                                                  @_isDefaultDisabled VARCHAR(128), -- 0-enabled, 1-disabled
                                                  @_currency VARCHAR(64) -- currency
AS
BEGIN
    DECLARE @newMatrixId bigint;
    declare @newAccountIds nvarchar(max);
    declare @accountIdIndex int;
    declare @noOfAccountIds int;
    declare @noOfNewAccountIds int;
    declare @existingAccountIds nvarchar(max);
    DECLARE @existingFeatureActionIds VARCHAR(MAX);
    --DECLARE @DistinctValue VARCHAR(MAX);
    IF @_contractId IS NULL OR @_contractId = ''
        BEGIN
            -- TODO: return error
            RETURN
        END
    IF @_coreCustomerIds IS NULL OR @_coreCustomerIds = ''
        BEGIN
            -- TODO: return error
            RETURN
        END
    IF @_featureActionIds IS NULL OR @_featureActionIds = ''
        BEGIN
            -- TODO: return error
            RETURN
        END
    DECLARE @legalEntityId VARCHAR(128) = 'ALL'
    DECLARE @isGroupLevel INT
-- if _isGroupRule NOT passed, user-based approval mode is assumed
    SET @isGroupLevel = CASE
                            WHEN @_isGroupRule IS NULL OR @_isGroupRule = '' THEN 0
                            ELSE @_isGroupRule * 1
        END
    DECLARE @isMatrixDisabled INT
-- if _isDefaultDisabled is NOT passed, the approval matrix is assumed to be enabled
    SET @isMatrixDisabled = CASE
                                WHEN @_isDefaultDisabled IS NULL OR @_isDefaultDisabled = '' THEN 0
                                ELSE @_isDefaultDisabled * 1
        END
    DECLARE @cifIndex INT = 0
    DECLARE @numOfCifs INT
    SET @numOfCifs = LEN('_coreCustomerIds') - LEN(REPLACE('_coreCustomerIds', ',', '')) + 1
    WHILE @cifIndex < @numOfCifs
        BEGIN
            SET @cifIndex = @cifIndex + 1
            IF @cifIndex = @numOfCifs + 1
                BEGIN
                    BREAK
                END
            ELSE
                BEGIN
                    DECLARE @coreCustomerId VARCHAR(100)
                    SET @coreCustomerId =
                            [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_coreCustomerIds, ',', @cifIndex), ',',
                                                    -1);
                    SELECT @legalEntityId = companyLegalUnit
                    FROM contractcorecustomers
                    WHERE coreCustomerId = @coreCustomerId
                      AND contractId = @_contractId
                    IF @legalEntityId IS NULL
                        BEGIN
                            -- TODO: return error - No Legal Entity Found for the user
                            RETURN
                        END
                    -- if there is no existing active approvalmatrixtemplate entries for the given contract AND cif
                    IF NOT EXISTS (SELECT id
                                   FROM approvalmatrixtemplate
                                   WHERE contractId = @_contractId
                                     AND coreCustomerId = @coreCustomerId
                                     AND softdeleteflag = 0)
                        BEGIN
                            -- CLEAN-UP APPROVALMATRIX - perform a force-delete ON approvalmatrix for that contract AND cif combination
                            DELETE
                            FROM approvalmatrix
                            WHERE contractId = @_contractId AND coreCustomerId = @coreCustomerId
                            -- CLEAN-UP APPROVALMATRIXTEMPLATE - perform a force-delete ON approvalmatrixtemplate for that contract AND cif combination
                            -- STEP 1: first list out all the inactive rules ON approvalmatrixtemplate
                            DECLARE @inactiveTemplateRuleIds VARCHAR(MAX) = (SELECT STRING_AGG(id, ',')
                                                                             FROM approvalmatrixtemplate
                                                                             WHERE contractId = @_contractId
                                                                               AND coreCustomerId = @coreCustomerId
                                                                               AND softdeleteflag = 1)
                            IF @inactiveTemplateRuleIds IS NULL OR @inactiveTemplateRuleIds = ''
                                BEGIN
                                    DELETE
                                    FROM signatorygroupmatrixtemplate
                                    WHERE approvalMatrixId IN
                                          (SELECT value FROM STRING_SPLIT(@inactiveTemplateRuleIds, ','));
                                    DELETE
                                    FROM customerapprovalmatrixtemplate
                                    WHERE approvalMatrixId IN
                                          (SELECT value FROM STRING_SPLIT(@inactiveTemplateRuleIds, ','));
                                END
                            DELETE
                            FROM approvalmatrixtemplate
                            WHERE contractId = @_contractId AND coreCustomerId = @coreCustomerId AND softdeleteflag = 1
                            -- SET APPROVAL MODE - check for existing entry IN approvalmode table
                            IF EXISTS (SELECT id FROM approvalmode WHERE contractId = @_contractId
                                                                     AND coreCustomerId = @coreCustomerId)
                                BEGIN
                                    UPDATE approvalmode
                                    SET isGroupLevel = @isGroupLevel
                                    WHERE contractId = @_contractId AND coreCustomerId = @coreCustomerId
                                END
                            ELSE
                                BEGIN
                                    INSERT INTO approvalmode (id, coreCustomerId, contractId, isGroupLevel)
                                    VALUES (NEWID(), @coreCustomerId, @_contractId, @isGroupLevel)
                                END
                            -- SET APPROVAL MATRIX STATUS - enabled/disabled
                            IF EXISTS (SELECT contractId
                                       FROM manageapprovalmatrix
                                       WHERE contractId = @_contractId
                                         AND coreCustomerId = @coreCustomerId
                                         AND softdeleteflag = 0)
                                BEGIN
                                    UPDATE manageapprovalmatrix
                                    SET isDisabled = @isMatrixDisabled
                                    WHERE contractId = @_contractId
                                      AND coreCustomerId = @coreCustomerId
                                END
                            ELSE
                                BEGIN
                                    INSERT INTO manageapprovalmatrix (contractId, coreCustomerId, isDisabled)
                                    VALUES (@_contractId, @coreCustomerId, @isMatrixDisabled)
                                END
                            -- CREATE APPROVAL MATRIX TEMPLATE ENTRIES
                            EXEC approvalmatrixtemplate_default_create_proc @_featureActionIds, @_contractId,
                                 @coreCustomerId, @isGroupLevel, @_currency
                        END
                    ELSE
                        -- there exists some entry/entries IN approvalmatrixtemplate for that contract AND cif get the existing featureactionIds for which active rules are set IN the approvalmat
                        -- SELECT @existingFeatureActionIds = (STRING_AGG(DISTINCT actionId,',')) FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @coreCustomerId AND softdeleteflag = 0;
                        -- SET @existingFeatureActionIds = (SELECT STRING_AGG(DISTINCT actionId,',') FROM approvalmatrixtemplate WHERE contractId = @_contractId AND coreCustomerId = @coreCustomerId AND softdeleteflag = 0);

                        SELECT @existingFeatureActionIds = STRING_AGG(DistinctValue, ',')
                        from (select distinct actionId as DistinctValue
                              FROM [${dbxschemaname}].approvalmatrixtemplate
                              WHERE contractId = @_contractId
                                AND coreCustomerId = @coreCustomerId
                                AND softdeleteflag = 0) As Subquery;

                    -- iterate over the provided featureActionIds to search for newer featureActions
                    DECLARE @newFeatureActionIds VARCHAR(MAX) = '';
                    DECLARE @actionIdIndex INT = 0;
                    DECLARE @numOfActionIds INT = LEN(@_featureActionIds) - LEN(REPLACE(@_featureActionIds, ',', '')) + 1;
                    WHILE @actionIdIndex < @numOfActionIds
                        BEGIN
                            SET @actionIdIndex = @actionIdIndex + 1
                            IF @actionIdIndex = @numOfActionIds + 1
                                BEGIN
                                    BREAK
                                END
                            ELSE
                                BEGIN
                                    DECLARE @actionId VARCHAR(128) = [${dbxschemaname}].SUBSTRING_INDEX(
                                            [${dbxschemaname}].SUBSTRING_INDEX(@_featureActionIds, ',', @actionIdIndex), ',', -1);
                                    IF CHARINDEX(@actionId, @existingFeatureActionIds) = 0
                                        BEGIN
                                            -- the action id supplied does NOT exist IN the existing feature action ids
                                            IF @newFeatureActionIds = ''
                                                BEGIN
                                                    SET @newFeatureActionIds = @actionId
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @newFeatureActionIds = CONCAT(@newFeatureActionIds, ',', @actionId)
                                                END
                                        END
                                END
                        END
                    IF @newFeatureActionIds != ''
                        BEGIN
                            -- create approval matrix template entries for the newer feature actions added to the contract AND cif
                            EXEC approvalmatrixtemplate_default_create_proc @newFeatureActionIds, @_contractId,
                                 @coreCustomerId, @isGroupLevel, @_currency
                        END
                    -- check for existing active rule entries IN approvalmatrix for the contract AND cif
                    IF EXISTS (SELECT id
                               FROM approvalmatrix
                               WHERE contractId = @_contractId
                                 AND coreCustomerId = @coreCustomerId
                                 AND softdeleteflag = 0)
                        BEGIN
                            --SET @existingAccountIds = (SELECT dbo.GROUP_CONCAT(dbo.DISTINCT(accountId)) FROM approvalmatrix WHERE contractId = _contractId AND coreCustomerId = @coreCustomerId AND softdeleteflag = 0);
                            SELECT @existingAccountIds = STRING_AGG(DistinctValue, ',')
                                from (select distinct actionId as DistinctValue
                                FROM [${dbxschemaname}].approvalmatrix
                                    WHERE contractId = @_contractId
                                    AND coreCustomerId = @coreCustomerId
                                    AND softdeleteflag = 0) As Subquery;

                            SET @newAccountIds = '';
                            SET @accountIdIndex = 0;
                            SET @noOfAccountIds = LEN(@_accountIds) - LEN(REPLACE(@_accountIds, ',', '')) + 1;
                            SET @noOfNewAccountIds = 0;
                            WHILE @accountIdIndex < @noOfAccountIds
                                BEGIN
                                    SET @accountIdIndex = @accountIdIndex + 1
                                    IF @accountIdIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK
                                        END
                                    ELSE
                                        BEGIN
                                            DECLARE @accountId VARCHAR(128) = [${dbxschemaname}].SUBSTRING_INDEX(
                                                    [${dbxschemaname}].SUBSTRING_INDEX(@_accountIds, ',', @accountIdIndex), ',',
                                                    -1)
                                            IF CHARINDEX(@accountId, @existingAccountIds) = 0
                                                BEGIN
                                                    -- the account id supplied does NOT exist IN the existing account ids
                                                    SET @noOfNewAccountIds = @noOfNewAccountIds + 1
                                                    IF @newAccountIds = ''
                                                        BEGIN
                                                            SET @newAccountIds = @accountId
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            SET @newAccountIds = CONCAT(@newAccountIds, ',', @accountId)
                                                        END
                                                END
                                        END
                                END
                            IF @newAccountIds = ''
                                BEGIN
                                    RETURN
                                END
                            ELSE
                                BEGIN
                                    -- migrate rules FROM approvalmatrixtemplate for the new account ids as well
                                    DECLARE @nonDefaultMatrixTemplateIds VARCHAR(MAX) = ''
                                    -- find all the ids IN approvalmatrixtemplate for which the account-level action rules are set, AND migrate only those account level actions to the newer accounts
                                    SELECT @nonDefaultMatrixTemplateIds = STRING_AGG(DistinctValue, ',')
                                    from (SELECT distinct amt.id as DistinctValue
                                          FROM approvalmatrixtemplate AS amt
                                                   LEFT JOIN featureaction AS fa ON amt.actionId = fa.id
                                          WHERE amt.coreCustomerId = @coreCustomerId
                                            AND amt.contractId = @_contractId
                                            AND fa.actionlevelId = 'ACCOUNT_LEVEL'
                                            AND fa.companyLegalUnit = @legalEntityId
                                            AND amt.id NOT IN (SELECT id
                                                               FROM approvalmatrixtemplate
                                                               WHERE approvalruleId = 'NO_APPROVAL'
                                                                 AND lowerlimit = -1.0
                                                                 AND upperlimit = -1.0)) As Subquery;
                                    DECLARE @templateIdIndex INT = 0
                                    DECLARE @noOfTemplateIds INT = LEN(@nonDefaultMatrixTemplateIds) -
                                                                   LEN(REPLACE(@nonDefaultMatrixTemplateIds, ',', '')) +
                                                                   1
                                    IF @nonDefaultMatrixTemplateIds IS NOT NULL
                                        BEGIN
                                            WHILE @templateIdIndex < @noOfTemplateIds
                                                BEGIN
                                                    SET @templateIdIndex = @templateIdIndex + 1

                                                    IF @templateIdIndex = @noOfTemplateIds + 1
                                                        BEGIN
                                                            BREAK
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            DECLARE @templateId VARCHAR(128) = [${dbxschemaname}].SUBSTRING_INDEX(
                                                                    [${dbxschemaname}].SUBSTRING_INDEX(
                                                                            @nonDefaultMatrixTemplateIds, ',',
                                                                            @templateIdIndex), ',', -1)
                                                            -- for the template id, pick up the rule info FROM approvalmatrixtemplate, AND store IN approvalmatrix for that account
                                                            --    DECLARE @accountIdIndex INT = 0
                                                            WHILE @accountIdIndex < @noOfNewAccountIds
                                                                BEGIN
                                                                    SET @accountIdIndex = @accountIdIndex + 1
                                                                    IF @accountIdIndex = @noOfNewAccountIds + 1
                                                                        BEGIN
                                                                            BREAK
                                                                        END
                                                                    ELSE
                                                                        BEGIN
                                                                            DECLARE @newAccountId VARCHAR(128) = [${dbxschemaname}].SUBSTRING_INDEX(
                                                                                    [${dbxschemaname}].SUBSTRING_INDEX(@newAccountIds, ',', @accountIdIndex),
                                                                                    ',', -1)
                                                                            DECLARE @migIsGroupMatrix INT = 0
                                                                            -- approval matrix table migration
                                                                            SELECT @migIsGroupMatrix = isGroupMatrix
                                                                            FROM approvalmatrixtemplate
                                                                            WHERE id = @templateId
                                                                            -- transfer the rule (given the templateId) from approvalmatrixtemplate to approvalmatrix for the new account id
                                                                            INSERT INTO approvalmatrix (name,
                                                                                                        contractId,
                                                                                                        coreCustomerId,
                                                                                                        accountId,
                                                                                                        actionId,
                                                                                                        approvalruleId,
                                                                                                        isGroupMatrix,
                                                                                                        limitTypeId,
                                                                                                        lowerlimit,
                                                                                                        upperlimit,
                                                                                                        currency)
                                                                            SELECT CONCAT(contractId, '-', actionId) AS name,
                                                                                   amt.contractId,
                                                                                   amt.coreCustomerId,
                                                                                   @newAccountId                     AS accountId,
                                                                                   amt.actionId,
                                                                                   amt.approvalruleId,
                                                                                   amt.isGroupMatrix,
                                                                                   amt.limitTypeId,
                                                                                   amt.lowerlimit,
                                                                                   amt.upperlimit,
                                                                                   amt.currency
                                                                            FROM approvalmatrixtemplate AS amt
                                                                            WHERE amt.id = @templateId
                                                                            SET @newMatrixId = SCOPE_IDENTITY()
                                                                            IF @migIsGroupMatrix = 1
                                                                                BEGIN
                                                                                    -- use this matrix id to insert into signatorygroupmatrix, for group-based approval
                                                                                    INSERT INTO signatorygroupmatrix (approvalMatrixId, groupList, groupRule)
                                                                                    SELECT @newMatrixId AS approvalMatrixId,
                                                                                           smt.groupList,
                                                                                           smt.groupRule
                                                                                    FROM signatorygroupmatrixtemplate AS smt
                                                                                    WHERE smt.approvalMatrixId = @templateId
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    -- use this matrix id to insert into signatorygroupmatrix, for user-based approval
                                                                                    INSERT INTO customerapprovalmatrix (customerId, approvalMatrixId)
                                                                                    SELECT camt.customerId,
                                                                                           @newMatrixId AS approvalMatrixId
                                                                                    FROM customerapprovalmatrixtemplate AS camt
                                                                                    WHERE camt.approvalMatrixId = @templateId
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[create_approvalmatrixrule_for_featureaction_and_limittype_proc]
    @_contractId      NVARCHAR(128) ,
    @_coreCustomerId    NVARCHAR(128) ,
    @_featureActionId   NVARCHAR(128) ,
    @_accountIds       NVARCHAR(128) ,
    @_limitTypeId       NVARCHAR(128) ,
    @_limitValuesJSON   NVARCHAR(MAX)
AS
BEGIN
    DECLARE @accountIds NVARCHAR(max);
    DECLARE @isAccountLevelUpdate NVARCHAR(128);
    DECLARE @isAccountLevelFeature NVARCHAR(128);
    DECLARE @isGroupMatrix NVARCHAR(128);
    DECLARE @currency NVARCHAR(max);
    DECLARE @templateIdsForForceDelete NVARCHAR(max);
    DECLARE @templateIds NVARCHAR(max);
    DECLARE @sqlStmt NVARCHAR(max);
    DECLARE @limitsLength NVARCHAR(128);
    DECLARE @currentLimit NVARCHAR(128);
    DECLARE @limitIndex NVARCHAR(128);
    DECLARE @approvalRuleId NVARCHAR(128);
    DECLARE @lowerLimit NVARCHAR(128);
    DECLARE @upperLimit NVARCHAR(128);
    DECLARE @groupList NVARCHAR(max);
    DECLARE @groupRule NVARCHAR(max);
    DECLARE @approvers NVARCHAR(max);
    DECLARE @noOfApprovers NVARCHAR(128);
    DECLARE @currentUserId NVARCHAR(128);
    DECLARE @approverIndex NVARCHAR(128);
    DECLARE @noOfAccountIds NVARCHAR(128);
    DECLARE @lastTemplateId NVARCHAR(128);
    DECLARE @accountId NVARCHAR(128);
    DECLARE @accountIndex NVARCHAR(128);
    DECLARE @_requestMatrixDataJSON NVARCHAR(MAX);
    DECLARE @companyLegalUnit NVARCHAR(128);
    declare @singleQuote nvarchar(1);

    SET @singleQuote = '''';
    SET @accountIds = NULL;
    SET @isAccountLevelUpdate = 1;
    SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId and companyLegalUnit = @companyLegalUnit);
    --SET @isAccountLevelFeature = (SELECT DISTINCT([featureaction].[isAccountLevel]) FROM [${dbxschemaname}].[featureaction] WHERE id = @_featureActionId);
    SET @isGroupMatrix = (SELECT DISTINCT([approvalmode].[isGroupLevel]) FROM [${dbxschemaname}].[approvalmode] WHERE coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    --SET @companyLegalUnit = (SELECT DISTINCT([contractcustomers].[companyLegalUnit]) FROM [${dbxschemaname}].[contractcustomers] WHERE  coreCustomerId = @_coreCustomerId AND contractId = @_contractId);
    IF @_accountIds IS NULL OR @_accountIds = '' OR @_accountIds ='[]'
        BEGIN
            SET @isAccountLevelUpdate = 0;
            SET @accountIds = (SELECT STRING_AGG (CAST([contractaccounts].[accountId] AS NVARCHAR(max)),',') FROM [${dbxschemaname}].[contractaccounts] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId);
        END
    ELSE
        BEGIN
            SET @accountIds = @_accountIds;
        END
    IF @isAccountLevelFeature = 0
        BEGIN
            -- customer-level feature action rule is being added/updated, hence, no account ids are associated
            SET @accountIds = '';
        END
    -- if rule is not being set at account level, force delete all the rules in approvalmatrixtemplate and soft delete in approvalmatrix for all the account ids
    -- else, only mark for soft delete in approvalmatrix for the mentioned account ids
    SET @currency = (SELECT DISTINCT([approvalmatrixtemplate].[currency]) FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId and companyLegalUnit = @companyLegalUnit);
    IF @isAccountLevelUpdate = 0
        BEGIN
            -- collate all the template ids which are to be force-deleted from approvalmatrixtemplate, customerapprovalmatrixtemplate and signatorygroupmatrixtemplate
            -- delete all occurences of the template ids in customerapprovalmatrixtemplate and signatorygroupmatrixtemplate first, then delete from approvalmatrixtemplate
            SET @templateIdsForForceDelete = (SELECT STRING_AGG(CAST([approvalmatrixtemplate].[id] AS NVARCHAR(max)),',')  FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId);
            IF @templateIdsForForceDelete IS NOT NULL
                BEGIN
                    SET @templateIds = CONCAT('''', REPLACE(@templateIdsForForceDelete, ',', ''','''), '''');
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[customerapprovalmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    SET @sqlStmt = CONCAT('DELETE FROM [${dbxschemaname}].[signatorygroupmatrixtemplate] WHERE approvalMatrixId IN (', @templateIds, ')');
                    EXEC(@sqlStmt);
                    DELETE FROM [${dbxschemaname}].[approvalmatrixtemplate] WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
                END
        END
    -- mark all the rules in approvalmatrix for the given contract, coreCustomer, featureAction, limitType and accountIds for soft delete
    IF @isAccountLevelFeature = 0
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId;
        END
    ELSE
        BEGIN
            UPDATE [${dbxschemaname}].[approvalmatrix] SET softdeleteflag = 1 WHERE contractId = @_contractId AND coreCustomerId = @_coreCustomerId AND actionId = @_featureActionId AND isGroupMatrix = @isGroupMatrix AND limitTypeId = @_limitTypeId AND [${dbxschemaname}].FIND_IN_SET(accountId, @accountIds)<>0;
        END
    SET @limitsLength = (select COUNT(*) FROM OPENJSON(@_limitValuesJSON));
    --select 'LimitsLength' + @limitsLength;
    SET @currentLimit = NULL;
    SET @limitIndex = 0;
    WHILE 1=1
        BEGIN
            IF @limitIndex = @limitsLength
                BEGIN
                    BREAK;
                END
            ELSE
                BEGIN

                    SET @currentLimit = (SELECT json.value FROM OPENJSON(@_limitValuesJSON) as json where json.[key] =@limitIndex) ;
                    --SET @currentLimit = JSON_VALUE(@_limitValuesJSON, CONCAT('$[', @limitIndex, ']'));
                    --select 'Current Limit:  ' + @currentLimit;
                    SET @limitIndex = @limitIndex + 1;
                    --select '@currentLimit' + @currentLimit;
                    --SET @approvalRuleId = (select json.value from OPENJSON(@currentLimit) as json where json.[key] = 'groupRule');
                    SET @approvalRuleId = REPLACE(JSON_VALUE(@currentLimit, '$.approvalruleId'),'"','');
                    SET @lowerLimit = JSON_VALUE(@currentLimit, '$.lowerlimit');
                    SET @upperLimit = JSON_VALUE(@currentLimit, '$.upperlimit');
                    SET @groupList = REPLACE(JSON_VALUE(@currentLimit, '$.groupList'),'"','');
                    SET @groupRule = REPLACE(JSON_VALUE(@currentLimit, '$.groupRule'),'"','');
                    SET @approvalRuleId =  (SELECT DISTINCT([approvalrule].[id]) FROM [${dbxschemaname}].[approvalrule] WHERE numberOfApprovals =  REPLACE(REPLACE(@groupRule, '[', ''), ']', ''));
                    SET @approvers = JSON_VALUE(@currentLimit, '$.approvers');
                    -- error handling
                    -- if lowerLimit or upperLimit is null, then return an error signal
                    -- if group mode, then if groupList or groupRule is null, then return error signal
                    -- if user-based mode, then if approvalRuleId or approvers are null, then return error

                    IF @upperLimit = '-1.0' AND @lowerLimit = '-1.0' AND @isAccountLevelFeature = 1
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@groupList IS NOT NULL AND @groupList = '[]') AND (@groupRule IS NOT NULL AND @groupRule = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    IF (@approvers IS NOT NULL AND REPLACE(@approvers,'"','') = '[]')
                        BEGIN
                            SET @approvalRuleId = 'NO_APPROVAL';
                        END
                    -- update the rules in approvalmatrixtemplate if accountLevel update is not taking place, or the feature action is not an account level feature
                    IF @isAccountLevelUpdate = 0 OR @isAccountLevelFeature = 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].[approvalmatrixtemplate](contractId, coreCustomerId, actionId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency, companyLegalUnit)
                            VALUES(@_contractId, @_coreCustomerId, (@_featureActionId), @_limitTypeId, @isGroupMatrix, (@approvalRuleId), CAST(@lowerLimit AS DECIMAL(20,2)), CAST(@upperLimit AS DECIMAL(20,2)), @currency, @companyLegalUnit);
                            SET @lastTemplateId = (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrixtemplate] ) ;
                            IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                BEGIN
                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            INSERT INTO [${dbxschemaname}].[signatorygroupmatrixtemplate] (approvalMatrixId, groupList, groupRule)
                                            VALUES(@lastTemplateId, @groupList, @groupRule);
                                        END
                                    ELSE
                                        BEGIN
                                            SET @noOfApprovers = (select COUNT(*) FROM OPENJSON(@approvers));
                                            SET @currentUserId = '';
                                            SET @approverIndex = 0;
                                            WHILE (1=1)
                                                BEGIN
                                                    IF @approverIndex = @noOfApprovers
                                                        BEGIN
                                                            BREAK;
                                                        END
                                                    ELSE
                                                        BEGIN
                                                            SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                            INSERT INTO [${dbxschemaname}].[customerapprovalmatrixtemplate] (customerId, approvalMatrixId)
                                                            VALUES (@currentUserId, @lastTemplateId);
                                                            SET @approverIndex = @approverIndex + 1;
                                                        END
                                                END
                                        END
                                END
                        END
                    IF @isAccountLevelFeature = 0
                        BEGIN
                            exec ${dbxschemaname}.approvalmatrix_ids_checkncleanup_for_safedelete_proc
                                 _contractId,
                                 _coreCustomerId,
                                 _featureActionId,
                                 NULL;
                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                BEGIN
                                    INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId, limitTypeId, isGroupMatrix, approvalruleId, lowerLimit, upperLimit, currency,companyLegalUnit)
                                    VALUES (
                                               CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                               @_featureActionId, NULL, @_limitTypeId, @isGroupMatrix, (@approvalRuleId),
                                               CAST(@lowerLimit AS DECIMAL(20, 2)), CAST(@upperLimit AS DECIMAL(20, 2)), @currency,
                                               @companyLegalUnit);
                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                        BEGIN
                                            IF @isGroupMatrix = 1
                                                BEGIN
                                                    INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList, groupRule)
                                                    VALUES(@lastTemplateId, @groupList, (@groupRule));
                                                END
                                            ELSE
                                                BEGIN
                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@approvers));
                                                    SET @currentUserId = '';
                                                    SET @approverIndex = 0;
                                                    WHILE (1=1)
                                                        BEGIN
                                                            IF @approverIndex = @noOfApprovers
                                                                BEGIN
                                                                    BREAK;
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                    SET @approverIndex = @approverIndex + 1;
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                    ELSE
                        BEGIN
                            -- insert new rules in approvalmatrix, customerapprovalmatrix and signatoryapprovalmatrix for each account id
                            SET @noOfAccountIds = LEN(@accountIds) - LEN(REPLACE(@accountIds, ',', '')) + 1;
                            SET @accountIndex = 0;
                            WHILE (1=1)
                                BEGIN
                                    SET @accountIndex = @accountIndex + 1;
                                    IF @accountIndex = @noOfAccountIds + 1
                                        BEGIN
                                            BREAK;
                                        END
                                    ELSE
                                        BEGIN
                                            SET @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@accountIds, ',', @accountIndex), ',', -1);
                                            -- CALL `approvalmatrix_ids_checkncleanup_for_safedelete_proc`(`_contractId`, `_coreCustomerId`, `_featureActionId`, @accountId);
                                            -- if default rule - NO_APPROVAL -1 -1 then do not insert into approvalmatrix and corresponding signatorygroupmatrix or customerapprovalmatrix
                                            IF NOT(@approvalRuleId IS NOT NULL AND @approvalRuleId = 'NO_APPROVAL' AND @lowerLimit = -1 AND @upperLimit = -1)
                                                BEGIN
                                                    --select '@accountId'+@accountId;
                                                    BEGIN
                                                        INSERT INTO [${dbxschemaname}].[approvalmatrix](name, contractId, coreCustomerId, actionId, accountId,
                                                                                                        limitTypeId, isGroupMatrix, approvalruleId, lowerLimit,
                                                                                                        upperLimit, currency, companyLegalUnit)
                                                        VALUES (CONCAT(@_contractId, '-', @_featureActionId), @_contractId, @_coreCustomerId,
                                                                @_featureActionId, @accountId, @_limitTypeId, @isGroupMatrix,
                                                                @approvalRuleId, CAST(@lowerLimit AS DECIMAL(20, 2)),
                                                                CAST(@upperLimit AS DECIMAL(20, 2)), @currency, @companyLegalUnit);

                                                    END
                                                    --COMMIT;
                                                    SET @lastTemplateId =  (SELECT MAX(id) FROM [${dbxschemaname}].[approvalmatrix] );
                                                    --select '@lastTemplateId......' + @lastTemplateId;
                                                    IF @approvalRuleId != 'NO_APPROVAL' OR @approvalRuleId IS NULL
                                                        BEGIN
                                                            IF @isGroupMatrix = 1
                                                                BEGIN
                                                                    set @sqlStmt = CONCAT('INSERT INTO [${dbxschemaname}].[signatorygroupmatrix] (approvalMatrixId, groupList,groupRule)
										VALUES(',@singleQuote,@lastTemplateId,@singleQuote,',',@singleQuote, @groupList,@singleQuote, ',',@singleQuote,@groupRule,@singleQuote,')');
                                                                    --select '@sqlStmt' + @sqlStmt;
                                                                    EXEC(@sqlStmt);
                                                                END
                                                            ELSE
                                                                BEGIN
                                                                    SET @noOfApprovers = (SELECT COUNT(*) FROM OPENJSON(@_requestMatrixDataJSON));
                                                                    SET @currentUserId = '';
                                                                    SET @approverIndex = 0;
                                                                    WHILE (1=1)
                                                                        BEGIN
                                                                            IF @approverIndex = @noOfApprovers
                                                                                BEGIN
                                                                                    BREAK;
                                                                                END
                                                                            ELSE
                                                                                BEGIN
                                                                                    SET @currentUserId = REPLACE(JSON_VALUE(JSON_VALUE(@approvers, CONCAT('$[', @approverIndex,']')), '$.approverId'),'"','');
                                                                                    INSERT INTO [${dbxschemaname}].[customerapprovalmatrix] (customerId, approvalMatrixId)
                                                                                    VALUES (@currentUserId, @lastTemplateId);
                                                                                    SET @approverIndex = @approverIndex + 1;
                                                                                END
                                                                        END
                                                                END
                                                        END
                                                END
                                        END
                                END
                        END
                END
        END
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[create_new_composite_request_in_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[create_new_composite_request_in_approvalqueue_proc](
    @_contractCifMatrixIdsJSON nvarchar(4000),
    @_assocRequestId varchar(64),
    @_confirmationNumber varchar(128),
    @_featureActionId varchar(64),
    @_accountId varchar(45),
    @_createdBy varchar(32),
    @_comments varchar(512),
    @_additionalMetaJSON nvarchar(max)
)
AS
BEGIN
    DECLARE @requestIds varchar(MAX) = '';
    DECLARE @assocRequestId nvarchar(64);

    IF @_assocRequestId IS NULL OR @_assocRequestId = ''
        BEGIN
            SET @assocRequestId = NEWID();
        END
    ELSE
        BEGIN
            SET @assocRequestId = CAST(@_assocRequestId AS uniqueidentifier);
        END

--DECLARE @noOfContractIds int = JSON_LENGTH(@_contractCifMatrixIdsJSON);
    DECLARE @noOfContractIds int = 0;
    SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMatrixIdsJSON);

    DECLARE @contractIdIndex int = 0;

    WHILE @contractIdIndex < @noOfContractIds
        BEGIN
            --DECLARE @contractId nvarchar(50) = JSON_VALUE(@contractJSON, '$.contractId');
            DECLARE @contractJSON NVARCHAR(MAX);
            SELECT @contractJSON = [value]
            FROM (SELECT [value], ROW_NUMBER() OVER (ORDER BY [key]) AS RowNum
                  FROM OPENJSON(@_contractCifMatrixIdsJSON)) AS subquery
            WHERE RowNum = @contractIdIndex + 1;

            SET @contractIdIndex = @contractIdIndex + 1;
            --DECLARE @contractId nvarchar(50) = JSON_VALUE(@contractJSON, '$.contractId');

            DECLARE @contractId NVARCHAR(MAX);
            SELECT @contractId = value FROM OPENJSON(@contractJSON) WHERE [key] = 'contractId';

            IF @contractId IS NULL OR @contractId = ''
                BEGIN
                    -- return sql error signal
                    RETURN;
                END;

            --DECLARE @cifsJSON nvarchar(MAX) = JSON_VALUE(@contractJSON, '$.cifs');
            DECLARE @cifsJSON NVARCHAR(MAX);
            SELECT @cifsJSON = value FROM OPENJSON(@contractJSON) WHERE [key] = 'cifs';
            --DECLARE @noOfCifIds int = JSON_LENGTH(@cifsJSON);
            DECLARE @noOfCifIds int;
            SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);
            DECLARE @cifIndex int = 0;

            WHILE @cifIndex < @noOfCifIds
                BEGIN
                    --DECLARE @cifJSON nvarchar(MAX) = JSON_VALUE(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
                    DECLARE @cifJSON NVARCHAR(MAX);
                    SELECT @cifJSON = [value]
                    FROM (SELECT [value], ROW_NUMBER() OVER (ORDER BY [key]) AS RowNum
                          FROM OPENJSON(@cifsJSON)) AS subquery
                    WHERE RowNum = @cifIndex + 1;

                    SET @cifIndex += 1;

                    --DECLARE @cifId nvarchar(50) = JSON_VALUE(@cifJSON, '$.id');
                    DECLARE @cifId NVARCHAR(MAX);
                    SELECT @cifId = value FROM OPENJSON(@cifJSON) WHERE [key] = 'id';

                    IF @cifId IS NULL OR @cifId = ''
                        BEGIN
                            -- return sql error signal
                            RETURN;
                        END;

                    DECLARE @companyLegalUnit nvarchar(50);
                    DECLARE @isGroupMatrix bit;

                    --SELECT @companyLegalUnit = (DISTINCT (companyLegalUnit))
                    --FROM [${dbxschemaname}].contractcorecustomers
                    --WHERE contractId = @contractId AND coreCustomerId = @cifId;
                    SET @companyLegalUnit = (SELECT DISTINCT companyLegalUnit
                                             FROM [${dbxschemaname}].contractcorecustomers
                                             WHERE contractId = @contractId
                                               AND coreCustomerId = @cifId);

                    --SELECT @isGroupMatrix = DISTINCT isGroupLevel
                    --FROM approvalmode
                    --WHERE contractId = @contractId AND coreCustomerId = @cifId;
                    SET @isGroupMatrix = (SELECT DISTINCT isGroupLevel
                                          FROM [${dbxschemaname}].approvalmode
                                          WHERE contractId = @contractId
                                            AND coreCustomerId = @cifId);


                    --DECLARE @matrixIdsJSON nvarchar(MAX) = JSON_VALUE(@cifJSON, '$.matrixIds');
                    DECLARE @matrixIdsJSON NVARCHAR(MAX);
                    SELECT @matrixIdsJSON = value FROM OPENJSON(@cifJSON) WHERE [key] = 'matrixIds';
                    --DECLARE @noOfMatrixIds int = JSON_LENGTH(@matrixIdsJSON);
                    DECLARE @noOfMatrixIds int;
                    SELECT @noOfMatrixIds = COUNT(*) FROM OPENJSON(@matrixIdsJSON);
                    IF @noOfMatrixIds != 0
                        BEGIN
                            INSERT INTO [${dbxschemaname}].bbrequest
                            (assocRequestId, transactionId, featureActionId, createdby, companyId, requiredSets,
                             receivedSets, status, accountId, isGroupMatrix, additionalMeta, companyLegalUnit)
                            VALUES (@assocRequestId, @_confirmationNumber, @_featureActionId, @_createdBy,
                                    CONCAT(@contractId, '_', @cifId), @noOfMatrixIds, 0, 'Pending', @_accountId,
                                    @isGroupMatrix, @_additionalMetaJSON, @companyLegalUnit);

                            --DECLARE @requestId int = SCOPE_IDENTITY();
                            DECLARE @requestId INT;
                            SELECT TOP 1 @requestId = requestId
                            FROM bbrequest
                            WHERE assocRequestId = @assocRequestId
                            ORDER BY requestId DESC;

                            IF @requestIds = ''
                                SET @requestIds = CAST(@requestId AS varchar(50));
                            ELSE
                                SET @requestIds = CONCAT(@requestIds, ',', CAST(@requestId AS varchar(50)));

                            DECLARE @matrixIdIndex int = 0;

                            WHILE @matrixIdIndex < @noOfMatrixIds
                                BEGIN
                                    --                                     DECLARE @matrixId nvarchar(50) = JSON_VALUE(
--                                             JSON_VALUE(@matrixIdsJSON, CONCAT('$[', @matrixIdIndex, ']')), '$.id');
                                    DECLARE @matrixId NVARCHAR(MAX);

                                    SELECT @matrixId = value
                                    FROM OPENJSON(JSON_VALUE(@matrixIdsJSON, CONCAT('$[', @matrixIdIndex, ']')))
                                    WHERE [key] = 'id';

                                    SET @matrixIdIndex += 1;

                                    INSERT INTO [${dbxschemaname}].requestapprovalmatrix
                                    (approvalMatrixId, requestId, receivedApprovals, isGroupRule, createdby)
                                    VALUES (@matrixId, @requestId, 0, @isGroupMatrix, @_createdBy);

                                    IF @isGroupMatrix = 1
                                        BEGIN
                                            DECLARE @signatoryGroupRequestMatrixId nvarchar(50);
                                            set @signatoryGroupRequestMatrixId = newid();
                                            INSERT INTO [${dbxschemaname}].signatorygrouprequestmatrix
                                            (signatoryGroupRequestMatrixId, requestId, approvalMatrixId,
                                             groupList, groupRuleValue, pendingGroupList, createdby)
                                            SELECT NEWID(),
                                                   @requestId,
                                                   @matrixId,
                                                   sgm.groupList,
                                                   sgm.groupRule,
                                                   sgm.groupList,
                                                   @_createdBy
                                            FROM [${dbxschemaname}].signatorygroupmatrix AS sgm
                                            WHERE sgm.approvalMatrixId = @matrixId;
                                        END;
                                END;

                            INSERT INTO [${dbxschemaname}].bbactedrequest
                            (requestId, assocRequestId, companyId, [status],
                             comments, createdby, [action], companyLegalUnit)
                            VALUES (@requestId, @assocRequestId, CONCAT(@contractId, '_', @cifId),
                                    'Pending', @_comments, @_createdBy, 'Pending', @companyLegalUnit);
                        END;
                END;
        END;

DECLARE @temp_res TABLE
	(
    requestId           bigint,
    assocRequestId      varchar(64),
    transactionId       nvarchar(50),
    contractId          nvarchar(50),
    coreCustomerId      varchar(50),
    featureActionId     varchar(64),
    accountId           nvarchar(45),
    status              nvarchar(50),
    createdby           nvarchar(50),
    createdts           datetime,
    requiredSets        int,
    receivedSets        int,
    approvalMatrixId    bigint,
    limitTypeId         nvarchar(50),
    approvalruleId      nvarchar(50),
    receivedApprovals   int,
    additionalMeta      nvarchar(max),
    groupList           nvarchar(max),
    pendingGroupList    nvarchar(max),
    groupRuleValue      nvarchar(max),
    isGroupMatrix       int,
    isGroupRuleApproved int,
    approverIds         nvarchar(max),
    approverUserNames   nvarchar(max)
	);

    IF @requestIds = ''
        BEGIN
        
            select * from @temp_res;
        end
    else
        begin
    INSERT INTO @temp_res (
        requestId,
        assocRequestId,
        transactionId,
        contractId,
        coreCustomerId,
        featureActionId,
        accountId,
        status,
        createdby,
        createdts,
        requiredSets,
        receivedSets,
        approvalMatrixId,
        limitTypeId,
        approvalruleId,
        receivedApprovals,
        additionalMeta,
        groupList,
        pendingGroupList,
        groupRuleValue,
        isGroupMatrix,
        isGroupRuleApproved,
        approverIds,
        approverUserNames)
    EXEC [${dbxschemaname}].fetch_requests_with_approvalmatrixinfo_proc @requestIds, '0', '', '0';
            select * from @temp_res;
        end
        
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalmatrix_activerules_for_contractcifmap];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalmatrix_activerules_for_contractcifmap]
    @contractCifMapJSON NVARCHAR(MAX),
    @featureActionIds NVARCHAR(MAX)
AS
BEGIN
    -- parse contractCifMapJSON to fetch all contractId and cif ids
    DECLARE @justaQuote nvarchar(1) = '''';
    DECLARE @contractIds NVARCHAR(MAX);
    DECLARE @cifIds NVARCHAR(MAX);
    DECLARE @noOfContractIds NVARCHAR(MAX);
    DECLARE @contractIdIndex NVARCHAR(MAX);
    DECLARE @featureActionIdsFormatted NVARCHAR(MAX);
    DECLARE @sqlStmt NVARCHAR(MAX);
	DECLARE @indexJson NVARCHAR(MAX);
    SET @contractIds = '';
    SET @cifIds = '';
    --SET @noOfContractIds = JSON_LENGTH(@contractCifMapJSON);
    SET @noOfContractIds = (select COUNT(*) FROM OPENJSON(@contractCifMapJSON));
    SET @contractIdIndex = 0;
    SET @featureActionIdsFormatted = '''' + REPLACE(@featureActionIds, ',', ''',''') + '''';
    SET @sqlStmt = '';

    WHILE @contractIdIndex < @noOfContractIds
    BEGIN
        IF @contractIdIndex = @noOfContractIds
            BREAK;

        DECLARE @contractIdJSON NVARCHAR(MAX);
        DECLARE @contractId NVARCHAR(MAX);
        DECLARE @cifIdsJSON NVARCHAR(MAX);
        DECLARE @noOfCifIds NVARCHAR(MAX);
        DECLARE @cifIndex NVARCHAR(MAX);
		SET @contractIdJSON = (SELECT json.value FROM OPENJSON(@contractCifMapJSON) as json where json.[key] =@contractIdIndex) ;
		--SET @contractIdJSON = JSON_QUERY(@contractCifMapJSON, CONCAT('$[', @contractIdIndex, ']'));
		SET @contractIdIndex = @contractIdIndex + 1;

		SET @contractId =  (select json.value FROM OPENJSON(@contractIdJSON) as json where [key] = 'contractId' );

	   -- SET @contractId = JSON_VALUE(@contractIdJSON, '$.contractId');

        IF @contractId IS NULL OR @contractId = ''
        BEGIN
            -- return sql error signal
            BREAK;
        END;

        IF @contractIds = ''
            SET @contractIds = @contractId;
        ELSE
            SET @contractIds = CONCAT(@contractIds, ',', @contractId);

        SET @cifIdsJSON = (select json.value FROM OPENJSON(@contractIdJSON) as json where [key] = 'cifs' );
		--SET @noOfCifIds = JSON_LENGTH(@cifIdsJSON);
        SELECT @noOfCifIds = (select COUNT(*) FROM OPENJSON(@cifIdsJSON));
		--select '@noOfCifIds' + @noOfCifIds;
        SET @cifIndex = 0;

        WHILE @cifIndex < @noOfCifIds
        BEGIN
            IF @cifIndex = @noOfCifIds
                BREAK;

            DECLARE @cifId NVARCHAR(MAX);
            DECLARE @isGroupMatrix BIT;
            DECLARE @matrixFetchStmt NVARCHAR(MAX);
			set @indexJson = (select json.value FROM OPENJSON(@cifIdsJSON) as json where [key] = @cifIndex );
			set @cifId = (select json.value FROM OPENJSON(@indexJson) as json where [key] = 'id');
         --   SET @cifId = JSON_VALUE(JSON_QUERY(@cifIdsJSON, CONCAT('$[', @cifIndex, '].id')), '$');

		  SET @cifIndex = @cifIndex + 1;
            IF @cifId IS NULL OR @cifId = ''
            BEGIN
                -- return sql error signal
                BREAK;
            END;

            -- prepare statement for approval matrix fetch
            -- check from approvalmode to conditionally join customerapprovalmatrix or signatorygroupmatrix
            SET @matrixFetchStmt =
                CASE
                    WHEN (SELECT DISTINCT isGroupLevel FROM approvalmode WHERE contractId = @contractId AND coreCustomerId = @cifId) = 0 THEN
                        CONCAT('(SELECT
                                am.id AS matrixId,
                                am.contractId,
                                am.coreCustomerId,
                                am.accountId,
                                am.actionId,
                                am.limitTypeId,
                                CAST(am.isGroupMatrix AS INT) AS isGroupMatrix,
                                am.approvalruleId,
                                am.lowerlimit,
                                am.upperlimit,
                                NULL AS groupList,
                                NULL AS groupRule,
                                STRING_AGG(cam.customerId, '','') AS customerIds
                            FROM [${dbxschemaname}].approvalmatrix AS am
                            LEFT JOIN [${dbxschemaname}].customerapprovalmatrix AS cam ON am.id = cam.approvalMatrixId
                            WHERE am.contractId IN (',@justaQuote, @contractId,@justaQuote, ') AND am.coreCustomerId IN (',@justaQuote, @cifId,@justaQuote, ') AND am.actionId IN (', @featureActionIdsFormatted, ') AND am.softdeleteflag = 0
                            GROUP BY cam.approvalMatrixId)')

                    ELSE
                        CONCAT('(SELECT
                                am.id AS matrixId,
                                am.contractId,
                                am.coreCustomerId,
                                am.accountId,
                                am.actionId,
                                am.limitTypeId,
                                CAST(am.isGroupMatrix AS INT) AS isGroupMatrix,
                                am.approvalruleId,
                                am.lowerlimit,
                                am.upperlimit,
                                sgm.groupList,
                                sgm.groupRule,
                                NULL AS customerIds
                            FROM [${dbxschemaname}].approvalmatrix AS am
                            LEFT JOIN [${dbxschemaname}].signatorygroupmatrix AS sgm ON am.id = sgm.approvalMatrixId
                            WHERE am.contractId IN (',@justaQuote, @contractId,@justaQuote, ') AND am.coreCustomerId IN (',@justaQuote, @cifId,@justaQuote, ') AND am.actionId IN (', @featureActionIdsFormatted, ') AND am.softdeleteflag = 0)')

                END;

            IF @sqlStmt = ''
                SET @sqlStmt = @matrixFetchStmt;
            ELSE
                SET @sqlStmt = CONCAT(@sqlStmt, ' UNION ', @matrixFetchStmt);
        END;
    END;

    EXECUTE sp_executesql @sqlStmt;
END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_approvalqueue_proc] @_customerId nvarchar(50),
                                                    @_transactionIds nvarchar(max),
                                                    @_requestIds nvarchar(max),
                                                    @_featureactionlist nvarchar(max)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
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
	DECLARE @logmessage nvarchar(max)
	DECLARE @drop_temp nvarchar(100)

	SET @logmessage = 'Customer id = ' + @_customerId + ' Featurelist = ' +  @_featureactionlist + ' TransactionList = ' +  @_transactionIds + ' Requestids = ' + @_requestIds;
	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Incoming Arguemnts ' + @logmessage;

    SET @combinedIds = (select String_agg(customer.id, ',')
                        from [${dbxschemaname}].customer
                        where [${dbxschemaname}].customer.combinedUserId = @_customerId)
    IF @combinedIds is NULL
        SET @combinedIds = @_customerId;
    ELSE
        SET @combinedIds = (@_customerId + ',' + @combinedIds);

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

    SET @customerMatrixIds = (SELECT String_agg(CAST(customerapprovalmatrix.approvalMatrixId as nvarchar(max)), ',')
                              FROM [${dbxschemaname}].customerapprovalmatrix
                              WHERE customerapprovalmatrix.customerId IN
                                    (select VALUE from STRING_SPLIT(@combinedIds, ',')))
    IF @customerMatrixIds IS NULL
        SET @customerMatrixIds = ''

    SET @customerGroupIds = (SELECT String_agg(CAST(customersignatorygroup.signatoryGroupId as nvarchar(max)), ',')
                             FROM [${dbxschemaname}].customersignatorygroup
                             WHERE customersignatorygroup.customerId IN
                                   (select VALUE FROM STRING_SPLIT(@combinedIds, ',')));
    IF @customerGroupIds IS NULL
        SET @customerGroupIds = ''

    SET @alreadyApprovedIds = (SELECT String_agg(CAST(bbactedrequest.requestId as nvarchar(max)), ',')
                               FROM [${dbxschemaname}].bbactedrequest
                               WHERE bbactedrequest.createdby IN (SELECT VALUE FROM STRING_SPLIT(@combinedIds, ','))
                                 AND bbactedrequest.action != 'Pending'
                                 AND bbactedrequest.softdeleteflag = 0);

    IF @alreadyApprovedIds IS NULL
        SET @alreadyApprovedIds = '';

    IF @customerMatrixIds = ''
        SET @approvalRequestIds = '';
    ELSE
        SET @approvalRequestIds =
                (SELECT String_agg(CAST(RQAMX.requestId as nvarchar(max)), ',')
                 FROM [${dbxschemaname}].requestapprovalmatrix AS RQAMX
                          INNER JOIN [${dbxschemaname}].approvalmatrix AS APMX ON (RQAMX.approvalMatrixId = APMX.id and APMX.id IN
                                                                                                             (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ',')))
                          INNER JOIN [${dbxschemaname}].approvalrule AS APRL ON (APMX.approvalruleId = APRL.id)
                 WHERE RQAMX.isGroupRule = 0
                   AND RQAMX.approvalMatrixId IN (SELECT VALUE FROM STRING_SPLIT(@customerMatrixIds, ','))
                   AND RQAMX.requestId NOT IN (SELECT VALUE FROM STRING_SPLIT(@alreadyApprovedIds, ','))
                   AND ((APRL.numberOfApprovals = -1 AND
                         RQAMX.receivedApprovals < (SELECT COUNT(DISTINCT (customerapprovalmatrix.customerId))
                                                    FROM [${dbxschemaname}].customerapprovalmatrix
                                                    WHERE customerapprovalmatrix.approvalMatrixId = RQAMX.approvalMatrixId)) OR
													(APRL.numberOfApprovals != -1 AND RQAMX.receivedApprovals < APRL.numberOfApprovals)));

    IF @approvalRequestIds IS NULL
        SET @approvalRequestIds = '';

    -- GROUP BASED APPROVAL Trxns
    SET @groupIds = @customerGroupIds;

    DECLARE @CurrentGroupId NVARCHAR(MAX);
    DECLARE @PendingGroupList NVARCHAR(MAX);
    SET @PendingGroupList = '';

    do_this:
    WHILE 1 = 1 BEGIN
        SET @strLen = LEN(@groupIds);
        SET @CurrentGroupId = [${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1);
        IF @CurrentGroupId != ''
            BEGIN
                SET @CurrentGroupId = '%' + @CurrentGroupId + '%';

                IF @PendingGroupList = ''
                    SET @PendingGroupList =
                                ' signatorygrouprequestmatrix.pendingGroupList LIKE ''' + @CurrentGroupId + ''''
                ELSE
                    SET @PendingGroupList =
                                @PendingGroupList + ' OR signatorygrouprequestmatrix.pendingGroupList LIKE ''' +
                                @CurrentGroupId + ''''
            END

        SET @SubStrLen = LEN([${dbxschemaname}].SUBSTRING_INDEX(@groupIds, ',', 1));
        SET @groupIds = SUBSTRING(@groupIds, CAST(@SubStrLen as INT) + 2, CAST(@strLen as INT));
        IF LEN(@groupIds) <= 0
            BEGIN
                BREAK;
            END
    END;

    IF @PendingGroupList != ''
        BEGIN
            DECLARE @sql_stmt NVARCHAR(MAX);
            SET @sql_stmt = ' SELECT @reqIds_out = (String_agg(CAST(signatorygrouprequestmatrix.requestId as nvarchar(max)) , '',''))
            FROM [${dbxschemaname}].signatorygrouprequestmatrix WHERE signatorygrouprequestmatrix.isApproved = 0 AND ( ' +
                            @PendingGroupList + ' ) ;';
            EXECUTE sp_executesql @sql_stmt, N'@reqIds_out NVARCHAR(MAX) OUTPUT', @reqIds_out = @reqIds OUTPUT
        END

    IF @reqIds IS NULL
        SET @reqIds = ''

    IF (@approvalRequestIds = '' OR @approvalRequestIds IS NULL)
        SET @approvalRequestIds = @reqIds;
    ELSE
        BEGIN
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
            SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                             FROM [${dbxschemaname}].featureaction
                             WHERE featureaction.id IN (select value FROM STRING_SPLIT(@_featureactionlist, ',')))

            IF @features IS NULL
                SET @features = ''

            IF @features != ''
                SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
                                        FROM [${dbxschemaname}].featureaction
                                        WHERE featureaction.Feature_id IN (select value FROM STRING_SPLIT(@features, ',')))

            IF @monetaryActions IS NULL
                SET @monetaryActions = ''

            IF @monetaryActions != ''
                SET @query = ' bbrequest.transactionId IN ( select VALUE from STRING_SPLIT(''' + @_transactionIds + ''', '','')) AND
				               bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '','')); '
        END
    ELSE
        BEGIN
            IF @_requestIds = ''
                BEGIN

                    SET @companyId = (SELECT String_agg(CAST(concat(contractcustomers.contractId, '_', contractcustomers.coreCustomerId) as nvarchar(max)),',')
                                      FROM [${dbxschemaname}].contractcustomers
                                      WHERE contractcustomers.customerId = @_customerId)

                    IF @companyId IS NULL
                        SET @companyId = '';
                    ELSE
                        SET @companyId = [${dbxschemaname}].DISTINCT_VALUE(@companyId);

                    SET @features = (SELECT String_agg(CAST(featureaction.Feature_id as nvarchar(max)), ',')
                                     FROM [${dbxschemaname}].featureaction
                                     WHERE featureaction.id IN
                                           (select value FROM STRING_SPLIT(@_featureactionlist, ',')))

                    IF @features IS NULL
                        SET @features = ''

                    IF @features != ''
                        SET @monetaryActions = (SELECT String_agg(CAST(featureaction.id as nvarchar(max)), ',')
                                                FROM [${dbxschemaname}].featureaction
                                                WHERE featureaction.Feature_id IN
                                                      (select value FROM STRING_SPLIT(@features, ',')))

                    IF @monetaryActions IS NULL
                        SET @monetaryActions = ''

                    IF @companyId != '' AND @monetaryActions != ''
                        SET @query = ' bbrequest.companyId IN (SELECT value FROM STRING_SPLIT(''' + @companyId + ''', '','')) and
                                       bbrequest.featureActionId IN (select value FROM STRING_SPLIT(''' + @monetaryActions + ''', '','')); '
                END
            ELSE
                BEGIN
                    SET @requestIds = @_requestIds
                    IF @requestIds != ''
                        SET @query = ' bbrequest.requestId IN ( select VALUE from STRING_SPLIT(''' + @requestIds + ''', '','')); '
                END
        END

	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Query Stmt = ' + @query;
	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Combined Ids = ' + @combinedIds;
	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Approval Req = ' + @approvalRequestIds;
	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Already Approved Req = ' + @alreadyApprovedIds;

    DECLARE @select_statement nvarchar(max)
	DECLARE @req_ids NVARCHAR(MAX)

	IF @query != ''
		BEGIN
			SET @select_statement = 'select @reqout_ids = (string_agg(CAST(bbrequest.requestId as nvarchar(max)), '','')) from [${dbxschemaname}].bbrequest where ' + @query;
			EXECUTE sp_executesql @select_statement, N'@reqout_ids NVARCHAR(MAX) OUTPUT', @reqout_ids = @req_ids OUTPUT
		END
	ELSE
		GOTO MAINLABEL$leave

    IF @req_ids IS NULL
        GOTO MAINLABEL$leave

	--EXEC [dxdb].ProcedureLog @ProcedureName = 'fetch_approvalqueue_proc', @AdditionalInfo = 'Req_ids = ' + @req_ids;

-- Option 1 , Use TABLE variable

	DECLARE @temp_customeraction TABLE
	(
		id NVARCHAR(65),
		Account_id NVARCHAR(65),
		Action_id NVARCHAR(255),
		companyId NVARCHAR(255)
	);

	INSERT INTO @temp_customeraction (id, Account_id, Action_id, companyId)
	SELECT
		customeraction.id,
		customeraction.Account_id,
		customeraction.Action_id,
		(customeraction.contractId + '_' + customeraction.coreCustomerId) as companyId
	from [${dbxschemaname}].customeraction
	where
		customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(@combinedIds, ',')) and
		customeraction.isAllowed = 1 AND
		customeraction.softdeleteflag = 0;

-- Option 2, Use CTE

--  WITH temp_customeraction (id, Account_id, Action_id, companyId)
--	AS (
--		SELECT
--			customeraction.id,
--			customeraction.Account_id,
--			customeraction.Action_id,
--			(customeraction.contractId + ''_'' + customeraction.coreCustomerId) as companyId
--			from [${dbxschemaname}].customeraction
--		where
--			customeraction.Customer_id IN (select VALUE FROM STRING_SPLIT(''' + @combinedIds + ''', '','')) and
--			customeraction.isAllowed = 1 AND
--			customeraction.softdeleteflag = 0
--	)

	SELECT
		bbrequest.requestId,
		bbrequest.transactionId,
		bbrequest.status,
		bbrequest.featureActionId,
		bbrequest.isGroupMatrix,
		bbrequest.additionalMeta,

        (CASE
        WHEN (select COUNT(1) FROM STRING_SPLIT(@combinedIds, ',') where value = bbrequest.createdby) > 0
        THEN 'true'
        ELSE 'false'
        END) as amICreator,

        (CASE
        WHEN
        ((select COUNT(1) FROM STRING_SPLIT(@approvalRequestIds, ',') WHERE VALUE = bbrequest.requestId) > 0) AND
        ((select TOP 1 tca.id
		from @temp_customeraction tca -- Remove @ symbol, in case if we use CTE variable instead of TABLE variable here
		where
		tca.Account_id = bbrequest.accountId AND
		tca.Action_id = (select distinct(featureaction.approveFeatureAction)  from [${dbxschemaname}].featureaction where featureaction.id = bbrequest.featureActionId) AND
		tca.companyId = bbrequest.companyId) IS NOT NULL)
		THEN 'true'
		ELSE 'false'
		END) as amIApprover,

        (CASE
        WHEN (select COUNT(1) FROM STRING_SPLIT(@alreadyApprovedIds, ',') WHERE VALUE = bbrequest.requestId) > 0
        THEN 'true'
        ELSE 'false'
        END) as actedByMeAlready,

		(select count(DISTINCT(createdby)) from
		[${dbxschemaname}].bbactedrequest where bbactedrequest.action = 'Approved' and
		bbactedrequest.requestId = bbrequest.requestId AND bbactedrequest.softdeleteflag = 0)
		as receivedApprovals,

		null as requiredApprovals

	FROM
	[${dbxschemaname}].bbrequest WHERE
		bbrequest.requestId IN (SELECT value FROM STRING_SPLIT(@req_ids, ','));

End
    MAINLABEL$leave:
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_composite_approvalmatrixstatus_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_composite_approvalmatrixstatus_proc] @_contractCifMapJSON NVARCHAR(MAX)
AS
BEGIN
    DECLARE @contractIds NVARCHAR(MAX) = '';
    DECLARE @cifIds NVARCHAR(MAX) = '';
    --DECLARE @noOfContractIds INT = JSON_LENGTH(@_contractCifMapJSON);
    DECLARE @noOfContractIds int;
    SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMapJSON);
    DECLARE @contractIdIndex INT = 0;
    DECLARE @sqlStmt nvarchar(max);

    WHILE @contractIdIndex < @noOfContractIds
        BEGIN
--DECLARE @contractJSON NVARCHAR(MAX) = JSON_VALUE(@_contractCifMapJSON, CONCAT('$[', @contractIdIndex, ']'));
            DECLARE @contractJSON NVARCHAR(MAX);
            SELECT @contractJSON = [value] FROM (SELECT [value], ROW_NUMBER() OVER (ORDER BY [key]) AS RowNum FROM OPENJSON(@_contractCifMapJSON)) AS subquery WHERE RowNum = @contractIdIndex + 1;

            SET @contractIdIndex = @contractIdIndex + 1;

--DECLARE @contractId NVARCHAR(MAX) = JSON_VALUE(@contractJSON, '$.contractId');

            DECLARE @contractId NVARCHAR(MAX);
            SELECT @contractId = value FROM OPENJSON(@contractJSON) WHERE [key] = 'contractId';

            IF @contractId IS NULL OR @contractId = ''
                BEGIN
                    -- return sql error signal
                    RETURN;
                END;

            IF @contractIds = ''
                BEGIN
                    SET @contractIds = @contractId;
                END
            ELSE
                BEGIN
                    SET @contractIds = CONCAT(@contractIds, ',', @contractId);
                END;

                DECLARE @cifsJSON NVARCHAR(MAX);
                SELECT @cifsJSON = value FROM OPENJSON(@contractJSON) WHERE [key] = 'cifs';

            --DECLARE @noOfCifIds INT = JSON_LENGTH(@cifsJSON);
            DECLARE @noOfCifIds int;
            SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);
            DECLARE @cifIndex INT = 0;

            WHILE @cifIndex < @noOfCifIds
                BEGIN
--DECLARE @cifJSON NVARCHAR(MAX) = JSON_VALUE(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
                    DECLARE @cifJSON NVARCHAR(MAX);
                    SELECT @cifJSON = [value] FROM (SELECT [value], ROW_NUMBER() OVER
                        (ORDER BY [key]) AS RowNum FROM OPENJSON(@cifsJSON))
                        AS subquery WHERE RowNum = @cifIndex + 1;

                    SET @cifIndex = @cifIndex + 1;

                    --DECLARE @cifId NVARCHAR(MAX) = JSON_VALUE(@cifJSON, '$.id');
                    DECLARE @cifId NVARCHAR(MAX);
                    SELECT @cifId = value FROM OPENJSON(@cifJSON) WHERE [key] = 'id';

                    IF @cifId IS NULL OR @cifId = ''
                        BEGIN
                            -- return sql error signal
                            RETURN;
                        END;

                    IF @cifIds = ''
                        BEGIN
                            SET @cifIds = @cifId;
                        END
                    ELSE
                        BEGIN
                            SET @cifIds = CONCAT(@cifIds, ',', @cifId);
                        END;
                END;
        END;

    SET @contractIds = CONCAT('''', REPLACE(@contractIds, ',', ''','''), '''');
    SET @cifIds = CONCAT('''', REPLACE(@cifIds, ',', ''','''), '''');

    SET @sqlStmt =
      --Select
          (CONCAT('SELECT contractId, coreCustomerId, isDisabled FROM [${dbxschemaname}].manageapprovalmatrix WHERE contractId IN (',
                   @contractIds, ') AND coreCustomerId IN (', @cifIds, ')'));
    EXEC sp_executesql @sqlStmt;

END;
GO



DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_composite_approvalmode_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_composite_approvalmode_proc]
    @_contractCifMapJSON NVARCHAR(MAX)
AS
BEGIN
    DECLARE @contractIds NVARCHAR(MAX) = '';
    DECLARE @cifIds NVARCHAR(MAX) = '';
--    DECLARE @noOfContractIds INT = JSON_LENGTH(@_contractCifMapJSON);
    DECLARE @noOfContractIds int;
    SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMapJSON);

    DECLARE @contractIdIndex INT = 0;

    WHILE @contractIdIndex < @noOfContractIds
    BEGIN
        DECLARE @contractJSON NVARCHAR(MAX) = JSON_VALUE(@_contractCifMapJSON, CONCAT('$[', @contractIdIndex, ']'));
        SET @contractIdIndex = @contractIdIndex + 1;

        DECLARE @contractId NVARCHAR(MAX) = JSON_VALUE(@contractJSON, '$.contractId');

        IF @contractId IS NULL OR @contractId = ''
        BEGIN
            -- return sql error signal
            RETURN;
        END;

        IF @contractIds = ''
        BEGIN
            SET @contractIds = @contractId;
        END
        ELSE
        BEGIN
            SET @contractIds = CONCAT(@contractIds, ',', @contractId);
        END;

        DECLARE @cifsJSON NVARCHAR(MAX) = JSON_VALUE(@contractJSON, '$.cifs');
        --DECLARE @noOfCifIds INT = JSON_LENGTH(@cifsJSON);
        DECLARE @noOfCifIds int;
    SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);

        DECLARE @cifIndex INT = 0;

        WHILE @cifIndex < @noOfCifIds
        BEGIN
            DECLARE @cifJSON NVARCHAR(MAX) = JSON_VALUE(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
            SET @cifIndex = @cifIndex + 1;

            DECLARE @cifId NVARCHAR(MAX) = JSON_VALUE(@cifJSON, '$.id');

            IF @cifId IS NULL OR @cifId = ''
            BEGIN
                -- return sql error signal
                RETURN;
            END;

            IF @cifIds = ''
            BEGIN
                SET @cifIds = @cifId;
            END
            ELSE
            BEGIN
                SET @cifIds = CONCAT(@cifIds, ',', @cifId);
            END;
        END;
    END;

    SET @contractIds = CONCAT('''', REPLACE(@contractIds, ',', ''','''), '''');
    SET @cifIds = CONCAT('''', REPLACE(@cifIds, ',', ''','''), '''');

    DECLARE @sqlStmt NVARCHAR(MAX) = CONCAT('SELECT contractId, coreCustomerId, isGroupLevel FROM approvalmode WHERE contractId IN (', @contractIds, ') AND coreCustomerId IN (', @cifIds, ')');

    EXEC sp_executesql @sqlStmt;

END;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_pendingapprovers_for_request_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_pendingapprovers_for_request_proc]
    @_requestId nvarchar(max),
    @_isAssociationId varchar(max)
AS
BEGIN
    --INSERT INTO log123 (ProcName, data1, data2) VALUES ('fetch_pendingapprovers_for_request_proc', @requestId, @_isAssociationId);

    DECLARE @isAssocId int;
    SET @isAssocId = CASE WHEN @_isAssociationId IS NULL OR @_isAssociationId = '' THEN 0 ELSE CAST(@_isAssociationId AS int) END;

    DECLARE @requestIds nvarchar(4000);
    SET @requestIds = @_requestId;

    DECLARE @sqlStmt nvarchar(max);
    SET @sqlStmt = '';

    IF @isAssocId = 1
    BEGIN
        SET @requestIds = CONCAT('''', REPLACE(@requestIds, ',', ''','''), '''');
        SET @sqlStmt = CONCAT('SELECT @requestIds = STRING_AGG(DISTINCT requestId, '','') FROM bbrequest WHERE assocRequestId IN (', @requestIds, ')');
        EXEC sp_executesql @sqlStmt, N'@requestIds text OUTPUT', @requestIds = @requestIds OUTPUT;
    END

    IF @requestIds IS NULL
        SET @requestIds = '';

    DECLARE @noOfRequestIds int;
    SET @noOfRequestIds = LEN(@requestIds) - LEN(REPLACE(@requestIds, ',', '')) + 1;

    DECLARE @requestIdIndex int;
    SET @requestIdIndex = 1;

    SET @sqlStmt = '';

    REQUESTIDEXTRACT_INIT:
    WHILE @requestIdIndex <= @noOfRequestIds + 1
    BEGIN
        DECLARE @requestId nvarchar(4000);
        SET @requestId = SUBSTRING(@requestIds, CHARINDEX(',', @requestIds, 0) + 1, LEN(@requestIds));

        IF @requestIdIndex = @noOfRequestIds + 1
            BREAK;

        SET @requestIdIndex = @requestIdIndex + 1;

        DECLARE @isGroupMatrix int;
        SET @isGroupMatrix = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @requestId);

        DECLARE @actedUsers nvarchar(4000);
        SET @actedUsers = (SELECT STRING_AGG(createdby, ',') FROM bbactedrequest WHERE requestId = @requestId AND status != 'Pending');

        IF @actedUsers IS NULL
            SET @actedUsers = '';

        SET @actedUsers = CONCAT('''', REPLACE(@actedUsers, ',', ''','''), '''');

        DECLARE @sqlSubStmt nvarchar(max);

        IF @isGroupMatrix = 0
        BEGIN
            SET @sqlSubStmt = CONCAT('SELECT br.requestId, br.assocRequestId, br.transactionId, br.featureActionId, br.status, CAST(br.isGroupMatrix AS int) AS isGroupMatrix, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ar.name AS approvalruleName, ram.receivedApprovals, NULL AS groupList, NULL AS groupRule, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, NULL AS signatoryGroupId, NULL AS signatoryGroupName, JSON_QUERY(''[{"customerId":'' + CAST(cam.customerId AS nvarchar) + '',"userName":'' + c.UserName + '',"firstName":'' + c.FirstName + '',"lastName":'' + c.LastName + '',"role":'' + mg.Name + '',"userImage":'' + c.userImageURL + ''}]'') AS approversList
                FROM bbrequest AS br
                INNER JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                INNER JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId
                INNER JOIN customerapprovalmatrix AS cam ON ram.approvalMatrixId = cam.approvalMatrixId
                LEFT JOIN approvalrule AS ar ON am.approvalruleId = ar.id
                INNER JOIN customer AS c ON cam.customerId = c.id
                LEFT JOIN customergroup AS cg ON c.id = cg.Customer_id AND cg.contractId = am.contractId AND cg.coreCustomerId = am.coreCustomerId
                LEFT JOIN membergroup AS mg ON cg.Group_id = mg.id
                WHERE br.requestId = ''', @requestId, '''
                AND cam.customerId NOT IN (', @actedUsers, ')
                GROUP BY br.requestId, am.id)');
        END
        ELSE
        BEGIN
            SET @sqlSubStmt = CONCAT('SELECT br.requestId, br.assocRequestId, br.transactionId, br.featureActionId, br.status, CAST(br.isGroupMatrix AS int) AS isGroupMatrix, sgrm.approvalMatrixId, am.limitTypeId, am.approvalruleId, NULL AS approvalruleName, NULL AS receivedApprovals, sgrm.groupList, sgm.groupRule, sgrm.pendingGroupList, sgrm.groupRuleValue, CAST(sgrm.isApproved AS int) AS isGroupRuleApproved, sg.signatoryGroupId, sg.signatoryGroupName, NULL AS groupApproversList, JSON_QUERY(''[{"customerId":'' + CAST(csg.customerId AS nvarchar) + '',"userName":'' + c.UserName + '',"firstName":'' + c.FirstName + '',"lastName":'' + c.LastName + '',"role":'' + mg.Name + '',"userImage":'' + c.userImageURL + ''}]'') AS approversList
                FROM bbrequest AS br
                INNER JOIN signatorygrouprequestmatrix AS sgrm ON br.requestId = sgrm.requestId
                INNER JOIN approvalmatrix AS am ON am.id = sgrm.approvalMatrixId
                INNER JOIN signatorygroupmatrix AS sgm ON am.id = sgm.approvalMatrixId
                INNER JOIN signatorygroup AS sg ON CHARINDEX(CAST(sg.signatoryGroupId AS nvarchar), REPLACE(REPLACE(sgrm.pendingGroupList, ''['', ''), '']'', '')) > 0
                INNER JOIN customersignatorygroup AS csg ON sg.signatoryGroupId = csg.signatoryGroupId
                INNER JOIN customer AS c ON csg.customerId = c.id
                LEFT JOIN customergroup AS cg ON c.id = cg.Customer_id AND cg.contractId = am.contractId AND cg.coreCustomerId = am.coreCustomerId
                LEFT JOIN membergroup AS mg ON cg.Group_id = mg.id
                WHERE br.requestId = ''', @requestId, '''
                AND csg.customerId NOT IN (', @actedUsers, ')
                GROUP BY br.requestId, am.id, sg.signatoryGroupId)');
        END

        IF @sqlStmt = ''
            SET @sqlStmt = @sqlSubStmt;
        ELSE
            SET @sqlStmt = CONCAT(@sqlStmt, 'UNION ALL', @sqlSubStmt);
    END;

    EXEC sp_executesql @sqlStmt;

END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_requests_with_approvalmatrixinfo_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_requests_with_approvalmatrixinfo_proc]
	@_requestIds NVARCHAR(MAX),
	@_isAssociationId NVARCHAR(2),
	@_contractCifMapJSON NVARCHAR(MAX),
	@_isActiveRulesFetch NVARCHAR(2)
AS
BEGIN
	SET NOCOUNT ON;
    DECLARE @currentSchema NVARCHAR(64) = OBJECT_SCHEMA_NAME(@@PROCID);
	DECLARE @isAssocId BIT;
	DECLARE @isActiveRulesFetch BIT;
	DECLARE @requestIds NVARCHAR(MAX);
	DECLARE @sqlStmt NVARCHAR(MAX);
	DECLARE @isCifLevelFilter BIT;
	DECLARE @contractIds NVARCHAR(MAX);
	DECLARE @cifIds NVARCHAR(MAX);
	DECLARE @noOfContractIds INT;
	DECLARE @contractIdIndex INT;
	DECLARE @contractJSON NVARCHAR(MAX);
	DECLARE @contractId NVARCHAR(MAX);
	DECLARE @cifsJSON NVARCHAR(MAX);
	DECLARE @noOfCifIds INT;
	DECLARE @cifIndex INT;
	DECLARE @cifJSON NVARCHAR(MAX);
	DECLARE @cifId NVARCHAR(MAX);
	DECLARE @requestId NVARCHAR(MAX);
	DECLARE @isGroupMatrix BIT;
	DECLARE @sqlSubStmt NVARCHAR(MAX);
	DECLARE @noOfRequestIds INT;
	DECLARE @requestIdIndex INT;
	DECLARE @singleQuote nvarchar(1);
    SET @singleQuote = '''';

	IF @_isAssociationId IS NULL OR @_isAssociationId = ''
		SET @isAssocId = 0;
	ELSE
		SET @isAssocId = CAST(@_isAssociationId AS INT);
	IF @_isActiveRulesFetch IS NULL OR @_isActiveRulesFetch = ''
		SET @isActiveRulesFetch = 0;
	ELSE
		SET @isActiveRulesFetch = CAST(@_isActiveRulesFetch AS BIT);
	SET @requestIds = @_requestIds;

	IF @isAssocId = 1
	BEGIN
		SET @requestIds = CONCAT('''', REPLACE(@_requestIds, ',', ''','''), '''');
		SET @sqlStmt = CONCAT('SELECT STRING_AGG(DISTINCT requestId, '','') WITHIN GROUP (ORDER BY requestId) INTO @requestIds FROM bbrequest WHERE assocRequestId IN (', @requestIds, ')');
		EXEC (@sqlStmt);
	END;

	IF @requestIds IS NULL
	BEGIN
		SET @requestIds = '';
	END;

	SET @isCifLevelFilter = 1;
	SET @contractIds = '';
	SET @cifIds = '';

	IF @_contractCifMapJSON IS NULL OR @_contractCifMapJSON = ''
	BEGIN
		SET @isCifLevelFilter = 0;
	END
	ELSE
	BEGIN
		SELECT @noOfContractIds = COUNT(*) FROM OPENJSON(@_contractCifMapJSON);
		SET @contractIdIndex = 0;
		WHILE @contractIdIndex < @noOfContractIds
		BEGIN
-- 			SELECT @contractJSON = [value] FROM OPENJSON(@_contractCifMapJSON) WITH (Value NVARCHAR(MAX) '$[' + CAST(@contractIdIndex AS NVARCHAR(10)) + ']');
--SET @contractJSON = JSON_QUERY(_contractCifMapJSON, CONCAT('$[', @contractIdIndex, ']'));
SELECT @contractJSON = [value]
FROM OPENJSON(@_contractCifMapJSON)
WHERE [key] = @contractIdIndex;
			SET @contractIdIndex = @contractIdIndex + 1;

			--SELECT @contractId = value FROM OPENJSON(@contractJSON) WITH (contractId NVARCHAR(MAX) '$.contractId');
            SET @contractId = JSON_VALUE(@contractJSON, '$.contractId');

            IF @contractId IS NULL OR @contractId = ''
			BEGIN
				-- return error signal
				RETURN;
			END;

			IF @contractIds = ''
			BEGIN
				SET @contractIds = @contractId;
			END
			ELSE
			BEGIN
				SET @contractIds = CONCAT(@contractIds, ',', @contractId);
			END;
			--SELECT @cifsJSON = [value] FROM OPENJSON(@contractJSON) WITH (cifs NVARCHAR(MAX) '$.cifs');
			--DECLARE @cifsJSON NVARCHAR(MAX);
            SET @cifsJSON = JSON_QUERY(@contractJSON, '$.cifs');

            SELECT @noOfCifIds = COUNT(*) FROM OPENJSON(@cifsJSON);
			SET @cifIndex = 0;

			WHILE @cifIndex < @noOfCifIds
			BEGIN
				SET @cifJSON = JSON_QUERY(@cifsJSON, CONCAT('$[', @cifIndex, ']'));
				SELECT @cifJSON = [value] FROM OPENJSON(@cifsJSON) WHERE [key] = @cifIndex;
				SET @cifIndex = @cifIndex + 1;
				--SELECT @contractId = value FROM OPENJSON(@cifJSON) WITH (contractId NVARCHAR(MAX) '$.id');
				--DECLARE @cifId NVARCHAR(MAX);
                SET @cifId = JSON_VALUE(@cifJSON, '$.id');

				IF @cifId IS NULL OR @cifId = ''
				BEGIN
					-- return error signal
					RETURN;
				END;

				IF @cifIds = ''
				BEGIN
					SET @cifIds = @cifId;
				END
				ELSE
				BEGIN
					SET @cifIds = CONCAT(@cifIds, ',', @cifId);
				END;
			END;
		END;

		SET @contractIds = CONCAT('''', REPLACE(@contractIds, ',', ''','''), '''');
		SET @cifIds = CONCAT('''', REPLACE(@cifIds, ',', ''','''), '''');
	END;

	SET @sqlStmt = '';
	SET @noOfRequestIds = LEN(@requestIds) - LEN(REPLACE(@requestIds, ',', '')) + 1;
	SET @requestIdIndex = 1;

	WHILE @requestIdIndex <= @noOfRequestIds
	BEGIN
		SET @requestId = SUBSTRING(@requestIds, CHARINDEX(',', @requestIds, 1) + 1, LEN(@requestIds));

		IF @requestIdIndex = @noOfRequestIds
		BEGIN
			SET @requestId = @requestIds;
		END;

		SET @requestIdIndex = @requestIdIndex + 1;
		SET @isGroupMatrix = (SELECT DISTINCT isGroupMatrix FROM bbrequest WHERE requestId = @requestId);
		SET @sqlSubStmt = '';

		IF @isGroupMatrix = 0
		BEGIN
			SET @sqlSubStmt = CONCAT('(SELECT br.requestId, br.assocRequestId, br.transactionId, am.contractId, am.coreCustomerId, br.featureActionId, br.accountId, br.status, br.createdby, br.createdts, br.requiredSets, br.receivedSets, ram.approvalMatrixId, am.limitTypeId, am.approvalruleId, ram.receivedApprovals, CAST(br.isGroupMatrix AS UNSIGNED) AS isGroupMatrix, NULL AS groupList, NULL AS pendingGroupList, NULL AS groupRuleValue, NULL AS isGroupRuleApproved, GROUP_CONCAT(cam.customerId) AS approverIds, br.additionalMeta,',
                        ' (SELECT GROUP_CONCAT(' , @singleQuote , '{"','cam.customerId : c.userName','}',') FROM customer AS c WHERE c.id = cam.customerId) AS approverUserNames
                            FROM bbrequest AS br LEFT JOIN requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                            LEFT JOIN customerapprovalmatrix AS cam ON cam.approvalMatrixId = ram.approvalMatrixId LEFT JOIN approvalmatrix AS am ON am.id = ram.approvalMatrixId WHERE br.requestId = ',@singleQuote, '@requestId', @singleQuote,
                        'IF(@isCifLevelFilter = 1, CONCAT(', @singleQuote, 'AND am.contractId IN (', '@contractIds', ') AND am.coreCustomerId IN (', '@cifIds', '))', ')', ' GROUP BY br.requestId, ram.approvalMatrixId)');
		END
		ELSE
		BEGIN
			SET @sqlSubStmt = concat('
    SELECT
        br.requestId,
        br.assocRequestId,
        br.transactionId,
        am.contractId,
        am.coreCustomerId,
        br.featureActionId,
        br.accountId,
        br.status,
        br.createdby,
        br.createdts,
        br.requiredSets,
        br.receivedSets,
        ram.approvalMatrixId,
        am.limitTypeId,
        am.approvalruleId,
        ram.receivedApprovals,
        br.additionalMeta,
        sgrm.groupList,
        sgrm.pendingGroupList,
        sgrm.groupRuleValue,
        CAST(br.isGroupMatrix AS INT) AS isGroupMatrix,
        CAST(sgrm.isApproved AS INT) AS isGroupRuleApproved,' +
                                             '(SELECT STUFF((SELECT '','' + CAST(cust.customerid AS VARCHAR)
                                                 FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                 WHERE CHARINDEX('','' + CAST(cust.signatoryGroupId AS VARCHAR) + '','', (REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', ''''))) > 0
                                                 FOR XML PATH('''')), 1, 1, '''')
                                                 ) AS approverIds,',
                                             '(SELECT STUFF((SELECT '','' + ''{"'' + CAST(cust.customerid AS VARCHAR) + ''":"'' + c.userName + ''"}''
                                                    FROM ',@currentSchema, '.customersignatorygroup AS cust
                                                    INNER JOIN ' ,@currentSchema,'.customer AS c ON c.id = cust.customerid
                                                    WHERE CHARINDEX(cust.signatoryGroupId, REPLACE(REPLACE(REPLACE(sgrm.pendingGroupList, '']'', ''''), ''['', ''''), ''"'', '''')) > 0
                                                    FOR XML PATH('''')), 1, 1, ''''))
                                                  AS approverUserNames ',
                                             'FROM ' ,@currentSchema,'. bbrequest AS br  ',
                                             'LEFT JOIN ' ,@currentSchema,'.requestapprovalmatrix AS ram ON br.requestId = ram.requestId
                                             LEFT JOIN  ' ,@currentSchema,'.signatorygrouprequestmatrix AS sgrm ON (ram.approvalMatrixId = sgrm.approvalMatrixId AND sgrm.requestId = br.requestId)
                                             LEFT JOIN ' ,@currentSchema,'.approvalmatrix AS am ON am.id = ram.approvalMatrixId
                                         WHERE br.requestId = ',
                                            @singleQuote, @requestId, @singleQuote)
		END
		IF @sqlStmt = ''
		BEGIN
			SET @sqlStmt = @sqlSubStmt;
		END
		ELSE
		BEGIN
			SET @sqlStmt = CONCAT(@sqlStmt, ' UNION ALL ', @sqlSubStmt);
		END;
	END;

  	   -- select @sqlStmt
--     select OBJECT_SCHEMA_NAME(@@PROCID);
--         select @currentSchema;
	EXEC (@sqlStmt);

    END
	GO
	
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[getRequestApprovers_proc]
GO
CREATE PROCEDURE [${dbxschemaname}].[getRequestApprovers_proc]
    @_requestId nvarchar(max),
    @_status nvarchar(max)
AS
BEGIN
    SET XACT_ABORT ON
    SET NOCOUNT ON
    DECLARE @isGroupMatrix NVARCHAR(50)
    IF @_status IS NULL
       OR @_status = ''
    BEGIN
        SET @isGroupMatrix =
        (
            SELECT isGroupMatrix
            from [${dbxschemaname}].bbrequest
            where [bbrequest].requestId = @_requestId
        );

        IF @isGroupMatrix IS NOT NULL AND @isGroupMatrix = '1'
        BEGIN
            SELECT @_requestId AS requestId,
                   (select companyLegalUnit from [${dbxschemaname}].[bbrequest] where [bbrequest].requestId = @_requestId) AS companyLegalUnit,
                   csg.customerId AS approvers,
                   c.FirstName AS FirstName,
                   c.LastName AS LastName
            FROM [${dbxschemaname}].[customersignatorygroup] AS csg
                LEFT JOIN [${dbxschemaname}].[customer] AS c
                    ON (csg.[customerId] = [c].[id])
            WHERE [${dbxschemaname}].FIND_IN_SET(
                                                    [csg].signatoryGroupId,
                  (
                      SELECT STRING_AGG(REPLACE(REPLACE(REPLACE([signatorygrouprequestmatrix].pendingGroupList, ']', ''),'[',''),'"',''),',')
                      FROM [${dbxschemaname}].[signatorygrouprequestmatrix]
                      WHERE [signatorygrouprequestmatrix].requestId = @_requestId
                            AND [signatorygrouprequestmatrix].isApproved = 'false'
                  )) > 0;
        END
        ELSE
            SELECT min(bb.requestId) AS requestId,
                   min(bb.companyLegalUnit) AS companyLegalUnit,
                   cam.customerId AS approvers,
                   min(c.FirstName) AS FirstName,
                   min(c.LastName) AS LastName
            FROM [${dbxschemaname}].bbrequest AS bb
                CROSS JOIN [${dbxschemaname}].requestapprovalmatrix AS ram
                CROSS JOIN [${dbxschemaname}].customerapprovalmatrix AS cam
                CROSS JOIN [${dbxschemaname}].customer AS c
            WHERE bb.requestId = ram.requestId
                  AND ram.approvalMatrixId = cam.approvalMatrixId
                  AND cam.customerId = c.id
                  AND CAST(bb.requestId AS nvarchar(max)) = @_requestId
            GROUP BY cam.customerId
            ORDER BY cam.customerId
    END
    ELSE
        SELECT bb.createdby AS approvers,
               min(bb.companyLegalUnit) AS companyLegalUnit,
               min(c.FirstName) AS FirstName,
               min(c.LastName) AS LastName
        FROM [${dbxschemaname}].bbactedrequest AS bb
            CROSS JOIN [${dbxschemaname}].customer AS c
        WHERE bb.createdby = c.id
              AND CAST(bb.requestId AS nvarchar(max)) = @_requestId
              AND bb.status = @_status
        GROUP BY bb.createdby
        ORDER BY bb.createdby
END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[reject_pendingrequests_in_approvalqueue_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[reject_pendingrequests_in_approvalqueue_proc]
    @_requestMatrixDataJSON NVARCHAR(MAX),
    @_customerId VARCHAR(64)
AS
BEGIN
    DECLARE @noOfRequests INT;
    DECLARE @sqlStmt NVARCHAR(MAX);
    DECLARE @requestIndex INT;
    DECLARE @requestIds NVARCHAR(MAX);
    DECLARE @assocRequestIds NVARCHAR(MAX);

    SET @noOfRequests = JSON_QUERY(@_requestMatrixDataJSON, '$.length');

    SET @sqlStmt = '';
    SET @requestIndex = 0;
    SET @requestIds = '';
    SET @assocRequestIds = '';

    WHILE @requestIndex < @noOfRequests
    BEGIN
        SET @requestIndex = @requestIndex + 1;

        DECLARE @requestJSON NVARCHAR(MAX);
        DECLARE @requestId NVARCHAR(MAX);
        DECLARE @comments NVARCHAR(MAX);
        DECLARE @isGroupMatrix INT;
        DECLARE @assocRequestId NVARCHAR(MAX);
        DECLARE @companyId NVARCHAR(MAX);
        DECLARE @companyLegalUnit NVARCHAR(MAX);
        DECLARE @actingGroupsCSV NVARCHAR(MAX);
        DECLARE @actingGroups NVARCHAR(MAX);

        SET @requestJSON = JSON_QUERY(@_requestMatrixDataJSON, CONCAT('$[', @requestIndex - 1, ']'));
        SET @requestId = JSON_VALUE(@requestJSON, '$.requestId');
        SET @comments = JSON_VALUE(@requestJSON, '$.comments');
        SET @isGroupMatrix = 0;
        SET @assocRequestId = '';
        SET @companyId = '';
        SET @companyLegalUnit = '';

        SELECT @assocRequestId = assocRequestId, @companyId = companyId, @isGroupMatrix = isGroupMatrix, @companyLegalUnit = companyLegalUnit
        FROM [${dbxschemaname}].bbrequest
        WHERE requestId = @requestId;

        IF @assocRequestIds = ''
            SET @assocRequestIds = @assocRequestId;
        ELSE
            SET @assocRequestIds = CONCAT(@assocRequestIds, ',', @assocRequestId);

        IF @isGroupMatrix = 1
        BEGIN
            SET @actingGroupsCSV = JSON_VALUE(@requestJSON, '$.actingGroupsCSV');

            IF @actingGroupsCSV IS NULL
                CONTINUE;

            SET @actingGroups = '';

            SET @sqlStmt = CONCAT('SELECT @actingGroups = STRING_AGG(signatoryGroupName, '') FROM signatorygroup WHERE signatoryGroupId IN (', @actingGroupsCSV, ')');
            EXEC sp_executesql @sqlStmt, N'@actingGroups NVARCHAR(MAX) OUTPUT', @actingGroups OUTPUT;

            INSERT INTO [${dbxschemaname}].bbactedrequest (requestId, assocRequestId, companyId, status, comments, createdby, groupName, action, companyLegalUnit)
            VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments, @_customerId, @actingGroups, 'Rejected', @companyLegalUnit);
        END
        ELSE
        BEGIN
            INSERT INTO [${dbxschemaname}].bbactedrequest (requestId, assocRequestId, companyId, status, comments, createdby, action, companyLegalUnit)
            VALUES (@requestId, @assocRequestId, @companyId, 'Rejected', @comments, @_customerId, 'Rejected', @companyLegalUnit);
        END;
    END;

    SET @assocRequestIds = CONCAT('''', REPLACE(@assocRequestIds, ',', ''','''), '''');
    SET @sqlStmt = CONCAT('SELECT @requestIds = STRING_AGG(requestId, '''') FROM bbrequest WHERE assocRequestId IN (', @assocRequestIds, ')');
    EXEC sp_executesql @sqlStmt, N'@requestIds NVARCHAR(MAX) OUTPUT', @requestIds OUTPUT;

    SET @requestIds = CONCAT('''', REPLACE(@requestIds, ',', ''','''), '''');
    SET @sqlStmt = CONCAT('UPDATE bbrequest SET status = ''Rejected'' WHERE requestId IN (', @requestIds, ')');
    EXEC sp_executesql @sqlStmt;

    EXEC fetch_requests_with_approvalmatrixinfo_proc @assocRequestIds, '1', '', '0';
END;
GO

DROP procedure IF EXISTS [${dbxschemaname}].[fetch_all_profiles_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].[fetch_all_profiles_proc]
AS
BEGIN

select p.[profileId], p.[profileName] , p.[profileDescription] , p.[profileStatus], p.[numberOfUsers], p.[profileCreationDate] , p.[profileDeactivatedDate],
pc.[profileConditionId] , pc.[conditionExpression] , dc.[dataContextId] ,dc.[dataContextName] , dc.[dataContextDescription] , dc.[dataContextEndPoints]
from
[${dbxschemaname}].[profile] p,
[${dbxschemaname}].[profilecondition] pc,
[${dbxschemaname}].[datacontext] dc
where
p.[profileId] = pc.[profileId] and
pc.[dataContextId] = dc.[dataContextId] and 
p.[profileStatus] <> 'DELETED';
 
select p.[profileId],  cd.[campaignId] , cd.[campaignName] , cd.[campaignDescription] , cd.[campaignStatus] , cd.[startDate] ,cd.[endDate]  from
[${dbxschemaname}].[campaigndefinition] cd  
inner join [${dbxschemaname}].[campaignprofile] cp on cd.[campaignId] = cp.[campaignId]  
right outer join [${dbxschemaname}].[profile] p on p.[profileId] = cp.[profileId] 
where p.[profileStatus] <> 'DELETED';
END;
GO

CREATE  PROCEDURE [${dbxschemaname}].[get_campaign_proc](
               @_eventCode nvarchar(50),
               @_status nvarchar(50))
AS
BEGIN
	SET  XACT_ABORT  ON

    SET  NOCOUNT  ON


if (LEN(@_eventCode)>0)
BEGIN
    select cd.* 
	from 
	[${dbxschemaname}].campaigndefinition cd, 
	[${dbxschemaname}].campaigneventtrigger cet, 
	[${dbxschemaname}].eventtriggers et 
	where 
	cd.campaignId = cet.campaignId and 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode;
END
else if (LEN(@_status)>0)
BEGIN
    select * from [${dbxschemaname}].campaigndefinition where campaignStatus = @_status;
END
else
BEGIN
    select * from [${dbxschemaname}].campaigndefinition;
END

select * from [${dbxschemaname}].[campaigneventtrigger] ;
select * from [${dbxschemaname}].[campaignprofile]; 
select * from [${dbxschemaname}].[campaignchanneltype]; 
select * from [${dbxschemaname}].[campaignchanneldetails];  
select * from [${dbxschemaname}].[offlinetemplate]; 
select * from [${dbxschemaname}].[onlinecontent] where [campaignId]  is not null;

select * from [${dbxschemaname}].[profile] where [profileId]  in (select [profileId]  from [${dbxschemaname}].[campaignprofile]);
select * from [${dbxschemaname}].[profilecondition] where [profileId]  in (select [profileId]  from [${dbxschemaname}].[campaignprofile]) ;
select * from [${dbxschemaname}].[placeholder] where [placeholderId]  in (select [placeholderId]  from [${dbxschemaname}].[onlinecontent] where [campaignId]  is not null);

if (LEN(@_eventCode)>0)
BEGIN
    select * from [${dbxschemaname}].[eventtriggers] where eventTriggerId  in (select cet.eventTriggerId 
	from 
	[${dbxschemaname}].campaigneventtrigger cet left outer join
	[${dbxschemaname}].eventtriggers et 
	on 
	cet.eventTriggerId = et.eventTriggerId and 
	et.eventCode = @_eventCode);
END
else if (LEN(@_status)>0)
BEGIN
    select * from [${dbxschemaname}].eventtriggers where eventTriggerId in (select eventTriggerId from [${dbxschemaname}].campaigneventtrigger c where campaignId in (select campaignId  from [${dbxschemaname}].campaigndefinition c where campaignStatus =@_status));
END
else
BEGIN
    select * from [${dbxschemaname}].[eventtriggers] where eventTriggerId  in (select [eventTriggerId] from [${dbxschemaname}].[campaigneventtrigger]);
END

select * from [${dbxschemaname}].[datacontext] where [dataContextId]  in (select [dataContextId]  from [${dbxschemaname}].[profilecondition] where [profileId] in (select [profileId]  from [${dbxschemaname}].[campaignprofile]) );


END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[default_campaign_get_proc];
GO

CREATE  PROCEDURE [${dbxschemaname}].[default_campaign_get_proc]
AS
BEGIN
	SET  XACT_ABORT  ON
    SET  NOCOUNT  ON
     select [${dbxschemaname}].[onlinecontent].placeholderId , 
	        [${dbxschemaname}].[onlinecontent].targetURL,
	        [${dbxschemaname}].[onlinecontent].onlineContentId,
	        [${dbxschemaname}].[onlinecontent].imageIndex,
	        [${dbxschemaname}].[onlinecontent].imageURL,
	        [${dbxschemaname}].[placeholder].placeholderDescription,
	        [${dbxschemaname}].[placeholder].placeholderName,
	        [${dbxschemaname}].[placeholder].channelSubType,
	        [${dbxschemaname}].[placeholder].imageResolution,
	        [${dbxschemaname}].[placeholder].imageScale,
	        [${dbxschemaname}].[placeholder].placeholderIdentifier
	from [${dbxschemaname}].[onlinecontent] 
	join [${dbxschemaname}].[placeholder] 
	on [${dbxschemaname}].[onlinecontent].placeholderId = [${dbxschemaname}].[placeholder].placeholderId
    WHERE [${dbxschemaname}].[onlinecontent].campaignId is NULL ;
	
END;
GO


CREATE PROCEDURE [${dbxschemaname}].[update_campaign_proc](
@eventTriggerIdList nvarchar(max),
@profileIdList nvarchar(max),
@channelType nvarchar(max),
@offlineTemplate nvarchar(max),
@onlineContent nvarchar(max),
@channelDetails nvarchar(max),
@campaignId nvarchar(50),
@campaignName nvarchar(50),
@campaignDescription nvarchar(1000),
@campaignPriority int ,
@startDate nvarchar(50),
@endDate nvarchar(50),
@campaignType nvarchar(50),
@objectiveType nvarchar(50),
@productId nvarchar(50),
@productGroupId nvarchar(50),
@campaignStatus nvarchar(50)
)
AS
BEGIN
	SET  XACT_ABORT  ON

    SET  NOCOUNT  ON

	delete from [${dbxschemaname}].[campaigneventtrigger] where [campaigneventtrigger].[campaignId]  = @campaignId;
	
	delete from [${dbxschemaname}].[campaignprofile] where [campaignprofile].[campaignId]    = @campaignId;
	delete from [${dbxschemaname}].[campaignchanneltype] where [campaignchanneltype].[campaignId]  =  @campaignId;
	delete from [${dbxschemaname}].[offlinetemplate] where [offlinetemplate].[campaignId]     =  @campaignId;
	delete from [${dbxschemaname}].[onlinecontent] where [onlinecontent].[campaignId]    =  @campaignId;
	delete from [${dbxschemaname}].[campaignchanneldetails] where [campaignchanneldetails].[campaignId]    =  @campaignId;
	delete from [${dbxschemaname}].[campaigndefinition] where [campaigndefinition].[campaignId]    =  @campaignId;
	 EXEC [${dbxschemaname}].create_campaign_proc @eventTriggerIdList,@profileIdList,@channelType,@offlineTemplate,@onlineContent,@channelDetails,
 @campaignId,@campaignName,@campaignDescription,@campaignPriority,@startDate,@endDate,@campaignType,@objectiveType,@productId,@productGroupId,@campaignStatus;
     
END
GO


ALTER TABLE [${dbxschemaname}].[makercheckerconfig] ADD [viewDetailsAPI] varchar(200);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[checkforpendingrequests_proc];
GO
CREATE  PROCEDURE [${dbxschemaname}].[checkforpendingrequests_proc](
    @module NVARCHAR(1024),
    @action NVARCHAR(1024),
    @record NVARCHAR(1024),
    @CompanyLegalUnitId NVARCHAR(1024)
)
AS
BEGIN
	  DECLARE @i INT = 1;
    DECLARE @num_records INT;
    DECLARE @current_module NVARCHAR(50);
    DECLARE @current_action NVARCHAR(50);
    DECLARE @current_legalUnit NVARCHAR(50);
    DECLARE @current_record NVARCHAR(255);
    DECLARE @requestDetails NVARCHAR(1024);
    DECLARE @temp_requestId NVARCHAR(50);
 
    SET @num_records = (SELECT MAX(LEN(@module) - LEN(REPLACE(@module, '|', ''))) + 1);
 
    IF OBJECT_ID('tempdb..#temp_checkforpendingrequests_results') IS NOT NULL
        DROP TABLE #temp_checkforpendingrequests_results;
 
    CREATE TABLE #temp_checkforpendingrequests_results (
        requestId NVARCHAR(50),
        requestDetails NVARCHAR(1024)
    );
 
    WHILE @i <= @num_records
    BEGIN
		SET @temp_requestId = null;
        SET @current_module = PARSENAME(REPLACE(@module, '|', '.'), @i);
        SET @current_action = PARSENAME(REPLACE(@action, '|', '.'), @i);
        SET @current_legalUnit = PARSENAME(REPLACE(@CompanyLegalUnitId, '|', '.'), @i);
        SET @current_record = PARSENAME(REPLACE(@record, '|', '.'), @i);
 
        SET @requestDetails = CONCAT(@current_module, '_', @current_action, '_', @current_legalUnit, '_', @current_record);
 
        SELECT TOP 1 @temp_requestId = requestId
        FROM approvalrequests
        WHERE
            recordId COLLATE SQL_Latin1_General_CP1_CI_AS = @current_record AND
            module COLLATE SQL_Latin1_General_CP1_CI_AS = @current_module AND
            companyLegalUnit COLLATE SQL_Latin1_General_CP1_CI_AS = @current_legalUnit AND
            action COLLATE SQL_Latin1_General_CP1_CI_AS = @current_action AND
            status COLLATE SQL_Latin1_General_CP1_CI_AS = 'pending';
 
        INSERT INTO #temp_checkforpendingrequests_results (requestId, requestDetails) VALUES (@temp_requestId, @requestDetails);
        SET @i = @i + 1;
    END;
    SELECT * FROM #temp_checkforpendingrequests_results;
    IF OBJECT_ID('tempdb..#temp_checkforpendingrequests_results') IS NOT NULL
        DROP TABLE #temp_checkforpendingrequests_results;
END
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[update_profile_users_proc];
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[update_profile_users_proc](
	@_profileDataCSV NVARCHAR(max)
)
AS 
BEGIN
	DECLARE @index int = 0
	DECLARE  @recordsData nvarchar(255) = N''
    DECLARE  @numOfRecords varchar(255) = N''
	DECLARE @profileId varchar(255) = N''
	DECLARE @userCount varchar(255) = N''
	DECLARE @execStmt NVARCHAR(max);
    SET @numOfRecords = datalength(@_profileDataCSV) - datalength(REPLACE(@_profileDataCSV, N',', N'')) + 1;
      WHILE (1 = 1) BEGIN
          SET @index = @index + 1;
          IF @index = @numOfRecords + 1  
            break;
          ELSE BEGIN
			SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_profileDataCSV, ',', @index), ',', -1 );
			SET @profileId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',1), ':', -1 ); 
			SET @userCount = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',2), ':', -1 );
			SET @execStmt  = concat('update [${dbxschemaname}].[${dbxschemaname}].profile set numberOfUsers = ''' , @userCount , ''' where profileId = ''',@profileId,'''');
			exec(@execStmt);
           END
		END
    END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[default_campaign_update_proc];
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[default_campaign_update_proc](
	@_campaignData  nvarchar(max)
)
AS
BEGIN

  DECLARE @index INTEGER = 0;
  DECLARE @numOfRecords INTEGER = 0;
  DECLARE @responseIds  nvarchar(max)
  DECLARE @recordsData nvarchar(max)
  DECLARE @onlineContentId  varchar(max)
  DECLARE @placeholderId nvarchar(max)
  DECLARE @imageURL varchar(max)
  DECLARE @targetURL nvarchar(max)
  DECLARE @imageIndex nvarchar(max)

  delete from [${dbxschemaname}].onlinecontent where [${dbxschemaname}].onlinecontent.campaignId IS NULL;

  SET @index = 1;
  SET @numOfRecords = LEN(@_campaignData) - LEN(REPLACE(@_campaignData, '|', '')) + 1;

  WHILE (@index <= @numOfRecords )

  BEGIN

    SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_campaignData, '|', @index), '|', -1 );
	SET @onlineContentId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '<>',1), '<>', -1 ); 
	SET @placeholderId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '<>',2), '<>', -1 );
	SET @imageURL = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '<>',3), '<>', -1 ); 
	SET @targetURL = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '<>',4), '<>', -1 );
	SET @imageIndex = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '<>',5), '<>', -1 ); 
		    
	INSERT INTO [${dbxschemaname}].onlinecontent(onlineContentId,placeholderId,imageURL,targetURL,imageIndex)
			values (@onlineContentId,@placeholderId,@imageURL,@targetURL,@imageIndex);

	SET @responseIds = concat(@responseIds,' ',@onlineContentId);
	SET  @index = @index + 1;

  END
  SELECT @responseIds as onlineContentId;
END
GO
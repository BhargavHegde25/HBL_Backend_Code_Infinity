CREATE TABLE [${dbxschemaname}].[makercheckerconfig] (
  [id] [int] NOT NULL,
  [expAPIOperationName] varchar(250) DEFAULT NULL,
  [module] varchar(250) DEFAULT NULL,
  [action] varchar(250) DEFAULT NULL,
  [approvalPermissionId] varchar(10) DEFAULT NULL,
  [approvalPermissionName] varchar(50) DEFAULT NULL,
  [keyColumnNames] varchar(200) NOT NULL,
  [isApprovalRequired] [smallint] NOT NULL,
  [createdby] varchar(50) DEFAULT NULL,
  [createdts] [datetime2] NOT NULL DEFAULT getDate(),
  [companyLegalUnit] varchar(50) NOT NULL DEFAULT 'ALL',
  PRIMARY KEY ([id])
)  ;

ALTER TABLE [${dbxschemaname}].[approvalrequests] ADD [reqPayload] varchar(max) NOT NULL, [companyLegalUnit] varchar(50) NOT NULL DEFAULT 'ALL';
GO

EXEC [${dbxschemaname}].sp_delete_default_constranit '${dbxschemaname}.[transaction]', 'companyLegalUnit';
GO
ALTER TABLE [${dbxschemaname}].[transaction] DROP COLUMN companyLegalUnit;
ALTER TABLE [${dbxschemaname}].[transaction] ADD isTypeBusiness VARCHAR(45) NULL;
GO
ALTER TABLE [${dbxschemaname}].[customer] ADD [sbaEnrolmentStatus] VARCHAR(50) NULL DEFAULT NULL;
GO

-- Modified MySQL statements with square brackets for schema, table, and column names
ALTER TABLE [${dbxschemaname}].[productInformation] ADD [availableFromDate] VARCHAR(100) NULL;
ALTER TABLE [${dbxschemaname}].[productInformation] ADD [availableToDate] VARCHAR(100) NULL;
ALTER TABLE [${dbxschemaname}].[productInformation] ADD [purposeData] VARCHAR(100) NULL;

CREATE TABLE [${dbxschemaname}].[Facilities] (
  [facilityId] VARCHAR(100) NOT NULL,
  [code] VARCHAR(100) NOT NULL,
  [facilityName] VARCHAR(100) NULL,
  [description] VARCHAR(100) NULL,
  [numOfFeatures] VARCHAR(100) NULL,
  PRIMARY KEY ([code])
);

CREATE TABLE [${dbxschemaname}].[productFacility] (
  [productRef] VARCHAR(100) NOT NULL,
  [facilityId] VARCHAR(100) NULL,
  [sequenceNo] VARCHAR(100) NULL,
  [code] VARCHAR(100) NOT NULL,
  [productFacilityId] VARCHAR(100) NOT NULL,
  [description] VARCHAR(100) NULL,
  [facilityName] VARCHAR(100) NULL,
  [isMandatory] VARCHAR(100) NULL,
  [defaultValue] VARCHAR(100) NULL,
  [optionDispType] VARCHAR(100) NULL,
  PRIMARY KEY ([productFacilityId]),
  INDEX [productFacility_PK] ([code]),
  INDEX [productFacility_PK_1] ([productRef]),
  CONSTRAINT [productFacility_FK] FOREIGN KEY ([code]) REFERENCES [${dbxschemaname}].[Facilities] ([code]),
  CONSTRAINT [productFacility_FK_1] FOREIGN KEY ([productRef]) REFERENCES [${dbxschemaname}].[productInformation] ([productRef])
);

-- productDescription table
CREATE TABLE [${dbxschemaname}].[productDescription] (
  [productRef] VARCHAR(100) NOT NULL,
  [description] VARCHAR(100) NULL,
  [detailedDesc] TEXT NULL,
  [disclosure] TEXT NULL,
  [notes] TEXT NULL,
  [termsConditions] TEXT NULL,
  PRIMARY KEY ([productRef]),
  CONSTRAINT [productDescription_FK] FOREIGN KEY ([productRef]) REFERENCES [${dbxschemaname}].[productInformation] ([productRef])
);

-- productImage table
CREATE TABLE [${dbxschemaname}].[productImage] (
  [productRef] VARCHAR(100) NOT NULL,
  [imageType] VARCHAR(100) NOT NULL,
  [height] VARCHAR(100) NULL,
  [width] VARCHAR(100) NULL,
  [imageUrl] VARCHAR(200) NULL,
  [imageId] VARCHAR(100) NOT NULL,
  [productId] VARCHAR(100) NULL,
  PRIMARY KEY ([productRef], [imageType]),
  CONSTRAINT [productImage_FK] FOREIGN KEY ([productRef]) REFERENCES [${dbxschemaname}].[productInformation] ([productRef])
);

-- productAdditionalAttributes table
CREATE TABLE [${dbxschemaname}].[productAdditionalAttributes] (
  [id] INT NOT NULL IDENTITY(1,1),
  [key] VARCHAR(100) NOT NULL,
  [value] VARCHAR(100) NULL,
  [productRef] VARCHAR(100) NOT NULL,
  PRIMARY KEY ([id]),
  CONSTRAINT [additionalAttributes_FK] FOREIGN KEY ([productRef]) REFERENCES [${dbxschemaname}].[productInformation] ([productRef])
);

-- productOptionValues table
CREATE TABLE [${dbxschemaname}].[productOptionValues] (
  [id] INT NOT NULL IDENTITY(1,1),
  [productFacilityId] VARCHAR(100) NOT NULL,
  [value] VARCHAR(100) NULL,
  [desc] VARCHAR(100) NULL,
  [extensionData] VARCHAR(100) NULL,
  PRIMARY KEY ([id]),
  CONSTRAINT [optionValues_FK] FOREIGN KEY ([productFacilityId]) REFERENCES [${dbxschemaname}].[productFacility] ([productFacilityId])
);

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[delete_marketingdata];
GO

-- Create the procedure
CREATE PROCEDURE [${dbxschemaname}].[delete_marketingdata]
AS
BEGIN
    DELETE FROM [${dbxschemaname}].productAdditionalAttributes ;
	  DELETE FROM [${dbxschemaname}].productOptionValues ;
	  DELETE FROM [${dbxschemaname}].productFeatureActions ;
	  DELETE FROM [${dbxschemaname}].productFeatures ;
    DELETE FROM [${dbxschemaname}].productDescription ;
	  DELETE FROM [${dbxschemaname}].productImage ;
    DELETE FROM [${dbxschemaname}].productFacility;
    DELETE FROM [${dbxschemaname}].Facilities;
    DELETE FROM [${dbxschemaname}].productInformation;
    DELETE FROM [${dbxschemaname}].productGroup;
    DELETE FROM [${dbxschemaname}].productLine;
END;

GO
-- Drop the procedure if it exists
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[insertProductInformationSch];
GO

-- Create the procedure
CREATE PROCEDURE [${dbxschemaname}].[insertProductInformationSch]
  @queryInput NVARCHAR(MAX)
AS
BEGIN
SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
  DECLARE @index INT = 0;
  DECLARE @numOfRecords INT;
  DECLARE @recordsData NVARCHAR(MAX);
  DECLARE @query NVARCHAR(MAX);
 set @queryInput = REPLACE(@queryInput,'\','')
	  set @queryInput = REPLACE(@queryInput,'"','''')
   SET @numOfRecords = case when @queryInput is null then 0 else LEN(@queryInput) - LEN(replace(@queryInput, '|', '')) + 1 end
 
  WHILE @index < @numOfRecords
  BEGIN
    SET @index = @index + 1;

    IF @index = @numOfRecords + 1
      BREAK;
    ELSE
      SET @recordsData = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@queryInput, N'|', @index), N'|', -1)
      SET @query = ('INSERT INTO [${dbxschemaname}].[productInformation] (productId, productRef, branchRef, productName, productLineRef, productGroupRef, status, availableFromDate, availableToDate) VALUES (') + (@recordsData) + (N')')
        EXEC(@query)
    END;
  END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[insertProductFacilitiesSch];
GO

CREATE PROCEDURE [${dbxschemaname}].[insertProductFacilitiesSch]
  @queryInput NVARCHAR(MAX)
AS
BEGIN
  SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
  DECLARE @index INT = 0;
  DECLARE @numOfRecords INT;
  DECLARE @recordsData NVARCHAR(MAX);
  DECLARE @facilityId NVARCHAR(MAX);
  DECLARE @code NVARCHAR(MAX);
  DECLARE @facilityName NVARCHAR(MAX);
  DECLARE @description NVARCHAR(MAX);
  DECLARE @numOfFeatures NVARCHAR(MAX);
  DECLARE @productRef NVARCHAR(MAX);
  DECLARE @sequenceNo NVARCHAR(MAX);
  DECLARE @isMandatory NVARCHAR(MAX);
  DECLARE @productFacilityId NVARCHAR(MAX);
  DECLARE @codec NVARCHAR(MAX);
  DECLARE @facilitiesInserted INT;
  DECLARE @queryFacilities NVARCHAR(MAX);
  DECLARE @queryProductFacility NVARCHAR(MAX);

  set @queryInput = REPLACE(@queryInput,'\','')
	  set @queryInput = REPLACE(@queryInput,'"','''')
     SET @numOfRecords = LEN(@queryInput) - LEN(REPLACE(@queryInput, '|', '')) + 1;
  WHILE @index < @numOfRecords
  BEGIN
    SET @index = @index + 1;

    IF @index = @numOfRecords + 1
      BREAK;
    ELSE
      SET @recordsData = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@queryInput, N'|', @index), N'|', -1)
      SET @facilityId = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 1), N',', -1)
      SET @code = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 2), N',', -1)
      SET @facilityName = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 3), N',', -1)
      SET @description = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 4), N',', -1)
      SET @numOfFeatures =[${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 5), N',', -1)
      SET @productRef = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 6), N',', -1)
      SET @sequenceNo = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 7), N',', -1)
      SET @isMandatory = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 8), N',', -1)
      SET @productFacilityId = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, N',', 9), N',', -1)
       SET @codec = REPLACE(@code,'''', '');


      SET @facilitiesInserted = 0;

      IF NOT EXISTS (SELECT code FROM [${dbxschemaname}].[Facilities] WHERE code = @codec )
      BEGIN
        SET @queryFacilities = ('INSERT INTO [${dbxschemaname}].[Facilities] (facilityId, code, facilityName, description, numOfFeatures) VALUES (')+ @facilityId+','+ @code+','+@facilityName+','+ @description+','+@numOfFeatures+ (N');')
    
        SET @facilitiesInserted = 1;
      END;

      SET @queryProductFacility = ('INSERT INTO [${dbxschemaname}].[productFacility] (productRef, facilityId, sequenceNo, code, productFacilityId, description, facilityName, isMandatory) VALUES (') +  @productRef+','+ @facilityId+','+  @sequenceNo+','+  @code+','+  @productFacilityId+','+  @description+','+  @facilityName+','+  @isMandatory+ (N');')
  
   
     IF @facilitiesInserted = 1
    BEGIN
       EXEC(@queryFacilities);
     END;

      EXEC(@queryProductFacility);
   END;
  END;
GO

 DROP PROCEDURE IF EXISTS [${dbxschemaname}].[facilities_get_proc];
GO

-- Create the procedure
CREATE PROCEDURE [${dbxschemaname}].[facilities_get_proc]
  @facilityId VARCHAR(50)
AS
BEGIN
  IF (@facilityId IS NULL OR @facilityId = '')
  BEGIN
    SELECT * FROM [${dbxschemaname}].[Facilities];
  END
  ELSE
  BEGIN
    SELECT * FROM [${dbxschemaname}].[Facilities] WHERE facilityId = @facilityId;
  END
END;
GO
-- Drop the procedure if it exists
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[product_details_proc];
GO

-- Create the procedure product_details_proc
CREATE PROCEDURE [${dbxschemaname}].[product_details_proc]
  @_productRef VARCHAR(50)
AS
BEGIN
    SELECT pi.productId, pl.productLineId, pl.productLineRef, pl.productLineName, pl.externalIndicator,
           pg.productGroupId, pg.productGroupRef, pg.productGroupName, pg.description, pg.detailedDesc,
           pi.productRef, pi.productName, pi.status, pi.branchRef, pi.availableFromDate, pi.availableToDate,
           pi.purposeData
    FROM [${dbxschemaname}].productInformation pi
    JOIN [${dbxschemaname}].productLine pl ON pl.productLineRef = pi.productLineRef
    JOIN [${dbxschemaname}].productGroup pg ON pg.productGroupRef = pi.productGroupRef
    WHERE pi.productRef = @_productRef;

    SELECT * FROM [${dbxschemaname}].productAdditionalAttributes WHERE productRef = @_productRef;

    SELECT * FROM [${dbxschemaname}].productImage WHERE productRef = @_productRef;

    SELECT * FROM [${dbxschemaname}].productFacility WHERE productRef = @_productRef;

    SELECT * FROM [${dbxschemaname}].productOptionValues WHERE productFacilityId IN (SELECT productFacilityId FROM [${dbxschemaname}].productFacility WHERE productRef = @_productRef);

    SELECT pd.description, pd.detailedDesc, pd.disclosure, pd.notes, pd.termsConditions
    FROM [${dbxschemaname}].productDescription pd
    WHERE pd.productRef = @_productRef;

    SELECT * FROM [${dbxschemaname}].productFeatures WHERE productRef = @_productRef;

    SELECT pfa.actionsId, pfa.productId, pfa.featureId, pfa.extensionData, pfa.status, pf.featureCode
    FROM [${dbxschemaname}].productFeatureActions pfa
    JOIN [${dbxschemaname}].productFeatures pf ON pf.productRef = @_productRef AND pfa.productId = pf.productId AND pfa.featureId = pf.featureId;
END;
GO

-- Drop the procedure if it exists
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[product_update_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[product_update_proc]
  @_description VARCHAR(100),
  @_detailedDesc NVARCHAR(MAX),
  @_notes VARCHAR(MAX), -- Change to VARCHAR(MAX)
  @_termsConditions VARCHAR(MAX), -- Change to VARCHAR(MAX)
  @_disclosure VARCHAR(MAX), -- Change to VARCHAR(MAX)
  @_productRef VARCHAR(50),
  @_productName VARCHAR(50),
  @_availableFrom VARCHAR(50),
  @_availableTo VARCHAR(50),
  @_purposes VARCHAR(50),
  @_addAttributes VARCHAR(50)
AS
BEGIN
    SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

    DECLARE @queryProductDesc NVARCHAR(MAX);
    DECLARE @queryProductInfo NVARCHAR(MAX);
    DECLARE @pd_count INT;
    DECLARE @index int;
    DECLARE @numOfRecords int;
    DECLARE @recordsData nvarchar(max);
    DECLARE @attributeId VARCHAR(100);
    DECLARE @attributeValue VARCHAR(100);
    SET @pd_count = (SELECT COUNT([productRef]) FROM [${dbxschemaname}].productDescription WHERE [productRef] = @_productRef);
    IF @pd_count = 0
    BEGIN
    INSERT INTO [${dbxschemaname}].productDescription ([description], [detailedDesc], [notes], [termsConditions], [disclosure], [productRef])
        VALUES (@_description, @_detailedDesc, @_notes, @_termsConditions, @_disclosure, @_productRef);
       END
       ELSE
       BEGIN
              SET @queryProductDesc=CONCAT('UPDATE [dbxdb].productDescription
        SET description = ','''',@_description,'''',',',
            'detailedDesc = ','''',@_detailedDesc,'''',',',
            'notes = ','''',@_notes,'''',',',
            'termsConditions = ','''',@_termsConditions,'''',',',
            'disclosure =','''',@_disclosure,'''',
        ' WHERE productRef = ','''',@_productRef,'''',';')
  END
  

    SET @queryProductInfo=CONCAT('UPDATE [dbxdb].productInformation
    SET productName = ','''',@_productName,'''',',',
        'availableFromDate = ','''',@_availableFrom,'''',',',
        'availableToDate = ','''',@_availableTo,'''',',',
        'purposeData = ','''',@_purposes,'''',
   ' WHERE productRef = ','''',@_productRef,'''',';')
  
    DELETE FROM [${dbxschemaname}].productAdditionalAttributes WHERE  [productRef] = @_productRef;
 
 		SET @index = 0;
		SET @numOfRecords = LEN(@_addAttributes) - LEN(REPLACE(@_addAttributes, '|^', '')) + 1;
    WHILE (1 = 1)
      BEGIN
        SET  @index = @index + 1
          IF (@index = @numOfRecords)
            BREAK
          ELSE 
            BEGIN
              set @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_addAttributes, '|^', @index), '|^', -1 );
              set @attributeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '#$',1 ), '#$', -1 );
              set @attributeValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, '#$',2 ), '#$', -1 );

              INSERT INTO [${dbxschemaname}].productAdditionalAttributes ([productRef],[key],[value]) values(@_productRef,@attributeId,@attributeValue);
            END
      END
    EXEC(@queryProductDesc);
    EXEC(@queryProductInfo);
    SELECT [productId] FROM [${dbxschemaname}].productInformation WHERE [productRef] = @_productRef;
END;
GO

-- Create the productFeatures table
CREATE TABLE [${dbxschemaname}].[productFeatures]
(
    [productRef] VARCHAR(100) NOT NULL,
    [productId] VARCHAR(100) NOT NULL,
    [featureId] VARCHAR(100) NOT NULL,
    [featureCode] VARCHAR(100) NOT NULL,
    [status] VARCHAR(10) DEFAULT 'Active',
    PRIMARY KEY ([productId], [featureId])
);

-- Create the productFeatureActions table
CREATE TABLE [${dbxschemaname}].[productFeatureActions]
(
    [actionsId] VARCHAR(100) NOT NULL,
    [productId] VARCHAR(100) NOT NULL,
    [featureId] VARCHAR(100) NOT NULL,
    [extensionData] VARCHAR(100) NOT NULL DEFAULT '{}',
    [status] VARCHAR(10) DEFAULT 'Active',
    PRIMARY KEY ([productId], [featureId], [actionsId])
);


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[product_features_proc];
GO
-- Create the procedure product_features_proc
CREATE PROCEDURE [${dbxschemaname}].[product_features_proc]
  @productId VARCHAR(50)
AS
BEGIN
    SELECT * FROM [${dbxschemaname}].[productFeatures] WHERE productId = @productId;
    SELECT * FROM [${dbxschemaname}].[productFeatureActions] WHERE productId = @productId AND featureId IN (SELECT featureId FROM [${dbxschemaname}].[productFeatures] WHERE productId = @productId);
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[product_features_delete_proc];
GO

-- Create the procedure product_features_delete_proc
CREATE PROCEDURE [${dbxschemaname}].[product_features_delete_proc]
  @productId VARCHAR(50),
  @featureId VARCHAR(50)
AS
BEGIN
    DELETE FROM [${dbxschemaname}].[productFeatures] WHERE productId = @productId AND featureId = @featureId;
    DELETE FROM [${dbxschemaname}].[productFeatureActions] WHERE productId = @productId AND featureId = @featureId;
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_distinct_customeraccounts];
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_distinct_customeraccounts]
    @_queryInput nvarchar(max),
    @_companyLegalunit nvarchar(max)
AS

BEGIN

    SET  XACT_ABORT  ON

    SET  NOCOUNT  ON

    DECLARE @index int = 0
    DECLARE @numOfRecords int =0
    DECLARE @currLoopPayload nvarchar(max)
    DECLARE @contractId nvarchar(50)
    DECLARE @coreCustomerId nvarchar(max)
    DECLARE @accountId nvarchar(100)
    DECLARE @query nvarchar(max)
    DECLARE @whereClause nvarchar(max)

    SET @index = 0;
    SET @whereClause= '';
    set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1;
        WHILE (1 = 1)
        BEGIN
            SET @index = @index + 1
            IF @index = @numOfRecords + 1
                BREAK
            ELSE 
                BEGIN
                    set @currLoopPayload = ${dbxschemaname}.SUBSTRING_INDEX(${dbxschemaname}.SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 );
                    set @contractId =  ${dbxschemaname}.SUBSTRING_INDEX(${dbxschemaname}.SUBSTRING_INDEX(@currLoopPayload, ',', 1), ',', -1 );
                    set @coreCustomerId = ${dbxschemaname}.SUBSTRING_INDEX(${dbxschemaname}.SUBSTRING_INDEX(@currLoopPayload, ',', 2), ',', -1 );
                    set @accountId = ${dbxschemaname}.SUBSTRING_INDEX(${dbxschemaname}.SUBSTRING_INDEX(@currLoopPayload, ',', 3), ',', -1 );

                    IF @index!=1
                            set @whereClause = CONCAT( @whereClause , ' or ');
                    

                    set @whereClause = CONCAT(@whereClause, '(contractId= ' , QUOTENAME(@contractId, '''') , ' and corecustomerId =  ' , 
                                                       QUOTENAME(@coreCustomerId, '''') , ' and Account_id = ' ,  QUOTENAME(@accountId, '''') , ')');

                    set @query = 'select distinct customer_id, account_id from ${dbxschemaname}.customeraccounts where (';
                    set @query = CONCAT(@query , @whereClause);

                    IF @index = @numOfRecords 
                                set @query = CONCAT(@query , ') and companyLegalUnit = ''', @_companyLegalunit ,'''');
                END    
        END
        exec(@query)
END
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[faqcategory_view];
GO
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE VIEW [${dbxschemaname}].[faqcategory_view] (
   [id], 
   [Status_id], 
   [QuestionCode], 
   [Question], 
   [Channel_id], 
   [Answer], 
   [CategoryId], 
   [CategoryName],
   [companyLegalUnit])
AS 
   SELECT 
      faqs.id AS id, 
      faqs.Status_id AS Status_id, 
      faqs.QuestionCode AS QuestionCode, 
      faqs.Question AS Question, 
      faqs.Channel_id AS Channel_id, 
      faqs.Answer AS Answer, 
      faqs.FaqCategory_Id AS CategoryId, 
	  faqcategory.Name AS CategoryName,
	  faqs.companyLegalUnit AS companyLegalUnit
    FROM ([${dbxschemaname}].faqs 
      INNER JOIN [${dbxschemaname}].faqcategory 
      ON ((faqcategory.id = faqs.FaqCategory_Id)));
GO

CREATE TABLE [${dbxschemaname}].[scf_records_module_configurations] (
    [module_id] VARCHAR(255) PRIMARY KEY,
    [allowed_fields] VARCHAR(MAX)
);

CREATE TABLE [${dbxschemaname}].[scf_records] (
    [record_id] INT PRIMARY KEY IDENTITY(1000000,1),
    [module_id] VARCHAR(255),
    [record_data] VARCHAR(MAX),
    [created_by] VARCHAR(255),
    [updated_by] VARCHAR(255),
    [created_date] DATETIME2 DEFAULT GETDATE(),
    [updated_date] DATETIME2 DEFAULT GETDATE(),
    CONSTRAINT [module_id_fk] FOREIGN KEY ([module_id]) REFERENCES [${dbxschemaname}].[scf_records_module_configurations]([module_id]),
    CONSTRAINT [record_data_validate] CHECK (ISJSON([record_data]) > 0)
);
-- Drop table

-- DROP TABLE [${dbxschemaname}].suspendedcustomers GO

CREATE TABLE [${dbxschemaname}].suspendedcustomers (
	id nvarchar(50) NOT NULL,
	contractId varchar(50) DEFAULT (NULL),
	customerId nvarchar(50) DEFAULT (NULL),
	coreCustomerId varchar(45) DEFAULT (NULL),
	createdby varchar(50) DEFAULT (NULL),
	modifiedby varchar(50) DEFAULT (NULL),
	createdts datetime2(7) DEFAULT (NULL),
	lastmodifiedts datetime2(7) DEFAULT (NULL),
	synctimestamp datetime2(7) DEFAULT (NULL),
	softdeleteflag bit DEFAULT ((0)) NOT NULL,
	CONSTRAINT PK_suspendedcustomers_id PRIMARY KEY (id),
	CONSTRAINT FK_suspendedcustomers_customer_customerId FOREIGN KEY (customerId) REFERENCES [${dbxschemaname}].customer(id)
)
GO
CREATE UNIQUE INDEX suspendedcustomers_UN ON [${dbxschemaname}].suspendedcustomers (contractId,customerId,coreCustomerId)
GO
CREATE INDEX suspendedcustomers_contractId_IDX ON [${dbxschemaname}].suspendedcustomers (contractId)
GO
CREATE INDEX suspendedcustomers_coreCustomerId_IDX ON [${dbxschemaname}].suspendedcustomers (coreCustomerId)
GO
CREATE INDEX suspendedcustomers_customerId_IDX ON [${dbxschemaname}].suspendedcustomers (customerId)
GO

DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertgrouplevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        dbxalerttype.Name AS alertGroupName,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        alertsubtype.Name AS alertName,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alerttypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem
    FROM
        (((dbxalerttype
        JOIN alerttypechannel ON ((dbxalerttype.id = alerttypechannel.alertTypeId)))
        JOIN alertsubtype ON ((dbxalerttype.id = alertsubtype.AlertTypeId)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel] 
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertlevel]
  AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        dbxalerttype.Name AS alertGroupName,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        alertsubtype.Name AS alertName,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertsubtypechannel.channelId AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem
    FROM
        (((alertsubtype
        JOIN alertsubtypechannel ON ((alertsubtype.id = alertsubtypechannel.alertSubTypeId)))
        JOIN dbxalerttype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))
        JOIN dbxalertcategory ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
GO


DROP VIEW IF EXISTS [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
GO
CREATE VIEW [${dbxschemaname}].[alerts_fetch_globaldata_view_alertcategorylevel]
 AS
    SELECT 
        dbxalerttype.id AS AlertTypeId,
        dbxalerttype.AlertCategoryId AS AlertCategoryId,
        dbxalerttype.Name AS alertGroupName,
        alertsubtype.attributeId AS AttributeId,
        alertsubtype.alertConditionId AS AlertConditionId,
        alertsubtype.value1 AS Value1,
        alertsubtype.value2 AS Value2,
        alertsubtype.Name AS alertName,
        dbxalerttype.Status_id AS alerttype_status_id,
        alertsubtype.isGlobal AS IsGlobal,
        dbxalertcategory.status_id AS alertcategory_status_id,
        alertcategorychannel.ChannelID AS ChannelId,
        alertsubtype.id AS AlertSubTypeId,
        alertsubtype.Status_id AS alertsubtypetype_status_id,
        alertsubtype.isAccountLevel AS accountLevel,
        alertsubtype.externalSystem AS externalSystem
    FROM
        (((dbxalertcategory
        JOIN alertcategorychannel ON ((alertcategorychannel.AlertCategoryId = dbxalertcategory.id)))
        JOIN dbxalerttype ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
        JOIN alertsubtype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))
GO

ALTER TABLE [${dbxschemaname}].[alertmessagetypeconfig] ADD [passVariableData] BIT NULL DEFAULT 0;
ALTER TABLE [${dbxschemaname}].[alertmessagetypeconfig] ADD [subject] NVARCHAR(255) NULL;
GO




DROP PROCEDURE IF EXISTS [${dbxschemaname}].[contract_users_details_get_proc];
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

ALTER TABLE [${dbxschemaname}].[favouriteinstruments]
ADD [favInstrumentIds] varchar(2000) DEFAULT NULL;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[dbxcustomeralertentitlement_insertbulk_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[dbxcustomeralertentitlement_insertbulk_proc](
 @_recordvalues nVARCHAR(max)
)
AS
BEGIN
SET NOCOUNT ON;
	declare @query nvarchar(max)
	declare @msg nvarchar(max) 
	
	BEGIN TRY
	IF  @_recordvalues is not null AND @_recordvalues !='' BEGIN
	
			 SET QUOTED_IDENTIFIER OFF;
	         SET @_recordvalues = REPLACE(@_recordvalues,'"',char(39));		
			 SET QUOTED_IDENTIFIER ON;
			 SET @query =  CONCAT('INSERT INTO  [${dbxschemaname}].dbxcustomeralertentitlement([Customer_id],[alertCategoryId],[AlertTypeId],[alertSubTypeId],[AccountId],[AccountType],[Value1],[Value2],[alertRequestId],[createdby]) VALUES ',@_recordvalues,';');	      
			 EXEC(@query)
	End  
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch
	   
end;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[dbxcustomeralertentitlement_updatebulk_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE PROCEDURE [${dbxschemaname}].[dbxcustomeralertentitlement_updatebulk_proc]( @_updateRecords nvarchar(max)   
)
AS
BEGIN
SET NOCOUNT ON;
  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @numOfParams INTEGER = 0;
  declare @rowValues  nvarchar(max)
  declare @customer_id nvarchar(50)
  declare @alertCategoryId  varchar(50)
  declare @alertTypeId nvarchar(50)
  declare @alertSubTypeId varchar(75)
  declare @accountId nvarchar(50)
  declare @modifiedby nvarchar(50)
  declare @alertRequestId nvarchar(255)
  declare @accountType nvarchar(50)
  declare @value1 nvarchar(255)
  declare @value2 nvarchar(255)
  declare @query nvarchar(max)
  declare @whereCondition nvarchar(max)
  declare @msg nvarchar(max)
  
	BEGIN TRY
    set @numOfRecords = LEN(@_updateRecords) - LEN(REPLACE(@_updateRecords, '|', '')) ;
    set @index1 = @index1 + 1;
	 
	 WHILE (@index1 != @numOfRecords + 1)
		begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_updateRecords, '|', @index1), '|', -1 );
            set @numOfParams = LEN(@rowValues) - LEN(REPLACE(@rowValues, ',', '')) ;
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
			set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );
		    set @alertRequestId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );		         
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('update [${dbxschemaname}].[dbxcustomeralertentitlement] set  [modifiedby] =', QUOTENAME(@modifiedby,''''));
             
			IF @alertRequestId != 'null' BEGIN
                   SET @query = CONCAT( @query,', [alertRequestId] = ', QUOTENAME(@alertRequestId,''''));	
			END  
			
			IF @numOfParams > 7 BEGIN 
              set @value1 = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );			  
              SET @query = CONCAT( @query,', [Value1] = ', QUOTENAME(@value1,''''));	
			END 
		
		   IF @numOfParams > 8 BEGIN
			set @value2 = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',9), ',"', -1 );
			SET @query = CONCAT(@query,', [Value2] = ', QUOTENAME(@value2,''''));
		   END 
			
		   SET @whereCondition =  CONCAT('where [Customer_id] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [AlertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [AccountId] = ',QUOTENAME(@accountId,''''),'  and [AccountType] = ',QUOTENAME(@accountType,''''),' ;');		
		   SET @query = CONCAT(@query,'  ' , @whereCondition);
		 
		   EXEC(@query)

		   SET @index1 = @index1 + 1
        END
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch        
end;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[dbxcustomeralertentitlement_deletebulk_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[dbxcustomeralertentitlement_deletebulk_proc]( @_deleteRecords NVARCHAR(max)  
)
AS
BEGIN
SET NOCOUNT ON;
  
  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @rowValues  nvarchar(max)
  declare @customer_id nvarchar(50)
  declare @alertCategoryId  varchar(50)
  declare @alertTypeId nvarchar(50)
  declare @alertSubTypeId varchar(75)
  declare @accountId nvarchar(50)
  declare @accountType nvarchar(50)
  declare @query nvarchar(max)
  declare @msg nvarchar(max)
  
  
  BEGIN TRY
    set @numOfRecords = LEN(@_deleteRecords) - LEN(REPLACE(@_deleteRecords, '|', '')) + 1;

     set @index1 = @index1 + 1;
	 WHILE (@index1 != @numOfRecords + 1)
	 
       begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_deleteRecords, '|', @index1), '|', -1 );
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
                
          SET @query  =  CONCAT('delete from [${dbxschemaname}].[dbxcustomeralertentitlement] where ' ,'[Customer_id] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [AlertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [AccountId] = ',QUOTENAME(@accountId,''''),'  and [AccountType] = ',QUOTENAME(@accountType,''''),' ;');       
           EXEC(@query)
          SET @index1 = @index1 + 1
        END   
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch   		
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertchannel_insertbulk_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertchannel_insertbulk_proc]( @_recordvalues nVARCHAR(max)
)
AS
BEGIN
SET NOCOUNT ON;
	declare @query nvarchar(max)
	declare @msg nvarchar(max) 
		
	BEGIN TRY
	IF  @_recordvalues is not null AND @_recordvalues !='' BEGIN
	
				SET QUOTED_IDENTIFIER OFF;
	         SET @_recordvalues = REPLACE(@_recordvalues,'"',char(39));		
			 SET QUOTED_IDENTIFIER ON;
			 SET @query =  CONCAT('insert into  [${dbxschemaname}].[customeralertchannel] ( [customerId], [alertCategoryId], [alertTypeId], [alertSubTypeId], [accountId], [accountType],[channelId], [createdby]) values  ',@_recordvalues,';');	      
			 EXEC(@query)
	End  
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch
	   
end;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertchannel_updatebulk_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertchannel_updatebulk_proc] @_updateRecords nvarchar(max) AS
BEGIN
  
  SET  NOCOUNT  ON

  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @rowValues  nvarchar(max);
  declare @customer_id nvarchar(50);
  declare @alertCategoryId  varchar(50);
  declare @alertTypeId nvarchar(50);
  declare @alertSubTypeId varchar(75);
  declare @accountId nvarchar(50);
  declare @modifiedby nvarchar(50);
  declare @accountType nvarchar(50);
  declare @channelId nvarchar(50);
  declare @query nvarchar(max);
  declare @whereCondition nvarchar(max);
  declare @msg nvarchar(max);
  
	BEGIN TRY
    set @numOfRecords = LEN(@_updateRecords) - LEN(REPLACE(@_updateRecords, '|', '')) ;
    set @index1 = @index1 + 1;
	 
	 WHILE (@index1 != @numOfRecords + 1)
		begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_updateRecords, '|', @index1), '|', -1 );
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
			set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );
		    set @channelId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );		         
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('update [${dbxschemaname}].[customeralertchannel] set  [modifiedby] =', QUOTENAME(@modifiedby,''''));
             
			
		   SET @whereCondition =  CONCAT('where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),'  and [channelId] = ',QUOTENAME(@channelId,''''));		
		   SET @query = CONCAT(@query,'  ' , @whereCondition);
		
		   EXEC(@query)

		   SET @index1 = @index1 + 1
        END
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch        
end;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertchannel_deletebulk_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertchannel_deletebulk_proc] @_deleteRecords nvarchar(max) AS
BEGIN
  
  SET  NOCOUNT  ON

  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @rowValues  nvarchar(max);
  declare @customer_id nvarchar(50);
  declare @alertCategoryId  varchar(50);
  declare @alertTypeId nvarchar(50);
  declare @alertSubTypeId varchar(75);
  declare @accountId nvarchar(50);
  declare @accountType nvarchar(50);
  declare @channelId nvarchar(50);
  declare @query nvarchar(max);
  declare @msg nvarchar(max);
  
	BEGIN TRY
    set @numOfRecords = LEN(@_deleteRecords) - LEN(REPLACE(@_deleteRecords, '|', '')) ;
    set @index1 = @index1 + 1;
	 
	 WHILE (@index1 != @numOfRecords + 1)
		begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_deleteRecords, '|', @index1), '|', -1 );
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
			set @channelId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );		         
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('delete from [${dbxschemaname}].[customeralertchannel] where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] =  ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),'  and [channelId] = ',QUOTENAME(@channelId,''''));		
		 
		   EXEC(@query)

		   SET @index1 = @index1 + 1
        END
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch        
end;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertfrequency_insertbulk_proc];
GO


SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertfrequency_insertbulk_proc]( @_recordvalues nVARCHAR(max)
)
AS
BEGIN
SET NOCOUNT ON;
	declare @query nvarchar(max)
	declare @msg nvarchar(max) 
		
	BEGIN TRY
	IF  @_recordvalues is not null AND @_recordvalues !='' BEGIN
	
			 SET QUOTED_IDENTIFIER OFF;
	         SET @_recordvalues = REPLACE(@_recordvalues,'"',char(39));		
			 SET QUOTED_IDENTIFIER ON;
			 SET @query =  CONCAT('INSERT INTO [${dbxschemaname}].[customeralertfrequency] ([customerId], [alertCategoryId], [alertTypeId], [alertSubTypeId], [accountId], [accountType],[alertFrequencyId], [frequencyValue], [frequencyTime], [createdby]) values ',@_recordvalues,';');	      
			
			 EXEC(@query)
	End  
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch
	   
end;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertfrequency_updatebulk_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO


CREATE PROCEDURE [${dbxschemaname}].[customeralertfrequency_updatebulk_proc] @_updateRecords nvarchar(max) AS
BEGIN
  
  SET  NOCOUNT  ON

  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @rowValues  nvarchar(max);
  declare @customer_id nvarchar(50);
  declare @alertCategoryId  varchar(50);
  declare @alertTypeId nvarchar(50);
  declare @alertSubTypeId varchar(75);
  declare @accountId nvarchar(50);
  declare @modifiedby nvarchar(50);
  declare @accountType nvarchar(50);
  declare @alertFrequencyId nvarchar(50);
  declare @frequencyValue nvarchar(50);
  declare @frequencyTime nvarchar(50);
  declare @query nvarchar(max);
  declare @whereCondition nvarchar(max);
  declare @msg nvarchar(max);
  
	BEGIN TRY
    set @numOfRecords = LEN(@_updateRecords) - LEN(REPLACE(@_updateRecords, '|', '')) ;
    set @index1 = @index1 + 1;
	 
	 WHILE (@index1 != @numOfRecords + 1)
		begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_updateRecords, '|', @index1), '|', -1 );
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );					         
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6 ), '"', -1 );
			set @alertFrequencyId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );
		    set @frequencyValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );
			set @frequencyTime = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',9), ',"', -1 );
		    set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
			
			
			IF @frequencyValue != 'null' BEGIN
                     SET @frequencyValue = QUOTENAME(@frequencyValue,'''');	
			END 
			
			IF @frequencyTime != 'null' BEGIN
                     SET @frequencyTime = QUOTENAME(@frequencyTime,'''');	
			END            
                
           	SET @query = CONCAT('UPDATE [${dbxschemaname}].[customeralertfrequency] set  [modifiedby] =', QUOTENAME(@modifiedby,''''),',[alertFrequencyId] = ',QUOTENAME(@alertFrequencyId,''''),',[frequencyValue]=',@frequencyValue,',[frequencyTime]=',@frequencyTime,' where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),' ;');		
			
			EXEC(@query)
		  	   

		   SET @index1 = @index1 + 1
        END
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch        
end;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeralertfrequency_deletebulk_proc];
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertfrequency_deletebulk_proc] @_deleteRecords nvarchar(max) AS
BEGIN
  
  SET  NOCOUNT  ON

  DECLARE @index1 INTEGER = 0;
  declare @numOfRecords INTEGER = 0;
  declare @rowValues  nvarchar(max);
  declare @customer_id nvarchar(50);
  declare @alertCategoryId  varchar(50);
  declare @alertTypeId nvarchar(50);
  declare @alertSubTypeId varchar(75);
  declare @accountId nvarchar(50);
  declare @accountType nvarchar(50);
  declare @query nvarchar(max);
  declare @msg nvarchar(max);
  
	BEGIN TRY
    set @numOfRecords = LEN(@_deleteRecords) - LEN(REPLACE(@_deleteRecords, '|', '')) ;
    set @index1 = @index1 + 1;
	 
	 WHILE (@index1 != @numOfRecords + 1)
		begin
            set @rowValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_deleteRecords, '|', @index1), '|', -1 );
            set @customer_id = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',1 ), '"', -1 );
            set @alertCategoryId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',2), ',"', -1 );
            set @alertTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',3), ',"', -1 );
            set @alertSubTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',4), ',"', -1 );
            set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
			set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('delete from [${dbxschemaname}].[customeralertfrequency] where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] =  ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),' ;');		
		 
		   EXEC(@query)

		   SET @index1 = @index1 + 1
        END
	END TRY
	BEGIN catch
	  SET @msg=(SELECT ERROR_MESSAGE())
	  SELECT @msg as ErrorMessage;
	END catch        
end;
GO

ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD [PublicKeyServiceURL] NVARCHAR(MAX) NULL;
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD [CertificateEncryptionKey] NVARCHAR(MAX) NULL;
ALTER TABLE [${dbxschemaname}].[backendcertificate] ADD [JWSAlgorithm] NVARCHAR(MAX) NULL;

GO

DROP TABLE IF EXISTS [${dbxschemaname}].[excludedcustomeraccounts];

CREATE TABLE [${dbxschemaname}].[excludedcustomeraccounts] (
	id nvarchar(50) NOT NULL,
	Customer_id nvarchar(50) DEFAULT (NULL),
	Membership_id nvarchar(50) DEFAULT (NULL),
	Account_id nvarchar(50) DEFAULT (NULL),
	Organization_id nvarchar(45) DEFAULT (NULL),
	AccountName nvarchar(50) DEFAULT (NULL),
	FavouriteStatus int DEFAULT ((0)) NOT NULL,
	IsViewAllowed bit DEFAULT ((0)) NOT NULL,
	IsDepositAllowed bit DEFAULT ((0)) NOT NULL,
	IsWithdrawAllowed bit DEFAULT ((0)) NOT NULL,
	IsOrganizationAccount bit DEFAULT ((0)) NOT NULL,
	IsOrgAccountUnLinked bit DEFAULT ((0)),
	createdby nvarchar(50) DEFAULT (NULL),
	modifiedby nvarchar(50) DEFAULT (NULL),
	createdts datetime DEFAULT (NULL),
	lastmodifiedts datetime DEFAULT (NULL),
	contractId varchar(20) DEFAULT (NULL),
	coreCustomerId varchar(20) DEFAULT (NULL),
	isBusinessAccount varchar(20) DEFAULT (NULL),
	email varchar(50) DEFAULT (NULL),
	EStatementmentEnable smallint DEFAULT ((0)),
	accountType varchar(50),
	CONSTRAINT PK_excludedcustomeraccounts_id PRIMARY KEY (id)
);
GO


DROP TABLE IF EXISTS [${dbxschemaname}].[suspendedcustomers];

CREATE TABLE [${dbxschemaname}].[suspendedcustomers] (
  [id] NVARCHAR(50) NOT NULL,
  [contractId] VARCHAR(50) DEFAULT NULL,
  [customerId] NVARCHAR(50) DEFAULT NULL,
  [coreCustomerId] VARCHAR(45) DEFAULT NULL,
  [createdby] VARCHAR(50) DEFAULT NULL,
  [modifiedby] VARCHAR(50) DEFAULT NULL,
  [createdts] datetime2 NULL DEFAULT NULL,
  [lastmodifiedts] datetime2 NULL DEFAULT NULL,
  [synctimestamp] datetime2 NULL DEFAULT NULL,
  [softdeleteflag] bit NOT NULL DEFAULT 0,
  CONSTRAINT PK_suspendedcustomers_id PRIMARY KEY (id)
);

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customer_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customer_contract_delete_proc]  
   @customerId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
		DECLARE @contract_statement nvarchar(max);
		DECLARE @suspended_statement nvarchar(max);
		DECLARE @accounts_statement nvarchar(max);
		DECLARE @group_statement nvarchar(max);
		DECLARE @action_statement nvarchar(max);
		DECLARE @excluded_accounts_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomers] where';
		SET @suspended_statement = N'DELETE FROM [${dbxschemaname}].[suspendedcustomers] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customeraccounts] where';
		SET @excluded_accounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomeraccounts] where';
		SET @group_statement = N'DELETE FROM [${dbxschemaname}].[customergroup] where';
		SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customeraction] where';
		SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where';

		SET @where_clause = '';
		SET @where_clause1 = '';
		if(@customerId != '') 
		BEGIN
			SET @where_clause = @where_clause + (N' customerId = ') + ((QUOTENAME((@customerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' Customer_id = ') + ((QUOTENAME((@customerId), '''')))
		END
		
		IF(@contractId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause1 = @where_clause1 + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
		END
		
		IF(@coreCustomerId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
		END
		
		IF(@where_clause != '')
		BEGIN	
			SET @contract_statement = @contract_statement + @where_clause;
			SET @suspended_statement = @suspended_statement + @where_clause;
			SET @accounts_statement = @accounts_statement + @where_clause1;
			SET @excluded_accounts_statement = @excluded_accounts_statement + @where_clause1;			
			SET @group_statement = @group_statement + @where_clause1;	
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause1;	
		
			exec(@contract_statement);
			exec(@suspended_statement);
			exec(@accounts_statement);
			exec(@excluded_accounts_statement);
			exec(@group_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[excludedcustomroleaccounts];
GO
CREATE TABLE [${dbxschemaname}].[excludedcustomroleaccounts] (
	id varchar(50) NOT NULL,
	customRoleId varchar(50) DEFAULT (NULL),
	Account_id varchar(50) DEFAULT (NULL),
	AccountName varchar(50) DEFAULT (NULL),
	accountType varchar(50) DEFAULT (NULL),
	contractId varchar(50) DEFAULT (NULL),
	coreCustomerId varchar(45) DEFAULT (NULL),
	createdby varchar(50) DEFAULT (NULL),
	modifiedby varchar(50) DEFAULT (NULL),
	createdts datetime2(7) DEFAULT (NULL),
	lastmodifiedts datetime2(7) DEFAULT (NULL),
	synctimestamp datetime2(7) DEFAULT (NULL),
	softdeleteflag bit DEFAULT ((0)) NOT NULL,
	CONSTRAINT PK__customro__3213E83FC1DF4F66 PRIMARY KEY (id)
);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customrole_contract_delete_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customrole_contract_delete_proc]  
   @customRoleId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max)
AS 
   BEGIN
		DECLARE @contract_statement nvarchar(max);
		DECLARE @accounts_statement nvarchar(max);
		DECLARE @excludedaccounts_statement nvarchar(max);
		DECLARE @action_statement nvarchar(max);
		DECLARE @limitgroup_statement nvarchar(max);
		DECLARE @where_clause nvarchar(max);
		DECLARE @where_clause1 nvarchar(max);
		DECLARE @where_clause2 nvarchar(max);
		
		SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomrole] where';
		SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customroleaccounts] where ';
		SET @excludedaccounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomroleaccounts] where';
		SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customroleactionlimits] where';
		SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where';

		SET @where_clause = '';
		SET @where_clause1 = '';
		if(@customRoleId != '') 
		BEGIN
			SET @where_clause = @where_clause + (N' customRoleId = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause1 = @where_clause1 + (N' customRole_id = ') + ((QUOTENAME((@customRoleId), '''')))
			SET @where_clause2 = @where_clause2 + (N' Customer_id = ') + ((QUOTENAME((@customRoleId), '''')))
			
		END
		
		IF(@contractId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause1 = @where_clause1 + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
			SET @where_clause2 = @where_clause2 + (N' contractId = ') + ((QUOTENAME((@contractId), '''')))
		END
		
		IF(@coreCustomerId != '') 
		BEGIN
			IF(@where_clause != '')
			BEGIN
				SET @where_clause = @where_clause + (N' AND ')
				SET @where_clause1 = @where_clause1 + (N' AND ')
				SET @where_clause2 = @where_clause2 + (N' AND ')
			END
			SET @where_clause = @where_clause + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause1 = @where_clause1 + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
			SET @where_clause2 = @where_clause2 + (N' coreCustomerId = ') + ((QUOTENAME((@coreCustomerId), '''')))
		END
		
		IF(@where_clause != '')
		BEGIN	
			SET @contract_statement = @contract_statement + @where_clause;	
			SET @accounts_statement = @accounts_statement + @where_clause;
			SET @excludedaccounts_statement = @excludedaccounts_statement + @where_clause;
			SET @action_statement = @action_statement + @where_clause1;	
			SET @limitgroup_statement = @limitgroup_statement + @where_clause2;	
		
			exec(@contract_statement);
			exec(@accounts_statement);
			exec(@excludedaccounts_statement);
			exec(@action_statement);
			exec(@limitgroup_statement)
		END
   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[customeraction_save_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeraction_save_proc]  @_queryInput nvarchar(max)
	AS 
	BEGIN
		SET  XACT_ABORT  ON
		SET  NOCOUNT  ON
		DECLARE @recordRow nvarchar(max)
		DECLARE @recordsData nvarchar(max)
		DECLARE @query nvarchar(max)
		set @_queryInput = REPLACE(@_queryInput,'\','')
	  	set @_queryInput = REPLACE(@_queryInput,'"','''')
		DECLARE actions CURSOR LOCAL
		FOR (SELECT value FROM STRING_SPLIT(@_queryInput, '|'));
		OPEN actions
		FETCH NEXT FROM actions into @recordRow
		WHILE (@@FETCH_STATUS=0)
		BEGIN
			SET @recordsData = 'N'''+CAST(newid() as nvarchar(max))+''','+@recordRow
  			SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value) VALUES (') + (@recordsData) + (N')')
			EXEC(@query)
			FETCH NEXT FROM actions into @recordRow
		   	CONTINUE
        END
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
        customer.Ssn AS Ssn,
        customer.Status_id AS Status_id
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
		and primaryphone.Customer_id = primaryemail.Customer_id  UNION 
		 SELECT 
         customer.id AS id,
        customer.FirstName AS FirstName,
        customer.MiddleName AS MiddleName,
        customer.LastName AS LastName,
        customer.UserName AS UserName,
        customer.Gender AS Gender,
        customer.DateOfBirth AS DateOfBirth,
        customer.Ssn AS Ssn,
        customer.Status_id AS Status_id
    FROM [${dbxschemaname}].customer where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id ,@usersList) = '1'
		
	END
GO

DROP TABLE IF EXISTS [${dbxschemaname}].[favouriteinstruments];
CREATE TABLE [${dbxschemaname}].[favouriteinstruments] (
[id] int NOT NULL IDENTITY,
[userId] nvarchar(50) DEFAULT NULL,
[customerId] nvarchar(50) DEFAULT NULL,
[favInstrumentIds] nvarchar(2000) DEFAULT NULL,
[favInstrumentCodes] nvarchar(2000) DEFAULT NULL,
[softdeleteflag] bit DEFAULT 0,
PRIMARY KEY ([id]),
CONSTRAINT FavoInstruments_UserId UNIQUE ([userId]),
CONSTRAINT [FK_FavoInstruments_UserId] FOREIGN KEY ([userId]) REFERENCES [${dbxschemaname}].[customer] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
);
GO

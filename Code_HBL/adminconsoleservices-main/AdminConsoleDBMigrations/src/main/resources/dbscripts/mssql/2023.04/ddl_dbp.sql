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
			 SET @query =  CONCAT('insert into  [${dbxschemaname}].[customeralertchannel] ( [customerId], [alertCategoryId], [alertTypeId], [alertSubTypeId], [accountId], [accountType],[channelId], [createdby],[companyLegalUnit]) values  ',@_recordvalues,';');	      
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
  declare @companyLegalUnit nvarchar(max);
  
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
            
            
			set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );
		    set @channelId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );	
		    set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );		         
		    	         
            set @companyLegalUnit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('update [${dbxschemaname}].[customeralertchannel] set  [modifiedby] =', QUOTENAME(@modifiedby,''''));
		   SET @whereCondition =  CONCAT('where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [companyLegalUnit] = ',QUOTENAME(@companyLegalUnit,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),'  and [channelId] = ',QUOTENAME(@channelId,''''));		
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
  declare @createdby nvarchar(max);
  declare @companyLegalUnit nvarchar(max);
  
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
            
            
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );	
			set @channelId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );	
			set @createdby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );		         
				         
            set @companyLegalUnit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('delete from [${dbxschemaname}].[customeralertchannel] where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] =  ',QUOTENAME(@alertCategoryId,''''),'  and [companyLegalUnit] =  ',QUOTENAME(@companyLegalUnit,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),'  and [channelId] = ',QUOTENAME(@channelId,''''));		
		 
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
			 SET @query =  CONCAT('INSERT INTO [${dbxschemaname}].[customeralertfrequency] ([customerId], [alertCategoryId], [alertTypeId], [alertSubTypeId], [accountId], [accountType],[alertFrequencyId], [frequencyValue], [frequencyTime], [createdby],[companyLegalUnit]) values ',@_recordvalues,';');	      
			
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
  declare @companylegalunit nvarchar(max);
  
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
			
			set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',10), ',"', -1 );
		    set @companylegalunit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
			
			
			IF @frequencyValue != 'null' BEGIN
                     SET @frequencyValue = QUOTENAME(@frequencyValue,'''');	
			END 
			
			IF @frequencyTime != 'null' BEGIN
                     SET @frequencyTime = QUOTENAME(@frequencyTime,'''');	
			END            
                
           	SET @query = CONCAT('UPDATE [${dbxschemaname}].[customeralertfrequency] set  [modifiedby] =', QUOTENAME(@modifiedby,''''),',[alertFrequencyId] = ',QUOTENAME(@alertFrequencyId,''''),',[companyLegalUnit] = ',QUOTENAME(@companyLegalUnit,''''),',[frequencyValue]=',@frequencyValue,',[frequencyTime]=',@frequencyTime,' where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),' ;');		
			
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
  declare @alertFrequencyId nvarchar(max);
  declare @frequencyValue nvarchar(max);
  declare @frequencyTime nvarchar(max);
  declare @modifiedby nvarchar(max);
  declare @companylegalunit nvarchar(max);
  
  
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
            
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );
            set @alertFrequencyId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',7), ',"', -1 );
            set @frequencyValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );
            set @frequencyTime = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',9), ',"', -1 );
            set @modifiedby = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',10), ',"', -1 );
            
			set @companylegalunit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
           	SET @query = CONCAT('delete from [${dbxschemaname}].[customeralertfrequency] where [customerId] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] =  ',QUOTENAME(@alertCategoryId,''''),'  and [companyLegalUnit] =  ',QUOTENAME(@companyLegalUnit,''''),'  and [alertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [accountId] = ',QUOTENAME(@accountId,''''),'  and [accountType] = ',QUOTENAME(@accountType,''''),' ;');		
		 
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
			 SET @query =  CONCAT('INSERT INTO  [${dbxschemaname}].dbxcustomeralertentitlement([Customer_id],[alertCategoryId],[AlertTypeId],[alertSubTypeId],[AccountId],[AccountType],[Value1],[Value2],[alertRequestId],[createdby],[companyLegalUnit]) VALUES ',@_recordvalues,';');	      
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
  declare @companylegalunit nvarchar(max)
  
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
		    
            set @value1 = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',8), ',"', -1 );	
            set @value2 = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',9), ',"', -1 );	
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',10), ',"', -1 );	
            
            set @companylegalunit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
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
			
		   SET @whereCondition =  CONCAT('where [Customer_id] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [companyLegalUnit] = ',QUOTENAME(@companyLegalUnit,''''),'  and [AlertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [AccountId] = ',QUOTENAME(@accountId,''''),'  and [AccountType] = ',QUOTENAME(@accountType,''''),' ;');		
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
  declare @companylegalunit nvarchar(max)
  
  
  
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
            set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',5), ',"', -1 );
            
            set @companylegalunit = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
                
          SET @query  =  CONCAT('delete from [${dbxschemaname}].[dbxcustomeralertentitlement] where ' ,'[Customer_id] = ',QUOTENAME(@customer_id,''''),'  and [alertCategoryId] = ',QUOTENAME(@alertCategoryId,''''),'  and [companyLegalUnit] = ',QUOTENAME(@companyLegalUnit,''''),'  and [AlertTypeId] = ',QUOTENAME(@alertTypeId,''''),'  and [alertSubTypeId] = ',QUOTENAME(@alertSubTypeId,''''),'  and [AccountId] = ',QUOTENAME(@accountId,''''),'  and [AccountType] = ',QUOTENAME(@accountType,''''),' ;');       
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

DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getEntitleMentsNocustomer]
GO

CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getEntitleMentsNocustomer]
@alerttypes nvarchar(max)
AS
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2,companyLegalUnit as companyLegalUnit FROM dbxcustomeralertentitlement where   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId,@alerttypes)   <> 0
END

GO

DROP procedure IF EXISTS [${dbxschemaname}].[subscriber_getEntitleMentsWithcustomer]
GO

CREATE  PROCEDURE [${dbxschemaname}].[subscriber_getEntitleMentsWithcustomer]
@alerttypes nvarchar(max),
@custids nvarchar(max)
AS
BEGIN
SELECT Customer_id as customerid , AlertTypeId as alerttypeid , alertSubTypeId as alertsubtypeid , AccountId as accountid, AccountType as accounttype, Value1 as value1, Value2 as value2,companyLegalUnit as companyLegalUnit FROM dbxcustomeralertentitlement where   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.AlertTypeId,@alerttypes) <> 0  and
   [${dbxschemaname}].FIND_IN_SET(dbxcustomeralertentitlement.Customer_id,@custids) <> 0
END

GO

ALTER TABLE [${dbxschemaname}].[contract] ALTER COLUMN [name] nvarchar(200);
GO
ALTER TABLE [${dbxschemaname}].[contractcorecustomers] ALTER COLUMN [coreCustomerName] varchar(200);
GO
ALTER TABLE [${dbxschemaname}].[contractaccounts] ALTER COLUMN [accountName] varchar(200);
GO
ALTER TABLE [${dbxschemaname}].[accounts] ALTER COLUMN [MembershipName] varchar(200);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_limitgroup_limits_create_proc];
GO
CREATE PROCEDURE [${dbxschemaname}].user_limitgroup_limits_create_proc  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_legalEntityId nvarchar(50)
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
         @max_per_transaction_single_paymentnum DECIMAL(20,2)     
         
      DECLARE
         @max_per_transaction_bulk_payment nvarchar(max) = N''
         
      DECLARE
         @max_per_transaction_bulk_paymentnum DECIMAL(20,2)     
            
      DECLARE
         @max_daily_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_daily_limit_single_paymentnum DECIMAL(20,2)     
           
      DECLARE
         @max_daily_limit_bulk_payment nvarchar(max) = N''
         
      DECLARE
         @max_daily_limit_bulk_paymentnum DECIMAL(20,2)     
     
      DECLARE
         @max_weekly_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_weekly_limit_single_paymentnum DECIMAL(20,2)     
         
      DECLARE
         @max_weekly_limit_bulk_payment nvarchar(max) = N''   
         
     DECLARE
         @max_weekly_limit_bulk_paymentnum DECIMAL(20,2)  
         
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
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
      set @max_per_transaction_single_paymentnum = cast(@max_per_transaction_single_payment AS DECIMAL(20,2)) 
      
      SET @max_per_transaction_bulk_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
       set @max_per_transaction_bulk_paymentnum = cast(@max_per_transaction_bulk_payment AS DECIMAL(20,2))
       
       SET @max_daily_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions ) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
               
        set @max_daily_limit_single_paymentnum = cast(@max_daily_limit_single_payment AS DECIMAL(20,2))
               
        SET @max_daily_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
            [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
      
        
        set @max_daily_limit_bulk_paymentnum = cast(@max_daily_limit_bulk_payment AS DECIMAL(20,2))
               
        SET @max_weekly_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
         
         
         set @max_weekly_limit_single_paymentnum = cast(@max_weekly_limit_single_payment AS DECIMAL(20,2))
          
         SET @max_weekly_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
			   [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value is NOT NULL)
         
         set @max_weekly_limit_bulk_paymentnum = cast(@max_weekly_limit_bulk_payment AS DECIMAL(20,2))
         
		IF @max_per_transaction_single_payment != ''
			INSERT [${dbxschemaname}].customerlimitgrouplimits(
			[${dbxschemaname}].customerlimitgrouplimits.id, 
			[${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
			[${dbxschemaname}].customerlimitgrouplimits.contractId,
			[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
			[${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
			[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
			[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
			[${dbxschemaname}].customerlimitgrouplimits.value
			) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, @_legalEntityId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_paymentnum)
		 
		IF @max_per_transaction_bulk_payment != ''                    
			INSERT [${dbxschemaname}].customerlimitgrouplimits(
			[${dbxschemaname}].customerlimitgrouplimits.id, 
			[${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
			[${dbxschemaname}].customerlimitgrouplimits.contractId,
			[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
			[${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
			[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
			[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
			[${dbxschemaname}].customerlimitgrouplimits.value
			) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, @_legalEntityId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_paymentnum)
		 
		IF @max_daily_limit_single_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits(
		 [${dbxschemaname}].customerlimitgrouplimits.id, 
		 [${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
		 [${dbxschemaname}].customerlimitgrouplimits.contractId,
		 [${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
		 [${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
		 [${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
		 [${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
		 [${dbxschemaname}].customerlimitgrouplimits.value
		 ) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId,  @_legalEntityId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_paymentnum)

		IF @max_daily_limit_bulk_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits(
		 [${dbxschemaname}].customerlimitgrouplimits.id, 
		 [${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
		 [${dbxschemaname}].customerlimitgrouplimits.contractId,
		 [${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
		 [${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
		 [${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
		 [${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
		 [${dbxschemaname}].customerlimitgrouplimits.value
		 ) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId,  @_legalEntityId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_paymentnum)
							 
		IF @max_weekly_limit_single_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits(
		 [${dbxschemaname}].customerlimitgrouplimits.id, 
		 [${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
		 [${dbxschemaname}].customerlimitgrouplimits.contractId,
		 [${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
		 [${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
		 [${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
		 [${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
		 [${dbxschemaname}].customerlimitgrouplimits.value
		 ) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId,  @_legalEntityId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_paymentnum)
							 
		IF @max_weekly_limit_bulk_payment != ''                             
         INSERT [${dbxschemaname}].customerlimitgrouplimits(
		 [${dbxschemaname}].customerlimitgrouplimits.id, 
		 [${dbxschemaname}].customerlimitgrouplimits.Customer_id, 
		 [${dbxschemaname}].customerlimitgrouplimits.contractId,
		 [${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,
		 [${dbxschemaname}].customerlimitgrouplimits.companyLegalUnit,
		 [${dbxschemaname}].customerlimitgrouplimits.limitGroupId,
		 [${dbxschemaname}].customerlimitgrouplimits.LimitType_id,
		 [${dbxschemaname}].customerlimitgrouplimits.value
		 ) VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId,  @_legalEntityId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_paymentnum)


   END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[useraccounts_create_proc];
GO

CREATE PROCEDURE [${dbxschemaname}].[useraccounts_create_proc]  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_legalEntityId nvarchar(50)
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
         @accounttypeid varchar(255)

	  DECLARE
         @accounttypename varchar(255)

      DECLARE
          accountData CURSOR LOCAL FORWARD_ONLY FOR 
             (   
                  SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE 
                               [${dbxschemaname}].contractaccounts.contractId = @_contractId AND
                               [${dbxschemaname}].contractaccounts.coreCustomerId = @_coreCustomerId AND
							   [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId AND
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
			   SET @id = (SELECT left(newid(), 50))
               SET @accounttypeid = (SELECT [${dbxschemaname}].contractaccounts.typeId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.accountId = accountID);
				SET @accounttypename = (select [${dbxschemaname}].accounttype.TypeDescription from [${dbxschemaname}].accounttype WHERE [${dbxschemaname}].accounttype.TypeID  =@accounttypeid);
                  INSERT [${dbxschemaname}].customeraccounts(
                     id, 
                     Customer_id,                     
					 Account_id, 
                     contractId, 
                     coreCustomerId,
					 companyLegalUnit,
					 accountType)
                     VALUES (
                        @id, 
                        @_userId, 
                        @accountID, 
                        @_contractId, 
                        @_coreCustomerId,
						@_legalEntityId,
						@accounttypename)
                 

               END
            FETCH NEXT FROM accountData into @accountID
            CONTINUE

         END
      CLOSE accountData
      DEALLOCATE accountData

   END
GO

-- Disable the constraints for feature/featureaction related tables 
ALTER TABLE [${dbxschemaname}].[compositeaction] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[featureroletype] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[featuredisplaynamedescription] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[featureaction] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[actionlimit] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[actiondisplaynamedescription] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[featureactionroletype] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[servicedefinitionactionlimit] NOCHECK CONSTRAINT ALL; 
ALTER TABLE [${dbxschemaname}].[groupactionlimit] NOCHECK CONSTRAINT ALL; 
GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [${dbxschemaname}].[accountLevelActionLimit](
	[id] [nvarchar](50) NOT NULL,
	[contractId] [nvarchar](50) NOT NULL,
	[coreCustomerId] [nvarchar](50) NOT NULL,
	[policyId] [nvarchar](50) NULL,
	[isPortfolio] [nvarchar](50) NOT NULL,
	[accountId] [nvarchar](50) NULL,
	[featureId] [nvarchar](50) NOT NULL,
	[actionId] [nvarchar](255) NOT NULL,
	[limitGroupId] [nvarchar](50) NULL,
	[limitTypeId] [nvarchar](50) NULL,
	[value] [decimal](20, 2) NULL,
	[isNewAction] [smallint] NOT NULL,
	[createdby] [nvarchar](50) NULL,
	[modifiedby] [nvarchar](50) NULL,
	[createdts] datetime  NOT NULL DEFAULT GETDATE(),
    [lastmodifiedts] datetime  NOT NULL DEFAULT GETDATE(),
    [synctimestamp] datetime  NOT NULL DEFAULT GETDATE(),
	[softdeleteflag] [smallint] NULL,
	[companyLegalUnit] [nvarchar](50) NOT NULL,
	
	CONSTRAINT PK_accountLevelActionLimit_id PRIMARY KEY ([id]),
    CONSTRAINT [FK_accountLevelActionLimit_contract_contractId] FOREIGN KEY ([contractId]) REFERENCES [${dbxschemaname}].[contract] ([id]) ON DELETE NO ACTION ON UPDATE NO ACTION
) 
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
  CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_contract_delete_proc]  
   @customerId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max),
   @legalEntityId nvarchar(50)
AS 
   BEGIN
              DECLARE @contract_statement nvarchar(max);
              DECLARE @suspended_statement nvarchar(max);
              DECLARE @accounts_statement nvarchar(max);
              DECLARE @group_statement nvarchar(max);
              DECLARE @action_statement nvarchar(max);
              DECLARE @excluded_action_statement nvarchar(max);
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
              SET @excluded_action_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomeraction] where';
              SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where';

 

              SET @where_clause = '';
              SET @where_clause1 = '';

			  SET @where_clause = @where_clause + (N' companyLegalUnit = ') + ((QUOTENAME((@legalEntityId), ''''))) + (N' AND ')
			  
			  SET @where_clause1 = @where_clause1 + (N' companyLegalUnit = ') + ((QUOTENAME((@legalEntityId), ''''))) + (N' AND ')
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
                     SET @excluded_action_statement = @excluded_action_statement + @where_clause1;
                     SET @limitgroup_statement = @limitgroup_statement + @where_clause1;     

                     exec(@contract_statement);
                     exec(@suspended_statement);
                     exec(@accounts_statement);
                     exec(@excluded_accounts_statement);
                     exec(@group_statement);
                     exec(@action_statement);
                     exec(@excluded_action_statement);
                     exec(@limitgroup_statement)
              END
   END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customeraction_save_proc]  @_queryInput nvarchar(max)
    AS 
    BEGIN
        SET  XACT_ABORT  ON
        SET  NOCOUNT  ON
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
              SET @query = ('INSERT INTO [${dbxschemaname}].customeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed,limitGroupId,limitType_id,value,companyLegalUnit) VALUES (') + (@recordsData) + (N')')
            EXEC(@query)
            FETCH NEXT FROM actions into @recordRow
               CONTINUE
        END
   END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[excluded_customeraction_save_proc]  @_queryInput nvarchar(max)
       AS 
       BEGIN
              SET  XACT_ABORT  ON
              SET  NOCOUNT  ON
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
                    SET @query = ('INSERT INTO [${dbxschemaname}].excludedcustomeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id,companyLegalUnit) VALUES (') + (@recordsData) + (N')')
                     EXEC(@query)
                     FETCH NEXT FROM actions into @recordRow
                     CONTINUE
        END
   END;
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contract_features_create_proc]  
   @_features nvarchar(max),
   @_contractId nvarchar(50),
   @_customerId nvarchar(50),
   @_serviceTypeId nvarchar(50),
   @_defaultActionsEnabled nvarchar(50),
   @_legalEntityId nvarchar(50)
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
            ON (featureroletype.Feature_id = feature.id AND featureroletype.RoleType_id = @_serviceTypeId and feature.companyLegalUnit = featureroletype.companyLegalUnit)
            WHERE (feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET(feature.id, @_features) > 0 AND feature.companyLegalUnit = @_legalEntityId))

      SET @features_list = CASE  WHEN (@features_list IS NULL) THEN N'' ELSE @features_list END

         DECLARE features CURSOR LOCAL FOR 
             ( SELECT feature.id
               FROM [${dbxschemaname}].feature
               WHERE [${dbxschemaname}].FIND_IN_SET(feature.id, @features_list) > 0 
			   AND feature.companyLegalUnit = @_legalEntityId
             )

 OPEN features
	  FETCH NEXT FROM features INTO @featureId
      WHILE (@@FETCH_STATUS=0)
			BEGIN
			   SET @id = (SELECT left(newid(), 50))
			   INSERT [${dbxschemaname}].contractfeatures([${dbxschemaname}].contractfeatures.id, [${dbxschemaname}].contractfeatures.contractId, [${dbxschemaname}].contractfeatures.coreCustomerId,[${dbxschemaname}].contractfeatures.featureId,[${dbxschemaname}].contractfeatures.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@_legalEntityId)
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

SET @servicedefinitionId = (SELECT contract.servicedefinitionId from [${dbxschemaname}].contract WHERE contract.id = @_contractId and contract.companyLegalUnit = @_legalEntityId)

SET @validServicedefinitionActions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                                     servicedefinitionactionlimit.serviceDefinitionId = @servicedefinitionId AND
                                     [${dbxschemaname}].FIND_IN_SET(servicedefinitionactionlimit.actionId, @validFIActions) > 0)                                   


DECLARE actions CURSOR LOCAL FOR 
             ( SELECT featureaction.id
               FROM [${dbxschemaname}].featureaction
               WHERE [${dbxschemaname}].FIND_IN_SET(featureaction.id, @validServicedefinitionActions) > 0
			   AND featureaction.companyLegalUnit = @_legalEntityId
             )
             
IF ISNULL(@_defaultActionsEnabled,'')<>'' AND  @_defaultActionsEnabled = 'true'
BEGIN  
OPEN actions
    FETCH NEXT FROM actions INTO @featureActionId 
    WHILE (@@FETCH_STATUS=0)
		BEGIN
		    SET @entryStatus = 0;
	        SET @featureId = (SELECT featureaction.Feature_id from [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId
			AND featureaction.companyLegalUnit = @_legalEntityId)
          	 DECLARE limits CURSOR LOCAL FOR 
             ( SELECT actionlimit.LimitType_id
               FROM [${dbxschemaname}].actionlimit
               WHERE actionlimit.Action_id = @featureActionId 
             )
            OPEN limits
                 FETCH NEXT FROM limits INTO @limitId 
                 WHILE (@@FETCH_STATUS=0)
		         BEGIN
                     SET @limitvalue = (SELECT actionlimit.value from [${dbxschemaname}].actionlimit WHERE actionlimit.Action_id = @featureActionId
                     AND actionlimit.LimitType_id = @limitId
					 AND actionlimit.companyLegalUnit = @_legalEntityId)
			                                       
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
		               INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.limitTypeId,[${dbxschemaname}].contractactionlimit.value,[${dbxschemaname}].contractactionlimit.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@limitId,@limitvalue,@_legalEntityId)
                
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
				     INSERT [${dbxschemaname}].contractactionlimit([${dbxschemaname}].contractactionlimit.id, [${dbxschemaname}].contractactionlimit.contractId, [${dbxschemaname}].contractactionlimit.coreCustomerId,[${dbxschemaname}].contractactionlimit.featureId,[${dbxschemaname}].contractactionlimit.actionId,[${dbxschemaname}].contractactionlimit.companyLegalUnit)
                             VALUES (@id,@_contractId,@_customerId,@featureId,@featureActionId,@_legalEntityId)
				  END
            
       FETCH NEXT FROM actions INTO @featureActionId 
       CONTINUE
       END
 CLOSE actions
 DEALLOCATE actions
 END
END
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contract_action_limit_save_proc]
@_queryInput VARCHAR(max)
AS
   BEGIN

     SET  XACT_ABORT  ON

     SET  NOCOUNT  ON

      DECLARE @index int
       DECLARE @numOfRecords int
       DECLARE @recordsData nvarchar(max)
       DECLARE @accountId varchar(max)
       DECLARE @query nvarchar(max)

     set @index = 0;
     set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1;
	 set @_queryInput = REPLACE (@_queryInput,'"','''');
     select @numOfRecords;
     WHILE (1 = 1)
      
         BEGIN

         set @index = @index + 1;
          IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN
		  
           set @recordsData = concat('newid()',',', [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, '|', @index), '|', -1 ));
            set @accountId = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@recordsData, ''',',4 ), '''', -1 );
             select @recordsData as inputstat;
             select @accountId;
              IF(@accountId='')
               BEGIN
               set @query = concat('INSERT INTO [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value,companyLegalUnit) VALUES (',@recordsData,');');
               END
              ELSE
                 BEGIN
                 set @query = concat('INSERT INTO [${dbxschemaname}].accountlevelactionlimit(id,contractId,coreCustomerId,isPortfolio,accountId,featureId,actionid,limitGroupId,limitTypeId,value,companyLegalUnit) VALUES (',@recordsData,');');
                 END
            EXEC(@query)

           END

           END;
           
      END;

GO

ALTER TABLE [${dbxschemaname}].[accountLevelActionLimit] ADD  DEFAULT 0 FOR [isNewAction];
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
  DECLARE @accid NVARCHAR(max);
  DECLARE @featureId NVARCHAR(max) ;
  DECLARE @isNewAction NVARCHAR(255) ;
  DECLARE @actionId NVARCHAR(max) ;
  DECLARE @limitTypeId NVARCHAR(max) ;
  DECLARE @limitValue NVARCHAR(max);
  DECLARE @legalEntityId NVARCHAR(max);
  DECLARE @num DECIMAL(20,2);
  
  SET @numOfRecords = LEN(@_contractActionLimit) - LEN(REPLACE(@_contractActionLimit, '|', '')) + 1;
  SET @_contractActionLimit = REPLACE(@_contractActionLimit, '"', '');
  WHILE 1=1 BEGIN
     SET @index1 = @index1 + 1;
        IF @index1 = @numOfRecords + 1 BEGIN
            break ;
        END
        ELSE BEGIN
            SET @contractValues = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_contractActionLimit, '|', @index1), '|', -1 );
            SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',1),',',-1);
            SET @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',2),',',-1);
			SET @accid = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',3),',',-1);
            SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',4),',',-1);
            SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',5),',',-1);
			SET @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',6),',',-1);
            SET @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',7),',',-1);
			SET @limitTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',8),',',-1);
            SET @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',-1),',',-1);
            SET @num = cast(@limitValue AS DECIMAL(20,2))
            UPDATE [${dbxschemaname}].contractactionlimit SET value = @num where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId AND companyLegalUnit = @legalEntityId;
        END

    END ;
END;
GO


GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS
   BEGIN -- proc level
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON

	  DECLARE @index int
	  DECLARE @query nvarchar(max)
	  DECLARE @numOfRecords int
	  DECLARE @id nvarchar(255) = N''
      DECLARE @tempLimitValue DECIMAL(20,2)
      DECLARE @group_concat_max_len bigint
	  DECLARE @serviceDefinitionId nvarchar(255) = N''
	  DECLARE @recordsData nvarchar(max) = N''
	  DECLARE @contractId nvarchar(max) = N''
	  DECLARE @customerId nvarchar(max) = N''
	  DECLARE @accountId nvarchar(max) = N''
	  DECLARE @featureId nvarchar(max) = N''
	  DECLARE @actionId nvarchar(max) = N''
	  DECLARE @isNewAction nvarchar(max)= N''
	  DECLARE @legalEntityId nvarchar(max)= N''
	  DECLARE @limitId nvarchar(max) = N''
	  DECLARE @limitValue nvarchar(max) = N''
	  DECLARE @limitAtFI nvarchar(max) = N''
	  DECLARE @contarctFeatures nvarchar(max) = N''  
	  DECLARE @limitATServiceDefinition nvarchar(max) = N''
	  DECLARE @recordsDataWithoutLimits nvarchar(max) = N'' 
	  DECLARE @serviceDefinitionActions nvarchar(max) = N'' 
	  DECLARE @existingActionLimitRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords nvarchar(max) = N'' 
	  DECLARE @existingActionRecords1 nvarchar(max) = N''
	  DECLARE @existingAccountActionLimitRecords nvarchar(max) = N''

	  set @index = 0;
	  set @numOfRecords = LEN(@_queryInput) - LEN(REPLACE(@_queryInput, '|', '')) + 1; 
	  SET @_queryInput = REPLACE(@_queryInput, '"', ''''); 

	    WHILE (1 = 1)
      
         BEGIN -- while level

         set @index = @index + 1;

		  IF @index = @numOfRecords + 1
              BREAK

         ELSE
          
          BEGIN -- outermost level

		   set @recordsData = concat('''',newid(),'''',',', [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, '|', @index), '|', -1 ));
		   set @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',2 ), '''', -1 );
		   set @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',3 ), ',''', -1 );   
		   set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',4 ), ',''', -1 ); 
		   set @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',5), ',''', -1 );           
	       set @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',6), ',''', -1 );           
	       set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',7), ',''', -1 ); 
		   set @legalEntityId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8), ',''', -1 );
	       set @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',9), ',''', -1 );          
	       set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ',''',-1 ), '''', 1 );              
	       set @recordsDataWithoutLimits = concat([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ''',',8),''''); 

		   IF @serviceDefinitionId is null or @serviceDefinitionId = '' BEGIN
				SET @serviceDefinitionId = (SELECT servicedefinitionId from [${dbxschemaname}].[contract] WHERE id = @contractId);
		   END 

		    IF @limitId <> '@' AND @limitValue <> '@'
                     BEGIN --111

					   SET @limitAtFI = (SELECT actionlimit.value FROM [${dbxschemaname}].[actionlimit] WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId AND actionlimit.companyLegalUnit = @legalEntityId);

					   SET @limitATServiceDefinition = (SELECT servicedefinitionactionlimit.value FROM [${dbxschemaname}].[servicedefinitionactionlimit] WHERE servicedefinitionactionlimit.actionId = @actionId AND servicedefinitionactionlimit.limitTypeId = @limitId        
					        AND servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
						AND servicedefinitionactionlimit.companyLegalUnit = @legalEntityId);                   
					  					   

					   IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                              
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                     
                  END ; --111

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
                               [${dbxschemaname}].contractfeatures.featureId = @featureId  AND 
							   [${dbxschemaname}].contractfeatures.companyLegalUnit = @legalEntityId  
                     )

		   SET @serviceDefinitionActions = 
                     (
								
                     SELECT String_agg(CAST(servicedefinitionactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND 
			 [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @legalEntityId 
                     ) 

             

			SET @existingActionLimitRecords = 
                     (
                                   
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId
                     )

					 SET @existingAccountActionLimitRecords = (SELECT String_agg(CAST(acl.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit acl WHERE acl.contractId = @contractId AND 
						acl.coreCustomerId = @customerId AND 
						acl.featureId = @featureId AND acl.actionId = @actionId AND 
						acl.limitTypeId = @limitId AND acl.companyLegalUnit = @legalEntityId);

					  SET @existingActionRecords = 
                     (
                     			 
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId  AND
							   [${dbxschemaname}].contractactionlimit.companyLegalUnit = @legalEntityId 
                     )
					 
					  SET @existingActionRecords1 = 
                     (
                     			 
                        SELECT String_agg(CAST(accountlevelactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].accountlevelactionlimit WHERE 
                               [${dbxschemaname}].accountlevelactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].accountlevelactionlimit.coreCustomerId = @customerId AND
							   [${dbxschemaname}].accountlevelactionlimit.accountId =@accountId AND
                               [${dbxschemaname}].accountlevelactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].accountlevelactionlimit.actionId = @actionId AND 
								[${dbxschemaname}].accountlevelactionlimit.companyLegalUnit = @legalEntityId							   
                       
                     ) 

			IF @contarctFeatures is NOT NULL AND @contarctFeatures <> '' AND @serviceDefinitionActions IS NOT NULL AND @serviceDefinitionActions <> '' AND (@accountId IS NULL or @accountId = '')
                  
				  BEGIN -- 9
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
							BEGIN -- 6
							 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @query = (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction,companyLegalUnit) VALUES (''') + (@recordsDataWithoutLimits) + (N''');')
                                  END
                                
							    
								   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction, contractactionlimit.companyLegalUnit,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@recordsData) + (N''');')
                                     
                                        END 
                                    END -- 6
							END  -- 9
					ELSE		
						BEGIN --11
							IF @existingAccountActionLimitRecords IS NOT NULL AND @existingAccountActionLimitRecords <> ''
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
                           							(@existingAccountActionLimitRecords)
                            							+ 
                           							(N'''')
                            							+ 
                           							(N';')
                     END
					ELSE
					BEGIN -- 13

						 IF (@limitId = '@' OR @limitValue = '@') AND (@existingActionRecords1 IS NULL OR @existingActionRecords1 = '')
                                     BEGIN
                                         SET @query =(N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.featureId,contractactionlimit.actionId, accountlevelactionlimit.isNewAction, accountlevelactionlimit.companyLegalUnit) VALUES (''') + (@recordsDataWithoutLimits) + (N''');')
                                    END
                                    
                           
							   ELSE IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
                                           SET @query = (N'INSERT INTO [${dbxschemaname}].accountlevelactionlimit(accountlevelactionlimit.id,accountlevelactionlimit.contractId,accountlevelactionlimit.coreCustomerId,accountlevelactionlimit.featureId,accountlevelactionlimit.actionId,accountlevelactionlimit.isNewAction,accountlevelactionlimit.companyLegalUnit,accountlevelactionlimit.limitTypeId,accountlevelactionlimit.value) VALUES (''') + (@recordsData) + (N''');')
                                     
                                        END 
							
				                      END -- 13 
				              END --11
									 
						 exec(@query)
						
   END -- outermost level 

   END -- while level
   END -- proc level  

GO

SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER FUNCTION [${dbxschemaname}].[DISTINCT_VALUE](@value NVARCHAR(max))
RETURNS NVARCHAR(max)
AS
BEGIN

SELECT @value=STRING_AGG(value,',') FROM (
SELECT DISTINCT value FROM STRING_SPLIT(@value,',')
) a
RETURN @value
END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[user_securityattributes_get_proc]    Script Date: 5/22/2023 7:36:34 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[user_securityattributes_get_proc]
   @_userId nvarchar(50),
   @_legalEntityId nvarchar(50)
AS 
BEGIN
​
SET  XACT_ABORT  ON
SET  NOCOUNT  ON
​
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
DECLARE @newActionsAtCoreCustomers nvarchar(max) = N''
DECLARE @newActionsAtGroups nvarchar(max) = N''
​
SELECT @userAssociatedContracts = String_agg(CAST(contractcustomers.contractId AS nvarchar(max)), ','),
@userAssociatedCoreCustomers =  String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId and contractcustomers.companyLegalUnit = @_legalEntityId;

SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId and customergroup.companyLegalUnit = @_legalEntityId);
​
SELECT
@intersectedUserActions = String_agg(CAST(featureaction.id AS nvarchar(max)), ','),
@intersectedUserFeatures = String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',')
FROM [${dbxschemaname}].featureaction
JOIN [${dbxschemaname}].feature ON ([${dbxschemaname}].feature.id = [${dbxschemaname}].featureaction.Feature_id AND [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE' AND [${dbxschemaname}].feature.companyLegalUnit = @_legalEntityId)
WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId AND
EXISTS (
SELECT 'X' FROM [${dbxschemaname}].customeraction where
[${dbxschemaname}].customeraction.Customer_id = @_userId AND [${dbxschemaname}].customeraction.isAllowed = 1 AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].customeraction.Action_id
AND [${dbxschemaname}].customeraction.companyLegalUnit = @_legalEntityId
) AND
EXISTS (
SELECT 'X' FROM [${dbxschemaname}].groupactionlimit where
[${dbxschemaname}].groupactionlimit.Group_id IN (SELECT DISTINCT value FROM STRING_SPLIT(@userAssociatedGroups, ',')) AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].groupactionlimit.Action_id
AND [${dbxschemaname}].groupactionlimit.companyLegalUnit = @_legalEntityId
) AND
EXISTS (
SELECT 'X' FROM [${dbxschemaname}].servicedefinitionactionlimit where
[${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId IN (SELECT DISTINCT contract.servicedefinitionId FROM [${dbxschemaname}].contract where [${dbxschemaname}].contract.id IN (SELECT DISTINCT value FROM STRING_SPLIT(@userAssociatedContracts, ',')) AND [${dbxschemaname}].contract.statusId = 'SID_CONTRACT_ACTIVE') AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].servicedefinitionactionlimit.actionId
AND [${dbxschemaname}].servicedefinitionactionlimit.companyLegalUnit = @_legalEntityId
) AND
EXISTS (
SELECT 'X' FROM [${dbxschemaname}].contractactionlimit where
[${dbxschemaname}].contractactionlimit.coreCustomerId IN (SELECT DISTINCT value FROM STRING_SPLIT(@userAssociatedCoreCustomers, ',')) AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].contractactionlimit.actionId

);

SELECT @intersectedUserActions AS actions;

SET @intersectedUserFeatures = (select [${dbxschemaname}].[DISTINCT_VALUE](@intersectedUserFeatures) );

SELECT @intersectedUserFeatures AS features;
​
END;
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[contracts_with_activeaccounts]    Script Date: 5/22/2023 1:27:16 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contracts_with_activeaccounts]
@_contractIdList nvarchar(max)

AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 DECLARE @finished INT = 0 ;
 DECLARE @contractIds nvarchar(max) = N'';
 DECLARE @statusPoint nvarchar(255);
 DECLARE @contracts nvarchar(255);
  

DECLARE statuses CURSOR
         FOR (SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractaccounts.contractId,@_contractIdList) ='1');

OPEN statuses;
 WHILE(1 = 1)
 BEGIN
		FETCH NEXT FROM statuses into @statusPoint
		IF @@FETCH_STATUS <> 0
               SET @finished = 1
		IF @finished = 1
               BREAK

           SET @contracts = (SELECT contractaccounts.contractId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.accountId = @statusPoint AND [${dbxschemaname}].contractaccounts.statusDesc != 'closed');
		   IF (@contracts IS NULL)
		   BEGIN
		    SET @finished = 1;
           END
		   
		   ELSE
		     BEGIN
			   IF(@contractIds is not null and LEN(@contractIds)>1)
			   BEGIN
			     SET @contracts =  ',' + @contracts;
			   END
			   SET @contractIds = @contractIds + @contracts;
             END
	END
CLOSE statuses;
DEALLOCATE statuses;
SELECT @contractIds as contractIds;
END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customrole_contract_delete_proc]    Script Date: 5/22/2023 5:10:33 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customrole_contract_delete_proc]
   @customRoleId nvarchar(max),
   @contractId nvarchar(max),
   @coreCustomerId nvarchar(max),
   @legalEntityId nvarchar(50)
AS 
   BEGIN
              DECLARE @contract_statement nvarchar(max);
              DECLARE @accounts_statement nvarchar(max);
              DECLARE @excludedaccounts_statement nvarchar(max);
              DECLARE @action_statement nvarchar(max);
              DECLARE @excluded_action_statement nvarchar(max);
              DECLARE @limitgroup_statement nvarchar(max);
              DECLARE @where_clause nvarchar(max);
              DECLARE @where_clause1 nvarchar(max);
              DECLARE @where_clause2 nvarchar(max);
              
              SET @contract_statement = N'DELETE FROM [${dbxschemaname}].[contractcustomrole] where';
              SET @accounts_statement = N'DELETE FROM [${dbxschemaname}].[customroleaccounts] where ';
              SET @excludedaccounts_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomroleaccounts] where';
              SET @action_statement = N'DELETE FROM [${dbxschemaname}].[customroleactionlimits] where';
              SET @excluded_action_statement = N'DELETE FROM [${dbxschemaname}].[excludedcustomroleactionlimits] where';
              SET @limitgroup_statement = N'DELETE FROM [${dbxschemaname}].[customerlimitgrouplimits] where';

              SET @where_clause = '';
              SET @where_clause1 = '';
              SET @where_clause2 = '';
              
              SET @where_clause = @where_clause + (N' companyLegalUnit = ') + ((QUOTENAME((@legalEntityId), ''''))) + (N' AND ')
			  
			  SET @where_clause1 = @where_clause1 + (N' companyLegalUnit = ') + ((QUOTENAME((@legalEntityId), ''''))) + (N' AND ')
              
              SET @where_clause2 = @where_clause2 + (N' companyLegalUnit = ') + ((QUOTENAME((@legalEntityId), ''''))) + (N' AND ')
              
              
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
                     SET @excluded_action_statement = @excluded_action_statement + @where_clause1;
                     SET @limitgroup_statement = @limitgroup_statement + @where_clause2;     
              
                     exec(@contract_statement);
                     exec(@accounts_statement);
                     exec(@excludedaccounts_statement);
                     exec(@action_statement);
                     exec(@excluded_action_statement);
                     exec(@limitgroup_statement)
              END
   END;
  GO
  
  /****** Object:  StoredProcedure [${dbxschemaname}].[default_contractactions_create_proc]    Script Date: 5/22/2023 5:12:02 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[default_contractactions_create_proc]
@_contractId nvarchar(max),
@_legalEntityId nvarchar(50)

AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON
	  
 DECLARE @finished INT = 0 ;
 DECLARE @featureActionId varchar(255);
 DECLARE @featureId varchar(255) ;
 DECLARE @actionslist varchar(255) ;
 DECLARE @limitId varchar(255);
 DECLARE @entryStatus INT= 0 ;
 DECLARE @accountId varchar(255);
 DECLARE @actualLimitId varchar(255) ;
 DECLARE @_accountsCSV varchar(255) ;
 DECLARE @coreCustomerId varchar(255)  ;
 DECLARE @serviceDefinitionId nvarchar(50);
 DECLARE @serviceType varchar(255);
 DECLARE @validActionsList varchar(max);
 DECLARE @validFIActions varchar(max);
 DECLARE @limitvalue varchar(255);
 DECLARE @id varchar(255);
  

DECLARE coreCustomers CURSOR
         FOR (SELECT contractcorecustomers.coreCustomerId FROM [${dbxschemaname}].contractcorecustomers WHERE [${dbxschemaname}].contractcorecustomers.contractId = @_contractId AND [${dbxschemaname}].contractcorecustomers.companyLegalUnit = @_legalEntityId );
		 
DECLARE limits CURSOR LOCAL
      FOR (select LimitType_id from [${dbxschemaname}].[actionlimit] where Action_id = @featureActionId and [${dbxschemaname}].actionlimit.companyLegalUnit = @_legalEntityId );
DECLARE globalLevelPermissions CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList)='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '0') and [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId);
DECLARE transactionLimits CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET(featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1') and [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId);
	
 
SET @_accountsCSV = (SELECT String_agg(CAST([${dbxschemaname}].contractaccounts.accountId AS nvarchar(max)), ',') FROM 
[${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId AND [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId);

SET @serviceDefinitionId = (SELECT [${dbxschemaname}].contract.servicedefinitionId from [${dbxschemaname}].contract WHERE [${dbxschemaname}].contract.id = @_contractId AND [${dbxschemaname}].contract.companyLegalUnit = @_legalEntityId);
SET @serviceType = (SELECT [${dbxschemaname}].servicedefinition.serviceType from [${dbxschemaname}].servicedefinition WHERE [${dbxschemaname}].servicedefinition.id = @serviceDefinitionId);
                        
SET @validFIActions = (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') 
FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].featureaction.Feature_id in (select
[${dbxschemaname}].contractfeatures.featureId from [${dbxschemaname}].contractfeatures
where [${dbxschemaname}].contractfeatures.contractId = @_contractId AND [${dbxschemaname}].contractfeatures.companyLegalUnit = @_legalEntityId));

SET @validActionsList = (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId=
@serviceDefinitionId AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,
@validFIActions)=1);

OPEN coreCustomers;
   FETCH NEXT FROM coreCustomers into @coreCustomerId
   WHILE (@@FETCH_STATUS=0)
      BEGIN
      DECLARE accounts CURSOR
           FOR (SELECT contractaccounts.accountId FROM [${dbxschemaname}].contractaccounts WHERE [${dbxschemaname}].contractaccounts.contractId = @_contractId AND [${dbxschemaname}].contractaccounts.companyLegalUnit = @_legalEntityId );
      
		
    OPEN accounts; 
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
       DECLARE accountLevelPermissions CURSOR 
		  FOR (select featureaction.id from [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@validActionsList) ='1' AND ([${dbxschemaname}].featureaction.Type_id = 'NON_MONETARY' and [${dbxschemaname}].featureaction.isAccountLevel = '1') AND [${dbxschemaname}].featureaction.companyLegalUnit = @_legalEntityId);

	  OPEN accountLevelPermissions; 

	  FETCH NEXT FROM accountLevelPermissions into @featureActionId
      WHILE (@@FETCH_STATUS=0)
          BEGIN
            
			SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId AND featureaction.companyLegalUnit = @_legalEntityId);
            SET @id = (SELECT left(newid(), 50));
	        INSERT [${dbxschemaname}].contractactionlimit(id,contractId,coreCustomerId,accountId,featureId,actionId,companyLegalUnit) VALUES
					(@id,@_contractId,@coreCustomerId,@accountId,@featureId,@featureActionId,@_legalEntityId);
			
			    FETCH NEXT FROM accountLevelPermissions into @featureActionId
                CONTINUE
			
           END
          CLOSE accountLevelPermissions;
          DEALLOCATE accountLevelPermissions;
		      FETCH NEXT FROM accounts into @accountId
              CONTINUE
           END
  CLOSE accounts;
  DEALLOCATE accounts;
	
END
CLOSE coreCustomers;
DEALLOCATE coreCustomers;
END;
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[get_associated_contractusers_proc]    Script Date: 5/22/2023 5:13:28 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE OR ALTER PROCEDURE [${dbxschemaname}].[get_associated_contractusers_proc](
	@_id NVARCHAR(MAX),
	@_backendType NVARCHAR(MAX),
	@_legalEntityId NVARCHAR(MAX)
) AS BEGIN

	select ca.Customer_id as id,
	ca.contractId as contractId,
	con.name as contractName,
	ca.coreCustomerId as coreCustomerId,
	concore.coreCustomerName as coreCustomerName,
	cc.customerId as customerId,
	cg.Group_id as groupId,
	cus.FirstName as firstName,
	cus.LastName as lastName,
	cus.UserName as userName ,
	cus.Lastlogintime as lastlogintime,
	cus.Status_id as statusId,
	b.BackendId as backendId,
	ca.companyLegalUnit as companyLegalUnit
	from 
	[customeraction] ca
	left join [${dbxschemaname}].contractcustomers cc on
	(ca.contractId = cc.contractId and ca.coreCustomerId = cc.coreCustomerId)
	left join [${dbxschemaname}].contractaccounts conact on
	(ca.contractId = conact.contractId and ca.coreCustomerId = conact.coreCustomerId)
	left join [${dbxschemaname}].contractcorecustomers concore on
	(ca.contractId = concore.contractId and ca.coreCustomerId = concore.coreCustomerId)
	left join [${dbxschemaname}].contract con on
	(concore.contractId = con.id)
	left join [${dbxschemaname}].customergroup cg on
	(cc.contractId = cg.contractId and cc.coreCustomerId = cg.coreCustomerId and cg.Customer_id = cc.customerId)
	left join [${dbxschemaname}].customer cus on
	(cg.Customer_id = cus.id)
	left join [${dbxschemaname}].backendidentifier b on
	(b.Customer_id = cus.id and b.BackendType = @_backendType)
	WHERE ca.Customer_id = @_id and ca.Action_id = 'USER_MANAGEMENT_VIEW' and ca.companyLegalUnit = @_legalEntityId and conact.statusDesc != 'CLOSED';
	
END;
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[contract_users_details_get_proc]    Script Date: 5/22/2023 5:14:35 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO


CREATE OR ALTER PROCEDURE [${dbxschemaname}].[contract_users_details_get_proc]
@_contractId nvarchar(50),
@_backendType nvarchar(50)
AS
BEGIN

SET XACT_ABORT ON
SET NOCOUNT ON

DECLARE
@customers nvarchar(max) = N''

SET @customers = (SELECT
String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].contractcustomers WHERE
[${dbxschemaname}].contractcustomers.contractId = @_contractId)

SET @customers = (SELECT DISTINCT String_agg(CAST(contractcustomers.customerId AS nvarchar(max)), ',')
FROM [${dbxschemaname}].contractcustomers WHERE [${dbxschemaname}].[contractcustomers].customerId IN (@customers))
	
SELECT
customer.id AS customerId,
customer.FirstName AS firstName,
customer.MiddleName AS middleName,
customer.LastName AS lastName,
customer.UserName AS userName,
customer.Status_id AS statusId,
customer.DateOfBirth AS dateOfBirth,
customer.Ssn AS Ssn,
BI.BackendId AS primaryCoreCustomerId,
CC.Value AS Email
FROM
[${dbxschemaname}].customer
LEFT JOIN [${dbxschemaname}].customercommunication CC ON (CC.Customer_id = customer.id AND CC.Type_id = 'COMM_TYPE_EMAIL' AND CC.Customer_id IN (@customers))
LEFT JOIN [${dbxschemaname}].backendidentifier BI ON (BI.Customer_id = customer.id AND BI.BackendType = @_backendType AND BI.Customer_id IN (@customers))
WHERE
customer.id IN (SELECT DISTINCT value FROM STRING_SPLIT(@customers, ','));
END;
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[fetch_restrictive_featureactionlimits_legalEntityId_proc]    Script Date: 5/22/2023 6:10:59 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER  PROCEDURE [${dbxschemaname}].[fetch_restrictive_featureactionlimits_legalEntityId_proc](
@_locale nvarchar(50),
@_userId nvarchar(50),
@_serviceDefinitionId nvarchar(50),
@_roleId nvarchar(50),
@_coreCustomerId nvarchar(50),
@_accessPolicyIdList nvarchar(50),
@_legalEntityId nvarchar(50)
)
AS
BEGIN
DECLARE @select_statement NVARCHAR(max);
DECLARE @action_select_statement NVARCHAR(max);
DECLARE @action_select_statement_output NVARCHAR(max);
DECLARE @FeatureActionList NVARCHAR(max);

SET @FeatureActionList = N'';
SET @select_statement = '';
SET @action_select_statement = '';


SET @select_statement = 'SELECT featureaction.id FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = ''SID_ACTION_ACTIVE'''
IF(@_accessPolicyIdList != '') BEGIN
SET @select_statement = @select_statement + ' AND [${dbxschemaname}].featureaction.accessPolicyId IN (select DISTINCT value from STRING_SPLIT(@_accessPolicyIdList, '',''))'
END;

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].servicedefinitionactionlimit
WHERE servicedefinitionactionlimit.serviceDefinitionId = ''' + @_serviceDefinitionId + ''' AND
servicedefinitionactionlimit.companyLegalUnit  =''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].servicedefinitionactionlimit.actionId
) '
END;
IF(@_roleId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].groupactionlimit
WHERE [${dbxschemaname}].groupactionlimit.Group_id = ''' + @_roleId + ''' AND
groupactionlimit.companyLegalUnit = '''+ @_legalEntityId + '''  AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].groupactionlimit.Action_id
) '
END
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].contractactionlimit
WHERE [${dbxschemaname}].contractactionlimit.coreCustomerId = ''' + @_coreCustomerId + ''' AND
contractactionlimit.companyLegalUnit = ''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].contractactionlimit.actionId
) '
END;
IF(@_userId != '') BEGIN
SET @select_statement = @select_statement + ' AND EXISTS
(
SELECT ''X'' FROM [${dbxschemaname}].customeraction
WHERE [${dbxschemaname}].customeraction.isAllowed = 1 AND
[${dbxschemaname}].featureaction.id = [${dbxschemaname}].customeraction.Action_id AND
customeraction.companyLegalUnit = ''' + @_legalEntityId + ''' AND 
[${dbxschemaname}].customeraction.Customer_id = ''' + @_userId + '''' + ')'
END
/*
IF (@_coreCustomerId != '')
SET @select_statement = @select_statement + ' AND [${dbxschemaname}].customeraction.coreCustomerId = ''' + @_coreCustomerId + ''')'
ELSE
SET @select_statement = @select_statement + ')'
END;
*/

SET @action_select_statement = @select_statement;

-- SELECT @select_statement AS 'Action_statement'

SET @select_statement = '';

SET @select_statement = '( SELECT
feature.id AS featureId,
featuredisplaynamedescription.displayName AS featureName,
featuredisplaynamedescription.displayDescription AS featureDescription,
feature.Status_id AS featureStatus,
featureaction.status AS actionStatus,
featureaction.id AS actionId,
actiondisplaynamedescription.displayName AS actionName,
actiondisplaynamedescription.displayDescription AS actionDescription,
featureaction.isAccountLevel AS isAccountLevel,
featureaction.Type_id AS typeId,
featureaction.limitgroupId as limitGroupId,
featureaction.accesspolicyId as accessPolicyId,
featureaction.companyLegalUnit as legalEntityId,
featureaction.actionlevelId as actionLevelId,
IIF(featureaction.Type_id = ''NON_MONETARY'',null,actionlimit.LimitType_id) as limitTypeId,
IIF(featureaction.Type_id = ''NON_MONETARY'',null,actionlimit.value) as fiLimitValue';
IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,servicedefinitionactionlimit.value) AS serviceLimitValue');
END ;
IF(@_roleId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,groupactionlimit.value) AS groupLimitValue');
END ;
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ', IIF(featureaction.Type_id = ''NON_MONETARY'',null,contractactionlimit.value) AS coreCustomerLimitValue');
END ;
IF @_roleId != '' AND @_coreCustomerId != ''
SET @select_statement = CONCAT(@select_statement , ', IIF([${dbxschemaname}].groupactionlimit.isNewAction = ''1'' OR contractactionlimit.isNewAction = ''1'', ''1'', ''0'') as isNewAction');
ELSE
BEGIN
IF @_roleId != ''
SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].groupactionlimit.isNewAction AS isNewAction');
ELSE IF @_coreCustomerId != ''
SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].contractactionlimit.isNewAction AS isNewAction');
END ;

SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON (  featuredisplaynamedescription.companyLegalUnit = feature.companyLegalUnit AND 
featuredisplaynamedescription.Feature_id = feature.id AND [${dbxschemaname}].featuredisplaynamedescription.Locale_id = ', '''', @_locale, '''', ')
LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.companyLegalUnit = feature.companyLegalUnit AND featureaction.Feature_id = feature.id)
LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.companyLegalUnit = featureaction.companyLegalUnit AND actiondisplaynamedescription.Action_id = featureaction.id AND [${dbxschemaname}].actiondisplaynamedescription.Locale_id = ', '''', @_locale, '''' , ')');

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].servicedefinitionactionlimit ON (servicedefinitionactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND servicedefinitionactionlimit.actionId = featureaction.id  AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId , '''', ')');
END;
IF(@_roleId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND groupactionlimit.Action_id = featureaction.id AND groupactionlimit.Group_id = ' , '''', @_roleId, '''',')');
END;
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON (contractactionlimit.companyLegalUnit = featureaction.companyLegalUnit AND contractactionlimit.actionId = featureaction.id  AND contractactionlimit.coreCustomerId = ', '''', @_coreCustomerId, '''', ')');
END ;

SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].actionlimit ON (actionlimit.companyLegalUnit = featureaction.companyLegalUnit AND actionlimit.Action_id = featureaction.id)');

SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.companyLegalUnit = ''' + @_legalEntityId + ''''+' AND  [${dbxschemaname}].featureaction.id IN ('+ @action_select_statement +')');

IF(@_serviceDefinitionId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' AND (([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId , ''') OR (featureaction.Type_id = ''NON_MONETARY''))');
END ;
IF(@_roleId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' AND (([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].groupactionlimit.LimitType_id = actionlimit.LimitType_id AND groupactionlimit.Group_id = ' , '''', @_roleId, ''') OR (featureaction.Type_id = ''NON_MONETARY''))');
END ;
IF(@_coreCustomerId != '') BEGIN
SET @select_statement = CONCAT(@select_statement , ' AND (([${dbxschemaname}].featureaction.Type_id = ''MONETARY'' AND [${dbxschemaname}].contractactionlimit.limitTypeId = actionlimit.LimitType_id AND contractactionlimit.coreCustomerId = ' , '''', @_coreCustomerId,''') OR (featureaction.Type_id = ''NON_MONETARY''))');
END ;

SET @select_statement = CONCAT(@select_statement , ')');

exec(@select_statement);

END
GO


/****** Object:  StoredProcedure [${dbxschemaname}].[customrole_actionlimits_create_proc]    Script Date: 5/23/2023 12:52:47 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customrole_actionlimits_create_proc]  
   @_queryInput nvarchar(max),
   @_customRoleId bigint
AS 
   BEGIN
   
      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON
   DECLARE @index int
   DECLARE @numOfRecords int
   DECLARE @recordsData nvarchar(max)
   DECLARE @query nvarchar(max)

      DELETE 
      FROM [${dbxschemaname}].customroleactionlimits
      WHERE customroleactionlimits.customRole_id = @_customRoleId

      SET @index = 0
	  SET @_queryInput = (SELECT REPLACE(@_queryInput,'"',''''))
      SET @numOfRecords = case when @_queryInput is null then 0 else LEN(@_queryInput) - LEN(replace(@_queryInput, '|', '')) + 1 end

      WHILE (1 = 1)
      
         BEGIN

           SET  @index = @index + 1
            IF (@index = @numOfRecords + 1)
               BREAK
            ELSE 
               BEGIN
                  SET @recordsData = [${dbxschemaname}].substring_index([${dbxschemaname}].substring_index(@_queryInput, N'|', @index), N'|', -1)
                  SET @query = ('INSERT INTO [${dbxschemaname}].customroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id,isAllowed, limitGroupId, limitType_id,value,companyLegalUnit) VALUES (') + (@recordsData) + (N')')
				  EXEC(@query)
               END
         END
   END
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customer_search_proc]    Script Date: 5/23/2023 5:53:23 AM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE OR ALTER PROCEDURE [${dbxschemaname}].[customer_search_proc]  
   @_searchType varchar(60),
   @_id varchar(50),
   @_name varchar(50),
   @_SSN varchar(50),
   @_username varchar(50),
   @_dateOfBirth varchar(100),
   @_phone varchar(100),
   @_email varchar(100),
   @_IsStaffMember varchar(10),
   @_cardorAccountnumber varchar(50), 
   @_TIN varchar(50),
   @_group varchar(40),
   @_IDType varchar(50),
   @_IDValue varchar(50),
   @_companyId varchar(50),
   @_requestID varchar(50),
   @_branchIDS varchar(2000),
   @_productIDS varchar(2000),
   @_cityIDS varchar(2000),
   @_entitlementIDS varchar(2000),
   @_groupIDS varchar(2000),
   @_customerStatus varchar(50),
   @_before varchar(20),
   @_after varchar(20),
   @_sortVariable varchar(100),
   @_sortDirection varchar(4),
   @_pageOffset bigint,
   @_pageSize bigint,
   @_legalEntityId varchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON

      SET  NOCOUNT  ON

      DECLARE
         @_maxLockCount varchar(50)
		 declare @search_select_statement nvarchar(max)
		 declare @search_count_statement nvarchar(max)
		 declare @queryStatement nvarchar(max)
		 declare @queryStatement2 nvarchar(max)
		 
      IF @_searchType LIKE N'GROUP_SEARCH%'
		BEGIN
            SET @search_select_statement = N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isCombinedUser as isCombinedUser,ISNULL(customer.combinedUserId, '''') as combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'', customer.CustomerType_id) AS CustomerTypeId, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value AS PrimaryEmail,STRING_AGG(CAST(customergroup.Group_id as nvarchar(max)),'','') as assigned_group_ids,address.City_id, city.Name as City_name, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.Location_id AS branch_id,location.Name AS branch_name, ''true'' as isProfileExist '

            SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
 
            SET @queryStatement = 'FROM [${dbxschemaname}].customer JOIN (SELECT [${dbxschemaname}].customer.id FROM [${dbxschemaname}].customer ' + (
               CASE 
                  WHEN (@_groupIDS <> '' OR @_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=customer.id)'
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_cityIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customeraddress ON (customer.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) 
				  LEFT JOIN [${dbxschemaname}].city ON (address.City_id = city.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_entitlementIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerentitlement ON (customerentitlement.Customer_id=customer.id) '
                  ELSE N''
               END) + (
               CASE 
                  WHEN (@_productIDS <> '') THEN N' LEFT JOIN [${dbxschemaname}].customerproduct ON (customerproduct.Customer_id=customer.id) '
                  ELSE N''
               END)

			   declare @whereclause nvarchar(max)
            SET @whereclause = N' WHERE 1=1 '


            IF @_username <> ''
               BEGIN


                  SET @whereclause = (@whereclause) + (N' AND (customer.firstname like (''') + (@_username)  +'%'')'

                  SET @whereclause = (@whereclause) + (N' OR customer.username like (''') + (@_username)  +'%'')'

             SET @whereclause = (@whereclause) + (N' OR customer.id like (''') + (@_username) + '%''))'


               END


            IF @_IsStaffMember <> ''
               IF @_IsStaffMember = 'true'
 
                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''1''')

               ELSE 

                  SET @whereclause = (@whereclause) + (N' AND customer.IsStaffMember = ''0''')



            IF @_entitlementIDS <> ''

               SET @whereclause = 
                  (@whereclause)
                   + 
                  (N' AND (customerentitlement.Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N') OR customergroup.Group_id in ( select Group_id from [${dbxschemaname}].groupentitlement where Service_id in (')
                   + 
                  ([${dbxschemaname}].func_escape_input_for_in_operator(@_entitlementIDS))
                   + 
                  (N' )))')
       
            IF @_groupIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customergroup.Group_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_groupIDS)) + (N') ')

            IF @_productIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customerproduct.Product_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_productIDS)) + (N')')


            IF @_branchIDS <> ''

               SET @whereclause = (@whereclause) + (N' AND customer.Location_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_branchIDS)) + (N')')
   

            IF @_customerStatus <> ''
           
               SET @whereclause = (@whereclause) + (N' AND customer.Status_id = ') + ((QUOTENAME((@_customerStatus), '''')))
           
            IF @_cityIDS <> ''
            
               SET @whereclause = (@whereclause) + (N' AND address.City_id in (') + ([${dbxschemaname}].func_escape_input_for_in_operator(@_cityIDS)) + (N')')
               

            IF @_before <> '' AND @_after <> ''
             
               SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), ''''))) + (N',105) and CONVERT(DATE,customer.createdts,105) <= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
            
            ELSE 
               IF @_before <> ''
             
                  SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_before), '''')))+',105)'
                  

                  
               ELSE 
                  BEGIN
                     IF @_after <> ''
                    
                        SET @whereclause = (@whereclause) + (N' AND CONVERT(DATE,customer.createdts,105) >= CONVERT(DATE, ') + ((QUOTENAME((@_after), '''')))+',105)'
                        
                  END

        
            SET @queryStatement = (@queryStatement) + (@whereclause) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=paginatedCustomers.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') LEFT JOIN [${dbxschemaname}].customergroup ON (customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id=''ADR_TYPE_HOME'') LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id) LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id) LEFT JOIN address ON (city.Country_id = country.id) LEFT JOIN [${dbxschemaname}].location ON (location.id=customer.Location_id)')
        
	
			IF @_searchType = 'GROUP_SEARCH'
				BEGIN

                  SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                  SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.isCombinedUser, customer.combinedUserId, customer.Salutation, customer.Gender, customer.IsStaffMember, customer.CustomerType_id, customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryEmail.value,address.City_id, city.Name ,customer.Location_id,location.Name, paginatedCustomers.id ')
                  

                  IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                  
                     SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                     
                  ELSE 
                     BEGIN
                        IF @_sortVariable <> ''
                        
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                    
                     END

                  IF @_sortDirection <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)
                

                  SET @queryStatement = (@queryStatement) + (' OFFSET ') + CAST(@_pageOffset AS NVARCHAR(MAX)) + ' rows fetch next ' +CAST(@_pageSize AS NVARCHAR(MAX)) + +(' rows only')
                
                END
			    ELSE IF @_searchType = 'GROUP_SEARCH_TOTAL_COUNT'
                 
                     SET @queryStatement = (@search_count_statement) + (@queryStatement)

		END
      ELSE IF @_searchType LIKE N'CUSTOMER_SEARCH%'
        BEGIN

                  SELECT @_maxLockCount = CAST(passwordlockoutsettings.accountLockoutThreshold AS varchar(50))
                  FROM [${dbxschemaname}].passwordlockoutsettings
                  WHERE passwordlockoutsettings.id = 'PLOCKID1'

                  SET @search_select_statement = (N'SELECT customer.id, customer.FirstName, customer.MiddleName, customer.LastName,customer.DateOfBirth,(ISNULL(customer.FirstName,'''')+ '' ''+ ISNULL(customer.MiddleName,'''')+ '' ''+ ISNULL(customer.LastName,'''')) as name,customer.UserName as Username, customer.isEnrolledFromSpotlight as isEnrolledFromSpotlight, customer.isCombinedUser as isCombinedUser, customer.Salutation, customer.Gender,(''****''+ RIGHT(customer.Ssn, 4)) as Ssn,IIF((customer.isCombinedUser = ''1''),''TYPE_ID_RETAIL,TYPE_ID_BUSINESS'',customer.CustomerType_id) AS CustomerTypeId, ISNULL(customer.combinedUserId, '''') as combinedUserId, company.id as CompanyId, company.Name as CompanyName,organisationemployees.isAuthSignatory as isAuthSignatory, case when ISNULL(customer.lockCount,0) >= ') + (@_maxLockCount) + (N' then ''SID_CUS_LOCKED'' else customer.Status_id end as Status_id,PrimaryPhone.value AS PrimaryPhoneNumber,PrimaryEmail.value AS PrimaryEmailAddress,String_agg(CAST(membergroup.Name as nvarchar(max)),'','') as groups, address.addressLine1 As addressLine1, address.addressLine2 As addressLine2, city.Name As city, address.zipCode As zipCode, country.Name As county, customer.isEnrolled as isEnrolled,customer.ApplicantChannel, customer.createdts, ''true'' as isProfileExist, customer.companyLegalUnit as companyLegalUnit')
                 

                  SET @search_count_statement = N'SELECT count(distinct customer.id) as SearchMatchs '
                
                  SET @queryStatement = ' FROM [${dbxschemaname}].customer JOIN (SELECT customer.id FROM [${dbxschemaname}].customer where customer.companyLegalUnit = '+''''+@_legalEntityId+''''+ (
                     CASE 
                        WHEN (@_phone <> '') THEN N' JOIN [${dbxschemaname}].customercommunication PrimaryPhone ON (PrimaryPhone.Customer_id=customer.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_email <> '') THEN N'  JOIN [${dbxschemaname}].customercommunication PrimaryEmail ON (PrimaryEmail.Customer_id=customer.id AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'') '
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_TIN <> '') THEN N' LEFT JOIN [${dbxschemaname}].organisationmembership ON (customer.Organization_id = organisationmembership.Organization_id)'
                        ELSE N''
                     END) + (
                     CASE 
                        WHEN (@_cardorAccountnumber <> '') THEN N' LEFT JOIN [${dbxschemaname}].card ON (customer.id = card.User_id) LEFT JOIN [${dbxschemaname}].accounts ON (customer.id = accounts.User_id) LEFT JOIN [${dbxschemaname}].customeraccounts ON (customer.id = customeraccounts.Customer_id)'
                        ELSE N''
                     END)
            
                 
                 
                  IF @_id <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and customer.id = ') + ((QUOTENAME((@_id), '''')))
                     

                  IF @_name <> ''
                    
                     SET @queryStatement = (@queryStatement) + (N' and customer.LastName like (''') + (@_name) +'%'')'
                     

                  IF @_SSN <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.Ssn = ') + ((QUOTENAME((@_SSN), '''')))
            

                  IF @_username <> ''
                 
                     SET @queryStatement = (@queryStatement) + (N' and customer.username = ') + ((QUOTENAME((@_username), '''')))
					 
				IF @_dateOfBirth <> ''
			 
				 SET @queryStatement = (@queryStatement) + (N' and customer.DateOfBirth = ') + ((QUOTENAME((@_dateOfBirth), '''')))
                   

                  IF @_phone <> ''
                     IF datalength(@_phone) > 9
                     
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value like (''%') + (@_phone) +
						'%'')'
                       
                     ELSE 
                       
                        SET @queryStatement = (@queryStatement) + (N' and PrimaryPhone.value = ') + ((QUOTENAME((@_phone), '''')))
                       

                  IF @_email <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and PrimaryEmail.value = ') + ((QUOTENAME((@_email), '''')))
                   

                  IF @_companyId <> ''
                  
                     SET @queryStatement = (@queryStatement) + (N' and customer.Organization_id = ') + ((QUOTENAME((@_companyId), '''')))
                    

                  IF @_IDValue <> ''
                     IF @_IDType = 'ID_DRIVING_LICENSE'
                     
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.DrivingLicenseNumber = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N' or (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                            + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N'))')
                        
                        
                     ELSE 
                        
                        SET @queryStatement = 
                           (@queryStatement)
                            + 
                           (N' and (customer.IDType_id = ')
                            + 
                           ((QUOTENAME((@_IDType), '''')))
                            + 
                           (N' and customer.IDValue = ')
                        + 
                           ((QUOTENAME((@_IDValue), '''')))
                            + 
                           (N')')
                       

                  IF @_TIN <> ''
                   
                     SET @queryStatement = (@queryStatement) + (N' and organisationmembership.Taxid = ') + ((QUOTENAME((@_TIN), '''')))
                     

                  IF @_cardorAccountnumber <> ''
                  
                     SET @queryStatement = 
                        (@queryStatement)
                         + 
                        (N' and (card.cardNumber = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or accounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N' or customeraccounts.Account_id = ')
                         + 
                        ((QUOTENAME((@_cardorAccountnumber), '''')))
                         + 
                        (N')')

                 
                  SET @queryStatement = (@queryStatement) + (N') paginatedCustomers ON (paginatedCustomers.id=customer.id) 
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryPhone 
					ON (PrimaryPhone.Customer_id=paginatedCustomers.id AND PrimaryPhone.isPrimary=1 AND PrimaryPhone.Type_id=''COMM_TYPE_PHONE'')
					LEFT JOIN [${dbxschemaname}].customercommunication PrimaryEmail 					
					ON (PrimaryEmail.Customer_id=paginatedCustomers.id 
					AND PrimaryEmail.isPrimary=1 AND PrimaryEmail.Type_id=''COMM_TYPE_EMAIL'')
					LEFT JOIN [${dbxschemaname}].customeraddress ON (paginatedCustomers.id=customeraddress.Customer_id AND customeraddress.isPrimary=1 and customeraddress.Type_id= ''ADR_TYPE_HOME'')
					LEFT JOIN [${dbxschemaname}].address ON (customeraddress.Address_id = address.id)
					LEFT JOIN [${dbxschemaname}].city ON (city.id = address.City_id)
					LEFT JOIN [${dbxschemaname}].country ON (city.Country_id = country.id)
					LEFT JOIN [${dbxschemaname}].customergroup ON 
					(customergroup.Customer_id=paginatedCustomers.id) LEFT JOIN [${dbxschemaname}].membergroup ON (membergroup.id=customergroup.group_id) 
					LEFT JOIN [${dbxschemaname}].organisation company ON (customer.Organization_id = company.id) LEFT JOIN 
					[${dbxschemaname}].organisationemployees ON (organisationemployees.Organization_id = company.id)')
				   
                 

                  IF @_searchType = 'CUSTOMER_SEARCH'
                     BEGIN

                    SET @queryStatement2 = (@search_count_statement) + (@queryStatement)
                        SET @queryStatement = (@search_select_statement) + (@queryStatement) + (N' GROUP BY customer.id, customer.FirstName, customer.MiddleName, customer.LastName, customer.UserName, customer.Salutation, customer.Gender, customer.IsStaffMember,customer.modifiedby, customer.lastmodifiedts, customer.Status_id, PrimaryPhone.value,PrimaryEmail.value,customer.Location_id,paginatedCustomers.id,customer.DateOfBirth,customer.Ssn,CustomerType_id,company.id,company.Name,customer.lockCount,customer.ApplicantChannel,customer.createdts,customer.isEnrolledFromSpotlight,customer.isCombinedUser,customer.combinedUserId,organisationemployees.isAuthSignatory,address.addressLine1, address.addressLine2, city.Name, address.zipCode, country.Name, customer.isEnrolled, customer.companyLegalUnit ')
                       

                        IF @_sortVariable = 'DEFAULT' OR @_sortVariable = '' OR @_sortVariable IS NULL
                       
                           SET @queryStatement = (@queryStatement) + (N' ORDER BY FirstName')
                        
                          
                        ELSE 
                           BEGIN
                              IF @_sortVariable <> ''
                              
                                 SET @queryStatement = (@queryStatement) + (N' ORDER BY ') + (@_sortVariable)
                                 
                           END

                        IF @_sortDirection <> ''
                         
                           SET @queryStatement = (@queryStatement) + (N' ') + (@_sortDirection)


                        SET @queryStatement = (@queryStatement) + (N' OFFSET ') + (CAST(@_pageOffset AS varchar(50))) +' rows fetch next ' + (CAST(@_pageSize AS varchar(50))) + +(N' rows only')
                      

                    END
                  ELSE 
                     BEGIN
                        IF @_searchType = 'CUSTOMER_SEARCH_TOTAL_COUNT'
                        
                           SET @queryStatement = (@search_count_statement) + (@queryStatement)
                    END

        END
     END
    
    exec(@queryStatement)
     IF @_searchType = 'CUSTOMER_SEARCH' OR  @_searchType = 'GROUP_SEARCH'
     BEGIN
        exec(@queryStatement2)
     END;
GO
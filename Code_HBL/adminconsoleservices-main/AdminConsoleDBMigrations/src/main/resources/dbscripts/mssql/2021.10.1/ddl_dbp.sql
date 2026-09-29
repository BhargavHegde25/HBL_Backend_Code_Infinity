ALTER PROCEDURE [${dbxschemaname}].[customeralertchannel_deletebulk_proc] @_deleteRecords nvarchar(max) AS
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
			set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, '",',6), ',"', -1 );		         
            set @channelId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@rowValues, ',"',-1 ), '"', 1 );
		
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
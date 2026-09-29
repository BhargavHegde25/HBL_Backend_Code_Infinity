USE [${dbxdbname}]
GO

/****** Object:  StoredProcedure [${dbxschemaname}].[customeralertchannel_sync]    Script Date: 10/21/2020 2:37:48 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertchannel_sync]
								@operationType varchar(20),
								@changedLevel varchar(20),
								@channelsStr varchar(255),                                                        
                                @filterValue varchar(255)
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
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertCategoryId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0;  
	END
	ELSE IF (@preference = @changedLevel AND @changedLevel LIKE 'GROUP') 
	BEGIN
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertTypeId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0;
	END
	ELSE IF (@preference = @changedLevel AND @changedLevel LIKE 'ALERT') 
	BEGIN
		DELETE FROM [${dbxschemaname}].customeralertchannel WHERE alertSubTypeId= @filterValue and [${dbxschemaname}].FIND_IN_SET(channelId,@channels_list) <> 0;	  
	END 
  
  ELSE IF @operationType = 'reassign' BEGIN
	  IF (@preference != 'CATEGORY' ) BEGIN
	   DELETE FROM [${dbxschemaname}].customeralertchannel where alertTypeId = @filterValue;
	  END 
  END 
END
GO

/****** Object:  StoredProcedure [[${dbxschemaname}]].[customeralertFrequency_sync]    Script Date: 10/21/2020 3:06:06 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[customeralertFrequency_sync]
								@operationType varchar(20),                                                      
                                @filterValue varchar(255)
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
   DELETE FROM [${dbxschemaname}].customeralertfrequency where alertSubTypeId = @filterValue;
  END   
  ELSE IF (@operationType LIKE 'reassign') 
  BEGIN
    IF (@preference != 'CATEGORY') 
	BEGIN
        DELETE FROM [${dbxschemaname}].customeralertfrequency where alertTypeId = @filterValue;
	END
  END
 END
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[dbxcustomeralertentitlement_sync];
/****** Object:  StoredProcedure [${dbxschemaname}].[dbxcustomeralertentitlement_sync]    Script Date: 10/21/2020 2:52:01 AM ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].[dbxcustomeralertentitlement_sync]
								@operationType varchar(20),                                                      
                                @filterValue varchar(255)
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
   DELETE FROM [${dbxschemaname}].dbxcustomeralertentitlement where alertSubTypeId = @filterValue;
  END   
  ELSE IF (@operationType LIKE 'reassign') 
  BEGIN
    DELETE FROM [${dbxschemaname}].dbxcustomeralertentitlement where AlertTypeId = @filterValue;
  END
END

GO
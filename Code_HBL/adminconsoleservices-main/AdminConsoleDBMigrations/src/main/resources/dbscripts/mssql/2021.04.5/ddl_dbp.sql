DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_action_limit_update;
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
  DECLARE @isNewAction NVARCHAR(max) ;
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
            set @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',5),',',-1);
            set @limitTypeId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',6),',',-1);
            set @limitValue = [${dbxschemaname}].SUBSTRING_INDEX(@contractValues,',',-1);
            set @limitValue = Replace(@limitValue,'"','')
            Declare @num DECIMAL(20,2)
            set @num = cast(@limitValue AS DECIMAL(20,2))
            SELECT @num as num
            UPDATE contractactionlimit SET value = @num where contractId = @contractId AND coreCustomerId = @coreCustomerId AND featureId = @featureId AND actionId = @actionId AND limitTypeId = @limitTypeId;
        END
​
    END ;
END;
GO

CREATE TABLE [${dbxschemaname}].jobtype (
	jobType nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	jobName nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	createdby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	modifiedby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	createdts datetime DEFAULT getdate() NOT NULL,
	lastmodifiedts datetime DEFAULT getdate() NOT NULL,
	synctimestamp datetime DEFAULT getdate() NOT NULL,
	softdeleteflag bit DEFAULT 0 NOT NULL,
	CONSTRAINT PK_jobType PRIMARY KEY (jobType)
);
GO

CREATE TABLE [${dbxschemaname}].InfinityJob (
	id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	serviceDefinitionId nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	status nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT N'SID_JOB_INPROGRESS' NOT NULL,
	createdby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	modifiedby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	createdts datetime DEFAULT getdate() NOT NULL,
	lastmodifiedts datetime DEFAULT getdate() NOT NULL,
	lastsynctimestamp datetime DEFAULT getdate() NOT NULL,
	softdeleteflag bit DEFAULT 0 NOT NULL,
	CONSTRAINT PK_InfinityJob_id PRIMARY KEY (id)
);
GO

CREATE TABLE [${dbxschemaname}].infinityjoblog (
	id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	data nvarchar(max) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
	createdby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	modifiedby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
	createdts datetime DEFAULT getdate() NOT NULL,
	lastmodifiedts datetime DEFAULT getdate() NOT NULL,
	lastsynctimestamp datetime DEFAULT getdate() NOT NULL,
	softdeleteflag bit DEFAULT 0 NOT NULL,
	CONSTRAINT PK_infinityjoblog_id PRIMARY KEY (id)
);
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].fetch_restrictive_featureactionlimits_proc;
GO

CREATE PROCEDURE [${dbxschemaname}].[fetch_restrictive_featureactionlimits_proc](
	@_locale nvarchar(50),
    @_userId nvarchar(50),
    @_serviceDefinitionId nvarchar(50),
    @_roleId nvarchar(50),
    @_coreCustomerId nvarchar(50),
	@_accessPolicyIdList nvarchar(50)
)
AS
BEGIN
	DECLARE @select_statement NVARCHAR(max);
	DECLARE @action_select_statement NVARCHAR(max);
	SET @select_statement = '';
    SET @action_select_statement = '';
	SET @action_select_statement = '(SELECT featureaction.id AS actionId FROM [${dbxschemaname}].featureaction LEFT JOIN [${dbxschemaname}].feature ON ( feature.id = featureaction.Feature_id )';
	IF(@_accessPolicyIdList != '') BEGIN 
		SET @action_select_statement = CONCAT(@action_select_statement , 'WHERE [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.accessPolicyId ,',''''+@_accessPolicyIdList+'''',') = 1');
	END ;
	
    SET @action_select_statement = CONCAT(@action_select_statement,')');

	IF(@_serviceDefinitionId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT servicedefinitionactionlimit.actionId AS actionId FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE servicedefinitionactionlimit.serviceDefinitionId = ' ,'''', @_serviceDefinitionId,'''');
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;	
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT('(SELECT groupactionlimit.Action_id AS actionId FROM [${dbxschemaname}].groupactionlimit WHERE groupactionlimit.Group_id = ','''',@_roleId,'''');
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Action_id IN ');
		SET @select_statement = CONCAT(@select_statement , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT contractactionlimit.actionId AS actionId FROM [${dbxschemaname}].contractactionlimit WHERE contractactionlimit.coreCustomerId = ','''',@_coreCustomerId,'''');
        SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.actionId IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;
	IF(@_userId != '') BEGIN 
		SET @select_statement = CONCAT('(SELECT customeraction.Action_id AS actionId FROM [${dbxschemaname}].customeraction WHERE customeraction.Customer_id = ','''',@_userId,'''');
		IF(@_coreCustomerId != '') BEGIN 
			SET @select_statement = CONCAT(@select_statement , ' AND customeraction.coreCustomerId = ' ,@_coreCustomerId);
        END ;
        SET @select_statement = CONCAT(@select_statement , ' AND customeraction.isAllowed = ''1'' AND customeraction.Action_id IN ' , @action_select_statement);
        SET @select_statement = CONCAT(@select_statement , ')');
        SET @action_select_statement = @select_statement;
    END ;

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
                                featureaction.actionlevelId as actionLevelId,
                                null as limitTypeId,
                                null as fiLimitValue';
    if(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', null AS serviceLimitValue');
    END ;
	IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS groupLimitValue');
    END ;  
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', null AS coreCustomerLimitValue');
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
															LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
	IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON (contractactionlimit.actionId = featureaction.id)');
    END ;
	
	IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , 'LEFT JOIN [${dbxschemaname}].groupactionlimit ON (groupactionlimit.Action_id = featureaction.id)');
    END;
	
	SET @select_statement = CONCAT(@select_statement , 'WHERE featureaction.Type_id = ''NON_MONETARY'' AND featureaction.id IN ' , @action_select_statement , ')');
    
    SET @select_statement = CONCAT(@select_statement , ' UNION (SELECT 
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
                                featureaction.actionlevelId as actionLevelId,
                                actionlimit.LimitType_id as limitTypeId,
                                actionlimit.value as fiLimitValue');
	IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ', servicedefinitionactionlimit.value AS serviceLimitValue');
    END ;
    IF(@_roleId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', groupactionlimit.value AS groupLimitValue');
    END ;
    IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ', contractactionlimit.value AS coreCustomerLimitValue');
    END ;
	
	IF @_roleId != '' AND @_coreCustomerId != ''
		SET @select_statement = CONCAT(@select_statement , ', IIF([${dbxschemaname}].groupactionlimit.isNewAction = ''1'' OR [${dbxschemaname}].contractactionlimit.isNewAction = ''1'', ''1'', ''0'') as isNewAction');
	ELSE
		BEGIN 
			IF @_roleId != ''
				SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].groupactionlimit.isNewAction AS isNewAction');
			ELSE IF @_coreCustomerId != ''
				SET @select_statement = CONCAT(@select_statement , ', [${dbxschemaname}].contractactionlimit.isNewAction AS isNewAction');
		END ;	
	
	SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
														LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
														LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.Feature_id = feature.id)
                                                        LEFT JOIN [${dbxschemaname}].actionlimit ON (actionlimit.Action_id = featureaction.id)
                                                        LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].servicedefinitionactionlimit ON ( servicedefinitionactionlimit.actionId = actionlimit.Action_id AND 
																										servicedefinitionactionlimit.limitTypeId = actionlimit.LimitType_id)');
                                                                                                        
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].groupactionlimit ON ( groupactionlimit.Action_id = actionlimit.Action_id AND 
																										groupactionlimit.LimitType_id = actionlimit.LimitType_id)');
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON ( contractactionlimit.actionId = actionlimit.Action_id AND 
																										contractactionlimit.limitTypeId = actionlimit.LimitType_id)');
	END ;
	SET @select_statement = CONCAT(@select_statement , ' WHERE featureaction.Type_id = ''MONETARY''');
    IF(@_serviceDefinitionId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND servicedefinitionactionlimit.serviceDefinitionId = ' ,'''',@_serviceDefinitionId ,'''');
	END ;
    IF(@_roleId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND groupactionlimit.Group_id = ' , '''',@_roleId,'''');
	END ;
    IF(@_coreCustomerId != '') BEGIN
		SET @select_statement = CONCAT(@select_statement , ' AND contractactionlimit.coreCustomerId = ' ,'''', @_coreCustomerId,'''');
	END ;
    SET @select_statement = CONCAT(@select_statement , ' AND featureaction.id IN ' , @action_select_statement , ')'); 
   exec(@select_statement);
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].user_securityattributes_get_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[user_securityattributes_get_proc]
   @_userId nvarchar(50)
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
SET @userAssociatedCoreCustomers =  (SELECT String_agg(CAST(contractcustomers.coreCustomerId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId);
​
SET @userAssociatedContracts =  (SELECT String_agg(CAST(contractcustomers.contractId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractcustomers where [${dbxschemaname}].contractcustomers.customerId = @_userId);
​
SET @userAssociatedServiceDefinitions =  (SELECT String_agg(CAST(contract.servicedefinitionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contract where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contract.id,@userAssociatedContracts) = '1' AND [${dbxschemaname}].contract.statusId = 'SID_CONTRACT_ACTIVE');
​
SET @userAssociatedGroups =  (SELECT String_agg(CAST(customergroup.Group_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customergroup where [${dbxschemaname}].customergroup.Customer_id = @_userId);
​
SET @actionsAtCoreCustomers =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) = '1');
​
SET @newActionsAtCoreCustomers =  (SELECT String_agg(CAST(contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.coreCustomerId,@userAssociatedCoreCustomers) = '1' AND [${dbxschemaname}].contractactionlimit.isNewAction = '1');
​
SET @actionsAtServiceDefinitions =  (SELECT String_agg(CAST(servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId,@userAssociatedServiceDefinitions) = '1');
​
SET @actionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1');
​
SET @newActionsAtGroups =  (SELECT String_agg(CAST(groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Group_id,@userAssociatedGroups) = '1' AND [${dbxschemaname}].groupactionlimit.isNewAction = '1');
​
SET @actionsAtuser =  (SELECT String_agg(CAST(customeraction.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].customeraction where [${dbxschemaname}].customeraction.Customer_id = @_userId AND ([${dbxschemaname}].customeraction.isAllowed = '1' OR [${dbxschemaname}].customeraction.isAllowed = '1'));
​
SET @activeFeaturesAtFI =  (SELECT String_agg(CAST(feature.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].feature where [${dbxschemaname}].feature.Status_id = 'SID_FEATURE_ACTIVE');
​
SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtuser) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtGroups) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtServiceDefinitions) = '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtCoreCustomers)= '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');
​
SET @intersectedUserActions =  (SELECT String_agg(CAST(featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction WHERE [${dbxschemaname}].featureaction.status = 'SID_ACTION_ACTIVE' AND ([${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions) = '1' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@newActionsAtCoreCustomers) = '1' OR [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@newActionsAtGroups) = '1') AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@actionsAtServiceDefinitions)= '1' AND [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.Feature_id,@activeFeaturesAtFI) = '1');
​
SELECT @intersectedUserActions AS actions;
​
SET @intersectedUserFeatures =  (SELECT String_agg(CAST(featureaction.Feature_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].featureaction.id,@intersectedUserActions)= '1');
​
SELECT @intersectedUserFeatures AS features;
​
END;
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].contract_actionlimits_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[contract_actionlimits_create_proc] 
   @_queryInput nvarchar(max)
AS
   BEGIN
​
      SET  XACT_ABORT  ON
​
      SET  NOCOUNT  ON
​
         DECLARE
         @id nvarchar(255) = N''
​
              DECLARE
         @tempLimitValuenum nvarchar(255) = N''
        
      DECLARE
         @tempLimitValue DECIMAL(20,2)
     
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
         @limitValuenum nvarchar(255) = N''
        
      DECLARE
         @limitValue DECIMAL(20,2)
        
         
      DECLARE
         @isNewAction nvarchar(255) = N''  
      
      DECLARE
         @recordsDataWithoutLimits nvarchar(max) = N'' 
       
      DECLARE
         @limitAtFInum nvarchar(255) = N''  
         
      DECLARE
         @limitAtFI DECIMAL(20,2) 
​
       DECLARE
         @limitATServiceDefinitionnum nvarchar(255) = N''
        
        DECLARE
         @limitATServiceDefinition DECIMAL(20,2) 
​
       DECLARE
         @contarctFeatures nvarchar(max) = N''
   
    DECLARE
         @existingActionLimitRecords nvarchar(max) = N''
   
    DECLARE
         @serviceDefinitionActions nvarchar(max) = N''
​
       DECLARE
         @existingActionRecords nvarchar(max) = N'' 
​
   DECLARE
         @query nvarchar(max) = N''
         
      SET @index = 0
​
         SET @_queryInput = replace(@_queryInput, N'\', N'')
         SET @_queryInput = replace(@_queryInput, N'"', N'')
     
      SET @numOfRecords = LEN(@_queryInput) - LEN(replace(@_queryInput, N'|', N'')) + 1
     
​
      SET @serviceDefinitionId = N''
    
      WHILE (1 = 1)
     
         BEGIN
            SET @index = @index + 1
            IF @index = @numOfRecords + 1
               BREAK
            ELSE
               BEGIN
​
                  SET @recordsData = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1)
​
                  SET @contractId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 1), N'', -1)
​
                  SET @customerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 2), N',', -1)
                 
                  SET @featureId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 3), N',', -1)
                 
                  SET @actionId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 4), N',', -1)
                 
                  SET @isNewAction = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 5), N',', -1)
                 
                  SET @limitId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 6), N',', -1)
                
                  SET @limitValuenum = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', -1), N'', 1)
                 
                  SET @recordsDataWithoutLimits = ([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, N',', 6)) + (N'')
 
                     SET @serviceDefinitionId =
                        (
                         
                           SELECT contract.servicedefinitionId
                           FROM [${dbxschemaname}].contract
                           WHERE contract.id = @contractId
                          
                        )
​
                  IF @limitId <> '@' AND @limitValuenum <> '@'
                     BEGIN
            		set @limitValue = cast(@limitValuenum AS DECIMAL(20,2))
                        SET @limitAtFInum =
                           (                            
                              SELECT actionlimit.value
                              FROM [${dbxschemaname}].actionlimit
                              WHERE actionlimit.Action_id = @actionId AND actionlimit.LimitType_id = @limitId
                             
                           )
                       
                        set @limitAtFI = cast(@limitAtFInum AS DECIMAL(20,2))  
                        SET @limitATServiceDefinitionnum =
                           (
                              SELECT servicedefinitionactionlimit.value
                              FROM [${dbxschemaname}].servicedefinitionactionlimit
                              WHERE
                                 servicedefinitionactionlimit.actionId = @actionId AND
                                 servicedefinitionactionlimit.limitTypeId = @limitId AND
                                 servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId
                            
                           )
                           set @limitATServiceDefinition = cast(@limitATServiceDefinitionnum AS DECIMAL(20,2))
                           IF @limitAtFI > @limitATServiceDefinition
                              SET @tempLimitValue = @limitATServiceDefinition
                           ELSE
                              SET @tempLimitValue = @limitAtFI
                             
                           IF @tempLimitValue > @limitValue
                              SET @tempLimitValue = @limitValue
                              
                          set @tempLimitValuenum = CAST(@tempLimitValue AS nvarchar);   
                     END
                                 
                  IF
                     CASE
                        WHEN NOT
                           CASE
                              WHEN (@tempLimitValuenum) IS NULL THEN 1
                              ELSE 0
                           END <> 0 THEN 1
                        ELSE
                           0
                     END <> 0 AND @tempLimitValuenum <> ''
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
​
                  SET @existingActionLimitRecords =
                     (
                                  
                        SELECT String_agg(CAST(contractactionlimit.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE
                               [${dbxschemaname}].contractactionlimit.contractId = @contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @customerId AND
                               [${dbxschemaname}].contractactionlimit.featureId = @featureId AND
                               [${dbxschemaname}].contractactionlimit.actionId = @actionId AND
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId
                     )
                 
​
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
                    ​                                  set @limitValuenum = CAST(@limitValue AS nvarchar)   
                                      SET @query = (@query)+
                                                    (N'UPDATE [${dbxschemaname}].contractactionlimit SET value = ')
                                                                          +
                                                                          (N'''')
                                                                          +
                                                                   (@limitValuenum)
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
                          		
                                IF (@limitId = '@' OR @limitValuenum = '@') AND (@existingActionRecords IS NULL OR @existingActionRecords = '')
                                    BEGIN
                                         SET @id = (SELECT left(newid(), 50))
                                         SET @query =(@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId, contractactionlimit.isNewAction) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''')+(@actionId) + (N''',''') +(@isNewAction) + (N''');')
                                   
                                    END
                                ELSE
                                    IF  @limitId != '@' AND  @limitValue is not null AND @tempLimitValue IS NOT NULL
                                      BEGIN
								         SET @id = (SELECT left(newid(), 50))
                                           SET @query = (@query) + (N'INSERT INTO [${dbxschemaname}].contractactionlimit(contractactionlimit.id,contractactionlimit.contractId,contractactionlimit.coreCustomerId,contractactionlimit.featureId,contractactionlimit.actionId,contractactionlimit.isNewAction,contractactionlimit.limitTypeId,contractactionlimit.value) VALUES (''') + (@id) + (N''',''') + (@contractId) + (N''',''') +(@customerId) + (N''',''') + (@featureId) + (N''',''') +(@actionId) + (N''',''')+ + (@isNewAction) + (N''',''')+ (@limitId) + (N''',''')+ (@limitValuenum) + (N''');')
                                     
                                      END
                                 
                    
                        END   
                 
              END
       END
       exec(@query)
   END
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].excludedcustomeraction;
GO
CREATE TABLE [${dbxschemaname}].excludedcustomeraction (
       id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
       RoleType_id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
       Customer_id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
       Action_id nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NULL,
       Account_id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       createdby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       modifiedby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       createdts datetime DEFAULT getdate() NOT NULL,
       lastmodifiedts datetime DEFAULT getdate() NOT NULL,
       synctimestamp datetime DEFAULT getdate() NOT NULL,
       softdeleteflag bit DEFAULT 0 NOT NULL,
       contractId varchar(45) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       coreCustomerId varchar(45) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       policyId varchar(45) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       featureId varchar(45) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       CONSTRAINT PK_excludedcustomeraction_id PRIMARY KEY (id,synctimestamp)
);
GO
DROP PROCEDURE IF EXISTS [${dbxschemaname}].excluded_customeraction_save_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].[excluded_customeraction_save_proc]  @_queryInput nvarchar(max)
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
                    SET @query = ('INSERT INTO [${dbxschemaname}].excludedcustomeraction(id,RoleType_id,Customer_id,coreCustomerId,contractId,featureId,action_id,account_id) VALUES (') + (@recordsData) + (N')')
                     EXEC(@query)
                     FETCH NEXT FROM actions into @recordRow
                     CONTINUE
        END
   END;
GO  

DROP PROCEDURE IF EXISTS [${dbxschemaname}].customer_contract_delete_proc;
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

CREATE TABLE [${dbxschemaname}].excludedcustomroleactionlimits (
       id bigint IDENTITY(1,1) NOT NULL,
       customRole_id bigint NOT NULL,
       action_id nvarchar(255) COLLATE SQL_Latin1_General_CP1_CI_AS NOT NULL,
       account_id nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       createdby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       modifiedby nvarchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       createdts datetime DEFAULT getdate() NOT NULL,
       lastmodifiedts datetime DEFAULT getdate() NOT NULL,
       synctimestamp datetime DEFAULT getdate() NOT NULL,
       softdeleteflag bit DEFAULT 0 NOT NULL,
       contractId varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       coreCustomerId varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       featureId varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       policyId varchar(50) COLLATE SQL_Latin1_General_CP1_CI_AS DEFAULT NULL NULL,
       CONSTRAINT PK_excludedcustomroleactionlimits_id PRIMARY KEY (id)
);
GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].excluded_customrole_actionlimits_create_proc;
GO 

CREATE PROCEDURE [${dbxschemaname}].[excluded_customrole_actionlimits_create_proc]  
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
                  SET @query = ('INSERT INTO [${dbxschemaname}].excludedcustomroleactionlimits(customRole_id,coreCustomerId,contractId,featureId,action_id,account_id) VALUES (') + (@recordsData) + (N')')
                             EXEC(@query)
               END
         END
   END;
GO
  
DROP PROCEDURE IF EXISTS [${dbxschemaname}].customrole_contract_delete_proc;
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
DROP PROCEDURE IF EXISTS [${dbxschemaname}].[fetch_restrictive_featureactionlimits_proc];
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
    SET @select_statement = CONCAT(@select_statement , ' FROM [${dbxschemaname}].feature
															LEFT JOIN [${dbxschemaname}].featuredisplaynamedescription ON ( featuredisplaynamedescription.Feature_id = feature.id)
															LEFT JOIN [${dbxschemaname}].featureaction ON (featureaction.Feature_id = feature.id)
                                                            LEFT JOIN [${dbxschemaname}].actiondisplaynamedescription ON (actiondisplaynamedescription.Action_id = featureaction.id)');
	IF(@_coreCustomerId != '') BEGIN 
		SET @select_statement = CONCAT(@select_statement , ' LEFT JOIN [${dbxschemaname}].contractactionlimit ON (contractactionlimit.actionId = featureaction.id)');
    END ;
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
END
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
        alertsubtype.externalSystem AS externalSystem
    FROM
        (((dbxalertcategory
        JOIN alertcategorychannel ON ((alertcategorychannel.AlertCategoryId = dbxalertcategory.id)))
        JOIN dbxalerttype ON ((dbxalerttype.AlertCategoryId = dbxalertcategory.id)))
        JOIN alertsubtype ON ((alertsubtype.AlertTypeId = dbxalerttype.id)))
GO
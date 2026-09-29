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


SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
ALTER PROCEDURE [${dbxschemaname}].[contract_users_details_get_proc]  
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
            [${dbxschemaname}].customercommunication.Type_id = 'COMM_TYPE_EMAIL')               
            LEFT JOIN [${dbxschemaname}].backendidentifier 
            ON ([${dbxschemaname}].backendidentifier.Customer_id = [${dbxschemaname}].customer.id AND 
                [${dbxschemaname}].backendidentifier.BackendType = @_backendType)
      WHERE 
        [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id, @customers) = '1'
   
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
        customer.Status_id AS Status_id,
		customer.CustomerType_id AS CustomerType_id
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
        customer.Status_id AS Status_id,
		customer.CustomerType_id AS CustomerType_id
    FROM [${dbxschemaname}].customer where [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customer.id ,@usersList) = '1'
		
	END
GO


CREATE TABLE [${dbxschemaname}].customrolesignatorygroup (
	customroleSignatoryGroupId nvarchar(50)  NOT NULL,
	signatoryGroupId nvarchar(50) DEFAULT NULL NULL,
	customRoleId bigint DEFAULT NULL NULL,
	createdby nvarchar(50) DEFAULT NULL NULL,
	modifiedby nvarchar(50) DEFAULT NULL NULL,
	createdts datetime2(7) DEFAULT getdate() NULL,
	lastmodifiedts datetime2(7) DEFAULT getdate() NULL,
	synctimestamp datetime2(7) DEFAULT getdate() NOT NULL,
	softdeleteflag bit DEFAULT '0' NOT NULL,
	CONSTRAINT PK__customrole__32D8CE2084E6FEC5 PRIMARY KEY (customroleSignatoryGroupId)
);
CREATE NONCLUSTERED INDEX FK_customrolesignatorygroup_customroleId ON [${dbxschemaname}].customrolesignatorygroup (customroleId);
CREATE NONCLUSTERED INDEX FK_customrolesignatorygroup_signaoryGroupId ON [${dbxschemaname}].customrolesignatorygroup (signatoryGroupId);

-- dbxdb.dbxdb.customersignatorygroup foreign keys

ALTER TABLE [${dbxschemaname}].customrolesignatorygroup ADD CONSTRAINT FK_customrolesignatorygroup_customerId FOREIGN KEY (customroleId) REFERENCES [${dbxschemaname}].customrole(id);
ALTER TABLE [${dbxschemaname}].customrolesignatorygroup ADD CONSTRAINT FK_customrolesignatorygroup_signaoryGroupId FOREIGN KEY (signatoryGroupId) REFERENCES [${dbxschemaname}].signatorygroup(signatoryGroupId);

GO

DROP PROCEDURE IF EXISTS [${dbxschemaname}].[user_limitgroup_limits_create_proc];
GO

SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [${dbxschemaname}].user_limitgroup_limits_create_proc  
   @_userId nvarchar(50),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50)
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
         @max_per_transaction_bulk_payment nvarchar(max) = N''
            
      DECLARE
         @max_daily_limit_single_payment nvarchar(max) = N''
           
      DECLARE
         @max_daily_limit_bulk_payment nvarchar(max) = N''
     
      DECLARE
         @max_weekly_limit_single_payment nvarchar(max) = N''
         
      DECLARE
         @max_weekly_limit_bulk_payment nvarchar(max) = N''   
         
     
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
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
      
      
      SET @max_per_transaction_bulk_payment = 
      
          (SELECT max(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_TRANSACTION_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
       
       SET @max_daily_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions ) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
               
               
        SET @max_daily_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_DAILY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
      
        SET @max_weekly_limit_single_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @singlePaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
               
          
         SET @max_weekly_limit_bulk_payment = 
      
          (SELECT sum(customeraction.value)
            FROM [${dbxschemaname}].customeraction
            WHERE 
               [${dbxschemaname}].customeraction.contractId = @_contractId AND 
               [${dbxschemaname}].customeraction.coreCustomerId = @_coreCustomerId AND 
               [${dbxschemaname}].customeraction.Customer_id = @_userId AND 
               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].customeraction.Action_id, @bulkPaymentsActions) = '1' AND 
               [${dbxschemaname}].customeraction.LimitType_id = 'AUTO_DENIED_WEEKLY_LIMIT' AND 
               [${dbxschemaname}].customeraction.Account_id <>'' AND
               [${dbxschemaname}].customeraction.value <> '')
          
         IF (@max_per_transaction_single_payment != 0) BEGIN
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_single_payment)
         END         
         IF (@max_per_transaction_bulk_payment != 0) BEGIN       
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'MAX_TRANSACTION_LIMIT', @max_per_transaction_bulk_payment)
         END    
         IF (@max_daily_limit_single_payment != 0) BEGIN                
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_single_payment)
         END      
         IF (@max_daily_limit_bulk_payment != 0) BEGIN                
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'DAILY_LIMIT', @max_daily_limit_bulk_payment)
         END       
         IF (@max_weekly_limit_single_payment != 0) BEGIN                
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'SINGLE_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_single_payment)
         END         
         IF (@max_weekly_limit_bulk_payment != 0) BEGIN             
         INSERT [${dbxschemaname}].customerlimitgrouplimits([${dbxschemaname}].customerlimitgrouplimits.id, [${dbxschemaname}].customerlimitgrouplimits.Customer_id, [${dbxschemaname}].customerlimitgrouplimits.contractId,[${dbxschemaname}].customerlimitgrouplimits.coreCustomerId,[${dbxschemaname}].customerlimitgrouplimits.limitGroupId,[${dbxschemaname}].customerlimitgrouplimits.LimitType_id,[${dbxschemaname}].customerlimitgrouplimits.value)
                             VALUES (left(newid(), 50), @_userId, @_contractId, @_coreCustomerId, 'BULK_PAYMENT', 'WEEKLY_LIMIT', @max_weekly_limit_bulk_payment)
		 END    

   END
GO

CREATE TABLE [${dbxschemaname}].eventtriggerconfiguration (
	id int  NOT NULL,
	service varchar(255) DEFAULT NULL NULL,
	classname varchar(45) DEFAULT NULL NULL,
	eventtype varchar(45) DEFAULT NULL NULL,
	eventsubtype varchar(45) DEFAULT NULL NULL,
	status varchar(45) DEFAULT NULL NULL,
	servicecall varchar(255) DEFAULT NULL NULL,
	hasfields varchar(255) DEFAULT NULL NULL,
	conditions varchar(255) DEFAULT NULL NULL,
	maskedfields varchar(255) DEFAULT NULL NULL,
	excludedfields varchar(255) DEFAULT NULL NULL,
	addFields text DEFAULT NULL NULL,
	passcustomerid varchar(45) DEFAULT NULL NULL,
	accountLevelField varchar(45) DEFAULT NULL NOT NULL,
	appid varchar(100) DEFAULT NULL NULL,
	isActive tinyint DEFAULT NULL,
	PRIMARY KEY (id)
);
GO


ALTER PROCEDURE [${dbxschemaname}].[customer_basic_info_proc]
	@_customerId nvarchar(50)
AS
	BEGIN
		SET XACT_ABORT ON
		SET NOCOUNT ON
		declare @accountLockoutThreshold nvarchar(max)
		declare @accountLockoutTime nvarchar(max)
		SET @accountLockoutThreshold = (SELECT accountLockoutThreshold from [${dbxschemaname}].passwordlockoutsettings)
		SET @accountLockoutTime =(SELECT accountLockoutTime from [${dbxschemaname}].passwordlockoutsettings)
		
		SELECT TOP (1)
			customer.UserName AS Username,
			customer.FirstName AS FirstName,
			customer.MiddleName AS MiddleName,
			customer.LastName AS LastName,
			(ISNULL(customer.FirstName, N'')) + (N' ') + (ISNULL(customer.MiddleName, N'')) + (N' ') + (ISNULL(customer.LastName, N'')) AS Name,
			customer.Salutation AS Salutation,
			customer.id AS Customer_id,
			customer.Ssn AS SSN,
			customer.createdts AS CustomerSince,
			customer.Gender AS Gender,
			customer.DateOfBirth AS DateOfBirth,
			customer.isEnrolledFromSpotlight AS isEnrolledFromSpotlight,
			CASE
				WHEN (customer.Status_id = 'SID_CUS_SUSPENDED') THEN customer.Status_id
			ELSE CASE
				WHEN (customer.lockCount + 1 >= @accountLockoutThreshold) THEN N'SID_CUS_LOCKED'
			ELSE customer.Status_id
			END
			END AS CustomerStatus_id,
			customerstatus.Description AS CustomerStatus_name,
			customer.MaritalStatus_id AS MaritalStatus_id,
			maritalstatus.Description AS MaritalStatus_name,
			customer.SpouseName AS SpouseName,
			customer.DrivingLicenseNumber AS DrivingLicenseNumber,
			customer.lockedOn AS lockedOn,
			customer.lockCount AS lockCount,
			customer.EmployementStatus_id AS EmployementStatus_id,employementstatus.Description AS EmployementStatus_name,
			( SELECT String_agg(CAST(Status_id as nvarchar(max)),',')
				FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id) ) AS CustomerFlag_ids,
			( SELECT String_agg(CAST(Description as nvarchar(max)),',')
				FROM [${dbxschemaname}].status
				WHERE status.id IN
			(
				SELECT customerflagstatus.Status_id
					FROM [${dbxschemaname}].customerflagstatus
				WHERE (customerflagstatus.Customer_id = customer.id)
			)) AS CustomerFlag,
			customer.IsEnrolledForOlb AS IsEnrolledForOlb,
			customer.isEnrolled AS isEnrolled,
			customer.IsStaffMember AS IsStaffMember,
			customer.Location_id AS Branch_id,
			location.Name AS Branch_name,
			location.Code AS Branch_code,
			customer.IsOlbAllowed AS IsOlbAllowed,
			customer.IsAssistConsented AS IsAssistConsented,
			customer.isEagreementSigned AS isEagreementSigned,
			customer.isCombinedUser AS isCombinedUser,
			ISNULL(customer.combinedUserId, N'') AS combinedUserId,
			IIF((customer.isCombinedUser = 1),N'TYPE_ID_RETAIL,TYPE_ID_BUSINESS',customer.CustomerType_id) AS CustomerType_id,
			IIF((customer.isCombinedUser = 1),N'Retail Banking,Business Banking',customertype.Name) AS CustomerType_Name,
			IIF((customer.isCombinedUser = 1),N'Retail and Business Banking User',customertype.Description) AS CustomerType_Description,
			customer.Organization_Id AS organisation_id,
			organisation.BusinessType_id AS BusinessType_id,
			businesstype.name AS BusinessType,
			organisation.Name AS organisation_name,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isPrimary = 1
			AND customercommunication.Customer_id = customer.id) AS PrimaryEmailAddress,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_PHONE' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryPhoneNumber,
			(SELECT customercommunication.Value FROM
			[${dbxschemaname}].customercommunication
			WHERE
			customercommunication.Type_id = 'COMM_TYPE_EMAIL' AND customercommunication.isTypeBusiness = '1'
			AND customercommunication.Customer_id = customer.id) AS BusinessPrimaryEmailAddress,
			customer.DocumentsSubmitted AS DocumentsSubmitted,
			customer.ApplicantChannel AS ApplicantChannel,
			customer.Product AS Product,
			customer.Reason AS Reason,
			@accountLockoutTime AS accountLockoutTime
		FROM ((((((([${dbxschemaname}].customer
		LEFT JOIN [${dbxschemaname}].location
		ON ((customer.Location_id = location.id)))
		LEFT JOIN [${dbxschemaname}].organisation
		ON ((customer.Organization_Id = organisation.id)))
		LEFT JOIN [${dbxschemaname}].businesstype ON ((businesstype.id = organisation.BusinessType_id)))
		INNER JOIN [${dbxschemaname}].customertype
		ON ((customer.CustomerType_id = customertype.id)))
		LEFT JOIN [${dbxschemaname}].status AS customerstatus
		ON ((customer.Status_id = customerstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS maritalstatus
		ON ((customer.MaritalStatus_id = maritalstatus.id)))
		LEFT JOIN [${dbxschemaname}].status AS employementstatus
		ON ((customer.EmployementStatus_id = employementstatus.id)))
		WHERE customer.id = @_customerId 
END
;
GO


DROP PROCEDURE IF EXISTS [${dbxschemaname}].default_autosync_accounts_create_proc;
GO
CREATE PROCEDURE [${dbxschemaname}].default_autosync_accounts_create_proc(
		@_customerId nvarchar(50),
        @_queryInput nvarchar(max)
    )AS BEGIN
        DECLARE  @index INT = 0
        DECLARE  @recordsData nvarchar(255) = N''
        DECLARE  @numOfRecords varchar(255) = N''
        DECLARE  @coreCustomerId varchar(255) = N''
        DECLARE  @accountId varchar(255) = N''
        DECLARE  @arrangementId varchar(255) = N''
        DECLARE  @accountName varchar(255) = N''
        DECLARE  @accountType varchar(255) = N''
        DECLARE  @contractId varchar(255) = N''
        DECLARE  @typeId varchar(255) = N''
        DECLARE  @contractaccounts varchar(255) = N''
		DECLARE  @customerAccounts varchar(255) = N''
        DECLARE  @id varchar(255) = N''
        SET @numOfRecords = datalength(@_queryInput) - datalength(replace(@_queryInput, N'|', N'')) + 1
         WHILE (1 = 1) BEGIN
              set @index = @index + 1;
              IF @index = @numOfRecords + 1 
                BREAK
              else BEGIN
                set @recordsData = ([${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@_queryInput, N'|', @index), N'|', -1));
                set @coreCustomerId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',1), ':', -1 );    
                set @accountId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',2), ':', -1 );
                set @arrangementId = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',3), ':', -1 );
                set @accountName = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',4), ':', -1 );
                set @accountType = [${dbxschemaname}].SUBSTRING_INDEX([${dbxschemaname}].SUBSTRING_INDEX(@recordsData, ':',5), ':', -1 );
                SET @contractId = (select [${dbxschemaname}].[contractcorecustomers].[contractId] from 
                [${dbxschemaname}].[contractcorecustomers] where [${dbxschemaname}].[contractcorecustomers].[coreCustomerId] = @coreCustomerId);
                SET @typeId = (select [${dbxschemaname}].[accounttype].[TypeID] from [${dbxschemaname}].[accounttype] where [${dbxschemaname}].[accounttype].[TypeDescription]= @accountType);
                
				
                SET @contractaccounts = (SELECT [${dbxschemaname}].[contractaccounts].[id] from [${dbxschemaname}].[contractaccounts] where [${dbxschemaname}].[contractaccounts].[coreCustomerId] = @coreCustomerId
                and [${dbxschemaname}].[contractaccounts].[accountId] = @accountId);
                if(ISNULL(@contractaccounts,'') = '') begin
                    SET @id = (SELECT left(newid(), 50))
	                    INSERT INTO [${dbxschemaname}].[contractaccounts](
                        [${dbxschemaname}].[contractaccounts].[id],
                        [${dbxschemaname}].[contractaccounts].[contractId],
                        [${dbxschemaname}].[contractaccounts].[accountId],
                        [${dbxschemaname}].[contractaccounts].[accountName],
                        [${dbxschemaname}].[contractaccounts].[typeId],
                        [${dbxschemaname}].[contractaccounts].[coreCustomerId],
                        [${dbxschemaname}].[contractaccounts].[ownerType],
                        [${dbxschemaname}].[contractaccounts].[statusDesc],
                        [${dbxschemaname}].[contractaccounts].[arrangementId])
                        VALUES (@id,@contractId,@accountId,@accountName,@typeId,@coreCustomerId,'Owner','Active',@arrangementId);
                end;

				SET @customerAccounts = (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] from [${dbxschemaname}].[customeraccounts]
											where [${dbxschemaname}].[customeraccounts].[Account_id] = @accountId);
				 if(ISNULL(@customerAccounts,'') = '') begin
					SET @id = (SELECT left(newid(), 50))
					INSERT INTO [${dbxschemaname}].customeraccounts(
					[${dbxschemaname}].[customeraccounts].[id],
					[${dbxschemaname}].[customeraccounts].[Customer_id],
					[${dbxschemaname}].[customeraccounts].[Account_id],
					[${dbxschemaname}].[customeraccounts].[AccountName],
					[${dbxschemaname}].[customeraccounts].[contractId],
					[${dbxschemaname}].[customeraccounts].[coreCustomerId],
					[${dbxschemaname}].[customeraccounts].[accountType])
					 VALUES (@id,@_customerId,@accountId,@accountName,@contractId,@coreCustomerId,@accountType);
					 EXEC [${dbxschemaname}].user_account_default_actions_create_proc @_customerId,@accountId,@coreCustomerId,@contractId,'';
					 END
                END
            END
    END
    GO
    
    ALTER PROCEDURE [${dbxschemaname}].user_account_default_actions_create_proc  
   @_userId nvarchar(50),
   @_accountsCSV nvarchar(max),
   @_coreCustomerId nvarchar(50),
   @_contractId nvarchar(50),
   @_groupId nvarchar(50)
AS 
   BEGIN

      SET  XACT_ABORT  ON
      SET  NOCOUNT  ON

      DECLARE
         @finished int = 0

      DECLARE
         @featureActionId varchar(255) = N''

      DECLARE
         @actionslist varchar(max) = N''

      DECLARE
         @limitId varchar(255) = N''

      DECLARE
         @entryStatus int = 0

      DECLARE
         @accountId varchar(255) = N''

      DECLARE
         @actualLimitId varchar(255) = N''
         
      DECLARE
         @serviceDefinitionId varchar(255) = N''
         
      DECLARE
         @serviceType varchar(255) = N''
         
      DECLARE
         @validFIActions varchar(max) = N''
         
      DECLARE
         @validServiceDefinitionActions varchar(max) = N''
         
      DECLARE
         @validGroupActions varchar(max) = N''
         
      DECLARE
         @validActionsList varchar(max) = N''
         
      DECLARE
         @groupId varchar(255) = N''
         
       DECLARE
         @featureId varchar(255) = N''

      DECLARE
         @limitvalue varchar(255) = N''

      DECLARE
         @id varchar(255) = N''
         
         
      DECLARE accounts CURSOR LOCAL
      FOR (SELECT [${dbxschemaname}].[customeraccounts].[Account_id] FROM [${dbxschemaname}].[customeraccounts] WHERE 
      [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
      [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId AND
      [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
      charindex(Account_id,@_accountsCSV)<>0);
      
      

    IF (
         CASE 
            WHEN @_accountsCSV IS NULL THEN 1
            ELSE 0
         END <> 0 OR @_accountsCSV = '')
         SET @_accountsCSV = 
            (						
            SELECT String_agg(CAST([${dbxschemaname}].[customeraccounts].[Account_id] AS nvarchar(max)), ',') FROM [${dbxschemaname}].[customeraccounts] WHERE 
                               [${dbxschemaname}].[customeraccounts].[Customer_id] = @_userId AND
                               [${dbxschemaname}].[customeraccounts].[contractId] = @_contractId AND
                               [${dbxschemaname}].[customeraccounts].[coreCustomerId] = @_coreCustomerId
             )
								
    SET @serviceDefinitionId = (SELECT [${dbxschemaname}].[contract].[servicedefinitionId] from [${dbxschemaname}].[contract] WHERE [${dbxschemaname}].[contract].[id] = @_contractId)
    SET @serviceType = (SELECT [${dbxschemaname}].[servicedefinition].[serviceType] from [${dbxschemaname}].[servicedefinition] WHERE [${dbxschemaname}].[servicedefinition].[id] = @serviceDefinitionId)  

     IF(CASE WHEN @_groupId IS NULL THEN 1 ELSE 0 END <> 0 OR @_groupId = '')
         SET @groupId =(SELECT [${dbxschemaname}].[groupservicedefinition].[Group_id] FROM [${dbxschemaname}].[groupservicedefinition]
                        WHERE [${dbxschemaname}].[groupservicedefinition].[serviceDefinitionId] = @serviceDefinitionId AND [${dbxschemaname}].[groupservicedefinition].[isDefaultGroup] = '1')
														
    SET @validFIActions =  (SELECT String_agg(CAST([${dbxschemaname}].featureaction.id AS nvarchar(max)), ',') FROM [${dbxschemaname}].featureaction)
																
    SET @validServiceDefinitionActions =  (SELECT String_agg(CAST([${dbxschemaname}].servicedefinitionactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].servicedefinitionactionlimit WHERE 
                               [${dbxschemaname}].servicedefinitionactionlimit.serviceDefinitionId = @serviceDefinitionId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].servicedefinitionactionlimit.actionId,@validFIActions)='1')
                                        
    SET @_groupId =  (SELECT [${dbxschemaname}].groupservicedefinition.Group_id FROM [${dbxschemaname}].groupservicedefinition where 
                             [${dbxschemaname}].groupservicedefinition.serviceDefinitionId = @serviceDefinitionId AND
                             [${dbxschemaname}].groupservicedefinition.Group_id = @groupId)  
											
    SET @validGroupActions =  (SELECT String_agg(CAST([${dbxschemaname}].groupactionlimit.Action_id AS nvarchar(max)), ',') FROM [${dbxschemaname}].groupactionlimit WHERE 
                               [${dbxschemaname}].groupactionlimit.Group_id = @_groupId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].groupactionlimit.Action_id,@validServiceDefinitionActions)='1')
												
    SET @validActionsList =  (SELECT String_agg(CAST([${dbxschemaname}].contractactionlimit.actionId AS nvarchar(max)), ',') FROM [${dbxschemaname}].contractactionlimit WHERE 
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].FIND_IN_SET([${dbxschemaname}].contractactionlimit.actionId,@validGroupActions)='1')
    
	DECLARE actions CURSOR LOCAL
      FOR (select id from [${dbxschemaname}].[featureaction] where charindex(id,@validActionsList)<>0 AND 
          ([${dbxschemaname}].[featureaction].[isAccountLevel] = '1' or [${dbxschemaname}].[featureaction].[isAccountLevel] = 'true'));

    OPEN accounts     
    FETCH NEXT FROM accounts into @accountId
      WHILE (@@FETCH_STATUS=0)
         BEGIN
    OPEN actions
    FETCH NEXT FROM actions into @featureActionId
	
      WHILE (@@FETCH_STATUS=0)
          BEGIN					
          SET @featureId = (SELECT [${dbxschemaname}].featureaction.Feature_id FROM [${dbxschemaname}].featureaction WHERE featureaction.id = @featureActionId );
          SET @entryStatus = 0
		  DECLARE limits CURSOR LOCAL
      FOR (select LimitType_id from [${dbxschemaname}].[actionlimit] where Action_id = @featureActionId);
    OPEN limits
    FETCH NEXT FROM limits into @limitId
      WHILE (@@FETCH_STATUS=0)
           BEGIN				
            SET @limitvalue = (SELECT [${dbxschemaname}].contractactionlimit.value FROM [${dbxschemaname}].contractactionlimit
                               WHERE [${dbxschemaname}].contractactionlimit.actionId = @featureActionId AND 
                               [${dbxschemaname}].contractactionlimit.limitTypeId = @limitId AND
                               [${dbxschemaname}].contractactionlimit.contractId = @_contractId AND
                               [${dbxschemaname}].contractactionlimit.coreCustomerId = @_coreCustomerId)

            IF (@limitId = 'MAX_TRANSACTION_LIMIT')
            SET @actualLimitId = N'AUTO_DENIED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'MIN_TRANSACTION_LIMIT')
            SET @actualLimitId = N'PRE_APPROVED_TRANSACTION_LIMIT'
            ELSE IF (@limitId = 'DAILY_LIMIT')
            BEGIN
               SET @actualLimitId = N'PRE_APPROVED_DAILY_LIMIT'
               SET @id = (SELECT left(newid(), 50))
               INSERT [${dbxschemaname}].customeraction([${dbxschemaname}].[customeraction].[id], 
                                             [${dbxschemaname}].[customeraction].[RoleType_id], 
                                             [${dbxschemaname}].[customeraction].[Customer_id], 
                                             [${dbxschemaname}].[customeraction].[contractId], 
                                             [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                             [${dbxschemaname}].[customeraction].[featureId], 
                                             [${dbxschemaname}].[customeraction].[Action_id], 
                                             [${dbxschemaname}].[customeraction].[Account_id], 
                                             [${dbxschemaname}].[customeraction].[isAllowed], 
                                             [${dbxschemaname}].[customeraction].[LimitType_id], 
                                             [${dbxschemaname}].[customeraction].[value])
                                             VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
               SET @actualLimitId = N'AUTO_DENIED_DAILY_LIMIT'

             END
             ELSE 
             BEGIN
             IF (@limitId = 'WEEKLY_LIMIT')
             BEGIN
             SET @actualLimitId = N'PRE_APPROVED_WEEKLY_LIMIT'
             SET @id = (SELECT left(newid(), 50))
             INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                           [${dbxschemaname}].[customeraction].[RoleType_id], 
                                           [${dbxschemaname}].[customeraction].[Customer_id], 
                                           [${dbxschemaname}].[customeraction].[contractId], 
                                           [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                           [${dbxschemaname}].[customeraction].[featureId],
                                           [${dbxschemaname}].[customeraction].[Action_id], 
                                           [${dbxschemaname}].[customeraction].[Account_id], 
                                           [${dbxschemaname}].[customeraction].[isAllowed], 
                                           [${dbxschemaname}].[customeraction].[LimitType_id], 
                                           [${dbxschemaname}].[customeraction].[value])
                                           VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,0.00)
             SET @actualLimitId = N'AUTO_DENIED_WEEKLY_LIMIT'
             END
             END
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@actualLimitId,@limitvalue)
                                      
        SET @id =(SELECT left(newid(), 50))
        INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                      [${dbxschemaname}].[customeraction].[RoleType_id], 
                                      [${dbxschemaname}].[customeraction].[Customer_id],
                                      [${dbxschemaname}].[customeraction].[contractId], 
                                      [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                      [${dbxschemaname}].[customeraction].[featureId], 
                                      [${dbxschemaname}].[customeraction].[Action_id], 
                                      [${dbxschemaname}].[customeraction].[Account_id], 
                                      [${dbxschemaname}].[customeraction].[isAllowed], 
                                      [${dbxschemaname}].[customeraction].[LimitType_id], 
                                      [${dbxschemaname}].[customeraction].[value])
                                      VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1,@limitId,@limitvalue) 
                                                                 
          SET @entryStatus = 1
          FETCH NEXT FROM limits into @limitId
          CONTINUE
          END
          CLOSE limits
          DEALLOCATE limits
          IF @entryStatus = 0
          BEGIN
          SET @id =(SELECT left(newid(), 50))
          INSERT [${dbxschemaname}].[customeraction]([${dbxschemaname}].[customeraction].[id], 
                                        [${dbxschemaname}].[customeraction].[RoleType_id], 
                                        [${dbxschemaname}].[customeraction].[Customer_id], 
                                        [${dbxschemaname}].[customeraction].[contractId], 
                                        [${dbxschemaname}].[customeraction].[coreCustomerId], 
                                        [${dbxschemaname}].[customeraction].[featureId], 
                                        [${dbxschemaname}].[customeraction].[Action_id], 
                                        [${dbxschemaname}].[customeraction].[Account_id], 
                                        [${dbxschemaname}].[customeraction].[isAllowed])
                                        VALUES (@id,@serviceType,@_userId,@_contractId,@_coreCustomerId,@featureId,@featureActionId,@accountId,1);
           END
           SET @actionslist = @featureActionId + N',' + @actionslist
           FETCH NEXT FROM actions into @featureActionId
           CONTINUE
           END
           CLOSE actions
           DEALLOCATE actions
           FETCH NEXT FROM accounts into @accountId
           CONTINUE
           END
           CLOSE accounts
           DEALLOCATE accounts
END;
GO